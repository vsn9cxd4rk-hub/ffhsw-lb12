/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.fahrzeuge;

import ao.AbstractFenster;
import ao.fahrzeuge.FahrzeugAnlegenAO;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeug_untersuchung;
import data.tabellen.einstellungen.TabelleMandant;
import go.Fahrzeug_Untersuchung;
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

public class FahrzeugUntersuchungAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField name_mitglied;
    private JTextField fahrzeugID;
    private JTextField t\u00fcv;
    private JTextField sicherheitspr\u00fcfung;
    private JTextField wartung;
    private JTextField gasWartung;
    private JLabel name_mitglied_label;
    private JLabel personalnummer_label;
    private JLabel t\u00fcv_label;
    private JLabel sicherheitspr\u00fcfung_label;
    private JLabel wartung_label;
    private JLabel gasWartung_label;
    private JPanel panelFarzeug;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public FahrzeugUntersuchungAnlegenAO() {
        super("FeuerwehrManagementSystem - Fahrzeuguntersuchung");
        logging.logInfo((Object)"Starte: FahrzeugUntersuchungAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.fahrzeugID = new JTextField(FahrzeugAnlegenAO.fahrzeugID.getText(), 20);
        this.name_mitglied = new JTextField(FahrzeugAnlegenAO.name.getText(), 20);
        this.t\u00fcv = new JTextField(20);
        this.sicherheitspr\u00fcfung = new JTextField(20);
        this.wartung = new JTextField(20);
        this.gasWartung = new JTextField(20);
        this.personalnummer_label = new JLabel("FahrzeugID: ");
        this.name_mitglied_label = new JLabel("Fahrzeug Name: ");
        this.t\u00fcv_label = new JLabel("T\u00dcV / AU (Format: MM.yyyy): ");
        this.sicherheitspr\u00fcfung_label = new JLabel("SP LKW (Format: MM.yyyy): ");
        this.wartung_label = new JLabel("Wartung / Service (Format: MM.yyyy): ");
        this.gasWartung_label = new JLabel("Wartung f\u00fcr Gassysteme (Format: MM.yyyy): ");
        this.modulBeschreibung = new JLabel("Fahrzeug - Untersuchung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.beschreibung = new JLabel("Bitte tragen Sie hier die n\u00e4chste Untersuchung / Wartung f\u00fcr dieses Fahrzeug ein");
    }

    protected void labelErstellen() {
        TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
        try {
            int mID = Integer.parseInt(this.fahrzeugID.getText());
            if (tabUntersuchung.getCount(mID) != 0) {
                this.t\u00fcv.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getT\u00fcv(Integer.parseInt(this.fahrzeugID.getText()))));
                this.sicherheitspr\u00fcfung.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getSP(Integer.parseInt(this.fahrzeugID.getText()))));
                this.wartung.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getService(Integer.parseInt(this.fahrzeugID.getText()))));
                this.gasWartung.setText(TimeCalculation.parseShortDateForGUI(tabUntersuchung.getGasWartung(Integer.parseInt(this.fahrzeugID.getText()))));
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
        this.setSize(600, 280);
        this.setTitle("FeuerwehrManagementSystem - Fahrzeug Untersuchung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.beschreibung);
        this.panelFarzeug = new JPanel(new GridLayout(6, 2));
        this.getContentPane().add("Center", this.panelFarzeug);
        this.panelFarzeug.add(this.personalnummer_label);
        this.panelFarzeug.add(this.fahrzeugID);
        this.panelFarzeug.add(this.name_mitglied_label);
        this.panelFarzeug.add(this.name_mitglied);
        this.panelFarzeug.add(this.sicherheitspr\u00fcfung_label);
        this.panelFarzeug.add(this.sicherheitspr\u00fcfung);
        this.panelFarzeug.add(this.t\u00fcv_label);
        this.panelFarzeug.add(this.t\u00fcv);
        this.panelFarzeug.add(this.wartung_label);
        this.panelFarzeug.add(this.wartung);
        this.panelFarzeug.add(this.gasWartung_label);
        this.panelFarzeug.add(this.gasWartung);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.fahrzeugID.setEditable(false);
        this.name_mitglied.setEditable(false);
        try {
            TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
            if (tabFahrzeug.getBeschreibungID(Integer.parseInt(this.fahrzeugID.getText())) != 11) {
                this.gasWartung.setVisible(false);
                this.gasWartung_label.setVisible(false);
            }
        }
        catch (NumberFormatException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
                Fahrzeug_Untersuchung untersuchung = new Fahrzeug_Untersuchung();
                try {
                    if (!TimeCalculation.checkDateShortFormat(FahrzeugUntersuchungAnlegenAO.this.t\u00fcv.getText()) && !FahrzeugUntersuchungAnlegenAO.this.t\u00fcv.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        FahrzeugUntersuchungAnlegenAO.this.t\u00fcv.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(FahrzeugUntersuchungAnlegenAO.this.sicherheitspr\u00fcfung.getText()) && !FahrzeugUntersuchungAnlegenAO.this.sicherheitspr\u00fcfung.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        FahrzeugUntersuchungAnlegenAO.this.sicherheitspr\u00fcfung.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(FahrzeugUntersuchungAnlegenAO.this.wartung.getText()) && !FahrzeugUntersuchungAnlegenAO.this.wartung.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        FahrzeugUntersuchungAnlegenAO.this.wartung.setBackground(Color.red);
                    } else {
                        FahrzeugUntersuchungAnlegenAO.this.t\u00fcv.setBackground(Color.white);
                        FahrzeugUntersuchungAnlegenAO.this.sicherheitspr\u00fcfung.setBackground(Color.white);
                        FahrzeugUntersuchungAnlegenAO.this.wartung.setBackground(Color.white);
                        int fID = Integer.parseInt(FahrzeugUntersuchungAnlegenAO.this.fahrzeugID.getText());
                        untersuchung.setId(fID);
                        untersuchung.setSp(TimeCalculation.parseShortDateForDatabase(FahrzeugUntersuchungAnlegenAO.this.sicherheitspr\u00fcfung.getText()));
                        untersuchung.setT\u00fcv(TimeCalculation.parseShortDateForDatabase(FahrzeugUntersuchungAnlegenAO.this.t\u00fcv.getText()));
                        untersuchung.setService(TimeCalculation.parseShortDateForDatabase(FahrzeugUntersuchungAnlegenAO.this.wartung.getText()));
                        untersuchung.setGaswartung(TimeCalculation.parseShortDateForDatabase(FahrzeugUntersuchungAnlegenAO.this.gasWartung.getText()));
                        untersuchung.setInfoTuev(0);
                        untersuchung.setInfoSP(0);
                        untersuchung.setInfoService(0);
                        untersuchung.setInfoGas(0);
                        untersuchung.setMandantID(new TabelleMandant().getMandantID(FahrzeugAnlegenAO.mandant.getSelectedItem().toString()));
                        if (tabUntersuchung.getCount(fID) == 0) {
                            tabUntersuchung.insert(untersuchung);
                        } else {
                            tabUntersuchung.update(untersuchung);
                        }
                        logging.logInfo((Object)"Untersuchung erfolgreich gespeichert");
                        logbuchEingabe.NeuerEintag("Fahrzeug untersuchung wurde ge\u00e4ndert: " + fID + " (SP: " + FahrzeugUntersuchungAnlegenAO.this.sicherheitspr\u00fcfung.getText() + "), " + " (T\u00dcV: " + FahrzeugUntersuchungAnlegenAO.this.t\u00fcv.getText() + "), " + " (Wartung: " + FahrzeugUntersuchungAnlegenAO.this.wartung.getText() + ")" + " (GasWartung: " + FahrzeugUntersuchungAnlegenAO.this.gasWartung.getText() + ")");
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        FahrzeugUntersuchungAnlegenAO.this.dispose();
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

