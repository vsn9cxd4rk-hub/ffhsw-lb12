import { describe, it, expect, beforeAll, afterAll, vi } from 'vitest';
import fs from 'fs';
import ExcelJS from 'exceljs';
import { prisma } from '../src/config/database';
import { recordUploadDir, removeUpload, resolveUploadPath } from '../src/utils/files';
import { templateState } from './templates';

// The Kräftenachweis form has 9 rows per section. Everybody from position 10 on was dropped
// without any message; they now go onto follow-up sheets.

// No LibreOffice: the generator then delivers the xlsx, which can be read back here.
vi.mock('child_process', async (importOriginal) => {
  const actual = await importOriginal<typeof import('child_process')>();
  return {
    ...actual,
    execFile: vi.fn((_cmd: string, _args: string[], _opts: unknown, cb: (err: Error | null) => void) => {
      cb(new Error('kein LibreOffice im Test'));
    }),
  };
});

const STAMP = Date.now();
const DEPLOYED = 11;
const REINFORCEMENT = 2;

let operationId: number;
const memberIds: number[] = [];

async function names(storedPath: string, firstRow: number): Promise<string[]> {
  const workbook = new ExcelJS.Workbook();
  await workbook.xlsx.readFile(resolveUploadPath(storedPath) as string);
  const sheet = workbook.getWorksheet(1)!;
  const result: string[] = [];
  for (let row = firstRow; row < firstRow + 9; row++) {
    const value = sheet.getCell(`B${row}`).value;
    if (value) result.push(String(value));
  }
  return result;
}

describe('Kräftenachweis mit mehr als 9 Kräften', () => {
  beforeAll(async () => {
    const op = await prisma.operation.create({
      data: { date: new Date('2026-07-21'), location: `Folgeblatt-Test ${STAMP}`, keyword: 'Brand 3' },
    });
    operationId = op.id;

    for (let i = 1; i <= DEPLOYED + REINFORCEMENT; i++) {
      const member = await prisma.member.create({
        data: { firstName: 'Kraft', lastName: `K${String(i).padStart(2, '0')}-${STAMP}` },
      });
      memberIds.push(member.id);
      await prisma.operationPersonnel.create({
        data: {
          operationId, memberId: member.id, vehicleName: '', function: 'Truppmann',
          section: i <= DEPLOYED ? 'deployed' : 'reinforcement',
        },
      });
    }
  });

  afterAll(async () => {
    const documents = await prisma.operationDocument.findMany({ where: { operationId } });
    for (const doc of documents) removeUpload(doc.filePath);
    fs.rmSync(recordUploadDir('operations', String(operationId)), { recursive: true, force: true });
    await prisma.operation.delete({ where: { id: operationId } }).catch(() => {});
    await prisma.member.deleteMany({ where: { id: { in: memberIds } } });
  });

  it('verteilt die Kräfte auf Folgeblätter, ohne jemanden wegzulassen', async () => {
    // Skip ohne nutzbares Template; fehlende Dateien meldet backup-integrity.test.ts
    if ((await templateState('Kräftenachweis')) !== 'present') return;

    const { generatePersonnelSheets } = await import('../src/services/report-generator.service');
    const files = await generatePersonnelSheets(operationId);

    expect(files).toHaveLength(2);
    expect(files[0].fileName).not.toContain('_Blatt');
    expect(files[1].fileName).toContain('_Blatt2');

    const deployed = [...await names(files[0].filePath, 12), ...await names(files[1].filePath, 12)];
    expect(deployed).toHaveLength(DEPLOYED);
    expect(deployed[0]).toBe(`K01-${STAMP}, Kraft`);
    expect(deployed[9]).toBe(`K10-${STAMP}, Kraft`);
    expect(deployed[10]).toBe(`K11-${STAMP}, Kraft`);

    // Verstärkung passt auf das erste Blatt, das Folgeblatt bleibt dort leer
    expect(await names(files[0].filePath, 24)).toHaveLength(REINFORCEMENT);
    expect(await names(files[1].filePath, 24)).toHaveLength(0);
  });

  it('bleibt bei bis zu 9 Kräften bei einem Blatt', async () => {
    if ((await templateState('Kräftenachweis')) !== 'present') return;

    await prisma.operationPersonnel.deleteMany({ where: { operationId, memberId: { in: memberIds.slice(9, DEPLOYED) } } });
    const { generatePersonnelSheets } = await import('../src/services/report-generator.service');
    const files = await generatePersonnelSheets(operationId);

    expect(files).toHaveLength(1);
    expect(await names(files[0].filePath, 12)).toHaveLength(9);
  });
});
