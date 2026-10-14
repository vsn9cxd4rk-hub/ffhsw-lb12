import { useQueryClient } from '@tanstack/react-query';
import { useState } from 'react';
import { RefreshControl, ScrollView, StyleSheet } from 'react-native';

import { DEFECTS_KEY, DefectList } from '@/components/equipment/defect-list';
import { DUE_INSPECTIONS_KEY, DueInspections } from '@/components/equipment/due-inspections';
import { ThemedView } from '@/components/themed-view';
import { ChoiceChips } from '@/components/ui/choice-chips';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';

type Segment = 'due' | 'defects';

/** Gerätewart tab: due inspections and defects in one place (keeps the tab bar at five tabs). */
export default function EquipmentScreen() {
  const theme = useTheme();
  const queryClient = useQueryClient();
  const [view, setView] = useState<Segment>('due');
  const { refreshing, onRefresh } = usePullToRefresh(() =>
    queryClient.refetchQueries({ queryKey: view === 'due' ? DUE_INSPECTIONS_KEY : DEFECTS_KEY, type: 'active' }),
  );

  return (
    <ThemedView style={styles.container}>
      <ScrollView
        contentContainerStyle={styles.content}
        keyboardShouldPersistTaps="handled"
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <ChoiceChips<Segment>
          choices={[
            { value: 'due', label: 'Fällige Prüfungen' },
            { value: 'defects', label: 'Mängel' },
          ]}
          value={view}
          onChange={setView}
        />
        {view === 'due' ? <DueInspections /> : <DefectList />}
      </ScrollView>
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
