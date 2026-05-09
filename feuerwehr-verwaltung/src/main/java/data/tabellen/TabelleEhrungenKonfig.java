/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Ehrungen;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleEhrungenKonfig {
    public void insert(Ehrungen ehrungen) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO ehrungen_konfiguration (`id`,`ehrungID`, `zeit`, `mandantID`) VALUES ('" + ehrungen.getId() + "', '" + ehrungen.getEhrungID() + "', '" + ehrungen.getZeit() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Ehrungen ehrungen) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update ehrungen_konfiguration set zeit = " + ehrungen.getZeit() + " where ehrungID = " + ehrungen.getEhrungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM ehrungen_konfiguration;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM ehrungen_konfiguration;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCount(int ehrungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getZeit(int ehrungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT zeit FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT zeit FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

