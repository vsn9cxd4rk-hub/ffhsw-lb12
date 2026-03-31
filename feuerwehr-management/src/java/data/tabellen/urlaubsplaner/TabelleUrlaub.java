package data.tabellen.urlaubsplaner;

import data.DatenbankZugriff;
import go.urlaub.Urlaub;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleUrlaub {

   public void insert(Urlaub urlaub) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO urlaub (`id`, `jahr`, `mitgliederID`, `mitgliederGruppe`, `datumVon`, `datumBis`, `loeschkenner`, `mandantID`) VALUES (\'" + urlaub.getId() + "\', \'" + urlaub.getJahr() + "\', \'" + urlaub.getMitgliederID() + "\', \'" + urlaub.getMitgliederGruppe() + "\', \'" + urlaub.getDatumVon() + "\', \'" + urlaub.getDatumBis() + "\', \'" + urlaub.getLoeschkenner() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void updateLoeschkenner(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update urlaub set loeschkenner = 1 where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM urlaub;");
      logging.logSQL("SELECT max(id) FROM urlaub;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public ArrayList getMitgliederMitUrlaubByDatum(String datum) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT mitgliederID FROM `urlaub` where datumVon = \'" + datum + "\' or datumBis = \'" + datum + "\' or datumVon <= \'" + datum + "\' and datumBis >= \'" + datum + "\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT mitgliederID FROM `urlaub` where datumVon = \'" + datum + "\' or datumBis = \'" + datum + "\' or datumVon <= \'" + datum + "\' and datumBis >= \'" + datum + "\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getUrlaubsliste(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname, u.datumVon, u.datumBis, u.jahr FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.mitgliederGruppe = " + mitgliederGruppe + " and u.datumBis >= \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and u.loeschkenner = 0 and u.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, u.datumVon, u.datumBis, u.jahr FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.mitgliederGruppe = " + mitgliederGruppe + " and u.datumBis >= \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and u.loeschkenner = 0 and u.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;");
      ArrayList liste = new ArrayList();
      int aktJahr = Integer.parseInt(SbcUtils.timeStamp("yyyy"));

      while(result.next()) {
         if(result.getInt(5) <= aktJahr) {
            liste.add(result.getString(1) + ", " + result.getString(2) + " (von: " + TimeCalculation.parseDateForGUI(result.getString(3)) + " bis: " + TimeCalculation.parseDateForGUI(result.getString(4)) + ")");
         }
      }

      return liste;
   }

   public ArrayList getIDListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM urlaub where datumBis > \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and loeschkenner= 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datumVon;");
      ResultSet result = statement.executeQuery("SELECT id FROM urlaub where datumBis > \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and loeschkenner= 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datumVon;");
      ArrayList liste = new ArrayList();
      liste.add(Integer.valueOf(0));

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public Vector getAllForList(String jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname, u.datumVon, u.datumBis FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.mitgliederGruppe = " + mitgliederGruppe + " and u.jahr = " + jahr + " and u.loeschkenner = 0 and u.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, u.datumVon, u.datumBis FROM urlaub u LEFT JOIN mitglieder m ON m.id = u.mitgliederID where u.mitgliederGruppe = " + mitgliederGruppe + " and u.jahr = " + jahr + " and u.loeschkenner = 0 and u.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by u.datumVon;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector liste = new Vector();
      liste.add(result.getString("name") + ", " + result.getString("vorname"));
      liste.add(TimeCalculation.parseDateForGUI(result.getString("datumVon")));
      liste.add(TimeCalculation.parseDateForGUI(result.getString("datumBis")));
      return liste;
   }
}
