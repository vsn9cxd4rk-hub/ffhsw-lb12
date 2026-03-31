package data.tabellen.karte;

import data.DatenbankKartenZugriff;
import data.DatenbankZugriff;
import data.tabellen.karte.DBConnectionServiceKarte;
import go.karte.Straße;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleStrassen {

   public ArrayList getStraßenListe() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT name from karte_strassen order by name");
      logging.logSQL("SELECT name from karte_strassen order by name");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getStraßenExport() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT ks.name, ks.plz, ka.anfahrt, ks.info, ks.koordinaten, ks.GPS_N, ks.GPS_O  from karte_strassen ks LEFT JOIN karte_anfahrt ka ON ks.id = ka.strassenID where ks.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and ka.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      ResultSet result = statement.executeQuery("SELECT ks.name, ks.plz, ka.anfahrt, ks.info, ks.koordinaten, ks.GPS_N, ks.GPS_O  from karte_strassen ks LEFT JOIN karte_anfahrt ka ON ks.id = ka.strassenID where ks.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and ka.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         String ergebnis = result.getString("name") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("plz") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + (String)runApplication.EINSTELLUNGEN.get("Stadt") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("anfahrt") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("info") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("koordinaten") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("GPS_N") + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator") + result.getString("GPS_O");
         liste.add(ergebnis);
      }

      return liste;
   }

   public Integer getStrassenCount(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT count(*) from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT count(*) from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?Integer.valueOf(result.getInt(1)):null;
   }

   public int getStrassenID(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT id from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT id from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getInt(1):0;
   }

   public Integer getStrassenNumber(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT id from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT id from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?Integer.valueOf(result.getInt(1)):null;
   }

   public String getStrassenName(int strassenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT name from karte_strassen where id =\'" + strassenID + "\'");
      logging.logSQL("SELECT name from karte_strassen where id =\'" + strassenID + "\'");
      return result.next()?result.getString(1):null;
   }

   public String getStrassenBild(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT bild from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT bild from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getString(1):null;
   }

   public String getStrassenBild2(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT bild2 from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT bild2 from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getString(1):null;
   }

   public String getStraßenInfo(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT info from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT info from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getString(1):null;
   }

   public String getStrassenKoordinaten(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT koordinaten from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT koordinaten from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getString(1):null;
   }

   public String getStrassenGPS_N(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT GPS_N from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT GPS_N from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getString(1):null;
   }

   public String getStrassenGPS_O(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT GPS_O from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT GPS_O from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getString(1):null;
   }

   public String getPLZ(String Strasse) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT plz from karte_strassen where name =\'" + Strasse + "\'");
      logging.logSQL("SELECT plz from karte_strassen where name =\'" + Strasse + "\'");
      return result.next()?result.getString(1):null;
   }

   public void insert(Straße straße) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "INSERT INTO karte_strassen (`id`, `name`, `bild`, `bild2`, `info`, `koordinaten`, `GPS_N`, `GPS_O`, `PLZ`, `mandantID`) VALUES (\'" + straße.getId() + "\', \'" + straße.getName() + "\', \'" + straße.getBild() + "\', \'" + straße.getBild2() + "\', \'" + straße.getInfo() + "\', \'" + straße.getKoordinaten() + "\', \'" + straße.getGPS_N() + "\', \'" + straße.getGPS_O() + "\', \'" + straße.getPLZ() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void update(Straße straße) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "Update `karte_strassen` set `name` = \'" + straße.getName() + "\', `info` = \'" + straße.getInfo() + "\', `koordinaten` = \'" + straße.getKoordinaten() + "\', `GPS_N` = \'" + straße.getGPS_N() + "\', `GPS_O` = \'" + straße.getGPS_O() + "\', `bild` = \'" + straße.getBild() + "\', `bild2` = \'" + straße.getBild2() + "\' where `id` = " + straße.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteLokal() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from `karte_strassen`";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM karte_strassen;");
      logging.logSQL("SELECT max(id) FROM karte_strassen;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCount() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM karte_strassen;");
      logging.logSQL("SELECT count(*) FROM karte_strassen;");
      return result.next()?result.getInt(1):0;
   }

   public int getStraßenCount(String straße) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM karte_strassen where name = \'" + straße + "\';");
      logging.logSQL("SELECT count(*) FROM karte_strassen where name = \'" + straße + "\';");
      return result.next()?result.getInt(1):0;
   }

   public void deleteTable() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_strassen;";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int straßenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_strassen where id = " + straßenID + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public String getDataForStrassenLokalBackup() throws SQLException, NullPointerException {
      Statement statement = DatenbankKartenZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("select * from karte_strassen;");
      ResultSet result = statement.executeQuery("select * from karte_strassen;");
      StringBuilder insert = new StringBuilder();
      insert.append("Insert INTO karte_strassen (`id`, `name`, `bild`, `bild2`, `info`,`koordinaten`, `GPS_N`, `GPS_O`, `PLZ`, `mandantID`) VALUES ");

      for(int counter = 0; result.next(); ++counter) {
         if(counter != 0) {
            insert.append(",");
         }

         insert.append("(\'");
         insert.append(result.getString("id"));
         insert.append("\', \'");
         insert.append(result.getString("name"));
         insert.append("\', \'");
         insert.append(result.getString("bild"));
         insert.append("\', \'");
         insert.append(result.getString("bild2"));
         insert.append("\', \'");
         insert.append(result.getString("info"));
         insert.append("\', \'");
         insert.append(result.getString("koordinaten"));
         insert.append("\', \'");
         insert.append(result.getString("GPS_N"));
         insert.append("\', \'");
         insert.append(result.getString("GPS_O"));
         insert.append("\', \'");
         insert.append(result.getString("PLZ"));
         insert.append("\', \'");
         insert.append(result.getString("mandantID"));
         insert.append("\')");
      }

      logging.logSQL(insert.toString());
      return insert.toString();
   }
}
