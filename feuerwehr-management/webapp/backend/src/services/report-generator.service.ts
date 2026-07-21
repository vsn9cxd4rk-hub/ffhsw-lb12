import path from 'path';
import fs from 'fs';
import { execSync } from 'child_process';
import PizZip from 'pizzip';
import ExcelJS from 'exceljs';
import { prisma } from '../config/database';
import { logger } from '../utils/logger';

const uploadDir = process.env.UPLOAD_PATH || './uploads';
const tempDir = path.join(uploadDir, 'temp');

function ensureTempDir() {
  if (!fs.existsSync(tempDir)) {
    fs.mkdirSync(tempDir, { recursive: true });
  }
}

function findLibreOffice(): string | null {
  const candidates = [
    'libreoffice',
    'soffice',
    '/usr/bin/libreoffice',
    '/usr/bin/soffice',
    'C:\\Program Files\\LibreOffice\\program\\soffice.exe',
    'C:\\Program Files (x86)\\LibreOffice\\program\\soffice.exe',
  ];

  for (const cmd of candidates) {
    try {
      execSync(`"${cmd}" --version`, { stdio: 'pipe' });
      return cmd;
    } catch {
      // not found, try next
    }
  }
  return null;
}

async function convertToPdf(inputPath: string): Promise<string> {
  const libre = findLibreOffice();
  if (!libre) {
    throw new Error('LibreOffice nicht gefunden. Bitte LibreOffice installieren für PDF-Konvertierung.');
  }

  const outDir = path.dirname(inputPath);
  const cmd = `"${libre}" --headless --convert-to pdf --outdir "${outDir}" "${inputPath}"`;

  try {
    execSync(cmd, { stdio: 'pipe', timeout: 60000 });
  } catch (err) {
    throw new Error(`PDF-Konvertierung fehlgeschlagen: ${(err as Error).message}`);
  }

  const pdfPath = inputPath.replace(/\.[^.]+$/, '.pdf');
  if (!fs.existsSync(pdfPath)) {
    throw new Error('PDF wurde nicht erzeugt');
  }
  return pdfPath;
}

async function getTemplateByName(name: string): Promise<string> {
  const template = await prisma.template.findFirst({
    where: { name: { contains: name } },
  });
  if (!template) {
    throw new Error(`Template "${name}" nicht gefunden. Bitte in Einstellungen → Templates hochladen.`);
  }
  if (!fs.existsSync(template.filePath)) {
    throw new Error(`Template-Datei nicht gefunden: ${template.filePath}`);
  }
  return template.filePath;
}

function escapeXml(str: string): string {
  return str
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

/**
 * Fills Word Content Controls by finding the SDT with matching w:tag,
 * then replacing all <w:t> text within its <w:sdtContent> with the new value.
 * Preserves the XML structure (tables, cells, formatting) completely.
 */
function fillContentControls(xml: string, data: Record<string, string>, _placeholders: Record<string, string>): string {
  // Step 1: Remove all showingPlcHdr flags so Word shows actual content
  xml = xml.replace(/<w:showingPlcHdr\s*\/>/g, '');

  // Step 2: Remove all red/pink placeholder shadings (FF9x, FFAx tones)
  xml = xml.replace(/<w:shd[^>]*w:fill="FF[9A-Fa-f][0-9A-Fa-f][9A-Fa-f][0-9A-Fa-f]"[^>]*\/>/g, '');

  // Step 3: Remove PlaceholderText character style
  xml = xml.replace(/<w:rStyle w:val="PlaceholderText"\/>/g, '');

  // Step 2: For each tag, find its SDT block and replace all <w:t> text in sdtContent
  for (const [tag, value] of Object.entries(data)) {
    const escaped = escapeXml(value);
    // Find the SDT that contains this specific tag, then replace text in its sdtContent
    const pattern = new RegExp(
      `(<w:tag\\s+w:val="${escapeForRegex(tag)}"[^/]*/>.*?<w:sdtContent>)(.*?)(</w:sdtContent>)`,
      's'
    );
    const match = xml.match(pattern);
    if (!match) continue;

    const sdtContentOrig = match[2];
    // Replace all <w:t>...</w:t> text content with empty, then put value in the first one
    let firstReplaced = false;
    const sdtContentNew = sdtContentOrig.replace(/<w:t(?:\s[^>]*)?>([^<]*)<\/w:t>/g, () => {
      if (!firstReplaced) {
        firstReplaced = true;
        return `<w:t>${escaped}</w:t>`;
      }
      return '<w:t></w:t>';
    });

    xml = xml.replace(match[0], match[1] + sdtContentNew + match[3]);
  }

  return xml;
}

function escapeForRegex(str: string): string {
  return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

interface TimeEntry {
  vehicleName: string;
  departureTime: string | null;
  arrivalTime: string | null;
  returnTime: string | null;
}

function calcDurationMinutes(start: string | null, end: string | null): number {
  if (!start || !end) return 0;
  const [sh, sm] = start.split(':').map(Number);
  const [eh, em] = end.split(':').map(Number);
  return (eh * 60 + em) - (sh * 60 + sm);
}

function formatDuration(minutes: number): string {
  if (minutes <= 0) return '00:00';
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  return `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`;
}

function replaceOleWithVehicleTable(xml: string, times: TimeEntry[], leaderCount: number, memberCount: number): string {
  // Find and remove the paragraph containing the OLE object using indexOf (regex-free)
  const objStart = xml.indexOf('<w:object');
  if (objStart === -1) return xml;

  const pStart = xml.lastIndexOf('<w:p ', objStart);
  const objEnd = xml.indexOf('</w:object>', objStart);
  const pEnd = xml.indexOf('</w:p>', objEnd) + 6;

  // Build a Word table with vehicle times
  const headerRow = `<w:tr>
    <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/><w:tcBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr><w:t>Ausgerückt</w:t></w:r></w:p></w:tc>
    <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/><w:tcBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr><w:t>Eingetroffen</w:t></w:r></w:p></w:tc>
    <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/><w:tcBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr><w:t>Eingerückt</w:t></w:r></w:p></w:tc>
    <w:tc><w:tcPr><w:tcW w:w="2000" w:type="dxa"/><w:tcBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr><w:t>Fahrzeug</w:t></w:r></w:p></w:tc>
    <w:tc><w:tcPr><w:tcW w:w="1000" w:type="dxa"/><w:tcBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr><w:t>Stärke</w:t></w:r></w:p></w:tc>
    <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/><w:tcBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr><w:t>Einsatzzeit</w:t></w:r></w:p></w:tc>
    <w:tc><w:tcPr><w:tcW w:w="1400" w:type="dxa"/><w:tcBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:b/><w:sz w:val="16"/></w:rPr><w:t>Gesamtzeit</w:t></w:r></w:p></w:tc>
  </w:tr>`;

  const makeCell = (text: string) =>
    `<w:tc><w:tcPr><w:tcBorders><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tcBorders></w:tcPr><w:p><w:pPr><w:jc w:val="center"/><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:sz w:val="16"/></w:rPr></w:pPr><w:r><w:rPr><w:rFonts w:ascii="Arial" w:hAnsi="Arial"/><w:sz w:val="16"/></w:rPr><w:t>${escapeXml(text)}</w:t></w:r></w:p></w:tc>`;

  const gesamt = leaderCount + memberCount;
  const staerkeStr = `${leaderCount}/${memberCount}/${gesamt}`;

  let dataRows = '';
  for (const t of times) {
    const einsatzMinutes = calcDurationMinutes(t.departureTime, t.returnTime);
    const einsatzzeit = formatDuration(einsatzMinutes);
    const gesamtMinutes = einsatzMinutes * gesamt;
    const gesamtzeit = formatDuration(gesamtMinutes);
    dataRows += `<w:tr>${makeCell(t.departureTime || '')}${makeCell(t.arrivalTime || '')}${makeCell(t.returnTime || '')}${makeCell(t.vehicleName)}${makeCell(staerkeStr)}${makeCell(einsatzzeit)}${makeCell(gesamtzeit)}</w:tr>`;
  }

  const table = `<w:tbl><w:tblPr><w:tblpPr w:leftFromText="142" w:rightFromText="142" w:vertAnchor="text" w:tblpY="1"/><w:tblOverlap w:val="never"/><w:tblW w:w="10000" w:type="dxa"/><w:tblBorders><w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:insideH w:val="single" w:sz="4" w:space="0" w:color="000000"/><w:insideV w:val="single" w:sz="4" w:space="0" w:color="000000"/></w:tblBorders></w:tblPr><w:tblGrid><w:gridCol w:w="1400"/><w:gridCol w:w="1400"/><w:gridCol w:w="1400"/><w:gridCol w:w="2000"/><w:gridCol w:w="1000"/><w:gridCol w:w="1400"/><w:gridCol w:w="1400"/></w:tblGrid>${headerRow}${dataRows}</w:tbl>`;

  // Replace OLE paragraph with our table, with empty paragraphs before/after for spacing.
  // Two leading paragraphs: the floating table's vertAnchor="text" consumes/collapses the
  // paragraph immediately preceding it as its anchor line, so a single blank paragraph there
  // renders with zero height; the extra one keeps a visible gap.
  xml = xml.substring(0, pStart) + '<w:p/><w:p/>' + table + '<w:p/>' + xml.substring(pEnd);

  return xml;
}

// Mapping of tag names to their placeholder texts as they appear in the template
const EINSATZBERICHT_PLACEHOLDERS: Record<string, string> = {
  'Art des Berichtes': 'Wählen Sie aus: Einsatzbericht oder Tätigkeitsbericht',
  'Einsatzleiter': 'Hier Name des Einsatzleiters eingeben.',
  'Einsatzstichwort': 'Wählen Sie ein Einsatzstichwort aus.',
  'Auftragsnummer': 'Auftragsnummer der ILS eingeben.',
  'Einsatzadresse': 'Hier Adresse des Einsatzortes eingeben.',
  'Einsatzdatum': 'Hier Einsatzdatum eingeben.',
  'Alarmzeit': 'Alarm-Uhrzeit',
  'Meldender': 'Hier Name und Erreichbarkeiten des Meldenden/Notrufenden eingeben.',
  'Einsatzangaben': 'Hier sonstige Einsatzangaben (Patientennamen, etc.) eingeben.',
  'Polizei': 'Hier Polizeiinspektion und Namen eintragen.',
  'Lage': 'Hier die Lage bei Eintreffen beschreiben. Insbesondere Menschenlage und Gefahrenlage.',
  'Maßnahmen': 'Hier die durchgeführten Maßnahmen beschreiben. Im Falle eines Einsatzberichtes insbesondere Befehle an untergeordnete Führer. Maßnahmen zur Menschenrettung und deren Erfolgszeit sind möglichst genau zu dokumentieren.',
  'Einsatzmittel': 'Hier die verbrauchten Einsatzmittel angeben welche von der Gemeinde Heusweiler ersatzbeschafft werden müssen.',
  'Ersteller': 'Hier Name des Erstellers eingeben',
  'Menschen gerettet': 'Zahl',
  'FW verletzt': 'Zahl',
  'Menschen tot': 'Zahl',
  'FW tot': 'Zahl',
};

export async function generateOperationReport(operationId: number): Promise<{ filePath: string; fileName: string }> {
  ensureTempDir();

  const op = await prisma.operation.findUnique({
    where: { id: operationId },
    include: { times: true },
  });
  if (!op) throw new Error('Einsatz nicht gefunden');

  let commanderName = '';
  if (op.commanderId) {
    const commander = await prisma.member.findUnique({ where: { id: op.commanderId } });
    if (commander) commanderName = `${commander.firstName} ${commander.lastName}`;
  }

  const templatePath = await getTemplateByName('Einsatzbericht');
  const content = fs.readFileSync(templatePath);
  const zip = new PizZip(content);

  const docXml = zip.file('word/document.xml');
  if (!docXml) throw new Error('document.xml nicht im Template gefunden');

  let xmlContent = docXml.asText();

  const tagData: Record<string, string> = {
    'Art des Berichtes': op.reportType || 'Einsatzbericht',
    'Einsatzleiter': commanderName || '',
    'Einsatzstichwort': op.keyword || '',
    'Auftragsnummer': op.ilsOrderNumber || '',
    'Einsatzadresse': op.location,
    'Einsatzdatum': new Date(op.date).toLocaleDateString('de-DE', { weekday: 'long', day: '2-digit', month: '2-digit', year: 'numeric' }),
    'Alarmzeit': op.alarmTime || '',
    'Meldender': op.callerInfo || '',
    'Einsatzangaben': op.description || '',
    'Polizei': op.policeInfo || '',
    'Lage': op.situationOnArrival || '',
    'Maßnahmen': op.actionsTaken || '',
    'Einsatzmittel': op.resourcesUsed || '',
    'Ersteller': op.createdByName || '',
    'Menschen gerettet': String(op.rescuedPersons || 0),
    'FW verletzt': String(op.injuredFirefighters || 0),
    'Menschen tot': String(op.deceasedPersons || 0),
    'FW tot': String(op.deceasedFirefighters || 0),
  };

  xmlContent = fillContentControls(xmlContent, tagData, EINSATZBERICHT_PLACEHOLDERS);

  // Check operation type checkboxes (14 checkboxes in order)
  const CHECKBOX_IDS = [
    '688269389', '-2102946991', '-286048790', '1035474428',
    '2013181942', '568161255', '-310714979', '905566519',
    '407583285', '-1214344209', '-364292920', '528689115',
    '1839644798', '1499931159',
  ];
  const CHECKBOX_TYPES = [
    'Kleinbrand a', 'Kleinbrand b', 'Mittelbrand', 'Grossbrand',
    'Katastr.-Einsatz', 'Techn. Hilfeleist.', 'Tiere/Insekten',
    'NAW Einsatz', 'RTW Einsatz', 'Kranken-Transport',
    'Sonstiger Einsatz', 'Blinder Alarm', 'Boeswilliger Alarm', 'Brandmelde Anlage',
  ];
  const selectedTypes = (op.operationType || '').split(',').map(t => t.trim());
  for (let i = 0; i < CHECKBOX_IDS.length; i++) {
    if (selectedTypes.includes(CHECKBOX_TYPES[i])) {
      // Set checked state: change w14:val="0" to w14:val="1" for this checkbox
      const cbPattern = new RegExp(
        `(<w:id\\s+w:val="${escapeForRegex(CHECKBOX_IDS[i])}"\\/>\\s*<w14:checkbox>\\s*<w14:checked\\s+w14:val=")0(")`
      );
      xmlContent = xmlContent.replace(cbPattern, '$11$2');
      // Also change the displayed character from ☐ (2610) to ☒ (2612)
      const charPattern = new RegExp(
        `(<w:id\\s+w:val="${escapeForRegex(CHECKBOX_IDS[i])}"[\\s\\S]*?<w:sdtContent>[\\s\\S]*?<w:t>)\\u2610(<\\/w:t>)`,
        's'
      );
      xmlContent = xmlContent.replace(charPattern, '$1☒$2');
    }
  }

  // Fill the "Ersteller-Rolle" dropdown (ID 1404645901)
  // For dropdowns we replace the entire sdtContent with a simple run
  const authorRole = op.authorRole || 'Einsatzleiter';
  const rolePattern = /(<w:id\s+w:val="1404645901"[\s\S]*?<w:sdtContent>)([\s\S]*?)(<\/w:sdtContent>)/s;
  xmlContent = xmlContent.replace(rolePattern, `$1<w:r><w:t>${escapeXml(authorRole)}</w:t></w:r>$3`);

  // Add Dienstgrad (rank) to the Ersteller name field
  let creatorWithRank = op.createdByName || '';
  if (op.createdByName) {
    // Try to find the member's rank
    const creatorMember = await prisma.member.findFirst({
      where: {
        OR: [
          { lastName: { contains: op.createdByName.split(' ').pop() || '' } },
          { firstName: { contains: op.createdByName.split(' ')[0] || '' } },
        ],
      },
      select: { rank: true, firstName: true, lastName: true },
    });
    if (creatorMember?.rank) {
      creatorWithRank = `${creatorMember.rank} ${op.createdByName}`;
    }
  }
  // Re-fill the Ersteller tag with rank included
  const erstellerPattern = new RegExp(
    `(<w:tag\\s+w:val="Ersteller"[^/]*/>.*?<w:sdtContent>)(.*?)(</w:sdtContent>)`, 's'
  );
  const erstellerMatch = xmlContent.match(erstellerPattern);
  if (erstellerMatch) {
    let firstDone = false;
    const newContent = erstellerMatch[2].replace(/<w:t(?:\s[^>]*)?>([^<]*)<\/w:t>/g, () => {
      if (!firstDone) { firstDone = true; return `<w:t>${escapeXml(creatorWithRank)}</w:t>`; }
      return '<w:t></w:t>';
    });
    xmlContent = xmlContent.replace(erstellerMatch[0], erstellerMatch[1] + newContent + erstellerMatch[3]);
  }

  // Replace the embedded OLE Excel object with a proper Word table containing vehicle times
  xmlContent = replaceOleWithVehicleTable(xmlContent, op.times, op.leaderCount, op.memberCount);

  // Remove the WMF image paragraph (the "Bericht als PDF abspeichern" macro button)
  const wmfPicStart = xmlContent.indexOf('<w:pict ');
  if (wmfPicStart !== -1) {
    const pStart = xmlContent.lastIndexOf('<w:p ', wmfPicStart);
    const picEnd = xmlContent.indexOf('</w:pict>', wmfPicStart);
    const pEnd = xmlContent.indexOf('</w:p>', picEnd) + 6;
    if (pStart !== -1 && pEnd > pStart) {
      xmlContent = xmlContent.substring(0, pStart) + xmlContent.substring(pEnd);
    }
  }

  // Write back the modified document.xml (after ALL modifications)
  zip.file('word/document.xml', xmlContent);

  // Remove orphaned OLE-related files and the macro button image
  zip.remove('word/embeddings/Microsoft_Excel_Macro-Enabled_Worksheet.xlsm');
  zip.remove('word/media/image3.emf');
  zip.remove('word/media/image2.wmf');

  // Clean up relationships: remove rIds that reference deleted files
  const relsFile = zip.file('word/_rels/document.xml.rels');
  if (relsFile) {
    let relsXml = relsFile.asText();
    relsXml = relsXml.replace(/<Relationship[^>]*Target="media\/image3\.emf"[^>]*\/>/g, '');
    relsXml = relsXml.replace(/<Relationship[^>]*Target="media\/image2\.wmf"[^>]*\/>/g, '');
    relsXml = relsXml.replace(/<Relationship[^>]*Target="embeddings\/Microsoft_Excel_Macro-Enabled_Worksheet\.xlsm"[^>]*\/>/g, '');
    zip.file('word/_rels/document.xml.rels', relsXml);
  }

  // Clean up [Content_Types].xml: remove Default entries for file types we deleted
  const ctFile = zip.file('[Content_Types].xml');
  if (ctFile) {
    let ctXml = ctFile.asText();
    ctXml = ctXml.replace(/<Default Extension="emf"[^>]*\/>/g, '');
    ctXml = ctXml.replace(/<Default Extension="wmf"[^>]*\/>/g, '');
    ctXml = ctXml.replace(/<Default Extension="xlsm"[^>]*\/>/g, '');
    zip.file('[Content_Types].xml', ctXml);
  }

  // Generate with DEFLATE compression
  const buf = zip.generate({ type: 'nodebuffer', compression: 'DEFLATE' });
  const timestamp = Date.now();
  const docxPath = path.join(tempDir, `einsatzbericht_${operationId}_${timestamp}.docx`);
  fs.writeFileSync(docxPath, buf);

  let finalPath: string;
  let fileName: string;

  try {
    finalPath = await convertToPdf(docxPath);
    fileName = `Einsatzbericht_${op.operationNumber || operationId}.pdf`;
  } catch (err) {
    logger.warn(`PDF-Konvertierung fehlgeschlagen, liefere docx: ${(err as Error).message}`);
    finalPath = docxPath;
    fileName = `Einsatzbericht_${op.operationNumber || operationId}.docx`;
  }

  const destDir = path.join(uploadDir, 'operations', String(operationId));
  fs.mkdirSync(destDir, { recursive: true });
  const destPath = path.join(destDir, `${timestamp}-${fileName}`);
  fs.renameSync(finalPath, destPath);

  if (finalPath !== docxPath && fs.existsSync(docxPath)) {
    fs.unlinkSync(docxPath);
  }

  const stat = fs.statSync(destPath);
  await prisma.operationDocument.create({
    data: {
      operationId,
      fileName,
      filePath: destPath,
      fileSize: stat.size,
      mimeType: fileName.endsWith('.pdf') ? 'application/pdf' : 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      uploadedBy: 'System (Generiert)',
    },
  });

  return { filePath: destPath, fileName };
}

export async function generatePersonnelSheet(operationId: number, vehicleFilter?: string): Promise<{ filePath: string; fileName: string }> {
  ensureTempDir();

  const op = await prisma.operation.findUnique({
    where: { id: operationId },
    include: {
      personnel: {
        include: {
          member: {
            select: {
              id: true, firstName: true, lastName: true,
              qualAGT: true, qualGruppenfuehrer: true, qualZugfuehrer: true,
              qualLicenseC: true, qualLicenseB: true,
            },
          },
        },
      },
    },
  });
  if (!op) throw new Error('Einsatz nicht gefunden');

  const templatePath = await getTemplateByName('Kräftenachweis');
  const workbook = new ExcelJS.Workbook();
  await workbook.xlsx.readFile(templatePath);

  const sheet = workbook.getWorksheet(1);
  if (!sheet) throw new Error('Arbeitsblatt nicht gefunden');

  sheet.getCell('B5').value = vehicleFilter || op.vehicles || '';
  sheet.getCell('B7').value = `${op.keyword || ''} ${op.location}`.trim();
  sheet.getCell('B8').value = `${new Date(op.date).toLocaleDateString('de-DE')} ${op.alarmTime || ''}`.trim();

  let personnel = op.personnel;
  if (vehicleFilter) {
    personnel = personnel.filter(p => p.vehicleName === vehicleFilter);
  }

  const deployed = personnel.filter(p => p.section === 'deployed');
  const reinforcement = personnel.filter(p => p.section === 'reinforcement');

  for (let i = 0; i < Math.min(deployed.length, 9); i++) {
    const row = 12 + i;
    const p = deployed[i];
    sheet.getCell(`A${row}`).value = p.function;
    sheet.getCell(`B${row}`).value = `${p.member.lastName}, ${p.member.firstName}`;
    if (p.member.qualAGT) sheet.getCell(`D${row}`).value = 'X';
    if (p.member.qualGruppenfuehrer) sheet.getCell(`E${row}`).value = 'X';
    if (p.member.qualZugfuehrer) sheet.getCell(`F${row}`).value = 'X';
    if (p.member.qualLicenseC) sheet.getCell(`G${row}`).value = 'X';
    if (p.member.qualLicenseB) sheet.getCell(`H${row}`).value = 'X';
  }

  for (let i = 0; i < Math.min(reinforcement.length, 9); i++) {
    const row = 24 + i;
    const p = reinforcement[i];
    sheet.getCell(`A${row}`).value = p.function;
    sheet.getCell(`B${row}`).value = `${p.member.lastName}, ${p.member.firstName}`;
    if (p.member.qualAGT) sheet.getCell(`D${row}`).value = 'X';
    if (p.member.qualGruppenfuehrer) sheet.getCell(`E${row}`).value = 'X';
    if (p.member.qualZugfuehrer) sheet.getCell(`F${row}`).value = 'X';
    if (p.member.qualLicenseC) sheet.getCell(`G${row}`).value = 'X';
    if (p.member.qualLicenseB) sheet.getCell(`H${row}`).value = 'X';
  }

  const timestamp = Date.now();
  const xlsxPath = path.join(tempDir, `kraeftenachweis_${operationId}_${timestamp}.xlsx`);
  await workbook.xlsx.writeFile(xlsxPath);

  let finalPath: string;
  let fileName: string;

  try {
    finalPath = await convertToPdf(xlsxPath);
    fileName = `Kraeftenachweis_${op.operationNumber || operationId}.pdf`;
  } catch (err) {
    logger.warn(`PDF-Konvertierung fehlgeschlagen, liefere xlsx: ${(err as Error).message}`);
    finalPath = xlsxPath;
    fileName = `Kraeftenachweis_${op.operationNumber || operationId}.xlsx`;
  }

  const destDir = path.join(uploadDir, 'operations', String(operationId));
  fs.mkdirSync(destDir, { recursive: true });
  const destPath = path.join(destDir, `${timestamp}-${fileName}`);
  fs.renameSync(finalPath, destPath);

  if (finalPath !== xlsxPath && fs.existsSync(xlsxPath)) {
    fs.unlinkSync(xlsxPath);
  }

  const stat = fs.statSync(destPath);
  await prisma.operationDocument.create({
    data: {
      operationId,
      fileName,
      filePath: destPath,
      fileSize: stat.size,
      mimeType: fileName.endsWith('.pdf') ? 'application/pdf' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      uploadedBy: 'System (Generiert)',
    },
  });

  return { filePath: destPath, fileName };
}
