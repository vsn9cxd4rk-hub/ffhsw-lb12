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
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.einstellungen.TabelleJahr;
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
import utilities.Utils;

public class EinsatzFahrzeugStatistikAO
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
        ProzessBarAO.label_bitteWarten.setText("Statistik wird berechnet... Bitte haben sie einen Moment Geduld...");
        Thread threadStatistik = new Thread(){

            @Override
            public void run() {
                EinsatzFahrzeugStatistikAO fahrzeugStatistik = new EinsatzFahrzeugStatistikAO(jahr);
                fahrzeugStatistik.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
                fahrzeugStatistik.setDefaultCloseOperation(2);
                fahrzeugStatistik.add(buttonZurueck);
                fahrzeugStatistik.add(buttonPdfExport);
                fahrzeugStatistik.add(buttonJpgExport);
                fahrzeugStatistik.add(jahresAuswahl);
                fahrzeugStatistik.add(buttonNeuLaden);
                RefineryUtilities.centerFrameOnScreen((Window)fahrzeugStatistik);
                Image icon = new ImageIcon("images/icon.jpg").getImage();
                fahrzeugStatistik.setIconImage(icon);
                if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
                    fahrzeugStatistik.setAlwaysOnTop(true);
                }
                fahrzeugStatistik.setVisible(true);
                MyEvent.setEvent((String)"0x0030");
            }
        };
        threadStatistik.start();
    }

    public EinsatzFahrzeugStatistikAO(int jahr) {
        super("FeuerwehrManagementSystem - Einsatzfahrzeug");
        this.myButtons(jahr);
        JPanel jpanel = EinsatzFahrzeugStatistikAO.createPanel(jahr);
        jpanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(jpanel);
    }

    public static CategoryDataset createDataset(int jahr) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        String s = "Anzahl der Eins\u00e4tze";
        TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        try {
            String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeFromDataBase());
            int i = 0;
            while (i < fahrzeugListe.length) {
                int wert = tabZeiten.getCountEingesetzterFahrzeuge(tabFahrzeug.getFahrzeugID(fahrzeugListe[i]), jahr);
                if (wert != 0) {
                    logging.logInfo((Object)wert);
                    defaultcategorydataset.addValue((double)wert, (Comparable)((Object)s), (Comparable)((Object)fahrzeugListe[i]));
                }
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return defaultcategorydataset;
    }

    public static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart((String)" ", (String)"Fahrzeuge", (String)"Anzahl", (CategoryDataset)categorydataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
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
        JFreeChart jfreechart = EinsatzFahrzeugStatistikAO.createChart(EinsatzFahrzeugStatistikAO.createDataset(jahr));
        return new ChartPanel(jfreechart);
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
                    EinsatzFahrzeugStatistikAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    EinsatzFahrzeugStatistikAO.start(Integer.parseInt(jahresAuswahl.getSelectedItem().toString()));
                }
            }
        });
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzFahrzeugStatistikAO.this.chooser.setFileSelectionMode(1);
                EinsatzFahrzeugStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + EinsatzFahrzeugStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(EinsatzFahrzeugStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AnwesenheitDienstabendListPDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(EinsatzFahrzeugStatistikAO.createChart(EinsatzFahrzeugStatistikAO.createDataset(jahr)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzFahrzeugStatistikAO.this.chooser.setFileSelectionMode(1);
                EinsatzFahrzeugStatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + EinsatzFahrzeugStatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(EinsatzFahrzeugStatistikAO.this.chooser.getSelectedFile().getPath()) + "/AnwesenheitDienstabendListJPG_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(EinsatzFahrzeugStatistikAO.createChart(EinsatzFahrzeugStatistikAO.createDataset(jahr)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

