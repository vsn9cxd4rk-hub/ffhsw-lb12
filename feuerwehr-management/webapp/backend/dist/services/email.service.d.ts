declare class EmailService {
    private transporter;
    private getTransporter;
    sendEmail(to: string, subject: string, html: string, text?: string): Promise<void>;
    sendInspectionReminder(to: string, type: string, entityName: string, dueDate: Date): Promise<void>;
    sendMedicalExamReminder(to: string, memberName: string, examType: string, dueDate: Date): Promise<void>;
}
export declare const emailService: EmailService;
export {};
//# sourceMappingURL=email.service.d.ts.map