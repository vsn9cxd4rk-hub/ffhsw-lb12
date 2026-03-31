import React, { useState } from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { Sidebar } from './Sidebar';
import { Header } from './Header';
import { useAuthStore } from '../../store/auth.store';

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
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const location = useLocation();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
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
