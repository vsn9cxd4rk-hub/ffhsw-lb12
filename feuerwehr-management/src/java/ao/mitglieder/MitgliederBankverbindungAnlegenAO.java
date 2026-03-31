package ao.mitglieder;

import ao.AbstractFenster;
import ao.mitglieder.MitgliederAnlegenAO;
import data.tabellen.mitglied.TabelleMitglieder_bankverbindung;
import go.Mitglieder_Bankverbindung;
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
import utilities.hash;
import utilities.logbuchEingabe;

public class MitgliederBankverbindungAnlegenAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextField name_mitglied;
   private JTextField personalnummer;
   private JTextField bic;
   private JTextField iban;
   private JLabel name_mitglied_label;
   private JLabel personalnummer_label;
   private JLabel bic_label;
   private JLabel iban_label;
   private JPanel panelMitglieder;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public MitgliederBankverbindungAnlegenAO() {
      super("FeuerwehrManagementSystem - Bankverbindung");
      logging.logInfo("Starte: BankverbindungUntersuchungAnlegenAO");
   }

   protected void buttonErstellen() {
      this.buttonSpeichern = new JButton("Speichern");
      this.buttonZurueck = new JButton("Zurück");
      this.personalnummer = new JTextField(MitgliederAnlegenAO.mitgliedID, 20);
      this.name_mitglied = new JTextField(MitgliederAnlegenAO.mitgliedName, 20);
      this.bic = new JTextField(20);
      this.iban = new JTextField(20);
      this.personalnummer_label = new JLabel("Personalnummer");
      this.name_mitglied_label = new JLabel("Mitglied Name: ");
      this.bic_label = new JLabel("BIC: ");
      this.iban_label = new JLabel("IBAN: ");
      this.modulBeschreibung = new JLabel("Mitglieder - Bankverbindung");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void labelErstellen() {
      TabelleMitglieder_bankverbindung tabBank = new TabelleMitglieder_bankverbindung();

      try {
         int e = Integer.parseInt(this.personalnummer.getText());
         if(tabBank.getCount(e) != 0) {
            this.bic.setText(hash.decodeHashCode(tabBank.getbic(Integer.parseInt(this.personalnummer.getText()))));
            this.iban.setText(hash.decodeHashCode(tabBank.getiban(Integer.parseInt(this.personalnummer.getText()))));
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
      this.setSize(580, 250);
      this.setTitle("FeuerwehrManagementSystem - Mitglieder Bankverbindung");
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.panelMitglieder = new JPanel(new GridLayout(4, 2));
      this.getContentPane().add("Center", this.panelMitglieder);
      this.panelMitglieder.add(this.personalnummer_label);
      this.panelMitglieder.add(this.personalnummer);
      this.panelMitglieder.add(this.name_mitglied_label);
      this.panelMitglieder.add(this.name_mitglied);
      this.panelMitglieder.add(this.iban_label);
      this.panelMitglieder.add(this.iban);
      this.panelMitglieder.add(this.bic_label);
      this.panelMitglieder.add(this.bic);
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
            TabelleMitglieder_bankverbindung tabBank = new TabelleMitglieder_bankverbindung();
            Mitglieder_Bankverbindung bank = new Mitglieder_Bankverbindung();

            try {
               int e = Integer.parseInt(MitgliederBankverbindungAnlegenAO.this.personalnummer.getText());
               bank.setId(e);
               bank.setIban(hash.createHashCode(MitgliederBankverbindungAnlegenAO.this.iban.getText()));
               bank.setBic(hash.createHashCode(MitgliederBankverbindungAnlegenAO.this.bic.getText()));
               if(tabBank.getCount(e) == 0) {
                  tabBank.insert(bank);
               } else {
                  tabBank.update(bank);
               }

               logging.logInfo("Untersuchung erfolgreich gespeichert");
               logbuchEingabe.NeuerEintag("Mitglieder Bankverbindung wurde eingetragen: " + e);
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
               MitgliederBankverbindungAnlegenAO.this.dispose();
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
