/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  javax.mail.MessagingException
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.SbcUtils
 */
package ao.email;

import ao.AbstractFenster;
import ao.email.EMailModulAO;
import ao.utils.ProzessBarAO;
import data.tabellen.email.TabelleEMail_entwurf;
import go.email.Entwurf;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities_email.ErstelleFileArrayForAnhang;
import utilities_email.SendeOpperation;

public class NeueEMailAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private int alsEntwurfBereitsGespeichert = 0;
    private JButton buttonSenden;
    private JButton buttonSpeichern;
    private JButton buttonAdressbuch;
    private JButton buttonAnhang;
    public static JTextArea textfeld;
    private JScrollPane pane;
    public static JTextField fieldAn;
    private JLabel fieldAn_label;
    public static JTextField fieldCC;
    private JLabel fieldCC_label;
    public static JTextField fieldBCC;
    private JLabel fieldBCC_label;
    public static JTextField fieldAnhang;
    private JLabel fieldAnhnag_label;
    public static JTextField fieldBetreff;
    private JLabel fieldBetreff_label;
    private JLabel dummy;
    private JLabel dummy2;
    private JPanel panel;
    private JPanel panelText;

    public NeueEMailAO() {
        super("FeuerwehrManagementSystem - Neue Email");
        logging.logInfo((Object)"Starte: NeueEMailAO");
    }

    protected void buttonErstellen() {
        this.buttonSenden = new JButton("Senden");
        this.buttonAdressbuch = new JButton("Adressbuch");
        this.buttonAnhang = new JButton("Anhang anf\u00fcgen");
        this.buttonSpeichern = new JButton("Speichern");
        fieldAn = new JTextField(65);
        this.fieldAn_label = new JLabel("    An: ");
        fieldCC = new JTextField(65);
        this.fieldCC_label = new JLabel("    CC: ");
        fieldBCC = new JTextField(65);
        this.fieldBCC_label = new JLabel("    BCC: ");
        fieldAnhang = new JTextField(65);
        this.fieldAnhnag_label = new JLabel("    Anhang: ");
        fieldBetreff = new JTextField(65);
        this.fieldBetreff_label = new JLabel("    Betreff: ");
        textfeld = new JTextArea();
        this.pane = new JScrollPane(textfeld);
        this.pane.setVerticalScrollBarPolicy(22);
        this.pane.setPreferredSize(new Dimension(900, 500));
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        textfeld.setText("\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
        if (runApplication.mailData.getStatus() == 1) {
            fieldAnhang.setText(runApplication.mailData.getAnhang());
            fieldAn.setText(runApplication.mailData.getAn());
            fieldCC.setText(runApplication.mailData.getCc());
            fieldBCC.setText(runApplication.mailData.getBcc());
            fieldBetreff.setText(runApplication.mailData.getBetreff());
            textfeld.setText(runApplication.mailData.geteMailText());
            runApplication.mailData.setStatus(0);
        }
        if (EMailModulAO.staticAntworten != null) {
            textfeld.setText("\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur") + "\n\n\n--------------------------------------------------- Original Nachricht ---------------------------------------------------\nVon:  " + EMailModulAO.staticAntworten.getSender() + "\nDatum:  " + EMailModulAO.staticAntworten.getDate() + "\n\n" + EMailModulAO.staticAntworten.getNachricht().toString());
            fieldBetreff.setText(EMailModulAO.staticAntworten.getBetreff().toString());
            fieldAn.setText(EMailModulAO.staticAntworten.getSender().toString());
            EMailModulAO.staticAntworten = null;
        }
    }

    protected void labelHinzufuegen() {
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Neue Email");
        this.setSize(1000, 750);
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                logging.logInfo((Object)"Neue Email beenden");
                NeueEMailAO.this.dispose();
            }
        });
    }

    protected void buttonHinzufuegen() {
        this.buttonSenden.setPreferredSize(new Dimension(100, 100));
        this.add(this.buttonSpeichern);
        this.add(this.buttonAdressbuch);
        this.add(this.buttonAnhang);
        this.add(this.dummy);
        this.add(this.buttonSenden);
        this.panelText = new JPanel(new GridLayout(5, 2));
        this.getContentPane().add("Center", this.panelText);
        this.panelText.add(this.fieldAn_label);
        this.panelText.add(this.fieldCC_label);
        this.panelText.add(this.fieldBCC_label);
        this.panelText.add(this.fieldBetreff_label);
        this.panelText.add(this.fieldAnhnag_label);
        this.panel = new JPanel(new GridLayout(5, 2));
        this.getContentPane().add("Center", this.panel);
        this.panel.add(fieldAn);
        this.panel.add(fieldCC);
        this.panel.add(fieldBCC);
        this.panel.add(fieldBetreff);
        this.panel.add(fieldAnhang);
        this.add(this.dummy2);
        this.add(this.pane);
        fieldAnhang.setEditable(false);
        textfeld.setCaretPosition(0);
        textfeld.setWrapStyleWord(true);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonSpeichern.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleEMail_entwurf tabEntwurf = new TabelleEMail_entwurf();
                    Entwurf entwurf = new Entwurf();
                    int nextId = tabEntwurf.getNextNummer();
                    if (NeueEMailAO.this.alsEntwurfBereitsGespeichert == 0) {
                        entwurf.setId(nextId);
                    } else {
                        tabEntwurf.deleteNachricht(NeueEMailAO.this.alsEntwurfBereitsGespeichert);
                        entwurf.setId(NeueEMailAO.this.alsEntwurfBereitsGespeichert);
                    }
                    entwurf.setAn(fieldAn.getText());
                    entwurf.setCc(fieldCC.getText());
                    entwurf.setBcc(fieldBCC.getText());
                    entwurf.setBetreff(fieldBetreff.getText());
                    entwurf.setNachricht(textfeld.getText());
                    entwurf.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    entwurf.setAnhang(Utils.removeBackSlashFromString(fieldAnhang.getText()));
                    NeueEMailAO.this.alsEntwurfBereitsGespeichert = nextId;
                    tabEntwurf.insert(entwurf);
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAdressbuch.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.EMAIL_ADRESSBUCH);
                Steuerung.steuerung();
            }
        });
        this.buttonSenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (fieldAn.getText().equals("") | fieldAn.getText() == null) {
                    JOptionPane.showMessageDialog(null, Konstante.BITTE_EMPAENGER_EINGEBEN, "Warnung", 2);
                } else {
                    Steuerung.setStatus(Status.PROZESSBAR);
                    Steuerung.steuerung();
                    ProzessBarAO.progressbar.setStringPainted(false);
                    ProzessBarAO.progressbar.setIndeterminate(true);
                    ProzessBarAO.label_bitteWarten.setText("EMail wird gesendet... Bitte haben sie einen Moment Geduld...");
                    Thread threadSenden = new Thread(){

                        @Override
                        public void run() {
                            try {
                                SendeOpperation.senden(fieldAn.getText(), fieldCC.getText(), fieldBCC.getText(), fieldBetreff.getText(), textfeld.getText(), ErstelleFileArrayForAnhang.analysiereString(fieldAnhang.getText()));
                                MyEvent.setEvent((String)"0x0030");
                                logbuchEingabe.NeuerEintag("E-Mail am " + fieldAn.getText());
                                JOptionPane.showMessageDialog(null, Konstante.SENDEN_ERFOLGREICH);
                                NeueEMailAO.this.dispose();
                            }
                            catch (UnsupportedEncodingException | SQLException | MessagingException e) {
                                MyEvent.setEvent((String)"0x0030");
                                JOptionPane.showMessageDialog(null, "Das senden der E-Mail ist Fehlgeschlagen", "Fehlermeldung", 0);
                            }
                        }
                    };
                    threadSenden.start();
                }
            }
        });
        this.buttonAnhang.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(chooser);
                if (returnVal == 0) {
                    logging.logInfo((Object)("Ausgew\u00e4hlte Datei: " + chooser.getSelectedFile().getPath()));
                }
                fieldAnhang.setText(String.valueOf(fieldAnhang.getText()) + chooser.getSelectedFile().getPath() + ",");
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
}

