package data.tabellen;

import data.DatenbankZugriff;
import go.Brandsicherheitswachen_temp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBrandsicherheitswachen_temp {

   public void insert(Brandsicherheitswachen_temp temp) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO brandsicherheitswachen_temp (`mitgliederID`, `beteiligung`, `mandantID`) VALUES (\'" + temp.getMitgliederID() + "\', \'" + temp.getBeteiligung() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void deleteAll() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from brandsicherheitswachen_temp where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public ArrayList getListOfBeteiligung() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname, bt.beteiligung FROM brandsicherheitswachen_temp bt LEFT JOIN mitglieder m ON bt.mitgliederID = m.id where bt.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by bt.beteiligung asc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, bt.beteiligung FROM brandsicherheitswachen_temp bt LEFT JOIN mitglieder m ON bt.mitgliederID = m.id where bt.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by bt.beteiligung asc;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(result.getRow() <= 15) {
            liste.add(result.getRow() + ". Position:  " + result.getString(1) + ", " + result.getString(2) + " (" + result.getString(3) + "xMal)");
         }
      }

      return liste;
   }
}
