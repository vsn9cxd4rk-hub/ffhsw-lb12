package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder_Angehoerige;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleMitglieder_angehoerige {

   public void insert(Mitglieder_Angehoerige angehoeriger) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO mitglieder_angehoerige (`id`, `name`, `strasse`, `ort`, `telefonPrivat`, `telefonMobil`, `email`, `mandantID`) VALUES (\'" + angehoeriger.getId() + "\', \'" + angehoeriger.getName() + "\', \'" + angehoeriger.getStrasse() + "\', \'" + angehoeriger.getOrt() + "\', \'" + angehoeriger.getTelefonPrivat() + "\', \'" + angehoeriger.getTelefonMobil() + "\', \'" + angehoeriger.getEmail() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Mitglieder_Angehoerige angehoerige) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder_angehoerige set name = \'" + angehoerige.getName() + "\', strasse = \'" + angehoerige.getStrasse() + "\', ort = \'" + angehoerige.getOrt() + "\', telefonPrivat = \'" + angehoerige.getTelefonPrivat() + "\', telefonMobil = \'" + angehoerige.getTelefonMobil() + "\', email = \'" + angehoerige.getEmail() + "\' where id = " + angehoerige.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getCount(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT name FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getStrasse(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT strasse FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT strasse FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getOrt(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT ort FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT ort FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getTelefonPrivat(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT telefonPrivat FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT telefonPrivat FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getTelefonMobil(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT telefonMobil FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT telefonMobil FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getEMail(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT email FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT email FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public Vector getAllAngehoerigeForTable(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d. beschreibung, m.name, m.vorname, ar.name as firma, ar.strasse, ar.ort, ar.telefonPrivat, ar.telefonMobil, ar.email FROM mitglieder m LEFT JOIN mitglieder_angehoerige ar ON m.id = ar.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = " + mitgliederGruppe + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT d. beschreibung, m.name, m.vorname, ar.name as firma, ar.strasse, ar.ort, ar.telefonPrivat, ar.telefonMobil, ar.email FROM mitglieder m LEFT JOIN mitglieder_angehoerige ar ON m.id = ar.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = " + mitgliederGruppe + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(result.getString("beschreibung"));
      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add(result.getString("firma"));
      mitgliederListe.add(result.getString("strasse"));
      mitgliederListe.add(result.getString("ort"));
      mitgliederListe.add(result.getString("telefonPrivat"));
      mitgliederListe.add(result.getString("telefonMobil"));
      mitgliederListe.add(result.getString("email"));
      return mitgliederListe;
   }
}
