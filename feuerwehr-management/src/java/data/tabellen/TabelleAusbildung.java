package data.tabellen;

import data.DatenbankZugriff;
import go.Ausbildung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleAusbildung {

   public void insert(Ausbildung ausbildung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO ausbildung (`id`, `jahr`, `veranstaltungID`, `mitgliederGruppe`,`ausbildungKategorie`, `mitgliederID` , `mandantID`) VALUES (\'" + ausbildung.getId() + "\', \'" + ausbildung.getJahr() + "\', \'" + ausbildung.getVeranstaltungID() + "\', \'" + ausbildung.getMitgliederGruppe() + "\', \'" + ausbildung.getAusbildungKategorieID() + "\', \'" + ausbildung.getMitgliederID() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getAusbildungStatus(int mitgliederID, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM ausbildung where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ausbildung where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM ausbildung;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM ausbildung;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getStatusFromDatabase(int mitgliederID, int veranstaltungID, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getBeteiligungByKategorie(int mitgliederID, int ausbildungKategorie, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and ausbildungKategorie = " + ausbildungKategorie + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and ausbildungKategorie = " + ausbildungKategorie + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
