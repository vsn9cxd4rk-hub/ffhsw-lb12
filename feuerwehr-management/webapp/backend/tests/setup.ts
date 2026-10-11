import { beforeAll, afterAll } from 'vitest';
import { prisma } from '../src/config/database';
import { signAccessToken } from '../src/config/jwt';

export let adminToken: string;
export const TEST_ADMIN_USERNAME = `test-admin-${process.pid}-${Date.now()}`;

// Own admin fixture per test file: the tests must not depend on the state of the real
// "admin" account (e.g. mustChangePassword after an update, or a changed password).
beforeAll(async () => {
  await prisma.$connect();
  const admin = await prisma.user.create({
    data: { username: TEST_ADMIN_USERNAME, password: 'x', name: 'Test-Admin', isAdmin: true, groupId: 1 },
  });
  adminToken = signAccessToken({
    userId: admin.id,
    username: admin.username,
    isAdmin: true,
    groupId: 1,
  });
});

afterAll(async () => {
  await prisma.user.deleteMany({ where: { username: TEST_ADMIN_USERNAME } });
  await prisma.$disconnect();
});

export function authHeader() {
  return { Authorization: `Bearer ${adminToken}` };
}
