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
import ao.bestandsliste.ArtikelZuweisenAO;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.bestandsliste.TabelleLager;
import data.tabellen.bestandsliste.TabelleLager_artikel;
import data.tabellen.bestandsliste.TabelleLager_zugewiesen;
import data.tabellen.mitglied.TabelleMitglied;
import go.bestandsliste.Zuweisen;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class BestandVerschiebenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JComboBox<String> lager;
    private JComboBox<String> fahrzeug;
    private JComboBox<String> mitglied;
    private JTextField artikel;
    private JLabel lager_label;
    private JLabel artikel_label;
    private JLabel fahrzeug_label;
    private JLabel mitglied_label;
    private JLabel information;
    private JLabel information2;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public BestandVerschiebenAO() {
        super("FeuerwehrManagementSystem - Bestand verschieben");
        logging.logInfo((Object)"Starte: BestandVerschiebenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Verschieben");
        this.information = new JLabel("Der gew\u00e4hlte Artikel kann nur in eines der Lager verschoben werden. Artikel, die Ausgesondert");
        this.information2 = new JLabel("werden, m\u00fcssen erst in das \"Virtuelles- / Defektteile- / Ausmusterlager Lager\" verschoben werden.");
        this.lager_label = new JLabel("Lager: ");
        this.fahrzeug_label = new JLabel("Fahrzeug: ");
        this.mitglied_label = new JLabel("Mitglieder: ");
        this.artikel_label = new JLabel("Artikel");
        this.artikel = new JTextField(20);
        this.modulBeschreibung = new JLabel("Bestand verschieben");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        try {
            TabelleLager tabLager = new TabelleLager();
            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
            String[] lagerListe = Utils.listToArrayOnlyFORComboBoxes(tabLager.getAllLager());
            String[] fahrzeugListe = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeFromDataBase());
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getAllMitgliederFromDataBase());
            this.lager = new JComboBox<String>(lagerListe);
            this.fahrzeug = new JComboBox<String>(fahrzeugListe);
            this.mitglied = new JComboBox<String>(mitgliederListe);
            this.artikel.setText(tabArtikel.getArtikelName(ArtikelZuweisenAO.staticZuweisen.getArtikelId()));
            this.artikel.setEditable(false);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Artikel");
        this.setSize(670, 300);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.information);
        this.add(this.information2);
        this.panel = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.artikel_label);
        this.panel.add(this.artikel);
        this.panel.add(this.lager_label);
        this.panel.add(this.lager);
        if (MyEvent.event.equals("0x0200")) {
            this.panel.add(this.fahrzeug_label);
            this.panel.add(this.fahrzeug);
            this.panel.add(this.mitglied_label);
            this.panel.add(this.mitglied);
            this.information.setText("Wohin soll der gew\u00e4hlte Artikel verschoben werden?");
            this.information2.setText("");
        }
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                block12: {
                    try {
                        if (BestandVerschiebenAO.this.fahrzeug.isVisible() && !BestandVerschiebenAO.this.fahrzeug.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && BestandVerschiebenAO.this.mitglied.isVisible() && !BestandVerschiebenAO.this.mitglied.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && BestandVerschiebenAO.this.lager.isVisible() && !BestandVerschiebenAO.this.lager.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN, "Fehlermeldung", 0);
                            logging.logInfo((Object)"Es wurde mehr als einer ausgew\u00e4hlt wo der Artikel verschoben werden soll");
                            break block12;
                        }
                        if (BestandVerschiebenAO.this.fahrzeug.isVisible() && !BestandVerschiebenAO.this.fahrzeug.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && BestandVerschiebenAO.this.mitglied.isVisible() && !BestandVerschiebenAO.this.mitglied.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN, "Fehlermeldung", 0);
                            logging.logInfo((Object)"Es wurde mehr als einer ausgew\u00e4hlt wo der Artikel verschoben werden soll");
                            break block12;
                        }
                        if (BestandVerschiebenAO.this.fahrzeug.isVisible() && !BestandVerschiebenAO.this.fahrzeug.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && BestandVerschiebenAO.this.lager.isVisible() && !BestandVerschiebenAO.this.lager.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN, "Fehlermeldung", 0);
                            logging.logInfo((Object)"Es wurde mehr als einer ausgew\u00e4hlt wo der Artikel verschoben werden soll");
                            break block12;
                        }
                        if (BestandVerschiebenAO.this.mitglied.isVisible() && !BestandVerschiebenAO.this.mitglied.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && BestandVerschiebenAO.this.lager.isVisible() && !BestandVerschiebenAO.this.lager.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && BestandVerschiebenAO.this.lager.isVisible()) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN, "Fehlermeldung", 0);
                            logging.logInfo((Object)"Es wurde mehr als einer ausgew\u00e4hlt wo der Artikel verschoben werden soll");
                            break block12;
                        }
                        TabelleLager tabLager = new TabelleLager();
                        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                        TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        Zuweisen zugewiesen = new Zuweisen();
                        int lagerID = tabLager.getLagerID(BestandVerschiebenAO.this.lager.getSelectedItem().toString());
                        zugewiesen.setId(ArtikelZuweisenAO.staticZuweisen.getId());
                        zugewiesen.setArtikelId(ArtikelZuweisenAO.staticZuweisen.getArtikelId());
                        zugewiesen.setAnzahl(ArtikelZuweisenAO.staticZuweisen.getAnzahl());
                        if (BestandVerschiebenAO.this.fahrzeug.isVisible() && !BestandVerschiebenAO.this.fahrzeug.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            zugewiesen.setGruppe("F");
                            zugewiesen.setMitgliedID(tabFahrzeug.getFahrzeugID(BestandVerschiebenAO.this.fahrzeug.getSelectedItem().toString()));
                        } else if (BestandVerschiebenAO.this.mitglied.isVisible() && !BestandVerschiebenAO.this.mitglied.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            zugewiesen.setGruppe("M");
                            zugewiesen.setMitgliedID(tabMitglied.getIdByGuiString(BestandVerschiebenAO.this.mitglied.getSelectedItem().toString()));
                        } else {
                            zugewiesen.setGruppe("L");
                            zugewiesen.setMitgliedID(lagerID);
                        }
                        zugewiesen.setOrt("");
                        tabZugewiesen.update(zugewiesen);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        ArtikelZuweisenAO.listeLager.setListData(Utils.listToArray(tabZugewiesen.getZugewiesendeArtikel("L", lagerID)));
                        try {
                            ArtikelZuweisenAO.listeMitglieder.setListData(Utils.listToArray(tabZugewiesen.getZugewiesendeArtikel("M", tabMitglied.getIdByGuiString(ArtikelZuweisenAO.mitglied.getSelectedItem().toString()))));
                        }
                        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
                            // empty catch block
                        }
                        ArtikelZuweisenAO.listeFahrzeuge.setListData(Utils.listToArray(tabZugewiesen.getZugewiesendeArtikel("F", tabFahrzeug.getFahrzeugID(ArtikelZuweisenAO.fahrzeug.getSelectedItem().toString()))));
                        ArtikelZuweisenAO.lager.setSelectedItem(BestandVerschiebenAO.this.lager.getSelectedItem().toString());
                        MyEvent.setEvent((String)"0");
                        BestandVerschiebenAO.this.dispose();
                    }
                    catch (Exception e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
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

