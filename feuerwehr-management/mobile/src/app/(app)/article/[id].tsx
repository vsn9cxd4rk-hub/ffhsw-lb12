import { useQuery } from '@tanstack/react-query';
import { router, Stack, useLocalSearchParams } from 'expo-router';
import { RefreshControl, ScrollView, StyleSheet, View } from 'react-native';

import { inspectionsApi, inventoryApi } from '@/api/client';
import type { Article } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { Button } from '@/components/ui/button';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { usePullToRefresh } from '@/hooks/use-pull-to-refresh';
import { useTheme } from '@/hooks/use-theme';
import { formatDate } from '@/lib/format';
import { isDefectOpen, SEVERITY_COLORS, SEVERITY_LABELS } from '@/lib/labels';

export default function ArticleScreen() {
  const theme = useTheme();
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const article = useQuery({ queryKey: ['articles', id], queryFn: () => inventoryApi.getArticle(id) });
  const inspections = useQuery({
    queryKey: ['inspections', 'article', id],
    queryFn: () => inspectionsApi.getArticleInspections(id),
  });
  const { refreshing, onRefresh } = usePullToRefresh(() =>
    Promise.all([article.refetch(), inspections.refetch()]),
  );

  const params = { articleId: String(id) };
  // Article detail only includes the 10 latest defects; enough to show the open ones here
  const openDefects = article.data?.defects?.filter(isDefectOpen) ?? [];
  const lastInspection = inspections.data?.[0];

  return (
    <ThemedView style={styles.container}>
      <Stack.Screen options={{ title: article.data?.name ?? 'Artikel' }} />
      <ScrollView
        contentContainerStyle={styles.content}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={onRefresh} tintColor={theme.primary} />}>
        <QueryState isPending={article.isPending} error={article.error} />

        {article.data && (
          <>
            {article.data.isDecommissioned && (
              <ThemedText type="smallBold" themeColor="danger">
                Ausgesondert{article.data.decommissionedDate ? ` am ${formatDate(article.data.decommissionedDate)}` : ''}
              </ThemedText>
            )}

            <View style={styles.actions}>
              <View style={styles.action}>
                <Button
                  title="Prüfung durchführen"
                  onPress={() => router.push({ pathname: '/inspect/[articleId]', params })}
                  disabled={article.data.isDecommissioned}
                />
              </View>
              <View style={styles.action}>
                <Button
                  title="Mangel melden"
                  variant="secondary"
                  onPress={() => router.push({ pathname: '/defect/new', params })}
                />
              </View>
            </View>

            <Section title="Prüfstatus">
              <ListRow
                title={lastInspection ? `Zuletzt geprüft am ${formatDate(lastInspection.inspectedAt)}` : 'Noch nie geprüft'}
                subtitle={lastInspection ? `${lastInspection.inspectedBy} · ${lastInspection.result === 'passed' ? 'bestanden' : 'nicht bestanden'}` : undefined}
                status={lastInspection ? (lastInspection.result === 'passed' ? 'success' : 'danger') : undefined}
              />
              {lastInspection?.nextDueDate && (
                <ListRow title="Nächste Prüfung" detail={formatDate(lastInspection.nextDueDate)} />
              )}
              {article.data.inspectionInterval ? (
                <ListRow title="Prüfintervall" detail={`${article.data.inspectionInterval} Monate`} />
              ) : (
                <ListRow title="Kein Prüfintervall hinterlegt" />
              )}
            </Section>

            {openDefects.length > 0 && (
              <Section title="Offene Mängel">
                {openDefects.map((defect) => (
                  <ListRow
                    key={defect.id}
                    title={defect.description}
                    subtitle={`${SEVERITY_LABELS[defect.severity]} · ${formatDate(defect.reportedAt)}`}
                    status={SEVERITY_COLORS[defect.severity]}
                    onPress={() => router.push({ pathname: '/defect/[id]', params: { id: String(defect.id) } })}
                  />
                ))}
              </Section>
            )}

            <MasterData article={article.data} />

            {inspections.data && inspections.data.length > 1 && (
              <Section title="Frühere Prüfungen">
                {inspections.data.slice(1, 6).map((inspection) => (
                  <ListRow
                    key={inspection.id}
                    title={formatDate(inspection.inspectedAt)}
                    subtitle={[inspection.inspectionType?.name, inspection.inspectedBy].filter(Boolean).join(' · ')}
                    status={inspection.result === 'passed' ? 'success' : 'danger'}
                  />
                ))}
              </Section>
            )}
          </>
        )}
      </ScrollView>
    </ThemedView>
  );
}

function MasterData({ article }: { article: Article }) {
  const rows: [string, string | null | undefined][] = [
    ['Inventarnummer', article.inventoryNumber],
    ['MP-Feuer-Nr.', article.mpFeuerInventoryNumber],
    ['Gemeinde-Inv.-Nr.', article.communityInventoryNumber],
    ['Geräteklasse', article.deviceSubclass ? `${article.deviceSubclass.deviceClass?.name ?? ''} / ${article.deviceSubclass.name}` : null],
    ['Lagerort', article.warehouse?.name],
    ['Hersteller', article.manufacturer],
    ['Typ', article.articleType],
    ['Seriennummer', article.serialNumber],
    ['Herstellung', article.manufacturingDate ? formatDate(article.manufacturingDate) : null],
    ['In Dienst seit', article.commissionedDate ? formatDate(article.commissionedDate) : null],
  ];
  const assignments = article.assignments ?? [];

  return (
    <Section title="Stammdaten">
      {rows
        .filter(([, value]) => value)
        .map(([label, value]) => (
          <ListRow key={label} title={label} detail={value ?? ''} />
        ))}
      {assignments.map((assignment) => (
        <ListRow
          key={`assignment-${assignment.id}`}
          title={`Zugeordnet: ${assignment.warehouse?.name ?? ''}`}
          detail={`${assignment.quantity}×${assignment.assignedTo ? ` · ${assignment.assignedTo}` : ''}`}
        />
      ))}
    </Section>
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
  actions: { flexDirection: 'row', gap: Spacing.two },
  action: { flex: 1 },
});
