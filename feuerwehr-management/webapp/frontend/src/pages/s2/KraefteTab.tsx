import { useState } from 'react';
import { TrashIcon, PencilSquareIcon } from '@heroicons/react/24/outline';
import type { S2Abschnitt, S2Kraft, S2LageState } from '../../api/s2';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Modal } from '../../components/ui/Modal';
import { Textarea } from '../../components/ui/Textarea';
import { FORMATIONEN, KRAFT_STATUS, ORGS, staerkeGesamt } from '../../utils/s2';
import { cn } from '../../utils/cn';
import { useItemMutations } from './useS2';

const sel = 'border border-gray-300 rounded-md px-2 py-1 text-sm bg-white';

/** Text field that saves on blur/Enter only when the value changed. */
function InlineText({ value, onSave, className, placeholder, label }: {
  value: string; onSave: (v: string) => void; className?: string; placeholder?: string; label: string;
}) {
  const [v, setV] = useState(value);
  const [basis, setBasis] = useState(value);
  if (value !== basis) { setBasis(value); setV(value); } // colleague changed it meanwhile
  return (
    <input aria-label={label} className={cn('border border-gray-300 rounded-md px-2 py-1 text-sm', className)} value={v}
      placeholder={placeholder} onChange={(e) => setV(e.target.value)}
      onBlur={() => { if (v !== value) onSave(v); }}
      onKeyDown={(e) => { if (e.key === 'Enter') (e.target as HTMLInputElement).blur(); }} />
  );
}

const neueKraft = { org: 'FW', formation: 'Löschbezirk', bezeichnung: '', staerke: '', status: 'alarmiert', abschnittId: '' };

export function KraefteTab({ lage }: { lage: S2LageState }) {
  const ab = useItemMutations<S2Abschnitt>(lage.id, 'abschnitte');
  const kr = useItemMutations<S2Kraft>(lage.id, 'kraefte');
  const [eaEdit, setEaEdit] = useState<Partial<S2Abschnitt> | null>(null);
  const [kraft, setKraft] = useState(neueKraft);
  const u = lage.kraefteUebersicht;

  const freieNr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10].find((n) => !lage.abschnitte.some((a) => a.nr === n));

  const speichereEa = () => {
    const { id, nr, name, lage: text, schwerpunkt } = eaEdit!;
    const data = { nr, name, lage: text, schwerpunkt };
    const done = { onSuccess: () => setEaEdit(null) };
    if (id) ab.update.mutate({ item: lage.abschnitte.find((a) => a.id === id)!, changes: data }, done);
    else ab.create.mutate(data, done);
  };

  // Clear the input right away, so the next unit can be typed while the request runs;
  // on an error the entry comes back unless something new was typed meanwhile
  const fuegeKraftHinzu = () => {
    const eintrag = kraft;
    setKraft({ ...kraft, bezeichnung: '', staerke: '' });
    kr.create.mutate({ ...eintrag, abschnittId: eintrag.abschnittId ? Number(eintrag.abschnittId) : null } as Partial<S2Kraft>,
      { onError: () => setKraft((k) => (k.bezeichnung || k.staerke ? k : eintrag)) });
  };

  const proStatus = KRAFT_STATUS.map((s) => ({
    ...s, anzahl: lage.kraefte.filter((k) => k.status === s.value).length,
  }));

  return (
    <div className="space-y-4">
      <Card title="Einsatzabschnitte" subtitle="Nummer = EA-Zeichen auf der Karte und Zeile „raumbezogen“ im Lagebericht. Grenze im Tab Lagekarte zeichnen."
        actions={freieNr && <Button size="sm" variant="primary" onClick={() => setEaEdit({ nr: freieNr, name: '', lage: '', schwerpunkt: false })}>Abschnitt anlegen</Button>}>
        {lage.abschnitte.length === 0 ? <p className="text-sm text-gray-500">Noch keine Einsatzabschnitte.</p> : (
          <ul className="divide-y divide-gray-100">
            {lage.abschnitte.map((a) => (
              <li key={a.id} className="py-2 flex items-start gap-3">
                <span className="inline-flex h-7 w-7 shrink-0 items-center justify-center rounded-full bg-yellow-300 font-bold text-sm">{a.nr}</span>
                <div className="flex-1 min-w-0">
                  <p className="font-medium text-sm">
                    EA {a.nr}: {a.name}
                    {a.schwerpunkt && <span className="ml-2 rounded bg-red-600 px-1.5 text-xs text-white">Schwerpunkt</span>}
                    {!a.flaeche && <span className="ml-2 text-xs text-amber-700">keine Grenze auf der Karte</span>}
                  </p>
                  {a.lage && <p className="text-sm text-gray-600">{a.lage}</p>}
                  <p className="text-xs text-gray-400">
                    {lage.kraefte.filter((k) => k.abschnittId === a.id && k.status !== 'entlassen').map((k) => k.bezeichnung).join(', ') || 'keine Kräfte zugeordnet'}
                  </p>
                </div>
                <button aria-label={`EA ${a.nr} bearbeiten`} className="text-gray-400 hover:text-gray-700" onClick={() => setEaEdit(a)}>
                  <PencilSquareIcon className="h-4 w-4" />
                </button>
                <button aria-label={`EA ${a.nr} löschen`} className="text-gray-400 hover:text-red-600" onClick={() => ab.remove.mutate(a)}>
                  <TrashIcon className="h-4 w-4" />
                </button>
              </li>
            ))}
          </ul>
        )}
      </Card>

      <Card title="Kräfte" subtitle="Stärke wie gewohnt erfassen (1/8/9, 1/6 oder Gesamtzahl). Im Lagebericht steht nur die Gesamtzahl.">
        <div className="mb-4 flex flex-wrap gap-x-6 gap-y-1 text-sm">
          <span><strong data-testid="s2-staerke-gesamt">{u.staerkeGesamt}</strong> Personen gesamt</span>
          {proStatus.filter((s) => s.anzahl > 0).map((s) => <span key={s.value} className="text-gray-600">{s.label}: {s.anzahl}</span>)}
          {u.ohneStaerke.length > 0 && <span className="text-amber-700">ohne Stärke: {u.ohneStaerke.join(', ')}</span>}
        </div>

        <div className="overflow-x-auto">
          <table className="min-w-full text-sm">
            <thead>
              <tr className="text-left text-xs uppercase text-gray-500">
                <th className="py-1 pr-2">Org</th><th className="pr-2">Formation</th><th className="pr-2">Einheit / Rufname</th>
                <th className="pr-2">Stärke</th><th className="pr-2">Status</th><th className="pr-2">EA</th><th className="pr-2">Bemerkung</th><th />
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {lage.kraefte.map((k) => {
                const n = staerkeGesamt(k.staerke);
                const save = (changes: Partial<S2Kraft>) => kr.update.mutate({ item: k, changes });
                return (
                  <tr key={k.id} className={cn(k.status === 'entlassen' && 'opacity-50')}>
                    <td className="py-1 pr-2">
                      <select aria-label="Organisation" className={sel} value={k.org} onChange={(e) => save({ org: e.target.value })}>
                        {ORGS.map((o) => <option key={o}>{o}</option>)}
                      </select>
                    </td>
                    <td className="pr-2">
                      <select aria-label="Formation" className={cn(sel, 'max-w-48')} value={k.formation ?? ''} onChange={(e) => save({ formation: e.target.value || null })}>
                        <option value="">– (Einzelfahrzeug)</option>
                        {FORMATIONEN.map((f) => <option key={f}>{f}</option>)}
                      </select>
                    </td>
                    <td className="pr-2"><InlineText label="Einheit" value={k.bezeichnung} onSave={(v) => save({ bezeichnung: v })} className="w-36" /></td>
                    <td className="pr-2 whitespace-nowrap">
                      <InlineText label="Stärke" value={k.staerke ?? ''} onSave={(v) => save({ staerke: v })} className="w-20" placeholder="1/8/9" />
                      <span className="ml-1 text-xs text-gray-500">{n !== null ? `= ${n}` : ''}</span>
                    </td>
                    <td className="pr-2">
                      <select aria-label="Status" className={sel} value={k.status} onChange={(e) => save({ status: e.target.value })}>
                        {KRAFT_STATUS.map((s) => <option key={s.value} value={s.value}>{s.label}</option>)}
                      </select>
                    </td>
                    <td className="pr-2">
                      <select aria-label="Einsatzabschnitt" className={sel} value={k.abschnittId ?? ''}
                        onChange={(e) => save({ abschnittId: e.target.value ? Number(e.target.value) : null })}>
                        <option value="">–</option>
                        {lage.abschnitte.map((a) => <option key={a.id} value={a.id}>EA {a.nr}</option>)}
                      </select>
                    </td>
                    <td className="pr-2"><InlineText label="Bemerkung" value={k.bemerkung ?? ''} onSave={(v) => save({ bemerkung: v })} className="w-40" /></td>
                    <td>
                      <button aria-label={`${k.bezeichnung} löschen`} className="text-gray-400 hover:text-red-600" onClick={() => kr.remove.mutate(k)}>
                        <TrashIcon className="h-4 w-4" />
                      </button>
                    </td>
                  </tr>
                );
              })}
              <tr className="bg-gray-50">
                <td className="py-2 pr-2">
                  <select aria-label="Neue Kraft Organisation" className={sel} value={kraft.org} onChange={(e) => setKraft({ ...kraft, org: e.target.value })}>
                    {ORGS.map((o) => <option key={o}>{o}</option>)}
                  </select>
                </td>
                <td className="pr-2">
                  <select aria-label="Neue Kraft Formation" className={cn(sel, 'max-w-48')} value={kraft.formation} onChange={(e) => setKraft({ ...kraft, formation: e.target.value })}>
                    <option value="">– (Einzelfahrzeug)</option>
                    {FORMATIONEN.map((f) => <option key={f}>{f}</option>)}
                  </select>
                </td>
                <td className="pr-2">
                  <input aria-label="Neue Kraft Einheit" className="border border-gray-300 rounded-md px-2 py-1 text-sm w-36" placeholder="LB 111 / HSW 1/23"
                    value={kraft.bezeichnung} onChange={(e) => setKraft({ ...kraft, bezeichnung: e.target.value })}
                    onKeyDown={(e) => { if (e.key === 'Enter' && kraft.bezeichnung.trim()) fuegeKraftHinzu(); }} />
                </td>
                <td className="pr-2">
                  <input aria-label="Neue Kraft Stärke" className="border border-gray-300 rounded-md px-2 py-1 text-sm w-20" placeholder="1/8/9"
                    value={kraft.staerke} onChange={(e) => setKraft({ ...kraft, staerke: e.target.value })}
                    onKeyDown={(e) => { if (e.key === 'Enter' && kraft.bezeichnung.trim()) fuegeKraftHinzu(); }} />
                </td>
                <td className="pr-2">
                  <select aria-label="Neue Kraft Status" className={sel} value={kraft.status} onChange={(e) => setKraft({ ...kraft, status: e.target.value })}>
                    {KRAFT_STATUS.map((s) => <option key={s.value} value={s.value}>{s.label}</option>)}
                  </select>
                </td>
                <td className="pr-2">
                  <select aria-label="Neue Kraft Einsatzabschnitt" className={sel} value={kraft.abschnittId} onChange={(e) => setKraft({ ...kraft, abschnittId: e.target.value })}>
                    <option value="">–</option>
                    {lage.abschnitte.map((a) => <option key={a.id} value={a.id}>EA {a.nr}</option>)}
                  </select>
                </td>
                <td colSpan={2}>
                  <Button size="sm" variant="primary" disabled={!kraft.bezeichnung.trim()} loading={kr.create.isPending} onClick={fuegeKraftHinzu}>
                    Hinzufügen
                  </Button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </Card>

      <Modal isOpen={!!eaEdit} onClose={() => setEaEdit(null)} title={eaEdit?.id ? `EA ${eaEdit.nr} bearbeiten` : 'Einsatzabschnitt anlegen'}
        footer={<>
          <Button variant="secondary" onClick={() => setEaEdit(null)}>Abbrechen</Button>
          <Button variant="primary" disabled={!eaEdit?.name?.trim()} loading={ab.create.isPending || ab.update.isPending} onClick={speichereEa}>Speichern</Button>
        </>}>
        {eaEdit && (
          <div className="space-y-4">
            <div className="grid grid-cols-4 gap-3">
              <Input label="Nr." type="number" min={1} max={10} value={eaEdit.nr ?? ''} onChange={(e) => setEaEdit({ ...eaEdit, nr: Number(e.target.value) })} />
              <div className="col-span-3">
                <Input label="Name" value={eaEdit.name ?? ''} placeholder="z. B. Dilsburg/Köllerbach" onChange={(e) => setEaEdit({ ...eaEdit, name: e.target.value })} />
              </div>
            </div>
            <Textarea label="Lage im Abschnitt" rows={3} value={eaEdit.lage ?? ''} onChange={(e) => setEaEdit({ ...eaEdit, lage: e.target.value })} />
            <label className="flex items-center gap-2 text-sm">
              <input type="checkbox" checked={!!eaEdit.schwerpunkt} onChange={(e) => setEaEdit({ ...eaEdit, schwerpunkt: e.target.checked })} />
              Raumbezogener Schwerpunkt (nur ein EA, im Lagebericht rot)
            </label>
          </div>
        )}
      </Modal>
    </div>
  );
}
