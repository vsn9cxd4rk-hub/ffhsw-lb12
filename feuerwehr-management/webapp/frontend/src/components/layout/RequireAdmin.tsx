import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../../store/auth.store';

// Mirrors requireAdmin in the backend: admin = user.isAdmin, independent of the group.
export function RequireAdmin({ children }: { children: React.ReactNode }) {
  const user = useAuthStore((s) => s.user);

  if (!user) return <Navigate to="/login" replace />;
  if (user.isAdmin) return <>{children}</>;

  return <Navigate to="/dashboard" replace />;
}
