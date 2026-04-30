import React, { useEffect } from 'react';
import { XMarkIcon, ExclamationCircleIcon } from '@heroicons/react/24/outline';
import { create } from 'zustand';

interface ToastMessage {
  id: number;
  message: string;
  type: 'error' | 'success';
}

interface ToastStore {
  toasts: ToastMessage[];
  addToast: (message: string, type?: 'error' | 'success') => void;
  removeToast: (id: number) => void;
}

let nextId = 0;

export const useToastStore = create<ToastStore>((set) => ({
  toasts: [],
  addToast: (message, type = 'error') => {
    const id = nextId++;
    set((s) => ({ toasts: [...s.toasts, { id, message, type }] }));
    setTimeout(() => set((s) => ({ toasts: s.toasts.filter(t => t.id !== id) })), 6000);
  },
  removeToast: (id) => set((s) => ({ toasts: s.toasts.filter(t => t.id !== id) })),
}));

export function ToastContainer() {
  const { toasts, removeToast } = useToastStore();

  if (toasts.length === 0) return null;

  return (
    <div className="fixed bottom-4 right-4 z-50 space-y-2 max-w-md">
      {toasts.map(toast => (
        <div key={toast.id}
          className={`flex items-start gap-3 px-4 py-3 rounded-lg shadow-lg border text-sm animate-slide-in ${
            toast.type === 'error'
              ? 'bg-red-50 border-red-200 text-red-800'
              : 'bg-green-50 border-green-200 text-green-800'
          }`}>
          <ExclamationCircleIcon className={`h-5 w-5 flex-shrink-0 mt-0.5 ${toast.type === 'error' ? 'text-red-500' : 'text-green-500'}`} />
          <p className="flex-1">{toast.message}</p>
          <button onClick={() => removeToast(toast.id)} className="flex-shrink-0 text-gray-400 hover:text-gray-600">
            <XMarkIcon className="h-4 w-4" />
          </button>
        </div>
      ))}
    </div>
  );
}
