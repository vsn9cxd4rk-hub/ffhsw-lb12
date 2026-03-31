package ao.mitglieder;

import ao.AbstractFenster;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.mitgliedakte.PDFMitgliedAusserDienst;
import pdfdocumente.mitgliedakte.PDFMitgliedInDienst;
import run.runApplication;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class MitgliedAusserDienstAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JComboBox mitgliederListe;
   private JLabel beschreibung;
   private JCheckBox ausserDienst;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panel;


   public MitgliedAusserDienstAO() {
      super("FeuerwehrManagementSystem - Mitglieder Außer Dienst stellen");
      logging.logInfo("Starte: MitgliedAusserDienstAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.ausserDienst = new JCheckBox("Ausser Dienst: ");
      this.modulBeschreibung = new JLabel("Mitglied Ausser Dienst stellen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      String[] liste = null;
      TabelleMitglied mitglied = new TabelleMitglied();

      try {
         liste = Utils.listToArrayOnlyFORComboBoxes(mitglied.getAllMitgliederFromDataBase());
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

      this.mitgliederListe = new JComboBox(liste);
      this.beschreibung = new JLabel("Name: ");
      this.mitgliederListe.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            TabelleMitglied tabMitglied = new TabelleMitglied();

            try {
               int e = tabMitglied.getIdByGuiString(MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
               if(tabMitglied.getAusserDienstStatus(e) == 0) {
                  MitgliedAusserDienstAO.this.ausserDienst.setSelected(false);
                  MitgliedAusserDienstAO.this.ausserDienst.setEnabled(true);
               } else if(tabMitglied.getAusserDienstStatus(e) == 1) {
                  MitgliedAusserDienstAO.this.ausserDienst.setSelected(true);
                  MitgliedAusserDienstAO.this.ausserDienst.setEnabled(true);
               }
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
            }

         }
      });
   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(500, 180);
      this.setTitle("FeuerwehrManagementSystem - Mitglieder Außer Dienst stellen");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panel = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.beschreibung);
      this.panel.add(this.mitgliederListe);
      this.add(this.ausserDienst);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
      this.ausserDienst.setEnabled(false);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleMitglied tabMitglied = new TabelleMitglied();

            try {
               int e = tabMitglied.getIdByGuiString(MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
               if(!MitgliedAusserDienstAO.this.ausserDienst.isSelected()) {
                  tabMitglied.updateAusserDienst(e, 0);
                  PDFMitgliedInDienst.PDFdocumentErstellen(runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + e + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_InDienstGestellt.pdf", MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
                  logging.logInfo("Mitglied: " + MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString() + " wurde Ausser Dienst gestellt");
               } else if(MitgliedAusserDienstAO.this.ausserDienst.isSelected()) {
                  tabMitglied.updateAusserDienst(e, 1);
                  PDFMitgliedAusserDienst.PDFdocumentErstellen(runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + e + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_AußerDienstGestellt.pdf", MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
                  logging.logInfo("Mitglied: " + MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString() + " wurde in Dienst gestellt");
               }

               logbuchEingabe.NeuerEintag("Mitglied Außer Dienst Status geändert zu: " + Integer.toString(MitgliedAusserDienstAO.this.ausserDienst.isSelected()?1:0) + " " + MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
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
