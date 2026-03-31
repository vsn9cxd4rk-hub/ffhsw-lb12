package ao;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import keyBoardListener.EnterKeyboardListener;
import listener.DisposeListener;
import listener.LoginListener;
import logging.logging;
import run.runApplication;
import utilities.MyProperties;

public class AnmeldungAO extends JFrame {

   private static final long serialVersionUID = 1L;
   private JButton loginbutton;
   private JButton exitbutton;
   public static JTextField fieldUser;
   public static JPasswordField fieldPasswort;
   private JLabel user_label;
   private JLabel passwort_label;
   private JLabel willkommenText;
   public static JCheckBox bentzeranmeldungspeichern;
   public static JCheckBox angemeldetBleiben;
   public static JCheckBox windowsAnmeldungÜbernehmen;
   private JPanel panelTextfield;
   private JPanel panelButtons;
   private JPanel panelBeschriftung;
   private JPanel panelLogo;


   public AnmeldungAO() {
      super("FeuerwehrManagementSystem - ServiceLogin");
      logging.logInfo("Starte: AnmeldungAO");
      this.felderErzeugen();
      this.actionErzeugen();
      this.setDefaultCloseOperation(2);
      this.panelBeschriftung = new JPanel(new GridLayout(3, 1));
      this.getContentPane().add("West", this.panelBeschriftung);
      this.panelBeschriftung.add(this.user_label);
      this.panelBeschriftung.add(this.passwort_label);
      this.panelTextfield = new JPanel(new GridLayout(5, 1));
      this.getContentPane().add("Center", this.panelTextfield);
      this.panelTextfield.add(fieldUser);
      this.panelTextfield.add(fieldPasswort);
      this.panelTextfield.add(windowsAnmeldungÜbernehmen);
      this.panelTextfield.add(bentzeranmeldungspeichern);
      this.panelTextfield.add(angemeldetBleiben);
      this.panelButtons = new JPanel(new GridLayout(2, 1));
      this.getContentPane().add("South", this.panelButtons);
      this.panelButtons.add(this.loginbutton);
      this.panelButtons.add(this.exitbutton);
      this.panelLogo = new JPanel(new GridLayout(2, 1));
      this.getContentPane().add("North", this.panelLogo);
      this.panelLogo.add(this.willkommenText);
      this.pack();
   }

   private void actionErzeugen() {
      this.loginbutton.addActionListener(new LoginListener(this));
      this.exitbutton.addActionListener(new DisposeListener(this));
      fieldPasswort.addKeyListener(new EnterKeyboardListener(this));
      fieldUser.addKeyListener(new EnterKeyboardListener(this));
   }

   private void felderErzeugen() {
      this.user_label = new JLabel("   Benutzername:   ");
      this.passwort_label = new JLabel("   Passwort:   ");
      this.loginbutton = new JButton("Anmelden");
      this.loginbutton.setToolTipText("Anmelden");
      this.exitbutton = new JButton("Fenster schließen");
      this.exitbutton.setToolTipText("Fenster schließen");
      bentzeranmeldungspeichern = new JCheckBox("Anmeldenamen speichern");
      bentzeranmeldungspeichern.setToolTipText("Anmeldedaten speichern");
      angemeldetBleiben = new JCheckBox("Anmeldung beibehalten");
      angemeldetBleiben.setToolTipText("Der Benutzer bleibt am System angemeldet");
      windowsAnmeldungÜbernehmen = new JCheckBox("Betriebssystemanmeldung übernehmen");
      windowsAnmeldungÜbernehmen.setToolTipText("Diese Einstellung übernimmt den Benutzer des Betirebsystems!");
      fieldPasswort = new JPasswordField(25);
      this.willkommenText = new JLabel("                           Willkommen im Anmelde Menü");
      this.willkommenText.setFont(new Font("Arial", 1, 16));
      if(((String)runApplication.EINSTELLUNGEN.get("anmeldungSpeichernErlauben")).equals("0")) {
         angemeldetBleiben.setVisible(false);
      }

      MyProperties gespeichertebenutzeranmeldung = new MyProperties(runApplication.arbeitsverzeichnis + "properties/user.properties");
      if(gespeichertebenutzeranmeldung.sourceFileExists()) {
         gespeichertebenutzeranmeldung.loadVars();
         fieldUser = new JTextField((String)gespeichertebenutzeranmeldung.getVar("Name"), 25);
         bentzeranmeldungspeichern.setSelected(true);
      } else {
         fieldUser = new JTextField(25);
      }

      windowsAnmeldungÜbernehmen.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            if(AnmeldungAO.windowsAnmeldungÜbernehmen.isSelected()) {
               AnmeldungAO.fieldUser.setText(System.getProperty("user.name"));
               AnmeldungAO.fieldUser.setEditable(false);
               AnmeldungAO.fieldPasswort.setEditable(false);
            } else {
               AnmeldungAO.fieldUser.setText((String)null);
               AnmeldungAO.fieldUser.setEditable(true);
               AnmeldungAO.fieldPasswort.setEditable(true);
            }

         }
      });
   }

   public void fensterAnzeigen() {
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
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
