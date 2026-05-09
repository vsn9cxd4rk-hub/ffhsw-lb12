/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.statistik;

import ao.AbstractFenster;
import ao.statistik.AbwesenheitsStatistikAO;
import ao.statistik.AnAbwesenheitStatistikAO;
import ao.statistik.AnwesendeMitgliederProDienstAO;
import ao.statistik.AnwesendeMitgliederProEinsatzAO;
import ao.statistik.AnwesenheitBrandsicherheitswachenAO;
import ao.statistik.AnwesenheitDienstabendAO;
import ao.statistik.AnwesenheitEinsatzAO;
import ao.statistik.AnwesenheitGesamtAO;
import ao.statistik.AnwesenheitGesamtInProzentAO;
import ao.statistik.AnwesenheitSonstigeVeranstaltungAO;
import ao.statistik.AtemschutzstatistikAO;
import ao.statistik.AusbilderStatistikAO;
import ao.statistik.AusbildungsStatistikAO;
import ao.statistik.AusrueckezeitenAO;
import ao.statistik.BSWMannstundenProMonatAO;
import ao.statistik.BeteiligungsdauerAO;
import ao.statistik.BswMannstundenAO;
import ao.statistik.DauerAlarmfahrtAO;
import ao.statistik.EinsatzArtAO;
import ao.statistik.EinsatzFahrzeugStatistikAO;
import ao.statistik.EinsatzMannstundenAO;
import ao.statistik.EinsatzMannstundenproMonatAO;
import ao.statistik.EinsatzProMonatAO;
import ao.statistik.EinsatzProStundeAO;
import ao.statistik.EinsatzProWochentagAO;
import ao.statistik.EinsatzTagNacht;
import ao.statistik.EinsatzdauerAO;
import ao.statistik.FahrzeugbelegungStatistikAO;
import ao.statistik.FehlalarmeStatistikAO;
import ao.statistik.MitgliederAnzahlStatistikAO;
import ao.statistik.MitgliederDienstgradStatistikAO;
import ao.statistik.MitgliederDurchschnittsalterStatistikAO;
import ao.statistik.MitgliederFunktionenStatistikAO;
import ao.statistik.SchutzzielStatistikAO;
import ao.statistik.SonstigeMannstundenAO;
import ao.statistik.StadtteilStatistikAO;
import ao.statistik.StichwortStatistikAO;
import ao.statistik.VeranstaltungAnwesenheitStatistikAO;
import ao.statistik.VeranstaltungStatistikAO;
import ao.statistik.VerfuegbarkeitsstatistikEinsatzAO;
import ao.utils.ProzessBarAO;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
import utilities.MyChartUtils;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class StatistikAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZuerueck;
    private JButton buttonPdfExport;
    private JButton buttonJpgExport;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelStatistik;
    private JPanel panelKonfiguration;
    private JTabbedPane tabPane;
    private JFileChooser chooser;
    private JLabel jahr_label;
    private JLabel mitglieder_label;
    private JLabel fahrzeuge_label;
    private JLabel veranstaltungskategorie_label;
    private JComboBox<String> jahr;
    private JComboBox<String> mitglieder;
    private JComboBox<String> fahrzeuge;
    private JComboBox<String> veranstaltungskategorie;
    private JLabel hinweis;
    private JLabel statistikBeschreibung;
    private JButton buttonParameterAnwenden;
    private String lastSelectedTreeItem = "";

    public StatistikAO() {
        super("FeuerwehrManagementSystem - Statistik");
        logging.logInfo((Object)"Starte: StatistikAO");
    }

    protected void buttonErstellen() {
        this.buttonZuerueck = new JButton("Schlie\u00dfen");
        this.buttonPdfExport = new JButton("PDF Export");
        this.buttonJpgExport = new JButton("JPG Export");
        this.buttonJpgExport.setToolTipText("Statistik als Bild Exportieren");
        this.modulBeschreibung = new JLabel("Statistik");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.panelStatistik = new JPanel();
        this.panelKonfiguration = new JPanel();
        this.tabPane = new JTabbedPane();
        this.chooser = new JFileChooser();
        tree = new JTree(CreateTrees.CreateTreeStatistik());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
        this.buttonParameterAnwenden = new JButton("Parameter anwenden");
        this.buttonParameterAnwenden.setToolTipText("Berechnet die Statistik mit den eingestellen Parametern neu...");
        this.jahr_label = new JLabel("Jahr: ");
        this.mitglieder_label = new JLabel("Mitglieder: ");
        this.fahrzeuge_label = new JLabel("Fahrzeuge: ");
        this.veranstaltungskategorie_label = new JLabel("Veranstaltungskategorie: ");
        this.hinweis = new JLabel();
        this.statistikBeschreibung = new JLabel();
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleJahr tabJahr = new TabelleJahr();
            TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
            TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
            String[] mitliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            String[] jahreListe = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            String[] fahrzeugListe = Utils.listToArrayMitOptionAlle(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());
            String[] kategorieListe = Utils.listToArrayOnlyFORComboBoxes(tabKategorie.getAllKategorien());
            this.mitglieder = new JComboBox<String>(mitliederListe);
            this.fahrzeuge = new JComboBox<String>(fahrzeugListe);
            this.veranstaltungskategorie = new JComboBox<String>(kategorieListe);
            this.jahr = new JComboBox<String>(jahreListe);
            this.jahr.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelErstellen() {
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() >= 1600.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() >= 900.0) {
            logging.logInfo((Object)"Starte GUI mit: 1600x900 ++");
            this.setSize(1580, 890);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1440.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 900.0) {
            logging.logInfo((Object)"Starte GUI mit: 1440x900");
            this.setSize(1440, 890);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 768.0) {
            logging.logInfo((Object)"Starte GUI mit: 1366x768");
            this.setSize(1366, 768);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
            logging.logInfo((Object)"Starte GUI mit: 1280x1024");
            this.setSize(1280, 890);
        } else {
            logging.logInfo((Object)("Starte GUI mit: " + runApplication.bildschirmgr\u00f6\u00dfe.getWidth() + "x" + runApplication.bildschirmgr\u00f6\u00dfe.getHeight()));
            int w = (int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth();
            int h = (int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight();
            this.setSize(w, h);
        }
        this.setTitle("FeuerwehrManagementSystem - Staistik");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelKonfiguration = new JPanel(new GridLayout(25, 1));
        this.getContentPane().add("Center", this.panelKonfiguration);
        this.panelKonfiguration.add(this.statistikBeschreibung);
        this.panelKonfiguration.add(this.dummy2);
        this.panelKonfiguration.add(this.hinweis);
        this.panelKonfiguration.add(this.jahr_label);
        this.panelKonfiguration.add(this.jahr);
        this.panelKonfiguration.add(this.mitglieder_label);
        this.panelKonfiguration.add(this.mitglieder);
        this.panelKonfiguration.add(this.fahrzeuge_label);
        this.panelKonfiguration.add(this.fahrzeuge);
        this.panelKonfiguration.add(this.veranstaltungskategorie_label);
        this.panelKonfiguration.add(this.veranstaltungskategorie);
        this.panelKonfiguration.add(this.buttonParameterAnwenden);
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() >= 1600.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() >= 900.0) {
            this.tabPane.setPreferredSize(new Dimension(1240, 760));
            this.scrollPaneTree.setPreferredSize(new Dimension(300, 760));
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1440.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 900.0) {
            this.tabPane.setPreferredSize(new Dimension(1160, 760));
            this.scrollPaneTree.setPreferredSize(new Dimension(220, 760));
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
            this.tabPane.setPreferredSize(new Dimension(1040, 760));
            this.scrollPaneTree.setPreferredSize(new Dimension(200, 760));
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 768.0) {
            this.tabPane.setPreferredSize(new Dimension(1100, 630));
            this.scrollPaneTree.setPreferredSize(new Dimension(200, 630));
        } else {
            this.tabPane.setPreferredSize(new Dimension(1040, 760));
            this.scrollPaneTree.setPreferredSize(new Dimension(200, 760));
        }
        this.add(this.scrollPaneTree);
        this.tabPane.addTab("Statistik Parmeter     ", this.panelKonfiguration);
        this.tabPane.addTab("Statistik Daten        ", this.panelStatistik);
        this.add(this.tabPane);
        this.sichtbarkeitKonfigurationspanel(3);
        this.add(this.buttonPdfExport);
        this.add(this.buttonJpgExport);
        this.add(this.buttonZuerueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZuerueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                StatistikAO.this.executeTreeSelection();
            }
        });
        this.buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                StatistikAO.this.chooser.setFileSelectionMode(1);
                StatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + StatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(StatistikAO.this.chooser.getSelectedFile().getPath()) + "/" + tree.getLastSelectedPathComponent() + "_PDF_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                String isSelected = tree.getLastSelectedPathComponent().toString();
                int jahrParameter = Integer.parseInt(StatistikAO.this.jahr.getSelectedItem().toString());
                int wight = 550;
                int hight = 800;
                if (isSelected.equals("Einsatz")) {
                    MyChartUtils.writeChartToPDF(AnwesenheitEinsatzAO.createChart(AnwesenheitEinsatzAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Dienst")) {
                    MyChartUtils.writeChartToPDF(AnwesenheitDienstabendAO.createChart(AnwesenheitDienstabendAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("BSW")) {
                    MyChartUtils.writeChartToPDF(AnwesenheitBrandsicherheitswachenAO.createChart(AnwesenheitBrandsicherheitswachenAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Anwesenheit Gesamt")) {
                    MyChartUtils.writeChartToPDF(AnwesenheitGesamtAO.createChart(AnwesenheitGesamtAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Beteiligungsdauer")) {
                    try {
                        MyChartUtils.writeChartToPDF(BeteiligungsdauerAO.createChart(BeteiligungsdauerAO.createDataset(new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()), jahrParameter)), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Abwesenheitsstatistik")) {
                    MyChartUtils.writeChartToPDF(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Ausbildungsstatistik")) {
                    MyChartUtils.writeChartToPDF(AusbildungsStatistikAO.createChart(AusbildungsStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Durchschnittliche Einsatzdauer")) {
                    MyChartUtils.writeChartToPDF(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahrParameter)), 500, hight, outputFile);
                } else if (isSelected.equals("Einsatzdauer")) {
                    MyChartUtils.writeChartToPDF(EinsatzdauerAO.createChart(EinsatzdauerAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Ausr\u00fcckezeiten")) {
                    MyChartUtils.writeChartToPDF(AusrueckezeitenAO.createChart(AusrueckezeitenAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Alarmfahrtdauer")) {
                    MyChartUtils.writeChartToPDF(DauerAlarmfahrtAO.createChart(DauerAlarmfahrtAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Fahrzeuge im Einsatz")) {
                    MyChartUtils.writeChartToPDF(EinsatzFahrzeugStatistikAO.createChart(EinsatzFahrzeugStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Fehlalarme")) {
                    MyChartUtils.writeChartToPDF(FehlalarmeStatistikAO.createChart(FehlalarmeStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz pro Stunde")) {
                    MyChartUtils.writeChartToPDF(EinsatzProStundeAO.createChart(EinsatzProStundeAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz pro Monat")) {
                    MyChartUtils.writeChartToPDF(EinsatzProMonatAO.createChart(EinsatzProMonatAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz Pro Wochentag")) {
                    MyChartUtils.writeChartToPDF(EinsatzProWochentagAO.createChart(EinsatzProWochentagAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz Mannstunden")) {
                    StatistikAO.this.panelStatistik = EinsatzMannstundenAO.createPanel();
                    MyChartUtils.writeChartToPDF(EinsatzMannstundenAO.createChart(EinsatzMannstundenAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("BSW Mannstunden")) {
                    MyChartUtils.writeChartToPDF(BswMannstundenAO.createChart(BswMannstundenAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Sonstige Mannstunden")) {
                    try {
                        int kID = new TabelleVeranstaltung_Kategorie().getID(StatistikAO.this.veranstaltungskategorie.getSelectedItem().toString());
                        MyChartUtils.writeChartToPDF(SonstigeMannstundenAO.createChart(SonstigeMannstundenAO.createDataset(kID)), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Stadtteil Statistik")) {
                    MyChartUtils.writeChartToPDF(StadtteilStatistikAO.createChart(StadtteilStatistikAO.createDataset(jahrParameter)), 500, hight, outputFile);
                } else if (isSelected.equals("Anwesenheit Gesamt in %")) {
                    MyChartUtils.writeChartToPDF(AnwesenheitGesamtInProzentAO.createChart(AnwesenheitGesamtInProzentAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Beteiligung bei...")) {
                    MyChartUtils.writeChartToPDF(VeranstaltungAnwesenheitStatistikAO.createChart(VeranstaltungAnwesenheitStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Atemschutzstatistik")) {
                    try {
                        MyChartUtils.writeChartToPDF(AtemschutzstatistikAO.createChart(AtemschutzstatistikAO.createDataset(new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()), jahrParameter)), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Durchschnittliche Einsatzbeteiligung")) {
                    MyChartUtils.writeChartToPDF(AnwesendeMitgliederProEinsatzAO.createChart(AnwesendeMitgliederProEinsatzAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Durchschnittliche Dienstbeteiligung")) {
                    MyChartUtils.writeChartToPDF(AnwesendeMitgliederProDienstAO.createChart(AnwesendeMitgliederProEinsatzAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Sonstige Veranstaltungen")) {
                    MyChartUtils.writeChartToPDF(AnwesenheitSonstigeVeranstaltungAO.createChart(AnwesenheitSonstigeVeranstaltungAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("An- / Abwesenheitsstatistik")) {
                    try {
                        MyChartUtils.writeChartToPDF(AnAbwesenheitStatistikAO.createChart(AnAbwesenheitStatistikAO.createDataset(jahrParameter, new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()))), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Ausbilder Statistik")) {
                    MyChartUtils.writeChartToPDF(AusbilderStatistikAO.createChart(AusbilderStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Veranstaltungsz\u00e4hlung")) {
                    MyChartUtils.writeChartToPDF(VeranstaltungStatistikAO.createChart(VeranstaltungStatistikAO.createDataset(jahrParameter), jahrParameter), wight, hight, outputFile);
                } else if (isSelected.equals("Schutzziel Statistik")) {
                    MyChartUtils.writeChartToPDF(SchutzzielStatistikAO.createChart(SchutzzielStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Durchschnittsalter")) {
                    MyChartUtils.writeChartToPDF(MitgliederDurchschnittsalterStatistikAO.createChart(MitgliederDurchschnittsalterStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Mitgliederzahlen")) {
                    MyChartUtils.writeChartToPDF(MitgliederAnzahlStatistikAO.createChart(MitgliederAnzahlStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz Mannstunden pro Monat")) {
                    MyChartUtils.writeChartToPDF(EinsatzMannstundenproMonatAO.createChart(EinsatzMannstundenproMonatAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("BSW Mannstunden pro Monat")) {
                    MyChartUtils.writeChartToPDF(BSWMannstundenProMonatAO.createChart(BSWMannstundenProMonatAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Mitglieder Dienstgrad")) {
                    MyChartUtils.writeChartToPDF(MitgliederDienstgradStatistikAO.createChart(MitgliederDienstgradStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Stichwort Statistik")) {
                    MyChartUtils.writeChartToPDF(StichwortStatistikAO.createChart(StichwortStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Tag / Nacht Eins\u00e4tze")) {
                    MyChartUtils.writeChartToPDF(EinsatzTagNacht.createChart(EinsatzTagNacht.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Verf\u00fcgbarkeit Einsatz")) {
                    MyChartUtils.writeChartToPDF(VerfuegbarkeitsstatistikEinsatzAO.createChart(VerfuegbarkeitsstatistikEinsatzAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Fahrzeugbelegung")) {
                    try {
                        MyChartUtils.writeChartToPDF(FahrzeugbelegungStatistikAO.createChart(FahrzeugbelegungStatistikAO.createDataset(jahrParameter, new TabelleFahrzeug().getFahrzeugID(StatistikAO.this.fahrzeuge.getSelectedItem().toString()))), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Mitglieder Funktionen (Anzahl)")) {
                    try {
                        MyChartUtils.writeChartToPDF(MitgliederFunktionenStatistikAO.createChart(MitgliederFunktionenStatistikAO.createDataset(jahrParameter, new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()))), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        this.buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                StatistikAO.this.chooser.setFileSelectionMode(1);
                StatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + StatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(StatistikAO.this.chooser.getSelectedFile().getPath()) + "/" + tree.getLastSelectedPathComponent() + "_JPG_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                String isSelected = tree.getLastSelectedPathComponent().toString();
                int jahrParameter = Integer.parseInt(StatistikAO.this.jahr.getSelectedItem().toString());
                int wight = 1000;
                int hight = 800;
                if (isSelected.equals("Einsatz")) {
                    MyChartUtils.writeChartToJPEG(AnwesenheitEinsatzAO.createChart(AnwesenheitEinsatzAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Dienst")) {
                    MyChartUtils.writeChartToJPEG(AnwesenheitDienstabendAO.createChart(AnwesenheitDienstabendAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("BSW")) {
                    MyChartUtils.writeChartToJPEG(AnwesenheitBrandsicherheitswachenAO.createChart(AnwesenheitBrandsicherheitswachenAO.createDataset(jahrParameter)), 1000, hight, outputFile);
                } else if (isSelected.equals("Anwesenheit Gesamt")) {
                    MyChartUtils.writeChartToJPEG(AnwesenheitGesamtAO.createChart(AnwesenheitGesamtAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Beteiligungsdauer")) {
                    try {
                        MyChartUtils.writeChartToJPEG(BeteiligungsdauerAO.createChart(BeteiligungsdauerAO.createDataset(new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()), jahrParameter)), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Abwesenheitsstatistik")) {
                    MyChartUtils.writeChartToJPEG(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Ausbildungsstatistik")) {
                    MyChartUtils.writeChartToJPEG(AusbildungsStatistikAO.createChart(AusbildungsStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatzart")) {
                    MyChartUtils.writeChartToJPEG(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Durchschnittliche Einsatzdauer")) {
                    MyChartUtils.writeChartToJPEG(EinsatzdauerAO.createChart(EinsatzdauerAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Ausr\u00fcckezeiten")) {
                    MyChartUtils.writeChartToJPEG(AusrueckezeitenAO.createChart(AusrueckezeitenAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Alarmfahrtdauer")) {
                    MyChartUtils.writeChartToJPEG(DauerAlarmfahrtAO.createChart(DauerAlarmfahrtAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Fahrzeuge im Einsatz")) {
                    MyChartUtils.writeChartToJPEG(EinsatzFahrzeugStatistikAO.createChart(EinsatzFahrzeugStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Fehlalarme")) {
                    MyChartUtils.writeChartToJPEG(FehlalarmeStatistikAO.createChart(FehlalarmeStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz pro Stunde")) {
                    MyChartUtils.writeChartToJPEG(EinsatzProStundeAO.createChart(EinsatzProStundeAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz pro Monat")) {
                    MyChartUtils.writeChartToJPEG(EinsatzProMonatAO.createChart(EinsatzProMonatAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz Pro Wochentag")) {
                    MyChartUtils.writeChartToJPEG(EinsatzProWochentagAO.createChart(EinsatzProWochentagAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz Mannstunden")) {
                    StatistikAO.this.panelStatistik = EinsatzMannstundenAO.createPanel();
                    MyChartUtils.writeChartToJPEG(EinsatzMannstundenAO.createChart(EinsatzMannstundenAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("BSW Mannstunden")) {
                    MyChartUtils.writeChartToJPEG(BswMannstundenAO.createChart(BswMannstundenAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Sonstige Mannstunden")) {
                    try {
                        int kID = new TabelleVeranstaltung_Kategorie().getID(StatistikAO.this.veranstaltungskategorie.getSelectedItem().toString());
                        MyChartUtils.writeChartToJPEG(SonstigeMannstundenAO.createChart(SonstigeMannstundenAO.createDataset(kID)), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Stadtteil Statistik")) {
                    MyChartUtils.writeChartToJPEG(StadtteilStatistikAO.createChart(StadtteilStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Anwesenheit Gesamt in %")) {
                    MyChartUtils.writeChartToJPEG(AnwesenheitGesamtInProzentAO.createChart(AnwesenheitGesamtInProzentAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Beteiligung bei...")) {
                    MyChartUtils.writeChartToJPEG(VeranstaltungAnwesenheitStatistikAO.createChart(VeranstaltungAnwesenheitStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Atemschutzstatistik")) {
                    try {
                        MyChartUtils.writeChartToJPEG(AtemschutzstatistikAO.createChart(AtemschutzstatistikAO.createDataset(new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()), jahrParameter)), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Durchschnittliche Einsatzbeteiligung")) {
                    MyChartUtils.writeChartToJPEG(AnwesendeMitgliederProEinsatzAO.createChart(AnwesendeMitgliederProEinsatzAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Durchschnittliche Dienstbeteiligung")) {
                    MyChartUtils.writeChartToJPEG(AnwesendeMitgliederProDienstAO.createChart(AnwesendeMitgliederProEinsatzAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Sonstige Veranstaltungen")) {
                    MyChartUtils.writeChartToJPEG(AnwesenheitSonstigeVeranstaltungAO.createChart(AnwesenheitSonstigeVeranstaltungAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("An- / Abwesenheitsstatistik")) {
                    try {
                        MyChartUtils.writeChartToJPEG(AnAbwesenheitStatistikAO.createChart(AnAbwesenheitStatistikAO.createDataset(jahrParameter, new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()))), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Ausbilder Statistik")) {
                    MyChartUtils.writeChartToJPEG(AusbilderStatistikAO.createChart(AusbilderStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Veranstaltungsz\u00e4hlung")) {
                    MyChartUtils.writeChartToJPEG(VeranstaltungStatistikAO.createChart(VeranstaltungStatistikAO.createDataset(jahrParameter), jahrParameter), wight, hight, outputFile);
                } else if (isSelected.equals("Schutzziel Statistik")) {
                    MyChartUtils.writeChartToJPEG(SchutzzielStatistikAO.createChart(SchutzzielStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Durchschnittsalter")) {
                    MyChartUtils.writeChartToJPEG(MitgliederDurchschnittsalterStatistikAO.createChart(MitgliederDurchschnittsalterStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Mitgliederzahlen")) {
                    MyChartUtils.writeChartToJPEG(MitgliederAnzahlStatistikAO.createChart(MitgliederAnzahlStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Einsatz Mannstunden pro Monat")) {
                    MyChartUtils.writeChartToJPEG(EinsatzMannstundenproMonatAO.createChart(EinsatzMannstundenproMonatAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("BSW Mannstunden pro Monat")) {
                    MyChartUtils.writeChartToJPEG(BSWMannstundenProMonatAO.createChart(BSWMannstundenProMonatAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Mitglieder Dienstgrad")) {
                    MyChartUtils.writeChartToJPEG(MitgliederDienstgradStatistikAO.createChart(MitgliederDienstgradStatistikAO.createDataset()), wight, hight, outputFile);
                } else if (isSelected.equals("Stichwort Statistik")) {
                    MyChartUtils.writeChartToJPEG(StichwortStatistikAO.createChart(StichwortStatistikAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Tag / Nacht Eins\u00e4tze")) {
                    MyChartUtils.writeChartToJPEG(EinsatzTagNacht.createChart(EinsatzTagNacht.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Verf\u00fcgbarkeit Einsatz")) {
                    MyChartUtils.writeChartToJPEG(VerfuegbarkeitsstatistikEinsatzAO.createChart(VerfuegbarkeitsstatistikEinsatzAO.createDataset(jahrParameter)), wight, hight, outputFile);
                } else if (isSelected.equals("Fahrzeugbelegung")) {
                    try {
                        MyChartUtils.writeChartToJPEG(FahrzeugbelegungStatistikAO.createChart(FahrzeugbelegungStatistikAO.createDataset(jahrParameter, new TabelleFahrzeug().getFahrzeugID(StatistikAO.this.fahrzeuge.getSelectedItem().toString()))), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else if (isSelected.equals("Mitglieder Funktionen (Anzahl)")) {
                    try {
                        MyChartUtils.writeChartToJPEG(MitgliederFunktionenStatistikAO.createChart(MitgliederFunktionenStatistikAO.createDataset(jahrParameter, new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()))), wight, hight, outputFile);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        this.buttonParameterAnwenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                StatistikAO.this.executeTreeSelection();
            }
        });
    }

    private void sichtbarkeitKonfigurationspanel(int id) {
        this.statistikBeschreibung.setText(tree.getLastSelectedPathComponent().toString());
        if (id == 0) {
            this.hinweis.setText("HINWEIS: Sie k\u00f6nnen durch die Auswahlboxen die Parameter der Statikstik konfigurieren");
            this.mitglieder.setVisible(true);
            this.mitglieder_label.setVisible(true);
            this.jahr.setVisible(true);
            this.jahr_label.setVisible(true);
            this.buttonParameterAnwenden.setVisible(true);
        } else if (id == 1) {
            this.hinweis.setText("HINWEIS: Sie k\u00f6nnen durch die Auswahlboxen die Parameter der Statikstik konfigurieren");
            this.jahr_label.setVisible(true);
            this.jahr.setVisible(true);
            this.buttonParameterAnwenden.setVisible(true);
        } else if (id == 2) {
            this.hinweis.setText("HINWEIS: Sie k\u00f6nnen durch die Auswahlboxen die Parameter der Statikstik konfigurieren");
            this.mitglieder_label.setVisible(true);
            this.mitglieder.setVisible(true);
            this.buttonParameterAnwenden.setVisible(true);
        } else if (id == 3) {
            this.hinweis.setText("HINWEIS: F\u00fcr diese Statistik gibt es keine Konfigurationsparameter!");
            this.mitglieder.setVisible(false);
            this.mitglieder_label.setVisible(false);
            this.fahrzeuge_label.setVisible(false);
            this.fahrzeuge.setVisible(false);
            this.jahr.setVisible(false);
            this.jahr_label.setVisible(false);
            this.buttonParameterAnwenden.setVisible(false);
            this.veranstaltungskategorie_label.setVisible(false);
            this.veranstaltungskategorie.setVisible(false);
            this.buttonParameterAnwenden.setVisible(false);
        } else if (id == 4) {
            this.hinweis.setText("HINWEIS: Sie k\u00f6nnen durch die Auswahlboxen die Parameter der Statikstik konfigurieren");
            this.fahrzeuge_label.setVisible(true);
            this.fahrzeuge.setVisible(true);
            this.jahr_label.setVisible(true);
            this.jahr.setVisible(true);
            this.buttonParameterAnwenden.setVisible(true);
        } else if (id == 5) {
            this.hinweis.setText("HINWEIS: Sie k\u00f6nnen durch die Auswahlboxen die Parameter der Statikstik konfigurieren");
            this.veranstaltungskategorie_label.setVisible(true);
            this.veranstaltungskategorie.setVisible(true);
            this.buttonParameterAnwenden.setVisible(true);
        }
    }

    private void grundeinstellungenKonfigurationspanel() {
        this.jahr.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
        this.mitglieder.setSelectedItem("<bitte w\u00e4hlen>");
    }

    private void executeTreeSelection() {
        Steuerung.setStatus(Status.PROZESSBAR);
        Steuerung.steuerung();
        ProzessBarAO.progressbar.setStringPainted(false);
        ProzessBarAO.progressbar.setIndeterminate(true);
        ProzessBarAO.label_bitteWarten.setText("Statistik wird berechnet... Bitte haben sie einen Moment Geduld...");
        Thread thread = new Thread(){

            @Override
            public void run() {
                boolean konfigurationN\u00f6tig;
                String isSelected;
                block110: {
                    try {
                        StatistikAO.this.tabPane.remove(1);
                    }
                    catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                        // empty catch block
                    }
                    StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                    tree.setEnabled(false);
                    isSelected = tree.getLastSelectedPathComponent().toString();
                    if (!StatistikAO.this.lastSelectedTreeItem.equals(isSelected)) {
                        StatistikAO.this.grundeinstellungenKonfigurationspanel();
                    }
                    logging.logInfo((Object)("Gew\u00e4hlter Eintrag: " + tree.getLastSelectedPathComponent()));
                    int jahrParameter = Integer.parseInt(StatistikAO.this.jahr.getSelectedItem().toString());
                    konfigurationN\u00f6tig = false;
                    if (isSelected.equals("Einsatz")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AnwesenheitEinsatzAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Dienst")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AnwesenheitDienstabendAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("BSW")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AnwesenheitBrandsicherheitswachenAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Anwesenheit Gesamt")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AnwesenheitGesamtAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Beteiligungsdauer")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(0);
                        try {
                            StatistikAO.this.panelStatistik = BeteiligungsdauerAO.createPanel(new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()), jahrParameter);
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        catch (StringIndexOutOfBoundsException e1) {
                            StatistikAO.this.panelStatistik = BeteiligungsdauerAO.createPanel(0, jahrParameter);
                            konfigurationN\u00f6tig = true;
                        }
                    } else if (isSelected.equals("Abwesenheitsstatistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AbwesenheitsStatistikAO.createDemoPanel(jahrParameter);
                    } else if (isSelected.equals("Ausbildungsstatistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AusbildungsStatistikAO.createDemoPanel(jahrParameter);
                    } else if (isSelected.equals("Einsatzart")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = EinsatzArtAO.createDemoPanel(jahrParameter);
                    } else if (isSelected.equals("Durchschnittliche Einsatzdauer")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = EinsatzdauerAO.createPanel();
                    } else if (isSelected.equals("Ausr\u00fcckezeiten")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = AusrueckezeitenAO.createPanel();
                    } else if (isSelected.equals("Alarmfahrtdauer")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = DauerAlarmfahrtAO.createPanel();
                    } else if (isSelected.equals("Fahrzeuge im Einsatz")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = EinsatzFahrzeugStatistikAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Fehlalarme")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = FehlalarmeStatistikAO.createDemoPanel();
                    } else if (isSelected.equals("Einsatz pro Stunde")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = EinsatzProStundeAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Einsatz pro Monat")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = EinsatzProMonatAO.createPanel(jahrParameter);
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                    } else if (isSelected.equals("Einsatz Pro Wochentag")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = EinsatzProWochentagAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Einsatz Mannstunden")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = EinsatzMannstundenAO.createPanel();
                    } else if (isSelected.equals("Einsatz Mannstunden pro Monat")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = EinsatzMannstundenproMonatAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("BSW Mannstunden")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = BswMannstundenAO.createPanel();
                    } else if (isSelected.equals("Sonstige Mannstunden")) {
                        try {
                            StatistikAO.this.sichtbarkeitKonfigurationspanel(5);
                            int kID = new TabelleVeranstaltung_Kategorie().getID(StatistikAO.this.veranstaltungskategorie.getSelectedItem().toString());
                            if (kID == 0) {
                                konfigurationN\u00f6tig = true;
                                break block110;
                            }
                            StatistikAO.this.panelStatistik = SonstigeMannstundenAO.createPanel(kID);
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                    } else if (isSelected.equals("BSW Mannstunden pro Monat")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = BSWMannstundenProMonatAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Stadtteil Statistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = StadtteilStatistikAO.createDemoPanel(jahrParameter);
                    } else if (isSelected.equals("Anwesenheit Gesamt in %")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AnwesenheitGesamtInProzentAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Beteiligung bei...")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = VeranstaltungAnwesenheitStatistikAO.createDemoPanel(jahrParameter);
                    } else if (isSelected.equals("An- / Abwesenheitsstatistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(0);
                        try {
                            StatistikAO.this.panelStatistik = AnAbwesenheitStatistikAO.createPanel(jahrParameter, new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()));
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        catch (StringIndexOutOfBoundsException e1) {
                            StatistikAO.this.panelStatistik = AnAbwesenheitStatistikAO.createPanel(jahrParameter, 0);
                            konfigurationN\u00f6tig = true;
                        }
                    } else if (isSelected.equals("Atemschutzstatistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(0);
                        try {
                            StatistikAO.this.panelStatistik = AtemschutzstatistikAO.createPanel(new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()), jahrParameter);
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        catch (StringIndexOutOfBoundsException e1) {
                            StatistikAO.this.panelStatistik = AtemschutzstatistikAO.createPanel(0, jahrParameter);
                            konfigurationN\u00f6tig = true;
                        }
                    } else if (isSelected.equals("Durchschnittliche Einsatzbeteiligung")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = AnwesendeMitgliederProEinsatzAO.createPanel();
                    } else if (isSelected.equals("Durchschnittliche Dienstbeteiligung")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = AnwesendeMitgliederProDienstAO.createPanel();
                    } else if (isSelected.equals("Sonstige Veranstaltungen")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AnwesenheitSonstigeVeranstaltungAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Ausbilder Statistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = AusbilderStatistikAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Veranstaltungsz\u00e4hlung")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = VeranstaltungStatistikAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Schutzziel Statistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = SchutzzielStatistikAO.createPanel();
                    } else if (isSelected.equals("Durchschnittsalter")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = MitgliederDurchschnittsalterStatistikAO.createPanel();
                    } else if (isSelected.equals("Mitgliederzahlen")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = MitgliederAnzahlStatistikAO.createPanel();
                    } else if (isSelected.equals("Mitglieder Dienstgrad")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(3);
                        StatistikAO.this.panelStatistik = MitgliederDienstgradStatistikAO.createPanel();
                    } else if (isSelected.equals("Stichwort Statistik")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = StichwortStatistikAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Tag / Nacht Eins\u00e4tze")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = EinsatzTagNacht.createDemoPanel(jahrParameter);
                    } else if (isSelected.equals("Verf\u00fcgbarkeit Einsatz")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(1);
                        StatistikAO.this.panelStatistik = VerfuegbarkeitsstatistikEinsatzAO.createPanel(jahrParameter);
                    } else if (isSelected.equals("Fahrzeugbelegung")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(4);
                        try {
                            StatistikAO.this.panelStatistik = FahrzeugbelegungStatistikAO.createDemoPanel(jahrParameter, new TabelleFahrzeug().getFahrzeugID(StatistikAO.this.fahrzeuge.getSelectedItem().toString()));
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                    } else if (isSelected.equals("Mitglieder Funktionen (Anzahl)")) {
                        StatistikAO.this.sichtbarkeitKonfigurationspanel(0);
                        try {
                            StatistikAO.this.panelStatistik = MitgliederFunktionenStatistikAO.createDemoPanel(jahrParameter, new TabelleMitglied().getIdByGuiString(StatistikAO.this.mitglieder.getSelectedItem().toString()));
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        catch (StringIndexOutOfBoundsException e1) {
                            StatistikAO.this.panelStatistik = MitgliederFunktionenStatistikAO.createDemoPanel(jahrParameter, 0);
                            konfigurationN\u00f6tig = true;
                        }
                    } else {
                        StatistikAO.this.panelStatistik = new JPanel();
                    }
                }
                if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() >= 1600.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() >= 900.0) {
                    StatistikAO.this.tabPane.setPreferredSize(new Dimension(1240, 760));
                } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1440.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 900.0) {
                    StatistikAO.this.tabPane.setPreferredSize(new Dimension(1160, 760));
                } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
                    StatistikAO.this.tabPane.setPreferredSize(new Dimension(1040, 760));
                } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 768.0) {
                    StatistikAO.this.tabPane.setPreferredSize(new Dimension(1040, 630));
                } else {
                    StatistikAO.this.tabPane.setPreferredSize(new Dimension(1040, 760));
                }
                StatistikAO.this.tabPane.addTab("Statistik Parmeter     ", StatistikAO.this.panelKonfiguration);
                StatistikAO.this.tabPane.addTab("Statistik - " + isSelected, StatistikAO.this.panelStatistik);
                StatistikAO.this.add(StatistikAO.this.tabPane);
                if (!konfigurationN\u00f6tig) {
                    StatistikAO.this.tabPane.setSelectedIndex(1);
                } else {
                    StatistikAO.this.tabPane.setSelectedIndex(0);
                }
                StatistikAO.this.add(StatistikAO.this.buttonPdfExport);
                StatistikAO.this.add(StatistikAO.this.buttonJpgExport);
                StatistikAO.this.add(StatistikAO.this.buttonZuerueck);
                StatistikAO.this.repaint();
                StatistikAO.this.validate();
                tree.setEnabled(true);
                StatistikAO.this.lastSelectedTreeItem = isSelected;
                logging.logInfo((Object)"Validierung der Statistik abgeschlossen...");
                MyEvent.setEvent((String)"0x0030");
            }
        };
        thread.start();
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

