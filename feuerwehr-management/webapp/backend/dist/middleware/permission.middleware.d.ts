import { Request, Response, NextFunction } from 'express';
export declare function requireAdmin(req: Request, res: Response, next: NextFunction): void;
export declare function requirePermission(bit: string): (req: Request, res: Response, next: NextFunction) => void;
//# sourceMappingURL=permission.middleware.d.ts.map