/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Jahresbericht;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleJahresbericht {
    public void insert(Jahresbericht bericht) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO jahresberichte (`id`,`jahr`, `title`, `bericht`, `erstelldatum`, `autoBericht`, `dateiname`, `statistiken`, `mandantID`) VALUES ('" + bericht.getId() + "', '" + bericht.getJahr() + "', '" + bericht.getTitle() + "', '" + bericht.getBericht() + "', '" + bericht.getErstelldatum() + "', '" + bericht.getAutoBericht() + "', '" + bericht.getDateiname() + "', '" + bericht.getStatistiken() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM jahresberichte;");
        logging.logSQL((Object)"SELECT max(id) FROM jahresberichte;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public String getBerichtDateiname(String title) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT dateiname FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT dateiname FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAllVerf\u00fcgbarenBerichte(int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT title FROM jahresberichte where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;");
        logging.logSQL((Object)("SELECT title FROM jahresberichte where jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllTitle() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT title FROM jahresberichte where autoBericht = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;");
        logging.logSQL((Object)("SELECT title FROM jahresberichte where autoBericht = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public String getBericht(String title) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT bericht FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;");
        logging.logSQL((Object)("SELECT bericht FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getJahrOfBericht(String title) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT jahr FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;");
        logging.logSQL((Object)("SELECT jahr FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int[] getSelectedStatistiken(String title) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT statistiken FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;");
        logging.logSQL((Object)("SELECT statistiken FROM jahresberichte where title = '" + title + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by title;"));
        int[] intListe = null;
        while (result.next()) {
            if (result.getString(1).equals("leer")) {
                logging.logSQL((Object)"StatistikenListe ist leer!");
                return intListe;
            }
            try {
                String[] liste = result.getString(1).split(",");
                intListe = new int[liste.length];
                int i = 0;
                while (i < liste.length) {
                    intListe[i] = Integer.parseInt(liste[i]);
                    System.out.println(String.valueOf(intListe[i]) + " = " + Integer.parseInt(liste[i]));
                    ++i;
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return intListe;
    }
}

