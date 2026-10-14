import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { router, Stack, useLocalSearchParams } from 'expo-router';
import { RefreshControl, ScrollView, StyleSheet } from 'react-native';

import { operationsApi } from '@/api/client';
import type { OperationPersonnel, OperationTime } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { confirmDestructive } from '@/lib/confirm';
import { formatDate } from '@/lib/format';
import { functionLabel, memberName } from '@/lib/labels';

/**
 * Vehicles of an operation with their declared strength. Several time entries for the same vehicle
 * name are summed, like the personnel tab of the web OperationDetailPage.
 */
function vehicleStrengths(times: OperationTime[]) {
  const strengths = new Map<string, { leaderCount: number; memberCount: number }>();
  for (const t of times) {
    const name = t.vehicleName.trim();
    if (!name) continue;
    const s = strengths.get(name) ?? { leaderCount: 0, memberCount: 0 };
    s.leaderCount += t.leaderCount;
    s.memberCount += t.memberCount;
    strengths.set(name, s);
  }
  return strengths;
}

function timeSummary(t: OperationTime): string {
  const parts: [string, string | null][] = [
    ['Alarm', t.alarmTime],
    ['aus', t.departureTime],
    ['an', t.arrivalTime],
    ['ein', t.returnTime],
  ];
  return parts
    .filter(([, value]) => value)
    .map(([label, value]) => `${label} ${value}`)
    .join(' · ');
}

export default function OperationScreen() {
  const theme = useTheme();
  const queryClient = useQueryClient();
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const operation = useQuery({ queryKey: ['operations', id], queryFn: () => operationsApi.get(id) });
  const personnel = useQuery({ queryKey: ['operations', id, 'personnel'], queryFn: () => operationsApi.getPersonnel(id) });
  const { refreshing, onRefresh } = usePullToRefresh(() => Promise.all([operation.refetch(), personnel.refetch()]));

  const removePerson = useMutation({
    mutationFn: (personnelId: number) => operationsApi.deletePersonnel(id, personnelId),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['operations', id, 'personnel'] }),
  });

  async function confirmRemove(p: OperationPersonnel) {
    if (await confirmDestructive('Einsatzkraft entfernen?', memberName(p.member), 'Entfernen')) {
      removePerson.mutate(p.id);
    }
  }

  const op = operation.data;
  const times = op?.times ?? [];
  const strengths = vehicleStrengths(times);
  const vehicleNames = [...strengths.keys()];
  const people = personnel.data ?? [];
  const deployed = people.filter((p) => p.section === 'deployed');
  const reinforcement = people.filter((p) => p.section === 'reinforcement');
  const unassigned = deployed.filter((p) => !strengths.has(p.vehicleName));

  const personRow = (p: OperationPersonnel, showVehicle: boolean) => (
    <ListRow
      key={p.id}
      title={memberName(p.member)}
      subtitle={[functionLabel(p.function), showVehicle ? p.vehicleName : null].filter(Boolean).join(' · ')}
      detail="Entfernen"
      onPress={() => confirmRemove(p)}
    />
  );

  const addPerson = (params: { vehicleName?: string; section?: string }) =>
    router.push({ pathname: '/operation/[id]/personnel', params: { id: String(id), ...params } });

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen options={{ title: op?.keyword ?? 'Einsatz' }} />
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <QueryState isPending={operation.isPending} error={operation.error} />
        {op && (
          <>
            <Section title={op.keyword ? `${op.keyword} – ${op.location}` : op.location}>
              <ListRow title="Datum" detail={`${formatDate(op.date)}${op.alarmTime ? ` · ${op.alarmTime} Uhr` : ''}`} />
              {op.operationNumber ? <ListRow title="Einsatznummer" detail={op.operationNumber} /> : null}
              <ListRow
                title="Stärke gesamt"
                detail={`${op.leaderCount}/${op.memberCount}/${op.leaderCount + op.memberCount}`}
              />
              <ListRow
                title="Bearbeiten"
                onPress={() => router.push({ pathname: '/operation/[id]/edit', params: { id: String(id) } })}
              />
            </Section>

            <Section title="Fahrzeuge und Zeiten" empty="Noch kein Fahrzeug erfasst">
              {times.map((t) => (
                <ListRow
                  key={t.id}
                  title={t.vehicleName}
                  subtitle={timeSummary(t) || 'Keine Zeiten'}
                  detail={`${t.leaderCount}/${t.memberCount}/${t.leaderCount + t.memberCount}`}
                  onPress={() =>
                    router.push({ pathname: '/operation/[id]/time', params: { id: String(id), timeId: String(t.id) } })
                  }
                />
              ))}
            </Section>
            <Button
              title="Fahrzeug hinzufügen"
              variant="secondary"
              onPress={() => router.push({ pathname: '/operation/[id]/time', params: { id: String(id) } })}
            />

            <QueryState isPending={personnel.isPending} error={personnel.error} />
            {vehicleNames.map((name) => {
              const s = strengths.get(name)!;
              const crew = deployed.filter((p) => p.vehicleName === name);
              const leaders = crew.filter((p) => p.function === 'Gruppenfuehrer').length;
              const declared = s.leaderCount + s.memberCount;
              const reached = declared > 0 && leaders >= s.leaderCount && crew.length >= declared;
              return (
                <Section key={name} title={`Besatzung ${name}`} empty="Keine Kräfte zugeordnet">
                  {declared > 0 && (
                    <ThemedText type="small" themeColor={reached ? 'success' : 'warning'}>
                      {reached
                        ? `Stärke ${s.leaderCount}/${s.memberCount}/${declared} erreicht`
                        : `Soll ${s.leaderCount}/${s.memberCount}/${declared} · Ist ${leaders} GF / ${crew.length} gesamt`}
                    </ThemedText>
                  )}
                  {crew.map((p) => personRow(p, false))}
                </Section>
              );
            })}
            {unassigned.length > 0 && (
              <Section title="Ohne bekanntes Fahrzeug">{unassigned.map((p) => personRow(p, true))}</Section>
            )}
            {vehicleNames.length > 0 && (
              <Button title="Einsatzkraft hinzufügen" variant="secondary" onPress={() => addPerson({})} />
            )}

            <Section title="Nachgerückte Kräfte" empty="Keine nachgerückten Kräfte">
              {reinforcement.map((p) => personRow(p, true))}
            </Section>
            <Button
              title="Nachgerückte Kraft hinzufügen"
              variant="secondary"
              onPress={() => addPerson({ section: 'reinforcement' })}
            />
            {removePerson.error && (
              <ThemedText type="small" themeColor="danger">
                {removePerson.error.message}
              </ThemedText>
            )}
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
