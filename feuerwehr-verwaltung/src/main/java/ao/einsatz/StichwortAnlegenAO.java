/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.einsatz;

import ao.AbstractFenster;
import ao.einsatz.EinsatzEintragenAO;
import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleStichwort;
import go.Stichwort;
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
import utilities.MyEvent;
import utilities.Utils;
import utilities.logbuchEingabe;

public class StichwortAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField name;
    public static String letzteStichwort;
    private JLabel beschreibung;
    private JLabel einsatzKategorie_label;
    private JComboBox<String> einsatzKategorie;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelStichwort;

    public StichwortAnlegenAO() {
        super("FeuerwehrManagementSystem - Stichwort");
        logging.logInfo((Object)"Starte: StichwortEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.einsatzKategorie_label = new JLabel("Stichwortkategorie: ");
        this.modulBeschreibung = new JLabel("Stichwort anlegen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Stichwort: ");
    }

    protected void labelErstellen() {
        TabelleEinsatz_kategorie tabKategorie = new TabelleEinsatz_kategorie();
        try {
            String[] kategorieListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAllEinsatzKategorie());
            this.einsatzKategorie = new JComboBox<String>(kategorieListe);
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
        this.setSize(600, 200);
        this.setTitle("FeuerwehrManagementSystem - Stichwort");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelStichwort = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panelStichwort);
        this.panelStichwort.add(this.beschreibung);
        this.panelStichwort.add(this.name);
        this.panelStichwort.add(this.einsatzKategorie_label);
        this.panelStichwort.add(this.einsatzKategorie);
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
                TabelleStichwort tabelleStichwort = new TabelleStichwort();
                TabelleEinsatz_kategorie tabKategorie = new TabelleEinsatz_kategorie();
                Stichwort stichwort = new Stichwort();
                try {
                    if (StichwortAnlegenAO.this.einsatzKategorie.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        logging.logInfo((Object)"Stichwort / Einsatzkategorie wurde nicht gew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_KATEGORIE_WAEHLEN, "Warnung", 2);
                    } else if (tabelleStichwort.getStichwortCount(StichwortAnlegenAO.this.name.getText()) != 0) {
                        logging.logInfo((Object)"Stichwort existiert bereits");
                        JOptionPane.showMessageDialog(null, Konstante.STICHWORT_EXISTIERT_BEREITS, "Warnung", 2);
                    } else if (StichwortAnlegenAO.this.name.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_STICHWORT_EINGEBEN, "Warnung", 2);
                    } else {
                        stichwort.setId(tabelleStichwort.getNextNummer());
                        stichwort.setKategorie(tabKategorie.getKategorieID(StichwortAnlegenAO.this.einsatzKategorie.getSelectedItem().toString()));
                        stichwort.setName(StichwortAnlegenAO.this.name.getText());
                        letzteStichwort = StichwortAnlegenAO.this.name.getText();
                        tabelleStichwort.insert(stichwort);
                        StichwortAnlegenAO.this.name.setText(null);
                        StichwortAnlegenAO.this.einsatzKategorie.setSelectedItem("<bitte w\u00e4hlen>");
                        logging.logInfo((Object)"Stichwort erfogreich gespeichert");
                        logbuchEingabe.NeuerEintag("Stichwort wurde angelegt: " + letzteStichwort);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0020")) {
                            EinsatzEintragenAO.Box_Stichwort.addItem(letzteStichwort);
                            StichwortAnlegenAO.this.dispose();
                        }
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

