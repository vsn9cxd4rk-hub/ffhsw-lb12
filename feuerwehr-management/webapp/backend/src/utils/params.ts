import { Request } from 'express';

/**
 * Route parameter as string. Express 5 types params as string | string[] because wildcard
 * segments (/*splat) yield arrays; this app defines no wildcard routes.
 */
export function param(req: Request, name: string): string {
  const value = req.params[name];
  return Array.isArray(value) ? value.join('/') : value;
}
