import { useCallback, useEffect, useState } from 'react';

/**
 * Minimal data loading for the first screens. Once the app grows (caching, mutations,
 * background refetch), switch to @tanstack/react-query like the web frontend.
 */
export function useQuery<T>(load: () => Promise<T>) {
  const [data, setData] = useState<T | null>(null);
  const [error, setError] = useState<Error | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isRefreshing, setIsRefreshing] = useState(false);

  const run = useCallback(async () => {
    try {
      setData(await load());
      setError(null);
    } catch (e) {
      setError(e as Error);
    }
  }, [load]);

  useEffect(() => {
    run().finally(() => setIsLoading(false));
  }, [run]);

  const reload = useCallback(() => {
    setIsRefreshing(true);
    run().finally(() => setIsRefreshing(false));
  }, [run]);

  return { data, error, isLoading, isRefreshing, reload };
}
