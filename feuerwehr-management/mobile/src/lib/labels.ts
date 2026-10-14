import type { ArticleDefect, AttendanceStatus, DefectSeverity, DefectStatus, Event } from '@/api/types';
import type { StatusColor } from '@/components/ui/section';
import { formatDate } from '@/lib/format';

// Same wording as webapp/frontend/src/pages/defects/DefectsPage.tsx
export const SEVERITY_LABELS: Record<DefectSeverity, string> = {
  low: 'Gering',
  medium: 'Mittel',
  high: 'Hoch',
  critical: 'Kritisch',
};

export const SEVERITY_COLORS: Record<DefectSeverity, StatusColor> = {
  low: 'textSecondary',
  medium: 'warning',
  high: 'danger',
  critical: 'danger',
};

export const DEFECT_STATUS_LABELS: Record<DefectStatus, string> = {
  open: 'Offen',
  in_progress: 'In Bearbeitung',
  resolved: 'Behoben',
  closed: 'Geschlossen',
};

export function isDefectOpen(defect: Pick<ArticleDefect, 'status'>): boolean {
  return defect.status === 'open' || defect.status === 'in_progress';
}

/** "Hydraulikaggregat" for article defects, the free-text subject otherwise. */
export function defectSubject(defect: ArticleDefect): string {
  return defect.article?.name ?? defect.subject ?? 'Ohne Zuordnung';
}

export function inventoryLabel(article: { inventoryNumber: string | null; mpFeuerInventoryNumber: string | null }) {
  return article.inventoryNumber ?? article.mpFeuerInventoryNumber ?? '';
}

// Same as getEventCategoryLabel in webapp/frontend/src/utils/format.ts
export const EVENT_CATEGORY_LABELS: Record<number, string> = {
  1: 'Einsatz',
  2: 'Dienstabend',
  3: 'BSW',
  4: 'Sonstige',
  5: 'Übung',
};

export function eventTitle(event: Event): string {
  return event.name2 ? `${event.name} – ${event.name2}` : event.name;
}

/** "14.10.2026 · 19:30–21:30 Uhr · Dienstabend" */
export function eventSubtitle(event: Event): string {
  const time = event.startTime ? `${event.startTime}${event.endTime ? `–${event.endTime}` : ''} Uhr` : null;
  return [formatDate(event.date), time, EVENT_CATEGORY_LABELS[event.category]].filter(Boolean).join(' · ');
}

export const ATTENDANCE_LABELS: Record<AttendanceStatus, string> = {
  present: 'Anwesend',
  absent: 'Abwesend',
  excused: 'Entschuldigt',
};

/** Stored values of OperationPersonnel.function (as in the web Kräftenachweis) */
export const PERSONNEL_FUNCTIONS = [
  'Gruppenfuehrer',
  'Maschinist',
  'Melder',
  'AT-Fuehrer',
  'AT-Mann',
  'WT-Fuehrer',
  'WT-Mann',
  'ST-Fuehrer',
  'ST-Mann',
] as const;

/** "Gruppenfuehrer" → "Gruppenführer" for display; the stored value keeps the web spelling */
export function functionLabel(value: string): string {
  return value.replace(/ue/g, 'ü');
}

export function memberName(member: { firstName: string; lastName: string }): string {
  return `${member.lastName}, ${member.firstName}`;
}
