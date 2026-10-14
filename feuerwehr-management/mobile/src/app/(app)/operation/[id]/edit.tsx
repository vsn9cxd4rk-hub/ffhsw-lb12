import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useLocalSearchParams } from 'expo-router';

import { operationsApi } from '@/api/client';
import { OperationForm, type OperationFormValues } from '@/components/operation-form';
import { ThemedView } from '@/components/themed-view';
import { QueryState } from '@/components/ui/query-state';
import { goBackOr } from '@/lib/navigation';

export default function EditOperationScreen() {
  const id = Number(useLocalSearchParams<{ id: string }>().id);
  const queryClient = useQueryClient();
  const operation = useQuery({ queryKey: ['operations', id], queryFn: () => operationsApi.get(id) });

  const update = useMutation({
    mutationFn: (values: OperationFormValues) =>
      operationsApi.update(id, { date: values.date, location: values.location, keyword: values.keyword || null }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['operations'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      goBackOr({ pathname: '/operation/[id]', params: { id: String(id) } });
    },
  });

  if (!operation.data) {
    return (
      <ThemedView style={{ flex: 1 }}>
        <QueryState isPending={operation.isPending} error={operation.error} />
      </ThemedView>
    );
  }

  return (
    <OperationForm
      operation={operation.data}
      submitLabel="Speichern"
      busy={update.isPending || update.isSuccess}
      error={update.error}
      onSubmit={(values) => update.mutate(values)}
    />
  );
}
