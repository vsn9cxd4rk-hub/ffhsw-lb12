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
import data.tabellen.statistik.TabelleStatistikMitglieder;
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
import utilities.TimeCalculation;
import utilities.Utils;

public class MitgliederAnzahlStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private static JComboBox<String> jahresAuswahl;
    private JFileChooser chooser;

    public static void start() {
        Steuerung.setStatus(Status.PROZESSBAR);
        Steuerung.steuerung();
        ProzessBarAO.progressbar.setStringPainted(false);
        ProzessBarAO.progressbar.setIndeterminate(true);
        ProzessBarAO.label_bitteWarten.setText("Statistik wird berechnet... Bitte haben sie einen Moment Geduld...");
        Thread threadStatistik = new Thread(){

            @Override
            public void run() {
                MitgliederAnzahlStatistikAO anzahl = new MitgliederAnzahlStatistikAO();
                anzahl.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
                anzahl.setDefaultCloseOperation(2);
                anzahl.add(buttonZurueck);
                anzahl.add(buttonPdfExport);
                anzahl.add(buttonJpgExport);
                anzahl.add(jahresAuswahl);
                RefineryUtilities.centerFrameOnScreen((Window)anzahl);
                Image icon = new ImageIcon("images/icon.jpg").getImage();
                anzahl.setIconImage(icon);
                if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
                    anzahl.setAlwaysOnTop(true);
                }
                anzahl.setVisible(true);
                MyEvent.setEvent((String)"0x0030");
            }
        };
        threadStatistik.start();
    }

    public MitgliederAnzahlStatistikAO() {
        super("FeuerwehrManagementSystem - Mitgliederanzahl");
        this.myButtons();
        JPanel jpanel = MitgliederAnzahlStatistikAO.createPanel();
        jpanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(jpanel);
    }

    public static CategoryDataset createDataset() {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        String s = "Mitgliederanzahl";
        TabelleStatistikMitglieder tabStatistikMitglieder = new TabelleStatistikMitglieder();
        try {
            int[] jahre = Utils.listToIntArray(tabStatistikMitglieder.getAllJahreInDB());
            int[] anzahl = Utils.listToIntArray(tabStatistikMitglieder.getAnzahl());
            String[] erstellung = Utils.listToArray(tabStatistikMitglieder.getErstellung());
            int i = 0;
            while (i < jahre.length) {
                logging.logInfo((Object)anzahl[i]);
                defaultcategorydataset.addValue((double)anzahl[i], (Comparable)((Object)s), (Comparable)((Object)(String.valueOf(Integer.toString(jahre[i])) + " (Erstellt: " + TimeCalculation.parseDateForGUI(erstellung[i]) + ")")));
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return defaultcategorydataset;
    }

    public static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart((String)" ", (String)"Jahre", (String)"Mitgliederanzahl", (CategoryDataset)categorydataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
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

    public static JPanel createPanel() {
        JFreeChart jfreechart = MitgliederAnzahlStatistikAO.createChart(MitgliederAnzahlStatistikAO.createDataset());
        return new ChartPanel(jfreechart);
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
                MitgliederAnzahlStatistikAO.this.chooser.setFileSelectionMode(1);
                MitgliederAnzahlStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + MitgliederAnzahlStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(MitgliederAnzahlStatistikAO.this.chooser.getSelectedFile().getPath()) + "/MitgliederAnzahlPDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(MitgliederAnzahlStatistikAO.createChart(MitgliederAnzahlStatistikAO.createDataset()), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MitgliederAnzahlStatistikAO.this.chooser.setFileSelectionMode(1);
                MitgliederAnzahlStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + MitgliederAnzahlStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(MitgliederAnzahlStatistikAO.this.chooser.getSelectedFile().getPath()) + "/MitgliederAnzahlJPG_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(MitgliederAnzahlStatistikAO.createChart(MitgliederAnzahlStatistikAO.createDataset()), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

