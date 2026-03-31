package ao.listen;

import ao.AbstractFenster;
import ao.listen.LehrgangListeAO;
import data.tabellen.TabelleLehrgang;
import data.tabellen.TabelleLehrgang_kategorie;
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

public class LehrgangListeOptionenAO extends AbstractFenster {

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


   public LehrgangListeOptionenAO() {
      super("FeuerwehrManagementSystem - Lehrgang Liste Optionen");
      logging.logInfo("Starte: LehrgangListeOptionenAO");
   }

   protected void buttonErstellen() {
      this.buttonUebernehmen = new JButton("Übernehmen");
      this.buttonZurueck = new JButton("Zurück");
      this.beschreibung = new JLabel("Wählen Sie Bitte die Lehrgänge aus die in der Liste angezeigt werden sollen: ");
      this.beschreibung2 = new JLabel("Hier können Sie Optionale Zusatzfelder für die Liste auswählen: ");
      this.modulBeschreibung = new JLabel("Lehrgnagsliste Optionen");
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
      this.setSize(700, 760);
      this.setTitle("FeuerwehrManagementSystem - Lehrgang Liste Optionen");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.beschreibung);
      JPanel panel = new JPanel(new GridLayout(0, 2));
      TabelleLehrgang_kategorie tabLehrgang_kat = new TabelleLehrgang_kategorie();

      int x;
      try {
         String[] zusatzPanel = Utils.listToArray(tabLehrgang_kat.getAlleLehrgänge());
         int[] zusatzLabels = Utils.listToIntArray(tabLehrgang_kat.getAlleLehrgängeID());
         this.jCheckboxArray = new JCheckBox[zusatzPanel.length];

         for(x = 0; x < zusatzPanel.length; ++x) {
            this.jCheckboxArray[x] = new JCheckBox();
            this.jCheckboxArray[x].setText(zusatzPanel[x]);
            this.jCheckboxArray[x].setName(Integer.toString(zusatzLabels[x]));
            panel.add(this.jCheckboxArray[x]);
            logging.logInfo("Füge Check Box: " + zusatzPanel[x] + " hinzu....");
         }

         if(zusatzPanel.length <= 32) {
            this.add(panel, "Center");
         } else {
            JScrollPane var9 = new JScrollPane(panel);
            var9.setVerticalScrollBarPolicy(22);
            var9.setPreferredSize(new Dimension(630, 390));
            this.add(var9, "Center");
         }
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

      this.add(this.beschreibung2);
      JPanel var7 = new JPanel(new GridLayout(0, 2));
      String[] var8 = new String[]{"G25", "G26/3", "G30", "G41", "G42", "AGT Belastungsübung", "AGT Einsatzübung", "Ablaufdatum Führerschein C1", "Ablaufdatum Führerschein C", "Ablaufdatum Führerschein C1E", "Ablaufdatum Führerschein CE", "Ausstelldatum Führerschein [4a]", "Ablaufdatum Führerschein [4b]"};
      jCheckboxArrayZusatzfelder = new JCheckBox[var8.length];

      for(x = 0; x < var8.length; ++x) {
         jCheckboxArrayZusatzfelder[x] = new JCheckBox();
         jCheckboxArrayZusatzfelder[x].setText(var8[x]);
         var7.add(jCheckboxArrayZusatzfelder[x]);
      }

      this.add(var7, "Center");
      panelFreiFelder = new JPanel(new GridLayout(4, 2));
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

               for(int selectierteLehrgänge = 0; selectierteLehrgänge < LehrgangListeOptionenAO.this.jCheckboxArray.length; ++selectierteLehrgänge) {
                  if(LehrgangListeOptionenAO.this.jCheckboxArray[selectierteLehrgänge].isSelected()) {
                     ++e;
                  }
               }

               if(e == 0) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_LEHRGANG_AUSWAEHLEN, "Warnung", 2);
               } else {
                  int[] var6 = new int[e];
                  e = 0;

                  for(int i = 0; i < LehrgangListeOptionenAO.this.jCheckboxArray.length; ++i) {
                     if(LehrgangListeOptionenAO.this.jCheckboxArray[i].isSelected()) {
                        var6[e] = Integer.parseInt(LehrgangListeOptionenAO.this.jCheckboxArray[i].getName());
                        ++e;
                     }
                  }

                  LehrgangListeAO.headname = (new TabelleLehrgang()).mapFilterHeadNameToVector(var6);
                  ((DefaultTableModel)LehrgangListeAO.table.getModel()).setDataVector((new TabelleLehrgang()).getFilterDataForList(var6), LehrgangListeAO.headname);
                  LehrgangListeAO.buttonStandard.setVisible(true);
                  LehrgangListeOptionenAO.this.dispose();
               }
            } catch (SQLException var5) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
               logging.logPrintStackTrace(var5);
               LehrgangListeOptionenAO.this.dispose();
            }

         }
      });
      this.buttonZurueck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            logging.logInfo("Optionsmaske wird geschlossen...");
            LehrgangListeOptionenAO.this.dispose();
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
