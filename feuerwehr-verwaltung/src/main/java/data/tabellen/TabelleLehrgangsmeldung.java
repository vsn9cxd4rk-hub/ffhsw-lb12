/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Lehrgangsmeldung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleLehrgangsmeldung {
    public void insert(Lehrgangsmeldung meldung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO lehrgangsmeldung (`mitgliedID`, `lehrgang`, `art`, `mandantID`) VALUES ('" + meldung.getId() + "', '" + meldung.getLehrgang() + "', '" + meldung.getArt() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void deleteAll() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from lehrgangsmeldung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getLehrgangsmeldungByLehrgang(String lehrgang) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN lehrgangsmeldung lm ON m.id = lm.mitgliedID where lm.lehrgang = '" + lehrgang + "' and lm.art = 'L'  and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN lehrgangsmeldung lm ON m.id = lm.mitgliedID where lm.lehrgang = '" + lehrgang + "' and lm.art = 'L' and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getBefoerderungen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = 'B' and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = 'B' and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2) + "  |  " + result.getString(3));
        }
        return liste;
    }

    public ArrayList<String> getEhrungen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = 'EH' and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = 'EH' and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2) + "  |  " + result.getString(3));
        }
        return liste;
    }

    public int getCount(String art) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM lehrgangsmeldung where art = '" + art + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM lehrgangsmeldung where art = '" + art + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

