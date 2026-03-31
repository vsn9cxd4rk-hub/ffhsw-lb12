package data.tabellen.abrechnung;

import data.DatenbankZugriff;
import go.abrechnung.AbrechnungKonto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleAbrechnung_konto {

   public void insert(AbrechnungKonto konto) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO abrechnung_konto (`id`, `name` , `mandantID`) VALUES (\'" + konto.getId() + "\', \'" + konto.getName() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM abrechnung_konto where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(id) FROM abrechnung_konto where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?(result.getInt(1) <= 10?11:result.getInt(1) + 1):0;
   }

   public int getID(String kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM abrechnung_konto where name = \'" + kategorie + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM abrechnung_konto where name = \'" + kategorie + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_konto where id = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM abrechnung_konto where id = \'" + id + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAllKontos() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_konto where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT name FROM abrechnung_konto where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getCount(String kategorieName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung_konto where name = \'" + kategorieName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM abrechnung_konto where name = \'" + kategorieName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
