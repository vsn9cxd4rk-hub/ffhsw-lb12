import { Request, Response } from 'express';
export declare const createUserValidation: import("express-validator").ValidationChain[];
export declare function getUsers(req: Request, res: Response): Promise<void>;
export declare function createUser(req: Request, res: Response): Promise<void>;
export declare function getUser(req: Request, res: Response): Promise<void>;
export declare function updateUser(req: Request, res: Response): Promise<void>;
export declare function deleteUser(req: Request, res: Response): Promise<void>;
export declare function getPermissionGroups(_req: Request, res: Response): Promise<void>;
export declare function createPermissionGroup(req: Request, res: Response): Promise<void>;
export declare function updatePermissionGroup(req: Request, res: Response): Promise<void>;
export declare function deletePermissionGroup(req: Request, res: Response): Promise<void>;
//# sourceMappingURL=user.controller.d.ts.map