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
import com.itextpdf.text.DocumentException;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.mitgliedakte.PDFMitgliedAusserDienst;
import pdfdocumente.mitgliedakte.PDFMitgliedInDienst;
import run.runApplication;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class MitgliedAusserDienstAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JComboBox<String> mitgliederListe;
    private JLabel beschreibung;
    private JCheckBox ausserDienst;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;

    public MitgliedAusserDienstAO() {
        super("FeuerwehrManagementSystem - Mitglieder Au\u00dfer Dienst stellen");
        logging.logInfo((Object)"Starte: MitgliedAusserDienstAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.ausserDienst = new JCheckBox("Ausser Dienst: ");
        this.modulBeschreibung = new JLabel("Mitglied Ausser Dienst stellen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        String[] liste = null;
        TabelleMitglied mitglied = new TabelleMitglied();
        try {
            liste = Utils.listToArrayOnlyFORComboBoxes(mitglied.getAllMitgliederFromDataBase());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.mitgliederListe = new JComboBox<String>(liste);
        this.beschreibung = new JLabel("Name: ");
        this.mitgliederListe.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    int mID = tabMitglied.getIdByGuiString(MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
                    if (tabMitglied.getAusserDienstStatus(mID) == 0) {
                        MitgliedAusserDienstAO.this.ausserDienst.setSelected(false);
                        MitgliedAusserDienstAO.this.ausserDienst.setEnabled(true);
                    } else if (tabMitglied.getAusserDienstStatus(mID) == 1) {
                        MitgliedAusserDienstAO.this.ausserDienst.setSelected(true);
                        MitgliedAusserDienstAO.this.ausserDienst.setEnabled(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
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
        this.setSize(500, 180);
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Au\u00dfer Dienst stellen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.beschreibung);
        this.panel.add(this.mitgliederListe);
        this.add(this.ausserDienst);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.ausserDienst.setEnabled(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    int mID = tabMitglied.getIdByGuiString(MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
                    if (!MitgliedAusserDienstAO.this.ausserDienst.isSelected()) {
                        tabMitglied.updateAusserDienst(mID, 0);
                        PDFMitgliedInDienst.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_InDienstGestellt.pdf", MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
                        logging.logInfo((Object)("Mitglied: " + MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString() + " wurde Ausser Dienst gestellt"));
                    } else if (MitgliedAusserDienstAO.this.ausserDienst.isSelected()) {
                        tabMitglied.updateAusserDienst(mID, 1);
                        PDFMitgliedAusserDienst.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Au\u00dferDienstGestellt.pdf", MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
                        logging.logInfo((Object)("Mitglied: " + MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString() + " wurde in Dienst gestellt"));
                    }
                    logbuchEingabe.NeuerEintag("Mitglied Au\u00dfer Dienst Status ge\u00e4ndert zu: " + Integer.toString(MitgliedAusserDienstAO.this.ausserDienst.isSelected() ? 1 : 0) + " " + MitgliedAusserDienstAO.this.mitgliederListe.getSelectedItem().toString());
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                }
                catch (DocumentException | IOException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
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

