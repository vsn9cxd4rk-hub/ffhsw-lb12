import express, { Request } from 'express';
import helmet from 'helmet';
import cors from 'cors';
import compression from 'compression';
import cookieParser from 'cookie-parser';
import morgan from 'morgan';
import rateLimit from 'express-rate-limit';
import { errorHandler } from './middleware/error.middleware';
import router from './routes';
import { logger } from './utils/logger';
import { verifyAccessToken } from './config/jwt';

const app = express();

// Trust proxy (Nginx forwards requests)
app.set('trust proxy', 1);

// Security headers for API responses (the SPA's headers come from nginx, see
// deploy/nginx/security-headers.conf). The frontend calls the API same-origin via the proxy.
// The default install serves plain HTTP in the LAN: HSTS and upgrade-insecure-requests are
// left to the TLS-terminating nginx where HTTPS is actually set up.
app.use(helmet({
  crossOriginResourcePolicy: { policy: 'same-origin' },
  strictTransportSecurity: false,
  contentSecurityPolicy: { directives: { upgradeInsecureRequests: null } },
}));

// CORS
const allowedOrigins = (process.env.CORS_ORIGIN || 'http://localhost:3000').split(',').map(s => s.trim());
app.use(cors({
  origin: (origin, callback) => {
    // Allow requests with no origin (same-origin via nginx proxy, server-to-server, mobile apps)
    if (!origin) return callback(null, true);
    // Allow configured origins
    if (allowedOrigins.includes(origin) || allowedOrigins.includes('*')) return callback(null, true);
    callback(new Error('Not allowed by CORS'));
  },
  credentials: true,
  methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'OPTIONS'],
  allowedHeaders: ['Content-Type', 'Authorization'],
}));

// Compression
app.use(compression());

// Body parsing
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// Cookie parser
app.use(cookieParser());

// HTTP logging
app.use(morgan('combined', {
  stream: { write: (message: string) => logger.http(message.trim()) },
}));

// Rate limiting
// Several users of one Feuerwehrhaus usually share a single public IP (NAT), so
// authenticated traffic is counted per user; only anonymous requests fall back to the IP.
// The counters are in-memory: fine for the single PM2 fork process, a shared store
// (Redis/MySQL) is needed before running more than one backend instance.
function userOrIpKey(req: Request): string {
  const auth = req.headers.authorization;
  if (auth?.startsWith('Bearer ')) {
    try {
      return `user:${verifyAccessToken(auth.substring(7)).userId}`;
    } catch {
      // expired/invalid token: count against the IP
    }
  }
  return `ip:${req.ip}`;
}

if (process.env.NODE_ENV !== 'development') {
  const generalLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: (req) => (userOrIpKey(req).startsWith('user:') ? 2000 : 300),
    keyGenerator: userOrIpKey,
    standardHeaders: true,
    legacyHeaders: false,
    message: { error: 'Zu viele Anfragen, bitte später versuchen.' },
  });

  // Only failed logins count, so colleagues logging in from the same IP don't lock each
  // other out; brute force is limited per account and per IP.
  const loginPerAccountLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 10,
    skipSuccessfulRequests: true,
    keyGenerator: (req) => `${req.ip}:${String(req.body?.username ?? '').trim().toLowerCase()}`,
    standardHeaders: true,
    legacyHeaders: false,
    message: { error: 'Zu viele Anmeldeversuche, bitte 15 Minuten warten.' },
  });

  const loginPerIpLimiter = rateLimit({
    windowMs: 15 * 60 * 1000,
    max: 50,
    skipSuccessfulRequests: true,
    standardHeaders: true,
    legacyHeaders: false,
    message: { error: 'Zu viele Anmeldeversuche, bitte 15 Minuten warten.' },
  });

  app.use('/api/', generalLimiter);
  app.use('/api/auth/login', loginPerIpLimiter, loginPerAccountLimiter);
}

// Health check
app.get('/api/health', (_req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

// API routes
app.use('/api', router);

// 404 handler
app.use((_req, res) => {
  res.status(404).json({ error: 'Route nicht gefunden' });
});

// Error handler
app.use(errorHandler);

export default app;
