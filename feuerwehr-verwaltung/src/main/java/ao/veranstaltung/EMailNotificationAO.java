/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.SbcUtils
 */
package ao.veranstaltung;

import ao.AbstractFenster;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.email.TabelleEMail_ausgang;
import data.tabellen.mitglied.TabelleMitglied;
import go.email.Ausgang;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities_email.SendePostausgang;

public class EMailNotificationAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonZurueck;
    private JButton buttonSenden;
    private JTextArea textfiled;
    private JScrollPane scrollPane;
    private JTextField datumVon;
    private JTextField datumBis;
    private JLabel von_label;
    private JLabel bis_label;
    private JRadioButton buttonAlle;
    private JRadioButton buttonNurNeue;
    private JLabel buttonAlle_label;
    private JLabel buttonNurNeue_label;
    private ButtonGroup bg;
    private JPanel panel;
    private JLabel modulBeschreibung;
    private JLabel dummy;

    public EMailNotificationAO() {
        super("FeuerwehrManagementSystem");
        logging.logInfo((Object)"Starte: EMailNotificationAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonSenden = new JButton("Senden");
        this.datumVon = new JTextField("01." + SbcUtils.timeStamp((String)"MM.yyyy"), 20);
        this.datumBis = new JTextField("31." + SbcUtils.timeStamp((String)"MM.yyyy"), 20);
        this.von_label = new JLabel("Veranstaltungen von: ");
        this.bis_label = new JLabel("Veranstaltungen bis: ");
        this.buttonAlle = new JRadioButton();
        this.buttonNurNeue = new JRadioButton();
        this.buttonAlle_label = new JLabel("Alle Veranstaltungen im Zeitraum: ");
        this.buttonNurNeue_label = new JLabel("Nur \"NICHT\" versendete Veranstaltungen im Zeitraum: ");
        this.bg = new ButtonGroup();
        this.bg.add(this.buttonAlle);
        this.bg.add(this.buttonNurNeue);
        this.textfiled = new JTextArea();
        this.scrollPane = new JScrollPane(this.textfiled);
        this.scrollPane.setVerticalScrollBarPolicy(22);
        this.scrollPane.setPreferredSize(new Dimension(610, 300));
        this.textfiled.setText("Hallo Kameraden,\n\nf\u00fcr folgende Termine Werden noch Teilnehmer ben\u00f6tigt.\nBitte meldet euch wann Ihr zeit habt.\n\n");
        this.modulBeschreibung = new JLabel("Veranstaltungsbenachrichtigunen Senden");
        this.dummy = new JLabel(runApplication.dummyImage);
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
        this.setTitle("FeuerwehrManagementSystem - Veranstaltungsbenachrichtigung Senden");
        this.setSize(650, 520);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.panel = new JPanel(new GridLayout(4, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(this.von_label);
        this.panel.add(this.datumVon);
        this.panel.add(this.bis_label);
        this.panel.add(this.datumBis);
        this.panel.add(this.buttonAlle_label);
        this.panel.add(this.buttonAlle);
        this.panel.add(this.buttonNurNeue_label);
        this.panel.add(this.buttonNurNeue);
        Border lowerEtched = BorderFactory.createEtchedBorder(1);
        TitledBorder rahmenTextBox = BorderFactory.createTitledBorder(lowerEtched, "E-Mail Text (Einleitungstext)");
        this.scrollPane.setBorder(rahmenTextBox);
        this.add(this.scrollPane);
        this.add(this.buttonZurueck);
        this.add(this.buttonSenden);
        this.textfiled.setCaretPosition(0);
        this.textfiled.setWrapStyleWord(true);
        this.buttonNurNeue.setSelected(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonSenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                    TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
                    Ausgang ausgang = new Ausgang();
                    if (!TimeCalculation.checkDateFormat(EMailNotificationAO.this.datumVon.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                        logging.logInfo((Object)"Hoppala... Datum Von falsch eingegeben");
                        EMailNotificationAO.this.datumVon.setBackground(Color.red);
                    } else if (!TimeCalculation.checkDateFormat(EMailNotificationAO.this.datumBis.getText())) {
                        JOptionPane.showMessageDialog(null, Konstante.DATUMSFORMAT_FALSCH, "Fehlermeldung", 0);
                        logging.logInfo((Object)"Hoppala... Datum Von falsch eingegeben");
                        EMailNotificationAO.this.datumBis.setBackground(Color.red);
                    } else if (tabVeranstaltung.getCountVeranstaltungWithoutInfoVersandtInDiesemMonat(TimeCalculation.parseDateForDatabase(EMailNotificationAO.this.datumVon.getText()), TimeCalculation.parseDateForDatabase(EMailNotificationAO.this.datumBis.getText()), 0) == 0) {
                        JOptionPane.showMessageDialog(null, Konstante.KEINE_VERANSTALTUNG_GEFUNDEN, "Warnung", 2);
                    } else {
                        EMailNotificationAO.this.datumVon.setBackground(Color.white);
                        EMailNotificationAO.this.datumBis.setBackground(Color.white);
                        logging.logInfo((Object)"Habe Termine zum versenden gefunden");
                        boolean infoOptionen = false;
                        String betreff = "";
                        if (EMailNotificationAO.this.buttonAlle.isSelected()) {
                            infoOptionen = true;
                            betreff = String.valueOf(runApplication.EINSTELLUNGEN.get("Name")) + " - Alle Termine bis " + EMailNotificationAO.this.datumBis.getText();
                        } else if (EMailNotificationAO.this.buttonNurNeue.isSelected()) {
                            infoOptionen = false;
                            betreff = String.valueOf(runApplication.EINSTELLUNGEN.get("Name")) + " - Weitere Termine bis " + EMailNotificationAO.this.datumBis.getText();
                        }
                        String[] mitgliederEMailListe = Utils.listToArray(tabMitglied.getAlleMailAdressenGruppe1());
                        String[] veranstaltungsliste = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungWithoutInfoVersandtInDiesemMonat(TimeCalculation.parseDateForDatabase(EMailNotificationAO.this.datumVon.getText()), TimeCalculation.parseDateForDatabase(EMailNotificationAO.this.datumBis.getText()), infoOptionen));
                        StringBuilder nachricht = new StringBuilder();
                        nachricht.append(EMailNotificationAO.this.textfiled.getText());
                        nachricht.append("\n");
                        nachricht.append("\n");
                        int t = 0;
                        while (t < veranstaltungsliste.length) {
                            nachricht.append(veranstaltungsliste[t]);
                            ++t;
                        }
                        nachricht.append("\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                        ausgang.setAn(runApplication.EINSTELLUNGEN.get("emailAdresse"));
                        ausgang.setCc("");
                        StringBuilder build = new StringBuilder();
                        int i = 0;
                        while (i < mitgliederEMailListe.length) {
                            build.append(mitgliederEMailListe[i]);
                            build.append(", ");
                            ++i;
                        }
                        ausgang.setBcc(build.toString().substring(0, build.toString().length() - 1));
                        ausgang.setBetreff(betreff);
                        ausgang.setNachricht(nachricht.toString());
                        ausgang.setAnhang("");
                        ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                        tabAusgang.insert(ausgang);
                        tabVeranstaltung.updateInfoVersandt(TimeCalculation.parseDateForDatabase(EMailNotificationAO.this.datumVon.getText()), TimeCalculation.parseDateForDatabase(EMailNotificationAO.this.datumBis.getText()));
                        logging.logInfo((Object)"E-Mail f\u00fcr den Terminversand wurde erfolgreich in den Ausgangskorb gelegt");
                        logging.logInfo((Object)"Sende Postausgang...");
                        SendePostausgang.sendAusgang();
                        JOptionPane.showMessageDialog(null, Konstante.SENDEN_ERFOLGREICH);
                        EMailNotificationAO.this.dispose();
                    }
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    protected void labelErstellen() {
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}

