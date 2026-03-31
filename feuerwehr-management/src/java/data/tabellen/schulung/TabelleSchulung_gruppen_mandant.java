package data.tabellen.schulung;

import data.DatenbankZugriff;
import go.schicht.SchichtGruppenMitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleSchulung_gruppen_mandant {

   public void insert(SchichtGruppenMitglieder schichtMitglieder) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schulung_gruppen_mandanten (`gruppenID`, `mandantID`) VALUES (\'" + schichtMitglieder.getSchichtID() + "\', \'" + schichtMitglieder.getMitgliederID() + "\');";
      statement.executeUpdate(sql);
   }

   public void deleteOne(SchichtGruppenMitglieder schichtMitglieder) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schulung_gruppen_mandanten where gruppenID = " + schichtMitglieder.getSchichtID() + " and mandantID = " + schichtMitglieder.getMitgliederID() + ";";
      statement.executeUpdate(sql);
   }

   public void deleteGruppe0() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schulung_gruppen_mandanten where gruppenID = 0;";
      statement.executeUpdate(sql);
   }

   public void deleteAlleEinerGruppe(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Delete from schulung_gruppen_mandanten where gruppenID = " + gruppenID + ";";
      statement.executeUpdate(sql);
   }

   public void updateGruppe0(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update schulung_gruppen_mandanten set gruppenID = " + gruppenID + " where gruppenID = 0;";
      statement.executeUpdate(sql);
   }

   public ArrayList getMitglederEinerSchichtGruppe(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name FROM schulung_gruppen_mandanten sm LEFT JOIN mandant m ON sm.mandantID = m.id where sm.gruppenID = " + gruppenID + " order by m.name;");
      ResultSet result = statement.executeQuery("SELECT m.name FROM schulung_gruppen_mandanten sm LEFT JOIN mandant m ON sm.mandantID = m.id where sm.gruppenID = " + gruppenID + " order by m.name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getCountOfMitglieder(int gruppenID, int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM schulung_gruppen_mandanten where gruppenID = " + gruppenID + " and mandantID = " + mitgliederID + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schulung_gruppen_mandanten where gruppenID = " + gruppenID + " and mandantID = " + mitgliederID + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getMandantIDEinerSchulungGruppe(int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT gruppenID FROM schulung_gruppen_mandanten where mandantID = " + mandantID + ";");
      ResultSet result = statement.executeQuery("SELECT gruppenID FROM schulung_gruppen_mandanten where mandantID = " + mandantID + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }
}
