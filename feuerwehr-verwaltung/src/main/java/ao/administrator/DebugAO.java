/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 *  utilities.MyEvent
 *  utilities.MyProperties
 */
package ao.administrator;

import ao.AbstractFenster;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import logging.logging;
import run.runApplication;
import thread.DebugThread;
import utilities.MyEvent;
import utilities.MyProperties;
import utilities.logbuchEingabe;

public class DebugAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonBeenden;
    private JButton buttonstart;
    private JButton buttonstop;
    public static JTextArea textarea;
    public static JScrollPane pane;
    private JLabel logo;
    private JLabel dummy;
    private JLabel dummy2;

    public DebugAO() {
        super("FeuerwehrManagementSystem - Debug Viewer");
        logging.logInfo((Object)"Starte: DebugAO");
    }

    protected void buttonErstellen() {
        this.buttonBeenden = new JButton("Zur\u00fcck");
        this.buttonBeenden.setToolTipText("Schlie\u00dfen");
        this.buttonstart = new JButton("Start");
        this.buttonstop = new JButton("Stopp");
        textarea = new JTextArea(26, 108);
        textarea.setEditable(false);
        pane = new JScrollPane(textarea);
        pane.setVerticalScrollBarPolicy(22);
        this.logo = new JLabel(runApplication.bannerHauptprogramm);
        this.dummy = new JLabel(runApplication.dummyImage);
        this.dummy2 = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        MyProperties properties = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/logging.properties");
        properties.loadVars();
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Debug Viewer");
        this.setSize(1280, 768);
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.logo);
        this.add(this.dummy);
        this.add(this.buttonstart);
        this.add(this.buttonstop);
        this.buttonstop.setEnabled(false);
        this.add(pane);
        this.add(this.dummy2);
        this.add(this.buttonBeenden);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonBeenden.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                MyEvent.setEvent((String)"0x0401");
                logging.logInfo((Object)"Schie\u00dfe Fenster - DebugAO");
                logbuchEingabe.NeuerEintag("Schie\u00dfe Fenster - DebugAO");
                DebugAO.this.dispose();
            }
        });
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                DebugAO.this.buttonBeenden.doClick();
            }
        });
        this.buttonstart.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Thread thread = new Thread(){

                    @Override
                    public void run() {
                        MyEvent.setEvent((String)"0");
                        DebugThread.run();
                    }
                };
                if (MyEvent.event.equals("0x0401")) {
                    MyEvent.setEvent((String)"0");
                } else {
                    DebugAO.this.buttonstart.setEnabled(false);
                    DebugAO.this.buttonstop.setEnabled(true);
                    thread.start();
                }
            }
        });
        this.buttonstop.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                MyEvent.setEvent((String)"0x0401");
                DebugAO.this.buttonstop.setEnabled(false);
                DebugAO.this.buttonstart.setEnabled(true);
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

