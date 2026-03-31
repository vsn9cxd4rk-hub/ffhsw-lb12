import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation } from '@tanstack/react-query';
import { ArrowLeftIcon, CheckIcon } from '@heroicons/react/24/outline';
import { eventsApi } from '../../api/events';
import { Button } from '../../components/ui/Button';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { formatDate, getEventCategoryLabel } from '../../utils/format';

const STATUS_OPTIONS = [
  { value: 'present', label: 'Anwesend', color: 'bg-green-100 text-green-800', ring: 'ring-green-500' },
  { value: 'absent', label: 'Abwesend', color: 'bg-red-100 text-red-800', ring: 'ring-red-500' },
  { value: 'excused', label: 'Entschuldigt', color: 'bg-yellow-100 text-yellow-800', ring: 'ring-yellow-500' },
];

interface MemberItem {
  memberId: number;
  member: { id: number; firstName: string; lastName: string; rank: string | null; group?: { name: string } | null };
  status: string | null;
}

export function AttendancePage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [attendance, setAttendance] = useState<Record<number, string>>({});
  const [saved, setSaved] = useState(false);
  const [error, setError] = useState('');

  const { data, isLoading } = useQuery({
    queryKey: ['event-attendance', id],
    queryFn: () => eventsApi.getAttendance(parseInt(id!)).then((r) => r.data.data),
  });

  useEffect(() => {
    if (data?.members) {
      const initial: Record<number, string> = {};
      data.members.forEach((m: MemberItem) => {
        if (m.status) initial[m.memberId] = m.status;
      });
      setAttendance(initial);
    }
  }, [data]);

  const saveMutation = useMutation({
    mutationFn: () => {
      const records = Object.entries(attendance).map(([memberId, status]) => ({
        memberId: parseInt(memberId),
        status,
      }));
      return eventsApi.updateAttendance(parseInt(id!), records);
    },
    onSuccess: () => { setSaved(true); setError(''); },
    onError: (err: unknown) => {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen';
      setError(msg);
    },
  });

  const setMemberStatus = (memberId: number, status: string) => {
    setSaved(false);
    setAttendance((prev) => {
      if (prev[memberId] === status) {
        const next = { ...prev };
        delete next[memberId];
        return next;
      }
      return { ...prev, [memberId]: status };
    });
  };

  const markAll = (status: string) => {
    setSaved(false);
    const newAttendance: Record<number, string> = {};
    (data?.members as MemberItem[] || []).forEach((m) => {
      newAttendance[m.memberId] = status;
    });
    setAttendance(newAttendance);
  };

  if (isLoading) return <LoadingSpinner />;
  if (!data) return null;

  const { event, members } = data as { event: { name: string; date: string; category: number }; members: MemberItem[] };

  const presentCount = Object.values(attendance).filter(s => s === 'present').length;
  const absentCount = Object.values(attendance).filter(s => s === 'absent').length;
  const excusedCount = Object.values(attendance).filter(s => s === 'excused').length;

  return (
    <div className="space-y-4 max-w-5xl">
      <div className="flex items-center gap-3">
        <Button variant="ghost" onClick={() => navigate(-1)} icon={<ArrowLeftIcon />} />
        <div>
          <h2 className="text-xl font-bold">{event?.name}</h2>
          <p className="text-sm text-gray-500">
            {formatDate(event?.date)} · {getEventCategoryLabel(event?.category)}
          </p>
        </div>
      </div>

      {/* Summary */}
      <div className="flex gap-6 p-4 bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="text-center"><p className="text-2xl font-bold text-green-600">{presentCount}</p><p className="text-xs text-gray-500">Anwesend</p></div>
        <div className="text-center"><p className="text-2xl font-bold text-red-600">{absentCount}</p><p className="text-xs text-gray-500">Abwesend</p></div>
        <div className="text-center"><p className="text-2xl font-bold text-yellow-600">{excusedCount}</p><p className="text-xs text-gray-500">Entschuldigt</p></div>
        <div className="text-center"><p className="text-2xl font-bold text-gray-600">{members?.length || 0}</p><p className="text-xs text-gray-500">Gesamt</p></div>
        <div className="ml-auto flex items-center gap-2">
          <Button size="sm" variant="secondary" onClick={() => markAll('present')}>Alle anwesend</Button>
          <Button size="sm" variant="secondary" onClick={() => markAll('absent')}>Alle abwesend</Button>
        </div>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>
      )}

      {/* Attendance table */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Dienstgrad</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Gruppe</th>
              <th className="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {members?.map((item) => {
              const currentStatus = attendance[item.memberId];
              return (
                <tr key={item.memberId} className="hover:bg-gray-50">
                  <td className="px-6 py-3 text-sm font-medium text-gray-900">
                    {item.member.lastName}, {item.member.firstName}
                  </td>
                  <td className="px-6 py-3 text-sm text-gray-500">
                    {item.member.rank || '-'}
                  </td>
                  <td className="px-6 py-3 text-sm text-gray-500">
                    {item.member.group?.name || '-'}
                  </td>
                  <td className="px-6 py-3">
                    <div className="flex gap-2 justify-center">
                      {STATUS_OPTIONS.map(({ value, label, color, ring }) => (
                        <button
                          key={value}
                          onClick={() => setMemberStatus(item.memberId, value)}
                          className={`px-3 py-1 rounded-full text-xs font-medium transition-all ${
                            currentStatus === value
                              ? `${color} ring-2 ring-offset-1 ${ring}`
                              : 'bg-gray-100 text-gray-400 hover:bg-gray-200 hover:text-gray-600'
                          }`}
                        >
                          {label}
                        </button>
                      ))}
                    </div>
                  </td>
                </tr>
              );
            })}
            {(!members || members.length === 0) && (
              <tr>
                <td colSpan={4} className="px-6 py-8 text-center text-sm text-gray-500">
                  Keine aktiven Mitglieder gefunden.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Save button */}
      <div className="flex justify-end items-center gap-3">
        {saved && <span className="text-sm text-green-600 flex items-center gap-1"><CheckIcon className="h-4 w-4" />Gespeichert</span>}
        <Button variant="primary" onClick={() => saveMutation.mutate()} loading={saveMutation.isPending}>
          Anwesenheit speichern
        </Button>
      </div>
    </div>
  );
}
