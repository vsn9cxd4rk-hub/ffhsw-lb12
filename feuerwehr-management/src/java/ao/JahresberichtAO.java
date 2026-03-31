package ao;

import ao.AbstractFenster;
import ao.statistik.AbwesenheitsStatistikAO;
import ao.statistik.AnwesenheitBrandsicherheitswachenAO;
import ao.statistik.AnwesenheitDienstabendAO;
import ao.statistik.AnwesenheitEinsatzAO;
import ao.statistik.AnwesenheitGesamtAO;
import ao.statistik.AusrueckezeitenAO;
import ao.statistik.BSWMannstundenProMonatAO;
import ao.statistik.BswMannstundenAO;
import ao.statistik.EinsatzArtAO;
import ao.statistik.EinsatzMannstundenAO;
import ao.statistik.EinsatzMannstundenproMonatAO;
import ao.statistik.EinsatzProMonatAO;
import ao.statistik.EinsatzProStundeAO;
import ao.statistik.EinsatzProWochentagAO;
import ao.statistik.EinsatzTagNacht;
import ao.statistik.FahrzeugbelegungStatistikAO;
import ao.statistik.MitgliederAnzahlStatistikAO;
import ao.statistik.MitgliederDienstgradStatistikAO;
import ao.statistik.MitgliederDurchschnittsalterStatistikAO;
import ao.statistik.SchutzzielStatistikAO;
import ao.statistik.StadtteilStatistikAO;
import ao.statistik.StichwortStatistikAO;
import ao.statistik.VeranstaltungStatistikAO;
import ao.statistik.VerfuegbarkeitsstatistikEinsatzAO;
import ao.utils.ProzessBarAO;
import data.tabellen.TabelleJahresbericht;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import go.Jahresbericht;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.BerichtPDFSchreiben;
import run.runApplication;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class JahresberichtAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextArea textfield;
   private JTextField title;
   private JLabel title_label;
   private JLabel druckenMitDeckblatt_label;
   private JCheckBox druckenMitDeckblatt;
   private JScrollPane pane;
   private JCheckBox[] jCheckboxArray;
   private JScrollPane paneStatistiken;
   private JComboBox jahre;
   private JLabel jahre_label;
   private JComboBox mitgliederGruppe;
   private JLabel mitgliederGruppe_label;
   public static JCheckBox protokolle;
   public static JTree tree;
   private JScrollPane scrollPaneTree;
   private JPanel panel;
   private JPanel panel2;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JLabel dummy3;


   public JahresberichtAO() {
      super("FeuerwehrManagementSystem - Jahresbericht");
      logging.logInfo("Starte: JahresberichtAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern & Erstellen");
      this.buttonZurueck = new JButton("Schließen");
      this.textfield = new JTextArea(23, 50);
      this.textfield.setLineWrap(true);
      this.textfield.setWrapStyleWord(true);
      this.pane = new JScrollPane(this.textfield);
      this.pane.setVerticalScrollBarPolicy(22);
      this.title = new JTextField("BERICHT VOM " + SbcUtils.timeStamp("dd.MM.yyyy"), 60);
      this.title_label = new JLabel("Berichtetitel: ");
      this.jahre_label = new JLabel("Bericht Jahr: ");
      this.mitgliederGruppe_label = new JLabel("Mitgliedergruppe: ");
      this.druckenMitDeckblatt_label = new JLabel("Druck mit Deckblatt: ");
      this.druckenMitDeckblatt = new JCheckBox();
      tree = new JTree(CreateTrees.CreateTreeJahresberichteTemplates(runApplication.mitgliederGruppe));
      tree.setSelectionRow(1);
      this.scrollPaneTree = new JScrollPane(tree);
      this.scrollPaneTree.setVerticalScrollBarPolicy(22);
      tree.setSelectionRow(0);
      this.modulBeschreibung = new JLabel("Jahresbericht");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.dummy3 = new JLabel(runApplication.dummyImage);
      protokolle = new JCheckBox("Protokolle / Tätigkeitsberichte von Einsätzen hinzufügen");
   }

   protected void labelErstellen() {
      try {
         TabelleJahr e = new TabelleJahr();
         TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
         String[] jahresListe = Utils.listToArray(e.getAllVerfügbarenJahre());
         String[] mitgliederGruppeListe = Utils.listToArray(tabGruppe.getAllGruppen());
         this.jahre = new JComboBox(jahresListe);
         this.jahre.setSelectedItem(SbcUtils.timeStamp("yyyy"));
         this.mitgliederGruppe = new JComboBox(mitgliederGruppeListe);
         this.mitgliederGruppe.setSelectedItem(runApplication.mitgliederGruppe);
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

      this.mitgliederGruppe.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            runApplication.mitgliederGruppe = JahresberichtAO.this.mitgliederGruppe.getSelectedItem().toString();
            JahresberichtAO.tree.setModel(CreateTrees.CreateTreeJahresberichteTemplates(runApplication.mitgliederGruppe));
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
      this.setSize(1220, 750);
      this.setTitle("FeuerwehrManagementSystem - Jahresbericht");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      Border lowerEtched = BorderFactory.createEtchedBorder(1);
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panel = new JPanel(new GridLayout(4, 1));
      this.getContentPane().add("Center", this.panel);
      this.jahre_label.setPreferredSize(new Dimension(100, 25));
      this.panel.add(this.mitgliederGruppe_label);
      this.panel.add(this.jahre_label);
      this.panel.add(this.title_label);
      this.panel.add(this.druckenMitDeckblatt_label);
      this.panel2 = new JPanel(new GridLayout(4, 1));
      this.getContentPane().add("Center", this.panel2);
      this.panel2.add(this.mitgliederGruppe);
      this.panel2.add(this.jahre);
      this.panel2.add(this.title);
      this.panel2.add(this.druckenMitDeckblatt);
      this.add(this.dummy3);
      this.scrollPaneTree.setPreferredSize(new Dimension(300, 480));
      TitledBorder rahmenTree = BorderFactory.createTitledBorder(lowerEtched, "Vorlagen");
      this.scrollPaneTree.setBorder(rahmenTree);
      this.add(this.scrollPaneTree);
      TitledBorder rahmen = BorderFactory.createTitledBorder(lowerEtched, "Beschreibung / Kommentar / Bericht");
      this.pane.setBorder(rahmen);
      this.pane.setPreferredSize(new Dimension(550, 480));
      this.add(this.pane);
      JPanel panel = new JPanel(new GridLayout(0, 1));
      this.paneStatistiken = new JScrollPane(panel);
      this.paneStatistiken.setVerticalScrollBarPolicy(22);
      this.paneStatistiken.setPreferredSize(new Dimension(300, 480));
      TitledBorder rahmen2 = BorderFactory.createTitledBorder(lowerEtched, "Statistiken");
      this.paneStatistiken.setBorder(rahmen2);
      String[] statiktikenListe = new String[]{"Anwesenheit Einsatz", "Anwesenheit Brandsicherheitswache", "Anwesenheit Dienstabend", "Anwesenheit Gesamt", "Ausrückezeiten", "Abwesenheitsstatistik", "Einsatzart", "Einsatz Mannstunden", "Einsatz Mannstunden pro Monat", "BSW Mannstunden", "BSW Mannstunden pro Monat", "Einsatz Pro Monat", "Einsatz Pro Stunde", "Einsatz Pro Wochentag", "Veranstaltungszählung", "Durchschnittsalter", "Mitgliederzahlen", "Schutzziel Statistik", "Stadtteil Statistik", "Mitglieder Dienstgard", "Stichwort Statistik", "Tag / Nacht Einsätze", "Verfügbarkeit Einsatz", "Fahrzeugbelegung"};
      int[] statistikenBerechtigung = new int[]{BerechtigunsManager.ber[21], BerechtigunsManager.ber[23], BerechtigunsManager.ber[22], BerechtigunsManager.ber[20], BerechtigunsManager.ber[26], BerechtigunsManager.ber[24], BerechtigunsManager.ber[25], BerechtigunsManager.ber[28], BerechtigunsManager.ber[28], BerechtigunsManager.ber[32], BerechtigunsManager.ber[32], BerechtigunsManager.ber[29], BerechtigunsManager.ber[30], BerechtigunsManager.ber[31], BerechtigunsManager.ber2[33], BerechtigunsManager.ber2[34], BerechtigunsManager.ber2[35], BerechtigunsManager.ber2[29], BerechtigunsManager.ber2[28], BerechtigunsManager.ber2[36], BerechtigunsManager.ber[25], BerechtigunsManager.ber2[30], BerechtigunsManager.ber2[27], BerechtigunsManager.ber2[31]};
      this.jCheckboxArray = new JCheckBox[statiktikenListe.length];

      for(int i = 0; i < statiktikenListe.length; ++i) {
         this.jCheckboxArray[i] = new JCheckBox(statiktikenListe[i]);
         panel.add(this.jCheckboxArray[i]);
         if(statistikenBerechtigung[i] == 0) {
            this.jCheckboxArray[i].setEnabled(false);
         }

         if(i == statiktikenListe.length - 1) {
            panel.add(new JLabel());
            panel.add(protokolle);
         }
      }

      this.add(this.paneStatistiken);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
      this.druckenMitDeckblatt.setSelected(true);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent arg0) {
            TabelleJahresbericht tabBericht = new TabelleJahresbericht();

            try {
               JahresberichtAO.this.title.setText(JahresberichtAO.tree.getSelectionPath().getLastPathComponent().toString());
               JahresberichtAO.this.jahre.setSelectedItem(tabBericht.getJahrOfBericht(JahresberichtAO.tree.getSelectionPath().getLastPathComponent().toString()));
               JahresberichtAO.this.textfield.setText(tabBericht.getBericht(JahresberichtAO.tree.getSelectionPath().getLastPathComponent().toString()));

               for(int e = 0; e < JahresberichtAO.this.jCheckboxArray.length; ++e) {
                  JahresberichtAO.this.jCheckboxArray[e].setSelected(false);
               }

               JahresberichtAO.protokolle.setSelected(false);
               int[] var7 = tabBericht.getSelectedStatistiken(JahresberichtAO.tree.getSelectionPath().getLastPathComponent().toString());
               if(var7 != null) {
                  for(int s = 0; s < var7.length; ++s) {
                     if(var7[s] == -1) {
                        JahresberichtAO.protokolle.setSelected(true);
                     } else {
                        JahresberichtAO.this.jCheckboxArray[var7[s]].setSelected(true);
                     }
                  }
               }
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            } catch (NullPointerException var6) {
               ;
            }

         }
      });
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Thread thread = new Thread() {
               public void run() {
                  TabelleJahresbericht tabBericht = new TabelleJahresbericht();
                  Jahresbericht bericht = new Jahresbericht();
                  TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

                  try {
                     if(tabBericht.getBerichtCount(JahresberichtAO.this.title.getText()) == 1) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.BERICHTNAME_BEREITS_VORHANDEN, "Warnung", 2);
                     } else {
                        int e = tabBericht.getNextNummer();
                        int jahr = Integer.parseInt(JahresberichtAO.this.jahre.getSelectedItem().toString());
                        int mGruppe = tabGruppe.getID(runApplication.mitgliederGruppe);
                        String pdfDateiname = runApplication.arbeitsverzeichnis + "data/" + jahr + "/Berichte/" + JahresberichtAO.this.title.getText() + "_" + runApplication.mitgliederGruppe + "_" + JahresberichtAO.this.jahre.getSelectedItem().toString() + "_ID" + e + ".pdf";
                        boolean pos = false;
                        bericht.setId(e);
                        bericht.setJahr(jahr);
                        bericht.setTitle(JahresberichtAO.this.title.getText());
                        bericht.setBericht(JahresberichtAO.this.textfield.getText());
                        bericht.setErstelldatum(SbcUtils.timeStamp("yyyy-MM-dd"));
                        bericht.setAutoBericht(0);
                        bericht.setDateiname(JahresberichtAO.this.title.getText() + " " + JahresberichtAO.this.jahre.getSelectedItem().toString() + ".pdf");
                        bericht.setMitgliederGruppe(mGruppe);
                        ProzessBarAO.progressbar.setValue(1);
                        logging.logInfo("Bericht wird erstellt");
                        String outputfolderTemp = runApplication.arbeitsverzeichnis + "data/" + jahr + "/Temp/";
                        StringBuilder selectedStatistiken = new StringBuilder();
                        String[] grafiken = new String[24];
                        String[] grafikenBeschreibungen = new String[24];
                        if(JahresberichtAO.this.jCheckboxArray[0].isSelected()) {
                           MyChartUtils.writeChartToJPEG(AnwesenheitEinsatzAO.createChart(AnwesenheitEinsatzAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Einsatz.jpg");
                           grafiken[0] = outputfolderTemp + "Anwesenheit_Einsatz.jpg";
                           grafikenBeschreibungen[0] = "Anwesenheit Einsatz";
                           ProzessBarAO.progressbar.setValue(100 / JahresberichtAO.this.jCheckboxArray.length + 1);
                           selectedStatistiken.append("0,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[1].isSelected()) {
                           MyChartUtils.writeChartToJPEG(AnwesenheitBrandsicherheitswachenAO.createChart(AnwesenheitBrandsicherheitswachenAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Brandsicherheitswache.jpg");
                           grafiken[1] = outputfolderTemp + "Anwesenheit_Brandsicherheitswache.jpg";
                           grafikenBeschreibungen[1] = "Anwesenheit Brandsicherheitswache";
                           ProzessBarAO.progressbar.setValue(200 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("1,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[2].isSelected()) {
                           MyChartUtils.writeChartToJPEG(AnwesenheitDienstabendAO.createChart(AnwesenheitDienstabendAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Dienst.jpg");
                           grafiken[2] = outputfolderTemp + "Anwesenheit_Dienst.jpg";
                           grafikenBeschreibungen[2] = "Anwesenheit Dienstabend";
                           ProzessBarAO.progressbar.setValue(300 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("2,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[3].isSelected()) {
                           MyChartUtils.writeChartToJPEG(AnwesenheitGesamtAO.createChart(AnwesenheitGesamtAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Gesamt.jpg");
                           grafiken[3] = outputfolderTemp + "Anwesenheit_Gesamt.jpg";
                           grafikenBeschreibungen[3] = "Anwesenheit Gesamt";
                           ProzessBarAO.progressbar.setValue(400 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("3,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[4].isSelected()) {
                           MyChartUtils.writeChartToJPEG(AusrueckezeitenAO.createChart(AusrueckezeitenAO.createDataset()), 1000, 800, outputfolderTemp + "Ausrückezeit.jpg");
                           grafiken[4] = outputfolderTemp + "Ausrückezeit.jpg";
                           grafikenBeschreibungen[4] = "Ausrückezeiten";
                           ProzessBarAO.progressbar.setValue(500 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("4,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[5].isSelected()) {
                           MyChartUtils.writeChartToJPEG(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Abwesenheit.jpg");
                           grafiken[5] = outputfolderTemp + "Abwesenheit.jpg";
                           grafikenBeschreibungen[5] = "Abwesenheitsgründe";
                           ProzessBarAO.progressbar.setValue(600 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("5,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[6].isSelected()) {
                           MyChartUtils.writeChartToJPEG(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Einsatzart.jpg");
                           grafiken[6] = outputfolderTemp + "Einsatzart.jpg";
                           grafikenBeschreibungen[6] = "Einsatzart";
                           ProzessBarAO.progressbar.setValue(700 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("6,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[7].isSelected()) {
                           MyChartUtils.writeChartToJPEG(EinsatzMannstundenAO.createChart(EinsatzMannstundenAO.createDataset()), 1000, 800, outputfolderTemp + "EinsatzMannstunden.jpg");
                           grafiken[7] = outputfolderTemp + "EinsatzMannstunden.jpg";
                           grafikenBeschreibungen[7] = "Einsatz Mannstunden";
                           ProzessBarAO.progressbar.setValue(800 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("7,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[8].isSelected()) {
                           MyChartUtils.writeChartToJPEG(EinsatzMannstundenproMonatAO.createChart(EinsatzMannstundenproMonatAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "EinsatzMannstundenProMonat.jpg");
                           grafiken[8] = outputfolderTemp + "EinsatzMannstundenProMonat.jpg";
                           grafikenBeschreibungen[8] = "Einsatz Mannstunden pro Monat";
                           ProzessBarAO.progressbar.setValue(900 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("8,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[9].isSelected()) {
                           MyChartUtils.writeChartToJPEG(BswMannstundenAO.createChart(BswMannstundenAO.createDataset()), 1000, 800, outputfolderTemp + "BSWMannstunden.jpg");
                           grafiken[9] = outputfolderTemp + "BSWMannstunden.jpg";
                           grafikenBeschreibungen[9] = "BSW Mannstunden";
                           ProzessBarAO.progressbar.setValue(1000 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("9,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[10].isSelected()) {
                           MyChartUtils.writeChartToJPEG(BSWMannstundenProMonatAO.createChart(BSWMannstundenProMonatAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "BSWMannstundenProMonat.jpg");
                           grafiken[10] = outputfolderTemp + "BSWMannstundenProMonat.jpg";
                           grafikenBeschreibungen[10] = "BSW Mannstunden pro Monat";
                           ProzessBarAO.progressbar.setValue(1100 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("10,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[11].isSelected()) {
                           MyChartUtils.writeChartToJPEG(EinsatzProMonatAO.createChart(EinsatzProMonatAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "EinsatzProMonat.jpg");
                           grafiken[11] = outputfolderTemp + "EinsatzProMonat.jpg";
                           grafikenBeschreibungen[11] = "Einsatz Pro Monat";
                           ProzessBarAO.progressbar.setValue(1200 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("11,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[12].isSelected()) {
                           MyChartUtils.writeChartToJPEG(EinsatzProStundeAO.createChart(EinsatzProStundeAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "EinsatzProStunde.jpg");
                           grafiken[12] = outputfolderTemp + "EinsatzProStunde.jpg";
                           grafikenBeschreibungen[12] = "Einsatz Pro Stunde";
                           ProzessBarAO.progressbar.setValue(1300 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("12,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[13].isSelected()) {
                           MyChartUtils.writeChartToJPEG(EinsatzProWochentagAO.createChart(EinsatzProWochentagAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "EinsatzProWochentag.jpg");
                           grafiken[13] = outputfolderTemp + "EinsatzProWochentag.jpg";
                           grafikenBeschreibungen[13] = "Einsatz Pro Wochentag";
                           ProzessBarAO.progressbar.setValue(1400 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("13,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[14].isSelected()) {
                           MyChartUtils.writeChartToJPEG(VeranstaltungStatistikAO.createChart(VeranstaltungStatistikAO.createDataset(jahr), jahr), 1000, 800, outputfolderTemp + "Veranstaltungszählung.jpg");
                           grafiken[14] = outputfolderTemp + "Veranstaltungszählung.jpg";
                           grafikenBeschreibungen[14] = "Veranstaltungszählung";
                           ProzessBarAO.progressbar.setValue(1500 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("14,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[15].isSelected()) {
                           MyChartUtils.writeChartToJPEG(MitgliederDurchschnittsalterStatistikAO.createChart(MitgliederDurchschnittsalterStatistikAO.createDataset()), 1000, 800, outputfolderTemp + "MitgliederDuchschnittsalter.jpg");
                           grafiken[15] = outputfolderTemp + "MitgliederDuchschnittsalter.jpg";
                           grafikenBeschreibungen[15] = "Durchschnittsalter";
                           ProzessBarAO.progressbar.setValue(1600 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("15,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[16].isSelected()) {
                           MyChartUtils.writeChartToJPEG(MitgliederAnzahlStatistikAO.createChart(MitgliederAnzahlStatistikAO.createDataset()), 1000, 800, outputfolderTemp + "Mitgliederzahlen.jpg");
                           grafiken[16] = outputfolderTemp + "Mitgliederzahlen.jpg";
                           grafikenBeschreibungen[16] = "Mitgliederzahlen";
                           ProzessBarAO.progressbar.setValue(1700 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("16,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[17].isSelected()) {
                           MyChartUtils.writeChartToJPEG(SchutzzielStatistikAO.createChart(SchutzzielStatistikAO.createDataset()), 1000, 800, outputfolderTemp + "Schutzziel_Statistik.jpg");
                           grafiken[17] = outputfolderTemp + "Schutzziel_Statistik.jpg";
                           grafikenBeschreibungen[17] = "Schutzziel Statistik";
                           ProzessBarAO.progressbar.setValue(1800 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("17,");
                        }

                        if(JahresberichtAO.this.jCheckboxArray[18].isSelected()) {
                           MyChartUtils.writeChartToJPEG(StadtteilStatistikAO.createChart(StadtteilStatistikAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Stadtteil_Statistik.jpg");
                           grafiken[18] = outputfolderTemp + "Stadtteil_Statistik.jpg";
                           grafikenBeschreibungen[18] = "Stadtteil Statistik";
                           ProzessBarAO.progressbar.setValue(1800 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append("18,");
                        }

                        byte pos1 = 19;
                        if(JahresberichtAO.this.jCheckboxArray[pos1].isSelected()) {
                           MyChartUtils.writeChartToJPEG(MitgliederDienstgradStatistikAO.createChart(MitgliederDienstgradStatistikAO.createDataset()), 1000, 800, outputfolderTemp + "Mitglieder_Dienstgrad.jpg");
                           grafiken[pos1] = outputfolderTemp + "Mitglieder_Dienstgrad.jpg";
                           grafikenBeschreibungen[pos1] = "Mitglieder Dienstgrad";
                           ProzessBarAO.progressbar.setValue(pos1 * 100 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append(pos1 + ",");
                        }

                        pos1 = 20;
                        if(JahresberichtAO.this.jCheckboxArray[pos1].isSelected()) {
                           MyChartUtils.writeChartToJPEG(StichwortStatistikAO.createChart(StichwortStatistikAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Stichwort_Statistik.jpg");
                           grafiken[pos1] = outputfolderTemp + "Stichwort_Statistik.jpg";
                           grafikenBeschreibungen[pos1] = "Stichwort Statistik";
                           ProzessBarAO.progressbar.setValue(pos1 * 100 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append(pos1 + ",");
                        }

                        pos1 = 21;
                        if(JahresberichtAO.this.jCheckboxArray[pos1].isSelected()) {
                           MyChartUtils.writeChartToJPEG(EinsatzTagNacht.createChart(EinsatzTagNacht.createDataset(jahr)), 1000, 800, outputfolderTemp + "Tag_Nacht_Einsätze.jpg");
                           grafiken[pos1] = outputfolderTemp + "Tag_Nacht_Einsätze.jpg";
                           grafikenBeschreibungen[pos1] = "Tag / Nacht Einsätze";
                           ProzessBarAO.progressbar.setValue(pos1 * 100 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append(pos1 + ",");
                        }

                        pos1 = 22;
                        if(JahresberichtAO.this.jCheckboxArray[pos1].isSelected()) {
                           MyChartUtils.writeChartToJPEG(VerfuegbarkeitsstatistikEinsatzAO.createChart(VerfuegbarkeitsstatistikEinsatzAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Verfuegbare_Mitglieder.jpg");
                           grafiken[pos1] = outputfolderTemp + "Verfuegbare_Mitglieder.jpg";
                           grafikenBeschreibungen[pos1] = "Verfügbare Mitglieder";
                           ProzessBarAO.progressbar.setValue(pos1 * 100 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append(pos1 + ",");
                        }

                        pos1 = 23;
                        if(JahresberichtAO.this.jCheckboxArray[pos1].isSelected()) {
                           MyChartUtils.writeChartToJPEG(FahrzeugbelegungStatistikAO.createChart(FahrzeugbelegungStatistikAO.createDataset(jahr, 0)), 1000, 800, outputfolderTemp + "Fahrzeugbelegung.jpg");
                           grafiken[pos1] = outputfolderTemp + "Fahrzeugbelegung.jpg";
                           grafikenBeschreibungen[pos1] = "Fahrzeugbelegung";
                           ProzessBarAO.progressbar.setValue(pos1 * 100 / JahresberichtAO.this.jCheckboxArray.length);
                           selectedStatistiken.append(pos1 + ",");
                        }

                        BerichtPDFSchreiben.PDFdocumentErstellen(pdfDateiname, JahresberichtAO.this.title.getText(), JahresberichtAO.this.textfield.getText(), Integer.toString(jahr), grafiken, grafikenBeschreibungen, JahresberichtAO.this.druckenMitDeckblatt.isSelected(), JahresberichtAO.protokolle.isSelected());
                        if(JahresberichtAO.protokolle.isSelected()) {
                           selectedStatistiken.append("-1");
                        }

                        if(selectedStatistiken.length() == 0) {
                           selectedStatistiken.append("leer");
                        }

                        bericht.setStatistiken(selectedStatistiken.toString());
                        tabBericht.insert(bericht);
                        ProzessBarAO.progressbar.setValue(100);
                        MyEvent.setEvent("0x0030");
                        Utils.dateiKatalogisieren(pdfDateiname);
                        Desktop.getDesktop().open(new File(pdfDateiname));
                        logbuchEingabe.NeuerEintag("Bericht wurde erstellt Dateinmae: " + pdfDateiname);
                        JahresberichtAO.this.dispose();
                     }
                  } catch (IOException var13) {
                     logging.logPrintStackTrace(var13);
                  }

               }
            };
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            thread.start();
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
