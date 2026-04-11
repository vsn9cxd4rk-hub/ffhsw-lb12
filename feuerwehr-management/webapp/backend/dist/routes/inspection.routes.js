"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const express_1 = require("express");
const auth_middleware_1 = require("../middleware/auth.middleware");
const inspection_controller_1 = require("../controllers/inspection.controller");
const router = (0, express_1.Router)();
router.use(auth_middleware_1.authenticate);
router.get('/', inspection_controller_1.getInspections);
router.get('/due', inspection_controller_1.getDueInspections);
router.get('/report', inspection_controller_1.getInspectionReport);
router.get('/criteria/:articleId', inspection_controller_1.getInspectionCriteria);
router.get('/article/:articleId', inspection_controller_1.getArticleInspections);
router.post('/', inspection_controller_1.createInspection);
router.put('/:id', inspection_controller_1.updateInspection);
exports.default = router;
//# sourceMappingURL=inspection.routes.js.map