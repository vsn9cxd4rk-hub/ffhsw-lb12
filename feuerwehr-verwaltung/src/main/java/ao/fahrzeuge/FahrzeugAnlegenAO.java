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
import data.tabellen.TabelleFahrzeug_beschreibung;
import data.tabellen.einstellungen.TabelleMandant;
import go.Fahrzeug;
import java.awt.Color;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.mitgliedakte.PDFFahrzeugAusserDienst;
import pdfdocumente.mitgliedakte.PDFFahrzeugInDienst;
import pdfdocumente.mitgliedakte.PDFFahrzeugInfo;
import run.runApplication;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class FahrzeugAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAktualisieren;
    private JButton buttonUntersuchung;
    private JButton buttonNeu;
    private JButton buttonAusserDienst;
    private JButton buttonFahrzeugBeschreibung;
    private JButton buttonAbbruch;
    public static JTextField name;
    public static JTextField fahrzeugID;
    public static JComboBox<String> kategorie;
    private JTextField sitzplaetze;
    private JTextField maxBesatzung;
    private JTextField minBesatzung;
    private JTextField kennzeichen;
    private JTextField funkRufName;
    private JCheckBox ausserDienst;
    private JSlider sortierung;
    private JCheckBox trupp;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JLabel name_label;
    private JLabel fahrzeugID_label;
    private JLabel kategorie_label;
    private JLabel sitzplaetze_label;
    private JLabel maxBesatzung_label;
    private JLabel minBesatzung_label;
    private JLabel kennzeichen_label;
    private JLabel funkRufName_label;
    private JLabel sortierung_label;
    private JLabel ausserDienst_label;
    private JLabel trupp_label;
    public static JComboBox<String> mandant;
    private JLabel mandant_label;
    private JCheckBox anhaenger;
    private JLabel anhaenger_label;
    private HashMap<String, String> mapFahrzeug;
    private JRadioButton klasseB;
    private JRadioButton klasseC1;
    private JRadioButton klasseC1E;
    private JRadioButton klasseC;
    private JRadioButton klasseCE;
    private JRadioButton klasseBE;
    private ButtonGroup group;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JPanel panelFahrzeug;
    private JPanel panelFuehrerscheinKlasse;

    public FahrzeugAnlegenAO() {
        super("FeuerwehrManagementSystem - Fahrzeuge");
        logging.logInfo((Object)"Starte: FahrzaugeAnlagenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonUntersuchung = new JButton("Wartung / Untersuchung");
        this.buttonNeu = new JButton("Neu");
        this.buttonAusserDienst = new JButton("Au\u00dfer Dienst");
        this.buttonFahrzeugBeschreibung = new JButton("Neue Fahrzeuggruppe anlegen");
        this.buttonAbbruch = new JButton("Abbruch");
        name = new JTextField(20);
        this.sitzplaetze = new JTextField(20);
        this.maxBesatzung = new JTextField(20);
        this.minBesatzung = new JTextField(20);
        fahrzeugID = new JTextField(20);
        this.kennzeichen = new JTextField(20);
        this.funkRufName = new JTextField(20);
        this.trupp = new JCheckBox();
        this.sortierung = new JSlider();
        this.sortierung.setToolTipText("Niedrigste Priorit\u00e4t (Schieberegler Links) Fahrzeug ist in der Einsatzreihenfolge das Erste!");
        this.sortierung.setPaintTicks(true);
        this.sortierung.setMajorTickSpacing(10);
        this.ausserDienst = new JCheckBox();
        this.name_label = new JLabel("Name: ");
        this.kategorie_label = new JLabel("Fahrzeug Kategorie: ");
        this.sitzplaetze_label = new JLabel("Sitzpl\u00e4tze: ");
        this.maxBesatzung_label = new JLabel("Maximale Ausr\u00fcckst\u00e4rke: ");
        this.minBesatzung_label = new JLabel("Minimale Ausr\u00fcckst\u00e4rke: ");
        this.fahrzeugID_label = new JLabel("Fahrzeugnummer: ");
        this.kennzeichen_label = new JLabel("Kfz-Kennezeichen: ");
        this.funkRufName_label = new JLabel("Funkrufname: ");
        this.sortierung_label = new JLabel("Fahrzeugpriorit\u00e4t: ");
        this.ausserDienst_label = new JLabel("Au\u00dfer Dienst");
        this.mandant_label = new JLabel("Mandant: ");
        this.trupp_label = new JLabel("Truppfahrzeug: ");
        this.anhaenger = new JCheckBox();
        this.anhaenger_label = new JLabel("Ist das Fahrzeug ein Anh\u00e4nger: ");
        this.modulBeschreibung = new JLabel("Fahrzeugverwaltung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.klasseC = new JRadioButton("F\u00fchrerschein Klasse C");
        this.klasseC1 = new JRadioButton("F\u00fchrerschein Klasse C1");
        this.klasseC1E = new JRadioButton("F\u00fchrerschein Klasse C1E");
        this.klasseCE = new JRadioButton("F\u00fchrerschein Klasse CE");
        this.klasseB = new JRadioButton("F\u00fchrerschein Klasse B");
        this.klasseBE = new JRadioButton("F\u00fchrerschein Klasse BE");
        this.group = new ButtonGroup();
        this.group.add(this.klasseC1);
        this.group.add(this.klasseC1E);
        this.group.add(this.klasseC);
        this.group.add(this.klasseB);
        this.group.add(this.klasseCE);
        this.group.add(this.klasseBE);
        tree = new JTree(CreateTrees.CreateTreeFahrzeugListe());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
        try {
            TabelleFahrzeug_beschreibung beschreibungListe = new TabelleFahrzeug_beschreibung();
            TabelleMandant tabMandant = new TabelleMandant();
            String[] liste = Utils.listToArrayOnlyFORComboBoxes(beschreibungListe.getAllFahrzeugBeschreibungen());
            String[] mandantListe = Utils.listToArrayOnlyFORComboBoxes(tabMandant.getAllMandanten());
            kategorie = new JComboBox<String>(liste);
            mandant = new JComboBox<String>(mandantListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelErstellen() {
        this.anhaenger.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (FahrzeugAnlegenAO.this.anhaenger.isSelected()) {
                    FahrzeugAnlegenAO.this.minBesatzung.setEditable(false);
                    FahrzeugAnlegenAO.this.maxBesatzung.setEditable(false);
                    FahrzeugAnlegenAO.this.sitzplaetze.setEditable(false);
                    FahrzeugAnlegenAO.this.trupp.setEnabled(false);
                    FahrzeugAnlegenAO.this.minBesatzung.setText("0");
                    FahrzeugAnlegenAO.this.maxBesatzung.setText("0");
                    FahrzeugAnlegenAO.this.sitzplaetze.setText("0");
                } else {
                    FahrzeugAnlegenAO.this.minBesatzung.setEditable(true);
                    FahrzeugAnlegenAO.this.maxBesatzung.setEditable(true);
                    FahrzeugAnlegenAO.this.sitzplaetze.setEditable(true);
                    FahrzeugAnlegenAO.this.trupp.setEnabled(true);
                    FahrzeugAnlegenAO.this.minBesatzung.setText(null);
                    FahrzeugAnlegenAO.this.maxBesatzung.setText(null);
                    FahrzeugAnlegenAO.this.sitzplaetze.setText(null);
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
        this.setSize(1000, 560);
        this.setTitle("FeuerwehrManagementSystem - Fahrzeuge");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonNeu);
        this.add(this.buttonAusserDienst);
        this.add(this.buttonFahrzeugBeschreibung);
        this.add(this.dummy3);
        this.scrollPaneTree.setPreferredSize(new Dimension(300, 350));
        this.add(this.scrollPaneTree);
        this.panelFahrzeug = new JPanel(new GridLayout(13, 2));
        this.getContentPane().add("Center", this.panelFahrzeug);
        this.panelFahrzeug.add(this.fahrzeugID_label);
        this.panelFahrzeug.add(fahrzeugID);
        if (runApplication.BF == 1) {
            this.panelFahrzeug.add(this.mandant_label);
            this.panelFahrzeug.add(mandant);
        }
        this.panelFahrzeug.add(this.name_label);
        this.panelFahrzeug.add(name);
        this.panelFahrzeug.add(this.kennzeichen_label);
        this.panelFahrzeug.add(this.kennzeichen);
        this.panelFahrzeug.add(this.funkRufName_label);
        this.panelFahrzeug.add(this.funkRufName);
        this.panelFahrzeug.add(this.sortierung_label);
        this.panelFahrzeug.add(this.sortierung);
        this.panelFahrzeug.add(this.kategorie_label);
        this.panelFahrzeug.add(kategorie);
        this.panelFahrzeug.add(this.sitzplaetze_label);
        this.panelFahrzeug.add(this.sitzplaetze);
        this.panelFahrzeug.add(this.minBesatzung_label);
        this.panelFahrzeug.add(this.minBesatzung);
        this.panelFahrzeug.add(this.maxBesatzung_label);
        this.panelFahrzeug.add(this.maxBesatzung);
        this.panelFahrzeug.add(this.anhaenger_label);
        this.panelFahrzeug.add(this.anhaenger);
        this.panelFahrzeug.add(this.trupp_label);
        this.panelFahrzeug.add(this.trupp);
        this.panelFahrzeug.add(this.ausserDienst_label);
        this.panelFahrzeug.add(this.ausserDienst);
        this.panelFuehrerscheinKlasse = new JPanel(new GridLayout(6, 1));
        this.getContentPane().add("Center", this.panelFuehrerscheinKlasse);
        this.panelFuehrerscheinKlasse.add(this.klasseB);
        this.panelFuehrerscheinKlasse.add(this.klasseBE);
        this.panelFuehrerscheinKlasse.add(this.klasseC1);
        this.panelFuehrerscheinKlasse.add(this.klasseC1E);
        this.panelFuehrerscheinKlasse.add(this.klasseC);
        this.panelFuehrerscheinKlasse.add(this.klasseCE);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonAbbruch);
        this.add(this.buttonAktualisieren);
        this.add(this.buttonUntersuchung);
        fahrzeugID.setEditable(false);
        this.ausserDienst.setEnabled(false);
        this.buttonAbbruch.setVisible(false);
        this.buttonSpeichern.setVisible(false);
        if (BerechtigunsManager.ber[38] == 1) {
            this.buttonFahrzeugBeschreibung.setEnabled(true);
        } else {
            this.buttonFahrzeugBeschreibung.setEnabled(false);
        }
        if (BerechtigunsManager.ber[41] == 1) {
            this.buttonAusserDienst.setEnabled(true);
        } else {
            this.buttonAusserDienst.setEnabled(false);
        }
        this.sichtbarkeitSetzen(false);
        mandant.setSelectedItem(runApplication.mandantName);
    }

    private void sichtbarkeitSetzen(boolean wert) {
        this.buttonUntersuchung.setEnabled(false);
        name.setEnabled(wert);
        this.funkRufName.setEnabled(wert);
        this.maxBesatzung.setEnabled(wert);
        this.minBesatzung.setEnabled(wert);
        this.kennzeichen.setEnabled(wert);
        this.sortierung.setEnabled(wert);
        this.klasseB.setEnabled(wert);
        this.klasseC.setEnabled(wert);
        this.klasseC1.setEnabled(wert);
        this.klasseC1E.setEnabled(wert);
        this.klasseCE.setEnabled(wert);
        this.klasseBE.setEnabled(wert);
        this.anhaenger.setEnabled(wert);
        kategorie.setEnabled(wert);
        mandant.setEnabled(wert);
        this.trupp.setEnabled(wert);
        if (!wert) {
            this.buttonAusserDienst.setEnabled(wert);
        } else if (BerechtigunsManager.ber[41] == 1) {
            this.buttonAusserDienst.setEnabled(true);
        } else {
            this.buttonAusserDienst.setEnabled(false);
        }
        this.buttonAktualisieren.setEnabled(wert);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                FahrzeugAnlegenAO.this.buttonAktualisieren.setEnabled(true);
                TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
                TabelleMandant tabMandant = new TabelleMandant();
                try {
                    int fID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    fahrzeugID.setText(Integer.toString(fID));
                    FahrzeugAnlegenAO.this.mapFahrzeug = tabFahrzeuge.getAllFahrzeugData(fID);
                    name.setText((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("name"));
                    FahrzeugAnlegenAO.this.sitzplaetze.setText((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("sitzplaetze"));
                    FahrzeugAnlegenAO.this.maxBesatzung.setText((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("maxBesatzung"));
                    FahrzeugAnlegenAO.this.minBesatzung.setText((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("minBesatzung"));
                    kategorie.setSelectedItem(FahrzeugAnlegenAO.this.mapFahrzeug.get("beschreibung"));
                    FahrzeugAnlegenAO.this.funkRufName.setText((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("funkrufname"));
                    FahrzeugAnlegenAO.this.kennzeichen.setText((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("kennzeichen"));
                    FahrzeugAnlegenAO.this.sortierung.setValue(Integer.parseInt((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("sortierung")));
                    mandant.setSelectedItem(tabMandant.getMandantName(Integer.parseInt((String)FahrzeugAnlegenAO.this.mapFahrzeug.get("mandantID"))));
                    if (tabFahrzeuge.getFahrzeugAusserDienstStatus(fID) == 1) {
                        FahrzeugAnlegenAO.this.ausserDienst.setSelected(true);
                        FahrzeugAnlegenAO.this.buttonAusserDienst.setText("In Dienst");
                    } else {
                        FahrzeugAnlegenAO.this.ausserDienst.setSelected(false);
                        FahrzeugAnlegenAO.this.buttonAusserDienst.setText("Au\u00dfer Dienst");
                    }
                    if (tabFahrzeuge.getAnhaenger(fID) == 1) {
                        FahrzeugAnlegenAO.this.anhaenger.setSelected(true);
                    } else {
                        FahrzeugAnlegenAO.this.anhaenger.setSelected(false);
                    }
                    if (tabFahrzeuge.getTrupp(fID) == 1) {
                        FahrzeugAnlegenAO.this.trupp.setSelected(true);
                    } else {
                        FahrzeugAnlegenAO.this.trupp.setSelected(false);
                    }
                    String fschein = (String)FahrzeugAnlegenAO.this.mapFahrzeug.get("fuehrerschein");
                    if (fschein.equals("B")) {
                        FahrzeugAnlegenAO.this.klasseB.setSelected(true);
                    } else if (fschein.equals("C1")) {
                        FahrzeugAnlegenAO.this.klasseC1.setSelected(true);
                    } else if (fschein.equals("C1E")) {
                        FahrzeugAnlegenAO.this.klasseC1E.setSelected(true);
                    } else if (fschein.equals("C")) {
                        FahrzeugAnlegenAO.this.klasseC.setSelected(true);
                    } else if (fschein.equals("CE")) {
                        FahrzeugAnlegenAO.this.klasseCE.setSelected(true);
                    } else if (fschein.equals("BE")) {
                        FahrzeugAnlegenAO.this.klasseBE.setSelected(true);
                    }
                    FahrzeugAnlegenAO.this.sichtbarkeitSetzen(true);
                    FahrzeugAnlegenAO.this.buttonUntersuchung.setEnabled(true);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NumberFormatException e) {
                    tree.expandPath(tree.getSelectionPath());
                }
                catch (NullPointerException nullPointerException) {
                }
                catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
                    // empty catch block
                }
            }
        });
        this.buttonFahrzeugBeschreibung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0028");
                Steuerung.setStatus(Status.FAHRZEUG_BESCHREIBUNG);
                Steuerung.steuerung();
            }
        });
        this.buttonUntersuchung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.FAHRZUEG_UNTERSUCHUNG);
                Steuerung.steuerung();
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleFahrzeug tabelleFahrzeug = new TabelleFahrzeug();
                TabelleFahrzeug_beschreibung tabBeschreibung = new TabelleFahrzeug_beschreibung();
                Fahrzeug fahrzeug = new Fahrzeug();
                try {
                    if (Integer.parseInt(FahrzeugAnlegenAO.this.sitzplaetze.getText()) >= 10 && !FahrzeugAnlegenAO.this.anhaenger.isSelected()) {
                        logging.logInfo((Object)"Die maximale Anzehal der Sitzpl\u00e4tze stimmt nicht");
                        JOptionPane.showMessageDialog(null, Konstante.MAX_SITZPLATZ, "Warnung", 2);
                    } else if (Integer.parseInt(FahrzeugAnlegenAO.this.minBesatzung.getText()) <= 0 && !FahrzeugAnlegenAO.this.anhaenger.isSelected()) {
                        logging.logInfo((Object)"Die minimale Anzehal der Sitzpl\u00e4tze stimmt nicht");
                        JOptionPane.showMessageDialog(null, Konstante.MIN_SITZPLATZ, "Warnung", 2);
                    } else if (!(FahrzeugAnlegenAO.this.klasseB.isSelected() || FahrzeugAnlegenAO.this.klasseC1.isSelected() || FahrzeugAnlegenAO.this.klasseC1E.isSelected() || FahrzeugAnlegenAO.this.klasseC.isSelected() || FahrzeugAnlegenAO.this.klasseCE.isSelected() || FahrzeugAnlegenAO.this.klasseBE.isSelected())) {
                        logging.logInfo((Object)"Es wurde keine F\u00fchrerschein Kategorie ausgew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.FUEHRERSCHEINKATEGORIE_AUSWAEHLEN, "Warnung", 2);
                    } else if (kategorie.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_BESCHREIBUNG_ANGEBEN, "Warnung", 2);
                    } else if (FahrzeugAnlegenAO.this.trupp.isSelected() && !FahrzeugAnlegenAO.this.sitzplaetze.getText().equals("3") && !FahrzeugAnlegenAO.this.sitzplaetze.getText().equals("2")) {
                        JOptionPane.showMessageDialog(null, Konstante.BESATZUNG_TRUPPFAHRZEUG, "Fehlermeldung", 0);
                        FahrzeugAnlegenAO.this.sitzplaetze.setBackground(Color.red);
                    } else {
                        FahrzeugAnlegenAO.this.sitzplaetze.setBackground(Color.white);
                        FahrzeugAnlegenAO.this.minBesatzung.setBackground(Color.white);
                        FahrzeugAnlegenAO.this.maxBesatzung.setBackground(Color.white);
                        int fID = Integer.parseInt(fahrzeugID.getText());
                        int beschreibung = tabBeschreibung.getBeschreibungID(kategorie.getSelectedItem().toString());
                        fahrzeug.setId(fID);
                        fahrzeug.setName(name.getText());
                        fahrzeug.setBeschreibung(beschreibung);
                        fahrzeug.setKennzeichen(FahrzeugAnlegenAO.this.kennzeichen.getText());
                        fahrzeug.setFunkrufname(FahrzeugAnlegenAO.this.funkRufName.getText());
                        fahrzeug.setSitzplaetze(Integer.parseInt(FahrzeugAnlegenAO.this.sitzplaetze.getText()));
                        fahrzeug.setMinBesatzung(Integer.parseInt(FahrzeugAnlegenAO.this.minBesatzung.getText()));
                        fahrzeug.setMaxBesatzung(Integer.parseInt(FahrzeugAnlegenAO.this.maxBesatzung.getText()));
                        fahrzeug.setAusserDienst(0);
                        fahrzeug.setAnhaenger(FahrzeugAnlegenAO.this.anhaenger.isSelected() ? 1 : 0);
                        fahrzeug.setTrupp(FahrzeugAnlegenAO.this.trupp.isSelected() ? 1 : 0);
                        fahrzeug.setSortierung(FahrzeugAnlegenAO.this.sortierung.getValue());
                        fahrzeug.setMandantID(new TabelleMandant().getMandantID(mandant.getSelectedItem().toString()));
                        if (FahrzeugAnlegenAO.this.klasseB.isSelected()) {
                            fahrzeug.setFuehrerschein("B");
                        } else if (FahrzeugAnlegenAO.this.klasseC1.isSelected()) {
                            fahrzeug.setFuehrerschein("C1");
                        } else if (FahrzeugAnlegenAO.this.klasseC1E.isSelected()) {
                            fahrzeug.setFuehrerschein("C1E");
                        } else if (FahrzeugAnlegenAO.this.klasseC.isSelected()) {
                            fahrzeug.setFuehrerschein("C");
                        } else if (FahrzeugAnlegenAO.this.klasseCE.isSelected()) {
                            fahrzeug.setFuehrerschein("CE");
                        } else if (FahrzeugAnlegenAO.this.klasseBE.isSelected()) {
                            fahrzeug.setFuehrerschein("BE");
                        }
                        if (runApplication.PROPERTIES.get("MandantID").equals(FahrzeugAnlegenAO.this.mapFahrzeug.get("mandantID"))) {
                            tabelleFahrzeug.update(fahrzeug);
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID).mkdirs();
                        } else {
                            tabelleFahrzeug.updateFahrzeugUndMandant(fahrzeug);
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID).mkdirs();
                        }
                        PDFFahrzeugInfo.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_FahrzeugKarteAktualisiert.pdf", fahrzeug);
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_FahrzeugKarteAktualisiert.pdf");
                        tree.setModel(CreateTrees.CreateTreeFahrzeugListe());
                        tree.expandRow(beschreibung);
                        if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente").equals("1")) {
                            Joomla.erstelleFahrzeug(fahrzeug);
                        }
                        logging.logInfo((Object)"Fahrzeug wurde erfolgreich gespeichert");
                        logbuchEingabe.NeuerEintag("Fahrzeug aktualisiert: " + name.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (NumberFormatException e) {
                    FahrzeugAnlegenAO.this.sitzplaetze.setBackground(Color.red);
                    FahrzeugAnlegenAO.this.minBesatzung.setBackground(Color.red);
                    FahrzeugAnlegenAO.this.maxBesatzung.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.WERT_FALSCH_EINGEGEBEN_ZAHL, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (DocumentException | IOException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleFahrzeug tabelleFahrzeug = new TabelleFahrzeug();
                TabelleFahrzeug_beschreibung tabBeschreibung = new TabelleFahrzeug_beschreibung();
                Fahrzeug fahrzeug = new Fahrzeug();
                try {
                    if (FahrzeugAnlegenAO.this.sitzplaetze.getText().equals("") | FahrzeugAnlegenAO.this.minBesatzung.getText().equals("") | FahrzeugAnlegenAO.this.maxBesatzung.getText().equals("")) {
                        logging.logInfo((Object)"Es wurde keine Sitzplatzbelegungen angegeben");
                        JOptionPane.showMessageDialog(null, Konstante.SITZPLATZ, "Warnung", 2);
                    } else if (Integer.parseInt(FahrzeugAnlegenAO.this.sitzplaetze.getText()) >= 10 && !FahrzeugAnlegenAO.this.anhaenger.isSelected()) {
                        logging.logInfo((Object)"Die maximale Anzehal der Sitzpl\u00e4tze stimmt nicht");
                        JOptionPane.showMessageDialog(null, Konstante.MAX_SITZPLATZ, "Warnung", 2);
                    } else if (Integer.parseInt(FahrzeugAnlegenAO.this.minBesatzung.getText()) <= 0 && !FahrzeugAnlegenAO.this.anhaenger.isSelected()) {
                        logging.logInfo((Object)"Die minimale Anzehal der Sitzpl\u00e4tze stimmt nicht");
                        JOptionPane.showMessageDialog(null, Konstante.MIN_SITZPLATZ, "Warnung", 2);
                    } else if (!(FahrzeugAnlegenAO.this.klasseB.isSelected() || FahrzeugAnlegenAO.this.klasseC1.isSelected() || FahrzeugAnlegenAO.this.klasseC1E.isSelected() || FahrzeugAnlegenAO.this.klasseC.isSelected() || FahrzeugAnlegenAO.this.klasseCE.isSelected() || FahrzeugAnlegenAO.this.klasseBE.isSelected())) {
                        logging.logInfo((Object)"Es wurde keine F\u00fchrerschein Kategorie ausgew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.FUEHRERSCHEINKATEGORIE_AUSWAEHLEN, "Warnung", 2);
                    } else if (kategorie.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_BESCHREIBUNG_ANGEBEN, "Warnung", 2);
                    } else if (FahrzeugAnlegenAO.this.trupp.isSelected() && !FahrzeugAnlegenAO.this.sitzplaetze.getText().equals("3") && !FahrzeugAnlegenAO.this.sitzplaetze.getText().equals("2")) {
                        JOptionPane.showMessageDialog(null, Konstante.BESATZUNG_TRUPPFAHRZEUG, "Fehlermeldung", 0);
                        FahrzeugAnlegenAO.this.sitzplaetze.setBackground(Color.red);
                    } else {
                        FahrzeugAnlegenAO.this.sitzplaetze.setBackground(Color.white);
                        FahrzeugAnlegenAO.this.minBesatzung.setBackground(Color.white);
                        FahrzeugAnlegenAO.this.maxBesatzung.setBackground(Color.white);
                        int fID = tabelleFahrzeug.getNextNummer();
                        int beschreibung = tabBeschreibung.getBeschreibungID(kategorie.getSelectedItem().toString());
                        fahrzeug.setId(fID);
                        fahrzeug.setName(name.getText());
                        fahrzeug.setBeschreibung(beschreibung);
                        fahrzeug.setSitzplaetze(Integer.parseInt(FahrzeugAnlegenAO.this.sitzplaetze.getText()));
                        fahrzeug.setKennzeichen(FahrzeugAnlegenAO.this.kennzeichen.getText());
                        fahrzeug.setFunkrufname(FahrzeugAnlegenAO.this.funkRufName.getText());
                        fahrzeug.setMinBesatzung(Integer.parseInt(FahrzeugAnlegenAO.this.minBesatzung.getText()));
                        fahrzeug.setMaxBesatzung(Integer.parseInt(FahrzeugAnlegenAO.this.maxBesatzung.getText()));
                        fahrzeug.setAusserDienst(0);
                        fahrzeug.setAnhaenger(FahrzeugAnlegenAO.this.anhaenger.isSelected() ? 1 : 0);
                        fahrzeug.setTrupp(FahrzeugAnlegenAO.this.trupp.isSelected() ? 1 : 0);
                        fahrzeug.setSortierung(FahrzeugAnlegenAO.this.sortierung.getValue());
                        fahrzeug.setMandantID(new TabelleMandant().getMandantID(mandant.getSelectedItem().toString()));
                        if (FahrzeugAnlegenAO.this.klasseB.isSelected()) {
                            fahrzeug.setFuehrerschein("B");
                        } else if (FahrzeugAnlegenAO.this.klasseC1.isSelected()) {
                            fahrzeug.setFuehrerschein("C1");
                        } else if (FahrzeugAnlegenAO.this.klasseC1E.isSelected()) {
                            fahrzeug.setFuehrerschein("C1E");
                        } else if (FahrzeugAnlegenAO.this.klasseC.isSelected()) {
                            fahrzeug.setFuehrerschein("C");
                        } else if (FahrzeugAnlegenAO.this.klasseCE.isSelected()) {
                            fahrzeug.setFuehrerschein("CE");
                        } else if (FahrzeugAnlegenAO.this.klasseBE.isSelected()) {
                            fahrzeug.setFuehrerschein("BE");
                        }
                        tabelleFahrzeug.insert(fahrzeug);
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID, runApplication.clientID);
                        PDFFahrzeugInfo.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_FahrzeugKarte.pdf", fahrzeug);
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_FahrzeugKarte.pdf");
                        if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente").equals("1")) {
                            Joomla.erstelleFahrzeug(fahrzeug);
                        }
                        logging.logInfo((Object)"Fahrzeug wurde erfolgreich gespeichert");
                        logbuchEingabe.NeuerEintag("Fahrzeug neu angelegt: " + name.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        tree.setEnabled(true);
                        FahrzeugAnlegenAO.this.buttonAktualisieren.setVisible(true);
                        FahrzeugAnlegenAO.this.buttonSpeichern.setVisible(false);
                        FahrzeugAnlegenAO.this.buttonAbbruch.setVisible(false);
                        FahrzeugAnlegenAO.this.buttonUntersuchung.setEnabled(true);
                        tree.setModel(CreateTrees.CreateTreeFahrzeugListe());
                        tree.expandRow(beschreibung);
                        fahrzeugID.setText(Integer.toString(fID));
                    }
                }
                catch (NumberFormatException e) {
                    FahrzeugAnlegenAO.this.sitzplaetze.setBackground(Color.red);
                    FahrzeugAnlegenAO.this.minBesatzung.setBackground(Color.red);
                    FahrzeugAnlegenAO.this.maxBesatzung.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.WERT_FALSCH_EINGEGEBEN_ZAHL, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAusserDienst.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                try {
                    int fID = tabFahrzeug.getFahrzeugID(name.getText());
                    if (FahrzeugAnlegenAO.this.buttonAusserDienst.getText().equals("In Dienst")) {
                        tabFahrzeug.updateAusserDienst(fID, 0);
                        PDFFahrzeugInDienst.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_InDienstGestellt.pdf", name.getText());
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_InDienstGestellt.pdf");
                        logging.logInfo((Object)("Fahrzeug: " + name.getText() + "  wurde in Dienst gestellt"));
                        FahrzeugAnlegenAO.this.ausserDienst.setSelected(false);
                        FahrzeugAnlegenAO.this.buttonAusserDienst.setText("Au\u00dfer Dienst");
                        JOptionPane.showMessageDialog(null, Konstante.FAHRZEUG_IN_DIENST);
                    } else if (FahrzeugAnlegenAO.this.buttonAusserDienst.getText().equals("Au\u00dfer Dienst")) {
                        tabFahrzeug.updateAusserDienst(fID, 1);
                        PDFFahrzeugAusserDienst.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Au\u00dferDienstGestellt.pdf", name.getText());
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Au\u00dferDienstGestellt.pdf");
                        logging.logInfo((Object)("Fahrzeug: " + name.getText() + " wurde Ausser Dienst gestellt"));
                        FahrzeugAnlegenAO.this.ausserDienst.setSelected(true);
                        FahrzeugAnlegenAO.this.buttonAusserDienst.setText("In Dienst");
                        JOptionPane.showMessageDialog(null, Konstante.FAHRZEUG_AUSSER_DIENST);
                    }
                    logbuchEingabe.NeuerEintag("Fahrzeug Au\u00dfer Dienst Status ge\u00e4ndert: " + Integer.toString(FahrzeugAnlegenAO.this.ausserDienst.isSelected() ? 1 : 0) + " " + name.getText());
                }
                catch (DocumentException | IOException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNeu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                fahrzeugID.setText("0");
                tree.setEnabled(false);
                FahrzeugAnlegenAO.this.buttonAktualisieren.setVisible(false);
                FahrzeugAnlegenAO.this.buttonSpeichern.setVisible(true);
                FahrzeugAnlegenAO.this.buttonUntersuchung.setEnabled(false);
                FahrzeugAnlegenAO.this.buttonAbbruch.setVisible(true);
                name.setText(null);
                FahrzeugAnlegenAO.this.funkRufName.setText(null);
                FahrzeugAnlegenAO.this.kennzeichen.setText(null);
                FahrzeugAnlegenAO.this.sortierung.setValue(50);
                kategorie.setSelectedItem("<bitte w\u00e4hlen>");
                FahrzeugAnlegenAO.this.sitzplaetze.setText(null);
                FahrzeugAnlegenAO.this.maxBesatzung.setText(null);
                FahrzeugAnlegenAO.this.minBesatzung.setText(null);
                FahrzeugAnlegenAO.this.anhaenger.setSelected(false);
                FahrzeugAnlegenAO.this.trupp.setSelected(false);
                FahrzeugAnlegenAO.this.group.clearSelection();
                FahrzeugAnlegenAO.this.buttonUntersuchung.setEnabled(false);
                FahrzeugAnlegenAO.this.sichtbarkeitSetzen(true);
            }
        });
        this.buttonAbbruch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                tree.setSelectionRow(0);
                tree.setEnabled(true);
                FahrzeugAnlegenAO.this.buttonAktualisieren.setVisible(true);
                FahrzeugAnlegenAO.this.buttonSpeichern.setVisible(false);
                FahrzeugAnlegenAO.this.buttonUntersuchung.setEnabled(true);
                FahrzeugAnlegenAO.this.buttonAbbruch.setVisible(false);
                FahrzeugAnlegenAO.this.sichtbarkeitSetzen(false);
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

