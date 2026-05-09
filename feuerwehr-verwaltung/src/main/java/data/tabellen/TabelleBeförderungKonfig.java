/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Bef\u00f6rderung;
import go.Bef\u00f6rderung_erforderlich;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBef\u00f6rderungKonfig {
    public void insert(Bef\u00f6rderung bef\u00f6rderung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO bef\u00f6rderung_konfiguration (`id`,`dienstgradID`, `dienstgradVoraussetzung`, `zeit`, `dienstZeit`, `nurZeitBefoerderung`, `letzteStufe`, `auslassen`, `mandantID`) VALUES ('" + bef\u00f6rderung.getId() + "', '" + bef\u00f6rderung.getDienstgradID() + "', '" + bef\u00f6rderung.getDienstgradVoraussetzung() + "', '" + bef\u00f6rderung.getZeit() + "', '" + bef\u00f6rderung.getDienstZeit() + "', '" + bef\u00f6rderung.getNurZeitBefoerderung() + "', '" + bef\u00f6rderung.getLetzteStufe() + "', '" + bef\u00f6rderung.getAuslassen() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Bef\u00f6rderung bef\u00f6rderung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update bef\u00f6rderung_konfiguration set dienstgradID = " + bef\u00f6rderung.getDienstgradID() + ", dienstgradVoraussetzung = " + bef\u00f6rderung.getDienstgradVoraussetzung() + ", zeit = " + bef\u00f6rderung.getZeit() + ", dienstZeit = " + bef\u00f6rderung.getDienstZeit() + ", nurZeitBefoerderung = " + bef\u00f6rderung.getNurZeitBefoerderung() + ", letzteStufe = " + bef\u00f6rderung.getLetzteStufe() + ", auslassen = " + bef\u00f6rderung.getAuslassen() + " where id = " + bef\u00f6rderung.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public void insert(Bef\u00f6rderung_erforderlich bef\u00f6rderung_erforderlich) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO bef\u00f6rderung_erforderlich (`id`,`lehrgangID`,`mandantID`) VALUES ('" + bef\u00f6rderung_erforderlich.getId() + "', '" + bef\u00f6rderung_erforderlich.getLehrgangID() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void delete(Bef\u00f6rderung_erforderlich bef\u00f6rderung_erforderlich) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from bef\u00f6rderung_erforderlich where lehrgangID = " + bef\u00f6rderung_erforderlich.getLehrgangID() + " and id = " + bef\u00f6rderung_erforderlich.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID");
        statement.executeUpdate(sql);
    }

    public void deleteAll(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from bef\u00f6rderung_erforderlich where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM bef\u00f6rderung_konfiguration;");
        logging.logSQL((Object)"SELECT max(id) FROM bef\u00f6rderung_konfiguration;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getID(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getDienstgradVorausseltzung(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT dienstgradVoraussetzung FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT dienstgradVoraussetzung FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getZeit(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT zeit FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT zeit FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getDienstZeit(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT dienstZeit FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT dienstZeit FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getNurZeitBefoerderung(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT nurZeitBefoerderung FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT nurZeitBefoerderung FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getLetzteStufe(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT letzteStufe FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT letzteStufe FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getAuslassen(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT auslassen FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT auslassen FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountForDienstgrad(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCount() {
        try {
            Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
            ResultSet result = statement.executeQuery("SELECT count(*) FROM bef\u00f6rderung_konfiguration where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
            logging.logSQL((Object)("SELECT count(*) FROM bef\u00f6rderung_konfiguration where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
            if (result.next()) {
                return result.getInt(1);
            }
            return 0;
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
            return 0;
        }
    }

    public ArrayList<String> getAllRelevantenLerh\u00e4nge(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT lk.name FROM lehrgang_kategorie lk LEFT JOIN bef\u00f6rderung_erforderlich bf ON lk.id = bf.lehrgangID where bf.id = " + dienstgradID + " and bf.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and lk.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT lk.name FROM lehrgang_kategorie lk LEFT JOIN bef\u00f6rderung_erforderlich bf ON lk.id = bf.lehrgangID where bf.id = " + dienstgradID + " and bf.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and lk.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllRelevantenLerh\u00e4ngeID(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT lehrgangID FROM bef\u00f6rderung_erforderlich WHERE  id = (Select id from bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + ") and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT lehrgangID FROM bef\u00f6rderung_erforderlich WHERE  id = (Select id from bef\u00f6rderung_konfiguration where dienstgradID = " + dienstgradID + ") and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public int getAlternativNextDienstgradID(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d.id FROM dienstgrad d LEFT JOIN bef\u00f6rderung_konfiguration bk ON d.id = bk.dienstgradID WHERE d.id > " + dienstgradID + " and bk.auslassen != 1 and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by d.id;"));
        ResultSet result = statement.executeQuery("SELECT d.id FROM dienstgrad d LEFT JOIN bef\u00f6rderung_konfiguration bk ON d.id = bk.dienstgradID WHERE d.id > " + dienstgradID + " and bk.auslassen != 1 and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by d.id;");
        if (result.next()) {
            logging.logSQL((Object)("Ergebnis: " + result.getInt(1)));
            return result.getInt(1);
        }
        return 0;
    }

    public int getAlternativNextDienstgradID_ZeitBefoerderung(int dienstgradID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT d.id FROM dienstgrad d LEFT JOIN bef\u00f6rderung_konfiguration bk ON d.id = bk.dienstgradID  WHERE d.id > " + dienstgradID + " and bk.nurZeitBefoerderung != 1 and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "order by d.id;");
        logging.logSQL((Object)("SELECT d.id FROM dienstgrad d LEFT JOIN bef\u00f6rderung_konfiguration bk ON d.id = bk.dienstgradID  WHERE d.id > " + dienstgradID + " and bk.nurZeitBefoerderung != 1 and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + "order by d.id;"));
        if (result.next()) {
            logging.logSQL((Object)("Ergebnis: " + result.getInt(1)));
            return result.getInt(1);
        }
        return 0;
    }
}

