package ao.einsatz;

import ao.AbstractFenster;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleVeranstaltung;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;

public class VerdienstausfallAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   public static JButton buttonErstellen;
   private JButton buttonDrucken;
   private JButton buttonAlleDrucken;
   private JButton buttonAnsehen;
   private JButton buttonSendenEMail;
   public static JComboBox veranstaltung;
   private JLabel beschreibung;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   public static String aktuellerOrdner;
   private JPanel panelKategorie;
   public static JList liste;
   private JScrollPane pane_liste;


   public VerdienstausfallAO() {
      super("FeuerwehrManagementSystem - Verdienstausfall");
      logging.logInfo("Starte: VerdienstausfallAO");
   }

   protected void buttonErstellen() {
      buttonErstellen = new JButton("Erstellen");
      this.buttonZurueck = new JButton("Schließen");
      this.buttonDrucken = new JButton("Drucken");
      this.buttonAlleDrucken = new JButton("Alle Drucken");
      this.buttonAnsehen = new JButton("Öffnen");
      this.buttonSendenEMail = new JButton("Als E-Mail senden");
      this.modulBeschreibung = new JLabel("Verdienstausfallbescheinigung");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.beschreibung = new JLabel("Veranstaltung / Einsatz: ");
      liste = new JList();
      liste.setVisibleRowCount(15);
      liste.setToolTipText("Liste der verfügbaren Berichte");
      this.pane_liste = new JScrollPane(liste);
      this.pane_liste.setVerticalScrollBarPolicy(22);
      this.pane_liste.setPreferredSize(new Dimension(700, 200));
      TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();

      try {
         String[] e = Utils.listToArrayOnlyFORComboBoxes(tabVeransatltung.getAllVeranstaltungEinerKategorieByJahr(1, Integer.parseInt(SbcUtils.timeStamp("yyyy")), 1));
         veranstaltung = new JComboBox(e);
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

   }

   protected void labelErstellen() {
      veranstaltung.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleEinsatz tabEinsatz = new TabelleEinsatz();

            try {
               int e = tabVeranstaltung.getVeranstaltungID(VerdienstausfallAO.veranstaltung.getSelectedItem().toString());
               int eID = tabEinsatz.getEinsatzIDByVeranstaltungID(e);
               File file = new File(runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/verdienstausfall/Einsatz_ID_" + eID);
               if(file.exists()) {
                  File[] dateilisteF = file.listFiles();
                  String[] fileName = new String[dateilisteF.length];

                  for(int i = 0; i < dateilisteF.length; ++i) {
                     fileName[i] = dateilisteF[i].getName();
                  }

                  VerdienstausfallAO.aktuellerOrdner = runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/verdienstausfall/Einsatz_ID_" + eID + "/";
                  VerdienstausfallAO.liste.setListData(fileName);
                  VerdienstausfallAO.buttonErstellen.setEnabled(false);
               } else {
                  VerdienstausfallAO.liste.setListData(new String[0]);
                  VerdienstausfallAO.buttonErstellen.setEnabled(true);
               }
            } catch (SQLException var10) {
               logging.logPrintStackTrace(var10);
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
      this.setSize(750, 380);
      this.setTitle("FeuerwehrManagementSystem - Verdienstausfall");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelKategorie = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panelKategorie);
      this.panelKategorie.add(this.beschreibung);
      this.panelKategorie.add(veranstaltung);
      this.add(this.pane_liste);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(buttonErstellen);
      this.add(this.buttonAnsehen);
      this.add(this.buttonDrucken);
      this.add(this.buttonAlleDrucken);

      try {
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1) {
            this.add(this.buttonSendenEMail);
         }
      } catch (NumberFormatException var2) {
         logging.logPrintStackTrace(var2);
      }

   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      liste.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
               VerdienstausfallAO.this.buttonAnsehen.doClick();
            }

         }
      });
      this.buttonSendenEMail.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               runApplication.mailData.setStatus(1);
               runApplication.mailData.setAnhang(VerdienstausfallAO.aktuellerOrdner + VerdienstausfallAO.liste.getSelectedValue().toString() + ",");
               Steuerung.setStatus(Status.NEUE_EMAIL);
               Steuerung.steuerung();
            } catch (NullPointerException var3) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_EINTRAG_WAEHLEN, "Warnung", 2);
            }

         }
      });
      buttonErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(VerdienstausfallAO.veranstaltung.getSelectedItem().toString().equals("<bitte wählen>")) {
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
            } else {
               Steuerung.setStatus(Status.VERDIENSTAUSFALL_ZEITENANPASSEN);
               Steuerung.steuerung();
            }

         }
      });
      this.buttonAnsehen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               String e = VerdienstausfallAO.aktuellerOrdner + VerdienstausfallAO.liste.getSelectedValue().toString();
               logging.logInfo("Öffne: " + e);
               Desktop.getDesktop().open(new File(e));
            } catch (IOException var3) {
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_OEFFNEN, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonAlleDrucken.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               File e = new File(VerdienstausfallAO.aktuellerOrdner);
               File[] files = e.listFiles();

               for(int i = 0; i < files.length; ++i) {
                  File ausdruckfile = new File(VerdienstausfallAO.aktuellerOrdner + files[i].getName());
                  logging.logInfo("Drucke: " + ausdruckfile);
                  Desktop dt = Desktop.getDesktop();
                  dt.print(ausdruckfile);
               }
            } catch (IOException var7) {
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_DRUCKEN, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var7);
            }

         }
      });
      this.buttonDrucken.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               File e = new File(VerdienstausfallAO.aktuellerOrdner + VerdienstausfallAO.liste.getSelectedValue().toString());
               logging.logInfo("Drucke: " + e);
               Desktop dt = Desktop.getDesktop();
               dt.print(e);
            } catch (IOException var4) {
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_BEIM_DRUCKEN, "Fehlermeldung", 0);
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
