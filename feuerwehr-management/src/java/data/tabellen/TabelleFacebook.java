package data.tabellen;

import data.DatenbankZugriff;
import go.Facebook;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleFacebook {

   public void insert(Facebook facebook) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO facebook (`id`,`veranstaltungID`, `veranstaltungKategorie`, `postTyp`, `postText`, `fbMessageID`, `mandantID`) VALUES (\'" + facebook.getId() + "\', \'" + facebook.getVeranstaltungID() + "\', \'" + facebook.getVeranstaltungKategorie() + "\', \'" + facebook.getPostTyp() + "\', \'" + facebook.getPostText() + "\', \'" + facebook.getFbMessageID() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void delete(String fbMessageID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from facebook where fbMessageID = \'" + fbMessageID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM facebook;");
      logging.logSQL("SELECT max(id) FROM facebook;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public String getFbMessageID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT fbMessageID FROM facebook where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT fbMessageID FROM facebook where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }
}
