import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon, ArrowDownTrayIcon, PencilIcon, ClipboardDocumentCheckIcon } from '@heroicons/react/24/outline';
import { Article, Warehouse, DeviceClass } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { SearchInput } from '../../components/ui/SearchInput';
import { Pagination } from '../../components/ui/Pagination';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { Badge } from '../../components/ui/Badge';
import { formatCurrency } from '../../utils/format';
import client from '../../api/client';
import { settingsApi } from '../../api/settings';
import { exportInventoryCsv, exportInventoryPdf } from '../../utils/inventoryExport';
import { CriteriaInspectionModal } from '../../components/inspections/CriteriaInspectionModal';

const inventoryApiWrapper = {
  getWarehouses: (params?: Record<string, unknown>) => client.get('/inventory/warehouses', { params }),
  createWarehouse: (data: Partial<Warehouse>) => client.post('/inventory/warehouses', data),
  getArticles: (params?: Record<string, unknown>) => client.get('/inventory/articles', { params }),
  createArticle: (data: Record<string, unknown>) => client.post('/inventory/articles', data),
  updateArticle: (id: number, data: Record<string, unknown>) => client.put(`/inventory/articles/${id}`, data),
};

function ArticleFormModal({ isOpen, onClose, warehouses, article, deviceClasses }: {
  isOpen: boolean; onClose: () => void; warehouses: Warehouse[]; article?: Article; deviceClasses: DeviceClass[];
}) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    name: '',
    manufacturer: '',
    articleType: '',
    description: '',
    inspectionInterval: '',
    value: '',
    inventoryNumber: '',
    warehouseId: '',
    deviceClassId: '',
    deviceSubclassId: '',
    manufacturingDate: '',
    specification: '',
    serialNumber: '',
    din: '',
    isDecommissioned: false,
    designationLB: 'LB12',
    commissionedDate: '',
    decommissionedDate: '',
    communityInventoryNumber: '',
    mpFeuerInventoryNumber: '',
    retirementPeriodMonths: '',
  });
  const [error, setError] = useState('');

  useEffect(() => {
    if (isOpen) {
      const dcId = article?.deviceSubclass?.deviceClassId?.toString() || '';
      if (!article) {
        client.get<{ data: { next: string } }>('/inventory/articles/next-number').then(r => {
          setForm(f => ({ ...f, inventoryNumber: r.data.data.next }));
        }).catch(() => {});
      }
      setForm({
        name: article?.name || '',
        manufacturer: article?.manufacturer || '',
        articleType: article?.articleType || '',
        description: article?.description || '',
        inspectionInterval: article?.inspectionInterval?.toString() || '',
        value: article?.value?.toString() || '',
        inventoryNumber: article?.inventoryNumber || '',
        warehouseId: article?.warehouseId?.toString() || '',
        deviceClassId: dcId,
        deviceSubclassId: article?.deviceSubclassId?.toString() || '',
        manufacturingDate: article?.manufacturingDate ? article.manufacturingDate.split('T')[0] : '',
        specification: article?.specification || '',
        serialNumber: article?.serialNumber || '',
        din: article?.din || '',
        isDecommissioned: article?.isDecommissioned || false,
        designationLB: article?.designationLB || 'LB12',
        commissionedDate: article?.commissionedDate ? article.commissionedDate.split('T')[0] : '',
        decommissionedDate: article?.decommissionedDate ? article.decommissionedDate.split('T')[0] : '',
        communityInventoryNumber: article?.communityInventoryNumber || '',
        mpFeuerInventoryNumber: article?.mpFeuerInventoryNumber || '',
        retirementPeriodMonths: article?.retirementPeriodMonths?.toString() || '',
      });
      setError('');
    }
  }, [isOpen, article]);

  const mutation = useMutation({
    mutationFn: () => {
      const data: Record<string, unknown> = {
        name: form.name,
        manufacturer: form.manufacturer || null,
        articleType: form.articleType || null,
        description: form.description || null,
        inspectionInterval: form.inspectionInterval ? parseInt(form.inspectionInterval) : null,
        value: form.value ? parseFloat(form.value) : null,
        inventoryNumber: form.inventoryNumber || null,
        warehouseId: form.warehouseId ? parseInt(form.warehouseId) : null,
        deviceSubclassId: form.deviceSubclassId ? parseInt(form.deviceSubclassId) : null,
        manufacturingDate: form.manufacturingDate || null,
        specification: form.specification || null,
        serialNumber: form.serialNumber || null,
        din: form.din || null,
        isDecommissioned: form.isDecommissioned,
        designationLB: form.designationLB || 'LB12',
        commissionedDate: form.commissionedDate || null,
        decommissionedDate: form.decommissionedDate || null,
        communityInventoryNumber: form.communityInventoryNumber || null,
        mpFeuerInventoryNumber: form.mpFeuerInventoryNumber || null,
        retirementPeriodMonths: form.retirementPeriodMonths ? parseInt(form.retirementPeriodMonths) : null,
      };
      return article
        ? inventoryApiWrapper.updateArticle(article.id, data)
        : inventoryApiWrapper.createArticle(data);
    },
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['articles'] }); setError(''); onClose(); },
    onError: (err: unknown) => {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen';
      setError(msg);
    },
  });

  const u = (f: string, v: string | boolean) => setForm(p => ({ ...p, [f]: v }));

  const selectedClass = deviceClasses.find(dc => dc.id === parseInt(form.deviceClassId));
  const subclasses = selectedClass?.subclasses || [];

  const fixedWarehouses = warehouses.filter(w => !w.vehicleId);
  const vehicleWarehouses = warehouses.filter(w => w.vehicleId);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={article ? 'Artikel bearbeiten' : 'Neuer Artikel'} size="lg"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.name}>Speichern</Button></>}
    >
      <div className="space-y-3">
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>
        )}
        <div className="grid grid-cols-2 gap-3">
          <Input label="Bezeichnung" value={form.name} onChange={(e) => u('name', e.target.value)} required />
          <Input label="Inventarnummer" value={form.inventoryNumber} onChange={(e) => u('inventoryNumber', e.target.value)} />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <Input label="Hersteller" value={form.manufacturer} onChange={(e) => u('manufacturer', e.target.value)} />
          <Input label="Typ" value={form.articleType} onChange={(e) => u('articleType', e.target.value)} />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Geräteklasse</label>
            <select value={form.deviceClassId}
              onChange={(e) => { u('deviceClassId', e.target.value); u('deviceSubclassId', ''); }}
              className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
              <option value="">-- Keine --</option>
              {deviceClasses.map(dc => <option key={dc.id} value={dc.id}>{dc.name}</option>)}
            </select>
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Unterklasse</label>
            <select value={form.deviceSubclassId}
              onChange={(e) => u('deviceSubclassId', e.target.value)}
              disabled={!form.deviceClassId}
              className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white disabled:bg-gray-100">
              <option value="">-- Keine --</option>
              {subclasses.map(sc => <option key={sc.id} value={sc.id}>{sc.name}</option>)}
            </select>
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Lagerort</label>
          <select
            value={form.warehouseId}
            onChange={(e) => u('warehouseId', e.target.value)}
            className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white"
          >
            <option value="">-- Kein Lagerort --</option>
            {fixedWarehouses.length > 0 && (
              <optgroup label="Lagerorte">
                {fixedWarehouses.map(w => <option key={w.id} value={w.id}>{w.name}</option>)}
              </optgroup>
            )}
            {vehicleWarehouses.length > 0 && (
              <optgroup label="Fahrzeuge">
                {vehicleWarehouses.map(w => <option key={w.id} value={w.id}>{w.name}</option>)}
              </optgroup>
            )}
          </select>
        </div>
        <div className="grid grid-cols-3 gap-3">
          <Input label="Herstellerdatum" value={form.manufacturingDate} onChange={(e) => u('manufacturingDate', e.target.value)} type="date" />
          <Input label="Seriennummer" value={form.serialNumber} onChange={(e) => u('serialNumber', e.target.value)} />
          <Input label="DIN" value={form.din} onChange={(e) => u('din', e.target.value)} />
        </div>
        <div className="grid grid-cols-3 gap-3">
          <Input label="Bezeichnung LB" value={form.designationLB} onChange={(e) => u('designationLB', e.target.value)} />
          <Input label="Inv.-Nr. Gemeinde (DOPPIK)" value={form.communityInventoryNumber} onChange={(e) => u('communityInventoryNumber', e.target.value)} />
          <Input label="Inv.-Nr. MP Feuer" value={form.mpFeuerInventoryNumber} onChange={(e) => u('mpFeuerInventoryNumber', e.target.value)} />
        </div>
        <div className="grid grid-cols-3 gap-3">
          <Input label="Indienststellung" value={form.commissionedDate} onChange={(e) => u('commissionedDate', e.target.value)} type="date" />
          <Input label="Außerdienststellung" value={form.decommissionedDate} onChange={(e) => u('decommissionedDate', e.target.value)} type="date" />
          <Input label="Aussonderungsfrist (Monate)" value={form.retirementPeriodMonths} onChange={(e) => u('retirementPeriodMonths', e.target.value)} type="number" />
        </div>
        <div className="grid grid-cols-3 gap-3">
          <Input label="Prüfintervall (Monate)" value={form.inspectionInterval} onChange={(e) => u('inspectionInterval', e.target.value)} type="number" />
          <Input label="Wert (€)" value={form.value} onChange={(e) => u('value', e.target.value)} type="number" step="0.01" />
          <Input label="Spezifikation" value={form.specification} onChange={(e) => u('specification', e.target.value)} />
        </div>
        <Textarea label="Beschreibung" value={form.description} onChange={(e) => u('description', e.target.value)} rows={2} />
      </div>
    </Modal>
  );
}

export function InventoryPage() {
  const navigate = useNavigate();
  const [search, setSearch] = useState('');
  const [selectedWarehouse, setSelectedWarehouse] = useState<number | null>(null);
  const [selectedDeviceClass, setSelectedDeviceClass] = useState<number | null>(null);
  const [page, setPage] = useState(1);
  const [showForm, setShowForm] = useState(false);
  const [editArticle, setEditArticle] = useState<Article | undefined>();
  const [showInspection, setShowInspection] = useState(false);
  const [inspectArticle, setInspectArticle] = useState<Article | undefined>();

  const { data: warehouses } = useQuery({
    queryKey: ['warehouses'],
    queryFn: () => inventoryApiWrapper.getWarehouses().then(r => r.data.data),
  });

  const { data: deviceClassesRes } = useQuery({
    queryKey: ['device-classes'],
    queryFn: () => settingsApi.getDeviceClasses(),
  });
  const deviceClasses: DeviceClass[] = deviceClassesRes?.data?.data || [];

  const { data, isLoading } = useQuery({
    queryKey: ['articles', search, selectedWarehouse, selectedDeviceClass, page],
    queryFn: () => inventoryApiWrapper.getArticles({
      search: search || undefined,
      warehouseId: selectedWarehouse || undefined,
      deviceClassId: selectedDeviceClass || undefined,
      page, limit: 20,
    }).then(r => r.data),
  });

  const openCreate = () => { setEditArticle(undefined); setShowForm(true); };
  const openEdit = (article: Article) => { setEditArticle(article); setShowForm(true); };
  const closeForm = () => { setShowForm(false); setEditArticle(undefined); };

  const columns = [
    { key: 'inventoryNumber', header: 'Inv.-Nr.', sortable: true, sortValue: (a: Article) => a.inventoryNumber || '', render: (a: Article) => a.inventoryNumber || '-' },
    { key: 'name', header: 'Bezeichnung', sortable: true, sortValue: (a: Article) => a.name, render: (a: Article) => (
      <div>
        <p className={`font-medium ${a.isDecommissioned ? 'text-gray-400 line-through' : ''}`}>{a.name}</p>
        {a.manufacturer && <p className="text-xs text-gray-500">{a.manufacturer}</p>}
      </div>
    )},
    { key: 'deviceClass', header: 'Geräteklasse', sortable: true, sortValue: (a: Article) => a.deviceSubclass?.deviceClass?.name || '', render: (a: Article) => {
      const dc = a.deviceSubclass?.deviceClass;
      return dc ? <span className="text-sm">{dc.name}</span> : <span className="text-gray-400">-</span>;
    }},
    { key: 'warehouse', header: 'Lagerort', sortable: true, sortValue: (a: Article) => a.warehouse?.name || '', render: (a: Article) => a.warehouse?.name || '-' },
    { key: 'inspectionInterval', header: 'Prüfintervall', sortable: true, sortValue: (a: Article) => a.inspectionInterval || 0, render: (a: Article) => a.inspectionInterval ? `${a.inspectionInterval} Monate` : '-' },
    { key: 'status', header: 'Status', render: (a: Article) =>
      a.isDecommissioned ? <Badge variant="default">Außer Dienst</Badge> : null
    },
    { key: 'value', header: 'Wert', sortable: true, sortValue: (a: Article) => a.value || 0, render: (a: Article) => formatCurrency(a.value) },
    { key: 'actions', header: '', render: (a: Article) => (
      <div className="flex items-center gap-1">
        <button onClick={(e) => { e.stopPropagation(); setInspectArticle(a); setShowInspection(true); }}
          className="p-1.5 text-gray-400 hover:text-green-600 rounded hover:bg-gray-100" title="Prüfung durchführen">
          <ClipboardDocumentCheckIcon className="h-4 w-4" />
        </button>
        <button onClick={(e) => { e.stopPropagation(); openEdit(a); }}
          className="p-1.5 text-gray-400 hover:text-primary-600 rounded hover:bg-gray-100" title="Artikel bearbeiten">
          <PencilIcon className="h-4 w-4" />
        </button>
      </div>
    )},
  ];

  return (
    <div className="space-y-4">
      <div className="flex gap-4">
        {/* Filter sidebar */}
        <div className="w-48 flex-shrink-0 space-y-4">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-3">
            <h3 className="text-xs font-semibold text-gray-500 uppercase mb-2">Lager</h3>
            <ul className="space-y-1">
              <li>
                <button onClick={() => { setSelectedWarehouse(null); setPage(1); }}
                  className={`w-full text-left px-2 py-1.5 rounded text-sm ${!selectedWarehouse ? 'bg-primary-50 text-primary-700 font-medium' : 'text-gray-700 hover:bg-gray-50'}`}>
                  Alle
                </button>
              </li>
              {(warehouses || []).map((w: Warehouse) => (
                <li key={w.id}>
                  <button onClick={() => { setSelectedWarehouse(w.id); setPage(1); }}
                    className={`w-full text-left px-2 py-1.5 rounded text-sm ${selectedWarehouse === w.id ? 'bg-primary-50 text-primary-700 font-medium' : 'text-gray-700 hover:bg-gray-50'}`}>
                    {w.name}
                  </button>
                </li>
              ))}
            </ul>
          </div>

          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-3">
            <h3 className="text-xs font-semibold text-gray-500 uppercase mb-2">Geräteklasse</h3>
            <ul className="space-y-1">
              <li>
                <button onClick={() => { setSelectedDeviceClass(null); setPage(1); }}
                  className={`w-full text-left px-2 py-1.5 rounded text-sm ${!selectedDeviceClass ? 'bg-primary-50 text-primary-700 font-medium' : 'text-gray-700 hover:bg-gray-50'}`}>
                  Alle
                </button>
              </li>
              {deviceClasses.map(dc => (
                <li key={dc.id}>
                  <button onClick={() => { setSelectedDeviceClass(dc.id); setPage(1); }}
                    className={`w-full text-left px-2 py-1.5 rounded text-sm truncate ${selectedDeviceClass === dc.id ? 'bg-primary-50 text-primary-700 font-medium' : 'text-gray-700 hover:bg-gray-50'}`}>
                    {dc.name}
                  </button>
                </li>
              ))}
            </ul>
          </div>
        </div>

        {/* Articles table */}
        <div className="flex-1 space-y-4">
          <div className="flex items-center justify-between">
            <SearchInput value={search} onChange={setSearch} placeholder="Artikel suchen..." className="w-64" />
            <div className="flex items-center gap-2">
              <Button variant="secondary" size="sm" icon={<ArrowDownTrayIcon />}
                onClick={() => data?.data && exportInventoryCsv(data.data)} disabled={!data?.data?.length}>
                CSV
              </Button>
              <Button variant="secondary" size="sm" icon={<ArrowDownTrayIcon />}
                onClick={() => data?.data && exportInventoryPdf(data.data, 'Gesamtübersicht')} disabled={!data?.data?.length}>
                PDF
              </Button>
              <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neuer Artikel</Button>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <Table
              columns={columns}
              data={data?.data || []}
              loading={isLoading}
              emptyMessage="Keine Artikel gefunden."
              keyExtractor={(a) => a.id}
              onRowClick={(a) => navigate(`/inventory/${(a as Article).id}`)}
            />
            {data?.pagination && <Pagination {...data.pagination} onPageChange={setPage} />}
          </div>
        </div>
      </div>

      <ArticleFormModal isOpen={showForm} onClose={closeForm} warehouses={warehouses || []} article={editArticle} deviceClasses={deviceClasses} />

      <CriteriaInspectionModal
        isOpen={showInspection}
        onClose={() => { setShowInspection(false); setInspectArticle(undefined); }}
        preselectedArticle={inspectArticle}
        articles={data?.data || []}
      />
    </div>
  );
}
