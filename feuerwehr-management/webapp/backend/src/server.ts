import 'dotenv/config';
import app from './app';
import { logger } from './utils/logger';
import { prisma } from './config/database';
import { runDataMigrations } from './services/data-migrations.service';
import { jwtSecretProblem } from './config/jwt';

const PORT = parseInt(process.env.PORT || '3001', 10);

async function startServer() {
  const secretProblem = jwtSecretProblem();
  if (secretProblem) {
    logger.error(`Start abgebrochen: ${secretProblem}. Bitte in backend/.env einen Zufallswert setzen, z.B. "openssl rand -base64 48".`);
    process.exit(1);
  }

  try {
    await prisma.$connect();
    logger.info('Database connected successfully');

    await runDataMigrations();

    const server = app.listen(PORT, () => {
      logger.info(`Server running on port ${PORT} in ${process.env.NODE_ENV || 'development'} mode`);
    });

    // Graceful shutdown
    const shutdown = async (signal: string) => {
      logger.info(`${signal} received - shutting down gracefully`);
      server.close(async () => {
        await prisma.$disconnect();
        logger.info('Database disconnected');
        process.exit(0);
      });
      setTimeout(() => {
        logger.error('Forced shutdown after timeout');
        process.exit(1);
      }, 10000);
    };

    process.on('SIGTERM', () => shutdown('SIGTERM'));
    process.on('SIGINT', () => shutdown('SIGINT'));
  } catch (error) {
    logger.error('Failed to start server:', error);
    process.exit(1);
  }
}

startServer();
