/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  ao.AbstractFenster
 *  logging.logging
 */
package ao.administrator;

import ao.AbstractFenster;
import ao.administrator.ClientAoTableRenderer;
import data.tabellen.einstellungen.TabelleClients;
import go.Clients;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.Utils;

public class ClientAO
extends AbstractFenster {
    private static final long serialVersionUID = 1L;
    private static String[] IDLISTE = null;
    private JButton buttonZurueck;
    private JButton buttonZulassen;
    private JButton buttonSperren;
    private JButton buttonNeu;
    private DefaultTableModel defaultTableModelLaufbahnListe;
    public JTable table;
    private JScrollPane scrollpane;
    private JLabel modulBeschreibung;
    private JLabel dummy;
    public static String letzteKategorie;
    private static int selectedRow;
    private Vector<String> headname = new Vector<String>(){
        private static final long serialVersionUID = 1L;
        {
            this.add("Arbeitsplatz ID");
            this.add("Name / Beschreibung PC");
            this.add("Programm");
            this.add("Verf\u00fcgbar");
            this.add("Status");
        }
    };

    static {
        selectedRow = 0;
    }

    public ClientAO() {
        super("FeuerwehrManagementSystem - Clients");
        logging.logInfo((Object)"Starte: ClientAO");
    }

    protected void buttonErstellen() {
        this.buttonZulassen = new JButton("Client Zulassen");
        this.buttonSperren = new JButton("Client Sperren");
        this.buttonNeu = new JButton("Neu");
        this.buttonZurueck = new JButton("Schlie\u00dfen");
        this.modulBeschreibung = new JLabel("Client Berechtigungen");
        this.dummy = new JLabel(runApplication.dummyImage);
    }

    protected void labelErstellen() {
    }

    protected void setzeAuswahllisten() {
    }

    protected void labelHinzufuegen() {
        this.defaultTableModelLaufbahnListe = new DefaultTableModel(10, 9);
        this.defaultTableModelLaufbahnListe.setColumnIdentifiers(this.headname);
        this.table = new JTable(this.defaultTableModelLaufbahnListe);
        this.table.setPreferredScrollableViewportSize(new Dimension(840, 300));
        this.table.setFillsViewportHeight(true);
        this.table.setRowHeight(30);
        this.table.setDefaultRenderer(Object.class, new ClientAoTableRenderer());
        this.scrollpane = new JScrollPane(this.table);
        this.scrollpane.setVerticalScrollBarPolicy(22);
        this.table.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int[] rows = ClientAO.this.table.getSelectedRows();
                    if (rows.length >= 2) {
                        JOptionPane.showMessageDialog(null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
                    } else {
                        selectedRow = rows[0];
                        logging.logInfo((Object)("Selektierte Spalte in der Tabelle: " + selectedRow));
                    }
                }
            }
        });
    }

    protected void layoutFestlegen() {
        this.layout.setHgap(10);
        this.layout.setVgap(10);
        this.layout.setAlignment(1);
        this.setLayout(this.layout);
        this.setSize(900, 450);
        this.setTitle("FeuerwehrManagementSystem - Client Berechtigungen");
        this.setDefaultCloseOperation(2);
        Image icon = runApplication.icon.getImage();
        this.setIconImage(icon);
    }

    protected void buttonHinzufuegen() {
        this.add(this.modulBeschreibung);
        this.add(this.dummy);
        this.add(this.scrollpane);
        this.add(this.buttonZurueck);
        this.add(this.buttonNeu);
        this.add(this.buttonZulassen);
        this.add(this.buttonSperren);
    }

    protected void boxenHinzufuegen() {
    }

    protected void actionErzeugen() {
        this.buttonZurueck.addActionListener((ActionListener)((Object)new DisposeListener((JFrame)((Object)this))));
        this.buttonNeu.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleClients tabClients = new TabelleClients();
                    Clients clients = new Clients();
                    String msg = JOptionPane.showInputDialog("ClientID:");
                    if (msg != null) {
                        clients.setZugelassen(0);
                        clients.setClientID(msg);
                        clients.setId(tabClients.getNextNummer());
                        clients.setTyp("FMS");
                        clients.setOnline(0);
                        tabClients.insert(clients);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        ((DefaultTableModel)ClientAO.this.table.getModel()).setDataVector(tabClients.getAllForTable(), ClientAO.this.headname);
                        IDLISTE = Utils.listToArray(tabClients.getAllIDs());
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonZulassen.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    TabelleClients tabClients = new TabelleClients();
                    Clients clients = new Clients();
                    clients.setZugelassen(1);
                    clients.setClientID(IDLISTE[selectedRow]);
                    tabClients.updateZugelassen(clients);
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                    ((DefaultTableModel)ClientAO.this.table.getModel()).setDataVector(tabClients.getAllForTable(), ClientAO.this.headname);
                    IDLISTE = Utils.listToArray(tabClients.getAllIDs());
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
        this.buttonSperren.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try {
                    if (runApplication.clientID.equals(IDLISTE[selectedRow])) {
                        JOptionPane.showMessageDialog(null, Konstante.CLIENT_BERECHTIGUNG_VERGEBEN, "Warnung", 2);
                    } else {
                        TabelleClients tabClients = new TabelleClients();
                        Clients clients = new Clients();
                        clients.setZugelassen(0);
                        clients.setClientID(IDLISTE[selectedRow]);
                        tabClients.updateZugelassen(clients);
                        JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_ERFOLGREICH);
                        ((DefaultTableModel)ClientAO.this.table.getModel()).setDataVector(tabClients.getAllForTable(), ClientAO.this.headname);
                        IDLISTE = Utils.listToArray(tabClients.getAllIDs());
                    }
                }
                catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        });
    }

    public void fensterAnzeigen() {
        try {
            ((DefaultTableModel)this.table.getModel()).setDataVector(new TabelleClients().getAllForTable(), this.headname);
            IDLISTE = Utils.listToArray(new TabelleClients().getAllIDs());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
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

