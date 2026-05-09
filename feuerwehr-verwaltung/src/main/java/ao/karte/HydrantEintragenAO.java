/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.karte;

import ao.AbstractFenster;
import data.tabellen.karte.TabelleHydranten;
import data.tabellen.karte.TabelleStrassen;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import keyBoardListener.EnterKeyboardSpeichernHydrantListener;
import listener.DisposeListener;
import listener.HydrantSpeichernListener;
import logging.logging;
import run.runApplication;
import utilities.Utils;
import utilities.logbuchEingabe;

public class HydrantEintragenAO
extends AbstractFenster {
    public static JLabel nennweite_label;
    public static JTextField nennweite;
    public static JLabel hausnummer_label;
    public static JTextField hausnummer;
    public static JComboBox<String> StrassenName;
    private JLabel StrassenNamen_label;
    public static JComboBox<String> StrassenName2;
    private String strassendb;
    public static JComboBox<String> lageHydrant;
    private JLabel lageHydrant_label;
    private JLabel lageWO_label;
    private JButton buttonSpeichern;
    private JButton buttonZurueck;
    private JButton buttonLetztenDatensatzLoeschen;
    public static JLabel insertErfolgreich;
    private JPanel panel2;
    private JLabel dummy2;

    public HydrantEintragenAO() {
        super("FeuerwehrManagementSystem Version: 3.21");
        logging.logInfo((Object)"Starte: HydrantenEintragenAO");
    }

    protected void buttonErstellen() {
        this.panel2 = new JPanel();
        this.StrassenNamen_label = new JLabel("Stra\u00dfe: ");
        this.lageHydrant_label = new JLabel("Lage: ");
        this.lageWO_label = new JLabel("Lage Wo: ");
        nennweite_label = new JLabel("Nennweite: ");
        nennweite = new JTextField(25);
        hausnummer_label = new JLabel("Hausnummer: ");
        hausnummer = new JTextField(25);
        this.buttonSpeichern = new JButton("Speichern");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonLetztenDatensatzLoeschen = new JButton("letzten Hydrant l\u00f6schen");
        insertErfolgreich = new JLabel();
        this.dummy2 = new JLabel(new ImageIcon("images/dummy.jpg"));
    }

    protected void setzeAuswahllisten() {
        TabelleStrassen dbsta\u00dfen = new TabelleStrassen();
        String[] strassenListe = null;
        this.strassendb = null;
        try {
            strassenListe = Utils.listToArrayOnlyFORComboBoxes(dbsta\u00dfen.getStra\u00dfenListe());
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        StrassenName = new JComboBox<String>(strassenListe);
        StrassenName2 = new JComboBox<String>(strassenListe);
        StrassenName.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    HydrantEintragenAO.this.strassendb = (String)StrassenName.getSelectedItem();
                }
            }
        });
        String[] lage = new String[]{"<bitte w\u00e4hlen>", "Ecke ", "Kreuzung", "Einm\u00fcndung"};
        lageHydrant = new JComboBox<String>(lage);
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Hydrant Eintragen");
        this.setSize(630, 250);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
    }

    protected void boxenHinzufuegen() {
        this.panel2 = new JPanel(new GridLayout(5, 2));
        this.getContentPane().add("Center", this.panel2);
        this.panel2.add(this.StrassenNamen_label);
        this.panel2.add(StrassenName);
        this.panel2.add(this.lageHydrant_label);
        this.panel2.add(lageHydrant);
        this.panel2.add(this.lageWO_label);
        this.panel2.add(StrassenName2);
        this.panel2.add(hausnummer_label);
        this.panel2.add(hausnummer);
        this.panel2.add(nennweite_label);
        this.panel2.add(nennweite);
        this.add(this.buttonSpeichern);
        this.add(this.buttonLetztenDatensatzLoeschen);
        this.add(this.buttonZurueck);
        this.add(this.dummy2);
        this.add(insertErfolgreich);
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSpeichern.addActionListener((ActionListener)((Object)new HydrantSpeichernListener((JFrame)((Object)this))));
        nennweite.addKeyListener(new EnterKeyboardSpeichernHydrantListener((JFrame)((Object)this)));
        hausnummer.addKeyListener(new EnterKeyboardSpeichernHydrantListener((JFrame)((Object)this)));
        this.buttonLetztenDatensatzLoeschen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                TabelleHydranten hydrant = new TabelleHydranten();
                try {
                    hydrant.deleteLastEntry();
                    logbuchEingabe.NeuerEintag("Letzter Hydrant wurde gel\u00f6scht");
                    insertErfolgreich.setText("letzter Hydrant wurde gel\u00f6scht");
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

