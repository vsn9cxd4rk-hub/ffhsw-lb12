/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.hash
 */
package ao.einstellungen;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleBerechtigunggruppe;
import data.tabellen.einstellungen.TabelleUser;
import go.User;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;
import utilities.hash;
import utilities.logbuchEingabe;

public class BenutzerAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static String mitgliedName = null;
    public static String mitgliedID = null;
    private JButton buttonZurueck;
    private JButton buttonSpeichernBenutzer;
    private JButton neueBerechtigungsGruppeErstellen;
    private JButton berechtigunBearbeiten;
    private JTextField benutzername;
    private JPasswordField passswort1;
    private JPasswordField passswort2;
    public static JComboBox<String> berechtigungsgruppe;
    private JCheckBox adminbereichZuweisen;
    private JLabel benutzername_label;
    private JLabel passwort1_label;
    private JLabel passwort2_label;
    private JLabel berechtigungsgrppe_label;
    private JLabel adminbereichZuweisen_label;
    private JButton buttonSpeichernBenutzerSperren;
    private JComboBox<String> benutzer;
    private JLabel benutzer_label;
    private JCheckBox activ;
    private JLabel activ_label;
    private JCheckBox loeschkenner;
    private JLabel loeschkenner_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JPanel panelBenutzer;
    private JPanel panelSperrenLoeschen;
    private JButton buttonSpeichernPasswortAendern;
    private JLabel pass_benutzer_label;
    private JLabel pass_altesPasswort_label;
    private JLabel pass_neuesPasswort_label;
    private JLabel pass_neuesPasswort2_label;
    private JComboBox<String> pass_benutzer;
    private JPasswordField pass_altesPasswort;
    private JPasswordField pass_neuesPasswort;
    private JPasswordField pass_neuesPasswort2;
    private JPanel panelPasswort\u00c4nderung;
    private JTabbedPane tabPane;

    public BenutzerAnlegenAO() {
        super("FeuerwehrManagementSystem - Benutzerverwaltung");
        logging.logInfo((Object)"Starte: BenutzerAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichernBenutzer = new JButton("Speichern");
        this.buttonSpeichernBenutzerSperren = new JButton("Speichern");
        this.buttonSpeichernPasswortAendern = new JButton("Passwort \u00e4ndern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.neueBerechtigungsGruppeErstellen = new JButton("Neue Berechtigungsgruppe");
        this.berechtigunBearbeiten = new JButton("Berechtigungsgruppe bearbeiten");
        this.benutzername = new JTextField(20);
        this.passswort1 = new JPasswordField(20);
        this.passswort2 = new JPasswordField(20);
        this.adminbereichZuweisen = new JCheckBox();
        this.benutzername_label = new JLabel("Benutzername: ");
        this.passwort1_label = new JLabel("Passwort: ");
        this.passwort2_label = new JLabel("Passwort wdh.: ");
        this.berechtigungsgrppe_label = new JLabel("Berechtigungsgruppe: ");
        this.adminbereichZuweisen_label = new JLabel("Administratorenberich zuweisen: ");
        this.benutzer_label = new JLabel("Benutzerliste: ");
        this.activ = new JCheckBox();
        this.activ_label = new JLabel("Benutzer sperren: ");
        this.loeschkenner_label = new JLabel("Benutzer l\u00f6schen: ");
        this.loeschkenner = new JCheckBox();
        this.pass_altesPasswort = new JPasswordField(20);
        this.pass_neuesPasswort = new JPasswordField(20);
        this.pass_neuesPasswort2 = new JPasswordField(20);
        this.pass_altesPasswort_label = new JLabel("Altes Passwort: ");
        this.pass_neuesPasswort_label = new JLabel("Neues Passwort: ");
        this.pass_neuesPasswort2_label = new JLabel("Neues Passwort wdh.: ");
        this.pass_benutzer_label = new JLabel("Benutzername");
        this.modulBeschreibung = new JLabel("Benutzerverwaltung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.tabPane = new JTabbedPane();
    }

    protected void labelErstellen() {
        try {
            TabelleBerechtigunggruppe tabBer = new TabelleBerechtigunggruppe();
            TabelleUser tabUser = new TabelleUser();
            String[] gruppenListe = Utils.listToArrayOnlyFORComboBoxes(tabBer.getBercehtigungsgruppen());
            String[] userliste = Utils.listToArrayOnlyFORComboBoxes(tabUser.getUserListe());
            berechtigungsgruppe = new JComboBox<String>(gruppenListe);
            this.benutzer = new JComboBox<String>(userliste);
            this.pass_benutzer = new JComboBox<String>(userliste);
            this.benutzer.removeItem("admin");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.benutzer.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleUser tabUser = new TabelleUser();
                try {
                    if (tabUser.getDeaktivStatus(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString()) == 1) {
                        BenutzerAnlegenAO.this.activ.setSelected(true);
                    } else {
                        BenutzerAnlegenAO.this.activ.setSelected(false);
                    }
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
        this.setSize(670, 500);
        this.setTitle("FeuerwehrManagementSystem - Benutzerverwaltung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.neueBerechtigungsGruppeErstellen);
        this.add(this.berechtigunBearbeiten);
        this.panelBenutzer = new JPanel(new GridLayout(10, 2));
        this.getContentPane().add("Center", this.panelBenutzer);
        this.panelBenutzer.add(this.berechtigungsgrppe_label);
        this.panelBenutzer.add(berechtigungsgruppe);
        this.panelBenutzer.add(this.adminbereichZuweisen_label);
        this.panelBenutzer.add(this.adminbereichZuweisen);
        this.panelBenutzer.add(this.benutzername_label);
        this.panelBenutzer.add(this.benutzername);
        this.panelBenutzer.add(this.passwort1_label);
        this.panelBenutzer.add(this.passswort1);
        this.panelBenutzer.add(this.passwort2_label);
        this.panelBenutzer.add(this.passswort2);
        this.panelBenutzer.add(new JLabel());
        this.panelBenutzer.add(this.buttonSpeichernBenutzer);
        this.panelBenutzer.add(new JLabel());
        this.panelBenutzer.add(new JLabel());
        this.panelBenutzer.add(new JLabel());
        this.panelBenutzer.add(new JLabel());
        this.panelSperrenLoeschen = new JPanel(new GridLayout(10, 2));
        this.getContentPane().add("Center", this.panelSperrenLoeschen);
        this.panelSperrenLoeschen.add(this.benutzer_label);
        this.panelSperrenLoeschen.add(this.benutzer);
        this.panelSperrenLoeschen.add(this.activ_label);
        this.panelSperrenLoeschen.add(this.activ);
        this.panelSperrenLoeschen.add(this.loeschkenner_label);
        this.panelSperrenLoeschen.add(this.loeschkenner);
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(this.buttonSpeichernBenutzerSperren);
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelSperrenLoeschen.add(new JLabel());
        this.panelPasswort\u00c4nderung = new JPanel(new GridLayout(10, 2));
        this.getContentPane().add("Center", this.panelPasswort\u00c4nderung);
        this.panelPasswort\u00c4nderung.add(this.pass_benutzer_label);
        this.panelPasswort\u00c4nderung.add(this.pass_benutzer);
        this.panelPasswort\u00c4nderung.add(this.pass_altesPasswort_label);
        this.panelPasswort\u00c4nderung.add(this.pass_altesPasswort);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort_label);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort2_label);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort2);
        this.panelPasswort\u00c4nderung.add(new JLabel());
        this.panelPasswort\u00c4nderung.add(this.buttonSpeichernPasswortAendern);
        this.panelPasswort\u00c4nderung.add(new JLabel());
        this.panelPasswort\u00c4nderung.add(new JLabel());
        this.panelPasswort\u00c4nderung.add(new JLabel());
        this.panelPasswort\u00c4nderung.add(new JLabel());
        this.tabPane.addTab("Benutzer anlegen", this.panelBenutzer);
        this.tabPane.addTab("Benutzer sperren / l\u00f6schen", this.panelSperrenLoeschen);
        this.tabPane.addTab("Passwort \u00e4ndern", this.panelPasswort\u00c4nderung);
        this.tabPane.setPreferredSize(new Dimension(630, 350));
        this.add(this.tabPane);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.neueBerechtigungsGruppeErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BERECHTIGUNG);
                Steuerung.steuerung();
            }
        });
        this.berechtigunBearbeiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0005");
                Steuerung.setStatus(Status.BERECHTIGUNG);
                Steuerung.steuerung();
            }
        });
        this.buttonSpeichernPasswortAendern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleUser tabUser = new TabelleUser();
                try {
                    User user = tabUser.get(BenutzerAnlegenAO.this.pass_benutzer.getSelectedItem().toString());
                    if (!BenutzerAnlegenAO.this.pass_altesPasswort.getText().equals(hash.decodeHashCode((String)user.getPasswort()))) {
                        JOptionPane.showMessageDialog(null, Konstante.ALTE_PASSWORT_FALSCH, "Fehlermeldung", 0);
                    } else if (!BenutzerAnlegenAO.this.pass_neuesPasswort.getText().equals(BenutzerAnlegenAO.this.pass_neuesPasswort2.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.NEUE_PASSWORT_FALSCH, "Fehlermeldung", 0);
                    } else {
                        tabUser.updatepasswort(BenutzerAnlegenAO.this.pass_benutzer.getSelectedItem().toString(), hash.createHashCode((String)BenutzerAnlegenAO.this.pass_neuesPasswort.getText()));
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        logbuchEingabe.NeuerEintag("Passwort wurde ge\u00e4ndert f\u00fcr: " + BenutzerAnlegenAO.this.pass_benutzer.getSelectedItem().toString());
                        BenutzerAnlegenAO.this.pass_benutzer.setSelectedItem("<bitte w\u00e4hlen>");
                        BenutzerAnlegenAO.this.pass_neuesPasswort.setText(null);
                        BenutzerAnlegenAO.this.pass_neuesPasswort2.setText(null);
                        BenutzerAnlegenAO.this.pass_altesPasswort.setText(null);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichernBenutzerSperren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleUser tabUser = new TabelleUser();
                try {
                    if (BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_BENUTZER_WAEHLEN, "Warnung", 2);
                    } else {
                        tabUser.updateDeaktiv(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString(), BenutzerAnlegenAO.this.activ.isSelected() ? 1 : 0);
                        tabUser.updateLoeschkenner(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString(), BenutzerAnlegenAO.this.loeschkenner.isSelected() ? 1 : 0);
                        logbuchEingabe.NeuerEintag("Benutzer wurde Sperre gesetzt auf " + Integer.toString(BenutzerAnlegenAO.this.activ.isSelected() ? 1 : 0) + " " + BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                        logbuchEingabe.NeuerEintag("Benutzer wurde Loeschkenner gesetzt auf " + Integer.toString(BenutzerAnlegenAO.this.loeschkenner.isSelected() ? 1 : 0) + " " + BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                        if (BenutzerAnlegenAO.this.loeschkenner.isSelected()) {
                            BenutzerAnlegenAO.this.benutzer.removeItem(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                            BenutzerAnlegenAO.this.benutzer.setSelectedItem("<bitte w\u00e4hlen>");
                            BenutzerAnlegenAO.this.activ.setSelected(false);
                            BenutzerAnlegenAO.this.loeschkenner.setSelected(false);
                        }
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichernBenutzer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleUser tabUser = new TabelleUser();
                TabelleBerechtigunggruppe tabBer = new TabelleBerechtigunggruppe();
                User user = new User();
                try {
                    if (BenutzerAnlegenAO.this.benutzername.getText().equals("MASTER_USER_FMS")) {
                        JOptionPane.showMessageDialog(null, Konstante.USER_NAME_UNGUELTIG, "Fehlermeldung", 0);
                    } else if (berechtigungsgruppe.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_BERECHTIGUNG_WAEHLEN, "Warnung", 2);
                    } else if (BenutzerAnlegenAO.this.passswort1.getText().equals(BenutzerAnlegenAO.this.passswort2.getText())) {
                        user.setUser(BenutzerAnlegenAO.this.benutzername.getText());
                        user.setPasswort(hash.createHashCode((String)BenutzerAnlegenAO.this.passswort1.getText()));
                        user.setAdmin(tabBer.getID(berechtigungsgruppe.getSelectedItem().toString()));
                        if (BenutzerAnlegenAO.this.adminbereichZuweisen.isSelected()) {
                            user.setUsergruppe("admin");
                        } else {
                            user.setUsergruppe("benutzer");
                        }
                        user.setDeaktiv(0);
                        tabUser.insert(user);
                        BenutzerAnlegenAO.this.benutzer.addItem(BenutzerAnlegenAO.this.benutzername.getText());
                        BenutzerAnlegenAO.this.pass_benutzer.addItem(BenutzerAnlegenAO.this.benutzername.getText());
                        berechtigungsgruppe.setSelectedItem("<bitte w\u00e4hlen>");
                        BenutzerAnlegenAO.this.benutzername.setText(null);
                        BenutzerAnlegenAO.this.passswort1.setText(null);
                        BenutzerAnlegenAO.this.passswort2.setText(null);
                        BenutzerAnlegenAO.this.adminbereichZuweisen.setSelected(false);
                        logbuchEingabe.NeuerEintag("Benutzer wurde erstellt " + BenutzerAnlegenAO.this.benutzername.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    } else {
                        logging.logInfo((Object)"Passwort stimmt beim anlegen des Benutzers nicht \u00fcberein....");
                        JOptionPane.showMessageDialog(null, Konstante.PASSWORT_STIMMT_NICHT_UEBEREIN, "Fehlermeldung", 0);
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

