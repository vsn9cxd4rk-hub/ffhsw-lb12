package ao.fahrzeuge;

import ao.AbstractFenster;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.fahrzeug.TabelleFahrzeugeinteilung_temp;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import go.Fahrzeugeinteilung_temp;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class FahrzeugeinteilungNachtragenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonErstellen;
   private JButton buttonAnwesenheitNachtragen;
   private JComboBox veranstaltung;
   private JLabel beschreibung;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public FahrzeugeinteilungNachtragenAO() {
      super("FeuerwehrManagementSystem - Fahrzeugeinteilung Erstellen");
      logging.logInfo("Starte: FahrzeugeinteilungNachtragenAO");
   }

   protected void buttonErstellen() {
      this.buttonErstellen = new JButton("Erstellen");
      this.buttonZurueck = new JButton("Zurück");
      this.buttonAnwesenheitNachtragen = new JButton("Anwesenheit nachtragen");
      this.modulBeschreibung = new JLabel("Fahrzeugeinteilung Erstellen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.beschreibung = new JLabel("Veranstaltung: ");
   }

   protected void labelErstellen() {
      TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();

      try {
         String[] e = Utils.listToArrayOnlyFORComboBoxes(tabVeranstaltung.getAllVeranstaltungWithoutFahrzeugeinteilung());
         this.veranstaltung = new JComboBox(e);
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(500, 170);
      this.setTitle("FeuerwehrManagementSystem - Fahrzeugeinteilung Erstellen");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.beschreibung);
      this.add(this.veranstaltung);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonErstellen);
      this.add(this.buttonAnwesenheitNachtragen);
      this.buttonAnwesenheitNachtragen.setVisible(false);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonAnwesenheitNachtragen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            runApplication.letzterVeranstaltungsname = FahrzeugeinteilungNachtragenAO.this.veranstaltung.getSelectedItem().toString();
            FahrzeugeinteilungNachtragenAO.this.dispose();
            MyEvent.setEvent("0x0040");
            Steuerung.setStatus(Status.ANWESENHEIT_EINTRAGEN);
            Steuerung.steuerung();
         }
      });
      this.buttonErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
            TabelleFahrzeugeinteilung_temp tabTemp = new TabelleFahrzeugeinteilung_temp();
            Fahrzeugeinteilung_temp temp = new Fahrzeugeinteilung_temp();

            try {
               if(FahrzeugeinteilungNachtragenAO.this.veranstaltung.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_VERANSTALTUNG_WAEHLEN, "Fehlermeldung", 2);
               } else if(tabAnwesenheit.getGesamtVeranstaltung(tabVeranstaltung.getVeranstaltungID(FahrzeugeinteilungNachtragenAO.this.veranstaltung.getSelectedItem().toString())) == 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.KEINE_ANWESENHEIT, "Fehlermeldung", 0);
                  FahrzeugeinteilungNachtragenAO.this.buttonAnwesenheitNachtragen.setVisible(true);
               } else {
                  runApplication.letzterVeranstaltungsname = FahrzeugeinteilungNachtragenAO.this.veranstaltung.getSelectedItem().toString();
                  FahrzeugeinteilungNachtragenAO.this.dispose();
                  tabTemp.deleteAll();
                  int e = tabVeranstaltung.getVeranstaltungID(FahrzeugeinteilungNachtragenAO.this.veranstaltung.getSelectedItem().toString());
                  String[] mitgliederListe = Utils.listToArray(tabAnwesenheit.getAnwesendeMitgliederByVeranstaltung(e));

                  for(int threadFahrzeugeinteilung = 0; threadFahrzeugeinteilung < mitgliederListe.length; ++threadFahrzeugeinteilung) {
                     int mID = tabMitglied.getIdByGuiString(mitgliederListe[threadFahrzeugeinteilung]);
                     int[] funktionen = tabLaufbahn.getLehrgangData(mID);
                     temp.setMitgliederID(mID);
                     temp.setDienstgradID(tabMitglied.getDienstgradID(mID));
                     temp.setKlasseC(funktionen[0]);
                     temp.setKlasseB(funktionen[1]);
                     temp.setMaschi(funktionen[11]);
                     temp.setChef(funktionen[25]);
                     temp.setTm1(funktionen[5]);
                     temp.setAgt(funktionen[7]);
                     temp.setTf(funktionen[14]);
                     temp.setGf(funktionen[18]);
                     temp.setZf(funktionen[19]);
                     temp.setKorbsteuerung(funktionen[20]);
                     temp.setDlkmaschi(funktionen[21]);
                     temp.setRh(funktionen[22]);
                     temp.setRs(funktionen[23]);
                     temp.setRa(funktionen[24]);
                     temp.setBeteiligung(tabAnwesenheit.getBeteiligungEinsatzDienst(mID));
                     temp.setPosition(0);
                     tabTemp.insert(temp);
                  }

                  logging.logInfo("Schließe FahrzeugEinteilungNachtragenAO und Starte FahrzeugEinteilungAO");
                  MyEvent.setEvent("0");
                  Steuerung.setStatus(Status.PROZESSBAR);
                  Steuerung.steuerung();
                  Thread var14 = new Thread() {
                     public void run() {
                        Steuerung.setStatus(Status.FAHRZEUGEINTEILUNG);
                        Steuerung.steuerung();
                     }
                  };
                  var14.start();
               }
            } catch (SQLException var13) {
               logging.logPrintStackTrace(var13);
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
