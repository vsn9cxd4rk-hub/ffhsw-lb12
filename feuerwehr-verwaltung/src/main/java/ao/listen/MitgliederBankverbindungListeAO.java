/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.mitglied.TabelleMitglieder_bankverbindung;
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
import utilities.Konstante;
import utilities.Utils;

public class MitgliederBankverbindungListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelMitgliederListe;
    public JTable table;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Dienstgrad");
            this.add("Name");
            this.add("Vorname");
            this.add("IBAN");
            this.add("BIC");
        }
    };

    public MitgliederBankverbindungListeAO() {
        super("FeuerwehrManagementSystem - Mitglieder Bankverbindung Liste");
        logging.logInfo((Object)"Starte: MitgliederBankverbindungListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.modulBeschreibung = new JLabel("Mitglieder bankverbindung Liste");
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
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelMitgliederListe = new DefaultTableModel(10, 9);
        this.defaultTableModelMitgliederListe.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelMitgliederListe);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 600));
        this.table.setFillsViewportHeight(true);
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
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
                String headerText = "Mitglieder Bankverbindung";
                Utils.printJTable(headerText, MitgliederBankverbindungListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MitgliederBankverbindungListeAO.this.chooser.setFileSelectionMode(1);
                MitgliederBankverbindungListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + MitgliederBankverbindungListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = MitgliederBankverbindungListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(MitgliederBankverbindungListeAO.this.table, new File(outputOrdner), "Bankverbindung");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleMitglieder_bankverbindung().getAllBankverbindungenForTable(), this.headname);
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

