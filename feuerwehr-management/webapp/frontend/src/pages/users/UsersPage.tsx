import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon } from '@heroicons/react/24/outline';
import { usersApi } from '../../api/users';
import { User, PermissionGroup } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { SearchInput } from '../../components/ui/SearchInput';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
<<<<<<< HEAD
import { Select } from '../../components/ui/Select';

function UserFormModal({ isOpen, onClose, editUser }: { isOpen: boolean; onClose: () => void; editUser?: User | null }) {
  const queryClient = useQueryClient();
  const { data: groups } = useQuery({ queryKey: ['permission-groups'], queryFn: () => usersApi.getGroups().then(r => r.data.data) });
=======

function UserFormModal({ isOpen, onClose, editUser }: { isOpen: boolean; onClose: () => void; editUser?: User | null }) {
  const queryClient = useQueryClient();
  const { data: groups } = useQuery({ queryKey: ['permission-groups'], queryFn: () => usersApi.getGroups().then(r => r.data.data as PermissionGroup[]) });
>>>>>>> a9dc7840 (Added New FW Management system)
  const [form, setForm] = useState({
    username: editUser?.username || '',
    email: editUser?.email || '',
    name: editUser?.name || '',
    password: '',
    groupId: editUser?.groupId ? String(editUser.groupId) : '',
    isAdmin: editUser?.isAdmin || false,
    isActive: editUser?.isActive ?? true,
  });
<<<<<<< HEAD
=======
  const [error, setError] = useState('');
>>>>>>> a9dc7840 (Added New FW Management system)

  const mutation = useMutation({
    mutationFn: () => editUser
      ? usersApi.update(editUser.id, { ...form, groupId: form.groupId ? parseInt(form.groupId) : null, password: form.password || undefined })
      : usersApi.create({ ...form, groupId: form.groupId ? parseInt(form.groupId) : null } as User & { password: string }),
<<<<<<< HEAD
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['users'] }); onClose(); },
  });

  const u = (f: string, v: string | boolean) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={editUser ? 'Benutzer bearbeiten' : 'Neuer Benutzer'} size="md"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.username || (!editUser && !form.password)}>Speichern</Button></>}
    >
      <div className="space-y-3">
=======
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
>>>>>>> a9dc7840 (Added New FW Management system)
        <Input label="Benutzername" value={form.username} onChange={(e) => u('username', e.target.value)} required disabled={!!editUser} />
        <Input label="Name" value={form.name} onChange={(e) => u('name', e.target.value)} />
        <Input label="E-Mail" value={form.email} onChange={(e) => u('email', e.target.value)} type="email" />
        <Input label={editUser ? 'Neues Passwort (leer = unverändert)' : 'Passwort'} value={form.password} onChange={(e) => u('password', e.target.value)} type="password" required={!editUser} />
<<<<<<< HEAD
        <Select label="Berechtigungsgruppe" value={form.groupId} onChange={(e) => u('groupId', e.target.value)}
          options={(groups || []).map(g => ({ value: g.id, label: g.name }))} placeholder="Keine Gruppe" />
=======
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
>>>>>>> a9dc7840 (Added New FW Management system)
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
<<<<<<< HEAD
  const [showCreate, setShowCreate] = useState(false);
=======
  const [showForm, setShowForm] = useState(false);
>>>>>>> a9dc7840 (Added New FW Management system)
  const [editUser, setEditUser] = useState<User | null>(null);

  const { data, isLoading } = useQuery({
    queryKey: ['users', search],
    queryFn: () => usersApi.getAll({ search: search || undefined }).then(r => r.data.data),
  });

  const deleteMutation = useMutation({
    mutationFn: (id: number) => usersApi.delete(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['users'] }),
  });

<<<<<<< HEAD
=======
  const openCreate = () => { setEditUser(null); setShowForm(true); };
  const openEdit = (u: User) => { setEditUser(u); setShowForm(true); };
  const closeForm = () => { setShowForm(false); setEditUser(null); };

>>>>>>> a9dc7840 (Added New FW Management system)
  const columns = [
    { key: 'username', header: 'Benutzername', render: (u: User) => (
      <div><p className="font-medium">{u.username}</p>{u.name && <p className="text-xs text-gray-500">{u.name}</p>}</div>
    )},
    { key: 'email', header: 'E-Mail', render: (u: User) => u.email || '-' },
    { key: 'group', header: 'Gruppe', render: (u: User) => u.group?.name || '-' },
    { key: 'flags', header: 'Rollen', render: (u: User) => (
      <div className="flex gap-1">
        {u.isAdmin && <Badge variant="danger">Admin</Badge>}
        <Badge variant={u.isActive ? 'success' : 'default'}>{u.isActive ? 'Aktiv' : 'Inaktiv'}</Badge>
      </div>
    )},
    { key: 'actions', header: '', render: (u: User) => (
      <div className="flex gap-2">
<<<<<<< HEAD
        <Button size="sm" variant="secondary" onClick={() => { setEditUser(u); setShowCreate(true); }}>Bearbeiten</Button>
        <Button size="sm" variant="danger" onClick={() => deleteMutation.mutate(u.id)} loading={deleteMutation.isPending}>Löschen</Button>
=======
        <Button size="sm" variant="secondary" onClick={(e: React.MouseEvent) => { e.stopPropagation(); openEdit(u); }}>Bearbeiten</Button>
        <Button size="sm" variant="danger" onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(u.id); }} loading={deleteMutation.isPending}>Löschen</Button>
>>>>>>> a9dc7840 (Added New FW Management system)
      </div>
    )},
  ];

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <SearchInput value={search} onChange={setSearch} placeholder="Benutzer suchen..." className="w-64" />
<<<<<<< HEAD
        <Button variant="primary" icon={<PlusIcon />} onClick={() => { setEditUser(null); setShowCreate(true); }}>Neuer Benutzer</Button>
=======
        <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neuer Benutzer</Button>
>>>>>>> a9dc7840 (Added New FW Management system)
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table columns={columns} data={data || []} loading={isLoading} emptyMessage="Keine Benutzer gefunden." keyExtractor={(u) => u.id} />
      </div>

<<<<<<< HEAD
      <UserFormModal isOpen={showCreate} onClose={() => { setShowCreate(false); setEditUser(null); }} editUser={editUser} />
=======
      {showForm && (
        <UserFormModal key={editUser?.id || 'new'} isOpen={showForm} onClose={closeForm} editUser={editUser} />
      )}
>>>>>>> a9dc7840 (Added New FW Management system)
    </div>
  );
}
