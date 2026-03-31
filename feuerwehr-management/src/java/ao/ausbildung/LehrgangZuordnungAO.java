package ao.ausbildung;

import ao.AbstractFenster;
import data.tabellen.TabelleLehrgang_kategorie;
import go.Lehrgang_Kategorie;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Utils;

public class LehrgangZuordnungAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   public JButton buttonZurueck;
   private JButton buttonRein;
   private JButton buttonRaus;
   private JButton buttonHoch;
   private JButton buttonRunter;
   public static JList nichtRelevantList;
   public static JList relevantList;
   public static JScrollPane pane_nichtRelevantList;
   public static JScrollPane pane_relevantList;
   private JLabel label_nichtrRelevantListe;
   private JLabel label_relevantListe;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JPanel panelListe;
   int datensatznummer;
   String datensatzname;


   public LehrgangZuordnungAO() {
      super("FeuerwehrManagementSystem - Lehrgangskonfiguration");
      logging.logInfo("Starte: LehrgangZuordnungAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Zurück");
      this.buttonRein = new JButton("←");
      this.buttonRein.setToolTipText("Hinzufügen");
      this.buttonRaus = new JButton("→");
      this.buttonRaus.setToolTipText("Entfernen");
      this.buttonHoch = new JButton("↑");
      this.buttonRunter = new JButton("↓");
      this.label_nichtrRelevantListe = new JLabel("Nicht Relevante Liste:                           ");
      this.label_relevantListe = new JLabel("Relevante Liste für Lehrgangsmeldungen:                          ");
      this.modulBeschreibung = new JLabel("Lehrgangskonfiguration");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {
      TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();

      try {
         String[] e = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
         String[] nichtRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleNichtRelevantenNamen());
         nichtRelevantList = new JList(nichtRelevant);
         nichtRelevantList.setVisibleRowCount(15);
         nichtRelevantList.setToolTipText("Liste der nicht Zugeordneten Relevanten Lehrgänge");
         pane_nichtRelevantList = new JScrollPane(nichtRelevantList);
         pane_nichtRelevantList.setVerticalScrollBarPolicy(22);
         pane_nichtRelevantList.setPreferredSize(new Dimension(300, 260));
         relevantList = new JList(e);
         relevantList.setVisibleRowCount(15);
         relevantList.setToolTipText("Liste der Zugeordneten Relevanten Lehrgänge");
         pane_relevantList = new JScrollPane(relevantList);
         pane_relevantList.setVerticalScrollBarPolicy(22);
         pane_relevantList.setPreferredSize(new Dimension(300, 260));
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Lehrgangskonfiguration");
      this.setSize(650, 430);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.label_relevantListe);
      this.add(this.label_nichtrRelevantListe);
      this.panelListe = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panelListe);
      this.panelListe.add(pane_relevantList);
      this.panelListe.add(pane_nichtRelevantList);
      this.add(this.dummy2);
      this.add(this.buttonHoch);
      this.add(this.buttonRunter);
      this.add(this.buttonRein);
      this.add(this.buttonRaus);
      this.add(this.buttonZurueck);
   }

   protected void boxenHinzufuegen() {}

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonRein.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
            Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();

            try {
               kategorie.setRelevant(1);
               kategorie.setReihenfolge(tabLehrgangKategorie.getNextReihenfolgenummerNummer());
               kategorie.setName(LehrgangZuordnungAO.nichtRelevantList.getSelectedValue().toString());
               tabLehrgangKategorie.update(kategorie);
               String[] e1 = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
               LehrgangZuordnungAO.relevantList.setListData(e1);
               String[] listDataNichtRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleNichtRelevantenNamen());
               LehrgangZuordnungAO.nichtRelevantList.setListData(listDataNichtRelevant);
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      this.buttonRaus.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
            Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();

            try {
               kategorie.setRelevant(0);
               kategorie.setReihenfolge(0);
               kategorie.setName(LehrgangZuordnungAO.relevantList.getSelectedValue().toString());
               tabLehrgangKategorie.update(kategorie);
               String[] e1 = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
               LehrgangZuordnungAO.relevantList.setListData(e1);
               String[] listDataNichtRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleNichtRelevantenNamen());
               LehrgangZuordnungAO.nichtRelevantList.setListData(listDataNichtRelevant);
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      this.buttonHoch.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
            Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();

            try {
               int e1 = LehrgangZuordnungAO.relevantList.getSelectedIndex();
               kategorie.setRelevant(1);
               kategorie.setReihenfolge(e1);
               kategorie.setName(LehrgangZuordnungAO.relevantList.getSelectedValue().toString());
               tabLehrgangKategorie.update(kategorie);
               LehrgangZuordnungAO.relevantList.setSelectedIndex(e1 - 1);
               kategorie.setRelevant(1);
               kategorie.setReihenfolge(e1 + 1);
               kategorie.setName(LehrgangZuordnungAO.relevantList.getSelectedValue().toString());
               tabLehrgangKategorie.update(kategorie);
               String[] listDataRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
               LehrgangZuordnungAO.relevantList.setListData(listDataRelevant);
               LehrgangZuordnungAO.relevantList.setSelectedIndex(e1 - 1);
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      this.buttonRunter.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
            Lehrgang_Kategorie kategorie = new Lehrgang_Kategorie();

            try {
               int e1 = LehrgangZuordnungAO.relevantList.getSelectedIndex();
               kategorie.setRelevant(1);
               kategorie.setReihenfolge(e1 + 2);
               kategorie.setName(LehrgangZuordnungAO.relevantList.getSelectedValue().toString());
               tabLehrgangKategorie.update(kategorie);
               LehrgangZuordnungAO.relevantList.setSelectedIndex(e1 + 1);
               kategorie.setRelevant(1);
               kategorie.setReihenfolge(e1 + 1);
               kategorie.setName(LehrgangZuordnungAO.relevantList.getSelectedValue().toString());
               tabLehrgangKategorie.update(kategorie);
               String[] listDataRelevant = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
               LehrgangZuordnungAO.relevantList.setListData(listDataRelevant);
               LehrgangZuordnungAO.relevantList.setSelectedIndex(e1 + 1);
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
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
