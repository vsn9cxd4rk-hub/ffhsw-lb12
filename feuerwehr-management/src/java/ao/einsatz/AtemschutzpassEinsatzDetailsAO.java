package ao.einsatz;

import ao.AbstractFenster;
import ao.einsatz.AtemschutzpassAO;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;

public class AtemschutzpassEinsatzDetailsAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JButton buttonZurueck;
   private JButton buttonSpeichern;
   private JTextArea textfiled;
   private JScrollPane scrollPane;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public AtemschutzpassEinsatzDetailsAO() {
      super("FeuerwehrManagementSystem");
      logging.logInfo("Starte: AtemschutzpassEinsatzDetailsAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Zurück");
      this.buttonSpeichern = new JButton("Speichern");
      this.textfiled = new JTextArea();
      this.scrollPane = new JScrollPane(this.textfiled);
      this.scrollPane.setVerticalScrollBarPolicy(22);
      this.scrollPane.setPreferredSize(new Dimension(450, 300));
      this.modulBeschreibung = new JLabel("Atemschutzpass - Einsatz Details");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {
      if(!AtemschutzpassAO.einsatzDetailInformationen[AtemschutzpassAO.einsatzDetailInformationenID].equals("")) {
         this.textfiled.setText(AtemschutzpassAO.einsatzDetailInformationen[AtemschutzpassAO.einsatzDetailInformationenID]);
      }

   }

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - Atemschutzpass - Einsatz Details");
      this.setSize(500, 450);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.scrollPane);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
      this.add(this.buttonSpeichern);
      this.textfiled.setCaretPosition(0);
      this.textfiled.setWrapStyleWord(true);
   }

   protected void boxenHinzufuegen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.buttonSpeichern.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            AtemschutzpassAO.einsatzDetailInformationen[AtemschutzpassAO.einsatzDetailInformationenID] = AtemschutzpassEinsatzDetailsAO.this.textfiled.getText();
            AtemschutzpassEinsatzDetailsAO.this.dispose();
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
