import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { usersApi } from '../../api/users';
import { membersApi } from '../../api/members';
import { User, Member, PermissionGroup } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { SearchInput } from '../../components/ui/SearchInput';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';

function UserFormModal({ isOpen, onClose, editUser }: { isOpen: boolean; onClose: () => void; editUser?: User | null }) {
  const queryClient = useQueryClient();
  const { data: groups } = useQuery({ queryKey: ['permission-groups'], queryFn: () => usersApi.getGroups().then(r => r.data.data as PermissionGroup[]) });
  const { data: membersData } = useQuery({ queryKey: ['members-for-user-link'], queryFn: () => membersApi.getAll({ limit: 10000, isInactive: false }).then(r => r.data.data as Member[]) });
  const [form, setForm] = useState({
    username: editUser?.username || '',
    email: editUser?.email || '',
    name: editUser?.name || '',
    password: '',
    groupId: editUser?.groupId ? String(editUser.groupId) : '',
    memberId: editUser?.memberId ? String(editUser.memberId) : '',
    isAdmin: editUser?.isAdmin || false,
    isActive: editUser?.isActive ?? true,
  });
  const [error, setError] = useState('');

  const mutation = useMutation({
    mutationFn: () => editUser
      ? usersApi.update(editUser.id, { ...form, groupId: form.groupId ? parseInt(form.groupId) : null, memberId: form.memberId ? parseInt(form.memberId) : null, password: form.password || undefined })
      : usersApi.create({ ...form, groupId: form.groupId ? parseInt(form.groupId) : null, memberId: form.memberId ? parseInt(form.memberId) : null } as User & { password: string }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['users'] }); setError(''); onClose(); },
    onError: (err: unknown) => {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen';
      setError(msg);
    },
  });

  const u = (f: string, v: string | boolean) => setForm(p => ({ ...p, [f]: v }));
  const isValid = form.username && form.groupId && (editUser || form.password);

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={editUser ? 'Benutzer bearbeiten' : 'Neuer Benutzer'} size="md"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!isValid}>Speichern</Button></>}
    >
      <div className="space-y-3">
        {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}
        <Input label="Benutzername" value={form.username} onChange={(e) => u('username', e.target.value)} required disabled={!!editUser} />
        <Input label="Name" value={form.name} onChange={(e) => u('name', e.target.value)} />
        <Input label="E-Mail" value={form.email} onChange={(e) => u('email', e.target.value)} type="email" />
        <Input label={editUser ? 'Neues Passwort (leer = unverändert)' : 'Passwort'} value={form.password} onChange={(e) => u('password', e.target.value)} type="password" required={!editUser} />
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Berechtigungsgruppe <span className="text-red-500">*</span></label>
          <select
            value={form.groupId}
            onChange={(e) => u('groupId', e.target.value)}
            className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white"
            required
          >
            <option value="">-- Bitte wählen --</option>
            {(groups || []).map(g => <option key={g.id} value={g.id}>{g.name}</option>)}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Verknüpftes Mitglied</label>
          <select
            value={form.memberId}
            onChange={(e) => u('memberId', e.target.value)}
            className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white"
          >
            <option value="">-- Kein Mitglied verknüpft --</option>
            {(membersData || []).map((m: Member) => <option key={m.id} value={m.id}>{m.lastName}, {m.firstName}</option>)}
          </select>
        </div>
        <div className="flex gap-4">
          <label className="flex items-center gap-2 text-sm cursor-pointer">
            <input type="checkbox" checked={form.isAdmin} onChange={(e) => u('isAdmin', e.target.checked)} className="rounded" />
            Administrator
          </label>
          <label className="flex items-center gap-2 text-sm cursor-pointer">
            <input type="checkbox" checked={form.isActive} onChange={(e) => u('isActive', e.target.checked)} className="rounded" />
            Aktiv
          </label>
        </div>
      </div>
    </Modal>
  );
}

export function UsersPage() {
  const queryClient = useQueryClient();
  const [search, setSearch] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [editUser, setEditUser] = useState<User | null>(null);

  const { data, isLoading } = useQuery({
    queryKey: ['users', search],
    queryFn: () => usersApi.getAll({ search: search || undefined }).then(r => r.data.data),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => usersApi.delete(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['users'] }),
  });

  const openCreate = () => { setEditUser(null); setShowForm(true); };
  const openEdit = (u: User) => { setEditUser(u); setShowForm(true); };
  const closeForm = () => { setShowForm(false); setEditUser(null); };

  const columns = [
    { key: 'username', header: 'Benutzername', render: (u: User) => (
      <div><p className="font-medium">{u.username}</p>{u.name && <p className="text-xs text-gray-500">{u.name}</p>}</div>
    )},
    { key: 'email', header: 'E-Mail', render: (u: User) => u.email || '-' },
    { key: 'member', header: 'Mitglied', render: (u: User) => u.member ? `${u.member.lastName}, ${u.member.firstName}` : '-' },
    { key: 'group', header: 'Gruppe', render: (u: User) => u.group?.name || '-' },
    { key: 'flags', header: 'Rollen', render: (u: User) => (
      <div className="flex gap-1">
        {u.isAdmin && <Badge variant="danger">Admin</Badge>}
        <Badge variant={u.isActive ? 'success' : 'default'}>{u.isActive ? 'Aktiv' : 'Inaktiv'}</Badge>
      </div>
    )},
    { key: 'actions', header: '', render: (u: User) => (
      <div className="flex gap-2">
        <Button size="sm" variant="secondary" onClick={(e: React.MouseEvent) => { e.stopPropagation(); openEdit(u); }}>Bearbeiten</Button>
        <Button size="sm" variant="danger" onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(u.id); }} loading={deleteMutation.isPending}>Löschen</Button>
      </div>
    )},
  ];

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <SearchInput value={search} onChange={setSearch} placeholder="Benutzer suchen..." className="w-64" />
        <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neuer Benutzer</Button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table columns={columns} data={data || []} loading={isLoading} emptyMessage="Keine Benutzer gefunden." keyExtractor={(u) => u.id} />
      </div>

      {showForm && (
        <UserFormModal key={editUser?.id || 'new'} isOpen={showForm} onClose={closeForm} editUser={editUser} />
      )}
    </div>
  );
}
