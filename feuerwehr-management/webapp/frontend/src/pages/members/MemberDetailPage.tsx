import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { ArrowLeftIcon, ChevronLeftIcon, ChevronRightIcon, TrashIcon, PlusIcon } from '@heroicons/react/24/outline';
import { membersApi } from '../../api/members';
import { trainingApi } from '../../api/training';
import { settingsApi } from '../../api/settings';
import { Member, MemberFamily, CourseCategory, Absence, AbsenceReason } from '../../types';
import { Modal } from '../../components/ui/Modal';
import { Button } from '../../components/ui/Button';
import { Input } from '../../components/ui/Input';
import { Select } from '../../components/ui/Select';
import { Textarea } from '../../components/ui/Textarea';
import { Card } from '../../components/ui/Card';
import { Badge } from '../../components/ui/Badge';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { ConfirmDialog } from '../../components/ui/ConfirmDialog';
import { formatDate, getCourseStatusLabel, getCourseStatusColor, getAbsenceReasonColorClasses } from '../../utils/format';

type Tab = 'stammdaten' | 'kontakt' | 'laufbahn' | 'abwesenheiten' | 'untersuchungen' | 'akte';

export function MemberDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [tab, setTab] = useState<Tab>('stammdaten');
  const [deleteOpen, setDeleteOpen] = useState(false);

  const { data: member, isLoading } = useQuery({
    queryKey: ['member', id],
    queryFn: () => membersApi.getById(parseInt(id!)).then((r) => r.data.data),
  });

  const { data: allMemberIds } = useQuery({
    queryKey: ['member-ids-all'],
    queryFn: () => membersApi.getAll({ limit: 10000, isInactive: false }).then((r) => {
      const list: Member[] = r.data.data || [];
      return list.map((m) => m.id);
    }),
    staleTime: 5 * 60 * 1000,
  });

  const memberIds: number[] = allMemberIds || [];
  const currentIndex = memberIds.indexOf(parseInt(id!));
  const prevMemberId = currentIndex > 0 ? memberIds[currentIndex - 1] : null;
  const nextMemberId = currentIndex < memberIds.length - 1 ? memberIds[currentIndex + 1] : null;

  const { data: groups } = useQuery({
    queryKey: ['member-groups'],
    queryFn: () => membersApi.getGroups().then((r) => r.data.data),
  });

  const updateMutation = useMutation({
    mutationFn: (data: Partial<Member>) => membersApi.update(parseInt(id!), data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['member', id] }),
  });

  const deleteMutation = useMutation({
    mutationFn: () => membersApi.delete(parseInt(id!)),
    onSuccess: () => navigate('/members'),
  });

  const updateExamMutation = useMutation({
    mutationFn: (data: Record<string, string | null>) =>
      membersApi.updateExamination(parseInt(id!), data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['member', id] }),
  });

  if (isLoading) return <LoadingSpinner />;
  if (!member) return <div className="text-center py-12 text-gray-500">Mitglied nicht gefunden</div>;

  const tabs: Array<{ id: Tab; label: string }> = [
    { id: 'stammdaten', label: 'Stammdaten' },
    { id: 'kontakt', label: 'Kontakt' },
    { id: 'laufbahn', label: 'Laufbahn' },
    { id: 'abwesenheiten', label: 'Abwesenheiten' },
    { id: 'untersuchungen', label: 'Untersuchungen' },
    { id: 'akte', label: 'Akte' },
  ];

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <Button variant="ghost" onClick={() => navigate('/members')} icon={<ArrowLeftIcon />} />
          <div>
            <h2 className="text-xl font-bold text-gray-900">
              {member.lastName}, {member.firstName}
            </h2>
            <p className="text-sm text-gray-500">{member.rank || 'Kein Dienstgrad'} · {member.group?.name || 'Keine Gruppe'}</p>
          </div>
          <Badge variant={member.isInactive ? 'default' : 'success'}>
            {member.isInactive ? 'Inaktiv' : 'Aktiv'}
          </Badge>
        </div>
        <div className="flex items-center gap-2">
          <Button
            variant="ghost"
            size="sm"
            icon={<ChevronLeftIcon />}
            onClick={() => prevMemberId && navigate(`/members/${prevMemberId}`)}
            disabled={!prevMemberId}
            title="Vorheriges Mitglied"
          />
          <Button
            variant="ghost"
            size="sm"
            icon={<ChevronRightIcon />}
            onClick={() => nextMemberId && navigate(`/members/${nextMemberId}`)}
            disabled={!nextMemberId}
            title="Nächstes Mitglied"
          />
          <Button
            variant="danger"
            size="sm"
            icon={<TrashIcon />}
            onClick={() => setDeleteOpen(true)}
          >
            Löschen
          </Button>
        </div>
      </div>

      {/* Tabs */}
      <div className="border-b border-gray-200">
        <nav className="flex gap-4 -mb-px">
          {tabs.map((t) => (
            <button
              key={t.id}
              onClick={() => setTab(t.id)}
              className={`pb-3 px-1 text-sm font-medium border-b-2 transition-colors ${
                tab === t.id
                  ? 'border-primary-600 text-primary-600'
                  : 'border-transparent text-gray-500 hover:text-gray-700'
              }`}
            >
              {t.label}
            </button>
          ))}
        </nav>
      </div>

      {/* Tab Content */}
      {tab === 'stammdaten' && (
        <StammdatenTab
          member={member}
          groups={groups || []}
          onSave={(data) => updateMutation.mutate(data)}
          saving={updateMutation.isPending}
          prevMemberId={prevMemberId}
          nextMemberId={nextMemberId}
          onNavigate={(memberId) => navigate(`/members/${memberId}`)}
        />
      )}
      {tab === 'untersuchungen' && (
        <UntersuchungenTab examination={member.examination ?? null} onSave={(data) => updateExamMutation.mutate(data)} saving={updateExamMutation.isPending} memberId={parseInt(id!)} birthDate={member.birthDate} />
      )}
      {tab === 'laufbahn' && <LaufbahnTab courses={member.courses || []} memberId={parseInt(id!)} />}
      {tab === 'abwesenheiten' && <AbwesenheitenTab absences={member.absences || []} memberId={parseInt(id!)} />}
      {tab === 'kontakt' && <KontaktTab member={member} memberId={parseInt(id!)} />}
      {tab === 'akte' && <AkteTab memberId={parseInt(id!)} createdAt={member.createdAt} />}

      <ConfirmDialog
        isOpen={deleteOpen}
        onClose={() => setDeleteOpen(false)}
        onConfirm={() => deleteMutation.mutate()}
        loading={deleteMutation.isPending}
        title="Mitglied löschen"
        message={`Möchten Sie ${member.firstName} ${member.lastName} wirklich löschen?`}
      />
    </div>
  );
}

function StammdatenTab({ member, groups, onSave, saving, prevMemberId, nextMemberId, onNavigate }: {
  member: Member;
  groups: Array<{ id: number; name: string }>;
  onSave: (data: Partial<Member>) => void;
  saving: boolean;
  prevMemberId: number | null;
  nextMemberId: number | null;
  onNavigate: (memberId: number) => void;
}) {
  const [form, setForm] = useState({
    salutation: member.salutation || '',
    firstName: member.firstName,
    lastName: member.lastName,
    rank: member.rank || '',
    groupId: member.groupId ? String(member.groupId) : '',
    street: member.street || '',
    city: member.city || '',
    phonePrivate: member.phonePrivate || '',
    phoneMobile: member.phoneMobile || '',
    email: member.email || '',
    birthDate: member.birthDate ? member.birthDate.substring(0, 10) : '',
    memberSince: member.memberSince ? member.memberSince.substring(0, 10) : '',
    isInactive: member.isInactive,
    comment: member.comment || '',
    qualLicenseC: member.qualLicenseC ?? false,
    qualLicenseB: member.qualLicenseB ?? false,
    qualFirstAid: member.qualFirstAid ?? false,
    qualRadioOperator: member.qualRadioOperator ?? false,
    qualMachinist: member.qualMachinist ?? false,
    qualTruppmann: member.qualTruppmann ?? false,
    qualTruppfuehrer: member.qualTruppfuehrer ?? false,
    qualGruppenfuehrer: member.qualGruppenfuehrer ?? false,
    qualZugfuehrer: member.qualZugfuehrer ?? false,
    qualRettSan: member.qualRettSan ?? false,
    qualFwSan: member.qualFwSan ?? false,
    qualVerbandfuehrer: member.qualVerbandfuehrer ?? false,
    qualAGT: member.qualAGT ?? false,
    qualTH1: member.qualTH1 ?? false,
  });

  const update = (field: string, value: string | boolean) =>
    setForm((prev) => ({ ...prev, [field]: value }));

  const handleSave = () => {
    onSave({
      ...form,
      groupId: form.groupId ? parseInt(form.groupId) : null,
      birthDate: form.birthDate || null,
      memberSince: form.memberSince || null,
    } as Partial<Member>);
  };

  return (
    <Card>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Select label="Anrede" value={form.salutation} onChange={(e) => update('salutation', e.target.value)}
          options={[{ value: 'Herr', label: 'Herr' }, { value: 'Frau', label: 'Frau' }]}
          placeholder="Bitte wählen" />
        <Input label="Vorname" value={form.firstName} onChange={(e) => update('firstName', e.target.value)} required />
        <Input label="Nachname" value={form.lastName} onChange={(e) => update('lastName', e.target.value)} required />
        <Input label="Dienstgrad" value={form.rank} onChange={(e) => update('rank', e.target.value)} />
        <Select label="Gruppe" value={form.groupId} onChange={(e) => update('groupId', e.target.value)}
          options={groups.map((g) => ({ value: g.id, label: g.name }))}
          placeholder="Keine Gruppe" />
        <Input label="Straße" value={form.street} onChange={(e) => update('street', e.target.value)} />
        <Input label="Ort" value={form.city} onChange={(e) => update('city', e.target.value)} />
        <Input label="Telefon Privat" value={form.phonePrivate} onChange={(e) => update('phonePrivate', e.target.value)} type="tel" />
        <Input label="Mobil" value={form.phoneMobile} onChange={(e) => update('phoneMobile', e.target.value)} type="tel" />
        <Input label="E-Mail" value={form.email} onChange={(e) => update('email', e.target.value)} type="email" />
        <Input label="Geburtsdatum" value={form.birthDate} onChange={(e) => update('birthDate', e.target.value)} type="date" />
        <Input label="Mitglied seit" value={form.memberSince} onChange={(e) => update('memberSince', e.target.value)} type="date" />
        <div className="col-span-full">
          <Textarea label="Kommentar" value={form.comment} onChange={(e) => update('comment', e.target.value)} rows={3} />
        </div>
        <div className="col-span-full flex items-center gap-2">
          <input type="checkbox" id="isInactive" checked={form.isInactive}
            onChange={(e) => update('isInactive', e.target.checked)} className="rounded" />
          <label htmlFor="isInactive" className="text-sm text-gray-700">Außer Dienst</label>
        </div>

        {/* Qualifikationen */}
        <div className="col-span-full border-t border-gray-200 pt-4 mt-2">
          <h4 className="text-sm font-semibold text-gray-700 mb-3">Qualifikationen</h4>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-2">
            {([
              ['qualLicenseB', 'Führerscheinklasse B'],
              ['qualLicenseC', 'Führerscheinklasse C'],
              ['qualFirstAid', 'Erste Hilfe'],
              ['qualRadioOperator', 'Sprechfunker'],
              ['qualMachinist', 'Maschinist'],
              ['qualTruppmann', 'Truppmann'],
              ['qualTruppfuehrer', 'Truppführer'],
              ['qualGruppenfuehrer', 'Gruppenführer'],
              ['qualZugfuehrer', 'Zugführer'],
              ['qualRettSan', 'RettSan'],
              ['qualFwSan', 'FwSan'],
              ['qualVerbandfuehrer', 'Verbandsführer'],
              ['qualAGT', 'AGT'],
              ['qualTH1', 'TH1'],
            ] as const).map(([key, label]) => (
              <div key={key} className="flex items-center gap-2">
                <input type="checkbox" id={key} checked={(form as Record<string, unknown>)[key] as boolean}
                  onChange={(e) => update(key, e.target.checked)} className="rounded" />
                <label htmlFor={key} className="text-sm text-gray-700">{label}</label>
              </div>
            ))}
          </div>
        </div>
      </div>
      <div className="mt-6 flex justify-end items-center gap-2">
        <Button
          variant="ghost"
          size="sm"
          icon={<ChevronLeftIcon />}
          onClick={() => prevMemberId && onNavigate(prevMemberId)}
          disabled={!prevMemberId}
          title="Vorheriges Mitglied"
        />
        <Button
          variant="ghost"
          size="sm"
          icon={<ChevronRightIcon />}
          onClick={() => nextMemberId && onNavigate(nextMemberId)}
          disabled={!nextMemberId}
          title="Nächstes Mitglied"
        />
        <Button variant="primary" onClick={handleSave} loading={saving}>Speichern</Button>
      </div>
    </Card>
  );
}

const AGT_TYPES = [
  { value: 'g26', label: 'G26 Untersuchung' },
  { value: 'belastung', label: 'AGT Belastungsübung' },
  { value: 'einsatzuebung', label: 'AGT Einsatzübung' },
  { value: 'einsatz', label: 'AGT Einsatz' },
];

function getAgtTypeLabel(type: string): string {
  return AGT_TYPES.find(t => t.value === type)?.label || type;
}

function calculateAgtStatus(records: Array<{ type: string; date: string; result?: string | null }>, birthDate: string | null, g26Date: string | null): { tauglich: boolean; reasons: string[] } {
  const now = new Date();
  const reasons: string[] = [];

  // G26 gilt sowohl über einen "Nachweis"-Eintrag (Typ g26, Ergebnis geeignet) als auch
  // über das einfache Feld "G26 (Atemschutz)" unter Untersuchungen - der jeweils
  // aktuellere Stand zählt, damit man den Nachweis nicht doppelt pflegen muss.
  const g26Candidates = records.filter(r => r.type === 'g26' && r.result === 'geeignet');
  if (g26Date) g26Candidates.push({ type: 'g26', date: g26Date, result: 'geeignet' });
  const latestG26 = g26Candidates.sort((a, b) => b.date.localeCompare(a.date))[0];
  const latestBelastung = records.filter(r => r.type === 'belastung').sort((a, b) => b.date.localeCompare(a.date))[0];
  const latestEinsatzuebung = records.filter(r => r.type === 'einsatzuebung').sort((a, b) => b.date.localeCompare(a.date))[0];
  const latestEinsatz = records.filter(r => r.type === 'einsatz').sort((a, b) => b.date.localeCompare(a.date))[0];

  if (!latestG26) { return { tauglich: false, reasons: ['Keine gültige G26 Untersuchung vorhanden'] }; }

  const age = birthDate ? Math.floor((now.getTime() - new Date(birthDate).getTime()) / (365.25 * 24 * 60 * 60 * 1000)) : 0;
  const g26Age = (now.getTime() - new Date(latestG26.date).getTime()) / (365.25 * 24 * 60 * 60 * 1000);

  if (age < 50 && g26Age > 3) reasons.push('G26 älter als 3 Jahre (Alter < 50)');
  if (age >= 50 && g26Age > 1) reasons.push('G26 älter als 1 Jahr (Alter >= 50)');

  const belastungAge = latestBelastung ? (now.getTime() - new Date(latestBelastung.date).getTime()) / (365.25 * 24 * 60 * 60 * 1000) : Infinity;
  if (belastungAge > 1) reasons.push('Belastungsübung älter als 1 Jahr');

  const einsatzuebungAge = latestEinsatzuebung ? (now.getTime() - new Date(latestEinsatzuebung.date).getTime()) / (365.25 * 24 * 60 * 60 * 1000) : Infinity;
  const einsatzAge = latestEinsatz ? (now.getTime() - new Date(latestEinsatz.date).getTime()) / (365.25 * 24 * 60 * 60 * 1000) : Infinity;

  if (einsatzuebungAge > 1 && einsatzAge > 1) reasons.push('Weder Einsatzübung noch Einsatz innerhalb 1 Jahr');

  return { tauglich: reasons.length === 0, reasons };
}

function UntersuchungenTab({ examination, onSave, saving, memberId, birthDate }: {
  examination: { g25Date: string | null; g26Date: string | null; g30Date: string | null; agtTrainingDate: string | null; lkwLicenseExpiry: string | null } | null;
  onSave: (data: Record<string, string | null>) => void;
  saving: boolean;
  memberId: number;
  birthDate: string | null;
}) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    g25Date: examination?.g25Date?.substring(0, 10) || '',
    g26Date: examination?.g26Date?.substring(0, 10) || '',
    g30Date: examination?.g30Date?.substring(0, 10) || '',
    agtTrainingDate: examination?.agtTrainingDate?.substring(0, 10) || '',
    lkwLicenseExpiry: examination?.lkwLicenseExpiry?.substring(0, 10) || '',
  });

  const [agtForm, setAgtForm] = useState({ type: 'g26', date: '', result: '', notes: '' });
  const [showAgtForm, setShowAgtForm] = useState(false);

  const { data: agtRecords } = useQuery({
    queryKey: ['agt-records', memberId],
    queryFn: () => membersApi.getAgtRecords(memberId).then(r => r.data.data),
  });

  const createAgtMutation = useMutation({
    mutationFn: () => membersApi.createAgtRecord(memberId, {
      type: agtForm.type,
      date: agtForm.date,
      result: agtForm.result || undefined,
      notes: agtForm.notes || undefined,
    }),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['agt-records', memberId] }); setShowAgtForm(false); setAgtForm({ type: 'g26', date: '', result: '', notes: '' }); },
  });

  const deleteAgtMutation = useMutation({
    mutationFn: (recordId: number) => membersApi.deleteAgtRecord(memberId, recordId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['agt-records', memberId] }),
  });

  const agtStatus = calculateAgtStatus(agtRecords || [], birthDate, examination?.g26Date || null);

  return (
    <div className="space-y-6">
      <Card title="Arbeitsmedizinische Untersuchungen">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Input label="G25 (Fahrtauglichkeit)" value={form.g25Date} onChange={(e) => setForm(f => ({ ...f, g25Date: e.target.value }))} type="date" />
          <Input label="G26 (Atemschutz)" value={form.g26Date} onChange={(e) => setForm(f => ({ ...f, g26Date: e.target.value }))} type="date" />
          <Input label="G30 (Gefahrstoffe)" value={form.g30Date} onChange={(e) => setForm(f => ({ ...f, g30Date: e.target.value }))} type="date" />
          <Input label="AGT Ausbildung" value={form.agtTrainingDate} onChange={(e) => setForm(f => ({ ...f, agtTrainingDate: e.target.value }))} type="date" />
          <Input label="LKW-Führerschein gültig bis" value={form.lkwLicenseExpiry} onChange={(e) => setForm(f => ({ ...f, lkwLicenseExpiry: e.target.value }))} type="date" />
        </div>
        <div className="mt-6 flex justify-end">
          <Button variant="primary" onClick={() => onSave({
            g25Date: form.g25Date || null,
            g26Date: form.g26Date || null,
            g30Date: form.g30Date || null,
            agtTrainingDate: form.agtTrainingDate || null,
            lkwLicenseExpiry: form.lkwLicenseExpiry || null,
          })} loading={saving}>Speichern</Button>
        </div>
      </Card>

      <Card title="AGT-Tauglichkeit">
        <div className={`mb-4 p-4 rounded-lg border ${agtStatus.tauglich ? 'bg-green-50 border-green-200' : 'bg-red-50 border-red-200'}`}>
          <div className="flex items-center gap-2">
            <span className={`inline-block h-3 w-3 rounded-full ${agtStatus.tauglich ? 'bg-green-500' : 'bg-red-500'}`} />
            <span className={`font-semibold ${agtStatus.tauglich ? 'text-green-800' : 'text-red-800'}`}>
              {agtStatus.tauglich ? 'AGT tauglich' : 'AGT nicht tauglich'}
            </span>
          </div>
          {agtStatus.reasons.length > 0 && (
            <ul className="mt-2 text-sm text-red-700 list-disc list-inside">
              {agtStatus.reasons.map((r, i) => <li key={i}>{r}</li>)}
            </ul>
          )}
        </div>

        <div className="flex justify-between items-center mb-3">
          <h4 className="text-sm font-medium text-gray-700">Nachweise</h4>
          <Button variant="secondary" size="sm" icon={<PlusIcon />} onClick={() => setShowAgtForm(true)}>Eintrag hinzufügen</Button>
        </div>

        {showAgtForm && (
          <div className="border border-gray-200 rounded-lg p-4 mb-4 bg-gray-50 space-y-3">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Typ</label>
                <select value={agtForm.type} onChange={(e) => setAgtForm(f => ({ ...f, type: e.target.value }))}
                  className="block w-full px-3 py-2 border border-gray-300 rounded-md text-sm">
                  {AGT_TYPES.map(t => <option key={t.value} value={t.value}>{t.label}</option>)}
                </select>
              </div>
              <Input label="Datum" value={agtForm.date} onChange={(e) => setAgtForm(f => ({ ...f, date: e.target.value }))} type="date" required />
              {agtForm.type === 'g26' && (
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Ergebnis</label>
                  <select value={agtForm.result} onChange={(e) => setAgtForm(f => ({ ...f, result: e.target.value }))}
                    className="block w-full px-3 py-2 border border-gray-300 rounded-md text-sm">
                    <option value="">-- Bitte wählen --</option>
                    <option value="geeignet">Geeignet</option>
                    <option value="nicht_geeignet">Nicht geeignet</option>
                  </select>
                </div>
              )}
              <Input label="Bemerkung" value={agtForm.notes} onChange={(e) => setAgtForm(f => ({ ...f, notes: e.target.value }))} />
            </div>
            <div className="flex gap-2">
              <Button variant="primary" onClick={() => createAgtMutation.mutate()} loading={createAgtMutation.isPending} disabled={!agtForm.date}>Speichern</Button>
              <Button variant="secondary" onClick={() => setShowAgtForm(false)}>Abbrechen</Button>
            </div>
          </div>
        )}

        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200 text-sm">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left font-medium text-gray-500">Typ</th>
                <th className="px-4 py-2 text-left font-medium text-gray-500">Datum</th>
                <th className="px-4 py-2 text-left font-medium text-gray-500">Ergebnis</th>
                <th className="px-4 py-2 text-left font-medium text-gray-500">Bemerkung</th>
                <th className="px-4 py-2"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {(agtRecords || []).map((rec: { id: number; type: string; date: string; result: string | null; notes: string | null }) => (
                <tr key={rec.id}>
                  <td className="px-4 py-2">{getAgtTypeLabel(rec.type)}</td>
                  <td className="px-4 py-2">{formatDate(rec.date)}</td>
                  <td className="px-4 py-2">
                    {rec.result === 'geeignet' && <Badge variant="success">Geeignet</Badge>}
                    {rec.result === 'nicht_geeignet' && <Badge variant="danger">Nicht geeignet</Badge>}
                    {!rec.result && '-'}
                  </td>
                  <td className="px-4 py-2 text-gray-500">{rec.notes || '-'}</td>
                  <td className="px-4 py-2">
                    <button onClick={() => deleteAgtMutation.mutate(rec.id)} className="text-red-500 hover:text-red-700 text-xs">Löschen</button>
                  </td>
                </tr>
              ))}
              {(!agtRecords || agtRecords.length === 0) && (
                <tr><td colSpan={5} className="px-4 py-4 text-center text-gray-400">Keine AGT-Nachweise vorhanden</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  );
}

function LaufbahnTab({ courses, memberId }: { courses: Array<{ id: number; category?: { name: string }; status: string; startDate: string | null; endDate: string | null; location: string | null }>; memberId: number }) {
  const queryClient = useQueryClient();
  const [showAdd, setShowAdd] = useState(false);
  const [form, setForm] = useState({ categoryId: '', status: 'completed', startDate: '', endDate: '', location: '', notes: '' });
  const [error, setError] = useState('');

  const { data: categories } = useQuery({ queryKey: ['course-categories'], queryFn: () => trainingApi.getCategories().then(r => r.data.data as CourseCategory[]) });

  const mutation = useMutation({
    mutationFn: () => membersApi.createCourse(memberId, {
      ...form,
      categoryId: parseInt(form.categoryId),
      startDate: form.startDate || null,
      endDate: form.endDate || null,
    } as Record<string, unknown>),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['member', String(memberId)] });
      setShowAdd(false);
      setForm({ categoryId: '', status: 'completed', startDate: '', endDate: '', location: '', notes: '' });
      setError('');
    },
    onError: (err: unknown) => {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen');
    },
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <>
      <Card title="Lehrgänge & Ausbildungen"
        actions={<Button variant="primary" icon={<PlusIcon />} onClick={() => setShowAdd(true)}>Lehrgang hinzufügen</Button>}
      >
        {courses.length === 0 ? (
          <p className="text-sm text-gray-500">Keine Lehrgänge eingetragen.</p>
        ) : (
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Lehrgang</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Ort</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Datum</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {courses.map((c) => (
                <tr key={c.id}>
                  <td className="px-4 py-2 text-sm font-medium">{c.category?.name || 'Unbekannt'}</td>
                  <td className="px-4 py-2"><span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${getCourseStatusColor(c.status)}`}>{getCourseStatusLabel(c.status)}</span></td>
                  <td className="px-4 py-2 text-sm text-gray-500">{c.location || '-'}</td>
                  <td className="px-4 py-2 text-sm text-gray-500">{c.endDate ? formatDate(c.endDate) : c.startDate ? formatDate(c.startDate) : '-'}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Card>

      <Modal isOpen={showAdd} onClose={() => setShowAdd(false)} title="Lehrgang hinzufügen" size="md"
        footer={<><Button variant="secondary" onClick={() => setShowAdd(false)}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.categoryId}>Speichern</Button></>}
      >
        <div className="space-y-3">
          {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Lehrgang</label>
            <select value={form.categoryId} onChange={(e) => u('categoryId', e.target.value)}
              className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
              <option value="">-- Bitte wählen --</option>
              {(categories || []).map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
          </div>
          <Select label="Status" value={form.status} onChange={(e) => u('status', e.target.value)}
            options={[{ value: 'pending', label: 'Geplant' }, { value: 'active', label: 'Laufend' }, { value: 'completed', label: 'Abgeschlossen' }, { value: 'failed', label: 'Nicht bestanden' }]} />
          <div className="grid grid-cols-2 gap-3">
            <Input label="Beginn" value={form.startDate} onChange={(e) => u('startDate', e.target.value)} type="date" />
            <Input label="Ende" value={form.endDate} onChange={(e) => u('endDate', e.target.value)} type="date" />
          </div>
          <Input label="Ort" value={form.location} onChange={(e) => u('location', e.target.value)} />
        </div>
      </Modal>
    </>
  );
}

function AbwesenheitenTab({ absences, memberId }: { absences: Absence[]; memberId: number }) {
  const queryClient = useQueryClient();
  const [showForm, setShowForm] = useState(false);
  const [editEntry, setEditEntry] = useState<Absence | undefined>();

  const { data: reasons } = useQuery({
    queryKey: ['absence-reasons'],
    queryFn: () => settingsApi.getAbsenceReasons().then(r => r.data.data),
  });
  const reasonById = new Map((reasons || []).map(r => [r.id, r]));

  const deleteMutation = useMutation({
    mutationFn: (absenceId: number) => membersApi.deleteAbsence(memberId, absenceId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['member', String(memberId)] }),
  });

  const openAdd = () => { setEditEntry(undefined); setShowForm(true); };
  const openEdit = (a: Absence) => { setEditEntry(a); setShowForm(true); };
  const closeForm = () => { setShowForm(false); setEditEntry(undefined); };

  return (
    <>
      <Card title="Abwesenheiten"
        actions={<Button variant="primary" icon={<PlusIcon />} onClick={openAdd}>Abwesenheit erfassen</Button>}
      >
        {absences.length === 0 ? (
          <p className="text-sm text-gray-500">Keine Abwesenheiten erfasst.</p>
        ) : (
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Datum</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Grund</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Notiz</th>
                <th className="px-4 py-2"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {absences.map((a) => {
                const reason = reasonById.get(a.reason);
                return (
                  <tr key={a.id} className="hover:bg-gray-50 cursor-pointer" onClick={() => openEdit(a)}>
                    <td className="px-4 py-2 text-sm font-medium">{formatDate(a.date)}</td>
                    <td className="px-4 py-2">
                      <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${getAbsenceReasonColorClasses(reason?.color)}`}>
                        {reason?.name || 'Unbekannt'}
                      </span>
                    </td>
                    <td className="px-4 py-2 text-sm text-gray-500">{a.notes || '-'}</td>
                    <td className="px-4 py-2 text-right">
                      <Button variant="danger" size="sm" onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteMutation.mutate(a.id); }}>
                        <TrashIcon className="h-4 w-4" />
                      </Button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </Card>

      <AbsenceFormModal isOpen={showForm} onClose={closeForm} memberId={memberId} entry={editEntry} reasons={reasons || []} />
    </>
  );
}

function AbsenceFormModal({ isOpen, onClose, memberId, entry, reasons }: {
  isOpen: boolean; onClose: () => void; memberId: number; entry?: Absence; reasons: AbsenceReason[];
}) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    date: entry?.date?.substring(0, 10) || '',
    reason: entry?.reason !== undefined ? String(entry.reason) : '',
    notes: entry?.notes || '',
  });
  const [error, setError] = useState('');

  const mutation = useMutation({
    mutationFn: () => {
      const data = { date: form.date, reason: parseInt(form.reason), notes: form.notes || null };
      return entry
        ? membersApi.updateAbsence(memberId, entry.id, data)
        : membersApi.createAbsence(memberId, data);
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['member', String(memberId)] });
      onClose();
    },
    onError: (err: unknown) => {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen');
    },
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));
  const isValid = form.date && form.reason;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={entry ? 'Abwesenheit bearbeiten' : 'Abwesenheit erfassen'} size="md"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!isValid}>Speichern</Button></>}
    >
      <div className="space-y-3">
        {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}
        <Input label="Datum" value={form.date} onChange={(e) => u('date', e.target.value)} type="date" required />
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Grund</label>
          <select value={form.reason} onChange={(e) => u('reason', e.target.value)}
            className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
            <option value="">-- Bitte wählen --</option>
            {reasons.map(r => <option key={r.id} value={r.id}>{r.name}</option>)}
          </select>
        </div>
        <Textarea label="Notiz" value={form.notes} onChange={(e) => u('notes', e.target.value)} rows={3} />
      </div>
    </Modal>
  );
}

function FamilyFormModal({ isOpen, onClose, memberId, entry }: { isOpen: boolean; onClose: () => void; memberId: number; entry?: MemberFamily }) {
  const queryClient = useQueryClient();
  const [form, setForm] = useState({
    name: entry?.name || '', relationship: entry?.relationship || '',
    phone: entry?.phone || '', phone2: entry?.phone2 || '',
    email: entry?.email || '', street: entry?.street || '', city: entry?.city || '',
  });
  const [error, setError] = useState('');

  const mutation = useMutation({
    mutationFn: () => entry
      ? membersApi.updateFamily(memberId, entry.id, form)
      : membersApi.createFamily(memberId, form),
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['member', String(memberId)] }); setError(''); onClose(); },
    onError: (err: unknown) => {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen');
    },
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={entry ? 'Angehörigen bearbeiten' : 'Angehörigen hinzufügen'} size="md"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!form.name}>Speichern</Button></>}
    >
      <div className="space-y-3">
        {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}
        <div className="grid grid-cols-2 gap-3">
          <Input label="Name" value={form.name} onChange={(e) => u('name', e.target.value)} required />
          <Input label="Beziehung" value={form.relationship} onChange={(e) => u('relationship', e.target.value)} placeholder="z.B. Ehepartner, Kind" />
        </div>
        <div className="grid grid-cols-2 gap-3">
          <Input label="Telefon" value={form.phone} onChange={(e) => u('phone', e.target.value)} type="tel" />
          <Input label="Telefon 2" value={form.phone2} onChange={(e) => u('phone2', e.target.value)} type="tel" />
        </div>
        <Input label="E-Mail" value={form.email} onChange={(e) => u('email', e.target.value)} type="email" />
        <div className="grid grid-cols-2 gap-3">
          <Input label="Straße" value={form.street} onChange={(e) => u('street', e.target.value)} />
          <Input label="Ort" value={form.city} onChange={(e) => u('city', e.target.value)} />
        </div>
      </div>
    </Modal>
  );
}

function KontaktTab({ member, memberId }: { member: Member; memberId: number }) {
  const queryClient = useQueryClient();
  const [showFamilyForm, setShowFamilyForm] = useState(false);
  const [editFamily, setEditFamily] = useState<MemberFamily | undefined>();

  const deleteFamilyMutation = useMutation({
    mutationFn: (familyId: number) => membersApi.deleteFamily(memberId, familyId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['member', String(memberId)] }),
  });

  const openAdd = () => { setEditFamily(undefined); setShowFamilyForm(true); };
  const openEdit = (f: MemberFamily) => { setEditFamily(f); setShowFamilyForm(true); };
  const closeForm = () => { setShowFamilyForm(false); setEditFamily(undefined); };

  return (
    <div className="space-y-4">
      {member.work && (
        <Card title="Arbeitgeber">
          <dl className="grid grid-cols-2 gap-2 text-sm">
            <dt className="text-gray-500">Firma</dt><dd>{member.work.employer || '-'}</dd>
            <dt className="text-gray-500">Ort</dt><dd>{member.work.city || '-'}</dd>
            <dt className="text-gray-500">Telefon</dt><dd>{member.work.phone || '-'}</dd>
          </dl>
        </Card>
      )}
      <Card title="Angehörige"
        actions={<Button variant="primary" icon={<PlusIcon />} onClick={openAdd}>Hinzufügen</Button>}
      >
        {(!member.family || member.family.length === 0) ? (
          <p className="text-sm text-gray-500">Keine Angehörigen eingetragen.</p>
        ) : (
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Beziehung</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Telefon</th>
                <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">E-Mail</th>
                <th className="px-4 py-2"></th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {member.family.map((f) => (
                <tr key={f.id} className="hover:bg-gray-50 cursor-pointer" onClick={() => openEdit(f)}>
                  <td className="px-4 py-2 text-sm font-medium">{f.name}</td>
                  <td className="px-4 py-2 text-sm text-gray-500">{f.relationship || '-'}</td>
                  <td className="px-4 py-2 text-sm text-gray-500">{f.phone || '-'}</td>
                  <td className="px-4 py-2 text-sm text-gray-500">{f.email || '-'}</td>
                  <td className="px-4 py-2 text-right">
                    <Button variant="danger" size="sm" onClick={(e: React.MouseEvent) => { e.stopPropagation(); deleteFamilyMutation.mutate(f.id); }}>
                      <TrashIcon className="h-4 w-4" />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </Card>

      <FamilyFormModal isOpen={showFamilyForm} onClose={closeForm} memberId={memberId} entry={editFamily} />
    </div>
  );
}

function AkteTab({ memberId, createdAt }: { memberId: number; createdAt: string }) {
  const { data: history } = useQuery({
    queryKey: ['member-history', memberId],
    queryFn: () => membersApi.getHistory(memberId).then(r => r.data.data as Array<{ id: number; field: string; oldValue: string | null; newValue: string | null; changedBy: string; changedAt: string }>),
  });

  return (
    <Card title="Änderungshistorie">
      {(!history || history.length === 0) ? (
        <p className="text-sm text-gray-500">Keine Einträge vorhanden.</p>
      ) : (
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Datum</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Feld</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Alt</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Neu</th>
              <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Benutzer</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {history.map((h) => (
              <tr key={h.id}>
                <td className="px-4 py-2 text-sm text-gray-500">{formatDate(h.changedAt)}</td>
                <td className="px-4 py-2 text-sm font-medium">{h.field}</td>
                <td className="px-4 py-2 text-sm text-gray-500">{h.oldValue || '-'}</td>
                <td className="px-4 py-2 text-sm text-gray-500">{h.newValue || '-'}</td>
                <td className="px-4 py-2 text-sm text-gray-500">{h.changedBy}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </Card>
  );
}
