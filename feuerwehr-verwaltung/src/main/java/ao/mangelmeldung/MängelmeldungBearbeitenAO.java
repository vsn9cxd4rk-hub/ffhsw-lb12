/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.mangelmeldung;

import ao.AbstractFenster;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleMaengelmeldung_kommentar;
import data.tabellen.einstellungen.TabelleMandant;
import go.M\u00e4ngelmeldung_kommentar;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class M\u00e4ngelmeldungBearbeitenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonKommentarEintragen;
    private JButton buttonAnsehen;
    private JButton buttonWiederoeffnung;
    public static JComboBox<String> mandant;
    private JLabel mandant_label;
    public static JTextArea liste;
    private JScrollPane pane_liste;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;

    public M\u00e4ngelmeldungBearbeitenAO() {
        super("FeuerwehrManagementSystem - M\u00e4ngelmeldung bearbeiten");
        logging.logInfo((Object)"Starte: M\u00e4ngelmeldungBearbeiten AO");
    }

    protected void buttonErstellen() {
        this.buttonKommentarEintragen = new JButton("Kommentar eintragen");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonAnsehen = new JButton("Ansehen");
        this.buttonWiederoeffnung = new JButton("Wiederer\u00f6ffnung");
        this.mandant_label = new JLabel("Mandant: ");
        this.modulBeschreibung = new JLabel("M\u00e4ngelmeldung bearbeiten");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        tree = new JTree(CreateTrees.CreateTreeMaengelListe(runApplication.PROPERTIES.get("MandantID")));
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        liste = new JTextArea();
        this.pane_liste = new JScrollPane(liste);
        this.pane_liste.setVerticalScrollBarPolicy(22);
        this.pane_liste.setPreferredSize(new Dimension(600, 200));
    }

    protected void labelErstellen() {
        try {
            TabelleMandant tabMandant = new TabelleMandant();
            String[] mandantListe = Utils.listToArray(tabMandant.getAllMandanten());
            mandant = new JComboBox<String>(mandantListe);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        mandant.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleMandant tabMandant = new TabelleMandant();
                    tree.setModel(CreateTrees.CreateTreeMaengelListe(Integer.toString(tabMandant.getMandantID(mandant.getSelectedItem().toString()))));
                    liste.setText(null);
                    if (mandant.getSelectedItem().toString().equals(runApplication.mandantName)) {
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonAnsehen.setVisible(true);
                    } else {
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonAnsehen.setVisible(false);
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
        if (runApplication.BF == 1) {
            this.setSize(1000, 630);
        } else {
            this.setSize(1000, 600);
        }
        this.setTitle("FeuerwehrManagementSystem - M\u00e4ngelmeldung bearbeiten");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        if (runApplication.BF == 1) {
            this.add(this.mandant_label);
            this.add(mandant);
            this.add(this.dummy3);
        }
        this.scrollPaneTree.setPreferredSize(new Dimension(300, 450));
        this.add(this.scrollPaneTree);
        this.pane_liste.setPreferredSize(new Dimension(600, 450));
        this.add(this.pane_liste);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonAnsehen);
        this.add(this.buttonWiederoeffnung);
        this.add(this.buttonKommentarEintragen);
        this.buttonKommentarEintragen.setEnabled(false);
        this.buttonAnsehen.setEnabled(false);
        this.buttonWiederoeffnung.setVisible(false);
        mandant.setSelectedItem(runApplication.mandantName);
        liste.setWrapStyleWord(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                try {
                    Object[] auswahl = tree.getSelectionPath().getPath();
                    TabelleMaengelmeldung_kommentar tabMangelKommentar = new TabelleMaengelmeldung_kommentar();
                    TabelleMandant tabMandant = new TabelleMandant();
                    int maID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(9, tree.getSelectionPath().getLastPathComponent().toString().length()));
                    int mandantID = tabMandant.getMandantID(mandant.getSelectedItem().toString());
                    liste.setText(tabMangelKommentar.getKommentarListe(maID, mandantID));
                    if (auswahl[2].toString().equals("Offene M\u00e4ngelmeldungen")) {
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonKommentarEintragen.setEnabled(true);
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonAnsehen.setEnabled(true);
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonWiederoeffnung.setVisible(false);
                    } else {
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonKommentarEintragen.setEnabled(false);
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonAnsehen.setEnabled(true);
                        M\u00e4ngelmeldungBearbeitenAO.this.buttonWiederoeffnung.setVisible(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NumberFormatException e) {
                }
                catch (StringIndexOutOfBoundsException e) {
                }
                catch (NullPointerException e) {
                    liste.setText(null);
                }
            }
        });
        this.buttonWiederoeffnung.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
                TabelleMaengelmeldung_kommentar tabKommentar = new TabelleMaengelmeldung_kommentar();
                M\u00e4ngelmeldung_kommentar kommentarObjekt = new M\u00e4ngelmeldung_kommentar();
                TabelleMandant tabMandant = new TabelleMandant();
                try {
                    int maID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(9, tree.getSelectionPath().getLastPathComponent().toString().length()));
                    int mandantID = tabMandant.getMandantID(mandant.getSelectedItem().toString());
                    tabMangel.updateStatus(maID, 0);
                    kommentarObjekt.setMangelID(maID);
                    kommentarObjekt.setKommentarID(tabKommentar.getNextKommentarNummer(maID, mandantID));
                    kommentarObjekt.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    kommentarObjekt.setZeit(SbcUtils.timeStamp((String)"HH:mm:ss"));
                    kommentarObjekt.setKommentar("Wieder\u00f6ffnung der M\u00e4ngelmeldung!\nDer Mangel muss nocheinmal bearbeitet werden.");
                    kommentarObjekt.setUser(runApplication.loginName);
                    kommentarObjekt.setMandantID(mandantID);
                    tabKommentar.insert(kommentarObjekt);
                    liste.setText(tabKommentar.getKommentarListe(maID, mandantID));
                    tree.setModel(CreateTrees.CreateTreeMaengelListe(Integer.toString(mandantID)));
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    logbuchEingabe.NeuerEintag("M\u00e4ngelmeldung: " + tree.getSelectionPath().getLastPathComponent().toString() + " wurde wieder ge\u00f6ffnet");
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonKommentarEintragen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.MAENGELMELDUNG_KOMMENTAR);
                Steuerung.steuerung();
            }
        });
        this.buttonAnsehen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleMaengelmeldung tabM\u00e4ngel = new TabelleMaengelmeldung();
                    Object[] auswahl = tree.getSelectionPath().getPath();
                    int maID = Integer.parseInt(tree.getSelectionPath().getLastPathComponent().toString().substring(9, tree.getSelectionPath().getLastPathComponent().toString().length()));
                    String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + auswahl[1] + "/Mangel/" + tabM\u00e4ngel.getDateinameByID(maID);
                    Desktop.getDesktop().open(new File(dateiname));
                }
                catch (IOException | SQLException e) {
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

