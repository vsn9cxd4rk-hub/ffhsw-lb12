"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.authenticate = authenticate;
const jwt_1 = require("../config/jwt");
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
async function authenticate(req, res, next) {
    try {
        const authHeader = req.headers.authorization;
        if (!authHeader?.startsWith('Bearer ')) {
            (0, response_1.sendError)(res, 'Nicht authentifiziert', 401);
            return;
        }
        const token = authHeader.substring(7);
        const payload = (0, jwt_1.verifyAccessToken)(token);
        const user = await database_1.prisma.user.findUnique({
            where: { id: payload.userId },
            include: { group: true },
        });
        if (!user || !user.isActive) {
            (0, response_1.sendError)(res, 'Benutzer nicht gefunden oder deaktiviert', 401);
            return;
        }
        // Build permissions map from group
        const permissions = {};
        if (user.group) {
            for (let i = 0; i <= 75; i++) {
                permissions[`br${i}`] = user.group[`br${i}`] ?? false;
            }
        }
        req.user = {
            id: user.id,
            username: user.username,
            email: user.email,
            name: user.name,
            isAdmin: user.isAdmin,
            groupId: user.groupId,
            permissions,
        };
        next();
    }
    catch {
        (0, response_1.sendError)(res, 'Ungültiger oder abgelaufener Token', 401);
    }
}
//# sourceMappingURL=auth.middleware.js.map