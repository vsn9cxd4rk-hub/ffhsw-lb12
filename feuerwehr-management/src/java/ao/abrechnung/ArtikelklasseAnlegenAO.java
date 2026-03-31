package ao.abrechnung;

import ao.AbstractFenster;
import ao.abrechnung.ArtikelAbrechnungEintragenAO;
import data.tabellen.abrechnung.TabelleAbrechnung_artikelklassen;
import go.abrechnung.AbrechnungArtikelklassen;
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
import utilities.logbuchEingabe;

public class ArtikelklasseAnlegenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextField name;
   private JLabel beschreibung;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panelKategorie;
   public static String letzteKategorie;


   public ArtikelklasseAnlegenAO() {
      super("FeuerwehrManagementSystem - Artikelklasse");
      logging.logInfo("Starte: ArtikelklasseEintragenAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.modulBeschreibung = new JLabel("Artikelklasse anlegen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.name = new JTextField(20);
      this.beschreibung = new JLabel("Artikelklasse Name: ");
   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(500, 170);
      this.setTitle("FeuerwehrManagementSystem - Artikelklasse anlegen");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelKategorie = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panelKategorie);
      this.panelKategorie.add(this.beschreibung);
      this.panelKategorie.add(this.name);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleAbrechnung_artikelklassen tabArtikelklasse = new TabelleAbrechnung_artikelklassen();
            AbrechnungArtikelklassen klasse = new AbrechnungArtikelklassen();

            try {
               if(tabArtikelklasse.getCount(ArtikelklasseAnlegenAO.this.name.getText()) != 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.ARTIKELKLASSE_SCHON_VORHANDEN, "Warnung", 2);
               } else {
                  klasse.setId(tabArtikelklasse.getNextNummer());
                  klasse.setName(ArtikelklasseAnlegenAO.this.name.getText());
                  tabArtikelklasse.insert(klasse);
                  ArtikelklasseAnlegenAO.letzteKategorie = ArtikelklasseAnlegenAO.this.name.getText();
                  ArtikelAbrechnungEintragenAO.klasse.addItem(ArtikelklasseAnlegenAO.this.name.getText());
                  logbuchEingabe.NeuerEintag("Artikelklasse wurde angelegt: " + ArtikelklasseAnlegenAO.letzteKategorie);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  ArtikelklasseAnlegenAO.this.dispose();
               }
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
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
