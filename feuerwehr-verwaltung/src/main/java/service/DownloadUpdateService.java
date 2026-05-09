/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  org.apache.commons.net.ftp.FTPClient
 *  utilities.hash
 */
package service;

import ao.utils.ProzessBarAO;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JOptionPane;
import logging.logging;
import org.apache.commons.net.ftp.FTPClient;
import run.runApplication;
import utilities.Konstante;
import utilities.hash;

public class DownloadUpdateService {
    private static String server = "00067F0FsXm4BdoWZ5d77792mo\u00fch0hFMT4\u00c4TpJxdudpbdypvmnmfdvdnt\u00fcc\u00fctdv.gdRCej\u00c4\u00dcd\u00d6Fi8MaRGNkx\u00dcra3ddPhOLJZzwBAjRiY4S";
    private static int port = 21;
    private static String user = "00084lTWjB7HsPYjURg77761qK44INBoD3Cymuxta_xv\u00fc_gwbn\u00f6wmg@xdudpbdypvmnmfdvdnt\u00fcc\u00fctdv.gdtb5iyVuvaSvnT\u00fcqt\u00d6\u00dcGRkUie2HPNTOt3QCHLYTru";
    private static String pass = "00052NxOYQBDBgaldkn77780oG\u00e4UPimvPnA0iB\u00c41t\u00e41uy86sjnb\u00f6Oapa\u00c470vDMpObJ7Epxh9S85OsXSY1DSQimYsF9B";

    public static void getNewUpdateFormServer(String speicherOrt) {
        runApplication.verarbeitungL\u00e4uft = 1;
        FTPClient ftpClient = new FTPClient();
        try {
            try {
                ftpClient.connect(hash.decodeHashCode((String)hash.decodeHashCode((String)server)), port);
                ftpClient.login(hash.decodeHashCode((String)hash.decodeHashCode((String)user)), hash.decodeHashCode((String)hash.decodeHashCode((String)pass)));
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(2);
                String remoteFile1 = "Setup_FeuerwehrManagementSystem.exe";
                File downloadFile1 = new File(String.valueOf(speicherOrt) + "/Setup_FeuerwehrManagementSystem.exe");
                BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, (OutputStream)outputStream1);
                ((OutputStream)outputStream1).close();
                if (success) {
                    JOptionPane.showMessageDialog(null, Konstante.DOWNLOAD_ERFOLGREICH);
                    logging.logInfo((Object)"Datei: Setup_FeuerwehrManagementSystem.exe wurde erfolgreich heruntergeladen...");
                }
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_DOWNLOAD, "Fehlermeldung", 0);
                logging.logPrintStackTrace((Exception)ex);
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                    runApplication.verarbeitungL\u00e4uft = 0;
                    logging.logInfo((Object)"DOWNLOAD...setzte Bearbetungsstatus zur\u00fcck...");
                }
                catch (IOException ex2) {
                    logging.logPrintStackTrace((Exception)ex2);
                    runApplication.verarbeitungL\u00e4uft = 0;
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
                logging.logInfo((Object)"DOWNLOAD...setzte Bearbetungsstatus zur\u00fcck...");
            }
            catch (IOException ex) {
                logging.logPrintStackTrace((Exception)ex);
                runApplication.verarbeitungL\u00e4uft = 0;
            }
        }
    }

    public static void getJavaWebStartContendFormServer() {
        runApplication.verarbeitungL\u00e4uft = 1;
        FTPClient ftpClient = new FTPClient();
        try {
            try {
                ftpClient.connect(hash.decodeHashCode((String)hash.decodeHashCode((String)server)), port);
                ftpClient.login(hash.decodeHashCode((String)hash.decodeHashCode((String)user)), hash.decodeHashCode((String)hash.decodeHashCode((String)pass)));
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(2);
                new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung").mkdir();
                logging.logInfo((Object)"Ordner f\u00fcr die Bedinungsanleitung wurde angelegt...");
                new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Templates").mkdir();
                logging.logInfo((Object)"Ordner f\u00fcr die Templates wurde angelegt...");
                String remoteFile1 = "Bedienungsanleitung/Feuerwehr Management System - Bedienugsanleitung.pdf";
                File downloadFile1 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Bedienugsanleitung.pdf");
                BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, (OutputStream)outputStream1);
                ((OutputStream)outputStream1).close();
                if (success) {
                    ProzessBarAO.progressbar.setValue(7);
                    logging.logInfo((Object)("Datei: " + remoteFile1 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile2 = "Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf";
                File downloadFile2 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf");
                BufferedOutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
                boolean success2 = ftpClient.retrieveFile(remoteFile2, (OutputStream)outputStream2);
                ((OutputStream)outputStream2).close();
                if (success2) {
                    ProzessBarAO.progressbar.setValue(15);
                    logging.logInfo((Object)("Datei: " + remoteFile2 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile3 = "Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf";
                File downloadFile3 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf");
                BufferedOutputStream outputStream3 = new BufferedOutputStream(new FileOutputStream(downloadFile3));
                boolean success3 = ftpClient.retrieveFile(remoteFile3, (OutputStream)outputStream3);
                ((OutputStream)outputStream3).close();
                if (success3) {
                    ProzessBarAO.progressbar.setValue(23);
                    logging.logInfo((Object)("Datei: " + remoteFile3 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile4 = "Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf";
                File downloadFile4 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf");
                BufferedOutputStream outputStream4 = new BufferedOutputStream(new FileOutputStream(downloadFile4));
                boolean success4 = ftpClient.retrieveFile(remoteFile4, (OutputStream)outputStream4);
                ((OutputStream)outputStream4).close();
                if (success4) {
                    ProzessBarAO.progressbar.setValue(30);
                    logging.logInfo((Object)("Datei: " + remoteFile4 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile5 = "Bedienungsanleitung/Feuerwehr Management System - Tools.pdf";
                File downloadFile5 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/Feuerwehr Management System - Tools.pdf");
                BufferedOutputStream outputStream5 = new BufferedOutputStream(new FileOutputStream(downloadFile5));
                boolean success5 = ftpClient.retrieveFile(remoteFile5, (OutputStream)outputStream5);
                ((OutputStream)outputStream5).close();
                if (success5) {
                    ProzessBarAO.progressbar.setValue(38);
                    logging.logInfo((Object)("Datei: " + remoteFile5 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile6 = "Bedienungsanleitung/FeuerwehrManagementSystem - Datenkonzept.pdf";
                File downloadFile6 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/FeuerwehrManagementSystem - Datenkonzept.pdf");
                BufferedOutputStream outputStream6 = new BufferedOutputStream(new FileOutputStream(downloadFile6));
                boolean success6 = ftpClient.retrieveFile(remoteFile6, (OutputStream)outputStream6);
                ((OutputStream)outputStream6).close();
                if (success6) {
                    ProzessBarAO.progressbar.setValue(46);
                    logging.logInfo((Object)("Datei: " + remoteFile6 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile7 = "Templates/briefkopf.jpg";
                File downloadFile7 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Templates/briefkopf.jpg");
                BufferedOutputStream outputStream7 = new BufferedOutputStream(new FileOutputStream(downloadFile7));
                boolean success7 = ftpClient.retrieveFile(remoteFile7, (OutputStream)outputStream7);
                ((OutputStream)outputStream7).close();
                if (success7) {
                    ProzessBarAO.progressbar.setValue(53);
                    logging.logInfo((Object)("Datei: " + remoteFile7 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile8 = "Templates/Einsatzbericht.xml";
                File downloadFile8 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Templates/Einsatzbericht.xml");
                BufferedOutputStream outputStream8 = new BufferedOutputStream(new FileOutputStream(downloadFile8));
                boolean success8 = ftpClient.retrieveFile(remoteFile8, (OutputStream)outputStream8);
                ((OutputStream)outputStream8).close();
                if (success8) {
                    ProzessBarAO.progressbar.setValue(61);
                    logging.logInfo((Object)("Datei: " + remoteFile8 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile9 = "Templates/M\u00e4ngelmeldung.xml";
                File downloadFile9 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Templates/M\u00e4ngelmeldung.xml");
                BufferedOutputStream outputStream9 = new BufferedOutputStream(new FileOutputStream(downloadFile9));
                boolean success9 = ftpClient.retrieveFile(remoteFile9, (OutputStream)outputStream9);
                ((OutputStream)outputStream9).close();
                if (success9) {
                    ProzessBarAO.progressbar.setValue(69);
                    logging.logInfo((Object)("Datei: " + remoteFile9 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile10 = "Templates/Verdienstausfallbescheinigung.xml";
                File downloadFile10 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Templates/Verdienstausfallbescheinigung.xml");
                BufferedOutputStream outputStream10 = new BufferedOutputStream(new FileOutputStream(downloadFile10));
                boolean success10 = ftpClient.retrieveFile(remoteFile10, (OutputStream)outputStream10);
                ((OutputStream)outputStream10).close();
                if (success10) {
                    ProzessBarAO.progressbar.setValue(76);
                    logging.logInfo((Object)("Datei: " + remoteFile10 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile11 = "Templates/BescheinigungEinsatzTeilnahme.xml";
                File downloadFile11 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Templates/BescheinigungEinsatzTeilnahme.xml");
                BufferedOutputStream outputStream11 = new BufferedOutputStream(new FileOutputStream(downloadFile11));
                boolean success11 = ftpClient.retrieveFile(remoteFile11, (OutputStream)outputStream11);
                ((OutputStream)outputStream10).close();
                if (success11) {
                    ProzessBarAO.progressbar.setValue(84);
                    logging.logInfo((Object)("Datei: " + remoteFile11 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile12 = "Bedienungsanleitung/homepage.html";
                File downloadFile12 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/homepage.html");
                BufferedOutputStream outputStream12 = new BufferedOutputStream(new FileOutputStream(downloadFile12));
                boolean success12 = ftpClient.retrieveFile(remoteFile12, (OutputStream)outputStream12);
                ((OutputStream)outputStream12).close();
                if (success12) {
                    ProzessBarAO.progressbar.setValue(92);
                    logging.logInfo((Object)("Datei: " + remoteFile12 + " wurde erfolgreich heruntergeladen..."));
                }
                String remoteFile13 = "Bedienungsanleitung/aktuelleSoftware\u00e4nderungen.html";
                File downloadFile13 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bedienungsanleitung/aktuelleSoftware\u00e4nderungen.html");
                BufferedOutputStream outputStream13 = new BufferedOutputStream(new FileOutputStream(downloadFile13));
                boolean success13 = ftpClient.retrieveFile(remoteFile13, (OutputStream)outputStream13);
                ((OutputStream)outputStream13).close();
                if (success13) {
                    ProzessBarAO.progressbar.setValue(100);
                    logging.logInfo((Object)("Datei: " + remoteFile13 + " wurde erfolgreich heruntergeladen..."));
                }
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_DOWNLOAD, "Fehlermeldung", 0);
                logging.logPrintStackTrace((Exception)ex);
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                    runApplication.verarbeitungL\u00e4uft = 0;
                    logging.logInfo((Object)"DOWNLOAD...setzte Bearbetungsstatus zur\u00fcck...");
                }
                catch (IOException ex2) {
                    logging.logPrintStackTrace((Exception)ex2);
                    runApplication.verarbeitungL\u00e4uft = 0;
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
                logging.logInfo((Object)"DOWNLOAD...setzte Bearbetungsstatus zur\u00fcck...");
            }
            catch (IOException ex) {
                logging.logPrintStackTrace((Exception)ex);
                runApplication.verarbeitungL\u00e4uft = 0;
            }
        }
    }

    public static void getVersionInfoFromServer() {
        FTPClient ftpClient = new FTPClient();
        try {
            try {
                ftpClient.connect(hash.decodeHashCode((String)hash.decodeHashCode((String)server)), port);
                ftpClient.login(hash.decodeHashCode((String)hash.decodeHashCode((String)user)), hash.decodeHashCode((String)hash.decodeHashCode((String)pass)));
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(2);
                String remoteFile1 = "versioninfo.txt";
                File downloadFile1 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/versioninfo.txt");
                BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, (OutputStream)outputStream1);
                ((OutputStream)outputStream1).close();
                if (success) {
                    logging.logInfo((Object)"Datei: versioninfo.txt wurde erfolgreich heruntergeladen...");
                }
            }
            catch (IOException ex) {
                logging.logWarning((Object)"Die Verbindung mit dem Internet ist nicht m\u00f6glich um auf event. Updates zu pr\u00fcfen");
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                }
                catch (IOException ex2) {
                    logging.logPrintStackTrace((Exception)ex2);
                }
            }
        }
        finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            }
            catch (IOException ex) {
                logging.logPrintStackTrace((Exception)ex);
            }
        }
    }

    public static void getNachricht() {
        FTPClient ftpClient = new FTPClient();
        try {
            try {
                ftpClient.connect(hash.decodeHashCode((String)hash.decodeHashCode((String)server)), port);
                ftpClient.login(hash.decodeHashCode((String)hash.decodeHashCode((String)user)), hash.decodeHashCode((String)hash.decodeHashCode((String)pass)));
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(2);
                String remoteFile1 = "info.txt";
                File downloadFile1 = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/info.txt");
                BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, (OutputStream)outputStream1);
                ((OutputStream)outputStream1).close();
                if (success) {
                    logging.logInfo((Object)"Datei: info.txt wurde erfolgreich heruntergeladen...");
                }
            }
            catch (IOException ex) {
                logging.logWarning((Object)"Die Verbindung mit dem Internet ist nicht m\u00f6glich um auf event. Nachrichten zu pr\u00fcfen");
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                }
                catch (IOException ex2) {
                    logging.logPrintStackTrace((Exception)ex2);
                }
            }
        }
        finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            }
            catch (IOException ex) {
                logging.logPrintStackTrace((Exception)ex);
            }
        }
    }
}

