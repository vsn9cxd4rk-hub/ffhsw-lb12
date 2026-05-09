/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.M\u00e4ngelmeldung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleMaengelmeldung {
    public void insert(M\u00e4ngelmeldung mangel) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO maengelmeldung (`id`, `jahr`, `mitgliedID`, `fahrzeugID`, `datum`, `wann`, `beschreibung`, `dateiname`, `status`, `mandantID`) VALUES ('" + mangel.getId() + "', '" + mangel.getJahr() + "', '" + mangel.getMitgliedID() + "', '" + mangel.getFahrzeugID() + "', '" + mangel.getDatum() + "', '" + mangel.getWann() + "', '" + mangel.getBeschreibung() + "', '" + mangel.getDateiname() + "', '" + mangel.getStatus() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void updateStatus(int id, int status) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update maengelmeldung set status = " + status + " where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM maengelmeldung;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM maengelmeldung;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public ArrayList<String> getWann(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT wann FROM maengelmeldung where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT wann FROM maengelmeldung where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getMaengelmeldungWithStatus(int status, int jahr, String mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM maengelmeldung where status = " + status + " and jahr = " + jahr + " and mandantID = " + mandantID + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM maengelmeldung where status = " + status + " and jahr = " + jahr + " and mandantID = " + mandantID + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add("Mangel-ID" + result.getString(1));
        }
        return liste;
    }

    public String getDateiname(String wann) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dateiname FROM maengelmeldung where wann = '" + wann + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dateiname FROM maengelmeldung where wann = '" + wann + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getDateinameByID(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dateiname FROM maengelmeldung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dateiname FROM maengelmeldung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getWannAvailable(String wann) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM maengelmeldung where wann = '" + wann + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM maengelmeldung where wann = '" + wann + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getMaengelmeldungForInformation(int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String partOfStatement = mandantID == 0 ? "" : "and mandantID = " + runApplication.PROPERTIES.get("MandantID");
        logging.logSQL((Object)("SELECT id, beschreibung FROM maengelmeldung where status = 0 " + partOfStatement + ";"));
        ResultSet result = statement.executeQuery("SELECT id, beschreibung FROM maengelmeldung where status = 0 " + partOfStatement + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            String beschreibung = result.getString(2).length() >= 23 ? String.valueOf(result.getString(2).substring(0, 23)) + "..." : result.getString(2);
            liste.add("Mangel-ID" + result.getString(1) + " - " + beschreibung);
        }
        return liste;
    }
}

