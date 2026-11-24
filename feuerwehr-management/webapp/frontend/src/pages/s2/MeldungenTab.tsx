import { useState } from 'react';
import { TrashIcon } from '@heroicons/react/24/outline';
import type { S2LageState, S2Meldung } from '../../api/s2';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { cn } from '../../utils/cn';
import { useItemMutations } from './useS2';

function lokal(d: Date): string {
  const p = (n: number) => String(n).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}T${p(d.getHours())}:${p(d.getMinutes())}`;
}

const leer = () => ({ nr: '', eingangAt: lokal(new Date()), absender: '', ort: '', inhalt: '' });

/**
 * Meldungseingang: Einzelmeldungen (Nachrichtenvordruck, Funk, Telefon) mit Zeitstempel. Wer
 * eine Meldung in Lagebericht oder Karte eingearbeitet hat, hakt sie ab.
 */
export function MeldungenTab({ lage }: { lage: S2LageState }) {
  const { create, update, remove } = useItemMutations<S2Meldung>(lage.id, 'meldungen');
  const [form, setForm] = useState(leer);
  const [nurOffen, setNurOffen] = useState(false);

  const speichern = () => {
    const eintrag = form;
    setForm(leer());
    create.mutate({ ...eintrag, eingangAt: new Date(eintrag.eingangAt).toISOString() } as Partial<S2Meldung>,
      { onError: () => setForm((f) => (f.inhalt.trim() ? f : eintrag)) });
  };
  const liste = nurOffen ? lage.meldungen.filter((m) => !m.erledigt) : lage.meldungen;

  return (
    <div className="grid grid-cols-1 xl:grid-cols-3 gap-4">
      <Card title="Neue Meldung">
        <div className="space-y-3">
          <div className="grid grid-cols-2 gap-3">
            <Input label="Nachweisungs-Nr." value={form.nr} onChange={(e) => setForm({ ...form, nr: e.target.value })} />
            <Input label="Eingang" type="datetime-local" value={form.eingangAt} onChange={(e) => setForm({ ...form, eingangAt: e.target.value })} />
          </div>
          <Input label="Absender" value={form.absender} placeholder="z. B. EA 1, LB 111, ILS" onChange={(e) => setForm({ ...form, absender: e.target.value })} />
          <Input label="Ort" value={form.ort} placeholder="Adresse oder Gitter 351,2/5466,8" onChange={(e) => setForm({ ...form, ort: e.target.value })} />
          <Textarea label="Inhalt" rows={4} value={form.inhalt} onChange={(e) => setForm({ ...form, inhalt: e.target.value })} />
          <Button variant="primary" loading={create.isPending} disabled={!form.inhalt.trim()} onClick={speichern}>Meldung erfassen</Button>
        </div>
      </Card>

      <Card title={`Meldungen (${lage.meldungen.length})`} className="xl:col-span-2"
        actions={<label className="flex items-center gap-2 text-sm">
          <input type="checkbox" checked={nurOffen} onChange={(e) => setNurOffen(e.target.checked)} /> nur offene
        </label>}>
        {liste.length === 0 ? <p className="text-sm text-gray-500">Keine Meldungen.</p> : (
          <ul className="divide-y divide-gray-100">
            {liste.map((m) => (
              <li key={m.id} className={cn('py-3 flex gap-3', m.erledigt && 'opacity-60')}>
                <input type="checkbox" className="mt-1" checked={m.erledigt} aria-label="eingearbeitet"
                  title="In Lagebericht/Lagekarte eingearbeitet"
                  onChange={(e) => update.mutate({ item: m, changes: { erledigt: e.target.checked } })} />
                <div className="flex-1 min-w-0">
                  <p className="text-xs text-gray-500">
                    {new Date(m.eingangAt).toLocaleString('de-DE', { dateStyle: 'short', timeStyle: 'short' })}
                    {m.nr && ` · Nr. ${m.nr}`}{m.absender && ` · von ${m.absender}`}{m.ort && ` · ${m.ort}`}
                    {m.updatedBy && ` · erfasst/geändert von ${m.updatedBy}`}
                  </p>
                  <p className={cn('text-sm whitespace-pre-wrap', m.erledigt && 'line-through')}>{m.inhalt}</p>
                </div>
                <button className="text-gray-400 hover:text-red-600" aria-label="Meldung löschen" onClick={() => remove.mutate(m)}>
                  <TrashIcon className="h-4 w-4" />
                </button>
              </li>
            ))}
          </ul>
        )}
      </Card>
    </div>
  );
}
