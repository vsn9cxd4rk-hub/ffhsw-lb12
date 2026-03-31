package data.tabellen;

import ao.listen.LehrgangListeOptionenAO;
import data.DatenbankZugriff;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;
import utilities.Utils;

public class TabelleLehrgang {

   public Vector getAllDataForList() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      Vector liste = new Vector();
      int anzahlLehrgaenge = (new TabelleLehrgang_kategorie()).getCount();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result, anzahlLehrgaenge));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result, int anzahlLehrgaenge) throws SQLException {
      Vector lehrgangListe = new Vector();
      lehrgangListe.add(result.getString("beschreibung"));
      lehrgangListe.add(result.getString("name"));
      lehrgangListe.add(result.getString("vorname"));
      int[] mustBeSelected = (new TabelleMitglieder_laufbahn()).getLehrgangData(result.getInt("id"));

      for(int i = 0; i < anzahlLehrgaenge; ++i) {
         if(mustBeSelected[i] == 1) {
            lehrgangListe.add("X");
         } else {
            lehrgangListe.add(" ");
         }
      }

      return lehrgangListe;
   }

   public Vector getFilterDataForList(int[] lehrgangKategorieIDs) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname, m.id FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      Vector liste = new Vector();
      int anzahlLehrgaenge = lehrgangKategorieIDs.length;

      while(result.next()) {
         liste.add(this.mapFilteredResultSetToVector(result, anzahlLehrgaenge, lehrgangKategorieIDs));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapFilteredResultSetToVector(ResultSet result, int anzahlLehrgaenge, int[] lehrgangKategorieIDs) throws SQLException {
      Vector lehrgangListe = new Vector();
      lehrgangListe.add(result.getString("beschreibung"));
      lehrgangListe.add(result.getString("name"));
      lehrgangListe.add(result.getString("vorname"));
      int[] mustBeSelected = (new TabelleMitglieder_laufbahn()).getLehrgangData(result.getInt("id"), lehrgangKategorieIDs);
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();

      for(int i = 0; i < anzahlLehrgaenge; ++i) {
         if(mustBeSelected[i] == 1) {
            lehrgangListe.add("X");
         } else {
            lehrgangListe.add(" ");
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[0].isSelected()) {
         if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getG25(result.getInt("id"))));
         } else {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG25(result.getInt("id"))));
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[1].isSelected()) {
         if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getG26(result.getInt("id"))));
         } else {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG26(result.getInt("id"))));
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[2].isSelected()) {
         if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getG30(result.getInt("id"))));
         } else {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG30(result.getInt("id"))));
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[3].isSelected()) {
         if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getG41(result.getInt("id"))));
         } else {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG41(result.getInt("id"))));
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[4].isSelected()) {
         if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getG42(result.getInt("id"))));
         } else {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG42(result.getInt("id"))));
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[5].isSelected()) {
         if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAgtTraining(result.getInt("id"))));
         } else {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAgtTraining(result.getInt("id"))));
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[6].isSelected()) {
         if(((String)runApplication.EINSTELLUNGEN.get("langesDatumsformatUntersuchungsliste")).equals("1")) {
            lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAgtEinsatzTraining(result.getInt("id"))));
         } else {
            lehrgangListe.add(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAgtEinsatzTraining(result.getInt("id"))));
         }
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[7].isSelected()) {
         lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufC1(result.getInt("id"))));
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[8].isSelected()) {
         lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufLKW(result.getInt("id"))));
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[9].isSelected()) {
         lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufC1E(result.getInt("id"))));
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[10].isSelected()) {
         lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufCE(result.getInt("id"))));
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[11].isSelected()) {
         lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAusstelldatumFuehrerschein(result.getInt("id"))));
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[12].isSelected()) {
         lehrgangListe.add(TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufFuehrerschein(result.getInt("id"))));
      }

      return lehrgangListe;
   }

   public Vector mapHeadNameToVector() throws SQLException {
      Vector lehrgangListe = new Vector();
      TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
      String[] lehrgangKategorie = Utils.listToArray(tabLehrgangKategorie.getAlleLehrgänge());
      lehrgangListe.add("Dienstgrad");
      lehrgangListe.add("Name");
      lehrgangListe.add("Vorname");

      for(int i = 0; i < lehrgangKategorie.length; ++i) {
         lehrgangListe.add(lehrgangKategorie[i]);
      }

      return lehrgangListe;
   }

   public Vector mapFilterHeadNameToVector(int[] lehrgangKategorieIds) throws SQLException {
      Vector lehrgangListe = new Vector();
      TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
      String[] lehrgangKategorie = Utils.listToArray(tabLehrgangKategorie.getFilterNameLehrgang(lehrgangKategorieIds));
      lehrgangListe.add("Dienstgrad");
      lehrgangListe.add("Name");
      lehrgangListe.add("Vorname");

      for(int i = 0; i < lehrgangKategorie.length; ++i) {
         lehrgangListe.add(lehrgangKategorie[i]);
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[0].isSelected()) {
         lehrgangListe.add("G25");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[1].isSelected()) {
         lehrgangListe.add("G26/3");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[2].isSelected()) {
         lehrgangListe.add("G30");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[3].isSelected()) {
         lehrgangListe.add("G41");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[4].isSelected()) {
         lehrgangListe.add("G42");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[5].isSelected()) {
         lehrgangListe.add("AGT Belastungsübung");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[6].isSelected()) {
         lehrgangListe.add("AGT Einsatzübung");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[7].isSelected()) {
         lehrgangListe.add("C1 [11]");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[8].isSelected()) {
         lehrgangListe.add("C [11]");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[9].isSelected()) {
         lehrgangListe.add("C1E [11]");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[10].isSelected()) {
         lehrgangListe.add("CE [11]");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[11].isSelected()) {
         lehrgangListe.add("Austelldatum Führerschein [4a]");
      }

      if(LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[12].isSelected()) {
         lehrgangListe.add("Ablaufdatum Führerschein [4b]");
      }

      if(LehrgangListeOptionenAO.zusatzBox1.isSelected()) {
         lehrgangListe.add(LehrgangListeOptionenAO.zusatzFeld1.getText());
      }

      if(LehrgangListeOptionenAO.zusatzBox2.isSelected()) {
         lehrgangListe.add(LehrgangListeOptionenAO.zusatzFeld2.getText());
      }

      if(LehrgangListeOptionenAO.zusatzBox3.isSelected()) {
         lehrgangListe.add(LehrgangListeOptionenAO.zusatzFeld3.getText());
      }

      return lehrgangListe;
   }
}
