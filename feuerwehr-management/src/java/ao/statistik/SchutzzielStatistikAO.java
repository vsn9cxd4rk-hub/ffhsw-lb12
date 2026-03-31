package ao.statistik;

import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import logging.logging;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import run.runApplication;
import utilities.Utils;

public class SchutzzielStatistikAO extends JFrame {

   private static final long serialVersionUID = 1L;


   public static void start() {
      SchutzzielStatistikAO schutzziel = new SchutzzielStatistikAO();
      schutzziel.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
      schutzziel.setDefaultCloseOperation(2);
      RefineryUtilities.centerFrameOnScreen(schutzziel);
      Image icon = (new ImageIcon("images/icon.jpg")).getImage();
      schutzziel.setIconImage(icon);
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         schutzziel.setAlwaysOnTop(true);
      }

      schutzziel.setVisible(true);
   }

   public SchutzzielStatistikAO() {
      super("SchutzzielStatistik");
      logging.logInfo("Starte: SchutzzielStatistikAO");
      JPanel jpanel = createPanel();
      jpanel.setPreferredSize(new Dimension(500, 270));
      this.setContentPane(jpanel);
   }

   public static XYDataset createDataset() {
      try {
         TabelleStatistikEinsatz e = new TabelleStatistikEinsatz();
         TabelleJahr tabJahr = new TabelleJahr();
         XYSeriesCollection dataset = new XYSeriesCollection();
         String[] jahre = Utils.listToArray(tabJahr.getAllVerfügbarenJahre());

         for(int j = 0; j < jahre.length; ++j) {
            int[] ausrückezeiten = Utils.listToIntArray(e.getAusrueckezeiten(jahre[j]));
            int[] alarmfahrten = Utils.listToIntArray(e.getAlarmfahrten(jahre[j]));
            int counter = 1;
            XYSeries series = new XYSeries(jahre[j]);

            for(int i = 0; i < ausrückezeiten.length; ++i) {
               if(ausrückezeiten[i] != 0 && alarmfahrten[i] != 0) {
                  int summe = ausrückezeiten[i] + alarmfahrten[i];
                  series.add((double)counter, (double)summe);
                  System.out.println(jahre[j] + " A:" + counter + " S:" + summe);
                  ++counter;
               }
            }

            dataset.addSeries(series);
         }

         return dataset;
      } catch (SQLException var11) {
         logging.logPrintStackTrace(var11);
         return null;
      }
   }

   public static JFreeChart createChart(XYDataset dataset) {
      JFreeChart chart = ChartFactory.createXYLineChart("Schutzzielstatistik", "Anzahl Eingetroffen (kein Bezug Auf Einsatzzahlen!)", "Zeit in Minuten", dataset, PlotOrientation.VERTICAL, true, true, false);
      chart.setBackgroundPaint(Color.white);
      XYPlot plot = chart.getXYPlot();
      plot.setBackgroundPaint(Color.lightGray);
      ValueMarker marker = new ValueMarker((double)Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("schutzziel2")));
      marker.setPaint(Color.black);
      plot.addRangeMarker(marker);
      ValueMarker marker2 = new ValueMarker((double)Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("schutzziel1")));
      marker2.setPaint(Color.black);
      plot.addRangeMarker(marker2);
      plot.setDomainGridlinePaint(Color.white);
      plot.setRangeGridlinePaint(Color.white);

      try {
         XYLineAndShapeRenderer rangeAxis = new XYLineAndShapeRenderer();
         TabelleJahr tabJahr = new TabelleJahr();
         String[] jahre = Utils.listToArray(tabJahr.getAllVerfügbarenJahre());

         for(int j = 0; j < jahre.length; ++j) {
            rangeAxis.setSeriesShapesVisible(j, true);
         }

         plot.setRenderer(rangeAxis);
      } catch (SQLException var9) {
         ;
      }

      NumberAxis var10 = (NumberAxis)plot.getRangeAxis();
      var10.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
      return chart;
   }

   public static JPanel createPanel() {
      JFreeChart jfreechart = createChart(createDataset());
      return new ChartPanel(jfreechart);
   }
}
