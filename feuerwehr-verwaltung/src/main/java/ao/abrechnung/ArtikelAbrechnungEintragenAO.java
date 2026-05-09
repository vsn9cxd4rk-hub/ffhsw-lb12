/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.abrechnung;

import ao.AbstractFenster;
import data.tabellen.abrechnung.TabelleAbrechnung_artikel;
import data.tabellen.abrechnung.TabelleAbrechnung_artikelklassen;
import data.tabellen.abrechnung.TabelleAbrechnung_konto;
import go.abrechnung.ArtikelAbrechnung;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import utilities.MoneyCalculation;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class ArtikelAbrechnungEintragenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static String letzterArtikelName;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAktualisieren;
    private JButton buttonArtikelklasseAnlegen;
    private JButton buttonKontoAnlegen;
    private JButton buttonNaechstenArtikel;
    private JTextField id;
    private JTextField name;
    public static JComboBox<String> klasse;
    public static JComboBox<String> buchungskonto;
    private JComboBox<String> zahlungsart;
    private JTextField wert;
    private JComboBox<String> artikel;
    private JRadioButton stuendlichBerechnen;
    private JRadioButton pauschalBerechnen;
    private JRadioButton variablerPreis;
    private JLabel pauschalBerechnen_label;
    private JLabel stuendlichBerechnen_label;
    private JLabel variablerPreis_label;
    private JRadioButton berechnungMiniutenGenau;
    private JRadioButton berechnungJedeAngefangendeStunde;
    private ButtonGroup bgBerechnungsart;
    private ButtonGroup bgBerechnungsart2;
    private JCheckBox aktiviert;
    private JTextField gueltigvon;
    private JTextField gueltigbis;
    private JLabel id_label;
    private JLabel name_label;
    private JLabel klasse_label;
    private JLabel wert_label;
    private JLabel artikel_label;
    private JLabel aktiviert_label;
    private JLabel berechnungJedeAngefangendeStunde_label;
    private JLabel berechnungMiniutenGenau_label;
    private JLabel buchungskonto_label;
    private JLabel zahlungsart_label;
    private JLabel gueltigvon_label;
    private JLabel gueltigbis_label;
    private JTextField automatischerEinbehalt;
    private JRadioButton automatischerEinbehalt_pauschal;
    private JRadioButton automatischerEinbehalt_st\u00fcndlich;
    private ButtonGroup bgAutomatischerEinbehalt;
    private JLabel automatischEinbehalten_label;
    private JLabel automatischerEinbehalt_pauschal_label;
    private JLabel automatischerEinbehalt_st\u00fcndlich_label;
    private JPanel panelArtikel;
    private JPanel panelAutomatischEinbehalten;
    private JPanel panelGueltigkeit;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;

    public ArtikelAbrechnungEintragenAO() {
        super("FeuerwehrManagementSystem - Artikel");
        logging.logInfo((Object)"Starte: ArtikelAbrechnungEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonNaechstenArtikel = new JButton("N\u00e4chsten Artikel");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonArtikelklasseAnlegen = new JButton("Neue Artikelklasse");
        this.buttonKontoAnlegen = new JButton("Konto anlegen");
        this.zahlungsart_label = new JLabel("Zahlungsart vom Buchungskonto: ");
        this.buchungskonto_label = new JLabel("Buchungskonto: ");
        this.stuendlichBerechnen = new JRadioButton();
        this.pauschalBerechnen = new JRadioButton();
        this.variablerPreis = new JRadioButton();
        this.berechnungJedeAngefangendeStunde = new JRadioButton();
        this.berechnungMiniutenGenau = new JRadioButton();
        this.aktiviert = new JCheckBox();
        this.bgBerechnungsart = new ButtonGroup();
        this.bgBerechnungsart.add(this.pauschalBerechnen);
        this.bgBerechnungsart.add(this.stuendlichBerechnen);
        this.bgBerechnungsart.add(this.variablerPreis);
        this.bgBerechnungsart2 = new ButtonGroup();
        this.bgBerechnungsart2.add(this.berechnungJedeAngefangendeStunde);
        this.bgBerechnungsart2.add(this.berechnungMiniutenGenau);
        this.id = new JTextField(25);
        this.name = new JTextField(25);
        this.wert = new JTextField(25);
        this.id_label = new JLabel("Artikelnummer: ");
        this.name_label = new JLabel("Name: ");
        this.klasse_label = new JLabel("Artikelklasse: ");
        this.wert_label = new JLabel("Wert in \u20ac: ");
        this.artikel_label = new JLabel("Artikel: ");
        this.aktiviert_label = new JLabel("Artikel aktivieren: ");
        this.berechnungJedeAngefangendeStunde_label = new JLabel(" --> Berechnung Angefangende Stunden kompl.:");
        this.berechnungMiniutenGenau_label = new JLabel(" --> Berechnung Minutengenau: ");
        this.pauschalBerechnen_label = new JLabel("Wert in \u20ac ist eine Pauschale: ");
        this.variablerPreis_label = new JLabel("Wert in \u20ac ist Variabel: ");
        this.stuendlichBerechnen_label = new JLabel("Wert in \u20ac ist f\u00fcr eine Stunde: ");
        this.automatischerEinbehalt = new JTextField("0,00", 25);
        this.automatischerEinbehalt_pauschal = new JRadioButton();
        this.automatischerEinbehalt_st\u00fcndlich = new JRadioButton();
        this.bgAutomatischerEinbehalt = new ButtonGroup();
        this.automatischEinbehalten_label = new JLabel("Automatisch Einbehalten in \u20ac: ");
        this.automatischerEinbehalt_pauschal_label = new JLabel("Pauschale Einbehalten: ");
        this.automatischerEinbehalt_st\u00fcndlich_label = new JLabel("Stundeabh\u00e4ngig Einbehalten: ");
        this.bgAutomatischerEinbehalt.add(this.automatischerEinbehalt_pauschal);
        this.bgAutomatischerEinbehalt.add(this.automatischerEinbehalt_st\u00fcndlich);
        this.gueltigbis = new JTextField(25);
        this.gueltigvon = new JTextField(25);
        this.gueltigbis_label = new JLabel("G\u00fcltigkeit bis: ");
        this.gueltigvon_label = new JLabel("G\u00fcltigkeit von: ");
        this.modulBeschreibung = new JLabel("Artikel");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        try {
            TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
            TabelleAbrechnung_artikelklassen tabArtikelklassen = new TabelleAbrechnung_artikelklassen();
            TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
            String[] artikelListe = Utils.listToArrayOnlyFORComboBoxes(tabArtikel.getAllArtikel());
            String[] klassenListe = Utils.listToArrayOnlyFORComboBoxes(tabArtikelklassen.getAllKategorien());
            String[] kontoListe = Utils.listToArrayOnlyFORComboBoxes(tabKonto.getAllKontos());
            String[] zahlungsartListe = new String[]{"Einzahlung", "Auszahlung"};
            this.artikel = new JComboBox<String>(artikelListe);
            klasse = new JComboBox<String>(klassenListe);
            buchungskonto = new JComboBox<String>(kontoListe);
            this.zahlungsart = new JComboBox<String>(zahlungsartListe);
            if (!MyEvent.event.equals("0x0300")) {
                this.id.setText(Integer.toString(tabArtikel.getNextNummer()));
            }
            this.id.setEditable(false);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelHinzufuegen() {
        this.artikel.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                TabelleAbrechnung_artikelklassen tabArtikelklassen = new TabelleAbrechnung_artikelklassen();
                try {
                    int artID = tabArtikel.getArtikelID(ArtikelAbrechnungEintragenAO.this.artikel.getSelectedItem().toString());
                    ArtikelAbrechnungEintragenAO.this.id.setText(Integer.toString(artID));
                    ArtikelAbrechnungEintragenAO.this.name.setText(tabArtikel.getArtikelName(artID));
                    klasse.setSelectedItem(tabArtikelklassen.getName(tabArtikel.getArtikelKlasse(artID)));
                    ArtikelAbrechnungEintragenAO.this.wert.setText(MoneyCalculation.parseMoneyVauleForGUI(tabArtikel.getArtikelWert(artID)));
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt.setText(MoneyCalculation.parseMoneyVauleForGUI(tabArtikel.getArtikelRabattWert(artID)));
                    buchungskonto.setSelectedItem(tabArtikel.getBuchungskontoName(artID));
                    ArtikelAbrechnungEintragenAO.this.gueltigvon.setText(TimeCalculation.parseDateForGUI(tabArtikel.getGueltigVon(artID)));
                    String gueltigBisDatum = tabArtikel.getGueltigBis(artID);
                    if (gueltigBisDatum.equals("2099-12-31")) {
                        ArtikelAbrechnungEintragenAO.this.gueltigbis.setText("");
                    } else {
                        ArtikelAbrechnungEintragenAO.this.gueltigbis.setText(TimeCalculation.parseDateForGUI(gueltigBisDatum));
                    }
                    if (tabArtikel.getAktiv(artID) == 1) {
                        ArtikelAbrechnungEintragenAO.this.aktiviert.setSelected(true);
                    } else {
                        ArtikelAbrechnungEintragenAO.this.aktiviert.setSelected(false);
                    }
                    int zahlart = tabArtikel.getZahlungsart(artID);
                    if (zahlart == 1) {
                        ArtikelAbrechnungEintragenAO.this.zahlungsart.setSelectedItem("Einzahlung");
                    } else if (zahlart == 2) {
                        ArtikelAbrechnungEintragenAO.this.zahlungsart.setSelectedItem("Auszahlung");
                    }
                    ArtikelAbrechnungEintragenAO.this.bgBerechnungsart.clearSelection();
                    int dbBerechnungsart = tabArtikel.getArtikelBerechnungsart(artID);
                    if (dbBerechnungsart == 1) {
                        ArtikelAbrechnungEintragenAO.this.stuendlichBerechnen.setSelected(true);
                    } else if (dbBerechnungsart == 2) {
                        ArtikelAbrechnungEintragenAO.this.pauschalBerechnen.setSelected(true);
                    } else if (dbBerechnungsart == 3) {
                        ArtikelAbrechnungEintragenAO.this.variablerPreis.setSelected(true);
                    }
                    ArtikelAbrechnungEintragenAO.this.bgBerechnungsart2.clearSelection();
                    int dbBerechnungsart2 = tabArtikel.getArtikelBerechnungsart2(artID);
                    if (dbBerechnungsart2 == 1) {
                        ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.setSelected(true);
                    } else if (dbBerechnungsart2 == 2) {
                        ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.setSelected(true);
                    } else {
                        ArtikelAbrechnungEintragenAO.this.bgBerechnungsart2.clearSelection();
                    }
                    ArtikelAbrechnungEintragenAO.this.bgAutomatischerEinbehalt.clearSelection();
                    int dbAutomatischerEinbehalt = tabArtikel.getArtikelrabattArt(artID);
                    if (dbAutomatischerEinbehalt == 1) {
                        ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setSelected(true);
                    } else if (dbAutomatischerEinbehalt == 2) {
                        ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_st\u00fcndlich.setSelected(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Artikel");
        this.setSize(600, 700);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonKontoAnlegen);
        this.add(this.buttonArtikelklasseAnlegen);
        this.add(this.dummy3);
        this.panelArtikel = new JPanel(new GridLayout(14, 2));
        this.getContentPane().add("Center", this.panelArtikel);
        this.panelArtikel.add(this.artikel_label);
        this.panelArtikel.add(this.artikel);
        this.panelArtikel.add(this.id_label);
        this.panelArtikel.add(this.id);
        this.panelArtikel.add(this.name_label);
        this.panelArtikel.add(this.name);
        this.panelArtikel.add(this.aktiviert_label);
        this.panelArtikel.add(this.aktiviert);
        this.panelArtikel.add(this.buchungskonto_label);
        this.panelArtikel.add(buchungskonto);
        this.panelArtikel.add(this.zahlungsart_label);
        this.panelArtikel.add(this.zahlungsart);
        this.panelArtikel.add(this.klasse_label);
        this.panelArtikel.add(klasse);
        this.panelArtikel.add(this.variablerPreis_label);
        this.panelArtikel.add(this.variablerPreis);
        this.panelArtikel.add(this.pauschalBerechnen_label);
        this.panelArtikel.add(this.pauschalBerechnen);
        this.panelArtikel.add(this.stuendlichBerechnen_label);
        this.panelArtikel.add(this.stuendlichBerechnen);
        this.panelArtikel.add(this.berechnungJedeAngefangendeStunde_label);
        this.panelArtikel.add(this.berechnungJedeAngefangendeStunde);
        this.panelArtikel.add(this.berechnungMiniutenGenau_label);
        this.panelArtikel.add(this.berechnungMiniutenGenau);
        this.panelArtikel.add(this.wert_label);
        this.panelArtikel.add(this.wert);
        this.panelAutomatischEinbehalten = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", this.panelAutomatischEinbehalten);
        this.panelAutomatischEinbehalten.add(this.automatischEinbehalten_label);
        this.panelAutomatischEinbehalten.add(this.automatischerEinbehalt);
        this.panelAutomatischEinbehalten.add(this.automatischerEinbehalt_pauschal_label);
        this.panelAutomatischEinbehalten.add(this.automatischerEinbehalt_pauschal);
        this.panelAutomatischEinbehalten.add(this.automatischerEinbehalt_st\u00fcndlich_label);
        this.panelAutomatischEinbehalten.add(this.automatischerEinbehalt_st\u00fcndlich);
        this.panelGueltigkeit = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panelGueltigkeit);
        this.panelGueltigkeit.add(this.gueltigvon_label);
        this.panelGueltigkeit.add(this.gueltigvon);
        this.panelGueltigkeit.add(this.gueltigbis_label);
        this.panelGueltigkeit.add(this.gueltigbis);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonNaechstenArtikel);
        this.add(this.buttonAktualisieren);
        if (MyEvent.event.equals("0x0300")) {
            this.buttonAktualisieren.setVisible(true);
            this.buttonSpeichern.setVisible(false);
            this.artikel.setVisible(true);
            this.artikel_label.setVisible(true);
        } else {
            this.buttonAktualisieren.setVisible(false);
            this.artikel.setVisible(false);
            this.artikel_label.setVisible(false);
            this.gueltigvon.setText(SbcUtils.timeStamp((String)"dd.MM.yyyy"));
        }
        this.automatischerEinbehalt_pauschal.setEnabled(true);
        this.buttonNaechstenArtikel.setVisible(false);
    }

    protected void boxenHinzufuegen() {
        this.pauschalBerechnen.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (ArtikelAbrechnungEintragenAO.this.pauschalBerechnen.isSelected()) {
                    ArtikelAbrechnungEintragenAO.this.bgBerechnungsart2.clearSelection();
                    ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.setEnabled(false);
                    ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.setEnabled(false);
                    ArtikelAbrechnungEintragenAO.this.wert.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setSelected(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setEnabled(false);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_st\u00fcndlich.setEnabled(false);
                } else {
                    ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.wert.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setSelected(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_st\u00fcndlich.setEnabled(true);
                }
            }
        });
        this.variablerPreis.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (ArtikelAbrechnungEintragenAO.this.variablerPreis.isSelected()) {
                    ArtikelAbrechnungEintragenAO.this.bgBerechnungsart2.clearSelection();
                    ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.setEnabled(false);
                    ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.setEnabled(false);
                    ArtikelAbrechnungEintragenAO.this.wert.setText("0,00");
                    ArtikelAbrechnungEintragenAO.this.wert.setEnabled(false);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setSelected(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setEnabled(false);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_st\u00fcndlich.setEnabled(false);
                } else {
                    ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.wert.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setSelected(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.setEnabled(true);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_st\u00fcndlich.setEnabled(true);
                }
            }
        });
        klasse.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleAbrechnung_artikelklassen tabKlasse = new TabelleAbrechnung_artikelklassen();
                    int artKlasse = tabKlasse.getID(klasse.getSelectedItem().toString());
                    if (artKlasse >= 50) {
                        ArtikelAbrechnungEintragenAO.this.stuendlichBerechnen.setEnabled(false);
                        ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.setEnabled(false);
                        ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.setEnabled(false);
                        ArtikelAbrechnungEintragenAO.this.bgBerechnungsart.clearSelection();
                        ArtikelAbrechnungEintragenAO.this.bgBerechnungsart2.clearSelection();
                    } else {
                        ArtikelAbrechnungEintragenAO.this.stuendlichBerechnen.setEnabled(true);
                        ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.setEnabled(true);
                        ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.setEnabled(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                ArtikelAbrechnungEintragenAO.this.dispose();
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                ArtikelAbrechnung artikel = new ArtikelAbrechnung();
                TabelleAbrechnung_artikelklassen tabArtikelklassen = new TabelleAbrechnung_artikelklassen();
                TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
                try {
                    int kID = tabArtikelklassen.getID(klasse.getSelectedItem().toString());
                    if (klasse.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_KATEGORIE_WAEHLEN, "Warnung", 2);
                    } else if (buchungskonto.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_KONTO_WAEHLEN, "Warnung", 2);
                    } else if (ArtikelAbrechnungEintragenAO.this.wert.getText().equals("") || ArtikelAbrechnungEintragenAO.this.wert.getText().toString().matches("',*'")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_BETRAG_KONTROLLIEREN, "Warnung", 2);
                    } else if (!(ArtikelAbrechnungEintragenAO.this.stuendlichBerechnen.isSelected() || ArtikelAbrechnungEintragenAO.this.pauschalBerechnen.isSelected() || ArtikelAbrechnungEintragenAO.this.variablerPreis.isSelected())) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_ZAHLUNGSART_KONTROLLIEREN, "Warnung", 2);
                    } else if (tabArtikel.getArtikelCountByName(ArtikelAbrechnungEintragenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NAME_EXISTIERT_BEREITS, "Warnung", 2);
                    } else if (tabArtikel.getKlasseCount(kID) != 0 && kID <= 100) {
                        JOptionPane.showMessageDialog(null, Konstante.DIESE_ARTIKELKLASSE_EXISTIERT_BEREITS, "Warnung", 2);
                    } else if (!TimeCalculation.checkDateFormat(ArtikelAbrechnungEintragenAO.this.gueltigvon.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                        ArtikelAbrechnungEintragenAO.this.gueltigvon.setBackground(Color.red);
                    } else if (!ArtikelAbrechnungEintragenAO.this.gueltigbis.getText().equals("") && !TimeCalculation.checkDateFormat(ArtikelAbrechnungEintragenAO.this.gueltigbis.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                        ArtikelAbrechnungEintragenAO.this.gueltigbis.setBackground(Color.red);
                    } else {
                        ArtikelAbrechnungEintragenAO.this.gueltigvon.setBackground(Color.white);
                        ArtikelAbrechnungEintragenAO.this.gueltigbis.setBackground(Color.white);
                        artikel.setId(Integer.parseInt(ArtikelAbrechnungEintragenAO.this.id.getText()));
                        artikel.setKlasse(kID);
                        artikel.setName(ArtikelAbrechnungEintragenAO.this.name.getText());
                        artikel.setBuchungskonto(tabKonto.getID(buchungskonto.getSelectedItem().toString()));
                        artikel.setWert(MoneyCalculation.parseMoneyVauleForDatabase(ArtikelAbrechnungEintragenAO.this.wert.getText()));
                        artikel.setRabattwert(MoneyCalculation.parseMoneyVauleForDatabase(ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt.getText()));
                        artikel.setMwst(1);
                        artikel.setAktiv(ArtikelAbrechnungEintragenAO.this.aktiviert.isSelected() ? 1 : 0);
                        artikel.setVon(TimeCalculation.parseDateForDatabase(ArtikelAbrechnungEintragenAO.this.gueltigvon.getText()));
                        if (ArtikelAbrechnungEintragenAO.this.gueltigbis.getText().equals("")) {
                            artikel.setBis("2099-12-31");
                        } else {
                            artikel.setBis(TimeCalculation.parseDateForDatabase(ArtikelAbrechnungEintragenAO.this.gueltigbis.getText()));
                        }
                        if (ArtikelAbrechnungEintragenAO.this.stuendlichBerechnen.isSelected()) {
                            artikel.setBerechnungsart(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.pauschalBerechnen.isSelected()) {
                            artikel.setBerechnungsart(2);
                        } else if (ArtikelAbrechnungEintragenAO.this.variablerPreis.isSelected()) {
                            artikel.setBerechnungsart(3);
                        }
                        if (ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.isSelected()) {
                            artikel.setBerechnungsart2(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.isSelected()) {
                            artikel.setBerechnungsart2(2);
                        } else {
                            artikel.setBerechnungsart2(0);
                        }
                        if (ArtikelAbrechnungEintragenAO.this.zahlungsart.getSelectedItem().toString().equals("Einzahlung")) {
                            artikel.setZahlungsart(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.zahlungsart.getSelectedItem().toString().equals("Auszahlung")) {
                            artikel.setZahlungsart(2);
                        }
                        if (ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.isSelected()) {
                            artikel.setRabattart(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_st\u00fcndlich.isSelected()) {
                            artikel.setRabattart(2);
                        }
                        tabArtikel.insert(artikel);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        ArtikelAbrechnungEintragenAO.this.buttonSpeichern.setEnabled(false);
                        ArtikelAbrechnungEintragenAO.this.buttonNaechstenArtikel.setVisible(true);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                ArtikelAbrechnung artikel = new ArtikelAbrechnung();
                TabelleAbrechnung_artikelklassen tabArtikelklassen = new TabelleAbrechnung_artikelklassen();
                TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
                try {
                    int kID = tabArtikelklassen.getID(klasse.getSelectedItem().toString());
                    if (klasse.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_KATEGORIE_WAEHLEN, "Warnung", 2);
                    } else if (ArtikelAbrechnungEintragenAO.this.wert.getText().equals("") || ArtikelAbrechnungEintragenAO.this.wert.getText().toString().matches("',*'")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_BETRAG_KONTROLLIEREN, "Warnung", 2);
                    } else if (!TimeCalculation.checkDateFormat(ArtikelAbrechnungEintragenAO.this.gueltigvon.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                        ArtikelAbrechnungEintragenAO.this.gueltigvon.setBackground(Color.red);
                    } else if (!ArtikelAbrechnungEintragenAO.this.gueltigbis.getText().equals("") && !TimeCalculation.checkDateFormat(ArtikelAbrechnungEintragenAO.this.gueltigbis.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                        ArtikelAbrechnungEintragenAO.this.gueltigbis.setBackground(Color.red);
                    } else {
                        ArtikelAbrechnungEintragenAO.this.gueltigvon.setBackground(Color.white);
                        ArtikelAbrechnungEintragenAO.this.gueltigbis.setBackground(Color.white);
                        artikel.setId(Integer.parseInt(ArtikelAbrechnungEintragenAO.this.id.getText()));
                        artikel.setKlasse(kID);
                        artikel.setName(ArtikelAbrechnungEintragenAO.this.name.getText());
                        artikel.setBuchungskonto(tabKonto.getID(buchungskonto.getSelectedItem().toString()));
                        artikel.setWert(MoneyCalculation.parseMoneyVauleForDatabase(ArtikelAbrechnungEintragenAO.this.wert.getText()));
                        artikel.setRabattwert(MoneyCalculation.parseMoneyVauleForDatabase(ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt.getText()));
                        artikel.setMwst(1);
                        artikel.setAktiv(ArtikelAbrechnungEintragenAO.this.aktiviert.isSelected() ? 1 : 0);
                        artikel.setVon(TimeCalculation.parseDateForDatabase(ArtikelAbrechnungEintragenAO.this.gueltigvon.getText()));
                        if (ArtikelAbrechnungEintragenAO.this.gueltigbis.getText().equals("")) {
                            artikel.setBis("2099-12-31");
                        } else {
                            artikel.setBis(TimeCalculation.parseDateForDatabase(ArtikelAbrechnungEintragenAO.this.gueltigbis.getText()));
                        }
                        if (ArtikelAbrechnungEintragenAO.this.stuendlichBerechnen.isSelected()) {
                            artikel.setBerechnungsart(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.pauschalBerechnen.isSelected()) {
                            artikel.setBerechnungsart(2);
                        } else if (ArtikelAbrechnungEintragenAO.this.variablerPreis.isSelected()) {
                            artikel.setBerechnungsart(3);
                        }
                        if (ArtikelAbrechnungEintragenAO.this.berechnungJedeAngefangendeStunde.isSelected()) {
                            artikel.setBerechnungsart2(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.berechnungMiniutenGenau.isSelected()) {
                            artikel.setBerechnungsart2(2);
                        } else {
                            artikel.setBerechnungsart2(0);
                        }
                        if (ArtikelAbrechnungEintragenAO.this.zahlungsart.getSelectedItem().toString().equals("Einzahlung")) {
                            artikel.setZahlungsart(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.zahlungsart.getSelectedItem().toString().equals("Auszahlung")) {
                            artikel.setZahlungsart(2);
                        }
                        if (ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_pauschal.isSelected()) {
                            artikel.setRabattart(1);
                        } else if (ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt_st\u00fcndlich.isSelected()) {
                            artikel.setRabattart(2);
                        }
                        tabArtikel.update(artikel);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNaechstenArtikel.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    ArtikelAbrechnungEintragenAO.this.id.setText(Integer.toString(new TabelleAbrechnung_artikel().getNextNummer()));
                    ArtikelAbrechnungEintragenAO.this.name.setText(null);
                    ArtikelAbrechnungEintragenAO.this.aktiviert.setEnabled(false);
                    buchungskonto.setSelectedItem("<bitte w\u00e4hlen>");
                    ArtikelAbrechnungEintragenAO.this.zahlungsart.setSelectedItem("Einzahlung");
                    klasse.setSelectedItem("<bitte w\u00e4hlen>");
                    ArtikelAbrechnungEintragenAO.this.bgBerechnungsart.clearSelection();
                    ArtikelAbrechnungEintragenAO.this.bgBerechnungsart2.clearSelection();
                    ArtikelAbrechnungEintragenAO.this.wert.setText(null);
                    ArtikelAbrechnungEintragenAO.this.automatischerEinbehalt.setText("0,00");
                    ArtikelAbrechnungEintragenAO.this.bgAutomatischerEinbehalt.clearSelection();
                    ArtikelAbrechnungEintragenAO.this.gueltigvon.setText(SbcUtils.timeStamp((String)"dd.MM.yyyy"));
                    ArtikelAbrechnungEintragenAO.this.gueltigbis.setText("");
                    ArtikelAbrechnungEintragenAO.this.buttonNaechstenArtikel.setVisible(false);
                    ArtikelAbrechnungEintragenAO.this.buttonSpeichern.setEnabled(true);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonArtikelklasseAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ARTIKELKLASSE_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonKontoAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0301");
                Steuerung.setStatus(Status.KONTO_ANLEGEN);
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

