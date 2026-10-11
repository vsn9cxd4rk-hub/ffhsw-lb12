import { Request, Response, NextFunction, RequestHandler } from 'express';
import crypto from 'crypto';
import fs from 'fs';
import multer from 'multer';

type FileKind = 'pdf' | 'doc' | 'docx' | 'xls' | 'xlsx' | 'xlsm' | 'jpeg' | 'png';

interface KindSpec {
  mimeType: string;
  ext: string;
  magic: Buffer;
}

const OLE = Buffer.from([0xd0, 0xcf, 0x11, 0xe0, 0xa1, 0xb1, 0x1a, 0xe1]); // .doc/.xls
const ZIP = Buffer.from([0x50, 0x4b, 0x03, 0x04]); // .docx/.xlsx/.xlsm

const KINDS: Record<FileKind, KindSpec> = {
  pdf: { mimeType: 'application/pdf', ext: '.pdf', magic: Buffer.from('%PDF-') },
  doc: { mimeType: 'application/msword', ext: '.doc', magic: OLE },
  docx: { mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document', ext: '.docx', magic: ZIP },
  xls: { mimeType: 'application/vnd.ms-excel', ext: '.xls', magic: OLE },
  xlsx: { mimeType: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', ext: '.xlsx', magic: ZIP },
  xlsm: { mimeType: 'application/vnd.ms-excel.sheet.macroEnabled.12', ext: '.xlsm', magic: ZIP },
  jpeg: { mimeType: 'image/jpeg', ext: '.jpg', magic: Buffer.from([0xff, 0xd8, 0xff]) },
  png: { mimeType: 'image/png', ext: '.png', magic: Buffer.from([0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a]) },
};

function kindForMime(kinds: FileKind[], mimeType: string): KindSpec | undefined {
  return kinds.map((k) => KINDS[k]).find((spec) => spec.mimeType === mimeType);
}

function httpError(message: string, statusCode: number): Error {
  return Object.assign(new Error(message), { statusCode });
}

/**
 * Busboy decodes filename parameters as latin1, browsers send UTF-8 bytes, so umlauts
 * arrive as "PrÃ¼fung". Re-decode only if every character is a single byte (code <= 255),
 * at least one is non-ASCII and the bytes form valid UTF-8 – a correctly decoded name
 * (e.g. sent with filename*=UTF-8'') is left alone.
 */
export function decodeOriginalName(name: string): string {
  let hasHighByte = false;
  for (let i = 0; i < name.length; i++) {
    const code = name.charCodeAt(i);
    if (code > 0xff) return name;
    if (code >= 0x80) hasHighByte = true;
  }
  if (!hasHighByte) return name;
  const bytes = Buffer.from(name, 'latin1');
  const decoded = bytes.toString('utf8');
  // invalid sequences turn into U+FFFD and do not round-trip
  return Buffer.from(decoded, 'utf8').equals(bytes) ? decoded : name;
}

function startsWith(file: string, magic: Buffer): boolean {
  const fd = fs.openSync(file, 'r');
  try {
    const head = Buffer.alloc(magic.length);
    const read = fs.readSync(fd, head, 0, magic.length, 0);
    return read === magic.length && head.equals(magic);
  } finally {
    fs.closeSync(fd);
  }
}

/** Removes a file multer has just written (e.g. when the target record turns out not to exist). */
export function discardUpload(file: Express.Multer.File | undefined): void {
  if (file?.path && fs.existsSync(file.path)) fs.unlinkSync(file.path);
}

interface UploadOptions {
  /** Upload directory; may throw (e.g. invalid record id) – the error is passed on. */
  destination: (req: Request) => string;
  /** Accepted file kinds; the declared MIME type must match one and the content its signature. */
  kinds: FileKind[];
  field?: string;
  maxBytes?: number;
  /** Error text for a rejected file type, e.g. "Nur PDF-Dateien sind erlaubt". */
  rejectMessage: string;
}

/**
 * Single-file upload middleware. Stored files get a random name with a fixed extension
 * derived from the (whitelisted) MIME type – nothing from the client-supplied name ends up
 * on disk. After writing, the file signature is checked so that e.g. an HTML file declared
 * as application/pdf is rejected. `req.file.originalname` is UTF-8 decoded for display.
 */
export function createUpload(options: UploadOptions): RequestHandler {
  const { destination, kinds, field = 'file', maxBytes = 20 * 1024 * 1024, rejectMessage } = options;

  const storage = multer.diskStorage({
    destination: (req, _file, cb) => {
      try {
        const dir = destination(req);
        fs.mkdirSync(dir, { recursive: true });
        cb(null, dir);
      } catch (err) {
        cb(err as Error, '');
      }
    },
    filename: (_req, file, cb) => {
      const spec = kindForMime(kinds, file.mimetype);
      cb(null, `${crypto.randomUUID()}${spec?.ext ?? ''}`);
    },
  });

  const handler = multer({
    storage,
    limits: { fileSize: maxBytes, files: 1 },
    fileFilter: (_req, file, cb) => {
      if (kindForMime(kinds, file.mimetype)) cb(null, true);
      else cb(httpError(rejectMessage, 400));
    },
  }).single(field);

  return (req: Request, res: Response, next: NextFunction) => {
    handler(req, res, (err?: unknown) => {
      if (err) {
        discardUpload(req.file);
        if (err instanceof multer.MulterError) {
          const tooLarge = err.code === 'LIMIT_FILE_SIZE';
          const message = tooLarge
            ? `Datei zu groß (max. ${Math.round(maxBytes / 1024 / 1024)} MB)`
            : 'Ungültiger Datei-Upload';
          next(httpError(message, tooLarge ? 413 : 400));
          return;
        }
        next(err);
        return;
      }
      if (!req.file) { next(); return; }

      const spec = kindForMime(kinds, req.file.mimetype);
      try {
        if (!spec || !startsWith(req.file.path, spec.magic)) {
          discardUpload(req.file);
          next(httpError(`${rejectMessage} (Dateiinhalt passt nicht zum Dateityp)`, 400));
          return;
        }
      } catch (readErr) {
        discardUpload(req.file);
        next(readErr);
        return;
      }
      req.file.originalname = decodeOriginalName(req.file.originalname);
      next();
    });
  };
}
