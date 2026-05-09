/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen.urlaubsplaner;

import data.DatenbankZugriff;
import go.urlaub.Urlaub;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleUrlaub {
    public void insert(Urlaub urlaub) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO urlaub (`id`, `jahr`, `mitgliederID`, `datumVon`, `datumBis`, `loeschkenner`, `mandantID`) VALUES ('" + urlaub.getId() + "', '" + urlaub.getJahr() + "', '" + urlaub.getMitgliederID() + "', '" + urlaub.getDatumVon() + "', '" + urlaub.getDatumBis() + "', '" + urlaub.getLoeschkenner() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void updateLoeschkenner(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update urlaub set loeschkenner = 1 where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM urlaub;");
        logging.logSQL((Object)"SELECT max(id) FROM urlaub;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public ArrayList<String> getMitgliederMitUrlaubByDatum(String datum) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT mitgliederID FROM `urlaub` where datumVon = '" + datum + "' or datumBis = '" + datum + "' or datumVon <= '" + datum + "' and datumBis >= '" + datum + "' and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT mitgliederID FROM `urlaub` where datumVon = '" + datum + "' or datumBis = '" + datum + "' or datumVon <= '" + datum + "' and datumBis >= '" + datum + "' and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getUrlaubsliste() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname, u.datumVon, u.datumBis, u.jahr FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.datumBis >= '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and u.loeschkenner = 0 and u.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, u.datumVon, u.datumBis, u.jahr FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.datumBis >= '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and u.loeschkenner = 0 and u.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;");
        ArrayList<String> liste = new ArrayList<String>();
        int aktJahr = Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"));
        while (result.next()) {
            if (result.getInt(5) > aktJahr) continue;
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2) + " (von: " + TimeCalculation.parseDateForGUI(result.getString(3)) + " bis: " + TimeCalculation.parseDateForGUI(result.getString(4)) + ")");
        }
        return liste;
    }

    public ArrayList<Integer> getIDListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM urlaub where datumBis > '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and loeschkenner= 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datumVon;"));
        ResultSet result = statement.executeQuery("SELECT id FROM urlaub where datumBis > '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and loeschkenner= 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datumVon;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        liste.add(0);
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public Vector<Vector<String>> getAllForList(String jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname, u.datumVon, u.datumBis FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.jahr = " + jahr + " and u.loeschkenner = 0 and u.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, u.datumVon, u.datumBis FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.jahr = " + jahr + " and u.loeschkenner = 0 and u.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> liste = new Vector<String>();
        liste.add(String.valueOf(result.getString("name")) + ", " + result.getString("vorname"));
        liste.add(TimeCalculation.parseDateForGUI(result.getString("datumVon")));
        liste.add(TimeCalculation.parseDateForGUI(result.getString("datumBis")));
        return liste;
    }
}

