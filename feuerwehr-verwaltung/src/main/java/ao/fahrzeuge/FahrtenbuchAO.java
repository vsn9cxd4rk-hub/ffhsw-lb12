/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.fahrzeuge;

import ao.AbstractFenster;
import data.tabellen.TabelleFahrtenbuch;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Fahrtenbuch;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import java.util.HashMap;
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
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.logbuchEingabe;

public class FahrtenbuchAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JComboBox<String> fahrzeugName;
    private JComboBox<String> veranstaltung;
    private JTextField kmBeginn;
    private JTextField kmEnde;
    private JTextField tanken;
    private JTextField pumpenbetrieb;
    private JTextField sonstiges;
    private JComboBox<String> fahrer;
    private JTextField datumVon;
    private JTextField datumBis;
    private JTextField zeitVon;
    private JTextField zeitBis;
    private JLabel fahrzeugName_label;
    private JLabel veranstaltung_label;
    private JLabel kmBeginn_label;
    private JLabel kmEnde_label;
    private JLabel tanken_lanel;
    private JLabel pumpenbetrieb_label;
    private JLabel sonstiges_label;
    private JLabel fahrer_label;
    private JLabel datumVon_label;
    private JLabel datumBis_label;
    private JLabel zeitVon_label;
    private JLabel zeitBis_label;
    private JPanel panelFahrtenbuch;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public FahrtenbuchAO() {
        super("FeuerwehrManagementSystem - Fahrtenbuch");
        logging.logInfo((Object)"Starte: FahrtenbuchAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.kmBeginn = new JTextField(20);
        this.kmEnde = new JTextField(20);
        this.tanken = new JTextField(20);
        this.pumpenbetrieb = new JTextField(20);
        this.sonstiges = new JTextField(20);
        this.datumVon = new JTextField(20);
        this.datumBis = new JTextField(20);
        this.zeitVon = new JTextField(20);
        this.zeitBis = new JTextField(20);
        this.fahrzeugName_label = new JLabel("Fahrzeug Name: ");
        this.kmBeginn_label = new JLabel("Kilometerstand Fahrtbeginn: ");
        this.kmEnde_label = new JLabel("Kilometerstand Fahrtende: ");
        this.tanken_lanel = new JLabel("Tanken (Liter): ");
        this.pumpenbetrieb_label = new JLabel("PumpenBetrieb (Std.): ");
        this.sonstiges_label = new JLabel("Sonstiges / Bemerkung / Kommentar: ");
        this.fahrer_label = new JLabel("Fahrer: ");
        this.veranstaltung_label = new JLabel("Veranstaltung");
        this.datumVon_label = new JLabel("Datum Von: ");
        this.datumBis_label = new JLabel("Datum Bis: ");
        this.zeitVon_label = new JLabel("Zeit Von: ");
        this.zeitBis_label = new JLabel("Zeit Bis: ");
        this.modulBeschreibung = new JLabel("Fahrtenbuch");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        try {
            String[] listeFahrzeuge = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
            String[] listeMitglieder = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getAlleMitF\u00fchrerscheinDerGruppe1());
            String[] listeVeranstaltung = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltung());
            this.fahrzeugName = new JComboBox<String>(listeFahrzeuge);
            this.fahrer = new JComboBox<String>(listeMitglieder);
            this.veranstaltung = new JComboBox<String>(listeVeranstaltung);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                try {
                    int vID = tabVeranstaltung.getVeranstaltungID(FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString());
                    if (FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        FahrtenbuchAO.this.datumVon.setText(null);
                        FahrtenbuchAO.this.zeitVon.setText(null);
                        FahrtenbuchAO.this.datumBis.setText(null);
                        FahrtenbuchAO.this.zeitBis.setText(null);
                        FahrtenbuchAO.this.datumVon.setEditable(true);
                        FahrtenbuchAO.this.zeitVon.setEditable(true);
                        FahrtenbuchAO.this.datumBis.setEditable(true);
                        FahrtenbuchAO.this.zeitBis.setEditable(true);
                    } else {
                        FahrtenbuchAO.this.datumVon.setText(TimeCalculation.parseDateForGUI(tabVeranstaltung.getDatum(vID)));
                        FahrtenbuchAO.this.zeitVon.setText(tabVeranstaltung.getZeitStart(vID));
                        FahrtenbuchAO.this.datumBis.setText(TimeCalculation.parseDateForGUI(tabVeranstaltung.getDatum(vID)));
                        FahrtenbuchAO.this.zeitBis.setText(tabVeranstaltung.getZeitEnde(vID));
                        FahrtenbuchAO.this.datumVon.setEditable(false);
                        FahrtenbuchAO.this.zeitVon.setEditable(false);
                        FahrtenbuchAO.this.datumBis.setEditable(false);
                        FahrtenbuchAO.this.zeitBis.setEditable(false);
                    }
                    FahrtenbuchAO.this.fahrzeugName.setSelectedItem("<bitte w\u00e4hlen>");
                    FahrtenbuchAO.this.fahrer.setSelectedItem("<bitte w\u00e4hlen>");
                    FahrtenbuchAO.this.tanken.setText(null);
                    FahrtenbuchAO.this.kmBeginn.setText(null);
                    FahrtenbuchAO.this.kmEnde.setText(null);
                    FahrtenbuchAO.this.pumpenbetrieb.setText(null);
                    FahrtenbuchAO.this.sonstiges.setText(null);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.fahrzeugName.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleFahrtenbuch tabFahrtenbuch = new TabelleFahrtenbuch();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                try {
                    int fID = tabFahrzeug.getFahrzeugID(FahrtenbuchAO.this.fahrzeugName.getSelectedItem().toString());
                    int vID = tabVeranstaltung.getVeranstaltungID(FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString());
                    int last = tabFahrtenbuch.getLastEntryOfVehicle(fID);
                    if (tabFahrtenbuch.getCountVeranstaltungID(vID, fID) != 0) {
                        HashMap<String, String> map = tabFahrtenbuch.getData(vID, fID);
                        FahrtenbuchAO.this.fahrer.setSelectedItem(map.get("fahrer"));
                        FahrtenbuchAO.this.kmBeginn.setText(map.get("kmBeginn"));
                        FahrtenbuchAO.this.kmEnde.setText(map.get("kmEnde"));
                        FahrtenbuchAO.this.tanken.setText(map.get("tanken"));
                        FahrtenbuchAO.this.pumpenbetrieb.setText(map.get("pumpenbetrieb"));
                        FahrtenbuchAO.this.sonstiges.setText(map.get("sonstiges"));
                    } else if (last != 0) {
                        FahrtenbuchAO.this.kmBeginn.setText(Integer.toString(tabFahrtenbuch.getLastKmStand(last)));
                        FahrtenbuchAO.this.kmBeginn.setEditable(false);
                        FahrtenbuchAO.this.kmEnde.setText(null);
                        FahrtenbuchAO.this.fahrer.setSelectedItem("<bitte w\u00e4hlen>");
                    } else {
                        FahrtenbuchAO.this.kmBeginn.setText(null);
                        FahrtenbuchAO.this.kmBeginn.setEditable(true);
                        FahrtenbuchAO.this.kmEnde.setText(null);
                        FahrtenbuchAO.this.fahrer.setSelectedItem("<bitte w\u00e4hlen>");
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
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(700, 470);
        this.setTitle("FeuerwehrManagementSystem - Fahrtenbuch");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelFahrtenbuch = new JPanel(new GridLayout(12, 2));
        this.getContentPane().add("Center", this.panelFahrtenbuch);
        this.panelFahrtenbuch.add(this.veranstaltung_label);
        this.panelFahrtenbuch.add(this.veranstaltung);
        this.panelFahrtenbuch.add(this.fahrzeugName_label);
        this.panelFahrtenbuch.add(this.fahrzeugName);
        this.panelFahrtenbuch.add(this.fahrer_label);
        this.panelFahrtenbuch.add(this.fahrer);
        this.panelFahrtenbuch.add(this.datumVon_label);
        this.panelFahrtenbuch.add(this.datumVon);
        this.panelFahrtenbuch.add(this.datumBis_label);
        this.panelFahrtenbuch.add(this.datumBis);
        this.panelFahrtenbuch.add(this.zeitVon_label);
        this.panelFahrtenbuch.add(this.zeitVon);
        this.panelFahrtenbuch.add(this.zeitBis_label);
        this.panelFahrtenbuch.add(this.zeitBis);
        this.panelFahrtenbuch.add(this.kmBeginn_label);
        this.panelFahrtenbuch.add(this.kmBeginn);
        this.panelFahrtenbuch.add(this.kmEnde_label);
        this.panelFahrtenbuch.add(this.kmEnde);
        this.panelFahrtenbuch.add(this.tanken_lanel);
        this.panelFahrtenbuch.add(this.tanken);
        this.panelFahrtenbuch.add(this.pumpenbetrieb_label);
        this.panelFahrtenbuch.add(this.pumpenbetrieb);
        this.panelFahrtenbuch.add(this.sonstiges_label);
        this.panelFahrtenbuch.add(this.sonstiges);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        if (MyEvent.event.equals("0x0351")) {
            this.veranstaltung.setSelectedItem(runApplication.letzterVeranstaltungsname);
            this.veranstaltung.setEnabled(false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleFahrtenbuch tabFahrtenbuch = new TabelleFahrtenbuch();
                Fahrtenbuch fahrtenbuch = new Fahrtenbuch();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                try {
                    if (FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                    } else if (Integer.parseInt(FahrtenbuchAO.this.kmEnde.getText()) <= Integer.parseInt(FahrtenbuchAO.this.kmBeginn.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.FAHRTENBUCH_KM_STAND_FEHLERHAFT, "Warnung", 2);
                    } else if (FahrtenbuchAO.this.fahrzeugName.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_FAHRZEUG_WAEHLEN, "Warnung", 2);
                    } else if (FahrtenbuchAO.this.fahrer.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                    } else if (!TimeCalculation.checkDateFormat(FahrtenbuchAO.this.datumVon.getText())) {
                        FahrtenbuchAO.this.datumVon.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 0);
                    } else if (!TimeCalculation.checkDateFormat(FahrtenbuchAO.this.datumBis.getText())) {
                        FahrtenbuchAO.this.datumBis.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 0);
                    } else if (!TimeCalculation.checkTimeFormat(FahrtenbuchAO.this.zeitVon.getText())) {
                        FahrtenbuchAO.this.zeitVon.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 0);
                    } else if (!TimeCalculation.checkTimeFormat(FahrtenbuchAO.this.zeitBis.getText())) {
                        FahrtenbuchAO.this.zeitBis.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 0);
                    } else {
                        int vID = tabVeranstaltung.getVeranstaltungID(FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString());
                        int fID = tabFahrzeug.getFahrzeugID(FahrtenbuchAO.this.fahrzeugName.getSelectedItem().toString());
                        FahrtenbuchAO.this.datumVon.setBackground(Color.white);
                        FahrtenbuchAO.this.datumBis.setBackground(Color.white);
                        FahrtenbuchAO.this.zeitVon.setBackground(Color.white);
                        FahrtenbuchAO.this.zeitBis.setBackground(Color.white);
                        fahrtenbuch.setId(tabFahrtenbuch.getNextNummer());
                        fahrtenbuch.setFahrzeugID(fID);
                        fahrtenbuch.setVeranstaltungID(vID);
                        fahrtenbuch.setDatumVon(TimeCalculation.parseDateForDatabase(FahrtenbuchAO.this.datumVon.getText()));
                        fahrtenbuch.setDatumBis(TimeCalculation.parseDateForDatabase(FahrtenbuchAO.this.datumBis.getText()));
                        fahrtenbuch.setZeitVon(FahrtenbuchAO.this.zeitVon.getText());
                        fahrtenbuch.setZeitBis(FahrtenbuchAO.this.zeitBis.getText());
                        fahrtenbuch.setKmBeginn(Integer.parseInt(FahrtenbuchAO.this.kmBeginn.getText()));
                        fahrtenbuch.setKmEnde(Integer.parseInt(FahrtenbuchAO.this.kmEnde.getText()));
                        fahrtenbuch.setDistance(Integer.parseInt(FahrtenbuchAO.this.kmEnde.getText()) - Integer.parseInt(FahrtenbuchAO.this.kmBeginn.getText()));
                        fahrtenbuch.setTanken(FahrtenbuchAO.this.tanken.getText());
                        fahrtenbuch.setPumpenbetrieb(FahrtenbuchAO.this.pumpenbetrieb.getText());
                        fahrtenbuch.setSonstiges(FahrtenbuchAO.this.sonstiges.getText());
                        fahrtenbuch.setFahrer(tabMitglied.getIdByGuiString(FahrtenbuchAO.this.fahrer.getSelectedItem().toString()));
                        if (tabFahrtenbuch.getCountVeranstaltungID(vID, fID) != 0) {
                            tabFahrtenbuch.update(fahrtenbuch);
                            logbuchEingabe.NeuerEintag("Fahrtenbucheintrag wurde editiert f\u00fcr: " + FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString());
                        } else {
                            tabFahrtenbuch.insert(fahrtenbuch);
                            logbuchEingabe.NeuerEintag("Fahrtenbucheintrag wurde erstellt f\u00fcr: " + FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString());
                        }
                        logbuchEingabe.NeuerEintag("Fahrtenbucheintrag wurde erstellt f\u00fcr: " + FahrtenbuchAO.this.veranstaltung.getSelectedItem().toString());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (!MyEvent.event.equals("0x0351")) {
                            FahrtenbuchAO.this.veranstaltung.setSelectedItem("<bitte w\u00e4hlen>");
                        }
                        FahrtenbuchAO.this.fahrzeugName.setSelectedItem("<bitte w\u00e4hlen>");
                        FahrtenbuchAO.this.fahrer.setSelectedItem("<bitte w\u00e4hlen>");
                        FahrtenbuchAO.this.datumVon.setText(null);
                        FahrtenbuchAO.this.zeitVon.setText(null);
                        FahrtenbuchAO.this.datumBis.setText(null);
                        FahrtenbuchAO.this.zeitBis.setText(null);
                        FahrtenbuchAO.this.tanken.setText(null);
                        FahrtenbuchAO.this.kmBeginn.setText(null);
                        FahrtenbuchAO.this.kmEnde.setText(null);
                        FahrtenbuchAO.this.pumpenbetrieb.setText(null);
                        FahrtenbuchAO.this.sonstiges.setText(null);
                        FahrtenbuchAO.this.datumVon.setEditable(true);
                        FahrtenbuchAO.this.zeitVon.setEditable(true);
                        FahrtenbuchAO.this.datumBis.setEditable(true);
                        FahrtenbuchAO.this.zeitBis.setEditable(true);
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

