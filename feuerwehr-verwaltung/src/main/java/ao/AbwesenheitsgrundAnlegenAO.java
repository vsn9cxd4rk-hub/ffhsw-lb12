/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao;

import ao.AbstractFenster;
import ao.AbwesenheitAO;
import data.tabellen.TabelleAbwesenheitsgrund;
import go.Abwesenheitsgrund;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.logbuchEingabe;

public class AbwesenheitsgrundAnlegenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSpeichern;
    private JTextField name;
    private JTextField nameKurz;
    private JLabel name_label;
    private JLabel nameKurz_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelGrund;

    public AbwesenheitsgrundAnlegenAO() {
        super("FeuerwehrManagementSystem - Abwesenheitsgrund Erstellen");
        logging.logInfo((Object)"Starte: AbwesenheitsgrundAnlegenAO");
    }

    protected void buttonErstellen() {
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Abwesenheitsgrund Eintragen");
        this.dummy = new JLabel(new ImageIcon("images/dummy.jpg"));
        this.dummy2 = new JLabel(new ImageIcon("images/dummy.jpg"));
        this.name = new JTextField(20);
        this.nameKurz = new JTextField(20);
        this.name_label = new JLabel("Name / Grund: ");
        this.nameKurz_label = new JLabel("K\u00fcrzel: ");
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
        this.setSize(500, 180);
        this.setTitle("FeuerwehrManagementSystem - Abwesenheitsgrund Erstellen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelGrund = new JPanel(new GridLayout(2, 1));
        this.getContentPane().add("Center", this.panelGrund);
        this.panelGrund.add(this.name_label);
        this.panelGrund.add(this.name);
        this.panelGrund.add(this.nameKurz_label);
        this.panelGrund.add(this.nameKurz);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonSpeichern);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                AbwesenheitsgrundAnlegenAO.this.dispose();
            }
        });
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleAbwesenheitsgrund tabGrund = new TabelleAbwesenheitsgrund();
                Abwesenheitsgrund grund = new Abwesenheitsgrund();
                try {
                    grund.setId(tabGrund.getNextNummer());
                    grund.setName(AbwesenheitsgrundAnlegenAO.this.name.getText());
                    grund.setNameKurz(AbwesenheitsgrundAnlegenAO.this.nameKurz.getText());
                    tabGrund.insert(grund);
                    logging.logInfo((Object)"Neuer Abwesenheitsgrung gespeichert");
                    logbuchEingabe.NeuerEintag("Abwesenheitsgrund wurde erstellt: " + AbwesenheitsgrundAnlegenAO.this.name.getText());
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    if (MyEvent.event.equals("0x0019")) {
                        int i = 0;
                        while (i < AbwesenheitAO.grund.length) {
                            AbwesenheitAO.grund[i].addItem(AbwesenheitsgrundAnlegenAO.this.name.getText());
                            ++i;
                        }
                        MyEvent.setEvent((String)"0");
                        AbwesenheitsgrundAnlegenAO.this.dispose();
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

