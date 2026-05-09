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
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.statistik;

import ao.utils.ProzessBarAO;
import data.tabellen.TabelleAbwesenheit;
import data.tabellen.TabelleAbwesenheitsgrund;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Dimension;
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
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
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

public class AbwesenheitsStatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private static JButton buttonNeuLaden;
    private static JComboBox<String> jahresAuswahl;
    private JFileChooser chooser;

    public static void start(final int jahr) {
        Steuerung.setStatus(Status.PROZESSBAR);
        Steuerung.steuerung();
        ProzessBarAO.progressbar.setStringPainted(false);
        ProzessBarAO.progressbar.setIndeterminate(true);
        ProzessBarAO.label_bitteWarten.setText("Statistik wird berechnet... Bitte haben Sie einen Moment Geduld...");
        Thread threadStatistik = new Thread(){

            @Override
            public void run() {
                AbwesenheitsStatistikAO abwesenheit = new AbwesenheitsStatistikAO(jahr);
                abwesenheit.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
                abwesenheit.add(buttonZurueck);
                abwesenheit.add(buttonPdfExport);
                abwesenheit.add(buttonJpgExport);
                abwesenheit.add(jahresAuswahl);
                abwesenheit.add(buttonNeuLaden);
                abwesenheit.setDefaultCloseOperation(2);
                RefineryUtilities.centerFrameOnScreen((Window)abwesenheit);
                Image icon = new ImageIcon("images/icon.jpg").getImage();
                abwesenheit.setIconImage(icon);
                if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
                    abwesenheit.setAlwaysOnTop(true);
                }
                abwesenheit.setVisible(true);
                MyEvent.setEvent((String)"0x0030");
            }
        };
        threadStatistik.start();
    }

    public AbwesenheitsStatistikAO(int jahr) {
        super("FeuerwehrManagementSystem - Abwesenheitsstatistik");
        this.myButtons(jahr);
        JPanel chartPanel = AbwesenheitsStatistikAO.createDemoPanel(jahr);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(chartPanel);
    }

    public static CategoryDataset createDataset(int jahr) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleAbwesenheit tabAbwesend = new TabelleAbwesenheit();
        TabelleAbwesenheitsgrund tabGrund = new TabelleAbwesenheitsgrund();
        try {
            String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
            int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
            String[] grundListe = Utils.listToArray(tabGrund.getAlleGruende());
            int[] grundIDListe = Utils.listToIntArray(tabGrund.getAlleGruendID());
            int i = 0;
            while (i < mitgliederListe.length) {
                int g = 0;
                while (g < grundListe.length) {
                    dataset.addValue((double)tabAbwesend.getCountOfAbwesenheitsgrund(mitgliederIDListe[i], grundIDListe[g], jahr), (Comparable)((Object)grundListe[g]), (Comparable)((Object)mitgliederListe[i]));
                    ++g;
                }
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return dataset;
    }

    public static JFreeChart createChart(CategoryDataset dataset) {
        JFreeChart chart = ChartFactory.createStackedBarChart((String)" ", (String)"Name", (String)"Anzahl", (CategoryDataset)dataset, (PlotOrientation)PlotOrientation.HORIZONTAL, (boolean)true, (boolean)true, (boolean)false);
        CategoryPlot plot = (CategoryPlot)chart.getPlot();
        StackedBarRenderer renderer = (StackedBarRenderer)plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator((CategoryItemLabelGenerator)new StandardCategoryItemLabelGenerator());
        return chart;
    }

    public static JPanel createDemoPanel(int jahr) {
        JFreeChart chart = AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahr));
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
                    AbwesenheitsStatistikAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    AbwesenheitsStatistikAO.start(Integer.parseInt(jahresAuswahl.getSelectedItem().toString()));
                }
            }
        });
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AbwesenheitsStatistikAO.this.chooser.setFileSelectionMode(1);
                AbwesenheitsStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + AbwesenheitsStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AbwesenheitsStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahr)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AbwesenheitsStatistikAO.this.chooser.setFileSelectionMode(1);
                AbwesenheitsStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + AbwesenheitsStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AbwesenheitsStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AbwsenheitslistePDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahr)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

