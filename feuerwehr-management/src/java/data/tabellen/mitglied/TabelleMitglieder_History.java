package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleMitglieder_History {

   public void insert(Mitglieder mitglied) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO mitglieder_history (`changeDate`,`changeTime`,`benutzer`, `id`, `mitgliederGruppe`,`anrede`,`name`, `vorname`, `strasse`, `ort`, `telefonPrivat`, `telefonMobil`, `telefonArbeit`,  `telegrammID`, `email`, `email2`,  `beruf`, `staat`, `dienstgrad`, `ausserDienst`, `mitgliedSeit`, `mitgliedBis`, `gebDatum`, `hochzeit`, `kommentar`, `loeschkenner`, `eMailDeaktiv`, `mandantID`) VALUES (\'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\', \'" + SbcUtils.timeStamp("HH:mm:ss") + "\', \'" + runApplication.loginName + "\', \'" + mitglied.getId() + "\', \'" + mitglied.getMitgliederGruppe() + "\', \'" + mitglied.getAnrede() + "\', \'" + mitglied.getName() + "\', \'" + mitglied.getVorname() + "\', \'" + mitglied.getStrasse() + "\', \'" + mitglied.getOrt() + "\', \'" + mitglied.getTelefonePrivate() + "\', \'" + mitglied.getTelefonMobile() + "\', \'" + mitglied.getTelefonArbeit() + "\', \'" + mitglied.getTelegrammID() + "\', \'" + mitglied.getEmail() + "\', \'" + mitglied.getEmail2() + "\', \'" + mitglied.getBeruf() + "\', \'" + mitglied.getStaat() + "\', \'" + mitglied.getDienstgrad() + "\', \'" + mitglied.getAusserDienst() + "\', \'" + mitglied.getMitgliedSeit() + "\', \'" + mitglied.getMitgliedBis() + "\', \'" + mitglied.getGebDatum() + "\', \'" + mitglied.getHochzeit() + "\', \'" + mitglied.getKommentar() + "\', \'" + mitglied.getLoschkenner() + "\', \'" + mitglied.geteMailVerteilung() + "\', \'" + mitglied.getMandantID() + "\');";
      statement.executeUpdate(sql);
   }

   public Vector getAllMitgliederForHistoryTable(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.changeDate, m.changeTime, m.benutzer, d.beschreibung as dienstgrad, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.email, m.gebDatum FROM mitglieder_history m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.id = " + mitgliedID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by changeDate desc, changeTime desc;");
      ResultSet result = statement.executeQuery("SELECT m.changeDate, m.changeTime, m.benutzer, d.beschreibung as dienstgrad, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.email, m.gebDatum FROM mitglieder_history m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.id = " + mitgliedID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by changeDate desc, changeTime desc;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("changeDate")));
      mitgliederListe.add(result.getString("changeTime"));
      mitgliederListe.add(result.getString("benutzer"));
      mitgliederListe.add(result.getString("dienstgrad"));
      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add(result.getString("strasse"));
      mitgliederListe.add(result.getString("ort"));
      mitgliederListe.add(result.getString("telefonPrivat"));
      mitgliederListe.add(result.getString("telefonMobil"));
      mitgliederListe.add(result.getString("email"));
      mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("gebDatum")));
      return mitgliederListe;
   }
}
