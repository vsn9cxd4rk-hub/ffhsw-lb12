/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
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
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleJahresbericht;
import data.tabellen.einstellungen.TabelleJahr;
import go.Jahresbericht;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.MyChartUtils;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class JahresberichtAO
extends AbstractFenster {
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
    private JComboBox<String> jahre;
    private JLabel jahre_label;
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
        logging.logInfo((Object)"Starte: JahresberichtAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern & Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.textfield = new JTextArea(23, 50);
        this.textfield.setLineWrap(true);
        this.textfield.setWrapStyleWord(true);
        this.pane = new JScrollPane(this.textfield);
        this.pane.setVerticalScrollBarPolicy(22);
        this.title = new JTextField("BERICHT VOM " + SbcUtils.timeStamp((String)"dd.MM.yyyy"), 60);
        this.title_label = new JLabel("Berichtetitel: ");
        this.jahre_label = new JLabel("Bericht Jahr: ");
        this.druckenMitDeckblatt_label = new JLabel("Druck mit Deckblatt: ");
        this.druckenMitDeckblatt = new JCheckBox();
        tree = new JTree(CreateTrees.CreateTreeJahresberichteTemplates());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
        this.modulBeschreibung = new JLabel("Jahresbericht");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        protokolle = new JCheckBox("Protokolle / T\u00e4tigkeitsberichte bei Eins\u00e4tzen hinzuf\u00fcgen");
    }

    protected void labelErstellen() {
        try {
            TabelleJahr tabJahr = new TabelleJahr();
            String[] jahresListe = Utils.listToArray(tabJahr.getAllVerf\u00fcgbarenJahre());
            this.jahre = new JComboBox<String>(jahresListe);
            this.jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.setSize(1220, 730);
        this.setTitle("FeuerwehrManagementSystem - Jahresbericht");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(3, 1));
        this.getContentPane().add("Center", this.panel);
        this.jahre_label.setPreferredSize(new Dimension(100, 25));
        this.panel.add(this.jahre_label);
        this.panel.add(this.title_label);
        this.panel.add(this.druckenMitDeckblatt_label);
        this.panel2 = new JPanel(new GridLayout(3, 1));
        this.getContentPane().add("Center", this.panel2);
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
        String[] statiktikenListe = new String[]{"Anwesenheit Einsatz", "Anwesenheit Brandsicherheitswache", "Anwesenheit Dienstabend", "Anwesenheit Gesamt", "Ausr\u00fcckezeiten", "Abwesenheitsgr\u00fcnde", "Einsatzart", "Einsatz Mannstunden", "Einsatz Mannstunden pro Monat", "BSW Mannstunden", "BSW Mannstunden pro Monat", "Einsatz Pro Monat", "Einsatz Pro Stunde", "Einsatz Pro Wochentag", "Veranstaltungsz\u00e4hlung", "Durchschnittsalter", "Mitgliederzahlen", "Schutzziel Statistik", "Stadtteil Statistik", "Mitglieder Dienstgard", "Stichwort Statistik", "Tag / Nacht Eins\u00e4tze", "Verf\u00fcgbare Mitglieder", "Fahrzeugbelegung"};
        this.jCheckboxArray = new JCheckBox[statiktikenListe.length];
        int i = 0;
        while (i < statiktikenListe.length) {
            this.jCheckboxArray[i] = new JCheckBox(statiktikenListe[i]);
            panel.add(this.jCheckboxArray[i]);
            if (i == statiktikenListe.length - 1) {
                panel.add(new JLabel());
                panel.add(protokolle);
            }
            ++i;
        }
        this.add(this.paneStatistiken);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.druckenMitDeckblatt.setSelected(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                TabelleJahresbericht tabBericht = new TabelleJahresbericht();
                try {
                    JahresberichtAO.this.title.setText(tree.getSelectionPath().getLastPathComponent().toString());
                    JahresberichtAO.this.jahre.setSelectedItem(tabBericht.getJahrOfBericht(tree.getSelectionPath().getLastPathComponent().toString()));
                    JahresberichtAO.this.textfield.setText(tabBericht.getBericht(tree.getSelectionPath().getLastPathComponent().toString()));
                    int checkBox = 0;
                    while (checkBox < JahresberichtAO.this.jCheckboxArray.length) {
                        JahresberichtAO.this.jCheckboxArray[checkBox].setSelected(false);
                        ++checkBox;
                    }
                    protokolle.setSelected(false);
                    int[] selectedStatistiken = tabBericht.getSelectedStatistiken(tree.getSelectionPath().getLastPathComponent().toString());
                    if (selectedStatistiken != null) {
                        int s = 0;
                        while (s < selectedStatistiken.length) {
                            if (selectedStatistiken[s] == -1) {
                                protokolle.setSelected(true);
                            } else {
                                JahresberichtAO.this.jCheckboxArray[selectedStatistiken[s]].setSelected(true);
                            }
                            ++s;
                        }
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Thread thread = new Thread(){

                    @Override
                    public void run() {
                        TabelleJahresbericht tabBericht = new TabelleJahresbericht();
                        Jahresbericht bericht = new Jahresbericht();
                        try {
                            int jahr = Integer.parseInt(JahresberichtAO.this.jahre.getSelectedItem().toString());
                            String pdfDateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/berichte/" + JahresberichtAO.this.title.getText() + " " + JahresberichtAO.this.jahre.getSelectedItem().toString() + ".pdf";
                            int pos = 0;
                            bericht.setId(tabBericht.getNextNummer());
                            bericht.setJahr(jahr);
                            bericht.setTitle(JahresberichtAO.this.title.getText());
                            bericht.setBericht(JahresberichtAO.this.textfield.getText());
                            bericht.setErstelldatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                            bericht.setAutoBericht(0);
                            bericht.setDateiname(String.valueOf(JahresberichtAO.this.title.getText()) + " " + JahresberichtAO.this.jahre.getSelectedItem().toString() + ".pdf");
                            ProzessBarAO.progressbar.setValue(1);
                            logging.logInfo((Object)"Bericht wird erstellt");
                            String outputfolderTemp = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/";
                            StringBuilder selectedStatistiken = new StringBuilder();
                            String[] grafiken = new String[24];
                            String[] grafikenBeschreibungen = new String[24];
                            if (JahresberichtAO.this.jCheckboxArray[0].isSelected()) {
                                MyChartUtils.writeChartToJPEG(AnwesenheitEinsatzAO.createChart(AnwesenheitEinsatzAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Einsatz.jpg");
                                grafiken[0] = String.valueOf(outputfolderTemp) + "Anwesenheit_Einsatz.jpg";
                                grafikenBeschreibungen[0] = "Anwesenheit Einsatz";
                                ProzessBarAO.progressbar.setValue(100 / JahresberichtAO.this.jCheckboxArray.length + 1);
                                selectedStatistiken.append("0,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[1].isSelected()) {
                                MyChartUtils.writeChartToJPEG(AnwesenheitBrandsicherheitswachenAO.createChart(AnwesenheitBrandsicherheitswachenAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Brandsicherheitswache.jpg");
                                grafiken[1] = String.valueOf(outputfolderTemp) + "Anwesenheit_Brandsicherheitswache.jpg";
                                grafikenBeschreibungen[1] = "Anwesenheit Brandsicherheitswache";
                                ProzessBarAO.progressbar.setValue(200 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("1,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[2].isSelected()) {
                                MyChartUtils.writeChartToJPEG(AnwesenheitDienstabendAO.createChart(AnwesenheitDienstabendAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Dienst.jpg");
                                grafiken[2] = String.valueOf(outputfolderTemp) + "Anwesenheit_Dienst.jpg";
                                grafikenBeschreibungen[2] = "Anwesenheit Dienstabend";
                                ProzessBarAO.progressbar.setValue(300 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("2,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[3].isSelected()) {
                                MyChartUtils.writeChartToJPEG(AnwesenheitGesamtAO.createChart(AnwesenheitGesamtAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Gesamt.jpg");
                                grafiken[3] = String.valueOf(outputfolderTemp) + "Anwesenheit_Gesamt.jpg";
                                grafikenBeschreibungen[3] = "Anwesenheit Gesamt";
                                ProzessBarAO.progressbar.setValue(400 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("3,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[4].isSelected()) {
                                MyChartUtils.writeChartToJPEG(AusrueckezeitenAO.createChart(AusrueckezeitenAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "Ausr\u00fcckezeit.jpg");
                                grafiken[4] = String.valueOf(outputfolderTemp) + "Ausr\u00fcckezeit.jpg";
                                grafikenBeschreibungen[4] = "Ausr\u00fcckezeiten";
                                ProzessBarAO.progressbar.setValue(500 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("4,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[5].isSelected()) {
                                MyChartUtils.writeChartToJPEG(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Abwesenheit.jpg");
                                grafiken[5] = String.valueOf(outputfolderTemp) + "Abwesenheit.jpg";
                                grafikenBeschreibungen[5] = "Abwesenheitsgr\u00fcnde";
                                ProzessBarAO.progressbar.setValue(600 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("5,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[6].isSelected()) {
                                MyChartUtils.writeChartToJPEG(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Einsatzart.jpg");
                                grafiken[6] = String.valueOf(outputfolderTemp) + "Einsatzart.jpg";
                                grafikenBeschreibungen[6] = "Einsatzart";
                                ProzessBarAO.progressbar.setValue(700 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("6,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[7].isSelected()) {
                                MyChartUtils.writeChartToJPEG(EinsatzMannstundenAO.createChart(EinsatzMannstundenAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzMannstunden.jpg");
                                grafiken[7] = String.valueOf(outputfolderTemp) + "EinsatzMannstunden.jpg";
                                grafikenBeschreibungen[7] = "Einsatz Mannstunden";
                                ProzessBarAO.progressbar.setValue(800 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("7,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[8].isSelected()) {
                                MyChartUtils.writeChartToJPEG(EinsatzMannstundenproMonatAO.createChart(EinsatzMannstundenproMonatAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzMannstundenProMonat.jpg");
                                grafiken[8] = String.valueOf(outputfolderTemp) + "EinsatzMannstundenProMonat.jpg";
                                grafikenBeschreibungen[8] = "Einsatz Mannstunden pro Monat";
                                ProzessBarAO.progressbar.setValue(900 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("8,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[9].isSelected()) {
                                MyChartUtils.writeChartToJPEG(BswMannstundenAO.createChart(BswMannstundenAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "BSWMannstunden.jpg");
                                grafiken[9] = String.valueOf(outputfolderTemp) + "BSWMannstunden.jpg";
                                grafikenBeschreibungen[9] = "BSW Mannstunden";
                                ProzessBarAO.progressbar.setValue(1000 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("9,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[10].isSelected()) {
                                MyChartUtils.writeChartToJPEG(BSWMannstundenProMonatAO.createChart(BSWMannstundenProMonatAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "BSWMannstundenProMonat.jpg");
                                grafiken[10] = String.valueOf(outputfolderTemp) + "BSWMannstundenProMonat.jpg";
                                grafikenBeschreibungen[10] = "BSW Mannstunden pro Monat";
                                ProzessBarAO.progressbar.setValue(1100 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("10,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[11].isSelected()) {
                                MyChartUtils.writeChartToJPEG(EinsatzProMonatAO.createChart(EinsatzProMonatAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzProMonat.jpg");
                                grafiken[11] = String.valueOf(outputfolderTemp) + "EinsatzProMonat.jpg";
                                grafikenBeschreibungen[11] = "Einsatz Pro Monat";
                                ProzessBarAO.progressbar.setValue(1200 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("11,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[12].isSelected()) {
                                MyChartUtils.writeChartToJPEG(EinsatzProStundeAO.createChart(EinsatzProStundeAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzProStunde.jpg");
                                grafiken[12] = String.valueOf(outputfolderTemp) + "EinsatzProStunde.jpg";
                                grafikenBeschreibungen[12] = "Einsatz Pro Stunde";
                                ProzessBarAO.progressbar.setValue(1300 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("12,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[13].isSelected()) {
                                MyChartUtils.writeChartToJPEG(EinsatzProWochentagAO.createChart(EinsatzProWochentagAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzProWochentag.jpg");
                                grafiken[13] = String.valueOf(outputfolderTemp) + "EinsatzProWochentag.jpg";
                                grafikenBeschreibungen[13] = "Einsatz Pro Wochentag";
                                ProzessBarAO.progressbar.setValue(1400 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("13,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[14].isSelected()) {
                                MyChartUtils.writeChartToJPEG(VeranstaltungStatistikAO.createChart(VeranstaltungStatistikAO.createDataset(jahr), jahr), 1000, 800, String.valueOf(outputfolderTemp) + "Veranstaltungsz\u00e4hlung.jpg");
                                grafiken[14] = String.valueOf(outputfolderTemp) + "Veranstaltungsz\u00e4hlung.jpg";
                                grafikenBeschreibungen[14] = "Veranstaltungsz\u00e4hlung";
                                ProzessBarAO.progressbar.setValue(1500 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("14,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[15].isSelected()) {
                                MyChartUtils.writeChartToJPEG(MitgliederDurchschnittsalterStatistikAO.createChart(MitgliederDurchschnittsalterStatistikAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "MitgliederDuchschnittsalter.jpg");
                                grafiken[15] = String.valueOf(outputfolderTemp) + "MitgliederDuchschnittsalter.jpg";
                                grafikenBeschreibungen[15] = "Durchschnittsalter";
                                ProzessBarAO.progressbar.setValue(1600 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("15,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[16].isSelected()) {
                                MyChartUtils.writeChartToJPEG(MitgliederAnzahlStatistikAO.createChart(MitgliederAnzahlStatistikAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "Mitgliederzahlen.jpg");
                                grafiken[16] = String.valueOf(outputfolderTemp) + "Mitgliederzahlen.jpg";
                                grafikenBeschreibungen[16] = "Mitgliederzahlen";
                                ProzessBarAO.progressbar.setValue(1700 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("16,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[17].isSelected()) {
                                MyChartUtils.writeChartToJPEG(SchutzzielStatistikAO.createChart(SchutzzielStatistikAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "Schutzziel_Statistik.jpg");
                                grafiken[17] = String.valueOf(outputfolderTemp) + "Schutzziel_Statistik.jpg";
                                grafikenBeschreibungen[17] = "Schutzziel Statistik";
                                ProzessBarAO.progressbar.setValue(1800 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("17,");
                            }
                            if (JahresberichtAO.this.jCheckboxArray[18].isSelected()) {
                                MyChartUtils.writeChartToJPEG(StadtteilStatistikAO.createChart(StadtteilStatistikAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Stadtteil_Statistik.jpg");
                                grafiken[18] = String.valueOf(outputfolderTemp) + "Stadtteil_Statistik.jpg";
                                grafikenBeschreibungen[18] = "Stadtteil Statistik";
                                ProzessBarAO.progressbar.setValue(1800 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append("18,");
                            }
                            pos = 19;
                            if (JahresberichtAO.this.jCheckboxArray[pos].isSelected()) {
                                MyChartUtils.writeChartToJPEG(MitgliederDienstgradStatistikAO.createChart(MitgliederDienstgradStatistikAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "Mitglieder_Dienstgrad.jpg");
                                grafiken[pos] = String.valueOf(outputfolderTemp) + "Mitglieder_Dienstgrad.jpg";
                                grafikenBeschreibungen[pos] = "Mitglieder Dienstgrad";
                                ProzessBarAO.progressbar.setValue(pos * 100 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append(String.valueOf(pos) + ",");
                            }
                            pos = 20;
                            if (JahresberichtAO.this.jCheckboxArray[pos].isSelected()) {
                                MyChartUtils.writeChartToJPEG(StichwortStatistikAO.createChart(StichwortStatistikAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Stichwort_Statistik.jpg");
                                grafiken[pos] = String.valueOf(outputfolderTemp) + "Stichwort_Statistik.jpg";
                                grafikenBeschreibungen[pos] = "Stichwort Statistik";
                                ProzessBarAO.progressbar.setValue(pos * 100 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append(String.valueOf(pos) + ",");
                            }
                            pos = 21;
                            if (JahresberichtAO.this.jCheckboxArray[pos].isSelected()) {
                                MyChartUtils.writeChartToJPEG(EinsatzTagNacht.createChart(EinsatzTagNacht.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Tag_Nacht_Eins\u00e4tze.jpg");
                                grafiken[pos] = String.valueOf(outputfolderTemp) + "Tag_Nacht_Eins\u00e4tze.jpg";
                                grafikenBeschreibungen[pos] = "Tag / Nacht Eins\u00e4tze";
                                ProzessBarAO.progressbar.setValue(pos * 100 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append(String.valueOf(pos) + ",");
                            }
                            pos = 22;
                            if (JahresberichtAO.this.jCheckboxArray[pos].isSelected()) {
                                MyChartUtils.writeChartToJPEG(VerfuegbarkeitsstatistikEinsatzAO.createChart(VerfuegbarkeitsstatistikEinsatzAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Verfuegbare_Mitglieder.jpg");
                                grafiken[pos] = String.valueOf(outputfolderTemp) + "Verfuegbare_Mitglieder.jpg";
                                grafikenBeschreibungen[pos] = "Verf\u00fcgbare Mitglieder";
                                ProzessBarAO.progressbar.setValue(pos * 100 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append(String.valueOf(pos) + ",");
                            }
                            pos = 23;
                            if (JahresberichtAO.this.jCheckboxArray[pos].isSelected()) {
                                MyChartUtils.writeChartToJPEG(FahrzeugbelegungStatistikAO.createChart(FahrzeugbelegungStatistikAO.createDataset(jahr, 0)), 1000, 800, String.valueOf(outputfolderTemp) + "Fahrzeugbelegung.jpg");
                                grafiken[pos] = String.valueOf(outputfolderTemp) + "Fahrzeugbelegung.jpg";
                                grafikenBeschreibungen[pos] = "Fahrzeugbelegung";
                                ProzessBarAO.progressbar.setValue(pos * 100 / JahresberichtAO.this.jCheckboxArray.length);
                                selectedStatistiken.append(String.valueOf(pos) + ",");
                            }
                            BerichtPDFSchreiben.PDFdocumentErstellen(pdfDateiname, JahresberichtAO.this.title.getText(), JahresberichtAO.this.textfield.getText(), Integer.toString(jahr), grafiken, grafikenBeschreibungen, JahresberichtAO.this.druckenMitDeckblatt.isSelected(), protokolle.isSelected());
                            if (protokolle.isSelected()) {
                                selectedStatistiken.append("-1");
                            }
                            if (selectedStatistiken.length() == 0) {
                                selectedStatistiken.append("leer");
                            }
                            bericht.setStatistiken(selectedStatistiken.toString());
                            tabBericht.insert(bericht);
                            ProzessBarAO.progressbar.setValue(100);
                            MyEvent.setEvent((String)"0x0030");
                            Utils.dateiKatalogisieren(pdfDateiname);
                            Desktop.getDesktop().open(new File(pdfDateiname));
                            logbuchEingabe.NeuerEintag("Bericht wurde erstellt Dateinmae: " + pdfDateiname);
                            JahresberichtAO.this.dispose();
                        }
                        catch (DocumentException | IOException | SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
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

