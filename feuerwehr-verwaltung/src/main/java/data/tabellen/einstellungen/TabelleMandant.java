/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import data.DatenbankZugriffGrundkonfiguration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleMandant {
    public void update(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mandant set name = '" + name + "' where id = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getMandantID(String mandantName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM mandant where name = '" + mandantName + "';"));
        ResultSet result = statement.executeQuery("SELECT id FROM mandant where name = '" + mandantName + "';");
        if (result.next()) {
            return result.getInt(1);
        }
        return 1;
    }

    public String getMandantName(int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM mandant where id = '" + mandantID + "';");
        logging.logSQL((Object)("SELECT name FROM mandant where id = '" + mandantID + "';"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriffGrundkonfiguration.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM mandant;");
        logging.logSQL((Object)"SELECT max(id) FROM mandant;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 1;
    }

    public int getBFStatus() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT bf FROM mandant where id = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT bf FROM mandant where id = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 1;
    }

    public ArrayList<String> getAllMandanten() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT name FROM mandant order by name;");
        ResultSet result = statement.executeQuery("SELECT name FROM mandant order by name;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }
}

