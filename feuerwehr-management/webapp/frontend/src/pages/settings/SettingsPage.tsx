import React, { useState, useEffect, useRef } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon, ArrowDownTrayIcon, TrashIcon, ArrowPathIcon } from '@heroicons/react/24/outline';
import { settingsApi } from '../../api/settings';
import { Card } from '../../components/ui/Card';
import { Input } from '../../components/ui/Input';
import { Button } from '../../components/ui/Button';
import { Table } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { Rank, Template, TemplateHistory } from '../../types';
import { formatDate } from '../../utils/format';
import { DeviceInspectionSettings } from './DeviceInspectionSettings';
import { DataImportSettings } from './DataImportSettings';
import { WarehouseSettings } from './WarehouseSettings';
import { InspectionTypeSettings } from './InspectionTypeSettings';

type Tab = 'allgemein' | 'dienstgrade' | 'jahre' | 'templates' | 'geraetepruefung' | 'lagerorte' | 'pruefarten' | 'datenimport';

export function SettingsPage() {
  const [tab, setTab] = useState<Tab>('allgemein');

  return (
    <div className="space-y-4">
      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {[{ id: 'allgemein', label: 'Allgemein' }, { id: 'dienstgrade', label: 'Dienstgrade' }, { id: 'jahre', label: 'Jahre' }, { id: 'templates', label: 'Templates' }, { id: 'geraetepruefung', label: 'Geräteprüfung' }, { id: 'lagerorte', label: 'Lagerorte' }, { id: 'pruefarten', label: 'Prüfarten' }, { id: 'datenimport', label: 'Datenimport' }].map((t) => (
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
      {tab === 'templates' && <TemplatesSettings />}
      {tab === 'geraetepruefung' && <DeviceInspectionSettings />}
      {tab === 'lagerorte' && <WarehouseSettings />}
      {tab === 'pruefarten' && <InspectionTypeSettings />}
      {tab === 'datenimport' && <DataImportSettings />}
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

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function TemplatesSettings() {
  const queryClient = useQueryClient();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const replaceInputRef = useRef<HTMLInputElement>(null);
  const [showUpload, setShowUpload] = useState(false);
  const [newName, setNewName] = useState('');
  const [fileSelected, setFileSelected] = useState(false);
  const [error, setError] = useState('');
  const [uploading, setUploading] = useState(false);
  const [selectedTemplate, setSelectedTemplate] = useState<Template | null>(null);
  const [showHistory, setShowHistory] = useState(false);

  const { data: templates, isLoading } = useQuery({
    queryKey: ['templates'],
    queryFn: () => settingsApi.getTemplates().then(r => r.data.data as Template[]),
  });

  const { data: history } = useQuery({
    queryKey: ['template-history', selectedTemplate?.id],
    queryFn: () => selectedTemplate ? settingsApi.getTemplateHistory(selectedTemplate.id).then(r => r.data.data as TemplateHistory[]) : Promise.resolve([]),
    enabled: !!selectedTemplate && showHistory,
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => settingsApi.deleteTemplate(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['templates'] }),
  });

  const handleUpload = async () => {
    const file = fileInputRef.current?.files?.[0];
    if (!file || !newName.trim()) return;
    setUploading(true);
    setError('');
    try {
      await settingsApi.uploadTemplate(newName.trim(), file);
      queryClient.invalidateQueries({ queryKey: ['templates'] });
      setShowUpload(false);
      setNewName('');
      setFileSelected(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    } catch (err: unknown) {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Upload fehlgeschlagen');
    } finally {
      setUploading(false);
    }
  };

  const handleReplace = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file || !selectedTemplate) return;
    setUploading(true);
    setError('');
    try {
      await settingsApi.updateTemplate(selectedTemplate.id, file);
      queryClient.invalidateQueries({ queryKey: ['templates'] });
      queryClient.invalidateQueries({ queryKey: ['template-history', selectedTemplate.id] });
    } catch (err: unknown) {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Ersetzen fehlgeschlagen');
    } finally {
      setUploading(false);
      if (replaceInputRef.current) replaceInputRef.current.value = '';
    }
  };

  const handleDownload = async (t: Template) => {
    try {
      const response = await settingsApi.downloadTemplate(t.id);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.download = t.name + (t.mimeType.includes('pdf') ? '.pdf' : '.docx');
      link.click();
      window.URL.revokeObjectURL(url);
    } catch {
      setError('Download fehlgeschlagen');
    }
  };

  return (
    <div className="space-y-4">
      <Card title="Dokument-Vorlagen"
        actions={<Button variant="primary" icon={<PlusIcon />} onClick={() => setShowUpload(true)}>Neues Template</Button>}
      >
        {error && <div className="mb-4 bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}

        <Table
          columns={[
            { key: 'name', header: 'Name', render: (t: Template) => (
              <button onClick={() => handleDownload(t)} className="text-primary-600 hover:underline font-medium text-sm">{t.name}</button>
            )},
            { key: 'fileSize', header: 'Größe', render: (t: Template) => formatFileSize(t.fileSize) },
            { key: 'createdBy', header: 'Erstellt von' },
            { key: 'createdAt', header: 'Erstellt am', render: (t: Template) => formatDate(t.createdAt) },
            { key: 'updatedAt', header: 'Geändert am', render: (t: Template) => formatDate(t.updatedAt) },
            { key: 'actions', header: '', render: (t: Template) => (
              <div className="flex gap-2">
                <input ref={selectedTemplate?.id === t.id ? replaceInputRef : undefined} type="file" accept=".pdf,.doc,.docx" onChange={handleReplace} className="hidden" />
                <Button size="sm" variant="secondary" icon={<ArrowPathIcon />}
                  onClick={(e: React.MouseEvent) => { e.stopPropagation(); setSelectedTemplate(t); setTimeout(() => replaceInputRef.current?.click(), 50); }}
                  loading={uploading && selectedTemplate?.id === t.id}>
                  Ersetzen
                </Button>
                <Button size="sm" variant="secondary"
                  onClick={(e: React.MouseEvent) => { e.stopPropagation(); setSelectedTemplate(t); setShowHistory(true); }}>
                  Historie
                </Button>
                <Button size="sm" variant="danger" icon={<TrashIcon />}
                  onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(t.id); }} />
              </div>
            )},
          ]}
          data={templates || []}
          loading={isLoading}
          emptyMessage="Keine Templates vorhanden."
          keyExtractor={(t) => t.id}
        />
        <p className="mt-3 text-xs text-gray-400">Erlaubte Dateitypen: PDF, Word (max. 20 MB)</p>
      </Card>

      {/* Upload Modal */}
      <Modal isOpen={showUpload} onClose={() => setShowUpload(false)} title="Neues Template" size="md"
        footer={<><Button variant="secondary" onClick={() => setShowUpload(false)}>Abbrechen</Button><Button variant="primary" onClick={handleUpload} loading={uploading} disabled={!newName.trim() || !fileSelected}>Hochladen</Button></>}
      >
        <div className="space-y-3">
          <Input label="Template-Name" value={newName} onChange={(e) => setNewName(e.target.value)} required placeholder="z.B. Einsatzbericht-Vorlage" />
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Datei</label>
            <input ref={fileInputRef} type="file" accept=".pdf,.doc,.docx" onChange={(e) => setFileSelected(!!e.target.files?.length)} className="block w-full text-sm text-gray-500 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-medium file:bg-primary-50 file:text-primary-700 hover:file:bg-primary-100" />
          </div>
        </div>
      </Modal>

      {/* History Modal */}
      <Modal isOpen={showHistory} onClose={() => setShowHistory(false)} title={`Historie: ${selectedTemplate?.name || ''}`} size="md">
        {(!history || history.length === 0) ? (
          <p className="text-sm text-gray-500">Keine Einträge.</p>
        ) : (
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Datum</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Aktion</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Benutzer</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {history.map(h => (
                <tr key={h.id}>
                  <td className="px-4 py-2 text-sm text-gray-500">{formatDate(h.changedAt)}</td>
                  <td className="px-4 py-2 text-sm font-medium">{h.action}</td>
                  <td className="px-4 py-2 text-sm text-gray-500">{h.changedBy}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Modal>
    </div>
  );
}
