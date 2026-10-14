import { useMutation, useQueryClient } from '@tanstack/react-query';
import { router } from 'expo-router';

import { operationsApi } from '@/api/client';
import { OperationForm, type OperationFormValues } from '@/components/operation-form';

export default function NewOperationScreen() {
  const queryClient = useQueryClient();
  const create = useMutation({
    mutationFn: (values: OperationFormValues) =>
      operationsApi.create({
        date: values.date,
        alarmTime: values.alarmTime || undefined,
        location: values.location,
        keyword: values.keyword || undefined,
      }),
    onSuccess: (operation) => {
      queryClient.invalidateQueries({ queryKey: ['operations'] });
      queryClient.invalidateQueries({ queryKey: ['dashboard'] });
      // Continue with vehicles and personnel; "back" from there leads to the list
      router.replace({ pathname: '/operation/[id]', params: { id: String(operation.id) } });
    },
  });

  return (
    <OperationForm
      submitLabel="Einsatz anlegen"
      busy={create.isPending || create.isSuccess}
      error={create.error}
      onSubmit={(values) => create.mutate(values)}
    />
  );
}
