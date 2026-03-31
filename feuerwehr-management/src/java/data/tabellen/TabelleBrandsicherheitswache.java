package data.tabellen;

import data.DatenbankZugriff;
import go.Brandsicherheitswachen;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;
import utilities.Utils;

public class TabelleBrandsicherheitswache {

   public void insert(Brandsicherheitswachen bsw) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO brandsicherheitswachen (`id`, `bswNummer`, `jahr`, `veranstaltungID`, `ort` ,`art` , `datum`, `zeit_treffen` ,`zeit_start`, zeit_ende, `mandantID`) VALUES (\'" + bsw.getId() + "\', \'" + bsw.getBswNummer() + "\', \'" + bsw.getJahr() + "\', \'" + bsw.getVeranstaltungID() + "\', \'" + bsw.getOrt() + "\', \'" + bsw.getArt() + "\', \'" + bsw.getDatum() + "\', \'" + bsw.getZeit_treffen() + "\',\'" + bsw.getZeit_start() + "\',\'" + bsw.getZeit_ende() + "\',\'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Brandsicherheitswachen bsw) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update brandsicherheitswachen set ort = \'" + bsw.getOrt() + "\', jahr = \'" + bsw.getJahr() + "\', art = \'" + bsw.getArt() + "\', datum = \'" + bsw.getDatum() + "\', zeit_treffen = \'" + bsw.getZeit_treffen() + "\', zeit_start = \'" + bsw.getZeit_start() + "\', zeit_ende = \'" + bsw.getZeit_ende() + "\' where veranstaltungID = " + bsw.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateBSWNummer(int bswNummerNeu, int bswNummerAlt, String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update brandsicherheitswachen set bswNummer = " + bswNummerNeu + " where bswNummer = " + bswNummerAlt + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from brandsicherheitswachen where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public int getNextNummer(String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(bswNummer) FROM brandsicherheitswachen WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\';");
      ResultSet result = statement.executeQuery("SELECT max(bswNummer) FROM brandsicherheitswachen WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and Datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\';");
      return result.next()?result.getInt(1) + 1:0;
   }

   public HashMap getData(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT datum, ort, art, zeit_treffen, zeit_start, zeit_ende FROM brandsicherheitswachen where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT datum, ort, art, zeit_treffen, zeit_start, zeit_ende FROM brandsicherheitswachen where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("datum", result.getString("datum"));
         map.put("ort", result.getString("ort"));
         map.put("art", result.getString("art"));
         map.put("zeit_treffen", result.getString("zeit_treffen"));
         map.put("zeit_start", result.getString("zeit_start"));
         map.put("zeit_ende", result.getString("zeit_ende"));
      }

      return map;
   }

   public int getNextID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM `brandsicherheitswachen`;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM `brandsicherheitswachen`;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public Vector getAllForTable(String jahr, int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT bswNummer, ort, art, datum, zeit_treffen, veranstaltungID FROM brandsicherheitswachen WHERE mandantID = " + mandantID + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by datum desc;");
      ResultSet result = statement.executeQuery("SELECT bswNummer, ort, art, datum, zeit_treffen, veranstaltungID FROM brandsicherheitswachen WHERE mandantID = " + mandantID + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by datum desc;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getAllVeranstaltungsIDsForList(String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT veranstaltungID FROM brandsicherheitswachen WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by datum desc;");
      ResultSet result = statement.executeQuery("SELECT veranstaltungID FROM brandsicherheitswachen WHERE mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by datum desc;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getBswIDbyVeranstaltungID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT bswNummer FROM `brandsicherheitswachen` where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT bswNummer FROM `brandsicherheitswachen` where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getVeranstaltungIDbyBSWID(int bswID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT veranstaltungID FROM `brandsicherheitswachen` where bswNummer = " + bswID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT veranstaltungID FROM `brandsicherheitswachen` where bswNummer = " + bswID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getOrtListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT ort FROM brandsicherheitswachen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by ort");
      ResultSet result = statement.executeQuery("SELECT ort FROM brandsicherheitswachen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by ort");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getArtListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT art FROM brandsicherheitswachen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by art");
      ResultSet result = statement.executeQuery("SELECT art FROM brandsicherheitswachen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by art");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getBSWNummerListeForDelete(int bswNummer, String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT bswNummer FROM brandsicherheitswachen WHERE bswNummer > " + bswNummer + " and jahr = " + jahr + ";");
      ResultSet result = statement.executeQuery("SELECT bswNummer FROM brandsicherheitswachen WHERE bswNummer > " + bswNummer + " and jahr = " + jahr + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector bswListe = new Vector();
      bswListe.add(Integer.toString(result.getInt("bswNummer")));
      bswListe.add(result.getString("ort"));
      bswListe.add(result.getString("art"));
      bswListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
      bswListe.add(result.getString("zeit_treffen"));
      bswListe.add(Utils.getTeilnehmerEinerVeranstaltung(result.getInt("veranstaltungID")));
      return bswListe;
   }
}
