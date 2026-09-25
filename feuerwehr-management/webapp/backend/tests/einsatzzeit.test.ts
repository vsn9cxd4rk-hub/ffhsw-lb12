import { describe, it, expect } from 'vitest';
import { calcDurationMinutes, formatDuration } from '../src/services/report-generator.service';

// Diese Tests sichern die Berechnung der Einsatz- und Gesamtzeit im
// Einsatzbericht ab (report-generator.service.ts). Die Gesamtzeit wird dort als
// Einsatzzeit (in Minuten) × Stärke (leaderCount + memberCount) gebildet und
// über formatDuration als HH:MM ausgegeben.

describe('calcDurationMinutes – Einsatzdauer in Minuten', () => {
  it('berechnet eine einfache Dauer innerhalb einer Stunde', () => {
    expect(calcDurationMinutes('14:05', '14:50')).toBe(45);
  });

  it('berechnet eine Dauer über volle Stunden', () => {
    expect(calcDurationMinutes('14:05', '15:00')).toBe(55);
    expect(calcDurationMinutes('08:00', '11:30')).toBe(210);
  });

  it('ergibt 0 bei gleicher Start- und Endzeit', () => {
    expect(calcDurationMinutes('10:00', '10:00')).toBe(0);
  });

  it('ergibt 0, wenn Start oder Ende fehlt', () => {
    expect(calcDurationMinutes(null, '15:00')).toBe(0);
    expect(calcDurationMinutes('14:00', null)).toBe(0);
    expect(calcDurationMinutes(null, null)).toBe(0);
  });

  it('behandelt Einsätze über Mitternacht korrekt', () => {
    // Ausgerückt 23:50, eingerückt 00:30 des Folgetags = 40 Minuten.
    // Die Endzeit liegt am Folgetag; bei negativer Differenz werden 24 h addiert.
    expect(calcDurationMinutes('23:50', '00:30')).toBe(40);
    // Weiteres Beispiel: 22:00 bis 02:15 = 4h 15min = 255 Minuten.
    expect(calcDurationMinutes('22:00', '02:15')).toBe(255);
    // Kurz vor bis kurz nach Mitternacht: 23:59 bis 00:01 = 2 Minuten.
    expect(calcDurationMinutes('23:59', '00:01')).toBe(2);
  });
});

describe('formatDuration – Formatierung als HH:MM', () => {
  it('formatiert Minuten als HH:MM mit führenden Nullen', () => {
    expect(formatDuration(45)).toBe('00:45');
    expect(formatDuration(55)).toBe('00:55');
    expect(formatDuration(60)).toBe('01:00');
    expect(formatDuration(210)).toBe('03:30');
    expect(formatDuration(605)).toBe('10:05');
  });

  it('liefert 00:00 bei 0 oder negativen Minuten', () => {
    expect(formatDuration(0)).toBe('00:00');
    expect(formatDuration(-30)).toBe('00:00');
  });
});

describe('Gesamtzeit – Einsatzzeit × Stärke', () => {
  // Bildet die Berechnung aus replaceOleWithVehicleTable nach:
  //   einsatzMinutes = calcDurationMinutes(departure, return)
  //   gesamtMinutes  = einsatzMinutes × (leaderCount + memberCount)
  const gesamtzeit = (departure: string, ret: string, leaderCount: number, memberCount: number) =>
    formatDuration(calcDurationMinutes(departure, ret) * (leaderCount + memberCount));

  it('multipliziert die Einsatzzeit mit der Gesamtstärke', () => {
    // 55 Min Einsatzzeit × 6 Personen (1 Führer + 5) = 330 Min = 05:30
    expect(gesamtzeit('14:05', '15:00', 1, 5)).toBe('05:30');
  });

  it('rechnet mit einer einzelnen Person = Einsatzzeit', () => {
    // 45 Min × 1 Person = 45 Min
    expect(gesamtzeit('14:05', '14:50', 1, 0)).toBe('00:45');
  });

  it('ergibt 00:00 wenn keine Personen erfasst sind', () => {
    expect(gesamtzeit('14:05', '15:00', 0, 0)).toBe('00:00');
  });

  it('summiert große Einsätze korrekt über die Stundengrenze', () => {
    // 210 Min × 9 Personen (2 + 7) = 1890 Min = 31:30
    expect(gesamtzeit('08:00', '11:30', 2, 7)).toBe('31:30');
  });

  it('rechnet die Gesamtzeit auch für Einsätze über Mitternacht', () => {
    // 22:00–02:15 = 255 Min × 4 Personen (1 + 3) = 1020 Min = 17:00
    expect(gesamtzeit('22:00', '02:15', 1, 3)).toBe('17:00');
  });
});
