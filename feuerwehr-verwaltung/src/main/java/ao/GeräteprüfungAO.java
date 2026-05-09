/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao;

import ao.AbstractFenster;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleGeraetepruefung;
import go.Geraetepruefung;
import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.TimeCalculation;
import utilities.Utils;

public class Ger\u00e4tepr\u00fcfungAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField fahrzeugID;
    private JComboBox<String> fahrzeugName;
    private JTextField stromerzeuger;
    private JTextField steckleiter;
    private JTextField schiebleiter;
    private JTextField hydraulik;
    private JTextField pumpe;
    private JTextField kettensaege;
    private JTextField doppelkanister;
    private JTextField geraetepruefung_allgem;
    private JTextField abstusiset;
    private JLabel fahrzeugName_label;
    private JLabel fahrzeugID_label;
    private JLabel stromerzeuger_label;
    private JLabel steckleiter_label;
    private JLabel schiebleiter_label;
    private JLabel hydraulik_label;
    private JLabel pumpe_label;
    private JLabel kettensaege_label;
    private JLabel doppelkanister_label;
    private JLabel geraetepruefung_allgem_label;
    private JLabel abstusiset_label;
    private JPanel panelFarzeug;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public Ger\u00e4tepr\u00fcfungAO() {
        super("FeuerwehrManagementSystem - Ger\u00e4tepr\u00fcfung");
        logging.logInfo((Object)"Starte: Ger\u00e4tepr\u00fcfungAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.fahrzeugID = new JTextField(20);
        this.stromerzeuger = new JTextField(20);
        this.steckleiter = new JTextField(20);
        this.schiebleiter = new JTextField(20);
        this.hydraulik = new JTextField(20);
        this.pumpe = new JTextField(20);
        this.kettensaege = new JTextField(20);
        this.doppelkanister = new JTextField(20);
        this.geraetepruefung_allgem = new JTextField(20);
        this.abstusiset = new JTextField(20);
        this.fahrzeugID_label = new JLabel("FahrzeugID: ");
        this.fahrzeugName_label = new JLabel("Fahrzeug Name: ");
        this.stromerzeuger_label = new JLabel("Stromerzeuger (Format: MM.yyyy): ");
        this.steckleiter_label = new JLabel("Steckleiter (Format: MM.yyyy): ");
        this.schiebleiter_label = new JLabel("Siebleiter (Format: MM.yyyy): ");
        this.hydraulik_label = new JLabel("Hydraulikaggregat (Format: MM.yyyy): ");
        this.pumpe_label = new JLabel("Pumpe (Format: MM.yyyy): ");
        this.kettensaege_label = new JLabel("Kettens\u00e4ge (Format: MM.yyyy): ");
        this.doppelkanister_label = new JLabel("Doppelkanister Kettens\u00e4ge (Format: MM.yyyy): ");
        this.geraetepruefung_allgem_label = new JLabel("Ger\u00e4tepr\u00fcfung allgem. (Format: MM.yyyy): ");
        this.abstusiset_label = new JLabel("Absturzsicherungsset (Format: MM.yyyy): ");
        this.modulBeschreibung = new JLabel("Ger\u00e4tepr\u00fcfung");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        String[] listeFahrzeuge = null;
        try {
            listeFahrzeuge = Utils.listToArrayOnlyFORComboBoxes(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
            this.fahrzeugName = new JComboBox<String>(listeFahrzeuge);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.fahrzeugName.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent arg0) {
                try {
                    TabelleGeraetepruefung tabGeraete = new TabelleGeraetepruefung();
                    TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                    int fID = tabFahrzeug.getFahrzeugID(Ger\u00e4tepr\u00fcfungAO.this.fahrzeugName.getSelectedItem().toString());
                    Ger\u00e4tepr\u00fcfungAO.this.fahrzeugID.setText(Integer.toString(fID));
                    if (tabGeraete.getCount(fID) != 0) {
                        Ger\u00e4tepr\u00fcfungAO.this.stromerzeuger.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getStromerzeuger(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.steckleiter.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getSteckleiter(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.schiebleiter.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getSchiebleiter(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.hydraulik.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getHydraulik(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.pumpe.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getPumpe(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.kettensaege.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getKettensaege(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.doppelkanister.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getDoppelkanister(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.geraetepruefung_allgem.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getGer\u00e4tePr\u00fcfungAllgemein(fID)));
                        Ger\u00e4tepr\u00fcfungAO.this.abstusiset.setText(TimeCalculation.parseShortDateForGUI(tabGeraete.getAbstusiset(fID)));
                    } else {
                        Ger\u00e4tepr\u00fcfungAO.this.stromerzeuger.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.steckleiter.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.schiebleiter.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.hydraulik.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.pumpe.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.kettensaege.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.doppelkanister.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.geraetepruefung_allgem.setText(null);
                        Ger\u00e4tepr\u00fcfungAO.this.abstusiset.setText(null);
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
        this.setSize(580, 410);
        this.setTitle("FeuerwehrManagementSystem - Mitglieder Untersuchung");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelFarzeug = new JPanel(new GridLayout(11, 2));
        this.getContentPane().add("Center", this.panelFarzeug);
        this.panelFarzeug.add(this.fahrzeugName_label);
        this.panelFarzeug.add(this.fahrzeugName);
        this.panelFarzeug.add(this.fahrzeugID_label);
        this.panelFarzeug.add(this.fahrzeugID);
        this.panelFarzeug.add(this.steckleiter_label);
        this.panelFarzeug.add(this.steckleiter);
        this.panelFarzeug.add(this.schiebleiter_label);
        this.panelFarzeug.add(this.schiebleiter);
        this.panelFarzeug.add(this.abstusiset_label);
        this.panelFarzeug.add(this.abstusiset);
        this.panelFarzeug.add(this.stromerzeuger_label);
        this.panelFarzeug.add(this.stromerzeuger);
        this.panelFarzeug.add(this.hydraulik_label);
        this.panelFarzeug.add(this.hydraulik);
        this.panelFarzeug.add(this.pumpe_label);
        this.panelFarzeug.add(this.pumpe);
        this.panelFarzeug.add(this.kettensaege_label);
        this.panelFarzeug.add(this.kettensaege);
        this.panelFarzeug.add(this.doppelkanister_label);
        this.panelFarzeug.add(this.doppelkanister);
        this.panelFarzeug.add(this.geraetepruefung_allgem_label);
        this.panelFarzeug.add(this.geraetepruefung_allgem);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
        this.fahrzeugName.setEditable(false);
        this.fahrzeugID.setEditable(false);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleGeraetepruefung tabUntersuchung = new TabelleGeraetepruefung();
                Geraetepruefung untersuchung = new Geraetepruefung();
                try {
                    if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.stromerzeuger.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.stromerzeuger.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.stromerzeuger.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.steckleiter.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.steckleiter.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.steckleiter.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.schiebleiter.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.schiebleiter.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.schiebleiter.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.hydraulik.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.hydraulik.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.hydraulik.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.pumpe.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.pumpe.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.pumpe.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.kettensaege.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.kettensaege.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.kettensaege.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.doppelkanister.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.doppelkanister.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.doppelkanister.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateShortFormat(Ger\u00e4tepr\u00fcfungAO.this.geraetepruefung_allgem.getText()) && !Ger\u00e4tepr\u00fcfungAO.this.geraetepruefung_allgem.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH2, "Fehlermeldung", 0);
                        Ger\u00e4tepr\u00fcfungAO.this.geraetepruefung_allgem.setBackground(Color.red);
                    } else {
                        Ger\u00e4tepr\u00fcfungAO.this.stromerzeuger.setBackground(Color.white);
                        Ger\u00e4tepr\u00fcfungAO.this.steckleiter.setBackground(Color.white);
                        Ger\u00e4tepr\u00fcfungAO.this.hydraulik.setBackground(Color.white);
                        Ger\u00e4tepr\u00fcfungAO.this.pumpe.setBackground(Color.white);
                        Ger\u00e4tepr\u00fcfungAO.this.kettensaege.setBackground(Color.white);
                        Ger\u00e4tepr\u00fcfungAO.this.doppelkanister.setBackground(Color.white);
                        Ger\u00e4tepr\u00fcfungAO.this.geraetepruefung_allgem.setBackground(Color.white);
                        int fID = Integer.parseInt(Ger\u00e4tepr\u00fcfungAO.this.fahrzeugID.getText());
                        untersuchung.setId(fID);
                        untersuchung.setStromerzeuger(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.stromerzeuger.getText()));
                        untersuchung.setSteckleiter(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.steckleiter.getText()));
                        untersuchung.setSchiebleiter(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.schiebleiter.getText()));
                        untersuchung.setHydraulik(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.hydraulik.getText()));
                        untersuchung.setPumpe(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.pumpe.getText()));
                        untersuchung.setKettensaege(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.kettensaege.getText()));
                        untersuchung.setDoppelkanister(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.doppelkanister.getText()));
                        untersuchung.setGeraetepruefung_allgm(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.geraetepruefung_allgem.getText()));
                        untersuchung.setAbstusiset(TimeCalculation.parseShortDateForDatabase(Ger\u00e4tepr\u00fcfungAO.this.abstusiset.getText()));
                        untersuchung.setInfoEMail(0);
                        if (tabUntersuchung.getCount(fID) == 0) {
                            tabUntersuchung.insert(untersuchung);
                            tabUntersuchung.updateInfoEMail(0);
                        } else {
                            tabUntersuchung.update(untersuchung);
                            tabUntersuchung.updateInfoEMail(0);
                        }
                        logging.logInfo((Object)"Ger\u00e4tewartung erfolgreich gespeichert");
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    }
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

