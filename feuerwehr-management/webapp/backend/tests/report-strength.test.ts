import { describe, it, expect } from 'vitest';
import { replaceOleWithVehicleTable, reportStrength, TimeEntry } from '../src/services/report-generator.service';

// Einsatzbericht: Die Stärke steht in der Fahrzeugtabelle als Gesamtzahl (9),
// erfasst wird weiter getrennt nach GF/FM (1/8).

// Minimal document.xml with the embedded OLE object the generator replaces
const OLE_XML =
  '<w:body><w:p w:rsidR="1"><w:r><w:t>Vorher</w:t></w:r></w:p>' +
  '<w:p w:rsidR="2"><w:r><w:object><v:shape/></w:object></w:r></w:p>' +
  '<w:p w:rsidR="3"><w:r><w:t>Nachher</w:t></w:r></w:p></w:body>';

const time = (vehicleName: string, leaderCount: number, memberCount: number): TimeEntry => ({
  vehicleName, leaderCount, memberCount, departureTime: '14:05', arrivalTime: '14:10', returnTime: '15:05',
});

/** Cell texts of each table row */
function rows(xml: string): string[][] {
  return [...xml.matchAll(/<w:tr>([\s\S]*?)<\/w:tr>/g)].map(m =>
    [...m[1].matchAll(/<w:t>([^<]*)<\/w:t>/g)].map(t => t[1]));
}

describe('Einsatzbericht – Stärke als Gesamtzahl', () => {
  it('reportStrength summiert GF und FM', () => {
    expect(reportStrength(1, 8)).toBe('9');
    expect(reportStrength(0, 0)).toBe('0');
    expect(reportStrength(2, 0)).toBe('2');
  });

  it('Fahrzeugtabelle zeigt die Gesamtzahl statt 1/8/9', () => {
    const xml = replaceOleWithVehicleTable(OLE_XML, [time('12/45', 1, 8), time('12/23', 1, 2)]);
    const [header, lf, tlf] = rows(xml);
    const col = header.indexOf('Stärke');
    expect(col).toBeGreaterThan(-1);
    expect(lf[col]).toBe('9');
    expect(tlf[col]).toBe('3');
    expect(xml).not.toContain('1/8/9');
  });

  it('Gesamtzeit rechnet weiter mit der Gesamtstärke', () => {
    const xml = replaceOleWithVehicleTable(OLE_XML, [time('12/45', 1, 8)]);
    const [header, row] = rows(xml);
    expect(row[header.indexOf('Einsatzzeit')]).toBe('01:00');
    expect(row[header.indexOf('Gesamtzeit')]).toBe('09:00');
  });

  it('ersetzt nur den OLE-Absatz', () => {
    const xml = replaceOleWithVehicleTable(OLE_XML, [time('12/45', 1, 8)]);
    expect(xml).toContain('Vorher');
    expect(xml).toContain('Nachher');
    expect(xml).not.toContain('<w:object');
  });
});
