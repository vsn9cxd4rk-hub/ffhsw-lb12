/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.listen;

import ao.AbstractFenster;
import ao.listen.LehrgangListeAO;
import data.tabellen.TabelleLehrgang;
import data.tabellen.TabelleLehrgang_kategorie;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class LehrgangListeOptionenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonUebernehmen;
    private JButton buttonZurueck;
    private JCheckBox[] jCheckboxArray;
    public static JCheckBox[] jCheckboxArrayZusatzfelder;
    public static JTextField zusatzFeld1;
    public static JCheckBox zusatzBox1;
    public static JTextField zusatzFeld2;
    public static JCheckBox zusatzBox2;
    public static JTextField zusatzFeld3;
    public static JCheckBox zusatzBox3;
    public static JPanel panelFreiFelder;
    private JLabel modulBeschreibung;
    private JLabel beschreibung2;
    private JLabel beschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public LehrgangListeOptionenAO() {
        super("FeuerwehrManagementSystem - Lehrgang Liste Optionen");
        logging.logInfo((Object)"Starte: LehrgangListeOptionenAO");
    }

    protected void buttonErstellen() {
        this.buttonUebernehmen = new JButton("\u00dcbernehmen");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.beschreibung = new JLabel("W\u00e4hlen Sie Bitte die Lehrg\u00e4nge aus die in der Liste angezeigt werden sollen: ");
        this.beschreibung2 = new JLabel("Hier k\u00f6nnen Sie Optionale Zusatzfelder f\u00fcr die Liste ausw\u00e4hlen: ");
        this.modulBeschreibung = new JLabel("Lehrgnagsliste Optionen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        zusatzFeld1 = new JTextField(10);
        zusatzFeld2 = new JTextField(10);
        zusatzFeld3 = new JTextField(10);
        zusatzBox1 = new JCheckBox("Frei Feld 1: ");
        zusatzBox2 = new JCheckBox("Frei Feld 2: ");
        zusatzBox3 = new JCheckBox("Frei Feld 3: ");
    }

    protected void labelErstellen() {
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
        this.setSize(680, 690);
        this.setTitle("FeuerwehrManagementSystem - Lehrgang Liste");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.beschreibung);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        TabelleLehrgang_kategorie tabLehrgang_kat = new TabelleLehrgang_kategorie();
        try {
            String[] labels = Utils.listToArray(tabLehrgang_kat.getAlleLehrg\u00e4nge());
            int[] labelsID = Utils.listToIntArray(tabLehrgang_kat.getAlleLehrg\u00e4ngeID());
            this.jCheckboxArray = new JCheckBox[labels.length];
            int x = 0;
            while (x < labels.length) {
                this.jCheckboxArray[x] = new JCheckBox();
                this.jCheckboxArray[x].setText(labels[x]);
                this.jCheckboxArray[x].setName(Integer.toString(labelsID[x]));
                panel.add(this.jCheckboxArray[x]);
                logging.logInfo((Object)("F\u00fcge Check Box: " + labels[x] + " hinzu...."));
                ++x;
            }
            if (labels.length <= 32) {
                this.add(panel, "Center");
            } else {
                JScrollPane pane = new JScrollPane(panel);
                pane.setVerticalScrollBarPolicy(22);
                pane.setPreferredSize(new Dimension(630, 390));
                this.add(pane, "Center");
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.beschreibung2);
        JPanel zusatzPanel = new JPanel(new GridLayout(0, 2));
        String[] zusatzLabels = new String[]{"G25", "G26/3", "G30", "G41", "G42", "Ablauf LKW F\u00fchrerschein", "AGT Training"};
        jCheckboxArrayZusatzfelder = new JCheckBox[zusatzLabels.length];
        int x = 0;
        while (x < zusatzLabels.length) {
            LehrgangListeOptionenAO.jCheckboxArrayZusatzfelder[x] = new JCheckBox();
            jCheckboxArrayZusatzfelder[x].setText(zusatzLabels[x]);
            zusatzPanel.add(jCheckboxArrayZusatzfelder[x]);
            ++x;
        }
        this.add(zusatzPanel, "Center");
        panelFreiFelder = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", panelFreiFelder);
        panelFreiFelder.add(zusatzBox1);
        panelFreiFelder.add(zusatzFeld1);
        panelFreiFelder.add(zusatzBox2);
        panelFreiFelder.add(zusatzFeld2);
        panelFreiFelder.add(zusatzBox3);
        panelFreiFelder.add(zusatzFeld3);
        this.add(this.dummy2);
        this.add(this.buttonUebernehmen);
        this.add(this.buttonZurueck);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonUebernehmen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    int count = 0;
                    int i = 0;
                    while (i < LehrgangListeOptionenAO.this.jCheckboxArray.length) {
                        if (LehrgangListeOptionenAO.this.jCheckboxArray[i].isSelected()) {
                            ++count;
                        }
                        ++i;
                    }
                    if (count == 0) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_LEHRGANG_AUSWAEHLEN, "Warnung", 2);
                    } else {
                        int[] selectierteLehrg\u00e4nge = new int[count];
                        count = 0;
                        int i2 = 0;
                        while (i2 < LehrgangListeOptionenAO.this.jCheckboxArray.length) {
                            if (LehrgangListeOptionenAO.this.jCheckboxArray[i2].isSelected()) {
                                selectierteLehrg\u00e4nge[count] = Integer.parseInt(LehrgangListeOptionenAO.this.jCheckboxArray[i2].getName());
                                ++count;
                            }
                            ++i2;
                        }
                        LehrgangListeAO.headname = new TabelleLehrgang().mapFilterHeadNameToVector(selectierteLehrg\u00e4nge);
                        ((DefaultTableModel)LehrgangListeAO.table.getModel()).setDataVector(new TabelleLehrgang().getFilterDataForList(selectierteLehrg\u00e4nge), LehrgangListeAO.headname);
                        LehrgangListeAO.buttonStandard.setVisible(true);
                        LehrgangListeOptionenAO.this.dispose();
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                    LehrgangListeOptionenAO.this.dispose();
                }
            }
        });
        this.buttonZurueck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logging.logInfo((Object)"Optionsmaske wird geschlossen...");
                LehrgangListeOptionenAO.this.dispose();
            }
        });
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        MyEvent.setEvent((String)"0x0030");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

