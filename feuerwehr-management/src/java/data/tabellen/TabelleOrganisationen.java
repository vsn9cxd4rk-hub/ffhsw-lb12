package data.tabellen;

import data.DatenbankZugriff;
import go.Organisation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleOrganisationen {

   public void insert(Organisation organisation) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO organisationen (`id`, `name` , `sortierung` , `mandantID`) VALUES (\'" + organisation.getId() + "\', \'" + organisation.getName() + "\', \'" + organisation.getSortierung() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Organisation organisation) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update organisationen set name = \'" + organisation.getName() + "\',  sortierung = " + organisation.getSortierung() + " where id = " + organisation.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from organisationen where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM organisationen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(id) FROM organisationen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public String getOrganisationName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM organisationen where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM organisationen where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAllOrganisationenWithout1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM organisationen where id > 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung, id;");
      logging.logSQL("SELECT name FROM organisationen where id > 1 mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung, id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllOrganisationenIDsWithout1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM organisationen where id > 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      logging.logSQL("SELECT id FROM organisationen where id > 1 mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung, id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAllOrganisationenIDs() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM organisationen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM organisationen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getOrganisationID(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM organisationen where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM organisationen where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getSortierung(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sortierung FROM organisationen where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sortierung FROM organisationen where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfSortierung(int sorterungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM organisationen where sortierung = " + sorterungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM organisationen where sortierung = " + sorterungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getOrganisationenCount(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM organisationen where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM organisationen where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public Organisation getData(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id, name, sortierung FROM organisationen where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id, name, sortierung FROM organisationen where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Organisation organisation = new Organisation();

      while(result.next()) {
         organisation.setId(result.getInt("id"));
         organisation.setName(result.getString("name"));
         organisation.setSortierung(result.getInt("sortierung"));
      }

      return organisation;
   }
}
