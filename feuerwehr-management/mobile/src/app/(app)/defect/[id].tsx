import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { router, useLocalSearchParams } from 'expo-router';
import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet } from 'react-native';

import { defectsApi } from '@/api/client';
import type { ArticleDefect, DefectStatus } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { ChoiceChips } from '@/components/ui/choice-chips';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { useMyName } from '@/hooks/use-my-name';
import { formatDate, toIsoDate } from '@/lib/format';
import { DEFECT_STATUS_LABELS, defectSubject, SEVERITY_COLORS, SEVERITY_LABELS } from '@/lib/labels';

const STATUSES: DefectStatus[] = ['open', 'in_progress', 'resolved', 'closed'];

export default function DefectScreen() {
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const defect = useQuery({ queryKey: ['defects', id], queryFn: () => defectsApi.get(id) });

  return (
    <ThemedView style={styles.container}>
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          <QueryState isPending={defect.isPending} error={defect.error} />
          {defect.data && <DefectDetail defect={defect.data} />}
        </ScrollView>
      </KeyboardAvoidingView>
    </ThemedView>
  );
}

function DefectDetail({ defect }: { defect: ArticleDefect }) {
  const queryClient = useQueryClient();
  const myName = useMyName();
  const [status, setStatus] = useState<DefectStatus>(defect.status);
  const [notes, setNotes] = useState(defect.notes ?? '');

  const isDone = status === 'resolved' || status === 'closed';
  const save = useMutation({
    mutationFn: () =>
      defectsApi.update(defect.id, {
        status,
        notes: notes.trim() || null,
        // Same as the web form: closing a defect records who and when, unless already recorded
        ...(isDone && !defect.resolvedBy ? { resolvedBy: myName, resolvedAt: toIsoDate(new Date()) } : {}),
      }),
    onSuccess: (updated) => {
      queryClient.setQueryData(['defects', defect.id], updated);
      queryClient.invalidateQueries({ queryKey: ['defects'] });
      if (updated.articleId) queryClient.invalidateQueries({ queryKey: ['articles', updated.articleId] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
    },
  });

  const changed = status !== defect.status || notes.trim() !== (defect.notes ?? '');

  return (
    <>
      <Section title={defectSubject(defect)}>
        <ThemedText>{defect.description}</ThemedText>
        <ListRow title="Schweregrad" detail={SEVERITY_LABELS[defect.severity]} status={SEVERITY_COLORS[defect.severity]} />
        <ListRow title="Gemeldet" detail={`${formatDate(defect.reportedAt)} · ${defect.reportedBy}`} />
        {defect.resolvedBy && (
          <ListRow
            title="Erledigt"
            detail={`${defect.resolvedAt ? formatDate(defect.resolvedAt) : ''} · ${defect.resolvedBy}`}
          />
        )}
        {defect.articleId && (
          <ListRow
            title="Zum Artikel"
            onPress={() => router.push({ pathname: '/article/[id]', params: { id: String(defect.articleId) } })}
          />
        )}
      </Section>

      <ChoiceChips
        label="Status"
        choices={STATUSES.map((value) => ({ value, label: DEFECT_STATUS_LABELS[value] }))}
        value={status}
        onChange={setStatus}
      />
      <TextField label="Bemerkungen" value={notes} onChangeText={setNotes} multiline style={styles.multiline} />

      {save.error && (
        <ThemedText type="small" themeColor="danger">
          {save.error instanceof TypeError ? 'Server nicht erreichbar' : save.error.message}
        </ThemedText>
      )}
      {save.isSuccess && !changed && (
        <ThemedText type="small" themeColor="success">
          Gespeichert.
        </ThemedText>
      )}
      <Button title="Speichern" onPress={() => save.mutate()} disabled={!changed} busy={save.isPending} />
    </>
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
  multiline: { minHeight: 80, textAlignVertical: 'top' },
});
