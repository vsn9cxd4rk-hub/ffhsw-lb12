import type { User } from '@/api/types';

// Named capability bits within PermissionGroup.br0-br75 (mirrors webapp/backend/src/config/permissionBits.ts).
// A group can combine any of these freely (e.g. "Gerätewart + Gruppenführer" sets both).
export const BIT_VEHICLES = 'br1'; // Fahrzeuge (incl. Fahrtenbuch, Gerätepüfungen am Fahrzeug)
export const BIT_OPERATIONS = 'br2'; // Einsätze, Statistik, Personalliste für Kräftenachweis
export const BIT_EQUIPMENT = 'br3'; // Gerätewart-Bereich: Bestandsliste, Prüfbuch, Mängel
// Read access to the following areas is open to every logged-in user; the bit grants writing.
export const BIT_EVENTS = 'br4'; // Veranstaltungen/BSW pflegen: Anwesenheit, Dokumente, Berichte
export const BIT_TRAINING = 'br5'; // Ausbildung pflegen: Lehrgänge, Urkunden

export type PermissionBit =
  | typeof BIT_VEHICLES
  | typeof BIT_OPERATIONS
  | typeof BIT_EQUIPMENT
  | typeof BIT_EVENTS
  | typeof BIT_TRAINING;

export const PERMISSION_LABELS: Record<PermissionBit, string> = {
  [BIT_VEHICLES]: 'Fahrzeuge',
  [BIT_OPERATIONS]: 'Einsätze',
  [BIT_EQUIPMENT]: 'Gerätewart (Bestand, Prüfungen, Mängel)',
  [BIT_EVENTS]: 'Veranstaltungen pflegen',
  [BIT_TRAINING]: 'Ausbildung pflegen',
};

/** Same rule as the web frontend (RequirePermission/Sidebar): admins may do everything. */
export function hasPermission(user: User | null, bit: PermissionBit): boolean {
  if (!user) return false;
  return user.isAdmin || !!user.permissions?.[bit];
}
