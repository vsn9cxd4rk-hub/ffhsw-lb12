import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import bcrypt from 'bcryptjs';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader } from './setup';

// Initial and admin-reset passwords must be replaced before the app can be used.

const USERNAME = `mcp-test-${Date.now()}`;
const START_PASSWORD = 'StartPass123';
const NEW_PASSWORD = 'MeinNeues456';
let userId: number;

async function loginToken(password: string): Promise<{ token: string; user: { mustChangePassword: boolean } }> {
  const res = await request(app).post('/api/auth/login').send({ username: USERNAME, password });
  expect(res.status).toBe(200);
  return { token: res.body.data.accessToken, user: res.body.data.user };
}

describe('Passwortwechsel erzwingen', () => {
  beforeAll(async () => {
    const user = await prisma.user.create({
      data: { username: USERNAME, password: await bcrypt.hash(START_PASSWORD, 4), groupId: 3, mustChangePassword: true },
    });
    userId = user.id;
  });

  afterAll(async () => {
    await prisma.user.deleteMany({ where: { id: userId } });
  });

  it('Login meldet mustChangePassword, andere Endpunkte antworten 403', async () => {
    const { token, user } = await loginToken(START_PASSWORD);
    expect(user.mustChangePassword).toBe(true);

    const events = await request(app).get('/api/events').set('Authorization', `Bearer ${token}`);
    expect(events.status).toBe(403);
    expect(events.body.code).toBe('PASSWORD_CHANGE_REQUIRED');

    const me = await request(app).get('/api/auth/me').set('Authorization', `Bearer ${token}`);
    expect(me.status).toBe(200);
    expect(me.body.data.mustChangePassword).toBe(true);
  });

  it('Gleiches Passwort wird abgelehnt, neues Passwort hebt die Sperre auf', async () => {
    const { token } = await loginToken(START_PASSWORD);
    const same = await request(app).put('/api/auth/change-password').set('Authorization', `Bearer ${token}`)
      .send({ oldPassword: START_PASSWORD, newPassword: START_PASSWORD });
    expect(same.status).toBe(400);

    const ok = await request(app).put('/api/auth/change-password').set('Authorization', `Bearer ${token}`)
      .send({ oldPassword: START_PASSWORD, newPassword: NEW_PASSWORD });
    expect(ok.status).toBe(200);

    const after = await loginToken(NEW_PASSWORD);
    expect(after.user.mustChangePassword).toBe(false);
    const events = await request(app).get('/api/events').set('Authorization', `Bearer ${after.token}`);
    expect(events.status).toBe(200);
  });

  it('Passwort-Reset durch den Admin setzt das Flag wieder', async () => {
    const res = await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ password: 'ResetPass789' });
    expect(res.status).toBe(200);
    const user = await prisma.user.findUnique({ where: { id: userId } });
    expect(user?.mustChangePassword).toBe(true);
  });
});
