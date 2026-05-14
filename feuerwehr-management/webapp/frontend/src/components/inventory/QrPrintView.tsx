import React, { useEffect, useRef, useState } from 'react';
import QRCode from 'qrcode';
import { Article } from '../../types';

interface QrEtikettProps {
  article: Article;
  baseUrl: string;
}

function QrEtikett({ article, baseUrl }: QrEtikettProps) {
  const canvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    if (canvasRef.current && article.inventoryNumber) {
      const url = `${baseUrl}/scan/${article.inventoryNumber}`;
      QRCode.toCanvas(canvasRef.current, url, { width: 100, margin: 1 });
    }
  }, [article, baseUrl]);

  return (
    <div className="qr-etikett" style={{ width: '40mm', height: '30mm', padding: '2mm', display: 'inline-flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', border: '0.5px dashed #ccc', pageBreakInside: 'avoid' }}>
      <canvas ref={canvasRef} style={{ width: '20mm', height: '20mm' }} />
      <div style={{ textAlign: 'center', marginTop: '1mm' }}>
        <div style={{ fontSize: '7pt', fontWeight: 'bold' }}>{article.inventoryNumber}</div>
        <div style={{ fontSize: '5pt', color: '#666' }}>{article.designationLB || 'LB12'}</div>
      </div>
    </div>
  );
}

interface QrSingleProps {
  article: Article;
  baseUrl: string;
  onPrint: () => void;
}

export function QrSingleView({ article, baseUrl, onPrint }: QrSingleProps) {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [qrDataUrl, setQrDataUrl] = useState('');

  useEffect(() => {
    if (article.inventoryNumber) {
      const url = `${baseUrl}/scan/${article.inventoryNumber}`;
      QRCode.toDataURL(url, { width: 200, margin: 2 }).then(setQrDataUrl);
    }
  }, [article, baseUrl]);

  return (
    <div className="flex flex-col items-center gap-3">
      {qrDataUrl && <img src={qrDataUrl} alt="QR-Code" className="w-48 h-48" />}
      <div className="text-center">
        <p className="text-sm font-bold text-gray-900">{article.inventoryNumber}</p>
        <p className="text-xs text-gray-500">{article.designationLB || 'LB12'}</p>
      </div>
      <p className="text-xs text-gray-400 break-all">{baseUrl}/scan/{article.inventoryNumber}</p>
      <button onClick={onPrint} className="mt-2 px-4 py-2 bg-primary-600 text-white text-sm rounded-md hover:bg-primary-700">
        Etikett drucken
      </button>
    </div>
  );
}

interface QrBatchProps {
  articles: Article[];
  baseUrl: string;
}

export function QrBatchPrintView({ articles, baseUrl }: QrBatchProps) {
  return (
    <div className="qr-batch-print">
      <style>{`
        @media print {
          body * { visibility: hidden; }
          .qr-batch-print, .qr-batch-print * { visibility: visible; }
          .qr-batch-print { position: absolute; top: 0; left: 0; width: 210mm; }
          .qr-grid { display: grid; grid-template-columns: repeat(5, 40mm); grid-auto-rows: 30mm; gap: 1mm; padding: 5mm; }
          .qr-etikett { border: 0.5px dashed #ccc !important; }
          .no-print { display: none !important; }
        }
      `}</style>
      <div className="no-print mb-4 flex items-center justify-between">
        <p className="text-sm text-gray-600">{articles.length} Etiketten (5 x {Math.ceil(articles.length / 5)} auf A4)</p>
        <button onClick={() => window.print()} className="px-4 py-2 bg-primary-600 text-white text-sm rounded-md hover:bg-primary-700">
          Drucken
        </button>
      </div>
      <div className="qr-grid" style={{ display: 'grid', gridTemplateColumns: 'repeat(5, 40mm)', gap: '1mm' }}>
        {articles.filter(a => a.inventoryNumber).map(a => (
          <QrEtikett key={a.id} article={a} baseUrl={baseUrl} />
        ))}
      </div>
    </div>
  );
}
