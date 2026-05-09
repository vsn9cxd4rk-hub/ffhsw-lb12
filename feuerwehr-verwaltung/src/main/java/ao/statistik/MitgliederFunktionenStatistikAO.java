/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  org.jfree.chart.ChartFactory
 *  org.jfree.chart.ChartPanel
 *  org.jfree.chart.JFreeChart
 *  org.jfree.chart.axis.AxisLocation
 *  org.jfree.chart.axis.CategoryAxis
 *  org.jfree.chart.axis.NumberAxis
 *  org.jfree.chart.labels.CategoryItemLabelGenerator
 *  org.jfree.chart.labels.StandardCategoryItemLabelGenerator
 *  org.jfree.chart.plot.CategoryPlot
 *  org.jfree.chart.plot.PlotOrientation
 *  org.jfree.chart.renderer.category.BarRenderer
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Paint;
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
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
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

public class MitgliederFunktionenStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private static JButton buttonNeuLaden;
    private static JComboBox<String> jahresAuswahl;
    private static JComboBox<String> fahrzeugAuswahl;
    private JFileChooser chooser;

    public static void start(final int jahr, final int mitgliederID) {
        Steuerung.setStatus(Status.PROZESSBAR);
        Steuerung.steuerung();
        ProzessBarAO.progressbar.setStringPainted(false);
        ProzessBarAO.progressbar.setIndeterminate(true);
        ProzessBarAO.label_bitteWarten.setText("Statistik wird berechnet... Bitte haben sie einen Moment Geduld...");
        Thread threadStatistik = new Thread(){

            @Override
            public void run() {
                MitgliederFunktionenStatistikAO mitgliederFunktionen = new MitgliederFunktionenStatistikAO(jahr, mitgliederID);
                mitgliederFunktionen.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
                mitgliederFunktionen.add(buttonZurueck);
                mitgliederFunktionen.add(buttonPdfExport);
                mitgliederFunktionen.add(buttonJpgExport);
                mitgliederFunktionen.add(jahresAuswahl);
                mitgliederFunktionen.add(fahrzeugAuswahl);
                mitgliederFunktionen.add(buttonNeuLaden);
                mitgliederFunktionen.setDefaultCloseOperation(2);
                RefineryUtilities.centerFrameOnScreen((Window)mitgliederFunktionen);
                Image icon = new ImageIcon("images/icon.jpg").getImage();
                mitgliederFunktionen.setIconImage(icon);
                if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
                    mitgliederFunktionen.setAlwaysOnTop(true);
                }
                mitgliederFunktionen.setVisible(true);
                MyEvent.setEvent((String)"0x0030");
            }
        };
        threadStatistik.start();
    }

    public MitgliederFunktionenStatistikAO(int jahr, int mitgliederID) {
        super("FeuerwehrManagementSystem - Fahrzeugbelegungsstatistik");
        this.myButtons(jahr, mitgliederID);
        JPanel chartPanel = MitgliederFunktionenStatistikAO.createDemoPanel(jahr, mitgliederID);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(chartPanel);
    }

    public static CategoryDataset createDataset(int jahr, int mitgliederID) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleFahrzeugeinteilung tabEinteilung = new TabelleFahrzeugeinteilung();
        try {
            String[] positionName = new String[]{"Gruppenf\u00fchrer", "Maschinist", "Angriffstruppf\u00fchrer", "Angriffstruppmann", "Wassertruppf\u00fchrer", "Wassertruppmann", "Schlauchtruppf\u00fchrer", "Schlauchtruppmann", "Melder"};
            int p = 0;
            while (p < positionName.length) {
                dataset.addValue((double)tabEinteilung.getCountOfPosition(mitgliederID, p, jahr, 0), (Comparable)((Object)positionName[p]), (Comparable)((Object)(String.valueOf(tabMitglieder.getName(mitgliederID)) + ", " + tabMitglieder.getVorname(mitgliederID))));
                ++p;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart((String)" ", (String)" ", (String)"Mitglieder Funktionen (Anzahl)", (CategoryDataset)dataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
        jfreechart.setBackgroundPaint((Paint)Color.white);
        CategoryPlot categoryplot = (CategoryPlot)jfreechart.getPlot();
        categoryplot.setBackgroundPaint((Paint)Color.lightGray);
        categoryplot.setRangeGridlinePaint((Paint)Color.white);
        categoryplot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
        BarRenderer barrenderer = (BarRenderer)categoryplot.getRenderer();
        barrenderer.setBaseItemLabelsVisible(true);
        barrenderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)new StandardCategoryItemLabelGenerator());
        CategoryAxis categoryaxis = categoryplot.getDomainAxis();
        categoryaxis.setCategoryMargin(0.0);
        categoryaxis.setUpperMargin(0.02);
        categoryaxis.setLowerMargin(0.02);
        NumberAxis numberaxis = (NumberAxis)categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setUpperMargin(0.1);
        return jfreechart;
    }

    public static JPanel createDemoPanel(int jahr, int mitgliederID) {
        JFreeChart chart = MitgliederFunktionenStatistikAO.createChart(MitgliederFunktionenStatistikAO.createDataset(jahr, mitgliederID));
        return new ChartPanel(chart);
    }

    private void myButtons(final int jahr, final int mitgliederID) {
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
                    MitgliederFunktionenStatistikAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    try {
                        MitgliederFunktionenStatistikAO.start(Integer.parseInt(jahresAuswahl.getSelectedItem().toString()), new TabelleFahrzeug().getFahrzeugID(fahrzeugAuswahl.getSelectedItem().toString()));
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
                MitgliederFunktionenStatistikAO.this.chooser.setFileSelectionMode(1);
                MitgliederFunktionenStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + MitgliederFunktionenStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(MitgliederFunktionenStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(MitgliederFunktionenStatistikAO.createChart(MitgliederFunktionenStatistikAO.createDataset(jahr, mitgliederID)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MitgliederFunktionenStatistikAO.this.chooser.setFileSelectionMode(1);
                MitgliederFunktionenStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + MitgliederFunktionenStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(MitgliederFunktionenStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(MitgliederFunktionenStatistikAO.createChart(MitgliederFunktionenStatistikAO.createDataset(jahr, mitgliederID)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

