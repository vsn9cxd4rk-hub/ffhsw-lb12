import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { PlusIcon, ArrowDownTrayIcon } from '@heroicons/react/24/outline';
import { membersApi } from '../../api/members';
import { Member } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { SearchInput } from '../../components/ui/SearchInput';
import { Badge } from '../../components/ui/Badge';
import { Pagination } from '../../components/ui/Pagination';
import { Select } from '../../components/ui/Select';

export function MembersPage() {
  const navigate = useNavigate();
  const [search, setSearch] = useState('');
  const [groupId, setGroupId] = useState('');
  const [showInactive, setShowInactive] = useState(false);
  const [page, setPage] = useState(1);

  const { data: groupsData } = useQuery({
    queryKey: ['member-groups'],
    queryFn: () => membersApi.getGroups().then((r) => r.data.data),
  });

  const { data, isLoading } = useQuery({
    queryKey: ['members', search, groupId, showInactive, page],
    queryFn: () =>
      membersApi
        .getAll({
          search: search || undefined,
          groupId: groupId || undefined,
          isInactive: showInactive ? undefined : false,
          page,
          limit: 20,
        })
        .then((r) => r.data),
  });

  const columns = [
    {
      key: 'name',
      header: 'Name',
      render: (row: Member) => (
        <div>
          <p className="font-medium">{row.lastName}, {row.firstName}</p>
          {row.email && <p className="text-xs text-gray-500">{row.email}</p>}
        </div>
      ),
    },
    { key: 'rank', header: 'Dienstgrad', render: (row: Member) => row.rank || '-' },
    {
      key: 'group',
      header: 'Gruppe',
      render: (row: Member) => row.group?.name || '-',
    },
    {
      key: 'phoneMobile',
      header: 'Telefon',
      render: (row: Member) => row.phoneMobile || row.phonePrivate || '-',
    },
    {
      key: 'status',
      header: 'Status',
      render: (row: Member) => (
        <Badge variant={row.isInactive ? 'default' : 'success'}>
          {row.isInactive ? 'Inaktiv' : 'Aktiv'}
        </Badge>
      ),
    },
  ];

  const members: Member[] = data?.data || [];
  const pagination = data?.pagination;

  return (
    <div className="space-y-4">
      {/* Toolbar */}
      <div className="flex flex-col sm:flex-row gap-3 justify-between">
        <div className="flex gap-3 flex-1">
          <SearchInput
            value={search}
            onChange={setSearch}
            placeholder="Nach Name suchen..."
            className="flex-1 max-w-xs"
          />
          <Select
            options={[
              { value: '', label: 'Alle Gruppen' },
              ...(groupsData || []).map((g) => ({ value: g.id, label: g.name })),
            ]}
            value={groupId}
            onChange={(e) => { setGroupId(e.target.value); setPage(1); }}
            className="w-40"
          />
          <label className="flex items-center gap-2 text-sm text-gray-700 cursor-pointer">
            <input
              type="checkbox"
              checked={showInactive}
              onChange={(e) => { setShowInactive(e.target.checked); setPage(1); }}
              className="rounded"
            />
            Inaktive zeigen
          </label>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="secondary" size="sm" icon={<ArrowDownTrayIcon />}
            onClick={() => {
              membersApi.getAll({ limit: 10000 }).then(r => {
                const list: Member[] = r.data.data || [];
                const headers = ['lastName','firstName','salutation','group','rank','street','city','phonePrivate','phoneMobile','phoneWork','email','email2','birthDate','memberSince','occupation','nationality','driverLicenseNo','serviceCardNo','healthInsurance','status','comment'];
                const rows = list.map(m => [
                  m.lastName, m.firstName, m.salutation || '', m.group?.name || '', m.rank || '',
                  m.street || '', m.city || '', m.phonePrivate || '', m.phoneMobile || '', m.phoneWork || '',
                  m.email || '', m.email2 || '',
                  m.birthDate ? m.birthDate.split('T')[0] : '',
                  m.memberSince ? m.memberSince.split('T')[0] : '',
                  m.occupation || '', m.nationality || '',
                  m.driverLicenseNo || '', m.serviceCardNo || '', m.healthInsurance || '',
                  m.isInactive ? 'Inaktiv' : 'Aktiv', m.comment || '',
                ]);
                const csv = [headers, ...rows].map(row => row.map(c => '"' + String(c).replace(/"/g, '""') + '"').join(';')).join('\n');
                const blob = new Blob(['﻿' + csv], { type: 'text/csv;charset=utf-8;' });
                const url = URL.createObjectURL(blob);
                const a = document.createElement('a'); a.href = url; a.download = 'Mitgliederliste_' + new Date().toISOString().split('T')[0] + '.csv'; a.click();
                URL.revokeObjectURL(url);
              });
            }}>
            CSV Export
          </Button>
          <Button
            variant="primary"
            icon={<PlusIcon />}
            onClick={() => navigate('/members/new')}
          >
            Neues Mitglied
          </Button>
        </div>
      </div>

      {/* Table */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table
          columns={columns}
          data={members}
          loading={isLoading}
          emptyMessage="Keine Mitglieder gefunden."
          onRowClick={(row) => navigate(`/members/${row.id}`)}
          keyExtractor={(row) => row.id}
        />
        {pagination && (
          <Pagination
            page={pagination.page}
            pages={pagination.pages}
            total={pagination.total}
            limit={pagination.limit}
            onPageChange={setPage}
          />
        )}
      </div>
    </div>
  );
}
