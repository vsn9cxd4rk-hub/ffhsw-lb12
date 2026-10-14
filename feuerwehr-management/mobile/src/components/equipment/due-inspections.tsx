import { useQuery } from '@tanstack/react-query';
import { router } from 'expo-router';
import { useState } from 'react';

import { inspectionsApi } from '@/api/client';
import type { DueArticle } from '@/api/types';
import { ThemedText } from '@/components/themed-text';
import { QueryState } from '@/components/ui/query-state';
import { ListRow, Section } from '@/components/ui/section';
import { TextField } from '@/components/ui/text-field';
import { formatDate } from '@/lib/format';
import { inventoryLabel } from '@/lib/labels';

export const DUE_INSPECTIONS_KEY = ['inspections', 'due'];

function dueText(article: DueArticle): string {
  const last = article.inspections[0];
  if (!last) return 'Noch nie geprüft';
  return last.nextDueDate ? `Fällig seit ${formatDate(last.nextDueDate)}` : 'Fällig';
}

/** Articles whose inspection is due (no inspection yet or next due date passed). */
export function DueInspections() {
  const [search, setSearch] = useState('');
  const due = useQuery({ queryKey: DUE_INSPECTIONS_KEY, queryFn: () => inspectionsApi.getDue() });

  // Filtered on the device: the list is loaded once and stays usable while typing
  const term = search.trim().toLowerCase();
  const articles = (due.data ?? []).filter(
    (a) =>
      !term ||
      [a.name, a.inventoryNumber, a.mpFeuerInventoryNumber, a.warehouse?.name, a.deviceSubclass?.name].some((value) =>
        value?.toLowerCase().includes(term),
      ),
  );

  return (
    <>
      <TextField
        label="Suche"
        value={search}
        onChangeText={setSearch}
        placeholder="Name, Inventarnummer, Lagerort"
        autoCorrect={false}
        clearButtonMode="while-editing"
      />
      <QueryState isPending={due.isPending} error={due.error} />
      {due.data && (
        <>
          <ThemedText type="small" themeColor="textSecondary">
            {articles.length} von {due.data.length} fälligen Geräten
          </ThemedText>
          <Section title="Fällig" empty={term ? 'Keine Treffer' : 'Keine Prüfungen fällig'}>
            {articles.map((article) => (
              <ListRow
                key={article.id}
                title={article.name}
                subtitle={[inventoryLabel(article), article.warehouse?.name, dueText(article)].filter(Boolean).join(' · ')}
                status="danger"
                onPress={() => router.push({ pathname: '/inspect/[articleId]', params: { articleId: String(article.id) } })}
              />
            ))}
          </Section>
        </>
      )}
    </>
  );
}
