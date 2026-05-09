/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import data.tabellen.statistik.TabelleStatistikLehrgang;
import go.Lehrgang_Kategorie;
import go.Mitgliederlaufbahn;
import go.StatistikLehrgang;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.logbuchEingabe;

public class MitgliederLaufbahnPflegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private static int[] IDLISTE = null;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAktualisieren;
    private JButton buttonNeu;
    private JButton buttonL\u00f6schen;
    private JTextField datumBis;
    private JTextField datumVon;
    private JLabel datumBis_label;
    private JLabel datumVon_label;
    private JComboBox<String> lehrgangBox;
    private JLabel lehrgang_label;
    private JComboBox<String> funktionBox;
    private JLabel funktion_label;
    private JComboBox<String> ehrungenAbzeichenBox;
    private JLabel ehrungenAbzeichen_label;
    private JComboBox<String> mitglieder;
    private JLabel mitglieder_label;
    private JComboBox<String> dienstgrad;
    private JLabel dienstgrad_label;
    private JTextField unterrichtseinheiten;
    private JLabel unterrichtseinheiten_label;
    private JLabel hinweisUnterrichtseinheit;
    private JLabel unterrichtseinheitenAlsStandard_label;
    private JLabel zweiteTeilnahmeAmLehrgang_label;
    private JCheckBox unterrichtseinheitenAlsStandard;
    private JCheckBox zweiteTeilnahmeAmLehrgang;
    private DefaultTableModel defaultTableModelLaufbahnListe;
    public JTable table;
    private JScrollPane scrollpane;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;
    public static String letzteKategorie;
    private static int selectedRow;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum von");
            this.add("Datum bis (Pr\u00fcfungsdatum)");
            this.add("Dienstgrad");
            this.add("Ehrungen / Abzeichen");
            this.add("Funktion");
            this.add("Lehrgang");
            this.add("Unterrichtseinheiten");
        }
    };

    static {
        selectedRow = 0;
    }

    public MitgliederLaufbahnPflegenAO() {
        super("FeuerwehrManagementSystem - Mitgliederlaufbahn Pflegen");
        logging.logInfo((Object)"Starte: MitgliederLaufbahnPflegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonNeu = new JButton("Neu");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonL\u00f6schen = new JButton("L\u00f6schen");
        this.modulBeschreibung = new JLabel("Mitgliederlaufbahn Pflegen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.lehrgang_label = new JLabel("Lehrgang / F\u00fchrerschein: ");
        this.funktion_label = new JLabel("Funktion innerhalb / au\u00dferhab der Feuerwehr: ");
        this.datumBis_label = new JLabel("Datum bis (Pr\u00fcfungsdatum): ");
        this.datumVon_label = new JLabel("Datum von: ");
        this.mitglieder_label = new JLabel("Mitglied: ");
        this.dienstgrad_label = new JLabel("Dienstgrad: ");
        this.ehrungenAbzeichen_label = new JLabel("Ehrungen / Abzeichen: ");
        this.unterrichtseinheiten_label = new JLabel("Unterrichtseinheiten*: ");
        this.hinweisUnterrichtseinheit = new JLabel("*HINWEIS: Die Unterrichtseinheiten werden immer in 45 Minuten angegeben (1xUE = 45min.)");
        this.unterrichtseinheitenAlsStandard_label = new JLabel("Unterrichtseinheiten in den Standard \u00fcbernehmen:          ");
        this.zweiteTeilnahmeAmLehrgang_label = new JLabel("Mitglied hat ein zweites mal Teilgenommen?: ");
        this.unterrichtseinheitenAlsStandard = new JCheckBox();
        this.zweiteTeilnahmeAmLehrgang = new JCheckBox();
        this.unterrichtseinheiten = new JTextField(20);
        this.datumBis = new JTextField(20);
        this.datumVon = new JTextField(20);
    }

    protected void labelErstellen() {
        TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
        try {
            String[] lehrgangListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAlleLehrg\u00e4ngeByName());
            String[] ehrungenAbzeichenListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAlleEhrungenAbzeichen());
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            String[] dienstgradListe = Utils.listToArrayOnlyFORComboBoxes(tabDienstgrad.getAllDienstgradLang());
            String[] funktionListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAlleFunktionen());
            String[] funktionAu\u00dferhalbListe = Utils.listToArray(tabKategorie.getAlleFunktionenAu\u00dferhalb());
            this.lehrgangBox = new JComboBox<String>(lehrgangListe);
            this.funktionBox = new JComboBox<String>(funktionListe);
            this.ehrungenAbzeichenBox = new JComboBox<String>(ehrungenAbzeichenListe);
            this.mitglieder = new JComboBox<String>(mitgliederListe);
            this.dienstgrad = new JComboBox<String>(dienstgradListe);
            int f = 0;
            while (f < funktionAu\u00dferhalbListe.length) {
                this.funktionBox.addItem(funktionAu\u00dferhalbListe[f]);
                ++f;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.mitglieder.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                    int mID = tabMitglied.getIdByGuiString(MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString());
                    IDLISTE = Utils.listToIntArray(tabLaufbahn.getIDListe(mID));
                    ((DefaultTableModel)MitgliederLaufbahnPflegenAO.this.table.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, null), MitgliederLaufbahnPflegenAO.this.headname);
                    MitgliederLaufbahnPflegenAO.this.buttonSpeichern.setVisible(true);
                    MitgliederLaufbahnPflegenAO.this.buttonAktualisieren.setVisible(false);
                    MitgliederLaufbahnPflegenAO.this.dienstgrad.setSelectedItem("<bitte w\u00e4hlen>");
                    MitgliederLaufbahnPflegenAO.this.lehrgangBox.setSelectedItem("<bitte w\u00e4hlen>");
                    MitgliederLaufbahnPflegenAO.this.datumBis.setText(null);
                    MitgliederLaufbahnPflegenAO.this.datumVon.setText(null);
                    MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText(null);
                    MitgliederLaufbahnPflegenAO.this.buttonNeu.setVisible(false);
                    MitgliederLaufbahnPflegenAO.this.buttonL\u00f6schen.setVisible(false);
                    MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setVisible(true);
                    MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang_label.setVisible(true);
                    MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setSelected(false);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.lehrgangBox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                try {
                    int lID = tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString());
                    int ue = tabKategorie.getUnterrichtseinheiten(lID);
                    MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText(Integer.toString(ue));
                    MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setSelected(false);
                    if (ue == 0) {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheitenAlsStandard.setSelected(false);
                    } else {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheitenAlsStandard.setSelected(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.ehrungenAbzeichenBox.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                try {
                    int lID = tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString());
                    int ue = tabKategorie.getUnterrichtseinheiten(lID);
                    MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText(Integer.toString(ue));
                    MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setSelected(false);
                    if (ue == 0) {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheitenAlsStandard.setSelected(false);
                    } else {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheitenAlsStandard.setSelected(true);
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
        this.defaultTableModelLaufbahnListe = new DefaultTableModel(10, 9);
        this.defaultTableModelLaufbahnListe.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelLaufbahnListe);
        this.table.setPreferredScrollableViewportSize(new Dimension(1000, 300));
        this.table.setFillsViewportHeight(true);
        this.table.setRowHeight(30);
        this.scrollpane = new JScrollPane(this.table);
        this.scrollpane.setVerticalScrollBarPolicy(22);
        this.table.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int[] rows = MitgliederLaufbahnPflegenAO.this.table.getSelectedRows();
                    if (rows.length >= 2) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    } else {
                        selectedRow = rows[0];
                        logging.logInfo((Object)("Selektierte Spalte in der Tabelle: " + selectedRow));
                        if (!MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 0).toString().equals("-")) {
                            MitgliederLaufbahnPflegenAO.this.datumVon.setText(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 0).toString());
                        }
                        MitgliederLaufbahnPflegenAO.this.datumBis.setText(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 1).toString());
                        if (MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 2).toString().equals("")) {
                            MitgliederLaufbahnPflegenAO.this.dienstgrad.setSelectedItem("<bitte w\u00e4hlen>");
                        } else {
                            MitgliederLaufbahnPflegenAO.this.dienstgrad.setSelectedItem(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 2).toString().substring(17, MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 2).toString().length()));
                        }
                        if (MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 3).toString().equals("")) {
                            MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.setSelectedItem("<bitte w\u00e4hlen>");
                        } else if (MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 3).toString().startsWith("Ehrung")) {
                            MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.setSelectedItem(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 3).toString().substring(9, MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 3).toString().length()));
                        } else {
                            MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.setSelectedItem(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 3).toString().substring(12, MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 3).toString().length()));
                        }
                        if (MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 4).toString().equals("")) {
                            MitgliederLaufbahnPflegenAO.this.funktionBox.setSelectedItem("<bitte w\u00e4hlen>");
                        } else {
                            MitgliederLaufbahnPflegenAO.this.funktionBox.setSelectedItem(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 4).toString().substring(11, MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 4).toString().length()));
                        }
                        if (MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 5).toString().equals("")) {
                            MitgliederLaufbahnPflegenAO.this.lehrgangBox.setSelectedItem("<bitte w\u00e4hlen>");
                        } else {
                            MitgliederLaufbahnPflegenAO.this.lehrgangBox.setSelectedItem(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 5));
                        }
                        if (MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 6).toString().equals("n. V.")) {
                            MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText("0");
                        } else {
                            MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText(MitgliederLaufbahnPflegenAO.this.table.getValueAt(selectedRow, 6).toString());
                        }
                        MitgliederLaufbahnPflegenAO.this.buttonAktualisieren.setVisible(true);
                        MitgliederLaufbahnPflegenAO.this.buttonNeu.setVisible(true);
                        MitgliederLaufbahnPflegenAO.this.buttonSpeichern.setVisible(false);
                        MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setSelected(false);
                        if (BerechtigunsManager.ber[92] == 1) {
                            MitgliederLaufbahnPflegenAO.this.buttonL\u00f6schen.setVisible(true);
                        }
                    }
                }
            }
        });
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(1100, 768);
        this.setTitle("FeuerwehrManagementSystem - Mitgliederlaufbahn Pflegen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.scrollpane);
        this.panel = new JPanel(new GridLayout(10, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.mitglieder_label);
        this.panel.add(this.mitglieder);
        this.panel.add(this.lehrgang_label);
        this.panel.add(this.lehrgangBox);
        this.panel.add(this.funktion_label);
        this.panel.add(this.funktionBox);
        this.panel.add(this.dienstgrad_label);
        this.panel.add(this.dienstgrad);
        this.panel.add(this.ehrungenAbzeichen_label);
        this.panel.add(this.ehrungenAbzeichenBox);
        this.panel.add(this.datumVon_label);
        this.panel.add(this.datumVon);
        this.panel.add(this.datumBis_label);
        this.panel.add(this.datumBis);
        this.panel.add(this.unterrichtseinheiten_label);
        this.panel.add(this.unterrichtseinheiten);
        this.panel.add(this.unterrichtseinheitenAlsStandard_label);
        this.panel.add(this.unterrichtseinheitenAlsStandard);
        this.panel.add(this.zweiteTeilnahmeAmLehrgang_label);
        this.panel.add(this.zweiteTeilnahmeAmLehrgang);
        this.add(this.hinweisUnterrichtseinheit);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonNeu);
        this.add(this.buttonL\u00f6schen);
        this.add(this.buttonSpeichern);
        this.add(this.buttonAktualisieren);
        this.buttonNeu.setVisible(false);
        this.buttonAktualisieren.setVisible(false);
        this.buttonL\u00f6schen.setVisible(false);
        if (MyEvent.event.equals("0x9999")) {
            this.mitglieder.setSelectedItem(MitgliederAnlegenAO.mitgliedName);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonL\u00f6schen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleDienstgrad tabDiestgrad = new TabelleDienstgrad();
                Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                try {
                    int mID = tabMitglied.getIdByGuiString(MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString());
                    int lID = tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString());
                    if (lID == 0 && MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        logging.logInfo((Object)"Der aktuelle Eintag ist eine Ehrung / Abzeichen");
                        lID = tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString());
                    }
                    if (!MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        logging.logInfo((Object)"L\u00f6sche Dienstgrad aus der Mitgliederlaufbahn");
                        laufbahn.setArt("D");
                        laufbahn.setNeuerDienstgrad(tabDiestgrad.getDienstgradID(MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString()));
                        laufbahn.setLehrgang(lID);
                        laufbahn.setDatum(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumBis.getText()));
                        laufbahn.setDatumVon(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumVon.getText()));
                        laufbahn.setMitgliederID(mID);
                    } else {
                        logging.logInfo((Object)"L\u00f6sche Lehrgang / Ehrung / Abzeichen aus der Mitgliederlaufbahn");
                        laufbahn.setArt(tabLehrgangKategorie.getArt(lID));
                        laufbahn.setNeuerDienstgrad(0);
                        laufbahn.setLehrgang(lID);
                        laufbahn.setDatum(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumBis.getText()));
                        laufbahn.setDatumVon(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumVon.getText()));
                        laufbahn.setMitgliederID(mID);
                    }
                    tabLaufbahn.delete(laufbahn);
                    ((DefaultTableModel)MitgliederLaufbahnPflegenAO.this.table.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, null), MitgliederLaufbahnPflegenAO.this.headname);
                    logbuchEingabe.NeuerEintag("Laufbahneintrag von Mitglied " + MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString() + " wurde gel\u00f6scht");
                    logging.logInfo((Object)("Laufbahneintrag von Mitglied " + MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString() + " wurde gel\u00f6scht"));
                    MitgliederLaufbahnPflegenAO.this.buttonAktualisieren.setVisible(false);
                    MitgliederLaufbahnPflegenAO.this.buttonL\u00f6schen.setVisible(false);
                    MitgliederLaufbahnPflegenAO.this.buttonNeu.setVisible(false);
                    MitgliederLaufbahnPflegenAO.this.buttonSpeichern.setVisible(true);
                    MitgliederLaufbahnPflegenAO.this.datumVon.setText(null);
                    MitgliederLaufbahnPflegenAO.this.datumBis.setText(null);
                    MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText(null);
                    MitgliederLaufbahnPflegenAO.this.lehrgangBox.setSelectedItem("<bitte w\u00e4hlen>");
                    MitgliederLaufbahnPflegenAO.this.dienstgrad.setSelectedItem("<bitte w\u00e4hlen>");
                    MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.setSelectedItem("<bitte w\u00e4hlen>");
                    JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                TabelleDienstgrad tabDiestgrad = new TabelleDienstgrad();
                TabelleStatistikLehrgang tabStatistik = new TabelleStatistikLehrgang();
                StatistikLehrgang statistik = new StatistikLehrgang();
                try {
                    if (MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText().equals("") | MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText().equals("n. V.")) {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText("0");
                    }
                    MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setSelected(false);
                    if (MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                    } else if (MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_LEHRGANG_AUSWAEHLEN, "Warnung", 2);
                    } else if (!MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText().toString().matches("[+-]?[0-9]+")) {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_UNTERRICHTSEINHEITEN, "Warnung", 2);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederLaufbahnPflegenAO.this.datumBis.getText()) && MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        MitgliederLaufbahnPflegenAO.this.datumBis.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederLaufbahnPflegenAO.this.datumVon.getText()) && !MitgliederLaufbahnPflegenAO.this.datumVon.getText().equals("") && MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        MitgliederLaufbahnPflegenAO.this.datumVon.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                    } else if (!(MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") || MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") || MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") || MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>"))) {
                        JOptionPane.showMessageDialog(null, Konstante.LAUFBAHN_DIENSTGRAD_LEHRGANG, "Warnung", 2);
                    } else {
                        MitgliederLaufbahnPflegenAO.this.datumBis.setBackground(Color.white);
                        MitgliederLaufbahnPflegenAO.this.datumVon.setBackground(Color.white);
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setBackground(Color.white);
                        int mID = tabMitglied.getIdByGuiString(MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString());
                        int dbID = IDLISTE[selectedRow];
                        int lID = 0;
                        lID = !MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") ? tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString()) : (MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") ? tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString()) : tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString()));
                        laufbahn.setId(dbID);
                        laufbahn.setMitgliederID(mID);
                        if (MitgliederLaufbahnPflegenAO.this.datumVon.getText().equals("")) {
                            laufbahn.setDatumVon("");
                        } else {
                            laufbahn.setDatumVon(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumVon.getText()));
                        }
                        if (MitgliederLaufbahnPflegenAO.this.datumBis.getText().equals("")) {
                            laufbahn.setDatum("");
                        } else {
                            laufbahn.setDatum(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumBis.getText()));
                        }
                        if (!MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText("0");
                            laufbahn.setArt("D");
                            laufbahn.setNeuerDienstgrad(tabDiestgrad.getDienstgradID(MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString()));
                            laufbahn.setUe(0);
                        } else if (!MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") | !MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") | !MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            laufbahn.setArt(tabKategorie.getArt(lID));
                            laufbahn.setLehrgang(lID);
                            laufbahn.setUe(Integer.parseInt(MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText()));
                        }
                        tabLaufbahn.update(laufbahn);
                        if (!MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            statistik.setId(dbID);
                            statistik.setMitgliederID(mID);
                            statistik.setJahr(Integer.parseInt(MitgliederLaufbahnPflegenAO.this.datumBis.getText().substring(6, 10)));
                            statistik.setLehrgangID(lID);
                            statistik.setDauer(Integer.parseInt(MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText()) * 45);
                            tabStatistik.update(statistik);
                        }
                        ((DefaultTableModel)MitgliederLaufbahnPflegenAO.this.table.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, null), MitgliederLaufbahnPflegenAO.this.headname);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNeu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MitgliederLaufbahnPflegenAO.this.buttonSpeichern.setVisible(true);
                MitgliederLaufbahnPflegenAO.this.buttonAktualisieren.setVisible(false);
                MitgliederLaufbahnPflegenAO.this.dienstgrad.setSelectedItem("<bitte w\u00e4hlen>");
                MitgliederLaufbahnPflegenAO.this.lehrgangBox.setSelectedItem("<bitte w\u00e4hlen>");
                MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.setSelectedItem("<bitte w\u00e4hlen>");
                MitgliederLaufbahnPflegenAO.this.datumBis.setText(null);
                MitgliederLaufbahnPflegenAO.this.datumVon.setText(null);
                MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText(null);
                MitgliederLaufbahnPflegenAO.this.buttonNeu.setVisible(false);
                MitgliederLaufbahnPflegenAO.this.buttonL\u00f6schen.setVisible(false);
                MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setVisible(true);
                MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang_label.setVisible(true);
                MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.setSelected(false);
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                TabelleDienstgrad tabDiestgrad = new TabelleDienstgrad();
                TabelleStatistikLehrgang tabStatistik = new TabelleStatistikLehrgang();
                StatistikLehrgang statistik = new StatistikLehrgang();
                try {
                    if (MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText().equals("")) {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText("0");
                    }
                    int mID = tabMitglied.getIdByGuiString(MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString());
                    int dbID = tabLaufbahn.getNextNumber();
                    int lID = 0;
                    lID = !MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") ? tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString()) : (MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") ? tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString()) : tabKategorie.getLehrgangID(MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString()));
                    if (MitgliederLaufbahnPflegenAO.this.mitglieder.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                    } else if (MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_LEHRGANG_AUSWAEHLEN, "Warnung", 2);
                    } else if (!MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText().toString().matches("[+-]?[0-9]+")) {
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_UNTERRICHTSEINHEITEN, "Warnung", 2);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederLaufbahnPflegenAO.this.datumBis.getText()) && MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        MitgliederLaufbahnPflegenAO.this.datumBis.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederLaufbahnPflegenAO.this.datumVon.getText()) && !MitgliederLaufbahnPflegenAO.this.datumVon.getText().equals("") && MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        MitgliederLaufbahnPflegenAO.this.datumVon.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                    } else if (!(MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") || MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") || MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>"))) {
                        JOptionPane.showMessageDialog(null, Konstante.LAUFBAHN_DIENSTGRAD_LEHRGANG, "Warnung", 2);
                    } else if (tabLaufbahn.getLehrgangCount(mID, lID) != 0 && !MitgliederLaufbahnPflegenAO.this.zweiteTeilnahmeAmLehrgang.isSelected() && !MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.LEHRGANG_EXISTIERT_BEREITS, "Warnung", 2);
                    } else {
                        MitgliederLaufbahnPflegenAO.this.datumBis.setBackground(Color.white);
                        MitgliederLaufbahnPflegenAO.this.datumVon.setBackground(Color.white);
                        MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setBackground(Color.white);
                        laufbahn.setId(dbID);
                        laufbahn.setMitgliederID(mID);
                        if (MitgliederLaufbahnPflegenAO.this.datumVon.getText().equals("")) {
                            laufbahn.setDatumVon("");
                        } else {
                            laufbahn.setDatumVon(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumVon.getText()));
                        }
                        if (MitgliederLaufbahnPflegenAO.this.datumBis.getText().equals("")) {
                            laufbahn.setDatum("");
                        } else {
                            laufbahn.setDatum(TimeCalculation.parseDateForDatabase(MitgliederLaufbahnPflegenAO.this.datumBis.getText()));
                        }
                        if (!MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.setText("0");
                            laufbahn.setArt("D");
                            laufbahn.setNeuerDienstgrad(tabDiestgrad.getDienstgradID(MitgliederLaufbahnPflegenAO.this.dienstgrad.getSelectedItem().toString()));
                            laufbahn.setUe(0);
                        } else if (!MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") | !MitgliederLaufbahnPflegenAO.this.ehrungenAbzeichenBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") | !MitgliederLaufbahnPflegenAO.this.funktionBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            laufbahn.setArt(tabKategorie.getArt(lID));
                            laufbahn.setLehrgang(lID);
                            laufbahn.setUe(Integer.parseInt(MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText()));
                        }
                        tabLaufbahn.insert(laufbahn);
                        logging.logInfo((Object)"Laufbahn wurde gespeichert");
                        if (!MitgliederLaufbahnPflegenAO.this.lehrgangBox.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            statistik.setId(dbID);
                            statistik.setMitgliederID(mID);
                            statistik.setJahr(Integer.parseInt(MitgliederLaufbahnPflegenAO.this.datumBis.getText().substring(6, 10)));
                            statistik.setLehrgangID(lID);
                            statistik.setDauer(Integer.parseInt(MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText()) * 45);
                            tabStatistik.insert(statistik);
                            logging.logInfo((Object)"Statistik wurde aktualisiert");
                        }
                        if (MitgliederLaufbahnPflegenAO.this.unterrichtseinheitenAlsStandard.isSelected()) {
                            Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();
                            kategorie.setId(lID);
                            kategorie.setUe(Integer.parseInt(MitgliederLaufbahnPflegenAO.this.unterrichtseinheiten.getText()));
                            tabKategorie.updateUnterrichtseinheiten(kategorie);
                            logging.logInfo((Object)"Unterrichtseinheiten wurden festgeschrieben");
                        }
                        ((DefaultTableModel)MitgliederLaufbahnPflegenAO.this.table.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, null), MitgliederLaufbahnPflegenAO.this.headname);
                        IDLISTE = Utils.listToIntArray(tabLaufbahn.getIDListe(mID));
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    public void fensterAnzeigen() {
        block7: {
            if (MyEvent.event.equals("0x9999")) {
                try {
                    try {
                        ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(Integer.parseInt(MitgliederAnlegenAO.mitgliedID), null), this.headname);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                        MyEvent.setEvent((String)"0x0030");
                        break block7;
                    }
                }
                catch (Throwable throwable) {
                    MyEvent.setEvent((String)"0x0030");
                    throw throwable;
                }
                MyEvent.setEvent((String)"0x0030");
            }
        }
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

