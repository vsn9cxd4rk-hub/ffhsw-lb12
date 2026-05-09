/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.abrechnung;

import ao.AbstractFenster;
import ao.abrechnung.AbrechnungAO;
import data.tabellen.abrechnung.TabelleAbrechnung;
import data.tabellen.abrechnung.TabelleAbrechnung_artikel;
import data.tabellen.abrechnung.TabelleAbrechnung_konto;
import data.tabellen.mitglied.TabelleMitglied;
import go.abrechnung.Abrechnung;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MoneyCalculation;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class ManuelleVerbuchungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JComboBox<String> mitglieder;
    private JComboBox<String> konto;
    private JComboBox<String> artikel;
    private JTextField wert;
    private JComboBox<String> menge;
    private JLabel mitglieder_label;
    private JLabel konto_label;
    private JLabel artikel_label;
    private JLabel wert_label;
    private JLabel menge_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;

    public ManuelleVerbuchungAO() {
        super("FeuerwehrManagementSystem - Manuelle Verbuchung");
        logging.logInfo((Object)"Starte: ManuelleVerbuchungAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Verbuchen");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Manuelle Verbuchung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.konto_label = new JLabel("Konten: ");
        this.mitglieder_label = new JLabel("Mitglieder Name: ");
        this.artikel_label = new JLabel("Artikel: ");
        this.wert_label = new JLabel("Wert in \u20ac: ");
        this.menge_label = new JLabel("Menge: ");
        String[] mengenListe = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "40", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60"};
        this.menge = new JComboBox<String>(mengenListe);
        this.wert = new JTextField(20);
        try {
            TabelleMitglied mitglied = new TabelleMitglied();
            TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
            TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(mitglied.getAllMitgliederFromDataBase());
            String[] artikelL = Utils.listToArrayOnlyFORComboBoxes(tabArtikel.getAllArtikelGroe\u00dfer100());
            String[] kontoListe = Utils.listToArrayOnlyFORComboBoxes(tabKonto.getAllKontos());
            this.mitglieder = new JComboBox<String>(mitgliederListe);
            this.artikel = new JComboBox<String>(artikelL);
            this.konto = new JComboBox<String>(kontoListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelErstellen() {
        this.artikel.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                try {
                    int artID = tabArtikel.getArtikelID(ManuelleVerbuchungAO.this.artikel.getSelectedItem().toString());
                    if (tabArtikel.getArtikelBerechnungsart(artID) == 2) {
                        ManuelleVerbuchungAO.this.wert.setText(MoneyCalculation.parseMoneyVauleForGUI(tabArtikel.getArtikelWert(artID)));
                        ManuelleVerbuchungAO.this.wert.setEditable(false);
                    } else {
                        ManuelleVerbuchungAO.this.wert.setText(null);
                        ManuelleVerbuchungAO.this.wert.setEditable(true);
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
                    int artID = tabArtikel.getArtikelID(ManuelleVerbuchungAO.this.artikel.getSelectedItem().toString());
                    int ergebnis = Integer.parseInt(ManuelleVerbuchungAO.this.menge.getSelectedItem().toString()) * tabArtikel.getArtikelWert(artID);
                    System.out.println(ergebnis);
                    ManuelleVerbuchungAO.this.wert.setText(null);
                    ManuelleVerbuchungAO.this.wert.setText(MoneyCalculation.parseMoneyVauleForGUI(ergebnis));
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
        this.setSize(750, 280);
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Au\u00dfer Dienst stellen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(5, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.konto_label);
        this.panel.add(this.konto);
        this.panel.add(this.mitglieder_label);
        this.panel.add(this.mitglieder);
        this.panel.add(this.artikel_label);
        this.panel.add(this.artikel);
        this.panel.add(this.menge_label);
        this.panel.add(this.menge);
        this.panel.add(this.wert_label);
        this.panel.add(this.wert);
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
                block16: {
                    TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
                    TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
                    Abrechnung abrechnung = new Abrechnung();
                    try {
                        if (ManuelleVerbuchungAO.this.mitglieder.getSelectedItem().equals("<bitte w\u00e4hlen>") && ManuelleVerbuchungAO.this.konto.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_ODER_KONTO_WAEHLEN, "Warnung", 2);
                            break block16;
                        }
                        if (!ManuelleVerbuchungAO.this.mitglieder.getSelectedItem().equals("<bitte w\u00e4hlen>") && !ManuelleVerbuchungAO.this.konto.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_MITGLIED_ODER_KONTO_WAEHLEN, "Warnung", 2);
                            break block16;
                        }
                        if (ManuelleVerbuchungAO.this.artikel.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_ARTIKEL_WAEHLEN, "Warnung", 2);
                            break block16;
                        }
                        if (ManuelleVerbuchungAO.this.wert.getText().equals("") || ManuelleVerbuchungAO.this.wert.getText().toString().matches("',*'")) {
                            JOptionPane.showMessageDialog(null, Konstante.BITTE_BETRAG_KONTROLLIEREN, "Warnung", 2);
                            break block16;
                        }
                        int artID = tabArtikel.getArtikelID(ManuelleVerbuchungAO.this.artikel.getSelectedItem().toString());
                        abrechnung.setId(tabAbrechnung.getNextNummer());
                        abrechnung.setAbrechnungID(0);
                        abrechnung.setArtikelID(artID);
                        abrechnung.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                        abrechnung.setJahr(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
                        if (!ManuelleVerbuchungAO.this.konto.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                            abrechnung.setMitgliedID(tabKonto.getID(ManuelleVerbuchungAO.this.konto.getSelectedItem().toString()));
                        } else if (!ManuelleVerbuchungAO.this.mitglieder.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                            abrechnung.setMitgliedID(tabMitglied.getIdByGuiString(ManuelleVerbuchungAO.this.mitglieder.getSelectedItem().toString()));
                        }
                        abrechnung.setStatus(0);
                        abrechnung.setVeranstaltungID(0);
                        abrechnung.setVeranstaltungKategorie(tabArtikel.getArtikelKlasse(artID));
                        abrechnung.setMenge(Integer.parseInt(ManuelleVerbuchungAO.this.menge.getSelectedItem().toString()));
                        abrechnung.setWert(MoneyCalculation.parseMoneyVauleForDatabase(ManuelleVerbuchungAO.this.wert.getText()));
                        abrechnung.setZahlungsart(tabArtikel.getZahlungsart(artID));
                        abrechnung.setBuchungskonto(tabArtikel.getBuchungskontoID(artID));
                        tabAbrechnung.insert(abrechnung);
                        logbuchEingabe.NeuerEintag("Es wurde manuell Verbucht f\u00fcr: " + abrechnung.getMitgliedID());
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
                        ManuelleVerbuchungAO.this.mitglieder.setSelectedItem("<bitte w\u00e4hlen>");
                        ManuelleVerbuchungAO.this.konto.setSelectedItem("<bitte w\u00e4hlen>");
                        ManuelleVerbuchungAO.this.menge.setSelectedItem("1");
                        ManuelleVerbuchungAO.this.artikel.setSelectedItem("<bitte w\u00e4hlen>");
                        ManuelleVerbuchungAO.this.wert.setText(null);
                        ManuelleVerbuchungAO.this.wert.setEnabled(true);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
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

