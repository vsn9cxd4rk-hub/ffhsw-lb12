package ao.fahrzeuge;

import ao.AbstractFenster;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;

public class FahrzeugeinteilungOptionenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panelFahrzeug;
   private JPanel panelFuehrerscheinKlasse;


   public FahrzeugeinteilungOptionenAO() {
      super("FeuerwehrManagementSystem - FahrzeugEinteilungOptionen");
      logging.logInfo("Starte: FahrzaugeAnlagenAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.modulBeschreibung = new JLabel("Fahrzeugeinteilung Optionen");
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
      this.setSize(500, 350);
      this.setTitle("FeuerwehrManagementSystem - Fahrzeuge");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelFahrzeug = new JPanel(new GridLayout(5, 2));
      this.getContentPane().add("Center", this.panelFahrzeug);
      this.panelFuehrerscheinKlasse = new JPanel(new GridLayout(3, 1));
      this.getContentPane().add("Center", this.panelFuehrerscheinKlasse);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {}
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
