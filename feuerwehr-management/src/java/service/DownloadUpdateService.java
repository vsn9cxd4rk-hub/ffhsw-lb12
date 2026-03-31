package service;

import ao.utils.ProzessBarAO;
import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import logging.logging;
import org.apache.commons.net.ftp.FTPClient;
import run.runApplication;
import utilities.Konstante;
import utilities.hash;

public class DownloadUpdateService {

   private static String server = "00067F0FsXm4BdoWZ5d77792moüh0hFMT4ÄTpJxdudpbdypvmnmfdvdntücütdv.gdRCejÄÜdÖFi8MaRGNkxÜra3ddPhOLJZzwBAjRiY4S";
   private static int port = 21;
   private static String user = "00084lTWjB7HsPYjURg77761qK44INBoD3Cymuxta_xvü_gwbnöwmg@xdudpbdypvmnmfdvdntücütdv.gdtb5iyVuvaSvnTüqtÖÜGRkUie2HPNTOt3QCHLYTru";
   private static String pass = "00052NxOYQBDBgaldkn77780oGäUPimvPnA0iBÄ1tä1uy86sjnböOapaÄ70vDMpObJ7Epxh9S85OsXSY1DSQimYsF9B";


   public static void getNewUpdateFormServer(String speicherOrt) {
      runApplication.verarbeitungLäuft = 1;
      FTPClient ftpClient = new FTPClient();

      try {
         ftpClient.connect(hash.decodeHashCode(hash.decodeHashCode(server)), port);
         ftpClient.login(hash.decodeHashCode(hash.decodeHashCode(user)), hash.decodeHashCode(hash.decodeHashCode(pass)));
         ftpClient.enterLocalPassiveMode();
         ftpClient.setFileType(2);
         String ex = "Setup_FeuerwehrManagementSystem.exe";
         File downloadFile1 = new File(speicherOrt + "/Setup_FeuerwehrManagementSystem.exe");
         BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
         boolean success = ftpClient.retrieveFile(ex, outputStream1);
         outputStream1.close();
         if(success) {
            JOptionPane.showMessageDialog((Component)null, Konstante.DOWNLOAD_ERFOLGREICH);
            logging.logInfo("Datei: Setup_FeuerwehrManagementSystem.exe wurde erfolgreich heruntergeladen...");
         }
      } catch (IOException var14) {
         JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_DOWNLOAD, "Fehlermeldung", 0);
         logging.logPrintStackTrace(var14);
      } finally {
         try {
            if(ftpClient.isConnected()) {
               ftpClient.logout();
               ftpClient.disconnect();
            }

            runApplication.verarbeitungLäuft = 0;
            logging.logInfo("DOWNLOAD...setzte Bearbetungsstatus zurück...");
         } catch (IOException var13) {
            logging.logPrintStackTrace(var13);
            runApplication.verarbeitungLäuft = 0;
         }

      }

   }

   public static void getJavaWebStartContendFormServer() {
      runApplication.verarbeitungLäuft = 1;
      FTPClient ftpClient = new FTPClient();

      try {
         ftpClient.connect(hash.decodeHashCode(hash.decodeHashCode(server)), port);
         ftpClient.login(hash.decodeHashCode(hash.decodeHashCode(user)), hash.decodeHashCode(hash.decodeHashCode(pass)));
         ftpClient.enterLocalPassiveMode();
         ftpClient.setFileType(2);
         (new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung")).mkdir();
         logging.logInfo("Ordner für die Bedinungsanleitung wurde angelegt...");
         (new File(runApplication.arbeitsverzeichnis + "data/Templates")).mkdir();
         logging.logInfo("Ordner für die Templates wurde angelegt...");
         String ex = "Bedienungsanleitung/Feuerwehr Management System - Bedienugsanleitung.pdf";
         File downloadFile1 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Bedienugsanleitung.pdf");
         BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
         boolean success = ftpClient.retrieveFile(ex, outputStream1);
         outputStream1.close();
         if(success) {
            ProzessBarAO.progressbar.setValue(7);
            logging.logInfo("Datei: " + ex + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile2 = "Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf";
         File downloadFile2 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Ausbildungsplan.pdf");
         BufferedOutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
         boolean success2 = ftpClient.retrieveFile(remoteFile2, outputStream2);
         outputStream2.close();
         if(success2) {
            ProzessBarAO.progressbar.setValue(15);
            logging.logInfo("Datei: " + remoteFile2 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile3 = "Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf";
         File downloadFile3 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Einsatzkomponente.pdf");
         BufferedOutputStream outputStream3 = new BufferedOutputStream(new FileOutputStream(downloadFile3));
         boolean success3 = ftpClient.retrieveFile(remoteFile3, outputStream3);
         outputStream3.close();
         if(success3) {
            ProzessBarAO.progressbar.setValue(23);
            logging.logInfo("Datei: " + remoteFile3 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile4 = "Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf";
         File downloadFile4 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Schnittstellenbeschreibung Veranstaltungen.pdf");
         BufferedOutputStream outputStream4 = new BufferedOutputStream(new FileOutputStream(downloadFile4));
         boolean success4 = ftpClient.retrieveFile(remoteFile4, outputStream4);
         outputStream4.close();
         if(success4) {
            ProzessBarAO.progressbar.setValue(30);
            logging.logInfo("Datei: " + remoteFile4 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile5 = "Bedienungsanleitung/Feuerwehr Management System - Tools.pdf";
         File downloadFile5 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/Feuerwehr Management System - Tools.pdf");
         BufferedOutputStream outputStream5 = new BufferedOutputStream(new FileOutputStream(downloadFile5));
         boolean success5 = ftpClient.retrieveFile(remoteFile5, outputStream5);
         outputStream5.close();
         if(success5) {
            ProzessBarAO.progressbar.setValue(38);
            logging.logInfo("Datei: " + remoteFile5 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile6 = "Bedienungsanleitung/FeuerwehrManagementSystem - Datenkonzept.pdf";
         File downloadFile6 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/FeuerwehrManagementSystem - Datenkonzept.pdf");
         BufferedOutputStream outputStream6 = new BufferedOutputStream(new FileOutputStream(downloadFile6));
         boolean success6 = ftpClient.retrieveFile(remoteFile6, outputStream6);
         outputStream6.close();
         if(success6) {
            ProzessBarAO.progressbar.setValue(46);
            logging.logInfo("Datei: " + remoteFile6 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile7 = "Templates/briefkopf.jpg";
         File downloadFile7 = new File(runApplication.arbeitsverzeichnis + "data/Templates/briefkopf.jpg");
         BufferedOutputStream outputStream7 = new BufferedOutputStream(new FileOutputStream(downloadFile7));
         boolean success7 = ftpClient.retrieveFile(remoteFile7, outputStream7);
         outputStream7.close();
         if(success7) {
            ProzessBarAO.progressbar.setValue(53);
            logging.logInfo("Datei: " + remoteFile7 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile8 = "Templates/Einsatzbericht.xml";
         File downloadFile8 = new File(runApplication.arbeitsverzeichnis + "data/Templates/Einsatzbericht.xml");
         BufferedOutputStream outputStream8 = new BufferedOutputStream(new FileOutputStream(downloadFile8));
         boolean success8 = ftpClient.retrieveFile(remoteFile8, outputStream8);
         outputStream8.close();
         if(success8) {
            ProzessBarAO.progressbar.setValue(61);
            logging.logInfo("Datei: " + remoteFile8 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile9 = "Templates/Mängelmeldung.xml";
         File downloadFile9 = new File(runApplication.arbeitsverzeichnis + "data/Templates/Mängelmeldung.xml");
         BufferedOutputStream outputStream9 = new BufferedOutputStream(new FileOutputStream(downloadFile9));
         boolean success9 = ftpClient.retrieveFile(remoteFile9, outputStream9);
         outputStream9.close();
         if(success9) {
            ProzessBarAO.progressbar.setValue(69);
            logging.logInfo("Datei: " + remoteFile9 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile10 = "Templates/Verdienstausfallbescheinigung.xml";
         File downloadFile10 = new File(runApplication.arbeitsverzeichnis + "data/Templates/Verdienstausfallbescheinigung.xml");
         BufferedOutputStream outputStream10 = new BufferedOutputStream(new FileOutputStream(downloadFile10));
         boolean success10 = ftpClient.retrieveFile(remoteFile10, outputStream10);
         outputStream10.close();
         if(success10) {
            ProzessBarAO.progressbar.setValue(76);
            logging.logInfo("Datei: " + remoteFile10 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile11 = "Templates/BescheinigungEinsatzTeilnahme.xml";
         File downloadFile11 = new File(runApplication.arbeitsverzeichnis + "data/Templates/BescheinigungEinsatzTeilnahme.xml");
         BufferedOutputStream outputStream11 = new BufferedOutputStream(new FileOutputStream(downloadFile11));
         boolean success11 = ftpClient.retrieveFile(remoteFile11, outputStream11);
         outputStream10.close();
         if(success11) {
            ProzessBarAO.progressbar.setValue(84);
            logging.logInfo("Datei: " + remoteFile11 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile12 = "Bedienungsanleitung/homepage.html";
         File downloadFile12 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/homepage.html");
         BufferedOutputStream outputStream12 = new BufferedOutputStream(new FileOutputStream(downloadFile12));
         boolean success12 = ftpClient.retrieveFile(remoteFile12, outputStream12);
         outputStream12.close();
         if(success12) {
            ProzessBarAO.progressbar.setValue(92);
            logging.logInfo("Datei: " + remoteFile12 + " wurde erfolgreich heruntergeladen...");
         }

         String remoteFile13 = "Bedienungsanleitung/aktuelleSoftwareänderungen.html";
         File downloadFile13 = new File(runApplication.arbeitsverzeichnis + "data/Bedienungsanleitung/aktuelleSoftwareänderungen.html");
         BufferedOutputStream outputStream13 = new BufferedOutputStream(new FileOutputStream(downloadFile13));
         boolean success13 = ftpClient.retrieveFile(remoteFile13, outputStream13);
         outputStream13.close();
         if(success13) {
            ProzessBarAO.progressbar.setValue(100);
            logging.logInfo("Datei: " + remoteFile13 + " wurde erfolgreich heruntergeladen...");
         }
      } catch (IOException var61) {
         JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_DOWNLOAD, "Fehlermeldung", 0);
         logging.logPrintStackTrace(var61);
      } finally {
         try {
            if(ftpClient.isConnected()) {
               ftpClient.logout();
               ftpClient.disconnect();
            }

            runApplication.verarbeitungLäuft = 0;
            logging.logInfo("DOWNLOAD...setzte Bearbetungsstatus zurück...");
         } catch (IOException var60) {
            logging.logPrintStackTrace(var60);
            runApplication.verarbeitungLäuft = 0;
         }

      }

   }

   public static void getVersionInfoFromServer() {
      FTPClient ftpClient = new FTPClient();

      try {
         ftpClient.connect(hash.decodeHashCode(hash.decodeHashCode(server)), port);
         ftpClient.login(hash.decodeHashCode(hash.decodeHashCode(user)), hash.decodeHashCode(hash.decodeHashCode(pass)));
         ftpClient.enterLocalPassiveMode();
         ftpClient.setFileType(2);
         String ex = "versioninfo.txt";
         File downloadFile1 = new File(runApplication.arbeitsverzeichnis + "data/versioninfo.txt");
         BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
         boolean success = ftpClient.retrieveFile(ex, outputStream1);
         outputStream1.close();
         if(success) {
            logging.logInfo("Datei: versioninfo.txt wurde erfolgreich heruntergeladen...");
         }
      } catch (IOException var13) {
         logging.logWarning("Die Verbindung mit dem Internet ist nicht möglich um auf event. Updates zu prüfen");
      } finally {
         try {
            if(ftpClient.isConnected()) {
               ftpClient.logout();
               ftpClient.disconnect();
            }
         } catch (IOException var12) {
            logging.logPrintStackTrace(var12);
         }

      }

   }

   public static void getNachricht() {
      FTPClient ftpClient = new FTPClient();

      try {
         ftpClient.connect(hash.decodeHashCode(hash.decodeHashCode(server)), port);
         ftpClient.login(hash.decodeHashCode(hash.decodeHashCode(user)), hash.decodeHashCode(hash.decodeHashCode(pass)));
         ftpClient.enterLocalPassiveMode();
         ftpClient.setFileType(2);
         String ex = "info.txt";
         File downloadFile1 = new File(runApplication.arbeitsverzeichnis + "data/info.txt");
         BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
         boolean success = ftpClient.retrieveFile(ex, outputStream1);
         outputStream1.close();
         if(success) {
            logging.logInfo("Datei: info.txt wurde erfolgreich heruntergeladen...");
         }
      } catch (IOException var13) {
         logging.logWarning("Die Verbindung mit dem Internet ist nicht möglich um auf event. Nachrichten zu prüfen");
      } finally {
         try {
            if(ftpClient.isConnected()) {
               ftpClient.logout();
               ftpClient.disconnect();
            }
         } catch (IOException var12) {
            logging.logPrintStackTrace(var12);
         }

      }

   }
}
