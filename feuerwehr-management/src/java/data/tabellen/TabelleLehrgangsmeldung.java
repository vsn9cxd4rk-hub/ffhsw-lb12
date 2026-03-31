package data.tabellen;

import data.DatenbankZugriff;
import go.Lehrgangsmeldung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleLehrgangsmeldung {

   public void insert(Lehrgangsmeldung meldung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO lehrgangsmeldung (`mitgliedID`, `lehrgang`, `art`, `mandantID`) VALUES (\'" + meldung.getId() + "\', \'" + meldung.getLehrgang() + "\', \'" + meldung.getArt() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void deleteAll() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from lehrgangsmeldung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getLehrgangsmeldungByLehrgang(String lehrgang) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN lehrgangsmeldung lm ON m.id = lm.mitgliedID where lm.lehrgang = \'" + lehrgang + "\' and lm.art = \'L\'  and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN lehrgangsmeldung lm ON m.id = lm.mitgliedID where lm.lehrgang = \'" + lehrgang + "\' and lm.art = \'L\' and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getBefoerderungen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = \'B\' and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = \'B\' and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2) + "  |  " + result.getString(3));
      }

      return liste;
   }

   public ArrayList getEhrungen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = \'EH\' and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, lm.lehrgang FROM lehrgangsmeldung lm LEFT JOIN mitglieder m ON lm.mitgliedID = m.id WHERE art = \'EH\' and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname, lm.lehrgang;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2) + "  |  " + result.getString(3));
      }

      return liste;
   }

   public int getCount(String art) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM lehrgangsmeldung where art = \'" + art + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM lehrgangsmeldung where art = \'" + art + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }
}
