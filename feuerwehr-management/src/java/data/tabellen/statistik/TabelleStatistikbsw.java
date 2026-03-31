package data.tabellen.statistik;

import data.DatenbankZugriff;
import go.StatistikBSW;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleStatistikbsw {

   public void insert(StatistikBSW statistik) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO statistikbsw (`id`, `veranstaltungID`, `bswID`, `jahr`,`dauer`, `mannstunden`, `wochentag`, `mandantID`) VALUES (\'" + statistik.getId() + "\', \'" + statistik.getVeranstaltungID() + "\', \'" + statistik.getBswID() + "\', \'" + statistik.getJahr() + "\', \'" + statistik.getDauer() + "\', \'" + statistik.getMannstunden() + "\', \'" + statistik.getWochentag() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(StatistikBSW statistik) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update statistikbsw set jahr = \'" + statistik.getJahr() + "\', dauer = \'" + statistik.getDauer() + "\', mannstunden = \'" + statistik.getMannstunden() + "\', wochentag = \'" + statistik.getWochentag() + "\' where veranstaltungID = " + statistik.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateBSWNummer(int bswNummerNeu, int bswNummerAlt, String jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update statistikbsw set bswID = " + bswNummerNeu + " where bswID = " + bswNummerAlt + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from statistikbsw where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM statistikbsw;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM statistikbsw;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public double getZusammengerechneteBSWdauer(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(dauer) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(dauer) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getDouble(1) / 60.0D:0.0D;
   }

   public int getZusammengerechneteBSWMannstunden(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(mannstunden) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(mannstunden) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) / 60:0;
   }

   public int getZusammengerechneteBSWMannstunden(int jahr, String monat) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(sb.mannstunden) FROM statistikbsw sb LEFT JOIN veranstaltung v ON v.id = sb.veranstaltungID where sb.jahr = " + jahr + " and v.datum between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\' and sb.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(sb.mannstunden) FROM statistikbsw sb LEFT JOIN veranstaltung v ON v.id = sb.veranstaltungID where sb.jahr = " + jahr + " and v.datum between \'" + jahr + "-" + monat + "-01\' and \'" + jahr + "-" + monat + "-31\' and sb.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) / 60:0;
   }

   public int getAnzahlBSWProJahr(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM statistikbsw where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllJahreInDB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT jahr FROM `statistikbsw` where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by jahr;");
      logging.logSQL("SELECT jahr FROM `statistikbsw` where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by jahr;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getDauer(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT dauer FROM statistikbsw where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT dauer FROM statistikbsw where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public void updateMannstunden(StatistikBSW statistik) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update statistikbsw set mannstunden = " + statistik.getMannstunden() + " where veranstaltungID = " + statistik.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }
}
