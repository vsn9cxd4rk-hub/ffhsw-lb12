package data;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import utilities.hash;

public class DatenbankKartenZugriff {

   private static DatenbankKartenZugriff instance;
   private Connection dbConnection = null;
   private static Session session = null;


   public static DatenbankKartenZugriff getInstance() {
      if(instance == null) {
         instance = new DatenbankKartenZugriff();
      }

      return instance;
   }

   public static DatenbankKartenZugriff removeInstance() {
      logging.logInfo("Entferne DB instance...");
      instance = null;
      return null;
   }

   protected DatenbankKartenZugriff() {
      this.setDbConnection();
   }

   public Connection getDbConnection() {
      return this.dbConnection;
   }

   private void setDbConnection() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         logging.logInfo("Treiber erfolgreich geladen...");
      } catch (ClassNotFoundException var3) {
         JOptionPane.showMessageDialog((Component)null, " Fehler bei laden des Datenbanktreibers!", "Fehlermeldung", 0);
         logging.logError("Fehler beim Laden des DB-Treibers");
         logging.logPrintStackTrace(var3);
         System.exit(0);
      }

      String dbPort;
      if(((String)runApplication.EINSTELLUNGEN.get("externeKartenDB_Typ")).equals("SSH")) {
         this.connectSSHServer();
         dbPort = (String)runApplication.EINSTELLUNGEN.get("externeKartenSSHTunnel");
      } else {
         dbPort = (String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankPort");
      }

      try {
         this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + (String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankIP") + ":" + dbPort + "/" + (String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankName"), hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankUser")), hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankPasswort")));
         logging.logInfo("DB-Verbindung erfolgreich...");
      } catch (SQLException var4) {
         if(var4.toString().contains("Access denied for user")) {
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Access denied");
         } else if(var4.toString().contains("Communications link failure")) {
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Service for MySQL is not running // No Connection");
         } else {
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Unexpected Error");
         }

         logging.logPrintStackTrace(var4);
         if(((String)runApplication.EINSTELLUNGEN.get("externeKartenDB_Typ")).equals("SSH")) {
            disconnectSSHServer();
         }

         removeInstance();
      }

   }

   private void connectSSHServer() {
      String user = hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHUser"));
      String password = hash.decodeHashCode((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHPasswort"));
      String host = (String)runApplication.EINSTELLUNGEN.get("externeKartenSSHServer");
      int port = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHServerPort"));

      try {
         JSch e = new JSch();
         session = e.getSession(user, host, port);
         int lokalPort = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("externeKartenSSHTunnel"));
         String remoteHost = (String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankIP");
         int remotePort = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("externeKartenDatenbankPort"));
         session.setPassword(password);
         session.setConfig("StrictHostKeyChecking", "no");
         logging.logInfo("Verbindung zum SSH Server...");
         session.connect();
         int assinged_port = session.setPortForwardingL(lokalPort, remoteHost, remotePort);
         logging.logInfo("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
      } catch (Exception var10) {
         disconnectSSHServer();
         logging.logError("SSH Server nicht erreichbar");
      }

   }

   public static void disconnectSSHServer() {
      if(((String)runApplication.PROPERTIES.get("DB_TYP")).equals("SSH")) {
         logging.logInfo("Trenne SSH Verbindung");
         session.disconnect();
         logging.logInfo("SSH Verbindung erfolgreich getrennt");
      }

   }
}
