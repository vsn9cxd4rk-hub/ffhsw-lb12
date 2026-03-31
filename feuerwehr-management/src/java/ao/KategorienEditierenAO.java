package ao;

import ao.AbstractFenster;
import data.tabellen.TabelleAusbildung_Kategorie;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleOrganisationen;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.einstellungen.TabelleBerechtigung;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeug_beschreibung;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import go.Ausbildung_Kategorie;
import go.Dienstgrad;
import go.Einsatz_kategorie;
import go.Fahrzeug_beschreibung;
import go.Mitglieder_gruppe;
import go.Organisation;
import go.Stichwort;
import go.Veranstaltung_Kategorie;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities.joomla.Joomla;

public class KategorienEditierenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JButton buttonAktualisieren;
   private JButton buttonNeu;
   private JButton buttonAbbruch;
   private JButton buttonLöschen;
   private JTextField name;
   private JLabel name_label;
   private JTextField beschreibungKurz;
   private JLabel beschreibungKurz_label;
   private JTextField id;
   private JLabel id_label;
   private JSlider sortierung;
   private JLabel sortierung_label;
   private JComboBox berechtigung;
   private JLabel berechtigung_label;
   private JComboBox einsatzKategorie;
   private JLabel einsatzkategorie_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JComboBox typ;
   private JLabel typ_label;
   public static JTree tree;
   private JScrollPane scrollPaneTree;
   private JPanel panel;


   public KategorienEditierenAO() {
      super("FeuerwehrManagementSystem - Kategorien");
      logging.logInfo("Starte: KategorienEditierenAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonAktualisieren = new JButton("Aktualisieren");
      this.buttonNeu = new JButton("Neu");
      this.buttonAbbruch = new JButton("Abbruch");
      this.buttonLöschen = new JButton("Löschen");
      this.buttonZurueck = new JButton("Zurück");
      this.modulBeschreibung = new JLabel("Kategorien Editieren / Anlegen / Löschen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.typ_label = new JLabel("Typ: ");
      this.einsatzkategorie_label = new JLabel("Einsatzkategorie: ");
      this.name = new JTextField(20);
      this.name_label = new JLabel("Name: ");
      this.id = new JTextField(20);
      this.id_label = new JLabel("ID: ");
      this.beschreibungKurz = new JTextField(20);
      this.beschreibungKurz_label = new JLabel("Dienstgrad Abkürzung: ");
      this.sortierung = new JSlider();
      this.sortierung_label = new JLabel("Sortierung: ");
      this.sortierung.setPaintTicks(true);
      this.sortierung.setMajorTickSpacing(10);
      this.berechtigung_label = new JLabel("Berechtigungszuweisung: ");
      tree = new JTree(CreateTrees.CreateTreeKategorienListe());
      tree.setSelectionRow(1);
      this.scrollPaneTree = new JScrollPane(tree);
      this.scrollPaneTree.setVerticalScrollBarPolicy(22);
      tree.setSelectionRow(0);
   }

   protected void labelErstellen() {
      String[] listeBerechtigung;
      try {
         TabelleEinsatz_kategorie e = new TabelleEinsatz_kategorie();
         this.typ = new JComboBox();
         this.typ.addItem("<bitte wählen>");
         if(BerechtigunsManager.ber[47] == 1) {
            this.typ.addItem("Ausbildungskategorie");
         }

         if(BerechtigunsManager.ber[0] == 1) {
            this.typ.addItem("Dienstgrad");
         }

         if(BerechtigunsManager.ber[38] == 1) {
            this.typ.addItem("Fahrzeugkategorie");
         }

         if(BerechtigunsManager.ber[46] == 1) {
            this.typ.addItem("Mitgliedergruppe");
         }

         if(((String)runApplication.EINSTELLUNGEN.get("WeitereOrganisationen")).equals("1") && BerechtigunsManager.ber[90] == 1) {
            this.typ.addItem("Organisationen");
         }

         if(BerechtigunsManager.ber[39] == 1) {
            this.typ.addItem("Stichwort");
         }

         if(BerechtigunsManager.ber[39] == 1) {
            this.typ.addItem("Stichwortkategorie");
         }

         if(BerechtigunsManager.ber[40] == 1) {
            this.typ.addItem("Veranstalungskategorie");
         }

         listeBerechtigung = Utils.listToArrayOnlyFORComboBoxes(e.getAllEinsatzKategorie());
         this.einsatzKategorie = new JComboBox(listeBerechtigung);
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

      this.typ.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Dienstgrad")) {
               KategorienEditierenAO.this.beschreibungKurz.setVisible(true);
               KategorienEditierenAO.this.beschreibungKurz_label.setVisible(true);
               KategorienEditierenAO.this.sortierung.setVisible(false);
               KategorienEditierenAO.this.sortierung_label.setVisible(false);
            } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Stichwort")) {
               KategorienEditierenAO.this.einsatzKategorie.setVisible(true);
               KategorienEditierenAO.this.einsatzkategorie_label.setVisible(true);
               KategorienEditierenAO.this.sortierung.setVisible(false);
               KategorienEditierenAO.this.sortierung_label.setVisible(false);
            } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Organisationen")) {
               KategorienEditierenAO.this.sortierung.setVisible(true);
               KategorienEditierenAO.this.sortierung_label.setVisible(true);
            } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Mitgliedergruppe")) {
               KategorienEditierenAO.this.berechtigung.setVisible(true);
               KategorienEditierenAO.this.berechtigung_label.setVisible(true);
            } else {
               KategorienEditierenAO.this.beschreibungKurz.setVisible(false);
               KategorienEditierenAO.this.beschreibungKurz_label.setVisible(false);
               KategorienEditierenAO.this.einsatzKategorie.setVisible(false);
               KategorienEditierenAO.this.einsatzkategorie_label.setVisible(false);
               KategorienEditierenAO.this.sortierung.setVisible(false);
               KategorienEditierenAO.this.sortierung_label.setVisible(false);
            }

         }
      });
      tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent arg0) {
            TabelleStichwort tabStichwort = new TabelleStichwort();
            TabelleAusbildung_Kategorie tabAusbildungKategorie = new TabelleAusbildung_Kategorie();
            TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
            TabelleFahrzeug_beschreibung tabeFahrzeugbeschreibung = new TabelleFahrzeug_beschreibung();
            TabelleMitglieder_gruppe tabMitgliederGruppe = new TabelleMitglieder_gruppe();
            TabelleVeranstaltung_Kategorie tabVeranstaltungskategorie = new TabelleVeranstaltung_Kategorie();
            TabelleEinsatz_kategorie tabEinsatzKategorie = new TabelleEinsatz_kategorie();
            TabelleOrganisationen tabOrganisation = new TabelleOrganisationen();
            TabelleEinsatz_kategorie tabKategorie = new TabelleEinsatz_kategorie();
            KategorienEditierenAO.this.buttonLöschen.setVisible(true);
            KategorienEditierenAO.this.buttonAktualisieren.setVisible(true);

            try {
               Object[] e = KategorienEditierenAO.tree.getSelectionPath().getPath();
               KategorienEditierenAO.this.typ.setSelectedItem(e[1].toString());
               String selectedTreeItem = KategorienEditierenAO.tree.getSelectionPath().getLastPathComponent().toString();
               KategorienEditierenAO.this.name.setEnabled(true);
               KategorienEditierenAO.this.name.setText(selectedTreeItem);
               if(e[1].toString().equals("Ausbildungskategorie")) {
                  KategorienEditierenAO.this.id.setText(Integer.toString(tabAusbildungKategorie.getID(selectedTreeItem)));
               } else if(e[1].toString().equals("Dienstgrad")) {
                  KategorienEditierenAO.this.id.setText(Integer.toString(tabDienstgrad.getDienstgradID(selectedTreeItem)));
                  KategorienEditierenAO.this.beschreibungKurz.setText(tabDienstgrad.getDienstgradBeschreibungKurz(Integer.parseInt(KategorienEditierenAO.this.id.getText())));
                  KategorienEditierenAO.this.beschreibungKurz.setVisible(true);
                  KategorienEditierenAO.this.beschreibungKurz_label.setVisible(true);
               } else if(e[1].toString().equals("Fahrzeugkategorie")) {
                  KategorienEditierenAO.this.id.setText(Integer.toString(tabeFahrzeugbeschreibung.getBeschreibungID(selectedTreeItem)));
               } else {
                  int oID;
                  if(e[1].toString().equals("Mitgliedergruppe")) {
                     KategorienEditierenAO.this.id.setText(Integer.toString(tabMitgliederGruppe.getID(selectedTreeItem)));
                     oID = tabMitgliederGruppe.getBerechtigungID(selectedTreeItem);
                     if(oID == -1) {
                        KategorienEditierenAO.this.berechtigung.setVisible(true);
                        KategorienEditierenAO.this.berechtigung_label.setVisible(true);
                     } else {
                        KategorienEditierenAO.this.berechtigung.setVisible(false);
                        KategorienEditierenAO.this.berechtigung_label.setVisible(false);
                     }
                  } else if(e[1].toString().equals("Stichwort")) {
                     KategorienEditierenAO.this.id.setText(Integer.toString(tabStichwort.getStichwortID(selectedTreeItem)));
                     KategorienEditierenAO.this.einsatzKategorie.setVisible(true);
                     KategorienEditierenAO.this.einsatzkategorie_label.setVisible(true);
                     KategorienEditierenAO.this.einsatzKategorie.setSelectedItem(tabEinsatzKategorie.getEinsatzKategorieName(tabStichwort.getStichwortKategorieID(selectedTreeItem)));
                  } else if(e[1].toString().equals("Stichwortkategorie")) {
                     KategorienEditierenAO.this.id.setText(Integer.toString(tabKategorie.getKategorieID(selectedTreeItem)));
                  } else if(e[1].toString().equals("Veranstalungskategorie")) {
                     KategorienEditierenAO.this.id.setText(Integer.toString(tabVeranstaltungskategorie.getID(selectedTreeItem)));
                  } else if(e[1].toString().equals("Organisationen")) {
                     if(((String)runApplication.EINSTELLUNGEN.get("Name")).equals(selectedTreeItem)) {
                        oID = 1;
                        KategorienEditierenAO.this.id.setText("1");
                        KategorienEditierenAO.this.name.setEnabled(false);
                     } else {
                        oID = tabOrganisation.getOrganisationID(selectedTreeItem);
                        KategorienEditierenAO.this.id.setText(Integer.toString(oID));
                     }

                     KategorienEditierenAO.this.sortierung.setValue(tabOrganisation.getSortierung(oID));
                     KategorienEditierenAO.this.sortierung.setVisible(true);
                     KategorienEditierenAO.this.sortierung_label.setVisible(true);
                  }
               }
            } catch (SQLException var14) {
               logging.logPrintStackTrace(var14);
            } catch (ArrayIndexOutOfBoundsException var15) {
               ;
            }

         }
      });

      try {
         TabelleBerechtigung e1 = new TabelleBerechtigung();
         listeBerechtigung = Utils.listToArrayOnlyFORComboBoxes(e1.getBerechtigungNameUndFrei(3));
         this.berechtigung = new JComboBox(listeBerechtigung);
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(880, 600);
      this.setTitle("FeuerwehrManagementSystem - Stichwort");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.scrollPaneTree.setPreferredSize(new Dimension(300, 450));
      this.add(this.scrollPaneTree);
      this.panel = new JPanel(new GridLayout(7, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.typ_label);
      this.panel.add(this.typ);
      this.panel.add(this.id_label);
      this.panel.add(this.id);
      this.panel.add(this.name_label);
      this.panel.add(this.name);
      this.panel.add(this.beschreibungKurz_label);
      this.panel.add(this.beschreibungKurz);
      this.panel.add(this.einsatzkategorie_label);
      this.panel.add(this.einsatzKategorie);
      this.panel.add(this.sortierung_label);
      this.panel.add(this.sortierung);
      this.panel.add(this.berechtigung_label);
      this.panel.add(this.berechtigung);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonNeu);
      this.add(this.buttonAbbruch);
      this.add(this.buttonLöschen);
      this.add(this.buttonSpeichern);
      this.add(this.buttonAktualisieren);
      this.buttonAktualisieren.setVisible(false);
      this.buttonAbbruch.setVisible(false);
      this.buttonSpeichern.setVisible(false);
      this.buttonLöschen.setVisible(false);
      this.typ.setEnabled(false);
      this.id.setEditable(false);
      this.beschreibungKurz.setVisible(false);
      this.beschreibungKurz_label.setVisible(false);
      this.einsatzKategorie.setVisible(false);
      this.einsatzkategorie_label.setVisible(false);
      this.sortierung.setVisible(false);
      this.sortierung_label.setVisible(false);
      this.berechtigung_label.setVisible(false);
      this.berechtigung.setVisible(false);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonNeu.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            KategorienEditierenAO.tree.setEnabled(false);
            KategorienEditierenAO.this.buttonLöschen.setEnabled(false);
            KategorienEditierenAO.this.buttonAbbruch.setVisible(true);
            KategorienEditierenAO.this.buttonSpeichern.setVisible(true);
            KategorienEditierenAO.this.buttonAktualisieren.setVisible(false);
            KategorienEditierenAO.this.buttonLöschen.setVisible(false);
            KategorienEditierenAO.this.typ.setEnabled(true);
            KategorienEditierenAO.this.id.setText("-");
            KategorienEditierenAO.this.name.setText((String)null);
            KategorienEditierenAO.this.name.setEnabled(true);
            KategorienEditierenAO.this.typ.setSelectedItem("<bitte wählen>");
            KategorienEditierenAO.this.einsatzKategorie.setSelectedItem("<bitte wählen>");
            KategorienEditierenAO.this.beschreibungKurz.setText((String)null);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.panel);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.dummy2);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.buttonZurueck);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.buttonNeu);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.buttonLöschen);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.buttonSpeichern);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.buttonAbbruch);
            KategorienEditierenAO.this.remove(KategorienEditierenAO.this.buttonAktualisieren);
            KategorienEditierenAO.this.panel = new JPanel(new GridLayout(7, 2));
            KategorienEditierenAO.this.getContentPane().add("Center", KategorienEditierenAO.this.panel);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.typ_label);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.typ);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.id_label);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.id);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.name_label);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.name);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.beschreibungKurz_label);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.beschreibungKurz);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.einsatzkategorie_label);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.einsatzKategorie);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.sortierung_label);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.sortierung);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.berechtigung_label);
            KategorienEditierenAO.this.panel.add(KategorienEditierenAO.this.berechtigung);
            KategorienEditierenAO.this.add(KategorienEditierenAO.this.dummy2);
            KategorienEditierenAO.this.add(KategorienEditierenAO.this.buttonZurueck);
            KategorienEditierenAO.this.add(KategorienEditierenAO.this.buttonNeu);
            KategorienEditierenAO.this.add(KategorienEditierenAO.this.buttonAbbruch);
            KategorienEditierenAO.this.add(KategorienEditierenAO.this.buttonLöschen);
            KategorienEditierenAO.this.add(KategorienEditierenAO.this.buttonSpeichern);
            KategorienEditierenAO.this.add(KategorienEditierenAO.this.buttonAktualisieren);
            KategorienEditierenAO.this.repaint();
            KategorienEditierenAO.this.validate();
         }
      });
      this.buttonAbbruch.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            KategorienEditierenAO.this.buttonAbbruch.setVisible(false);
            KategorienEditierenAO.tree.setEnabled(true);
         }
      });
      this.buttonLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleStichwort e = new TabelleStichwort();
               TabelleAusbildung_Kategorie tabAusbildungKategorie = new TabelleAusbildung_Kategorie();
               TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
               TabelleFahrzeug_beschreibung tabeFahrzeugbeschreibung = new TabelleFahrzeug_beschreibung();
               TabelleMitglieder_gruppe tabMitgliederGruppe = new TabelleMitglieder_gruppe();
               TabelleBerechtigung tabBerechtigung = new TabelleBerechtigung();
               TabelleVeranstaltung_Kategorie tabVeranstaltungskategorie = new TabelleVeranstaltung_Kategorie();
               TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
               TabelleEinsatz tabEinsatz = new TabelleEinsatz();
               TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
               TabelleMitglied tabMitglied = new TabelleMitglied();
               TabelleAusbildung_plan tabAusbildungsplan = new TabelleAusbildung_plan();
               TabelleOrganisationen tabOrganisationen = new TabelleOrganisationen();
               TabelleEinsatz_organisationen tabEinsatz_organisationen = new TabelleEinsatz_organisationen();
               TabelleEinsatz_kategorie tabKategorie = new TabelleEinsatz_kategorie();
               Object[] kategorieAuswahl = KategorienEditierenAO.tree.getSelectionPath().getPath();
               boolean errorLevel = false;
               if(kategorieAuswahl[1].toString().equals("Ausbildungskategorie")) {
                  if(tabAusbildungsplan.getCountAusbildungskategorie(Integer.parseInt(KategorienEditierenAO.this.id.getText())) == 0) {
                     tabAusbildungKategorie.delete(KategorienEditierenAO.this.name.getText());
                     logbuchEingabe.NeuerEintag("Dienstgrad gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  }
               } else if(kategorieAuswahl[1].toString().equals("Dienstgrad")) {
                  if(tabMitglied.getDienstgradCount(Integer.parseInt(KategorienEditierenAO.this.id.getText())) == 0) {
                     tabDienstgrad.delete(KategorienEditierenAO.this.name.getText());
                     logbuchEingabe.NeuerEintag("Dienstgrad gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  }
               } else if(kategorieAuswahl[1].toString().equals("Fahrzeugkategorie")) {
                  if(tabFahrzeug.getCountByFahrzeugBeschreibung(Integer.parseInt(KategorienEditierenAO.this.id.getText())) == 0) {
                     tabeFahrzeugbeschreibung.delete(KategorienEditierenAO.this.name.getText());
                     logbuchEingabe.NeuerEintag("FahrzeugBeschreibung gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  }
               } else if(kategorieAuswahl[1].toString().equals("Mitgliedergruppe")) {
                  if(tabMitglied.getMitglierderGruppenCount(Integer.parseInt(KategorienEditierenAO.this.id.getText())) == 0) {
                     int berechtigungID = tabMitgliederGruppe.getBerechtigungID(KategorienEditierenAO.this.name.getText());
                     tabMitgliederGruppe.delete(KategorienEditierenAO.this.name.getText());
                     tabBerechtigung.updateBerechtigungName(3, berechtigungID, "frei" + berechtigungID);
                     KategorienEditierenAO.this.berechtigung.addItem("frei" + berechtigungID);
                     logbuchEingabe.NeuerEintag("MitgliederGruppe gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  }
               } else if(kategorieAuswahl[1].toString().equals("Stichwort")) {
                  if(tabEinsatz.getStichwortCount(Integer.parseInt(KategorienEditierenAO.this.id.getText())) == 0) {
                     e.delete(KategorienEditierenAO.this.name.getText());
                     logbuchEingabe.NeuerEintag("Stichwort gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  }
               } else if(kategorieAuswahl[1].toString().equals("Stichwortkategorie")) {
                  if(e.getKategorieCount(Integer.parseInt(KategorienEditierenAO.this.id.getText())) == 0) {
                     tabKategorie.delete(KategorienEditierenAO.this.name.getText());
                     KategorienEditierenAO.this.einsatzKategorie.removeItem(KategorienEditierenAO.this.name.getText());
                     logbuchEingabe.NeuerEintag("Einsatzkategorie gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  }
               } else if(kategorieAuswahl[1].toString().equals("Veranstalungskategorie")) {
                  if(KategorienEditierenAO.this.id.getText().equals("1") | KategorienEditierenAO.this.id.getText().equals("2") | KategorienEditierenAO.this.id.getText().equals("3") | KategorienEditierenAO.this.id.getText().equals("4")) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG2, "Warnung", 2);
                     errorLevel = true;
                  } else if(tabVeranstaltung.getCountByVeranstaltungskategorie(Integer.parseInt(KategorienEditierenAO.this.id.getText())) != 0) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.DATENSATZ_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  } else {
                     tabVeranstaltungskategorie.delete(KategorienEditierenAO.this.name.getText());
                     logbuchEingabe.NeuerEintag("Vernastaltungskategorie gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  }
               } else if(kategorieAuswahl[1].toString().equals("Organisationen")) {
                  if(KategorienEditierenAO.this.id.getText().equals("1")) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.EIGENE_ORANISTAION_KANN_NICHT_GELÖSCHT_WERDEN, "Warnung", 2);
                     errorLevel = true;
                  } else if(tabEinsatz_organisationen.getCount(Integer.parseInt(KategorienEditierenAO.this.id.getText()), 1) != 0) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.ORGANISATION_IN_BENUTZUNG, "Warnung", 2);
                     errorLevel = true;
                  } else {
                     tabOrganisationen.delete(KategorienEditierenAO.this.name.getText());
                     logbuchEingabe.NeuerEintag("Organisation gelöscht: " + KategorienEditierenAO.this.id.getText() + " / " + KategorienEditierenAO.this.name.getText());
                     JOptionPane.showMessageDialog((Component)null, Konstante.LOESCHEN_ERFOLGREICH);
                  }
               }

               if(!errorLevel) {
                  KategorienEditierenAO.tree.setModel(CreateTrees.CreateTreeKategorienListe());
                  KategorienEditierenAO.tree.setSelectionRow(0);
                  KategorienEditierenAO.this.name.setText((String)null);
                  KategorienEditierenAO.this.id.setText((String)null);
                  KategorienEditierenAO.this.typ.setSelectedItem("<bitte wählen>");
                  KategorienEditierenAO.this.einsatzKategorie.setSelectedItem("<bitte wählen>");
                  KategorienEditierenAO.this.beschreibungKurz.setText((String)null);
                  KategorienEditierenAO.this.beschreibungKurz.setVisible(false);
                  KategorienEditierenAO.this.einsatzKategorie.setVisible(false);
                  KategorienEditierenAO.this.buttonAktualisieren.setVisible(false);
                  KategorienEditierenAO.this.buttonLöschen.setVisible(false);
                  KategorienEditierenAO.this.sortierung.setVisible(false);
                  KategorienEditierenAO.this.sortierung_label.setVisible(false);
               }
            } catch (SQLException var20) {
               logging.logPrintStackTrace(var20);
            }

         }
      });
      this.buttonAktualisieren.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleStichwort tabStichwort = new TabelleStichwort();
            TabelleAusbildung_Kategorie tabAusbildungKategorie = new TabelleAusbildung_Kategorie();
            TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
            TabelleFahrzeug_beschreibung tabeFahrzeugbeschreibung = new TabelleFahrzeug_beschreibung();
            TabelleMitglieder_gruppe tabMitgliederGruppe = new TabelleMitglieder_gruppe();
            TabelleBerechtigung tabBerechtigung = new TabelleBerechtigung();
            TabelleVeranstaltung_Kategorie tabVeranstaltungskategorie = new TabelleVeranstaltung_Kategorie();
            TabelleEinsatz_kategorie tabEinsatzKategorie = new TabelleEinsatz_kategorie();
            Stichwort stichwort = new Stichwort();
            TabelleStatistikEinsatz tabStatistikEinsatz = new TabelleStatistikEinsatz();
            Veranstaltung_Kategorie vKategorie = new Veranstaltung_Kategorie();
            Mitglieder_gruppe mGruppe = new Mitglieder_gruppe();
            Fahrzeug_beschreibung fBeschreibung = new Fahrzeug_beschreibung();
            Dienstgrad dienstgrad = new Dienstgrad();
            Ausbildung_Kategorie aKategorie = new Ausbildung_Kategorie();
            TabelleOrganisationen tabOrganisationen = new TabelleOrganisationen();
            Organisation organisation = new Organisation();
            TabelleEinsatz_kategorie tabKategorie = new TabelleEinsatz_kategorie();
            Einsatz_kategorie kategorie = new Einsatz_kategorie();
            boolean errorLevel = false;

            try {
               if(KategorienEditierenAO.this.name.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_NAME_EINGEBEN);
               } else {
                  if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Ausbildungskategorie")) {
                     aKategorie.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                     aKategorie.setName(KategorienEditierenAO.this.name.getText());
                     tabAusbildungKategorie.update(aKategorie);
                     logging.logInfo("Ausbildungskategorie erfogreich aktualisiert");
                     logbuchEingabe.NeuerEintag("Ausbildungskategorie wurde aktualisiert: " + KategorienEditierenAO.this.name.getText());
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Dienstgrad")) {
                     dienstgrad.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                     dienstgrad.setBeschreibung(KategorienEditierenAO.this.beschreibungKurz.getText());
                     dienstgrad.setBeschreibungLang(KategorienEditierenAO.this.name.getText());
                     tabDienstgrad.update(dienstgrad);
                     logging.logInfo("Dienstgrad erfogreich aktualisiert");
                     logbuchEingabe.NeuerEintag("Dienstgrad wurde aktualisiert: " + KategorienEditierenAO.this.name.getText());
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Fahrzeugkategorie")) {
                     fBeschreibung.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                     fBeschreibung.setName(KategorienEditierenAO.this.name.getText());
                     tabeFahrzeugbeschreibung.update(fBeschreibung);
                     logging.logInfo("Fahrzeugkategorie erfogreich aktualisiert");
                     logbuchEingabe.NeuerEintag("Fahrzeugkategorie wurde aktualisiert: " + KategorienEditierenAO.this.name.getText());
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Mitgliedergruppe")) {
                     mGruppe.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                     mGruppe.setName(KategorienEditierenAO.this.name.getText());
                     if(KategorienEditierenAO.this.berechtigung.getSelectedItem().toString().equals("<bitte wählen>")) {
                        mGruppe.setBerechtigung(-1);
                     } else {
                        mGruppe.setBerechtigung(tabBerechtigung.getID(3, KategorienEditierenAO.this.berechtigung.getSelectedItem().toString()));
                        tabBerechtigung.updateBerechtigungName(3, tabBerechtigung.getID(3, KategorienEditierenAO.this.berechtigung.getSelectedItem().toString()), "Mitgliedergruppe - " + KategorienEditierenAO.this.name.getText());
                        KategorienEditierenAO.this.berechtigung.removeItem(KategorienEditierenAO.this.berechtigung.getSelectedItem());
                        KategorienEditierenAO.this.berechtigung.setVisible(false);
                        KategorienEditierenAO.this.berechtigung_label.setVisible(false);
                     }

                     tabMitgliederGruppe.update(mGruppe);
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Stichwort")) {
                     if(KategorienEditierenAO.this.einsatzKategorie.getSelectedItem().toString().equals("<bitte wählen>")) {
                        logging.logInfo("Stichwort / Einsatzkategorie wurde nicht gewählt");
                        JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_KATEGORIE_WAEHLEN, "Warnung", 2);
                        errorLevel = true;
                     } else {
                        stichwort.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                        stichwort.setName(KategorienEditierenAO.this.name.getText());
                        stichwort.setKategorie(tabEinsatzKategorie.getKategorieID(KategorienEditierenAO.this.einsatzKategorie.getSelectedItem().toString()));
                        tabStichwort.update(stichwort);
                        tabStatistikEinsatz.updateKategorie(stichwort);
                        logging.logInfo("Stichwort erfogreich aktualisiert");
                        logbuchEingabe.NeuerEintag("Stichwort wurde aktualisiert: " + KategorienEditierenAO.this.name.getText());
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Stichwortkategorie")) {
                     KategorienEditierenAO.this.einsatzKategorie.removeItem(tabKategorie.getEinsatzKategorieName(Integer.parseInt(KategorienEditierenAO.this.id.getText())));
                     kategorie.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                     kategorie.setName(KategorienEditierenAO.this.name.getText());
                     tabKategorie.update(kategorie);
                     KategorienEditierenAO.this.einsatzKategorie.addItem(KategorienEditierenAO.this.name.getText());
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Veranstalungskategorie")) {
                     vKategorie.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                     vKategorie.setName(KategorienEditierenAO.this.name.getText());
                     tabVeranstaltungskategorie.update(vKategorie);
                     logging.logInfo("Veranstalungskategorie erfogreich aktualisiert");
                     logbuchEingabe.NeuerEintag("Veranstalungskategorie wurde aktualisiert: " + KategorienEditierenAO.this.name.getText());
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Organisationen")) {
                     if(tabOrganisationen.getCountOfSortierung(KategorienEditierenAO.this.sortierung.getValue()) != 0) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.SORTIEUNG_MUSS_EINDEUTIG_SEIN, "Warnung", 2);
                        logging.logInfo("Die Sortierung für die Organisation ist nicht eindeutig...");
                        errorLevel = true;
                     } else {
                        organisation.setId(Integer.parseInt(KategorienEditierenAO.this.id.getText()));
                        if(KategorienEditierenAO.this.id.getText().equals("1")) {
                           organisation.setName("");
                        } else {
                           organisation.setName(KategorienEditierenAO.this.name.getText());
                        }

                        organisation.setSortierung(KategorienEditierenAO.this.sortierung.getValue());
                        tabOrganisationen.update(organisation);
                        logging.logInfo("Organisation erfogreich aktualisiert");
                        logbuchEingabe.NeuerEintag("Organisation wurde aktualisiert: " + KategorienEditierenAO.this.name.getText());
                        if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente")).equals("1")) {
                           Joomla.erstelleOrganisation(organisation);
                        }
                     }
                  }

                  if(!errorLevel) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                     KategorienEditierenAO.tree.setModel(CreateTrees.CreateTreeKategorienListe());
                     KategorienEditierenAO.tree.expandRow(KategorienEditierenAO.this.typ.getSelectedIndex());
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                     logging.logInfo("Bedinerfehler gefunden...");
                  }
               }
            } catch (SQLException var23) {
               logging.logPrintStackTrace(var23);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            }

         }
      });
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleStichwort tabStichwort = new TabelleStichwort();
            TabelleAusbildung_Kategorie tabAusbildungKategorie = new TabelleAusbildung_Kategorie();
            TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
            TabelleFahrzeug_beschreibung tabeFahrzeugbeschreibung = new TabelleFahrzeug_beschreibung();
            TabelleMitglieder_gruppe tabMitgliederGruppe = new TabelleMitglieder_gruppe();
            TabelleBerechtigung tabBerechtigung = new TabelleBerechtigung();
            TabelleVeranstaltung_Kategorie tabVeranstaltungskategorie = new TabelleVeranstaltung_Kategorie();
            TabelleEinsatz_kategorie tabEinsatzKategorie = new TabelleEinsatz_kategorie();
            Stichwort stichwort = new Stichwort();
            Veranstaltung_Kategorie vKategorie = new Veranstaltung_Kategorie();
            Mitglieder_gruppe mGruppe = new Mitglieder_gruppe();
            Fahrzeug_beschreibung fBeschreibung = new Fahrzeug_beschreibung();
            Dienstgrad dienstgrad = new Dienstgrad();
            Ausbildung_Kategorie aKategorie = new Ausbildung_Kategorie();
            TabelleOrganisationen tabOrganisationen = new TabelleOrganisationen();
            Organisation organisation = new Organisation();
            TabelleEinsatz_kategorie tabKategorie = new TabelleEinsatz_kategorie();
            Einsatz_kategorie kategorie = new Einsatz_kategorie();
            int newKID = 0;
            boolean errorLevel = false;

            try {
               if(KategorienEditierenAO.this.name.getText().equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_NAME_EINGEBEN, "Warnung", 2);
               } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_KATEGORIE_WAEHLEN, "Warnung", 2);
               } else {
                  if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Ausbildungskategorie")) {
                     if(tabAusbildungKategorie.getCount(KategorienEditierenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.AUSBILDUNGKATEGORIE_SCHON_VORHANDEN, "Warnung", 2);
                        errorLevel = true;
                     } else {
                        newKID = tabAusbildungKategorie.getNextNummer();
                        aKategorie.setId(newKID);
                        aKategorie.setName(KategorienEditierenAO.this.name.getText());
                        tabAusbildungKategorie.insert(aKategorie);
                        if(((String)runApplication.EINSTELLUNGEN.get("JoomlaAusbildungsplanSenden")).equals("1")) {
                           Joomla.erstelleAusbildungKategorie(aKategorie);
                        }

                        logging.logInfo("Ausbildungskategorie angelegt: " + KategorienEditierenAO.this.name.getText());
                        logbuchEingabe.NeuerEintag("Ausbildungskategorie angelegt: " + KategorienEditierenAO.this.name.getText());
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Dienstgrad")) {
                     if(tabDienstgrad.getCountBeschreibung(KategorienEditierenAO.this.beschreibungKurz.getText()) == 0 && tabDienstgrad.getCountBeschreibungLang(KategorienEditierenAO.this.name.getText()) == 0) {
                        newKID = tabDienstgrad.getNextNummer();
                        dienstgrad.setId(newKID);
                        dienstgrad.setBeschreibung(KategorienEditierenAO.this.beschreibungKurz.getText());
                        dienstgrad.setBeschreibungLang(KategorienEditierenAO.this.name.getText());
                        tabDienstgrad.insert(dienstgrad);
                        logging.logInfo("Dienstgrad wurde angelegt: " + KategorienEditierenAO.this.name.getText());
                        logbuchEingabe.NeuerEintag("Dienstgrad wurde angelegt: " + KategorienEditierenAO.this.name.getText());
                     } else {
                        JOptionPane.showMessageDialog((Component)null, Konstante.DIENSTGRAD_SCHON_VORHANDEN, "Warnung", 2);
                        errorLevel = true;
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Fahrzeugkategorie")) {
                     if(tabeFahrzeugbeschreibung.getFahrzeugGruppenID(KategorienEditierenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.GRUPPE_SCHON_VORHANDEN, "Warnung", 2);
                        errorLevel = true;
                     } else {
                        newKID = tabeFahrzeugbeschreibung.getNextNummer();
                        fBeschreibung.setId(newKID);
                        fBeschreibung.setName(KategorienEditierenAO.this.name.getText());
                        tabeFahrzeugbeschreibung.insert(fBeschreibung);
                        logging.logInfo("Neue Fahrzeuggruppe wurde erfolgreich angelegt");
                        logbuchEingabe.NeuerEintag("Neue Fahrzeug Gruppe wurde angelegt: " + KategorienEditierenAO.this.name.getText());
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Mitgliedergruppe")) {
                     if(tabMitgliederGruppe.count(KategorienEditierenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.GRUPPE_SCHON_VORHANDEN, "Warnung", 2);
                        errorLevel = true;
                     } else {
                        newKID = tabMitgliederGruppe.getNextNummer();
                        int e = tabMitgliederGruppe.getNextPersonalnummer();
                        mGruppe.setId(newKID);
                        mGruppe.setName(KategorienEditierenAO.this.name.getText());
                        mGruppe.setPersonalnummer(e);
                        mGruppe.setNextPersonalnummer(e);
                        if(KategorienEditierenAO.this.berechtigung.getSelectedItem().toString().equals("<bitte wählen>")) {
                           mGruppe.setBerechtigung(-1);
                        } else {
                           mGruppe.setBerechtigung(tabBerechtigung.getID(3, KategorienEditierenAO.this.berechtigung.getSelectedItem().toString()));
                           tabBerechtigung.updateBerechtigungName(3, tabBerechtigung.getID(3, KategorienEditierenAO.this.berechtigung.getSelectedItem().toString()), "Mitgliedergruppe - " + KategorienEditierenAO.this.name.getText());
                           KategorienEditierenAO.this.berechtigung.removeItem(KategorienEditierenAO.this.berechtigung.getSelectedItem());
                           KategorienEditierenAO.this.berechtigung.setVisible(false);
                           KategorienEditierenAO.this.berechtigung_label.setVisible(false);
                        }

                        tabMitgliederGruppe.insert(mGruppe);
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Stichwort")) {
                     if(tabStichwort.getStichwortCount(KategorienEditierenAO.this.name.getText()) != 0) {
                        logging.logInfo("Stichwort existiert bereits");
                        JOptionPane.showMessageDialog((Component)null, Konstante.STICHWORT_EXISTIERT_BEREITS, "Warnung", 2);
                        errorLevel = true;
                     } else if(KategorienEditierenAO.this.einsatzKategorie.getSelectedItem().toString().equals("<bitte wählen>")) {
                        logging.logInfo("Stichwort / Einsatzkategorie wurde nicht gewählt");
                        JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_KATEGORIE_WAEHLEN, "Warnung", 2);
                        errorLevel = true;
                     } else {
                        newKID = tabStichwort.getNextNummer();
                        stichwort.setId(newKID);
                        stichwort.setName(KategorienEditierenAO.this.name.getText());
                        stichwort.setKategorie(tabEinsatzKategorie.getKategorieID(KategorienEditierenAO.this.einsatzKategorie.getSelectedItem().toString()));
                        tabStichwort.insert(stichwort);
                        logging.logInfo("Stichwort erfogreich gespeichert");
                        logbuchEingabe.NeuerEintag("Stichwort wurde angelegt: " + KategorienEditierenAO.this.name.getText());
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Stichwortkategorie")) {
                     if(tabKategorie.getKategorieCount(KategorienEditierenAO.this.name.getText()) != 0) {
                        logging.logInfo("Stichwortkategorie existiert bereits");
                        JOptionPane.showMessageDialog((Component)null, Konstante.STICHWORTKATEGORIE_EXISTIERT_BEREITS, "Warnung", 2);
                        errorLevel = true;
                     } else {
                        newKID = tabKategorie.getNextNummer();
                        kategorie.setId(newKID);
                        kategorie.setName(KategorienEditierenAO.this.name.getText());
                        tabKategorie.insert(kategorie);
                        KategorienEditierenAO.this.einsatzKategorie.addItem(KategorienEditierenAO.this.name.getText());
                        logging.logInfo("Stichwortkategorie erfogreich gespeichert");
                        logbuchEingabe.NeuerEintag("Stichwortkategorie wurde angelegt: " + KategorienEditierenAO.this.name.getText());
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Veranstalungskategorie")) {
                     if(tabVeranstaltungskategorie.getCount(KategorienEditierenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.VERANSTALTUNGKATEGORIE_SCHON_VORHANDEN, "Warnung", 2);
                        errorLevel = true;
                     } else {
                        newKID = tabVeranstaltungskategorie.getNextNummer();
                        vKategorie.setId(newKID);
                        vKategorie.setName(KategorienEditierenAO.this.name.getText());
                        tabVeranstaltungskategorie.insert(vKategorie);
                        if(((String)runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden")).equals("1")) {
                           Joomla.erstelleVeranstaltungKategorie(vKategorie);
                        }

                        logging.logInfo("Veranstaltungskategorie wurde angelegt: " + KategorienEditierenAO.this.name.getText());
                        logbuchEingabe.NeuerEintag("Veranstaltungskategorie wurde angelegt: " + KategorienEditierenAO.this.name.getText());
                     }
                  } else if(KategorienEditierenAO.this.typ.getSelectedItem().toString().equals("Organisationen")) {
                     if(tabOrganisationen.getOrganisationenCount(KategorienEditierenAO.this.name.getText()) != 0 | ((String)runApplication.EINSTELLUNGEN.get("Name")).equals(KategorienEditierenAO.this.name.getText())) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.ORGANISATION_BEREITS_VORHANDEN, "Warnung", 2);
                        errorLevel = true;
                     } else if(tabOrganisationen.getCountOfSortierung(KategorienEditierenAO.this.sortierung.getValue()) != 0) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.SORTIEUNG_MUSS_EINDEUTIG_SEIN, "Warnung", 2);
                        logging.logInfo("Die Sortierung für die Organisation ist nicht eindeutig...");
                        errorLevel = true;
                     } else {
                        newKID = tabOrganisationen.getNextNummer();
                        organisation.setId(newKID);
                        organisation.setName(KategorienEditierenAO.this.name.getText());
                        organisation.setSortierung(KategorienEditierenAO.this.sortierung.getValue());
                        tabOrganisationen.insert(organisation);
                        logging.logInfo("Organisation erfogreich gespeichert");
                        logbuchEingabe.NeuerEintag("Organisation wurde gespeichert: " + KategorienEditierenAO.this.name.getText());
                        if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponente")).equals("1")) {
                           Joomla.erstelleOrganisation(organisation);
                        }
                     }
                  }

                  if(!errorLevel) {
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                     KategorienEditierenAO.this.id.setText(Integer.toString(newKID));
                     KategorienEditierenAO.tree.setEnabled(true);
                     KategorienEditierenAO.tree.setModel(CreateTrees.CreateTreeKategorienListe());
                     KategorienEditierenAO.tree.expandRow(KategorienEditierenAO.this.typ.getSelectedIndex());
                     KategorienEditierenAO.this.buttonSpeichern.setVisible(false);
                     KategorienEditierenAO.this.buttonAktualisieren.setVisible(true);
                     KategorienEditierenAO.this.buttonLöschen.setVisible(true);
                     KategorienEditierenAO.this.buttonLöschen.setEnabled(true);
                     KategorienEditierenAO.this.buttonAbbruch.setVisible(false);
                     KategorienEditierenAO.this.buttonNeu.setEnabled(true);
                     KategorienEditierenAO.this.typ.setEnabled(false);
                  } else {
                     JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                     logging.logInfo("Bedienerfehler gefunden...");
                  }
               }
            } catch (SQLException var23) {
               logging.logPrintStackTrace(var23);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
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
