/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.Datei
 */
package ao;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleEinsatz_bericht;
import data.tabellen.TabelleJahresbericht;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleFTPSync;
import go.Protokoll;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.ProtokollPDFScheiben;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Datei;
import utilities.Konstante;
import utilities.Utils;

public class DokumenteAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private StringBuilder aktuellerOrdner;
    private JButton buttonZurueck;
    private JButton buttonAnsehen;
    private JButton buttonHochladen;
    private JButton buttonEntfernen;
    private JButton buttonSendenEMail;
    private JButton buttonSpeichernUnter;
    private JList liste;
    private JTextField ordnerLeiste;
    private JScrollPane pane_liste;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    private JFileChooser chooser;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JLabel dummy3;

    public DokumenteAO() {
        super("FeuerwehrManagementSystem - Dokumente");
        logging.logInfo((Object)"Starte: DokumentenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.buttonAnsehen = new JButton("\u00d6ffnen");
        this.buttonHochladen = new JButton("Einf\u00fcgen");
        this.buttonEntfernen = new JButton("L\u00f6schen");
        this.buttonSendenEMail = new JButton("Als E-Mail senden");
        this.buttonSpeichernUnter = new JButton("Speichern unter");
        this.chooser = new JFileChooser();
        this.modulBeschreibung = new JLabel("Liste der Dokumente");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dummy3 = new JLabel(runApplication.dummyImage);
        this.ordnerLeiste = new JTextField("Ordner Liste", 83);
        tree = new JTree(CreateTrees.CreateTreeDokumentenListe());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        tree.setSelectionRow(0);
    }

    protected void setzeAuswahllisten() {
        this.liste = new JList();
        this.liste.setVisibleRowCount(15);
        this.liste.setToolTipText("Liste der verf\u00fcgbaren Berichte");
        this.pane_liste = new JScrollPane(this.liste);
        this.pane_liste.setVerticalScrollBarPolicy(22);
        this.pane_liste.setPreferredSize(new Dimension(600, 200));
    }

    protected void boxenHinzufuegen() {
        if (runApplication.ftpDownloadL\u00e4uft != 0) {
            JOptionPane.showMessageDialog(null, Konstante.FTP_DOWNLOAD_LAEUFT, "Hinweis", 2);
        }
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Dokumente");
        this.setSize(950, 670);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.ordnerLeiste);
        this.scrollPaneTree.setPreferredSize(new Dimension(300, 450));
        this.add(this.scrollPaneTree);
        this.pane_liste.setPreferredSize(new Dimension(600, 450));
        this.add(this.pane_liste);
        this.add(this.dummy3);
        this.add(this.buttonAnsehen);
        this.add(this.buttonHochladen);
        this.add(this.buttonEntfernen);
        this.add(this.buttonSpeichernUnter);
        try {
            if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1) {
                this.add(this.buttonSendenEMail);
            }
        }
        catch (NumberFormatException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.buttonHochladen.setVisible(false);
        this.buttonEntfernen.setVisible(false);
        this.buttonSpeichernUnter.setVisible(false);
        this.ordnerLeiste.setEditable(false);
    }

    protected void labelHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    private void erstelleOrdnerLeiste() {
        StringBuilder build = new StringBuilder();
        this.aktuellerOrdner = new StringBuilder();
        Object[] treeListe = tree.getSelectionPath().getPath();
        build.append("data >> ");
        this.aktuellerOrdner.append("data/");
        int o = 1;
        while (o < treeListe.length) {
            build.append(treeListe[o]);
            if (treeListe.length - 1 != o) {
                build.append(" >> ");
            }
            if (treeListe[o].toString().equals("Briefe")) {
                this.aktuellerOrdner.append("Brief");
            } else if (treeListe[o].toString().equals("M\u00e4ngelmeldungen")) {
                this.aktuellerOrdner.append("Mangel");
            } else if (treeListe[o].toString().equals("Verdienstausfillbescheinigungen")) {
                this.aktuellerOrdner.append("Verdienstausfall");
            } else if (treeListe[o].toString().equals("Beteiligungs\u00fcbersicht")) {
                this.aktuellerOrdner.append("Beteiligung_uebersicht");
            } else {
                this.aktuellerOrdner.append(treeListe[o]);
            }
            this.aktuellerOrdner.append("/");
            ++o;
        }
        this.ordnerLeiste.setText(build.toString());
        logging.logInfo((Object)("Aktueller Ordner: " + runApplication.arbeitsverzeichnis + this.aktuellerOrdner.toString()));
    }

    private String[] prepareFileNameForList(File[] files) {
        String[] fileName = new String[files.length];
        int i = 0;
        while (i < files.length) {
            fileName[i] = files[i].getName();
            ++i;
        }
        return fileName;
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.liste.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DokumenteAO.this.buttonAnsehen.doClick();
                }
            }
        });
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                block48: {
                    Object[] jahresAuswahl = tree.getSelectionPath().getPath();
                    DokumenteAO.this.erstelleOrdnerLeiste();
                    DokumenteAO.this.buttonHochladen.setVisible(false);
                    DokumenteAO.this.buttonEntfernen.setVisible(false);
                    DokumenteAO.this.buttonSpeichernUnter.setVisible(false);
                    if (tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
                        TabelleJahresbericht tabBericht = new TabelleJahresbericht();
                        try {
                            int jahr = Integer.parseInt(jahresAuswahl[1].toString());
                            String[] berichteListe = Utils.listToArray(tabBericht.getAllVerf\u00fcgbarenBerichte(jahr));
                            DokumenteAO.this.liste.setListData(berichteListe);
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Fahrzeugeinteilung")) {
                        try {
                            File ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                            File[] dateilisteF = ordner.listFiles();
                            if (dateilisteF.length != 0) {
                                DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteF));
                                break block48;
                            }
                            DokumenteAO.this.liste.setListData(new String[0]);
                        }
                        catch (NullPointerException e) {
                            JOptionPane.showMessageDialog(null, Konstante.KEIN_DOKUMENT_VORHANDEN);
                        }
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                        TabelleVeranstaltung tabVeransataltung = new TabelleVeranstaltung();
                        try {
                            String[] einsatzBerichteListe = Utils.listToArray(tabVeransataltung.getAllVeranstaltungEinerKategorieByJahr(1, Integer.parseInt(jahresAuswahl[1].toString())));
                            DokumenteAO.this.liste.setListData(einsatzBerichteListe);
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Briefe")) {
                        File ordnerB = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteB = ordnerB.listFiles();
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteB));
                        DokumenteAO.this.buttonEntfernen.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Beteiligungs\u00fcbersicht")) {
                        File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                        if (dateilisteBeteiligung.length != 0) {
                            DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
                        } else {
                            DokumenteAO.this.liste.setListData(new Object[0]);
                        }
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Schichten")) {
                        File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                        if (dateilisteBeteiligung.length != 0) {
                            DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
                        } else {
                            DokumenteAO.this.liste.setListData(new Object[0]);
                        }
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Verdienstausfallbescheinigung")) {
                        Steuerung.setStatus(Status.VERDIENSTAUSFALL);
                        Steuerung.steuerung();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("M\u00e4ngelmeldungen")) {
                        TabelleMaengelmeldung tabM\u00e4ngel = new TabelleMaengelmeldung();
                        try {
                            String[] mangelBerichteListe = Utils.listToArray(tabM\u00e4ngel.getWann(Integer.parseInt(jahresAuswahl[1].toString())));
                            DokumenteAO.this.liste.setListData(mangelBerichteListe);
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                        DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Eigene Dateien")) {
                        File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
                        DokumenteAO.this.buttonHochladen.setVisible(true);
                        DokumenteAO.this.buttonEntfernen.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Ausbildungsunterlagen")) {
                        File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
                        DokumenteAO.this.buttonHochladen.setVisible(true);
                        DokumenteAO.this.buttonEntfernen.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Bestandsliste")) {
                        File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
                        DokumenteAO.this.buttonEntfernen.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Lehrgangsmeldungen")) {
                        File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteLehrgangsmeldungen = ordnerBeteiligung.listFiles();
                        if (dateilisteLehrgangsmeldungen.length != 0) {
                            DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteLehrgangsmeldungen));
                        } else {
                            DokumenteAO.this.liste.setListData(new String[0]);
                        }
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Abrechnung")) {
                        File ordnerAbrechnung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteAbrechnungen = ordnerAbrechnung.listFiles();
                        if (dateilisteAbrechnungen.length != 0) {
                            DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteAbrechnungen));
                        } else {
                            DokumenteAO.this.liste.setListData(new String[0]);
                        }
                        DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                        try {
                            TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                            String[] protokolle = Utils.listToArray(tabProtokoll.getAlleTitel(Integer.parseInt(jahresAuswahl[1].toString())));
                            DokumenteAO.this.liste.setListData(protokolle);
                            DokumenteAO.this.buttonSpeichernUnter.setVisible(true);
                        }
                        catch (SQLException e) {
                            logging.logPrintStackTrace((Exception)e);
                        }
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Atemschutz")) {
                        File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                        File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
                        DokumenteAO.this.buttonHochladen.setVisible(true);
                        DokumenteAO.this.buttonEntfernen.setVisible(true);
                    }
                }
            }
        });
        this.buttonSendenEMail.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleJahresbericht tabBericht = new TabelleJahresbericht();
                TabelleEinsatz_bericht tabEinsatzBericht = new TabelleEinsatz_bericht();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
                TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                try {
                    runApplication.mailData.setStatus(1);
                    if (tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
                        runApplication.mailData.setAnhang(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + tabBericht.getBerichtDateiname(DokumenteAO.this.liste.getSelectedValue().toString()));
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                        runApplication.mailData.setAnhang(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + tabEinsatzBericht.getDateiname(tabVeranstaltung.getVeranstaltungID(DokumenteAO.this.liste.getSelectedValue().toString())) + ",");
                        runApplication.mailData.setAn(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtAN"));
                        runApplication.mailData.setCc(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtCC"));
                        runApplication.mailData.setBcc(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtBCC"));
                        runApplication.mailData.setBetreff("Einsatzbericht: " + DokumenteAO.this.liste.getSelectedValue().toString());
                        runApplication.mailData.seteMailText(String.valueOf(runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzbericht")) + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("M\u00e4ngelmeldungen")) {
                        runApplication.mailData.setAn(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldungAN"));
                        runApplication.mailData.setCc(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldungCC"));
                        runApplication.mailData.setBcc(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldungBCC"));
                        runApplication.mailData.setBetreff("M\u00e4ngelmeldung: " + DokumenteAO.this.liste.getSelectedValue().toString());
                        runApplication.mailData.seteMailText(String.valueOf(runApplication.EINSTELLUNGEN.get("emailTemplateM\u00e4ngelmeldung")) + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                        runApplication.mailData.setAnhang(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + tabMangel.getDateiname(DokumenteAO.this.liste.getSelectedValue().toString()) + ",");
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                        int komma = DokumenteAO.this.liste.getSelectedValue().toString().indexOf(" - ");
                        String isSelectedVeranstaltungName = DokumenteAO.this.liste.getSelectedValue().toString().substring(0, komma);
                        int vID = tabVeranstaltung.getVeranstaltungID(isSelectedVeranstaltungName);
                        Protokoll protokoll = new Protokoll();
                        protokoll = tabProtokoll.getData(vID);
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                        ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
                        Utils.dateiKatalogisieren(dateiname);
                        runApplication.mailData.setAnhang(dateiname);
                    } else {
                        runApplication.mailData.setAnhang(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString() + ",");
                    }
                    Steuerung.setStatus(Status.NEUE_EMAIL);
                    Steuerung.steuerung();
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (NullPointerException e1) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_EINTRAG_WAEHLEN, "Warnung", 2);
                }
                catch (DocumentException e) {
                    logging.logPrintStackTrace((Exception)((Object)e));
                }
                catch (IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSpeichernUnter.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    DokumenteAO.this.chooser = new JFileChooser();
                    DokumenteAO.this.chooser.setFileSelectionMode(1);
                    DokumenteAO.this.chooser.showSaveDialog(null);
                    String input = null;
                    String outout = null;
                    if (tree.getSelectionPath().getLastPathComponent().toString().equals("M\u00e4ngelmeldungen")) {
                        input = new TabelleMaengelmeldung().getDateiname(DokumenteAO.this.liste.getSelectedValue().toString());
                        outout = String.valueOf(DokumenteAO.this.chooser.getSelectedFile().getPath()) + "/" + input;
                        Datei.copyFileAusf\u00fchren((File)new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + input), (String)outout);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                        input = new TabelleEinsatz_bericht().getDateiname(new TabelleVeranstaltung().getVeranstaltungID(DokumenteAO.this.liste.getSelectedValue().toString()));
                        outout = String.valueOf(DokumenteAO.this.chooser.getSelectedFile().getPath()) + "/" + input;
                        Datei.copyFileAusf\u00fchren((File)new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + input), (String)outout);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
                        input = new TabelleJahresbericht().getBerichtDateiname(DokumenteAO.this.liste.getSelectedValue().toString());
                        outout = String.valueOf(DokumenteAO.this.chooser.getSelectedFile().getPath()) + "/" + input;
                        Datei.copyFileAusf\u00fchren((File)new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + input), (String)outout);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Abrechnung")) {
                        input = DokumenteAO.this.liste.getSelectedValue().toString();
                        outout = String.valueOf(DokumenteAO.this.chooser.getSelectedFile().getPath()) + "/" + input;
                        Datei.copyFileAusf\u00fchren((File)new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + input), (String)outout);
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                        TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int komma = DokumenteAO.this.liste.getSelectedValue().toString().indexOf(" - ");
                        String isSelectedVeranstaltungName = DokumenteAO.this.liste.getSelectedValue().toString().substring(0, komma);
                        int vID = tabVeranstaltung.getVeranstaltungID(isSelectedVeranstaltungName);
                        Protokoll protokoll = new Protokoll();
                        protokoll = tabProtokoll.getData(vID);
                        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                        ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
                        Utils.dateiKatalogisieren(dateiname);
                        outout = String.valueOf(DokumenteAO.this.chooser.getSelectedFile().getPath()) + "/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                        Datei.copyFileAusf\u00fchren((File)new File(dateiname), (String)outout);
                    }
                    logging.logInfo((Object)"Datei wurde erfolgreich kopiert");
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, Konstante.DATEI_NICHT_GEFUNDEN, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e1);
                }
                catch (DocumentException e) {
                    logging.logPrintStackTrace((Exception)((Object)e));
                }
            }
        });
        this.buttonAnsehen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleJahresbericht tabBericht = new TabelleJahresbericht();
                Object[] jahresAuswahl = tree.getSelectionPath().getPath();
                String dateiname = null;
                try {
                    if (tree.getSelectionPath().getLastPathComponent().toString().equals("Berichte")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + tabBericht.getBerichtDateiname((String)DokumenteAO.this.liste.getSelectedValue());
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Fahrzeugeinteilung")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Briefe")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Beteiligungs\u00fcbersicht")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Schichten")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Abrechnung")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Einsatzberichte")) {
                        TabelleEinsatz_bericht tabEinsatzBericht = new TabelleEinsatz_bericht();
                        TabelleVeranstaltung tabVeransataltung = new TabelleVeranstaltung();
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahresAuswahl[1].toString() + "/einsatzberichte/" + tabEinsatzBericht.getDateiname(tabVeransataltung.getVeranstaltungID((String)DokumenteAO.this.liste.getSelectedValue()));
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("M\u00e4ngelmeldungen")) {
                        TabelleMaengelmeldung tabM\u00e4ngel = new TabelleMaengelmeldung();
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + tabM\u00e4ngel.getDateiname((String)DokumenteAO.this.liste.getSelectedValue());
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Eigene Dateien")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Atemschutz")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Ausbildungsunterlagen")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Bestandsliste")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Lehrgangsmeldungen")) {
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString();
                    } else if (tree.getSelectionPath().getLastPathComponent().toString().equals("Protokoll")) {
                        TabelleProtokoll tabProtokoll = new TabelleProtokoll();
                        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                        int komma = DokumenteAO.this.liste.getSelectedValue().toString().indexOf(" - ");
                        String isSelectedVeranstaltungName = DokumenteAO.this.liste.getSelectedValue().toString().substring(0, komma);
                        int vID = tabVeranstaltung.getVeranstaltungID(isSelectedVeranstaltungName);
                        Protokoll protokoll = new Protokoll();
                        protokoll = tabProtokoll.getData(vID);
                        dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + protokoll.getJahr() + "/Temp/Protokoll_" + protokoll.getVeranstaltungID() + ".pdf";
                        ProtokollPDFScheiben.PDFdocumentErstellen(dateiname, protokoll);
                        Utils.dateiKatalogisieren(dateiname);
                    }
                    Desktop.getDesktop().open(new File(dateiname));
                    new TabelleFTPSync().updateFTPSync_StatusResert(dateiname, runApplication.clientID);
                }
                catch (DocumentException | IOException | IllegalArgumentException | NullPointerException | SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.FEHLER_BEIM_OEFFNEN, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonHochladen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnVal = DokumenteAO.this.chooser.showOpenDialog(DokumenteAO.this.chooser);
                if (returnVal == 0) {
                    logging.logInfo((Object)("Ausgew\u00e4hlte Datei: " + DokumenteAO.this.chooser.getSelectedFile().getPath()));
                }
                try {
                    String name = String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.chooser.getSelectedFile().getName();
                    if (new File(name).exists()) {
                        int msg = JOptionPane.showConfirmDialog(null, Konstante.DATEI_EXISTIERT_BEREITS, "Frage", 0);
                        if (msg == 0) {
                            logging.logInfo((Object)"Datei existiert bereits und Benutzer m\u00f6chte sie ersetzen...");
                            Datei.copyFileAusf\u00fchren((File)new File(DokumenteAO.this.chooser.getSelectedFile().getPath()), (String)name);
                        }
                    } else {
                        logging.logInfo((Object)"Datei existiert nicht, es wird kopiert");
                        Datei.copyFileAusf\u00fchren((File)new File(DokumenteAO.this.chooser.getSelectedFile().getPath()), (String)name);
                    }
                    Utils.dateiKatalogisieren(name);
                }
                catch (IOException | SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                File ordnerBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString());
                File[] dateilisteBeteiligung = ordnerBeteiligung.listFiles();
                DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
            }
        });
        this.buttonEntfernen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                File file = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString());
                try {
                    int msg = JOptionPane.showConfirmDialog(null, Konstante.WIRKLICH_LOESCHEN, "Frage", 0);
                    if (msg == 0) {
                        TabelleFTPSync tabSync = new TabelleFTPSync();
                        Datei.copyFileAusf\u00fchren((File)file, (String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/papierkorb/" + file.getName()));
                        file.delete();
                        Utils.dateiKatalogisierenForDelete(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString());
                        tabSync.deleteOneFile(Utils.removeBackSlashFromString(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString() + DokumenteAO.this.liste.getSelectedValue().toString()));
                        logging.logInfo((Object)("Datei: " + file.toString() + " wurde in den Papierkorb verschoben"));
                        File[] dateilisteBeteiligung = new File(String.valueOf(runApplication.arbeitsverzeichnis) + DokumenteAO.this.aktuellerOrdner.toString()).listFiles();
                        DokumenteAO.this.liste.setListData(DokumenteAO.this.prepareFileNameForList(dateilisteBeteiligung));
                    }
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

