package data.tabellen;

import data.DatenbankZugriff;
import go.Abwesenheitsgrund;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import logging.logging;
import run.runApplication;

public class TabelleAbwesenheitsgrund {

   public void insert(Abwesenheitsgrund grund) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO abwesenheitsgrund (`id`, `name`, `kurzName`, `mandantID`) VALUES (\'" + grund.getId() + "\', \'" + grund.getName() + "\', \'" + grund.getNameKurz() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM abwesenheitsgrund;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM abwesenheitsgrund;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public ArrayList getAlleGruende() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name from abwesenheitsgrund where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id");
      ResultSet result = statement.executeQuery("SELECT name from abwesenheitsgrund where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAlleGruendID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id from abwesenheitsgrund where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by id");
      ResultSet result = statement.executeQuery("SELECT id from abwesenheitsgrund where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by id");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getAbwesenheitsGrundID(String grund) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM abwesenheitsgrund where name = \'" + grund + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM abwesenheitsgrund where name = \'" + grund + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public HashMap getAbwesenheitsGrundMap() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name, id FROM abwesenheitsgrund where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT name, id FROM abwesenheitsgrund where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put(result.getString(1), Integer.valueOf(result.getInt(2)));
      }

      return map;
   }

   public String getAbwesenheitsGrundbyID(int grund) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM abwesenheitsgrund where id = \'" + grund + "\';");
      ResultSet result = statement.executeQuery("SELECT name FROM abwesenheitsgrund where id = \'" + grund + "\';");
      return result.next()?result.getString(1):null;
   }
}
