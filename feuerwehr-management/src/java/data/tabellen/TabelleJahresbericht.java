package data.tabellen;

import data.DatenbankZugriff;
import go.Jahresbericht;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleJahresbericht {

   public void insert(Jahresbericht bericht) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO jahresberichte (`id`,`jahr`, `title`, `bericht`, `erstelldatum`, `autoBericht`, `dateiname`, `statistiken`, `mitgliederGruppe`, `mandantID`) VALUES (\'" + bericht.getId() + "\', \'" + bericht.getJahr() + "\', \'" + bericht.getTitle() + "\', \'" + bericht.getBericht() + "\', \'" + bericht.getErstelldatum() + "\', \'" + bericht.getAutoBericht() + "\', \'" + bericht.getDateiname() + "\', \'" + bericht.getStatistiken() + "\', \'" + bericht.getMitgliederGruppe() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM jahresberichte;");
      logging.logSQL("SELECT max(id) FROM jahresberichte;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public String getBerichtDateiname(String title) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT dateiname FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT dateiname FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAllVerfügbarenBerichte(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT title FROM jahresberichte where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      logging.logSQL("SELECT title FROM jahresberichte where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllTitle(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT title FROM jahresberichte where mitgliederGruppe =  " + mitgliederGruppe + " and autoBericht = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      logging.logSQL("SELECT title FROM jahresberichte where mitgliederGruppe =  " + mitgliederGruppe + " and autoBericht = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public String getBericht(String title) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT bericht FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      logging.logSQL("SELECT bericht FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      return result.next()?result.getString(1):null;
   }

   public int getBerichtCount(String title) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT bericht FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      logging.logSQL("SELECT bericht FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      return result.next()?result.getInt(1):0;
   }

   public String getJahrOfBericht(String title) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT jahr FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      logging.logSQL("SELECT jahr FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      return result.next()?result.getString(1):null;
   }

   public int[] getSelectedStatistiken(String title) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT statistiken FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      logging.logSQL("SELECT statistiken FROM jahresberichte where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title;");
      int[] intListe = null;

      while(result.next()) {
         if(result.getString(1).equals("leer")) {
            logging.logSQL("StatistikenListe ist leer!");
            return intListe;
         }

         try {
            String[] liste = result.getString(1).split(",");
            intListe = new int[liste.length];

            for(int i = 0; i < liste.length; ++i) {
               intListe[i] = Integer.parseInt(liste[i]);
               System.out.println(intListe[i] + " = " + Integer.parseInt(liste[i]));
            }
         } catch (NumberFormatException var7) {
            ;
         }
      }

      return intListe;
   }
}
