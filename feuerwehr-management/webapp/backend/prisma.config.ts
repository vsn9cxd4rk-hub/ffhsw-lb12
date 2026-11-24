// Prisma 7 CLI configuration (generate, db push, db execute, studio).
// Prisma no longer reads .env itself, hence dotenv.
import 'dotenv/config';
import { defineConfig, env } from 'prisma/config';

export default defineConfig({
  schema: 'prisma/schema.prisma',
  datasource: {
    url: env('DATABASE_URL'),
  },
});
