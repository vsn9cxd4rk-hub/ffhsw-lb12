package ao;

import ao.AbstractFenster;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import go.Einsatz;
import go.Protokoll;
import go.StatistikEinsatz;
import go.Veranstaltung;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.ProtokollPDFScheiben;
import run.runApplication;
import service.BerechtigunsManager;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities.facebook.Facebook;
import utilities.joomla.Joomla;

public class ProtokollAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JButton buttonExport;
   private JLabel veranstaltungen_label;
   private JLabel mitgliederGruppe_label;
   private JLabel title_label;
   private JLabel facebookPost_label;
   private JComboBox mitgliederGruppe;
   private JComboBox veranstaltung;
   public static JTextArea textfield;
   public static JTextField title;
   private JScrollPane pane;
   private JCheckBox facebookPost;
   private JPanel panel;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public ProtokollAO() {
      super("FeuerwehrManagementSystem - Protokoll");
      logging.logInfo("Starte: ProtokollAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Schließen");
      this.buttonExport = new JButton("Export");
      textfield = new JTextArea(23, 50);
      textfield.setLineWrap(true);
      textfield.setWrapStyleWord(true);
      this.pane = new JScrollPane(textfield);
      this.pane.setVerticalScrollBarPolicy(22);
      this.facebookPost = new JCheckBox();
      title = new JTextField(35);
      this.title_label = new JLabel("Protokoll Betreff / Überschrift: ");
      this.veranstaltungen_label = new JLabel("Veranstaltung: ");
      this.mitgliederGruppe_label = new JLabel("Mitgliedergruppe: ");
      this.facebookPost_label = new JLabel("Protokoll auf Facebook Posten: ");
      this.modulBeschreibung = new JLabel("Protokoll erstellen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      try {
         TabelleVeranstaltung e = new TabelleVeranstaltung();
         TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
         int mGruppe = tabGruppe.getID(runApplication.mitgliederGruppe);
         String[] veranstaltungenListe = Utils.listToArrayOnlyFORComboBoxes(e.getAllVeranstaltung(mGruppe));
         String[] mitgliederGruppeList = Utils.listToArrayOnlyFORComboBoxes(tabGruppe.getAllGruppen());
         this.veranstaltung = new JComboBox(veranstaltungenListe);
         this.mitgliederGruppe = new JComboBox(mitgliederGruppeList);
         this.mitgliederGruppe.setSelectedItem(runApplication.mitgliederGruppe);
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

      this.mitgliederGruppe.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            runApplication.mitgliederGruppe = ProtokollAO.this.mitgliederGruppe.getSelectedItem().toString();
            ProtokollAO.this.veranstaltung.removeAllItems();

            try {
               TabelleVeranstaltung e1 = new TabelleVeranstaltung();
               TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
               int mGruppe = tabGruppe.getID(runApplication.mitgliederGruppe);
               String[] veranstaltungenListe = Utils.listToArrayOnlyFORComboBoxes(e1.getAllVeranstaltung(mGruppe));

               for(int v = 0; v < veranstaltungenListe.length; ++v) {
                  ProtokollAO.this.veranstaltung.addItem(veranstaltungenListe[v]);
               }
            } catch (SQLException var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      });
      this.veranstaltung.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            if(!ProtokollAO.this.veranstaltung.getSelectedItem().equals("<bitte wählen>")) {
               try {
                  TabelleProtokoll e = new TabelleProtokoll();
                  TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                  ProtokollAO.this.buttonSpeichern.setEnabled(true);
                  ProtokollAO.this.buttonExport.setEnabled(true);
                  ProtokollAO.textfield.setEditable(true);
                  ProtokollAO.title.setEditable(true);
                  int vID = tabVeranstaltung.getVeranstaltungID(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
                  if(e.getCount(vID) == 1) {
                     new Protokoll();
                     Protokoll protokoll = e.getData(vID);
                     ProtokollAO.title.setText(protokoll.getTitle());
                     ProtokollAO.textfield.setText(protokoll.getProtokolltext());
                  } else {
                     ProtokollAO.textfield.setText((String)null);
                     ProtokollAO.title.setText((String)null);
                  }

                  if(ProtokollAO.this.veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                     ProtokollAO.title.setText(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
                     ProtokollAO.title.setEditable(false);
                     if(BerechtigunsManager.ber2[57] == 1) {
                        ProtokollAO.this.facebookPost.setEnabled(true);
                     }
                  } else {
                     ProtokollAO.this.facebookPost.setEnabled(false);
                     ProtokollAO.this.facebookPost.setSelected(false);
                  }
               } catch (SQLException var6) {
                  logging.logPrintStackTrace(var6);
               }
            } else {
               ProtokollAO.this.buttonSpeichern.setEnabled(false);
               ProtokollAO.this.buttonExport.setEnabled(false);
               ProtokollAO.textfield.setEditable(false);
               ProtokollAO.this.facebookPost.setEnabled(false);
               ProtokollAO.title.setEditable(false);
               ProtokollAO.textfield.setText((String)null);
               ProtokollAO.title.setText((String)null);
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
      this.setSize(870, 750);
      this.setTitle("FeuerwehrManagementSystem - Protokoll");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panel = new JPanel(new GridLayout(3, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.mitgliederGruppe_label);
      this.panel.add(this.mitgliederGruppe);
      this.panel.add(this.veranstaltungen_label);
      this.panel.add(this.veranstaltung);
      this.panel.add(this.title_label);
      this.panel.add(title);
      Border lowerEtched = BorderFactory.createEtchedBorder(1);
      TitledBorder rahmen = BorderFactory.createTitledBorder(lowerEtched, "Protokolltext");
      this.pane.setBorder(rahmen);
      this.pane.setPreferredSize(new Dimension(800, 480));
      this.add(this.pane);
      this.add(this.facebookPost_label);
      this.add(this.facebookPost);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
      this.add(this.buttonExport);
      this.buttonExport.setEnabled(false);
      this.buttonSpeichern.setEnabled(false);
      textfield.setEditable(false);
      title.setEditable(false);
      this.facebookPost.setEnabled(false);
      if(((String)runApplication.EINSTELLUNGEN.get("facebookAutoPostEinsatz")).equals("0")) {
         this.facebookPost_label.setVisible(false);
         this.facebookPost.setVisible(false);
      }

   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               if(ProtokollAO.title.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_TITLE_ANGEBEN, "Warnung", 2);
               } else {
                  TabelleProtokoll e = new TabelleProtokoll();
                  final TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                  Protokoll protokoll = new Protokoll();
                  final int vID = tabVeranstaltung.getVeranstaltungID(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
                  int kID = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                  int mGruppe = (new TabelleMitglieder_gruppe()).getID(runApplication.mitgliederGruppe);
                  protokoll.setId(e.getNextNummer());
                  protokoll.setVeranstaltungID(vID);
                  protokoll.setJahr(tabVeranstaltung.getJahrDerVeranstaltung(vID));
                  protokoll.setTitle(ProtokollAO.title.getText());
                  protokoll.setProtokolltext(ProtokollAO.textfield.getText());
                  protokoll.setErstelldatum(SbcUtils.timeStamp("yyyy-MM-dd"));
                  protokoll.setMitgliederGruppe(mGruppe);
                  if(e.getCount(vID) == 0) {
                     e.insert(protokoll);
                     logbuchEingabe.NeuerEintag("Protokoll wurde zur Veranstaltung: " + ProtokollAO.this.veranstaltung.getSelectedItem().toString() + " hinzugefügt");
                  } else {
                     e.update(protokoll);
                     logbuchEingabe.NeuerEintag("Protokoll wurde zur Veranstaltung: " + ProtokollAO.this.veranstaltung.getSelectedItem().toString() + " aktualisiert");
                  }

                  if(kID == 1 && ((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteEinsatzBerichtÜbermitteln")).equals("1")) {
                     ProtokollAO.this.updateEinsatzBerichtHomepage(vID);
                  }

                  if(ProtokollAO.this.facebookPost.isSelected()) {
                     Thread threadFacebookPost = new Thread() {
                        public void run() {
                           try {
                              logging.logInfo("Starte FacebookThread - Post Protokoll auf die Facebook Pinnwand...");
                              Facebook e = new Facebook();
                              TabelleStatistikEinsatz tabStatistikEinsatz = new TabelleStatistikEinsatz();
                              TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                              new StatistikEinsatz();
                              new Einsatz();
                              new Veranstaltung();
                              Einsatz einsatzForFacebook = tabEinsatz.getData2(vID);
                              StatistikEinsatz statistikForFacebook = tabStatistikEinsatz.getData(vID);
                              Veranstaltung veranstaltungForFacebook = tabVeranstaltung.getVeranstaltungData2(vID);
                              String message = e.createProtokollPostString(einsatzForFacebook, statistikForFacebook, ProtokollAO.textfield.getText());
                              if(((String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateProtokollBild")).equals("")) {
                                 e.publishMessage(message, veranstaltungForFacebook, "Protokoll");
                              } else {
                                 e.publishMessageWithPicture(message, (String)runApplication.EINSTELLUNGEN.get("facebookPostTemplateProtokollBild"), "Protokoll Bild", veranstaltungForFacebook, "Protokoll");
                              }
                           } catch (SQLException var8) {
                              logging.logPrintStackTrace(var8);
                           }

                        }
                     };
                     threadFacebookPost.start();
                  }

                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               }
            } catch (SQLException var9) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var9);
            }

         }
      });
      this.buttonExport.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleProtokoll e = new TabelleProtokoll();
               TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
               int vID = tabVeranstaltung.getVeranstaltungID(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
               new Protokoll();
               Protokoll protokoll = e.getData(vID);
               String dateiname = runApplication.arbeitsverzeichnis + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
               ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
               Utils.dateiKatalogisieren(dateiname);
               Desktop.getDesktop().open(new File(dateiname));
            } catch (IOException var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      });
   }

   private void updateEinsatzBerichtHomepage(final int vID) {
      Thread threadEinsatzkomponenteBericht = new Thread() {
         public void run() {
            logging.logInfo("Starte JoomlaThread - Sende EinsatzBericht an die Einsatzkomponente...");
            TabelleEinsatz_organisationen tabEinsatz_organisation = new TabelleEinsatz_organisationen();

            try {
               String e = Utils.checkTextAndRemoveIllegalSigns2(ProtokollAO.textfield.getText());
               Joomla.erstelleEinsatzBericht(vID, e.split("\n"), tabEinsatz_organisation.getOrganisationIDKommaSeperated(vID));
               logging.logInfo("Bericht an die Homepage erfogreich übertragen...");
            } catch (SQLException var3) {
               logging.logError("Bei der Übertragung des Einsatzberichtes auf dei Homepage ist ein Fehler aufgetreten...");
               logging.logPrintStackTrace(var3);
            }

         }
      };
      threadEinsatzkomponenteBericht.start();
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
