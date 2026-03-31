package data.tabellen.karte;

import data.DatenbankKartenZugriff;
import data.DatenbankZugriff;
import data.tabellen.karte.DBConnectionServiceKarte;
import go.karte.Hydrant;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleHydranten {

   public static Vector headnameHydranten = new Vector() {

      private static final long serialVersionUID = 1L;

      {
         this.add("ID");
         this.add("Beschreibung");
         this.add("Hausnummer");
         this.add("Nennweite");
         this.add("GPS_N");
         this.add("GPS_O");
      }
   };


   public Integer getNextIndex() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT max(id) from karte_hydranten");
      ResultSet result = statement.executeQuery("SELECT max(id) from karte_hydranten");
      return result.next()?Integer.valueOf(result.getInt(1) + 1):null;
   }

   public Integer getHydrantID(Hydrant hydrant) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT id from karte_hydranten where starssenid = " + hydrant.getStrassenid() + " and hausnummerID =  " + hydrant.getHausnummerID() + " and nennweite = " + hydrant.getNennweite() + " and hausnummer = \'" + hydrant.getHausnummer() + "\';");
      ResultSet result = statement.executeQuery("SELECT id from karte_hydranten where starssenid = " + hydrant.getStrassenid() + " and hausnummerID =  " + hydrant.getHausnummerID() + " and nennweite = " + hydrant.getNennweite() + " and hausnummer = \'" + hydrant.getHausnummer() + "\';");
      return result.next()?Integer.valueOf(result.getInt(1)):Integer.valueOf(0);
   }

   public void insert(Hydrant hydrant) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "INSERT INTO karte_hydranten (`id`, `starssenid`, `hausnummer`, `hausnummerID`, `nennweite`, `GPS_N`, `GPS_O`, `mandantID`) VALUES (\'" + hydrant.getId() + "\', \'" + hydrant.getStrassenid() + "\', \'" + hydrant.getHausnummer() + "\', \'" + hydrant.getHausnummerID() + "\', \'" + hydrant.getNennweite() + "\', \'" + hydrant.getGPS_N() + "\', \'" + hydrant.getGPS_O() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void update(Hydrant hydrant) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "Update `karte_hydranten` set GPS_N = \'" + hydrant.getGPS_N() + "\', GPS_O = \'" + hydrant.getGPS_O() + "\', starssenid = \'" + hydrant.getStrassenid() + "\', hausnummer = \'" + hydrant.getHausnummer() + "\', hausnummerID = \'" + hydrant.getHausnummerID() + "\', nennweite = \'" + hydrant.getNennweite() + "\' where id = " + hydrant.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteLokal() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from `karte_hydranten`";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public HashMap getHydrantData(int hydrantID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT s.name, h.id, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O FROM karte_hydranten h left join karte_strassen s ON h.starssenid = s.id where h.id = " + hydrantID + ";");
      ResultSet result = statement.executeQuery("SELECT s.name, h.id, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O FROM karte_hydranten h left join karte_strassen s ON h.starssenid = s.id where h.id = " + hydrantID + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("GPS_N", result.getString("GPS_N"));
         map.put("GPS_O", result.getString("GPS_O"));
         map.put("hausnummer", result.getString("hausnummer"));
         map.put("hausnummerID", result.getString("hausnummerID"));
         map.put("id", result.getString("id"));
         map.put("nennweite", result.getString("nennweite"));
         map.put("straßeName", result.getString("name"));
      }

      return map;
   }

   public void updateHydrantenKoordinaten(Hydrant hydrant) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "Update `karte_hydranten` set GPS_N = \'" + hydrant.getGPS_N() + "\', GPS_O = \'" + hydrant.getGPS_O() + "\' where id = " + hydrant.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public Integer getHydrantCountByID(Hydrant hydrant) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT count(*) from karte_hydranten where id = " + hydrant.getId() + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) from karte_hydranten where id = " + hydrant.getId() + ";");
      return result.next()?Integer.valueOf(result.getInt(1)):null;
   }

   public Integer getCount() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT count(*) from karte_hydranten;");
      ResultSet result = statement.executeQuery("SELECT count(*) from karte_hydranten;");
      return result.next()?Integer.valueOf(result.getInt(1)):null;
   }

   public List select(String strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT h.hausnummer, h.nennweite FROM karte_hydranten h left join karte_strassen s ON h.starssenid = s.id where s.name = \'" + strasse + "\' order by h.id");
      ResultSet result = statement.executeQuery("SELECT h.hausnummer, h.nennweite FROM karte_hydranten h left join karte_strassen s ON h.starssenid = s.id where s.name = \'" + strasse + "\' order by h.id");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(this.mapToObject(result));
      }

      return liste;
   }

   private Hydrant mapToObject(ResultSet result) throws SQLException {
      Hydrant h = new Hydrant();
      h.setHausnummer(result.getString(1));
      h.setNennweite(result.getInt(2));
      return h;
   }

   public void deleteLastEntry() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      int hyid = this.getNextIndex().intValue() - 1;
      String sql = "delete from karte_hydranten where id = " + hyid + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int straßenID, int hydrantID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_hydranten where starssenid = " + straßenID + " and id = " + hydrantID + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteAll(int straßenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_hydranten where starssenid = " + straßenID + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteTable() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_hydranten;";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getHydrantenExport() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT h.id, s.name, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O  from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id  order by h.id");
      ResultSet result = statement.executeQuery("SELECT h.id, s.name, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O  from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id  order by h.id");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         String ergebnis = result.getString("id") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("name") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("hausnummer") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("hausnummerID") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("nennweite") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("GPS_N") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("GPS_O");
         liste.add(ergebnis);
      }

      return liste;
   }

   public ArrayList getHydrantenExportOSMServer(String nennweite) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT s.name, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O  from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id  where h.nennweite = " + nennweite + " order by s.name");
      ResultSet result = statement.executeQuery("SELECT s.name, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O  from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id  where h.nennweite = " + nennweite + " order by s.name");

      ArrayList liste;
      String ergebnis;
      for(liste = new ArrayList(); result.next(); liste.add(ergebnis)) {
         if(result.getInt("hausnummerID") == 0) {
            ergebnis = result.getString("GPS_N") + "\t" + result.getString("GPS_O") + "\t" + "Hydrant H" + result.getString("nennweite") + "\t" + result.getString("name") + result.getString("hausnummer") + "\t" + result.getString("nennweite") + ".png" + "\t";
         } else {
            ergebnis = result.getString("GPS_N") + "\t" + result.getString("GPS_O") + "\t" + "Hydrant H" + result.getString("nennweite") + "\t" + result.getString("name") + " " + result.getString("hausnummerID") + "\t" + result.getString("nennweite") + ".png" + "\t";
         }
      }

      return liste;
   }

   public ArrayList getAllNennweiten() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT nennweite FROM karte_hydranten group by nennweite;");
      ResultSet result = statement.executeQuery("SELECT nennweite FROM karte_hydranten group by nennweite;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getIDListe() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT id from karte_hydranten where GPS_N = \'\' and GPS_O = \'\';");
      ResultSet result = statement.executeQuery("SELECT id from karte_hydranten where GPS_N = \'\' and GPS_O = \'\';");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public String getStraßeUndHausnummerByHydrantID(int hydrantID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT ks.name, kh.hausnummer, kh.hausnummerID from karte_hydranten kh LEFT JOIN karte_strassen ks ON kh.starssenid = ks.id where kh.id = " + hydrantID + ";");
      ResultSet result = statement.executeQuery("SELECT ks.name, kh.hausnummer, kh.hausnummerID from karte_hydranten kh LEFT JOIN karte_strassen ks ON kh.starssenid = ks.id where kh.id = " + hydrantID + ";");
      return result.next()?(result.getInt(3) == 0?result.getString(1) + result.getString(2):result.getString(1) + result.getString(3)):null;
   }

   public Vector getHydrantenForTable(int straßenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT h.id, s.name, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O  from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id where starssenid = " + straßenID + " order by s.name;");
      ResultSet result = statement.executeQuery("SELECT h.id, s.name, h.hausnummer, h.hausnummerID, h.nennweite, h.GPS_N, h.GPS_O  from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id where starssenid = " + straßenID + " order by s.name;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getHydrantenIDForTable(int straßenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT h.id from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id where starssenid = " + straßenID + " order by s.name;");
      ResultSet result = statement.executeQuery("SELECT h.id from karte_hydranten h LEFT JOIN karte_strassen s ON h.starssenid = s.id where starssenid = " + straßenID + " order by s.name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      logging.logSQL(liste);
      return liste;
   }

   public String getDataForHydrantenLokalBackup() throws SQLException, NullPointerException {
      Statement statement = DatenbankKartenZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("select * from karte_hydranten;");
      ResultSet result = statement.executeQuery("select * from karte_hydranten;");
      StringBuilder insert = new StringBuilder();
      insert.append("Insert INTO karte_hydranten (`id`, `starssenid`, `hausnummer`, `hausnummerID`, `nennweite`, `GPS_N`,`GPS_O`, `mandantID`) VALUES ");

      for(int counter = 0; result.next(); ++counter) {
         if(counter != 0) {
            insert.append(",");
         }

         insert.append("(\'");
         insert.append(result.getString("id"));
         insert.append("\', \'");
         insert.append(result.getString("starssenid"));
         insert.append("\', \'");
         insert.append(result.getString("hausnummer"));
         insert.append("\', \'");
         insert.append(result.getString("hausnummerID"));
         insert.append("\', \'");
         insert.append(result.getString("nennweite"));
         insert.append("\', \'");
         insert.append(result.getString("GPS_N"));
         insert.append("\', \'");
         insert.append(result.getString("GPS_O"));
         insert.append("\', \'");
         insert.append(result.getString("mandantID"));
         insert.append("\')");
      }

      logging.logSQL(insert.toString());
      return insert.toString();
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector hydranten = new Vector();
      hydranten.add(result.getString("id"));
      hydranten.add(result.getString("hausnummer"));
      if(result.getInt("hausnummerID") != 0) {
         hydranten.add(result.getString("hausnummerID"));
      } else {
         hydranten.add("");
      }

      hydranten.add(result.getString("nennweite"));
      hydranten.add(result.getString("GPS_N"));
      hydranten.add(result.getString("GPS_O"));
      return hydranten;
   }
}
