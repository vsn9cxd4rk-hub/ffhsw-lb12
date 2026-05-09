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
import ao.listen.AnwesenheitListeAO;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.mitglied.TabelleMitglied;
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

public class AnwesenheitListeOptionenAO
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

    public AnwesenheitListeOptionenAO() {
        super("FeuerwehrManagementSystem - Anwesenheit Liste Optionen");
        logging.logInfo((Object)"Starte: AnwesenheitListeOptionenAO");
    }

    protected void buttonErstellen() {
        this.buttonUebernehmen = new JButton("\u00dcbernehmen");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.beschreibung = new JLabel("W\u00e4hlen Sie Bitte die Mitglieder aus, die in der Liste erscheinen sollen: ");
        this.beschreibung2 = new JLabel("Hier k\u00f6nnen Sie Optionale Zusatzfelder f\u00fcr die Liste ausw\u00e4hlen: ");
        this.modulBeschreibung = new JLabel("Anwesneheitsliste Optionen");
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
        this.setSize(480, 700);
        this.setTitle("FeuerwehrManagementSystem - Anwesenheitsliste Optionen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.beschreibung);
        JPanel panel = new JPanel(new GridLayout(0, 2));
        TabelleMitglied tabMitglied = new TabelleMitglied();
        try {
            String[] labels = Utils.listToArray(tabMitglied.getMitgliederGruppe1());
            int[] labelsID = Utils.listToIntArray(tabMitglied.getMitgliederIDGruppe1());
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
            if (labels.length <= 36) {
                this.add(panel, "Center");
            } else {
                JScrollPane pane = new JScrollPane(panel);
                pane.setVerticalScrollBarPolicy(22);
                pane.setPreferredSize(new Dimension(430, 390));
                this.add(pane, "Center");
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.add(this.beschreibung2);
        panelFreiFelder = new JPanel(new GridLayout(3, 2));
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
                    while (i < AnwesenheitListeOptionenAO.this.jCheckboxArray.length) {
                        if (AnwesenheitListeOptionenAO.this.jCheckboxArray[i].isSelected()) {
                            ++count;
                        }
                        ++i;
                    }
                    if (count == 0) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                    } else {
                        String[] selektierteMitglieder = new String[count];
                        count = 0;
                        int i2 = 0;
                        while (i2 < AnwesenheitListeOptionenAO.this.jCheckboxArray.length) {
                            if (AnwesenheitListeOptionenAO.this.jCheckboxArray[i2].isSelected()) {
                                selektierteMitglieder[count] = AnwesenheitListeOptionenAO.this.jCheckboxArray[i2].getName();
                                ++count;
                            }
                            ++i2;
                        }
                        AnwesenheitListeAO.dynacmicRowHigh(count);
                        ((DefaultTableModel)AnwesenheitListeAO.table.getModel()).setDataVector(new TabelleAnwesenheit().getFilterDataForList(selektierteMitglieder), new TabelleAnwesenheit().createHeadnameForFilter());
                        AnwesenheitListeAO.buttonStandard.setVisible(true);
                        AnwesenheitListeOptionenAO.this.dispose();
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                    AnwesenheitListeOptionenAO.this.dispose();
                }
            }
        });
        this.buttonZurueck.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                logging.logInfo((Object)"Optionsmaske wird geschlossen...");
                AnwesenheitListeOptionenAO.this.dispose();
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

