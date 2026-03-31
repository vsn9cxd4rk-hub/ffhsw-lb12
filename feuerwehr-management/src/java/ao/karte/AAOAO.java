package ao.karte;

import ao.AbstractFenster;
import data.tabellen.TabelleStichwort;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.karte.TabelleAAO;
import data.tabellen.karte.TabelleStrassen;
import go.karte.AAO;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Utils;

public class AAOAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonRein;
   private JButton buttonRaus;
   private JComboBox stichwort;
   private JComboBox straßen;
   private JTextArea hinweis;
   public static JList nichtZugeordnet;
   public static JList zugeordnet;
   public static JScrollPane pane_nichtZugeordnet;
   public static JScrollPane pane_zugeordnet;
   private JLabel nichtZugeordnet_label;
   private JLabel zugeordnet_label;
   private JLabel stichwort_label;
   private JLabel straßen_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JLabel dummy3;
   private JPanel panelListe;
   private JPanel panelAAO;
   public static String letzteKategorie;


   public AAOAO() {
      super("FeuerwehrManagementSystem - AAO");
      logging.logInfo("Starte: AAOAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonRein = new JButton("←");
      this.buttonRein.setToolTipText("Hinzufügen");
      this.buttonRaus = new JButton("→");
      this.buttonRaus.setToolTipText("Entfernen");
      this.hinweis = new JTextArea(5, 50);
      this.hinweis.setText("HINWEIS:\nDie Alarm und Ausrückeordnung (AAO) kann Stichwortbasierent oder auch\npro Straßenzug eingestellt werden.\n\nDie AAO im EinssatzMonitor wird nach folgenden Regeln ausgewertet:\n1. Stichwort und Straßen in kompination\n2. Straße ohne Stichwort\n3. Stichwort ohne Straße\n4. Rückfallebene - Sortirung der Fahrzeuge aus der Fahrzeuverwaltung\n");
      this.hinweis.setLineWrap(true);
      this.hinweis.setEditable(false);
      this.nichtZugeordnet_label = new JLabel("              Fahrzeuge ohne Zuordnung:             ");
      this.zugeordnet_label = new JLabel("Fahrzeuge in der AAO:                                        ");
      this.stichwort_label = new JLabel("Stichwort: ");
      this.straßen_label = new JLabel("Straßen: ");
      this.modulBeschreibung = new JLabel("AAO");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.dummy3 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      try {
         TabelleStichwort e = new TabelleStichwort();
         TabelleStrassen tabStrassen = new TabelleStrassen();
         TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
         String[] stichwortListe = Utils.listToArrayOnlyFORComboBoxes(e.getAllStichwort());
         String[] starssenListe = Utils.listToArrayOnlyFORComboBoxes(tabStrassen.getStraßenListe());
         String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeOhneAnhaenger());
         this.stichwort = new JComboBox(stichwortListe);
         this.straßen = new JComboBox(starssenListe);
         nichtZugeordnet = new JList();
         nichtZugeordnet.setVisibleRowCount(15);
         nichtZugeordnet.setToolTipText("Liste aller verfügbaren Fahrzeuge");
         nichtZugeordnet.setListData(fahrzeugListe);
         pane_nichtZugeordnet = new JScrollPane(nichtZugeordnet);
         pane_nichtZugeordnet.setVerticalScrollBarPolicy(22);
         pane_nichtZugeordnet.setPreferredSize(new Dimension(360, 260));
         zugeordnet = new JList();
         zugeordnet.setVisibleRowCount(15);
         zugeordnet.setToolTipText("Liste der Zugeordneten Fahrzeuge");
         pane_zugeordnet = new JScrollPane(zugeordnet);
         pane_zugeordnet.setVerticalScrollBarPolicy(22);
         pane_zugeordnet.setPreferredSize(new Dimension(360, 260));
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
      }

      this.stichwort.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            try {
               TabelleStichwort ex = new TabelleStichwort();
               TabelleStrassen tabStrassen = new TabelleStrassen();
               TabelleAAO tabAAO = new TabelleAAO();
               int stichwortID = ex.getStichwortID(AAOAO.this.stichwort.getSelectedItem().toString());
               int straßenID = tabStrassen.getStrassenID(AAOAO.this.straßen.getSelectedItem().toString());
               AAOAO.zugeordnet.setListData(Utils.listToArray(tabAAO.getZugeordneteFahrzeuge(stichwortID, straßenID)));
            } catch (SQLException var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      });
      this.straßen.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            try {
               TabelleStichwort ex = new TabelleStichwort();
               TabelleStrassen tabStrassen = new TabelleStrassen();
               TabelleAAO tabAAO = new TabelleAAO();
               int stichwortID = ex.getStichwortID(AAOAO.this.stichwort.getSelectedItem().toString());
               int straßenID = tabStrassen.getStrassenID(AAOAO.this.straßen.getSelectedItem().toString());
               AAOAO.zugeordnet.setListData(Utils.listToArray(tabAAO.getZugeordneteFahrzeuge(stichwortID, straßenID)));
            } catch (SQLException var7) {
               logging.logPrintStackTrace(var7);
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
      this.setSize(780, 700);
      this.setTitle("FeuerwehrManagementSystem - AAO");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.hinweis);
      this.panelAAO = new JPanel(new GridLayout(2, 2));
      this.getContentPane().add("Center", this.panelAAO);
      this.panelAAO.add(this.stichwort_label);
      this.panelAAO.add(this.stichwort);
      this.panelAAO.add(this.straßen_label);
      this.panelAAO.add(this.straßen);
      this.add(this.dummy2);
      this.add(this.zugeordnet_label);
      this.add(this.nichtZugeordnet_label);
      this.panelListe = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panelListe);
      this.panelListe.add(pane_zugeordnet);
      this.panelListe.add(pane_nichtZugeordnet);
      this.add(this.buttonRein);
      this.add(this.buttonRaus);
      this.add(this.dummy3);
      this.add(this.buttonZurueck);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonRein.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleStichwort e = new TabelleStichwort();
               TabelleStrassen tabStrassen = new TabelleStrassen();
               TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
               TabelleAAO tabAAO = new TabelleAAO();
               AAO aao = new AAO();
               int stichwortID = e.getStichwortID(AAOAO.this.stichwort.getSelectedItem().toString());
               int straßenID = tabStrassen.getStrassenID(AAOAO.this.straßen.getSelectedItem().toString());
               int fID = tabFahrzeug.getFahrzeugID(AAOAO.nichtZugeordnet.getSelectedValue().toString());
               aao.setId(tabAAO.getNextNummer());
               aao.setFahrzeugID(fID);
               aao.setReihenfolge(tabAAO.getNextRheienfolge(stichwortID, straßenID));
               aao.setStichwortID(stichwortID);
               aao.setStrassenID(straßenID);
               tabAAO.insert(aao);
               AAOAO.zugeordnet.setListData(Utils.listToArray(tabAAO.getZugeordneteFahrzeuge(stichwortID, straßenID)));
            } catch (SQLException var10) {
               logging.logPrintStackTrace(var10);
            }

         }
      });
      this.buttonRaus.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleStichwort e = new TabelleStichwort();
               TabelleStrassen tabStrassen = new TabelleStrassen();
               TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
               TabelleAAO tabAAO = new TabelleAAO();
               int stichwortID = e.getStichwortID(AAOAO.this.stichwort.getSelectedItem().toString());
               int straßenID = tabStrassen.getStrassenID(AAOAO.this.straßen.getSelectedItem().toString());
               int fID = tabFahrzeug.getFahrzeugID(AAOAO.zugeordnet.getSelectedValue().toString());
               tabAAO.deleteOne(fID, stichwortID, straßenID);
               AAOAO.zugeordnet.setListData(Utils.listToArray(tabAAO.getZugeordneteFahrzeuge(stichwortID, straßenID)));
            } catch (SQLException var9) {
               logging.logPrintStackTrace(var9);
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
