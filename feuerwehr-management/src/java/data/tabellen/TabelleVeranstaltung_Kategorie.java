package data.tabellen;

import data.DatenbankZugriff;
import go.Veranstaltung_Kategorie;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleVeranstaltung_Kategorie {

   public void insert(Veranstaltung_Kategorie kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO veranstaltung_kategorie (`id`, `name` , `mandantID`) VALUES (\'" + kategorie.getId() + "\', \'" + kategorie.getName() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Veranstaltung_Kategorie kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update veranstaltung_kategorie set name = \'" + kategorie.getName() + "\' where id = " + kategorie.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from veranstaltung_kategorie where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM veranstaltung_kategorie;");
      logging.logSQL("SELECT max(id) FROM veranstaltung_kategorie;");
      return result.next()?(result.getInt(1) <= 10?11:result.getInt(1) + 1):0;
   }

   public int getID(String kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung_kategorie where name = \'" + kategorie + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM veranstaltung_kategorie where name = \'" + kategorie + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung_kategorie where id = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM veranstaltung_kategorie where id = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAllKategorien() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung_kategorie where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT name FROM veranstaltung_kategorie where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllKategorienID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung_kategorie where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT id FROM veranstaltung_kategorie where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getCount(String kategorieName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung_kategorie where name = \'" + kategorieName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM veranstaltung_kategorie where name = \'" + kategorieName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
