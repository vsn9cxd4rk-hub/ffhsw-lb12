import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useLocalSearchParams } from 'expo-router';
import { useState } from 'react';
import { KeyboardAvoidingView, Platform, ScrollView, StyleSheet, View } from 'react-native';

import { defectsApi, inventoryApi } from '@/api/client';
import type { DefectSeverity } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { ChoiceChips } from '@/components/ui/choice-chips';
import { DateField } from '@/components/ui/date-field';
import { QueryState } from '@/components/ui/query-state';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { useMyName } from '@/hooks/use-my-name';
import { parseIsoDate, toIsoDate } from '@/lib/format';
import { goBackOr } from '@/lib/navigation';
import { inventoryLabel, SEVERITY_COLORS, SEVERITY_LABELS } from '@/lib/labels';

const SEVERITIES: DefectSeverity[] = ['low', 'medium', 'high', 'critical'];

/** Defect for an article; opened from the article or the inspection (with prefilled fields). */
export default function NewDefectScreen() {
  const params = useLocalSearchParams<{
    articleId: string;
    description?: string;
    reportedBy?: string;
    reportedAt?: string;
  }>();
  const articleId = Number(params.articleId);
  const queryClient = useQueryClient();
  const myName = useMyName();
  const article = useQuery({ queryKey: ['articles', articleId], queryFn: () => inventoryApi.getArticle(articleId) });

  const [description, setDescription] = useState(params.description ?? '');
  const [severity, setSeverity] = useState<DefectSeverity>('medium');
  // null = untouched: use the name from the inspection or the signed-in user
  const [chosenReporter, setChosenReporter] = useState<string | null>(null);
  const [reportedAt, setReportedAt] = useState(params.reportedAt || toIsoDate(new Date()));
  const [notes, setNotes] = useState('');
  const reportedBy = chosenReporter ?? (params.reportedBy || myName);

  const save = useMutation({
    mutationFn: () =>
      defectsApi.create({
        articleId,
        description: description.trim(),
        severity,
        reportedBy: reportedBy.trim(),
        reportedAt,
        notes: notes.trim() || undefined,
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['defects'] });
      queryClient.invalidateQueries({ queryKey: ['articles', articleId] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      goBackOr({ pathname: '/article/[id]', params: { id: String(articleId) } });
    },
  });

  // isSuccess: the screen is about to close; a second tap must not create the defect twice
  const canSave =
    description.trim() !== '' && reportedBy.trim() !== '' && !!parseIsoDate(reportedAt) && !save.isSuccess;

  return (
    <ThemedView style={styles.container}>
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          <QueryState isPending={article.isPending} error={article.error} />
          {article.data && (
            <View>
              <ThemedText type="smallBold">{article.data.name}</ThemedText>
              <ThemedText type="small" themeColor="textSecondary">
                {[inventoryLabel(article.data), article.data.warehouse?.name].filter(Boolean).join(' · ')}
              </ThemedText>
            </View>
          )}

          <TextField
            label="Beschreibung"
            value={description}
            onChangeText={setDescription}
            multiline
            placeholder="Was ist defekt?"
            style={styles.multiline}
          />
          <ChoiceChips
            label="Schweregrad"
            choices={SEVERITIES.map((value) => ({
              value,
              label: SEVERITY_LABELS[value],
              color: SEVERITY_COLORS[value] === 'textSecondary' ? 'primary' : SEVERITY_COLORS[value],
            }))}
            value={severity}
            onChange={setSeverity}
          />
          <TextField label="Gemeldet von" value={reportedBy} onChangeText={setChosenReporter} />
          <DateField label="Meldedatum" value={reportedAt} onChange={setReportedAt} maximumDate={new Date()} />
          <TextField label="Bemerkungen" value={notes} onChangeText={setNotes} multiline style={styles.multiline} />

          {save.error && (
            <ThemedText type="small" themeColor="danger">
              {save.error instanceof TypeError ? 'Server nicht erreichbar' : save.error.message}
            </ThemedText>
          )}
          <Button title="Mangel speichern" onPress={() => save.mutate()} disabled={!canSave} busy={save.isPending} />
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
  multiline: { minHeight: 80, textAlignVertical: 'top' },
});
