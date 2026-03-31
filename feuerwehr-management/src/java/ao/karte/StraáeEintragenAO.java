package ao.karte;

import ao.AbstractFenster;
import ao.karte.KarteAO;
import data.tabellen.karte.TabelleAnfahrt;
import data.tabellen.karte.TabelleHydranten;
import data.tabellen.karte.TabelleObjekte;
import data.tabellen.karte.TabelleStrassen;
import go.karte.Anfahrt;
import go.karte.Straße;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import listener.ImportListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities.components.MyDefaultTableRenderer;

public class StraßeEintragenAO extends AbstractFenster {

   private JButton buttonAktualisieren;
   private JButton buttonSpeichern;
   private JButton buttonZurueck;
   private JButton ordnerauswahl1;
   private JButton ordnerauswahl2;
   private JButton buttonNeu;
   private JButton buttonAbbruch;
   private JButton buttonLöschen;
   private JButton buttonBearbeiten;
   private JButton buttonImport;
   private JLabel straßenname2_label;
   private JTextField straßenName;
   private JLabel straßeninfo_label;
   private JTextArea straßeninfo;
   private JTextArea anfahrtInfo;
   private JLabel anfahrtInfo_label;
   private JLabel koordinaten_label;
   private JTextField koordinaten;
   private JTextField GPS_N;
   private JLabel GPS_N_label;
   private JTextField GPS_O;
   private JLabel GPS_O_label;
   private JLabel postleitzahl_label;
   private JTextField postleitzzahl;
   private JTextField datensatznummer;
   private JLabel datensatznummer_label;
   private JTextField bildStrasse;
   private JLabel bildStarsse_label;
   private JTextField bildStrasse2;
   private JLabel bildStarsse2_label;
   private JScrollPane anfahrtInfoPane;
   private JScrollPane straßenInfoPane;
   public static JTree tree;
   private JScrollPane scrollPaneTree;
   private String strassendb;
   private JTabbedPane tabPane;
   private JLabel hydranten_label;
   private JLabel objekte_label;
   public static JTable table;
   private DefaultTableModel defaultTableModelTable;
   private JScrollPane scrollpaneTable;
   public static JTable tableObjekte;
   private DefaultTableModel defaultTableModelTableObjekte;
   private JScrollPane scrollpaneTableObjekte;
   private JPanel panel;
   private JPanel panel2;
   private JPanel panel3;
   private JPanel panel4;
   private JPanel panelBackgroundStraßen;
   private JPanel panelBackgroundHydranten;
   private JPanel panelBackgroundObjekte;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JFileChooser chooser;
   public static int HYDRANTID;
   public static int OBJEKTID;
   public static int STRAßENID;


   public StraßeEintragenAO() {
      super("FeuerwehrManagementSystem Version: 4.08");
      logging.logInfo("Starte: StraßeEintragenAO");
   }

   protected void buttonErstellen() {
      this.modulBeschreibung = new JLabel("Einsatzgebiet Editieren");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.buttonAktualisieren = new JButton("Aktualisieren");
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.buttonNeu = new JButton("Neu");
      this.buttonAbbruch = new JButton("Abbruch");
      this.ordnerauswahl1 = new JButton("...");
      this.ordnerauswahl2 = new JButton("...");
      this.buttonLöschen = new JButton("Löschen");
      this.buttonBearbeiten = new JButton("Bearbeiten");
      this.buttonImport = new JButton("Import / Export");
      this.buttonImport.setToolTipText("Hier kann das Einsatzgebiet mit allen Daten in CSV importiert oder expotiert werden...");
      this.straßenname2_label = new JLabel("Straßen Name: ");
      this.straßenName = new JTextField(25);
      this.straßeninfo_label = new JLabel("Straßen Informationen: ");
      this.bildStarsse_label = new JLabel("Bild Straße Groß (Größe: 980x590 Pixel): ");
      this.bildStarsse2_label = new JLabel("Bild Straße Klein (Größe: 980x590 Pixel): ");
      this.straßeninfo = new JTextArea(4, 24);
      this.bildStrasse = new JTextField(23);
      this.bildStrasse2 = new JTextField(23);
      this.koordinaten_label = new JLabel("Stadtplan Koordinaten: ");
      this.koordinaten = new JTextField(25);
      this.GPS_N_label = new JLabel("GPS Position Nord: ");
      this.GPS_O_label = new JLabel("GPS Position Ost: ");
      this.GPS_N = new JTextField(25);
      this.GPS_O = new JTextField(25);
      this.postleitzahl_label = new JLabel("Postleitzahl und Ort: ");
      this.postleitzzahl = new JTextField(25);
      this.anfahrtInfo_label = new JLabel("Anfahrt Information: ");
      this.anfahrtInfo = new JTextArea(4, 24);
      this.datensatznummer_label = new JLabel("Datensatznummer:");
      this.datensatznummer = new JTextField(25);
      this.hydranten_label = new JLabel("KEINE STRAßE AUSGEWÄHLT!");
      this.objekte_label = new JLabel("KEINE STRAßE AUSGEWÄHLT!");
      this.anfahrtInfoPane = new JScrollPane(this.anfahrtInfo);
      this.anfahrtInfoPane.setVerticalScrollBarPolicy(22);
      this.straßenInfoPane = new JScrollPane(this.straßeninfo);
      this.straßenInfoPane.setVerticalScrollBarPolicy(22);
      this.defaultTableModelTable = new DefaultTableModel(10, 9);
      this.defaultTableModelTable.setColumnIdentifiers(TabelleHydranten.headnameHydranten);
      table = new JTable(this.defaultTableModelTable);
      table.setDefaultRenderer(Object.class, new MyDefaultTableRenderer());
      table.setRowHeight(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("zeilenhöheAnsicht")));
      this.scrollpaneTable = new JScrollPane(table);
      this.scrollpaneTable.setVerticalScrollBarPolicy(22);
      table.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            e.getClickCount();
         }
      });
      this.defaultTableModelTableObjekte = new DefaultTableModel(10, 9);
      this.defaultTableModelTableObjekte.setColumnIdentifiers(TabelleObjekte.headnameObjekte);
      tableObjekte = new JTable(this.defaultTableModelTableObjekte);
      tableObjekte.setDefaultRenderer(Object.class, new MyDefaultTableRenderer());
      tableObjekte.setRowHeight(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("zeilenhöheAnsicht")));
      this.scrollpaneTableObjekte = new JScrollPane(tableObjekte);
      this.scrollpaneTableObjekte.setVerticalScrollBarPolicy(22);
      tableObjekte.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            e.getClickCount();
         }
      });
      this.chooser = new JFileChooser();
      tree = new JTree(CreateTrees.CreateTreeStraßenHydranten());
      tree.setSelectionRow(1);
      this.scrollPaneTree = new JScrollPane(tree);
      this.scrollPaneTree.setVerticalScrollBarPolicy(22);
      tree.setSelectionRow(0);
      this.tabPane = new JTabbedPane();
   }

   protected void setzeAuswahllisten() {
      this.tabPane.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            JTabbedPane sourceTabbedPane = (JTabbedPane)e.getSource();
            int index = sourceTabbedPane.getSelectedIndex();
            logging.logInfo("Tab changed to: " + sourceTabbedPane.getTitleAt(index));
            if(index == 0) {
               StraßeEintragenAO.this.buttonAktualisieren.setVisible(true);
               StraßeEintragenAO.this.buttonBearbeiten.setVisible(false);
            } else if(index == 1 | index == 2) {
               StraßeEintragenAO.this.buttonAktualisieren.setVisible(false);
               StraßeEintragenAO.this.buttonBearbeiten.setVisible(true);
            }

         }
      });
   }

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Einsatzgebiet Editieren");
      this.setSize(1050, 610);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {}

   protected void boxenHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.scrollPaneTree.setPreferredSize(new Dimension(330, 450));
      this.add(this.scrollPaneTree);
      this.panelBackgroundStraßen = new JPanel();
      this.panelBackgroundHydranten = new JPanel();
      this.panelBackgroundObjekte = new JPanel();
      this.panel = new JPanel(new GridLayout(6, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.straßenname2_label);
      this.panel.add(this.straßenName);
      this.panel.add(this.datensatznummer_label);
      this.panel.add(this.datensatznummer);
      this.panel.add(this.koordinaten_label);
      this.panel.add(this.koordinaten);
      this.panel.add(this.GPS_N_label);
      this.panel.add(this.GPS_N);
      this.panel.add(this.GPS_O_label);
      this.panel.add(this.GPS_O);
      this.panel.add(this.postleitzahl_label);
      this.panel.add(this.postleitzzahl);
      this.panelBackgroundStraßen.add(this.panel);
      this.panel2 = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panel2);
      this.panel2.add(this.bildStarsse_label);
      this.panel2.add(this.bildStrasse);
      this.add(this.ordnerauswahl1);
      this.panelBackgroundStraßen.add(this.panel2);
      this.panelBackgroundStraßen.add(this.ordnerauswahl1);
      this.panel3 = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panel3);
      this.panel3.add(this.bildStarsse2_label);
      this.panel3.add(this.bildStrasse2);
      this.add(this.ordnerauswahl2);
      this.panelBackgroundStraßen.add(this.panel3);
      this.panelBackgroundStraßen.add(this.ordnerauswahl2);
      this.panel4 = new JPanel(new GridLayout(2, 2));
      this.getContentPane().add("Center", this.panel4);
      this.panel4.add(this.straßeninfo_label);
      this.panel4.add(this.straßenInfoPane);
      this.panel4.add(this.anfahrtInfo_label);
      this.panel4.add(this.anfahrtInfoPane);
      this.panelBackgroundStraßen.add(this.panel4);
      this.panelBackgroundHydranten.add(this.hydranten_label);
      this.scrollpaneTable.setPreferredSize(new Dimension(600, 400));
      this.panelBackgroundHydranten.add(this.scrollpaneTable);
      this.panelBackgroundObjekte.add(this.objekte_label);
      this.scrollpaneTableObjekte.setPreferredSize(new Dimension(600, 400));
      this.panelBackgroundObjekte.add(this.scrollpaneTableObjekte);
      this.tabPane.addTab("Straßen", this.panelBackgroundStraßen);
      this.tabPane.addTab("Hydranten", this.panelBackgroundHydranten);
      this.tabPane.addTab("Objekte", this.panelBackgroundObjekte);
      this.tabPane.setPreferredSize(new Dimension(650, 450));
      this.add(this.tabPane);
      this.add(this.buttonNeu);
      this.add(this.buttonAbbruch);
      this.add(this.buttonBearbeiten);
      this.add(this.buttonAktualisieren);
      this.add(this.buttonSpeichern);
      this.add(this.buttonLöschen);
      this.add(this.buttonImport);
      this.add(this.buttonZurueck);
      this.datensatznummer.setEditable(false);
      this.buttonAktualisieren.setVisible(false);
      this.buttonSpeichern.setVisible(false);
      this.buttonAbbruch.setVisible(false);
      this.buttonLöschen.setVisible(false);
      this.buttonBearbeiten.setVisible(false);
   }

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonImport.addActionListener(new ImportListener(this));
      this.buttonNeu.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 0) {
               StraßeEintragenAO.this.straßenName.setVisible(true);
               StraßeEintragenAO.this.straßenname2_label.setVisible(true);
               StraßeEintragenAO.this.datensatznummer.setText("0");
               StraßeEintragenAO.tree.setEnabled(false);
               StraßeEintragenAO.this.straßenName.setText((String)null);
               StraßeEintragenAO.this.anfahrtInfo.setText((String)null);
               StraßeEintragenAO.this.straßeninfo.setText((String)null);
               StraßeEintragenAO.this.koordinaten.setText((String)null);
               StraßeEintragenAO.this.GPS_N.setText((String)null);
               StraßeEintragenAO.this.GPS_O.setText((String)null);
               StraßeEintragenAO.this.postleitzzahl.setText((String)null);
               StraßeEintragenAO.this.bildStrasse.setText((String)null);
               StraßeEintragenAO.this.bildStrasse2.setText((String)null);
               StraßeEintragenAO.this.tabPane.setEnabledAt(1, false);
               StraßeEintragenAO.this.buttonAktualisieren.setVisible(false);
               StraßeEintragenAO.this.buttonSpeichern.setVisible(true);
               StraßeEintragenAO.this.buttonAbbruch.setVisible(true);
            } else if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 1) {
               Steuerung.setStatus(Status.HYDRANT_EINTARGEN);
               Steuerung.steuerung();
            } else if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 2) {
               Steuerung.setStatus(Status.OBJEKT_EINTARGEN);
               Steuerung.steuerung();
            }

         }
      });
      this.buttonBearbeiten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            TabelleStrassen tabStraßen = new TabelleStrassen();

            try {
               int e1;
               int[] listIDs;
               int row;
               if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 1) {
                  e1 = tabStraßen.getStrassenID(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString());
                  listIDs = Utils.listToIntArray((new TabelleHydranten()).getHydrantenIDForTable(e1));
                  row = StraßeEintragenAO.table.getSelectedRow();
                  StraßeEintragenAO.HYDRANTID = listIDs[row];
                  MyEvent.setEvent("0x0102");
                  Steuerung.setStatus(Status.HYDRANT_EINTARGEN);
                  Steuerung.steuerung();
               } else if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 2) {
                  e1 = tabStraßen.getStrassenID(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString());
                  listIDs = Utils.listToIntArray((new TabelleObjekte()).getObjektIDForTable(e1));
                  row = StraßeEintragenAO.tableObjekte.getSelectedRow();
                  StraßeEintragenAO.OBJEKTID = listIDs[row];
                  MyEvent.setEvent("0x0103");
                  Steuerung.setStatus(Status.OBJEKT_EINTARGEN);
                  Steuerung.steuerung();
               }
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      this.buttonLöschen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            TabelleStrassen tabStraßen = new TabelleStrassen();
            TabelleHydranten tabHydranten = new TabelleHydranten();
            TabelleObjekte tabObjekte = new TabelleObjekte();
            int msg = JOptionPane.showConfirmDialog((Component)null, Konstante.STRASSE_WIRKLICH_LÖSCHEN, "Frage", 0);
            if(msg == 0) {
               try {
                  int e1 = tabStraßen.getStrassenID(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString());
                  if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 0) {
                     tabStraßen.deleteOne(e1);
                     tabHydranten.deleteAll(e1);
                     StraßeEintragenAO.tree.setModel(CreateTrees.CreateTreeStraßenHydranten());
                     StraßeEintragenAO.this.buttonAktualisieren.setVisible(false);
                     StraßeEintragenAO.this.buttonLöschen.setVisible(false);
                     StraßeEintragenAO.this.straßenName.setText((String)null);
                     StraßeEintragenAO.this.anfahrtInfo.setText((String)null);
                     StraßeEintragenAO.this.straßeninfo.setText((String)null);
                     StraßeEintragenAO.this.koordinaten.setText((String)null);
                     StraßeEintragenAO.this.GPS_N.setText((String)null);
                     StraßeEintragenAO.this.GPS_O.setText((String)null);
                     StraßeEintragenAO.this.postleitzzahl.setText((String)null);
                     StraßeEintragenAO.this.bildStrasse.setText((String)null);
                     StraßeEintragenAO.this.bildStrasse2.setText((String)null);
                     StraßeEintragenAO.this.datensatznummer.setText("-");
                     StraßeEintragenAO.tree.setSelectionRow(0);
                  } else {
                     int[] listIDs;
                     int row;
                     if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 1) {
                        listIDs = Utils.listToIntArray((new TabelleHydranten()).getHydrantenIDForTable(e1));
                        row = StraßeEintragenAO.table.getSelectedRow();
                        if(row == -1) {
                           JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_EINTRAG_ZUM_Loeschen_WAEHLEN, "Warnung", 2);
                        } else {
                           tabHydranten.deleteOne(e1, listIDs[row]);
                           ((DefaultTableModel)StraßeEintragenAO.table.getModel()).setDataVector((new TabelleHydranten()).getHydrantenForTable(e1), TabelleHydranten.headnameHydranten);
                           logbuchEingabe.NeuerEintag("Hydrant mit der ID " + listIDs[row] + " wurde gelöscht");
                           logging.logInfo("Hydrant mit der ID " + listIDs[row] + " wurde gelöscht");
                        }
                     } else if(StraßeEintragenAO.this.tabPane.getSelectedIndex() == 2) {
                        listIDs = Utils.listToIntArray((new TabelleObjekte()).getObjektIDForTable(e1));
                        row = StraßeEintragenAO.tableObjekte.getSelectedRow();
                        if(row == -1) {
                           JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_EINTRAG_ZUM_Loeschen_WAEHLEN, "Warnung", 2);
                        } else {
                           tabObjekte.deleteOne(e1, listIDs[row]);
                           ((DefaultTableModel)StraßeEintragenAO.tableObjekte.getModel()).setDataVector((new TabelleObjekte()).getObjekteForTable(e1), TabelleObjekte.headnameObjekte);
                           logbuchEingabe.NeuerEintag("Objekt mit der ID " + listIDs[row] + " wurde gelöscht");
                           logging.logInfo("Objekt mit der ID " + listIDs[row] + " wurde gelöscht");
                        }
                     }
                  }
               } catch (SQLException var9) {
                  logging.logPrintStackTrace(var9);
               }
            }

         }
      });
      this.buttonAbbruch.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            StraßeEintragenAO.this.buttonAbbruch.setVisible(false);
            StraßeEintragenAO.this.buttonSpeichern.setVisible(false);
            StraßeEintragenAO.this.straßenName.setText((String)null);
            StraßeEintragenAO.this.anfahrtInfo.setText((String)null);
            StraßeEintragenAO.this.straßeninfo.setText((String)null);
            StraßeEintragenAO.this.koordinaten.setText((String)null);
            StraßeEintragenAO.this.GPS_N.setText((String)null);
            StraßeEintragenAO.this.GPS_O.setText((String)null);
            StraßeEintragenAO.this.postleitzzahl.setText((String)null);
            StraßeEintragenAO.this.bildStrasse.setText((String)null);
            StraßeEintragenAO.this.bildStrasse2.setText((String)null);
            StraßeEintragenAO.this.tabPane.setEnabledAt(1, true);
            StraßeEintragenAO.this.datensatznummer.setText("-");
            StraßeEintragenAO.tree.setEnabled(true);
            StraßeEintragenAO.tree.setSelectionRow(0);
         }
      });
      this.ordnerauswahl1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int returnVal = StraßeEintragenAO.this.chooser.showOpenDialog(StraßeEintragenAO.this.chooser);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + StraßeEintragenAO.this.chooser.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/KarteBilder/groß/" + StraßeEintragenAO.this.chooser.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(StraßeEintragenAO.this.chooser.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/KarteBilder/groß");
            StraßeEintragenAO.this.bildStrasse.setText(runApplication.arbeitsverzeichnis + "data/KarteBilder/groß/" + StraßeEintragenAO.this.chooser.getSelectedFile().getName());
         }
      });
      this.ordnerauswahl2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            int returnVal = StraßeEintragenAO.this.chooser.showOpenDialog(StraßeEintragenAO.this.chooser);
            if(returnVal == 0) {
               logging.logInfo("Ausgewählte Datei: " + StraßeEintragenAO.this.chooser.getSelectedFile().getPath());
            }

            String dateiname = runApplication.arbeitsverzeichnis + "data/KarteBilder/klein/" + StraßeEintragenAO.this.chooser.getSelectedFile().getName();
            Utils.kopiereDateiInDataOrdner(StraßeEintragenAO.this.chooser.getSelectedFile().getAbsoluteFile(), dateiname, runApplication.arbeitsverzeichnis + "data/KarteBilder/klein");
            StraßeEintragenAO.this.bildStrasse2.setText(runApplication.arbeitsverzeichnis + "data/KarteBilder/klein/" + StraßeEintragenAO.this.chooser.getSelectedFile().getName());
         }
      });
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleStrassen tabelleStrassen = new TabelleStrassen();
            TabelleAnfahrt tabAnfahrt = new TabelleAnfahrt();
            Straße straße = new Straße();
            Anfahrt anfahrt = new Anfahrt();

            try {
               if(tabelleStrassen.getStrassenCount(StraßeEintragenAO.this.straßenName.getText()).intValue() != 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.STRAßE_BEREITS_VORHANDEN, "Warnung", 2);
               } else {
                  int e1 = tabelleStrassen.getNextNummer();
                  straße.setId(e1);
                  straße.setName(StraßeEintragenAO.this.straßenName.getText());
                  straße.setInfo(StraßeEintragenAO.this.straßeninfo.getText());
                  straße.setKoordinaten(StraßeEintragenAO.this.koordinaten.getText());
                  straße.setGPS_N(StraßeEintragenAO.this.GPS_N.getText());
                  straße.setGPS_O(StraßeEintragenAO.this.GPS_O.getText());
                  straße.setPLZ(StraßeEintragenAO.this.postleitzzahl.getText());
                  straße.setBild(Utils.removeBackSlashFromString(StraßeEintragenAO.this.bildStrasse.getText()));
                  straße.setBild2(Utils.removeBackSlashFromString(StraßeEintragenAO.this.bildStrasse2.getText()));
                  anfahrt.setAnfahrt(StraßeEintragenAO.this.anfahrtInfo.getText());
                  anfahrt.setObjektID(0);
                  anfahrt.setStrassenID(e1);
                  anfahrt.setId(tabAnfahrt.getNextNummer());
                  tabelleStrassen.insert(straße);
                  if(!StraßeEintragenAO.this.anfahrtInfo.getText().equals("")) {
                     tabAnfahrt.insert(anfahrt);
                  }

                  logbuchEingabe.NeuerEintag("Straße wurde angelegt: " + StraßeEintragenAO.this.straßenName.getText());
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  KarteAO.StrasseSuchen.addItem(StraßeEintragenAO.this.straßenName.getText());
                  StraßeEintragenAO.this.datensatznummer.setText(Integer.toString(tabelleStrassen.getNextNummer()));
                  StraßeEintragenAO.tree.setEnabled(true);
                  StraßeEintragenAO.this.buttonAktualisieren.setVisible(true);
                  StraßeEintragenAO.this.buttonSpeichern.setVisible(false);
                  StraßeEintragenAO.this.buttonAbbruch.setVisible(false);
                  StraßeEintragenAO.this.tabPane.setEnabledAt(1, true);
                  StraßeEintragenAO.tree.setModel(CreateTrees.CreateTreeStraßenHydranten());
                  StraßeEintragenAO.tree.expandRow(1);
               }
            } catch (SQLException var7) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var7);
            }

         }
      });
      this.buttonAktualisieren.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            TabelleStrassen tabelleStrassen = new TabelleStrassen();
            TabelleAnfahrt tabAnfahrt = new TabelleAnfahrt();
            Straße straße = new Straße();
            Anfahrt anfahrt = new Anfahrt();

            try {
               int e1 = Integer.parseInt(StraßeEintragenAO.this.datensatznummer.getText());
               straße.setId(e1);
               straße.setName(StraßeEintragenAO.this.straßenName.getText());
               straße.setInfo(StraßeEintragenAO.this.straßeninfo.getText());
               straße.setKoordinaten(StraßeEintragenAO.this.koordinaten.getText());
               straße.setGPS_N(StraßeEintragenAO.this.GPS_N.getText());
               straße.setGPS_O(StraßeEintragenAO.this.GPS_O.getText());
               straße.setPLZ(StraßeEintragenAO.this.postleitzzahl.getText());
               straße.setBild(StraßeEintragenAO.this.bildStrasse.getText());
               straße.setBild2(StraßeEintragenAO.this.bildStrasse2.getText());
               anfahrt.setAnfahrt(StraßeEintragenAO.this.anfahrtInfo.getText());
               anfahrt.setObjektID(0);
               anfahrt.setStrassenID(e1);
               anfahrt.setId(tabAnfahrt.getNextNummer());
               tabelleStrassen.update(straße);
               tabAnfahrt.deleteStraßenAnfahrt(anfahrt);
               if(!StraßeEintragenAO.this.anfahrtInfo.getText().equals("")) {
                  tabAnfahrt.insert(anfahrt);
               }

               StraßeEintragenAO.tree.setModel(CreateTrees.CreateTreeStraßenHydranten());
               StraßeEintragenAO.tree.expandRow(1);
               logbuchEingabe.NeuerEintag("Straße wurde aktualisiert: StraßenID " + StraßeEintragenAO.this.datensatznummer.getText());
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
            } catch (SQLException var7) {
               JOptionPane.showMessageDialog((Component)null, "Beim Speichern ist ein Fehler aufgetreten!", "Fehlermeldung", 0);
               logging.logPrintStackTrace(var7);
            }

         }
      });
      tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent arg0) {
            try {
               TabelleStrassen e1 = new TabelleStrassen();
               TabelleAnfahrt tabAnfahrt = new TabelleAnfahrt();
               int sID = e1.getStrassenNumber(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()).intValue();
               StraßeEintragenAO.STRAßENID = sID;
               StraßeEintragenAO.this.straßenName.setText(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString());
               StraßeEintragenAO.this.datensatznummer.setText(Integer.toString(sID));
               StraßeEintragenAO.this.anfahrtInfo.setText(tabAnfahrt.getAnfahrtStraße(sID));
               StraßeEintragenAO.this.straßeninfo.setText(e1.getStraßenInfo(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()));
               StraßeEintragenAO.this.koordinaten.setText(e1.getStrassenKoordinaten(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()));
               StraßeEintragenAO.this.GPS_N.setText(e1.getStrassenGPS_N(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()));
               StraßeEintragenAO.this.GPS_O.setText(e1.getStrassenGPS_O(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()));
               StraßeEintragenAO.this.postleitzzahl.setText(e1.getPLZ(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()));
               StraßeEintragenAO.this.bildStrasse.setText(e1.getStrassenBild(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()));
               StraßeEintragenAO.this.bildStrasse2.setText(e1.getStrassenBild2(StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString()));
               StraßeEintragenAO.this.buttonAktualisieren.setVisible(true);
               StraßeEintragenAO.this.buttonLöschen.setVisible(true);
               StraßeEintragenAO.this.tabPane.setSelectedIndex(0);
               StraßeEintragenAO.this.hydranten_label.setText("Wasserentnahmestellen-Liste: " + StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString());
               ((DefaultTableModel)StraßeEintragenAO.table.getModel()).setDataVector((new TabelleHydranten()).getHydrantenForTable(sID), TabelleHydranten.headnameHydranten);
               StraßeEintragenAO.this.objekte_label.setText("Objekt-Liste: " + StraßeEintragenAO.tree.getSelectionPath().getLastPathComponent().toString());
               ((DefaultTableModel)StraßeEintragenAO.tableObjekte.getModel()).setDataVector((new TabelleObjekte()).getObjekteForTable(sID), TabelleObjekte.headnameObjekte);
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            } catch (NullPointerException var6) {
               ;
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
