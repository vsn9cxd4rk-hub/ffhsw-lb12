/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  org.apache.commons.net.ftp.FTPClient
 *  utilities.MyProperties
 *  utilities.hash
 */
package service;

import ao.utils.ProzessBarAO;
import ao.utils.SystemTrayInfo;
import data.tabellen.einstellungen.TabelleFTPSync;
import go.FTPSync;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import logging.logging;
import org.apache.commons.net.ftp.FTPClient;
import run.runApplication;
import utilities.Konstante;
import utilities.MyProperties;
import utilities.Utils;
import utilities.hash;

public class FTPFileTransferservice {
    public static void uploadService() {
        Thread threadUpload = new Thread(){

            @Override
            public void run() {
                runApplication.ftpUploadL\u00e4uft = 1;
                logging.logInfo((Object)"Daten werden Syncronisiert (UPLOAD)");
                TabelleFTPSync tabFTPsync = new TabelleFTPSync();
                try {
                    FTPSync sync;
                    int anzahl = tabFTPsync.getCountOfNotUploaded("SYSTEM") + tabFTPsync.getCountOfNotUploaded(runApplication.clientID);
                    int aktPosition = 1;
                    if (tabFTPsync.getCountOfNotUploaded("SYSTEM") != 0) {
                        String[] ordnerListeSystem = Utils.listToArray(tabFTPsync.getOrdnerForUpload("SYSTEM"));
                        String[] dateiListeSystem = Utils.listToArray(tabFTPsync.getDateiForUpload("SYSTEM"));
                        int o = 0;
                        while (o < ordnerListeSystem.length) {
                            logging.logInfo((Object)(String.valueOf(ordnerListeSystem[o]) + " --> FTP-Server (SYSTEM Ordner)"));
                            FTPFileTransferservice.createFolderOnServer(ordnerListeSystem[o]);
                            tabFTPsync.updateOrdnerNachUpload(ordnerListeSystem[o], "SYSTEM");
                            ProzessBarAO.progressbar.setValue(100 * aktPosition / anzahl);
                            ++aktPosition;
                            ++o;
                        }
                        logging.logInfo((Object)"Ordner syncronisation abgeschlossen --> FTP (SYSTEM)");
                        int d = 0;
                        while (d < dateiListeSystem.length) {
                            logging.logInfo((Object)(String.valueOf(dateiListeSystem[d]) + " --> FTP-Server (SYSTEM Datei)"));
                            FTPFileTransferservice.uploadToServer(dateiListeSystem[d]);
                            tabFTPsync.updateDateiNachUpload(dateiListeSystem[d], "SYSTEM", new File(dateiListeSystem[d]).length());
                            ProzessBarAO.progressbar.setValue(100 * aktPosition / anzahl);
                            ++aktPosition;
                            ++d;
                        }
                        logging.logInfo((Object)"Datei syncronisation abgeschlossen --> FTP (SYSTEM)");
                    }
                    String[] dateiListeF\u00fcrL\u00f6schen = Utils.listToArray(tabFTPsync.getDateiForDeleteOnServer(runApplication.clientID));
                    String[] ordnerListe = Utils.listToArray(tabFTPsync.getOrdnerForUpload(runApplication.clientID));
                    String[] dateiListe = Utils.listToArray(tabFTPsync.getDateiForUpload(runApplication.clientID));
                    logging.logInfo((Object)"### FTP-UPLOAD TASKs ###");
                    logging.logInfo((Object)("FTP Upload --> dateiListeF\u00fcrL\u00f6schen[] == " + dateiListeF\u00fcrL\u00f6schen.length));
                    logging.logInfo((Object)("FTP Upload --> ordnerListe[] == " + ordnerListe.length));
                    logging.logInfo((Object)("FTP Upload --> dateiListe[] == " + dateiListe.length));
                    int l = 0;
                    while (l < dateiListeF\u00fcrL\u00f6schen.length) {
                        logging.logInfo((Object)"Starte L\u00f6schen der Dateien auf dem Server...");
                        FTPFileTransferservice.deleteFromServer(dateiListeF\u00fcrL\u00f6schen[l]);
                        tabFTPsync.updateDateiNachDelete(dateiListeF\u00fcrL\u00f6schen[l], runApplication.clientID);
                        logging.logInfo((Object)(String.valueOf(dateiListeF\u00fcrL\u00f6schen[l]) + " --> L\u00f6sche Datei vom FTP-Server"));
                        ++l;
                    }
                    int o = 0;
                    while (o < ordnerListe.length) {
                        if (!new File(ordnerListe[o]).exists()) {
                            logging.logWarning((Object)("Der Ordner (" + ordnerListe[o] + ") zum Upload kann nicht gefunden werden..."));
                            sync = new FTPSync();
                            sync.setDatei("");
                            sync.setOrdner(ordnerListe[o]);
                            sync.setClientID(runApplication.clientID);
                            tabFTPsync.insertFTPSync_Error(sync);
                            tabFTPsync.deleteOneFolder(ordnerListe[o]);
                            logging.logWarning((Object)"Ordner wurde in ftpsync_error verschoben...");
                        } else {
                            logging.logInfo((Object)(String.valueOf(ordnerListe[o]) + " --> FTP-Server (Ordner)"));
                            FTPFileTransferservice.createFolderOnServer(ordnerListe[o]);
                            tabFTPsync.updateOrdnerNachUpload(ordnerListe[o], runApplication.clientID);
                        }
                        ProzessBarAO.progressbar.setValue(100 * aktPosition / anzahl);
                        ++aktPosition;
                        ++o;
                    }
                    logging.logInfo((Object)"Ordner syncronisation abgeschlossen --> FTP");
                    int d = 0;
                    while (d < dateiListe.length) {
                        if (!new File(dateiListe[d]).exists()) {
                            logging.logWarning((Object)("Die Datei (" + dateiListe[d] + ") zum Upload kann nicht gefunden werden..."));
                            sync = new FTPSync();
                            sync.setDatei(dateiListe[d]);
                            sync.setOrdner("");
                            sync.setClientID(runApplication.clientID);
                            tabFTPsync.insertFTPSync_Error(sync);
                            tabFTPsync.deleteOneFile(dateiListe[d]);
                            logging.logWarning((Object)"Datei wurde in ftpsync_error verschoben...");
                        } else {
                            logging.logInfo((Object)(String.valueOf(dateiListe[d]) + " --> FTP-Server (Datei)"));
                            FTPFileTransferservice.uploadToServer(dateiListe[d]);
                            tabFTPsync.updateDateiNachUpload(dateiListe[d], runApplication.clientID, new File(dateiListe[d]).length());
                        }
                        ProzessBarAO.progressbar.setValue(100 * aktPosition / anzahl);
                        ++aktPosition;
                        ++d;
                    }
                    logging.logInfo((Object)"Datei syncronisation abgeschlossen --> FTP (UPLOAD)");
                    ProzessBarAO.progressbar.setValue(100);
                    ProzessBarAO.label_bitteWarten.setText("FTP Upload abgeschlossen... Beenden wird vorbereitet...");
                    runApplication.ftpUploadL\u00e4uft = 0;
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                    runApplication.ftpUploadL\u00e4uft = 0;
                }
            }
        };
        threadUpload.start();
    }

    public static void downloadService() {
        Thread threadDownThread = new Thread(){

            @Override
            public void run() {
                SystemTrayInfo trayInfo = new SystemTrayInfo();
                try {
                    runApplication.ftpDownloadL\u00e4uft = 1;
                    trayInfo.InfoIconFTP("Der Download l\u00e4uft");
                    logging.logInfo((Object)"Daten werden Syncronisiert (DOWNLOAD)");
                    TabelleFTPSync tabFTPsync = new TabelleFTPSync();
                    String[] dateiListeF\u00fcrL\u00f6schen = Utils.listToArray(tabFTPsync.getDateiForDeleteFileSystem());
                    String[] downloadOrdner = Utils.listToArray(tabFTPsync.getAllOrdner());
                    String[] downloadDatei = Utils.listToArray(tabFTPsync.getAllDatei());
                    long[] downloadDateiGroesse = Utils.listToLongArray(tabFTPsync.getAllDateiGroe\u00dfe());
                    logging.logInfo((Object)"### FTP-DOWNLOAD TASKs ###");
                    logging.logInfo((Object)("FTP Download --> dateiListeF\u00fcrL\u00f6schen[] == " + dateiListeF\u00fcrL\u00f6schen.length));
                    logging.logInfo((Object)("FTP Download --> downloadOrdner[] == " + downloadOrdner.length));
                    logging.logInfo((Object)("FTP Download --> downloadDatei[] == " + downloadDatei.length));
                    int l = 0;
                    while (l < dateiListeF\u00fcrL\u00f6schen.length) {
                        File file = new File(dateiListeF\u00fcrL\u00f6schen[l]);
                        if (file.exists()) {
                            file.delete();
                            logging.logInfo((Object)(String.valueOf(dateiListeF\u00fcrL\u00f6schen[l]) + " --> L\u00f6sche --> FTP-Server abgleich"));
                        }
                        ++l;
                    }
                    int o = 0;
                    while (o < downloadOrdner.length) {
                        File ordner = new File(downloadOrdner[o]);
                        if (!ordner.exists()) {
                            ordner.mkdir();
                            logging.logInfo((Object)(String.valueOf(downloadOrdner[o]) + " Erstellt --> FTP (Download)"));
                        }
                        ++o;
                    }
                    logging.logInfo((Object)"Ordner syncronisation abgeschlossen --> FTP (Download)");
                    int d = 0;
                    while (d < downloadDatei.length) {
                        File datei = new File(downloadDatei[d]);
                        if (!datei.exists() | datei.length() != downloadDateiGroesse[d]) {
                            int positionArbeitsverzeichnis = downloadDatei[d].indexOf("data");
                            String dateiNameOnServer = downloadDatei[d].substring(positionArbeitsverzeichnis, downloadDatei[d].length());
                            logging.logInfo((Object)(String.valueOf(downloadDatei[d]) + " heruntergeladen --> FTP (Download)"));
                            FTPFileTransferservice.downloadFromServer(downloadDatei[d], dateiNameOnServer);
                        }
                        ++d;
                    }
                    logging.logInfo((Object)"Datei syncronisation abgeschlossen --> FTP (Download)");
                    logging.logInfo((Object)"Datei syncronisation abgeschlossen, alle Dateinen sind auf dem neusten stand...");
                    runApplication.ftpDownloadL\u00e4uft = 0;
                    trayInfo.removeInfoIcon();
                }
                catch (Exception e) {
                    trayInfo.removeInfoIcon();
                    runApplication.ftpDownloadL\u00e4uft = 0;
                    logging.logInfo((Object)"Fehler beim FTP Download");
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadDownThread.start();
    }

    private static void downloadFromServer(String speicherOrt, String dateiName) {
        String server = runApplication.PROPERTIES.get("FTPServer");
        int port = Integer.parseInt(runApplication.PROPERTIES.get("FTPPort"));
        String user = hash.decodeHashCode((String)runApplication.PROPERTIES.get("FTPUser"));
        String pass = hash.decodeHashCode((String)runApplication.PROPERTIES.get("FTPPasswort"));
        runApplication.verarbeitungL\u00e4uft = 1;
        FTPClient ftpClient = new FTPClient();
        try {
            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, pass);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(2);
                String remoteFile1 = "/" + dateiName;
                File downloadFile1 = new File(speicherOrt);
                BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, (OutputStream)outputStream1);
                ((OutputStream)outputStream1).close();
                if (success) {
                    logging.logInfo((Object)"Datei wurde erfolgreich heruntergeladen...");
                }
            }
            catch (FileNotFoundException ex) {
                logging.logError((Object)("FileNotFoundException: " + dateiName + "(Das System kann den angegebenen Pfad nicht finden)"));
                try {
                    logging.logInfo((Object)("Verschiebe Datei: " + dateiName + " in ftpsync_error"));
                    FTPSync sync = new FTPSync();
                    TabelleFTPSync tabSync = new TabelleFTPSync();
                    sync.setClientID(tabSync.getClientIDFromUploader(dateiName));
                    sync.setDatei(dateiName);
                    sync.setOrdner("");
                    tabSync.insertFTPSync_Error(sync);
                    tabSync.deleteOneFile(dateiName);
                    logging.logInfo((Object)("Verschieben / L\u00f6schen von " + dateiName + " erfolgreich in ftpsync / ftpsync_error..."));
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                    runApplication.verarbeitungL\u00e4uft = 0;
                }
                catch (IOException ex2) {
                    logging.logPrintStackTrace((Exception)ex2);
                }
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_DOWNLOAD, "Fehlermeldung", 0);
                logging.logPrintStackTrace((Exception)e);
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                    runApplication.verarbeitungL\u00e4uft = 0;
                }
                catch (IOException ex) {
                    logging.logPrintStackTrace((Exception)ex);
                }
            }
        }
        finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
                runApplication.verarbeitungL\u00e4uft = 0;
            }
            catch (IOException ex) {
                logging.logPrintStackTrace((Exception)ex);
            }
        }
    }

    private static void uploadToServer(String dateiName) {
        MyProperties einstellungenholen = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties");
        einstellungenholen.loadVars();
        FTPClient client = new FTPClient();
        FileInputStream fis = null;
        try {
            int positionArbeitsverzeichnis = dateiName.indexOf("data");
            String dateiNameOnServer = dateiName.substring(positionArbeitsverzeichnis, dateiName.length());
            client.connect((String)einstellungenholen.getVar("FTPServer"));
            client.login(hash.decodeHashCode((String)((String)einstellungenholen.getVar("FTPUser"))), hash.decodeHashCode((String)((String)einstellungenholen.getVar("FTPPasswort"))));
            fis = new FileInputStream(dateiName);
            client.setFileType(2, 2);
            client.setFileTransferMode(2);
            client.storeFile(dateiNameOnServer, (InputStream)fis);
            fis.close();
            client.logout();
            client.disconnect();
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private static void deleteFromServer(String dateiName) {
        FTPClient client = new FTPClient();
        try {
            int positionArbeitsverzeichnis = dateiName.indexOf("data");
            String dateiNameOnServer = dateiName.substring(positionArbeitsverzeichnis, dateiName.length());
            System.out.println(dateiName.substring(positionArbeitsverzeichnis, dateiName.length()));
            client.connect(runApplication.PROPERTIES.get("FTPServer"));
            client.login(hash.decodeHashCode((String)runApplication.PROPERTIES.get("FTPUser")), hash.decodeHashCode((String)runApplication.PROPERTIES.get("FTPPasswort")));
            client.deleteFile(dateiNameOnServer);
            client.logout();
            client.disconnect();
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private static void createFolderOnServer(String ordnerName) {
        FTPClient client = new FTPClient();
        try {
            int positionArbeitsverzeichnis = ordnerName.indexOf("data");
            client.connect(runApplication.PROPERTIES.get("FTPServer"));
            client.login(hash.decodeHashCode((String)runApplication.PROPERTIES.get("FTPUser")), hash.decodeHashCode((String)runApplication.PROPERTIES.get("FTPPasswort")));
            client.makeDirectory(ordnerName.substring(positionArbeitsverzeichnis, ordnerName.length()));
            client.logout();
            client.disconnect();
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

