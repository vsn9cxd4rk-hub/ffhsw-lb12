/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Organisation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleOrganisationen {
    public void insert(Organisation organisation) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO organisationen (`id`, `name` , `sortierung` , `mandantID`) VALUES ('" + organisation.getId() + "', '" + organisation.getName() + "', '" + organisation.getSortierung() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Organisation organisation) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update organisationen set name = '" + organisation.getName() + "',  sortierung = " + organisation.getSortierung() + " where id = " + organisation.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from organisationen where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM organisationen where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT max(id) FROM organisationen where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public String getOrganisationName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM organisationen where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name FROM organisationen where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAllOrganisationenWithout1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM organisationen where id > 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung, id;");
        logging.logSQL((Object)("SELECT name FROM organisationen where id > 1 mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung, id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllOrganisationenIDsWithout1() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM organisationen where id > 1 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
        logging.logSQL((Object)("SELECT id FROM organisationen where id > 1 mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung, id;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllOrganisationenIDs() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM organisationen where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM organisationen where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getOrganisationID(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM organisationen where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM organisationen where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getSortierung(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sortierung FROM organisationen where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sortierung FROM organisationen where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfSortierung(int sorterungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM organisationen where sortierung = " + sorterungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM organisationen where sortierung = " + sorterungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getOrganisationenCount(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM organisationen where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM organisationen where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public Organisation getData(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id, name, sortierung FROM organisationen where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id, name, sortierung FROM organisationen where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        Organisation organisation = new Organisation();
        while (result.next()) {
            organisation.setId(result.getInt("id"));
            organisation.setName(result.getString("name"));
            organisation.setSortierung(result.getInt("sortierung"));
        }
        return organisation;
    }
}

