/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.ausbildung;

import ao.AbstractFenster;
import ao.ausbildung.AusbildungsInhaltEintragenAO;
import ao.ausbildung.AusbildungsplanAO;
import data.tabellen.TabelleAusbildung_Kategorie;
import go.Ausbildung_Kategorie;
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
import utilities.MyEvent;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class AusbildungKategorieAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField name;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelKategorie;

    public AusbildungKategorieAnlegenAO() {
        super("FeuerwehrManagementSystem - Ausbildungskategorie Anlegen");
        logging.logInfo((Object)"Starte: AusbildungKategorieEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.modulBeschreibung = new JLabel("Ausbildung Eintragen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Ausbildungskategorie: ");
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
        this.setSize(500, 170);
        this.setTitle("FeuerwehrManagementSystem - Veranstaltungskategorie");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelKategorie = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panelKategorie);
        this.panelKategorie.add(this.beschreibung);
        this.panelKategorie.add(this.name);
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
                TabelleAusbildung_Kategorie tabelleKategorie = new TabelleAusbildung_Kategorie();
                Ausbildung_Kategorie kategorie = new Ausbildung_Kategorie();
                try {
                    if (tabelleKategorie.getCount(AusbildungKategorieAnlegenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.AUSBILDUNGKATEGORIE_SCHON_VORHANDEN, "Warnung", 2);
                    } else {
                        kategorie.setId(tabelleKategorie.getNextNummer());
                        kategorie.setName(AusbildungKategorieAnlegenAO.this.name.getText());
                        tabelleKategorie.insert(kategorie);
                        if (runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden").equals("1")) {
                            Joomla.erstelleAusbildungKategorie(kategorie);
                        }
                        logging.logInfo((Object)("Ausbildungskategorie angelegt: " + AusbildungKategorieAnlegenAO.this.name.getText()));
                        logbuchEingabe.NeuerEintag("Ausbildungskategorie angelegt: " + AusbildungKategorieAnlegenAO.this.name.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0027")) {
                            int i = 0;
                            while (i < AusbildungsplanAO.ausbildungkategorien.length) {
                                AusbildungsplanAO.ausbildungkategorien[i].addItem(AusbildungKategorieAnlegenAO.this.name.getText());
                                ++i;
                            }
                            AusbildungKategorieAnlegenAO.this.dispose();
                        }
                        if (MyEvent.event.equals("0x0023")) {
                            AusbildungsInhaltEintragenAO.ausbildungskategorie.addItem(AusbildungKategorieAnlegenAO.this.name.getText());
                            AusbildungKategorieAnlegenAO.this.dispose();
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

