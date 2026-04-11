"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.emailService = void 0;
const nodemailer_1 = __importDefault(require("nodemailer"));
const logger_1 = require("../utils/logger");
class EmailService {
    constructor() {
        this.transporter = null;
    }
    getTransporter() {
        if (!this.transporter) {
            this.transporter = nodemailer_1.default.createTransport({
                host: process.env.SMTP_HOST,
                port: parseInt(process.env.SMTP_PORT || '587', 10),
                secure: process.env.SMTP_SECURE === 'true',
                auth: process.env.SMTP_USER
                    ? { user: process.env.SMTP_USER, pass: process.env.SMTP_PASS }
                    : undefined,
            });
        }
        return this.transporter;
    }
    async sendEmail(to, subject, html, text) {
        if (!process.env.SMTP_HOST) {
            logger_1.logger.warn('SMTP not configured, skipping email to:', to);
            return;
        }
        try {
            await this.getTransporter().sendMail({
                from: process.env.SMTP_FROM || 'noreply@feuerwehr.local',
                to,
                subject,
                html,
                text: text || html.replace(/<[^>]*>/g, ''),
            });
            logger_1.logger.info(`Email sent to ${to}: ${subject}`);
        }
        catch (error) {
            logger_1.logger.error(`Failed to send email to ${to}:`, error);
            throw error;
        }
    }
    async sendInspectionReminder(to, type, entityName, dueDate) {
        const dateStr = dueDate.toLocaleDateString('de-DE');
        await this.sendEmail(to, `Erinnerung: ${type} - ${entityName}`, `<h2>Erinnerung: ${type}</h2>
       <p>Die ${type} für <strong>${entityName}</strong> ist am <strong>${dateStr}</strong> fällig.</p>
       <p>Bitte planen Sie die notwendigen Maßnahmen rechtzeitig ein.</p>`);
    }
    async sendMedicalExamReminder(to, memberName, examType, dueDate) {
        const dateStr = dueDate.toLocaleDateString('de-DE');
        await this.sendEmail(to, `Erinnerung: Untersuchung ${examType} - ${memberName}`, `<h2>Erinnerung: Arbeitsmedizinische Untersuchung</h2>
       <p>Die Untersuchung <strong>${examType}</strong> für <strong>${memberName}</strong> ist am <strong>${dateStr}</strong> fällig.</p>
       <p>Bitte vereinbaren Sie rechtzeitig einen Termin.</p>`);
    }
}
exports.emailService = new EmailService();
//# sourceMappingURL=email.service.js.map