import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { s2Api, type S2Kind } from '../../api/s2';
import { useToastStore } from '../../components/ui/Toast';
import { fehlertext } from '../../utils/s2';

/** Polling interval: changes of the colleagues show up after a few seconds. */
export const POLL_MS = 5000;

export function useLage(id: number) {
  return useQuery({
    queryKey: ['s2-lage', id],
    queryFn: () => s2Api.getLage(id).then((r) => r.data.data),
    refetchInterval: POLL_MS,
  });
}

/**
 * Mutation that reloads the Lage afterwards. A 409 (somebody else changed the row meanwhile)
 * shows the server's message and reloads, so the user sees the current state before retrying.
 */
export function useS2Mutation<V>(lageId: number, fn: (v: V) => Promise<unknown>, erfolg?: string) {
  const qc = useQueryClient();
  const addToast = useToastStore((s) => s.addToast);
  return useMutation({
    mutationFn: fn,
    onSuccess: () => {
      if (erfolg) addToast(erfolg, 'success');
    },
    onError: async (err) => addToast(await fehlertext(err)),
    onSettled: () => qc.invalidateQueries({ queryKey: ['s2-lage', lageId] }),
  });
}

/** Create/update/delete for Abschnitte, Kräfte, Kartenobjekte, Meldungen. */
export function useItemMutations<T extends { id: number; version: number }>(lageId: number, kind: S2Kind) {
  const create = useS2Mutation(lageId, (data: Partial<T>) => s2Api.createItem<T>(lageId, kind, data));
  const update = useS2Mutation(lageId, ({ item, changes }: { item: T; changes: Partial<T> }) =>
    s2Api.updateItem<T>(kind, item.id, { ...changes, version: item.version }));
  const remove = useS2Mutation(lageId, (item: T) => s2Api.deleteItem(kind, item.id));
  return { create, update, remove };
}
