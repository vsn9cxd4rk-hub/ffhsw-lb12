/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  org.jfree.chart.ChartFactory
 *  org.jfree.chart.ChartPanel
 *  org.jfree.chart.JFreeChart
 *  org.jfree.chart.plot.PiePlot
 *  org.jfree.data.general.DefaultPieDataset
 *  org.jfree.data.general.PieDataset
 *  org.jfree.ui.RefineryUtilities
 *  utilities.SbcUtils
 */
package ao.statistik;

import data.tabellen.TabelleEinsatz;
import data.tabellen.einstellungen.TabelleJahr;
import java.awt.Font;
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
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
import run.runApplication;
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.SbcUtils;
import utilities.Utils;

public class StadtteilStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private static JButton buttonNeuLaden;
    private static JComboBox<String> jahresAuswahl;
    private JFileChooser chooser;

    public static void start(int jahr) {
        StadtteilStatistikAO stadtteil = new StadtteilStatistikAO(jahr);
        stadtteil.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
        stadtteil.setDefaultCloseOperation(2);
        stadtteil.add(buttonZurueck);
        stadtteil.add(buttonPdfExport);
        stadtteil.add(buttonJpgExport);
        stadtteil.add(jahresAuswahl);
        stadtteil.add(buttonNeuLaden);
        RefineryUtilities.centerFrameOnScreen((Window)stadtteil);
        Image icon = new ImageIcon("images/icon.jpg").getImage();
        stadtteil.setIconImage(icon);
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            stadtteil.setAlwaysOnTop(true);
        }
        stadtteil.setVisible(true);
    }

    public StadtteilStatistikAO(int jahr) {
        super("FeuerwehrManagementSystem - Einsatzart");
        this.myButtons(jahr);
        this.setContentPane(StadtteilStatistikAO.createDemoPanel(jahr));
    }

    public static PieDataset createDataset(int jahr) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        try {
            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
            String[] stadtteileListe = Utils.listToArray(tabEinsatz.getStadtteilListe());
            int i = 0;
            while (i < stadtteileListe.length) {
                if (stadtteileListe[i].equals("") | stadtteileListe[i].equals(" ")) {
                    dataset.setValue((Comparable)((Object)"keine Angaben"), (Number)new Double(tabEinsatz.getCountOfStadtteil(jahr, stadtteileListe[i])));
                } else {
                    dataset.setValue((Comparable)((Object)stadtteileListe[i]), (Number)new Double(tabEinsatz.getCountOfStadtteil(jahr, stadtteileListe[i])));
                }
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return dataset;
    }

    public static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart((String)" ", (PieDataset)dataset, (boolean)true, (boolean)true, (boolean)false);
        PiePlot plot = (PiePlot)chart.getPlot();
        plot.setLabelFont(new Font("SansSerif", 0, 12));
        plot.setNoDataMessage("Keine Daten Verf\u00fcgbar");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        return chart;
    }

    public static JPanel createDemoPanel(int jahr) {
        JFreeChart chart = StadtteilStatistikAO.createChart(StadtteilStatistikAO.createDataset(jahr));
        return new ChartPanel(chart);
    }

    private void myButtons(final int jahr) {
        buttonZurueck = new JButton("Schlie\u00dfen");
        buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener(this)));
        buttonPdfExport = new JButton("PDF Export");
        buttonJpgExport = new JButton("JPG Export");
        buttonNeuLaden = new JButton("Neu Laden");
        TabelleJahr tabJahr = new TabelleJahr();
        try {
            String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            jahresAuswahl = new JComboBox<String>(jahresListe);
            jahresAuswahl.setSelectedItem(Integer.toString(jahr));
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
                    StadtteilStatistikAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    StadtteilStatistikAO.start(Integer.parseInt(jahresAuswahl.getSelectedItem().toString()));
                }
            }
        });
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                StadtteilStatistikAO.this.chooser.setFileSelectionMode(1);
                StadtteilStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + StadtteilStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(StadtteilStatistikAO.this.chooser.getSelectedFile().getPath()) + "/EinsatzArt_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(StadtteilStatistikAO.createChart(StadtteilStatistikAO.createDataset(jahr)), 550, 500, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                StadtteilStatistikAO.this.chooser.setFileSelectionMode(1);
                StadtteilStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + StadtteilStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(StadtteilStatistikAO.this.chooser.getSelectedFile().getPath()) + "/EinsatzArt_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(StadtteilStatistikAO.createChart(StadtteilStatistikAO.createDataset(jahr)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

