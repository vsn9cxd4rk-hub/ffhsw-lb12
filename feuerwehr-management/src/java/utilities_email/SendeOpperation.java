package utilities_email;

import data.tabellen.email.TabelleEMail_gesendet;
import go.email.Gesendet;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
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
      double startZeit = (double)System.currentTimeMillis();
      String host = (String)runApplication.EINSTELLUNGEN.get("smtpServer");
      int port = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("smtpPort"));
      String user = (String)runApplication.EINSTELLUNGEN.get("emailAdresse");
      String pass = hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("emailPasswort"));
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      if(((String)runApplication.EINSTELLUNGEN.get("useSSL")).equals("1")) {
         props.put("mail.smtp.starttls.enable", "true");
      } else {
         props.put("mail.smtp.starttls.enable", "false");
      }

      Session session = Session.getInstance(props);
      Transport transport = session.getTransport("smtp");
      transport.connect(host, port, user, pass);
      InternetAddress[] addressesTO = InternetAddress.parse(TO);
      InternetAddress[] addressesCC = InternetAddress.parse(CC);
      InternetAddress[] addressesBCC = InternetAddress.parse(BCC);
      MimeMessage message = new MimeMessage(session);
      if(((String)runApplication.EINSTELLUNGEN.get("eMailName")).equals("")) {
         message.setFrom(new InternetAddress(user));
         logging.logInfo("Kein E-Mail Name konfiguriert, setze E-Mail Adresse als Namen");
      } else {
         message.setFrom(new InternetAddress(user, (String)runApplication.EINSTELLUNGEN.get("eMailName")));
         logging.logInfo("Setze E-Mail Name als Absender...");
      }

      message.setRecipients(RecipientType.TO, addressesTO);
      message.setRecipients(RecipientType.CC, addressesCC);
      message.setRecipients(RecipientType.BCC, addressesBCC);
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
      send.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
      if(Anhang.length != 0) {
         MimeMultipart endZeit = new MimeMultipart();
         MimeBodyPart text = new MimeBodyPart();
         text.setText(Nachricht);

         for(int i = 0; i < Anhang.length; ++i) {
            try {
               text.setDisposition("inline");
               MimeBodyPart e = addFileToMail(Anhang[i].getAbsoluteFile());
               endZeit.addBodyPart(e);
               EMail_utils.saveFile(Anhang[i], newMailID, "Gesendet");
            } catch (IOException var26) {
               logging.logPrintStackTrace(var26);
            }
         }

         endZeit.addBodyPart(text);
         message.setContent(endZeit);
         send.setAnhang(1);
      } else {
         message.setText(Nachricht);
         send.setAnhang(0);
      }

      transport.sendMessage(message, addressesTO);
      if(!CC.equals("")) {
         transport.sendMessage(message, addressesCC);
      }

      if(!BCC.equals("")) {
         transport.sendMessage(message, addressesBCC);
      }

      tabSend.insert(send);
      logging.logInfo("Email Senden erfolgreich...");
      transport.close();
      double var27 = (double)System.currentTimeMillis() - startZeit;
      logging.logInfo("E-Mail gesendet in: " + var27 + " ms");
   }

   private static MimeBodyPart addFileToMail(File Anhang) throws MessagingException {
      MimeBodyPart attachement = new MimeBodyPart();
      attachement.setDataHandler(new DataHandler(new FileDataSource(Anhang)));
      attachement.setFileName(Anhang.getName());
      attachement.setDisposition("attachment");
      return attachement;
   }
}
