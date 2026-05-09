/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.ausbildung;

import ao.AbstractFenster;
import data.tabellen.TabelleBef\u00f6rderungKonfig;
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleLehrgang_kategorie;
import go.Bef\u00f6rderung;
import go.Bef\u00f6rderung_erforderlich;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Utils;

public class Bef\u00f6rderungZuordnungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonRein;
    private JButton buttonRaus;
    public static JList<Object> nichtRelevantList;
    public static JList<Object> relevantList;
    public static JScrollPane pane_nichtRelevantList;
    public static JScrollPane pane_relevantList;
    private JComboBox<String> dienstgrad;
    private JComboBox<String> dienstgradVoraussetzung;
    private JComboBox<String> mindestzeit;
    private JComboBox<String> zugeh\u00f6rigkeitsZeit;
    private JCheckBox nurZeitBefoerderung;
    private JCheckBox letzteStufe;
    private JCheckBox auslassen;
    private JLabel dienstgard_label;
    private JLabel dienstgradVoraussetzung_label;
    private JLabel mindestzeit_label;
    private JLabel nurZeitBefoerderung_label;
    private JLabel letzteStufe_label;
    private JLabel auslassen_label;
    private JLabel zugeh\u00f6rigkeitsZeit_label;
    private JLabel label_nichtrRelevantListe;
    private JLabel label_relevantListe;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelListe;
    private JPanel panelKonfig;
    int datensatznummer;
    String datensatzname;

    public Bef\u00f6rderungZuordnungAO() {
        super("FeuerwehrManagementSystem - Lehrgangskonfiguration");
        logging.logInfo((Object)"Starte: LehrgangZuordnungAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonRein = new JButton("\u2190");
        this.buttonRein.setToolTipText("Hinzuf\u00fcgen");
        this.buttonRaus = new JButton("\u2192");
        this.buttonRaus.setToolTipText("Entfernen");
        this.nurZeitBefoerderung = new JCheckBox();
        this.letzteStufe = new JCheckBox();
        this.auslassen = new JCheckBox();
        this.dienstgard_label = new JLabel("Dienstgrad: ");
        this.dienstgradVoraussetzung_label = new JLabel("Vorausgesetzer Dienstgrad: ");
        this.mindestzeit_label = new JLabel("Mindestzeit des Vorausgesetzen Dienstgrades:          ");
        this.nurZeitBefoerderung_label = new JLabel("Nur Zeit Bef\u00f6rderung (KEINE weiteren Lehrg\u00e4nge): ");
        this.letzteStufe_label = new JLabel("Letzte Bef\u00f6rderungsstufe: ");
        this.auslassen_label = new JLabel("Diesen Dienstgrad auslassen: ");
        this.zugeh\u00f6rigkeitsZeit_label = new JLabel("Erforderliche Zugeh\u00f6rigkeit: ");
        this.label_nichtrRelevantListe = new JLabel("Nicht Relevante Liste:                           ");
        this.label_relevantListe = new JLabel("Relevante Liste f\u00fcr Bef\u00f6rderungsmeldungen:                          ");
        this.modulBeschreibung = new JLabel("Bef\u00f6rderungskonfiguration");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
        TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
        try {
            String[] nichtRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleLehrg\u00e4nge());
            String[] dienstgardListe = Utils.listToArray(tabDienstgrad.getAllDienstgradLang());
            String[] mindestzeitListe = new String[]{"0 Jahre", "1 Jahre", "2 Jahre", "3 Jahre", "4 Jahre", "5 Jahre", "6 Jahre", "7 Jahre", "8 Jahre", "9 Jahre", "10 Jahre", "11 Jahre", "12 Jahre", "13 Jahre", "14 Jahre", "15 Jahre", "16 Jahre", "17 Jahre", "18 Jahre", "19 Jahre", "20 Jahre"};
            String[] zugeh\u00f6rigkeitsListe = new String[]{"<nicht ber\u00fccksichtigen>", "0 Jahre", "1 Jahre", "2 Jahre", "3 Jahre", "4 Jahre", "5 Jahre", "6 Jahre", "7 Jahre", "8 Jahre", "9 Jahre", "10 Jahre", "11 Jahre", "12 Jahre", "13 Jahre", "14 Jahre", "15 Jahre", "16 Jahre", "17 Jahre", "18 Jahre", "19 Jahre", "20 Jahre", "21 Jahre", "22 Jahre", "23 Jahre", "24 Jahre", "25 Jahre", "30 Jahre", "35 Jahre", "40 Jahre", "45 Jahre", "50 Jahre"};
            this.dienstgrad = new JComboBox<String>(dienstgardListe);
            this.dienstgradVoraussetzung = new JComboBox<String>(dienstgardListe);
            this.mindestzeit = new JComboBox<String>(mindestzeitListe);
            this.zugeh\u00f6rigkeitsZeit = new JComboBox<String>(zugeh\u00f6rigkeitsListe);
            nichtRelevantList = new JList(nichtRelevant);
            nichtRelevantList.setVisibleRowCount(15);
            nichtRelevantList.setToolTipText("Liste der nicht Zugeordneten Relevanten Lehrg\u00e4nge");
            pane_nichtRelevantList = new JScrollPane(nichtRelevantList);
            pane_nichtRelevantList.setVerticalScrollBarPolicy(22);
            pane_nichtRelevantList.setPreferredSize(new Dimension(300, 260));
            relevantList = new JList();
            relevantList.setVisibleRowCount(15);
            relevantList.setToolTipText("Liste der Zugeordneten Relevanten Lehrg\u00e4nge");
            pane_relevantList = new JScrollPane(relevantList);
            pane_relevantList.setVerticalScrollBarPolicy(22);
            pane_relevantList.setPreferredSize(new Dimension(300, 260));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.dienstgrad.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleBef\u00f6rderungKonfig tabBef\u00f6rderung = new TabelleBef\u00f6rderungKonfig();
                    TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
                    if (tabBef\u00f6rderung.getCountForDienstgrad(tabDienstgrad.getDienstgradID(Bef\u00f6rderungZuordnungAO.this.dienstgrad.getSelectedItem().toString())) != 0) {
                        int dID = tabDienstgrad.getDienstgradID(Bef\u00f6rderungZuordnungAO.this.dienstgrad.getSelectedItem().toString());
                        int id = tabBef\u00f6rderung.getID(dID);
                        relevantList.setListData((Object[])Utils.listToArray(tabBef\u00f6rderung.getAllRelevantenLerh\u00e4nge(id)));
                        Bef\u00f6rderungZuordnungAO.this.dienstgradVoraussetzung.setSelectedItem(tabDienstgrad.getDienstgradBeschreibungLang(tabBef\u00f6rderung.getDienstgradVorausseltzung(dID)));
                        Bef\u00f6rderungZuordnungAO.this.mindestzeit.setSelectedItem(String.valueOf(tabBef\u00f6rderung.getZeit(dID)) + " Jahre");
                        Bef\u00f6rderungZuordnungAO.this.buttonRein.setEnabled(true);
                        if (tabBef\u00f6rderung.getNurZeitBefoerderung(dID) == 0) {
                            Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setSelected(false);
                            Bef\u00f6rderungZuordnungAO.this.buttonRein.setEnabled(true);
                        } else {
                            Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setSelected(true);
                            Bef\u00f6rderungZuordnungAO.this.buttonRein.setEnabled(false);
                        }
                        if (tabBef\u00f6rderung.getLetzteStufe(dID) == 1) {
                            Bef\u00f6rderungZuordnungAO.this.letzteStufe.setSelected(true);
                        } else {
                            Bef\u00f6rderungZuordnungAO.this.letzteStufe.setSelected(false);
                        }
                        if (tabBef\u00f6rderung.getAuslassen(dID) == 1) {
                            Bef\u00f6rderungZuordnungAO.this.auslassen.setSelected(true);
                        } else {
                            Bef\u00f6rderungZuordnungAO.this.auslassen.setSelected(false);
                        }
                        if (tabBef\u00f6rderung.getDienstZeit(dID) == -1) {
                            Bef\u00f6rderungZuordnungAO.this.zugeh\u00f6rigkeitsZeit.setSelectedItem("<nicht ber\u00fccksichtigen>");
                        } else {
                            Bef\u00f6rderungZuordnungAO.this.zugeh\u00f6rigkeitsZeit.setSelectedItem(String.valueOf(tabBef\u00f6rderung.getDienstZeit(dID)) + " Jahre");
                        }
                    } else {
                        relevantList.setListData((Object[])new Object[0]);
                        Bef\u00f6rderungZuordnungAO.this.dienstgradVoraussetzung.setSelectedItem(tabDienstgrad.getDienstgradBeschreibungLang(tabBef\u00f6rderung.getDienstgradVorausseltzung(0)));
                        Bef\u00f6rderungZuordnungAO.this.mindestzeit.setSelectedItem("0 Jahre");
                        Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setSelected(false);
                        Bef\u00f6rderungZuordnungAO.this.buttonRein.setEnabled(true);
                        Bef\u00f6rderungZuordnungAO.this.letzteStufe.setSelected(false);
                        Bef\u00f6rderungZuordnungAO.this.auslassen.setSelected(false);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.nurZeitBefoerderung.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleBef\u00f6rderungKonfig tabBef\u00f6rderung = new TabelleBef\u00f6rderungKonfig();
                TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
                if (Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.isSelected()) {
                    try {
                        Bef\u00f6rderungZuordnungAO.this.buttonRein.setEnabled(false);
                        int dID = tabDienstgrad.getDienstgradID(Bef\u00f6rderungZuordnungAO.this.dienstgrad.getSelectedItem().toString());
                        int id = tabBef\u00f6rderung.getID(dID);
                        tabBef\u00f6rderung.deleteAll(id);
                        Bef\u00f6rderungZuordnungAO.this.auslassen.setEnabled(false);
                        Bef\u00f6rderungZuordnungAO.this.auslassen.setSelected(false);
                        Bef\u00f6rderungZuordnungAO.this.letzteStufe.setEnabled(false);
                        Bef\u00f6rderungZuordnungAO.this.letzteStufe.setSelected(false);
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                } else {
                    relevantList.setEnabled(true);
                    Bef\u00f6rderungZuordnungAO.this.buttonRein.setEnabled(true);
                    Bef\u00f6rderungZuordnungAO.this.auslassen.setEnabled(true);
                    Bef\u00f6rderungZuordnungAO.this.letzteStufe.setEnabled(true);
                }
            }
        });
        this.letzteStufe.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (Bef\u00f6rderungZuordnungAO.this.letzteStufe.isSelected()) {
                    Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setEnabled(false);
                    Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setSelected(false);
                    Bef\u00f6rderungZuordnungAO.this.auslassen.setEnabled(false);
                    Bef\u00f6rderungZuordnungAO.this.auslassen.setSelected(false);
                } else {
                    Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setEnabled(true);
                    Bef\u00f6rderungZuordnungAO.this.auslassen.setEnabled(true);
                }
            }
        });
        this.auslassen.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                if (Bef\u00f6rderungZuordnungAO.this.auslassen.isSelected()) {
                    Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setEnabled(false);
                    Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setSelected(false);
                    Bef\u00f6rderungZuordnungAO.this.letzteStufe.setEnabled(false);
                    Bef\u00f6rderungZuordnungAO.this.letzteStufe.setSelected(false);
                } else {
                    Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.setEnabled(true);
                    Bef\u00f6rderungZuordnungAO.this.letzteStufe.setEnabled(true);
                }
            }
        });
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Bef\u00f6rderungskonfiguration");
        this.setSize(650, 640);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelKonfig = new JPanel(new GridLayout(7, 2));
        this.getContentPane().add("Center", this.panelKonfig);
        this.panelKonfig.add(this.dienstgard_label);
        this.panelKonfig.add(this.dienstgrad);
        this.panelKonfig.add(this.dienstgradVoraussetzung_label);
        this.panelKonfig.add(this.dienstgradVoraussetzung);
        this.panelKonfig.add(this.mindestzeit_label);
        this.panelKonfig.add(this.mindestzeit);
        this.panelKonfig.add(this.zugeh\u00f6rigkeitsZeit_label);
        this.panelKonfig.add(this.zugeh\u00f6rigkeitsZeit);
        this.panelKonfig.add(this.nurZeitBefoerderung_label);
        this.panelKonfig.add(this.nurZeitBefoerderung);
        this.panelKonfig.add(this.letzteStufe_label);
        this.panelKonfig.add(this.letzteStufe);
        this.panelKonfig.add(this.auslassen_label);
        this.panelKonfig.add(this.auslassen);
        this.add(this.label_relevantListe);
        this.add(this.label_nichtrRelevantListe);
        this.panelListe = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panelListe);
        this.panelListe.add(pane_relevantList);
        this.panelListe.add(pane_nichtRelevantList);
        this.add(this.dummy2);
        this.add(this.buttonRein);
        this.add(this.buttonRaus);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleBef\u00f6rderungKonfig tabBef\u00f6rderung = new TabelleBef\u00f6rderungKonfig();
                    TabelleDienstgrad tabDienstgard = new TabelleDienstgrad();
                    Bef\u00f6rderung bef\u00f6rderung = new Bef\u00f6rderung();
                    int dID = tabDienstgard.getDienstgradID(Bef\u00f6rderungZuordnungAO.this.dienstgrad.getSelectedItem().toString());
                    int stringJahr = Bef\u00f6rderungZuordnungAO.this.mindestzeit.getSelectedItem().toString().indexOf(" Jahre");
                    System.out.println(stringJahr);
                    bef\u00f6rderung.setDienstgradID(dID);
                    bef\u00f6rderung.setDienstgradVoraussetzung(tabDienstgard.getDienstgradID(Bef\u00f6rderungZuordnungAO.this.dienstgradVoraussetzung.getSelectedItem().toString()));
                    bef\u00f6rderung.setZeit(Integer.parseInt(Bef\u00f6rderungZuordnungAO.this.mindestzeit.getSelectedItem().toString().substring(0, stringJahr)));
                    bef\u00f6rderung.setNurZeitBefoerderung(Bef\u00f6rderungZuordnungAO.this.nurZeitBefoerderung.isSelected() ? 1 : 0);
                    bef\u00f6rderung.setLetzteStufe(Bef\u00f6rderungZuordnungAO.this.letzteStufe.isSelected() ? 1 : 0);
                    bef\u00f6rderung.setAuslassen(Bef\u00f6rderungZuordnungAO.this.auslassen.isSelected() ? 1 : 0);
                    if (Bef\u00f6rderungZuordnungAO.this.zugeh\u00f6rigkeitsZeit.getSelectedItem().toString().equals("<nicht ber\u00fccksichtigen>")) {
                        bef\u00f6rderung.setDienstZeit(-1);
                    } else {
                        int stringJahrZurgh\u00f6rigkeit = Bef\u00f6rderungZuordnungAO.this.zugeh\u00f6rigkeitsZeit.getSelectedItem().toString().indexOf(" Jahre");
                        bef\u00f6rderung.setDienstZeit(Integer.parseInt(Bef\u00f6rderungZuordnungAO.this.zugeh\u00f6rigkeitsZeit.getSelectedItem().toString().substring(0, stringJahrZurgh\u00f6rigkeit)));
                    }
                    if (tabBef\u00f6rderung.getCountForDienstgrad(dID) == 0) {
                        bef\u00f6rderung.setId(tabBef\u00f6rderung.getNextNummer());
                        tabBef\u00f6rderung.insert(bef\u00f6rderung);
                    } else {
                        bef\u00f6rderung.setId(tabBef\u00f6rderung.getID(dID));
                        tabBef\u00f6rderung.update(bef\u00f6rderung);
                    }
                    logging.logInfo((Object)"Bef\u00f6rderungsinfo erfolgreich gespeichert...");
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonRein.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Bef\u00f6rderungZuordnungAO.this.buttonSpeichern.doClick();
                try {
                    TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                    TabelleBef\u00f6rderungKonfig tabBef\u00f6rderung = new TabelleBef\u00f6rderungKonfig();
                    TabelleDienstgrad tabDienstgard = new TabelleDienstgrad();
                    Bef\u00f6rderung_erforderlich befErforderlich = new Bef\u00f6rderung_erforderlich();
                    int id = tabBef\u00f6rderung.getID(tabDienstgard.getDienstgradID(Bef\u00f6rderungZuordnungAO.this.dienstgrad.getSelectedItem().toString()));
                    befErforderlich.setId(id);
                    befErforderlich.setLehrgangID(tabKategorie.getLehrgangID(nichtRelevantList.getSelectedValue().toString()));
                    tabBef\u00f6rderung.insert(befErforderlich);
                    relevantList.setListData((Object[])Utils.listToArray(tabBef\u00f6rderung.getAllRelevantenLerh\u00e4nge(id)));
                }
                catch (SQLException e1) {
                    logging.logPrintStackTrace((Exception)e1);
                }
            }
        });
        this.buttonRaus.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TabelleLehrgang_kategorie tabKategorie = new TabelleLehrgang_kategorie();
                TabelleBef\u00f6rderungKonfig tabBef\u00f6rderung = new TabelleBef\u00f6rderungKonfig();
                TabelleDienstgrad tabDienstgard = new TabelleDienstgrad();
                Bef\u00f6rderung_erforderlich befErforderlich = new Bef\u00f6rderung_erforderlich();
                try {
                    int id = tabBef\u00f6rderung.getID(tabDienstgard.getDienstgradID(Bef\u00f6rderungZuordnungAO.this.dienstgrad.getSelectedItem().toString()));
                    befErforderlich.setId(id);
                    befErforderlich.setLehrgangID(tabKategorie.getLehrgangID(relevantList.getSelectedValue().toString()));
                    tabBef\u00f6rderung.delete(befErforderlich);
                    relevantList.setListData((Object[])Utils.listToArray(tabBef\u00f6rderung.getAllRelevantenLerh\u00e4nge(id)));
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

