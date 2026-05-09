/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.TabelleLehrgang;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Vector;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JButton;
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
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class LehrgangListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private JButton buttonOptionen;
    public static JButton buttonStandard;
    private DefaultTableModel defaultTableModelLehrgang;
    public static JTable table;
    public static Vector headname;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;

    public LehrgangListeAO() {
        super("FeuerwehrManagementSystem - Lehrgang Liste");
        logging.logInfo((Object)"Starte: LehrgangListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonOptionen = new JButton("Filter / Optionen");
        buttonStandard = new JButton("Standard Wiederherstellen");
        headname = new Vector();
        try {
            headname = new TabelleLehrgang().mapHeadNameToVector();
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.modulBeschreibung = new JLabel("Lehrgang Liste (Stand: " + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ")");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
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
        this.setTitle("FeuerwehrManagementSystem - Lehrgang Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelLehrgang = new DefaultTableModel(10, 9);
        this.defaultTableModelLehrgang.setColumnIdentifiers(headname);
        table = new JTable(this.defaultTableModelLehrgang);
        table.setPreferredScrollableViewportSize(new Dimension(1100, 560));
        table.setEnabled(false);
        table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        table.setAutoResizeMode(0);
        JScrollPane scrollpane = new JScrollPane(table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(buttonStandard);
        this.add(this.buttonOptionen);
        this.add(scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonZurueck);
        buttonStandard.setVisible(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Lehrgang Liste (Stand: " + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ")";
                Utils.printJTable(headerText, table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonOptionen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.LEHRGANG_LISTE_OPTIONEN);
                Steuerung.steuerung();
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                LehrgangListeAO.this.chooser.setFileSelectionMode(1);
                LehrgangListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + LehrgangListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = LehrgangListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(table, new File(outputOrdner), "LehrgangListe");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
        buttonStandard.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                buttonStandard.setVisible(false);
                try {
                    headname = new Vector();
                    headname = new TabelleLehrgang().mapHeadNameToVector();
                    ((DefaultTableModel)table.getModel()).setDataVector(new TabelleLehrgang().getAllDataForList(), headname);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)table.getModel()).setDataVector(new TabelleLehrgang().getAllDataForList(), headname);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        MyEvent.setEvent((String)"0x0030");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

