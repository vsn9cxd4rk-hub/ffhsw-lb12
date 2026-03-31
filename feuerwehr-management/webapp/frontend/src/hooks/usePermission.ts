import { useAuthStore } from '../store/auth.store';

export function usePermission(bit: string): boolean {
  const user = useAuthStore((s) => s.user);
  if (!user) return false;
  if (user.isAdmin) return true;
  return user.permissions?.[bit] === true;
}

export function useIsAdmin(): boolean {
  const user = useAuthStore((s) => s.user);
  return user?.isAdmin === true;
}
