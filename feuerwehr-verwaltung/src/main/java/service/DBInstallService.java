/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.MyProperties
 */
package service;

import ao.einstellungen.GrundkonfigurationAO;
import ao.utils.ProzessBarAO;
import data.tabellen.einstellungen.CreateDatabase;
import java.io.File;
import java.io.IOException;
import logging.logging;
import run.runApplication;
import utilities.MyEvent;
import utilities.MyProperties;

public class DBInstallService {
    public static void DBInstallAusfuehre() {
        runApplication.verarbeitungL\u00e4uft = 1;
        ProzessBarAO.label_bitteWarten.setText("Datenbank wird installiert... Bitte warten...");
        ProzessBarAO.progressbar.setValue(0);
        Thread threadCalculateProcessBar = new Thread(){

            @Override
            public void run() {
                int count = 0;
                while (true) {
                    ProzessBarAO.progressbar.setValue(++count);
                    try {
                        Thread.sleep(1200L);
                    }
                    catch (InterruptedException interruptedException) {
                    }
                }
            }
        };
        threadCalculateProcessBar.start();
        String cmd1 = "msiexec /i \"" + System.getProperty("user.dir") + "\\install\\mysql-5.5.28-win32.msi\" /passive";
        String[] cmd2 = new String[2];
        String[] cmd3 = new String[9];
        try {
            logging.logInfo((Object)("Starte Installation der internen DB: " + System.getProperty("user.dir") + "\\install\\mysql-5.5.28-win32.msi"));
            DBInstallService.executeDosCommand2(cmd1);
            logging.logInfo((Object)"DB wurde erfolgreich Installiert");
            if (System.getenv("ProgramW6432") != null && new File("C:\\Program Files (x86)").exists()) {
                logging.logInfo((Object)"Installiere f\u00fcr 64bit Betriebssysteme");
                logging.logInfo((Object)"Installationsordner: C:/Programm Files (x86)");
                cmd2[0] = "\"C:\\Program Files (x86)\\MySQL\\MySQL Server 5.5\\bin\\mysqld.exe\"";
                cmd3[0] = "\"C:\\Program Files (x86)\\MySQL\\MySQL Server 5.5\\bin\\mysqlinstanceconfig.exe\"";
            } else if (new File("C:\\Programme").exists()) {
                logging.logInfo((Object)"Installiere f\u00fcr 32bit Betriebssysteme (DEU)");
                logging.logInfo((Object)"Installationsordner: C:/Programme");
                cmd2[0] = "\"C:\\Programme\\MySQL\\MySQL Server 5.5\\bin\\mysqld.exe\"";
                cmd3[0] = "\"C:\\Programme\\MySQL\\MySQL Server 5.5\\bin\\mysqlinstanceconfig.exe\"";
            } else if (new File("C:\\Program Files").exists()) {
                logging.logInfo((Object)"Installiere f\u00fcr 32bit Betriebssysteme (ENG)");
                logging.logInfo((Object)"Installationsordner: C:/Program Files");
                cmd2[0] = "\"C:\\Program Files\\MySQL\\MySQL Server 5.5\\bin\\mysqld.exe\"";
                cmd3[0] = "\"C:\\Program Files\\MySQL\\MySQL Server 5.5\\bin\\mysqlinstanceconfig.exe\"";
            }
            cmd2[1] = "--install";
            cmd3[1] = "-i";
            cmd3[2] = "-q";
            cmd3[3] = "ServiceName=MySQL";
            cmd3[4] = "RootPassword=root_fms";
            cmd3[5] = "ServerType=SERVER";
            cmd3[6] = "DatabaseType=INNODB";
            cmd3[7] = "Port=3306";
            cmd3[8] = "Charset=utf8";
            try {
                ProzessBarAO.label_bitteWarten.setText("Datenbank Dienst wird installiert... Bitte warten...");
                Thread.sleep(1000L);
                DBInstallService.executeDosCommand(cmd2);
                Thread.sleep(1000L);
                logging.logInfo((Object)"Starte MySQL Dienst mittels net start...");
                DBInstallService.executeDosCommand2("net start mysql");
                logging.logInfo((Object)"MySQL Windows-Dienst erfolgreich gestartet");
            }
            catch (Exception e) {
                logging.logWarning((Object)"MySQL Dienst konnte nicht gestartet werden oder l\u00e4uft bereits...");
            }
            try {
                ProzessBarAO.label_bitteWarten.setText("Datenbank wird konfiguriert... Bitte warten...");
                Thread.sleep(3000L);
                DBInstallService.executeDosCommand(cmd3);
            }
            catch (Exception e) {
                logging.logWarning((Object)"Bei der Konfiguration ist ein Fehler aufgetreten.");
                logging.logWarning((Object)"NullPointerException wird abgefangen...");
                new CreateDatabase().changeRootPasswort(GrundkonfigurationAO.datenbankPasswort.getText());
            }
            logging.logInfo((Object)"Datenbank erfolgreich konfiguriert");
            Thread.sleep(1000L);
            MyProperties dbProperties = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/db.properties");
            if (System.getenv("ProgramW6432") != null && new File("C:\\Program Files (x86)").exists()) {
                MyProperties mysqlINI = new MyProperties("C:\\Program Files (x86)\\MySQL\\MySQL Server 5.5\\my.ini");
                mysqlINI.loadVars();
                dbProperties.putVar("installFolder", mysqlINI.getVar("basedir"));
                dbProperties.putVar("dataFolder", mysqlINI.getVar("datadir"));
            } else if (new File("C:\\Programme").exists()) {
                MyProperties mysqlINI = new MyProperties("C:\\Programme\\MySQL\\MySQL Server 5.5\\my.ini");
                mysqlINI.loadVars();
                dbProperties.putVar("installFolder", mysqlINI.getVar("basedir"));
                dbProperties.putVar("dataFolder", mysqlINI.getVar("datadir"));
            }
            dbProperties.putVar("insatallaion", (Object)"1");
            dbProperties.putVar("konfiguration", (Object)"1");
            dbProperties.putVar("DB_TYP", (Object)"intern");
            dbProperties.saveVars();
            logging.logInfo((Object)"Datenbank Einstellungsdatei wurde erzeugt");
            Thread.sleep(1000L);
            runApplication.verarbeitungL\u00e4uft = 0;
            threadCalculateProcessBar.stop();
            ProzessBarAO.progressbar.setValue(100);
            Thread.sleep(1000L);
            logging.logInfo((Object)"Datenbank installation erfogreich abgeschlossen");
        }
        catch (Exception e) {
            runApplication.verarbeitungL\u00e4uft = 0;
            threadCalculateProcessBar.stop();
            try {
                logging.logInfo((Object)"Deinstalliere Service...");
                DBInstallService.executeDosCommand2("sc delete mysql");
                logging.logInfo((Object)"Service Deinstallation erfolgreich...");
            }
            catch (Exception e1) {
                logging.logError((Object)"Fehler beim entfernen des Services...");
                logging.logPrintStackTrace((Exception)e);
            }
            MyProperties dbPropertis = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/db.properties");
            dbPropertis.sourceFileDelete();
            MyEvent.setEvent((String)"0x0030");
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private static void executeDosCommand(String[] parameter) throws Exception {
        Thread.sleep(1500L);
        Process runtimeProcess = Runtime.getRuntime().exec(parameter);
        int processComplete = runtimeProcess.waitFor();
        if (processComplete != 0) {
            logging.logError((Object)"CMD konnte nicht ausgef\u00fchrt werden");
            throw new Exception("CMD konnte nicht ausgef\u00fchrt werden");
        }
        logging.logInfo((Object)"CMD Erfolgreich ausgef\u00fchrt");
    }

    private static void executeDosCommand2(String parameter) throws Exception {
        Thread.sleep(1500L);
        Process runtimeProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", parameter});
        int processComplete = runtimeProcess.waitFor();
        if (processComplete != 0) {
            logging.logError((Object)"CMD konnte nicht ausgef\u00fchrt werden");
            throw new Exception("CMD konnte nicht ausgef\u00fchrt werden");
        }
        logging.logInfo((Object)"CMD Erfolgreich ausgef\u00fchrt");
    }

    public static boolean checkFolderPermission() {
        block4: {
            File file = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/checkPermission");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                if (!file.canExecute() || !file.canRead() || !file.canWrite()) break block4;
                logging.logInfo((Object)"Der Windows Benutzer hat Lese und Schreibrechte im Installationsordner");
                return true;
            }
            catch (IOException e) {
                logging.logError((Object)"Fehler beim ausf\u00fchren im Installationsordner");
                logging.logPrintStackTrace((Exception)e);
                return false;
            }
        }
        logging.logInfo((Object)"Keine Lese und Schreibrechte im Installationsordner");
        return false;
    }
}

