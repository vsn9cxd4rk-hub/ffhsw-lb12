package data.tabellen.email;

import data.DatenbankZugriff;
import go.email.Empfangen;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleEMail_empfangen {

   public void insert(Empfangen emp) throws SQLException {
      String sql = "INSERT INTO email_empfangende (`id`, `sender`, `betreff`, `nachricht`, `date`, `size` , `anhang`, `gelesen`, `art`, `mandantID`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
      PreparedStatement pstmtQuery = DatenbankZugriff.getInstance().getDbConnection().prepareStatement(sql);
      pstmtQuery.setInt(1, emp.getId());
      pstmtQuery.setString(2, emp.getSender());
      pstmtQuery.setString(3, emp.getBetreff());
      pstmtQuery.setObject(4, emp.getNachricht());
      pstmtQuery.setString(5, emp.getDate());
      pstmtQuery.setInt(6, emp.getSize());
      pstmtQuery.setInt(7, emp.getAnhang());
      pstmtQuery.setInt(8, emp.getGelesen());
      pstmtQuery.setString(9, emp.getArt());
      pstmtQuery.setString(10, (String)runApplication.PROPERTIES.get("MandantID"));
      logging.logSQL(sql);
      pstmtQuery.executeUpdate();
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM email_empfangende where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(id) FROM email_empfangende where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public String getNachricht(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT nachricht FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT nachricht FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getBetreff(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT betreff FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT betreff FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getSender(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT sender FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT sender FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getDate(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT date FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT date FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public void deleteNachricht(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from email_empfangende where id = " + id + ";";
      statement.executeUpdate(sql);
   }

   public ArrayList getAllMails(int gelesen_status) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("Select * from email_empfangende where gelesen = " + gelesen_status + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id desc");
      logging.logSQL("Select * from email_empfangende where gelesen = " + gelesen_status + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id desc");

      ArrayList liste;
      for(liste = new ArrayList(); result.next(); liste.add("-----------------------------")) {
         liste.add("Nachricht: " + result.getString(1));
         liste.add(result.getString(2));
         liste.add("Betreff: " + result.getString(3));
         if(result.getInt(7) == 1) {
            liste.add(" --> Anhang <-- ");
         }
      }

      return liste;
   }

   public void updateReadStatus(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update email_empfangende set gelesen = 1 where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public String getArt(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT art FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT art FROM email_empfangende where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }
}
