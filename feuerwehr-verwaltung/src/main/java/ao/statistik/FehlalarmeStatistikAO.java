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
 *  utilities.SbcUtils
 */
package ao.statistik;

import data.tabellen.statistik.TabelleStatistikEinsatz;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.SbcUtils;
import utilities.Utils;

public class FehlalarmeStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private JFileChooser chooser;

    public static void start() {
        FehlalarmeStatistikAO fehlalarm = new FehlalarmeStatistikAO();
        fehlalarm.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
        fehlalarm.add(buttonZurueck);
        fehlalarm.add(buttonPdfExport);
        fehlalarm.add(buttonJpgExport);
        fehlalarm.setDefaultCloseOperation(2);
        RefineryUtilities.centerFrameOnScreen((Window)fehlalarm);
        Image icon = new ImageIcon("images/icon.jpg").getImage();
        fehlalarm.setIconImage(icon);
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            fehlalarm.setAlwaysOnTop(true);
        }
        fehlalarm.setVisible(true);
    }

    public FehlalarmeStatistikAO() {
        super("FeuerwehrManagementSystem - Fehlalarmstatistik");
        this.myButtons();
        JPanel chartPanel = FehlalarmeStatistikAO.createDemoPanel();
        chartPanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(chartPanel);
    }

    public static CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        TabelleStatistikEinsatz tabStatistik = new TabelleStatistikEinsatz();
        try {
            int[] jahrListe = Utils.listToIntArray(tabStatistik.getAllJahreInDB());
            int i = 0;
            while (i < jahrListe.length) {
                int dauer = Integer.parseInt(runApplication.EINSTELLUNGEN.get("fehlalarm"));
                dataset.addValue((double)tabStatistik.getCountOfDauerKleinerWert(dauer, jahrListe[i]), (Comparable)((Object)"Fehlalarme"), (Comparable)((Object)Integer.toString(jahrListe[i])));
                dataset.addValue((double)tabStatistik.getCountOfDauerGr\u00f6\u00dferWert(dauer, jahrListe[i]), (Comparable)((Object)"Einsatz"), (Comparable)((Object)Integer.toString(jahrListe[i])));
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createStackedBarChart((String)" ", (String)"Jahr", (String)"Anzahl", (CategoryDataset)dataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        StackedBarRenderer renderer = (StackedBarRenderer)plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)new StandardCategoryItemLabelGenerator());
        return chart;
    }

    public static JPanel createDemoPanel() {
        JFreeChart chart = FehlalarmeStatistikAO.createChart(FehlalarmeStatistikAO.createDataset());
        return new ChartPanel(chart);
    }

    private void myButtons() {
        buttonZurueck = new JButton("Schlie\u00dfen");
        buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener(this)));
        buttonPdfExport = new JButton("PDF Export");
        buttonJpgExport = new JButton("JPG Export");
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FehlalarmeStatistikAO.this.chooser.setFileSelectionMode(1);
                FehlalarmeStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + FehlalarmeStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(FehlalarmeStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(FehlalarmeStatistikAO.createChart(FehlalarmeStatistikAO.createDataset()), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FehlalarmeStatistikAO.this.chooser.setFileSelectionMode(1);
                FehlalarmeStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + FehlalarmeStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(FehlalarmeStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(FehlalarmeStatistikAO.createChart(FehlalarmeStatistikAO.createDataset()), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

