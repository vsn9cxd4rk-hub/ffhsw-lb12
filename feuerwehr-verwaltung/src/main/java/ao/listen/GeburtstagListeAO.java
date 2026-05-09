/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
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
import utilities.MyEvent;
import utilities.Utils;

public class GeburtstagListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelLehrgang;
    public JTable table;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private JComboBox<String> mitgliederKategorie;
    private JLabel mitgliederKategorie_label;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Mitglieder Gruppe");
            if (runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste").equals("1")) {
                this.add("Dienstgrad");
            }
            this.add("Name");
            this.add("Vorname");
            this.add("Geb. Datum");
        }
    };

    public GeburtstagListeAO() {
        super("FeuerwehrManagementSystem - Geburtstagsliste");
        logging.logInfo((Object)"Starte: GeburtstagListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.modulBeschreibung = new JLabel("Geburtstagsliste");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
        this.mitgliederKategorie_label = new JLabel("Mitgliedergruppe: ");
    }

    protected void labelErstellen() {
        try {
            TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
            String[] gruppenListe = Utils.listToArrayMitOptionAlle(tabGruppe.getAllGruppen());
            this.mitgliederKategorie = new JComboBox<String>(gruppenListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.mitgliederKategorie.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
                    int gruppe = 0;
                    if (!GeburtstagListeAO.this.mitgliederKategorie.getSelectedItem().toString().equals("<alle>")) {
                        gruppe = tabGruppe.getID(GeburtstagListeAO.this.mitgliederKategorie.getSelectedItem().toString());
                    }
                    ((DefaultTableModel)GeburtstagListeAO.this.table.getModel()).setDataVector(new TabelleMitglied().getAllGeburtstageForTable(gruppe), GeburtstagListeAO.this.headname);
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
        this.setTitle("FeuerwehrManagementSystem - Geburtstagsliste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelLehrgang = new DefaultTableModel(10, 9);
        this.defaultTableModelLehrgang.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelLehrgang);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.mitgliederKategorie_label);
        this.add(this.mitgliederKategorie);
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
                String headerText = "Geburtstagsliste";
                Utils.printJTable(headerText, GeburtstagListeAO.this.table, OrientationRequested.PORTRAIT, true, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                GeburtstagListeAO.this.chooser.setFileSelectionMode(1);
                GeburtstagListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + GeburtstagListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = GeburtstagListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(GeburtstagListeAO.this.table, new File(outputOrdner), "GeburtstagListe");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleMitglied().getAllGeburtstageForTable(0), this.headname);
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

