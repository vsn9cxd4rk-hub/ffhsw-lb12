import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import bcrypt from 'bcryptjs';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader, TEST_ADMIN_USERNAME } from './setup';

// Own fixture user: the tests must not depend on the real admin's password
const USERNAME = `auth-test-${Date.now()}`;
const PASSWORD = 'AuthTest123';

describe('Authentifizierung', () => {
  beforeAll(async () => {
    await prisma.user.create({ data: { username: USERNAME, password: await bcrypt.hash(PASSWORD, 4), groupId: 3 } });
  });

  afterAll(async () => {
    await prisma.user.deleteMany({ where: { username: USERNAME } });
  });

  it('POST /api/auth/login mit gültigen Daten gibt Token zurück', async () => {
    const res = await request(app)
      .post('/api/auth/login')
      .send({ username: USERNAME, password: PASSWORD });

    expect(res.status).toBe(200);
    expect(res.body.data.accessToken).toBeDefined();
    expect(res.body.data.user.username).toBe(USERNAME);
  });

  it('POST /api/auth/login mit falschem Passwort gibt 401', async () => {
    const res = await request(app)
      .post('/api/auth/login')
      .send({ username: USERNAME, password: 'WrongPassword' });

    expect(res.status).toBe(401);
  });

  it('POST /api/auth/login mit unbekanntem User gibt 401', async () => {
    const res = await request(app)
      .post('/api/auth/login')
      .send({ username: 'nonexistent', password: 'anything' });

    expect(res.status).toBe(401);
  });

  it('GET /api/auth/me mit gültigem Token gibt User zurück', async () => {
    const res = await request(app)
      .get('/api/auth/me')
      .set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.data.username).toBe(TEST_ADMIN_USERNAME);
  });

  it('GET /api/auth/me ohne Token gibt 401', async () => {
    const res = await request(app).get('/api/auth/me');
    expect(res.status).toBe(401);
  });
});
