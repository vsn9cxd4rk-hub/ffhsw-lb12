/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Fahrzeug;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import logging.logging;
import run.runApplication;

public class TabelleFahrzeug {
    public void insert(Fahrzeug fahrzeug) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO fahrzeuge (`id`, `name`, `beschreibung`, `kennzeichen`, `funkrufname`, `sitzplaetze`, `minBesatzung`, `maxBesatzung`, `fuehrerschein`, `ausserDienst`, `anhaenger`, `trupp`, `sortierung`, `mandantID`) VALUES ('" + fahrzeug.getId() + "', '" + fahrzeug.getName() + "', '" + fahrzeug.getBeschreibung() + "', '" + fahrzeug.getKennzeichen() + "', '" + fahrzeug.getFunkrufname() + "', '" + fahrzeug.getSitzplaetze() + "', '" + fahrzeug.getMinBesatzung() + "', '" + fahrzeug.getMaxBesatzung() + "', '" + fahrzeug.getFuehrerschein() + "', '" + fahrzeug.getAusserDienst() + "', '" + fahrzeug.getAnhaenger() + "', '" + fahrzeug.getTrupp() + "', '" + fahrzeug.getSortierung() + "', '" + fahrzeug.getMandantID() + "');";
        statement.executeUpdate(sql);
    }

    public void update(Fahrzeug fahrzeug) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update fahrzeuge set name = '" + fahrzeug.getName() + "', beschreibung = '" + fahrzeug.getBeschreibung() + "', kennzeichen = '" + fahrzeug.getKennzeichen() + "', funkrufname = '" + fahrzeug.getFunkrufname() + "', sitzplaetze = '" + fahrzeug.getSitzplaetze() + "', minBesatzung = '" + fahrzeug.getMinBesatzung() + "', maxBesatzung = '" + fahrzeug.getMaxBesatzung() + "', fuehrerschein = '" + fahrzeug.getFuehrerschein() + "', ausserDienst = '" + fahrzeug.getAusserDienst() + "', anhaenger = '" + fahrzeug.getAnhaenger() + "', trupp = '" + fahrzeug.getTrupp() + "', sortierung = '" + fahrzeug.getSortierung() + "' where id = " + fahrzeug.getId() + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateFahrzeugUndMandant(Fahrzeug fahrzeug) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update fahrzeuge set name = '" + fahrzeug.getName() + "', beschreibung = '" + fahrzeug.getBeschreibung() + "', kennzeichen = '" + fahrzeug.getKennzeichen() + "', funkrufname = '" + fahrzeug.getFunkrufname() + "', sitzplaetze = '" + fahrzeug.getSitzplaetze() + "', minBesatzung = '" + fahrzeug.getMinBesatzung() + "', maxBesatzung = '" + fahrzeug.getMaxBesatzung() + "', fuehrerschein = '" + fahrzeug.getFuehrerschein() + "', ausserDienst = '" + fahrzeug.getAusserDienst() + "', anhaenger = '" + fahrzeug.getAnhaenger() + "', trupp = '" + fahrzeug.getTrupp() + "', sortierung = '" + fahrzeug.getSortierung() + "', mandantID = '" + fahrzeug.getMandantID() + "' where id = " + fahrzeug.getId() + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
        String sql2 = "Update fahrzeug_untersuchung set mandantID = " + fahrzeug.getMandantID() + " where id = " + fahrzeug.getId() + ";";
        logging.logSQL((Object)sql2);
        statement.executeUpdate(sql2);
    }

    public void updateAusserDienst(int fahrzeugID, int ausserDienstStatus) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update fahrzeuge set ausserDienst = " + ausserDienstStatus + " where id = '" + fahrzeugID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCountByFahrzeugBeschreibung(int fahrzeugbeschreibungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `fahrzeuge` WHERE beschreibung = " + fahrzeugbeschreibungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `fahrzeuge` WHERE beschreibung = " + fahrzeugbeschreibungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrzeuge;");
        logging.logSQL((Object)"SELECT max(id) FROM fahrzeuge;");
        if (result.next()) {
            if (result.getInt(1) <= 9999) {
                return result.getInt(1) + 10000;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int countWithoutAnhaenger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int countALL() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeuge where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeuge where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int countOhneAnhaenger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getFahrzeugAusserDienstStatus(int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ausserDienst FROM fahrzeuge where id = " + fahrzeugID + ";");
        logging.logSQL((Object)("SELECT ausserDienst FROM fahrzeuge where id = " + fahrzeugID + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public HashMap<String, String> getAllFahrzeugData(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT f.id, f.name, b.beschreibung, f.kennzeichen, f.funkrufname, f.sitzplaetze, f.minBesatzung, f.maxBesatzung, f.ausserDienst, f.anhaenger, f.sortierung, f.fuehrerschein, f.mandantID FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + ";"));
        ResultSet result = statement.executeQuery("SELECT f.id, f.name, b.beschreibung, f.kennzeichen, f.funkrufname, f.sitzplaetze, f.minBesatzung, f.maxBesatzung, f.ausserDienst, f.anhaenger, f.sortierung, f.fuehrerschein, f.mandantID FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + ";");
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("id", Integer.toString(result.getInt(1)));
            map.put("name", result.getString(2));
            map.put("beschreibung", result.getString(3));
            map.put("kennzeichen", result.getString(4));
            map.put("funkrufname", result.getString(5));
            map.put("sitzplaetze", Integer.toString(result.getInt(6)));
            map.put("minBesatzung", Integer.toString(result.getInt(7)));
            map.put("maxBesatzung", Integer.toString(result.getInt(8)));
            map.put("ausserDienst", Integer.toString(result.getInt(9)));
            map.put("anhaenger", Integer.toString(result.getInt(10)));
            map.put("sortierung", Integer.toString(result.getInt(11)));
            map.put("fuehrerschein", result.getString(12));
            map.put("mandantID", result.getString(13));
        }
        return map;
    }

    public String getFahrzeugName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getBeschreibungID(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT beschreibung FROM fahrzeuge where id = " + id + ";"));
        ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeuge where id = " + id + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getBeschreibungName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT b.beschreibung FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + " and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT b.beschreibung FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + " and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getKennezeichen(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT kennzeichen FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT kennzeichen FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getSortierung(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sortierung FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sortierung FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getTrupp(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT trupp FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT trupp FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getFunkrufname(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT funkrufname FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT funkrufname FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getFahrzeugID(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM sitzplaetze where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getSitzplatz(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT sitzplaetze FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT sitzplaetze FROM sitzplaetze where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMaxBesatzung(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT maxBesatzung FROM sitzplaetze where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT maxBesatzung FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMinBesatzung(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT minBesatzung FROM sitzplaetze where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT minBesatzung FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getFuehrerschein(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT fuehrerschein FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT fuehrerschein FROM fahrzeuge where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getAnhaenger(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT anhaenger FROM fahrzeuge where id = " + id + ";");
        logging.logSQL((Object)("SELECT anhaenger FROM sitzplaetze where id = " + id + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getFahrzeugeByBeschreibungID(int beschreibungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = null;
        sql = runApplication.BF == 1 ? "SELECT id, name FROM fahrzeuge where beschreibung = " + beschreibungID + " order by name;" : "SELECT id, name FROM fahrzeuge where beschreibung = " + beschreibungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;";
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add("(" + result.getString(1) + ") " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getAllFahrzeugeMitAnhaenger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
        logging.logSQL((Object)("SELECT name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllFahrzeugeIDMitAnhaenger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where ausserDienst = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
        logging.logSQL((Object)("SELECT id FROM fahrzeuge where ausserDienst = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAllFahrzeugeOhneAnhaenger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and anhaenger = 0 order by sortierung;");
        logging.logSQL((Object)("SELECT name FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllFahrzeugBeschreibungOhneAnhaenger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
        logging.logSQL((Object)("SELECT beschreibung FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllFahrzeugeIDsOhneAnhaenger() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
        logging.logSQL((Object)("SELECT id FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllFahrzeugeOhneAnhaengerFunkrufname() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT funkrufname FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
        logging.logSQL((Object)("SELECT funkrufname FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public Fahrzeug getData(int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM fahrzeuge where id = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT * FROM fahrzeuge where id = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        Fahrzeug fahrzeug = new Fahrzeug();
        while (result.next()) {
            fahrzeug.setId(result.getInt("id"));
            fahrzeug.setName(result.getString("name"));
            fahrzeug.setBeschreibung(result.getInt("beschreibung"));
            fahrzeug.setKennzeichen(result.getString("kennzeichen"));
            fahrzeug.setFunkrufname(result.getString("funkrufname"));
            fahrzeug.setFuehrerschein(result.getString("fuehrerschein"));
            fahrzeug.setSortierung(result.getInt("sortierung"));
            fahrzeug.setAusserDienst(result.getInt("ausserDienst"));
            fahrzeug.setSitzplaetze(result.getInt("minBesatzung"));
            fahrzeug.setMaxBesatzung(result.getInt("maxBesatzung"));
            fahrzeug.setAnhaenger(result.getInt("anhaenger"));
        }
        return fahrzeug;
    }

    public HashMap<Integer, String> getFahrzeugNamenAndID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id, name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id, name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<Integer, String> map = new HashMap<Integer, String>();
        while (result.next()) {
            map.put(result.getInt(1), result.getString(2));
        }
        return map;
    }

    public ArrayList<String> getAllFahrzeugeFromDataBase() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;");
        logging.logSQL((Object)("SELECT name FROM fahrzeuge where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllFahrzeugIDsFromDataBase() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;");
        logging.logSQL((Object)("SELECT id FROM fahrzeuge where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getMinimaleBesatungAllerFahrzeuge() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT sum(minBesatzung) FROM fahrzeuge where ausserDienst = 0;");
        ResultSet result = statement.executeQuery("SELECT sum(minBesatzung) FROM fahrzeuge where ausserDienst = 0;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMaximaleBesatungAllerFahrzeuge() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT sum(maxBesatzung) FROM fahrzeuge where ausserDienst = 0;");
        ResultSet result = statement.executeQuery("SELECT sum(maxBesatzung) FROM fahrzeuge where ausserDienst = 0;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

