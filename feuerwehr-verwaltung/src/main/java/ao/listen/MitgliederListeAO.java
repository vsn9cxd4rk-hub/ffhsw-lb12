/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.listen;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleMandant;
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
import service.BerechtigunsManager;
import utilities.Konstante;
import utilities.Utils;

public class MitgliederListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private JComboBox<String> details;
    private DefaultTableModel defaultTableModelMitgliederListe;
    public JTable table;
    private JComboBox<String> mitgliederGruppen;
    private JLabel mitgliederGruppen_label;
    private JComboBox<String> mandant;
    private JLabel mandant_label;
    private JLabel details_label;
    private JFileChooser chooser;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private Vector<String> headname\u00dcbersicht = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Dienstgrad");
            this.add("Name");
            this.add("Vorname");
            this.add("Stra\u00dfe");
            this.add("Wohnort");
            this.add("Telefon Privat");
            this.add("Telefon Mobil");
            this.add("E-Mail");
            this.add("Geb. Datum");
        }
    };
    private Vector<String> headname\u00dcbersicht2 = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Dienstgrad");
            this.add("Name");
            this.add("Vorname");
            this.add("Telefon Artebit");
            this.add("E-Mail2");
            this.add("Mitglied Seit");
            this.add("Kommentar");
        }
    };
    private Vector<String> headnameTelefonliste = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Dienstgrad");
            this.add("Name");
            this.add("Vorname");
            this.add("Telefon Privat");
            this.add("Telefon Mobil");
            this.add("Telefon Arbeit");
        }
    };
    private Vector<String> headnameZusatzdaten = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Dienstgrad");
            this.add("Name");
            this.add("Vorname");
            this.add("F\u00fchrerscheinnummer");
            this.add("LKW F\u00fchrerschein - Ablaufdatum");
            this.add("Dienstausweisnummer");
            this.add("Dienstausweis - Ablaufdatum");
            this.add("Fahrberechtigungsnummer");
            this.add("Fahrberechtigung - Ablaufdatum");
        }
    };

    public MitgliederListeAO() {
        super("FeuerwehrManagementSystem - Mitglieder Liste");
        logging.logInfo((Object)"Starte: MitgliederListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.mitgliederGruppen_label = new JLabel("Mitgliedergruppen: ");
        this.details_label = new JLabel("Details: ");
        this.mandant_label = new JLabel("Mandant: ");
        this.modulBeschreibung = new JLabel("Mitglieder Liste");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
        TabelleMandant tabMandant = new TabelleMandant();
        try {
            String[] mitgliederGruppeListe = Utils.listToArrayOnlyFORComboBoxes(tabGruppe.getAllGruppen());
            String[] mandantListe = Utils.listToArrayOnlyFORComboBoxes(tabMandant.getAllMandanten());
            this.mitgliederGruppen = new JComboBox<String>(mitgliederGruppeListe);
            this.mandant = new JComboBox<String>(mandantListe);
            String[] deteilsListe = new String[]{"\u00dcbersichtsliste", "\u00dcbersichtsliste2", "Telefonliste"};
            this.details = new JComboBox<String>(deteilsListe);
            if (BerechtigunsManager.ber2[52] == 1) {
                this.details.addItem("Zusatzdaten");
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.mitgliederGruppen.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
                    ((DefaultTableModel)MitgliederListeAO.this.table.getModel()).setDataVector(new TabelleMitglied().getAllMitgliederForTable(tabGruppe.getID(MitgliederListeAO.this.mitgliederGruppen.getSelectedItem().toString()), new TabelleMandant().getMandantID(MitgliederListeAO.this.mandant.getSelectedItem().toString())), MitgliederListeAO.this.headname\u00dcbersicht);
                    MitgliederListeAO.this.details.setSelectedItem("\u00dcbersichtsliste");
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.details.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
                try {
                    if (MitgliederListeAO.this.details.getSelectedItem().toString().equals("\u00dcbersichtsliste")) {
                        ((DefaultTableModel)MitgliederListeAO.this.table.getModel()).setDataVector(new TabelleMitglied().getAllMitgliederForTable(tabGruppe.getID(MitgliederListeAO.this.mitgliederGruppen.getSelectedItem().toString()), new TabelleMandant().getMandantID(MitgliederListeAO.this.mandant.getSelectedItem().toString())), MitgliederListeAO.this.headname\u00dcbersicht);
                    } else if (MitgliederListeAO.this.details.getSelectedItem().toString().equals("\u00dcbersichtsliste2")) {
                        ((DefaultTableModel)MitgliederListeAO.this.table.getModel()).setDataVector(new TabelleMitglied().getAllMitgliederForTableUebersicht2(tabGruppe.getID(MitgliederListeAO.this.mitgliederGruppen.getSelectedItem().toString()), new TabelleMandant().getMandantID(MitgliederListeAO.this.mandant.getSelectedItem().toString())), MitgliederListeAO.this.headname\u00dcbersicht2);
                    } else if (MitgliederListeAO.this.details.getSelectedItem().toString().equals("Telefonliste")) {
                        ((DefaultTableModel)MitgliederListeAO.this.table.getModel()).setDataVector(new TabelleMitglied().getAllMitgliederForTableTelefonliste(tabGruppe.getID(MitgliederListeAO.this.mitgliederGruppen.getSelectedItem().toString()), new TabelleMandant().getMandantID(MitgliederListeAO.this.mandant.getSelectedItem().toString())), MitgliederListeAO.this.headnameTelefonliste);
                    } else if (MitgliederListeAO.this.details.getSelectedItem().toString().equals("Zusatzdaten")) {
                        ((DefaultTableModel)MitgliederListeAO.this.table.getModel()).setDataVector(new TabelleMitglied().getAllMitgliederForTableZusatzdaten(tabGruppe.getID(MitgliederListeAO.this.mitgliederGruppen.getSelectedItem().toString()), new TabelleMandant().getMandantID(MitgliederListeAO.this.mandant.getSelectedItem().toString())), MitgliederListeAO.this.headnameZusatzdaten);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.mandant.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    MitgliederListeAO.this.mitgliederGruppen.setSelectedItem("Einsatzabteilung");
                    MitgliederListeAO.this.details.setSelectedItem("\u00dcbersichtsliste");
                    if (runApplication.mandantName.equals(MitgliederListeAO.this.mandant.getSelectedItem())) {
                        MitgliederListeAO.this.mitgliederGruppen.setEnabled(true);
                    } else {
                        MitgliederListeAO.this.mitgliederGruppen.setEnabled(false);
                    }
                    TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
                    ((DefaultTableModel)MitgliederListeAO.this.table.getModel()).setDataVector(new TabelleMitglied().getAllMitgliederForTable(tabGruppe.getID(MitgliederListeAO.this.mitgliederGruppen.getSelectedItem().toString()), new TabelleMandant().getMandantID(MitgliederListeAO.this.mandant.getSelectedItem().toString())), MitgliederListeAO.this.headname\u00dcbersicht);
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
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.defaultTableModelMitgliederListe = new DefaultTableModel(10, 9);
        this.defaultTableModelMitgliederListe.setColumnIdentifiers(this.headname\u00dcbersicht);
        this.table = new JTable(this.defaultTableModelMitgliederListe);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
        this.table.setFillsViewportHeight(true);
        this.table.setEnabled(false);
        this.table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        if (runApplication.BF == 1) {
            this.add(this.mandant_label);
            this.add(this.mandant);
        }
        this.add(this.mitgliederGruppen_label);
        this.add(this.mitgliederGruppen);
        this.add(this.details_label);
        this.add(this.details);
        this.add(scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonZurueck);
        this.mitgliederGruppen.setSelectedItem("Einsatzabteilung");
        this.mandant.setSelectedItem(runApplication.mandantName);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Mitgliederliste - " + MitgliederListeAO.this.details.getSelectedItem().toString();
                Utils.printJTable(headerText, MitgliederListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MitgliederListeAO.this.chooser.setFileSelectionMode(1);
                MitgliederListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + MitgliederListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = MitgliederListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(MitgliederListeAO.this.table, new File(outputOrdner), "Mitgliederliste");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleMitglied().getAllMitgliederForTable(1, Integer.parseInt(runApplication.PROPERTIES.get("MandantID"))), this.headname\u00dcbersicht);
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

