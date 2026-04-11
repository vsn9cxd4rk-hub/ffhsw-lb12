import { Request } from 'express';
export interface PaginationOptions {
    skip: number;
    take: number;
    page: number;
    limit: number;
}
export declare function getPagination(req: Request): PaginationOptions;
//# sourceMappingURL=pagination.d.ts.map