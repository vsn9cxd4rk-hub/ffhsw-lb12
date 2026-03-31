package data.tabellen.karte;

import data.DatenbankZugriff;
import go.karte.AAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleAAO {

   public void insert(AAO aao) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO aao (`id`,`stichwortID`, `strassenID`, `fahrzeugID`, `reihenfolge`, `mandantID`) VALUES (\'" + aao.getId() + "\', \'" + aao.getStichwortID() + "\', \'" + aao.getStrassenID() + "\', \'" + aao.getFahrzeugID() + "\', \'" + aao.getReihenfolge() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void deleteOne(int fahrzeugID, int stichwortID, int straßenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from aao where fahrzeugID = " + fahrzeugID + " and strassenID = " + straßenID + " and stichwortID = " + stichwortID + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM aao;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM aao;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getNextRheienfolge(int stichwortID, int straßenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(reihenfolge) FROM aao where  stichwortID = " + stichwortID + " and strassenID = " + straßenID + ";");
      ResultSet result = statement.executeQuery("SELECT max(reihenfolge) FROM aao where  stichwortID = " + stichwortID + " and strassenID = " + straßenID + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public ArrayList getZugeordneteFahrzeuge(int stichwortID, int straßenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT f.name FROM aao a LEFT JOIN fahrzeuge f ON a.fahrzeugID = f.id where a.stichwortID = " + stichwortID + " and a.strassenID = " + straßenID + " order by reihenfolge;");
      ResultSet result = statement.executeQuery("SELECT f.name FROM aao a LEFT JOIN fahrzeuge f ON a.fahrzeugID = f.id where a.stichwortID = " + stichwortID + " and a.strassenID = " + straßenID + " order by reihenfolge;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      logging.logSQL(liste);
      return liste;
   }
}
