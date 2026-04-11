import { Request, Response } from 'express';
export declare const certificateUpload: import("express").RequestHandler<import("express-serve-static-core").ParamsDictionary, any, any, import("qs").ParsedQs, Record<string, any>>;
export declare function getCourses(req: Request, res: Response): Promise<void>;
export declare function createCourse(req: Request, res: Response): Promise<void>;
export declare function getCourse(req: Request, res: Response): Promise<void>;
export declare function updateCourse(req: Request, res: Response): Promise<void>;
export declare function deleteCourse(req: Request, res: Response): Promise<void>;
export declare function uploadCertificate(req: Request, res: Response): Promise<void>;
export declare function downloadCertificate(req: Request, res: Response): Promise<void>;
export declare function deleteCertificate(req: Request, res: Response): Promise<void>;
export declare function getCategories(_req: Request, res: Response): Promise<void>;
export declare function createCategory(req: Request, res: Response): Promise<void>;
export declare function updateCategory(req: Request, res: Response): Promise<void>;
export declare function deleteCategory(req: Request, res: Response): Promise<void>;
//# sourceMappingURL=training.controller.d.ts.map