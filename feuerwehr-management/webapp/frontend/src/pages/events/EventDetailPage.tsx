import React, { useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeftIcon, PlusIcon, TrashIcon, ArrowDownTrayIcon } from '@heroicons/react/24/outline';
import { eventsApi } from '../../api/events';
import { settingsApi } from '../../api/settings';
import { Event, EventDocument, Template } from '../../types';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Card } from '../../components/ui/Card';
import { Table } from '../../components/ui/Table';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { formatDate, getEventCategoryLabel } from '../../utils/format';

function formatFileSize(bytes: number): string {
  if (bytes < 1024) return `${bytes} B`;
  if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
  return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [tab, setTab] = useState<'details' | 'documents'>('details');

  const { data: event, isLoading } = useQuery({
    queryKey: ['event', id],
    queryFn: () => eventsApi.getById(parseInt(id!)).then(r => r.data.data),
  });

  const updateMutation = useMutation({
    mutationFn: (data: Partial<Event>) => eventsApi.update(parseInt(id!), data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['event', id] }),
  });

  if (isLoading) return <LoadingSpinner />;
  if (!event) return <div className="text-center py-12 text-gray-500">Veranstaltung nicht gefunden</div>;

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-3">
        <Button variant="ghost" onClick={() => navigate('/events')} icon={<ArrowLeftIcon />} />
        <div>
          <h2 className="text-xl font-bold">{event.name}</h2>
          <p className="text-sm text-gray-500">{formatDate(event.date)} · {getEventCategoryLabel(event.category)}</p>
        </div>
      </div>

      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {[{ id: 'details', label: 'Details' }, { id: 'documents', label: 'Dokumente' }].map(t => (
            <button key={t.id} onClick={() => setTab(t.id as typeof tab)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'details' && <EventDetailsTab event={event} onSave={d => updateMutation.mutate(d)} saving={updateMutation.isPending} />}
      {tab === 'documents' && <EventDocumentsTab eventId={parseInt(id!)} />}
    </div>
  );
}

function EventDetailsTab({ event, onSave, saving }: { event: Event; onSave: (d: Partial<Event>) => void; saving: boolean }) {
  const [form, setForm] = useState({
    name: event.name,
    name2: event.name2 || '',
    date: event.date.substring(0, 10),
    startTime: event.startTime || '',
    endTime: event.endTime || '',
    notes: event.notes || '',
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Card>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input label="Bezeichnung" value={form.name} onChange={e => u('name', e.target.value)} required />
        <Input label="Bezeichnung 2" value={form.name2} onChange={e => u('name2', e.target.value)} />
        <Input label="Datum" value={form.date} onChange={e => u('date', e.target.value)} type="date" />
        <div className="grid grid-cols-2 gap-3">
          <Input label="Beginn" value={form.startTime} onChange={e => u('startTime', e.target.value)} type="time" />
          <Input label="Ende" value={form.endTime} onChange={e => u('endTime', e.target.value)} type="time" />
        </div>
        <div className="col-span-full">
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Notizen</label>
          <textarea value={form.notes} onChange={e => u('notes', e.target.value)} rows={3}
            className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none" />
        </div>
      </div>
      <div className="mt-6 flex justify-end">
        <Button variant="primary" onClick={() => onSave({ ...form, date: form.date ? new Date(form.date).toISOString() : undefined } as Partial<Event>)} loading={saving}>Speichern</Button>
      </div>
    </Card>
  );
}

function EventDocumentsTab({ eventId }: { eventId: number }) {
  const queryClient = useQueryClient();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [error, setError] = useState('');
  const [uploading, setUploading] = useState(false);

  const { data: documents } = useQuery({
    queryKey: ['event-documents', eventId],
    queryFn: () => eventsApi.getDocuments(eventId).then(r => r.data.data as EventDocument[]),
  });

  const { data: templates } = useQuery({
    queryKey: ['templates'],
    queryFn: () => settingsApi.getTemplates().then(r => r.data.data as Template[]),
  });

  const deleteMutation = useMutation({
    mutationFn: (docId: number) => eventsApi.deleteDocument(eventId, docId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['event-documents', eventId] }),
  });

  const handleUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    setError('');
    setUploading(true);
    try {
      await eventsApi.uploadDocument(eventId, file);
      queryClient.invalidateQueries({ queryKey: ['event-documents', eventId] });
    } catch (err: unknown) {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Upload fehlgeschlagen');
    } finally {
      setUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const handleDownload = async (doc: EventDocument) => {
    try {
      const response = await eventsApi.downloadDocument(eventId, doc.id);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.download = doc.fileName;
      link.click();
      window.URL.revokeObjectURL(url);
    } catch { setError('Download fehlgeschlagen'); }
  };

  const handleTemplateDownload = async (t: Template) => {
    try {
      const response = await settingsApi.downloadTemplate(t.id);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.download = t.name + (t.mimeType.includes('pdf') ? '.pdf' : '.docx');
      link.click();
      window.URL.revokeObjectURL(url);
    } catch { setError('Download fehlgeschlagen'); }
  };

  return (
    <div className="space-y-4">
      <Card title="Dokumente"
        actions={
          <div>
            <input ref={fileInputRef} type="file" accept=".pdf,.doc,.docx" onChange={handleUpload} className="hidden" />
            <Button variant="primary" icon={<PlusIcon />} onClick={() => fileInputRef.current?.click()} loading={uploading}>Dokument hochladen</Button>
          </div>
        }
      >
        {error && <div className="mb-4 bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}
        {!documents || documents.length === 0 ? (
          <p className="text-sm text-gray-500">Keine Dokumente vorhanden.</p>
        ) : (
          <Table
            columns={[
              { key: 'fileName', header: 'Dateiname', render: (d: EventDocument) => (
                <button onClick={() => handleDownload(d)} className="text-primary-600 hover:underline font-medium text-sm">{d.fileName}</button>
              )},
              { key: 'fileSize', header: 'Größe', render: (d: EventDocument) => formatFileSize(d.fileSize) },
              { key: 'uploadedBy', header: 'Hochgeladen von' },
              { key: 'createdAt', header: 'Datum', render: (d: EventDocument) => formatDate(d.createdAt) },
              { key: 'actions', header: '', render: (d: EventDocument) => (
                <Button variant="ghost" size="sm" onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(d.id); }}>
                  <TrashIcon className="h-4 w-4 text-red-500" />
                </Button>
              )},
            ]}
            data={documents}
            keyExtractor={d => d.id}
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
                <Button variant="secondary" size="sm" icon={<ArrowDownTrayIcon />} onClick={() => handleTemplateDownload(t)}>Herunterladen</Button>
              </li>
            ))}
          </ul>
        </Card>
      )}
    </div>
  );
}
