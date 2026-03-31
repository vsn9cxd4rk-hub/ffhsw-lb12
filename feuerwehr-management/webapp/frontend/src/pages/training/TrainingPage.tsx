import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { trainingApi } from '../../api/training';
import { membersApi } from '../../api/members';
import { Course, Member } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { SearchInput } from '../../components/ui/SearchInput';
import { Pagination } from '../../components/ui/Pagination';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Select } from '../../components/ui/Select';
import { Textarea } from '../../components/ui/Textarea';
import { formatDate, getCourseStatusLabel, getCourseStatusColor } from '../../utils/format';

function CourseCreateModal({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({ memberId: '', categoryId: '', status: 'pending', startDate: '', endDate: '', location: '', notes: '' });

  const { data: categories } = useQuery({ queryKey: ['course-categories'], queryFn: () => trainingApi.getCategories().then(r => r.data.data) });
  const { data: membersData } = useQuery({ queryKey: ['members-search', ''], queryFn: () => membersApi.getAll({ isInactive: false, limit: 100 }).then(r => r.data.data) });

  const mutation = useMutation({
    mutationFn: () => trainingApi.create({ ...form, memberId: parseInt(form.memberId), categoryId: parseInt(form.categoryId), startDate: form.startDate || null, endDate: form.endDate || null }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['courses'] }); onClose(); },
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Neuer Lehrgang" size="lg"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.memberId || !form.categoryId}>Anlegen</Button></>}
    >
      <div className="space-y-3">
        <Select label="Mitglied" value={form.memberId} onChange={(e) => u('memberId', e.target.value)} required
          options={(membersData || []).map((m: Member) => ({ value: m.id, label: `${m.lastName}, ${m.firstName}` }))}
          placeholder="Bitte wählen" />
        <Select label="Lehrgang" value={form.categoryId} onChange={(e) => u('categoryId', e.target.value)} required
          options={(categories || []).map(c => ({ value: c.id, label: c.name }))}
          placeholder="Bitte wählen" />
        <Select label="Status" value={form.status} onChange={(e) => u('status', e.target.value)}
          options={[{ value: 'pending', label: 'Geplant' }, { value: 'active', label: 'Laufend' }, { value: 'completed', label: 'Abgeschlossen' }, { value: 'failed', label: 'Nicht bestanden' }]} />
        <div className="grid grid-cols-2 gap-3">
          <Input label="Beginn" value={form.startDate} onChange={(e) => u('startDate', e.target.value)} type="date" />
          <Input label="Ende" value={form.endDate} onChange={(e) => u('endDate', e.target.value)} type="date" />
        </div>
        <Input label="Ort" value={form.location} onChange={(e) => u('location', e.target.value)} />
        <Textarea label="Notizen" value={form.notes} onChange={(e) => u('notes', e.target.value)} rows={2} />
      </div>
    </Modal>
  );
}

export function TrainingPage() {
  const [search, setSearch] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [status, setStatus] = useState('');
  const [page, setPage] = useState(1);
  const [showCreate, setShowCreate] = useState(false);

  const { data: categories } = useQuery({ queryKey: ['course-categories'], queryFn: () => trainingApi.getCategories().then(r => r.data.data) });

  const { data, isLoading } = useQuery({
    queryKey: ['courses', search, categoryId, status, page],
    queryFn: () => trainingApi.getCourses({ search: search || undefined, categoryId: categoryId || undefined, status: status || undefined, page, limit: 20 }).then(r => r.data),
  });

  const columns = [
    { key: 'member', header: 'Mitglied', render: (c: Course) => c.member ? `${c.member.lastName}, ${c.member.firstName}` : '-' },
    { key: 'category', header: 'Lehrgang', render: (c: Course) => c.category?.name || '-' },
    { key: 'status', header: 'Status', render: (c: Course) => (
      <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${getCourseStatusColor(c.status)}`}>
        {getCourseStatusLabel(c.status)}
      </span>
    )},
    { key: 'dates', header: 'Zeitraum', render: (c: Course) =>
      c.startDate || c.endDate ? `${c.startDate ? formatDate(c.startDate) : '?'} - ${c.endDate ? formatDate(c.endDate) : '?'}` : '-'
    },
    { key: 'location', header: 'Ort', render: (c: Course) => c.location || '-' },
  ];

  return (
    <div className="space-y-4">
      <div className="flex flex-col sm:flex-row gap-3 justify-between">
        <div className="flex gap-3">
          <SearchInput value={search} onChange={setSearch} placeholder="Nach Mitglied suchen..." className="w-48" />
          <select value={categoryId} onChange={(e) => { setCategoryId(e.target.value); setPage(1); }}
            className="border border-gray-300 rounded-md px-3 py-2 text-sm">
            <option value="">Alle Lehrgänge</option>
            {(categories || []).map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
          <select value={status} onChange={(e) => { setStatus(e.target.value); setPage(1); }}
            className="border border-gray-300 rounded-md px-3 py-2 text-sm">
            <option value="">Alle Status</option>
            <option value="pending">Geplant</option>
            <option value="active">Laufend</option>
            <option value="completed">Abgeschlossen</option>
            <option value="failed">Nicht bestanden</option>
          </select>
        </div>
        <Button variant="primary" icon={<PlusIcon />} onClick={() => setShowCreate(true)}>Neuer Lehrgang</Button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table columns={columns} data={data?.data || []} loading={isLoading} emptyMessage="Keine Lehrgänge gefunden." keyExtractor={(c) => c.id} />
        {data?.pagination && <Pagination {...data.pagination} onPageChange={setPage} />}
      </div>

      <CourseCreateModal isOpen={showCreate} onClose={() => setShowCreate(false)} />
    </div>
  );
}
