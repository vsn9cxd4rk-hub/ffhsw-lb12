/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.statistik;

import data.DatenbankZugriff;
import go.StatistikMitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleStatistikMitglieder {
    public void insert(StatistikMitglieder statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO statistikMitglieder (`id`, `jahr`, `alterGes`, `anzahl`, `erstellung`, `mandantID`) VALUES ('" + statistik.getId() + "', '" + statistik.getJahr() + "', '" + statistik.getAlter() + "', '" + statistik.getAnzahl() + "', '" + statistik.getErstellung() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM statistikMitglieder;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM statistikMitglieder;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public ArrayList<Integer> getAllJahreInDB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT jahr FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;"));
        ResultSet result = statement.executeQuery("SELECT jahr FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlter() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT alterGes FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;"));
        ResultSet result = statement.executeQuery("SELECT alterGes FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAnzahl() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT anzahl FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;"));
        ResultSet result = statement.executeQuery("SELECT anzahl FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getErstellung() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT erstellung FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;"));
        ResultSet result = statement.executeQuery("SELECT erstellung FROM `statistikMitglieder` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }
}

