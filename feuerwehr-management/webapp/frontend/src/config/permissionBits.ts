// Named capability bits within PermissionGroup.br0-br75 (mirrors backend src/config/permissionBits.ts).
// A group can combine any of these freely (e.g. "Gerätewart + Gruppenführer" sets both).
export const BIT_VEHICLES = 'br1'; // Fahrzeuge (incl. Fahrtenbuch, Gerätepüfungen am Fahrzeug)
export const BIT_OPERATIONS = 'br2'; // Einsätze, Statistik, Personalliste für Kräftenachweis
export const BIT_EQUIPMENT = 'br3'; // Gerätewart-Bereich: Bestandsliste, Prüfbuch, Mängel
