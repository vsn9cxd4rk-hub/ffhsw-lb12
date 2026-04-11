"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.sendSuccess = sendSuccess;
exports.sendError = sendError;
exports.sendPaginated = sendPaginated;
function sendSuccess(res, data, statusCode = 200) {
    return res.status(statusCode).json({ success: true, data });
}
function sendError(res, message, statusCode = 500) {
    return res.status(statusCode).json({ success: false, error: message });
}
function sendPaginated(res, data, total, page, limit) {
    return res.json({
        success: true,
        data,
        pagination: {
            total,
            page,
            limit,
            pages: Math.ceil(total / limit),
        },
    });
}
//# sourceMappingURL=response.js.map