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
import javax.swing.JComboBox;
import logging.logging;
import utilities.Konstante;
import utilities.Utils;
import utilities.fahrzeugeinteilung.RegelUtilities;

public class RegelnMannschaftstransportfahrzeug
extends FahrzeugEinteilungAO {
    private static final long serialVersionUID = 1L;

    public static void RegelnFahrzeug(int x, int fahrzeugID, String aktuelleFahrzeugName, HashMap<String, String[]> listen) throws SQLException {
        logging.logInfo((Object)"Erzeuge Fahrzeugeinteilung nach Regel: RegelnMannschaftstransportfahrzeug");
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
        TabelleFahrzeugeinteilung tabFahrzeugeinteilung = new TabelleFahrzeugeinteilung();
        build = new StringBuilder();
        build.append(Konstante.FAHRZEUGEINTEILUNG_PROBLEME);
        int maximaleBesatzungFahrzeuge = tabFahrzeug.getMaximaleBesatungAllerFahrzeuge();
        String noetigerFuehrerschein = tabFahrzeug.getFuehrerschein(fahrzeugID);
        logging.logInfo((Object)("Erstelle Fahzeugeinteilung f\u00fcr: " + aktuelleFahrzeugName));
        String[] fahrerListe = null;
        fahrerListe = noetigerFuehrerschein.startsWith("C") ? listen.get("maschiListeKlasseC") : listen.get("maschiListeKlasseB");
        if (tabFahrzeug.getTrupp(fahrzeugID) == 0) {
            logging.logInfo((Object)"Erzeuge Label (Staffel od. Gruppe)");
            sitzplatz_label[x][0].setText("Fahrzeugf\u00fchrer: ");
            sitzplatz_label[x][1].setText("Fahrer / Maschinist: ");
            sitzplatz_label[x][2].setText("Stitzplatz 1: ");
            sitzplatz_label[x][3].setText("Stitzplatz 2: ");
            sitzplatz_label[x][4].setText("Stitzplatz 3: ");
            sitzplatz_label[x][5].setText("Stitzplatz 4: ");
            sitzplatz_label[x][6].setText("Stitzplatz 5: ");
            sitzplatz_label[x][7].setText("Stitzplatz 6: ");
            sitzplatz_label[x][8].setText("Stitzplatz 7: ");
        } else if (tabFahrzeug.getTrupp(fahrzeugID) == 0) {
            logging.logInfo((Object)"Erzeuge Label (Trupp)");
            sitzplatz_label[x][0].setText("Truppf\u00fchrer: ");
            sitzplatz_label[x][1].setText("Fahrer / Maschinist: ");
            sitzplatz_label[x][2].setText("Truppmann: ");
            sitzplatz_label[x][3].setText("Frei: ");
            sitzplatz_label[x][4].setText("Frei: ");
            sitzplatz_label[x][5].setText("Frei: ");
            sitzplatz_label[x][6].setText("Frei: ");
            sitzplatz_label[x][7].setText("Frei: ");
            sitzplatz_label[x][8].setText("Frei: ");
        }
        logging.logInfo((Object)"Erzeuge Combo Boxen");
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][0] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][1] = new JComboBox<String>(fahrerListe);
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][2] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][3] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][4] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][5] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][6] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][7] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
        RegelnMannschaftstransportfahrzeug.sitzplatz[x][8] = new JComboBox<String>(listen.get("sitzplatzlListeDLK"));
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
        int position = 0;
        int maxBesatzung = tabFahrzeug.getMaxBesatzung(fahrzeugID);
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Gruppenf\u00fchrer"));
        sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
        if (sitzplatz[x][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
            sitzplatz[x][0].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrer());
        }
        tabTemp.deleteOne(sitzplatz[x][0].getSelectedItem().toString());
        ++position;
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Fahrer"));
        if (noetigerFuehrerschein.startsWith("C")) {
            sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
            if (sitzplatz[x][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrerMitKlasseC());
            }
        } else {
            sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenGruppenfuehrer());
            if (sitzplatz[x][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                sitzplatz[x][1].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrerMitKlasseB());
            }
        }
        tabTemp.deleteOne(sitzplatz[x][1].getSelectedItem().toString());
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][2].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][3].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][4].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][5].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][6].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][7].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][8].addItem("<bitte w\u00e4hlen>");
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
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Sitzplatz 1"));
        sitzplatz[x][2].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrer());
        if (sitzplatz[x][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
            sitzplatz[x][0].setSelectedItem(tabTemp.getMelder());
        }
        tabTemp.deleteOne(sitzplatz[x][2].getSelectedItem().toString());
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][3].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][4].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][5].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][6].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][7].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][8].addItem("<bitte w\u00e4hlen>");
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
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Sitzplatz 2"));
        sitzplatz[x][3].setSelectedItem(tabTemp.getMelder());
        tabTemp.deleteOne(sitzplatz[x][3].getSelectedItem().toString());
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][4].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][5].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][6].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][7].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][8].addItem("<bitte w\u00e4hlen>");
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
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Sitzplatz 3"));
        sitzplatz[x][4].setSelectedItem(tabTemp.getErfahrenstenAngriffstruppf\u00fchrer());
        if (sitzplatz[x][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
            sitzplatz[x][0].setSelectedItem(tabTemp.getMelder());
        }
        tabTemp.deleteOne(sitzplatz[x][4].getSelectedItem().toString());
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][5].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][6].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][7].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][8].addItem("<bitte w\u00e4hlen>");
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
        ProzessBarAO.progressbar.setValue(++RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge);
        logging.logInfo((Object)("Count = " + RegelUtilities.count + ", Prozent = " + RegelUtilities.count * 100 / maximaleBesatzungFahrzeuge + "%"));
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Sitzplatz 4"));
        sitzplatz[x][5].setSelectedItem(tabTemp.getMelder());
        tabTemp.deleteOne(sitzplatz[x][5].getSelectedItem().toString());
        if (position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][6].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][7].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][8].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][6].setVisible(false);
            sitzplatz[x][7].setVisible(false);
            sitzplatz[x][8].setVisible(false);
            sitzplatz_label[x][6].setVisible(false);
            sitzplatz_label[x][7].setVisible(false);
            sitzplatz_label[x][8].setVisible(false);
            return;
        }
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Sitzplatz 5"));
        sitzplatz[x][6].setSelectedItem(tabTemp.getMelder());
        tabTemp.deleteOne(sitzplatz[x][6].getSelectedItem().toString());
        if (++position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][7].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][8].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][7].setVisible(false);
            sitzplatz[x][8].setVisible(false);
            sitzplatz_label[x][7].setVisible(false);
            sitzplatz_label[x][8].setVisible(false);
            return;
        }
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Sitzplatz 6"));
        sitzplatz[x][7].setSelectedItem(tabTemp.getMelder());
        tabTemp.deleteOne(sitzplatz[x][7].getSelectedItem().toString());
        if (++position + 1 == maxBesatzung) {
            logging.logInfo((Object)"Fahrzeug ist mit maximaler Besatzung voll");
            sitzplatz[x][8].addItem("<bitte w\u00e4hlen>");
            sitzplatz[x][8].setVisible(false);
            sitzplatz_label[x][8].setVisible(false);
            return;
        }
        ++position;
        logging.logInfo((Object)(String.valueOf(aktuelleFahrzeugName) + " Sitzplatz 7"));
        sitzplatz[x][8].setSelectedItem(tabTemp.getMelder());
        tabTemp.deleteOne(sitzplatz[x][8].getSelectedItem().toString());
    }
}

