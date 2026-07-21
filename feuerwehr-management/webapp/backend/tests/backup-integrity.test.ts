import { describe, it, expect } from 'vitest';
import { prisma } from '../src/config/database';

describe('Backup-Integrität (Post-Restore Checks)', () => {
  it('Admin-User existiert', async () => {
    const admin = await prisma.user.findFirst({ where: { username: 'admin' } });
    expect(admin, 'Admin-User fehlt in der Datenbank').not.toBeNull();
    expect(admin!.isAdmin).toBe(true);
  });

  it('Mindestens eine Berechtigungsgruppe existiert', async () => {
    const groups = await prisma.permissionGroup.findMany();
    expect(groups.length, 'Keine Berechtigungsgruppen vorhanden').toBeGreaterThan(0);
  });

  it('Mitglieder-Tabelle hat Einträge', async () => {
    const count = await prisma.member.count();
    expect(count, 'Keine Mitglieder in der Datenbank').toBeGreaterThan(0);
  });

  it('Fahrzeuge sind vorhanden', async () => {
    const count = await prisma.vehicle.count();
    expect(count, 'Keine Fahrzeuge in der Datenbank').toBeGreaterThan(0);
  });

  it('Settings-Tabelle ist befüllt', async () => {
    const settings = await prisma.setting.findFirst();
    expect(settings, 'Keine Settings in der Datenbank').not.toBeNull();
  });

  it('Templates sind vorhanden (Einsatzbericht, Kräftenachweis)', async () => {
    const templates = await prisma.template.findMany();
    expect(templates.length, 'Keine Templates vorhanden').toBeGreaterThan(0);

    const names = templates.map(t => t.name.toLowerCase());
    const hasEinsatzbericht = names.some(n => n.includes('einsatzbericht'));
    const hasKraeftenachweis = names.some(n => n.includes('kräftenachweis') || n.includes('kraeftenachweis'));
    expect(hasEinsatzbericht, 'Template "Einsatzbericht" fehlt').toBe(true);
    expect(hasKraeftenachweis, 'Template "Kräftenachweis" fehlt').toBe(true);
  });

  it('Keine verwaisten Einsatz-Dokumente (Dateipfade existieren)', async () => {
    const docs = await prisma.operationDocument.findMany({ take: 10 });
    const fs = await import('fs');
    let missing = 0;
    for (const doc of docs) {
      if (!fs.existsSync(doc.filePath)) missing++;
    }
    expect(missing, `${missing} von ${docs.length} Dokument-Dateien fehlen auf dem Filesystem`).toBe(0);
  });

  it('Keine verwaisten Template-Dateien', async () => {
    const templates = await prisma.template.findMany();
    const fs = await import('fs');
    let missing = 0;
    for (const t of templates) {
      if (!fs.existsSync(t.filePath)) missing++;
    }
    expect(missing, `${missing} von ${templates.length} Template-Dateien fehlen auf dem Filesystem`).toBe(0);
  });

  it('Prisma-Client-Version stimmt mit Schema überein', async () => {
    // If Prisma client is out of sync with DB, queries would fail
    // This test verifies by doing a complex query that touches relations
    await prisma.operation.findFirst({
      include: { times: true, reports: true, documents: true, personnel: true },
    });
    // If this doesn't throw, the schema matches
    expect(true).toBe(true);
  });

  it('Keine NULL-Werte in Pflichtfeldern', async () => {
    const usersWithoutUsername: Array<{ id: number }> = await prisma.$queryRaw`
      SELECT id FROM users WHERE username IS NULL OR username = ''
    `;
    expect(usersWithoutUsername.length, 'User ohne Username gefunden').toBe(0);

    const membersWithoutName: Array<{ id: number }> = await prisma.$queryRaw`
      SELECT id FROM members WHERE lastName IS NULL OR lastName = '' OR firstName IS NULL OR firstName = ''
    `;
    expect(membersWithoutName.length, 'Mitglieder ohne Name gefunden').toBe(0);
  });
});
