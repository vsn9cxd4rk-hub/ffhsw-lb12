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

   public Vector getAllForList(String jahr, int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT e.einsatzNummer, e.einsatznummerOffiziell, e.Datum, e.ZeitAlarm, s.name, e.Ort, e.Fahrzeug, e.fahrzeugID, e.beschreibung, e.staerkeZF, e.staerkeGF, e.staerkeFM FROM einsatz e LEFT JOIN stichwort s ON s.id = e.stichwort WHERE e.mandantID = " + mandantID + " and e.Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by e.einsatzNummer desc;");
      ResultSet result = statement.executeQuery("SELECT e.einsatzNummer, e.einsatznummerOffiziell, e.Datum, e.ZeitAlarm, s.name, e.Ort, e.Fahrzeug, e.fahrzeugID, e.beschreibung, e.staerkeZF, e.staerkeGF, e.staerkeFM FROM einsatz e LEFT JOIN stichwort s ON s.id = e.stichwort WHERE e.mandantID = " + mandantID + " and e.Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by e.einsatzNummer desc;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getAllVeranstaltungsIDsForList(String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT veranstaltungID from einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by einsatzNummer desc;");
      ResultSet result = statement.executeQuery("SELECT veranstaltungID from einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by einsatzNummer desc;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public void insert(Einsatz einsatz) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO einsatz (`id`, `einsatzNummer`, `einsatznummerOffiziell`, `veranstaltungID`, `Datum` ,`ZeitAlarm` , `ZeitAusgerueckt`, `ZeitEingetroffen`, `ZeitEingerueckt` ,`Ort`,`stadtteil` ,`Stichwort` ,`Fahrzeug`, `fahrzeugID` ,`beschreibung`,`staerkeGF`,`staerkeFM`,`einsatzleiter`,`staerkeZF`, `einsatzleiterBF`, `mandantID`) VALUES (\'" + einsatz.getId() + "\', \'" + einsatz.getEinsatznummer() + "\', \'" + einsatz.getEinsatznummerOffiziell() + "\', \'" + einsatz.getVeranstaltungID() + "\', \'" + einsatz.getDatum() + "\', \'" + einsatz.getZeitAlarm() + "\', \'" + einsatz.getZeitAusgerueckt() + "\', \'" + einsatz.getZeitEingetroffen() + "\', \'" + einsatz.getZeitEingerueckt() + "\', \'" + einsatz.getOrt() + "\',\'" + einsatz.getStadtteil() + "\',\'" + einsatz.getStichwort() + "\', \'" + einsatz.getFahrzeug() + "\', \'" + einsatz.getFahrzeugID() + "\', \'" + einsatz.getBeschreibung() + "\', \'" + einsatz.getStaerkeGF() + "\', \'" + einsatz.getStaerkeFM() + "\', \'" + einsatz.getEinsatzleiter() + "\', \'" + einsatz.getStaerkeZF() + "\', \'" + einsatz.getEinsatzleiterBF() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void update(Einsatz einsatz) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update einsatz set einsatznummerOffiziell = \'" + einsatz.getEinsatznummerOffiziell() + "\', datum = \'" + einsatz.getDatum() + "\', zeitAlarm = \'" + einsatz.getZeitAlarm() + "\', zeitAusgerueckt = \'" + einsatz.getZeitAusgerueckt() + "\', zeitEingetroffen = \'" + einsatz.getZeitEingetroffen() + "\', zeitEingerueckt = \'" + einsatz.getZeitEingerueckt() + "\', ort = \'" + einsatz.getOrt() + "\', stadtteil = \'" + einsatz.getStadtteil() + "\', stichwort = \'" + einsatz.getStichwort() + "\', einsatzleiter = \'" + einsatz.getEinsatzleiter() + "\', einsatzleiterBF = \'" + einsatz.getEinsatzleiterBF() + "\', fahrzeug = \'" + einsatz.getFahrzeug() + "\', fahrzeugID = \'" + einsatz.getFahrzeugID() + "\', beschreibung = \'" + einsatz.getBeschreibung() + "\', staerkeFM = \'" + einsatz.getStaerkeFM() + "\', staerkeGF = \'" + einsatz.getStaerkeGF() + "\', staerkeZF = \'" + einsatz.getStaerkeZF() + "\' where veranstaltungID = " + einsatz.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateEinsatzNummer(int einsatzNummerNeu, int einsatzNummerAlt, String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update einsatz set einsatzNummer = \'" + einsatzNummerNeu + "\' where einsatzNummer = " + einsatzNummerAlt + " and SUBSTR(datum,1,4) = \'" + jahr + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateStaerke(Einsatz einsatz) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update einsatz set staerkeFM = " + einsatz.getStaerkeFM() + ", staerkeGF = " + einsatz.getStaerkeGF() + ", staerkeZF = " + einsatz.getStaerkeZF() + " where veranstaltungID = " + einsatz.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public HashMap getData(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
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
      ResultSet result = statement.executeQuery("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM einsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Einsatz einsatz = new Einsatz();

      while(result.next()) {
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
      logging.logSQL("SELECT max(eisatzNummer) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\';");
      ResultSet result = statement.executeQuery("SELECT max(einsatzNummer) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\';");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getStichwortCount(int stichwortID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `einsatz` WHERE stichwort = " + stichwortID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE stichwort = " + stichwortID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getEinsatzProMonat(int jahr, int monat) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "and Datum between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\';");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\';");
      return result.next()?result.getInt(1):0;
   }

   public int getEinsatzProStunde(int jahr, int stunde) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      NumberFormat nf = NumberFormat.getIntegerInstance();
      nf.setMinimumIntegerDigits(2);
      nf.setGroupingUsed(false);
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and zeitAlarm like \'" + nf.format((long)stunde) + "%\';");
      logging.logSQL("SELECT count(*) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and zeitAlarm like \'" + nf.format((long)stunde) + "%\';");
      return result.next()?result.getInt(1):0;
   }

   public int getVerfuegbarkeitProStunde(int jahr, int stunde) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      NumberFormat nf = NumberFormat.getIntegerInstance();
      nf.setMinimumIntegerDigits(2);
      nf.setGroupingUsed(false);
      logging.logSQL("SELECT count(*), sum(staerkeFM), sum(staerkeGF), sum(staerkeZF) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and zeitAlarm like \'" + nf.format((long)stunde) + "%\';");
      ResultSet result = statement.executeQuery("SELECT count(*), sum(staerkeFM), sum(staerkeGF), sum(staerkeZF) FROM `einsatz` WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and zeitAlarm like \'" + nf.format((long)stunde) + "%\';");
      return result.next()?(result.getInt(1) == 0?0:(result.getInt(2) + result.getInt(3) + result.getInt(4)) / result.getInt(1)):0;
   }

   public int getNextID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM `einsatz`;");
      logging.logSQL("SELECT max(id) FROM `einsatz`;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getletzteVeranstaltungID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(veranstaltungID) FROM `einsatz`;");
      logging.logSQL("SELECT max(veranstaltungID) FROM `einsatz`;");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfStadtteil(int jahr, String stadtteil) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` WHERE stadtteil = \'" + stadtteil + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\';");
      logging.logSQL("SELECT count(*) FROM `einsatz` WHERE stadtteil = \'" + stadtteil + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\';");
      return result.next()?result.getInt(1):0;
   }

   public int getEinsatzIDByVeranstaltungID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT einsatzNummer FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT einsatzNummer FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getEinsatzNummerListeForDelete(int einsatzNummer, String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT einsatzNummer FROM einsatz WHERE einsatzNummer > " + einsatzNummer + " and SUBSTR(datum,1,4) like \'" + jahr + "\';");
      ResultSet result = statement.executeQuery("SELECT einsatzNummer FROM einsatz WHERE einsatzNummer > " + einsatzNummer + " and SUBSTR(datum,1,4) like \'" + jahr + "\';");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getVeranstaltungIDbyEinsatzID(int einsatzID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT veranstaltungID FROM einsatz where einsatzNummer = " + einsatzID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\'  ;");
      logging.logSQL("SELECT veranstaltungID FROM einsatz where einsatzNummer = " + einsatzID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\'  ;");
      return result.next()?result.getInt(1):0;
   }

   public String getEinsatzDatum(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datum FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datum FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getEinsatzVonUhr(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ZeitAlarm FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ZeitAlarm FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getEinsatzBisUhr(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ZeitEingerueckt FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ZeitEingerueckt FROM einsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getEinsatznummerForVerdienstausfall(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT e.einsatzNummerOffiziell, s.name, e.ort FROM einsatz e LEFT JOIN stichwort s ON e.Stichwort = s.id where e.veranstaltungID = " + veranstaltungID + " and e.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT e.einsatzNummerOffiziell, s.name, e.ort FROM einsatz e LEFT JOIN stichwort s ON e.Stichwort = s.id where e.veranstaltungID = " + veranstaltungID + " and e.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("1")?"Nr. " + result.getString(1) + ", " + result.getString(2) + " " + result.getString(3):(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("2")?"Nr. " + result.getString(1) + ", " + result.getString(2):(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("3")?"Einsatz-Nr. " + result.getString(1):(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("4")?"Einsatz " + result.getString(2):(((String)runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen")).equals("6")?"Nr. " + result.getString(1) + ", " + (new TabelleEinsatz_kategorie()).getEinsatzKategorieName((new TabelleStichwort()).getStichwortKategorieID(result.getString(2))):"Einsatz-Nr. " + result.getString(1)))))):null;
   }

   public ArrayList getStrasseListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT ort FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by ort");
      ResultSet result = statement.executeQuery("SELECT ort FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by ort");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(result.getString(1).length() >= 45) {
            liste.add(result.getString(1).substring(0, 45) + "...");
         } else {
            liste.add(result.getString(1));
         }
      }

      return liste;
   }

   public ArrayList getStadtteilListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT stadtteil FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by stadtteil");
      ResultSet result = statement.executeQuery("SELECT stadtteil FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by stadtteil");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getBeschreibungListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT beschreibung FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by beschreibung");
      ResultSet result = statement.executeQuery("SELECT beschreibung FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by beschreibung");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(result.getString(1).length() >= 40) {
            liste.add(result.getString(1).substring(0, 40) + "...");
         } else {
            liste.add(result.getString(1));
         }
      }

      return liste;
   }

   public ArrayList getEinsatzleiterBFListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT einsatzleiterBF FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by einsatzleiterBF");
      ResultSet result = statement.executeQuery("SELECT einsatzleiterBF FROM einsatz where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + SbcUtils.timeStamp("yyyy") + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy") + "-12-31\' group by einsatzleiterBF");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector einsatzListe = new Vector();
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
