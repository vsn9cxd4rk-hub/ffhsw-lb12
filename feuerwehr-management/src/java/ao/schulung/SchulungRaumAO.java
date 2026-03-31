package ao.schulung;

import ao.AbstractFenster;
import data.tabellen.schulung.TabelleSchulung_raum;
import go.schulung.Raum;
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
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class SchulungRaumAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JLabel raumName_label;
   private JTextField raumName;
   private JPanel panel;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   public static String letzteKategorie;


   public SchulungRaumAO() {
      super("FeuerwehrManagementSystem - Raum anlegen");
      logging.logInfo("Starte: SchulunRaumAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Zurück");
      this.buttonSpeichern = new JButton("Speichern");
      this.raumName = new JTextField(20);
      this.raumName_label = new JLabel("Schulungsraum Name: ");
      this.modulBeschreibung = new JLabel("Schulungsraum anlegen");
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
      this.setSize(500, 180);
      this.setTitle("FeuerwehrManagementSystem - Schulungsraum anlegen");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panel = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.raumName_label);
      this.panel.add(this.raumName);
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
               TabelleSchulung_raum e = new TabelleSchulung_raum();
               Raum raum = new Raum();
               if(SchulungRaumAO.this.raumName.equals("")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_NAME_EINGEBEN, "Warnung", 2);
               } else if(e.getCount(SchulungRaumAO.this.raumName.getText()) != 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_NAME_EXISTIERT_BEREITS, "Warnung", 2);
               } else {
                  raum.setId(e.getNextNummer());
                  raum.setName(SchulungRaumAO.this.raumName.getText());
                  e.insert(raum);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  SchulungRaumAO.this.dispose();
               }
            } catch (SQLException var4) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var4);
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
