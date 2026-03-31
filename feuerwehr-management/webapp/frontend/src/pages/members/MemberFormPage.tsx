import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQuery } from '@tanstack/react-query';
import { ArrowLeftIcon } from '@heroicons/react/24/outline';
import { membersApi } from '../../api/members';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Select } from '../../components/ui/Select';
import { Card } from '../../components/ui/Card';

export function MemberFormPage() {
  const navigate = useNavigate();

  const { data: groups } = useQuery({
    queryKey: ['member-groups'],
    queryFn: () => membersApi.getGroups().then((r) => r.data.data),
  });

  const [form, setForm] = useState({
    salutation: '',
    firstName: '',
    lastName: '',
    rank: '',
    groupId: '',
    street: '',
    city: '',
    phoneMobile: '',
    email: '',
    birthDate: '',
    memberSince: new Date().toISOString().substring(0, 10),
  });

  const mutation = useMutation({
    mutationFn: () =>
      membersApi.create({
        ...form,
        groupId: form.groupId ? parseInt(form.groupId) : null,
        birthDate: form.birthDate || null,
        memberSince: form.memberSince || null,
      }),
    onSuccess: (res) => navigate(`/members/${res.data.data.id}`),
  });

  const update = (field: string, value: string) =>
    setForm((prev) => ({ ...prev, [field]: value }));

  return (
    <div className="space-y-4 max-w-3xl">
      <div className="flex items-center gap-3">
        <Button variant="ghost" onClick={() => navigate('/members')} icon={<ArrowLeftIcon />} />
        <h2 className="text-xl font-bold">Neues Mitglied anlegen</h2>
      </div>

      <Card>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Select label="Anrede" value={form.salutation} onChange={(e) => update('salutation', e.target.value)}
            options={[{ value: 'Herr', label: 'Herr' }, { value: 'Frau', label: 'Frau' }]}
            placeholder="Bitte wählen" />
          <div />
          <Input label="Vorname" value={form.firstName} onChange={(e) => update('firstName', e.target.value)} required />
          <Input label="Nachname" value={form.lastName} onChange={(e) => update('lastName', e.target.value)} required />
          <Input label="Dienstgrad" value={form.rank} onChange={(e) => update('rank', e.target.value)} />
          <Select label="Gruppe" value={form.groupId} onChange={(e) => update('groupId', e.target.value)}
            options={(groups || []).map((g) => ({ value: g.id, label: g.name }))}
            placeholder="Keine Gruppe" />
          <Input label="Straße" value={form.street} onChange={(e) => update('street', e.target.value)} />
          <Input label="Ort" value={form.city} onChange={(e) => update('city', e.target.value)} />
          <Input label="Mobil" value={form.phoneMobile} onChange={(e) => update('phoneMobile', e.target.value)} type="tel" />
          <Input label="E-Mail" value={form.email} onChange={(e) => update('email', e.target.value)} type="email" />
          <Input label="Geburtsdatum" value={form.birthDate} onChange={(e) => update('birthDate', e.target.value)} type="date" />
          <Input label="Mitglied seit" value={form.memberSince} onChange={(e) => update('memberSince', e.target.value)} type="date" />
        </div>
        {mutation.isError && (
          <div className="mt-4 p-3 bg-red-50 border border-red-200 text-red-700 text-sm rounded">
            Fehler beim Anlegen: {(mutation.error as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Unbekannter Fehler'}
          </div>
        )}
        <div className="mt-6 flex gap-3 justify-end">
          <Button variant="secondary" onClick={() => navigate('/members')}>Abbrechen</Button>
          <Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending}
            disabled={!form.firstName || !form.lastName}>
            Mitglied anlegen
          </Button>
        </div>
      </Card>
    </div>
  );
}
