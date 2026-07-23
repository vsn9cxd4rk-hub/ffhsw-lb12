import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuthStore } from '../../store/auth.store';

interface RequireGroupProps {
  allowedGroups: number[];
  children: React.ReactNode;
}

export function RequireGroup({ allowedGroups, children }: RequireGroupProps) {
  const user = useAuthStore((s) => s.user);

  if (!user) return <Navigate to="/login" replace />;
  if (user.isAdmin) return <>{children}</>;
  if (user.groupId && allowedGroups.includes(user.groupId)) return <>{children}</>;

  return <Navigate to="/dashboard" replace />;
}
