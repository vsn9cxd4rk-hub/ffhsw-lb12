package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import logging.logging;
import run.runApplication;

public class TabelleEinstellungen {

   public String getEinstellungen(String key) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT wert FROM einstellungen where `key` = \'" + key + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT wert FROM einstellungen where `key` = \'" + key + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getEinstellungenWithMandantID(String key, String mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT wert FROM einstellungen where `key` = \'" + key + "\' and mandantID = " + mandantID + ";");
      ResultSet result = statement.executeQuery("SELECT wert FROM einstellungen where `key` = \'" + key + "\' and mandantID = " + mandantID + ";");
      return result.next()?result.getString(1):null;
   }

   public void update(String key, String wert) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update einstellungen set wert = \'" + wert + "\' where `key` = \'" + key + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public HashMap getAllEinstellungen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result;
      if(this.getVersion().startsWith("Version: 1.")) {
         logging.logSQL("SELECT `key`, wert FROM einstellungen;");
         result = statement.executeQuery("SELECT `key`, wert FROM einstellungen;");
      } else {
         logging.logSQL("SELECT `key`, wert FROM einstellungen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
         result = statement.executeQuery("SELECT `key`, wert FROM einstellungen where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      }

      HashMap map = new HashMap();
      logging.logInfo("Liste der Einstellungen:");

      while(result.next()) {
         map.put(result.getString(1), result.getString(2));
      }

      return map;
   }

   public String getVersion() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT wert FROM einstellungen where `key` = \'version\';");
      ResultSet result = statement.executeQuery("SELECT wert FROM einstellungen where `key` = \'version\';");
      return result.next()?result.getString(1):null;
   }
}
