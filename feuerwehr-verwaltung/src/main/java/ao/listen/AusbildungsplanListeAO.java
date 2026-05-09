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
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.einstellungen.TabelleJahr;
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
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;

public class AusbildungsplanListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonEditieren;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelLehrgang;
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
            this.add("Datum");
            this.add("Uhrzeit");
            this.add("Ausbildungsinhalt");
            this.add("Details / Kommentar");
            this.add("Ausbilder 1");
            this.add("Ausbilder 2");
        }
    };

    public AusbildungsplanListeAO() {
        super("FeuerwehrManagementSystem - Ausbildungsplan");
        logging.logInfo((Object)"Starte: AusbildungsplanAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonEditieren = new JButton("Ausbildungsplan editieren");
        this.modulBeschreibung = new JLabel("Ausbildungsplan");
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
            this.veranstaltungOption_label = new JLabel("Ausbildungsjahr: ");
            this.jahre.addItem(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1));
            this.jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.jahre.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    ((DefaultTableModel)AusbildungsplanListeAO.this.table.getModel()).setDataVector(new TabelleAusbildung_plan().getAusbildungsplanForTable(AusbildungsplanListeAO.this.jahre.getSelectedItem().toString()), AusbildungsplanListeAO.this.headname);
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
        this.setTitle("FeuerwehrManagementSystem - Ausbildungsplan");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelLehrgang = new DefaultTableModel(5, 4);
        this.defaultTableModelLehrgang.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelLehrgang);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
        this.table.setFillsViewportHeight(true);
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
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
        this.add(this.buttonEditieren);
        this.add(this.buttonZurueck);
        if (BerechtigunsManager.ber[63] == 1) {
            this.buttonEditieren.setEnabled(true);
        } else {
            this.buttonEditieren.setEnabled(false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Ausbildungsplan " + AusbildungsplanListeAO.this.jahre.getSelectedItem().toString();
                Utils.printJTable(headerText, AusbildungsplanListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonEditieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AusbildungsplanListeAO.this.dispose();
                if (runApplication.instanceofAusbildungsplanISRunning == 0) {
                    Steuerung.setStatus(Status.AUSBILDUNGSPLAN);
                    Steuerung.steuerung();
                }
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AusbildungsplanListeAO.this.chooser.setFileSelectionMode(1);
                AusbildungsplanListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + AusbildungsplanListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = AusbildungsplanListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(AusbildungsplanListeAO.this.table, new File(outputOrdner), "Ausbildungsplan");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleAusbildung_plan().getAusbildungsplanForTable(this.jahre.getSelectedItem().toString()), this.headname);
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

