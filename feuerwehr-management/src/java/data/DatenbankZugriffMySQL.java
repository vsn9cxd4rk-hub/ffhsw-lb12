package data;

import ao.einstellungen.GrundkonfigurationAO;
import ao.einstellungen.GrundkonfigurationJWSAO;
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

public class DatenbankZugriffMySQL {

   private static DatenbankZugriffMySQL instance;
   private Connection dbConnection = null;
   private static Session session = null;


   public static DatenbankZugriffMySQL getInstance() {
      if(instance == null) {
         instance = new DatenbankZugriffMySQL();
      }

      return instance;
   }

   public static DatenbankZugriffMySQL removeInstance() {
      logging.logInfo("Entferne DB instance...");
      instance = null;
      return null;
   }

   protected DatenbankZugriffMySQL() {
      this.setDbConnection();
   }

   public Connection getDbConnection() {
      return this.dbConnection;
   }

   private void setDbConnection() {
      try {
         Class.forName("com.mysql.jdbc.Driver");
         logging.logInfo("Treiber erfolgreich geladen...");
      } catch (ClassNotFoundException var2) {
         JOptionPane.showMessageDialog((Component)null, " Fehler bei laden des Datenbanktreibers!", "Fehlermeldung", 0);
         logging.logError("Fehler beim Laden des Treibers");
         logging.logPrintStackTrace(var2);
         System.exit(0);
      }

      try {
         String e;
         if(runApplication.JavaWebStart == 1) {
            if(GrundkonfigurationJWSAO.installationsTyp.getSelectedItem().equals("Serverinstallation") | GrundkonfigurationJWSAO.installationsTyp.getSelectedItem().equals("Neuen Mandant erstellen (Serverinstallation)")) {
               this.connectSSHServer();
               e = GrundkonfigurationJWSAO.sshServerTunnel.getText();
            } else {
               e = GrundkonfigurationJWSAO.datenbankPort.getText();
            }

            this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + GrundkonfigurationJWSAO.datenbankIP.getText() + ":" + e + "/" + "mysql", GrundkonfigurationJWSAO.datenbankUser.getText(), GrundkonfigurationJWSAO.datenbankPasswort.getText());
            logging.logInfo("Verbindung erfolgreich...");
         } else {
            if(GrundkonfigurationAO.installationsTyp.getSelectedItem().equals("Serverinstallation") | GrundkonfigurationAO.installationsTyp.getSelectedItem().equals("Neuen Mandant erstellen (Serverinstallation)")) {
               this.connectSSHServer();
               e = GrundkonfigurationAO.sshServerTunnel.getText();
            } else {
               e = GrundkonfigurationAO.datenbankPort.getText();
            }

            this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + GrundkonfigurationAO.datenbankIP.getText() + ":" + e + "/" + "mysql", GrundkonfigurationAO.datenbankUser.getText(), GrundkonfigurationAO.datenbankPasswort.getText());
            logging.logInfo("Verbindung erfolgreich...");
         }
      } catch (SQLException var3) {
         if(var3.toString().contains("Access denied for user")) {
            JOptionPane.showMessageDialog((Component)null, "Es kann keine Verbindung zur Datenbank aufgebaut werden.\nDas eingegebene Passwort ist falsch.\n\n" + var3, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Access denied");
         } else if(var3.toString().contains("Communications link failure")) {
            JOptionPane.showMessageDialog((Component)null, "Der Datenbank Zugiff ist Fehlgeschlagen\nBitte überprüfen Sie ob der Datenbank Service läuft.\n\n" + var3, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Service for MySQL is not running // No Connection");
         } else {
            JOptionPane.showMessageDialog((Component)null, "Unerwarteter Fehler Datenbankfehler\nBitte überprüfen Sie die Verbindung zur Datenbank\n\n" + var3, "Fehlermeldung", 0);
            logging.logError("Verbindung zur Datenbank Fehlgeschlagen: Unexpected Error");
         }

         logging.logPrintStackTrace(var3);
      }

   }

   private void connectSSHServer() {
      String user;
      String password;
      String host;
      int port;
      int lokalPort;
      String remoteHost;
      int remotePort;
      if(runApplication.JavaWebStart == 1) {
         user = GrundkonfigurationJWSAO.sshServerUser.getText();
         password = GrundkonfigurationJWSAO.sshServerPasswort.getText();
         host = GrundkonfigurationJWSAO.sshServerAdresse.getText();
         port = Integer.parseInt(GrundkonfigurationJWSAO.sshServerPort.getText());
         lokalPort = Integer.parseInt(GrundkonfigurationJWSAO.sshServerTunnel.getText());
         remoteHost = GrundkonfigurationJWSAO.datenbankIP.getText();
         remotePort = Integer.parseInt(GrundkonfigurationJWSAO.datenbankPort.getText());
      } else {
         user = GrundkonfigurationAO.sshServerUser.getText();
         password = GrundkonfigurationAO.sshServerPasswort.getText();
         host = GrundkonfigurationAO.sshServerAdresse.getText();
         port = Integer.parseInt(GrundkonfigurationAO.sshServerPort.getText());
         lokalPort = Integer.parseInt(GrundkonfigurationAO.sshServerTunnel.getText());
         remoteHost = GrundkonfigurationAO.datenbankIP.getText();
         remotePort = Integer.parseInt(GrundkonfigurationAO.datenbankPort.getText());
      }

      try {
         JSch e = new JSch();
         session = e.getSession(user, host, port);
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
      MyProperties einstellungenholen = new MyProperties(runApplication.arbeitsverzeichnis + "properties/FeuerwehrManagementSystem.properties");
      einstellungenholen.loadVars();
      if(einstellungenholen.getVar("DB_TYP").equals("SSH")) {
         logging.logInfo("Trenne SSH Verbindung");
         session.disconnect();
      }

   }
}
