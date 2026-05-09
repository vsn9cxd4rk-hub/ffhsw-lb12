/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.activation.DataHandler
 *  javax.activation.DataSource
 *  javax.activation.FileDataSource
 *  javax.mail.Address
 *  javax.mail.BodyPart
 *  javax.mail.Message
 *  javax.mail.Message$RecipientType
 *  javax.mail.MessagingException
 *  javax.mail.Multipart
 *  javax.mail.Session
 *  javax.mail.Transport
 *  javax.mail.internet.InternetAddress
 *  javax.mail.internet.MimeBodyPart
 *  javax.mail.internet.MimeMessage
 *  javax.mail.internet.MimeMultipart
 *  logging.logging
 *  utilities.SbcUtils
 *  utilities.hash
 */
package utilities_email;

import data.tabellen.email.TabelleEMail_gesendet;
import go.email.Gesendet;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.hash;
import utilities_email.EMail_utils;

public class SendeOpperation {
    public static void senden(String TO, String CC, String BCC, String Betreff, String Nachricht, File[] Anhang) throws SQLException, MessagingException, UnsupportedEncodingException {
        double startZeit = System.currentTimeMillis();
        String host = runApplication.EINSTELLUNGEN.get("smtpServer");
        int port = Integer.parseInt(runApplication.EINSTELLUNGEN.get("smtpPort"));
        String user = runApplication.EINSTELLUNGEN.get("emailAdresse");
        String pass = hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("emailPasswort"));
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        if (runApplication.EINSTELLUNGEN.get("useSSL").equals("1")) {
            props.put("mail.smtp.starttls.enable", "true");
        } else {
            props.put("mail.smtp.starttls.enable", "false");
        }
        Session session = Session.getInstance((Properties)props);
        Transport transport = session.getTransport("smtp");
        transport.connect(host, port, user, pass);
        InternetAddress[] addressesTO = InternetAddress.parse((String)TO);
        InternetAddress[] addressesCC = InternetAddress.parse((String)CC);
        InternetAddress[] addressesBCC = InternetAddress.parse((String)BCC);
        MimeMessage message = new MimeMessage(session);
        if (runApplication.EINSTELLUNGEN.get("eMailName").equals("")) {
            message.setFrom((Address)new InternetAddress(user));
            logging.logInfo((Object)"Kein E-Mail Name konfiguriert, setze E-Mail Adresse als Namen");
        } else {
            message.setFrom((Address)new InternetAddress(user, runApplication.EINSTELLUNGEN.get("eMailName")));
            logging.logInfo((Object)"Setze E-Mail Name als Absender...");
        }
        message.setRecipients(Message.RecipientType.TO, (Address[])addressesTO);
        message.setRecipients(Message.RecipientType.CC, (Address[])addressesCC);
        message.setRecipients(Message.RecipientType.BCC, (Address[])addressesBCC);
        message.setSubject(Betreff);
        Gesendet send = new Gesendet();
        TabelleEMail_gesendet tabSend = new TabelleEMail_gesendet();
        int newMailID = tabSend.getNextNummer();
        send.setId(newMailID);
        send.setAn(TO);
        send.setCc(CC);
        send.setBcc(BCC);
        send.setBetreff(Betreff);
        send.setNachricht(Nachricht);
        send.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
        if (Anhang.length != 0) {
            MimeMultipart mimeMultipart = new MimeMultipart();
            MimeBodyPart text = new MimeBodyPart();
            text.setText(Nachricht);
            int i = 0;
            while (i < Anhang.length) {
                try {
                    text.setDisposition("inline");
                    MimeBodyPart attachement = SendeOpperation.addFileToMail(Anhang[i].getAbsoluteFile());
                    mimeMultipart.addBodyPart((BodyPart)attachement);
                    EMail_utils.saveFile(Anhang[i], newMailID, "Gesendet");
                }
                catch (IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                ++i;
            }
            mimeMultipart.addBodyPart((BodyPart)text);
            message.setContent((Multipart)mimeMultipart);
            send.setAnhang(1);
        } else {
            message.setText(Nachricht);
            send.setAnhang(0);
        }
        transport.sendMessage((Message)message, (Address[])addressesTO);
        if (!CC.equals("")) {
            transport.sendMessage((Message)message, (Address[])addressesCC);
        }
        if (!BCC.equals("")) {
            transport.sendMessage((Message)message, (Address[])addressesBCC);
        }
        tabSend.insert(send);
        logging.logInfo((Object)"Email Senden erfolgreich...");
        transport.close();
        double endZeit = (double)System.currentTimeMillis() - startZeit;
        logging.logInfo((Object)("E-Mail gesendet in: " + endZeit + " ms"));
    }

    private static MimeBodyPart addFileToMail(File Anhang) throws MessagingException {
        MimeBodyPart attachement = new MimeBodyPart();
        attachement.setDataHandler(new DataHandler((DataSource)new FileDataSource(Anhang)));
        attachement.setFileName(Anhang.getName());
        attachement.setDisposition("attachment");
        return attachement;
    }
}

