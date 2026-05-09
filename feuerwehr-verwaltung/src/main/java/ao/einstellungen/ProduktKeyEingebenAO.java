/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  snUtilities.snUtils
 *  utilities.MyEvent
 *  utilities.hash
 */
package ao.einstellungen;

import ao.AbstractFenster;
import ao.HauptprogrammAO;
import data.tabellen.einstellungen.TabelleKeyStore;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import listener.BeendenListener;
import logging.logging;
import run.runApplication;
import snUtilities.snUtils;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.hash;

public class ProduktKeyEingebenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonBeenden;
    private JButton buttonWeiter;
    private JTextField key;
    private JLabel beschreibung;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;
    public static String letzteKategorie;

    public ProduktKeyEingebenAO() {
        super("FeuerwehrManagementSystem - Produkt-Key eingeben");
        logging.logInfo((Object)"Starte: ProduktKeyEingebenAO");
    }

    protected void buttonErstellen() {
        this.buttonWeiter = new JButton("Weiter");
        this.buttonBeenden = new JButton("Programm beenden");
        this.modulBeschreibung = new JLabel("Lizenz-Key eingeben");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.key = new JTextField(20);
        this.beschreibung = new JLabel("Produktschl\u00fcssel: ");
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
        this.setSize(500, 170);
        this.setTitle("FeuerwehrManagementSystem - Produkt-Key eingeben");
        this.setDefaultCloseOperation(3);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(1, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.beschreibung);
        this.panel.add(this.key);
        this.add(this.dummy2);
        this.add(this.buttonBeenden);
        this.add(this.buttonWeiter);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonBeenden.addActionListener((ActionListener)((Object)new BeendenListener((JFrame)((Object)this))));
        this.buttonWeiter.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleKeyStore tabKeyStroe = new TabelleKeyStore();
                if (snUtils.checkKey((String)ProduktKeyEingebenAO.this.key.getText())) {
                    ProduktKeyEingebenAO.this.dispose();
                    try {
                        tabKeyStroe.update("Nummer1", "0");
                        tabKeyStroe.update("Nummer2", hash.createHashCode((String)ProduktKeyEingebenAO.this.key.getText()));
                    }
                    catch (SQLException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                    JOptionPane.showMessageDialog(null, Konstante.PRODUKTKEY_GUELTIG);
                    if (!MyEvent.event.equals("0x0051")) {
                        try {
                            runApplication.executeStart(new String[0]);
                        }
                        catch (InterruptedException | UnknownHostException | SQLException exception) {}
                    } else {
                        MyEvent.setEvent((String)"0");
                        HauptprogrammAO.produktKeyEingeben.setEnabled(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, Konstante.PRODUKTKEY_FALSCH, "Fehlermeldung", 0);
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

