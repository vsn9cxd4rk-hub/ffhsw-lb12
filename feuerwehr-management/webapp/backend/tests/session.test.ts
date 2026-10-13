import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import bcrypt from 'bcryptjs';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { hashRefreshToken } from '../src/services/auth.service';
import { authHeader } from './setup';

// Session handling: refresh via httpOnly cookie, logout, revocation by an admin.

const USERNAME = `session-test-${Date.now()}`;
const PASSWORD = 'Session123!';
let userId: number;

function refreshCookieOf(res: request.Response): string {
  const cookies = ([] as string[]).concat(res.headers['set-cookie'] ?? []);
  const cookie = cookies.find((c) => c.startsWith('refreshToken='));
  if (!cookie) throw new Error('no refreshToken cookie set');
  return cookie.split(';')[0];
}

async function login() {
  const res = await request(app).post('/api/auth/login').send({ username: USERNAME, password: PASSWORD });
  expect(res.status).toBe(200);
  return { res, cookie: refreshCookieOf(res) };
}

describe('Session-Handling', () => {
  beforeAll(async () => {
    const user = await prisma.user.create({
      data: { username: USERNAME, password: await bcrypt.hash(PASSWORD, 4), groupId: 3 },
    });
    userId = user.id;
  });

  afterAll(async () => {
    await prisma.user.deleteMany({ where: { id: userId } });
  });

  it('Login setzt ein httpOnly-Cookie und speichert nur den Hash des Refresh-Tokens', async () => {
    const { res, cookie } = await login();
    const setCookie = ([] as string[]).concat(res.headers['set-cookie']).join(';');
    expect(setCookie).toMatch(/HttpOnly/i);
    expect(setCookie).toMatch(/Path=\/api\/auth/);

    const raw = cookie.substring('refreshToken='.length);
    expect(await prisma.refreshToken.findUnique({ where: { token: raw } })).toBeNull();
    expect(await prisma.refreshToken.findUnique({ where: { token: hashRefreshToken(raw) } })).not.toBeNull();
  });

  it('POST /api/auth/refresh liefert neuen Access-Token und aktuellen Benutzer', async () => {
    const { cookie } = await login();
    const res = await request(app).post('/api/auth/refresh').set('Cookie', cookie);

    expect(res.status).toBe(200);
    expect(res.body.data.accessToken).toBeDefined();
    expect(res.body.data.user.username).toBe(USERNAME);
    expect(res.body.data.user.permissions).toBeDefined();

    const me = await request(app).get('/api/auth/me').set('Authorization', `Bearer ${res.body.data.accessToken}`);
    expect(me.status).toBe(200);
  });

  it('Mehrere parallele Refreshes desselben Cookies (mehrere Tabs) sind alle erfolgreich', async () => {
    const { cookie } = await login();
    const results = await Promise.all(
      [1, 2, 3].map(() => request(app).post('/api/auth/refresh').set('Cookie', cookie)),
    );
    expect(results.map((r) => r.status)).toEqual([200, 200, 200]);
  });

  it('Refresh verlängert die Sitzung (gleitender Ablauf) und erneuert das Cookie', async () => {
    const { cookie } = await login();
    const tokenHash = hashRefreshToken(cookie.substring('refreshToken='.length));
    // Session started long ago and is about to expire
    const soon = new Date(Date.now() + 60_000);
    await prisma.refreshToken.update({ where: { token: tokenHash }, data: { expiresAt: soon } });

    const res = await request(app).post('/api/auth/refresh').set('Cookie', cookie);
    expect(res.status).toBe(200);

    const stored = await prisma.refreshToken.findUnique({ where: { token: tokenHash } });
    expect(stored!.expiresAt.getTime()).toBeGreaterThan(Date.now() + 24 * 3600_000);
    // Same token value, only the expiry moves
    expect(refreshCookieOf(res)).toBe(cookie);
    expect(([] as string[]).concat(res.headers['set-cookie']).join(';')).toMatch(/Expires=/i);
  });

  it('Abgelaufene Sitzung wird nicht mehr verlängert', async () => {
    const { cookie } = await login();
    const tokenHash = hashRefreshToken(cookie.substring('refreshToken='.length));
    await prisma.refreshToken.update({ where: { token: tokenHash }, data: { expiresAt: new Date(Date.now() - 1000) } });

    const res = await request(app).post('/api/auth/refresh').set('Cookie', cookie);
    expect(res.status).toBe(401);
    expect(await prisma.refreshToken.findUnique({ where: { token: tokenHash } })).toBeNull();
  });

  it('Mehrere Logins (Geräte) desselben Benutzers sind unabhängige Sitzungen', async () => {
    const a = await login();
    const b = await login();
    await request(app).post('/api/auth/logout').set('Cookie', a.cookie);

    expect((await request(app).post('/api/auth/refresh').set('Cookie', a.cookie)).status).toBe(401);
    expect((await request(app).post('/api/auth/refresh').set('Cookie', b.cookie)).status).toBe(200);
  });

  it('Logout funktioniert auch ohne (abgelaufenen) Access-Token', async () => {
    const { cookie } = await login();
    const res = await request(app).post('/api/auth/logout').set('Cookie', cookie);
    expect(res.status).toBe(200);

    const refresh = await request(app).post('/api/auth/refresh').set('Cookie', cookie);
    expect(refresh.status).toBe(401);
  });

  it('Refresh mit unbekanntem Token gibt 401', async () => {
    const res = await request(app).post('/api/auth/refresh').set('Cookie', 'refreshToken=deadbeef');
    expect(res.status).toBe(401);
  });

  it('Passwort-Reset durch Admin beendet bestehende Sitzungen', async () => {
    const { cookie } = await login();
    const reset = await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ password: PASSWORD });
    expect(reset.status).toBe(200);

    const refresh = await request(app).post('/api/auth/refresh').set('Cookie', cookie);
    expect(refresh.status).toBe(401);
  });

  it('Deaktivierung beendet Sitzungen und sperrt den Access-Token', async () => {
    const { res, cookie } = await login();
    const accessToken = res.body.data.accessToken;

    await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ isActive: false });

    expect((await request(app).post('/api/auth/refresh').set('Cookie', cookie)).status).toBe(401);
    expect((await request(app).get('/api/auth/me').set('Authorization', `Bearer ${accessToken}`)).status).toBe(401);

    await prisma.user.update({ where: { id: userId }, data: { isActive: true } });
  });

  it('Rechteänderungen kommen beim nächsten Refresh ohne neuen Login an', async () => {
    const { cookie } = await login();
    await prisma.user.update({ where: { id: userId }, data: { groupId: 2 } });

    const res = await request(app).post('/api/auth/refresh').set('Cookie', cookie);
    expect(res.body.data.user.groupId).toBe(2);

    await prisma.user.update({ where: { id: userId }, data: { groupId: 3 } });
  });

  it('Der letzte aktive Admin kann sich nicht herabstufen', async () => {
    const admins = await prisma.user.findMany({ where: { isAdmin: true, isActive: true }, select: { id: true } });
    if (admins.length !== 1) return; // only meaningful with exactly one admin in the DB
    const res = await request(app).put(`/api/users/${admins[0].id}`).set(authHeader()).send({ isAdmin: false });
    expect(res.status).toBe(400);
  });
});
