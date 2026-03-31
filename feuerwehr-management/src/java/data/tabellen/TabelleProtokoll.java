package data.tabellen;

import data.DatenbankZugriff;
import go.Protokoll;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleProtokoll {

   public void insert(Protokoll protokoll) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO protokoll (`id`,`veranstaltungID`, `jahr`, `title`, `protokolltext`, `erstelldatum`, `mitgliederGruppe`, `mandantID`) VALUES (\'" + protokoll.getId() + "\', \'" + protokoll.getVeranstaltungID() + "\', \'" + protokoll.getJahr() + "\', \'" + protokoll.getTitle() + "\', \'" + protokoll.getProtokolltext() + "\', \'" + protokoll.getErstelldatum() + "\', \'" + protokoll.getMitgliederGruppe() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Protokoll protokoll) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update protokoll set protokolltext = \'" + protokoll.getProtokolltext() + "\',  erstelldatum = \'" + protokoll.getErstelldatum() + "\',  title = \'" + protokoll.getTitle() + "\' where veranstaltungID = " + protokoll.getVeranstaltungID() + " and mitgliederGruppe = " + protokoll.getMitgliederGruppe() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM protokoll;");
      logging.logSQL("SELECT max(id) FROM protokoll;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCount(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getProtokoll(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT protokolltext FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT protokolltext FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):"";
   }

   public Protokoll getData(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT * FROM protokoll where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT * FROM protokoll where veranstaltungID = " + veranstaltungID + " and  mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      if(result.next()) {
         Protokoll protokoll = new Protokoll();
         protokoll.setId(result.getInt(1));
         protokoll.setVeranstaltungID(result.getInt(2));
         protokoll.setJahr(result.getInt(3));
         protokoll.setTitle(result.getString(4));
         protokoll.setProtokolltext(result.getString(5));
         protokoll.setErstelldatum(result.getString(6));
         return protokoll;
      } else {
         return null;
      }
   }

   public ArrayList getAlleTitel(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT v.name, p.title FROM protokoll p LEFT JOIN veranstaltung v ON p.veranstaltungID = v.id where p.jahr = " + jahr + " and v.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT v.name, p.title FROM protokoll p LEFT JOIN veranstaltung v ON p.veranstaltungID = v.id where p.jahr = " + jahr + " and v.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + " - " + result.getString(2));
      }

      return liste;
   }
}
