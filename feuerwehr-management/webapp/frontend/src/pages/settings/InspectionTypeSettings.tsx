import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { AxiosResponse } from 'axios';
import { settingsApi } from '../../api/settings';
import { Card } from '../../components/ui/Card';
import { Input } from '../../components/ui/Input';
import { Button } from '../../components/ui/Button';
import { Table } from '../../components/ui/Table';
import { Modal } from '../../components/ui/Modal';
import { Textarea } from '../../components/ui/Textarea';
import { InspectionType } from '../../types';

export function InspectionTypeSettings() {
  const queryClient = useQueryClient();
  const [showModal, setShowModal] = useState(false);
  const [editItem, setEditItem] = useState<InspectionType | null>(null);
  const [form, setForm] = useState({ name: '', description: '' });

  const { data: inspectionTypes, isLoading } = useQuery({
    queryKey: ['inspection-types'],
    queryFn: () => settingsApi.getInspectionTypes().then(r => r.data.data),
  });

  const saveMutation = useMutation({
    mutationFn: (): Promise<AxiosResponse<any>> => editItem
      ? settingsApi.updateInspectionType(editItem.id, { name: form.name, description: form.description || undefined })
      : settingsApi.createInspectionType({ name: form.name, description: form.description || undefined }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['inspection-types'] });
      closeModal();
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => settingsApi.deleteInspectionType(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['inspection-types'] }),
  });

  function openCreate() {
    setEditItem(null);
    setForm({ name: '', description: '' });
    setShowModal(true);
  }

  function openEdit(item: InspectionType) {
    setEditItem(item);
    setForm({ name: item.name, description: item.description || '' });
    setShowModal(true);
  }

  function closeModal() {
    setShowModal(false);
    setEditItem(null);
    setForm({ name: '', description: '' });
  }

  return (
    <Card
      title="Prüfarten"
      actions={
        <Button variant="secondary" size="sm" onClick={openCreate}>
          + Neue Prüfart
        </Button>
      }
    >
      <Table
        columns={[
          { key: 'name', header: 'Name' },
          { key: 'description', header: 'Beschreibung', render: (r: InspectionType) => r.description || '-' },
          {
            key: 'actions',
            header: 'Aktionen',
            render: (r: InspectionType) => (
              <div className="flex gap-2">
                <Button size="sm" variant="secondary" onClick={() => openEdit(r)}>
                  Bearbeiten
                </Button>
                <Button size="sm" variant="danger" onClick={() => deleteMutation.mutate(r.id)}>
                  Löschen
                </Button>
              </div>
            ),
          },
        ]}
        data={inspectionTypes || []}
        loading={isLoading}
        emptyMessage="Keine Prüfarten vorhanden."
        keyExtractor={(r) => r.id}
      />

      <Modal
        isOpen={showModal}
        onClose={closeModal}
        title={editItem ? 'Prüfart bearbeiten' : 'Neue Prüfart'}
        size="sm"
        footer={
          <>
            <Button variant="secondary" onClick={closeModal}>
              Abbrechen
            </Button>
            <Button
              variant="primary"
              onClick={() => saveMutation.mutate()}
              loading={saveMutation.isPending}
              disabled={!form.name.trim()}
            >
              Speichern
            </Button>
          </>
        }
      >
        <div className="space-y-3">
          <Input
            label="Name"
            value={form.name}
            onChange={(e) => setForm(f => ({ ...f, name: e.target.value }))}
            required
          />
          <Textarea
            label="Beschreibung"
            value={form.description}
            onChange={(e) => setForm(f => ({ ...f, description: e.target.value }))}
          />
        </div>
      </Modal>
    </Card>
  );
}
