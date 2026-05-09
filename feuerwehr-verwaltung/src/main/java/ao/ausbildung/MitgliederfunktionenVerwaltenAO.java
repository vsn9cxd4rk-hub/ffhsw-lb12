/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.ausbildung;

import ao.AbstractFenster;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import go.Lehrgang_Kategorie;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.Utils;
import utilities.logbuchEingabe;

public class MitgliederfunktionenVerwaltenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonLehrgangskonfiguration;
    private JButton buttonEhrungenKonfiguration;
    private JButton buttonLehrgangLoeschen;
    private JTextField name;
    public static String letzterLehrgang;
    private JLabel beschreibung;
    private JLabel unterrichtseinheiten_label;
    private JTextField unterrichtseinheiten;
    private JLabel relevant_label;
    private JLabel lehrgangAnlegen_beschreibung;
    private JLabel lehrgangLoeschen_beschreibung;
    private JLabel lehrgangLoeschen_label;
    private JComboBox<String> lehrgangLoeschen;
    private JRadioButton kategorieLehrgang;
    private JRadioButton kategorieFunktion;
    private JRadioButton kategorieFunktionAu\u00dferhalb;
    private JRadioButton kategorieSeminar;
    private JRadioButton kategorieEinweisung;
    private JRadioButton kategorieFuehrerschein;
    private JRadioButton kategorieEhrung;
    private JRadioButton kategorieAbzeichen;
    private ButtonGroup group;
    private JCheckBox relevant;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JLabel dummy4;
    private JLabel dummy5;
    private JLabel dummy6;
    private JPanel panelLehrgang;
    private JPanel panelOptionen;

    public MitgliederfunktionenVerwaltenAO() {
        super("FeuerwehrManagementSystem - Mitgliederfunktionen Verwalten");
        logging.logInfo((Object)"Starte: LehrgangAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonLehrgangskonfiguration = new JButton("Lehrgangskonfiguration");
        this.buttonEhrungenKonfiguration = new JButton("Ehrungenkonfiguration");
        this.buttonLehrgangLoeschen = new JButton("L\u00f6schen");
        this.relevant_label = new JLabel("Relevant f\u00fcr Lehrgangsmeldung: ");
        this.unterrichtseinheiten_label = new JLabel("Unterrichtseinheiten (1xUE = 45min.): ");
        this.relevant = new JCheckBox();
        this.unterrichtseinheiten = new JTextField("0", 20);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Mitgliederfunktionsname: ");
        this.kategorieFunktion = new JRadioButton("Funktion in der Feuerwehr");
        this.kategorieFunktionAu\u00dferhalb = new JRadioButton("Funktion au\u00dferhalb der Feuerwehr");
        this.kategorieLehrgang = new JRadioButton("Lehrgang");
        this.kategorieSeminar = new JRadioButton("Seminar");
        this.kategorieEinweisung = new JRadioButton("Einweisung");
        this.kategorieFuehrerschein = new JRadioButton("F\u00fchrerschein");
        this.kategorieEhrung = new JRadioButton("Ehrung");
        this.kategorieAbzeichen = new JRadioButton("Abzeichen");
        this.lehrgangAnlegen_beschreibung = new JLabel("Mitgliederfunktion anlegen");
        this.lehrgangLoeschen_beschreibung = new JLabel("Mitgliederfunktion l\u00f6schen");
        this.lehrgangLoeschen_label = new JLabel("Mitgliederfunktionsliste: ");
        this.group = new ButtonGroup();
        this.group.add(this.kategorieFunktion);
        this.group.add(this.kategorieFunktionAu\u00dferhalb);
        this.group.add(this.kategorieLehrgang);
        this.group.add(this.kategorieSeminar);
        this.group.add(this.kategorieEinweisung);
        this.group.add(this.kategorieFuehrerschein);
        this.group.add(this.kategorieEhrung);
        this.group.add(this.kategorieAbzeichen);
        this.modulBeschreibung = new JLabel("Mitgliederfunktionen Verwalten");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.dummy4 = new JLabel(runApplication.dummyImage);
        this.dummy5 = new JLabel(runApplication.dummyImage);
        this.dummy6 = new JLabel(runApplication.dummyImage);
        try {
            TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
            String[] lehrgangListe = Utils.listToArrayOnlyFORComboBoxes(tabLehrgangKategorie.getAlleLehrg\u00e4ngeByName());
            this.lehrgangLoeschen = new JComboBox<String>(lehrgangListe);
            String[] ehrungListe = Utils.listToArrayOnlyFORComboBoxes(tabLehrgangKategorie.getAlleEhrungenAbzeichen());
            int e = 0;
            while (e < ehrungListe.length) {
                this.lehrgangLoeschen.addItem(ehrungListe[e]);
                ++e;
            }
            this.lehrgangLoeschen.setPreferredSize(new Dimension(350, 25));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.setSize(550, 550);
        this.setTitle("FeuerwehrManagementSystem - Mitgliederfunktionen Verwalten");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonLehrgangskonfiguration);
        this.add(this.buttonEhrungenKonfiguration);
        this.add(this.dummy3);
        this.add(this.lehrgangAnlegen_beschreibung);
        this.add(this.dummy5);
        this.panelLehrgang = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panelLehrgang);
        this.panelLehrgang.add(this.beschreibung);
        this.panelLehrgang.add(this.name);
        this.panelLehrgang.add(this.unterrichtseinheiten_label);
        this.panelLehrgang.add(this.unterrichtseinheiten);
        this.panelLehrgang.add(this.relevant_label);
        this.panelLehrgang.add(this.relevant);
        this.panelOptionen = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", this.panelOptionen);
        this.panelOptionen.add(this.kategorieFunktion);
        this.panelOptionen.add(this.kategorieFunktionAu\u00dferhalb);
        this.panelOptionen.add(this.kategorieLehrgang);
        this.panelOptionen.add(this.kategorieFuehrerschein);
        this.panelOptionen.add(this.kategorieSeminar);
        this.panelOptionen.add(this.kategorieEinweisung);
        this.panelOptionen.add(this.kategorieEhrung);
        this.panelOptionen.add(this.kategorieAbzeichen);
        this.add(this.buttonSpeichern);
        this.add(this.dummy4);
        this.add(this.lehrgangLoeschen_beschreibung);
        this.add(this.dummy6);
        this.add(this.lehrgangLoeschen_label);
        this.add(this.lehrgangLoeschen);
        this.add(this.buttonLehrgangLoeschen);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();
                try {
                    if (!(MitgliederfunktionenVerwaltenAO.this.kategorieFunktion.isSelected() || MitgliederfunktionenVerwaltenAO.this.kategorieFunktionAu\u00dferhalb.isSelected() || MitgliederfunktionenVerwaltenAO.this.kategorieLehrgang.isSelected() || MitgliederfunktionenVerwaltenAO.this.kategorieSeminar.isSelected() || MitgliederfunktionenVerwaltenAO.this.kategorieEinweisung.isSelected() || MitgliederfunktionenVerwaltenAO.this.kategorieFuehrerschein.isSelected() || MitgliederfunktionenVerwaltenAO.this.kategorieEhrung.isSelected() || MitgliederfunktionenVerwaltenAO.this.kategorieAbzeichen.isSelected())) {
                        logging.logInfo((Object)"Lehrgnag od. Funktion wurde nicht gew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.LEHRGANG_FUNKTION_FEHLER, "Fehlermeldung", 0);
                    } else if (tabLehrgangKategorie.getCountByName(MitgliederfunktionenVerwaltenAO.this.name.getText()) != 0) {
                        logging.logInfo((Object)"Es wurde kein Name eingegeben");
                        JOptionPane.showMessageDialog(null, Konstante.LEHRGANG_EXISTIERT_BEREITS, "Warnung", 2);
                    } else if (!MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.getText().toString().matches("[+-]?[0-9]+")) {
                        MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_UNTERRICHTSEINHEITEN, "Warnung", 2);
                    } else {
                        MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.setBackground(Color.white);
                        int lNummer = tabLehrgangKategorie.getNextNummer();
                        kategorie.setId(lNummer);
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieFunktion.isSelected()) {
                            kategorie.setArt("F");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieFunktionAu\u00dferhalb.isSelected()) {
                            kategorie.setArt("F_Au\u00dferhalb");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieLehrgang.isSelected()) {
                            kategorie.setArt("L");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieSeminar.isSelected()) {
                            kategorie.setArt("S");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieEinweisung.isSelected()) {
                            kategorie.setArt("E");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieFuehrerschein.isSelected()) {
                            kategorie.setArt("F\u00fc");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieEhrung.isSelected()) {
                            kategorie.setArt("EH");
                            MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.setText("0");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.kategorieAbzeichen.isSelected()) {
                            kategorie.setArt("AB");
                            MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.setText("0");
                        }
                        if (MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.getText().equals("")) {
                            kategorie.setUe(0);
                        } else {
                            kategorie.setUe(Integer.parseInt(MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.getText()));
                        }
                        kategorie.setName(MitgliederfunktionenVerwaltenAO.this.name.getText());
                        if (MitgliederfunktionenVerwaltenAO.this.relevant.isSelected()) {
                            kategorie.setRelevant(1);
                            kategorie.setReihenfolge(tabLehrgangKategorie.getNextReihenfolgenummerNummer());
                        } else {
                            kategorie.setRelevant(0);
                            kategorie.setReihenfolge(0);
                        }
                        kategorie.setLoeschbar(0);
                        kategorie.setLoeschkenner(0);
                        tabLehrgangKategorie.insert(kategorie);
                        MitgliederfunktionenVerwaltenAO.this.lehrgangLoeschen.addItem(MitgliederfunktionenVerwaltenAO.this.name.getText());
                        MitgliederfunktionenVerwaltenAO.this.name.setText(null);
                        MitgliederfunktionenVerwaltenAO.this.unterrichtseinheiten.setText("0");
                        MitgliederfunktionenVerwaltenAO.this.relevant.setSelected(false);
                        MitgliederfunktionenVerwaltenAO.this.group.clearSelection();
                        logging.logInfo((Object)"Lehrgang erfogreich gespeichert");
                        logbuchEingabe.NeuerEintag("Lehrgang wurde angelegt: " + letzterLehrgang);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonLehrgangLoeschen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                    TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                    Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();
                    int lNummer = tabLehrgangKategorie.getLehrgangID(MitgliederfunktionenVerwaltenAO.this.lehrgangLoeschen.getSelectedItem().toString());
                    if (MitgliederfunktionenVerwaltenAO.this.lehrgangLoeschen.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_LEHRGANG_AUSWAEHLEN, "Warnung", 2);
                    } else if (tabLehrgangKategorie.getloeschbarStatus(lNummer) == 1) {
                        JOptionPane.showMessageDialog(null, Konstante.LEHRGANG_NICHT_LOESCHBAR_FAHRZEUGEINTEILUNG, "Warnung", 2);
                    } else if (tabLehrgangKategorie.getRelevantStatus(lNummer) == 1) {
                        JOptionPane.showMessageDialog(null, Konstante.LEHRGANG_NICHT_LOESCHBAR_RELEVANT, "Warnung", 2);
                    } else if (tabLaufbahn.getCountOfLehrgang(lNummer) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.LEHRGANG_NICHT_LOESCHBAR_ZUGEWIESEN, "Warnung", 2);
                    } else {
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.LOESCHEN_BESTAETIGEN, "Frage", 0);
                        if (msg == 0) {
                            kategorie.setId(lNummer);
                            kategorie.setLoeschkenner(1);
                            tabLehrgangKategorie.updateLoeschkenner(kategorie);
                            JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                            MitgliederfunktionenVerwaltenAO.this.lehrgangLoeschen.removeItem(MitgliederfunktionenVerwaltenAO.this.lehrgangLoeschen.getSelectedItem().toString());
                            MitgliederfunktionenVerwaltenAO.this.lehrgangLoeschen.setSelectedItem("<bitte w\u00e4hlen>");
                        }
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                }
            }
        });
        this.buttonLehrgangskonfiguration.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.LEHRGANG_KONFIGURIEREN);
                Steuerung.steuerung();
            }
        });
        this.buttonEhrungenKonfiguration.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.EHRUNGEN_KONFIGURATION);
                Steuerung.steuerung();
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

