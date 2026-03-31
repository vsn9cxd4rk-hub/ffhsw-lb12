package ao.listen;

import ao.AbstractFenster;
import ao.statistik.BeteiligungsdauerAO;
import ao.utils.ProzessBarAO;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
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
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.BeteiligungsListePDFSchreiben;
import pdfdocumente.BeteiligungsListePDFSchreibenAusgabeformatListe;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class BeteiligungUebersichtListeAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonErstellen;
   public static JComboBox mitglieder;
   private JComboBox mitgliederGruppe;
   private JComboBox jahr;
   private JLabel mitglieder_label;
   private JLabel mitgliederGruppe_label;
   private JLabel jahr_label;
   private JRadioButton buttonAlsListe;
   private JRadioButton buttonDetailsPDF;
   private JRadioButton buttonAlsTabelle;
   private ButtonGroup bGroup;
   private JLabel grafik_label;
   private JCheckBox grafik;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panel;


   public BeteiligungUebersichtListeAO() {
      super("FeuerwehrManagementSystem - Übersichtsliste");
      logging.logInfo("Starte: BeteiligungUebersichtListeAO");
   }

   protected void buttonErstellen() {
      this.buttonErstellen = new JButton("Erstellen");
      this.buttonZurueck = new JButton("Schließen");
      this.mitglieder_label = new JLabel("Mitglieder: ");
      this.mitgliederGruppe_label = new JLabel("Mitgliedergruppe: ");
      this.jahr_label = new JLabel("Jahr: ");
      this.grafik_label = new JLabel("Grafik einfügen: ");
      this.buttonAlsListe = new JRadioButton("Nur Beteiligungszahlen");
      this.buttonAlsListe.setToolTipText("Erstellt eine Liste der Beteiligung in Verhältnis zur den Veranstaltungen...");
      this.buttonDetailsPDF = new JRadioButton("Details mit Beteiligungszahlen");
      this.buttonDetailsPDF.setToolTipText("Erstellt eine detailierte Liste mit allen An- und Abwesenheiten...");
      this.buttonAlsTabelle = new JRadioButton("Liste aller Veranstaltungen in einer Tabelle");
      this.buttonAlsTabelle.setToolTipText("Erstelle eine Tabelle aller Anwesenden Veranstaltungen...");
      this.grafik = new JCheckBox();
      this.bGroup = new ButtonGroup();
      this.bGroup.add(this.buttonAlsListe);
      this.bGroup.add(this.buttonDetailsPDF);
      this.bGroup.add(this.buttonAlsTabelle);
      this.modulBeschreibung = new JLabel("Beteiligungsübersicht");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      try {
         TabelleMitglied e = new TabelleMitglied();
         TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
         TabelleJahr tabJahr = new TabelleJahr();
         int mGruppe = tabGruppe.getID(runApplication.mitgliederGruppe);
         String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(e.getMitgliederEinerGruppe(mGruppe));
         String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerfügbarenJahre());
         String[] mitgliederGruppeListe = Utils.listToArray(tabGruppe.getAllGruppen());
         mitglieder = new JComboBox(mitgliederListe);
         this.mitgliederGruppe = new JComboBox(mitgliederGruppeListe);
         this.jahr = new JComboBox(jahresListe);
         this.jahr.setSelectedItem(SbcUtils.timeStamp("yyyy"));
         this.mitgliederGruppe.setSelectedItem(runApplication.mitgliederGruppe);
      } catch (SQLException var8) {
         logging.logPrintStackTrace(var8);
      }

      this.mitgliederGruppe.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            try {
               TabelleMitglied e1 = new TabelleMitglied();
               TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
               BeteiligungUebersichtListeAO.mitglieder.removeAllItems();
               String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(e1.getMitgliederEinerGruppe(tabGruppe.getID(BeteiligungUebersichtListeAO.this.mitgliederGruppe.getSelectedItem().toString())));

               for(int i = 0; i < mitgliederListe.length; ++i) {
                  BeteiligungUebersichtListeAO.mitglieder.addItem(mitgliederListe[i]);
               }
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
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
      this.setSize(500, 320);
      this.setTitle("FeuerwehrManagementSystem - Beteiligungsübersicht");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.mitgliederGruppe.setPreferredSize(new Dimension(210, 25));
      this.panel = new JPanel(new GridLayout(4, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.mitgliederGruppe_label);
      this.panel.add(this.mitgliederGruppe);
      this.panel.add(this.mitglieder_label);
      this.panel.add(mitglieder);
      this.panel.add(this.jahr_label);
      this.panel.add(this.jahr);
      this.panel.add(this.grafik_label);
      this.panel.add(this.grafik);
      this.add(this.buttonAlsListe);
      this.add(this.buttonDetailsPDF);
      this.add(this.buttonAlsTabelle);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonErstellen);
      this.buttonAlsListe.setSelected(true);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(BeteiligungUebersichtListeAO.mitglieder.getSelectedItem().toString().equals("<bitte wählen>")) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
            } else if(BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString().equals("<bitte wählen>")) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
            } else {
               Thread threadBerichtErstellen = new Thread() {
                  public void run() {
                     try {
                        ProzessBarAO.progressbar.setIndeterminate(true);
                        ProzessBarAO.progressbar.setStringPainted(false);
                        String e = runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Beteiligung_uebersicht/" + BeteiligungUebersichtListeAO.mitglieder.getSelectedItem() + ".pdf";
                        String outputFileGarfik = null;
                        if(BeteiligungUebersichtListeAO.this.grafik.isSelected()) {
                           outputFileGarfik = runApplication.arbeitsverzeichnis + "data/" + BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString() + "/Temp/Beteiligungsuebersicht.jpg";
                           MyChartUtils.writeChartToJPEG(BeteiligungsdauerAO.createChart(BeteiligungsdauerAO.createDataset((new TabelleMitglied()).getIdByGuiString(BeteiligungUebersichtListeAO.mitglieder.getSelectedItem().toString()), Integer.parseInt(BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString()))), 1000, 800, outputFileGarfik);
                        }

                        if(BeteiligungUebersichtListeAO.this.buttonDetailsPDF.isSelected()) {
                           BeteiligungsListePDFSchreiben.PDFdocumentErstellen(e, BeteiligungUebersichtListeAO.mitglieder.getSelectedItem().toString(), BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString(), outputFileGarfik);
                           Utils.dateiKatalogisieren(e);
                           Desktop.getDesktop().open(new File(e));
                           BeteiligungUebersichtListeAO.this.dispose();
                        } else if(BeteiligungUebersichtListeAO.this.buttonAlsListe.isSelected()) {
                           BeteiligungsListePDFSchreibenAusgabeformatListe.PDFdocumentErstellen(e, BeteiligungUebersichtListeAO.mitglieder.getSelectedItem().toString(), BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString(), outputFileGarfik);
                           Utils.dateiKatalogisieren(e);
                           Desktop.getDesktop().open(new File(e));
                           BeteiligungUebersichtListeAO.this.dispose();
                        } else if(BeteiligungUebersichtListeAO.this.buttonAlsTabelle.isSelected()) {
                           logging.logInfo("Erzeuge Tabelleanschicht");
                           Steuerung.setStatus(Status.ANWESENHEITSTABELLE_PRO_MITGLIED);
                           Steuerung.steuerung();
                        }

                        MyEvent.setEvent("0x0030");
                     } catch (SQLException var3) {
                        logging.logPrintStackTrace(var3);
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
