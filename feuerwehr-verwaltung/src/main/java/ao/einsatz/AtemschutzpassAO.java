/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.einsatz;

import ao.AbstractFenster;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleAtemschutzpass;
import data.tabellen.TabelleAtemschutzpass_einsatzart;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Atemschutzpass;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.XML;
import utilities.logbuchEingabe;

public class AtemschutzpassAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonAktualisieren;
    private JComboBox[] einsatzart;
    private JLabel[] name;
    private JTextField[] zeit;
    private JComboBox<String> veranstaltung;
    private JComboBox[] truppZuordnung;
    private JLabel veranstaltung_label;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;
    private JScrollPane pane;

    public AtemschutzpassAO() {
        super("FeuerwehrManagementSystem - Atemschutzpass");
        logging.logInfo((Object)"Starte: AtemschutzpassAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.modulBeschreibung = new JLabel("Atemschutzpass");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.beschreibung = new JLabel("Name, Vorname                           Ger\u00e4teart                    Truppzuordnung                Zeit in Minuten        ");
        String[] liste = null;
        TabelleVeranstaltung veranstaltungListe = new TabelleVeranstaltung();
        try {
            liste = Utils.listToArrayOnlyFORComboBoxes(veranstaltungListe.getAllVeranstaltung());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.veranstaltung = new JComboBox<String>(liste);
        this.veranstaltung_label = new JLabel("Veranstaltung: ");
        this.veranstaltung.addItem("Sonstige \u00dcbung / Lehrgang / Fortbildung");
    }

    protected void labelErstellen() {
        this.veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                AtemschutzpassAO.this.executeComboboxThread();
            }
        });
    }

    public void executeComboboxThread() {
        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                    TabelleAtemschutzpass tabAtemschutzpass = new TabelleAtemschutzpass();
                    TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                    int vID = tabVeranstaltung.getVeranstaltungID(AtemschutzpassAO.this.veranstaltung.getSelectedItem().toString());
                    int listLength = tabMitglied.getMitgliederCountAGTGruppe1();
                    if (tabAtemschutzpass.getCountByVeranstaltungID(vID) != 0 && vID != 0) {
                        int i = 0;
                        while (i < listLength) {
                            int mID = tabMitglied.getIdByGuiString(AtemschutzpassAO.this.name[i].getText());
                            String getEinsatzart = tabAtemschutzpass.getEinsatzart(vID, mID);
                            if (getEinsatzart == null) {
                                AtemschutzpassAO.this.einsatzart[i].setSelectedItem("<bitte w\u00e4hlen>");
                            } else {
                                AtemschutzpassAO.this.einsatzart[i].setSelectedItem(getEinsatzart);
                            }
                            int getZeit = tabAtemschutzpass.getZeit(vID, mID);
                            if (getZeit == 0) {
                                AtemschutzpassAO.this.zeit[i].setText(null);
                            } else {
                                AtemschutzpassAO.this.zeit[i].setText(Integer.toString(getZeit));
                            }
                            int getTruppZuordnung = tabAtemschutzpass.getTruppZuordnung(vID, mID);
                            if (getTruppZuordnung == 0) {
                                AtemschutzpassAO.this.truppZuordnung[i].setSelectedItem("keine");
                            } else {
                                AtemschutzpassAO.this.truppZuordnung[i].setSelectedItem(Integer.toString(getTruppZuordnung));
                            }
                            if (tabAnwesenheit.getAnwesendStatus(mID, vID) == 0 && vID != 0) {
                                AtemschutzpassAO.this.einsatzart[i].setBackground(Color.red);
                                AtemschutzpassAO.this.einsatzart[i].setEnabled(false);
                                AtemschutzpassAO.this.truppZuordnung[i].setBackground(Color.red);
                                AtemschutzpassAO.this.truppZuordnung[i].setEnabled(false);
                                AtemschutzpassAO.this.zeit[i].setEnabled(false);
                            } else {
                                AtemschutzpassAO.this.einsatzart[i].setBackground(null);
                                AtemschutzpassAO.this.einsatzart[i].setEnabled(true);
                                AtemschutzpassAO.this.truppZuordnung[i].setBackground(null);
                                AtemschutzpassAO.this.truppZuordnung[i].setEnabled(true);
                                AtemschutzpassAO.this.zeit[i].setEnabled(true);
                            }
                            ++i;
                        }
                    } else {
                        int i = 0;
                        while (i < listLength) {
                            AtemschutzpassAO.this.einsatzart[i].setSelectedItem("<bitte w\u00e4hlen>");
                            AtemschutzpassAO.this.truppZuordnung[i].setSelectedItem("keine");
                            AtemschutzpassAO.this.zeit[i].setText(null);
                            int mID = tabMitglied.getIdByGuiString(AtemschutzpassAO.this.name[i].getText());
                            if (tabAnwesenheit.getAnwesendStatus(mID, vID) == 0 && vID != 0) {
                                AtemschutzpassAO.this.einsatzart[i].setBackground(Color.red);
                                AtemschutzpassAO.this.einsatzart[i].setEnabled(false);
                                AtemschutzpassAO.this.truppZuordnung[i].setBackground(Color.red);
                                AtemschutzpassAO.this.truppZuordnung[i].setEnabled(false);
                                AtemschutzpassAO.this.zeit[i].setEnabled(false);
                            } else {
                                AtemschutzpassAO.this.einsatzart[i].setBackground(null);
                                AtemschutzpassAO.this.einsatzart[i].setEnabled(true);
                                AtemschutzpassAO.this.truppZuordnung[i].setBackground(null);
                                AtemschutzpassAO.this.truppZuordnung[i].setEnabled(true);
                                AtemschutzpassAO.this.zeit[i].setEnabled(true);
                            }
                            ++i;
                        }
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        thread.start();
    }

    protected void setzeAuswahllisten() {
        this.veranstaltung.setSelectedItem(runApplication.letzterVeranstaltungsname);
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(600, 720);
        this.setTitle("FeuerwehrManagementSystem - Atemschutzpass");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.veranstaltung_label);
        this.add(this.veranstaltung);
        this.add(this.beschreibung);
        TabelleAtemschutzpass_einsatzart tabEinsatzart = new TabelleAtemschutzpass_einsatzart();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        try {
            String[] nameListe = Utils.listToArray(tabMitglieder.getAlleAtemschutztraeger());
            String[] truppListe = new String[]{"keine", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};
            this.panel = new JPanel(new GridLayout(nameListe.length, 3));
            this.pane = new JScrollPane(this.panel);
            this.pane.setVerticalScrollBarPolicy(22);
            this.pane.setPreferredSize(new Dimension(550, 500));
            this.einsatzart = new JComboBox[nameListe.length];
            this.zeit = new JTextField[nameListe.length];
            this.name = new JLabel[nameListe.length];
            this.truppZuordnung = new JComboBox[nameListe.length];
            String[] grundListe = Utils.listToArrayOnlyFORComboBoxes(tabEinsatzart.getAllKategorien());
            int i = 0;
            while (i < nameListe.length) {
                this.name[i] = new JLabel(nameListe[i]);
                this.einsatzart[i] = new JComboBox<String>(grundListe);
                this.einsatzart[i].setName(this.name[i].toString());
                this.truppZuordnung[i] = new JComboBox<String>(truppListe);
                this.zeit[i] = new JTextField();
                this.zeit[i].setName(this.name[i].toString());
                this.panel.add(this.name[i]);
                this.panel.add(this.einsatzart[i]);
                this.panel.add(this.truppZuordnung[i]);
                this.panel.add(this.zeit[i]);
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.pane);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonAktualisieren);
        if (MyEvent.event.equals("0x0350")) {
            this.veranstaltung.setSelectedItem(runApplication.letzterVeranstaltungsname);
            this.veranstaltung.setEnabled(false);
            this.executeComboboxThread();
            runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleAtemschutzpass tabAtemschutzpass = new TabelleAtemschutzpass();
                TabelleAtemschutzpass_einsatzart tabEinsatzart = new TabelleAtemschutzpass_einsatzart();
                Atemschutzpass atemschutzpass = new Atemschutzpass();
                try {
                    int vID = tabVeranstaltung.getVeranstaltungID(AtemschutzpassAO.this.veranstaltung.getSelectedItem().toString());
                    int jahr = tabVeranstaltung.getJahrDerVeranstaltung(vID);
                    int vKategorie = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                    int listLength = tabMitglied.getMitgliederCountAGTGruppe1();
                    int checkEinsatzArt = 0;
                    int checkTruppZuordnung = 0;
                    int i = 0;
                    while (i < listLength) {
                        if (!AtemschutzpassAO.this.zeit[i].getText().equals("") && AtemschutzpassAO.this.einsatzart[i].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            ++checkEinsatzArt;
                        }
                        ++i;
                    }
                    i = 0;
                    while (i < listLength) {
                        if (!AtemschutzpassAO.this.zeit[i].getText().equals("") && AtemschutzpassAO.this.truppZuordnung[i].getSelectedItem().toString().equals("keine")) {
                            ++checkTruppZuordnung;
                        }
                        ++i;
                    }
                    if (AtemschutzpassAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                    } else if (checkEinsatzArt != 0) {
                        logging.logInfo((Object)"Hppala, die Einsatzart fehlt...");
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_EINSATZART_PRUEFEN, "Warnung", 2);
                    } else if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtAtemschutzpassHinzuf\u00fcgen").equals("1") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle") && checkTruppZuordnung != 0) {
                        logging.logInfo((Object)"Hppala, die Truppzuweisung fehlt...");
                        JOptionPane.showMessageDialog(null, Konstante.ATEMSCHTZPASS_TRUPP_ZUORDNUNG, "Warnung", 2);
                    } else {
                        if (jahr == 0) {
                            jahr = Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"));
                        }
                        i = 0;
                        while (i < listLength) {
                            if (!AtemschutzpassAO.this.zeit[i].getText().equals("")) {
                                int mID = tabMitglied.getIdByGuiString(AtemschutzpassAO.this.name[i].getText());
                                atemschutzpass.setId(tabAtemschutzpass.getNextNummer());
                                atemschutzpass.setJahr(jahr);
                                atemschutzpass.setMitgliederID(mID);
                                atemschutzpass.setVeranstaltungID(vID);
                                atemschutzpass.setVeranstaltungKategorie(vKategorie);
                                atemschutzpass.setZeit(Integer.parseInt(AtemschutzpassAO.this.zeit[i].getText()));
                                atemschutzpass.setEinsatzart(tabEinsatzart.getID(AtemschutzpassAO.this.einsatzart[i].getSelectedItem().toString()));
                                if (AtemschutzpassAO.this.truppZuordnung[i].getSelectedItem().toString().equals("keine")) {
                                    atemschutzpass.setTruppZuordnung(0);
                                } else {
                                    atemschutzpass.setTruppZuordnung(AtemschutzpassAO.this.truppZuordnung[i].getSelectedIndex());
                                }
                                if (tabAtemschutzpass.getCountByVeranstaltungIDUndMitglied(vID, mID) == 0) {
                                    tabAtemschutzpass.insert(atemschutzpass);
                                } else if (vID == 0) {
                                    tabAtemschutzpass.insert(atemschutzpass);
                                } else {
                                    tabAtemschutzpass.update(atemschutzpass);
                                }
                            }
                            ++i;
                        }
                        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtAtemschutzpassHinzuf\u00fcgen").equals("1") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle") && new TabelleEinsatz_bericht().getAtemschutzStatus(vID) == 0) {
                            TabelleEinsatz_bericht tabBericht = new TabelleEinsatz_bericht();
                            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                            HashMap<String, String> map = tabEinsatz.getData(vID);
                            String dateinameDoc = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/" + tabBericht.getDateiname(vID);
                            String dateinameXml = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + ".xml";
                            String dateinameXmlNeu = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + "_Neu.xml";
                            new File(dateinameDoc).renameTo(new File(dateinameXml));
                            logging.logInfo((Object)"Atemschutzpass: benenne DOC --> XML um...");
                            logging.logInfo((Object)("DOC-Dateiname: " + dateinameDoc));
                            logging.logInfo((Object)("XML-Dateiname: " + dateinameXml));
                            String[] ist = new String[]{"TR11N", "TR11T", "TR11Z", "TR12N", "TR12T", "TR12Z", "TR21N", "TR21T", "TR21Z", "TR22N", "TR22T", "TR22Z", "TR31N", "TR31T", "TR31Z", "TR32N", "TR32T", "TR32Z", "TR41N", "TR41T", "TR41Z", "TR42N", "TR42T", "TR42Z"};
                            String tuppF\u00fchrer1Name = "";
                            String tuppF\u00fchrer1Zeit = "";
                            String tuppF\u00fchrer1Type = "";
                            String tuppMann1Name = "";
                            String tuppMann1Zeit = "";
                            String tuppMann1Type = "";
                            String tuppF\u00fchrer2Name = "";
                            String tuppF\u00fchrer2Zeit = "";
                            String tuppF\u00fchrer2Type = "";
                            String tuppMann2Name = "";
                            String tuppMann2Zeit = "";
                            String tuppMann2Type = "";
                            String tuppF\u00fchrer3Name = "";
                            String tuppF\u00fchrer3Zeit = "";
                            String tuppF\u00fchrer3Type = "";
                            String tuppMann3Name = "";
                            String tuppMann3Zeit = "";
                            String tuppMann3Type = "";
                            String tuppF\u00fchrer4Name = "";
                            String tuppF\u00fchrer4Zeit = "";
                            String tuppF\u00fchrer4Type = "";
                            String tuppMann4Name = "";
                            String tuppMann4Zeit = "";
                            String tuppMann4Type = "";
                            int tf1 = -1;
                            int tm1 = -1;
                            int tf2 = -1;
                            int tm2 = -1;
                            int tf3 = -1;
                            int tm3 = -1;
                            int tf4 = -1;
                            int tm4 = -1;
                            int i2 = 0;
                            while (i2 < listLength) {
                                if (!AtemschutzpassAO.this.zeit[i2].getText().equals("")) {
                                    if (tf1 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 1) {
                                        tf1 = i2;
                                        tuppF\u00fchrer1Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppF\u00fchrer1Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppF\u00fchrer1Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    } else if (tm1 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 1) {
                                        tm1 = i2;
                                        tuppMann1Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppMann1Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppMann1Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    } else if (tf2 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 2) {
                                        tf2 = i2;
                                        tuppF\u00fchrer2Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppF\u00fchrer2Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppF\u00fchrer2Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    } else if (tm2 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 2) {
                                        tm2 = i2;
                                        tuppMann2Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppMann2Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppMann2Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    } else if (tf3 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 3) {
                                        tf3 = i2;
                                        tuppF\u00fchrer3Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppF\u00fchrer3Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppF\u00fchrer3Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    } else if (tm3 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 3) {
                                        tm3 = i2;
                                        tuppMann3Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppMann3Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppMann3Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    } else if (tf4 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 4) {
                                        tf4 = i2;
                                        tuppF\u00fchrer4Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppF\u00fchrer4Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppF\u00fchrer4Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    } else if (tm4 == -1 && AtemschutzpassAO.this.truppZuordnung[i2].getSelectedIndex() == 4) {
                                        tm4 = i2;
                                        tuppMann4Name = Utils.checkTextAndRemoveIllegalSigns(AtemschutzpassAO.this.name[i2].getText());
                                        tuppMann4Type = AtemschutzpassAO.this.einsatzart[i2].getSelectedItem().toString();
                                        tuppMann4Zeit = AtemschutzpassAO.this.zeit[i2].getText();
                                    }
                                }
                                ++i2;
                            }
                            String[] zu = new String[]{tuppF\u00fchrer1Name, tuppF\u00fchrer1Type, tuppF\u00fchrer1Zeit, tuppMann1Name, tuppMann1Type, tuppMann1Zeit, tuppF\u00fchrer2Name, tuppF\u00fchrer2Type, tuppF\u00fchrer2Zeit, tuppMann2Name, tuppMann2Type, tuppMann2Zeit, tuppF\u00fchrer3Name, tuppF\u00fchrer3Type, tuppF\u00fchrer3Zeit, tuppMann3Name, tuppMann3Type, tuppMann3Zeit, tuppF\u00fchrer4Name, tuppF\u00fchrer4Type, tuppF\u00fchrer4Zeit, tuppMann4Name, tuppMann4Type, tuppMann4Zeit};
                            XML.createEinsatzBericht(ist, zu, dateinameXmlNeu, dateinameXml);
                            new File(dateinameXmlNeu).renameTo(new File(dateinameDoc));
                            logging.logInfo((Object)"Atemscutzpass: benenne XML --> DOC um...");
                            new File(dateinameXml).delete();
                            logging.logInfo((Object)"Atemschutzpass: L\u00f6sche Templatefile...");
                            tabBericht.updateAtemschutz(vID);
                            logging.logInfo((Object)"Bericht Tabelle aktualisiert mit dem Atemschutzeintrag!");
                        }
                        logbuchEingabe.NeuerEintag("Atemschutzpasseintrag f\u00fcr der Veranstaltung: " + AtemschutzpassAO.this.veranstaltung.getSelectedItem().toString());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0350")) {
                            AtemschutzpassAO.this.dispose();
                        }
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

