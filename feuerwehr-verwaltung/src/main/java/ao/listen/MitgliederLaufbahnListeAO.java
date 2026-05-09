/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
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

public class MitgliederLaufbahnListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelLaufbahnListe;
    public JTable table;
    private JComboBox<String> mitglieder;
    private JLabel mitgliederGruppen_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum von");
            this.add("Datum bis (Pr\u00fcfungsdatum)");
            this.add("Dienstgrad");
            this.add("Ehrungen / Abzeichen");
            this.add("Lehrgang");
            this.add("Unterrichtseinheiten");
        }
    };

    public MitgliederLaufbahnListeAO() {
        super("FeuerwehrManagementSystem - Mitgliederlaufbahn Liste");
        logging.logInfo((Object)"Starte: MitgliederLaufbahnListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.mitgliederGruppen_label = new JLabel("Mitglieder: ");
        this.modulBeschreibung = new JLabel("Mitgliederlaufbahn Liste");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        TabelleMitglied tabMitglied = new TabelleMitglied();
        try {
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            this.mitglieder = new JComboBox<String>(mitgliederListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.mitglieder.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    if (MitgliederLaufbahnListeAO.this.mitglieder.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        ((DefaultTableModel)MitgliederLaufbahnListeAO.this.table.getModel()).setDataVector(null, MitgliederLaufbahnListeAO.this.headname);
                    } else {
                        ((DefaultTableModel)MitgliederLaufbahnListeAO.this.table.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(tabMitglied.getIdByGuiString(MitgliederLaufbahnListeAO.this.mitglieder.getSelectedItem().toString()), null), MitgliederLaufbahnListeAO.this.headname);
                    }
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
        this.setTitle("FeuerwehrManagementSystem - Mitgliederlaufbahn Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelLaufbahnListe = new DefaultTableModel(10, 9);
        this.defaultTableModelLaufbahnListe.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelLaufbahnListe);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
        this.table.setFillsViewportHeight(true);
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.mitgliederGruppen_label);
        this.add(this.mitglieder);
        this.add(scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonZurueck);
        this.mitglieder.setSelectedItem("Einsatzabteilung");
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Mitgliederlaufbahn " + MitgliederLaufbahnListeAO.this.mitglieder.getSelectedItem().toString();
                Utils.printJTable(headerText, MitgliederLaufbahnListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MitgliederLaufbahnListeAO.this.chooser.setFileSelectionMode(1);
                MitgliederLaufbahnListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + MitgliederLaufbahnListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = MitgliederLaufbahnListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(MitgliederLaufbahnListeAO.this.table, new File(outputOrdner), "Laufbahn");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(0, null), this.headname);
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

