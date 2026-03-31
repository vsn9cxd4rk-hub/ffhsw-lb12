package ao.mitglieder;

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
import java.sql.SQLException;
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
import utilities.Utils;

public class MitgliederakteAO extends AbstractFenster {

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
   private static String aktuellerOrdner;


   public MitgliederakteAO() {
      super("FeuerwehrManagementSystem - Mitgliederakte");
      logging.logInfo("Starte: AttachmentAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonAnsehen = new JButton("Öffnen");
      this.buttonDrucken = new JButton("Drucken");
      this.buttonAlsEMailSenden = new JButton("Als E-Mail senden");
      this.buttonKommentar = new JButton("Kommentar einfügen");
      this.buttonHochladen = new JButton("Datei einfügen");
      this.modulBeschreibung = new JLabel("Mitgliederakte");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      tree = new JTree(CreateTrees.CreateTreeMitgliederListe((String)null));
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
      this.setTitle("FeuerwehrManagementSystem - Mitgliederakte");
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
               int ex = Integer.parseInt(MitgliederakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
               File ordner = new File(runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + ex);
               File[] files = ordner.listFiles();
               String[] fileName = new String[files.length];

               for(int i = 0; i < files.length; ++i) {
                  fileName[i] = files[i].getName();
               }

               MitgliederakteAO.aktuellerOrdner = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + ex + "/";
               MitgliederakteAO.liste.setListData(fileName);
            } catch (StringIndexOutOfBoundsException var7) {
               MitgliederakteAO.tree.expandPath(MitgliederakteAO.tree.getSelectionPath());
               MitgliederakteAO.liste.setListData(new String[0]);
            } catch (NullPointerException var8) {
               MitgliederakteAO.liste.setListData(new String[0]);
            } catch (NumberFormatException var9) {
               MitgliederakteAO.liste.setListData(new String[0]);
            }

         }
      });
      liste.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
               MitgliederakteAO.this.buttonAnsehen.doClick();
            }

         }
      });
      this.buttonKommentar.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               int e = Integer.parseInt(MitgliederakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
               Steuerung.setStatus(Status.MITGLIEDERAKTE_KOMMENTAR);
               Steuerung.steuerung();
            } catch (StringIndexOutOfBoundsException var3) {
               MitgliederakteAO.tree.expandPath(MitgliederakteAO.tree.getSelectionPath());
            } catch (NullPointerException var4) {
               ;
            } catch (NumberFormatException var5) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
            }

         }
      });
      this.buttonAlsEMailSenden.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               runApplication.mailData.setStatus(1);
               runApplication.mailData.setAnhang(MitgliederakteAO.aktuellerOrdner + MitgliederakteAO.liste.getSelectedValue().toString() + ",");
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
               String e = MitgliederakteAO.aktuellerOrdner + MitgliederakteAO.liste.getSelectedValue().toString();
               Desktop.getDesktop().print(new File(e));
            } catch (IOException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonAnsehen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               String e = MitgliederakteAO.aktuellerOrdner + MitgliederakteAO.liste.getSelectedValue().toString();
               Desktop.getDesktop().open(new File(e));
            } catch (IOException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonHochladen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            MitgliederakteAO.this.chooser = new JFileChooser();
            int returnVal = MitgliederakteAO.this.chooser.showOpenDialog(MitgliederakteAO.this.chooser);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + MitgliederakteAO.this.chooser.getSelectedFile().getPath());
            }

            try {
               int e = Integer.parseInt(MitgliederakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
               String mitgliederAktePath = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + e + "/" + MitgliederakteAO.this.chooser.getSelectedFile().getName();
               if((new File(mitgliederAktePath)).exists()) {
                  int ordnerBeteiligung = JOptionPane.showConfirmDialog((Component)null, Konstante.DATEI_EXISTIERT_BEREITS, "Frage", 0);
                  if(ordnerBeteiligung == 0) {
                     logging.logInfo("Datei existiert bereits und Benutzer möchte sie ersetzen...");
                     Datei.copyFileAusführen(new File(MitgliederakteAO.this.chooser.getSelectedFile().getPath()), mitgliederAktePath);
                     Utils.dateiKatalogisieren(mitgliederAktePath);
                  }
               } else {
                  logging.logInfo("Datei existiert nicht, es wird kopiert");
                  Datei.copyFileAusführen(new File(MitgliederakteAO.this.chooser.getSelectedFile().getPath()), mitgliederAktePath);
                  Utils.dateiKatalogisieren(mitgliederAktePath);
               }

               File var10 = new File(runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + e);
               File[] dateilisteBeteiligung = var10.listFiles();
               String[] fileName = new String[dateilisteBeteiligung.length];

               for(int i = 0; i < dateilisteBeteiligung.length; ++i) {
                  fileName[i] = dateilisteBeteiligung[i].getName();
               }

               MitgliederakteAO.aktuellerOrdner = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + e + "/";
               MitgliederakteAO.liste.setListData(fileName);
            } catch (SQLException var9) {
               logging.logPrintStackTrace(var9);
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
