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
package ao.mitglieder;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_History;
import data.tabellen.mitglied.TabelleMitglieder_anrede;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import go.Mitglieder;
import go.Mitglieder_Untersuchung;
import go.Mitgliederlaufbahn;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.mitgliedakte.PDFMitgliedAusserDienst;
import pdfdocumente.mitgliedakte.PDFMitgliedInDienst;
import pdfdocumente.mitgliedakte.PDFMitgliedInfo;
import pdfdocumente.mitgliedakte.PDFMitgliedLoeschkenner;
import run.runApplication;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.VCard;
import utilities.logbuchEingabe;

public class MitgliederAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static String mitgliedName = null;
    public static String mitgliedID = null;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAktualisieren;
    private JButton buttonArbeitgeber;
    private JButton buttonAngehoerige;
    private JButton buttonUntersuchung;
    private JButton buttonNeueMitgliederGruppe;
    private JButton buttonBankverbindung;
    private JButton buttonAnredeAnlegen;
    private JButton buttonNeu;
    private JButton buttonAusserDienst;
    private JButton vCard;
    private JButton buttonDienstgradAnlegen;
    private JButton buttonLoeschkenner;
    private JButton buttonAbbruch;
    private JButton buttonSuche;
    private JButton buttonHistory;
    private JButton buttonLehrgangEintragen;
    private JButton buttonF\u00fchrerscheinEintragen;
    private JButton buttonFunktionEintragen;
    private JButton buttonEhrungEintragen;
    private JTextField name;
    private JTextField vorname;
    private JTextField strasse;
    private JTextField ort;
    private JTextField telPrivat;
    private JTextField telMobil;
    private JTextField telArbeit;
    private JTextField email;
    private JTextField email2;
    private JTextField personalnummer;
    private JTextField gebDatum;
    private JTextField mitgliedSeit;
    private JTextField austritt;
    private JTextArea kommentar;
    private JScrollPane kommentarPane;
    private JTextField fuehrerscheinNummer;
    private JTextField fahrberechtigungNummer;
    private JTextField dienstausweisNummer;
    private JTextField hochzeitstag;
    private JCheckBox ausserDienst;
    private JCheckBox eMailVerteilungDeaktivieren;
    private JTextField ablaufdatumDienstausweis;
    private JTextField pr\u00fcfungDerFahrberechtigung;
    private JTextField beruf;
    private JTextField telegrammID;
    private JLabel name_label;
    private JLabel vorname_label;
    private JLabel strasse_label;
    private JLabel ort_label;
    private JLabel telPrivat_label;
    private JLabel telMobil_label;
    private JLabel telArbeit_label;
    private JLabel email_label;
    private JLabel email2_label;
    private JLabel dienstgrad_label;
    private JLabel personalnummer_label;
    private JLabel gruppe_label;
    private JLabel anrede_label;
    private JLabel gebDatum_label;
    private JLabel mitgliedSeit_lebel;
    private JLabel austritt_label;
    private JLabel ausserDienst_label;
    private JLabel kommentar_label;
    private JLabel fuehrerscheinNummer_label;
    private JLabel fahrberechtigungNummer_label;
    private JLabel dienstausweisNummer_label;
    private JLabel hochzeitstag_label;
    private JLabel eMailVerteilungDeaktivieren_label;
    private JLabel ablaufdatumDienstausweis_label;
    private JLabel pr\u00fcfungDerFahrberechtigung_label;
    private JLabel beruf_label;
    private JLabel telegrammID_label;
    private JLabel \u00fcberblickTageBisZumGeburtstag;
    private JLabel \u00fcberblickTageBisG25;
    private JLabel \u00fcberblickTageBisG26;
    private JLabel \u00fcberblickTageBisG30;
    private JLabel \u00fcberblickTageBisG41;
    private JLabel \u00fcberblickTageBisG42;
    private JLabel \u00fcberblickTageBisAblaufLKW;
    private JLabel \u00fcberblickTageBisAblaufDienstausweis;
    private JLabel \u00fcberblickHochzeitstag;
    private JLabel \u00fcberblickPr\u00fcfungDerFahrberechtigung;
    private HashMap<String, String> aktuelleMitgliederDaten;
    private HashMap<String, String> aktuelleUntersuchngsdaten;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    public static JComboBox<String> dienstgrad;
    public static JComboBox<String> gruppe;
    public static JComboBox<String> anrede;
    private JTable tableLehrg\u00e4nge;
    private JTable tableDienstgrad;
    private JTable tableF\u00fchrerschein;
    private JTable tableFunktion;
    private JTable tableEhrungen;
    private DefaultTableModel defaultTableModelLehrg\u00e4nge;
    private DefaultTableModel defaultTableModelDienstgrad;
    private DefaultTableModel defaultTableModelF\u00fchrerschein;
    private DefaultTableModel defaultTableModelFunktion;
    private DefaultTableModel defaultTableModelEhrungen;
    private JScrollPane scrollpaneLehrg\u00e4nge;
    private JScrollPane scrollpaneDienstgrad;
    private JScrollPane scrollpaneF\u00fchrerschein;
    private JScrollPane scrollpaneFunktion;
    private JScrollPane scrollpaneEhrungen;
    private JCheckBox[] jCheckboxArrayLehrgang;
    private JCheckBox[] jCheckboxArrayF\u00fchrerschein;
    private JCheckBox[] jCheckboxArrayFunktion;
    private JCheckBox[] jCheckboxArrayFunktionAu\u00dferhalb;
    private JCheckBox[] jCheckboxArrayEhrungen;
    private JCheckBox[] jCheckboxArrayAbzeichen;
    private JPanel panelLehrg\u00e4nge;
    private JPanel panelDienstgrad;
    private JPanel panelF\u00fchrerschein;
    private JPanel panelFunktion;
    private JPanel panelEhrungen;
    private JPanel panelMitglieder;
    private JPanel panelSonstiges;
    private JPanel panel\u00dcberblick;
    private JTabbedPane tabPane;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;
    private JLabel dummy\u00dcberblick1;
    private JLabel dummy\u00dcberblick2;

    public MitgliederAnlegenAO() {
        super("FeuerwehrManagementSystem - Mitgliederverwaltung");
        logging.logInfo((Object)"Starte: MitgliederAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonNeu = new JButton("Neu");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonArbeitgeber = new JButton("Arbeitgeber");
        this.buttonAngehoerige = new JButton("Angeh\u00f6rige");
        this.buttonUntersuchung = new JButton("Untersuchung");
        this.buttonNeueMitgliederGruppe = new JButton("Neue Mitgliedergruppe");
        this.buttonBankverbindung = new JButton("Bankverbindung");
        this.buttonAnredeAnlegen = new JButton("Neue Anrede");
        this.buttonAusserDienst = new JButton("Au\u00dfer Dienst");
        this.vCard = new JButton("vCard Export");
        this.buttonDienstgradAnlegen = new JButton("Dienstgrad anlegen");
        this.buttonLoeschkenner = new JButton("L\u00f6schen");
        this.buttonAbbruch = new JButton("Abbruch");
        this.buttonSuche = new JButton("Suche / Filter");
        this.buttonHistory = new JButton("Historie");
        this.buttonLehrgangEintragen = new JButton("Lehrgang Eintragen");
        this.buttonF\u00fchrerscheinEintragen = new JButton("F\u00fchrerschein eintragen");
        this.buttonFunktionEintragen = new JButton("Funktion Eintragen");
        this.buttonEhrungEintragen = new JButton("Ehrung/Abzeichen Eintragen");
        this.name = new JTextField(25);
        this.vorname = new JTextField(25);
        this.strasse = new JTextField(25);
        this.ort = new JTextField(25);
        this.telPrivat = new JTextField(25);
        this.telMobil = new JTextField(25);
        this.telArbeit = new JTextField(25);
        this.email = new JTextField(25);
        this.email2 = new JTextField(25);
        this.personalnummer = new JTextField(25);
        this.gebDatum = new JTextField(25);
        this.mitgliedSeit = new JTextField(SbcUtils.timeStamp((String)runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat")), 25);
        this.austritt = new JTextField(25);
        this.kommentar = new JTextArea(5, 25);
        this.kommentarPane = new JScrollPane(this.kommentar);
        this.kommentarPane.setVerticalScrollBarPolicy(22);
        this.kommentar.setLineWrap(true);
        this.kommentar.setWrapStyleWord(true);
        this.fuehrerscheinNummer = new JTextField(25);
        this.fahrberechtigungNummer = new JTextField(25);
        this.dienstausweisNummer = new JTextField(25);
        this.hochzeitstag = new JTextField(25);
        this.ausserDienst = new JCheckBox();
        this.eMailVerteilungDeaktivieren = new JCheckBox();
        this.eMailVerteilungDeaktivieren.setToolTipText("Dieses Mitglied wird mit der Funktion aus dem E-Mail Verteiler entfernt und erh\u00e4lt keine E-Mails mehr!");
        this.ablaufdatumDienstausweis = new JTextField(25);
        this.pr\u00fcfungDerFahrberechtigung = new JTextField(25);
        this.beruf = new JTextField(25);
        this.telegrammID = new JTextField(25);
        this.name_label = new JLabel("Nachname: ");
        this.vorname_label = new JLabel("Vorname: ");
        this.strasse_label = new JLabel("Stra\u00dfe: ");
        this.ort_label = new JLabel("PLZ und Ort: ");
        this.telPrivat_label = new JLabel("Telefon privat: ");
        this.telMobil_label = new JLabel("Telefon Mobil: ");
        this.telArbeit_label = new JLabel("Telefon Arbeit: ");
        this.email_label = new JLabel("E-Mail: ");
        this.email2_label = new JLabel("E-Mail2: ");
        this.dienstgrad_label = new JLabel("Dienstgrad");
        this.personalnummer_label = new JLabel("Personalnummer");
        this.gruppe_label = new JLabel("Mitglieder Gruppe: ");
        this.anrede_label = new JLabel("Anrede: ");
        this.gebDatum_label = new JLabel("Geburtsdatum");
        this.mitgliedSeit_lebel = new JLabel("Mitglied Seit (Format: yyyy):");
        this.austritt_label = new JLabel("Austritt am (Format: dd.MM.yyyy): ");
        this.ausserDienst_label = new JLabel("Au\u00dfer Dienst");
        this.kommentar_label = new JLabel("Sonstiges / Bemerkung / Kommentar: ");
        this.fuehrerscheinNummer_label = new JLabel("F\u00fchrerscheinnummer: ");
        this.fahrberechtigungNummer_label = new JLabel("Fahrberechtigungnummer: ");
        this.dienstausweisNummer_label = new JLabel("Dienstausweisnummer: ");
        this.eMailVerteilungDeaktivieren_label = new JLabel("E-Mail Verteilung deaktivieren: ");
        this.hochzeitstag_label = new JLabel("Hochzeitstag: ");
        this.ablaufdatumDienstausweis_label = new JLabel("Ablaufdatum Dienstausweis: ");
        this.pr\u00fcfungDerFahrberechtigung_label = new JLabel("Ablauf der Fahrberechtigung am: ");
        this.beruf_label = new JLabel("Berufliche T\u00e4tigkeit: ");
        this.telegrammID_label = new JLabel("Telegramm Messanger ID: ");
        this.\u00fcberblickTageBisZumGeburtstag = new JLabel();
        this.\u00fcberblickTageBisG25 = new JLabel();
        this.\u00fcberblickTageBisG26 = new JLabel();
        this.\u00fcberblickTageBisG30 = new JLabel();
        this.\u00fcberblickTageBisG41 = new JLabel();
        this.\u00fcberblickTageBisG42 = new JLabel();
        this.\u00fcberblickTageBisAblaufDienstausweis = new JLabel();
        this.\u00fcberblickPr\u00fcfungDerFahrberechtigung = new JLabel();
        this.\u00fcberblickHochzeitstag = new JLabel();
        this.\u00fcberblickTageBisAblaufLKW = new JLabel();
        tree = new JTree(CreateTrees.CreateTreeMitgliederListe(null));
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
        this.tabPane = new JTabbedPane();
        this.modulBeschreibung = new JLabel("Mitgliederverwaltung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.dummy\u00dcberblick1 = new JLabel(runApplication.dummyImage);
        this.dummy\u00dcberblick2 = new JLabel(runApplication.dummyImage);
        TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
        TabelleMitglieder_gruppe tabGrppe = new TabelleMitglieder_gruppe();
        TabelleMitglieder_anrede tabAnrede = new TabelleMitglieder_anrede();
        try {
            String[] dienstgradListe = Utils.listToArrayOnlyFORComboBoxes(tabDienstgrad.getAllDienstgradLang());
            String[] mitgliederGruppe = Utils.listToArrayOnlyFORComboBoxes(tabGrppe.getAllGruppen());
            String[] anredeListe = Utils.listToArrayOnlyFORComboBoxes(tabAnrede.getAnrede());
            dienstgrad = new JComboBox<String>(dienstgradListe);
            gruppe = new JComboBox<String>(mitgliederGruppe);
            anrede = new JComboBox<String>(anredeListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
            logging.logInfo((Object)"Setzte Aufl\u00f6sung f\u00fcr Mitgiederverwaltung: 1280x1024");
            this.setSize(1280, 750);
        } else {
            logging.logInfo((Object)"Setzte Aufl\u00f6sung f\u00fcr Mitgiederverwaltung: 16:9");
            this.setSize(1366, 750);
        }
        this.setTitle("FeuerwehrManagementSystem - Mitgliederverwaltung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonNeu);
        this.add(this.buttonSuche);
        this.add(this.buttonAusserDienst);
        this.add(this.buttonLoeschkenner);
        this.add(this.buttonNeueMitgliederGruppe);
        this.add(this.buttonAnredeAnlegen);
        this.add(this.buttonDienstgradAnlegen);
        this.add(this.vCard);
        this.add(this.dummy3);
        if (runApplication.bildschirmgr\u00f6\u00dfe.getWidth() == 1280.0 && runApplication.bildschirmgr\u00f6\u00dfe.getHeight() == 1024.0) {
            this.scrollPaneTree.setPreferredSize(new Dimension(300, 550));
        } else {
            this.scrollPaneTree.setPreferredSize(new Dimension(330, 550));
        }
        this.add(this.scrollPaneTree);
        this.panelMitglieder = new JPanel(new GridLayout(19, 2));
        this.getContentPane().add("Center", this.panelMitglieder);
        this.panelMitglieder.add(this.gruppe_label);
        this.panelMitglieder.add(gruppe);
        this.panelMitglieder.add(this.personalnummer_label);
        this.panelMitglieder.add(this.personalnummer);
        this.panelMitglieder.add(this.anrede_label);
        this.panelMitglieder.add(anrede);
        this.panelMitglieder.add(this.name_label);
        this.panelMitglieder.add(this.name);
        this.panelMitglieder.add(this.vorname_label);
        this.panelMitglieder.add(this.vorname);
        this.panelMitglieder.add(this.strasse_label);
        this.panelMitglieder.add(this.strasse);
        this.panelMitglieder.add(this.ort_label);
        this.panelMitglieder.add(this.ort);
        this.panelMitglieder.add(this.telPrivat_label);
        this.panelMitglieder.add(this.telPrivat);
        this.panelMitglieder.add(this.telMobil_label);
        this.panelMitglieder.add(this.telMobil);
        this.panelMitglieder.add(this.telArbeit_label);
        this.panelMitglieder.add(this.telArbeit);
        this.panelMitglieder.add(this.email_label);
        this.panelMitglieder.add(this.email);
        this.panelMitglieder.add(this.email2_label);
        this.panelMitglieder.add(this.email2);
        this.panelMitglieder.add(this.gebDatum_label);
        this.panelMitglieder.add(this.gebDatum);
        this.panelMitglieder.add(this.mitgliedSeit_lebel);
        this.panelMitglieder.add(this.mitgliedSeit);
        if (runApplication.EINSTELLUNGEN.get("hochzeitFeldFuerMitglieder").equals("1")) {
            this.panelMitglieder.add(this.hochzeitstag_label);
            this.panelMitglieder.add(this.hochzeitstag);
        }
        this.panelMitglieder.add(this.dienstgrad_label);
        this.panelMitglieder.add(dienstgrad);
        this.panelMitglieder.add(this.ausserDienst_label);
        this.panelMitglieder.add(this.ausserDienst);
        this.panelMitglieder.add(this.eMailVerteilungDeaktivieren_label);
        this.panelMitglieder.add(this.eMailVerteilungDeaktivieren);
        this.panelMitglieder.add(this.kommentar_label);
        this.panelMitglieder.add(this.kommentarPane);
        this.panelSonstiges = new JPanel(new GridLayout(19, 2));
        this.getContentPane().add("Center", this.panelSonstiges);
        this.panelSonstiges.add(this.beruf_label);
        this.panelSonstiges.add(this.beruf);
        if (runApplication.EINSTELLUNGEN.get("modulMitgliederVerf\u00fcgbarkeit").equals("1")) {
            this.panelSonstiges.add(this.telegrammID_label);
            this.panelSonstiges.add(this.telegrammID);
        }
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(this.ablaufdatumDienstausweis_label);
        this.panelSonstiges.add(this.ablaufdatumDienstausweis);
        this.panelSonstiges.add(this.dienstausweisNummer_label);
        this.panelSonstiges.add(this.dienstausweisNummer);
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(this.fuehrerscheinNummer_label);
        this.panelSonstiges.add(this.fuehrerscheinNummer);
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        if (runApplication.EINSTELLUNGEN.get("pr\u00fcfungDerFahrerlaubnis").equals("1")) {
            this.panelSonstiges.add(this.pr\u00fcfungDerFahrberechtigung_label);
            this.panelSonstiges.add(this.pr\u00fcfungDerFahrberechtigung);
            this.panelSonstiges.add(this.fahrberechtigungNummer_label);
            this.panelSonstiges.add(this.fahrberechtigungNummer);
        }
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(this.austritt_label);
        this.panelSonstiges.add(this.austritt);
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panelSonstiges.add(new JLabel(""));
        this.panel\u00dcberblick = new JPanel(new GridLayout(19, 2));
        this.getContentPane().add("Center", this.panel\u00dcberblick);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisZumGeburtstag);
        this.panel\u00dcberblick.add(this.dummy\u00dcberblick1);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisG25);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisG26);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisG30);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisG41);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisG42);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisAblaufLKW);
        this.panel\u00dcberblick.add(this.dummy\u00dcberblick2);
        this.panel\u00dcberblick.add(this.\u00fcberblickTageBisAblaufDienstausweis);
        if (runApplication.EINSTELLUNGEN.get("pr\u00fcfungDerFahrerlaubnis").equals("1")) {
            this.panel\u00dcberblick.add(this.\u00fcberblickPr\u00fcfungDerFahrberechtigung);
        }
        if (runApplication.EINSTELLUNGEN.get("hochzeitFeldFuerMitglieder").equals("1")) {
            this.panel\u00dcberblick.add(this.\u00fcberblickHochzeitstag);
        }
        try {
            TabelleLehrgang_kategorie tabLehrgang_kat = new TabelleLehrgang_kategorie();
            if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("Liste")) {
                this.panelLehrg\u00e4nge = new JPanel();
                this.panelF\u00fchrerschein = new JPanel();
                this.panelFunktion = new JPanel();
                this.panelEhrungen = new JPanel();
                this.panelDienstgrad = new JPanel();
                this.defaultTableModelLehrg\u00e4nge = new DefaultTableModel(10, 9);
                this.defaultTableModelLehrg\u00e4nge.setColumnIdentifiers(TabelleMitglieder_laufbahn.headnameLehrgang);
                this.tableLehrg\u00e4nge = new JTable(this.defaultTableModelLehrg\u00e4nge);
                this.tableLehrg\u00e4nge.setPreferredScrollableViewportSize(new Dimension(670, 470));
                this.tableLehrg\u00e4nge.setFillsViewportHeight(true);
                this.tableLehrg\u00e4nge.setEnabled(false);
                this.tableLehrg\u00e4nge.setRowHeight(30);
                this.scrollpaneLehrg\u00e4nge = new JScrollPane(this.tableLehrg\u00e4nge);
                this.scrollpaneLehrg\u00e4nge.setVerticalScrollBarPolicy(22);
                this.defaultTableModelF\u00fchrerschein = new DefaultTableModel(10, 9);
                this.defaultTableModelF\u00fchrerschein.setColumnIdentifiers(TabelleMitglieder_laufbahn.headnameF\u00fchrerschein);
                this.tableF\u00fchrerschein = new JTable(this.defaultTableModelF\u00fchrerschein);
                this.tableF\u00fchrerschein.setPreferredScrollableViewportSize(new Dimension(670, 470));
                this.tableF\u00fchrerschein.setFillsViewportHeight(true);
                this.tableF\u00fchrerschein.setEnabled(false);
                this.tableF\u00fchrerschein.setRowHeight(30);
                this.scrollpaneF\u00fchrerschein = new JScrollPane(this.tableF\u00fchrerschein);
                this.scrollpaneF\u00fchrerschein.setVerticalScrollBarPolicy(22);
                this.defaultTableModelFunktion = new DefaultTableModel(10, 9);
                this.defaultTableModelFunktion.setColumnIdentifiers(TabelleMitglieder_laufbahn.headnameFunktion);
                this.tableFunktion = new JTable(this.defaultTableModelFunktion);
                this.tableFunktion.setPreferredScrollableViewportSize(new Dimension(670, 470));
                this.tableFunktion.setFillsViewportHeight(true);
                this.tableFunktion.setEnabled(false);
                this.tableFunktion.setRowHeight(30);
                this.scrollpaneFunktion = new JScrollPane(this.tableFunktion);
                this.scrollpaneFunktion.setVerticalScrollBarPolicy(22);
                this.defaultTableModelEhrungen = new DefaultTableModel(10, 9);
                this.defaultTableModelEhrungen.setColumnIdentifiers(TabelleMitglieder_laufbahn.headnameEhrung);
                this.tableEhrungen = new JTable(this.defaultTableModelEhrungen);
                this.tableEhrungen.setPreferredScrollableViewportSize(new Dimension(670, 470));
                this.tableEhrungen.setFillsViewportHeight(true);
                this.tableEhrungen.setEnabled(false);
                this.tableEhrungen.setRowHeight(30);
                this.scrollpaneEhrungen = new JScrollPane(this.tableEhrungen);
                this.scrollpaneEhrungen.setVerticalScrollBarPolicy(22);
                this.defaultTableModelDienstgrad = new DefaultTableModel(10, 9);
                this.defaultTableModelDienstgrad.setColumnIdentifiers(TabelleMitglieder_laufbahn.headnameDienstgrad);
                this.tableDienstgrad = new JTable(this.defaultTableModelDienstgrad);
                this.tableDienstgrad.setPreferredScrollableViewportSize(new Dimension(670, 470));
                this.tableDienstgrad.setFillsViewportHeight(true);
                this.tableDienstgrad.setEnabled(false);
                this.tableDienstgrad.setRowHeight(30);
                this.scrollpaneDienstgrad = new JScrollPane(this.tableDienstgrad);
                this.scrollpaneDienstgrad.setVerticalScrollBarPolicy(22);
                this.panelLehrg\u00e4nge.add(this.scrollpaneLehrg\u00e4nge);
                this.panelLehrg\u00e4nge.add(this.buttonLehrgangEintragen);
                this.panelF\u00fchrerschein.add(this.scrollpaneF\u00fchrerschein);
                this.panelF\u00fchrerschein.add(this.buttonF\u00fchrerscheinEintragen);
                this.panelFunktion.add(this.scrollpaneFunktion);
                this.panelFunktion.add(this.buttonFunktionEintragen);
                this.panelEhrungen.add(this.scrollpaneEhrungen);
                this.panelEhrungen.add(this.buttonEhrungEintragen);
                this.panelDienstgrad.add(this.scrollpaneDienstgrad);
            } else if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
                this.panelLehrg\u00e4nge = new JPanel(new GridLayout(18, 2));
                this.panelF\u00fchrerschein = new JPanel(new GridLayout(18, 2));
                this.panelFunktion = new JPanel(new GridLayout(18, 2));
                this.panelEhrungen = new JPanel(new GridLayout(18, 2));
                String[] lehrgangListe = Utils.listToArray(tabLehrgang_kat.getAlleLehrg\u00e4ngeSeminare());
                int[] lehrgangListeID = Utils.listToIntArray(tabLehrgang_kat.getAlleLehrg\u00e4ngeSeminareID());
                String[] f\u00fchrerscheinListe = Utils.listToArray(tabLehrgang_kat.getAlleF\u00fchrerschein());
                int[] f\u00fchrerscheinListeID = Utils.listToIntArray(tabLehrgang_kat.getAlleF\u00fchrerscheinID());
                String[] funktionListe = Utils.listToArray(tabLehrgang_kat.getAlleFunktionen());
                int[] funktionListeID = Utils.listToIntArray(tabLehrgang_kat.getAlleFunktionenID());
                String[] funktionAu\u00dferhalbListe = Utils.listToArray(tabLehrgang_kat.getAlleFunktionenAu\u00dferhalb());
                int[] funktionAu\u00dferhalbListeID = Utils.listToIntArray(tabLehrgang_kat.getAlleFunktionenAu\u00dferhalbID());
                String[] ehrungenListe = Utils.listToArray(tabLehrgang_kat.getAlleEhrungen());
                int[] ehrungenListeID = Utils.listToIntArray(tabLehrgang_kat.getAlleEhrungenIDs());
                String[] abzeichenListe = Utils.listToArray(tabLehrgang_kat.getAlleAbzeichen());
                int[] abzeichenListeID = Utils.listToIntArray(tabLehrgang_kat.getAlleAbzeichenIDs());
                this.jCheckboxArrayLehrgang = new JCheckBox[lehrgangListe.length];
                this.jCheckboxArrayF\u00fchrerschein = new JCheckBox[f\u00fchrerscheinListe.length];
                this.jCheckboxArrayFunktion = new JCheckBox[funktionListe.length];
                this.jCheckboxArrayFunktionAu\u00dferhalb = new JCheckBox[funktionAu\u00dferhalbListe.length];
                this.jCheckboxArrayEhrungen = new JCheckBox[ehrungenListe.length];
                this.jCheckboxArrayAbzeichen = new JCheckBox[abzeichenListe.length];
                int x = 0;
                while (x < lehrgangListe.length) {
                    if (x == 0) {
                        JLabel lehrg\u00e4nge_label = new JLabel("Lehrg\u00e4nge:");
                        lehrg\u00e4nge_label.setFont(new Font("Arial", 1, 12));
                        this.panelLehrg\u00e4nge.add(lehrg\u00e4nge_label);
                        this.panelLehrg\u00e4nge.add(this.buttonLehrgangEintragen);
                    }
                    this.jCheckboxArrayLehrgang[x] = new JCheckBox();
                    this.jCheckboxArrayLehrgang[x].setText(lehrgangListe[x]);
                    this.jCheckboxArrayLehrgang[x].setName(Integer.toString(lehrgangListeID[x]));
                    this.panelLehrg\u00e4nge.add(this.jCheckboxArrayLehrgang[x]);
                    logging.logInfo((Object)("F\u00fcge Check Box: " + lehrgangListe[x] + " hinzu...."));
                    ++x;
                }
                int f\u00fc = 0;
                while (f\u00fc < f\u00fchrerscheinListe.length) {
                    if (f\u00fc == 0) {
                        this.panelF\u00fchrerschein.add(this.buttonF\u00fchrerscheinEintragen);
                        JLabel f\u00fchrerschein_label = new JLabel("F\u00fchrerscheine:");
                        f\u00fchrerschein_label.setFont(new Font("Arial", 1, 12));
                        this.panelF\u00fchrerschein.add(f\u00fchrerschein_label);
                    }
                    this.jCheckboxArrayF\u00fchrerschein[f\u00fc] = new JCheckBox();
                    this.jCheckboxArrayF\u00fchrerschein[f\u00fc].setText(f\u00fchrerscheinListe[f\u00fc]);
                    this.jCheckboxArrayF\u00fchrerschein[f\u00fc].setName(Integer.toString(f\u00fchrerscheinListeID[f\u00fc]));
                    this.panelF\u00fchrerschein.add(this.jCheckboxArrayF\u00fchrerschein[f\u00fc]);
                    logging.logInfo((Object)("F\u00fcge Check Box: " + f\u00fchrerscheinListe[f\u00fc] + " hinzu...."));
                    ++f\u00fc;
                }
                int f = 0;
                while (f < funktionListe.length) {
                    if (f == 0) {
                        this.panelFunktion.add(this.buttonFunktionEintragen);
                        JLabel funktionInDerFeuerwehr_label = new JLabel("Funktionen in der Feuerweher:");
                        funktionInDerFeuerwehr_label.setFont(new Font("Arial", 1, 12));
                        this.panelFunktion.add(funktionInDerFeuerwehr_label);
                    }
                    this.jCheckboxArrayFunktion[f] = new JCheckBox();
                    this.jCheckboxArrayFunktion[f].setText(funktionListe[f]);
                    this.jCheckboxArrayFunktion[f].setName(Integer.toString(funktionListeID[f]));
                    this.panelFunktion.add(this.jCheckboxArrayFunktion[f]);
                    logging.logInfo((Object)("F\u00fcge Check Box: " + funktionListe[f] + " hinzu...."));
                    ++f;
                }
                int f_au\u00dferhalb = 0;
                while (f_au\u00dferhalb < funktionAu\u00dferhalbListe.length) {
                    if (f_au\u00dferhalb == 0) {
                        JLabel funktionAu\u00dferhalbDerFeuerwehr_label = new JLabel("Funktionen au\u00dferhalb der Feuerweher:");
                        funktionAu\u00dferhalbDerFeuerwehr_label.setFont(new Font("Arial", 1, 12));
                        this.panelFunktion.add(funktionAu\u00dferhalbDerFeuerwehr_label);
                    }
                    this.jCheckboxArrayFunktionAu\u00dferhalb[f_au\u00dferhalb] = new JCheckBox();
                    this.jCheckboxArrayFunktionAu\u00dferhalb[f_au\u00dferhalb].setText(funktionAu\u00dferhalbListe[f_au\u00dferhalb]);
                    this.jCheckboxArrayFunktionAu\u00dferhalb[f_au\u00dferhalb].setName(Integer.toString(funktionAu\u00dferhalbListeID[f_au\u00dferhalb]));
                    this.panelFunktion.add(this.jCheckboxArrayFunktionAu\u00dferhalb[f_au\u00dferhalb]);
                    logging.logInfo((Object)("F\u00fcge Check Box: " + funktionAu\u00dferhalbListe[f_au\u00dferhalb] + " hinzu...."));
                    ++f_au\u00dferhalb;
                }
                int e = 0;
                while (e < ehrungenListe.length) {
                    if (e == 0) {
                        this.panelEhrungen.add(this.buttonEhrungEintragen);
                        JLabel ehrungen_label = new JLabel("Ehrungen:");
                        ehrungen_label.setFont(new Font("Arial", 1, 12));
                        this.panelEhrungen.add(ehrungen_label);
                    }
                    this.jCheckboxArrayEhrungen[e] = new JCheckBox();
                    this.jCheckboxArrayEhrungen[e].setText(ehrungenListe[e]);
                    this.jCheckboxArrayEhrungen[e].setName(Integer.toString(ehrungenListeID[e]));
                    this.panelEhrungen.add(this.jCheckboxArrayEhrungen[e]);
                    logging.logInfo((Object)("F\u00fcge Check Box: " + ehrungenListe[e] + " hinzu...."));
                    ++e;
                }
                int a = 0;
                while (a < abzeichenListe.length) {
                    if (a == 0) {
                        JLabel abzeichen_label = new JLabel("Abzeichen:");
                        abzeichen_label.setFont(new Font("Arial", 1, 12));
                        this.panelEhrungen.add(abzeichen_label);
                    }
                    this.jCheckboxArrayAbzeichen[a] = new JCheckBox();
                    this.jCheckboxArrayAbzeichen[a].setText(abzeichenListe[a]);
                    this.jCheckboxArrayAbzeichen[a].setName(Integer.toString(abzeichenListeID[a]));
                    this.panelEhrungen.add(this.jCheckboxArrayAbzeichen[a]);
                    logging.logInfo((Object)("F\u00fcge Check Box: " + abzeichenListe[a] + " hinzu...."));
                    ++a;
                }
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.tabPane.addTab("Personendaten", this.panelMitglieder);
        this.tabPane.addTab("Lehrg\u00e4nge", new JScrollPane(this.panelLehrg\u00e4nge));
        this.tabPane.addTab("F\u00fchrerschein", this.panelF\u00fchrerschein);
        this.tabPane.addTab("Funktionen", this.panelFunktion);
        this.tabPane.addTab("Ehrungen / Abzeichen", this.panelEhrungen);
        if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("Liste")) {
            this.tabPane.addTab("Dienstgard", this.panelDienstgrad);
        }
        this.tabPane.addTab("Zusatzdaten", this.panelSonstiges);
        this.tabPane.addTab("\u00dcberblick", this.panel\u00dcberblick);
        this.tabPane.setPreferredSize(new Dimension(900, 550));
        this.add(this.tabPane);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonAbbruch);
        this.add(this.buttonAktualisieren);
        this.add(this.buttonArbeitgeber);
        this.add(this.buttonAngehoerige);
        this.add(this.buttonUntersuchung);
        this.add(this.buttonBankverbindung);
        this.add(this.buttonHistory);
        this.buttonSpeichern.setVisible(false);
        this.buttonAktualisieren.setVisible(true);
        this.buttonAktualisieren.setEnabled(true);
        this.ausserDienst.setEnabled(false);
        this.personalnummer.setEditable(false);
        this.buttonAbbruch.setVisible(false);
        if (BerechtigunsManager.ber[46] == 1) {
            this.buttonNeueMitgliederGruppe.setEnabled(true);
        } else {
            this.buttonNeueMitgliederGruppe.setEnabled(false);
        }
        if (BerechtigunsManager.ber[54] == 1) {
            this.buttonAnredeAnlegen.setEnabled(true);
        } else {
            this.buttonAnredeAnlegen.setEnabled(false);
        }
        if (BerechtigunsManager.ber[2] == 1) {
            this.buttonAusserDienst.setEnabled(true);
            this.buttonLoeschkenner.setEnabled(true);
        } else {
            this.buttonAusserDienst.setEnabled(false);
            this.buttonLoeschkenner.setEnabled(false);
        }
        if (BerechtigunsManager.ber[0] == 1) {
            this.buttonDienstgradAnlegen.setEnabled(true);
        } else {
            this.buttonDienstgradAnlegen.setEnabled(false);
        }
        this.sichtbarkeitSetzen(false);
    }

    private void sichtbarkeitSetzen(boolean wert) {
        this.name.setEnabled(wert);
        this.vorname.setEnabled(wert);
        this.strasse.setEnabled(wert);
        this.ort.setEnabled(wert);
        this.telPrivat.setEnabled(wert);
        this.telMobil.setEnabled(wert);
        this.telArbeit.setEnabled(wert);
        this.email.setEnabled(wert);
        this.email2.setEnabled(wert);
        this.personalnummer.setEnabled(wert);
        this.gebDatum.setEnabled(wert);
        this.kommentar.setEnabled(wert);
        this.fuehrerscheinNummer.setEnabled(wert);
        this.fahrberechtigungNummer.setEnabled(wert);
        this.dienstausweisNummer.setEnabled(wert);
        this.hochzeitstag.setEnabled(wert);
        this.mitgliedSeit.setEnabled(wert);
        this.austritt.setEnabled(wert);
        gruppe.setEnabled(wert);
        dienstgrad.setEnabled(wert);
        anrede.setEnabled(wert);
        this.eMailVerteilungDeaktivieren.setEnabled(wert);
        this.ablaufdatumDienstausweis.setEnabled(wert);
        this.pr\u00fcfungDerFahrberechtigung.setEnabled(wert);
        this.beruf.setEnabled(wert);
        this.telegrammID.setEnabled(wert);
        if (!wert) {
            this.buttonLoeschkenner.setEnabled(wert);
            this.buttonAusserDienst.setEnabled(wert);
        } else if (BerechtigunsManager.ber[2] == 1) {
            this.buttonAusserDienst.setEnabled(true);
            this.buttonLoeschkenner.setEnabled(true);
        } else {
            this.buttonAusserDienst.setEnabled(false);
            this.buttonLoeschkenner.setEnabled(false);
        }
        this.buttonArbeitgeber.setEnabled(wert);
        this.buttonAngehoerige.setEnabled(wert);
        this.buttonAktualisieren.setEnabled(wert);
        this.buttonUntersuchung.setEnabled(wert);
        this.buttonBankverbindung.setEnabled(wert);
        this.buttonHistory.setEnabled(wert);
        this.vCard.setEnabled(wert);
        this.buttonLehrgangEintragen.setEnabled(wert);
        this.buttonEhrungEintragen.setEnabled(wert);
        this.buttonFunktionEintragen.setEnabled(wert);
        this.buttonF\u00fchrerscheinEintragen.setEnabled(wert);
        if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
            int i = 0;
            while (i < this.jCheckboxArrayLehrgang.length) {
                this.jCheckboxArrayLehrgang[i].setEnabled(false);
                ++i;
            }
            int f\u00fc = 0;
            while (f\u00fc < this.jCheckboxArrayF\u00fchrerschein.length) {
                this.jCheckboxArrayF\u00fchrerschein[f\u00fc].setEnabled(false);
                ++f\u00fc;
            }
            int f = 0;
            while (f < this.jCheckboxArrayFunktion.length) {
                this.jCheckboxArrayFunktion[f].setEnabled(false);
                ++f;
            }
            int f_au\u00dferhalb = 0;
            while (f_au\u00dferhalb < this.jCheckboxArrayFunktionAu\u00dferhalb.length) {
                this.jCheckboxArrayFunktionAu\u00dferhalb[f_au\u00dferhalb].setEnabled(false);
                ++f_au\u00dferhalb;
            }
            int e = 0;
            while (e < this.jCheckboxArrayEhrungen.length) {
                this.jCheckboxArrayEhrungen[e].setEnabled(false);
                ++e;
            }
            int a = 0;
            while (a < this.jCheckboxArrayAbzeichen.length) {
                this.jCheckboxArrayAbzeichen[a].setEnabled(false);
                ++a;
            }
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                MitgliederAnlegenAO.this.buttonAktualisieren.setEnabled(true);
                TabelleMitglied dbdaten = new TabelleMitglied();
                TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
                try {
                    int mID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    MitgliederAnlegenAO.this.personalnummer.setText(Integer.toString(mID));
                    MitgliederAnlegenAO.this.aktuelleMitgliederDaten = null;
                    MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten = null;
                    MitgliederAnlegenAO.this.aktuelleMitgliederDaten = dbdaten.getAllMitgliederData(mID);
                    MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten = tabUntersuchung.getAllMitgliederUntersuchungData(mID);
                    gruppe.setSelectedItem(MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("mitgliederGruppe"));
                    anrede.setSelectedItem(MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("anrede"));
                    MitgliederAnlegenAO.this.name.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("name"));
                    MitgliederAnlegenAO.this.vorname.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("vorname"));
                    MitgliederAnlegenAO.this.strasse.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("strasse"));
                    MitgliederAnlegenAO.this.ort.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("ort"));
                    MitgliederAnlegenAO.this.telPrivat.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("telefonPrivat"));
                    MitgliederAnlegenAO.this.telMobil.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("telefonMobil"));
                    MitgliederAnlegenAO.this.telArbeit.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("telefonArbeit"));
                    MitgliederAnlegenAO.this.email.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("email"));
                    MitgliederAnlegenAO.this.email2.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("email2"));
                    dienstgrad.setSelectedItem(MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("dienstgrad"));
                    MitgliederAnlegenAO.this.gebDatum.setText(TimeCalculation.parseDateForGUI((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("geburtsdatum")));
                    MitgliederAnlegenAO.this.hochzeitstag.setText(TimeCalculation.parseDateForGUI((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("hochzeit")));
                    MitgliederAnlegenAO.this.ablaufdatumDienstausweis.setText(TimeCalculation.parseDateForGUI((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("ablaufDienstausweis")));
                    MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.setText(TimeCalculation.parseDateForGUI((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("pruefungDerFahrberechtigung")));
                    MitgliederAnlegenAO.this.beruf.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("beruf"));
                    MitgliederAnlegenAO.this.telegrammID.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("telegrammID"));
                    if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy")) {
                        MitgliederAnlegenAO.this.mitgliedSeit.setText(((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("mitgliedSeit")).substring(0, 4));
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
                        if (((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("mitgliedSeit")).toString().length() == 4) {
                            MitgliederAnlegenAO.this.mitgliedSeit.setText(TimeCalculation.parseDateForGUI(String.valueOf((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("mitgliedSeit")) + "-01-01"));
                        } else {
                            MitgliederAnlegenAO.this.mitgliedSeit.setText(TimeCalculation.parseDateForGUI((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("mitgliedSeit")));
                        }
                    }
                    MitgliederAnlegenAO.this.austritt.setText(TimeCalculation.parseDateForGUI((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("mitgliedBis")));
                    MitgliederAnlegenAO.this.kommentar.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("kommentar"));
                    MitgliederAnlegenAO.this.fuehrerscheinNummer.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("fuehrerscheinNummer"));
                    MitgliederAnlegenAO.this.fahrberechtigungNummer.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("fahrberechtigungNummer"));
                    MitgliederAnlegenAO.this.dienstausweisNummer.setText((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("dienstausweisNummer"));
                    if (dbdaten.getAusserDienstStatus(mID) == 1) {
                        MitgliederAnlegenAO.this.ausserDienst.setSelected(true);
                        MitgliederAnlegenAO.this.buttonAusserDienst.setText("In Dienst");
                    } else {
                        MitgliederAnlegenAO.this.ausserDienst.setSelected(false);
                        MitgliederAnlegenAO.this.buttonAusserDienst.setText("Au\u00dfer Dienst");
                    }
                    if (((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("eMailDeaktiv")).equals("1")) {
                        MitgliederAnlegenAO.this.eMailVerteilungDeaktivieren.setSelected(true);
                    } else {
                        MitgliederAnlegenAO.this.eMailVerteilungDeaktivieren.setSelected(false);
                    }
                    if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("Liste")) {
                        ((DefaultTableModel)MitgliederAnlegenAO.this.tableLehrg\u00e4nge.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "Lehrgang"), TabelleMitglieder_laufbahn.headnameLehrgang);
                        ((DefaultTableModel)MitgliederAnlegenAO.this.tableF\u00fchrerschein.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "F\u00fchrerschein"), TabelleMitglieder_laufbahn.headnameF\u00fchrerschein);
                        ((DefaultTableModel)MitgliederAnlegenAO.this.tableFunktion.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "Funktion"), TabelleMitglieder_laufbahn.headnameFunktion);
                        ((DefaultTableModel)MitgliederAnlegenAO.this.tableEhrungen.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "Ehrungen"), TabelleMitglieder_laufbahn.headnameEhrung);
                        ((DefaultTableModel)MitgliederAnlegenAO.this.tableDienstgrad.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "Dienstgrad"), TabelleMitglieder_laufbahn.headnameDienstgrad);
                    } else if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
                        int[] mustBeSelected = tabLaufbahn.getLehrgangSeminarData(mID);
                        int[] mustBeSelectedF\u00fchrerschein = tabLaufbahn.getF\u00fchrerscheinData(mID);
                        int[] mustBeSelectedFunktion = tabLaufbahn.getFunktionData(mID);
                        int[] mustBeSelectedFunktionAu\u00dferhalb = tabLaufbahn.getFunktionAu\u00dferhalbData(mID);
                        int[] mustBeSelectedEhrungen = tabLaufbahn.getEhrungenData(mID);
                        int[] mustBeSelectedAbzeichen = tabLaufbahn.getAbzeichenData(mID);
                        int i = 0;
                        while (i < mustBeSelected.length) {
                            if (mustBeSelected[i] == 1) {
                                MitgliederAnlegenAO.this.jCheckboxArrayLehrgang[i].setSelected(true);
                            } else {
                                MitgliederAnlegenAO.this.jCheckboxArrayLehrgang[i].setSelected(false);
                            }
                            ++i;
                        }
                        int f\u00fc = 0;
                        while (f\u00fc < mustBeSelectedF\u00fchrerschein.length) {
                            if (mustBeSelectedF\u00fchrerschein[f\u00fc] == 1) {
                                MitgliederAnlegenAO.this.jCheckboxArrayF\u00fchrerschein[f\u00fc].setSelected(true);
                            } else {
                                MitgliederAnlegenAO.this.jCheckboxArrayF\u00fchrerschein[f\u00fc].setSelected(false);
                            }
                            ++f\u00fc;
                        }
                        int f = 0;
                        while (f < mustBeSelectedFunktion.length) {
                            if (mustBeSelectedFunktion[f] == 1) {
                                MitgliederAnlegenAO.this.jCheckboxArrayFunktion[f].setSelected(true);
                            } else {
                                MitgliederAnlegenAO.this.jCheckboxArrayFunktion[f].setSelected(false);
                            }
                            ++f;
                        }
                        int f_au\u00dferhalb = 0;
                        while (f_au\u00dferhalb < mustBeSelectedFunktionAu\u00dferhalb.length) {
                            if (mustBeSelectedFunktionAu\u00dferhalb[f_au\u00dferhalb] == 1) {
                                MitgliederAnlegenAO.this.jCheckboxArrayFunktionAu\u00dferhalb[f_au\u00dferhalb].setSelected(true);
                            } else {
                                MitgliederAnlegenAO.this.jCheckboxArrayFunktionAu\u00dferhalb[f_au\u00dferhalb].setSelected(false);
                            }
                            ++f_au\u00dferhalb;
                        }
                        int e = 0;
                        while (e < mustBeSelectedEhrungen.length) {
                            if (mustBeSelectedEhrungen[e] == 1) {
                                MitgliederAnlegenAO.this.jCheckboxArrayEhrungen[e].setSelected(true);
                            } else {
                                MitgliederAnlegenAO.this.jCheckboxArrayEhrungen[e].setSelected(false);
                            }
                            ++e;
                        }
                        e = 0;
                        while (e < mustBeSelectedAbzeichen.length) {
                            if (mustBeSelectedAbzeichen[e] == 1) {
                                MitgliederAnlegenAO.this.jCheckboxArrayAbzeichen[e].setSelected(true);
                            } else {
                                MitgliederAnlegenAO.this.jCheckboxArrayAbzeichen[e].setSelected(false);
                            }
                            ++e;
                        }
                    }
                    MitgliederAnlegenAO.this.tabPane.setSelectedIndex(0);
                    if (BerechtigunsManager.ber[93] == 1) {
                        MitgliederAnlegenAO.this.sichtbarkeitSetzen(true);
                    } else {
                        MitgliederAnlegenAO.this.sichtbarkeitSetzen(false);
                    }
                    if (BerechtigunsManager.ber[94] == 1) {
                        MitgliederAnlegenAO.this.buttonUntersuchung.setEnabled(true);
                    } else {
                        MitgliederAnlegenAO.this.buttonUntersuchung.setEnabled(false);
                    }
                    if (BerechtigunsManager.ber[86] == 0) {
                        logging.logInfo((Object)"Berechtigung fehlt zum editieren der Lehrg\u00e4nge");
                    }
                    if (MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.isEmpty() && ((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("geburtsdatum")).equals("") && ((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("hochzeit")).equals("")) {
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisZumGeburtstag.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG25.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG26.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG30.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG41.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG42.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisAblaufLKW.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisAblaufDienstausweis.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickPr\u00fcfungDerFahrberechtigung.setText(null);
                        MitgliederAnlegenAO.this.\u00fcberblickHochzeitstag.setText(null);
                        logging.logWarning((Object)"\u00dcberblick Daten sind nicht vorhanden...");
                    } else {
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisZumGeburtstag.setText("Geburtstag in:                                         " + TimeCalculation.getTageBisGeburtstag(((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("geburtsdatum")).toString().substring(4, 10)));
                        this.setLabelColorGeburtstag(MitgliederAnlegenAO.this.\u00fcberblickTageBisZumGeburtstag);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG25.setText("N\u00e4chste G25 Untersuchung in:          " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("g25")));
                        this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickTageBisG25);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG26.setText("N\u00e4chste G26 Untersuchung in:          " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("g26")));
                        this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickTageBisG26);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG30.setText("N\u00e4chste G30 Untersuchung in:          " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("g30")));
                        this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickTageBisG30);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG41.setText("N\u00e4chste G41 Untersuchung in:          " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("g41")));
                        this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickTageBisG41);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisG42.setText("N\u00e4chste G42 Untersuchung in:          " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("g42")));
                        this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickTageBisG42);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisAblaufLKW.setText("Ablauf des LKW F\u00fchrerscheins in:    " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("ablaufLKW")));
                        this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickTageBisAblaufLKW);
                        MitgliederAnlegenAO.this.\u00fcberblickTageBisAblaufDienstausweis.setText("Ablauf des Dienstausweises in:        " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("ablaufDienstausweis")));
                        this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickTageBisAblaufDienstausweis);
                        if (runApplication.EINSTELLUNGEN.get("pr\u00fcfungDerFahrerlaubnis").equals("1")) {
                            MitgliederAnlegenAO.this.\u00fcberblickPr\u00fcfungDerFahrberechtigung.setText("Pr\u00fcfung der Fahrberechtigung in:      " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleUntersuchngsdaten.get("pruefungDerFahrberechtigung")));
                            this.setLabelColor(MitgliederAnlegenAO.this.\u00fcberblickPr\u00fcfungDerFahrberechtigung);
                        }
                        if (runApplication.EINSTELLUNGEN.get("hochzeitFeldFuerMitglieder").equals("1")) {
                            MitgliederAnlegenAO.this.\u00fcberblickHochzeitstag.setText("Hochzeitstag in:                                     " + TimeCalculation.getTageBis((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("hochzeit")));
                        }
                        logging.logInfo((Object)"\u00dcberblick Daten wurden errechnet...");
                    }
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
                catch (StringIndexOutOfBoundsException e) {
                    tree.expandPath(tree.getSelectionPath());
                }
                catch (NullPointerException nullPointerException) {
                }
                catch (NumberFormatException numberFormatException) {
                    // empty catch block
                }
            }

            private void setLabelColor(JLabel label) {
                if (label.getText().contains("abgelaufen")) {
                    label.setForeground(Color.red);
                    logging.logInfo((Object)(String.valueOf(label.getText()) + " == setForeground(Color.red)"));
                } else if (label.getText().contains("Morgen") | label.getText().contains("Heute")) {
                    label.setForeground(Color.orange);
                    logging.logInfo((Object)(String.valueOf(label.getText()) + " == setForeground(Color.orange)"));
                } else {
                    label.setForeground(null);
                    logging.logInfo((Object)(String.valueOf(label.getText()) + " == setForeground(null)"));
                }
            }

            private void setLabelColorGeburtstag(JLabel label) {
                if (label.getText().contains("Heute")) {
                    label.setForeground(Color.GREEN);
                    logging.logInfo((Object)(String.valueOf(label.getText()) + " == setForeground(Color.orange)"));
                } else {
                    label.setForeground(null);
                    logging.logInfo((Object)(String.valueOf(label.getText()) + " == setForeground(null)"));
                }
            }
        });
        this.buttonEhrungEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (runApplication.EINSTELLUNGEN.get("LehrgangEintragenAusMitgliederVerwaltungMode").equals("1")) {
                        mitgliedID = MitgliederAnlegenAO.this.personalnummer.getText();
                        mitgliedName = String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText();
                        MyEvent.setEvent((String)"0x9999");
                        logging.logInfo((Object)"Komplexe Laufbahnverwaltung ist aktiviert...");
                        Steuerung.setStatus(Status.LAUFBAHN_EINTRAG);
                        Steuerung.steuerung();
                    } else {
                        TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                        TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                        Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                        int mID = Integer.parseInt(MitgliederAnlegenAO.this.personalnummer.getText());
                        JFrame frame = new JFrame("Frage");
                        Object[] nichtBestandeneLehrg\u00e4nge = Utils.listToArray(tabLaufbahn.getNichtBestandeneListe(mID, "'EH', 'AB'"));
                        String neuerLehrgang = (String)JOptionPane.showInputDialog(frame, "Welcher Ehrung / Abzeichen soll zu der Mitglieder Laufbahn von\n(" + MitgliederAnlegenAO.this.personalnummer.getText() + ") " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " hinzugef\u00fcgt werden?\n\n", "Frage", 3, null, nichtBestandeneLehrg\u00e4nge, nichtBestandeneLehrg\u00e4nge[0]);
                        if (neuerLehrgang != null) {
                            logging.logInfo((Object)("Neue Ehrung / Abzeichen f\u00fcr: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " --> " + neuerLehrgang));
                            int lID = tabLehrgangKategorie.getLehrgangID(neuerLehrgang);
                            laufbahn.setAlterDienstgrad(0);
                            laufbahn.setNeuerDienstgrad(0);
                            laufbahn.setArt(tabLehrgangKategorie.getArt(lID));
                            laufbahn.setDatum("");
                            laufbahn.setDatumVon("");
                            laufbahn.setId(tabLaufbahn.getNextNumber());
                            laufbahn.setLehrgang(lID);
                            laufbahn.setMitgliederID(mID);
                            laufbahn.setUe(tabLehrgangKategorie.getUnterrichtseinheiten(lID));
                            tabLaufbahn.insert(laufbahn);
                            if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("Liste")) {
                                ((DefaultTableModel)MitgliederAnlegenAO.this.tableEhrungen.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "Ehrungen"), TabelleMitglieder_laufbahn.headnameEhrung);
                            } else if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
                                int[] mustBeSelectedEhrungen = tabLaufbahn.getEhrungenData(mID);
                                int[] mustBeSelectedAbzeichen = tabLaufbahn.getAbzeichenData(mID);
                                int i = 0;
                                while (i < mustBeSelectedEhrungen.length) {
                                    if (mustBeSelectedEhrungen[i] == 1) {
                                        MitgliederAnlegenAO.this.jCheckboxArrayEhrungen[i].setSelected(true);
                                    } else {
                                        MitgliederAnlegenAO.this.jCheckboxArrayEhrungen[i].setSelected(false);
                                    }
                                    ++i;
                                }
                                i = 0;
                                while (i < mustBeSelectedAbzeichen.length) {
                                    if (mustBeSelectedAbzeichen[i] == 1) {
                                        MitgliederAnlegenAO.this.jCheckboxArrayAbzeichen[i].setSelected(true);
                                    } else {
                                        MitgliederAnlegenAO.this.jCheckboxArrayAbzeichen[i].setSelected(false);
                                    }
                                    ++i;
                                }
                            }
                            logging.logInfo((Object)"Laufbahn wurde aktualisiert und wird jetzt angezeigt...");
                        } else {
                            logging.logWarning((Object)"Hoppala, es wurde nicht zum eintragen ausgew\u00e4hlt...");
                        }
                    }
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonFunktionEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                    TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                    Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                    int mID = Integer.parseInt(MitgliederAnlegenAO.this.personalnummer.getText());
                    JFrame frame = new JFrame("Frage");
                    Object[] nichtBestandeneLehrg\u00e4nge = Utils.listToArray(tabLaufbahn.getNichtBestandeneListe(mID, "'F', 'F_Au\u00dferhalb'"));
                    String neuerLehrgang = (String)JOptionPane.showInputDialog(frame, "Welcher Funktion soll zu der Mitglieder Laufbahn von\n(" + MitgliederAnlegenAO.this.personalnummer.getText() + ") " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " hinzugef\u00fcgt werden?\n\n", "Frage", 3, null, nichtBestandeneLehrg\u00e4nge, nichtBestandeneLehrg\u00e4nge[0]);
                    if (neuerLehrgang != null) {
                        logging.logInfo((Object)("Neue Funktion f\u00fcr: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " --> " + neuerLehrgang));
                        int lID = tabLehrgangKategorie.getLehrgangID(neuerLehrgang);
                        laufbahn.setAlterDienstgrad(0);
                        laufbahn.setNeuerDienstgrad(0);
                        laufbahn.setArt(tabLehrgangKategorie.getArt(lID));
                        laufbahn.setDatum("");
                        laufbahn.setDatumVon("");
                        laufbahn.setId(tabLaufbahn.getNextNumber());
                        laufbahn.setLehrgang(lID);
                        laufbahn.setMitgliederID(mID);
                        laufbahn.setUe(tabLehrgangKategorie.getUnterrichtseinheiten(lID));
                        tabLaufbahn.insert(laufbahn);
                        if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("Liste")) {
                            ((DefaultTableModel)MitgliederAnlegenAO.this.tableFunktion.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "Funktion"), TabelleMitglieder_laufbahn.headnameFunktion);
                        } else if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
                            int[] mustBeSelectedFunktion = tabLaufbahn.getFunktionData(mID);
                            int[] mustBeSelectedFunktionAu\u00dferhalb = tabLaufbahn.getFunktionAu\u00dferhalbData(mID);
                            int i = 0;
                            while (i < mustBeSelectedFunktion.length) {
                                if (mustBeSelectedFunktion[i] == 1) {
                                    MitgliederAnlegenAO.this.jCheckboxArrayFunktion[i].setSelected(true);
                                } else {
                                    MitgliederAnlegenAO.this.jCheckboxArrayFunktion[i].setSelected(false);
                                }
                                ++i;
                            }
                            i = 0;
                            while (i < mustBeSelectedFunktionAu\u00dferhalb.length) {
                                if (mustBeSelectedFunktionAu\u00dferhalb[i] == 1) {
                                    MitgliederAnlegenAO.this.jCheckboxArrayFunktionAu\u00dferhalb[i].setSelected(true);
                                } else {
                                    MitgliederAnlegenAO.this.jCheckboxArrayFunktionAu\u00dferhalb[i].setSelected(false);
                                }
                                ++i;
                            }
                        }
                        logging.logInfo((Object)"Laufbahn wurde aktualisiert und wird jetzt angezeigt...");
                    } else {
                        logging.logWarning((Object)"Hoppala, es wurde nicht zum eintragen ausgew\u00e4hlt...");
                    }
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonF\u00fchrerscheinEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                    TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                    Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                    int mID = Integer.parseInt(MitgliederAnlegenAO.this.personalnummer.getText());
                    JFrame frame = new JFrame("Frage");
                    Object[] nichtBestandeneLehrg\u00e4nge = Utils.listToArray(tabLaufbahn.getNichtBestandeneListe(mID, "'F\u00fc'"));
                    String neuerLehrgang = (String)JOptionPane.showInputDialog(frame, "Welcher F\u00fchrerschein soll zu der Mitglieder Laufbahn von\n(" + MitgliederAnlegenAO.this.personalnummer.getText() + ") " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " hinzugef\u00fcgt werden?\n\n", "Frage", 3, null, nichtBestandeneLehrg\u00e4nge, nichtBestandeneLehrg\u00e4nge[0]);
                    if (neuerLehrgang != null) {
                        logging.logInfo((Object)("Neuer bestandener F\u00fchrerschein f\u00fcr: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " --> " + neuerLehrgang));
                        int lID = tabLehrgangKategorie.getLehrgangID(neuerLehrgang);
                        laufbahn.setAlterDienstgrad(0);
                        laufbahn.setNeuerDienstgrad(0);
                        laufbahn.setArt(tabLehrgangKategorie.getArt(lID));
                        laufbahn.setDatum("");
                        laufbahn.setDatumVon("");
                        laufbahn.setId(tabLaufbahn.getNextNumber());
                        laufbahn.setLehrgang(lID);
                        laufbahn.setMitgliederID(mID);
                        laufbahn.setUe(tabLehrgangKategorie.getUnterrichtseinheiten(lID));
                        tabLaufbahn.insert(laufbahn);
                        if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("Liste")) {
                            ((DefaultTableModel)MitgliederAnlegenAO.this.tableF\u00fchrerschein.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "F\u00fchrerschein"), TabelleMitglieder_laufbahn.headnameF\u00fchrerschein);
                        } else if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
                            int[] mustBeSelectedF\u00fchrerschein = tabLaufbahn.getF\u00fchrerscheinData(mID);
                            int i = 0;
                            while (i < mustBeSelectedF\u00fchrerschein.length) {
                                if (mustBeSelectedF\u00fchrerschein[i] == 1) {
                                    MitgliederAnlegenAO.this.jCheckboxArrayF\u00fchrerschein[i].setSelected(true);
                                } else {
                                    MitgliederAnlegenAO.this.jCheckboxArrayF\u00fchrerschein[i].setSelected(false);
                                }
                                ++i;
                            }
                        }
                        logging.logInfo((Object)"Laufbahn wurde aktualisiert und wird jetzt angezeigt...");
                    } else {
                        logging.logWarning((Object)"Hoppala, es wurde nicht zum eintragen ausgew\u00e4hlt...");
                    }
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonLehrgangEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (runApplication.EINSTELLUNGEN.get("LehrgangEintragenAusMitgliederVerwaltungMode").equals("1")) {
                        mitgliedID = MitgliederAnlegenAO.this.personalnummer.getText();
                        mitgliedName = String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText();
                        MyEvent.setEvent((String)"0x9999");
                        logging.logInfo((Object)"Komplexe Laufbahnverwaltung ist aktiviert...");
                        Steuerung.setStatus(Status.LAUFBAHN_EINTRAG);
                        Steuerung.steuerung();
                    } else {
                        logging.logInfo((Object)"Komplexe Laufbahnverwaltung ist deaktiviert...");
                        TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                        TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                        Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                        int mID = Integer.parseInt(MitgliederAnlegenAO.this.personalnummer.getText());
                        JFrame frame = new JFrame("Frage");
                        Object[] nichtBestandeneLehrg\u00e4nge = Utils.listToArray(tabLaufbahn.getNichtBestandeneListe(mID, "'L', 'S', 'E'"));
                        String neuerLehrgang = (String)JOptionPane.showInputDialog(frame, "Welcher Lehrgang soll zu der Mitglieder Laufbahn von\n(" + MitgliederAnlegenAO.this.personalnummer.getText() + ") " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " hinzugef\u00fcgt werden?\n\n", "Frage", 3, null, nichtBestandeneLehrg\u00e4nge, nichtBestandeneLehrg\u00e4nge[0]);
                        if (neuerLehrgang != null) {
                            logging.logInfo((Object)("Neuer bestandener Lehrgang f\u00fcr: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " --> " + neuerLehrgang));
                            int lID = tabLehrgangKategorie.getLehrgangID(neuerLehrgang);
                            laufbahn.setAlterDienstgrad(0);
                            laufbahn.setNeuerDienstgrad(0);
                            laufbahn.setArt(tabLehrgangKategorie.getArt(lID));
                            laufbahn.setDatum("");
                            laufbahn.setDatumVon("");
                            laufbahn.setId(tabLaufbahn.getNextNumber());
                            laufbahn.setLehrgang(lID);
                            laufbahn.setMitgliederID(mID);
                            laufbahn.setUe(tabLehrgangKategorie.getUnterrichtseinheiten(lID));
                            tabLaufbahn.insert(laufbahn);
                            if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("Liste")) {
                                ((DefaultTableModel)MitgliederAnlegenAO.this.tableLehrg\u00e4nge.getModel()).setDataVector(new TabelleMitglieder_laufbahn().getLaufbahnForTable(mID, "Lehrgang"), TabelleMitglieder_laufbahn.headnameLehrgang);
                            } else if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
                                int[] mustBeSelected = tabLaufbahn.getLehrgangSeminarData(mID);
                                int i = 0;
                                while (i < mustBeSelected.length) {
                                    if (mustBeSelected[i] == 1) {
                                        MitgliederAnlegenAO.this.jCheckboxArrayLehrgang[i].setSelected(true);
                                    } else {
                                        MitgliederAnlegenAO.this.jCheckboxArrayLehrgang[i].setSelected(false);
                                    }
                                    ++i;
                                }
                            }
                            logging.logInfo((Object)"Laufbahn wurde aktualisiert und wird jetzt angezeigt...");
                        } else {
                            logging.logWarning((Object)"Hoppala, es wurde nicht zum eintragen ausgew\u00e4hlt...");
                        }
                    }
                }
                catch (NumberFormatException | SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonSuche.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!MitgliederAnlegenAO.this.buttonSuche.getText().equals("Filter aus")) {
                    String msg = JOptionPane.showInputDialog("Bitte Namen f\u00fcr die Suche Eingeben:\n\nEs wird nach Namen und Vornamen gesucht!");
                    tree.setModel(CreateTrees.CreateTreeMitgliederListe(msg));
                    int i = 0;
                    while (i < tree.getRowCount()) {
                        tree.expandRow(i);
                        ++i;
                    }
                    MitgliederAnlegenAO.this.buttonSuche.setText("Filter aus");
                    MitgliederAnlegenAO.this.buttonSuche.setBackground(Color.cyan);
                } else {
                    MitgliederAnlegenAO.this.buttonSuche.setText("Suche / Filter");
                    tree.setModel(CreateTrees.CreateTreeMitgliederListe(null));
                    MitgliederAnlegenAO.this.buttonSuche.setBackground(null);
                }
            }
        });
        this.buttonLoeschkenner.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int msg = JOptionPane.showConfirmDialog(null, Konstante.WIRKLICH_LOESCHEN, "Frage", 0);
                if (msg == 0) {
                    try {
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        tabMitglied.updateLoeschkenner(Integer.parseInt(MitgliederAnlegenAO.this.personalnummer.getText()), 1);
                        logging.logInfo((Object)("Mitglied " + MitgliederAnlegenAO.this.personalnummer.getText() + " gel\u00f6scht..."));
                        logbuchEingabe.NeuerEintag("Mitglied " + MitgliederAnlegenAO.this.personalnummer.getText() + " gel\u00f6scht...");
                        tree.setModel(CreateTrees.CreateTreeMitgliederListe(null));
                        PDFMitgliedLoeschkenner.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + MitgliederAnlegenAO.this.personalnummer.getText() + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Mitglieder_L\u00f6schkenner.pdf", String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText());
                        JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                        MitgliederAnlegenAO.this.sichtbarkeitSetzen(false);
                    }
                    catch (DocumentException | IOException | NumberFormatException | SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonDienstgradAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0029");
                Steuerung.setStatus(Status.DIENSTGRAD_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonAnredeAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0025");
                Steuerung.setStatus(Status.ANREDE_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonHistory.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mitgliedID = MitgliederAnlegenAO.this.personalnummer.getText();
                Steuerung.setStatus(Status.MITGLIEDER_HISTORY);
                Steuerung.steuerung();
            }
        });
        this.buttonNeueMitgliederGruppe.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0022");
                Steuerung.setStatus(Status.MITGLIEDER_GRUPPE_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonUntersuchung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mitgliedID = MitgliederAnlegenAO.this.personalnummer.getText();
                mitgliedName = String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText();
                Steuerung.setStatus(Status.UNTERSUCHUNG_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonBankverbindung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mitgliedID = MitgliederAnlegenAO.this.personalnummer.getText();
                mitgliedName = String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText();
                Steuerung.setStatus(Status.BANKVERBINDUNG_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonAngehoerige.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mitgliedID = MitgliederAnlegenAO.this.personalnummer.getText();
                mitgliedName = String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText();
                Steuerung.setStatus(Status.ANGEHORIGE_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonArbeitgeber.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                mitgliedID = MitgliederAnlegenAO.this.personalnummer.getText();
                mitgliedName = String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText();
                Steuerung.setStatus(Status.ARBEITGEBER_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglied tabelleMitglied = new TabelleMitglied();
                TabelleMitglieder_anrede tabAnrede = new TabelleMitglieder_anrede();
                TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
                TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
                TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
                Mitglieder mitglied = new Mitglieder();
                Mitglieder_Untersuchung untersuchung = new Mitglieder_Untersuchung();
                TabelleMitglieder_History tabHistory = new TabelleMitglieder_History();
                try {
                    if (!Utils.checkText(MitgliederAnlegenAO.this.name.getText(), ",")) {
                        logging.logInfo((Object)"Hoopala, ich habe ein verbotendes Zeichen endeckt");
                        JOptionPane.showMessageDialog(null, Konstante.FALSCHES_ZEICHEN, "Fehlermeldung", 0);
                        MitgliederAnlegenAO.this.name.setBackground(Color.red);
                    } else if (!Utils.checkText(MitgliederAnlegenAO.this.vorname.getText(), ",")) {
                        logging.logInfo((Object)"Hoopala, ich habe ein verbotendes Zeichen endeckt");
                        JOptionPane.showMessageDialog(null, Konstante.FALSCHES_ZEICHEN, "Fehlermeldung", 0);
                        MitgliederAnlegenAO.this.vorname.setBackground(Color.red);
                    } else if (anrede.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_ANREDE_AUSWAEHELEN, "Fehlermeldung", 2);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("1") && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy") && MitgliederAnlegenAO.this.mitgliedSeit.getText().length() != 4 | !MitgliederAnlegenAO.this.mitgliedSeit.getText().toString().matches("[+-]?[0-9]+")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_YYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("1") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.mitgliedSeit.getText()) && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_DDMMYYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.austritt.getText()) && !MitgliederAnlegenAO.this.austritt.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.austritt.setBackground(Color.red);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("0") && !MitgliederAnlegenAO.this.mitgliedSeit.getText().equals("") && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy") && MitgliederAnlegenAO.this.mitgliedSeit.getText().length() != 4 | !MitgliederAnlegenAO.this.mitgliedSeit.getText().toString().matches("[+-]?[0-9]+")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_YYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("0") && !MitgliederAnlegenAO.this.mitgliedSeit.getText().equals("") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.mitgliedSeit.getText()) && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_DDMMYYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.gebDatum.getText()) && !MitgliederAnlegenAO.this.gebDatum.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.gebDatum.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.hochzeitstag.getText()) && !MitgliederAnlegenAO.this.hochzeitstag.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.hochzeitstag.setBackground(Color.red);
                    } else if (!MitgliederAnlegenAO.this.ablaufdatumDienstausweis.getText().equals("") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.ablaufdatumDienstausweis.getText())) {
                        MitgliederAnlegenAO.this.ablaufdatumDienstausweis.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else if (!MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.getText().equals("") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.getText())) {
                        MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else if (!MitgliederAnlegenAO.this.telegrammID.getText().toString().matches("[+-]?[0-9]+") && !MitgliederAnlegenAO.this.telegrammID.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.TELEGRAMM_ID, "Warnung", 2);
                        MitgliederAnlegenAO.this.telegrammID.setBackground(Color.red);
                    } else {
                        MitgliederAnlegenAO.this.name.setBackground(Color.white);
                        MitgliederAnlegenAO.this.vorname.setBackground(Color.white);
                        MitgliederAnlegenAO.this.gebDatum.setBackground(Color.white);
                        MitgliederAnlegenAO.this.hochzeitstag.setBackground(Color.white);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.white);
                        MitgliederAnlegenAO.this.ablaufdatumDienstausweis.setBackground(Color.white);
                        MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.setBackground(Color.white);
                        MitgliederAnlegenAO.this.telegrammID.setBackground(Color.white);
                        MitgliederAnlegenAO.this.austritt.setBackground(Color.white);
                        int mID = Integer.parseInt(MitgliederAnlegenAO.this.personalnummer.getText());
                        int mGruppe = tabGruppe.getID(gruppe.getSelectedItem().toString());
                        int dGrad = tabDienstgrad.getDienstgradID(dienstgrad.getSelectedItem().toString());
                        int alterDiestgrad = tabDienstgrad.getDienstgradID((String)MitgliederAnlegenAO.this.aktuelleMitgliederDaten.get("dienstgrad"));
                        mitglied.setMandantID(Integer.parseInt(runApplication.PROPERTIES.get("MandantID")));
                        mitglied.setMitgliederGruppe(mGruppe);
                        mitglied.setAnrede(tabAnrede.getAnredeID(anrede.getSelectedItem().toString()));
                        mitglied.setId(mID);
                        mitglied.setName(MitgliederAnlegenAO.this.name.getText());
                        mitglied.setVorname(MitgliederAnlegenAO.this.vorname.getText());
                        mitglied.setStrasse(MitgliederAnlegenAO.this.strasse.getText());
                        mitglied.setOrt(MitgliederAnlegenAO.this.ort.getText());
                        mitglied.setTelefonePrivate(MitgliederAnlegenAO.this.telPrivat.getText());
                        mitglied.setTelefonMobile(MitgliederAnlegenAO.this.telMobil.getText());
                        mitglied.setTelefonArbeit(MitgliederAnlegenAO.this.telArbeit.getText());
                        mitglied.setTelegrammID(MitgliederAnlegenAO.this.telegrammID.getText());
                        mitglied.setEmail(MitgliederAnlegenAO.this.email.getText());
                        mitglied.setEmail2(MitgliederAnlegenAO.this.email2.getText());
                        mitglied.setBeruf(MitgliederAnlegenAO.this.beruf.getText());
                        mitglied.setDienstgrad(dGrad);
                        mitglied.setGebDatum(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.gebDatum.getText()));
                        mitglied.setAusserDienst(0);
                        mitglied.setHochzeit(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.hochzeitstag.getText()));
                        if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy")) {
                            mitglied.setMitgliedSeit(MitgliederAnlegenAO.this.mitgliedSeit.getText());
                        } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
                            mitglied.setMitgliedSeit(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.mitgliedSeit.getText()));
                        }
                        mitglied.setMitgliedBis(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.austritt.getText()));
                        mitglied.setKommentar(MitgliederAnlegenAO.this.kommentar.getText());
                        mitglied.setFuehrerscheinNummer(MitgliederAnlegenAO.this.fuehrerscheinNummer.getText());
                        mitglied.setFahrberechtigungNummer(MitgliederAnlegenAO.this.fahrberechtigungNummer.getText());
                        mitglied.setDienstausweisNummer(MitgliederAnlegenAO.this.dienstausweisNummer.getText());
                        mitglied.setLoschkenner(0);
                        mitglied.seteMailVerteilung(MitgliederAnlegenAO.this.eMailVerteilungDeaktivieren.isSelected() ? 1 : 0);
                        tabelleMitglied.update(mitglied);
                        tabHistory.insert(mitglied);
                        logging.logInfo((Object)"Speichern in der Datenbank erfolgreich");
                        untersuchung.setInfoAblaufDienstausweis(0);
                        untersuchung.setAblaufDienstausweis(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.ablaufdatumDienstausweis.getText()));
                        untersuchung.setInfoPruefungDerFahrberechtigung(0);
                        untersuchung.setPruefungDerFahrberechtigung(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.getText()));
                        untersuchung.setId(mID);
                        if (tabUntersuchung.getCount(mID) == 1) {
                            tabUntersuchung.updateDienstausweis(untersuchung);
                        } else {
                            untersuchung.setG25("");
                            untersuchung.setG26("");
                            untersuchung.setG30("");
                            untersuchung.setG42("");
                            untersuchung.setG41("");
                            untersuchung.setAtemschutztraining("");
                            untersuchung.setAblaufLKW("");
                            untersuchung.setId(mID);
                            untersuchung.setInfoAblaufLKW(0);
                            untersuchung.setInfoG25(0);
                            untersuchung.setInfoG26(0);
                            untersuchung.setInfoG30(0);
                            tabUntersuchung.insert(untersuchung);
                        }
                        logging.logInfo((Object)"Tabelle Untersuchungen aktualisiert");
                        if (alterDiestgrad != dGrad && !dienstgrad.getSelectedItem().toString().equals("<Kein Dienstgrad>")) {
                            Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                            TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                            laufbahn.setId(tabLaufbahn.getNextNumber());
                            laufbahn.setMitgliederID(mID);
                            laufbahn.setDatumVon("");
                            laufbahn.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                            laufbahn.setArt("D");
                            laufbahn.setAlterDienstgrad(alterDiestgrad);
                            laufbahn.setNeuerDienstgrad(dGrad);
                            tabLaufbahn.insert(laufbahn);
                            logging.logInfo((Object)"Dienstgrad wurde ge\u00e4ndert und die Laufbahn wurde aktualisiert...");
                        }
                        tree.setModel(CreateTrees.CreateTreeMitgliederListe(null));
                        tree.expandRow(mGruppe);
                        MitgliederAnlegenAO.this.buttonSuche.setText("Suche / Filter");
                        MitgliederAnlegenAO.this.buttonSuche.setBackground(null);
                        MitgliederAnlegenAO.this.aktuelleMitgliederDaten = tabelleMitglied.getAllMitgliederData(mID);
                        PDFMitgliedInfo.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_MitgliederKateAktualisiert.pdf", mitglied, untersuchung);
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_MitgliederKateAktualisiert.pdf");
                        logging.logInfo((Object)"Mitglied erfolgreich aktualisiert");
                        logbuchEingabe.NeuerEintag("Mitglieder Daten wurden aktualisiert: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                }
                catch (DocumentException | IOException | NumberFormatException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglied tabelleMitglied = new TabelleMitglied();
                TabelleMitglieder_anrede tabAnrede = new TabelleMitglieder_anrede();
                TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
                TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
                TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
                Mitglieder_Untersuchung untersuchung = new Mitglieder_Untersuchung();
                Mitglieder mitglied = new Mitglieder();
                Mitgliederlaufbahn laufbahn = new Mitgliederlaufbahn();
                TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
                TabelleMitglieder_History tabHistory = new TabelleMitglieder_History();
                try {
                    if (dienstgrad.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.DIENSTGRAD_WAEHLEM, "Warnung", 2);
                    } else if (!Utils.checkText(MitgliederAnlegenAO.this.name.getText(), ",")) {
                        logging.logInfo((Object)"Hoopala, ich habe ein verbotendes Zeichen endeckt");
                        JOptionPane.showMessageDialog(null, Konstante.FALSCHES_ZEICHEN, "Fehlermeldung", 0);
                        MitgliederAnlegenAO.this.name.setBackground(Color.red);
                    } else if (!Utils.checkText(MitgliederAnlegenAO.this.vorname.getText(), ",")) {
                        logging.logInfo((Object)"Hoopala, ich habe ein verbotendes Zeichen endeckt");
                        JOptionPane.showMessageDialog(null, Konstante.FALSCHES_ZEICHEN, "Fehlermeldung", 0);
                        MitgliederAnlegenAO.this.vorname.setBackground(Color.red);
                    } else if (gruppe.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        logging.logInfo((Object)"Benutzergruppe wurde nicht ausgew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.KEINE_BENUTZERGRUPPE_GEWAEHLT, "Warnung", 2);
                    } else if (anrede.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_ANREDE_AUSWAEHELEN, "Fehlermeldung", 2);
                    } else if (tabelleMitglied.getMitgliederCountByNachnameVorname(MitgliederAnlegenAO.this.name.getText(), MitgliederAnlegenAO.this.vorname.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.MITGLIED_NAME_BEREITS_VORHANDEN, "Fehlermeldung", 0);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("1") && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy") && MitgliederAnlegenAO.this.mitgliedSeit.getText().length() != 4 | !MitgliederAnlegenAO.this.mitgliedSeit.getText().toString().matches("[+-]?[0-9]+")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_YYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("1") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.mitgliedSeit.getText()) && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_DDMMYYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("0") && !MitgliederAnlegenAO.this.mitgliedSeit.getText().equals("") && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy") && MitgliederAnlegenAO.this.mitgliedSeit.getText().length() != 4 | !MitgliederAnlegenAO.this.mitgliedSeit.getText().toString().matches("[+-]?[0-9]+")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_YYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitPflichtEintrag").equals("0") && !MitgliederAnlegenAO.this.mitgliedSeit.getText().equals("") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.mitgliedSeit.getText()) && runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
                        JOptionPane.showMessageDialog(null, Konstante.FEHLER_MITGLIED_SEIT_DDMMYYYY, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.austritt.getText()) && !MitgliederAnlegenAO.this.austritt.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.austritt.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.gebDatum.getText()) && !MitgliederAnlegenAO.this.gebDatum.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.gebDatum.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.hochzeitstag.getText()) && !MitgliederAnlegenAO.this.hochzeitstag.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 2);
                        MitgliederAnlegenAO.this.hochzeitstag.setBackground(Color.red);
                    } else if (!MitgliederAnlegenAO.this.ablaufdatumDienstausweis.getText().equals("") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.ablaufdatumDienstausweis.getText())) {
                        MitgliederAnlegenAO.this.ablaufdatumDienstausweis.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else if (!MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.getText().equals("") && !TimeCalculation.checkDateFormat(MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.getText())) {
                        MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.setBackground(Color.red);
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                    } else if (!MitgliederAnlegenAO.this.telegrammID.getText().toString().matches("[+-]?[0-9]+") && !MitgliederAnlegenAO.this.telegrammID.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.TELEGRAMM_ID, "Warnung", 2);
                        MitgliederAnlegenAO.this.telegrammID.setBackground(Color.red);
                    } else {
                        MitgliederAnlegenAO.this.name.setBackground(Color.white);
                        MitgliederAnlegenAO.this.vorname.setBackground(Color.white);
                        MitgliederAnlegenAO.this.gebDatum.setBackground(Color.white);
                        MitgliederAnlegenAO.this.hochzeitstag.setBackground(Color.white);
                        MitgliederAnlegenAO.this.mitgliedSeit.setBackground(Color.white);
                        MitgliederAnlegenAO.this.ablaufdatumDienstausweis.setBackground(Color.white);
                        MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.setBackground(Color.white);
                        MitgliederAnlegenAO.this.telegrammID.setBackground(Color.white);
                        MitgliederAnlegenAO.this.austritt.setBackground(Color.white);
                        int mID = tabelleMitglied.getNextFreeNumber();
                        int mGruppe = tabGruppe.getID(gruppe.getSelectedItem().toString());
                        int dGrad = tabDienstgrad.getDienstgradID(dienstgrad.getSelectedItem().toString());
                        MitgliederAnlegenAO.this.personalnummer.setText(Integer.toString(mID));
                        mitglied.setMandantID(Integer.parseInt(runApplication.PROPERTIES.get("MandantID")));
                        mitglied.setMitgliederGruppe(mGruppe);
                        mitglied.setAnrede(tabAnrede.getAnredeID(anrede.getSelectedItem().toString()));
                        mitglied.setId(mID);
                        mitglied.setName(MitgliederAnlegenAO.this.name.getText());
                        mitglied.setVorname(MitgliederAnlegenAO.this.vorname.getText());
                        mitglied.setStrasse(MitgliederAnlegenAO.this.strasse.getText());
                        mitglied.setOrt(MitgliederAnlegenAO.this.ort.getText());
                        mitglied.setTelefonePrivate(MitgliederAnlegenAO.this.telPrivat.getText());
                        mitglied.setTelefonMobile(MitgliederAnlegenAO.this.telMobil.getText());
                        mitglied.setTelefonArbeit(MitgliederAnlegenAO.this.telArbeit.getText());
                        mitglied.setTelegrammID(MitgliederAnlegenAO.this.telegrammID.getText());
                        mitglied.setEmail(MitgliederAnlegenAO.this.email.getText());
                        mitglied.setEmail2(MitgliederAnlegenAO.this.email2.getText());
                        mitglied.setBeruf(MitgliederAnlegenAO.this.beruf.getText());
                        mitglied.setDienstgrad(dGrad);
                        mitglied.setGebDatum(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.gebDatum.getText()));
                        mitglied.setAusserDienst(0);
                        mitglied.setHochzeit(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.hochzeitstag.getText()));
                        if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("yyyy")) {
                            mitglied.setMitgliedSeit(MitgliederAnlegenAO.this.mitgliedSeit.getText());
                        } else if (runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat").equals("dd.MM.yyyy")) {
                            mitglied.setMitgliedSeit(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.mitgliedSeit.getText()));
                        }
                        mitglied.setMitgliedBis(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.austritt.getText()));
                        mitglied.setKommentar(MitgliederAnlegenAO.this.kommentar.getText());
                        mitglied.setFuehrerscheinNummer(MitgliederAnlegenAO.this.fuehrerscheinNummer.getText());
                        mitglied.setFahrberechtigungNummer(MitgliederAnlegenAO.this.fahrberechtigungNummer.getText());
                        mitglied.setDienstausweisNummer(MitgliederAnlegenAO.this.dienstausweisNummer.getText());
                        mitglied.setLoschkenner(0);
                        mitglied.seteMailVerteilung(MitgliederAnlegenAO.this.eMailVerteilungDeaktivieren.isSelected() ? 1 : 0);
                        tabelleMitglied.insert(mitglied);
                        tabHistory.insert(mitglied);
                        logging.logInfo((Object)"Speichern in der Datenbank erfolgreich");
                        untersuchung.setInfoAblaufDienstausweis(0);
                        untersuchung.setAblaufDienstausweis(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.ablaufdatumDienstausweis.getText()));
                        untersuchung.setInfoPruefungDerFahrberechtigung(0);
                        untersuchung.setPruefungDerFahrberechtigung(TimeCalculation.parseDateForDatabase(MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.getText()));
                        untersuchung.setG25("");
                        untersuchung.setG26("");
                        untersuchung.setG30("");
                        untersuchung.setG42("");
                        untersuchung.setG41("");
                        untersuchung.setAtemschutztraining("");
                        untersuchung.setAblaufLKW("");
                        untersuchung.setId(mID);
                        untersuchung.setInfoAblaufLKW(0);
                        untersuchung.setInfoG25(0);
                        untersuchung.setInfoG26(0);
                        untersuchung.setInfoG30(0);
                        tabUntersuchung.insert(untersuchung);
                        logging.logInfo((Object)"Tabelle Untersuchungen aktualisiert");
                        if (!dienstgrad.getSelectedItem().toString().equals("<Kein Dienstgrad>")) {
                            laufbahn.setId(tabLaufbahn.getNextNumber());
                            laufbahn.setMitgliederID(mID);
                            laufbahn.setDatumVon("");
                            laufbahn.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                            laufbahn.setArt("D");
                            laufbahn.setAlterDienstgrad(0);
                            laufbahn.setNeuerDienstgrad(dGrad);
                            tabLaufbahn.insert(laufbahn);
                            logging.logInfo((Object)"Dienstgrad in die Laufbahn eingetragen...");
                        }
                        Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID, runApplication.clientID);
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_MitgliederKarte.pdf");
                        PDFMitgliedInfo.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_MitgliederKarte.pdf", mitglied, untersuchung);
                        logging.logInfo((Object)"N\u00e4che Mitgliedernummer wurde aktualisiert");
                        logging.logInfo((Object)"Mitglied erfolgreich gespeichert");
                        logbuchEingabe.NeuerEintag("Neues Mitglied wurde gespeichert: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        tree.setEnabled(true);
                        MitgliederAnlegenAO.this.buttonAktualisieren.setVisible(true);
                        MitgliederAnlegenAO.this.buttonSpeichern.setVisible(false);
                        MitgliederAnlegenAO.this.buttonBankverbindung.setEnabled(true);
                        MitgliederAnlegenAO.this.buttonUntersuchung.setEnabled(true);
                        MitgliederAnlegenAO.this.buttonAngehoerige.setEnabled(true);
                        MitgliederAnlegenAO.this.buttonArbeitgeber.setEnabled(true);
                        MitgliederAnlegenAO.this.buttonAbbruch.setVisible(false);
                        MitgliederAnlegenAO.this.buttonHistory.setEnabled(true);
                        MitgliederAnlegenAO.this.buttonAusserDienst.setEnabled(true);
                        MitgliederAnlegenAO.this.buttonLoeschkenner.setEnabled(true);
                        tree.setModel(CreateTrees.CreateTreeMitgliederListe(null));
                        tree.expandRow(mGruppe);
                        MitgliederAnlegenAO.this.buttonSuche.setText("Suche / Filter");
                        MitgliederAnlegenAO.this.buttonSuche.setBackground(null);
                        MitgliederAnlegenAO.this.aktuelleMitgliederDaten = tabelleMitglied.getAllMitgliederData(mID);
                    }
                }
                catch (DocumentException | IOException | NumberFormatException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAusserDienst.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    int mID = tabMitglied.getIdByGuiString(String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText());
                    if (MitgliederAnlegenAO.this.buttonAusserDienst.getText().equals("In Dienst")) {
                        tabMitglied.updateAusserDienst(mID, 0);
                        PDFMitgliedInDienst.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_InDienstGestellt.pdf", String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText());
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_InDienstGestellt.pdf");
                        logging.logInfo((Object)("Mitglied: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " wurde Ausser Dienst gestellt"));
                        MitgliederAnlegenAO.this.ausserDienst.setSelected(false);
                        MitgliederAnlegenAO.this.buttonAusserDienst.setText("Au\u00dfer Dienst");
                    } else if (MitgliederAnlegenAO.this.buttonAusserDienst.getText().equals("Au\u00dfer Dienst")) {
                        tabMitglied.updateAusserDienst(mID, 1);
                        PDFMitgliedAusserDienst.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Au\u00dferDienstGestellt.pdf", String.valueOf(MitgliederAnlegenAO.this.name.getText()) + ", " + MitgliederAnlegenAO.this.vorname.getText());
                        Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Au\u00dferDienstGestellt.pdf");
                        logging.logInfo((Object)("Mitglied: " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText() + " wurde in Dienst gestellt"));
                        MitgliederAnlegenAO.this.ausserDienst.setSelected(true);
                        MitgliederAnlegenAO.this.buttonAusserDienst.setText("In Dienst");
                    }
                    logbuchEingabe.NeuerEintag("Mitglied Au\u00dfer Dienst Status ge\u00e4ndert zu: " + Integer.toString(MitgliederAnlegenAO.this.ausserDienst.isSelected() ? 1 : 0) + " " + MitgliederAnlegenAO.this.name.getText() + ", " + MitgliederAnlegenAO.this.vorname.getText());
                }
                catch (DocumentException | IOException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNeu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MitgliederAnlegenAO.this.tabPane.setSelectedIndex(0);
                tree.setEnabled(false);
                MitgliederAnlegenAO.this.buttonAktualisieren.setVisible(false);
                MitgliederAnlegenAO.this.buttonSpeichern.setVisible(true);
                MitgliederAnlegenAO.this.buttonBankverbindung.setEnabled(false);
                MitgliederAnlegenAO.this.buttonUntersuchung.setEnabled(false);
                MitgliederAnlegenAO.this.buttonAngehoerige.setEnabled(false);
                MitgliederAnlegenAO.this.buttonArbeitgeber.setEnabled(false);
                MitgliederAnlegenAO.this.buttonAbbruch.setVisible(true);
                MitgliederAnlegenAO.this.eMailVerteilungDeaktivieren.setEnabled(true);
                MitgliederAnlegenAO.this.personalnummer.setText(null);
                MitgliederAnlegenAO.this.name.setText(null);
                MitgliederAnlegenAO.this.vorname.setText(null);
                MitgliederAnlegenAO.this.strasse.setText(null);
                MitgliederAnlegenAO.this.ort.setText(null);
                MitgliederAnlegenAO.this.telPrivat.setText(null);
                MitgliederAnlegenAO.this.telMobil.setText(null);
                MitgliederAnlegenAO.this.telArbeit.setText(null);
                MitgliederAnlegenAO.this.email.setText(null);
                MitgliederAnlegenAO.this.email2.setText(null);
                MitgliederAnlegenAO.this.gebDatum.setText(null);
                MitgliederAnlegenAO.this.kommentar.setText(null);
                MitgliederAnlegenAO.this.fuehrerscheinNummer.setText(null);
                MitgliederAnlegenAO.this.fahrberechtigungNummer.setText(null);
                MitgliederAnlegenAO.this.dienstausweisNummer.setText(null);
                MitgliederAnlegenAO.this.hochzeitstag.setText(null);
                MitgliederAnlegenAO.this.ablaufdatumDienstausweis.setText(null);
                MitgliederAnlegenAO.this.pr\u00fcfungDerFahrberechtigung.setText(null);
                MitgliederAnlegenAO.this.beruf.setText(null);
                MitgliederAnlegenAO.this.telegrammID.setText(null);
                anrede.setSelectedItem("<bitte w\u00e4hlen>");
                gruppe.setSelectedItem("<bitte w\u00e4hlen>");
                dienstgrad.setSelectedItem("<bitte w\u00e4hlen>");
                MitgliederAnlegenAO.this.mitgliedSeit.setText(SbcUtils.timeStamp((String)runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat")));
                MitgliederAnlegenAO.this.sichtbarkeitSetzen(true);
                MitgliederAnlegenAO.this.buttonAngehoerige.setEnabled(false);
                MitgliederAnlegenAO.this.buttonArbeitgeber.setEnabled(false);
                MitgliederAnlegenAO.this.buttonBankverbindung.setEnabled(false);
                MitgliederAnlegenAO.this.buttonUntersuchung.setEnabled(false);
                MitgliederAnlegenAO.this.buttonHistory.setEnabled(false);
                MitgliederAnlegenAO.this.buttonAusserDienst.setEnabled(false);
                MitgliederAnlegenAO.this.buttonLoeschkenner.setEnabled(false);
                MitgliederAnlegenAO.this.eMailVerteilungDeaktivieren.setSelected(false);
                if (runApplication.EINSTELLUNGEN.get("darstellungLehrg\u00e4ngeMitgliederverwaltung").equals("CheckBox")) {
                    int x = 0;
                    while (x < MitgliederAnlegenAO.this.jCheckboxArrayLehrgang.length) {
                        MitgliederAnlegenAO.this.jCheckboxArrayLehrgang[x].setSelected(false);
                        ++x;
                    }
                    x = 0;
                    while (x < MitgliederAnlegenAO.this.jCheckboxArrayAbzeichen.length) {
                        MitgliederAnlegenAO.this.jCheckboxArrayAbzeichen[x].setSelected(false);
                        ++x;
                    }
                    x = 0;
                    while (x < MitgliederAnlegenAO.this.jCheckboxArrayEhrungen.length) {
                        MitgliederAnlegenAO.this.jCheckboxArrayEhrungen[x].setSelected(false);
                        ++x;
                    }
                    x = 0;
                    while (x < MitgliederAnlegenAO.this.jCheckboxArrayFunktion.length) {
                        MitgliederAnlegenAO.this.jCheckboxArrayFunktion[x].setSelected(false);
                        ++x;
                    }
                    x = 0;
                    while (x < MitgliederAnlegenAO.this.jCheckboxArrayFunktionAu\u00dferhalb.length) {
                        MitgliederAnlegenAO.this.jCheckboxArrayFunktionAu\u00dferhalb[x].setSelected(false);
                        ++x;
                    }
                    x = 0;
                    while (x < MitgliederAnlegenAO.this.jCheckboxArrayF\u00fchrerschein.length) {
                        MitgliederAnlegenAO.this.jCheckboxArrayF\u00fchrerschein[x].setSelected(false);
                        ++x;
                    }
                }
            }
        });
        this.buttonAbbruch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                tree.setSelectionRow(0);
                tree.setEnabled(true);
                MitgliederAnlegenAO.this.buttonAktualisieren.setVisible(true);
                MitgliederAnlegenAO.this.buttonSpeichern.setVisible(false);
                MitgliederAnlegenAO.this.buttonBankverbindung.setEnabled(true);
                MitgliederAnlegenAO.this.buttonUntersuchung.setEnabled(true);
                MitgliederAnlegenAO.this.buttonAngehoerige.setEnabled(true);
                MitgliederAnlegenAO.this.buttonArbeitgeber.setEnabled(true);
                MitgliederAnlegenAO.this.buttonAbbruch.setVisible(false);
                MitgliederAnlegenAO.this.sichtbarkeitSetzen(false);
            }
        });
        this.vCard.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(1);
                    chooser.showSaveDialog(null);
                    Object[] auswahl = tree.getSelectionPath().getPath();
                    VCard.export(chooser.getSelectedFile().getPath(), runApplication.EINSTELLUNGEN.get("vCardSeperator"), new TabelleMitglieder_gruppe().getID(auswahl[1].toString()));
                    logging.logInfo((Object)"vCard wurde erfogreich exportiert");
                }
                catch (SQLException e) {
                    logging.logError((Object)"Fehler beim Exportieren der vCard");
                    logging.logPrintStackTrace((Exception)e);
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

