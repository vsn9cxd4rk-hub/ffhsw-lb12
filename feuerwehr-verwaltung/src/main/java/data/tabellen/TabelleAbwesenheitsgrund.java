/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Abwesenheitsgrund;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import logging.logging;
import run.runApplication;

public class TabelleAbwesenheitsgrund {
    public void insert(Abwesenheitsgrund grund) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO abwesenheitsgrund (`id`, `name`, `kurzName`, `mandantID`) VALUES ('" + grund.getId() + "', '" + grund.getName() + "', '" + grund.getNameKurz() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM abwesenheitsgrund;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM abwesenheitsgrund;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public ArrayList<String> getAlleGruende() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name from abwesenheitsgrund where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id"));
        ResultSet result = statement.executeQuery("SELECT name from abwesenheitsgrund where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleGruendID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id from abwesenheitsgrund where mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by id"));
        ResultSet result = statement.executeQuery("SELECT id from abwesenheitsgrund where mandantID = " + runApplication.PROPERTIES.get("MandantID") + "  order by id");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getAbwesenheitsGrundID(String grund) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM abwesenheitsgrund where name = '" + grund + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM abwesenheitsgrund where name = '" + grund + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public HashMap<String, Integer> getAbwesenheitsGrundMap() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name, id FROM abwesenheitsgrund where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT name, id FROM abwesenheitsgrund where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        while (result.next()) {
            map.put(result.getString(1), result.getInt(2));
        }
        return map;
    }

    public String getAbwesenheitsGrundbyID(int grund) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM abwesenheitsgrund where id = '" + grund + "';"));
        ResultSet result = statement.executeQuery("SELECT name FROM abwesenheitsgrund where id = '" + grund + "';");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }
}

