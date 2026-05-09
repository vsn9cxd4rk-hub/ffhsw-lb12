/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.listen;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.einstellungen.TabelleJahr;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
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
import pdfdocumente.PDFVeranstaltungTeilnahmen;
import run.runApplication;
import service.BerechtigunsManager;
import service.EMailService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class VeranstaltungListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonDrucken;
    private JButton buttonDrucken2;
    private JButton buttonAnwesenheitAnzeigen;
    private JButton buttonAnwesenheitNachtragen;
    private JButton buttonNachrichtSenden;
    private JButton buttonIcsExport;
    private JButton buttonZurueckZurVeranstaltungsUebersicht;
    private JButton buttonTeilnehmerliste;
    private JButton buttonCsvExport;
    private JButton buttonEMailNotification;
    private DefaultTableModel defaultTableModelLehrgang;
    public JTable table;
    private JComboBox<String> veranstaltungOption;
    private JLabel veranstaltungOption_label;
    private JComboBox<String> veranstaltungKategorie;
    private JLabel veranstaltungKategorie_label;
    private JFileChooser chooser;
    String datumVon = null;
    String datumBis = null;
    String ausgewaehlteVeranstaltung = "<bitte w\u00e4hlen>";
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Datum");
            this.add("Uhrzeit");
            this.add("Veranstaltungsname");
        }
    };
    private Vector<String> headnameAnwesenheit = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Dienstgrad");
            this.add("Name");
            this.add("Vorname");
        }
    };

    public VeranstaltungListeAO() {
        super("FeuerwehrManagementSystem - Veranstaltungsliste");
        logging.logInfo((Object)"Starte: VeranstaltungListeAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonDrucken2 = new JButton("PDF-Druck");
        this.buttonDrucken2.setToolTipText("Tabelle Drucken mit Briefkopf");
        this.buttonAnwesenheitAnzeigen = new JButton("Anwesenheit anzeigen");
        this.buttonZurueckZurVeranstaltungsUebersicht = new JButton("Veranstaltungs\u00fcbersicht");
        this.buttonAnwesenheitNachtragen = new JButton("Anwesenheit eintragen");
        this.buttonNachrichtSenden = new JButton("E-Mail Erinnerung");
        this.buttonNachrichtSenden.setToolTipText("Benachrichtigung an die Mitglieder senden, die an der Veranstaltung Teilnehmen");
        this.buttonTeilnehmerliste = new JButton("Teilnehmerliste drucken");
        this.buttonTeilnehmerliste.setToolTipText("Erstellt eine Teilnehmerliste von der Aktuelle Veranstaltungskategorie");
        this.buttonEMailNotification = new JButton("E-Mail senden");
        this.buttonEMailNotification.setToolTipText("Senden von Veranstaltungen in einem Zeitraum an alle Mitglieder...");
        this.buttonIcsExport = new JButton("ICS-Export");
        this.buttonIcsExport.setToolTipText("Export der Veranstaltungsdaten als Kalender z.B. f\u00fcr Outlook, Google, usw.");
        this.modulBeschreibung = new JLabel("Veranstaltungsliste");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonCsvExport = new JButton("CSV Export");
        this.chooser = new JFileChooser();
    }

    protected void labelErstellen() {
        String[] option = new String[]{"Abgelaufende Veranstaltungen", "Kommende Veranstaltungen", "Veranstaltungen n\u00e4chstes Jahr"};
        this.veranstaltungOption = new JComboBox<String>(option);
        this.veranstaltungOption_label = new JLabel("Optionen: ");
        this.veranstaltungOption.setSelectedItem("Kommende Veranstaltungen");
        this.veranstaltungKategorie_label = new JLabel("Veranstaltungskategorie: ");
        try {
            TabelleJahr tabJahr = new TabelleJahr();
            TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
            String[] jahre = Utils.listToArray(tabJahr.getAllVerf\u00fcgbarenJahre());
            String[] kategorieListe = Utils.listToArray(tabKategorie.getAllKategorien());
            int i = 0;
            while (i < jahre.length) {
                this.veranstaltungOption.addItem("Alle Veranstaltungen " + jahre[i]);
                ++i;
            }
            this.veranstaltungKategorie = new JComboBox<String>(kategorieListe);
            this.veranstaltungKategorie.removeItem("Einsatz");
            this.veranstaltungKategorie.addItem("Alle");
            this.veranstaltungKategorie.setSelectedItem("Alle");
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.veranstaltungOption.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                VeranstaltungListeAO.this.buttonZurueckZurVeranstaltungsUebersicht.setVisible(false);
                VeranstaltungListeAO.this.buttonAnwesenheitNachtragen.setVisible(false);
                VeranstaltungListeAO.this.ausgewaehlteVeranstaltung = "<bitte w\u00e4hlen>";
                VeranstaltungListeAO.this.table.setEnabled(true);
                VeranstaltungListeAO.this.buttonNachrichtSenden.setVisible(false);
                VeranstaltungListeAO.this.buttonAnwesenheitAnzeigen.setVisible(true);
                if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Abgelaufende Veranstaltungen")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = SbcUtils.timeStamp((String)"yyyy-MM-dd");
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Kommende Veranstaltungen")) {
                    VeranstaltungListeAO.this.datumVon = SbcUtils.timeStamp((String)"yyyy-MM-dd");
                    VeranstaltungListeAO.this.datumBis = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-12-31";
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().startsWith("Alle Veranstaltungen ")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().substring(21, 25)) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = String.valueOf(VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().substring(21, 25)) + "-12-31";
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Veranstaltungen n\u00e4chstes Jahr")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1)) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = String.valueOf(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1)) + "-12-31";
                }
                try {
                    if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Kommende Veranstaltungen") && !VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString().equals("Alle")) {
                        VeranstaltungListeAO.this.buttonTeilnehmerliste.setVisible(true);
                    } else {
                        VeranstaltungListeAO.this.buttonTeilnehmerliste.setVisible(false);
                    }
                    int kID = VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString().equals("Alle") ? 0 : new TabelleVeranstaltung_Kategorie().getID(VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                    ((DefaultTableModel)VeranstaltungListeAO.this.table.getModel()).setDataVector(new TabelleVeranstaltung().getAllVeranstaltungForTable(VeranstaltungListeAO.this.datumVon, VeranstaltungListeAO.this.datumBis, kID), VeranstaltungListeAO.this.headname);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.veranstaltungKategorie.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                VeranstaltungListeAO.this.buttonZurueckZurVeranstaltungsUebersicht.setVisible(false);
                VeranstaltungListeAO.this.buttonAnwesenheitNachtragen.setVisible(false);
                VeranstaltungListeAO.this.ausgewaehlteVeranstaltung = "<bitte w\u00e4hlen>";
                VeranstaltungListeAO.this.table.setEnabled(true);
                VeranstaltungListeAO.this.buttonNachrichtSenden.setVisible(false);
                if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Abgelaufende Veranstaltungen")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = SbcUtils.timeStamp((String)"yyyy-MM-dd");
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Kommende Veranstaltungen")) {
                    VeranstaltungListeAO.this.datumVon = SbcUtils.timeStamp((String)"yyyy-MM-dd");
                    VeranstaltungListeAO.this.datumBis = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-12-31";
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().startsWith("Alle Veranstaltungen ")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().substring(21, 25)) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = String.valueOf(VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().substring(21, 25)) + "-12-31";
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Veranstaltungen n\u00e4chstes Jahr")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1)) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = String.valueOf(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1)) + "-12-31";
                }
                try {
                    int kID;
                    if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Kommende Veranstaltungen") && !VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString().equals("Alle")) {
                        VeranstaltungListeAO.this.buttonTeilnehmerliste.setVisible(true);
                    } else {
                        VeranstaltungListeAO.this.buttonTeilnehmerliste.setVisible(false);
                    }
                    if (VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString().equals("Alle")) {
                        kID = 0;
                        VeranstaltungListeAO.this.modulBeschreibung.setText("Veranstaltungsliste");
                    } else {
                        kID = new TabelleVeranstaltung_Kategorie().getID(VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                        VeranstaltungListeAO.this.modulBeschreibung.setText("Veranstaltungsliste - " + VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                    }
                    ((DefaultTableModel)VeranstaltungListeAO.this.table.getModel()).setDataVector(new TabelleVeranstaltung().getAllVeranstaltungForTable(VeranstaltungListeAO.this.datumVon, VeranstaltungListeAO.this.datumBis, kID), VeranstaltungListeAO.this.headname);
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
        this.defaultTableModelLehrgang = new DefaultTableModel(5, 4);
        this.defaultTableModelLehrgang.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelLehrgang);
        this.table.setPreferredScrollableViewportSize(new Dimension(1100, 580));
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
        this.setTitle("FeuerwehrManagementSystem - Lehrgang Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.veranstaltungOption_label);
        this.add(this.veranstaltungOption);
        this.add(this.veranstaltungKategorie_label);
        this.add(this.veranstaltungKategorie);
        this.add(scrollpane);
        this.add(this.dummy2);
        this.add(this.buttonCsvExport);
        this.add(this.buttonIcsExport);
        this.add(this.buttonDrucken);
        this.add(this.buttonTeilnehmerliste);
        this.add(this.buttonAnwesenheitAnzeigen);
        this.add(this.buttonAnwesenheitNachtragen);
        this.add(this.buttonNachrichtSenden);
        this.add(this.buttonZurueckZurVeranstaltungsUebersicht);
        this.add(this.buttonEMailNotification);
        this.add(this.buttonZurueck);
        this.buttonZurueckZurVeranstaltungsUebersicht.setVisible(false);
        this.buttonAnwesenheitNachtragen.setVisible(false);
        this.buttonNachrichtSenden.setVisible(false);
        this.buttonTeilnehmerliste.setVisible(false);
        if (runApplication.EINSTELLUNGEN.get("emailModul").equals("0")) {
            this.buttonEMailNotification.setVisible(false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonTeilnehmerliste.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    logging.logInfo((Object)"Erstelle Teilnehmerliste...");
                    String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/Temp/Teilnehmerliste_" + VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString() + ".pdf";
                    PDFVeranstaltungTeilnahmen.PDFdocumentErstellen(dateiname, VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                    Desktop.getDesktop().open(new File(dateiname));
                }
                catch (DocumentException | IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonEMailNotification.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.VERANSTALTUNG_NOTIFICATON);
                Steuerung.steuerung();
            }
        });
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (VeranstaltungListeAO.this.modulBeschreibung.getText().startsWith("Anwesenheit")) {
                    Utils.printJTable(VeranstaltungListeAO.this.modulBeschreibung.getText(), VeranstaltungListeAO.this.table, OrientationRequested.LANDSCAPE, true, true);
                } else {
                    Utils.printJTable(VeranstaltungListeAO.this.modulBeschreibung.getText(), VeranstaltungListeAO.this.table, OrientationRequested.PORTRAIT, true, true);
                }
            }
        });
        this.buttonAnwesenheitAnzeigen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                VeranstaltungListeAO.this.table.setEnabled(false);
                int[] rows = VeranstaltungListeAO.this.table.getSelectedRows();
                if (rows.length >= 2) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    VeranstaltungListeAO.this.table.setEnabled(true);
                } else {
                    try {
                        TabelleVeranstaltung tabVeranstalung = new TabelleVeranstaltung();
                        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                        int kID = VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString().equals("Alle") ? 0 : new TabelleVeranstaltung_Kategorie().getID(VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                        String[] liste = Utils.listToArray(tabVeranstalung.getAllVeranstaltungForTableListe(VeranstaltungListeAO.this.datumVon, VeranstaltungListeAO.this.datumBis, kID));
                        int vID = tabVeranstalung.getVeranstaltungID(liste[rows[0]]);
                        VeranstaltungListeAO.this.ausgewaehlteVeranstaltung = liste[rows[0]];
                        ((DefaultTableModel)VeranstaltungListeAO.this.table.getModel()).setDataVector(tabAnwesenheit.getAnwesendeMitgliederEinerVeranstaltung(vID), VeranstaltungListeAO.this.headnameAnwesenheit);
                        if (runApplication.EINSTELLUNGEN.get("emailModul").equals("1")) {
                            if (BerechtigunsManager.ber[55] == 1) {
                                VeranstaltungListeAO.this.buttonNachrichtSenden.setVisible(true);
                                VeranstaltungListeAO.this.buttonNachrichtSenden.setEnabled(true);
                            } else {
                                VeranstaltungListeAO.this.buttonNachrichtSenden.setVisible(true);
                                VeranstaltungListeAO.this.buttonNachrichtSenden.setEnabled(false);
                            }
                        }
                        VeranstaltungListeAO.this.modulBeschreibung.setText("Anwesenheit - " + tabVeranstalung.getVeranstaltungName(vID));
                        VeranstaltungListeAO.this.buttonAnwesenheitNachtragen.setVisible(true);
                        VeranstaltungListeAO.this.buttonAnwesenheitAnzeigen.setVisible(false);
                        VeranstaltungListeAO.this.buttonZurueckZurVeranstaltungsUebersicht.setVisible(true);
                        logging.logInfo((Object)("Zeige mitglieder der Veranstaltung: " + VeranstaltungListeAO.this.ausgewaehlteVeranstaltung));
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                    catch (ArrayIndexOutOfBoundsException e1) {
                        VeranstaltungListeAO.this.table.setEnabled(true);
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    }
                }
            }
        });
        this.buttonAnwesenheitNachtragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.letzterVeranstaltungsname = VeranstaltungListeAO.this.ausgewaehlteVeranstaltung;
                logging.logInfo((Object)("Rufe Anwesenheit Nachtragen auf: " + VeranstaltungListeAO.this.ausgewaehlteVeranstaltung));
                MyEvent.setEvent((String)"0x0040");
                Steuerung.setStatus(Status.ANWESENHEIT_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonZurueckZurVeranstaltungsUebersicht.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                VeranstaltungListeAO.this.buttonZurueckZurVeranstaltungsUebersicht.setVisible(false);
                VeranstaltungListeAO.this.buttonAnwesenheitNachtragen.setVisible(false);
                VeranstaltungListeAO.this.table.setEnabled(true);
                VeranstaltungListeAO.this.buttonNachrichtSenden.setVisible(false);
                VeranstaltungListeAO.this.buttonAnwesenheitAnzeigen.setVisible(true);
                if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Abgelaufende Veranstaltungen")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = SbcUtils.timeStamp((String)"yyyy-MM-dd");
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Kommende Veranstaltungen")) {
                    VeranstaltungListeAO.this.datumVon = SbcUtils.timeStamp((String)"yyyy-MM-dd");
                    VeranstaltungListeAO.this.datumBis = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-12-31";
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Veranstaltungen laufendes Jahr")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-12-31";
                } else if (VeranstaltungListeAO.this.veranstaltungOption.getSelectedItem().toString().equals("Veranstaltungen n\u00e4chstes Jahr")) {
                    VeranstaltungListeAO.this.datumVon = String.valueOf(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1)) + "-01-01";
                    VeranstaltungListeAO.this.datumBis = String.valueOf(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1)) + "-12-31";
                }
                try {
                    int kID;
                    if (VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString().equals("Alle")) {
                        kID = 0;
                        VeranstaltungListeAO.this.modulBeschreibung.setText("Veranstaltungsliste");
                    } else {
                        kID = new TabelleVeranstaltung_Kategorie().getID(VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                        VeranstaltungListeAO.this.modulBeschreibung.setText("Veranstaltungsliste - " + VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                    }
                    ((DefaultTableModel)VeranstaltungListeAO.this.table.getModel()).setDataVector(new TabelleVeranstaltung().getAllVeranstaltungForTable(VeranstaltungListeAO.this.datumVon, VeranstaltungListeAO.this.datumBis, kID), VeranstaltungListeAO.this.headname);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNachrichtSenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                    TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                    StringBuilder build = new StringBuilder();
                    String[] emailListe = Utils.listToArray(tabAnwesenheit.getAnwesendeMitgliederEMail(tabVeranstaltung.getVeranstaltungID(VeranstaltungListeAO.this.ausgewaehlteVeranstaltung)));
                    int i = 0;
                    while (i < emailListe.length) {
                        if (!emailListe[i].equals("")) {
                            build.append(emailListe[i]);
                            build.append(", ");
                        }
                        ++i;
                    }
                    EMailService.EMailInformationServiceVeranstaltung(build.toString(), VeranstaltungListeAO.this.ausgewaehlteVeranstaltung);
                    JOptionPane.showMessageDialog(null, Konstante.SENDEN_ERFOLGREICH);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonCsvExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                VeranstaltungListeAO.this.chooser.setFileSelectionMode(1);
                VeranstaltungListeAO.this.chooser.showSaveDialog(null);
                logging.logInfo((Object)("Starte XLS Export in: " + VeranstaltungListeAO.this.chooser.getSelectedFile().getPath()));
                String outputOrdner = VeranstaltungListeAO.this.chooser.getSelectedFile().getPath();
                Utils.ExportJTabletoCSV(VeranstaltungListeAO.this.table, new File(outputOrdner), "VeranstaltungListe");
                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
            }
        });
        this.buttonIcsExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    VeranstaltungListeAO.this.chooser.setFileSelectionMode(1);
                    VeranstaltungListeAO.this.chooser.showSaveDialog(null);
                    logging.logInfo((Object)("Starte ICS Export in: " + VeranstaltungListeAO.this.chooser.getSelectedFile().getPath()));
                    String outputOrdner = VeranstaltungListeAO.this.chooser.getSelectedFile().getPath();
                    int kID = VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString().equals("Alle") ? 0 : new TabelleVeranstaltung_Kategorie().getID(VeranstaltungListeAO.this.veranstaltungKategorie.getSelectedItem().toString());
                    Utils.ExportICS(outputOrdner, VeranstaltungListeAO.this.datumVon, VeranstaltungListeAO.this.datumBis, kID);
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.ERSTELLEN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    public void fensterAnzeigen() {
        this.datumVon = SbcUtils.timeStamp((String)"yyyy-MM-dd");
        this.datumBis = String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-12-31";
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleVeranstaltung().getAllVeranstaltungForTable(this.datumVon, this.datumBis, 0), this.headname);
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

