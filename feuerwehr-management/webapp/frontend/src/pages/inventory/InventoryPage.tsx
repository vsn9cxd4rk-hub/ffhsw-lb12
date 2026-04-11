<<<<<<< HEAD
import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { Article, Warehouse } from '../../types';
=======
import React, { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { Article, Warehouse, DeviceClass } from '../../types';
>>>>>>> a9dc7840 (Added New FW Management system)
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { SearchInput } from '../../components/ui/SearchInput';
import { Pagination } from '../../components/ui/Pagination';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
<<<<<<< HEAD
import { formatCurrency } from '../../utils/format';
import client from '../../api/client';
=======
import { Badge } from '../../components/ui/Badge';
import { formatCurrency } from '../../utils/format';
import client from '../../api/client';
import { settingsApi } from '../../api/settings';
>>>>>>> a9dc7840 (Added New FW Management system)

const inventoryApiWrapper = {
  getWarehouses: (params?: Record<string, unknown>) => client.get('/inventory/warehouses', { params }),
  createWarehouse: (data: Partial<Warehouse>) => client.post('/inventory/warehouses', data),
  getArticles: (params?: Record<string, unknown>) => client.get('/inventory/articles', { params }),
<<<<<<< HEAD
  createArticle: (data: Partial<Article>) => client.post('/inventory/articles', data),
  updateArticle: (id: number, data: Partial<Article>) => client.put(`/inventory/articles/${id}`, data),
};

function ArticleFormModal({ isOpen, onClose, warehouses, article }: { isOpen: boolean; onClose: () => void; warehouses: Warehouse[]; article?: Article }) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    name: article?.name || '',
    manufacturer: article?.manufacturer || '',
    articleType: article?.articleType || '',
    description: article?.description || '',
    inspectionInterval: article?.inspectionInterval?.toString() || '',
    value: article?.value?.toString() || '',
    inventoryNumber: article?.inventoryNumber || '',
    warehouseId: article?.warehouseId?.toString() || '',
  });
  const [error, setError] = useState('');

  const mutation = useMutation({
    mutationFn: () => {
      const data = {
        name: form.name,
        manufacturer: form.manufacturer || undefined,
        articleType: form.articleType || undefined,
        description: form.description || undefined,
=======
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
  });
  const [error, setError] = useState('');

  useEffect(() => {
    if (isOpen) {
      const dcId = article?.deviceSubclass?.deviceClassId?.toString() || '';
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
>>>>>>> a9dc7840 (Added New FW Management system)
        inspectionInterval: form.inspectionInterval ? parseInt(form.inspectionInterval) : null,
        value: form.value ? parseFloat(form.value) : null,
        inventoryNumber: form.inventoryNumber || null,
        warehouseId: form.warehouseId ? parseInt(form.warehouseId) : null,
<<<<<<< HEAD
      } as Partial<Article>;
=======
        deviceSubclassId: form.deviceSubclassId ? parseInt(form.deviceSubclassId) : null,
        manufacturingDate: form.manufacturingDate || null,
        specification: form.specification || null,
        serialNumber: form.serialNumber || null,
        din: form.din || null,
        isDecommissioned: form.isDecommissioned,
      };
>>>>>>> a9dc7840 (Added New FW Management system)
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

<<<<<<< HEAD
  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));
=======
  const u = (f: string, v: string | boolean) => setForm(p => ({ ...p, [f]: v }));

  const selectedClass = deviceClasses.find(dc => dc.id === parseInt(form.deviceClassId));
  const subclasses = selectedClass?.subclasses || [];
>>>>>>> a9dc7840 (Added New FW Management system)

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
<<<<<<< HEAD
=======
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
>>>>>>> a9dc7840 (Added New FW Management system)
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
<<<<<<< HEAD
                {fixedWarehouses.map(w => (
                  <option key={w.id} value={w.id}>{w.name}</option>
                ))}
=======
                {fixedWarehouses.map(w => <option key={w.id} value={w.id}>{w.name}</option>)}
>>>>>>> a9dc7840 (Added New FW Management system)
              </optgroup>
            )}
            {vehicleWarehouses.length > 0 && (
              <optgroup label="Fahrzeuge">
<<<<<<< HEAD
                {vehicleWarehouses.map(w => (
                  <option key={w.id} value={w.id}>{w.name}</option>
                ))}
=======
                {vehicleWarehouses.map(w => <option key={w.id} value={w.id}>{w.name}</option>)}
>>>>>>> a9dc7840 (Added New FW Management system)
              </optgroup>
            )}
          </select>
        </div>
<<<<<<< HEAD
        <div className="grid grid-cols-2 gap-3">
          <Input label="Prüfintervall (Monate)" value={form.inspectionInterval} onChange={(e) => u('inspectionInterval', e.target.value)} type="number" />
          <Input label="Wert (€)" value={form.value} onChange={(e) => u('value', e.target.value)} type="number" step="0.01" />
        </div>
        <Textarea label="Beschreibung" value={form.description} onChange={(e) => u('description', e.target.value)} rows={2} />
=======
        <div className="grid grid-cols-3 gap-3">
          <Input label="Herstellerdatum" value={form.manufacturingDate} onChange={(e) => u('manufacturingDate', e.target.value)} type="date" />
          <Input label="Seriennummer" value={form.serialNumber} onChange={(e) => u('serialNumber', e.target.value)} />
          <Input label="DIN" value={form.din} onChange={(e) => u('din', e.target.value)} />
        </div>
        <div className="grid grid-cols-3 gap-3">
          <Input label="Prüfintervall (Monate)" value={form.inspectionInterval} onChange={(e) => u('inspectionInterval', e.target.value)} type="number" />
          <Input label="Wert (€)" value={form.value} onChange={(e) => u('value', e.target.value)} type="number" step="0.01" />
          <Input label="Spezifikation" value={form.specification} onChange={(e) => u('specification', e.target.value)} />
        </div>
        <Textarea label="Beschreibung" value={form.description} onChange={(e) => u('description', e.target.value)} rows={2} />
        <div className="flex items-center gap-2 pt-1">
          <input type="checkbox" id="isDecommissioned" checked={form.isDecommissioned}
            onChange={(e) => u('isDecommissioned', e.target.checked)}
            className="h-4 w-4 text-primary-600 focus:ring-primary-500 border-gray-300 rounded" />
          <label htmlFor="isDecommissioned" className="text-sm text-gray-700">Außer Dienst gestellt / Ausgesondert</label>
        </div>
>>>>>>> a9dc7840 (Added New FW Management system)
      </div>
    </Modal>
  );
}

export function InventoryPage() {
  const [search, setSearch] = useState('');
  const [selectedWarehouse, setSelectedWarehouse] = useState<number | null>(null);
<<<<<<< HEAD
=======
  const [selectedDeviceClass, setSelectedDeviceClass] = useState<number | null>(null);
>>>>>>> a9dc7840 (Added New FW Management system)
  const [page, setPage] = useState(1);
  const [showForm, setShowForm] = useState(false);
  const [editArticle, setEditArticle] = useState<Article | undefined>();

  const { data: warehouses } = useQuery({
    queryKey: ['warehouses'],
    queryFn: () => inventoryApiWrapper.getWarehouses().then(r => r.data.data),
  });

<<<<<<< HEAD
  const { data, isLoading } = useQuery({
    queryKey: ['articles', search, selectedWarehouse, page],
    queryFn: () => inventoryApiWrapper.getArticles({
      search: search || undefined,
      warehouseId: selectedWarehouse || undefined,
=======
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
>>>>>>> a9dc7840 (Added New FW Management system)
      page, limit: 20,
    }).then(r => r.data),
  });

  const openCreate = () => { setEditArticle(undefined); setShowForm(true); };
  const openEdit = (article: Article) => { setEditArticle(article); setShowForm(true); };
  const closeForm = () => { setShowForm(false); setEditArticle(undefined); };

  const columns = [
    { key: 'inventoryNumber', header: 'Inv.-Nr.', render: (a: Article) => a.inventoryNumber || '-' },
    { key: 'name', header: 'Bezeichnung', render: (a: Article) => (
<<<<<<< HEAD
      <div><p className="font-medium">{a.name}</p>{a.manufacturer && <p className="text-xs text-gray-500">{a.manufacturer}</p>}</div>
    )},
    { key: 'articleType', header: 'Typ', render: (a: Article) => a.articleType || '-' },
    { key: 'warehouse', header: 'Lagerort', render: (a: Article) => a.warehouse?.name || '-' },
    { key: 'inspectionInterval', header: 'Prüfintervall', render: (a: Article) => a.inspectionInterval ? `${a.inspectionInterval} Monate` : '-' },
=======
      <div>
        <p className={`font-medium ${a.isDecommissioned ? 'text-gray-400 line-through' : ''}`}>{a.name}</p>
        {a.manufacturer && <p className="text-xs text-gray-500">{a.manufacturer}</p>}
      </div>
    )},
    { key: 'deviceClass', header: 'Geräteklasse', render: (a: Article) => {
      const dc = a.deviceSubclass?.deviceClass;
      return dc ? <span className="text-sm">{dc.name}</span> : <span className="text-gray-400">-</span>;
    }},
    { key: 'warehouse', header: 'Lagerort', render: (a: Article) => a.warehouse?.name || '-' },
    { key: 'inspectionInterval', header: 'Prüfintervall', render: (a: Article) => a.inspectionInterval ? `${a.inspectionInterval} Monate` : '-' },
    { key: 'status', header: 'Status', render: (a: Article) =>
      a.isDecommissioned ? <Badge variant="default">Außer Dienst</Badge> : null
    },
>>>>>>> a9dc7840 (Added New FW Management system)
    { key: 'value', header: 'Wert', render: (a: Article) => formatCurrency(a.value) },
  ];

  return (
    <div className="space-y-4">
      <div className="flex gap-4">
<<<<<<< HEAD
        {/* Warehouse filter sidebar */}
        <div className="w-48 flex-shrink-0">
=======
        {/* Filter sidebar */}
        <div className="w-48 flex-shrink-0 space-y-4">
>>>>>>> a9dc7840 (Added New FW Management system)
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
<<<<<<< HEAD
=======

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
>>>>>>> a9dc7840 (Added New FW Management system)
        </div>

        {/* Articles table */}
        <div className="flex-1 space-y-4">
          <div className="flex items-center justify-between">
            <SearchInput value={search} onChange={setSearch} placeholder="Artikel suchen..." className="w-64" />
            <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neuer Artikel</Button>
          </div>

          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <Table
              columns={columns}
              data={data?.data || []}
              loading={isLoading}
              emptyMessage="Keine Artikel gefunden."
              keyExtractor={(a) => a.id}
              onRowClick={(a) => openEdit(a as Article)}
            />
            {data?.pagination && <Pagination {...data.pagination} onPageChange={setPage} />}
          </div>
        </div>
      </div>

<<<<<<< HEAD
      <ArticleFormModal isOpen={showForm} onClose={closeForm} warehouses={warehouses || []} article={editArticle} />
=======
      <ArticleFormModal isOpen={showForm} onClose={closeForm} warehouses={warehouses || []} article={editArticle} deviceClasses={deviceClasses} />
>>>>>>> a9dc7840 (Added New FW Management system)
    </div>
  );
}
