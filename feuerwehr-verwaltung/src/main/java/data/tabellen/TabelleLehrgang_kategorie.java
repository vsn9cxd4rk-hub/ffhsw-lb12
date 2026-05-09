/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Lehrgang_Kategorie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleLehrgang_kategorie {
    public void insert(Lehrgang_Kategorie kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO lehrgang_kategorie (`id`, `art`, `name`, `relevant`, `reihenfolge`, `ue`, `loeschbar`, `loeschkenner`, `mandantID`) VALUES ('" + kategorie.getId() + "', '" + kategorie.getArt() + "', '" + kategorie.getName() + "', '" + kategorie.getRelevant() + "', '" + kategorie.getReihenfolge() + "', '" + kategorie.getUe() + "', '" + kategorie.getLoeschbar() + "', '" + kategorie.getLoeschkenner() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Lehrgang_Kategorie kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update lehrgang_kategorie set relevant = " + kategorie.getRelevant() + ", reihenfolge = " + kategorie.getReihenfolge() + " where name = '" + kategorie.getName() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateUnterrichtseinheiten(Lehrgang_Kategorie kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update lehrgang_kategorie set ue = " + kategorie.getUe() + " where id = '" + kategorie.getId() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateLoeschkenner(Lehrgang_Kategorie kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update lehrgang_kategorie set loeschkenner = " + kategorie.getLoeschkenner() + " where id = '" + kategorie.getId() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getAlleLehrg\u00e4nge() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleLehrg\u00e4ngeByName() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleLehrg\u00e4ngeSeminare() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('L', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('L', 'S', 'E') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleF\u00fchrerschein() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('F\u00fc') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('F\u00fc') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleFunktionen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('F') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('F') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleFunktionenAu\u00dferhalb() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('F_Au\u00dferhalb') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('F_Au\u00dferhalb') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleEhrungenAbzeichen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('EH', 'AB') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('EH', 'AB') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleEhrungen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('EH') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('EH') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleEhrungenIDs() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where art in ('EH') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where art in ('EH') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleAbzeichen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where art in ('AB') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in ('AB') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleAbzeichenIDs() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where art in ('AB') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where art in ('AB') and  loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getFilterNameLehrgang(int[] lehrgangKategorieIDs) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        StringBuilder build = new StringBuilder();
        build.append("(");
        int i = 0;
        while (i < lehrgangKategorieIDs.length) {
            build.append(lehrgangKategorieIDs[i]);
            if (i != lehrgangKategorieIDs.length - 1) {
                build.append(",");
            }
            ++i;
        }
        build.append(")");
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where loeschkenner = 0 and id in " + build.toString() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where loeschkenner = 0 and id in " + build.toString() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleLehrg\u00e4ngeID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F', 'F_Au\u00dferhalb', 'L', 'F\u00fc', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleLehrg\u00e4ngeSeminarID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('L', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('L', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleLehrg\u00e4ngeSeminareID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('L', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('L', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleF\u00fchrerscheinID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F\u00fc') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F\u00fc') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleFunktionenID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleFunktionenAu\u00dferhalbID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F_Au\u00dferhalb') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F_Au\u00dferhalb') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleRelevantenNamen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where relevant = 1 and art in ('L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where relevant = 1 and art in ('L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAlleRelevantenIDs() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where relevant = 1 and art in ('L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where relevant = 1 and art in ('L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAlleNichtRelevantenNamen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM lehrgang_kategorie where relevant = 0 and art in ('L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;"));
        ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where relevant = 0 and art in ('L', 'F\u00fc', 'S', 'E') and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public String getAlleRelevantenDBNamen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
        StringBuilder liste = new StringBuilder();
        int count = 0;
        while (result.next()) {
            if (count != 0) {
                liste.append(", ");
            }
            liste.append(result.getString(1));
            ++count;
        }
        return liste.toString();
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM lehrgang_kategorie;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM lehrgang_kategorie;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public String getArt(int lehrgangID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT art FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT art FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getNextReihenfolgenummerNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT max(reihenfolge) FROM lehrgang_kategorie where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT max(reihenfolge) FROM lehrgang_kategorie where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getLehrgangID(String lehrgang) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where name = '" + lehrgang + "' and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where name = '" + lehrgang + "' and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountByName(String lehrgang) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM lehrgang_kategorie where name = '" + lehrgang + "' and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where name = '" + lehrgang + "' and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountAlleRelevanten() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F', 'L', 'F\u00fc', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM lehrgang_kategorie where loeschkenner = 0 and art in ('F', 'L', 'F\u00fc', 'S', 'E') and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getUnterrichtseinheiten(int lehrgangID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ue FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT ue FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getloeschbarStatus(int lehrgangID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT loeschbar FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT loeschbar FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getRelevantStatus(int lehrgangID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT relevant FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT relevant FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

