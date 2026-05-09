/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.ausbildung;

import ao.AbstractFenster;
import ao.ausbildung.AusbildungsplanAO;
import data.tabellen.TabelleVeranstaltung;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.Utils;
import utilities.logbuchEingabe;

public class AusbildungsinhaltTauschenAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonTauschen;
    private JComboBox<String> dienst1;
    private JComboBox<String> dienst2;
    private JLabel dienst1_label;
    private JLabel dienst2_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panelKategorie;
    public static String letzterDienstgrad;

    public AusbildungsinhaltTauschenAO() {
        super("FeuerwehrManagementSystem - Ausbildungsinhalte Tauschen");
        logging.logInfo((Object)("Starte: " + this.getName()));
    }

    protected void buttonErstellen() {
        this.buttonTauschen = new JButton("Tauschen");
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.modulBeschreibung = new JLabel("Ausbildungsinhalte Tauschen");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
        this.dienst1_label = new JLabel("Tauschen von: ");
        this.dienst2_label = new JLabel("Tauschen nach: ");
        try {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            String[] vListe = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltungEinerKategorieByJahr(2, Integer.parseInt(AusbildungsplanAO.jahre.getSelectedItem().toString())));
            this.dienst1 = new JComboBox<String>(vListe);
            this.dienst2 = new JComboBox<String>(vListe);
        }
        catch (NumberFormatException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.setSize(600, 190);
        this.setTitle("FeuerwehrManagementSystem - Ausbildungsinhalte Tauschen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panelKategorie = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panelKategorie);
        this.panelKategorie.add(this.dienst1_label);
        this.panelKategorie.add(this.dienst1);
        this.panelKategorie.add(this.dienst2_label);
        this.panelKategorie.add(this.dienst2);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonTauschen);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonTauschen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (AusbildungsinhaltTauschenAO.this.dienst1.getSelectedItem().toString().equals("<bitte w\u00e4hlen>") | AusbildungsinhaltTauschenAO.this.dienst2.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.KEIN_TAUSCHEN_MOEGLICH, "Warnung", 2);
                } else if (AusbildungsinhaltTauschenAO.this.dienst1.getSelectedItem().toString().equals(AusbildungsinhaltTauschenAO.this.dienst2.getSelectedItem().toString())) {
                    JOptionPane.showMessageDialog(null, Konstante.KEIN_TAUSCHEN_MOEGLICH, "Warnung", 2);
                } else {
                    int selectedValueDienst1 = AusbildungsinhaltTauschenAO.this.dienst1.getSelectedIndex() - 1;
                    int selectedValueDienst2 = AusbildungsinhaltTauschenAO.this.dienst2.getSelectedIndex() - 1;
                    String[] dienst1Selected = new String[]{AusbildungsplanAO.ausbildungkategorien[selectedValueDienst1].getSelectedItem().toString(), AusbildungsplanAO.details[selectedValueDienst1].getText(), AusbildungsplanAO.ausbilder1[selectedValueDienst1].getSelectedItem().toString(), AusbildungsplanAO.ausbilder2[selectedValueDienst1].getSelectedItem().toString()};
                    String[] dienst2Selected = new String[]{AusbildungsplanAO.ausbildungkategorien[selectedValueDienst2].getSelectedItem().toString(), AusbildungsplanAO.details[selectedValueDienst2].getText(), AusbildungsplanAO.ausbilder1[selectedValueDienst2].getSelectedItem().toString(), AusbildungsplanAO.ausbilder2[selectedValueDienst2].getSelectedItem().toString()};
                    AusbildungsplanAO.ausbildungkategorien[selectedValueDienst2].setSelectedItem(dienst1Selected[0].toString());
                    AusbildungsplanAO.details[selectedValueDienst2].setText(dienst1Selected[1].toString());
                    AusbildungsplanAO.ausbilder1[selectedValueDienst2].setSelectedItem(dienst1Selected[2].toString());
                    AusbildungsplanAO.ausbilder2[selectedValueDienst2].setSelectedItem(dienst1Selected[3].toString());
                    AusbildungsplanAO.ausbildungkategorien[selectedValueDienst1].setSelectedItem(dienst2Selected[0].toString());
                    AusbildungsplanAO.details[selectedValueDienst1].setText(dienst2Selected[1].toString());
                    AusbildungsplanAO.ausbilder1[selectedValueDienst1].setSelectedItem(dienst2Selected[2].toString());
                    AusbildungsplanAO.ausbilder2[selectedValueDienst1].setSelectedItem(dienst2Selected[3].toString());
                    logging.logInfo((Object)("Dienst: " + AusbildungsinhaltTauschenAO.this.dienst1.getSelectedItem().toString() + " wurde gegen: " + AusbildungsinhaltTauschenAO.this.dienst2.getSelectedItem().toString() + " getauscht"));
                    logbuchEingabe.NeuerEintag("Dienst: " + AusbildungsinhaltTauschenAO.this.dienst1.getSelectedItem().toString() + " wurde gegen: " + AusbildungsinhaltTauschenAO.this.dienst2.getSelectedItem().toString() + " getauscht");
                    AusbildungsinhaltTauschenAO.this.dispose();
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

