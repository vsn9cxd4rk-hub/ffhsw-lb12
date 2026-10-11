import React, { useState } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { Header } from './Header';
import { useAuthStore } from '../../store/auth.store';
import { LoadingSpinner } from '../ui/LoadingSpinner';
import { ChangePasswordPage } from '../../pages/auth/ChangePasswordPage';

const PAGE_TITLES: Record<string, string> = {
  '/dashboard': 'Dashboard',
  '/members': 'Mitgliederverwaltung',
  '/members/new': 'Neues Mitglied',
  '/vehicles': 'Fahrzeugverwaltung',
  '/inventory': 'Bestandsliste',
  '/operations': 'Einsätze',
  '/events': 'Veranstaltungen',
  '/training': 'Ausbildung',
  '/settings': 'Einstellungen',
  '/admin/users': 'Benutzerverwaltung',
  '/admin/permissions': 'Berechtigungsgruppen',
};

function getPageTitle(pathname: string): string {
  if (PAGE_TITLES[pathname]) return PAGE_TITLES[pathname];
  if (pathname.startsWith('/members/') && pathname !== '/members/new') return 'Mitgliederakte';
  if (pathname.startsWith('/vehicles/')) return 'Fahrzeugakte';
  if (pathname.startsWith('/operations/')) return 'Einsatzdetails';
  if (pathname.startsWith('/events/')) return 'Veranstaltung';
  return 'Feuerwehr Management';
}

export function Layout() {
  const status = useAuthStore((s) => s.status);
  const mustChangePassword = useAuthStore((s) => !!s.user?.mustChangePassword);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();

  // Session is being restored from the refresh cookie after a page load
  if (status === 'unknown') {
    return <LoadingSpinner size="lg" message="Sitzung wird geladen..." />;
  }

  if (status !== 'authenticated') {
    const redirect = encodeURIComponent(location.pathname + location.search);
    return <Navigate to={`/login?redirect=${redirect}`} replace />;
  }

  // The backend rejects everything else until the initial/reset password is replaced
  if (mustChangePassword) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center p-4">
        <div className="w-full max-w-md space-y-4">
          <div className="bg-yellow-50 border border-yellow-200 text-yellow-800 text-sm px-4 py-3 rounded-md">
            Ihr Passwort wurde vom Administrator vergeben oder ist ein Startpasswort.
            Bitte legen Sie jetzt ein eigenes Passwort fest.
          </div>
          <ChangePasswordPage />
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen overflow-hidden bg-gray-50">
      <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />

      <div className="flex flex-col flex-1 overflow-hidden">
        <Header
          onMenuClick={() => setSidebarOpen(true)}
          pageTitle={getPageTitle(location.pathname)}
        />
        <main className="flex-1 overflow-y-auto p-4 lg:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
