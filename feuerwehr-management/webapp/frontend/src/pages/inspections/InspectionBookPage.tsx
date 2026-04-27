import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { PlusIcon, PencilIcon } from '@heroicons/react/24/outline';
import { Article, ArticleInspection, DeviceClass } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Pagination } from '../../components/ui/Pagination';
import { formatDate } from '../../utils/format';
import { inspectionsApi } from '../../api/inspections';
import { settingsApi } from '../../api/settings';
import { CriteriaInspectionModal } from '../../components/inspections/CriteriaInspectionModal';
import { ReportTab } from '../../components/inspections/ReportTab';
import { SearchInput } from '../../components/ui/SearchInput';
import client from '../../api/client';

type Tab = 'due' | 'history' | 'report';

function DueTab({ onInspect, search }: { onInspect: (article: Article) => void; search: string }) {
  const [deviceClassId, setDeviceClassId] = useState<number | ''>('');
  const [deviceSubclassId, setDeviceSubclassId] = useState<number | ''>('');

  const { data: classesRes } = useQuery({
    queryKey: ['device-classes'],
    queryFn: () => settingsApi.getDeviceClasses(),
  });
  const deviceClasses: DeviceClass[] = classesRes?.data?.data || [];
  const selectedClass = deviceClasses.find(dc => dc.id === deviceClassId);

  const { data: dueArticles, isLoading } = useQuery({
    queryKey: ['due-inspections', deviceClassId, deviceSubclassId, search],
    queryFn: () => inspectionsApi.getDue({
      deviceClassId: deviceClassId || undefined,
      deviceSubclassId: deviceSubclassId || undefined,
      search: search || undefined,
    }).then(r => r.data.data),
  });

  const columns = [
    { key: 'inventoryNumber', header: 'Inv.-Nr.', render: (a: Article) => a.inventoryNumber || '-' },
    { key: 'name', header: 'Artikel', render: (a: Article) => (
      <div><p className="font-medium">{a.name}</p>{a.manufacturer && <p className="text-xs text-gray-500">{a.manufacturer}</p>}</div>
    )},
    { key: 'deviceClass', header: 'Geräteklasse', render: (a: Article) =>
      a.deviceSubclass?.deviceClass?.name || '-'
    },
    { key: 'warehouse', header: 'Lagerort', render: (a: Article) => a.warehouse?.name || '-' },
    { key: 'interval', header: 'Intervall', render: (a: Article) => a.inspectionInterval ? `${a.inspectionInterval} Mon.` : '-' },
    { key: 'lastInspection', header: 'Letzte Prüfung', render: (a: Article) => {
      const inspections = (a as Article & { inspections?: ArticleInspection[] }).inspections;
      return inspections && inspections.length > 0 ? formatDate(inspections[0].inspectedAt) : 'Nie';
    }},
    { key: 'status', header: 'Status', render: (a: Article) => {
      const inspections = (a as Article & { inspections?: ArticleInspection[] }).inspections;
      if (!inspections || inspections.length === 0) return <Badge variant="warning">Nie geprüft</Badge>;
      const last = inspections[0];
      if (last.nextDueDate && new Date(last.nextDueDate) <= new Date()) return <Badge variant="danger">Überfällig</Badge>;
      return <Badge variant="warning">Fällig</Badge>;
    }},
    { key: 'action', header: '', render: (a: Article) => (
      <Button variant="primary" size="sm" onClick={(e: React.MouseEvent) => { e.stopPropagation(); onInspect(a); }}>
        Prüfen
      </Button>
    )},
  ];

  return (
    <div className="space-y-4">
      <div className="flex items-end gap-4 bg-white p-4 rounded-lg border border-gray-200">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Geräteklasse</label>
          <select value={deviceClassId}
            onChange={(e) => { setDeviceClassId(e.target.value ? parseInt(e.target.value) : ''); setDeviceSubclassId(''); }}
            className="block w-56 px-3 py-2 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
            <option value="">Alle Geräteklassen</option>
            {deviceClasses.map(dc => <option key={dc.id} value={dc.id}>{dc.name}</option>)}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Unterklasse</label>
          <select value={deviceSubclassId}
            onChange={(e) => setDeviceSubclassId(e.target.value ? parseInt(e.target.value) : '')}
            disabled={!deviceClassId}
            className="block w-56 px-3 py-2 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white disabled:bg-gray-100">
            <option value="">Alle Unterklassen</option>
            {selectedClass?.subclasses?.map(sc => <option key={sc.id} value={sc.id}>{sc.name}</option>)}
          </select>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table
          columns={columns}
          data={dueArticles || []}
          loading={isLoading}
          emptyMessage="Keine fälligen Prüfungen."
          keyExtractor={(a) => a.id}
        />
      </div>
    </div>
  );
}

function HistoryTab({ search, onEdit }: { search: string; onEdit: (inspection: ArticleInspection) => void }) {
  const [page, setPage] = useState(1);

  const { data, isLoading } = useQuery({
    queryKey: ['inspections-history', page, search],
    queryFn: () => inspectionsApi.getAll({ page, limit: 20, search: search || undefined }).then(r => r.data),
  });

  const columns = [
    { key: 'inspectedAt', header: 'Datum', render: (i: ArticleInspection) => formatDate(i.inspectedAt) },
    { key: 'article', header: 'Artikel', render: (i: ArticleInspection) => (
      <div>
        <p className="font-medium">{i.article?.name}</p>
        {i.article?.inventoryNumber && <p className="text-xs text-gray-500">{i.article.inventoryNumber}</p>}
      </div>
    )},
    { key: 'inspectionType', header: 'Prüfart', render: (i: ArticleInspection) => i.inspectionType?.name || '-' },
    { key: 'deviceClass', header: 'Geräteklasse', render: (i: ArticleInspection) =>
      i.article?.deviceSubclass?.deviceClass?.name || '-'
    },
    { key: 'inspectedBy', header: 'Prüfer' },
    { key: 'result', header: 'Ergebnis', render: (i: ArticleInspection) => (
      <Badge variant={i.result === 'passed' ? 'success' : 'danger'}>
        {i.result === 'passed' ? 'Bestanden' : 'Nicht bestanden'}
      </Badge>
    )},
    { key: 'notes', header: 'Bemerkungen', render: (i: ArticleInspection) => (
      <span className="text-gray-500 text-sm truncate max-w-[200px] block">{i.notes || '-'}</span>
    )},
    { key: 'nextDueDate', header: 'Nächste Prüfung', render: (i: ArticleInspection) => i.nextDueDate ? formatDate(i.nextDueDate) : '-' },
    { key: 'actions', header: '', render: (i: ArticleInspection) => (
      <button onClick={(e) => { e.stopPropagation(); onEdit(i); }}
        className="p-1.5 text-gray-400 hover:text-primary-600 rounded hover:bg-gray-100" title="Prüfung bearbeiten">
        <PencilIcon className="h-4 w-4" />
      </button>
    )},
  ];

  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200">
      <Table
        columns={columns}
        data={data?.data || []}
        loading={isLoading}
        emptyMessage="Keine Prüfungen dokumentiert."
        keyExtractor={(i) => i.id}
      />
      {data?.pagination && <Pagination {...data.pagination} onPageChange={setPage} />}
    </div>
  );
}

export function InspectionBookPage() {
  const [tab, setTab] = useState<Tab>('due');
  const [search, setSearch] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [selectedArticle, setSelectedArticle] = useState<Article | undefined>();
  const [editInspection, setEditInspection] = useState<ArticleInspection | undefined>();

  const { data: dueArticles } = useQuery({
    queryKey: ['due-inspections'],
    queryFn: () => inspectionsApi.getDue().then(r => r.data.data),
  });

  // Also load all articles for the edit modal (it needs the article in the list)
  const { data: allArticlesRes } = useQuery({
    queryKey: ['all-articles-for-inspection'],
    queryFn: () =>
      client.get('/inventory/articles', { params: { limit: 1000 } }).then(r => r.data.data as Article[]),
  });

  const openInspect = (article?: Article) => {
    setSelectedArticle(article);
    setEditInspection(undefined);
    setShowModal(true);
  };

  const openEdit = (inspection: ArticleInspection) => {
    setEditInspection(inspection);
    setSelectedArticle(undefined);
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedArticle(undefined);
    setEditInspection(undefined);
  };

  // Combine due articles and all articles for the modal
  const articlesForModal = editInspection
    ? (allArticlesRes || dueArticles || [])
    : (dueArticles || []);

  const tabs = [
    { id: 'due' as const, label: 'Fällige Prüfungen' },
    { id: 'history' as const, label: 'Prüfhistorie' },
    { id: 'report' as const, label: 'Berichte' },
  ];

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <div className="border-b border-gray-200">
          <nav className="flex gap-4 -mb-px">
            {tabs.map((t) => (
              <button key={t.id} onClick={() => setTab(t.id)}
                className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
                {t.label}
              </button>
            ))}
          </nav>
        </div>
        <div className="flex items-center gap-3">
          <SearchInput
            value={search}
            onChange={setSearch}
            placeholder="Nach Inv.-Nr., Gemeinde-Nr., MP-Feuer-Nr. suchen..."
            className="w-80"
          />
          {tab !== 'report' && (
            <Button variant="primary" icon={<PlusIcon />} onClick={() => openInspect()}>
              Prüfung dokumentieren
            </Button>
          )}
        </div>
      </div>

      {tab === 'due' && <DueTab onInspect={openInspect} search={search} />}
      {tab === 'history' && <HistoryTab search={search} onEdit={openEdit} />}
      {tab === 'report' && <ReportTab />}

      <CriteriaInspectionModal
        isOpen={showModal}
        onClose={closeModal}
        preselectedArticle={selectedArticle}
        editInspection={editInspection}
        articles={articlesForModal}
      />
    </div>
  );
}
