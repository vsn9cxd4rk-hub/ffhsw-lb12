import { useQuery } from '@tanstack/react-query';

import { inspectionsApi } from '@/api/client';
import { useSession } from '@/auth/session';

/**
 * Name to prefill "Prüfer" / "Gemeldet von": the linked member as listed by /inspections/inspectors
 * (same spelling as the web dropdown), otherwise the account name.
 */
export function useMyName(): string {
  const { user } = useSession();
  const inspectors = useQuery({ queryKey: ['inspectors'], queryFn: inspectionsApi.getInspectors });
  const memberId = user?.memberId;
  const member = inspectors.data?.find((inspector) => inspector.id === memberId);
  return member?.name ?? user?.name ?? user?.username ?? '';
}
