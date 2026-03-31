package ao.karte;

import ao.AbstractFenster;
import ao.karte.ObjektEintragenAO;
import data.tabellen.karte.TabelleHydranten;
import data.tabellen.karte.TabelleObjekthydranten;
import data.tabellen.karte.TabelleStrassen;
import go.karte.Objekthydranten;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import utilities.components.MyDefaultTableRenderer;

public class ObjekthydrantenAuswählenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JComboBox straßen;
   private JLabel straßen_label;
   private JTable table;
   private DefaultTableModel defaultTableModelTable;
   private JScrollPane scrollpaneTable;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public ObjekthydrantenAuswählenAO() {
      super("FeuerwehrManagementSystem - Objekthydranten Details");
      logging.logInfo("Starte: ObjekthydrantendetailsnAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Hinzufügen");
      this.buttonZurueck = new JButton("Zurück");
      this.straßen_label = new JLabel("Straße: ");
      this.modulBeschreibung = new JLabel("Objekthydranten auswählen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.defaultTableModelTable = new DefaultTableModel(10, 9);
      this.defaultTableModelTable.setColumnIdentifiers(TabelleHydranten.headnameHydranten);
      this.table = new JTable(this.defaultTableModelTable);
      this.table.setDefaultRenderer(Object.class, new MyDefaultTableRenderer());
      this.table.setRowHeight(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("zeilenhöheAnsicht")));
      this.scrollpaneTable = new JScrollPane(this.table);
      this.scrollpaneTable.setVerticalScrollBarPolicy(22);
      this.table.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            e.getClickCount();
         }
      });

      try {
         TabelleStrassen e1 = new TabelleStrassen();
         String[] straßenListe = Utils.listToArray(e1.getStraßenListe());
         this.straßen = new JComboBox(straßenListe);
         this.straßen.setSelectedItem(ObjektEintragenAO.StrassenName.getSelectedItem());
         this.straßen.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
               try {
                  int e1 = (new TabelleStrassen()).getStrassenID(ObjekthydrantenAuswählenAO.this.straßen.getSelectedItem().toString());
                  ((DefaultTableModel)ObjekthydrantenAuswählenAO.this.table.getModel()).setDataVector((new TabelleHydranten()).getHydrantenForTable(e1), TabelleHydranten.headnameHydranten);
               } catch (SQLException var3) {
                  logging.logPrintStackTrace(var3);
               }

            }
         });
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(670, 580);
      this.setTitle("FeuerwehrManagementSystem - Objekthydranten hinzufügen");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.straßen_label);
      this.add(this.straßen);
      this.scrollpaneTable.setPreferredSize(new Dimension(600, 400));
      this.add(this.scrollpaneTable);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               int[] e = ObjekthydrantenAuswählenAO.this.table.getSelectedRows();
               if(e.length == 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_EINTRAG_WAEHLEN, "Warnung", 2);
               } else {
                  TabelleHydranten tabHydranten = new TabelleHydranten();
                  TabelleStrassen tabStraßen = new TabelleStrassen();
                  TabelleObjekthydranten tabObjekthydranten = new TabelleObjekthydranten();
                  Objekthydranten hydrant = new Objekthydranten();
                  int[] hydrantenIDListe = Utils.listToIntArray(tabHydranten.getHydrantenIDForTable(tabStraßen.getStrassenID(ObjekthydrantenAuswählenAO.this.straßen.getSelectedItem().toString())));

                  for(int i = 0; i < e.length; ++i) {
                     hydrant.setBeschreibung("");
                     hydrant.setEntfernung("");
                     hydrant.setObjektID(Integer.parseInt(ObjektEintragenAO.id.getText()));
                     hydrant.setId(tabObjekthydranten.getNextIndex().intValue());
                     hydrant.setHydrantID(hydrantenIDListe[e[i]]);
                     tabObjekthydranten.insert(hydrant);
                  }

                  ((DefaultTableModel)ObjektEintragenAO.table.getModel()).setDataVector((new TabelleObjekthydranten()).getAllObjekthydrantenForTable(Integer.parseInt(ObjektEintragenAO.id.getText())), ObjektEintragenAO.headname);
               }
            } catch (SQLException var9) {
               logging.logPrintStackTrace(var9);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            }

         }
      });
   }

   public void fensterAnzeigen() {
      try {
         ((DefaultTableModel)this.table.getModel()).setDataVector((new TabelleHydranten()).getHydrantenForTable((new TabelleStrassen()).getStrassenID(ObjektEintragenAO.StrassenName.getSelectedItem().toString())), TabelleHydranten.headnameHydranten);
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
