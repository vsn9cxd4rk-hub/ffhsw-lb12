import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { ArrowDownTrayIcon } from '@heroicons/react/24/outline';
import { DeviceClass, ArticleInspection } from '../../types';
import { Table } from '../ui/Table';
import { Button } from '../ui/Button';
import { Badge } from '../ui/Badge';
import { settingsApi } from '../../api/settings';
import { inspectionsApi } from '../../api/inspections';
import { formatDate } from '../../utils/format';
import { generateInspectionReport } from '../../utils/pdfReport';
import { useAuthStore } from '../../store/auth.store';

export function ReportTab({ search }: { search?: string }) {
  const [deviceClassId, setDeviceClassId] = useState<number | ''>('');
  const [year, setYear] = useState<number | ''>(new Date().getFullYear());
  const user = useAuthStore(s => s.user);

  const { data: classesRes } = useQuery({
    queryKey: ['device-classes'],
    queryFn: () => settingsApi.getDeviceClasses(),
  });
  const deviceClasses: DeviceClass[] = classesRes?.data?.data || [];

  const { data: reportRes, isLoading } = useQuery({
    queryKey: ['inspection-report', deviceClassId, year, search],
    queryFn: () => inspectionsApi.getReport({
      deviceClassId: deviceClassId || undefined,
      year: year || undefined,
      search: search || undefined,
    }),
    enabled: !!year,
  });
  const inspections: ArticleInspection[] = reportRes?.data?.data || [];

  // Generate year options (last 10 years)
  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 10 }, (_, i) => currentYear - i);

  const handleExport = () => {
    const className = deviceClassId ? deviceClasses.find(dc => dc.id === deviceClassId)?.name || '' : 'Alle Klassen';
    const filterLabel = `${className} - ${year}`;
    generateInspectionReport(inspections, user?.name || user?.username || 'Unbekannt', filterLabel);
  };

  const columns = [
    { key: 'date', header: 'Datum', render: (i: ArticleInspection) => formatDate(i.inspectedAt) },
    { key: 'article', header: 'Artikel', render: (i: ArticleInspection) => (
      <div>
        <p className="font-medium">{i.article?.name}</p>
        {i.article?.inventoryNumber && <p className="text-xs text-gray-500">Inv.-Nr.: {i.article.inventoryNumber}</p>}
      </div>
    )},
    { key: 'deviceClass', header: 'Geräteklasse', render: (i: ArticleInspection) =>
      i.article?.deviceSubclass?.deviceClass?.name || '-'
    },
    { key: 'inspector', header: 'Prüfer', render: (i: ArticleInspection) => i.inspectedBy },
    { key: 'result', header: 'Ergebnis', render: (i: ArticleInspection) =>
      i.result === 'passed'
        ? <Badge variant="success">Bestanden</Badge>
        : <Badge variant="danger">Nicht bestanden</Badge>
    },
    { key: 'criteria', header: 'Kriterien', render: (i: ArticleInspection) => {
      const total = i.criterionResults?.length || 0;
      const io = i.criterionResults?.filter(cr => cr.result === 'io').length || 0;
      return total > 0 ? <span className="text-sm text-gray-600">{io}/{total} io</span> : '-';
    }},
  ];

  return (
    <div className="space-y-4">
      {/* Filters */}
      <div className="flex items-end gap-4 bg-white p-4 rounded-lg border border-gray-200">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Geräteklasse</label>
          <select value={deviceClassId} onChange={(e) => setDeviceClassId(e.target.value ? parseInt(e.target.value) : '')}
            className="block w-56 px-3 py-2 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
            <option value="">Alle Geräteklassen</option>
            {deviceClasses.map(dc => <option key={dc.id} value={dc.id}>{dc.name}</option>)}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Jahr</label>
          <select value={year} onChange={(e) => setYear(e.target.value ? parseInt(e.target.value) : '')}
            className="block w-32 px-3 py-2 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
            {years.map(y => <option key={y} value={y}>{y}</option>)}
          </select>
        </div>
        <Button variant="primary" icon={<ArrowDownTrayIcon />} onClick={handleExport} disabled={inspections.length === 0}>
          PDF Export ({inspections.length})
        </Button>
      </div>

      {/* Results table */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table
          columns={columns}
          data={inspections}
          loading={isLoading}
          emptyMessage="Keine Prüfungen für den gewählten Zeitraum gefunden."
          keyExtractor={(i) => i.id}
        />
      </div>
    </div>
  );
}
