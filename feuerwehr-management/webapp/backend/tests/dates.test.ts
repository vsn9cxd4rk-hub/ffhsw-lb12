import { describe, it, expect } from 'vitest';
import { addMonths } from '../src/utils/dates';

// Date#setMonth rolls over at the end of a month: 31.08. + 6 months became 02./03.03.,
// so the next inspection was due up to three days later than the interval allows.

const ymd = (d: Date) => `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;

describe('addMonths', () => {
  it('bleibt am Monatsende im Zielmonat', () => {
    expect(ymd(addMonths(new Date(2026, 7, 31), 6))).toBe('2027-02-28');
    expect(ymd(addMonths(new Date(2027, 7, 31), 6))).toBe('2028-02-29');
    expect(ymd(addMonths(new Date(2026, 0, 31), 1))).toBe('2026-02-28');
    expect(ymd(addMonths(new Date(2026, 2, 31), 3))).toBe('2026-06-30');
  });

  it('lässt normale Tage unverändert', () => {
    expect(ymd(addMonths(new Date(2026, 4, 15), 12))).toBe('2027-05-15');
    expect(ymd(addMonths(new Date(2026, 10, 30), 3))).toBe('2027-02-28');
    expect(ymd(addMonths(new Date(2026, 11, 31), 2))).toBe('2027-02-28');
    expect(ymd(addMonths(new Date(2026, 0, 31), 12))).toBe('2027-01-31');
  });

  it('verändert das Ausgangsdatum nicht', () => {
    const start = new Date(2026, 7, 31);
    addMonths(start, 6);
    expect(ymd(start)).toBe('2026-08-31');
  });
});
