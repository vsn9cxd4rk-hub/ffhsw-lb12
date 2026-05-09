/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.CompressFolder
 *  utilities.MyProperties
 *  utilities.hash
 */
package service;

import data.tabellen.einstellungen.TabelleEinstellungen;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import service.SystemWarnungService;
import utilities.CompressFolder;
import utilities.MyProperties;
import utilities.Utils;
import utilities.hash;

public class DatensicherungService {
    public static void DBSave(String speicherOrt) {
        try {
            if (runApplication.EINSTELLUNGEN.get("letzterDBsave").equals("0")) {
                logging.logInfo((Object)"Update Einstellungstabelle mit aktueller Zeit");
                new TabelleEinstellungen().update("letzterDBsave", Long.toString(System.currentTimeMillis()));
            } else {
                long days = (long)(86400 * Integer.parseInt(runApplication.EINSTELLUNGEN.get("autoDBsaveTage"))) * 1000L;
                long value = System.currentTimeMillis() - days;
                if (Long.parseLong(runApplication.EINSTELLUNGEN.get("letzterDBsave")) <= value) {
                    logging.logInfo((Object)"DBSave muss erstellt werden");
                    DatensicherungService.ausfuehrenDBSave(speicherOrt);
                    new TabelleEinstellungen().update("letzterDBsave", Long.toString(System.currentTimeMillis()));
                    logging.logInfo((Object)"DBSave wurde erstellt");
                } else {
                    logging.logInfo((Object)"DBSave muss NICHT erstellt werden");
                }
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void ausfuehrenDBSave(final String speicherOrt) {
        Thread threadDBBackup = new Thread(){

            @Override
            public void run() {
                runApplication.verarbeitungL\u00e4uft = 1;
                MyProperties properties = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties");
                properties.loadVars();
                String username = hash.decodeHashCode((String)((String)properties.getVar("DatenbankUser")));
                String password = hash.decodeHashCode((String)((String)properties.getVar("DatenbankPasswort")));
                String dbname = (String)properties.getVar("DatenbankName");
                String[] executeCmd = new String[]{String.valueOf(System.getProperty("user.dir")) + "\\exe\\mysqldump", "-u" + username, "-p" + password, "--database", dbname, "-r", speicherOrt};
                try {
                    try {
                        double startZeit = System.currentTimeMillis();
                        logging.logInfo((Object)"Start Datenbank Backup");
                        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
                        int processComplete = runtimeProcess.waitFor();
                        if (processComplete == 0) {
                            logging.logInfo((Object)"Datenbank Backup beendet");
                        } else {
                            logging.logError((Object)"Datenbank Backup nicht erfolgreich beendet");
                        }
                        Utils.dateiKatalogisieren(speicherOrt);
                        double endZeit = (double)System.currentTimeMillis() - startZeit;
                        logging.logInfo((Object)("DB Datensicherung erstellt in: " + endZeit + " ms"));
                        if (runApplication.EINSTELLUNGEN.get("FullBackupInZip").equals("1")) {
                            logging.logInfo((Object)"FullBackupInZip --> Aktiviert");
                            DatensicherungService.fullBackupAusfuehren();
                        }
                    }
                    catch (Exception ex) {
                        logging.logError((Object)"Fehler bei der Datensicherung...");
                        SystemWarnungService.insertSystemWarnung("Datensicherung Fehler");
                        logging.logPrintStackTrace((Exception)ex);
                        runApplication.verarbeitungL\u00e4uft = 0;
                        logging.logInfo((Object)"BACKUP...setzte Bearbeitungsstatus zur\u00fcck und beende die Backup Methode");
                        this.stop();
                    }
                }
                finally {
                    runApplication.verarbeitungL\u00e4uft = 0;
                    logging.logInfo((Object)"BACKUP...setzte Bearbeitungsstatus zur\u00fcck und beende die Backup Methode");
                    this.stop();
                }
            }
        };
        threadDBBackup.start();
    }

    private static void fullBackupAusfuehren() throws Exception {
        String tempBackupFile;
        double startZeit = System.currentTimeMillis();
        File tempBackupPath = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data_backup");
        if (!tempBackupPath.exists()) {
            tempBackupPath.mkdirs();
            logging.logInfo((Object)"Temp Backup ordner wurde erstellt");
        }
        if (new File(tempBackupFile = String.valueOf(runApplication.arbeitsverzeichnis) + "data_backup/" + "FeuerwehrManagementSystem" + "_Backup.zip").exists()) {
            new File(tempBackupFile).delete();
            logging.logInfo((Object)"Altes Backup wurde gel\u00f6scht...");
        }
        logging.logInfo((Object)("Erstelle ZIP Backup Datei! --> " + tempBackupFile));
        String toZipBackup = null;
        toZipBackup = runApplication.arbeitsverzeichnis.equals("") ? String.valueOf(System.getProperty("user.dir")) + "/data" : String.valueOf(runApplication.arbeitsverzeichnis) + "data";
        logging.logInfo((Object)("Folgendes wird zum Backup hinzugef\u00fcgt: " + toZipBackup));
        CompressFolder.ZipErstellen((String)tempBackupFile, (String)toZipBackup);
        logging.logInfo((Object)"ZIP Datei erfolgreich erstellt!");
        logging.logInfo((Object)("Kopiere Backup Datei in " + runApplication.EINSTELLUNGEN.get("FullBackupPath")));
        logging.logInfo((Object)"Sichere Backup auf dem Backup-Pfad...");
        Path input = Paths.get(tempBackupFile, new String[0]);
        Path output = Paths.get(String.valueOf(runApplication.EINSTELLUNGEN.get("FullBackupPath")) + "FeuerwehrManagementSystem" + "_Backup.zip", new String[0]);
        Files.copy(input, output, StandardCopyOption.REPLACE_EXISTING);
        logging.logInfo((Object)"Kopieren der Backup Datei abgeschlossen");
        double endZeit = (double)System.currentTimeMillis() - startZeit;
        logging.logInfo((Object)("ZIP erstellt in: " + endZeit + " ms"));
    }
}

