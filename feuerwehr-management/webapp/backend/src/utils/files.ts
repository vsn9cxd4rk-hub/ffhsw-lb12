import { Response } from 'express';
import fs from 'fs';
import path from 'path';
import { sendError } from './response';

export function uploadRoot(): string {
  return path.resolve(process.env.UPLOAD_PATH || './uploads');
}

/** Upload directory for a category, e.g. uploads/templates. */
export function categoryUploadDir(kind: string): string {
  return path.join(uploadRoot(), kind);
}

/**
 * Upload directory for a record, e.g. uploads/events/12. The id comes from the URL and
 * Express decodes %2F, so it must be checked before it becomes part of a path.
 */
export function recordUploadDir(kind: string, id: string): string {
  if (!/^\d+$/.test(id)) throw Object.assign(new Error('Ungültige ID'), { statusCode: 400 });
  return path.join(uploadRoot(), kind, id);
}

/**
 * Layout below UPLOAD_PATH: number of path segments that follow the category directory
 * (record directories have the record id in between).
 */
const UPLOAD_LAYOUT: Record<string, number> = {
  templates: 1,
  certificates: 1,
  'article-documents': 1,
  'inspection-documents': 1,
  operations: 2,
  events: 2,
};

/**
 * Turns a file below UPLOAD_PATH into the form stored in the DB: relative to UPLOAD_PATH,
 * with forward slashes. This keeps DB dumps usable on another machine/OS with a different
 * UPLOAD_PATH (backup server, Docker, local development).
 */
export function toStoredPath(absolutePath: string): string {
  return path.relative(uploadRoot(), path.resolve(absolutePath)).split(path.sep).join('/');
}

/**
 * Up to 1.4.0 absolute paths were stored, e.g. /var/www/feuerwehrmanagement/uploads/templates/x.
 * They are mapped onto the current UPLOAD_PATH via the known directory layout, so a dump of
 * the production system works wherever its uploads folder is copied to.
 */
export function legacyRelativePath(storedPath: string): string | null {
  const segments = storedPath.split(/[\\/]+/).filter(Boolean);
  for (let i = segments.length - 2; i >= 0; i--) {
    const depth = UPLOAD_LAYOUT[segments[i]];
    if (depth !== undefined && segments.length - 1 - i === depth) {
      return segments.slice(i).join('/');
    }
  }
  return null;
}

/** Absolute on Linux or Windows – DB dumps travel between both. */
export function isAbsoluteAnyOs(p: string): boolean {
  return path.posix.isAbsolute(p) || path.win32.isAbsolute(p);
}

/**
 * Resolves a stored file path and returns it only if it lies inside UPLOAD_PATH.
 * Paths in the DB are written by multer/report generation, but a path pointing elsewhere
 * (old data, manual edits) must never be streamed or unlinked.
 */
export function resolveUploadPath(storedPath: string | null | undefined): string | null {
  if (!storedPath) return null;
  const root = uploadRoot();
  let resolved: string;
  if (!isAbsoluteAnyOs(storedPath)) {
    resolved = path.resolve(root, storedPath);
  } else {
    // Absolute legacy path: use it as is if it is below the current UPLOAD_PATH, otherwise
    // map it via the directory layout (it then necessarily lies below UPLOAD_PATH).
    const direct = path.resolve(storedPath);
    const directRel = path.relative(root, direct);
    if (directRel && !directRel.startsWith('..') && !path.isAbsolute(directRel)) {
      resolved = direct;
    } else {
      const legacy = legacyRelativePath(storedPath);
      if (!legacy) return null;
      resolved = path.resolve(root, legacy);
    }
  }
  const rel = path.relative(root, resolved);
  if (!rel || rel.startsWith('..') || path.isAbsolute(rel)) return null;
  return resolved;
}

/** Deletes a stored upload if it exists inside UPLOAD_PATH; anything else is left alone. */
export function removeUpload(storedPath: string | null | undefined): void {
  const file = resolveUploadPath(storedPath);
  if (file && fs.existsSync(file)) fs.unlinkSync(file);
}

/** Streams a stored upload as attachment; answers 404 if missing or outside UPLOAD_PATH. */
export function sendUpload(res: Response, storedPath: string | null | undefined, fileName: string, mimeType?: string): void {
  const file = resolveUploadPath(storedPath);
  if (!file || !fs.existsSync(file) || !fs.statSync(file).isFile()) {
    sendError(res, 'Datei nicht gefunden', 404);
    return;
  }
  res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(fileName)}"`);
  if (mimeType) res.setHeader('Content-Type', mimeType);
  const stream = fs.createReadStream(file);
  stream.on('error', () => {
    if (!res.headersSent) sendError(res, 'Datei konnte nicht gelesen werden', 500);
    else res.destroy();
  });
  stream.pipe(res);
}
