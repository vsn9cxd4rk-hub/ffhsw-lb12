import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon, TrashIcon } from '@heroicons/react/24/outline';
import { trainingApi } from '../../api/training';
import { CourseCategory } from '../../types';
import { Card } from '../../components/ui/Card';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Table } from '../../components/ui/Table';

const QUALIFICATION_OPTIONS = [
  { value: '', label: '-- Keine Zuordnung --' },
  { value: 'qualAGT', label: 'AGT (Atemschutzgeräteträger)' },
  { value: 'qualTruppmann', label: 'Truppmann' },
  { value: 'qualTruppfuehrer', label: 'Truppführer' },
  { value: 'qualGruppenfuehrer', label: 'Gruppenführer' },
  { value: 'qualZugfuehrer', label: 'Zugführer' },
  { value: 'qualMachinist', label: 'Maschinist' },
  { value: 'qualRadioOperator', label: 'Sprechfunker' },
  { value: 'qualFirstAid', label: 'Erste Hilfe' },
  { value: 'qualRettSan', label: 'RettSan' },
  { value: 'qualFwSan', label: 'FwSan' },
  { value: 'qualVerbandfuehrer', label: 'Verbandsführer' },
  { value: 'qualLicenseB', label: 'Führerschein B' },
  { value: 'qualLicenseC', label: 'Führerschein C' },
  { value: 'qualTH1', label: 'TH1' },
];

function getQualLabel(field: string | null): string {
  if (!field) return '-';
  return QUALIFICATION_OPTIONS.find(o => o.value === field)?.label || field;
}

export function CourseCategorySettings() {
  const queryClient = useQueryClient();
  const { data: categories, isLoading } = useQuery({
    queryKey: ['course-categories'],
    queryFn: () => trainingApi.getCategories().then(r => r.data.data as CourseCategory[]),
  });

  const [showForm, setShowForm] = useState(false);
  const [editCat, setEditCat] = useState<CourseCategory | null>(null);
  const [form, setForm] = useState({ name: '', description: '', qualificationField: '' });

  const openCreate = () => { setEditCat(null); setForm({ name: '', description: '', qualificationField: '' }); setShowForm(true); };
  const openEdit = (cat: CourseCategory) => {
    setEditCat(cat);
    setForm({ name: cat.name, description: cat.description || '', qualificationField: cat.qualificationField || '' });
    setShowForm(true);
  };

  const saveMutation = useMutation({
    mutationFn: () => {
      const data = { name: form.name, description: form.description || null, qualificationField: form.qualificationField || null };
      return editCat ? trainingApi.updateCategory(editCat.id, data) : trainingApi.createCategory(data);
    },
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['course-categories'] }); setShowForm(false); },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => trainingApi.deleteCategory(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['course-categories'] }),
  });

  const columns = [
    { key: 'name', header: 'Kategorie', render: (c: CourseCategory) => c.name },
    { key: 'description', header: 'Beschreibung', render: (c: CourseCategory) => c.description || '-' },
    { key: 'qual', header: 'Verknüpfte Qualifikation', render: (c: CourseCategory) => getQualLabel(c.qualificationField) },
    { key: 'actions', header: '', render: (c: CourseCategory) => (
      <div className="flex gap-2">
        <Button size="sm" variant="secondary" onClick={(e: React.MouseEvent) => { e.stopPropagation(); openEdit(c); }}>Bearbeiten</Button>
        <Button size="sm" variant="danger" icon={<TrashIcon />} onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(c.id); }} />
      </div>
    )},
  ];

  return (
    <Card title="Lehrgangskategorien" subtitle="Bei abgeschlossenem Lehrgang wird die verknüpfte Qualifikation automatisch beim Mitglied gesetzt.">
      <div className="space-y-4">
        <div className="flex justify-end">
          <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neue Kategorie</Button>
        </div>

        <Table columns={columns} data={categories || []} loading={isLoading} emptyMessage="Keine Kategorien vorhanden." keyExtractor={(c) => c.id} />

        {showForm && (
          <div className="border border-gray-200 rounded-lg p-4 bg-gray-50 space-y-3">
            <h4 className="text-sm font-semibold text-gray-700">{editCat ? 'Kategorie bearbeiten' : 'Neue Kategorie'}</h4>
            <Input label="Name" value={form.name} onChange={(e) => setForm(p => ({ ...p, name: e.target.value }))} required />
            <Input label="Beschreibung" value={form.description} onChange={(e) => setForm(p => ({ ...p, description: e.target.value }))} />
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1.5">Verknüpfte Qualifikation</label>
              <select
                value={form.qualificationField}
                onChange={(e) => setForm(p => ({ ...p, qualificationField: e.target.value }))}
                className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white"
              >
                {QUALIFICATION_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
              </select>
              <p className="text-xs text-gray-500 mt-1">Wird automatisch gesetzt, wenn ein Lehrgang dieser Kategorie als "Abgeschlossen" markiert wird.</p>
            </div>
            <div className="flex gap-2">
              <Button variant="primary" onClick={() => saveMutation.mutate()} loading={saveMutation.isPending} disabled={!form.name}>Speichern</Button>
              <Button variant="secondary" onClick={() => setShowForm(false)}>Abbrechen</Button>
            </div>
          </div>
        )}
      </div>
    </Card>
  );
}
