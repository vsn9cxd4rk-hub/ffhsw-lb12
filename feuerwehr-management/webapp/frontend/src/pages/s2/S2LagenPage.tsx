import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { s2Api, type S2LageListItem } from '../../api/s2';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { useToastStore } from '../../components/ui/Toast';
import { formatDateTime } from '../../utils/format';
import { fehlertext } from '../../utils/s2';

export function S2LagenPage() {
  const navigate = useNavigate();
  const qc = useQueryClient();
  const addToast = useToastStore((s) => s.addToast);
  const [showCreate, setShowCreate] = useState(false);
  const [form, setForm] = useState({ name: '', isExercise: true, recipient: 'fuehrungsstab@saarbruecken.de', contact: '' });

  const { data, isLoading } = useQuery({
    queryKey: ['s2-lagen'],
    queryFn: () => s2Api.getLagen().then((r) => r.data.data),
    refetchInterval: 15_000,
  });

  const create = useMutation({
    mutationFn: () => s2Api.createLage(form).then((r) => r.data.data),
    onSuccess: (lage) => {
      qc.invalidateQueries({ queryKey: ['s2-lagen'] });
      setShowCreate(false);
      navigate(`/s2/${lage.id}`);
    },
    onError: async (err) => addToast(await fehlertext(err)),
  });

  const columns = [
    { key: 'name', header: 'Lage', render: (l: S2LageListItem) => (
      <div className="flex items-center gap-2">
        <span className="font-medium">{l.name}</span>
        {l.isExercise && <Badge variant="warning">Übung</Badge>}
      </div>
    ) },
    { key: 'status', header: 'Status', render: (l: S2LageListItem) => (
      <Badge variant={l.status === 'aktiv' ? 'success' : 'default'}>{l.status === 'aktiv' ? 'aktiv' : 'beendet'}</Badge>
    ) },
    { key: 'startedAt', header: 'Beginn', render: (l: S2LageListItem) => formatDateTime(l.startedAt) },
    { key: 'bericht', header: 'Letzter Lagebericht', render: (l: S2LageListItem) => (
      l.letzterBericht ? `Nr. ${l.letzterBericht.lfdNr} – ${l.letzterBericht.zeit}` : '–'
    ) },
    { key: 'counts', header: 'Kräfte / Meldungen', render: (l: S2LageListItem) => `${l._count.kraefte} / ${l._count.meldungen}` },
  ];

  return (
    <div className="space-y-4">
      <div className="flex flex-col sm:flex-row gap-3 justify-between sm:items-center">
        <p className="text-sm text-gray-600 max-w-3xl">
          Lagebericht (Excel-Vorlage RV Saarbrücken) und Lagekarte mit taktischen Zeichen für die TEL.
          Mehrere Bearbeiter können gleichzeitig arbeiten; Änderungen der anderen erscheinen nach wenigen Sekunden.
        </p>
        <Button variant="primary" icon={<PlusIcon />} onClick={() => setShowCreate(true)}>Neue Lage</Button>
      </div>

      <Table columns={columns} data={data ?? []} loading={isLoading}
        emptyMessage="Noch keine Lage angelegt." onRowClick={(l) => navigate(`/s2/${l.id}`)} />

      <Modal isOpen={showCreate} onClose={() => setShowCreate(false)} title="Neue Lage"
        footer={<>
          <Button variant="secondary" onClick={() => setShowCreate(false)}>Abbrechen</Button>
          <Button variant="primary" loading={create.isPending} disabled={!form.name.trim()} onClick={() => create.mutate()}>Anlegen</Button>
        </>}>
        <div className="space-y-4">
          <Input label="Bezeichnung" value={form.name} autoFocus
            placeholder="z. B. Unwetter Heusweiler 14.11." onChange={(e) => setForm({ ...form, name: e.target.value })} />
          <label className="flex items-center gap-2 text-sm">
            <input type="checkbox" checked={form.isExercise} onChange={(e) => setForm({ ...form, isExercise: e.target.checked })} />
            Übung (Kennzeichnung „ÜBUNG – ÜBUNG – ÜBUNG“ in der Mail)
          </label>
          <Input label="Empfänger Lagebericht" value={form.recipient}
            hint="In Übungen die von der Übungsleitung genannte Adresse" onChange={(e) => setForm({ ...form, recipient: e.target.value })} />
          <Input label="Erreichbarkeit TEL" value={form.contact} placeholder="Telefon / Funkrufname"
            onChange={(e) => setForm({ ...form, contact: e.target.value })} />
        </div>
      </Modal>
    </div>
  );
}
