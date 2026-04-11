import { Request, Response } from 'express';
export declare const eventDocUpload: import("express").RequestHandler<import("express-serve-static-core").ParamsDictionary, any, any, import("qs").ParsedQs, Record<string, any>>;
export declare function getEvents(req: Request, res: Response): Promise<void>;
export declare function createEvent(req: Request, res: Response): Promise<void>;
export declare function getEvent(req: Request, res: Response): Promise<void>;
export declare function updateEvent(req: Request, res: Response): Promise<void>;
export declare function deleteEvent(req: Request, res: Response): Promise<void>;
export declare function getAttendance(req: Request, res: Response): Promise<void>;
export declare function updateAttendance(req: Request, res: Response): Promise<void>;
export declare function getFireWatches(req: Request, res: Response): Promise<void>;
export declare function createFireWatch(req: Request, res: Response): Promise<void>;
export declare function getFireWatch(req: Request, res: Response): Promise<void>;
export declare function updateFireWatch(req: Request, res: Response): Promise<void>;
export declare function deleteFireWatch(req: Request, res: Response): Promise<void>;
export declare function getEventDocuments(req: Request, res: Response): Promise<void>;
export declare function uploadEventDocument(req: Request, res: Response): Promise<void>;
export declare function downloadEventDocument(req: Request, res: Response): Promise<void>;
export declare function deleteEventDocument(req: Request, res: Response): Promise<void>;
//# sourceMappingURL=event.controller.d.ts.map