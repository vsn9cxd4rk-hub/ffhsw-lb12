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
 *  utilities.SbcUtils
 */
package ao.statistik;

import data.tabellen.TabelleStatistikbsw;
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
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.SbcUtils;

public class BSWMannstundenProMonatAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private JFileChooser chooser;

    public static void start(int jahr) {
        BSWMannstundenProMonatAO mannstunden = new BSWMannstundenProMonatAO(jahr);
        mannstunden.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
        mannstunden.setDefaultCloseOperation(2);
        mannstunden.add(buttonZurueck);
        mannstunden.add(buttonPdfExport);
        mannstunden.add(buttonJpgExport);
        RefineryUtilities.centerFrameOnScreen((Window)mannstunden);
        Image icon = new ImageIcon("images/icon.jpg").getImage();
        mannstunden.setIconImage(icon);
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            mannstunden.setAlwaysOnTop(true);
        }
        mannstunden.setVisible(true);
    }

    public BSWMannstundenProMonatAO(int jahr) {
        super("FeuerwehrManagementSystem - Mannstunden BSW pro Monat");
        this.myButtons(jahr);
        JPanel jpanel = BSWMannstundenProMonatAO.createPanel(jahr);
        jpanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(jpanel);
    }

    public static CategoryDataset createDataset(int jahr) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        String s = "Mannstunden pro Jahr " + Integer.toString(jahr);
        TabelleStatistikbsw statistik = new TabelleStatistikbsw();
        try {
            String[] monate = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            String[] monateName = new String[]{"Januar", "Febuar", "M\u00e4rz", "April", "Mai", "Juni", "Juli", "August", "Steptember", "Oktober", "November", "Dezember"};
            int i = 0;
            while (i < monate.length) {
                defaultcategorydataset.addValue((double)statistik.getZusammengerechneteBSWMannstunden(jahr, monate[i]), (Comparable)((Object)s), (Comparable)((Object)monateName[i]));
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return defaultcategorydataset;
    }

    public static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart((String)" ", (String)"Jahr", (String)"Mannstunden pro Jahr", (CategoryDataset)categorydataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
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

    public static JPanel createPanel(int jahr) {
        JFreeChart jfreechart = BSWMannstundenProMonatAO.createChart(BSWMannstundenProMonatAO.createDataset(jahr));
        return new ChartPanel(jfreechart);
    }

    private void myButtons(final int jahr) {
        buttonZurueck = new JButton("Schlie\u00dfen");
        buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener(this)));
        buttonPdfExport = new JButton("PDF Export");
        buttonJpgExport = new JButton("JPG Export");
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BSWMannstundenProMonatAO.this.chooser.setFileSelectionMode(1);
                BSWMannstundenProMonatAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + BSWMannstundenProMonatAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(BSWMannstundenProMonatAO.this.chooser.getSelectedFile().getPath()) + "/Ausr\u00fcckezeit_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(BSWMannstundenProMonatAO.createChart(BSWMannstundenProMonatAO.createDataset(jahr)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BSWMannstundenProMonatAO.this.chooser.setFileSelectionMode(1);
                BSWMannstundenProMonatAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + BSWMannstundenProMonatAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(BSWMannstundenProMonatAO.this.chooser.getSelectedFile().getPath()) + "/Ausr\u00fcckezeit_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(BSWMannstundenProMonatAO.createChart(BSWMannstundenProMonatAO.createDataset(jahr)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

