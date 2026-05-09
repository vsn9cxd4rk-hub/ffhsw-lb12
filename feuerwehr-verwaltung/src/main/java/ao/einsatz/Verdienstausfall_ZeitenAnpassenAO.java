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
package ao.einsatz;

import ao.AbstractFenster;
import ao.einsatz.VerdienstausfallAO;
import ao.utils.ProzessBarAO;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_arbeit;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.verdienstausfallbescheinigung.BestaetigungFreistellungEinsatzPDFSchreiben;
import pdfdocumente.verdienstausfallbescheinigung.VerdienstausfallbescheinigungPDFSchreiben;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.XML;
import utilities.logbuchEingabe;

public class Verdienstausfall_ZeitenAnpassenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonErstellen;
    private JTextField veranstaltung;
    private JLabel veranstaltung_label;
    private JTextField datumStart;
    private JLabel datumStart_label;
    private JTextField datumEnde;
    private JLabel datumEnde_label;
    private JTextField start;
    private JLabel start_label;
    private JTextField ende;
    private JLabel ende_label;
    private JTextField freitext;
    private JLabel freitext_label;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public Verdienstausfall_ZeitenAnpassenAO() {
        super("FeuerwehrManagementSystem - Zeiten anpassen");
        logging.logInfo((Object)"Starte: Verdienstausfall_ZeitenAnpassenAO");
    }

    protected void buttonErstellen() {
        this.modulBeschreibung = new JLabel("Verdienstausfallbescheinigung - Zeiten anpassen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.buttonErstellen = new JButton("Erstellen");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.veranstaltung = new JTextField(VerdienstausfallAO.veranstaltung.getSelectedItem().toString(), 25);
        this.veranstaltung_label = new JLabel("Einsatz: ");
        this.datumStart = new JTextField(25);
        this.datumStart_label = new JLabel("Datum Anfang: ");
        this.start = new JTextField(25);
        this.start_label = new JLabel("Anfang: ");
        this.datumEnde = new JTextField(25);
        this.datumEnde_label = new JLabel("Datum Ende: ");
        this.ende = new JTextField(25);
        this.ende_label = new JLabel("Ende: ");
        this.freitext = new JTextField(25);
        this.freitext_label = new JLabel("Text f\u00fcr Einsatzinformation: ");
    }

    protected void labelErstellen() {
        this.veranstaltung.setEditable(false);
        if (!runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen").equals("5")) {
            this.freitext.setVisible(false);
            this.freitext_label.setVisible(false);
        }
    }

    protected void setzeAuswahllisten() {
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
        try {
            int vID = tabVeranstaltung.getVeranstaltungID(VerdienstausfallAO.veranstaltung.getSelectedItem().toString());
            String uhrzeitStart = tabEinsatz.getEinsatzVonUhr(vID);
            String uhrzeitEnde = tabEinsatz.getEinsatzBisUhr(vID);
            this.datumStart.setText(TimeCalculation.parseDateForGUI(tabEinsatz.getEinsatzDatum(vID)));
            if (Integer.parseInt(uhrzeitStart.substring(0, 2)) <= Integer.parseInt(uhrzeitEnde.substring(0, 2))) {
                logging.logInfo((Object)"Einsatz endet am gleichen Tag");
                this.datumEnde.setText(TimeCalculation.parseDateForGUI(tabEinsatz.getEinsatzDatum(vID)));
            } else {
                logging.logInfo((Object)"Einsatz endet am folge Tag");
                String endeDatum = tabEinsatz.getEinsatzDatum(vID);
                int neuerTag = Integer.parseInt(endeDatum.substring(8, 10)) + 1;
                String neueEnde = null;
                neueEnde = neuerTag <= 9 ? String.valueOf(endeDatum.substring(0, 8)) + "0" + neuerTag : String.valueOf(endeDatum.substring(0, 8)) + neuerTag;
                this.datumEnde.setText(TimeCalculation.parseDateForGUI(neueEnde));
            }
            this.start.setText(uhrzeitStart);
            this.ende.setText(uhrzeitEnde);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, Konstante.KEINE_ZEITEN_VERFUEGBAR, "Warnung", 2);
        }
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(600, 270);
        this.setTitle("FeuerwehrManagementSystem - Zeiten anpassen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(6, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.veranstaltung_label);
        this.panel.add(this.veranstaltung);
        this.panel.add(this.datumStart_label);
        this.panel.add(this.datumStart);
        this.panel.add(this.start_label);
        this.panel.add(this.start);
        this.panel.add(this.datumEnde_label);
        this.panel.add(this.datumEnde);
        this.panel.add(this.ende_label);
        this.panel.add(this.ende);
        this.panel.add(this.freitext_label);
        this.panel.add(this.freitext);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonErstellen);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!TimeCalculation.checkDateFormat(Verdienstausfall_ZeitenAnpassenAO.this.datumEnde.getText())) {
                    Verdienstausfall_ZeitenAnpassenAO.this.datumEnde.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkTimeFormat(Verdienstausfall_ZeitenAnpassenAO.this.ende.getText())) {
                    Verdienstausfall_ZeitenAnpassenAO.this.ende.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (Verdienstausfall_ZeitenAnpassenAO.this.freitext.isVisible() && Verdienstausfall_ZeitenAnpassenAO.this.freitext.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_FREITEXT_ANGEBEN, "Warnung", 2);
                } else if (!new File(runApplication.EINSTELLUNGEN.get("verdienstausfall")).exists() && runApplication.EINSTELLUNGEN.get("VerdienstausfallBerichtArt").equals("Word Schnittstelle")) {
                    JOptionPane.showMessageDialog(null, "Die Verdienstausfallvorlage ist nicht vorhanden und kann nicht erstellt werden.\nBitte kontrollieren Sie die Programmeinstellungen.\n\nFolgende Datei ist nicht vorhanden:\n" + runApplication.EINSTELLUNGEN.get("verdienstausfall"), "Fehlermeldung", 0);
                } else {
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    ProzessBarAO.progressbar.setStringPainted(false);
                    ProzessBarAO.progressbar.setIndeterminate(true);
                    ProzessBarAO.label_bitteWarten.setText("Statistik wird berechnet... Bitte haben sie einen Moment Geduld...");
                    Thread thread = new Thread(){

                        @Override
                        public void run() {
                            try {
                                String dateiname;
                                int mID;
                                String isSelectedVorname;
                                String isSelectedName;
                                int komma;
                                int i;
                                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                                TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                                TabelleStatistikEinsatz tabStatistik = new TabelleStatistikEinsatz();
                                TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                                TabelleMitglied tabMitglieder = new TabelleMitglied();
                                TabelleMitglieder_arbeit tabArbeit = new TabelleMitglieder_arbeit();
                                Verdienstausfall_ZeitenAnpassenAO.this.datumEnde.setBackground(null);
                                Verdienstausfall_ZeitenAnpassenAO.this.ende.setBackground(null);
                                int vID = tabVeranstaltung.getVeranstaltungID(Verdienstausfall_ZeitenAnpassenAO.this.veranstaltung.getText());
                                String file = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/verdienstausfall/Einsatz_ID_" + tabEinsatz.getEinsatzIDByVeranstaltungID(vID);
                                Utils.ordnerErstellen(file, runApplication.clientID);
                                String[] anwesendeListe = Utils.listToArray(tabAnwesenheit.getAnwesendeMitgliederByVeranstaltung(vID));
                                String am = Verdienstausfall_ZeitenAnpassenAO.this.datumStart.getText();
                                String bis = Verdienstausfall_ZeitenAnpassenAO.this.datumEnde.getText();
                                String vonUhr = Verdienstausfall_ZeitenAnpassenAO.this.start.getText();
                                String bisUhr = Verdienstausfall_ZeitenAnpassenAO.this.ende.getText();
                                StringBuilder buildError = new StringBuilder();
                                String[] dataList = new String[anwesendeListe.length];
                                String[] dataListBescheinigung = new String[anwesendeListe.length];
                                String bezeichnung = runApplication.EINSTELLUNGEN.get("verdienstausfallOptionen").equals("5") ? Verdienstausfall_ZeitenAnpassenAO.this.freitext.getText() : tabEinsatz.getEinsatznummerForVerdienstausfall(vID);
                                if (TimeCalculation.stundenInMinutenUmrechnen(vonUhr) + tabStatistik.getDauer(tabEinsatz.getEinsatzIDByVeranstaltungID(vID)) >= 1440) {
                                    logging.logInfo((Object)"Der Einsatz wurde nicht an dem Tag beendet wo er begonnen hat");
                                }
                                if (runApplication.EINSTELLUNGEN.get("VerdienstausfallBerichtArt").equals("Word Schnittstelle") | runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzArt").equals("Word Schnittstelle")) {
                                    i = 0;
                                    while (i < anwesendeListe.length) {
                                        komma = anwesendeListe[i].toString().indexOf(",");
                                        isSelectedName = anwesendeListe[i].toString().substring(0, komma);
                                        mID = tabMitglieder.getId(isSelectedName, isSelectedVorname = anwesendeListe[i].toString().substring(komma + 2, anwesendeListe[i].toString().length()));
                                        if (tabArbeit.getName(mID).equals("")) {
                                            buildError.append(String.valueOf(isSelectedName) + ", " + isSelectedVorname);
                                            buildError.append("\n");
                                        } else {
                                            File docFile;
                                            dateiname = String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".xml";
                                            String dateinameBescheinigung = String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".BescheinigungFreistellungEinsatz.xml";
                                            String[] ist = new String[]{"wfirm", "wdate", "wansc", "wtele", "wname", "wwohn", "wgebd", "wberu", "wdsta", "wdend", "wusta", "wuend", "weins"};
                                            String[] zu = new String[]{Utils.checkTextAndRemoveIllegalSigns(tabArbeit.getName(mID)), SbcUtils.timeStamp((String)"dd.MM.yyyy"), String.valueOf(Utils.checkTextAndRemoveIllegalSigns(tabArbeit.getStrasse(mID))) + ", " + Utils.checkTextAndRemoveIllegalSigns(tabArbeit.getOrt(mID)), tabArbeit.getTelefon(mID), String.valueOf(Utils.checkTextAndRemoveIllegalSigns(isSelectedName)) + ", " + Utils.checkTextAndRemoveIllegalSigns(isSelectedVorname), String.valueOf(Utils.checkTextAndRemoveIllegalSigns(tabMitglieder.getStrasse(mID))) + ", " + Utils.checkTextAndRemoveIllegalSigns(tabMitglieder.getOrt(mID)), TimeCalculation.parseDateForGUI(tabMitglieder.getGebDatum(mID)), tabMitglieder.getBeruf(mID), am, bis, vonUhr, bisUhr, Utils.checkTextAndRemoveIllegalSigns(bezeichnung)};
                                            if (runApplication.EINSTELLUNGEN.get("VerdienstausfallBerichtArt").equals("Word Schnittstelle")) {
                                                XML.createEinsatzBericht(ist, zu, dateiname, runApplication.EINSTELLUNGEN.get("verdienstausfall"));
                                                docFile = new File(String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".doc");
                                                new File(dateiname).renameTo(docFile);
                                                dataList[i] = String.valueOf(isSelectedName) + "." + isSelectedVorname + ".doc";
                                                Utils.dateiKatalogisieren(String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".doc");
                                            }
                                            if (runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzArt").equals("Word Schnittstelle") && runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzAktiv").equals("1")) {
                                                XML.createEinsatzBericht(ist, zu, dateinameBescheinigung, runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatz"));
                                                docFile = new File(String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".BescheinigungFreistellungEinsatz.doc");
                                                new File(dateinameBescheinigung).renameTo(docFile);
                                                dataListBescheinigung[i] = String.valueOf(isSelectedName) + "." + isSelectedVorname + ".BescheinigungFreistellungEinsatz.doc";
                                                Utils.dateiKatalogisieren(String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".BescheinigungFreistellungEinsatz.doc");
                                            }
                                        }
                                        ++i;
                                    }
                                }
                                if (runApplication.EINSTELLUNGEN.get("VerdienstausfallBerichtArt").equals("PDF (intern)") | runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzArt").equals("PDF (intern)")) {
                                    i = 0;
                                    while (i < anwesendeListe.length) {
                                        komma = anwesendeListe[i].toString().indexOf(",");
                                        isSelectedName = anwesendeListe[i].toString().substring(0, komma);
                                        mID = tabMitglieder.getId(isSelectedName, isSelectedVorname = anwesendeListe[i].toString().substring(komma + 2, anwesendeListe[i].toString().length()));
                                        if (tabArbeit.getName(mID).equals("")) {
                                            buildError.append(String.valueOf(isSelectedName) + ", " + isSelectedVorname);
                                            buildError.append("\n");
                                        } else {
                                            if (runApplication.EINSTELLUNGEN.get("VerdienstausfallBerichtArt").equals("PDF (intern)")) {
                                                dateiname = String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".pdf";
                                                VerdienstausfallbescheinigungPDFSchreiben.PDFdocumentErstellen(dateiname, bezeichnung, mID, am, bis, vonUhr, bisUhr);
                                                dataList[i] = String.valueOf(isSelectedName) + "." + isSelectedVorname + ".pdf";
                                                Utils.dateiKatalogisieren(String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".pdf");
                                            }
                                            if (runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzArt").equals("PDF (intern)") && runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzAktiv").equals("1")) {
                                                dateiname = String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".BescheinigungFreistellungEinsatz.pdf";
                                                BestaetigungFreistellungEinsatzPDFSchreiben.PDFdocumentErstellen(dateiname, bezeichnung, mID, am, bis, vonUhr, bisUhr);
                                                dataListBescheinigung[i] = String.valueOf(isSelectedName) + "." + isSelectedVorname + ".BescheinigungFreistellungEinsatz.pdf";
                                                Utils.dateiKatalogisieren(String.valueOf(file) + "/" + isSelectedName + "." + isSelectedVorname + ".BescheinigungFreistellungEinsatz.pdf");
                                            }
                                        }
                                        ++i;
                                    }
                                }
                                if (buildError.length() != 0) {
                                    JOptionPane.showMessageDialog(null, Konstante.KEIN_VERDINSTAUSFALLBESCHEINIGUNG + buildError.toString(), "Warnung", 2);
                                }
                                if (runApplication.EINSTELLUNGEN.get("bestaetignungFreistellungEinsatzAktiv").equals("1")) {
                                    String[] listData = new String[dataList.length + dataListBescheinigung.length];
                                    int i2 = 0;
                                    while (i2 < dataList.length) {
                                        listData[i2] = dataList[i2];
                                        System.out.println(i2);
                                        ++i2;
                                    }
                                    i2 = dataList.length;
                                    while (i2 < listData.length) {
                                        listData[i2] = dataListBescheinigung[i2 - dataList.length];
                                        System.out.println(i2);
                                        ++i2;
                                    }
                                    logging.logInfo((Object)"Setze ListeData mit Best\u00e4tigung und Verdisntausfall");
                                    VerdienstausfallAO.liste.setListData(listData);
                                } else {
                                    logging.logInfo((Object)"Setze ListeData mit Verdisntausfall");
                                    VerdienstausfallAO.liste.setListData(dataList);
                                }
                                VerdienstausfallAO.buttonErstellen.setEnabled(false);
                                VerdienstausfallAO.aktuellerOrdner = String.valueOf(file) + "/";
                                logbuchEingabe.NeuerEintag("Verdienstausfallbescheinigungen wurden erstellt: " + bezeichnung);
                                JOptionPane.showMessageDialog(null, Konstante.VERDINSTAUSFALL_DATEIEN_ERSTELLT);
                                MyEvent.setEvent((String)"0x0030");
                                Verdienstausfall_ZeitenAnpassenAO.this.dispose();
                            }
                            catch (DocumentException | IOException | SQLException e) {
                                MyEvent.setEvent((String)"0x0030");
                                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                                logging.logPrintStackTrace((Exception)e);
                            }
                        }
                    };
                    thread.start();
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

