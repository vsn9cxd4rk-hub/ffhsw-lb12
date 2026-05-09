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
import ao.statistik.BeteiligungsdauerAO;
import ao.utils.ProzessBarAO;
import com.itextpdf.text.DocumentException;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.BeteiligungsListePDFSchreiben;
import pdfdocumente.BeteiligungsListePDFSchreibenAusgabeformatListe;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyChartUtils;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class BeteiligungUebersichtListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonErstellen;
    public static JComboBox<String> mitglieder;
    private JComboBox<String> jahr;
    private JLabel mitglieder_label;
    private JLabel jahr_label;
    private JRadioButton buttonAlsListe;
    private JRadioButton buttonDetailsPDF;
    private JRadioButton buttonAlsTabelle;
    private ButtonGroup bGroup;
    private JLabel grafik_label;
    private JCheckBox grafik;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;

    public BeteiligungUebersichtListeAO() {
        super("FeuerwehrManagementSystem - \u00dcbersichtsliste");
        logging.logInfo((Object)"Starte: BeteiligungUebersichtListeAO");
    }

    protected void buttonErstellen() {
        this.buttonErstellen = new JButton("Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.mitglieder_label = new JLabel("Mitglieder: ");
        this.jahr_label = new JLabel("Jahr: ");
        this.grafik_label = new JLabel("Grafik einf\u00fcgen: ");
        this.buttonAlsListe = new JRadioButton("Nur Beteiligungszahlen");
        this.buttonAlsListe.setToolTipText("Erstellt eine Liste der Beteiligung in Verh\u00e4ltnis zur den Veranstaltungen...");
        this.buttonDetailsPDF = new JRadioButton("Details mit Beteiligungszahlen");
        this.buttonDetailsPDF.setToolTipText("Erstellt eine detailierte Liste mit allen An- und Abwesenheiten...");
        this.buttonAlsTabelle = new JRadioButton("Liste aller Veranstaltungen in einer Tabelle");
        this.buttonAlsTabelle.setToolTipText("Erstelle eine Tabelle aller Anwesenden Veranstaltungen...");
        this.grafik = new JCheckBox();
        this.bGroup = new ButtonGroup();
        this.bGroup.add(this.buttonAlsListe);
        this.bGroup.add(this.buttonDetailsPDF);
        this.bGroup.add(this.buttonAlsTabelle);
        this.modulBeschreibung = new JLabel("Beteiligungs\u00fcbersicht");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleJahr tabJahr = new TabelleJahr();
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            mitglieder = new JComboBox<String>(mitgliederListe);
            this.jahr = new JComboBox<String>(jahresListe);
            this.jahr.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.setSize(400, 280);
        this.setTitle("FeuerwehrManagementSystem - Beteiligungs\u00fcbersicht");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.mitglieder_label);
        this.panel.add(mitglieder);
        this.panel.add(this.jahr_label);
        this.panel.add(this.jahr);
        this.panel.add(this.grafik_label);
        this.panel.add(this.grafik);
        this.add(this.buttonAlsListe);
        this.add(this.buttonDetailsPDF);
        this.add(this.buttonAlsTabelle);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonErstellen);
        this.buttonAlsListe.setSelected(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (mitglieder.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                } else if (BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
                } else {
                    Thread threadBerichtErstellen = new Thread(){

                        @Override
                        public void run() {
                            try {
                                ProzessBarAO.progressbar.setIndeterminate(true);
                                ProzessBarAO.progressbar.setStringPainted(false);
                                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/beteiligung_uebersicht/" + mitglieder.getSelectedItem() + ".pdf";
                                String outputFileGarfik = null;
                                if (BeteiligungUebersichtListeAO.this.grafik.isSelected()) {
                                    outputFileGarfik = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString() + "/temp/Beteiligungsuebersicht.jpg";
                                    MyChartUtils.writeChartToJPEG(BeteiligungsdauerAO.createChart(BeteiligungsdauerAO.createDataset(new TabelleMitglied().getIdByGuiString(mitglieder.getSelectedItem().toString()), Integer.parseInt(BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString()))), 1000, 800, outputFileGarfik);
                                }
                                if (BeteiligungUebersichtListeAO.this.buttonDetailsPDF.isSelected()) {
                                    BeteiligungsListePDFSchreiben.PDFdocumentErstellen(dateiname, mitglieder.getSelectedItem().toString(), BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString(), outputFileGarfik);
                                    Utils.dateiKatalogisieren(dateiname);
                                    Desktop.getDesktop().open(new File(dateiname));
                                    BeteiligungUebersichtListeAO.this.dispose();
                                } else if (BeteiligungUebersichtListeAO.this.buttonAlsListe.isSelected()) {
                                    BeteiligungsListePDFSchreibenAusgabeformatListe.PDFdocumentErstellen(dateiname, mitglieder.getSelectedItem().toString(), BeteiligungUebersichtListeAO.this.jahr.getSelectedItem().toString(), outputFileGarfik);
                                    Utils.dateiKatalogisieren(dateiname);
                                    Desktop.getDesktop().open(new File(dateiname));
                                    BeteiligungUebersichtListeAO.this.dispose();
                                } else if (BeteiligungUebersichtListeAO.this.buttonAlsTabelle.isSelected()) {
                                    logging.logInfo((Object)"Erzeuge Tabelleanschicht");
                                    Steuerung.setStatus(Status.ANWESENHEITSTABELLE_PRO_MITGLIED);
                                    Steuerung.steuerung();
                                }
                                MyEvent.setEvent((String)"0x0030");
                            }
                            catch (DocumentException | IOException | SQLException e) {
                                logging.logPrintStackTrace((Exception)e);
                            }
                        }
                    };
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    threadBerichtErstellen.start();
                }
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

