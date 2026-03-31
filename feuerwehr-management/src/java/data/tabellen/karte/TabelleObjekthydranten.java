package data.tabellen.karte;

import data.DatenbankKartenZugriff;
import data.DatenbankZugriff;
import data.tabellen.karte.DBConnectionServiceKarte;
import go.karte.Objekthydranten;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleObjekthydranten {

   public void insert(Objekthydranten hydrant) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "INSERT INTO karte_objekthydranten (`id`, `objektID`, `hydrantID`,  `entfernung`, `beschreibung`, `mandantID`) VALUES (\'" + hydrant.getId() + "\', \'" + hydrant.getObjektID() + "\', \'" + hydrant.getHydrantID() + "\', \'" + hydrant.getEntfernung() + "\', \'" + hydrant.getBeschreibung() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateObjekthdrantenDetails(Objekthydranten hydrant) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "Update karte_objekthydranten set entfernung = \'" + hydrant.getEntfernung() + "\', Beschreibung = \'" + hydrant.getBeschreibung() + "\' where id = " + hydrant.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(int id) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_objekthydranten where id = " + id + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteLokal() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from `karte_objekthydranten`";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public Integer getNextIndex() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT max(id) from karte_objekthydranten");
      ResultSet result = statement.executeQuery("SELECT max(id) from karte_objekthydranten");
      return result.next()?Integer.valueOf(result.getInt(1) + 1):null;
   }

   public String getEntfernung(int id) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT entfernung from karte_objekthydranten where id = " + id + ";");
      ResultSet result = statement.executeQuery("SELECT entfernung from karte_objekthydranten where id = " + id + ";");
      return result.next()?result.getString(1):null;
   }

   public String getBeschreibung(int id) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT beschreibung from karte_objekthydranten where id = " + id + ";");
      ResultSet result = statement.executeQuery("SELECT beschreibung from karte_objekthydranten where id = " + id + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getHydrantenIDs(int objektID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT id from karte_objekthydranten where objektID = " + objektID + " order by hydrantID;");
      ResultSet result = statement.executeQuery("SELECT id from karte_objekthydranten where objektID = " + objektID + " order by hydrantID;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public Vector getAllObjekthydrantenForTable(int objektID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT kh.id, ks.name, kh.hausnummer, kh.nennweite, kh.GPS_N, kh.GPS_O, koh.entfernung, koh.beschreibung FROM karte_objekthydranten koh LEFT JOIN karte_hydranten kh ON koh.hydrantID = kh.id LEFT JOIN karte_strassen ks ON kh.starssenid = ks.id where koh.objektID = " + objektID + " order by koh.hydrantID;");
      ResultSet result = statement.executeQuery("SELECT kh.id, ks.name, kh.hausnummer, kh.nennweite, kh.GPS_N, kh.GPS_O, koh.entfernung, koh.beschreibung FROM karte_objekthydranten koh LEFT JOIN karte_hydranten kh ON koh.hydrantID = kh.id LEFT JOIN karte_strassen ks ON kh.starssenid = ks.id where koh.objektID = " + objektID + " order by koh.hydrantID;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public String getDataForObjekteHydrantenLokalBackup() throws SQLException, NullPointerException {
      Statement statement = DatenbankKartenZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("select * from karte_objekthydranten;");
      ResultSet result = statement.executeQuery("select * from karte_objekthydranten;");
      StringBuilder insert = new StringBuilder();
      insert.append("Insert INTO karte_objekthydranten (`ID`, `objektID`, `hydrantID`, `entfernung`, `beschreibung`, `mandantID`) VALUES ");

      for(int counter = 0; result.next(); ++counter) {
         if(counter != 0) {
            insert.append(",");
         }

         insert.append("(\'");
         insert.append(result.getString("ID"));
         insert.append("\', \'");
         insert.append(result.getString("objektID"));
         insert.append("\', \'");
         insert.append(result.getString("hydrantID"));
         insert.append("\', \'");
         insert.append(result.getString("entfernung"));
         insert.append("\', \'");
         insert.append(result.getString("beschreibung"));
         insert.append("\', \'");
         insert.append(result.getString("mandantID"));
         insert.append("\')");
      }

      logging.logSQL(insert.toString());
      return insert.toString();
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector objektHydrantenListe = new Vector();
      objektHydrantenListe.add(result.getString("id"));
      objektHydrantenListe.add(result.getString("name"));
      objektHydrantenListe.add(result.getString("hausnummer"));
      objektHydrantenListe.add(result.getString("nennweite"));
      objektHydrantenListe.add(result.getString("GPS_N"));
      objektHydrantenListe.add(result.getString("GPS_O"));
      objektHydrantenListe.add(result.getString("entfernung"));
      objektHydrantenListe.add(result.getString("beschreibung"));
      return objektHydrantenListe;
   }
}
