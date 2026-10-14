import { describe, it, expect, beforeAll, afterAll, vi } from 'vitest';
import request from 'supertest';
import bcrypt from 'bcryptjs';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader, TEST_ADMIN_USERNAME } from './setup';

// Benutzerverwaltung: Schutzregeln für Administratoren und das Beenden von Sitzungen.

const USERNAME = `users-test-${process.pid}-${Date.now()}`;
const PASSWORD = 'UsersTest123';
let userId: number;

describe('Benutzerverwaltung', () => {
  beforeAll(async () => {
    const user = await prisma.user.create({
      data: { username: USERNAME, password: await bcrypt.hash(PASSWORD, 4), groupId: 3, isAdmin: true },
    });
    userId = user.id;
  });

  afterAll(async () => {
    await prisma.user.deleteMany({ where: { id: userId } });
  });

  it('Admin kann sich nicht selbst löschen', async () => {
    const self = await prisma.user.findUniqueOrThrow({ where: { username: TEST_ADMIN_USERNAME } });
    const res = await request(app).delete(`/api/users/${self.id}`).set(authHeader());
    expect(res.status).toBe(400);
    expect(await prisma.user.count({ where: { id: self.id } })).toBe(1);
  });

  it('Benutzer ohne E-Mail kann angelegt werden, eine ungültige E-Mail wird abgelehnt', async () => {
    // Das Formular schickt ein leeres Feld als email: ''
    const username = `${USERNAME}-ohne-mail`;
    try {
      const res = await request(app).post('/api/users').set(authHeader())
        .send({ username, email: '', password: 'OhneMail123', groupId: 3 });
      expect(res.status).toBe(201);
      expect(res.body.data.email).toBeNull();

      const invalid = await request(app).post('/api/users').set(authHeader())
        .send({ username: `${username}-2`, email: 'keine-mail', password: 'OhneMail123', groupId: 3 });
      expect(invalid.status).toBe(422);
    } finally {
      await prisma.user.deleteMany({ where: { username: { startsWith: username } } });
    }
  });

  it('Berechtigungsgruppe darf nicht geleert werden', async () => {
    const res = await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ groupId: null });
    expect(res.status).toBe(400);
  });

  it('der letzte aktive Administrator kann nicht herabgestuft oder deaktiviert werden', async () => {
    // Weitere Admins gibt es in der Test-DB immer; hier wird "keine weiteren" simuliert
    const count = vi.spyOn(prisma.user, 'count').mockResolvedValue(0);
    try {
      const demote = await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ isAdmin: false });
      expect(demote.status).toBe(400);
      const deactivate = await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ isActive: false });
      expect(deactivate.status).toBe(400);
    } finally {
      count.mockRestore();
    }
    const user = await prisma.user.findUniqueOrThrow({ where: { id: userId } });
    expect(user.isAdmin).toBe(true);
    expect(user.isActive).toBe(true);
  });

  it('Herabstufen ist erlaubt, solange es weitere Administratoren gibt', async () => {
    const res = await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ isAdmin: false });
    expect(res.status).toBe(200);
    expect(res.body.data.isAdmin).toBe(false);
  });

  it('Deaktivieren beendet laufende Sitzungen', async () => {
    const login = await request(app).post('/api/auth/login').send({ username: USERNAME, password: PASSWORD });
    expect(login.status).toBe(200);
    const cookies = login.headers['set-cookie'];

    const before = await request(app).post('/api/auth/refresh').set('Cookie', cookies).send({});
    expect(before.status).toBe(200);

    const res = await request(app).put(`/api/users/${userId}`).set(authHeader()).send({ isActive: false });
    expect(res.status).toBe(200);

    const after = await request(app).post('/api/auth/refresh').set('Cookie', before.headers['set-cookie'] ?? cookies).send({});
    expect(after.status).toBe(401);
  });
});
