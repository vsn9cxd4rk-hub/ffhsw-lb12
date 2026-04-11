import React, { useState, useEffect } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { CheckCircleIcon, XCircleIcon } from '@heroicons/react/24/solid';
import { Article, InspectionCriterion } from '../../types';
import { Modal } from '../ui/Modal';
import { Input } from '../ui/Input';
import { Textarea } from '../ui/Textarea';
import { Button } from '../ui/Button';
import { Badge } from '../ui/Badge';
import { inspectionsApi } from '../../api/inspections';

interface Props {
  isOpen: boolean;
  onClose: () => void;
  articles: Article[];
  preselectedArticle?: Article;
}

export function CriteriaInspectionModal({ isOpen, onClose, articles, preselectedArticle }: Props) {
  const queryClient = useQueryClient();
  const [articleId, setArticleId] = useState<number | ''>('');
  const [inspectedAt, setInspectedAt] = useState(new Date().toISOString().split('T')[0]);
  const [inspectedBy, setInspectedBy] = useState('');
  const [notes, setNotes] = useState('');
  const [criterionResults, setCriterionResults] = useState<Record<number, 'io' | 'nio'>>({});
  const [error, setError] = useState('');

  useEffect(() => {
    if (isOpen) {
      setArticleId(preselectedArticle?.id || '');
      setInspectedAt(new Date().toISOString().split('T')[0]);
      setInspectedBy('');
      setNotes('');
      setCriterionResults({});
      setError('');
    }
  }, [isOpen, preselectedArticle]);

  const selectedArticle = articles.find(a => a.id === articleId);

  const { data: criteriaRes } = useQuery({
    queryKey: ['inspection-criteria', articleId],
    queryFn: () => inspectionsApi.getCriteria(articleId as number),
    enabled: !!articleId,
  });
  const criteria: InspectionCriterion[] = criteriaRes?.data?.data || [];

  // Reset criterion results when criteria change
  useEffect(() => {
    setCriterionResults({});
  }, [articleId]);

  const allCriteriaEvaluated = criteria.length === 0 || criteria.every(c => criterionResults[c.id] !== undefined);
  const allIo = criteria.length > 0 && criteria.every(c => criterionResults[c.id] === 'io');
  const hasAnyResult = Object.keys(criterionResults).length > 0;

  // Compute next due date
  let nextDueDateStr = '';
  if (selectedArticle?.inspectionInterval && inspectedAt) {
    const d = new Date(inspectedAt);
    d.setMonth(d.getMonth() + selectedArticle.inspectionInterval);
    nextDueDateStr = d.toLocaleDateString('de-DE');
  }

  const mutation = useMutation({
    mutationFn: () => {
      return inspectionsApi.create({
        articleId: articleId as number,
        inspectedAt,
        inspectedBy,
        notes: notes || undefined,
        criterionResults: criteria.length > 0
          ? criteria.map(c => ({ criterionId: c.id, result: criterionResults[c.id] }))
          : undefined,
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['inspections'] });
      queryClient.invalidateQueries({ queryKey: ['due-inspections'] });
      onClose();
    },
    onError: (err: unknown) => {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Speichern fehlgeschlagen';
      setError(msg);
    },
  });

  const canSave = articleId && inspectedAt && inspectedBy && allCriteriaEvaluated;

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Prüfung dokumentieren" size="lg"
      footer={
        <>
          <Button variant="secondary" onClick={onClose}>Abbrechen</Button>
          <Button variant="primary" onClick={() => mutation.mutate()} loading={mutation.isPending} disabled={!canSave}>
            Prüfung speichern
          </Button>
        </>
      }
    >
      <div className="space-y-4">
        {error && <div className="bg-red-50 border border-red-200 text-red-700 text-sm px-4 py-3 rounded-md">{error}</div>}

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1.5">Artikel <span className="text-red-500">*</span></label>
            <select value={articleId} onChange={(e) => setArticleId(e.target.value ? parseInt(e.target.value) : '')}
              disabled={!!preselectedArticle}
              className="block w-full px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white disabled:bg-gray-100">
              <option value="">-- Artikel wählen --</option>
              {articles.map(a => (
                <option key={a.id} value={a.id}>{a.name}{a.inventoryNumber ? ` (${a.inventoryNumber})` : ''}</option>
              ))}
            </select>
          </div>
          <Input label="Prüfdatum" value={inspectedAt} onChange={(e) => setInspectedAt(e.target.value)} type="date" required />
        </div>

        <Input label="Prüfer" value={inspectedBy} onChange={(e) => setInspectedBy(e.target.value)} required />

        {/* Criteria section */}
        {criteria.length > 0 && (
          <div>
            <h4 className="text-sm font-medium text-gray-700 mb-2">Prüfkriterien</h4>
            <div className="border border-gray-200 rounded-lg divide-y divide-gray-200">
              {criteria.map(c => {
                const result = criterionResults[c.id];
                return (
                  <div key={c.id} className="flex items-center justify-between px-4 py-2.5">
                    <span className="text-sm text-gray-700">{c.name}</span>
                    <div className="flex items-center gap-2">
                      <button
                        type="button"
                        onClick={() => setCriterionResults(prev => ({ ...prev, [c.id]: 'io' }))}
                        className={`flex items-center gap-1 px-3 py-1.5 rounded-md text-sm font-medium border transition-colors ${
                          result === 'io'
                            ? 'bg-green-100 border-green-500 text-green-700'
                            : 'bg-white border-gray-300 text-gray-500 hover:bg-green-50 hover:border-green-300'
                        }`}
                      >
                        <CheckCircleIcon className="h-4 w-4" /> io
                      </button>
                      <button
                        type="button"
                        onClick={() => setCriterionResults(prev => ({ ...prev, [c.id]: 'nio' }))}
                        className={`flex items-center gap-1 px-3 py-1.5 rounded-md text-sm font-medium border transition-colors ${
                          result === 'nio'
                            ? 'bg-red-100 border-red-500 text-red-700'
                            : 'bg-white border-gray-300 text-gray-500 hover:bg-red-50 hover:border-red-300'
                        }`}
                      >
                        <XCircleIcon className="h-4 w-4" /> nio
                      </button>
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Auto-computed result */}
            {hasAnyResult && (
              <div className="mt-3 flex items-center gap-2">
                <span className="text-sm font-medium text-gray-700">Gesamtergebnis:</span>
                {allCriteriaEvaluated ? (
                  allIo
                    ? <Badge variant="success">Bestanden</Badge>
                    : <Badge variant="danger">Nicht bestanden</Badge>
                ) : (
                  <span className="text-sm text-gray-400">Alle Kriterien bewerten...</span>
                )}
              </div>
            )}
          </div>
        )}

        {articleId && criteria.length === 0 && (
          <div className="bg-yellow-50 border border-yellow-200 text-yellow-700 text-sm px-4 py-3 rounded-md">
            Für diesen Artikel sind keine Prüfkriterien hinterlegt. Bitte weisen Sie dem Artikel eine Unterklasse mit Kriterien zu.
          </div>
        )}

        <Textarea label="Bemerkungen" value={notes} onChange={(e) => setNotes(e.target.value)} rows={2} />

        {nextDueDateStr && (
          <div className="bg-blue-50 border border-blue-200 text-blue-700 text-sm px-4 py-3 rounded-md">
            Nächste Prüfung fällig am: <strong>{nextDueDateStr}</strong>
          </div>
        )}
      </div>
    </Modal>
  );
}
