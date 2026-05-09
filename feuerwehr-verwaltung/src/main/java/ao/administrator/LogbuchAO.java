/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.administrator;

import ao.AbstractFenster;
import data.tabellen.TabelleLogbuch;
import data.tabellen.einstellungen.TabelleUser;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Utils;
import utilities.logbuchEingabe;

public class LogbuchAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private JButton buttonBeenden;
    private JButton buttonDebugViewer;
    private JLabel filter_label;
    private JComboBox<String> filteruser;
    public static String filteruserdb;
    private DefaultTableModel defaultTableModelLogbuch;
    private JTable table;
    private JLabel logo;
    private JLabel dummy;

    public LogbuchAO() {
        super("FeuerwehrManagementSystem - Logbuch");
        logging.logInfo((Object)"Starte: LogbuchAO");
    }

    protected void buttonErstellen() {
        this.buttonBeenden = new JButton("Schlie\u00dfen");
        this.buttonBeenden.setToolTipText("Logbuch Schlie\u00dfen");
        this.buttonDebugViewer = new JButton("Debug Viewer");
        this.filter_label = new JLabel("Benutzerfilter: ");
        this.logo = new JLabel(runApplication.bannerHauptprogramm);
        this.dummy = new JLabel(runApplication.dummyImage);
    }

    protected void setzeAuswahllisten() {
        TabelleUser dbdatenUser = new TabelleUser();
        String[] username = null;
        try {
            username = Utils.listToArray(dbdatenUser.getUserListe());
        }
        catch (SQLException e1) {
            logging.logPrintStackTrace((Exception)e1);
        }
        filteruserdb = null;
        this.filteruser = new JComboBox<String>(username);
        this.filteruser.addItem("\u00f6ffentlich");
        this.filteruser.addItemListener(new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    filteruserdb = (String)LogbuchAO.this.filteruser.getSelectedItem();
                    try {
                        ((DefaultTableModel)LogbuchAO.this.table.getModel()).setDataVector(new TabelleLogbuch().getFilterByUser((String)LogbuchAO.this.filteruser.getSelectedItem()), TabelleLogbuch.headname);
                    }
                    catch (SQLException e1) {
                        logging.logPrintStackTrace((Exception)e1);
                    }
                }
            }
        });
        this.defaultTableModelLogbuch = new DefaultTableModel(30, 5);
        this.defaultTableModelLogbuch.setColumnIdentifiers(TabelleLogbuch.headname);
        this.table = new JTable(this.defaultTableModelLogbuch);
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleLogbuch().getAll(), TabelleLogbuch.headname);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        this.table.setPreferredScrollableViewportSize(new Dimension(1200, 400));
        this.table.setFillsViewportHeight(true);
        JScrollPane scrollpane = new JScrollPane(this.table);
        scrollpane.setVerticalScrollBarPolicy(22);
        this.add(this.logo);
        this.add(this.dummy);
        this.add(this.filter_label);
        this.add(this.filteruser);
        this.add(scrollpane);
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setTitle("FeuerwehrManagementSystem - Logbuch");
        this.setSize(1280, 768);
        this.setDefaultCloseOperation(0);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.buttonDebugViewer);
        this.add(this.buttonBeenden);
    }

    protected void boxenHinzufuegen() {
    }

    protected void labelHinzufuegen() {
    }

    protected void labelErstellen() {
    }

    protected void actionErzeugen() {
        this.buttonBeenden.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent we) {
                logging.logInfo((Object)"Schlie\u00dfe Fenster - LogbuchAO");
                logbuchEingabe.NeuerEintag("Schlie\u00dfe Fenster - LogbuchAO");
                LogbuchAO.this.dispose();
            }
        });
        this.buttonDebugViewer.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                LogbuchAO.this.dispose();
                Steuerung.setStatus(Status.DEBUG);
                Steuerung.steuerung();
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

