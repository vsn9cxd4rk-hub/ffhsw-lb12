package data.tabellen;

import data.DatenbankZugriff;
import go.Beförderung;
import go.Beförderung_erforderlich;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBeförderungKonfig {

   public void insert(Beförderung beförderung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO beförderung_konfiguration (`id`,`dienstgradID`, `dienstgradVoraussetzung`, `zeit`, `dienstZeit`, `nurZeitBefoerderung`, `letzteStufe`, `auslassen`, `mandantID`) VALUES (\'" + beförderung.getId() + "\', \'" + beförderung.getDienstgradID() + "\', \'" + beförderung.getDienstgradVoraussetzung() + "\', \'" + beförderung.getZeit() + "\', \'" + beförderung.getDienstZeit() + "\', \'" + beförderung.getNurZeitBefoerderung() + "\', \'" + beförderung.getLetzteStufe() + "\', \'" + beförderung.getAuslassen() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Beförderung beförderung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update beförderung_konfiguration set dienstgradID = " + beförderung.getDienstgradID() + ", dienstgradVoraussetzung = " + beförderung.getDienstgradVoraussetzung() + ", zeit = " + beförderung.getZeit() + ", dienstZeit = " + beförderung.getDienstZeit() + ", nurZeitBefoerderung = " + beförderung.getNurZeitBefoerderung() + ", letzteStufe = " + beförderung.getLetzteStufe() + ", auslassen = " + beförderung.getAuslassen() + " where id = " + beförderung.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void insert(Beförderung_erforderlich beförderung_erforderlich) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO beförderung_erforderlich (`id`,`lehrgangID`,`mandantID`) VALUES (\'" + beförderung_erforderlich.getId() + "\', \'" + beförderung_erforderlich.getLehrgangID() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void delete(Beförderung_erforderlich beförderung_erforderlich) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from beförderung_erforderlich where lehrgangID = " + beförderung_erforderlich.getLehrgangID() + " and id = " + beförderung_erforderlich.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID");
      statement.executeUpdate(sql);
   }

   public void deleteAll(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from beförderung_erforderlich where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM beförderung_konfiguration;");
      logging.logSQL("SELECT max(id) FROM beförderung_konfiguration;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getID(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getDienstgradVorausseltzung(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT dienstgradVoraussetzung FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT dienstgradVoraussetzung FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getZeit(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT zeit FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT zeit FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getDienstZeit(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT dienstZeit FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT dienstZeit FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getNurZeitBefoerderung(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT nurZeitBefoerderung FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT nurZeitBefoerderung FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getLetzteStufe(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT letzteStufe FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT letzteStufe FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAuslassen(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT auslassen FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT auslassen FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountForDienstgrad(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM beförderung_konfiguration where dienstgradID = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCount() {
      try {
         Statement e = DatenbankZugriff.getInstance().getDbConnection().createStatement();
         ResultSet result = e.executeQuery("SELECT count(*) FROM beförderung_konfiguration where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
         logging.logSQL("SELECT count(*) FROM beförderung_konfiguration where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
         return result.next()?result.getInt(1):0;
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
         return 0;
      }
   }

   public ArrayList getAllRelevantenLerhänge(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT lk.name FROM lehrgang_kategorie lk LEFT JOIN beförderung_erforderlich bf ON lk.id = bf.lehrgangID where bf.id = " + dienstgradID + " and bf.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and lk.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT lk.name FROM lehrgang_kategorie lk LEFT JOIN beförderung_erforderlich bf ON lk.id = bf.lehrgangID where bf.id = " + dienstgradID + " and bf.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and lk.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllRelevantenLerhängeID(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT lehrgangID FROM beförderung_erforderlich WHERE  id = (Select id from beförderung_konfiguration where dienstgradID = " + dienstgradID + ") and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT lehrgangID FROM beförderung_erforderlich WHERE  id = (Select id from beförderung_konfiguration where dienstgradID = " + dienstgradID + ") and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getAlternativNextDienstgradID(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.id FROM dienstgrad d LEFT JOIN beförderung_konfiguration bk ON d.id = bk.dienstgradID WHERE d.id > " + dienstgradID + " and bk.auslassen != 1 and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by d.id;");
      ResultSet result = statement.executeQuery("SELECT d.id FROM dienstgrad d LEFT JOIN beförderung_konfiguration bk ON d.id = bk.dienstgradID WHERE d.id > " + dienstgradID + " and bk.auslassen != 1 and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by d.id;");
      if(result.next()) {
         logging.logSQL("Ergebnis: " + result.getInt(1));
         return result.getInt(1);
      } else {
         return 0;
      }
   }

   public int getAlternativNextDienstgradID_ZeitBefoerderung(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT d.id FROM dienstgrad d LEFT JOIN beförderung_konfiguration bk ON d.id = bk.dienstgradID  WHERE d.id > " + dienstgradID + " and bk.nurZeitBefoerderung != 1 and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "order by d.id;");
      logging.logSQL("SELECT d.id FROM dienstgrad d LEFT JOIN beförderung_konfiguration bk ON d.id = bk.dienstgradID  WHERE d.id > " + dienstgradID + " and bk.nurZeitBefoerderung != 1 and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "order by d.id;");
      if(result.next()) {
         logging.logSQL("Ergebnis: " + result.getInt(1));
         return result.getInt(1);
      } else {
         return 0;
      }
   }
}
