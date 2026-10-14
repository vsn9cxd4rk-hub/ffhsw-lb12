import { useQuery } from '@tanstack/react-query';
import { router } from 'expo-router';
import { RefreshControl, ScrollView, StyleSheet } from 'react-native';

import { operationsApi } from '@/api/client';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { formatDate } from '@/lib/format';

export default function OperationsScreen() {
  const theme = useTheme();
  const operations = useQuery({ queryKey: ['operations'], queryFn: () => operationsApi.list() });
  const { refreshing, onRefresh } = usePullToRefresh(operations.refetch);

  return (
    <ThemedView style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <Button title="Neuer Einsatz" onPress={() => router.push('/operation/new')} />
        <QueryState isPending={operations.isPending} error={operations.error} />
        {operations.data && (
          <Section title="Letzte Einsätze" empty="Keine Einsätze vorhanden">
            {operations.data.map((op) => (
              <ListRow
                key={op.id}
                title={op.keyword ? `${op.keyword} – ${op.location}` : op.location}
                subtitle={[
                  formatDate(op.date),
                  op.alarmTime ? `${op.alarmTime} Uhr` : null,
                  op.vehicles,
                  op.leaderCount + op.memberCount > 0 ? `${op.leaderCount + op.memberCount} Kräfte` : null,
                ]
                  .filter(Boolean)
                  .join(' · ')}
                onPress={() => router.push({ pathname: '/operation/[id]', params: { id: String(op.id) } })}
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
