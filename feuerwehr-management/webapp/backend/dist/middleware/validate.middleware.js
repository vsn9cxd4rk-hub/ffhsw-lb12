"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.validate = validate;
const express_validator_1 = require("express-validator");
function validate(req, res, next) {
    const errors = (0, express_validator_1.validationResult)(req);
    if (!errors.isEmpty()) {
        res.status(422).json({
            success: false,
            error: 'Validierungsfehler',
            details: errors.array(),
        });
        return;
    }
    next();
}
//# sourceMappingURL=validate.middleware.js.map