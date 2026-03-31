package data.tabellen;

import data.DatenbankZugriff;
import go.EinsatzUebernahme;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleEinsatz_uebernahme {

   public void updateUebernommen(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update einsatz_uebernahme set uebernommen = 1 where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateAlleObjekte() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update einsatz_uebernahme set FMSObjektID = -1 where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateVeranstaltungID(int id, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update einsatz_uebernahme set veranstaltungID = " + veranstaltungID + " where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getCountOfNichtUebernommen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM einsatz_uebernahme where uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM einsatz_uebernahme where uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public EinsatzUebernahme getErstenEinsatz() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT * FROM einsatz_uebernahme WHERE uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT * FROM einsatz_uebernahme WHERE uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      EinsatzUebernahme uebernahme = new EinsatzUebernahme();
      if(result.next()) {
         uebernahme.setId(result.getInt("id"));
         uebernahme.setVeranstaltungID(result.getInt("veranstaltungID"));
         uebernahme.setFMSObjektID(result.getInt("FMSObjektID"));
         uebernahme.setStraße(result.getString("straße"));
         uebernahme.setDatum(result.getString("datum"));
         uebernahme.setZeit(result.getString("zeit"));
         uebernahme.setStichwort(result.getString("stichwort"));
         uebernahme.setStadtteil(result.getString("stadtteil"));
         uebernahme.setEinsatznummerOffiziell(result.getString("einsatznummerOffiziell"));
         uebernahme.setBeschreibung(result.getString("beschreibung"));
         uebernahme.setMeldung(result.getString("meldung"));
         uebernahme.setUebernommen(result.getInt("uebernommen"));
         return uebernahme;
      } else {
         return null;
      }
   }

   public EinsatzUebernahme getErstenEinsatz(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT * FROM einsatz_uebernahme WHERE id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT * FROM einsatz_uebernahme WHERE id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      EinsatzUebernahme uebernahme = new EinsatzUebernahme();
      if(result.next()) {
         uebernahme.setId(result.getInt("id"));
         uebernahme.setStraße(result.getString("straße"));
         uebernahme.setDatum(result.getString("datum"));
         uebernahme.setZeit(result.getString("zeit"));
         uebernahme.setStichwort(result.getString("stichwort"));
         uebernahme.setStadtteil(result.getString("stadtteil"));
         uebernahme.setEinsatznummerOffiziell(result.getString("einsatznummerOffiziell"));
         uebernahme.setBeschreibung(result.getString("beschreibung"));
         uebernahme.setMeldung(result.getString("meldung"));
         uebernahme.setUebernommen(result.getInt("uebernommen"));
         return uebernahme;
      } else {
         return null;
      }
   }

   public ArrayList getEinsatListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT einsatzNummerOffiziell,stichwort, straße FROM einsatz_uebernahme WHERE uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT einsatzNummerOffiziell,stichwort, straße FROM einsatz_uebernahme WHERE uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + " / " + result.getString(2) + ", " + result.getString(3));
      }

      return liste;
   }

   public ArrayList getEinsatListeID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM einsatz_uebernahme WHERE uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM einsatz_uebernahme WHERE uebernommen = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }
}
