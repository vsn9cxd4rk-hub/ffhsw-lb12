import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeftIcon, PlusIcon, TrashIcon } from '@heroicons/react/24/outline';
import { operationsApi } from '../../api/operations';
import { Operation } from '../../types';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Textarea } from '../../components/ui/Textarea';
import { Card } from '../../components/ui/Card';
import { Table } from '../../components/ui/Table';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { formatDate } from '../../utils/format';

export function OperationDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [tab, setTab] = useState<'details' | 'times' | 'report'>('details');

  const { data: op, isLoading } = useQuery({
    queryKey: ['operation', id],
    queryFn: () => operationsApi.getById(parseInt(id!)).then((r) => r.data.data),
  });

  const updateMutation = useMutation({
    mutationFn: (data: Partial<Operation>) => operationsApi.update(parseInt(id!), data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation', id] }),
  });

  if (isLoading) return <LoadingSpinner />;
  if (!op) return <div className="text-center py-12 text-gray-500">Einsatz nicht gefunden</div>;

  return (
    <div className="space-y-4">
      <div className="flex items-center gap-3">
        <Button variant="ghost" onClick={() => navigate('/operations')} icon={<ArrowLeftIcon />} />
        <div>
          <h2 className="text-xl font-bold">{op.location}</h2>
          <p className="text-sm text-gray-500">{formatDate(op.date)} · {op.alarmTime || ''} · {op.keyword || 'Kein Stichwort'}</p>
        </div>
      </div>

      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {[{ id: 'details', label: 'Details' }, { id: 'times', label: 'Fahrzeugzeiten' }, { id: 'report', label: 'Bericht' }].map((t) => (
            <button key={t.id} onClick={() => setTab(t.id as typeof tab)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${tab === t.id ? 'border-primary-600 text-primary-600' : 'border-transparent text-gray-500 hover:text-gray-700'}`}>
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {tab === 'details' && <OperationDetailsTab op={op} onSave={(d) => updateMutation.mutate(d)} saving={updateMutation.isPending} />}
      {tab === 'times' && <OperationTimesTab operationId={parseInt(id!)} times={op.times || []} />}
      {tab === 'report' && <OperationReportTab operationId={parseInt(id!)} report={op.reports?.[0]} />}
    </div>
  );
}

function OperationDetailsTab({ op, onSave, saving }: { op: Operation; onSave: (d: Partial<Operation>) => void; saving: boolean }) {
  const [form, setForm] = useState({
    date: op.date.substring(0, 10),
    alarmTime: op.alarmTime || '',
    departureTime: op.departureTime || '',
    arrivalTime: op.arrivalTime || '',
    returnTime: op.returnTime || '',
    location: op.location,
    district: op.district || '',
    keyword: op.keyword || '',
    operationNumber: op.operationNumber || '',
    leaderCount: String(op.leaderCount),
    memberCount: String(op.memberCount),
    description: op.description || '',
    vehicles: op.vehicles || '',
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Card>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input label="Datum" value={form.date} onChange={(e) => u('date', e.target.value)} type="date" />
        <Input label="Einsatznummer" value={form.operationNumber} onChange={(e) => u('operationNumber', e.target.value)} />
        <Input label="Alarmzeit" value={form.alarmTime} onChange={(e) => u('alarmTime', e.target.value)} type="time" />
        <Input label="Ausgerückt" value={form.departureTime} onChange={(e) => u('departureTime', e.target.value)} type="time" />
        <Input label="Eingetroffen" value={form.arrivalTime} onChange={(e) => u('arrivalTime', e.target.value)} type="time" />
        <Input label="Eingerückt" value={form.returnTime} onChange={(e) => u('returnTime', e.target.value)} type="time" />
        <Input label="Einsatzort" value={form.location} onChange={(e) => u('location', e.target.value)} required />
        <Input label="Stadtteil" value={form.district} onChange={(e) => u('district', e.target.value)} />
        <Input label="Stichwort" value={form.keyword} onChange={(e) => u('keyword', e.target.value)} />
        <Input label="Fahrzeuge" value={form.vehicles} onChange={(e) => u('vehicles', e.target.value)} />
        <Input label="GF-Stärke" value={form.leaderCount} onChange={(e) => u('leaderCount', e.target.value)} type="number" />
        <Input label="FM-Stärke" value={form.memberCount} onChange={(e) => u('memberCount', e.target.value)} type="number" />
        <div className="col-span-full">
          <Textarea label="Beschreibung" value={form.description} onChange={(e) => u('description', e.target.value)} rows={4} />
        </div>
      </div>
      <div className="mt-6 flex justify-end">
        <Button variant="primary" onClick={() => onSave({ ...form, leaderCount: parseInt(form.leaderCount) || 0, memberCount: parseInt(form.memberCount) || 0 } as Partial<Operation>)} loading={saving}>
          Speichern
        </Button>
      </div>
    </Card>
  );
}

function OperationTimesTab({ operationId, times }: { operationId: number; times: Array<{ id: number; vehicleName: string; alarmTime: string | null; departureTime: string | null; arrivalTime: string | null; returnTime: string | null }> }) {
  const queryClient = useQueryClient();
  const [showAdd, setShowAdd] = useState(false);
  const [newTime, setNewTime] = useState({ vehicleName: '', alarmTime: '', departureTime: '', arrivalTime: '', returnTime: '' });

  const addMutation = useMutation({
    mutationFn: () => operationsApi.createTime(operationId, { ...newTime, alarmTime: newTime.alarmTime || null, departureTime: newTime.departureTime || null, arrivalTime: newTime.arrivalTime || null, returnTime: newTime.returnTime || null }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['operation', String(operationId)] }); setShowAdd(false); },
  });

  const deleteMutation = useMutation({
    mutationFn: (timeId: number) => operationsApi.deleteTime(operationId, timeId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation', String(operationId)] }),
  });

  return (
    <Card title="Fahrzeugzeiten" actions={<Button variant="secondary" size="sm" icon={<PlusIcon />} onClick={() => setShowAdd(!showAdd)}>Hinzufügen</Button>}>
      {showAdd && (
        <div className="mb-4 p-4 bg-gray-50 rounded-md grid grid-cols-2 md:grid-cols-3 gap-3">
          <Input label="Fahrzeug" value={newTime.vehicleName} onChange={(e) => setNewTime(t => ({ ...t, vehicleName: e.target.value }))} />
          <Input label="Alarm" value={newTime.alarmTime} onChange={(e) => setNewTime(t => ({ ...t, alarmTime: e.target.value }))} type="time" />
          <Input label="Ausgerückt" value={newTime.departureTime} onChange={(e) => setNewTime(t => ({ ...t, departureTime: e.target.value }))} type="time" />
          <Input label="Eingetroffen" value={newTime.arrivalTime} onChange={(e) => setNewTime(t => ({ ...t, arrivalTime: e.target.value }))} type="time" />
          <Input label="Eingerückt" value={newTime.returnTime} onChange={(e) => setNewTime(t => ({ ...t, returnTime: e.target.value }))} type="time" />
          <div className="flex items-end gap-2">
            <Button variant="primary" size="sm" onClick={() => addMutation.mutate()} loading={addMutation.isPending} disabled={!newTime.vehicleName}>Speichern</Button>
            <Button variant="secondary" size="sm" onClick={() => setShowAdd(false)}>Abbrechen</Button>
          </div>
        </div>
      )}
      <Table
        columns={[
          { key: 'vehicleName', header: 'Fahrzeug' },
          { key: 'alarmTime', header: 'Alarm', render: (r) => r.alarmTime || '-' },
          { key: 'departureTime', header: 'Ausgerückt', render: (r) => r.departureTime || '-' },
          { key: 'arrivalTime', header: 'Eingetroffen', render: (r) => r.arrivalTime || '-' },
          { key: 'returnTime', header: 'Eingerückt', render: (r) => r.returnTime || '-' },
          { key: 'actions', header: '', render: (r) => (
            <Button variant="ghost" size="sm" onClick={(e) => { e.stopPropagation(); deleteMutation.mutate(r.id as number); }}>
              <TrashIcon className="h-4 w-4 text-red-500" />
            </Button>
          )},
        ]}
        data={times}
        emptyMessage="Keine Fahrzeugzeiten eingetragen."
      />
    </Card>
  );
}

function OperationReportTab({ operationId, report }: { operationId: number; report?: { content: string } }) {
  const queryClient = useQueryClient();
  const [content, setContent] = useState(report?.content || '');

  const saveMutation = useMutation({
    mutationFn: () => operationsApi.updateReport(operationId, content),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operation', String(operationId)] }),
  });

  return (
    <Card title="Einsatzbericht">
      <Textarea value={content} onChange={(e) => setContent(e.target.value)} rows={12} placeholder="Einsatzbericht eingeben..." />
      <div className="mt-4 flex justify-end">
        <Button variant="primary" onClick={() => saveMutation.mutate()} loading={saveMutation.isPending}>Speichern</Button>
      </div>
    </Card>
  );
}
