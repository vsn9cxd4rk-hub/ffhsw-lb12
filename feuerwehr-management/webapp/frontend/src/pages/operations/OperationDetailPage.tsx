import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
<<<<<<< HEAD
import { ArrowLeftIcon, PlusIcon, TrashIcon } from '@heroicons/react/24/outline';
import { operationsApi } from '../../api/operations';
import { Operation } from '../../types';
=======
import { ArrowLeftIcon, PlusIcon, TrashIcon, ArrowDownTrayIcon } from '@heroicons/react/24/outline';
import { operationsApi } from '../../api/operations';
import { settingsApi } from '../../api/settings';
import { Operation, OperationDocument, Template } from '../../types';
>>>>>>> a9dc7840 (Added New FW Management system)
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { Card } from '../../components/ui/Card';
import { Table } from '../../components/ui/Table';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { formatDate } from '../../utils/format';

export function OperationDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
<<<<<<< HEAD
  const [tab, setTab] = useState<'details' | 'times' | 'report'>('details');
=======
  const [tab, setTab] = useState<'details' | 'times' | 'report' | 'documents'>('details');
>>>>>>> a9dc7840 (Added New FW Management system)

  const { data: op, isLoading } = useQuery({
    queryKey: ['operation', id],
    queryFn: () => operationsApi.getById(parseInt(id!)).then((r) => r.data.data),
  });

  const updateMutation = useMutation({
    mutationFn: (data: Partial<Operation>) => operationsApi.update(parseInt(id!), data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation', id] }),
  });

  if (isLoading) return <LoadingSpinner />;
  if (!op) return <div className="text-center py-12 text-gray-500">Einsatz nicht gefunden</div>;

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-3">
        <Button variant="ghost" onClick={() => navigate('/operations')} icon={<ArrowLeftIcon />} />
        <div>
          <h2 className="text-xl font-bold">{op.location}</h2>
          <p className="text-sm text-gray-500">{formatDate(op.date)} · {op.alarmTime || ''} · {op.keyword || 'Kein Stichwort'}</p>
        </div>
      </div>

      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
<<<<<<< HEAD
          {[{ id: 'details', label: 'Details' }, { id: 'times', label: 'Fahrzeugzeiten' }, { id: 'report', label: 'Bericht' }].map((t) => (
=======
          {[{ id: 'details', label: 'Details' }, { id: 'times', label: 'Fahrzeugzeiten' }, { id: 'report', label: 'Bericht' }, { id: 'documents', label: 'Dokumente' }].map((t) => (
>>>>>>> a9dc7840 (Added New FW Management system)
            <button key={t.id} onClick={() => setTab(t.id as typeof tab)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'details' && <OperationDetailsTab op={op} onSave={(d) => updateMutation.mutate(d)} saving={updateMutation.isPending} />}
      {tab === 'times' && <OperationTimesTab operationId={parseInt(id!)} times={op.times || []} />}
      {tab === 'report' && <OperationReportTab operationId={parseInt(id!)} report={op.reports?.[0]} />}
<<<<<<< HEAD
=======
      {tab === 'documents' && <OperationDocumentsTab operationId={parseInt(id!)} />}
>>>>>>> a9dc7840 (Added New FW Management system)
    </div>
  );
}

function OperationDetailsTab({ op, onSave, saving }: { op: Operation; onSave: (d: Partial<Operation>) => void; saving: boolean }) {
  const [form, setForm] = useState({
    date: op.date.substring(0, 10),
    alarmTime: op.alarmTime || '',
    departureTime: op.departureTime || '',
    arrivalTime: op.arrivalTime || '',
    returnTime: op.returnTime || '',
    location: op.location,
    district: op.district || '',
    keyword: op.keyword || '',
    operationNumber: op.operationNumber || '',
    leaderCount: String(op.leaderCount),
    memberCount: String(op.memberCount),
    description: op.description || '',
    vehicles: op.vehicles || '',
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Card>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input label="Datum" value={form.date} onChange={(e) => u('date', e.target.value)} type="date" />
        <Input label="Einsatznummer" value={form.operationNumber} onChange={(e) => u('operationNumber', e.target.value)} />
        <Input label="Alarmzeit" value={form.alarmTime} onChange={(e) => u('alarmTime', e.target.value)} type="time" />
        <Input label="Ausgerückt" value={form.departureTime} onChange={(e) => u('departureTime', e.target.value)} type="time" />
        <Input label="Eingetroffen" value={form.arrivalTime} onChange={(e) => u('arrivalTime', e.target.value)} type="time" />
        <Input label="Eingerückt" value={form.returnTime} onChange={(e) => u('returnTime', e.target.value)} type="time" />
        <Input label="Einsatzort" value={form.location} onChange={(e) => u('location', e.target.value)} required />
        <Input label="Stadtteil" value={form.district} onChange={(e) => u('district', e.target.value)} />
        <Input label="Stichwort" value={form.keyword} onChange={(e) => u('keyword', e.target.value)} />
        <Input label="Fahrzeuge" value={form.vehicles} onChange={(e) => u('vehicles', e.target.value)} />
        <Input label="GF-Stärke" value={form.leaderCount} onChange={(e) => u('leaderCount', e.target.value)} type="number" />
        <Input label="FM-Stärke" value={form.memberCount} onChange={(e) => u('memberCount', e.target.value)} type="number" />
        <div className="col-span-full">
          <Textarea label="Beschreibung" value={form.description} onChange={(e) => u('description', e.target.value)} rows={4} />
        </div>
      </div>
      <div className="mt-6 flex justify-end">
        <Button variant="primary" onClick={() => onSave({ ...form, leaderCount: parseInt(form.leaderCount) || 0, memberCount: parseInt(form.memberCount) || 0 } as Partial<Operation>)} loading={saving}>
          Speichern
        </Button>
      </div>
    </Card>
  );
}

function OperationTimesTab({ operationId, times }: { operationId: number; times: Array<{ id: number; vehicleName: string; alarmTime: string | null; departureTime: string | null; arrivalTime: string | null; returnTime: string | null }> }) {
  const queryClient = useQueryClient();
  const [showAdd, setShowAdd] = useState(false);
  const [newTime, setNewTime] = useState({ vehicleName: '', alarmTime: '', departureTime: '', arrivalTime: '', returnTime: '' });

  const addMutation = useMutation({
    mutationFn: () => operationsApi.createTime(operationId, { ...newTime, alarmTime: newTime.alarmTime || null, departureTime: newTime.departureTime || null, arrivalTime: newTime.arrivalTime || null, returnTime: newTime.returnTime || null }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['operation', String(operationId)] }); setShowAdd(false); },
  });

  const deleteMutation = useMutation({
    mutationFn: (timeId: number) => operationsApi.deleteTime(operationId, timeId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation', String(operationId)] }),
  });

  return (
    <Card title="Fahrzeugzeiten" actions={<Button variant="secondary" size="sm" icon={<PlusIcon />} onClick={() => setShowAdd(!showAdd)}>Hinzufügen</Button>}>
      {showAdd && (
        <div className="mb-4 p-4 bg-gray-50 rounded-md grid grid-cols-2 md:grid-cols-3 gap-3">
          <Input label="Fahrzeug" value={newTime.vehicleName} onChange={(e) => setNewTime(t => ({ ...t, vehicleName: e.target.value }))} />
          <Input label="Alarm" value={newTime.alarmTime} onChange={(e) => setNewTime(t => ({ ...t, alarmTime: e.target.value }))} type="time" />
          <Input label="Ausgerückt" value={newTime.departureTime} onChange={(e) => setNewTime(t => ({ ...t, departureTime: e.target.value }))} type="time" />
          <Input label="Eingetroffen" value={newTime.arrivalTime} onChange={(e) => setNewTime(t => ({ ...t, arrivalTime: e.target.value }))} type="time" />
          <Input label="Eingerückt" value={newTime.returnTime} onChange={(e) => setNewTime(t => ({ ...t, returnTime: e.target.value }))} type="time" />
          <div className="flex items-end gap-2">
            <Button variant="primary" size="sm" onClick={() => addMutation.mutate()} loading={addMutation.isPending} disabled={!newTime.vehicleName}>Speichern</Button>
            <Button variant="secondary" size="sm" onClick={() => setShowAdd(false)}>Abbrechen</Button>
          </div>
        </div>
      )}
      <Table
        columns={[
          { key: 'vehicleName', header: 'Fahrzeug' },
          { key: 'alarmTime', header: 'Alarm', render: (r) => r.alarmTime || '-' },
          { key: 'departureTime', header: 'Ausgerückt', render: (r) => r.departureTime || '-' },
          { key: 'arrivalTime', header: 'Eingetroffen', render: (r) => r.arrivalTime || '-' },
          { key: 'returnTime', header: 'Eingerückt', render: (r) => r.returnTime || '-' },
          { key: 'actions', header: '', render: (r) => (
            <Button variant="ghost" size="sm" onClick={(e) => { e.stopPropagation(); deleteMutation.mutate(r.id as number); }}>
              <TrashIcon className="h-4 w-4 text-red-500" />
            </Button>
          )},
        ]}
        data={times}
        emptyMessage="Keine Fahrzeugzeiten eingetragen."
      />
    </Card>
  );
}

function OperationReportTab({ operationId, report }: { operationId: number; report?: { content: string } }) {
  const queryClient = useQueryClient();
  const [content, setContent] = useState(report?.content || '');

  const saveMutation = useMutation({
    mutationFn: () => operationsApi.updateReport(operationId, content),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation', String(operationId)] }),
  });

  return (
    <Card title="Einsatzbericht">
      <Textarea value={content} onChange={(e) => setContent(e.target.value)} rows={12} placeholder="Einsatzbericht eingeben..." />
      <div className="mt-4 flex justify-end">
        <Button variant="primary" onClick={() => saveMutation.mutate()} loading={saveMutation.isPending}>Speichern</Button>
      </div>
    </Card>
  );
}
<<<<<<< HEAD
=======

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

function OperationDocumentsTab({ operationId }: { operationId: number }) {
  const queryClient = useQueryClient();
  const fileInputRef = React.useRef<HTMLInputElement>(null);
  const [error, setError] = useState('');
  const [uploading, setUploading] = useState(false);

  const { data: documents, isLoading } = useQuery({
    queryKey: ['operation-documents', operationId],
    queryFn: () => operationsApi.getDocuments(operationId).then(r => r.data.data as OperationDocument[]),
  });

  const { data: templates } = useQuery({
    queryKey: ['templates'],
    queryFn: () => settingsApi.getTemplates().then(r => r.data.data as Template[]),
  });

  const deleteMutation = useMutation({
    mutationFn: (docId: number) => operationsApi.deleteDocument(operationId, docId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation-documents', operationId] }),
  });

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    setError('');
    setUploading(true);
    try {
      await operationsApi.uploadDocument(operationId, file);
      queryClient.invalidateQueries({ queryKey: ['operation-documents', operationId] });
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Upload fehlgeschlagen';
      setError(msg);
    } finally {
      setUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const handleDownload = async (doc: OperationDocument) => {
    try {
      const response = await operationsApi.downloadDocument(operationId, doc.id);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.download = doc.fileName;
      link.click();
      window.URL.revokeObjectURL(url);
    } catch {
      setError('Download fehlgeschlagen');
    }
  };

  return (
    <>
    <Card title="Dokumente"
      actions={
        <div>
          <input ref={fileInputRef} type="file" accept=".pdf,.doc,.docx" onChange={handleUpload} className="hidden" />
          <Button variant="primary" icon={<PlusIcon />} onClick={() => fileInputRef.current?.click()} loading={uploading}>
            Dokument hochladen
          </Button>
        </div>
      }
    >
      {error && (
        <div className="mb-4 bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>
      )}

      {isLoading ? (
        <p className="text-sm text-gray-500">Lade...</p>
      ) : !documents || documents.length === 0 ? (
        <p className="text-sm text-gray-500">Keine Dokumente vorhanden.</p>
      ) : (
        <Table
          columns={[
            { key: 'fileName', header: 'Dateiname', render: (d: OperationDocument) => (
              <button onClick={() => handleDownload(d)} className="text-primary-600 hover:underline font-medium text-sm">
                {d.fileName}
              </button>
            )},
            { key: 'fileSize', header: 'Größe', render: (d: OperationDocument) => formatFileSize(d.fileSize) },
            { key: 'uploadedBy', header: 'Hochgeladen von' },
            { key: 'createdAt', header: 'Datum', render: (d: OperationDocument) => formatDate(d.createdAt) },
            { key: 'actions', header: '', render: (d: OperationDocument) => (
              <Button variant="ghost" size="sm" onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(d.id); }}>
                <TrashIcon className="h-4 w-4 text-red-500" />
              </Button>
            )},
          ]}
          data={documents}
          keyExtractor={(d) => d.id}
        />
      )}

      <p className="mt-3 text-xs text-gray-400">Erlaubte Dateitypen: PDF, Word (max. 20 MB)</p>
    </Card>

    {templates && templates.length > 0 && (
      <Card title="Vorlagen">
        <ul className="divide-y divide-gray-100">
          {templates.map(t => (
            <li key={t.id} className="py-2 flex items-center justify-between">
              <span className="text-sm font-medium text-gray-700">{t.name}</span>
              <Button variant="secondary" size="sm" icon={<ArrowDownTrayIcon />} onClick={async () => {
                try {
                  const response = await settingsApi.downloadTemplate(t.id);
                  const url = window.URL.createObjectURL(new Blob([response.data]));
                  const link = document.createElement('a');
                  link.href = url;
                  link.download = t.name + (t.mimeType.includes('pdf') ? '.pdf' : '.docx');
                  link.click();
                  window.URL.revokeObjectURL(url);
                } catch { setError('Template-Download fehlgeschlagen'); }
              }}>Herunterladen</Button>
            </li>
          ))}
        </ul>
      </Card>
    )}
    </>
  );
}
>>>>>>> a9dc7840 (Added New FW Management system)
