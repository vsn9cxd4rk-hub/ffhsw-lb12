/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.einsatz;

import ao.AbstractFenster;
import ao.AnwesenheitEintragenAO;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleEinsatz_bericht_daten;
import data.tabellen.TabelleEinsatz_bericht_elemente;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleVeranstaltung;
import go.EinsatzBerichtDaten;
import go.Einsatz_bericht;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.einsatzbericht.EinsatBerichtPDFSchreiben;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class EinsatzBerichtAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonSeite1;
    private JButton buttonSeite2;
    private JLabel veranstaltung_label;
    private JComboBox<String> veranstaltung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JTextField eigent\u00fcmerName;
    private JTextField eigent\u00fcmerAnschrift;
    private JTextField eigent\u00fcmerTelefon;
    private JTextField verursacherName;
    private JTextField verursacherAnschrift;
    private JTextField verursacherTelefon;
    private JTextField meldenderName;
    private JTextField meldenderAnschrift;
    private JTextField meldenderTelefon;
    private JLabel eigent\u00fcmerName_label;
    private JLabel eigent\u00fcmerAnschrift_label;
    private JLabel eigent\u00fcmerTelefon_label;
    private JLabel verursacherName_label;
    private JLabel verursacherAnschrift_label;
    private JLabel verursacherTelefon_label;
    private JLabel meldenderName_label;
    private JLabel meldenderAnschrift_label;
    private JLabel meldenderTelefon_label;
    private JCheckBox[] einsatzArt;
    private JCheckBox[] stelle;
    private JCheckBox[] objekt;
    private JCheckBox[] alamierung;
    private JCheckBox[] ausdehung;
    private JTextArea vorgefundeneLage;
    private JTextArea verlaufT\u00e4tigkeit;
    private JTextArea eingesetzteGer\u00e4te;
    private JScrollPane pane_vorgefundeneLage;
    private JScrollPane pane_verlaufT\u00e4tigkeit;
    private JScrollPane pane_eingesetzteGer\u00e4te;
    private JLabel vorgefundeneLage_label;
    private JLabel verlaufT\u00e4tigkeit_label;
    private JLabel eingesetzteGer\u00e4te_label;
    private JTextField verbrauchWasser;
    private JTextField verbrauchSchaum;
    private JTextField verbrauchPulver;
    private JTextField verbrauch\u00d6lB;
    private JLabel verbrauchWasser_label;
    private JLabel verbrauchSchm_label;
    private JLabel verbrauchPulver_label;
    private JLabel verbrauch\u00d6lB_label;
    private JComboBox<String> brandwacheFahrzeug;
    private JLabel brandwacheFahrzeug_label;
    private JLabel brandwacheSt\u00e4rke_label;
    private JLabel brandwacheDauer_label;
    private JTextField brandwacheSt\u00e4rke;
    private JTextField brandwacheDauer;
    private JCheckBox voreintreffenGel\u00f6scht;
    private JCheckBox schnellAngriff;
    private JLabel vorEintreffenGel\u00f6scht_label;
    private JLabel cRohre_label;
    private JLabel bRohre_label;
    private JLabel kleinesL\u00f6schger\u00e4t_label;
    private JLabel schnellAngriff_label;
    private JTextField cRohre;
    private JTextField bRohre;
    private JTextField kleinesL\u00f6schger\u00e4t;
    private JCheckBox tragbareLeitern;
    private JCheckBox rettungsger\u00e4t;
    private JLabel tragbareLeitern_label;
    private JLabel atemschutzGer\u00e4te_label;
    private JLabel fluchthauben_label;
    private JLabel bel\u00fcftungsger\u00e4t_label;
    private JLabel rettungsger\u00e4t_label;
    private JTextField atemschutzGer\u00e4te;
    private JTextField fluchthauben;
    private JTextField bel\u00fcftungsger\u00e4t;
    private JTextArea entstehungsursache;
    private JLabel entstehungsursache_label;
    private JScrollPane pane_enstehungsursache;
    private JLabel verletzte_label;
    private JLabel tote_label;
    private JLabel gerettete_label;
    private JLabel schadenH\u00f6he_label;
    private JTextField verletzte;
    private JTextField tote;
    private JTextField gerettete;
    private JTextField schadenH\u00f6he;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panel5;
    private JPanel panel6;
    private JPanel panel7;
    private JPanel panel8;
    private JPanel panel9;
    private JPanel panel10;
    private JPanel panel11;
    private JPanel panel12;
    private JPanel panel13;
    private JPanel panel14;
    private JPanel panel15;

    public EinsatzBerichtAO() {
        super("FeuerwehrManagementSystem - Einsatz Bericht");
        logging.logInfo((Object)"Starte: EinsatzBerichtAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern & Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonSeite1 = new JButton("Seite 1");
        this.buttonSeite2 = new JButton("Seite 2");
        this.veranstaltung_label = new JLabel("Veranstaltung: ");
        try {
            String[] veranstaltungListe = Utils.listToArrayOnlyFORComboBoxes(new TabelleVeranstaltung().getAllVeranstaltungEinerKategorie(1));
            this.veranstaltung = new JComboBox<String>(veranstaltungListe);
            if (MyEvent.event.equals("0x0006")) {
                this.veranstaltung.setSelectedItem(AnwesenheitEintragenAO.veranstaltung.getSelectedItem());
                this.veranstaltung.setEnabled(false);
                MyEvent.setEvent((String)"0");
            }
        }
        catch (NumberFormatException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.eigent\u00fcmerName = new JTextField(25);
        this.eigent\u00fcmerAnschrift = new JTextField(25);
        this.eigent\u00fcmerTelefon = new JTextField(25);
        this.verursacherAnschrift = new JTextField(25);
        this.verursacherName = new JTextField(25);
        this.verursacherTelefon = new JTextField(25);
        this.meldenderName = new JTextField(25);
        this.meldenderAnschrift = new JTextField(25);
        this.meldenderTelefon = new JTextField(25);
        this.eigent\u00fcmerName_label = new JLabel("Eigent\u00fcmer / Gesch\u00e4digter Name");
        this.eigent\u00fcmerAnschrift_label = new JLabel("Eigent\u00fcmer / Gesch\u00e4digter Anschrift");
        this.eigent\u00fcmerTelefon_label = new JLabel("Eigent\u00fcmer / Gesch\u00e4digter Telefon");
        this.verursacherAnschrift_label = new JLabel("Verursacher Anschrift: ");
        this.verursacherName_label = new JLabel("Verursacher Name");
        this.verursacherTelefon_label = new JLabel("Verursacher Telefon");
        this.meldenderName_label = new JLabel("Meldender Name: ");
        this.meldenderAnschrift_label = new JLabel("Meldender Anschrift: ");
        this.meldenderTelefon_label = new JLabel("Meldender Telefon: ");
        this.vorgefundeneLage_label = new JLabel("vorgefundene Lage: ");
        this.verlaufT\u00e4tigkeit_label = new JLabel("Verlauf der Einsatzt\u00e4tigleit: ");
        this.eingesetzteGer\u00e4te_label = new JLabel("Eingesetzte Ger\u00e4te: ");
        this.vorgefundeneLage = new JTextArea(10, 20);
        this.verlaufT\u00e4tigkeit = new JTextArea(10, 20);
        this.eingesetzteGer\u00e4te = new JTextArea(10, 20);
        this.pane_vorgefundeneLage = new JScrollPane(this.vorgefundeneLage);
        this.pane_vorgefundeneLage.setVerticalScrollBarPolicy(22);
        this.vorgefundeneLage.setLineWrap(true);
        this.vorgefundeneLage.setWrapStyleWord(true);
        this.pane_verlaufT\u00e4tigkeit = new JScrollPane(this.verlaufT\u00e4tigkeit);
        this.pane_verlaufT\u00e4tigkeit.setVerticalScrollBarPolicy(22);
        this.verlaufT\u00e4tigkeit.setLineWrap(true);
        this.verlaufT\u00e4tigkeit.setWrapStyleWord(true);
        this.pane_eingesetzteGer\u00e4te = new JScrollPane(this.eingesetzteGer\u00e4te);
        this.pane_eingesetzteGer\u00e4te.setVerticalScrollBarPolicy(22);
        this.eingesetzteGer\u00e4te.setLineWrap(true);
        this.eingesetzteGer\u00e4te.setWrapStyleWord(true);
        this.verbrauchWasser = new JTextField(25);
        this.verbrauchSchaum = new JTextField(25);
        this.verbrauchPulver = new JTextField(25);
        this.verbrauch\u00d6lB = new JTextField(25);
        this.verbrauchWasser_label = new JLabel("Verbruach Wasser: ");
        this.verbrauchSchm_label = new JLabel("Verbruach Schaummittel: ");
        this.verbrauchPulver_label = new JLabel("Verbruach Pulver: ");
        this.verbrauch\u00d6lB_label = new JLabel("Verbruach \u00d6lbindemittel: ");
        this.brandwacheDauer = new JTextField(25);
        this.brandwacheSt\u00e4rke = new JTextField(25);
        this.brandwacheDauer_label = new JLabel("Dauer: ");
        this.brandwacheSt\u00e4rke_label = new JLabel("St\u00e4rke: ");
        this.brandwacheFahrzeug_label = new JLabel("Fahrzeug: ");
        this.vorEintreffenGel\u00f6scht_label = new JLabel("vor Eintreffen Gel\u00f6scht: ");
        this.cRohre_label = new JLabel("C-Rohre: ");
        this.bRohre_label = new JLabel("B-Rohre: ");
        this.kleinesL\u00f6schger\u00e4t_label = new JLabel("Klein L\u00f6schger\u00e4t: ");
        this.schnellAngriff_label = new JLabel("Schnellangriff: ");
        this.schnellAngriff = new JCheckBox();
        this.voreintreffenGel\u00f6scht = new JCheckBox();
        this.cRohre = new JTextField(25);
        this.bRohre = new JTextField(25);
        this.kleinesL\u00f6schger\u00e4t = new JTextField(25);
        this.tragbareLeitern_label = new JLabel("Tragbare Leitern: ");
        this.atemschutzGer\u00e4te_label = new JLabel("Atemschutzger\u00e4t(e): ");
        this.fluchthauben_label = new JLabel("Fluchthabe(n): ");
        this.bel\u00fcftungsger\u00e4t_label = new JLabel("Bel\u00fcftungsger\u00e4t(e): ");
        this.rettungsger\u00e4t_label = new JLabel("Hydraulische Rettungsger\u00e4t(e): ");
        this.tragbareLeitern = new JCheckBox();
        this.atemschutzGer\u00e4te = new JTextField(25);
        this.fluchthauben = new JTextField(25);
        this.bel\u00fcftungsger\u00e4t = new JTextField(25);
        this.rettungsger\u00e4t = new JCheckBox();
        this.entstehungsursache_label = new JLabel("Entstehungsursache: ");
        this.entstehungsursache = new JTextArea(10, 20);
        this.pane_enstehungsursache = new JScrollPane(this.entstehungsursache);
        this.pane_enstehungsursache.setVerticalScrollBarPolicy(22);
        this.entstehungsursache.setLineWrap(true);
        this.entstehungsursache.setWrapStyleWord(true);
        this.verletzte_label = new JLabel("Verletzte Personen: ");
        this.gerettete_label = new JLabel("Gerettete Personen (mit Verletzten): ");
        this.tote_label = new JLabel("Tote Personen: ");
        this.schadenH\u00f6he_label = new JLabel("Gesch\u00e4tze Schadenh\u00f6he: ");
        this.verletzte = new JTextField(25);
        this.gerettete = new JTextField(25);
        this.tote = new JTextField(25);
        this.schadenH\u00f6he = new JTextField(25);
        this.modulBeschreibung = new JLabel("Einsatz Bericht erstellen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        this.veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleEinsatz_bericht_daten tabDaten = new TabelleEinsatz_bericht_daten();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                try {
                    int vID = tabVeranstaltung.getVeranstaltungID(EinsatzBerichtAO.this.veranstaltung.getSelectedItem().toString());
                    if (tabDaten.getCountByVerasnatltungID(vID) == 1) {
                        logging.logInfo((Object)"Veranstaltung gefunden, lade Daten...");
                        TabelleEinsatz_bericht_elemente tabElemenete = new TabelleEinsatz_bericht_elemente();
                        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                        EinsatzBerichtDaten daten = tabDaten.getBerichtData(vID);
                        EinsatzBerichtAO.this.eigent\u00fcmerName.setText(daten.getEigentuemerName());
                        EinsatzBerichtAO.this.eigent\u00fcmerAnschrift.setText(daten.getEigentuemerAnschrift());
                        EinsatzBerichtAO.this.eigent\u00fcmerTelefon.setText(daten.getEigentuemerTelefon());
                        EinsatzBerichtAO.this.verursacherAnschrift.setText(daten.getVerursacherAnschrift());
                        EinsatzBerichtAO.this.verursacherName.setText(daten.getVerursacherName());
                        EinsatzBerichtAO.this.verursacherTelefon.setText(daten.getVerursacherTelefon());
                        EinsatzBerichtAO.this.meldenderName.setText(daten.getMeldenderName());
                        EinsatzBerichtAO.this.meldenderAnschrift.setText(daten.getMeldenderAnschrift());
                        EinsatzBerichtAO.this.meldenderTelefon.setText(daten.getMeldenderTelefon());
                        EinsatzBerichtAO.this.vorgefundeneLage.setText(daten.getLage());
                        EinsatzBerichtAO.this.verlaufT\u00e4tigkeit.setText(daten.getVerlauf());
                        EinsatzBerichtAO.this.eingesetzteGer\u00e4te.setText(daten.getEingesetzteGeraete());
                        EinsatzBerichtAO.this.verbrauchWasser.setText(daten.getVerbrauchWasser());
                        EinsatzBerichtAO.this.verbrauchSchaum.setText(daten.getVerbrauchSchaum());
                        EinsatzBerichtAO.this.verbrauchPulver.setText(daten.getVerbrauchPulver());
                        EinsatzBerichtAO.this.verbrauch\u00d6lB.setText(daten.getVerbrauchBindemittel());
                        if (daten.getBrandwacheFahrzeug() == 0) {
                            EinsatzBerichtAO.this.brandwacheFahrzeug.setSelectedItem("<bitte w\u00e4hlen>");
                        } else {
                            EinsatzBerichtAO.this.brandwacheFahrzeug.setSelectedItem(tabFahrzeug.getFahrzeugName(daten.getBrandwacheFahrzeug()));
                        }
                        EinsatzBerichtAO.this.brandwacheDauer.setText(daten.getDauer());
                        EinsatzBerichtAO.this.brandwacheSt\u00e4rke.setText(daten.getStaerke());
                        if (daten.getSchnellangriff() == 1) {
                            EinsatzBerichtAO.this.schnellAngriff.setSelected(true);
                        } else {
                            EinsatzBerichtAO.this.schnellAngriff.setSelected(false);
                        }
                        if (daten.getVorEintreffenGeloescht() == 1) {
                            EinsatzBerichtAO.this.voreintreffenGel\u00f6scht.setSelected(true);
                        } else {
                            EinsatzBerichtAO.this.voreintreffenGel\u00f6scht.setSelected(false);
                        }
                        EinsatzBerichtAO.this.cRohre.setText(daten.getcRohr());
                        EinsatzBerichtAO.this.bRohre.setText(daten.getbRohr());
                        EinsatzBerichtAO.this.kleinesL\u00f6schger\u00e4t.setText(daten.getKleinLoeschgeraet());
                        if (daten.getTragbareLeitern() == 1) {
                            EinsatzBerichtAO.this.tragbareLeitern.setSelected(true);
                        } else {
                            EinsatzBerichtAO.this.tragbareLeitern.setSelected(false);
                        }
                        EinsatzBerichtAO.this.atemschutzGer\u00e4te.setText(daten.getAtemschutzgeraet());
                        EinsatzBerichtAO.this.fluchthauben.setText(daten.getFluchthauben());
                        EinsatzBerichtAO.this.bel\u00fcftungsger\u00e4t.setText(daten.getBelueftungsgeraet());
                        if (daten.getRettungsgeraet() == 1) {
                            EinsatzBerichtAO.this.rettungsger\u00e4t.setSelected(true);
                        } else {
                            EinsatzBerichtAO.this.rettungsger\u00e4t.setSelected(false);
                        }
                        EinsatzBerichtAO.this.entstehungsursache.setText(daten.getEntstehungsursache());
                        EinsatzBerichtAO.this.verletzte.setText(daten.getVerletzte());
                        EinsatzBerichtAO.this.gerettete.setText(daten.getGerettete());
                        EinsatzBerichtAO.this.tote.setText(daten.getTote());
                        EinsatzBerichtAO.this.schadenH\u00f6he.setText(daten.getSchadenhoehe());
                        String valueEinsatzArt = tabElemenete.getName(daten.getEinsatzArt());
                        int i = 0;
                        while (i < EinsatzBerichtAO.this.einsatzArt.length) {
                            if (EinsatzBerichtAO.this.einsatzArt[i].getText().equals(valueEinsatzArt)) {
                                EinsatzBerichtAO.this.einsatzArt[i].setSelected(true);
                            } else {
                                EinsatzBerichtAO.this.einsatzArt[i].setSelected(false);
                            }
                            ++i;
                        }
                        String valueStelle = tabElemenete.getName(daten.getStelle());
                        int i2 = 0;
                        while (i2 < EinsatzBerichtAO.this.stelle.length) {
                            if (EinsatzBerichtAO.this.stelle[i2].getText().equals(valueStelle)) {
                                EinsatzBerichtAO.this.stelle[i2].setSelected(true);
                            } else {
                                EinsatzBerichtAO.this.stelle[i2].setSelected(false);
                            }
                            ++i2;
                        }
                        String valueObjekt = tabElemenete.getName(daten.getObjekt());
                        int i3 = 0;
                        while (i3 < EinsatzBerichtAO.this.objekt.length) {
                            if (EinsatzBerichtAO.this.objekt[i3].getText().equals(valueObjekt)) {
                                EinsatzBerichtAO.this.objekt[i3].setSelected(true);
                            } else {
                                EinsatzBerichtAO.this.objekt[i3].setSelected(false);
                            }
                            ++i3;
                        }
                        String valueAlamierung = tabElemenete.getName(daten.getAlamierung());
                        int i4 = 0;
                        while (i4 < EinsatzBerichtAO.this.alamierung.length) {
                            if (EinsatzBerichtAO.this.alamierung[i4].getText().equals(valueAlamierung)) {
                                EinsatzBerichtAO.this.alamierung[i4].setSelected(true);
                            } else {
                                EinsatzBerichtAO.this.alamierung[i4].setSelected(false);
                            }
                            ++i4;
                        }
                        String valueAusdehnung = tabElemenete.getName(daten.getAusdehnung());
                        int i5 = 0;
                        while (i5 < EinsatzBerichtAO.this.ausdehung.length) {
                            if (EinsatzBerichtAO.this.ausdehung[i5].getText().equals(valueAusdehnung)) {
                                EinsatzBerichtAO.this.ausdehung[i5].setSelected(true);
                            } else {
                                EinsatzBerichtAO.this.ausdehung[i5].setSelected(false);
                            }
                            ++i5;
                        }
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
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
        this.setSize(1280, 768);
        this.setTitle("FeuerwehrManagementSystem - Einsatz Bericht");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.veranstaltung_label);
        this.add(this.veranstaltung);
        this.add(this.dummy3);
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        TabelleEinsatz_bericht_elemente tabElemente = new TabelleEinsatz_bericht_elemente();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        String[] einsatzArtListe = null;
        String[] stelleListe = null;
        String[] objektListe = null;
        String[] AlamierungListe = null;
        String[] fahrzeugListe = null;
        String[] ausdehungListe = null;
        try {
            einsatzArtListe = Utils.listToArray(tabElemente.getElemente("EinsatzArt"));
            stelleListe = Utils.listToArray(tabElemente.getElemente("Stelle"));
            objektListe = Utils.listToArray(tabElemente.getElemente("Objekt"));
            AlamierungListe = Utils.listToArray(tabElemente.getElemente("Alamierung"));
            ausdehungListe = Utils.listToArray(tabElemente.getElemente("Ausdehnung"));
            fahrzeugListe = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.brandwacheFahrzeug = new JComboBox(fahrzeugListe);
        this.panel1 = new JPanel(new GridLayout(3, 7));
        TitledBorder rahmen1 = BorderFactory.createTitledBorder(lowerEtched, "1 - Einsatz Art");
        this.panel1.setBorder(rahmen1);
        this.einsatzArt = new JCheckBox[einsatzArtListe.length];
        int i = 0;
        while (i < einsatzArtListe.length) {
            this.einsatzArt[i] = new JCheckBox(einsatzArtListe[i]);
            this.panel1.add(this.einsatzArt[i]);
            ++i;
        }
        this.add(this.panel1);
        this.panel1.setPreferredSize(new Dimension(1240, 100));
        this.panel2 = new JPanel(new GridLayout(3, 4));
        TitledBorder rahmen2 = BorderFactory.createTitledBorder(lowerEtched, "2 - Stelle des Einsatzes");
        this.panel2.setBorder(rahmen2);
        this.stelle = new JCheckBox[stelleListe.length];
        int i2 = 0;
        while (i2 < stelleListe.length) {
            this.stelle[i2] = new JCheckBox(stelleListe[i2]);
            this.panel2.add(this.stelle[i2]);
            ++i2;
        }
        this.add(this.panel2);
        this.panel2.setPreferredSize(new Dimension(620, 100));
        this.panel3 = new JPanel(new GridLayout(3, 4));
        TitledBorder rahmen3 = BorderFactory.createTitledBorder(lowerEtched, "3 - Objekt");
        this.panel3.setBorder(rahmen3);
        this.objekt = new JCheckBox[objektListe.length];
        int i3 = 0;
        while (i3 < objektListe.length) {
            this.objekt[i3] = new JCheckBox(objektListe[i3]);
            this.panel3.add(this.objekt[i3]);
            ++i3;
        }
        this.add(this.panel3);
        this.panel3.setPreferredSize(new Dimension(620, 100));
        this.panel4 = new JPanel(new GridLayout(3, 2));
        TitledBorder rahmen4 = BorderFactory.createTitledBorder(lowerEtched, "4 - Eigent\u00fcmer / Gesch\u00e4digter");
        this.panel4.setBorder(rahmen4);
        this.panel4.add(this.eigent\u00fcmerName_label);
        this.panel4.add(this.eigent\u00fcmerName);
        this.panel4.add(this.eigent\u00fcmerAnschrift_label);
        this.panel4.add(this.eigent\u00fcmerAnschrift);
        this.panel4.add(this.eigent\u00fcmerTelefon_label);
        this.panel4.add(this.eigent\u00fcmerTelefon);
        this.add(this.panel4);
        this.panel4.setPreferredSize(new Dimension(620, 100));
        this.panel5 = new JPanel(new GridLayout(3, 2));
        TitledBorder rahmen5 = BorderFactory.createTitledBorder(lowerEtched, "5 - Verursacher");
        this.panel5.setBorder(rahmen5);
        this.panel5.add(this.verursacherName_label);
        this.panel5.add(this.verursacherName);
        this.panel5.add(this.verursacherAnschrift_label);
        this.panel5.add(this.verursacherAnschrift);
        this.panel5.add(this.verursacherTelefon_label);
        this.panel5.add(this.verursacherTelefon);
        this.add(this.panel5);
        this.panel5.setPreferredSize(new Dimension(620, 100));
        this.panel6 = new JPanel(new GridLayout(2, 4));
        TitledBorder rahmen6 = BorderFactory.createTitledBorder(lowerEtched, "6 - Alamierung");
        this.panel6.setBorder(rahmen6);
        this.alamierung = new JCheckBox[AlamierungListe.length];
        int i4 = 0;
        while (i4 < AlamierungListe.length) {
            this.alamierung[i4] = new JCheckBox(AlamierungListe[i4]);
            this.panel6.add(this.alamierung[i4]);
            ++i4;
        }
        this.add(this.panel6);
        this.panel6.setPreferredSize(new Dimension(620, 100));
        this.panel7 = new JPanel(new GridLayout(3, 2));
        TitledBorder rahmen7 = BorderFactory.createTitledBorder(lowerEtched, "7 - Meldender");
        this.panel7.setBorder(rahmen7);
        this.panel7.add(this.meldenderName_label);
        this.panel7.add(this.meldenderName);
        this.panel7.add(this.meldenderAnschrift_label);
        this.panel7.add(this.meldenderAnschrift);
        this.panel7.add(this.meldenderTelefon_label);
        this.panel7.add(this.meldenderTelefon);
        this.add(this.panel7);
        this.panel7.setPreferredSize(new Dimension(620, 100));
        this.panel8 = new JPanel(new GridLayout(3, 2));
        TitledBorder rahmen8 = BorderFactory.createTitledBorder(lowerEtched, "8 - Einsatzt\u00e4tigkeit");
        this.panel8.setBorder(rahmen8);
        this.panel8.add(this.vorgefundeneLage_label);
        this.panel8.add(this.pane_vorgefundeneLage);
        this.panel8.add(this.verlaufT\u00e4tigkeit_label);
        this.panel8.add(this.pane_verlaufT\u00e4tigkeit);
        this.panel8.add(this.eingesetzteGer\u00e4te_label);
        this.panel8.add(this.pane_eingesetzteGer\u00e4te);
        this.add(this.panel8);
        this.panel8.setPreferredSize(new Dimension(620, 135));
        this.panel9 = new JPanel(new GridLayout(4, 2));
        TitledBorder rahmen9 = BorderFactory.createTitledBorder(lowerEtched, "9 - Verbrauch");
        this.panel9.setBorder(rahmen9);
        this.panel9.add(this.verbrauchWasser_label);
        this.panel9.add(this.verbrauchWasser);
        this.panel9.add(this.verbrauchSchm_label);
        this.panel9.add(this.verbrauchSchaum);
        this.panel9.add(this.verbrauchPulver_label);
        this.panel9.add(this.verbrauchPulver);
        this.panel9.add(this.verbrauch\u00d6lB_label);
        this.panel9.add(this.verbrauch\u00d6lB);
        this.add(this.panel9);
        this.panel9.setPreferredSize(new Dimension(620, 135));
        this.panel10 = new JPanel(new GridLayout(5, 2));
        TitledBorder rahmen10 = BorderFactory.createTitledBorder(lowerEtched, "10 - L\u00f6schma\u00dfnahmen");
        this.panel10.setBorder(rahmen10);
        this.panel10.add(this.vorEintreffenGel\u00f6scht_label);
        this.panel10.add(this.voreintreffenGel\u00f6scht);
        this.panel10.add(this.schnellAngriff_label);
        this.panel10.add(this.schnellAngriff);
        this.panel10.add(this.cRohre_label);
        this.panel10.add(this.cRohre);
        this.panel10.add(this.bRohre_label);
        this.panel10.add(this.bRohre);
        this.panel10.add(this.kleinesL\u00f6schger\u00e4t_label);
        this.panel10.add(this.kleinesL\u00f6schger\u00e4t);
        this.panel10.setPreferredSize(new Dimension(620, 135));
        this.panel11 = new JPanel(new GridLayout(5, 2));
        TitledBorder rahmen11 = BorderFactory.createTitledBorder(lowerEtched, "11 - Ger\u00e4te im Detail");
        this.panel11.setBorder(rahmen11);
        this.panel11.add(this.tragbareLeitern_label);
        this.panel11.add(this.tragbareLeitern);
        this.panel11.add(this.atemschutzGer\u00e4te_label);
        this.panel11.add(this.atemschutzGer\u00e4te);
        this.panel11.add(this.fluchthauben_label);
        this.panel11.add(this.fluchthauben);
        this.panel11.add(this.bel\u00fcftungsger\u00e4t_label);
        this.panel11.add(this.bel\u00fcftungsger\u00e4t);
        this.panel11.add(this.rettungsger\u00e4t_label);
        this.panel11.add(this.rettungsger\u00e4t);
        this.panel11.setPreferredSize(new Dimension(620, 135));
        this.panel12 = new JPanel(new GridLayout(3, 2));
        TitledBorder rahmen12 = BorderFactory.createTitledBorder(lowerEtched, "12 - Ausdehnung");
        this.panel12.setBorder(rahmen12);
        this.ausdehung = new JCheckBox[ausdehungListe.length];
        int i5 = 0;
        while (i5 < ausdehungListe.length) {
            this.ausdehung[i5] = new JCheckBox(ausdehungListe[i5]);
            this.panel12.add(this.ausdehung[i5]);
            ++i5;
        }
        this.panel12.setPreferredSize(new Dimension(620, 100));
        this.panel13 = new JPanel(new GridLayout(1, 1));
        TitledBorder rahmen13 = BorderFactory.createTitledBorder(lowerEtched, "13 - Entstehungsursache");
        this.panel13.setBorder(rahmen13);
        this.panel13.add(this.entstehungsursache_label);
        this.panel13.add(this.pane_enstehungsursache);
        this.panel13.setPreferredSize(new Dimension(620, 100));
        this.panel14 = new JPanel(new GridLayout(4, 2));
        TitledBorder rahmen14 = BorderFactory.createTitledBorder(lowerEtched, "14 - Schaden");
        this.panel14.setBorder(rahmen14);
        this.panel14.add(this.verletzte_label);
        this.panel14.add(this.verletzte);
        this.panel14.add(this.gerettete_label);
        this.panel14.add(this.gerettete);
        this.panel14.add(this.tote_label);
        this.panel14.add(this.tote);
        this.panel14.add(this.schadenH\u00f6he_label);
        this.panel14.add(this.schadenH\u00f6he);
        this.panel14.setPreferredSize(new Dimension(620, 100));
        this.panel15 = new JPanel(new GridLayout(3, 2));
        TitledBorder rahmen16 = BorderFactory.createTitledBorder(lowerEtched, "15 - Brandwache");
        this.panel15.setBorder(rahmen16);
        this.panel15.add(this.brandwacheFahrzeug_label);
        this.panel15.add(this.brandwacheFahrzeug);
        this.panel15.add(this.brandwacheSt\u00e4rke_label);
        this.panel15.add(this.brandwacheSt\u00e4rke);
        this.panel15.add(this.brandwacheDauer_label);
        this.panel15.add(this.brandwacheDauer);
        this.panel15.setPreferredSize(new Dimension(620, 100));
        this.add(this.dummy2);
        this.add(this.buttonSeite1);
        this.add(this.buttonSeite2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.buttonSeite1.setEnabled(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSeite1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzBerichtAO.this.buttonSeite1.setEnabled(false);
                EinsatzBerichtAO.this.buttonSeite2.setEnabled(true);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.dummy2);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonSeite1);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonSeite2);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonZurueck);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonSpeichern);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel1);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel2);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel3);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel4);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel5);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel6);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel7);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel8);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel9);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel10);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel11);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel12);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel13);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel14);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel15);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.dummy2);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonSeite1);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonSeite2);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonZurueck);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonSpeichern);
                EinsatzBerichtAO.this.validate();
                EinsatzBerichtAO.this.repaint();
            }
        });
        this.buttonSeite2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzBerichtAO.this.buttonSeite1.setEnabled(true);
                EinsatzBerichtAO.this.buttonSeite2.setEnabled(false);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.dummy2);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonSeite1);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonSeite2);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonZurueck);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.buttonSpeichern);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel1);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel2);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel3);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel4);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel5);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel6);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel7);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel8);
                EinsatzBerichtAO.this.remove(EinsatzBerichtAO.this.panel9);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel10);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel11);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel12);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel13);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel14);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.panel15);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.dummy2);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonSeite1);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonSeite2);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonZurueck);
                EinsatzBerichtAO.this.add(EinsatzBerichtAO.this.buttonSpeichern);
                EinsatzBerichtAO.this.validate();
                EinsatzBerichtAO.this.repaint();
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzBerichtDaten berichtDaten = new EinsatzBerichtDaten();
                Einsatz_bericht bericht = new Einsatz_bericht();
                TabelleEinsatz_bericht tabBericht = new TabelleEinsatz_bericht();
                TabelleEinsatz_bericht_daten tabDaten = new TabelleEinsatz_bericht_daten();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                TabelleEinsatz_bericht_elemente tabElemente = new TabelleEinsatz_bericht_elemente();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                try {
                    int einsatzArtID = 0;
                    int stelleID = 0;
                    int objektID = 0;
                    int alamierungID = 0;
                    int ausdehnungID = 0;
                    int countEinsatzArt = 0;
                    int i = 0;
                    while (i < EinsatzBerichtAO.this.einsatzArt.length) {
                        if (EinsatzBerichtAO.this.einsatzArt[i].isSelected()) {
                            einsatzArtID = tabElemente.getID(EinsatzBerichtAO.this.einsatzArt[i].getText());
                            ++countEinsatzArt;
                        }
                        ++i;
                    }
                    int countStelle = 0;
                    int i2 = 0;
                    while (i2 < EinsatzBerichtAO.this.stelle.length) {
                        if (EinsatzBerichtAO.this.stelle[i2].isSelected()) {
                            stelleID = tabElemente.getID(EinsatzBerichtAO.this.stelle[i2].getText());
                            ++countStelle;
                        }
                        ++i2;
                    }
                    int countObjekt = 0;
                    int i3 = 0;
                    while (i3 < EinsatzBerichtAO.this.objekt.length) {
                        if (EinsatzBerichtAO.this.objekt[i3].isSelected()) {
                            objektID = tabElemente.getID(EinsatzBerichtAO.this.objekt[i3].getText());
                            ++countObjekt;
                        }
                        ++i3;
                    }
                    int countAlamierung = 0;
                    int i4 = 0;
                    while (i4 < EinsatzBerichtAO.this.alamierung.length) {
                        if (EinsatzBerichtAO.this.alamierung[i4].isSelected()) {
                            alamierungID = tabElemente.getID(EinsatzBerichtAO.this.alamierung[i4].getText());
                            ++countAlamierung;
                        }
                        ++i4;
                    }
                    int countAusdehnung = 0;
                    int i5 = 0;
                    while (i5 < EinsatzBerichtAO.this.ausdehung.length) {
                        if (EinsatzBerichtAO.this.ausdehung[i5].isSelected()) {
                            ausdehnungID = tabElemente.getID(EinsatzBerichtAO.this.ausdehung[i5].getText());
                            ++countAusdehnung;
                        }
                        ++i5;
                    }
                    if (EinsatzBerichtAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                    } else if (countAlamierung != 1 | countAusdehnung != 1 | countEinsatzArt != 1 | countObjekt != 1 | countStelle != 1) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_EINSTAZBERICHT_EINTRAEGE_KONTROLLE, "Warnung", 2);
                    } else {
                        int vID = tabVeranstaltung.getVeranstaltungID(EinsatzBerichtAO.this.veranstaltung.getSelectedItem().toString());
                        int jahr = tabVeranstaltung.getJahrDerVeranstaltung(vID);
                        int eID = tabEinsatz.getEinsatzIDByVeranstaltungID(vID);
                        berichtDaten.setId(tabDaten.getNextNummer());
                        berichtDaten.setVeranstaltungID(vID);
                        berichtDaten.setEinsatzID(eID);
                        berichtDaten.setJahr(jahr);
                        berichtDaten.setEinsatzArt(einsatzArtID);
                        berichtDaten.setStelle(stelleID);
                        berichtDaten.setObjekt(objektID);
                        berichtDaten.setEigentuemerName(EinsatzBerichtAO.this.eigent\u00fcmerName.getText());
                        berichtDaten.setEigentuemerAnschrift(EinsatzBerichtAO.this.eigent\u00fcmerAnschrift.getText());
                        berichtDaten.setEigentuemerTelefon(EinsatzBerichtAO.this.eigent\u00fcmerTelefon.getText());
                        berichtDaten.setVerursacherName(EinsatzBerichtAO.this.verursacherName.getText());
                        berichtDaten.setVerursacherAnschrift(EinsatzBerichtAO.this.verursacherAnschrift.getText());
                        berichtDaten.setVerursacherTelefon(EinsatzBerichtAO.this.verursacherTelefon.getText());
                        berichtDaten.setAlamierung(alamierungID);
                        berichtDaten.setMeldenderName(EinsatzBerichtAO.this.meldenderName.getText());
                        berichtDaten.setMeldenderAnschrift(EinsatzBerichtAO.this.meldenderAnschrift.getText());
                        berichtDaten.setMeldenderTelefon(EinsatzBerichtAO.this.meldenderTelefon.getText());
                        berichtDaten.setLage(EinsatzBerichtAO.this.vorgefundeneLage.getText());
                        berichtDaten.setVerlauf(EinsatzBerichtAO.this.verlaufT\u00e4tigkeit.getText());
                        berichtDaten.setEingesetzteGeraete(EinsatzBerichtAO.this.eingesetzteGer\u00e4te.getText());
                        berichtDaten.setVerbrauchWasser(EinsatzBerichtAO.this.verbrauchWasser.getText());
                        berichtDaten.setVerbrauchSchaum(EinsatzBerichtAO.this.verbrauchSchaum.getText());
                        berichtDaten.setVerbrauchPulver(EinsatzBerichtAO.this.verbrauchPulver.getText());
                        berichtDaten.setVerbrauchBindemittel(EinsatzBerichtAO.this.verbrauch\u00d6lB.getText());
                        berichtDaten.setVorEintreffenGeloescht(EinsatzBerichtAO.this.voreintreffenGel\u00f6scht.isSelected() ? 1 : 0);
                        berichtDaten.setSchnellangriff(EinsatzBerichtAO.this.schnellAngriff.isSelected() ? 1 : 0);
                        berichtDaten.setcRohr(EinsatzBerichtAO.this.cRohre.getText());
                        berichtDaten.setbRohr(EinsatzBerichtAO.this.bRohre.getText());
                        berichtDaten.setKleinLoeschgeraet(EinsatzBerichtAO.this.kleinesL\u00f6schger\u00e4t.getText());
                        berichtDaten.setTragbareLeitern(EinsatzBerichtAO.this.tragbareLeitern.isSelected() ? 1 : 0);
                        berichtDaten.setAtemschutzgeraet(EinsatzBerichtAO.this.atemschutzGer\u00e4te.getText());
                        berichtDaten.setFluchthauben(EinsatzBerichtAO.this.fluchthauben.getText());
                        berichtDaten.setBelueftungsgeraet(EinsatzBerichtAO.this.bel\u00fcftungsger\u00e4t.getText());
                        berichtDaten.setRettungsgeraet(EinsatzBerichtAO.this.rettungsger\u00e4t.isSelected() ? 1 : 0);
                        berichtDaten.setAusdehnung(ausdehnungID);
                        berichtDaten.setEntstehungsursache(EinsatzBerichtAO.this.entstehungsursache.getText());
                        berichtDaten.setVerletzte(EinsatzBerichtAO.this.verletzte.getText());
                        berichtDaten.setGerettete(EinsatzBerichtAO.this.gerettete.getText());
                        berichtDaten.setTote(EinsatzBerichtAO.this.tote.getText());
                        berichtDaten.setSchadenhoehe(EinsatzBerichtAO.this.schadenH\u00f6he.getText());
                        berichtDaten.setBrandwacheFahrzeug(tabFahrzeug.getFahrzeugID(EinsatzBerichtAO.this.brandwacheFahrzeug.getSelectedItem().toString()));
                        berichtDaten.setStaerke(EinsatzBerichtAO.this.brandwacheSt\u00e4rke.getText());
                        berichtDaten.setDauer(EinsatzBerichtAO.this.brandwacheDauer.getText());
                        if (tabDaten.getCountByVerasnatltungID(vID) == 0) {
                            tabDaten.insert(berichtDaten);
                            logging.logInfo((Object)"Erstelle Bericht Datensatz");
                        } else {
                            tabDaten.update(berichtDaten);
                            logging.logInfo((Object)"Aktualisiere Bericht Datensatz");
                        }
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        HashMap<String, String> map = tabEinsatz.getData(vID);
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + ".pdf";
                        bericht.setId(tabBericht.getNextNummer());
                        bericht.setEinsatzNummer(eID);
                        bericht.setVeranstaltungID(vID);
                        bericht.setJahr(jahr);
                        bericht.setDateiname("Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + ".pdf");
                        bericht.setFahrzeugbelegung(0);
                        bericht.setAtemschutz(0);
                        tabBericht.insert(bericht);
                        Utils.dateiKatalogisieren(dateiname);
                        EinsatBerichtPDFSchreiben.PDFdocumentErstellen(dateiname, berichtDaten, map);
                        EinsatzBerichtAO.this.dispose();
                        Desktop.getDesktop().open(new File(dateiname));
                    }
                }
                catch (DocumentException | IOException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
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

