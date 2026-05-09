/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen;

import data.DatenbankZugriff;
import data.tabellen.TabelleAnwesenheit;
import go.Veranstaltung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class TabelleVeranstaltung {
    public void insert(Veranstaltung veranstaltung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO veranstaltung (`id`, `name`, `name2`, `kategorie`, `datum`, `zeit`, `zeitEnde`, `fahrzeugeinteilung`, `infoVersandt` , `mandantID`) VALUES ('" + veranstaltung.getId() + "', '" + veranstaltung.getName() + "', '" + veranstaltung.getName2() + "', '" + veranstaltung.getKategorie() + "', '" + veranstaltung.getDatum() + "', '" + veranstaltung.getZeit() + "', '" + veranstaltung.getZeitEnde() + "', '" + veranstaltung.getFahrzeugeinteilung() + "', '" + veranstaltung.getInfoVersandt() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Veranstaltung veranstaltung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update veranstaltung set name = '" + veranstaltung.getName() + "', name2 = '" + veranstaltung.getName2() + "', kategorie = '" + veranstaltung.getKategorie() + "', datum = '" + veranstaltung.getDatum() + "', zeit = '" + veranstaltung.getZeit() + "', zeitEnde = '" + veranstaltung.getZeitEnde() + "' where id = " + veranstaltung.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateFahrzeugeinteilung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "UPDATE veranstaltung set fahrzeugeinteilung = 1 where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoVersandt(String datumVon, String datumBis) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update veranstaltung set infoVersandt = 1 where kategorie != 1 and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void deleteOne(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM veranstaltung;");
        logging.logSQL((Object)"SELECT max(id) FROM veranstaltung;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getJahrDerVeranstaltung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return Integer.parseInt(result.getString(1).substring(0, 4));
        }
        return 0;
    }

    public int getVeranstaltungID(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM veranstaltung where name = '" + name + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getDatum(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getZeitStart(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT zeit FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT zeit FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getZeitEnde(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT zeitEnde FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT zeitEnde FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getFahrzeugeinteilungStatus(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT fahrzeugeinteilung FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT fahrzeugeinteilung FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCount(int veranstaltungKategorie, String datum, String zeit) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and datum = '" + TimeCalculation.parseDateForDatabase(datum) + "' and zeit = '" + zeit + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and datum = '" + TimeCalculation.parseDateForDatabase(datum) + "' and zeit = '" + zeit + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountByVeranstaltungskategorie(int veranstaltungKategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getAllVeranstaltung() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where datum between '" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01' and '" + runApplication.veranstaltungsAnzeigeZukunft + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        logging.logSQL((Object)("SELECT name FROM veranstaltung where datum between '" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01' and '" + runApplication.veranstaltungsAnzeigeZukunft + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllVeranstaltungID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ID;");
        logging.logSQL((Object)("SELECT id FROM veranstaltung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by ID;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public HashMap<String, String> getVeranstaltungData(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT * FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT * FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("id", result.getString(1));
            map.put("name", result.getString(2));
            map.put("name2", result.getString(3));
            map.put("kategorie", result.getString(4));
            map.put("datum", result.getString(5));
            map.put("zeit", result.getString(6));
            map.put("zeitEnde", result.getString(7));
            map.put("fahrzeugeinteilung", result.getString(8));
            map.put("infoVersandt", result.getString(9));
        }
        return map;
    }

    public ArrayList<String> getAllVeranstaltungWithoutFahrzeugeinteilung() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where fahrzeugeinteilung = 0 and datum between '" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01' and '" + runApplication.veranstaltungsAnzeigeZukunft + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        logging.logSQL((Object)("SELECT name FROM veranstaltung where fahrzeugeinteilung = 0 and  datum between '" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01' and '" + runApplication.veranstaltungsAnzeigeZukunft + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungEinerKategorie(int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01' and '" + runApplication.veranstaltungsAnzeigeZukunft + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01' and '" + runApplication.veranstaltungsAnzeigeZukunft + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungEinerKategorieFromDB(int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungEinerKategorieByJahr(int kategorie, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllKommendenVeranstaltungEinerKategorieByJahr(int kategorie, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-" + SbcUtils.timeStamp((String)"MM-dd") + "' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-" + SbcUtils.timeStamp((String)"MM-dd") + "' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungWithoutInfoVersandtInDiesemMonat(String datumVon, String datumBis, boolean alleVeranstaltungenWaehlen) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String additionalSQL = alleVeranstaltungenWaehlen ? "" : "and infoVersandt = 0";
        logging.logSQL((Object)("SELECT id, name2, datum, zeit, kategorie FROM veranstaltung v where kategorie != 1 " + additionalSQL + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT id, name2, datum, zeit, kategorie FROM veranstaltung v where kategorie != 1 " + additionalSQL + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString("name2")) + "\n" + TimeCalculation.parseDateForGUI(result.getString("datum")) + " um " + result.getString("zeit") + " Uhr\n");
            if (result.getInt("kategorie") == 3) {
                String[] anwesendeMitglieder = Utils.listToArray(new TabelleAnwesenheit().getAnwesendeMitglieder(result.getInt("id")));
                if (anwesendeMitglieder.length != 0) {
                    liste.add("Teilnehmer: ");
                    int i = 0;
                    while (i < anwesendeMitglieder.length) {
                        liste.add(String.valueOf(anwesendeMitglieder[i]) + "; ");
                        ++i;
                    }
                    liste.add("\n");
                } else {
                    liste.add("Teilnehmer: --\n");
                }
            }
            liste.add("\n");
        }
        return liste;
    }

    public int getCountVeranstaltungWithoutInfoVersandtInDiesemMonat(String datumVon, String datumBis, int status) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie != 1 and infoVersandt = " + status + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM veranstaltung where kategorie != 1 and infoVersandt = " + status + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getAllVeranstaltungEinesJahres(String jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM veranstaltung where datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;"));
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungEinesZeitraums(String datumVon, String datumBis, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String kategoriePartOfStatement = null;
        kategoriePartOfStatement = kategorie == 0 ? "kategorie > 1" : "kategorie = " + kategorie;
        logging.logSQL((Object)("SELECT name2 FROM veranstaltung where " + kategoriePartOfStatement + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT name2 FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllVeranstaltungEinesZeitraumsIDs(String datumVon, String datumBis, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String kategoriePartOfStatement = null;
        kategoriePartOfStatement = kategorie == 0 ? "kategorie > 1" : "kategorie = " + kategorie;
        logging.logSQL((Object)("SELECT id FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungEinesZeitraumsDatum(String datumVon, String datumBis, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String kategoriePartOfStatement = null;
        kategoriePartOfStatement = kategorie == 0 ? "kategorie > 1" : "kategorie = " + kategorie;
        logging.logSQL((Object)("SELECT datum FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT datum FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungEinesZeitraumsZeit(String datumVon, String datumBis, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String kategoriePartOfStatement = null;
        kategoriePartOfStatement = kategorie == 0 ? "kategorie > 1" : "kategorie = " + kategorie;
        logging.logSQL((Object)("SELECT zeit FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT zeit FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungEinesZeitraumsZeitEnde(String datumVon, String datumBis, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String kategoriePartOfStatement = null;
        kategoriePartOfStatement = kategorie == 0 ? "kategorie > 1" : "kategorie = " + kategorie;
        logging.logSQL((Object)("SELECT zeitEnde FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT zeitEnde FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getCountAllVeranstaltungEinesJahresByKategorie(String jahr, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountAllAbgelaufendenVeranstaltungEinesJahresByKategorie(String jahr, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between '" + jahr + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountAllVeranstaltungEinesJahres(String jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM veranstaltung where datum between '" + jahr + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where datum between '" + jahr + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getVeranstaltungKategorieID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT kategorie FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT kategorie FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getVeranstaltungName2AndDatum(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name2, datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name2, datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return String.valueOf(result.getString(1)) + "_" + TimeCalculation.parseDateForGUI(result.getString(2));
        }
        return null;
    }

    public String getVeranstaltungName2(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name2 FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name2 FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getVeranstaltungName(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT name FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public Vector<Vector<String>> getAllVeranstaltungForTable(String datumVon, String datumBis, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String kategoriePartOfStatement = null;
        kategoriePartOfStatement = kategorie == 0 ? "kategorie > 1" : "kategorie = " + kategorie;
        logging.logSQL((Object)("SELECT datum, zeit, name FROM veranstaltung where " + kategoriePartOfStatement + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT datum, zeit, name FROM veranstaltung where " + kategoriePartOfStatement + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<String> getAllVeranstaltungForTableListe(String datumVon, String datumBis, int kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String kategoriePartOfStatement = null;
        kategoriePartOfStatement = kategorie == 0 ? "kategorie > 1" : "kategorie = " + kategorie;
        logging.logSQL((Object)("SELECT name FROM veranstaltung where " + kategoriePartOfStatement + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where " + kategoriePartOfStatement + " and datum between '" + datumVon + "' and '" + datumBis + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getVeranstaltungDiesenMonats() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM veranstaltung where datum between '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and '" + SbcUtils.timeStamp((String)"yyyy-MM") + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;"));
        ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where datum between '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and '" + SbcUtils.timeStamp((String)"yyyy-MM") + "-31' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> veranstaltungListe = new Vector<String>();
        veranstaltungListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
        veranstaltungListe.add(result.getString("zeit"));
        veranstaltungListe.add(result.getString("name"));
        return veranstaltungListe;
    }
}

