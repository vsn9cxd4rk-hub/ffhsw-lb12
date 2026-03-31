import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeftIcon, PlusIcon } from '@heroicons/react/24/outline';
import { vehiclesApi } from '../../api/vehicles';
import { Vehicle, LogbookEntry } from '../../types';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { Table } from '../../components/ui/Table';
import { Badge } from '../../components/ui/Badge';
import { Modal } from '../../components/ui/Modal';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { formatDate } from '../../utils/format';

type Tab = 'fahrzeugdaten' | 'pruefungen' | 'fahrtenbuch';

export function VehicleDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [tab, setTab] = useState<Tab>('fahrzeugdaten');

  const { data: vehicle, isLoading } = useQuery({
    queryKey: ['vehicle', id],
    queryFn: () => vehiclesApi.getById(parseInt(id!)).then((r) => r.data.data),
  });

  const updateMutation = useMutation({
    mutationFn: (data: Partial<Vehicle>) => vehiclesApi.update(parseInt(id!), data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['vehicle', id] }),
  });

  const updateInspectionMutation = useMutation({
    mutationFn: (data: Record<string, string | null>) => vehiclesApi.updateInspection(parseInt(id!), data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['vehicle', id] }),
  });

  if (isLoading) return <LoadingSpinner />;
  if (!vehicle) return <div className="text-center py-12 text-gray-500">Fahrzeug nicht gefunden</div>;

  const tabs = [
    { id: 'fahrzeugdaten', label: 'Fahrzeugdaten' },
    { id: 'pruefungen', label: 'Prüfungen' },
    { id: 'fahrtenbuch', label: 'Fahrtenbuch' },
  ] as const;

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-3">
        <Button variant="ghost" onClick={() => navigate('/vehicles')} icon={<ArrowLeftIcon />} />
        <div>
          <h2 className="text-xl font-bold">{vehicle.name}</h2>
          <p className="text-sm text-gray-500">{vehicle.callSign} · {vehicle.licensePlate}</p>
        </div>
        <Badge variant={vehicle.isRetired ? 'default' : 'success'}>
          {vehicle.isRetired ? 'Außer Dienst' : 'Aktiv'}
        </Badge>
      </div>

      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {tabs.map((t) => (
            <button key={t.id} onClick={() => setTab(t.id)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'fahrzeugdaten' && (
        <FahrzeugdatenTab vehicle={vehicle} onSave={(data) => updateMutation.mutate(data)} saving={updateMutation.isPending} />
      )}
      {tab === 'pruefungen' && (
        <PruefungenTab vehicle={vehicle} onSave={(data) => updateInspectionMutation.mutate(data)} saving={updateInspectionMutation.isPending} />
      )}
      {tab === 'fahrtenbuch' && <FahrtenbuchTab vehicleId={parseInt(id!)} />}
    </div>
  );
}

function FahrzeugdatenTab({ vehicle, onSave, saving }: { vehicle: Vehicle; onSave: (d: Partial<Vehicle>) => void; saving: boolean }) {
  const [form, setForm] = useState({
    name: vehicle.name,
    licensePlate: vehicle.licensePlate || '',
    callSign: vehicle.callSign || '',
    minCrew: vehicle.minCrew?.toString() || '',
    maxCrew: vehicle.maxCrew?.toString() || '',
    licenseClass: vehicle.licenseClass || '',
    description: vehicle.description || '',
    isRetired: vehicle.isRetired,
  });

  const u = (f: string, v: string | boolean) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Card>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input label="Bezeichnung" value={form.name} onChange={(e) => u('name', e.target.value)} required />
        <Input label="Kennzeichen" value={form.licensePlate} onChange={(e) => u('licensePlate', e.target.value)} />
        <Input label="Funkrufname" value={form.callSign} onChange={(e) => u('callSign', e.target.value)} />
        <Input label="Führerscheinklasse" value={form.licenseClass} onChange={(e) => u('licenseClass', e.target.value)} />
        <Input label="Min. Besatzung" value={form.minCrew} onChange={(e) => u('minCrew', e.target.value)} type="number" />
        <Input label="Max. Besatzung" value={form.maxCrew} onChange={(e) => u('maxCrew', e.target.value)} type="number" />
        <div className="flex items-center gap-2">
          <input type="checkbox" id="isRetired" checked={form.isRetired} onChange={(e) => u('isRetired', e.target.checked)} className="rounded" />
          <label htmlFor="isRetired" className="text-sm text-gray-700">Außer Dienst</label>
        </div>
      </div>
      <div className="mt-6 flex justify-end">
        <Button variant="primary" onClick={() => onSave({ ...form, minCrew: form.minCrew ? parseInt(form.minCrew) : null, maxCrew: form.maxCrew ? parseInt(form.maxCrew) : null })} loading={saving}>
          Speichern
        </Button>
      </div>
    </Card>
  );
}

function PruefungenTab({ vehicle, onSave, saving }: { vehicle: Vehicle; onSave: (d: Record<string, string | null>) => void; saving: boolean }) {
  const insp = vehicle.inspection;
  const [form, setForm] = useState({
    tuevDate: insp?.tuevDate?.substring(0, 10) || '',
    spDate: insp?.spDate?.substring(0, 10) || '',
    serviceDate: insp?.serviceDate?.substring(0, 10) || '',
  });

  return (
    <Card title="Fahrzeugprüfungen">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <Input label="TÜV" value={form.tuevDate} onChange={(e) => setForm(f => ({ ...f, tuevDate: e.target.value }))} type="date" />
        <Input label="SP-Prüfung" value={form.spDate} onChange={(e) => setForm(f => ({ ...f, spDate: e.target.value }))} type="date" />
        <Input label="Service" value={form.serviceDate} onChange={(e) => setForm(f => ({ ...f, serviceDate: e.target.value }))} type="date" />
      </div>
      <div className="mt-4 flex justify-end">
        <Button variant="primary" onClick={() => onSave({ tuevDate: form.tuevDate || null, spDate: form.spDate || null, serviceDate: form.serviceDate || null })} loading={saving}>Speichern</Button>
      </div>

      {vehicle.equipmentInspections && vehicle.equipmentInspections.length > 0 && (
        <div className="mt-6">
          <h4 className="font-medium text-gray-700 mb-3">Gerätepüfungen</h4>
          <Table
            columns={[
              { key: 'type', header: 'Gerät' },
              { key: 'lastInspection', header: 'Letzte Prüfung', render: (r) => formatDate(r.lastInspection as string) },
              { key: 'nextInspection', header: 'Nächste Prüfung', render: (r) => formatDate(r.nextInspection as string) },
            ]}
            data={vehicle.equipmentInspections}
          />
        </div>
      )}
    </Card>
  );
}

function LogbookFormModal({ isOpen, onClose, vehicleId, entry }: { isOpen: boolean; onClose: () => void; vehicleId: number; entry?: LogbookEntry }) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    date: entry?.date?.substring(0, 10) || '',
    driver: entry?.driver || '',
    purpose: entry?.purpose || '',
    startMileage: entry?.startMileage?.toString() || '',
    endMileage: entry?.endMileage?.toString() || '',
    notes: entry?.notes || '',
  });
  const [error, setError] = useState('');

  const mutation = useMutation({
    mutationFn: () => {
      const data = {
        date: form.date ? new Date(form.date).toISOString() : undefined,
        driver: form.driver,
        purpose: form.purpose,
        startMileage: form.startMileage ? parseInt(form.startMileage) : undefined,
        endMileage: form.endMileage ? parseInt(form.endMileage) : undefined,
        notes: form.notes || undefined,
      };
      return entry
        ? vehiclesApi.updateLogbookEntry(vehicleId, entry.id, data)
        : vehiclesApi.createLogbookEntry(vehicleId, data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['logbook', vehicleId] });
      setError('');
      onClose();
    },
    onError: (err: unknown) => {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen';
      setError(msg);
    },
  });

  const update = (f: string, v: string) => setForm(prev => ({ ...prev, [f]: v }));
  const isValid = form.date && form.driver && form.purpose && form.startMileage && form.endMileage;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={entry ? 'Eintrag bearbeiten' : 'Neuer Fahrtenbucheintrag'} size="md"
      footer={
        <>
          <Button variant="secondary" onClick={onClose}>Abbrechen</Button>
          <Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!isValid}>
            Speichern
          </Button>
        </>
      }
    >
      <div className="space-y-3">
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>
        )}
        <Input label="Datum" value={form.date} onChange={(e) => update('date', e.target.value)} type="date" required />
        <Input label="Fahrer" value={form.driver} onChange={(e) => update('driver', e.target.value)} required />
        <Input label="Zweck" value={form.purpose} onChange={(e) => update('purpose', e.target.value)} required />
        <div className="grid grid-cols-2 gap-3">
          <Input label="km-Stand Start" value={form.startMileage} onChange={(e) => update('startMileage', e.target.value)} type="number" required />
          <Input label="km-Stand Ende" value={form.endMileage} onChange={(e) => update('endMileage', e.target.value)} type="number" required />
        </div>
        <Input label="Bemerkungen" value={form.notes} onChange={(e) => update('notes', e.target.value)} />
      </div>
    </Modal>
  );
}

function FahrtenbuchTab({ vehicleId }: { vehicleId: number }) {
  const [showModal, setShowModal] = useState(false);
  const [editEntry, setEditEntry] = useState<LogbookEntry | undefined>();

  const { data } = useQuery({
    queryKey: ['logbook', vehicleId],
    queryFn: () => vehiclesApi.getLogbook(vehicleId).then((r) => r.data.data),
  });

  const openCreate = () => { setEditEntry(undefined); setShowModal(true); };
  const openEdit = (entry: LogbookEntry) => { setEditEntry(entry); setShowModal(true); };
  const closeModal = () => { setShowModal(false); setEditEntry(undefined); };

  return (
    <Card title="Fahrtenbuch"
      actions={<Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neuer Eintrag</Button>}
    >
      <Table
        columns={[
          { key: 'date', header: 'Datum', render: (r) => formatDate((r as { date: string }).date) },
          { key: 'driver', header: 'Fahrer' },
          { key: 'purpose', header: 'Zweck' },
          { key: 'km', header: 'km', render: (r) => { const e = r as { endMileage: number; startMileage: number }; return `${e.endMileage - e.startMileage} km`; } },
        ]}
        data={data?.data || []}
        emptyMessage="Keine Fahrteneinträge vorhanden."
        onRowClick={(r) => openEdit(r as LogbookEntry)}
        keyExtractor={(r) => (r as LogbookEntry).id}
      />
      <LogbookFormModal isOpen={showModal} onClose={closeModal} vehicleId={vehicleId} entry={editEntry} />
    </Card>
  );
}
