/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.MyProperties
 *  utilities.RandomGenerator
 *  utilities.RandomGenerator$Mode
 *  utilities.hash
 */
package ao.einstellungen;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import data.DatenbankZugriff;
import data.DatenbankZugriffMySQL;
import data.DatenbankZugriffMySQL2;
import data.tabellen.einstellungen.CreateDatabase;
import data.tabellen.einstellungen.TabelleEinstellungen;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleKeyStore;
import data.tabellen.einstellungen.TabelleMandant;
import go.Mandant;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import listener.BeendenListener;
import listener.HilfeListener;
import logging.logging;
import run.runApplication;
import run.update.Update;
import service.DBInstallService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.MyProperties;
import utilities.RandomGenerator;
import utilities.hash;

public class GrundkonfigurationAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonBeenden;
    private JButton buttonStart;
    private JButton buttonSpeichern;
    private JButton buttonEinstellungen;
    private JButton buttonBenutzerverwaltung;
    private JButton buttonUpdate;
    private JButton buttonHilfe;
    private JTextField clientID;
    public static JTextField datenbankName;
    public static JTextField datenbankIP;
    public static JTextField datenbankPort;
    public static JTextField datenbankUser;
    public static JPasswordField datenbankPasswort;
    private JPasswordField datenbankpasswort2;
    private JCheckBox datenbankInstallation;
    private JComboBox<String> bundesland;
    public static JComboBox<String> installationsTyp;
    private JCheckBox tabellenAnlegen;
    private JComboBox<String> organisation;
    private JTextField mandantName;
    private JCheckBox bf;
    private JTextField mandantID;
    private JLabel clientID_label;
    private JLabel datenbankName_label;
    private JLabel datenbankIP_label;
    private JLabel datenbankPort_label;
    private JLabel datenbankUser_label;
    private JLabel datenbankPasswort_label;
    private JLabel datenbankPasswort2_label;
    private JLabel datenbanktyp_label;
    private JLabel organisation_label;
    private JTextArea information;
    private JLabel mandantName_label;
    private JLabel bf_label;
    private JLabel mandantID_label;
    private JLabel datenbankInstallation_label;
    private JLabel tabellenAnlegen_label;
    private JLabel bundesland_label;
    private JLabel installationsTyp_label;
    private JRadioButton datenbanktyp;
    private JTextField benutzer;
    public static JPasswordField adminPasswort;
    private JPasswordField adminPasswort2;
    private JLabel benutzer_label;
    private JLabel passwort_label;
    private JLabel passwort2_label;
    public static JTextField sshServerAdresse;
    public static JTextField sshServerPort;
    public static JTextField sshServerUser;
    public static JTextField sshServerTunnel;
    public static JPasswordField sshServerPasswort;
    private JPasswordField sshServerPasswort2;
    private JLabel sshServerAdresse_label;
    private JLabel sshServerPort_label;
    private JLabel sshServerUser_label;
    private JLabel sshServerTunnel_label;
    private JLabel sshServerPasswort_label;
    private JLabel sshServerPasswort2_label;
    private JTextField ftpServerAdresse;
    private JTextField ftpServerPort;
    private JTextField ftpServerUser;
    private JPasswordField ftpServerPasswort;
    private JPasswordField ftpServerPasswort2;
    private JCheckBox ftpUploadAktivieren;
    private JLabel ftpServerAdresse_label;
    private JLabel ftpServerPort_label;
    private JLabel ftpServerUser_label;
    private JLabel ftpServerPasswort_label;
    private JLabel ftpServerPasswort2_label;
    private JLabel ftpUploadAktivieren_label;
    private JTabbedPane tabPane;
    private JPanel panelMainKonfig;
    private JPanel panelAdminPassword;
    private JPanel panelDBKonfig;
    private JPanel panelFTPKonfig;
    private JPanel panelSSHKonfig;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public GrundkonfigurationAO() {
        super("FeuerwehrManagementSystem - Grundkonfiguration");
        logging.logInfo((Object)"Starte: GrundkonfigurationAO");
    }

    protected void buttonErstellen() {
        this.buttonBeenden = new JButton("Programm beenden");
        this.buttonStart = new JButton("Programm Starten");
        this.buttonBeenden.setToolTipText("Programm beenden");
        this.buttonEinstellungen = new JButton("Programmeinstellungen");
        this.buttonSpeichern = new JButton("Datenbank Erstellen");
        this.buttonBenutzerverwaltung = new JButton("Benutzerverwaltung");
        this.buttonUpdate = new JButton("Auf Update pr\u00fcfen");
        this.buttonHilfe = new JButton("Hilfe");
        this.clientID = new JTextField(RandomGenerator.generate((int)20, (RandomGenerator.Mode)RandomGenerator.Mode.ALPHANUMERIC), 20);
        datenbankIP = new JTextField("127.0.0.1", 20);
        datenbankPort = new JTextField("3306", 20);
        datenbankName = new JTextField("feuerwehrmanagementsystem", 20);
        datenbankUser = new JTextField("root", 20);
        datenbankPasswort = new JPasswordField();
        this.datenbankpasswort2 = new JPasswordField();
        this.datenbankInstallation = new JCheckBox();
        this.datenbankInstallation.setToolTipText("Wenn dieser Option markiert ist, ist keine externe Datenbank n\u00f6tig");
        this.tabellenAnlegen = new JCheckBox();
        this.tabellenAnlegen.setToolTipText("Hier kann das Anlegen der Tabellen ausgeschaltet werden, diese Option wird nur genutzt wenn die Datenbank bereits angelegt ist");
        this.mandantName = new JTextField(20);
        this.mandantID = new JTextField(20);
        this.bf = new JCheckBox();
        this.bf.setToolTipText("Verbindet mehrere Mandanten IDs, damit Sie zentral verwaltet werden kann...");
        this.benutzer = new JTextField("admin", 20);
        adminPasswort = new JPasswordField();
        this.adminPasswort2 = new JPasswordField();
        this.benutzer_label = new JLabel("Administartor: ");
        this.passwort_label = new JLabel("Passwort: ");
        this.passwort2_label = new JLabel("Passwort wdh.: ");
        this.clientID_label = new JLabel("Arbeitsplatz ID: ");
        this.datenbankIP_label = new JLabel("Datenbank IP: ");
        this.datenbankPort_label = new JLabel("Datenbank Port: ");
        this.datenbankName_label = new JLabel("Datenbank Name: ");
        this.datenbankPasswort_label = new JLabel("Datenbank User: ");
        this.datenbankPasswort_label = new JLabel("Passwort: ");
        this.datenbankPasswort2_label = new JLabel("Passwort wdh. .:");
        this.datenbankUser_label = new JLabel("Datenbank User: ");
        this.datenbanktyp_label = new JLabel("Datenbanktyp: ");
        this.datenbankInstallation_label = new JLabel("MySQL Datenbank installieren: ");
        this.bundesland_label = new JLabel("Bundesland: ");
        this.tabellenAnlegen_label = new JLabel("Datenbank Tabellen erstellen (Standard):       ");
        this.installationsTyp_label = new JLabel("Nutzungstyp: ");
        this.organisation_label = new JLabel("Organisationstyp: ");
        this.mandantName_label = new JLabel("Name der Einheit (Mandant): ");
        this.mandantID_label = new JLabel("Vorhandene MandantID: ");
        this.bf_label = new JLabel("Verwaltungsbeh\u00f6rde (z.B. BF): ");
        this.bf_label.setToolTipText("Verbindet mehrere Mandanten IDs, damit Sie zentral verwaltet werden kann...");
        String[] bundeslandListe = new String[]{"<bitte w\u00e4hlen>", "Baden-W\u00fcrttemberg", "Bayern", "Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen", "Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Th\u00fcringen"};
        String[] typen = new String[]{"Einzelarbeitsplatz", "Serverinstallation", "Neuen Mandant erstellen", "Neuen Mandant erstellen (Serverinstallation)", "Neuen Client anlegen (vorhandener Mandat)"};
        String[] organisationsListe = new String[]{"Feuerwehr", "HiOrg", "THW"};
        this.bundesland = new JComboBox<String>(bundeslandListe);
        installationsTyp = new JComboBox<String>(typen);
        this.organisation = new JComboBox<String>(organisationsListe);
        this.datenbanktyp = new JRadioButton("MySQL5");
        this.information = new JTextArea(10, 90);
        this.information.setEditable(false);
        this.information.setText("HINWEIS ZUR INSTALLATION:\n\nDas Programm ist f\u00fcr eine Bildschirmaufl\u00f6sungen ab 1440x900 optimiert!Die MySQL Datenbank kann vom Programm automatisch installiert werden oder es kann\neine vorhandene MySQL Datenbank verwendet werden. Wenn Sie bereits eine Datenbankauf Ihrem Rechner installiert haben, demakieren Sie bitte \"MySQL Datenbank installieren\"\nund geben Sie Ihre demendsprechenden Verbindunsparameter ein.\n\n\u00dcber die Einstellung \"Serverinstallation\" k\u00f6nnen Sie das Programm f\u00fcr mehrere Nutzer an\nunterschiedlichen Standorten nutzbar machen. Hierzu muss aber eine FTP-Server und eineSSH-Server installiert werden. Die Konfiguration wird nicht vom Programm \u00fcbernommen.\nDie Installationsanleitung / -dateien befinden sich aber im \"Install\" Verzeichnis.\nUm das FeuerwehrManagementSystem von mehreren Einheiten nutzen zu k\u00f6nnen, k\u00f6nnen sie auf der\ngleichen Datenbank mehrere Mandanten anlegen. Dies k\u00f6nnen dann gemeinsam Verwaltet werden.");
        sshServerAdresse = new JTextField(20);
        sshServerPort = new JTextField("22", 20);
        sshServerTunnel = new JTextField("63000", 20);
        sshServerUser = new JTextField(20);
        sshServerPasswort = new JPasswordField(20);
        this.sshServerPasswort2 = new JPasswordField(20);
        this.sshServerAdresse_label = new JLabel("SSH Server Adresse: ");
        this.sshServerPort_label = new JLabel("SSH Server Port: ");
        this.sshServerTunnel_label = new JLabel("SSH Tunnelport: ");
        this.sshServerUser_label = new JLabel("SSH Benutzer: ");
        this.sshServerPasswort_label = new JLabel("SSH Passwort: ");
        this.sshServerPasswort2_label = new JLabel("SSH Passwort wdh.: ");
        this.ftpServerAdresse = new JTextField(20);
        this.ftpServerPort = new JTextField("21", 20);
        this.ftpServerUser = new JTextField(20);
        this.ftpServerPasswort = new JPasswordField(20);
        this.ftpServerPasswort2 = new JPasswordField(20);
        this.ftpUploadAktivieren = new JCheckBox();
        this.ftpServerAdresse_label = new JLabel("FTP Server Adresse: ");
        this.ftpServerPort_label = new JLabel("FTP Server Port: ");
        this.ftpServerUser_label = new JLabel("FTP Benutzer: ");
        this.ftpServerPasswort_label = new JLabel("FTP Passwort: ");
        this.ftpServerPasswort2_label = new JLabel("FTP Passwort wdh.: ");
        this.ftpUploadAktivieren_label = new JLabel("Aktiviere FTP Upload / FTP Datensicherung: ");
        this.tabPane = new JTabbedPane();
        this.modulBeschreibung = new JLabel("Grundkonfiguration");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        this.datenbankInstallation.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (GrundkonfigurationAO.this.datenbankInstallation.isSelected()) {
                    datenbankIP.setText("127.0.0.1");
                    datenbankPort.setText("3306");
                    datenbankName.setText("feuerwehrmanagementsystem");
                    datenbankUser.setText("root");
                    datenbankPasswort.setText("root_fms");
                    GrundkonfigurationAO.this.datenbankpasswort2.setText("root_fms");
                    GrundkonfigurationAO.this.tabellenAnlegen.setSelected(true);
                    GrundkonfigurationAO.this.bundesland.setEnabled(true);
                    datenbankIP.setEnabled(false);
                    datenbankPort.setEnabled(false);
                    datenbankName.setEnabled(false);
                    datenbankUser.setEnabled(false);
                    datenbankPasswort.setEnabled(false);
                    GrundkonfigurationAO.this.datenbankpasswort2.setEnabled(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setEnabled(false);
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Datenbank Installieren");
                } else {
                    datenbankPasswort.setText(null);
                    GrundkonfigurationAO.this.datenbankpasswort2.setText(null);
                    GrundkonfigurationAO.this.bundesland.setEnabled(true);
                    GrundkonfigurationAO.this.tabellenAnlegen.setSelected(true);
                    datenbankIP.setEnabled(true);
                    datenbankPort.setEnabled(true);
                    datenbankName.setEnabled(true);
                    datenbankUser.setEnabled(true);
                    datenbankPasswort.setEnabled(true);
                    GrundkonfigurationAO.this.datenbankpasswort2.setEnabled(true);
                    GrundkonfigurationAO.this.tabellenAnlegen.setEnabled(true);
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Datenbank Erstellen");
                }
            }
        });
        this.tabellenAnlegen.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (GrundkonfigurationAO.this.tabellenAnlegen.isSelected()) {
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Datenbank Erstellen");
                    GrundkonfigurationAO.this.bundesland.setEnabled(true);
                    GrundkonfigurationAO.this.tabPane.addTab("Administrator Passwort", GrundkonfigurationAO.this.panelAdminPassword);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                } else if (installationsTyp.getSelectedItem().toString().equals("Serverinstallation")) {
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Konfiguration Erstellen");
                    GrundkonfigurationAO.this.bundesland.setEnabled(false);
                    GrundkonfigurationAO.this.tabPane.remove(GrundkonfigurationAO.this.panelAdminPassword);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                } else {
                    GrundkonfigurationAO.this.bundesland.setEnabled(true);
                }
            }
        });
        installationsTyp.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                GrundkonfigurationAO.this.buttonSpeichern.setVisible(true);
                GrundkonfigurationAO.this.mandantName.setVisible(true);
                GrundkonfigurationAO.this.mandantName_label.setVisible(true);
                GrundkonfigurationAO.this.mandantID.setVisible(false);
                GrundkonfigurationAO.this.mandantID_label.setVisible(false);
                GrundkonfigurationAO.this.bundesland.setEnabled(true);
                GrundkonfigurationAO.this.tabPane.removeTabAt(2);
                if (!installationsTyp.getSelectedItem().toString().equals("Neuen Client anlegen (vorhandener Mandat)")) {
                    GrundkonfigurationAO.this.tabPane.addTab("Administrator Passwort", GrundkonfigurationAO.this.panelAdminPassword);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                }
                if (installationsTyp.getSelectedItem().toString().equals("Serverinstallation")) {
                    GrundkonfigurationAO.this.tabPane.addTab("SSH-Server", GrundkonfigurationAO.this.panelSSHKonfig);
                    GrundkonfigurationAO.this.ftpUploadAktivieren.setSelected(true);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                    GrundkonfigurationAO.this.bundesland.setSelectedItem("<bitte w\u00e4hlen>");
                    GrundkonfigurationAO.this.datenbankInstallation.setSelected(false);
                    GrundkonfigurationAO.this.datenbankInstallation.setEnabled(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setSelected(true);
                    GrundkonfigurationAO.this.tabellenAnlegen.setEnabled(true);
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Einstellungen speichern");
                } else if (installationsTyp.getSelectedItem().toString().equals("Einzelarbeitsplatz")) {
                    GrundkonfigurationAO.this.tabPane.remove(GrundkonfigurationAO.this.panelSSHKonfig);
                    GrundkonfigurationAO.this.ftpUploadAktivieren.setSelected(false);
                    GrundkonfigurationAO.this.ftpUploadAktivieren.setEnabled(true);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                    GrundkonfigurationAO.this.datenbankInstallation.setEnabled(true);
                    GrundkonfigurationAO.this.tabellenAnlegen.setEnabled(true);
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Einstellungen speichern");
                } else if (installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen")) {
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Mandanten speichern");
                    GrundkonfigurationAO.this.tabPane.remove(GrundkonfigurationAO.this.panelSSHKonfig);
                    GrundkonfigurationAO.this.datenbankInstallation.setSelected(false);
                    GrundkonfigurationAO.this.datenbankInstallation.setEnabled(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setSelected(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setEnabled(false);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Neuen Mandanten erstellen");
                } else if (installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen (Serverinstallation)")) {
                    GrundkonfigurationAO.this.tabPane.addTab("SSH-Server", GrundkonfigurationAO.this.panelSSHKonfig);
                    GrundkonfigurationAO.this.datenbankInstallation.setSelected(false);
                    GrundkonfigurationAO.this.datenbankInstallation.setEnabled(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setSelected(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setEnabled(false);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Neuen Mandanten erstellen");
                } else if (installationsTyp.getSelectedItem().toString().equals("Neuen Client anlegen (vorhandener Mandat)")) {
                    GrundkonfigurationAO.this.mandantName.setVisible(false);
                    GrundkonfigurationAO.this.mandantName_label.setVisible(false);
                    GrundkonfigurationAO.this.mandantID.setVisible(true);
                    GrundkonfigurationAO.this.mandantID_label.setVisible(true);
                    GrundkonfigurationAO.this.bundesland.setSelectedItem("<bitte w\u00e4hlen>");
                    GrundkonfigurationAO.this.bundesland.setEnabled(false);
                    GrundkonfigurationAO.this.tabPane.addTab("SSH-Server", GrundkonfigurationAO.this.panelSSHKonfig);
                    GrundkonfigurationAO.this.datenbankInstallation.setSelected(false);
                    GrundkonfigurationAO.this.datenbankInstallation.setEnabled(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setSelected(false);
                    GrundkonfigurationAO.this.tabellenAnlegen.setEnabled(false);
                    GrundkonfigurationAO.this.repaint();
                    GrundkonfigurationAO.this.validate();
                    GrundkonfigurationAO.this.buttonSpeichern.setVisible(true);
                    GrundkonfigurationAO.this.buttonSpeichern.setText("Einstellungen speichern");
                }
            }
        });
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Grundkonfiguration");
        this.setSize(1100, 580);
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
    }

    protected void boxenHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.information);
        this.panelMainKonfig = new JPanel(new GridLayout(10, 2));
        this.getContentPane().add("Center", this.panelMainKonfig);
        this.panelMainKonfig.add(this.installationsTyp_label);
        this.panelMainKonfig.add(installationsTyp);
        this.panelMainKonfig.add(this.datenbanktyp_label);
        this.panelMainKonfig.add(this.datenbanktyp);
        this.panelMainKonfig.add(this.datenbankInstallation_label);
        this.panelMainKonfig.add(this.datenbankInstallation);
        this.panelMainKonfig.add(this.tabellenAnlegen_label);
        this.panelMainKonfig.add(this.tabellenAnlegen);
        this.panelMainKonfig.add(this.clientID_label);
        this.panelMainKonfig.add(this.clientID);
        this.panelMainKonfig.add(this.bundesland_label);
        this.panelMainKonfig.add(this.bundesland);
        this.panelMainKonfig.add(this.organisation_label);
        this.panelMainKonfig.add(this.organisation);
        this.panelMainKonfig.add(this.bf_label);
        this.panelMainKonfig.add(this.bf);
        this.panelMainKonfig.add(this.mandantName_label);
        this.panelMainKonfig.add(this.mandantName);
        this.panelMainKonfig.add(this.mandantID_label);
        this.panelMainKonfig.add(this.mandantID);
        this.panelAdminPassword = new JPanel(new GridLayout(6, 2));
        this.getContentPane().add("Center", this.panelAdminPassword);
        this.panelAdminPassword.add(this.benutzer_label);
        this.panelAdminPassword.add(this.benutzer);
        this.panelAdminPassword.add(this.passwort_label);
        this.panelAdminPassword.add(adminPasswort);
        this.panelAdminPassword.add(this.passwort2_label);
        this.panelAdminPassword.add(this.adminPasswort2);
        this.panelDBKonfig = new JPanel(new GridLayout(6, 2));
        this.getContentPane().add("Center", this.panelDBKonfig);
        this.panelDBKonfig.add(this.datenbankName_label);
        this.panelDBKonfig.add(datenbankName);
        this.panelDBKonfig.add(this.datenbankIP_label);
        this.panelDBKonfig.add(datenbankIP);
        this.panelDBKonfig.add(this.datenbankPort_label);
        this.panelDBKonfig.add(datenbankPort);
        this.panelDBKonfig.add(this.datenbankUser_label);
        this.panelDBKonfig.add(datenbankUser);
        this.panelDBKonfig.add(this.datenbankPasswort_label);
        this.panelDBKonfig.add(datenbankPasswort);
        this.panelDBKonfig.add(this.datenbankPasswort2_label);
        this.panelDBKonfig.add(this.datenbankpasswort2);
        this.panelSSHKonfig = new JPanel(new GridLayout(6, 2));
        this.panelSSHKonfig.add(this.sshServerAdresse_label);
        this.panelSSHKonfig.add(sshServerAdresse);
        this.panelSSHKonfig.add(this.sshServerPort_label);
        this.panelSSHKonfig.add(sshServerPort);
        this.panelSSHKonfig.add(this.sshServerTunnel_label);
        this.panelSSHKonfig.add(sshServerTunnel);
        this.panelSSHKonfig.add(this.sshServerUser_label);
        this.panelSSHKonfig.add(sshServerUser);
        this.panelSSHKonfig.add(this.sshServerPasswort_label);
        this.panelSSHKonfig.add(sshServerPasswort);
        this.panelSSHKonfig.add(this.sshServerPasswort2_label);
        this.panelSSHKonfig.add(this.sshServerPasswort2);
        this.panelFTPKonfig = new JPanel(new GridLayout(6, 2));
        this.panelFTPKonfig.add(this.ftpUploadAktivieren_label);
        this.panelFTPKonfig.add(this.ftpUploadAktivieren);
        this.panelFTPKonfig.add(this.ftpServerAdresse_label);
        this.panelFTPKonfig.add(this.ftpServerAdresse);
        this.panelFTPKonfig.add(this.ftpServerPort_label);
        this.panelFTPKonfig.add(this.ftpServerPort);
        this.panelFTPKonfig.add(this.ftpServerUser_label);
        this.panelFTPKonfig.add(this.ftpServerUser);
        this.panelFTPKonfig.add(this.ftpServerPasswort_label);
        this.panelFTPKonfig.add(this.ftpServerPasswort);
        this.panelFTPKonfig.add(this.ftpServerPasswort2_label);
        this.panelFTPKonfig.add(this.ftpServerPasswort2);
        this.tabPane.addTab("Datenbank", this.panelDBKonfig);
        this.tabPane.addTab("Administrator Passwort", this.panelAdminPassword);
        this.tabPane.addTab("FTP-Server", this.panelFTPKonfig);
        this.tabPane.setPreferredSize(new Dimension(500, 180));
        this.add(this.tabPane);
        this.add(this.dummy2);
        this.add(this.buttonSpeichern);
        this.add(this.buttonEinstellungen);
        this.add(this.buttonBenutzerverwaltung);
        this.add(this.buttonUpdate);
        this.add(this.buttonHilfe);
        this.add(this.buttonStart);
        this.add(this.buttonBeenden);
        this.datenbankInstallation.setSelected(true);
        this.buttonEinstellungen.setEnabled(false);
        this.buttonBenutzerverwaltung.setEnabled(false);
        this.datenbanktyp.setSelected(true);
        this.datenbanktyp.setEnabled(false);
        this.buttonStart.setEnabled(false);
        this.clientID.setEditable(false);
        this.tabellenAnlegen.setSelected(true);
        this.benutzer.setEditable(false);
        this.mandantID.setVisible(false);
        this.mandantID_label.setVisible(false);
    }

    protected void labelErstellen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                if (runApplication.verarbeitungL\u00e4uft == 1) {
                    JOptionPane.showMessageDialog(null, Konstante.VERARBEITUNG_L\u00c4UFT, "Warnung", 2);
                } else {
                    logging.logInfo((Object)"Progarmm wird beendet...");
                    System.exit(0);
                }
            }
        });
    }

    protected void actionErzeugen() {
        this.buttonBeenden.addActionListener((ActionListener)((Object)new BeendenListener((JFrame)((Object)this))));
        this.buttonHilfe.addActionListener((ActionListener)((Object)new HilfeListener((JFrame)((Object)this))));
        this.buttonUpdate.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.checkForUpdate();
            }
        });
        this.buttonEinstellungen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.EINSTELLUNGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonBenutzerverwaltung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BENUTZER_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonStart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    runApplication.prepareStart(new String[0], runApplication.arbeitsverzeichnis);
                    GrundkonfigurationAO.this.dispose();
                }
                catch (Exception e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (GrundkonfigurationAO.this.mandantName.getText().equals("") && GrundkonfigurationAO.this.mandantName.isVisible()) {
                    GrundkonfigurationAO.this.mandantName.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.MANDANTNAME_FEHLT, "Fehlermeldung", 0);
                } else if (installationsTyp.getSelectedItem().toString().equals("Neuen Client anlegen (vorhandener Mandat)") && GrundkonfigurationAO.this.mandantID.getText().equals("") | GrundkonfigurationAO.this.mandantID.getText() == null) {
                    GrundkonfigurationAO.this.mandantID.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.MANDANT_ID_FEHLT, "Warnung", 2);
                } else if (GrundkonfigurationAO.this.bundesland.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && GrundkonfigurationAO.this.tabellenAnlegen.isSelected() | installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen") | installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen (Serverinstallation)")) {
                    GrundkonfigurationAO.this.bundesland.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_BUNDESLAND_WAEHLEN, "Warnung", 2);
                } else if (!DBInstallService.checkFolderPermission()) {
                    JOptionPane.showMessageDialog(null, Konstante.KEINE_WINDOWS_SCHREIBBERECHTIGUNG, "Fehlermeldung", 0);
                } else if (installationsTyp.getSelectedItem().toString().equals("Serverinstallation") | installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen (Serverinstallation)") && !sshServerPasswort.getText().equals(GrundkonfigurationAO.this.sshServerPasswort2.getText())) {
                    sshServerPasswort.setBackground(Color.red);
                    GrundkonfigurationAO.this.sshServerPasswort2.setBackground(Color.red);
                    GrundkonfigurationAO.this.tabPane.setSelectedIndex(3);
                    JOptionPane.showMessageDialog(null, Konstante.PASSWORT_STIMMT_NICHT_UEBEREIN, "Warnung", 2);
                } else if (GrundkonfigurationAO.this.ftpUploadAktivieren.isSelected() && !GrundkonfigurationAO.this.ftpServerPasswort.getText().equals(GrundkonfigurationAO.this.ftpServerPasswort2.getText())) {
                    GrundkonfigurationAO.this.ftpServerPasswort.setBackground(Color.red);
                    GrundkonfigurationAO.this.ftpServerPasswort2.setBackground(Color.red);
                    GrundkonfigurationAO.this.tabPane.setSelectedIndex(1);
                    JOptionPane.showMessageDialog(null, Konstante.PASSWORT_STIMMT_NICHT_UEBEREIN, "Warnung", 2);
                } else if (adminPasswort.getText().equals("") && GrundkonfigurationAO.this.adminPasswort2.getText().equals("") && GrundkonfigurationAO.this.tabellenAnlegen.isSelected() | installationsTyp.getSelectedItem().toString().startsWith("Neuen Mandant erstellen")) {
                    adminPasswort.setBackground(Color.red);
                    GrundkonfigurationAO.this.adminPasswort2.setBackground(Color.red);
                    GrundkonfigurationAO.this.tabPane.setSelectedIndex(2);
                    JOptionPane.showMessageDialog(null, Konstante.PASSWORT_NICHT_VERGEBEN, "Warnung", 2);
                } else if (!adminPasswort.getText().equals(GrundkonfigurationAO.this.adminPasswort2.getText()) && GrundkonfigurationAO.this.tabellenAnlegen.isSelected() | installationsTyp.getSelectedItem().toString().startsWith("Neuen Mandant erstellen")) {
                    adminPasswort.setBackground(Color.red);
                    GrundkonfigurationAO.this.adminPasswort2.setBackground(Color.red);
                    GrundkonfigurationAO.this.tabPane.setSelectedIndex(2);
                    JOptionPane.showMessageDialog(null, Konstante.PASSWORT_STIMMT_NICHT_UEBEREIN, "Warnung", 2);
                } else if (!datenbankPasswort.getText().equals(GrundkonfigurationAO.this.datenbankpasswort2.getText())) {
                    datenbankPasswort.setBackground(Color.red);
                    GrundkonfigurationAO.this.datenbankpasswort2.setBackground(Color.red);
                    GrundkonfigurationAO.this.tabPane.setSelectedIndex(0);
                    logging.logInfo((Object)"Das Passwort stimmt nicht \u00fcberein");
                    JOptionPane.showMessageDialog(null, Konstante.PASSWORT_STIMMT_NICHT_UEBEREIN, "Warnung", 2);
                } else {
                    GrundkonfigurationAO.this.mandantName.setBackground(Color.white);
                    GrundkonfigurationAO.this.mandantID.setBackground(Color.white);
                    GrundkonfigurationAO.this.bundesland.setBackground(Color.white);
                    sshServerPasswort.setBackground(Color.white);
                    GrundkonfigurationAO.this.sshServerPasswort2.setBackground(Color.white);
                    GrundkonfigurationAO.this.ftpServerPasswort.setBackground(Color.white);
                    GrundkonfigurationAO.this.ftpServerPasswort2.setBackground(Color.white);
                    adminPasswort.setBackground(Color.white);
                    GrundkonfigurationAO.this.adminPasswort2.setBackground(Color.white);
                    datenbankPasswort.setBackground(Color.white);
                    GrundkonfigurationAO.this.datenbankpasswort2.setBackground(Color.white);
                    Thread threadDBErstellen = new Thread(){

                        @Override
                        public void run() {
                            try {
                                CreateDatabase datenbankerstelen = new CreateDatabase();
                                MyProperties programmProperties = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties");
                                Mandant mandant = new Mandant();
                                mandant.setName(GrundkonfigurationAO.this.mandantName.getText());
                                mandant.setBf(GrundkonfigurationAO.this.bf.isSelected() ? 1 : 0);
                                if (GrundkonfigurationAO.this.tabellenAnlegen.isSelected()) {
                                    mandant.setId(1);
                                } else if (installationsTyp.getSelectedItem().toString().equals("Neuen Client anlegen (vorhandener Mandat)")) {
                                    mandant.setId(Integer.parseInt(GrundkonfigurationAO.this.mandantID.getText()));
                                } else {
                                    mandant.setId(new TabelleMandant().getNextNummer());
                                }
                                programmProperties.putVar("DatenbankIP", (Object)datenbankIP.getText());
                                programmProperties.putVar("DatenbankPort", (Object)datenbankPort.getText());
                                programmProperties.putVar("DatenbankUser", (Object)hash.createHashCode((String)datenbankUser.getText()));
                                programmProperties.putVar("DatenbankPasswort", (Object)hash.createHashCode((String)datenbankPasswort.getText()));
                                programmProperties.putVar("DatenbankName", (Object)datenbankName.getText());
                                programmProperties.putVar("ClientID", (Object)GrundkonfigurationAO.this.clientID.getText());
                                programmProperties.putVar("logmax", (Object)"100000");
                                programmProperties.putVar("Organisation", (Object)GrundkonfigurationAO.this.organisation.getSelectedItem().toString());
                                programmProperties.putVar("FTPServer", (Object)GrundkonfigurationAO.this.ftpServerAdresse.getText());
                                programmProperties.putVar("FTPPort", (Object)GrundkonfigurationAO.this.ftpServerPort.getText());
                                programmProperties.putVar("FTPUser", (Object)hash.createHashCode((String)GrundkonfigurationAO.this.ftpServerUser.getText()));
                                programmProperties.putVar("FTPPasswort", (Object)hash.createHashCode((String)GrundkonfigurationAO.this.ftpServerPasswort.getText()));
                                if (GrundkonfigurationAO.this.ftpUploadAktivieren.isSelected()) {
                                    programmProperties.putVar("FTPUploadActiv", (Object)"true");
                                } else {
                                    programmProperties.putVar("FTPUploadActiv", (Object)"false");
                                }
                                programmProperties.putVar("SSHServer", (Object)sshServerAdresse.getText());
                                programmProperties.putVar("SSHServerPort", (Object)sshServerPort.getText());
                                programmProperties.putVar("SSHTunnel", (Object)sshServerTunnel.getText());
                                programmProperties.putVar("SSHUser", (Object)hash.createHashCode((String)sshServerUser.getText()));
                                programmProperties.putVar("SSHPasswort", (Object)hash.createHashCode((String)sshServerPasswort.getText()));
                                programmProperties.putVar("MandantID", (Object)Integer.toString(mandant.getId()));
                                programmProperties.putVar("BlobActiv", (Object)"false");
                                if (GrundkonfigurationAO.this.ftpUploadAktivieren.isSelected()) {
                                    programmProperties.putVar("FTPUploadActiv", (Object)"true");
                                } else {
                                    programmProperties.putVar("FTPUploadActiv", (Object)"false");
                                }
                                if (installationsTyp.getSelectedItem().toString().equals("Einzelarbeitsplatz") | installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen")) {
                                    programmProperties.putVar("DB_TYP", (Object)"Lokal");
                                } else {
                                    programmProperties.putVar("DB_TYP", (Object)"SSH");
                                }
                                programmProperties.saveVars();
                                logging.logInfo((Object)"Konfigurationsdatei wurde erfolgreich gespeichert / erstellt");
                                runApplication.PROPERTIES = runApplication.lesePropertieDatei(programmProperties);
                                logging.logInfo((Object)"Einstellungen wurden in Variablen geladen zur Verarbeitung...");
                                if (installationsTyp.getSelectedItem().toString().equals("Neuen Client anlegen (vorhandener Mandat)")) {
                                    logging.logInfo((Object)"Es wird nur die Client Installation ben\u00f6tigt. Beende Installation...");
                                    ProzessBarAO.progressbar.setValue(95);
                                    this.createDataFolder();
                                    GrundkonfigurationAO.this.buttonSpeichern.setEnabled(false);
                                    GrundkonfigurationAO.this.buttonStart.setEnabled(true);
                                    ProzessBarAO.progressbar.setValue(100);
                                    MyEvent.setEvent((String)"0x0030");
                                    this.stop();
                                }
                                MyProperties dbProperties = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/db.properties");
                                if (GrundkonfigurationAO.this.datenbankInstallation.isSelected() && !dbProperties.sourceFileExists()) {
                                    logging.logInfo((Object)"Datenbank wird per Skript installiert");
                                    DBInstallService.DBInstallAusfuehre();
                                }
                                if (GrundkonfigurationAO.this.tabellenAnlegen.isSelected() | installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen") | installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen (Serverinstallation)") | installationsTyp.getSelectedItem().toString().equals("Serverinstallation")) {
                                    int errorCode;
                                    ProzessBarAO.progressbar.setValue(0);
                                    ProzessBarAO.label_bitteWarten.setText("Datenbank-Tabellen werden erstellt... Bitte warten...");
                                    logging.logInfo((Object)"Datenbank wird erstellt");
                                    if (GrundkonfigurationAO.this.tabellenAnlegen.isSelected() && (errorCode = datenbankerstelen.createDatabase(datenbankName.getText())) == -1) {
                                        MyEvent.setEvent((String)"0x0030");
                                        this.stop();
                                    }
                                    datenbankerstelen.createTables(GrundkonfigurationAO.this.bundesland.getSelectedItem().toString(), mandant, GrundkonfigurationAO.this.clientID.getText());
                                    MyEvent.setEvent((String)"0x0030");
                                } else {
                                    logging.logInfo((Object)"Datenbank Tabellen werden nicht angelegt");
                                }
                                runApplication.PROPERTIES = runApplication.lesePropertieDatei(programmProperties);
                                logging.logInfo((Object)"Properties in Globale Variable gesetzt...");
                                TabelleKeyStore tabKeyStore = new TabelleKeyStore();
                                if (tabKeyStore.count("Nummer1", mandant.getId()) == 0 && tabKeyStore.count("Nummer2", mandant.getId()) == 0) {
                                    tabKeyStore.insert("Nummer1", hash.createHashCode((String)Long.toString(System.currentTimeMillis())), mandant.getId());
                                    tabKeyStore.insert("Nummer2", "", mandant.getId());
                                }
                                this.createDataFolder();
                                if (installationsTyp.getSelectedItem().toString().equals("Serverinstallation") | installationsTyp.getSelectedItem().toString().equals("Neuen Mandant erstellen (Serverinstallation)") && GrundkonfigurationAO.this.tabellenAnlegen.isSelected()) {
                                    logging.logInfo((Object)"Datenbank wird auf den neuen stand gebracht");
                                    Update.initUpdate();
                                }
                                logging.logInfo((Object)"Konfiguration Abgeschlossen");
                                JOptionPane.showMessageDialog(null, Konstante.ALLE_KONFIGURATIONEN_FERTIG);
                                GrundkonfigurationAO.this.buttonEinstellungen.setEnabled(true);
                                GrundkonfigurationAO.this.buttonBenutzerverwaltung.setEnabled(true);
                                GrundkonfigurationAO.this.buttonSpeichern.setEnabled(false);
                                GrundkonfigurationAO.this.buttonStart.setEnabled(true);
                                runApplication.EINSTELLUNGEN = new TabelleEinstellungen().getAllEinstellungen();
                                runApplication.EINSTELLUNGEN_GESPEICHERT = new TabelleEinstellungen_gespeichert().getAllEinstellungen();
                                Steuerung.setStatus(Status.EINSTELLUNGEN);
                                Steuerung.steuerung();
                            }
                            catch (Exception e) {
                                logging.logError((Object)"Bei der Konfiguration ist ein Fehler aufgetreten");
                                logging.logError((Object)"Einstellungen werden r\u00fcckg\u00e4ngig gemacht");
                                logging.logPrintStackTrace((Exception)e);
                                runApplication.verarbeitungL\u00e4uft = 0;
                                runApplication.PROPERTIES = null;
                                if (installationsTyp.getSelectedItem().toString().equals("Einzelarbeitsplatz")) {
                                    DatenbankZugriffMySQL.removeInstance();
                                    MyEvent.setEvent((String)"0x0030");
                                    if (GrundkonfigurationAO.this.tabellenAnlegen.isSelected() && GrundkonfigurationAO.this.datenbankInstallation.isSelected()) {
                                        CreateDatabase datenbankerstelen = new CreateDatabase();
                                        datenbankerstelen.dropDatabase(datenbankName.getText());
                                    }
                                    JOptionPane.showMessageDialog(null, "Es besteht keine Verbindung zur Datenbank\nBitte \u00fcberpr\u00fcfen Sie ihre Eingaben", "Fehlermeldung", 0);
                                } else {
                                    MyEvent.setEvent((String)"0x0030");
                                    DatenbankZugriff.removeInstance();
                                    DatenbankZugriffMySQL.removeInstance();
                                    DatenbankZugriffMySQL2.removeInstance();
                                    DatenbankZugriff.disconnectSSHServer();
                                    JOptionPane.showMessageDialog(null, Konstante.KEINE_VERBINDUNG_ZUR_DB, "Fehlermeldung", 0);
                                }
                                File properties = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/FeuerwehrManagementSystem.properties");
                                properties.delete();
                                GrundkonfigurationAO.this.buttonSpeichern.setEnabled(true);
                                this.stop();
                            }
                        }

                        private void createDataFolder() {
                            logging.logInfo((Object)"data Ordner wird erstellt");
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Templates").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Papierkorb").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Eigene Dateien").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Atemschutz").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Gesendet").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Entwurf").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Empfangende").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Temp").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Temp/original_nachricht").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Temp/original_nachricht_unwetter").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/DBBACKUP").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bestandsliste").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Abrechnung").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Ausbildungsunterlagen").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/gro\u00df").mkdir();
                            new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/klein").mkdir();
                        }
                    };
                    if (GrundkonfigurationAO.this.datenbankInstallation.isSelected()) {
                        int msgDB = JOptionPane.showConfirmDialog(null, Konstante.LETZER_HINWEIS_DBINSTALLATION, "Frage", 0);
                        if (msgDB == 0) {
                            Steuerung.setStatus(Status.PROZESSBAR);
                            Steuerung.steuerung();
                            threadDBErstellen.start();
                        }
                    } else {
                        Steuerung.setStatus(Status.PROZESSBAR);
                        Steuerung.steuerung();
                        threadDBErstellen.start();
                    }
                }
            }
        });
    }

    public void fensterAnzeigen() {
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}

