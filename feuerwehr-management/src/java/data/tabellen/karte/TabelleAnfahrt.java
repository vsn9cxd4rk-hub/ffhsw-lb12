package data.tabellen.karte;

import data.DatenbankKartenZugriff;
import data.DatenbankZugriff;
import data.tabellen.karte.DBConnectionServiceKarte;
import go.karte.Anfahrt;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleAnfahrt {

   public void insert(Anfahrt anfahrt) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "INSERT INTO karte_anfahrt (`id`, `strassenID`, `objektID`, `anfahrt`, `mandantID`) VALUES (\'" + anfahrt.getId() + "\', \'" + anfahrt.getStrassenID() + "\', \'" + anfahrt.getObjektID() + "\', \'" + anfahrt.getAnfahrt() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteObjektAnfahrt(Anfahrt anfahrt) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_anfahrt where objektID = " + anfahrt.getObjektID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID");
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteStraßenAnfahrt(Anfahrt anfahrt) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql2 = "delete from karte_anfahrt where strassenID = " + anfahrt.getStrassenID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID");
      logging.logSQL(sql2);
      statement.executeUpdate(sql2);
   }

   public void deleteLokal() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from `karte_anfahrt`";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM karte_anfahrt;");
      logging.logSQL("SELECT max(id) FROM karte_anfahrt;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public String getAnfahrtStraße(int straßenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT anfahrt FROM karte_anfahrt where strassenID = " + straßenID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT anfahrt FROM karte_anfahrt where strassenID = " + straßenID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):"";
   }

   public String getAnfahrtObjekt(int objektID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      ResultSet result = statement.executeQuery("SELECT anfahrt FROM karte_anfahrt where objektID = " + objektID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT anfahrt FROM karte_anfahrt where objektID = " + objektID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):"";
   }

   public String getDataForAnfahrtLokalBackup() throws SQLException, NullPointerException {
      Statement statement = DatenbankKartenZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("select * from karte_anfahrt where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("select * from karte_anfahrt where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      StringBuilder insert = new StringBuilder();
      insert.append("Insert INTO karte_anfahrt (`id`, `strassenID`, `objektID`, `anfahrt`, `mandantID`) VALUES ");

      for(int counter = 0; result.next(); ++counter) {
         if(counter != 0) {
            insert.append(",");
         }

         insert.append("(\'");
         insert.append(result.getString("id"));
         insert.append("\', \'");
         insert.append(result.getString("strassenID"));
         insert.append("\', \'");
         insert.append(result.getString("objektID"));
         insert.append("\', \'");
         insert.append(result.getString("anfahrt"));
         insert.append("\', \'");
         insert.append(result.getString("mandantID"));
         insert.append("\')");
      }

      logging.logSQL(insert.toString());
      return insert.toString();
   }
}
