package ao.mangelmeldung;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleMaengelmeldung_kommentar;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.mitglied.TabelleMitglied;
import go.Mängelmeldung;
import go.Mängelmeldung_kommentar;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.maengelmeldung.MaengelmeldungPDFSchreiben;
import run.runApplication;
import service.EMailService;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.XML;
import utilities.logbuchEingabe;

public class MängelmeldungAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JComboBox mitglieder;
   private JComboBox fahrzeuge;
   private JLabel textfield_label;
   private JTextArea textfield;
   private JComboBox wann;
   private JLabel wann_label;
   private JScrollPane pane;
   private JLabel meldender_label;
   private JLabel fahrzeug_label;
   private JPanel panel;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public MängelmeldungAO() {
      super("FeuerwehrManagementSystem - Mängelmeldung");
      logging.logInfo("Starte: MangelmeldungAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern & Erstellen");
      this.buttonZurueck = new JButton("Schließen");
      this.textfield = new JTextArea(19, 50);
      this.textfield.setLineWrap(true);
      this.textfield.setWrapStyleWord(true);
      this.pane = new JScrollPane(this.textfield);
      this.pane.setVerticalScrollBarPolicy(22);
      this.textfield_label = new JLabel("Detaillierte Beschreibung / Kommentar:");
      this.meldender_label = new JLabel("Meldender: ");
      this.fahrzeug_label = new JLabel("Fahrzeug: ");
      String[] liste = new String[]{"<bitte wählen>", "Geräteprüfung", "Dienstabend", "Einsatz", "Übung", "Sonstiges"};
      this.wann = new JComboBox(liste);
      this.wann_label = new JLabel("Wann trat der Mangel auf: ");
      this.modulBeschreibung = new JLabel("Mängelmeldung");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();

      try {
         String[] e = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
         String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
         this.mitglieder = new JComboBox(mitgliederListe);
         this.fahrzeuge = new JComboBox(e);
         this.fahrzeuge.addItem("Gerätehaus");
         this.fahrzeuge.addItem("Sonstige");
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(600, 560);
      this.setTitle("FeuerwehrManagementSystem - Mängelmeldung");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.wann.setPreferredSize(new Dimension(250, 25));
      this.panel = new JPanel(new GridLayout(3, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.fahrzeug_label);
      this.panel.add(this.fahrzeuge);
      this.panel.add(this.meldender_label);
      this.panel.add(this.mitglieder);
      this.panel.add(this.wann_label);
      this.panel.add(this.wann);
      this.add(this.textfield_label);
      this.add(this.pane);
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
               TabelleFahrzeug e = new TabelleFahrzeug();
               TabelleMitglied tabMitglieder = new TabelleMitglied();
               TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
               TabelleMaengelmeldung_kommentar tabMangelKommentar = new TabelleMaengelmeldung_kommentar();
               Mängelmeldung mangel = new Mängelmeldung();
               Mängelmeldung_kommentar kommentarObjekt = new Mängelmeldung_kommentar();
               int ID = tabMangel.getNextNummer();
               int fID = e.getFahrzeugID(MängelmeldungAO.this.fahrzeuge.getSelectedItem().toString());
               if(MängelmeldungAO.this.fahrzeuge.getSelectedItem().toString().equals("<bitte wählen>")) {
                  logging.logInfo("Es wurde kein Fahrzeug ausgewählt");
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_FAHRZEUG_WAEHLEN, "Warnung", 2);
               } else if(MängelmeldungAO.this.mitglieder.getSelectedItem().toString().equals("<bitte wählen>")) {
                  logging.logInfo("Es wurde kein Mitglied ausgewählt");
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
               } else if(MängelmeldungAO.this.wann.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_BESCHREIBUNG_ANGEBEN, "Warnung", 2);
               } else if(!(new File((String)runApplication.EINSTELLUNGEN.get("mängelmeldung"))).exists()) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.MAENGELMELDUNG_BERICHT_NICHT_VORHANDEN + (String)runApplication.EINSTELLUNGEN.get("mängelmeldung"), "Fehlermeldung", 0);
               } else {
                  int mID = tabMitglieder.getIdByGuiString(MängelmeldungAO.this.mitglieder.getSelectedItem().toString());
                  int mandantID = Integer.parseInt((String)runApplication.PROPERTIES.get("MandantID"));
                  String wannTratDerMangelAuf;
                  if(fID != 0) {
                     wannTratDerMangelAuf = MängelmeldungAO.this.wann.getSelectedItem().toString() + " (Mangel-ID" + ID + ", Fahrzeug: " + MängelmeldungAO.this.fahrzeuge.getSelectedItem().toString() + ")";
                  } else {
                     wannTratDerMangelAuf = MängelmeldungAO.this.wann.getSelectedItem().toString() + " (Mangel-ID" + ID + ", Art / Ort: " + MängelmeldungAO.this.fahrzeuge.getSelectedItem().toString() + ")";
                  }

                  mangel.setId(ID);
                  mangel.setJahr(Integer.parseInt(SbcUtils.timeStamp("yyyy")));
                  mangel.setMitgliedID(mID);
                  mangel.setFahrzeugID(fID);
                  mangel.setDatum(SbcUtils.timeStamp("dd.MM.yyyy"));
                  mangel.setWann(wannTratDerMangelAuf);
                  mangel.setBeschreibung(MängelmeldungAO.this.textfield.getText());
                  if(((String)runApplication.EINSTELLUNGEN.get("MängelBerichtArt")).equals("Word Schnittstelle")) {
                     mangel.setDateiname("Meldung_ID_" + ID + ".doc");
                  } else {
                     mangel.setDateiname("Meldung_ID_" + ID + ".pdf");
                  }

                  mangel.setStatus(0);
                  tabMangel.insert(mangel);
                  kommentarObjekt.setMangelID(ID);
                  kommentarObjekt.setKommentarID(tabMangelKommentar.getNextKommentarNummer(ID, mandantID));
                  kommentarObjekt.setDatum(SbcUtils.timeStamp("yyyy-MM-dd"));
                  kommentarObjekt.setZeit(SbcUtils.timeStamp("HH:mm:ss"));
                  kommentarObjekt.setKommentar("Mängelmeldung wurde erstellt");
                  kommentarObjekt.setUser(runApplication.loginName);
                  kommentarObjekt.setMandantID(mandantID);
                  tabMangelKommentar.insert(kommentarObjekt);
                  kommentarObjekt.setMangelID(ID);
                  kommentarObjekt.setKommentarID(tabMangelKommentar.getNextKommentarNummer(ID, mandantID));
                  kommentarObjekt.setDatum(SbcUtils.timeStamp("yyyy-MM-dd"));
                  kommentarObjekt.setZeit(SbcUtils.timeStamp("HH:mm:ss"));
                  kommentarObjekt.setKommentar("Details zur Mängelmeldung:\nFahrzeug: " + MängelmeldungAO.this.fahrzeuge.getSelectedItem() + "\nMeldender: " + MängelmeldungAO.this.mitglieder.getSelectedItem() + "\nWann trat der Mangel auf: " + mangel.getWann() + "\n\nBeschreibung:\n" + mangel.getBeschreibung());
                  kommentarObjekt.setUser(runApplication.loginName);
                  kommentarObjekt.setMandantID(mandantID);
                  tabMangelKommentar.insert(kommentarObjekt);
                  String dateiname;
                  if(((String)runApplication.EINSTELLUNGEN.get("MängelBerichtArt")).equals("Word Schnittstelle")) {
                     dateiname = runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Mangel/Meldung_ID_" + ID + ".xml";
                     String[] ist = new String[]{"wfahr", "wdate", "wkenn", "wfunk", "wwann", "wpers", "wbesc"};
                     String[] zu = new String[]{Utils.checkTextAndRemoveIllegalSigns(MängelmeldungAO.this.fahrzeuge.getSelectedItem().toString()), SbcUtils.timeStamp("dd.MM.yyyy"), Utils.checkTextAndRemoveIllegalSigns(e.getKennezeichen(fID)), Utils.checkTextAndRemoveIllegalSigns(e.getFunkrufname(fID)), Utils.checkTextAndRemoveIllegalSigns(wannTratDerMangelAuf), Utils.checkTextAndRemoveIllegalSigns(MängelmeldungAO.this.mitglieder.getSelectedItem().toString()), Utils.checkTextAndRemoveIllegalSigns(MängelmeldungAO.this.textfield.getText())};
                     XML.createEinsatzBericht(ist, zu, dateiname, (String)runApplication.EINSTELLUNGEN.get("mängelmeldung"));
                     File docFile = new File(runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Mangel/Meldung_ID_" + ID + ".doc");
                     (new File(dateiname)).renameTo(docFile);
                     dateiname = Utils.removeBackSlashFromString(docFile.getAbsolutePath());
                  } else {
                     dateiname = runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Mangel/Meldung_ID_" + ID + ".pdf";
                     MaengelmeldungPDFSchreiben.PDFdocumentErstellen(dateiname, mangel, MängelmeldungAO.this.fahrzeuge.getSelectedItem().toString());
                  }

                  logbuchEingabe.NeuerEintag("Mägelmeldung erstellt: " + wannTratDerMangelAuf + " Details: " + dateiname);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  if(((String)runApplication.EINSTELLUNGEN.get("emailModul")).equals("1") && ((String)runApplication.EINSTELLUNGEN.get("mängelmeldungViaEMailVersenden")).equals("1")) {
                     EMailService.EMailInformationServiceMängelmeldung(mangel);
                  }

                  Utils.dateiKatalogisieren(dateiname);
                  MängelmeldungAO.this.dispose();
                  logging.logInfo("Öffne Datei: " + dateiname);
                  Desktop.getDesktop().open(new File(dateiname));
               }
            } catch (DocumentException var17) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var17);
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
