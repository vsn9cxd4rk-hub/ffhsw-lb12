import fs from 'fs';
import path from 'path';
import { describe, it, expect } from 'vitest';
import { JSON_FIELDS, pickWritable } from '../src/utils/body';

// pickWritable drops object values except on Json columns. Prisma 7 no longer exposes the
// field types at runtime, so the Json columns are listed in body.ts; keep them in sync.

describe('pickWritable – Feldliste aus Prisma 7', () => {
  it('JSON_FIELDS entspricht den Json-Spalten in schema.prisma', () => {
    const schema = fs.readFileSync(path.join(__dirname, '../prisma/schema.prisma'), 'utf8');
    const found = new Set<string>();
    let model = '';
    for (const line of schema.split('\n')) {
      const m = line.match(/^model (\w+) \{/);
      if (m) model = m[1];
      const f = line.match(/^\s+(\w+)\s+Json\??(\s|$)/);
      if (f && model) found.add(`${model}.${f[1]}`);
    }
    expect([...found].sort()).toEqual([...JSON_FIELDS].sort());
  });

  it('übernimmt eigene Spalten, verwirft Relationen, IDs und Objekte', () => {
    const out = pickWritable('Event', {
      id: 99, name: 'Übung', category: 5, createdAt: '2020-01-01',
      attendances: { create: { memberId: 1 } },
      bswData: { checklist: { item1: 'ja' } },
      notes: { set: 'x' },
    });
    expect(out).toEqual({ name: 'Übung', category: 5, bswData: { checklist: { item1: 'ja' } } });
  });

  it('Fremdschlüssel bleiben beschreibbar, außer sie stehen in omit', () => {
    expect(pickWritable('OperationTime', { operationId: 1, vehicleName: 'LF' }, ['operationId']))
      .toEqual({ vehicleName: 'LF' });
    expect(pickWritable('Article', { warehouseId: 3 })).toEqual({ warehouseId: 3 });
  });
});
