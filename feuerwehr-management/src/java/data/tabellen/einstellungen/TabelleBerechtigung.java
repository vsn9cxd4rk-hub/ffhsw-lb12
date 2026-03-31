package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBerechtigung {

   public void updateBerechtigungName(int seite, int id, String neuerName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update berechtigung set name = \'" + neuerName + "\' where id = " + id + " and seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getBerechtigungName(int seite) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name from berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT name from berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getBerechtigungNameUndFrei(int seite) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name from berechtigung where seite = " + seite + " and name like \'frei%\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT name from berechtigung where seite = " + seite + " and name like \'frei%\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getBerechtigungIDs(int seite) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id from berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT id from berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add("BR" + result.getString(1));
      }

      return liste;
   }

   public ArrayList getBerechtigungGruppe(int seite) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT gruppe from berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT gruppe from berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getCount(int seite) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM berechtigung where seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getID(int seite, String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM berechtigung where seite = " + seite + " and name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM berechtigung where seite = " + seite + " and name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getName(int seite, int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM berechtigung where seite = " + seite + " and id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT name FROM berechtigung where seite = " + seite + " and id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }
}
