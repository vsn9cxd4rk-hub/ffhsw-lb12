package ao.administrator;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleClients;
import go.Clients;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
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
import utilities.components.ClientAoTableRenderer;

public class ClientAO extends AbstractFenster {

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
   private static int selectedRow = 0;
   private Vector headname = new Vector() {

      private static final long serialVersionUID = 1L;

      {
         this.add("Arbeitsplatz ID");
         this.add("Name / Beschreibung PC");
         this.add("Programm");
         this.add("Verfügbar");
         this.add("Status");
      }
   };


   public ClientAO() {
      super("FeuerwehrManagementSystem - Clients");
      logging.logInfo("Starte: ClientAO");
   }

   protected void buttonErstellen() {
      this.buttonZulassen = new JButton("Client Zulassen");
      this.buttonSperren = new JButton("Client Sperren");
      this.buttonNeu = new JButton("Neu");
      this.buttonZurueck = new JButton("Schließen");
      this.modulBeschreibung = new JLabel("Client Berechtigungen");
      this.dummy = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

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
      this.table.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 1) {
               int[] rows = ClientAO.this.table.getSelectedRows();
               if(rows.length >= 2) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_NUR_EINEN_WAEHLEN2, "Warnung", 2);
               } else {
                  ClientAO.selectedRow = rows[0];
                  logging.logInfo("Selektierte Spalte in der Tabelle: " + ClientAO.selectedRow);
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

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonNeu.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleClients e = new TabelleClients();
               Clients clients = new Clients();
               String msg = JOptionPane.showInputDialog("ClientID:");
               if(msg != null) {
                  clients.setZugelassen(0);
                  clients.setClientID(msg);
                  clients.setId(e.getNextNummer());
                  clients.setTyp("FMS");
                  clients.setOnline(0);
                  e.insert(clients);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  ((DefaultTableModel)ClientAO.this.table.getModel()).setDataVector(e.getAllForTable(), ClientAO.this.headname);
                  ClientAO.IDLISTE = Utils.listToArray(e.getAllIDs());
               }
            } catch (SQLException var5) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonZulassen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleClients e = new TabelleClients();
               Clients clients = new Clients();
               clients.setZugelassen(1);
               clients.setClientID(ClientAO.IDLISTE[ClientAO.selectedRow]);
               e.updateZugelassen(clients);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               ((DefaultTableModel)ClientAO.this.table.getModel()).setDataVector(e.getAllForTable(), ClientAO.this.headname);
               ClientAO.IDLISTE = Utils.listToArray(e.getAllIDs());
            } catch (SQLException var4) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var4);
            }

         }
      });
      this.buttonSperren.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               if(runApplication.clientID.equals(ClientAO.IDLISTE[ClientAO.selectedRow])) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.CLIENT_BERECHTIGUNG_VERGEBEN, "Warnung", 2);
               } else {
                  TabelleClients e = new TabelleClients();
                  Clients clients = new Clients();
                  clients.setZugelassen(0);
                  clients.setClientID(ClientAO.IDLISTE[ClientAO.selectedRow]);
                  e.updateZugelassen(clients);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  ((DefaultTableModel)ClientAO.this.table.getModel()).setDataVector(e.getAllForTable(), ClientAO.this.headname);
                  ClientAO.IDLISTE = Utils.listToArray(e.getAllIDs());
               }
            } catch (SQLException var4) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var4);
            }

         }
      });
   }

   public void fensterAnzeigen() {
      try {
         ((DefaultTableModel)this.table.getModel()).setDataVector((new TabelleClients()).getAllForTable(), this.headname);
         IDLISTE = Utils.listToArray((new TabelleClients()).getAllIDs());
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }

   public void fensterSchlissen() {
      this.dispose();
   }
}
