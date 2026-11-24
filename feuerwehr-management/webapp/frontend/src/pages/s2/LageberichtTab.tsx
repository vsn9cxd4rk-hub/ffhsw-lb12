import { useEffect, useState } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { ArrowDownTrayIcon, ClipboardDocumentIcon, EnvelopeIcon, ExclamationTriangleIcon } from '@heroicons/react/24/outline';
import { s2Api, type BerichtDaten, type S2Bericht, type S2LageState } from '../../api/s2';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { useToastStore } from '../../components/ui/Toast';
import { cn } from '../../utils/cn';
import { KRITIS_SEKTOREN, PROGNOSEN, fehlertext, istKonflikt, speichereDownload } from '../../utils/s2';
import { POLL_MS, useS2Mutation } from './useS2';

const input = 'w-full border border-gray-300 rounded-md px-2 py-1 text-sm';
const TZ_RE = /^(\d{2})([01]\d|2[0-3])([0-5]\d)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(\d{2})$/i;

const KRAEFTE_FELDER = [
  ['alarmiert', 'Alarmiert / auf Marsch'], ['bereitschaft', 'In Bereitschaft'], ['grundschutz', 'davon für Grundschutz zurückgehalten'],
  ['einsatz', 'Im Einsatz'], ['ruhe', 'In Ruhe'],
] as const;
const SCHAEDEN = [
  ['verletzt', 'Verletzte'], ['verschuettet', 'Verschüttete'], ['vermisst', 'Vermisste'], ['verstorben', 'Verstorbene (Gesamtzahl)'],
  ['betreuung', 'Betreute Personen'], ['tiere_betroffen', 'Tiere betroffen (Großvieh)'], ['tiere_verstorben', 'Tiere verstorben'],
  ['gebaeude_beschaedigt', 'Gebäude beschädigt (nutzbar)'], ['gebaeude_teilzerstoert', 'Gebäude teilzerstört'], ['gebaeude_zerstoert', 'Gebäude zerstört'],
] as const;
const BEURTEILUNG = [['schadenslage', 'Schadenslage'], ['fuehrung', 'Führung'], ['kraefte', 'Kräfte/Fähigkeiten'], ['logistik', 'Logistik'], ['kommunikation', 'Kommunikation']] as const;
const AUFGABEN = [
  ['menschenrettung', 'Menschenrettung'], ['evakuierung', 'Evakuierung'], ['brandschutz', 'Brandschutz'], ['technische_hilfe', 'Technische Hilfe'],
  ['cbrn', 'CBRN-Schutz'], ['sanitaet', 'Sanität'], ['betreuung', 'Betreuung/Versorgung'], ['wasserrettung', 'Wasserrettung'], ['psnv', 'PSNV'], ['sonstige', 'Sonstige'],
] as const;
const TH_TEXTE = ['Bergen/Räumen', 'Wasserschaden / Pumpen', 'Notversorgung / -instandsetzung', 'Trinkwassernotversorgung', 'Infrastruktur'];

interface Draft { zeit: string; bearbeiter: string; karteAlsFoto: boolean; data: BerichtDaten }
const toDraft = (b: S2Bericht): Draft => ({ zeit: b.zeit, bearbeiter: b.bearbeiter ?? '', karteAlsFoto: b.karteAlsFoto, data: b.data ?? {} });

function Feld({ label, children, hint }: { label: string; children: React.ReactNode; hint?: string }) {
  return (
    <label className="block">
      <span className="block text-xs font-medium text-gray-600 mb-0.5">{label}</span>
      {children}
      {hint && <span className="block text-xs text-gray-400 mt-0.5">{hint}</span>}
    </label>
  );
}

export function LageberichtTab({ lage }: { lage: S2LageState }) {
  const qc = useQueryClient();
  const addToast = useToastStore((s) => s.addToast);
  const neuester = lage.berichte[lage.berichte.length - 1];
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const berichtId = selectedId ?? neuester?.id ?? null;

  const neu = useS2Mutation(lage.id, () => s2Api.createBericht(lage.id).then((r) => setSelectedId(r.data.data.id)));

  return (
    <div className="flex flex-col lg:flex-row gap-4">
      <div className="lg:w-56 shrink-0 space-y-2">
        <Button variant="primary" className="w-full" loading={neu.isPending} onClick={() => neu.mutate(undefined)}>
          {neuester ? `Fortschreiben (Nr. ${neuester.lfdNr + 1})` : '1. Lagebericht anlegen'}
        </Button>
        <p className="text-xs text-gray-500">Neue Nummer übernimmt alle Werte des letzten Berichts; nur Änderungen einarbeiten. Zeit = nächste volle Stunde.</p>
        <ul className="space-y-1">
          {[...lage.berichte].reverse().map((b) => (
            <li key={b.id}>
              <button onClick={() => setSelectedId(b.id)}
                className={cn('w-full text-left rounded-md px-3 py-2 text-sm border', b.id === berichtId ? 'border-red-600 bg-red-50' : 'border-gray-200 bg-white hover:bg-gray-50')}>
                <span className="font-medium">Nr. {b.lfdNr}</span> <span className="tabular-nums">{b.zeit}</span>
                <span className={cn('block text-xs', b.status === 'versendet' ? 'text-green-700' : 'text-gray-500')}>
                  {b.status === 'versendet' ? 'versendet' : 'Entwurf'}{b.updatedBy && ` · ${b.updatedBy}`}
                </span>
              </button>
            </li>
          ))}
        </ul>
      </div>
      <div className="flex-1 min-w-0">
        {berichtId
          ? <BerichtEditor key={berichtId} id={berichtId} lage={lage} onDeleted={() => { setSelectedId(null); qc.invalidateQueries({ queryKey: ['s2-lage', lage.id] }); }} addToast={addToast} />
          : <Card><p className="text-sm text-gray-600">Noch kein Lagebericht. Mit „1. Lagebericht anlegen“ beginnen – unverzüglich nach Aktivierung, danach stündlich zur vollen Stunde.</p></Card>}
      </div>
    </div>
  );
}

function BerichtEditor({ id, lage, onDeleted, addToast }: {
  id: number; lage: S2LageState; onDeleted: () => void; addToast: (m: string, t?: 'error' | 'success') => void;
}) {
  const qc = useQueryClient();
  const { data: server } = useQuery({
    queryKey: ['s2-bericht', id],
    queryFn: () => s2Api.getBericht(id).then((r) => r.data.data),
    refetchInterval: POLL_MS,
  });
  const [draft, setDraft] = useState<Draft | null>(null);
  const [basis, setBasis] = useState<S2Bericht | null>(null);
  const [saving, setSaving] = useState(false);

  const dirty = !!(draft && basis && JSON.stringify(draft) !== JSON.stringify(toDraft(basis)));
  const fremdGeaendert = !!(server && basis && server.version !== basis.version);

  // Take over the server state as long as there are no own unsaved changes
  useEffect(() => {
    if (server && (!basis || (!dirty && server.version !== basis.version))) {
      setBasis(server);
      setDraft(toDraft(server));
    }
  }, [server, basis, dirty]);

  const { data: pruefung } = useQuery({
    queryKey: ['s2-pruefung', id, server?.version, lage.version, lage.abschnitte.map((a) => a.version).join(), lage.objekte.length],
    queryFn: () => s2Api.getPruefung(id).then((r) => r.data.data),
    enabled: !!server,
  });

  if (!draft || !basis || !server) return <LoadingSpinner />;
  const d = draft.data;
  const setData = (patch: Partial<BerichtDaten>) => setDraft({ ...draft, data: { ...d, ...patch } });
  const zeitOk = TZ_RE.test(draft.zeit);

  const speichern = async (extra: Partial<S2Bericht> = {}) => {
    setSaving(true);
    try {
      const res = await s2Api.updateBericht(id, { ...draft, ...extra, version: basis.version });
      setBasis(res.data.data);
      setDraft(toDraft(res.data.data));
      qc.setQueryData(['s2-bericht', id], res.data.data);
      qc.invalidateQueries({ queryKey: ['s2-lage', lage.id] });
      addToast(`Lagebericht Nr. ${basis.lfdNr} gespeichert`, 'success');
    } catch (err) {
      addToast(await fehlertext(err, 'Speichern fehlgeschlagen'));
      if (istKonflikt(err)) qc.invalidateQueries({ queryKey: ['s2-bericht', id] });
    } finally {
      setSaving(false);
    }
  };

  const verwerfen = () => { setBasis(server); setDraft(toDraft(server)); };

  const uebernehmeKraefte = () => {
    const u = lage.kraefteUebersicht;
    setData({ kraefte: { ...u.kraefte }, staerke: { ...d.staerke, gesamt: u.staerkeGesamt }, einheiten: u.einheiten.slice(0, 36) });
    if (u.ohneStaerke.length > 0) addToast(`Ohne lesbare Stärke (nicht gezählt): ${u.ohneStaerke.join(', ')}`);
    if (u.einheiten.length > 36) addToast('Mehr als 36 Einheiten: nur die ersten 36 passen ins Formular, Rest in Bemerkungen');
  };

  const download = async (datei: 'lagebericht.xlsx' | 'lagekarte.pptx' | 'lagekarte.pdf') => {
    if (dirty) { addToast('Bitte zuerst speichern – exportiert wird der gespeicherte Stand'); return; }
    try {
      speichereDownload(await s2Api.download(id, datei), datei);
    } catch (err) {
      addToast(await fehlertext(err, 'Export fehlgeschlagen'));
    }
  };

  const loeschen = async () => {
    try {
      await s2Api.deleteBericht(id);
      onDeleted();
    } catch (err) {
      addToast(await fehlertext(err));
    }
  };

  const kopiere = async (text: string, was: string) => {
    try { await navigator.clipboard.writeText(text); addToast(`${was} kopiert`, 'success'); } catch { addToast('Kopieren nicht möglich – Text markieren und kopieren'); }
  };

  const schaden = (k: string, i: 0 | 1, v: string) => {
    const cur = d.schaden?.[k] ?? [0, 0];
    const next: [number, number] = [cur[0], cur[1]];
    next[i] = Math.max(0, Number(v) || 0);
    const s = { ...(d.schaden ?? {}) };
    if (next[0] === 0 && next[1] === 0) delete s[k]; else s[k] = next;
    setData({ schaden: s });
  };

  const kritis = (key: 'kritis_bedroht' | 'kritis_ausgefallen', titel: string) => {
    const list = d[key] ?? [];
    const set = (l: typeof list) => setData({ [key]: l });
    return (
      <div className="space-y-1">
        <p className="text-xs font-medium text-gray-600">{titel} (max. 5)</p>
        {list.map((e, i) => (
          <div key={i} className="flex gap-1">
            <select aria-label={`${titel} Sektor ${i + 1}`} className={cn(input, 'w-44')} value={e.sektor}
              onChange={(ev) => set(list.map((x, j) => (j === i ? { ...x, sektor: ev.target.value } : x)))}>
              {KRITIS_SEKTOREN.map((s) => <option key={s}>{s}</option>)}
            </select>
            <input aria-label={`${titel} Text ${i + 1}`} className={input} value={e.text ?? ''} placeholder="was/wo, ≤ 30 Zeichen"
              onChange={(ev) => set(list.map((x, j) => (j === i ? { ...x, text: ev.target.value } : x)))} />
            <button className="px-2 text-gray-400 hover:text-red-600" aria-label="entfernen" onClick={() => set(list.filter((_, j) => j !== i))}>×</button>
          </div>
        ))}
        {list.length < 5 && <Button size="sm" variant="ghost" onClick={() => set([...list, { sektor: 'Energie', text: '' }])}>+ Sektor</Button>}
      </div>
    );
  };

  const kurzfassungVorlage = () => {
    const zeilen = ['Allgemein: ', ...lage.abschnitte.map((a) => `EA ${a.name}: `)];
    setData({ kurzfassung: d.kurzfassung?.trim() ? d.kurzfassung : zeilen.join('\n') });
  };

  return (
    <div className="space-y-4">
      {fremdGeaendert && dirty && (
        <div className="rounded-md border border-amber-300 bg-amber-50 p-3 text-sm flex flex-wrap items-center gap-3" role="alert">
          <ExclamationTriangleIcon className="h-5 w-5 text-amber-600" />
          <span className="flex-1">{server.updatedBy ?? 'Jemand'} hat diesen Lagebericht inzwischen geändert. Ihre Änderungen sind noch nicht gespeichert und würden abgelehnt.</span>
          <Button size="sm" variant="secondary" onClick={verwerfen}>Eigene Änderungen verwerfen und neu laden</Button>
        </div>
      )}

      <div className="sticky top-0 z-10 flex flex-wrap items-center gap-2 bg-gray-50/95 py-2 backdrop-blur">
        <h3 className="text-lg font-semibold mr-2">Lagebericht Nr. {basis.lfdNr}</h3>
        <span className={cn('text-xs', dirty ? 'text-amber-700' : 'text-gray-500')}>
          {dirty ? 'ungespeicherte Änderungen' : `gespeichert${basis.updatedBy ? ` von ${basis.updatedBy}` : ''}, ${new Date(basis.updatedAt).toLocaleTimeString('de-DE')}`}
        </span>
        <div className="ml-auto flex gap-2">
          <Button size="sm" variant="primary" disabled={!dirty || !zeitOk} loading={saving} onClick={() => speichern()}>Speichern</Button>
          {basis.status === 'entwurf' && basis.id === lage.berichte[lage.berichte.length - 1]?.id && (
            <Button size="sm" variant="ghost" onClick={loeschen}>Entwurf löschen</Button>
          )}
        </div>
      </div>

      <Card title="Kopf">
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
          <Feld label="Zeit (taktisch)" hint={zeitOk ? 'DDhhmm + Monat engl. + JJ' : 'Format z. B. 141400nov26 (Monat: jan feb mar … oct nov dec)'}>
            <input aria-label="Zeit" className={cn(input, !zeitOk && 'border-red-500')} value={draft.zeit} onChange={(e) => setDraft({ ...draft, zeit: e.target.value.trim() })} />
          </Feld>
          <Feld label="Bearbeiter"><input aria-label="Bearbeiter" className={input} value={draft.bearbeiter} onChange={(e) => setDraft({ ...draft, bearbeiter: e.target.value })} /></Feld>
          <label className="flex items-center gap-2 text-sm sm:mt-5">
            <input type="checkbox" checked={draft.karteAlsFoto} onChange={(e) => setDraft({ ...draft, karteAlsFoto: e.target.checked })} />
            Lagekarte als Foto der analogen Karte
          </label>
        </div>
      </Card>

      <Card title="Kräfte und Stärke" subtitle="Nur taktische Einheiten; Stärke nur als Gesamtzahl (Festlegung Gemeinde)"
        actions={<Button size="sm" variant="secondary" onClick={uebernehmeKraefte}>Aus Kräfteübersicht übernehmen</Button>}>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          {KRAEFTE_FELDER.map(([k, l]) => (
            <Feld key={k} label={l}>
              <textarea aria-label={l} rows={2} className={input} value={d.kraefte?.[k] ?? ''} placeholder="./."
                onChange={(e) => setData({ kraefte: { ...d.kraefte, [k]: e.target.value } })} />
            </Feld>
          ))}
          <div className="grid grid-cols-2 gap-3">
            <Feld label="Stärke gesamt (Personen)">
              <input aria-label="Stärke gesamt" type="number" min={0} className={input} value={d.staerke?.gesamt ?? ''}
                onChange={(e) => setData({ staerke: { gesamt: e.target.value === '' ? null : Math.max(0, Math.round(Number(e.target.value))) } })} />
            </Feld>
            <Feld label="Einheitenliste">
              <p className="text-sm py-1">{d.einheiten?.length ?? 0} Einheit(en){d.einheiten?.length ? <button className="ml-2 text-xs text-gray-500 underline" onClick={() => setData({ einheiten: [] })}>leeren</button> : null}</p>
            </Feld>
          </div>
        </div>
      </Card>

      <Card title="Schäden" subtitle="Momentaufnahme, nicht doppelt zählen; Verstorbene = Gesamtzahl">
        <div className="overflow-x-auto">
          <table className="text-sm">
            <thead><tr className="text-xs text-gray-500"><th className="text-left pr-4">Art</th><th className="px-2">? unbestätigt</th><th className="px-2">! bestätigt</th><th className="px-2">Σ</th></tr></thead>
            <tbody>
              {SCHAEDEN.map(([k, l]) => {
                const v = d.schaden?.[k] ?? [0, 0];
                return (
                  <tr key={k}>
                    <td className="pr-4 py-0.5">{l}</td>
                    {([0, 1] as const).map((i) => (
                      <td key={i} className="px-2"><input aria-label={`${l} ${i === 0 ? 'unbestätigt' : 'bestätigt'}`} type="number" min={0}
                        className={cn(input, 'w-20')} value={v[i] || ''} onChange={(e) => schaden(k, i, e.target.value)} /></td>
                    ))}
                    <td className="px-2 text-center tabular-nums">{v[0] + v[1] || ''}</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
        <div className="mt-3"><Feld label="Umweltschäden"><input aria-label="Umweltschäden" className={input} value={d.umwelt ?? ''} onChange={(e) => setData({ umwelt: e.target.value })} /></Feld></div>
      </Card>

      <Card title="KRITIS">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {kritis('kritis_bedroht', 'Bedroht')}
          {kritis('kritis_ausgefallen', 'Ausgefallen')}
        </div>
      </Card>

      <Card title="Lagebeurteilung" subtitle="1 geringfügig/keine Probleme · 2 erheblich/kompensiert · 3 kritisch/nicht kompensierbar">
        <table className="text-sm">
          <tbody>
            {BEURTEILUNG.map(([k, l]) => {
              const e = d.beurteilung?.[k] ?? {};
              const set = (patch: object) => setData({ beurteilung: { ...d.beurteilung, [k]: { ...e, ...patch } } });
              return (
                <tr key={k}>
                  <td className="pr-4 py-1">{l}</td>
                  {[1, 2, 3].map((s) => (
                    <td key={s} className="px-2">
                      <label className="flex items-center gap-1">
                        <input type="radio" name={`beurteilung-${k}`} aria-label={`${l} Stufe ${s}`} checked={e.stufe === s} onChange={() => set({ stufe: s })} />{s}
                      </label>
                    </td>
                  ))}
                  <td className="pl-3">
                    <select aria-label={`${l} Prognose`} className={input} value={e.prognose ?? ''} onChange={(ev) => set({ prognose: ev.target.value || null })}>
                      <option value="">Prognose …</option>
                      {PROGNOSEN.map((p) => <option key={p}>{p}</option>)}
                    </select>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </Card>

      <Card title="Aufgaben und Schwerpunkte" subtitle="Nur aktive Aufgaben; genau ein aufgabenbezogener Schwerpunkt. Texte ≤ 30 Zeichen (schmale Zellen), Details in die Kurzfassung.">
        <datalist id="s2-th">{TH_TEXTE.map((t) => <option key={t} value={t} />)}</datalist>
        <div className="space-y-1">
          {AUFGABEN.map(([k, l]) => {
            const e = d.aufgaben?.[k];
            const set = (v: typeof e) => {
              const a = { ...(d.aufgaben ?? {}) };
              if (v) a[k] = v; else delete a[k];
              setData({ aufgaben: a });
            };
            return (
              <div key={k} className="flex flex-wrap items-center gap-2 text-sm">
                <label className="flex items-center gap-2 w-48">
                  <input type="checkbox" checked={!!e} onChange={(ev) => set(ev.target.checked ? { text: '' } : undefined)} />{l}
                </label>
                {e && (<>
                  {k === 'sonstige' && <input aria-label="Sonstige Bezeichnung" className={cn(input, 'w-36')} placeholder="Bezeichnung" value={e.bezeichnung ?? ''} onChange={(ev) => set({ ...e, bezeichnung: ev.target.value })} />}
                  <input aria-label={`${l} Text`} className={cn(input, 'flex-1 min-w-40', (e.text?.length ?? 0) > 30 && 'border-amber-500')} value={e.text ?? ''}
                    list={k === 'technische_hilfe' ? 's2-th' : undefined} onChange={(ev) => set({ ...e, text: ev.target.value })} />
                  <label className="flex items-center gap-1">
                    <input type="radio" name="aufgabe-schwerpunkt" checked={!!e.schwerpunkt}
                      onChange={() => setData({ aufgaben: Object.fromEntries(Object.entries(d.aufgaben ?? {}).map(([kk, vv]) => [kk, { ...vv, schwerpunkt: kk === k }])) })} />
                    Schwerpunkt
                  </label>
                </>)}
              </div>
            );
          })}
        </div>
        <p className="mt-3 text-sm text-gray-600">
          Raumbezogen (aus den Einsatzabschnitten): {lage.abschnitte.length === 0 ? 'keine' : lage.abschnitte.map((a) => `EA ${a.nr} ${a.name}${a.schwerpunkt ? ' (Schwerpunkt)' : ''}`).join(' · ')}
        </p>
      </Card>

      <Card title="Lage, Probleme, Maßnahmen">
        <div className="space-y-3">
          <Feld label="Probleme / Bedarfe (max. 3, nur was die TEL nicht selbst lösen kann)">
            <textarea aria-label="Probleme / Bedarfe" rows={3} className={input} value={d.probleme ?? ''} placeholder={'1. …\n2. …'} onChange={(e) => setData({ probleme: e.target.value })} />
          </Feld>
          <Feld label="Kurzfassung der Lage (Zeile „Allgemein: …“, dann je EA „EA <Name>: …“)">
            <textarea aria-label="Kurzfassung" rows={5} className={input} value={d.kurzfassung ?? ''} onChange={(e) => setData({ kurzfassung: e.target.value })} />
            {!d.kurzfassung?.trim() && <button className="text-xs text-blue-700 underline" onClick={kurzfassungVorlage}>Zeilen je EA vorbereiten</button>}
          </Feld>
          <Feld label="Maßnahmen (Menschenrettung, Evakuierung/Warnung, KRITIS)">
            <textarea aria-label="Maßnahmen" rows={3} className={input} value={d.massnahmen ?? ''} onChange={(e) => setData({ massnahmen: e.target.value })} />
          </Feld>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <Feld label="Sperrungen / abgeschnittene Ortsteile"><textarea aria-label="Sperrungen" rows={2} className={input} value={d.sperrungen ?? ''} onChange={(e) => setData({ sperrungen: e.target.value })} /></Feld>
            <Feld label="Presse / Medien"><textarea aria-label="Presse" rows={2} className={input} value={d.presse ?? ''} onChange={(e) => setData({ presse: e.target.value })} /></Feld>
            <Feld label="Verpflegung extern: Portionen">
              <input aria-label="Portionen" type="number" min={0} className={input} value={d.verpflegung?.portionen ?? ''}
                onChange={(e) => setData({ verpflegung: { ...d.verpflegung, portionen: e.target.value === '' ? null : Number(e.target.value) } })} />
            </Feld>
            <Feld label="Verpflegung Zeitpunkt (takt. Zeit)">
              <input aria-label="Verpflegung Zeitpunkt" className={input} value={d.verpflegung?.zeitpunkt ?? ''} placeholder="141800nov26"
                onChange={(e) => setData({ verpflegung: { ...d.verpflegung, zeitpunkt: e.target.value.trim() || null } })} />
            </Feld>
          </div>
          <Feld label="Bemerkungen"><textarea aria-label="Bemerkungen" rows={2} className={input} value={d.bemerkungen ?? ''} onChange={(e) => setData({ bemerkungen: e.target.value })} /></Feld>
        </div>
      </Card>

      <Card title="Prüfen und versenden" subtitle="Die App versendet nichts selbst: Dateien herunterladen und mit dem Mailtext versenden.">
        <div className="space-y-4">
          {pruefung && (pruefung.warnungen.length === 0
            ? <p className="text-sm text-green-700">Keine Auffälligkeiten.</p>
            : (
              <ul className="text-sm text-amber-800 list-disc pl-5" data-testid="s2-warnungen">
                {pruefung.warnungen.map((w) => <li key={w}>{w}</li>)}
              </ul>
            ))}
          <div className="flex flex-wrap gap-2">
            <Button size="sm" variant="secondary" icon={<ArrowDownTrayIcon />} onClick={() => download('lagebericht.xlsx')}>Lagebericht (Excel)</Button>
            <Button size="sm" variant="secondary" icon={<ArrowDownTrayIcon />} onClick={() => download('lagekarte.pdf')}>Lagekarte (PDF, Folie 1)</Button>
            <Button size="sm" variant="secondary" icon={<ArrowDownTrayIcon />} onClick={() => download('lagekarte.pptx')}>Lagekarte (PowerPoint)</Button>
          </div>
          {pruefung && (
            <div className="rounded-md border border-gray-200 p-3 space-y-2 text-sm">
              <p><span className="text-gray-500">An:</span> {pruefung.mail.an}</p>
              <div className="flex items-start gap-2">
                <p className="flex-1"><span className="text-gray-500">Betreff:</span> <span data-testid="s2-mail-betreff">{pruefung.mail.betreff}</span></p>
                <button aria-label="Betreff kopieren" className="text-gray-500 hover:text-gray-800" onClick={() => kopiere(pruefung.mail.betreff, 'Betreff')}><ClipboardDocumentIcon className="h-4 w-4" /></button>
              </div>
              <p><span className="text-gray-500">Anhänge:</span> {pruefung.mail.anhaenge.join(', ')}</p>
              <div className="relative">
                <pre className="whitespace-pre-wrap font-sans bg-gray-50 rounded p-2 pr-8">{pruefung.mail.text}</pre>
                <button aria-label="Mailtext kopieren" className="absolute top-2 right-2 text-gray-500 hover:text-gray-800" onClick={() => kopiere(pruefung.mail.text, 'Mailtext')}><ClipboardDocumentIcon className="h-4 w-4" /></button>
              </div>
              <div className="flex flex-wrap gap-2">
                <a className="inline-flex items-center gap-1 rounded-md border border-gray-300 px-3 py-1.5 text-sm hover:bg-gray-50"
                  href={`mailto:${pruefung.mail.an}?subject=${encodeURIComponent(pruefung.mail.betreff)}&body=${encodeURIComponent(pruefung.mail.text)}`}>
                  <EnvelopeIcon className="h-4 w-4" /> Im Mailprogramm öffnen
                </a>
                {basis.status === 'entwurf'
                  ? <Button size="sm" variant="primary" disabled={dirty} onClick={() => speichern({ status: 'versendet' })}>Als versendet markieren</Button>
                  : <Button size="sm" variant="ghost" disabled={dirty} onClick={() => speichern({ status: 'entwurf' })}>Zurück auf Entwurf</Button>}
              </div>
              <p className="text-xs text-gray-500">Anhänge im Mailprogramm selbst anfügen. Zeitkritische Nachforderungen zusätzlich per Funk/Draht.</p>
            </div>
          )}
        </div>
      </Card>
    </div>
  );
}
