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
import data.tabellen.mitglied.TabelleMitglieder_anrede;
import go.Mitglieder_Anrede;
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
import utilities.logbuchEingabe;

public class MitgliederAnredeAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    public static String letzteAnrede;
    private JTextField name;
    private JLabel beschreibung;
    private JComboBox<String> anredeBrief;
    private JLabel anredeBrief_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelKategorie;

    public MitgliederAnredeAnlegenAO() {
        super("FeuerwehrManagementSystem - Mitglieder Anrede");
        logging.logInfo((Object)"Starte: MitgliederAnredeAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Mitglieder Anrede");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Anrede Teil2: ");
        String[] anredeListe = new String[]{"Sehr geehrter", "Sehr geehrte"};
        this.anredeBrief = new JComboBox<String>(anredeListe);
        this.anredeBrief_label = new JLabel("Anrede Teil1: ");
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
        this.setSize(500, 190);
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Anrede");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelKategorie = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panelKategorie);
        this.panelKategorie.add(this.anredeBrief_label);
        this.panelKategorie.add(this.anredeBrief);
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
                TabelleMitglieder_anrede tabAnrede = new TabelleMitglieder_anrede();
                Mitglieder_Anrede anrede = new Mitglieder_Anrede();
                try {
                    if (tabAnrede.getAnredeID(MitgliederAnredeAnlegenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.ANREDE_SCHON_VORHANDEN, "Warnung", 2);
                    } else {
                        anrede.setId(tabAnrede.getNextNummer());
                        anrede.setName(MitgliederAnredeAnlegenAO.this.name.getText());
                        anrede.setAnredeBrief(MitgliederAnredeAnlegenAO.this.anredeBrief.getSelectedItem().toString());
                        tabAnrede.insert(anrede);
                        letzteAnrede = MitgliederAnredeAnlegenAO.this.name.getText();
                        MitgliederAnredeAnlegenAO.this.name.setText(null);
                        logging.logInfo((Object)"Neue Anrede wurde erfolgreich angelegt");
                        logbuchEingabe.NeuerEintag("Neue Anrede wurde erstellt: " + letzteAnrede);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0025")) {
                            MitgliederAnlegenAO.anrede.addItem(letzteAnrede);
                            MitgliederAnredeAnlegenAO.this.dispose();
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

