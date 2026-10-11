import { Prisma } from '@prisma/client';
import bcrypt from 'bcryptjs';
import { prisma } from '../config/database';
import { logger } from '../utils/logger';
import { BIT_EVENTS, BIT_OPERATIONS, BIT_TRAINING } from '../config/permissionBits';

/**
 * One-off data migrations that must run once per installation. Each is recorded in the
 * settings table, so it never runs twice – later changes made in the UI stay untouched.
 */
interface DataMigration {
  key: string;
  description: string;
  run: (tx: Prisma.TransactionClient) => Promise<string>;
}

const MIGRATIONS: DataMigration[] = [
  {
    // Until 2026-10 events and training had no permission check at all. Groups that
    // maintain operations (Gruppenführer) also maintain events/training per their
    // description, so they keep write access; everybody else becomes read-only.
    key: 'migration.permissionBits.eventsTraining',
    description: 'Veranstaltungen/Ausbildung: Schreibrecht für Gruppen mit Einsatz-Berechtigung',
    run: async (tx) => {
      const { count } = await tx.permissionGroup.updateMany({
        where: { [BIT_OPERATIONS]: true },
        data: { [BIT_EVENTS]: true, [BIT_TRAINING]: true },
      });
      return `${count} Gruppe(n) erhielten ${BIT_EVENTS}/${BIT_TRAINING}`;
    },
  },
  {
    // Up to 1.3.x the seed created "admin" with the publicly documented password Admin123!.
    // Accounts still using it must pick a new password at their next login.
    key: 'migration.mustChangePassword.defaultPassword',
    description: 'Passwortwechsel für Konten mit dem früheren Standardpasswort erzwingen',
    run: async (tx) => {
      const users = await tx.user.findMany({ where: { isActive: true }, select: { id: true, password: true } });
      const affected: number[] = [];
      for (const u of users) {
        if (await bcrypt.compare(LEGACY_DEFAULT_PASSWORD, u.password)) affected.push(u.id);
      }
      if (affected.length > 0) {
        await tx.user.updateMany({ where: { id: { in: affected } }, data: { mustChangePassword: true } });
      }
      return `${affected.length} Konto/Konten mit Standardpasswort markiert`;
    },
  },
];

const LEGACY_DEFAULT_PASSWORD = 'Admin123!';

export async function runDataMigrations(): Promise<void> {
  for (const m of MIGRATIONS) {
    const done = await prisma.setting.findUnique({ where: { key: m.key } });
    if (done) continue;

    const result = await prisma.$transaction(async (tx) => {
      // The marker row is unique: a second instance starting at the same time fails here
      await tx.setting.create({ data: { key: m.key, value: new Date().toISOString(), description: m.description } });
      return m.run(tx);
    }, { timeout: 60_000 }); // bcrypt checks over all users take longer than Prisma's 5 s default
    logger.info(`Datenmigration ${m.key}: ${result}`);
  }
}
