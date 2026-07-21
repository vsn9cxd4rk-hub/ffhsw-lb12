import { beforeAll, afterAll } from 'vitest';
import { prisma } from '../src/config/database';
import { signAccessToken } from '../src/config/jwt';

export let adminToken: string;

beforeAll(async () => {
  await prisma.$connect();
  adminToken = signAccessToken({
    userId: 1,
    username: 'admin',
    isAdmin: true,
    groupId: 1,
  });
});

afterAll(async () => {
  await prisma.$disconnect();
});

export function authHeader() {
  return { Authorization: `Bearer ${adminToken}` };
}
