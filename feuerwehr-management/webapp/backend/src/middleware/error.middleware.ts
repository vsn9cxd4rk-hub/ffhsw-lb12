import { Request, Response, NextFunction } from 'express';
import { Prisma } from '@prisma/client';
import { logger } from '../utils/logger';

export function errorHandler(
  err: Error,
  _req: Request,
  res: Response,
  _next: NextFunction
): void {
  logger.error('Unhandled error:', err);

  // Prisma errors
  if (err instanceof Prisma.PrismaClientKnownRequestError) {
    if (err.code === 'P2002') {
      res.status(409).json({ success: false, error: 'Datensatz existiert bereits (eindeutige Einschränkung verletzt)' });
      return;
    }
    if (err.code === 'P2025') {
      res.status(404).json({ success: false, error: 'Datensatz nicht gefunden' });
      return;
    }
    res.status(400).json({ success: false, error: `Datenbankfehler: ${err.code}` });
    return;
  }

  if (err instanceof Prisma.PrismaClientValidationError) {
    res.status(400).json({ success: false, error: 'Ungültige Datenbankabfrage' });
    return;
  }

  // JWT errors
  if (err.name === 'JsonWebTokenError') {
    res.status(401).json({ success: false, error: 'Ungültiger Token' });
    return;
  }
  if (err.name === 'TokenExpiredError') {
    res.status(401).json({ success: false, error: 'Token abgelaufen' });
    return;
  }

  // Generic error
  const statusCode = (err as { statusCode?: number }).statusCode || 500;
  const message = process.env.NODE_ENV === 'production' && statusCode === 500
    ? 'Interner Serverfehler'
    : err.message;

  res.status(statusCode).json({ success: false, error: message });
}
