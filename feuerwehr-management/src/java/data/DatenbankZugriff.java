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

public class DatenbankZugriff {

   private static DatenbankZugriff instance;
   private Connection dbConnection = null;
   private static Session session = null;


   public static DatenbankZugriff getInstance() {
      if(instance == null) {
         instance = new DatenbankZugriff();
      }

      return instance;
   }

   public static DatenbankZugriff removeInstance() {
      logging.logInfo("Entferne DB instance...");
      instance = null;
      return null;
   }

   protected DatenbankZugriff() {
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
      if(((String)runApplication.PROPERTIES.get("DB_TYP")).equals("SSH")) {
         this.connectSSHServer();
         dbPort = (String)runApplication.PROPERTIES.get("SSHTunnel");
      } else {
         dbPort = (String)runApplication.PROPERTIES.get("DatenbankPort");
      }

      try {
         this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + (String)runApplication.PROPERTIES.get("DatenbankIP") + ":" + dbPort + "/" + (String)runApplication.PROPERTIES.get("DatenbankName"), hash.decodeHashCode((String)runApplication.PROPERTIES.get("DatenbankUser")), hash.decodeHashCode((String)runApplication.PROPERTIES.get("DatenbankPasswort")));
         logging.logInfo("DB-Verbindung erfolgreich...");
      } catch (SQLException var4) {
         if(var4.toString().contains("Access denied for user")) {
            JOptionPane.showMessageDialog((Component)null, "Es kann keine Verbindung zur Datenbank aufgebaut werden.\nDas eingegebene Passwort ist falsch.\n\n" + var4, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Access denied");
         } else if(var4.toString().contains("Communications link failure")) {
            JOptionPane.showMessageDialog((Component)null, "Der Datenbank Zugiff ist Fehlgeschlagen\nBitte überprüfen Sie ob der Datenbank Service läuft.\n\n" + var4, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Service for MySQL is not running // No Connection");
         } else {
            JOptionPane.showMessageDialog((Component)null, "Unerwarteter Fehler Datenbankfehler\nBitte überprüfen Sie die Verbindung zur Datenbank\n\n" + var4, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Unexpected Error");
         }

         logging.logPrintStackTrace(var4);
      }

   }

   private void connectSSHServer() {
      String user = hash.decodeHashCode((String)runApplication.PROPERTIES.get("SSHUser"));
      String password = hash.decodeHashCode((String)runApplication.PROPERTIES.get("SSHPasswort"));
      String host = (String)runApplication.PROPERTIES.get("SSHServer");
      int port = Integer.parseInt((String)runApplication.PROPERTIES.get("SSHServerPort"));

      try {
         JSch e = new JSch();
         session = e.getSession(user, host, port);
         int lokalPort = Integer.parseInt((String)runApplication.PROPERTIES.get("SSHTunnel"));
         String remoteHost = (String)runApplication.PROPERTIES.get("DatenbankIP");
         int remotePort = Integer.parseInt((String)runApplication.PROPERTIES.get("DatenbankPort"));
         session.setPassword(password);
         session.setConfig("StrictHostKeyChecking", "no");
         logging.logInfo("Verbindung zum SSH Server...");
         session.connect();
         int assinged_port = session.setPortForwardingL(lokalPort, remoteHost, remotePort);
         logging.logInfo("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
      } catch (Exception var10) {
         session.disconnect();
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
