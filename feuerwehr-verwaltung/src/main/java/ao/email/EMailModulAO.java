/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.email;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import data.tabellen.email.TabelleEMail_empfangen;
import data.tabellen.email.TabelleEMail_entwurf;
import data.tabellen.email.TabelleEMail_gesendet;
import go.email.Antworten;
import go.email.Attachment;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.TimeCalculation;
import utilities_email.DeleteAndDisplayEMail;
import utilities_email.EMail_utils;
import utilities_email.EmpfangenOpperation;

public class EMailModulAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static Antworten staticAntworten = null;
    public static Attachment staticAttachment = null;
    private JButton buttonDelete;
    private JButton buttonNeueEMail;
    private JButton buttonEmpfangen;
    private JButton buttonSignatur;
    public static JButton buttonAttachment;
    private JButton buttonAntworten;
    private JButton buttonWeiterleiten;
    private JButton buttonBearbeiten;
    public static JList<Object> mailListe;
    private JScrollPane mailListe_ungelesen_pane;
    public static JTree tree;
    private JScrollPane scrollPaneTree;
    public static JTextPane textfeld_email;
    private JScrollPane pane;
    public static JLabel status;
    private JLabel dummy;

    public EMailModulAO() {
        super("FeuerwehrManagementSystem");
        logging.logInfo((Object)"Starte: EMailModulAO");
    }

    protected void buttonErstellen() {
        this.buttonDelete = new JButton("L\u00f6schen");
        this.buttonNeueEMail = new JButton("Neue E-Mail schreiben");
        this.buttonSignatur = new JButton("Signatur erstellen");
        this.buttonEmpfangen = new JButton("E-Mails Empfangen");
        buttonAttachment = new JButton("Anhang ansehen");
        this.buttonAntworten = new JButton("Antworten");
        this.buttonWeiterleiten = new JButton("Weiterleiten");
        this.buttonBearbeiten = new JButton("Bearbeiten");
        tree = new JTree(CreateTrees.CreateEMailTree());
        tree.setSelectionRow(1);
        this.scrollPaneTree = new JScrollPane(tree);
        this.scrollPaneTree.setVerticalScrollBarPolicy(22);
        textfeld_email = new JTextPane();
        this.pane = new JScrollPane(textfeld_email);
        this.pane.setVerticalScrollBarPolicy(22);
        this.dummy = new JLabel(runApplication.dummyImage);
        status = new JLabel();
        mailListe = new JList();
        this.mailListe_ungelesen_pane = new JScrollPane(mailListe);
        mailListe.setVisibleRowCount(15);
        this.mailListe_ungelesen_pane.setVerticalScrollBarPolicy(22);
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
        this.setTitle("FeuerwehrManagementSystem - E-Mail Modul");
        this.setSize(1200, 800);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                logging.logInfo((Object)"E-Mail Modul beenden");
            }
        });
    }

    protected void buttonHinzufuegen() {
        this.scrollPaneTree.setPreferredSize(new Dimension(200, 700));
        this.mailListe_ungelesen_pane.setPreferredSize(new Dimension(300, 700));
        this.pane.setPreferredSize(new Dimension(600, 700));
        textfeld_email.setEditable(false);
        this.add(this.buttonNeueEMail);
        this.add(this.buttonBearbeiten);
        this.add(this.buttonAntworten);
        this.add(this.buttonWeiterleiten);
        this.add(buttonAttachment);
        this.add(this.buttonDelete);
        this.add(this.buttonSignatur);
        this.add(this.dummy);
        this.add(this.scrollPaneTree);
        this.add(this.mailListe_ungelesen_pane);
        this.add(this.pane);
        this.add(status);
        buttonAttachment.setVisible(false);
        this.buttonBearbeiten.setVisible(false);
        tree.setSelectionRow(0);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelErstellen() {
        buttonAttachment.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                staticAttachment = new Attachment();
                int nID = Integer.parseInt(mailListe.getSelectedValue().toString().substring(11, mailListe.getSelectedValue().toString().length()));
                staticAttachment.setId(nID);
                staticAttachment.setOrdner(tree.getSelectionPath().getLastPathComponent().toString());
                Steuerung.setStatus(Status.EMAIL_ANHANG);
                Steuerung.steuerung();
            }
        });
        this.buttonAntworten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleEMail_empfangen tabEmpf = new TabelleEMail_empfangen();
                TabelleEMail_gesendet tabGes = new TabelleEMail_gesendet();
                staticAntworten = new Antworten();
                try {
                    int nID = Integer.parseInt(mailListe.getSelectedValue().toString().substring(11, mailListe.getSelectedValue().toString().length()));
                    switch (tree.getSelectionPath().getLastPathComponent().toString()) {
                        case "Posteingang Ungelesen": {
                            staticAntworten.setBetreff("Aw: " + tabEmpf.getBetreff(nID));
                            staticAntworten.setNachricht(tabEmpf.getNachricht(nID));
                            staticAntworten.setSender(tabEmpf.getSender(nID));
                            staticAntworten.setDate(tabEmpf.getDate(nID));
                            break;
                        }
                        case "Posteingang Gelesen": {
                            staticAntworten.setBetreff("Aw: " + tabEmpf.getBetreff(nID));
                            staticAntworten.setNachricht(tabEmpf.getNachricht(nID));
                            staticAntworten.setSender(tabEmpf.getSender(nID));
                            staticAntworten.setDate(tabEmpf.getDate(nID));
                            break;
                        }
                        case "Gesendete Objekte": {
                            staticAntworten.setBetreff("Aw: " + tabGes.getBetreff(nID));
                            staticAntworten.setNachricht(tabGes.getNachricht(nID));
                            staticAntworten.setSender(tabGes.getSender(nID));
                            staticAntworten.setDate(TimeCalculation.parseDateForGUI(tabGes.getDate(nID)));
                        }
                    }
                    Steuerung.setStatus(Status.NEUE_EMAIL);
                    Steuerung.steuerung();
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonBearbeiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleEMail_entwurf tabEntwurf = new TabelleEMail_entwurf();
                    int nID = Integer.parseInt(mailListe.getSelectedValue().toString().substring(11, mailListe.getSelectedValue().toString().length()));
                    runApplication.mailData.setStatus(1);
                    HashMap<String, String> map = tabEntwurf.getEntwurfMail(nID);
                    if (map.get("anhang").equals("")) {
                        runApplication.mailData.setAnhang("");
                    } else {
                        runApplication.mailData.setAnhang(map.get("anhang").substring(0, map.get("anhang").length()));
                    }
                    runApplication.mailData.setAn(map.get("AN"));
                    runApplication.mailData.setCc(map.get("CC"));
                    runApplication.mailData.setBcc(map.get("BCC"));
                    runApplication.mailData.setBetreff(map.get("betreff"));
                    runApplication.mailData.seteMailText(map.get("nachricht"));
                    tabEntwurf.deleteNachricht(nID);
                    Steuerung.setStatus(Status.NEUE_EMAIL);
                    Steuerung.steuerung();
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonWeiterleiten.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                TabelleEMail_empfangen tabEmpf = new TabelleEMail_empfangen();
                TabelleEMail_gesendet tabGes = new TabelleEMail_gesendet();
                staticAntworten = new Antworten();
                try {
                    int nID = Integer.parseInt(mailListe.getSelectedValue().toString().substring(11, mailListe.getSelectedValue().toString().length()));
                    switch (tree.getSelectionPath().getLastPathComponent().toString()) {
                        case "Posteingang Ungelesen": {
                            staticAntworten.setBetreff("Wg: " + tabEmpf.getBetreff(nID));
                            staticAntworten.setNachricht(tabEmpf.getNachricht(nID));
                            staticAntworten.setSender("");
                            staticAntworten.setDate(tabEmpf.getDate(nID));
                            break;
                        }
                        case "Posteingang Gelesen": {
                            staticAntworten.setBetreff("Wg: " + tabEmpf.getBetreff(nID));
                            staticAntworten.setNachricht(tabEmpf.getNachricht(nID));
                            staticAntworten.setSender("");
                            staticAntworten.setDate(tabEmpf.getDate(nID));
                            break;
                        }
                        case "Gesendete Objekte": {
                            staticAntworten.setBetreff("Wg: " + tabGes.getBetreff(nID));
                            staticAntworten.setNachricht(tabGes.getNachricht(nID));
                            staticAntworten.setSender("");
                            staticAntworten.setDate(TimeCalculation.parseDateForGUI(tabGes.getDate(nID)));
                        }
                    }
                    Steuerung.setStatus(Status.NEUE_EMAIL);
                    Steuerung.steuerung();
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonEmpfangen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                ProzessBarAO.progressbar.setStringPainted(false);
                ProzessBarAO.progressbar.setIndeterminate(true);
                ProzessBarAO.label_bitteWarten.setText("E-Mails werden vom Server abgerufen... Bitte warten...");
                Thread threadEmpf = new Thread(){

                    @Override
                    public void run() {
                        try {
                            EmpfangenOpperation.empfangen();
                            EMail_utils.refreshMailsErhalten(0);
                            MyEvent.setEvent((String)"0x0030");
                        }
                        catch (Exception e) {
                            MyEvent.setEvent((String)"0x0030");
                            JOptionPane.showMessageDialog(null, Konstante.FEHLER_EMAIL_EMPFANGEN, "Fehlermeldung", 0);
                            logging.logPrintStackTrace((Exception)e);
                        }
                    }
                };
                threadEmpf.start();
            }
        });
        this.buttonSignatur.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.EMAIL_SIGNATUR);
                Steuerung.steuerung();
            }
        });
        this.buttonDelete.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                DeleteAndDisplayEMail disp = new DeleteAndDisplayEMail();
                disp.delete();
            }
        });
        tree.addTreeSelectionListener(new TreeSelectionListener(){

            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                switch (tree.getSelectionPath().getLastPathComponent().toString()) {
                    case "Posteingang Ungelesen": {
                        EMail_utils.refreshMailsErhalten(0);
                        EMailModulAO.this.buttonBearbeiten.setVisible(false);
                        break;
                    }
                    case "Posteingang Gelesen": {
                        EMail_utils.refreshMailsErhalten(1);
                        EMailModulAO.this.buttonBearbeiten.setVisible(false);
                        break;
                    }
                    case "Postausgang": {
                        EMail_utils.refreshMailsPostausgang();
                        EMailModulAO.this.buttonBearbeiten.setVisible(false);
                        break;
                    }
                    case "Gesendete Objekte": {
                        EMail_utils.refreshMailsGesendet();
                        EMailModulAO.this.buttonBearbeiten.setVisible(false);
                        break;
                    }
                    case "Entwurf": {
                        EMail_utils.refreshMailsEntwurf();
                        EMailModulAO.this.buttonBearbeiten.setVisible(true);
                    }
                }
                textfeld_email.setText(null);
            }
        });
    }

    protected void actionErzeugen() {
        mailListe.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                DeleteAndDisplayEMail mail = new DeleteAndDisplayEMail();
                mail.display();
            }
        });
        this.buttonNeueEMail.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Steuerung.setStatus(Status.NEUE_EMAIL);
                Steuerung.steuerung();
            }
        });
    }

    public void fensterAnzeigen() {
        if (runApplication.EINSTELLUNGEN.get("AlwaysOnTop").equals("1")) {
            this.setAlwaysOnTop(true);
        }
        this.setVisible(true);
        EMail_utils.refreshMailsErhalten(0);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }
}

