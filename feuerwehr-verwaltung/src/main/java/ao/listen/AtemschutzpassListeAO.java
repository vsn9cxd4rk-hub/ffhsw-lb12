/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.TabelleAtemschutzpass;
import data.tabellen.mitglied.TabelleMitglied;
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
import utilities.Utils;

public class AtemschutzpassListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private JButton buttonVerwandteAnzeigen;
    private DefaultTableModel defaultTableModelMitgliederListe;
    public JTable table;
    private JScrollPane scrollpane;
    private JComboBox<String> mitglieder;
    private JLabel mitglieder_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum");
            this.add("Einsatznummer");
            this.add("Veranstaltung");
            this.add("Einsatzart");
            this.add("Zeit des Einsatzes");
            if (runApplication.EINSTELLUNGEN.get("einsatzleiterBF").equals("1")) {
                this.add("1. Gruppenf\u00fchrer FF");
            } else {
                this.add("Einsatzleiter");
            }
        }
    };
    private Vector<String> headnameByEinsatznummer = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum");
            this.add("Einsatznummer");
            this.add("Veranstaltung");
            this.add("Einsatzart");
            this.add("Atemschutztr\u00e4ger");
            this.add("Zeit des Einsatzes");
            this.add("Truppzuordnung");
            if (runApplication.EINSTELLUNGEN.get("einsatzleiterBF").equals("1")) {
                this.add("1. Gruppenf\u00fchrer FF");
            } else {
                this.add("Einsatzleiter");
            }
        }
    };

    public AtemschutzpassListeAO() {
        super("FeuerwehrManagementSystem - Atemschutzpass");
        logging.logInfo((Object)"Starte: AtemschutzpassListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonVerwandteAnzeigen = new JButton("Verwandte anzeigen");
        this.mitglieder_label = new JLabel("Mitglieder: ");
        this.modulBeschreibung = new JLabel("Atemschutzpass");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        TabelleMitglied tabMitglied = new TabelleMitglied();
        try {
            String[] mitgliederGruppeListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getAlleAtemschutztraeger());
            this.mitglieder = new JComboBox<String>(mitgliederGruppeListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.mitglieder.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    ((DefaultTableModel)AtemschutzpassListeAO.this.table.getModel()).setDataVector(new TabelleAtemschutzpass().getEinsaetzeForTable(tabMitglied.getIdByGuiString(AtemschutzpassListeAO.this.mitglieder.getSelectedItem().toString())), AtemschutzpassListeAO.this.headname);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void setzeAuswahllisten() {
        this.defaultTableModelMitgliederListe = new DefaultTableModel(10, 9);
        this.defaultTableModelMitgliederListe.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelMitgliederListe);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
        this.table.setFillsViewportHeight(true);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        this.table.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                e.getClickCount();
            }
        });
        this.scrollpane = new JScrollPane(this.table);
        this.scrollpane.setVerticalScrollBarPolicy(22);
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
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.mitglieder_label);
        this.add(this.mitglieder);
        this.add(this.scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonVerwandteAnzeigen);
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
                String headerText = "Atemschutzpass - " + AtemschutzpassListeAO.this.mitglieder.getSelectedItem().toString();
                Utils.printJTable(headerText, AtemschutzpassListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonVerwandteAnzeigen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleAtemschutzpass tabPass = new TabelleAtemschutzpass();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    if (AtemschutzpassListeAO.this.table.getSelectedRow() == -1) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                    } else {
                        String[] vName = Utils.listToArray(tabPass.getIDArrayVeransatltung(tabMitglied.getIdByGuiString(AtemschutzpassListeAO.this.mitglieder.getSelectedItem().toString())));
                        ((DefaultTableModel)AtemschutzpassListeAO.this.table.getModel()).setDataVector(new TabelleAtemschutzpass().getEinsaetzeForTableBYEinsatznummer(vName[AtemschutzpassListeAO.this.table.getSelectedRow()]), AtemschutzpassListeAO.this.headnameByEinsatznummer);
                        AtemschutzpassListeAO.this.mitglieder.setSelectedItem("<bitte w\u00e4hlen>");
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AtemschutzpassListeAO.this.chooser.setFileSelectionMode(1);
                AtemschutzpassListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + AtemschutzpassListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = AtemschutzpassListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(AtemschutzpassListeAO.this.table, new File(outputOrdner), "Atemschutzpass");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleAtemschutzpass().getEinsaetzeForTable(0), this.headname);
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

