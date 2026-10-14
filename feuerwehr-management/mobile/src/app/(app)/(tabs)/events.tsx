import { useQuery } from '@tanstack/react-query';
import { router } from 'expo-router';
import { RefreshControl, ScrollView, StyleSheet } from 'react-native';

import { eventsApi } from '@/api/client';
import type { Event } from '@/api/types';
import { ThemedView } from '@/components/themed-view';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { toIsoDate } from '@/lib/format';
import { eventSubtitle, eventTitle } from '@/lib/labels';

const byDate = (a: Event, b: Event) => a.date.localeCompare(b.date) || (a.startTime ?? '').localeCompare(b.startTime ?? '');

export default function EventsScreen() {
  const theme = useTheme();
  const now = new Date();
  const today = toIsoDate(now);
  const yesterday = toIsoDate(new Date(now.getFullYear(), now.getMonth(), now.getDate() - 1));

  const upcoming = useQuery({
    queryKey: ['events', 'upcoming', today],
    queryFn: () => eventsApi.list({ dateFrom: today }),
    // The backend sorts descending; the next event belongs on top
    select: (events) => [...events].sort(byDate),
  });
  const past = useQuery({
    queryKey: ['events', 'past', today],
    queryFn: () => eventsApi.list({ dateTo: yesterday, limit: 15 }),
  });
  const { refreshing, onRefresh } = usePullToRefresh(() => Promise.all([upcoming.refetch(), past.refetch()]));

  const open = (event: Event) => router.push({ pathname: '/event/[id]', params: { id: String(event.id) } });

  return (
    <ThemedView style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <QueryState isPending={upcoming.isPending} error={upcoming.error ?? past.error} />
        {upcoming.data && (
          <Section title="Kommende Termine" empty="Keine Termine geplant">
            {upcoming.data.map((event) => (
              <ListRow
                key={event.id}
                title={eventTitle(event)}
                subtitle={eventSubtitle(event)}
                status={event.date.slice(0, 10) === today ? 'success' : undefined}
                onPress={() => open(event)}
              />
            ))}
          </Section>
        )}
        {past.data && past.data.length > 0 && (
          <Section title="Zuletzt">
            {past.data.map((event) => (
              <ListRow
                key={event.id}
                title={eventTitle(event)}
                subtitle={eventSubtitle(event)}
                onPress={() => open(event)}
              />
            ))}
          </Section>
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
