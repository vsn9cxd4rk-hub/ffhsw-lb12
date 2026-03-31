package data.tabellen.fahrzeug;

import data.DatenbankZugriff;
import go.Fahrzeug_Untersuchung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleFahrzeug_untersuchung {

   public void insert(Fahrzeug_Untersuchung untersuchung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO fahrzeug_untersuchung (`id`, `sp`, `tuev`, `service`,`gaswartung`, `infoTuev`, `infoSP`, `infoService`, `infoGas`, `mandantID`) VALUES (\'" + untersuchung.getId() + "\', \'" + untersuchung.getSp() + "\', \'" + untersuchung.getTüv() + "\', \'" + untersuchung.getService() + "\', \'" + untersuchung.getGaswartung() + "\', \'" + untersuchung.getInfoTuev() + "\', \'" + untersuchung.getInfoSP() + "\', \'" + untersuchung.getInfoService() + "\', \'" + untersuchung.getInfoGas() + "\', \'" + untersuchung.getMandantID() + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Fahrzeug_Untersuchung untersuchung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update fahrzeug_untersuchung set sp = \'" + untersuchung.getSp() + "\', tuev = \'" + untersuchung.getTüv() + "\', service = \'" + untersuchung.getService() + "\', gaswartung = \'" + untersuchung.getGaswartung() + "\', infoTuev = \'" + untersuchung.getInfoTuev() + "\', infoSP = \'" + untersuchung.getInfoSP() + "\', infoService = \'" + untersuchung.getInfoService() + "\', infoGas = \'" + untersuchung.getInfoGas() + "\' where id = " + untersuchung.getId() + " and mandantID = " + untersuchung.getMandantID() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoStatus(String spalte, int fahrzeugID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update fahrzeug_untersuchung set " + spalte + " = 1 where id = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getCount(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeug_untersuchung where id = " + id + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeug_untersuchung where id = " + id + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getSP(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sp FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sp FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getTüv(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT tuev FROM fahrzeug_untersuchung where id = " + id + ";");
      ResultSet result = statement.executeQuery("SELECT tuev FROM fahrzeug_untersuchung where id = " + id + ";");
      return result.next()?result.getString(1):null;
   }

   public String getService(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT service FROM fahrzeug_untersuchung where id = " + id + ";");
      ResultSet result = statement.executeQuery("SELECT service FROM fahrzeug_untersuchung where id = " + id + ";");
      return result.next()?result.getString(1):null;
   }

   public String getGasWartung(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT gaswartung FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT gaswartung FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAbgelaufendeUntersuchungen(String untersuchungsType) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, mu." + untersuchungsType + " FROM fahrzeuge m LEFT JOIN fahrzeug_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and ausserDienst = 0 and mu.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT m.name, mu." + untersuchungsType + " FROM fahrzeuge m LEFT JOIN fahrzeug_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and ausserDienst = 0 and mu.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(!result.getString(2).equals("")) {
            liste.add(result.getString(1) + " " + TimeCalculation.parseShortDateForGUI(result.getString(2)));
         } else {
            liste.add("");
         }
      }

      return liste;
   }

   public int getInfoTuevStatus(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoTuev FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoTuev FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoSPStatus(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoSP FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoSP FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoServiceStatus(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoService FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoService FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoGasStatus(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoGas FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoGas FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public Fahrzeug_Untersuchung getData(int fahrzeugID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM fahrzeug_untersuchung where id = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM fahrzeug_untersuchung where id = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Fahrzeug_Untersuchung untersuchng = new Fahrzeug_Untersuchung();

      while(result.next()) {
         untersuchng.setId(result.getInt("id"));
         untersuchng.setTüv(result.getString("tuev"));
         untersuchng.setInfoTuev(result.getInt("infoTuev"));
         untersuchng.setSp(result.getString("sp"));
         untersuchng.setInfoSP(result.getInt("infoSP"));
         untersuchng.setService(result.getString("service"));
         untersuchng.setInfoService(result.getInt("infoService"));
         untersuchng.setGaswartung(result.getString("gaswartung"));
         untersuchng.setInfoGas(result.getInt("infoGas"));
      }

      return untersuchng;
   }
}
