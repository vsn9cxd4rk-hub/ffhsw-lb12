import DateTimePicker from '@react-native-community/datetimepicker';
import { useState } from 'react';
import { Platform, Pressable, StyleSheet, View } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { TextField } from '@/components/ui/text-field';
import { Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';
import { formatDate, parseIsoDate, toIsoDate } from '@/lib/format';

interface DateFieldProps {
  label: string;
  /** "YYYY-MM-DD" */
  value: string;
  onChange: (value: string) => void;
  maximumDate?: Date;
}

/**
 * Date-only field. Native: the platform picker (Android dialog, iOS inline calendar on tap).
 * Web has no picker in the community package, so it falls back to a text field "YYYY-MM-DD".
 */
export function DateField({ label, value, onChange, maximumDate }: DateFieldProps) {
  const theme = useTheme();
  const [open, setOpen] = useState(false);

  if (Platform.OS === 'web') {
    return <TextField label={label} value={value} onChangeText={onChange} placeholder="JJJJ-MM-TT" />;
  }

  const date = parseIsoDate(value) ?? new Date();

  return (
    <View style={styles.field}>
      <ThemedText type="smallBold">{label}</ThemedText>
      <Pressable
        accessibilityRole="button"
        accessibilityLabel={`${label}: ${formatDate(value)}`}
        onPress={() => setOpen((o) => !o)}
        style={[styles.input, { backgroundColor: theme.backgroundElement, borderColor: theme.backgroundSelected }]}>
        <ThemedText>{formatDate(value)}</ThemedText>
      </Pressable>
      {open && (
        <DateTimePicker
          value={date}
          mode="date"
          display={Platform.OS === 'ios' ? 'inline' : 'default'}
          maximumDate={maximumDate}
          locale="de-DE"
          onValueChange={(_event, selected) => {
            // Android shows a dialog that closes itself; iOS stays inline until tapped again
            if (Platform.OS === 'android') setOpen(false);
            onChange(toIsoDate(selected));
          }}
          onDismiss={() => setOpen(false)}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  field: { gap: Spacing.two, marginBottom: Spacing.two },
  input: {
    borderWidth: 1,
    borderRadius: Spacing.two,
    paddingHorizontal: Spacing.three,
    paddingVertical: Spacing.two + Spacing.one,
  },
});
