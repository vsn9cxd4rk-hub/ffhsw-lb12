/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import go.Mitglieder_Untersuchung;
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

public class MitgliederUntersuchungAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField name_mitglied;
    private JTextField personalnummer;
    private JTextField g26;
    private JTextField g25;
    private JTextField g30;
    private JTextField g41;
    private JTextField g42;
    private JTextField ablaufdatumLKWFuehrerschein;
    private JTextField atemschutztraining;
    private JLabel name_mitglied_label;
    private JLabel personalnummer_label;
    private JLabel g26_label;
    private JLabel g25_label;
    private JLabel g30_label;
    private JLabel g41_label;
    private JLabel g42_label;
    private JLabel ablaufdatumLKWFuehrerscheibn_label;
    private JLabel atemschutztraining_label;
    private JPanel panelMitglieder;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public MitgliederUntersuchungAnlegenAO() {
        super("FeuerwehrManagementSystem - Untersuchung");
        logging.logInfo((Object)"Starte: MitgliederUntersuchungAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.personalnummer = new JTextField(MitgliederAnlegenAO.mitgliedID, 20);
        this.name_mitglied = new JTextField(MitgliederAnlegenAO.mitgliedName, 20);
        this.g26 = new JTextField(20);
        this.g25 = new JTextField(20);
        this.g30 = new JTextField(20);
        this.g41 = new JTextField(20);
        this.g42 = new JTextField(20);
        this.ablaufdatumLKWFuehrerschein = new JTextField();
        this.atemschutztraining = new JTextField(20);
        this.personalnummer_label = new JLabel("Personalnummer");
        this.name_mitglied_label = new JLabel("Mitglied Name: ");
        this.g26_label = new JLabel("G26 / 3 (Format: MM.yyyy): ");
        this.g25_label = new JLabel("G25 (Format: MM.yyyy): ");
        this.g30_label = new JLabel("G30 (Format: MM.yyyy): ");
        this.g41_label = new JLabel("G41 (Format: MM.yyyy): ");
        this.g42_label = new JLabel("G42 (Format: MM.yyyy): ");
        this.ablaufdatumLKWFuehrerscheibn_label = new JLabel("Ablaufdatum LKW F\u00fchrerischein (Format: dd.MM.yyyy): ");
        this.atemschutztraining_label = new JLabel("Atemschutztraining (Format: MM.yyyy): ");
        this.modulBeschreibung = new JLabel("Mitglieder - Untersuchung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.beschreibung = new JLabel("Bitte tragen Sie hier die n\u00e4chste Untersuchung f\u00fcr dieses Mitglied ein");
    }

    protected void labelErstellen() {
        TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
        try {
            int mID = Integer.parseInt(this.personalnummer.getText());
            if (tabUntersuchung.getCount(mID) != 0) {
                this.g26.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG26(mID)));
                this.g25.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG25(mID)));
                this.g30.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG30(mID)));
                this.g41.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG41(mID)));
                this.g42.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG42(mID)));
                this.atemschutztraining.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAgtTraining(mID)));
                this.ablaufdatumLKWFuehrerschein.setText(TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufLKW(mID)));
            }
        }
        catch (NumberFormatException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.setSize(710, 350);
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Untersuchung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.beschreibung);
        this.panelMitglieder = new JPanel(new GridLayout(9, 2));
        this.getContentPane().add("Center", this.panelMitglieder);
        this.panelMitglieder.add(this.personalnummer_label);
        this.panelMitglieder.add(this.personalnummer);
        this.panelMitglieder.add(this.name_mitglied_label);
        this.panelMitglieder.add(this.name_mitglied);
        this.panelMitglieder.add(this.g25_label);
        this.panelMitglieder.add(this.g25);
        this.panelMitglieder.add(this.ablaufdatumLKWFuehrerscheibn_label);
        this.panelMitglieder.add(this.ablaufdatumLKWFuehrerschein);
        this.panelMitglieder.add(this.g26_label);
        this.panelMitglieder.add(this.g26);
        this.panelMitglieder.add(this.g30_label);
        this.panelMitglieder.add(this.g30);
        this.panelMitglieder.add(this.g41_label);
        this.panelMitglieder.add(this.g41);
        this.panelMitglieder.add(this.g42_label);
        this.panelMitglieder.add(this.g42);
        this.panelMitglieder.add(this.atemschutztraining_label);
        this.panelMitglieder.add(this.atemschutztraining);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.personalnummer.setEditable(false);
        this.name_mitglied.setEditable(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
                Mitglieder_Untersuchung untersuchung = new Mitglieder_Untersuchung();
                try {
                    if (!TimeCalculation.checkDateShortFormat(MitgliederUntersuchungAnlegenAO.this.g26.getText()) && !MitgliederUntersuchungAnlegenAO.this.g26.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        MitgliederUntersuchungAnlegenAO.this.g26.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(MitgliederUntersuchungAnlegenAO.this.g25.getText()) && !MitgliederUntersuchungAnlegenAO.this.g25.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        MitgliederUntersuchungAnlegenAO.this.g25.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(MitgliederUntersuchungAnlegenAO.this.g30.getText()) && !MitgliederUntersuchungAnlegenAO.this.g30.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        MitgliederUntersuchungAnlegenAO.this.g30.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(MitgliederUntersuchungAnlegenAO.this.g41.getText()) && !MitgliederUntersuchungAnlegenAO.this.g41.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        MitgliederUntersuchungAnlegenAO.this.g41.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(MitgliederUntersuchungAnlegenAO.this.g42.getText()) && !MitgliederUntersuchungAnlegenAO.this.g42.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        MitgliederUntersuchungAnlegenAO.this.g42.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederUntersuchungAnlegenAO.this.ablaufdatumLKWFuehrerschein.getText()) && !MitgliederUntersuchungAnlegenAO.this.ablaufdatumLKWFuehrerschein.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        MitgliederUntersuchungAnlegenAO.this.ablaufdatumLKWFuehrerschein.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(MitgliederUntersuchungAnlegenAO.this.atemschutztraining.getText()) && !MitgliederUntersuchungAnlegenAO.this.atemschutztraining.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        MitgliederUntersuchungAnlegenAO.this.atemschutztraining.setBackground(Color.red);
                    } else {
                        MitgliederUntersuchungAnlegenAO.this.g26.setBackground(Color.white);
                        MitgliederUntersuchungAnlegenAO.this.g25.setBackground(Color.white);
                        MitgliederUntersuchungAnlegenAO.this.g30.setBackground(Color.white);
                        MitgliederUntersuchungAnlegenAO.this.g41.setBackground(Color.white);
                        MitgliederUntersuchungAnlegenAO.this.g42.setBackground(Color.white);
                        MitgliederUntersuchungAnlegenAO.this.atemschutztraining.setBackground(Color.white);
                        MitgliederUntersuchungAnlegenAO.this.ablaufdatumLKWFuehrerschein.setBackground(Color.white);
                        int mID = Integer.parseInt(MitgliederUntersuchungAnlegenAO.this.personalnummer.getText());
                        untersuchung.setId(mID);
                        untersuchung.setG25(TimeCalculation.parseShortDateForDatabase(MitgliederUntersuchungAnlegenAO.this.g25.getText()));
                        untersuchung.setG30(TimeCalculation.parseShortDateForDatabase(MitgliederUntersuchungAnlegenAO.this.g30.getText()));
                        untersuchung.setG41(TimeCalculation.parseShortDateForDatabase(MitgliederUntersuchungAnlegenAO.this.g41.getText()));
                        untersuchung.setG42(TimeCalculation.parseShortDateForDatabase(MitgliederUntersuchungAnlegenAO.this.g42.getText()));
                        untersuchung.setAblaufLKW(TimeCalculation.parseDateForDatabase(MitgliederUntersuchungAnlegenAO.this.ablaufdatumLKWFuehrerschein.getText()));
                        untersuchung.setG26(TimeCalculation.parseShortDateForDatabase(MitgliederUntersuchungAnlegenAO.this.g26.getText()));
                        untersuchung.setAtemschutztraining(TimeCalculation.parseShortDateForDatabase(MitgliederUntersuchungAnlegenAO.this.atemschutztraining.getText()));
                        untersuchung.setInfoG25(0);
                        untersuchung.setInfoG26(0);
                        untersuchung.setInfoAblaufLKW(0);
                        untersuchung.setInfoG30(0);
                        if (tabUntersuchung.getCount(mID) == 0) {
                            tabUntersuchung.insert(untersuchung);
                        } else {
                            tabUntersuchung.update(untersuchung);
                        }
                        logging.logInfo((Object)"Untersuchung erfolgreich gespeichert");
                        logbuchEingabe.NeuerEintag("Mitgliederuntersuchung wurde angelegt/ge\u00e4ndert: " + mID + " (G25: " + MitgliederUntersuchungAnlegenAO.this.g25.getText() + ")," + " (AblaufLKW: " + MitgliederUntersuchungAnlegenAO.this.ablaufdatumLKWFuehrerschein.getText() + ")," + " (G26/3: " + MitgliederUntersuchungAnlegenAO.this.g26.getText() + ")," + " (AGTTraining: " + MitgliederUntersuchungAnlegenAO.this.atemschutztraining.getText() + ")," + " (G41: " + MitgliederUntersuchungAnlegenAO.this.g41.getText() + ")," + " (G42: " + MitgliederUntersuchungAnlegenAO.this.g42.getText() + ")");
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        MitgliederUntersuchungAnlegenAO.this.dispose();
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

