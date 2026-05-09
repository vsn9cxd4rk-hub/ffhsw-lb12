/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.veranstaltung;

import ao.AbstractFenster;
import ao.veranstaltung.VeranstaltungAnlegenAO;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.abrechnung.TabelleAbrechnung_artikelklassen;
import go.Veranstaltung_Kategorie;
import go.abrechnung.AbrechnungArtikelklassen;
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

public class VeranstaltungKategorieAnlegenAO
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
    public static String letzteKategorie;

    public VeranstaltungKategorieAnlegenAO() {
        super("FeuerwehrManagementSystem - Veranstaltungskategorie");
        logging.logInfo((Object)"Starte: VeransatltungKategorieEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Veransatltungskategorie Eintragen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Kategorie: ");
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
                TabelleVeranstaltung_Kategorie tabelleKategorie = new TabelleVeranstaltung_Kategorie();
                TabelleAbrechnung_artikelklassen tabArtikelklasse = new TabelleAbrechnung_artikelklassen();
                Veranstaltung_Kategorie kategorie = new Veranstaltung_Kategorie();
                AbrechnungArtikelklassen artikelklasse = new AbrechnungArtikelklassen();
                try {
                    if (tabelleKategorie.getCount(VeranstaltungKategorieAnlegenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.VERANSTALTUNGKATEGORIE_SCHON_VORHANDEN, "Warnung", 2);
                    } else {
                        int nextID = tabelleKategorie.getNextNummer();
                        kategorie.setId(nextID);
                        kategorie.setName(VeranstaltungKategorieAnlegenAO.this.name.getText());
                        artikelklasse.setId(nextID);
                        artikelklasse.setName(VeranstaltungKategorieAnlegenAO.this.name.getText());
                        tabelleKategorie.insert(kategorie);
                        tabArtikelklasse.insert(artikelklasse);
                        letzteKategorie = VeranstaltungKategorieAnlegenAO.this.name.getText();
                        VeranstaltungKategorieAnlegenAO.this.name.setText(null);
                        if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1")) {
                            Joomla.erstelleVeranstaltungKategorie(kategorie);
                        }
                        logging.logInfo((Object)("Veranstaltungskategorie wurde angelegt: " + letzteKategorie));
                        logbuchEingabe.NeuerEintag("Veranstaltungskategorie wurde angelegt: " + letzteKategorie);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0021")) {
                            VeranstaltungAnlegenAO.kategorie.addItem(letzteKategorie);
                            VeranstaltungKategorieAnlegenAO.this.dispose();
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

