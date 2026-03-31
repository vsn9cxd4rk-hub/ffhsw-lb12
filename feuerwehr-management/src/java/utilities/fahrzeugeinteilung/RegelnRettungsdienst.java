package utilities.fahrzeugeinteilung;

import ao.fahrzeuge.FahrzeugEinteilungAO;
import ao.utils.ProzessBarAO;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeugeinteilung;
import data.tabellen.fahrzeug.TabelleFahrzeugeinteilung_temp;
import data.tabellen.mitglied.TabelleMitglied;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JComboBox;
import logging.logging;
import utilities.Konstante;
import utilities.Utils;
import utilities.fahrzeugeinteilung.RegelUtilities;

public class RegelnRettungsdienst extends FahrzeugEinteilungAO {

   private static final long serialVersionUID = 1L;


   public static void RegelnRettungdienstfahrzeug(int x, int fahrzeugID, String aktuelleFahrzeugName, HashMap listen) throws SQLException {
      logging.logInfo("Erzeuge Fahrzeugeinteilung nach Regel: RegelnRettungdienstfahrzeug");
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
      TabelleFahrzeugeinteilung tabFahrzeugeinteilung = new TabelleFahrzeugeinteilung();
      build = new StringBuilder();
      build.append(Konstante.FAHRZEUGEINTEILUNG_PROBLEME);
      int maximaleBesatzungFahrzeuge = tabFahrzeug.getMaximaleBesatungAllerFahrzeuge();
      String noetigerFuehrerschein = tabFahrzeug.getFuehrerschein(fahrzeugID);
      logging.logInfo("Erstelle Fahzeugeinteilung für: " + aktuelleFahrzeugName);
      String[] fahrerListe = null;
      if(noetigerFuehrerschein.startsWith("C")) {
         fahrerListe = (String[])listen.get("FahrerListeRTWKlasseC");
      } else {
         fahrerListe = (String[])listen.get("FahrerListeRTWKlasseB");
      }

      logging.logInfo("Erzeuge Label");
      sitzplatz_label[x][0].setText("Transportführer: ");
      sitzplatz_label[x][1].setText("Fahrzeugführer: ");
      sitzplatz_label[x][2].setText("Frei: ");
      sitzplatz_label[x][3].setText("Frei: ");
      sitzplatz_label[x][4].setText("Frei: ");
      sitzplatz_label[x][5].setText("Frei: ");
      sitzplatz_label[x][6].setText("Frei: ");
      sitzplatz_label[x][7].setText("Frei: ");
      sitzplatz_label[x][8].setText("Frei: ");
      logging.logInfo("Erzeuge Combo Boxen");
      sitzplatz[x][0] = new JComboBox((String[])listen.get("rdpListe"));
      sitzplatz[x][1] = new JComboBox(fahrerListe);
      sitzplatz[x][2] = new JComboBox();
      sitzplatz[x][3] = new JComboBox();
      sitzplatz[x][4] = new JComboBox();
      sitzplatz[x][5] = new JComboBox();
      sitzplatz[x][6] = new JComboBox();
      sitzplatz[x][7] = new JComboBox();
      sitzplatz[x][8] = new JComboBox();
      logging.logInfo("Setze Feldernamen");
      sitzplatz[x][0].setName("POS0_" + aktuelleFahrzeugName);
      sitzplatz[x][1].setName("POS1_" + aktuelleFahrzeugName);
      sitzplatz[x][2].setName("POS2_" + aktuelleFahrzeugName);
      sitzplatz[x][3].setName("POS3_" + aktuelleFahrzeugName);
      sitzplatz[x][4].setName("POS4_" + aktuelleFahrzeugName);
      sitzplatz[x][5].setName("POS5_" + aktuelleFahrzeugName);
      sitzplatz[x][6].setName("POS6_" + aktuelleFahrzeugName);
      sitzplatz[x][7].setName("POS7_" + aktuelleFahrzeugName);
      sitzplatz[x][8].setName("POS8_" + aktuelleFahrzeugName);
      logging.logInfo("Position wird aktualisiert");
      String[] listeVonAktuellVerfügbarenPersonen = Utils.listToArray(tabTemp.getRestOfMitglieder());

      for(int pos = 0; pos < listeVonAktuellVerfügbarenPersonen.length; ++pos) {
         int komma = listeVonAktuellVerfügbarenPersonen[pos].toString().indexOf(",");
         String isSelectedName = listeVonAktuellVerfügbarenPersonen[pos].toString().substring(0, komma);
         String isSelectedVorname = listeVonAktuellVerfügbarenPersonen[pos].toString().substring(komma + 2, listeVonAktuellVerfügbarenPersonen[pos].toString().length());
         int currentMID = tabMitglieder.getId(isSelectedName, isSelectedVorname);
         tabTemp.updatePosition(currentMID, tabFahrzeugeinteilung.getCountOfCurrentVehicle(currentMID, fahrzeugID));
      }

      logging.logInfo(aktuelleFahrzeugName + " Transportführer");
      sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenRA());
      if(sitzplatz[x][0].getSelectedItem().toString().equals("<bitte wählen>")) {
         sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenRS());
         tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
      } else {
         tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
      }

      ++RegelUtilities.count;
      ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
      logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
      logging.logInfo(aktuelleFahrzeugName + " Fahrzeugführer");
      if(noetigerFuehrerschein.equals("C")) {
         sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenRSMitKlasseC());
         if(sitzplatz[x][1].getSelectedItem().toString().equals("<bitte wählen>")) {
            sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenRHMitKlasseC());
         }
      } else {
         sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenRSMitKlasseB());
         if(sitzplatz[x][1].getSelectedItem().toString().equals("<bitte wählen>")) {
            sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenRHMitKlasseB());
         }
      }

      tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
      ++RegelUtilities.count;
      ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
      logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
      logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
      sitzplatz[x][2].addItem("<bitte wählen>");
      sitzplatz[x][3].addItem("<bitte wählen>");
      sitzplatz[x][4].addItem("<bitte wählen>");
      sitzplatz[x][5].addItem("<bitte wählen>");
      sitzplatz[x][6].addItem("<bitte wählen>");
      sitzplatz[x][7].addItem("<bitte wählen>");
      sitzplatz[x][8].addItem("<bitte wählen>");
      sitzplatz[x][2].setVisible(false);
      sitzplatz[x][3].setVisible(false);
      sitzplatz[x][4].setVisible(false);
      sitzplatz[x][5].setVisible(false);
      sitzplatz[x][6].setVisible(false);
      sitzplatz[x][7].setVisible(false);
      sitzplatz[x][8].setVisible(false);
      sitzplatz_label[x][2].setVisible(false);
      sitzplatz_label[x][3].setVisible(false);
      sitzplatz_label[x][4].setVisible(false);
      sitzplatz_label[x][5].setVisible(false);
      sitzplatz_label[x][6].setVisible(false);
      sitzplatz_label[x][7].setVisible(false);
      sitzplatz_label[x][8].setVisible(false);
   }
}
