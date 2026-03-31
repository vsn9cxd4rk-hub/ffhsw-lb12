package ao.karte;

import ao.AbstractFenster;
import ao.karte.ObjektEintragenAO;
import data.tabellen.karte.TabelleObjekthydranten;
import go.karte.Objekthydranten;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class ObjekthydrantenDetailsAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextField entfernung;
   private JTextField beschreibung;
   private JLabel beschreibung_label;
   private JLabel entfernung_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panel;


   public ObjekthydrantenDetailsAO() {
      super("FeuerwehrManagementSystem - Objekthydranten Details");
      logging.logInfo("Starte: ObjekthydrantendetailsnAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.modulBeschreibung = new JLabel("Objekthydrantendetails");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.entfernung = new JTextField(20);
      this.beschreibung = new JTextField(20);
      this.beschreibung_label = new JLabel("Beschreibung: ");
      this.entfernung_label = new JLabel("Entfernung zum Hydranten: ");
   }

   protected void labelErstellen() {
      try {
         TabelleObjekthydranten e = new TabelleObjekthydranten();
         this.entfernung.setText(e.getEntfernung(ObjektEintragenAO.ausgewählterHydrant));
         this.beschreibung.setText(e.getBeschreibung(ObjektEintragenAO.ausgewählterHydrant));
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
      }

   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(500, 190);
      this.setTitle("FeuerwehrManagementSystem - Objekthydranten Details");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panel = new JPanel(new GridLayout(2, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.entfernung_label);
      this.panel.add(this.entfernung);
      this.panel.add(this.beschreibung_label);
      this.panel.add(this.beschreibung);
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
               TabelleObjekthydranten e = new TabelleObjekthydranten();
               Objekthydranten hydrant = new Objekthydranten();
               hydrant.setBeschreibung(ObjekthydrantenDetailsAO.this.beschreibung.getText());
               hydrant.setEntfernung(ObjekthydrantenDetailsAO.this.entfernung.getText());
               hydrant.setId(ObjektEintragenAO.ausgewählterHydrant);
               e.updateObjekthdrantenDetails(hydrant);
               ((DefaultTableModel)ObjektEintragenAO.table.getModel()).setDataVector((new TabelleObjekthydranten()).getAllObjekthydrantenForTable(Integer.parseInt(ObjektEintragenAO.id.getText())), ObjektEintragenAO.headname);
               ObjekthydrantenDetailsAO.this.dispose();
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            }

         }
      });
   }

   public void fensterAnzeigen() {
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
