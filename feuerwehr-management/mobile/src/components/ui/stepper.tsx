import { Pressable, StyleSheet, View } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

/** Small whole number with − / + buttons (e.g. crew strength). */
export function Stepper({
  label,
  value,
  onChange,
  min = 0,
  max = 99,
}: {
  label: string;
  value: number;
  onChange: (value: number) => void;
  min?: number;
  max?: number;
}) {
  const theme = useTheme();
  const button = (text: string, next: number, disabled: boolean, a11y: string) => (
    <Pressable
      accessibilityRole="button"
      accessibilityLabel={a11y}
      disabled={disabled}
      onPress={() => onChange(next)}
      style={[styles.button, { backgroundColor: theme.backgroundElement, borderColor: theme.backgroundSelected }, disabled && styles.disabled]}>
      <ThemedText type="smallBold">{text}</ThemedText>
    </Pressable>
  );

  return (
    <View style={styles.field}>
      <ThemedText type="smallBold">{label}</ThemedText>
      <View style={styles.row}>
        {button('−', value - 1, value <= min, `${label} verringern`)}
        <ThemedText style={styles.value} accessibilityLabel={`${label}: ${value}`}>
          {value}
        </ThemedText>
        {button('+', value + 1, value >= max, `${label} erhöhen`)}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  field: { gap: Spacing.two, marginBottom: Spacing.two },
  row: { flexDirection: 'row', alignItems: 'center', gap: Spacing.two },
  button: {
    width: 44,
    height: 44,
    borderWidth: 1,
    borderRadius: Spacing.two,
    alignItems: 'center',
    justifyContent: 'center',
  },
  disabled: { opacity: 0.4 },
  value: { minWidth: 32, textAlign: 'center' },
});
