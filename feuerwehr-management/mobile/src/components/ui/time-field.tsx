import { StyleSheet, View } from 'react-native';

import { Button } from '@/components/ui/button';
import { TextField } from '@/components/ui/text-field';
import { Spacing } from '@/constants/theme';

/** "1234" or "9.05" → "12:34" / "09:05"; anything else is returned unchanged for validation. */
export function normalizeTime(value: string): string {
  const digits = value.replace(/\D/g, '');
  if (/^\d{3,4}$/.test(digits) && !value.includes(':')) {
    const padded = digits.padStart(4, '0');
    return `${padded.slice(0, 2)}:${padded.slice(2)}`;
  }
  const match = /^(\d{1,2})[:.](\d{2})$/.exec(value.trim());
  return match ? `${match[1].padStart(2, '0')}:${match[2]}` : value.trim();
}

/** Empty or a valid "HH:MM" */
export function isValidTime(value: string): boolean {
  if (value === '') return true;
  const match = /^(\d{2}):(\d{2})$/.exec(value);
  return !!match && Number(match[1]) < 24 && Number(match[2]) < 60;
}

function now(): string {
  const date = new Date();
  return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
}

/** Time as "HH:MM" with a "Jetzt" button for capturing on the spot (stored as text by the backend). */
export function TimeField({ label, value, onChange }: { label: string; value: string; onChange: (value: string) => void }) {
  return (
    <View style={styles.row}>
      <View style={styles.input}>
        <TextField
          label={label}
          value={value}
          onChangeText={onChange}
          onBlur={() => onChange(normalizeTime(value))}
          placeholder="HH:MM"
          keyboardType="numbers-and-punctuation"
          maxLength={5}
        />
      </View>
      <View style={styles.now}>
        <Button title="Jetzt" variant="secondary" onPress={() => onChange(now())} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  row: { flexDirection: 'row', alignItems: 'flex-end', gap: Spacing.two },
  input: { flex: 1 },
  now: { width: 88, marginBottom: Spacing.two },
});
