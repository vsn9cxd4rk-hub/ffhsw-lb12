package ao.email;

import ao.AbstractFenster;
import ao.utils.ProzessBarAO;
import data.tabellen.email.TabelleEMail_empfangen;
import data.tabellen.email.TabelleEMail_entwurf;
import data.tabellen.email.TabelleEMail_gesendet;
import go.email.Antworten;
import go.email.Attachment;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.CreateTrees;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.TimeCalculation;
import utilities_email.DeleteAndDisplayEMail;
import utilities_email.EMail_utils;
import utilities_email.EmpfangenOpperation;

public class EMailModulAO extends AbstractFenster {

   private static final long serialVersionUID = 1L;
   public static Antworten staticAntworten = null;
   public static Attachment staticAttachment = null;
   private JButton buttonDelete;
   private JButton buttonNeueEMail;
   private JButton buttonEmpfangen;
   private JButton buttonSignatur;
   public static JButton buttonAttachment;
   private JButton buttonAntworten;
   private JButton buttonWeiterleiten;
   private JButton buttonBearbeiten;
   public static JList mailListe;
   private JScrollPane mailListe_ungelesen_pane;
   public static JTree tree;
   private JScrollPane scrollPaneTree;
   public static JTextPane textfeld_email;
   private JScrollPane pane;
   public static JLabel status;
   private JLabel dummy;


   public EMailModulAO() {
      super("FeuerwehrManagementSystem");
      logging.logInfo("Starte: EMailModulAO");
   }

   protected void buttonErstellen() {
      this.buttonDelete = new JButton("Löschen");
      this.buttonNeueEMail = new JButton("Neue E-Mail schreiben");
      this.buttonSignatur = new JButton("Signatur erstellen");
      this.buttonEmpfangen = new JButton("E-Mails Empfangen");
      buttonAttachment = new JButton("Anhang ansehen");
      this.buttonAntworten = new JButton("Antworten");
      this.buttonWeiterleiten = new JButton("Weiterleiten");
      this.buttonBearbeiten = new JButton("Bearbeiten");
      tree = new JTree(CreateTrees.CreateEMailTree());
      tree.setSelectionRow(1);
      this.scrollPaneTree = new JScrollPane(tree);
      this.scrollPaneTree.setVerticalScrollBarPolicy(22);
      textfeld_email = new JTextPane();
      this.pane = new JScrollPane(textfeld_email);
      this.pane.setVerticalScrollBarPolicy(22);
      this.dummy = new JLabel(runApplication.dummyImage);
      status = new JLabel();
      mailListe = new JList();
      this.mailListe_ungelesen_pane = new JScrollPane(mailListe);
      mailListe.setVisibleRowCount(15);
      this.mailListe_ungelesen_pane.setVerticalScrollBarPolicy(22);
   }

   protected void setzeAuswahllisten() {}

   protected void labelHinzufuegen() {}

   protected void layoutFestlegen() {
      this.layout.setHgap(10);
      this.layout.setVgap(10);
      this.layout.setAlignment(1);
      this.setLayout(this.layout);
      this.setTitle("FeuerwehrManagementSystem - E-Mail Modul");
      this.setSize(1200, 800);
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
      this.scrollPaneTree.setPreferredSize(new Dimension(200, 700));
      this.mailListe_ungelesen_pane.setPreferredSize(new Dimension(300, 700));
      this.pane.setPreferredSize(new Dimension(600, 700));
      textfeld_email.setEditable(false);
      this.add(this.buttonNeueEMail);
      this.add(this.buttonBearbeiten);
      this.add(this.buttonAntworten);
      this.add(this.buttonWeiterleiten);
      this.add(buttonAttachment);
      this.add(this.buttonDelete);
      this.add(this.buttonSignatur);
      this.add(this.dummy);
      this.add(this.scrollPaneTree);
      this.add(this.mailListe_ungelesen_pane);
      this.add(this.pane);
      this.add(status);
      buttonAttachment.setVisible(false);
      this.buttonBearbeiten.setVisible(false);
      tree.setSelectionRow(0);
   }

   protected void boxenHinzufuegen() {}

   protected void labelErstellen() {
      buttonAttachment.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            EMailModulAO.staticAttachment = new Attachment();
            int nID = Integer.parseInt(EMailModulAO.mailListe.getSelectedValue().toString().substring(11, EMailModulAO.mailListe.getSelectedValue().toString().length()));
            EMailModulAO.staticAttachment.setId(nID);
            EMailModulAO.staticAttachment.setOrdner(EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString());
            Steuerung.setStatus(Status.EMAIL_ANHANG);
            Steuerung.steuerung();
         }
      });
      this.buttonAntworten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleEMail_empfangen tabEmpf = new TabelleEMail_empfangen();
            TabelleEMail_gesendet tabGes = new TabelleEMail_gesendet();
            EMailModulAO.staticAntworten = new Antworten();

            try {
               int e = Integer.parseInt(EMailModulAO.mailListe.getSelectedValue().toString().substring(11, EMailModulAO.mailListe.getSelectedValue().toString().length()));
               String var5;
               switch((var5 = EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString()).hashCode()) {
               case -1598226551:
                  if(var5.equals("Posteingang Ungelesen")) {
                     EMailModulAO.staticAntworten.setBetreff("Aw: " + tabEmpf.getBetreff(e));
                     EMailModulAO.staticAntworten.setNachricht(tabEmpf.getNachricht(e));
                     EMailModulAO.staticAntworten.setSender(tabEmpf.getSender(e));
                     EMailModulAO.staticAntworten.setDate(tabEmpf.getDate(e));
                  }
                  break;
               case 57663362:
                  if(var5.equals("Posteingang Gelesen")) {
                     EMailModulAO.staticAntworten.setBetreff("Aw: " + tabEmpf.getBetreff(e));
                     EMailModulAO.staticAntworten.setNachricht(tabEmpf.getNachricht(e));
                     EMailModulAO.staticAntworten.setSender(tabEmpf.getSender(e));
                     EMailModulAO.staticAntworten.setDate(tabEmpf.getDate(e));
                  }
                  break;
               case 1079996702:
                  if(var5.equals("Gesendete Objekte")) {
                     EMailModulAO.staticAntworten.setBetreff("Aw: " + tabGes.getBetreff(e));
                     EMailModulAO.staticAntworten.setNachricht(tabGes.getNachricht(e));
                     EMailModulAO.staticAntworten.setSender(tabGes.getSender(e));
                     EMailModulAO.staticAntworten.setDate(TimeCalculation.parseDateForGUI(tabGes.getDate(e)));
                  }
               }

               Steuerung.setStatus(Status.NEUE_EMAIL);
               Steuerung.steuerung();
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      this.buttonBearbeiten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            try {
               TabelleEMail_entwurf e = new TabelleEMail_entwurf();
               int nID = Integer.parseInt(EMailModulAO.mailListe.getSelectedValue().toString().substring(11, EMailModulAO.mailListe.getSelectedValue().toString().length()));
               runApplication.mailData.setStatus(1);
               HashMap map = e.getEntwurfMail(nID);
               if(((String)map.get("anhang")).equals("")) {
                  runApplication.mailData.setAnhang("");
               } else {
                  runApplication.mailData.setAnhang(((String)map.get("anhang")).substring(0, ((String)map.get("anhang")).length()));
               }

               runApplication.mailData.setAn((String)map.get("AN"));
               runApplication.mailData.setCc((String)map.get("CC"));
               runApplication.mailData.setBcc((String)map.get("BCC"));
               runApplication.mailData.setBetreff((String)map.get("betreff"));
               runApplication.mailData.seteMailText((String)map.get("nachricht"));
               e.deleteNachricht(nID);
               Steuerung.setStatus(Status.NEUE_EMAIL);
               Steuerung.steuerung();
            } catch (SQLException var5) {
               logging.logPrintStackTrace(var5);
            }

         }
      });
      this.buttonWeiterleiten.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            TabelleEMail_empfangen tabEmpf = new TabelleEMail_empfangen();
            TabelleEMail_gesendet tabGes = new TabelleEMail_gesendet();
            EMailModulAO.staticAntworten = new Antworten();

            try {
               int e = Integer.parseInt(EMailModulAO.mailListe.getSelectedValue().toString().substring(11, EMailModulAO.mailListe.getSelectedValue().toString().length()));
               String var5;
               switch((var5 = EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString()).hashCode()) {
               case -1598226551:
                  if(var5.equals("Posteingang Ungelesen")) {
                     EMailModulAO.staticAntworten.setBetreff("Wg: " + tabEmpf.getBetreff(e));
                     EMailModulAO.staticAntworten.setNachricht(tabEmpf.getNachricht(e));
                     EMailModulAO.staticAntworten.setSender("");
                     EMailModulAO.staticAntworten.setDate(tabEmpf.getDate(e));
                  }
                  break;
               case 57663362:
                  if(var5.equals("Posteingang Gelesen")) {
                     EMailModulAO.staticAntworten.setBetreff("Wg: " + tabEmpf.getBetreff(e));
                     EMailModulAO.staticAntworten.setNachricht(tabEmpf.getNachricht(e));
                     EMailModulAO.staticAntworten.setSender("");
                     EMailModulAO.staticAntworten.setDate(tabEmpf.getDate(e));
                  }
                  break;
               case 1079996702:
                  if(var5.equals("Gesendete Objekte")) {
                     EMailModulAO.staticAntworten.setBetreff("Wg: " + tabGes.getBetreff(e));
                     EMailModulAO.staticAntworten.setNachricht(tabGes.getNachricht(e));
                     EMailModulAO.staticAntworten.setSender("");
                     EMailModulAO.staticAntworten.setDate(TimeCalculation.parseDateForGUI(tabGes.getDate(e)));
                  }
               }

               Steuerung.setStatus(Status.NEUE_EMAIL);
               Steuerung.steuerung();
            } catch (SQLException var6) {
               logging.logPrintStackTrace(var6);
            }

         }
      });
      this.buttonEmpfangen.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            ProzessBarAO.progressbar.setStringPainted(false);
            ProzessBarAO.progressbar.setIndeterminate(true);
            ProzessBarAO.label_bitteWarten.setText("E-Mails werden vom Server abgerufen... Bitte warten...");
            Thread threadEmpf = new Thread() {
               public void run() {
                  try {
                     EmpfangenOpperation.empfangen();
                     EMail_utils.refreshMailsErhalten(0);
                     MyEvent.setEvent("0x0030");
                  } catch (Exception var2) {
                     MyEvent.setEvent("0x0030");
                     JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_EMAIL_EMPFANGEN, "Fehlermeldung", 0);
                     logging.logPrintStackTrace(var2);
                  }

               }
            };
            threadEmpf.start();
         }
      });
      this.buttonSignatur.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.EMAIL_SIGNATUR);
            Steuerung.steuerung();
         }
      });
      this.buttonDelete.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            DeleteAndDisplayEMail disp = new DeleteAndDisplayEMail();
            disp.delete();
         }
      });
      tree.addTreeSelectionListener(new TreeSelectionListener() {
         public void valueChanged(TreeSelectionEvent arg0) {
            String var2;
            switch((var2 = EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString()).hashCode()) {
            case -1598226551:
               if(var2.equals("Posteingang Ungelesen")) {
                  EMail_utils.refreshMailsErhalten(0);
                  EMailModulAO.this.buttonBearbeiten.setVisible(false);
               }
               break;
            case -109155694:
               if(var2.equals("Postausgang")) {
                  EMail_utils.refreshMailsPostausgang();
                  EMailModulAO.this.buttonBearbeiten.setVisible(false);
               }
               break;
            case 57663362:
               if(var2.equals("Posteingang Gelesen")) {
                  EMail_utils.refreshMailsErhalten(1);
                  EMailModulAO.this.buttonBearbeiten.setVisible(false);
               }
               break;
            case 73240797:
               if(var2.equals("Entwurf")) {
                  EMail_utils.refreshMailsEntwurf();
                  EMailModulAO.this.buttonBearbeiten.setVisible(true);
               }
               break;
            case 1079996702:
               if(var2.equals("Gesendete Objekte")) {
                  EMail_utils.refreshMailsGesendet();
                  EMailModulAO.this.buttonBearbeiten.setVisible(false);
               }
            }

            EMailModulAO.textfeld_email.setText((String)null);
         }
      });
   }

   protected void actionErzeugen() {
      mailListe.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent arg0) {
            DeleteAndDisplayEMail mail = new DeleteAndDisplayEMail();
            mail.display();
         }
      });
      this.buttonNeueEMail.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent arg0) {
            Steuerung.setStatus(Status.NEUE_EMAIL);
            Steuerung.steuerung();
         }
      });
   }

   public void fensterAnzeigen() {
      if(((String)runApplication.EINSTELLUNGEN.get("AlwaysOnTop")).equals("1")) {
         this.setAlwaysOnTop(true);
      }

      this.setVisible(true);
      EMail_utils.refreshMailsErhalten(0);
      this.setLocationRelativeTo((Component)null);
      this.setResizable(false);
   }
}
