package ao.unwetterwarnung;

import ao.AbstractFenster;
import data.tabellen.email.TabelleEMail_unwetterwarnung;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;

public class UnwetterwarnungAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   private JTextArea textfield;
   private JScrollPane pane;
   private JButton buttonZurück;
   private JButton buttonDWDSeite;


   public UnwetterwarnungAO() {
      super("Unwetterwarnung");
      logging.logInfo("Starte: UnwetterwarnungAO");
   }

   protected void buttonErstellen() {
      this.buttonZurück = new JButton("Zurück");
      this.buttonDWDSeite = new JButton("Internet - DWD.de");
      this.buttonDWDSeite.setToolTipText("Öffnet den Browser mit: www.dwd.de");
      this.textfield = new JTextArea(20, 50);
      this.textfield.setLineWrap(true);
      this.textfield.setWrapStyleWord(true);
      this.textfield.setEditable(false);
      this.pane = new JScrollPane(this.textfield);
      this.pane.setVerticalScrollBarPolicy(22);

      try {
         TabelleEMail_unwetterwarnung e = new TabelleEMail_unwetterwarnung();
         this.textfield.setText(e.getNachricht(e.getLastWarnungID()));
         this.textfield.setCaretPosition(0);
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
      }

   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("Aktive Unwetterwarnung");
      this.setSize(600, 410);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.pane);
      this.add(this.buttonZurück);
      this.add(this.buttonDWDSeite);
   }

   protected void boxenHinzufuegen() {}

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.buttonZurück.addActionListener(new DisposeListener(this));
      this.buttonDWDSeite.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            try {
               Desktop.getDesktop().browse(new URI("http://www.dwd.de"));
            } catch (URISyntaxException var3) {
               logging.logError("Beim öffnen des Browsers ist ein Fehler aufgetreten...");
            }

         }
      });
   }

   public void fensterAnzeigen() {
      this.setVisible(true);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }

   public void fensterSchlissen() {
      this.dispose();
   }
}
