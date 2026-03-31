package service;

import ao.utils.ProzessBarAO;
import com.mysql.jdbc.PacketTooBigException;
import data.tabellen.TabelleDateisystem;
import data.tabellen.einstellungen.TabelleFTPSync;
import go.Dateisystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.Utils;

public class DatabaseFileTransferService {

   public static void uploadService() {
      runApplication.dbUploadLäuft = 1;
      Thread threadUpload = new Thread() {
         public void run() {
            try {
               TabelleFTPSync e = new TabelleFTPSync();
               TabelleDateisystem tabDateisystem = new TabelleDateisystem();
               Dateisystem dateisystem = new Dateisystem();
               String[] dateiListeFürLöschen = Utils.listToArray(e.getDateiForDeleteOnServerDB(runApplication.clientID));
               int[] dateiIDListeFürLöschen = Utils.listToIntArray(e.getDateiIDForDeleteOnServerDB(runApplication.clientID));
               String[] filesForUpload = Utils.listToArray(e.getDateiForUploadInDB(runApplication.clientID));
               int[] fileIDsForUpload = Utils.listToIntArray(e.getIdsForUploadInDB(runApplication.clientID));
               int aktPosition = 1;

               int i;
               for(i = 0; i < dateiListeFürLöschen.length; ++i) {
                  logging.logInfo("Starte Löschen der Dateien aus der Datenbank...");
                  tabDateisystem.deleteOne(dateiIDListeFürLöschen[i]);
                  e.updateDateiNachDeleteDB(dateiListeFürLöschen[i], runApplication.clientID);
                  logging.logInfo(dateiListeFürLöschen[i] + " --> Lösche Datei aus der Datenbenk erfogrich");
               }

               for(i = 0; i < filesForUpload.length; ++i) {
                  dateisystem.setId(fileIDsForUpload[i]);
                  dateisystem.setDatei(new File(filesForUpload[i]));

                  try {
                     tabDateisystem.insert(dateisystem);
                  } catch (FileNotFoundException var11) {
                     logging.logPrintStackTrace(var11);
                  } catch (PacketTooBigException var12) {
                     logging.logWarning("Diese Datei ist zu groß für BLOB - " + filesForUpload[i]);
                  }

                  e.updateDateiNachUpload_STATUSDB(filesForUpload[i], runApplication.clientID, (new File(filesForUpload[i])).length());
                  logging.logInfo("DB Upload: " + filesForUpload[i]);
                  ProzessBarAO.progressbar.setValue(100 * aktPosition / filesForUpload.length);
                  ++aktPosition;
               }

               logging.logInfo("DB Upload erfolgreich beendet...");
               runApplication.dbUploadLäuft = 0;
            } catch (SQLException var13) {
               runApplication.dbUploadLäuft = 0;
               logging.logPrintStackTrace(var13);
            }

         }
      };
      threadUpload.start();
   }

   public static void downloadService() {
      runApplication.dbDownloadLäuft = 1;
      Thread threadDownload = new Thread() {
         public void run() {
            try {
               TabelleFTPSync e = new TabelleFTPSync();
               TabelleDateisystem tabDateisystem = new TabelleDateisystem();
               String[] filesForDownload = Utils.listToArray(e.getAllDateiFromDB());
               long[] downloadDateiGroesse = Utils.listToLongArray(e.getAllDateiGroeßeDB());
               int countOfDownload = 0;

               for(int i = 0; i < filesForDownload.length; ++i) {
                  File datei = new File(filesForDownload[i]);
                  if(!datei.exists() | datei.length() != downloadDateiGroesse[i]) {
                     tabDateisystem.read(filesForDownload[i]);
                     logging.logInfo("Lade Datei: " + filesForDownload[i] + " aus der Datenbank...");
                     ++countOfDownload;
                  }
               }

               logging.logInfo("Anzahl der geladenden Dateien aus der DB: " + countOfDownload);
               runApplication.dbDownloadLäuft = 0;
            } catch (Exception var8) {
               runApplication.dbDownloadLäuft = 0;
               logging.logPrintStackTrace(var8);
            }

         }
      };
      threadDownload.start();
   }
}
