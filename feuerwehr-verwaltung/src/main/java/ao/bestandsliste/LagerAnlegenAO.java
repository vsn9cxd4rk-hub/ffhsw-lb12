/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.bestandsliste;

import ao.AbstractFenster;
import ao.bestandsliste.BestandslisteAO;
import data.tabellen.bestandsliste.TabelleLager;
import data.tabellen.mitglied.TabelleMitglied;
import go.bestandsliste.Lager;
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
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.Utils;

public class LagerAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonNaechsterLager;
    private JTextField id;
    private JTextField name;
    private JComboBox<String> mitglied;
    private JLabel id_label;
    private JLabel name_label;
    private JLabel mitglied_label;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public LagerAnlegenAO() {
        super("FeuerwehrManagementSystem - Lager anlegen");
        logging.logInfo((Object)"Starte: LagerAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonNaechsterLager = new JButton("N\u00e4chster Artikel");
        this.id = new JTextField(20);
        this.name = new JTextField(20);
        this.id_label = new JLabel("Lagernummer: ");
        this.name_label = new JLabel("Name: ");
        this.mitglied_label = new JLabel("Lager Verantwortlicher: ");
        this.modulBeschreibung = new JLabel("Lager anlegen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleLager tabLager = new TabelleLager();
        try {
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            this.mitglied = new JComboBox<String>(mitgliederListe);
            this.id.setText(Integer.toString(tabLager.getNextNummer()));
            this.id.setEditable(false);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Artikel");
        this.setSize(500, 220);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.id_label);
        this.panel.add(this.id);
        this.panel.add(this.name_label);
        this.panel.add(this.name);
        this.panel.add(this.mitglied_label);
        this.panel.add(this.mitglied);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonNaechsterLager);
        this.buttonNaechsterLager.setVisible(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleLager tabLager = new TabelleLager();
                    Lager lager = new Lager();
                    lager.setId(Integer.parseInt(LagerAnlegenAO.this.id.getText()));
                    lager.setName(LagerAnlegenAO.this.name.getText());
                    if (LagerAnlegenAO.this.mitglied.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        lager.setVerantwortlicher(0);
                    } else {
                        lager.setVerantwortlicher(new TabelleMitglied().getIdByGuiString(LagerAnlegenAO.this.mitglied.getSelectedItem().toString()));
                    }
                    tabLager.insert(lager);
                    LagerAnlegenAO.this.buttonSpeichern.setVisible(false);
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    BestandslisteAO.tree.setModel(CreateTrees.CreateBestandslisteTree());
                    LagerAnlegenAO.this.dispose();
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNaechsterLager.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLager tabLager = new TabelleLager();
                try {
                    LagerAnlegenAO.this.buttonNaechsterLager.setVisible(false);
                    LagerAnlegenAO.this.buttonSpeichern.setEnabled(true);
                    LagerAnlegenAO.this.name.setText(null);
                    LagerAnlegenAO.this.id.setText(Integer.toString(tabLager.getNextNummer()));
                }
                catch (SQLException e) {
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

