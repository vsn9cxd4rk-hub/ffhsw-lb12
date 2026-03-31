package ao.email;

import ao.AbstractFenster;
import ao.email.EMailModulAO;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import listener.DisposeListener;
import logging.logging;
import run.runApplication;

public class EMailAttachmentAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   public static String dataForMail = null;
   private JButton buttonZurueck;
   private JButton buttonAnsehen;
   private JButton buttonDrucken;
   private JList liste;
   private JScrollPane pane_liste;
   private JLabel modulBeschreibung;
   private JLabel dummy;
   private JLabel dummy2;


   public EMailAttachmentAO() {
      super("FeuerwehrManagementSystem - E-Mail Anhang ansehen");
      logging.logInfo("Starte: AttachmentAO");
   }

   protected void buttonErstellen() {
      this.buttonZurueck = new JButton("Zurück");
      this.buttonAnsehen = new JButton("Öffnen");
      this.buttonDrucken = new JButton("Drucken");
      this.modulBeschreibung = new JLabel("E-Mail Anhang ansehen (Nachricht: " + EMailModulAO.staticAttachment.getId() + ", " + EMailModulAO.staticAttachment.getOrdner() + ")");
      this.dummy = new JLabel(runApplication.dummyImage);
      this.dummy2 = new JLabel(runApplication.dummyImage);
   }

   protected void setzeAuswahllisten() {
      this.liste = new JList();
      this.liste.setVisibleRowCount(15);
      this.liste.setToolTipText("Liste der verfügbaren Anhänge");
      this.pane_liste = new JScrollPane(this.liste);
      this.pane_liste.setVerticalScrollBarPolicy(22);
      this.pane_liste.setPreferredSize(new Dimension(600, 200));
   }

   protected void boxenHinzufuegen() {
      File ordner = null;
      String var2;
      switch((var2 = EMailModulAO.staticAttachment.getOrdner()).hashCode()) {
      case -1598226551:
         if(var2.equals("Posteingang Ungelesen")) {
            ordner = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Empfangende/" + EMailModulAO.staticAttachment.getId());
            logging.logInfo("Ordner: " + ordner);
         }
         break;
      case 57663362:
         if(var2.equals("Posteingang Gelesen")) {
            ordner = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Empfangende/" + EMailModulAO.staticAttachment.getId());
            logging.logInfo("Ordner: " + ordner);
         }
         break;
      case 73240797:
         if(var2.equals("Entwurf")) {
            ordner = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Entwurf/" + EMailModulAO.staticAttachment.getId());
            logging.logInfo("Ordner: " + ordner);
         }
         break;
      case 1079996702:
         if(var2.equals("Gesendete Objekte")) {
            ordner = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Gesendet/" + EMailModulAO.staticAttachment.getId());
            logging.logInfo("Ordner: " + ordner);
         }
      }

      File[] files = ordner.listFiles();
      this.liste.setListData(files);
      EMailModulAO.staticAttachment = null;
   }

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - E-Mail Anhang ansehen");
      this.setSize(650, 380);
      this.setDefaultCloseOperation(2);
      Image icon = runApplication.icon.getImage();
      this.setIconImage(icon);
   }

   protected void buttonHinzufuegen() {
      this.add(this.modulBeschreibung);
      this.add(this.dummy);
      this.add(this.pane_liste);
      this.add(this.buttonAnsehen);
      this.add(this.buttonDrucken);
      this.add(this.dummy2);
      this.add(this.buttonZurueck);
   }

   protected void labelHinzufuegen() {}

   protected void labelErstellen() {}

   protected void actionErzeugen() {
      this.buttonZurueck.addActionListener(new DisposeListener(this));
      this.liste.addMouseListener(new MouseAdapter() {
         public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2) {
               EMailAttachmentAO.this.buttonAnsehen.doClick();
            }

         }
      });
      this.buttonDrucken.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               String e = EMailAttachmentAO.this.liste.getSelectedValue().toString();
               Desktop.getDesktop().print(new File(e));
            } catch (IOException var3) {
               logging.logPrintStackTrace(var3);
            }

         }
      });
      this.buttonAnsehen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               String e = EMailAttachmentAO.this.liste.getSelectedValue().toString();
               Desktop.getDesktop().open(new File(e));
            } catch (IOException var3) {
               logging.logPrintStackTrace(var3);
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
