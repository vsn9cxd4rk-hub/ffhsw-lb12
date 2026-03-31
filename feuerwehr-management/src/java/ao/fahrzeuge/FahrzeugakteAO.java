package ao.fahrzeuge;

import ao.AbstractFenster;
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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Datei;
import utilities.Konstante;

public class FahrzeugakteAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonAnsehen;
   private JButton buttonDrucken;
   private JButton buttonAlsEMailSenden;
   private JButton buttonKommentar;
   private JButton buttonHochladen;
   private JFileChooser chooser;
   public static JList liste;
   private JScrollPane pane_liste;
   public static JTree tree;
   private JScrollPane scrollPaneTree;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private static String aktuellerOrdner = null;


   public FahrzeugakteAO() {
      super("FeuerwehrManagementSystem - Fahrzeugakte");
      logging.logInfo("Starte: " + this.getName());
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonAnsehen = new JButton("Öffnen");
      this.buttonDrucken = new JButton("Drucken");
      this.buttonAlsEMailSenden = new JButton("Als E-Mail senden");
      this.buttonKommentar = new JButton("Kommentar einfügen");
      this.buttonHochladen = new JButton("Datei einfügen");
      this.modulBeschreibung = new JLabel("Fahrzeugakte");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      tree = new JTree(CreateTrees.CreateTreeFahrzeugListe());
      tree.setSelectionRow(1);
      this.scrollPaneTree = new JScrollPane(tree);
      this.scrollPaneTree.setVerticalScrollBarPolicy(22);
      tree.setSelectionRow(0);
   }

   protected void setzeAuswahllisten() {
      liste = new JList();
      liste.setVisibleRowCount(15);
      liste.setToolTipText("Liste der verfügbaren Anhänge");
      this.pane_liste = new JScrollPane(liste);
      this.pane_liste.setVerticalScrollBarPolicy(22);
      this.pane_liste.setPreferredSize(new Dimension(600, 200));
   }

   protected void boxenHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Fahrzeugakte");
      this.setSize(1020, 620);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.scrollPaneTree.setPreferredSize(new Dimension(330, 450));
      this.add(this.scrollPaneTree);
      this.pane_liste.setPreferredSize(new Dimension(630, 450));
      this.add(this.pane_liste);
      this.add(this.buttonAnsehen);
      this.add(this.buttonDrucken);
      this.add(this.buttonKommentar);
      this.add(this.buttonHochladen);

      try {
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1) {
            this.add(this.buttonAlsEMailSenden);
         }
      } catch (NumberFormatException var2) {
         logging.logInfo(var2);
      }

      this.add(this.dummy2);
      this.add(this.buttonZurueck);
   }

   protected void labelHinzufuegen() {}

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent e) {
            try {
               int ex = Integer.parseInt(FahrzeugakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
               File ordner = new File(runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + ex);
               File[] files = ordner.listFiles();
               String[] fileName = new String[files.length];

               for(int i = 0; i < files.length; ++i) {
                  fileName[i] = files[i].getName();
               }

               FahrzeugakteAO.aktuellerOrdner = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + ex + "/";
               FahrzeugakteAO.liste.setListData(fileName);
            } catch (StringIndexOutOfBoundsException var7) {
               FahrzeugakteAO.tree.expandPath(FahrzeugakteAO.tree.getSelectionPath());
               FahrzeugakteAO.liste.setListData(new String[0]);
            } catch (NullPointerException var8) {
               FahrzeugakteAO.liste.setListData(new String[0]);
            } catch (NumberFormatException var9) {
               FahrzeugakteAO.liste.setListData(new String[0]);
            }

         }
      });
      liste.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
               FahrzeugakteAO.this.buttonAnsehen.doClick();
            }

         }
      });
      this.buttonAlsEMailSenden.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               runApplication.mailData.setStatus(1);
               runApplication.mailData.setAnhang(FahrzeugakteAO.aktuellerOrdner + FahrzeugakteAO.liste.getSelectedValue().toString() + ",");
               Steuerung.setStatus(Status.NEUE_EMAIL);
               Steuerung.steuerung();
            } catch (NullPointerException var3) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_EINTRAG_WAEHLEN, "Warnung", 2);
            }

         }
      });
      this.buttonDrucken.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               String e = FahrzeugakteAO.aktuellerOrdner + FahrzeugakteAO.liste.getSelectedValue().toString();
               Desktop.getDesktop().print(new File(e));
            } catch (IOException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonAnsehen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               String e = FahrzeugakteAO.aktuellerOrdner + FahrzeugakteAO.liste.getSelectedValue().toString();
               Desktop.getDesktop().open(new File(e));
            } catch (IOException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonHochladen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            FahrzeugakteAO.this.chooser = new JFileChooser();
            int returnVal = FahrzeugakteAO.this.chooser.showOpenDialog(FahrzeugakteAO.this.chooser);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + FahrzeugakteAO.this.chooser.getSelectedFile().getPath());
            }

            try {
               int e = Integer.parseInt(FahrzeugakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
               String name = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + e + "/" + FahrzeugakteAO.this.chooser.getSelectedFile().getName();
               if((new File(name)).exists()) {
                  int ordnerBeteiligung = JOptionPane.showConfirmDialog((Component)null, Konstante.DATEI_EXISTIERT_BEREITS, "Frage", 0);
                  if(ordnerBeteiligung == 0) {
                     logging.logInfo("Datei existiert bereits und Benutzer möchte sie ersetzen...");
                     Datei.copyFileAusführen(new File(FahrzeugakteAO.this.chooser.getSelectedFile().getPath()), name);
                  }
               } else {
                  logging.logInfo("Datei existiert nicht, es wird kopiert");
                  Datei.copyFileAusführen(new File(FahrzeugakteAO.this.chooser.getSelectedFile().getPath()), name);
               }

               File var10 = new File(runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + e);
               File[] dateilisteBeteiligung = var10.listFiles();
               String[] fileName = new String[dateilisteBeteiligung.length];

               for(int i = 0; i < dateilisteBeteiligung.length; ++i) {
                  fileName[i] = dateilisteBeteiligung[i].getName();
               }

               FahrzeugakteAO.aktuellerOrdner = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + e + "/";
               FahrzeugakteAO.liste.setListData(fileName);
            } catch (IOException var9) {
               logging.logPrintStackTrace(var9);
            }

         }
      });
      this.buttonKommentar.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               int e = Integer.parseInt(FahrzeugakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
               Steuerung.setStatus(Status.FAHRZEUGAKTE_KOMMENTAR);
               Steuerung.steuerung();
            } catch (StringIndexOutOfBoundsException var3) {
               FahrzeugakteAO.tree.expandPath(FahrzeugakteAO.tree.getSelectionPath());
            } catch (NullPointerException var4) {
               ;
            } catch (NumberFormatException var5) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_FAHRZEUG_WAEHLEN, "Warnung", 2);
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
