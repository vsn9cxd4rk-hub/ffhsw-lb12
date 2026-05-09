/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleLogbuch {
    public static Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Nummer");
            this.add("Datum");
            this.add("Zeit");
            this.add("User");
            this.add("Aktion");
        }
    };

    public void insert(int id, String datum, String zeit, String user, String aktion) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO logbuch (`id`, `datum`, `zeit`, `user`, `aktion`, `mandantID`) VALUES ('" + id + "', '" + datum + "', '" + zeit + "', '" + user + "', '" + aktion + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM logbuch;");
        logging.logSQL((Object)"SELECT max(id) FROM logbuch;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM logbuch where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM logbuch where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public void delete() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from logbuch where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)("delete from logbuch where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        statement.executeUpdate(sql);
    }

    public Vector<Vector<String>> getAll() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM logbuch where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT * FROM logbuch where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public Vector<Vector<String>> getFilterByUser(String user) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM logbuch where user = '" + user + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT * FROM `logbuch` where user = '" + user + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> logbuch = new Vector<String>();
        logbuch.add(Integer.toString(result.getInt("id")));
        logbuch.add(result.getString("Datum"));
        logbuch.add(result.getString("Zeit"));
        logbuch.add(result.getString("User"));
        logbuch.add(result.getString("Aktion"));
        return logbuch;
    }
}

