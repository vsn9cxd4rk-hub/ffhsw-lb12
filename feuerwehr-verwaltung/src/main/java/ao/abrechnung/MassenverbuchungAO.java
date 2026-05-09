/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.abrechnung;

import ao.AbstractFenster;
import ao.abrechnung.AbrechnungAO;
import data.tabellen.abrechnung.TabelleAbrechnung;
import data.tabellen.abrechnung.TabelleAbrechnung_artikel;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import go.abrechnung.Abrechnung;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MoneyCalculation;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class MassenverbuchungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonSpeichern;
    private JButton buttonZurueck;
    private JCheckBox[] jCheckboxArray;
    private JButton[] buttonSelektiereBenutzerGruppen;
    private JButton buttonSelektiereAlle;
    private JButton buttonDeSelektiereAlle;
    private JScrollPane paneJCheckBoxArray;
    private JLabel artikel_label;
    private JComboBox<String> artikel;
    private JLabel betrag_label;
    private JTextField wert;
    private JLabel menge_label;
    private JComboBox<String> menge;
    private JLabel modulBeschreibung;
    private JLabel beschreibung2;
    private JLabel beschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JLabel dummy4;
    private JPanel panelArtikel;

    public MassenverbuchungAO() {
        super("FeuerwehrManagementSystem - MassenverbuchungAO");
        logging.logInfo((Object)"Starte: MassenverbuchungAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Verbuchen");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSelektiereAlle = new JButton("Alle");
        this.buttonDeSelektiereAlle = new JButton("Entferne Alle");
        this.beschreibung = new JLabel("W\u00e4hlen Sie Bitte die Mitglieder aus, f\u00fcr die der Artikel verbucht werden soll: ");
        this.beschreibung2 = new JLabel("Artikelauswahl: ");
        this.modulBeschreibung = new JLabel("Massenverbuchung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.dummy4 = new JLabel(runApplication.dummyImage);
        this.artikel_label = new JLabel("Artikel: ");
        this.betrag_label = new JLabel("Betrag: ");
        this.menge_label = new JLabel("Menge: ");
        this.wert = new JTextField();
    }

    protected void labelErstellen() {
        TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
        try {
            String[] artikelListe = Utils.listToArrayOnlyFORComboBoxes(tabArtikel.getAllArtikel());
            this.artikel = new JComboBox<String>(artikelListe);
            String[] mengenListe = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "40", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
            this.menge = new JComboBox<String>(mengenListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.artikel.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                try {
                    int artID = tabArtikel.getArtikelID(MassenverbuchungAO.this.artikel.getSelectedItem().toString());
                    if (tabArtikel.getArtikelBerechnungsart(artID) == 2) {
                        MassenverbuchungAO.this.wert.setText(MoneyCalculation.parseMoneyVauleForGUI(tabArtikel.getArtikelWert(artID)));
                        MassenverbuchungAO.this.wert.setEditable(false);
                    } else {
                        MassenverbuchungAO.this.wert.setText(null);
                        MassenverbuchungAO.this.wert.setEditable(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.menge.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                try {
                    int artID = tabArtikel.getArtikelID(MassenverbuchungAO.this.artikel.getSelectedItem().toString());
                    int ergebnis = Integer.parseInt(MassenverbuchungAO.this.menge.getSelectedItem().toString()) * tabArtikel.getArtikelWert(artID);
                    System.out.println(ergebnis);
                    MassenverbuchungAO.this.wert.setText(null);
                    MassenverbuchungAO.this.wert.setText(MoneyCalculation.parseMoneyVauleForGUI(ergebnis));
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
        this.setSize(750, 768);
        this.setTitle("FeuerwehrManagementSystem - Massenverbuchung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.beschreibung);
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        this.paneJCheckBoxArray = new JScrollPane(panel);
        this.paneJCheckBoxArray.setVerticalScrollBarPolicy(22);
        this.paneJCheckBoxArray.setPreferredSize(new Dimension(300, 450));
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        TitledBorder rahmen2 = BorderFactory.createTitledBorder(lowerEtched, "Mitglieder Liste");
        this.paneJCheckBoxArray.setBorder(rahmen2);
        try {
            String[] mitgliederName = Utils.listToArray(tabMitglied.getAllMitgliederFromDataBase());
            int[] mitgleiderGruppenID = Utils.listToIntArray(tabMitglied.getAllMitgliederGruppenFromDataBaseByMitglied());
            int letzteMitgleiderGruppe = 0;
            String[] buttonLabels = Utils.listToArray(tabGruppe.getAllGruppen());
            this.jCheckboxArray = new JCheckBox[tabMitglied.getAllMitgliederCount()];
            this.buttonSelektiereBenutzerGruppen = new JButton[tabGruppe.count()];
            panel.add(this.buttonSelektiereAlle);
            panel.add(this.buttonDeSelektiereAlle);
            int g = 0;
            while (g < buttonLabels.length) {
                this.buttonSelektiereBenutzerGruppen[g] = new JButton(buttonLabels[g]);
                this.buttonSelektiereBenutzerGruppen[g].setName(buttonLabels[g]);
                panel.add(this.buttonSelektiereBenutzerGruppen[g]);
                this.buttonSelektiereBenutzerGruppen[g].addActionListener(this.createActionListener(tabGruppe.getID(buttonLabels[g])));
                ++g;
            }
            int x = 0;
            while (x < mitgliederName.length) {
                if (letzteMitgleiderGruppe != mitgleiderGruppenID[x]) {
                    JLabel trennung1 = new JLabel();
                    JLabel nameMitgliedergruppe = new JLabel(String.valueOf(tabGruppe.getGruppenName(mitgleiderGruppenID[x])) + ":");
                    panel.add(trennung1);
                    panel.add(nameMitgliedergruppe);
                }
                this.jCheckboxArray[x] = new JCheckBox();
                this.jCheckboxArray[x].setText(mitgliederName[x]);
                this.jCheckboxArray[x].setName(Integer.toString(mitgleiderGruppenID[x]));
                letzteMitgleiderGruppe = mitgleiderGruppenID[x];
                panel.add(this.jCheckboxArray[x]);
                ++x;
            }
            this.add(this.paneJCheckBoxArray);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.dummy3);
        this.add(this.beschreibung2);
        this.add(this.dummy4);
        this.panelArtikel = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panelArtikel);
        this.panelArtikel.add(this.artikel_label);
        this.panelArtikel.add(this.artikel);
        this.panelArtikel.add(this.menge_label);
        this.panelArtikel.add(this.menge);
        this.panelArtikel.add(this.betrag_label);
        this.panelArtikel.add(this.wert);
        this.add(this.dummy2);
        this.add(this.buttonSpeichern);
        this.add(this.buttonZurueck);
        this.buttonSelektiereAlle.setBackground(Color.cyan);
        this.buttonDeSelektiereAlle.setBackground(Color.cyan);
    }

    protected void boxenHinzufuegen() {
    }

    private ActionListener createActionListener(final int index) {
        ActionListener action = new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    logging.logInfo((Object)("Selektiere MitgliederGruppe: " + index));
                    int mCount = tabMitglied.getAllMitgliederCount();
                    int i = 0;
                    while (i < mCount) {
                        if (index == -1) {
                            MassenverbuchungAO.this.jCheckboxArray[i].setSelected(true);
                        } else if (index == -2) {
                            MassenverbuchungAO.this.jCheckboxArray[i].setSelected(false);
                        } else if (Integer.parseInt(MassenverbuchungAO.this.jCheckboxArray[i].getName()) == index) {
                            MassenverbuchungAO.this.jCheckboxArray[i].setSelected(true);
                        }
                        ++i;
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        return action;
    }

    protected void actionErzeugen() {
        this.buttonSelektiereAlle.addActionListener(this.createActionListener(-1));
        this.buttonDeSelektiereAlle.addActionListener(this.createActionListener(-2));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                block14: {
                    TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
                    TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                    Abrechnung abrechnung = new Abrechnung();
                    try {
                        if (MassenverbuchungAO.this.artikel.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN, "Warnung", 2);
                            break block14;
                        }
                        if (MassenverbuchungAO.this.wert.getText().equals("") || MassenverbuchungAO.this.wert.getText().toString().matches("',*'")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_BETRAG_KONTROLLIEREN, "Warnung", 2);
                            break block14;
                        }
                        int[] mitgliederIDListe = Utils.listToIntArray(new TabelleMitglied().getAllMitgliederNummernFromDataBase());
                        int m = 0;
                        while (m < MassenverbuchungAO.this.jCheckboxArray.length) {
                            if (MassenverbuchungAO.this.jCheckboxArray[m].isSelected()) {
                                int artID = tabArtikel.getArtikelID(MassenverbuchungAO.this.artikel.getSelectedItem().toString());
                                abrechnung.setId(tabAbrechnung.getNextNummer());
                                abrechnung.setAbrechnungID(0);
                                abrechnung.setArtikelID(artID);
                                abrechnung.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                                abrechnung.setJahr(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
                                abrechnung.setMitgliedID(mitgliederIDListe[m]);
                                abrechnung.setStatus(0);
                                abrechnung.setVeranstaltungID(0);
                                abrechnung.setVeranstaltungKategorie(tabArtikel.getArtikelKlasse(artID));
                                abrechnung.setMenge(Integer.parseInt(MassenverbuchungAO.this.menge.getSelectedItem().toString()));
                                abrechnung.setWert(MoneyCalculation.parseMoneyVauleForDatabase(MassenverbuchungAO.this.wert.getText()));
                                abrechnung.setZahlungsart(tabArtikel.getZahlungsart(artID));
                                abrechnung.setBuchungskonto(tabArtikel.getBuchungskontoID(artID));
                                tabAbrechnung.insert(abrechnung);
                                logbuchEingabe.NeuerEintag("Es wurde Verbucht f\u00fcr: " + abrechnung.getMitgliedID());
                            }
                            ++m;
                        }
                        MassenverbuchungAO.this.menge.setSelectedItem("1");
                        MassenverbuchungAO.this.artikel.setSelectedItem("<bitte w\u00e4hlen>");
                        MassenverbuchungAO.this.wert.setText(null);
                        MassenverbuchungAO.this.wert.setEnabled(true);
                        int i = 0;
                        while (i < MassenverbuchungAO.this.jCheckboxArray.length) {
                            MassenverbuchungAO.this.jCheckboxArray[i].setSelected(false);
                            ++i;
                        }
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        try {
                            Object[] auswahl = AbrechnungAO.tree.getSelectionPath().getPath();
                            logging.logInfo((Object)("Lade Abrechnungs GUI neu mit: " + AbrechnungAO.tree.getSelectionPath().getPath()));
                            if (auswahl[1].toString().equals("Konten")) {
                                ((DefaultTableModel)AbrechnungAO.table.getModel()).setDataVector(tabAbrechnung.getAllAbrechnungenByKonto(AbrechnungAO.tree.getSelectionPath().getLastPathComponent().toString()), AbrechnungAO.headnameKonto);
                                AbrechnungAO.IDLISTE = Utils.listToIntArray(tabAbrechnung.getIDArrayKonto(AbrechnungAO.tree.getSelectionPath().getLastPathComponent().toString()));
                            } else if (auswahl[1].toString().equals("Abrechnungen")) {
                                ((DefaultTableModel)AbrechnungAO.table.getModel()).setDataVector(tabAbrechnung.getAllAbrechnungenByAbrechnung(Integer.parseInt(AbrechnungAO.tree.getSelectionPath().getLastPathComponent().toString())), AbrechnungAO.headnameAbrechnung);
                                AbrechnungAO.buttonAbrechnen.setVisible(false);
                            } else if (auswahl[1].toString().equals("Mitglieder")) {
                                int mID = Integer.parseInt(AbrechnungAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                                ((DefaultTableModel)AbrechnungAO.table.getModel()).setDataVector(new TabelleAbrechnung().getAllAbrechnungenByMitglied(mID), AbrechnungAO.headname);
                                AbrechnungAO.IDLISTE = Utils.listToIntArray(tabAbrechnung.getIDArrayMitglieder(mID));
                            }
                        }
                        catch (NullPointerException e) {
                            logging.logWarning((Object)"Im Tree ist nicht Selektiert, kann keine GUI neu laden...");
                        }
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonZurueck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MassenverbuchungAO.this.dispose();
            }
        });
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        MyEvent.setEvent((String)"0x0030");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

