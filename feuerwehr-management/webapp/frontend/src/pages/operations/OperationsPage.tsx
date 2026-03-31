import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { PlusIcon } from '@heroicons/react/24/outline';
import { operationsApi } from '../../api/operations';
import { Operation } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { SearchInput } from '../../components/ui/SearchInput';
import { Pagination } from '../../components/ui/Pagination';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { formatDate } from '../../utils/format';

function OperationCreateModal({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    date: new Date().toISOString().substring(0, 10),
    alarmTime: '',
    location: '',
    keyword: '',
    leaderCount: '0',
    memberCount: '0',
  });

  const mutation = useMutation({
    mutationFn: () => operationsApi.create({
      ...form,
      date: new Date(form.date).toISOString(),
      leaderCount: parseInt(form.leaderCount) || 0,
      memberCount: parseInt(form.memberCount) || 0,
    } as Partial<Operation>),
    onSuccess: (res) => {
      queryClient.invalidateQueries({ queryKey: ['operations'] });
      onClose();
    },
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Neuer Einsatz" size="md"
      footer={
        <>
          <Button variant="secondary" onClick={onClose}>Abbrechen</Button>
          <Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.location || !form.date}>
            Anlegen
          </Button>
        </>
      }
    >
      <div className="space-y-3">
        <Input label="Datum" value={form.date} onChange={(e) => u('date', e.target.value)} type="date" required />
        <Input label="Alarmzeit" value={form.alarmTime} onChange={(e) => u('alarmTime', e.target.value)} type="time" />
        <Input label="Einsatzort" value={form.location} onChange={(e) => u('location', e.target.value)} required />
        <Input label="Stichwort" value={form.keyword} onChange={(e) => u('keyword', e.target.value)} />
        <div className="grid grid-cols-2 gap-3">
          <Input label="GF-Stärke" value={form.leaderCount} onChange={(e) => u('leaderCount', e.target.value)} type="number" />
          <Input label="FM-Stärke" value={form.memberCount} onChange={(e) => u('memberCount', e.target.value)} type="number" />
        </div>
      </div>
    </Modal>
  );
}

export function OperationsPage() {
  const navigate = useNavigate();
  const [search, setSearch] = useState('');
  const [year, setYear] = useState(String(new Date().getFullYear()));
  const [page, setPage] = useState(1);
  const [showCreate, setShowCreate] = useState(false);

  const { data, isLoading } = useQuery({
    queryKey: ['operations', search, year, page],
    queryFn: () => operationsApi.getAll({
      location: search || undefined,
      year: year || undefined,
      page,
      limit: 20,
    }).then((r) => r.data),
  });

  const columns = [
    { key: 'date', header: 'Datum', render: (op: Operation) => formatDate(op.date) },
    { key: 'operationNumber', header: 'Einsatz-Nr.', render: (op: Operation) => op.operationNumber || '-' },
    { key: 'location', header: 'Ort' },
    { key: 'keyword', header: 'Stichwort', render: (op: Operation) => op.keyword || '-' },
    { key: 'alarmTime', header: 'Alarmzeit', render: (op: Operation) => op.alarmTime || '-' },
    { key: 'staerke', header: 'Stärke', render: (op: Operation) =>
      (op.leaderCount + op.memberCount) > 0 ? `${op.leaderCount}/${op.memberCount}` : '-'
    },
  ];

  const years = Array.from({ length: 5 }, (_, i) => String(new Date().getFullYear() - i));

  return (
    <div className="space-y-4">
      <div className="flex flex-col sm:flex-row gap-3 justify-between">
        <div className="flex gap-3">
          <SearchInput value={search} onChange={setSearch} placeholder="Ort oder Stichwort..." className="w-56" />
          <select value={year} onChange={(e) => { setYear(e.target.value); setPage(1); }}
            className="border border-gray-300 rounded-md px-3 py-2 text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500">
            <option value="">Alle Jahre</option>
            {years.map(y => <option key={y} value={y}>{y}</option>)}
          </select>
        </div>
        <Button variant="primary" icon={<PlusIcon />} onClick={() => setShowCreate(true)}>
          Neuer Einsatz
        </Button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table
          columns={columns}
          data={data?.data || []}
          loading={isLoading}
          emptyMessage="Keine Einsätze gefunden."
          onRowClick={(op) => navigate(`/operations/${op.id}`)}
          keyExtractor={(op) => op.id}
        />
        {data?.pagination && (
          <Pagination {...data.pagination} onPageChange={setPage} />
        )}
      </div>

      <OperationCreateModal isOpen={showCreate} onClose={() => setShowCreate(false)} />
    </div>
  );
}
