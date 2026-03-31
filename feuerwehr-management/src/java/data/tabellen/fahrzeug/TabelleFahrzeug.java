package data.tabellen.fahrzeug;

import data.DatenbankZugriff;
import go.Fahrzeug;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import logging.logging;
import run.runApplication;

public class TabelleFahrzeug {

   public void insert(Fahrzeug fahrzeug) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO fahrzeuge (`id`, `name`, `beschreibung`, `kennzeichen`, `funkrufname`, `sitzplaetze`, `minBesatzung`, `maxBesatzung`, `fuehrerschein`, `ausserDienst`, `anhaenger`, `trupp`, `sortierung`, `homepageBild`, `homepageLink`, `mandantID`) VALUES (\'" + fahrzeug.getId() + "\', \'" + fahrzeug.getName() + "\', \'" + fahrzeug.getBeschreibung() + "\', \'" + fahrzeug.getKennzeichen() + "\', \'" + fahrzeug.getFunkrufname() + "\', \'" + fahrzeug.getSitzplaetze() + "\', \'" + fahrzeug.getMinBesatzung() + "\', \'" + fahrzeug.getMaxBesatzung() + "\', \'" + fahrzeug.getFuehrerschein() + "\', \'" + fahrzeug.getAusserDienst() + "\', \'" + fahrzeug.getAnhaenger() + "\', \'" + fahrzeug.getTrupp() + "\', \'" + fahrzeug.getSortierung() + "\', \'" + fahrzeug.getHomepageBild() + "\', \'" + fahrzeug.getHomepageLink() + "\', \'" + fahrzeug.getMandantID() + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Fahrzeug fahrzeug) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update fahrzeuge set name = \'" + fahrzeug.getName() + "\', beschreibung = \'" + fahrzeug.getBeschreibung() + "\', kennzeichen = \'" + fahrzeug.getKennzeichen() + "\', funkrufname = \'" + fahrzeug.getFunkrufname() + "\', sitzplaetze = \'" + fahrzeug.getSitzplaetze() + "\', minBesatzung = \'" + fahrzeug.getMinBesatzung() + "\', maxBesatzung = \'" + fahrzeug.getMaxBesatzung() + "\', fuehrerschein = \'" + fahrzeug.getFuehrerschein() + "\', ausserDienst = \'" + fahrzeug.getAusserDienst() + "\', anhaenger = \'" + fahrzeug.getAnhaenger() + "\', trupp = \'" + fahrzeug.getTrupp() + "\', sortierung = \'" + fahrzeug.getSortierung() + "\', homepageBild = \'" + fahrzeug.getHomepageBild() + "\', homepageLink = \'" + fahrzeug.getHomepageLink() + "\' where id = " + fahrzeug.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateFahrzeugUndMandant(Fahrzeug fahrzeug) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update fahrzeuge set name = \'" + fahrzeug.getName() + "\', beschreibung = \'" + fahrzeug.getBeschreibung() + "\', kennzeichen = \'" + fahrzeug.getKennzeichen() + "\', funkrufname = \'" + fahrzeug.getFunkrufname() + "\', sitzplaetze = \'" + fahrzeug.getSitzplaetze() + "\', minBesatzung = \'" + fahrzeug.getMinBesatzung() + "\', maxBesatzung = \'" + fahrzeug.getMaxBesatzung() + "\', fuehrerschein = \'" + fahrzeug.getFuehrerschein() + "\', ausserDienst = \'" + fahrzeug.getAusserDienst() + "\', anhaenger = \'" + fahrzeug.getAnhaenger() + "\', trupp = \'" + fahrzeug.getTrupp() + "\', sortierung = \'" + fahrzeug.getSortierung() + "\', mandantID = \'" + fahrzeug.getMandantID() + "\' where id = " + fahrzeug.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
      String sql2 = "Update fahrzeug_untersuchung set mandantID = " + fahrzeug.getMandantID() + " where id = " + fahrzeug.getId() + ";";
      logging.logSQL(sql2);
      statement.executeUpdate(sql2);
   }

   public void updateAusserDienst(int fahrzeugID, int ausserDienstStatus) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update fahrzeuge set ausserDienst = " + ausserDienstStatus + " where id = \'" + fahrzeugID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getCountByFahrzeugBeschreibung(int fahrzeugbeschreibungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `fahrzeuge` WHERE beschreibung = " + fahrzeugbeschreibungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `fahrzeuge` WHERE beschreibung = " + fahrzeugbeschreibungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrzeuge;");
      logging.logSQL("SELECT max(id) FROM fahrzeuge;");
      return result.next()?(result.getInt(1) <= 9999?result.getInt(1) + 10000:result.getInt(1) + 1):0;
   }

   public int countWithoutAnhaenger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int countALL() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeuge where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM fahrzeuge where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int countOhneAnhaenger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getFahrzeugAusserDienstStatus(int fahrzeugID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ausserDienst FROM fahrzeuge where id = " + fahrzeugID + ";");
      logging.logSQL("SELECT ausserDienst FROM fahrzeuge where id = " + fahrzeugID + ";");
      return result.next()?result.getInt(1):0;
   }

   public HashMap getAllFahrzeugData(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT f.id, f.name, b.beschreibung, f.kennzeichen, f.funkrufname, f.sitzplaetze, f.minBesatzung, f.maxBesatzung, f.ausserDienst, f.anhaenger, f.sortierung, f.fuehrerschein, f.homepageBild, f.homepageLink, f.mandantID FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + ";");
      ResultSet result = statement.executeQuery("SELECT f.id, f.name, b.beschreibung, f.kennzeichen, f.funkrufname, f.sitzplaetze, f.minBesatzung, f.maxBesatzung, f.ausserDienst, f.anhaenger, f.sortierung, f.fuehrerschein, f.homepageBild, f.homepageLink, f.mandantID FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("id", Integer.toString(result.getInt(1)));
         map.put("name", result.getString(2));
         map.put("beschreibung", result.getString(3));
         map.put("kennzeichen", result.getString(4));
         map.put("funkrufname", result.getString(5));
         map.put("sitzplaetze", Integer.toString(result.getInt(6)));
         map.put("minBesatzung", Integer.toString(result.getInt(7)));
         map.put("maxBesatzung", Integer.toString(result.getInt(8)));
         map.put("ausserDienst", Integer.toString(result.getInt(9)));
         map.put("anhaenger", Integer.toString(result.getInt(10)));
         map.put("sortierung", Integer.toString(result.getInt(11)));
         map.put("fuehrerschein", result.getString(12));
         map.put("homepageBild", result.getString(13));
         map.put("homepageLink", result.getString(14));
         map.put("mandantID", result.getString(15));
      }

      return map;
   }

   public String getFahrzeugName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getBeschreibungID(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT beschreibung FROM fahrzeuge where id = " + id + ";");
      ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeuge where id = " + id + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getBeschreibungName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT b.beschreibung FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + " and f.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT b.beschreibung FROM fahrzeuge f LEFT JOIN fahrzeug_beschreibung b ON f.beschreibung = b.id where f.id = " + id + " and f.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getKennezeichen(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT kennzeichen FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT kennzeichen FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getSortierung(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sortierung FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sortierung FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getTrupp(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT trupp FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT trupp FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getFunkrufname(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT funkrufname FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT funkrufname FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getFahrzeugID(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM sitzplaetze where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getSitzplatz(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT sitzplaetze FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT sitzplaetze FROM sitzplaetze where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMaxBesatzung(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT maxBesatzung FROM sitzplaetze where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT maxBesatzung FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMinBesatzung(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT minBesatzung FROM sitzplaetze where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT minBesatzung FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getFuehrerschein(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fuehrerschein FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT fuehrerschein FROM fahrzeuge where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getAnhaenger(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT anhaenger FROM fahrzeuge where id = " + id + ";");
      logging.logSQL("SELECT anhaenger FROM sitzplaetze where id = " + id + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getFahrzeugeByBeschreibungID(int beschreibungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = null;
      if(runApplication.BF == 1) {
         sql = "SELECT id, name FROM fahrzeuge where beschreibung = " + beschreibungID + " order by name;";
      } else {
         sql = "SELECT id, name FROM fahrzeuge where beschreibung = " + beschreibungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name;";
      }

      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add("(" + result.getString(1) + ") " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugeMitAnhaenger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      logging.logSQL("SELECT name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugeIDMitAnhaenger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where ausserDienst = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      logging.logSQL("SELECT id FROM fahrzeuge where ausserDienst = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugeOhneAnhaenger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and anhaenger = 0 order by sortierung;");
      logging.logSQL("SELECT name FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugBeschreibungOhneAnhaenger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      logging.logSQL("SELECT beschreibung FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugeIDsOhneAnhaenger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      logging.logSQL("SELECT id FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugeOhneAnhaengerFunkrufname() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT funkrufname FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      logging.logSQL("SELECT funkrufname FROM fahrzeuge where ausserDienst = 0 and anhaenger = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public Fahrzeug getData(int fahrzeugID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM fahrzeuge where id = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM fahrzeuge where id = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Fahrzeug fahrzeug = new Fahrzeug();

      while(result.next()) {
         fahrzeug.setId(result.getInt("id"));
         fahrzeug.setName(result.getString("name"));
         fahrzeug.setBeschreibung(result.getInt("beschreibung"));
         fahrzeug.setKennzeichen(result.getString("kennzeichen"));
         fahrzeug.setFunkrufname(result.getString("funkrufname"));
         fahrzeug.setFuehrerschein(result.getString("fuehrerschein"));
         fahrzeug.setSortierung(result.getInt("sortierung"));
         fahrzeug.setAusserDienst(result.getInt("ausserDienst"));
         fahrzeug.setSitzplaetze(result.getInt("minBesatzung"));
         fahrzeug.setMaxBesatzung(result.getInt("maxBesatzung"));
         fahrzeug.setAnhaenger(result.getInt("anhaenger"));
      }

      return fahrzeug;
   }

   public HashMap getFahrzeugNamenAndID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id, name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id, name FROM fahrzeuge where ausserDienst = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put(Integer.valueOf(result.getInt(1)), result.getString(2));
      }

      return map;
   }

   public ArrayList getAllFahrzeugeFromDataBase() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM fahrzeuge where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;");
      logging.logSQL("SELECT name FROM fahrzeuge where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllFahrzeugIDsFromDataBase() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM fahrzeuge where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;");
      logging.logSQL("SELECT id FROM fahrzeuge where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by sortierung, name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getMinimaleBesatungAllerFahrzeuge() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(minBesatzung) FROM fahrzeuge where ausserDienst = 0;");
      ResultSet result = statement.executeQuery("SELECT sum(minBesatzung) FROM fahrzeuge where ausserDienst = 0;");
      return result.next()?result.getInt(1):0;
   }

   public int getMaximaleBesatungAllerFahrzeuge() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(maxBesatzung) FROM fahrzeuge where ausserDienst = 0;");
      ResultSet result = statement.executeQuery("SELECT sum(maxBesatzung) FROM fahrzeuge where ausserDienst = 0;");
      return result.next()?result.getInt(1):0;
   }
}
