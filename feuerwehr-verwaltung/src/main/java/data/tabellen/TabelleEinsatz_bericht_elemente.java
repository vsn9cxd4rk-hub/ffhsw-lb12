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
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleEinsatz_bericht_elemente {
    public ArrayList<String> getElemente(String gruppe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM einsatz_bericht_elemente where gruppe = '" + gruppe + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT name FROM einsatz_bericht_elemente where gruppe = '" + gruppe + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getID(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM einsatz_bericht_elemente where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM einsatz_bericht_elemente where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM einsatz_bericht_elemente where id = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT name FROM einsatz_bericht_elemente where id = '" + id + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }
}

