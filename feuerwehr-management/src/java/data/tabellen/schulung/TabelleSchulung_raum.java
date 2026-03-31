package data.tabellen.schulung;

import data.DatenbankZugriff;
import go.schulung.Raum;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleSchulung_raum {

   public void insert(Raum raum) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schulung_raum (`id`, `name`) VALUES (\'" + raum.getId() + "\', \'" + raum.getName() + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM schulung_raum;");
      logging.logSQL("SELECT max(id) FROM schulung_raum;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getRaumID(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM schulung_raum where name = \'" + name + "\';");
      logging.logSQL("SELECT id FROM schulung_raum where name = \'" + name + "\';");
      return result.next()?result.getInt(1):0;
   }

   public String getRaumName(int raumID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM schulung_raum where id = " + raumID + ";");
      logging.logSQL("SELECT name FROM schulung_raum where id = " + raumID + ";");
      return result.next()?result.getString(1):null;
   }

   public int getCount(String raumName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schulung_raum where name = \'" + raumName + "\';");
      logging.logSQL("SELECT count(*) FROM schulung_raum where name = \'" + raumName + "\';");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAlleRäume() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name from schulung_raum order by name;");
      logging.logSQL("SELECT name from schulung_raum order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
