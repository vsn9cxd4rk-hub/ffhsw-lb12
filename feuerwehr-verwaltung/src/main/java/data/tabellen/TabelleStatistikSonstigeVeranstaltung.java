/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.StatistikSonstigeVeranstaltung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleStatistikSonstigeVeranstaltung {
    public void insert(StatistikSonstigeVeranstaltung statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO statistiksonstigeveranstaltung (`id`, `veranstaltungID`, `kategorie`, `jahr`, `dauer`, `mannstunden` , `wochentag` , `mandantID`) VALUES ('" + statistik.getId() + "', '" + statistik.getVeranstaltungID() + "', '" + statistik.getKategorie() + "', '" + statistik.getJahr() + "', '" + statistik.getDauer() + "', '" + statistik.getMannstunden() + "', '" + statistik.getWochentag() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(StatistikSonstigeVeranstaltung statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update statistiksonstigeveranstaltung set jahr = '" + statistik.getJahr() + "', kategorie = '" + statistik.getKategorie() + "', dauer = '" + statistik.getDauer() + "', mannstunden = '" + statistik.getMannstunden() + "', wochentag = '" + statistik.getWochentag() + "' where veranstaltungID = " + statistik.getVeranstaltungID() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateMannstunden(StatistikSonstigeVeranstaltung statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update statistiksonstigeveranstaltung set mannstunden = " + statistik.getMannstunden() + " where veranstaltungID = " + statistik.getVeranstaltungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOne(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from statistiksonstigeveranstaltung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM statistiksonstigeveranstaltung;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM statistiksonstigeveranstaltung;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getDauer(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dauer FROM statistiksonstigeveranstaltung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dauer FROM statistiksonstigeveranstaltung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getKategorieCount(int kategorie, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistiksonstigeveranstaltung where kategorie = " + kategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistiksonstigeveranstaltung where kategorie = " + kategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public double getZusammengerechneteDauer(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(dauer) FROM statistiksonstigeveranstaltung where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(dauer) FROM statistiksonstigeveranstaltung where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getDouble(1) / 60.0;
        }
        return 0.0;
    }

    public int getAnzahlProJahr(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM statistiksonstigeveranstaltung where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM statistiksonstigeveranstaltung where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int[] getBeteiligungsdauer(int mitgliedID, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(sv.dauer), sum(se.dauer), sum(sb.dauer) FROM anwesenheit a LEFT JOIN statistiksonstigeveranstaltung sv ON a.veranstaltungID = sv.veranstaltungID LEFT JOIN statistikeinsatz se ON a.veranstaltungID = se.veranstaltungID LEFT JOIN statistikbsw sb ON a.veranstaltungID = sb.veranstaltungID where a.mitgliederID = " + mitgliedID + " and a.jahr = " + jahr + " and sv.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(sv.dauer), sum(se.dauer), sum(sb.dauer) FROM anwesenheit a LEFT JOIN statistiksonstigeveranstaltung sv ON a.veranstaltungID = sv.veranstaltungID LEFT JOIN statistikeinsatz se ON a.veranstaltungID = se.veranstaltungID LEFT JOIN statistikbsw sb ON a.veranstaltungID = sb.veranstaltungID where a.mitgliederID = " + mitgliedID + " and a.jahr = " + jahr + " and sv.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        int[] liste = new int[3];
        while (result.next()) {
            int i = 0;
            while (i < 3) {
                liste[i] = result.getInt(i + 1);
                ++i;
            }
        }
        return liste;
    }
}

