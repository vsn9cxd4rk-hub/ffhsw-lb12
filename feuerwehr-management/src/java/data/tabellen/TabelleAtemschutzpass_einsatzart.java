package data.tabellen;

import data.DatenbankZugriff;
import go.Atemschutzpass_einsatzart;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleAtemschutzpass_einsatzart {

   public void insert(Atemschutzpass_einsatzart einsatzart) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO atemschutzpass_einsatzart (`id`, `name`) VALUES (\'" + einsatzart.getId() + "\', \'" + einsatzart.getName() + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM atemschutzpass_einsatzart;");
      logging.logSQL("SELECT max(id) FROM atemschutzpass_einsatzart;");
      return result.next()?(result.getInt(1) <= 10?11:result.getInt(1) + 1):0;
   }

   public int getID(String kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM atemschutzpass_einsatzart where name = \'" + kategorie + "\';");
      logging.logSQL("SELECT id FROM atemschutzpass_einsatzart where name = \'" + kategorie + "\';");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllKategorien() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM atemschutzpass_einsatzart order by id;");
      logging.logSQL("SELECT name FROM atemschutzpass_einsatzart order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getCount(String kategorieName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM atemschutzpass_einsatzart where name = \'" + kategorieName + "\';");
      logging.logSQL("SELECT count(*) FROM atemschutzpass_einsatzart where name = \'" + kategorieName + "\';");
      return result.next()?result.getInt(1):0;
   }

   public String getName(int einsatzart) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM atemschutzpass_einsatzart where id = " + einsatzart + ";");
      logging.logSQL("SELECT name FROM atemschutzpass_einsatzart where id = " + einsatzart + ";");
      return result.next()?result.getString(1):null;
   }
}
