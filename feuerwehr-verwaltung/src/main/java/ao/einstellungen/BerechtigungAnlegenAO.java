/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.einstellungen;

import ao.AbstractFenster;
import ao.HauptprogrammAO;
import ao.einstellungen.BenutzerAnlegenAO;
import data.tabellen.einstellungen.TabelleBerechtigung;
import data.tabellen.einstellungen.TabelleBerechtigung_gruppe_name;
import data.tabellen.einstellungen.TabelleBerechtigunggruppe;
import data.tabellen.einstellungen.TabelleUser;
import go.Berechtigung;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import service.InformationService;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;
import utilities.logbuchEingabe;

public class BerechtigungAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static String letzteBerechtigungsgruppe;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JCheckBox[] jCheckboxArraySeite1;
    private JCheckBox[] jCheckboxArraySeite2;
    private JTextField name;
    private JLabel name_label;
    private JComboBox<String> berechtigungsgruppen;
    private JLabel berechtigungsgruppen_label;
    private JLabel filter_label;
    private JComboBox<String> filter;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelAuswahl;
    private JTabbedPane tabPane;

    public BerechtigungAnlegenAO() {
        super("FeuerwehrManagementSystem - Berechtigung");
        logging.logInfo((Object)"Starte: BerechtigungAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.name_label = new JLabel("Name des Profiles: ");
        this.name = new JTextField(35);
        this.berechtigungsgruppen_label = new JLabel("Berechtigungsgruppen: ");
        this.filter_label = new JLabel("Berechtungungsfilter: ");
        this.tabPane = new JTabbedPane();
        this.modulBeschreibung = new JLabel("Berechtigung anlegen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        TabelleBerechtigunggruppe tabBer = new TabelleBerechtigunggruppe();
        TabelleBerechtigung_gruppe_name tabGruppenName = new TabelleBerechtigung_gruppe_name();
        try {
            String[] gruppenListe = Utils.listToArrayOnlyFORComboBoxes(tabBer.getBercehtigungsgruppen());
            String[] gruppenNamen = Utils.listToArrayMitOptionAlle(tabGruppenName.getBerechtigungGruppeName());
            this.berechtigungsgruppen = new JComboBox<String>(gruppenListe);
            this.filter = new JComboBox<String>(gruppenNamen);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.berechtigungsgruppen.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                TabelleBerechtigunggruppe tabBer = new TabelleBerechtigunggruppe();
                BerechtigungAnlegenAO.this.tabPane.setEnabledAt(0, true);
                BerechtigungAnlegenAO.this.tabPane.setEnabledAt(1, true);
                try {
                    int[] ber = tabBer.getAll(tabBer.getID(BerechtigungAnlegenAO.this.berechtigungsgruppen.getSelectedItem().toString()), 1);
                    int[] ber2 = tabBer.getAll(tabBer.getID(BerechtigungAnlegenAO.this.berechtigungsgruppen.getSelectedItem().toString()), 2);
                    int i = 0;
                    while (i < ber.length) {
                        if (ber[i] == 1) {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite1[i].setSelected(true);
                        } else {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite1[i].setSelected(false);
                        }
                        if (BerechtigungAnlegenAO.this.berechtigungsgruppen.getSelectedItem().toString().equals("Administrator")) {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite1[i].setEnabled(false);
                        } else {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite1[i].setEnabled(true);
                        }
                        ++i;
                    }
                    i = 0;
                    while (i < ber2.length) {
                        if (ber2[i] == 1) {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite2[i].setSelected(true);
                        } else {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite2[i].setSelected(false);
                        }
                        if (BerechtigungAnlegenAO.this.berechtigungsgruppen.getSelectedItem().toString().equals("Administrator")) {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite2[i].setEnabled(false);
                        } else {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite2[i].setEnabled(true);
                        }
                        ++i;
                    }
                    BerechtigungAnlegenAO.this.name.setText(BerechtigungAnlegenAO.this.berechtigungsgruppen.getSelectedItem().toString());
                    if (BerechtigungAnlegenAO.this.berechtigungsgruppen.getSelectedItem().toString().equals("Administrator")) {
                        BerechtigungAnlegenAO.this.name.setEditable(false);
                        BerechtigungAnlegenAO.this.buttonSpeichern.setEnabled(false);
                    } else {
                        BerechtigungAnlegenAO.this.name.setEditable(true);
                        BerechtigungAnlegenAO.this.buttonSpeichern.setEnabled(true);
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.filter.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                int n = 0;
                while (n < BerechtigungAnlegenAO.this.jCheckboxArraySeite1.length) {
                    if (BerechtigungAnlegenAO.this.filter.getSelectedItem().equals("<alle>")) {
                        BerechtigungAnlegenAO.this.jCheckboxArraySeite1[n].setVisible(true);
                        BerechtigungAnlegenAO.this.filter.setBackground(null);
                    } else {
                        BerechtigungAnlegenAO.this.filter.setBackground(Color.CYAN);
                        if (BerechtigungAnlegenAO.this.jCheckboxArraySeite1[n].getName().equals(Integer.toString(BerechtigungAnlegenAO.this.filter.getSelectedIndex()))) {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite1[n].setVisible(true);
                        } else {
                            BerechtigungAnlegenAO.this.jCheckboxArraySeite1[n].setVisible(false);
                        }
                    }
                    ++n;
                }
                n = 0;
                while (n < BerechtigungAnlegenAO.this.jCheckboxArraySeite2.length) {
                    if (BerechtigungAnlegenAO.this.filter.getSelectedItem().equals("<Alle>")) {
                        BerechtigungAnlegenAO.this.jCheckboxArraySeite2[n].setVisible(true);
                    } else if (BerechtigungAnlegenAO.this.jCheckboxArraySeite2[n].getName().equals(Integer.toString(BerechtigungAnlegenAO.this.filter.getSelectedIndex()))) {
                        BerechtigungAnlegenAO.this.jCheckboxArraySeite2[n].setVisible(true);
                    } else {
                        BerechtigungAnlegenAO.this.jCheckboxArraySeite2[n].setVisible(false);
                    }
                    ++n;
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
        this.setSize(1280, 768);
        this.setTitle("FeuerwehrManagementSystem - Berechtigung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelAuswahl = new JPanel(new GridLayout(3, 2));
        this.getContentPane().add("Center", this.panelAuswahl);
        this.panelAuswahl.add(this.berechtigungsgruppen_label);
        this.panelAuswahl.add(this.berechtigungsgruppen);
        this.panelAuswahl.add(this.filter_label);
        this.panelAuswahl.add(this.filter);
        this.panelAuswahl.add(this.name_label);
        this.panelAuswahl.add(this.name);
        JPanel panelSeite1 = new JPanel(new GridLayout(0, 4));
        JPanel panelSeite2 = new JPanel(new GridLayout(0, 4));
        TabelleBerechtigung tabBerechtigung = new TabelleBerechtigung();
        try {
            String[] berechtigungNameSeite1 = Utils.listToArray(tabBerechtigung.getBerechtigungName(1));
            String[] berechtigungIDListeSeite1 = Utils.listToArray(tabBerechtigung.getBerechtigungIDs(1));
            String[] berechtigungGruppeSeite1 = Utils.listToArray(tabBerechtigung.getBerechtigungGruppe(1));
            String[] berechtigungNameSeite2 = Utils.listToArray(tabBerechtigung.getBerechtigungName(2));
            String[] berechtigungIDListeSeite2 = Utils.listToArray(tabBerechtigung.getBerechtigungIDs(2));
            String[] berechtigungGruppeSeite2 = Utils.listToArray(tabBerechtigung.getBerechtigungGruppe(2));
            this.jCheckboxArraySeite1 = new JCheckBox[berechtigungNameSeite1.length];
            this.jCheckboxArraySeite2 = new JCheckBox[berechtigungNameSeite2.length];
            int x = 0;
            while (x < berechtigungNameSeite1.length) {
                this.jCheckboxArraySeite1[x] = new JCheckBox();
                this.jCheckboxArraySeite1[x].setText(berechtigungNameSeite1[x]);
                this.jCheckboxArraySeite1[x].setName(berechtigungGruppeSeite1[x]);
                this.jCheckboxArraySeite1[x].setToolTipText("Berechtigung S1." + berechtigungIDListeSeite1[x] + " - " + berechtigungNameSeite1[x]);
                panelSeite1.add(this.jCheckboxArraySeite1[x]);
                if (MyEvent.event.equals("0x0005")) {
                    this.jCheckboxArraySeite1[x].setEnabled(false);
                }
                ++x;
            }
            x = 0;
            while (x < berechtigungNameSeite2.length) {
                this.jCheckboxArraySeite2[x] = new JCheckBox();
                this.jCheckboxArraySeite2[x].setText(berechtigungNameSeite2[x]);
                this.jCheckboxArraySeite2[x].setName(berechtigungGruppeSeite2[x]);
                this.jCheckboxArraySeite2[x].setToolTipText("Berechtigung S2." + berechtigungIDListeSeite2[x] + " - " + berechtigungNameSeite2[x]);
                panelSeite2.add(this.jCheckboxArraySeite2[x]);
                if (MyEvent.event.equals("0x0005")) {
                    this.jCheckboxArraySeite2[x].setEnabled(false);
                }
                ++x;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.tabPane.addTab("Berechtigungen - Seite 1", panelSeite1);
        this.tabPane.addTab("Berechtigungen - Seite 2", panelSeite2);
        this.tabPane.setPreferredSize(new Dimension(1180, 550));
        this.add(this.tabPane);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.berechtigungsgruppen_label.setVisible(false);
        this.berechtigungsgruppen.setVisible(false);
        if (MyEvent.event.equals("0x0005")) {
            this.berechtigungsgruppen_label.setVisible(true);
            this.berechtigungsgruppen.setVisible(true);
            this.buttonSpeichern.setText("Aktualisieren");
            this.tabPane.setEnabledAt(0, false);
            this.tabPane.setEnabledAt(1, false);
        }
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                if (MyEvent.event.equals("0x0024")) {
                    BenutzerAnlegenAO.berechtigungsgruppe.addItem(letzteBerechtigungsgruppe);
                }
                MyEvent.setEvent((String)"0");
                BerechtigungAnlegenAO.this.dispose();
            }
        });
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleUser tabUser = new TabelleUser();
                TabelleBerechtigunggruppe tabBer = new TabelleBerechtigunggruppe();
                Berechtigung berechtigungSeite1 = new Berechtigung();
                Berechtigung berechtigungSeite2 = new Berechtigung();
                try {
                    berechtigungSeite1.setName(BerechtigungAnlegenAO.this.name.getText());
                    berechtigungSeite1.setSeite(1);
                    berechtigungSeite1.setBR0(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[0].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR1(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[1].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR2(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[2].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR3(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[3].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR4(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[4].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR5(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[5].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR6(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[6].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR7(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[7].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR8(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[8].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR9(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[9].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR10(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[10].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR11(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[11].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR12(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[12].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR13(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[13].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR14(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[14].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR15(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[15].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR16(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[16].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR17(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[17].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR18(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[18].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR19(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[19].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR20(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[20].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR21(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[21].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR22(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[22].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR23(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[23].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR24(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[24].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR25(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[25].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR26(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[26].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR27(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[27].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR28(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[28].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR29(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[29].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR30(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[30].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR31(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[31].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR32(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[32].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR33(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[33].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR34(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[34].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR35(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[35].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR36(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[36].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR37(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[37].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR38(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[38].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR39(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[39].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR40(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[40].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR41(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[41].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR42(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[42].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR43(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[43].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR44(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[44].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR45(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[45].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR46(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[46].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR47(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[47].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR48(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[48].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR49(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[49].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR50(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[50].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR51(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[51].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR52(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[52].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR53(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[53].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR54(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[54].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR55(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[55].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR56(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[56].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR57(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[57].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR58(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[58].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR59(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[59].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR60(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[60].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR61(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[61].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR62(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[62].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR63(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[63].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR64(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[64].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR65(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[65].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR66(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[66].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR67(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[67].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR68(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[68].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR69(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[69].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR70(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[70].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR71(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[71].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR72(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[72].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR73(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[73].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR74(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[74].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR75(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[75].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR76(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[76].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR77(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[77].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR78(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[78].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR79(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[79].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR80(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[80].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR81(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[81].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR82(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[82].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR83(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[83].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR84(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[84].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR85(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[85].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR86(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[86].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR87(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[87].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR88(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[88].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR89(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[89].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR90(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[90].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR91(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[91].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR92(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[92].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR93(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[93].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR94(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[94].isSelected() ? 1 : 0);
                    berechtigungSeite1.setBR95(BerechtigungAnlegenAO.this.jCheckboxArraySeite1[95].isSelected() ? 1 : 0);
                    berechtigungSeite2.setName(BerechtigungAnlegenAO.this.name.getText());
                    berechtigungSeite2.setSeite(2);
                    berechtigungSeite2.setBR0(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[0].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR1(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[1].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR2(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[2].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR3(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[3].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR4(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[4].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR5(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[5].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR6(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[6].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR7(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[7].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR8(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[8].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR9(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[9].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR10(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[10].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR11(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[11].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR12(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[12].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR13(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[13].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR14(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[14].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR15(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[15].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR16(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[16].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR17(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[17].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR18(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[18].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR19(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[19].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR20(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[20].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR21(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[21].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR22(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[22].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR23(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[23].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR24(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[24].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR25(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[25].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR26(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[26].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR27(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[27].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR28(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[28].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR29(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[29].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR30(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[30].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR31(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[31].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR32(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[32].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR33(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[33].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR34(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[34].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR35(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[35].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR36(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[36].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR37(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[37].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR38(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[38].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR39(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[39].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR40(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[40].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR41(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[41].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR42(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[42].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR43(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[43].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR44(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[44].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR45(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[45].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR46(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[46].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR47(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[47].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR48(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[48].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR49(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[49].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR50(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[50].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR51(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[51].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR52(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[52].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR53(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[53].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR54(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[54].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR55(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[55].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR56(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[56].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR57(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[57].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR58(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[58].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR59(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[59].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR60(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[60].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR61(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[61].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR62(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[62].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR63(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[63].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR64(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[64].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR65(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[65].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR66(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[66].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR67(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[67].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR68(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[68].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR69(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[69].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR70(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[70].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR71(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[71].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR72(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[72].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR73(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[73].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR74(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[74].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR75(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[75].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR76(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[76].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR77(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[77].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR78(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[78].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR79(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[79].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR80(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[80].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR81(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[81].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR82(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[82].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR83(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[83].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR84(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[84].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR85(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[85].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR86(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[86].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR87(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[87].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR88(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[88].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR89(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[89].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR90(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[90].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR91(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[91].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR92(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[92].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR93(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[93].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR94(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[94].isSelected() ? 1 : 0);
                    berechtigungSeite2.setBR95(BerechtigungAnlegenAO.this.jCheckboxArraySeite2[95].isSelected() ? 1 : 0);
                    if (MyEvent.event.equals("0x0005")) {
                        int berechtigungID = tabBer.getID(BerechtigungAnlegenAO.this.berechtigungsgruppen.getSelectedItem().toString());
                        berechtigungSeite1.setId(berechtigungID);
                        berechtigungSeite2.setId(berechtigungID);
                        tabBer.update(berechtigungSeite1);
                        tabBer.update(berechtigungSeite2);
                        logbuchEingabe.NeuerEintag("Berechtigungsgruppe ge\u00e4ndert " + BerechtigungAnlegenAO.this.name.getText());
                    } else {
                        MyEvent.setEvent((String)"0x0024");
                        int nextID = tabBer.getNextNummer();
                        berechtigungSeite1.setId(nextID);
                        berechtigungSeite2.setId(nextID);
                        tabBer.insert(berechtigungSeite1);
                        tabBer.insert(berechtigungSeite2);
                        letzteBerechtigungsgruppe = BerechtigungAnlegenAO.this.name.getText();
                        BenutzerAnlegenAO.berechtigungsgruppe.addItem(letzteBerechtigungsgruppe);
                        logbuchEingabe.NeuerEintag("Berechtigungsgruppe erstellt " + BerechtigungAnlegenAO.this.name.getText());
                    }
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    BerechtigunsManager.ber = tabBer.getAll(tabUser.getRechte(runApplication.loginName), 1);
                    BerechtigunsManager.ber2 = tabBer.getAll(tabUser.getRechte(runApplication.loginName), 2);
                    Thread threadLadeInformationen = new Thread(){

                        @Override
                        public void run() {
                            HauptprogrammAO.aufgabenListe.setText(null);
                            HauptprogrammAO.aufgabenListe.setText("Bitte warten...");
                            HauptprogrammAO.aufgabenListe.setText(InformationService.checkInformationen());
                        }
                    };
                    MyEvent.setEvent((String)"0");
                    threadLadeInformationen.start();
                    BerechtigungAnlegenAO.this.dispose();
                }
                catch (SQLException e) {
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

