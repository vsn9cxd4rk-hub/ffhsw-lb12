import { Request } from 'express';

export interface PaginationOptions {
  skip: number;
  take: number;
  page: number;
  limit: number;
}

export function getPagination(req: Request): PaginationOptions {
  const page = Math.max(1, parseInt(req.query.page as string) || 1);
  const limit = Math.min(100, Math.max(1, parseInt(req.query.limit as string) || 20));
  const skip = (page - 1) * limit;
  return { skip, take: limit, page, limit };
}
