import { useState } from 'react';

/**
 * State for a RefreshControl. react-query's isRefetching also turns true for background refetches
 * (app back in the foreground), which should not show the pull-to-refresh spinner.
 */
export function usePullToRefresh(refetch: () => Promise<unknown>) {
  const [refreshing, setRefreshing] = useState(false);

  async function onRefresh() {
    setRefreshing(true);
    try {
      await refetch();
    } finally {
      setRefreshing(false);
    }
  }

  return { refreshing, onRefresh };
}
