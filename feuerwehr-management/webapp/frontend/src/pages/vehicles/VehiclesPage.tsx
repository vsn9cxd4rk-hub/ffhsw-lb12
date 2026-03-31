import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { PlusIcon } from '@heroicons/react/24/outline';
import { vehiclesApi } from '../../api/vehicles';
import { Vehicle } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';

function VehicleFormModal({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    name: '', licensePlate: '', callSign: '', minCrew: '', maxCrew: '', licenseClass: '',
  });
  const [error, setError] = useState('');

  const mutation = useMutation({
    mutationFn: () => vehiclesApi.create({
      name: form.name,
      licensePlate: form.licensePlate || undefined,
      callSign: form.callSign || undefined,
      minCrew: form.minCrew ? parseInt(form.minCrew) : undefined,
      maxCrew: form.maxCrew ? parseInt(form.maxCrew) : undefined,
      licenseClass: form.licenseClass || undefined,
    }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vehicles'] });
      setError('');
      onClose();
    },
    onError: (err: unknown) => {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Fahrzeug konnte nicht angelegt werden';
      setError(msg);
    },
  });

  const update = (f: string, v: string) => setForm(prev => ({ ...prev, [f]: v }));

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Neues Fahrzeug" size="md"
      footer={
        <>
          <Button variant="secondary" onClick={onClose}>Abbrechen</Button>
          <Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.name}>
            Anlegen
          </Button>
        </>
      }
    >
      <div className="space-y-3">
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>
        )}
        <Input label="Bezeichnung" value={form.name} onChange={(e) => update('name', e.target.value)} required />
        <Input label="Kennzeichen" value={form.licensePlate} onChange={(e) => update('licensePlate', e.target.value)} />
        <Input label="Funkrufname" value={form.callSign} onChange={(e) => update('callSign', e.target.value)} />
        <div className="grid grid-cols-2 gap-3">
          <Input label="Min. Besatzung" value={form.minCrew} onChange={(e) => update('minCrew', e.target.value)} type="number" />
          <Input label="Max. Besatzung" value={form.maxCrew} onChange={(e) => update('maxCrew', e.target.value)} type="number" />
        </div>
        <Input label="Führerscheinklasse" value={form.licenseClass} onChange={(e) => update('licenseClass', e.target.value)} />
      </div>
    </Modal>
  );
}

export function VehiclesPage() {
  const navigate = useNavigate();
  const [showRetired, setShowRetired] = useState(false);
  const [showCreate, setShowCreate] = useState(false);

  const { data, isLoading } = useQuery({
    queryKey: ['vehicles', showRetired],
    queryFn: () =>
      vehiclesApi.getAll(showRetired ? undefined : { isRetired: false }).then((r) => r.data.data),
  });

  const columns = [
    { key: 'name', header: 'Fahrzeug', render: (v: Vehicle) => (
      <div><p className="font-medium">{v.name}</p>{v.callSign && <p className="text-xs text-gray-500">{v.callSign}</p>}</div>
    )},
    { key: 'licensePlate', header: 'Kennzeichen', render: (v: Vehicle) => v.licensePlate || '-' },
    { key: 'crew', header: 'Besatzung', render: (v: Vehicle) =>
      v.minCrew || v.maxCrew ? `${v.minCrew || '?'} - ${v.maxCrew || '?'}` : '-'
    },
    { key: 'licenseClass', header: 'Führerschein', render: (v: Vehicle) => v.licenseClass || '-' },
    { key: 'status', header: 'Status', render: (v: Vehicle) => (
      <Badge variant={v.isRetired ? 'default' : 'success'}>{v.isRetired ? 'Außer Dienst' : 'Aktiv'}</Badge>
    )},
  ];

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <label className="flex items-center gap-2 text-sm text-gray-700 cursor-pointer">
            <input type="checkbox" checked={showRetired} onChange={(e) => setShowRetired(e.target.checked)} className="rounded" />
            Außer Dienst zeigen
          </label>
        </div>
        <Button variant="primary" icon={<PlusIcon />} onClick={() => setShowCreate(true)}>
          Neues Fahrzeug
        </Button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table
          columns={columns}
          data={data || []}
          loading={isLoading}
          emptyMessage="Keine Fahrzeuge vorhanden."
          onRowClick={(v) => navigate(`/vehicles/${v.id}`)}
          keyExtractor={(v) => v.id}
        />
      </div>

      <VehicleFormModal isOpen={showCreate} onClose={() => setShowCreate(false)} />
    </div>
  );
}
