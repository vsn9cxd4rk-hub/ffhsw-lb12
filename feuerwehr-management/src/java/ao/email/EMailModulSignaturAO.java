package ao.email;

import ao.AbstractFenster;
import data.tabellen.einstellungen.TabelleEinstellungen;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;
import utilities.Konstante;

public class EMailModulSignaturAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JComboBox type;
   private JLabel type_label;
   private JTextArea textfiled;
   private JScrollPane scrollPane;
   private JTextField textfiel_to;
   private JTextField textfiel_cc;
   private JTextField textfiel_bcc;
   private JLabel textfiel_to_label;
   private JLabel textfiel_cc_label;
   private JLabel textfiel_bcc_label;
   private JCheckBox signaturHinzufuegen;
   private JLabel signaturHinzufuegen_label;
   private JPanel panel;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public EMailModulSignaturAO() {
      super("FeuerwehrManagementSystem");
      logging.logInfo("Starte: EMailModulSignaturAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Zurück");
      this.buttonSpeichern = new JButton("Speichern");
      this.textfiled = new JTextArea();
      this.scrollPane = new JScrollPane(this.textfiled);
      this.scrollPane.setVerticalScrollBarPolicy(22);
      this.scrollPane.setPreferredSize(new Dimension(500, 300));
      this.signaturHinzufuegen = new JCheckBox();
      this.signaturHinzufuegen_label = new JLabel("Standard Signatur beim Speichern hinzufügen: ");
      this.textfiel_to = new JTextField(22);
      this.textfiel_cc = new JTextField(22);
      this.textfiel_bcc = new JTextField(22);
      this.textfiel_to_label = new JLabel("Standard An: ");
      this.textfiel_cc_label = new JLabel("Standard CC: ");
      this.textfiel_bcc_label = new JLabel("Standard BCC: ");
      this.modulBeschreibung = new JLabel("E-MailSignatur / Template Konfigurator");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {
      String[] typeListe = new String[]{"Signatur", "Einsatzbericht", "Mängelmeldung"};
      this.type = new JComboBox(typeListe);
      this.type_label = new JLabel("Signatur / Templatetyp: ");
      this.type.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent arg0) {
            EMailModulSignaturAO.this.textfiel_bcc.setVisible(true);
            EMailModulSignaturAO.this.textfiel_cc.setVisible(true);
            EMailModulSignaturAO.this.textfiel_to.setVisible(true);
            EMailModulSignaturAO.this.textfiel_to_label.setVisible(true);
            EMailModulSignaturAO.this.textfiel_cc_label.setVisible(true);
            EMailModulSignaturAO.this.textfiel_bcc_label.setVisible(true);
            EMailModulSignaturAO.this.signaturHinzufuegen.setVisible(true);
            EMailModulSignaturAO.this.signaturHinzufuegen_label.setVisible(true);
            if(EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Signatur")) {
               EMailModulSignaturAO.this.textfiled.setText((String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
               EMailModulSignaturAO.this.textfiel_bcc.setVisible(false);
               EMailModulSignaturAO.this.textfiel_cc.setVisible(false);
               EMailModulSignaturAO.this.textfiel_to.setVisible(false);
               EMailModulSignaturAO.this.textfiel_to_label.setVisible(false);
               EMailModulSignaturAO.this.textfiel_cc_label.setVisible(false);
               EMailModulSignaturAO.this.textfiel_bcc_label.setVisible(false);
               EMailModulSignaturAO.this.signaturHinzufuegen.setVisible(false);
               EMailModulSignaturAO.this.signaturHinzufuegen_label.setVisible(false);
            } else if(EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Einsatzbericht")) {
               EMailModulSignaturAO.this.textfiled.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzbericht"));
               EMailModulSignaturAO.this.textfiel_bcc.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtBCC"));
               EMailModulSignaturAO.this.textfiel_cc.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtCC"));
               EMailModulSignaturAO.this.textfiel_to.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateEinsatzberichtAN"));
            } else if(EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Mängelmeldung")) {
               EMailModulSignaturAO.this.textfiled.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldung"));
               EMailModulSignaturAO.this.textfiel_bcc.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldungBCC"));
               EMailModulSignaturAO.this.textfiel_cc.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldungCC"));
               EMailModulSignaturAO.this.textfiel_to.setText((String)runApplication.EINSTELLUNGEN.get("emailTemplateMängelmeldungAN"));
            }

         }
      });
      this.textfiled.setText((String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
   }

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - E-Mail Modul");
      this.setSize(560, 590);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
      this.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
            logging.logInfo("E-Mail Modul beenden");
         }
      });
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.type_label);
      this.add(this.type);
      this.add(this.scrollPane);
      this.add(this.dummy2);
      this.panel = new JPanel(new GridLayout(3, 2));
      this.getContentPane().add("Center", this.panel);
      this.panel.add(this.textfiel_to_label);
      this.panel.add(this.textfiel_to);
      this.panel.add(this.textfiel_cc_label);
      this.panel.add(this.textfiel_cc);
      this.panel.add(this.textfiel_bcc_label);
      this.panel.add(this.textfiel_bcc);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
      this.textfiel_bcc.setVisible(false);
      this.textfiel_cc.setVisible(false);
      this.textfiel_to.setVisible(false);
      this.textfiel_to_label.setVisible(false);
      this.textfiel_cc_label.setVisible(false);
      this.textfiel_bcc_label.setVisible(false);
      this.signaturHinzufuegen.setVisible(false);
      this.signaturHinzufuegen_label.setVisible(false);
      this.textfiled.setCaretPosition(0);
      this.textfiled.setWrapStyleWord(true);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleEinstellungen tabEinstellungen = new TabelleEinstellungen();

            try {
               if(EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Signatur")) {
                  tabEinstellungen.update("emailSignatur", EMailModulSignaturAO.this.textfiled.getText());
               } else if(EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Einsatzbericht")) {
                  tabEinstellungen.update("emailTemplateEinsatzberichtAN", EMailModulSignaturAO.this.textfiel_to.getText());
                  tabEinstellungen.update("emailTemplateEinsatzberichtCC", EMailModulSignaturAO.this.textfiel_cc.getText());
                  tabEinstellungen.update("emailTemplateEinsatzberichtBCC", EMailModulSignaturAO.this.textfiel_bcc.getText());
                  if(EMailModulSignaturAO.this.signaturHinzufuegen.isSelected()) {
                     tabEinstellungen.update("emailTemplateEinsatzbericht", EMailModulSignaturAO.this.textfiled.getText() + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
                  } else {
                     tabEinstellungen.update("emailTemplateEinsatzbericht", EMailModulSignaturAO.this.textfiled.getText());
                  }
               } else if(EMailModulSignaturAO.this.type.getSelectedItem().toString().equals("Mängelmeldung")) {
                  tabEinstellungen.update("emailTemplateMängelmeldungAN", EMailModulSignaturAO.this.textfiel_to.getText());
                  tabEinstellungen.update("emailTemplateMängelmeldungCC", EMailModulSignaturAO.this.textfiel_cc.getText());
                  tabEinstellungen.update("emailTemplateMängelmeldungBCC", EMailModulSignaturAO.this.textfiel_bcc.getText());
                  if(EMailModulSignaturAO.this.signaturHinzufuegen.isSelected()) {
                     tabEinstellungen.update("emailTemplateMängelmeldung", EMailModulSignaturAO.this.textfiled.getText() + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
                  } else {
                     tabEinstellungen.update("emailTemplateMängelmeldung", EMailModulSignaturAO.this.textfiled.getText());
                  }
               }

               runApplication.EINSTELLUNGEN = tabEinstellungen.getAllEinstellungen();
               JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_ERFOLGREICH);
            } catch (SQLException var4) {
               logging.logPrintStackTrace(var4);
            }

         }
      });
   }

   protected void labelErstellen() {}

   public void fensterAnzeigen() {
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }
}
