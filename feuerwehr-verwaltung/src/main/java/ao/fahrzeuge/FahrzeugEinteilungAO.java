/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.fahrzeuge;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeugeinteilung;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Fahrzeugeinteilung;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import logging.logging;
import pdfdocumente.FarzeugeinteilungPDFSchreiben;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.PDFPrinter;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.fahrzeugeinteilung.RegelUtilities;
import utilities.logbuchEingabe;

public class FahrzeugEinteilungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonDrucken;
    public static String currendChange = null;
    public static JLabel konflikt_label;
    public static JTextArea textfield;
    public static JScrollPane pane;
    public static JComboBox[][] sitzplatz;
    public static JLabel[][] sitzplatz_label;
    public static StringBuilder build;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public FahrzeugEinteilungAO() {
        super("FeuerwehrManagementSystem - Fahrzeugeinteilung");
        logging.logInfo((Object)"Starte: FahrzeugEinteilungAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        textfield = new JTextArea(14, 13);
        textfield.setEditable(false);
        textfield.setLineWrap(true);
        textfield.setWrapStyleWord(true);
        pane = new JScrollPane(textfield);
        pane.setVerticalScrollBarPolicy(22);
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        TitledBorder titleBorderTextPane = BorderFactory.createTitledBorder(lowerEtched, "\u00dcbrige Personen");
        pane.setBorder(titleBorderTextPane);
        konflikt_label = new JLabel("");
        this.modulBeschreibung = new JLabel("Fahrzeugeinteilung f\u00fcr " + runApplication.letzterVeranstaltungsname);
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
            if (tabFahrzeug.countWithoutAnhaenger() >= 6 && tabFahrzeug.countWithoutAnhaenger() <= 8) {
                logging.logInfo((Object)"Setze GUI 1150x950");
                this.setSize(1150, 950);
            } else if (tabFahrzeug.countWithoutAnhaenger() >= 4 && tabFahrzeug.countWithoutAnhaenger() <= 6) {
                logging.logInfo((Object)"Setze GUI 1150x650");
                this.setSize(1150, 650);
            } else {
                logging.logInfo((Object)"Setze GUI 11150x400");
                this.setSize(1150, 400);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.setTitle("FeuerwehrManagementSystem - Fahrzeugeinteilung");
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        try {
            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
            String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
            int x = 0;
            while (x < fahrzeugListe.length) {
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
                Border lowerEtched = BorderFactory.createEtchedBorder(1);
                TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, fahrzeugListe[x].toString());
                panelFahrzeug.setBorder(title);
                ++x;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(pane);
        this.add(konflikt_label);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonDrucken);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
        RegelUtilities.BerechneFahrzeugeinteilung();
    }

    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                FahrzeugEinteilungAO.this.buttonZurueck.doClick();
            }
        });
        this.buttonZurueck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (FahrzeugEinteilungAO.this.buttonSpeichern.isEnabled()) {
                    int msg = JOptionPane.showConfirmDialog(null, Konstante.WIRKLICH_SCHLIESSEN, "Frage", 0);
                    if (msg == 0) {
                        runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                        FahrzeugEinteilungAO.this.dispose();
                    }
                } else {
                    runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                    FahrzeugEinteilungAO.this.dispose();
                }
            }
        });
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (FahrzeugEinteilungAO.this.buttonSpeichern.isEnabled()) {
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ZUERST, "Warnung", 2);
                    } else {
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int vID = tabVeranstaltung.getVeranstaltungID(runApplication.letzterVeranstaltungsname);
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/fahrzeugeinteilung/" + tabVeranstaltung.getVeranstaltungName2AndDatum(vID) + "_ID_" + vID + ".pdf";
                        new PDFPrinter(dateiname);
                        JOptionPane.showMessageDialog(null, Konstante.DRUCKAUFTRAG_VERSENDET);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                if (!FahrzeugEinteilungAO.this.buttonSpeichern.isEnabled()) {
                    runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                }
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleFahrzeugeinteilung tabEinteilung = new TabelleFahrzeugeinteilung();
                Fahrzeugeinteilung einteilung = new Fahrzeugeinteilung();
                try {
                    int vID = tabVeranstaltung.getVeranstaltungID(runApplication.letzterVeranstaltungsname);
                    int jahr = tabVeranstaltung.getJahrDerVeranstaltung(vID);
                    int kID = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                    String[] fahrzeugListe = Utils.listToArray(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());
                    if (!konflikt_label.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_KONFILIKT_BESEITIGEN, "Fehlermeldung", 0);
                    } else {
                        int f = 0;
                        while (f < fahrzeugListe.length) {
                            int fID = tabFahrzeuge.getFahrzeugID(fahrzeugListe[f]);
                            int currentFahrzeugIsTrupp = tabFahrzeuge.getTrupp(fID);
                            int s = 0;
                            while (s < 9) {
                                if (!(sitzplatz[f][s].getSelectedItem().toString().equals("<bitte w\u00e4hlen>") | sitzplatz[f][s].getSelectedItem().toString().equals(null))) {
                                    einteilung.setId(tabEinteilung.getNextNumer());
                                    einteilung.setVeranstaltungID(vID);
                                    einteilung.setKategorie(kID);
                                    einteilung.setJahr(jahr);
                                    einteilung.setFahrzeugID(fID);
                                    einteilung.setMitgliederID(tabMitglied.getIdByGuiString(sitzplatz[f][s].getSelectedItem().toString()));
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
                        Utils.dateiKatalogisieren(dateiname);
                        FarzeugeinteilungPDFSchreiben.PDFdocumentErstellen(dateiname);
                        logging.logInfo((Object)"fahrzeugeinteilung erfolgreich gespeichert");
                        tabVeranstaltung.updateFahrzeugeinteilung(vID);
                        logging.logInfo((Object)"Veranstaltungstabelle aktualisiert, das die Fahrzeugeinteilung erfolgreich erstellt wurde");
                        logbuchEingabe.NeuerEintag("Fahrzeugeinteilung erstellt: " + runApplication.letzterVeranstaltungsname + " Details: " + dateiname);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        FahrzeugEinteilungAO.this.setDefaultCloseOperation(2);
                        FahrzeugEinteilungAO.this.buttonSpeichern.setEnabled(false);
                    }
                }
                catch (DocumentException | IOException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    public void fensterAnzeigen() {
        MyEvent.setEvent((String)"0x0030");
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        if (!build.toString().equals(Konstante.FAHRZEUGEINTEILUNG_PROBLEME)) {
            logging.logInfo((Object)"Warnung wird angezeigt");
            JOptionPane.showMessageDialog(null, build.toString(), "Warnung", 2);
        }
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

