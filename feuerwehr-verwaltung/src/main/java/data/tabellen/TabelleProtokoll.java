/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Protokoll;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleProtokoll {
    public void insert(Protokoll protokoll) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO protokoll (`id`,`veranstaltungID`, `jahr`, `title`, `protokolltext`, `erstelldatum`, `mandantID`) VALUES ('" + protokoll.getId() + "', '" + protokoll.getVeranstaltungID() + "', '" + protokoll.getJahr() + "', '" + protokoll.getTitle() + "', '" + protokoll.getProtokolltext() + "', '" + protokoll.getErstelldatum() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Protokoll protokoll) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update protokoll set protokolltext = '" + protokoll.getProtokolltext() + "',  erstelldatum = '" + protokoll.getErstelldatum() + "',  title = '" + protokoll.getTitle() + "' where veranstaltungID = " + protokoll.getVeranstaltungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM protokoll;");
        logging.logSQL((Object)"SELECT max(id) FROM protokoll;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCount(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getProtokoll(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT protokolltext FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT protokolltext FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return "";
    }

    public Protokoll getData(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT * FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT * FROM protokoll where veranstaltungID = " + veranstaltungID + " and  mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            Protokoll protokoll = new Protokoll();
            protokoll.setId(result.getInt(1));
            protokoll.setVeranstaltungID(result.getInt(2));
            protokoll.setJahr(result.getInt(3));
            protokoll.setTitle(result.getString(4));
            protokoll.setProtokolltext(result.getString(5));
            protokoll.setErstelldatum(result.getString(6));
            return protokoll;
        }
        return null;
    }

    public ArrayList<String> getAlleTitel(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT v.name, p.title FROM protokoll p LEFT JOIN veranstaltung v ON p.veranstaltungID = v.id where p.jahr = " + jahr + " and v.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT v.name, p.title FROM protokoll p LEFT JOIN veranstaltung v ON p.veranstaltungID = v.id where p.jahr = " + jahr + " and v.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + " - " + result.getString(2));
        }
        return liste;
    }
}

