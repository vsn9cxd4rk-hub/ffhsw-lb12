import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useLocalSearchParams } from 'expo-router';
import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet, View } from 'react-native';

import { vehiclesApi } from '@/api/client';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { DateField } from '@/components/ui/date-field';
import { QueryState } from '@/components/ui/query-state';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { useMyName } from '@/hooks/use-my-name';
import { parseIsoDate, toIsoDate } from '@/lib/format';
import { goBackOr } from '@/lib/navigation';

/** Whole kilometres only; "12.345" (German thousands separator) counts as 12345. */
function parseKm(value: string): number | null {
  const digits = value.replace(/[.\s]/g, '');
  return /^\d+$/.test(digits) ? Number(digits) : null;
}

export default function LogbookEntryScreen() {
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const queryClient = useQueryClient();
  const myName = useMyName();
  const vehicle = useQuery({ queryKey: ['vehicles', id], queryFn: () => vehiclesApi.get(id) });

  const [date, setDate] = useState(() => toIsoDate(new Date()));
  // null = untouched: prefilled from the signed-in user / the last entry
  const [driver, setDriver] = useState<string | null>(null);
  const [startKm, setStartKm] = useState<string | null>(null);
  const [endKm, setEndKm] = useState('');
  const [purpose, setPurpose] = useState('');
  const [destination, setDestination] = useState('');
  const [notes, setNotes] = useState('');

  // The new trip starts where the last one ended. The detail holds the 20 latest entries sorted by
  // day only, so take the highest mileage rather than the first entry.
  const entries = vehicle.data?.logbook ?? [];
  const lastEnd = entries.length > 0 ? Math.max(...entries.map((e) => e.endMileage)) : undefined;
  const driverValue = driver ?? myName;
  const startValue = startKm ?? (lastEnd !== undefined ? String(lastEnd) : '');
  const start = parseKm(startValue);
  const end = parseKm(endKm);
  const mileageError = start !== null && end !== null && end < start;

  const save = useMutation({
    mutationFn: () =>
      vehiclesApi.addLogbookEntry(id, {
        date,
        driver: driverValue.trim(),
        purpose: purpose.trim(),
        destination: destination.trim() || undefined,
        startMileage: start as number,
        endMileage: end as number,
        notes: notes.trim() || undefined,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['vehicles', id] });
      goBackOr({ pathname: '/vehicle/[id]', params: { id: String(id) } });
    },
  });

  const canSave =
    !!parseIsoDate(date) &&
    driverValue.trim() !== '' &&
    purpose.trim() !== '' &&
    start !== null &&
    end !== null &&
    !mileageError &&
    !save.isSuccess;

  return (
    <ThemedView style={styles.container}>
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          <QueryState isPending={vehicle.isPending} error={vehicle.error} />
          {vehicle.data && <ThemedText type="smallBold">{vehicle.data.name}</ThemedText>}

          <DateField label="Datum" value={date} onChange={setDate} maximumDate={new Date()} />
          <TextField label="Fahrer" value={driverValue} onChangeText={setDriver} />
          <TextField label="Zweck" value={purpose} onChangeText={setPurpose} placeholder="z. B. Übung, Einsatz, Werkstatt" />
          <TextField label="Ziel (optional)" value={destination} onChangeText={setDestination} />
          <View style={styles.row}>
            <View style={styles.half}>
              <TextField label="km-Stand Start" value={startValue} onChangeText={setStartKm} keyboardType="number-pad" />
            </View>
            <View style={styles.half}>
              <TextField label="km-Stand Ende" value={endKm} onChangeText={setEndKm} keyboardType="number-pad" />
            </View>
          </View>
          {mileageError && (
            <ThemedText type="small" themeColor="danger">
              km-Stand Ende darf nicht kleiner als km-Stand Start sein.
            </ThemedText>
          )}
          {start !== null && end !== null && !mileageError && (
            <ThemedText type="small" themeColor="textSecondary">
              Gefahren: {(end - start).toLocaleString('de-DE')} km
            </ThemedText>
          )}
          <TextField label="Bemerkungen (optional)" value={notes} onChangeText={setNotes} multiline style={styles.multiline} />

          {save.error && (
            <ThemedText type="small" themeColor="danger">
              {save.error instanceof TypeError ? 'Server nicht erreichbar' : save.error.message}
            </ThemedText>
          )}
          <Button title="Fahrt speichern" onPress={() => save.mutate()} disabled={!canSave} busy={save.isPending} />
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
  row: { flexDirection: 'row', gap: Spacing.two },
  half: { flex: 1 },
  multiline: { minHeight: 80, textAlignVertical: 'top' },
});
