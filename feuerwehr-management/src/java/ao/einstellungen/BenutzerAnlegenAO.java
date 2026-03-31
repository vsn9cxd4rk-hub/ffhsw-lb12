package ao.einstellungen;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleBerechtigunggruppe;
import data.tabellen.einstellungen.TabelleUser;
import go.User;
import java.awt.Component;
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

public class BenutzerAnlegenAO extends AbstractFenster {

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
   public static JComboBox berechtigungsgruppe;
   private JCheckBox adminbereichZuweisen;
   private JLabel benutzername_label;
   private JLabel passwort1_label;
   private JLabel passwort2_label;
   private JLabel berechtigungsgrppe_label;
   private JLabel adminbereichZuweisen_label;
   private JButton buttonSpeichernBenutzerSperrenÄndernLöschen;
   private JComboBox benutzer;
   private JLabel benutzer_label;
   private JComboBox benutzer_berechtigungsgruppe;
   private JLabel benutzer_berechtigungsgruppe_label;
   private JCheckBox activ;
   private JLabel activ_label;
   private JCheckBox loeschkenner;
   private JLabel loeschkenner_label;
   private JCheckBox benutzer_admin;
   private JLabel benutzer_admin_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JPanel panelBenutzer;
   private JPanel panelSperrenÄndernLoeschen;
   private JButton buttonSpeichernPasswortAendern;
   private JLabel pass_benutzer_label;
   private JLabel pass_altesPasswort_label;
   private JLabel pass_neuesPasswort_label;
   private JLabel pass_neuesPasswort2_label;
   private JComboBox pass_benutzer;
   private JPasswordField pass_altesPasswort;
   private JPasswordField pass_neuesPasswort;
   private JPasswordField pass_neuesPasswort2;
   private JPanel panelPasswortÄnderung;
   private JTabbedPane tabPane;


   public BenutzerAnlegenAO() {
      super("FeuerwehrManagementSystem - Benutzerverwaltung");
      logging.logInfo("Starte: BenutzerAnlegenAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichernBenutzer = new JButton("Speichern");
      this.buttonSpeichernBenutzerSperrenÄndernLöschen = new JButton("Speichern");
      this.buttonSpeichernPasswortAendern = new JButton("Passwort ändern");
      this.buttonZurueck = new JButton("Schließen");
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
      this.benutzer_berechtigungsgruppe_label = new JLabel("Berechtigungsgrppe: ");
      this.activ = new JCheckBox();
      this.activ_label = new JLabel("Benutzer sperren: ");
      this.loeschkenner_label = new JLabel("Benutzer löschen: ");
      this.loeschkenner = new JCheckBox();
      this.benutzer_admin = new JCheckBox();
      this.benutzer_admin_label = new JLabel("Administratorenberich zuweisen: ");
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
         TabelleBerechtigunggruppe e = new TabelleBerechtigunggruppe();
         TabelleUser tabUser = new TabelleUser();
         String[] gruppenListe = Utils.listToArrayOnlyFORComboBoxes(e.getBercehtigungsgruppen());
         String[] userliste = Utils.listToArrayOnlyFORComboBoxes(tabUser.getUserListe());
         berechtigungsgruppe = new JComboBox(gruppenListe);
         this.benutzer_berechtigungsgruppe = new JComboBox(gruppenListe);
         this.benutzer = new JComboBox(userliste);
         this.pass_benutzer = new JComboBox(userliste);
         this.benutzer.removeItem("admin");
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

      this.benutzer.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            TabelleUser tabUser = new TabelleUser();
            TabelleBerechtigunggruppe tabBerechtigunggruppe = new TabelleBerechtigunggruppe();

            try {
               if(tabUser.getDeaktivStatus(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString()) == 1) {
                  BenutzerAnlegenAO.this.activ.setSelected(true);
               } else {
                  BenutzerAnlegenAO.this.activ.setSelected(false);
               }

               if(tabUser.getAdministratorStatus(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString()).equals("admin")) {
                  BenutzerAnlegenAO.this.benutzer_admin.setSelected(true);
               } else {
                  BenutzerAnlegenAO.this.benutzer_admin.setSelected(false);
               }

               if(!BenutzerAnlegenAO.this.benutzer.equals("<bitte wählen>")) {
                  BenutzerAnlegenAO.this.benutzer_berechtigungsgruppe.setEnabled(true);
                  BenutzerAnlegenAO.this.benutzer_berechtigungsgruppe.setSelectedItem(tabBerechtigunggruppe.getBerechtigungName(tabUser.getRechte(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString())));
               } else {
                  BenutzerAnlegenAO.this.benutzer_berechtigungsgruppe.setEnabled(false);
                  BenutzerAnlegenAO.this.benutzer_berechtigungsgruppe.setSelectedItem("<bitte wählen>");
               }
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(670, 530);
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
      this.panelSperrenÄndernLoeschen = new JPanel(new GridLayout(10, 2));
      this.getContentPane().add("Center", this.panelSperrenÄndernLoeschen);
      this.panelSperrenÄndernLoeschen.add(this.benutzer_label);
      this.panelSperrenÄndernLoeschen.add(this.benutzer);
      this.panelSperrenÄndernLoeschen.add(this.benutzer_berechtigungsgruppe_label);
      this.panelSperrenÄndernLoeschen.add(this.benutzer_berechtigungsgruppe);
      this.panelSperrenÄndernLoeschen.add(this.activ_label);
      this.panelSperrenÄndernLoeschen.add(this.activ);
      this.panelSperrenÄndernLoeschen.add(this.loeschkenner_label);
      this.panelSperrenÄndernLoeschen.add(this.loeschkenner);
      this.panelSperrenÄndernLoeschen.add(this.benutzer_admin_label);
      this.panelSperrenÄndernLoeschen.add(this.benutzer_admin);
      this.panelSperrenÄndernLoeschen.add(new JLabel());
      this.panelSperrenÄndernLoeschen.add(this.buttonSpeichernBenutzerSperrenÄndernLöschen);
      this.panelSperrenÄndernLoeschen.add(new JLabel());
      this.panelSperrenÄndernLoeschen.add(new JLabel());
      this.panelSperrenÄndernLoeschen.add(new JLabel());
      this.panelSperrenÄndernLoeschen.add(new JLabel());
      this.panelSperrenÄndernLoeschen.add(new JLabel());
      this.panelSperrenÄndernLoeschen.add(new JLabel());
      this.panelPasswortÄnderung = new JPanel(new GridLayout(10, 2));
      this.getContentPane().add("Center", this.panelPasswortÄnderung);
      this.panelPasswortÄnderung.add(this.pass_benutzer_label);
      this.panelPasswortÄnderung.add(this.pass_benutzer);
      this.panelPasswortÄnderung.add(this.pass_altesPasswort_label);
      this.panelPasswortÄnderung.add(this.pass_altesPasswort);
      this.panelPasswortÄnderung.add(this.pass_neuesPasswort_label);
      this.panelPasswortÄnderung.add(this.pass_neuesPasswort);
      this.panelPasswortÄnderung.add(this.pass_neuesPasswort2_label);
      this.panelPasswortÄnderung.add(this.pass_neuesPasswort2);
      this.panelPasswortÄnderung.add(new JLabel());
      this.panelPasswortÄnderung.add(this.buttonSpeichernPasswortAendern);
      this.panelPasswortÄnderung.add(new JLabel());
      this.panelPasswortÄnderung.add(new JLabel());
      this.panelPasswortÄnderung.add(new JLabel());
      this.panelPasswortÄnderung.add(new JLabel());
      this.tabPane.addTab("Benutzer anlegen", this.panelBenutzer);
      this.tabPane.addTab("Benutzer sperren / ändern / löschen", this.panelSperrenÄndernLoeschen);
      this.tabPane.addTab("Passwort ändern", this.panelPasswortÄnderung);
      this.tabPane.setPreferredSize(new Dimension(630, 350));
      this.add(this.tabPane);
      this.add(this.buttonZurueck);
      this.benutzer_berechtigungsgruppe.setEnabled(false);
      this.benutzer_admin.setEnabled(false);
      this.adminbereichZuweisen.setEnabled(false);
      this.benutzer_admin.setToolTipText("Nur Administratoren können die Adminrechter bearbeiten!");
      this.adminbereichZuweisen.setToolTipText("Nur Administratoren können die Adminrechter hinzufügen!");
      this.benutzer_admin_label.setToolTipText("Nur Administratoren können die Adminrechter bearbeiten!");
      this.adminbereichZuweisen_label.setToolTipText("Nur Administratoren können die Adminrechter hinzufügen!");

      try {
         if(!runApplication.loginName.equals("public") && (new TabelleUser()).getUserGruppe(runApplication.loginName).equals("admin")) {
            this.benutzer_admin.setEnabled(true);
            this.adminbereichZuweisen.setEnabled(true);
            this.pass_altesPasswort.setEditable(false);
            this.pass_altesPasswort.setToolTipText("Administratoren können neue Passwörter ohne das alte vergeben...");
            this.benutzer_admin.setToolTipText((String)null);
            this.adminbereichZuweisen.setToolTipText((String)null);
            this.benutzer_admin_label.setToolTipText((String)null);
            this.adminbereichZuweisen_label.setToolTipText((String)null);
         }
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
      }

   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.neueBerechtigungsGruppeErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.BERECHTIGUNG);
            Steuerung.steuerung();
         }
      });
      this.berechtigunBearbeiten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            MyEvent.setEvent("0x0005");
            Steuerung.setStatus(Status.BERECHTIGUNG);
            Steuerung.steuerung();
         }
      });
      this.buttonSpeichernPasswortAendern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleUser tabUser = new TabelleUser();

            try {
               User user = tabUser.get(BenutzerAnlegenAO.this.pass_benutzer.getSelectedItem().toString());
               if(!BenutzerAnlegenAO.this.pass_altesPasswort.getText().equals(hash.decodeHashCode(user.getPasswort())) && BenutzerAnlegenAO.this.pass_altesPasswort.isEditable()) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.ALTE_PASSWORT_FALSCH, "Fehlermeldung", 0);
               } else if(!BenutzerAnlegenAO.this.pass_neuesPasswort.getText().equals(BenutzerAnlegenAO.this.pass_neuesPasswort2.getText())) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.NEUE_PASSWORT_FALSCH, "Fehlermeldung", 0);
               } else {
                  tabUser.updatepasswort(BenutzerAnlegenAO.this.pass_benutzer.getSelectedItem().toString(), hash.createHashCode(BenutzerAnlegenAO.this.pass_neuesPasswort.getText()));
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  logbuchEingabe.NeuerEintag("Passwort wurde geändert für: " + BenutzerAnlegenAO.this.pass_benutzer.getSelectedItem().toString());
                  BenutzerAnlegenAO.this.pass_benutzer.setSelectedItem("<bitte wählen>");
                  BenutzerAnlegenAO.this.pass_neuesPasswort.setText((String)null);
                  BenutzerAnlegenAO.this.pass_neuesPasswort2.setText((String)null);
                  BenutzerAnlegenAO.this.pass_altesPasswort.setText((String)null);
               }
            } catch (SQLException var5) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonSpeichernBenutzerSperrenÄndernLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleUser tabUser = new TabelleUser();
            TabelleBerechtigunggruppe tabBerechtigung = new TabelleBerechtigunggruppe();

            try {
               if(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_BENUTZER_WAEHLEN, "Warnung", 2);
               } else {
                  tabUser.updateDeaktiv(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString(), BenutzerAnlegenAO.this.activ.isSelected()?1:0);
                  tabUser.updateLoeschkenner(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString(), BenutzerAnlegenAO.this.loeschkenner.isSelected()?1:0);
                  tabUser.updateBerechtigungsProfil(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString(), tabBerechtigung.getID(BenutzerAnlegenAO.this.benutzer_berechtigungsgruppe.getSelectedItem().toString()));
                  if(BenutzerAnlegenAO.this.benutzer_admin.isSelected()) {
                     tabUser.updateAdministratorRecht(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString(), "admin");
                  } else {
                     tabUser.updateAdministratorRecht(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString(), "benutzer");
                  }

                  logbuchEingabe.NeuerEintag("Benutzer wurde geändert: Sperre = " + Integer.toString(BenutzerAnlegenAO.this.activ.isSelected()?1:0) + " / " + BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                  logbuchEingabe.NeuerEintag("Benutzer wurde geändert: Loeschkenner = " + Integer.toString(BenutzerAnlegenAO.this.loeschkenner.isSelected()?1:0) + " / " + BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                  logbuchEingabe.NeuerEintag("Benutzer wurde geändert: Berechtigung = " + BenutzerAnlegenAO.this.benutzer_berechtigungsgruppe.getSelectedItem().toString() + " / " + BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                  logbuchEingabe.NeuerEintag("Benutzer wurde geändert: Administrationsrecht = " + Integer.toString(BenutzerAnlegenAO.this.benutzer_admin.isSelected()?1:0) + " / " + BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                  if(BenutzerAnlegenAO.this.loeschkenner.isSelected()) {
                     BenutzerAnlegenAO.this.benutzer.removeItem(BenutzerAnlegenAO.this.benutzer.getSelectedItem().toString());
                     BenutzerAnlegenAO.this.benutzer.setSelectedItem("<bitte wählen>");
                     BenutzerAnlegenAO.this.activ.setSelected(false);
                     BenutzerAnlegenAO.this.loeschkenner.setSelected(false);
                  }

                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               }
            } catch (SQLException var5) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonSpeichernBenutzer.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleUser tabUser = new TabelleUser();
            TabelleBerechtigunggruppe tabBer = new TabelleBerechtigunggruppe();
            User user = new User();

            try {
               if(BenutzerAnlegenAO.this.benutzername.getText().equals("MASTER_USER_FMS")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.USER_NAME_UNGUELTIG, "Fehlermeldung", 0);
               } else if(BenutzerAnlegenAO.berechtigungsgruppe.getSelectedItem().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_BERECHTIGUNG_WAEHLEN, "Warnung", 2);
               } else if(BenutzerAnlegenAO.this.passswort1.getText().equals(BenutzerAnlegenAO.this.passswort2.getText())) {
                  user.setUser(BenutzerAnlegenAO.this.benutzername.getText());
                  user.setPasswort(hash.createHashCode(BenutzerAnlegenAO.this.passswort1.getText()));
                  user.setAdmin(tabBer.getID(BenutzerAnlegenAO.berechtigungsgruppe.getSelectedItem().toString()));
                  if(BenutzerAnlegenAO.this.adminbereichZuweisen.isSelected()) {
                     user.setUsergruppe("admin");
                  } else {
                     user.setUsergruppe("benutzer");
                  }

                  user.setDeaktiv(0);
                  tabUser.insert(user);
                  BenutzerAnlegenAO.this.benutzer.addItem(BenutzerAnlegenAO.this.benutzername.getText());
                  BenutzerAnlegenAO.this.pass_benutzer.addItem(BenutzerAnlegenAO.this.benutzername.getText());
                  BenutzerAnlegenAO.berechtigungsgruppe.setSelectedItem("<bitte wählen>");
                  BenutzerAnlegenAO.this.benutzername.setText((String)null);
                  BenutzerAnlegenAO.this.passswort1.setText((String)null);
                  BenutzerAnlegenAO.this.passswort2.setText((String)null);
                  BenutzerAnlegenAO.this.adminbereichZuweisen.setSelected(false);
                  logbuchEingabe.NeuerEintag("Benutzer wurde erstellt " + BenutzerAnlegenAO.this.benutzername.getText());
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               } else {
                  logging.logInfo("Passwort stimmt beim anlegen des Benutzers nicht überein....");
                  JOptionPane.showMessageDialog((Component)null, Konstante.PASSWORT_STIMMT_NICHT_UEBEREIN, "Fehlermeldung", 0);
               }
            } catch (SQLException var6) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var6);
            }

         }
      });
   }

   public void fensterAnzeigen() {
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }

   public void fensterSchlissen() {
      this.dispose();
   }
}
