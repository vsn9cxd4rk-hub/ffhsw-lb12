package data.tabellen;

import data.DatenbankZugriff;
import data.tabellen.mitglied.TabelleMitglied;
import go.Ausbildung_Plan;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;

public class TabelleAusbildung_plan {

   public void insert(Ausbildung_Plan plan) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO ausbildung_plan (`id`, `jahr`, `veranstaltungID`,  `mitgliederGruppe`, `ausbildungKategorie`, `details`, `ausbilder1`, `ausbilder2`, `mandantID`) VALUES (\'" + plan.getId() + "\', \'" + plan.getJahr() + "\', \'" + plan.getVeranstaltungID() + "\', \'" + plan.getMitgliederGruppe() + "\', \'" + plan.getAusbildungKategorie() + "\', \'" + plan.getDetails() + "\', \'" + plan.getAusbilder1() + "\', \'" + plan.getAusbilder2() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Ausbildung_Plan plan) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update ausbildung_plan set ausbildungKategorie = " + plan.getAusbildungKategorie() + ", details = \'" + plan.getDetails() + "\', ausbilder1 = " + plan.getAusbilder1() + ", ausbilder2 = " + plan.getAusbilder2() + ", mitgliederGruppe = " + plan.getMitgliederGruppe() + " where veranstaltungID = " + plan.getVeranstaltungID() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM ausbildung_plan;");
      logging.logSQL("SELECT max(id) FROM ausbildung_plan;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCountVeranstaltungID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountAusbildungenProJahr(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ausbildung_plan where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ausbildung_plan where jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountAusbildungskategorie(int ausbildungsKategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `ausbildung_plan` WHERE ausbildungKategorie = " + ausbildungsKategorie + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `ausbildung_plan` WHERE ausbildungKategorie = " + ausbildungsKategorie + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAusbildungKategorie(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ausbildungKategorie FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ausbildungKategorie FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getDeatils(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT details FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT details FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getAusbilder1(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ausbilder1 FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ausbilder1 FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAusbilder2(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ausbilder2 FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ausbilder2 FROM ausbildung_plan where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM ausbildung_plan where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ID;");
      logging.logSQL("SELECT id FROM ausbildung_plan where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "order by ID;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public int getCountAusbilder1(int mitgliederID, int jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ausbildung_plan where ausbilder1 = " + mitgliederID + " and mitgliederGruppe = " + mitgliederGruppe + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ausbildung_plan where ausbilder1 = " + mitgliederID + " and mitgliederGruppe = " + mitgliederGruppe + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountAusbilder2(int mitgliederID, int jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM ausbildung_plan where ausbilder2 = " + mitgliederID + " and mitgliederGruppe = " + mitgliederGruppe + "  and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM ausbildung_plan where ausbilder2 = " + mitgliederID + " and mitgliederGruppe = " + mitgliederGruppe + "  and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public HashMap getDatenFürDatenübernahme(int jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT p.id, p.jahr, p.veranstaltungID, k.name, p.details, m.name, m.vorname, a2.name, a2.vorname FROM ausbildung_plan p LEFT JOIN ausbildung_kategorie k ON p.ausbildungKategorie = k.id LEFT JOIN mitglieder m ON p.ausbilder1 = m.id LEFT JOIN mitglieder a2 ON p.ausbilder2 = a2.id where jahr = " + jahr + " and p.mitgliedergruppe = " + mitgliederGruppe + "  and p.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by veranstaltungID;");
      logging.logSQL("SELECT p.id, p.jahr, p.veranstaltungID, k.name, p.details, m.name, m.vorname, a2.name, a2.vorname FROM ausbildung_plan p LEFT JOIN ausbildung_kategorie k ON p.ausbildungKategorie = k.id LEFT JOIN mitglieder m ON p.ausbilder1 = m.id LEFT JOIN mitglieder a2 ON p.ausbilder2 = a2.id where jahr = " + jahr + " and p.mitgliedergruppe = " + mitgliederGruppe + "  and p.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by veranstaltungID;");
      HashMap mapData = new HashMap();

      for(int counter = 0; result.next(); ++counter) {
         String[] data = new String[]{result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), null, null};
         if(result.getString(6) == null) {
            data[5] = "<bitte wählen>";
         } else {
            data[5] = result.getString(6) + ", " + result.getString(7);
         }

         if(result.getString(8) == null) {
            data[6] = "<bitte wählen>";
         } else {
            data[6] = result.getString(8) + ", " + result.getString(9);
         }

         mapData.put(Integer.valueOf(counter), data);
      }

      return mapData;
   }

   public HashMap getData(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM ausbildung_plan where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM ausbildung_plan where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("id", result.getString(1));
         map.put("jahr", result.getString(2));
         map.put("veranstaltungID", result.getString(3));
         map.put("ausbildungKategorie", result.getString(4));
         map.put("details", result.getString(5));
         map.put("ausbilder1", result.getString(6));
         map.put("ausbilder2", result.getString(7));
      }

      return map;
   }

   public HashMap getDataByVeranstaltungId(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT p.jahr, p.veranstaltungID, k.name as kategorie, p.details, m.name as ausbilder1name, m.vorname as ausbilder1vorname FROM ausbildung_plan p LEFT JOIN ausbildung_kategorie k ON k.id = p.ausbildungKategorie LEFT JOIN mitglieder m ON p.ausbilder1 = m.id where p.veranstaltungID = " + veranstaltungID + " and p.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT p.jahr, p.veranstaltungID, k.name as kategorie, p.details, m.name as ausbilder1name, m.vorname as ausbilder1vorname FROM ausbildung_plan p LEFT JOIN ausbildung_kategorie k ON k.id = p.ausbildungKategorie LEFT JOIN mitglieder m ON p.ausbilder1 = m.id where p.veranstaltungID = " + veranstaltungID + " and p.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("jahr", result.getString("jahr"));
         map.put("veranstaltungID", result.getString("veranstaltungID"));
         map.put("ausbildungKategorie", result.getString("kategorie"));
         map.put("details", result.getString("details"));
         map.put("ausbilder1", result.getString("ausbilder1name") + ", " + result.getString("ausbilder1vorname"));
      }

      return map;
   }

   public Vector getAusbildungsplanForTable(String jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT v.datum, v.zeit, (SELECT name FROM ausbildung_kategorie where id = p.ausbildungKategorie and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ") as ausbildungKategorie, p.details, p.ausbilder1, p.ausbilder2 FROM veranstaltung v LEFT JOIN ausbildung_plan p ON v.id=p.veranstaltungID where p.mitgliederGruppe = " + mitgliederGruppe + " and v.kategorie = 2 and p.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and v.datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by v.datum, v.zeit;");
      ResultSet result = statement.executeQuery("SELECT v.datum, v.zeit, (SELECT name FROM ausbildung_kategorie where id = p.ausbildungKategorie and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ") as ausbildungKategorie, p.details, p.ausbilder1, p.ausbilder2 FROM veranstaltung v LEFT JOIN ausbildung_plan p ON v.id=p.veranstaltungID where p.mitgliederGruppe = " + mitgliederGruppe + " and v.kategorie = 2 and p.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and v.datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' order by v.datum, v.zeit;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector veranstaltungListe = new Vector();
      veranstaltungListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
      veranstaltungListe.add(result.getString("zeit"));
      veranstaltungListe.add(result.getString("ausbildungKategorie"));
      veranstaltungListe.add(result.getString("details"));
      veranstaltungListe.add((new TabelleMitglied()).getNameVornameByID(result.getInt("ausbilder1")));
      veranstaltungListe.add((new TabelleMitglied()).getNameVornameByID(result.getInt("ausbilder2")));
      return veranstaltungListe;
   }
}
