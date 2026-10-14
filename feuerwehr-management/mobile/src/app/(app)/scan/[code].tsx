import { useQuery } from '@tanstack/react-query';
import { router, useLocalSearchParams } from 'expo-router';
import { useEffect } from 'react';
import { ScrollView, StyleSheet } from 'react-native';

import { inspectionsApi } from '@/api/client';
import type { Article } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { MaxContentWidth, Spacing } from '@/constants/theme';
import { inventoryLabel } from '@/lib/labels';
import { findArticlesByCode } from '@/lib/scan';

/**
 * Resolves a scanned or typed code (also the deep link fenix://scan/<nr>), like the web ScanPage:
 * a unique article that is due opens the inspection, otherwise the article. Ambiguous codes and
 * search suggestions are listed.
 */
export default function ScanResultScreen() {
  const { code } = useLocalSearchParams<{ code: string }>();

  const lookup = useQuery({
    queryKey: ['scan', code],
    queryFn: async () => {
      const found = await findArticlesByCode(code);
      if (!found.exact || found.articles.length !== 1) return { ...found, target: null };
      const article = found.articles[0];
      const due = await inspectionsApi.getDue(code);
      return { ...found, target: { article, isDue: due.some((a) => a.id === article.id) } };
    },
    // A scan is a one-off lookup: always ask the server again
    gcTime: 0,
  });

  const target = lookup.data?.target;
  useEffect(() => {
    if (!target) return;
    const id = String(target.article.id);
    if (target.isDue) router.replace({ pathname: '/inspect/[articleId]', params: { articleId: id } });
    else router.replace({ pathname: '/article/[id]', params: { id } });
  }, [target]);

  const articles = lookup.data?.articles ?? [];

  return (
    <ThemedView style={styles.container}>
      <ScrollView contentContainerStyle={styles.content}>
        <ThemedText type="small" themeColor="textSecondary">
          Gescannt: {code}
        </ThemedText>
        <QueryState isPending={lookup.isPending || !!target} error={lookup.error} />

        {lookup.data && !target && articles.length === 0 && (
          <ThemedText themeColor="danger">Kein Artikel mit der Kennung „{code}“ vorhanden.</ThemedText>
        )}
        {lookup.data && !target && articles.length > 0 && (
          <Section title={lookup.data.exact ? 'Mehrere Artikel mit dieser Nummer' : 'Kein exakter Treffer – ähnliche Artikel'}>
            {articles.map((article) => (
              <ArticleRow key={article.id} article={article} />
            ))}
          </Section>
        )}
      </ScrollView>
    </ThemedView>
  );
}

function ArticleRow({ article }: { article: Article }) {
  return (
    <ListRow
      title={article.name}
      subtitle={[inventoryLabel(article), article.warehouse?.name].filter(Boolean).join(' · ')}
      onPress={() => router.replace({ pathname: '/article/[id]', params: { id: String(article.id) } })}
    />
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
