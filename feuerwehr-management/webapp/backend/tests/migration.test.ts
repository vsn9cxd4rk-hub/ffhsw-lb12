import { describe, it, expect } from 'vitest';
import { prisma } from '../src/config/database';

describe('Schema-Integrität (Migration)', () => {
  it('Tabelle "operations" hat alle erwarteten Spalten', async () => {
    const columns: Array<{ Field: string }> = await prisma.$queryRaw`SHOW COLUMNS FROM operations`;
    const columnNames = columns.map(c => c.Field);

    const expected = [
      'id', 'operationNumber', 'officialNumber', 'date',
      'alarmTime', 'departureTime', 'arrivalTime', 'returnTime',
      'location', 'district', 'keyword', 'vehicles', 'description',
      'leaderCount', 'memberCount', 'commanderId', 'commanderBfId',
      'categoryId', 'tenantId',
      'reportType', 'ilsOrderNumber', 'callerInfo', 'policeInfo',
      'situationOnArrival', 'actionsTaken', 'resourcesUsed',
      'operationType', 'rescuedPersons', 'injuredFirefighters',
      'deceasedPersons', 'deceasedFirefighters', 'createdByName', 'authorRole',
      'createdAt', 'updatedAt',
    ];

    for (const col of expected) {
      expect(columnNames, `Spalte "${col}" fehlt in operations`).toContain(col);
    }
  });

  it('Tabelle "operation_personnel" existiert mit korrekten Spalten', async () => {
    const columns: Array<{ Field: string }> = await prisma.$queryRaw`SHOW COLUMNS FROM operation_personnel`;
    const columnNames = columns.map(c => c.Field);

    const expected = ['id', 'operationId', 'memberId', 'vehicleName', 'function', 'section'];
    for (const col of expected) {
      expect(columnNames, `Spalte "${col}" fehlt in operation_personnel`).toContain(col);
    }
  });

  it('Tabelle "members" hat Qualifikations-Spalten', async () => {
    const columns: Array<{ Field: string }> = await prisma.$queryRaw`SHOW COLUMNS FROM members`;
    const columnNames = columns.map(c => c.Field);

    const expected = [
      'qualLicenseC', 'qualLicenseB', 'qualAGT',
      'qualGruppenfuehrer', 'qualZugfuehrer', 'qualMachinist',
    ];
    for (const col of expected) {
      expect(columnNames, `Spalte "${col}" fehlt in members`).toContain(col);
    }
  });

  it('Alle erwarteten Tabellen existieren', async () => {
    const tables: Array<{ Tables_in_FFWVSLB12?: string; [key: string]: unknown }> =
      await prisma.$queryRaw`SHOW TABLES`;
    const tableNames = tables.map(t => Object.values(t)[0] as string);

    const expected = [
      'users', 'members', 'vehicles', 'operations', 'operation_times',
      'operation_reports', 'operation_documents', 'operation_personnel',
      'events', 'attendances', 'templates', 'template_history',
      'permission_groups', 'settings',
    ];

    for (const table of expected) {
      expect(tableNames, `Tabelle "${table}" fehlt`).toContain(table);
    }
  });

  it('Foreign Keys in operation_personnel sind korrekt', async () => {
    const constraints: Array<{ CONSTRAINT_NAME: string; REFERENCED_TABLE_NAME: string }> =
      await prisma.$queryRaw`
        SELECT CONSTRAINT_NAME, REFERENCED_TABLE_NAME
        FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
        WHERE TABLE_NAME = 'operation_personnel'
        AND REFERENCED_TABLE_NAME IS NOT NULL
        AND TABLE_SCHEMA = DATABASE()
      `;

    const refs = constraints.map(c => c.REFERENCED_TABLE_NAME);
    expect(refs).toContain('operations');
    expect(refs).toContain('members');
  });
});
