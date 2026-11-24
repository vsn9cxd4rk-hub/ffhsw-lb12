import client from './client';

export type Pt = [number, number]; // ETRS89/UTM 32N in Metern [Ost, Nord]

interface Versioned { id: number; version: number; updatedBy: string | null; updatedAt: string }

export interface S2Lage extends Versioned {
  name: string;
  kommune: string;
  isExercise: boolean;
  status: 'aktiv' | 'beendet';
  recipient: string;
  contact: string | null;
  startedAt: string;
  endedAt: string | null;
  createdBy: string | null;
}

export interface S2LageListItem extends S2Lage {
  _count: { berichte: number; kraefte: number; meldungen: number };
  letzterBericht: { lfdNr: number; zeit: string; status: string } | null;
}

export interface S2BerichtKopf extends Versioned {
  lfdNr: number;
  zeit: string;
  bearbeiter: string | null;
  status: 'entwurf' | 'versendet';
  karteAlsFoto: boolean;
}

export interface Beurteilung { stufe?: number | null; prognose?: string | null }
export interface Aufgabe { text?: string; schwerpunkt?: boolean; bezeichnung?: string }

/** Inhalt des Lageberichts, Struktur wie bericht.json des S2-Skills. */
export interface BerichtDaten {
  kraefte?: Partial<Record<'alarmiert' | 'bereitschaft' | 'grundschutz' | 'einsatz' | 'ruhe', string>>;
  schaden?: Record<string, [number, number]>;
  umwelt?: string;
  kritis_bedroht?: { sektor: string; text?: string }[];
  kritis_ausgefallen?: { sektor: string; text?: string }[];
  staerke?: { gesamt?: number | null };
  verpflegung?: { portionen?: number | null; zeitpunkt?: string | null };
  beurteilung?: Record<string, Beurteilung>;
  aufgaben?: Record<string, Aufgabe>;
  probleme?: string;
  kurzfassung?: string;
  massnahmen?: string;
  sperrungen?: string;
  presse?: string;
  bemerkungen?: string;
  einheiten?: { org: string; formation: string; bezeichnung?: string }[];
}

export interface S2Bericht extends S2BerichtKopf { lageId: number; data: BerichtDaten }

export interface S2Abschnitt extends Versioned {
  nr: number;
  name: string;
  lage: string | null;
  schwerpunkt: boolean;
  flaeche: Pt[] | null;
}

export interface S2Kraft extends Versioned {
  org: string;
  formation: string | null;
  bezeichnung: string;
  staerke: string | null;
  status: string;
  abschnittId: number | null;
  bemerkung: string | null;
}

export interface S2Objekt extends Versioned {
  art: 'zeichen' | 'flaeche' | 'linie';
  typ: string;
  punkte: Pt[];
  text: string | null;
  randkaestchen: boolean;
}

export interface S2Meldung extends Versioned {
  nr: string | null;
  eingangAt: string;
  absender: string | null;
  ort: string | null;
  inhalt: string;
  erledigt: boolean;
}

export interface KraefteUebersicht {
  kraefte: Record<'alarmiert' | 'bereitschaft' | 'grundschutz' | 'einsatz' | 'ruhe', string>;
  staerkeGesamt: number;
  ohneStaerke: string[];
  einheiten: { org: string; formation: string; bezeichnung: string }[];
}

export interface S2LageState extends S2Lage {
  berichte: S2BerichtKopf[];
  abschnitte: S2Abschnitt[];
  kraefte: S2Kraft[];
  objekte: S2Objekt[];
  meldungen: S2Meldung[];
  kraefteUebersicht: KraefteUebersicht;
}

export interface S2Pruefung {
  warnungen: string[];
  mail: { an: string; betreff: string; text: string; anhaenge: string[] };
}

export interface GeoTreffer {
  ortsteil: string;
  strasse: string;
  hausnr: string;
  utm: Pt | null;
  genauigkeit: 'HAUSNR' | 'NAECHSTE_HNR' | 'STRASSENMITTE' | 'NICHT_GEFUNDEN';
  hinweis?: string;
  vorschlaege?: string[];
}

export type S2Kind = 'abschnitte' | 'kraefte' | 'objekte' | 'meldungen';

type D<T> = { data: T };

export const s2Api = {
  getLagen: () => client.get<D<S2LageListItem[]>>('/s2/lagen'),
  createLage: (data: Partial<S2Lage>) => client.post<D<S2Lage>>('/s2/lagen', data),
  getLage: (id: number) => client.get<D<S2LageState>>(`/s2/lagen/${id}`),
  updateLage: (id: number, data: Partial<S2Lage> & { version: number }) => client.put<D<S2Lage>>(`/s2/lagen/${id}`, data),
  deleteLage: (id: number) => client.delete(`/s2/lagen/${id}`),

  createBericht: (lageId: number) => client.post<D<S2Bericht>>(`/s2/lagen/${lageId}/berichte`),
  getBericht: (id: number) => client.get<D<S2Bericht>>(`/s2/berichte/${id}`),
  updateBericht: (id: number, data: Partial<S2Bericht> & { version: number }) => client.put<D<S2Bericht>>(`/s2/berichte/${id}`, data),
  deleteBericht: (id: number) => client.delete(`/s2/berichte/${id}`),
  getPruefung: (id: number) => client.get<D<S2Pruefung>>(`/s2/berichte/${id}/pruefung`),
  download: (id: number, datei: 'lagebericht.xlsx' | 'lagekarte.pptx' | 'lagekarte.pdf') =>
    client.get<Blob>(`/s2/berichte/${id}/${datei}`, { responseType: 'blob', timeout: 120_000 }),

  createItem: <T,>(lageId: number, kind: S2Kind, data: Partial<T>) => client.post<D<T>>(`/s2/lagen/${lageId}/${kind}`, data),
  updateItem: <T,>(kind: S2Kind, id: number, data: Partial<T> & { version: number }) => client.put<D<T>>(`/s2/${kind}/${id}`, data),
  deleteItem: (kind: S2Kind, id: number) => client.delete(`/s2/${kind}/${id}`),

  geo: (q: string, ortsteil?: string) => client.get<D<GeoTreffer[]>>('/s2/geo', { params: { q, ortsteil } }),
};
