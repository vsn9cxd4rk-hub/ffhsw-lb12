/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 */
package ao.utils;

import ao.AbstractFenster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import logging.logging;
import run.runApplication;
import utilities.MyEvent;

public class ProzessBarAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    public static JProgressBar progressbar;
    public static JLabel label_bitteWarten;
    private JLabel dummyLabel;

    public ProzessBarAO() {
        super("FeuerwehrManagementSystem - Bitte warten...");
        logging.logInfo((Object)"Starte: ProzessBarAO");
    }

    protected void buttonErstellen() {
        progressbar = new JProgressBar();
        label_bitteWarten = new JLabel("Bitte warten...");
        this.dummyLabel = new JLabel("                                ");
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
        this.setTitle("FeuerwehrManagementSystem - Bitte warten...");
        this.setDefaultCloseOperation(0);
        this.setSize(400, 130);
        this.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.dummyLabel);
        this.add(progressbar);
        this.add(label_bitteWarten);
    }

    protected void boxenHinzufuegen() {
        progressbar.setPreferredSize(new Dimension(350, 20));
        progressbar.setStringPainted(true);
    }

    protected void labelErstellen() {
        Thread thread = new Thread(){

            @Override
            public void run() {
                while (true) {
                    if (MyEvent.event.equals("0x0030")) break;
                    try {
                        Thread.sleep(200L);
                    }
                    catch (InterruptedException e) {
                        logging.logPrintStackTrace((Exception)e);
                    }
                }
                logging.logInfo((Object)"Thread wird beendet..");
                MyEvent.setEvent((String)"0");
                ProzessBarAO.this.dispose();
            }
        };
        thread.start();
    }

    protected void actionErzeugen() {
    }

    public void fensterAnzeigen() {
        this.setAlwaysOnTop(false);
        this.setUndecorated(true);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    public void fensterSchlissen() {
        this.dispose();
    }
}

