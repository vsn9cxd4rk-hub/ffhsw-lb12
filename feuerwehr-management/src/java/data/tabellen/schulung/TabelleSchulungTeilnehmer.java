package data.tabellen.schulung;

import data.DatenbankZugriff;
import go.schulung.SchulungTeilnehmer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import utilities.TimeCalculation;

public class TabelleSchulungTeilnehmer {

   public void insert(SchulungTeilnehmer schulungTeilnehmer) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schulung_teilnehmer (`id`,`mitgliederID`,`teilnehmerMandant`,`schulungID`, `status`, `statusGrund`, `statusDatum`, `statusZeit`) VALUES (\'" + schulungTeilnehmer.getId() + "\', \'" + schulungTeilnehmer.getMitgliedID() + "\', \'" + schulungTeilnehmer.getTeilnehmerMandant() + "\', \'" + schulungTeilnehmer.getSchulungID() + "\', \'" + schulungTeilnehmer.getStatus() + "\', \'" + schulungTeilnehmer.getStatusGrund() + "\', \'" + schulungTeilnehmer.getStatusDatum() + "\', \'" + schulungTeilnehmer.getStatusZeit() + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateStatus(SchulungTeilnehmer schulungTeilnehmer) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update schulung_teilnehmer set status = " + schulungTeilnehmer.getStatus() + ", statusGrund = \'" + schulungTeilnehmer.getStatusGrund() + "\', statusDatum = \'" + schulungTeilnehmer.getStatusDatum() + "\', statusZeit = \'" + schulungTeilnehmer.getStatusZeit() + "\' where mitgliederID = " + schulungTeilnehmer.getMitgliedID() + " and schulungID = " + schulungTeilnehmer.getSchulungID() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM schulung_teilnehmer;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM schulung_teilnehmer;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCountOfTeilnehmer(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM schulung_teilnehmer where schulungID = " + schulungID + " and status != -1;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schulung_teilnehmer where schulungID = " + schulungID + " and status != -1;");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfOneTeilnehmer(int schulungID, int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM schulung_teilnehmer where schulungID = " + schulungID + " and mitgliederID = " + mitgliederID + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schulung_teilnehmer where schulungID = " + schulungID + " and mitgliederID = " + mitgliederID + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getStatus(int schulungID, int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT status FROM schulung_teilnehmer where schulungID = " + schulungID + " and mitgliederID = " + mitgliederID + ";");
      ResultSet result = statement.executeQuery("SELECT status FROM schulung_teilnehmer where schulungID = " + schulungID + " and mitgliederID = " + mitgliederID + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllTeilnehmerIDs(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.id FROM schulung_teilnehmer st LEFT JOIN mitglieder m ON m.id = st.mitgliederID LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN schulung s ON s.id = st.schulungID where schulungID = " + schulungID + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.id FROM schulung_teilnehmer st LEFT JOIN mitglieder m ON m.id = st.mitgliederID LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN schulung s ON s.id = st.schulungID where schulungID = " + schulungID + " order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public Vector getAllTeilnehmer(int schulungID, int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String additionalSQL = "";
      if(mandantID != 0) {
         additionalSQL = " and teilnehmerMandant = " + mandantID + " ";
      }

      logging.logSQL("SELECT d. beschreibung, m.name, m.vorname, s.name as schulungName, ma.name as einheit, st.status, st.statusDatum, st.statusZeit, st.statusGrund FROM schulung_teilnehmer st LEFT JOIN mitglieder m ON m.id = st.mitgliederID LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN schulung s ON s.id = st.schulungID LEFT JOIN mandant ma ON ma.id = teilnehmerMandant where schulungID = " + schulungID + additionalSQL + "  order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT d. beschreibung, m.name, m.vorname, s.name as schulungName, ma.name as einheit, st.status, st.statusDatum, st.statusZeit, st.statusGrund FROM schulung_teilnehmer st LEFT JOIN mitglieder m ON m.id = st.mitgliederID LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN schulung s ON s.id = st.schulungID LEFT JOIN mandant ma ON ma.id = teilnehmerMandant where schulungID = " + schulungID + additionalSQL + "  order by m.name, m.vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector liste = new Vector();
      liste.add(result.getString("beschreibung"));
      liste.add(result.getString("name"));
      liste.add(result.getString("vorname"));
      liste.add(result.getString("schulungName"));
      liste.add(result.getString("einheit"));
      if(result.getInt("status") == 1) {
         liste.add("Beweorben");
      } else if(result.getInt("status") == 2) {
         liste.add("Akzeptiert");
      } else if(result.getInt("status") == 3) {
         liste.add("Eingeladen");
      } else if(result.getInt("status") == 4) {
         liste.add("Urkunde erstellt");
      } else if(result.getInt("status") == -1) {
         liste.add("ABGELEHNT! - " + result.getString("statusGrund"));
      }

      liste.add(TimeCalculation.parseDateForGUI(result.getString("statusDatum")) + " / " + result.getString("statusZeit"));
      return liste;
   }
}
