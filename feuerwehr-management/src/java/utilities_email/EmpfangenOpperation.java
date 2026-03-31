package utilities_email;

import data.tabellen.email.TabelleEMail_empfangen;
import go.email.Empfangen;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import logging.logging;
import run.runApplication;
import utilities.Utils;
import utilities.hash;

public class EmpfangenOpperation {

   public static void empfangen() throws SQLException {
      String host = (String)runApplication.EINSTELLUNGEN.get("pop3Server");
      String user = (String)runApplication.EINSTELLUNGEN.get("emailAdresse");
      String passwd = hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("emailPasswort"));
      empfangenAusfuehren(host, user, passwd);
   }

   private static void empfangenAusfuehren(String host, String user, String passwd) {
      try {
         Properties e = new Properties();
         e.setProperty("mail.pop3.host", host);
         e.setProperty("mail.pop3.user", user);
         e.setProperty("mail.pop3.password", passwd);
         e.setProperty("mail.pop3.port", "995");
         e.setProperty("mail.pop3.auth", "true");
         e.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         Session session = Session.getDefaultInstance(e);
         Store store = session.getStore("pop3");
         store.connect(host, user, passwd);
         Folder folder = store.getFolder("INBOX");
         folder.open(2);
         Message[] message = folder.getMessages();
         TabelleEMail_empfangen empfangen = new TabelleEMail_empfangen();
         Empfangen data = new Empfangen();

         for(int i = 0; i < message.length; ++i) {
            Message m = message[i];
            int newMailID = empfangen.getNextNummer();
            logging.logInfo("Akt. Nachricht: " + i + " DB-Nummer: " + newMailID);
            data.setId(newMailID);
            logging.logInfo("From: " + m.getFrom()[0]);
            data.setSender(m.getFrom()[0].toString());
            logging.logInfo("Send Date: " + m.getSentDate());
            data.setDate(m.getSentDate().toString());
            logging.logInfo("Size: " + m.getSize());
            data.setSize(m.getSize());
            logging.logInfo("Subject: " + m.getSubject());
            data.setBetreff(m.getSubject());
            if(m.isMimeType("text/plain")) {
               logging.logInfo("Nachrichttype --> text/plain");
               data.setNachricht(m.getContent().toString());
               data.setArt("plain");
            } else if(m.isMimeType("multipart/*")) {
               logging.logInfo("Nachrichttype --> multipart/*");
               Multipart mp = (Multipart)m.getContent();
               if(mp.getCount() > 1) {
                  BodyPart j = mp.getBodyPart(0);
                  System.out.println(j.getContent());
               }

               for(int var24 = 0; var24 < mp.getCount(); ++var24) {
                  logging.logInfo(Integer.valueOf(mp.getCount()));
                  logging.logInfo(mp.getContentType());
                  BodyPart part = mp.getBodyPart(var24);
                  String disposition = part.getDisposition();
                  if(disposition == null || disposition.equalsIgnoreCase("attachment")) {
                     MimeBodyPart mimePart = (MimeBodyPart)part;
                     BufferedReader in;
                     StringBuilder nachrichtMultipart;
                     File file;
                     FileWriter writer;
                     String line;
                     String var25;
                     if(mimePart.isMimeType("multipart/*")) {
                        logging.logInfo("--------------------------------------------------------- Amazon nachricht");
                        if(mimePart.isMimeType("text/html")) {
                           logging.logInfo("Nachrichttype --> text/html");
                           in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                           nachrichtMultipart = new StringBuilder();
                           file = new File(runApplication.arbeitsverzeichnis + "data/EMail/temp/email" + newMailID + ".html");
                           writer = new FileWriter(file, true);

                           while((line = in.readLine()) != null) {
                              nachrichtMultipart.append(line);
                              nachrichtMultipart.append("\n");
                              writer.write(line);
                              writer.write(System.getProperty("line.separator"));
                           }

                           writer.flush();
                           writer.close();
                           data.setArt("html");
                           data.setNachricht("WeHaveAnHTMLFileForClient");
                        }

                        if(mimePart.isMimeType("text/plain")) {
                           logging.logInfo("Nachrichttype --> text/plain");
                           in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                           nachrichtMultipart = new StringBuilder();

                           while((var25 = in.readLine()) != null) {
                              nachrichtMultipart.append(var25);
                              nachrichtMultipart.append("\n");
                           }

                           data.setNachricht(nachrichtMultipart.toString());
                        }
                     }

                     if(mimePart.isMimeType("text/plain")) {
                        logging.logInfo("Nachrichttype --> text/plain");
                        in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                        nachrichtMultipart = new StringBuilder();

                        while((var25 = in.readLine()) != null) {
                           nachrichtMultipart.append(var25);
                           nachrichtMultipart.append("\n");
                        }

                        data.setNachricht(nachrichtMultipart.toString());
                     }

                     if(mimePart.isMimeType("text/html")) {
                        logging.logInfo("Nachrichttype --> text/html");
                        in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                        nachrichtMultipart = new StringBuilder();
                        file = new File(runApplication.arbeitsverzeichnis + "data/EMail/temp/email" + newMailID + ".html");
                        writer = new FileWriter(file, true);

                        while((line = in.readLine()) != null) {
                           nachrichtMultipart.append(line);
                           nachrichtMultipart.append("\n");
                           writer.write(line);
                           writer.write(System.getProperty("line.separator"));
                        }

                        writer.flush();
                        writer.close();
                        data.setArt("html");
                        data.setNachricht("WeHaveAnHTMLFileForClient");
                     }
                  }

                  receiveAttachment(data, newMailID, part, disposition);
               }
            } else {
               logging.logInfo("Nachrichttype --> other");
               data.setNachricht(m.getContent().toString());
               data.setArt("other");
            }

            data.setGelesen(0);
            empfangen.insert(data);
            m.writeTo(new FileOutputStream(new File(runApplication.arbeitsverzeichnis + "data/EMail/Temp/original_nachricht/email" + newMailID + ".email")));
            Utils.dateiKatalogisieren(runApplication.arbeitsverzeichnis + "data/EMail/Temp/original_nachricht/email" + newMailID + ".email");
         }

         folder.close(false);
         store.close();
         logging.logInfo("Empfangen beendet");
      } catch (Exception var23) {
         logging.logError(var23);
         var23.printStackTrace();
      }

   }

   private static void receiveAttachment(Empfangen data, int newMailID, Part part, String disposition) throws MessagingException, FileNotFoundException, IOException, SQLException {
      if(disposition != null && (disposition.equals("attachment") || disposition.equals("inline"))) {
         Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/empfangende/" + newMailID, runApplication.clientID);
         File f = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/empfangende/" + newMailID + "/" + part.getFileName());
         FileOutputStream fos = new FileOutputStream(f);
         InputStream is = part.getInputStream();
         byte[] buffer = new byte[1024];
         boolean bytesRead = false;

         int bytesRead1;
         while((bytesRead1 = is.read(buffer)) != -1) {
            fos.write(buffer, 0, bytesRead1);
         }

         fos.close();
         is.close();
         data.setAnhang(1);
         logging.logInfo("Speichere Anhang");
         Utils.dateiKatalogisieren(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/empfangende/" + newMailID + "/" + part.getFileName());
      } else {
         data.setAnhang(0);
      }

   }
}
