import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Stack, useLocalSearchParams } from 'expo-router';
import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet, View } from 'react-native';

import { operationsApi, vehiclesApi } from '@/api/client';
import type { Operation, OperationTime } from '@/api/types';
import { BIT_VEHICLES, hasPermission } from '@/auth/permissions';
import { useSession } from '@/auth/session';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { ChoiceChips } from '@/components/ui/choice-chips';
import { QueryState } from '@/components/ui/query-state';
import { Stepper } from '@/components/ui/stepper';
import { TextField } from '@/components/ui/text-field';
import { isValidTime, normalizeTime, TimeField } from '@/components/ui/time-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { confirmDestructive } from '@/lib/confirm';
import { goBackOr } from '@/lib/navigation';

/** Add or edit one vehicle of an operation: name, strength and the four times. */
export default function OperationTimeScreen() {
  const params = useLocalSearchParams<{ id: string; timeId?: string }>();
  const id = Number(params.id);
  const timeId = params.timeId ? Number(params.timeId) : null;
  const operation = useQuery({ queryKey: ['operations', id], queryFn: () => operationsApi.get(id) });
  const existing = operation.data?.times?.find((t) => t.id === timeId);

  if (!operation.data || (timeId !== null && !existing)) {
    return (
      <ThemedView style={styles.container}>
        <QueryState isPending={operation.isPending} error={operation.error} />
        {operation.data && <ThemedText themeColor="danger">Eintrag nicht gefunden.</ThemedText>}
      </ThemedView>
    );
  }
  return <TimeForm operation={operation.data} existing={existing} />;
}

function TimeForm({ operation, existing }: { operation: Operation; existing?: OperationTime }) {
  const queryClient = useQueryClient();
  const { user } = useSession();
  const back = () => goBackOr({ pathname: '/operation/[id]', params: { id: String(operation.id) } });

  // The vehicle list needs BIT_VEHICLES; without it the name is typed in
  const vehicles = useQuery({
    queryKey: ['vehicles'],
    queryFn: vehiclesApi.list,
    enabled: hasPermission(user, BIT_VEHICLES),
  });
  // Plain values instead of existing?.x inside callbacks: the React Compiler turns such accesses
  // into memo dependencies without the optional check, which crashes for a new entry
  const existingId = existing ? existing.id : null;
  const existingName = existing ? existing.vehicleName : '';
  const taken = new Set((operation.times ?? []).filter((t) => t.id !== existingId).map((t) => t.vehicleName));
  const vehicleChoices = (vehicles.data ?? []).filter((v) => !taken.has(v.name)).map((v) => ({ value: v.name, label: v.name }));

  const [vehicleName, setVehicleName] = useState(existing?.vehicleName ?? '');
  const [leaderCount, setLeaderCount] = useState(existing?.leaderCount ?? 0);
  const [memberCount, setMemberCount] = useState(existing?.memberCount ?? 0);
  // Vehicles are usually alarmed together: suggest the operation's alarm time for a new vehicle
  const [alarmTime, setAlarmTime] = useState(existing ? (existing.alarmTime ?? '') : (operation.alarmTime ?? ''));
  const [departureTime, setDepartureTime] = useState(existing?.departureTime ?? '');
  const [arrivalTime, setArrivalTime] = useState(existing?.arrivalTime ?? '');
  const [returnTime, setReturnTime] = useState(existing?.returnTime ?? '');

  const invalidate = () => {
    queryClient.invalidateQueries({ queryKey: ['operations'] });
    queryClient.invalidateQueries({ queryKey: ['dashboard'] });
  };

  const save = useMutation({
    mutationFn: () => {
      const data = {
        vehicleName: vehicleName.trim(),
        leaderCount,
        memberCount,
        alarmTime: normalizeTime(alarmTime) || null,
        departureTime: normalizeTime(departureTime) || null,
        arrivalTime: normalizeTime(arrivalTime) || null,
        returnTime: normalizeTime(returnTime) || null,
      };
      return existingId !== null
        ? operationsApi.updateTime(operation.id, existingId, data)
        : operationsApi.addTime(operation.id, data);
    },
    onSuccess: () => {
      invalidate();
      back();
    },
  });

  const remove = useMutation({
    mutationFn: () => operationsApi.deleteTime(operation.id, existingId as number),
    onSuccess: () => {
      invalidate();
      back();
    },
  });

  async function confirmRemove() {
    const ok = await confirmDestructive(
      'Fahrzeug entfernen?',
      `${existingName} mit Stärke und Zeiten aus dem Einsatz entfernen. Zugeordnetes Personal bleibt erhalten.`,
      'Entfernen',
    );
    if (ok) remove.mutate();
  }

  const times = [alarmTime, departureTime, arrivalTime, returnTime].map(normalizeTime);
  const timesValid = times.every(isValidTime);
  const done = save.isSuccess || remove.isSuccess;
  const canSave = vehicleName.trim() !== '' && timesValid && !done;
  const error = save.error ?? remove.error;

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen options={{ title: existing ? existing.vehicleName : 'Fahrzeug hinzufügen' }} />
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          {vehicleChoices.length > 0 && (
            <ChoiceChips label="Fahrzeug" choices={vehicleChoices} value={vehicleName || null} onChange={setVehicleName} />
          )}
          <TextField
            label={vehicleChoices.length > 0 ? 'oder Name eingeben' : 'Fahrzeug'}
            value={vehicleName}
            onChangeText={setVehicleName}
            placeholder="z. B. LF 8/6"
          />
          <View style={styles.row}>
            <Stepper label="Führung (GF)" value={leaderCount} onChange={setLeaderCount} />
            <Stepper label="Mannschaft" value={memberCount} onChange={setMemberCount} />
          </View>
          <ThemedText type="small" themeColor="textSecondary">
            Stärke {leaderCount}/{memberCount}/{leaderCount + memberCount}
          </ThemedText>

          <TimeField label="Alarm" value={alarmTime} onChange={setAlarmTime} />
          <TimeField label="Ausgerückt" value={departureTime} onChange={setDepartureTime} />
          <TimeField label="Eingetroffen" value={arrivalTime} onChange={setArrivalTime} />
          <TimeField label="Eingerückt" value={returnTime} onChange={setReturnTime} />
          {!timesValid && (
            <ThemedText type="small" themeColor="danger">
              Zeiten bitte als HH:MM eingeben, z. B. 19:45.
            </ThemedText>
          )}

          {error && (
            <ThemedText type="small" themeColor="danger">
              {error instanceof TypeError ? 'Server nicht erreichbar' : error.message}
            </ThemedText>
          )}
          <Button title="Speichern" onPress={() => save.mutate()} disabled={!canSave} busy={save.isPending} />
          {existing && (
            <Button title="Fahrzeug entfernen" variant="secondary" onPress={confirmRemove} disabled={done} busy={remove.isPending} />
          )}
        </ScrollView>
      </KeyboardAvoidingView>
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
  row: { flexDirection: 'row', gap: Spacing.four, flexWrap: 'wrap' },
});
