package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder_Untersuchung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleMitglieder_untersuchung {

   public void insert(Mitglieder_Untersuchung untersuchung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO mitglieder_untersuchung (`id`, `g25`, `g26`, `agttraining`, `agteinsatztraining`, `infoG25`, `infoG26`, `ablaufLKW`, `infoAblaufLKW`,`fuehrerscheinAusstelldatum`,`fuehrerscheinAblaufDatum`,`fuehrerscheinAblaufC1`,`fuehrerscheinAblaufC1E`,`fuehrerscheinAblaufCE`, `ablaufDienstausweis`,`infoAblaufDienstausweis`, `pruefungDerFahrberechtigung`,`infoPruefungDerFahrberechtigung`,`g30`, `infoG30`,`g41`,`g42`, `mandantID`) VALUES (\'" + untersuchung.getId() + "\', \'" + untersuchung.getG25() + "\', \'" + untersuchung.getG26() + "\', \'" + untersuchung.getAtemschutztraining() + "\', \'" + untersuchung.getAtemschutzEinsatzTraining() + "\', \'" + untersuchung.getInfoG25() + "\', \'" + untersuchung.getInfoG26() + "\', \'" + untersuchung.getAblaufLKW() + "\', \'" + untersuchung.getInfoAblaufLKW() + "\', \'" + untersuchung.getFuehrerscheinAusstellDatum() + "\', \'" + untersuchung.getFuehrerscheinAblaufDatum() + "\', \'" + untersuchung.getFuehrerscheinAblaufDatumC1() + "\', \'" + untersuchung.getFuehrerscheinAblaufDatumC1E() + "\', \'" + untersuchung.getFuehrerscheinAblaufDatumCE() + "\', \'" + untersuchung.getAblaufDienstausweis() + "\', \'" + untersuchung.getInfoAblaufDienstausweis() + "\', \'" + untersuchung.getPruefungDerFahrberechtigung() + "\', \'" + untersuchung.getInfoPruefungDerFahrberechtigung() + "\', \'" + untersuchung.getG30() + "\', \'" + untersuchung.getInfoG30() + "\', \'" + untersuchung.getG41() + "\', \'" + untersuchung.getG42() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void update(Mitglieder_Untersuchung untersuchung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set g25 = \'" + untersuchung.getG25() + "\', g26 = \'" + untersuchung.getG26() + "\', agttraining = \'" + untersuchung.getAtemschutztraining() + "\', agteinsatztraining = \'" + untersuchung.getAtemschutzEinsatzTraining() + "\', infoG25 = \'" + untersuchung.getInfoG25() + "\', infoG26 = \'" + untersuchung.getInfoG26() + "\', ablaufLKW = \'" + untersuchung.getAblaufLKW() + "\', infoAblaufLKW = \'" + untersuchung.getInfoAblaufLKW() + "\', fuehrerscheinAusstelldatum = \'" + untersuchung.getFuehrerscheinAusstellDatum() + "\', fuehrerscheinAblaufDatum = \'" + untersuchung.getFuehrerscheinAblaufDatum() + "\', fuehrerscheinAblaufC1 = \'" + untersuchung.getFuehrerscheinAblaufDatumC1() + "\', fuehrerscheinAblaufC1E = \'" + untersuchung.getFuehrerscheinAblaufDatumC1E() + "\', fuehrerscheinAblaufCE = \'" + untersuchung.getFuehrerscheinAblaufDatumCE() + "\', g30 = \'" + untersuchung.getG30() + "\', infoG30 = \'" + untersuchung.getInfoG30() + "\', g41 = \'" + untersuchung.getG41() + "\', g42 = \'" + untersuchung.getG42() + "\' where id = " + untersuchung.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateDienstausweis(Mitglieder_Untersuchung untersuchung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set ablaufDienstausweis = \'" + untersuchung.getAblaufDienstausweis() + "\', infoAblaufDienstausweis = \'" + untersuchung.getInfoAblaufDienstausweis() + "\', pruefungDerFahrberechtigung = \'" + untersuchung.getPruefungDerFahrberechtigung() + "\', infoPruefungDerFahrberechtigung = \'" + untersuchung.getInfoPruefungDerFahrberechtigung() + "\' where id = " + untersuchung.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoG25(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set infoG25 = 1 where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoAblaufLKW(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set infoAblaufLKW = 1 where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoAblaufDienstausweis(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set infoAblaufDienstausweis = 1 where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoPruefungDerFahrberechtigung(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set infoPruefungDerFahrberechtigung = 1 where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoG26(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set infoG26 = 1 where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoG30(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set infoG30 = 1 where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateAGTTraining(int mitgliedID, String datum) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set agttraining = \'" + datum + "\' where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateAGTEinsatzTraining(int mitgliedID, String datum) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_untersuchung set agteinsatztraining = \'" + datum + "\' where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getCount(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoStatusG25(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoG25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoG25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoStatusAblaufLKW(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoAblaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoAblaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoStatusAblaufDienstausweis(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoAblaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoAblaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoPruefungDerFahrberechtigung(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoPruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoPruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoStatusG26(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoG26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoG26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getInfoStatusG30(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT infoG30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT infoG30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getG25(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT g25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT g25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAblaufLKW(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT ablaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT ablaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAblaufC1(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fuehrerscheinAblaufC1 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT fuehrerscheinAblaufC1 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAblaufC1E(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fuehrerscheinAblaufC1E FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT fuehrerscheinAblaufC1E FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAblaufCE(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fuehrerscheinAblaufCE FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT fuehrerscheinAblaufCE FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAblaufFuehrerschein(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fuehrerscheinAblaufDatum FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT fuehrerscheinAblaufDatum FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAusstelldatumFuehrerschein(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fuehrerscheinAusstelldatum FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT fuehrerscheinAusstelldatum FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAblaufDienstausweis(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT ablaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT ablaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getPruefungDerFahrberechtigung(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT pruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT pruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getG26(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT g26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT g26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getG30(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT g30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT g30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getG41(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT g41 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT g41 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getG42(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT g42 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT g42 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAgtTraining(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT agttraining FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT agttraining FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getAgtEinsatzTraining(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT agteinsatztraining FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT agteinsatztraining FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAbgelaufendeUntersuchungen(String untersuchungsType) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname, mu." + untersuchungsType + " FROM mitglieder m LEFT JOIN mitglieder_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\'and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, mu." + untersuchungsType + " FROM mitglieder m LEFT JOIN mitglieder_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\'and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(!result.getString(3).equals("")) {
            liste.add(result.getString(1) + ", " + result.getString(2) + " " + TimeCalculation.parseShortDateForGUI(result.getString(3)));
         } else {
            liste.add("");
         }
      }

      return liste;
   }

   public Vector getAllUntersuchungForTable() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d. beschreibung, m.name, m.vorname, u.g25, u.g26, u.g30, u.g41, u.g42, u.ablaufLKW FROM mitglieder m LEFT JOIN mitglieder_untersuchung u ON m.id = u.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname");
      ResultSet result = statement.executeQuery("SELECT d. beschreibung, m.name, m.vorname, u.g25, u.g26, u.g30, u.g41, u.g42, u.ablaufLKW FROM mitglieder m LEFT JOIN mitglieder_untersuchung u ON m.id = u.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public HashMap getAllMitgliederUntersuchungData(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("select * from mitglieder_untersuchung where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("select * from mitglieder_untersuchung where id = " + mitgliedID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("id", Integer.toString(result.getInt("id")));
         map.put("g25", result.getString("g25"));
         map.put("g26", result.getString("g26"));
         map.put("agttraining", result.getString("agttraining"));
         map.put("agteinsatztraining", result.getString("agteinsatztraining"));
         map.put("infoG25", result.getString("infoG25"));
         map.put("infoG26", result.getString("infoG26"));
         map.put("ablaufLKW", result.getString("ablaufLKW"));
         map.put("infoAblaufLKW", result.getString("infoAblaufLKW"));
         map.put("fuehrerscheinAusstelldatum", result.getString("fuehrerscheinAusstelldatum"));
         map.put("fuehrerscheinAblaufDatum", result.getString("fuehrerscheinAblaufDatum"));
         map.put("fuehrerscheinAblaufC1", result.getString("fuehrerscheinAblaufC1"));
         map.put("fuehrerscheinAblaufC1E", result.getString("fuehrerscheinAblaufC1E"));
         map.put("fuehrerscheinAblaufCE", result.getString("fuehrerscheinAblaufCE"));
         map.put("ablaufDienstausweis", result.getString("ablaufDienstausweis"));
         map.put("infoAblaufDienstausweis", result.getString("infoAblaufDienstausweis"));
         map.put("pruefungDerFahrberechtigung", result.getString("pruefungDerFahrberechtigung"));
         map.put("infoPruefungDerFahrberechtigung", result.getString("infoPruefungDerFahrberechtigung"));
         map.put("g30", result.getString("g30"));
         map.put("infoG30", result.getString("infoG30"));
         map.put("g41", result.getString("g41"));
         map.put("g42", result.getString("g42"));
      }

      return map;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(result.getString("beschreibung"));
      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add("D-->" + result.getString("g25") + ";" + (String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      mitgliederListe.add("D-->" + result.getString("g26") + ";" + (String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      mitgliederListe.add("D-->" + result.getString("g30") + ";" + (String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      mitgliederListe.add("D-->" + result.getString("g41") + ";" + (String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      mitgliederListe.add("D-->" + result.getString("g42") + ";" + (String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      mitgliederListe.add("D--!" + result.getString("ablaufLKW") + ";" + (String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      return mitgliederListe;
   }
}
