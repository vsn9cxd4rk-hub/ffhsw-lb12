/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen.abrechnung;

import data.DatenbankZugriff;
import go.abrechnung.ArtikelAbrechnung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;

public class TabelleAbrechnung_artikel {
    public void insert(ArtikelAbrechnung artikel) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO abrechnung_artikel (`id`,`name`, `klasse`,`buchungskonto`,`zahlungsart`, `wert`,`rabattwert`,`mwst`, `berechnungsart`,`berechnungsart2`,`rabattart`, `aktiv`, `von`, `bis` , `mandantID`) VALUES ('" + artikel.getId() + "', '" + artikel.getName() + "', '" + artikel.getKlasse() + "', '" + artikel.getBuchungskonto() + "', '" + artikel.getZahlungsart() + "', '" + artikel.getWert() + "', '" + artikel.getRabattwert() + "', '" + artikel.getMwst() + "', '" + artikel.getBerechnungsart() + "', '" + artikel.getBerechnungsart2() + "', '" + artikel.getRabattart() + "', '" + artikel.getAktiv() + "', '" + artikel.getVon() + "', '" + artikel.getBis() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(ArtikelAbrechnung artikel) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update abrechnung_artikel set name = '" + artikel.getName() + "', klasse = '" + artikel.getKlasse() + "', buchungskonto = '" + artikel.getBuchungskonto() + "', zahlungsart = '" + artikel.getZahlungsart() + "', wert = '" + artikel.getWert() + "', rabattwert = '" + artikel.getRabattwert() + "', mwst = '" + artikel.getMwst() + "', berechnungsart = '" + artikel.getBerechnungsart() + "', berechnungsart2 = '" + artikel.getBerechnungsart2() + "', rabattart = '" + artikel.getRabattart() + "', aktiv = '" + artikel.getAktiv() + "', von = '" + artikel.getVon() + "', bis = '" + artikel.getBis() + "' where id = " + artikel.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM abrechnung_artikel where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT max(id) FROM abrechnung_artikel where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            if (result.getInt(1) <= 3999) {
                return result.getInt(1) + 4000;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getArtikelIDByKlasse(int klasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String datum = SbcUtils.timeStamp((String)"yyyy-MM-dd");
        ResultSet result = statement.executeQuery("SELECT id FROM abrechnung_artikel where klasse = " + klasse + " and aktiv = 1 and von <= '" + datum + "' and bis >= '" + datum + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM abrechnung_artikel where klasse = " + klasse + " and aktiv = 1 and von <= '" + datum + "' and bis >= '" + datum + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getAllArtikel() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;");
        logging.logSQL((Object)("SELECT name FROM abrechnung_artikel where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllValidArtikel() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String datum = SbcUtils.timeStamp((String)"yyyy-MM-dd");
        ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where von <= '" + datum + "' and bis >= '" + datum + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;");
        logging.logSQL((Object)("SELECT name FROM abrechnung_artikel where von <= '" + datum + "' and bis >= '" + datum + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllArtikelGroe\u00dfer100() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String datum = SbcUtils.timeStamp((String)"yyyy-MM-dd");
        ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where klasse > 100 and aktiv = 1  and von <= '" + datum + "' and bis >= '" + datum + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;");
        logging.logSQL((Object)("SELECT name FROM abrechnung_artikel where klasse > 100 and aktiv = 1  and von <= '" + datum + "' and bis >= '" + datum + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by name;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getArtikelID(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM abrechnung_artikel where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT id FROM abrechnung_artikel where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getZahlungsart(int artikelID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT zahlungsart FROM abrechnung_artikel where id = '" + artikelID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT zahlungsart FROM abrechnung_artikel where id = '" + artikelID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getBuchungskontoName(int artID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT k.name FROM abrechnung_konto k LEFT JOIN abrechnung_artikel a ON a.buchungskonto = k.id where a.id = '" + artID + "' and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT k.name FROM abrechnung_konto k LEFT JOIN abrechnung_artikel a ON a.buchungskonto = k.id where a.id = '" + artID + "' and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getBuchungskontoID(int artID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT buchungskonto FROM abrechnung_artikel where id = '" + artID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT buchungskonto FROM abrechnung_artikel where id = '" + artID + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelCount(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM abrechnung_artikel where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung_artikel where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelCountByName(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM abrechnung_artikel where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung_artikel where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getArtikelName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getGueltigVon(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT von FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT von FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getGueltigBis(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT bis FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT bis FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getKlasseCount(int klassenID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung_artikel where klasse = " + klassenID + " and klasse > 100 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM abrechnung_artikel where klasse = " + klassenID + " and klasse > 100 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelKlasse(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT klasse FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT klasse FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelWert(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT wert FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT wert FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelRabattWert(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT rabattwert FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT rabattwert FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelBerechnungsart(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT berechnungsart FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT berechnungsart FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelBerechnungsart2(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT berechnungsart2 FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT berechnungsart2 FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getArtikelrabattArt(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT rabattart FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT rabattart FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getAktiv(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT aktiv FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT akti FROM abrechnung_artikel where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

