import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../../store/auth.store';

interface RequirePermissionProps {
  bit: string;
  children: React.ReactNode;
}

export function RequirePermission({ bit, children }: RequirePermissionProps) {
  const user = useAuthStore((s) => s.user);

  if (!user) return <Navigate to="/login" replace />;
  if (user.isAdmin) return <>{children}</>;
  if (user.permissions?.[bit]) return <>{children}</>;

  return <Navigate to="/dashboard" replace />;
}
