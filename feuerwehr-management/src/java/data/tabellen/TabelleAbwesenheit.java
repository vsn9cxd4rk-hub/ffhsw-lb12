package data.tabellen;

import data.DatenbankZugriff;
import go.Abwesenheit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleAbwesenheit {

   public void insert(Abwesenheit abwesenheit) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO abwesenheit (`id`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `mitgliederID`, `grund` , `mandantID`) VALUES (\'" + abwesenheit.getId() + "\', \'" + abwesenheit.getJahr() + "\', \'" + abwesenheit.getVeranstaltungID() + "\', \'" + abwesenheit.getVeranstaltungKategorie() + "\', \'" + abwesenheit.getMitgliederID() + "\', \'" + abwesenheit.getGrund() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Abwesenheit abwesenheit) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update abwesenheit set grund = " + abwesenheit.getGrund() + " where veranstaltungID = " + abwesenheit.getVeranstaltungID() + " and mitgliederID = " + abwesenheit.getMitgliederID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int mitgliederID, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from abwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from abwesenheit where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM abwesenheit;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM abwesenheit;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCountOfAbwesenheitsgrund(int mitgliederID, int abwesenheitsgrund, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM abwesenheit where mitgliederID = " + mitgliederID + " and grund = " + abwesenheitsgrund + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abwesenheit where mitgliederID = " + mitgliederID + " and grund = " + abwesenheitsgrund + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountByVeranstaltung(int veranstaltungID, int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM abwesenheit where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abwesenheit where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCount(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM abwesenheit where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abwesenheit where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getGrundOfAbwesenheitsByUser(int mitgliederID, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT grund FROM abwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT grund FROM abwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAbwesendeMitgliederIDListe(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT mitgliederID FROM abwesenheit where veranstaltungID = " + veranstaltungID + "  and grund != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT mitgliederID FROM abwesenheit where veranstaltungID = " + veranstaltungID + " and grund != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAbwesendGrundIDListe(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT grund FROM abwesenheit where veranstaltungID = " + veranstaltungID + "  and grund != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT grund FROM abwesenheit where veranstaltungID = " + veranstaltungID + " and grund != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }
}
