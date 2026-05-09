/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.veranstaltung;

import ao.AbstractFenster;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleAtemschutzpass;
import data.tabellen.TabelleBrandsicherheitswache;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeugeinteilung;
import data.tabellen.TabelleOrganisationen;
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleStatistikbsw;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import data.tabellen.statistik.TabelleStatistikSonstigeVeranstaltung;
import go.Anwesenheit;
import go.Brandsicherheitswachen;
import go.Einsatz;
import go.Einsatz_organisationen;
import go.Einsatz_zeiten;
import go.StatistikBSW;
import go.StatistikEinsatz;
import go.StatistikSonstigeVeranstaltung;
import go.Veranstaltung;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CheckCombo;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.XML;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class VeranstaltungEditierenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonAktualisieren;
    private JButton buttonEinsatzberichtNeuErstellen;
    private JButton buttonFahrzeugBelegung;
    private JButton buttonAtemschutzpass;
    private JComboBox<String> veranstaltungComboBox;
    private JTextField veranstaltungID;
    private JLabel veranstaltung_label;
    private JLabel veranstaltungID_label;
    private JTextField datum;
    private JLabel datum_label;
    private JTextField name;
    private JTextField zeit;
    private JTextField zeitEnde;
    private JLabel name_label;
    private JLabel zeit_label;
    private JLabel zeitEnde_label;
    private JLabel kateogie_label;
    public static JComboBox<String> kategorie;
    private JTextField zeit_treffen;
    private JTextField zeit_start;
    private JTextField zeit_ende;
    private JComboBox<String> ort;
    private JComboBox<String> art;
    private JLabel zeit_treffen_label;
    private JLabel zeit_start_label;
    private JLabel zeit_ende_label;
    private JLabel ort_label;
    private JLabel art_label;
    private JCheckBox[] jCheckboxArray;
    private JTextField einsatzNummerOffiziell;
    private JTextField einsatzNummerIntern;
    private JTextField zeitAlarm;
    private JTextField[] zeitAusgerueckt;
    private JTextField[] zeitEingetroffen;
    private JTextField[] zeitEingerueckt;
    private JComboBox<String> strasse;
    private JComboBox<String> stadtteil;
    private JComboBox<String> beschreibung;
    public static JComboBox<String> einsatzleiter;
    private JComboBox<String> einsatzleiterBF;
    private JLabel einsatzNummerIntern_label;
    private String[] organisationenArray;
    private int[] organisationenArrayIDs;
    public static Boolean[] organisationenArrayBelegung;
    public static JComboBox<String> Box_Stichwort;
    private JLabel einsatznummerOffiziell_label;
    private JLabel[] zeitAusgerueckt_label;
    private JLabel zeitAlamierung_label;
    private JLabel[] zeitEingetroffen_label;
    private JLabel[] zeitEingerueckt_label;
    private JLabel[] fahrzeug_label;
    private JLabel strasse_label;
    private JLabel stadtteil_label;
    private JLabel stichwort_label;
    private JLabel beschreibung_label;
    private JLabel einsatzleiter_label;
    private JLabel einsatzleiterBF_label;
    private JLabel organisationen_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JLabel dummy4;
    private JPanel panelVeranstaltung;
    private JPanel panelBsw;
    private JPanel panelEinsatz;
    private JPanel[] panelFahrzeugeEinsatz;
    private JPanel panelHead;

    public VeranstaltungEditierenAO() {
        super("FeuerwehrManagementSystem - Veranstaltung editieren");
        logging.logInfo((Object)"Starte: VeranstaltungEditierenAO");
    }

    protected void buttonErstellen() {
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonEinsatzberichtNeuErstellen = new JButton("Einsatzbericht Neu erstellen");
        this.buttonFahrzeugBelegung = new JButton("Fahrzeug Belegung");
        this.buttonAtemschutzpass = new JButton("Atemschutzpass");
        this.veranstaltungID = new JTextField(20);
        this.veranstaltung_label = new JLabel("Veranstaltung: ");
        this.veranstaltungID_label = new JLabel("Veranstaltungsnummer: ");
        this.modulBeschreibung = new JLabel("Veranstaltung Editieren");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.dummy4 = new JLabel(runApplication.dummyImage);
        this.datum_label = new JLabel("Datum: ");
        this.datum = new JTextField(20);
        this.name = new JTextField(20);
        this.zeit = new JTextField(20);
        this.zeitEnde = new JTextField(20);
        this.name_label = new JLabel("Veranstaltungsname: ");
        this.zeit_label = new JLabel("Zeit: ");
        this.kateogie_label = new JLabel("Kategorie: ");
        this.zeitEnde_label = new JLabel("Zeit Ende: ");
        TabelleBrandsicherheitswache tabBsw = new TabelleBrandsicherheitswache();
        try {
            String[] ortListe = Utils.listToArrayWithEmptyLine(tabBsw.getOrtListe());
            String[] artListe = Utils.listToArrayWithEmptyLine(tabBsw.getArtListe());
            this.ort = new JComboBox<String>(ortListe);
            this.art = new JComboBox<String>(artListe);
            this.ort.setEditable(true);
            this.art.setEditable(true);
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.zeit_start = new JTextField(20);
        this.zeit_treffen = new JTextField(20);
        this.zeit_ende = new JTextField(20);
        this.zeit_start_label = new JLabel("Veranstaltungsbeginn: ");
        this.zeit_treffen_label = new JLabel("Treffen BSW: ");
        this.zeit_ende_label = new JLabel("Ende BSW: ");
        this.ort_label = new JLabel("Ort:");
        this.art_label = new JLabel("Art:  ");
        try {
            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleStichwort stichwort = new TabelleStichwort();
            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
            TabelleOrganisationen tabOrganisation = new TabelleOrganisationen();
            this.einsatzNummerOffiziell = new JTextField(20);
            this.einsatzNummerIntern = new JTextField(20);
            String[] ortListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getStrasseListe());
            String[] beschreibungListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getBeschreibungListe());
            String[] stadtteilListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getStadtteilListe());
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getAlleTruppUndGruppenfuehrerDerGruppe1());
            String[] einsatzleiterBFListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getEinsatzleiterBFListe());
            String[] stichwortListe = Utils.listToArrayOnlyFORComboBoxes(stichwort.getAllStichwort());
            String[] listeOrganisation = Utils.listToArray(tabOrganisation.getAllOrganisationenWithout1());
            int[] listeOrganisationIDs = Utils.listToIntArray(tabOrganisation.getAllOrganisationenIDsWithout1());
            Box_Stichwort = new JComboBox<String>(stichwortListe);
            this.strasse = new JComboBox<String>(ortListe);
            this.beschreibung = new JComboBox<String>(beschreibungListe);
            this.stadtteil = new JComboBox<String>(stadtteilListe);
            einsatzleiter = new JComboBox<String>(mitgliederListe);
            this.einsatzleiterBF = new JComboBox<String>(einsatzleiterBFListe);
            this.organisationenArray = new String[listeOrganisation.length];
            this.organisationenArray = listeOrganisation;
            this.organisationenArrayIDs = new int[listeOrganisation.length];
            this.organisationenArrayIDs = listeOrganisationIDs;
            organisationenArrayBelegung = new Boolean[listeOrganisation.length];
            this.strasse.setEditable(true);
            this.stadtteil.setEditable(true);
            this.beschreibung.setEditable(true);
            this.einsatzleiterBF.setEditable(true);
            this.zeitAlarm = new JTextField(20);
            this.einsatzNummerIntern_label = new JLabel("Einsatz Z\u00e4hlung: ");
            this.einsatznummerOffiziell_label = new JLabel("Einsatznummer: ");
            this.datum_label = new JLabel("Datum: ");
            this.zeitAlamierung_label = new JLabel("Alamierung (Uhrzeit): ");
            this.strasse_label = new JLabel("Stra\u00dfe / Ort:");
            this.stichwort_label = new JLabel("Stichwort:  ");
            this.beschreibung_label = new JLabel("Einsatzbeschreibung (optional): ");
            this.stadtteil_label = new JLabel("Stadtteil: ");
            this.einsatzleiter_label = new JLabel("Einsatzleiter: ");
            this.einsatzleiterBF_label = new JLabel("Einsatzleiter Berufsfeuerwehr: ");
            this.organisationen_label = new JLabel("Weitere Einheiten / Organisationen: ");
            String[] labels = Utils.listToArray(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
            String[] fahrzeugIDListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeIDsOhneAnhaenger());
            String[] fahrzeugFunkListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeOhneAnhaengerFunkrufname());
            int CheckBoxNumber = tabFahrzeug.countOhneAnhaenger();
            this.jCheckboxArray = new JCheckBox[CheckBoxNumber];
            this.zeitAusgerueckt = new JTextField[CheckBoxNumber];
            this.zeitEingerueckt = new JTextField[CheckBoxNumber];
            this.zeitEingetroffen = new JTextField[CheckBoxNumber];
            this.zeitAusgerueckt_label = new JLabel[CheckBoxNumber];
            this.zeitEingetroffen_label = new JLabel[CheckBoxNumber];
            this.zeitEingerueckt_label = new JLabel[CheckBoxNumber];
            this.fahrzeug_label = new JLabel[CheckBoxNumber];
            this.panelFahrzeugeEinsatz = new JPanel[CheckBoxNumber];
            int x = 0;
            while (x < CheckBoxNumber) {
                this.panelFahrzeugeEinsatz[x] = new JPanel(new GridLayout(4, 2));
                this.jCheckboxArray[x] = new JCheckBox();
                this.zeitAusgerueckt[x] = new JTextField(8);
                this.zeitEingerueckt[x] = new JTextField(8);
                this.zeitEingetroffen[x] = new JTextField(8);
                this.zeitAusgerueckt_label[x] = new JLabel("Ausr\u00fcckzeit: ");
                this.zeitEingetroffen_label[x] = new JLabel("Eingetroffen: ");
                this.zeitEingerueckt_label[x] = new JLabel("Einsatzende: ");
                this.fahrzeug_label[x] = new JLabel(labels[x]);
                this.fahrzeug_label[x].setName(fahrzeugFunkListe[x]);
                this.jCheckboxArray[x].setName(fahrzeugIDListe[x]);
                logging.logInfo((Object)("F\u00fcge Fahrzeug: " + labels[x] + " hinzu...."));
                ++x;
            }
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
    }

    protected void labelErstellen() {
        try {
            TabelleVeranstaltung_Kategorie kategorieListe = new TabelleVeranstaltung_Kategorie();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            String[] veranstaltungListe = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltung());
            String[] liste = Utils.listToArrayOnlyFORComboBoxes(kategorieListe.getAllKategorien());
            this.veranstaltungComboBox = new JComboBox<String>(veranstaltungListe);
            kategorie = new JComboBox<String>(liste);
            kategorie.removeItem("Einsatz");
            kategorie.removeItem("BSW");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.veranstaltungComboBox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                try {
                    int vID = tabVeranstaltung.getVeranstaltungID(VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString());
                    int errorCode = 0;
                    VeranstaltungEditierenAO.this.veranstaltungID.setText(Integer.toString(vID));
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.panelEinsatz);
                    int i = 0;
                    while (i < tabFahrzeug.countOhneAnhaenger()) {
                        VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[i]);
                        ++i;
                    }
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.panelBsw);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.panelVeranstaltung);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.dummy2);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.dummy4);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.buttonZurueck);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.buttonAktualisieren);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.buttonEinsatzberichtNeuErstellen);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.buttonFahrzeugBelegung);
                    VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.buttonAtemschutzpass);
                    if (VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        VeranstaltungEditierenAO.this.veranstaltungID.setText(null);
                    } else if (VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("Einsatz")) {
                        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                        TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        TabelleStichwort tabStichwort = new TabelleStichwort();
                        TabelleEinsatz_organisationen tabEinsatz_Organisation = new TabelleEinsatz_organisationen();
                        HashMap<String, String> map = tabEinsatz.getData(vID);
                        if (!map.get("Datum").equals(SbcUtils.timeStamp((String)"yyyy-MM-dd")) && BerechtigunsManager.ber2[38] == 0) {
                            errorCode = -1;
                            VeranstaltungEditierenAO.this.veranstaltungComboBox.setSelectedItem("<bitte w\u00e4hlen>");
                        } else {
                            VeranstaltungEditierenAO.this.einsatzNummerOffiziell.setText(map.get("einsatznummerOffiziell"));
                            VeranstaltungEditierenAO.this.einsatzNummerIntern.setText(map.get("einsatzNummer"));
                            Box_Stichwort.setSelectedItem(tabStichwort.getStichwortName(Integer.parseInt(map.get("Stichwort"))));
                            VeranstaltungEditierenAO.this.datum.setText(TimeCalculation.parseDateForGUI(map.get("Datum")));
                            VeranstaltungEditierenAO.this.zeitAlarm.setText(map.get("ZeitAlarm"));
                            VeranstaltungEditierenAO.this.strasse.setSelectedItem(map.get("Ort"));
                            VeranstaltungEditierenAO.this.stadtteil.setSelectedItem(map.get("stadtteil"));
                            VeranstaltungEditierenAO.this.einsatzleiterBF.setSelectedItem(map.get("einsatzleiterBF"));
                            if (Integer.parseInt(map.get("einsatzleiter")) == 0) {
                                einsatzleiter.setSelectedItem("<bitte w\u00e4hlen>");
                            } else {
                                einsatzleiter.setSelectedItem(tabMitglied.getNameVornameByID(Integer.parseInt(map.get("einsatzleiter"))));
                            }
                            VeranstaltungEditierenAO.this.beschreibung.setSelectedItem(map.get("beschreibung"));
                            VeranstaltungEditierenAO.this.panelEinsatz = new JPanel(new GridLayout(11, 2));
                            VeranstaltungEditierenAO.this.getContentPane().add("Center", VeranstaltungEditierenAO.this.panelEinsatz);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.einsatzNummerIntern_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.einsatzNummerIntern);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.einsatznummerOffiziell_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.einsatzNummerOffiziell);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.stichwort_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(Box_Stichwort);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.datum_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.datum);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.zeitAlamierung_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.zeitAlarm);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.strasse_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.strasse);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.stadtteil_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.stadtteil);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.einsatzleiter_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(einsatzleiter);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.einsatzleiterBF_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.einsatzleiterBF);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.beschreibung_label);
                            VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.beschreibung);
                            if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1") && VeranstaltungEditierenAO.this.organisationenArray.length != 0) {
                                VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.organisationen_label);
                                organisationenArrayBelegung = new Boolean[VeranstaltungEditierenAO.this.organisationenArray.length];
                                int ob = 0;
                                while (ob < VeranstaltungEditierenAO.this.organisationenArray.length) {
                                    VeranstaltungEditierenAO.organisationenArrayBelegung[ob] = tabEinsatz_Organisation.getStatusOfOrganisation(vID, VeranstaltungEditierenAO.this.organisationenArrayIDs[ob]);
                                    ++ob;
                                }
                                VeranstaltungEditierenAO.this.panelEinsatz.add(CheckCombo.getComboboxWithCheckBoxes(VeranstaltungEditierenAO.this.organisationenArray, VeranstaltungEditierenAO.this.organisationenArrayIDs, organisationenArrayBelegung));
                            } else if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1") && VeranstaltungEditierenAO.this.organisationenArray.length == 0) {
                                VeranstaltungEditierenAO.this.panelEinsatz.add(VeranstaltungEditierenAO.this.organisationen_label);
                                JLabel keineOrganisation = new JLabel("keine Weiteren Organisationen verf\u00fcgbar!");
                                keineOrganisation.setToolTipText("Organisationen k\u00f6nnen \u00fcber die Kategorieverwaltung hinzugef\u00fcgt bzw. editiert werden!");
                                VeranstaltungEditierenAO.this.panelEinsatz.add(keineOrganisation);
                            }
                            VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.dummy4);
                            int CheckBoxNumber = tabFahrzeug.countOhneAnhaenger();
                            int x = 0;
                            while (x < CheckBoxNumber) {
                                int fID = Integer.parseInt(VeranstaltungEditierenAO.this.jCheckboxArray[x].getName());
                                HashMap<String, String> mapZeiten = tabZeiten.getData(vID, fID);
                                if (tabZeiten.getCount(fID, vID) == 1) {
                                    VeranstaltungEditierenAO.this.jCheckboxArray[x].setSelected(true);
                                    VeranstaltungEditierenAO.this.zeitAusgerueckt[x].setText(mapZeiten.get("zeitAusgerueckt"));
                                    VeranstaltungEditierenAO.this.zeitEingetroffen[x].setText(mapZeiten.get("zeitEingetroffen"));
                                    VeranstaltungEditierenAO.this.zeitEingerueckt[x].setText(mapZeiten.get("zeitEingerueckt"));
                                } else {
                                    VeranstaltungEditierenAO.this.jCheckboxArray[x].setSelected(false);
                                    VeranstaltungEditierenAO.this.zeitAusgerueckt[x].setText(null);
                                    VeranstaltungEditierenAO.this.zeitEingetroffen[x].setText(null);
                                    VeranstaltungEditierenAO.this.zeitEingerueckt[x].setText(null);
                                }
                                if (runApplication.EINSTELLUNGEN.get("feldEintreffenAusblenden").equals("1")) {
                                    VeranstaltungEditierenAO.this.zeitEingetroffen[x].setVisible(false);
                                    VeranstaltungEditierenAO.this.zeitEingetroffen_label[x].setVisible(false);
                                }
                                ((VeranstaltungEditierenAO)VeranstaltungEditierenAO.this).panelFahrzeugeEinsatz[x] = new JPanel(new GridLayout(4, 2));
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.fahrzeug_label[x]);
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.jCheckboxArray[x]);
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.zeitAusgerueckt_label[x]);
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.zeitAusgerueckt[x]);
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.zeitEingetroffen_label[x]);
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.zeitEingetroffen[x]);
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.zeitEingerueckt_label[x]);
                                VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x].add(VeranstaltungEditierenAO.this.zeitEingerueckt[x]);
                                VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.panelFahrzeugeEinsatz[x], "Center");
                                ++x;
                            }
                            if (runApplication.EINSTELLUNGEN.get("einsatzleiterBF").equals("0")) {
                                VeranstaltungEditierenAO.this.einsatzleiterBF.setVisible(false);
                                VeranstaltungEditierenAO.this.einsatzleiterBF_label.setVisible(false);
                            } else {
                                VeranstaltungEditierenAO.this.einsatzleiter_label.setText("1. Gruppenf\u00fchrer FF: ");
                            }
                        }
                    } else if (VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("BSW")) {
                        TabelleBrandsicherheitswache tabBsw = new TabelleBrandsicherheitswache();
                        HashMap<String, String> map = tabBsw.getData(vID);
                        VeranstaltungEditierenAO.this.datum.setText(TimeCalculation.parseDateForGUI(map.get("datum")));
                        VeranstaltungEditierenAO.this.ort.setSelectedItem(map.get("ort"));
                        VeranstaltungEditierenAO.this.art.setSelectedItem(map.get("art"));
                        VeranstaltungEditierenAO.this.zeit_treffen.setText(map.get("zeit_treffen"));
                        VeranstaltungEditierenAO.this.zeit_start.setText(map.get("zeit_start"));
                        VeranstaltungEditierenAO.this.zeit_ende.setText(map.get("zeit_ende"));
                        VeranstaltungEditierenAO.this.panelBsw = new JPanel(new GridLayout(6, 2));
                        VeranstaltungEditierenAO.this.getContentPane().add("Center", VeranstaltungEditierenAO.this.panelBsw);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.datum_label);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.datum);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.ort_label);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.ort);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.art_label);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.art);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.zeit_treffen_label);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.zeit_treffen);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.zeit_start_label);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.zeit_start);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.zeit_ende_label);
                        VeranstaltungEditierenAO.this.panelBsw.add(VeranstaltungEditierenAO.this.zeit_ende);
                    } else {
                        TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
                        VeranstaltungEditierenAO.this.name.setText(tabVeranstaltung.getVeranstaltungName2(vID));
                        VeranstaltungEditierenAO.this.datum.setText(TimeCalculation.parseDateForGUI(tabVeranstaltung.getDatum(vID)));
                        VeranstaltungEditierenAO.this.zeit.setText(tabVeranstaltung.getZeitStart(vID));
                        VeranstaltungEditierenAO.this.zeitEnde.setText(tabVeranstaltung.getZeitEnde(vID));
                        kategorie.setSelectedItem(tabKategorie.getName(tabVeranstaltung.getVeranstaltungKategorieID(vID)));
                        VeranstaltungEditierenAO.this.panelVeranstaltung = new JPanel(new GridLayout(5, 2));
                        VeranstaltungEditierenAO.this.getContentPane().add("Center", VeranstaltungEditierenAO.this.panelVeranstaltung);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.kateogie_label);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(kategorie);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.name_label);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.name);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.datum_label);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.datum);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.zeit_label);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.zeit);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.zeitEnde_label);
                        VeranstaltungEditierenAO.this.panelVeranstaltung.add(VeranstaltungEditierenAO.this.zeitEnde);
                    }
                    if (!VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.dummy2);
                        VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.buttonZurueck);
                        VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.buttonAktualisieren);
                        if (VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("Einsatz")) {
                            VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.buttonFahrzeugBelegung);
                            VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.buttonAtemschutzpass);
                        }
                        if (VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("Einsatz") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle")) {
                            VeranstaltungEditierenAO.this.add(VeranstaltungEditierenAO.this.buttonEinsatzberichtNeuErstellen);
                            if (BerechtigunsManager.ber[95] == 0) {
                                VeranstaltungEditierenAO.this.buttonEinsatzberichtNeuErstellen.setEnabled(false);
                            } else {
                                VeranstaltungEditierenAO.this.buttonEinsatzberichtNeuErstellen.setEnabled(true);
                            }
                        }
                    } else if (errorCode == -1) {
                        logging.logInfo((Object)"ErrorCode -1 --> Berechtigung ber2[38] ist nicht aktiv");
                        logbuchEingabe.NeuerEintag("Berechtigung Seite 2 --> Veranstaltung Editieren - Einsatzbericht bearbeiten (id = S2.BR38) ist nicht aktiv");
                        JOptionPane.showMessageDialog(null, Konstante.KEINE_BERECHTIGUNG_EINSATZ_EDITIEREN, "Warnung", 2);
                    }
                    VeranstaltungEditierenAO.this.repaint();
                    VeranstaltungEditierenAO.this.validate();
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void setzeAuswahllisten() {
        this.panelBsw = new JPanel(new GridLayout(6, 2));
        this.getContentPane().add("Center", this.panelBsw);
        this.panelVeranstaltung = new JPanel(new GridLayout(5, 2));
        this.getContentPane().add("Center", this.panelVeranstaltung);
        this.panelEinsatz = new JPanel(new GridLayout(9, 2));
        this.getContentPane().add("Center", this.panelEinsatz);
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
        try {
            int anzahlFahrzeuge = tabFahrzeuge.countOhneAnhaenger();
            if (anzahlFahrzeuge <= 3) {
                logging.logInfo((Object)"Starte VeranstaltungEditierenAO mit der Gr\u00f6\u00dfe: 690x560");
                this.setSize(760, 600);
            } else if (anzahlFahrzeuge <= 4) {
                logging.logInfo((Object)"Starte VeranstaltungEditierenAO mit der Gr\u00f6\u00dfe: 890x560");
                this.setSize(960, 600);
            } else if (anzahlFahrzeuge <= 6) {
                logging.logInfo((Object)"Starte VeranstaltungEditierenAO mit der Gr\u00f6\u00dfe: 690x660");
                this.setSize(760, 700);
            } else if (anzahlFahrzeuge <= 8) {
                logging.logInfo((Object)"Starte VeranstaltungEditierenAO mit der Gr\u00f6\u00dfe: 890x560");
                this.setSize(960, 700);
            } else if (anzahlFahrzeuge <= 9) {
                logging.logInfo((Object)"Starte VeranstaltungEditierenAO mit der Gr\u00f6\u00dfe: 890x760");
                this.setSize(960, 768);
            } else if (anzahlFahrzeuge <= 12) {
                logging.logInfo((Object)"Starte VeranstaltungEditierenAO mit der Gr\u00f6\u00dfe: 890x760");
                this.setSize(960, 768);
            } else {
                logging.logInfo((Object)"Starte VeranstaltungEditierenAO mit der Gr\u00f6\u00dfe: 1280x760");
                this.setSize(1280, 768);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.setTitle("FeuerwehrManagementSystem - Veranstaltung editieren");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelHead = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panelHead);
        this.panelHead.add(this.veranstaltung_label);
        this.panelHead.add(this.veranstaltungComboBox);
        this.panelHead.add(this.veranstaltungID_label);
        this.panelHead.add(this.veranstaltungID);
        this.add(this.dummy3);
        this.veranstaltungID.setEditable(false);
        this.einsatzNummerIntern.setEnabled(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonEinsatzberichtNeuErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.SOLL_DER_EINSTZBERICHT_NEU_ERSTELLT_WERDEN, "Frage", 0);
                if (msg == 0) {
                    TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    int erstFahrzeug = -1;
                    int zweitFahrzeug = -1;
                    int drittFahrzeug = -1;
                    int viertFahrzeug = -1;
                    try {
                        String dateinameNeuXML;
                        int fahrzeugCount = tabFahrzeug.countWithoutAnhaenger();
                        int x = 0;
                        while (x < fahrzeugCount) {
                            if (VeranstaltungEditierenAO.this.jCheckboxArray[x].isSelected() && erstFahrzeug == -1) {
                                erstFahrzeug = x;
                                logging.logInfo((Object)("Fahrzeug 1 im Einsatz - fID = " + erstFahrzeug));
                            } else if (VeranstaltungEditierenAO.this.jCheckboxArray[x].isSelected() && zweitFahrzeug == -1 && erstFahrzeug != -1) {
                                zweitFahrzeug = x;
                                logging.logInfo((Object)("Fahrzeug 2 im Einsatz - fID = " + zweitFahrzeug));
                            } else if (VeranstaltungEditierenAO.this.jCheckboxArray[x].isSelected() && zweitFahrzeug != -1 && erstFahrzeug != -1 && drittFahrzeug == -1) {
                                drittFahrzeug = x;
                                logging.logInfo((Object)("Fahrzeug 3 im Einsatz - fID = " + drittFahrzeug));
                            } else if (VeranstaltungEditierenAO.this.jCheckboxArray[x].isSelected() && zweitFahrzeug != -1 && erstFahrzeug != -1 && drittFahrzeug != -1 && viertFahrzeug == -1) {
                                viertFahrzeug = x;
                                logging.logInfo((Object)("Fahrzeug 4 im Einsatz - fID = " + viertFahrzeug));
                                break;
                            }
                            ++x;
                        }
                        int einsatzleiterID = tabMitglied.getIdByGuiString(einsatzleiter.getSelectedItem().toString());
                        int vID = Integer.parseInt(VeranstaltungEditierenAO.this.veranstaltungID.getText());
                        int wochentagID = TimeCalculation.wochentagErmitteln(VeranstaltungEditierenAO.this.datum.getText());
                        String wochentagName = TimeCalculation.wochentagNameByWochentagID(wochentagID);
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + VeranstaltungEditierenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + VeranstaltungEditierenAO.this.einsatzNummerIntern.getText() + "_ID_" + VeranstaltungEditierenAO.this.einsatzNummerOffiziell.getText() + ".xml";
                        String[] ist = new String[]{"12000", "Jahr1", "WTag1", "01.01.2000", "Wert3", "Wert4", "Wert5", "Wert6", "23:59", "Fahr1", "Fahr2", "Fahr3", "Fahr4", "Funk1", "Funk2", "Funk3", "Funk4", "00:00", "00:01", "00:02", "00:03", "00:04", "00:05", "00:06", "00:07", "00:08", "00:09", "00:10", "00:11", "00:12", "00:13", "00:14", "00:15", "ELeit", "ELeBF", "CreDa"};
                        String bericht_name2 = "";
                        String bericht_name3 = "";
                        String bericht_name4 = "";
                        String bericht_funkname2 = "";
                        String bericht_funkname3 = "";
                        String bericht_funkname4 = "";
                        String bericht_ausruecken2 = "";
                        String bericht_ausruecken3 = "";
                        String bericht_ausruecken4 = "";
                        String bericht_eingetroffen2 = "";
                        String bericht_eingetroffen3 = "";
                        String bericht_eingetroffen4 = "";
                        String bericht_einsatzEnde2 = "";
                        String bericht_einsatzEnde3 = "";
                        String bericht_einsatzEnde4 = "";
                        String bericht_EinsatzZeit2 = "";
                        String bericht_EinsatzZeit3 = "";
                        String bericht_EinsatzZeit4 = "";
                        if (zweitFahrzeug != -1) {
                            bericht_name2 = VeranstaltungEditierenAO.this.fahrzeug_label[zweitFahrzeug].getText();
                            bericht_funkname2 = VeranstaltungEditierenAO.this.fahrzeug_label[zweitFahrzeug].getName();
                            bericht_ausruecken2 = VeranstaltungEditierenAO.this.zeitAusgerueckt[zweitFahrzeug].getText();
                            bericht_eingetroffen2 = VeranstaltungEditierenAO.this.zeitEingetroffen[zweitFahrzeug].getText();
                            bericht_einsatzEnde2 = VeranstaltungEditierenAO.this.zeitEingerueckt[zweitFahrzeug].getText();
                            bericht_EinsatzZeit2 = TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeitAlarm.getText(), VeranstaltungEditierenAO.this.zeitEingerueckt[zweitFahrzeug].getText()));
                        }
                        if (drittFahrzeug != -1) {
                            bericht_name3 = VeranstaltungEditierenAO.this.fahrzeug_label[drittFahrzeug].getText();
                            bericht_funkname3 = VeranstaltungEditierenAO.this.fahrzeug_label[drittFahrzeug].getName();
                            bericht_ausruecken3 = VeranstaltungEditierenAO.this.zeitAusgerueckt[drittFahrzeug].getText();
                            bericht_eingetroffen3 = VeranstaltungEditierenAO.this.zeitEingetroffen[drittFahrzeug].getText();
                            bericht_einsatzEnde3 = VeranstaltungEditierenAO.this.zeitEingerueckt[drittFahrzeug].getText();
                            bericht_EinsatzZeit3 = TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeitAlarm.getText(), VeranstaltungEditierenAO.this.zeitEingerueckt[drittFahrzeug].getText()));
                        }
                        if (viertFahrzeug != -1) {
                            bericht_name4 = VeranstaltungEditierenAO.this.fahrzeug_label[viertFahrzeug].getText();
                            bericht_funkname4 = VeranstaltungEditierenAO.this.fahrzeug_label[viertFahrzeug].getName();
                            bericht_ausruecken4 = VeranstaltungEditierenAO.this.zeitAusgerueckt[viertFahrzeug].getText();
                            bericht_eingetroffen4 = VeranstaltungEditierenAO.this.zeitEingetroffen[viertFahrzeug].getText();
                            bericht_einsatzEnde4 = VeranstaltungEditierenAO.this.zeitEingerueckt[viertFahrzeug].getText();
                            bericht_EinsatzZeit4 = TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeitAlarm.getText(), VeranstaltungEditierenAO.this.zeitEingerueckt[viertFahrzeug].getText()));
                        }
                        String[] zu = new String[]{VeranstaltungEditierenAO.this.einsatzNummerOffiziell.getText(), VeranstaltungEditierenAO.this.datum.getText().substring(6, 10), wochentagName, VeranstaltungEditierenAO.this.datum.getText(), Utils.checkTextAndRemoveIllegalSigns(runApplication.EINSTELLUNGEN.get("Stadt")), Utils.checkTextAndRemoveIllegalSigns(VeranstaltungEditierenAO.this.stadtteil.getSelectedItem().toString()), Utils.checkTextAndRemoveIllegalSigns(VeranstaltungEditierenAO.this.strasse.getSelectedItem().toString()), Utils.checkTextAndRemoveIllegalSigns(Box_Stichwort.getSelectedItem().toString()), VeranstaltungEditierenAO.this.zeitAlarm.getText(), VeranstaltungEditierenAO.this.fahrzeug_label[erstFahrzeug].getText(), bericht_name2, bericht_name3, bericht_name4, VeranstaltungEditierenAO.this.fahrzeug_label[erstFahrzeug].getName(), bericht_funkname2, bericht_funkname3, bericht_funkname4, VeranstaltungEditierenAO.this.zeitAusgerueckt[erstFahrzeug].getText(), bericht_ausruecken2, bericht_ausruecken3, bericht_ausruecken4, VeranstaltungEditierenAO.this.zeitEingetroffen[erstFahrzeug].getText(), bericht_eingetroffen2, bericht_eingetroffen3, bericht_eingetroffen4, VeranstaltungEditierenAO.this.zeitEingerueckt[erstFahrzeug].getText(), bericht_einsatzEnde2, bericht_einsatzEnde3, bericht_einsatzEnde4, TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeitAlarm.getText(), VeranstaltungEditierenAO.this.zeitEingerueckt[erstFahrzeug].getText())), bericht_EinsatzZeit2, bericht_EinsatzZeit3, bericht_EinsatzZeit4, Utils.checkTextAndRemoveIllegalSigns(tabMitglied.getEinsatzleiter(einsatzleiterID)), Utils.checkTextAndRemoveIllegalSigns(VeranstaltungEditierenAO.this.einsatzleiterBF.getSelectedItem().toString()), SbcUtils.timeStamp((String)"dd.MM.yyyy")};
                        XML.createEinsatzBericht(ist, zu, dateiname, runApplication.EINSTELLUNGEN.get("EinsatzBericht"));
                        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtFahrzeugbelegungHinzuf\u00fcgen").equals("1") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle")) {
                            int i;
                            TabelleFahrzeugeinteilung tabEinteilung = new TabelleFahrzeugeinteilung();
                            dateinameNeuXML = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + VeranstaltungEditierenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + VeranstaltungEditierenAO.this.einsatzNummerIntern.getText() + "_ID_" + VeranstaltungEditierenAO.this.einsatzNummerOffiziell.getText() + "_Neu.xml";
                            new File(dateiname).renameTo(new File(dateinameNeuXML));
                            logging.logInfo((Object)"Erstelle neues Templete f\u00fcr Fahrzeugbelegung...!");
                            String[] istFahrzeugbelegung = new String[]{"F1PO0", "F1PO1", "F1PO2", "F1PO3", "F1PO4", "F1PO5", "F1PO6", "F1PO7", "F1PO8", "F2PO0", "F2PO1", "F2PO2", "F2PO3", "F2PO4", "F2PO5", "F2PO6", "F2PO7", "F2PO8", "F3PO0", "F3PO1", "F3PO2", "F3PO3", "F3PO4", "F3PO5", "F3PO6", "F3PO7", "F3PO8", "F4PO0", "F4PO1", "F4PO2", "F4PO3", "F4PO4", "F4PO5", "F4PO6", "F4PO7", "F4PO8", "GH000", "GH001", "GH002", "GH003", "GH004", "GH005", "GH006", "GH007", "GH008", "GH009"};
                            String fahr1pos0 = "";
                            String fahr1pos1 = "";
                            String fahr1pos2 = "";
                            String fahr1pos3 = "";
                            String fahr1pos4 = "";
                            String fahr1pos5 = "";
                            String fahr1pos6 = "";
                            String fahr1pos7 = "";
                            String fahr1pos8 = "";
                            String fahr2pos0 = "";
                            String fahr2pos1 = "";
                            String fahr2pos2 = "";
                            String fahr2pos3 = "";
                            String fahr2pos4 = "";
                            String fahr2pos5 = "";
                            String fahr2pos6 = "";
                            String fahr2pos7 = "";
                            String fahr2pos8 = "";
                            String fahr3pos0 = "";
                            String fahr3pos1 = "";
                            String fahr3pos2 = "";
                            String fahr3pos3 = "";
                            String fahr3pos4 = "";
                            String fahr3pos5 = "";
                            String fahr3pos6 = "";
                            String fahr3pos7 = "";
                            String fahr3pos8 = "";
                            String fahr4pos0 = "";
                            String fahr4pos1 = "";
                            String fahr4pos2 = "";
                            String fahr4pos3 = "";
                            String fahr4pos4 = "";
                            String fahr4pos5 = "";
                            String fahr4pos6 = "";
                            String fahr4pos7 = "";
                            String fahr4pos8 = "";
                            String gh0 = "";
                            String gh1 = "";
                            String gh2 = "";
                            String gh3 = "";
                            String gh4 = "";
                            String gh5 = "";
                            String gh6 = "";
                            String gh7 = "";
                            String gh8 = "";
                            String gh9 = "";
                            String fahrzeug1ID = Integer.toString(tabFahrzeug.getFahrzeugID(VeranstaltungEditierenAO.this.fahrzeug_label[erstFahrzeug].getText()));
                            String[] fahrzeug1 = Utils.listToArray(tabEinteilung.getPositionListe(fahrzeug1ID, vID));
                            int fahrzeug1IsTrupp = tabFahrzeug.getTrupp(Integer.parseInt(fahrzeug1ID));
                            int i2 = 0;
                            while (i2 < fahrzeug1.length) {
                                if (fahrzeug1IsTrupp == 0) {
                                    if (i2 == 0) {
                                        fahr1pos0 = fahrzeug1[i2];
                                    }
                                    if (i2 == 1) {
                                        fahr1pos1 = fahrzeug1[i2];
                                    }
                                    if (i2 == 2) {
                                        fahr1pos2 = fahrzeug1[i2];
                                    }
                                    if (i2 == 3) {
                                        fahr1pos3 = fahrzeug1[i2];
                                    }
                                    if (i2 == 4) {
                                        fahr1pos4 = fahrzeug1[i2];
                                    }
                                    if (i2 == 5) {
                                        fahr1pos5 = fahrzeug1[i2];
                                    }
                                    if (i2 == 6) {
                                        fahr1pos6 = fahrzeug1[i2];
                                    }
                                    if (i2 == 7) {
                                        fahr1pos7 = fahrzeug1[i2];
                                    }
                                    if (i2 == 8) {
                                        fahr1pos8 = fahrzeug1[i2];
                                    }
                                } else if (fahrzeug1IsTrupp == 1) {
                                    if (i2 == 0) {
                                        fahr1pos2 = fahrzeug1[i2];
                                    }
                                    if (i2 == 1) {
                                        fahr1pos1 = fahrzeug1[i2];
                                    }
                                    if (i2 == 2) {
                                        fahr1pos3 = fahrzeug1[i2];
                                    }
                                }
                                ++i2;
                            }
                            if (zweitFahrzeug != -1) {
                                String fahrzeug2ID = Integer.toString(tabFahrzeug.getFahrzeugID(VeranstaltungEditierenAO.this.fahrzeug_label[zweitFahrzeug].getText()));
                                String[] fahrzeug2 = Utils.listToArray(tabEinteilung.getPositionListe(fahrzeug2ID, vID));
                                int fahrzeug2IsTrupp = tabFahrzeug.getTrupp(Integer.parseInt(fahrzeug2ID));
                                if (fahrzeug2.length != 0) {
                                    i = 0;
                                    while (i < fahrzeug2.length) {
                                        if (fahrzeug2IsTrupp == 0) {
                                            if (i == 0) {
                                                fahr2pos0 = fahrzeug2[i];
                                            }
                                            if (i == 1) {
                                                fahr2pos1 = fahrzeug2[i];
                                            }
                                            if (i == 2) {
                                                fahr2pos2 = fahrzeug2[i];
                                            }
                                            if (i == 3) {
                                                fahr2pos3 = fahrzeug2[i];
                                            }
                                            if (i == 4) {
                                                fahr2pos4 = fahrzeug2[i];
                                            }
                                            if (i == 5) {
                                                fahr2pos5 = fahrzeug2[i];
                                            }
                                            if (i == 6) {
                                                fahr2pos6 = fahrzeug2[i];
                                            }
                                            if (i == 7) {
                                                fahr2pos7 = fahrzeug2[i];
                                            }
                                            if (i == 8) {
                                                fahr2pos8 = fahrzeug2[i];
                                            }
                                        } else if (fahrzeug2IsTrupp == 1) {
                                            if (i == 0) {
                                                fahr2pos2 = fahrzeug2[i];
                                            }
                                            if (i == 1) {
                                                fahr2pos1 = fahrzeug2[i];
                                            }
                                            if (i == 2) {
                                                fahr2pos3 = fahrzeug2[i];
                                            }
                                        }
                                        ++i;
                                    }
                                }
                            }
                            if (drittFahrzeug != -1) {
                                String fahrzeug3ID = Integer.toString(tabFahrzeug.getFahrzeugID(VeranstaltungEditierenAO.this.fahrzeug_label[drittFahrzeug].getText()));
                                String[] fahrzeug3 = Utils.listToArray(tabEinteilung.getPositionListe(fahrzeug3ID, vID));
                                int fahrzeug3IsTrupp = tabFahrzeug.getTrupp(Integer.parseInt(fahrzeug3ID));
                                if (fahrzeug3.length != 0) {
                                    i = 0;
                                    while (i < fahrzeug3.length) {
                                        if (fahrzeug3IsTrupp == 0) {
                                            if (i == 0) {
                                                fahr3pos0 = fahrzeug3[i];
                                            }
                                            if (i == 1) {
                                                fahr3pos1 = fahrzeug3[i];
                                            }
                                            if (i == 2) {
                                                fahr3pos2 = fahrzeug3[i];
                                            }
                                            if (i == 3) {
                                                fahr3pos3 = fahrzeug3[i];
                                            }
                                            if (i == 4) {
                                                fahr3pos4 = fahrzeug3[i];
                                            }
                                            if (i == 5) {
                                                fahr3pos5 = fahrzeug3[i];
                                            }
                                            if (i == 6) {
                                                fahr3pos6 = fahrzeug3[i];
                                            }
                                            if (i == 7) {
                                                fahr3pos7 = fahrzeug3[i];
                                            }
                                            if (i == 8) {
                                                fahr3pos8 = fahrzeug3[i];
                                            }
                                        } else if (fahrzeug3IsTrupp == 1) {
                                            if (i == 0) {
                                                fahr3pos2 = fahrzeug3[i];
                                            }
                                            if (i == 1) {
                                                fahr3pos1 = fahrzeug3[i];
                                            }
                                            if (i == 2) {
                                                fahr3pos3 = fahrzeug3[i];
                                            }
                                        }
                                        ++i;
                                    }
                                }
                            }
                            if (viertFahrzeug != -1) {
                                String fahrzeug4ID = Integer.toString(tabFahrzeug.getFahrzeugID(VeranstaltungEditierenAO.this.fahrzeug_label[viertFahrzeug].getText()));
                                String[] fahrzeug4 = Utils.listToArray(tabEinteilung.getPositionListe(fahrzeug4ID, vID));
                                int fahrzeug4IsTrupp = tabFahrzeug.getTrupp(Integer.parseInt(fahrzeug4ID));
                                if (fahrzeug4.length != 0) {
                                    i = 0;
                                    while (i < fahrzeug4.length) {
                                        if (fahrzeug4IsTrupp == 0) {
                                            if (i == 0) {
                                                fahr4pos0 = fahrzeug4[i];
                                            }
                                            if (i == 1) {
                                                fahr4pos1 = fahrzeug4[i];
                                            }
                                            if (i == 2) {
                                                fahr4pos2 = fahrzeug4[i];
                                            }
                                            if (i == 3) {
                                                fahr4pos3 = fahrzeug4[i];
                                            }
                                            if (i == 4) {
                                                fahr4pos4 = fahrzeug4[i];
                                            }
                                            if (i == 5) {
                                                fahr4pos5 = fahrzeug4[i];
                                            }
                                            if (i == 6) {
                                                fahr4pos6 = fahrzeug4[i];
                                            }
                                            if (i == 7) {
                                                fahr4pos7 = fahrzeug4[i];
                                            }
                                            if (i == 8) {
                                                fahr4pos8 = fahrzeug4[i];
                                            }
                                        } else if (fahrzeug4IsTrupp == 1) {
                                            if (i == 0) {
                                                fahr4pos2 = fahrzeug4[i];
                                            }
                                            if (i == 1) {
                                                fahr4pos1 = fahrzeug4[i];
                                            }
                                            if (i == 2) {
                                                fahr4pos3 = fahrzeug4[i];
                                            }
                                        }
                                        ++i;
                                    }
                                }
                            }
                            String[] kamerandenImEinsatz = Utils.listToArray(tabEinteilung.getEingeteilteKameraden(vID));
                            String[] kameradenImGH = Utils.listToArray(new TabelleAnwesenheit().getNichtInFahrzeugeinteilung(vID, kamerandenImEinsatz));
                            try {
                                gh0 = kameradenImGH[0];
                                gh1 = kameradenImGH[1];
                                gh2 = kameradenImGH[2];
                                gh3 = kameradenImGH[3];
                                gh4 = kameradenImGH[4];
                                gh5 = kameradenImGH[5];
                                gh6 = kameradenImGH[6];
                                gh7 = kameradenImGH[7];
                                gh8 = kameradenImGH[8];
                                gh9 = kameradenImGH[9];
                            }
                            catch (ArrayIndexOutOfBoundsException e) {
                                logging.logInfo((Object)"Liste der Kameraden im GH ist zu ende!");
                            }
                            String[] zuFahrzeugbelegung = new String[]{Utils.checkTextAndRemoveIllegalSigns(fahr1pos0), Utils.checkTextAndRemoveIllegalSigns(fahr1pos1), Utils.checkTextAndRemoveIllegalSigns(fahr1pos2), Utils.checkTextAndRemoveIllegalSigns(fahr1pos3), Utils.checkTextAndRemoveIllegalSigns(fahr1pos4), Utils.checkTextAndRemoveIllegalSigns(fahr1pos5), Utils.checkTextAndRemoveIllegalSigns(fahr1pos6), Utils.checkTextAndRemoveIllegalSigns(fahr1pos7), Utils.checkTextAndRemoveIllegalSigns(fahr1pos8), Utils.checkTextAndRemoveIllegalSigns(fahr2pos0), Utils.checkTextAndRemoveIllegalSigns(fahr2pos1), Utils.checkTextAndRemoveIllegalSigns(fahr2pos2), Utils.checkTextAndRemoveIllegalSigns(fahr2pos3), Utils.checkTextAndRemoveIllegalSigns(fahr2pos4), Utils.checkTextAndRemoveIllegalSigns(fahr2pos5), Utils.checkTextAndRemoveIllegalSigns(fahr2pos6), Utils.checkTextAndRemoveIllegalSigns(fahr2pos7), Utils.checkTextAndRemoveIllegalSigns(fahr2pos8), Utils.checkTextAndRemoveIllegalSigns(fahr3pos0), Utils.checkTextAndRemoveIllegalSigns(fahr3pos1), Utils.checkTextAndRemoveIllegalSigns(fahr3pos2), Utils.checkTextAndRemoveIllegalSigns(fahr3pos3), Utils.checkTextAndRemoveIllegalSigns(fahr3pos4), Utils.checkTextAndRemoveIllegalSigns(fahr3pos5), Utils.checkTextAndRemoveIllegalSigns(fahr3pos6), Utils.checkTextAndRemoveIllegalSigns(fahr3pos7), Utils.checkTextAndRemoveIllegalSigns(fahr3pos8), Utils.checkTextAndRemoveIllegalSigns(fahr4pos0), Utils.checkTextAndRemoveIllegalSigns(fahr4pos1), Utils.checkTextAndRemoveIllegalSigns(fahr4pos2), Utils.checkTextAndRemoveIllegalSigns(fahr4pos3), Utils.checkTextAndRemoveIllegalSigns(fahr4pos4), Utils.checkTextAndRemoveIllegalSigns(fahr4pos5), Utils.checkTextAndRemoveIllegalSigns(fahr4pos6), Utils.checkTextAndRemoveIllegalSigns(fahr4pos7), Utils.checkTextAndRemoveIllegalSigns(fahr4pos8), Utils.checkTextAndRemoveIllegalSigns(gh0), Utils.checkTextAndRemoveIllegalSigns(gh1), Utils.checkTextAndRemoveIllegalSigns(gh2), Utils.checkTextAndRemoveIllegalSigns(gh3), Utils.checkTextAndRemoveIllegalSigns(gh4), Utils.checkTextAndRemoveIllegalSigns(gh5), Utils.checkTextAndRemoveIllegalSigns(gh6), Utils.checkTextAndRemoveIllegalSigns(gh7), Utils.checkTextAndRemoveIllegalSigns(gh8), Utils.checkTextAndRemoveIllegalSigns(gh9)};
                            XML.createEinsatzBericht(istFahrzeugbelegung, zuFahrzeugbelegung, dateiname, dateinameNeuXML);
                            new File(dateinameNeuXML).delete();
                            logging.logInfo((Object)"L\u00f6sche neues Template f\u00fcr Fahrzeugbelegung...!");
                        }
                        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtAtemschutzpassHinzuf\u00fcgen").equals("1") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle")) {
                            TabelleAtemschutzpass tabAtemschutz = new TabelleAtemschutzpass();
                            dateinameNeuXML = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + VeranstaltungEditierenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + VeranstaltungEditierenAO.this.einsatzNummerIntern.getText() + "_ID_" + VeranstaltungEditierenAO.this.einsatzNummerOffiziell.getText() + "_Neu.xml";
                            new File(dateiname).renameTo(new File(dateinameNeuXML));
                            logging.logInfo((Object)"Erstelle neues Templete f\u00fcr Atemschutzpass...!");
                            String[] istAGT = new String[]{"TR11N", "TR11T", "TR11Z", "TR12N", "TR12T", "TR12Z", "TR21N", "TR21T", "TR21Z", "TR22N", "TR22T", "TR22Z", "TR31N", "TR31T", "TR31Z", "TR32N", "TR32T", "TR32Z", "TR41N", "TR41T", "TR41Z", "TR42N", "TR42T", "TR42Z"};
                            String tuppF\u00fchrer1Name = "";
                            String tuppF\u00fchrer1Zeit = "";
                            String tuppF\u00fchrer1Type = "";
                            String tuppMann1Name = "";
                            String tuppMann1Zeit = "";
                            String tuppMann1Type = "";
                            String tuppF\u00fchrer2Name = "";
                            String tuppF\u00fchrer2Zeit = "";
                            String tuppF\u00fchrer2Type = "";
                            String tuppMann2Name = "";
                            String tuppMann2Zeit = "";
                            String tuppMann2Type = "";
                            String tuppF\u00fchrer3Name = "";
                            String tuppF\u00fchrer3Zeit = "";
                            String tuppF\u00fchrer3Type = "";
                            String tuppMann3Name = "";
                            String tuppMann3Zeit = "";
                            String tuppMann3Type = "";
                            String tuppF\u00fchrer4Name = "";
                            String tuppF\u00fchrer4Zeit = "";
                            String tuppF\u00fchrer4Type = "";
                            String tuppMann4Name = "";
                            String tuppMann4Zeit = "";
                            String tuppMann4Type = "";
                            int tf1 = -1;
                            int tm1 = -1;
                            int tf2 = -1;
                            int tm2 = -1;
                            int tf3 = -1;
                            int tm3 = -1;
                            int tf4 = -1;
                            int tm4 = -1;
                            int[] verfuegbareMitglieder = Utils.listToIntArray(tabAtemschutz.getMitgliederIDsByVeransatltung(vID));
                            int i = 0;
                            while (i < verfuegbareMitglieder.length) {
                                HashMap<String, String> map = tabAtemschutz.getData(vID, verfuegbareMitglieder[i]);
                                if (tf1 == -1 && map.get("truppZuordnung").equals("1")) {
                                    tf1 = i;
                                    tuppF\u00fchrer1Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppF\u00fchrer1Type = map.get("einsatzart");
                                    tuppF\u00fchrer1Zeit = map.get("zeit");
                                } else if (tm1 == -1 && map.get("truppZuordnung").equals("1")) {
                                    tm1 = i;
                                    tuppMann1Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppMann1Type = map.get("einsatzart");
                                    tuppMann1Zeit = map.get("zeit");
                                } else if (tf2 == -1 && map.get("truppZuordnung").equals("2")) {
                                    tf2 = i;
                                    tuppF\u00fchrer2Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppF\u00fchrer2Type = map.get("einsatzart");
                                    tuppF\u00fchrer2Zeit = map.get("zeit");
                                } else if (tm2 == -1 && map.get("truppZuordnung").equals("2")) {
                                    tm2 = i;
                                    tuppMann2Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppMann2Type = map.get("einsatzart");
                                    tuppMann2Zeit = map.get("zeit");
                                } else if (tf3 == -1 && map.get("truppZuordnung").equals("3")) {
                                    tf3 = i;
                                    tuppF\u00fchrer3Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppF\u00fchrer3Type = map.get("einsatzart");
                                    tuppF\u00fchrer3Zeit = map.get("zeit");
                                } else if (tm3 == -1 && map.get("truppZuordnung").equals("3")) {
                                    tm3 = i;
                                    tuppMann3Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppMann3Type = map.get("einsatzart");
                                    tuppMann3Zeit = map.get("zeit");
                                } else if (tf4 == -1 && map.get("truppZuordnung").equals("4")) {
                                    tf4 = i;
                                    tuppF\u00fchrer4Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppF\u00fchrer4Type = map.get("einsatzart");
                                    tuppF\u00fchrer4Zeit = map.get("zeit");
                                } else if (tm4 == -1 && map.get("truppZuordnung").equals("4")) {
                                    tm4 = i;
                                    tuppMann4Name = Utils.checkTextAndRemoveIllegalSigns(map.get("name"));
                                    tuppMann4Type = map.get("einsatzart");
                                    tuppMann4Zeit = map.get("zeit");
                                }
                                ++i;
                            }
                            String[] zuAGT = new String[]{tuppF\u00fchrer1Name, tuppF\u00fchrer1Type, tuppF\u00fchrer1Zeit, tuppMann1Name, tuppMann1Type, tuppMann1Zeit, tuppF\u00fchrer2Name, tuppF\u00fchrer2Type, tuppF\u00fchrer2Zeit, tuppMann2Name, tuppMann2Type, tuppMann2Zeit, tuppF\u00fchrer3Name, tuppF\u00fchrer3Type, tuppF\u00fchrer3Zeit, tuppMann3Name, tuppMann3Type, tuppMann3Zeit, tuppF\u00fchrer4Name, tuppF\u00fchrer4Type, tuppF\u00fchrer4Zeit, tuppMann4Name, tuppMann4Type, tuppMann4Zeit};
                            XML.createEinsatzBericht(istAGT, zuAGT, dateiname, dateinameNeuXML);
                            new File(dateinameNeuXML).delete();
                            logging.logInfo((Object)"L\u00f6sche neues Template f\u00fcr Fahrzeugbelegung...!");
                        }
                        logging.logInfo((Object)"Erstelle DOC-Datei aus dem Einsatzbericht");
                        File docFile = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + VeranstaltungEditierenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + VeranstaltungEditierenAO.this.einsatzNummerIntern.getText() + "_ID_" + VeranstaltungEditierenAO.this.einsatzNummerOffiziell.getText() + ".doc");
                        logging.logInfo((Object)"Kopiere Alten Einsatzbericht in den Papierkorb...");
                        Path input = Paths.get(docFile.getAbsolutePath(), new String[0]);
                        Path output = Paths.get(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Papierkorb/" + docFile.getName(), new String[0]);
                        Files.copy(input, output, StandardCopyOption.REPLACE_EXISTING);
                        logging.logInfo((Object)"Alten Einsatz Bericht erfolgreich in den Papierkorb gelegt...");
                        docFile.delete();
                        logging.logInfo((Object)"Alten Einsatzbericht gel\u00f6scht...");
                        new File(dateiname).renameTo(docFile);
                        Utils.dateiKatalogisieren(Utils.removeBackSlashFromString(docFile.getAbsolutePath()));
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        Desktop.getDesktop().open(docFile);
                    }
                    catch (IOException | SQLException e) {
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                TabelleStichwort tabStichwort = new TabelleStichwort();
                TabelleBrandsicherheitswache tabBsw = new TabelleBrandsicherheitswache();
                TabelleStatistikbsw tabStatistikBSW = new TabelleStatistikbsw();
                TabelleStatistikEinsatz tabStatistikEinsatz = new TabelleStatistikEinsatz();
                TabelleStatistikSonstigeVeranstaltung tabStatistikSonstige = new TabelleStatistikSonstigeVeranstaltung();
                Veranstaltung veranstaltung = new Veranstaltung();
                Einsatz einsatz = new Einsatz();
                TabelleEinsatz_organisationen tabEinsatzOrganisation = new TabelleEinsatz_organisationen();
                Brandsicherheitswachen bsw = new Brandsicherheitswachen();
                StatistikSonstigeVeranstaltung statistikSonstige = new StatistikSonstigeVeranstaltung();
                StatistikBSW statistikBsw = new StatistikBSW();
                StatistikEinsatz statistikEinsatz = new StatistikEinsatz();
                Einsatz_zeiten zeiten = new Einsatz_zeiten();
                Anwesenheit anwesenheit = new Anwesenheit();
                try {
                    String nameVeranstaltung;
                    int vID = Integer.parseInt(VeranstaltungEditierenAO.this.veranstaltungID.getText());
                    int zf = tabAnwesenheit.getZFCountMitVeranstaltungsID(vID);
                    int gf = tabAnwesenheit.getGFCountMitVeranstaltungsID(vID) - zf;
                    int fm = tabAnwesenheit.getFMCountMitVeranstaltungsID(vID) - zf - gf;
                    boolean errorCode = false;
                    if (VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("Einsatz")) {
                        nameVeranstaltung = "Einsatz (" + Box_Stichwort.getSelectedItem() + " " + VeranstaltungEditierenAO.this.datum.getText() + ", " + VeranstaltungEditierenAO.this.zeitAlarm.getText() + ")";
                        int zaehler = tabVeranstaltung.getCount(1, VeranstaltungEditierenAO.this.datum.getText(), VeranstaltungEditierenAO.this.zeitAlarm.getText());
                        if (zaehler >= 2) {
                            nameVeranstaltung = "Einsatz" + zaehler + " (" + Box_Stichwort.getSelectedItem() + " " + VeranstaltungEditierenAO.this.datum.getText() + ", " + VeranstaltungEditierenAO.this.zeitAlarm.getText() + ")";
                        }
                        int fahrzeugCount = tabFahrzeug.countWithoutAnhaenger();
                        int erstFahrzeug = -1;
                        int x = 0;
                        while (x < tabFahrzeug.countWithoutAnhaenger()) {
                            if (VeranstaltungEditierenAO.this.jCheckboxArray[x].isSelected()) {
                                erstFahrzeug = x;
                                logging.logInfo((Object)("curr ID = " + erstFahrzeug));
                                break;
                            }
                            ++x;
                        }
                        if (VeranstaltungEditierenAO.this.zeitAlarm.getText().length() == 4) {
                            String eingabe = VeranstaltungEditierenAO.this.zeitAlarm.getText();
                            VeranstaltungEditierenAO.this.zeitAlarm.setText("0" + eingabe);
                        }
                        x = 0;
                        while (x < fahrzeugCount) {
                            String eingabe;
                            if (VeranstaltungEditierenAO.this.zeitAusgerueckt[x].getText().length() == 4) {
                                eingabe = VeranstaltungEditierenAO.this.zeitAusgerueckt[x].getText();
                                VeranstaltungEditierenAO.this.zeitAusgerueckt[x].setText("0" + eingabe);
                            }
                            if (VeranstaltungEditierenAO.this.zeitEingerueckt[x].getText().length() == 4) {
                                eingabe = VeranstaltungEditierenAO.this.zeitEingerueckt[x].getText();
                                VeranstaltungEditierenAO.this.zeitEingerueckt[x].setText("0" + eingabe);
                            }
                            if (VeranstaltungEditierenAO.this.zeitEingetroffen[x].getText().length() == 4) {
                                eingabe = VeranstaltungEditierenAO.this.zeitEingetroffen[x].getText();
                                VeranstaltungEditierenAO.this.zeitEingetroffen[x].setText("0" + eingabe);
                            }
                            ++x;
                        }
                        if (!TimeCalculation.checkDateFormat(VeranstaltungEditierenAO.this.datum.getText())) {
                            VeranstaltungEditierenAO.this.datum.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (!TimeCalculation.checkTimeFormat(VeranstaltungEditierenAO.this.zeitAlarm.getText())) {
                            VeranstaltungEditierenAO.this.zeitAlarm.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (Box_Stichwort.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_STICHTWORT_WAEHLEN, "Warnung", 2);
                            errorCode = true;
                        } else if (!VeranstaltungEditierenAO.this.checkInputs()) {
                            JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (erstFahrzeug == -1) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_FAHRZEUG_WAEHLEN, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (einsatzleiter.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Fehlermeldung", 0);
                            einsatzleiter.setBackground(Color.red);
                            errorCode = true;
                        } else if (VeranstaltungEditierenAO.this.strasse.getSelectedItem().equals("")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_STRASSE_WAEHLEN, "Warnung", 2);
                            VeranstaltungEditierenAO.this.strasse.setBackground(Color.red);
                            errorCode = true;
                        } else if (VeranstaltungEditierenAO.this.zeitAlarm.getText().equals(VeranstaltungEditierenAO.this.zeitAusgerueckt[0].getText())) {
                            JOptionPane.showMessageDialog(null, Konstante.ZEITEN_GLEICH, "Fehlermeldung", 0);
                            VeranstaltungEditierenAO.this.zeitAusgerueckt[0].setBackground(Color.red);
                            errorCode = true;
                        } else if (runApplication.EINSTELLUNGEN.get("EinsatznummerIstPflicht").equals("1") && VeranstaltungEditierenAO.this.einsatzNummerOffiziell.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, Konstante.DIE_EINSTAZNUMMER_IST_EIN_PFLICHTEINTRAG, "Warnung", 2);
                            VeranstaltungEditierenAO.this.einsatzNummerOffiziell.setBackground(Color.red);
                            errorCode = true;
                        } else if (runApplication.EINSTELLUNGEN.get("EinsatzLeiterBFIstPflicht").equals("1") && runApplication.EINSTELLUNGEN.get("einsatzleiterBF").equals("1") && VeranstaltungEditierenAO.this.einsatzleiterBF.getSelectedItem().toString().equals("")) {
                            JOptionPane.showMessageDialog(null, Konstante.DIE_EINSATZLEITERBF_IST_EIN_PFLICHTEINTRAG, "Warnung", 2);
                            VeranstaltungEditierenAO.this.einsatzleiterBF.setBackground(Color.red);
                            errorCode = true;
                        } else {
                            VeranstaltungEditierenAO.this.datum.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.zeitAlamierung_label.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.zeitAlarm.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.einsatzNummerOffiziell.setBackground(Color.white);
                            einsatzleiter.setBackground(null);
                            VeranstaltungEditierenAO.this.einsatzleiterBF.setBackground(null);
                            VeranstaltungEditierenAO.this.strasse.setBackground(null);
                            int einsatzleiterID = tabMitglied.getIdByGuiString(einsatzleiter.getSelectedItem().toString());
                            if (tabAnwesenheit.getAnwesendStatus(einsatzleiterID, vID) == 0) {
                                anwesenheit.setId(tabAnwesenheit.getNextNummer());
                                anwesenheit.setJahr(tabVeranstaltung.getJahrDerVeranstaltung(vID));
                                anwesenheit.setMitgliederID(einsatzleiterID);
                                anwesenheit.setVeranstaltungID(vID);
                                anwesenheit.setVeranstaltungKategorie(1);
                                tabAnwesenheit.insert(anwesenheit);
                                logging.logInfo((Object)"Gew\u00e4hlter Einsatzleiter war nicht anwesend. Anwesenheit wurde gesetzt.");
                            }
                            veranstaltung.setId(vID);
                            veranstaltung.setDatum(TimeCalculation.parseDateForDatabase(VeranstaltungEditierenAO.this.datum.getText()));
                            veranstaltung.setZeit(VeranstaltungEditierenAO.this.zeitAlarm.getText());
                            veranstaltung.setZeitEnde(VeranstaltungEditierenAO.this.zeitEingerueckt[0].getText());
                            veranstaltung.setName(nameVeranstaltung);
                            veranstaltung.setName2("Einsatz_" + Box_Stichwort.getSelectedItem());
                            veranstaltung.setKategorie(1);
                            tabVeranstaltung.update(veranstaltung);
                            if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1")) {
                                Joomla.updateVeranstaltung(veranstaltung);
                            }
                            einsatz.setEinsatznummerOffiziell(VeranstaltungEditierenAO.this.einsatzNummerOffiziell.getText());
                            einsatz.setVeranstaltungID(vID);
                            einsatz.setDatum(TimeCalculation.parseDateForDatabase(VeranstaltungEditierenAO.this.datum.getText()));
                            einsatz.setZeitAlarm(VeranstaltungEditierenAO.this.zeitAlarm.getText());
                            einsatz.setZeitAusgerueckt(VeranstaltungEditierenAO.this.zeitAusgerueckt[erstFahrzeug].getText());
                            einsatz.setZeitEingetroffen(VeranstaltungEditierenAO.this.zeitEingetroffen[erstFahrzeug].getText());
                            einsatz.setZeitEingerueckt(VeranstaltungEditierenAO.this.zeitEingerueckt[erstFahrzeug].getText());
                            einsatz.setOrt(VeranstaltungEditierenAO.this.strasse.getSelectedItem().toString());
                            einsatz.setStadtteil(VeranstaltungEditierenAO.this.stadtteil.getSelectedItem().toString());
                            einsatz.setStichwort(tabStichwort.getStichwortID(Box_Stichwort.getSelectedItem().toString()));
                            einsatz.setEinsatzleiter(einsatzleiterID);
                            einsatz.setEinsatzleiterBF(VeranstaltungEditierenAO.this.einsatzleiterBF.getSelectedItem().toString());
                            StringBuilder buildFahrzeugName = new StringBuilder();
                            StringBuilder buildFahrzeugID = new StringBuilder();
                            int x2 = 0;
                            while (x2 < VeranstaltungEditierenAO.this.jCheckboxArray.length) {
                                if (VeranstaltungEditierenAO.this.jCheckboxArray[x2].isSelected()) {
                                    buildFahrzeugName.append(VeranstaltungEditierenAO.this.fahrzeug_label[x2].getText());
                                    buildFahrzeugName.append(", ");
                                    buildFahrzeugID.append(VeranstaltungEditierenAO.this.jCheckboxArray[x2].getName());
                                    buildFahrzeugID.append(",");
                                }
                                ++x2;
                            }
                            einsatz.setFahrzeug(buildFahrzeugName.toString());
                            einsatz.setFahrzeugID(buildFahrzeugID.toString());
                            einsatz.setBeschreibung(VeranstaltungEditierenAO.this.beschreibung.getSelectedItem().toString());
                            einsatz.setStaerkeGF(gf);
                            einsatz.setStaerkeFM(fm);
                            einsatz.setStaerkeZF(zf);
                            tabEinsatz.update(einsatz);
                            int dauer = TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeitAlarm.getText(), VeranstaltungEditierenAO.this.zeitEingerueckt[erstFahrzeug].getText());
                            statistikEinsatz.setVeranstaltungID(vID);
                            statistikEinsatz.setJahr(Integer.parseInt(VeranstaltungEditierenAO.this.datum.getText().substring(6, 10)));
                            statistikEinsatz.setStichwort(tabStichwort.getStichwortID(Box_Stichwort.getSelectedItem().toString()));
                            statistikEinsatz.setKategorie(tabStichwort.getStichwortKategorieID(Box_Stichwort.getSelectedItem().toString()));
                            statistikEinsatz.setAusrueckezeit(TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeitAlarm.getText(), VeranstaltungEditierenAO.this.zeitAusgerueckt[erstFahrzeug].getText()));
                            statistikEinsatz.setDauer(dauer);
                            if (VeranstaltungEditierenAO.this.zeitAusgerueckt[erstFahrzeug].getText().equals("") && VeranstaltungEditierenAO.this.zeitEingetroffen[erstFahrzeug].getText().equals("")) {
                                statistikEinsatz.setDauerAlarmfahrt(0);
                            } else {
                                statistikEinsatz.setDauerAlarmfahrt(TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeitAusgerueckt[erstFahrzeug].getText(), VeranstaltungEditierenAO.this.zeitEingetroffen[erstFahrzeug].getText()));
                            }
                            statistikEinsatz.setMannstunden(dauer * (fm + gf + zf));
                            statistikEinsatz.setWochentag(TimeCalculation.wochentagErmitteln(VeranstaltungEditierenAO.this.datum.getText()));
                            tabStatistikEinsatz.update(statistikEinsatz);
                            if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1") && VeranstaltungEditierenAO.this.organisationenArray.length != 0) {
                                logging.logInfo((Object)"Hinzuf\u00fcgen der Organisationen");
                                Einsatz_organisationen[] organisatationenArray = new Einsatz_organisationen[VeranstaltungEditierenAO.this.organisationenArray.length + 1];
                                tabEinsatzOrganisation.delete(vID);
                                int count = tabEinsatzOrganisation.getNextNummer();
                                int i = 0;
                                while (i < VeranstaltungEditierenAO.this.organisationenArray.length) {
                                    Einsatz_organisationen organisatationen = new Einsatz_organisationen();
                                    organisatationen.setId(count);
                                    organisatationen.setVeranstaltungID(vID);
                                    organisatationen.setOrganisationID(VeranstaltungEditierenAO.this.organisationenArrayIDs[i]);
                                    if (CheckCombo.stateArray[i].booleanValue()) {
                                        organisatationen.setStatus(1);
                                    } else {
                                        organisatationen.setStatus(0);
                                    }
                                    organisatationenArray[i] = organisatationen;
                                    ++count;
                                    ++i;
                                }
                                Einsatz_organisationen meineOrganisation = new Einsatz_organisationen();
                                meineOrganisation.setId(count);
                                meineOrganisation.setVeranstaltungID(vID);
                                meineOrganisation.setOrganisationID(1);
                                meineOrganisation.setStatus(1);
                                organisatationenArray[((VeranstaltungEditierenAO)VeranstaltungEditierenAO.this).organisationenArray.length] = meineOrganisation;
                                tabEinsatzOrganisation.insertArray(organisatationenArray);
                            }
                            int x3 = 0;
                            while (x3 < VeranstaltungEditierenAO.this.jCheckboxArray.length) {
                                int fID = Integer.parseInt(VeranstaltungEditierenAO.this.jCheckboxArray[x3].getName());
                                if (VeranstaltungEditierenAO.this.jCheckboxArray[x3].isSelected()) {
                                    zeiten.setJahr(Integer.parseInt(VeranstaltungEditierenAO.this.datum.getText().substring(6, 10)));
                                    zeiten.setVeranstaltungID(vID);
                                    zeiten.setFahrzeugID(fID);
                                    zeiten.setZeitAlarm(VeranstaltungEditierenAO.this.zeitAlarm.getText());
                                    zeiten.setZeitAusgerueckt(VeranstaltungEditierenAO.this.zeitAusgerueckt[x3].getText());
                                    zeiten.setZeitEingetroffen(VeranstaltungEditierenAO.this.zeitEingetroffen[x3].getText());
                                    zeiten.setZeitEingerueckt(VeranstaltungEditierenAO.this.zeitEingerueckt[x3].getText());
                                }
                                if (VeranstaltungEditierenAO.this.jCheckboxArray[x3].isSelected() && tabZeiten.getCount(fID, vID) == 1) {
                                    tabZeiten.update(zeiten);
                                } else if (VeranstaltungEditierenAO.this.jCheckboxArray[x3].isSelected() && tabZeiten.getCount(fID, vID) == 0) {
                                    zeiten.setEinsatznummer(tabEinsatz.getEinsatzIDByVeranstaltungID(vID));
                                    zeiten.setId(tabZeiten.getNextNummer());
                                    tabZeiten.insert(zeiten);
                                } else if (!VeranstaltungEditierenAO.this.jCheckboxArray[x3].isSelected() && tabZeiten.getCount(fID, vID) == 1) {
                                    tabZeiten.delete(vID, fID);
                                }
                                ++x3;
                            }
                            if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente").equals("1")) {
                                TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                                String bericht = tabProtokoll.getProtokoll(vID);
                                Joomla.erstelleEinsatzBericht(vID, bericht.split("\n"), tabEinsatzOrganisation.getOrganisationIDKommaSeperated(vID));
                            }
                        }
                    } else if (VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("BSW")) {
                        nameVeranstaltung = "BSW (" + VeranstaltungEditierenAO.this.ort.getSelectedItem().toString() + " " + VeranstaltungEditierenAO.this.datum.getText() + ", " + VeranstaltungEditierenAO.this.zeit_start.getText() + ")";
                        int zaehler = tabVeranstaltung.getCount(3, VeranstaltungEditierenAO.this.datum.getText(), VeranstaltungEditierenAO.this.zeit_treffen.getText());
                        if (zaehler >= 2) {
                            nameVeranstaltung = "BSW" + zaehler + " (" + VeranstaltungEditierenAO.this.ort.getSelectedItem().toString() + " " + VeranstaltungEditierenAO.this.datum.getText() + ", " + VeranstaltungEditierenAO.this.zeit_start.getText() + ")";
                        }
                        if (!TimeCalculation.checkDateFormat(VeranstaltungEditierenAO.this.datum.getText())) {
                            VeranstaltungEditierenAO.this.datum.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (!TimeCalculation.checkTimeFormat(VeranstaltungEditierenAO.this.zeit_start.getText())) {
                            VeranstaltungEditierenAO.this.zeit_start.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (!TimeCalculation.checkTimeFormat(VeranstaltungEditierenAO.this.zeit_ende.getText())) {
                            VeranstaltungEditierenAO.this.zeit_ende.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (!TimeCalculation.checkTimeFormat(VeranstaltungEditierenAO.this.zeit_treffen.getText())) {
                            VeranstaltungEditierenAO.this.zeit_treffen.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else {
                            VeranstaltungEditierenAO.this.datum.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.zeit_start.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.zeit_ende.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.zeit_treffen.setBackground(Color.white);
                            int dauer = TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeit_treffen.getText(), VeranstaltungEditierenAO.this.zeit_ende.getText());
                            veranstaltung.setId(vID);
                            veranstaltung.setDatum(TimeCalculation.parseDateForDatabase(VeranstaltungEditierenAO.this.datum.getText()));
                            veranstaltung.setZeit(VeranstaltungEditierenAO.this.zeit_treffen.getText());
                            veranstaltung.setZeitEnde(VeranstaltungEditierenAO.this.zeit_ende.getText());
                            veranstaltung.setName(nameVeranstaltung);
                            veranstaltung.setName2("BSW_" + VeranstaltungEditierenAO.this.ort.getSelectedItem().toString());
                            veranstaltung.setKategorie(3);
                            tabVeranstaltung.update(veranstaltung);
                            if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1")) {
                                Joomla.updateVeranstaltung(veranstaltung);
                            }
                            bsw.setJahr(Integer.parseInt(VeranstaltungEditierenAO.this.datum.getText().substring(6, 10)));
                            bsw.setVeranstaltungID(vID);
                            bsw.setOrt(VeranstaltungEditierenAO.this.ort.getSelectedItem().toString());
                            bsw.setArt(VeranstaltungEditierenAO.this.art.getSelectedItem().toString());
                            bsw.setDatum(TimeCalculation.parseDateForDatabase(VeranstaltungEditierenAO.this.datum.getText()));
                            bsw.setZeit_treffen(VeranstaltungEditierenAO.this.zeit_treffen.getText());
                            bsw.setZeit_start(VeranstaltungEditierenAO.this.zeit_start.getText());
                            bsw.setZeit_ende(VeranstaltungEditierenAO.this.zeit_ende.getText());
                            tabBsw.update(bsw);
                            statistikBsw.setVeranstaltungID(vID);
                            statistikBsw.setJahr(Integer.parseInt(VeranstaltungEditierenAO.this.datum.getText().substring(6, 10)));
                            statistikBsw.setDauer(dauer);
                            statistikBsw.setMannstunden(dauer * (fm + gf + zf));
                            statistikBsw.setWochentag(TimeCalculation.wochentagErmitteln(VeranstaltungEditierenAO.this.datum.getText()));
                            tabStatistikBSW.update(statistikBsw);
                        }
                    } else {
                        nameVeranstaltung = String.valueOf(VeranstaltungEditierenAO.this.name.getText()) + " (" + VeranstaltungEditierenAO.this.datum.getText() + ", " + VeranstaltungEditierenAO.this.zeit.getText() + ")";
                        if (VeranstaltungEditierenAO.this.datum.getText().length() <= 9) {
                            VeranstaltungEditierenAO.this.datum.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (!TimeCalculation.checkDateFormat(VeranstaltungEditierenAO.this.datum.getText())) {
                            VeranstaltungEditierenAO.this.datum.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (!TimeCalculation.checkTimeFormat(VeranstaltungEditierenAO.this.zeit.getText())) {
                            VeranstaltungEditierenAO.this.zeit.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else if (!TimeCalculation.checkTimeFormat(VeranstaltungEditierenAO.this.zeitEnde.getText()) && !VeranstaltungEditierenAO.this.zeitEnde.getText().equals("")) {
                            VeranstaltungEditierenAO.this.zeitEnde.setBackground(Color.red);
                            JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                            errorCode = true;
                        } else {
                            VeranstaltungEditierenAO.this.datum.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.zeit.setBackground(Color.white);
                            VeranstaltungEditierenAO.this.zeitEnde.setBackground(Color.white);
                            int kID = tabKategorie.getID(kategorie.getSelectedItem().toString());
                            int dauer = TimeCalculation.calculateDuration(VeranstaltungEditierenAO.this.zeit.getText(), VeranstaltungEditierenAO.this.zeitEnde.getText());
                            veranstaltung.setId(vID);
                            veranstaltung.setDatum(TimeCalculation.parseDateForDatabase(VeranstaltungEditierenAO.this.datum.getText()));
                            veranstaltung.setZeit(VeranstaltungEditierenAO.this.zeit.getText());
                            veranstaltung.setZeitEnde(VeranstaltungEditierenAO.this.zeitEnde.getText());
                            veranstaltung.setName2(VeranstaltungEditierenAO.this.name.getText());
                            veranstaltung.setName(nameVeranstaltung);
                            veranstaltung.setKategorie(kID);
                            tabVeranstaltung.update(veranstaltung);
                            if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1")) {
                                Joomla.updateVeranstaltung(veranstaltung);
                            }
                            statistikSonstige.setVeranstaltungID(vID);
                            statistikSonstige.setJahr(Integer.parseInt(VeranstaltungEditierenAO.this.datum.getText().substring(6, 10)));
                            statistikSonstige.setDauer(dauer);
                            statistikSonstige.setKategorie(kID);
                            statistikSonstige.setMannstunden(dauer * (fm + gf + zf));
                            statistikSonstige.setWochentag(TimeCalculation.wochentagErmitteln(VeranstaltungEditierenAO.this.datum.getText()));
                            tabStatistikSonstige.update(statistikSonstige);
                            new TabelleFahrzeugeinteilung().updateKategorie(kID, vID);
                        }
                    }
                    if (!errorCode) {
                        logbuchEingabe.NeuerEintag("Veranstaltungsdaten wurden ver\u00e4ndert VeranstaltungID = " + vID);
                        logging.logInfo((Object)("VeranstaltungID: " + vID + " gespeichert"));
                        if (VeranstaltungEditierenAO.this.zeitEnde.getText().equals("") && !VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("BSW") && !VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString().startsWith("Einsatz")) {
                            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_VERANSTALTUNG_HINWEIS);
                        } else {
                            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        }
                        if (!VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().equals(nameVeranstaltung)) {
                            logging.logInfo((Object)("Veranstaltungsname ge\u00e4ndert: " + VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem() + " ->> " + nameVeranstaltung));
                            VeranstaltungEditierenAO.this.veranstaltungComboBox.removeItem(VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem());
                            VeranstaltungEditierenAO.this.veranstaltungComboBox.addItem(nameVeranstaltung);
                        }
                        VeranstaltungEditierenAO.this.veranstaltungComboBox.setSelectedItem("<bitte w\u00e4hlen>");
                        VeranstaltungEditierenAO.this.veranstaltungID.setText(null);
                        VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.panelVeranstaltung);
                        VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.dummy2);
                        VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.dummy4);
                        VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.buttonAktualisieren);
                        VeranstaltungEditierenAO.this.remove(VeranstaltungEditierenAO.this.buttonZurueck);
                        VeranstaltungEditierenAO.this.repaint();
                        VeranstaltungEditierenAO.this.validate();
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAtemschutzpass.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logging.logInfo((Object)("Setze Veranstaltung f\u00fcr Atemschutzpass: --> " + VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString()));
                runApplication.letzterVeranstaltungsname = VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString();
                MyEvent.setEvent((String)"0x0350");
                logging.logInfo((Object)"Starte: AtemschutzpassAO --> aus Veranstaltung Editieren");
                Steuerung.setStatus(Status.ATEMSCHUTZPASS_EINTRAG);
                Steuerung.steuerung();
            }
        });
        this.buttonFahrzeugBelegung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logging.logInfo((Object)("Setze Veranstaltung f\u00fcr Fahrzeugbelegung: --> " + VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString()));
                runApplication.letzterVeranstaltungsname = VeranstaltungEditierenAO.this.veranstaltungComboBox.getSelectedItem().toString();
                logging.logInfo((Object)"Starte: FahrzeugBelegungAO --> aus Veranstaltung Editieren");
                Steuerung.setStatus(Status.FAHRZEUG_BELEGUNG);
                Steuerung.steuerung();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean checkInputs() {
        try {
            int x = 0;
            while (x < this.jCheckboxArray.length) {
                if (this.jCheckboxArray[x].isSelected()) {
                    if (!this.zeitAusgerueckt[x].getText().equals("") && !TimeCalculation.checkTimeFormat(this.zeitAusgerueckt[x].getText())) {
                        this.zeitAusgerueckt[x].setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                        return false;
                    }
                    if (!this.zeitEingetroffen[x].getText().equals("") && !TimeCalculation.checkTimeFormat(this.zeitEingetroffen[x].getText())) {
                        this.zeitEingetroffen[x].setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                        return false;
                    }
                    if (!TimeCalculation.checkTimeFormat(this.zeitEingerueckt[x].getText())) {
                        this.zeitEingerueckt[x].setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                        return false;
                    }
                }
                ++x;
            }
            x = 0;
            while (x < this.jCheckboxArray.length) {
                this.zeitAusgerueckt[x].setBackground(Color.white);
                this.zeitEingetroffen[x].setBackground(Color.white);
                this.zeitEingerueckt[x].setBackground(Color.white);
                ++x;
            }
            return true;
        }
        catch (ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)e);
            return true;
        }
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

