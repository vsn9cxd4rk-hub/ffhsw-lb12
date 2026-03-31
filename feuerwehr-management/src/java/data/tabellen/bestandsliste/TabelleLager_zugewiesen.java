package data.tabellen.bestandsliste;

import data.DatenbankZugriff;
import go.bestandsliste.Zuweisen;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleLager_zugewiesen {

   public void insert(Zuweisen zuweisen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO lager_zugewiesen (`id`, `artikelId`,`anzahl`,`gruppe`, `mitgliedID`, `ort`,`produktion`,`identifikation`,`inDienst`,`pruefung`, `mandantID`) VALUES (\'" + zuweisen.getId() + "\', \'" + zuweisen.getArtikelId() + "\', \'" + zuweisen.getAnzahl() + "\', \'" + zuweisen.getGruppe() + "\', \'" + zuweisen.getMitgliedID() + "\', \'" + zuweisen.getOrt() + "\', \'" + zuweisen.getProduktion() + "\', \'" + zuweisen.getIdentifikation() + "\', \'" + zuweisen.getInDienst() + "\', \'" + zuweisen.getPruefung() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void update(Zuweisen zuweisen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update lager_zugewiesen set artikelId = " + zuweisen.getArtikelId() + ", anzahl = " + this.getNextNummerOfArtikle(zuweisen.getArtikelId(), zuweisen.getMitgliedID(), zuweisen.getGruppe()) + ", gruppe = \'" + zuweisen.getGruppe() + "\', mitgliedID = " + zuweisen.getMitgliedID() + ", ort = \'" + zuweisen.getOrt() + "\' where id = " + zuweisen.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteVituellenLagerinhalt() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "DELETE FROM lager_zugewiesen where mitgliedID = 9000 and gruppe = \'L\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public void updateOrt(Zuweisen zuweisen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update lager_zugewiesen set ort = \'" + zuweisen.getOrt() + "\', produktion = \'" + zuweisen.getProduktion() + "\', identifikation = \'" + zuweisen.getIdentifikation() + "\', inDienst = \'" + zuweisen.getInDienst() + "\', pruefung = \'" + zuweisen.getPruefung() + "\' where anzahl = \'" + zuweisen.getAnzahl() + "\' and gruppe = \'" + zuweisen.getGruppe() + "\' and mitgliedID = \'" + zuweisen.getMitgliedID() + "\' and  artikelId = \'" + zuweisen.getArtikelId() + "\' and  mandantID = \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\';";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummerOfArtikle(int artikelID, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(anzahl) FROM lager_zugewiesen where artikelId = " + artikelID + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(anzahl) FROM lager_zugewiesen where artikelId = " + artikelID + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getNextID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM lager_zugewiesen;");
      logging.logSQL("SELECT max(id) FROM lager_zugewiesen;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCountOfArtikel(int artikelID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM lager_zugewiesen where artikelID = " + artikelID + ";");
      logging.logSQL("SELECT count(*) FROM lager_zugewiesen where artikelID = " + artikelID + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfArtikelInLager(int lagerID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM lager_zugewiesen where gruppe = \'L\' and mitgliedID = " + lagerID + ";");
      logging.logSQL("SELECT count(*) FROM lager_zugewiesen where gruppe = \'L\' and mitgliedID = " + lagerID + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllOrt(String gruppe, int benutzerID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ort FROM `lager_zugewiesen` where gruppe = \'" + gruppe + "\' and mitgliedId = " + benutzerID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by ort;");
      logging.logSQL("SELECT ort FROM `lager_zugewiesen` where gruppe = \'" + gruppe + "\' and mitgliedId = " + benutzerID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by ort;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getZugewiesendeArtikel(String gruppe, int benutzerID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT a.name, z.anzahl FROM lager_artikel a LEFT JOIN lager_zugewiesen z ON a.id = z.artikelID  where z.mitgliedID = " + benutzerID + " and z.gruppe = \'" + gruppe + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.id;");
      logging.logSQL("SELECT a.name, z.anzahl FROM lager_artikel a LEFT JOIN lager_zugewiesen z ON a.id = z.artikelID  where z.mitgliedID = " + benutzerID + " and z.gruppe = \'" + gruppe + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + " (" + result.getString(2) + ".)");
      }

      return liste;
   }

   public ArrayList getZugewiesendeArtikelForPDF(String gruppe, int benutzerID, String ort) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT a.name, z.anzahl FROM lager_artikel a LEFT JOIN lager_zugewiesen z ON a.id = z.artikelID  where z.mitgliedID = " + benutzerID + " and z.gruppe = \'" + gruppe + "\' and z.ort = \'" + ort + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by a.name, z.ort order by a.id;");
      logging.logSQL("SELECT a.name, z.anzahl FROM lager_artikel a LEFT JOIN lager_zugewiesen z ON a.id = z.artikelID  where z.mitgliedID = " + benutzerID + " and z.gruppe = \'" + gruppe + "\' and z.ort = \'" + ort + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by a.name, z.ort order by a.id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + " (" + result.getString(2) + "x)");
      }

      return liste;
   }

   public int getLastNumberOfArtikel(int artikelID, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(anzahl) FROM lager_zugewiesen where artikelId = " + artikelID + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(anzahl) FROM lager_zugewiesen where artikelId = " + artikelID + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getId(int artikelID, int anzahl, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM lager_zugewiesen where artikelId = " + artikelID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM lager_zugewiesen where artikelId = " + artikelID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getOrt(int artikleID, int anzahl, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT ort FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT ort FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getProduktion(int artikleID, int anzahl, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT produktion FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT produktion FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getIdentifikation(int artikleID, int anzahl, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT identifikation FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT identifikation FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getInDienst(int artikleID, int anzahl, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT inDienst FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT inDienst FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getPruefung(int artikleID, int anzahl, int mitgliedID, String gruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT pruefung FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT pruefung FROM lager_zugewiesen where artikelID = " + artikleID + " and anzahl = " + anzahl + " and mitgliedID = " + mitgliedID + " and gruppe = \'" + gruppe + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }
}
