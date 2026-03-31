package ao.schulung;

import ao.AbstractFenster;
import ao.schulung.SchulungListeAO;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.schulung.TabelleSchulung;
import data.tabellen.schulung.TabelleSchulungTeilnehmer;
import data.tabellen.schulung.TabelleSchulung_gruppen_mandant;
import go.schulung.SchulungTeilnehmer;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.SbcUtils;
import utilities.Utils;

public class SchulungListeOptionenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonSpeichern;
   private JButton buttonZurueck;
   private JCheckBox[] jCheckboxArray;
   public static JCheckBox[] jCheckboxArrayZusatzfelder;
   private JLabel modulBeschreibung;
   private JLabel beschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public SchulungListeOptionenAO() {
      super("FeuerwehrManagementSystem - Schulung - Bewerberauswahl");
      logging.logInfo("Starte: AnwesenheitListeOptionenAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.beschreibung = new JLabel("Wählen Sie Bitte die Mitglieder aus, die für die Schulung eingeplat werden sollen: ");
      this.modulBeschreibung = new JLabel("Schulung - Bewerberauswahl");
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
      this.setSize(480, 550);
      this.setTitle("FeuerwehrManagementSystem - Schulung - Bewerberauswahl");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.beschreibung);
      JPanel panel = new JPanel(new GridLayout(0, 2));
      TabelleMitglied tabMitglied = new TabelleMitglied();

      try {
         String[] e = Utils.listToArray(tabMitglied.getMitgliederGruppe1());
         int[] labelsID = Utils.listToIntArray(tabMitglied.getMitgliederIDGruppe1());
         this.jCheckboxArray = new JCheckBox[e.length];

         for(int pane = 0; pane < e.length; ++pane) {
            this.jCheckboxArray[pane] = new JCheckBox();
            this.jCheckboxArray[pane].setText(e[pane]);
            this.jCheckboxArray[pane].setName(Integer.toString(labelsID[pane]));
            panel.add(this.jCheckboxArray[pane]);
            logging.logInfo("Füge Check Box: " + e[pane] + " hinzu....");
         }

         if(e.length <= 36) {
            this.add(panel, "Center");
         } else {
            JScrollPane var7 = new JScrollPane(panel);
            var7.setVerticalScrollBarPolicy(22);
            var7.setPreferredSize(new Dimension(430, 390));
            this.add(var7, "Center");
         }
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

      this.add(this.dummy2);
      this.add(this.buttonSpeichern);
      this.add(this.buttonZurueck);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleSchulungTeilnehmer e = new TabelleSchulungTeilnehmer();
               SchulungTeilnehmer teilnehmer = new SchulungTeilnehmer();
               String datum = SbcUtils.timeStamp("yyyy-MM-dd");
               String zeit = SbcUtils.timeStamp("HH:mm:ss");
               int count = 0;

               int myGruppen;
               for(myGruppen = 0; myGruppen < SchulungListeOptionenAO.this.jCheckboxArray.length; ++myGruppen) {
                  if(SchulungListeOptionenAO.this.jCheckboxArray[myGruppen].isSelected()) {
                     ++count;
                  }
               }

               if(count == 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
               } else {
                  for(myGruppen = 0; myGruppen < SchulungListeOptionenAO.this.jCheckboxArray.length; ++myGruppen) {
                     if(SchulungListeOptionenAO.this.jCheckboxArray[myGruppen].isSelected() && e.getCountOfOneTeilnehmer(SchulungListeAO.currentSchulungsID, Integer.parseInt(SchulungListeOptionenAO.this.jCheckboxArray[myGruppen].getName())) == 0) {
                        teilnehmer.setId(e.getNextNummer());
                        teilnehmer.setMitgliedID(Integer.parseInt(SchulungListeOptionenAO.this.jCheckboxArray[myGruppen].getName()));
                        teilnehmer.setTeilnehmerMandant(Integer.parseInt((String)runApplication.PROPERTIES.get("MandantID")));
                        teilnehmer.setSchulungID(SchulungListeAO.currentSchulungsID);
                        teilnehmer.setStatus(1);
                        teilnehmer.setStatusGrund("");
                        teilnehmer.setStatusDatum(datum);
                        teilnehmer.setStatusZeit(zeit);
                        e.insert(teilnehmer);
                     }
                  }

                  int[] var9 = Utils.listToIntArray((new TabelleSchulung_gruppen_mandant()).getMandantIDEinerSchulungGruppe(Integer.parseInt((String)runApplication.PROPERTIES.get("MandantID"))));
                  ((DefaultTableModel)SchulungListeAO.table.getModel()).setDataVector((new TabelleSchulung()).getAllSchulungen(var9), SchulungListeAO.headname);
                  JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
                  SchulungListeOptionenAO.this.dispose();
               }
            } catch (SQLException var8) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var8);
               SchulungListeOptionenAO.this.dispose();
            }

         }
      });
      this.buttonZurueck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            logging.logInfo("Optionsmaske wird geschlossen...");
            SchulungListeOptionenAO.this.dispose();
         }
      });
   }

   public void fensterAnzeigen() {
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      MyEvent.setEvent("0x0030");
      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }

   public void fensterSchlissen() {
      this.dispose();
   }
}
