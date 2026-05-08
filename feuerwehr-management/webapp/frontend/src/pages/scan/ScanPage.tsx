import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../store/auth.store';
import client from '../../api/client';
import { Article } from '../../types';

export function ScanPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const isAuthenticated = useAuthStore(s => s.isAuthenticated);
  const [status, setStatus] = useState<'loading' | 'not-found' | 'redirecting'>('loading');

  useEffect(() => {
    if (!isAuthenticated) {
      navigate(`/login?redirect=/scan/${id}`, { replace: true });
      return;
    }
    if (!id) { setStatus('not-found'); return; }

    (async () => {
      try {
        // Find article by inventory number
        const articlesRes = await client.get<{ data: Article[] }>('/inventory/articles', {
          params: { search: id, limit: 50 },
        });
        const articles = articlesRes.data.data || [];
        const article = articles.find(a => a.inventoryNumber === id) || articles[0];

        if (!article) { setStatus('not-found'); return; }

        // Check if article is due for inspection
        const dueRes = await client.get<{ data: Article[] }>('/inspections/due');
        const dueArticles = dueRes.data.data || [];
        const isDue = dueArticles.some(a => a.id === article.id);

        setStatus('redirecting');

        if (isDue) {
          navigate(`/inspections`, { replace: true, state: { inspectArticleId: article.id } });
        } else {
          navigate(`/inventory/${article.id}`, { replace: true });
        }
      } catch {
        setStatus('not-found');
      }
    })();
  }, [id, isAuthenticated, navigate]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="text-center">
        {status === 'loading' && (
          <>
            <div className="animate-spin h-8 w-8 border-4 border-primary-500 border-t-transparent rounded-full mx-auto mb-4" />
            <p className="text-gray-600">Artikel wird geladen...</p>
          </>
        )}
        {status === 'redirecting' && (
          <p className="text-gray-600">Weiterleitung...</p>
        )}
        {status === 'not-found' && (
          <div>
            <p className="text-lg font-medium text-gray-900 mb-2">Artikel nicht gefunden</p>
            <p className="text-sm text-gray-500 mb-4">Kein Artikel mit der Kennung "{id}" vorhanden.</p>
            <button onClick={() => navigate('/inventory')} className="text-primary-600 hover:underline text-sm">
              Zur Bestandsliste
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
