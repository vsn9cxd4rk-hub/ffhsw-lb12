package data.tabellen.schulung;

import data.DatenbankZugriff;
import go.schulung.SchulungDetails;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import utilities.TimeCalculation;

public class TabelleSchulungDetails {

   public void insert(SchulungDetails schulungDetails) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schulung_details (`id`,`schulungID`,`jahr`,  `datum`,`raumID`,`inhalt`,`fahrzeug1`,`fahrzeug2`) VALUES (\'" + schulungDetails.getId() + "\', \'" + schulungDetails.getSchulungID() + "\', \'" + schulungDetails.getJahr() + "\', \'" + schulungDetails.getDatum() + "\', \'" + schulungDetails.getRaumID() + "\', \'" + schulungDetails.getInhalt() + "\', \'" + schulungDetails.getFahrzeug1() + "\', \'" + schulungDetails.getFahrzeug2() + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM schulung_details;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM schulung_details;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getRaumIDBySchlungstag(String datum, int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT raumID FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT raumID FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getInhaltBySchlungstag(String datum, int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT inhalt FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT inhalt FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      return result.next()?result.getString(1):null;
   }

   public int getFahrzeug1IDBySchlungstag(String datum, int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fahrzeug1 FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT fahrzeug1 FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getFahrzeug2IDBySchlungstag(String datum, int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fahrzeug2 FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT fahrzeug2 FROM schulung_details where datum = \'" + datum + "\' and schulungID = " + schulungID + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAlleTermineEinesMonats(String monat, int jahr, int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT datum from schulung_details where jahr = " + jahr + " and datum like \'" + jahr + "-" + monat + "-%\' and schulungID = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT datum from schulung_details where jahr = " + jahr + " and datum like \'" + jahr + "-" + monat + "-%\' and schulungID = " + schulungID + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleSchulungstage(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT datum from schulung_details where schulungID = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT datum from schulung_details where schulungID = " + schulungID + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public Vector getDetailsForTable(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT s.name as schulungName, sd.datum, sr.name as raumname, sd.inhalt FROM schulung_details sd LEFT JOIN schulung_raum sr ON sd.raumID = sr.id LEFT JOIN schulung s ON s.id = sd.schulungID WHERE schulungID = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT s.name as schulungName, sd.datum, sr.name as raumname, sd.inhalt FROM schulung_details sd LEFT JOIN schulung_raum sr ON sd.raumID = sr.id LEFT JOIN schulung s ON s.id = sd.schulungID WHERE schulungID = " + schulungID + ";");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector liste = new Vector();
      liste.add(result.getString("schulungName"));
      liste.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
      liste.add(result.getString("raumname"));
      liste.add(result.getString("inhalt"));
      return liste;
   }
}
