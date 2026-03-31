package data.tabellen.schicht;

import data.DatenbankZugriff;
import go.schicht.Schicht;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleSchicht {

   public void insert(Schicht schicht) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schicht (`id`, `jahr`, `name`, `datumVon`, `uhrVon`, `datumBis`, `uhrBis`, `von`, `bis`, `mandantID`) VALUES (\'" + schicht.getId() + "\', \'" + schicht.getJahr() + "\', \'" + schicht.getName() + "\', \'" + schicht.getSchichtStartDatum() + "\', \'" + schicht.getSchichtStartUhrzeit() + "\', \'" + schicht.getSchichtEndeDatum() + "\', \'" + schicht.getSchichtEndeUhrzeit() + "\', \'" + schicht.getMinutenVon() + "\', \'" + schicht.getMinutenBis() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM schicht;");
      logging.logSQL("SELECT max(id) FROM schicht;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getSchichtID(String schichtName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM schicht where name = \'" + schichtName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM schicht where name = \'" + schichtName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllSchichten() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM schicht where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;");
      logging.logSQL("SELECT name FROM schicht where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllSchichtenEinesMonats(String monat, String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM schicht where datumVon between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;");
      logging.logSQL("SELECT name FROM schicht where datumVon between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datumVon, uhrVon;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getSchichtIDFürEreignis(String datum, int zeitInMinuten) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM schicht where datumVon = \'" + datum + "\' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM schicht where datumVon = \'" + datum + "\' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getSchichtIDFürEreignis2(String datum, int zeitInMinuten) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM schicht where datumBis = \'" + datum + "\' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM schicht where datumBis = \'" + datum + "\' and von < " + zeitInMinuten + " and bis > " + zeitInMinuten + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
