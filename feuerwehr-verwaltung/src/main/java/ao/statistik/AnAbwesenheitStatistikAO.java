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

import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
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

public class AnAbwesenheitStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private JFileChooser chooser;

    public static void start(int jahr, int mitgliederID) {
        AnAbwesenheitStatistikAO anAbwesenheit = new AnAbwesenheitStatistikAO(jahr, mitgliederID);
        anAbwesenheit.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
        anAbwesenheit.add(buttonZurueck);
        anAbwesenheit.add(buttonPdfExport);
        anAbwesenheit.add(buttonJpgExport);
        anAbwesenheit.setDefaultCloseOperation(2);
        RefineryUtilities.centerFrameOnScreen((Window)anAbwesenheit);
        Image icon = new ImageIcon("images/icon.jpg").getImage();
        anAbwesenheit.setIconImage(icon);
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            anAbwesenheit.setAlwaysOnTop(true);
        }
        anAbwesenheit.setVisible(true);
    }

    public AnAbwesenheitStatistikAO(int jahr, int mitgliederID) {
        super("FeuerwehrManagementSystem - Fehlalarmstatistik");
        this.myButtons(jahr, mitgliederID);
        JPanel chartPanel = AnAbwesenheitStatistikAO.createPanel(jahr, mitgliederID);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(chartPanel);
    }

    public static CategoryDataset createDataset(int jahr, int mitgliederID) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
        try {
            String[] kategorieListe = Utils.listToArray(tabKategorie.getAllKategorien());
            int i = 0;
            while (i < kategorieListe.length) {
                int kID = tabKategorie.getID(kategorieListe[i]);
                int vCount = tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(Integer.toString(jahr), kID);
                int anwesendCount = tabAnwesenheit.getBeteiligungByKategorie(mitgliederID, kID, jahr);
                dataset.addValue((double)anwesendCount, (Comparable)((Object)"Anwesende"), (Comparable)((Object)kategorieListe[i]));
                dataset.addValue((double)(vCount - anwesendCount), (Comparable)((Object)"Abwesende"), (Comparable)((Object)kategorieListe[i]));
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createStackedBarChart((String)" ", (String)"Veranstaltungskategorie", (String)"Anzahl", (CategoryDataset)dataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        StackedBarRenderer renderer = (StackedBarRenderer)plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)new StandardCategoryItemLabelGenerator());
        return chart;
    }

    public static JPanel createPanel(int jahr, int mitgliederID) {
        JFreeChart chart = AnAbwesenheitStatistikAO.createChart(AnAbwesenheitStatistikAO.createDataset(jahr, mitgliederID));
        return new ChartPanel(chart);
    }

    private void myButtons(final int jahr, final int mitgliederID) {
        buttonZurueck = new JButton("Schlie\u00dfen");
        buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener(this)));
        buttonPdfExport = new JButton("PDF Export");
        buttonJpgExport = new JButton("JPG Export");
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnAbwesenheitStatistikAO.this.chooser.setFileSelectionMode(1);
                AnAbwesenheitStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + AnAbwesenheitStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AnAbwesenheitStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(AnAbwesenheitStatistikAO.createChart(AnAbwesenheitStatistikAO.createDataset(jahr, mitgliederID)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnAbwesenheitStatistikAO.this.chooser.setFileSelectionMode(1);
                AnAbwesenheitStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + AnAbwesenheitStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AnAbwesenheitStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(AnAbwesenheitStatistikAO.createChart(AnAbwesenheitStatistikAO.createDataset(jahr, mitgliederID)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

