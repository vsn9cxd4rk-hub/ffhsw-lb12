import React, { Fragment } from 'react';
import { Menu, Transition, Popover } from '@headlessui/react';
import {
  Bars3Icon,
  UserCircleIcon,
  ArrowRightOnRectangleIcon,
  KeyIcon,
  BellIcon,
  QuestionMarkCircleIcon,
  ExclamationTriangleIcon,
  ExclamationCircleIcon,
  InformationCircleIcon,
} from '@heroicons/react/24/outline';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';
import client from '../../api/client';

interface Notification {
  id: string;
  type: string;
  severity: 'red' | 'yellow' | 'info';
  title: string;
  message: string;
  link?: string;
}

interface HeaderProps {
  onMenuClick: () => void;
  pageTitle: string;
}

export function Header({ onMenuClick, pageTitle }: HeaderProps) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const { data: notifications } = useQuery({
    queryKey: ['notifications'],
    queryFn: () => client.get<{ data: Notification[] }>('/dashboard/notifications').then(r => r.data.data),
    refetchInterval: 5 * 60 * 1000,
  });

  const notifCount = notifications?.length || 0;
  const hasRed = notifications?.some(n => n.severity === 'red');

  return (
    <header className="bg-white border-b border-gray-200 h-16 flex items-center justify-between px-4 lg:px-6 sticky top-0 z-30">
      <div className="flex items-center gap-4">
        <button
          onClick={onMenuClick}
          className="lg:hidden p-2 rounded-md text-gray-500 hover:bg-gray-100"
        >
          <Bars3Icon className="h-6 w-6" />
        </button>
        <h1 className="text-lg font-semibold text-gray-900">{pageTitle}</h1>
      </div>

      <div className="flex items-center gap-3">
        {/* Help link */}
        <button onClick={() => navigate('/help')} className="p-2 rounded-md text-gray-500 hover:bg-gray-100" title="Hilfe">
          <QuestionMarkCircleIcon className="h-5 w-5" />
        </button>

        {/* Notifications */}
        <Popover as="div" className="relative">
          <Popover.Button className="p-2 rounded-md text-gray-500 hover:bg-gray-100 relative focus:outline-none">
            <BellIcon className="h-5 w-5" />
            {notifCount > 0 && (
              <span className={`absolute -top-0.5 -right-0.5 h-4.5 w-4.5 flex items-center justify-center rounded-full text-[10px] font-bold text-white ${hasRed ? 'bg-red-500' : 'bg-yellow-500'}`}
                style={{ minWidth: '18px', height: '18px', padding: '0 4px' }}>
                {notifCount}
              </span>
            )}
          </Popover.Button>

          <Transition
            as={Fragment}
            enter="transition ease-out duration-150"
            enterFrom="opacity-0 translate-y-1"
            enterTo="opacity-100 translate-y-0"
            leave="transition ease-in duration-100"
            leaveFrom="opacity-100 translate-y-0"
            leaveTo="opacity-0 translate-y-1"
          >
            <Popover.Panel className="absolute right-0 mt-2 w-96 bg-white rounded-lg shadow-xl border border-gray-200 focus:outline-none z-50">
              <div className="px-4 py-3 border-b border-gray-100">
                <p className="text-sm font-semibold text-gray-900">Benachrichtigungen</p>
              </div>
              <div className="max-h-80 overflow-y-auto">
                {(!notifications || notifications.length === 0) ? (
                  <div className="px-4 py-6 text-center text-sm text-gray-400">
                    Keine Benachrichtigungen
                  </div>
                ) : (
                  <ul className="divide-y divide-gray-100">
                    {notifications.map(n => (
                      <li key={n.id}>
                        <Popover.Button
                          as="button"
                          onClick={() => n.link && navigate(n.link)}
                          className="w-full text-left px-4 py-3 hover:bg-gray-50 transition-colors flex gap-3"
                        >
                          <div className="flex-shrink-0 mt-0.5">
                            {n.severity === 'red' && <ExclamationCircleIcon className="h-5 w-5 text-red-500" />}
                            {n.severity === 'yellow' && <ExclamationTriangleIcon className="h-5 w-5 text-yellow-500" />}
                            {n.severity === 'info' && <InformationCircleIcon className="h-5 w-5 text-blue-500" />}
                          </div>
                          <div className="min-w-0">
                            <p className={`text-sm font-medium ${n.severity === 'red' ? 'text-red-700' : n.severity === 'yellow' ? 'text-yellow-700' : 'text-gray-700'}`}>
                              {n.title}
                            </p>
                            <p className="text-xs text-gray-500 mt-0.5">{n.message}</p>
                          </div>
                        </Popover.Button>
                      </li>
                    ))}
                  </ul>
                )}
              </div>
            </Popover.Panel>
          </Transition>
        </Popover>

        {/* User menu */}
        <Menu as="div" className="relative">
          <Menu.Button className="flex items-center gap-2 p-1.5 rounded-md hover:bg-gray-100 transition-colors">
            <UserCircleIcon className="h-7 w-7 text-gray-500" />
            <span className="hidden sm:block text-sm font-medium text-gray-700">
              {user?.name || user?.username}
            </span>
          </Menu.Button>

          <Transition
            as={Fragment}
            enter="transition ease-out duration-100"
            enterFrom="opacity-0 scale-95"
            enterTo="opacity-100 scale-100"
            leave="transition ease-in duration-75"
            leaveFrom="opacity-100 scale-100"
            leaveTo="opacity-0 scale-95"
          >
            <Menu.Items className="absolute right-0 mt-1 w-48 bg-white rounded-md shadow-lg border border-gray-200 focus:outline-none">
              <div className="px-4 py-3 border-b border-gray-100">
                <p className="text-sm font-medium text-gray-900">{user?.name || user?.username}</p>
                <p className="text-xs text-gray-500">{user?.email || user?.username}</p>
              </div>
              <div className="py-1">
                <Menu.Item>
                  {({ active }) => (
                    <button
                      onClick={() => navigate('/password')}
                      className={`flex items-center gap-2 w-full px-4 py-2 text-sm ${active ? 'bg-gray-50' : ''} text-gray-700`}
                    >
                      <KeyIcon className="h-4 w-4" />
                      Passwort ändern
                    </button>
                  )}
                </Menu.Item>
                <Menu.Item>
                  {({ active }) => (
                    <button
                      onClick={logout}
                      className={`flex items-center gap-2 w-full px-4 py-2 text-sm ${active ? 'bg-gray-50' : ''} text-red-600`}
                    >
                      <ArrowRightOnRectangleIcon className="h-4 w-4" />
                      Abmelden
                    </button>
                  )}
                </Menu.Item>
              </div>
            </Menu.Items>
          </Transition>
        </Menu>
      </div>
    </header>
  );
}
