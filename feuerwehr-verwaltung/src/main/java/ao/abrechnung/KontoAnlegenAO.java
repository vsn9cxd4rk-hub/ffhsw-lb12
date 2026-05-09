/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.abrechnung;

import ao.AbstractFenster;
import ao.abrechnung.AbrechnungAO;
import ao.abrechnung.ArtikelAbrechnungEintragenAO;
import data.tabellen.abrechnung.TabelleAbrechnung_konto;
import go.abrechnung.AbrechnungKonto;
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
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.logbuchEingabe;

public class KontoAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField name;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelKategorie;
    public static String letzteKategorie;

    public KontoAnlegenAO() {
        super("FeuerwehrManagementSystem - Konto anlegen");
        logging.logInfo((Object)"Starte: KontoAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Konto anlegen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.name = new JTextField(20);
        this.beschreibung = new JLabel("Konto Name: ");
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
        this.setSize(500, 170);
        this.setTitle("FeuerwehrManagementSystem - Konto anlegen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelKategorie = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panelKategorie);
        this.panelKategorie.add(this.beschreibung);
        this.panelKategorie.add(this.name);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleAbrechnung_konto tabKonto = new TabelleAbrechnung_konto();
                AbrechnungKonto konto = new AbrechnungKonto();
                try {
                    if (tabKonto.getCount(KontoAnlegenAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.ARTIKELKLASSE_SCHON_VORHANDEN, "Warnung", 2);
                    } else {
                        konto.setId(tabKonto.getNextNummer());
                        konto.setName(KontoAnlegenAO.this.name.getText());
                        tabKonto.insert(konto);
                        letzteKategorie = KontoAnlegenAO.this.name.getText();
                        logbuchEingabe.NeuerEintag("Artikelklasse wurde angelegt: " + letzteKategorie);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (MyEvent.event.equals("0x0301")) {
                            ArtikelAbrechnungEintragenAO.buchungskonto.addItem(KontoAnlegenAO.this.name.getText());
                            MyEvent.setEvent((String)"0");
                        }
                        logbuchEingabe.NeuerEintag("Neues Konto wurde erstellt: " + KontoAnlegenAO.this.name.getText());
                        AbrechnungAO.tree.setModel(CreateTrees.CreateTreeAbrechnung());
                        KontoAnlegenAO.this.dispose();
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

