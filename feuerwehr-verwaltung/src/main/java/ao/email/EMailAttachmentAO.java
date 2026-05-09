/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.email;

import ao.AbstractFenster;
import ao.email.EMailModulAO;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;

public class EMailAttachmentAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static String dataForMail = null;
    private JButton buttonZurueck;
    private JButton buttonAnsehen;
    private JButton buttonDrucken;
    private JList liste;
    private JScrollPane pane_liste;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    private JLabel dummy2;

    public EMailAttachmentAO() {
        super("FeuerwehrManagementSystem - E-Mail Anhang ansehen");
        logging.logInfo((Object)"Starte: AttachmentAO");
    }

    protected void buttonErstellen() {
        this.buttonZurueck = new JButton("Zur\u00fcck");
        this.buttonAnsehen = new JButton("\u00d6ffnen");
        this.buttonDrucken = new JButton("Drucken");
        this.modulBeschreibung = new JLabel("E-Mail Anhang ansehen (Nachricht: " + EMailModulAO.staticAttachment.getId() + ", " + EMailModulAO.staticAttachment.getOrdner() + ")");
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        this.liste = new JList();
        this.liste.setVisibleRowCount(15);
        this.liste.setToolTipText("Liste der verf\u00fcgbaren Anh\u00e4nge");
        this.pane_liste = new JScrollPane(this.liste);
        this.pane_liste.setVerticalScrollBarPolicy(22);
        this.pane_liste.setPreferredSize(new Dimension(600, 200));
    }

    protected void boxenHinzufuegen() {
        File ordner = null;
        switch (EMailModulAO.staticAttachment.getOrdner()) {
            case "Posteingang Ungelesen": {
                ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Empfangende/" + EMailModulAO.staticAttachment.getId());
                logging.logInfo((Object)("Ordner: " + ordner));
                break;
            }
            case "Posteingang Gelesen": {
                ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Empfangende/" + EMailModulAO.staticAttachment.getId());
                logging.logInfo((Object)("Ordner: " + ordner));
                break;
            }
            case "Gesendete Objekte": {
                ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Gesendet/" + EMailModulAO.staticAttachment.getId());
                logging.logInfo((Object)("Ordner: " + ordner));
                break;
            }
            case "Entwurf": {
                ordner = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/Anhang/Entwurf/" + EMailModulAO.staticAttachment.getId());
                logging.logInfo((Object)("Ordner: " + ordner));
            }
        }
        File[] files = ordner.listFiles();
        this.liste.setListData(files);
        EMailModulAO.staticAttachment = null;
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - E-Mail Anhang ansehen");
        this.setSize(650, 380);
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.pane_liste);
        this.add(this.buttonAnsehen);
        this.add(this.buttonDrucken);
        this.add(this.dummy2);
        this.add(this.buttonZurueck);
    }

    protected void labelHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.liste.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    EMailAttachmentAO.this.buttonAnsehen.doClick();
                }
            }
        });
        this.buttonDrucken.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String dateiname = EMailAttachmentAO.this.liste.getSelectedValue().toString();
                    Desktop.getDesktop().print(new File(dateiname));
                }
                catch (IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonAnsehen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    String dateiname = EMailAttachmentAO.this.liste.getSelectedValue().toString();
                    Desktop.getDesktop().open(new File(dateiname));
                }
                catch (IOException e) {
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

