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

import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.statistik.TabelleStatistikEinsatz;
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

public class EinsatzArtAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private static JButton buttonNeuLaden;
    private static JComboBox<String> jahresAuswahl;
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
        RefineryUtilities.centerFrameOnScreen((Window)einsatzArt);
        Image icon = new ImageIcon("images/icon.jpg").getImage();
        einsatzArt.setIconImage(icon);
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            einsatzArt.setAlwaysOnTop(true);
        }
        einsatzArt.setVisible(true);
    }

    public EinsatzArtAO(int jahr) {
        super("FeuerwehrManagementSystem - Einsatzart");
        this.myButtons(jahr);
        this.setContentPane(EinsatzArtAO.createDemoPanel(jahr));
    }

    public static PieDataset createDataset(int jahr) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        TabelleStatistikEinsatz statistik = new TabelleStatistikEinsatz();
        TabelleEinsatz_kategorie kategorie = new TabelleEinsatz_kategorie();
        try {
            int i = 0;
            while (i < kategorie.getCount()) {
                dataset.setValue((Comparable)((Object)kategorie.getEinsatzKategorieName(i + 1)), (Number)new Double(statistik.getKategorieCount(i + 1, jahr)));
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
        JFreeChart chart = EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr));
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
                    EinsatzArtAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    EinsatzArtAO.start(Integer.parseInt(jahresAuswahl.getSelectedItem().toString()));
                }
            }
        });
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzArtAO.this.chooser.setFileSelectionMode(1);
                EinsatzArtAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + EinsatzArtAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(EinsatzArtAO.this.chooser.getSelectedFile().getPath()) + "/EinsatzArt_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 550, 500, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzArtAO.this.chooser.setFileSelectionMode(1);
                EinsatzArtAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + EinsatzArtAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(EinsatzArtAO.this.chooser.getSelectedFile().getPath()) + "/EinsatzArt_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

