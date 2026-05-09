/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Ausbildung_Kategorie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleAusbildung_Kategorie {
    public void insert(Ausbildung_Kategorie kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO ausbildung_kategorie (`id`, `name`, `mandantID`) VALUES ('" + kategorie.getId() + "', '" + kategorie.getName() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Ausbildung_Kategorie kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update ausbildung_kategorie set name = '" + kategorie.getName() + "' where id = " + kategorie.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from ausbildung_kategorie where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM ausbildung_kategorie;");
        logging.logSQL((Object)"SELECT max(id) FROM ausbildung_kategorie;");
        if (result.next()) {
            if (result.getInt(1) <= 10) {
                return 11;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getID(String kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM ausbildung_kategorie where name = '" + kategorie + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM ausbildung_kategorie where name = '" + kategorie + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getNameByID(int kategorieID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM ausbildung_kategorie where id = " + kategorieID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name FROM ausbildung_kategorie where id = " + kategorieID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAllKategorien() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM ausbildung_kategorie where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT name FROM ausbildung_kategorie where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllKategorienID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM ausbildung_kategorie where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT id FROM ausbildung_kategorie where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getCount(String kategorieName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ausbildung_kategorie where name = '" + kategorieName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM ausbildung_kategorie where name = '" + kategorieName + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

