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

public class RegelnFeuerwehr extends FahrzeugEinteilungAO {

   private static final long serialVersionUID = 1L;


   public static void FeuerwehrFahrzeug(int x, int fahrzeugID, String aktuelleFahrzeugName, HashMap listen) throws SQLException, InterruptedException {
      logging.logInfo("Erzeuge Fahrzeugeinteilung nach Regel: FeuerwehrFahrzeug");
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

      Random var22 = new Random();
      gfCount = tabTemp.getGruppenfuehrerCount();
      int var23 = tabTemp.getMaschiCount();
      int var24 = tabTemp.getTfCount();
      anzahlWeitereFahrezuge = anzahlFahrzeuge - 1;
      byte position = 0;
      int maxBesatzung = tabFahrzeug.getMaxBesatzung(fahrzeugID);
      logging.logInfo(aktuelleFahrzeugName + " Gruppenführer");
      if(tabTemp.getCountErfahrenstenChef() >= 1) {
         sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenChef());
         tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
         --gfCount;
      } else {
         sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
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
         int var25 = position + 1;
         Thread.sleep(200L);
         ++RegelUtilities.count;
         ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
         logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
         logging.logInfo(aktuelleFahrzeugName + " Maschinist");
         if(tabFahrzeug.getFuehrerschein(fahrzeugID).equals("C")) {
            if(gfCount + var24 - anzahlFahrzeuge * 2 <= 0) {
               sitzplatz[x][1].setSelectedItem(tabTemp.getMaschiOhneTruppführer());
               tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
               --var23;
            } else {
               if(var22.nextInt(9) >= 5) {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseC());
               } else {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getUnerfahrenstenMaschnistKlasseC());
               }

               tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
               --var23;
            }

            if(sitzplatz[x][1].getSelectedItem().equals("<bitte wählen>")) {
               if(var22.nextInt(9) >= 5) {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseC());
               } else {
                  sitzplatz[x][1].setSelectedItem(tabTemp.getUnerfahrenstenMaschnistKlasseC());
               }

               tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
               --var23;
            }
         } else if(tabFahrzeug.getFuehrerschein(fahrzeugID).equals("B")) {
            sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseB());
            tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
            --var23;
         }

         if(var25 + 1 == maxBesatzung) {
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
            ++var25;
            Thread.sleep(200L);
            ++RegelUtilities.count;
            ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
            logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
            logging.logInfo(aktuelleFahrzeugName + " Angriffstruppführer");
            if(var23 - anzahlWeitereFahrezuge >= 1) {
               logging.logInfo("Es sind ebenfalls genug Maschinisten verfügbar");
               if(gfCount - anzahlWeitereFahrezuge >= 1) {
                  logging.logInfo("Ich habe noch " + gfCount + " Gruppenführer zur verfügung und kann einen GF als Atruppführer einsetzten");
                  sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
                  tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
                  --gfCount;
                  --var24;
               } else {
                  logging.logInfo("Atruppführer Dienstgrad = Kein Gruppenführer");
                  sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppführer());
                  tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
                  --var24;
               }
            } else {
               logging.logInfo("Es darf kein Maschinist im Trupp eingesetzt werden, sonst können nicht alle Fahrzeuge bewegt werden");
               sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppführerMitKlasseB());
               tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
               --var24;
            }

            if(var25 + 1 == maxBesatzung) {
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
               ++var25;
               Thread.sleep(200L);
               ++RegelUtilities.count;
               ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
               logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
               logging.logInfo(aktuelleFahrzeugName + " Angriffstruppmann");
               if(!sitzplatz[x][2].getSelectedItem().equals("<bitte wählen>")) {
                  logging.logInfo(sitzplatz[x][2].getSelectedItem());
                  if(tabMitglieder.getDienstgradID(tabMitglieder.getIdByGuiString(sitzplatz[x][2].getSelectedItem().toString())) >= 6) {
                     logging.logInfo("Atruppführer ist ein Gruppenführer --> Unerfahrensten ATM");
                     sitzplatz[x][3].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
                     tabTemp.deleteOne(sitzplatz[x][3].getSelectedItem().toString());
                  } else {
                     logging.logInfo("Atruppführer Dienstgrad = Kein Gruppenführer --> Erfahrensten ATM");
                     sitzplatz[x][3].setSelectedItem(tabTemp.getErfahrenerenAngriffstruppmann());
                     tabTemp.deleteOne(sitzplatz[x][3].getSelectedItem().toString());
                  }
               } else {
                  sitzplatz[x][2].setSelectedItem("<bitte wählen>");
               }

               if(var25 + 1 == maxBesatzung) {
                  logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
                  sitzplatz[x][4].setVisible(false);
                  sitzplatz[x][5].setVisible(false);
                  sitzplatz[x][6].setVisible(false);
                  sitzplatz[x][7].setVisible(false);
                  sitzplatz[x][8].setVisible(false);
                  sitzplatz_label[x][4].setVisible(false);
                  sitzplatz_label[x][5].setVisible(false);
                  sitzplatz_label[x][6].setVisible(false);
                  sitzplatz_label[x][7].setVisible(false);
                  sitzplatz_label[x][8].setVisible(false);
               } else {
                  ++var25;
                  Thread.sleep(200L);
                  ++RegelUtilities.count;
                  ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
                  logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
                  if(var23 - anzahlWeitereFahrezuge >= 1) {
                     logging.logInfo(aktuelleFahrzeugName + " Wassertruppführer");
                     if(gfCount - anzahlWeitereFahrezuge >= 1) {
                        logging.logInfo("Ich habe noch " + gfCount + " Gruppenführer zur verfügung und kann einen GF als Wtruppführer einsetzten");
                        sitzplatz[x][4].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
                        tabTemp.deleteOne(sitzplatz[x][4].getSelectedItem().toString());
                        --gfCount;
                        --var24;
                     } else {
                        logging.logInfo("Wtruppführer Dienstgrad = Kein Gruppenführer");
                        sitzplatz[x][4].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppführer());
                        tabTemp.deleteOne(sitzplatz[x][4].getSelectedItem().toString());
                        --var24;
                     }
                  } else {
                     logging.logInfo("Es darf kein Maschinist im Trupp eingesetzt werden, sonst können nicht alle Fahrzeuge bewegt werden");
                     sitzplatz[x][4].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppführerMitKlasseB());
                     tabTemp.deleteOne(sitzplatz[x][4].getSelectedItem().toString());
                     --var24;
                  }

                  if(var25 + 1 == maxBesatzung) {
                     logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
                     sitzplatz[x][5].setVisible(false);
                     sitzplatz[x][6].setVisible(false);
                     sitzplatz[x][7].setVisible(false);
                     sitzplatz[x][8].setVisible(false);
                     sitzplatz_label[x][5].setVisible(false);
                     sitzplatz_label[x][6].setVisible(false);
                     sitzplatz_label[x][7].setVisible(false);
                     sitzplatz_label[x][8].setVisible(false);
                  } else {
                     ++var25;
                     Thread.sleep(200L);
                     ++RegelUtilities.count;
                     ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
                     logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
                     logging.logInfo(aktuelleFahrzeugName + " Wassertruppmann");
                     sitzplatz[x][5].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
                     tabTemp.deleteOne(sitzplatz[x][5].getSelectedItem().toString());
                     if(sitzplatz[x][5].getSelectedItem().equals("<bitte wählen>")) {
                        sitzplatz[x][5].setSelectedItem(tabTemp.getAgtTräger());
                        tabTemp.deleteOne(sitzplatz[x][5].getSelectedItem().toString());
                     }

                     if(sitzplatz[x][5].getSelectedItem().equals("<bitte wählen>") && !build.toString().contains("Es fehlt ein Atemschutzgeräteträger in der Fahrzeugeinteilung (" + aktuelleFahrzeugName + ")\n\n")) {
                        build.append("Es fehlt ein Atemschutzgeräteträger in der Fahrzeugeinteilung (" + aktuelleFahrzeugName + ")\n\n");
                     }

                     if(var25 + 1 == maxBesatzung) {
                        logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
                        sitzplatz[x][6].setVisible(false);
                        sitzplatz[x][7].setVisible(false);
                        sitzplatz[x][8].setVisible(false);
                        sitzplatz_label[x][6].setVisible(false);
                        sitzplatz_label[x][7].setVisible(false);
                        sitzplatz_label[x][8].setVisible(false);
                     } else {
                        ++var25;
                        Thread.sleep(200L);
                        ++RegelUtilities.count;
                        ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
                        logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
                        logging.logInfo(aktuelleFahrzeugName + " Schlauchtruppführer");
                        sitzplatz[x][6].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
                        tabTemp.deleteOne(sitzplatz[x][6].getSelectedItem().toString());
                        if(sitzplatz[x][6].getSelectedItem().toString().equals("<bitte wählen>")) {
                           sitzplatz[x][6].setSelectedItem(tabTemp.getAgtTräger());
                           tabTemp.deleteOne(sitzplatz[x][6].getSelectedItem().toString());
                        }

                        String sonderMitgliedPos7;
                        if(sitzplatz[x][6].getSelectedItem().equals("<bitte wählen>")) {
                           sonderMitgliedPos7 = tabTemp.getMelder();
                           if(!sonderMitgliedPos7.equals("<bitte wählen>")) {
                              sitzplatz[x][7].addItem(sonderMitgliedPos7);
                           }

                           sitzplatz[x][6].setSelectedItem(sonderMitgliedPos7);
                           tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
                        }

                        if(var25 + 1 == maxBesatzung) {
                           logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
                           sitzplatz[x][7].setVisible(false);
                           sitzplatz[x][8].setVisible(false);
                           sitzplatz_label[x][7].setVisible(false);
                           sitzplatz_label[x][8].setVisible(false);
                        } else {
                           ++var25;
                           Thread.sleep(200L);
                           ++RegelUtilities.count;
                           ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
                           logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
                           logging.logInfo(aktuelleFahrzeugName + " Schlauchtruppmann");
                           sitzplatz[x][7].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
                           tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
                           if(sitzplatz[x][7].getSelectedItem().equals("<bitte wählen>")) {
                              sitzplatz[x][7].setSelectedItem(tabTemp.getErfahrenerenAngriffstruppmann());
                              tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
                           }

                           if(sitzplatz[x][7].getSelectedItem().equals("<bitte wählen>")) {
                              sonderMitgliedPos7 = tabTemp.getMelder();
                              if(!sonderMitgliedPos7.equals("<bitte wählen>")) {
                                 sitzplatz[x][7].addItem(sonderMitgliedPos7);
                              }

                              sitzplatz[x][7].setSelectedItem(sonderMitgliedPos7);
                              tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
                           }

                           if(var25 + 1 == maxBesatzung) {
                              logging.logInfo("Fahrzeug ist mit maximaler Besatzung voll");
                              sitzplatz[x][8].setVisible(false);
                              sitzplatz_label[x][8].setVisible(false);
                           } else {
                              ++var25;
                              Thread.sleep(200L);
                              ++RegelUtilities.count;
                              ProzessBarAO.progressbar.setValue(RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
                              logging.logInfo("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%");
                              logging.logInfo(aktuelleFahrzeugName + " Melder");
                              sitzplatz[x][8].setSelectedItem(tabTemp.getMelder());
                              tabTemp.deleteOne(sitzplatz[x][8].getSelectedItem().toString());
                              if(tabTemp.getCount() >= tabFahrzeug.getMinBesatzung(x + 2) && tabFahrzeug.getMinBesatzung(x + 2) != 0) {
                                 build.append(Konstante.FAHRZEUGEINTEILUNG_KEINE_LEUTE_MEHR);
                              }

                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
