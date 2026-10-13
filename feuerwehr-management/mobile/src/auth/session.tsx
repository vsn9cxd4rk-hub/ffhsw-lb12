import { createContext, use, useEffect, useState, type PropsWithChildren } from 'react';

import { authApi, normalizeServerUrl, setServerUrl, setSessionEndedHandler } from '@/api/client';
import type { User } from '@/api/types';
import { getItem, setItem } from '@/lib/storage';

const SERVER_URL_KEY = 'serverUrl';

interface SessionContextValue {
  isLoading: boolean;
  user: User | null;
  /** Last server the user signed in to, used to prefill the sign-in form */
  serverUrl: string;
  signIn: (serverUrl: string, username: string, password: string) => Promise<void>;
  signOut: () => Promise<void>;
}

const SessionContext = createContext<SessionContextValue | null>(null);

export function useSession() {
  const value = use(SessionContext);
  if (!value) throw new Error('useSession must be wrapped in a <SessionProvider />');
  return value;
}

export function SessionProvider({ children }: PropsWithChildren) {
  const [isLoading, setIsLoading] = useState(true);
  const [user, setUser] = useState<User | null>(null);
  const [serverUrl, setServerUrlState] = useState('');

  useEffect(() => {
    setSessionEndedHandler(() => setUser(null));

    (async () => {
      try {
        const stored = await getItem(SERVER_URL_KEY);
        if (!stored) return;
        setServerUrlState(stored);
        setServerUrl(stored);
        setUser(await authApi.restore());
      } catch {
        // Server unreachable: show the sign-in form with the stored server prefilled
      } finally {
        setIsLoading(false);
      }
    })();
  }, []);

  async function signIn(url: string, username: string, password: string) {
    const normalized = normalizeServerUrl(url);
    setServerUrl(normalized);
    const signedIn = await authApi.login(username, password);
    await setItem(SERVER_URL_KEY, normalized);
    setServerUrlState(normalized);
    setUser(signedIn);
  }

  async function signOut() {
    try {
      await authApi.logout();
    } finally {
      setUser(null);
    }
  }

  return (
    <SessionContext value={{ isLoading, user, serverUrl, signIn, signOut }}>
      {children}
    </SessionContext>
  );
}
