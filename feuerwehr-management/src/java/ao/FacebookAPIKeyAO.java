package ao;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleEinstellungen;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.images;
import run.runApplication;
import utilities.Konstante;
import utilities.facebook.Facebook;

public class FacebookAPIKeyAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextField facebookAccessToken;
   private JTextField facebookAppID;
   private JTextField facebookAppGeheimCode;
   private JLabel facebookAccessToken_label;
   private JLabel facebookAppID_label;
   private JLabel facebookAppGeheimCode_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panelAPIKey;


   public FacebookAPIKeyAO() {
      super("FeuerwehrManagementSystem - Stichwort");
      logging.logInfo("Starte: FacebookAPIKeyAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern & API-Key Prüfen");
      this.buttonZurueck = new JButton("Zurück");
      this.modulBeschreibung = new JLabel("Facebook API Key Einstellungen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.facebookAccessToken_label = new JLabel("Facebook Access Token: ");
      this.facebookAppID_label = new JLabel("Facebook App-ID: ");
      this.facebookAppGeheimCode_label = new JLabel("Facebook Geheim-Code: ");
      this.facebookAppID = new JTextField((String)runApplication.EINSTELLUNGEN.get("facebookAppID"), 35);
      this.facebookAppGeheimCode = new JTextField((String)runApplication.EINSTELLUNGEN.get("facebookAppGeheimCode"), 35);
      this.facebookAccessToken = new JTextField((String)runApplication.EINSTELLUNGEN.get("facebookAccessToken"), 35);
   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(870, 200);
      this.setTitle("FeuerwehrManagementSystem - Facebook API Key Einstellungen");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelAPIKey = new JPanel(new GridLayout(3, 2));
      this.getContentPane().add("Center", this.panelAPIKey);
      this.panelAPIKey.add(this.facebookAccessToken_label);
      this.panelAPIKey.add(this.facebookAccessToken);
      this.panelAPIKey.add(this.facebookAppID_label);
      this.panelAPIKey.add(this.facebookAppID);
      this.panelAPIKey.add(this.facebookAppGeheimCode_label);
      this.panelAPIKey.add(this.facebookAppGeheimCode);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               if(FacebookAPIKeyAO.this.facebookAccessToken.getText().equals("") | FacebookAPIKeyAO.this.facebookAppID.getText().equals("") | FacebookAPIKeyAO.this.facebookAppGeheimCode.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.FACEBOOK_API_PRUEFEN, "Warnung", 2);
               } else {
                  ImageIcon e1 = (new images()).loadImagesFromJARFacebookIcon();
                  e1.setImage(e1.getImage().getScaledInstance(100, 100, 5));
                  TabelleEinstellungen einstellungen = new TabelleEinstellungen();
                  einstellungen.update("facebookAccessToken", FacebookAPIKeyAO.this.facebookAccessToken.getText());
                  einstellungen.update("facebookAppID", FacebookAPIKeyAO.this.facebookAppID.getText());
                  einstellungen.update("facebookAppGeheimCode", FacebookAPIKeyAO.this.facebookAppGeheimCode.getText());
                  Facebook fb = new Facebook();
                  fb.getExtendedAccesToken();
                  FacebookAPIKeyAO.this.facebookAccessToken.setText((String)runApplication.EINSTELLUNGEN.get("facebookAccessToken"));
                  JOptionPane.showMessageDialog((Component)null, "Die Facebook-API ist richtig konfiguriert!\n\n" + fb.getUserInformations() + Konstante.FACEBOOK_ACCESSTOKEN_GUELTIGKEIT + "\n" + (String)runApplication.EINSTELLUNGEN_GESPEICHERT.get("facebookAccessTokenExpiereDate"), "Facebook-API-Info", 1, e1);
               }
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
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
