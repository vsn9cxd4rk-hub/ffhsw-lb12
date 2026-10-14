import { useQuery } from '@tanstack/react-query';
import { router } from 'expo-router';
import { useState } from 'react';

import { defectsApi } from '@/api/client';
import type { DefectStatus } from '@/api/types';
import { ChoiceChips } from '@/components/ui/choice-chips';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { formatDate } from '@/lib/format';
import { DEFECT_STATUS_LABELS, defectSubject, SEVERITY_COLORS, SEVERITY_LABELS } from '@/lib/labels';

export const DEFECTS_KEY = ['defects'];

const FILTERS: DefectStatus[] = ['open', 'in_progress', 'resolved', 'closed'];

export function DefectList() {
  const [status, setStatus] = useState<DefectStatus>('open');
  const defects = useQuery({
    queryKey: [...DEFECTS_KEY, { status }],
    queryFn: () => defectsApi.list({ status }),
  });

  return (
    <>
      <ChoiceChips
        choices={FILTERS.map((value) => ({ value, label: DEFECT_STATUS_LABELS[value] }))}
        value={status}
        onChange={setStatus}
      />
      <QueryState isPending={defects.isPending} error={defects.error} />
      {defects.data && (
        <Section title={`${DEFECT_STATUS_LABELS[status]} (${defects.data.length})`} empty="Keine Mängel">
          {defects.data.map((defect) => (
            <ListRow
              key={defect.id}
              title={defectSubject(defect)}
              subtitle={`${defect.description}\n${SEVERITY_LABELS[defect.severity]} · ${formatDate(defect.reportedAt)} · ${defect.reportedBy}`}
              status={SEVERITY_COLORS[defect.severity]}
              onPress={() => router.push({ pathname: '/defect/[id]', params: { id: String(defect.id) } })}
            />
          ))}
        </Section>
      )}
    </>
  );
}
