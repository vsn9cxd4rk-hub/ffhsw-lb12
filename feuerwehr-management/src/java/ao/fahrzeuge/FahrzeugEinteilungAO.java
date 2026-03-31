package ao.fahrzeuge;

import ao.AbstractFenster;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeugeinteilung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Fahrzeugeinteilung;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import logging.logging;
import pdfdocumente.FarzeugeinteilungPDFSchreiben;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.PDFPrinter;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities.fahrzeugeinteilung.RegelUtilities;

public class FahrzeugEinteilungAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JButton buttonDrucken;
   public static String currendChange = null;
   public static JLabel konflikt_label;
   public static JTextArea textfield;
   public static JScrollPane pane;
   public static JComboBox[][] sitzplatz;
   public static JLabel[][] sitzplatz_label;
   public static StringBuilder build;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public FahrzeugEinteilungAO() {
      super("FeuerwehrManagementSystem - Fahrzeugeinteilung");
      logging.logInfo("Starte: FahrzeugEinteilungAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Schließen");
      this.buttonDrucken = new JButton("Drucken");
      textfield = new JTextArea(14, 13);
      textfield.setEditable(false);
      textfield.setLineWrap(true);
      textfield.setWrapStyleWord(true);
      pane = new JScrollPane(textfield);
      pane.setVerticalScrollBarPolicy(22);
      Border lowerEtched = BorderFactory.createEtchedBorder(1);
      TitledBorder titleBorderTextPane = BorderFactory.createTitledBorder(lowerEtched, "Übrige Personen");
      pane.setBorder(titleBorderTextPane);
      konflikt_label = new JLabel("");
      this.modulBeschreibung = new JLabel("Fahrzeugeinteilung für " + runApplication.letzterVeranstaltungsname);
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();

      try {
         if(tabFahrzeug.countWithoutAnhaenger() >= 6 && tabFahrzeug.countWithoutAnhaenger() <= 8) {
            logging.logInfo("Setze GUI 1150x950");
            this.setSize(1150, 950);
         } else if(tabFahrzeug.countWithoutAnhaenger() >= 4 && tabFahrzeug.countWithoutAnhaenger() <= 6) {
            logging.logInfo("Setze GUI 1150x650");
            this.setSize(1150, 650);
         } else {
            logging.logInfo("Setze GUI 11150x400");
            this.setSize(1150, 400);
         }
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

      this.setTitle("FeuerwehrManagementSystem - Fahrzeugeinteilung");
      this.setDefaultCloseOperation(0);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);

      try {
         TabelleFahrzeug e = new TabelleFahrzeug();
         String[] fahrzeugListe = Utils.listToArray(e.getAllFahrzeugeOhneAnhaenger());

         for(int x = 0; x < fahrzeugListe.length; ++x) {
            JPanel panelFahrzeug = new JPanel(new GridLayout(9, 2));
            this.getContentPane().add("Center", panelFahrzeug);
            panelFahrzeug.add(sitzplatz_label[x][0]);
            panelFahrzeug.add(sitzplatz[x][0]);
            panelFahrzeug.add(sitzplatz_label[x][1]);
            panelFahrzeug.add(sitzplatz[x][1]);
            panelFahrzeug.add(sitzplatz_label[x][2]);
            panelFahrzeug.add(sitzplatz[x][2]);
            panelFahrzeug.add(sitzplatz_label[x][3]);
            panelFahrzeug.add(sitzplatz[x][3]);
            panelFahrzeug.add(sitzplatz_label[x][4]);
            panelFahrzeug.add(sitzplatz[x][4]);
            panelFahrzeug.add(sitzplatz_label[x][5]);
            panelFahrzeug.add(sitzplatz[x][5]);
            panelFahrzeug.add(sitzplatz_label[x][6]);
            panelFahrzeug.add(sitzplatz[x][6]);
            panelFahrzeug.add(sitzplatz_label[x][7]);
            panelFahrzeug.add(sitzplatz[x][7]);
            panelFahrzeug.add(sitzplatz_label[x][8]);
            panelFahrzeug.add(sitzplatz[x][8]);
            Border lowerEtched = BorderFactory.createEtchedBorder(1);
            TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, fahrzeugListe[x].toString());
            panelFahrzeug.setBorder(title);
         }
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
      }

      this.add(pane);
      this.add(konflikt_label);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonDrucken);
      this.add(this.buttonSpeichern);
   }

   protected void boxenHinzufuegen() {
      RegelUtilities.BerechneFahrzeugeinteilung();
   }

   protected void actionErzeugen() {
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            FahrzeugEinteilungAO.this.buttonZurueck.doClick();
         }
      });
      this.buttonZurueck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(FahrzeugEinteilungAO.this.buttonSpeichern.isEnabled()) {
               int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.WIRKLICH_SCHLIESSEN, "Frage", 0);
               if(msg == 0) {
                  runApplication.letzterVeranstaltungsname = "<bitte wählen>";
                  FahrzeugEinteilungAO.this.dispose();
               }
            } else {
               runApplication.letzterVeranstaltungsname = "<bitte wählen>";
               FahrzeugEinteilungAO.this.dispose();
            }

         }
      });
      this.buttonDrucken.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               if(FahrzeugEinteilungAO.this.buttonSpeichern.isEnabled()) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ZUERST, "Warnung", 2);
               } else {
                  TabelleVeranstaltung e = new TabelleVeranstaltung();
                  int vID = e.getVeranstaltungID(runApplication.letzterVeranstaltungsname);
                  String dateiname = runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Fahrzeugeinteilung/" + e.getVeranstaltungName2AndDatum(vID) + "_ID_" + vID + ".pdf";
                  new PDFPrinter(dateiname);
                  JOptionPane.showMessageDialog((Component)null, Konstante.DRUCKAUFTRAG_VERSENDET);
               }
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

            if(!FahrzeugEinteilungAO.this.buttonSpeichern.isEnabled()) {
               runApplication.letzterVeranstaltungsname = "<bitte wählen>";
            }

         }
      });
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleFahrzeugeinteilung tabEinteilung = new TabelleFahrzeugeinteilung();
            Fahrzeugeinteilung einteilung = new Fahrzeugeinteilung();

            try {
               int e = tabVeranstaltung.getVeranstaltungID(runApplication.letzterVeranstaltungsname);
               int jahr = tabVeranstaltung.getJahrDerVeranstaltung(e);
               int kID = tabVeranstaltung.getVeranstaltungKategorieID(e);
               String[] fahrzeugListe = Utils.listToArray(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());
               if(!FahrzeugEinteilungAO.konflikt_label.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_KONFILIKT_BESEITIGEN, "Fehlermeldung", 0);
               } else {
                  for(int dateiname = 0; dateiname < fahrzeugListe.length; ++dateiname) {
                     int fID = tabFahrzeuge.getFahrzeugID(fahrzeugListe[dateiname]);
                     int currentFahrzeugIsTrupp = tabFahrzeuge.getTrupp(fID);

                     for(int s = 0; s < 9; ++s) {
                        if(!(FahrzeugEinteilungAO.sitzplatz[dateiname][s].getSelectedItem().toString().equals("<bitte wählen>") | FahrzeugEinteilungAO.sitzplatz[dateiname][s].getSelectedItem().toString().equals((Object)null))) {
                           einteilung.setId(tabEinteilung.getNextNumer());
                           einteilung.setVeranstaltungID(e);
                           einteilung.setKategorie(kID);
                           einteilung.setJahr(jahr);
                           einteilung.setFahrzeugID(fID);
                           einteilung.setMitgliederID(tabMitglied.getIdByGuiString(FahrzeugEinteilungAO.sitzplatz[dateiname][s].getSelectedItem().toString()));
                           if(currentFahrzeugIsTrupp == 0) {
                              einteilung.setPosition(s);
                           } else if(s == 0) {
                              einteilung.setPosition(2);
                           } else if(s == 1) {
                              einteilung.setPosition(1);
                           } else if(s == 2) {
                              einteilung.setPosition(3);
                           }

                           tabEinteilung.insert(einteilung);
                        }
                     }
                  }

                  String var16 = runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Fahrzeugeinteilung/" + tabVeranstaltung.getVeranstaltungName2AndDatum(e) + "_ID_" + e + ".pdf";
                  Utils.dateiKatalogisieren(var16);
                  FarzeugeinteilungPDFSchreiben.PDFdocumentErstellen(var16);
                  logging.logInfo("fahrzeugeinteilung erfolgreich gespeichert");
                  tabVeranstaltung.updateFahrzeugeinteilung(e);
                  logging.logInfo("Veranstaltungstabelle aktualisiert, das die Fahrzeugeinteilung erfolgreich erstellt wurde");
                  logbuchEingabe.NeuerEintag("Fahrzeugeinteilung erstellt: " + runApplication.letzterVeranstaltungsname + " Details: " + var16);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  FahrzeugEinteilungAO.this.setDefaultCloseOperation(2);
                  FahrzeugEinteilungAO.this.buttonSpeichern.setEnabled(false);
               }
            } catch (IOException var15) {
               logging.logPrintStackTrace(var15);
            }

         }
      });
   }

   public void fensterAnzeigen() {
      MyEvent.setEvent("0x0030");
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
      if(!build.toString().equals(Konstante.FAHRZEUGEINTEILUNG_PROBLEME)) {
         logging.logInfo("Warnung wird angezeigt");
         JOptionPane.showMessageDialog((Component)null, build.toString(), "Warnung", 2);
      }

   }

   public void fensterSchlissen() {
      this.dispose();
   }
}
