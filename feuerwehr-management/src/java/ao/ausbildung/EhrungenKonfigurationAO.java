package ao.ausbildung;

import ao.AbstractFenster;
import data.tabellen.TabelleEhrungenKonfig;
import data.tabellen.TabelleLehrgang_kategorie;
import go.Ehrungen;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.Utils;

public class EhrungenKonfigurationAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JComboBox ehrungen;
   private JLabel ehrungen_label;
   private JComboBox jahre;
   private JLabel jahre_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panel;


   public EhrungenKonfigurationAO() {
      super("FeuerwehrManagementSystem - Ehrungen Konfiguration");
      logging.logInfo("Starte: EhrungenKonfigurationAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.modulBeschreibung = new JLabel("Ehrungen Konfiguration");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.ehrungen_label = new JLabel("Ehrungen: ");
      this.jahre_label = new JLabel("Ehrung nach Jahren:               ");
   }

   protected void labelErstellen() {
      TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();

      try {
         String[] e = Utils.listToArrayOnlyFORComboBoxes(tabLehrgangKategorie.getAlleEhrungen());
         String[] zeitListe = new String[]{"<bitte wählen>", "1 Jahre", "2 Jahre", "3 Jahre", "4 Jahre", "5 Jahre", "6 Jahre", "7 Jahre", "8 Jahre", "9 Jahre", "10 Jahre", "11 Jahre", "12 Jahre", "13 Jahre", "14 Jahre", "15 Jahre", "16 Jahre", "17 Jahre", "18 Jahre", "19 Jahre", "20 Jahre", "21 Jahre", "22 Jahre", "23 Jahre", "24 Jahre", "25 Jahre", "30 Jahre", "35 Jahre", "40 Jahre", "45 Jahre", "50 Jahre", "55 Jahre", "60 Jahre"};
         this.ehrungen = new JComboBox(e);
         this.jahre = new JComboBox(zeitListe);
         this.ehrungen.setPreferredSize(new Dimension(300, 25));
         this.jahre.setPreferredSize(new Dimension(300, 25));
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

      this.ehrungen.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            try {
               TabelleEhrungenKonfig e1 = new TabelleEhrungenKonfig();
               TabelleLehrgang_kategorie tabLergangKategorie = new TabelleLehrgang_kategorie();
               int ehID = tabLergangKategorie.getLehrgangID(EhrungenKonfigurationAO.this.ehrungen.getSelectedItem().toString());
               if(e1.getCount(ehID) == 1) {
                  int zeit = e1.getZeit(ehID);
                  EhrungenKonfigurationAO.this.jahre.setSelectedItem(Integer.toString(zeit) + " Jahre");
               } else {
                  EhrungenKonfigurationAO.this.jahre.setSelectedItem("<bitte wählen>");
               }
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(700, 200);
      this.setTitle("FeuerwehrManagementSystem - Ehrungen Konfiguration");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panel = new JPanel(new GridLayout(2, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.ehrungen_label);
      this.panel.add(this.ehrungen);
      this.panel.add(this.jahre_label);
      this.panel.add(this.jahre);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleEhrungenKonfig tabEhrungKonfig = new TabelleEhrungenKonfig();
            TabelleLehrgang_kategorie tabLergangKategorie = new TabelleLehrgang_kategorie();
            Ehrungen ehrung = new Ehrungen();

            try {
               if(EhrungenKonfigurationAO.this.ehrungen.getSelectedItem().toString().equals("<bitte wählen>")) {
                  logging.logInfo("Es wurde keine Ehrung ausgewählt...");
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_EHRUNG_AUSWAEHLEN, "Warnung", 2);
               } else if(EhrungenKonfigurationAO.this.jahre.getSelectedItem().toString().equals("<bitte wählen>")) {
                  logging.logInfo("Es wurde kein Jahr ausgewählt...");
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_JAHR_AUSWAEHLEN, "Warnung", 2);
               } else {
                  int e = EhrungenKonfigurationAO.this.jahre.getSelectedItem().toString().indexOf(" Jahre");
                  int ehID = tabLergangKategorie.getLehrgangID(EhrungenKonfigurationAO.this.ehrungen.getSelectedItem().toString());
                  ehrung.setId(tabEhrungKonfig.getNextNummer());
                  ehrung.setEhrungID(ehID);
                  ehrung.setZeit(Integer.parseInt(EhrungenKonfigurationAO.this.jahre.getSelectedItem().toString().substring(0, e)));
                  if(tabEhrungKonfig.getCount(ehID) == 1) {
                     tabEhrungKonfig.update(ehrung);
                  } else {
                     tabEhrungKonfig.insert(ehrung);
                  }

                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               }
            } catch (SQLException var7) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var7);
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
