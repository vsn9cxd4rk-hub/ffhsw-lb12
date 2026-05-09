/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.fahrzeuge;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeugeinteilung;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Fahrzeugeinteilung;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import logging.logging;
import pdfdocumente.FahrzeugbelegungPDFSchreiben;
import run.runApplication;
import utilities.Konstante;
import utilities.PDFPrinter;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.XML;
import utilities.logbuchEingabe;

public class FahrzeugBelegungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonDrucken;
    public static String currendChange = null;
    private static JLabel konflikt_label;
    private static String currentVeranstaltung;
    public static JComboBox[][] sitzplatz;
    public static JLabel[][] sitzplatz_label;
    public static StringBuilder build;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public FahrzeugBelegungAO() {
        super("FeuerwehrManagementSystem - Fahrzeugbelegung");
        logging.logInfo((Object)"Starte: FahrzeugBelegungAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        konflikt_label = new JLabel("");
        currentVeranstaltung = runApplication.letzterVeranstaltungsname;
        logging.logInfo((Object)("Aktuelle Veranstaltung f\u00fcr Fahrzeugbelegung: " + currentVeranstaltung));
        runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
        this.modulBeschreibung = new JLabel("Fahrzeugbelegung f\u00fcr " + currentVeranstaltung);
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
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
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        try {
            if (tabFahrzeug.countWithoutAnhaenger() >= 7 && tabFahrzeug.countWithoutAnhaenger() <= 9) {
                logging.logInfo((Object)"Setze GUI 1150x960");
                this.setSize(1150, 960);
            } else if (tabFahrzeug.countWithoutAnhaenger() >= 4 && tabFahrzeug.countWithoutAnhaenger() <= 6) {
                logging.logInfo((Object)"Setze GUI 1150x660");
                this.setSize(1150, 660);
            } else {
                logging.logInfo((Object)"Setze GUI 1150x400");
                this.setSize(1150, 400);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.setTitle("FeuerwehrManagementSystem - Fahrzeugbelegung");
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        try {
            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
            TabelleFahrzeugeinteilung tabFahrzeugeinteilung = new TabelleFahrzeugeinteilung();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
            TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
            int vID = tabVeranstaltung.getVeranstaltungID(currentVeranstaltung);
            String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
            String[] fahrzeugIDListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeIDsOhneAnhaenger());
            String[] fahrzeugBeschreibungListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugBeschreibungOhneAnhaenger());
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabAnwesenheit.getAnwesendeMitgliederByVeranstaltung(tabVeranstaltung.getVeranstaltungID(currentVeranstaltung)));
            sitzplatz = new JComboBox[fahrzeugListe.length][9];
            sitzplatz_label = new JLabel[fahrzeugListe.length][9];
            int x = 0;
            while (x < fahrzeugListe.length) {
                logging.logInfo((Object)("Aktuelles Fahrzeug == " + fahrzeugListe[x]));
                int currentFahrzeugIsTrupp = tabFahrzeug.getTrupp(Integer.parseInt(fahrzeugIDListe[x]));
                logging.logInfo((Object)("Aktuelles Fahrzeug ist ein Truppfahrzeug == " + currentFahrzeugIsTrupp));
                if (fahrzeugBeschreibungListe[x].equals("13") || fahrzeugBeschreibungListe[x].equals("14")) {
                    FahrzeugBelegungAO.sitzplatz_label[x][0] = new JLabel("Transportf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][1] = new JLabel("Fahrzeugf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][2] = new JLabel("Sitzplatz 1: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][3] = new JLabel("Sitzplatz 2: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][4] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][5] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][6] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][7] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][8] = new JLabel("Frei: ");
                } else if (fahrzeugBeschreibungListe[x].equals("4") || fahrzeugBeschreibungListe[x].equals("5")) {
                    FahrzeugBelegungAO.sitzplatz_label[x][0] = new JLabel("Leiterf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][1] = new JLabel("Maschinist: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][2] = new JLabel("Truppmann: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][3] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][4] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][5] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][6] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][7] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][8] = new JLabel("Frei: ");
                } else if (fahrzeugBeschreibungListe[x].equals("12")) {
                    FahrzeugBelegungAO.sitzplatz_label[x][0] = new JLabel("Zugf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][1] = new JLabel("Fahrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][2] = new JLabel("Sitzplatz 1: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][3] = new JLabel("Sitzplatz 2: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][4] = new JLabel("Sitzplatz 3: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][5] = new JLabel("Sitzplatz 4: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][6] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][7] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][8] = new JLabel("Frei: ");
                } else if (fahrzeugBeschreibungListe[x].equals("6") | fahrzeugBeschreibungListe[x].equals("9") | fahrzeugBeschreibungListe[x].equals("7")) {
                    if (currentFahrzeugIsTrupp == 0) {
                        FahrzeugBelegungAO.sitzplatz_label[x][0] = new JLabel("Fahrzeugf\u00fchrer: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][1] = new JLabel("Fahrer / Maschinist: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][2] = new JLabel("Sitzplatz 1: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][3] = new JLabel("Sitzplatz 2: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][4] = new JLabel("Sitzplatz 3: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][5] = new JLabel("Sitzplatz 4: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][6] = new JLabel("Sitzplatz 5: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][7] = new JLabel("Sitzplatz 6: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][8] = new JLabel("Sitzplatz 7: ");
                    } else {
                        FahrzeugBelegungAO.sitzplatz_label[x][0] = new JLabel("Truppf\u00fchrer: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][1] = new JLabel("Fahrer / Maschinist: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][2] = new JLabel("Truppmann: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][3] = new JLabel("Frei: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][4] = new JLabel("Frei: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][5] = new JLabel("Frei: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][6] = new JLabel("Frei: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][7] = new JLabel("Frei: ");
                        FahrzeugBelegungAO.sitzplatz_label[x][8] = new JLabel("Frei: ");
                    }
                } else if (currentFahrzeugIsTrupp == 0) {
                    logging.logInfo((Object)"Erstelle Fahrzeug mit Gruppen- oder Staffelbesatzung...");
                    FahrzeugBelegungAO.sitzplatz_label[x][0] = new JLabel("Gruppenf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][1] = new JLabel("Maschinist: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][2] = new JLabel("Angriffstruppf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][3] = new JLabel("Angriffstruppmann: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][4] = new JLabel("Wassertruppf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][5] = new JLabel("Wassertruppmann: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][6] = new JLabel("Schlauchtruppf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][7] = new JLabel("Schlauchtruppmann: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][8] = new JLabel("Melder: ");
                } else {
                    logging.logInfo((Object)"Erstelle Fahrzeug mit Truppbesatzung...");
                    FahrzeugBelegungAO.sitzplatz_label[x][0] = new JLabel("Truppf\u00fchrer: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][1] = new JLabel("Maschinist: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][2] = new JLabel("Truppmann: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][3] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][4] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][5] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][6] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][7] = new JLabel("Frei: ");
                    FahrzeugBelegungAO.sitzplatz_label[x][8] = new JLabel("Frei: ");
                }
                FahrzeugBelegungAO.sitzplatz[x][0] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][1] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][2] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][3] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][4] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][5] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][6] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][7] = new JComboBox<String>(mitgliederListe);
                FahrzeugBelegungAO.sitzplatz[x][8] = new JComboBox<String>(mitgliederListe);
                JPanel panelFahrzeug = new JPanel(new GridLayout(9, 2));
                this.getContentPane().add("Center", panelFahrzeug);
                panelFahrzeug.add(sitzplatz_label[x][0]);
                panelFahrzeug.add(sitzplatz[x][0]);
                panelFahrzeug.add(sitzplatz_label[x][1]);
                panelFahrzeug.add(sitzplatz[x][1]);
                panelFahrzeug.add(sitzplatz_label[x][2]);
                panelFahrzeug.add(sitzplatz[x][2]);
                panelFahrzeug.add(sitzplatz_label[x][3]);
                panelFahrzeug.add(sitzplatz[x][3]);
                panelFahrzeug.add(sitzplatz_label[x][4]);
                panelFahrzeug.add(sitzplatz[x][4]);
                panelFahrzeug.add(sitzplatz_label[x][5]);
                panelFahrzeug.add(sitzplatz[x][5]);
                panelFahrzeug.add(sitzplatz_label[x][6]);
                panelFahrzeug.add(sitzplatz[x][6]);
                panelFahrzeug.add(sitzplatz_label[x][7]);
                panelFahrzeug.add(sitzplatz[x][7]);
                panelFahrzeug.add(sitzplatz_label[x][8]);
                panelFahrzeug.add(sitzplatz[x][8]);
                int anzahlSitzplaetze = tabFahrzeug.getSitzplatz(Integer.parseInt(fahrzeugIDListe[x]));
                int i = 8;
                while (i >= anzahlSitzplaetze) {
                    sitzplatz_label[x][i].setVisible(false);
                    sitzplatz[x][i].setVisible(false);
                    --i;
                }
                Border lowerEtched = BorderFactory.createEtchedBorder(1);
                TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, fahrzeugListe[x].toString());
                panelFahrzeug.setBorder(title);
                sitzplatz[x][0].addItemListener(FahrzeugBelegungAO.createItemListener(x, 0, fahrzeugListe));
                sitzplatz[x][1].addItemListener(FahrzeugBelegungAO.createItemListener(x, 1, fahrzeugListe));
                sitzplatz[x][2].addItemListener(FahrzeugBelegungAO.createItemListener(x, 2, fahrzeugListe));
                sitzplatz[x][3].addItemListener(FahrzeugBelegungAO.createItemListener(x, 3, fahrzeugListe));
                sitzplatz[x][4].addItemListener(FahrzeugBelegungAO.createItemListener(x, 4, fahrzeugListe));
                sitzplatz[x][5].addItemListener(FahrzeugBelegungAO.createItemListener(x, 5, fahrzeugListe));
                sitzplatz[x][6].addItemListener(FahrzeugBelegungAO.createItemListener(x, 6, fahrzeugListe));
                sitzplatz[x][7].addItemListener(FahrzeugBelegungAO.createItemListener(x, 7, fahrzeugListe));
                sitzplatz[x][8].addItemListener(FahrzeugBelegungAO.createItemListener(x, 8, fahrzeugListe));
                if (tabFahrzeugeinteilung.getCountOfVeranstaltung(vID) != 0) {
                    String[] positionListe = Utils.listToArray(tabFahrzeugeinteilung.getPositionListe(fahrzeugIDListe[x], vID));
                    if (currentFahrzeugIsTrupp == 0) {
                        int i2 = 0;
                        while (i2 < positionListe.length) {
                            sitzplatz[x][i2].setSelectedItem(positionListe[i2]);
                            ++i2;
                        }
                    } else {
                        try {
                            sitzplatz[x][1].setSelectedItem(positionListe[0]);
                            sitzplatz[x][0].setSelectedItem(positionListe[1]);
                            sitzplatz[x][2].setSelectedItem(positionListe[2]);
                        }
                        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
                            // empty catch block
                        }
                    }
                }
                if (tabZeiten.getCount(Integer.parseInt(fahrzeugIDListe[x]), vID) == 0) {
                    int i3 = 0;
                    while (i3 < 9) {
                        sitzplatz_label[x][i3].setVisible(false);
                        sitzplatz[x][i3].setVisible(false);
                        ++i3;
                    }
                    sitzplatz_label[x][0].setText("Fahrzeug nicht im Einsatz!");
                    sitzplatz_label[x][0].setVisible(true);
                }
                ++x;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(konflikt_label);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonDrucken);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                FahrzeugBelegungAO.this.buttonZurueck.doClick();
            }
        });
        this.buttonZurueck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (FahrzeugBelegungAO.this.buttonSpeichern.isEnabled()) {
                    int msg = JOptionPane.showConfirmDialog(null, Konstante.WIRKLICH_SCHLIESSEN, "Frage", 0);
                    if (msg == 0) {
                        runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                        FahrzeugBelegungAO.this.dispose();
                    }
                } else {
                    runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                    FahrzeugBelegungAO.this.dispose();
                }
            }
        });
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (FahrzeugBelegungAO.this.buttonSpeichern.isEnabled()) {
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ZUERST, "Warnung", 2);
                    } else {
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int vID = tabVeranstaltung.getVeranstaltungID(currentVeranstaltung);
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/fahrzeugeinteilung/" + tabVeranstaltung.getVeranstaltungName2AndDatum(vID) + "_ID_" + vID + ".pdf";
                        new PDFPrinter(dateiname);
                        JOptionPane.showMessageDialog(null, Konstante.DRUCKAUFTRAG_VERSENDET);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                if (!FahrzeugBelegungAO.this.buttonSpeichern.isEnabled()) {
                    runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                }
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                block92: {
                    TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                    TabelleFahrzeugeinteilung tabEinteilung = new TabelleFahrzeugeinteilung();
                    Fahrzeugeinteilung einteilung = new Fahrzeugeinteilung();
                    TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                    try {
                        int vID = tabVeranstaltung.getVeranstaltungID(currentVeranstaltung);
                        int jahr = tabVeranstaltung.getJahrDerVeranstaltung(vID);
                        int kID = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                        String[] fahrzeugListe = Utils.listToArray(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());
                        if (!konflikt_label.getText().equals("")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_KONFILIKT_BESEITIGEN, "Fehlermeldung", 0);
                            break block92;
                        }
                        if (tabEinteilung.getCountOfVeranstaltung(vID) != 0) {
                            tabEinteilung.delete(vID);
                            logging.logInfo((Object)"Altdaten aus der Fehrzeugeinteilung gel\u00f6scht...");
                            logging.logInfo((Object)"F\u00fcge neue Daten hinzu!");
                        }
                        int f = 0;
                        while (f < fahrzeugListe.length) {
                            int fID = tabFahrzeuge.getFahrzeugID(fahrzeugListe[f]);
                            int currentFahrzeugIsTrupp = tabFahrzeuge.getTrupp(fID);
                            int s = 0;
                            while (s < 9) {
                                if (!(sitzplatz[f][s].getSelectedItem().toString().equals("<bitte w\u00e4hlen>") | sitzplatz[f][s].getSelectedItem().toString().equals(null))) {
                                    int mID = tabMitglied.getIdByGuiString(sitzplatz[f][s].getSelectedItem().toString());
                                    einteilung.setId(tabEinteilung.getNextNumer());
                                    einteilung.setVeranstaltungID(vID);
                                    einteilung.setKategorie(kID);
                                    einteilung.setJahr(jahr);
                                    einteilung.setFahrzeugID(fID);
                                    einteilung.setMitgliederID(mID);
                                    if (currentFahrzeugIsTrupp == 0) {
                                        einteilung.setPosition(s);
                                    } else if (s == 0) {
                                        einteilung.setPosition(2);
                                    } else if (s == 1) {
                                        einteilung.setPosition(1);
                                    } else if (s == 2) {
                                        einteilung.setPosition(3);
                                    }
                                    tabEinteilung.insert(einteilung);
                                }
                                ++s;
                            }
                            ++f;
                        }
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/fahrzeugeinteilung/" + tabVeranstaltung.getVeranstaltungName2AndDatum(vID) + "_ID_" + vID + ".pdf";
                        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtFahrzeugbelegungHinzuf\u00fcgen").equals("1") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle") && new TabelleEinsatz_bericht().getFahrzeugbelegungStatus(vID) == 0) {
                            TabelleEinsatz_bericht tabBericht = new TabelleEinsatz_bericht();
                            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                            HashMap<String, String> map = tabEinsatz.getData(vID);
                            String dateinameDoc = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/" + tabBericht.getDateiname(vID);
                            String dateinameXml = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + ".xml";
                            String dateinameXmlNeu = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + "_Neu.xml";
                            new File(dateinameDoc).renameTo(new File(dateinameXml));
                            logging.logInfo((Object)"Fahrzeugbelegung: benenne DOC --> XML um...");
                            logging.logInfo((Object)("DOC-Dateiname: " + dateinameDoc));
                            logging.logInfo((Object)("XML-Dateiname: " + dateinameXml));
                            int erstFahrzeug = -1;
                            int zweitFahrzeug = -1;
                            int drittFahrzeug = -1;
                            int viertFahrzeug = -1;
                            boolean erstFahrzeugisTrupp = false;
                            boolean zweitFahrzeugisTrupp = false;
                            boolean drittFahrzeugisTrupp = false;
                            boolean viertFahrzeugisTrupp = false;
                            int fahrzeugCount = tabFahrzeug.countWithoutAnhaenger();
                            String[] fahrzeugIDListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeIDsOhneAnhaenger());
                            int x = 0;
                            while (x < fahrzeugCount) {
                                if (!sitzplatz_label[x][0].getText().equals("Fahrzeug nicht im Einsatz!") && erstFahrzeug == -1) {
                                    erstFahrzeug = x;
                                    logging.logInfo((Object)("Fahrzeug 1 im Einsatz - fID = " + erstFahrzeug));
                                    if (tabFahrzeug.getTrupp(Integer.parseInt(fahrzeugIDListe[x])) == 1) {
                                        erstFahrzeugisTrupp = true;
                                        logging.logInfo((Object)"Fahrzeug 1 im Einsatz - ist ein Truppfahrzeug");
                                    }
                                } else if (!sitzplatz_label[x][0].getText().equals("Fahrzeug nicht im Einsatz!") && zweitFahrzeug == -1 && erstFahrzeug != -1) {
                                    zweitFahrzeug = x;
                                    logging.logInfo((Object)("Fahrzeug 2 im Einsatz - fID = " + zweitFahrzeug));
                                    if (tabFahrzeug.getTrupp(Integer.parseInt(fahrzeugIDListe[x])) == 1) {
                                        zweitFahrzeugisTrupp = true;
                                        logging.logInfo((Object)"Fahrzeug 2 im Einsatz - ist ein Truppfahrzeug");
                                    }
                                } else if (!sitzplatz_label[x][0].getText().equals("Fahrzeug nicht im Einsatz!") && zweitFahrzeug != -1 && erstFahrzeug != -1 && drittFahrzeug == -1) {
                                    drittFahrzeug = x;
                                    logging.logInfo((Object)("Fahrzeug 3 im Einsatz - fID = " + drittFahrzeug));
                                    if (tabFahrzeug.getTrupp(Integer.parseInt(fahrzeugIDListe[x])) == 1) {
                                        drittFahrzeugisTrupp = true;
                                        logging.logInfo((Object)"Fahrzeug 3 im Einsatz - ist ein Truppfahrzeug");
                                    }
                                } else if (!sitzplatz_label[x][0].getText().equals("Fahrzeug nicht im Einsatz!") && zweitFahrzeug != -1 && erstFahrzeug != -1 && drittFahrzeug != -1 && viertFahrzeug == -1) {
                                    viertFahrzeug = x;
                                    logging.logInfo((Object)("Fahrzeug 4 im Einsatz - fID = " + viertFahrzeug));
                                    if (tabFahrzeug.getTrupp(Integer.parseInt(fahrzeugIDListe[x])) != 1) break;
                                    viertFahrzeugisTrupp = true;
                                    logging.logInfo((Object)"Fahrzeug 4 im Einsatz - ist ein Truppfahrzeug");
                                    break;
                                }
                                ++x;
                            }
                            String[] ist = new String[]{"F1PO0", "F1PO1", "F1PO2", "F1PO3", "F1PO4", "F1PO5", "F1PO6", "F1PO7", "F1PO8", "F2PO0", "F2PO1", "F2PO2", "F2PO3", "F2PO4", "F2PO5", "F2PO6", "F2PO7", "F2PO8", "F3PO0", "F3PO1", "F3PO2", "F3PO3", "F3PO4", "F3PO5", "F3PO6", "F3PO7", "F3PO8", "F4PO0", "F4PO1", "F4PO2", "F4PO3", "F4PO4", "F4PO5", "F4PO6", "F4PO7", "F4PO8", "GH000", "GH001", "GH002", "GH003", "GH004", "GH005", "GH006", "GH007", "GH008", "GH009"};
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
                            if (erstFahrzeug != -1) {
                                if (!erstFahrzeugisTrupp) {
                                    if (!sitzplatz[erstFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos0 = sitzplatz[erstFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos1 = sitzplatz[erstFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos2 = sitzplatz[erstFahrzeug][2].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][3].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos3 = sitzplatz[erstFahrzeug][3].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][4].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos4 = sitzplatz[erstFahrzeug][4].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][5].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos5 = sitzplatz[erstFahrzeug][5].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][6].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos6 = sitzplatz[erstFahrzeug][6].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][7].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos7 = sitzplatz[erstFahrzeug][7].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][8].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos8 = sitzplatz[erstFahrzeug][8].getSelectedItem().toString();
                                    }
                                } else if (erstFahrzeugisTrupp) {
                                    if (!sitzplatz[erstFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos2 = sitzplatz[erstFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos1 = sitzplatz[erstFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[erstFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr1pos3 = sitzplatz[erstFahrzeug][2].getSelectedItem().toString();
                                    }
                                }
                            }
                            if (zweitFahrzeug != -1) {
                                if (!zweitFahrzeugisTrupp) {
                                    if (!sitzplatz[zweitFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos0 = sitzplatz[zweitFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos1 = sitzplatz[zweitFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos2 = sitzplatz[zweitFahrzeug][2].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][3].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos3 = sitzplatz[zweitFahrzeug][3].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][4].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos4 = sitzplatz[zweitFahrzeug][4].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][5].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos5 = sitzplatz[zweitFahrzeug][5].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][6].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos6 = sitzplatz[zweitFahrzeug][6].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][7].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos7 = sitzplatz[zweitFahrzeug][7].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][8].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos8 = sitzplatz[zweitFahrzeug][8].getSelectedItem().toString();
                                    }
                                } else if (zweitFahrzeugisTrupp) {
                                    if (!sitzplatz[zweitFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos2 = sitzplatz[zweitFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos1 = sitzplatz[zweitFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[zweitFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr2pos3 = sitzplatz[zweitFahrzeug][2].getSelectedItem().toString();
                                    }
                                }
                            }
                            if (drittFahrzeug != -1) {
                                if (!drittFahrzeugisTrupp) {
                                    if (!sitzplatz[drittFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos0 = sitzplatz[drittFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos1 = sitzplatz[drittFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos2 = sitzplatz[drittFahrzeug][2].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][3].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos3 = sitzplatz[drittFahrzeug][3].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][4].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos4 = sitzplatz[drittFahrzeug][4].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][5].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos5 = sitzplatz[drittFahrzeug][5].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][6].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos6 = sitzplatz[drittFahrzeug][6].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][7].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos7 = sitzplatz[drittFahrzeug][7].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][8].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos8 = sitzplatz[drittFahrzeug][8].getSelectedItem().toString();
                                    }
                                } else if (drittFahrzeugisTrupp) {
                                    if (!sitzplatz[drittFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos2 = sitzplatz[drittFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos1 = sitzplatz[drittFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[drittFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr3pos3 = sitzplatz[drittFahrzeug][2].getSelectedItem().toString();
                                    }
                                }
                            }
                            if (viertFahrzeug != -1) {
                                if (!viertFahrzeugisTrupp) {
                                    if (!sitzplatz[viertFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos0 = sitzplatz[viertFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos1 = sitzplatz[viertFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos2 = sitzplatz[viertFahrzeug][2].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][3].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos3 = sitzplatz[viertFahrzeug][3].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][4].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos4 = sitzplatz[viertFahrzeug][4].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][5].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos5 = sitzplatz[viertFahrzeug][5].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][6].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos6 = sitzplatz[viertFahrzeug][6].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][7].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos7 = sitzplatz[viertFahrzeug][7].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][8].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos8 = sitzplatz[viertFahrzeug][8].getSelectedItem().toString();
                                    }
                                } else if (viertFahrzeugisTrupp) {
                                    if (!sitzplatz[viertFahrzeug][0].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos2 = sitzplatz[viertFahrzeug][0].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][1].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos1 = sitzplatz[viertFahrzeug][1].getSelectedItem().toString();
                                    }
                                    if (!sitzplatz[viertFahrzeug][2].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                        fahr4pos3 = sitzplatz[viertFahrzeug][2].getSelectedItem().toString();
                                    }
                                }
                            }
                            String[] kamerandenImEinsatz = Utils.listToArray(tabEinteilung.getEingeteilteKameraden(vID));
                            String[] kameradenImGH = Utils.listToArray(tabAnwesenheit.getNichtInFahrzeugeinteilung(vID, kamerandenImEinsatz));
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
                            String[] zu = new String[]{Utils.checkTextAndRemoveIllegalSigns(fahr1pos0), Utils.checkTextAndRemoveIllegalSigns(fahr1pos1), Utils.checkTextAndRemoveIllegalSigns(fahr1pos2), Utils.checkTextAndRemoveIllegalSigns(fahr1pos3), Utils.checkTextAndRemoveIllegalSigns(fahr1pos4), Utils.checkTextAndRemoveIllegalSigns(fahr1pos5), Utils.checkTextAndRemoveIllegalSigns(fahr1pos6), Utils.checkTextAndRemoveIllegalSigns(fahr1pos7), Utils.checkTextAndRemoveIllegalSigns(fahr1pos8), Utils.checkTextAndRemoveIllegalSigns(fahr2pos0), Utils.checkTextAndRemoveIllegalSigns(fahr2pos1), Utils.checkTextAndRemoveIllegalSigns(fahr2pos2), Utils.checkTextAndRemoveIllegalSigns(fahr2pos3), Utils.checkTextAndRemoveIllegalSigns(fahr2pos4), Utils.checkTextAndRemoveIllegalSigns(fahr2pos5), Utils.checkTextAndRemoveIllegalSigns(fahr2pos6), Utils.checkTextAndRemoveIllegalSigns(fahr2pos7), Utils.checkTextAndRemoveIllegalSigns(fahr2pos8), Utils.checkTextAndRemoveIllegalSigns(fahr3pos0), Utils.checkTextAndRemoveIllegalSigns(fahr3pos1), Utils.checkTextAndRemoveIllegalSigns(fahr3pos2), Utils.checkTextAndRemoveIllegalSigns(fahr3pos3), Utils.checkTextAndRemoveIllegalSigns(fahr3pos4), Utils.checkTextAndRemoveIllegalSigns(fahr3pos5), Utils.checkTextAndRemoveIllegalSigns(fahr3pos6), Utils.checkTextAndRemoveIllegalSigns(fahr3pos7), Utils.checkTextAndRemoveIllegalSigns(fahr3pos8), Utils.checkTextAndRemoveIllegalSigns(fahr4pos0), Utils.checkTextAndRemoveIllegalSigns(fahr4pos1), Utils.checkTextAndRemoveIllegalSigns(fahr4pos2), Utils.checkTextAndRemoveIllegalSigns(fahr4pos3), Utils.checkTextAndRemoveIllegalSigns(fahr4pos4), Utils.checkTextAndRemoveIllegalSigns(fahr4pos5), Utils.checkTextAndRemoveIllegalSigns(fahr4pos6), Utils.checkTextAndRemoveIllegalSigns(fahr4pos7), Utils.checkTextAndRemoveIllegalSigns(fahr4pos8), Utils.checkTextAndRemoveIllegalSigns(gh0), Utils.checkTextAndRemoveIllegalSigns(gh1), Utils.checkTextAndRemoveIllegalSigns(gh2), Utils.checkTextAndRemoveIllegalSigns(gh3), Utils.checkTextAndRemoveIllegalSigns(gh4), Utils.checkTextAndRemoveIllegalSigns(gh5), Utils.checkTextAndRemoveIllegalSigns(gh6), Utils.checkTextAndRemoveIllegalSigns(gh7), Utils.checkTextAndRemoveIllegalSigns(gh8), Utils.checkTextAndRemoveIllegalSigns(gh9)};
                            XML.createEinsatzBericht(ist, zu, dateinameXmlNeu, dateinameXml);
                            new File(dateinameXmlNeu).renameTo(new File(dateinameDoc));
                            logging.logInfo((Object)"Fahrzeugbelegung: benenne XML --> DOC um...");
                            new File(dateinameXml).delete();
                            logging.logInfo((Object)"Fahrzeugbelegung: L\u00f6sche Templatefile...");
                            tabBericht.updateFahrzeugbelegung(vID);
                            logging.logInfo((Object)"Bericht Tabelle aktualisiert mit der Fahrzeugbelegung!");
                        }
                        Utils.dateiKatalogisieren(dateiname);
                        FahrzeugbelegungPDFSchreiben.PDFdocumentErstellen(dateiname, currentVeranstaltung);
                        logging.logInfo((Object)"Fahrzeugbelegung erfolgreich gespeichert");
                        logbuchEingabe.NeuerEintag("Fahrzeugbelegung erstellt: " + currentVeranstaltung + " Details: " + dateiname);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        FahrzeugBelegungAO.this.setDefaultCloseOperation(2);
                        FahrzeugBelegungAO.this.buttonSpeichern.setEnabled(false);
                    }
                    catch (DocumentException | IOException | SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
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
                        FahrzeugBelegungAO.changeColor(currendChange, fahrzeugListe);
                        konflikt_label.setText("Konflikt gefunden Position: Fahrzeug: " + (index + 1) + " Position: " + (index2 + 1));
                    } else {
                        logging.logInfo((Object)"Konflikt behoben");
                        FahrzeugBelegungAO.changeColor(" ", fahrzeugListe);
                        konflikt_label.setText("");
                    }
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
                catch (NullPointerException e2) {
                    logging.logWarning((Object)"NullPointerException --> Beim erstellen des Itemlistener, wenn schon daten vorhanden sind");
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

