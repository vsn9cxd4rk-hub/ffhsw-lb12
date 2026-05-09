/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederakteAO;
import com.itextpdf.text.DocumentException;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitgliederakte_kommentar;
import go.MitgliederAkte_Kommentar;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.mitgliedakte.PDFMitgliederKommentar;
import run.runApplication;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class MitgliederAkteKomentarAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextArea textfiled;
    private JScrollPane scrollPane;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public MitgliederAkteKomentarAO() {
        super("FeuerwehrManagementSystem");
        logging.logInfo((Object)"Starte: MitgliederakteKommentarAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.textfiled = new JTextArea();
        this.scrollPane = new JScrollPane(this.textfiled);
        this.scrollPane.setVerticalScrollBarPolicy(22);
        this.scrollPane.setPreferredSize(new Dimension(450, 300));
        this.modulBeschreibung = new JLabel("Mitgliederakte Kommentar");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
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
        this.setTitle("FeuerwehrManagementSystem - E-Mail Modul");
        this.setSize(500, 450);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.scrollPane);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.textfiled.setCaretPosition(0);
        this.textfiled.setWrapStyleWord(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    int mID = Integer.parseInt(MitgliederakteAO.tree.getSelectionPath().getLastPathComponent().toString().substring(1, 6));
                    String mName = String.valueOf(tabMitglied.getName(mID)) + ", " + tabMitglied.getVorname(mID);
                    String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                    PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, mName, MitgliederAkteKomentarAO.this.textfiled.getText());
                    File ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID);
                    File[] files = ordner.listFiles();
                    String[] fileName = new String[files.length];
                    int i = 0;
                    while (i < files.length) {
                        fileName[i] = files[i].getName();
                        ++i;
                    }
                    MitgliederakteAO.liste.setListData(fileName);
                    logbuchEingabe.NeuerEintag("Kommentar in die Mitgliederakte eingetragen: " + MitgliederakteAO.tree.getSelectionPath().getLastPathComponent().toString() + " Details: " + dateiname);
                    MitgliederAkte_Kommentar kommentar = new MitgliederAkte_Kommentar();
                    kommentar.setId(mID);
                    kommentar.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    kommentar.setZeit(SbcUtils.timeStamp((String)"HH:mm"));
                    kommentar.setKommentar(MitgliederAkteKomentarAO.this.textfiled.getText());
                    new TabelleMitgliederakte_kommentar().insert(kommentar);
                    Utils.dateiKatalogisieren(dateiname);
                    MitgliederAkteKomentarAO.this.dispose();
                }
                catch (DocumentException | IOException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void labelErstellen() {
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}

