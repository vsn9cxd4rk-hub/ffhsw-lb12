import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { Article, Warehouse } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { SearchInput } from '../../components/ui/SearchInput';
import { Pagination } from '../../components/ui/Pagination';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { formatCurrency } from '../../utils/format';
import client from '../../api/client';

const inventoryApiWrapper = {
  getWarehouses: (params?: Record<string, unknown>) => client.get('/inventory/warehouses', { params }),
  createWarehouse: (data: Partial<Warehouse>) => client.post('/inventory/warehouses', data),
  getArticles: (params?: Record<string, unknown>) => client.get('/inventory/articles', { params }),
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
        inspectionInterval: form.inspectionInterval ? parseInt(form.inspectionInterval) : null,
        value: form.value ? parseFloat(form.value) : null,
        inventoryNumber: form.inventoryNumber || null,
        warehouseId: form.warehouseId ? parseInt(form.warehouseId) : null,
      } as Partial<Article>;
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

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

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
                {fixedWarehouses.map(w => (
                  <option key={w.id} value={w.id}>{w.name}</option>
                ))}
              </optgroup>
            )}
            {vehicleWarehouses.length > 0 && (
              <optgroup label="Fahrzeuge">
                {vehicleWarehouses.map(w => (
                  <option key={w.id} value={w.id}>{w.name}</option>
                ))}
              </optgroup>
            )}
          </select>
        </div>
        <div className="grid grid-cols-2 gap-3">
          <Input label="Prüfintervall (Monate)" value={form.inspectionInterval} onChange={(e) => u('inspectionInterval', e.target.value)} type="number" />
          <Input label="Wert (€)" value={form.value} onChange={(e) => u('value', e.target.value)} type="number" step="0.01" />
        </div>
        <Textarea label="Beschreibung" value={form.description} onChange={(e) => u('description', e.target.value)} rows={2} />
      </div>
    </Modal>
  );
}

export function InventoryPage() {
  const [search, setSearch] = useState('');
  const [selectedWarehouse, setSelectedWarehouse] = useState<number | null>(null);
  const [page, setPage] = useState(1);
  const [showForm, setShowForm] = useState(false);
  const [editArticle, setEditArticle] = useState<Article | undefined>();

  const { data: warehouses } = useQuery({
    queryKey: ['warehouses'],
    queryFn: () => inventoryApiWrapper.getWarehouses().then(r => r.data.data),
  });

  const { data, isLoading } = useQuery({
    queryKey: ['articles', search, selectedWarehouse, page],
    queryFn: () => inventoryApiWrapper.getArticles({
      search: search || undefined,
      warehouseId: selectedWarehouse || undefined,
      page, limit: 20,
    }).then(r => r.data),
  });

  const openCreate = () => { setEditArticle(undefined); setShowForm(true); };
  const openEdit = (article: Article) => { setEditArticle(article); setShowForm(true); };
  const closeForm = () => { setShowForm(false); setEditArticle(undefined); };

  const columns = [
    { key: 'inventoryNumber', header: 'Inv.-Nr.', render: (a: Article) => a.inventoryNumber || '-' },
    { key: 'name', header: 'Bezeichnung', render: (a: Article) => (
      <div><p className="font-medium">{a.name}</p>{a.manufacturer && <p className="text-xs text-gray-500">{a.manufacturer}</p>}</div>
    )},
    { key: 'articleType', header: 'Typ', render: (a: Article) => a.articleType || '-' },
    { key: 'warehouse', header: 'Lagerort', render: (a: Article) => a.warehouse?.name || '-' },
    { key: 'inspectionInterval', header: 'Prüfintervall', render: (a: Article) => a.inspectionInterval ? `${a.inspectionInterval} Monate` : '-' },
    { key: 'value', header: 'Wert', render: (a: Article) => formatCurrency(a.value) },
  ];

  return (
    <div className="space-y-4">
      <div className="flex gap-4">
        {/* Warehouse filter sidebar */}
        <div className="w-48 flex-shrink-0">
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

      <ArticleFormModal isOpen={showForm} onClose={closeForm} warehouses={warehouses || []} article={editArticle} />
    </div>
  );
}
