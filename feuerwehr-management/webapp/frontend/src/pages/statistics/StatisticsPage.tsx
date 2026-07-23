import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  BarChart,
  Bar,
  LineChart,
  Line,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import { dashboardApi } from '../../api/dashboard';
import { Card } from '../../components/ui/Card';
import { Badge } from '../../components/ui/Badge';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import {
  FireIcon,
  CheckCircleIcon,
  ExclamationTriangleIcon,
  HandRaisedIcon,
} from '@heroicons/react/24/outline';

interface OperationStatistics {
  total: number;
  byMonth: number[];
  byKeyword: { keyword: string; count: number }[];
  byDistrict: { district: string; count: number; percent: number }[];
  byResult: { result: string; count: number; percent: number }[];
  byTimeOfDay: { interval: string; count: number; avgPersonnel: number; risk: string }[];
  byDayOfWeek: { day: string; frueh: number; mittag: number; spaet: number; total: number }[];
  byPersonnel: {
    name: string;
    total: number;
    positions: { GF: number; MA: number; ME: number; AT: number; WT: number; ST: number };
  }[];
  activeInvolved: { yes: number; no: number };
  totalReal: number;
  totalFalse: number;
}

const MONTHS = ['Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez'];

const PIE_COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#06b6d4', '#ec4899', '#6366f1'];

function StatCard({
  icon: Icon,
  label,
  value,
  color,
}: {
  icon: React.ElementType;
  label: string;
  value: number | string;
  color: string;
}) {
  return (
    <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
      <div className="flex items-center gap-4">
        <div className={`p-3 rounded-lg ${color}`}>
          <Icon className="h-6 w-6 text-white" />
        </div>
        <div>
          <p className="text-2xl font-bold text-gray-900">{value}</p>
          <p className="text-sm text-gray-500">{label}</p>
        </div>
      </div>
    </div>
  );
}

function getRiskBadgeVariant(risk: string): 'success' | 'warning' | 'danger' {
  switch (risk) {
    case 'KRITISCH':
      return 'danger';
    case 'AKZEPTABEL':
      return 'warning';
    default:
      return 'success';
  }
}

export function StatisticsPage() {
  const currentYear = new Date().getFullYear();
  const [year, setYear] = useState(currentYear);

  const { data, isLoading, error } = useQuery<OperationStatistics>({
    queryKey: ['statistics-operations', year],
    queryFn: () =>
      dashboardApi.getStatistics('operations', { year }).then((r) => r.data.data),
  });

  if (isLoading) return <LoadingSpinner message="Lade Statistik..." />;

  if (error) {
    return (
      <div className="text-center py-12 text-red-500">
        Fehler beim Laden der Statistik: {(error as Error).message}
      </div>
    );
  }

  if (!data) {
    return (
      <div className="text-center py-12 text-gray-500">
        Keine Daten verfügbar.
      </div>
    );
  }

  const monthlyData = data.byMonth.map((count, idx) => ({
    month: MONTHS[idx],
    einsaetze: count,
  }));

  const personnelTop10 = [...data.byPersonnel]
    .sort((a, b) => b.total - a.total)
    .slice(0, 10);

  return (
    <div className="space-y-6">
      {/* Header with year selector */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-gray-900">Einsatzstatistik</h1>
        <select
          value={year}
          onChange={(e) => setYear(Number(e.target.value))}
          className="rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500"
        >
          {Array.from({ length: 6 }, (_, i) => currentYear - i).map((y) => (
            <option key={y} value={y}>
              {y}
            </option>
          ))}
        </select>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard icon={FireIcon} label="Gesamt" value={data.total} color="bg-blue-500" />
        <StatCard icon={CheckCircleIcon} label="Real-Einsätze" value={data.totalReal} color="bg-green-500" />
        <StatCard icon={ExclamationTriangleIcon} label="Fehlalarme" value={data.totalFalse} color="bg-red-500" />
        <StatCard
          icon={HandRaisedIcon}
          label="Aktiv tätig"
          value={data.activeInvolved.yes}
          color="bg-yellow-500"
        />
      </div>

      {/* Row 2: Monthly line chart */}
      <div className="grid grid-cols-1 lg:grid-cols-1 gap-6">
        <Card title="Einsätze pro Monat">
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={monthlyData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="month" />
                <YAxis allowDecimals={false} />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="einsaetze"
                  name="Einsätze"
                  stroke="#3b82f6"
                  strokeWidth={2}
                  dot={{ fill: '#3b82f6', r: 4 }}
                  activeDot={{ r: 6 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </Card>
      </div>

      {/* Row 3: District + Keyword bar charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card title="Einsätze nach Ortsteil">
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={data.byDistrict} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" allowDecimals={false} />
                <YAxis dataKey="district" type="category" width={100} tick={{ fontSize: 12 }} />
                <Tooltip
                  formatter={(value: number, _name: string, props: any) => [
                    `${value} (${props.payload.percent}%)`,
                    'Einsätze',
                  ]}
                />
                <Bar dataKey="count" name="Einsätze" fill="#3b82f6" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>

        <Card title="Einsätze nach Stichwort">
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={data.byKeyword.slice(0, 10)} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" allowDecimals={false} />
                <YAxis dataKey="keyword" type="category" width={120} tick={{ fontSize: 12 }} />
                <Tooltip />
                <Bar dataKey="count" name="Einsätze" fill="#8b5cf6" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>
      </div>

      {/* Row 4: Result pie chart + Time of day */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card title="Einsatzergebnis">
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={data.byResult}
                  dataKey="count"
                  nameKey="result"
                  cx="50%"
                  cy="50%"
                  outerRadius={100}
                  label={({ result, percent }) => `${result} (${percent}%)`}
                >
                  {data.byResult.map((_entry, index) => (
                    <Cell key={`cell-${index}`} fill={PIE_COLORS[index % PIE_COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip
                  formatter={(value: number, _name: string, props: any) => [
                    `${value} (${props.payload.percent}%)`,
                    props.payload.result,
                  ]}
                />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </Card>

        <Card title="Einsätze nach Tageszeit">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-200">
                  <th className="text-left py-2 px-3 font-medium text-gray-700">Zeitraum</th>
                  <th className="text-right py-2 px-3 font-medium text-gray-700">Anzahl</th>
                  <th className="text-right py-2 px-3 font-medium text-gray-700">Pers.</th>
                  <th className="text-center py-2 px-3 font-medium text-gray-700">Risiko</th>
                </tr>
              </thead>
              <tbody>
                {data.byTimeOfDay.map((row) => (
                  <tr key={row.interval} className="border-b border-gray-100">
                    <td className="py-2 px-3 text-gray-900">{row.interval}</td>
                    <td className="py-2 px-3 text-right text-gray-700">{row.count}</td>
                    <td className="py-2 px-3 text-right text-gray-700">
                      {row.avgPersonnel.toFixed(1)}
                    </td>
                    <td className="py-2 px-3 text-center">
                      <Badge variant={getRiskBadgeVariant(row.risk)}>{row.risk}</Badge>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      </div>

      {/* Row 5: Day of week stacked bar + Personnel top 10 */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card title="Einsätze nach Wochentag">
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={data.byDayOfWeek}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="day" />
                <YAxis allowDecimals={false} />
                <Tooltip />
                <Legend />
                <Bar dataKey="frueh" name="Früh (06-14)" stackId="a" fill="#3b82f6" />
                <Bar dataKey="mittag" name="Mittag (14-22)" stackId="a" fill="#f59e0b" />
                <Bar dataKey="spaet" name="Spät (22-06)" stackId="a" fill="#6366f1" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>

        <Card title="Personal Top 10">
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <BarChart data={personnelTop10} layout="vertical">
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis type="number" allowDecimals={false} />
                <YAxis dataKey="name" type="category" width={100} tick={{ fontSize: 12 }} />
                <Tooltip />
                <Bar dataKey="total" name="Einsätze" fill="#10b981" radius={[0, 4, 4, 0]} />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </Card>
      </div>
    </div>
  );
}
