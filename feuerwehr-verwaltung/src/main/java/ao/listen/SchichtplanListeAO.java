/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.listen;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import com.itextpdf.text.DocumentException;
import data.tabellen.einstellungen.TabelleJahr;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import pdfdocumente.SchichtListePDFSchreiben;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class SchichtplanListeAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonErstellen;
    private JComboBox<String> monate;
    private JComboBox<String> jahr;
    private JLabel monat_label;
    private JLabel jahr_label;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;

    public SchichtplanListeAO() {
        super("FeuerwehrManagementSystem - \u00dcbersichtsliste");
        logging.logInfo((Object)"Starte: BeteiligungUebersichtListeAO");
    }

    protected void buttonErstellen() {
        this.buttonErstellen = new JButton("Erstellen");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.monat_label = new JLabel("Monat: ");
        this.jahr_label = new JLabel("Jahr: ");
        this.modulBeschreibung = new JLabel("\u00dcbersichtsListe");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
        try {
            TabelleJahr tabJahr = new TabelleJahr();
            String[] monatListe = new String[]{"<bitte w\u00e4hlen>", "Januar", "Februar", "M\u00e4rz", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"};
            String[] jahresListe = Utils.listToArrayOnlyFORComboBoxes(tabJahr.getAllVerf\u00fcgbarenJahre());
            this.monate = new JComboBox<String>(monatListe);
            this.jahr = new JComboBox<String>(jahresListe);
            this.jahr.setSelectedItem(SbcUtils.timeStamp((String)"yyyy"));
            this.jahr.addItem(Integer.toString(Integer.parseInt(SbcUtils.timeStamp((String)"yyyy")) + 1));
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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
        this.setSize(500, 200);
        this.setTitle("FeuerwehrManagementSystem - Stichwort");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(2, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.jahr_label);
        this.panel.add(this.jahr);
        this.panel.add(this.monat_label);
        this.panel.add(this.monate);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
        this.add(this.buttonErstellen);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonErstellen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (SchichtplanListeAO.this.monate.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
                } else if (SchichtplanListeAO.this.jahr.getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
                } else {
                    Thread threadBerichtErstellen = new Thread(){

                        @Override
                        public void run() {
                            try {
                                ProzessBarAO.progressbar.setIndeterminate(true);
                                ProzessBarAO.progressbar.setStringPainted(false);
                                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/Schichten/" + SchichtplanListeAO.this.jahr.getSelectedItem() + "_" + SchichtplanListeAO.this.monate.getSelectedItem() + ".pdf";
                                SchichtListePDFSchreiben.PDFdocumentErstellen(dateiname, SchichtplanListeAO.this.monate.getSelectedItem().toString(), SchichtplanListeAO.this.jahr.getSelectedItem().toString());
                                Utils.dateiKatalogisieren(dateiname);
                                Desktop.getDesktop().open(new File(dateiname));
                                MyEvent.setEvent((String)"0x0030");
                                SchichtplanListeAO.this.dispose();
                            }
                            catch (DocumentException | IOException | SQLException e) {
                                logging.logPrintStackTrace((Exception)e);
                            }
                        }
                    };
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    threadBerichtErstellen.start();
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

