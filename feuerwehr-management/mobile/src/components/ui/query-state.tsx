import { ActivityIndicator, StyleSheet } from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { Spacing } from '@/constants/theme';
import { useTheme } from '@/hooks/use-theme';

/** Loading spinner or error line for a query; renders nothing once data is there. */
export function QueryState({ isPending, error }: { isPending: boolean; error: Error | null }) {
  const theme = useTheme();
  if (error) {
    // fetch throws a TypeError when the server cannot be reached at all
    const message = error instanceof TypeError ? 'Server nicht erreichbar' : error.message;
    return (
      <ThemedText type="small" themeColor="danger">
        Daten konnten nicht geladen werden: {message}
      </ThemedText>
    );
  }
  if (isPending) return <ActivityIndicator color={theme.primary} style={styles.loading} />;
  return null;
}

const styles = StyleSheet.create({
  loading: { marginTop: Spacing.five },
});
