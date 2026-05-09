/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.einsatz;

import ao.AbstractFenster;
import data.tabellen.TabelleEinsatz;
import data.tabellen.TabelleVeranstaltung;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;

public class VerdienstausfallAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    public static JButton buttonErstellen;
    private JButton buttonDrucken;
    private JButton buttonAlleDrucken;
    private JButton buttonAnsehen;
    private JButton buttonSendenEMail;
    public static JComboBox<String> veranstaltung;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    public static String aktuellerOrdner;
    private JPanel panelKategorie;
    public static JList liste;
    private JScrollPane pane_liste;

    public VerdienstausfallAO() {
        super("FeuerwehrManagementSystem - Verdienstausfall");
        logging.logInfo((Object)"Starte: VerdienstausfallAO");
    }

    protected void buttonErstellen() {
        buttonErstellen = new JButton("Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonDrucken = new JButton("Drucken");
        this.buttonAlleDrucken = new JButton("Alle Drucken");
        this.buttonAnsehen = new JButton("\u00d6ffnen");
        this.buttonSendenEMail = new JButton("Als E-Mail senden");
        this.modulBeschreibung = new JLabel("Verdienstausfallbescheinigung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.beschreibung = new JLabel("Veranstaltung / Einsatz: ");
        liste = new JList();
        liste.setVisibleRowCount(15);
        liste.setToolTipText("Liste der verf\u00fcgbaren Berichte");
        this.pane_liste = new JScrollPane(liste);
        this.pane_liste.setVerticalScrollBarPolicy(22);
        this.pane_liste.setPreferredSize(new Dimension(700, 200));
        TabelleVeranstaltung tabVeransatltung = new TabelleVeranstaltung();
        try {
            String[] vListe = Utils.listToArrayOnlyFORComboBoxes(tabVeransatltung.getAllVeranstaltungEinerKategorieByJahr(1, Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"))));
            veranstaltung = new JComboBox<String>(vListe);
        }
        catch (NumberFormatException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelErstellen() {
        veranstaltung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleEinsatz tabEinsatz = new TabelleEinsatz();
                try {
                    int vID = tabVeranstaltung.getVeranstaltungID(veranstaltung.getSelectedItem().toString());
                    int eID = tabEinsatz.getEinsatzIDByVeranstaltungID(vID);
                    File file = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/verdienstausfall/Einsatz_ID_" + eID);
                    if (file.exists()) {
                        File[] dateilisteF = file.listFiles();
                        String[] fileName = new String[dateilisteF.length];
                        int i = 0;
                        while (i < dateilisteF.length) {
                            fileName[i] = dateilisteF[i].getName();
                            ++i;
                        }
                        aktuellerOrdner = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/verdienstausfall/Einsatz_ID_" + eID + "/";
                        liste.setListData(fileName);
                        buttonErstellen.setEnabled(false);
                    } else {
                        liste.setListData(new String[0]);
                        buttonErstellen.setEnabled(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
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
        this.setSize(750, 380);
        this.setTitle("FeuerwehrManagementSystem - Verdienstausfall");
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
        this.panelKategorie.add(veranstaltung);
        this.add(this.pane_liste);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(buttonErstellen);
        this.add(this.buttonAnsehen);
        this.add(this.buttonDrucken);
        this.add(this.buttonAlleDrucken);
        try {
            if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1) {
                this.add(this.buttonSendenEMail);
            }
        }
        catch (NumberFormatException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        liste.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    VerdienstausfallAO.this.buttonAnsehen.doClick();
                }
            }
        });
        this.buttonSendenEMail.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    runApplication.mailData.setStatus(1);
                    runApplication.mailData.setAnhang(String.valueOf(aktuellerOrdner) + liste.getSelectedValue().toString() + ",");
                    Steuerung.setStatus(Status.NEUE_EMAIL);
                    Steuerung.steuerung();
                }
                catch (NullPointerException e) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_EINTRAG_WAEHLEN, "Warnung", 2);
                }
            }
        });
        buttonErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (veranstaltung.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Warnung", 2);
                } else {
                    Steuerung.setStatus(Status.VERDIENSTAUSFALL_ZEITENANPASSEN);
                    Steuerung.steuerung();
                }
            }
        });
        this.buttonAnsehen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String dateiname = String.valueOf(aktuellerOrdner) + liste.getSelectedValue().toString();
                    logging.logInfo((Object)("\u00d6ffne: " + dateiname));
                    Desktop.getDesktop().open(new File(dateiname));
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_OEFFNEN, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAlleDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    File ordner = new File(aktuellerOrdner);
                    File[] files = ordner.listFiles();
                    int i = 0;
                    while (i < files.length) {
                        File ausdruckfile = new File(String.valueOf(aktuellerOrdner) + files[i].getName());
                        logging.logInfo((Object)("Drucke: " + ausdruckfile));
                        Desktop dt = Desktop.getDesktop();
                        dt.print(ausdruckfile);
                        ++i;
                    }
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_DRUCKEN, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    File ausdruckfile = new File(String.valueOf(aktuellerOrdner) + liste.getSelectedValue().toString());
                    logging.logInfo((Object)("Drucke: " + ausdruckfile));
                    Desktop dt = Desktop.getDesktop();
                    dt.print(ausdruckfile);
                }
                catch (IOException e) {
                    JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_DRUCKEN, "Fehlermeldung", 0);
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

