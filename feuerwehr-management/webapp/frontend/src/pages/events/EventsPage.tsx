import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { PlusIcon } from '@heroicons/react/24/outline';
import { eventsApi } from '../../api/events';
import { Event } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { Pagination } from '../../components/ui/Pagination';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Select } from '../../components/ui/Select';
import { formatDate, getEventCategoryLabel, getEventCategoryColor } from '../../utils/format';

const CATEGORY_OPTIONS = [
  { value: 1, label: 'Einsatz' },
  { value: 2, label: 'Dienstabend' },
  { value: 3, label: 'BSW' },
  { value: 4, label: 'Sonstige' },
  { value: 5, label: 'Übung' },
];

function EventCreateModal({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    name: '', category: '2', date: new Date().toISOString().substring(0, 10),
    startTime: '', endTime: '',
  });

  const mutation = useMutation({
    mutationFn: () => eventsApi.create({ ...form, category: parseInt(form.category) } as Partial<Event>),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['events'] }); onClose(); },
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Neue Veranstaltung" size="md"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.name || !form.date}>Anlegen</Button></>}
    >
      <div className="space-y-3">
        <Input label="Bezeichnung" value={form.name} onChange={(e) => u('name', e.target.value)} required />
        <Select label="Kategorie" value={form.category} onChange={(e) => u('category', e.target.value)} options={CATEGORY_OPTIONS} />
        <Input label="Datum" value={form.date} onChange={(e) => u('date', e.target.value)} type="date" required />
        <div className="grid grid-cols-2 gap-3">
          <Input label="Beginn" value={form.startTime} onChange={(e) => u('startTime', e.target.value)} type="time" />
          <Input label="Ende" value={form.endTime} onChange={(e) => u('endTime', e.target.value)} type="time" />
        </div>
      </div>
    </Modal>
  );
}

export function EventsPage() {
  const navigate = useNavigate();
  const [category, setCategory] = useState('');
  const [year, setYear] = useState(String(new Date().getFullYear()));
  const [page, setPage] = useState(1);
  const [showCreate, setShowCreate] = useState(false);

  const { data, isLoading } = useQuery({
    queryKey: ['events', category, year, page],
    queryFn: () => eventsApi.getAll({
      category: category || undefined,
      year: year || undefined,
      page, limit: 20,
    }).then((r) => r.data),
  });

  const columns = [
    { key: 'date', header: 'Datum', render: (ev: Event) => formatDate(ev.date) },
    { key: 'name', header: 'Bezeichnung', render: (ev: Event) => (
      <div><p className="font-medium">{ev.name}</p>{ev.name2 && <p className="text-xs text-gray-500">{ev.name2}</p>}</div>
    )},
    { key: 'category', header: 'Kategorie', render: (ev: Event) => (
      <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${getEventCategoryColor(ev.category)}`}>
        {getEventCategoryLabel(ev.category)}
      </span>
    )},
    { key: 'time', header: 'Zeit', render: (ev: Event) => ev.startTime ? `${ev.startTime}${ev.endTime ? ` - ${ev.endTime}` : ''}` : '-' },
    { key: 'attendance', header: 'Anwesenheit', render: (ev: Event) => (
      <Button variant="ghost" size="sm" onClick={(e) => { e.stopPropagation(); navigate(`/events/${ev.id}/attendance`); }}>
        Erfassen
      </Button>
    )},
  ];

  const years = Array.from({ length: 5 }, (_, i) => String(new Date().getFullYear() - i));

  return (
    <div className="space-y-4">
      <div className="flex flex-col sm:flex-row gap-3 justify-between">
        <div className="flex gap-3">
          <select value={category} onChange={(e) => { setCategory(e.target.value); setPage(1); }}
            className="border border-gray-300 rounded-md px-3 py-2 text-sm">
            <option value="">Alle Kategorien</option>
            {CATEGORY_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
          </select>
          <select value={year} onChange={(e) => { setYear(e.target.value); setPage(1); }}
            className="border border-gray-300 rounded-md px-3 py-2 text-sm">
            <option value="">Alle Jahre</option>
            {years.map(y => <option key={y} value={y}>{y}</option>)}
          </select>
        </div>
        <Button variant="primary" icon={<PlusIcon />} onClick={() => setShowCreate(true)}>Neue Veranstaltung</Button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table columns={columns} data={data?.data || []} loading={isLoading} emptyMessage="Keine Veranstaltungen gefunden."
          onRowClick={(ev) => navigate(`/events/${ev.id}`)} keyExtractor={(ev) => ev.id} />
        {data?.pagination && <Pagination {...data.pagination} onPageChange={setPage} />}
      </div>

      <EventCreateModal isOpen={showCreate} onClose={() => setShowCreate(false)} />
    </div>
  );
}
