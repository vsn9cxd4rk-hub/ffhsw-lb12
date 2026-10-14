import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { router, useLocalSearchParams } from 'expo-router';
import { useState } from 'react';
import { KeyboardAvoidingView, Platform, Pressable, ScrollView, StyleSheet, View } from 'react-native';

import { inspectionsApi, inventoryApi } from '@/api/client';
import type { ArticleInspection, CriterionResult, InspectionCriterion } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { ChoiceChips } from '@/components/ui/choice-chips';
import { DateField } from '@/components/ui/date-field';
import { QueryState } from '@/components/ui/query-state';
import { Section } from '@/components/ui/section';
import { TextField } from '@/components/ui/text-field';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { useMyName } from '@/hooks/use-my-name';
import { useTheme } from '@/hooks/use-theme';
import { addMonths, formatDate, parseIsoDate, toIsoDate } from '@/lib/format';
import { inventoryLabel } from '@/lib/labels';
import { goBackOr } from '@/lib/navigation';

/** Mobile version of webapp/frontend/src/components/inspections/CriteriaInspectionModal.tsx (create only). */
export default function InspectScreen() {
  const articleId = Number(useLocalSearchParams<{ articleId: string }>().articleId);
  const queryClient = useQueryClient();
  const myName = useMyName();

  const article = useQuery({ queryKey: ['articles', articleId], queryFn: () => inventoryApi.getArticle(articleId) });
  const criteriaQuery = useQuery({
    queryKey: ['inspections', 'criteria', articleId],
    queryFn: () => inspectionsApi.getCriteria(articleId),
  });
  const inspectors = useQuery({ queryKey: ['inspectors'], queryFn: inspectionsApi.getInspectors });
  const types = useQuery({ queryKey: ['inspection-types'], queryFn: inspectionsApi.getTypes });

  const [inspectedAt, setInspectedAt] = useState(() => toIsoDate(new Date()));
  // null = not chosen yet: falls back to the signed-in user (if listed as inspector)
  const [chosenInspector, setChosenInspector] = useState<string | null>(null);
  const [inspectionTypeId, setInspectionTypeId] = useState<number | null>(null);
  const [notes, setNotes] = useState('');
  const [results, setResults] = useState<Record<number, CriterionResult>>({});
  const [saved, setSaved] = useState<ArticleInspection | null>(null);

  const criteria = criteriaQuery.data ?? [];
  const inspectorNames = inspectors.data?.map((i) => i.name) ?? [];
  // Without configured inspectors (no member in a "Gerätewart" group) the name is typed in
  const freeTextInspector = inspectors.isSuccess && inspectorNames.length === 0;
  const defaultInspector = freeTextInspector || inspectorNames.includes(myName) ? myName : '';
  const inspectedBy = chosenInspector ?? defaultInspector;

  const openCount = criteria.filter((c) => results[c.id] === undefined).length;
  const allEvaluated = openCount === 0;
  const failed = criteria.filter((c) => results[c.id] === 'nio');

  // Sets only the criteria not rated yet: a "nio" found at the device must not be overwritten
  function setOpenCriteriaIo() {
    setResults((prev) => {
      const next = { ...prev };
      for (const c of criteria) if (next[c.id] === undefined) next[c.id] = 'io';
      return next;
    });
  }

  const save = useMutation({
    mutationFn: () =>
      inspectionsApi.create({
        articleId,
        inspectedAt,
        inspectedBy: inspectedBy.trim(),
        inspectionTypeId: inspectionTypeId ?? undefined,
        notes: notes.trim() || undefined,
        criterionResults: criteria.length > 0 ? criteria.map((c) => ({ criterionId: c.id, result: results[c.id] })) : undefined,
      }),
    onSuccess: (inspection) => {
      queryClient.invalidateQueries({ queryKey: ['inspections'] });
      queryClient.invalidateQueries({ queryKey: ['articles', articleId] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      setSaved(inspection);
    },
  });

  function reportDefect() {
    const description =
      failed.length > 0 ? `Prüfung nicht bestanden. Beanstandete Kriterien: ${failed.map((c) => c.name).join(', ')}.` : '';
    const params = { articleId: String(articleId), description, reportedBy: inspectedBy, reportedAt: inspectedAt };
    // After saving, the inspection screen is done: replace it so "back" leads to where the user came from
    if (saved) router.replace({ pathname: '/defect/new', params });
    else router.push({ pathname: '/defect/new', params });
  }

  const date = parseIsoDate(inspectedAt);
  const nextDue = date && article.data?.inspectionInterval ? addMonths(date, article.data.inspectionInterval) : null;
  const canSave = !!date && inspectedBy.trim() !== '' && allEvaluated && !save.isPending;

  if (saved) {
    const passed = saved.result === 'passed';
    return (
      <ThemedView style={styles.container}>
        <ScrollView contentContainerStyle={styles.content}>
          <ThemedText type="subtitle" themeColor={passed ? 'success' : 'danger'}>
            {passed ? 'Bestanden' : 'Nicht bestanden'}
          </ThemedText>
          <ThemedText>Prüfung für {article.data?.name} gespeichert.</ThemedText>
          {saved.nextDueDate && (
            <ThemedText themeColor="textSecondary">Nächste Prüfung fällig am {formatDate(saved.nextDueDate)}</ThemedText>
          )}
          {!passed && <Button title="Mangel melden" onPress={reportDefect} />}
          <Button
            title="Fertig"
            variant={passed ? 'primary' : 'secondary'}
            onPress={() => goBackOr({ pathname: '/article/[id]', params: { id: String(articleId) } })}
          />
        </ScrollView>
      </ThemedView>
    );
  }

  return (
    <ThemedView style={styles.container}>
      <KeyboardAvoidingView style={styles.container} behavior={Platform.OS === 'ios' ? 'padding' : undefined}>
        <ScrollView contentContainerStyle={styles.content} keyboardShouldPersistTaps="handled">
          <QueryState isPending={article.isPending} error={article.error ?? criteriaQuery.error} />
          {article.data && (
            <View>
              <ThemedText type="smallBold">{article.data.name}</ThemedText>
              <ThemedText type="small" themeColor="textSecondary">
                {[inventoryLabel(article.data), article.data.deviceSubclass?.name, article.data.warehouse?.name]
                  .filter(Boolean)
                  .join(' · ')}
              </ThemedText>
            </View>
          )}

          <DateField label="Prüfdatum" value={inspectedAt} onChange={setInspectedAt} maximumDate={new Date()} />

          {freeTextInspector ? (
            <TextField label="Prüfer" value={inspectedBy} onChangeText={setChosenInspector} />
          ) : (
            <ChoiceChips
              label="Prüfer"
              choices={inspectorNames.map((name) => ({ value: name, label: name }))}
              value={inspectedBy || null}
              onChange={setChosenInspector}
            />
          )}

          {types.data && types.data.length > 0 && (
            <ChoiceChips
              label="Prüfart (optional)"
              choices={types.data.map((t) => ({ value: t.id, label: t.name }))}
              value={inspectionTypeId}
              onChange={setInspectionTypeId}
              onClear={() => setInspectionTypeId(null)}
            />
          )}

          {criteriaQuery.isSuccess && criteria.length === 0 && (
            <ThemedView type="backgroundElement" style={styles.notice}>
              <ThemedText type="small" themeColor="warning">
                Für diesen Artikel sind keine Prüfkriterien hinterlegt. Bitte im Web eine Unterklasse mit
                Kriterien zuweisen.
              </ThemedText>
            </ThemedView>
          )}

          {criteria.length > 0 && (
            <Section title={`Prüfkriterien (${criteria.length - openCount}/${criteria.length})`}>
              {criteria.map((criterion) => (
                <CriterionRow
                  key={criterion.id}
                  criterion={criterion}
                  result={results[criterion.id]}
                  onChange={(result) => setResults((prev) => ({ ...prev, [criterion.id]: result }))}
                />
              ))}
            </Section>
          )}
          {criteria.length > 0 && (
            <Button
              title={`Alle offenen auf io setzen${openCount > 0 && openCount < criteria.length ? ` (${openCount})` : ''}`}
              variant="secondary"
              onPress={setOpenCriteriaIo}
              disabled={openCount === 0}
            />
          )}

          {criteria.length > 0 && Object.keys(results).length > 0 && (
            <ThemedText type="smallBold" themeColor={!allEvaluated ? 'textSecondary' : failed.length ? 'danger' : 'success'}>
              Gesamtergebnis: {!allEvaluated ? 'alle Kriterien bewerten …' : failed.length ? 'Nicht bestanden' : 'Bestanden'}
            </ThemedText>
          )}

          <TextField label="Bemerkungen" value={notes} onChangeText={setNotes} multiline style={styles.multiline} />

          {nextDue && (
            <ThemedText type="small" themeColor="textSecondary">
              Nächste Prüfung fällig am {formatDate(toIsoDate(nextDue))}
            </ThemedText>
          )}

          {save.error && (
            <ThemedText type="small" themeColor="danger">
              {save.error instanceof TypeError ? 'Server nicht erreichbar' : save.error.message}
            </ThemedText>
          )}
          <Button title="Prüfung speichern" onPress={() => save.mutate()} disabled={!canSave} busy={save.isPending} />
          {article.data && <Button title="Mangel melden" variant="secondary" onPress={reportDefect} />}
        </ScrollView>
      </KeyboardAvoidingView>
    </ThemedView>
  );
}

function CriterionRow({
  criterion,
  result,
  onChange,
}: {
  criterion: InspectionCriterion;
  result: CriterionResult | undefined;
  onChange: (result: CriterionResult) => void;
}) {
  return (
    <View style={styles.criterion}>
      <ThemedText type="small" style={styles.criterionName}>
        {criterion.name}
      </ThemedText>
      <ResultButton label="io" selected={result === 'io'} color="success" onPress={() => onChange('io')} />
      <ResultButton label="nio" selected={result === 'nio'} color="danger" onPress={() => onChange('nio')} />
    </View>
  );
}

function ResultButton({
  label,
  selected,
  color,
  onPress,
}: {
  label: string;
  selected: boolean;
  color: 'success' | 'danger';
  onPress: () => void;
}) {
  const theme = useTheme();
  return (
    <Pressable
      accessibilityRole="radio"
      accessibilityState={{ selected }}
      accessibilityLabel={label}
      onPress={onPress}
      hitSlop={Spacing.one}
      style={[
        styles.resultButton,
        {
          borderColor: selected ? theme[color] : theme.backgroundSelected,
          backgroundColor: selected ? theme[color] : theme.background,
        },
      ]}>
      <ThemedText type="smallBold" style={selected ? styles.selectedText : undefined}>
        {label}
      </ThemedText>
    </Pressable>
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
  notice: { borderRadius: Spacing.two, padding: Spacing.three },
  multiline: { minHeight: 80, textAlignVertical: 'top' },
  criterion: { flexDirection: 'row', alignItems: 'center', gap: Spacing.two },
  criterionName: { flex: 1 },
  resultButton: {
    minWidth: 56,
    minHeight: 40,
    borderWidth: 1,
    borderRadius: Spacing.two,
    alignItems: 'center',
    justifyContent: 'center',
  },
  selectedText: { color: '#ffffff' },
});
