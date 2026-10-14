import { useQuery } from '@tanstack/react-query';
import { router } from 'expo-router';
import { RefreshControl, ScrollView, StyleSheet } from 'react-native';

import { vehiclesApi } from '@/api/client';
import { ThemedView } from '@/components/themed-view';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { worstDeadline } from '@/lib/vehicles';

export default function VehiclesScreen() {
  const theme = useTheme();
  const vehicles = useQuery({ queryKey: ['vehicles'], queryFn: vehiclesApi.list });
  const { refreshing, onRefresh } = usePullToRefresh(vehicles.refetch);

  return (
    <ThemedView style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <QueryState isPending={vehicles.isPending} error={vehicles.error} />
        {vehicles.data && (
          <Section title="Fahrzeuge" empty="Keine Fahrzeuge">
            {vehicles.data.map((vehicle) => {
              const deadline = worstDeadline(vehicle.inspection);
              return (
                <ListRow
                  key={vehicle.id}
                  title={vehicle.name}
                  subtitle={[vehicle.callSign, vehicle.licensePlate, deadline?.text].filter(Boolean).join(' · ')}
                  status={deadline?.status}
                  onPress={() => router.push({ pathname: '/vehicle/[id]', params: { id: String(vehicle.id) } })}
                />
              );
            })}
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
