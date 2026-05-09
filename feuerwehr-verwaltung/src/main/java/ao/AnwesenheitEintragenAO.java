/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao;

import ao.AbstractFenster;
import ao.einsatz.EinsatzEintragenAO;
import ao.utils.ProzessBarAO;
import data.tabellen.TabelleAbwesenheit;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleFahrzeugeinteilung_temp;
import data.tabellen.TabelleStatistikbsw;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import data.tabellen.schicht.TabelleSchicht;
import data.tabellen.schicht.TabelleSchicht_mitglieder;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import data.tabellen.statistik.TabelleStatistikSonstigeVeranstaltung;
import data.tabellen.urlaubsplaner.TabelleUrlaub;
import go.Abwesenheit;
import go.Anwesenheit;
import go.Einsatz;
import go.Fahrzeugeinteilung_temp;
import go.StatistikBSW;
import go.StatistikEinsatz;
import go.StatistikSonstigeVeranstaltung;
import go.Veranstaltung;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.AbrechnungService;
import service.BerechtigunsManager;
import service.EMailService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.XML;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class AnwesenheitEintragenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonFahrzeugEinteilung;
    private JButton buttonFahrzeugBelegung;
    private JButton buttonEinsatzBericht;
    private JButton buttonFahrtenbuch;
    private JButton buttonVerdienstausfallbescheinugungErstellen;
    public static JButton buttonAbwesenheit;
    private JButton buttonAktualisieren;
    private JButton buttonFolgeEinsatz;
    private JButton buttonNachrichtSenden;
    private JButton buttonAtemschutzpass;
    public static JComboBox<String> veranstaltung;
    private JLabel veranstaltung_label;
    private static JCheckBox[] jCheckboxArray;
    private JLabel anzahlAllerMarkierterTeilnehmer_label;
    private JLabel anzahlAllerMarkierterTeilnehmer2_label;
    private int anzahlAllerMarkierterTeilnehmer = 0;
    public static StringBuilder abwesendePersonen;
    public static String vorhergehendesEvent;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public AnwesenheitEintragenAO() {
        super("FeuerwehrManagementSystem - Anwesenheit");
        logging.logInfo((Object)"Starte: AnwesenheitEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonFahrzeugEinteilung = new JButton("Fahrzeugeinteilung erstellen");
        this.buttonFahrzeugBelegung = new JButton("Fahrzeug Belegung");
        this.buttonEinsatzBericht = new JButton("Einsatz Bericht erstellen");
        buttonAbwesenheit = new JButton("Abwesenheit eintragen");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonVerdienstausfallbescheinugungErstellen = new JButton("Verdienstausfallbescheinigung");
        this.buttonFolgeEinsatz = new JButton("Folge Einsatz");
        this.buttonFolgeEinsatz.setToolTipText("Mit dieser Schaltfl\u00e4che werden alle Mitglider vom vorherigen Einsatz makiert");
        this.buttonNachrichtSenden = new JButton("E-Mail Erinnerung");
        this.buttonNachrichtSenden.setToolTipText("Benachrichtigung an die Mitglieder senden, die an der Veranstaltung Teilnehmen");
        this.buttonAtemschutzpass = new JButton("Atemschutzpass");
        this.buttonFahrtenbuch = new JButton("Fahrtenbuch");
        this.anzahlAllerMarkierterTeilnehmer_label = new JLabel("Anzahl markierter Teilnehmer: ");
        this.anzahlAllerMarkierterTeilnehmer2_label = new JLabel(Integer.toString(this.anzahlAllerMarkierterTeilnehmer));
        this.modulBeschreibung = new JLabel("Anwesenheit Eintragen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        abwesendePersonen = new StringBuilder();
        String[] liste = null;
        TabelleVeranstaltung veranstaltungListe = new TabelleVeranstaltung();
        try {
            liste = Utils.listToArrayOnlyFORComboBoxes(veranstaltungListe.getAllVeranstaltung());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        veranstaltung = new JComboBox<String>(liste);
        this.veranstaltung_label = new JLabel("Veranstaltung: ");
    }

    protected void labelErstellen() {
        veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
                TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                try {
                    int selectedVID = tabVeransatltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString());
                    int i = 0;
                    while (i < jCheckboxArray.length) {
                        int mID = Integer.parseInt(jCheckboxArray[i].getName());
                        if (tabAnwesenheit.getAnwesendStatus(mID, selectedVID) == 1) {
                            jCheckboxArray[i].setSelected(true);
                            jCheckboxArray[i].setBackground(Color.GREEN);
                            if (BerechtigunsManager.ber[85] == 1) {
                                jCheckboxArray[i].setEnabled(true);
                            } else {
                                jCheckboxArray[i].setEnabled(false);
                            }
                        } else {
                            jCheckboxArray[i].setSelected(false);
                            jCheckboxArray[i].setEnabled(true);
                            jCheckboxArray[i].setBackground(null);
                        }
                        ++i;
                    }
                    AnwesenheitEintragenAO.abwesenheitService();
                    AnwesenheitEintragenAO.this.urlaubService();
                    if (tabVeransatltung.getFahrzeugeinteilungStatus(selectedVID) == 0) {
                        AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setVisible(true);
                        AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setEnabled(true);
                    } else {
                        AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setVisible(false);
                        AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setEnabled(false);
                    }
                    if (veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                        AnwesenheitEintragenAO.this.buttonEinsatzBericht.setVisible(true);
                        AnwesenheitEintragenAO.this.buttonEinsatzBericht.setText("Einsatz Bericht \u00f6ffnen");
                        AnwesenheitEintragenAO.this.buttonAtemschutzpass.setVisible(true);
                    } else {
                        AnwesenheitEintragenAO.this.buttonEinsatzBericht.setVisible(false);
                        AnwesenheitEintragenAO.this.buttonAtemschutzpass.setVisible(false);
                    }
                    if (veranstaltung.getSelectedItem().toString().startsWith("Dienstabend")) {
                        buttonAbwesenheit.setVisible(true);
                    } else {
                        buttonAbwesenheit.setVisible(false);
                    }
                    if (runApplication.EINSTELLUNGEN.get("Fahrtenbuch").equals("1")) {
                        AnwesenheitEintragenAO.this.buttonFahrtenbuch.setVisible(true);
                    } else {
                        AnwesenheitEintragenAO.this.buttonFahrtenbuch.setVisible(false);
                    }
                    AnwesenheitEintragenAO.this.buttonAktualisieren.setEnabled(true);
                    AnwesenheitEintragenAO.this.anzahlAllerMarkierterTeilnehmer = tabAnwesenheit.getGesamtVeranstaltung(selectedVID);
                    AnwesenheitEintragenAO.this.anzahlAllerMarkierterTeilnehmer2_label.setText(Integer.toString(AnwesenheitEintragenAO.this.anzahlAllerMarkierterTeilnehmer));
                    if (BerechtigunsManager.ber[55] == 1) {
                        AnwesenheitEintragenAO.this.buttonNachrichtSenden.setEnabled(true);
                    } else {
                        AnwesenheitEintragenAO.this.buttonNachrichtSenden.setEnabled(false);
                    }
                }
                catch (NullPointerException | SQLException e) {
                    logging.logInfo((Object)e);
                }
            }
        });
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
        veranstaltung.setSelectedItem(runApplication.letzterVeranstaltungsname);
        logging.logInfo((Object)("HauptprogrammAO.letzterVeranstaltungsname = " + runApplication.letzterVeranstaltungsname));
        if (veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") && !runApplication.letzterVeranstaltungsname.equals("<bitte w\u00e4hlen>")) {
            veranstaltung.addItem(runApplication.letzterVeranstaltungsname);
            veranstaltung.setSelectedItem(runApplication.letzterVeranstaltungsname);
            logging.logWarning((Object)"Veranstaltung liegt NICHT im Sichtbarkeisbereich, wird als Workaround hinzugef\u00fcgt...");
        }
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        TabelleMitglied mitglieder = new TabelleMitglied();
        try {
            int GUI_laenge = mitglieder.getMitgliederCountGruppe1() / 4 * 23;
            this.setSize(750, 290 + GUI_laenge);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.setTitle("FeuerwehrManagementSystem - Anwesenheit");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.veranstaltung_label);
        this.add(veranstaltung);
        try {
            TabelleMitglied tabMitglieder = new TabelleMitglied();
            String[] labels = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
            int[] personalnummern = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
            JPanel panel = new JPanel(new GridLayout(0, 4));
            int CheckBoxNumber = tabMitglieder.getMitgliederCountGruppe1();
            jCheckboxArray = new JCheckBox[CheckBoxNumber];
            int x = 0;
            while (x < CheckBoxNumber) {
                AnwesenheitEintragenAO.jCheckboxArray[x] = new JCheckBox();
                jCheckboxArray[x].setText(labels[x]);
                jCheckboxArray[x].setName(Integer.toString(personalnummern[x]));
                jCheckboxArray[x].setBackground(null);
                jCheckboxArray[x].setToolTipText(String.valueOf(labels[x]) + " kann als Anwesend markiert werden...");
                panel.add(jCheckboxArray[x]);
                jCheckboxArray[x].addItemListener(this.createItemListener(x));
                logging.logInfo((Object)("F\u00fcge Mitglied: " + labels[x] + " hinzu...."));
                ++x;
            }
            this.add(panel, "Center");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.anzahlAllerMarkierterTeilnehmer_label);
        this.add(this.anzahlAllerMarkierterTeilnehmer2_label);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonAktualisieren);
        this.add(this.buttonEinsatzBericht);
        this.add(this.buttonFahrzeugEinteilung);
        this.add(this.buttonFahrzeugBelegung);
        this.add(this.buttonVerdienstausfallbescheinugungErstellen);
        this.add(buttonAbwesenheit);
        this.add(this.buttonFolgeEinsatz);
        this.add(this.buttonNachrichtSenden);
        this.add(this.buttonAtemschutzpass);
        this.add(this.buttonFahrtenbuch);
        this.buttonAktualisieren.setVisible(false);
        this.buttonFahrzeugEinteilung.setVisible(false);
        this.buttonFahrzeugBelegung.setVisible(false);
        this.buttonEinsatzBericht.setVisible(false);
        this.buttonVerdienstausfallbescheinugungErstellen.setVisible(false);
        buttonAbwesenheit.setVisible(false);
        veranstaltung.setEnabled(false);
        this.buttonFolgeEinsatz.setVisible(false);
        this.buttonNachrichtSenden.setVisible(false);
        this.buttonAtemschutzpass.setVisible(false);
        this.buttonFahrtenbuch.setVisible(false);
        if (BerechtigunsManager.ber[75] == 0) {
            this.buttonFahrtenbuch.setEnabled(false);
        }
        if (MyEvent.event.equals("0x0040")) {
            try {
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                logging.logInfo((Object)("Belge Veranstaltung f\u00fcr die Anwesenheit vor mit: " + runApplication.letzterVeranstaltungsname));
                int selectedVID = tabVeranstaltung.getVeranstaltungID(runApplication.letzterVeranstaltungsname);
                this.buttonSpeichern.setVisible(false);
                this.buttonAktualisieren.setVisible(true);
                veranstaltung.setEnabled(true);
                if (runApplication.EINSTELLUNGEN.get("emailModul").equals("1")) {
                    this.buttonNachrichtSenden.setVisible(true);
                    this.buttonNachrichtSenden.setEnabled(false);
                }
                if (selectedVID != 0) {
                    TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                    int i = 0;
                    while (i < jCheckboxArray.length) {
                        int mID = Integer.parseInt(jCheckboxArray[i].getName());
                        if (tabAnwesenheit.getAnwesendStatus(mID, selectedVID) == 1) {
                            jCheckboxArray[i].setSelected(true);
                            if (BerechtigunsManager.ber[85] == 1) {
                                jCheckboxArray[i].setEnabled(true);
                            } else {
                                jCheckboxArray[i].setEnabled(false);
                            }
                            jCheckboxArray[i].setBackground(Color.GREEN);
                        } else {
                            jCheckboxArray[i].setSelected(false);
                            jCheckboxArray[i].setEnabled(true);
                            jCheckboxArray[i].setBackground(null);
                        }
                        AnwesenheitEintragenAO.abwesenheitService();
                        ++i;
                    }
                    if (tabVeranstaltung.getFahrzeugeinteilungStatus(selectedVID) == 0) {
                        this.buttonFahrzeugEinteilung.setVisible(true);
                        this.buttonFahrzeugEinteilung.setEnabled(true);
                    } else {
                        this.buttonFahrzeugEinteilung.setVisible(false);
                        this.buttonFahrzeugEinteilung.setEnabled(false);
                    }
                    if (veranstaltung.getSelectedItem().toString().startsWith("Einsatz")) {
                        this.buttonEinsatzBericht.setVisible(true);
                        this.buttonEinsatzBericht.setText("Einsatz Bericht \u00f6ffnen");
                        this.buttonAtemschutzpass.setVisible(true);
                    } else {
                        this.buttonEinsatzBericht.setVisible(false);
                        this.buttonAtemschutzpass.setVisible(false);
                    }
                    if (veranstaltung.getSelectedItem().toString().startsWith("Dienstabend")) {
                        buttonAbwesenheit.setVisible(true);
                    } else {
                        buttonAbwesenheit.setVisible(false);
                    }
                    if (runApplication.EINSTELLUNGEN.get("Fahrtenbuch").equals("1")) {
                        this.buttonFahrtenbuch.setVisible(true);
                    } else {
                        this.buttonFahrtenbuch.setVisible(false);
                    }
                    this.buttonAktualisieren.setEnabled(true);
                    this.anzahlAllerMarkierterTeilnehmer = tabAnwesenheit.getGesamtVeranstaltung(selectedVID);
                    this.anzahlAllerMarkierterTeilnehmer2_label.setText(Integer.toString(this.anzahlAllerMarkierterTeilnehmer));
                    if (BerechtigunsManager.ber[55] == 1) {
                        this.buttonNachrichtSenden.setEnabled(true);
                    } else {
                        this.buttonNachrichtSenden.setEnabled(false);
                    }
                }
                logging.logInfo((Object)("Veranstaltung wurde vorbelegt mit: " + veranstaltung.getSelectedItem().toString()));
            }
            catch (SQLException e) {
                logging.logPrintStackTrace((Exception)e);
            }
        }
        if (MyEvent.event.equals("0x0010")) {
            this.buttonFolgeEinsatz.setVisible(true);
            int i = 0;
            while (i < jCheckboxArray.length) {
                if (jCheckboxArray[i].getText().equals(EinsatzEintragenAO.einsatzleiter.getSelectedItem().toString())) {
                    jCheckboxArray[i].setSelected(true);
                    jCheckboxArray[i].setEnabled(false);
                    jCheckboxArray[i].setBackground(Color.green);
                    logging.logInfo((Object)"Makiere Einsatzleiter");
                }
                ++i;
            }
        }
        if (runApplication.EINSTELLUNGEN.get("Schichtplaner").equals("1") && !veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
            logging.logInfo((Object)"Suche nach Schichtdaten...");
            TabelleSchicht tabSchicht = new TabelleSchicht();
            TabelleSchicht_mitglieder tabSchichtMitglieder = new TabelleSchicht_mitglieder();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            try {
                int vID = tabVeranstaltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString());
                String vDatum = tabVeranstaltung.getDatum(vID);
                int vZeitInMinuten = TimeCalculation.calculateDuration("00:00", tabVeranstaltung.getZeitStart(vID));
                int sID = tabSchicht.getSchichtIDF\u00fcrEreignis(vDatum, vZeitInMinuten);
                if (sID == 0) {
                    logging.logInfo((Object)"Suche mit Schicht mit Zeitoffset von 1440");
                    sID = tabSchicht.getSchichtIDF\u00fcrEreignis2(vDatum, vZeitInMinuten + 1440);
                }
                if (sID != 0) {
                    logging.logInfo((Object)("Schicht gefunden, makiere Mitglieder - SchichtID = " + sID));
                    String[] teilnehmerSchicht = Utils.listToArray(tabSchichtMitglieder.getMitglederEinerSchicht(sID));
                    int i = 0;
                    while (i < teilnehmerSchicht.length) {
                        int a = 0;
                        while (a < jCheckboxArray.length) {
                            if (jCheckboxArray[a].getText().equals(teilnehmerSchicht[i])) {
                                jCheckboxArray[a].setSelected(true);
                                jCheckboxArray[a].setBackground(Color.green);
                            }
                            ++a;
                        }
                        ++i;
                    }
                } else {
                    logging.logInfo((Object)"Keine Schicht f\u00fcr Einsatzzeit gefunden...Es wird nicht makiert...");
                }
            }
            catch (SQLException e) {
                logging.logPrintStackTrace((Exception)e);
            }
        }
        this.urlaubService();
    }

    private void urlaubService() {
        if (runApplication.EINSTELLUNGEN.get("Urlaubsplaner").equals("1") && !veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
            try {
                TabelleUrlaub tabUrlaub = new TabelleUrlaub();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                int vID = tabVeranstaltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString());
                String vDatum = tabVeranstaltung.getDatum(vID);
                String[] urlaub = Utils.listToArray(tabUrlaub.getMitgliederMitUrlaubByDatum(vDatum));
                int i = 0;
                while (i < jCheckboxArray.length) {
                    int u = 0;
                    while (u < urlaub.length) {
                        if (jCheckboxArray[i].getName().equals(urlaub[u])) {
                            jCheckboxArray[i].setBackground(Color.magenta);
                            jCheckboxArray[i].setEnabled(false);
                            jCheckboxArray[i].setToolTipText("Mitglied ist in Urlaub");
                            logging.logInfo((Object)(String.valueOf(jCheckboxArray[i].getText()) + " ->> Mitglied mit Urlaub markiert..."));
                        }
                        ++u;
                    }
                    ++i;
                }
            }
            catch (SQLException e) {
                logging.logPrintStackTrace((Exception)e);
            }
        }
    }

    public static void abwesenheitService() {
        try {
            TabelleAbwesenheit tabAbwesenehit = new TabelleAbwesenheit();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            int vID = tabVeranstaltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString());
            String[] abwesenheit = Utils.listToArray(tabAbwesenehit.getAbwesendeMitgliederIDListe(vID));
            int i = 0;
            while (i < jCheckboxArray.length) {
                int a = 0;
                while (a < abwesenheit.length) {
                    if (jCheckboxArray[i].getName().equals(abwesenheit[a])) {
                        jCheckboxArray[i].setBackground(Color.ORANGE);
                        jCheckboxArray[i].setEnabled(false);
                        jCheckboxArray[i].setToolTipText("Mitglied ist Abwesend");
                        logging.logInfo((Object)(String.valueOf(jCheckboxArray[i].getText()) + " ->> Mitglied Abwesend markiert..."));
                    }
                    ++a;
                }
                ++i;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private ItemListener createItemListener(final int i) {
        ItemListener result = new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (jCheckboxArray[i].isSelected()) {
                    AnwesenheitEintragenAO anwesenheitEintragenAO = AnwesenheitEintragenAO.this;
                    anwesenheitEintragenAO.anzahlAllerMarkierterTeilnehmer = anwesenheitEintragenAO.anzahlAllerMarkierterTeilnehmer + 1;
                    AnwesenheitEintragenAO.this.anzahlAllerMarkierterTeilnehmer2_label.setText(Integer.toString(AnwesenheitEintragenAO.this.anzahlAllerMarkierterTeilnehmer));
                } else if (!jCheckboxArray[i].isSelected()) {
                    AnwesenheitEintragenAO anwesenheitEintragenAO = AnwesenheitEintragenAO.this;
                    anwesenheitEintragenAO.anzahlAllerMarkierterTeilnehmer = anwesenheitEintragenAO.anzahlAllerMarkierterTeilnehmer - 1;
                    AnwesenheitEintragenAO.this.anzahlAllerMarkierterTeilnehmer2_label.setText(Integer.toString(AnwesenheitEintragenAO.this.anzahlAllerMarkierterTeilnehmer));
                }
            }
        };
        return result;
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonFolgeEinsatz.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                    TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                    TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                    int eID = tabEinsatz.getEinsatzIDByVeranstaltungID(tabVeranstaltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString())) - 1;
                    int vID = tabEinsatz.getVeranstaltungIDbyEinsatzID(eID);
                    logging.logInfo((Object)("Letzte Einsatznummer: " + eID));
                    int i = 0;
                    while (i < jCheckboxArray.length) {
                        if (tabAnwesenheit.getAnwesendStatus(Integer.parseInt(jCheckboxArray[i].getName()), vID) == 1) {
                            jCheckboxArray[i].setSelected(true);
                        } else {
                            jCheckboxArray[i].setSelected(false);
                            if (jCheckboxArray[i].isBackgroundSet()) {
                                logging.logWarning((Object)"WORKAROUND!!! - EinsatzLeiter ist nicht der gleiche wie beim vorherigen Einsatz...");
                                jCheckboxArray[i].setBackground(null);
                                jCheckboxArray[i].setEnabled(true);
                            }
                        }
                        ++i;
                    }
                    AnwesenheitEintragenAO.this.buttonFolgeEinsatz.setVisible(false);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                TabelleAbwesenheit tabAbwesenheit = new TabelleAbwesenheit();
                TabelleMitglied tabMitglieder = new TabelleMitglied();
                final TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
                TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
                TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                Anwesenheit anwesenheit = new Anwesenheit();
                Fahrzeugeinteilung_temp temp = new Fahrzeugeinteilung_temp();
                try {
                    int vID = tabVeransatltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString());
                    int jahr = tabVeransatltung.getJahrDerVeranstaltung(vID);
                    if (veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        logging.logInfo((Object)"Es wurde keine Veranstaltung ausgew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Fehlermeldung", 2);
                    } else {
                        int anzahlMitglieder = tabMitglieder.getMitgliederCountGruppe1();
                        int kID = tabVeransatltung.getVeranstaltungKategorieID(vID);
                        int x = 0;
                        while (x < anzahlMitglieder) {
                            int mID = Integer.parseInt(jCheckboxArray[x].getName());
                            if (jCheckboxArray[x].isSelected() && jCheckboxArray[x].isEnabled() && !jCheckboxArray[x].isBackgroundSet()) {
                                anwesenheit.setId(tabAnwesenheit.getNextNummer());
                                anwesenheit.setJahr(jahr);
                                anwesenheit.setVeranstaltungID(vID);
                                anwesenheit.setVeranstaltungKategorie(kID);
                                anwesenheit.setMitgliederID(mID);
                                tabAnwesenheit.insert(anwesenheit);
                                tabAbwesenheit.deleteOne(mID, vID);
                                jCheckboxArray[x].setSelected(true);
                                jCheckboxArray[x].setBackground(Color.GREEN);
                                if (BerechtigunsManager.ber[85] == 0) {
                                    jCheckboxArray[x].setEnabled(false);
                                }
                            } else if (!jCheckboxArray[x].isSelected() && jCheckboxArray[x].isBackgroundSet()) {
                                jCheckboxArray[x].setBackground(null);
                                tabAnwesenheit.deleteOne(mID, vID);
                                logging.logInfo((Object)(String.valueOf(jCheckboxArray[x].getText()) + " nimmt an der Veranstaltung nicht mehr teil"));
                            }
                            ++x;
                        }
                        AnwesenheitEintragenAO.abwesenheitService();
                        AnwesenheitEintragenAO.this.urlaubService();
                        int zf = tabAnwesenheit.getZFCountMitVeranstaltungsID(vID);
                        int gf = tabAnwesenheit.getGFCountMitVeranstaltungsID(vID) - zf;
                        int fm = tabAnwesenheit.getFMCountMitVeranstaltungsID(vID) - zf - gf;
                        if (tabVeransatltung.getVeranstaltungKategorieID(vID) == 1) {
                            TabelleStatistikEinsatz tabStatistikEinsatz = new TabelleStatistikEinsatz();
                            TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                            StatistikEinsatz statistikE = new StatistikEinsatz();
                            Einsatz einsatz = new Einsatz();
                            statistikE.setVeranstaltungID(vID);
                            statistikE.setMannstunden(tabStatistikEinsatz.getDauer(vID) * (fm + gf + zf));
                            tabStatistikEinsatz.updateMannstunden(statistikE);
                            logging.logInfo((Object)"Einsatz Mannstunden aktualisiert");
                            einsatz.setVeranstaltungID(vID);
                            einsatz.setStaerkeFM(fm);
                            einsatz.setStaerkeGF(gf);
                            einsatz.setStaerkeZF(zf);
                            tabEinsatz.updateStaerke(einsatz);
                            logging.logInfo((Object)"Einsatz St\u00e4rke aktualisiert");
                        } else if (tabVeransatltung.getVeranstaltungKategorieID(vID) == 3) {
                            TabelleStatistikbsw tabStatistikBSW = new TabelleStatistikbsw();
                            StatistikBSW statistikBSW = new StatistikBSW();
                            statistikBSW.setVeranstaltungID(vID);
                            statistikBSW.setMannstunden(tabStatistikBSW.getDauer(vID) * (fm + gf + zf));
                            tabStatistikBSW.updateMannstunden(statistikBSW);
                            logging.logInfo((Object)"BSW Mannstunden aktualisiert");
                        } else {
                            TabelleStatistikSonstigeVeranstaltung tabStatistikSonstVer = new TabelleStatistikSonstigeVeranstaltung();
                            StatistikSonstigeVeranstaltung sonstigeStatistik = new StatistikSonstigeVeranstaltung();
                            sonstigeStatistik.setVeranstaltungID(vID);
                            sonstigeStatistik.setMannstunden(tabStatistikSonstVer.getDauer(vID) * (fm + gf + zf));
                            tabStatistikSonstVer.updateMannstunden(sonstigeStatistik);
                            logging.logInfo((Object)"Dienst Mannstunden aktualisiert");
                        }
                        if (tabVeransatltung.getFahrzeugeinteilungStatus(vID) == 0) {
                            tabTemp.deleteAll();
                            runApplication.letzterVeranstaltungsname = veranstaltung.getSelectedItem().toString();
                            if (runApplication.letzterVeranstaltungsname.equals(veranstaltung.getSelectedItem().toString())) {
                                int x2 = 0;
                                while (x2 < anzahlMitglieder) {
                                    if (jCheckboxArray[x2].isSelected()) {
                                        int mID = Integer.parseInt(jCheckboxArray[x2].getName());
                                        int[] funktionen = tabLaufbahn.getLehrgangData(mID);
                                        temp.setMitgliederID(mID);
                                        temp.setDienstgradID(tabMitglieder.getDienstgradID(mID));
                                        temp.setKlasseC(funktionen[0]);
                                        temp.setKlasseB(funktionen[1]);
                                        temp.setMaschi(funktionen[11]);
                                        temp.setChef(funktionen[25]);
                                        temp.setTm1(funktionen[5]);
                                        temp.setAgt(funktionen[7]);
                                        temp.setTf(funktionen[14]);
                                        temp.setGf(funktionen[18]);
                                        temp.setZf(funktionen[19]);
                                        temp.setKorbsteuerung(funktionen[20]);
                                        temp.setDlkmaschi(funktionen[21]);
                                        temp.setRh(funktionen[22]);
                                        temp.setRs(funktionen[23]);
                                        temp.setRa(funktionen[24]);
                                        temp.setBeteiligung(tabAnwesenheit.getBeteiligungEinsatzDienst(mID));
                                        temp.setPosition(0);
                                        tabTemp.insert(temp);
                                    }
                                    ++x2;
                                }
                                AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, Konstante.BITTE_AKTUALISIEREN, "Fehlermeldung", 2);
                            }
                        }
                        if (runApplication.EINSTELLUNGEN.get("abrechnungModul").equals("1")) {
                            AbrechnungService.calculateAbrechnung(vID, kID, jahr);
                        }
                        if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1") && veranstaltung.getSelectedItem().toString().startsWith("BSW")) {
                            final int joomlaVID = vID;
                            Thread threadJoomla = new Thread(){

                                @Override
                                public void run() {
                                    try {
                                        Veranstaltung veranstaltungObjekt = new Veranstaltung();
                                        HashMap<String, String> map = tabVeransatltung.getVeranstaltungData(joomlaVID);
                                        veranstaltungObjekt.setId(joomlaVID);
                                        veranstaltungObjekt.setDatum(map.get("datum"));
                                        veranstaltungObjekt.setKategorie(Integer.parseInt(map.get("kategorie")));
                                        veranstaltungObjekt.setName(map.get("name"));
                                        veranstaltungObjekt.setName2(map.get("name2"));
                                        veranstaltungObjekt.setZeit(map.get("zeit"));
                                        veranstaltungObjekt.setZeitEnde(map.get("zeitEnde"));
                                        Joomla.updateVeranstaltung(veranstaltungObjekt);
                                    }
                                    catch (SQLException e) {
                                        logging.logPrintStackTrace((Exception)e);
                                    }
                                }
                            };
                            threadJoomla.start();
                        }
                        AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setEnabled(true);
                        logging.logInfo((Object)"Die Anwesenheit wurde aktualisiert");
                        logbuchEingabe.NeuerEintag("Anwesenheit wurde aktualisiert: " + veranstaltung.getSelectedItem().toString());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                vorhergehendesEvent = MyEvent.event;
                logging.logInfo((Object)("Merke vorhergehendes Event, damit ich ein neues erzeugen kann: " + vorhergehendesEvent));
                MyEvent.setEvent((String)"0");
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                Thread threadAnwesenheit = new Thread(){

                    @Override
                    public void run() {
                        try {
                            TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                            TabelleMitglied tabMitglieder = new TabelleMitglied();
                            final TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                            TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
                            TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                            Anwesenheit anwesenheit = new Anwesenheit();
                            Fahrzeugeinteilung_temp temp = new Fahrzeugeinteilung_temp();
                            TabelleAbwesenheit tabAbwesenheit = new TabelleAbwesenheit();
                            Abwesenheit abwesenheit = new Abwesenheit();
                            abwesendePersonen.setLength(0);
                            logging.logInfo((Object)"Losche Alle Temp Daten...");
                            tabTemp.deleteAll();
                            int vID = tabVeranstaltung.getVeranstaltungID(runApplication.letzterVeranstaltungsname);
                            int jahr = tabVeranstaltung.getJahrDerVeranstaltung(vID);
                            int kID = tabVeranstaltung.getVeranstaltungKategorieID(vID);
                            int anzahlMitglieder = tabMitglieder.getMitgliederCountGruppe1();
                            int x = 0;
                            while (x < anzahlMitglieder) {
                                int mID = Integer.parseInt(jCheckboxArray[x].getName());
                                if (jCheckboxArray[x].isSelected()) {
                                    anwesenheit.setId(tabAnwesenheit.getNextNummer());
                                    anwesenheit.setJahr(jahr);
                                    anwesenheit.setVeranstaltungID(vID);
                                    anwesenheit.setVeranstaltungKategorie(kID);
                                    anwesenheit.setMitgliederID(mID);
                                    tabAnwesenheit.insert(anwesenheit);
                                    jCheckboxArray[x].setBackground(Color.GREEN);
                                    if (BerechtigunsManager.ber[85] == 0) {
                                        jCheckboxArray[x].setEnabled(false);
                                    }
                                    int[] funktionen = tabLaufbahn.getLehrgangData(mID);
                                    temp.setMitgliederID(mID);
                                    temp.setDienstgradID(tabMitglieder.getDienstgradID(mID));
                                    temp.setKlasseC(funktionen[0]);
                                    temp.setKlasseB(funktionen[1]);
                                    temp.setMaschi(funktionen[11]);
                                    temp.setChef(funktionen[25]);
                                    temp.setTm1(funktionen[5]);
                                    temp.setAgt(funktionen[7]);
                                    temp.setTf(funktionen[14]);
                                    temp.setGf(funktionen[18]);
                                    temp.setZf(funktionen[19]);
                                    temp.setKorbsteuerung(funktionen[20]);
                                    temp.setDlkmaschi(funktionen[21]);
                                    temp.setRh(funktionen[22]);
                                    temp.setRs(funktionen[23]);
                                    temp.setRa(funktionen[24]);
                                    temp.setBeteiligung(tabAnwesenheit.getBeteiligungEinsatzDienst(mID));
                                    temp.setPosition(0);
                                    tabTemp.insert(temp);
                                    logging.logInfo((Object)(String.valueOf(jCheckboxArray[x].getText().toString()) + " wurde als Anwesend f\u00fcr die Veransatltung " + runApplication.letzterVeranstaltungsname + " eingetragen"));
                                    ProzessBarAO.progressbar.setValue(x * 100 / anzahlMitglieder);
                                } else if (!vorhergehendesEvent.equals("0x0010") && !vorhergehendesEvent.equals("0x0011")) {
                                    abwesenheit.setId(tabAbwesenheit.getNextNummer());
                                    abwesenheit.setJahr(jahr);
                                    abwesenheit.setMitgliederID(mID);
                                    abwesenheit.setVeranstaltungID(vID);
                                    if (jCheckboxArray[x].getToolTipText().toString().equals("Mitglied ist in Urlaub")) {
                                        abwesenheit.setGrund(3);
                                    } else {
                                        abwesenheit.setGrund(0);
                                    }
                                    abwesenheit.setVeranstaltungKategorie(tabVeranstaltung.getVeranstaltungKategorieID(vID));
                                    tabAbwesenheit.insert(abwesenheit);
                                    logging.logInfo((Object)(String.valueOf(jCheckboxArray[x].getText().toString()) + " wurde als Abwesend markiert STATUS: undefiniert"));
                                }
                                ++x;
                            }
                            AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setVisible(true);
                            buttonAbwesenheit.setVisible(true);
                            if (vorhergehendesEvent.equals("0x0010")) {
                                TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                                TabelleStatistikEinsatz tabStatistik = new TabelleStatistikEinsatz();
                                Einsatz einsatz = new Einsatz();
                                StatistikEinsatz statistik = new StatistikEinsatz();
                                int zf = tabAnwesenheit.getZFCountMitVeranstaltungsID(vID);
                                int gf = tabAnwesenheit.getGFCountMitVeranstaltungsID(vID) - zf;
                                int fm = tabAnwesenheit.getFMCountMitVeranstaltungsID(vID) - zf - gf;
                                einsatz.setVeranstaltungID(vID);
                                einsatz.setStaerkeFM(fm);
                                einsatz.setStaerkeGF(gf);
                                einsatz.setStaerkeZF(zf);
                                tabEinsatz.updateStaerke(einsatz);
                                statistik.setVeranstaltungID(vID);
                                statistik.setMannstunden(tabStatistik.getDauer(vID) * (fm + gf + zf));
                                tabStatistik.updateMannstunden(statistik);
                                logging.logInfo((Object)"Update statistikEinsatz.mannstunden und einsatz.staerke FM / GF / ZF");
                                AnwesenheitEintragenAO.this.buttonEinsatzBericht.setVisible(true);
                                AnwesenheitEintragenAO.this.buttonVerdienstausfallbescheinugungErstellen.setVisible(true);
                                AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setVisible(false);
                                AnwesenheitEintragenAO.this.buttonFahrzeugBelegung.setVisible(true);
                                buttonAbwesenheit.setVisible(false);
                                runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                                AnwesenheitEintragenAO.this.buttonFolgeEinsatz.setVisible(false);
                                AnwesenheitEintragenAO.this.buttonAtemschutzpass.setVisible(true);
                            }
                            if (vorhergehendesEvent.equals("0x0011")) {
                                TabelleStatistikbsw tabBSWstatistik = new TabelleStatistikbsw();
                                StatistikBSW bswStatistik = new StatistikBSW();
                                int bswZf = tabAnwesenheit.getZFCountMitVeranstaltungsID(vID);
                                int bswGf = tabAnwesenheit.getGFCountMitVeranstaltungsID(vID);
                                int bswFm = tabAnwesenheit.getFMCountMitVeranstaltungsID(vID);
                                bswStatistik.setVeranstaltungID(vID);
                                bswStatistik.setMannstunden(tabBSWstatistik.getDauer(vID) * (bswGf + bswFm + bswZf));
                                tabBSWstatistik.updateMannstunden(bswStatistik);
                                AnwesenheitEintragenAO.this.buttonFahrzeugEinteilung.setVisible(false);
                                buttonAbwesenheit.setVisible(false);
                                runApplication.letzterVeranstaltungsname = "<bitte w\u00e4hlen>";
                            }
                            if (vorhergehendesEvent.equals("0x0012")) {
                                TabelleStatistikSonstigeVeranstaltung tabSonstigeStatistik = new TabelleStatistikSonstigeVeranstaltung();
                                StatistikSonstigeVeranstaltung sonstigeStatistik = new StatistikSonstigeVeranstaltung();
                                int sonstigeZf = tabAnwesenheit.getZFCountMitVeranstaltungsID(vID);
                                int sonstigeGf = tabAnwesenheit.getGFCountMitVeranstaltungsID(vID);
                                int sonstigeFm = tabAnwesenheit.getFMCountMitVeranstaltungsID(vID);
                                sonstigeStatistik.setVeranstaltungID(vID);
                                sonstigeStatistik.setMannstunden(tabSonstigeStatistik.getDauer(vID) * (sonstigeGf + sonstigeFm + sonstigeZf));
                                tabSonstigeStatistik.updateMannstunden(sonstigeStatistik);
                            }
                            AnwesenheitEintragenAO.this.buttonSpeichern.setVisible(false);
                            AnwesenheitEintragenAO.this.buttonAktualisieren.setVisible(true);
                            if (runApplication.EINSTELLUNGEN.get("Fahrtenbuch").equals("1")) {
                                AnwesenheitEintragenAO.this.buttonFahrtenbuch.setVisible(true);
                            }
                            if (runApplication.EINSTELLUNGEN.get("abrechnungModul").equals("1")) {
                                AbrechnungService.calculateAbrechnung(vID, kID, jahr);
                            }
                            if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1") && veranstaltung.getSelectedItem().toString().startsWith("BSW")) {
                                final int joomlaVID = vID;
                                Thread threadJoomla = new Thread(){

                                    @Override
                                    public void run() {
                                        try {
                                            Veranstaltung veranstaltungObjekt = new Veranstaltung();
                                            HashMap<String, String> map = tabVeranstaltung.getVeranstaltungData(joomlaVID);
                                            veranstaltungObjekt.setId(joomlaVID);
                                            veranstaltungObjekt.setDatum(map.get("datum"));
                                            veranstaltungObjekt.setKategorie(Integer.parseInt(map.get("kategorie")));
                                            veranstaltungObjekt.setName(map.get("name"));
                                            veranstaltungObjekt.setName2(map.get("name2"));
                                            veranstaltungObjekt.setZeit(map.get("zeit"));
                                            veranstaltungObjekt.setZeitEnde(map.get("zeitEnde"));
                                            Joomla.updateVeranstaltung(veranstaltungObjekt);
                                        }
                                        catch (SQLException e) {
                                            logging.logPrintStackTrace((Exception)e);
                                        }
                                    }
                                };
                                threadJoomla.start();
                            }
                            MyEvent.setEvent((String)"0x0030");
                            logbuchEingabe.NeuerEintag("Anwesenheit wurde eingetragen: " + veranstaltung.getSelectedItem().toString());
                            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                            this.stop();
                        }
                        catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                            logging.logPrintStackTrace((Exception)e);
                        }
                    }
                };
                threadAnwesenheit.start();
            }
        });
        this.buttonNachrichtSenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    int msg = JOptionPane.showConfirmDialog(null, Konstante.SOLL_MITGLIED_INFORMIERT_WERDEN, "Frage", 0);
                    if (msg == 0) {
                        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        StringBuilder build = new StringBuilder();
                        String[] emailListe = Utils.listToArray(tabAnwesenheit.getAnwesendeMitgliederEMail(tabVeranstaltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString())));
                        int i = 0;
                        while (i < emailListe.length) {
                            if (!emailListe[i].equals("")) {
                                build.append(emailListe[i]);
                                build.append(", ");
                            }
                            ++i;
                        }
                        EMailService.EMailInformationServiceVeranstaltung(build.toString(), veranstaltung.getSelectedItem().toString());
                        JOptionPane.showMessageDialog(null, Konstante.SENDEN_ERFOLGREICH);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonEinsatzBericht.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle") | runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Eigene Vorlage / Dateienvorlage")) {
                    try {
                        String[] ist;
                        String dateinameXmlNeu;
                        String dateinameXml;
                        String dateinameDoc;
                        HashMap<String, String> map;
                        TabelleEinsatz tabEinsatz;
                        int msgAtemschutz;
                        TabelleEinsatz_bericht tabBericht = new TabelleEinsatz_bericht();
                        TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
                        int vID = tabVeransatltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString());
                        int errorCode = 0;
                        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtAtemschutzpassHinzuf\u00fcgen").equals("1") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle") && tabBericht.getAtemschutzStatus(vID) == 0) {
                            msgAtemschutz = JOptionPane.showConfirmDialog(null, Konstante.BITTE_ATEMSCHTZPASS_FUELLEN, "Frage", 0);
                            if (msgAtemschutz == 1) {
                                errorCode = -1;
                                logging.logInfo((Object)"Setze ErrorCode = -1 --> Benutzer hat den Atemschutzpass vergessen zu f\u00fcllen...");
                            } else {
                                tabEinsatz = new TabelleEinsatz();
                                map = tabEinsatz.getData(vID);
                                dateinameDoc = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/" + tabBericht.getDateiname(vID);
                                dateinameXml = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + ".xml";
                                dateinameXmlNeu = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + "_Neu.xml";
                                new File(dateinameDoc).renameTo(new File(dateinameXml));
                                logging.logInfo((Object)"Atemschutzpass: benenne DOC --> XML um...");
                                logging.logInfo((Object)("DOC-Dateiname: " + dateinameDoc));
                                logging.logInfo((Object)("XML-Dateiname: " + dateinameXml));
                                ist = new String[]{"TR11N", "TR11T", "TR11Z", "TR12N", "TR12T", "TR12Z", "TR21N", "TR21T", "TR21Z", "TR22N", "TR22T", "TR22Z", "TR31N", "TR31T", "TR31Z", "TR32N", "TR32T", "TR32Z", "TR41N", "TR41T", "TR41Z", "TR42N", "TR42T", "TR42Z"};
                                String tuppF\u00fchrer1Name = "";
                                String tuppF\u00fchrer1Zeit = "";
                                String tuppF\u00fchrer1Type = "";
                                String tuppMann1Name = "";
                                String tuppMann1Zeit = "";
                                String tuppMann1Type = "";
                                String tuppF\u00fchrer2Name = "";
                                String tuppF\u00fchrer2Zeit = "";
                                String tuppF\u00fchrer2Type = "";
                                String tuppMann2Name = "";
                                String tuppMann2Zeit = "";
                                String tuppMann2Type = "";
                                String tuppF\u00fchrer3Name = "";
                                String tuppF\u00fchrer3Zeit = "";
                                String tuppF\u00fchrer3Type = "";
                                String tuppMann3Name = "";
                                String tuppMann3Zeit = "";
                                String tuppMann3Type = "";
                                String tuppF\u00fchrer4Name = "";
                                String tuppF\u00fchrer4Zeit = "";
                                String tuppF\u00fchrer4Type = "";
                                String tuppMann4Name = "";
                                String tuppMann4Zeit = "";
                                String tuppMann4Type = "";
                                String[] zu = new String[]{tuppF\u00fchrer1Name, tuppF\u00fchrer1Type, tuppF\u00fchrer1Zeit, tuppMann1Name, tuppMann1Type, tuppMann1Zeit, tuppF\u00fchrer2Name, tuppF\u00fchrer2Type, tuppF\u00fchrer2Zeit, tuppMann2Name, tuppMann2Type, tuppMann2Zeit, tuppF\u00fchrer3Name, tuppF\u00fchrer3Type, tuppF\u00fchrer3Zeit, tuppMann3Name, tuppMann3Type, tuppMann3Zeit, tuppF\u00fchrer4Name, tuppF\u00fchrer4Type, tuppF\u00fchrer4Zeit, tuppMann4Name, tuppMann4Type, tuppMann4Zeit};
                                XML.createEinsatzBericht(ist, zu, dateinameXmlNeu, dateinameXml);
                                new File(dateinameXmlNeu).renameTo(new File(dateinameDoc));
                                logging.logInfo((Object)"Atemscutzpass: benenne XML --> DOC um...");
                                new File(dateinameXml).delete();
                                logging.logInfo((Object)"Atemschutzpass: L\u00f6sche Templatefile...");
                                tabBericht.updateAtemschutz(vID);
                                logging.logInfo((Object)"Bericht Tabelle aktualisiert mit dem Atemschutzeintrag!");
                            }
                        }
                        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtFahrzeugbelegungHinzuf\u00fcgen").equals("1") && runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("Word Schnittstelle") && tabBericht.getFahrzeugbelegungStatus(vID) == 0) {
                            msgAtemschutz = JOptionPane.showConfirmDialog(null, Konstante.BITTE_FAHRZEUGBELEGUNG_FUELLEN, "Frage", 0);
                            if (msgAtemschutz == 1) {
                                errorCode = -2;
                                logging.logInfo((Object)"Setze ErrorCode = -2 --> Benutzer hat die Fahrzeugbelegung vergessen zu f\u00fcllen...");
                            } else {
                                tabEinsatz = new TabelleEinsatz();
                                map = tabEinsatz.getData(vID);
                                dateinameDoc = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/" + tabBericht.getDateiname(vID);
                                dateinameXml = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + ".xml";
                                dateinameXmlNeu = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + tabBericht.getJahr(vID) + "/einsatzberichte/Einsatz_ID_" + map.get("einsatzNummer") + "_ID_" + map.get("einsatznummerOffiziell") + "_Neu.xml";
                                new File(dateinameDoc).renameTo(new File(dateinameXml));
                                logging.logInfo((Object)"Fahrzeugbelegung: benenne DOC --> XML um...");
                                logging.logInfo((Object)("DOC-Dateiname: " + dateinameDoc));
                                logging.logInfo((Object)("XML-Dateiname: " + dateinameXml));
                                ist = new String[]{"F1PO0", "F1PO1", "F1PO2", "F1PO3", "F1PO4", "F1PO5", "F1PO6", "F1PO7", "F1PO8", "F2PO0", "F2PO1", "F2PO2", "F2PO3", "F2PO4", "F2PO5", "F2PO6", "F2PO7", "F2PO8", "F3PO0", "F3PO1", "F3PO2", "F3PO3", "F3PO4", "F3PO5", "F3PO6", "F3PO7", "F3PO8", "F4PO0", "F4PO1", "F4PO2", "F4PO3", "F4PO4", "F4PO5", "F4PO6", "F4PO7", "F4PO8", "GH000", "GH001", "GH002", "GH003", "GH004", "GH005", "GH006", "GH007", "GH008", "GH009"};
                                String fahr1pos0 = "";
                                String fahr1pos1 = "";
                                String fahr1pos2 = "";
                                String fahr1pos3 = "";
                                String fahr1pos4 = "";
                                String fahr1pos5 = "";
                                String fahr1pos6 = "";
                                String fahr1pos7 = "";
                                String fahr1pos8 = "";
                                String fahr2pos0 = "";
                                String fahr2pos1 = "";
                                String fahr2pos2 = "";
                                String fahr2pos3 = "";
                                String fahr2pos4 = "";
                                String fahr2pos5 = "";
                                String fahr2pos6 = "";
                                String fahr2pos7 = "";
                                String fahr2pos8 = "";
                                String fahr3pos0 = "";
                                String fahr3pos1 = "";
                                String fahr3pos2 = "";
                                String fahr3pos3 = "";
                                String fahr3pos4 = "";
                                String fahr3pos5 = "";
                                String fahr3pos6 = "";
                                String fahr3pos7 = "";
                                String fahr3pos8 = "";
                                String fahr4pos0 = "";
                                String fahr4pos1 = "";
                                String fahr4pos2 = "";
                                String fahr4pos3 = "";
                                String fahr4pos4 = "";
                                String fahr4pos5 = "";
                                String fahr4pos6 = "";
                                String fahr4pos7 = "";
                                String fahr4pos8 = "";
                                String gh0 = "";
                                String gh1 = "";
                                String gh2 = "";
                                String gh3 = "";
                                String gh4 = "";
                                String gh5 = "";
                                String gh6 = "";
                                String gh7 = "";
                                String gh8 = "";
                                String gh9 = "";
                                String[] zu = new String[]{fahr1pos0, fahr1pos1, fahr1pos2, fahr1pos3, fahr1pos4, fahr1pos5, fahr1pos6, fahr1pos7, fahr1pos8, fahr2pos0, fahr2pos1, fahr2pos2, fahr2pos3, fahr2pos4, fahr2pos5, fahr2pos6, fahr2pos7, fahr2pos8, fahr3pos0, fahr3pos1, fahr3pos2, fahr3pos3, fahr3pos4, fahr3pos5, fahr3pos6, fahr3pos7, fahr3pos8, fahr4pos0, fahr4pos1, fahr4pos2, fahr4pos3, fahr4pos4, fahr4pos5, fahr4pos6, fahr4pos7, fahr4pos8, gh0, gh1, gh2, gh3, gh4, gh5, gh6, gh7, gh8, gh9};
                                XML.createEinsatzBericht(ist, zu, dateinameXmlNeu, dateinameXml);
                                new File(dateinameXmlNeu).renameTo(new File(dateinameDoc));
                                logging.logInfo((Object)"Fahrzeugbelegung: benenne XML --> DOC um...");
                                new File(dateinameXml).delete();
                                logging.logInfo((Object)"Fahrzeugbelegung: L\u00f6sche Templatefile...");
                                tabBericht.updateFahrzeugbelegung(vID);
                                logging.logInfo((Object)"Bericht Tabelle aktualisiert mit der Fahrzeugbelegung!");
                            }
                        }
                        if (errorCode == 0) {
                            logging.logInfo((Object)"\u00d6ffne Einsatzbeicht!");
                            AnwesenheitEintragenAO.this.dispose();
                            tabBericht.updateAtemschutz(vID);
                            tabBericht.updateFahrzeugbelegung(vID);
                            Desktop.getDesktop().open(new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/einsatzberichte/" + tabBericht.getDateiname(vID)));
                        }
                    }
                    catch (IOException | SQLException e1) {
                        logging.logPrintStackTrace((Exception)e1);
                    }
                } else if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("PDF (intern)")) {
                    AnwesenheitEintragenAO.this.dispose();
                    MyEvent.setEvent((String)"0x0006");
                    Steuerung.setStatus(Status.EINSATZ_BERICHT);
                    Steuerung.steuerung();
                }
            }
        });
        this.buttonVerdienstausfallbescheinugungErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.VERDIENSTAUSFALL);
                Steuerung.steuerung();
            }
        });
        buttonAbwesenheit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.letzterVeranstaltungsname = veranstaltung.getSelectedItem().toString();
                int x = 0;
                while (x < jCheckboxArray.length) {
                    if (!jCheckboxArray[x].isSelected()) {
                        abwesendePersonen.append(jCheckboxArray[x].getText().toString());
                        abwesendePersonen.append("\n");
                    }
                    ++x;
                }
                Steuerung.setStatus(Status.Abwesenheit);
                Steuerung.steuerung();
            }
        });
        this.buttonFahrzeugEinteilung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
                try {
                    if (tabTemp.getCount() == 0) {
                        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                        TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                        TabelleMitglied tabMitglieder = new TabelleMitglied();
                        Fahrzeugeinteilung_temp temp = new Fahrzeugeinteilung_temp();
                        int anzahlMitglieder = tabMitglieder.getMitgliederCountGruppe1();
                        runApplication.letzterVeranstaltungsname = veranstaltung.getSelectedItem().toString();
                        logging.logInfo((Object)("Setzte Globale Variable runApplication.letzterVeranstaltungsname = " + veranstaltung.getSelectedItem().toString()));
                        int x = 0;
                        while (x < anzahlMitglieder) {
                            if (jCheckboxArray[x].isSelected()) {
                                int mID = Integer.parseInt(jCheckboxArray[x].getName());
                                int[] funktionen = tabLaufbahn.getLehrgangData(mID);
                                temp.setMitgliederID(mID);
                                temp.setDienstgradID(tabMitglieder.getDienstgradID(mID));
                                temp.setKlasseC(funktionen[0]);
                                temp.setKlasseB(funktionen[1]);
                                temp.setMaschi(funktionen[11]);
                                temp.setChef(funktionen[25]);
                                temp.setTm1(funktionen[5]);
                                temp.setAgt(funktionen[7]);
                                temp.setTf(funktionen[14]);
                                temp.setGf(funktionen[18]);
                                temp.setZf(funktionen[19]);
                                temp.setKorbsteuerung(funktionen[20]);
                                temp.setDlkmaschi(funktionen[21]);
                                temp.setRh(funktionen[22]);
                                temp.setRs(funktionen[23]);
                                temp.setRa(funktionen[24]);
                                temp.setBeteiligung(tabAnwesenheit.getBeteiligungEinsatzDienst(mID));
                                temp.setPosition(0);
                                tabTemp.insert(temp);
                            }
                            ++x;
                        }
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                AnwesenheitEintragenAO.this.dispose();
                logging.logInfo((Object)"Schlie\u00dfe AnwesenheitEintragenAO und Starte FahrzeugEinteilungAO");
                MyEvent.setEvent((String)"0");
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                Thread threadFahrzeugeinteilung = new Thread(){

                    @Override
                    public void run() {
                        Steuerung.setStatus(Status.FAHRZEUGEINTEILUNG);
                        Steuerung.steuerung();
                    }
                };
                threadFahrzeugeinteilung.start();
            }
        });
        this.buttonFahrzeugBelegung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.letzterVeranstaltungsname = veranstaltung.getSelectedItem().toString();
                Steuerung.setStatus(Status.FAHRZEUG_BELEGUNG);
                Steuerung.steuerung();
            }
        });
        this.buttonAtemschutzpass.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.letzterVeranstaltungsname = veranstaltung.getSelectedItem().toString();
                MyEvent.setEvent((String)"0x0350");
                Steuerung.setStatus(Status.ATEMSCHUTZPASS_EINTRAG);
                Steuerung.steuerung();
            }
        });
        this.buttonFahrtenbuch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                runApplication.letzterVeranstaltungsname = veranstaltung.getSelectedItem().toString();
                MyEvent.setEvent((String)"0x0351");
                Steuerung.setStatus(Status.FAHRTENBUCH);
                Steuerung.steuerung();
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

