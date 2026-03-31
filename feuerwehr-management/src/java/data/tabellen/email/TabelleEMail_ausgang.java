package data.tabellen.email;

import data.DatenbankZugriff;
import go.email.Ausgang;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleEMail_ausgang {

   public void insert(Ausgang send) throws SQLException {
      String sql = "INSERT INTO email_ausgang (`id`, `an`, `cc`, `bcc`, `betreff`, `nachricht` , `anhang`, `date`, `mandantID`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
      logging.logSQL(sql);
      PreparedStatement pstmtQuery = DatenbankZugriff.getInstance().getDbConnection().prepareStatement(sql);
      pstmtQuery.setInt(1, this.getNextNummer());
      pstmtQuery.setString(2, send.getAn());
      pstmtQuery.setString(3, send.getCc());
      pstmtQuery.setObject(4, send.getBcc());
      pstmtQuery.setString(5, send.getBetreff());
      pstmtQuery.setString(6, send.getNachricht());
      pstmtQuery.setString(7, send.getAnhang());
      pstmtQuery.setString(8, send.getDate());
      pstmtQuery.setString(9, (String)runApplication.PROPERTIES.get("MandantID"));
      logging.logSQL(sql);
      pstmtQuery.executeUpdate();
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(id) FROM email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getPostausgangNachrichten() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAllSendMails() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("Select * from email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id desc");
      logging.logSQL("Select * from email_ausgang where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id desc");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add("Nachricht: " + result.getString(1));
         liste.add("An: " + result.getString(2));
         liste.add("Betreff: " + result.getString(5));
         liste.add("-----------------------------");
      }

      return liste;
   }

   public String getEmpfaenger(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT an FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT an FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getCC(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT cc FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT cc FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getBCC(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT bcc FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT bcc FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getNachricht(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT nachricht FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT nachricht FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getBetreff(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT betreff FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT betreff FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAnhang(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT anhang FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT anhang FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getDate(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT date FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT date FROM email_ausgang where id = " + id + "  and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public void deleteNachricht(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from email_ausgang where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }
}
