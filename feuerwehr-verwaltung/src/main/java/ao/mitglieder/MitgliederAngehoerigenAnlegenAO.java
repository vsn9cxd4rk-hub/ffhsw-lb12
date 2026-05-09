/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.mitglied.TabelleMitglieder_angehoerige;
import go.Mitglieder_Angehoerige;
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
import utilities.logbuchEingabe;

public class MitgliederAngehoerigenAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField name_mitglied;
    private JTextField personalnummer;
    private JTextField name;
    private JTextField strasse;
    private JTextField ort;
    private JTextField telPrivat;
    private JTextField telMobil;
    private JTextField email;
    private JLabel name_mitglied_label;
    private JLabel personalnummer_label;
    private JLabel name_label;
    private JLabel strasse_label;
    private JLabel ort_label;
    private JLabel telPrivat_label;
    private JLabel telMobil_label;
    private JLabel email_label;
    private JPanel panelMitglieder;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public MitgliederAngehoerigenAnlegenAO() {
        super("FeuerwehrManagementSystem - Angeh\u00f6rige");
        logging.logInfo((Object)"Starte: MitgliederAngehoerigeAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.personalnummer = new JTextField(MitgliederAnlegenAO.mitgliedID, 20);
        this.name_mitglied = new JTextField(MitgliederAnlegenAO.mitgliedName, 20);
        this.name = new JTextField(20);
        this.strasse = new JTextField(20);
        this.ort = new JTextField(20);
        this.telPrivat = new JTextField(20);
        this.telMobil = new JTextField(20);
        this.email = new JTextField(20);
        this.personalnummer_label = new JLabel("Personalnummer");
        this.name_mitglied_label = new JLabel("Mitglied Name: ");
        this.name_label = new JLabel("Name u. Vorname: ");
        this.strasse_label = new JLabel("Stra\u00dfe: ");
        this.ort_label = new JLabel("Ort od. PLZ: ");
        this.telPrivat_label = new JLabel("Telefon Privat: ");
        this.email_label = new JLabel("E-Mail: ");
        this.telMobil_label = new JLabel("Telefon Mobil: ");
        this.modulBeschreibung = new JLabel("Mitglieder - Angeh\u00f6rige");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        TabelleMitglieder_angehoerige arbeit = new TabelleMitglieder_angehoerige();
        try {
            int mID = Integer.parseInt(this.personalnummer.getText());
            if (arbeit.getCount(mID) != 0) {
                this.name.setText(arbeit.getName(mID));
                this.strasse.setText(arbeit.getStrasse(mID));
                this.ort.setText(arbeit.getOrt(mID));
                this.telPrivat.setText(arbeit.getTelefonPrivat(mID));
                this.email.setText(arbeit.getEMail(mID));
                this.telMobil.setText(arbeit.getTelefonMobil(mID));
            }
        }
        catch (NumberFormatException | SQLException e) {
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
        this.setSize(580, 310);
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Angeh\u00f6rige");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelMitglieder = new JPanel(new GridLayout(8, 2));
        this.getContentPane().add("Center", this.panelMitglieder);
        this.panelMitglieder.add(this.personalnummer_label);
        this.panelMitglieder.add(this.personalnummer);
        this.panelMitglieder.add(this.name_mitglied_label);
        this.panelMitglieder.add(this.name_mitglied);
        this.panelMitglieder.add(this.name_label);
        this.panelMitglieder.add(this.name);
        this.panelMitglieder.add(this.strasse_label);
        this.panelMitglieder.add(this.strasse);
        this.panelMitglieder.add(this.ort_label);
        this.panelMitglieder.add(this.ort);
        this.panelMitglieder.add(this.telPrivat_label);
        this.panelMitglieder.add(this.telPrivat);
        this.panelMitglieder.add(this.telMobil_label);
        this.panelMitglieder.add(this.telMobil);
        this.panelMitglieder.add(this.email_label);
        this.panelMitglieder.add(this.email);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.personalnummer.setEditable(false);
        this.name_mitglied.setEditable(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglieder_angehoerige tabAngehoerige = new TabelleMitglieder_angehoerige();
                Mitglieder_Angehoerige angehoerige = new Mitglieder_Angehoerige();
                try {
                    int mID = Integer.parseInt(MitgliederAngehoerigenAnlegenAO.this.personalnummer.getText());
                    angehoerige.setId(mID);
                    angehoerige.setName(MitgliederAngehoerigenAnlegenAO.this.name.getText());
                    angehoerige.setStrasse(MitgliederAngehoerigenAnlegenAO.this.strasse.getText());
                    angehoerige.setOrt(MitgliederAngehoerigenAnlegenAO.this.ort.getText());
                    angehoerige.setTelefonPrivat(MitgliederAngehoerigenAnlegenAO.this.telPrivat.getText());
                    angehoerige.setTelefonMobil(MitgliederAngehoerigenAnlegenAO.this.telMobil.getText());
                    angehoerige.setEmail(MitgliederAngehoerigenAnlegenAO.this.email.getText());
                    if (tabAngehoerige.getCount(mID) == 0) {
                        tabAngehoerige.insert(angehoerige);
                    } else {
                        tabAngehoerige.update(angehoerige);
                    }
                    logging.logInfo((Object)"Angeh\u00f6rige erfolgreich gespeichert");
                    logbuchEingabe.NeuerEintag("Mitgliederangeh\u00f6rige wurde angelegt: " + mID);
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
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

