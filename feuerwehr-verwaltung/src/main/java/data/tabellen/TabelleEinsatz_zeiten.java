/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Einsatz_zeiten;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;

public class TabelleEinsatz_zeiten {
    public void insert(Einsatz_zeiten zeiten) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO einsatz_zeiten (`id`, `jahr`, `veranstaltungID`, `einsatzID`, `fahrzeugID` ,`ZeitAlarm` , `ZeitAusgerueckt`, `ZeitEingetroffen`, `ZeitEingerueckt`, `mandantID`) VALUES ('" + zeiten.getId() + "', '" + zeiten.getJahr() + "', '" + zeiten.getVeranstaltungID() + "', '" + zeiten.getEinsatznummer() + "', '" + zeiten.getFahrzeugID() + "', '" + zeiten.getZeitAlarm() + "', '" + zeiten.getZeitAusgerueckt() + "', '" + zeiten.getZeitEingetroffen() + "', '" + zeiten.getZeitEingerueckt() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Einsatz_zeiten zeiten) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update einsatz_zeiten set jahr = '" + zeiten.getJahr() + "', zeitAlarm = '" + zeiten.getZeitAlarm() + "', zeitAusgerueckt = '" + zeiten.getZeitAusgerueckt() + "', zeitEingetroffen = '" + zeiten.getZeitEingetroffen() + "', zeitEingerueckt = '" + zeiten.getZeitEingerueckt() + "' where veranstaltungID = " + zeiten.getVeranstaltungID() + " and fahrzeugID = " + zeiten.getFahrzeugID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(int veransatltungID, int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from einsatz_zeiten where veranstaltungID = " + veransatltungID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteALL(int veransatltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from einsatz_zeiten where veranstaltungID = " + veransatltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM `einsatz_zeiten`;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM `einsatz_zeiten`;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCountEingesetzterFahrzeuge(int fahrzeugID, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM einsatz_zeiten where fahrzeugID = " + fahrzeugID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM einsatz_zeiten where fahrzeugID = " + fahrzeugID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCount(int fahrzeugID, int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM einsatz_zeiten where fahrzeugID = " + fahrzeugID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM einsatz_zeiten where fahrzeugID = " + fahrzeugID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountGesamtFahrzeuge(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM einsatz_zeiten where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM einsatz_zeiten where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public HashMap<String, String> getData(int veranstaltungID, int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT * FROM einsatz_zeiten where veranstaltungID = " + veranstaltungID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT * FROM einsatz_zeiten where veranstaltungID = " + veranstaltungID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("zeitAlarm", result.getString("zeitAlarm"));
            map.put("zeitEingetroffen", result.getString("zeitEingetroffen"));
            map.put("zeitEingerueckt", result.getString("zeitEingerueckt"));
            map.put("zeitAusgerueckt", result.getString("zeitAusgerueckt"));
            map.put("fahrzeugID", result.getString("fahrzeugID"));
        }
        return map;
    }

    public ArrayList<Integer> getFahrzeugListe(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT fahrzeugID FROM einsatz_zeiten where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT fahrzeugID FROM einsatz_zeiten where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public Vector<Vector<String>> getZeitenForTabelle(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT f.name as fahrzeugname, ez.zeitAlarm, ez.zeitAusgerueckt, ez.zeitEingetroffen, ez.zeitEingerueckt, se.dauer, se.mannstunden FROM einsatz_zeiten ez LEFT JOIN fahrzeuge f ON ez.fahrzeugID = f.id LEFT JOIN statistikeinsatz se ON ez.veranstaltungID = se.veranstaltungID  WHERE ez.veranstaltungID = " + veranstaltungID + " and ez.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT f.name as fahrzeugname, ez.zeitAlarm, ez.zeitAusgerueckt, ez.zeitEingetroffen, ez.zeitEingerueckt, se.dauer, se.mannstunden FROM einsatz_zeiten ez LEFT JOIN fahrzeuge f ON ez.fahrzeugID = f.id LEFT JOIN statistikeinsatz se ON ez.veranstaltungID = se.veranstaltungID  WHERE ez.veranstaltungID = " + veranstaltungID + " and ez.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> zeitenListe = new Vector<String>();
        zeitenListe.add(result.getString("fahrzeugname"));
        zeitenListe.add(result.getString("zeitAlarm"));
        zeitenListe.add(result.getString("zeitAusgerueckt"));
        zeitenListe.add(result.getString("zeitEingetroffen"));
        zeitenListe.add(result.getString("zeitEingerueckt"));
        zeitenListe.add(TimeCalculation.minutenInStundenUmrechnen(result.getInt("dauer")));
        zeitenListe.add(String.valueOf(TimeCalculation.minutenInStundenUmrechnen(result.getInt("mannstunden"))) + " Std.");
        return zeitenListe;
    }
}

