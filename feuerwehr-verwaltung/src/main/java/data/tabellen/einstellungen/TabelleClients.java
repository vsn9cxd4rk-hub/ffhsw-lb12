/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import go.Clients;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleClients {
    public void insert(Clients clients) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO clients (`id`,`clientID`, `alias`, `typ`, `online`, `zugelassen`, `mandantID`) VALUES ('" + clients.getId() + "', '" + clients.getClientID() + "', '" + clients.getAlias() + "', '" + clients.getTyp() + "', '" + clients.getOnline() + "', '" + clients.getZugelassen() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void updateZugelassen(Clients clients) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update clients set zugelassen = " + clients.getZugelassen() + " where clientID = '" + clients.getClientID() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateOnline(int onlineStatus) {
        try {
            Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
            String sql = "Update clients set online = " + onlineStatus + " where clientID = '" + runApplication.clientID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
            logging.logSQL((Object)sql);
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            logging.logError((Object)"Online Status kann nicht gesetzt werden!");
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM clients;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM clients;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getZugelassenStatus(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT zugelassen FROM clients where clientID = '" + clientID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT zugelassen FROM clients where clientID = '" + clientID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountClientID(String clientID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM clients where clientID = '" + clientID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM clients where clientID = '" + clientID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public Vector<Vector<String>> getAllForTable() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT clientID, alias, typ, online, zugelassen from clients where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT clientID, alias, typ, online, zugelassen from clients where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<String> getAllIDs() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("Select clientID from clients where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("Select clientID from clients where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> clients = new Vector<String>();
        clients.add(result.getString("clientID"));
        clients.add(result.getString("alias"));
        clients.add(result.getString("typ"));
        if (result.getInt("online") == 0) {
            clients.add("Offline");
        } else {
            clients.add("Online");
        }
        if (result.getInt("zugelassen") == 0) {
            clients.add("NICHT Zugelassen");
        } else {
            clients.add("Zugelassen");
        }
        return clients;
    }
}

