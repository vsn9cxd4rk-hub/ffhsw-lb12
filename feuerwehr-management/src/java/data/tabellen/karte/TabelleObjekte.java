package data.tabellen.karte;

import data.DatenbankKartenZugriff;
import data.DatenbankZugriff;
import data.tabellen.karte.DBConnectionServiceKarte;
import go.karte.Objekte;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleObjekte {

   public static Vector headnameObjekte = new Vector() {

      private static final long serialVersionUID = 1L;

      {
         this.add("ID");
         this.add("Adresse");
         this.add("Objektnummer");
         this.add("Name");
         this.add("GPS_N");
         this.add("GPS_O");
      }
   };


   public void insert(Objekte objekte) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "INSERT INTO karte_objekte (`id`,`name`, `objektNummer`, `straße`, `hausnummer`, `gefaerdungen`, `beschreibung`, `GPS_N`, `GPS_O`, `ansprechpartner`, `bewohner`, `personal`, `oeffnungszeiten`, `mandantID`) VALUES (\'" + objekte.getId() + "\', \'" + objekte.getName() + "\', \'" + objekte.getObjektNummer() + "\', \'" + objekte.getStraße() + "\', \'" + objekte.getHausnummer() + "\', \'" + objekte.getGefaehrdungen() + "\', \'" + objekte.getBeschreibung() + "\', \'" + objekte.getGPS_N() + "\', \'" + objekte.getGPS_O() + "\', \'" + objekte.getAnsprechpartner() + "\', \'" + objekte.getBewohner() + "\', \'" + objekte.getPersonal() + "\', \'" + objekte.getOeffnungszeiten() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Objekte objekte) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "update karte_objekte set name = \'" + objekte.getName() + "\', objektNummer = \'" + objekte.getObjektNummer() + "\', beschreibung = \'" + objekte.getBeschreibung() + "\', straße = \'" + objekte.getStraße() + "\', hausnummer = \'" + objekte.getHausnummer() + "\', gefaerdungen = \'" + objekte.getGefaehrdungen() + "\', GPS_N = \'" + objekte.getGPS_N() + "\', GPS_O = \'" + objekte.getGPS_O() + "\', ansprechpartner = \'" + objekte.getAnsprechpartner() + "\', bewohner = \'" + objekte.getBewohner() + "\', personal = \'" + objekte.getPersonal() + "\', oeffnungszeiten = \'" + objekte.getOeffnungszeiten() + "\' where id = " + objekte.getId();
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteTable() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_objekte;";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteLokal() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from `karte_objekte`";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int straßenID, int objetID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      String sql = "delete from karte_objekte where straße = " + straßenID + " and id = " + objetID + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT max(id) FROM karte_objekte;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM karte_objekte;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public HashMap getObjektData(int objektID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT ks.name as straßenName, ko.hausnummer, ko.id, ko.name, ko.objektNummer, ko.beschreibung, ko.gefaerdungen, ko.GPS_N, ko.GPS_O, ko.ansprechpartner, ko.bewohner, ko.personal, ko.oeffnungszeiten FROM karte_objekte ko LEFT JOIN karte_strassen ks ON ks.id = ko.straße where ko.id = " + objektID + ";");
      ResultSet result = statement.executeQuery("SELECT ks.name as straßenName, ko.hausnummer, ko.id, ko.name, ko.objektNummer, ko.beschreibung, ko.gefaerdungen, ko.GPS_N, ko.GPS_O, ko.ansprechpartner, ko.bewohner, ko.personal, ko.oeffnungszeiten FROM karte_objekte ko LEFT JOIN karte_strassen ks ON ks.id = ko.straße where ko.id = " + objektID + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("GPS_N", result.getString("GPS_N"));
         map.put("GPS_O", result.getString("GPS_O"));
         map.put("hausnummer", result.getString("hausnummer"));
         map.put("objektNummer", result.getString("objektNummer"));
         map.put("straßenName", result.getString("straßenName"));
         map.put("id", result.getString("id"));
         map.put("name", result.getString("name"));
         map.put("gefaerdungen", result.getString("gefaerdungen"));
         map.put("beschreibung", result.getString("beschreibung"));
         map.put("ansprechpartner", result.getString("ansprechpartner"));
         map.put("bewohner", result.getString("bewohner"));
         map.put("personal", result.getString("personal"));
         map.put("oeffnungszeiten", result.getString("oeffnungszeiten"));
      }

      return map;
   }

   public Vector getObjekteForTable(int straßenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT ks.name as straßenName, ko.hausnummer, ko.id, ko.name, ko.objektNummer, ko.beschreibung, ko.GPS_N, ko.GPS_O FROM karte_objekte ko LEFT JOIN karte_strassen ks ON ks.id = ko.straße where ko.straße = " + straßenID + " order by hausnummer;");
      ResultSet result = statement.executeQuery("SELECT ks.name as straßenName, ko.hausnummer, ko.id, ko.name, ko.objektNummer, ko.beschreibung, ko.GPS_N, ko.GPS_O FROM karte_objekte ko LEFT JOIN karte_strassen ks ON ks.id = ko.straße where ko.straße = " + straßenID + " order by hausnummer;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getObjektIDForTable(int straßenID) throws SQLException {
      Statement statement = DBConnectionServiceKarte.getDBZugriff();
      logging.logSQL("SELECT ko.id FROM karte_objekte ko LEFT JOIN karte_strassen ks ON ks.id = ko.straße where straße = " + straßenID + " order by hausnummer;");
      ResultSet result = statement.executeQuery("SELECT ko.id FROM karte_objekte ko LEFT JOIN karte_strassen ks ON ks.id = ko.straße where straße = " + straßenID + " order by hausnummer;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      logging.logSQL(liste);
      return liste;
   }

   public String getDataForObjekteLokalBackup() throws SQLException, NullPointerException {
      Statement statement = DatenbankKartenZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("select * from karte_objekte;");
      ResultSet result = statement.executeQuery("select * from karte_objekte;");
      StringBuilder insert = new StringBuilder();
      insert.append("Insert INTO karte_objekte (`id`, `name`, `objektNummer`, `straße`, `hausnummer`,`gefaerdungen`,`beschreibung`, `GPS_N`, `GPS_O`, `ansprechpartner`,`bewohner`,`personal`,`oeffnungszeiten`, `mandantID`) VALUES ");

      for(int counter = 0; result.next(); ++counter) {
         if(counter != 0) {
            insert.append(",");
         }

         insert.append("(\'");
         insert.append(result.getString("id"));
         insert.append("\', \'");
         insert.append(result.getString("name"));
         insert.append("\', \'");
         insert.append(result.getString("objektNummer"));
         insert.append("\', \'");
         insert.append(result.getString("straße"));
         insert.append("\', \'");
         insert.append(result.getString("hausnummer"));
         insert.append("\', \'");
         insert.append(result.getString("gefaerdungen"));
         insert.append("\', \'");
         insert.append(result.getString("beschreibung"));
         insert.append("\', \'");
         insert.append(result.getString("GPS_N"));
         insert.append("\', \'");
         insert.append(result.getString("GPS_O"));
         insert.append("\', \'");
         insert.append(result.getString("ansprechpartner"));
         insert.append("\', \'");
         insert.append(result.getString("bewohner"));
         insert.append("\', \'");
         insert.append(result.getString("personal"));
         insert.append("\', \'");
         insert.append(result.getString("oeffnungszeiten"));
         insert.append("\', \'");
         insert.append(result.getString("mandantID"));
         insert.append("\')");
      }

      logging.logSQL(insert.toString());
      return insert.toString();
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector objekte = new Vector();
      objekte.add(result.getString("id"));
      objekte.add(result.getString("straßenName") + " " + Integer.toString(result.getInt("hausnummer")));
      objekte.add(result.getString("objektNummer"));
      objekte.add(result.getString("name"));
      objekte.add(result.getString("GPS_N"));
      objekte.add(result.getString("GPS_O"));
      return objekte;
   }
}
