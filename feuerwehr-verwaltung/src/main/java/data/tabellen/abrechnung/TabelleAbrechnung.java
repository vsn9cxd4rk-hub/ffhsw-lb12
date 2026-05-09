/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.abrechnung;

import data.DatenbankZugriff;
import go.abrechnung.Abrechnung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.MoneyCalculation;

public class TabelleAbrechnung {
    public void insert(Abrechnung abrechnung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO abrechnung (`id`,`abrechnungID`, `artikelID`,`buchungskonto`,`zahlungsart`,`mitgliederID`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `menge`, `wert`, `datum`,`status`,`umbuchungID` , `mandantID`) VALUES ('" + abrechnung.getId() + "', '" + abrechnung.getAbrechnungID() + "', '" + abrechnung.getArtikelID() + "', '" + abrechnung.getBuchungskonto() + "', '" + abrechnung.getZahlungsart() + "', '" + abrechnung.getMitgliedID() + "', '" + abrechnung.getJahr() + "', '" + abrechnung.getVeranstaltungID() + "', '" + abrechnung.getVeranstaltungKategorie() + "', '" + abrechnung.getMenge() + "', '" + abrechnung.getWert() + "', '" + abrechnung.getDatum() + "', '" + abrechnung.getStatus() + "', '" + abrechnung.getUmbuchungID() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateUmbuchungID(int id, int neuUmbuchungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update abrechnung set umbuchungID = " + neuUmbuchungID + " where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from abrechnung where veranstaltungID = " + veranstaltungID + " and abrechnungID = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateOffeneVorgaenge(int abrechnungID, int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update abrechnung SET status = 1, abrechnungID = " + abrechnungID + " where status = 0 and abrechnungID = 0 and id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getAbrechnugID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(abrechnungID) FROM abrechnung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT max(abrechnungID) FROM abrechnung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            if (result.getInt(1) <= 999999) {
                return 1000000;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM abrechnung;");
        logging.logSQL((Object)"SELECT max(id) FROM abrechnung;");
        if (result.next()) {
            if (result.getInt(1) <= 49999) {
                return 500000;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getVeranstaltungsCount(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getVeranstaltungsCountMitAbrechnung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getSum(String kontoname, int zahlungsart) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.zahlungsart = " + zahlungsart + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.zahlungsart = " + zahlungsart + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getSumWithStatus(String kontoname, int zahlungsart, int status) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.zahlungsart = " + zahlungsart + " and a.status = " + status + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.zahlungsart = " + zahlungsart + " and a.status = " + status + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getStatus(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT status FROM abrechnung where id  = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT status FROM abrechnung where id  = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getWertByID(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT wert FROM abrechnung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT wert FROM abrechnung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getMengeByID(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT menge FROM abrechnung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT menge FROM abrechnung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<Integer> getIDsByVeranstaltungForUmbuchung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getIDsByVeranstaltungForUmbuchung2(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID = 0 and zahlungsart = 3 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID = 0 and zahlungsart = 3 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<Integer> getMitgliederIDByVeranstaltungID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT mitgliederID FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT mitgliederID FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public ArrayList<String> getDataForPDF(int abrechnungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;");
        logging.logSQL((Object)("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            String zahlart = result.getString(8).equals("1") ? "Einzahlung" : (result.getString(8).equals("2") ? "Auszahlung" : "Umbuchung");
            String veranstaltung = result.getString(3) == null ? "" : result.getString(3);
            String name = result.getString(4) == null && result.getString(5) == null ? (result.getString(8).equals("1") ? "EINZAHLUNG" : "AUSZAHLUNG") : String.valueOf(result.getString(4)) + ", " + result.getString(5);
            liste.add(String.valueOf(result.getString(1)) + "     " + result.getString(2) + "     " + veranstaltung + "\n" + name + "     " + "\n" + zahlart + ":                                                                                           Menge: " + result.getString(6) + "    " + MoneyCalculation.parseMoneyVauleForGUI(result.getInt(7)) + "\u20ac");
        }
        return liste;
    }

    public ArrayList<String> getAllAbrechnungID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT abrechnungID FROM abrechnung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by abrechnungID order by abrechnungID desc ;");
        logging.logSQL((Object)("SELECT abrechnungID FROM abrechnung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " group by abrechnungID order by abrechnungID desc ;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public Vector<Vector<String>> getAllAbrechnungenByMitglied(int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, k.name as veranstaltungKategorie, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliederID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;"));
        ResultSet result = statement.executeQuery("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, k.name as veranstaltungKategorie, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliederID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<Integer> getIDArrayMitglieder(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
        logging.logSQL((Object)("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public Vector<Vector<String>> getAllAbrechnungenByKonto(String kontoname) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname, a.artikelID, aa.name as artikelname, v.name as veranstaltungName, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, a.artikelID, aa.name as artikelname, v.name as veranstaltungName, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultToKontoVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<Integer> getIDArrayKonto(String kontoname) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
        logging.logSQL((Object)("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = '" + kontoname + "' and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public Vector<Vector<String>> getAllAbrechnungenByAbrechnung(int abrechnungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;"));
        ResultSet result = statement.executeQuery("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultToAbrechnungenVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> artikelListe = new Vector<String>();
        artikelListe.add(result.getString("artikelID"));
        artikelListe.add(result.getString("artikelName"));
        artikelListe.add(result.getString("veranstaltungName"));
        artikelListe.add(result.getString("veranstaltungKategorie"));
        artikelListe.add(result.getString("menge"));
        artikelListe.add(String.valueOf(MoneyCalculation.parseMoneyVauleForGUI(Integer.parseInt(result.getString("wert")))) + "\u20ac");
        if (result.getString("zahlungsart").equals("1")) {
            artikelListe.add("Einzahlung (" + result.getString("buchungskonto") + ")");
        } else if (result.getString("zahlungsart").equals("2")) {
            artikelListe.add("Auszahlung (" + result.getString("buchungskonto") + ")");
        } else {
            artikelListe.add("Umbuchung (" + result.getString("buchungskonto") + ")");
        }
        if (result.getString("status").equals("0")) {
            artikelListe.add("offen");
        } else if (result.getString("status").equals("1")) {
            artikelListe.add("abgerechnet");
        }
        return artikelListe;
    }

    private Vector<String> mapResultToKontoVector(ResultSet result) throws SQLException {
        Vector<String> artikelListe = new Vector<String>();
        if (result.getString("name") == null && result.getString("vorname") == null) {
            if (result.getString("zahlungsart").equals("1")) {
                artikelListe.add("EINZAHLUNG");
            } else if (result.getString("zahlungsart").equals("2")) {
                artikelListe.add("AUSZAHLUNG");
            } else {
                artikelListe.add("UMBUCHUNG");
            }
        } else {
            artikelListe.add(String.valueOf(result.getString("name")) + ", " + result.getString("vorname"));
        }
        artikelListe.add(result.getString("artikelID"));
        artikelListe.add(result.getString("artikelName"));
        artikelListe.add(result.getString("veranstaltungName"));
        artikelListe.add(result.getString("menge"));
        artikelListe.add(String.valueOf(MoneyCalculation.parseMoneyVauleForGUI(Integer.parseInt(result.getString("wert")))) + "\u20ac");
        if (result.getString("zahlungsart").equals("1")) {
            artikelListe.add("Einzahlung");
        } else if (result.getString("zahlungsart").equals("2")) {
            artikelListe.add("Auszahlung");
        } else {
            artikelListe.add("Umbuchung");
        }
        if (result.getString("status").equals("0")) {
            artikelListe.add("offen");
        } else if (result.getString("status").equals("1")) {
            artikelListe.add("abgerechnet");
        }
        return artikelListe;
    }

    private Vector<String> mapResultToAbrechnungenVector(ResultSet result) throws SQLException {
        Vector<String> artikelListe = new Vector<String>();
        if (result.getString("name") == null && result.getString("vorname") == null) {
            if (result.getString("zahlungsart").equals("1")) {
                artikelListe.add("EINZAHLUNG");
            } else {
                artikelListe.add("AUSZAHLUNG");
            }
        } else {
            artikelListe.add(String.valueOf(result.getString("name")) + ", " + result.getString("vorname"));
        }
        artikelListe.add(result.getString("artikelID"));
        artikelListe.add(result.getString("artikelName"));
        artikelListe.add(result.getString("veranstaltungName"));
        artikelListe.add(result.getString("menge"));
        artikelListe.add(String.valueOf(MoneyCalculation.parseMoneyVauleForGUI(Integer.parseInt(result.getString("wert")))) + "\u20ac");
        if (result.getString("zahlungsart").equals("1")) {
            artikelListe.add("Einzahlung (" + result.getString("buchungskonto") + ")");
        } else if (result.getString("zahlungsart").equals("2")) {
            artikelListe.add("Auszahlung (" + result.getString("buchungskonto") + ")");
        } else {
            artikelListe.add("Umbuchung");
        }
        return artikelListe;
    }
}

