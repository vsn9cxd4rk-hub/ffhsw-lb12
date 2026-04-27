import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import {
  UserGroupIcon,
  TruckIcon,
  FireIcon,
  ExclamationTriangleIcon,
} from '@heroicons/react/24/outline';
import { dashboardApi } from '../../api/dashboard';
import { Card } from '../../components/ui/Card';
import { Badge } from '../../components/ui/Badge';
import { LoadingSpinner } from '../../components/ui/LoadingSpinner';
import { formatDate } from '../../utils/format';

function StatCard({ icon: Icon, label, value, color }: {
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

export function DashboardPage() {
  const navigate = useNavigate();
  const { data, isLoading } = useQuery({
    queryKey: ['dashboard-stats'],
    queryFn: () => dashboardApi.getStats().then((r) => r.data.data),
    refetchInterval: 5 * 60 * 1000,
  });

  if (isLoading) return <LoadingSpinner message="Lade Dashboard..." />;

  const stats = data;

  return (
    <div className="space-y-6">
      {/* Stats cards */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <StatCard icon={UserGroupIcon} label="Aktive Mitglieder" value={stats?.activeMembers ?? 0} color="bg-blue-500" />
        <StatCard icon={TruckIcon} label="Einsatzfahrzeuge" value={stats?.vehicles ?? 0} color="bg-green-500" />
        <StatCard icon={FireIcon} label="Einsätze (Jahr)" value={stats?.operationsThisYear ?? 0} color="bg-red-500" />
        <StatCard icon={ExclamationTriangleIcon} label="Anst. Prüfungen" value={stats?.upcomingInspections?.filter((i) => i.status === 'red' || i.status === 'yellow').length ?? 0} color="bg-yellow-500" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Recent operations */}
        <Card title="Letzte Einsätze">
          {stats?.recentOperations?.length === 0 ? (
            <p className="text-sm text-gray-500">Keine Einsätze vorhanden.</p>
          ) : (
            <ul className="divide-y divide-gray-100">
              {stats?.recentOperations?.map((op) => (
                <li
                  key={op.id}
                  className="py-3 flex items-center justify-between cursor-pointer hover:bg-gray-50 -mx-2 px-2 rounded"
                  onClick={() => navigate(`/operations/${op.id}`)}
                >
                  <div>
                    <p className="text-sm font-medium text-gray-900">{op.location}</p>
                    <p className="text-xs text-gray-500">{op.keyword || 'Kein Stichwort'}</p>
                  </div>
                  <div className="text-right">
                    <p className="text-xs text-gray-500">{formatDate(op.date)}</p>
                    <p className="text-xs text-gray-500">{op.leaderCount + op.memberCount} Kräfte</p>
                  </div>
                </li>
              ))}
            </ul>
          )}
        </Card>

        {/* Upcoming inspections */}
        <Card title="Anstehende Prüfungen">
          {stats?.upcomingInspections?.length === 0 && stats?.upcomingMedicalExams?.length === 0 ? (
            <p className="text-sm text-gray-500">Keine anstehenden Prüfungen.</p>
          ) : (
            <div className="space-y-4">
              {stats?.upcomingInspections && stats.upcomingInspections.length > 0 && (
                <div>
                  <h4 className="text-xs font-semibold text-gray-500 uppercase mb-2">Fahrzeug-/Gerätepüfungen</h4>
                  <ul className="space-y-2">
                    {stats.upcomingInspections.slice(0, 5).map((insp, i) => (
                      <li key={i} className="flex items-center justify-between text-sm">
                        <span className="flex items-center text-gray-700">
                          <span className={`inline-block h-2.5 w-2.5 rounded-full mr-2 ${
                            insp.status === 'red' ? 'bg-red-500' : insp.status === 'yellow' ? 'bg-yellow-500' : 'bg-green-500'
                          }`} />
                          {insp.articleId ? (
                            <button onClick={() => navigate(`/inventory/${insp.articleId}`)}
                              className="text-primary-600 hover:text-primary-800 hover:underline text-left">
                              {insp.entityName}
                            </button>
                          ) : (
                            insp.entityName
                          )}
                        </span>
                        <div className="flex items-center gap-2">
                          <Badge variant={insp.status === 'red' ? 'danger' : insp.status === 'yellow' ? 'warning' : 'success'}>
                            {insp.status === 'red' ? 'Überfällig' : insp.status === 'yellow' ? 'Fällig' : 'OK'}
                          </Badge>
                          <span className="text-gray-500 text-xs">{formatDate(insp.dueDate)}</span>
                        </div>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
              {stats?.upcomingMedicalExams && stats.upcomingMedicalExams.length > 0 && (
                <div>
                  <h4 className="text-xs font-semibold text-gray-500 uppercase mb-2">Arbeitsmedizinische Untersuchungen</h4>
                  <ul className="space-y-2">
                    {stats.upcomingMedicalExams.slice(0, 5).map((exam, i) => (
                      <li key={i} className="flex items-center justify-between text-sm">
                        <span className="text-gray-700">{exam.memberName}</span>
                        <div className="flex items-center gap-2">
                          <Badge variant="info">{exam.examType}</Badge>
                          <span className="text-gray-500 text-xs">{formatDate(exam.dueDate)}</span>
                        </div>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          )}
        </Card>
      </div>
    </div>
  );
}
