/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleKeyStore {
    public String get(String key) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT wert FROM keystore where `key` = '" + key + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT wert FROM keystore where `key` = '" + key + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public void update(String key, String wert) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update keystore set wert = '" + wert + "' where `key` = '" + key + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + "';";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void insert(String key, String wert, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO `keystore` (`key`, `wert` , `mandantID`) VALUES ('" + key + "', '" + wert + "', " + mandantID + ");";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int count(String key, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM keystore where `key` = '" + key + "' and mandantID = " + mandantID + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM keystore where `key` = '" + key + "' and mandantID = " + mandantID + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

