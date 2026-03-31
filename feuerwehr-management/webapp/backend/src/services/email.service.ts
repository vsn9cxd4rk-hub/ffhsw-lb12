import nodemailer from 'nodemailer';
import { logger } from '../utils/logger';

class EmailService {
  private transporter: nodemailer.Transporter | null = null;

  private getTransporter(): nodemailer.Transporter {
    if (!this.transporter) {
      this.transporter = nodemailer.createTransport({
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

  async sendEmail(to: string, subject: string, html: string, text?: string): Promise<void> {
    if (!process.env.SMTP_HOST) {
      logger.warn('SMTP not configured, skipping email to:', to);
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
      logger.info(`Email sent to ${to}: ${subject}`);
    } catch (error) {
      logger.error(`Failed to send email to ${to}:`, error);
      throw error;
    }
  }

  async sendInspectionReminder(to: string, type: string, entityName: string, dueDate: Date): Promise<void> {
    const dateStr = dueDate.toLocaleDateString('de-DE');
    await this.sendEmail(
      to,
      `Erinnerung: ${type} - ${entityName}`,
      `<h2>Erinnerung: ${type}</h2>
       <p>Die ${type} für <strong>${entityName}</strong> ist am <strong>${dateStr}</strong> fällig.</p>
       <p>Bitte planen Sie die notwendigen Maßnahmen rechtzeitig ein.</p>`
    );
  }

  async sendMedicalExamReminder(to: string, memberName: string, examType: string, dueDate: Date): Promise<void> {
    const dateStr = dueDate.toLocaleDateString('de-DE');
    await this.sendEmail(
      to,
      `Erinnerung: Untersuchung ${examType} - ${memberName}`,
      `<h2>Erinnerung: Arbeitsmedizinische Untersuchung</h2>
       <p>Die Untersuchung <strong>${examType}</strong> für <strong>${memberName}</strong> ist am <strong>${dateStr}</strong> fällig.</p>
       <p>Bitte vereinbaren Sie rechtzeitig einen Termin.</p>`
    );
  }
}

export const emailService = new EmailService();
