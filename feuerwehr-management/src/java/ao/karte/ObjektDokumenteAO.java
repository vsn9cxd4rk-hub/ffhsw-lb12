package ao.karte;

import ao.AbstractFenster;
import ao.karte.ObjektEintragenAO;
import data.tabellen.einstellungen.TabelleFTPSync;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Datei;
import utilities.Konstante;
import utilities.Utils;

public class ObjektDokumenteAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonHinzufügen;
   private JButton buttonLöschen;
   private JButton buttonÖffnen;
   private JButton buttonSpeichernUnter;
   private JList liste;
   private JScrollPane pane_liste;
   private JFileChooser chooser;
   private String aktuellerOrdner;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public ObjektDokumenteAO() {
      super("FeuerwehrManagementSystem - Objekt Dokumente");
      logging.logInfo("Starte: ObjektDokumenteAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonHinzufügen = new JButton("Hinzufügen");
      this.buttonLöschen = new JButton("Löschen");
      this.buttonÖffnen = new JButton("Öffnen");
      this.buttonSpeichernUnter = new JButton("Speicher unter");
      this.modulBeschreibung = new JLabel("Objekt Dokumente - " + ObjektEintragenAO.name.getText() + " (" + ObjektEintragenAO.StrassenName.getSelectedItem() + " " + ObjektEintragenAO.hausnummer.getText() + ")");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.chooser = new JFileChooser();
      this.liste = new JList();
      this.liste.setVisibleRowCount(15);
      this.liste.setToolTipText("Liste der verfügbaren Objekt Dokumente");
      this.pane_liste = new JScrollPane(this.liste);
      this.pane_liste.setVerticalScrollBarPolicy(22);
      this.pane_liste.setPreferredSize(new Dimension(600, 200));
   }

   protected void labelErstellen() {
      this.aktuellerOrdner = runApplication.arbeitsverzeichnis + "data/Objektakte/" + ObjektEintragenAO.id.getText() + "/";
      if((new File(this.aktuellerOrdner)).exists()) {
         File ordnerBeteiligung = new File(this.aktuellerOrdner);
         File[] dateiListe = ordnerBeteiligung.listFiles();
         this.liste.setListData(this.prepareFileNameForList(dateiListe));
      }

   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(700, 500);
      this.setTitle("FeuerwehrManagementSystem - Objekt Dokumente");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.pane_liste.setPreferredSize(new Dimension(600, 350));
      this.add(this.pane_liste);
      this.add(this.dummy2);
      this.add(this.buttonÖffnen);
      this.add(this.buttonHinzufügen);
      this.add(this.buttonSpeichernUnter);
      this.add(this.buttonLöschen);
      this.add(this.buttonZurueck);
   }

   protected void boxenHinzufuegen() {}

   private String[] prepareFileNameForList(File[] files) {
      String[] fileName = new String[files.length];

      for(int i = 0; i < files.length; ++i) {
         fileName[i] = files[i].getName();
      }

      return fileName;
   }

   protected void actionErzeugen() {
      this.liste.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
               ObjektDokumenteAO.this.buttonÖffnen.doClick();
            }

         }
      });
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonHinzufügen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               ObjektDokumenteAO.this.chooser = new JFileChooser();
               ObjektDokumenteAO.this.chooser.setFileSelectionMode(2);
               if(!(new File(ObjektDokumenteAO.this.aktuellerOrdner)).exists()) {
                  logging.logInfo("Objektakte existiert nicht für dieses Objekt und wird angelegt...");
                  Utils.ordnerErstellen(ObjektDokumenteAO.this.aktuellerOrdner, (String)runApplication.PROPERTIES.get("ClientID"));
               }

               int e1 = ObjektDokumenteAO.this.chooser.showOpenDialog(ObjektDokumenteAO.this.chooser);
               if(e1 == 0) {
                  logging.logInfo("Ausgewählte Datei: " + ObjektDokumenteAO.this.chooser.getSelectedFile().getPath());
               }

               String name = ObjektDokumenteAO.this.aktuellerOrdner + ObjektDokumenteAO.this.chooser.getSelectedFile().getName();
               Datei.copyFileAusführen(new File(ObjektDokumenteAO.this.chooser.getSelectedFile().getPath()), name);
               Utils.dateiKatalogisieren(name);
               File ordnerBeteiligung = new File(ObjektDokumenteAO.this.aktuellerOrdner);
               File[] dateiListe = ordnerBeteiligung.listFiles();
               ObjektDokumenteAO.this.liste.setListData(ObjektDokumenteAO.this.prepareFileNameForList(dateiListe));
            } catch (IOException var6) {
               logging.logPrintStackTrace(var6);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            }

         }
      });
      this.buttonLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            File file = new File(ObjektDokumenteAO.this.aktuellerOrdner + ObjektDokumenteAO.this.liste.getSelectedValue().toString());

            try {
               int e1 = JOptionPane.showConfirmDialog((Component)null, Konstante.WIRKLICH_LOESCHEN, "Frage", 0);
               if(e1 == 0) {
                  TabelleFTPSync tabSync = new TabelleFTPSync();
                  Datei.copyFileAusführen(file, runApplication.arbeitsverzeichnis + "data/papierkorb/" + file.getName());
                  file.delete();
                  Utils.dateiKatalogisierenForDelete(ObjektDokumenteAO.this.aktuellerOrdner + ObjektDokumenteAO.this.liste.getSelectedValue().toString());
                  tabSync.deleteOneFile(Utils.removeBackSlashFromString(ObjektDokumenteAO.this.aktuellerOrdner + ObjektDokumenteAO.this.liste.getSelectedValue().toString()));
                  logging.logInfo("Datei: " + file.toString() + " wurde in den Papierkorb verschoben");
                  File[] dateilisteBeteiligung = (new File(ObjektDokumenteAO.this.aktuellerOrdner)).listFiles();
                  ObjektDokumenteAO.this.liste.setListData(ObjektDokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
               }
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      this.buttonÖffnen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               String e1 = ObjektDokumenteAO.this.aktuellerOrdner + ObjektDokumenteAO.this.liste.getSelectedValue().toString();
               Desktop.getDesktop().open(new File(e1));
               (new TabelleFTPSync()).updateFTPSync_StatusResert(e1, runApplication.clientID);
            } catch (SQLException var3) {
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_OEFFNEN, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonSpeichernUnter.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               ObjektDokumenteAO.this.chooser = new JFileChooser();
               ObjektDokumenteAO.this.chooser.setFileSelectionMode(1);
               ObjektDokumenteAO.this.chooser.showSaveDialog((Component)null);
               String e1 = null;
               String outout = null;
               e1 = ObjektDokumenteAO.this.liste.getSelectedValue().toString();
               outout = ObjektDokumenteAO.this.chooser.getSelectedFile().getPath() + "/" + e1;
               Datei.copyFileAusführen(new File(ObjektDokumenteAO.this.aktuellerOrdner + e1), outout);
               logging.logInfo("Datei wurde erfolgreich kopiert");
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
            } catch (IOException var4) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var4);
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
