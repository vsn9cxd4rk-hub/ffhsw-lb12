package data.tabellen.statistik;

import data.DatenbankZugriff;
import go.StatistikEinsatz;
import go.Stichwort;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleStatistikEinsatz {

   public void insert(StatistikEinsatz statistik) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO statistikeinsatz (`id`, `veranstaltungID`, `einsatzID`, `jahr`, `stichwort`, `kategorie`, `ausrueckezeit`, `dauer`, `dauerAlarmfahrt`, `mannstunden`, `wochentag`, `mandantID`) VALUES (\'" + statistik.getId() + "\', \'" + statistik.getVeranstaltungID() + "\', \'" + statistik.getEinsatzID() + "\', \'" + statistik.getJahr() + "\', \'" + statistik.getStichwort() + "\', \'" + statistik.getKategorie() + "\', \'" + statistik.getAusrueckezeit() + "\', \'" + statistik.getDauer() + "\', \'" + statistik.getDauerAlarmfahrt() + "\', \'" + statistik.getMannstunden() + "\', \'" + statistik.getWochentag() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void update(StatistikEinsatz statistik) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update statistikeinsatz set stichwort = \'" + statistik.getStichwort() + "\', jahr = \'" + statistik.getJahr() + "\', kategorie = \'" + statistik.getKategorie() + "\', ausrueckezeit = \'" + statistik.getAusrueckezeit() + "\', dauer = \'" + statistik.getDauer() + "\', dauerAlarmfahrt = \'" + statistik.getDauerAlarmfahrt() + "\', mannstunden = \'" + statistik.getMannstunden() + "\', wochentag = \'" + statistik.getWochentag() + "\' where veranstaltungID = " + statistik.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateKategorie(Stichwort stichwort) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update statistikeinsatz set kategorie = " + stichwort.getKategorie() + " where stichwort = " + stichwort.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateEinsatzNummer(int einsatzNummerNeu, int einsatzNummerAlt, String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update statistikeinsatz set einsatzID = " + einsatzNummerNeu + " where einsatzID = " + einsatzNummerAlt + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from statistikeinsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM statistikeinsatz;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM statistikeinsatz;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getKategorieCount(int kategorie, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz where kategorie = " + kategorie + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where kategorie = " + kategorie + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getTagEinsätze(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `einsatz` e LEFT JOIN statistikeinsatz s ON s.veranstaltungID = e.veranstaltungID where e.ZeitAlarm between \'07:00\' and \'22:00\' and s.jahr = " + jahr + " and e.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `einsatz` e LEFT JOIN statistikeinsatz s ON s.veranstaltungID = e.veranstaltungID where e.ZeitAlarm between \'07:00\' and \'22:00\' and s.jahr = " + jahr + " and e.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      return result.next()?result.getInt(1):0;
   }

   public double getZusammengerechneteAusrueckezeit(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(ausrueckezeit) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(ausrueckezeit) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getDouble(1):0.0D;
   }

   public double getZusammengerechneteAlarmfahrtZeiten(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(dauerAlarmfahrt) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(dauerAlarmfahrt) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getDouble(1):0.0D;
   }

   public double getZusammengerechneteEinsatzdauer(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(dauer) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(dauer) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getDouble(1) / 60.0D:0.0D;
   }

   public int getZusammengerechneteMannstunden(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(mannstunden) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(mannstunden) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) / 60:0;
   }

   public int getZusammengerechneteMannstunden(int jahr, String monat) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(se.mannstunden) FROM statistikeinsatz se LEFT JOIN veranstaltung v ON v.id = se.veranstaltungID where se.jahr = " + jahr + " and v.datum between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\' and se.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(se.mannstunden) FROM statistikeinsatz se LEFT JOIN veranstaltung v ON v.id = se.veranstaltungID where se.jahr = " + jahr + " and v.datum between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\' and se.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) / 60:0;
   }

   public int getAnzahlProJahr(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAnzahlStichwortProJahr(int jahr, int stichwortID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and stichwort = " + stichwortID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and stichwort = " + stichwortID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAnzahlProJahrOnlyForAlarmfahrtdauer(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and dauerAlarmfahrt != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and dauerAlarmfahrt != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAnzahlProJahrOnlyForAusrueckzeit(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and ausrueckezeit != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where jahr = " + jahr + " and ausrueckezeit != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllJahreInDB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT jahr FROM `statistikeinsatz` where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by jahr;");
      logging.logSQL("SELECT jahr FROM `statistikeinsatz` where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by jahr;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getDauer(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT dauer FROM statistikeinsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT dauer FROM statistikeinsatz where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfDauerKleinerWert(int wert, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz where dauer <= " + wert + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where dauer <= " + wert + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfDauerGrößerWert(int wert, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz where dauer >= " + wert + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz where dauer >= " + wert + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getEinsatzProWochentag(int jahr, int wochentag) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikeinsatz WHERE jahr = " + jahr + " and wochentag = " + wochentag + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikeinsatz WHERE jahr = " + jahr + " and wochentag = " + wochentag + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public void updateMannstunden(StatistikEinsatz statistik) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update statistikeinsatz set mannstunden = " + statistik.getMannstunden() + " where veranstaltungID = " + statistik.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getAlarmfahrten(String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT dauerAlarmfahrt FROM `statistikeinsatz` where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;");
      logging.logSQL("SELECT dauerAlarmfahrt FROM `statistikeinsatz` where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAusrueckezeiten(String jahre) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ausrueckezeit FROM `statistikeinsatz` where jahr = " + jahre + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;");
      logging.logSQL("SELECT ausrueckezeit FROM `statistikeinsatz` where jahr = " + jahre + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by einsatzID;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public StatistikEinsatz getData(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM statistikeinsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM statistikeinsatz WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      StatistikEinsatz statistik = new StatistikEinsatz();

      while(result.next()) {
         statistik.setId(result.getInt("id"));
         statistik.setVeranstaltungID(result.getInt("veranstaltungID"));
         statistik.setEinsatzID(result.getInt("einsatzID"));
         statistik.setJahr(result.getInt("jahr"));
         statistik.setStichwort(result.getInt("stichwort"));
         statistik.setKategorie(result.getInt("kategorie"));
         statistik.setAusrueckezeit(result.getInt("ausrueckezeit"));
         statistik.setDauer(result.getInt("dauer"));
         statistik.setDauerAlarmfahrt(result.getInt("dauerAlarmfahrt"));
         statistik.setMannstunden(result.getInt("mannstunden"));
         statistik.setWochentag(result.getInt("wochentag"));
      }

      return statistik;
   }
}
