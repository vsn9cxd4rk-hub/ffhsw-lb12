/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.urlaubsplaner;

import ao.AbstractFenster;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.urlaubsplaner.TabelleUrlaub;
import go.urlaub.Urlaub;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.logbuchEingabe;

public class UrlaubsplanerAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonLoeschen;
    private JComboBox<String> mitgliederListe;
    private JComboBox<String> urlaubsliste;
    private JTextField datumVon;
    private JTextField datumBis;
    private JLabel datumVon_label;
    private JLabel datumBis_label;
    private JLabel mitgliederListe_label;
    private JPanel panel;
    private JPanel panelLoeschen;
    private JLabel modulBeschreibung;
    private JLabel beschreibungLoeschen;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JLabel dummy4;

    public UrlaubsplanerAO() {
        super("FeuerwehrManagementSystem - Urlubsplaner");
        logging.logInfo((Object)"Starte: UrlaubsplanerAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonLoeschen = new JButton("L\u00f6schen");
        this.datumVon = new JTextField(20);
        this.datumBis = new JTextField(20);
        this.datumVon_label = new JLabel("Datum Beginn: ");
        this.datumBis_label = new JLabel("Datum Ende: ");
        this.mitgliederListe_label = new JLabel("Mitgliederliste: ");
        this.modulBeschreibung = new JLabel("Urlaubsplaner");
        this.beschreibungLoeschen = new JLabel("Urlaub l\u00f6schen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.dummy4 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        try {
            TabelleMitglied tabMitglieder = new TabelleMitglied();
            TabelleUrlaub tabUrlaub = new TabelleUrlaub();
            String[] liste = Utils.listToArrayOnlyFORComboBoxes(tabMitglieder.getMitgliederGruppe1());
            String[] urlaubListe = Utils.listToArrayOnlyFORComboBoxes(tabUrlaub.getUrlaubsliste());
            this.mitgliederListe = new JComboBox<String>(liste);
            this.urlaubsliste = new JComboBox<String>(urlaubListe);
        }
        catch (SQLException e) {
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
        this.setSize(560, 340);
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Untersuchung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.mitgliederListe_label);
        this.panel.add(this.mitgliederListe);
        this.panel.add(this.datumVon_label);
        this.panel.add(this.datumVon);
        this.panel.add(this.datumBis_label);
        this.panel.add(this.datumBis);
        this.add(this.buttonSpeichern);
        this.add(this.dummy3);
        this.add(this.beschreibungLoeschen);
        this.add(this.dummy4);
        this.panelLoeschen = new JPanel(new GridLayout(1, 1));
        this.getContentPane().add("Center", this.panelLoeschen);
        this.panelLoeschen.add(this.urlaubsliste);
        this.add(this.buttonLoeschen);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (UrlaubsplanerAO.this.mitgliederListe.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                } else if (!TimeCalculation.checkDateFormat(UrlaubsplanerAO.this.datumVon.getText())) {
                    JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                    UrlaubsplanerAO.this.datumVon.setBackground(Color.red);
                } else if (!TimeCalculation.checkDateFormat(UrlaubsplanerAO.this.datumBis.getText())) {
                    JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Warnung", 2);
                    UrlaubsplanerAO.this.datumBis.setBackground(Color.red);
                } else {
                    UrlaubsplanerAO.this.datumVon.setBackground(Color.white);
                    UrlaubsplanerAO.this.datumBis.setBackground(Color.white);
                    try {
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        TabelleUrlaub tabUrlaub = new TabelleUrlaub();
                        Urlaub urlaub = new Urlaub();
                        int mID = tabMitglied.getIdByGuiString(UrlaubsplanerAO.this.mitgliederListe.getSelectedItem().toString());
                        urlaub.setId(tabUrlaub.getNextNummer());
                        urlaub.setJahr(Integer.parseInt((String)UrlaubsplanerAO.this.datumVon.getText().subSequence(6, 10)));
                        urlaub.setMitgliederID(mID);
                        urlaub.setDatumVon(TimeCalculation.parseDateForDatabase(UrlaubsplanerAO.this.datumVon.getText()));
                        urlaub.setDatumBis(TimeCalculation.parseDateForDatabase(UrlaubsplanerAO.this.datumBis.getText()));
                        urlaub.setLoeschkenner(0);
                        tabUrlaub.insert(urlaub);
                        logging.logInfo((Object)("Urlaub erfolgreich gespeichert (" + UrlaubsplanerAO.this.mitgliederListe.getSelectedItem().toString() + ", DatumVon: " + UrlaubsplanerAO.this.datumVon.getText() + ", DatumBis: " + UrlaubsplanerAO.this.datumBis.getText() + ")"));
                        logbuchEingabe.NeuerEintag("Urlaub erfolgreich gespeichert (" + UrlaubsplanerAO.this.mitgliederListe.getSelectedItem().toString() + ", DatumVon: " + UrlaubsplanerAO.this.datumVon.getText() + ", DatumBis: " + UrlaubsplanerAO.this.datumBis.getText() + ")");
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        UrlaubsplanerAO.this.mitgliederListe.setSelectedItem("<bitte w\u00e4hlen>");
                        UrlaubsplanerAO.this.datumVon.setText(null);
                        UrlaubsplanerAO.this.datumBis.setText(null);
                        UrlaubsplanerAO.this.urlaubsliste.removeAllItems();
                        String[] urlaubListe = Utils.listToArrayOnlyFORComboBoxes(tabUrlaub.getUrlaubsliste());
                        int i = 0;
                        while (i < urlaubListe.length) {
                            UrlaubsplanerAO.this.urlaubsliste.addItem(urlaubListe[i]);
                            ++i;
                        }
                    }
                    catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonLoeschen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (UrlaubsplanerAO.this.urlaubsliste.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_EINTRAG_ZUM_Loeschen_WAEHLEN, "Warnung", 2);
                } else {
                    int msg = JOptionPane.showConfirmDialog(null, Konstante.WIRKLICH_LOESCHEN_DATENSATZ, "Frage", 0);
                    if (msg == 0) {
                        TabelleUrlaub tabUrlaub = new TabelleUrlaub();
                        try {
                            int[] idListe = Utils.listToIntArray(tabUrlaub.getIDListe());
                            tabUrlaub.updateLoeschkenner(idListe[UrlaubsplanerAO.this.urlaubsliste.getSelectedIndex()]);
                            JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                            UrlaubsplanerAO.this.urlaubsliste.removeAllItems();
                            String[] urlaubListe = Utils.listToArrayOnlyFORComboBoxes(tabUrlaub.getUrlaubsliste());
                            int i = 0;
                            while (i < urlaubListe.length) {
                                UrlaubsplanerAO.this.urlaubsliste.addItem(urlaubListe[i]);
                                ++i;
                            }
                        }
                        catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                            logging.logPrintStackTrace((Exception)e);
                        }
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

