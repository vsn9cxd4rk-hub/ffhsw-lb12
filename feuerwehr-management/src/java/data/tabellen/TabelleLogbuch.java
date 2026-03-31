package data.tabellen;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleLogbuch {

   public static Vector headname = new Vector() {

      private static final long serialVersionUID = 1L;

      {
         this.add("Nummer");
         this.add("Datum");
         this.add("Zeit");
         this.add("User");
         this.add("Aktion");
      }
   };


   public void insert(int id, String datum, String zeit, String user, String aktion) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO logbuch (`id`, `datum`, `zeit`, `user`, `aktion`, `mandantID`) VALUES (\'" + id + "\', \'" + datum + "\', \'" + zeit + "\', \'" + user + "\', \'" + aktion + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM logbuch;");
      logging.logSQL("SELECT max(id) FROM logbuch;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM logbuch where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM logbuch where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public void delete() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from logbuch where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL("delete from logbuch where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      statement.executeUpdate(sql);
   }

   public Vector getAll() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM logbuch where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM logbuch where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public Vector getFilterByUser(String user) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM logbuch where user = \'" + user + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM `logbuch` where user = \'" + user + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector logbuch = new Vector();
      logbuch.add(Integer.toString(result.getInt("id")));
      logbuch.add(result.getString("Datum"));
      logbuch.add(result.getString("Zeit"));
      logbuch.add(result.getString("User"));
      logbuch.add(result.getString("Aktion"));
      return logbuch;
   }
}
