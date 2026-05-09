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
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleFahrzeugeinteilung;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.einstellungen.TabelleMandant;
import java.awt.Color;
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
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class EinsatzListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelEinsatz;
    public JTable table;
    private JComboBox<String> jahre;
    private JLabel veranstaltungOption_label;
    private JButton buttonAnwesenheitAnzeigen;
    private JButton buttonEinsatzZeiten;
    private JButton buttonEinsatzListe;
    private JButton buttonFahrzeugBesetzung;
    private JButton buttonOrganisationen;
    private JComboBox<String> mandant;
    private JLabel mandant_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Einsatzzahl");
            this.add("Einsatznummer");
            this.add("Datum");
            this.add("Uhrzeit");
            this.add("Stichwort");
            this.add("Ort");
            this.add("Fahrzeug");
            this.add("Beschreibung");
            this.add("ZF");
            this.add("GF");
            this.add("FM");
        }
    };
    private Vector<String> headnameAnwesenheit = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            if (runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste").equals("1")) {
                this.add("Dienstgrad");
            }
            this.add("Name");
            this.add("Vorname");
        }
    };
    private Vector<String> headnameOrganisationen = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Weitere Einheiten / Organisationen");
        }
    };
    private Vector<String> headnameEinsatzzeiten = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Fahrzeug Name");
            this.add("Alamierung");
            this.add("Ausger\u00fcckt");
            this.add("Eingetroffen");
            this.add("Einsatzende");
            this.add("Gesamtdauer");
            this.add("Mannstunden");
        }
    };
    private Vector<String> headnameFahrzeugBesetzung = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Fahrzeug");
            this.add("Funktion im Einsatz");
            this.add("Dienstgard");
            this.add("Name");
        }
    };

    public EinsatzListeAO() {
        super("FeuerwehrManagementSystem - Einsatz Liste");
        logging.logInfo((Object)"Starte: EinsatzListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonAnwesenheitAnzeigen = new JButton("Anwesenheit anzeigen");
        this.buttonEinsatzZeiten = new JButton("Einsatzzeiten anzeigen");
        this.buttonEinsatzListe = new JButton("Zur\u00fcck zur Einsatzliste");
        this.buttonEinsatzListe.setBackground(Color.CYAN);
        this.buttonFahrzeugBesetzung = new JButton("Fahrzeugbesetzung");
        this.mandant_label = new JLabel("Mandant: ");
        this.buttonOrganisationen = new JButton("Andere Organisationen");
        this.modulBeschreibung = new JLabel("Einsatz Liste " + SbcUtils.timeStamp((String)"yyyy"));
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        TabelleJahr tabJahr = new TabelleJahr();
        TabelleMandant tabMandant = new TabelleMandant();
        try {
            String[] option = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            String[] mandantListe = Utils.listToArrayOnlyFORComboBoxes(tabMandant.getAllMandanten());
            this.jahre = new JComboBox<String>(option);
            this.mandant = new JComboBox<String>(mandantListe);
            this.veranstaltungOption_label = new JLabel("Jahr: ");
            this.jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
            this.modulBeschreibung.setText("Einsatz Liste " + this.jahre.getSelectedItem().toString());
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.jahre.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    ((DefaultTableModel)EinsatzListeAO.this.table.getModel()).setDataVector(new TabelleEinsatz().getAllForList(EinsatzListeAO.this.jahre.getSelectedItem().toString(), new TabelleMandant().getMandantID(EinsatzListeAO.this.mandant.getSelectedItem().toString())), EinsatzListeAO.this.headname);
                    EinsatzListeAO.this.modulBeschreibung.setText("Einsatz Liste " + EinsatzListeAO.this.jahre.getSelectedItem().toString());
                    EinsatzListeAO.this.table.setEnabled(true);
                    EinsatzListeAO.this.buttonEinsatzListe.setVisible(false);
                    EinsatzListeAO.this.buttonFahrzeugBesetzung.setVisible(true);
                    EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setVisible(true);
                    EinsatzListeAO.this.buttonEinsatzZeiten.setVisible(true);
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
                    EinsatzListeAO.this.jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
                    if (runApplication.mandantName.equals(EinsatzListeAO.this.mandant.getSelectedItem())) {
                        EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setEnabled(true);
                        EinsatzListeAO.this.table.setEnabled(true);
                    } else {
                        EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setEnabled(false);
                        EinsatzListeAO.this.table.setEnabled(false);
                    }
                    ((DefaultTableModel)EinsatzListeAO.this.table.getModel()).setDataVector(new TabelleEinsatz().getAllForList(EinsatzListeAO.this.jahre.getSelectedItem().toString(), new TabelleMandant().getMandantID(EinsatzListeAO.this.mandant.getSelectedItem().toString())), EinsatzListeAO.this.headname);
                    if (runApplication.BF == 1) {
                        EinsatzListeAO.this.modulBeschreibung.setText("Einsatz Liste " + EinsatzListeAO.this.jahre.getSelectedItem().toString() + " (" + EinsatzListeAO.this.mandant.getSelectedItem() + ")");
                    } else {
                        EinsatzListeAO.this.modulBeschreibung.setText("Einsatz Liste " + EinsatzListeAO.this.jahre.getSelectedItem().toString());
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
        this.defaultTableModelEinsatz = new DefaultTableModel(10, 9);
        this.defaultTableModelEinsatz.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelEinsatz);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 550));
        this.table.setFillsViewportHeight(true);
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
        if (runApplication.BF == 1) {
            this.add(this.mandant_label);
            this.add(this.mandant);
        }
        this.add(this.veranstaltungOption_label);
        this.add(this.jahre);
        this.add(scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonAnwesenheitAnzeigen);
        this.add(this.buttonOrganisationen);
        this.add(this.buttonEinsatzZeiten);
        this.add(this.buttonEinsatzListe);
        this.add(this.buttonFahrzeugBesetzung);
        this.add(this.buttonZurueck);
        this.buttonEinsatzListe.setVisible(false);
        this.mandant.setSelectedItem(runApplication.mandantName);
        if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1")) {
            this.buttonOrganisationen.setVisible(true);
        } else {
            this.buttonOrganisationen.setVisible(false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = EinsatzListeAO.this.modulBeschreibung.getText();
                Utils.printJTable(headerText, EinsatzListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonAnwesenheitAnzeigen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzListeAO.this.table.setEnabled(false);
                int[] rows = EinsatzListeAO.this.table.getSelectedRows();
                if (rows.length >= 2) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    EinsatzListeAO.this.table.setEnabled(true);
                } else {
                    try {
                        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int[] vIDs = Utils.listToIntArray(tabEinsatz.getAllVeranstaltungsIDsForList(EinsatzListeAO.this.jahre.getSelectedItem().toString()));
                        ((DefaultTableModel)EinsatzListeAO.this.table.getModel()).setDataVector(tabAnwesenheit.getAnwesendeMitgliederEinerVeranstaltung(vIDs[rows[0]]), EinsatzListeAO.this.headnameAnwesenheit);
                        EinsatzListeAO.this.buttonEinsatzListe.setVisible(true);
                        EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setVisible(false);
                        EinsatzListeAO.this.buttonEinsatzZeiten.setVisible(false);
                        EinsatzListeAO.this.buttonOrganisationen.setVisible(false);
                        EinsatzListeAO.this.buttonFahrzeugBesetzung.setVisible(false);
                        EinsatzListeAO.this.buttonZurueck.setVisible(false);
                        logging.logInfo((Object)("Zeige Mitglieder der Veranstaltung: " + vIDs[rows[0]]));
                        EinsatzListeAO.this.modulBeschreibung.setText("Anwesenheit - " + tabVeranstaltung.getVeranstaltungName(vIDs[rows[0]]));
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                    catch (ArrayIndexOutOfBoundsException e1) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                        EinsatzListeAO.this.table.setEnabled(true);
                    }
                }
            }
        });
        this.buttonOrganisationen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzListeAO.this.table.setEnabled(false);
                int[] rows = EinsatzListeAO.this.table.getSelectedRows();
                if (rows.length >= 2) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    EinsatzListeAO.this.table.setEnabled(true);
                } else {
                    try {
                        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                        TabelleEinsatz_organisationen tabEinsatz_organisationen = new TabelleEinsatz_organisationen();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int[] vIDs = Utils.listToIntArray(tabEinsatz.getAllVeranstaltungsIDsForList(EinsatzListeAO.this.jahre.getSelectedItem().toString()));
                        ((DefaultTableModel)EinsatzListeAO.this.table.getModel()).setDataVector(tabEinsatz_organisationen.getAnwesendeOrganisationen(vIDs[rows[0]]), EinsatzListeAO.this.headnameOrganisationen);
                        EinsatzListeAO.this.buttonEinsatzListe.setVisible(true);
                        EinsatzListeAO.this.buttonOrganisationen.setVisible(false);
                        EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setVisible(false);
                        EinsatzListeAO.this.buttonEinsatzZeiten.setVisible(false);
                        EinsatzListeAO.this.buttonFahrzeugBesetzung.setVisible(false);
                        EinsatzListeAO.this.buttonZurueck.setVisible(false);
                        logging.logInfo((Object)("Zeige Organisationen der Veranstaltung: " + vIDs[rows[0]]));
                        EinsatzListeAO.this.modulBeschreibung.setText("Weitere Einheiten / Organisationen - " + tabVeranstaltung.getVeranstaltungName(vIDs[rows[0]]));
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                    catch (ArrayIndexOutOfBoundsException e1) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                        EinsatzListeAO.this.table.setEnabled(true);
                    }
                }
            }
        });
        this.buttonEinsatzZeiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzListeAO.this.table.setEnabled(false);
                int[] rows = EinsatzListeAO.this.table.getSelectedRows();
                if (rows.length >= 2) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    EinsatzListeAO.this.table.setEnabled(true);
                } else {
                    try {
                        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                        TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int[] vIDs = Utils.listToIntArray(tabEinsatz.getAllVeranstaltungsIDsForList(EinsatzListeAO.this.jahre.getSelectedItem().toString()));
                        ((DefaultTableModel)EinsatzListeAO.this.table.getModel()).setDataVector(tabZeiten.getZeitenForTabelle(vIDs[rows[0]]), EinsatzListeAO.this.headnameEinsatzzeiten);
                        EinsatzListeAO.this.buttonEinsatzListe.setVisible(true);
                        EinsatzListeAO.this.buttonEinsatzZeiten.setVisible(false);
                        EinsatzListeAO.this.buttonFahrzeugBesetzung.setVisible(false);
                        EinsatzListeAO.this.buttonOrganisationen.setVisible(false);
                        EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setVisible(false);
                        EinsatzListeAO.this.buttonZurueck.setVisible(false);
                        logging.logInfo((Object)("Zeige EinsatzZeiten der Veranstaltung: " + vIDs[rows[0]]));
                        EinsatzListeAO.this.modulBeschreibung.setText("Fahrzeuge / Einsatzzeiten - " + tabVeranstaltung.getVeranstaltungName(vIDs[rows[0]]));
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                    catch (ArrayIndexOutOfBoundsException e1) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                        EinsatzListeAO.this.table.setEnabled(true);
                    }
                }
            }
        });
        this.buttonFahrzeugBesetzung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzListeAO.this.table.setEnabled(false);
                int[] rows = EinsatzListeAO.this.table.getSelectedRows();
                if (rows.length >= 2) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    EinsatzListeAO.this.table.setEnabled(true);
                } else {
                    try {
                        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                        TabelleFahrzeugeinteilung tabFahrzeugeinteilung = new TabelleFahrzeugeinteilung();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int[] vIDs = Utils.listToIntArray(tabEinsatz.getAllVeranstaltungsIDsForList(EinsatzListeAO.this.jahre.getSelectedItem().toString()));
                        ((DefaultTableModel)EinsatzListeAO.this.table.getModel()).setDataVector(tabFahrzeugeinteilung.getFahrzeugBesatzungForTable(vIDs[rows[0]]), EinsatzListeAO.this.headnameFahrzeugBesetzung);
                        EinsatzListeAO.this.buttonEinsatzListe.setVisible(true);
                        EinsatzListeAO.this.buttonEinsatzZeiten.setVisible(false);
                        EinsatzListeAO.this.buttonFahrzeugBesetzung.setVisible(false);
                        EinsatzListeAO.this.buttonOrganisationen.setVisible(false);
                        EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setVisible(false);
                        EinsatzListeAO.this.buttonZurueck.setVisible(false);
                        logging.logInfo((Object)("Zeige Fahrzeugbesatzung der Veranstaltung: " + vIDs[rows[0]]));
                        EinsatzListeAO.this.modulBeschreibung.setText("Fahrzeugebesatzung - " + tabVeranstaltung.getVeranstaltungName(vIDs[rows[0]]));
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                    catch (ArrayIndexOutOfBoundsException e1) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                        EinsatzListeAO.this.table.setEnabled(true);
                    }
                }
            }
        });
        this.buttonEinsatzListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzListeAO.this.table.setEnabled(true);
                EinsatzListeAO.this.buttonEinsatzListe.setVisible(false);
                EinsatzListeAO.this.buttonFahrzeugBesetzung.setVisible(true);
                EinsatzListeAO.this.buttonEinsatzZeiten.setVisible(true);
                EinsatzListeAO.this.buttonAnwesenheitAnzeigen.setVisible(true);
                EinsatzListeAO.this.buttonOrganisationen.setVisible(true);
                EinsatzListeAO.this.buttonZurueck.setVisible(true);
                EinsatzListeAO.this.modulBeschreibung.setText("Einsatz Liste " + EinsatzListeAO.this.jahre.getSelectedItem().toString());
                try {
                    ((DefaultTableModel)EinsatzListeAO.this.table.getModel()).setDataVector(new TabelleEinsatz().getAllForList(EinsatzListeAO.this.jahre.getSelectedItem().toString(), new TabelleMandant().getMandantID(EinsatzListeAO.this.mandant.getSelectedItem().toString())), EinsatzListeAO.this.headname);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzListeAO.this.chooser.setFileSelectionMode(1);
                EinsatzListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + EinsatzListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = EinsatzListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(EinsatzListeAO.this.table, new File(outputOrdner), "Einsatz");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleEinsatz().getAllForList(SbcUtils.timeStamp((String)"yyyy"), Integer.parseInt(runApplication.PROPERTIES.get("MandantID"))), this.headname);
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
        MyEvent.setEvent((String)"0x0030");
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

