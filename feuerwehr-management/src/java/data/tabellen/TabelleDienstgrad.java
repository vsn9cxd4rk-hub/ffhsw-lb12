package data.tabellen;

import data.DatenbankZugriff;
import go.Dienstgrad;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleDienstgrad {

   public void insert(Dienstgrad dienstgrad) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO dienstgrad (`id`, `beschreibung`, `beschreibungLang`, `mandantID`) VALUES (\'" + dienstgrad.getId() + "\', \'" + dienstgrad.getBeschreibung() + "\', \'" + dienstgrad.getBeschreibungLang() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Dienstgrad dienstgrad) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update dienstgrad set beschreibung = \'" + dienstgrad.getBeschreibung() + "\',   beschreibungLang = \'" + dienstgrad.getBeschreibungLang() + "\' where id = " + dienstgrad.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from dienstgrad where beschreibungLang = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM dienstgrad;");
      logging.logSQL("SELECT max(id) FROM dienstgrad;");
      return result.next()?(result.getInt(1) <= 20?51:result.getInt(1) + 1):0;
   }

   public int getCountBeschreibung(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM dienstgrad where beschreibung = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM dienstgrad where beschreibung = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountBeschreibungLang(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM dienstgrad where beschreibungLang = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM dienstgrad where beschreibungLang = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getDienstgradID(String dienstgradLang) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM dienstgrad where beschreibungLang = \'" + dienstgradLang + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM dienstgrad where beschreibungLang = \'" + dienstgradLang + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getDienstgradBeschreibungLang(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT beschreibungLang FROM dienstgrad where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT beschreibungLang FROM dienstgrad where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getDienstgradBeschreibungKurz(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT beschreibung FROM dienstgrad where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT beschreibung FROM dienstgrad where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAllDienstgradKurz() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT beschreibung FROM dienstgrad  where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT beschreibung FROM dienstgrad where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllDienstgradLang() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT beschreibungLang FROM dienstgrad where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT beschreibungLang FROM dienstgrad where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllDienstgradIds() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM dienstgrad where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT id FROM dienstgrad where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getNextDienstgradID(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM `dienstgrad` WHERE id > " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT * FROM `dienstgrad` WHERE id > " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      if(result.next()) {
         logging.logSQL("Ergebnis: " + result.getInt(1));
         return result.getInt(1);
      } else {
         return 0;
      }
   }
}
