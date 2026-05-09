/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.mangelmeldung;

import ao.AbstractFenster;
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleMaengelmeldung_kommentar;
import data.tabellen.mitglied.TabelleMitglied;
import go.M\u00e4ngelmeldung;
import go.M\u00e4ngelmeldung_kommentar;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.maengelmeldung.MaengelmeldungPDFSchreiben;
import run.runApplication;
import service.EMailService;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.XML;
import utilities.logbuchEingabe;

public class M\u00e4ngelmeldungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JComboBox<String> mitglieder;
    private JComboBox<String> fahrzeuge;
    private JLabel textfield_label;
    private JTextArea textfield;
    private JComboBox<String> wann;
    private JLabel wann_label;
    private JScrollPane pane;
    private JLabel meldender_label;
    private JLabel fahrzeug_label;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public M\u00e4ngelmeldungAO() {
        super("FeuerwehrManagementSystem - M\u00e4ngelmeldung");
        logging.logInfo((Object)"Starte: MangelmeldungAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern & Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.textfield = new JTextArea(19, 50);
        this.textfield.setLineWrap(true);
        this.textfield.setWrapStyleWord(true);
        this.pane = new JScrollPane(this.textfield);
        this.pane.setVerticalScrollBarPolicy(22);
        this.textfield_label = new JLabel("Detaillierte Beschreibung / Kommentar:");
        this.meldender_label = new JLabel("Meldender: ");
        this.fahrzeug_label = new JLabel("Fahrzeug: ");
        String[] liste = new String[]{"<bitte w\u00e4hlen>", "Ger\u00e4tepr\u00fcfung", "Dienstabend", "Einsatz", "\u00dcbung", "Sonstiges"};
        this.wann = new JComboBox<String>(liste);
        this.wann_label = new JLabel("Wann trat der Mangel auf: ");
        this.modulBeschreibung = new JLabel("M\u00e4ngelmeldung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        try {
            String[] fahrzeugListe = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
            String[] mitgliederListe = Utils.listToArrayOnlyFORComboBoxes(tabMitglied.getMitgliederGruppe1());
            this.mitglieder = new JComboBox<String>(mitgliederListe);
            this.fahrzeuge = new JComboBox<String>(fahrzeugListe);
            this.fahrzeuge.addItem("Ger\u00e4tehaus");
            this.fahrzeuge.addItem("Sonstige");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.setSize(600, 560);
        this.setTitle("FeuerwehrManagementSystem - M\u00e4ngelmeldung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.wann.setPreferredSize(new Dimension(250, 25));
        this.panel = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.fahrzeug_label);
        this.panel.add(this.fahrzeuge);
        this.panel.add(this.meldender_label);
        this.panel.add(this.mitglieder);
        this.panel.add(this.wann_label);
        this.panel.add(this.wann);
        this.add(this.textfield_label);
        this.add(this.pane);
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
                try {
                    TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                    TabelleMitglied tabMitglieder = new TabelleMitglied();
                    TabelleMaengelmeldung tabMangel = new TabelleMaengelmeldung();
                    TabelleMaengelmeldung_kommentar tabMangelKommentar = new TabelleMaengelmeldung_kommentar();
                    M\u00e4ngelmeldung mangel = new M\u00e4ngelmeldung();
                    M\u00e4ngelmeldung_kommentar kommentarObjekt = new M\u00e4ngelmeldung_kommentar();
                    int ID = tabMangel.getNextNummer();
                    int fID = tabFahrzeug.getFahrzeugID(M\u00e4ngelmeldungAO.this.fahrzeuge.getSelectedItem().toString());
                    if (M\u00e4ngelmeldungAO.this.fahrzeuge.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        logging.logInfo((Object)"Es wurde kein Fahrzeug ausgew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_FAHRZEUG_WAEHLEN, "Warnung", 2);
                    } else if (M\u00e4ngelmeldungAO.this.mitglieder.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        logging.logInfo((Object)"Es wurde kein Mitglied ausgew\u00e4hlt");
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                    } else if (M\u00e4ngelmeldungAO.this.wann.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_BESCHREIBUNG_ANGEBEN, "Warnung", 2);
                    } else if (!new File(runApplication.EINSTELLUNGEN.get("m\u00e4ngelmeldung")).exists()) {
                        JOptionPane.showMessageDialog(null, Konstante.MAENGELMELDUNG_BERICHT_NICHT_VORHANDEN + runApplication.EINSTELLUNGEN.get("m\u00e4ngelmeldung"), "Fehlermeldung", 0);
                    } else {
                        String dateiname;
                        int mID = tabMitglieder.getIdByGuiString(M\u00e4ngelmeldungAO.this.mitglieder.getSelectedItem().toString());
                        int mandantID = Integer.parseInt(runApplication.PROPERTIES.get("MandantID"));
                        String wannTratDerMangelAuf = fID != 0 ? String.valueOf(M\u00e4ngelmeldungAO.this.wann.getSelectedItem().toString()) + " (Mangel-ID" + ID + ", Fahrzeug: " + M\u00e4ngelmeldungAO.this.fahrzeuge.getSelectedItem().toString() + ")" : String.valueOf(M\u00e4ngelmeldungAO.this.wann.getSelectedItem().toString()) + " (Mangel-ID" + ID + ", Art / Ort: " + M\u00e4ngelmeldungAO.this.fahrzeuge.getSelectedItem().toString() + ")";
                        mangel.setId(ID);
                        mangel.setJahr(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")));
                        mangel.setMitgliedID(mID);
                        mangel.setFahrzeugID(fID);
                        mangel.setDatum(SbcUtils.timeStamp((String)"dd.MM.yyyy"));
                        mangel.setWann(wannTratDerMangelAuf);
                        mangel.setBeschreibung(M\u00e4ngelmeldungAO.this.textfield.getText());
                        if (runApplication.EINSTELLUNGEN.get("M\u00e4ngelBerichtArt").equals("Word Schnittstelle")) {
                            mangel.setDateiname("Meldung_ID_" + ID + ".doc");
                        } else {
                            mangel.setDateiname("Meldung_ID_" + ID + ".pdf");
                        }
                        mangel.setStatus(0);
                        tabMangel.insert(mangel);
                        kommentarObjekt.setMangelID(ID);
                        kommentarObjekt.setKommentarID(tabMangelKommentar.getNextKommentarNummer(ID, mandantID));
                        kommentarObjekt.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                        kommentarObjekt.setZeit(SbcUtils.timeStamp((String)"HH:mm:ss"));
                        kommentarObjekt.setKommentar("M\u00e4ngelmeldung wurde erstellt");
                        kommentarObjekt.setUser(runApplication.loginName);
                        kommentarObjekt.setMandantID(mandantID);
                        tabMangelKommentar.insert(kommentarObjekt);
                        kommentarObjekt.setMangelID(ID);
                        kommentarObjekt.setKommentarID(tabMangelKommentar.getNextKommentarNummer(ID, mandantID));
                        kommentarObjekt.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                        kommentarObjekt.setZeit(SbcUtils.timeStamp((String)"HH:mm:ss"));
                        kommentarObjekt.setKommentar("Details zur M\u00e4ngelmeldung:\nFahrzeug: " + M\u00e4ngelmeldungAO.this.fahrzeuge.getSelectedItem() + "\nMeldender: " + M\u00e4ngelmeldungAO.this.mitglieder.getSelectedItem() + "\nWann trat der Mangel auf: " + mangel.getWann() + "\n\nBeschreibung:\n" + mangel.getBeschreibung());
                        kommentarObjekt.setUser(runApplication.loginName);
                        kommentarObjekt.setMandantID(mandantID);
                        tabMangelKommentar.insert(kommentarObjekt);
                        if (runApplication.EINSTELLUNGEN.get("M\u00e4ngelBerichtArt").equals("Word Schnittstelle")) {
                            dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/mangel/Meldung_ID_" + ID + ".xml";
                            String[] ist = new String[]{"wfahr", "wdate", "wkenn", "wfunk", "wwann", "wpers", "wbesc"};
                            String[] zu = new String[]{Utils.checkTextAndRemoveIllegalSigns(M\u00e4ngelmeldungAO.this.fahrzeuge.getSelectedItem().toString()), SbcUtils.timeStamp((String)"dd.MM.yyyy"), Utils.checkTextAndRemoveIllegalSigns(tabFahrzeug.getKennezeichen(fID)), Utils.checkTextAndRemoveIllegalSigns(tabFahrzeug.getFunkrufname(fID)), Utils.checkTextAndRemoveIllegalSigns(wannTratDerMangelAuf), Utils.checkTextAndRemoveIllegalSigns(M\u00e4ngelmeldungAO.this.mitglieder.getSelectedItem().toString()), Utils.checkTextAndRemoveIllegalSigns(M\u00e4ngelmeldungAO.this.textfield.getText())};
                            XML.createEinsatzBericht(ist, zu, dateiname, runApplication.EINSTELLUNGEN.get("m\u00e4ngelmeldung"));
                            File docFile = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/mangel/Meldung_ID_" + ID + ".doc");
                            new File(dateiname).renameTo(docFile);
                            dateiname = docFile.getAbsolutePath();
                        } else {
                            dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/mangel/Meldung_ID_" + ID + ".pdf";
                            MaengelmeldungPDFSchreiben.PDFdocumentErstellen(dateiname, mangel, M\u00e4ngelmeldungAO.this.fahrzeuge.getSelectedItem().toString());
                        }
                        logbuchEingabe.NeuerEintag("M\u00e4gelmeldung erstellt: " + wannTratDerMangelAuf + " Details: " + dateiname);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (runApplication.EINSTELLUNGEN.get("emailModul").equals("1") && runApplication.EINSTELLUNGEN.get("m\u00e4ngelmeldungViaEMailVersenden").equals("1")) {
                            EMailService.EMailInformationServiceM\u00e4ngelmeldung(mangel);
                        }
                        Utils.dateiKatalogisieren(dateiname);
                        M\u00e4ngelmeldungAO.this.dispose();
                        logging.logInfo((Object)("\u00d6ffne Datei: " + dateiname));
                        Desktop.getDesktop().open(new File(dateiname));
                    }
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

