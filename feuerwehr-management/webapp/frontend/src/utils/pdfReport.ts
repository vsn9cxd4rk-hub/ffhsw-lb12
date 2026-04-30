import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { ArticleInspection } from '../types';

export function generateInspectionReport(
  inspections: ArticleInspection[],
  exportedBy: string,
  filterLabel: string,
) {
  const doc = new jsPDF('p', 'mm', 'a4');
  const pageWidth = doc.internal.pageSize.getWidth();
  const pageHeight = doc.internal.pageSize.getHeight();
  const margin = 15;
  const exportDate = new Date().toLocaleDateString('de-DE');

  inspections.forEach((inspection, index) => {
    if (index > 0) doc.addPage();

    const article = inspection.article;
    let y = margin;

    doc.setFontSize(14);
    doc.setFont('helvetica', 'bold');
    doc.text('Prüfbericht', margin, y);
    y += 6;
    doc.setFontSize(9);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(100);
    doc.text(filterLabel, margin, y);
    doc.setTextColor(0);
    y += 8;

    doc.setDrawColor(200);
    doc.setLineWidth(0.5);
    doc.line(margin, y, pageWidth - margin, y);
    y += 6;

    doc.setFontSize(10);
    doc.setFont('helvetica', 'bold');
    doc.text('Artikeldaten', margin, y);
    y += 5;

    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    const headerData = [
      ['Bezeichnung', article?.name || '-'],
      ['Inventarnummer', article?.inventoryNumber || '-'],
      ['Hersteller', article?.manufacturer || '-'],
      ['Seriennummer', article?.serialNumber || '-'],
      ['DIN', article?.din || '-'],
      ['Geräteklasse', article?.deviceSubclass?.deviceClass?.name || '-'],
      ['Unterklasse', article?.deviceSubclass?.name || '-'],
      ['Spezifikation', article?.specification || '-'],
      ['Herstellerdatum', article?.manufacturingDate ? new Date(article.manufacturingDate).toLocaleDateString('de-DE') : '-'],
      ['Lagerort', article?.warehouse?.name || '-'],
    ];

    autoTable(doc, {
      startY: y,
      head: [],
      body: headerData,
      theme: 'plain',
      margin: { left: margin, right: margin },
      styles: { fontSize: 9, cellPadding: 1.5 },
      columnStyles: {
        0: { fontStyle: 'bold', cellWidth: 40 },
        1: { cellWidth: pageWidth - 2 * margin - 40 },
      },
    });

    y = (doc as unknown as { lastAutoTable: { finalY: number } }).lastAutoTable.finalY + 8;

    doc.setFontSize(10);
    doc.setFont('helvetica', 'bold');
    doc.text('Prüfung', margin, y);
    y += 5;

    const inspDate = new Date(inspection.inspectedAt).toLocaleDateString('de-DE');
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(9);
    doc.text('Prüfdatum: ' + inspDate, margin, y);
    doc.text('Prüfer: ' + inspection.inspectedBy, margin + 70, y);
    if (inspection.inspectionType) {
      doc.text('Prüfart: ' + inspection.inspectionType.name, margin + 140, y);
    }
    y += 7;

    // Criteria results table with drawn checkmarks and crosses
    if (inspection.criterionResults && inspection.criterionResults.length > 0) {
      const tableBody = inspection.criterionResults.map(cr => [
        cr.criterion?.name || '-',
        cr.result,
      ]);

      autoTable(doc, {
        startY: y,
        head: [['Prüfkriterium', 'Ergebnis']],
        body: tableBody,
        margin: { left: margin, right: margin },
        styles: { fontSize: 9, cellPadding: 2.5 },
        headStyles: { fillColor: [66, 66, 66], textColor: 255, fontStyle: 'bold' },
        columnStyles: {
          0: { cellWidth: pageWidth - 2 * margin - 30 },
          1: { cellWidth: 30, halign: 'center' },
        },
        didParseCell: (data) => {
          if (data.section === 'body' && data.column.index === 1) {
            data.cell.text = [''];
          }
        },
        didDrawCell: (data) => {
          if (data.section === 'body' && data.column.index === 1) {
            const raw = data.cell.raw as string;
            const cx = data.cell.x + data.cell.width / 2;
            const cy = data.cell.y + data.cell.height / 2;
            const s = 2.5;

            if (raw === 'io') {
              // Green checkmark + "OK"
              doc.setDrawColor(34, 139, 34);
              doc.setLineWidth(0.7);
              doc.line(cx - s, cy, cx - s * 0.3, cy + s * 0.7);
              doc.line(cx - s * 0.3, cy + s * 0.7, cx + s, cy - s * 0.6);
              doc.setFontSize(7);
              doc.setTextColor(34, 139, 34);
              doc.text('OK', cx + s + 1.5, cy + 1);
            } else if (raw === 'nio') {
              // Red cross (X)
              doc.setDrawColor(220, 20, 20);
              doc.setLineWidth(0.7);
              doc.line(cx - s, cy - s, cx + s, cy + s);
              doc.line(cx + s, cy - s, cx - s, cy + s);
            }
            doc.setDrawColor(0);
            doc.setTextColor(0);
          }
        },
      });

      y = (doc as unknown as { lastAutoTable: { finalY: number } }).lastAutoTable.finalY + 5;
    }

    // Overall result
    doc.setFontSize(11);
    doc.setFont('helvetica', 'bold');
    const resultText = inspection.result === 'passed' ? 'Bestanden' : 'Nicht bestanden';
    const resultColor: [number, number, number] = inspection.result === 'passed' ? [34, 139, 34] : [220, 20, 20];
    doc.text('Gesamtergebnis: ', margin, y);
    const labelWidth = doc.getTextWidth('Gesamtergebnis: ');
    doc.setTextColor(...resultColor);
    doc.text(resultText, margin + labelWidth, y);
    doc.setTextColor(0);
    y += 8;

    if (inspection.notes) {
      doc.setFontSize(9);
      doc.setFont('helvetica', 'bold');
      doc.text('Bemerkungen:', margin, y);
      y += 4;
      doc.setFont('helvetica', 'normal');
      const splitNotes = doc.splitTextToSize(inspection.notes, pageWidth - 2 * margin);
      doc.text(splitNotes, margin, y);
    }

    doc.setFontSize(7);
    doc.setFont('helvetica', 'normal');
    doc.setTextColor(150);
    doc.text(
      'Erstellt von ' + exportedBy + ' am ' + exportDate + ' | Seite ' + (index + 1) + ' von ' + inspections.length,
      margin,
      pageHeight - 10,
    );
    doc.setTextColor(0);
  });

  if (inspections.length === 0) {
    doc.setFontSize(12);
    doc.text('Keine Prüfungen im gewählten Zeitraum gefunden.', margin, 30);
    doc.setFontSize(7);
    doc.setTextColor(150);
    doc.text('Erstellt von ' + exportedBy + ' am ' + exportDate, margin, pageHeight - 10);
    doc.setTextColor(0);
  }

  doc.save('Pruefbericht_' + filterLabel.replace(/[^a-zA-Z0-9]/g, '_') + '_' + exportDate.replace(/\./g, '-') + '.pdf');
}
