package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import go.Clients;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleClients {

   public void insert(Clients clients) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO clients (`id`,`clientID`, `alias`, `typ`, `online`, `zugelassen`, `mandantID`) VALUES (\'" + clients.getId() + "\', \'" + clients.getClientID() + "\', \'" + clients.getAlias() + "\', \'" + clients.getTyp() + "\', \'" + clients.getOnline() + "\', \'" + clients.getZugelassen() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void updateZugelassen(Clients clients) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update clients set zugelassen = " + clients.getZugelassen() + " where clientID = \'" + clients.getClientID() + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateOnline(int onlineStatus) {
      try {
         Statement e = DatenbankZugriff.getInstance().getDbConnection().createStatement();
         String sql = "Update clients set online = " + onlineStatus + ", alias = \'" + InetAddress.getLocalHost() + "\' where clientID = \'" + runApplication.clientID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
         logging.logSQL(sql);
         e.executeUpdate(sql);
      } catch (UnknownHostException var4) {
         logging.logError("Online Status kann nicht gesetzt werden!");
         logging.logPrintStackTrace(var4);
      }

   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM clients;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM clients;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getZugelassenStatus(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT zugelassen FROM clients where clientID = \'" + clientID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT zugelassen FROM clients where clientID = \'" + clientID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountClientID(String clientID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM clients where clientID = \'" + clientID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM clients where clientID = \'" + clientID + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public Vector getAllForTable() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT clientID, alias, typ, online, zugelassen from clients where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("SELECT clientID, alias, typ, online, zugelassen from clients where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getAllIDs() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("Select clientID from clients where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ResultSet result = statement.executeQuery("Select clientID from clients where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector clients = new Vector();
      clients.add(result.getString("clientID"));
      clients.add(result.getString("alias"));
      clients.add(result.getString("typ"));
      if(result.getInt("online") == 0) {
         clients.add("Offline");
      } else {
         clients.add("Online");
      }

      if(result.getInt("zugelassen") == 0) {
         clients.add("NICHT Zugelassen");
      } else {
         clients.add("Zugelassen");
      }

      return clients;
   }
}
