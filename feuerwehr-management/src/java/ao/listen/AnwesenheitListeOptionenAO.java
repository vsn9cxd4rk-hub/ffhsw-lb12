package ao.listen;

import ao.AbstractFenster;
import ao.listen.AnwesenheitListeAO;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class AnwesenheitListeOptionenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonUebernehmen;
   private JButton buttonZurueck;
   private JCheckBox[] jCheckboxArray;
   public static JCheckBox[] jCheckboxArrayZusatzfelder;
   public static JTextField zusatzFeld1;
   public static JCheckBox zusatzBox1;
   public static JTextField zusatzFeld2;
   public static JCheckBox zusatzBox2;
   public static JTextField zusatzFeld3;
   public static JCheckBox zusatzBox3;
   public static JPanel panelFreiFelder;
   private JLabel modulBeschreibung;
   private JLabel beschreibung2;
   private JLabel beschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public AnwesenheitListeOptionenAO() {
      super("FeuerwehrManagementSystem - Anwesenheit Liste Optionen");
      logging.logInfo("Starte: AnwesenheitListeOptionenAO");
   }

   protected void buttonErstellen() {
      this.buttonUebernehmen = new JButton("Übernehmen");
      this.buttonZurueck = new JButton("Zurück");
      this.beschreibung = new JLabel("Wählen Sie Bitte die Mitglieder aus, die in der Liste erscheinen sollen: ");
      this.beschreibung2 = new JLabel("Hier können Sie Optionale Zusatzfelder für die Liste auswählen: ");
      this.modulBeschreibung = new JLabel("Anwesneheitsliste Optionen");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      zusatzFeld1 = new JTextField(10);
      zusatzFeld2 = new JTextField(10);
      zusatzFeld3 = new JTextField(10);
      zusatzBox1 = new JCheckBox("Frei Feld 1: ");
      zusatzBox2 = new JCheckBox("Frei Feld 2: ");
      zusatzBox3 = new JCheckBox("Frei Feld 3: ");
   }

   protected void labelErstellen() {}

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setSize(480, 700);
      this.setTitle("FeuerwehrManagementSystem - Anwesenheitsliste Optionen");
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
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

      try {
         int e = tabGruppe.getID(AnwesenheitListeAO.mitgliederGruppe.getSelectedItem().toString());
         String[] labels = Utils.listToArray(tabMitglied.getMitgliederEinerGruppe(e));
         int[] labelsID = Utils.listToIntArray(tabMitglied.getMitgliederIDsEinerGruppe(e));
         this.jCheckboxArray = new JCheckBox[labels.length];

         for(int pane = 0; pane < labels.length; ++pane) {
            this.jCheckboxArray[pane] = new JCheckBox();
            this.jCheckboxArray[pane].setText(labels[pane]);
            this.jCheckboxArray[pane].setName(Integer.toString(labelsID[pane]));
            panel.add(this.jCheckboxArray[pane]);
            logging.logInfo("Füge Check Box: " + labels[pane] + " hinzu....");
         }

         if(labels.length <= 36) {
            this.add(panel, "Center");
         } else {
            JScrollPane var9 = new JScrollPane(panel);
            var9.setVerticalScrollBarPolicy(22);
            var9.setPreferredSize(new Dimension(430, 390));
            this.add(var9, "Center");
         }
      } catch (SQLException var8) {
         logging.logPrintStackTrace(var8);
      }

      this.add(this.beschreibung2);
      panelFreiFelder = new JPanel(new GridLayout(3, 2));
      this.getContentPane().add("Center", panelFreiFelder);
      panelFreiFelder.add(zusatzBox1);
      panelFreiFelder.add(zusatzFeld1);
      panelFreiFelder.add(zusatzBox2);
      panelFreiFelder.add(zusatzFeld2);
      panelFreiFelder.add(zusatzBox3);
      panelFreiFelder.add(zusatzFeld3);
      this.add(this.dummy2);
      this.add(this.buttonUebernehmen);
      this.add(this.buttonZurueck);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonUebernehmen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               int e = 0;

               for(int selektierteMitglieder = 0; selektierteMitglieder < AnwesenheitListeOptionenAO.this.jCheckboxArray.length; ++selektierteMitglieder) {
                  if(AnwesenheitListeOptionenAO.this.jCheckboxArray[selektierteMitglieder].isSelected()) {
                     ++e;
                  }
               }

               if(e == 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_MITGLIEDER_WAEHLEN, "Warnung", 2);
               } else {
                  String[] var7 = new String[e];
                  e = 0;

                  for(int tabGruppe = 0; tabGruppe < AnwesenheitListeOptionenAO.this.jCheckboxArray.length; ++tabGruppe) {
                     if(AnwesenheitListeOptionenAO.this.jCheckboxArray[tabGruppe].isSelected()) {
                        var7[e] = AnwesenheitListeOptionenAO.this.jCheckboxArray[tabGruppe].getName();
                        ++e;
                     }
                  }

                  TabelleMitglieder_gruppe var8 = new TabelleMitglieder_gruppe();
                  int gID = var8.getID(AnwesenheitListeAO.mitgliederGruppe.getSelectedItem().toString());
                  AnwesenheitListeAO.dynacmicRowHigh(e);
                  ((DefaultTableModel)AnwesenheitListeAO.table.getModel()).setDataVector((new TabelleAnwesenheit()).getFilterDataForList(var7, gID), (new TabelleAnwesenheit()).createHeadnameForFilter());
                  AnwesenheitListeAO.buttonStandard.setVisible(true);
                  AnwesenheitListeOptionenAO.this.dispose();
               }
            } catch (SQLException var6) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var6);
               AnwesenheitListeOptionenAO.this.dispose();
            }

         }
      });
      this.buttonZurueck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            logging.logInfo("Optionsmaske wird geschlossen...");
            AnwesenheitListeOptionenAO.this.dispose();
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
