/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package utilities.fahrzeugeinteilung;

import ao.fahrzeuge.FahrzeugEinteilungAO;
import ao.utils.ProzessBarAO;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeugeinteilung;
import data.tabellen.TabelleFahrzeugeinteilung_temp;
import data.tabellen.mitglied.TabelleMitglied;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JComboBox;
import logging.logging;
import utilities.Konstante;
import utilities.Utils;
import utilities.fahrzeugeinteilung.RegelUtilities;

public class RegelnFeuerwehr
extends FahrzeugEinteilungAO {
    private static final long serialVersionUID = 1L;

    public static void FeuerwehrFahrzeug(int x, int fahrzeugID, String aktuelleFahrzeugName, HashMap<String, String[]> listen) throws SQLException, InterruptedException {
        logging.logInfo((Object)"Erzeuge Fahrzeugeinteilung nach Regel: FeuerwehrFahrzeug");
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
        TabelleFahrzeugeinteilung tabFahrzeugeinteilung = new TabelleFahrzeugeinteilung();
        int anzahlFahrzeuge = tabFahrzeug.countWithoutAnhaenger();
        int zurVerfuegungStehendeLeute = tabTemp.getCount();
        int minmaleBesatzungAllerFahrezuge = tabFahrzeug.getMinimaleBesatungAllerFahrzeuge();
        int maximaleBesatzungFahrzeuge = tabFahrzeug.getMaximaleBesatungAllerFahrzeuge();
        logging.logInfo((Object)("Erstelle Fahzeugeinteilung f\u00fcr: " + aktuelleFahrzeugName));
        logging.logInfo((Object)"Erzeuge Label");
        logging.logInfo((Object)"Erzeuge Arrays f\u00fcr Fahrzeugpositionen");
        String[] maschiListe = null;
        maschiListe = tabFahrzeug.getFuehrerschein(fahrzeugID).startsWith("C") ? listen.get("maschiListeKlasseC") : listen.get("maschiListeKlasseB");
        logging.logInfo((Object)"Erzeuge Combo Boxen");
        RegelnFeuerwehr.sitzplatz[x][0] = new JComboBox<String>(listen.get("gfListe"));
        RegelnFeuerwehr.sitzplatz[x][1] = new JComboBox<String>(maschiListe);
        RegelnFeuerwehr.sitzplatz[x][2] = new JComboBox<String>(listen.get("atruppfListe"));
        RegelnFeuerwehr.sitzplatz[x][3] = new JComboBox<String>(listen.get("atruppmListe"));
        RegelnFeuerwehr.sitzplatz[x][4] = new JComboBox<String>(listen.get("atruppfListe"));
        RegelnFeuerwehr.sitzplatz[x][5] = new JComboBox<String>(listen.get("atruppmListe"));
        RegelnFeuerwehr.sitzplatz[x][6] = new JComboBox<String>(listen.get("atruppmListe"));
        RegelnFeuerwehr.sitzplatz[x][7] = new JComboBox<String>(listen.get("atruppmListe"));
        RegelnFeuerwehr.sitzplatz[x][8] = new JComboBox<String>(listen.get("melderListe"));
        logging.logInfo((Object)"Setze Feldernamen");
        sitzplatz[x][0].setName("POS0_" + aktuelleFahrzeugName);
        sitzplatz[x][1].setName("POS1_" + aktuelleFahrzeugName);
        sitzplatz[x][2].setName("POS2_" + aktuelleFahrzeugName);
        sitzplatz[x][3].setName("POS3_" + aktuelleFahrzeugName);
        sitzplatz[x][4].setName("POS4_" + aktuelleFahrzeugName);
        sitzplatz[x][5].setName("POS5_" + aktuelleFahrzeugName);
        sitzplatz[x][6].setName("POS6_" + aktuelleFahrzeugName);
        sitzplatz[x][7].setName("POS7_" + aktuelleFahrzeugName);
        sitzplatz[x][8].setName("POS8_" + aktuelleFahrzeugName);
        logging.logInfo((Object)"Position wird aktualisiert");
        String[] listeVonAktuellVerf\u00fcgbarenPersonen = Utils.listToArray(tabTemp.getRestOfMitglieder());
        int pos = 0;
        while (pos < listeVonAktuellVerf\u00fcgbarenPersonen.length) {
            int komma = listeVonAktuellVerf\u00fcgbarenPersonen[pos].toString().indexOf(",");
            String isSelectedName = listeVonAktuellVerf\u00fcgbarenPersonen[pos].toString().substring(0, komma);
            String isSelectedVorname = listeVonAktuellVerf\u00fcgbarenPersonen[pos].toString().substring(komma + 2, listeVonAktuellVerf\u00fcgbarenPersonen[pos].toString().length());
            int currentMID = tabMitglieder.getId(isSelectedName, isSelectedVorname);
            tabTemp.updatePosition(currentMID, tabFahrzeugeinteilung.getCountOfCurrentVehicle(currentMID, fahrzeugID));
            ++pos;
        }
        if (zurVerfuegungStehendeLeute - minmaleBesatzungAllerFahrezuge <= 0 && build.toString().startsWith("Es k\u00f6nnen nicht alle voll Fahrzeuge Besetzt werden,\nda an dieser Veransatltung nicht ge\u00fcgent Mitglieder teilnehmen.\n\n")) {
            build.append("Es k\u00f6nnen nicht alle voll Fahrzeuge Besetzt werden,\nda an dieser Veransatltung nicht ge\u00fcgent Mitglieder teilnehmen.\n\n");
        }
        Random random = new Random();
        int gfCount = tabTemp.getGruppenfuehrerCount();
        int maschiCount = tabTemp.getMaschiCount();
        int tfCount = tabTemp.getTfCount();
        int anzahlWeitereFahrezuge = anzahlFahrzeuge - 1;
        int position = 0;
        int maxBesatzung = tabFahrzeug.getMaxBesatzung(fahrzeugID);
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Gruppenf\u00fchrer"));
        if (tabTemp.getCountErfahrenstenChef() >= 1) {
            sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenChef());
            tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
            --gfCount;
        } else {
            sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
            tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
            --gfCount;
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
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
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Maschinist"));
        if (tabFahrzeug.getFuehrerschein(fahrzeugID).equals("C")) {
            if (gfCount + tfCount - anzahlFahrzeuge * 2 <= 0) {
                sitzplatz[x][1].setSelectedItem(tabTemp.getMaschiOhneTruppf\u00fchrer());
                tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
                --maschiCount;
            } else {
                if (random.nextInt(9) >= 5) {
                    sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseC());
                } else {
                    sitzplatz[x][1].setSelectedItem(tabTemp.getUnerfahrenstenMaschnistKlasseC());
                }
                tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
                --maschiCount;
            }
            if (sitzplatz[x][1].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                if (random.nextInt(9) >= 5) {
                    sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseC());
                } else {
                    sitzplatz[x][1].setSelectedItem(tabTemp.getUnerfahrenstenMaschnistKlasseC());
                }
                tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
                --maschiCount;
            }
        } else if (tabFahrzeug.getFuehrerschein(fahrzeugID).equals("B")) {
            sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenMaschnistKlasseB());
            tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
            --maschiCount;
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
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
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Angriffstruppf\u00fchrer"));
        if (maschiCount - anzahlWeitereFahrezuge >= 1) {
            logging.logInfo((Object)"Es sind ebenfalls genug Maschinisten verf\u00fcgbar");
            if (gfCount - anzahlWeitereFahrezuge >= 1) {
                logging.logInfo((Object)("Ich habe noch " + gfCount + " Gruppenf\u00fchrer zur verf\u00fcgung und kann einen GF als Atruppf\u00fchrer einsetzten"));
                sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
                tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
                --gfCount;
                --tfCount;
            } else {
                logging.logInfo((Object)"Atruppf\u00fchrer Dienstgrad = Kein Gruppenf\u00fchrer");
                sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrer());
                tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
                --tfCount;
            }
        } else {
            logging.logInfo((Object)"Es darf kein Maschinist im Trupp eingesetzt werden, sonst k\u00f6nnen nicht alle Fahrzeuge bewegt werden");
            sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrerMitKlasseB());
            tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
            --tfCount;
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
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
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Angriffstruppmann"));
        if (!sitzplatz[x][2].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
            logging.logInfo((Object)sitzplatz[x][2].getSelectedItem());
            if (tabMitglieder.getDienstgradID(tabMitglieder.getIdByGuiString(sitzplatz[x][2].getSelectedItem().toString())) >= 6) {
                logging.logInfo((Object)"Atruppf\u00fchrer ist ein Gruppenf\u00fchrer --> Unerfahrensten ATM");
                sitzplatz[x][3].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
                tabTemp.deleteOne(sitzplatz[x][3].getSelectedItem().toString());
            } else {
                logging.logInfo((Object)"Atruppf\u00fchrer Dienstgrad = Kein Gruppenf\u00fchrer --> Erfahrensten ATM");
                sitzplatz[x][3].setSelectedItem(tabTemp.getErfahrenerenAngriffstruppmann());
                tabTemp.deleteOne(sitzplatz[x][3].getSelectedItem().toString());
            }
        } else {
            sitzplatz[x][2].setSelectedItem("<bitte w\u00e4hlen>");
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
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
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        if (maschiCount - anzahlWeitereFahrezuge >= 1) {
            logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Wassertruppf\u00fchrer"));
            if (gfCount - anzahlWeitereFahrezuge >= 1) {
                logging.logInfo((Object)("Ich habe noch " + gfCount + " Gruppenf\u00fchrer zur verf\u00fcgung und kann einen GF als Wtruppf\u00fchrer einsetzten"));
                sitzplatz[x][4].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
                tabTemp.deleteOne(sitzplatz[x][4].getSelectedItem().toString());
                --gfCount;
                --tfCount;
            } else {
                logging.logInfo((Object)"Wtruppf\u00fchrer Dienstgrad = Kein Gruppenf\u00fchrer");
                sitzplatz[x][4].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrer());
                tabTemp.deleteOne(sitzplatz[x][4].getSelectedItem().toString());
                --tfCount;
            }
        } else {
            logging.logInfo((Object)"Es darf kein Maschinist im Trupp eingesetzt werden, sonst k\u00f6nnen nicht alle Fahrzeuge bewegt werden");
            sitzplatz[x][4].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrerMitKlasseB());
            tabTemp.deleteOne(sitzplatz[x][4].getSelectedItem().toString());
            --tfCount;
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][5].setVisible(false);
            sitzplatz[x][6].setVisible(false);
            sitzplatz[x][7].setVisible(false);
            sitzplatz[x][8].setVisible(false);
            sitzplatz_label[x][5].setVisible(false);
            sitzplatz_label[x][6].setVisible(false);
            sitzplatz_label[x][7].setVisible(false);
            sitzplatz_label[x][8].setVisible(false);
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Wassertruppmann"));
        sitzplatz[x][5].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
        tabTemp.deleteOne(sitzplatz[x][5].getSelectedItem().toString());
        if (sitzplatz[x][5].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
            sitzplatz[x][5].setSelectedItem(tabTemp.getAgtTr\u00e4ger());
            tabTemp.deleteOne(sitzplatz[x][5].getSelectedItem().toString());
        }
        if (sitzplatz[x][5].getSelectedItem().equals("<bitte w\u00e4hlen>") && !build.toString().contains("Es fehlt ein Atemschutzger\u00e4tetr\u00e4ger in der Fahrzeugeinteilung (" + aktuelleFahrzeugName + ")\n\n")) {
            build.append("Es fehlt ein Atemschutzger\u00e4tetr\u00e4ger in der Fahrzeugeinteilung (" + aktuelleFahrzeugName + ")\n\n");
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][6].setVisible(false);
            sitzplatz[x][7].setVisible(false);
            sitzplatz[x][8].setVisible(false);
            sitzplatz_label[x][6].setVisible(false);
            sitzplatz_label[x][7].setVisible(false);
            sitzplatz_label[x][8].setVisible(false);
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Schlauchtruppf\u00fchrer"));
        sitzplatz[x][6].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
        tabTemp.deleteOne(sitzplatz[x][6].getSelectedItem().toString());
        if (sitzplatz[x][6].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
            sitzplatz[x][6].setSelectedItem(tabTemp.getAgtTr\u00e4ger());
            tabTemp.deleteOne(sitzplatz[x][6].getSelectedItem().toString());
        }
        if (sitzplatz[x][6].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
            String sonderMitgliedPos6 = tabTemp.getMelder();
            if (!sonderMitgliedPos6.equals("<bitte w\u00e4hlen>")) {
                sitzplatz[x][7].addItem(sonderMitgliedPos6);
            }
            sitzplatz[x][6].setSelectedItem(sonderMitgliedPos6);
            tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][7].setVisible(false);
            sitzplatz[x][8].setVisible(false);
            sitzplatz_label[x][7].setVisible(false);
            sitzplatz_label[x][8].setVisible(false);
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Schlauchtruppmann"));
        sitzplatz[x][7].setSelectedItem(tabTemp.getUnerfahrenstenAngriffstruppmann());
        tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
        if (sitzplatz[x][7].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
            sitzplatz[x][7].setSelectedItem(tabTemp.getErfahrenerenAngriffstruppmann());
            tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
        }
        if (sitzplatz[x][7].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
            String sonderMitgliedPos7 = tabTemp.getMelder();
            if (!sonderMitgliedPos7.equals("<bitte w\u00e4hlen>")) {
                sitzplatz[x][7].addItem(sonderMitgliedPos7);
            }
            sitzplatz[x][7].setSelectedItem(sonderMitgliedPos7);
            tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
        }
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][8].setVisible(false);
            sitzplatz_label[x][8].setVisible(false);
            return;
        }
        ++position;
        Thread.sleep(200L);
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Melder"));
        sitzplatz[x][8].setSelectedItem(tabTemp.getMelder());
        tabTemp.deleteOne(sitzplatz[x][8].getSelectedItem().toString());
        if (tabTemp.getCount() >= tabFahrzeug.getMinBesatzung(x + 2) && tabFahrzeug.getMinBesatzung(x + 2) != 0) {
            build.append(Konstante.FAHRZEUGEINTEILUNG_KEINE_LEUTE_MEHR);
        }
    }
}

