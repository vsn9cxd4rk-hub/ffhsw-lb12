/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.abrechnung;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.abrechnung.TabelleAbrechnung;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.AbrechnungService;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class AbrechnungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static int[] IDLISTE = null;
    private JButton buttonZurueck;
    private JButton buttonArtikel;
    private JButton buttonArtikelBearbeiten;
    private JButton buttonTabelleDrucken;
    private JButton buttonCsvExport;
    private JButton buttonManuelleVerbuchung;
    private JButton buttonMassenverbuchung;
    private JButton buttonKontoAnlegen;
    public static JButton buttonAbrechnen;
    private JButton buttonAnsehen;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private DefaultTableModel defaultTableModelListe;
    public static JTable table;
    private JScrollPane scrollpaneListe;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JFileChooser chooser;
    public static Vector<String> headname;
    public static Vector<String> headnameKonto;
    public static Vector<String> headnameAbrechnung;
    private Vector<String> headnameBilanz = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Zahlungsart");
            this.add("Wert in \u20ac");
        }
    };

    static {
        headname = new Vector<String>(){
            private static final long serialVersionUID = 1L;
            {
                this.add("Artikel ID");
                this.add("Artikel Name");
                this.add("Veranstaltung");
                this.add("Kategorie");
                this.add("Menge");
                this.add("Wert in \u20ac");
                this.add("Zahlungsart");
                this.add("Status");
            }
        };
        headnameKonto = new Vector<String>(){
            private static final long serialVersionUID = 1L;
            {
                this.add("Name");
                this.add("Artikel ID");
                this.add("Artikel Name");
                this.add("Veranstaltung");
                this.add("Menge");
                this.add("Wert in \u20ac");
                this.add("Zahlungsart");
                this.add("Status");
            }
        };
        headnameAbrechnung = new Vector<String>(){
            private static final long serialVersionUID = 1L;
            {
                this.add("Name");
                this.add("Artikel ID");
                this.add("Artikel Name");
                this.add("Veranstaltung");
                this.add("Menge");
                this.add("Wert in \u20ac");
                this.add("Zahlungsart");
            }
        };
    }

    public AbrechnungAO() {
        super("FeuerwehrManagementSystem - Abrechnung");
        logging.logInfo((Object)"Starte: AbrechnungAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonArtikel = new JButton("Artikel anlegen");
        this.buttonArtikelBearbeiten = new JButton("Artikel bearbeiten");
        this.buttonTabelleDrucken = new JButton("Drucken");
        this.buttonCsvExport = new JButton("CSV Export");
        this.buttonManuelleVerbuchung = new JButton("Manuelle Verbuchung");
        this.buttonMassenverbuchung = new JButton("Verbuchung");
        this.buttonKontoAnlegen = new JButton("Konto anlegen");
        buttonAbrechnen = new JButton("Abrechnen");
        buttonAbrechnen.setToolTipText("Hiermit werden alle markierten Vorg\u00e4nge abgerechnet bzw. geschlossen");
        this.buttonAnsehen = new JButton("Abrechnung ansehen");
        tree = new JTree(CreateTrees.CreateTreeAbrechnung());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        this.chooser = new JFileChooser();
        this.modulBeschreibung = new JLabel("Abrechnung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
        this.defaultTableModelListe = new DefaultTableModel(10, 9);
        this.defaultTableModelListe.setColumnIdentifiers(headname);
        table = new JTable(this.defaultTableModelListe);
        table.setPreferredScrollableViewportSize(new Dimension(850, 500));
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        this.scrollpaneListe = new JScrollPane(table);
        this.scrollpaneListe.setVerticalScrollBarPolicy(22);
        table.addMouseListener(new MouseAdapter(){

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
        this.setTitle("FeuerwehrManagementSystem - Abrechnung");
        this.setSize(1280, 730);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonKontoAnlegen);
        this.add(this.buttonManuelleVerbuchung);
        this.add(this.buttonMassenverbuchung);
        this.add(this.buttonArtikel);
        this.add(this.buttonArtikelBearbeiten);
        this.add(buttonAbrechnen);
        this.add(this.dummy2);
        this.scrollPaneTree.setPreferredSize(new Dimension(350, 525));
        this.add(this.scrollPaneTree);
        this.add(this.scrollpaneListe);
        this.add(this.dummy3);
        this.add(this.buttonZurueck);
        this.add(this.buttonCsvExport);
        this.add(this.buttonTabelleDrucken);
        this.add(this.buttonAnsehen);
        if (BerechtigunsManager.ber[71] == 0) {
            this.buttonArtikel.setEnabled(false);
            this.buttonArtikelBearbeiten.setEnabled(false);
        }
        if (BerechtigunsManager.ber[72] == 0) {
            this.buttonKontoAnlegen.setEnabled(false);
        }
        if (BerechtigunsManager.ber[73] == 0) {
            this.buttonManuelleVerbuchung.setEnabled(false);
        }
        this.buttonAnsehen.setVisible(false);
    }

    protected void boxenHinzufuegen() {
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                try {
                    TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
                    Object[] auswahl = tree.getSelectionPath().getPath();
                    buttonAbrechnen.setVisible(true);
                    AbrechnungAO.this.buttonAnsehen.setVisible(false);
                    if (auswahl[1].toString().equals("Konten")) {
                        ((DefaultTableModel)table.getModel()).setDataVector(tabAbrechnung.getAllAbrechnungenByKonto(tree.getSelectionPath().getLastPathComponent().toString()), headnameKonto);
                        IDLISTE = Utils.listToIntArray(tabAbrechnung.getIDArrayKonto(tree.getSelectionPath().getLastPathComponent().toString()));
                    } else if (auswahl[1].toString().equals("Abrechnungen")) {
                        ((DefaultTableModel)table.getModel()).setDataVector(tabAbrechnung.getAllAbrechnungenByAbrechnung(Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString())), headnameAbrechnung);
                        buttonAbrechnen.setVisible(false);
                        AbrechnungAO.this.buttonAnsehen.setVisible(true);
                    } else if (auswahl[1].toString().equals("Mitglieder")) {
                        int mID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                        ((DefaultTableModel)table.getModel()).setDataVector(new TabelleAbrechnung().getAllAbrechnungenByMitglied(mID), headname);
                        IDLISTE = Utils.listToIntArray(tabAbrechnung.getIDArrayMitglieder(mID));
                    } else if (auswahl[1].toString().equals("Kontobilanz")) {
                        ((DefaultTableModel)table.getModel()).setDataVector(AbrechnungService.getAllKontobilanzen(tree.getSelectionPath().getLastPathComponent().toString()), AbrechnungAO.this.headnameBilanz);
                        buttonAbrechnen.setVisible(false);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NumberFormatException numberFormatException) {
                }
                catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
            }
        });
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonTabelleDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String headerText = "Abrechnung - " + tree.getSelectionPath().getLastPathComponent().toString();
                Utils.printJTable(headerText, table, OrientationRequested.PORTRAIT, true, true);
            }
        });
        this.buttonArtikel.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ABRECHNUNG_ARTIKEL);
                Steuerung.steuerung();
            }
        });
        this.buttonArtikelBearbeiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0300");
                Steuerung.setStatus(Status.ABRECHNUNG_ARTIKEL);
                Steuerung.steuerung();
            }
        });
        this.buttonManuelleVerbuchung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MANUELLE_VERBUCHUNG);
                Steuerung.steuerung();
            }
        });
        this.buttonMassenverbuchung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MASSENVERBUCHUNG);
                Steuerung.steuerung();
            }
        });
        this.buttonKontoAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.KONTO_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        buttonAbrechnen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
                    int[] rows = table.getSelectedRows();
                    int[] abzurechnendeIds = new int[rows.length];
                    int statusCount = 0;
                    int r = 0;
                    while (r < rows.length) {
                        abzurechnendeIds[r] = IDLISTE[rows[r]];
                        if (tabAbrechnung.getStatus(IDLISTE[rows[r]]) != 0) {
                            ++statusCount;
                        }
                        ++r;
                    }
                    if (statusCount != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.ABRECHNUNG_IST_BEREITS_ABGERECHNET, "Fehlermeldung", 2);
                    } else {
                        AbrechnungService.rechneVorgangAb(abzurechnendeIds);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        Object[] auswahl = tree.getSelectionPath().getPath();
                        if (auswahl[1].toString().equals("Konten")) {
                            ((DefaultTableModel)table.getModel()).setDataVector(tabAbrechnung.getAllAbrechnungenByKonto(tree.getSelectionPath().getLastPathComponent().toString()), headnameKonto);
                            IDLISTE = Utils.listToIntArray(tabAbrechnung.getIDArrayKonto(tree.getSelectionPath().getLastPathComponent().toString()));
                        } else if (auswahl[1].toString().equals("Mitglieder")) {
                            int mID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                            ((DefaultTableModel)table.getModel()).setDataVector(tabAbrechnung.getAllAbrechnungenByMitglied(mID), headname);
                            IDLISTE = Utils.listToIntArray(tabAbrechnung.getIDArrayMitglieder(mID));
                        }
                        tree.setModel(CreateTrees.CreateTreeAbrechnung());
                    }
                }
                catch (DocumentException | IOException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAnsehen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (tree.getSelectionPath().getLastPathComponent().toString().equals("0")) {
                        JOptionPane.showMessageDialog(null, Konstante.ABRECHNUNG_0, "Warnung", 2);
                    } else {
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Abrechnung/ABR_" + tree.getSelectionPath().getLastPathComponent().toString() + ".pdf";
                        Desktop.getDesktop().open(new File(dateiname));
                    }
                }
                catch (IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AbrechnungAO.this.chooser.setFileSelectionMode(1);
                AbrechnungAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + AbrechnungAO.this.chooser.getSelectedFile().getPath()));
                String outputFile = AbrechnungAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(table, new File(outputFile), "Abrechnung");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
    }

    public void fensterAnzeigen() {
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

