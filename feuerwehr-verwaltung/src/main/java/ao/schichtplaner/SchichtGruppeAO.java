/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.schichtplaner;

import ao.AbstractFenster;
import ao.schichtplaner.SchichtplanerAO;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.schicht.TabelleSchicht_gruppe;
import data.tabellen.schicht.TabelleSchicht_gruppen_mitglieder;
import go.schicht.SchichtGruppe;
import go.schicht.SchichtGruppenMitglieder;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;
import utilities.logbuchEingabe;

public class SchichtGruppeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JButton buttonRein;
    private JButton buttonRaus;
    private JButton buttonRausAlle;
    private JLabel name_label;
    private JTextField name;
    private JLabel gruppenName_label;
    private JComboBox<String> gruppenName;
    private JLabel nichtZugeordnet_label;
    private JLabel zugeordnet_label;
    public static JList<Object> nichtZugeordnet;
    public static JList<Object> zugeordnet;
    public static JScrollPane pane_nichtZugeordnet;
    public static JScrollPane pane_zugeordnet;
    private JPanel panel;
    private JPanel panelListe;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    public static String letzteKategorie;

    public SchichtGruppeAO() {
        super("FeuerwehrManagementSystem - Schichtplaner");
        logging.logInfo((Object)"Starte: SchichtplanerAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonRein = new JButton("\u2190");
        this.buttonRein.setToolTipText("Hinzuf\u00fcgen");
        this.buttonRaus = new JButton("\u2192");
        this.buttonRaus.setToolTipText("Entfernen");
        this.buttonRausAlle = new JButton("\u2192 \u2192");
        this.buttonRausAlle.setToolTipText("Entferne alle Zugeordneten Mitglieder");
        this.name = new JTextField(20);
        this.name_label = new JLabel("Gruppenname: ");
        this.gruppenName_label = new JLabel("Gruppenname: ");
        this.nichtZugeordnet_label = new JLabel("              Mitglieder ohne Zuordnung:             ");
        this.zugeordnet_label = new JLabel("Schichtteilnehmer:                                        ");
        this.modulBeschreibung = new JLabel("Schichtplaner");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        TabelleMitglied tabMiglied = new TabelleMitglied();
        TabelleSchicht_gruppe tabGruppe = new TabelleSchicht_gruppe();
        try {
            String[] listeGruppen = Utils.listToArrayOnlyFORComboBoxes(tabGruppe.getAllSchichtGruppen());
            this.gruppenName = new JComboBox<String>(listeGruppen);
            nichtZugeordnet = new JList(Utils.listToArray(tabMiglied.getMitgliederGruppe1()));
            nichtZugeordnet.setVisibleRowCount(15);
            nichtZugeordnet.setToolTipText("Liste aller verf\u00fcgbaren Mitglieder");
            pane_nichtZugeordnet = new JScrollPane(nichtZugeordnet);
            pane_nichtZugeordnet.setVerticalScrollBarPolicy(22);
            pane_nichtZugeordnet.setPreferredSize(new Dimension(300, 260));
            zugeordnet = new JList();
            zugeordnet.setVisibleRowCount(15);
            zugeordnet.setToolTipText("Liste der Zugeordneten Mitglieder");
            pane_zugeordnet = new JScrollPane(zugeordnet);
            pane_zugeordnet.setVerticalScrollBarPolicy(22);
            pane_zugeordnet.setPreferredSize(new Dimension(300, 260));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.gruppenName.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleSchicht_gruppen_mitglieder tabSchichtGruppenMitglieder = new TabelleSchicht_gruppen_mitglieder();
                TabelleSchicht_gruppe tabSchichtGruppe = new TabelleSchicht_gruppe();
                try {
                    int gID = tabSchichtGruppe.getGruppenID(SchichtGruppeAO.this.gruppenName.getSelectedItem().toString());
                    zugeordnet.setListData((Object[])Utils.listToArray(tabSchichtGruppenMitglieder.getMitglederEinerSchichtGruppe(gID)));
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
        this.setSize(650, 530);
        this.setTitle("FeuerwehrManagementSystem - Schichtplaner");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.gruppenName_label);
        this.panel.add(this.gruppenName);
        this.panel.add(this.name_label);
        this.panel.add(this.name);
        this.add(this.zugeordnet_label);
        this.add(this.nichtZugeordnet_label);
        this.panelListe = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panelListe);
        this.panelListe.add(pane_zugeordnet);
        this.panelListe.add(pane_nichtZugeordnet);
        this.add(this.buttonRein);
        this.add(this.buttonRaus);
        this.add(this.buttonRausAlle);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        if (MyEvent.event.equals("0x0501")) {
            this.gruppenName_label.setVisible(true);
            this.gruppenName.setVisible(true);
            this.name.setVisible(false);
            this.name_label.setVisible(false);
            this.buttonSpeichern.setVisible(false);
        } else {
            this.gruppenName_label.setVisible(false);
            this.gruppenName.setVisible(false);
            this.name.setVisible(true);
            this.name_label.setVisible(true);
            this.buttonSpeichern.setVisible(true);
        }
        try {
            new TabelleSchicht_gruppen_mitglieder().deleteGruppe0();
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleSchicht_gruppe tabGruppe = new TabelleSchicht_gruppe();
                TabelleSchicht_gruppen_mitglieder tabGruppenMitglieder = new TabelleSchicht_gruppen_mitglieder();
                SchichtGruppe gruppe = new SchichtGruppe();
                try {
                    if (tabGruppe.getCount(SchichtGruppeAO.this.name.getText()) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NAME_EXISTIERT_BEREITS, "Warnung", 2);
                    } else if (!MyEvent.event.equals("0x0501")) {
                        int gID = tabGruppe.getNextNummer();
                        gruppe.setId(gID);
                        gruppe.setName(SchichtGruppeAO.this.name.getText());
                        tabGruppe.insert(gruppe);
                        tabGruppenMitglieder.updateGruppe0(gID);
                        logbuchEingabe.NeuerEintag("Neue Schicht Gruppe wurde erstellt: " + SchichtGruppeAO.this.name.getText());
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        if (SchichtplanerAO.gruppenZuordnung.isSelected()) {
                            SchichtplanerAO.nichtZugeordnet.setListData((Object[])Utils.listToArray(tabGruppe.getAllSchichtGruppen()));
                        }
                        SchichtGruppeAO.this.dispose();
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonRein.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleSchicht_gruppen_mitglieder tabSchichtGruppenMitglieder = new TabelleSchicht_gruppen_mitglieder();
                SchichtGruppenMitglieder schichtGruppe = new SchichtGruppenMitglieder();
                TabelleSchicht_gruppe tabSchichtGruppe = new TabelleSchicht_gruppe();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    int gID = tabSchichtGruppe.getGruppenID(SchichtGruppeAO.this.gruppenName.getSelectedItem().toString());
                    int mID = tabMitglied.getIdByGuiString(nichtZugeordnet.getSelectedValue().toString());
                    if (tabSchichtGruppenMitglieder.getCountOfMitglieder(gID, mID) != 0) {
                        JOptionPane.showMessageDialog(null, Konstante.MITGLIED_BETREIS_VORHANDEN, "Warnung", 2);
                    } else {
                        schichtGruppe.setMitgliederID(mID);
                        schichtGruppe.setGruppenID(gID);
                        tabSchichtGruppenMitglieder.insert(schichtGruppe);
                        zugeordnet.setListData((Object[])Utils.listToArray(tabSchichtGruppenMitglieder.getMitglederEinerSchichtGruppe(gID)));
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonRaus.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleSchicht_gruppen_mitglieder tabSchichtGruppenMitglieder = new TabelleSchicht_gruppen_mitglieder();
                SchichtGruppenMitglieder schichtGruppe = new SchichtGruppenMitglieder();
                TabelleSchicht_gruppe tabSchichtGruppe = new TabelleSchicht_gruppe();
                TabelleMitglied tabMitglied = new TabelleMitglied();
                try {
                    int gID = tabSchichtGruppe.getGruppenID(SchichtGruppeAO.this.gruppenName.getSelectedItem().toString());
                    schichtGruppe.setMitgliederID(tabMitglied.getIdByGuiString(zugeordnet.getSelectedValue().toString()));
                    schichtGruppe.setGruppenID(gID);
                    tabSchichtGruppenMitglieder.deleteOne(schichtGruppe);
                    zugeordnet.setListData((Object[])Utils.listToArray(tabSchichtGruppenMitglieder.getMitglederEinerSchichtGruppe(gID)));
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonRausAlle.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleSchicht_gruppe tabGruppe = new TabelleSchicht_gruppe();
                TabelleSchicht_gruppen_mitglieder tabSchichtGruppenMitglieder = new TabelleSchicht_gruppen_mitglieder();
                try {
                    int gID = tabGruppe.getGruppenID(SchichtGruppeAO.this.gruppenName.getSelectedItem().toString());
                    tabSchichtGruppenMitglieder.deleteAlleEinerGruppe(gID);
                    String[] listeZugeordnet = Utils.listToArray(tabSchichtGruppenMitglieder.getMitglederEinerSchichtGruppe(gID));
                    zugeordnet.setListData((Object[])listeZugeordnet);
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

