package ao;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleEinstellungen;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.Utils;
import utilities.facebook.Facebook;

public class FacebookPostKonfigurationAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JButton buttonBildAuswahl;
   private JButton buttonBildAuswahlLöschen;
   private JButton buttonPostTestMessage;
   private JButton buttonAPIKeyEinstellungen;
   private JTextArea textfiled;
   private JScrollPane scrollPane;
   private JComboBox templdateTyp;
   private JComboBox variablen;
   private JTextField bild;
   private JLabel templateTyp_label;
   private JLabel variablen_label;
   private JLabel bild_label;
   private JFileChooser chooserJPEG;
   private FileNameExtensionFilter filterJPEG;
   private FileNameExtensionFilter filterPNG;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panelOptionen;
   private JPanel panelTemplate;


   public FacebookPostKonfigurationAO() {
      super("FeuerwehrManagementSystem");
      logging.logInfo("Starte: FacebookPostKonfigurationAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Zurück");
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonBildAuswahl = new JButton("...");
      this.buttonBildAuswahlLöschen = new JButton("Bild entfernen");
      this.buttonPostTestMessage = new JButton("Post Test Nachricht");
      this.buttonBildAuswahl.setToolTipText("HINWEIS: Bei der Test-POST werden die Variablen nicht ersetzt...");
      this.buttonAPIKeyEinstellungen = new JButton("API-Key Einstellungen");
      this.textfiled = new JTextArea();
      this.scrollPane = new JScrollPane(this.textfiled);
      this.scrollPane.setVerticalScrollBarPolicy(22);
      this.scrollPane.setPreferredSize(new Dimension(450, 300));
      this.templateTyp_label = new JLabel("Template / Vorlage: ");
      this.variablen_label = new JLabel("Verfügbare Variablen: ");
      this.bild_label = new JLabel("Bild für Post:");
      this.bild = new JTextField(25);
      this.chooserJPEG = new JFileChooser();
      this.filterJPEG = new FileNameExtensionFilter("JPG", new String[]{"jpg"});
      this.filterPNG = new FileNameExtensionFilter("PNG", new String[]{"png"});
      this.modulBeschreibung = new JLabel("Facebook-Post Konfiguration");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {
      String[] typListe = new String[]{"<bitte wählen>", "Einsatz", "Protokoll / Tätigkeitsbericht"};
      String[] variablenListe = new String[]{"<bitte wählen>", "<<EINSATZ_NUMMER>>", "<<EINSATZ_DATUM>>", "<<EINSATZ_JAHR>>", "<<EINSATZ_ZEIT>>", "<<EINSATZ_ORT>>", "<<EINSATZ_STADTTEIL>>", "<<EINSATZ_STICHWORT>>", "<<EINSATZ_FAHRZEUG>>", "<<EINSATZ_KATEGORIE>>", "<<VERANSTALTUNG_ID>>", "<<PROTOKOLL_TEXT>>"};
      this.templdateTyp = new JComboBox(typListe);
      this.variablen = new JComboBox(variablenListe);
      this.variablen.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            if(!FacebookPostKonfigurationAO.this.variablen.getSelectedItem().equals("<bitte wählen>")) {
               FacebookPostKonfigurationAO.this.variablen.setSelectedItem("<bitte wählen>");
               if(!FacebookPostKonfigurationAO.this.variablen.getSelectedItem().equals("<bitte wählen>")) {
                  int curser = FacebookPostKonfigurationAO.this.textfiled.getCaretPosition();
                  String vorCurster = FacebookPostKonfigurationAO.this.textfiled.getText().substring(0, curser);
                  String nachCurster = FacebookPostKonfigurationAO.this.textfiled.getText().substring(curser, FacebookPostKonfigurationAO.this.textfiled.getText().length());
                  FacebookPostKonfigurationAO.this.textfiled.setText(vorCurster + FacebookPostKonfigurationAO.this.variablen.getSelectedItem().toString() + nachCurster);
                  FacebookPostKonfigurationAO.this.textfiled.setCaretPosition(0);
                  FacebookPostKonfigurationAO.this.textfiled.setWrapStyleWord(true);
               }
            }

         }
      });
      this.templdateTyp.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().toString().equals("Einsatz")) {
               FacebookPostKonfigurationAO.this.textfiled.setText((String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateEinsatz"));
               FacebookPostKonfigurationAO.this.bild.setText((String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateEinsatzBild"));
               FacebookPostKonfigurationAO.this.textfiled.setEnabled(true);
               FacebookPostKonfigurationAO.this.variablen.setEnabled(true);
               FacebookPostKonfigurationAO.this.buttonSpeichern.setEnabled(true);
               FacebookPostKonfigurationAO.this.buttonBildAuswahl.setEnabled(true);
               FacebookPostKonfigurationAO.this.buttonBildAuswahlLöschen.setEnabled(true);
               FacebookPostKonfigurationAO.this.textfiled.setCaretPosition(0);
               FacebookPostKonfigurationAO.this.textfiled.setWrapStyleWord(true);
            } else if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().toString().equals("Protokoll / Tätigkeitsbericht")) {
               FacebookPostKonfigurationAO.this.textfiled.setText((String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateProtokoll"));
               FacebookPostKonfigurationAO.this.bild.setText((String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateProtokollBild"));
               FacebookPostKonfigurationAO.this.textfiled.setEnabled(true);
               FacebookPostKonfigurationAO.this.variablen.setEnabled(true);
               FacebookPostKonfigurationAO.this.buttonSpeichern.setEnabled(true);
               FacebookPostKonfigurationAO.this.buttonBildAuswahl.setEnabled(true);
               FacebookPostKonfigurationAO.this.buttonBildAuswahlLöschen.setEnabled(true);
               FacebookPostKonfigurationAO.this.textfiled.setCaretPosition(0);
               FacebookPostKonfigurationAO.this.textfiled.setWrapStyleWord(true);
            }

         }
      });
   }

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Facebook-Post Konfiguration");
      this.setSize(800, 500);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.templateTyp_label.setPreferredSize(new Dimension(250, 25));
      this.panelTemplate = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panelTemplate);
      this.panelTemplate.add(this.templateTyp_label);
      this.panelTemplate.add(this.templdateTyp);
      this.add(this.scrollPane);
      this.panelOptionen = new JPanel(new GridLayout(10, 1));
      this.getContentPane().add("Center", this.panelOptionen);
      this.panelOptionen.add(this.variablen_label);
      this.panelOptionen.add(this.variablen);
      this.panelOptionen.add(new JLabel());
      this.panelOptionen.add(this.bild_label);
      this.panelOptionen.add(this.bild);
      this.panelOptionen.add(this.buttonBildAuswahl);
      this.panelOptionen.add(this.buttonBildAuswahlLöschen);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonPostTestMessage);
      this.add(this.buttonAPIKeyEinstellungen);
      this.add(this.buttonSpeichern);
      this.textfiled.setEnabled(false);
      this.variablen.setEnabled(false);
      this.buttonSpeichern.setEnabled(false);
      this.bild.setEditable(false);
      this.buttonBildAuswahl.setEnabled(false);
      this.buttonBildAuswahlLöschen.setEnabled(false);
      if(BerechtigunsManager.ber2[56] == 0) {
         this.buttonAPIKeyEinstellungen.setEnabled(false);
      }

   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonAPIKeyEinstellungen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Steuerung.setStatus(Status.FACEBOOK_API_KEY_EINSTELLUNGEN);
            Steuerung.steuerung();
         }
      });
      this.buttonPostTestMessage.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Facebook fb = new Facebook();
            String messageID = fb.publishTestMessage(FacebookPostKonfigurationAO.this.textfiled.getText());
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.FACEBOOK_TESTNACHRICHT_LOESCHEN, "Frage", 0);
            if(msg == 0) {
               fb.deletePublishedMessageByID(messageID);
            }

         }
      });
      this.buttonBildAuswahl.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            FacebookPostKonfigurationAO.this.chooserJPEG.setFileFilter(FacebookPostKonfigurationAO.this.filterJPEG);
            FacebookPostKonfigurationAO.this.chooserJPEG.setFileFilter(FacebookPostKonfigurationAO.this.filterPNG);
            int returnVal = FacebookPostKonfigurationAO.this.chooserJPEG.showOpenDialog(FacebookPostKonfigurationAO.this.chooserJPEG);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + FacebookPostKonfigurationAO.this.chooserJPEG.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/Templates/Facebook/" + FacebookPostKonfigurationAO.this.chooserJPEG.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(FacebookPostKonfigurationAO.this.chooserJPEG.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/Templates/Facebook");
            FacebookPostKonfigurationAO.this.bild.setText(dateiname);

            try {
               TabelleEinstellungen e = new TabelleEinstellungen();
               if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().equals("Einsatz")) {
                  e.update("facebookPostTemplateEinsatzBild", dateiname);
               } else if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().equals("Protokoll / Tätigkeitsbericht")) {
                  e.update("facebookPostTemplateProtokollBild", dateiname);
               }

               runApplication.EINSTELLUNGEN = e.getAllEinstellungen();
               Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/Templates/Facebook/", runApplication.clientID);
               Utils.dateiKatalogisieren(dateiname);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

            if((new File(dateiname)).length() >= 1024000L) {
               JOptionPane.showMessageDialog((Component)null, Konstante.FACEBOOK_GROSSES_BILD, "Warnung", 2);
            }

         }
      });
      this.buttonBildAuswahlLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               TabelleEinstellungen e1 = new TabelleEinstellungen();
               if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().equals("Einsatz")) {
                  e1.update("facebookPostTemplateEinsatzBild", "");
               } else if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().equals("Protokoll / Tätigkeitsbericht")) {
                  e1.update("facebookPostTemplateProtokollBild", "");
               }

               FacebookPostKonfigurationAO.this.bild.setText("");
               runApplication.EINSTELLUNGEN = e1.getAllEinstellungen();
               JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
            } catch (SQLException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleEinstellungen e = new TabelleEinstellungen();
               if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().equals("Einsatz")) {
                  e.update("facebookPostTemplateEinsatz", FacebookPostKonfigurationAO.this.textfiled.getText());
               } else if(FacebookPostKonfigurationAO.this.templdateTyp.getSelectedItem().equals("Protokoll / Tätigkeitsbericht")) {
                  e.update("facebookPostTemplateProtokoll", FacebookPostKonfigurationAO.this.textfiled.getText());
               }

               runApplication.EINSTELLUNGEN = e.getAllEinstellungen();
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
            } catch (SQLException var3) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            }

         }
      });
   }

   protected void labelErstellen() {}

   public void fensterAnzeigen() {
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }
}
