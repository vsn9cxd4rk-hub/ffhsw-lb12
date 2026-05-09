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
import java.util.HashMap;
import logging.logging;
import run.runApplication;

public class TabelleEinstellungen {
    public String getEinstellungen(String key) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT wert FROM einstellungen where `key` = '" + key + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT wert FROM einstellungen where `key` = '" + key + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getEinstellungenWithMandantID(String key, String mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT wert FROM einstellungen where `key` = '" + key + "' and mandantID = " + mandantID + ";"));
        ResultSet result = statement.executeQuery("SELECT wert FROM einstellungen where `key` = '" + key + "' and mandantID = " + mandantID + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public void update(String key, String wert) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update einstellungen set wert = '" + wert + "' where `key` = '" + key + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public HashMap<String, String> getAllEinstellungen() throws SQLException {
        ResultSet result;
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        if (this.getVersion().startsWith("Version: 1.")) {
            logging.logSQL((Object)"SELECT `key`, wert FROM einstellungen;");
            result = statement.executeQuery("SELECT `key`, wert FROM einstellungen;");
        } else {
            logging.logSQL((Object)("SELECT `key`, wert FROM einstellungen where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
            result = statement.executeQuery("SELECT `key`, wert FROM einstellungen where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        }
        HashMap<String, String> map = new HashMap<String, String>();
        logging.logInfo((Object)"Liste der Einstellungen:");
        while (result.next()) {
            map.put(result.getString(1), result.getString(2));
        }
        return map;
    }

    public String getVersion() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT wert FROM einstellungen where `key` = 'version';");
        ResultSet result = statement.executeQuery("SELECT wert FROM einstellungen where `key` = 'version';");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }
}

