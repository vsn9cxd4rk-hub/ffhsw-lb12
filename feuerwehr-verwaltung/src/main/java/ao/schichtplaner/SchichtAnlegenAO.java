/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.schichtplaner;

import ao.AbstractFenster;
import ao.schichtplaner.SchichtplanerAO;
import data.tabellen.schicht.TabelleSchicht;
import go.schicht.Schicht;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.TimeCalculation;
import utilities.logbuchEingabe;

public class SchichtAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JLabel schichtStartDatum_label;
    private JLabel schichtStartUhrzeit_label;
    private JLabel schichtEndeDatum_label;
    private JLabel schichtEndeUhrzeit_label;
    private JTextField schichtStartDatum;
    private JTextField schichtStartUhrzeit;
    private JTextField schichtEndeDatum;
    private JTextField schichtEndeUhrzeit;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    public static String letzteKategorie;

    public SchichtAnlegenAO() {
        super("FeuerwehrManagementSystem - Schichtplaner");
        logging.logInfo((Object)"Starte: SchichtplanerAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.schichtStartDatum = new JTextField(20);
        this.schichtStartUhrzeit = new JTextField(20);
        this.schichtEndeDatum = new JTextField(20);
        this.schichtEndeUhrzeit = new JTextField(20);
        this.schichtStartDatum_label = new JLabel("Start Datum: ");
        this.schichtStartUhrzeit_label = new JLabel("Start Uhrzeit: ");
        this.schichtEndeDatum_label = new JLabel("Ende Datum: ");
        this.schichtEndeUhrzeit_label = new JLabel("Ende Uhrzeit: ");
        this.modulBeschreibung = new JLabel("Schichtplaner");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
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
        this.setSize(500, 260);
        this.setTitle("FeuerwehrManagementSystem - Schichtplaner");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.schichtStartDatum_label);
        this.panel.add(this.schichtStartDatum);
        this.panel.add(this.schichtStartUhrzeit_label);
        this.panel.add(this.schichtStartUhrzeit);
        this.panel.add(this.schichtEndeDatum_label);
        this.panel.add(this.schichtEndeDatum);
        this.panel.add(this.schichtEndeUhrzeit_label);
        this.panel.add(this.schichtEndeUhrzeit);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleSchicht tabSchicht = new TabelleSchicht();
                    Schicht schicht = new Schicht();
                    System.out.println(TimeCalculation.calculateDuration(SchichtAnlegenAO.this.schichtStartUhrzeit.getText(), SchichtAnlegenAO.this.schichtEndeUhrzeit.getText()));
                    if (!TimeCalculation.checkDateFormat(SchichtAnlegenAO.this.schichtStartDatum.getText())) {
                        SchichtAnlegenAO.this.schichtStartDatum.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else if (!TimeCalculation.checkDateFormat(SchichtAnlegenAO.this.schichtEndeDatum.getText())) {
                        SchichtAnlegenAO.this.schichtEndeDatum.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else if (!TimeCalculation.checkTimeFormat(SchichtAnlegenAO.this.schichtStartUhrzeit.getText())) {
                        SchichtAnlegenAO.this.schichtStartUhrzeit.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else if (!TimeCalculation.checkTimeFormat(SchichtAnlegenAO.this.schichtEndeUhrzeit.getText())) {
                        SchichtAnlegenAO.this.schichtEndeUhrzeit.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else {
                        SchichtAnlegenAO.this.schichtStartDatum.setBackground(Color.white);
                        SchichtAnlegenAO.this.schichtEndeDatum.setBackground(Color.white);
                        SchichtAnlegenAO.this.schichtStartUhrzeit.setBackground(Color.white);
                        SchichtAnlegenAO.this.schichtEndeUhrzeit.setBackground(Color.white);
                        String schichtName = "Schicht (" + SchichtAnlegenAO.this.schichtStartDatum.getText() + ", " + SchichtAnlegenAO.this.schichtStartUhrzeit.getText() + " - " + SchichtAnlegenAO.this.schichtEndeDatum.getText() + ", " + SchichtAnlegenAO.this.schichtEndeUhrzeit.getText() + ")";
                        schicht.setId(tabSchicht.getNextNummer());
                        schicht.setJahr(Integer.parseInt(SchichtAnlegenAO.this.schichtStartDatum.getText().substring(6, 10)));
                        schicht.setName(schichtName);
                        schicht.setSchichtStartDatum(TimeCalculation.parseDateForDatabase(SchichtAnlegenAO.this.schichtStartDatum.getText()));
                        schicht.setSchichtStartUhrzeit(SchichtAnlegenAO.this.schichtStartUhrzeit.getText());
                        schicht.setSchichtEndeDatum(TimeCalculation.parseDateForDatabase(SchichtAnlegenAO.this.schichtEndeDatum.getText()));
                        schicht.setSchichtEndeUhrzeit(SchichtAnlegenAO.this.schichtEndeUhrzeit.getText());
                        if (SchichtAnlegenAO.this.schichtStartDatum.getText().equals(SchichtAnlegenAO.this.schichtEndeDatum.getText())) {
                            schicht.setMinutenVon(TimeCalculation.calculateDuration("00:00", SchichtAnlegenAO.this.schichtStartUhrzeit.getText()));
                            schicht.setMinutenBis(TimeCalculation.calculateDuration("00:00", SchichtAnlegenAO.this.schichtEndeUhrzeit.getText()));
                        } else {
                            schicht.setMinutenVon(TimeCalculation.calculateDuration("00:00", SchichtAnlegenAO.this.schichtStartUhrzeit.getText()));
                            schicht.setMinutenBis(TimeCalculation.calculateDuration("00:00", SchichtAnlegenAO.this.schichtEndeUhrzeit.getText()) + 1440);
                        }
                        tabSchicht.insert(schicht);
                        logbuchEingabe.NeuerEintag("Schicht wurde angelegt: " + schichtName);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        SchichtplanerAO.schichtListe.addItem(schichtName);
                        SchichtAnlegenAO.this.dispose();
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

