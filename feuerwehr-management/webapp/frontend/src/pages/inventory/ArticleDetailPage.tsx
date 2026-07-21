import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeftIcon, PlusIcon, TrashIcon, ArrowDownTrayIcon, ClipboardDocumentCheckIcon } from '@heroicons/react/24/outline';
import { Article, ArticleInspection, ArticleDefect, ArticleRepair, ArticleInspectionStandard, ArticleInspectionSchedule, ArticleDocument, InspectionType } from '../../types';
import { QrSingleView } from '../../components/inventory/QrPrintView';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { formatDate, formatCurrency } from '../../utils/format';
import { inspectionsApi } from '../../api/inspections';
import { defectsApi } from '../../api/defects';
import { repairsApi } from '../../api/repairs';
import { CriteriaInspectionModal } from '../../components/inspections/CriteriaInspectionModal';
import { settingsApi } from '../../api/settings';
import client from '../../api/client';

type Tab = 'stammdaten' | 'dokumente' | 'pruefgrundsaetze' | 'pruefintervalle' | 'maengel' | 'reparaturen' | 'historie';

export function ArticleDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [tab, setTab] = useState<Tab>('stammdaten');
  const [showInspection, setShowInspection] = useState(false);

  const { data: articleRes, isLoading } = useQuery({
    queryKey: ['article', id],
    queryFn: () => client.get<{ data: Article }>(`/inventory/articles/${id}`).then(r => r.data.data),
    enabled: !!id,
  });
  const article = articleRes;

  if (isLoading) return <div className="text-center py-8 text-gray-500">Laden...</div>;
  if (!article) return <div className="text-center py-8 text-gray-500">Artikel nicht gefunden</div>;

  const tabs = [
    { id: 'stammdaten' as const, label: 'Stammdaten' },
    { id: 'dokumente' as const, label: 'Dokumente' },
    { id: 'pruefgrundsaetze' as const, label: 'Prüfgrundsätze' },
    { id: 'pruefintervalle' as const, label: 'Prüfintervalle' },
    { id: 'maengel' as const, label: 'Mängel' },
    { id: 'reparaturen' as const, label: 'Reparaturen' },
    { id: 'historie' as const, label: 'Prüfhistorie' },
  ];

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-3">
        <button onClick={() => navigate('/inventory')} className="p-1 text-gray-400 hover:text-gray-600">
          <ArrowLeftIcon className="h-5 w-5" />
        </button>
        <div>
          <h2 className="text-lg font-semibold text-gray-900">{article.name}</h2>
          <p className="text-sm text-gray-500">
            {article.inventoryNumber && `Inv.-Nr.: ${article.inventoryNumber}`}
            {article.manufacturer && ` | ${article.manufacturer}`}
            {article.deviceSubclass?.deviceClass && ` | ${article.deviceSubclass.deviceClass.name}`}
          </p>
        </div>
        {article.isDecommissioned && <Badge variant="default">Außer Dienst</Badge>}
        <div className="ml-auto">
          <Button variant="primary" size="sm" icon={<ClipboardDocumentCheckIcon />} onClick={() => setShowInspection(true)}>
            Prüfung durchführen
          </Button>
        </div>
      </div>

      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {tabs.map(t => (
            <button key={t.id} onClick={() => setTab(t.id)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'stammdaten' && <StammdatenTab article={article} />}
      {tab === 'dokumente' && <DokumenteTab articleId={article.id} />}
      {tab === 'pruefgrundsaetze' && <PruefgrundsaetzeTab articleId={article.id} />}
      {tab === 'pruefintervalle' && <PruefintervalleTab articleId={article.id} />}
      {tab === 'maengel' && <MaengelTab articleId={article.id} articleName={article.name} />}
      {tab === 'reparaturen' && <ReparaturenTab articleId={article.id} articleName={article.name} />}
      {tab === 'historie' && <HistorieTab articleId={article.id} />}

      <CriteriaInspectionModal
        isOpen={showInspection}
        onClose={() => setShowInspection(false)}
        preselectedArticle={article}
        articles={[article]}
      />
    </div>
  );
}

function StammdatenTab({ article }: { article: Article }) {
  const fields = [
    { label: 'Bezeichnung', value: article.name },
    { label: 'Bezeichnung LB', value: article.designationLB },
    { label: 'Inventarnummer', value: article.inventoryNumber },
    { label: 'Inv.-Nr. Gemeinde (DOPPIK)', value: article.communityInventoryNumber },
    { label: 'Inv.-Nr. MP Feuer', value: article.mpFeuerInventoryNumber },
    { label: 'Hersteller', value: article.manufacturer },
    { label: 'Seriennummer', value: article.serialNumber },
    { label: 'Typ', value: article.articleType },
    { label: 'DIN', value: article.din },
    { label: 'Spezifikation', value: article.specification },
    { label: 'Herstellerdatum', value: article.manufacturingDate ? formatDate(article.manufacturingDate) : null },
    { label: 'Indienststellung', value: article.commissionedDate ? formatDate(article.commissionedDate) : null },
    { label: 'Außerdienststellung', value: article.decommissionedDate ? formatDate(article.decommissionedDate) : null },
    { label: 'Aussonderungsfrist', value: article.retirementPeriodMonths ? `${article.retirementPeriodMonths} Monate` : null },
    { label: 'Prüfintervall', value: article.inspectionInterval ? `${article.inspectionInterval} Monate` : null },
    { label: 'Wert', value: article.value ? formatCurrency(article.value) : null },
    { label: 'Geräteklasse', value: article.deviceSubclass?.deviceClass?.name },
    { label: 'Unterklasse', value: article.deviceSubclass?.name },
    { label: 'Lagerort', value: article.warehouse?.name },
    { label: 'Beschreibung', value: article.description },
  ];

  const baseUrl = window.location.origin;

  return (
    <div className="flex gap-6">
      <div className="flex-1 bg-white rounded-lg border border-gray-200 p-6">
        <div className="grid grid-cols-2 gap-x-8 gap-y-3">
          {fields.map((f, i) => (
            <div key={i}>
              <dt className="text-xs font-medium text-gray-500">{f.label}</dt>
              <dd className="text-sm text-gray-900">{f.value || '-'}</dd>
            </div>
          ))}
        </div>
      </div>
      {article.inventoryNumber && (
        <div className="w-56 flex-shrink-0 bg-white rounded-lg border border-gray-200 p-4">
          <QrSingleView article={article} baseUrl={baseUrl} onPrint={() => {
            const printWin = window.open('', '_blank', 'width=300,height=400');
            if (!printWin) return;
            import('qrcode').then(QRCode => {
              QRCode.default.toDataURL(`${baseUrl}/scan/${article.inventoryNumber}`, { width: 200, margin: 2 }).then((url: string) => {
                printWin.document.write(`<html><head><title>QR Etikett</title><style>body{margin:0;display:flex;align-items:center;justify-content:center;height:100vh;font-family:sans-serif}
                .label{text-align:center;width:40mm;height:30mm;padding:2mm;border:0.5px dashed #ccc}
                img{width:20mm;height:20mm} .inv{font-size:8pt;font-weight:bold} .name{font-size:7pt} .lb{font-size:6pt;color:#666}
                @media print{body{height:auto}.label{border:none}}</style></head>
                <body><div class="label"><img src="${url}"/><div class="inv">${article.inventoryNumber}</div><div class="name">${article.name}</div><div class="lb">${article.designationLB || 'LB12'}</div></div></body></html>`);
                printWin.document.close();
                setTimeout(() => { printWin.print(); }, 300);
              });
            });
          }} />
        </div>
      )}
    </div>
  );
}

function DokumenteTab({ articleId }: { articleId: number }) {
  const queryClient = useQueryClient();
  const { data: docsRes, isLoading } = useQuery({
    queryKey: ['article-documents', articleId],
    queryFn: () => client.get<{ data: ArticleDocument[] }>(`/inventory/articles/${articleId}/documents`).then(r => r.data.data),
  });

  const uploadMut = useMutation({
    mutationFn: (file: File) => {
      const fd = new FormData();
      fd.append('file', file);
      return client.post(`/inventory/articles/${articleId}/documents`, fd);
    },
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['article-documents', articleId] }),
  });

  const deleteMut = useMutation({
    mutationFn: (docId: number) => client.delete(`/inventory/articles/documents/${docId}`),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['article-documents', articleId] }),
  });

  const downloadDoc = async (docId: number, fileName: string) => {
    const res = await client.get(`/inventory/articles/documents/${docId}/download`, { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const a = document.createElement('a');
    a.href = url; a.download = fileName; a.click();
    window.URL.revokeObjectURL(url);
  };

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h3 className="text-sm font-medium text-gray-700">PDF-Anhänge</h3>
        <label className="cursor-pointer">
          <input type="file" accept=".pdf" className="hidden" onChange={(e) => { if (e.target.files?.[0]) uploadMut.mutate(e.target.files[0]); }} />
          <Button variant="primary" size="sm" icon={<PlusIcon />} loading={uploadMut.isPending} onClick={() => {}}>Hochladen</Button>
        </label>
      </div>
      <div className="bg-white rounded-lg border border-gray-200">
        <Table
          columns={[
            { key: 'fileName', header: 'Dateiname', render: (d: ArticleDocument) => <span className="text-sm">{d.fileName}</span> },
            { key: 'uploadedBy', header: 'Hochgeladen von' },
            { key: 'createdAt', header: 'Datum', render: (d: ArticleDocument) => formatDate(d.createdAt) },
            { key: 'actions', header: '', render: (d: ArticleDocument) => (
              <div className="flex gap-2">
                <button className="text-primary-600 hover:text-primary-700" onClick={() => downloadDoc(d.id, d.fileName)}><ArrowDownTrayIcon className="h-4 w-4" /></button>
                <button className="text-red-500 hover:text-red-700" onClick={() => { if (confirm('Dokument löschen?')) deleteMut.mutate(d.id); }}><TrashIcon className="h-4 w-4" /></button>
              </div>
            )},
          ]}
          data={docsRes || []}
          loading={isLoading}
          emptyMessage="Keine Dokumente vorhanden."
          keyExtractor={(d) => d.id}
        />
      </div>
    </div>
  );
}

function PruefgrundsaetzeTab({ articleId }: { articleId: number }) {
  const queryClient = useQueryClient();
  const [showModal, setShowModal] = useState(false);
  const [editItem, setEditItem] = useState<ArticleInspectionStandard | null>(null);
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  const { data: standards, isLoading } = useQuery({
    queryKey: ['article-standards', articleId],
    queryFn: () => client.get<{ data: ArticleInspectionStandard[] }>(`/inventory/articles/${articleId}/standards`).then(r => r.data.data),
  });

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['article-standards', articleId] });

  const saveMut = useMutation({
    mutationFn: () => {
      if (editItem) return client.put(`/inventory/articles/standards/${editItem.id}`, { name, description: description || null });
      return client.post(`/inventory/articles/${articleId}/standards`, { name, description: description || null });
    },
    onSuccess: () => { invalidate(); setShowModal(false); },
  });

  const deleteMut = useMutation({ mutationFn: (id: number) => client.delete(`/inventory/articles/standards/${id}`), onSuccess: invalidate });

  const openCreate = () => { setEditItem(null); setName(''); setDescription(''); setShowModal(true); };
  const openEdit = (s: ArticleInspectionStandard) => { setEditItem(s); setName(s.name); setDescription(s.description || ''); setShowModal(true); };

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <div>
          <h3 className="text-sm font-medium text-gray-700">Prüfgrundsätze</h3>
          <p className="text-xs text-gray-500">Abweichende Prüfgrundsätze für diesen Artikel (z.B. DGUV 305-002, Herstellerangaben)</p>
        </div>
        <Button variant="primary" size="sm" icon={<PlusIcon />} onClick={openCreate}>Hinzufügen</Button>
      </div>
      <div className="bg-white rounded-lg border border-gray-200">
        <Table
          columns={[
            { key: 'name', header: 'Bezeichnung' },
            { key: 'description', header: 'Beschreibung', render: (s: ArticleInspectionStandard) => s.description || '-' },
            { key: 'actions', header: '', render: (s: ArticleInspectionStandard) => (
              <div className="flex gap-2">
                <Button size="sm" variant="secondary" onClick={() => openEdit(s)}>Bearbeiten</Button>
                <Button size="sm" variant="danger" onClick={() => { if (confirm('Löschen?')) deleteMut.mutate(s.id); }}>Löschen</Button>
              </div>
            )},
          ]}
          data={standards || []}
          loading={isLoading}
          emptyMessage="Keine Prüfgrundsätze hinterlegt."
          keyExtractor={(s) => s.id}
        />
      </div>
      <Modal isOpen={showModal} onClose={() => setShowModal(false)} title={editItem ? 'Prüfgrundsatz bearbeiten' : 'Neuer Prüfgrundsatz'} size="sm">
        <form onSubmit={(e) => { e.preventDefault(); saveMut.mutate(); }} className="space-y-4">
          <Input label="Bezeichnung" value={name} onChange={(e) => setName(e.target.value)} required />
          <Textarea label="Beschreibung" value={description} onChange={(e) => setDescription(e.target.value)} rows={3} />
          <div className="flex justify-end gap-2">
            <Button variant="secondary" type="button" onClick={() => setShowModal(false)}>Abbrechen</Button>
            <Button type="submit" loading={saveMut.isPending} disabled={!name.trim()}>Speichern</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}

function PruefintervalleTab({ articleId }: { articleId: number }) {
  const queryClient = useQueryClient();
  const [showModal, setShowModal] = useState(false);
  const [editItem, setEditItem] = useState<ArticleInspectionSchedule | null>(null);
  const [typeId, setTypeId] = useState('');
  const [intervalMonths, setIntervalMonths] = useState('');

  const { data: schedules, isLoading } = useQuery({
    queryKey: ['article-schedules', articleId],
    queryFn: () => client.get<{ data: ArticleInspectionSchedule[] }>(`/inventory/articles/${articleId}/schedules`).then(r => r.data.data),
  });

  const { data: typesRes } = useQuery({
    queryKey: ['inspection-types'],
    queryFn: () => settingsApi.getInspectionTypes(),
  });
  const types: InspectionType[] = typesRes?.data?.data || [];

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['article-schedules', articleId] });

  const saveMut = useMutation({
    mutationFn: () => {
      const data = { inspectionTypeId: parseInt(typeId), intervalMonths: parseInt(intervalMonths) };
      if (editItem) return client.put(`/inventory/articles/schedules/${editItem.id}`, data);
      return client.post(`/inventory/articles/${articleId}/schedules`, data);
    },
    onSuccess: () => { invalidate(); setShowModal(false); },
  });

  const deleteMut = useMutation({ mutationFn: (id: number) => client.delete(`/inventory/articles/schedules/${id}`), onSuccess: invalidate });

  const openCreate = () => { setEditItem(null); setTypeId(''); setIntervalMonths(''); setShowModal(true); };

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <div>
          <h3 className="text-sm font-medium text-gray-700">Prüfintervalle je Prüfart</h3>
          <p className="text-xs text-gray-500">Abweichende Intervalle pro Prüfungsart für diesen Artikel</p>
        </div>
        <Button variant="primary" size="sm" icon={<PlusIcon />} onClick={openCreate}>Hinzufügen</Button>
      </div>
      <div className="bg-white rounded-lg border border-gray-200">
        <Table
          columns={[
            { key: 'type', header: 'Prüfart', render: (s: ArticleInspectionSchedule) => s.inspectionType?.name || '-' },
            { key: 'interval', header: 'Intervall', render: (s: ArticleInspectionSchedule) => `${s.intervalMonths} Monate` },
            { key: 'actions', header: '', render: (s: ArticleInspectionSchedule) => (
              <Button size="sm" variant="danger" onClick={() => { if (confirm('Löschen?')) deleteMut.mutate(s.id); }}>Löschen</Button>
            )},
          ]}
          data={schedules || []}
          loading={isLoading}
          emptyMessage="Keine typ-spezifischen Intervalle."
          keyExtractor={(s) => s.id}
        />
      </div>
      <Modal isOpen={showModal} onClose={() => setShowModal(false)} title="Prüfintervall hinzufügen" size="sm">
        <form onSubmit={(e) => { e.preventDefault(); saveMut.mutate(); }} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Prüfart</label>
            <select value={typeId} onChange={(e) => setTypeId(e.target.value)}
              className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
              <option value="">-- Prüfart wählen --</option>
              {types.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
            </select>
          </div>
          <Input label="Intervall (Monate)" value={intervalMonths} onChange={(e) => setIntervalMonths(e.target.value)} type="number" required />
          <div className="flex justify-end gap-2">
            <Button variant="secondary" type="button" onClick={() => setShowModal(false)}>Abbrechen</Button>
            <Button type="submit" loading={saveMut.isPending} disabled={!typeId || !intervalMonths}>Speichern</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}

function MaengelTab({ articleId, articleName }: { articleId: number; articleName: string }) {
  const queryClient = useQueryClient();
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ description: '', severity: 'medium', reportedBy: '', reportedAt: new Date().toISOString().split('T')[0], notes: '' });

  const { data: defectsRes, isLoading } = useQuery({
    queryKey: ['article-defects', articleId],
    queryFn: () => defectsApi.getAll({ articleId, limit: 100 }).then(r => r.data.data),
  });

  const createMut = useMutation({
    mutationFn: () => defectsApi.create({ articleId, ...form }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['article-defects', articleId] }); setShowModal(false); },
  });

  const severityBadge = (s: string) => {
    const map: Record<string, { variant: 'info' | 'warning' | 'danger'; label: string }> = {
      low: { variant: 'info', label: 'Gering' }, medium: { variant: 'warning', label: 'Mittel' },
      high: { variant: 'danger', label: 'Hoch' }, critical: { variant: 'danger', label: 'Kritisch' },
    };
    const m = map[s] || map.medium;
    return <Badge variant={m.variant}>{m.label}</Badge>;
  };

  const statusBadge = (s: string) => {
    const map: Record<string, { variant: 'danger' | 'warning' | 'success' | 'default'; label: string }> = {
      open: { variant: 'danger', label: 'Offen' }, in_progress: { variant: 'warning', label: 'In Bearbeitung' },
      resolved: { variant: 'success', label: 'Behoben' }, closed: { variant: 'default', label: 'Geschlossen' },
    };
    const m = map[s] || map.open;
    return <Badge variant={m.variant}>{m.label}</Badge>;
  };

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h3 className="text-sm font-medium text-gray-700">Mängel für {articleName}</h3>
        <Button variant="primary" size="sm" icon={<PlusIcon />} onClick={() => setShowModal(true)}>Neuer Mangel</Button>
      </div>
      <div className="bg-white rounded-lg border border-gray-200">
        <Table
          columns={[
            { key: 'reportedAt', header: 'Datum', render: (d: ArticleDefect) => formatDate(d.reportedAt) },
            { key: 'description', header: 'Beschreibung', render: (d: ArticleDefect) => <span className="text-sm max-w-[300px] truncate block">{d.description}</span> },
            { key: 'severity', header: 'Schweregrad', render: (d: ArticleDefect) => severityBadge(d.severity) },
            { key: 'status', header: 'Status', render: (d: ArticleDefect) => statusBadge(d.status) },
            { key: 'reportedBy', header: 'Gemeldet von' },
          ]}
          data={defectsRes || []}
          loading={isLoading}
          emptyMessage="Keine Mängel gemeldet."
          keyExtractor={(d) => d.id}
        />
      </div>
      <Modal isOpen={showModal} onClose={() => setShowModal(false)} title="Neuen Mangel melden" size="md"
        footer={<><Button variant="secondary" onClick={() => setShowModal(false)}>Abbrechen</Button><Button onClick={() => createMut.mutate()} loading={createMut.isPending} disabled={!form.description || !form.reportedBy}>Speichern</Button></>}>
        <div className="space-y-3">
          <Textarea label="Beschreibung" value={form.description} onChange={(e) => setForm(f => ({ ...f, description: e.target.value }))} rows={3} required />
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">Schweregrad</label>
              <select value={form.severity} onChange={(e) => setForm(f => ({ ...f, severity: e.target.value }))}
                className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
                <option value="low">Gering</option><option value="medium">Mittel</option><option value="high">Hoch</option><option value="critical">Kritisch</option>
              </select>
            </div>
            <Input label="Gemeldet von" value={form.reportedBy} onChange={(e) => setForm(f => ({ ...f, reportedBy: e.target.value }))} required />
          </div>
          <Input label="Meldedatum" value={form.reportedAt} onChange={(e) => setForm(f => ({ ...f, reportedAt: e.target.value }))} type="date" />
          <Textarea label="Bemerkungen" value={form.notes} onChange={(e) => setForm(f => ({ ...f, notes: e.target.value }))} rows={2} />
        </div>
      </Modal>
    </div>
  );
}

function ReparaturenTab({ articleId, articleName }: { articleId: number; articleName: string }) {
  const queryClient = useQueryClient();
  const [showModal, setShowModal] = useState(false);
  const [form, setForm] = useState({ description: '', repairedBy: '', repairedAt: new Date().toISOString().split('T')[0], cost: '', notes: '' });

  const { data: repairsRes, isLoading } = useQuery({
    queryKey: ['article-repairs', articleId],
    queryFn: () => repairsApi.getAll({ articleId, limit: 100 }).then(r => r.data.data),
  });

  const createMut = useMutation({
    mutationFn: () => repairsApi.create({
      articleId, description: form.description, repairedBy: form.repairedBy, repairedAt: form.repairedAt,
      cost: form.cost ? parseFloat(form.cost) : undefined, notes: form.notes || undefined,
    }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['article-repairs', articleId] }); setShowModal(false); },
  });

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h3 className="text-sm font-medium text-gray-700">Reparaturen für {articleName}</h3>
        <Button variant="primary" size="sm" icon={<PlusIcon />} onClick={() => setShowModal(true)}>Neue Reparatur</Button>
      </div>
      <div className="bg-white rounded-lg border border-gray-200">
        <Table
          columns={[
            { key: 'repairedAt', header: 'Datum', render: (r: ArticleRepair) => formatDate(r.repairedAt) },
            { key: 'description', header: 'Beschreibung', render: (r: ArticleRepair) => <span className="text-sm max-w-[300px] truncate block">{r.description}</span> },
            { key: 'cost', header: 'Kosten', render: (r: ArticleRepair) => r.cost ? formatCurrency(r.cost) : '-' },
            { key: 'repairedBy', header: 'Durchgeführt von' },
          ]}
          data={repairsRes || []}
          loading={isLoading}
          emptyMessage="Keine Reparaturen dokumentiert."
          keyExtractor={(r) => r.id}
        />
      </div>
      <Modal isOpen={showModal} onClose={() => setShowModal(false)} title="Neue Reparatur dokumentieren" size="md"
        footer={<><Button variant="secondary" onClick={() => setShowModal(false)}>Abbrechen</Button><Button onClick={() => createMut.mutate()} loading={createMut.isPending} disabled={!form.description || !form.repairedBy}>Speichern</Button></>}>
        <div className="space-y-3">
          <Textarea label="Beschreibung" value={form.description} onChange={(e) => setForm(f => ({ ...f, description: e.target.value }))} rows={3} required />
          <div className="grid grid-cols-2 gap-3">
            <Input label="Durchgeführt von" value={form.repairedBy} onChange={(e) => setForm(f => ({ ...f, repairedBy: e.target.value }))} required />
            <Input label="Datum" value={form.repairedAt} onChange={(e) => setForm(f => ({ ...f, repairedAt: e.target.value }))} type="date" />
          </div>
          <div className="grid grid-cols-2 gap-3">
            <Input label="Kosten (€)" value={form.cost} onChange={(e) => setForm(f => ({ ...f, cost: e.target.value }))} type="number" step="0.01" />
            <div />
          </div>
          <Textarea label="Bemerkungen" value={form.notes} onChange={(e) => setForm(f => ({ ...f, notes: e.target.value }))} rows={2} />
        </div>
      </Modal>
    </div>
  );
}

function HistorieTab({ articleId }: { articleId: number }) {
  const queryClient = useQueryClient();
  const { data: inspectionsRes, isLoading } = useQuery({
    queryKey: ['article-inspections', articleId],
    queryFn: () => inspectionsApi.getArticleInspections(articleId).then(r => r.data.data),
  });
  const [editId, setEditId] = useState<number | null>(null);
  const [nextDate, setNextDate] = useState('');

  const updateNextDateMut = useMutation({
    mutationFn: () => inspectionsApi.update(editId!, { nextDueDate: nextDate || null }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['article-inspections', articleId] }); setEditId(null); },
  });

  return (
    <div className="bg-white rounded-lg border border-gray-200">
      <Table
        columns={[
          { key: 'inspectedAt', header: 'Datum', render: (i: ArticleInspection) => formatDate(i.inspectedAt) },
          { key: 'inspectionType', header: 'Prüfart', render: (i: ArticleInspection) => i.inspectionType?.name || '-' },
          { key: 'inspectedBy', header: 'Prüfer' },
          { key: 'result', header: 'Ergebnis', render: (i: ArticleInspection) => (
            <Badge variant={i.result === 'passed' ? 'success' : 'danger'}>
              {i.result === 'passed' ? 'Bestanden' : 'Nicht bestanden'}
            </Badge>
          )},
          { key: 'criteria', header: 'Kriterien', render: (i: ArticleInspection) => {
            const total = i.criterionResults?.length || 0;
            const io = i.criterionResults?.filter(cr => cr.result === 'io').length || 0;
            return total > 0 ? `${io}/${total} io` : '-';
          }},
          { key: 'docs', header: 'PDF', render: (i: ArticleInspection) =>
            i.documents && i.documents.length > 0
              ? <button onClick={(e) => { e.stopPropagation(); inspectionsApi.downloadDocument(i.documents![0].id).then(res => { const url = URL.createObjectURL(new Blob([res.data], { type: 'application/pdf' })); window.open(url, '_blank'); }); }}
                  className="text-red-500 hover:text-red-700" title={i.documents[0].fileName}>
                  <svg className="h-4 w-4" fill="currentColor" viewBox="0 0 20 20"><path d="M4 18h12a2 2 0 002-2V6.414A2 2 0 0017.414 5L14 1.586A2 2 0 0012.586 1H4a2 2 0 00-2 2v13a2 2 0 002 2z"/></svg>
                </button>
              : <span className="text-gray-300">-</span>
          },
          { key: 'notes', header: 'Bemerkungen', render: (i: ArticleInspection) => <span className="text-sm text-gray-500 truncate max-w-[200px] block">{i.notes || '-'}</span> },
          { key: 'nextDueDate', header: 'Nächste Prüfung', render: (i: ArticleInspection) => (
            editId === i.id ? (
              <div className="flex items-center gap-1">
                <input type="date" value={nextDate} onChange={(e) => setNextDate(e.target.value)}
                  className="px-2 py-1 border border-gray-300 rounded text-xs w-32" />
                <button onClick={() => updateNextDateMut.mutate()} className="text-green-600 text-xs font-medium">OK</button>
                <button onClick={() => setEditId(null)} className="text-gray-400 text-xs">X</button>
              </div>
            ) : (
              <button onClick={() => { setEditId(i.id); setNextDate(i.nextDueDate ? i.nextDueDate.split('T')[0] : ''); }}
                className="text-sm text-gray-700 hover:text-primary-600 hover:underline" title="Klicken zum Ändern">
                {i.nextDueDate ? formatDate(i.nextDueDate) : '-'}
              </button>
            )
          )},
        ]}
        data={inspectionsRes || []}
        loading={isLoading}
        emptyMessage="Keine Prüfungen dokumentiert."
        keyExtractor={(i) => i.id}
      />
    </div>
  );
}
