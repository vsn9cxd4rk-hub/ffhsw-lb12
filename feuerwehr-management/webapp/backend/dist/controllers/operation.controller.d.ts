import { Request, Response } from 'express';
export declare const uploadMiddleware: import("express").RequestHandler<import("express-serve-static-core").ParamsDictionary, any, any, import("qs").ParsedQs, Record<string, any>>;
export declare function getOperations(req: Request, res: Response): Promise<void>;
export declare function createOperation(req: Request, res: Response): Promise<void>;
export declare function getOperation(req: Request, res: Response): Promise<void>;
export declare function updateOperation(req: Request, res: Response): Promise<void>;
export declare function deleteOperation(req: Request, res: Response): Promise<void>;
export declare function createOperationTime(req: Request, res: Response): Promise<void>;
export declare function updateOperationTime(req: Request, res: Response): Promise<void>;
export declare function deleteOperationTime(req: Request, res: Response): Promise<void>;
export declare function getOperationReport(req: Request, res: Response): Promise<void>;
export declare function upsertOperationReport(req: Request, res: Response): Promise<void>;
export declare function getDocuments(req: Request, res: Response): Promise<void>;
export declare function uploadDocument(req: Request, res: Response): Promise<void>;
export declare function downloadDocument(req: Request, res: Response): Promise<void>;
export declare function deleteDocument(req: Request, res: Response): Promise<void>;
//# sourceMappingURL=operation.controller.d.ts.map