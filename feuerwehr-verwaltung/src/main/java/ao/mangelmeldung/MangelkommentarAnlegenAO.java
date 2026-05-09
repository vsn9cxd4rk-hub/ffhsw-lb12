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
import ao.mangelmeldung.M\u00e4ngelmeldungBearbeitenAO;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleMaengelmeldung_kommentar;
import data.tabellen.einstellungen.TabelleMandant;
import go.M\u00e4ngelmeldung_kommentar;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import utilities.SbcUtils;
import utilities.logbuchEingabe;

public class MangelkommentarAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JLabel mangelID_label;
    private JTextField mangelID;
    private JTextField kommentar;
    private JLabel kommentar_label;
    private JCheckBox bearbeitenStatus;
    private JLabel bearbeitenStatus_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelKommentar;

    public MangelkommentarAnlegenAO() {
        super("FeuerwehrManagementSystem - Kommentar");
        logging.logInfo((Object)"Starte: MangelkommentarAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Kommentar");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.kommentar = new JTextField(25);
        this.kommentar_label = new JLabel("Komentarfeld: ");
        this.bearbeitenStatus = new JCheckBox();
        this.bearbeitenStatus_label = new JLabel("Als behoben makieren: ");
        this.mangelID = new JTextField(25);
        this.mangelID_label = new JLabel("MangelID: ");
    }

    protected void labelErstellen() {
        this.bearbeitenStatus.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (MangelkommentarAnlegenAO.this.bearbeitenStatus.isSelected()) {
                    MangelkommentarAnlegenAO.this.kommentar.setText("Mangel ist Behoben");
                    MangelkommentarAnlegenAO.this.kommentar.setEditable(false);
                } else {
                    MangelkommentarAnlegenAO.this.kommentar.setText(null);
                    MangelkommentarAnlegenAO.this.kommentar.setEditable(true);
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
        this.setSize(600, 200);
        this.setTitle("FeuerwehrManagementSystem - Kommentar");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelKommentar = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panelKommentar);
        this.panelKommentar.add(this.mangelID_label);
        this.panelKommentar.add(this.mangelID);
        this.panelKommentar.add(this.kommentar_label);
        this.panelKommentar.add(this.kommentar);
        this.panelKommentar.add(this.bearbeitenStatus_label);
        this.panelKommentar.add(this.bearbeitenStatus);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.mangelID.setText(M\u00e4ngelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString());
        this.mangelID.setEditable(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleMaengelmeldung tabManel = new TabelleMaengelmeldung();
                TabelleMaengelmeldung_kommentar tabMangelKommentar = new TabelleMaengelmeldung_kommentar();
                M\u00e4ngelmeldung_kommentar kommentarObjekt = new M\u00e4ngelmeldung_kommentar();
                TabelleMandant tabMandant = new TabelleMandant();
                try {
                    int maID = Integer.parseInt(M\u00e4ngelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().substring(9, M\u00e4ngelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString().length()));
                    int mandantID = tabMandant.getMandantID(M\u00e4ngelmeldungBearbeitenAO.mandant.getSelectedItem().toString());
                    if (MangelkommentarAnlegenAO.this.bearbeitenStatus.isSelected()) {
                        tabManel.updateStatus(maID, 1);
                        logbuchEingabe.NeuerEintag("M\u00e4ngel Meldung: " + M\u00e4ngelmeldungBearbeitenAO.tree.getSelectionPath().getLastPathComponent().toString() + " wurde als behoben makiert");
                        M\u00e4ngelmeldungBearbeitenAO.tree.setModel(CreateTrees.CreateTreeMaengelListe(Integer.toString(mandantID)));
                    }
                    kommentarObjekt.setMangelID(maID);
                    kommentarObjekt.setKommentarID(tabMangelKommentar.getNextKommentarNummer(maID, mandantID));
                    kommentarObjekt.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    kommentarObjekt.setZeit(SbcUtils.timeStamp((String)"HH:mm:ss"));
                    kommentarObjekt.setKommentar(MangelkommentarAnlegenAO.this.kommentar.getText());
                    kommentarObjekt.setUser(runApplication.loginName);
                    kommentarObjekt.setMandantID(mandantID);
                    tabMangelKommentar.insert(kommentarObjekt);
                    M\u00e4ngelmeldungBearbeitenAO.liste.setText(tabMangelKommentar.getKommentarListe(maID, mandantID));
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    MangelkommentarAnlegenAO.this.dispose();
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

