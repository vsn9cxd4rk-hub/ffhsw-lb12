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
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
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

public class AnwesenheitGesamtInProzentAO
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
                AnwesenheitGesamtInProzentAO anweseneheitStatistikInProzent = new AnwesenheitGesamtInProzentAO(jahr);
                anweseneheitStatistikInProzent.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
                anweseneheitStatistikInProzent.setDefaultCloseOperation(2);
                anweseneheitStatistikInProzent.add(buttonZurueck);
                anweseneheitStatistikInProzent.add(buttonPdfExport);
                anweseneheitStatistikInProzent.add(buttonJpgExport);
                anweseneheitStatistikInProzent.add(jahresAuswahl);
                anweseneheitStatistikInProzent.add(buttonNeuLaden);
                RefineryUtilities.centerFrameOnScreen((Window)anweseneheitStatistikInProzent);
                Image icon = new ImageIcon("images/icon.jpg").getImage();
                anweseneheitStatistikInProzent.setIconImage(icon);
                if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
                    anweseneheitStatistikInProzent.setAlwaysOnTop(true);
                }
                anweseneheitStatistikInProzent.setVisible(true);
                MyEvent.setEvent((String)"0x0030");
            }
        };
        threadStatistik.start();
    }

    public AnwesenheitGesamtInProzentAO(int jahr) {
        super("FeuerwehrManagementSystem - Anwesenheitstatistik Gesamt");
        this.myButtons(jahr);
        JPanel jpanel = AnwesenheitGesamtInProzentAO.createPanel(jahr);
        jpanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(jpanel);
    }

    public static CategoryDataset createDataset(int jahr) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        TabelleAnwesenheit tabAnwesend = new TabelleAnwesenheit();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        try {
            String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
            int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
            int length = tabMitglieder.getMitgliederCountGruppe1();
            int anzahlVeranstaltungenProJahr = tabVeranstaltung.getCountAllVeranstaltungEinesJahres(Integer.toString(jahr));
            String s = "Anweseneheit in % (Veranstaltungen Gesamt: " + anzahlVeranstaltungenProJahr + ")";
            int i = 0;
            while (i < length) {
                int wert = tabAnwesend.getGesamtBeteiligung(mitgliederIDListe[i], jahr);
                logging.logInfo((Object)wert);
                defaultcategorydataset.addValue((double)(wert * 100 / anzahlVeranstaltungenProJahr), (Comparable)((Object)s), (Comparable)((Object)mitgliederListe[i]));
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        catch (ArithmeticException arithmeticException) {
            // empty catch block
        }
        return defaultcategorydataset;
    }

    public static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart((String)" ", (String)"Personen", (String)"Anwesenheit in %", (CategoryDataset)categorydataset, (PlotOrientation)PlotOrientation.HORIZONTAL, (boolean)true, (boolean)true, (boolean)false);
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
        NumberAxis rangeAxis = (NumberAxis)categoryplot.getRangeAxis();
        rangeAxis.setRange(0.0, 100.0);
        return jfreechart;
    }

    public static JPanel createPanel(int jahr) {
        JFreeChart jfreechart = AnwesenheitGesamtInProzentAO.createChart(AnwesenheitGesamtInProzentAO.createDataset(jahr));
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
                    AnwesenheitGesamtInProzentAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    AnwesenheitGesamtInProzentAO.start(Integer.parseInt(jahresAuswahl.getSelectedItem().toString()));
                }
            }
        });
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitGesamtInProzentAO.this.chooser.setFileSelectionMode(1);
                AnwesenheitGesamtInProzentAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + AnwesenheitGesamtInProzentAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AnwesenheitGesamtInProzentAO.this.chooser.getSelectedFile().getPath()) + "/AnwesenheitListPDF_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(AnwesenheitGesamtInProzentAO.createChart(AnwesenheitGesamtInProzentAO.createDataset(jahr)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitGesamtInProzentAO.this.chooser.setFileSelectionMode(1);
                AnwesenheitGesamtInProzentAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + AnwesenheitGesamtInProzentAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AnwesenheitGesamtInProzentAO.this.chooser.getSelectedFile().getPath()) + "/AnwesenheitListJPG_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(AnwesenheitGesamtInProzentAO.createChart(AnwesenheitGesamtInProzentAO.createDataset(jahr)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

