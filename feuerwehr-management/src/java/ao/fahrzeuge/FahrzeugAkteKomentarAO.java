package ao.fahrzeuge;

import ao.AbstractFenster;
import ao.fahrzeuge.FahrzeugakteAO;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeugakte_kommentar;
import go.MitgliederFahrzeugAkte_Kommentar;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.mitgliedakte.PDFFahrzeugKommentar;
import run.runApplication;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class FahrzeugAkteKomentarAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextArea textfiled;
   private JScrollPane scrollPane;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public FahrzeugAkteKomentarAO() {
      super("FeuerwehrManagementSystem");
      logging.logInfo("Starte: FahrzeugAkteKommentarAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Zurück");
      this.buttonSpeichern = new JButton("Speichern");
      this.textfiled = new JTextArea();
      this.scrollPane = new JScrollPane(this.textfiled);
      this.scrollPane.setVerticalScrollBarPolicy(22);
      this.scrollPane.setPreferredSize(new Dimension(450, 300));
      this.modulBeschreibung = new JLabel("Fahrzeugakte Kommentar");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Fahrzeugakte Kommentar");
      this.setSize(500, 450);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.scrollPane);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
      this.textfiled.setCaretPosition(0);
      this.textfiled.setWrapStyleWord(true);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleFahrzeug e = new TabelleFahrzeug();
               int fID = Integer.parseInt(FahrzeugakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
               String fName = e.getFahrzeugName(fID);
               String dateiname = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + fID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
               PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fName, FahrzeugAkteKomentarAO.this.textfiled.getText());
               File ordner = new File(runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + fID);
               File[] files = ordner.listFiles();
               String[] fileName = new String[files.length];

               for(int kommentar = 0; kommentar < files.length; ++kommentar) {
                  fileName[kommentar] = files[kommentar].getName();
               }

               FahrzeugakteAO.liste.setListData(fileName);
               logbuchEingabe.NeuerEintag("Kommentar in die Fahrzeugakte eingetragen: " + FahrzeugakteAO.tree.getSelectionPath().getLastPathComponent().toString() + " Details: " + dateiname);
               MitgliederFahrzeugAkte_Kommentar var11 = new MitgliederFahrzeugAkte_Kommentar();
               var11.setId(fID);
               var11.setDatum(SbcUtils.timeStamp("yyyy-MM-dd"));
               var11.setZeit(SbcUtils.timeStamp("HH:mm"));
               var11.setKommentar(FahrzeugAkteKomentarAO.this.textfiled.getText());
               (new TabelleFahrzeugakte_kommentar()).insert(var11);
               Utils.dateiKatalogisieren(dateiname);
               FahrzeugAkteKomentarAO.this.dispose();
            } catch (SQLException var10) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var10);
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
