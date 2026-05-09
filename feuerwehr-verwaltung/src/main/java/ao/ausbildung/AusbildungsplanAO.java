/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.ausbildung;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import data.tabellen.TabelleAusbildung_Kategorie;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import go.Ausbildung_Plan;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class AusbildungsplanAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonAktualisieren;
    private JButton buttonAusbildungKategorie;
    private JButton buttonDruckvorschau;
    private JButton buttonTauschen;
    private JButton buttonDaten\u00fcbernahmeVorjahr;
    public static JComboBox[] ausbildungkategorien;
    public static JComboBox[] ausbilder1;
    public static JComboBox[] ausbilder2;
    public static JTextField[] details;
    private JLabel[] veranstaltungName;
    public static JComboBox<String> jahre;
    private JLabel jahre_label;
    private JLabel beschriftung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelGrund;
    private JScrollPane pane;

    public AusbildungsplanAO() {
        super("FeuerwehrManagementSystem - Ausbildungsplan");
        logging.logInfo((Object)"Starte: Ausbildungsplan");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonAusbildungKategorie = new JButton("Neue Kategorie anlegen");
        this.buttonDruckvorschau = new JButton("Druckvorschau");
        this.buttonTauschen = new JButton("Ausbildungsinhalte Tauschen");
        this.buttonDaten\u00fcbernahmeVorjahr = new JButton("Daten\u00fcbernahme Vorjahr");
        this.beschriftung = new JLabel("Dienst / Veranstaltung                                          Ausbildungsinhalt                                          Details / Kommentar                                          Ausbilder 1                                          Ausbilder 2                             ");
        this.modulBeschreibung = new JLabel("Ausbildungsplan");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        String[] liste = null;
        TabelleJahr tabJahr = new TabelleJahr();
        try {
            liste = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            jahre = new JComboBox<String>(liste);
            this.jahre_label = new JLabel("Jahr: ");
            jahre.addItem(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1));
            jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelErstellen() {
        jahre.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                AusbildungsplanAO.this.panelGrund.removeAll();
                AusbildungsplanAO.this.remove(AusbildungsplanAO.this.dummy2);
                AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonZurueck);
                AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonTauschen);
                AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonDaten\u00fcbernahmeVorjahr);
                AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonAktualisieren);
                AusbildungsplanAO.this.remove(AusbildungsplanAO.this.pane);
                AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonDruckvorschau);
                AusbildungsplanAO.this.repaint();
                AusbildungsplanAO.this.validate();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
                TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    if (jahre.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
                    } else {
                        int menge = tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(jahre.getSelectedItem().toString(), 2);
                        String[] nameListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieByJahr(2, Integer.parseInt(jahre.getSelectedItem().toString())));
                        AusbildungsplanAO.this.panelGrund = new JPanel(new GridLayout(menge, 2));
                        AusbildungsplanAO.this.pane = new JScrollPane(AusbildungsplanAO.this.panelGrund);
                        AusbildungsplanAO.this.pane.setVerticalScrollBarPolicy(22);
                        AusbildungsplanAO.this.pane.setPreferredSize(new Dimension(1200, 500));
                        ausbildungkategorien = new JComboBox[menge];
                        ausbilder1 = new JComboBox[menge];
                        ausbilder2 = new JComboBox[menge];
                        details = new JTextField[menge];
                        AusbildungsplanAO.this.veranstaltungName = new JLabel[menge];
                        String[] kategorieListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAllKategorien());
                        String[] ausbilderName = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
                        int i = 0;
                        while (i < menge) {
                            ((AusbildungsplanAO)AusbildungsplanAO.this).veranstaltungName[i] = new JLabel(nameListe[i]);
                            AusbildungsplanAO.ausbildungkategorien[i] = new JComboBox<String>(kategorieListe);
                            ausbildungkategorien[i].setName(AusbildungsplanAO.this.veranstaltungName[i].toString());
                            AusbildungsplanAO.details[i] = new JTextField(20);
                            AusbildungsplanAO.ausbilder1[i] = new JComboBox<String>(ausbilderName);
                            AusbildungsplanAO.ausbilder2[i] = new JComboBox<String>(ausbilderName);
                            int vID = tabVeranstaltung.getVeranstaltungID(nameListe[i]);
                            if (tabPlan.getCountVeranstaltungID(vID) == 1) {
                                int kID = tabPlan.getAusbildungKategorie(vID);
                                int a1ID = tabPlan.getAusbilder1(vID);
                                int a2ID = tabPlan.getAusbilder2(vID);
                                if (kID != 0) {
                                    String mName;
                                    ausbildungkategorien[i].setSelectedItem(tabKategorie.getNameByID(kID));
                                    details[i].setText(tabPlan.getDeatils(vID));
                                    if (a1ID == 0) {
                                        ausbilder1[i].setSelectedItem("<bitte w\u00e4hlen>");
                                    } else {
                                        mName = tabMitglied.getNameVornameByID(a1ID);
                                        ausbilder1[i].setSelectedItem(mName);
                                        if (ausbilder1[i].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                                            ausbilder1[i].addItem(mName);
                                            ausbilder1[i].setSelectedItem(mName);
                                        }
                                    }
                                    if (a2ID == 0) {
                                        ausbilder2[i].setSelectedItem("<bitte w\u00e4hlen>");
                                    } else {
                                        mName = tabMitglied.getNameVornameByID(a2ID);
                                        ausbilder2[i].setSelectedItem(mName);
                                        if (ausbilder2[i].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                                            ausbilder2[i].addItem(mName);
                                            ausbilder2[i].setSelectedItem(mName);
                                        }
                                    }
                                }
                            }
                            AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.this.veranstaltungName[i]);
                            AusbildungsplanAO.this.panelGrund.add(ausbildungkategorien[i]);
                            AusbildungsplanAO.this.panelGrund.add(details[i]);
                            AusbildungsplanAO.this.panelGrund.add(ausbilder1[i]);
                            AusbildungsplanAO.this.panelGrund.add(ausbilder2[i]);
                            ++i;
                        }
                        AusbildungsplanAO.this.panelGrund.validate();
                        AusbildungsplanAO.this.add(AusbildungsplanAO.this.pane);
                        AusbildungsplanAO.this.add(AusbildungsplanAO.this.dummy2);
                        AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonZurueck);
                        AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonAktualisieren);
                        AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonTauschen);
                        AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonDaten\u00fcbernahmeVorjahr);
                        AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonDruckvorschau);
                        AusbildungsplanAO.this.repaint();
                        AusbildungsplanAO.this.validate();
                        if (Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) <= Integer.parseInt(jahre.getSelectedItem().toString())) {
                            AusbildungsplanAO.this.buttonDaten\u00fcbernahmeVorjahr.setVisible(true);
                        } else {
                            AusbildungsplanAO.this.buttonDaten\u00fcbernahmeVorjahr.setVisible(false);
                        }
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                    MyEvent.setEvent((String)"0x0030");
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
        this.setSize(1280, 710);
        this.setTitle("FeuerwehrManagementSystem - Ausbildungsplan");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.jahre_label);
        this.add(jahre);
        this.add(this.buttonAusbildungKategorie);
        this.add(this.beschriftung);
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
        TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        try {
            int menge = tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(jahre.getSelectedItem().toString(), 2);
            String[] nameListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieByJahr(2, Integer.parseInt(jahre.getSelectedItem().toString())));
            String[] ausbilderName = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            this.panelGrund = new JPanel(new GridLayout(menge, 5));
            this.pane = new JScrollPane(this.panelGrund);
            this.pane.setVerticalScrollBarPolicy(22);
            this.pane.setPreferredSize(new Dimension(1250, 500));
            ausbildungkategorien = new JComboBox[menge];
            ausbilder1 = new JComboBox[menge];
            ausbilder2 = new JComboBox[menge];
            details = new JTextField[menge];
            this.veranstaltungName = new JLabel[menge];
            String[] kategorieListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAllKategorien());
            int i = 0;
            while (i < menge) {
                this.veranstaltungName[i] = new JLabel(nameListe[i]);
                AusbildungsplanAO.ausbildungkategorien[i] = new JComboBox<String>(kategorieListe);
                ausbildungkategorien[i].setName(this.veranstaltungName[i].toString());
                AusbildungsplanAO.details[i] = new JTextField(20);
                AusbildungsplanAO.ausbilder1[i] = new JComboBox<String>(ausbilderName);
                AusbildungsplanAO.ausbilder2[i] = new JComboBox<String>(ausbilderName);
                int vID = tabVeranstaltung.getVeranstaltungID(nameListe[i]);
                if (tabPlan.getCountVeranstaltungID(vID) == 1) {
                    int kID = tabPlan.getAusbildungKategorie(vID);
                    int a1ID = tabPlan.getAusbilder1(vID);
                    int a2ID = tabPlan.getAusbilder2(vID);
                    if (kID != 0) {
                        ausbildungkategorien[i].setSelectedItem(tabKategorie.getNameByID(kID));
                        details[i].setText(tabPlan.getDeatils(vID));
                        if (a1ID == 0) {
                            ausbilder1[i].setSelectedItem("<bitte w\u00e4hlen>");
                        } else {
                            ausbilder1[i].setSelectedItem(tabMitglied.getNameVornameByID(a1ID));
                        }
                        if (a2ID == 0) {
                            ausbilder2[i].setSelectedItem("<bitte w\u00e4hlen>");
                        } else {
                            ausbilder2[i].setSelectedItem(tabMitglied.getNameVornameByID(a2ID));
                        }
                    }
                }
                this.panelGrund.add(this.veranstaltungName[i]);
                this.panelGrund.add(ausbildungkategorien[i]);
                this.panelGrund.add(details[i]);
                this.panelGrund.add(ausbilder1[i]);
                this.panelGrund.add(ausbilder2[i]);
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
        this.add(this.buttonTauschen);
        this.add(this.buttonDaten\u00fcbernahmeVorjahr);
        this.add(this.buttonDruckvorschau);
        this.buttonDaten\u00fcbernahmeVorjahr.setVisible(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                AusbildungsplanAO.this.buttonZurueck.doClick();
            }
        });
        this.buttonZurueck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.instanceofAusbildungsplanISRunning = 0;
                AusbildungsplanAO.this.dispose();
            }
        });
        this.buttonDaten\u00fcbernahmeVorjahr.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
                    int jahr = Integer.parseInt(jahre.getSelectedItem().toString()) - 1;
                    int anzahl = tabPlan.getCountAusbildungenProJahr(jahr);
                    HashMap<Integer, String[]> mapData = tabPlan.getDatenF\u00fcrDaten\u00fcbernahme(jahr);
                    String[] data = new String[7];
                    int i = 0;
                    while (i < anzahl) {
                        try {
                            data = mapData.get(i);
                            ausbildungkategorien[i].setSelectedItem(data[3]);
                            details[i].setText(data[4]);
                            ausbilder1[i].setSelectedItem(data[5]);
                            ausbilder2[i].setSelectedItem(data[6]);
                        }
                        catch (ArrayIndexOutOfBoundsException e) {
                            logging.logWarning((Object)"Es gibt keine Veranstaltungen mehr um Daten aus dem Vorjahr zu importieren");
                            break;
                        }
                        ++i;
                    }
                    JOptionPane.showMessageDialog(null, Konstante.AUSBILDUNGSPLAN_IMPORT_ERFOLGREICH);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonTauschen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.AUSBILDUNGSINHALTE_TAUSCHEN);
                Steuerung.steuerung();
            }
        });
        this.buttonDruckvorschau.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.AUSBILDUNGPLAN_LISTE);
                Steuerung.steuerung();
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("Inhalte werden gespeichert... Bitte warten...");
                Thread threadSpeichern = new Thread(){

                    @Override
                    public void run() {
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
                        TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
                        Ausbildung_Plan plan = new Ausbildung_Plan();
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        try {
                            HashMap<Integer, String> map = null;
                            if (runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden").equals("1")) {
                                map = tabMitglied.getMitgliederListe();
                            }
                            int i = 0;
                            while (i < AusbildungsplanAO.this.veranstaltungName.length) {
                                int vID = tabVeranstaltung.getVeranstaltungID(AusbildungsplanAO.this.veranstaltungName[i].getText());
                                plan.setId(tabPlan.getNextNummer());
                                plan.setJahr(Integer.parseInt(jahre.getSelectedItem().toString()));
                                plan.setVeranstaltungID(vID);
                                plan.setAusbildungKategorie(tabKategorie.getID(ausbildungkategorien[i].getSelectedItem().toString()));
                                plan.setDetails(details[i].getText());
                                if (ausbilder1[i].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                    plan.setAusbilder1(0);
                                } else {
                                    plan.setAusbilder1(tabMitglied.getIdByGuiString(ausbilder1[i].getSelectedItem().toString()));
                                }
                                if (ausbilder2[i].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                                    plan.setAusbilder2(0);
                                } else {
                                    plan.setAusbilder2(tabMitglied.getIdByGuiString(ausbilder2[i].getSelectedItem().toString()));
                                }
                                if (tabPlan.getCountVeranstaltungID(vID) != 0) {
                                    tabPlan.update(plan);
                                } else {
                                    tabPlan.insert(plan);
                                }
                                if (runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden").equals("1")) {
                                    Joomla.erstelleAusbildungsplan(plan, map);
                                }
                                ++i;
                            }
                            MyEvent.setEvent((String)"0x0030");
                            logging.logInfo((Object)"Ausbildungsplan wurde aktualisiert");
                            logbuchEingabe.NeuerEintag("Ausbildungsplan wurde aktualisiert/ge\u00e4ndert f\u00fcr: " + jahre.getSelectedItem().toString());
                            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        }
                        catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                            logging.logPrintStackTrace((Exception)e);
                        }
                    }
                };
                threadSpeichern.start();
            }
        });
        this.buttonAusbildungKategorie.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0027");
                Steuerung.setStatus(Status.AUSBILDUNG_KATEGORIE);
                Steuerung.steuerung();
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
        runApplication.instanceofAusbildungsplanISRunning = 1;
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

