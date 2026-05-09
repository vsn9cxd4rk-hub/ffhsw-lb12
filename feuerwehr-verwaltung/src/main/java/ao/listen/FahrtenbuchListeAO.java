/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.TabelleFahrtenbuch;
import data.tabellen.TabelleFahrzeug;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import utilities.Utils;

public class FahrtenbuchListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelFahrtenbuch;
    public JTable table;
    private JComboBox<String> fahrzeug;
    private JLabel fahrzeug_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Veranstaltung");
            this.add("Datum");
            this.add("Zeit Von");
            this.add("Zeit Bis");
            this.add("KM Beginn");
            this.add("KM Ende");
            this.add("Distanz");
            this.add("Tanken");
            this.add("Pumpenbetrieb");
            this.add("Sonstiges");
            this.add("Fahrer");
        }
    };

    public FahrtenbuchListeAO() {
        super("FeuerwehrManagementSystem - Fahrtenbuch Liste");
        logging.logInfo((Object)"Starte: FahrtenbuchListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.fahrzeug_label = new JLabel("Fahrzeug: ");
        this.modulBeschreibung = new JLabel("Fahrtenbuch Liste");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
        try {
            String[] fahrzeugListe = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());
            this.fahrzeug = new JComboBox<String>(fahrzeugListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.fahrzeug.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
                    ((DefaultTableModel)FahrtenbuchListeAO.this.table.getModel()).setDataVector(new TabelleFahrtenbuch().getFahrtenbuchForTable(tabFahrzeuge.getFahrzeugID(FahrtenbuchListeAO.this.fahrzeug.getSelectedItem().toString())), FahrtenbuchListeAO.this.headname);
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
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(1200, 768);
        this.setTitle("FeuerwehrManagementSystem - Fahrtenbuch Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelFahrtenbuch = new DefaultTableModel(10, 9);
        this.defaultTableModelFahrtenbuch.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelFahrtenbuch);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
        this.table.setFillsViewportHeight(true);
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.fahrzeug_label);
        this.add(this.fahrzeug);
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
                String headerText = "Fahrtenbuch";
                Utils.printJTable(headerText, FahrtenbuchListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FahrtenbuchListeAO.this.chooser.setFileSelectionMode(1);
                FahrtenbuchListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + FahrtenbuchListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = FahrtenbuchListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(FahrtenbuchListeAO.this.table, new File(outputOrdner), "Fahrtenbuch");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleFahrtenbuch().getFahrtenbuchForTable(0), this.headname);
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

