package ao.statistik;

import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
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
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
import run.runApplication;
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.SbcUtils;
import utilities.Utils;

public class EinsatzArtAO extends JFrame {

   private static final long serialVersionUID = 1L;
   private static JButton buttonZurueck;
   private static JButton buttonPdfExport;
   private static JButton buttonJpgExport;
   private static JButton buttonNeuLaden;
   private static JComboBox jahresAuswahl;
   private JFileChooser chooser;


   public static void start(int jahr) {
      EinsatzArtAO einsatzArt = new EinsatzArtAO(jahr);
      einsatzArt.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
      einsatzArt.setDefaultCloseOperation(2);
      einsatzArt.add(buttonZurueck);
      einsatzArt.add(buttonPdfExport);
      einsatzArt.add(buttonJpgExport);
      einsatzArt.add(jahresAuswahl);
      einsatzArt.add(buttonNeuLaden);
      RefineryUtilities.centerFrameOnScreen(einsatzArt);
      Image icon = (new ImageIcon("images/icon.jpg")).getImage();
      einsatzArt.setIconImage(icon);
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         einsatzArt.setAlwaysOnTop(true);
      }

      einsatzArt.setVisible(true);
   }

   public EinsatzArtAO(int jahr) {
      super("FeuerwehrManagementSystem - Einsatzart");
      this.myButtons(jahr);
      this.setContentPane(createDemoPanel(jahr));
   }

   public static PieDataset createDataset(int jahr) {
      DefaultPieDataset dataset = new DefaultPieDataset();
      TabelleStatistikEinsatz statistik = new TabelleStatistikEinsatz();
      TabelleEinsatz_kategorie kategorie = new TabelleEinsatz_kategorie();

      try {
         for(int e = 0; e < kategorie.getCount(); ++e) {
            dataset.setValue(kategorie.getEinsatzKategorieName(e + 1), new Double((double)statistik.getKategorieCount(e + 1, jahr)));
         }
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

      return dataset;
   }

   public static JFreeChart createChart(PieDataset dataset) {
      JFreeChart chart = ChartFactory.createPieChart(" ", dataset, true, true, false);
      PiePlot plot = (PiePlot)chart.getPlot();
      plot.setLabelFont(new Font("SansSerif", 0, 12));
      plot.setNoDataMessage("Keine Daten Verfügbar");
      plot.setCircular(false);
      plot.setLabelGap(0.02D);
      return chart;
   }

   public static JPanel createDemoPanel(int jahr) {
      JFreeChart chart = createChart(createDataset(jahr));
      return new ChartPanel(chart);
   }

   private void myButtons(final int jahr) {
      buttonZurueck = new JButton("Schließen");
      buttonZurueck.addActionListener(new DisposeListener(this));
      buttonPdfExport = new JButton("PDF Export");
      buttonJpgExport = new JButton("JPG Export");
      buttonNeuLaden = new JButton("Neu Laden");
      TabelleJahr tabJahr = new TabelleJahr();

      try {
         String[] e = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerfügbarenJahre());
         jahresAuswahl = new JComboBox(e);
         jahresAuswahl.setSelectedItem(Integer.toString(jahr));
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

      buttonNeuLaden.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            if(EinsatzArtAO.jahresAuswahl.getSelectedItem().toString().equals("<bitte wählen>")) {
               logging.logInfo("Es wurde keine Jahr ausgewählt");
               JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
            } else {
               EinsatzArtAO.this.dispose();
               logging.logInfo("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + EinsatzArtAO.jahresAuswahl.getSelectedItem().toString());
               EinsatzArtAO.start(Integer.parseInt(EinsatzArtAO.jahresAuswahl.getSelectedItem().toString()));
            }

         }
      });
      this.chooser = new JFileChooser();
      buttonPdfExport.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinsatzArtAO.this.chooser.setFileSelectionMode(1);
            EinsatzArtAO.this.chooser.showSaveDialog((Component)null);
            logging.logInfo("Starte PDF Export in: " + EinsatzArtAO.this.chooser.getSelectedFile().getPath());
            String outputFile = EinsatzArtAO.this.chooser.getSelectedFile().getPath() + "/EinsatzArt_Stand_" + SbcUtils.timeStamp("dd.MM.yyyy") + ".pdf";
            MyChartUtils.writeChartToPDF(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 550, 500, outputFile);
            logging.logInfo("PDF Export Beendet");
            JOptionPane.showMessageDialog((Component)null, Konstante.CHART_EXPORT_ERFOLGREICH);
         }
      });
      buttonJpgExport.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EinsatzArtAO.this.chooser.setFileSelectionMode(1);
            EinsatzArtAO.this.chooser.showSaveDialog((Component)null);
            logging.logInfo("Starte JPG Export in: " + EinsatzArtAO.this.chooser.getSelectedFile().getPath());
            String outputFile = EinsatzArtAO.this.chooser.getSelectedFile().getPath() + "/EinsatzArt_Stand_" + SbcUtils.timeStamp("dd.MM.yyyy") + ".jpg";
            MyChartUtils.writeChartToJPEG(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 1000, 800, outputFile);
            logging.logInfo("JPG Export Beendet");
            JOptionPane.showMessageDialog((Component)null, Konstante.CHART_EXPORT_ERFOLGREICH);
         }
      });
   }
}
