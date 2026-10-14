import { Pressable, StyleSheet, View } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { Spacing, type ThemeColor } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

export interface Choice<T> {
  value: T;
  label: string;
  /** Color of the selected chip; FENIX red by default */
  color?: ThemeColor;
}

interface ChoiceChipsProps<T> {
  label?: string;
  choices: Choice<T>[];
  value: T | null;
  onChange: (value: T) => void;
  /** Tapping the selected chip again clears the selection */
  onClear?: () => void;
}

/** Single choice as a row of chips; replaces a <select> for short option lists. */
export function ChoiceChips<T extends string | number>({ label, choices, value, onChange, onClear }: ChoiceChipsProps<T>) {
  const theme = useTheme();
  return (
    <View style={styles.field}>
      {label && <ThemedText type="smallBold">{label}</ThemedText>}
      <View style={styles.chips}>
        {choices.map((choice) => {
          const selected = choice.value === value;
          const color = theme[choice.color ?? 'primary'];
          return (
            <Pressable
              key={String(choice.value)}
              accessibilityRole="radio"
              accessibilityState={{ selected }}
              onPress={() => (selected && onClear ? onClear() : onChange(choice.value))}
              style={({ pressed }) => [
                styles.chip,
                {
                  borderColor: selected ? color : theme.backgroundSelected,
                  backgroundColor: selected ? color : theme.backgroundElement,
                },
                pressed && styles.pressed,
              ]}>
              <ThemedText type="small" style={selected ? styles.selectedText : undefined}>
                {choice.label}
              </ThemedText>
            </Pressable>
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  field: { gap: Spacing.two, marginBottom: Spacing.two },
  chips: { flexDirection: 'row', flexWrap: 'wrap', gap: Spacing.two },
  chip: {
    borderWidth: 1,
    borderRadius: Spacing.four,
    paddingHorizontal: Spacing.three,
    paddingVertical: Spacing.two,
    minHeight: 40,
    justifyContent: 'center',
  },
  pressed: { opacity: 0.6 },
  selectedText: { color: '#ffffff' },
});
