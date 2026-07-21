import { describe, it, expect } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { authHeader } from './setup';

describe('Authentifizierung', () => {
  it('POST /api/auth/login mit gültigen Daten gibt Token zurück', async () => {
    const res = await request(app)
      .post('/api/auth/login')
      .send({ username: 'admin', password: 'Admin123!' });

    expect(res.status).toBe(200);
    expect(res.body.data.accessToken).toBeDefined();
    expect(res.body.data.user.username).toBe('admin');
  });

  it('POST /api/auth/login mit falschem Passwort gibt 401', async () => {
    const res = await request(app)
      .post('/api/auth/login')
      .send({ username: 'admin', password: 'WrongPassword' });

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
    expect(res.body.data.username).toBe('admin');
  });

  it('GET /api/auth/me ohne Token gibt 401', async () => {
    const res = await request(app).get('/api/auth/me');
    expect(res.status).toBe(401);
  });
});
