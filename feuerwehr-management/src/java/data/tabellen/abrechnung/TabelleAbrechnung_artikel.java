package data.tabellen.abrechnung;

import data.DatenbankZugriff;
import go.abrechnung.ArtikelAbrechnung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;

public class TabelleAbrechnung_artikel {

   public void insert(ArtikelAbrechnung artikel) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO abrechnung_artikel (`id`,`name`, `klasse`,`buchungskonto`,`zahlungsart`, `wert`,`rabattwert`,`mwst`, `berechnungsart`,`berechnungsart2`,`rabattart`, `aktiv`, `von`, `bis` , `mandantID`) VALUES (\'" + artikel.getId() + "\', \'" + artikel.getName() + "\', \'" + artikel.getKlasse() + "\', \'" + artikel.getBuchungskonto() + "\', \'" + artikel.getZahlungsart() + "\', \'" + artikel.getWert() + "\', \'" + artikel.getRabattwert() + "\', \'" + artikel.getMwst() + "\', \'" + artikel.getBerechnungsart() + "\', \'" + artikel.getBerechnungsart2() + "\', \'" + artikel.getRabattart() + "\', \'" + artikel.getAktiv() + "\', \'" + artikel.getVon() + "\', \'" + artikel.getBis() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(ArtikelAbrechnung artikel) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update abrechnung_artikel set name = \'" + artikel.getName() + "\', klasse = \'" + artikel.getKlasse() + "\', buchungskonto = \'" + artikel.getBuchungskonto() + "\', zahlungsart = \'" + artikel.getZahlungsart() + "\', wert = \'" + artikel.getWert() + "\', rabattwert = \'" + artikel.getRabattwert() + "\', mwst = \'" + artikel.getMwst() + "\', berechnungsart = \'" + artikel.getBerechnungsart() + "\', berechnungsart2 = \'" + artikel.getBerechnungsart2() + "\', rabattart = \'" + artikel.getRabattart() + "\', aktiv = \'" + artikel.getAktiv() + "\', von = \'" + artikel.getVon() + "\', bis = \'" + artikel.getBis() + "\' where id = " + artikel.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM abrechnung_artikel where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(id) FROM abrechnung_artikel where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?(result.getInt(1) <= 3999?result.getInt(1) + 4000:result.getInt(1) + 1):0;
   }

   public int getArtikelIDByKlasse(int klasse) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String datum = SbcUtils.timeStamp("yyyy-MM-dd");
      ResultSet result = statement.executeQuery("SELECT id FROM abrechnung_artikel where klasse = " + klasse + " and aktiv = 1 and von <= \'" + datum + "\' and bis >= \'" + datum + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM abrechnung_artikel where klasse = " + klasse + " and aktiv = 1 and von <= \'" + datum + "\' and bis >= \'" + datum + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllArtikel() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      logging.logSQL("SELECT name FROM abrechnung_artikel where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllValidArtikel() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String datum = SbcUtils.timeStamp("yyyy-MM-dd");
      ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where von <= \'" + datum + "\' and bis >= \'" + datum + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      logging.logSQL("SELECT name FROM abrechnung_artikel where von <= \'" + datum + "\' and bis >= \'" + datum + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllArtikelGroeßer100() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String datum = SbcUtils.timeStamp("yyyy-MM-dd");
      ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where klasse > 100 and aktiv = 1  and von <= \'" + datum + "\' and bis >= \'" + datum + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      logging.logSQL("SELECT name FROM abrechnung_artikel where klasse > 100 and aktiv = 1  and von <= \'" + datum + "\' and bis >= \'" + datum + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getArtikelID(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM abrechnung_artikel where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM abrechnung_artikel where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getZahlungsart(int artikelID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT zahlungsart FROM abrechnung_artikel where id = \'" + artikelID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT zahlungsart FROM abrechnung_artikel where id = \'" + artikelID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getBuchungskontoName(int artID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT k.name FROM abrechnung_konto k LEFT JOIN abrechnung_artikel a ON a.buchungskonto = k.id where a.id = \'" + artID + "\' and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT k.name FROM abrechnung_konto k LEFT JOIN abrechnung_artikel a ON a.buchungskonto = k.id where a.id = \'" + artID + "\' and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getBuchungskontoID(int artID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT buchungskonto FROM abrechnung_artikel where id = \'" + artID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT buchungskonto FROM abrechnung_artikel where id = \'" + artID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelCount(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM abrechnung_artikel where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung_artikel where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelCountByName(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM abrechnung_artikel where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung_artikel where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getArtikelName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getGueltigVon(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT von FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT von FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getGueltigBis(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT bis FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT bis FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getKlasseCount(int klassenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung_artikel where klasse = " + klassenID + " and klasse > 100 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM abrechnung_artikel where klasse = " + klassenID + " and klasse > 100 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelKlasse(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT klasse FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT klasse FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelWert(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT wert FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT wert FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelRabattWert(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT rabattwert FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT rabattwert FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelBerechnungsart(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT berechnungsart FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT berechnungsart FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelBerechnungsart2(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT berechnungsart2 FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT berechnungsart2 FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getArtikelrabattArt(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT rabattart FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT rabattart FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAktiv(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT aktiv FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT akti FROM abrechnung_artikel where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
