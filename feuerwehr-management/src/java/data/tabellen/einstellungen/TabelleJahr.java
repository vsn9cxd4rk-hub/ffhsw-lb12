package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleJahr {

   public void insert(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO jahr (`jahr` , `mandantID`) VALUES (" + jahr + ", " + (String)runApplication.PROPERTIES.get("MandantID") + ");";
      statement.executeUpdate(sql);
   }

   public int getJahr(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM jahr where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM jahr where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllVerfügbarenJahre() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM jahr where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by jahr;");
      logging.logSQL("SELECT * FROM jahr where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by jahr;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
