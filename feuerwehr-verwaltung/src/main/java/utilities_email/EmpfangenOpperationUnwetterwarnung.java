/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.mail.BodyPart
 *  javax.mail.Flags$Flag
 *  javax.mail.Folder
 *  javax.mail.Message
 *  javax.mail.MessagingException
 *  javax.mail.Multipart
 *  javax.mail.Part
 *  javax.mail.Session
 *  javax.mail.Store
 *  javax.mail.internet.MimeBodyPart
 *  logging.logging
 *  utilities.hash
 */
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
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Flags;
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
import utilities.logbuchEingabe;

public class EmpfangenOpperationUnwetterwarnung {
    public static void empfangen() throws SQLException {
        String host = runApplication.EINSTELLUNGEN.get("unwetterwarnungPop3");
        String user = runApplication.EINSTELLUNGEN.get("unwetterwarnungEMail");
        String passwd = hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungPasswort"));
        String port = runApplication.EINSTELLUNGEN.get("unwetterwarnungPopPort");
        EmpfangenOpperationUnwetterwarnung.empfangenAusfuehren(host, user, passwd, port);
    }

    private static void empfangenAusfuehren(String host, String user, String passwd, String port) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.pop3.host", host);
            props.setProperty("mail.pop3.user", user);
            props.setProperty("mail.pop3.password", passwd);
            props.setProperty("mail.pop3.port", port);
            if (runApplication.EINSTELLUNGEN.get("unwetterwarnungSSL").equals("1")) {
                props.put("mail.pop3.auth", "true");
            } else {
                props.put("mail.pop3.auth", "false");
            }
            props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            Session session = Session.getDefaultInstance((Properties)props);
            Store store = session.getStore("pop3");
            store.connect(host, user, passwd);
            Folder folder = store.getFolder("INBOX");
            folder.open(2);
            Message[] message = folder.getMessages();
            TabelleEMail_unwetterwarnung empfangen = new TabelleEMail_unwetterwarnung();
            Empfangen data = new Empfangen();
            logging.logInfo((Object)("Anzehl der EMails:" + message.length));
            int i = 0;
            while (i < message.length) {
                Message m = message[i];
                int newMailID = empfangen.getNextNummer();
                logging.logInfo((Object)("Akt. Nachricht: " + i + " DB-Nummer: " + newMailID));
                data.setId(newMailID);
                logging.logInfo((Object)("From: " + m.getFrom()[0]));
                data.setSender(m.getFrom()[0].toString());
                logging.logInfo((Object)("Send Date: " + m.getSentDate()));
                data.setDate(m.getSentDate().toString());
                logging.logInfo((Object)("Size: " + m.getSize()));
                data.setSize(m.getSize());
                logging.logInfo((Object)("Subject: " + m.getSubject()));
                data.setBetreff(m.getSubject());
                if (m.isMimeType("text/plain")) {
                    logging.logInfo((Object)"Nachrichttype --> text/plain");
                    data.setNachricht(m.getContent().toString());
                    data.setArt("plain");
                } else if (m.isMimeType("multipart/*")) {
                    logging.logInfo((Object)"Nachrichttype --> multipart/*");
                    Multipart mp = (Multipart)m.getContent();
                    if (mp.getCount() > 1) {
                        BodyPart part = mp.getBodyPart(0);
                        System.out.println(part.getContent());
                    }
                    int j = 0;
                    while (j < mp.getCount()) {
                        logging.logInfo((Object)mp.getCount());
                        logging.logInfo((Object)mp.getContentType());
                        BodyPart part = mp.getBodyPart(j);
                        String disposition = part.getDisposition();
                        if (disposition == null || disposition.equalsIgnoreCase("attachment")) {
                            String line;
                            StringBuilder nachrichtPlainMultipart;
                            String line2;
                            FileWriter writer;
                            File file;
                            StringBuilder nachrichtMultipart;
                            BufferedReader in;
                            MimeBodyPart mimePart = (MimeBodyPart)part;
                            if (mimePart.isMimeType("multipart/*")) {
                                logging.logInfo((Object)"--------------------------------------------------------- Amazon nachricht");
                                if (mimePart.isMimeType("text/html")) {
                                    logging.logInfo((Object)"Nachrichttype --> text/html");
                                    in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                                    nachrichtMultipart = new StringBuilder();
                                    file = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/temp/email" + newMailID + ".html");
                                    writer = new FileWriter(file, true);
                                    while ((line2 = in.readLine()) != null) {
                                        nachrichtMultipart.append(line2);
                                        nachrichtMultipart.append("\n");
                                        writer.write(line2);
                                        writer.write(System.getProperty("line.separator"));
                                    }
                                    writer.flush();
                                    writer.close();
                                    data.setArt("html");
                                    data.setNachricht("WeHaveAnHTMLFileForClient");
                                }
                                if (mimePart.isMimeType("text/plain")) {
                                    logging.logInfo((Object)"Nachrichttype --> text/plain");
                                    in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                                    nachrichtPlainMultipart = new StringBuilder();
                                    while ((line = in.readLine()) != null) {
                                        nachrichtPlainMultipart.append(line);
                                        nachrichtPlainMultipart.append("\n");
                                    }
                                    data.setNachricht(nachrichtPlainMultipart.toString());
                                }
                            }
                            if (mimePart.isMimeType("text/plain")) {
                                logging.logInfo((Object)"Nachrichttype --> text/plain");
                                in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                                nachrichtPlainMultipart = new StringBuilder();
                                while ((line = in.readLine()) != null) {
                                    nachrichtPlainMultipart.append(line);
                                    nachrichtPlainMultipart.append("\n");
                                }
                                data.setNachricht(nachrichtPlainMultipart.toString());
                            }
                            if (mimePart.isMimeType("text/html")) {
                                logging.logInfo((Object)"Nachrichttype --> text/html");
                                in = new BufferedReader(new InputStreamReader(mimePart.getInputStream()));
                                nachrichtMultipart = new StringBuilder();
                                file = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/temp/email" + newMailID + ".html");
                                writer = new FileWriter(file, true);
                                while ((line2 = in.readLine()) != null) {
                                    nachrichtMultipart.append(line2);
                                    nachrichtMultipart.append("\n");
                                    writer.write(line2);
                                    writer.write(System.getProperty("line.separator"));
                                }
                                writer.flush();
                                writer.close();
                                data.setArt("html");
                                data.setNachricht("WeHaveAnHTMLFileForClient");
                            }
                        }
                        EmpfangenOpperationUnwetterwarnung.receiveAttachment(data, newMailID, (Part)part, disposition);
                        ++j;
                    }
                } else {
                    logging.logInfo((Object)"Nachrichttype --> other");
                    data.setNachricht(m.getContent().toString());
                    data.setArt("other");
                }
                data.setGelesen(0);
                empfangen.insert(data);
                EmpfangenOpperationUnwetterwarnung.infomGUI(data);
                m.writeTo((OutputStream)new FileOutputStream(new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/temp/original_nachricht_unwetter/email" + newMailID + ".email")));
                Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/temp/original_nachricht_unwetter/email" + newMailID + ".email");
                m.setFlag(Flags.Flag.DELETED, true);
                ++i;
            }
            folder.close(true);
            store.close();
            logging.logInfo((Object)"Empfangen beendet");
        }
        catch (Exception e) {
            logging.logError((Object)e);
            e.printStackTrace();
        }
    }

    private static void infomGUI(Empfangen data) {
        logging.logInfo((Object)"Strate: informGUI()");
        logging.logInfo((Object)data.getBetreff());
        if (data.getBetreff().toString().startsWith("DWD ->")) {
            try {
                String[] warnungBis = null;
                int v = data.getNachricht().toString().indexOf("von:");
                int b = data.getNachricht().toString().indexOf("voraussichtlich bis:");
                warnungBis = data.getNachricht().toString().substring(b, b + (b - v + 1)).split("\\s");
                runApplication.unwetterwarnungDatumBis = warnungBis[3];
                runApplication.unwetterwarnungUhrzeitBis = warnungBis[4];
                if (runApplication.lastUnwetterwarnungID <= data.getId()) {
                    HauptprogrammAO.buttonUnwetterwarnung.setVisible(true);
                    HauptprogrammAO.buttonUnwetterwarnung.setToolTipText(data.getBetreff());
                    logging.logInfo((Object)("Unwetterwarnung Bis: " + runApplication.unwetterwarnungDatumBis + " / " + runApplication.unwetterwarnungUhrzeitBis));
                    logbuchEingabe.NeuerEintag("Unwetterwarnung: " + data.getBetreff());
                    logbuchEingabe.NeuerEintag("Unwetterwarnung Bis: " + runApplication.unwetterwarnungDatumBis + " / " + runApplication.unwetterwarnungUhrzeitBis);
                    runApplication.lastUnwetterwarnungID = data.getId();
                    runApplication.unwetterwarnungStatus = 1;
                    SystemTrayInfo trayInfo = new SystemTrayInfo();
                    trayInfo.InfoIconUnwetterwarnung("Unwetterwarnung: " + data.getBetreff());
                }
            }
            catch (Exception e) {
                logging.logWarning((Object)("Bei Lesen der DWD E-Mails ist ein Problem aufgeteten - " + e));
            }
        } else {
            logging.logWarning((Object)"Ich habe eine EMail \u00fcber die Unwetteradresse erhalten, dieser EMail fehlt der Unwetter (DWD ->) Pr\u00e4fix!!! --> Ignoriere Nachricht!!!");
        }
    }

    private static void receiveAttachment(Empfangen data, int newMailID, Part part, String disposition) throws MessagingException, FileNotFoundException, IOException, SQLException {
        if (disposition != null && (disposition.equals("attachment") || disposition.equals("inline"))) {
            Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/empfangende/" + newMailID, runApplication.clientID);
            File f = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/empfangende/" + newMailID + "/" + part.getFileName());
            FileOutputStream fos = new FileOutputStream(f);
            InputStream is = part.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            fos.close();
            is.close();
            data.setAnhang(1);
            logging.logInfo((Object)"Speichere Anhang");
            Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/empfangende/" + newMailID + "/" + part.getFileName());
        } else {
            data.setAnhang(0);
        }
    }
}

