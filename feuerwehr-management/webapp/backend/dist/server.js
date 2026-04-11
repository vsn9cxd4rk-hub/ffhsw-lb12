"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
require("dotenv/config");
const app_1 = __importDefault(require("./app"));
const logger_1 = require("./utils/logger");
const database_1 = require("./config/database");
const PORT = parseInt(process.env.PORT || '3001', 10);
async function startServer() {
    try {
        await database_1.prisma.$connect();
        logger_1.logger.info('Database connected successfully');
        const server = app_1.default.listen(PORT, () => {
            logger_1.logger.info(`Server running on port ${PORT} in ${process.env.NODE_ENV || 'development'} mode`);
        });
        // Graceful shutdown
        const shutdown = async (signal) => {
            logger_1.logger.info(`${signal} received - shutting down gracefully`);
            server.close(async () => {
                await database_1.prisma.$disconnect();
                logger_1.logger.info('Database disconnected');
                process.exit(0);
            });
            setTimeout(() => {
                logger_1.logger.error('Forced shutdown after timeout');
                process.exit(1);
            }, 10000);
        };
        process.on('SIGTERM', () => shutdown('SIGTERM'));
        process.on('SIGINT', () => shutdown('SIGINT'));
    }
    catch (error) {
        logger_1.logger.error('Failed to start server:', error);
        process.exit(1);
    }
}
startServer();
//# sourceMappingURL=server.js.map