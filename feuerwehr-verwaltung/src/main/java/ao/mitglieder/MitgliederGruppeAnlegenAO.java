/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import go.Mitglieder_gruppe;
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
import utilities.logbuchEingabe;

public class MitgliederGruppeAnlegenAO
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

    public MitgliederGruppeAnlegenAO() {
        super("FeuerwehrManagementSystem - Mitglieder Gruppe");
        logging.logInfo((Object)"Starte: MitgliederGruppeAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Mitglieder Gruppe");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Mitgliedergruppen Name: ");
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
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Gruppe");
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
                TabelleMitglieder_gruppe tabeGruppe = new TabelleMitglieder_gruppe();
                Mitglieder_gruppe gruppe = new Mitglieder_gruppe();
                try {
                    if (tabeGruppe.getMitgliederGruppenID(MitgliederGruppeAnlegenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.GRUPPE_SCHON_VORHANDEN, "Warnung", 2);
                    } else {
                        int pNummer = tabeGruppe.getNextPersonalnummer();
                        gruppe.setId(tabeGruppe.getNextNummer());
                        gruppe.setPersonalnummer(pNummer);
                        gruppe.setNextPersonalnummer(pNummer);
                        gruppe.setName(MitgliederGruppeAnlegenAO.this.name.getText());
                        tabeGruppe.insert(gruppe);
                        letzteGruppe = MitgliederGruppeAnlegenAO.this.name.getText();
                        MitgliederGruppeAnlegenAO.this.name.setText(null);
                        logging.logInfo((Object)"Neue Benutzergruppe wurde erfolgreich angelegt");
                        logbuchEingabe.NeuerEintag("Neue Mitgliedergruppe wurde angelegt: " + letzteGruppe);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0022")) {
                            MitgliederAnlegenAO.gruppe.addItem(letzteGruppe);
                            MitgliederGruppeAnlegenAO.this.dispose();
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

