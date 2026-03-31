package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.mitglied.TabelleMitglieder_History;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
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

public class MitgliederHistoryListeAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonDrucken;
   private DefaultTableModel defaultTableModelMitgliederListe;
   public JTable table;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private Vector headname = new Vector() {

      private static final long serialVersionUID = 1L;

      {
         this.add("Datum");
         this.add("Zeit");
         this.add("Benutzer");
         this.add("Dienstgrad");
         this.add("Name");
         this.add("Vorname");
         this.add("Straße");
         this.add("Ort");
         this.add("Telefon Privat");
         this.add("Telefon Mobil");
         this.add("E-Mail");
         this.add("Geb. Datum");
      }
   };


   public MitgliederHistoryListeAO() {
      super("FeuerwehrManagementSystem - Mitglieder Historie");
      logging.logInfo("Starte: MitgliederHistoryListeAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonDrucken = new JButton("Drucken");
      this.modulBeschreibung = new JLabel("Mitglieder Historie");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(1200, 768);
      this.setTitle("FeuerwehrManagementSystem - Mitglieder Historie");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.defaultTableModelMitgliederListe = new DefaultTableModel(10, 9);
      this.defaultTableModelMitgliederListe.setColumnIdentifiers(this.headname);
      this.table = new JTable(this.defaultTableModelMitgliederListe);
      this.table.setPreferredScrollableViewportSize(new Dimension(1100, 600));
      this.table.setFillsViewportHeight(true);
      this.table.setEnabled(false);
      this.table.setRowHeight(30);
      JScrollPane scrollpane = new JScrollPane(this.table);
      scrollpane.setVerticalScrollBarPolicy(22);
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(scrollpane);
      this.add(this.dummy2);
      this.add(this.buttonDrucken);
      this.add(this.buttonZurueck);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonDrucken.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               boolean pe = MitgliederHistoryListeAO.this.table.print();
               if(pe) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.DRUCK_ERFOLGREICH);
               } else {
                  JOptionPane.showMessageDialog((Component)null, "Der Druck ist Fehlgeschlagen", "Fehlermeldung", 0);
               }
            } catch (PrinterException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
   }

   public void fensterAnzeigen() {
      try {
         ((DefaultTableModel)this.table.getModel()).setDataVector((new TabelleMitglieder_History()).getAllMitgliederForHistoryTable(Integer.parseInt(MitgliederAnlegenAO.mitgliedID)), this.headname);
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
