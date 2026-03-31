package utilities_email;

import ao.HauptprogrammAO;
import ao.utils.SystemTrayInfo;
import data.tabellen.email.TabelleEMail_unwetterwarnung;
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
import javax.mail.Flags.Flag;
import javax.mail.internet.MimeBodyPart;
import logging.logging;
import run.runApplication;
import utilities.Utils;
import utilities.hash;
import utilities.logbuchEingabe;

public class EmpfangenOpperationUnwetterwarnung {

   public static void empfangen() throws SQLException {
      String host = (String)runApplication.EINSTELLUNGEN.get("unwetterwarnungPop3");
      String user = (String)runApplication.EINSTELLUNGEN.get("unwetterwarnungEMail");
      String passwd = hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungPasswort"));
      String port = (String)runApplication.EINSTELLUNGEN.get("unwetterwarnungPopPort");
      empfangenAusfuehren(host, user, passwd, port);
   }

   private static void empfangenAusfuehren(String host, String user, String passwd, String port) {
      try {
         Properties e = new Properties();
         e.setProperty("mail.pop3.host", host);
         e.setProperty("mail.pop3.user", user);
         e.setProperty("mail.pop3.password", passwd);
         e.setProperty("mail.pop3.port", port);
         if(((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungSSL")).equals("1")) {
            e.put("mail.pop3.auth", "true");
         } else {
            e.put("mail.pop3.auth", "false");
         }

         e.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         Session session = Session.getDefaultInstance(e);
         Store store = session.getStore("pop3");
         store.connect(host, user, passwd);
         Folder folder = store.getFolder("INBOX");
         folder.open(2);
         Message[] message = folder.getMessages();
         TabelleEMail_unwetterwarnung empfangen = new TabelleEMail_unwetterwarnung();
         Empfangen data = new Empfangen();
         logging.logInfo("Anzehl der EMails:" + message.length);

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

               for(int var25 = 0; var25 < mp.getCount(); ++var25) {
                  logging.logInfo(Integer.valueOf(mp.getCount()));
                  logging.logInfo(mp.getContentType());
                  BodyPart part = mp.getBodyPart(var25);
                  String disposition = part.getDisposition();
                  if(disposition == null || disposition.equalsIgnoreCase("attachment")) {
                     MimeBodyPart mimePart = (MimeBodyPart)part;
                     BufferedReader in;
                     StringBuilder nachrichtMultipart;
                     File file;
                     FileWriter writer;
                     String line;
                     String var26;
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

                           while((var26 = in.readLine()) != null) {
                              nachrichtMultipart.append(var26);
                              nachrichtMultipart.append("\n");
                           }

                           data.setNachricht(nachrichtMultipart.toString());
                        }
                     }

                     if(mimePart.isMimeType("text/plain")) {
                        logging.logInfo("Nachrichttype --> text/plain");
                        in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                        nachrichtMultipart = new StringBuilder();

                        while((var26 = in.readLine()) != null) {
                           nachrichtMultipart.append(var26);
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
            infomGUI(data);
            m.writeTo(new FileOutputStream(new File(runApplication.arbeitsverzeichnis + "data/EMail/Temp/original_nachricht_unwetter/email" + newMailID + ".email")));
            Utils.dateiKatalogisieren(runApplication.arbeitsverzeichnis + "data/EMail/Temp/original_nachricht_unwetter/email" + newMailID + ".email");
            m.setFlag(Flag.DELETED, true);
         }

         folder.close(true);
         store.close();
         logging.logInfo("Empfangen beendet");
      } catch (Exception var24) {
         logging.logError(var24);
         var24.printStackTrace();
      }

   }

   private static void infomGUI(Empfangen data) {
      logging.logInfo("Strate: informGUI()");
      logging.logInfo(data.getBetreff());
      if(data.getBetreff().toString().startsWith("DWD ->")) {
         try {
            String[] e = null;
            int v = data.getNachricht().toString().indexOf("von:");
            int b = data.getNachricht().toString().indexOf("voraussichtlich bis:");
            e = data.getNachricht().toString().substring(b, b + b - v + 1).split("\\s");
            runApplication.unwetterwarnungDatumBis = e[3];
            runApplication.unwetterwarnungUhrzeitBis = e[4];
            if(runApplication.lastUnwetterwarnungID <= data.getId()) {
               HauptprogrammAO.buttonUnwetterwarnung.setVisible(true);
               HauptprogrammAO.buttonUnwetterwarnung.setToolTipText(data.getBetreff());
               logging.logInfo("Unwetterwarnung Bis: " + runApplication.unwetterwarnungDatumBis + " / " + runApplication.unwetterwarnungUhrzeitBis);
               logbuchEingabe.NeuerEintag("Unwetterwarnung: " + data.getBetreff());
               logbuchEingabe.NeuerEintag("Unwetterwarnung Bis: " + runApplication.unwetterwarnungDatumBis + " / " + runApplication.unwetterwarnungUhrzeitBis);
               runApplication.lastUnwetterwarnungID = data.getId();
               runApplication.unwetterwarnungStatus = 1;
               SystemTrayInfo trayInfo = new SystemTrayInfo();
               trayInfo.InfoIconUnwetterwarnung("Unwetterwarnung: " + data.getBetreff());
            }
         } catch (Exception var5) {
            logging.logWarning("Bei Lesen der DWD E-Mails ist ein Problem aufgeteten - " + var5);
         }
      } else {
         logging.logWarning("Ich habe eine EMail über die Unwetteradresse erhalten, dieser EMail fehlt der Unwetter (DWD ->) Präfix!!! --> Ignoriere Nachricht!!!");
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
