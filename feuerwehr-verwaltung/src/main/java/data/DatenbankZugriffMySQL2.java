/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jcraft.jsch.JSch
 *  com.jcraft.jsch.Session
 *  logging.logging
 *  utilities.MyProperties
 */
package data;

import ao.einstellungen.GrundkonfigurationAO;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import utilities.MyProperties;

public class DatenbankZugriffMySQL2 {
    private static DatenbankZugriffMySQL2 instance;
    private Connection dbConnection = null;
    private static Session session;

    static {
        session = null;
    }

    public static DatenbankZugriffMySQL2 getInstance() {
        if (instance == null) {
            instance = new DatenbankZugriffMySQL2();
        }
        return instance;
    }

    public static DatenbankZugriffMySQL2 removeInstance() {
        logging.logInfo((Object)"Entferne DB instance...");
        instance = null;
        return null;
    }

    protected DatenbankZugriffMySQL2() {
        this.setDbConnection();
    }

    public Connection getDbConnection() {
        return this.dbConnection;
    }

    private void setDbConnection() {
        String dbPort;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            logging.logInfo((Object)"Treiber erfolgreich geladen...");
        }
        catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, " Fehler bei laden des Datenbanktreibers!", "Fehlermeldung", 0);
            logging.logError((Object)"Fehler beim Laden des Treibers");
            logging.logPrintStackTrace((Exception)e);
            System.exit(0);
        }
        if (GrundkonfigurationAO.installationsTyp.getSelectedItem().equals("Serverinstallation") | GrundkonfigurationAO.installationsTyp.getSelectedItem().equals("Neuen Mandant erstellen (Serverinstallation)")) {
            this.connectSSHServer();
            dbPort = GrundkonfigurationAO.sshServerTunnel.getText();
        } else {
            dbPort = GrundkonfigurationAO.datenbankPort.getText();
        }
        try {
            this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + GrundkonfigurationAO.datenbankIP.getText() + ":" + dbPort + "/" + "mysql", GrundkonfigurationAO.datenbankUser.getText(), "");
            logging.logInfo((Object)"Verbindung erfolgreich...");
        }
        catch (SQLException e) {
            if (e.toString().contains("Access denied for user")) {
                JOptionPane.showMessageDialog(null, "Es kann keine Verbindung zur Datenbank aufgebaut werden.\nDas eingegebene Passwort ist falsch.\n\n" + e, "Fehlermeldung", 0);
                logging.logError((Object)"Verbindung zur Datenbank Fehlgeschlagen: Access denied");
            } else if (e.toString().contains("Communications link failure")) {
                JOptionPane.showMessageDialog(null, "Der Datenbank Zugiff ist Fehlgeschlagen\nBitte \u00fcberpr\u00fcfen Sie ob der Datenbank Service l\u00e4uft.\n\n" + e, "Fehlermeldung", 0);
                logging.logError((Object)"Verbindung zur Datenbank Fehlgeschlagen: Service for MySQL is not running // No Connection");
            } else {
                JOptionPane.showMessageDialog(null, "Unerwarteter Fehler Datenbankfehler\nBitte \u00fcberpr\u00fcfen Sie die Verbindung zur Datenbank\n\n" + e, "Fehlermeldung", 0);
                logging.logError((Object)"Verbindung zur Datenbank Fehlgeschlagen: Unexpected Error");
            }
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private void connectSSHServer() {
        String user = GrundkonfigurationAO.sshServerUser.getText();
        String password = GrundkonfigurationAO.sshServerPasswort.getText();
        String host = GrundkonfigurationAO.sshServerAdresse.getText();
        int port = Integer.parseInt(GrundkonfigurationAO.sshServerPort.getText());
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            int lokalPort = Integer.parseInt(GrundkonfigurationAO.sshServerTunnel.getText());
            String remoteHost = GrundkonfigurationAO.datenbankIP.getText();
            int remotePort = Integer.parseInt(GrundkonfigurationAO.datenbankPort.getText());
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            logging.logInfo((Object)"Verbindung zum SSH Server...");
            session.connect();
            int assinged_port = session.setPortForwardingL(lokalPort, remoteHost, remotePort);
            logging.logInfo((Object)("localhost:" + assinged_port + " -> " + remoteHost + ":" + remotePort));
        }
        catch (Exception e) {
            session.disconnect();
            logging.logError((Object)"SSH Server nicht erreichbar");
        }
    }

    public static void disconnectSSHServer() {
        MyProperties einstellungenholen = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties");
        einstellungenholen.loadVars();
        if (einstellungenholen.getVar("DB_TYP").equals("SSH")) {
            logging.logInfo((Object)"Trenne SSH Verbindung");
            session.disconnect();
        }
    }
}

