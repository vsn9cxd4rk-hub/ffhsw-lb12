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
import ao.fahrzeuge.FahrzeugAnlegenAO;
import data.tabellen.TabelleFahrzeug_beschreibung;
import go.Fahrzeug_beschreibung;
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
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.logbuchEingabe;

public class FahrzeugGruppeAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    public static String letzteGruppe;
    private JTextField name;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelKategorie;

    public FahrzeugGruppeAnlegenAO() {
        super("FeuerwehrManagementSystem - Fahrzeug Gruppe");
        logging.logInfo((Object)"Starte: FahrzeugGruppeAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Fahrzeug Gruppe");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Fahrzeuggruppen Name: ");
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
        this.setTitle("FeuerwehrManagementSystem - Fahrzeug Gruppe");
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
                TabelleFahrzeug_beschreibung tabFahrzeugBeschreibung = new TabelleFahrzeug_beschreibung();
                Fahrzeug_beschreibung beschreibung = new Fahrzeug_beschreibung();
                try {
                    if (tabFahrzeugBeschreibung.getFahrzeugGruppenID(FahrzeugGruppeAnlegenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.GRUPPE_SCHON_VORHANDEN, "Warnung", 2);
                    } else {
                        beschreibung.setId(tabFahrzeugBeschreibung.getNextNummer());
                        beschreibung.setName(FahrzeugGruppeAnlegenAO.this.name.getText());
                        tabFahrzeugBeschreibung.insert(beschreibung);
                        letzteGruppe = FahrzeugGruppeAnlegenAO.this.name.getText();
                        FahrzeugGruppeAnlegenAO.this.name.setText(null);
                        logging.logInfo((Object)"Neue Fahrzeuggruppe wurde erfolgreich angelegt");
                        logbuchEingabe.NeuerEintag("Neue Fahrzeug Gruppe wurde angelegt: " + letzteGruppe);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0028")) {
                            FahrzeugAnlegenAO.tree.setModel(CreateTrees.CreateTreeFahrzeugListe());
                            FahrzeugAnlegenAO.kategorie.addItem(letzteGruppe);
                            FahrzeugGruppeAnlegenAO.this.dispose();
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

