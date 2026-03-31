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
      runApplication.verarbeitungLäuft = 1;
      ProzessBarAO.label_bitteWarten.setText("Datenbank wird installiert... Bitte warten...");
      ProzessBarAO.progressbar.setValue(0);
      Thread threadCalculateProcessBar = new Thread() {
         public void run() {
            int count = 0;

            while(true) {
               ++count;
               ProzessBarAO.progressbar.setValue(count);

               try {
                  Thread.sleep(1200L);
               } catch (InterruptedException var3) {
                  ;
               }
            }
         }
      };
      threadCalculateProcessBar.start();
      String cmd1 = "msiexec /i \"" + System.getProperty("user.dir") + "\\install\\mysql-5.5.28-win32.msi\" /passive";
      String[] cmd2 = new String[2];
      String[] cmd3 = new String[9];

      MyProperties dbPropertis;
      try {
         logging.logInfo("Starte Installation der internen DB: " + System.getProperty("user.dir") + "\\install\\mysql-5.5.28-win32.msi");
         executeDosCommand2(cmd1);
         logging.logInfo("DB wurde erfolgreich Installiert");
         if(System.getenv("ProgramW6432") != null && (new File("C:\\Program Files (x86)")).exists()) {
            logging.logInfo("Installiere für 64bit Betriebssysteme");
            logging.logInfo("Installationsordner: C:/Programm Files (x86)");
            cmd2[0] = "\"C:\\Program Files (x86)\\MySQL\\MySQL Server 5.5\\bin\\mysqld.exe\"";
            cmd3[0] = "\"C:\\Program Files (x86)\\MySQL\\MySQL Server 5.5\\bin\\mysqlinstanceconfig.exe\"";
         } else if((new File("C:\\Programme")).exists()) {
            logging.logInfo("Installiere für 32bit Betriebssysteme (DEU)");
            logging.logInfo("Installationsordner: C:/Programme");
            cmd2[0] = "\"C:\\Programme\\MySQL\\MySQL Server 5.5\\bin\\mysqld.exe\"";
            cmd3[0] = "\"C:\\Programme\\MySQL\\MySQL Server 5.5\\bin\\mysqlinstanceconfig.exe\"";
         } else if((new File("C:\\Program Files")).exists()) {
            logging.logInfo("Installiere für 32bit Betriebssysteme (ENG)");
            logging.logInfo("Installationsordner: C:/Program Files");
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
            executeDosCommand(cmd2);
            Thread.sleep(1000L);
            logging.logInfo("Starte MySQL Dienst mittels net start...");
            executeDosCommand2("net start mysql");
            logging.logInfo("MySQL Windows-Dienst erfolgreich gestartet");
         } catch (Exception var8) {
            logging.logWarning("MySQL Dienst konnte nicht gestartet werden oder läuft bereits...");
         }

         try {
            ProzessBarAO.label_bitteWarten.setText("Datenbank wird konfiguriert... Bitte warten...");
            Thread.sleep(3000L);
            executeDosCommand(cmd3);
         } catch (Exception var7) {
            logging.logWarning("Bei der Konfiguration ist ein Fehler aufgetreten.");
            logging.logWarning("NullPointerException wird abgefangen...");
            (new CreateDatabase()).changeRootPasswort(GrundkonfigurationAO.datenbankPasswort.getText());
         }

         logging.logInfo("Datenbank erfolgreich konfiguriert");
         Thread.sleep(1000L);
         MyProperties e = new MyProperties(runApplication.arbeitsverzeichnis + "properties/db.properties");
         if(System.getenv("ProgramW6432") != null && (new File("C:\\Program Files (x86)")).exists()) {
            dbPropertis = new MyProperties("C:\\Program Files (x86)\\MySQL\\MySQL Server 5.5\\my.ini");
            dbPropertis.loadVars();
            e.putVar("installFolder", dbPropertis.getVar("basedir"));
            e.putVar("dataFolder", dbPropertis.getVar("datadir"));
         } else if((new File("C:\\Programme")).exists()) {
            dbPropertis = new MyProperties("C:\\Programme\\MySQL\\MySQL Server 5.5\\my.ini");
            dbPropertis.loadVars();
            e.putVar("installFolder", dbPropertis.getVar("basedir"));
            e.putVar("dataFolder", dbPropertis.getVar("datadir"));
         }

         e.putVar("insatallaion", "1");
         e.putVar("konfiguration", "1");
         e.putVar("DB_TYP", "intern");
         e.saveVars();
         logging.logInfo("Datenbank Einstellungsdatei wurde erzeugt");
         Thread.sleep(1000L);
         runApplication.verarbeitungLäuft = 0;
         threadCalculateProcessBar.stop();
         ProzessBarAO.progressbar.setValue(100);
         Thread.sleep(1000L);
         logging.logInfo("Datenbank installation erfogreich abgeschlossen");
      } catch (Exception var9) {
         runApplication.verarbeitungLäuft = 0;
         threadCalculateProcessBar.stop();

         try {
            logging.logInfo("Deinstalliere Service...");
            executeDosCommand2("sc delete mysql");
            logging.logInfo("Service Deinstallation erfolgreich...");
         } catch (Exception var6) {
            logging.logError("Fehler beim entfernen des Services...");
            logging.logPrintStackTrace(var9);
         }

         dbPropertis = new MyProperties(runApplication.arbeitsverzeichnis + "properties/db.properties");
         dbPropertis.sourceFileDelete();
         MyEvent.setEvent("0x0030");
         logging.logPrintStackTrace(var9);
      }

   }

   private static void executeDosCommand(String[] parameter) throws Exception {
      Thread.sleep(1500L);
      Process runtimeProcess = Runtime.getRuntime().exec(parameter);
      int processComplete = runtimeProcess.waitFor();
      if(processComplete == 0) {
         logging.logInfo("CMD Erfolgreich ausgeführt");
      } else {
         logging.logError("CMD konnte nicht ausgeführt werden");
         throw new Exception("CMD konnte nicht ausgeführt werden");
      }
   }

   private static void executeDosCommand2(String parameter) throws Exception {
      Thread.sleep(1500L);
      Process runtimeProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", parameter});
      int processComplete = runtimeProcess.waitFor();
      if(processComplete == 0) {
         logging.logInfo("CMD Erfolgreich ausgeführt");
      } else {
         logging.logError("CMD konnte nicht ausgeführt werden");
         throw new Exception("CMD konnte nicht ausgeführt werden");
      }
   }

   public static boolean checkFolderPermission() {
      File file = new File(runApplication.arbeitsverzeichnis + "properties/checkPermission");

      try {
         if(!file.exists()) {
            file.createNewFile();
         }

         if(file.canExecute() && file.canRead() && file.canWrite()) {
            logging.logInfo("Der Windows Benutzer hat Lese und Schreibrechte im Installationsordner");
            return true;
         } else {
            logging.logInfo("Keine Lese und Schreibrechte im Installationsordner");
            return false;
         }
      } catch (IOException var2) {
         logging.logError("Fehler beim ausführen im Installationsordner");
         logging.logPrintStackTrace(var2);
         return false;
      }
   }
}
