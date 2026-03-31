package data.tabellen.schulung;

import data.DatenbankZugriff;
import go.schicht.SchichtGruppe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleSchulung_gruppe {

   public void insert(SchichtGruppe schichtGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schulung_gruppe (`id`, `name`) VALUES (\'" + schichtGruppe.getId() + "\', \'" + schichtGruppe.getName() + "\');";
      statement.executeUpdate(sql);
   }

   public int getCount(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schulung_gruppe where name = \'" + name + "\';");
      logging.logSQL("SELECT count(*) FROM schulung_gruppe where name = \'" + name + "\';");
      return result.next()?result.getInt(1):0;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM schulung_gruppe;");
      logging.logSQL("SELECT max(id) FROM schulung_gruppe;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getGruppenID(String gruppenName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM schulung_gruppe where name = \'" + gruppenName + "\';");
      ResultSet result = statement.executeQuery("SELECT id FROM schulung_gruppe where name = \'" + gruppenName + "\';");
      return result.next()?result.getInt(1):0;
   }

   public String getGruppenName(int gruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM schulung_gruppe where id = \'" + gruppenID + "\';");
      ResultSet result = statement.executeQuery("SELECT name FROM schulung_gruppe where id = \'" + gruppenID + "\';");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAllschulungGruppen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM schulung_gruppe;");
      ResultSet result = statement.executeQuery("SELECT name FROM schulung_gruppe;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
