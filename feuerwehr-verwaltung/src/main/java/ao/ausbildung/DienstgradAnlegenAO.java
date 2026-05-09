/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.ausbildung;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.TabelleDienstgrad;
import go.Dienstgrad;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
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
import utilities.logbuchEingabe;

public class DienstgradAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonBef\u00f6rderungVerwalten;
    private JTextField beschreibung;
    private JTextField beschreibungLang;
    private JLabel beschreibung_label;
    private JLabel beschreibungLang_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelKategorie;
    public static String letzterDienstgrad;

    public DienstgradAnlegenAO() {
        super("FeuerwehrManagementSystem - Dienstgrad anlegen");
        logging.logInfo((Object)"Starte: DienstgradAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonBef\u00f6rderungVerwalten = new JButton("Bef\u00f6rderung Verwalten");
        this.modulBeschreibung = new JLabel("Dienstgrad anlegen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.beschreibung = new JTextField(20);
        this.beschreibungLang = new JTextField(20);
        this.beschreibung_label = new JLabel("Dienstgrad (Abk\u00fcrzung): ");
        this.beschreibungLang_label = new JLabel("Dienstgrad: ");
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
        this.setSize(500, 220);
        this.setTitle("FeuerwehrManagementSystem - Dienstgrad anlegen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonBef\u00f6rderungVerwalten);
        this.panelKategorie = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panelKategorie);
        this.panelKategorie.add(this.beschreibung_label);
        this.panelKategorie.add(this.beschreibung);
        this.panelKategorie.add(this.beschreibungLang_label);
        this.panelKategorie.add(this.beschreibungLang);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonBef\u00f6rderungVerwalten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.BEF\u00d6RDERUNG_KONFIGURIEREN);
                Steuerung.steuerung();
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
                Dienstgrad dienstgrad = new Dienstgrad();
                try {
                    if (tabDienstgrad.getCountBeschreibung(DienstgradAnlegenAO.this.beschreibung.getText()) != 0 || tabDienstgrad.getCountBeschreibungLang(DienstgradAnlegenAO.this.beschreibungLang.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.DIENSTGRAD_SCHON_VORHANDEN, "Warnung", 2);
                    } else {
                        dienstgrad.setId(tabDienstgrad.getNextNummer());
                        dienstgrad.setBeschreibung(DienstgradAnlegenAO.this.beschreibung.getText());
                        dienstgrad.setBeschreibungLang(DienstgradAnlegenAO.this.beschreibungLang.getText());
                        tabDienstgrad.insert(dienstgrad);
                        letzterDienstgrad = DienstgradAnlegenAO.this.beschreibungLang.getText();
                        DienstgradAnlegenAO.this.beschreibung.setText(null);
                        DienstgradAnlegenAO.this.beschreibungLang.setText(null);
                        logging.logInfo((Object)("Dienstgrad wurde angelegt: " + letzterDienstgrad));
                        logbuchEingabe.NeuerEintag("Dienstgrad wurde angelegt: " + letzterDienstgrad);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0029")) {
                            MitgliederAnlegenAO.dienstgrad.addItem(letzterDienstgrad);
                            DienstgradAnlegenAO.this.dispose();
                        }
                    }
                }
                catch (SQLException e) {
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

