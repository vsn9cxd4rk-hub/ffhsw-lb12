import jwt from 'jsonwebtoken';

const ACCESS_EXPIRES = process.env.JWT_ACCESS_EXPIRES || '15m';
const REFRESH_EXPIRES = process.env.JWT_REFRESH_EXPIRES || '7d';

const MIN_SECRET_LENGTH = 32;
// Values shipped in .env.example files and earlier code fallbacks
const KNOWN_PLACEHOLDERS = [
  'fallback-access-secret-change-me',
  'your-access-secret-change-in-production-min-32-chars',
  'your-refresh-secret-change-in-production-min-32-chars',
];

/**
 * Returns a reason why the access-token secret is unusable, or null. Without a strong secret
 * anybody who knows the default could sign admin tokens, so the server refuses to start.
 */
export function jwtSecretProblem(secret = process.env.JWT_ACCESS_SECRET): string | null {
  if (!secret) return 'JWT_ACCESS_SECRET ist nicht gesetzt';
  if (secret.length < MIN_SECRET_LENGTH) return `JWT_ACCESS_SECRET ist kürzer als ${MIN_SECRET_LENGTH} Zeichen`;
  const lower = secret.toLowerCase();
  if (KNOWN_PLACEHOLDERS.includes(secret) || lower.includes('change-me') || lower.includes('change-in-production')) {
    return 'JWT_ACCESS_SECRET ist ein Platzhalterwert';
  }
  return null;
}

function accessSecret(): string {
  const problem = jwtSecretProblem();
  if (problem) throw new Error(problem);
  return process.env.JWT_ACCESS_SECRET as string;
}

export interface JwtPayload {
  userId: number;
  username: string;
  isAdmin: boolean;
  groupId: number | null;
}

export function signAccessToken(payload: JwtPayload): string {
  return jwt.sign(payload, accessSecret(), { expiresIn: ACCESS_EXPIRES } as jwt.SignOptions);
}

export function verifyAccessToken(token: string): JwtPayload {
  return jwt.verify(token, accessSecret()) as JwtPayload;
}

// Refresh tokens are opaque random values stored (hashed) in the DB, not JWTs;
// JWT_REFRESH_EXPIRES only sets their lifetime in days.
export function getRefreshTokenExpiry(): Date {
  const days = parseInt(REFRESH_EXPIRES.replace('d', ''), 10) || 7;
  const expiry = new Date();
  expiry.setDate(expiry.getDate() + days);
  return expiry;
}
