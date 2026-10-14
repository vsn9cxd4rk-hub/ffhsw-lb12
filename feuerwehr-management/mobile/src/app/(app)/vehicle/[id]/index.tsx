import { useQuery } from '@tanstack/react-query';
import { router, Stack, useLocalSearchParams } from 'expo-router';
import { RefreshControl, ScrollView, StyleSheet } from 'react-native';

import { vehiclesApi } from '@/api/client';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { formatDate } from '@/lib/format';
import { vehicleDeadlines } from '@/lib/vehicles';

export default function VehicleScreen() {
  const theme = useTheme();
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const vehicle = useQuery({ queryKey: ['vehicles', id], queryFn: () => vehiclesApi.get(id) });
  const { refreshing, onRefresh } = usePullToRefresh(vehicle.refetch);

  const deadlines = vehicleDeadlines(vehicle.data?.inspection);
  const logbook = vehicle.data?.logbook ?? [];

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen options={{ title: vehicle.data?.name ?? 'Fahrzeug' }} />
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <QueryState isPending={vehicle.isPending} error={vehicle.error} />
        {vehicle.data && (
          <>
            <Button
              title="Fahrt eintragen"
              onPress={() => router.push({ pathname: '/vehicle/[id]/logbook', params: { id: String(id) } })}
            />

            <Section title="Prüftermine" empty="Keine Termine hinterlegt">
              {deadlines.map((d) => (
                <ListRow key={d.name} title={d.name} detail={formatDate(d.date)} status={d.status} />
              ))}
            </Section>

            <Section title="Fahrzeug">
              {[
                ['Funkrufname', vehicle.data.callSign],
                ['Kennzeichen', vehicle.data.licensePlate],
                ['Sitzplätze', vehicle.data.seats?.toString()],
                ['Führerscheinklasse', vehicle.data.licenseClass],
              ]
                .filter(([, value]) => value)
                .map(([label, value]) => (
                  <ListRow key={label} title={label as string} detail={value as string} />
                ))}
              {vehicle.data.description ? (
                <ThemedText type="small" themeColor="textSecondary">
                  {vehicle.data.description}
                </ThemedText>
              ) : null}
            </Section>

            <Section title="Fahrtenbuch (zuletzt)" empty="Noch keine Fahrten eingetragen">
              {logbook.map((entry) => (
                <ListRow
                  key={entry.id}
                  title={`${formatDate(entry.date)} · ${entry.purpose}`}
                  subtitle={[entry.driver, entry.destination].filter(Boolean).join(' · ')}
                  detail={`${(entry.endMileage - entry.startMileage).toLocaleString('de-DE')} km`}
                />
              ))}
            </Section>
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
