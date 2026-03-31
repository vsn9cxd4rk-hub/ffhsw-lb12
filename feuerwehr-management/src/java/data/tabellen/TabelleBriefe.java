package data.tabellen;

import data.DatenbankZugriff;
import go.Briefe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBriefe {

   public void insert(Briefe brief) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO briefe (`id`,`jahr`, `title`, `bericht`, `erstelldatum`, `dateiname`, `empfaenger`, `template`, `mitgliederGruppe`, `mandantID`) VALUES (\'" + brief.getId() + "\', \'" + brief.getJahr() + "\', \'" + brief.getTitle() + "\', \'" + brief.getBericht() + "\', \'" + brief.getErstelldatum() + "\', \'" + brief.getDateiname() + "\', \'" + brief.getEmpfaenger() + "\', \'" + brief.getTemplate() + "\', \'" + brief.getMitgliederGruppe() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void updateTemplate(String templateName) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update briefe set template = 0 where title = \'" + templateName + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM briefe;");
      logging.logSQL("SELECT max(id) FROM briefe;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public String getBrief(String title) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT dateiname FROM briefe where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT dateiname FROM briefe where title = \'" + title + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getText(String title) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT bericht FROM briefe where title = \'" + title + "\' and template = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT bericht FROM briefe where title = \'" + title + "\' and template = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getTemplates(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT title FROM briefe where mitgliederGruppe = " + mitgliederGruppe + " and  template = 1  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title, jahr;");
      logging.logSQL("SELECT title FROM briefe where mitgliederGruppe = " + mitgliederGruppe + " and  template = 1  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by title, jahr;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
