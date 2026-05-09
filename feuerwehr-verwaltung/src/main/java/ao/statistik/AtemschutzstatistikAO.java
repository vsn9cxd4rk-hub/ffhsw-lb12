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

import data.tabellen.TabelleAtemschutzpass;
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
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.SbcUtils;
import utilities.Utils;

public class AtemschutzstatistikAO
extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JButton buttonZurueck;
    private static JButton buttonPdfExport;
    private static JButton buttonJpgExport;
    private static JButton buttonNeuLaden;
    public static JComboBox<String> jahresAuswahl;
    public static JComboBox<String> mitgliederAuswahl;
    private JFileChooser chooser;

    public static void start(int mitgliedID, int jahr) {
        AtemschutzstatistikAO atemschutzstatistik = new AtemschutzstatistikAO(mitgliedID, jahr);
        atemschutzstatistik.setSize(runApplication.widthStatistikGUI, runApplication.higthStatistikGUI);
        atemschutzstatistik.setDefaultCloseOperation(2);
        atemschutzstatistik.add(buttonZurueck);
        atemschutzstatistik.add(buttonPdfExport);
        atemschutzstatistik.add(buttonJpgExport);
        atemschutzstatistik.add(mitgliederAuswahl);
        atemschutzstatistik.add(jahresAuswahl);
        atemschutzstatistik.add(buttonNeuLaden);
        RefineryUtilities.centerFrameOnScreen((Window)atemschutzstatistik);
        Image icon = new ImageIcon("images/icon.jpg").getImage();
        atemschutzstatistik.setIconImage(icon);
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            atemschutzstatistik.setAlwaysOnTop(true);
        }
        atemschutzstatistik.setVisible(true);
    }

    public AtemschutzstatistikAO(int mitgliedID, int jahr) {
        super("FeuerwehrManagementSystem - Atemschutzstaistik");
        this.myButtons(mitgliedID, jahr);
        JPanel jpanel = AtemschutzstatistikAO.createPanel(mitgliedID, jahr);
        jpanel.setPreferredSize(new Dimension(500, 270));
        this.setContentPane(jpanel);
    }

    public static CategoryDataset createDataset(int mitgliedID, int jahr) {
        DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset();
        String s = "Einsatzzeit in Minuten";
        TabelleAtemschutzpass tabAtemschutz = new TabelleAtemschutzpass();
        try {
            defaultcategorydataset.addValue((double)tabAtemschutz.getGesamtZeit(mitgliedID, 1, jahr, 1), (Comparable)((Object)s), (Comparable)((Object)"Einsatz Pressluftatmer (PA)"));
            defaultcategorydataset.addValue((double)tabAtemschutz.getGesamtZeit(mitgliedID, 2, jahr, 1), (Comparable)((Object)s), (Comparable)((Object)"Einsatz Filter"));
            defaultcategorydataset.addValue((double)tabAtemschutz.getGesamtZeit(mitgliedID, 3, jahr, 1), (Comparable)((Object)s), (Comparable)((Object)"Einsatz CSA"));
            defaultcategorydataset.addValue((double)tabAtemschutz.getGesamtZeit(mitgliedID, 1, jahr, -1), (Comparable)((Object)s), (Comparable)((Object)"\u00dcbungen Pressluftatmer (PA)"));
            defaultcategorydataset.addValue((double)tabAtemschutz.getGesamtZeit(mitgliedID, 2, jahr, -1), (Comparable)((Object)s), (Comparable)((Object)"\u00dcbungen Filter"));
            defaultcategorydataset.addValue((double)tabAtemschutz.getGesamtZeit(mitgliedID, 3, jahr, -1), (Comparable)((Object)s), (Comparable)((Object)"\u00dcbungen CSA"));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return defaultcategorydataset;
    }

    public static JFreeChart createChart(CategoryDataset categorydataset) {
        JFreeChart jfreechart = ChartFactory.createBarChart((String)" ", (String)"Atemschutz Statistik", (String)"Einsatzzeit in Minuten", (CategoryDataset)categorydataset, (PlotOrientation)PlotOrientation.VERTICAL, (boolean)true, (boolean)true, (boolean)false);
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

    public static JPanel createPanel(int mitgliedID, int jahr) {
        JFreeChart jfreechart = AtemschutzstatistikAO.createChart(AtemschutzstatistikAO.createDataset(mitgliedID, jahr));
        return new ChartPanel(jfreechart);
    }

    private void myButtons(final int mitgliedID, final int jahr) {
        buttonZurueck = new JButton("Schlie\u00dfen");
        buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener(this)));
        buttonPdfExport = new JButton("PDF Export");
        buttonJpgExport = new JButton("JPG Export");
        buttonNeuLaden = new JButton("Neu Laden");
        TabelleJahr tabJahr = new TabelleJahr();
        final TabelleMitglied tabMitglied = new TabelleMitglied();
        try {
            String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            jahresAuswahl = new JComboBox<String>(jahresListe);
            jahresAuswahl.setSelectedItem(Integer.toString(jahr));
            mitgliederAuswahl = new JComboBox<String>(mitgliederListe);
            if (mitgliedID == 0) {
                mitgliederAuswahl.setSelectedItem("<bitte w\u00e4hlen>");
            } else {
                mitgliederAuswahl.setSelectedItem(tabMitglied.getNameVornameByID(mitgliedID));
            }
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
                } else if (mitgliederAuswahl.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    logging.logInfo((Object)"Es wurde keine Mitglied ausgew\u00e4hlt");
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                } else {
                    AtemschutzstatistikAO.this.dispose();
                    logging.logInfo((Object)("Die Statistik wird mit der neuen Jahresauswahl aktualisiert " + jahresAuswahl.getSelectedItem().toString()));
                    try {
                        AtemschutzstatistikAO.start(tabMitglied.getIdByGuiString(mitgliederAuswahl.getSelectedItem().toString()), Integer.parseInt(jahresAuswahl.getSelectedItem().toString()));
                    }
                    catch (NumberFormatException | SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.chooser = new JFileChooser();
        buttonPdfExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AtemschutzstatistikAO.this.chooser.setFileSelectionMode(1);
                AtemschutzstatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte PDF Export in: " + AtemschutzstatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AtemschutzstatistikAO.this.chooser.getSelectedFile().getPath()) + "/Ausr\u00fcckezeit_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
                MyChartUtils.writeChartToPDF(AtemschutzstatistikAO.createChart(AtemschutzstatistikAO.createDataset(mitgliedID, jahr)), 550, 800, outputFile);
                logging.logInfo((Object)"PDF Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
        buttonJpgExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AtemschutzstatistikAO.this.chooser.setFileSelectionMode(1);
                AtemschutzstatistikAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte JPG Export in: " + AtemschutzstatistikAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = String.valueOf(AtemschutzstatistikAO.this.chooser.getSelectedFile().getPath()) + "/Ausr\u00fcckezeit_Stand_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".jpg";
                MyChartUtils.writeChartToJPEG(AtemschutzstatistikAO.createChart(AtemschutzstatistikAO.createDataset(mitgliedID, jahr)), 1000, 800, outputFile);
                logging.logInfo((Object)"JPG Export Beendet");
                JOptionPane.showMessageDialog(null, Konstante.CHART_EXPORT_ERFOLGREICH);
            }
        });
    }
}

