import 'dotenv/config';
import fs from 'fs';
import { PrismaClient } from '@prisma/client';
import { recordUploadDir, removeUpload } from '../src/utils/files';

/**
 * Removes the records the Playwright tests (webapp/frontend/tests) create, including their
 * files below UPLOAD_PATH. Runs as Playwright globalTeardown and via `npm run e2e:cleanup`.
 *
 * Only exact test patterns are matched (fixed names plus the Date.now() suffix of the specs),
 * so manually entered data is never touched. Keep these in sync with the specs.
 */

// Own client: the shared one logs every query in development
const prisma = new PrismaClient({ log: ['error'] });

const STAMP = String.raw`\d{13}`;

const MEMBER_PATTERNS: Array<[RegExp, RegExp]> = [
  // [firstName, lastName]
  [/^Pw(Abwesenheit|Agt)$/, /^Testmitglied$/],
  [new RegExp(`^Pw(Einsatz|Alters)abteilung${STAMP}$`), /^Uebungstest$/],
  [/^Lea$/, new RegExp(`^PwLehrgang${STAMP}$`)],
];
const EVENT_NAME = new RegExp(`^Test-[A-Za-z-]+-${STAMP}$`);
const EVENT_DATE = '2099-12-31';
const OPERATION_LOCATION = new RegExp(`^Testort - (Teststrasse|Einsatzleiter|Dokumente) ${STAMP}$`);
const VEHICLE_NAME = new RegExp(`^PW-Testfahrzeug ${STAMP}$`);
const ARTICLE_NAME = new RegExp(`^PW-Prüfartikel ${STAMP}$`);
const DEVICE_CLASS_NAME = new RegExp(`^PW-Geräteklasse ${STAMP}$`);
const USERNAME = new RegExp(`^(pw-(user|weak)-${STAMP}|e2e-tmp-\\d+)$`);

function removeRecordDir(kind: string, id: number): void {
  fs.rmSync(recordUploadDir(kind, String(id)), { recursive: true, force: true });
}

export async function cleanupE2eData(): Promise<Record<string, number>> {
  const members = (await prisma.member.findMany({
    where: { lastName: { in: ['Testmitglied', 'Uebungstest'] } },
    select: { id: true, firstName: true, lastName: true },
  })).concat(await prisma.member.findMany({
    where: { firstName: 'Lea', lastName: { startsWith: 'PwLehrgang' } },
    select: { id: true, firstName: true, lastName: true },
  })).filter(m => MEMBER_PATTERNS.some(([first, last]) => first.test(m.firstName) && last.test(m.lastName)));

  const events = (await prisma.event.findMany({
    where: { name: { startsWith: 'Test-' } },
    select: { id: true, name: true, date: true },
  })).filter(e => EVENT_NAME.test(e.name) && e.date?.toISOString().slice(0, 10) === EVENT_DATE);

  const operations = (await prisma.operation.findMany({
    where: { location: { startsWith: 'Testort - ' } },
    select: { id: true, location: true },
  })).filter(o => o.location && OPERATION_LOCATION.test(o.location));

  const vehicles = (await prisma.vehicle.findMany({
    where: { name: { startsWith: 'PW-Testfahrzeug ' } },
    select: { id: true, name: true },
  })).filter(v => VEHICLE_NAME.test(v.name));

  const users = (await prisma.user.findMany({
    where: { OR: [{ username: { startsWith: 'pw-' } }, { username: { startsWith: 'e2e-tmp-' } }] },
    select: { id: true, username: true },
  })).filter(u => USERNAME.test(u.username));

  const articles = (await prisma.article.findMany({
    where: { name: { startsWith: 'PW-Prüfartikel ' } },
    select: { id: true, name: true },
  })).filter(a => ARTICLE_NAME.test(a.name));

  const deviceClasses = (await prisma.deviceClass.findMany({
    where: { name: { startsWith: 'PW-Geräteklasse ' } },
    select: { id: true, name: true },
  })).filter(c => DEVICE_CLASS_NAME.test(c.name));

  // Files first; the DB rows cascade (documents, attendances, courses, logbook, ...)
  const certificates = await prisma.course.findMany({
    where: { memberId: { in: members.map(m => m.id) }, certificatePath: { not: null } },
    select: { certificatePath: true },
  });
  certificates.forEach(c => removeUpload(c.certificatePath));
  events.forEach(e => removeRecordDir('events', e.id));
  operations.forEach(o => removeRecordDir('operations', o.id));
  const inspectionDocs = await prisma.inspectionDocument.findMany({
    where: { inspection: { articleId: { in: articles.map(a => a.id) } } },
    select: { filePath: true },
  });
  inspectionDocs.forEach(d => removeUpload(d.filePath));

  const ids = <T extends { id: number }>(rows: T[]) => ({ id: { in: rows.map(r => r.id) } });
  await prisma.$transaction([
    prisma.event.deleteMany({ where: ids(events) }),
    prisma.operation.deleteMany({ where: ids(operations) }),
    prisma.member.deleteMany({ where: ids(members) }),
    prisma.vehicle.deleteMany({ where: ids(vehicles) }),
    // Articles before their device class: inspection results reference its criteria
    prisma.article.deleteMany({ where: ids(articles) }),
    prisma.deviceClass.deleteMany({ where: ids(deviceClasses) }),
    prisma.user.deleteMany({ where: ids(users) }),
  ]);

  return {
    Mitglieder: members.length,
    Veranstaltungen: events.length,
    Einsätze: operations.length,
    Fahrzeuge: vehicles.length,
    Artikel: articles.length,
    Geräteklassen: deviceClasses.length,
    Benutzer: users.length,
  };
}

if (require.main === module) {
  cleanupE2eData()
    .then(counts => {
      console.log('E2E-Testdaten entfernt:', Object.entries(counts).map(([k, v]) => `${k} ${v}`).join(', '));
    })
    .catch(err => {
      console.error('E2E-Aufräumen fehlgeschlagen:', err);
      process.exitCode = 1;
    })
    .finally(() => prisma.$disconnect());
}
