package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.mitglied.TabelleMitglieder_arbeit;
import go.Mitglieder_Arbeit;
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

public class MitgliederArbeitAnlegenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextField name_mitglied;
   private JTextField personalnummer;
   private JTextField name;
   private JTextField strasse;
   private JTextField ort;
   private JTextField telPrivat;
   private JTextField ansprechpartner;
   private JTextField email;
   private JLabel name_mitglied_label;
   private JLabel personalnummer_label;
   private JLabel name_label;
   private JLabel strasse_label;
   private JLabel ort_label;
   private JLabel telPrivat_label;
   private JLabel ansprechpartner_label;
   private JLabel email_label;
   private JPanel panelMitglieder;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public MitgliederArbeitAnlegenAO() {
      super("FeuerwehrManagementSystem - Arbeitgeber");
      logging.logInfo("Starte: MitgliederArbeitAnlegenAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.personalnummer = new JTextField(MitgliederAnlegenAO.mitgliedID, 20);
      this.name_mitglied = new JTextField(MitgliederAnlegenAO.mitgliedName, 20);
      this.name = new JTextField(20);
      this.strasse = new JTextField(20);
      this.ort = new JTextField(20);
      this.telPrivat = new JTextField(20);
      this.ansprechpartner = new JTextField(20);
      this.email = new JTextField(20);
      this.personalnummer_label = new JLabel("Personalnummer");
      this.name_mitglied_label = new JLabel("Mitglied Name: ");
      this.name_label = new JLabel("Firma: ");
      this.strasse_label = new JLabel("Straße: ");
      this.ort_label = new JLabel("PLZ und Ort: ");
      this.telPrivat_label = new JLabel("Telefon: ");
      this.email_label = new JLabel("E-Mail: ");
      this.ansprechpartner_label = new JLabel("Ansprechpartner: ");
      this.modulBeschreibung = new JLabel("Mitglieder - Arbeitgeber");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      TabelleMitglieder_arbeit arbeit = new TabelleMitglieder_arbeit();

      try {
         int e = Integer.parseInt(this.personalnummer.getText());
         if(arbeit.getCount(e) != 0) {
            this.name.setText(arbeit.getName(e));
            this.strasse.setText(arbeit.getStrasse(e));
            this.ort.setText(arbeit.getOrt(e));
            this.telPrivat.setText(arbeit.getTelefon(e));
            this.email.setText(arbeit.getEMail(e));
            this.ansprechpartner.setText(arbeit.getAnsprechpartner(e));
         }
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
      this.setSize(580, 310);
      this.setTitle("FeuerwehrManagementSystem - Mitglieder Arbeitgeber");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelMitglieder = new JPanel(new GridLayout(8, 2));
      this.getContentPane().add("Center", this.panelMitglieder);
      this.panelMitglieder.add(this.personalnummer_label);
      this.panelMitglieder.add(this.personalnummer);
      this.panelMitglieder.add(this.name_mitglied_label);
      this.panelMitglieder.add(this.name_mitglied);
      this.panelMitglieder.add(this.name_label);
      this.panelMitglieder.add(this.name);
      this.panelMitglieder.add(this.strasse_label);
      this.panelMitglieder.add(this.strasse);
      this.panelMitglieder.add(this.ort_label);
      this.panelMitglieder.add(this.ort);
      this.panelMitglieder.add(this.telPrivat_label);
      this.panelMitglieder.add(this.telPrivat);
      this.panelMitglieder.add(this.email_label);
      this.panelMitglieder.add(this.email);
      this.panelMitglieder.add(this.ansprechpartner_label);
      this.panelMitglieder.add(this.ansprechpartner);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
      this.personalnummer.setEditable(false);
      this.name_mitglied.setEditable(false);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleMitglieder_arbeit tabArbeit = new TabelleMitglieder_arbeit();
            Mitglieder_Arbeit arbeit = new Mitglieder_Arbeit();

            try {
               int e = Integer.parseInt(MitgliederArbeitAnlegenAO.this.personalnummer.getText());
               arbeit.setId(e);
               arbeit.setName(MitgliederArbeitAnlegenAO.this.name.getText());
               arbeit.setStrasse(MitgliederArbeitAnlegenAO.this.strasse.getText());
               arbeit.setOrt(MitgliederArbeitAnlegenAO.this.ort.getText());
               arbeit.setTelefon(MitgliederArbeitAnlegenAO.this.telPrivat.getText());
               arbeit.setAnsprechpartner(MitgliederArbeitAnlegenAO.this.ansprechpartner.getText());
               arbeit.setEmail(MitgliederArbeitAnlegenAO.this.email.getText());
               if(tabArbeit.getCount(e) == 0) {
                  tabArbeit.insert(arbeit);
               } else {
                  tabArbeit.update(arbeit);
               }

               logging.logInfo("Arbeitgeber erfolgreich gespeichert");
               logbuchEingabe.NeuerEintag("Mitglieder Arbeit wurde angelegt: " + e);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               MitgliederArbeitAnlegenAO.this.dispose();
            } catch (SQLException var5) {
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
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
