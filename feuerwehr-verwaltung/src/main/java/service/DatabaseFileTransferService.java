/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mysql.jdbc.PacketTooBigException
 *  logging.logging
 */
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
        runApplication.dbUploadL\u00e4uft = 1;
        Thread threadUpload = new Thread(){

            @Override
            public void run() {
                try {
                    TabelleFTPSync tabSync = new TabelleFTPSync();
                    TabelleDateisystem tabDateisystem = new TabelleDateisystem();
                    Dateisystem dateisystem = new Dateisystem();
                    String[] dateiListeF\u00fcrL\u00f6schen = Utils.listToArray(tabSync.getDateiForDeleteOnServerDB(runApplication.clientID));
                    int[] dateiIDListeF\u00fcrL\u00f6schen = Utils.listToIntArray(tabSync.getDateiIDForDeleteOnServerDB(runApplication.clientID));
                    String[] filesForUpload = Utils.listToArray(tabSync.getDateiForUploadInDB(runApplication.clientID));
                    int[] fileIDsForUpload = Utils.listToIntArray(tabSync.getIdsForUploadInDB(runApplication.clientID));
                    int aktPosition = 1;
                    int l = 0;
                    while (l < dateiListeF\u00fcrL\u00f6schen.length) {
                        logging.logInfo((Object)"Starte L\u00f6schen der Dateien aus der Datenbank...");
                        tabDateisystem.deleteOne(dateiIDListeF\u00fcrL\u00f6schen[l]);
                        tabSync.updateDateiNachDeleteDB(dateiListeF\u00fcrL\u00f6schen[l], runApplication.clientID);
                        logging.logInfo((Object)(String.valueOf(dateiListeF\u00fcrL\u00f6schen[l]) + " --> L\u00f6sche Datei aus der Datenbenk erfogrich"));
                        ++l;
                    }
                    int i = 0;
                    while (i < filesForUpload.length) {
                        dateisystem.setId(fileIDsForUpload[i]);
                        dateisystem.setDatei(new File(filesForUpload[i]));
                        try {
                            tabDateisystem.insert(dateisystem);
                        }
                        catch (FileNotFoundException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        catch (PacketTooBigException e1) {
                            logging.logWarning((Object)("Diese Datei ist zu gro\u00df f\u00fcr BLOB - " + filesForUpload[i]));
                        }
                        tabSync.updateDateiNachUpload_STATUSDB(filesForUpload[i], runApplication.clientID, new File(filesForUpload[i]).length());
                        logging.logInfo((Object)("DB Upload: " + filesForUpload[i]));
                        ProzessBarAO.progressbar.setValue(100 * aktPosition / filesForUpload.length);
                        ++aktPosition;
                        ++i;
                    }
                    logging.logInfo((Object)"DB Upload erfolgreich beendet...");
                    runApplication.dbUploadL\u00e4uft = 0;
                }
                catch (SQLException e) {
                    runApplication.dbUploadL\u00e4uft = 0;
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadUpload.start();
    }

    public static void downloadService() {
        runApplication.dbDownloadL\u00e4uft = 1;
        Thread threadDownload = new Thread(){

            @Override
            public void run() {
                try {
                    TabelleFTPSync tabSync = new TabelleFTPSync();
                    TabelleDateisystem tabDateisystem = new TabelleDateisystem();
                    String[] filesForDownload = Utils.listToArray(tabSync.getAllDateiFromDB());
                    long[] downloadDateiGroesse = Utils.listToLongArray(tabSync.getAllDateiGroe\u00dfeDB());
                    int countOfDownload = 0;
                    int i = 0;
                    while (i < filesForDownload.length) {
                        File datei = new File(filesForDownload[i]);
                        if (!datei.exists() | datei.length() != downloadDateiGroesse[i]) {
                            tabDateisystem.read(filesForDownload[i]);
                            logging.logInfo((Object)("Lade Datei: " + filesForDownload[i] + " aus der Datenbank..."));
                            ++countOfDownload;
                        }
                        ++i;
                    }
                    logging.logInfo((Object)("Anzahl der geladenden Dateien aus der DB: " + countOfDownload));
                    runApplication.dbDownloadL\u00e4uft = 0;
                }
                catch (Exception e) {
                    runApplication.dbDownloadL\u00e4uft = 0;
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadDownload.start();
    }
}

