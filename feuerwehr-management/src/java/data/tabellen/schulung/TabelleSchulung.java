package data.tabellen.schulung;

import data.DatenbankZugriff;
import data.tabellen.schulung.TabelleSchulungTeilnehmer;
import go.schulung.Schulung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import utilities.TimeCalculation;

public class TabelleSchulung {

   public void insert(Schulung schulung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO schulung (`id`,`jahr`, `name`,  `gruppenID`, `minTeilnehmer`,`maxTeilnehmer`,`startDatum`,`endeDatum`) VALUES (\'" + schulung.getId() + "\', \'" + schulung.getJahr() + "\', \'" + schulung.getName() + "\', \'" + schulung.getGruppenID() + "\', \'" + schulung.getMinTeilnehmer() + "\', \'" + schulung.getMaxTeilnehmer() + "\', \'" + schulung.getStartDatum() + "\', \'" + schulung.getEndeDatum() + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateStartEnde(Schulung schulung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update schulung set startDatum = \'" + schulung.getStartDatum() + "\', endeDatum = \'" + schulung.getEndeDatum() + "\' where id = " + schulung.getId() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public String getMinTeilnhemer(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT minTeilnehmer FROM schulung where id = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT minTeilnehmer FROM schulung where id = " + schulungID + ";");
      return result.next()?result.getString(1):null;
   }

   public String getMaxTeilnhemer(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT maxTeilnehmer FROM schulung where id = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT maxTeilnehmer FROM schulung where id = " + schulungID + ";");
      return result.next()?result.getString(1):null;
   }

   public int getGruppe(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT gruppenID FROM schulung where id = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT gruppenID FROM schulung where id = " + schulungID + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getName(int schulungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM schulung where id = " + schulungID + ";");
      ResultSet result = statement.executeQuery("SELECT name FROM schulung where id = " + schulungID + ";");
      return result.next()?result.getString(1):null;
   }

   public int getSchulungID(String name, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM schulung where name = \'" + name + "\' and jahr = " + jahr + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM schulung where name = \'" + name + "\' and jahr = " + jahr + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM schulung;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM schulung;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getCount(String schulungName, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM schulung where name = \'" + schulungName + "\' and jahr = " + jahr + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM schulung where name = \'" + schulungName + "\' and jahr = " + jahr + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAlleSchulungenEinesJahres(int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name from schulung where jahr = " + jahr + " order by name;");
      ResultSet result = statement.executeQuery("SELECT name from schulung where jahr = " + jahr + " order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllSchulungIDs(int[] schulungGruppen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      StringBuilder additionSQL = new StringBuilder();

      for(int result = 0; result < schulungGruppen.length; ++result) {
         additionSQL.append(schulungGruppen[result]);
         if(result < schulungGruppen.length - 1) {
            additionSQL.append(",");
         }
      }

      logging.logSQL("SELECT id FROM schulung where gruppenID in (" + additionSQL + ");");
      ResultSet var6 = statement.executeQuery("SELECT id FROM schulung where gruppenID in (" + additionSQL + ");");
      ArrayList liste = new ArrayList();

      while(var6.next()) {
         liste.add(Integer.valueOf(var6.getInt(1)));
      }

      return liste;
   }

   public Vector getAllSchulungen(int[] schulungGruppen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      StringBuilder additionSQL = new StringBuilder();

      for(int result = 0; result < schulungGruppen.length; ++result) {
         additionSQL.append(schulungGruppen[result]);
         if(result < schulungGruppen.length - 1) {
            additionSQL.append(",");
         }
      }

      logging.logSQL("SELECT id, name, maxTeilnehmer, startDatum, endeDatum FROM schulung where gruppenID in (" + additionSQL + ")");
      ResultSet var6 = statement.executeQuery("SELECT id, name, maxTeilnehmer, startDatum, endeDatum FROM schulung where gruppenID in (" + additionSQL + ")");
      Vector liste = new Vector();

      while(var6.next()) {
         liste.add(this.mapResultSetToVector(var6));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector liste = new Vector();
      liste.add(result.getString("name"));
      liste.add(TimeCalculation.parseDateForGUI(result.getString("startDatum")));
      liste.add(TimeCalculation.parseDateForGUI(result.getString("endeDatum")));
      liste.add((new TabelleSchulungTeilnehmer()).getCountOfTeilnehmer(result.getInt("id")) + " / " + result.getString("maxTeilnehmer"));
      return liste;
   }
}
