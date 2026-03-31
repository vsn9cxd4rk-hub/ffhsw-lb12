package data.tabellen.statistik;

import data.DatenbankZugriff;
import go.StatistikMitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleStatistikMitglieder {

   public void insert(StatistikMitglieder statistik) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO statistikmitglieder (`id`, `jahr`, `alterGes`, `anzahlGebTage`, `anzahl`, `erstellung`, `mitgliederGruppe`, `mandantID`) VALUES (\'" + statistik.getId() + "\', \'" + statistik.getJahr() + "\', \'" + statistik.getAlter() + "\', \'" + statistik.getAnzahlGebTage() + "\', \'" + statistik.getAnzahl() + "\', \'" + statistik.getErstellung() + "\', \'" + statistik.getMitgliederGruppe() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM statistikmitglieder;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM statistikmitglieder;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public ArrayList getAllJahreInDB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT jahr FROM statistikmitglieder where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ResultSet result = statement.executeQuery("SELECT jahr FROM statistikmitglieder where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlter(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT alterGes FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ResultSet result = statement.executeQuery("SELECT alterGes FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAnzahl(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT anzahl FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ResultSet result = statement.executeQuery("SELECT anzahl FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAnzahlGebTage(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT anzahlGebTage FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ResultSet result = statement.executeQuery("SELECT anzahlGebTage FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getErstellung(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT erstellung FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ResultSet result = statement.executeQuery("SELECT erstellung FROM statistikmitglieder where mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by erstellung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
