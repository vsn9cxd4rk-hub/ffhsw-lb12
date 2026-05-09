/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Dienstgrad;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleDienstgrad {
    public void insert(Dienstgrad dienstgrad) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO dienstgrad (`id`, `beschreibung`, `beschreibungLang`, `mandantID`) VALUES ('" + dienstgrad.getId() + "', '" + dienstgrad.getBeschreibung() + "', '" + dienstgrad.getBeschreibungLang() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Dienstgrad dienstgrad) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update dienstgrad set beschreibung = '" + dienstgrad.getBeschreibung() + "',   beschreibungLang = '" + dienstgrad.getBeschreibungLang() + "' where id = " + dienstgrad.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from dienstgrad where beschreibungLang = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM dienstgrad;");
        logging.logSQL((Object)"SELECT max(id) FROM dienstgrad;");
        if (result.next()) {
            if (result.getInt(1) <= 20) {
                return 51;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCountBeschreibung(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM dienstgrad where beschreibung = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM dienstgrad where beschreibung = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountBeschreibungLang(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM dienstgrad where beschreibungLang = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM dienstgrad where beschreibungLang = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getDienstgradID(String dienstgradLang) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM dienstgrad where beschreibungLang = '" + dienstgradLang + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM dienstgrad where beschreibungLang = '" + dienstgradLang + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getDienstgradBeschreibungLang(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT beschreibungLang FROM dienstgrad where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT beschreibungLang FROM dienstgrad where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getDienstgradBeschreibungKurz(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT beschreibung FROM dienstgrad where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT beschreibung FROM dienstgrad where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAllDienstgradKurz() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT beschreibung FROM dienstgrad  where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT beschreibung FROM dienstgrad where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllDienstgradLang() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT beschreibungLang FROM dienstgrad where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT beschreibungLang FROM dienstgrad where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllDienstgradIds() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM dienstgrad where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT id FROM dienstgrad where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getNextDienstgradID(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM `dienstgrad` WHERE id > " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT * FROM `dienstgrad` WHERE id > " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        if (result.next()) {
            logging.logSQL((Object)("Ergebnis: " + result.getInt(1)));
            return result.getInt(1);
        }
        return 0;
    }
}

