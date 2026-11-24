import type { AxiosResponse } from 'axios';
import type { Pt } from '../api/s2';

// Listen der RV-Vorlage V1_02 (Lagebericht), siehe S2-Skill
export const ORGS = ['FW', 'RD/eRD', 'THW', 'ASB', 'DLRG', 'DRK', 'JUH', 'MHD', 'NKS', 'BW'];
export const FORMATIONEN = ['Löschbezirk', 'FZ Führungsunterstützung', 'FZ Einsatzzug', 'FZ Waldbrand', 'FZ Wasserversorgung',
  'FZ Hochwasserschutz', 'Drohneneinheit', 'ABC-Zug (gesamt)', 'ABC-Zug (nur Messkomponente)', 'ABC-Zug (nur Dekonkomponete)',
  'PtGr 5 (Patiententransportgruppe)', 'FZ BHP 25 (Behandlungsplatz)', 'FZ BHP 50 (Behandlungsplatz)', 'FZ BP 250 (Betreuungsplatz)',
  'FG Verpf 250 (Verpflegungsgruppe)', 'WRZ (Wasserrettungszug)', 'WRGr (Wasserrettungsgruppe)', 'TZ (Technischer Zug)',
  'FZ FüKom (Führung und Kommunikation)', 'FZ Log (Logistik)', 'FGr B (Bergung)', 'FGr BrB (Brückenbau)', 'FGr BT (Bergungstauchen)',
  'FGr E (Elektroversorgung)', 'FGr I (Infrastruktur)', 'FGr N (Notversorgung und Notinstandsetzung)', 'FGr Öl (Ölschaden)',
  'FGr O (Ortung)', 'FGr R (Räumen)', 'FGr SB (Schwere Bergung)', 'FGr Sp (Sprengen)', 'FGr T (Trinkwasserversorgung)',
  'FGr W (Wassergefahren)', 'FGr WP (Wasserschaden/Pumpen)'];
export const KRAFT_STATUS: { value: string; label: string }[] = [
  { value: 'alarmiert', label: 'Alarmiert / auf Marsch' },
  { value: 'bereitschaft', label: 'In Bereitschaft' },
  { value: 'grundschutz', label: 'Bereitschaft (Grundschutz)' },
  { value: 'einsatz', label: 'Im Einsatz' },
  { value: 'ruhe', label: 'In Ruhe' },
  { value: 'entlassen', label: 'Entlassen' },
];
export const KRITIS_SEKTOREN = ['Wasser', 'Energie', 'Ernährung', 'Finanz-& Versicherungswesen', 'Gesundheit',
  'IT & Telekommunikation', 'Siedlungsabfallentsorgung', 'Medien & Kultur', 'Staat & Verwaltung', 'Transport & Verkehr'];
export const PROGNOSEN = ['Entspannung', 'Stagnation', 'Verschärfung'];

export const FLAECHEN: Record<string, { label: string; color: string }> = {
  brand: { label: 'Brand', color: '#E4221B' },
  ueberschwemmt: { label: 'Überflutung', color: '#0090D6' },
  kontaminiert: { label: 'Kontaminiert', color: '#FFED00' },
  versorgung: { label: 'Versorgungsausfall', color: '#CD5FA0' },
  duerre: { label: 'Dürre', color: '#9A6732' },
  sonstiges: { label: 'Sonstiges', color: '#F49B19' },
};
export const LINIEN: Record<string, { label: string; color: string }> = {
  rettungsweg: { label: 'Rettungsweg', color: '#00B050' },
  riegelstellung: { label: 'Riegelstellung', color: '#0070C0' },
  brandausbreitung: { label: 'Brandausbreitung', color: '#FF0000' },
};

// Kalibrierung des Kartenbilds der Vorlage (3490 x 2467 px, UTM-Gitter 1 km), wie lagekarte.ts im Backend
export const KARTE = { width: 3490, height: 2467, pxPerKm: (3160 - 287) / 11, e0px: 287, e0: 345000, n0px: 184, n0: 5472000 };
export const AUSSCHNITT = { eMin: 344600, eMax: 356400, nMin: 5463700, nMax: 5472400 };

export function utmToPx([e, n]: Pt): Pt {
  return [KARTE.e0px + ((e - KARTE.e0) / 1000) * KARTE.pxPerKm, KARTE.n0px + ((KARTE.n0 - n) / 1000) * KARTE.pxPerKm];
}

export function pxToUtm([x, y]: Pt): Pt {
  return [Math.round(KARTE.e0 + ((x - KARTE.e0px) / KARTE.pxPerKm) * 1000), Math.round(KARTE.n0 - ((y - KARTE.n0px) / KARTE.pxPerKm) * 1000)];
}

export function imAusschnitt([e, n]: Pt): boolean {
  return e >= AUSSCHNITT.eMin && e <= AUSSCHNITT.eMax && n >= AUSSCHNITT.nMin && n <= AUSSCHNITT.nMax;
}

/** Gitterwert von der Wandkarte in km, z. B. "351,2/5466,8" -> [351200, 5466800]. */
export function parseGitter(text: string): Pt | null {
  const m = /^\s*(\d{3}(?:[.,]\d+)?)\s*[/;\s]\s*(\d{4}(?:[.,]\d+)?)\s*$/.exec(text);
  if (!m) return null;
  const pt: Pt = [Math.round(parseFloat(m[1].replace(',', '.')) * 1000), Math.round(parseFloat(m[2].replace(',', '.')) * 1000)];
  return imAusschnitt(pt) ? pt : null;
}

export function formatGitter([e, n]: Pt): string {
  return `${(e / 1000).toFixed(2).replace('.', ',')} / ${(n / 1000).toFixed(2).replace('.', ',')}`;
}

/** Wie staerkeGesamt im Backend: 1/8/9 -> 9, 1/6 -> 7, 2/5/24 -> 31, 12 -> 12. */
export function staerkeGesamt(value: string | null | undefined): number | null {
  const text = (value ?? '').trim();
  if (!text || !/^\d+(\s*\/\s*\d+)*$/.test(text)) return null;
  const n = text.split('/').map((x) => Number(x.trim()));
  if (n.length === 1) return n[0];
  const last = n[n.length - 1];
  const rest = n.slice(0, -1).reduce((a, b) => a + b, 0);
  return n.length >= 3 && last === rest ? last : rest + last;
}

/** Error text of an API call; blob responses (downloads) carry the JSON error as a Blob. */
export async function fehlertext(err: unknown, fallback = 'Aktion fehlgeschlagen'): Promise<string> {
  const data = (err as { response?: { data?: unknown } })?.response?.data;
  if (data instanceof Blob) {
    try { return (JSON.parse(await data.text()) as { error?: string }).error || fallback; } catch { return fallback; }
  }
  return (data as { error?: string } | undefined)?.error || fallback;
}

export function istKonflikt(err: unknown): boolean {
  return (err as { response?: { status?: number } })?.response?.status === 409;
}

export function speichereDownload(res: AxiosResponse<Blob>, fallbackName: string): void {
  const cd = String(res.headers['content-disposition'] ?? '');
  const utf = /filename\*=UTF-8''([^;]+)/.exec(cd);
  const plain = /filename="([^"]+)"/.exec(cd);
  const name = utf ? decodeURIComponent(utf[1]) : plain ? plain[1] : fallbackName;
  const url = URL.createObjectURL(res.data);
  const a = document.createElement('a');
  a.href = url;
  a.download = name;
  document.body.appendChild(a);
  a.click();
  a.remove();
  setTimeout(() => URL.revokeObjectURL(url), 1000);
}
