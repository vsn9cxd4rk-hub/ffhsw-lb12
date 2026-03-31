package utilities.fahrzeugeinteilung;

import ao.fahrzeuge.FahrzeugEinteilungAO;
import ao.utils.ProzessBarAO;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeugeinteilung;
import data.tabellen.fahrzeug.TabelleFahrzeugeinteilung_temp;
import data.tabellen.mitglied.TabelleMitglied;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JComboBox;
import logging.logging;
import utilities.Konstante;
import utilities.Utils;
import utilities.fahrzeugeinteilung.RegelUtilities;

public class RegelnFeuerwehrTrupp extends FahrzeugEinteilungAO {

   private static final long serialVersionUID = 1L;


   public static void FeuerwehrTruppFahrzeug(int x, int fahrzeugID, String aktuelleFahrzeugName, HashMap listen) throws SQLException, InterruptedException {
      logging.logInfo("Erzeuge Fahrzeugeinteilung nach Regel: FeuerwehrTruppFahrzeug");
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
      TabelleFahrzeugeinteilung tabFahrzeugeinteilung = new TabelleFahrzeugeinteilung();
      int anzahlFahrzeuge = tabFahrzeug.countWithoutAnhaenger();
      int zurVerfuegungStehendeLeute = tabTemp.getCount();
      int minmaleBesatzungAllerFahrezuge = tabFahrzeug.getMinimaleBesatungAllerFahrzeuge();
      int maximaleBesatzungFahrzeuge = tabFahrzeug.getMaximaleBesatungAllerFahrzeuge();
      logging.logInfo("Erstelle Fahzeugeinteilung für: " + aktuelleFahrzeugName);
      logging.logInfo("Erzeuge Label");
      sitzplatz_label[x][0].setText("Truppführer: ");
      sitzplatz_label[x][1].setText("Maschinist: ");
      sitzplatz_label[x][2].setText("Truppmann: ");
      sitzplatz_label[x][3].setText("Frei: ");
      sitzplatz_label[x][4].setText("Frei: ");
      sitzplatz_label[x][5].setText("Frei: ");
      sitzplatz_label[x][6].setText("Frei: ");
      sitzplatz_label[x][7].setText("Frei: ");
      sitzplatz_label[x][8].setText("Frei: ");
      logging.logInfo("Erzeuge Arrays für Fahrzeugpositionen");
      String[] maschiListe = null;
      if(tabFahrzeug.getFuehrerschein(fahrzeugID).startsWith("C")) {
         maschiListe = (String[])listen.get("maschiListeKlasseC");
      } else {
         maschiListe = (String[])listen.get("maschiListeKlasseB");
      }

      logging.logInfo("Erzeuge Combo Boxen");
      sitzplatz[x][0] = new JComboBox((String[])listen.get("gfListe"));
      sitzplatz[x][1] = new JComboBox(maschiListe);
      sitzplatz[x][2] = new JComboBox((String[])listen.get("atruppfListe"));
      sitzplatz[x][3] = new JComboBox((String[])listen.get("atruppmListe"));
      sitzplatz[x][4] = new JComboBox((String[])listen.get("atruppfListe"));
      sitzplatz[x][5] = new JComboBox((String[])listen.get("atruppmListe"));
      sitzplatz[x][6] = new JComboBox((String[])listen.get("atruppmListe"));
      sitzplatz[x][7] = new JComboBox((String[])listen.get("atruppmListe"));
      sitzplatz[x][8] = new JComboBox((String[])listen.get("melderListe"));
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

      int gfCount;
      int anzahlWeitereFahrezuge;
      for(int random = 0; random < listeVonAktuellVerfügbarenPersonen.length; ++random) {
         gfCount = listeVonAktuellVerfügbarenPersonen[random].toString().indexOf(",");
         String maschiCount = listeVonAktuellVerfügbarenPersonen[random].toString().substring(0, gfCount);
         String tfCount = listeVonAktuellVerfügbarenPersonen[random].toString().substring(gfCount + 2, listeVonAktuellVerfügbarenPersonen[random].toString().length());
         anzahlWeitereFahrezuge = tabMitglieder.getId(maschiCount, tfCount);
         tabTemp.updatePosition(anzahlWeitereFahrezuge, tabFahrzeugeinteilung.getCountOfCurrentVehicle(anzahlWeitereFahrezuge, fahrzeugID));
      }

      if(zurVerfuegungStehendeLeute - minmaleBesatzungAllerFahrezuge <= 0 && build.toString().startsWith("Es können nicht alle voll Fahrzeuge Besetzt werden,\nda an dieser Veransatltung nicht geügent Mitglieder teilnehmen.\n\n")) {
         build.append("Es können nicht alle voll Fahrzeuge Besetzt werden,\nda an dieser Veransatltung nicht geügent Mitglieder teilnehmen.\n\n");
      }

      Random var21 = new Random();
      gfCount = tabTemp.getGruppenfuehrerCount();
      int var22 = tabTemp.getMaschiCount();
      int var23 = tabTemp.getTfCount();
      anzahlWeitereFahrezuge = anzahlFahrzeuge - 1;
      byte position = 1;
      int maxBesatzung = tabFahrzeug.getMaxBesatzung(fahrzeugID);
      logging.logInfo(aktuelleFahrzeugName + " Truppführer");
      if(tabTemp.getCountErfahrenstenChef() >= 1) {
         sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
         tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
         --gfCount;
      } else {
         sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppführer());
         tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
         --gfCount;
      }

      if(position + 1 == maxBesatzung) {
         logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
         sitzplatz[x][1].setVisible(false);
         sitzplatz[x][2].setVisible(false);
         sitzplatz[x][3].setVisible(false);
         sitzplatz[x][4].setVisible(false);
         sitzplatz[x][5].setVisible(false);
         sitzplatz[x][6].setVisible(false);
         sitzplatz[x][7].setVisible(false);
         sitzplatz[x][8].setVisible(false);
         sitzplatz_label[x][1].setVisible(false);
         sitzplatz_label[x][2].setVisible(false);
         sitzplatz_label[x][3].setVisible(false);
         sitzplatz_label[x][4].setVisible(false);
         sitzplatz_label[x][5].setVisible(false);
         sitzplatz_label[x][6].setVisible(false);
         sitzplatz_label[x][7].setVisible(false);
         sitzplatz_label[x][8].setVisible(false);
      } else {
         int var24 = position + 1;
         Thread.sleep(200L);
         ++RegelUtilities.count;
         ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
         logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
         logging.logInfo(aktuelleFahrzeugName + " Maschinist");
         if(tabFahrzeug.getFuehrerschein(fahrzeugID).equals("C")) {
            if(gfCount + var23 - anzahlFahrzeuge * 2 <= 0) {
               sitzplatz[x][1].setSelectedItem(tabTemp.getMaschiOhneTruppführer());
               tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
               --var22;
            } else {
               if(var21.nextInt(9) >= 5) {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseC());
               } else {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getUnerfahrenstenMaschnistKlasseC());
               }

               tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
               --var22;
            }

            if(sitzplatz[x][1].getSelectedItem().equals("<bitte wählen>")) {
               if(var21.nextInt(9) >= 5) {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseC());
               } else {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getUnerfahrenstenMaschnistKlasseC());
               }

               tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
               --var22;
            }
         } else if(tabFahrzeug.getFuehrerschein(fahrzeugID).equals("B")) {
            sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseB());
            tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
            --var22;
         }

         if(var24 + 1 == maxBesatzung) {
            logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
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
         } else {
            ++var24;
            Thread.sleep(200L);
            ++RegelUtilities.count;
            ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
            logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
            logging.logInfo(aktuelleFahrzeugName + " Truppmann 1");
            if(var22 - anzahlWeitereFahrezuge >= 1) {
               logging.logInfo("Es sind ebenfalls genug Maschinisten verfügbar");
               if(gfCount - anzahlWeitereFahrezuge >= 1) {
                  logging.logInfo("Ich habe noch " + gfCount + " Gruppenführer zur verfügung und kann einen GF als Atruppführer einsetzten");
                  sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
                  tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
                  --gfCount;
                  --var23;
               } else {
                  logging.logInfo("Atruppführer Dienstgrad = Kein Gruppenführer");
                  sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppführer());
                  tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
                  --var23;
               }
            } else {
               logging.logInfo("Es darf kein Maschinist im Trupp eingesetzt werden, sonst können nicht alle Fahrzeuge bewegt werden");
               sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppführerMitKlasseB());
               tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
               --var23;
            }

            if(var24 + 1 == maxBesatzung) {
               logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
               sitzplatz[x][3].setVisible(false);
               sitzplatz[x][4].setVisible(false);
               sitzplatz[x][5].setVisible(false);
               sitzplatz[x][6].setVisible(false);
               sitzplatz[x][7].setVisible(false);
               sitzplatz[x][8].setVisible(false);
               sitzplatz_label[x][3].setVisible(false);
               sitzplatz_label[x][4].setVisible(false);
               sitzplatz_label[x][5].setVisible(false);
               sitzplatz_label[x][6].setVisible(false);
               sitzplatz_label[x][7].setVisible(false);
               sitzplatz_label[x][8].setVisible(false);
            } else {
               ++var24;
               Thread.sleep(200L);
               ++RegelUtilities.count;
               ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
               logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
               if(tabTemp.getCount() >= tabFahrzeug.getMinBesatzung(x + 2) && tabFahrzeug.getMinBesatzung(x + 2) != 0) {
                  build.append(Konstante.FAHRZEUGEINTEILUNG_KEINE_LEUTE_MEHR);
               }

            }
         }
      }
   }
}
