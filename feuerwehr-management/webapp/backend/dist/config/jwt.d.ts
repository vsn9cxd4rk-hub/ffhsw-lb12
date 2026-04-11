export interface JwtPayload {
    userId: number;
    username: string;
    isAdmin: boolean;
    groupId: number | null;
}
export declare function signAccessToken(payload: JwtPayload): string;
export declare function signRefreshToken(payload: JwtPayload): string;
export declare function verifyAccessToken(token: string): JwtPayload;
export declare function verifyRefreshToken(token: string): JwtPayload;
export declare function getRefreshTokenExpiry(): Date;
//# sourceMappingURL=jwt.d.ts.map