import { describe, it, expect } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { nextInventoryNumber } from '../src/utils/inventory-number';
import { authHeader } from './setup';

// The suggestion followed the highest number found anywhere: a manufacturer number like
// "23642003365" pushed it out of the own numbering, and "LB12-0045" counted as 12.

describe('nächste Inventarnummer', () => {
  it('folgt der eigenen Nummernfolge, nicht Hersteller- oder Seriennummern', () => {
    expect(nextInventoryNumber([
      '120001', '121006', '120500', '23642003365', '23642003364', '1015115',
      '56421-021', '2022IV.23/1758/004', '1PR002508GKA3Y7', null,
    ])).toBe('121007');
  });

  it('behält führende Nullen', () => {
    expect(nextInventoryNumber(['0041', '0042', '0099'])).toBe('0100');
  });

  it('beginnt bei 1 ohne numerische Inventarnummern', () => {
    expect(nextInventoryNumber([])).toBe('1');
    expect(nextInventoryNumber(['LB12-0045', null])).toBe('1');
  });

  it('Endpunkt liefert einen rein numerischen Vorschlag', async () => {
    const res = await request(app).get('/api/inventory/articles/next-number').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body.data.next).toMatch(/^\d+$/);
  });
});
