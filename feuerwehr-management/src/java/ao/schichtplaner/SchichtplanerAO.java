package ao.schichtplaner;

import ao.AbstractFenster;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.schicht.TabelleSchicht;
import data.tabellen.schicht.TabelleSchicht_gruppe;
import data.tabellen.schicht.TabelleSchicht_gruppen_mitglieder;
import data.tabellen.schicht.TabelleSchicht_mitglieder;
import go.schicht.SchichtMitglieder;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.Utils;

public class SchichtplanerAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSchichtErstellen;
   private JButton buttonSchichtGruppeErstellen;
   private JButton buttonSchichtGruppeBearbeiten;
   private JButton buttonRein;
   private JButton buttonRaus;
   private JButton buttonRausAlle;
   public static JComboBox schichtListe;
   private JLabel schichtListe_label;
   public static JRadioButton gruppenZuordnung;
   public static JRadioButton einzelZuordnung;
   private JLabel gruppenZuordnung_label;
   private JLabel einzelZuordnung_label;
   private ButtonGroup bg;
   public static JList nichtZugeordnet;
   public static JList zugeordnet;
   public static JScrollPane pane_nichtZugeordnet;
   public static JScrollPane pane_zugeordnet;
   private JLabel nichtZugeordnet_label;
   private JLabel zugeordnet_label;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;
   private JLabel dummy3;
   private JLabel dummy4;
   private JPanel panelListe;
   private JPanel panelSchichtListe;
   public static String letzteKategorie;


   public SchichtplanerAO() {
      super("FeuerwehrManagementSystem - Schichtplaner");
      logging.logInfo("Starte: SchichtplanerAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Schließen");
      this.buttonSchichtErstellen = new JButton("Schicht erstellen");
      this.buttonSchichtGruppeErstellen = new JButton("Schicht Gruppe erstellen");
      this.buttonSchichtGruppeBearbeiten = new JButton("Schicht Gruppe bearbeiten");
      this.buttonRein = new JButton("←");
      this.buttonRein.setToolTipText("Hinzufügen");
      this.buttonRaus = new JButton("→");
      this.buttonRaus.setToolTipText("Entfernen");
      this.buttonRausAlle = new JButton("→ →");
      this.buttonRausAlle.setToolTipText("Entferne Alle Zugeordneten Mitglieder");
      this.bg = new ButtonGroup();
      gruppenZuordnung = new JRadioButton();
      einzelZuordnung = new JRadioButton();
      this.bg.add(einzelZuordnung);
      this.bg.add(gruppenZuordnung);
      this.nichtZugeordnet_label = new JLabel("              Mitglieder ohne Zuordnung:             ");
      this.zugeordnet_label = new JLabel("Schichtteilnehmer:                                        ");
      this.schichtListe_label = new JLabel("Schichtliste: ");
      this.gruppenZuordnung_label = new JLabel("Schichtgruppenzuordnung: ");
      this.einzelZuordnung_label = new JLabel("Migliedereinzelzuordnung: ");
      this.modulBeschreibung = new JLabel("Schichtplaner");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
      this.dummy3 = new JLabel(runApplication.dummyImage);
      this.dummy4 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      TabelleSchicht tabSchicht = new TabelleSchicht();

      try {
         String[] e = Utils.listToArrayOnlyFORComboBoxes(tabSchicht.getAllSchichten());
         schichtListe = new JComboBox(e);
         nichtZugeordnet = new JList();
         nichtZugeordnet.setVisibleRowCount(15);
         nichtZugeordnet.setToolTipText("Liste aller verfügbaren Mitglieder");
         pane_nichtZugeordnet = new JScrollPane(nichtZugeordnet);
         pane_nichtZugeordnet.setVerticalScrollBarPolicy(22);
         pane_nichtZugeordnet.setPreferredSize(new Dimension(300, 260));
         zugeordnet = new JList();
         zugeordnet.setVisibleRowCount(15);
         zugeordnet.setToolTipText("Liste der Zugeordneten Mitglieder");
         pane_zugeordnet = new JScrollPane(zugeordnet);
         pane_zugeordnet.setVerticalScrollBarPolicy(22);
         pane_zugeordnet.setPreferredSize(new Dimension(300, 260));
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

      schichtListe.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            TabelleSchicht_mitglieder tabSchichtMitglieder = new TabelleSchicht_mitglieder();
            TabelleSchicht tabSchicht = new TabelleSchicht();

            try {
               int e = tabSchicht.getSchichtID(SchichtplanerAO.schichtListe.getSelectedItem().toString());
               if(SchichtplanerAO.einzelZuordnung.isSelected()) {
                  TabelleMitglied tabSchichtGruppe = new TabelleMitglied();
                  SchichtplanerAO.nichtZugeordnet.setListData(Utils.listToArray(tabSchichtGruppe.getMitgliederGruppe1()));
               } else if(SchichtplanerAO.gruppenZuordnung.isSelected()) {
                  TabelleSchicht_gruppe tabSchichtGruppe1 = new TabelleSchicht_gruppe();
                  SchichtplanerAO.nichtZugeordnet.setListData(Utils.listToArray(tabSchichtGruppe1.getAllSchichtGruppen()));
               }

               SchichtplanerAO.zugeordnet.setListData(Utils.listToArray(tabSchichtMitglieder.getMitglederEinerSchicht(e)));
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      einzelZuordnung.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            TabelleMitglied tabMitglied = new TabelleMitglied();

            try {
               SchichtplanerAO.nichtZugeordnet.setListData(Utils.listToArray(tabMitglied.getMitgliederGruppe1()));
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
            }

         }
      });
      gruppenZuordnung.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            TabelleSchicht_gruppe tabSchichtGruppe = new TabelleSchicht_gruppe();

            try {
               SchichtplanerAO.nichtZugeordnet.setListData(Utils.listToArray(tabSchichtGruppe.getAllSchichtGruppen()));
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
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
      this.setSize(650, 620);
      this.setTitle("FeuerwehrManagementSystem - Schichtplaner");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.buttonSchichtErstellen);
      this.add(this.buttonSchichtGruppeErstellen);
      this.add(this.buttonSchichtGruppeBearbeiten);
      this.add(this.dummy3);
      this.panelSchichtListe = new JPanel(new GridLayout(3, 2));
      this.getContentPane().add("Center", this.panelSchichtListe);
      this.panelSchichtListe.add(this.schichtListe_label);
      this.panelSchichtListe.add(schichtListe);
      this.panelSchichtListe.add(this.einzelZuordnung_label);
      this.panelSchichtListe.add(einzelZuordnung);
      this.panelSchichtListe.add(this.gruppenZuordnung_label);
      this.panelSchichtListe.add(gruppenZuordnung);
      this.add(this.dummy4);
      this.add(this.zugeordnet_label);
      this.add(this.nichtZugeordnet_label);
      this.panelListe = new JPanel(new GridLayout(1, 2));
      this.getContentPane().add("Center", this.panelListe);
      this.panelListe.add(pane_zugeordnet);
      this.panelListe.add(pane_nichtZugeordnet);
      this.add(this.buttonRein);
      this.add(this.buttonRaus);
      this.add(this.buttonRausAlle);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      gruppenZuordnung.setSelected(true);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSchichtErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.SCHICHT_ANLEGEN);
            Steuerung.steuerung();
         }
      });
      this.buttonSchichtGruppeErstellen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.SCHICHT_GRUPPE);
            Steuerung.steuerung();
         }
      });
      this.buttonSchichtGruppeBearbeiten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            MyEvent.setEvent("0x0501");
            Steuerung.setStatus(Status.SCHICHT_GRUPPE);
            Steuerung.steuerung();
         }
      });
      this.buttonRein.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleSchicht_mitglieder tabSchichtMitglieder = new TabelleSchicht_mitglieder();
            TabelleSchicht tabSchicht = new TabelleSchicht();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            SchichtMitglieder schichtMitglieder = new SchichtMitglieder();

            try {
               if(SchichtplanerAO.schichtListe.getSelectedItem().toString().equals("<bitte wählen>")) {
                  JOptionPane.showMessageDialog((Component)null, Konstante.BITTE_SCHICHT_WAEHLEN, "Warnung", 2);
               } else {
                  int e = tabSchicht.getSchichtID(SchichtplanerAO.schichtListe.getSelectedItem().toString());
                  if(SchichtplanerAO.einzelZuordnung.isSelected()) {
                     int listeZugeordnet = tabMitglied.getIdByGuiString(SchichtplanerAO.nichtZugeordnet.getSelectedValue().toString());
                     if(tabSchichtMitglieder.getCountOfMitglieder(e, listeZugeordnet) != 0) {
                        JOptionPane.showMessageDialog((Component)null, Konstante.MITGLIED_BETREIS_VORHANDEN, "Warnung", 2);
                     } else {
                        schichtMitglieder.setSchichtID(e);
                        schichtMitglieder.setMitgliederID(listeZugeordnet);
                        tabSchichtMitglieder.insert(schichtMitglieder);
                     }
                  } else if(SchichtplanerAO.gruppenZuordnung.isSelected()) {
                     TabelleSchicht_gruppen_mitglieder var12 = new TabelleSchicht_gruppen_mitglieder();
                     TabelleSchicht_gruppe tabSchichtGruppen = new TabelleSchicht_gruppe();
                     int[] mitgliederListe = Utils.listToIntArray(var12.getMitglederIDEinerSchichtGruppe(tabSchichtGruppen.getGruppenID(SchichtplanerAO.nichtZugeordnet.getSelectedValue().toString())));

                     for(int i = 0; i < mitgliederListe.length; ++i) {
                        schichtMitglieder.setSchichtID(e);
                        schichtMitglieder.setMitgliederID(mitgliederListe[i]);
                        tabSchichtMitglieder.insert(schichtMitglieder);
                     }
                  }

                  String[] var13 = Utils.listToArray(tabSchichtMitglieder.getMitglederEinerSchicht(e));
                  SchichtplanerAO.zugeordnet.setListData(var13);
               }
            } catch (SQLException var11) {
               logging.logPrintStackTrace(var11);
            }

         }
      });
      this.buttonRaus.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleSchicht_mitglieder tabSchichtMitglieder = new TabelleSchicht_mitglieder();
            TabelleSchicht tabSchicht = new TabelleSchicht();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            SchichtMitglieder schichtMitglieder = new SchichtMitglieder();

            try {
               int e = tabSchicht.getSchichtID(SchichtplanerAO.schichtListe.getSelectedItem().toString());
               int mID = tabMitglied.getIdByGuiString(SchichtplanerAO.zugeordnet.getSelectedValue().toString());
               schichtMitglieder.setSchichtID(e);
               schichtMitglieder.setMitgliederID(mID);
               tabSchichtMitglieder.deleteOne(schichtMitglieder);
               String[] listeZugeordnet = Utils.listToArray(tabSchichtMitglieder.getMitglederEinerSchicht(e));
               SchichtplanerAO.zugeordnet.setListData(listeZugeordnet);
            } catch (SQLException var9) {
               logging.logPrintStackTrace(var9);
            }

         }
      });
      this.buttonRausAlle.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleSchicht tabSchicht = new TabelleSchicht();
            TabelleSchicht_mitglieder tabSchichtMitglieder = new TabelleSchicht_mitglieder();

            try {
               int e = tabSchicht.getSchichtID(SchichtplanerAO.schichtListe.getSelectedItem().toString());
               tabSchichtMitglieder.deleteAlleSchichtMitglieder(e);
               String[] listeZugeordnet = Utils.listToArray(tabSchichtMitglieder.getMitglederEinerSchicht(e));
               SchichtplanerAO.zugeordnet.setListData(listeZugeordnet);
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
