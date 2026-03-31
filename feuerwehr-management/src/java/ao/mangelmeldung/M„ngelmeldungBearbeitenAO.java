package ao.mangelmeldung;

import ao.AbstractFenster;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleMaengelmeldung_kommentar;
import data.tabellen.einstellungen.TabelleMandant;
import go.Mängelmeldung_kommentar;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class MängelmeldungBearbeitenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonKommentarEintragen;
   private JButton buttonAnsehen;
   private JButton buttonWiederoeffnung;
   public static JComboBox mandant;
   private JLabel mandant_label;
   public static JTextArea liste;
   private JScrollPane pane_liste;
   public static JTree tree;
   private JScrollPane scrollPaneTree;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JLabel dummy3;


   public MängelmeldungBearbeitenAO() {
      super("FeuerwehrManagementSystem - Mängelmeldung bearbeiten");
      logging.logInfo("Starte: MängelmeldungBearbeiten AO");
   }

   protected void buttonErstellen() {
      this.buttonKommentarEintragen = new JButton("Kommentar eintragen");
      this.buttonZurueck = new JButton("Zurück");
      this.buttonAnsehen = new JButton("Ansehen");
      this.buttonWiederoeffnung = new JButton("Wiedereröffnung");
      this.mandant_label = new JLabel("Mandant: ");
      this.modulBeschreibung = new JLabel("Mängelmeldung bearbeiten");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.dummy3 = new JLabel(runApplication.dummyImage);
      tree = new JTree(CreateTrees.CreateTreeMaengelListe((String)runApplication.PROPERTIES.get("MandantID")));
      tree.setSelectionRow(1);
      this.scrollPaneTree = new JScrollPane(tree);
      this.scrollPaneTree.setVerticalScrollBarPolicy(22);
      liste = new JTextArea();
      this.pane_liste = new JScrollPane(liste);
      this.pane_liste.setVerticalScrollBarPolicy(22);
      this.pane_liste.setPreferredSize(new Dimension(600, 200));
   }

   protected void labelErstellen() {
      try {
         TabelleMandant e = new TabelleMandant();
         String[] mandantListe = Utils.listToArray(e.getAllMandanten());
         mandant = new JComboBox(mandantListe);
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

      mandant.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            try {
               TabelleMandant e = new TabelleMandant();
               MängelmeldungBearbeitenAO.tree.setModel(CreateTrees.CreateTreeMaengelListe(Integer.toString(e.getMandantID(MängelmeldungBearbeitenAO.mandant.getSelectedItem().toString()))));
               MängelmeldungBearbeitenAO.liste.setText((String)null);
               if(MängelmeldungBearbeitenAO.mandant.getSelectedItem().toString().equals(runApplication.mandantName)) {
                  MängelmeldungBearbeitenAO.this.buttonAnsehen.setVisible(true);
               } else {
                  MängelmeldungBearbeitenAO.this.buttonAnsehen.setVisible(false);
               }
            } catch (SQLException var3) {
               logging.logPrintStackTrace(var3);
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
      if(runApplication.BF == 1) {
         this.setSize(1000, 630);
      } else {
         this.setSize(1000, 600);
      }

      this.setTitle("FeuerwehrManagementSystem - Mängelmeldung bearbeiten");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      if(runApplication.BF == 1) {
         this.add(this.mandant_label);
         this.add(mandant);
         this.add(this.dummy3);
      }

      this.scrollPaneTree.setPreferredSize(new Dimension(300, 450));
      this.add(this.scrollPaneTree);
      this.pane_liste.setPreferredSize(new Dimension(600, 450));
      this.add(this.pane_liste);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonAnsehen);
      this.add(this.buttonWiederoeffnung);
      this.add(this.buttonKommentarEintragen);
      this.buttonKommentarEintragen.setEnabled(false);
      this.buttonAnsehen.setEnabled(false);
      this.buttonWiederoeffnung.setVisible(false);
      mandant.setSelectedItem(runApplication.mandantName);
      liste.setWrapStyleWord(true);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent arg0) {
            try {
               Object[] e = MängelmeldungBearbeitenAO.tree.getSelectionPath().getPath();
               TabelleMaengelmeldung_kommentar tabMangelKommentar = new TabelleMaengelmeldung_kommentar();
               TabelleMandant tabMandant = new TabelleMandant();
               int maID = Integer.parseInt(MängelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().substring(9, MängelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().length()));
               int mandantID = tabMandant.getMandantID(MängelmeldungBearbeitenAO.mandant.getSelectedItem().toString());
               MängelmeldungBearbeitenAO.liste.setText(tabMangelKommentar.getKommentarListe(maID, mandantID));
               if(e[2].toString().equals("Offene Mängelmeldungen")) {
                  MängelmeldungBearbeitenAO.this.buttonKommentarEintragen.setEnabled(true);
                  MängelmeldungBearbeitenAO.this.buttonAnsehen.setEnabled(true);
                  MängelmeldungBearbeitenAO.this.buttonWiederoeffnung.setVisible(false);
               } else {
                  MängelmeldungBearbeitenAO.this.buttonKommentarEintragen.setEnabled(false);
                  MängelmeldungBearbeitenAO.this.buttonAnsehen.setEnabled(true);
                  MängelmeldungBearbeitenAO.this.buttonWiederoeffnung.setVisible(true);
               }
            } catch (SQLException var7) {
               logging.logPrintStackTrace(var7);
            } catch (NumberFormatException var8) {
               ;
            } catch (StringIndexOutOfBoundsException var9) {
               ;
            } catch (NullPointerException var10) {
               MängelmeldungBearbeitenAO.liste.setText((String)null);
            }

         }
      });
      this.buttonWiederoeffnung.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
            TabelleMaengelmeldung_kommentar tabKommentar = new TabelleMaengelmeldung_kommentar();
            Mängelmeldung_kommentar kommentarObjekt = new Mängelmeldung_kommentar();
            TabelleMandant tabMandant = new TabelleMandant();

            try {
               int e = Integer.parseInt(MängelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().substring(9, MängelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().length()));
               int mandantID = tabMandant.getMandantID(MängelmeldungBearbeitenAO.mandant.getSelectedItem().toString());
               tabMangel.updateStatus(e, 0);
               kommentarObjekt.setMangelID(e);
               kommentarObjekt.setKommentarID(tabKommentar.getNextKommentarNummer(e, mandantID));
               kommentarObjekt.setDatum(SbcUtils.timeStamp("yyyy-MM-dd"));
               kommentarObjekt.setZeit(SbcUtils.timeStamp("HH:mm:ss"));
               kommentarObjekt.setKommentar("Wiederöffnung der Mängelmeldung!\nDer Mangel muss nocheinmal bearbeitet werden.");
               kommentarObjekt.setUser(runApplication.loginName);
               kommentarObjekt.setMandantID(mandantID);
               tabKommentar.insert(kommentarObjekt);
               MängelmeldungBearbeitenAO.liste.setText(tabKommentar.getKommentarListe(e, mandantID));
               MängelmeldungBearbeitenAO.tree.setModel(CreateTrees.CreateTreeMaengelListe(Integer.toString(mandantID)));
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               logbuchEingabe.NeuerEintag("Mängelmeldung: " + MängelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString() + " wurde wieder geöffnet");
            } catch (SQLException var8) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var8);
            }

         }
      });
      this.buttonKommentarEintragen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.MAENGELMELDUNG_KOMMENTAR);
            Steuerung.steuerung();
         }
      });
      this.buttonAnsehen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleMaengelmeldung e = new TabelleMaengelmeldung();
               Object[] auswahl = MängelmeldungBearbeitenAO.tree.getSelectionPath().getPath();
               int maID = Integer.parseInt(MängelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().substring(9, MängelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().length()));
               String dateiname = runApplication.arbeitsverzeichnis + "data/" + auswahl[1] + "/Mangel/" + e.getDateinameByID(maID);
               Desktop.getDesktop().open(new File(dateiname));
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
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
