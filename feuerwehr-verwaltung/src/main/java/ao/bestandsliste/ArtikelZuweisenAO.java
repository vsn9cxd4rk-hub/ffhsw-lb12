/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.bestandsliste;

import ao.AbstractFenster;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.bestandsliste.TabelleLager;
import data.tabellen.bestandsliste.TabelleLager_artikel;
import data.tabellen.bestandsliste.TabelleLager_zugewiesen;
import data.tabellen.mitglied.TabelleMitglied;
import go.bestandsliste.Zuweisen;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class ArtikelZuweisenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static Zuweisen staticZuweisen;
    private JButton buttonZurueck;
    private JButton buttonZuweisenMitglied;
    private JButton buttonZuweisenFahrzeug;
    private JButton buttonZuweisenLager;
    private JButton buttonEntfernenMitglieder;
    private JButton buttonEntfernenFahrzeuge;
    private JButton buttonEntfernenLager;
    private JButton buttonNeuerArtikel;
    private JButton buttonSpeichernMitglied;
    private JButton buttonSpeichernFahrzeug;
    private JButton buttonSpeichernLager;
    public static JComboBox<String> artikel;
    private JLabel artikel_label;
    public static JComboBox<String> mitglied;
    public static JComboBox<String> fahrzeug;
    public static JComboBox<String> lager;
    private JLabel mitgliederListe;
    private JLabel fahrzeugListe;
    private JLabel lagerListe;
    public static JList listeMitglieder;
    public static JList listeFahrzeuge;
    public static JList listeLager;
    private JScrollPane paneMitglieder;
    private JScrollPane paneFahrzeuge;
    private JScrollPane paneLager;
    private JComboBox<String> fieldMitgleider;
    private JComboBox<String> fieldFahrzeug;
    private JComboBox<String> fieldLager;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;

    public ArtikelZuweisenAO() {
        super("FeuerwehrManagementSystem - Artikel zuweisen");
        logging.logInfo((Object)"Starte: ArtikelZuweisenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonZuweisenMitglied = new JButton("Neu Zuweisen");
        this.buttonZuweisenFahrzeug = new JButton("Neu Zuweisen");
        this.buttonZuweisenLager = new JButton("Neu Zuweisen");
        this.buttonEntfernenMitglieder = new JButton("Verschieben");
        this.buttonEntfernenFahrzeuge = new JButton("Verschieben");
        this.buttonEntfernenLager = new JButton("Verschieben");
        this.buttonNeuerArtikel = new JButton("Artikel anlegen");
        this.buttonSpeichernMitglied = new JButton("Speichern");
        this.buttonSpeichernFahrzeug = new JButton("Speichern");
        this.buttonSpeichernLager = new JButton("Speichern");
        this.fieldFahrzeug = new JComboBox();
        this.fieldMitgleider = new JComboBox();
        this.fieldLager = new JComboBox();
        this.mitgliederListe = new JLabel("Mitglieder:");
        this.fahrzeugListe = new JLabel("Fahrzeuge: ");
        this.lagerListe = new JLabel("Lager:");
        this.artikel_label = new JLabel("Artikelliste: ");
        this.modulBeschreibung = new JLabel("Artikel Zuweisen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleLager tabLager = new TabelleLager();
        try {
            String[] artikelListe = Utils.listToArrayOnlyFORComboBoxes(tabArtikel.getAllArtikel());
            String[] fahrzeugListe = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getAllMitgliederFromDataBase());
            String[] lagerListe = Utils.listToArrayOnlyFORComboBoxes(tabLager.getAllLager());
            artikel = new JComboBox<String>(artikelListe);
            fahrzeug = new JComboBox<String>(fahrzeugListe);
            mitglied = new JComboBox<String>(mitgliederListe);
            lager = new JComboBox<String>(lagerListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        mitglied.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                    int komma = mitglied.getSelectedItem().toString().indexOf(",");
                    String isSelectedName = mitglied.getSelectedItem().toString().substring(0, komma);
                    String isSelectedVorname = mitglied.getSelectedItem().toString().substring(komma + 2, mitglied.getSelectedItem().toString().length());
                    int mID = tabMitglied.getId(isSelectedName, isSelectedVorname);
                    listeMitglieder.setListData(Utils.listToArray(tabZuweisen.getZugewiesendeArtikel("M", mID)));
                    ArtikelZuweisenAO.this.fieldMitgleider.removeAllItems();
                    String[] fieldMitgliedListe = Utils.listToArray(tabZuweisen.getAllOrt("M", mID));
                    int i = 0;
                    while (i < fieldMitgliedListe.length) {
                        ArtikelZuweisenAO.this.fieldMitgleider.addItem(fieldMitgliedListe[i]);
                        ++i;
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        fahrzeug.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                    TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                    int fID = tabFahrzeug.getFahrzeugID(fahrzeug.getSelectedItem().toString());
                    listeFahrzeuge.setListData(Utils.listToArray(tabZuweisen.getZugewiesendeArtikel("F", fID)));
                    ArtikelZuweisenAO.this.fieldFahrzeug.removeAllItems();
                    String[] fieldFahrzeugListe = Utils.listToArray(tabZuweisen.getAllOrt("F", fID));
                    int i = 0;
                    while (i < fieldFahrzeugListe.length) {
                        ArtikelZuweisenAO.this.fieldFahrzeug.addItem(fieldFahrzeugListe[i]);
                        ++i;
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        lager.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleLager tabFahrzeug = new TabelleLager();
                    TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                    int lID = tabFahrzeug.getLagerID(lager.getSelectedItem().toString());
                    listeLager.setListData(Utils.listToArray(tabZuweisen.getZugewiesendeArtikel("L", lID)));
                    ArtikelZuweisenAO.this.fieldLager.removeAllItems();
                    String[] fieldLagerListe = Utils.listToArray(tabZuweisen.getAllOrt("L", lID));
                    int i = 0;
                    while (i < fieldLagerListe.length) {
                        ArtikelZuweisenAO.this.fieldLager.addItem(fieldLagerListe[i]);
                        ++i;
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void labelHinzufuegen() {
        listeMitglieder = new JList();
        listeFahrzeuge = new JList();
        listeLager = new JList();
        this.paneMitglieder = new JScrollPane(listeMitglieder);
        this.paneMitglieder.setVerticalScrollBarPolicy(22);
        this.paneMitglieder.setPreferredSize(new Dimension(190, 250));
        this.paneFahrzeuge = new JScrollPane(listeFahrzeuge);
        this.paneFahrzeuge.setVerticalScrollBarPolicy(22);
        this.paneFahrzeuge.setPreferredSize(new Dimension(190, 250));
        this.paneLager = new JScrollPane(listeLager);
        this.paneLager.setVerticalScrollBarPolicy(22);
        this.paneLager.setPreferredSize(new Dimension(190, 250));
        listeFahrzeuge.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                try {
                    TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                    TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                    TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                    int fID = tabFahrzeug.getFahrzeugID(fahrzeug.getSelectedItem().toString());
                    int klammer = listeFahrzeuge.getSelectedValue().toString().indexOf("(");
                    String selectedArtikelName = listeFahrzeuge.getSelectedValue().toString().substring(0, klammer - 1);
                    int selectedAnzahl = Integer.parseInt(listeFahrzeuge.getSelectedValue().toString().substring(klammer + 1, listeFahrzeuge.getSelectedValue().toString().length() - 2));
                    ArtikelZuweisenAO.this.fieldFahrzeug.setSelectedItem(tabZuweisen.getOrt(tabArtikel.getArtikelID(selectedArtikelName), selectedAnzahl, fID, "F"));
                }
                catch (NullPointerException | NumberFormatException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        listeMitglieder.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                try {
                    TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                    int mID = tabMitglied.getIdByGuiString(mitglied.getSelectedItem().toString());
                    int klammer = listeMitglieder.getSelectedValue().toString().indexOf("(");
                    String selectedArtikelName = listeMitglieder.getSelectedValue().toString().substring(0, klammer - 1);
                    int selectedAnzahl = Integer.parseInt(listeMitglieder.getSelectedValue().toString().substring(klammer + 1, listeMitglieder.getSelectedValue().toString().length() - 2));
                    ArtikelZuweisenAO.this.fieldMitgleider.setSelectedItem(tabZuweisen.getOrt(tabArtikel.getArtikelID(selectedArtikelName), selectedAnzahl, mID, "M"));
                }
                catch (NullPointerException | NumberFormatException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        listeLager.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                try {
                    TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                    TabelleLager tabLager = new TabelleLager();
                    TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                    int lID = tabLager.getLagerID(lager.getSelectedItem().toString());
                    int klammer = listeLager.getSelectedValue().toString().indexOf("(");
                    String selectedArtikelName = listeLager.getSelectedValue().toString().substring(0, klammer - 1);
                    int selectedAnzahl = Integer.parseInt(listeLager.getSelectedValue().toString().substring(klammer + 1, listeLager.getSelectedValue().toString().length() - 2));
                    ArtikelZuweisenAO.this.fieldLager.setSelectedItem(tabZuweisen.getOrt(tabArtikel.getArtikelID(selectedArtikelName), selectedAnzahl, lID, "L"));
                }
                catch (NullPointerException | NumberFormatException | SQLException e) {
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
        this.setTitle("FeuerwehrManagementSystem - Artikel Zuweisen");
        this.setSize(670, 660);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.artikel_label);
        this.add(artikel);
        this.add(this.buttonNeuerArtikel);
        artikel.setPreferredSize(new Dimension(300, 25));
        this.add(this.dummy3);
        this.add(this.mitgliederListe);
        this.mitgliederListe.setPreferredSize(new Dimension(190, 25));
        this.add(this.fahrzeugListe);
        this.fahrzeugListe.setPreferredSize(new Dimension(190, 25));
        this.add(this.lagerListe);
        this.lagerListe.setPreferredSize(new Dimension(190, 25));
        this.add(mitglied);
        mitglied.setPreferredSize(new Dimension(190, 25));
        this.add(fahrzeug);
        fahrzeug.setPreferredSize(new Dimension(190, 25));
        this.add(lager);
        lager.setPreferredSize(new Dimension(190, 25));
        this.add(this.paneMitglieder);
        this.add(this.paneFahrzeuge);
        this.add(this.paneLager);
        this.add(this.buttonZuweisenMitglied);
        this.buttonZuweisenMitglied.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonZuweisenFahrzeug);
        this.buttonZuweisenFahrzeug.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonZuweisenLager);
        this.buttonZuweisenLager.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonEntfernenMitglieder);
        this.buttonEntfernenMitglieder.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonEntfernenFahrzeuge);
        this.buttonEntfernenFahrzeuge.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonEntfernenLager);
        this.buttonEntfernenLager.setPreferredSize(new Dimension(190, 25));
        this.add(this.fieldMitgleider);
        this.fieldMitgleider.setPreferredSize(new Dimension(190, 25));
        this.add(this.fieldFahrzeug);
        this.fieldFahrzeug.setPreferredSize(new Dimension(190, 25));
        this.add(this.fieldLager);
        this.fieldLager.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonSpeichernMitglied);
        this.buttonSpeichernMitglied.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonSpeichernFahrzeug);
        this.buttonSpeichernFahrzeug.setPreferredSize(new Dimension(190, 25));
        this.add(this.buttonSpeichernLager);
        this.buttonSpeichernLager.setPreferredSize(new Dimension(190, 25));
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.fieldMitgleider.setEditable(true);
        this.fieldFahrzeug.setEditable(true);
        this.fieldLager.setEditable(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonZuweisenMitglied.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (artikel.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN);
                    } else {
                        TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                        TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        Zuweisen zuweisen = new Zuweisen();
                        int artID = tabArtikel.getArtikelID(artikel.getSelectedItem().toString());
                        int mID = tabMitglied.getIdByGuiString(mitglied.getSelectedItem().toString());
                        zuweisen.setId(tabZuweisen.getNextID());
                        zuweisen.setArtikelId(artID);
                        zuweisen.setAnzahl(tabZuweisen.getNextNummerOfArtikle(artID, mID, "M"));
                        zuweisen.setGruppe("M");
                        zuweisen.setMitgliedID(mID);
                        zuweisen.setOrt("");
                        tabZuweisen.insert(zuweisen);
                        listeMitglieder.setListData(Utils.listToArray(tabZuweisen.getZugewiesendeArtikel("M", mID)));
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonEntfernenMitglieder.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                    TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    staticZuweisen = new Zuweisen();
                    int klammer = listeMitglieder.getSelectedValue().toString().indexOf("(");
                    String selectedArtikelName = listeMitglieder.getSelectedValue().toString().substring(0, klammer - 1);
                    int artID = tabArtikel.getArtikelID(selectedArtikelName);
                    int wer = tabMitglied.getIdByGuiString(mitglied.getSelectedItem().toString());
                    int id = tabZugewiesen.getLastNumberOfArtikel(artID, wer, "M");
                    staticZuweisen.setId(tabZugewiesen.getId(artID, id, wer, "M"));
                    staticZuweisen.setArtikelId(artID);
                    staticZuweisen.setAnzahl(id);
                    staticZuweisen.setGruppe("M");
                    staticZuweisen.setMitgliedID(wer);
                    Steuerung.setStatus(Status.BESTAND_VERSCHIEBEN);
                    Steuerung.steuerung();
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN, "Warnung", 2);
                }
            }
        });
        this.buttonSpeichernMitglied.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                Zuweisen zuweisen = new Zuweisen();
                try {
                    int komma = listeMitglieder.getSelectedValue().toString().indexOf("(");
                    String isSelectedArticleName = listeMitglieder.getSelectedValue().toString().substring(0, komma - 1);
                    int anzahlAtikel = Integer.parseInt(listeMitglieder.getSelectedValue().toString().substring(komma + 1, listeMitglieder.getSelectedValue().toString().length() - 2));
                    String ort = ArtikelZuweisenAO.this.fieldMitgleider.getSelectedItem().toString();
                    int mID = tabMitglied.getIdByGuiString(mitglied.getSelectedItem().toString());
                    zuweisen.setAnzahl(anzahlAtikel);
                    zuweisen.setArtikelId(tabArtikel.getArtikelID(isSelectedArticleName));
                    zuweisen.setGruppe("M");
                    zuweisen.setMitgliedID(mID);
                    zuweisen.setOrt(ort);
                    tabZugewiesen.updateOrt(zuweisen);
                    ArtikelZuweisenAO.this.fieldMitgleider.removeAllItems();
                    String[] fieldMitgliedListe = Utils.listToArray(tabZugewiesen.getAllOrt("M", mID));
                    int i = 0;
                    while (i < fieldMitgliedListe.length) {
                        ArtikelZuweisenAO.this.fieldMitgleider.addItem(fieldMitgliedListe[i]);
                        ++i;
                    }
                    ArtikelZuweisenAO.this.fieldMitgleider.setSelectedItem(ort);
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonZuweisenFahrzeug.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (artikel.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN);
                    } else {
                        TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                        TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                        Zuweisen zuweisen = new Zuweisen();
                        int artID = tabArtikel.getArtikelID(artikel.getSelectedItem().toString());
                        int fID = tabFahrzeug.getFahrzeugID(fahrzeug.getSelectedItem().toString());
                        zuweisen.setId(tabZuweisen.getNextID());
                        zuweisen.setArtikelId(artID);
                        zuweisen.setAnzahl(tabZuweisen.getNextNummerOfArtikle(artID, fID, "F"));
                        zuweisen.setGruppe("F");
                        zuweisen.setMitgliedID(fID);
                        zuweisen.setOrt("");
                        tabZuweisen.insert(zuweisen);
                        listeFahrzeuge.setListData(Utils.listToArray(tabZuweisen.getZugewiesendeArtikel("F", fID)));
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonEntfernenFahrzeuge.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                    TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                    TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                    staticZuweisen = new Zuweisen();
                    int klammer = listeFahrzeuge.getSelectedValue().toString().indexOf("(");
                    String selectedArtikelName = listeFahrzeuge.getSelectedValue().toString().substring(0, klammer - 1);
                    int artID = tabArtikel.getArtikelID(selectedArtikelName);
                    int wer = tabFahrzeug.getFahrzeugID(fahrzeug.getSelectedItem().toString());
                    int id = tabZugewiesen.getLastNumberOfArtikel(artID, wer, "F");
                    staticZuweisen.setId(tabZugewiesen.getId(artID, id, wer, "F"));
                    staticZuweisen.setArtikelId(artID);
                    staticZuweisen.setAnzahl(id);
                    staticZuweisen.setGruppe("F");
                    staticZuweisen.setMitgliedID(wer);
                    Steuerung.setStatus(Status.BESTAND_VERSCHIEBEN);
                    Steuerung.steuerung();
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN, "Warnung", 2);
                }
            }
        });
        this.buttonSpeichernFahrzeug.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                Zuweisen zuweisen = new Zuweisen();
                try {
                    int komma = listeFahrzeuge.getSelectedValue().toString().indexOf("(");
                    String isSelectedArticleName = listeFahrzeuge.getSelectedValue().toString().substring(0, komma - 1);
                    int anzahlAtikel = Integer.parseInt(listeFahrzeuge.getSelectedValue().toString().substring(komma + 1, listeFahrzeuge.getSelectedValue().toString().length() - 2));
                    String ort = ArtikelZuweisenAO.this.fieldFahrzeug.getSelectedItem().toString();
                    int fID = tabFahrzeug.getFahrzeugID(fahrzeug.getSelectedItem().toString());
                    zuweisen.setAnzahl(anzahlAtikel);
                    zuweisen.setArtikelId(tabArtikel.getArtikelID(isSelectedArticleName));
                    zuweisen.setGruppe("F");
                    zuweisen.setMitgliedID(fID);
                    zuweisen.setOrt(ort);
                    tabZugewiesen.updateOrt(zuweisen);
                    ArtikelZuweisenAO.this.fieldFahrzeug.removeAllItems();
                    String[] fieldFahrzeugListe = Utils.listToArray(tabZugewiesen.getAllOrt("F", fID));
                    int i = 0;
                    while (i < fieldFahrzeugListe.length) {
                        ArtikelZuweisenAO.this.fieldFahrzeug.addItem(fieldFahrzeugListe[i]);
                        ++i;
                    }
                    ArtikelZuweisenAO.this.fieldFahrzeug.setSelectedItem(ort);
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonZuweisenLager.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (artikel.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN);
                    } else {
                        TabelleLager_zugewiesen tabZuweisen = new TabelleLager_zugewiesen();
                        TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                        TabelleLager tabLager = new TabelleLager();
                        Zuweisen zuweisen = new Zuweisen();
                        int artID = tabArtikel.getArtikelID(artikel.getSelectedItem().toString());
                        int lID = tabLager.getLagerID(lager.getSelectedItem().toString());
                        zuweisen.setId(tabZuweisen.getNextID());
                        zuweisen.setArtikelId(artID);
                        zuweisen.setAnzahl(tabZuweisen.getNextNummerOfArtikle(artID, lID, "L"));
                        zuweisen.setGruppe("L");
                        zuweisen.setMitgliedID(lID);
                        zuweisen.setOrt("");
                        tabZuweisen.insert(zuweisen);
                        listeLager.setListData(Utils.listToArray(tabZuweisen.getZugewiesendeArtikel("L", lID)));
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonEntfernenLager.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                    TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                    TabelleLager tabLager = new TabelleLager();
                    staticZuweisen = new Zuweisen();
                    int klammer = listeLager.getSelectedValue().toString().indexOf("(");
                    String selectedArtikelName = listeLager.getSelectedValue().toString().substring(0, klammer - 1);
                    int artID = tabArtikel.getArtikelID(selectedArtikelName);
                    int wer = tabLager.getLagerID(lager.getSelectedItem().toString());
                    int id = tabZugewiesen.getLastNumberOfArtikel(artID, wer, "L");
                    staticZuweisen.setId(tabZugewiesen.getId(artID, id, wer, "L"));
                    staticZuweisen.setArtikelId(artID);
                    staticZuweisen.setAnzahl(id);
                    staticZuweisen.setGruppe("L");
                    staticZuweisen.setMitgliedID(wer);
                    MyEvent.setEvent((String)"0x0200");
                    Steuerung.setStatus(Status.BESTAND_VERSCHIEBEN);
                    Steuerung.steuerung();
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN, "Warnung", 2);
                }
            }
        });
        this.buttonSpeichernLager.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                TabelleLager tabLager = new TabelleLager();
                Zuweisen zuweisen = new Zuweisen();
                try {
                    int komma = listeLager.getSelectedValue().toString().indexOf("(");
                    String isSelectedArticleName = listeLager.getSelectedValue().toString().substring(0, komma - 1);
                    int anzahlAtikel = Integer.parseInt(listeLager.getSelectedValue().toString().substring(komma + 1, listeLager.getSelectedValue().toString().length() - 2));
                    String ort = ArtikelZuweisenAO.this.fieldLager.getSelectedItem().toString();
                    int lID = tabLager.getLagerID(lager.getSelectedItem().toString());
                    zuweisen.setAnzahl(anzahlAtikel);
                    zuweisen.setArtikelId(tabArtikel.getArtikelID(isSelectedArticleName));
                    zuweisen.setGruppe("L");
                    zuweisen.setMitgliedID(lID);
                    zuweisen.setOrt(ort);
                    tabZugewiesen.updateOrt(zuweisen);
                    ArtikelZuweisenAO.this.fieldLager.removeAllItems();
                    String[] fieldLagerListe = Utils.listToArray(tabZugewiesen.getAllOrt("L", lID));
                    int i = 0;
                    while (i < fieldLagerListe.length) {
                        ArtikelZuweisenAO.this.fieldLager.addItem(fieldLagerListe[i]);
                        ++i;
                    }
                    ArtikelZuweisenAO.this.fieldLager.setSelectedItem(ort);
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNeuerArtikel.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0026");
                Steuerung.setStatus(Status.ARTIKEL_EINTRAGEN);
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

