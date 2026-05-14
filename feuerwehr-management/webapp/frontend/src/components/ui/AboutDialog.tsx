import React, { Fragment } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import { XMarkIcon } from '@heroicons/react/24/outline';
import { useNavigate } from 'react-router-dom';

interface AboutDialogProps {
  isOpen: boolean;
  onClose: () => void;
}

export function AboutDialog({ isOpen, onClose }: AboutDialogProps) {
  const navigate = useNavigate();

  function openThirdParty() {
    onClose();
    navigate('/third-party-licenses');
  }

  return (
    <Transition appear show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-50" onClose={onClose}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-200"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-150"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-black/40" />
        </Transition.Child>

        <div className="fixed inset-0 overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-200"
              enterFrom="opacity-0 scale-95"
              enterTo="opacity-100 scale-100"
              leave="ease-in duration-150"
              leaveFrom="opacity-100 scale-100"
              leaveTo="opacity-0 scale-95"
            >
              <Dialog.Panel className="w-full max-w-sm bg-white rounded-xl shadow-xl transform transition-all">
                <div className="flex items-center justify-between px-6 py-4 border-b border-gray-200">
                  <Dialog.Title className="text-base font-semibold text-gray-900">
                    Über diese Anwendung
                  </Dialog.Title>
                  <button
                    onClick={onClose}
                    className="text-gray-400 hover:text-gray-600 transition-colors"
                  >
                    <XMarkIcon className="h-5 w-5" />
                  </button>
                </div>

                <div className="px-6 py-6 flex flex-col items-center text-center gap-4">
                  <div className="h-16 w-16 rounded-full bg-red-600 flex items-center justify-center">
                    <span className="text-white font-bold text-2xl">FF</span>
                  </div>

                  <div>
                    <h2 className="text-lg font-semibold text-gray-900">
                      Feuerwehr Management System
                    </h2>
                    <p className="text-sm text-gray-500 mt-0.5">Version 1.0.0</p>
                  </div>

                  <p className="text-sm text-gray-600 leading-relaxed">
                    Verwaltungssoftware für die<br />
                    <span className="font-medium text-gray-800">
                      Freiwillige Feuerwehr der Gemeinde Heusweiler
                    </span>
                  </p>

                  <p className="text-xs text-gray-400">
                    © 2013–{new Date().getFullYear()} Freiwillige Feuerwehr der Gemeinde Heusweiler.<br />
                    Alle Rechte vorbehalten.
                  </p>
                </div>

                <div className="flex justify-between items-center px-6 py-4 border-t border-gray-200 bg-gray-50 rounded-b-xl">
                  <button
                    onClick={openThirdParty}
                    className="text-sm text-red-600 hover:underline"
                  >
                    Drittanbieter-Bibliotheken
                  </button>
                  <button
                    onClick={onClose}
                    className="px-4 py-2 text-sm font-medium text-white bg-red-600 hover:bg-red-700 rounded-lg transition-colors"
                  >
                    Schließen
                  </button>
                </div>
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
}
