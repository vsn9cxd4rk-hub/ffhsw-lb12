/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  org.jfree.chart.ChartFactory
 *  org.jfree.chart.ChartPanel
 *  org.jfree.chart.JFreeChart
 *  org.jfree.chart.labels.CategoryItemLabelGenerator
 *  org.jfree.chart.labels.StandardCategoryItemLabelGenerator
 *  org.jfree.chart.plot.CategoryPlot
 *  org.jfree.chart.plot.PlotOrientation
 *  org.jfree.chart.renderer.category.StackedBarRenderer
 *  org.jfree.data.category.CategoryDataset
 *  org.jfree.data.category.DefaultCategoryDataset
 *  org.jfree.ui.RefineryUtilities
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.statistik;

import ao.utils.ProzessBarAO;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeugeinteilung;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class FahrzeugbelegungStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private static JButton buttonNeuLaden;
    private static JComboBox<String> jahresAuswahl;
    private static JComboBox<String> fahrzeugAuswahl;
    private JFileChooser chooser;

    public static void start(final int jahr, final int fahrzeugID) {
        Steuerung.setStatus(Status.PROZESSBAR);
        Steuerung.steuerung();
        ProzessBarAO.progressbar.setStringPainted(false);
        ProzessBarAO.progressbar.setIndeterminate(true);
        ProzessBarAO.label_bitteWarten.setText("Statistik wird berechnet... Bitte haben sie einen Moment Geduld...");
        Thread threadStatistik = new Thread(){

            @Override
            public void run() {
                FahrzeugbelegungStatistikAO fahrzeugbelegung = new FahrzeugbelegungStatistikAO(jahr, fahrzeugID);
                fahrzeugbelegung.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
                fahrzeugbelegung.add(buttonZurueck);
                fahrzeugbelegung.add(buttonPdfExport);
                fahrzeugbelegung.add(buttonJpgExport);
                fahrzeugbelegung.add(jahresAuswahl);
                fahrzeugbelegung.add(fahrzeugAuswahl);
                fahrzeugbelegung.add(buttonNeuLaden);
                fahrzeugbelegung.setDefaultCloseOperation(2);
                RefineryUtilities.centerFrameOnScreen((Window)fahrzeugbelegung);
                Image icon = new ImageIcon("images/icon.jpg").getImage();
                fahrzeugbelegung.setIconImage(icon);
                if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
                    fahrzeugbelegung.setAlwaysOnTop(true);
                }
                fahrzeugbelegung.setVisible(true);
                MyEvent.setEvent((String)"0x0030");
            }
        };
        threadStatistik.start();
    }

    public FahrzeugbelegungStatistikAO(int jahr, int fahrzeugID) {
        super("FeuerwehrManagementSystem - Fahrzeugbelegungsstatistik");
        this.myButtons(jahr, fahrzeugID);
        JPanel chartPanel = FahrzeugbelegungStatistikAO.createDemoPanel(jahr, fahrzeugID);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(chartPanel);
    }

    public static CategoryDataset createDataset(int jahr, int fahrzeugID) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleFahrzeugeinteilung tabEinteilung = new TabelleFahrzeugeinteilung();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        try {
            String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
            int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
            int fahrzeugKategorie = tabFahrzeug.getBeschreibungID(fahrzeugID);
            int currentFahrzeugIsTrupp = tabFahrzeug.getTrupp(fahrzeugID);
            String[] positionName = new String[9];
            if (fahrzeugKategorie == 13 || fahrzeugKategorie == 14) {
                positionName[0] = "Transportf\u00fchrer";
                positionName[1] = "Fahrzeugf\u00fchrer";
                positionName[2] = "Sitzplatz 1";
                positionName[3] = "Sitzplatz 2";
                positionName[4] = "";
                positionName[5] = "";
                positionName[6] = "";
                positionName[7] = "";
                positionName[8] = "";
            } else if (fahrzeugKategorie == 4 || fahrzeugKategorie == 5) {
                positionName[0] = "";
                positionName[1] = "Maschinist";
                positionName[2] = "Leiterf\u00fchrer";
                positionName[3] = "Truppmann";
                positionName[4] = "";
                positionName[5] = "";
                positionName[6] = "";
                positionName[7] = "";
                positionName[8] = "";
            } else if (fahrzeugKategorie == 12) {
                positionName[0] = "Zugf\u00fchrer";
                positionName[1] = "Fahrer";
                positionName[2] = "Sitzplatz 1";
                positionName[3] = "Sitzplatz 2";
                positionName[4] = "Sitzplatz 3";
                positionName[5] = "Sitzplatz 4";
                positionName[6] = "";
                positionName[7] = "";
                positionName[8] = "";
            } else if (fahrzeugKategorie == 6 | fahrzeugKategorie == 9 | fahrzeugKategorie == 7) {
                if (currentFahrzeugIsTrupp == 0) {
                    positionName[0] = "Fahrzeugf\u00fchrer";
                    positionName[1] = "Fahrer / Maschinist";
                    positionName[2] = "Sitzplatz 1";
                    positionName[3] = "Sitzplatz 2";
                    positionName[4] = "Sitzplatz 3";
                    positionName[5] = "Sitzplatz 4";
                    positionName[6] = "Sitzplatz 5";
                    positionName[7] = "Sitzplatz 6";
                    positionName[8] = "Sitzplatz 7";
                } else {
                    positionName[0] = "";
                    positionName[1] = "Fahrer / Maschinist";
                    positionName[2] = "Truppf\u00fchrer";
                    positionName[3] = "Truppmann";
                    positionName[4] = "";
                    positionName[5] = "";
                    positionName[6] = "";
                    positionName[7] = "";
                    positionName[8] = "";
                }
            } else if (currentFahrzeugIsTrupp == 0) {
                logging.logInfo((Object)"Erstelle Fahrzeug mit Gruppen- oder Staffelbesatzung...");
                positionName[0] = "Gruppenf\u00fchrer";
                positionName[1] = "Maschinist";
                positionName[2] = "Angriffstruppf\u00fchrer";
                positionName[3] = "Angriffstruppmann";
                positionName[4] = "Wassertruppf\u00fchrer";
                positionName[5] = "Wassertruppmann";
                positionName[6] = "Schlauchtruppf\u00fchrer";
                positionName[7] = "Schlauchtruppmann";
                positionName[8] = "Melder";
            } else {
                logging.logInfo((Object)"Erstelle Fahrzeug mit Truppbesatzung...");
                positionName[0] = "";
                positionName[1] = "Maschinist";
                positionName[2] = "Truppf\u00fchrer";
                positionName[3] = "Truppmann";
                positionName[4] = "";
                positionName[5] = "";
                positionName[6] = "";
                positionName[7] = "";
                positionName[8] = "";
            }
            int i = 0;
            while (i < mitgliederListe.length) {
                int p = 0;
                while (p < positionName.length) {
                    dataset.addValue((double)tabEinteilung.getCountOfPosition(mitgliederIDListe[i], p, jahr, fahrzeugID), (Comparable)((Object)positionName[p]), (Comparable)((Object)mitgliederListe[i]));
                    ++p;
                }
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createStackedBarChart((String)" ", (String)"Name", (String)"Anzahl", (CategoryDataset)dataset, (PlotOrientation)PlotOrientation.HORIZONTAL, (boolean)true, (boolean)true, (boolean)false);
        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        StackedBarRenderer renderer = (StackedBarRenderer)plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)new StandardCategoryItemLabelGenerator());
        return chart;
    }

    public static JPanel createDemoPanel(int jahr, int fahrzeugID) {
        JFreeChart chart = FahrzeugbelegungStatistikAO.createChart(FahrzeugbelegungStatistikAO.createDataset(jahr, fahrzeugID));
        return new ChartPanel(chart);
    }

    private void myButtons(final int jahr, final int fahrzeugID) {
        buttonZurueck = new JButton("Schlie\u00dfen");
        buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener(this)));
        buttonPdfExport = new JButton("PDF Export");
        buttonJpgExport = new JButton("JPG Export");
        buttonNeuLaden = new JButton("Neu Laden");
        TabelleJahr tabJahr = new TabelleJahr();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        try {
            String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            String[] fahrzeugListe = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
            jahresAuswahl = new JComboBox<String>(jahresListe);
            jahresAuswahl.setSelectedItem(Integer.toString(jahr));
            fahrzeugAuswahl = new JComboBox<String>(fahrzeugListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        buttonNeuLaden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (jahresAuswahl.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    logging.logInfo((Object)"Es wurde keine Jahr ausgew\u00e4hlt");
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
                } else {
                    FahrzeugbelegungStatistikAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    try {
                        FahrzeugbelegungStatistikAO.start(Integer.parseInt(jahresAuswahl.getSelectedItem().toString()), new TabelleFahrzeug().getFahrzeugID(fahrzeugAuswahl.getSelectedItem().toString()));
                    }
                    catch (NumberFormatException | SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FahrzeugbelegungStatistikAO.this.chooser.setFileSelectionMode(1);
                FahrzeugbelegungStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + FahrzeugbelegungStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(FahrzeugbelegungStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(FahrzeugbelegungStatistikAO.createChart(FahrzeugbelegungStatistikAO.createDataset(jahr, fahrzeugID)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FahrzeugbelegungStatistikAO.this.chooser.setFileSelectionMode(1);
                FahrzeugbelegungStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + FahrzeugbelegungStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(FahrzeugbelegungStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(FahrzeugbelegungStatistikAO.createChart(FahrzeugbelegungStatistikAO.createDataset(jahr, fahrzeugID)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

