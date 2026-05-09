/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen;

import data.DatenbankZugriff;
import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleStichwort;
import go.Einsatz;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleEinsatz {
    public Vector<Vector<String>> getAllForList(String jahr, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT e.einsatzNummer, e.einsatznummerOffiziell, e.Datum, e.ZeitAlarm, s.name, e.Ort, e.Fahrzeug, e.fahrzeugID, e.beschreibung, e.staerkeZF, e.staerkeGF, e.staerkeFM FROM einsatz e LEFT JOIN stichwort s ON s.id = e.stichwort WHERE e.mandantID = " + mandantID + " and e.Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' order by e.einsatzNummer desc;"));
        ResultSet result = statement.executeQuery("SELECT e.einsatzNummer, e.einsatznummerOffiziell, e.Datum, e.ZeitAlarm, s.name, e.Ort, e.Fahrzeug, e.fahrzeugID, e.beschreibung, e.staerkeZF, e.staerkeGF, e.staerkeFM FROM einsatz e LEFT JOIN stichwort s ON s.id = e.stichwort WHERE e.mandantID = " + mandantID + " and e.Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' order by e.einsatzNummer desc;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<Integer> getAllVeranstaltungsIDsForList(String jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT veranstaltungID from einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' order by einsatzNummer desc;"));
        ResultSet result = statement.executeQuery("SELECT veranstaltungID from einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' order by einsatzNummer desc;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public void insert(Einsatz einsatz) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO einsatz (`id`, `einsatzNummer`, `einsatznummerOffiziell`, `veranstaltungID`, `Datum` ,`ZeitAlarm` , `ZeitAusgerueckt`, `ZeitEingetroffen`, `ZeitEingerueckt` ,`Ort`,`stadtteil` ,`Stichwort` ,`Fahrzeug`, `fahrzeugID` ,`beschreibung`,`staerkeGF`,`staerkeFM`,`einsatzleiter`,`staerkeZF`, `einsatzleiterBF`, `mandantID`) VALUES ('" + einsatz.getId() + "', '" + einsatz.getEinsatznummer() + "', '" + einsatz.getEinsatznummerOffiziell() + "', '" + einsatz.getVeranstaltungID() + "', '" + einsatz.getDatum() + "', '" + einsatz.getZeitAlarm() + "', '" + einsatz.getZeitAusgerueckt() + "', '" + einsatz.getZeitEingetroffen() + "', '" + einsatz.getZeitEingerueckt() + "', '" + einsatz.getOrt() + "','" + einsatz.getStadtteil() + "','" + einsatz.getStichwort() + "', '" + einsatz.getFahrzeug() + "', '" + einsatz.getFahrzeugID() + "', '" + einsatz.getBeschreibung() + "', '" + einsatz.getStaerkeGF() + "', '" + einsatz.getStaerkeFM() + "', '" + einsatz.getEinsatzleiter() + "', '" + einsatz.getStaerkeZF() + "', '" + einsatz.getEinsatzleiterBF() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Einsatz einsatz) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update einsatz set einsatznummerOffiziell = '" + einsatz.getEinsatznummerOffiziell() + "', datum = '" + einsatz.getDatum() + "', zeitAlarm = '" + einsatz.getZeitAlarm() + "', zeitAusgerueckt = '" + einsatz.getZeitAusgerueckt() + "', zeitEingetroffen = '" + einsatz.getZeitEingetroffen() + "', zeitEingerueckt = '" + einsatz.getZeitEingerueckt() + "', ort = '" + einsatz.getOrt() + "', stadtteil = '" + einsatz.getStadtteil() + "', stichwort = '" + einsatz.getStichwort() + "', einsatzleiter = '" + einsatz.getEinsatzleiter() + "', einsatzleiterBF = '" + einsatz.getEinsatzleiterBF() + "', fahrzeug = '" + einsatz.getFahrzeug() + "', fahrzeugID = '" + einsatz.getFahrzeugID() + "', beschreibung = '" + einsatz.getBeschreibung() + "', staerkeFM = '" + einsatz.getStaerkeFM() + "', staerkeGF = '" + einsatz.getStaerkeGF() + "', staerkeZF = '" + einsatz.getStaerkeZF() + "' where veranstaltungID = " + einsatz.getVeranstaltungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateStaerke(Einsatz einsatz) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update einsatz set staerkeFM = " + einsatz.getStaerkeFM() + ", staerkeGF = " + einsatz.getStaerkeGF() + ", staerkeZF = " + einsatz.getStaerkeZF() + " where veranstaltungID = " + einsatz.getVeranstaltungID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public HashMap<String, String> getData(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("einsatzNummer", result.getString("einsatzNummer"));
            map.put("einsatznummerOffiziell", result.getString("einsatznummerOffiziell"));
            map.put("Datum", result.getString("Datum"));
            map.put("ZeitAlarm", result.getString("ZeitAlarm"));
            map.put("ZeitAusgerueckt", result.getString("ZeitAusgerueckt"));
            map.put("ZeitEingerueckt", result.getString("ZeitEingerueckt"));
            map.put("Ort", result.getString("Ort"));
            map.put("Fahrzeug", result.getString("Fahrzeug"));
            map.put("stadtteil", result.getString("stadtteil"));
            map.put("beschreibung", result.getString("beschreibung"));
            map.put("einsatzleiter", result.getString("einsatzleiter"));
            map.put("einsatzleiterBF", result.getString("einsatzleiterBF"));
            map.put("Stichwort", result.getString("Stichwort"));
            map.put("staerkeGF", result.getString("staerkeGF"));
            map.put("staerkeFM", result.getString("staerkeFM"));
            map.put("staerkeZF", result.getString("staerkeZF"));
        }
        return map;
    }

    public Einsatz getData2(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        Einsatz einsatz = new Einsatz();
        while (result.next()) {
            einsatz.setId(result.getInt("id"));
            einsatz.setVeranstaltungID(result.getInt("veranstaltungID"));
            einsatz.setEinsatznummer(result.getInt("einsatzNummer"));
            einsatz.setEinsatznummerOffiziell(result.getString("einsatznummerOffiziell"));
            einsatz.setDatum(result.getString("Datum"));
            einsatz.setZeitAlarm(result.getString("ZeitAlarm"));
            einsatz.setZeitAusgerueckt(result.getString("ZeitAusgerueckt"));
            einsatz.setZeitEingerueckt(result.getString("ZeitEingerueckt"));
            einsatz.setOrt(result.getString("Ort"));
            einsatz.setFahrzeug(result.getString("Fahrzeug"));
            einsatz.setStadtteil(result.getString("stadtteil"));
            einsatz.setBeschreibung(result.getString("beschreibung"));
            einsatz.setEinsatzleiter(result.getInt("einsatzleiter"));
            einsatz.setEinsatzleiterBF(result.getString("einsatzleiterBF"));
            einsatz.setStichwort(result.getInt("Stichwort"));
            einsatz.setStaerkeGF(result.getInt("staerkeGF"));
            einsatz.setStaerkeFM(result.getInt("staerkeFM"));
            einsatz.setStaerkeZF(result.getInt("staerkeZF"));
        }
        return einsatz;
    }

    public int getNextNummer(String jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT max(eisatzNummer) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31';"));
        ResultSet result = statement.executeQuery("SELECT max(einsatzNummer) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31';");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getStichwortCount(int stichwortID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `einsatz` WHERE stichwort = " + stichwortID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE stichwort = " + stichwortID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getEinsatzProMonat(int jahr, int monat) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + "and Datum between '" + jahr + "-" + monat + "-01' and '" + jahr + "-" + monat + "-31';"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-" + monat + "-01' and '" + jahr + "-" + monat + "-31';");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getEinsatzProStunde(int jahr, int stunde) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(2);
        nf.setGroupingUsed(false);
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and zeitAlarm like '" + nf.format(stunde) + "%';");
        logging.logSQL((Object)("SELECT count(*) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and zeitAlarm like '" + nf.format(stunde) + "%';"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getVerfuegbarkeitProStunde(int jahr, int stunde) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(2);
        nf.setGroupingUsed(false);
        logging.logSQL((Object)("SELECT count(*), sum(staerkeFM), sum(staerkeGF), sum(staerkeZF) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and zeitAlarm like '" + nf.format(stunde) + "%';"));
        ResultSet result = statement.executeQuery("SELECT count(*), sum(staerkeFM), sum(staerkeGF), sum(staerkeZF) FROM `einsatz` WHERE mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31' and zeitAlarm like '" + nf.format(stunde) + "%';");
        if (result.next()) {
            if (result.getInt(1) == 0) {
                return 0;
            }
            return (result.getInt(2) + result.getInt(3) + result.getInt(4)) / result.getInt(1);
        }
        return 0;
    }

    public int getNextID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM `einsatz`;");
        logging.logSQL((Object)"SELECT max(id) FROM `einsatz`;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getletzteVeranstaltungID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(veranstaltungID) FROM `einsatz`;");
        logging.logSQL((Object)"SELECT max(veranstaltungID) FROM `einsatz`;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfStadtteil(int jahr, String stadtteil) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE stadtteil = '" + stadtteil + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31';");
        logging.logSQL((Object)("SELECT count(*) FROM `einsatz` WHERE stadtteil = '" + stadtteil + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and Datum between '" + jahr + "-01-01' and '" + jahr + "-12-31';"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getEinsatzIDByVeranstaltungID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT einsatzNummer FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT einsatzNummer FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getVeranstaltungIDbyEinsatzID(int einsatzID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT veranstaltungID FROM einsatz where einsatzNummer = " + einsatzID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31'  ;");
        logging.logSQL((Object)("SELECT veranstaltungID FROM einsatz where einsatzNummer = " + einsatzID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31'  ;"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getEinsatzDatum(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT datum FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT datum FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getEinsatzVonUhr(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ZeitAlarm FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT ZeitAlarm FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getEinsatzBisUhr(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT ZeitEingerueckt FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT ZeitEingerueckt FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getEinsatznummerForVerdienstausfall(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT e.einsatzNummerOffiziell, s.name, e.ort FROM einsatz e LEFT JOIN stichwort s ON e.Stichwort = s.id where e.veranstaltungID = " + veranstaltungID + " and e.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT e.einsatzNummerOffiziell, s.name, e.ort FROM einsatz e LEFT JOIN stichwort s ON e.Stichwort = s.id where e.veranstaltungID = " + veranstaltungID + " and e.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            if (runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen").equals("1")) {
                return "Nr. " + result.getString(1) + ", " + result.getString(2) + " " + result.getString(3);
            }
            if (runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen").equals("2")) {
                return "Nr. " + result.getString(1) + ", " + result.getString(2);
            }
            if (runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen").equals("3")) {
                return "Einsatz-Nr. " + result.getString(1);
            }
            if (runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen").equals("4")) {
                return "Einsatz " + result.getString(2);
            }
            if (runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen").equals("6")) {
                return "Nr. " + result.getString(1) + ", " + new TabelleEinsatz_kategorie().getEinsatzKategorieName(new TabelleStichwort().getStichwortKategorieID(result.getString(2)));
            }
            return "Einsatz-Nr. " + result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getStrasseListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ort FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by ort"));
        ResultSet result = statement.executeQuery("SELECT ort FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by ort");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (result.getString(1).length() >= 45) {
                liste.add(String.valueOf(result.getString(1).substring(0, 45)) + "...");
                continue;
            }
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getStadtteilListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT stadtteil FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by stadtteil"));
        ResultSet result = statement.executeQuery("SELECT stadtteil FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by stadtteil");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getBeschreibungListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT beschreibung FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by beschreibung"));
        ResultSet result = statement.executeQuery("SELECT beschreibung FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by beschreibung");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (result.getString(1).length() >= 40) {
                liste.add(String.valueOf(result.getString(1).substring(0, 40)) + "...");
                continue;
            }
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getEinsatzleiterBFListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT einsatzleiterBF FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by einsatzleiterBF"));
        ResultSet result = statement.executeQuery("SELECT einsatzleiterBF FROM einsatz where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and datum between '" + SbcUtils.timeStamp((String)"yyyy") + "-01-01' and '" + SbcUtils.timeStamp((String)"yyyy") + "-12-31' group by einsatzleiterBF");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> einsatzListe = new Vector<String>();
        einsatzListe.add(Integer.toString(result.getInt("einsatzNummer")));
        einsatzListe.add(result.getString("einsatznummerOffiziell"));
        einsatzListe.add(TimeCalculation.parseDateForGUI(result.getString("Datum")));
        einsatzListe.add(result.getString("ZeitAlarm"));
        einsatzListe.add(result.getString("Name"));
        einsatzListe.add(result.getString("Ort"));
        einsatzListe.add(result.getString("Fahrzeug"));
        einsatzListe.add(result.getString("beschreibung"));
        einsatzListe.add(Integer.toString(result.getInt("staerkeZF")));
        einsatzListe.add(Integer.toString(result.getInt("staerkeGF")));
        einsatzListe.add(Integer.toString(result.getInt("staerkeFM")));
        return einsatzListe;
    }
}

