import { describe, it, expect } from 'vitest';
import path from 'path';
import { legacyRelativePath, resolveUploadPath, toStoredPath, uploadRoot } from '../src/utils/files';

// Stored paths must work on every installation, whatever its UPLOAD_PATH or OS.

describe('Upload-Pfade (umgebungsunabhängig)', () => {
  const root = uploadRoot();

  it('neue Uploads werden relativ zu UPLOAD_PATH gespeichert', () => {
    expect(toStoredPath(path.join(root, 'events', '12', 'a.pdf'))).toBe('events/12/a.pdf');
    expect(toStoredPath(path.join(root, 'templates', 'b.docx'))).toBe('templates/b.docx');
  });

  it('relative Pfade werden unter dem lokalen UPLOAD_PATH aufgelöst', () => {
    expect(resolveUploadPath('templates/b.docx')).toBe(path.join(root, 'templates', 'b.docx'));
  });

  it('absolute Altpfade anderer Systeme werden auf UPLOAD_PATH abgebildet', () => {
    expect(resolveUploadPath('/var/www/feuerwehrmanagement/uploads/templates/1-Vorlage.docx'))
      .toBe(path.join(root, 'templates', '1-Vorlage.docx'));
    expect(resolveUploadPath('c:\\project\\data\\uploads\\operations\\2\\1-Bericht.pdf'))
      .toBe(path.join(root, 'operations', '2', '1-Bericht.pdf'));
    expect(resolveUploadPath('/Users/x/uploads/inspection-documents/1-Prüfung.pdf'))
      .toBe(path.join(root, 'inspection-documents', '1-Prüfung.pdf'));
  });

  it('Altpfade unter dem eigenen UPLOAD_PATH bleiben unverändert', () => {
    const own = path.join(root, 'certificates', 'c.pdf');
    expect(resolveUploadPath(own)).toBe(own);
  });

  it('Pfade außerhalb der Upload-Struktur werden nie aufgelöst', () => {
    expect(resolveUploadPath('/etc/passwd')).toBeNull();
    expect(resolveUploadPath(`${process.cwd()}/package.json`)).toBeNull();
    expect(resolveUploadPath('../../.env')).toBeNull();
    expect(resolveUploadPath('templates/../../.env')).toBeNull();
    // record directories need the record id
    expect(legacyRelativePath('/var/www/x/uploads/operations/bericht.pdf')).toBeNull();
    expect(resolveUploadPath('')).toBeNull();
  });
});
