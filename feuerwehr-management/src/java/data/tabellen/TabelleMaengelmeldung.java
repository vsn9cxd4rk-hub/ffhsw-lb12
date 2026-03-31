package data.tabellen;

import data.DatenbankZugriff;
import go.Mängelmeldung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleMaengelmeldung {

   public void insert(Mängelmeldung mangel) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO maengelmeldung (`id`, `jahr`, `mitgliedID`, `fahrzeugID`, `datum`, `wann`, `beschreibung`, `dateiname`, `status`, `mandantID`) VALUES (\'" + mangel.getId() + "\', \'" + mangel.getJahr() + "\', \'" + mangel.getMitgliedID() + "\', \'" + mangel.getFahrzeugID() + "\', \'" + mangel.getDatum() + "\', \'" + mangel.getWann() + "\', \'" + mangel.getBeschreibung() + "\', \'" + mangel.getDateiname() + "\', \'" + mangel.getStatus() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void updateStatus(int id, int status) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update maengelmeldung set status = " + status + " where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM maengelmeldung;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM maengelmeldung;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public ArrayList getWann(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT wann FROM maengelmeldung where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT wann FROM maengelmeldung where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getMaengelmeldungWithStatus(int status, int jahr, String mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM maengelmeldung where status = " + status + " and jahr = " + jahr + " and mandantID = " + mandantID + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM maengelmeldung where status = " + status + " and jahr = " + jahr + " and mandantID = " + mandantID + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add("Mangel-ID" + result.getString(1));
      }

      return liste;
   }

   public String getDateiname(String wann) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT dateiname FROM maengelmeldung where wann = \'" + wann + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT dateiname FROM maengelmeldung where wann = \'" + wann + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getDateinameByID(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT dateiname FROM maengelmeldung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT dateiname FROM maengelmeldung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getWannAvailable(String wann) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM maengelmeldung where wann = \'" + wann + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM maengelmeldung where wann = \'" + wann + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getMaengelmeldungForInformation(int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String partOfStatement;
      if(mandantID == 0) {
         partOfStatement = "";
      } else {
         partOfStatement = "and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID");
      }

      logging.logSQL("SELECT id, beschreibung FROM maengelmeldung where status = 0 " + partOfStatement + ";");
      ResultSet result = statement.executeQuery("SELECT id, beschreibung FROM maengelmeldung where status = 0 " + partOfStatement + ";");

      ArrayList liste;
      String beschreibung;
      for(liste = new ArrayList(); result.next(); liste.add("Mangel-ID" + result.getString(1) + " - " + beschreibung)) {
         if(result.getString(2).length() >= 23) {
            beschreibung = result.getString(2).substring(0, 23) + "...";
         } else {
            beschreibung = result.getString(2);
         }
      }

      return liste;
   }
}
