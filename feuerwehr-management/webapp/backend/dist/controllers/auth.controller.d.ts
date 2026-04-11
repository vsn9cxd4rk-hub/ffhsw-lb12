import { Request, Response } from 'express';
export declare const loginValidation: import("express-validator").ValidationChain[];
export declare function login(req: Request, res: Response): Promise<void>;
export declare function logout(req: Request, res: Response): Promise<void>;
export declare function refresh(req: Request, res: Response): Promise<void>;
export declare function getMe(req: Request, res: Response): Promise<void>;
export declare const changePasswordValidation: import("express-validator").ValidationChain[];
export declare function changePassword(req: Request, res: Response): Promise<void>;
//# sourceMappingURL=auth.controller.d.ts.map