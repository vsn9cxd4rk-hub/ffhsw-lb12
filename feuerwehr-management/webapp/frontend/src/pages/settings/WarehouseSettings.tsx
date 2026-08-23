import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { AxiosResponse } from 'axios';
import { Warehouse } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Modal } from '../../components/ui/Modal';
import { Badge } from '../../components/ui/Badge';
import client from '../../api/client';

const warehouseApi = {
  getAll: () => client.get<{ data: Warehouse[] }>('/inventory/warehouses'),
  create: (data: { name: string; description?: string }) => client.post('/inventory/warehouses', data),
  update: (id: number, data: { name?: string; description?: string }) => client.put(`/inventory/warehouses/${id}`, data),
  delete: (id: number) => client.delete(`/inventory/warehouses/${id}`),
};

export function WarehouseSettings() {
  const queryClient = useQueryClient();
  const [showModal, setShowModal] = useState(false);
  const [editItem, setEditItem] = useState<Warehouse | null>(null);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [error, setError] = useState('');

  const { data: warehousesRes, isLoading } = useQuery({
    queryKey: ['warehouses'],
    queryFn: () => warehouseApi.getAll(),
  });
  const warehouses: Warehouse[] = warehousesRes?.data?.data || [];

  const fixedWarehouses = warehouses.filter(w => !w.vehicleId);
  const vehicleWarehouses = warehouses.filter(w => w.vehicleId);

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['warehouses'] });

  const saveMut = useMutation({
    mutationFn: (): Promise<AxiosResponse<any>> => {
      if (editItem) return warehouseApi.update(editItem.id, { name: name.trim(), description: description.trim() || undefined });
      return warehouseApi.create({ name: name.trim(), description: description.trim() || undefined });
    },
    onSuccess: () => { invalidate(); closeModal(); },
    onError: (err: unknown) => {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen');
    },
  });

  const deleteMut = useMutation({
    mutationFn: (id: number) => warehouseApi.delete(id),
    onSuccess: invalidate,
  });

  const openCreate = () => { setEditItem(null); setName(''); setDescription(''); setError(''); setShowModal(true); };
  const openEdit = (w: Warehouse) => { setEditItem(w); setName(w.name); setDescription(w.description || ''); setError(''); setShowModal(true); };
  const closeModal = () => { setShowModal(false); setEditItem(null); setError(''); };

  const columns = [
    { key: 'name', header: 'Bezeichnung', render: (w: Warehouse) => (
      <div>
        <span className="font-medium">{w.name}</span>
        {w.description && <p className="text-xs text-gray-500">{w.description}</p>}
      </div>
    )},
    { key: 'type', header: 'Typ', render: (w: Warehouse) =>
      w.vehicleId
        ? <Badge variant="info">Fahrzeug</Badge>
        : <Badge variant="default">Fester Lagerort</Badge>
    },
    { key: 'vehicle', header: 'Fahrzeug', render: (w: Warehouse) => w.vehicle?.name || '-' },
    { key: 'actions', header: '', render: (w: Warehouse) => {
      if (w.vehicleId) return <span className="text-xs text-gray-400">automatisch</span>;
      return (
        <div className="flex gap-2">
          <Button variant="secondary" size="sm" onClick={(e: React.MouseEvent) => { e.stopPropagation(); openEdit(w); }}>Bearbeiten</Button>
          <Button variant="danger" size="sm" onClick={(e: React.MouseEvent) => {
            e.stopPropagation();
            if (confirm(`Lagerort "${w.name}" löschen?`)) deleteMut.mutate(w.id);
          }}>Löschen</Button>
        </div>
      );
    }},
  ];

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <div>
          <h3 className="text-lg font-medium text-gray-900">Lagerorte</h3>
          <p className="text-sm text-gray-500">Feste Lagerorte verwalten. Fahrzeug-Lagerorte werden automatisch angelegt.</p>
        </div>
        <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neuer Lagerort</Button>
      </div>

      {fixedWarehouses.length > 0 && (
        <div>
          <h4 className="text-sm font-semibold text-gray-500 uppercase mb-2">Feste Lagerorte</h4>
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <Table columns={columns} data={fixedWarehouses} loading={isLoading} emptyMessage="Keine festen Lagerorte." keyExtractor={(w) => w.id} />
          </div>
        </div>
      )}

      {vehicleWarehouses.length > 0 && (
        <div>
          <h4 className="text-sm font-semibold text-gray-500 uppercase mb-2">Fahrzeug-Lagerorte (automatisch)</h4>
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <Table columns={columns} data={vehicleWarehouses} loading={isLoading} emptyMessage="Keine Fahrzeug-Lagerorte." keyExtractor={(w) => w.id} />
          </div>
        </div>
      )}

      <Modal isOpen={showModal} onClose={closeModal} title={editItem ? 'Lagerort bearbeiten' : 'Neuer Lagerort'} size="sm">
        <form onSubmit={(e) => { e.preventDefault(); saveMut.mutate(); }} className="space-y-4">
          {error && <div className="bg-red-50 text-red-700 text-sm p-3 rounded">{error}</div>}
          <Input label="Bezeichnung" value={name} onChange={(e) => setName(e.target.value)} required />
          <Input label="Beschreibung" value={description} onChange={(e) => setDescription(e.target.value)} />
          <div className="flex justify-end gap-2">
            <Button variant="secondary" type="button" onClick={closeModal}>Abbrechen</Button>
            <Button type="submit" loading={saveMut.isPending} disabled={!name.trim()}>Speichern</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
