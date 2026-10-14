import { ActivityIndicator, Pressable, StyleSheet } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

interface ButtonProps {
  title: string;
  onPress: () => void;
  /** primary: filled FENIX red; secondary: neutral surface */
  variant?: 'primary' | 'secondary';
  disabled?: boolean;
  busy?: boolean;
}

export function Button({ title, onPress, variant = 'primary', disabled, busy }: ButtonProps) {
  const theme = useTheme();
  const inactive = disabled || busy;
  const primary = variant === 'primary';
  const textColor = primary ? '#ffffff' : theme.text;

  return (
    <Pressable
      onPress={onPress}
      disabled={inactive}
      accessibilityRole="button"
      accessibilityState={{ disabled: inactive, busy }}
      style={({ pressed }) => [
        styles.button,
        primary
          ? { backgroundColor: pressed ? theme.primaryPressed : theme.primary }
          : { backgroundColor: pressed ? theme.backgroundSelected : theme.backgroundElement },
        inactive && styles.disabled,
      ]}>
      {busy ? (
        <ActivityIndicator color={textColor} />
      ) : (
        <ThemedText type="smallBold" style={{ color: textColor }}>
          {title}
        </ThemedText>
      )}
    </Pressable>
  );
}

const styles = StyleSheet.create({
  button: {
    borderRadius: Spacing.two,
    paddingVertical: Spacing.three,
    alignItems: 'center',
    marginTop: Spacing.two,
  },
  disabled: { opacity: 0.5 },
});
