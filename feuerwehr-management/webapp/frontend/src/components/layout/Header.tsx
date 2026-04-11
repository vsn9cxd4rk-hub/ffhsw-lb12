import React, { Fragment } from 'react';
import { Menu, Transition } from '@headlessui/react';
import {
  Bars3Icon,
  UserCircleIcon,
  ArrowRightOnRectangleIcon,
  KeyIcon,
  BellIcon,
} from '@heroicons/react/24/outline';
import { useAuth } from '../../hooks/useAuth';
import { useNavigate } from 'react-router-dom';

interface HeaderProps {
  onMenuClick: () => void;
  pageTitle: string;
}

export function Header({ onMenuClick, pageTitle }: HeaderProps) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

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
        {/* Notifications placeholder */}
        <button className="p-2 rounded-md text-gray-500 hover:bg-gray-100 relative">
          <BellIcon className="h-5 w-5" />
        </button>

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
