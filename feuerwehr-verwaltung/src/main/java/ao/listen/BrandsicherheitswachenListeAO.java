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
import data.tabellen.TabelleBrandsicherheitswache;
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

public class BrandsicherheitswachenListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonAnwesenheitAnzeigen;
    private JButton buttonBSWListe;
    private JButton buttonCsvExport;
    private DefaultTableModel defaultTableModelBSW;
    public JTable table;
    private JComboBox<String> jahre;
    private JLabel veranstaltungOption_label;
    private JComboBox<String> mandant;
    private JLabel mandant_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JFileChooser chooser;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("BSW-Nummer");
            this.add("Ort");
            this.add("Art");
            this.add("Datum");
            this.add("Treffen BSW");
            this.add("Teilnehmer");
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

    public BrandsicherheitswachenListeAO() {
        super("FeuerwehrManagementSystem - Brandsicherheitswchen Liste");
        logging.logInfo((Object)"Starte: BrandsicherheitswachenListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonAnwesenheitAnzeigen = new JButton("Anwesenheit anzeigen");
        this.buttonBSWListe = new JButton("Zur\u00fcck zur BSW-Liste");
        this.buttonBSWListe.setBackground(Color.CYAN);
        this.mandant_label = new JLabel("Mandant: ");
        this.modulBeschreibung = new JLabel("Brandsicherheitswachen Liste " + SbcUtils.timeStamp((String)"yyyy"));
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        try {
            TabelleJahr tabJahr = new TabelleJahr();
            TabelleMandant tabMandant = new TabelleMandant();
            String[] option = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            String[] mandantListe = Utils.listToArrayOnlyFORComboBoxes(tabMandant.getAllMandanten());
            this.jahre = new JComboBox<String>(option);
            this.mandant = new JComboBox<String>(mandantListe);
            this.veranstaltungOption_label = new JLabel("Jahr: ");
            this.jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.jahre.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    ((DefaultTableModel)BrandsicherheitswachenListeAO.this.table.getModel()).setDataVector(new TabelleBrandsicherheitswache().getAllForTable(BrandsicherheitswachenListeAO.this.jahre.getSelectedItem().toString(), new TabelleMandant().getMandantID(BrandsicherheitswachenListeAO.this.mandant.getSelectedItem().toString())), BrandsicherheitswachenListeAO.this.headname);
                    BrandsicherheitswachenListeAO.this.modulBeschreibung.setText("BSW Liste " + BrandsicherheitswachenListeAO.this.jahre.getSelectedItem().toString());
                    BrandsicherheitswachenListeAO.this.table.setEnabled(true);
                    BrandsicherheitswachenListeAO.this.buttonAnwesenheitAnzeigen.setVisible(true);
                    BrandsicherheitswachenListeAO.this.buttonBSWListe.setVisible(false);
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
                    BrandsicherheitswachenListeAO.this.jahre.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
                    if (runApplication.mandantName.equals(BrandsicherheitswachenListeAO.this.mandant.getSelectedItem())) {
                        BrandsicherheitswachenListeAO.this.buttonAnwesenheitAnzeigen.setEnabled(true);
                        BrandsicherheitswachenListeAO.this.table.setEnabled(true);
                    } else {
                        BrandsicherheitswachenListeAO.this.buttonAnwesenheitAnzeigen.setEnabled(false);
                        BrandsicherheitswachenListeAO.this.table.setEnabled(false);
                    }
                    ((DefaultTableModel)BrandsicherheitswachenListeAO.this.table.getModel()).setDataVector(new TabelleBrandsicherheitswache().getAllForTable(BrandsicherheitswachenListeAO.this.jahre.getSelectedItem().toString(), new TabelleMandant().getMandantID(BrandsicherheitswachenListeAO.this.mandant.getSelectedItem().toString())), BrandsicherheitswachenListeAO.this.headname);
                    if (runApplication.BF == 1) {
                        BrandsicherheitswachenListeAO.this.modulBeschreibung.setText("Brandsicherheitswachen Liste " + BrandsicherheitswachenListeAO.this.jahre.getSelectedItem().toString() + " (" + BrandsicherheitswachenListeAO.this.mandant.getSelectedItem() + ")");
                    } else {
                        BrandsicherheitswachenListeAO.this.modulBeschreibung.setText("Brandsicherheitswachen Liste " + BrandsicherheitswachenListeAO.this.jahre.getSelectedItem().toString());
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
        this.defaultTableModelBSW = new DefaultTableModel(10, 9);
        this.defaultTableModelBSW.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelBSW);
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
        this.setTitle("FeuerwehrManagementSystem - Brandsicherheitswchen Liste");
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
        this.add(this.buttonBSWListe);
        this.add(this.buttonZurueck);
        this.buttonBSWListe.setVisible(false);
        this.mandant.setSelectedItem(runApplication.mandantName);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = BrandsicherheitswachenListeAO.this.modulBeschreibung.getText();
                Utils.printJTable(headerText, BrandsicherheitswachenListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
            }
        });
        this.buttonAnwesenheitAnzeigen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BrandsicherheitswachenListeAO.this.table.setEnabled(false);
                int[] rows = BrandsicherheitswachenListeAO.this.table.getSelectedRows();
                if (rows.length >= 2) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    BrandsicherheitswachenListeAO.this.table.setEnabled(true);
                } else {
                    try {
                        TabelleBrandsicherheitswache tabBSW = new TabelleBrandsicherheitswache();
                        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int[] vIDs = Utils.listToIntArray(tabBSW.getAllVeranstaltungsIDsForList(BrandsicherheitswachenListeAO.this.jahre.getSelectedItem().toString()));
                        ((DefaultTableModel)BrandsicherheitswachenListeAO.this.table.getModel()).setDataVector(tabAnwesenheit.getAnwesendeMitgliederEinerVeranstaltung(vIDs[rows[0]]), BrandsicherheitswachenListeAO.this.headnameAnwesenheit);
                        BrandsicherheitswachenListeAO.this.buttonBSWListe.setVisible(true);
                        BrandsicherheitswachenListeAO.this.buttonAnwesenheitAnzeigen.setVisible(false);
                        BrandsicherheitswachenListeAO.this.buttonZurueck.setVisible(false);
                        logging.logInfo((Object)("Zeige Mitglieder der Veranstaltung: " + vIDs[rows[0]]));
                        BrandsicherheitswachenListeAO.this.modulBeschreibung.setText("Anwesenheit - " + tabVeranstaltung.getVeranstaltungName(vIDs[rows[0]]));
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                    catch (ArrayIndexOutOfBoundsException e1) {
                        BrandsicherheitswachenListeAO.this.table.setEnabled(true);
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    }
                }
            }
        });
        this.buttonBSWListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BrandsicherheitswachenListeAO.this.table.setEnabled(true);
                BrandsicherheitswachenListeAO.this.buttonBSWListe.setVisible(false);
                BrandsicherheitswachenListeAO.this.buttonAnwesenheitAnzeigen.setVisible(true);
                BrandsicherheitswachenListeAO.this.buttonZurueck.setVisible(true);
                BrandsicherheitswachenListeAO.this.modulBeschreibung.setText("Brandsicherheitswachen Liste " + SbcUtils.timeStamp((String)"yyyy"));
                try {
                    ((DefaultTableModel)BrandsicherheitswachenListeAO.this.table.getModel()).setDataVector(new TabelleBrandsicherheitswache().getAllForTable(SbcUtils.timeStamp((String)"yyyy"), new TabelleMandant().getMandantID(BrandsicherheitswachenListeAO.this.mandant.getSelectedItem().toString())), BrandsicherheitswachenListeAO.this.headname);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BrandsicherheitswachenListeAO.this.chooser.setFileSelectionMode(1);
                BrandsicherheitswachenListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + BrandsicherheitswachenListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = BrandsicherheitswachenListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(BrandsicherheitswachenListeAO.this.table, new File(outputOrdner), "BSW");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleBrandsicherheitswache().getAllForTable(SbcUtils.timeStamp((String)"yyyy"), Integer.parseInt(runApplication.PROPERTIES.get("MandantID"))), this.headname);
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

