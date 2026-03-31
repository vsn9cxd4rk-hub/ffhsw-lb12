package data.tabellen.schicht;

import data.DatenbankZugriff;
import go.schicht.SchichtGruppenMitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleSchicht_gruppen_mitglieder {

   public void insert(SchichtGruppenMitglieder schichtMitglieder) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schicht_gruppen_mitglieder (`gruppenID`, `mitgliederID`, `mandantID`) VALUES (\'" + schichtMitglieder.getSchichtID() + "\', \'" + schichtMitglieder.getMitgliederID() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void deleteOne(SchichtGruppenMitglieder schichtMitglieder) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schicht_gruppen_mitglieder where gruppenID = " + schichtMitglieder.getSchichtID() + " and mitgliederID = " + schichtMitglieder.getMitgliederID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void deleteGruppe0() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schicht_gruppen_mitglieder where gruppenID = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void deleteAlleEinerGruppe(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void updateGruppe0(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update schicht_gruppen_mitglieder set gruppenID = " + gruppenID + " where gruppenID = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public ArrayList getMitglederEinerSchichtGruppe(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM schicht_gruppen_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.gruppenID = " + gruppenID + " and sm.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM schicht_gruppen_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.gruppenID = " + gruppenID + " and sm.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public int getCountOfMitglieder(int gruppenID, int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mitgliederID = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mitgliederID = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getMitglederIDEinerSchichtGruppe(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT mitgliederID FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT mitgliederID FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }
}
