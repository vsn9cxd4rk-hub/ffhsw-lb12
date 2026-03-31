import React, { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { settingsApi } from '../../api/settings';
import { Card } from '../../components/ui/Card';
import { Input } from '../../components/ui/Input';
import { Button } from '../../components/ui/Button';
import { Table } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { Rank } from '../../types';

type Tab = 'allgemein' | 'dienstgrade' | 'jahre';

export function SettingsPage() {
  const [tab, setTab] = useState<Tab>('allgemein');

  return (
    <div className="space-y-4">
      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {[{ id: 'allgemein', label: 'Allgemein' }, { id: 'dienstgrade', label: 'Dienstgrade' }, { id: 'jahre', label: 'Jahre' }].map((t) => (
            <button key={t.id} onClick={() => setTab(t.id as Tab)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'allgemein' && <GeneralSettings />}
      {tab === 'dienstgrade' && <RanksSettings />}
      {tab === 'jahre' && <YearsSettings />}
    </div>
  );
}

function GeneralSettings() {
  const queryClient = useQueryClient();
  const { data: settings } = useQuery({ queryKey: ['settings'], queryFn: () => settingsApi.get().then(r => r.data.data) });
  const [form, setForm] = useState<Record<string, string>>({});

  useEffect(() => { if (settings) setForm(settings); }, [settings]);

  const mutation = useMutation({
    mutationFn: () => settingsApi.update(form),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['settings'] }),
  });

  const u = (k: string, v: string) => setForm(p => ({ ...p, [k]: v }));

  return (
    <Card title="Allgemeine Einstellungen">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input label="Feuerwehrname" value={form.fireStationName || ''} onChange={(e) => u('fireStationName', e.target.value)} />
        <Input label="Stadt" value={form.fireStationCity || ''} onChange={(e) => u('fireStationCity', e.target.value)} />
        <Input label="PLZ" value={form.fireStationZip || ''} onChange={(e) => u('fireStationZip', e.target.value)} />
        <Input label="Straße" value={form.fireStationStreet || ''} onChange={(e) => u('fireStationStreet', e.target.value)} />
        <Input label="Telefon" value={form.fireStationPhone || ''} onChange={(e) => u('fireStationPhone', e.target.value)} />
        <Input label="E-Mail" value={form.fireStationEmail || ''} onChange={(e) => u('fireStationEmail', e.target.value)} type="email" />
        <Input label="Prüfungserinnerung (Tage)" value={form.inspectionReminderDays || ''} onChange={(e) => u('inspectionReminderDays', e.target.value)} type="number" />
        <Input label="Untersuchungserinnerung (Tage)" value={form.medicalExamReminderDays || ''} onChange={(e) => u('medicalExamReminderDays', e.target.value)} type="number" />
      </div>
      <div className="mt-6 flex justify-end">
        <Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending}>Speichern</Button>
      </div>
    </Card>
  );
}

function RanksSettings() {
  const queryClient = useQueryClient();
  const [showCreate, setShowCreate] = useState(false);
  const [editRank, setEditRank] = useState<Rank | null>(null);
  const [form, setForm] = useState({ name: '', abbreviation: '', sortOrder: '0' });

  const { data: ranks, isLoading } = useQuery({ queryKey: ['ranks'], queryFn: () => settingsApi.getRanks().then(r => r.data.data) });

  const createMutation = useMutation({
    mutationFn: () => editRank
      ? settingsApi.updateRank(editRank.id, { name: form.name, abbreviation: form.abbreviation, sortOrder: parseInt(form.sortOrder) })
      : settingsApi.createRank({ name: form.name, abbreviation: form.abbreviation, sortOrder: parseInt(form.sortOrder) }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['ranks'] }); setShowCreate(false); setEditRank(null); setForm({ name: '', abbreviation: '', sortOrder: '0' }); },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => settingsApi.deleteRank(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['ranks'] }),
  });

  return (
    <Card title="Dienstgrade" actions={<Button variant="secondary" size="sm" onClick={() => setShowCreate(true)}>+ Hinzufügen</Button>}>
      <Table
        columns={[
          { key: 'abbreviation', header: 'Kürzel' },
          { key: 'name', header: 'Bezeichnung' },
          { key: 'actions', header: '', render: (r: Rank) => (
            <div className="flex gap-2">
              <Button size="sm" variant="secondary" onClick={() => { setEditRank(r); setForm({ name: r.name, abbreviation: r.abbreviation, sortOrder: String(r.sortOrder) }); setShowCreate(true); }}>Bearbeiten</Button>
              <Button size="sm" variant="danger" onClick={() => deleteMutation.mutate(r.id)}>Löschen</Button>
            </div>
          )},
        ]}
        data={ranks || []}
        loading={isLoading}
        emptyMessage="Keine Dienstgrade vorhanden."
      />

      <Modal isOpen={showCreate} onClose={() => { setShowCreate(false); setEditRank(null); }} title={editRank ? 'Dienstgrad bearbeiten' : 'Neuer Dienstgrad'} size="sm"
        footer={<><Button variant="secondary" onClick={() => { setShowCreate(false); setEditRank(null); }}>Abbrechen</Button><Button variant="primary" onClick={() => createMutation.mutate()} loading={createMutation.isPending} disabled={!form.name || !form.abbreviation}>Speichern</Button></>}
      >
        <div className="space-y-3">
          <Input label="Bezeichnung" value={form.name} onChange={(e) => setForm(f => ({ ...f, name: e.target.value }))} required />
          <Input label="Kürzel" value={form.abbreviation} onChange={(e) => setForm(f => ({ ...f, abbreviation: e.target.value }))} required />
          <Input label="Reihenfolge" value={form.sortOrder} onChange={(e) => setForm(f => ({ ...f, sortOrder: e.target.value }))} type="number" />
        </div>
      </Modal>
    </Card>
  );
}

function YearsSettings() {
  const queryClient = useQueryClient();
  const { data: years, isLoading } = useQuery({ queryKey: ['years'], queryFn: () => settingsApi.getYears().then(r => r.data.data) });

  const createMutation = useMutation({
    mutationFn: (year: number) => settingsApi.createYear(year),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['years'] }),
  });

  const setActiveMutation = useMutation({
    mutationFn: (id: number) => settingsApi.updateYear(id, { isActive: true }),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['years'] }),
  });

  return (
    <Card title="Betriebsjahre" actions={<Button variant="secondary" size="sm" onClick={() => createMutation.mutate(new Date().getFullYear())}>+ Aktuelles Jahr</Button>}>
      <Table
        columns={[
          { key: 'year', header: 'Jahr' },
          { key: 'isActive', header: 'Aktiv', render: (r: { id: number; year: number; isActive: boolean }) => r.isActive ? (
            <span className="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium bg-green-100 text-green-800">Aktiv</span>
          ) : (
            <Button size="sm" variant="secondary" onClick={() => setActiveMutation.mutate(r.id)}>Aktivieren</Button>
          )},
        ]}
        data={years || []}
        loading={isLoading}
        emptyMessage="Keine Jahre vorhanden."
      />
    </Card>
  );
}
