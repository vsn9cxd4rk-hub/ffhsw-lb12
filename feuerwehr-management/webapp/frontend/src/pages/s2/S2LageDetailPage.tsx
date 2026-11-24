import { useState } from 'react';
import { useNavigate, useParams, useSearchParams } from 'react-router-dom';
import { ArrowLeftIcon, SignalIcon } from '@heroicons/react/24/outline';
import { s2Api } from '../../api/s2';
import { Badge } from '../../components/ui/Badge';
import { Button } from '../../components/ui/Button';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { ConfirmDialog } from '../../components/ui/ConfirmDialog';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { cn } from '../../utils/cn';
import { useLage, useS2Mutation } from './useS2';
import { MeldungenTab } from './MeldungenTab';
import { KraefteTab } from './KraefteTab';
import { LagekarteTab } from './LagekarteTab';
import { LageberichtTab } from './LageberichtTab';

const TABS = [
  { key: 'bericht', label: 'Lagebericht' },
  { key: 'karte', label: 'Lagekarte' },
  { key: 'kraefte', label: 'Abschnitte & Kräfte' },
  { key: 'meldungen', label: 'Meldungen' },
] as const;
type TabKey = (typeof TABS)[number]['key'];

export function S2LageDetailPage() {
  const id = Number(useParams().id);
  const navigate = useNavigate();
  const [params, setParams] = useSearchParams();
  const tab = (TABS.find((t) => t.key === params.get('tab'))?.key ?? 'bericht') as TabKey;
  const { data: lage, isLoading, error, dataUpdatedAt, isFetching } = useLage(id);
  const [edit, setEdit] = useState<{ name: string; recipient: string; contact: string; isExercise: boolean } | null>(null);
  const [confirmDelete, setConfirmDelete] = useState(false);

  const update = useS2Mutation(id, (changes: Record<string, unknown>) =>
    s2Api.updateLage(id, { ...changes, version: lage!.version }));
  const remove = useS2Mutation(id, () => s2Api.deleteLage(id).then(() => navigate('/s2')));

  if (isLoading) return <LoadingSpinner />;
  if (error || !lage) return <p className="text-red-600">Lage nicht gefunden.</p>;

  const offeneMeldungen = lage.meldungen.filter((m) => !m.erledigt).length;

  return (
    <div className="space-y-4">
      <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-3">
        <div className="flex items-center gap-3 min-w-0">
          <button onClick={() => navigate('/s2')} className="p-1 rounded hover:bg-gray-100" aria-label="Zurück">
            <ArrowLeftIcon className="h-5 w-5" />
          </button>
          <div className="min-w-0">
            <div className="flex items-center gap-2 flex-wrap">
              <h2 className="text-xl font-semibold truncate">{lage.name}</h2>
              {lage.isExercise && <Badge variant="warning">Übung</Badge>}
              <Badge variant={lage.status === 'aktiv' ? 'success' : 'default'}>{lage.status}</Badge>
            </div>
            <p className="text-xs text-gray-500 flex items-center gap-1" title="Die Seite lädt alle 5 Sekunden die Änderungen der anderen Bearbeiter">
              <SignalIcon className={cn('h-3.5 w-3.5', isFetching ? 'text-green-600' : 'text-gray-400')} />
              Stand {new Date(dataUpdatedAt).toLocaleTimeString('de-DE')} · {lage.kommune} · an {lage.recipient}
            </p>
          </div>
        </div>
        <div className="flex gap-2 flex-wrap">
          <Button size="sm" variant="secondary" onClick={() => setEdit({ name: lage.name, recipient: lage.recipient, contact: lage.contact ?? '', isExercise: lage.isExercise })}>
            Bearbeiten
          </Button>
          <Button size="sm" variant="secondary" onClick={() => update.mutate({ status: lage.status === 'aktiv' ? 'beendet' : 'aktiv' })}>
            {lage.status === 'aktiv' ? 'Lage beenden' : 'Wieder aktivieren'}
          </Button>
          <Button size="sm" variant="danger" onClick={() => setConfirmDelete(true)}>Löschen</Button>
        </div>
      </div>

      <nav className="flex gap-1 border-b border-gray-200 overflow-x-auto">
        {TABS.map((t) => (
          <button key={t.key} onClick={() => setParams({ tab: t.key }, { replace: true })}
            className={cn('px-4 py-2 text-sm font-medium border-b-2 -mb-px whitespace-nowrap',
              tab === t.key ? 'border-red-600 text-red-700' : 'border-transparent text-gray-600 hover:text-gray-900')}>
            {t.label}
            {t.key === 'meldungen' && offeneMeldungen > 0 && (
              <span className="ml-1.5 rounded-full bg-red-600 text-white text-xs px-1.5">{offeneMeldungen}</span>
            )}
          </button>
        ))}
      </nav>

      {tab === 'bericht' && <LageberichtTab lage={lage} />}
      {tab === 'karte' && <LagekarteTab lage={lage} />}
      {tab === 'kraefte' && <KraefteTab lage={lage} />}
      {tab === 'meldungen' && <MeldungenTab lage={lage} />}

      <Modal isOpen={!!edit} onClose={() => setEdit(null)} title="Lage bearbeiten"
        footer={<>
          <Button variant="secondary" onClick={() => setEdit(null)}>Abbrechen</Button>
          <Button variant="primary" loading={update.isPending} disabled={!edit?.name.trim()}
            onClick={() => update.mutate(edit!, { onSuccess: () => setEdit(null) })}>Speichern</Button>
        </>}>
        {edit && (
          <div className="space-y-4">
            <Input label="Bezeichnung" value={edit.name} onChange={(e) => setEdit({ ...edit, name: e.target.value })} />
            <label className="flex items-center gap-2 text-sm">
              <input type="checkbox" checked={edit.isExercise} onChange={(e) => setEdit({ ...edit, isExercise: e.target.checked })} />
              Übung
            </label>
            <Input label="Empfänger Lagebericht" value={edit.recipient} onChange={(e) => setEdit({ ...edit, recipient: e.target.value })} />
            <Input label="Erreichbarkeit TEL" value={edit.contact} onChange={(e) => setEdit({ ...edit, contact: e.target.value })} />
          </div>
        )}
      </Modal>

      <ConfirmDialog isOpen={confirmDelete} onClose={() => setConfirmDelete(false)} onConfirm={() => remove.mutate(undefined)}
        title="Lage löschen" loading={remove.isPending}
        message={`„${lage.name}“ mit allen Lageberichten, Kräften, Kartenobjekten und Meldungen endgültig löschen?`}
        confirmLabel="Endgültig löschen" />
    </div>
  );
}
