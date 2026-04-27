import React, { useState, useRef } from 'react';
import { useMutation } from '@tanstack/react-query';
import { ArrowUpTrayIcon, DocumentTextIcon, InformationCircleIcon } from '@heroicons/react/24/outline';
import { CheckCircleIcon, XCircleIcon } from '@heroicons/react/24/solid';
import { settingsApi } from '../../api/settings';
import { Button } from '../../components/ui/Button';

type ImportType = 'articles' | 'inspections';

interface ImportResult {
  imported: number;
  errors: Array<{ row: number; message: string }>;
  total: number;
}

const ARTICLE_COLUMNS = [
  { name: 'name', label: 'Bezeichnung', required: true, example: 'Atemschutzmaske' },
  { name: 'inventoryNumber', label: 'Inventarnummer', required: false, example: 'INV-2024-001' },
  { name: 'manufacturer', label: 'Hersteller', required: false, example: 'Dräger' },
  { name: 'articleType', label: 'Typ', required: false, example: 'Atemschutz' },
  { name: 'description', label: 'Beschreibung', required: false, example: 'Vollmaske Klasse 3' },
  { name: 'inspectionInterval', label: 'Prüfintervall (Monate)', required: false, example: '12' },
  { name: 'value', label: 'Wert (EUR)', required: false, example: '450.00' },
  { name: 'ean', label: 'EAN-Code', required: false, example: '4012345678901' },
  { name: 'serialNumber', label: 'Seriennummer', required: false, example: 'SN-123456' },
  { name: 'din', label: 'DIN-Norm', required: false, example: 'DIN 14592' },
  { name: 'specification', label: 'Spezifikation', required: false, example: 'EN 136 Klasse 3' },
  { name: 'manufacturingDate', label: 'Herstellerdatum (YYYY-MM-DD)', required: false, example: '2023-06-15' },
  { name: 'warehouse', label: 'Lagerort (Name)', required: false, example: 'Gerätehalle' },
  { name: 'deviceClass', label: 'Geräteklasse (Name)', required: false, example: 'Sondergerät' },
  { name: 'deviceSubclass', label: 'Unterklasse (Name)', required: false, example: 'Elektrische Geräte' },
  { name: 'designationLB', label: 'Bezeichnung LB', required: false, example: 'LB12' },
  { name: 'commissionedDate', label: 'Indienststellung (YYYY-MM-DD)', required: false, example: '2020-01-15' },
  { name: 'decommissionedDate', label: 'Außerdienststellung (YYYY-MM-DD)', required: false, example: '' },
  { name: 'communityInventoryNumber', label: 'Inv.-Nr. Gemeinde (DOPPIK)', required: false, example: 'GEM-2024-001' },
  { name: 'mpFeuerInventoryNumber', label: 'Inv.-Nr. MP Feuer', required: false, example: 'MPF-001' },
  { name: 'retirementPeriodMonths', label: 'Aussonderungsfrist (Monate)', required: false, example: '180' },
];

const INSPECTION_COLUMNS = [
  { name: 'inventoryNumber', label: 'Inventarnummer', required: true, example: 'INV-2024-001' },
  { name: 'inspectedAt', label: 'Prüfdatum (YYYY-MM-DD)', required: true, example: '2024-03-15' },
  { name: 'inspectedBy', label: 'Prüfer', required: true, example: 'Max Mustermann' },
  { name: 'result', label: 'Ergebnis (passed/failed)', required: true, example: 'passed' },
  { name: 'inspectionType', label: 'Prüfart (Name)', required: false, example: 'Sicht- und Funktionsprüfung' },
  { name: 'notes', label: 'Bemerkungen', required: false, example: 'Keine Mängel' },
];

function parseCsv(text: string): Array<Record<string, string>> {
  const lines = text.split(/\r?\n/).filter(l => l.trim());
  if (lines.length < 2) return [];

  // Parse header - support both ; and , as delimiter, detect from header
  const delimiter = lines[0].includes(';') ? ';' : ',';

  const parseRow = (line: string): string[] => {
    const values: string[] = [];
    let current = '';
    let inQuotes = false;
    for (let i = 0; i < line.length; i++) {
      const ch = line[i];
      if (inQuotes) {
        if (ch === '"' && line[i + 1] === '"') { current += '"'; i++; }
        else if (ch === '"') inQuotes = false;
        else current += ch;
      } else {
        if (ch === '"') inQuotes = true;
        else if (ch === delimiter) { values.push(current.trim()); current = ''; }
        else current += ch;
      }
    }
    values.push(current.trim());
    return values;
  };

  const headers = parseRow(lines[0]);
  const rows: Array<Record<string, string>> = [];

  for (let i = 1; i < lines.length; i++) {
    const values = parseRow(lines[i]);
    const row: Record<string, string> = {};
    headers.forEach((h, idx) => {
      if (values[idx] !== undefined && values[idx] !== '') row[h] = values[idx];
    });
    if (Object.keys(row).length > 0) rows.push(row);
  }

  return rows;
}

export function DataImportSettings() {
  const [importType, setImportType] = useState<ImportType>('articles');
  const [parsedData, setParsedData] = useState<Array<Record<string, string>>>([]);
  const [fileName, setFileName] = useState('');
  const [result, setResult] = useState<ImportResult | null>(null);
  const fileRef = useRef<HTMLInputElement>(null);

  const columns = importType === 'articles' ? ARTICLE_COLUMNS : INSPECTION_COLUMNS;

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    setFileName(file.name);
    setResult(null);

    const reader = new FileReader();
    reader.onload = (evt) => {
      const text = evt.target?.result as string;
      const rows = parseCsv(text);
      setParsedData(rows);
    };
    reader.readAsText(file, 'UTF-8');
  };

  const resetFile = () => {
    setParsedData([]);
    setFileName('');
    setResult(null);
    if (fileRef.current) fileRef.current.value = '';
  };

  const importMutation = useMutation({
    mutationFn: () => {
      if (importType === 'articles') return settingsApi.importArticles(parsedData);
      return settingsApi.importInspections(parsedData);
    },
    onSuccess: (res) => {
      setResult(res.data.data);
    },
    onError: (err: unknown) => {
      const msg = (err as { response?: { data?: { error?: string } } })?.response?.data?.error || 'Import fehlgeschlagen';
      setResult({ imported: 0, errors: [{ row: 0, message: msg }], total: parsedData.length });
    },
  });

  // Determine which columns are present in the parsed data
  const presentHeaders = parsedData.length > 0
    ? [...new Set(parsedData.flatMap(r => Object.keys(r)))]
    : [];

  const previewRows = parsedData.slice(0, 5);
  const previewHeaders = presentHeaders.slice(0, 8); // Limit preview columns

  return (
    <div className="space-y-6">
      {/* Import type selection */}
      <div className="flex items-end gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Import-Typ</label>
          <select value={importType}
            onChange={(e) => { setImportType(e.target.value as ImportType); resetFile(); }}
            className="block w-64 px-3 py-2.5 border border-gray-300 rounded-md text-sm focus:border-primary-500 focus:ring-1 focus:ring-primary-500 outline-none bg-white">
            <option value="articles">Bestandsliste (Artikel)</option>
            <option value="inspections">Prüfungen</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">CSV-Datei</label>
          <div className="flex items-center gap-2">
            <input ref={fileRef} type="file" accept=".csv,.txt" onChange={handleFileChange}
              className="block text-sm text-gray-500 file:mr-3 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-medium file:bg-primary-50 file:text-primary-700 hover:file:bg-primary-100 cursor-pointer" />
            {fileName && (
              <button onClick={resetFile} className="text-xs text-gray-400 hover:text-red-500">Zurücksetzen</button>
            )}
          </div>
        </div>
      </div>

      {/* Column info */}
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
        <div className="flex items-start gap-2">
          <InformationCircleIcon className="h-5 w-5 text-blue-500 mt-0.5 flex-shrink-0" />
          <div>
            <p className="text-sm font-medium text-blue-800 mb-2">
              CSV-Format: Trennzeichen Semikolon (;) oder Komma (,), UTF-8, mit Header-Zeile
            </p>
            <div className="text-xs text-blue-700">
              <p className="font-medium mb-1">Erwartete Spalten:</p>
              <div className="grid grid-cols-2 gap-x-6 gap-y-0.5">
                {columns.map(c => (
                  <span key={c.name}>
                    <span className={c.required ? 'font-bold' : ''}>{c.name}</span>
                    {c.required && <span className="text-red-500">*</span>}
                    {' '}<span className="text-blue-500">({c.label})</span>
                  </span>
                ))}
              </div>
              <p className="mt-2 text-blue-600">* = Pflichtfeld</p>
            </div>
          </div>
        </div>
      </div>

      {/* Example CSV */}
      <details className="bg-gray-50 border border-gray-200 rounded-lg">
        <summary className="px-4 py-2 cursor-pointer text-sm font-medium text-gray-700 flex items-center gap-2">
          <DocumentTextIcon className="h-4 w-4" /> Beispiel-CSV anzeigen
        </summary>
        <div className="px-4 pb-3">
          <pre className="text-xs bg-white border rounded p-3 overflow-x-auto font-mono">
            {columns.map(c => c.name).join(';')}{'\n'}
            {columns.map(c => c.example).join(';')}
          </pre>
        </div>
      </details>

      {/* Preview table */}
      {parsedData.length > 0 && !result && (
        <div className="space-y-3">
          <h4 className="text-sm font-medium text-gray-700">
            Vorschau ({previewRows.length} von {parsedData.length} Zeilen)
          </h4>
          <div className="overflow-x-auto border border-gray-200 rounded-lg">
            <table className="min-w-full divide-y divide-gray-200 text-sm">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-3 py-2 text-left text-xs font-medium text-gray-500">#</th>
                  {previewHeaders.map(h => (
                    <th key={h} className="px-3 py-2 text-left text-xs font-medium text-gray-500">{h}</th>
                  ))}
                  {presentHeaders.length > 8 && (
                    <th className="px-3 py-2 text-left text-xs font-medium text-gray-400">+{presentHeaders.length - 8} weitere</th>
                  )}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {previewRows.map((row, i) => (
                  <tr key={i}>
                    <td className="px-3 py-1.5 text-gray-400">{i + 1}</td>
                    {previewHeaders.map(h => (
                      <td key={h} className="px-3 py-1.5 text-gray-700 max-w-[200px] truncate">{row[h] || ''}</td>
                    ))}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <Button variant="primary" icon={<ArrowUpTrayIcon />}
            onClick={() => importMutation.mutate()}
            loading={importMutation.isPending}>
            Importieren ({parsedData.length} Datensätze)
          </Button>
        </div>
      )}

      {/* Result */}
      {result && (
        <div className="space-y-3">
          {result.imported > 0 && (
            <div className="flex items-center gap-2 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-lg">
              <CheckCircleIcon className="h-5 w-5 text-green-500" />
              <span className="text-sm font-medium">{result.imported} von {result.total} Datensätzen erfolgreich importiert</span>
            </div>
          )}
          {result.errors.length > 0 && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
              <div className="flex items-center gap-2 mb-2">
                <XCircleIcon className="h-5 w-5 text-red-500" />
                <span className="text-sm font-medium text-red-700">{result.errors.length} Fehler</span>
              </div>
              <ul className="space-y-1 max-h-48 overflow-y-auto">
                {result.errors.map((e, i) => (
                  <li key={i} className="text-xs text-red-600">
                    {e.row > 0 && <span className="font-medium">Zeile {e.row}:</span>} {e.message}
                  </li>
                ))}
              </ul>
            </div>
          )}
          <Button variant="secondary" onClick={resetFile}>Neuer Import</Button>
        </div>
      )}
    </div>
  );
}
