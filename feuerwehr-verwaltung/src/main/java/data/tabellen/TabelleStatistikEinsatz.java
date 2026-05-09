/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.StatistikEinsatz;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleStatistikEinsatz {
    public void insert(StatistikEinsatz statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO statistikEinsatz (`id`, `veranstaltungID`, `einsatzID`, `jahr`, `stichwort`, `kategorie`, `ausrueckezeit`, `dauer`, `dauerAlarmfahrt`, `mannstunden`, `wochentag`, `mandantID`) VALUES ('" + statistik.getId() + "', '" + statistik.getVeranstaltungID() + "', '" + statistik.getEinsatzID() + "', '" + statistik.getJahr() + "', '" + statistik.getStichwort() + "', '" + statistik.getKategorie() + "', '" + statistik.getAusrueckezeit() + "', '" + statistik.getDauer() + "', '" + statistik.getDauerAlarmfahrt() + "', '" + statistik.getMannstunden() + "', '" + statistik.getWochentag() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(StatistikEinsatz statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update statistikEinsatz set stichwort = '" + statistik.getStichwort() + "', jahr = '" + statistik.getJahr() + "', kategorie = '" + statistik.getKategorie() + "', ausrueckezeit = '" + statistik.getAusrueckezeit() + "', dauer = '" + statistik.getDauer() + "', dauerAlarmfahrt = '" + statistik.getDauerAlarmfahrt() + "', mannstunden = '" + statistik.getMannstunden() + "', wochentag = '" + statistik.getWochentag() + "' where veranstaltungID = " + statistik.getVeranstaltungID() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOne(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from statistikEinsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM statistikEinsatz;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM statistikEinsatz;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getKategorieCount(int kategorie, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikEinsatz where kategorie = " + kategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikEinsatz where kategorie = " + kategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public double getZusammengerechneteAusrueckezeit(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(ausrueckezeit) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(ausrueckezeit) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getDouble(1);
        }
        return 0.0;
    }

    public double getZusammengerechneteAlarmfahrtZeiten(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(dauerAlarmfahrt) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(dauerAlarmfahrt) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getDouble(1);
        }
        return 0.0;
    }

    public double getZusammengerechneteEinsatzdauer(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(dauer) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(dauer) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getDouble(1) / 60.0;
        }
        return 0.0;
    }

    public int getZusammengerechneteMannstunden(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(mannstunden) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(mannstunden) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1) / 60;
        }
        return 0;
    }

    public int getAnzahlProJahr(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getAnzahlProJahrOnlyForAlarmfahrtdauer(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and dauerAlarmfahrt != 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and dauerAlarmfahrt != 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getAnzahlProJahrOnlyForAusrueckzeit(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and ausrueckezeit != 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and ausrueckezeit != 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<Integer> getAllJahreInDB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT jahr FROM `statistikeinsatz` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by jahr;");
        logging.logSQL((Object)("SELECT jahr FROM `statistikeinsatz` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by jahr;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getDauer(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dauer FROM statistikeinsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dauer FROM statistikeinsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfDauerKleinerWert(int wert, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikeinsatz where dauer <= " + wert + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where dauer <= " + wert + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfDauerGr\u00f6\u00dferWert(int wert, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikeinsatz where dauer >= " + wert + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where dauer >= " + wert + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getEinsatzProWochentag(int jahr, int wochentag) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikeinsatz WHERE jahr = " + jahr + " and wochentag = " + wochentag + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz WHERE jahr = " + jahr + " and wochentag = " + wochentag + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public void updateMannstunden(StatistikEinsatz statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update statistikeinsatz set mannstunden = " + statistik.getMannstunden() + " where veranstaltungID = " + statistik.getVeranstaltungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public ArrayList<Integer> getAlarmfahrten(String jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT dauerAlarmfahrt FROM `statistikeinsatz` where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;");
        logging.logSQL((Object)("SELECT dauerAlarmfahrt FROM `statistikeinsatz` where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAusrueckezeiten(String jahre) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ausrueckezeit FROM `statistikeinsatz` where jahr = " + jahre + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;");
        logging.logSQL((Object)("SELECT ausrueckezeit FROM `statistikeinsatz` where jahr = " + jahre + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }
}

