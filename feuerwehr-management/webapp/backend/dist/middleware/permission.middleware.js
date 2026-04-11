"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.requireAdmin = requireAdmin;
exports.requirePermission = requirePermission;
const response_1 = require("../utils/response");
function requireAdmin(req, res, next) {
    if (!req.user?.isAdmin) {
        (0, response_1.sendError)(res, 'Administratorrechte erforderlich', 403);
        return;
    }
    next();
}
function requirePermission(bit) {
    return (req, res, next) => {
        if (!req.user) {
            (0, response_1.sendError)(res, 'Nicht authentifiziert', 401);
            return;
        }
        if (req.user.isAdmin || req.user.permissions[bit]) {
            next();
            return;
        }
        (0, response_1.sendError)(res, 'Keine Berechtigung', 403);
    };
}
//# sourceMappingURL=permission.middleware.js.map