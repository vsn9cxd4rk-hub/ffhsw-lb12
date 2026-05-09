/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.bestandsliste;

import data.DatenbankZugriff;
import go.bestandsliste.Lager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleLager {
    public void insert(Lager lager) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO lager (`id`,`name`,`verantwortlicher` , `mandantID`) VALUES ('" + lager.getId() + "', '" + lager.getName() + "', '" + lager.getVerantwortlicher() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM lager where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT max(id) FROM lager where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            if (result.getInt(1) <= 8999) {
                return result.getInt(1) + 9000;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public String getLagerName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM lager where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name FROM lager where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAllLager() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM lager where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT name FROM lager where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getLagerID(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lager where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM lager where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getLagerCount(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM lager where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM lager where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getBestandsliste(String gruppe, int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT a.name, max(z.anzahl), z.ort FROM lager_zugewiesen z LEFT JOIN lager_artikel a ON a.id = z.artikelID where z.gruppe = '" + gruppe + "' and z.mitgliedID = " + mitgliedID + " and z.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by a.name, z.ort order by z.ort, a.name;");
        logging.logSQL((Object)("SELECT a.name, max(z.anzahl), z.ort FROM lager_zugewiesen z LEFT JOIN lager_artikel a ON a.id = z.artikelID where z.gruppe = '" + gruppe + "' and z.mitgliedID = " + mitgliedID + " and z.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by a.name, z.ort order by z.ort, a.name;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (result.getString(3).equals("")) {
                liste.add(String.valueOf(result.getString(1)) + " (" + result.getInt(2) + "x)");
                continue;
            }
            liste.add(String.valueOf(result.getString(1)) + " (" + result.getInt(2) + "x)" + " - " + result.getString(3));
        }
        return liste;
    }
}

