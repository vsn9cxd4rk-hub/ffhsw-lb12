/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.mitglieder;

import ao.AbstractFenster;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_verfuegbarkeit;
import go.Mitglieder_verfuegbarkeit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.Utils;

public class MitgliederVerfuegbarkeitAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton[] buttonMitglieder;
    private JButton buttonVerfuegbarkeitFuerAlle;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel uhr;

    public MitgliederVerfuegbarkeitAO() {
        super("FeuerwehrManagementSystem - Mitglieder Verf\u00fcgbarkeit");
        logging.logInfo((Object)"Starte: MitgliederVerfuegbarkeitAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonVerfuegbarkeitFuerAlle = new JButton("Verf\u00fcgbarkeit f\u00fcr Alle");
        this.modulBeschreibung = new JLabel("Mitglieder Verf\u00fcgbarkeit");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.uhr = new JLabel();
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
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            this.setSize(1280, 260 + tabMitglied.getMitgliederCountGruppe1() / 4 * 30);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Verf\u00fcgbarkeit");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.uhr);
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            String[] mitgliederListe = Utils.listToArray(tabMitglied.getMitgliederGruppe1());
            int[] mitgliederIDListe = Utils.listToIntArray(tabMitglied.getMitgliederIDGruppe1());
            this.buttonMitglieder = new JButton[mitgliederListe.length];
            int m = 0;
            while (m < mitgliederListe.length) {
                this.buttonMitglieder[m] = new JButton();
                this.buttonMitglieder[m].setPreferredSize(new Dimension(300, 25));
                this.buttonMitglieder[m].setText(mitgliederListe[m]);
                this.buttonMitglieder[m].setName(Integer.toString(mitgliederIDListe[m]));
                this.add(this.buttonMitglieder[m]);
                final int currID = m;
                this.buttonMitglieder[m].addActionListener(new ActionListener(){

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame frame = new JFrame("Frage");
                        Object[] verf\u00fcgbarkeitsStatusListe = new String[]{"Verf\u00fcgbar", "Bedingt Verf\u00fcgbar", "Nicht Verf\u00fcgbar"};
                        String verfuegbarkeitBox = (String)JOptionPane.showInputDialog(frame, "Bitte w\u00e4hlen Sie den Verf\u00fcgbarkeitsstatus f\u00fcr das Mitglied:\n(" + MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].getName() + ") " + MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].getText() + "\n\n", "Frage", 3, null, verf\u00fcgbarkeitsStatusListe, verf\u00fcgbarkeitsStatusListe[0]);
                        if (verf\u00fcgbarkeitsStatusListe != null) {
                            try {
                                TabelleMitglieder_verfuegbarkeit tabVerfuegbarkeit = new TabelleMitglieder_verfuegbarkeit();
                                Mitglieder_verfuegbarkeit verfuegbarkeit = new Mitglieder_verfuegbarkeit();
                                verfuegbarkeit.setMitgliedID(Integer.parseInt(MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].getName()));
                                verfuegbarkeit.setTelegrammID(new TabelleMitglied().getTelegrammID(Integer.parseInt(MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].getName())));
                                if (verfuegbarkeitBox.equals("Verf\u00fcgbar")) {
                                    verfuegbarkeit.setStatus(1);
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].setBackground(Color.green);
                                } else if (verfuegbarkeitBox.equals("Bedingt Verf\u00fcgbar")) {
                                    verfuegbarkeit.setStatus(2);
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].setBackground(Color.yellow);
                                } else if (verfuegbarkeitBox.equals("Nicht Verf\u00fcgbar")) {
                                    verfuegbarkeit.setStatus(0);
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].setBackground(Color.red);
                                }
                                MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].setToolTipText("Mitglied ist: " + verfuegbarkeitBox);
                                if (tabVerfuegbarkeit.getCount(Integer.parseInt(MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].getName())) == 0) {
                                    verfuegbarkeit.setId(tabVerfuegbarkeit.getNextNummer());
                                    tabVerfuegbarkeit.insert(verfuegbarkeit);
                                } else {
                                    tabVerfuegbarkeit.update(verfuegbarkeit);
                                }
                                logging.logInfo((Object)("Verfuegbarkeit von " + MitgliederVerfuegbarkeitAO.this.buttonMitglieder[currID].getText() + " aktualisiert..."));
                            }
                            catch (SQLException e1) {
                                logging.logPrintStackTrace((Exception)e1);
                            }
                        }
                    }
                });
                ++m;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.dummy2);
        this.add(this.buttonVerfuegbarkeitFuerAlle);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonVerfuegbarkeitFuerAlle.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Frage");
                Object[] verf\u00fcgbarkeitsStatusListe = new String[]{"Verf\u00fcgbar", "Bedingt Verf\u00fcgbar", "Nicht Verf\u00fcgbar"};
                String verfuegbarkeitBox = (String)JOptionPane.showInputDialog(frame, "Bitte w\u00e4hlen Sie den Verf\u00fcgbarkeitsstatus f\u00fcr das alle Mitglied aus!\n\n", "Frage", 3, null, verf\u00fcgbarkeitsStatusListe, verf\u00fcgbarkeitsStatusListe[0]);
                int v = 0;
                while (v < MitgliederVerfuegbarkeitAO.this.buttonMitglieder.length) {
                    if (verf\u00fcgbarkeitsStatusListe != null) {
                        try {
                            TabelleMitglieder_verfuegbarkeit tabVerfuegbarkeit = new TabelleMitglieder_verfuegbarkeit();
                            Mitglieder_verfuegbarkeit verfuegbarkeit = new Mitglieder_verfuegbarkeit();
                            verfuegbarkeit.setMitgliedID(Integer.parseInt(MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].getName()));
                            verfuegbarkeit.setTelegrammID(new TabelleMitglied().getTelegrammID(Integer.parseInt(MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].getName())));
                            if (verfuegbarkeitBox.equals("Verf\u00fcgbar")) {
                                verfuegbarkeit.setStatus(1);
                                MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setBackground(Color.green);
                                MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setToolTipText("Mitglied ist: " + verfuegbarkeitBox);
                            } else if (verfuegbarkeitBox.equals("Bedingt Verf\u00fcgbar")) {
                                verfuegbarkeit.setStatus(2);
                                MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setBackground(Color.yellow);
                                MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setToolTipText("Mitglied ist: " + verfuegbarkeitBox);
                            } else if (verfuegbarkeitBox.equals("Nicht Verf\u00fcgbar")) {
                                verfuegbarkeit.setStatus(0);
                                MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setBackground(Color.red);
                                MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setToolTipText("Mitglied ist: " + verfuegbarkeitBox);
                            }
                            if (tabVerfuegbarkeit.getCount(Integer.parseInt(MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].getName())) == 0) {
                                verfuegbarkeit.setId(tabVerfuegbarkeit.getNextNummer());
                                tabVerfuegbarkeit.insert(verfuegbarkeit);
                            } else {
                                tabVerfuegbarkeit.update(verfuegbarkeit);
                            }
                            logging.logInfo((Object)("Verfuegbarkeit von " + MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].getText() + " aktualisiert..."));
                        }
                        catch (SQLException e1) {
                            logging.logPrintStackTrace((Exception)e1);
                        }
                    }
                    ++v;
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
        Thread threadVerfuegbarkeit = new Thread(){

            @Override
            public void run() {
                logging.logInfo((Object)"pr\u00fcfe Verf\u00fcgbarkeit");
                while (true) {
                    try {
                        while (true) {
                            TabelleMitglieder_verfuegbarkeit tabVerfuegbarkeit = new TabelleMitglieder_verfuegbarkeit();
                            int[] status = Utils.listToIntArray(tabVerfuegbarkeit.getAlleVerfuegbarkeiten());
                            int v = 0;
                            while (v < status.length) {
                                if (status[v] == 1) {
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setBackground(Color.green);
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setToolTipText("Mitglied ist: Verf\u00fcgbar");
                                } else if (status[v] == 2) {
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setBackground(Color.yellow);
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setToolTipText("Mitglied ist: Bedingt Verf\u00fcgbar");
                                } else if (status[v] == 0) {
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setBackground(Color.red);
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setToolTipText("Mitglied ist: Nicht Verf\u00fcgbar");
                                } else {
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setBackground(null);
                                    MitgliederVerfuegbarkeitAO.this.buttonMitglieder[v].setToolTipText("Status ist n. V.");
                                }
                                ++v;
                            }
                            Thread.sleep(30000L);
                        }
                    }
                    catch (InterruptedException | SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                        continue;
                    }
                }
            }
        };
        threadVerfuegbarkeit.start();
        Thread threadUhr = new Thread(){

            @Override
            public void run() {
                while (true) {
                    try {
                        while (true) {
                            MitgliederVerfuegbarkeitAO.this.uhr.setText("                                                                                                                                                                                                                                                                                                                                                          " + SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy  HH:mm:ss"));
                            Thread.sleep(1000L);
                        }
                    }
                    catch (InterruptedException e) {
                        logging.logPrintStackTrace((Exception)e);
                        continue;
                    }
                }
            }
        };
        threadUhr.start();
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

