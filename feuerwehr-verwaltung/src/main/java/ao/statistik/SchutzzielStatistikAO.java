/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  org.jfree.chart.ChartFactory
 *  org.jfree.chart.ChartPanel
 *  org.jfree.chart.JFreeChart
 *  org.jfree.chart.axis.NumberAxis
 *  org.jfree.chart.plot.Marker
 *  org.jfree.chart.plot.PlotOrientation
 *  org.jfree.chart.plot.ValueMarker
 *  org.jfree.chart.plot.XYPlot
 *  org.jfree.chart.renderer.xy.XYItemRenderer
 *  org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
 *  org.jfree.data.xy.XYDataset
 *  org.jfree.data.xy.XYSeries
 *  org.jfree.data.xy.XYSeriesCollection
 *  org.jfree.ui.RefineryUtilities
 */
package ao.statistik;

import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Window;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import logging.logging;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import run.runApplication;
import utilities.Utils;

public class SchutzzielStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;

    public static void start() {
        SchutzzielStatistikAO schutzziel = new SchutzzielStatistikAO();
        schutzziel.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
        schutzziel.setDefaultCloseOperation(2);
        RefineryUtilities.centerFrameOnScreen((Window)schutzziel);
        Image icon = new ImageIcon("images/icon.jpg").getImage();
        schutzziel.setIconImage(icon);
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            schutzziel.setAlwaysOnTop(true);
        }
        schutzziel.setVisible(true);
    }

    public SchutzzielStatistikAO() {
        super("SchutzzielStatistik");
        logging.logInfo((Object)"Starte: SchutzzielStatistikAO");
        JPanel jpanel = SchutzzielStatistikAO.createPanel();
        jpanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(jpanel);
    }

    public static XYDataset createDataset() {
        try {
            TabelleStatistikEinsatz tabSta = new TabelleStatistikEinsatz();
            TabelleJahr tabJahr = new TabelleJahr();
            XYSeriesCollection dataset = new XYSeriesCollection();
            String[] jahre = Utils.listToArray(tabJahr.getAllVerf\u00fcgbarenJahre());
            int j = 0;
            while (j < jahre.length) {
                int[] ausr\u00fcckezeiten = Utils.listToIntArray(tabSta.getAusrueckezeiten(jahre[j]));
                int[] alarmfahrten = Utils.listToIntArray(tabSta.getAlarmfahrten(jahre[j]));
                int counter = 1;
                XYSeries series = new XYSeries((Comparable)((Object)jahre[j]));
                int i = 0;
                while (i < ausr\u00fcckezeiten.length) {
                    if (ausr\u00fcckezeiten[i] != 0 && alarmfahrten[i] != 0) {
                        int summe = ausr\u00fcckezeiten[i] + alarmfahrten[i];
                        series.add((double)counter, (double)summe);
                        System.out.println(String.valueOf(jahre[j]) + " A:" + counter + " S:" + summe);
                        ++counter;
                    }
                    ++i;
                }
                dataset.addSeries(series);
                ++j;
            }
            return dataset;
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
            return null;
        }
    }

    public static JFreeChart createChart(XYDataset dataset) {
        JFreeChart chart = ChartFactory.createXYLineChart((String)"Schutzzielstatistik", (String)"Anzahl Eingetroffen (kein Bezug Auf Einsatzzahlen!)", (String)"Zeit in Minuten", (XYDataset)dataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
        chart.setBackgroundPaint((Paint)Color.white);
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint((Paint)Color.lightGray);
        ValueMarker marker = new ValueMarker((double)Integer.parseInt(runApplication.EINSTELLUNGEN.get("schutzziel2")));
        marker.setPaint((Paint)Color.black);
        plot.addRangeMarker((Marker)marker);
        ValueMarker marker2 = new ValueMarker((double)Integer.parseInt(runApplication.EINSTELLUNGEN.get("schutzziel1")));
        marker2.setPaint((Paint)Color.black);
        plot.addRangeMarker((Marker)marker2);
        plot.setDomainGridlinePaint((Paint)Color.white);
        plot.setRangeGridlinePaint((Paint)Color.white);
        try {
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            TabelleJahr tabJahr = new TabelleJahr();
            String[] jahre = Utils.listToArray(tabJahr.getAllVerf\u00fcgbarenJahre());
            int j = 0;
            while (j < jahre.length) {
                renderer.setSeriesShapesVisible(j, true);
                ++j;
            }
            plot.setRenderer((XYItemRenderer)renderer);
        }
        catch (SQLException renderer) {
            // empty catch block
        }
        NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
    }

    public static JPanel createPanel() {
        JFreeChart jfreechart = SchutzzielStatistikAO.createChart(SchutzzielStatistikAO.createDataset());
        return new ChartPanel(jfreechart);
    }
}

