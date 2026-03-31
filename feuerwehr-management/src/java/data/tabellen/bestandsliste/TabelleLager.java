package data.tabellen.bestandsliste;

import data.DatenbankZugriff;
import go.bestandsliste.Lager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleLager {

   public void insert(Lager lager) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO lager (`id`,`name`,`verantwortlicher` , `mandantID`) VALUES (\'" + lager.getId() + "\', \'" + lager.getName() + "\', \'" + lager.getVerantwortlicher() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void delete(int lagerID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from lager where id = " + lagerID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM lager where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(id) FROM lager where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?(result.getInt(1) <= 8999?result.getInt(1) + 9000:result.getInt(1) + 1):0;
   }

   public String getLagerName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM lager where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM lager where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAllLager() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM lager where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      logging.logSQL("SELECT name FROM lager where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by id;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getLagerID(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM lager where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM lager where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getLagerCount(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM lager where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM lager where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getBestandsliste(String gruppe, int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT a.name, count(z.anzahl), z.ort FROM lager_zugewiesen z LEFT JOIN lager_artikel a ON a.id = z.artikelID where z.gruppe = \'" + gruppe + "\' and z.mitgliedID = " + mitgliedID + " and z.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by a.name, z.ort order by z.ort, a.name;");
      logging.logSQL("SELECT a.name, count(z.anzahl), z.ort FROM lager_zugewiesen z LEFT JOIN lager_artikel a ON a.id = z.artikelID where z.gruppe = \'" + gruppe + "\' and z.mitgliedID = " + mitgliedID + " and z.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by a.name, z.ort order by z.ort, a.name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(result.getString(3).equals("")) {
            liste.add(result.getString(1) + " (" + result.getInt(2) + "x)");
         } else {
            liste.add(result.getString(1) + " (" + result.getInt(2) + "x)" + " - " + result.getString(3));
         }
      }

      return liste;
   }
}
