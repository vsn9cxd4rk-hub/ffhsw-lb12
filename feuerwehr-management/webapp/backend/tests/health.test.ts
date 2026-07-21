import { describe, it, expect } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';

describe('Health & Infrastruktur', () => {
  it('GET /api/health gibt Status OK', async () => {
    const res = await request(app).get('/api/health');
    expect(res.status).toBe(200);
    expect(res.body.status).toBe('ok');
    expect(res.body.timestamp).toBeDefined();
  });

  it('Datenbank ist erreichbar', async () => {
    const result = await prisma.$queryRaw`SELECT 1 as alive`;
    expect(result).toBeDefined();
  });

  it('404 für unbekannte Routen', async () => {
    const res = await request(app).get('/api/nonexistent-route');
    expect(res.status).toBe(404);
  });

  it('401 für geschützte Routen ohne Token', async () => {
    const res = await request(app).get('/api/members');
    expect(res.status).toBe(401);
  });
});
