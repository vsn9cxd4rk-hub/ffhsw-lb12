import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { defectsApi } from '../../api/defects';
import { repairsApi } from '../../api/repairs';
import client from '../../api/client';
import { Card } from '../../components/ui/Card';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { Select } from '../../components/ui/Select';
import { Pagination } from '../../components/ui/Pagination';
import { ArticleDefect, ArticleRepair, Article, DeviceClass } from '../../types';
import { formatDate, formatCurrency } from '../../utils/format';
import { settingsApi } from '../../api/settings';

type Tab = 'defects' | 'repairs';

// -- Severity helpers --

const severityLabels: Record<string, string> = {
  low: 'Gering',
  medium: 'Mittel',
  high: 'Hoch',
  critical: 'Kritisch',
};

const severityVariants: Record<string, 'info' | 'warning' | 'danger'> = {
  low: 'info',
  medium: 'warning',
  high: 'danger',
  critical: 'danger',
};

// -- Status helpers --

const statusLabels: Record<string, string> = {
  open: 'Offen',
  in_progress: 'In Bearbeitung',
  resolved: 'Behoben',
  closed: 'Geschlossen',
};

const statusVariants: Record<string, 'danger' | 'warning' | 'success' | 'default'> = {
  open: 'danger',
  in_progress: 'warning',
  resolved: 'success',
  closed: 'default',
};

// -- Default form states --

interface DefectForm {
  articleId: string;
  description: string;
  severity: string;
  status: string;
  reportedBy: string;
  reportedAt: string;
  resolvedBy: string;
  resolvedAt: string;
  notes: string;
}

const emptyDefectForm: DefectForm = {
  articleId: '',
  description: '',
  severity: 'medium',
  status: 'open',
  reportedBy: '',
  reportedAt: new Date().toISOString().slice(0, 10),
  resolvedBy: '',
  resolvedAt: '',
  notes: '',
};

interface RepairForm {
  articleId: string;
  repairedAt: string;
  repairedBy: string;
  description: string;
  cost: string;
  notes: string;
  defectId: string;
}

const emptyRepairForm: RepairForm = {
  articleId: '',
  repairedAt: new Date().toISOString().slice(0, 10),
  repairedBy: '',
  description: '',
  cost: '',
  notes: '',
  defectId: '',
};

// =====================
// Main Component
// =====================

export function DefectsPage() {
  const [tab, setTab] = useState<Tab>('defects');

  return (
    <div className="space-y-4">
      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {([
            { id: 'defects' as Tab, label: 'Mängel' },
            { id: 'repairs' as Tab, label: 'Reparaturen' },
          ]).map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${
                tab === t.id
                  ? 'border-primary-600 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'defects' && <DefectsTab />}
      {tab === 'repairs' && <RepairsTab />}
    </div>
  );
}

// =====================
// Articles hook
// =====================

function useArticles() {
  return useQuery({
    queryKey: ['articles-list'],
    queryFn: () =>
      client
        .get<{ data: Article[] }>('/inventory/articles', { params: { limit: 1000 } })
        .then((r) => r.data.data),
  });
}

function ArticleSelector({ value, onChange, required }: { value: string; onChange: (val: string, label?: string) => void; required?: boolean }) {
  const { data: articles } = useArticles();
  const { data: vehiclesRes } = useQuery({
    queryKey: ['vehicles-for-defects'],
    queryFn: () => client.get<{ data: Array<{ id: number; name: string }> }>('/vehicles').then(r => r.data.data),
  });
  const { data: classesRes } = useQuery({
    queryKey: ['device-classes'],
    queryFn: () => settingsApi.getDeviceClasses(),
  });
  const deviceClasses: DeviceClass[] = classesRes?.data?.data || [];

  const [filterClass, setFilterClass] = useState('');
  const [filterSubclass, setFilterSubclass] = useState('');

  const selectedClass = deviceClasses.find(dc => dc.id === parseInt(filterClass));

  // Filter articles
  let filtered = articles || [];
  if (filterClass === 'none') {
    filtered = filtered.filter(a => !a.deviceSubclassId);
  } else if (filterClass) {
    filtered = filtered.filter(a => a.deviceSubclass?.deviceClass?.id === parseInt(filterClass));
    if (filterSubclass) {
      filtered = filtered.filter(a => a.deviceSubclassId === parseInt(filterSubclass));
    }
  }

  return (
    <div className="space-y-2">
      <label className="block text-sm font-medium text-gray-700">
        Artikel / Gerät {required && <span className="text-red-500">*</span>}
      </label>
      <div className="grid grid-cols-2 gap-2">
        <select value={filterClass}
          onChange={(e) => { setFilterClass(e.target.value); setFilterSubclass(''); }}
          className="px-2 py-1.5 border border-gray-300 rounded-md text-xs focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
          <option value="">Alle Geräteklassen</option>
          <option value="none">Ohne Geräteklasse</option>
          {deviceClasses.map(dc => <option key={dc.id} value={dc.id}>{dc.name}</option>)}
        </select>
        <select value={filterSubclass}
          onChange={(e) => setFilterSubclass(e.target.value)}
          disabled={!filterClass || filterClass === 'none'}
          className="px-2 py-1.5 border border-gray-300 rounded-md text-xs focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white disabled:bg-gray-100">
          <option value="">Alle Unterklassen</option>
          {selectedClass?.subclasses?.map(sc => <option key={sc.id} value={sc.id}>{sc.name}</option>)}
        </select>
      </div>
      <select value={value} onChange={(e) => {
          const v = e.target.value;
          const label = e.target.selectedOptions[0]?.text || '';
          const isSpecial = v.startsWith('vehicle_') || v === 'sonstige';
          onChange(v, isSpecial ? label : undefined);
        }}
        className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
        <option value="">-- Auswählen --</option>
        {filtered.length > 0 && (
          <optgroup label="Artikel">
            {filtered.map(a => (
              <option key={a.id} value={a.id}>
                {a.name}{a.inventoryNumber ? ` (${a.inventoryNumber})` : ''}{a.warehouse ? ` [${a.warehouse.name}]` : ''}
              </option>
            ))}
          </optgroup>
        )}
        {vehiclesRes && vehiclesRes.length > 0 && (
          <optgroup label="Fahrzeuge">
            {vehiclesRes.map(v => (
              <option key={`v_${v.id}`} value={`vehicle_${v.id}`}>{v.name}</option>
            ))}
          </optgroup>
        )}
        <optgroup label="Sonstige">
          <option value="sonstige">Sonstige</option>
        </optgroup>
      </select>
    </div>
  );
}

// =====================
// Defects Tab
// =====================

function DefectsTab() {
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [statusFilter, setStatusFilter] = useState('');
  const [severityFilter, setSeverityFilter] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [subjectLabel, setSubjectLabel] = useState('');
  const [editItem, setEditItem] = useState<ArticleDefect | null>(null);
  const [form, setForm] = useState<DefectForm>({ ...emptyDefectForm });

  const { data: articles } = useArticles();

  const { data, isLoading } = useQuery({
    queryKey: ['defects', page, statusFilter, severityFilter],
    queryFn: () =>
      defectsApi
        .getAll({
          page,
          limit: 20,
          status: statusFilter || undefined,
          severity: severityFilter || undefined,
        })
        .then((r) => r.data),
  });

  const defects = data?.data || [];
  const pagination = data?.pagination;

  const saveMutation = useMutation({
    mutationFn: () => {
      const isArticle = form.articleId && !form.articleId.startsWith('vehicle_') && form.articleId !== 'sonstige';
      const payload: Record<string, unknown> = {
        articleId: isArticle ? parseInt(form.articleId) : null,
        subject: !isArticle ? (subjectLabel || (form.articleId === 'sonstige' ? 'Sonstige' : form.articleId)) : null,
        description: form.description,
        severity: form.severity,
        reportedBy: form.reportedBy,
        reportedAt: form.reportedAt,
        notes: form.notes || undefined,
      };

      if (editItem) {
        payload.status = form.status;
        if (
          (form.status === 'resolved' || form.status === 'closed') &&
          form.resolvedBy
        ) {
          payload.resolvedBy = form.resolvedBy;
          payload.resolvedAt = form.resolvedAt || new Date().toISOString().slice(0, 10);
        }
        return defectsApi.update(editItem.id, payload);
      }
      return defectsApi.create(payload as {
        articleId: number;
        reportedBy: string;
        reportedAt: string;
        description: string;
        severity: string;
        notes?: string;
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['defects'] });
      closeModal();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => defectsApi.delete(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['defects'] }),
  });

  function openCreate() {
    setEditItem(null);
    setForm({ ...emptyDefectForm, reportedAt: new Date().toISOString().slice(0, 10) });
    setShowModal(true);
  }

  function openEdit(item: ArticleDefect) {
    setEditItem(item);
    setForm({
      articleId: String(item.articleId),
      description: item.description,
      severity: item.severity,
      status: item.status,
      reportedBy: item.reportedBy,
      reportedAt: item.reportedAt ? item.reportedAt.slice(0, 10) : '',
      resolvedBy: item.resolvedBy || '',
      resolvedAt: item.resolvedAt ? item.resolvedAt.slice(0, 10) : '',
      notes: item.notes || '',
    });
    setShowModal(true);
  }

  function closeModal() {
    setShowModal(false);
    setEditItem(null);
    setForm({ ...emptyDefectForm });
  }

  function articleLabel(defect: ArticleDefect): string {
    if (defect.article) {
      const inv = defect.article.inventoryNumber ? ` (${defect.article.inventoryNumber})` : '';
      return `${defect.article.name}${inv}`;
    }
    if (defect.subject) return defect.subject;
    return defect.articleId ? `Artikel #${defect.articleId}` : '-';
  }

  const u = (k: keyof DefectForm, v: string) => setForm((f) => ({ ...f, [k]: v }));

  const canSave =
    form.articleId && form.description.trim() && form.reportedBy.trim() && form.reportedAt;


  return (
    <>
      {/* Filter bar */}
      <div className="flex flex-wrap items-end gap-4 mb-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Status</label>
          <select
            value={statusFilter}
            onChange={(e) => { setStatusFilter(e.target.value); setPage(1); }}
            className="block w-44 rounded-md border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500"
          >
            <option value="">Alle</option>
            <option value="open">Offen</option>
            <option value="in_progress">In Bearbeitung</option>
            <option value="resolved">Behoben</option>
            <option value="closed">Geschlossen</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Schweregrad</label>
          <select
            value={severityFilter}
            onChange={(e) => { setSeverityFilter(e.target.value); setPage(1); }}
            className="block w-44 rounded-md border border-gray-300 px-3 py-2 text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500"
          >
            <option value="">Alle</option>
            <option value="low">Gering</option>
            <option value="medium">Mittel</option>
            <option value="high">Hoch</option>
            <option value="critical">Kritisch</option>
          </select>
        </div>
        <div className="ml-auto">
          <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>
            Neuer Mangel
          </Button>
        </div>
      </div>

      {/* Table */}
      <Card>
        <Table
          columns={[
            {
              key: 'reportedAt',
              header: 'Datum',
              render: (r: ArticleDefect) => formatDate(r.reportedAt),
            },
            {
              key: 'article',
              header: 'Artikel',
              render: (r: ArticleDefect) => articleLabel(r),
            },
            {
              key: 'description',
              header: 'Beschreibung',
              render: (r: ArticleDefect) =>
                r.description.length > 80
                  ? `${r.description.slice(0, 80)}...`
                  : r.description,
            },
            {
              key: 'severity',
              header: 'Schweregrad',
              render: (r: ArticleDefect) => (
                <Badge variant={severityVariants[r.severity] || 'default'}>
                  {severityLabels[r.severity] || r.severity}
                </Badge>
              ),
            },
            {
              key: 'status',
              header: 'Status',
              render: (r: ArticleDefect) => (
                <Badge variant={statusVariants[r.status] || 'default'}>
                  {statusLabels[r.status] || r.status}
                </Badge>
              ),
            },
            {
              key: 'reportedBy',
              header: 'Gemeldet von',
            },
            {
              key: 'actions',
              header: 'Aktionen',
              render: (r: ArticleDefect) => (
                <div className="flex gap-2">
                  <Button size="sm" variant="secondary" onClick={() => openEdit(r)}>
                    Bearbeiten
                  </Button>
                  <Button size="sm" variant="danger" onClick={() => deleteMutation.mutate(r.id)}>
                    Löschen
                  </Button>
                </div>
              ),
            },
          ]}
          data={defects}
          loading={isLoading}
          emptyMessage="Keine Mängel vorhanden."
          keyExtractor={(r) => r.id}
        />
        {pagination && pagination.pages > 1 && (
          <Pagination
            page={pagination.page}
            pages={pagination.pages}
            total={pagination.total}
            limit={pagination.limit}
            onPageChange={setPage}
          />
        )}
      </Card>

      {/* Create / Edit Modal */}
      <Modal
        isOpen={showModal}
        onClose={closeModal}
        title={editItem ? 'Mangel bearbeiten' : 'Neuer Mangel'}
        size="lg"
        footer={
          <>
            <Button variant="secondary" onClick={closeModal}>
              Abbrechen
            </Button>
            <Button
              variant="primary"
              onClick={() => saveMutation.mutate()}
              loading={saveMutation.isPending}
              disabled={!canSave}
            >
              Speichern
            </Button>
          </>
        }
      >
        <div className="space-y-4">
          <ArticleSelector value={form.articleId} onChange={(val, label) => { u('articleId', val); if (label) setSubjectLabel(label); else setSubjectLabel(''); }} required />
          <Textarea
            label="Beschreibung"
            value={form.description}
            onChange={(e) => u('description', e.target.value)}
            required
          />
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Select
              label="Schweregrad"
              value={form.severity}
              onChange={(e) => u('severity', e.target.value)}
              options={[
                { value: 'low', label: 'Gering' },
                { value: 'medium', label: 'Mittel' },
                { value: 'high', label: 'Hoch' },
                { value: 'critical', label: 'Kritisch' },
              ]}
            />
            {editItem && (
              <Select
                label="Status"
                value={form.status}
                onChange={(e) => u('status', e.target.value)}
                options={[
                  { value: 'open', label: 'Offen' },
                  { value: 'in_progress', label: 'In Bearbeitung' },
                  { value: 'resolved', label: 'Behoben' },
                  { value: 'closed', label: 'Geschlossen' },
                ]}
              />
            )}
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Gemeldet von"
              value={form.reportedBy}
              onChange={(e) => u('reportedBy', e.target.value)}
              required
            />
            <Input
              label="Meldedatum"
              type="date"
              value={form.reportedAt}
              onChange={(e) => u('reportedAt', e.target.value)}
              required
            />
          </div>
          {editItem && (form.status === 'resolved' || form.status === 'closed') && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <Input
                label="Behoben von"
                value={form.resolvedBy}
                onChange={(e) => u('resolvedBy', e.target.value)}
              />
              <Input
                label="Behoben am"
                type="date"
                value={form.resolvedAt}
                onChange={(e) => u('resolvedAt', e.target.value)}
              />
            </div>
          )}
          <Textarea
            label="Bemerkungen"
            value={form.notes}
            onChange={(e) => u('notes', e.target.value)}
          />
        </div>
      </Modal>
    </>
  );
}

// =====================
// Repairs Tab
// =====================

function RepairsTab() {
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [showModal, setShowModal] = useState(false);
  const [editItem, setEditItem] = useState<ArticleRepair | null>(null);
  const [subjectLabel, setSubjectLabel] = useState('');
  const [form, setForm] = useState<RepairForm>({ ...emptyRepairForm });

  const { data: articles } = useArticles();

  // Load open defects for linking (filtered by selected article/vehicle/sonstige)
  const isArticleSelected = form.articleId && !form.articleId.startsWith('vehicle_') && form.articleId !== 'sonstige';
  const defectFilterArticleId = isArticleSelected ? parseInt(form.articleId) : undefined;
  const defectFilterSubject = !isArticleSelected && form.articleId ? subjectLabel || (form.articleId === 'sonstige' ? 'Sonstige' : '') : undefined;

  const { data: openDefectsData } = useQuery({
    queryKey: ['defects', 'open-for-link', defectFilterArticleId, defectFilterSubject],
    queryFn: async () => {
      const res = await defectsApi.getAll({ limit: 1000, status: 'open', articleId: defectFilterArticleId });
      let defects = res.data.data || [];
      if (defectFilterSubject) {
        defects = defects.filter(d => d.subject === defectFilterSubject);
      }
      return defects;
    },
    enabled: !!form.articleId,
  });

  const { data, isLoading } = useQuery({
    queryKey: ['repairs', page],
    queryFn: () =>
      repairsApi
        .getAll({ page, limit: 20 })
        .then((r) => r.data),
  });

  const repairs = data?.data || [];
  const pagination = data?.pagination;

  const saveMutation = useMutation({
    mutationFn: () => {
      const isArticle = form.articleId && !form.articleId.startsWith('vehicle_') && form.articleId !== 'sonstige';
      const payload: Record<string, unknown> = {
        articleId: isArticle ? parseInt(form.articleId) : null,
        subject: !isArticle ? (subjectLabel || (form.articleId === 'sonstige' ? 'Sonstige' : form.articleId)) : null,
        repairedAt: form.repairedAt,
        repairedBy: form.repairedBy,
        description: form.description,
        cost: form.cost ? parseFloat(form.cost) : undefined,
        notes: form.notes || undefined,
        defectId: form.defectId ? parseInt(form.defectId) : undefined,
      };

      if (editItem) {
        return repairsApi.update(editItem.id, payload);
      }
      return repairsApi.create(payload as {
        articleId: number;
        repairedAt: string;
        repairedBy: string;
        description: string;
        cost?: number;
        notes?: string;
        defectId?: number;
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['repairs'] });
      closeModal();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => repairsApi.delete(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['repairs'] }),
  });

  function openCreate() {
    setEditItem(null);
    setForm({ ...emptyRepairForm, repairedAt: new Date().toISOString().slice(0, 10) });
    setShowModal(true);
  }

  function openEdit(item: ArticleRepair) {
    setEditItem(item);
    setForm({
      articleId: String(item.articleId),
      repairedAt: item.repairedAt ? item.repairedAt.slice(0, 10) : '',
      repairedBy: item.repairedBy,
      description: item.description,
      cost: item.cost != null ? String(item.cost) : '',
      notes: item.notes || '',
      defectId: item.defectId != null ? String(item.defectId) : '',
    });
    setShowModal(true);
  }

  function closeModal() {
    setShowModal(false);
    setEditItem(null);
    setForm({ ...emptyRepairForm });
  }

  function articleLabel(repair: ArticleRepair): string {
    if (repair.article) {
      const inv = repair.article.inventoryNumber ? ` (${repair.article.inventoryNumber})` : '';
      return `${repair.article.name}${inv}`;
    }
    if (repair.subject) return repair.subject;
    return repair.articleId ? `Artikel #${repair.articleId}` : '-';
  }

  const u = (k: keyof RepairForm, v: string) => setForm((f) => ({ ...f, [k]: v }));

  const canSave =
    form.articleId && form.description.trim() && form.repairedBy.trim() && form.repairedAt;


  const openDefects = openDefectsData || [];
  const defectOptions = openDefects.map((d) => {
    const artName = d.article?.name || d.subject || '';
    const desc = d.description.length > 40 ? d.description.slice(0, 40) + '...' : d.description;
    return { value: d.id, label: artName ? `${artName}: ${desc}` : `#${d.id} - ${desc}` };
  });

  return (
    <>
      <div className="flex justify-end mb-4">
        <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>
          Neue Reparatur
        </Button>
      </div>

      <Card>
        <Table
          columns={[
            {
              key: 'repairedAt',
              header: 'Datum',
              render: (r: ArticleRepair) => formatDate(r.repairedAt),
            },
            {
              key: 'article',
              header: 'Artikel',
              render: (r: ArticleRepair) => articleLabel(r),
            },
            {
              key: 'description',
              header: 'Beschreibung',
              render: (r: ArticleRepair) =>
                r.description.length > 80
                  ? `${r.description.slice(0, 80)}...`
                  : r.description,
            },
            {
              key: 'cost',
              header: 'Kosten',
              render: (r: ArticleRepair) => formatCurrency(r.cost),
            },
            {
              key: 'repairedBy',
              header: 'Durchgeführt von',
            },
            {
              key: 'actions',
              header: 'Aktionen',
              render: (r: ArticleRepair) => (
                <div className="flex gap-2">
                  <Button size="sm" variant="secondary" onClick={() => openEdit(r)}>
                    Bearbeiten
                  </Button>
                  <Button size="sm" variant="danger" onClick={() => deleteMutation.mutate(r.id)}>
                    Löschen
                  </Button>
                </div>
              ),
            },
          ]}
          data={repairs}
          loading={isLoading}
          emptyMessage="Keine Reparaturen vorhanden."
          keyExtractor={(r) => r.id}
        />
        {pagination && pagination.pages > 1 && (
          <Pagination
            page={pagination.page}
            pages={pagination.pages}
            total={pagination.total}
            limit={pagination.limit}
            onPageChange={setPage}
          />
        )}
      </Card>

      {/* Create / Edit Modal */}
      <Modal
        isOpen={showModal}
        onClose={closeModal}
        title={editItem ? 'Reparatur bearbeiten' : 'Neue Reparatur'}
        size="lg"
        footer={
          <>
            <Button variant="secondary" onClick={closeModal}>
              Abbrechen
            </Button>
            <Button
              variant="primary"
              onClick={() => saveMutation.mutate()}
              loading={saveMutation.isPending}
              disabled={!canSave}
            >
              Speichern
            </Button>
          </>
        }
      >
        <div className="space-y-4">
          <ArticleSelector value={form.articleId} onChange={(val, label) => { u('articleId', val); if (label) setSubjectLabel(label); else setSubjectLabel(''); }} required />
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Datum"
              type="date"
              value={form.repairedAt}
              onChange={(e) => u('repairedAt', e.target.value)}
              required
            />
            <Input
              label="Durchgeführt von"
              value={form.repairedBy}
              onChange={(e) => u('repairedBy', e.target.value)}
              required
            />
          </div>
          <Textarea
            label="Beschreibung"
            value={form.description}
            onChange={(e) => u('description', e.target.value)}
            required
          />
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <Input
              label="Kosten (EUR)"
              type="number"
              value={form.cost}
              onChange={(e) => u('cost', e.target.value)}
              min="0"
              step="0.01"
            />
            <Select
              label="Verknüpfter Mangel"
              value={form.defectId}
              onChange={(e) => u('defectId', e.target.value)}
              options={defectOptions}
              placeholder="Keiner"
            />
          </div>
          <Textarea
            label="Bemerkungen"
            value={form.notes}
            onChange={(e) => u('notes', e.target.value)}
          />
        </div>
      </Modal>
    </>
  );
}
