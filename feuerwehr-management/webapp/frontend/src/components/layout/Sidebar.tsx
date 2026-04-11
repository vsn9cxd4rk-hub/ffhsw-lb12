import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';
import {
  HomeIcon,
  UserGroupIcon,
  TruckIcon,
  ArchiveBoxIcon,
  ClipboardDocumentCheckIcon,
  FireIcon,
  CalendarIcon,
  AcademicCapIcon,
  Cog6ToothIcon,
  ShieldCheckIcon,
  Bars3Icon,
  XMarkIcon,
} from '@heroicons/react/24/outline';
import { cn } from '../../utils/cn';
import { useAuthStore } from '../../store/auth.store';

// groupIds: null = alle, [1] = nur Admin
const GROUP_ADMIN = 1;
const GROUP_GERAETEWARTE = 2;
const GROUP_BENUTZER = 3;
const GROUP_MASCHINISTEN = 4;
const GROUP_GRUPPENFUEHRER = 5;

const navigation = [
  { name: 'Dashboard', href: '/dashboard', icon: HomeIcon, groups: null },
  { name: 'Personal', href: '/members', icon: UserGroupIcon, groups: [GROUP_ADMIN, GROUP_BENUTZER, GROUP_MASCHINISTEN, GROUP_GRUPPENFUEHRER] },
  { name: 'Fahrzeuge', href: '/vehicles', icon: TruckIcon, groups: [GROUP_ADMIN, GROUP_GERAETEWARTE, GROUP_MASCHINISTEN] },
  { name: 'Bestandsliste', href: '/inventory', icon: ArchiveBoxIcon, groups: [GROUP_ADMIN, GROUP_GERAETEWARTE] },
  { name: 'Prüfbuch', href: '/inspections', icon: ClipboardDocumentCheckIcon, groups: [GROUP_ADMIN, GROUP_GERAETEWARTE] },
  { name: 'Einsätze', href: '/operations', icon: FireIcon, groups: [GROUP_ADMIN, GROUP_BENUTZER, GROUP_MASCHINISTEN, GROUP_GRUPPENFUEHRER] },
  { name: 'Veranstaltungen', href: '/events', icon: CalendarIcon, groups: [GROUP_ADMIN, GROUP_BENUTZER, GROUP_MASCHINISTEN, GROUP_GRUPPENFUEHRER] },
  { name: 'Ausbildung', href: '/training', icon: AcademicCapIcon, groups: [GROUP_ADMIN, GROUP_BENUTZER, GROUP_MASCHINISTEN, GROUP_GRUPPENFUEHRER] },
  { name: 'Einstellungen', href: '/settings', icon: Cog6ToothIcon, groups: [GROUP_ADMIN] },
];

interface SidebarProps {
  isOpen: boolean;
  onClose: () => void;
}

export function Sidebar({ isOpen, onClose }: SidebarProps) {
  const user = useAuthStore((s) => s.user);

  const visibleNavigation = navigation.filter(item => {
    if (user?.isAdmin) return true;
    if (item.groups === null) return true;
    return user?.groupId ? item.groups.includes(user.groupId) : false;
  });

  return (
    <>
      {/* Mobile overlay */}
      {isOpen && (
        <div
          className="fixed inset-0 z-40 bg-black/50 lg:hidden"
          onClick={onClose}
        />
      )}

      {/* Sidebar */}
      <aside
        className={cn(
          'fixed inset-y-0 left-0 z-50 w-64 bg-gray-900 text-white transform transition-transform duration-300',
          'lg:translate-x-0 lg:static lg:z-auto',
          isOpen ? 'translate-x-0' : '-translate-x-full'
        )}
      >
        {/* Header */}
        <div className="flex items-center justify-between h-16 px-4 bg-primary-700">
          <div className="flex items-center gap-3">
            <div className="flex-shrink-0">
              <img src="/LB12.png" alt="Logo" className="h-9 w-9 rounded-full object-contain bg-white p-0.5" />
            </div>
            <div>
              <p className="text-sm font-bold leading-tight">Feuerwehr</p>
              <p className="text-xs text-primary-200 leading-tight">Management</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="lg:hidden text-white/70 hover:text-white"
          >
            <XMarkIcon className="h-6 w-6" />
          </button>
        </div>

        {/* Navigation */}
        <nav className="flex-1 py-4 overflow-y-auto">
          <ul className="space-y-1 px-2">
            {visibleNavigation.map((item) => (
              <li key={item.name}>
                <NavLink
                  to={item.href}
                  onClick={onClose}
                  className={({ isActive }) =>
                    cn(
                      'flex items-center gap-3 px-3 py-2.5 rounded-md text-sm font-medium transition-colors',
                      isActive
                        ? 'bg-primary-600 text-white'
                        : 'text-gray-300 hover:bg-gray-800 hover:text-white'
                    )
                  }
                >
                  <item.icon className="h-5 w-5 flex-shrink-0" />
                  {item.name}
                </NavLink>
              </li>
            ))}

            {user?.isAdmin && (
              <>
                <li className="pt-4 pb-1">
                  <p className="px-3 text-xs font-semibold text-gray-500 uppercase tracking-wider">
                    Administration
                  </p>
                </li>
                <li>
                  <NavLink
                    to="/admin/users"
                    onClick={onClose}
                    className={({ isActive }) =>
                      cn(
                        'flex items-center gap-3 px-3 py-2.5 rounded-md text-sm font-medium transition-colors',
                        isActive
                          ? 'bg-primary-600 text-white'
                          : 'text-gray-300 hover:bg-gray-800 hover:text-white'
                      )
                    }
                  >
                    <ShieldCheckIcon className="h-5 w-5 flex-shrink-0" />
                    Benutzerverwaltung
                  </NavLink>
                </li>
              </>
            )}
          </ul>
        </nav>

        {/* User info at bottom */}
        <div className="p-4 border-t border-gray-700">
          <div className="text-xs text-gray-400">
            <p className="font-medium text-gray-300">{user?.name || user?.username}</p>
            <p>{user?.isAdmin ? 'Administrator' : user?.group?.name || 'Benutzer'}</p>
          </div>
        </div>
      </aside>
    </>
  );
}
