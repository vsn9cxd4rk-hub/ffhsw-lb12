/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.bestandsliste;

import ao.AbstractFenster;
import ao.bestandsliste.ArtikelZuweisenAO;
import ao.bestandsliste.BestandslisteAO;
import data.tabellen.bestandsliste.TabelleLager_artikel;
import go.bestandsliste.Artikel;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import utilities.Utils;

public class ArtikelBestandslisteEintragenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static String letzterArtikelName;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonSpeichernUndNaechster;
    private JButton buttonAktualisieren;
    private JButton buttonNaechsterArtikel;
    private JTextField id;
    private JTextField name;
    private JTextField beschreibung;
    private JTextField bild;
    private JTextField wert;
    private JTextField ean;
    private JComboBox<String> artikel;
    private JLabel id_label;
    private JLabel name_label;
    private JLabel beschreibung_label;
    private JLabel bild_label;
    private JLabel wert_label;
    private JLabel ean_label;
    private JLabel artikel_label;
    private JPanel panelArtikel;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public ArtikelBestandslisteEintragenAO() {
        super("FeuerwehrManagementSystem - Artikel");
        logging.logInfo((Object)"Starte: ArtikelEintragenAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonSpeichernUndNaechster = new JButton("Speichern & N\u00e4chster Artikel");
        this.buttonAktualisieren = new JButton("Aktualisieren");
        this.buttonNaechsterArtikel = new JButton("N\u00e4chster Artikel");
        this.id = new JTextField(20);
        this.name = new JTextField(20);
        this.beschreibung = new JTextField(20);
        this.bild = new JTextField(20);
        this.wert = new JTextField(20);
        this.ean = new JTextField(20);
        this.id_label = new JLabel("Artikelnummer: ");
        this.name_label = new JLabel("Name: ");
        this.beschreibung_label = new JLabel("Beschreibung");
        this.bild_label = new JLabel("Bild / Icon: ");
        this.wert_label = new JLabel("Wert: ");
        this.ean_label = new JLabel("EAN-Code: ");
        this.artikel_label = new JLabel("Artikel: ");
        this.modulBeschreibung = new JLabel("Artikel");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        try {
            TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
            String[] artikelListe = Utils.listToArrayOnlyFORComboBoxes(tabArtikel.getAllArtikel());
            this.artikel = new JComboBox<String>(artikelListe);
            if (!MyEvent.event.equals("0x0201")) {
                this.id.setText(Integer.toString(tabArtikel.getNextNummer()));
            }
            this.id.setEditable(false);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelHinzufuegen() {
        this.artikel.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                try {
                    int artID = tabArtikel.getArtikelID(ArtikelBestandslisteEintragenAO.this.artikel.getSelectedItem().toString());
                    ArtikelBestandslisteEintragenAO.this.id.setText(Integer.toString(artID));
                    ArtikelBestandslisteEintragenAO.this.name.setText(tabArtikel.getArtikelName(artID));
                    ArtikelBestandslisteEintragenAO.this.beschreibung.setText(tabArtikel.getArtikelBeschreibung(artID));
                    ArtikelBestandslisteEintragenAO.this.bild.setText(tabArtikel.getArtikelBild(artID));
                    ArtikelBestandslisteEintragenAO.this.wert.setText(Integer.toString(tabArtikel.getArtikelWert(artID)));
                    ArtikelBestandslisteEintragenAO.this.ean.setText(Integer.toString(tabArtikel.getArtikelEAN(artID)));
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Artikel");
        this.setSize(500, 330);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelArtikel = new JPanel(new GridLayout(7, 2));
        this.getContentPane().add("Center", this.panelArtikel);
        this.panelArtikel.add(this.artikel_label);
        this.panelArtikel.add(this.artikel);
        this.panelArtikel.add(this.id_label);
        this.panelArtikel.add(this.id);
        this.panelArtikel.add(this.name_label);
        this.panelArtikel.add(this.name);
        this.panelArtikel.add(this.beschreibung_label);
        this.panelArtikel.add(this.beschreibung);
        this.panelArtikel.add(this.bild_label);
        this.panelArtikel.add(this.bild);
        this.panelArtikel.add(this.wert_label);
        this.panelArtikel.add(this.wert);
        this.panelArtikel.add(this.ean_label);
        this.panelArtikel.add(this.ean);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.add(this.buttonSpeichernUndNaechster);
        this.add(this.buttonAktualisieren);
        this.add(this.buttonNaechsterArtikel);
        this.buttonNaechsterArtikel.setVisible(false);
        if (MyEvent.event.equals("0x0201")) {
            this.artikel.setVisible(true);
            this.artikel_label.setVisible(true);
            this.buttonAktualisieren.setVisible(true);
            this.buttonSpeichern.setVisible(false);
            this.buttonSpeichernUndNaechster.setVisible(false);
            MyEvent.setEvent((String)"0");
        } else {
            this.artikel.setVisible(false);
            this.artikel_label.setVisible(false);
            this.buttonAktualisieren.setVisible(false);
            this.buttonSpeichern.setVisible(true);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                if (MyEvent.event.equals("0x0026")) {
                    ArtikelZuweisenAO.artikel.addItem(letzterArtikelName);
                }
                ArtikelBestandslisteEintragenAO.this.dispose();
            }
        });
        this.buttonSpeichernUndNaechster.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                ArtikelBestandslisteEintragenAO.this.buttonSpeichern.doClick();
                ArtikelBestandslisteEintragenAO.this.buttonNaechsterArtikel.doClick();
            }
        });
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                block8: {
                    try {
                        TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                        Artikel artikel = new Artikel();
                        if (tabArtikel.getArtikelCountByName(ArtikelBestandslisteEintragenAO.this.name.getText()) != 0) {
                            JOptionPane.showMessageDialog(null, Konstante.ARTIKELNAME_BEREITS_VORHANDEN, "Warnung", 2);
                            break block8;
                        }
                        artikel.setId(Integer.parseInt(ArtikelBestandslisteEintragenAO.this.id.getText()));
                        artikel.setName(ArtikelBestandslisteEintragenAO.this.name.getText());
                        artikel.setBeschreibung(ArtikelBestandslisteEintragenAO.this.beschreibung.getText());
                        artikel.setBild(ArtikelBestandslisteEintragenAO.this.bild.getText());
                        try {
                            artikel.setWert(Integer.parseInt(ArtikelBestandslisteEintragenAO.this.wert.getText()));
                        }
                        catch (NumberFormatException e) {
                            artikel.setWert(0);
                        }
                        try {
                            artikel.setEAN(Integer.parseInt(ArtikelBestandslisteEintragenAO.this.ean.getText()));
                        }
                        catch (NumberFormatException e) {
                            artikel.setEAN(0);
                        }
                        tabArtikel.insert(artikel);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        ArtikelBestandslisteEintragenAO.this.buttonSpeichern.setEnabled(false);
                        if (MyEvent.event.equals("0x0026")) {
                            ArtikelZuweisenAO.artikel.addItem(ArtikelBestandslisteEintragenAO.this.name.getText());
                            ArtikelZuweisenAO.artikel.setSelectedItem(ArtikelBestandslisteEintragenAO.this.name.getText());
                            ArtikelBestandslisteEintragenAO.this.dispose();
                        }
                        ArtikelBestandslisteEintragenAO.this.buttonNaechsterArtikel.setVisible(true);
                        ArtikelBestandslisteEintragenAO.this.name.setEditable(false);
                        ArtikelBestandslisteEintragenAO.this.beschreibung.setEditable(false);
                        ArtikelBestandslisteEintragenAO.this.bild.setEditable(false);
                        ArtikelBestandslisteEintragenAO.this.wert.setEditable(false);
                        ArtikelBestandslisteEintragenAO.this.ean.setEditable(false);
                        BestandslisteAO.tree.setModel(CreateTrees.CreateBestandslisteTree());
                    }
                    catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
            }
        });
        this.buttonAktualisieren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                    Artikel artikel = new Artikel();
                    artikel.setId(Integer.parseInt(ArtikelBestandslisteEintragenAO.this.id.getText()));
                    artikel.setName(ArtikelBestandslisteEintragenAO.this.name.getText());
                    artikel.setBeschreibung(ArtikelBestandslisteEintragenAO.this.beschreibung.getText());
                    artikel.setBild(ArtikelBestandslisteEintragenAO.this.bild.getText());
                    try {
                        artikel.setWert(Integer.parseInt(ArtikelBestandslisteEintragenAO.this.wert.getText()));
                    }
                    catch (NumberFormatException e) {
                        artikel.setWert(0);
                    }
                    try {
                        artikel.setEAN(Integer.parseInt(ArtikelBestandslisteEintragenAO.this.ean.getText()));
                    }
                    catch (NumberFormatException e) {
                        artikel.setEAN(0);
                    }
                    tabArtikel.update(artikel);
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    BestandslisteAO.tree.setModel(CreateTrees.CreateBestandslisteTree());
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonNaechsterArtikel.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLager_artikel tabArtikel = new TabelleLager_artikel();
                try {
                    ArtikelBestandslisteEintragenAO.this.buttonNaechsterArtikel.setVisible(false);
                    ArtikelBestandslisteEintragenAO.this.buttonSpeichern.setEnabled(true);
                    ArtikelBestandslisteEintragenAO.this.name.setText(null);
                    ArtikelBestandslisteEintragenAO.this.beschreibung.setText(null);
                    ArtikelBestandslisteEintragenAO.this.bild.setText(null);
                    ArtikelBestandslisteEintragenAO.this.wert.setText(null);
                    ArtikelBestandslisteEintragenAO.this.id.setText(Integer.toString(tabArtikel.getNextNummer()));
                    ArtikelBestandslisteEintragenAO.this.name.setEditable(true);
                    ArtikelBestandslisteEintragenAO.this.beschreibung.setEditable(true);
                    ArtikelBestandslisteEintragenAO.this.bild.setEditable(true);
                    ArtikelBestandslisteEintragenAO.this.wert.setEditable(true);
                    ArtikelBestandslisteEintragenAO.this.ean.setEditable(true);
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

