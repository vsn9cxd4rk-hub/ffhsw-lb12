package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import data.DatenbankZugriffGrundkonfiguration;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleMandant {

   public void update(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mandant set name = \'" + name + "\' where id = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void update(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mandant set id = " + id + " where id = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getMandantID(String mandantName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM mandant where name = \'" + mandantName + "\';");
      ResultSet result = statement.executeQuery("SELECT id FROM mandant where name = \'" + mandantName + "\';");
      return result.next()?result.getInt(1):1;
   }

   public String getMandantName(int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM mandant where id = \'" + mandantID + "\';");
      logging.logSQL("SELECT name FROM mandant where id = \'" + mandantID + "\';");
      return result.next()?result.getString(1):null;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriffGrundkonfiguration.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM mandant;");
      logging.logSQL("SELECT max(id) FROM mandant;");
      return result.next()?result.getInt(1) + 1:1;
   }

   public int getBFStatus() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT bf FROM mandant where id = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT bf FROM mandant where id = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllMandanten() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM mandant order by name;");
      ResultSet result = statement.executeQuery("SELECT name FROM mandant order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
