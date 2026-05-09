/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleVeranstaltung;
import go.Protokoll;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.ProtokollPDFScheiben;
import run.runApplication;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class ProtokollAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonExport;
    private JLabel veranstaltungen_label;
    private JLabel title_label;
    private JComboBox<String> veranstaltung;
    public static JTextArea textfield;
    public static JTextField title;
    private JScrollPane pane;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public ProtokollAO() {
        super("FeuerwehrManagementSystem - Protokoll");
        logging.logInfo((Object)"Starte: ProtokollAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonExport = new JButton("Export");
        textfield = new JTextArea(23, 50);
        textfield.setLineWrap(true);
        textfield.setWrapStyleWord(true);
        this.pane = new JScrollPane(textfield);
        this.pane.setVerticalScrollBarPolicy(22);
        title = new JTextField(35);
        this.title_label = new JLabel("Protokoll Betreff / \u00dcberschrift: ");
        this.veranstaltungen_label = new JLabel("Veranstaltung: ");
        this.modulBeschreibung = new JLabel("Protokoll erstellen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        try {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            String[] veranstaltungenListe = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltung());
            this.veranstaltung = new JComboBox<String>(veranstaltungenListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (!ProtokollAO.this.veranstaltung.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                    try {
                        TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        ProtokollAO.this.buttonSpeichern.setEnabled(true);
                        ProtokollAO.this.buttonExport.setEnabled(true);
                        textfield.setEditable(true);
                        title.setEditable(true);
                        int vID = tabVeranstaltung.getVeranstaltungID(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
                        if (tabProtokoll.getCount(vID) == 1) {
                            Protokoll protokoll = new Protokoll();
                            protokoll = tabProtokoll.getData(vID);
                            title.setText(protokoll.getTitle());
                            textfield.setText(protokoll.getProtokolltext());
                        } else {
                            textfield.setText(null);
                            title.setText(null);
                        }
                        if (ProtokollAO.this.veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                            title.setText(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
                            title.setEditable(false);
                        }
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else {
                    ProtokollAO.this.buttonSpeichern.setEnabled(false);
                    ProtokollAO.this.buttonExport.setEnabled(false);
                    textfield.setEditable(false);
                    title.setEditable(false);
                    textfield.setText(null);
                    title.setText(null);
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
        this.setSize(870, 700);
        this.setTitle("FeuerwehrManagementSystem - Protokoll");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.veranstaltungen_label);
        this.panel.add(this.veranstaltung);
        this.panel.add(this.title_label);
        this.panel.add(title);
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        TitledBorder rahmen = BorderFactory.createTitledBorder(lowerEtched, "Protokolltext");
        this.pane.setBorder(rahmen);
        this.pane.setPreferredSize(new Dimension(800, 480));
        this.add(this.pane);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonExport);
        this.buttonExport.setEnabled(false);
        this.buttonSpeichern.setEnabled(false);
        textfield.setEditable(false);
        title.setEditable(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (title.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_TITLE_ANGEBEN, "Warnung", 2);
                    } else {
                        TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        Protokoll protokoll = new Protokoll();
                        int vID = tabVeranstaltung.getVeranstaltungID(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
                        int kID = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                        protokoll.setId(tabProtokoll.getNextNummer());
                        protokoll.setVeranstaltungID(vID);
                        protokoll.setJahr(tabVeranstaltung.getJahrDerVeranstaltung(vID));
                        protokoll.setTitle(title.getText());
                        protokoll.setProtokolltext(textfield.getText());
                        protokoll.setErstelldatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                        if (tabProtokoll.getCount(vID) == 0) {
                            tabProtokoll.insert(protokoll);
                            logbuchEingabe.NeuerEintag("Protokoll wurde zur Veranstaltung: " + ProtokollAO.this.veranstaltung.getSelectedItem().toString() + " hinzugef\u00fcgt");
                        } else {
                            tabProtokoll.update(protokoll);
                            logbuchEingabe.NeuerEintag("Protokoll wurde zur Veranstaltung: " + ProtokollAO.this.veranstaltung.getSelectedItem().toString() + " aktualisiert");
                        }
                        if (kID == 1 && runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteEinsatzBericht\u00dcbermitteln").equals("1")) {
                            ProtokollAO.this.updateEinsatzBerichtHomepage(vID);
                        }
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonExport.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                    TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                    int vID = tabVeranstaltung.getVeranstaltungID(ProtokollAO.this.veranstaltung.getSelectedItem().toString());
                    Protokoll protokoll = new Protokoll();
                    protokoll = tabProtokoll.getData(vID);
                    String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                    ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
                    Utils.dateiKatalogisieren(dateiname);
                    Desktop.getDesktop().open(new File(dateiname));
                }
                catch (DocumentException | IOException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    private void updateEinsatzBerichtHomepage(final int vID) {
        Thread threadEinsatzkomponenteBericht = new Thread(){

            @Override
            public void run() {
                logging.logInfo((Object)"Starte JoomlaThread - Sende EinsatzBericht an die Einsatzkomponente...");
                TabelleEinsatz_organisationen tabEinsatz_organisation = new TabelleEinsatz_organisationen();
                try {
                    Joomla.erstelleEinsatzBericht(vID, textfield.getText().toString().split("\n"), tabEinsatz_organisation.getOrganisationIDKommaSeperated(vID));
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadEinsatzkomponenteBericht.start();
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

