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
      String sql = "INSERT INTO lehrgang_kategorie (`id`, `art`, `name`, `relevant`, `reihenfolge`, `ue`, `loeschbar`, `loeschkenner`, `mandantID`) VALUES (\'" + kategorie.getId() + "\', \'" + kategorie.getArt() + "\', \'" + kategorie.getName() + "\', \'" + kategorie.getRelevant() + "\', \'" + kategorie.getReihenfolge() + "\', \'" + kategorie.getUe() + "\', \'" + kategorie.getLoeschbar() + "\', \'" + kategorie.getLoeschkenner() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Lehrgang_Kategorie kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update lehrgang_kategorie set relevant = " + kategorie.getRelevant() + ", reihenfolge = " + kategorie.getReihenfolge() + " where name = \'" + kategorie.getName() + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateUnterrichtseinheiten(Lehrgang_Kategorie kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update lehrgang_kategorie set ue = " + kategorie.getUe() + " where id = \'" + kategorie.getId() + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateLoeschkenner(Lehrgang_Kategorie kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update lehrgang_kategorie set loeschkenner = " + kategorie.getLoeschkenner() + " where id = \'" + kategorie.getId() + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getAlleLehrgänge() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'F\', \'F_Außerhalb\', \'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'F\', \'F_Außerhalb\', \'L\', \'Fü\', \'S\', \'E\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleLehrgängeByName() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'F\', \'F_Außerhalb\', \'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'F\', \'F_Außerhalb\', \'L\', \'Fü\', \'S\', \'E\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleLehrgängeSeminare() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'L\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'L\', \'S\', \'E\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleFührerschein() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'Fü\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'Fü\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleFunktionen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'F\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'F\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleFunktionenAußerhalb() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'F_Außerhalb\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'F_Außerhalb\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleEhrungenAbzeichen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'EH\', \'AB\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'EH\', \'AB\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleEhrungen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'EH\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'EH\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleEhrungenIDs() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where art in (\'EH\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where art in (\'EH\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleAbzeichen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where art in (\'AB\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where art in (\'AB\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleAbzeichenIDs() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where art in (\'AB\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where art in (\'AB\') and  loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getFilterNameLehrgang(int[] lehrgangKategorieIDs) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      StringBuilder build = new StringBuilder();
      build.append("(");

      for(int result = 0; result < lehrgangKategorieIDs.length; ++result) {
         build.append(lehrgangKategorieIDs[result]);
         if(result != lehrgangKategorieIDs.length - 1) {
            build.append(",");
         }
      }

      build.append(")");
      logging.logSQL("SELECT name FROM lehrgang_kategorie where loeschkenner = 0 and id in " + build.toString() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet var6 = statement.executeQuery("SELECT name FROM lehrgang_kategorie where loeschkenner = 0 and id in " + build.toString() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(var6.next()) {
         liste.add(var6.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleLehrgängeID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F\', \'F_Außerhalb\', \'L\', \'Fü\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F\', \'F_Außerhalb\', \'L\', \'Fü\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleLehrgängeSeminarID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'L\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'L\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleLehrgängeSeminareID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'L\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'L\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleFührerscheinID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'Fü\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'Fü\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleFunktionenID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleFunktionenAußerhalbID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F_Außerhalb\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F_Außerhalb\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleRelevantenNamen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where relevant = 1 and art in (\'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where relevant = 1 and art in (\'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleRelevantenIDs() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where relevant = 1 and art in (\'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where relevant = 1 and art in (\'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleNichtRelevantenNamen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM lehrgang_kategorie where relevant = 0 and art in (\'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      ResultSet result = statement.executeQuery("SELECT name FROM lehrgang_kategorie where relevant = 0 and art in (\'L\', \'Fü\', \'S\', \'E\') and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public String getAlleRelevantenDBNamen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by reihenfolge;");
      StringBuilder liste = new StringBuilder();

      for(int count = 0; result.next(); ++count) {
         if(count != 0) {
            liste.append(", ");
         }

         liste.append(result.getString(1));
      }

      return liste.toString();
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM lehrgang_kategorie;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM lehrgang_kategorie;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public String getArt(int lehrgangID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT art FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT art FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getNextReihenfolgenummerNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(reihenfolge) FROM lehrgang_kategorie where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT max(reihenfolge) FROM lehrgang_kategorie where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getLehrgangID(String lehrgang) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where name = \'" + lehrgang + "\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where name = \'" + lehrgang + "\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountByName(String lehrgang) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lehrgang_kategorie where name = \'" + lehrgang + "\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM lehrgang_kategorie where name = \'" + lehrgang + "\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountAlleRelevanten() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM lehrgang_kategorie where relevant = 1 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F\', \'L\', \'Fü\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM lehrgang_kategorie where loeschkenner = 0 and art in (\'F\', \'L\', \'Fü\', \'S\', \'E\') and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getUnterrichtseinheiten(int lehrgangID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT ue FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT ue FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getloeschbarStatus(int lehrgangID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT loeschbar FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT loeschbar FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getRelevantStatus(int lehrgangID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT relevant FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT relevant FROM lehrgang_kategorie where id = " + lehrgangID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
