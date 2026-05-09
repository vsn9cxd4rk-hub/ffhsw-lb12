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
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleBrandsicherheitswache;
import data.tabellen.TabelleBrandsicherheitswachen_temp;
import data.tabellen.TabelleStatistikbsw;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.Brandsicherheitswachen;
import go.Brandsicherheitswachen_temp;
import go.StatistikBSW;
import go.Veranstaltung;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.joomla.Joomla;
import utilities.logbuchEingabe;

public class BrandsicherheitswacheEintragenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAnwesenheit;
    private JButton buttonNaechsetBSWEintargen;
    private JTextField bswNummer;
    private JTextField datum;
    private JTextField zeit_treffen;
    private JTextField zeit_start;
    private JTextField zeit_ende;
    private JComboBox<String> ort;
    private JComboBox<String> art;
    private JLabel bswnummer_label;
    private JLabel datum_label;
    private JLabel zeit_treffen_label;
    private JLabel zeit_start_label;
    private JLabel zeit_ende_label;
    private JLabel ort_label;
    private JLabel art_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelBsw;

    public BrandsicherheitswacheEintragenAO() {
        super("FeuerwehrManagementSystem - Brandsicherheitswachen");
        logging.logInfo((Object)"Starte: BrandsicherheitswachenEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonAnwesenheit = new JButton("Anwesenheit eintragen");
        this.buttonNaechsetBSWEintargen = new JButton("N\u00e4chste BSW");
        this.datum = new JTextField(SbcUtils.timeStamp((String)"dd.MM.yyyy"), 20);
        this.zeit_start = new JTextField(20);
        this.zeit_treffen = new JTextField(20);
        this.zeit_ende = new JTextField(20);
        try {
            TabelleBrandsicherheitswache tabBsw = new TabelleBrandsicherheitswache();
            this.bswNummer = new JTextField("-", 20);
            String[] ortListe = Utils.listToArrayWithEmptyLine(tabBsw.getOrtListe());
            String[] artListe = Utils.listToArrayWithEmptyLine(tabBsw.getArtListe());
            this.ort = new JComboBox<String>(ortListe);
            this.art = new JComboBox<String>(artListe);
            this.zeit_treffen.setText(runApplication.EINSTELLUNGEN.get("vorbelegungBSWTreffen"));
            this.zeit_start.setText(runApplication.EINSTELLUNGEN.get("vorbelegungBSWVeranstaltungStart"));
            this.zeit_ende.setText(runApplication.EINSTELLUNGEN.get("vorbelegungBSWEnde"));
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        this.bswnummer_label = new JLabel("BSW Z\u00e4hlung:");
        this.datum_label = new JLabel("Datum: ");
        this.zeit_start_label = new JLabel("Veranstaltungsbeginn: ");
        this.zeit_treffen_label = new JLabel("Treffen BSW: ");
        this.zeit_ende_label = new JLabel("Ende BSW: ");
        this.ort_label = new JLabel("Ort:");
        this.art_label = new JLabel("Art:  ");
        this.modulBeschreibung = new JLabel("Brandsicherheitswachen Eintragen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
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
        this.setSize(530, 310);
        this.setTitle("FeuerwehrManagementSystem - Brandsicherheitswachen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelBsw = new JPanel(new GridLayout(7, 2));
        this.getContentPane().add("Center", this.panelBsw);
        this.panelBsw.add(this.bswnummer_label);
        this.panelBsw.add(this.bswNummer);
        this.panelBsw.add(this.datum_label);
        this.panelBsw.add(this.datum);
        this.panelBsw.add(this.ort_label);
        this.panelBsw.add(this.ort);
        this.panelBsw.add(this.art_label);
        this.panelBsw.add(this.art);
        this.panelBsw.add(this.zeit_treffen_label);
        this.panelBsw.add(this.zeit_treffen);
        this.panelBsw.add(this.zeit_start_label);
        this.panelBsw.add(this.zeit_start);
        this.panelBsw.add(this.zeit_ende_label);
        this.panelBsw.add(this.zeit_ende);
        this.bswNummer.setEditable(false);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonAnwesenheit);
        this.add(this.buttonNaechsetBSWEintargen);
        this.buttonNaechsetBSWEintargen.setVisible(false);
        this.buttonAnwesenheit.setEnabled(false);
        this.ort.setEditable(true);
        this.art.setEditable(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String eingabe;
                TabelleBrandsicherheitswache tabBsw = new TabelleBrandsicherheitswache();
                TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                TabelleStatistikbsw tabStatistik = new TabelleStatistikbsw();
                TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
                TabelleMitglied tabMitglieder = new TabelleMitglied();
                TabelleBrandsicherheitswachen_temp tabTemp = new TabelleBrandsicherheitswachen_temp();
                Brandsicherheitswachen bsw = new Brandsicherheitswachen();
                StatistikBSW statistik = new StatistikBSW();
                Veranstaltung veranstaltung = new Veranstaltung();
                Brandsicherheitswachen_temp temp = new Brandsicherheitswachen_temp();
                if (BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText().length() == 4) {
                    eingabe = BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText();
                    BrandsicherheitswacheEintragenAO.this.zeit_treffen.setText("0" + eingabe);
                }
                if (BrandsicherheitswacheEintragenAO.this.zeit_start.getText().length() == 4) {
                    eingabe = BrandsicherheitswacheEintragenAO.this.zeit_start.getText();
                    BrandsicherheitswacheEintragenAO.this.zeit_start.setText("0" + eingabe);
                }
                if (BrandsicherheitswacheEintragenAO.this.zeit_ende.getText().length() == 4) {
                    eingabe = BrandsicherheitswacheEintragenAO.this.zeit_ende.getText();
                    BrandsicherheitswacheEintragenAO.this.zeit_ende.setText("0" + eingabe);
                }
                if (!TimeCalculation.checkDateFormat(BrandsicherheitswacheEintragenAO.this.datum.getText())) {
                    BrandsicherheitswacheEintragenAO.this.datum.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkTimeFormat(BrandsicherheitswacheEintragenAO.this.zeit_start.getText())) {
                    BrandsicherheitswacheEintragenAO.this.zeit_start.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkTimeFormat(BrandsicherheitswacheEintragenAO.this.zeit_ende.getText())) {
                    BrandsicherheitswacheEintragenAO.this.zeit_ende.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkTimeFormat(BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText())) {
                    BrandsicherheitswacheEintragenAO.this.zeit_treffen.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else {
                    try {
                        BrandsicherheitswacheEintragenAO.this.datum.setBackground(Color.white);
                        BrandsicherheitswacheEintragenAO.this.zeit_start.setBackground(Color.white);
                        BrandsicherheitswacheEintragenAO.this.zeit_ende.setBackground(Color.white);
                        BrandsicherheitswacheEintragenAO.this.zeit_treffen.setBackground(Color.white);
                        String nameVeransatltung = "BSW (" + BrandsicherheitswacheEintragenAO.this.ort.getSelectedItem().toString() + " " + BrandsicherheitswacheEintragenAO.this.datum.getText() + ", " + BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText() + ")";
                        int zaehler = tabVeransatltung.getCount(3, BrandsicherheitswacheEintragenAO.this.datum.getText(), BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText());
                        if (zaehler != 0) {
                            nameVeransatltung = "BSW" + zaehler + " (" + BrandsicherheitswacheEintragenAO.this.ort.getSelectedItem().toString() + " " + BrandsicherheitswacheEintragenAO.this.datum.getText() + ", " + BrandsicherheitswacheEintragenAO.this.zeit_start.getText() + ")";
                        }
                        int vID = tabVeransatltung.getNextNummer();
                        int bNummer = tabBsw.getNextNummer(BrandsicherheitswacheEintragenAO.this.datum.getText().substring(6, 10));
                        BrandsicherheitswacheEintragenAO.this.bswNummer.setText(Integer.toString(bNummer));
                        veranstaltung.setId(vID);
                        veranstaltung.setDatum(TimeCalculation.parseDateForDatabase(BrandsicherheitswacheEintragenAO.this.datum.getText()));
                        veranstaltung.setZeit(BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText());
                        veranstaltung.setZeitEnde(BrandsicherheitswacheEintragenAO.this.zeit_ende.getText());
                        veranstaltung.setName(nameVeransatltung);
                        veranstaltung.setName2("BSW_" + BrandsicherheitswacheEintragenAO.this.ort.getSelectedItem().toString());
                        veranstaltung.setKategorie(3);
                        veranstaltung.setFahrzeugeinteilung(1);
                        veranstaltung.setInfoVersandt(0);
                        tabVeransatltung.insert(veranstaltung);
                        if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1")) {
                            Joomla.erstelleVeranstaltung(veranstaltung);
                        }
                        logging.logInfo((Object)"Veransatltung wurde eingetragen");
                        bsw.setId(tabBsw.getNextID());
                        bsw.setBswNummer(Integer.parseInt(BrandsicherheitswacheEintragenAO.this.bswNummer.getText()));
                        bsw.setJahr(Integer.parseInt(BrandsicherheitswacheEintragenAO.this.datum.getText().substring(6, 10)));
                        bsw.setVeranstaltungID(tabVeransatltung.getVeranstaltungID(nameVeransatltung));
                        bsw.setOrt(BrandsicherheitswacheEintragenAO.this.ort.getSelectedItem().toString());
                        bsw.setArt(BrandsicherheitswacheEintragenAO.this.art.getSelectedItem().toString());
                        bsw.setDatum(TimeCalculation.parseDateForDatabase(BrandsicherheitswacheEintragenAO.this.datum.getText()));
                        bsw.setZeit_treffen(BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText());
                        bsw.setZeit_start(BrandsicherheitswacheEintragenAO.this.zeit_start.getText());
                        bsw.setZeit_ende(BrandsicherheitswacheEintragenAO.this.zeit_ende.getText());
                        tabBsw.insert(bsw);
                        statistik.setId(tabStatistik.getNextNummer());
                        statistik.setVeranstaltungID(vID);
                        statistik.setBswID(Integer.parseInt(BrandsicherheitswacheEintragenAO.this.bswNummer.getText()));
                        statistik.setJahr(Integer.parseInt(BrandsicherheitswacheEintragenAO.this.datum.getText().substring(6, 10)));
                        statistik.setDauer(TimeCalculation.calculateDuration(BrandsicherheitswacheEintragenAO.this.zeit_treffen.getText(), BrandsicherheitswacheEintragenAO.this.zeit_ende.getText()));
                        statistik.setMannstunden(0);
                        statistik.setWochentag(TimeCalculation.wochentagErmitteln(BrandsicherheitswacheEintragenAO.this.datum.getText()));
                        tabStatistik.insert(statistik);
                        tabTemp.deleteAll();
                        String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
                        int i = 0;
                        while (i < mitgliederListe.length) {
                            int komma = mitgliederListe[i].toString().indexOf(",");
                            String isSelectedName = mitgliederListe[i].toString().substring(0, komma);
                            String isSelectedVorname = mitgliederListe[i].toString().substring(komma + 2, mitgliederListe[i].toString().length());
                            int mID = tabMitglieder.getId(isSelectedName, isSelectedVorname);
                            temp.setMitgliederID(mID);
                            temp.setBeteiligung(tabAnwesenheit.getBeteiligungByKategorie(mID, 3, Integer.parseInt(BrandsicherheitswacheEintragenAO.this.datum.getText().substring(6, 10))));
                            tabTemp.insert(temp);
                            ++i;
                        }
                        logging.logInfo((Object)"BSW wurde eingetragen");
                        runApplication.letzterVeranstaltungsname = nameVeransatltung;
                        BrandsicherheitswacheEintragenAO.this.buttonAnwesenheit.setEnabled(true);
                        BrandsicherheitswacheEintragenAO.this.buttonSpeichern.setEnabled(false);
                        BrandsicherheitswacheEintragenAO.this.buttonNaechsetBSWEintargen.setVisible(true);
                        logbuchEingabe.NeuerEintag("Brandsicherheitswache erstellt: " + nameVeransatltung);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (runApplication.EINSTELLUNGEN.get("bswHitliste").equals("1")) {
                            String[] beteiligung = Utils.listToArray(tabTemp.getListOfBeteiligung());
                            StringBuilder build = new StringBuilder();
                            int b = 0;
                            while (b < beteiligung.length) {
                                build.append(beteiligung[b]);
                                build.append("\n");
                                ++b;
                            }
                            JOptionPane.showMessageDialog(null, "Hitliste-Brandsicherheitswachen\n\n" + build.toString());
                        }
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonAnwesenheit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BrandsicherheitswacheEintragenAO.this.dispose();
                logging.logInfo((Object)"Schlie\u00dfe BrandsicherheitswachenEintragenAO und Starte: AnwesenheitEintargenAO");
                MyEvent.setEvent((String)"0x0011");
                Steuerung.setStatus(Status.ANWESENHEIT_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonNaechsetBSWEintargen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                BrandsicherheitswacheEintragenAO.this.buttonAnwesenheit.setVisible(false);
                BrandsicherheitswacheEintragenAO.this.buttonNaechsetBSWEintargen.setVisible(false);
                BrandsicherheitswacheEintragenAO.this.zeit_treffen.setText(null);
                BrandsicherheitswacheEintragenAO.this.zeit_ende.setText(null);
                BrandsicherheitswacheEintragenAO.this.zeit_start.setText(null);
                BrandsicherheitswacheEintragenAO.this.datum.setText(null);
                BrandsicherheitswacheEintragenAO.this.ort.setSelectedIndex(0);
                BrandsicherheitswacheEintragenAO.this.art.setSelectedIndex(0);
                BrandsicherheitswacheEintragenAO.this.buttonSpeichern.setEnabled(true);
                BrandsicherheitswacheEintragenAO.this.bswNummer.setText("-");
                BrandsicherheitswacheEintragenAO.this.zeit_treffen.setText(runApplication.EINSTELLUNGEN.get("vorbelegungBSWTreffen"));
                BrandsicherheitswacheEintragenAO.this.zeit_start.setText(runApplication.EINSTELLUNGEN.get("vorbelegungBSWVeranstaltungStart"));
                BrandsicherheitswacheEintragenAO.this.zeit_ende.setText(runApplication.EINSTELLUNGEN.get("vorbelegungBSWEnde"));
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

