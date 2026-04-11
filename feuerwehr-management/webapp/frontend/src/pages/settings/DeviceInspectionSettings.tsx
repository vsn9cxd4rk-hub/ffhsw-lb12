import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon, PencilIcon, TrashIcon, ChevronDownIcon, ChevronRightIcon } from '@heroicons/react/24/outline';
import { settingsApi } from '../../api/settings';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Modal } from '../../components/ui/Modal';
import { DeviceClass, DeviceSubclass, InspectionCriterion } from '../../types';

type EditTarget =
  | { type: 'class'; item?: DeviceClass }
  | { type: 'subclass'; parentId: number; item?: DeviceSubclass }
  | { type: 'criterion'; parentId: number; item?: InspectionCriterion };

export function DeviceInspectionSettings() {
  const queryClient = useQueryClient();
  const [expandedClasses, setExpandedClasses] = useState<Set<number>>(new Set());
  const [expandedSubclasses, setExpandedSubclasses] = useState<Set<number>>(new Set());
  const [editTarget, setEditTarget] = useState<EditTarget | null>(null);
  const [editName, setEditName] = useState('');
  const [error, setError] = useState('');

  const { data: classesRes } = useQuery({
    queryKey: ['device-classes'],
    queryFn: () => settingsApi.getDeviceClasses(),
  });
  const classes: DeviceClass[] = classesRes?.data?.data || [];

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['device-classes'] });

  const saveClassMut = useMutation({
    mutationFn: async () => {
      if (!editTarget || !editName.trim()) return;
      if (editTarget.type === 'class') {
        if (editTarget.item) await settingsApi.updateDeviceClass(editTarget.item.id, { name: editName.trim() });
        else await settingsApi.createDeviceClass({ name: editName.trim(), sortOrder: classes.length + 1 });
      } else if (editTarget.type === 'subclass') {
        const parent = classes.find(c => c.id === editTarget.parentId);
        if (editTarget.item) await settingsApi.updateSubclass(editTarget.item.id, { name: editName.trim() });
        else await settingsApi.createSubclass(editTarget.parentId, { name: editName.trim(), sortOrder: (parent?.subclasses?.length || 0) + 1 });
      } else if (editTarget.type === 'criterion') {
        if (editTarget.item) await settingsApi.updateCriterion(editTarget.item.id, { name: editName.trim() });
        else await settingsApi.createCriterion(editTarget.parentId, { name: editName.trim() });
      }
    },
    onSuccess: () => { invalidate(); setEditTarget(null); setEditName(''); setError(''); },
    onError: (err: Error) => setError(err.message),
  });

  const deleteClassMut = useMutation({ mutationFn: (id: number) => settingsApi.deleteDeviceClass(id), onSuccess: invalidate });
  const deleteSubclassMut = useMutation({ mutationFn: (id: number) => settingsApi.deleteSubclass(id), onSuccess: invalidate });
  const deleteCriterionMut = useMutation({ mutationFn: (id: number) => settingsApi.deleteCriterion(id), onSuccess: invalidate });

  const toggleClass = (id: number) => {
    const s = new Set(expandedClasses);
    s.has(id) ? s.delete(id) : s.add(id);
    setExpandedClasses(s);
  };
  const toggleSubclass = (id: number) => {
    const s = new Set(expandedSubclasses);
    s.has(id) ? s.delete(id) : s.add(id);
    setExpandedSubclasses(s);
  };

  const openEdit = (target: EditTarget) => {
    setEditTarget(target);
    setEditName(target.type === 'class' ? target.item?.name || '' : target.type === 'subclass' ? target.item?.name || '' : target.item?.name || '');
    setError('');
  };

  const getEditTitle = () => {
    if (!editTarget) return '';
    const isNew = editTarget.type === 'class' ? !editTarget.item : editTarget.type === 'subclass' ? !editTarget.item : !editTarget.item;
    const labels = { class: 'Geräteklasse', subclass: 'Unterklasse', criterion: 'Prüfkriterium' };
    return `${isNew ? 'Neue' : ''} ${labels[editTarget.type]} ${isNew ? 'hinzufügen' : 'bearbeiten'}`;
  };

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h3 className="text-lg font-medium text-gray-900">Geräteklassen & Prüfkriterien</h3>
        <Button size="sm" onClick={() => openEdit({ type: 'class' })}>
          <PlusIcon className="h-4 w-4 mr-1" /> Neue Geräteklasse
        </Button>
      </div>

      <div className="bg-white rounded-lg border border-gray-200 divide-y divide-gray-200">
        {classes.length === 0 && (
          <div className="p-6 text-center text-gray-500">Keine Geräteklassen vorhanden</div>
        )}
        {classes.map(dc => (
          <div key={dc.id}>
            {/* Device Class Row */}
            <div className="flex items-center justify-between px-4 py-3 bg-gray-50 hover:bg-gray-100 cursor-pointer" onClick={() => toggleClass(dc.id)}>
              <div className="flex items-center gap-2">
                {expandedClasses.has(dc.id)
                  ? <ChevronDownIcon className="h-5 w-5 text-gray-400" />
                  : <ChevronRightIcon className="h-5 w-5 text-gray-400" />}
                <span className="font-medium text-gray-900">{dc.name}</span>
                <span className="text-xs text-gray-500">({dc.subclasses?.length || 0} Unterklassen)</span>
              </div>
              <div className="flex items-center gap-1" onClick={e => e.stopPropagation()}>
                <button className="p-1 text-gray-400 hover:text-primary-600" onClick={() => openEdit({ type: 'class', item: dc })}>
                  <PencilIcon className="h-4 w-4" />
                </button>
                <button className="p-1 text-gray-400 hover:text-red-600" onClick={() => { if (confirm('Geräteklasse inkl. aller Unterklassen und Kriterien löschen?')) deleteClassMut.mutate(dc.id); }}>
                  <TrashIcon className="h-4 w-4" />
                </button>
              </div>
            </div>

            {/* Subclasses */}
            {expandedClasses.has(dc.id) && (
              <div className="pl-8">
                {dc.subclasses?.map(sc => (
                  <div key={sc.id}>
                    <div className="flex items-center justify-between px-4 py-2 border-t border-gray-100 hover:bg-gray-50 cursor-pointer" onClick={() => toggleSubclass(sc.id)}>
                      <div className="flex items-center gap-2">
                        {expandedSubclasses.has(sc.id)
                          ? <ChevronDownIcon className="h-4 w-4 text-gray-400" />
                          : <ChevronRightIcon className="h-4 w-4 text-gray-400" />}
                        <span className="text-sm font-medium text-gray-700">{sc.name}</span>
                        <span className="text-xs text-gray-500">({sc.criteria?.length || 0} Kriterien)</span>
                      </div>
                      <div className="flex items-center gap-1" onClick={e => e.stopPropagation()}>
                        <button className="p-1 text-gray-400 hover:text-primary-600" onClick={() => openEdit({ type: 'subclass', parentId: dc.id, item: sc })}>
                          <PencilIcon className="h-4 w-4" />
                        </button>
                        <button className="p-1 text-gray-400 hover:text-red-600" onClick={() => { if (confirm('Unterklasse inkl. Kriterien löschen?')) deleteSubclassMut.mutate(sc.id); }}>
                          <TrashIcon className="h-4 w-4" />
                        </button>
                      </div>
                    </div>

                    {/* Criteria */}
                    {expandedSubclasses.has(sc.id) && (
                      <div className="pl-8">
                        {sc.criteria?.map(cr => (
                          <div key={cr.id} className="flex items-center justify-between px-4 py-1.5 border-t border-gray-50 hover:bg-gray-50">
                            <span className="text-sm text-gray-600">{cr.name}</span>
                            <div className="flex items-center gap-1">
                              <button className="p-1 text-gray-400 hover:text-primary-600" onClick={() => openEdit({ type: 'criterion', parentId: sc.id, item: cr })}>
                                <PencilIcon className="h-3.5 w-3.5" />
                              </button>
                              <button className="p-1 text-gray-400 hover:text-red-600" onClick={() => { if (confirm('Prüfkriterium löschen?')) deleteCriterionMut.mutate(cr.id); }}>
                                <TrashIcon className="h-3.5 w-3.5" />
                              </button>
                            </div>
                          </div>
                        ))}
                        <div className="px-4 py-2 border-t border-gray-50">
                          <button className="text-xs text-primary-600 hover:text-primary-700 flex items-center gap-1"
                            onClick={() => openEdit({ type: 'criterion', parentId: sc.id })}>
                            <PlusIcon className="h-3.5 w-3.5" /> Neues Prüfkriterium
                          </button>
                        </div>
                      </div>
                    )}
                  </div>
                ))}
                <div className="px-4 py-2 border-t border-gray-100">
                  <button className="text-sm text-primary-600 hover:text-primary-700 flex items-center gap-1"
                    onClick={() => openEdit({ type: 'subclass', parentId: dc.id })}>
                    <PlusIcon className="h-4 w-4" /> Neue Unterklasse
                  </button>
                </div>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Edit Modal */}
      <Modal isOpen={!!editTarget} onClose={() => setEditTarget(null)} title={getEditTitle()} size="sm">
        <form onSubmit={e => { e.preventDefault(); saveClassMut.mutate(); }} className="space-y-4">
          {error && <div className="bg-red-50 text-red-700 text-sm p-3 rounded">{error}</div>}
          <Input label="Bezeichnung" value={editName} onChange={e => setEditName(e.target.value)} required />
          <div className="flex justify-end gap-2">
            <Button variant="secondary" type="button" onClick={() => setEditTarget(null)}>Abbrechen</Button>
            <Button type="submit" loading={saveClassMut.isPending}>Speichern</Button>
          </div>
        </form>
      </Modal>
    </div>
  );
}
