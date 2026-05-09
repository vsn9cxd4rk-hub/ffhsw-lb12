/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.MyEvent
 */
package utilities.fahrzeugeinteilung;

import ao.fahrzeuge.FahrzeugEinteilungAO;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeugeinteilung_temp;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;
import utilities.fahrzeugeinteilung.RegelnEinsatzleitfahrzeug;
import utilities.fahrzeugeinteilung.RegelnFeuerwehr;
import utilities.fahrzeugeinteilung.RegelnFeuerwehrTrupp;
import utilities.fahrzeugeinteilung.RegelnHubrettungsfahrzeuge;
import utilities.fahrzeugeinteilung.RegelnMannschaftstransportfahrzeug;
import utilities.fahrzeugeinteilung.RegelnRettungsdienst;

public class RegelUtilities
extends FahrzeugEinteilungAO {
    private static final long serialVersionUID = 1L;
    public static int count;

    public static void BerechneFahrzeugeinteilung() {
        try {
            try {
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
                TabelleMitglied tabMitglieder = new TabelleMitglied();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
                int vID = tabVeranstaltung.getVeranstaltungID(runApplication.letzterVeranstaltungsname);
                sitzplatz = new JComboBox[fahrzeugListe.length][9];
                sitzplatz_label = new JLabel[fahrzeugListe.length][9];
                build = new StringBuilder();
                build.append(Konstante.FAHRZEUGEINTEILUNG_PROBLEME);
                count = 0;
                HashMap<String, String[]> map = new HashMap<String, String[]>();
                map.put("rdpListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareRettunsdienstPersonal(vID)));
                map.put("FahrerListeRTWKlasseC", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareRTWFahrerKlasseC(vID)));
                map.put("FahrerListeRTWKlasseB", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareRTWFahrerKlasseB(vID)));
                map.put("dlkFahrerListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareDLKFahrer(vID)));
                map.put("dlkGFListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareDLKGF(vID)));
                map.put("sitzplatzlListeDLK", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareMannschaft(vID)));
                map.put("gfListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareGruppenfuehrer(vID)));
                map.put("zfListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareZugfuehrer(vID)));
                map.put("atruppfListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareTruppfuehrer(vID)));
                map.put("atruppmListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareAtemschtztraeger(vID)));
                map.put("melderListe", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareMannschaft(vID)));
                map.put("maschiListeKlasseC", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareMaschinistenKlasseC(vID)));
                map.put("maschiListeKlasseB", Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getVerfuegbareMaschinistenKlasseCuB(vID)));
                int x = 0;
                while (x < fahrzeugListe.length) {
                    String aktuelleFahrzeugName = fahrzeugListe[x].toString();
                    int aktuellefahrzeugID = tabFahrzeug.getFahrzeugID(aktuelleFahrzeugName);
                    String aktuelleFarzeugBescheibung = tabFahrzeug.getBeschreibungName(aktuellefahrzeugID);
                    logging.logInfo((Object)"Erzeuge Label");
                    RegelUtilities.sitzplatz_label[x][0] = new JLabel("Gruppenf\u00fchrer: ");
                    RegelUtilities.sitzplatz_label[x][1] = new JLabel("Maschinist: ");
                    RegelUtilities.sitzplatz_label[x][2] = new JLabel("Angriffstruppf\u00fchrer: ");
                    RegelUtilities.sitzplatz_label[x][3] = new JLabel("Angriffstruppmann: ");
                    RegelUtilities.sitzplatz_label[x][4] = new JLabel("Wassertruppf\u00fchrer: ");
                    RegelUtilities.sitzplatz_label[x][5] = new JLabel("Wassertruppmann: ");
                    RegelUtilities.sitzplatz_label[x][6] = new JLabel("Schlauchtruppf\u00fchrer: ");
                    RegelUtilities.sitzplatz_label[x][7] = new JLabel("Schlauchtruppmann: ");
                    RegelUtilities.sitzplatz_label[x][8] = new JLabel("Melder: ");
                    if (aktuelleFarzeugBescheibung.equals("Rettungswagen") || aktuelleFarzeugBescheibung.equals("Krankentransportwagen")) {
                        RegelnRettungsdienst.RegelnRettungdienstfahrzeug(x, aktuellefahrzeugID, aktuelleFahrzeugName, map);
                    } else if (aktuelleFarzeugBescheibung.equals("Drehleiter") || aktuelleFarzeugBescheibung.equals("Teleskopmast")) {
                        RegelnHubrettungsfahrzeuge.Hubrettungsfahrzeuge(x, aktuellefahrzeugID, aktuelleFahrzeugName, map);
                    } else if (aktuelleFarzeugBescheibung.equals("Einsatzleitwagen")) {
                        RegelnEinsatzleitfahrzeug.RegelnFahrzeug(x, aktuellefahrzeugID, aktuelleFahrzeugName, map);
                    } else if (aktuelleFarzeugBescheibung.equals("Mannschaftstransportfahrzeug") | aktuelleFarzeugBescheibung.equals("LKW") | aktuelleFarzeugBescheibung.equals("GW Logistik")) {
                        RegelnMannschaftstransportfahrzeug.RegelnFahrzeug(x, aktuellefahrzeugID, aktuelleFahrzeugName, map);
                    } else if (tabFahrzeug.getTrupp(aktuellefahrzeugID) == 0) {
                        RegelnFeuerwehr.FeuerwehrFahrzeug(x, aktuellefahrzeugID, aktuelleFahrzeugName, map);
                    } else {
                        RegelnFeuerwehrTrupp.FeuerwehrTruppFahrzeug(x, aktuellefahrzeugID, aktuelleFahrzeugName, map);
                    }
                    int y = 0;
                    while (y < 9) {
                        if (sitzplatz[x][y].getSelectedItem().equals("<bitte w\u00e4hlen>") && sitzplatz[x][y].isVisible()) {
                            sitzplatz[x][y].setBackground(Color.yellow);
                        }
                        ++y;
                    }
                    ++x;
                }
                x = 0;
                while (x < fahrzeugListe.length) {
                    sitzplatz[x][0].addItemListener(RegelUtilities.createItemListener(x, 0, fahrzeugListe));
                    sitzplatz[x][1].addItemListener(RegelUtilities.createItemListener(x, 1, fahrzeugListe));
                    sitzplatz[x][2].addItemListener(RegelUtilities.createItemListener(x, 2, fahrzeugListe));
                    sitzplatz[x][3].addItemListener(RegelUtilities.createItemListener(x, 3, fahrzeugListe));
                    sitzplatz[x][4].addItemListener(RegelUtilities.createItemListener(x, 4, fahrzeugListe));
                    sitzplatz[x][5].addItemListener(RegelUtilities.createItemListener(x, 5, fahrzeugListe));
                    sitzplatz[x][6].addItemListener(RegelUtilities.createItemListener(x, 6, fahrzeugListe));
                    sitzplatz[x][7].addItemListener(RegelUtilities.createItemListener(x, 7, fahrzeugListe));
                    sitzplatz[x][8].addItemListener(RegelUtilities.createItemListener(x, 8, fahrzeugListe));
                    ++x;
                }
                if (tabTemp.getCount() != 0) {
                    textfield.setText(Utils.listToString(tabTemp.getRestOfMitglieder()));
                    tabTemp.deleteAll();
                }
            }
            catch (InterruptedException | SQLException e) {
                MyEvent.setEvent((String)"0x0030");
                JOptionPane.showMessageDialog(null, Konstante.FAHRZEUGEINTEILUNG_FEHLGESCHLAGEN, "Fehlermeldung", 0);
                logging.logPrintStackTrace((Exception)e);
                try {
                    new TabelleFahrzeugeinteilung_temp().deleteAll();
                }
                catch (SQLException e2) {
                    logging.logPrintStackTrace((Exception)e2);
                }
            }
        }
        finally {
            try {
                new TabelleFahrzeugeinteilung_temp().deleteAll();
            }
            catch (SQLException e) {
                logging.logPrintStackTrace((Exception)e);
            }
        }
    }

    public static ItemListener createItemListener(final int index, final int index2, final String[] fahrzeugListe) {
        ItemListener result = new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                currendChange = sitzplatz[index][index2].getSelectedItem().toString();
                logging.logInfo((Object)("Neuer Eintrag: " + currendChange));
                int count = 0;
                try {
                    int f = 0;
                    while (f < fahrzeugListe.length) {
                        int s = 0;
                        while (s < 9) {
                            if (sitzplatz[f][s].getSelectedItem().toString().equals(currendChange) && !sitzplatz[f][s].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                ++count;
                            }
                            ++s;
                        }
                        ++f;
                    }
                    if (count >= 2) {
                        logging.logInfo((Object)("Konflikt gefunden Position: " + index + " " + index2));
                        RegelUtilities.changeColor(currendChange, fahrzeugListe);
                        konflikt_label.setText("Konflikt gefunden Position: Fahrzeug: " + (index + 1) + " Position: " + (index2 + 1));
                    } else {
                        logging.logInfo((Object)"Konflikt behoben");
                        RegelUtilities.changeColor(" ", fahrzeugListe);
                        konflikt_label.setText("");
                    }
                    String[] zeilen\u00dcbrige = FahrzeugEinteilungAO.textfield.getText().split("\n");
                    StringBuilder build = new StringBuilder();
                    int i = 0;
                    while (i < zeilen\u00dcbrige.length) {
                        if (!zeilen\u00dcbrige[i].equals(currendChange)) {
                            build.append(zeilen\u00dcbrige[i]);
                            build.append("\n");
                        }
                        ++i;
                    }
                    FahrzeugEinteilungAO.textfield.setText(build.toString());
                    logging.logInfo((Object)"Liste der \u00dcbrigen Teilnehmer aktualisiert...");
                }
                catch (NullPointerException | SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        };
        return result;
    }

    public static void changeColor(String currendName, String[] fahrzeugListe) throws SQLException {
        int f = 0;
        while (f < fahrzeugListe.length) {
            int s = 0;
            while (s < 9) {
                if (sitzplatz[f][s].getSelectedItem().toString().equals(currendName)) {
                    System.out.println(currendName);
                    sitzplatz[f][s].setBackground(Color.red);
                } else {
                    sitzplatz[f][s].setBackground(null);
                }
                ++s;
            }
            ++f;
        }
    }
}

