import React, { useState, useRef, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { PlusIcon, ArrowDownTrayIcon, TrashIcon, DocumentIcon } from '@heroicons/react/24/outline';
import { trainingApi } from '../../api/training';
import { membersApi } from '../../api/members';
import { Course, Member, CourseCategory } from '../../types';
import { Table } from '../../components/ui/Table';
import { Button } from '../../components/ui/Button';
import { Badge } from '../../components/ui/Badge';
import { SearchInput } from '../../components/ui/SearchInput';
import { Pagination } from '../../components/ui/Pagination';
import { Modal } from '../../components/ui/Modal';
import { Input } from '../../components/ui/Input';
import { Select } from '../../components/ui/Select';
import { Textarea } from '../../components/ui/Textarea';
import { formatDate, getCourseStatusLabel, getCourseStatusColor } from '../../utils/format';

const STATUS_OPTIONS = [
  { value: 'pending', label: 'Geplant' },
  { value: 'active', label: 'Laufend' },
  { value: 'completed', label: 'Abgeschlossen' },
  { value: 'failed', label: 'Nicht bestanden' },
];

function CourseFormModal({ isOpen, onClose, editCourse }: { isOpen: boolean; onClose: () => void; editCourse?: Course | null }) {
  const queryClient = useQueryClient();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [form, setForm] = useState({
    memberId: editCourse?.memberId ? String(editCourse.memberId) : '',
    categoryId: editCourse?.categoryId ? String(editCourse.categoryId) : '',
    status: editCourse?.status || 'pending',
    startDate: editCourse?.startDate?.substring(0, 10) || '',
    endDate: editCourse?.endDate?.substring(0, 10) || '',
    location: editCourse?.location || '',
    notes: editCourse?.notes || '',
  });
  const [error, setError] = useState('');
  const [uploading, setUploading] = useState(false);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  // Load certificate preview for images
  useEffect(() => {
    if (!editCourse?.certificatePath) return;
    const isImage = /\.(jpe?g|png)$/i.test(editCourse.certificatePath);
    if (!isImage) return;
    let url: string | null = null;
    trainingApi.downloadCertificate(editCourse.id).then(res => {
      url = window.URL.createObjectURL(new Blob([res.data]));
      setPreviewUrl(url);
    }).catch(() => {});
    return () => { if (url) window.URL.revokeObjectURL(url); };
  }, [editCourse?.id, editCourse?.certificatePath]);

  const { data: categories } = useQuery({ queryKey: ['course-categories'], queryFn: () => trainingApi.getCategories().then(r => r.data.data as CourseCategory[]) });
  const { data: membersData } = useQuery({ queryKey: ['members-search', ''], queryFn: () => membersApi.getAll({ isInactive: false, limit: 200 }).then(r => r.data.data) });

  const mutation = useMutation({
    mutationFn: () => {
      const data = {
        ...form,
        memberId: parseInt(form.memberId),
        categoryId: parseInt(form.categoryId),
        startDate: form.startDate || null,
        endDate: form.endDate || null,
        notes: form.notes || null,
        location: form.location || null,
      };
      return editCourse
        ? trainingApi.update(editCourse.id, data)
        : trainingApi.create(data);
    },
    onSuccess: () => { queryClient.invalidateQueries({ queryKey: ['courses'] }); setError(''); onClose(); },
    onError: (err: unknown) => {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen');
    },
  });

  const handleCertificateUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file || !editCourse) return;
    setUploading(true);
    setError('');
    try {
      await trainingApi.uploadCertificate(editCourse.id, file);
      queryClient.invalidateQueries({ queryKey: ['courses'] });
    } catch (err: unknown) {
      setError((err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Upload fehlgeschlagen');
    } finally {
      setUploading(false);
      if (fileInputRef.current) fileInputRef.current.value = '';
    }
  };

  const handleCertificateDownload = async () => {
    if (!editCourse) return;
    try {
      const response = await trainingApi.downloadCertificate(editCourse.id);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.download = `Urkunde_${editCourse.category?.name || editCourse.id}.pdf`;
      link.click();
      window.URL.revokeObjectURL(url);
    } catch {
      setError('Download fehlgeschlagen');
    }
  };

  const deleteCertMutation = useMutation({
    mutationFn: () => trainingApi.deleteCertificate(editCourse!.id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['courses'] }),
  });

  const u = (f: string, v: string) => setForm(p => ({ ...p, [f]: v }));
  const isValid = form.memberId && form.categoryId;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={editCourse ? 'Lehrgang bearbeiten' : 'Neuer Lehrgang'} size="lg"
      footer={<><Button variant="secondary" onClick={onClose}>Abbrechen</Button><Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!isValid}>Speichern</Button></>}
    >
      <div className="space-y-3">
        {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Mitglied <span className="text-red-500">*</span></label>
          <select value={form.memberId} onChange={(e) => u('memberId', e.target.value)} disabled={!!editCourse}
            className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white disabled:bg-gray-100">
            <option value="">-- Bitte wählen --</option>
            {(membersData || []).map((m: Member) => <option key={m.id} value={m.id}>{m.lastName}, {m.firstName}</option>)}
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1.5">Lehrgang <span className="text-red-500">*</span></label>
          <select value={form.categoryId} onChange={(e) => u('categoryId', e.target.value)}
            className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
            <option value="">-- Bitte wählen --</option>
            {(categories || []).map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
        </div>

        <Select label="Status" value={form.status} onChange={(e) => u('status', e.target.value)} options={STATUS_OPTIONS} />

        <div className="grid grid-cols-2 gap-3">
          <Input label="Beginn" value={form.startDate} onChange={(e) => u('startDate', e.target.value)} type="date" />
          <Input label="Ende" value={form.endDate} onChange={(e) => u('endDate', e.target.value)} type="date" />
        </div>
        <Input label="Ort" value={form.location} onChange={(e) => u('location', e.target.value)} />
        <Textarea label="Notizen" value={form.notes} onChange={(e) => u('notes', e.target.value)} rows={2} />

        {/* Certificate section - only for existing courses */}
        {editCourse && (
          <div className="border-t border-gray-200 pt-4 mt-2">
            <h4 className="text-sm font-semibold text-gray-700 mb-2">Urkunde / Zertifikat</h4>
            {editCourse.certificatePath ? (
              <div className="p-3 bg-green-50 border border-green-200 rounded-md space-y-3">
                <div className="flex items-center gap-3">
                  {previewUrl ? (
                    <img src={previewUrl} alt="Urkunde" className="h-16 w-16 object-cover rounded border border-green-300" />
                  ) : (
                    <DocumentIcon className="h-5 w-5 text-green-600 flex-shrink-0" />
                  )}
                  <span className="text-sm text-green-700 flex-1">Urkunde vorhanden</span>
                  <Button variant="secondary" size="sm" icon={<ArrowDownTrayIcon />} onClick={handleCertificateDownload}>
                    Herunterladen
                  </Button>
                  <Button variant="danger" size="sm" icon={<TrashIcon />} onClick={() => deleteCertMutation.mutate()} loading={deleteCertMutation.isPending}>
                    Entfernen
                  </Button>
                </div>
              </div>
            ) : (
              <div className="flex items-center gap-3">
                <input ref={fileInputRef} type="file" accept=".pdf,.jpg,.jpeg,.png" onChange={handleCertificateUpload} className="hidden" />
                <Button variant="secondary" size="sm" icon={<PlusIcon />} onClick={() => fileInputRef.current?.click()} loading={uploading}>
                  Urkunde hochladen
                </Button>
                <span className="text-xs text-gray-400">PDF oder Bild (max. 20 MB)</span>
              </div>
            )}
          </div>
        )}
      </div>
    </Modal>
  );
}

export function TrainingPage() {
  const [search, setSearch] = useState('');
  const [categoryId, setCategoryId] = useState('');
  const [status, setStatus] = useState('');
  const [page, setPage] = useState(1);
  const [showForm, setShowForm] = useState(false);
  const [editCourse, setEditCourse] = useState<Course | null>(null);

  const { data: categories } = useQuery({ queryKey: ['course-categories'], queryFn: () => trainingApi.getCategories().then(r => r.data.data) });

  const { data, isLoading } = useQuery({
    queryKey: ['courses', search, categoryId, status, page],
    queryFn: () => trainingApi.getCourses({ search: search || undefined, categoryId: categoryId || undefined, status: status || undefined, page, limit: 20 }).then(r => r.data),
  });

  const openCreate = () => { setEditCourse(null); setShowForm(true); };
  const openEdit = (course: Course) => { setEditCourse(course); setShowForm(true); };
  const closeForm = () => { setShowForm(false); setEditCourse(null); };

  const columns = [
    { key: 'member', header: 'Mitglied', render: (c: Course) => c.member ? `${c.member.lastName}, ${c.member.firstName}` : '-' },
    { key: 'category', header: 'Lehrgang', render: (c: Course) => c.category?.name || '-' },
    { key: 'status', header: 'Status', render: (c: Course) => (
      <span className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${getCourseStatusColor(c.status)}`}>
        {getCourseStatusLabel(c.status)}
      </span>
    )},
    { key: 'dates', header: 'Zeitraum', render: (c: Course) =>
      c.startDate || c.endDate ? `${c.startDate ? formatDate(c.startDate) : '?'} - ${c.endDate ? formatDate(c.endDate) : '?'}` : '-'
    },
    { key: 'location', header: 'Ort', render: (c: Course) => c.location || '-' },
    { key: 'certificate', header: 'Urkunde', render: (c: Course) => c.certificatePath ? (
      <Badge variant="success">Vorhanden</Badge>
    ) : null },
  ];

  return (
    <div className="space-y-4">
      <div className="flex flex-col sm:flex-row gap-3 justify-between">
        <div className="flex gap-3">
          <SearchInput value={search} onChange={setSearch} placeholder="Nach Mitglied suchen..." className="w-48" />
          <select value={categoryId} onChange={(e) => { setCategoryId(e.target.value); setPage(1); }}
            className="border border-gray-300 rounded-md px-3 py-2 text-sm">
            <option value="">Alle Lehrgänge</option>
            {(categories || []).map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
          <select value={status} onChange={(e) => { setStatus(e.target.value); setPage(1); }}
            className="border border-gray-300 rounded-md px-3 py-2 text-sm">
            <option value="">Alle Status</option>
            {STATUS_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
          </select>
        </div>
        <Button variant="primary" icon={<PlusIcon />} onClick={openCreate}>Neuer Lehrgang</Button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table columns={columns} data={data?.data || []} loading={isLoading} emptyMessage="Keine Lehrgänge gefunden."
          keyExtractor={(c) => c.id} onRowClick={(c) => openEdit(c as Course)} />
        {data?.pagination && <Pagination {...data.pagination} onPageChange={setPage} />}
      </div>

      {showForm && (
        <CourseFormModal key={editCourse?.id || 'new'} isOpen={showForm} onClose={closeForm} editCourse={editCourse} />
      )}
    </div>
  );
}
