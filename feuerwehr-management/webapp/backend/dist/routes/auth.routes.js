"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const auth_middleware_1 = require("../middleware/auth.middleware");
const validate_middleware_1 = require("../middleware/validate.middleware");
const auth_controller_1 = require("../controllers/auth.controller");
const router = (0, express_1.Router)();
router.post('/login', auth_controller_1.loginValidation, validate_middleware_1.validate, auth_controller_1.login);
router.post('/logout', auth_middleware_1.authenticate, auth_controller_1.logout);
router.post('/refresh', auth_controller_1.refresh);
router.get('/me', auth_middleware_1.authenticate, auth_controller_1.getMe);
router.put('/change-password', auth_middleware_1.authenticate, auth_controller_1.changePasswordValidation, validate_middleware_1.validate, auth_controller_1.changePassword);
exports.default = router;
//# sourceMappingURL=auth.routes.js.map