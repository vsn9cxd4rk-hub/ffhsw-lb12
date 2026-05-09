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
package ao.bestandsliste;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.bestandsliste.TabelleLager;
import data.tabellen.bestandsliste.TabelleLager_artikel;
import data.tabellen.bestandsliste.TabelleLager_zugewiesen;
import data.tabellen.mitglied.TabelleMitglied;
import java.awt.Desktop;
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
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.bestandsliste.BestandslistePDFSchreiben;
import run.runApplication;
import service.BerechtigunsManager;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;

public class BestandslisteAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonArtikel;
    private JButton buttonArtikelBeartbeiten;
    private JButton buttonArtikelZuweisen;
    private JButton buttonLagerAnlegen;
    private JButton buttonVirtuellesLagerLeeren;
    private JButton buttonDrucken;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JTextArea textfield;
    private JScrollPane scrollTextarea;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;

    public BestandslisteAO() {
        super("FeuerwehrManagementSystem - Bestandsliste");
        logging.logInfo((Object)"Starte: BestandslisteAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonArtikel = new JButton("Artikel anlegen");
        this.buttonArtikelBeartbeiten = new JButton("Artikel bearbeiten");
        this.buttonArtikelZuweisen = new JButton("Artikel Zuweisen & Entfernen");
        this.buttonLagerAnlegen = new JButton("Neues Lager anlegen");
        this.buttonVirtuellesLagerLeeren = new JButton("Virtuelles Lager leeren");
        this.buttonDrucken = new JButton("Drucken");
        tree = new JTree(CreateTrees.CreateBestandslisteTree());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        this.textfield = new JTextArea();
        this.scrollTextarea = new JScrollPane(this.textfield);
        this.scrollTextarea.setVerticalScrollBarPolicy(22);
        this.modulBeschreibung = new JLabel("Bestandsliste");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
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
        this.setTitle("FeuerwehrManagementSystem - Bestandsliste");
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.buttonLagerAnlegen);
        this.add(this.buttonArtikel);
        this.add(this.buttonArtikelBeartbeiten);
        this.add(this.buttonArtikelZuweisen);
        this.add(this.dummy2);
        this.scrollPaneTree.setPreferredSize(new Dimension(350, 500));
        this.add(this.scrollPaneTree);
        this.scrollTextarea.setPreferredSize(new Dimension(600, 500));
        this.add(this.scrollTextarea);
        this.add(this.dummy3);
        this.add(this.buttonZurueck);
        this.add(this.buttonDrucken);
        this.add(this.buttonVirtuellesLagerLeeren);
        if (BerechtigunsManager.ber[57] == 0) {
            this.buttonArtikel.setEnabled(false);
            this.buttonArtikelBeartbeiten.setEnabled(false);
            this.buttonArtikelZuweisen.setEnabled(false);
            this.buttonLagerAnlegen.setEnabled(false);
        }
        if (BerechtigunsManager.ber[58] == 0) {
            this.buttonArtikel.setEnabled(false);
            this.buttonArtikelBeartbeiten.setEnabled(false);
        }
        if (BerechtigunsManager.ber[65] == 0) {
            this.buttonVirtuellesLagerLeeren.setEnabled(false);
        }
    }

    protected void boxenHinzufuegen() {
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                TabelleLager tabLager = new TabelleLager();
                TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                try {
                    BestandslisteAO.this.textfield.setText(null);
                    BestandslisteAO.this.textfield.setText("Liste f\u00fcr: " + tree.getSelectionPath().getLastPathComponent().toString() + "\n\n");
                    if (tree.getSelectionPath().getParentPath().toString().startsWith("[Inhalt, A")) {
                        int artID = tabArtikel.getArtikelID(tree.getSelectionPath().getLastPathComponent().toString());
                        BestandslisteAO.this.textfield.append("Artikelname:\n" + tabArtikel.getArtikelName(artID) + "\n\nArtikelbeschreibung:\n" + tabArtikel.getArtikelBeschreibung(artID) + "\n\nArtikelwert:\n" + tabArtikel.getArtikelWert(artID) + "\n\nEAN-Code:\n" + tabArtikel.getArtikelEAN(artID));
                    } else if (tree.getSelectionPath().getParentPath().toString().startsWith("[Inhalt, L")) {
                        String[] items = Utils.listToArray(tabLager.getBestandsliste("L", tabLager.getLagerID(tree.getSelectionPath().getLastPathComponent().toString())));
                        int i = 0;
                        while (i < items.length) {
                            BestandslisteAO.this.textfield.append(items[i]);
                            BestandslisteAO.this.textfield.append("\n");
                            ++i;
                        }
                    } else if (tree.getSelectionPath().getParentPath().toString().startsWith("[Inhalt, F")) {
                        String[] items = Utils.listToArray(tabLager.getBestandsliste("F", tabFahrzeug.getFahrzeugID(tree.getSelectionPath().getLastPathComponent().toString())));
                        int i = 0;
                        while (i < items.length) {
                            BestandslisteAO.this.textfield.append(items[i]);
                            BestandslisteAO.this.textfield.append("\n");
                            ++i;
                        }
                    } else if (tree.getSelectionPath().getParentPath().toString().startsWith("[Inhalt, M")) {
                        String[] items = Utils.listToArray(tabLager.getBestandsliste("M", tabMitglied.getIdByGuiString(tree.getSelectionPath().getLastPathComponent().toString())));
                        int i = 0;
                        while (i < items.length) {
                            BestandslisteAO.this.textfield.append(items[i]);
                            BestandslisteAO.this.textfield.append("\n");
                            ++i;
                        }
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NullPointerException e) {
                    BestandslisteAO.this.textfield.setText(null);
                }
            }
        });
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                String dateiname = null;
                String name = tree.getSelectionPath().getLastPathComponent().toString();
                String typ = null;
                try {
                    if (tree.getSelectionPath().getParentPath().toString().startsWith("[Inhalt, L")) {
                        typ = "L";
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bestandsliste/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Bestandsliste_" + name + ".pdf";
                    } else if (tree.getSelectionPath().getParentPath().toString().startsWith("[Inhalt, F")) {
                        typ = "F";
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bestandsliste/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Bestandsliste_" + name + ".pdf";
                    } else if (tree.getSelectionPath().getParentPath().toString().startsWith("[Inhalt, M")) {
                        TabelleMitglied tabMitglied = new TabelleMitglied();
                        int mID = tabMitglied.getIdByGuiString(name);
                        typ = "M";
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Bestandsliste/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Bestandsliste_" + tabMitglied.getName(mID) + "." + tabMitglied.getVorname(mID) + ".pdf";
                    }
                    Utils.dateiKatalogisieren(dateiname);
                    BestandslistePDFSchreiben.PDFdocumentErstellen(dateiname, name, typ);
                    Desktop.getDesktop().open(new File(dateiname));
                }
                catch (DocumentException | IOException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonVirtuellesLagerLeeren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
                try {
                    tabZugewiesen.deleteVituellenLagerinhalt();
                    BestandslisteAO.this.textfield.setText(null);
                    logbuchEingabe.NeuerEintag("Das Virtuelle Lager wurde geleert");
                    JOptionPane.showMessageDialog(null, Konstante.LOESCHEN_ERFOLGREICH);
                }
                catch (SQLException e) {
                    logging.logInfo((Object)e);
                }
            }
        });
        this.buttonLagerAnlegen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.NEUES_LAGER);
                Steuerung.steuerung();
            }
        });
        this.buttonArtikelZuweisen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ARTIKEL_ZUWEISEN);
                Steuerung.steuerung();
            }
        });
        this.buttonArtikel.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.ARTIKEL_EINTRAGEN);
                Steuerung.steuerung();
            }
        });
        this.buttonArtikelBeartbeiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                MyEvent.setEvent((String)"0x0201");
                Steuerung.setStatus(Status.ARTIKEL_EINTRAGEN);
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

