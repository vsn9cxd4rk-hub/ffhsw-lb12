/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.karte;

import ao.AbstractFenster;
import ao.karte.KarteAO;
import data.tabellen.karte.TabelleStrassen;
import go.Stra\u00dfe;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;
import utilities.logbuchEingabe;

public class Stra\u00dfeEintragenAO
extends AbstractFenster {
    private JLabel stra\u00dfenname_label;
    private JLabel stra\u00dfenname2_label;
    private JTextField stra\u00dfenName2;
    private JLabel stra\u00dfeninfo_label;
    private JTextArea stra\u00dfeninfo;
    private JTextArea anfahrtInfo;
    private JLabel anfahrtInfo_label;
    private JLabel koordinaten_label;
    private JTextField koordinaten;
    private JLabel postleitzahl_label;
    private JTextField postleitzzahl;
    private JTextField datensatznummer;
    private JLabel datensatznummer_label;
    private JTextField bildStrasse;
    private JLabel bildStarsse_label;
    private JTextField bildStrasse2;
    private JLabel bildStarsse2_label;
    private JScrollPane anfahrtInfoPane;
    private JScrollPane stra\u00dfenInfoPane;
    public static JComboBox<String> StrassenName;
    private String strassendb;
    private JButton buttonAktualisieren;
    private JButton buttonSpeichern;
    private JButton buttonZurueck;
    private JButton ordnerauswahl1;
    private JButton ordnerauswahl2;
    private JPanel panel;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JFileChooser chooser;

    public Stra\u00dfeEintragenAO() {
        super("FeuerwehrManagementSystem Version: 3.21");
        logging.logInfo((Object)"Starte: Stra\u00dfeEintragenAO");
    }

    protected void buttonErstellen() {
        this.panel = new JPanel();
        this.stra\u00dfenname_label = new JLabel("Stra\u00dfen Name: ");
        this.stra\u00dfenname2_label = new JLabel("Stra\u00dfen Name: ");
        this.stra\u00dfenName2 = new JTextField(25);
        this.stra\u00dfeninfo_label = new JLabel("Stra\u00dfen Informationen: ");
        this.bildStarsse_label = new JLabel("Bild Stra\u00dfe Gro\u00df (Gr\u00f6\u00dfe: 980x590 Pixel): ");
        this.bildStarsse2_label = new JLabel("Bild Stra\u00dfe Klein (Gr\u00f6\u00dfe: 980x590 Pixel): ");
        this.stra\u00dfeninfo = new JTextArea(4, 24);
        this.bildStrasse = new JTextField(23);
        this.bildStrasse2 = new JTextField(23);
        this.koordinaten_label = new JLabel("Stadtplan Koordinaten: ");
        this.koordinaten = new JTextField(25);
        this.postleitzahl_label = new JLabel("Postleitzahl / Ort: ");
        this.postleitzzahl = new JTextField(25);
        this.anfahrtInfo_label = new JLabel("Anfahrt Information: ");
        this.anfahrtInfo = new JTextArea(4, 24);
        this.datensatznummer_label = new JLabel("Datensatznummer:");
        this.datensatznummer = new JTextField(25);
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.ordnerauswahl1 = new JButton("...");
        this.ordnerauswahl2 = new JButton("...");
        this.anfahrtInfoPane = new JScrollPane(this.anfahrtInfo);
        this.anfahrtInfoPane.setVerticalScrollBarPolicy(22);
        this.stra\u00dfenInfoPane = new JScrollPane(this.stra\u00dfeninfo);
        this.stra\u00dfenInfoPane.setVerticalScrollBarPolicy(22);
        this.chooser = new JFileChooser();
    }

    protected void setzeAuswahllisten() {
        TabelleStrassen dbsta\u00dfen = new TabelleStrassen();
        String[] strassenListe = null;
        this.strassendb = null;
        try {
            strassenListe = Utils.listToArrayOnlyFORComboBoxes(dbsta\u00dfen.getStra\u00dfenListe());
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        StrassenName = new JComboBox<String>(strassenListe);
        StrassenName.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    Stra\u00dfeEintragenAO.this.strassendb = (String)StrassenName.getSelectedItem();
                    try {
                        TabelleStrassen stra\u00dfendata = new TabelleStrassen();
                        Stra\u00dfeEintragenAO.this.datensatznummer.setText(stra\u00dfendata.getStrassenNumber((String)StrassenName.getSelectedItem()).toString());
                        Stra\u00dfeEintragenAO.this.anfahrtInfo.setText(stra\u00dfendata.getAnfahrtInfo((String)StrassenName.getSelectedItem()));
                        Stra\u00dfeEintragenAO.this.stra\u00dfeninfo.setText(stra\u00dfendata.getStra\u00dfenInfo((String)StrassenName.getSelectedItem()));
                        Stra\u00dfeEintragenAO.this.koordinaten.setText(stra\u00dfendata.getStrassenKoordinaten((String)StrassenName.getSelectedItem()));
                        Stra\u00dfeEintragenAO.this.postleitzzahl.setText(stra\u00dfendata.getPLZ((String)StrassenName.getSelectedItem()));
                        Stra\u00dfeEintragenAO.this.bildStrasse.setText(stra\u00dfendata.getStrassenBild((String)StrassenName.getSelectedItem()));
                        Stra\u00dfeEintragenAO.this.bildStrasse2.setText(stra\u00dfendata.getStrassenBild2((String)StrassenName.getSelectedItem()));
                    }
                    catch (SQLException e1) {
                        logging.logPrintStackTrace((Exception)e1);
                    }
                }
            }
        });
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Stra\u00dfe Eintragen");
        this.setSize(650, 440);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
    }

    protected void boxenHinzufuegen() {
        this.panel = new JPanel(new GridLayout(5, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.stra\u00dfenname_label);
        this.panel.add(StrassenName);
        this.panel.add(this.stra\u00dfenname2_label);
        this.panel.add(this.stra\u00dfenName2);
        this.panel.add(this.datensatznummer_label);
        this.panel.add(this.datensatznummer);
        this.panel.add(this.koordinaten_label);
        this.panel.add(this.koordinaten);
        this.panel.add(this.postleitzahl_label);
        this.panel.add(this.postleitzzahl);
        this.panel2 = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panel2);
        this.panel2.add(this.bildStarsse_label);
        this.panel2.add(this.bildStrasse);
        this.add(this.ordnerauswahl1);
        this.panel3 = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panel3);
        this.panel3.add(this.bildStarsse2_label);
        this.panel3.add(this.bildStrasse2);
        this.add(this.ordnerauswahl2);
        this.panel4 = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panel4);
        this.panel4.add(this.stra\u00dfeninfo_label);
        this.panel4.add(this.stra\u00dfenInfoPane);
        this.panel4.add(this.anfahrtInfo_label);
        this.panel4.add(this.anfahrtInfoPane);
        this.add(this.buttonAktualisieren);
        this.add(this.buttonSpeichern);
        this.add(this.buttonZurueck);
        this.datensatznummer.setEditable(false);
        this.buttonAktualisieren.setVisible(false);
        this.stra\u00dfenname_label.setVisible(false);
        StrassenName.setVisible(false);
        if (MyEvent.event.equals("0x0101")) {
            this.buttonSpeichern.setVisible(false);
            this.buttonAktualisieren.setVisible(true);
            this.stra\u00dfenname_label.setVisible(true);
            StrassenName.setVisible(true);
            this.stra\u00dfenname2_label.setVisible(false);
            this.stra\u00dfenName2.setVisible(false);
        } else {
            TabelleStrassen tabStr = new TabelleStrassen();
            try {
                this.datensatznummer.setText(Integer.toString(tabStr.getNextNummer()));
            }
            catch (SQLException e) {
                logging.logPrintStackTrace((Exception)e);
            }
        }
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.ordnerauswahl1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnVal = Stra\u00dfeEintragenAO.this.chooser.showOpenDialog(Stra\u00dfeEintragenAO.this.chooser);
                if (returnVal == 0) {
                    logging.logInfo((Object)("Ausgew\u00e4hlte Datei: " + Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getPath()));
                }
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/gro\u00df/" + Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getName();
                Utils.kopiereDateiInDataOrdner(Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getAbsoluteFile(), dateiname, String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/gro\u00df");
                Stra\u00dfeEintragenAO.this.bildStrasse.setText(String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/gro\u00df/" + Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getName());
            }
        });
        this.ordnerauswahl2.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnVal = Stra\u00dfeEintragenAO.this.chooser.showOpenDialog(Stra\u00dfeEintragenAO.this.chooser);
                if (returnVal == 0) {
                    logging.logInfo((Object)("Ausgew\u00e4hlte Datei: " + Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getPath()));
                }
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/klein/" + Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getName();
                Utils.kopiereDateiInDataOrdner(Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getAbsoluteFile(), dateiname, String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/klein");
                Stra\u00dfeEintragenAO.this.bildStrasse2.setText(String.valueOf(runApplication.arbeitsverzeichnis) + "data/KarteBilder/klein/" + Stra\u00dfeEintragenAO.this.chooser.getSelectedFile().getName());
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleStrassen tabelleStrassen = new TabelleStrassen();
                Stra\u00dfe stra\u00dfe = new Stra\u00dfe();
                try {
                    stra\u00dfe.setId(Integer.parseInt(Stra\u00dfeEintragenAO.this.datensatznummer.getText()));
                    stra\u00dfe.setName(Stra\u00dfeEintragenAO.this.stra\u00dfenName2.getText());
                    stra\u00dfe.setAnfahrt(Stra\u00dfeEintragenAO.this.anfahrtInfo.getText());
                    stra\u00dfe.setInfo(Stra\u00dfeEintragenAO.this.stra\u00dfeninfo.getText());
                    stra\u00dfe.setKoordinaten(Stra\u00dfeEintragenAO.this.koordinaten.getText());
                    stra\u00dfe.setPLZ(Stra\u00dfeEintragenAO.this.postleitzzahl.getText());
                    stra\u00dfe.setBild(Utils.removeBackSlashFromString(Stra\u00dfeEintragenAO.this.bildStrasse.getText()));
                    stra\u00dfe.setBild2(Utils.removeBackSlashFromString(Stra\u00dfeEintragenAO.this.bildStrasse2.getText()));
                    tabelleStrassen.insert(stra\u00dfe);
                    logbuchEingabe.NeuerEintag("Stra\u00dfe wurde angelegt: " + Stra\u00dfeEintragenAO.this.stra\u00dfenName2.getText());
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    KarteAO.StrasseSuchen.addItem(Stra\u00dfeEintragenAO.this.stra\u00dfenName2.getText());
                    Stra\u00dfeEintragenAO.this.datensatznummer.setText(Integer.toString(tabelleStrassen.getNextNummer()));
                    Stra\u00dfeEintragenAO.this.stra\u00dfenName2.setText(null);
                    Stra\u00dfeEintragenAO.this.anfahrtInfo.setText(null);
                    Stra\u00dfeEintragenAO.this.stra\u00dfeninfo.setText(null);
                    Stra\u00dfeEintragenAO.this.koordinaten.setText(null);
                    Stra\u00dfeEintragenAO.this.postleitzzahl.setText(null);
                    Stra\u00dfeEintragenAO.this.bildStrasse.setText(null);
                    Stra\u00dfeEintragenAO.this.bildStrasse2.setText(null);
                }
                catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TabelleStrassen tabelleStrassen = new TabelleStrassen();
                Stra\u00dfe stra\u00dfe = new Stra\u00dfe();
                if (StrassenName.getSelectedItem().equals("<bitte w\u00e4hlen>")) {
                    logging.logInfo((Object)"Es wurde keine Stra\u00dfe ausgew\u00e4hlt");
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_STRASSE_WAEHLEN, "Fehlermeldung", 2);
                } else {
                    try {
                        stra\u00dfe.setId(Integer.parseInt(Stra\u00dfeEintragenAO.this.datensatznummer.getText()));
                        stra\u00dfe.setAnfahrt(Stra\u00dfeEintragenAO.this.anfahrtInfo.getText());
                        stra\u00dfe.setInfo(Stra\u00dfeEintragenAO.this.stra\u00dfeninfo.getText());
                        stra\u00dfe.setKoordinaten(Stra\u00dfeEintragenAO.this.koordinaten.getText());
                        stra\u00dfe.setPLZ(Stra\u00dfeEintragenAO.this.postleitzzahl.getText());
                        stra\u00dfe.setBild(Stra\u00dfeEintragenAO.this.bildStrasse.getText());
                        stra\u00dfe.setBild2(Stra\u00dfeEintragenAO.this.bildStrasse2.getText());
                        tabelleStrassen.update(stra\u00dfe);
                        logbuchEingabe.NeuerEintag("Stra\u00dfe wurde aktualisiert: Stra\u00dfenID " + Stra\u00dfeEintragenAO.this.datensatznummer.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
                    catch (SQLException e1) {
                        JOptionPane.showMessageDialog(null, "Beim Speichern ist ein Fehler aufgetreten!", "Fehlermeldung", 0);
                        logging.logPrintStackTrace((Exception)e1);
                    }
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

