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
import utilities.MyProperties;
import utilities.hash;

public class DatenbankZugriffBLOB {

   private static DatenbankZugriffBLOB instance;
   private Connection dbConnection = null;
   private static Session session = null;


   public static DatenbankZugriffBLOB getInstance() {
      if(instance == null) {
         instance = new DatenbankZugriffBLOB();
      }

      return instance;
   }

   public static DatenbankZugriffBLOB removeInstance() {
      logging.logInfo("Entferne DB instance...");
      instance = null;
      return null;
   }

   protected DatenbankZugriffBLOB() {
      this.setDbConnection();
   }

   public Connection getDbConnection() {
      return this.dbConnection;
   }

   private void setDbConnection() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         logging.logInfo("Treiber erfolgreich geladen...");
      } catch (ClassNotFoundException var4) {
         JOptionPane.showMessageDialog((Component)null, " Fehler bei laden des Datenbanktreibers!", "Fehlermeldung", 0);
         logging.logError("Fehler beim Laden des Treibers");
         logging.logPrintStackTrace(var4);
         System.exit(0);
      }

      MyProperties einstellungenholen = new MyProperties(runApplication.arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties");
      einstellungenholen.loadVars();
      String dbPort;
      if(einstellungenholen.getVar("DB_TYP").equals("SSH")) {
         this.connectSSHServer();
         dbPort = (String)einstellungenholen.getVar("SSHTunnel");
      } else {
         dbPort = (String)einstellungenholen.getVar("DatenbankPort");
      }

      try {
         this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + (String)einstellungenholen.getVar("DatenbankIP") + ":" + dbPort + "/" + (String)einstellungenholen.getVar("DatenbankName"), hash.decodeHashCode((String)einstellungenholen.getVar("DatenbankUser")), hash.decodeHashCode((String)einstellungenholen.getVar("DatenbankPasswort")));
         logging.logInfo("Verbindung erfolgreich...");
      } catch (SQLException var5) {
         if(var5.toString().contains("Access denied for user")) {
            JOptionPane.showMessageDialog((Component)null, "Es kann keine Verbindung zur Datenbank aufgebaut werden.\nDas eingegebene Passwort ist falsch.\n\n" + var5, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Access denied");
         } else if(var5.toString().contains("Communications link failure")) {
            JOptionPane.showMessageDialog((Component)null, "Der Datenbank Zugiff ist Fehlgeschlagen\nBitte überprüfen Sie ob der Datenbank Service läuft.\n\n" + var5, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Service for MySQL is not running // No Connection");
         } else {
            JOptionPane.showMessageDialog((Component)null, "Unerwarteter Fehler Datenbankfehler\nBitte überprüfen Sie die Verbindung zur Datenbank\n\n" + var5, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Unexpected Error");
         }

         logging.logPrintStackTrace(var5);
      }

   }

   private void connectSSHServer() {
      MyProperties einstellungenholen = new MyProperties(runApplication.arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties");
      einstellungenholen.loadVars();
      String user = hash.decodeHashCode((String)einstellungenholen.getVar("SSHUser"));
      String password = hash.decodeHashCode((String)einstellungenholen.getVar("SSHPasswort"));
      String host = (String)einstellungenholen.getVar("SSHServer");
      int port = Integer.parseInt((String)einstellungenholen.getVar("SSHServerPort"));

      try {
         JSch e = new JSch();
         session = e.getSession(user, host, port);
         int lokalPort = Integer.parseInt((String)einstellungenholen.getVar("SSHTunnel"));
         String remoteHost = (String)einstellungenholen.getVar("DatenbankIP");
         int remotePort = Integer.parseInt((String)einstellungenholen.getVar("DatenbankPort"));
         session.setPassword(password);
         session.setConfig("StrictHostKeyChecking", "no");
         logging.logInfo("Verbindung zum SSH Server...");
         session.connect();
         int assinged_port = session.setPortForwardingL(lokalPort, remoteHost, remotePort);
         logging.logInfo("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort);
      } catch (Exception var11) {
         session.disconnect();
         logging.logError("SSH Server nicht erreichbar");
      }

   }

   public static void disconnectSSHServer() {
      MyProperties einstellungenholen = new MyProperties(runApplication.arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties");
      einstellungenholen.loadVars();
      if(einstellungenholen.getVar("DB_TYP").equals("SSH")) {
         logging.logInfo("Trenne SSH Verbindung");
         session.disconnect();
      }

   }
}
