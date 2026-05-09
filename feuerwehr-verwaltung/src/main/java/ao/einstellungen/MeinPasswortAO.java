/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.hash
 */
package ao.einstellungen;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleUser;
import go.User;
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
import javax.swing.JPasswordField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.Utils;
import utilities.hash;
import utilities.logbuchEingabe;

public class MeinPasswortAO
extends AbstractFenster {
    private static final long serialVersionUID = 4209795687757534729L;
    private JButton buttonSpeichern3;
    private JButton buttonZurueck;
    private JLabel pass_benutzer_label;
    private JLabel pass_altesPasswort_label;
    private JLabel pass_neuesPasswort_label;
    private JLabel pass_neuesPasswort2_label;
    private JComboBox<String> pass_benutzer;
    private JPasswordField pass_altesPasswort;
    private JPasswordField pass_neuesPasswort;
    private JPasswordField pass_neuesPasswort2;
    private JPanel panelPasswort\u00c4nderung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public MeinPasswortAO() {
        super("FeuerwehrManagementSystem - Passwort");
        logging.logInfo((Object)"Starte: MeinPasswortAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern3 = new JButton("Mein Passwort \u00e4ndern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.pass_altesPasswort = new JPasswordField(20);
        this.pass_neuesPasswort = new JPasswordField(20);
        this.pass_neuesPasswort2 = new JPasswordField(20);
        this.pass_altesPasswort_label = new JLabel("Altes Passwort: ");
        this.pass_neuesPasswort_label = new JLabel("Neues Passwort: ");
        this.pass_neuesPasswort2_label = new JLabel("Neues Passwort wdh.: ");
        this.pass_benutzer_label = new JLabel("Benutzername");
        this.modulBeschreibung = new JLabel("Mein Passwort \u00e4ndern");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        try {
            TabelleUser tabUser = new TabelleUser();
            String[] userliste = Utils.listToArrayOnlyFORComboBoxes(tabUser.getUserListe());
            this.pass_benutzer = new JComboBox<String>(userliste);
            this.pass_benutzer.setSelectedItem(runApplication.loginName);
            this.pass_benutzer.setEnabled(false);
            if (runApplication.loginName.equals("MASTER_USER_FMS")) {
                logging.logInfo((Object)"MASTERUSER ist angemeldet zum Passwortwechsel");
                this.pass_benutzer.setSelectedItem("admin");
                this.pass_altesPasswort.setText(tabUser.getPasswortForMASTERUSER());
                this.pass_altesPasswort.setEditable(false);
                this.buttonZurueck.setVisible(false);
            }
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
        this.setSize(500, 270);
        this.setTitle("FeuerwehrManagementSystem - Passwortverwaltung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelPasswort\u00c4nderung = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", this.panelPasswort\u00c4nderung);
        this.panelPasswort\u00c4nderung.add(this.pass_benutzer_label);
        this.panelPasswort\u00c4nderung.add(this.pass_benutzer);
        this.panelPasswort\u00c4nderung.add(this.pass_altesPasswort_label);
        this.panelPasswort\u00c4nderung.add(this.pass_altesPasswort);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort_label);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort2_label);
        this.panelPasswort\u00c4nderung.add(this.pass_neuesPasswort2);
        this.add(this.buttonSpeichern3);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern3.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleUser tabUser = new TabelleUser();
                try {
                    User user = tabUser.get(MeinPasswortAO.this.pass_benutzer.getSelectedItem().toString());
                    if (!MeinPasswortAO.this.pass_altesPasswort.getText().equals(hash.decodeHashCode((String)user.getPasswort()))) {
                        JOptionPane.showMessageDialog(null, Konstante.ALTE_PASSWORT_FALSCH, "Fehlermeldung", 0);
                    } else if (!MeinPasswortAO.this.pass_neuesPasswort.getText().equals(MeinPasswortAO.this.pass_neuesPasswort2.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.NEUE_PASSWORT_FALSCH, "Fehlermeldung", 0);
                    } else {
                        tabUser.updatepasswort(MeinPasswortAO.this.pass_benutzer.getSelectedItem().toString(), hash.createHashCode((String)MeinPasswortAO.this.pass_neuesPasswort.getText()));
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        logbuchEingabe.NeuerEintag("Passwort ge\u00e4ndert");
                        MeinPasswortAO.this.pass_neuesPasswort.setText(null);
                        MeinPasswortAO.this.pass_neuesPasswort2.setText(null);
                        MeinPasswortAO.this.pass_altesPasswort.setText(null);
                        if (runApplication.loginName.equals("MASTER_USER_FMS")) {
                            logbuchEingabe.NeuerEintag("MASTER_USER_FMS hat das Passwort des Admins ge\u00e4ndert!");
                            logging.logInfo((Object)"MASTER_USER_FMS hat das Passwort des Admins ge\u00e4ndert!");
                            JOptionPane.showMessageDialog(null, Konstante.NEUE_PASSWORT_MASTERUSER);
                            System.exit(0);
                        }
                    }
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

