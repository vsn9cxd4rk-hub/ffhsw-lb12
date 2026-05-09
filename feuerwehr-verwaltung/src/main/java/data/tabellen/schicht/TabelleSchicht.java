/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.schicht;

import data.DatenbankZugriff;
import go.schicht.Schicht;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleSchicht {
    public void insert(Schicht schicht) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO schicht (`id`, `jahr`, `name`, `datumVon`, `uhrVon`, `datumBis`, `uhrBis`, `von`, `bis`, `mandantID`) VALUES ('" + schicht.getId() + "', '" + schicht.getJahr() + "', '" + schicht.getName() + "', '" + schicht.getSchichtStartDatum() + "', '" + schicht.getSchichtStartUhrzeit() + "', '" + schicht.getSchichtEndeDatum() + "', '" + schicht.getSchichtEndeUhrzeit() + "', '" + schicht.getMinutenVon() + "', '" + schicht.getMinutenBis() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM schicht;");
        logging.logSQL((Object)"SELECT max(id) FROM schicht;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getSchichtID(String schichtName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM schicht where name = '" + schichtName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM schicht where name = '" + schichtName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getAllSchichten() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM schicht where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;");
        logging.logSQL((Object)("SELECT name FROM schicht where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllSchichtenEinesMonats(String monat, String jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM schicht where datumVon between '" + jahr + "-" + monat + "-01' and '" + jahr + "-" + monat + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;");
        logging.logSQL((Object)("SELECT name FROM schicht where datumVon between '" + jahr + "-" + monat + "-01' and '" + jahr + "-" + monat + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getSchichtIDF\u00fcrEreignis(String datum, int zeitInMinuten) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM schicht where datumVon = '" + datum + "' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM schicht where datumVon = '" + datum + "' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getSchichtIDF\u00fcrEreignis2(String datum, int zeitInMinuten) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM schicht where datumBis = '" + datum + "' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM schicht where datumBis = '" + datum + "' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

