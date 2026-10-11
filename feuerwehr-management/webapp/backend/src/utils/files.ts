import { Response } from 'express';
import fs from 'fs';
import path from 'path';
import { sendError } from './response';

function uploadRoot(): string {
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
 * Resolves a stored file path and returns it only if it lies inside UPLOAD_PATH.
 * Paths in the DB are written by multer/report generation, but a path pointing elsewhere
 * (old data, manual edits) must never be streamed or unlinked.
 */
export function resolveUploadPath(storedPath: string | null | undefined): string | null {
  if (!storedPath) return null;
  const resolved = path.resolve(storedPath);
  const rel = path.relative(uploadRoot(), resolved);
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
