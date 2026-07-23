import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon, TrashIcon } from '@heroicons/react/24/outline';
import { usersApi } from '../../api/users';
import { PermissionGroup } from '../../types';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Table } from '../../components/ui/Table';
import { Badge } from '../../components/ui/Badge';
import { BIT_VEHICLES, BIT_OPERATIONS, BIT_EQUIPMENT } from '../../config/permissionBits';

const CAPABILITIES = [
  { bit: BIT_VEHICLES, label: 'Fahrzeuge' },
  { bit: BIT_OPERATIONS, label: 'Einsätze / Statistik' },
  { bit: BIT_EQUIPMENT, label: 'Gerätewart-Bereich (Bestand/Prüfbuch/Mängel)' },
];

export function PermissionGroupSettings() {
  const queryClient = useQueryClient();
  const { data: groups, isLoading } = useQuery({
    queryKey: ['permission-groups'],
    queryFn: () => usersApi.getGroups().then(r => r.data.data as PermissionGroup[]),
  });

  const [showForm, setShowForm] = useState(false);
  const [editGroup, setEditGroup] = useState<PermissionGroup | null>(null);
  const [form, setForm] = useState({ name: '', description: '', bits: {} as Record<string, boolean> });
  const [error, setError] = useState('');

  const openCreate = () => { setEditGroup(null); setForm({ name: '', description: '', bits: {} }); setShowForm(true); setError(''); };
  const openEdit = (g: PermissionGroup) => {
    setEditGroup(g);
    const bits: Record<string, boolean> = {};
    CAPABILITIES.forEach(c => { bits[c.bit] = !!g[c.bit]; });
    setForm({ name: g.name, description: g.description || '', bits });
    setShowForm(true);
    setError('');
  };

  const saveMutation = useMutation({
    mutationFn: () => {
      const data = { name: form.name, description: form.description || null, ...form.bits };
      return editGroup ? usersApi.updateGroup(editGroup.id, data) : usersApi.createGroup(data);
    },
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['permission-groups'] }); setShowForm(false); },
    onError: (err: unknown) => {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen');
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => usersApi.deleteGroup(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['permission-groups'] }),
  });

  const toggleBit = (bit: string, checked: boolean) =>
    setForm(p => ({ ...p, bits: { ...p.bits, [bit]: checked } }));

  const columns = [
    { key: 'name', header: 'Gruppe', render: (g: PermissionGroup) => g.name },
    { key: 'description', header: 'Beschreibung', render: (g: PermissionGroup) => g.description || '-' },
    { key: 'capabilities', header: 'Fähigkeiten', render: (g: PermissionGroup) => (
      <div className="flex flex-wrap gap-1">
        {CAPABILITIES.filter(c => g[c.bit]).map(c => <Badge key={c.bit} variant="primary">{c.label}</Badge>)}
        {!CAPABILITIES.some(c => g[c.bit]) && <span className="text-gray-400 text-xs">-</span>}
      </div>
    )},
    { key: 'actions', header: '', render: (g: PermissionGroup) => (
      <div className="flex gap-2">
        <Button size="sm" variant="secondary" onClick={(e: React.MouseEvent) => { e.stopPropagation(); openEdit(g); }}>Bearbeiten</Button>
        <Button size="sm" variant="danger" icon={<TrashIcon />} onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(g.id); }} />
      </div>
    )},
  ];

  return (
    <Card title="Berechtigungsgruppen" subtitle="Jede Gruppe kann beliebige Fähigkeiten kombinieren - z.B. Gerätewart und Gruppenführer in einer Gruppe.">
      <div className="space-y-4">
        <div className="flex justify-end">
          <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neue Gruppe</Button>
        </div>

        <Table columns={columns} data={groups || []} loading={isLoading} emptyMessage="Keine Gruppen vorhanden." keyExtractor={(g) => g.id} />

        {showForm && (
          <div className="border border-gray-200 rounded-lg p-4 bg-gray-50 space-y-3">
            <h4 className="text-sm font-semibold text-gray-700">{editGroup ? 'Gruppe bearbeiten' : 'Neue Gruppe'}</h4>
            {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}
            <Input label="Name" value={form.name} onChange={(e) => setForm(p => ({ ...p, name: e.target.value }))} required />
            <Input label="Beschreibung" value={form.description} onChange={(e) => setForm(p => ({ ...p, description: e.target.value }))} />
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">Fähigkeiten</label>
              <div className="space-y-2">
                {CAPABILITIES.map(c => (
                  <label key={c.bit} className="flex items-center gap-2 text-sm cursor-pointer">
                    <input type="checkbox" checked={!!form.bits[c.bit]} onChange={(e) => toggleBit(c.bit, e.target.checked)} className="rounded" />
                    {c.label}
                  </label>
                ))}
              </div>
            </div>
            <div className="flex gap-2">
              <Button variant="primary" onClick={() => saveMutation.mutate()} loading={saveMutation.isPending} disabled={!form.name}>Speichern</Button>
              <Button variant="secondary" onClick={() => setShowForm(false)}>Abbrechen</Button>
            </div>
          </div>
        )}
      </div>
    </Card>
  );
}
