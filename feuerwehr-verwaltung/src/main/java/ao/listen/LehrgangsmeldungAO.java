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
import ao.utils.ProzessBarAO;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleBef\u00f6rderungKonfig;
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleEhrungenKonfig;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.TabelleLehrgangsmeldung;
import data.tabellen.einstellungen.TabelleJahr;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import go.Lehrgangsmeldung;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.LehrgangsmeldungPDFSchreiben;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.logbuchEingabe;

public class LehrgangsmeldungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonErstellen;
    private JButton buttonBef\u00f6rderungKonfig;
    private JButton buttonLehrgangKonfig;
    private JButton buttonEhrungKonfig;
    private JTextArea hinweis;
    private JLabel beschreibung;
    private JComboBox<String> jahr;
    public static JCheckBox lehrgang_checkbox;
    public static JCheckBox bef\u00f6rderung_checkbox;
    public static JCheckBox ehrung_checkbox;
    private JLabel lehrgang_checkbox_label;
    private JLabel bef\u00f6rderung_checkbox_label;
    private JLabel ehrung_checkbox_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;

    public LehrgangsmeldungAO() {
        super("FeuerwehrManagementSystem - Lehrgangsmeldung");
        logging.logInfo((Object)"Starte: LehrgangsmeldungAO");
    }

    protected void buttonErstellen() {
        this.buttonErstellen = new JButton("Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonBef\u00f6rderungKonfig = new JButton("Bef\u00f6rderungskonfiguration");
        this.buttonLehrgangKonfig = new JButton("Lehrgangskonfiguration");
        this.buttonEhrungKonfig = new JButton("Ehrungenkonfiguration");
        bef\u00f6rderung_checkbox = new JCheckBox();
        ehrung_checkbox = new JCheckBox();
        lehrgang_checkbox = new JCheckBox();
        this.lehrgang_checkbox_label = new JLabel("Lehrg\u00e4nge: ");
        this.bef\u00f6rderung_checkbox_label = new JLabel("Bef\u00f6rderungen: ");
        this.ehrung_checkbox_label = new JLabel("Ehrungen: ");
        this.hinweis = new JTextArea(2, 40);
        this.hinweis.setText("HINWEIS:\nDer Vorschlag f\u00fcr Bef\u00f6rderungen st\u00fctzt sich ausschlie\u00dflich auf die\nKonfiguration und auf die eingetragenen Lehrg\u00e4nge.\nEine weitere Rolle zur verfeinerung der Suche ist die Laufbahn, hier\nwerden die Zeiten f\u00fcr den Zeitraum entnommen. Um so genauer hier die\nDatenbank geplegt wird um so genauer sind die Vorschlagsergebnisse.");
        this.hinweis.setLineWrap(true);
        this.hinweis.setEditable(false);
        this.modulBeschreibung = new JLabel("Lehrgangsmeldung / Bef\u00f6rderungen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.beschreibung = new JLabel("Jahr: ");
        TabelleJahr tabJahre = new TabelleJahr();
        try {
            String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(tabJahre.getAllVerf\u00fcgbarenJahre());
            this.jahr = new JComboBox<String>(jahresListe);
            this.jahr.addItem(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1));
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
        this.setSize(600, 380);
        this.setTitle("FeuerwehrManagementSystem - Lehrgangsmeldung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.hinweis);
        this.add(this.buttonBef\u00f6rderungKonfig);
        this.add(this.buttonLehrgangKonfig);
        this.add(this.buttonEhrungKonfig);
        this.panel = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.lehrgang_checkbox_label);
        this.panel.add(lehrgang_checkbox);
        this.panel.add(this.bef\u00f6rderung_checkbox_label);
        this.panel.add(bef\u00f6rderung_checkbox);
        this.panel.add(this.ehrung_checkbox_label);
        this.panel.add(ehrung_checkbox);
        this.panel.add(this.beschreibung);
        this.panel.add(this.jahr);
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
                if (!(bef\u00f6rderung_checkbox.isSelected() || lehrgang_checkbox.isSelected() || ehrung_checkbox.isSelected())) {
                    JOptionPane.showMessageDialog(null, Konstante.BEF\u00d6RDERUNG_ODER_LEHRGANG, "Warnung", 2);
                } else if (bef\u00f6rderung_checkbox.isSelected() && new TabelleBef\u00f6rderungKonfig().getCount() == 0) {
                    JOptionPane.showMessageDialog(null, Konstante.KEINE_KONFIGURATION_BEF\u00d6RDERUNG, "Warnung", 2);
                } else if (LehrgangsmeldungAO.this.jahr.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
                } else {
                    Thread threadBerechnen = new Thread(){

                        // FIXME: Dekompiler konnte Kontrollfluss (GOTO) nicht vollständig rekonstruieren.
                        // Originale Logik für Beförderungs- und Lehrgangsmeldungsberechnung hier einfügen.
                        @Override
                        public void run() {
                            throw new UnsupportedOperationException(
                                "run() konnte vom Dekompiler nicht vollständig rekonstruiert werden. " +
                                "Bitte Original-Quellcode einpflegen."
                            );
                        }

                    };
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    threadBerechnen.start();
                }
            }

            // FIXME: Synthetische Zugriffsmethode konnte nicht rekonstruiert werden (anonyme Klasse #1).
        });
        this.buttonEhrungKonfig.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Steuerung.setStatus(Status.EHRUNGEN_KONFIGURATION);
                Steuerung.steuerung();
            }
        });
        this.buttonBef\u00f6rderungKonfig.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BEF\u00d6RDERUNG_KONFIGURIEREN);
                Steuerung.steuerung();
            }
        });
        this.buttonLehrgangKonfig.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.LEHRGANG_KONFIGURIEREN);
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

