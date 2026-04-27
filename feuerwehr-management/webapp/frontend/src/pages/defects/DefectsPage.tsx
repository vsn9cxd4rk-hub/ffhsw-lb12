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
import { ArticleDefect, ArticleRepair, Article } from '../../types';
import { formatDate, formatCurrency } from '../../utils/format';

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

// =====================
// Defects Tab
// =====================

function DefectsTab() {
  const queryClient = useQueryClient();
  const [page, setPage] = useState(1);
  const [statusFilter, setStatusFilter] = useState('');
  const [severityFilter, setSeverityFilter] = useState('');
  const [showModal, setShowModal] = useState(false);
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
      const payload: Record<string, unknown> = {
        articleId: parseInt(form.articleId),
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
    return `Artikel #${defect.articleId}`;
  }

  const u = (k: keyof DefectForm, v: string) => setForm((f) => ({ ...f, [k]: v }));

  const canSave =
    form.articleId && form.description.trim() && form.reportedBy.trim() && form.reportedAt;

  const articleOptions = (articles || []).map((a) => ({
    value: a.id,
    label: a.inventoryNumber ? `${a.name} (${a.inventoryNumber})` : a.name,
  }));

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
          <Select
            label="Artikel"
            value={form.articleId}
            onChange={(e) => u('articleId', e.target.value)}
            options={articleOptions}
            placeholder="Artikel auswählen..."
            required
          />
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
  const [form, setForm] = useState<RepairForm>({ ...emptyRepairForm });

  const { data: articles } = useArticles();

  // Load open defects for linking
  const { data: openDefectsData } = useQuery({
    queryKey: ['defects', 'open-for-link'],
    queryFn: () =>
      defectsApi
        .getAll({ limit: 1000, status: 'open' })
        .then((r) => r.data.data),
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
      const payload: Record<string, unknown> = {
        articleId: parseInt(form.articleId),
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
    return `Artikel #${repair.articleId}`;
  }

  const u = (k: keyof RepairForm, v: string) => setForm((f) => ({ ...f, [k]: v }));

  const canSave =
    form.articleId && form.description.trim() && form.repairedBy.trim() && form.repairedAt;

  const articleOptions = (articles || []).map((a) => ({
    value: a.id,
    label: a.inventoryNumber ? `${a.name} (${a.inventoryNumber})` : a.name,
  }));

  const openDefects = openDefectsData || [];
  const defectOptions = openDefects.map((d) => ({
    value: d.id,
    label: `#${d.id} - ${d.description.length > 50 ? d.description.slice(0, 50) + '...' : d.description}`,
  }));

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
          <Select
            label="Artikel"
            value={form.articleId}
            onChange={(e) => u('articleId', e.target.value)}
            options={articleOptions}
            placeholder="Artikel auswählen..."
            required
          />
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
