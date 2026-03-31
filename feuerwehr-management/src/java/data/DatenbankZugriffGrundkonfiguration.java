package data;

import ao.einstellungen.GrundkonfigurationAO;
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

public class DatenbankZugriffGrundkonfiguration {

   private static DatenbankZugriffGrundkonfiguration instance;
   private Connection dbConnection = null;
   private static Session session = null;


   public static DatenbankZugriffGrundkonfiguration getInstance() {
      if(instance == null) {
         instance = new DatenbankZugriffGrundkonfiguration();
      }

      return instance;
   }

   public static DatenbankZugriffGrundkonfiguration removeInstance() {
      logging.logInfo("Entferne DB instance...");
      instance = null;
      return null;
   }

   protected DatenbankZugriffGrundkonfiguration() {
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
         logging.logError("Fehler beim Laden des Treibers");
         logging.logPrintStackTrace(var3);
         System.exit(0);
      }

      String dbPort;
      if(GrundkonfigurationAO.installationsTyp.getSelectedItem().equals("Serverinstallation") | GrundkonfigurationAO.installationsTyp.getSelectedItem().equals("Neuen Mandant erstellen (Serverinstallation)")) {
         this.connectSSHServer();
         dbPort = GrundkonfigurationAO.sshServerTunnel.getText();
      } else {
         dbPort = GrundkonfigurationAO.datenbankPort.getText();
      }

      try {
         this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + GrundkonfigurationAO.datenbankIP.getText() + ":" + dbPort + "/" + GrundkonfigurationAO.datenbankName.getText(), GrundkonfigurationAO.datenbankUser.getText(), GrundkonfigurationAO.datenbankPasswort.getText());
         logging.logInfo("Verbindung erfolgreich...");
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
      String user = GrundkonfigurationAO.sshServerUser.getText();
      String password = GrundkonfigurationAO.sshServerPasswort.getText();
      String host = GrundkonfigurationAO.sshServerAdresse.getText();
      int port = Integer.parseInt(GrundkonfigurationAO.sshServerPort.getText());

      try {
         JSch e = new JSch();
         session = e.getSession(user, host, port);
         int lokalPort = Integer.parseInt(GrundkonfigurationAO.sshServerTunnel.getText());
         String remoteHost = GrundkonfigurationAO.datenbankIP.getText();
         int remotePort = Integer.parseInt(GrundkonfigurationAO.datenbankPort.getText());
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
