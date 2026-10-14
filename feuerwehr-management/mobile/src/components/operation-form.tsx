import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet } from 'react-native';

import type { Operation } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { DateField } from '@/components/ui/date-field';
import { TextField } from '@/components/ui/text-field';
import { isValidTime, TimeField } from '@/components/ui/time-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { parseIsoDate, toIsoDate } from '@/lib/format';

export interface OperationFormValues {
  date: string;
  alarmTime: string;
  location: string;
  keyword: string;
}

/**
 * Basic data of an operation, as in the web "Neuer Einsatz" dialog. The alarm time is only asked
 * when creating: afterwards the backend derives it from the earliest vehicle alarm time.
 */
export function OperationForm({
  operation,
  submitLabel,
  busy,
  error,
  onSubmit,
}: {
  operation?: Operation;
  submitLabel: string;
  busy: boolean;
  error: Error | null;
  onSubmit: (values: OperationFormValues) => void;
}) {
  const [date, setDate] = useState(() => (operation ? operation.date.slice(0, 10) : toIsoDate(new Date())));
  const [alarmTime, setAlarmTime] = useState(operation?.alarmTime ?? '');
  const [location, setLocation] = useState(operation?.location ?? '');
  const [keyword, setKeyword] = useState(operation?.keyword ?? '');

  // The alarm time is only shown (and checked) when creating
  const canSubmit = !!parseIsoDate(date) && location.trim() !== '' && (!!operation || isValidTime(alarmTime)) && !busy;

  return (
    <ThemedView style={styles.container}>
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          <DateField label="Datum" value={date} onChange={setDate} maximumDate={new Date()} />
          {!operation && <TimeField label="Alarmzeit" value={alarmTime} onChange={setAlarmTime} />}
          <TextField label="Einsatzort" value={location} onChangeText={setLocation} placeholder="Straße, Ort" />
          <TextField label="Stichwort" value={keyword} onChangeText={setKeyword} placeholder="z. B. B2, THL1" autoCapitalize="characters" />
          {!operation && (
            <ThemedText type="small" themeColor="textSecondary">
              Fahrzeuge, Stärke, Zeiten und Personal werden nach dem Anlegen erfasst. Berichtsfelder
              bleiben im Web.
            </ThemedText>
          )}
          {error && (
            <ThemedText type="small" themeColor="danger">
              {error instanceof TypeError ? 'Server nicht erreichbar' : error.message}
            </ThemedText>
          )}
          <Button
            title={submitLabel}
            onPress={() => onSubmit({ date, alarmTime, location: location.trim(), keyword: keyword.trim() })}
            disabled={!canSubmit}
            busy={busy}
          />
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
});
