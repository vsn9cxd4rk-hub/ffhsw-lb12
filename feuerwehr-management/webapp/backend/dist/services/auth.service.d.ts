export declare class AuthService {
    login(username: string, password: string): Promise<{
        accessToken: string;
        refreshToken: string;
        user: {
            id: number;
            username: string;
            email: string | null;
            name: string | null;
            isAdmin: boolean;
            groupId: number | null;
            permissions: Record<string, boolean>;
        };
    }>;
    refreshToken(token: string): Promise<{
        accessToken: string;
    }>;
    logout(userId: number, token: string): Promise<void>;
    logoutAll(userId: number): Promise<void>;
    changePassword(userId: number, oldPassword: string, newPassword: string): Promise<void>;
    hashPassword(password: string): Promise<string>;
}
export declare const authService: AuthService;
//# sourceMappingURL=auth.service.d.ts.map