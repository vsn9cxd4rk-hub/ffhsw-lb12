/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.urlaubsplaner.TabelleUrlaub;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.Vector;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;

public class UrlaubsListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelEinsatz;
    public JTable table;
    private JComboBox<String> jahre;
    private JLabel veranstaltungOption_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Name");
            this.add("Datum Von");
            this.add("Datum Bis");
        }
    };

    public UrlaubsListeAO() {
        super("FeuerwehrManagementSystem - Urlaubsliste");
        logging.logInfo((Object)"Starte: UrlaubsListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.modulBeschreibung = new JLabel("Urlaubsliste " + SbcUtils.timeStamp((String)"yyyy"));
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        TabelleJahr tabJahr = new TabelleJahr();
        try {
            String[] option = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            this.jahre = new JComboBox<String>(option);
            this.veranstaltungOption_label = new JLabel("Jahr: ");
            this.jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
            this.modulBeschreibung.setText("Urlaubsliste " + this.jahre.getSelectedItem().toString());
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.jahre.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    ((DefaultTableModel)UrlaubsListeAO.this.table.getModel()).setDataVector(new TabelleUrlaub().getAllForList(UrlaubsListeAO.this.jahre.getSelectedItem().toString()), UrlaubsListeAO.this.headname);
                    UrlaubsListeAO.this.modulBeschreibung.setText("Einsatz Liste " + UrlaubsListeAO.this.jahre.getSelectedItem().toString());
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
        this.defaultTableModelEinsatz = new DefaultTableModel(10, 9);
        this.defaultTableModelEinsatz.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelEinsatz);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
        this.table.setFillsViewportHeight(true);
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        this.table.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                e.getClickCount();
            }
        });
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(1200, 768);
        this.setTitle("FeuerwehrManagementSystem - Einsatz Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.veranstaltungOption_label);
        this.add(this.jahre);
        this.add(scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Urlaubsliste";
                Utils.printJTable(headerText, UrlaubsListeAO.this.table, OrientationRequested.PORTRAIT, true, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                UrlaubsListeAO.this.chooser.setFileSelectionMode(1);
                UrlaubsListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + UrlaubsListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = UrlaubsListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(UrlaubsListeAO.this.table, new File(outputOrdner), "UrlaubListe");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleUrlaub().getAllForList(SbcUtils.timeStamp((String)"yyyy")), this.headname);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

