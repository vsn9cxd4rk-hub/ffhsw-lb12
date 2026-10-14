import { Children, type ReactNode } from 'react';
import { Pressable, StyleSheet, View } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

export type StatusColor = 'danger' | 'warning' | 'success' | 'textSecondary';

/** Titled card; shows `empty` when it has no rows. */
export function Section({ title, empty, children }: { title: string; empty?: string; children?: ReactNode }) {
  const hasRows = Children.toArray(children).length > 0;
  return (
    <View style={styles.section}>
      <ThemedText type="smallBold">{title}</ThemedText>
      <ThemedView type="backgroundElement" style={styles.card}>
        {hasRows ? (
          children
        ) : (
          <ThemedText type="small" themeColor="textSecondary">
            {empty}
          </ThemedText>
        )}
      </ThemedView>
    </View>
  );
}

interface ListRowProps {
  title: string;
  subtitle?: string;
  /** Text on the right, e.g. a value or "✓" */
  detail?: string;
  status?: StatusColor;
  /** Makes the row tappable and shows a chevron */
  onPress?: () => void;
  titleColor?: 'text' | 'danger' | 'primary';
}

export function ListRow({ title, subtitle, detail, status, onPress, titleColor = 'text' }: ListRowProps) {
  const theme = useTheme();
  const content = (
    <>
      {status && <View style={[styles.dot, { backgroundColor: theme[status] }]} />}
      <View style={styles.rowText}>
        <ThemedText type="small" themeColor={titleColor}>
          {title}
        </ThemedText>
        {subtitle ? (
          <ThemedText type="small" themeColor="textSecondary">
            {subtitle}
          </ThemedText>
        ) : null}
      </View>
      {detail ? (
        <ThemedText type="small" themeColor="textSecondary" style={styles.detail}>
          {detail}
        </ThemedText>
      ) : null}
      {onPress && (
        <ThemedText type="smallBold" themeColor="textSecondary">
          ›
        </ThemedText>
      )}
    </>
  );

  if (!onPress) return <View style={styles.row}>{content}</View>;
  return (
    <Pressable
      onPress={onPress}
      accessibilityRole="button"
      style={({ pressed }) => [styles.row, pressed && styles.pressed]}>
      {content}
    </Pressable>
  );
}

const styles = StyleSheet.create({
  section: { gap: Spacing.two },
  card: { borderRadius: Spacing.three, padding: Spacing.three, gap: Spacing.three },
  row: { flexDirection: 'row', alignItems: 'center', gap: Spacing.two },
  pressed: { opacity: 0.6 },
  rowText: { flex: 1 },
  detail: { flexShrink: 1, maxWidth: '60%', textAlign: 'right' },
  dot: { width: 10, height: 10, borderRadius: 5 },
});
