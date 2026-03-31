package ao.email;

import ao.AbstractFenster;
import ao.email.NeueEMailAO;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import logging.logging;
import run.runApplication;
import utilities.Utils;

public class EMail_AdressbuchAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonTO;
   private JButton buttonCC;
   private JButton buttonBCC;
   private JTextField textfiel_to;
   private JTextField textfiel_cc;
   private JTextField textfiel_bcc;
   private JLabel textfiel_to_label;
   private JLabel textfiel_cc_label;
   private JLabel textfiel_bcc_label;
   private JLabel type_label;
   private JComboBox type;
   private JPanel panel;
   private JList liste;
   private JScrollPane pane_liste;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public EMail_AdressbuchAO() {
      super("FeuerwehrManagementSystem - Adressbauch");
      logging.logInfo("Starte: AdressbuchAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Übernehmen");
      this.buttonTO = new JButton(" An --> ");
      this.buttonCC = new JButton(" CC --> ");
      this.buttonBCC = new JButton(" BCC --> ");
      this.textfiel_to = new JTextField(25);
      this.textfiel_cc = new JTextField(25);
      this.textfiel_bcc = new JTextField(25);
      this.textfiel_to_label = new JLabel("An: ");
      this.textfiel_cc_label = new JLabel("CC: ");
      this.textfiel_bcc_label = new JLabel("BCC: ");
      this.modulBeschreibung = new JLabel("Adressbuch");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {
      this.liste = new JList();
      this.liste.setVisibleRowCount(15);
      this.liste.setToolTipText("Liste der verfügbaren Adressen");
      this.pane_liste = new JScrollPane(this.liste);
      this.pane_liste.setVerticalScrollBarPolicy(22);
      this.pane_liste.setPreferredSize(new Dimension(600, 200));
      TabelleMitglied tabMitglied = new TabelleMitglied();

      String[] listeType;
      try {
         listeType = Utils.listToArray(tabMitglied.getAllMitgliederFromDataBaseWithEMail("email"));
         this.liste.setListData(listeType);
         this.textfiel_to.setText(NeueEMailAO.fieldAn.getText());
         this.textfiel_cc.setText(NeueEMailAO.fieldCC.getText());
         this.textfiel_bcc.setText(NeueEMailAO.fieldBCC.getText());
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

      listeType = new String[]{"E-Mail", "E-Mail2", "E-Mail Verteiler"};
      this.type = new JComboBox(listeType);
      this.type_label = new JLabel("Typ: ");
      this.type.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            try {
               TabelleMitglied e = new TabelleMitglied();
               String[] adressListe = null;
               if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail")) {
                  adressListe = Utils.listToArray(e.getAllMitgliederFromDataBaseWithEMail("email"));
               } else if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail2")) {
                  adressListe = Utils.listToArray(e.getAllMitgliederFromDataBaseWithEMail("email2"));
               } else if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail Verteiler")) {
                  adressListe = Utils.listToArray((new TabelleMitglieder_gruppe()).getAllGruppen());
               }

               EMail_AdressbuchAO.this.liste.setListData(adressListe);
               EMail_AdressbuchAO.this.textfiel_to.setText(NeueEMailAO.fieldAn.getText());
               EMail_AdressbuchAO.this.textfiel_cc.setText(NeueEMailAO.fieldCC.getText());
               EMail_AdressbuchAO.this.textfiel_bcc.setText(NeueEMailAO.fieldBCC.getText());
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
            }

         }
      });
   }

   protected void boxenHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Adressbauch");
      this.setSize(650, 480);
      this.setDefaultCloseOperation(0);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.type_label);
      this.add(this.type);
      this.add(this.pane_liste);
      this.add(this.buttonTO);
      this.add(this.buttonCC);
      this.add(this.buttonBCC);
      this.panel = new JPanel(new GridLayout(3, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.textfiel_to_label);
      this.panel.add(this.textfiel_to);
      this.panel.add(this.textfiel_cc_label);
      this.panel.add(this.textfiel_cc);
      this.panel.add(this.textfiel_bcc_label);
      this.panel.add(this.textfiel_bcc);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
   }

   protected void labelHinzufuegen() {}

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            NeueEMailAO.fieldAn.setText(EMail_AdressbuchAO.this.textfiel_to.getText());
            NeueEMailAO.fieldCC.setText(EMail_AdressbuchAO.this.textfiel_cc.getText());
            NeueEMailAO.fieldBCC.setText(EMail_AdressbuchAO.this.textfiel_bcc.getText());
            EMail_AdressbuchAO.this.dispose();
         }
      });
      this.buttonZurueck.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            NeueEMailAO.fieldAn.setText(EMail_AdressbuchAO.this.textfiel_to.getText());
            NeueEMailAO.fieldCC.setText(EMail_AdressbuchAO.this.textfiel_cc.getText());
            NeueEMailAO.fieldBCC.setText(EMail_AdressbuchAO.this.textfiel_bcc.getText());
            EMail_AdressbuchAO.this.dispose();
         }
      });
      this.liste.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
               EMail_AdressbuchAO.this.buttonTO.doClick();
            }

         }
      });
      this.buttonTO.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
            StringBuilder build = new StringBuilder();

            try {
               if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail Verteiler")) {
                  String[] e = Utils.listToArray(tabMitglied.getAlleEMailEinerMitgliederGruppe(tabGruppe.getID(EMail_AdressbuchAO.this.liste.getSelectedValue().toString())));

                  for(int adress = 0; adress < e.length; ++adress) {
                     build.append(e[adress]);
                     build.append(", ");
                  }

                  EMail_AdressbuchAO.this.textfiel_to.setText(build.toString());
               } else {
                  int var8 = tabMitglied.getIdByGuiString(EMail_AdressbuchAO.this.liste.getSelectedValue().toString());
                  String var9 = null;
                  if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail")) {
                     var9 = tabMitglied.getEMail(var8);
                  } else if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail2")) {
                     var9 = tabMitglied.getEMail2(var8);
                  }

                  if(EMail_AdressbuchAO.this.textfiel_to.getText().equals("")) {
                     EMail_AdressbuchAO.this.textfiel_to.setText(var9);
                  } else {
                     build.append(EMail_AdressbuchAO.this.textfiel_to.getText());
                     build.append(", ");
                     build.append(var9);
                     EMail_AdressbuchAO.this.textfiel_to.setText(build.toString());
                  }
               }
            } catch (SQLException var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      });
      this.buttonCC.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            StringBuilder build = new StringBuilder();
            TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

            try {
               if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail Verteiler")) {
                  String[] e = Utils.listToArray(tabMitglied.getAlleEMailEinerMitgliederGruppe(tabGruppe.getID(EMail_AdressbuchAO.this.liste.getSelectedValue().toString())));

                  for(int adress = 0; adress < e.length; ++adress) {
                     build.append(e[adress]);
                     build.append(", ");
                  }

                  EMail_AdressbuchAO.this.textfiel_cc.setText(build.toString());
               } else {
                  int var8 = tabMitglied.getIdByGuiString(EMail_AdressbuchAO.this.liste.getSelectedValue().toString());
                  String var9 = null;
                  if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail")) {
                     var9 = tabMitglied.getEMail(var8);
                  } else if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail2")) {
                     var9 = tabMitglied.getEMail2(var8);
                  }

                  if(EMail_AdressbuchAO.this.textfiel_cc.getText().equals("")) {
                     EMail_AdressbuchAO.this.textfiel_cc.setText(var9);
                  } else {
                     build.append(EMail_AdressbuchAO.this.textfiel_cc.getText());
                     build.append(", ");
                     build.append(var9);
                     EMail_AdressbuchAO.this.textfiel_cc.setText(build.toString());
                  }
               }
            } catch (SQLException var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      });
      this.buttonBCC.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            StringBuilder build = new StringBuilder();
            TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();

            try {
               if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail Verteiler")) {
                  String[] e = Utils.listToArray(tabMitglied.getAlleEMailEinerMitgliederGruppe(tabGruppe.getID(EMail_AdressbuchAO.this.liste.getSelectedValue().toString())));

                  for(int adress = 0; adress < e.length; ++adress) {
                     build.append(e[adress]);
                     build.append(", ");
                  }

                  EMail_AdressbuchAO.this.textfiel_bcc.setText(build.toString());
               } else {
                  int var8 = tabMitglied.getIdByGuiString(EMail_AdressbuchAO.this.liste.getSelectedValue().toString());
                  String var9 = null;
                  if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail")) {
                     var9 = tabMitglied.getEMail(var8);
                  } else if(EMail_AdressbuchAO.this.type.getSelectedItem().toString().equals("E-Mail2")) {
                     var9 = tabMitglied.getEMail2(var8);
                  }

                  if(EMail_AdressbuchAO.this.textfiel_bcc.getText().equals("")) {
                     EMail_AdressbuchAO.this.textfiel_bcc.setText(var9);
                  } else {
                     build.append(EMail_AdressbuchAO.this.textfiel_bcc.getText());
                     build.append(", ");
                     build.append(var9);
                     EMail_AdressbuchAO.this.textfiel_bcc.setText(build.toString());
                  }
               }
            } catch (SQLException var7) {
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
