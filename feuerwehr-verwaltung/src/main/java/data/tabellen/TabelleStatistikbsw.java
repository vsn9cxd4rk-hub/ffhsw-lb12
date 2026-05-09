/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.StatistikBSW;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleStatistikbsw {
    public void insert(StatistikBSW statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO statistikbsw (`id`, `veranstaltungID`, `bswID`, `jahr`,`dauer`, `mannstunden`, `wochentag`, `mandantID`) VALUES ('" + statistik.getId() + "', '" + statistik.getVeranstaltungID() + "', '" + statistik.getBswID() + "', '" + statistik.getJahr() + "', '" + statistik.getDauer() + "', '" + statistik.getMannstunden() + "', '" + statistik.getWochentag() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(StatistikBSW statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update statistikbsw set jahr = '" + statistik.getJahr() + "', dauer = '" + statistik.getDauer() + "', mannstunden = '" + statistik.getMannstunden() + "', wochentag = '" + statistik.getWochentag() + "' where veranstaltungID = " + statistik.getVeranstaltungID() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOne(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from statistikbsw where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM statistikbsw;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM statistikbsw;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public double getZusammengerechneteBSWdauer(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(dauer) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(dauer) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getDouble(1) / 60.0;
        }
        return 0.0;
    }

    public int getZusammengerechneteBSWMannstunden(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(mannstunden) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(mannstunden) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1) / 60;
        }
        return 0;
    }

    public int getZusammengerechneteBSWMannstunden(int jahr, String monat) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(sb.mannstunden) FROM statistikbsw sb LEFT JOIN veranstaltung v ON v.id = sb.veranstaltungID where sb.jahr = " + jahr + " and v.datum between '" + jahr + "-" + monat + "-01' and '" + jahr + "-" + monat + "-31' and sb.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(sb.mannstunden) FROM statistikbsw sb LEFT JOIN veranstaltung v ON v.id = sb.veranstaltungID where sb.jahr = " + jahr + " and v.datum between '" + jahr + "-" + monat + "-01' and '" + jahr + "-" + monat + "-31' and sb.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1) / 60;
        }
        return 0;
    }

    public int getAnzahlBSWProJahr(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<Integer> getAllJahreInDB() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT jahr FROM `statistikbsw` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by jahr;");
        logging.logSQL((Object)("SELECT jahr FROM `statistikbsw` where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by jahr;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getDauer(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dauer FROM statistikbsw where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dauer FROM statistikbsw where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public void updateMannstunden(StatistikBSW statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update statistikbsw set mannstunden = " + statistik.getMannstunden() + " where veranstaltungID = " + statistik.getVeranstaltungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }
}

