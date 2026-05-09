/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.ausbildung;

import ao.AbstractFenster;
import data.tabellen.TabelleLehrgang_kategorie;
import go.Lehrgang_Kategorie;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Utils;

public class LehrgangZuordnungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public JButton buttonZurueck;
    private JButton buttonRein;
    private JButton buttonRaus;
    private JButton buttonHoch;
    private JButton buttonRunter;
    public static JList<Object> nichtRelevantList;
    public static JList<Object> relevantList;
    public static JScrollPane pane_nichtRelevantList;
    public static JScrollPane pane_relevantList;
    private JLabel label_nichtrRelevantListe;
    private JLabel label_relevantListe;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelListe;
    int datensatznummer;
    String datensatzname;

    public LehrgangZuordnungAO() {
        super("FeuerwehrManagementSystem - Lehrgangskonfiguration");
        logging.logInfo((Object)"Starte: LehrgangZuordnungAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonRein = new JButton("\u2190");
        this.buttonRein.setToolTipText("Hinzuf\u00fcgen");
        this.buttonRaus = new JButton("\u2192");
        this.buttonRaus.setToolTipText("Entfernen");
        this.buttonHoch = new JButton("\u2191");
        this.buttonRunter = new JButton("\u2193");
        this.label_nichtrRelevantListe = new JLabel("Nicht Relevante Liste:                           ");
        this.label_relevantListe = new JLabel("Relevante Liste f\u00fcr Lehrgangsmeldungen:                          ");
        this.modulBeschreibung = new JLabel("Lehrgangskonfiguration");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
        try {
            String[] relevant = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
            String[] nichtRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleNichtRelevantenNamen());
            nichtRelevantList = new JList(nichtRelevant);
            nichtRelevantList.setVisibleRowCount(15);
            nichtRelevantList.setToolTipText("Liste der nicht Zugeordneten Relevanten Lehrg\u00e4nge");
            pane_nichtRelevantList = new JScrollPane(nichtRelevantList);
            pane_nichtRelevantList.setVerticalScrollBarPolicy(22);
            pane_nichtRelevantList.setPreferredSize(new Dimension(300, 260));
            relevantList = new JList(relevant);
            relevantList.setVisibleRowCount(15);
            relevantList.setToolTipText("Liste der Zugeordneten Relevanten Lehrg\u00e4nge");
            pane_relevantList = new JScrollPane(relevantList);
            pane_relevantList.setVerticalScrollBarPolicy(22);
            pane_relevantList.setPreferredSize(new Dimension(300, 260));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Lehrgangskonfiguration");
        this.setSize(650, 430);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.label_relevantListe);
        this.add(this.label_nichtrRelevantListe);
        this.panelListe = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panelListe);
        this.panelListe.add(pane_relevantList);
        this.panelListe.add(pane_nichtRelevantList);
        this.add(this.dummy2);
        this.add(this.buttonHoch);
        this.add(this.buttonRunter);
        this.add(this.buttonRein);
        this.add(this.buttonRaus);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonRein.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();
                try {
                    kategorie.setRelevant(1);
                    kategorie.setReihenfolge(tabLehrgangKategorie.getNextReihenfolgenummerNummer());
                    kategorie.setName(nichtRelevantList.getSelectedValue().toString());
                    tabLehrgangKategorie.update(kategorie);
                    String[] listDataRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
                    relevantList.setListData((Object[])listDataRelevant);
                    String[] listDataNichtRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleNichtRelevantenNamen());
                    nichtRelevantList.setListData((Object[])listDataNichtRelevant);
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonRaus.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();
                try {
                    kategorie.setRelevant(0);
                    kategorie.setReihenfolge(0);
                    kategorie.setName(relevantList.getSelectedValue().toString());
                    tabLehrgangKategorie.update(kategorie);
                    String[] listDataRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
                    relevantList.setListData((Object[])listDataRelevant);
                    String[] listDataNichtRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleNichtRelevantenNamen());
                    nichtRelevantList.setListData((Object[])listDataNichtRelevant);
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonHoch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();
                try {
                    int makiert = relevantList.getSelectedIndex();
                    kategorie.setRelevant(1);
                    kategorie.setReihenfolge(makiert);
                    kategorie.setName(relevantList.getSelectedValue().toString());
                    tabLehrgangKategorie.update(kategorie);
                    relevantList.setSelectedIndex(makiert - 1);
                    kategorie.setRelevant(1);
                    kategorie.setReihenfolge(makiert + 1);
                    kategorie.setName(relevantList.getSelectedValue().toString());
                    tabLehrgangKategorie.update(kategorie);
                    String[] listDataRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
                    relevantList.setListData((Object[])listDataRelevant);
                    relevantList.setSelectedIndex(makiert - 1);
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonRunter.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
                Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();
                try {
                    int makiert = relevantList.getSelectedIndex();
                    kategorie.setRelevant(1);
                    kategorie.setReihenfolge(makiert + 2);
                    kategorie.setName(relevantList.getSelectedValue().toString());
                    tabLehrgangKategorie.update(kategorie);
                    relevantList.setSelectedIndex(makiert + 1);
                    kategorie.setRelevant(1);
                    kategorie.setReihenfolge(makiert + 1);
                    kategorie.setName(relevantList.getSelectedValue().toString());
                    tabLehrgangKategorie.update(kategorie);
                    String[] listDataRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
                    relevantList.setListData((Object[])listDataRelevant);
                    relevantList.setSelectedIndex(makiert + 1);
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
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

