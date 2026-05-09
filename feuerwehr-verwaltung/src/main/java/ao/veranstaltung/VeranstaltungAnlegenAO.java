/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.veranstaltung;

import ao.AbstractFenster;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import data.tabellen.statistik.TabelleStatistikSonstigeVeranstaltung;
import go.StatistikSonstigeVeranstaltung;
import go.Veranstaltung;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

public class VeranstaltungAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonAnwesenheit;
    private JButton buttonVeranstaltungKategorieAnlegen;
    private JButton buttonNaechsteVeranstaltung;
    private JTextField name;
    private JTextField datum;
    private JTextField zeit;
    private JTextField zeitEnde;
    private JLabel name_label;
    private JLabel datum_label;
    private JLabel zeit_label;
    private JLabel zeitEnde_label;
    private JLabel kateogie_label;
    public static JComboBox<String> kategorie;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelVeranstaltung;

    public VeranstaltungAnlegenAO() {
        super("FeuerwehrManagementSystem - Veranstaltung");
        logging.logInfo((Object)"Starte: VeranstaltungAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAnwesenheit = new JButton("Anwesenheit eintragen");
        this.buttonVeranstaltungKategorieAnlegen = new JButton("Veranstaltungskategorie anlegen");
        this.buttonNaechsteVeranstaltung = new JButton("N\u00e4chste Veranstaltung");
        this.modulBeschreibung = new JLabel("Veranstaltung Anlegen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.datum = new JTextField(SbcUtils.timeStamp((String)"dd.MM.yyyy"), 20);
        this.zeit = new JTextField(20);
        this.zeitEnde = new JTextField(20);
        this.name_label = new JLabel("Veranstaltungsname: ");
        this.datum_label = new JLabel("Datum: ");
        this.zeit_label = new JLabel("Zeit: ");
        this.kateogie_label = new JLabel("Kategorie: ");
        this.zeitEnde_label = new JLabel("Zeit Ende: ");
        String[] liste = null;
        TabelleVeranstaltung_Kategorie kategorieListe = new TabelleVeranstaltung_Kategorie();
        try {
            liste = Utils.listToArrayOnlyFORComboBoxes(kategorieListe.getAllKategorien());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        kategorie = new JComboBox<String>(liste);
        kategorie.removeItem("Einsatz");
        kategorie.removeItem("BSW");
    }

    protected void labelErstellen() {
        kategorie.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                VeranstaltungAnlegenAO.this.name.setText(kategorie.getSelectedItem().toString());
                if (kategorie.getSelectedItem().toString().equals("Dienstabend")) {
                    VeranstaltungAnlegenAO.this.zeit.setText(runApplication.EINSTELLUNGEN.get("vorbelegungDienstStart"));
                    VeranstaltungAnlegenAO.this.zeitEnde.setText(runApplication.EINSTELLUNGEN.get("vorbelegungDienstEnde"));
                }
            }
        });
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
        if (MyEvent.event.equals("0x0002")) {
            this.name.setText("Dienstabend");
            kategorie.setSelectedItem("Dienstabend");
            this.zeit.setText(runApplication.EINSTELLUNGEN.get("vorbelegungDienstStart"));
            this.zeitEnde.setText(runApplication.EINSTELLUNGEN.get("vorbelegungDienstEnde"));
        }
        if (MyEvent.event.equals("0x0003")) {
            this.name.setText("Sonstige");
            kategorie.setSelectedItem("Sonstige");
        }
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(580, 310);
        this.setTitle("FeuerwehrManagementSystem - Veranstaltung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonVeranstaltungKategorieAnlegen);
        this.panelVeranstaltung = new JPanel(new GridLayout(5, 2));
        this.getContentPane().add("Center", this.panelVeranstaltung);
        this.panelVeranstaltung.add(this.kateogie_label);
        this.panelVeranstaltung.add(kategorie);
        this.panelVeranstaltung.add(this.name_label);
        this.panelVeranstaltung.add(this.name);
        this.panelVeranstaltung.add(this.datum_label);
        this.panelVeranstaltung.add(this.datum);
        this.panelVeranstaltung.add(this.zeit_label);
        this.panelVeranstaltung.add(this.zeit);
        this.panelVeranstaltung.add(this.zeitEnde_label);
        this.panelVeranstaltung.add(this.zeitEnde);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonAnwesenheit);
        this.add(this.buttonNaechsteVeranstaltung);
        this.buttonAnwesenheit.setVisible(false);
        this.buttonNaechsteVeranstaltung.setVisible(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String eingabe;
                TabelleVeranstaltung tabelleVeranstaltung = new TabelleVeranstaltung();
                TabelleVeranstaltung_Kategorie tabelleKategorie = new TabelleVeranstaltung_Kategorie();
                Veranstaltung veranstaltung = new Veranstaltung();
                TabelleStatistikSonstigeVeranstaltung tabStatistik = new TabelleStatistikSonstigeVeranstaltung();
                StatistikSonstigeVeranstaltung statistik = new StatistikSonstigeVeranstaltung();
                if (VeranstaltungAnlegenAO.this.zeit.getText().length() == 4) {
                    eingabe = VeranstaltungAnlegenAO.this.zeit.getText();
                    VeranstaltungAnlegenAO.this.zeit.setText("0" + eingabe);
                }
                if (VeranstaltungAnlegenAO.this.zeitEnde.getText().length() == 4) {
                    eingabe = VeranstaltungAnlegenAO.this.zeitEnde.getText();
                    VeranstaltungAnlegenAO.this.zeitEnde.setText("0" + eingabe);
                }
                if (VeranstaltungAnlegenAO.this.datum.getText().length() <= 9) {
                    VeranstaltungAnlegenAO.this.datum.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkDateFormat(VeranstaltungAnlegenAO.this.datum.getText())) {
                    VeranstaltungAnlegenAO.this.datum.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkTimeFormat(VeranstaltungAnlegenAO.this.zeit.getText())) {
                    VeranstaltungAnlegenAO.this.zeit.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else if (!TimeCalculation.checkTimeFormat(VeranstaltungAnlegenAO.this.zeitEnde.getText()) && !VeranstaltungAnlegenAO.this.zeitEnde.getText().equals("")) {
                    VeranstaltungAnlegenAO.this.zeitEnde.setBackground(Color.red);
                    JOptionPane.showMessageDialog(null, Konstante.ZEITFORMAT_FALSCH, "Fehlermeldung", 0);
                } else {
                    try {
                        VeranstaltungAnlegenAO.this.datum.setBackground(Color.white);
                        VeranstaltungAnlegenAO.this.zeit.setBackground(Color.white);
                        VeranstaltungAnlegenAO.this.zeitEnde.setBackground(Color.white);
                        String nameVeranstaltung = String.valueOf(VeranstaltungAnlegenAO.this.name.getText()) + " (" + VeranstaltungAnlegenAO.this.datum.getText() + ", " + VeranstaltungAnlegenAO.this.zeit.getText() + ")";
                        int kID = tabelleKategorie.getID(kategorie.getSelectedItem().toString());
                        if (tabelleVeranstaltung.getCount(kID, VeranstaltungAnlegenAO.this.datum.getText(), VeranstaltungAnlegenAO.this.zeit.getText()) != 0) {
                            JOptionPane.showMessageDialog(null, Konstante.VERANSTALTUNG_SCHON_VORHANDEN, "Warnung", 2);
                        } else {
                            int vID = tabelleVeranstaltung.getNextNummer();
                            String datumDerVeranstaltung = TimeCalculation.parseDateForDatabase(VeranstaltungAnlegenAO.this.datum.getText());
                            veranstaltung.setId(vID);
                            veranstaltung.setName(nameVeranstaltung);
                            veranstaltung.setName2(VeranstaltungAnlegenAO.this.name.getText());
                            veranstaltung.setDatum(datumDerVeranstaltung);
                            veranstaltung.setZeit(VeranstaltungAnlegenAO.this.zeit.getText());
                            veranstaltung.setZeitEnde(VeranstaltungAnlegenAO.this.zeitEnde.getText());
                            veranstaltung.setKategorie(kID);
                            veranstaltung.setFahrzeugeinteilung(0);
                            veranstaltung.setInfoVersandt(0);
                            statistik.setId(tabStatistik.getNextNummer());
                            statistik.setVeranstaltungID(vID);
                            statistik.setKategorie(kID);
                            statistik.setJahr(Integer.parseInt(VeranstaltungAnlegenAO.this.datum.getText().substring(6, 10)));
                            statistik.setDauer(TimeCalculation.calculateDuration(VeranstaltungAnlegenAO.this.zeit.getText(), VeranstaltungAnlegenAO.this.zeitEnde.getText()));
                            statistik.setMannstunden(0);
                            statistik.setWochentag(TimeCalculation.wochentagErmitteln(VeranstaltungAnlegenAO.this.datum.getText()));
                            tabelleVeranstaltung.insert(veranstaltung);
                            tabStatistik.insert(statistik);
                            logging.logInfo((Object)"Veransatltung wurde eingetragen");
                            if (runApplication.EINSTELLUNGEN.get("JoomlaVeranstaltungSenden").equals("1")) {
                                Joomla.erstelleVeranstaltung(veranstaltung);
                            }
                            runApplication.letzterVeranstaltungsname = nameVeranstaltung;
                            VeranstaltungAnlegenAO.this.buttonSpeichern.setEnabled(false);
                            VeranstaltungAnlegenAO.this.buttonAnwesenheit.setVisible(true);
                            VeranstaltungAnlegenAO.this.buttonNaechsteVeranstaltung.setVisible(true);
                            logbuchEingabe.NeuerEintag("Veranstaltung angelegt: " + nameVeranstaltung);
                            if (VeranstaltungAnlegenAO.this.zeitEnde.getText().equals("")) {
                                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_VERANSTALTUNG_HINWEIS);
                            } else {
                                JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                            }
                        }
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonVeranstaltungKategorieAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0021");
                Steuerung.setStatus(Status.VERANSTALTUNG_KATEGORIE_ANLEGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonAnwesenheit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                VeranstaltungAnlegenAO.this.dispose();
                logging.logInfo((Object)"Schlie\u00dfe: DienstabendAnlegenAO und Starte Anwesenheit EintragenAO");
                MyEvent.setEvent((String)"0x0012");
                Steuerung.setStatus(Status.ANWESENHEIT_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonNaechsteVeranstaltung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                VeranstaltungAnlegenAO.this.buttonAnwesenheit.setVisible(false);
                VeranstaltungAnlegenAO.this.buttonSpeichern.setEnabled(true);
                VeranstaltungAnlegenAO.this.buttonNaechsteVeranstaltung.setVisible(false);
                VeranstaltungAnlegenAO.this.datum.setText(null);
                VeranstaltungAnlegenAO.this.zeit.setText(null);
                VeranstaltungAnlegenAO.this.zeitEnde.setText(null);
                kategorie.setSelectedItem("<bitte w\u00e4hlen>");
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

