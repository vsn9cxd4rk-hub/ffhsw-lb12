import { useQuery } from '@tanstack/react-query';
import { router, Stack, useLocalSearchParams } from 'expo-router';
import { RefreshControl, ScrollView, StyleSheet } from 'react-native';

import { eventsApi } from '@/api/client';
import type { AttendanceStatus } from '@/api/types';
import { BIT_EVENTS, hasPermission } from '@/auth/permissions';
import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { ATTENDANCE_LABELS, eventSubtitle, eventTitle } from '@/lib/labels';

const STATUS_ORDER: AttendanceStatus[] = ['present', 'excused', 'absent'];
const STATUS_COLOR = { present: 'success', excused: 'warning', absent: 'danger' } as const;

export default function EventScreen() {
  const theme = useTheme();
  const { user } = useSession();
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const event = useQuery({ queryKey: ['events', id], queryFn: () => eventsApi.get(id) });
  const attendance = useQuery({ queryKey: ['events', id, 'attendance'], queryFn: () => eventsApi.getAttendance(id) });
  const { refreshing, onRefresh } = usePullToRefresh(() => Promise.all([event.refetch(), attendance.refetch()]));

  const canEdit = hasPermission(user, BIT_EVENTS);
  const members = attendance.data?.members ?? [];
  const count = (status: AttendanceStatus) => members.filter((m) => m.status === status).length;
  const open = members.filter((m) => !m.status).length;

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen options={{ title: event.data ? eventTitle(event.data) : 'Veranstaltung' }} />
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <QueryState isPending={event.isPending} error={event.error} />
        {event.data && (
          <>
            <Section title={eventTitle(event.data)}>
              <ThemedText type="small" themeColor="textSecondary">
                {eventSubtitle(event.data)}
              </ThemedText>
              {event.data.notes ? <ThemedText type="small">{event.data.notes}</ThemedText> : null}
            </Section>

            {attendance.data && (
              <Section title={`Anwesenheit (${members.length} Mitglieder)`}>
                {STATUS_ORDER.map((status) => (
                  <ListRow
                    key={status}
                    title={ATTENDANCE_LABELS[status]}
                    detail={String(count(status))}
                    status={STATUS_COLOR[status]}
                  />
                ))}
                <ListRow title="Nicht erfasst" detail={String(open)} status="textSecondary" />
              </Section>
            )}

            <Button
              title={canEdit ? 'Anwesenheit erfassen' : 'Anwesenheitsliste ansehen'}
              onPress={() => router.push({ pathname: '/event/[id]/attendance', params: { id: String(id) } })}
            />
          </>
        )}
      </ScrollView>
    </ThemedView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  content: {
    padding: Spacing.three,
    gap: Spacing.three,
    width: '100%',
    maxWidth: MaxContentWidth,
    alignSelf: 'center',
  },
});
