/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.jcraft.jsch.JSch
 *  com.jcraft.jsch.Session
 *  logging.logging
 *  utilities.hash
 */
package data;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
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
    private static Session session;

    static {
        session = null;
    }

    public static DatenbankZugriff getInstance() {
        if (instance == null) {
            instance = new DatenbankZugriff();
        }
        return instance;
    }

    public static DatenbankZugriff removeInstance() {
        logging.logInfo((Object)"Entferne DB instance...");
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
        String dbPort;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            logging.logInfo((Object)"Treiber erfolgreich geladen...");
        }
        catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, " Fehler bei laden des Datenbanktreibers!", "Fehlermeldung", 0);
            logging.logError((Object)"Fehler beim Laden des DB-Treibers");
            logging.logPrintStackTrace((Exception)e);
            System.exit(0);
        }
        if (runApplication.PROPERTIES.get("DB_TYP").equals("SSH")) {
            this.connectSSHServer();
            dbPort = runApplication.PROPERTIES.get("SSHTunnel");
        } else {
            dbPort = runApplication.PROPERTIES.get("DatenbankPort");
        }
        try {
            this.dbConnection = DriverManager.getConnection("jdbc:mysql://" + runApplication.PROPERTIES.get("DatenbankIP") + ":" + dbPort + "/" + runApplication.PROPERTIES.get("DatenbankName"), hash.decodeHashCode((String)runApplication.PROPERTIES.get("DatenbankUser")), hash.decodeHashCode((String)runApplication.PROPERTIES.get("DatenbankPasswort")));
            logging.logInfo((Object)"DB-Verbindung erfolgreich...");
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
        String user = hash.decodeHashCode((String)runApplication.PROPERTIES.get("SSHUser"));
        String password = hash.decodeHashCode((String)runApplication.PROPERTIES.get("SSHPasswort"));
        String host = runApplication.PROPERTIES.get("SSHServer");
        int port = Integer.parseInt(runApplication.PROPERTIES.get("SSHServerPort"));
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            int lokalPort = Integer.parseInt(runApplication.PROPERTIES.get("SSHTunnel"));
            String remoteHost = runApplication.PROPERTIES.get("DatenbankIP");
            int remotePort = Integer.parseInt(runApplication.PROPERTIES.get("DatenbankPort"));
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
        if (runApplication.PROPERTIES.get("DB_TYP").equals("SSH")) {
            logging.logInfo((Object)"Trenne SSH Verbindung");
            session.disconnect();
            logging.logInfo((Object)"SSH Verbindung erfolgreich getrennt");
        }
    }
}

