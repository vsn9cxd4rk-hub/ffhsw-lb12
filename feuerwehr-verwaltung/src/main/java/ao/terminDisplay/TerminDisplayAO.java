/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.terminDisplay;

import ao.AbstractFenster;
import ao.utils.StartBildschirmAO;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleBrandsicherheitswache;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import logging.logging;
import run.images;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class TerminDisplayAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JLabel logo;
    private JLabel dummy;
    private static JLabel uhr;
    private JLabel hintergrund;
    private JPanel[] panelVeranstaltungen;
    private JLabel[] veranstaltungen_label;
    private JLabel[] veranstaltungenDatum_label;
    private JLabel[] veranstaltungenZeit_label;
    private JLabel[] ausbilder1;
    private JLabel[] ausbilder2;
    private JLabel[] bswTeilnehmer1;
    private JLabel[] bswTeilnehmer2;
    private JLabel[] details;
    private JLabel[] personenBeschreibung;
    private JLabel[] veranstaltung_logo;
    private int anzeigeAnzahlVeranstaltungen;
    private JPanel panelVeranstaltungsliste;
    private JLabel[] veranstaltungListe_label;
    private JLabel[] veranstaltungenDatumListe_label;
    private JLabel[] veranstaltungenZeitListe_label;
    private int anzeigeAnzahlVeranstaltungenListe;
    private JLabel veransatltungListe_logo;
    private JPanel panelLetzterEinsatz;
    private JLabel letztesStichwort;
    private JLabel letzterEinsatzDatum;
    private JLabel letzterEinsatzZeit;
    private JLabel letzterEinsatzOrt;
    private JLabel letzterEinsatzBeschreibung;
    private JLabel letzterEinsatzLogo;
    private JPanel panelUhr;
    private JLabel uhr_logo;
    private JLabel datum;
    private JLabel uhrGross;
    private JPanel mainPanel;

    public TerminDisplayAO() {
        super("FeuerwehrManagementSystem - Termin Display");
        logging.logInfo((Object)"Starte: TerminDisplayAO");
    }

    protected void buttonErstellen() {
        uhr = new JLabel();
        this.hintergrund = new JLabel();
        this.datum = new JLabel();
        this.uhrGross = new JLabel();
        this.logo = new JLabel(runApplication.bannerHauptprogramm);
        this.dummy = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Termin Display");
        this.setSize((int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth(), (int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight());
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(uhr);
        this.add(this.logo);
        this.add(this.dummy);
    }

    private void createUhrPanel() {
        images icons = new images();
        this.panelUhr = new JPanel();
        this.panelUhr.setLayout(null);
        this.panelUhr.setBackground(Color.DARK_GRAY);
        this.uhr_logo = new JLabel(icons.iconUhr());
        this.panelUhr.setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, Color.WHITE));
        this.datum.setFont(new Font("SansSerif", 1, 60));
        this.uhrGross.setFont(new Font("SansSerif", 1, 170));
        this.panelUhr.add(this.datum);
        this.datum.setForeground(Color.WHITE);
        this.panelUhr.add(this.uhrGross);
        this.uhrGross.setForeground(Color.WHITE);
        this.panelUhr.add(this.uhr_logo);
        this.datum.setBounds(275, 15, 800, 300);
        this.uhrGross.setBounds(275, 75, 850, 500);
        this.uhr_logo.setBounds(10, 10, 250, 450);
    }

    private void createEinsatzPanel() {
        TabelleEinsatz tabEinsatz = new TabelleEinsatz();
        TabelleStichwort tabStichwort = new TabelleStichwort();
        images icons = new images();
        try {
            this.panelLetzterEinsatz = new JPanel();
            this.panelLetzterEinsatz.setLayout(null);
            this.panelLetzterEinsatz.setPreferredSize(new Dimension(370, 115));
            this.panelLetzterEinsatz.setBackground(Color.LIGHT_GRAY);
            this.letzterEinsatzDatum = new JLabel();
            this.letzterEinsatzZeit = new JLabel();
            this.letzterEinsatzOrt = new JLabel();
            this.letztesStichwort = new JLabel();
            this.letzterEinsatzBeschreibung = new JLabel("Letzer Einsatz:");
            this.letzterEinsatzLogo = new JLabel();
            HashMap<String, String> einsatzDaten = tabEinsatz.getData(tabEinsatz.getletzteVeranstaltungID());
            String stichwort = tabStichwort.getStichwortName(Integer.parseInt(einsatzDaten.get("Stichwort")));
            int stichwortKategorie = tabStichwort.getStichwortKategorieID(stichwort);
            if (stichwortKategorie == 1) {
                this.letzterEinsatzLogo.setIcon(icons.iconBrandEinsatz());
            } else if (stichwortKategorie == 2) {
                this.letzterEinsatzLogo.setIcon(icons.iconTHEinsatz());
            } else if (stichwortKategorie == 3) {
                this.letzterEinsatzLogo.setIcon(icons.iconWBEinsatz());
            } else {
                this.letzterEinsatzLogo.setIcon(icons.iconSonstiges());
            }
            this.letztesStichwort.setText(stichwort);
            this.letzterEinsatzDatum.setText("Datum: " + TimeCalculation.parseDateForGUI(einsatzDaten.get("Datum")));
            this.letzterEinsatzZeit.setText(String.valueOf(einsatzDaten.get("ZeitAlarm")) + " Uhr");
            this.panelLetzterEinsatz.add(this.letzterEinsatzBeschreibung);
            this.letzterEinsatzBeschreibung.setFont(new Font("SansSerif", 1, 20));
            this.letzterEinsatzBeschreibung.setBounds(90, 10, 200, 20);
            this.panelLetzterEinsatz.add(this.letztesStichwort);
            this.letztesStichwort.setFont(new Font("SansSerif", 1, 30));
            this.letztesStichwort.setForeground(Color.red);
            this.letztesStichwort.setBounds(90, 22, 370, 50);
            this.panelLetzterEinsatz.add(this.letzterEinsatzDatum);
            this.panelLetzterEinsatz.add(this.letzterEinsatzZeit);
            this.panelLetzterEinsatz.add(this.letzterEinsatzLogo);
            if (runApplication.EINSTELLUNGEN.get("TerminDisplay_LetzterEinsatzOrtAnzeigen").equals("1")) {
                this.panelLetzterEinsatz.add(this.letzterEinsatzOrt);
                this.letzterEinsatzOrt.setText(einsatzDaten.get("Ort"));
                this.letzterEinsatzOrt.setFont(new Font("SansSerif", 1, 12));
                this.letzterEinsatzOrt.setBounds(90, 65, 370, 20);
            }
            this.letzterEinsatzDatum.setBounds(90, 90, 160, 20);
            this.letzterEinsatzZeit.setBounds(250, 90, 100, 20);
            this.letzterEinsatzLogo.setBounds(2, 2, 80, 110);
            this.panelLetzterEinsatz.setBounds((int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth() - 380, (int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight() - 410, 370, 115);
            Border lowerEtched = BorderFactory.createEtchedBorder(1);
            TitledBorder title2 = BorderFactory.createTitledBorder(lowerEtched, "");
            this.panelLetzterEinsatz.setBorder(title2);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private void createVeranstaltungPanels(int anzeigeAnzahl) {
        try {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleAusbildung_plan tabPlan = new TabelleAusbildung_plan();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleBrandsicherheitswache tabBSW = new TabelleBrandsicherheitswache();
            TabelleAnwesenheit tabAnweisenheit = new TabelleAnwesenheit();
            images icons = new images();
            String[] veranstaltungListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraums(SbcUtils.timeStamp((String)"yyyy-MM-dd"), String.valueOf(runApplication.veranstaltungsAnzeigeZukunft) + "-01", 0));
            int[] veranstaltungIDListe = Utils.listToIntArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsIDs(SbcUtils.timeStamp((String)"yyyy-MM-dd"), String.valueOf(runApplication.veranstaltungsAnzeigeZukunft) + "-01", 0));
            String[] veranstaltungDatumListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"), String.valueOf(runApplication.veranstaltungsAnzeigeZukunft) + "-01", 0));
            String[] veranstaltungZeitListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsZeit(SbcUtils.timeStamp((String)"yyyy-MM-dd"), String.valueOf(runApplication.veranstaltungsAnzeigeZukunft) + "-01", 0));
            logging.logInfo((Object)("Anzahl Veranstaltungen im Zeitraum: " + veranstaltungListe.length));
            logging.logInfo((Object)("Anzahl der anzuzeigenden Veranstaltungen: " + anzeigeAnzahl));
            if (veranstaltungListe.length <= anzeigeAnzahl) {
                logging.logInfo((Object)("Reduziere max Anzeige auf: " + veranstaltungListe.length));
                anzeigeAnzahl = veranstaltungListe.length;
            }
            this.panelVeranstaltungen = new JPanel[anzeigeAnzahl];
            this.veranstaltungen_label = new JLabel[anzeigeAnzahl];
            this.veranstaltungenDatum_label = new JLabel[anzeigeAnzahl];
            this.veranstaltungenZeit_label = new JLabel[anzeigeAnzahl];
            this.ausbilder1 = new JLabel[anzeigeAnzahl];
            this.ausbilder2 = new JLabel[anzeigeAnzahl];
            this.bswTeilnehmer1 = new JLabel[anzeigeAnzahl];
            this.bswTeilnehmer2 = new JLabel[anzeigeAnzahl];
            this.details = new JLabel[anzeigeAnzahl];
            this.veranstaltung_logo = new JLabel[anzeigeAnzahl];
            this.personenBeschreibung = new JLabel[anzeigeAnzahl];
            int i = anzeigeAnzahl - 1;
            while (i > -1) {
                int vID = veranstaltungIDListe[i];
                int kID = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                this.veranstaltungen_label[i] = new JLabel(veranstaltungListe[i]);
                this.veranstaltungen_label[i].setFont(new Font("Arial", 1, 22));
                this.veranstaltungenDatum_label[i] = new JLabel("Datum: " + TimeCalculation.parseDateForGUI(veranstaltungDatumListe[i]));
                this.veranstaltungenZeit_label[i] = new JLabel("Uhrzeit: " + veranstaltungZeitListe[i]);
                this.ausbilder1[i] = new JLabel();
                this.ausbilder2[i] = new JLabel();
                this.bswTeilnehmer1[i] = new JLabel();
                this.bswTeilnehmer2[i] = new JLabel();
                this.details[i] = new JLabel();
                this.personenBeschreibung[i] = new JLabel();
                this.veranstaltung_logo[i] = new JLabel(icons.iconSonstiges());
                this.panelVeranstaltungen[i] = new JPanel();
                this.panelVeranstaltungen[i].setLayout(null);
                this.panelVeranstaltungen[i].setBackground(Color.LIGHT_GRAY);
                this.panelVeranstaltungen[i].add(this.veranstaltungen_label[i]);
                this.panelVeranstaltungen[i].add(this.veranstaltungenDatum_label[i]);
                this.panelVeranstaltungen[i].add(this.veranstaltungenZeit_label[i]);
                this.panelVeranstaltungen[i].add(this.veranstaltung_logo[i]);
                if (kID == 2) {
                    int ausbilder1ID = tabPlan.getAusbilder1(vID);
                    int ausbilder2ID = tabPlan.getAusbilder2(vID);
                    this.details[i].setText(tabPlan.getDeatils(vID));
                    this.personenBeschreibung[i].setText("Ausbilder:");
                    if (ausbilder1ID != 0) {
                        this.ausbilder1[i].setText(tabMitglied.getNameVornameByID(ausbilder1ID));
                    } else {
                        this.ausbilder1[i].setText("FREI");
                    }
                    if (ausbilder2ID != 0) {
                        this.ausbilder2[i].setText(tabMitglied.getNameVornameByID(ausbilder2ID));
                    } else {
                        this.ausbilder2[i].setText("FREI");
                    }
                    this.panelVeranstaltungen[i].add(this.personenBeschreibung[i]);
                    this.personenBeschreibung[i].setFont(new Font("Arial", 1, 12));
                    this.panelVeranstaltungen[i].add(this.details[i]);
                    this.details[i].setFont(new Font("Arial", 1, 12));
                    this.details[i].setForeground(Color.BLUE);
                    this.panelVeranstaltungen[i].add(this.ausbilder1[i]);
                    this.panelVeranstaltungen[i].add(this.ausbilder2[i]);
                    this.ausbilder1[i].setBounds(220, 70, 140, 20);
                    this.ausbilder2[i].setBounds(220, 90, 140, 20);
                    this.details[i].setBounds(90, 30, 270, 20);
                    this.personenBeschreibung[i].setBounds(220, 50, 200, 20);
                    this.veranstaltung_logo[i].setIcon(icons.iconDienst());
                }
                if (kID == 3) {
                    HashMap<String, String> bswData = tabBSW.getData(vID);
                    this.details[i].setText(bswData.get("art").toString());
                    this.details[i].setFont(new Font("Arial", 1, 12));
                    this.details[i].setForeground(Color.BLUE);
                    this.personenBeschreibung[i].setText("Teilnehmer:");
                    String[] anwesende = Utils.listToArray(tabAnweisenheit.getAnwesendeMitglieder(vID));
                    if (anwesende.length == 1) {
                        this.bswTeilnehmer1[i].setText(anwesende[0]);
                    } else {
                        this.bswTeilnehmer1[i].setText("FREI");
                    }
                    if (anwesende.length == 2) {
                        this.bswTeilnehmer1[i].setText(anwesende[0]);
                        this.bswTeilnehmer2[i].setText(anwesende[1]);
                    } else {
                        this.bswTeilnehmer1[i].setText(anwesende[0]);
                        this.bswTeilnehmer2[i].setText("FREI");
                    }
                    this.panelVeranstaltungen[i].add(this.personenBeschreibung[i]);
                    this.personenBeschreibung[i].setFont(new Font("Arial", 1, 12));
                    this.panelVeranstaltungen[i].add(this.details[i]);
                    this.panelVeranstaltungen[i].add(this.bswTeilnehmer1[i]);
                    this.panelVeranstaltungen[i].add(this.bswTeilnehmer2[i]);
                    this.details[i].setBounds(90, 30, 270, 20);
                    this.bswTeilnehmer1[i].setBounds(220, 70, 200, 20);
                    this.bswTeilnehmer2[i].setBounds(220, 90, 200, 20);
                    this.personenBeschreibung[i].setBounds(220, 50, 200, 20);
                    this.veranstaltung_logo[i].setIcon(icons.iconBSW());
                }
                this.veranstaltungen_label[i].setBounds(90, 7, 270, 25);
                this.veranstaltungenDatum_label[i].setBounds(90, 70, 200, 20);
                this.veranstaltungenZeit_label[i].setBounds(90, 90, 200, 20);
                this.veranstaltung_logo[i].setBounds(2, 2, 80, 110);
                Border lowerEtched = BorderFactory.createEtchedBorder(1);
                TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, "");
                this.panelVeranstaltungen[i].setBorder(title);
                --i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private void createVeranstaltungListePanels() {
        try {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            images icons = new images();
            String[] veranstaltungListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraums(SbcUtils.timeStamp((String)"yyyy-MM-dd"), String.valueOf(runApplication.veranstaltungsAnzeigeZukunft) + "-01", 0));
            String[] veranstaltungDatumListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"), String.valueOf(runApplication.veranstaltungsAnzeigeZukunft) + "-01", 0));
            String[] veranstaltungZeitListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsZeit(SbcUtils.timeStamp((String)"yyyy-MM-dd"), String.valueOf(runApplication.veranstaltungsAnzeigeZukunft) + "-01", 0));
            logging.logInfo((Object)("Anzahl Veranstaltungen im Zeitraum: " + veranstaltungListe.length));
            logging.logInfo((Object)("Anzahl der anzuzeigenden Veranstaltungen: " + this.anzeigeAnzahlVeranstaltungen));
            if (veranstaltungListe.length <= this.anzeigeAnzahlVeranstaltungen) {
                logging.logInfo((Object)("Reduziere max Anzeige auf: " + veranstaltungListe.length));
                this.anzeigeAnzahlVeranstaltungen = veranstaltungListe.length;
            }
            this.panelVeranstaltungsliste = new JPanel();
            this.panelVeranstaltungsliste.setLayout(null);
            this.panelVeranstaltungsliste.setBackground(Color.DARK_GRAY);
            this.panelVeranstaltungsliste.setBorder(BorderFactory.createMatteBorder(8, 8, 8, 8, Color.WHITE));
            this.veranstaltungListe_label = new JLabel[this.anzeigeAnzahlVeranstaltungenListe];
            this.veranstaltungenDatumListe_label = new JLabel[this.anzeigeAnzahlVeranstaltungenListe];
            this.veranstaltungenZeitListe_label = new JLabel[this.anzeigeAnzahlVeranstaltungenListe];
            this.veransatltungListe_logo = new JLabel(icons.iconKalender());
            int y = 20;
            int i = 0;
            while (i < this.anzeigeAnzahlVeranstaltungenListe) {
                this.panelVeranstaltungsliste.add(this.veransatltungListe_logo);
                this.veransatltungListe_logo.setBounds(10, 10, 250, 700);
                try {
                    this.veranstaltungListe_label[i] = new JLabel(veranstaltungListe[i]);
                    this.veranstaltungenDatumListe_label[i] = new JLabel(TimeCalculation.parseDateForGUI(veranstaltungDatumListe[i]));
                    this.veranstaltungenZeitListe_label[i] = new JLabel(veranstaltungZeitListe[i]);
                    this.panelVeranstaltungsliste.add(this.veranstaltungListe_label[i]);
                    this.veranstaltungListe_label[i].setFont(new Font("SansSerif", 1, 42));
                    this.veranstaltungListe_label[i].setForeground(Color.WHITE);
                    this.panelVeranstaltungsliste.add(this.veranstaltungenDatumListe_label[i]);
                    this.veranstaltungenZeitListe_label[i].setFont(new Font("SansSerif", 1, 20));
                    this.veranstaltungenDatumListe_label[i].setForeground(Color.WHITE);
                    this.panelVeranstaltungsliste.add(this.veranstaltungenZeitListe_label[i]);
                    this.veranstaltungenZeitListe_label[i].setFont(new Font("SansSerif", 1, 20));
                    this.veranstaltungenZeitListe_label[i].setForeground(Color.WHITE);
                    this.veranstaltungListe_label[i].setBounds(365, y, 500, 55);
                    this.veranstaltungenDatumListe_label[i].setBounds(275, y, 150, 22);
                    this.veranstaltungenZeitListe_label[i].setBounds(275, y += 20, 150, 22);
                    y += 50;
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    logging.logWarning((Object)"Keine Veranstaltungen im angegebenen Zeitraum gefunden...");
                }
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private void addVeranstaltungPanelToMainPanel() {
        logging.logInfo((Object)"Zeige Veranstaltungen...");
        try {
            logging.logInfo((Object)"Remove Panels...");
            this.remove(this.panelUhr);
            this.remove(this.panelLetzterEinsatz);
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        this.createVeranstaltungPanels(this.anzeigeAnzahlVeranstaltungen);
        this.createEinsatzPanel();
        int y = (int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight() - 410;
        int p = this.anzeigeAnzahlVeranstaltungen - 1;
        while (p > -1) {
            try {
                this.panelVeranstaltungen[p].setPreferredSize(new Dimension(370, 115));
                this.panelVeranstaltungen[p].setBounds(10, y, 370, 115);
                this.mainPanel.add(this.panelVeranstaltungen[p]);
                y -= 123;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                logging.logWarning((Object)"Keine Veranstaltungen im angegebenen Zeitraum gefunden...");
            }
            --p;
        }
        if (runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigenLetzenEinsatz").equals("1")) {
            this.mainPanel.add(this.panelLetzterEinsatz);
        }
    }

    private void addVeranstaltungListePanelToMainPanel() {
        logging.logInfo((Object)"Zeige Veranstaltungen-Liste...");
        try {
            logging.logInfo((Object)"Remove Panels...");
            this.remove(this.panelLetzterEinsatz);
            int p = this.anzeigeAnzahlVeranstaltungen - 1;
            while (p > -1) {
                this.remove(this.panelVeranstaltungen[p]);
                --p;
            }
        }
        catch (ArrayIndexOutOfBoundsException | NullPointerException runtimeException) {
            // empty catch block
        }
        this.createVeranstaltungListePanels();
        this.panelVeranstaltungsliste.setBounds(((int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth() - 1000) / 2, ((int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight() - 1015) / 2, 1000, 720);
        this.mainPanel.add(this.panelVeranstaltungsliste);
    }

    private void addUhrPanelToMainPanel() {
        logging.logInfo((Object)"Zeige Uhr...");
        try {
            logging.logInfo((Object)"Remove Panels...");
            this.remove(this.panelLetzterEinsatz);
            int p = this.anzeigeAnzahlVeranstaltungen - 1;
            while (p > -1) {
                this.remove(this.panelVeranstaltungen[p]);
                --p;
            }
        }
        catch (ArrayIndexOutOfBoundsException | NullPointerException runtimeException) {
            // empty catch block
        }
        this.createUhrPanel();
        this.createEinsatzPanel();
        this.panelUhr.setBounds(((int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth() - 1000) / 2, ((int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight() - 795) / 2, 1000, 470);
        this.mainPanel.add(this.panelUhr);
        if (runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigenLetzenEinsatz").equals("1")) {
            this.mainPanel.add(this.panelLetzterEinsatz);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                logging.logInfo((Object)"Beende TerminDisplayAO");
                System.exit(0);
            }
        });
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        StartBildschirmAO.startDialog.setVisible(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        Thread threadUhr = new Thread(){

            @Override
            public void run() {
                while (true) {
                    try {
                        while (true) {
                            if (runApplication.unwetterwarnungStatus == 1) {
                                uhr.setText("                                                                                                                                                                                                                                                                   " + SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy  HH:mm:ss"));
                                TerminDisplayAO.this.datum.setText(SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy"));
                                TerminDisplayAO.this.uhrGross.setText(SbcUtils.timeStamp((String)"HH:mm:ss"));
                            } else {
                                uhr.setText("                                                                                                                                                                                                                                                                                                                                                          " + SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy  HH:mm:ss"));
                                TerminDisplayAO.this.datum.setText(SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy"));
                                TerminDisplayAO.this.uhrGross.setText(SbcUtils.timeStamp((String)"HH:mm:ss"));
                            }
                            Thread.sleep(1000L);
                        }
                    }
                    catch (InterruptedException e) {
                        logging.logPrintStackTrace((Exception)e);
                        continue;
                    }
                }
            }
        };
        Thread threadPanels = new Thread(){

            @Override
            public void run() {
                int counter = 0;
                int counterVeranstaltungsanschicht = 0;
                while (true) {
                    try {
                        while (true) {
                            int sleep;
                            TerminDisplayAO.this.anzeigeAnzahlVeranstaltungen = Integer.parseInt(runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeAnazahlVeranstaltungen"));
                            TerminDisplayAO.this.anzeigeAnzahlVeranstaltungenListe = Integer.parseInt(runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeAnazahlVeranstaltungListe"));
                            try {
                                TerminDisplayAO.this.remove(TerminDisplayAO.this.mainPanel);
                            }
                            catch (NullPointerException nullPointerException) {
                                // empty catch block
                            }
                            boolean istGerade = counter % 2 == 0;
                            TerminDisplayAO.this.mainPanel = new JPanel();
                            TerminDisplayAO.this.mainPanel.setLayout(null);
                            TerminDisplayAO.this.mainPanel.setPreferredSize(new Dimension((int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth(), (int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight() - 295));
                            if (istGerade) {
                                boolean istGeradeVeransatltungsansicht;
                                boolean bl = istGeradeVeransatltungsansicht = counterVeranstaltungsanschicht % 2 == 0;
                                if (istGeradeVeransatltungsansicht) {
                                    TerminDisplayAO.this.addVeranstaltungPanelToMainPanel();
                                    ++counterVeranstaltungsanschicht;
                                } else {
                                    TerminDisplayAO.this.addVeranstaltungListePanelToMainPanel();
                                    ++counterVeranstaltungsanschicht;
                                }
                            } else {
                                TerminDisplayAO.this.addUhrPanelToMainPanel();
                            }
                            if (runApplication.EINSTELLUNGEN.get("TerminDisplay_HintergrundBildAktivieren").equals("1")) {
                                ImageIcon bild = new ImageIcon(runApplication.EINSTELLUNGEN.get("TerminDisplay_HintergrundBild"));
                                TerminDisplayAO.this.hintergrund.setIcon(bild);
                                TerminDisplayAO.this.mainPanel.add(TerminDisplayAO.this.hintergrund);
                                TerminDisplayAO.this.hintergrund.setBounds(0, 0, (int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth(), (int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight() - 295);
                            }
                            TerminDisplayAO.this.add(TerminDisplayAO.this.mainPanel);
                            TerminDisplayAO.this.repaint();
                            TerminDisplayAO.this.validate();
                            ++counter;
                            if (istGerade) {
                                sleep = Integer.parseInt(String.valueOf(runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeDauerVeranstaltungen")) + "000");
                                Thread.sleep(sleep);
                                continue;
                            }
                            sleep = Integer.parseInt(String.valueOf(runApplication.EINSTELLUNGEN.get("TerminDisplay_AnzeigeDauerUhr")) + "000");
                            Thread.sleep(sleep);
                        }
                    }
                    catch (InterruptedException e) {
                        logging.logPrintStackTrace((Exception)e);
                        continue;
                    }
                }
            }
        };
        threadUhr.start();
        threadPanels.start();
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

