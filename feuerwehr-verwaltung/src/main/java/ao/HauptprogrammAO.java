/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.MyProperties
 *  utilities.SbcUtils
 *  utilities.hash
 */
package ao;

import ao.AbstractFenster;
import ao.statistik.AbwesenheitsStatistikAO;
import ao.statistik.AnwesenheitBrandsicherheitswachenAO;
import ao.statistik.AnwesenheitDienstabendAO;
import ao.statistik.AnwesenheitEinsatzAO;
import ao.statistik.AnwesenheitGesamtAO;
import ao.statistik.AusbildungsStatistikAO;
import ao.statistik.AusrueckezeitenAO;
import ao.statistik.BeteiligungsdauerAO;
import ao.statistik.BswMannstundenAO;
import ao.statistik.DauerAlarmfahrtAO;
import ao.statistik.EinsatzArtAO;
import ao.statistik.EinsatzFahrzeugStatistikAO;
import ao.statistik.EinsatzMannstundenAO;
import ao.statistik.EinsatzProMonatAO;
import ao.statistik.EinsatzProStundeAO;
import ao.statistik.EinsatzProWochentagAO;
import ao.statistik.EinsatzdauerAO;
import ao.statistik.FehlalarmeStatistikAO;
import ao.statistik.VeranstaltungAnwesenheitStatistikAO;
import ao.utils.ProzessBarAO;
import ao.utils.StartBildschirmAO;
import ao.utils.SystemTrayInfo;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.einstellungen.TabelleBerechtigunggruppe;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleKeyStore;
import data.tabellen.einstellungen.TabelleUser;
import data.tabellen.email.TabelleEMail_unwetterwarnung;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import listener.BeendenHauptprogrammAOListener;
import listener.DbBackupListener;
import listener.HilfeListener;
import listener.InfoListener;
import listener.UnwetterwarnungListener;
import logging.logging;
import run.runApplication;
import service.AutoBerichtService;
import service.BerechtigunsManager;
import service.DatensicherungService;
import service.InformationService;
import steuerung.Status;
import steuerung.Steuerung;
import thread.StatusCheck;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.MyProperties;
import utilities.SbcUtils;
import utilities.hash;
import utilities.logbuchEingabe;
import utilities_email.EmpfangenOpperationUnwetterwarnung;

public class HauptprogrammAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static JButton buttonBeenden;
    public static JButton buttonInfo;
    public static JButton buttonAnmelden;
    public static JButton buttonAbmelden;
    public static JButton buttonPasswort\u00c4ndern;
    public static JLabel anmeldeName;
    public static JButton buttonUnwetterwarnung;
    private JLabel logo;
    private JLabel dummy;
    private JLabel dummy2;
    public static JTextArea aufgabenListe;
    private JScrollPane pane;
    private static JLabel uhr;
    public static JLabel onlineStatus;
    private JButton buttonHilfe;
    private JButton buttonMitglieder;
    public static JButton buttonMitgliederVerwaltng;
    public static JButton buttonLaufbahnEintragen;
    public static JButton mitgliederakte;
    public static JButton buttonFahrzeugverwaltung;
    public static JButton buttonFahrzeugakte;
    public static JButton buttonAbrechnung;
    private JButton buttonOptionen;
    public static JButton buttonKategorienVerwalten;
    public static JButton buttonAbwesenheitsgrund;
    public static JButton buttonEinstellungenOeffnen;
    public static JButton lehrgangAnlegen;
    public static JButton buttonGeraetePr\u00fcfung;
    public static JButton buttonBenutzerAnlegen;
    public static JButton dbBackup;
    public static JButton karte;
    public static JButton produktKeyEingeben;
    private JButton berichte;
    public static JButton dokumentenexplorer;
    public static JButton einsatzBericht;
    public static JButton verdienstausfallbescheinigung;
    public static JButton jahresberichtErstellen;
    public static JButton briefErstellen;
    public static JButton m\u00e4ngelmeldung;
    public static JButton emailModul;
    public static JButton buttonBestandsliste;
    public static JButton buttonAnwesenheitNachtragen;
    public static JButton buttonAusbildungsplan;
    public static JButton buttonM\u00e4ngelmeldungBearbeiten;
    public static JButton buttonProtokoll;
    private JButton neueVeranstaltung;
    public static JButton buttonEinsatz;
    public static JButton buttonDienstabend;
    public static JButton buttonBSW;
    public static JButton buttonSonstige;
    public static JButton buttonFahrzeugeinteilungNachtragen;
    public static JButton buttonAusbildungsInhalt;
    public static JButton buttonAbwesenheitsgrundNachtragen;
    public static JButton buttonAtemschutzpassEintrag;
    public static JButton buttonVeranstaltungEditieren;
    public static JButton buttonSchichtplaner;
    public static JButton buttonUrlaubsplaner;
    public static JButton buttonFahrtenbuch;
    public static JButton buttonMitgliederVerfuegbarkeit;
    private JLabel dummyText2;
    private JLabel dummyText3;
    private JLabel dummyText4;
    private JLabel dummyText5;
    private JButton listen;
    public static JButton mitgliederListe;
    public static JButton geburtstagListe;
    public static JButton einsatzListe;
    public static JButton bswListe;
    public static JButton lehrgangListe;
    public static JButton anwesenheitListe;
    public static JButton arbeitgeberListe;
    public static JButton angehoerigenListe;
    public static JButton untersuchungListe;
    public static JButton beteiligungUebersichtListe;
    public static JButton mitgliederBankverbindungListe;
    public static JButton veranstaltungListe;
    public static JButton lehrgangsmeldung;
    public static JButton ausbildungplanListe;
    public static JButton atemschutzpass;
    public static JButton buttonSchichtplanListe;
    public static JButton buttonFahrtenbuchListe;
    public static JButton buttonLaufbahnListe;
    public static JButton buttonUrlaubsplanListe;
    private JButton statistik;
    public static JButton anwesennheitGesamt;
    public static JButton anwesenheitEinsatz;
    public static JButton anweseneheitDienstabend;
    public static JButton anweseneheitBrandsicherheitswachen;
    public static JButton abwesenheitDienstStatistik;
    public static JButton einsatzArt;
    public static JButton ausrueckezeiten;
    public static JButton einsatzdauer;
    public static JButton einsatzMannstunden;
    public static JButton einsatzProMonat;
    public static JButton einsatzProStunde;
    public static JButton einsatzProWochentag;
    public static JButton bswMannstunden;
    public static JButton fehlalarme;
    public static JButton beteiligungByVeranstaltung;
    public static JButton ausbildungsstatistik;
    public static JButton fahrzeugStatistik;
    public static JButton alarmfahrtDauer;
    public static JButton beteiligunsdauerStatistik;
    public static JLabel textAdminButtons;
    public static JButton buttonAdminGUI;
    private JPanel panelMitglieder;
    private JPanel panelOptionen;
    private JPanel panelVeranstaltung;
    private JPanel panelListen;
    private JPanel panelStatistik;
    private JPanel panelBerichte;

    public HauptprogrammAO() {
        super("FeuerwehrManagementSystem Version: 3.21");
        logging.logInfo((Object)"Starte: HauptprogrammAO");
    }

    protected void buttonErstellen() {
        buttonBeenden = new JButton("Programm beenden");
        buttonInfo = new JButton("Info");
        buttonAnmelden = new JButton("Anmelden");
        buttonAbmelden = new JButton("Abmelden");
        buttonPasswort\u00c4ndern = new JButton("Passwort \u00e4ndern");
        anmeldeName = new JLabel();
        this.buttonHilfe = new JButton("Hilfe");
        buttonUnwetterwarnung = new JButton("Aktive Unwetterwarnung");
        if (runApplication.BF == 0) {
            anmeldeName.setText("Angemeldet als: \u00f6ffentlich");
        } else {
            anmeldeName.setText("Verwaltungsbeh\u00f6rde (BF) // Angemeldet als: \u00f6ffentlich");
        }
        URL bildGelbURL = ((Object)((Object)this)).getClass().getClassLoader().getResource("images/statusGelb.jpg");
        ImageIcon statusGelb = new ImageIcon(bildGelbURL);
        onlineStatus = new JLabel(statusGelb);
        onlineStatus.setToolTipText("Internetverbingung wird gepr\u00fcft");
        uhr = new JLabel();
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1920.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1080.0) {
            aufgabenListe = new JTextArea(30, 21);
            this.logo = new JLabel(runApplication.bannerHauptprogramm);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() >= 1600.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() >= 900.0) {
            aufgabenListe = new JTextArea(30, 21);
            this.logo = new JLabel(runApplication.bannerHauptprogramm);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1440.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 900.0) {
            aufgabenListe = new JTextArea(30, 16);
            this.logo = new JLabel(runApplication.bannerHauptprogramm);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 768.0) {
            aufgabenListe = new JTextArea(31, 16);
            this.logo = new JLabel(runApplication.bannerHauptprogramm);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
            aufgabenListe = new JTextArea(8, 105);
            this.logo = new JLabel(runApplication.bannerHauptprogramm);
        } else {
            aufgabenListe = new JTextArea(8, 105);
            this.logo = new JLabel(runApplication.bannerHauptprogramm);
            logging.logInfo((Object)("Bildschirmaufl\u00f6sung wird nicht unterst\u00fctzt. Aktelle Aufl\u00f6sung ist: " + runApplication.bildschirmgr\u00f6\u00dfe.getWidth() + "x" + runApplication.bildschirmgr\u00f6\u00dfe.getHeight()));
            int msg = JOptionPane.showConfirmDialog(null, Konstante.BILDSCHIRMAUFL\u00d6SUNG_WIRD_NICHT_UNTERST\u00dcTZT, "Frage", 0);
            if (msg == 0) {
                logging.logInfo((Object)"Programm wird mit falscher Bildschirmaufl\u00f6sung gestartet...");
            } else if (msg == 1) {
                logging.logInfo((Object)"Programm wird beendet... GRUND: Falsche Bildschirmaufl\u00f6sung");
                System.exit(0);
            }
        }
        aufgabenListe.setLineWrap(true);
        aufgabenListe.setWrapStyleWord(true);
        aufgabenListe.setEditable(false);
        this.pane = new JScrollPane(aufgabenListe);
        this.pane.setVerticalScrollBarPolicy(22);
        this.buttonMitglieder = new JButton("Mitglieder- / Fahrzeugverwaltung");
        buttonMitgliederVerwaltng = new JButton("Mitgliederverwaltung");
        mitgliederakte = new JButton("Mitgliederakte");
        buttonAbrechnung = new JButton("Abrechnung");
        this.dummyText4 = new JLabel();
        buttonFahrzeugakte = new JButton("Fahrzeugakte");
        buttonFahrzeugverwaltung = new JButton("Fahrzeugeverwaltung");
        buttonLaufbahnEintragen = new JButton("Mitgliederlaufbahn Pflegen");
        this.buttonOptionen = new JButton("Optionen / Verwaltung");
        buttonEinstellungenOeffnen = new JButton("Programmeinstellungen");
        buttonKategorienVerwalten = new JButton("Kategorien Verwalten");
        buttonAbwesenheitsgrund = new JButton("Abwesenheitsgrund anlegen");
        buttonGeraetePr\u00fcfung = new JButton("Ger\u00e4tepr\u00fcfung");
        buttonBenutzerAnlegen = new JButton("Benutzerverwaltung");
        dbBackup = new JButton("Datensicherung");
        karte = new JButton("Einsatzgebiet");
        produktKeyEingeben = new JButton("Produkt-Key eingeben");
        lehrgangAnlegen = new JButton("Mitgliederfunktionen Verwalten");
        this.neueVeranstaltung = new JButton("Veranstaltung / Anwesenheit");
        buttonEinsatz = new JButton("Einsatz anlegen");
        buttonBSW = new JButton("BSW anlegen");
        buttonDienstabend = new JButton("Dienstabend anlegen");
        buttonSonstige = new JButton("Sonstige Veranstaltungen");
        this.dummyText2 = new JLabel();
        buttonAnwesenheitNachtragen = new JButton("Anwesenheit eintragen");
        buttonAbwesenheitsgrundNachtragen = new JButton("Abwesenheit eintragen");
        buttonFahrzeugeinteilungNachtragen = new JButton("Fahrzeugeinteilung erstellen");
        this.dummyText3 = new JLabel();
        buttonAusbildungsInhalt = new JButton("Ausbildungsinhalte eintragen");
        buttonAtemschutzpassEintrag = new JButton("Atemschutzpass eintragen");
        buttonVeranstaltungEditieren = new JButton("Veranstaltung editieren");
        buttonSchichtplaner = new JButton("Schichtplaner");
        buttonUrlaubsplaner = new JButton("Urlaubsplaner");
        buttonFahrtenbuch = new JButton("Fahrtenbuch eintragen");
        buttonMitgliederVerfuegbarkeit = new JButton("Mitglieder Verf\u00fcgbarkeit");
        this.listen = new JButton("Listen");
        mitgliederListe = new JButton("Mitgliederliste");
        geburtstagListe = new JButton("Geburtstagsliste");
        einsatzListe = new JButton("Einsatzliste");
        bswListe = new JButton("BSW Liste");
        lehrgangListe = new JButton("Lehrgangliste");
        anwesenheitListe = new JButton("Anwesenheitsliste");
        arbeitgeberListe = new JButton("Arbeitgeberliste");
        angehoerigenListe = new JButton("Angeh\u00f6rigenliste");
        untersuchungListe = new JButton("G26/3 u. G25-Liste");
        beteiligungUebersichtListe = new JButton("Beteiligung \u00dcbersicht");
        mitgliederBankverbindungListe = new JButton("Bankverbindung Liste");
        veranstaltungListe = new JButton("Veranstaltungsliste");
        lehrgangsmeldung = new JButton("Bef\u00f6r. / Lehrgangsmeldung");
        lehrgangsmeldung.setToolTipText("Bef\u00f6rderungen / Lehrgangsmeldung erstellen");
        ausbildungplanListe = new JButton("Ausbildungsplan");
        atemschutzpass = new JButton("Atemschutzpass");
        buttonSchichtplanListe = new JButton("Schichtplan Liste");
        buttonFahrtenbuchListe = new JButton("Fahrtenbuch Liste");
        buttonLaufbahnListe = new JButton("Mitgliederlaufbahn Liste");
        buttonUrlaubsplanListe = new JButton("Urlaubsplan");
        this.berichte = new JButton("Berichte / Dokumente");
        jahresberichtErstellen = new JButton("Jahresbericht erstellen");
        verdienstausfallbescheinigung = new JButton("Verdienstausfallbescheinigung");
        dokumentenexplorer = new JButton("Dokumentexplorer");
        briefErstellen = new JButton("Brief erstellen");
        einsatzBericht = new JButton("Einsatz Bericht erstellen");
        m\u00e4ngelmeldung = new JButton("M\u00e4ngelmeldung erstellen");
        emailModul = new JButton("E-Mail Modul");
        buttonBestandsliste = new JButton("Bestandsverwaltung");
        buttonAusbildungsplan = new JButton("Ausbildungsplan erstellen");
        buttonM\u00e4ngelmeldungBearbeiten = new JButton("M\u00e4ngelmeldung bearbeiten");
        buttonProtokoll = new JButton("Protokoll / T\u00e4tigkeitsbericht");
        this.statistik = new JButton("Statistik");
        anwesennheitGesamt = new JButton("Anwesenheit Gesamt");
        anwesenheitEinsatz = new JButton("Einsatzbeteiligung");
        anweseneheitDienstabend = new JButton("Dienstbeteiligung");
        anweseneheitBrandsicherheitswachen = new JButton("BSW-Beteiligung");
        einsatzArt = new JButton("Einsatz Art");
        ausrueckezeiten = new JButton("Ausr\u00fcckzeiten");
        einsatzdauer = new JButton("Durchschn. Einsatzdauer");
        einsatzMannstunden = new JButton("Einsatz-Mannstunden");
        einsatzProMonat = new JButton("Eins\u00e4tze Pro Monat");
        einsatzProStunde = new JButton("Einsatz Pro Stunde");
        bswMannstunden = new JButton("BSW-Mannstunden");
        abwesenheitDienstStatistik = new JButton("Abwesenheitsgr\u00fcnde");
        einsatzProWochentag = new JButton("Einsatz Pro Wochentag");
        fehlalarme = new JButton("Fehlalarme");
        beteiligungByVeranstaltung = new JButton("Beteiligung bei ...");
        ausbildungsstatistik = new JButton("Ausbildungsstatistik");
        fahrzeugStatistik = new JButton("Fahrzeugstatistik");
        alarmfahrtDauer = new JButton("Alarmfahrtdauer");
        beteiligunsdauerStatistik = new JButton("Beteiligungsdauer");
        this.dummyText5 = new JLabel();
        textAdminButtons = new JLabel("Administratorenbereich:");
        buttonAdminGUI = new JButton("Administrator Bereich");
    }

    protected void labelErstellen() {
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
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1920.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1080.0) {
            logging.logInfo((Object)"Starte GUI mit: 1920x1080");
            this.setSize(1560, 870 + runApplication.hightGUIOffset);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() >= 1600.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() >= 900.0) {
            logging.logInfo((Object)"Starte GUI mit: 1600x900 ++");
            this.setSize(1560, 870 + runApplication.hightGUIOffset);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1440.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 900.0) {
            logging.logInfo((Object)"Starte GUI mit: 1440x900");
            this.setSize(1440, 870 + runApplication.hightGUIOffset);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 768.0) {
            logging.logInfo((Object)"Starte GUI mit: 1366x768");
            this.setSize(1366, 768);
        } else if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
            logging.logInfo((Object)"Starte GUI mit: 1280x1024");
            this.setSize(1280, 1024);
        } else {
            logging.logInfo((Object)("Starte GUI mit: " + runApplication.bildschirmgr\u00f6\u00dfe.getWidth() + "x" + runApplication.bildschirmgr\u00f6\u00dfe.getHeight()));
            int w = (int)runApplication.bildschirmgr\u00f6\u00dfe.getWidth();
            int h = (int)runApplication.bildschirmgr\u00f6\u00dfe.getHeight();
            this.setSize(w, h);
        }
        this.setTitle("FeuerwehrManagementSystem Version: 3.21");
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(buttonUnwetterwarnung);
        this.add(uhr);
        if (runApplication.EINSTELLUNGEN.get("onlineStatus").equals("1")) {
            this.add(onlineStatus);
        }
        this.add(this.logo);
        this.add(this.dummy2);
        this.panelVeranstaltung = new JPanel(new GridLayout(20, 1));
        this.getContentPane().add("Center", this.panelVeranstaltung);
        this.panelVeranstaltung.add(this.neueVeranstaltung);
        if (runApplication.EINSTELLUNGEN.get("modulVeranstaltung").equals("1")) {
            this.panelVeranstaltung.add(buttonEinsatz);
            this.panelVeranstaltung.add(buttonBSW);
            this.panelVeranstaltung.add(buttonDienstabend);
            this.panelVeranstaltung.add(buttonSonstige);
        }
        if (runApplication.EINSTELLUNGEN.get("Schichtplaner").equals("1")) {
            this.panelVeranstaltung.add(buttonSchichtplaner);
        }
        if (runApplication.EINSTELLUNGEN.get("Urlaubsplaner").equals("1")) {
            this.panelVeranstaltung.add(buttonUrlaubsplaner);
        }
        if (runApplication.EINSTELLUNGEN.get("modulVeranstaltung").equals("1")) {
            this.panelVeranstaltung.add(buttonVeranstaltungEditieren);
            this.panelVeranstaltung.add(this.dummyText2);
            this.panelVeranstaltung.add(buttonAnwesenheitNachtragen);
            this.panelVeranstaltung.add(buttonAbwesenheitsgrundNachtragen);
            this.panelVeranstaltung.add(buttonAtemschutzpassEintrag);
        }
        if (runApplication.EINSTELLUNGEN.get("modulMitgliederVerf\u00fcgbarkeit").equals("1")) {
            this.panelVeranstaltung.add(buttonMitgliederVerfuegbarkeit);
        }
        if (runApplication.EINSTELLUNGEN.get("modulAusbildungsplan").equals("1")) {
            this.panelVeranstaltung.add(buttonAusbildungsInhalt);
        }
        if (runApplication.EINSTELLUNGEN.get("modulFahrzeugeinteilung").equals("1")) {
            this.panelVeranstaltung.add(this.dummyText3);
            this.panelVeranstaltung.add(buttonFahrzeugeinteilungNachtragen);
        }
        if (runApplication.EINSTELLUNGEN.get("Fahrtenbuch").equals("1")) {
            this.panelVeranstaltung.add(buttonFahrtenbuch);
        }
        this.panelMitglieder = new JPanel(new GridLayout(20, 1));
        this.getContentPane().add("Center", this.panelMitglieder);
        this.panelMitglieder.add(this.buttonMitglieder);
        this.panelMitglieder.add(buttonMitgliederVerwaltng);
        this.panelMitglieder.add(buttonLaufbahnEintragen);
        this.panelMitglieder.add(mitgliederakte);
        if (runApplication.EINSTELLUNGEN.get("abrechnungModul").equals("1")) {
            this.panelMitglieder.add(buttonAbrechnung);
        }
        this.panelMitglieder.add(this.dummyText4);
        this.panelMitglieder.add(buttonFahrzeugverwaltung);
        this.panelMitglieder.add(buttonFahrzeugakte);
        this.panelMitglieder.add(buttonGeraetePr\u00fcfung);
        this.panelListen = new JPanel(new GridLayout(20, 1));
        this.getContentPane().add("Center", this.panelListen);
        this.panelListen.add(this.listen);
        if (runApplication.EINSTELLUNGEN.get("modulVeranstaltung").equals("1")) {
            this.panelListen.add(veranstaltungListe);
        }
        this.panelListen.add(einsatzListe);
        this.panelListen.add(bswListe);
        this.panelListen.add(mitgliederListe);
        this.panelListen.add(geburtstagListe);
        this.panelListen.add(mitgliederBankverbindungListe);
        this.panelListen.add(lehrgangListe);
        this.panelListen.add(untersuchungListe);
        this.panelListen.add(anwesenheitListe);
        this.panelListen.add(arbeitgeberListe);
        this.panelListen.add(angehoerigenListe);
        this.panelListen.add(beteiligungUebersichtListe);
        this.panelListen.add(lehrgangsmeldung);
        if (runApplication.EINSTELLUNGEN.get("modulAusbildungsplan").equals("1")) {
            this.panelListen.add(ausbildungplanListe);
        }
        if (runApplication.EINSTELLUNGEN.get("modulVeranstaltung").equals("1")) {
            this.panelListen.add(atemschutzpass);
        }
        this.panelListen.add(buttonLaufbahnListe);
        if (runApplication.EINSTELLUNGEN.get("Schichtplaner").equals("1")) {
            this.panelListen.add(buttonSchichtplanListe);
        }
        if (runApplication.EINSTELLUNGEN.get("Fahrtenbuch").equals("1")) {
            this.panelListen.add(buttonFahrtenbuchListe);
        }
        if (runApplication.EINSTELLUNGEN.get("Urlaubsplaner").equals("1")) {
            this.panelListen.add(buttonUrlaubsplanListe);
        }
        this.panelBerichte = new JPanel(new GridLayout(20, 1));
        this.getContentPane().add("Center", this.panelBerichte);
        this.panelBerichte.add(this.berichte);
        this.panelBerichte.add(dokumentenexplorer);
        if (runApplication.EINSTELLUNGEN.get("emailModul").equals("1")) {
            this.panelBerichte.add(emailModul);
        }
        this.panelBerichte.add(briefErstellen);
        if (runApplication.EINSTELLUNGEN.get("EinsatzBerichtArt").equals("PDF (intern)")) {
            this.panelBerichte.add(einsatzBericht);
        }
        this.panelBerichte.add(jahresberichtErstellen);
        if (runApplication.EINSTELLUNGEN.get("modulVeranstaltung").equals("1")) {
            this.panelBerichte.add(verdienstausfallbescheinigung);
        }
        this.panelBerichte.add(buttonProtokoll);
        this.panelBerichte.add(m\u00e4ngelmeldung);
        this.panelBerichte.add(buttonM\u00e4ngelmeldungBearbeiten);
        this.panelBerichte.add(buttonBestandsliste);
        if (runApplication.EINSTELLUNGEN.get("modulAusbildungsplan").equals("1")) {
            this.panelBerichte.add(buttonAusbildungsplan);
        }
        this.panelStatistik = new JPanel(new GridLayout(20, 1));
        this.getContentPane().add("Center", this.panelStatistik);
        this.panelStatistik.add(this.statistik);
        this.panelStatistik.add(anwesenheitEinsatz);
        this.panelStatistik.add(anweseneheitDienstabend);
        this.panelStatistik.add(abwesenheitDienstStatistik);
        this.panelStatistik.add(anweseneheitBrandsicherheitswachen);
        this.panelStatistik.add(beteiligunsdauerStatistik);
        this.panelStatistik.add(anwesennheitGesamt);
        this.panelStatistik.add(beteiligungByVeranstaltung);
        this.panelStatistik.add(ausbildungsstatistik);
        this.panelStatistik.add(einsatzArt);
        this.panelStatistik.add(fehlalarme);
        this.panelStatistik.add(fahrzeugStatistik);
        this.panelStatistik.add(ausrueckezeiten);
        this.panelStatistik.add(alarmfahrtDauer);
        this.panelStatistik.add(einsatzdauer);
        this.panelStatistik.add(einsatzMannstunden);
        this.panelStatistik.add(bswMannstunden);
        this.panelStatistik.add(einsatzProWochentag);
        this.panelStatistik.add(einsatzProStunde);
        this.panelStatistik.add(einsatzProMonat);
        this.panelOptionen = new JPanel(new GridLayout(20, 1));
        this.getContentPane().add("Center", this.panelOptionen);
        this.panelOptionen.add(this.buttonOptionen);
        if (runApplication.EINSTELLUNGEN.get("modulEinsatzgebiet").equals("1")) {
            this.panelOptionen.add(karte);
        }
        this.panelOptionen.add(buttonBenutzerAnlegen);
        this.panelOptionen.add(buttonEinstellungenOeffnen);
        if (runApplication.EINSTELLUNGEN.get("modulVeranstaltung").equals("1")) {
            this.panelOptionen.add(buttonKategorienVerwalten);
        }
        this.panelOptionen.add(lehrgangAnlegen);
        if (runApplication.EINSTELLUNGEN.get("modulVeranstaltung").equals("1")) {
            this.panelOptionen.add(buttonAbwesenheitsgrund);
        }
        this.panelOptionen.add(dbBackup);
        try {
            if (!hash.decodeHashCode((String)new TabelleKeyStore().get("Nummer1")).equals("0")) {
                this.panelOptionen.add(produktKeyEingeben);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.panelOptionen.add(this.dummyText5);
        this.panelOptionen.add(textAdminButtons);
        this.panelOptionen.add(buttonAdminGUI);
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        TitledBorder rahmen = BorderFactory.createTitledBorder(lowerEtched, "Informationsbereich");
        this.pane.setBorder(rahmen);
        this.add(this.pane);
        this.add(this.dummy);
        this.add(buttonAnmelden);
        this.add(buttonAbmelden);
        this.add(buttonPasswort\u00c4ndern);
        this.add(buttonInfo);
        this.add(this.buttonHilfe);
        this.add(buttonBeenden);
        this.add(anmeldeName);
        buttonUnwetterwarnung.setPreferredSize(new Dimension(250, 15));
        buttonUnwetterwarnung.setBackground(Color.RED);
        buttonUnwetterwarnung.setForeground(Color.WHITE);
        buttonUnwetterwarnung.setVisible(false);
        buttonMitgliederVerwaltng.setVisible(false);
        mitgliederakte.setVisible(false);
        buttonFahrzeugverwaltung.setVisible(false);
        buttonFahrzeugakte.setVisible(false);
        buttonAbrechnung.setVisible(false);
        buttonLaufbahnEintragen.setVisible(false);
        buttonEinsatz.setVisible(false);
        buttonBSW.setVisible(false);
        buttonDienstabend.setVisible(false);
        buttonSonstige.setVisible(false);
        buttonAusbildungsInhalt.setVisible(false);
        buttonFahrzeugeinteilungNachtragen.setVisible(false);
        buttonAnwesenheitNachtragen.setVisible(false);
        buttonAbwesenheitsgrundNachtragen.setVisible(false);
        buttonAtemschutzpassEintrag.setVisible(false);
        buttonVeranstaltungEditieren.setVisible(false);
        buttonSchichtplaner.setVisible(false);
        buttonUrlaubsplaner.setVisible(false);
        buttonFahrtenbuch.setVisible(false);
        buttonMitgliederVerfuegbarkeit.setVisible(false);
        mitgliederListe.setVisible(false);
        geburtstagListe.setVisible(false);
        einsatzListe.setVisible(false);
        bswListe.setVisible(false);
        lehrgangListe.setVisible(false);
        anwesenheitListe.setVisible(false);
        arbeitgeberListe.setVisible(false);
        angehoerigenListe.setVisible(false);
        untersuchungListe.setVisible(false);
        beteiligungUebersichtListe.setVisible(false);
        mitgliederBankverbindungListe.setVisible(false);
        veranstaltungListe.setVisible(false);
        lehrgangsmeldung.setVisible(false);
        ausbildungplanListe.setVisible(false);
        atemschutzpass.setVisible(false);
        buttonSchichtplanListe.setVisible(false);
        buttonFahrtenbuchListe.setVisible(false);
        buttonLaufbahnListe.setVisible(false);
        buttonUrlaubsplanListe.setVisible(false);
        jahresberichtErstellen.setVisible(false);
        verdienstausfallbescheinigung.setVisible(false);
        dokumentenexplorer.setVisible(false);
        briefErstellen.setVisible(false);
        einsatzBericht.setVisible(false);
        m\u00e4ngelmeldung.setVisible(false);
        emailModul.setVisible(false);
        buttonBestandsliste.setVisible(false);
        buttonAusbildungsplan.setVisible(false);
        buttonM\u00e4ngelmeldungBearbeiten.setVisible(false);
        buttonProtokoll.setVisible(false);
        anwesenheitEinsatz.setVisible(false);
        anwesennheitGesamt.setVisible(false);
        anweseneheitDienstabend.setVisible(false);
        anweseneheitBrandsicherheitswachen.setVisible(false);
        einsatzArt.setVisible(false);
        ausrueckezeiten.setVisible(false);
        einsatzdauer.setVisible(false);
        einsatzMannstunden.setVisible(false);
        einsatzProMonat.setVisible(false);
        einsatzProStunde.setVisible(false);
        bswMannstunden.setVisible(false);
        abwesenheitDienstStatistik.setVisible(false);
        einsatzProWochentag.setVisible(false);
        fehlalarme.setVisible(false);
        beteiligungByVeranstaltung.setVisible(false);
        ausbildungsstatistik.setVisible(false);
        fahrzeugStatistik.setVisible(false);
        alarmfahrtDauer.setVisible(false);
        beteiligunsdauerStatistik.setVisible(false);
        buttonKategorienVerwalten.setVisible(false);
        buttonAbwesenheitsgrund.setVisible(false);
        buttonEinstellungenOeffnen.setVisible(false);
        buttonGeraetePr\u00fcfung.setVisible(false);
        buttonBenutzerAnlegen.setVisible(false);
        dbBackup.setVisible(false);
        karte.setVisible(false);
        produktKeyEingeben.setVisible(false);
        lehrgangAnlegen.setVisible(false);
        textAdminButtons.setVisible(false);
        buttonAdminGUI.setVisible(false);
        buttonAbmelden.setVisible(false);
        buttonPasswort\u00c4ndern.setVisible(false);
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1366.0 | runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1440.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 768.0 | runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 900.0) {
            logging.logInfo((Object)"Dezimiere die Groe\u00dfe der ButtonPanel in HapuprogtrammAO bei 1366 x 768 od. 1440 x 900");
            this.panelListen.setPreferredSize(new Dimension(180, 500));
            this.panelVeranstaltung.setPreferredSize(new Dimension(180, 500));
            this.panelMitglieder.setPreferredSize(new Dimension(180, 500));
            this.panelBerichte.setPreferredSize(new Dimension(180, 500));
            this.panelStatistik.setPreferredSize(new Dimension(180, 500));
            this.panelOptionen.setPreferredSize(new Dimension(180, 500));
        }
    }

    protected void boxenHinzufuegen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                if (buttonBeenden.isEnabled()) {
                    buttonBeenden.doClick();
                }
            }
        });
    }

    protected void actionErzeugen() {
        buttonBeenden.addActionListener((ActionListener)((Object)new BeendenHauptprogrammAOListener((JFrame)((Object)this))));
        buttonInfo.addActionListener((ActionListener)((Object)new InfoListener((JFrame)((Object)this))));
        this.buttonHilfe.addActionListener((ActionListener)((Object)new HilfeListener((JFrame)((Object)this))));
        buttonUnwetterwarnung.addActionListener((ActionListener)((Object)new UnwetterwarnungListener((JFrame)((Object)this))));
        buttonAnmelden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ANMELDUNG);
                Steuerung.steuerung();
            }
        });
        buttonAbmelden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logbuchEingabe.NeuerEintag("Benutzer hat sich abgemeldet");
                buttonAnmelden.setVisible(true);
                buttonAbmelden.setVisible(false);
                buttonPasswort\u00c4ndern.setVisible(false);
                BerechtigunsManager.ber = new TabelleBerechtigunggruppe().getAll(0, 1);
                BerechtigunsManager.ber2 = new TabelleBerechtigunggruppe().getAll(0, 2);
                runApplication.loginName = "public";
                logging.logInfo((Object)("Ein Benutzer hat sich abgemeldet: " + runApplication.loginName));
                anmeldeName.setText("Angemeldet als: \u00f6ffentlich");
                BerechtigunsManager.berechtigungDokumente();
                BerechtigunsManager.berechtigungListen();
                BerechtigunsManager.berechtigungMitglieder();
                BerechtigunsManager.berechtigungOptionen();
                BerechtigunsManager.berechtigungStatistik();
                BerechtigunsManager.berechtigungVeranstaltung();
            }
        });
        buttonPasswort\u00c4ndern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MEIN_PASSWORT);
                Steuerung.steuerung();
            }
        });
        karte.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.KARTE);
                Steuerung.steuerung();
            }
        });
        this.statistik.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (runApplication.EINSTELLUNGEN.get("Statistik2").equals("1")) {
                    Steuerung.setStatus(Status.STATISTIK);
                    Steuerung.steuerung();
                } else if (!anwesenheitEinsatz.isVisible()) {
                    anwesenheitEinsatz.setVisible(true);
                    anwesennheitGesamt.setVisible(true);
                    anweseneheitDienstabend.setVisible(true);
                    anweseneheitBrandsicherheitswachen.setVisible(true);
                    einsatzArt.setVisible(true);
                    ausrueckezeiten.setVisible(true);
                    einsatzdauer.setVisible(true);
                    einsatzMannstunden.setVisible(true);
                    einsatzProMonat.setVisible(true);
                    einsatzProStunde.setVisible(true);
                    bswMannstunden.setVisible(true);
                    abwesenheitDienstStatistik.setVisible(true);
                    einsatzProWochentag.setVisible(true);
                    fehlalarme.setVisible(true);
                    beteiligungByVeranstaltung.setVisible(true);
                    ausbildungsstatistik.setVisible(true);
                    fahrzeugStatistik.setVisible(true);
                    alarmfahrtDauer.setVisible(true);
                    beteiligunsdauerStatistik.setVisible(true);
                    BerechtigunsManager.berechtigungStatistik();
                } else {
                    anwesenheitEinsatz.setVisible(false);
                    anwesennheitGesamt.setVisible(false);
                    anweseneheitDienstabend.setVisible(false);
                    anweseneheitBrandsicherheitswachen.setVisible(false);
                    einsatzArt.setVisible(false);
                    ausrueckezeiten.setVisible(false);
                    einsatzdauer.setVisible(false);
                    einsatzMannstunden.setVisible(false);
                    einsatzProMonat.setVisible(false);
                    einsatzProStunde.setVisible(false);
                    bswMannstunden.setVisible(false);
                    abwesenheitDienstStatistik.setVisible(false);
                    einsatzProWochentag.setVisible(false);
                    fehlalarme.setVisible(false);
                    beteiligungByVeranstaltung.setVisible(false);
                    ausbildungsstatistik.setVisible(false);
                    fahrzeugStatistik.setVisible(false);
                    alarmfahrtDauer.setVisible(false);
                    beteiligunsdauerStatistik.setVisible(false);
                }
            }
        });
        beteiligunsdauerStatistik.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BeteiligungsdauerAO.start(0, Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        alarmfahrtDauer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                DauerAlarmfahrtAO.start();
            }
        });
        fahrzeugStatistik.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzFahrzeugStatistikAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        ausbildungsstatistik.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AusbildungsStatistikAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        beteiligungByVeranstaltung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                VeranstaltungAnwesenheitStatistikAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        fehlalarme.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                FehlalarmeStatistikAO.start();
            }
        });
        einsatzProWochentag.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzProWochentagAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        abwesenheitDienstStatistik.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AbwesenheitsStatistikAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        bswMannstunden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BswMannstundenAO.start();
            }
        });
        einsatzProStunde.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzProStundeAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        einsatzProMonat.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzProMonatAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        einsatzMannstunden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzMannstundenAO.start();
            }
        });
        einsatzdauer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzdauerAO.start();
            }
        });
        ausrueckezeiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AusrueckezeitenAO.start();
            }
        });
        einsatzArt.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                EinsatzArtAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        anweseneheitBrandsicherheitswachen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitBrandsicherheitswachenAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        anweseneheitDienstabend.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitDienstabendAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        anwesenheitEinsatz.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitEinsatzAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        anwesennheitGesamt.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AnwesenheitGesamtAO.start(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
            }
        });
        this.berichte.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!jahresberichtErstellen.isVisible()) {
                    jahresberichtErstellen.setVisible(true);
                    verdienstausfallbescheinigung.setVisible(true);
                    dokumentenexplorer.setVisible(true);
                    briefErstellen.setVisible(true);
                    einsatzBericht.setVisible(true);
                    m\u00e4ngelmeldung.setVisible(true);
                    emailModul.setVisible(true);
                    buttonBestandsliste.setVisible(true);
                    buttonAusbildungsplan.setVisible(true);
                    buttonM\u00e4ngelmeldungBearbeiten.setVisible(true);
                    buttonProtokoll.setVisible(true);
                    BerechtigunsManager.berechtigungDokumente();
                } else {
                    jahresberichtErstellen.setVisible(false);
                    verdienstausfallbescheinigung.setVisible(false);
                    dokumentenexplorer.setVisible(false);
                    briefErstellen.setVisible(false);
                    einsatzBericht.setVisible(false);
                    m\u00e4ngelmeldung.setVisible(false);
                    emailModul.setVisible(false);
                    buttonBestandsliste.setVisible(false);
                    buttonAusbildungsplan.setVisible(false);
                    buttonM\u00e4ngelmeldungBearbeiten.setVisible(false);
                    buttonProtokoll.setVisible(false);
                }
            }
        });
        buttonProtokoll.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROTOKOLL);
                Steuerung.steuerung();
            }
        });
        buttonM\u00e4ngelmeldungBearbeiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MAENGELMELDUNG_BEARBEITEN);
                Steuerung.steuerung();
            }
        });
        buttonAusbildungsplan.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("Ausbildungsplan wird geladen... Bitte warten...");
                Thread threadAusbildungsplanStart = new Thread(){

                    @Override
                    public void run() {
                        Steuerung.setStatus(Status.AUSBILDUNGSPLAN);
                        Steuerung.steuerung();
                    }
                };
                threadAusbildungsplanStart.start();
            }
        });
        emailModul.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.EMAIL_MODUL);
                Steuerung.steuerung();
            }
        });
        m\u00e4ngelmeldung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MAENGELMELDUNG);
                Steuerung.steuerung();
            }
        });
        einsatzBericht.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.EINSATZ_BERICHT);
                Steuerung.steuerung();
            }
        });
        briefErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BRIEF);
                Steuerung.steuerung();
            }
        });
        dokumentenexplorer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.DOKUMENTENEXPLORER);
                Steuerung.steuerung();
            }
        });
        verdienstausfallbescheinigung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.VERDIENSTAUSFALL);
                Steuerung.steuerung();
            }
        });
        jahresberichtErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.JAHRESBERICHT);
                Steuerung.steuerung();
            }
        });
        this.listen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!mitgliederListe.isVisible()) {
                    mitgliederListe.setVisible(true);
                    einsatzListe.setVisible(true);
                    bswListe.setVisible(true);
                    lehrgangListe.setVisible(true);
                    anwesenheitListe.setVisible(true);
                    arbeitgeberListe.setVisible(true);
                    angehoerigenListe.setVisible(true);
                    untersuchungListe.setVisible(true);
                    beteiligungUebersichtListe.setVisible(true);
                    mitgliederBankverbindungListe.setVisible(true);
                    veranstaltungListe.setVisible(true);
                    lehrgangsmeldung.setVisible(true);
                    ausbildungplanListe.setVisible(true);
                    atemschutzpass.setVisible(true);
                    geburtstagListe.setVisible(true);
                    buttonSchichtplanListe.setVisible(true);
                    buttonFahrtenbuchListe.setVisible(true);
                    buttonLaufbahnListe.setVisible(true);
                    buttonUrlaubsplanListe.setVisible(true);
                    BerechtigunsManager.berechtigungListen();
                } else {
                    mitgliederListe.setVisible(false);
                    einsatzListe.setVisible(false);
                    bswListe.setVisible(false);
                    lehrgangListe.setVisible(false);
                    anwesenheitListe.setVisible(false);
                    arbeitgeberListe.setVisible(false);
                    angehoerigenListe.setVisible(false);
                    untersuchungListe.setVisible(false);
                    beteiligungUebersichtListe.setVisible(false);
                    mitgliederBankverbindungListe.setVisible(false);
                    veranstaltungListe.setVisible(false);
                    ausbildungplanListe.setVisible(false);
                    lehrgangsmeldung.setVisible(false);
                    atemschutzpass.setVisible(false);
                    geburtstagListe.setVisible(false);
                    buttonSchichtplanListe.setVisible(false);
                    buttonFahrtenbuchListe.setVisible(false);
                    buttonLaufbahnListe.setVisible(false);
                    buttonUrlaubsplanListe.setVisible(false);
                }
            }
        });
        buttonUrlaubsplanListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.URLAUBSPLAN_LISTE);
                Steuerung.steuerung();
            }
        });
        buttonLaufbahnListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MITGLIEDER_LAUFBAHN);
                Steuerung.steuerung();
            }
        });
        buttonFahrtenbuchListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.FAHRTENBUCH_LISTE);
                Steuerung.steuerung();
            }
        });
        buttonSchichtplanListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.SCHICHTPLAN_LISTE);
                Steuerung.steuerung();
            }
        });
        geburtstagListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.GEBURTSTAG_LISTE);
                Steuerung.steuerung();
            }
        });
        atemschutzpass.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ATEMSCHUTZPASS);
                Steuerung.steuerung();
            }
        });
        ausbildungplanListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.AUSBILDUNGPLAN_LISTE);
                Steuerung.steuerung();
            }
        });
        lehrgangsmeldung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.LEHRGANGSMELDUNG);
                Steuerung.steuerung();
            }
        });
        veranstaltungListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("Veranstaltungsliste wird geladen... Bitte warten...");
                Thread threadVeranstaltungListe = new Thread(){

                    @Override
                    public void run() {
                        Steuerung.setStatus(Status.VERANSTALTUNG_LISTE);
                        Steuerung.steuerung();
                    }
                };
                threadVeranstaltungListe.start();
            }
        });
        mitgliederBankverbindungListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BANKVERBINDUNG_LISTE);
                Steuerung.steuerung();
            }
        });
        beteiligungUebersichtListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BETEILIGUNG_UEBERSICHT_LISTE);
                Steuerung.steuerung();
            }
        });
        untersuchungListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.UNTERSUCHUNG_LISTE);
                Steuerung.steuerung();
            }
        });
        angehoerigenListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ANGEHOERIGEN_LISTE);
                Steuerung.steuerung();
            }
        });
        arbeitgeberListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ARBEITGEBER_LISTE);
                Steuerung.steuerung();
            }
        });
        anwesenheitListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ANWESENHEIT_LISTE);
                Steuerung.steuerung();
            }
        });
        lehrgangListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("Bitte haben Sie einen Moment Geduld...");
                Thread threadLehrgang = new Thread(){

                    @Override
                    public void run() {
                        Steuerung.setStatus(Status.LEHRGANG_LISTE);
                        Steuerung.steuerung();
                    }
                };
                threadLehrgang.start();
            }
        });
        bswListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("BSW-Liste wird geladen... Bitte warten...");
                Thread threadBSWListe = new Thread(){

                    @Override
                    public void run() {
                        Steuerung.setStatus(Status.BSW_LISTE);
                        Steuerung.steuerung();
                    }
                };
                threadBSWListe.start();
            }
        });
        einsatzListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("Einsatzliste wird geladen... Bitte warten...");
                Thread threadEinstzListe = new Thread(){

                    @Override
                    public void run() {
                        Steuerung.setStatus(Status.EINSATZ_LISTE);
                        Steuerung.steuerung();
                    }
                };
                threadEinstzListe.start();
            }
        });
        mitgliederListe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MITGLIEDER_LISTE);
                Steuerung.steuerung();
            }
        });
        this.neueVeranstaltung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!buttonEinsatz.isVisible()) {
                    buttonEinsatz.setVisible(true);
                    buttonBSW.setVisible(true);
                    buttonDienstabend.setVisible(true);
                    buttonSonstige.setVisible(true);
                    buttonAnwesenheitNachtragen.setVisible(true);
                    buttonAbwesenheitsgrundNachtragen.setVisible(true);
                    buttonFahrzeugeinteilungNachtragen.setVisible(true);
                    buttonAusbildungsInhalt.setVisible(true);
                    buttonAtemschutzpassEintrag.setVisible(true);
                    buttonVeranstaltungEditieren.setVisible(true);
                    buttonSchichtplaner.setVisible(true);
                    buttonUrlaubsplaner.setVisible(true);
                    buttonFahrtenbuch.setVisible(true);
                    buttonMitgliederVerfuegbarkeit.setVisible(true);
                    BerechtigunsManager.berechtigungVeranstaltung();
                } else {
                    buttonEinsatz.setVisible(false);
                    buttonBSW.setVisible(false);
                    buttonDienstabend.setVisible(false);
                    buttonSonstige.setVisible(false);
                    buttonAnwesenheitNachtragen.setVisible(false);
                    buttonAbwesenheitsgrundNachtragen.setVisible(false);
                    buttonFahrzeugeinteilungNachtragen.setVisible(false);
                    buttonAusbildungsInhalt.setVisible(false);
                    buttonAtemschutzpassEintrag.setVisible(false);
                    buttonVeranstaltungEditieren.setVisible(false);
                    buttonSchichtplaner.setVisible(false);
                    buttonFahrtenbuch.setVisible(false);
                    buttonUrlaubsplaner.setVisible(false);
                    buttonMitgliederVerfuegbarkeit.setVisible(false);
                }
            }
        });
        buttonMitgliederVerfuegbarkeit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MITGLIEDER_VERFUEGBARKEIT);
                Steuerung.steuerung();
            }
        });
        buttonFahrtenbuch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.FAHRTENBUCH);
                Steuerung.steuerung();
            }
        });
        buttonSchichtplaner.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.SCHICHTPLANER);
                Steuerung.steuerung();
            }
        });
        buttonUrlaubsplaner.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.URLAUBSPLANER);
                Steuerung.steuerung();
            }
        });
        buttonVeranstaltungEditieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.VERANSTALTUNG_EDITIEREN);
                Steuerung.steuerung();
            }
        });
        buttonAtemschutzpassEintrag.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ATEMSCHUTZPASS_EINTRAG);
                Steuerung.steuerung();
            }
        });
        buttonBSW.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BSW_EINTARGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonOptionen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!dbBackup.isVisible()) {
                    buttonKategorienVerwalten.setVisible(true);
                    buttonAbwesenheitsgrund.setVisible(true);
                    buttonEinstellungenOeffnen.setVisible(true);
                    buttonBenutzerAnlegen.setVisible(true);
                    dbBackup.setVisible(true);
                    karte.setVisible(true);
                    produktKeyEingeben.setVisible(true);
                    lehrgangAnlegen.setVisible(true);
                    BerechtigunsManager.berechtigungOptionen();
                } else {
                    buttonKategorienVerwalten.setVisible(false);
                    buttonAbwesenheitsgrund.setVisible(false);
                    buttonEinstellungenOeffnen.setVisible(false);
                    buttonBenutzerAnlegen.setVisible(false);
                    karte.setVisible(false);
                    dbBackup.setVisible(false);
                    produktKeyEingeben.setVisible(false);
                    lehrgangAnlegen.setVisible(false);
                }
            }
        });
        buttonAdminGUI.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ADMINBEREICH_STARTEN);
                Steuerung.steuerung();
            }
        });
        lehrgangAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.LEHRGANG_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        produktKeyEingeben.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0051");
                Steuerung.setStatus(Status.LIZENZ_KEY);
                Steuerung.steuerung();
            }
        });
        dbBackup.addActionListener((ActionListener)((Object)new DbBackupListener((JFrame)((Object)this))));
        buttonBestandsliste.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BESTANDSLISTE);
                Steuerung.steuerung();
            }
        });
        buttonBenutzerAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BENUTZER_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        buttonFahrzeugeinteilungNachtragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.FAHRZEUGEINTEILUNG_NACHTRAGEN);
                Steuerung.steuerung();
            }
        });
        buttonGeraetePr\u00fcfung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.GERAETEPRUEFUNG);
                Steuerung.steuerung();
            }
        });
        buttonAusbildungsInhalt.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.AUSBILDUNGINHALT_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        buttonAbwesenheitsgrundNachtragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0041");
                Steuerung.setStatus(Status.Abwesenheit);
                Steuerung.steuerung();
            }
        });
        buttonAnwesenheitNachtragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0040");
                Steuerung.setStatus(Status.ANWESENHEIT_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        buttonEinstellungenOeffnen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.EINSTELLUNGEN);
                Steuerung.steuerung();
            }
        });
        buttonAbwesenheitsgrund.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ABWESENHEITSGRUND_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        buttonKategorienVerwalten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (BerechtigunsManager.ber[0] == 0 && BerechtigunsManager.ber[38] == 0 && BerechtigunsManager.ber[39] == 0 && BerechtigunsManager.ber[40] == 0 && BerechtigunsManager.ber[46] == 0 && BerechtigunsManager.ber[47] == 0) {
                    JOptionPane.showMessageDialog(null, Konstante.KEINE_BERECHTIGUNG_VERF\u00dcGBAR, "Warnung", 2);
                } else {
                    Steuerung.setStatus(Status.KATEGORIEN_EDITIEREN);
                    Steuerung.steuerung();
                }
            }
        });
        buttonDienstabend.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0002");
                Steuerung.setStatus(Status.VERANSTALTUNG_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        buttonSonstige.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0003");
                Steuerung.setStatus(Status.VERANSTALTUNG_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        buttonEinsatz.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (new TabelleFahrzeug().countALL() == 0) {
                        JOptionPane.showMessageDialog(null, Konstante.KEINE_FAHRZEUGE, "Warnung", 2);
                    } else {
                        Steuerung.setStatus(Status.EINSATZ_EINTRAGEN);
                        Steuerung.steuerung();
                    }
                }
                catch (HeadlessException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonMitglieder.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!buttonMitgliederVerwaltng.isVisible()) {
                    buttonMitgliederVerwaltng.setVisible(true);
                    mitgliederakte.setVisible(true);
                    buttonFahrzeugverwaltung.setVisible(true);
                    buttonFahrzeugakte.setVisible(true);
                    buttonAbrechnung.setVisible(true);
                    buttonLaufbahnEintragen.setVisible(true);
                    buttonGeraetePr\u00fcfung.setVisible(true);
                    BerechtigunsManager.berechtigungMitglieder();
                } else {
                    buttonMitgliederVerwaltng.setVisible(false);
                    mitgliederakte.setVisible(false);
                    buttonFahrzeugverwaltung.setVisible(false);
                    buttonFahrzeugakte.setVisible(false);
                    buttonAbrechnung.setVisible(false);
                    buttonLaufbahnEintragen.setVisible(false);
                    buttonGeraetePr\u00fcfung.setVisible(false);
                }
            }
        });
        buttonLaufbahnEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.LAUFBAHN_EINTRAG);
                Steuerung.steuerung();
            }
        });
        buttonAbrechnung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ABRECHNUNG);
                Steuerung.steuerung();
            }
        });
        buttonFahrzeugakte.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.FAHRZEUGAKTE);
                Steuerung.steuerung();
            }
        });
        buttonFahrzeugverwaltung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0004");
                Steuerung.setStatus(Status.FAHREZUG_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        mitgliederakte.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MITGLIEDERAKTE);
                Steuerung.steuerung();
            }
        });
        buttonMitgliederVerwaltng.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MITGLIEDER_ANLEGEN);
                Steuerung.steuerung();
            }
        });
    }

    public void fensterAnzeigen() {
        MyProperties userProperties;
        logging.logInfo((Object)"GUI wird angezeigt...");
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        if (runApplication.EINSTELLUNGEN.get("onlineStatus").equals("1")) {
            runApplication.checkOnlieStatus();
        }
        if ((userProperties = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/user.properties")).sourceFileExists() && runApplication.EINSTELLUNGEN.get("anmeldungSpeichernErlauben").equals("1")) {
            userProperties.loadVars();
            if (userProperties.getVar("angemeldetBleiben").equals("true")) {
                StartBildschirmAO.startDialogText.setText("Benutzeranmeldung wird \u00fcbernommen...");
                logging.logInfo((Object)"Angemeldet Bleiben ist Aktiv");
                TabelleUser tabUser = new TabelleUser();
                try {
                    if (runApplication.clientID.equals(userProperties.getVar("clientID")) && hash.decodeHashCode((String)((String)userProperties.getVar("Passwort"))).equals(hash.decodeHashCode((String)tabUser.getPasswort((String)userProperties.getVar("Name"))))) {
                        anmeldeName.setText("Angemeldet als: " + userProperties.getVar("Name"));
                        runApplication.loginName = (String)userProperties.getVar("Name");
                        BerechtigunsManager.ber = new TabelleBerechtigunggruppe().getAll(tabUser.getRechte((String)userProperties.getVar("Name")), 1);
                        BerechtigunsManager.ber2 = new TabelleBerechtigunggruppe().getAll(tabUser.getRechte((String)userProperties.getVar("Name")), 2);
                        BerechtigunsManager.berechtigungDokumente();
                        BerechtigunsManager.berechtigungListen();
                        BerechtigunsManager.berechtigungMitglieder();
                        BerechtigunsManager.berechtigungOptionen();
                        BerechtigunsManager.berechtigungStatistik();
                        BerechtigunsManager.berechtigungVeranstaltung();
                        buttonAnmelden.setVisible(false);
                        buttonAbmelden.setVisible(true);
                        buttonPasswort\u00c4ndern.setVisible(true);
                        logbuchEingabe.NeuerEintag("Benutzer hat sich automatisch angemeldet");
                    } else {
                        JOptionPane.showMessageDialog(null, Konstante.BENUTZER_KEINE_ANMELDUNG, "Warnung", 2);
                        userProperties.sourceFileDelete();
                    }
                }
                catch (NumberFormatException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        }
        StartBildschirmAO.startDialog.setVisible(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        Thread threadAufgaben = new Thread(){

            @Override
            public void run() {
                logging.logInfo((Object)"pr\u00fcfe Informationen/Aufgaben");
                while (true) {
                    if (runApplication.infoServiceL\u00e4uft == 0) {
                        aufgabenListe.setText(null);
                        aufgabenListe.setText("Bitte warten...");
                        aufgabenListe.setText(InformationService.checkInformationen());
                    }
                    try {
                        Thread.sleep(300000L);
                    }
                    catch (InterruptedException interruptedException) {
                    }
                }
            }
        };
        threadAufgaben.start();
        if (runApplication.EINSTELLUNGEN.get("autoBerichtAktiv").equals("1")) {
            Thread threadAutoBericht = new Thread(){

                @Override
                public void run() {
                    logging.logInfo((Object)"AutoBericht ist Aktiv");
                    AutoBerichtService.AutoBericht();
                }
            };
            threadAutoBericht.start();
        }
        if (runApplication.EINSTELLUNGEN.get("autoDBsave").equals("1")) {
            Thread threadAutoDBSave = new Thread(){

                @Override
                public void run() {
                    logging.logInfo((Object)"AutoDBSave ist Aktiv");
                    DatensicherungService.DBSave(String.valueOf(runApplication.arbeitsverzeichnis) + "data/DBBACKUP/databasebackup_" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + ".sql");
                }
            };
            threadAutoDBSave.start();
        }
        Thread threadUhr = new Thread(){

            @Override
            public void run() {
                while (true) {
                    try {
                        while (true) {
                            if (runApplication.unwetterwarnungStatus == 1) {
                                uhr.setText("                                                                                                                                                                                                                                                                   " + SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy  HH:mm:ss"));
                            } else {
                                uhr.setText("                                                                                                                                                                                                                                                                                                                                                          " + SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy  HH:mm:ss"));
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
        threadUhr.start();
        Thread threadUnwetterwarnung = new Thread(){

            @Override
            public void run() {
                logging.logInfo((Object)"pr\u00fcfe Unwetterwarnung");
                try {
                    TabelleEMail_unwetterwarnung tabUnwtterwarnung = new TabelleEMail_unwetterwarnung();
                    runApplication.lastUnwetterwarnungID = tabUnwtterwarnung.getCount();
                    if (!runApplication.EINSTELLUNGEN_GESPEICHERT.get("unwetterwarnungDatumBis").equals("null")) {
                        logging.logInfo((Object)"Gespeicherte Unwetterwarnung gefunden, setzte Daten...");
                        runApplication.unwetterwarnungDatumBis = runApplication.EINSTELLUNGEN_GESPEICHERT.get("unwetterwarnungDatumBis");
                        runApplication.unwetterwarnungUhrzeitBis = runApplication.EINSTELLUNGEN_GESPEICHERT.get("unwetterwarnungUhrzeitBis");
                        runApplication.unwetterwarnungStatus = 1;
                        TabelleEinstellungen_gespeichert tabGespeichert = new TabelleEinstellungen_gespeichert();
                        tabGespeichert.update("unwetterwarnungDatumBis", "null");
                        tabGespeichert.update("unwetterwarnungUhrzeitBis", "null");
                        buttonUnwetterwarnung.setVisible(true);
                        logging.logInfo((Object)"Gespeicherte Unwetterwarnung gefunden, Anzeige in der GUI erfolgreich!");
                    }
                    while (true) {
                        if (runApplication.unwetterwarnungDatumBis != null && runApplication.unwetterwarnungUhrzeitBis != null) {
                            if (runApplication.unwetterwarnungDatumBis.equals(SbcUtils.timeStamp((String)"dd.MM.yyyy")) && Integer.parseInt(runApplication.unwetterwarnungUhrzeitBis.substring(0, 2)) <= Integer.parseInt(SbcUtils.timeStamp((String)"HH")) && Integer.parseInt(runApplication.unwetterwarnungUhrzeitBis.substring(3, 5)) <= Integer.parseInt(SbcUtils.timeStamp((String)"mm"))) {
                                logging.logInfo((Object)"Aktuelle Unwetterwarnung ist nicht mehr aktiv --> Entferne GUIInfo!");
                                buttonUnwetterwarnung.setVisible(false);
                                buttonUnwetterwarnung.setToolTipText(null);
                                runApplication.unwetterwarnungDatumBis = null;
                                runApplication.unwetterwarnungUhrzeitBis = null;
                                runApplication.unwetterwarnungStatus = 0;
                                SystemTrayInfo trayInfo = new SystemTrayInfo();
                                trayInfo.removeInfoIcon();
                                logging.logInfo((Object)"Unwetterwarnung ist entfernt...");
                            } else {
                                logging.logInfo((Object)("Aktuelle Unwetterwarnung ist noch aktiv! --> Aktiv bis: " + runApplication.unwetterwarnungDatumBis + " / " + runApplication.unwetterwarnungUhrzeitBis));
                            }
                        }
                        EmpfangenOpperationUnwetterwarnung.empfangen();
                        if (runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung").equals("0")) {
                            logging.logInfo((Object)"N\u00e4chste pr\u00fcfung der Unwetterwarnung in 300000ms / 5min.");
                            Thread.sleep(300000L);
                            continue;
                        }
                        logging.logInfo((Object)"N\u00e4chste pr\u00fcfung der Unwetterwarnung in 900000ms / 15min. (Getaktete Verbindung aktiv!)");
                        Thread.sleep(900000L);
                    }
                }
                catch (InterruptedException | SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                    return;
                }
            }
        };
        if (runApplication.EINSTELLUNGEN.get("unwetterwarnungModulAktiv").equals("1")) {
            threadUnwetterwarnung.start();
        }
        Thread threadEinsatSchnittstelle = new Thread(){

            @Override
            public void run() {
                StatusCheck.StatusCheckEinsatz();
            }
        };
        try {
            if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("einsatzSchnittstelle")) == 1) {
                threadEinsatSchnittstelle.start();
            }
        }
        catch (NumberFormatException e) {
            logging.logInfo((Object)e);
        }
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

