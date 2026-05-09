/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  org.apache.commons.io.FilenameUtils
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.einsatz;

import ao.AbstractFenster;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleOrganisationen;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import go.Einsatz;
import go.Einsatz_bericht;
import go.Einsatz_organisationen;
import go.Einsatz_zeiten;
import go.StatistikEinsatz;
import go.Veranstaltung;
import java.awt.Color;
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import logging.logging;
import org.apache.commons.io.FilenameUtils;
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

public class EinsatzEintragenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAnwesenheit;
    private JButton buttonStichwortEintragen;
    private JCheckBox[] jCheckboxArray;
    private JTextField einsatzNummerIntern;
    private JTextField einsatzNummerOffiziell;
    private JTextField datum;
    private JTextField zeitAlarm;
    private JTextField[] zeitAusgerueckt;
    private JTextField[] zeitEingetroffen;
    private JTextField[] zeitEingerueckt;
    private JComboBox<String> strasse;
    private JComboBox<String> stadtteil;
    private JComboBox<String> beschreibung;
    public static JComboBox<String> einsatzleiter;
    private JComboBox<String> einsatzleiterBF;
    private String[] organisationenArray;
    private int[] organisationenArrayIDs;
    public static Boolean[] organisationenArrayBelegung;
    public static JComboBox<String> Box_Stichwort;
    private JLabel einsatznummerIntern_label;
    private JLabel einsatznummerOffiziell_label;
    private JLabel datum_label;
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
    private JPanel panelEinsatz;

    public EinsatzEintragenAO() {
        super("FeuerwehrManagementSystem - Einsatz");
        logging.logInfo((Object)"Starte: EinsatzEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonAnwesenheit = new JButton("Anwesenheit eintragen");
        this.buttonStichwortEintragen = new JButton("Neues Stichwort anlegen");
        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        String[] ortListe = null;
        String[] beschreibungListe = null;
        String[] stadtteilListe = null;
        String[] mitgliederListe = null;
        String[] einsatzleiterBFListe = null;
        try {
            this.einsatzNummerIntern = new JTextField("-", 20);
            this.einsatzNummerOffiziell = new JTextField(20);
            ortListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getStrasseListe());
            beschreibungListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getBeschreibungListe());
            stadtteilListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getStadtteilListe());
            mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getAlleTruppUndGruppenfuehrerDerGruppe1());
            einsatzleiterBFListe = Utils.listToArrayWithEmptyLine(tabEinsatz.getEinsatzleiterBFListe());
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.datum = new JTextField(SbcUtils.timeStamp((String)"dd.MM.yyyy"), 20);
        this.zeitAlarm = new JTextField(20);
        this.strasse = new JComboBox<String>(ortListe);
        this.beschreibung = new JComboBox<String>(beschreibungListe);
        this.stadtteil = new JComboBox<String>(stadtteilListe);
        einsatzleiter = new JComboBox<String>(mitgliederListe);
        this.einsatzleiterBF = new JComboBox<String>(einsatzleiterBFListe);
        this.einsatznummerIntern_label = new JLabel("Einsatz Z\u00e4hlung:");
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
        this.modulBeschreibung = new JLabel("Einsatz Eintragen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.dummy4 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        try {
            TabelleStichwort stichwort = new TabelleStichwort();
            TabelleOrganisationen tabOrganisation = new TabelleOrganisationen();
            String[] liste = Utils.listToArrayOnlyFORComboBoxes(stichwort.getAllStichwort());
            String[] listeOrganisation = Utils.listToArray(tabOrganisation.getAllOrganisationenWithout1());
            int[] listeOrganisationIDs = Utils.listToIntArray(tabOrganisation.getAllOrganisationenIDsWithout1());
            Box_Stichwort = new JComboBox<String>(liste);
            this.organisationenArray = new String[listeOrganisation.length];
            this.organisationenArray = listeOrganisation;
            this.organisationenArrayIDs = new int[listeOrganisation.length];
            this.organisationenArrayIDs = listeOrganisationIDs;
            organisationenArrayBelegung = new Boolean[listeOrganisation.length];
            int b = 0;
            while (b < listeOrganisation.length) {
                EinsatzEintragenAO.organisationenArrayBelegung[b] = Boolean.FALSE;
                ++b;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void setzeAuswahllisten() {
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
                logging.logInfo((Object)"Starte EinsatzEintragenAO mit der Gr\u00f6\u00dfe: 690x560");
                this.setSize(690, 570);
            } else if (anzahlFahrzeuge <= 4) {
                logging.logInfo((Object)"Starte EinsatzEintragenAO mit der Gr\u00f6\u00dfe: 890x560");
                this.setSize(890, 570);
            } else if (anzahlFahrzeuge <= 6) {
                logging.logInfo((Object)"Starte EinsatzEintragenAO mit der Gr\u00f6\u00dfe: 690x660");
                this.setSize(690, 670);
            } else if (anzahlFahrzeuge <= 8) {
                logging.logInfo((Object)"Starte EinsatzEintragenAO mit der Gr\u00f6\u00dfe: 890x560");
                this.setSize(890, 670);
            } else if (anzahlFahrzeuge <= 9) {
                logging.logInfo((Object)"Starte EinsatzEintragenAO mit der Gr\u00f6\u00dfe: 890x760");
                this.setSize(890, 768);
            } else if (anzahlFahrzeuge <= 12) {
                logging.logInfo((Object)"Starte EinsatzEintragenAO mit der Gr\u00f6\u00dfe: 890x760");
                this.setSize(890, 768);
            } else {
                logging.logInfo((Object)"Starte EinsatzEintragenAO mit der Gr\u00f6\u00dfe: 1280x760");
                this.setSize(1280, 768);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.setTitle("FeuerwehrManagementSystem - Einsatz");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonStichwortEintragen);
        this.add(this.dummy3);
        this.panelEinsatz = new JPanel(new GridLayout(11, 2));
        this.getContentPane().add("Center", this.panelEinsatz);
        this.panelEinsatz.add(this.einsatznummerIntern_label);
        this.panelEinsatz.add(this.einsatzNummerIntern);
        this.panelEinsatz.add(this.einsatznummerOffiziell_label);
        this.panelEinsatz.add(this.einsatzNummerOffiziell);
        this.panelEinsatz.add(this.stichwort_label);
        this.panelEinsatz.add(Box_Stichwort);
        this.panelEinsatz.add(this.datum_label);
        this.panelEinsatz.add(this.datum);
        this.panelEinsatz.add(this.zeitAlamierung_label);
        this.panelEinsatz.add(this.zeitAlarm);
        this.panelEinsatz.add(this.strasse_label);
        this.panelEinsatz.add(this.strasse);
        this.panelEinsatz.add(this.stadtteil_label);
        this.panelEinsatz.add(this.stadtteil);
        this.panelEinsatz.add(this.einsatzleiter_label);
        this.panelEinsatz.add(einsatzleiter);
        this.panelEinsatz.add(this.einsatzleiterBF_label);
        this.panelEinsatz.add(this.einsatzleiterBF);
        this.panelEinsatz.add(this.beschreibung_label);
        this.panelEinsatz.add(this.beschreibung);
        System.out.println(this.organisationenArray.length);
        if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1") && this.organisationenArray.length != 0) {
            this.panelEinsatz.add(this.organisationen_label);
            this.panelEinsatz.add(CheckCombo.getComboboxWithCheckBoxes(this.organisationenArray, this.organisationenArrayIDs, organisationenArrayBelegung));
        } else if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1") && this.organisationenArray.length == 0) {
            this.panelEinsatz.add(this.organisationen_label);
            JLabel keineOrganisation = new JLabel("keine Weiteren Organisationen verf\u00fcgbar!");
            keineOrganisation.setToolTipText("Organisationen k\u00f6nnen \u00fcber die Kategorieverwaltung hinzugef\u00fcgt bzw. editiert werden!");
            this.panelEinsatz.add(keineOrganisation);
        }
        this.add(this.dummy4);
        this.einsatzNummerIntern.setEditable(false);
        this.strasse.setEditable(true);
        this.stadtteil.setEditable(true);
        this.beschreibung.setEditable(true);
        this.einsatzleiterBF.setEditable(true);
        try {
            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
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
            int x = 0;
            while (x < CheckBoxNumber) {
                JPanel panel = new JPanel(new GridLayout(4, 2));
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
                this.jCheckboxArray[x].addItemListener(this.createItemListener(x));
                panel.add(this.fahrzeug_label[x]);
                panel.add(this.jCheckboxArray[x]);
                panel.add(this.zeitAusgerueckt_label[x]);
                panel.add(this.zeitAusgerueckt[x]);
                panel.add(this.zeitEingetroffen_label[x]);
                panel.add(this.zeitEingetroffen[x]);
                panel.add(this.zeitEingerueckt_label[x]);
                panel.add(this.zeitEingerueckt[x]);
                logging.logInfo((Object)("F\u00fcge Fahrzeug: " + labels[x] + " hinzu...."));
                this.add(panel, "Center");
                this.zeitAusgerueckt[x].setVisible(false);
                this.zeitAusgerueckt_label[x].setVisible(false);
                this.zeitEingetroffen[x].setVisible(false);
                this.zeitEingetroffen_label[x].setVisible(false);
                this.zeitEingerueckt[x].setVisible(false);
                this.zeitEingerueckt_label[x].setVisible(false);
                ++x;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonAnwesenheit);
        this.buttonAnwesenheit.setEnabled(false);
        if (BerechtigunsManager.ber[39] == 1) {
            this.buttonStichwortEintragen.setVisible(true);
        } else {
            this.buttonStichwortEintragen.setVisible(false);
        }
        if (runApplication.EINSTELLUNGEN.get("einsatzleiterBF").equals("0")) {
            this.einsatzleiterBF.setVisible(false);
            this.einsatzleiterBF_label.setVisible(false);
        } else {
            this.einsatzleiter_label.setText("1. Gruppenf\u00fchrer FF: ");
        }
        if (runApplication.EINSTELLUNGEN.get("feldStadtteilAusblenden").equals("1")) {
            this.stadtteil_label.setVisible(false);
            this.stadtteil.setVisible(false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!EinsatzEintragenAO.this.buttonSpeichern.isEnabled()) {
                    int msg = JOptionPane.showConfirmDialog(null, Konstante.BEENDEN_EINSATZ_ANLEGEN_AO, "Frage", 0);
                    if (msg == 0) {
                        runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                        EinsatzEintragenAO.this.dispose();
                    }
                } else {
                    EinsatzEintragenAO.this.dispose();
                }
            }
        });
        this.buttonStichwortEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0020");
                Steuerung.setStatus(Status.STICHWORT_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                TabelleEinsatz_bericht tabEinsatzBeicht = new TabelleEinsatz_bericht();
                TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
                TabelleStatistikEinsatz tabStatistik = new TabelleStatistikEinsatz();
                TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
                TabelleStichwort tabStichwort = new TabelleStichwort();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                Einsatz einsatz = new Einsatz();
                Einsatz_bericht bericht = new Einsatz_bericht();
                Einsatz_zeiten zeiten = new Einsatz_zeiten();
                StatistikEinsatz statistik = new StatistikEinsatz();
                Veranstaltung veranstaltung = new Veranstaltung();
                int erstFahrzeug = -1;
                int zweitFahrzeug = -1;
                int drittFahrzeug = -1;
                int viertFahrzeug = -1;
                try {
                    int fahrzeugCount = tabFahrzeug.countWithoutAnhaenger();
                    int x = 0;
                    while (x < fahrzeugCount) {
                        if (EinsatzEintragenAO.this.jCheckboxArray[x].isSelected() && erstFahrzeug == -1) {
                            erstFahrzeug = x;
                            logging.logInfo((Object)("Fahrzeug 1 im Einsatz - fID = " + erstFahrzeug));
                        } else if (EinsatzEintragenAO.this.jCheckboxArray[x].isSelected() && zweitFahrzeug == -1 && erstFahrzeug != -1) {
                            zweitFahrzeug = x;
                            logging.logInfo((Object)("Fahrzeug 2 im Einsatz - fID = " + zweitFahrzeug));
                        } else if (EinsatzEintragenAO.this.jCheckboxArray[x].isSelected() && zweitFahrzeug != -1 && erstFahrzeug != -1 && drittFahrzeug == -1) {
                            drittFahrzeug = x;
                            logging.logInfo((Object)("Fahrzeug 3 im Einsatz - fID = " + drittFahrzeug));
                        } else if (EinsatzEintragenAO.this.jCheckboxArray[x].isSelected() && zweitFahrzeug != -1 && erstFahrzeug != -1 && drittFahrzeug != -1 && viertFahrzeug == -1) {
                            viertFahrzeug = x;
                            logging.logInfo((Object)("Fahrzeug 4 im Einsatz - fID = " + viertFahrzeug));
                            break;
                        }
                        ++x;
                    }
                    if (EinsatzEintragenAO.this.zeitAlarm.getText().length() == 4) {
                        String eingabe = EinsatzEintragenAO.this.zeitAlarm.getText();
                        EinsatzEintragenAO.this.zeitAlarm.setText("0" + eingabe);
                    }
                    x = 0;
                    while (x < fahrzeugCount) {
                        String eingabe;
                        if (EinsatzEintragenAO.this.zeitAusgerueckt[x].getText().length() == 4) {
                            eingabe = EinsatzEintragenAO.this.zeitAusgerueckt[x].getText();
                            EinsatzEintragenAO.this.zeitAusgerueckt[x].setText("0" + eingabe);
                        }
                        if (EinsatzEintragenAO.this.zeitEingerueckt[x].getText().length() == 4) {
                            eingabe = EinsatzEintragenAO.this.zeitEingerueckt[x].getText();
                            EinsatzEintragenAO.this.zeitEingerueckt[x].setText("0" + eingabe);
                        }
                        if (EinsatzEintragenAO.this.zeitEingetroffen[x].getText().length() == 4) {
                            eingabe = EinsatzEintragenAO.this.zeitEingetroffen[x].getText();
                            EinsatzEintragenAO.this.zeitEingetroffen[x].setText("0" + eingabe);
                        }
                        ++x;
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                if (!TimeCalculation.checkDateFormat(EinsatzEintragenAO.this.datum.getText())) {
                    EinsatzEintragenAO.this.datum.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkTimeFormat(EinsatzEintragenAO.this.zeitAlarm.getText())) {
                    EinsatzEintragenAO.this.zeitAlarm.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (Box_Stichwort.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_STICHTWORT_WAEHLEN, "Warnung", 2);
                } else if (!EinsatzEintragenAO.this.checkInputs()) {
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (erstFahrzeug == -1) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_FAHRZEUG_WAEHLEN, "Fehlermeldung", 0);
                } else if (EinsatzEintragenAO.this.strasse.getSelectedItem().equals("")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_STRASSE_WAEHLEN, "Warnung", 2);
                    EinsatzEintragenAO.this.strasse.setBackground(Color.red);
                } else if (einsatzleiter.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Fehlermeldung", 0);
                    einsatzleiter.setBackground(Color.red);
                } else if (EinsatzEintragenAO.this.zeitAlarm.getText().equals(EinsatzEintragenAO.this.zeitAusgerueckt[erstFahrzeug].getText())) {
                    JOptionPane.showMessageDialog(null, Konstante.ZEITEN_GLEICH, "Fehlermeldung", 0);
                    EinsatzEintragenAO.this.zeitAusgerueckt[0].setBackground(Color.red);
                } else if (!new File(runApplication.EINSTELLUNGEN.get("EinsatzBericht")).exists() && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle")) {
                    JOptionPane.showMessageDialog(null, "Die Einsatzberichtvorlage ist nicht vorhanden und kann nicht erstellt werden.\nBitte kontrollieren Sie die Programmeinstellungen.\n\nFolgende Datei ist nicht vorhanden:\n" + runApplication.EINSTELLUNGEN.get("EinsatzBericht"), "Fehlermeldung", 0);
                } else if (EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("/") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("\\") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("\"") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("{") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("}") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains(",") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains(":") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains(";") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("`") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("#") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("*") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains(">") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("<") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("!") | EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().toString().contains("&")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_EINSATZNUMMER_KONTROLLIEREN, "Warnung", 2);
                    EinsatzEintragenAO.this.einsatzNummerOffiziell.setBackground(Color.red);
                    logging.logInfo((Object)"Illegales Zeichen in der Einsatznummer gefunden --> Fehlermeldung f\u00fcr den Benutzer");
                } else if (runApplication.EINSTELLUNGEN.get("EinsatznummerIstPflicht").equals("1") && EinsatzEintragenAO.this.einsatzNummerOffiziell.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, Konstante.DIE_EINSTAZNUMMER_IST_EIN_PFLICHTEINTRAG, "Warnung", 2);
                    EinsatzEintragenAO.this.einsatzNummerOffiziell.setBackground(Color.red);
                } else if (runApplication.EINSTELLUNGEN.get("EinsatzLeiterBFIstPflicht").equals("1") && runApplication.EINSTELLUNGEN.get("einsatzleiterBF").equals("1") && EinsatzEintragenAO.this.einsatzleiterBF.getSelectedItem().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, Konstante.DIE_EINSATZLEITERBF_IST_EIN_PFLICHTEINTRAG, "Warnung", 2);
                    EinsatzEintragenAO.this.einsatzleiterBF.setBackground(Color.red);
                } else {
                    try {
                        EinsatzEintragenAO.this.datum.setBackground(Color.white);
                        EinsatzEintragenAO.this.zeitAlamierung_label.setBackground(Color.white);
                        EinsatzEintragenAO.this.zeitAlarm.setBackground(Color.white);
                        EinsatzEintragenAO.this.einsatzNummerOffiziell.setBackground(Color.white);
                        einsatzleiter.setBackground(null);
                        EinsatzEintragenAO.this.einsatzleiterBF.setBackground(null);
                        EinsatzEintragenAO.this.strasse.setBackground(null);
                        String nameVeransatltung = "Einsatz (" + Box_Stichwort.getSelectedItem() + " " + EinsatzEintragenAO.this.datum.getText() + ", " + EinsatzEintragenAO.this.zeitAlarm.getText() + ")";
                        int zaehler = tabVeransatltung.getCount(1, EinsatzEintragenAO.this.datum.getText(), EinsatzEintragenAO.this.zeitAlarm.getText());
                        if (zaehler != 0) {
                            nameVeransatltung = "Einsatz" + zaehler + " (" + Box_Stichwort.getSelectedItem() + " " + EinsatzEintragenAO.this.datum.getText() + ", " + EinsatzEintragenAO.this.zeitAlarm.getText() + ")";
                        }
                        int vID = tabVeransatltung.getNextNummer();
                        int eNummer = tabEinsatz.getNextNummer(EinsatzEintragenAO.this.datum.getText().substring(6, 10));
                        int einsatzleiterID = tabMitglied.getIdByGuiString(einsatzleiter.getSelectedItem().toString());
                        EinsatzEintragenAO.this.einsatzNummerIntern.setText(Integer.toString(eNummer));
                        veranstaltung.setId(vID);
                        veranstaltung.setDatum(TimeCalculation.parseDateForDatabase(EinsatzEintragenAO.this.datum.getText()));
                        veranstaltung.setZeit(EinsatzEintragenAO.this.zeitAlarm.getText());
                        veranstaltung.setZeitEnde(EinsatzEintragenAO.this.zeitEingerueckt[0].getText());
                        veranstaltung.setName(nameVeransatltung);
                        veranstaltung.setName2("Einsatz_" + Box_Stichwort.getSelectedItem());
                        veranstaltung.setKategorie(1);
                        veranstaltung.setFahrzeugeinteilung(1);
                        veranstaltung.setInfoVersandt(1);
                        tabVeransatltung.insert(veranstaltung);
                        logging.logInfo((Object)"Veransatltung wurde eingetragen");
                        einsatz.setId(tabEinsatz.getNextID());
                        einsatz.setEinsatznummer(eNummer);
                        einsatz.setEinsatznummerOffiziell(EinsatzEintragenAO.this.einsatzNummerOffiziell.getText());
                        einsatz.setVeranstaltungID(vID);
                        einsatz.setDatum(TimeCalculation.parseDateForDatabase(EinsatzEintragenAO.this.datum.getText()));
                        einsatz.setZeitAlarm(EinsatzEintragenAO.this.zeitAlarm.getText());
                        einsatz.setZeitAusgerueckt(EinsatzEintragenAO.this.zeitAusgerueckt[erstFahrzeug].getText());
                        einsatz.setZeitEingetroffen(EinsatzEintragenAO.this.zeitEingetroffen[erstFahrzeug].getText());
                        einsatz.setZeitEingerueckt(EinsatzEintragenAO.this.zeitEingerueckt[erstFahrzeug].getText());
                        einsatz.setOrt(EinsatzEintragenAO.this.strasse.getSelectedItem().toString());
                        einsatz.setStadtteil(EinsatzEintragenAO.this.stadtteil.getSelectedItem().toString());
                        einsatz.setStichwort(tabStichwort.getStichwortID(Box_Stichwort.getSelectedItem().toString()));
                        einsatz.setEinsatzleiter(einsatzleiterID);
                        einsatz.setEinsatzleiterBF(EinsatzEintragenAO.this.einsatzleiterBF.getSelectedItem().toString());
                        StringBuilder buildFahrzeugName = new StringBuilder();
                        StringBuilder buildFahrzeugID = new StringBuilder();
                        int x = 0;
                        while (x < EinsatzEintragenAO.this.jCheckboxArray.length) {
                            if (EinsatzEintragenAO.this.jCheckboxArray[x].isSelected()) {
                                buildFahrzeugName.append(EinsatzEintragenAO.this.fahrzeug_label[x].getText());
                                buildFahrzeugName.append(", ");
                                buildFahrzeugID.append(EinsatzEintragenAO.this.jCheckboxArray[x].getName());
                                buildFahrzeugID.append(",");
                            }
                            ++x;
                        }
                        einsatz.setFahrzeug(buildFahrzeugName.toString());
                        einsatz.setFahrzeugID(buildFahrzeugID.toString());
                        einsatz.setBeschreibung(EinsatzEintragenAO.this.beschreibung.getSelectedItem().toString());
                        einsatz.setStaerkeGF(0);
                        einsatz.setStaerkeFM(0);
                        einsatz.setStaerkeZF(0);
                        tabEinsatz.insert(einsatz);
                        int wochentagID = TimeCalculation.wochentagErmitteln(EinsatzEintragenAO.this.datum.getText());
                        String wochentagName = TimeCalculation.wochentagNameByWochentagID(wochentagID);
                        statistik.setId(tabStatistik.getNextNummer());
                        statistik.setVeranstaltungID(vID);
                        statistik.setEinsatzID(Integer.parseInt(EinsatzEintragenAO.this.einsatzNummerIntern.getText()));
                        statistik.setJahr(Integer.parseInt(EinsatzEintragenAO.this.datum.getText().substring(6, 10)));
                        statistik.setStichwort(tabStichwort.getStichwortID(Box_Stichwort.getSelectedItem().toString()));
                        statistik.setKategorie(tabStichwort.getStichwortKategorieID(Box_Stichwort.getSelectedItem().toString()));
                        statistik.setAusrueckezeit(TimeCalculation.calculateDuration(EinsatzEintragenAO.this.zeitAlarm.getText(), EinsatzEintragenAO.this.zeitAusgerueckt[erstFahrzeug].getText()));
                        statistik.setDauer(TimeCalculation.calculateDuration(EinsatzEintragenAO.this.zeitAlarm.getText(), EinsatzEintragenAO.this.zeitEingerueckt[erstFahrzeug].getText()));
                        if (EinsatzEintragenAO.this.zeitAusgerueckt[erstFahrzeug].getText().equals("") && EinsatzEintragenAO.this.zeitEingetroffen[erstFahrzeug].getText().equals("")) {
                            statistik.setDauerAlarmfahrt(0);
                        } else {
                            statistik.setDauerAlarmfahrt(TimeCalculation.calculateDuration(EinsatzEintragenAO.this.zeitAusgerueckt[erstFahrzeug].getText(), EinsatzEintragenAO.this.zeitEingetroffen[erstFahrzeug].getText()));
                        }
                        statistik.setMannstunden(0);
                        statistik.setWochentag(wochentagID);
                        tabStatistik.insert(statistik);
                        int x2 = 0;
                        while (x2 < EinsatzEintragenAO.this.jCheckboxArray.length) {
                            if (EinsatzEintragenAO.this.jCheckboxArray[x2].isSelected()) {
                                zeiten.setId(tabZeiten.getNextNummer());
                                zeiten.setJahr(Integer.parseInt(EinsatzEintragenAO.this.datum.getText().substring(6, 10)));
                                zeiten.setEinsatznummer(Integer.parseInt(EinsatzEintragenAO.this.einsatzNummerIntern.getText()));
                                zeiten.setVeranstaltungID(tabVeransatltung.getVeranstaltungID(nameVeransatltung));
                                zeiten.setFahrzeugID(Integer.parseInt(EinsatzEintragenAO.this.jCheckboxArray[x2].getName()));
                                zeiten.setZeitAlarm(EinsatzEintragenAO.this.zeitAlarm.getText());
                                zeiten.setZeitAusgerueckt(EinsatzEintragenAO.this.zeitAusgerueckt[x2].getText());
                                zeiten.setZeitEingetroffen(EinsatzEintragenAO.this.zeitEingetroffen[x2].getText());
                                zeiten.setZeitEingerueckt(EinsatzEintragenAO.this.zeitEingerueckt[x2].getText());
                                tabZeiten.insert(zeiten);
                            }
                            ++x2;
                        }
                        if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1") && EinsatzEintragenAO.this.organisationenArray.length != 0) {
                            logging.logInfo((Object)"Hinzuf\u00fcgen der Organisationen");
                            TabelleEinsatz_organisationen tabEinsatzOrganisation = new TabelleEinsatz_organisationen();
                            Einsatz_organisationen[] organisatationenArray = new Einsatz_organisationen[EinsatzEintragenAO.this.organisationenArray.length + 1];
                            int count = tabEinsatzOrganisation.getNextNummer();
                            int i = 0;
                            while (i < EinsatzEintragenAO.this.organisationenArray.length) {
                                Einsatz_organisationen organisatationen = new Einsatz_organisationen();
                                organisatationen.setId(count);
                                organisatationen.setVeranstaltungID(vID);
                                organisatationen.setOrganisationID(EinsatzEintragenAO.this.organisationenArrayIDs[i]);
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
                            organisatationenArray[((EinsatzEintragenAO)EinsatzEintragenAO.this).organisationenArray.length] = meineOrganisation;
                            tabEinsatzOrganisation.insertArray(organisatationenArray);
                        }
                        logging.logInfo((Object)("Einsatz wurde eingetragen " + nameVeransatltung));
                        runApplication.letzterVeranstaltungsname = nameVeransatltung;
                        EinsatzEintragenAO.this.buttonAnwesenheit.setEnabled(true);
                        EinsatzEintragenAO.this.buttonSpeichern.setEnabled(false);
                        EinsatzEintragenAO.this.buttonStichwortEintragen.setEnabled(false);
                        if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente").equals("1")) {
                            final Einsatz einsatzForHomepage = einsatz;
                            final StatistikEinsatz statistikForHomepage = statistik;
                            Thread threadEinsatzkomponente = new Thread(){

                                @Override
                                public void run() {
                                    logging.logInfo((Object)"Starte JoomlaThread - Sende Einsatz an die Einsatzkomponente...");
                                    Joomla.erstelleEinsatz(einsatzForHomepage, statistikForHomepage, true, false);
                                }
                            };
                            threadEinsatzkomponente.start();
                        }
                        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle")) {
                            String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + EinsatzEintragenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + EinsatzEintragenAO.this.einsatzNummerIntern.getText() + "_ID_" + EinsatzEintragenAO.this.einsatzNummerOffiziell.getText() + ".xml";
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
                                bericht_name2 = EinsatzEintragenAO.this.fahrzeug_label[zweitFahrzeug].getText();
                                bericht_funkname2 = EinsatzEintragenAO.this.fahrzeug_label[zweitFahrzeug].getName();
                                bericht_ausruecken2 = EinsatzEintragenAO.this.zeitAusgerueckt[zweitFahrzeug].getText();
                                bericht_eingetroffen2 = EinsatzEintragenAO.this.zeitEingetroffen[zweitFahrzeug].getText();
                                bericht_einsatzEnde2 = EinsatzEintragenAO.this.zeitEingerueckt[zweitFahrzeug].getText();
                                bericht_EinsatzZeit2 = TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(EinsatzEintragenAO.this.zeitAlarm.getText(), EinsatzEintragenAO.this.zeitEingerueckt[zweitFahrzeug].getText()));
                            }
                            if (drittFahrzeug != -1) {
                                bericht_name3 = EinsatzEintragenAO.this.fahrzeug_label[drittFahrzeug].getText();
                                bericht_funkname3 = EinsatzEintragenAO.this.fahrzeug_label[drittFahrzeug].getName();
                                bericht_ausruecken3 = EinsatzEintragenAO.this.zeitAusgerueckt[drittFahrzeug].getText();
                                bericht_eingetroffen3 = EinsatzEintragenAO.this.zeitEingetroffen[drittFahrzeug].getText();
                                bericht_einsatzEnde3 = EinsatzEintragenAO.this.zeitEingerueckt[drittFahrzeug].getText();
                                bericht_EinsatzZeit3 = TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(EinsatzEintragenAO.this.zeitAlarm.getText(), EinsatzEintragenAO.this.zeitEingerueckt[drittFahrzeug].getText()));
                            }
                            if (viertFahrzeug != -1) {
                                bericht_name4 = EinsatzEintragenAO.this.fahrzeug_label[viertFahrzeug].getText();
                                bericht_funkname4 = EinsatzEintragenAO.this.fahrzeug_label[viertFahrzeug].getName();
                                bericht_ausruecken4 = EinsatzEintragenAO.this.zeitAusgerueckt[viertFahrzeug].getText();
                                bericht_eingetroffen4 = EinsatzEintragenAO.this.zeitEingetroffen[viertFahrzeug].getText();
                                bericht_einsatzEnde4 = EinsatzEintragenAO.this.zeitEingerueckt[viertFahrzeug].getText();
                                bericht_EinsatzZeit4 = TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(EinsatzEintragenAO.this.zeitAlarm.getText(), EinsatzEintragenAO.this.zeitEingerueckt[viertFahrzeug].getText()));
                            }
                            String[] zu = new String[]{EinsatzEintragenAO.this.einsatzNummerOffiziell.getText(), EinsatzEintragenAO.this.datum.getText().substring(6, 10), wochentagName, EinsatzEintragenAO.this.datum.getText(), Utils.checkTextAndRemoveIllegalSigns(runApplication.EINSTELLUNGEN.get("Stadt")), Utils.checkTextAndRemoveIllegalSigns(EinsatzEintragenAO.this.stadtteil.getSelectedItem().toString()), Utils.checkTextAndRemoveIllegalSigns(EinsatzEintragenAO.this.strasse.getSelectedItem().toString()), Utils.checkTextAndRemoveIllegalSigns(Box_Stichwort.getSelectedItem().toString()), EinsatzEintragenAO.this.zeitAlarm.getText(), EinsatzEintragenAO.this.fahrzeug_label[erstFahrzeug].getText(), bericht_name2, bericht_name3, bericht_name4, EinsatzEintragenAO.this.fahrzeug_label[erstFahrzeug].getName(), bericht_funkname2, bericht_funkname3, bericht_funkname4, EinsatzEintragenAO.this.zeitAusgerueckt[erstFahrzeug].getText(), bericht_ausruecken2, bericht_ausruecken3, bericht_ausruecken4, EinsatzEintragenAO.this.zeitEingetroffen[erstFahrzeug].getText(), bericht_eingetroffen2, bericht_eingetroffen3, bericht_eingetroffen4, EinsatzEintragenAO.this.zeitEingerueckt[erstFahrzeug].getText(), bericht_einsatzEnde2, bericht_einsatzEnde3, bericht_einsatzEnde4, TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(EinsatzEintragenAO.this.zeitAlarm.getText(), EinsatzEintragenAO.this.zeitEingerueckt[erstFahrzeug].getText())), bericht_EinsatzZeit2, bericht_EinsatzZeit3, bericht_EinsatzZeit4, Utils.checkTextAndRemoveIllegalSigns(tabMitglied.getEinsatzleiter(einsatzleiterID)), Utils.checkTextAndRemoveIllegalSigns(EinsatzEintragenAO.this.einsatzleiterBF.getSelectedItem().toString()), SbcUtils.timeStamp((String)"dd.MM.yyyy")};
                            XML.createEinsatzBericht(ist, zu, dateiname, runApplication.EINSTELLUNGEN.get("EinsatzBericht"));
                            logging.logInfo((Object)"Erstelle DOC-Datei aus dem Einsatzbericht");
                            File docFile = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + EinsatzEintragenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + EinsatzEintragenAO.this.einsatzNummerIntern.getText() + "_ID_" + EinsatzEintragenAO.this.einsatzNummerOffiziell.getText() + ".doc");
                            new File(dateiname).renameTo(docFile);
                            bericht.setId(tabEinsatzBeicht.getNextNummer());
                            bericht.setEinsatzNummer(Integer.parseInt(EinsatzEintragenAO.this.einsatzNummerIntern.getText()));
                            bericht.setVeranstaltungID(tabVeransatltung.getVeranstaltungID(nameVeransatltung));
                            bericht.setJahr(Integer.parseInt(EinsatzEintragenAO.this.datum.getText().substring(6, 10)));
                            bericht.setDateiname("Einsatz_ID_" + EinsatzEintragenAO.this.einsatzNummerIntern.getText() + "_ID_" + EinsatzEintragenAO.this.einsatzNummerOffiziell.getText() + ".doc");
                            tabEinsatzBeicht.insert(bericht);
                            Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + EinsatzEintragenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + EinsatzEintragenAO.this.einsatzNummerIntern.getText() + "_ID_" + EinsatzEintragenAO.this.einsatzNummerOffiziell.getText() + ".doc");
                            logging.logInfo((Object)"Einsatzbericht erfolgreich angelegt");
                        } else if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Eigene Vorlage / Dateienvorlage")) {
                            String ext = FilenameUtils.getExtension((String)runApplication.EINSTELLUNGEN.get("EinsatzBericht"));
                            Path input = Paths.get(runApplication.EINSTELLUNGEN.get("EinsatzBericht"), new String[0]);
                            Path output = Paths.get(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + EinsatzEintragenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + EinsatzEintragenAO.this.einsatzNummerIntern.getText() + "_ID_" + EinsatzEintragenAO.this.einsatzNummerOffiziell.getText() + "." + ext, new String[0]);
                            Files.copy(input, output, StandardCopyOption.REPLACE_EXISTING);
                            logging.logInfo((Object)"Kopieren des Einsatzberichtes abgeschlossen");
                            bericht.setId(tabEinsatzBeicht.getNextNummer());
                            bericht.setEinsatzNummer(Integer.parseInt(EinsatzEintragenAO.this.einsatzNummerIntern.getText()));
                            bericht.setVeranstaltungID(tabVeransatltung.getVeranstaltungID(nameVeransatltung));
                            bericht.setJahr(Integer.parseInt(EinsatzEintragenAO.this.datum.getText().substring(6, 10)));
                            bericht.setDateiname("Einsatz_ID_" + EinsatzEintragenAO.this.einsatzNummerIntern.getText() + "_ID_" + EinsatzEintragenAO.this.einsatzNummerOffiziell.getText() + "." + ext);
                            tabEinsatzBeicht.insert(bericht);
                            Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + EinsatzEintragenAO.this.datum.getText().substring(6, 10) + "/einsatzberichte/Einsatz_ID_" + EinsatzEintragenAO.this.einsatzNummerIntern.getText() + "_ID_" + EinsatzEintragenAO.this.einsatzNummerOffiziell.getText() + "." + ext);
                        }
                        logbuchEingabe.NeuerEintag("Einsatz erstellt: " + nameVeransatltung);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                    catch (IOException | SQLException e) {
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonAnwesenheit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzEintragenAO.this.dispose();
                logging.logInfo((Object)"Schlie\u00dfe EinsatzEintragenAO und Starte: AnwesenheitEintargenAO");
                MyEvent.setEvent((String)"0x0010");
                Steuerung.setStatus(Status.ANWESENHEIT_EINTRAGEN);
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

    private ItemListener createItemListener(final int index) {
        ItemListener result = new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (EinsatzEintragenAO.this.jCheckboxArray[index].isSelected()) {
                    logging.logInfo((Object)("Fahrzeug " + index + 1 + " wurde makiert"));
                    EinsatzEintragenAO.this.zeitAusgerueckt[index].setVisible(true);
                    EinsatzEintragenAO.this.zeitAusgerueckt_label[index].setVisible(true);
                    if (runApplication.EINSTELLUNGEN.get("feldEintreffenAusblenden").equals("0")) {
                        EinsatzEintragenAO.this.zeitEingetroffen[index].setVisible(true);
                        EinsatzEintragenAO.this.zeitEingetroffen_label[index].setVisible(true);
                    }
                    EinsatzEintragenAO.this.zeitEingerueckt[index].setVisible(true);
                    EinsatzEintragenAO.this.zeitEingerueckt_label[index].setVisible(true);
                } else {
                    EinsatzEintragenAO.this.zeitAusgerueckt[index].setVisible(false);
                    EinsatzEintragenAO.this.zeitAusgerueckt_label[index].setVisible(false);
                    EinsatzEintragenAO.this.zeitEingetroffen[index].setVisible(false);
                    EinsatzEintragenAO.this.zeitEingetroffen_label[index].setVisible(false);
                    EinsatzEintragenAO.this.zeitEingerueckt[index].setVisible(false);
                    EinsatzEintragenAO.this.zeitEingerueckt_label[index].setVisible(false);
                }
            }
        };
        return result;
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

