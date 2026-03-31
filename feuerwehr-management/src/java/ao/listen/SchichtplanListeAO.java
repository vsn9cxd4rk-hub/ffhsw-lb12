package ao.listen;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import data.tabellen.einstellungen.TabelleJahr;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.SchichtListePDFSchreiben;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class SchichtplanListeAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonErstellen;
   private JComboBox monate;
   private JComboBox jahr;
   private JLabel monat_label;
   private JLabel jahr_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panel;


   public SchichtplanListeAO() {
      super("FeuerwehrManagementSystem - Übersichtsliste");
      logging.logInfo("Starte: BeteiligungUebersichtListeAO");
   }

   protected void buttonErstellen() {
      this.buttonErstellen = new JButton("Erstellen");
      this.buttonZurueck = new JButton("Schließen");
      this.monat_label = new JLabel("Monat: ");
      this.jahr_label = new JLabel("Jahr: ");
      this.modulBeschreibung = new JLabel("ÜbersichtsListe");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      try {
         TabelleJahr e = new TabelleJahr();
         String[] monatListe = new String[]{"<bitte wählen>", "Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
         String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(e.getAllVerfügbarenJahre());
         this.monate = new JComboBox(monatListe);
         this.jahr = new JComboBox(jahresListe);
         this.jahr.setSelectedItem(SbcUtils.timeStamp("yyyy"));
         this.jahr.addItem(Integer.toString(Integer.parseInt(SbcUtils.timeStamp("yyyy")) + 1));
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(500, 200);
      this.setTitle("FeuerwehrManagementSystem - Stichwort");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panel = new JPanel(new GridLayout(2, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.jahr_label);
      this.panel.add(this.jahr);
      this.panel.add(this.monat_label);
      this.panel.add(this.monate);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonErstellen);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(SchichtplanListeAO.this.monate.getSelectedItem().toString().equals("<bitte wählen>")) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
            } else if(SchichtplanListeAO.this.jahr.getSelectedItem().toString().equals("<bitte wählen>")) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
            } else {
               Thread threadBerichtErstellen = new Thread() {
                  public void run() {
                     try {
                        ProzessBarAO.progressbar.setIndeterminate(true);
                        ProzessBarAO.progressbar.setStringPainted(false);
                        String e = runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Schichten/" + SchichtplanListeAO.this.jahr.getSelectedItem() + "_" + SchichtplanListeAO.this.monate.getSelectedItem() + ".pdf";
                        SchichtListePDFSchreiben.PDFdocumentErstellen(e, SchichtplanListeAO.this.monate.getSelectedItem().toString(), SchichtplanListeAO.this.jahr.getSelectedItem().toString());
                        Utils.dateiKatalogisieren(e);
                        Desktop.getDesktop().open(new File(e));
                        MyEvent.setEvent("0x0030");
                        SchichtplanListeAO.this.dispose();
                     } catch (SQLException var2) {
                        logging.logPrintStackTrace(var2);
                     }

                  }
               };
               Steuerung.setStatus(Status.PROZESSBAR);
               Steuerung.steuerung();
               threadBerichtErstellen.start();
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
