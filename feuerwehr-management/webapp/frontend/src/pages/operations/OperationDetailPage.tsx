import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeftIcon, PlusIcon, TrashIcon, ArrowDownTrayIcon, DocumentArrowDownIcon } from '@heroicons/react/24/outline';
import { operationsApi } from '../../api/operations';
import { settingsApi } from '../../api/settings';
import { membersApi } from '../../api/members';
import { Operation, OperationDocument, OperationPersonnel, Template } from '../../types';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { Card } from '../../components/ui/Card';
import { Table } from '../../components/ui/Table';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { Modal } from '../../components/ui/Modal';
import { formatDate } from '../../utils/format';

export function OperationDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [tab, setTab] = useState<'details' | 'times' | 'personnel' | 'report' | 'documents'>('details');

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
          {[{ id: 'details', label: 'Details' }, { id: 'times', label: 'Fahrzeugzeiten' }, { id: 'personnel', label: 'Kräfte' }, { id: 'report', label: 'Bericht' }, { id: 'documents', label: 'Dokumente' }].map((t) => (
            <button key={t.id} onClick={() => setTab(t.id as typeof tab)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'details' && <OperationDetailsTab op={op} onSave={(d) => updateMutation.mutate(d)} saving={updateMutation.isPending} />}
      {tab === 'times' && <OperationTimesTab operationId={parseInt(id!)} times={op.times || []} />}
      {tab === 'personnel' && <OperationPersonnelTab operationId={parseInt(id!)} />}
      {tab === 'report' && <OperationReportTab operationId={parseInt(id!)} report={op.reports?.[0]} />}
      {tab === 'documents' && <OperationDocumentsTab operationId={parseInt(id!)} />}
    </div>
  );
}

const OPERATION_TYPES = [
  'Kleinbrand a', 'Kleinbrand b', 'Mittelbrand', 'Grossbrand',
  'Katastr.-Einsatz', 'Techn. Hilfeleist.', 'Tiere/Insekten',
  'NAW Einsatz', 'RTW Einsatz', 'Kranken-Transport',
  'Sonstiger Einsatz', 'Blinder Alarm', 'Boeswilliger Alarm', 'Brandmelde Anlage',
];

const EINSATZSTICHWORTE = [
  'Brand 1', 'Brand 2', 'Brand 3', 'Brand 4', 'Brand 5', 'Brand 6-8',
  'Brand Fahrzeug gross', 'Brand Kamin', 'Brand Wald/Flache gross',
  'Brand in E-Anlage', 'Brand Gefahrstoff klein', 'Brand Gefahrstoff gross',
  'TH klein', 'Amtshilfe', 'Auslaufende Betriebsstoffe gross', 'Bombenfund',
  'Einsatz Tier', 'Einsturzgefahr', 'Erdrutsch', 'Fahrzeug in Gewasser',
  'Flugzeugabsturz klein', 'Flugzeugabsturz gross', 'Person droht/springt',
  'Person in Aufzug', 'Person in Notlage', 'Person/Tier in Wasser',
  'Person verschuttet', 'Tur Offnen', 'Unterstutzung RD Tragehilfe',
  'Unterstutzung RD DLK', 'VU mit Person', 'VU mit Person gross',
  'Wasserschaden klein', 'Wasserschaden gross', 'SRHT', 'Hochwasser',
  'Schadstoff auf Gewasser', 'ABC messen', 'ABC 1', 'ABC 2',
  'SONSTIGES nicht aufgefuhrt',
];

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
    reportType: op.reportType || 'Einsatzbericht',
    ilsOrderNumber: op.ilsOrderNumber || '',
    callerInfo: op.callerInfo || '',
    policeInfo: op.policeInfo || '',
    situationOnArrival: op.situationOnArrival || '',
    actionsTaken: op.actionsTaken || '',
    resourcesUsed: op.resourcesUsed || '',
    operationType: op.operationType || '',
    rescuedPersons: String(op.rescuedPersons || 0),
    injuredFirefighters: String(op.injuredFirefighters || 0),
    deceasedPersons: String(op.deceasedPersons || 0),
    deceasedFirefighters: String(op.deceasedFirefighters || 0),
    createdByName: op.createdByName || '',
    authorRole: op.authorRole || 'Einsatzleiter',
    operationResult: op.operationResult || '',
    wasActivelyInvolved: op.wasActivelyInvolved !== false ? 'JA' : 'NEIN',
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  const selectedTypes = form.operationType ? form.operationType.split(',').map(t => t.trim()) : [];
  const toggleOpType = (type: string) => {
    const current = selectedTypes.includes(type)
      ? selectedTypes.filter(t => t !== type)
      : [...selectedTypes, type];
    u('operationType', current.join(','));
  };

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
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Einsatzstichwort</label>
          <select value={form.keyword} onChange={(e) => u('keyword', e.target.value)} className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm">
            <option value="">-- Stichwort auswählen --</option>
            {EINSATZSTICHWORTE.map(s => <option key={s} value={s}>{s}</option>)}
          </select>
        </div>
        <Input label="Fahrzeuge" value={form.vehicles} onChange={(e) => u('vehicles', e.target.value)} />
        <Input label="GF-Stärke" value={form.leaderCount} onChange={(e) => u('leaderCount', e.target.value)} type="number" />
        <Input label="FM-Stärke" value={form.memberCount} onChange={(e) => u('memberCount', e.target.value)} type="number" />

        <div className="col-span-full border-t pt-4 mt-2">
          <h3 className="text-sm font-semibold text-gray-700 mb-3">Einsatzbericht-Details</h3>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Berichtsart</label>
          <select value={form.reportType} onChange={(e) => u('reportType', e.target.value)} className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm">
            <option value="Einsatzbericht">Einsatzbericht</option>
            <option value="Tätigkeitsbericht">Tätigkeitsbericht</option>
          </select>
        </div>
        <Input label="ILS Auftragsnummer" value={form.ilsOrderNumber} onChange={(e) => u('ilsOrderNumber', e.target.value)} />
        <Input label="Ersteller" value={form.createdByName} onChange={(e) => u('createdByName', e.target.value)} />
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Rolle des Erstellers</label>
          <select value={form.authorRole} onChange={(e) => u('authorRole', e.target.value)} className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm">
            <option value="Einsatzleiter">Einsatzleiter</option>
            <option value="Einheitenführer LB Wahlschied">Einheitenführer LB Wahlschied</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Einsatzresultat</label>
          <select value={form.operationResult} onChange={(e) => u('operationResult', e.target.value)} className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm">
            <option value="">-- Auswählen --</option>
            <option value="Brand Real">Brand Real</option>
            <option value="THL Real">THL Real</option>
            <option value="Brand Fehl">Brand Fehl</option>
            <option value="THL Fehl">THL Fehl</option>
            <option value="BMA Fehl">BMA Fehl</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">LB aktiv tätig gewesen?</label>
          <select value={form.wasActivelyInvolved} onChange={(e) => u('wasActivelyInvolved', e.target.value)} className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm">
            <option value="JA">Ja</option>
            <option value="NEIN">Nein</option>
          </select>
        </div>

        <div className="col-span-full">
          <Textarea label="Meldender (Name & Erreichbarkeit)" value={form.callerInfo} onChange={(e) => u('callerInfo', e.target.value)} rows={2} />
        </div>
        <div className="col-span-full">
          <Textarea label="Beschreibung / Einsatzangaben" value={form.description} onChange={(e) => u('description', e.target.value)} rows={3} />
        </div>
        <div className="col-span-full">
          <Textarea label="Polizei (Inspektion & Sachbearbeiter)" value={form.policeInfo} onChange={(e) => u('policeInfo', e.target.value)} rows={2} />
        </div>
        <div className="col-span-full">
          <Textarea label="Lage bei Eintreffen" value={form.situationOnArrival} onChange={(e) => u('situationOnArrival', e.target.value)} rows={4} />
        </div>
        <div className="col-span-full">
          <Textarea label="Durchgeführte Maßnahmen" value={form.actionsTaken} onChange={(e) => u('actionsTaken', e.target.value)} rows={4} />
        </div>
        <div className="col-span-full">
          <Textarea label="Verbrauchte Einsatzmittel" value={form.resourcesUsed} onChange={(e) => u('resourcesUsed', e.target.value)} rows={2} />
        </div>

        <div className="col-span-full border-t pt-4 mt-2">
          <h3 className="text-sm font-semibold text-gray-700 mb-3">Einsatzart</h3>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-2">
            {OPERATION_TYPES.map(type => (
              <label key={type} className="flex items-center gap-2 text-sm cursor-pointer">
                <input type="checkbox" checked={selectedTypes.includes(type)} onChange={() => toggleOpType(type)} className="rounded border-gray-300 text-primary-600 focus:ring-primary-500" />
                {type}
              </label>
            ))}
          </div>
        </div>

        <div className="col-span-full border-t pt-4 mt-2">
          <h3 className="text-sm font-semibold text-gray-700 mb-3">Statistische Angaben</h3>
        </div>
        <Input label="Menschen gerettet" value={form.rescuedPersons} onChange={(e) => u('rescuedPersons', e.target.value)} type="number" />
        <Input label="FW verletzt" value={form.injuredFirefighters} onChange={(e) => u('injuredFirefighters', e.target.value)} type="number" />
        <Input label="Menschen tot" value={form.deceasedPersons} onChange={(e) => u('deceasedPersons', e.target.value)} type="number" />
        <Input label="FW tot" value={form.deceasedFirefighters} onChange={(e) => u('deceasedFirefighters', e.target.value)} type="number" />
      </div>
      <div className="mt-6 flex justify-end">
        <Button variant="primary" onClick={() => onSave({
          ...form,
          leaderCount: parseInt(form.leaderCount) || 0,
          memberCount: parseInt(form.memberCount) || 0,
          rescuedPersons: parseInt(form.rescuedPersons) || 0,
          injuredFirefighters: parseInt(form.injuredFirefighters) || 0,
          deceasedPersons: parseInt(form.deceasedPersons) || 0,
          deceasedFirefighters: parseInt(form.deceasedFirefighters) || 0,
          wasActivelyInvolved: form.wasActivelyInvolved === 'JA',
        } as Partial<Operation>)} loading={saving}>
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

    <Card title="Berichte generieren">
      <div className="flex flex-wrap gap-3">
        <GenerateReportButton operationId={operationId} />
        <GeneratePersonnelSheetButton operationId={operationId} />
      </div>
    </Card>
    </>
  );
}

// Personnel Tab
const FUNCTIONS = ['Gruppenfuehrer', 'Maschinist', 'Melder', 'AT-Fuehrer', 'AT-Mann', 'WT-Fuehrer', 'WT-Mann', 'ST-Fuehrer', 'ST-Mann'];

function OperationPersonnelTab({ operationId }: { operationId: number }) {
  const queryClient = useQueryClient();
  const [showAdd, setShowAdd] = useState(false);
  const [newEntry, setNewEntry] = useState({ memberId: 0, vehicleName: '', function: 'Gruppenfuehrer', section: 'deployed' });

  const { data: personnel, isLoading } = useQuery({
    queryKey: ['operation-personnel', operationId],
    queryFn: () => operationsApi.getPersonnel(operationId).then(r => r.data.data),
    refetchOnMount: 'always',
  });

  const { data: members } = useQuery({
    queryKey: ['members-list'],
    queryFn: () => membersApi.getAll({ limit: 200 }).then(r => r.data.data?.items || r.data.data || []),
  });

  const addMutation = useMutation({
    mutationFn: () => operationsApi.addPersonnel(operationId, newEntry),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['operation-personnel', operationId] }); setShowAdd(false); setNewEntry({ memberId: 0, vehicleName: '', function: 'Gruppenfuehrer', section: 'deployed' }); },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => operationsApi.deletePersonnel(operationId, id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation-personnel', operationId] }),
  });

  const deployed = (personnel || []).filter((p: OperationPersonnel) => p.section === 'deployed');
  const reinforcement = (personnel || []).filter((p: OperationPersonnel) => p.section === 'reinforcement');

  if (isLoading) return <LoadingSpinner />;

  return (
    <div className="space-y-4">
      <Card title="Eingesetzte Kräfte" actions={<Button variant="secondary" size="sm" icon={<PlusIcon />} onClick={() => { setNewEntry(e => ({ ...e, section: 'deployed' })); setShowAdd(true); }}>Hinzufügen</Button>}>
        {deployed.length === 0 ? (
          <p className="text-sm text-gray-500">Keine Kräfte zugeordnet.</p>
        ) : (
          <Table
            columns={[
              { key: 'function', header: 'Funktion' },
              { key: 'name', header: 'Name', render: (p: OperationPersonnel) => `${p.member.lastName}, ${p.member.firstName}` },
              { key: 'vehicleName', header: 'Fahrzeug' },
              { key: 'qualAGT', header: 'AGT', render: (p: OperationPersonnel) => p.member.qualAGT ? '✓' : '' },
              { key: 'qualGF', header: 'GF', render: (p: OperationPersonnel) => p.member.qualGruppenfuehrer ? '✓' : '' },
              { key: 'qualZF', header: 'ZF', render: (p: OperationPersonnel) => p.member.qualZugfuehrer ? '✓' : '' },
              { key: 'licC', header: 'Kl.C', render: (p: OperationPersonnel) => p.member.qualLicenseC ? '✓' : '' },
              { key: 'licB', header: 'Kl.B', render: (p: OperationPersonnel) => p.member.qualLicenseB ? '✓' : '' },
              { key: 'actions', header: '', render: (p: OperationPersonnel) => (
                <Button variant="ghost" size="sm" onClick={() => deleteMutation.mutate(p.id)}>
                  <TrashIcon className="h-4 w-4 text-red-500" />
                </Button>
              )},
            ]}
            data={deployed}
            keyExtractor={(p) => p.id}
          />
        )}
      </Card>

      <Card title="Nachgerückte Kräfte" actions={<Button variant="secondary" size="sm" icon={<PlusIcon />} onClick={() => { setNewEntry(e => ({ ...e, section: 'reinforcement' })); setShowAdd(true); }}>Hinzufügen</Button>}>
        {reinforcement.length === 0 ? (
          <p className="text-sm text-gray-500">Keine nachgerückten Kräfte.</p>
        ) : (
          <Table
            columns={[
              { key: 'function', header: 'Funktion' },
              { key: 'name', header: 'Name', render: (p: OperationPersonnel) => `${p.member.lastName}, ${p.member.firstName}` },
              { key: 'vehicleName', header: 'Fahrzeug' },
              { key: 'qualAGT', header: 'AGT', render: (p: OperationPersonnel) => p.member.qualAGT ? '✓' : '' },
              { key: 'qualGF', header: 'GF', render: (p: OperationPersonnel) => p.member.qualGruppenfuehrer ? '✓' : '' },
              { key: 'actions', header: '', render: (p: OperationPersonnel) => (
                <Button variant="ghost" size="sm" onClick={() => deleteMutation.mutate(p.id)}>
                  <TrashIcon className="h-4 w-4 text-red-500" />
                </Button>
              )},
            ]}
            data={reinforcement}
            keyExtractor={(p) => p.id}
          />
        )}
      </Card>

      <Modal isOpen={showAdd} onClose={() => setShowAdd(false)} title="Einsatzkraft hinzufügen" size="md"
        footer={<><Button variant="secondary" onClick={() => setShowAdd(false)}>Abbrechen</Button><Button variant="primary" onClick={() => addMutation.mutate()} loading={addMutation.isPending} disabled={!newEntry.memberId}>Hinzufügen</Button></>}>
        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Mitglied</label>
            <select value={newEntry.memberId} onChange={(e) => setNewEntry(p => ({ ...p, memberId: parseInt(e.target.value) }))} className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm">
              <option value={0}>-- Mitglied auswählen --</option>
              {(members || []).map((m: { id: number; firstName: string; lastName: string }) => (
                <option key={m.id} value={m.id}>{m.lastName}, {m.firstName}</option>
              ))}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Funktion</label>
            <select value={newEntry.function} onChange={(e) => setNewEntry(p => ({ ...p, function: e.target.value }))} className="w-full rounded-md border-gray-300 shadow-sm focus:border-primary-500 focus:ring-primary-500 sm:text-sm">
              {FUNCTIONS.map(f => <option key={f} value={f}>{f}</option>)}
            </select>
          </div>
          <Input label="Fahrzeug" value={newEntry.vehicleName} onChange={(e) => setNewEntry(p => ({ ...p, vehicleName: e.target.value }))} />
        </div>
      </Modal>
    </div>
  );
}

// Report generation buttons
function GenerateReportButton({ operationId }: { operationId: number }) {
  const queryClient = useQueryClient();
  const [generating, setGenerating] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleGenerate = async () => {
    setGenerating(true);
    setError('');
    setSuccess('');
    try {
      const res = await operationsApi.generateReport(operationId);
      setSuccess(`Einsatzbericht generiert: ${(res.data as { data: { fileName: string } }).data.fileName}`);
      queryClient.invalidateQueries({ queryKey: ['operation-documents', operationId] });
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Generierung fehlgeschlagen';
      setError(msg);
    } finally {
      setGenerating(false);
    }
  };

  return (
    <div>
      <Button variant="primary" icon={<DocumentArrowDownIcon />} onClick={handleGenerate} loading={generating}>
        Einsatzbericht generieren
      </Button>
      {error && <p className="mt-1 text-xs text-red-600">{error}</p>}
      {success && <p className="mt-1 text-xs text-green-600">{success}</p>}
    </div>
  );
}

function GeneratePersonnelSheetButton({ operationId }: { operationId: number }) {
  const queryClient = useQueryClient();
  const [generating, setGenerating] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleGenerate = async () => {
    setGenerating(true);
    setError('');
    setSuccess('');
    try {
      const res = await operationsApi.generatePersonnelSheet(operationId);
      setSuccess(`Kräftenachweis generiert: ${(res.data as { data: { fileName: string } }).data.fileName}`);
      queryClient.invalidateQueries({ queryKey: ['operation-documents', operationId] });
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Generierung fehlgeschlagen';
      setError(msg);
    } finally {
      setGenerating(false);
    }
  };

  return (
    <div>
      <Button variant="secondary" icon={<DocumentArrowDownIcon />} onClick={handleGenerate} loading={generating}>
        Kräftenachweis generieren
      </Button>
      {error && <p className="mt-1 text-xs text-red-600">{error}</p>}
      {success && <p className="mt-1 text-xs text-green-600">{success}</p>}
    </div>
  );
}
