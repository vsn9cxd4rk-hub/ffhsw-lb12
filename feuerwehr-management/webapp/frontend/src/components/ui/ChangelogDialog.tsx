import { Fragment, useState } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import { XMarkIcon } from '@heroicons/react/24/outline';
import { CHANGELOG, currentChangelogEntry } from '../../config/changelog';
import { useIsAdmin } from '../../hooks/usePermission';
import { formatDate } from '../../utils/format';

interface ChangelogDialogProps {
  isOpen: boolean;
  onClose: () => void;
}

/** Changes of the running version compared to the previous one (opened from the About dialog). */
export function ChangelogDialog({ isOpen, onClose }: ChangelogDialogProps) {
  const isAdmin = useIsAdmin();
  const current = currentChangelogEntry(import.meta.env.VITE_APP_VERSION);
  const [selectedVersion, setSelectedVersion] = useState<string | null>(null);
  const entry = CHANGELOG.find((e) => e.version === selectedVersion) ?? current;

  const close = () => { setSelectedVersion(null); onClose(); };

  return (
    <Transition appear show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-60" onClose={close}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-200" enterFrom="opacity-0" enterTo="opacity-100"
          leave="ease-in duration-150" leaveFrom="opacity-100" leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-black/40" />
        </Transition.Child>

        <div className="fixed inset-0 overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-200" enterFrom="opacity-0 scale-95" enterTo="opacity-100 scale-100"
              leave="ease-in duration-150" leaveFrom="opacity-100 scale-100" leaveTo="opacity-0 scale-95"
            >
              <Dialog.Panel className="w-full max-w-2xl bg-white rounded-xl shadow-xl transform transition-all">
                <div className="flex items-center justify-between px-6 py-4 border-b border-gray-200">
                  <div>
                    <Dialog.Title className="text-base font-semibold text-gray-900">
                      {entry ? `Neu in Version ${entry.version}` : 'Änderungen'}
                    </Dialog.Title>
                    {entry && (
                      <p className="text-xs text-gray-500 mt-0.5">
                        {formatDate(entry.date)} · Änderungen gegenüber Version {entry.previousVersion}
                      </p>
                    )}
                  </div>
                  <button onClick={close} className="text-gray-400 hover:text-gray-600 transition-colors" aria-label="Schließen">
                    <XMarkIcon className="h-5 w-5" />
                  </button>
                </div>

                <div className="px-6 py-5 max-h-[65vh] overflow-y-auto space-y-5 text-sm">
                  {!entry ? (
                    <p className="text-gray-500">Für diese Version sind keine Änderungen hinterlegt.</p>
                  ) : (
                    <>
                      <p className="text-gray-700">{entry.summary}</p>

                      {entry.sections.map((section) => (
                        <section key={section.title}>
                          <h3 className="text-xs font-semibold uppercase tracking-wide text-red-600 mb-2">{section.title}</h3>
                          <ul className="list-disc pl-5 space-y-1.5 text-gray-700">
                            {section.items.map((item) => <li key={item}>{item}</li>)}
                          </ul>
                        </section>
                      ))}

                      {isAdmin && entry.adminNotes && entry.adminNotes.length > 0 && (
                        <section className="bg-yellow-50 border border-yellow-200 rounded-md px-4 py-3">
                          <h3 className="text-xs font-semibold uppercase tracking-wide text-yellow-800 mb-2">Hinweise für Administratoren</h3>
                          <ul className="list-disc pl-5 space-y-1.5 text-yellow-900">
                            {entry.adminNotes.map((note) => <li key={note}>{note}</li>)}
                          </ul>
                        </section>
                      )}
                    </>
                  )}
                </div>

                <div className="flex justify-between items-center px-6 py-4 border-t border-gray-200 bg-gray-50 rounded-b-xl">
                  {CHANGELOG.length > 1 ? (
                    <label className="text-xs text-gray-500 flex items-center gap-2">
                      Version
                      <select
                        value={entry?.version}
                        onChange={(e) => setSelectedVersion(e.target.value)}
                        className="border border-gray-300 rounded-md px-2 py-1 text-xs bg-white"
                      >
                        {CHANGELOG.map((e) => <option key={e.version} value={e.version}>{e.version}</option>)}
                      </select>
                    </label>
                  ) : <span />}
                  <button
                    onClick={close}
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
