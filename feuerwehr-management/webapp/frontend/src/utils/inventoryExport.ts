import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { Article } from '../types';

export function exportInventoryCsv(articles: Article[]) {
  const headers = [
    'name', 'inventoryNumber', 'manufacturer', 'articleType', 'description',
    'inspectionInterval', 'value', 'serialNumber', 'din', 'specification',
    'manufacturingDate', 'warehouse', 'deviceClass', 'deviceSubclass',
    'designationLB', 'commissionedDate', 'decommissionedDate',
    'communityInventoryNumber', 'mpFeuerInventoryNumber', 'retirementPeriodMonths',
  ];

  const rows = articles.map(a => [
    a.name,
    a.inventoryNumber || '',
    a.manufacturer || '',
    a.articleType || '',
    a.description || '',
    a.inspectionInterval?.toString() || '',
    a.value?.toString() || '',
    a.serialNumber || '',
    a.din || '',
    a.specification || '',
    a.manufacturingDate ? a.manufacturingDate.split('T')[0] : '',
    a.warehouse?.name || '',
    a.deviceSubclass?.deviceClass?.name || '',
    a.deviceSubclass?.name || '',
    a.designationLB || '',
    a.commissionedDate ? a.commissionedDate.split('T')[0] : '',
    a.decommissionedDate ? a.decommissionedDate.split('T')[0] : '',
    a.communityInventoryNumber || '',
    a.mpFeuerInventoryNumber || '',
    a.retirementPeriodMonths?.toString() || '',
  ]);

  const csvContent = [headers, ...rows]
    .map(row => row.map(cell => '"' + String(cell).replace(/"/g, '""') + '"').join(';'))
    .join('\n');

  const bom = '﻿';
  const blob = new Blob([bom + csvContent], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'Bestandsliste_' + new Date().toISOString().split('T')[0] + '.csv';
  a.click();
  URL.revokeObjectURL(url);
}

export function exportInventoryPdf(articles: Article[], title: string) {
  const doc = new jsPDF('l', 'mm', 'a4');
  const pageWidth = doc.internal.pageSize.getWidth();
  const pageHeight = doc.internal.pageSize.getHeight();
  const margin = 10;
  const exportDate = new Date().toLocaleDateString('de-DE');

  doc.setFontSize(14);
  doc.setFont('helvetica', 'bold');
  doc.text('Bestandsliste - ' + title, margin, 15);
  doc.setFontSize(8);
  doc.setFont('helvetica', 'normal');
  doc.setTextColor(120);
  doc.text(articles.length + ' Artikel | Erstellt am ' + exportDate, margin, 21);
  doc.setTextColor(0);

  const headers = [
    'Inv.-Nr.', 'Bezeichnung', 'Hersteller', 'Geräteklasse',
    'Lagerort', 'S/N', 'DIN', 'Intervall', 'Wert', 'Status',
  ];

  const rows = articles.map(a => [
    a.inventoryNumber || '-',
    a.name,
    a.manufacturer || '-',
    a.deviceSubclass?.deviceClass?.name || '-',
    a.warehouse?.name || '-',
    a.serialNumber || '-',
    a.din || '-',
    a.inspectionInterval ? a.inspectionInterval + ' Mon.' : '-',
    a.value ? a.value.toLocaleString('de-DE', { minimumFractionDigits: 2 }) + ' EUR' : '-',
    a.isDecommissioned ? 'Außer Dienst' : 'Aktiv',
  ]);

  autoTable(doc, {
    startY: 25,
    head: [headers],
    body: rows,
    margin: { left: margin, right: margin },
    styles: { fontSize: 7, cellPadding: 1.5 },
    headStyles: { fillColor: [66, 66, 66], textColor: 255, fontStyle: 'bold', fontSize: 7 },
    alternateRowStyles: { fillColor: [245, 245, 245] },
    didDrawPage: (data) => {
      doc.setFontSize(7);
      doc.setTextColor(150);
      doc.text(
        'Seite ' + doc.getCurrentPageInfo().pageNumber,
        pageWidth - margin,
        pageHeight - 5,
        { align: 'right' },
      );
      doc.setTextColor(0);
    },
  });

  doc.save('Bestandsliste_' + new Date().toISOString().split('T')[0] + '.pdf');
}
