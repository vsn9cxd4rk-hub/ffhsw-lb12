import { format, formatRelative, parseISO } from 'date-fns';
import { de } from 'date-fns/locale';
import { Member } from '../types';

export function formatDate(dateStr: string | null | undefined): string {
  if (!dateStr) return '-';
  try {
    return format(parseISO(dateStr), 'dd.MM.yyyy');
  } catch {
    return '-';
  }
}

export function formatDateTime(dateStr: string | null | undefined): string {
  if (!dateStr) return '-';
  try {
    return format(parseISO(dateStr), 'dd.MM.yyyy HH:mm');
  } catch {
    return '-';
  }
}

export function formatRelativeDate(dateStr: string): string {
  try {
    return formatRelative(parseISO(dateStr), new Date(), { locale: de });
  } catch {
    return dateStr;
  }
}

export function getEventCategoryLabel(category: number): string {
  const labels: Record<number, string> = {
    1: 'Einsatz',
    2: 'Dienstabend',
    3: 'BSW',
    4: 'Sonstige',
    5: 'Übung',
  };
  return labels[category] || 'Unbekannt';
}

export function getEventCategoryColor(category: number): string {
  const colors: Record<number, string> = {
    1: 'bg-red-100 text-red-800',
    2: 'bg-blue-100 text-blue-800',
    3: 'bg-green-100 text-green-800',
    4: 'bg-gray-100 text-gray-800',
    5: 'bg-orange-100 text-orange-800',
  };
  return colors[category] || 'bg-gray-100 text-gray-800';
}

export function getMemberFullName(member: Pick<Member, 'firstName' | 'lastName'>): string {
  return `${member.lastName}, ${member.firstName}`;
}

export function getCourseStatusLabel(status: string): string {
  const labels: Record<string, string> = {
    pending: 'Geplant',
    active: 'Laufend',
    completed: 'Abgeschlossen',
    failed: 'Nicht bestanden',
  };
  return labels[status] || status;
}

export function getCourseStatusColor(status: string): string {
  const colors: Record<string, string> = {
    pending: 'bg-yellow-100 text-yellow-800',
    active: 'bg-blue-100 text-blue-800',
    completed: 'bg-green-100 text-green-800',
    failed: 'bg-red-100 text-red-800',
  };
  return colors[status] || 'bg-gray-100 text-gray-800';
}

export function formatMileage(km: number): string {
  return `${km.toLocaleString('de-DE')} km`;
}

export function formatCurrency(value: number | null | undefined): string {
  if (value == null) return '-';
  return new Intl.NumberFormat('de-DE', { style: 'currency', currency: 'EUR' }).format(value);
}
