package data.tabellen.fahrzeug;

import data.DatenbankZugriff;
import go.Fahrzeug_beschreibung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleFahrzeug_beschreibung {

   public void insert(Fahrzeug_beschreibung gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO fahrzeug_beschreibung (`id`, `beschreibung`) VALUES (\'" + gruppe.getId() + "\', \'" + gruppe.getName() + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Fahrzeug_beschreibung gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update fahrzeug_beschreibung set beschreibung = \'" + gruppe.getName() + "\' where id = " + gruppe.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from fahrzeug_beschreibung where beschreibung = \'" + name + "\';";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrzeug_beschreibung;");
      logging.logSQL("SELECT max(id) FROM fahrzeug_beschreibung;");
      return result.next()?(result.getInt(1) <= 49?51:result.getInt(1) + 1):0;
   }

   public int getFahrzeugGruppenID(String beschreibung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeug_beschreibung where beschreibung = \'" + beschreibung + "\';");
      logging.logSQL("SELECT count(*) FROM fahrzeug_beschreibung where beschreibung = \'" + beschreibung + "\';");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllFahrzeugBeschreibungen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT beschreibung FROM fahrzeug_beschreibung order by id;");
      ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeug_beschreibung order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugBeschreibungenID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM fahrzeug_beschreibung order by id;");
      ResultSet result = statement.executeQuery("SELECT id FROM fahrzeug_beschreibung order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public String getBeschreibungName(int beschreibungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT beschreibung FROM fahrzeug_beschreibung where id = " + beschreibungID + ";");
      ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeug_beschreibung where id = " + beschreibungID + ";");
      return result.next()?result.getString(1):null;
   }

   public int getBeschreibungID(String beschreibungName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM fahrzeug_beschreibung where beschreibung = \'" + beschreibungName + "\';");
      ResultSet result = statement.executeQuery("SELECT id FROM fahrzeug_beschreibung where beschreibung = \'" + beschreibungName + "\';");
      return result.next()?result.getInt(1):0;
   }
}
