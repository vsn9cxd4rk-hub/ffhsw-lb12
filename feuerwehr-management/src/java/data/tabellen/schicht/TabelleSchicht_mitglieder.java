package data.tabellen.schicht;

import data.DatenbankZugriff;
import go.schicht.SchichtMitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleSchicht_mitglieder {

   public void insert(SchichtMitglieder schichtMitglieder) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schicht_mitglieder (`schichtID`, `mitgliederID`, `mandantID`) VALUES (\'" + schichtMitglieder.getSchichtID() + "\', \'" + schichtMitglieder.getMitgliederID() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void deleteOne(SchichtMitglieder schichtMitglieder) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schicht_mitglieder where schichtID = " + schichtMitglieder.getSchichtID() + " and mitgliederID = " + schichtMitglieder.getMitgliederID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void deleteAlleSchichtMitglieder(int schichtID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schicht_mitglieder where schichtID = " + schichtID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public ArrayList getMitglederEinerSchicht(int schichtID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM schicht_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.schichtID = " + schichtID + " and sm.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM schicht_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.schichtID = " + schichtID + " and sm.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public int getCountOfMitglieder(int schichtID, int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schicht_mitglieder where schichtID = " + schichtID + " and mitgliederID = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM schicht_mitglieder where schichtID = " + schichtID + " and mitgliederID = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
