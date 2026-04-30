import React from 'react';
import ReactDOM from 'react-dom/client';
import { QueryClient, QueryClientProvider, MutationCache } from '@tanstack/react-query';
import App from './App';
import { ToastContainer, useToastStore } from './components/ui/Toast';
import './index.css';

const mutationCache = new MutationCache({
  onError: (error: unknown) => {
    const axiosErr = error as { response?: { data?: { error?: string } } };
    const message = axiosErr?.response?.data?.error
      || (error instanceof Error ? error.message : 'Ein Fehler ist aufgetreten');
    useToastStore.getState().addToast(message, 'error');
  },
});

const queryClient = new QueryClient({
  mutationCache,
  defaultOptions: {
    queries: {
      retry: 1,
      staleTime: 30 * 1000,
      refetchOnWindowFocus: false,
    },
  },
});

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <App />
      <ToastContainer />
    </QueryClientProvider>
  </React.StrictMode>
);
