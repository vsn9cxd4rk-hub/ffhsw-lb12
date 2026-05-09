/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao;

import ao.AbstractFenster;
import ao.AnwesenheitEintragenAO;
import data.tabellen.TabelleAbwesenheit;
import data.tabellen.TabelleAbwesenheitsgrund;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Abwesenheit;
import java.awt.Color;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class AbwesenheitAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonAktualisieren;
    private JButton buttonAbwesenheitsgrund;
    public static JComboBox[] grund;
    private JLabel[] name;
    private JComboBox<String> veranstaltung;
    private JLabel veranstaltung_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JPanel panelGrund;
    private JScrollPane pane;

    public AbwesenheitAO() {
        super("FeuerwehrManagementSystem - Abwesenheit Eintragen");
        logging.logInfo((Object)"Starte: AbwesenheitAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonAbwesenheitsgrund = new JButton("Abwesenheitsgrund anlegen");
        this.modulBeschreibung = new JLabel("Abwesenheit Eintragen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        String[] liste = null;
        TabelleVeranstaltung veranstaltungListe = new TabelleVeranstaltung();
        try {
            liste = Utils.listToArrayOnlyFORComboBoxes(veranstaltungListe.getAllVeranstaltungEinerKategorie(2));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.veranstaltung = new JComboBox<String>(liste);
        this.veranstaltung_label = new JLabel("Veranstaltung: ");
    }

    protected void labelErstellen() {
        this.veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    AbwesenheitAO.this.execute(tabMitglied.getMitgliederCountGruppe1());
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    private void execute(final int menge) {
        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    TabelleAbwesenheit tabAbwesenheit = new TabelleAbwesenheit();
                    TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                    TabelleAbwesenheitsgrund tabGrund = new TabelleAbwesenheitsgrund();
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
                    int i = 0;
                    while (i < menge) {
                        grund[i].setBackground(null);
                        grund[i].setEnabled(true);
                        grund[i].removeItem("Anwesend");
                        int mID = tabMitglied.getIdByGuiString(AbwesenheitAO.this.name[i].getText().toString());
                        int vID = tabVeransatltung.getVeranstaltungID(AbwesenheitAO.this.veranstaltung.getSelectedItem().toString());
                        grund[i].setSelectedItem(tabGrund.getAbwesenheitsGrundbyID(tabAbwesenheit.getGrundOfAbwesenheitsByUser(mID, vID)));
                        if (grund[i].getSelectedItem().toString().equals(tabGrund.getAbwesenheitsGrundbyID(0)) && tabAnwesenheit.getAnwesendStatus(mID, vID) == 1) {
                            grund[i].addItem("Anwesend");
                            grund[i].setSelectedItem("Anwesend");
                            grund[i].setEnabled(false);
                            grund[i].setBackground(Color.green);
                        }
                        if (!(grund[i].getSelectedItem().toString().equals(tabGrund.getAbwesenheitsGrundbyID(0)) || grund[i].getSelectedItem().toString().equals("Anwesend") || grund[i].getSelectedItem().toString().equals("Keine Daten verf\u00fcgbar"))) {
                            grund[i].setEnabled(false);
                            grund[i].setBackground(Color.blue);
                        }
                        ++i;
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
        if (!runApplication.letzterVeranstaltungsname.equals("<bitte w\u00e4hlen>")) {
            this.veranstaltung.setEnabled(false);
        }
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(500, 720);
        this.setTitle("FeuerwehrManagementSystem - Abwesenheit Eintragen");
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonAbwesenheitsgrund);
        this.add(this.dummy3);
        this.add(this.veranstaltung_label);
        this.add(this.veranstaltung);
        TabelleAbwesenheitsgrund tabGrund = new TabelleAbwesenheitsgrund();
        TabelleAnwesenheit tabAnwesend = new TabelleAnwesenheit();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        try {
            int menge;
            String[] nameListe;
            int vID = tabVeranstaltung.getVeranstaltungID(this.veranstaltung.getSelectedItem().toString());
            if (MyEvent.event.equals("0x0041")) {
                nameListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
                menge = tabMitglieder.getMitgliederCountGruppe1();
                this.veranstaltung.setEnabled(true);
            } else {
                nameListe = AnwesenheitEintragenAO.abwesendePersonen.toString().split("\n");
                menge = tabMitglieder.getMitgliederCountGruppe1() - tabAnwesend.getGesamtVeranstaltung(vID);
            }
            this.panelGrund = new JPanel(new GridLayout(menge, 2));
            this.pane = new JScrollPane(this.panelGrund);
            this.pane.setVerticalScrollBarPolicy(22);
            this.pane.setPreferredSize(new Dimension(400, 500));
            grund = new JComboBox[menge];
            this.name = new JLabel[menge];
            int i = 0;
            while (i < menge) {
                String[] grundListe = Utils.listToArrayOnlyFORComboBoxes(tabGrund.getAlleGruende());
                this.name[i] = new JLabel(nameListe[i]);
                AbwesenheitAO.grund[i] = new JComboBox<String>(grundListe);
                grund[i].setName(this.name[i].toString());
                grund[i].setSelectedItem("Undefiniert");
                this.panelGrund.add(this.name[i]);
                this.panelGrund.add(grund[i]);
                ++i;
            }
            if (!MyEvent.event.equals("0x0041")) {
                this.execute(menge);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.pane);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonAktualisieren);
        if (BerechtigunsManager.ber[42] == 0) {
            this.buttonAbwesenheitsgrund.setEnabled(false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                AbwesenheitAO.this.buttonZurueck.doClick();
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleAbwesenheit tabAbwesenheit = new TabelleAbwesenheit();
                TabelleAbwesenheitsgrund tabGrund = new TabelleAbwesenheitsgrund();
                TabelleAnwesenheit tabAnwesend = new TabelleAnwesenheit();
                Abwesenheit abwesenheit = new Abwesenheit();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleMitglied tabMitglieder = new TabelleMitglied();
                try {
                    int vID = tabVeranstaltung.getVeranstaltungID(AbwesenheitAO.this.veranstaltung.getSelectedItem().toString());
                    int jahr = Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"));
                    int vKategorie = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                    HashMap<String, Integer> map = tabGrund.getAbwesenheitsGrundMap();
                    int menge = MyEvent.event.equals("0x0041") ? tabMitglieder.getMitgliederCountGruppe1() : tabMitglieder.getMitgliederCountGruppe1() - tabAnwesend.getGesamtVeranstaltung(vID);
                    int i = 0;
                    while (i < menge) {
                        if (grund[i].getSelectedItem().toString().equals("Anwesend") | grund[i].getSelectedItem().toString().equals("Keine Daten verf\u00fcgbar")) {
                            logging.logInfo((Object)"Daten in der Abwesenheitstabelle nicht verf\u00fcgbar muss nicht aktualisiert werden");
                        } else {
                            int mID = tabMitglieder.getIdByGuiString(AbwesenheitAO.this.name[i].getText().toString());
                            abwesenheit.setMitgliederID(mID);
                            abwesenheit.setVeranstaltungID(vID);
                            if (grund[i].getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                                abwesenheit.setGrund(0);
                            } else {
                                abwesenheit.setGrund(map.get(grund[i].getSelectedItem().toString()));
                            }
                            if (tabAbwesenheit.getCountByVeranstaltung(vID, mID) == 0) {
                                abwesenheit.setJahr(jahr);
                                abwesenheit.setId(tabAbwesenheit.getNextNummer());
                                abwesenheit.setVeranstaltungKategorie(vKategorie);
                                tabAbwesenheit.insert(abwesenheit);
                            } else {
                                tabAbwesenheit.update(abwesenheit);
                            }
                        }
                        ++i;
                    }
                    logging.logInfo((Object)"Abwesenheit wurde erfolgreich aktualisiert");
                    logbuchEingabe.NeuerEintag("Abwesenheit wurde aktualisiert: " + AbwesenheitAO.this.veranstaltung.getSelectedItem().toString());
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    if (!MyEvent.event.equals("0x0041")) {
                        AnwesenheitEintragenAO.abwesenheitService();
                        AbwesenheitAO.this.dispose();
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonAbwesenheitsgrund.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0019");
                Steuerung.setStatus(Status.ABWESENHEITSGRUND_ANLEGEN);
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

