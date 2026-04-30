import React, { useState, useMemo } from 'react';
import { cn } from '../../utils/cn';
import { ChevronUpIcon, ChevronDownIcon } from '@heroicons/react/24/solid';

interface Column<T> {
  key: string;
  header: string;
  render?: (row: T) => React.ReactNode;
  className?: string;
  sortable?: boolean;
  sortValue?: (row: T) => string | number | null;
}

interface TableProps<T> {
  columns: Column<T>[];
  data: T[];
  loading?: boolean;
  emptyMessage?: string;
  onRowClick?: (row: T) => void;
  keyExtractor?: (row: T) => string | number;
}

function SkeletonRow({ cols }: { cols: number }) {
  return (
    <tr>
      {Array.from({ length: cols }).map((_, i) => (
        <td key={i} className="px-6 py-4">
          <div className="h-4 bg-gray-200 rounded animate-pulse" />
        </td>
      ))}
    </tr>
  );
}

export function Table<T extends object>({
  columns,
  data,
  loading = false,
  emptyMessage = 'Keine Einträge vorhanden.',
  onRowClick,
  keyExtractor,
}: TableProps<T>) {
  const [sortKey, setSortKey] = useState<string | null>(null);
  const [sortDir, setSortDir] = useState<'asc' | 'desc'>('asc');

  const handleSort = (col: Column<T>) => {
    if (!col.sortable) return;
    if (sortKey === col.key) {
      setSortDir(d => d === 'asc' ? 'desc' : 'asc');
    } else {
      setSortKey(col.key);
      setSortDir('asc');
    }
  };

  const sortedData = useMemo(() => {
    if (!sortKey) return data;
    const col = columns.find(c => c.key === sortKey);
    if (!col) return data;

    return [...data].sort((a, b) => {
      let va: string | number | null;
      let vb: string | number | null;

      if (col.sortValue) {
        va = col.sortValue(a);
        vb = col.sortValue(b);
      } else {
        va = (a as Record<string, unknown>)[col.key] as string | number | null;
        vb = (b as Record<string, unknown>)[col.key] as string | number | null;
      }

      if (va == null && vb == null) return 0;
      if (va == null) return 1;
      if (vb == null) return -1;

      let cmp: number;
      if (typeof va === 'number' && typeof vb === 'number') {
        cmp = va - vb;
      } else {
        cmp = String(va).localeCompare(String(vb), 'de');
      }

      return sortDir === 'asc' ? cmp : -cmp;
    });
  }, [data, sortKey, sortDir, columns]);

  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">
        <thead className="bg-gray-50">
          <tr>
            {columns.map((col) => (
              <th
                key={col.key}
                scope="col"
                onClick={() => handleSort(col)}
                className={cn(
                  'px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider',
                  col.sortable && 'cursor-pointer hover:text-gray-700 select-none',
                  col.className
                )}
              >
                <span className="flex items-center gap-1">
                  {col.header}
                  {col.sortable && sortKey === col.key && (
                    sortDir === 'asc'
                      ? <ChevronUpIcon className="h-3 w-3" />
                      : <ChevronDownIcon className="h-3 w-3" />
                  )}
                  {col.sortable && sortKey !== col.key && col.header && (
                    <span className="h-3 w-3 inline-block opacity-0 group-hover:opacity-30">
                      <ChevronUpIcon className="h-3 w-3 text-gray-300" />
                    </span>
                  )}
                </span>
              </th>
            ))}
          </tr>
        </thead>
        <tbody className="bg-white divide-y divide-gray-200">
          {loading ? (
            Array.from({ length: 5 }).map((_, i) => (
              <SkeletonRow key={i} cols={columns.length} />
            ))
          ) : sortedData.length === 0 ? (
            <tr>
              <td
                colSpan={columns.length}
                className="px-6 py-12 text-center text-sm text-gray-500"
              >
                {emptyMessage}
              </td>
            </tr>
          ) : (
            sortedData.map((row, idx) => (
              <tr
                key={keyExtractor ? keyExtractor(row) : idx}
                className={cn(
                  'hover:bg-gray-50 transition-colors',
                  onRowClick ? 'cursor-pointer' : ''
                )}
                onClick={() => onRowClick?.(row)}
              >
                {columns.map((col) => (
                  <td
                    key={col.key}
                    className={cn('px-6 py-4 text-sm text-gray-900 whitespace-nowrap', col.className)}
                  >
                    {col.render ? col.render(row) : ((row as Record<string, unknown>)[col.key] as React.ReactNode)}
                  </td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}
