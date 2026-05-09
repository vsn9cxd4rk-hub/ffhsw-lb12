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
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleAusbildung;
import data.tabellen.TabelleAusbildung_Kategorie;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Ausbildung;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class AusbildungsInhaltEintragenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAusbildungsKategorie;
    private JLabel veranstaltung_label;
    private JLabel ausbildungskategorie_label;
    private JComboBox<String> veranstaltung;
    public static JComboBox<String> ausbildungskategorie;
    private JCheckBox[] jCheckboxArray;
    public static StringBuilder abwesendePersonen;
    public static String vorhergehendesEvent;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;

    public AusbildungsInhaltEintragenAO() {
        super("FeuerwehrManagementSystem - Ausbildungsinhalt");
        logging.logInfo((Object)"Starte: AusbildungsinhaltEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAusbildungsKategorie = new JButton("Neue Kategorie anlegen");
        this.modulBeschreibung = new JLabel("Ausbildungsinhalt Eintragen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.veranstaltung_label = new JLabel("Veranstaltung: ");
        this.ausbildungskategorie_label = new JLabel("Ausbildungsinhalt: ");
        String[] liste = null;
        String[] listeAusbludung = null;
        TabelleVeranstaltung veranstaltungListe = new TabelleVeranstaltung();
        TabelleAusbildung_Kategorie tabAusbildungKategorie = new TabelleAusbildung_Kategorie();
        try {
            liste = Utils.listToArrayOnlyFORComboBoxes(veranstaltungListe.getAllVeranstaltungEinerKategorieByJahr(2, Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"))));
            listeAusbludung = Utils.listToArrayOnlyFORComboBoxes(tabAusbildungKategorie.getAllKategorien());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.veranstaltung = new JComboBox<String>(liste);
        ausbildungskategorie = new JComboBox<String>(listeAusbludung);
    }

    protected void labelErstellen() {
        this.veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
                TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                TabelleAusbildung tabAusbildung = new TabelleAusbildung();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
                TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
                try {
                    int selectedVID = tabVeransatltung.getVeranstaltungID(AusbildungsInhaltEintragenAO.this.veranstaltung.getSelectedItem().toString());
                    int i = 0;
                    while (i < AusbildungsInhaltEintragenAO.this.jCheckboxArray.length) {
                        int komma = AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().indexOf(",");
                        String isSelectedName = AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().substring(0, komma);
                        String isSelectedVorname = AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().substring(komma + 2, AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().length());
                        int mID = tabMitglied.getId(isSelectedName, isSelectedVorname);
                        AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setBackground(null);
                        AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setEnabled(true);
                        AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setSelected(false);
                        if (tabAnwesenheit.getAnwesendStatus(mID, selectedVID) == 1) {
                            if (tabAusbildung.getStatusFromDatabase(mID, selectedVID, Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"))) == 1) {
                                AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setBackground(Color.blue);
                                AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setSelected(true);
                                AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setEnabled(false);
                            }
                        } else {
                            AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setSelected(false);
                            AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setEnabled(false);
                            AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setBackground(Color.red);
                        }
                        if (tabPlan.getCountVeranstaltungID(selectedVID) == 1) {
                            ausbildungskategorie.setSelectedItem(tabKategorie.getNameByID(tabPlan.getAusbildungKategorie(selectedVID)));
                        } else {
                            ausbildungskategorie.setSelectedItem("<bitte w\u00e4hlen>");
                        }
                        ++i;
                    }
                }
                catch (NullPointerException | SQLException e) {
                    logging.logInfo((Object)e);
                }
            }
        });
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
        this.veranstaltung.setSelectedItem(runApplication.letzterVeranstaltungsname);
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        TabelleMitglied mitglieder = new TabelleMitglied();
        try {
            int GUI_laenge = mitglieder.getMitgliederCountGruppe1() / 4 * 23;
            this.setSize(620, 270 + GUI_laenge);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.setTitle("FeuerwehrManagementSystem - AusbildungsInhalt Eintagen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.veranstaltung_label);
        this.add(this.veranstaltung);
        this.add(this.dummy3);
        this.add(this.ausbildungskategorie_label);
        this.add(ausbildungskategorie);
        this.add(this.buttonAusbildungsKategorie);
        try {
            TabelleMitglied tabMitglieder = new TabelleMitglied();
            String[] labels = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
            JPanel panel = new JPanel(new GridLayout(0, 4));
            int CheckBoxNumber = tabMitglieder.getMitgliederCountGruppe1();
            this.jCheckboxArray = new JCheckBox[CheckBoxNumber];
            int x = 0;
            while (x < CheckBoxNumber) {
                this.jCheckboxArray[x] = new JCheckBox();
                this.jCheckboxArray[x].setText(labels[x]);
                this.jCheckboxArray[x].setName(labels[x]);
                panel.add(this.jCheckboxArray[x]);
                logging.logInfo((Object)("F\u00fcge Mitglied: " + labels[x] + " hinzu...."));
                ++x;
            }
            this.add(panel, "Center");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
                TabelleAusbildung tabAusbildung = new TabelleAusbildung();
                TabelleMitglied tabMitglieder = new TabelleMitglied();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
                Ausbildung ausbildung = new Ausbildung();
                if (ausbildungskategorie.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    logging.logInfo((Object)"Es wurde keine Kategorie ge\u00e4hlet");
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_KATEGORIE_WAEHLEN, "Warnung", 2);
                } else if (AusbildungsInhaltEintragenAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    logging.logInfo((Object)"Es wurde Keine Veranstaltung gew\u00e4hlt");
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                } else {
                    try {
                        int vID = tabVeranstaltung.getVeranstaltungID(AusbildungsInhaltEintragenAO.this.veranstaltung.getSelectedItem().toString());
                        int jahr = tabVeranstaltung.getJahrDerVeranstaltung(vID);
                        int aKat = tabKategorie.getID(ausbildungskategorie.getSelectedItem().toString());
                        int i = 0;
                        while (i < AusbildungsInhaltEintragenAO.this.jCheckboxArray.length) {
                            if (AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].isSelected() && AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].isEnabled()) {
                                int komma = AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().indexOf(",");
                                String isSelectedName = AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().substring(0, komma);
                                String isSelectedVorname = AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().substring(komma + 2, AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].getName().toString().length());
                                ausbildung.setId(tabAusbildung.getNextNummer());
                                ausbildung.setJahr(jahr);
                                ausbildung.setVeranstaltungID(vID);
                                ausbildung.setAusbildungKategorieID(aKat);
                                ausbildung.setMitgliederID(tabMitglieder.getId(isSelectedName, isSelectedVorname));
                                tabAusbildung.insert(ausbildung);
                                AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setBackground(Color.blue);
                                AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setSelected(true);
                                AusbildungsInhaltEintragenAO.this.jCheckboxArray[i].setEnabled(false);
                            }
                            ++i;
                        }
                        logging.logInfo((Object)"Ausbildungsinformationen wurden erfolgriecht gespeichert");
                        logbuchEingabe.NeuerEintag("Ausbildungsinhate wurden eingetragen: " + AusbildungsInhaltEintragenAO.this.veranstaltung.getSelectedItem().toString() + " (" + ausbildungskategorie.getSelectedItem().toString() + ")");
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                    catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonAusbildungsKategorie.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0023");
                Steuerung.setStatus(Status.AUSBILDUNG_KATEGORIE);
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

