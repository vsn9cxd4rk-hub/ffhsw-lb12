import type { VehicleInspection } from '@/api/types';
import type { StatusColor } from '@/components/ui/section';
import { addMonths, formatDate } from '@/lib/format';

export interface Deadline {
  name: string;
  date: string;
  status: StatusColor;
  text: string;
}

/** Same thresholds as /dashboard/notifications: overdue = red, within two months = yellow. */
export function vehicleDeadlines(inspection: VehicleInspection | null | undefined): Deadline[] {
  if (!inspection) return [];
  const now = new Date();
  const soon = addMonths(now, 2);
  const checks: [string, string | null][] = [
    ['TÜV', inspection.tuevDate],
    ['SP', inspection.spDate],
    ['Service', inspection.serviceDate],
  ];
  return checks
    .filter((check): check is [string, string] => !!check[1])
    .map(([name, date]) => {
      const due = new Date(date);
      const status: StatusColor = due <= now ? 'danger' : due <= soon ? 'warning' : 'success';
      const text = `${name} ${due <= now ? 'überfällig seit' : 'bis'} ${formatDate(date)}`;
      return { name, date, status, text };
    });
}

/** The most urgent deadline that needs attention (red before yellow), or undefined if all are fine. */
export function worstDeadline(inspection: VehicleInspection | null | undefined): Deadline | undefined {
  const pending = vehicleDeadlines(inspection).filter((d) => d.status !== 'success');
  return pending.find((d) => d.status === 'danger') ?? pending[0];
}
