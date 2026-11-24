import { PrismaMariaDb } from '@prisma/adapter-mariadb';
import { PrismaClient } from '../generated/prisma/client';

declare global {
  // eslint-disable-next-line no-var
  var __prisma: PrismaClient | undefined;
}

// Prisma 7 talks to MySQL/MariaDB through the mariadb driver (driver adapter) instead of
// the Rust query engine. The URL keeps the mysql:// form used in .env and docker-compose.
function createClient(): PrismaClient {
  const url = process.env.DATABASE_URL;
  if (!url) throw new Error('DATABASE_URL ist nicht gesetzt');
  return new PrismaClient({
    adapter: new PrismaMariaDb(url),
    log: process.env.NODE_ENV === 'development' ? ['query', 'error', 'warn'] : ['error'],
  });
}

export const prisma = global.__prisma || createClient();

if (process.env.NODE_ENV !== 'production') {
  global.__prisma = prisma;
}
