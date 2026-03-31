package data.tabellen;

import data.DatenbankZugriff;
import go.Ehrungen;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleEhrungenKonfig {

   public void insert(Ehrungen ehrungen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO ehrungen_konfiguration (`id`,`ehrungID`, `zeit`, `mandantID`) VALUES (\'" + ehrungen.getId() + "\', \'" + ehrungen.getEhrungID() + "\', \'" + ehrungen.getZeit() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Ehrungen ehrungen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ehrungen_konfiguration set zeit = " + ehrungen.getZeit() + " where ehrungID = " + ehrungen.getEhrungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM ehrungen_konfiguration;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM ehrungen_konfiguration;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCount(int ehrungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getZeit(int ehrungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT zeit FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT zeit FROM ehrungen_konfiguration where ehrungID = " + ehrungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
