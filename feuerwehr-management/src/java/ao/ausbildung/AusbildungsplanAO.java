package ao.ausbildung;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import data.tabellen.TabelleAusbildung_Kategorie;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import go.Ausbildung_Plan;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities.joomla.Joomla;

public class AusbildungsplanAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonAktualisieren;
   private JButton buttonAusbildungKategorie;
   private JButton buttonDruckvorschau;
   private JButton buttonTauschen;
   private JButton buttonDatenübernahmeVorjahr;
   public static JComboBox[] ausbildungkategorien;
   public static JComboBox[] ausbilder1;
   public static JComboBox[] ausbilder2;
   public static JTextField[] details;
   private JLabel[] veranstaltungName;
   public static JComboBox jahre;
   private JComboBox mitgliederGruppe;
   private JLabel jahre_label;
   private JLabel beschriftung;
   private JLabel mitgliederGruppe_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panelGrund;
   private JScrollPane pane;


   public AusbildungsplanAO() {
      super("FeuerwehrManagementSystem - Ausbildungsplan");
      logging.logInfo("Starte: Ausbildungsplan");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonAktualisieren = new JButton("Aktualisieren");
      this.buttonAusbildungKategorie = new JButton("Neue Kategorie anlegen");
      this.buttonDruckvorschau = new JButton("Druckvorschau");
      this.buttonTauschen = new JButton("Ausbildungsinhalte Tauschen");
      this.buttonDatenübernahmeVorjahr = new JButton("Datenübernahme Vorjahr");
      this.beschriftung = new JLabel("Dienst / Veranstaltung                                          Ausbildungsinhalt                                          Details / Kommentar                                          Ausbilder 1                                          Ausbilder 2                             ");
      this.modulBeschreibung = new JLabel("Ausbildungsplan");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      String[] liste = null;
      String[] mitgliederGruppeListe = null;
      TabelleJahr tabJahr = new TabelleJahr();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

      try {
         liste = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerfügbarenJahre());
         mitgliederGruppeListe = Utils.listToArrayOnlyFORComboBoxes(tabGruppe.getAllGruppen());
         jahre = new JComboBox(liste);
         this.mitgliederGruppe = new JComboBox(mitgliederGruppeListe);
         this.jahre_label = new JLabel("Jahr: ");
         this.mitgliederGruppe_label = new JLabel("Mitgliedergruppe: ");
         jahre.addItem(Integer.toString(Integer.parseInt(SbcUtils.timeStamp("yyyy")) + 1));
         jahre.setSelectedItem(SbcUtils.timeStamp("yyyy"));
         this.mitgliederGruppe.setSelectedItem(runApplication.mitgliederGruppe);
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

   }

   protected void labelErstellen() {
      jahre.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            AusbildungsplanAO.this.panelGrund.removeAll();
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.dummy2);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonZurueck);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonTauschen);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonDatenübernahmeVorjahr);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonAktualisieren);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.pane);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonDruckvorschau);
            AusbildungsplanAO.this.repaint();
            AusbildungsplanAO.this.validate();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
            TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

            try {
               if(AusbildungsplanAO.jahre.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
               } else {
                  int e = tabGruppe.getID(runApplication.mitgliederGruppe);
                  int menge = tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(AusbildungsplanAO.jahre.getSelectedItem().toString(), 2, e);
                  String[] nameListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieByJahr(2, Integer.parseInt(AusbildungsplanAO.jahre.getSelectedItem().toString()), e));
                  AusbildungsplanAO.this.panelGrund = new JPanel(new GridLayout(menge, 2));
                  AusbildungsplanAO.this.pane = new JScrollPane(AusbildungsplanAO.this.panelGrund);
                  AusbildungsplanAO.this.pane.setVerticalScrollBarPolicy(22);
                  AusbildungsplanAO.this.pane.setPreferredSize(new Dimension(1200, 500));
                  AusbildungsplanAO.ausbildungkategorien = new JComboBox[menge];
                  AusbildungsplanAO.ausbilder1 = new JComboBox[menge];
                  AusbildungsplanAO.ausbilder2 = new JComboBox[menge];
                  AusbildungsplanAO.details = new JTextField[menge];
                  AusbildungsplanAO.this.veranstaltungName = new JLabel[menge];
                  String[] kategorieListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAllKategorien());
                  String[] ausbilderName = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());

                  for(int i = 0; i < menge; ++i) {
                     AusbildungsplanAO.this.veranstaltungName[i] = new JLabel(nameListe[i]);
                     AusbildungsplanAO.ausbildungkategorien[i] = new JComboBox(kategorieListe);
                     AusbildungsplanAO.ausbildungkategorien[i].setName(AusbildungsplanAO.this.veranstaltungName[i].toString());
                     AusbildungsplanAO.details[i] = new JTextField(20);
                     AusbildungsplanAO.ausbilder1[i] = new JComboBox(ausbilderName);
                     AusbildungsplanAO.ausbilder2[i] = new JComboBox(ausbilderName);
                     int vID = tabVeranstaltung.getVeranstaltungID(nameListe[i]);
                     if(tabPlan.getCountVeranstaltungID(vID) == 1) {
                        int kID = tabPlan.getAusbildungKategorie(vID);
                        int a1ID = tabPlan.getAusbilder1(vID);
                        int a2ID = tabPlan.getAusbilder2(vID);
                        if(kID != 0) {
                           AusbildungsplanAO.ausbildungkategorien[i].setSelectedItem(tabKategorie.getNameByID(kID));
                           AusbildungsplanAO.details[i].setText(tabPlan.getDeatils(vID));
                           String mName;
                           if(a1ID == 0) {
                              AusbildungsplanAO.ausbilder1[i].setSelectedItem("<bitte wählen>");
                           } else {
                              mName = tabMitglied.getNameVornameByID(a1ID);
                              AusbildungsplanAO.ausbilder1[i].setSelectedItem(mName);
                              if(AusbildungsplanAO.ausbilder1[i].getSelectedItem().equals("<bitte wählen>")) {
                                 AusbildungsplanAO.ausbilder1[i].addItem(mName);
                                 AusbildungsplanAO.ausbilder1[i].setSelectedItem(mName);
                              }
                           }

                           if(a2ID == 0) {
                              AusbildungsplanAO.ausbilder2[i].setSelectedItem("<bitte wählen>");
                           } else {
                              mName = tabMitglied.getNameVornameByID(a2ID);
                              AusbildungsplanAO.ausbilder2[i].setSelectedItem(mName);
                              if(AusbildungsplanAO.ausbilder2[i].getSelectedItem().equals("<bitte wählen>")) {
                                 AusbildungsplanAO.ausbilder2[i].addItem(mName);
                                 AusbildungsplanAO.ausbilder2[i].setSelectedItem(mName);
                              }
                           }
                        }
                     }

                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.this.veranstaltungName[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.ausbildungkategorien[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.details[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.ausbilder1[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.ausbilder2[i]);
                  }

                  AusbildungsplanAO.this.panelGrund.validate();
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.pane);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.dummy2);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonZurueck);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonAktualisieren);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonTauschen);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonDatenübernahmeVorjahr);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonDruckvorschau);
                  AusbildungsplanAO.this.repaint();
                  AusbildungsplanAO.this.validate();
                  if(Integer.parseInt(SbcUtils.timeStamp("yyyy")) <= Integer.parseInt(AusbildungsplanAO.jahre.getSelectedItem().toString())) {
                     AusbildungsplanAO.this.buttonDatenübernahmeVorjahr.setVisible(true);
                  } else {
                     AusbildungsplanAO.this.buttonDatenübernahmeVorjahr.setVisible(false);
                  }
               }
            } catch (SQLException var18) {
               logging.logPrintStackTrace(var18);
               MyEvent.setEvent("0x0030");
            }

         }
      });
      this.mitgliederGruppe.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            runApplication.mitgliederGruppe = AusbildungsplanAO.this.mitgliederGruppe.getSelectedItem().toString();
            AusbildungsplanAO.this.panelGrund.removeAll();
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.dummy2);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonZurueck);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonTauschen);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonDatenübernahmeVorjahr);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonAktualisieren);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.pane);
            AusbildungsplanAO.this.remove(AusbildungsplanAO.this.buttonDruckvorschau);
            AusbildungsplanAO.this.repaint();
            AusbildungsplanAO.this.validate();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
            TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

            try {
               if(AusbildungsplanAO.jahre.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
               } else {
                  int e = tabGruppe.getID(runApplication.mitgliederGruppe);
                  int menge = tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(AusbildungsplanAO.jahre.getSelectedItem().toString(), 2, e);
                  String[] nameListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieByJahr(2, Integer.parseInt(AusbildungsplanAO.jahre.getSelectedItem().toString()), e));
                  AusbildungsplanAO.this.panelGrund = new JPanel(new GridLayout(menge, 2));
                  AusbildungsplanAO.this.pane = new JScrollPane(AusbildungsplanAO.this.panelGrund);
                  AusbildungsplanAO.this.pane.setVerticalScrollBarPolicy(22);
                  AusbildungsplanAO.this.pane.setPreferredSize(new Dimension(1200, 500));
                  AusbildungsplanAO.ausbildungkategorien = new JComboBox[menge];
                  AusbildungsplanAO.ausbilder1 = new JComboBox[menge];
                  AusbildungsplanAO.ausbilder2 = new JComboBox[menge];
                  AusbildungsplanAO.details = new JTextField[menge];
                  AusbildungsplanAO.this.veranstaltungName = new JLabel[menge];
                  String[] kategorieListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAllKategorien());
                  String[] ausbilderName = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());

                  for(int i = 0; i < menge; ++i) {
                     AusbildungsplanAO.this.veranstaltungName[i] = new JLabel(nameListe[i]);
                     AusbildungsplanAO.ausbildungkategorien[i] = new JComboBox(kategorieListe);
                     AusbildungsplanAO.ausbildungkategorien[i].setName(AusbildungsplanAO.this.veranstaltungName[i].toString());
                     AusbildungsplanAO.details[i] = new JTextField(20);
                     AusbildungsplanAO.ausbilder1[i] = new JComboBox(ausbilderName);
                     AusbildungsplanAO.ausbilder2[i] = new JComboBox(ausbilderName);
                     int vID = tabVeranstaltung.getVeranstaltungID(nameListe[i]);
                     if(tabPlan.getCountVeranstaltungID(vID) == 1) {
                        int kID = tabPlan.getAusbildungKategorie(vID);
                        int a1ID = tabPlan.getAusbilder1(vID);
                        int a2ID = tabPlan.getAusbilder2(vID);
                        if(kID != 0) {
                           AusbildungsplanAO.ausbildungkategorien[i].setSelectedItem(tabKategorie.getNameByID(kID));
                           AusbildungsplanAO.details[i].setText(tabPlan.getDeatils(vID));
                           String mName;
                           if(a1ID == 0) {
                              AusbildungsplanAO.ausbilder1[i].setSelectedItem("<bitte wählen>");
                           } else {
                              mName = tabMitglied.getNameVornameByID(a1ID);
                              AusbildungsplanAO.ausbilder1[i].setSelectedItem(mName);
                              if(AusbildungsplanAO.ausbilder1[i].getSelectedItem().equals("<bitte wählen>")) {
                                 AusbildungsplanAO.ausbilder1[i].addItem(mName);
                                 AusbildungsplanAO.ausbilder1[i].setSelectedItem(mName);
                              }
                           }

                           if(a2ID == 0) {
                              AusbildungsplanAO.ausbilder2[i].setSelectedItem("<bitte wählen>");
                           } else {
                              mName = tabMitglied.getNameVornameByID(a2ID);
                              AusbildungsplanAO.ausbilder2[i].setSelectedItem(mName);
                              if(AusbildungsplanAO.ausbilder2[i].getSelectedItem().equals("<bitte wählen>")) {
                                 AusbildungsplanAO.ausbilder2[i].addItem(mName);
                                 AusbildungsplanAO.ausbilder2[i].setSelectedItem(mName);
                              }
                           }
                        }
                     }

                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.this.veranstaltungName[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.ausbildungkategorien[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.details[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.ausbilder1[i]);
                     AusbildungsplanAO.this.panelGrund.add(AusbildungsplanAO.ausbilder2[i]);
                  }

                  AusbildungsplanAO.this.panelGrund.validate();
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.pane);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.dummy2);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonZurueck);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonAktualisieren);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonTauschen);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonDatenübernahmeVorjahr);
                  AusbildungsplanAO.this.add(AusbildungsplanAO.this.buttonDruckvorschau);
                  AusbildungsplanAO.this.repaint();
                  AusbildungsplanAO.this.validate();
                  if(Integer.parseInt(SbcUtils.timeStamp("yyyy")) <= Integer.parseInt(AusbildungsplanAO.jahre.getSelectedItem().toString())) {
                     AusbildungsplanAO.this.buttonDatenübernahmeVorjahr.setVisible(true);
                  } else {
                     AusbildungsplanAO.this.buttonDatenübernahmeVorjahr.setVisible(false);
                  }
               }
            } catch (SQLException var18) {
               logging.logPrintStackTrace(var18);
               MyEvent.setEvent("0x0030");
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
      this.setSize(1280, 710);
      this.setTitle("FeuerwehrManagementSystem - Ausbildungsplan");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.mitgliederGruppe_label);
      this.add(this.mitgliederGruppe);
      this.add(this.jahre_label);
      this.add(jahre);
      this.add(this.buttonAusbildungKategorie);
      this.add(this.beschriftung);
      TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
      TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
      TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

      try {
         int e = tabGruppe.getID(runApplication.mitgliederGruppe);
         int menge = tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(jahre.getSelectedItem().toString(), 2, e);
         String[] nameListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieByJahr(2, Integer.parseInt(jahre.getSelectedItem().toString()), e));
         String[] ausbilderName = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
         this.panelGrund = new JPanel(new GridLayout(menge, 5));
         this.pane = new JScrollPane(this.panelGrund);
         this.pane.setVerticalScrollBarPolicy(22);
         this.pane.setPreferredSize(new Dimension(1250, 500));
         ausbildungkategorien = new JComboBox[menge];
         ausbilder1 = new JComboBox[menge];
         ausbilder2 = new JComboBox[menge];
         details = new JTextField[menge];
         this.veranstaltungName = new JLabel[menge];
         String[] kategorieListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAllKategorien());

         for(int i = 0; i < menge; ++i) {
            this.veranstaltungName[i] = new JLabel(nameListe[i]);
            ausbildungkategorien[i] = new JComboBox(kategorieListe);
            ausbildungkategorien[i].setName(this.veranstaltungName[i].toString());
            details[i] = new JTextField(20);
            ausbilder1[i] = new JComboBox(ausbilderName);
            ausbilder2[i] = new JComboBox(ausbilderName);
            int vID = tabVeranstaltung.getVeranstaltungID(nameListe[i]);
            if(tabPlan.getCountVeranstaltungID(vID) == 1) {
               int kID = tabPlan.getAusbildungKategorie(vID);
               int a1ID = tabPlan.getAusbilder1(vID);
               int a2ID = tabPlan.getAusbilder2(vID);
               if(kID != 0) {
                  ausbildungkategorien[i].setSelectedItem(tabKategorie.getNameByID(kID));
                  details[i].setText(tabPlan.getDeatils(vID));
                  if(a1ID == 0) {
                     ausbilder1[i].setSelectedItem("<bitte wählen>");
                  } else {
                     ausbilder1[i].setSelectedItem(tabMitglied.getNameVornameByID(a1ID));
                  }

                  if(a2ID == 0) {
                     ausbilder2[i].setSelectedItem("<bitte wählen>");
                  } else {
                     ausbilder2[i].setSelectedItem(tabMitglied.getNameVornameByID(a2ID));
                  }
               }
            }

            this.panelGrund.add(this.veranstaltungName[i]);
            this.panelGrund.add(ausbildungkategorien[i]);
            this.panelGrund.add(details[i]);
            this.panelGrund.add(ausbilder1[i]);
            this.panelGrund.add(ausbilder2[i]);
         }
      } catch (SQLException var16) {
         logging.logPrintStackTrace(var16);
      }

      this.add(this.pane);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonAktualisieren);
      this.add(this.buttonTauschen);
      this.add(this.buttonDatenübernahmeVorjahr);
      this.add(this.buttonDruckvorschau);
      this.buttonDatenübernahmeVorjahr.setVisible(false);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            AusbildungsplanAO.this.buttonZurueck.doClick();
         }
      });
      this.buttonZurueck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            runApplication.instanceofAusbildungsplanISRunning = 0;
            AusbildungsplanAO.this.dispose();
         }
      });
      this.buttonDatenübernahmeVorjahr.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleAusbildung_plan e = new TabelleAusbildung_plan();
               int jahr = Integer.parseInt(AusbildungsplanAO.jahre.getSelectedItem().toString()) - 1;
               int anzahl = e.getCountAusbildungenProJahr(jahr);
               int mGruppe = (new TabelleMitglieder_gruppe()).getID(runApplication.mitgliederGruppe);
               HashMap mapData = e.getDatenFürDatenübernahme(jahr, mGruppe);
               String[] data = new String[7];

               for(int i = 0; i < anzahl; ++i) {
                  try {
                     data = (String[])mapData.get(Integer.valueOf(i));
                     AusbildungsplanAO.ausbildungkategorien[i].setSelectedItem(data[3]);
                     AusbildungsplanAO.details[i].setText(data[4]);
                     AusbildungsplanAO.ausbilder1[i].setSelectedItem(data[5]);
                     AusbildungsplanAO.ausbilder2[i].setSelectedItem(data[6]);
                  } catch (ArrayIndexOutOfBoundsException var10) {
                     logging.logWarning("Es gibt keine Veranstaltungen mehr um Daten aus dem Vorjahr zu importieren");
                     break;
                  }
               }

               JOptionPane.showMessageDialog((Component)null, Konstante.AUSBILDUNGSPLAN_IMPORT_ERFOLGREICH);
            } catch (SQLException var11) {
               logging.logPrintStackTrace(var11);
            }

         }
      });
      this.buttonTauschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.AUSBILDUNGSINHALTE_TAUSCHEN);
            Steuerung.steuerung();
         }
      });
      this.buttonDruckvorschau.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.AUSBILDUNGPLAN_LISTE);
            Steuerung.steuerung();
         }
      });
      this.buttonAktualisieren.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.progressbar.setStringPainted(false);
            ProzessBarAO.progressbar.setIndeterminate(true);
            ProzessBarAO.label_bitteWarten.setText("Inhalte werden gespeichert... Bitte warten...");
            Thread threadSpeichern = new Thread() {
               public void run() {
                  TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                  TabelleAusbildung_Kategorie tabKategorie = new TabelleAusbildung_Kategorie();
                  TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
                  Ausbildung_Plan plan = new Ausbildung_Plan();
                  TabelleMitglied tabMitglied = new TabelleMitglied();

                  try {
                     HashMap e = null;
                     int mGruppe = (new TabelleMitglieder_gruppe()).getID(runApplication.mitgliederGruppe);
                     if(((String)runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden")).equals("1")) {
                        e = tabMitglied.getMitgliederListe();
                     }

                     for(int i = 0; i < AusbildungsplanAO.this.veranstaltungName.length; ++i) {
                        int vID = tabVeranstaltung.getVeranstaltungID(AusbildungsplanAO.this.veranstaltungName[i].getText());
                        plan.setId(tabPlan.getNextNummer());
                        plan.setJahr(Integer.parseInt(AusbildungsplanAO.jahre.getSelectedItem().toString()));
                        plan.setVeranstaltungID(vID);
                        plan.setAusbildungKategorie(tabKategorie.getID(AusbildungsplanAO.ausbildungkategorien[i].getSelectedItem().toString()));
                        plan.setDetails(AusbildungsplanAO.details[i].getText());
                        plan.setMitgliederGruppe(mGruppe);
                        if(AusbildungsplanAO.ausbilder1[i].getSelectedItem().toString().equals("<bitte wählen>")) {
                           plan.setAusbilder1(0);
                        } else {
                           plan.setAusbilder1(tabMitglied.getIdByGuiString(AusbildungsplanAO.ausbilder1[i].getSelectedItem().toString()));
                        }

                        if(AusbildungsplanAO.ausbilder2[i].getSelectedItem().toString().equals("<bitte wählen>")) {
                           plan.setAusbilder2(0);
                        } else {
                           plan.setAusbilder2(tabMitglied.getIdByGuiString(AusbildungsplanAO.ausbilder2[i].getSelectedItem().toString()));
                        }

                        if(tabPlan.getCountVeranstaltungID(vID) != 0) {
                           tabPlan.update(plan);
                        } else {
                           tabPlan.insert(plan);
                        }

                        if(((String)runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden")).equals("1")) {
                           Joomla.erstelleAusbildungsplan(plan, e);
                        }
                     }

                     MyEvent.setEvent("0x0030");
                     logging.logInfo("Ausbildungsplan wurde aktualisiert");
                     logbuchEingabe.NeuerEintag("Ausbildungsplan wurde aktualisiert/geändert für: " + AusbildungsplanAO.jahre.getSelectedItem().toString());
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  } catch (SQLException var10) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                     logging.logPrintStackTrace(var10);
                  }

               }
            };
            threadSpeichern.start();
         }
      });
      this.buttonAusbildungKategorie.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            MyEvent.setEvent("0x0027");
            Steuerung.setStatus(Status.AUSBILDUNG_KATEGORIE);
            Steuerung.steuerung();
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
      runApplication.instanceofAusbildungsplanISRunning = 1;
   }

   public void fensterSchlissen() {
      this.dispose();
   }
}
