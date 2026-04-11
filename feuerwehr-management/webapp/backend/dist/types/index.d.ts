import { Request } from 'express';
export interface AuthUser {
    id: number;
    username: string;
    email: string | null;
    name: string | null;
    isAdmin: boolean;
    groupId: number | null;
    permissions: Record<string, boolean>;
}
declare global {
    namespace Express {
        interface Request {
            user?: AuthUser;
        }
    }
}
export interface AuthRequest extends Request {
    user: AuthUser;
}
//# sourceMappingURL=index.d.ts.map