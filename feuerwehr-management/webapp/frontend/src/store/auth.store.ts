import { create } from 'zustand';
import { User } from '../types';

// Nothing here is persisted: the access token only lives in memory, so an XSS cannot read it
// from localStorage. After a page load the session is restored from the httpOnly refresh
// cookie (see restoreSession in api/client.ts), which also brings the current permissions.
//
// status: 'unknown' until the startup refresh has answered, then 'authenticated' or 'anonymous'.
type AuthStatus = 'unknown' | 'authenticated' | 'anonymous';

interface AuthState {
  user: User | null;
  accessToken: string | null;
  status: AuthStatus;
  isAuthenticated: boolean;
  isLoading: boolean;
  setAuth: (user: User, token: string) => void;
  setAccessToken: (token: string) => void;
  clearAuth: () => void;
  setLoading: (loading: boolean) => void;
  updateUser: (user: Partial<User>) => void;
}

export const useAuthStore = create<AuthState>()((set) => ({
  user: null,
  accessToken: null,
  status: 'unknown',
  isAuthenticated: false,
  isLoading: false,
  setAuth: (user, token) =>
    set({ user, accessToken: token, status: 'authenticated', isAuthenticated: true, isLoading: false }),
  setAccessToken: (token) => set({ accessToken: token }),
  clearAuth: () =>
    set({ user: null, accessToken: null, status: 'anonymous', isAuthenticated: false, isLoading: false }),
  setLoading: (loading) => set({ isLoading: loading }),
  updateUser: (updates) =>
    set((state) => ({
      user: state.user ? { ...state.user, ...updates } : null,
    })),
}));
