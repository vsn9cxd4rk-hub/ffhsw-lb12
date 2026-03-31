package utilities_email;

import ao.email.EMailModulAO;
import data.tabellen.email.TabelleEMail_ausgang;
import data.tabellen.email.TabelleEMail_empfangen;
import data.tabellen.email.TabelleEMail_entwurf;
import data.tabellen.email.TabelleEMail_gesendet;
import java.io.File;
import logging.logging;
import run.runApplication;
import utilities.Datei;
import utilities.Utils;

public class DeleteAndDisplayEMail {

   public void display() {
      try {
         TabelleEMail_gesendet e = new TabelleEMail_gesendet();
         TabelleEMail_entwurf tabEnt = new TabelleEMail_entwurf();
         StringBuilder build = new StringBuilder();
         build.setLength(0);
         String var4;
         switch((var4 = EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString()).hashCode()) {
         case -1598226551:
            if(var4.equals("Posteingang Ungelesen")) {
               this.formatMailContend();
               this.getAttachmentFromServer("empfangende", createNachrichtenID());
            }
            break;
         case 57663362:
            if(var4.equals("Posteingang Gelesen")) {
               this.formatMailContend();
               this.getAttachmentFromServer("empfangende", createNachrichtenID());
            }
            break;
         case 73240797:
            if(var4.equals("Entwurf")) {
               EMailModulAO.textfeld_email.setText(tabEnt.getNachricht(createNachrichtenID()));
               this.getAttachmentFromServer("entwurf", createNachrichtenID());
            }
            break;
         case 1079996702:
            if(var4.equals("Gesendete Objekte")) {
               build.append(e.getBetreff(createNachrichtenID()));
               build.append("\n");
               build.append("---------------------------------------------------------------------------------------------------------------------------------------------");
               build.append("\n");
               build.append("An: " + e.getSender(createNachrichtenID()));
               build.append("\n");
               build.append("CC: " + e.getCC(createNachrichtenID()));
               build.append("\n");
               build.append("BCC: " + e.getBCC(createNachrichtenID()));
               build.append("\n");
               build.append("---------------------------------------------------------------------------------------------------------------------------------------------");
               build.append("\n\n");
               build.append(e.getNachricht(createNachrichtenID()));
               EMailModulAO.textfeld_email.setText(build.toString());
               this.getAttachmentFromServer("gesendet", createNachrichtenID());
            }
         }

         EMailModulAO.textfeld_email.setCaretPosition(0);
      } catch (Exception var5) {
         logging.logError(var5);
      }

   }

   void formatMailContend() throws Exception {
      TabelleEMail_empfangen tabEmpf = new TabelleEMail_empfangen();
      String mailText = tabEmpf.getNachricht(createNachrichtenID());
      logging.logInfo(mailText);
      if(mailText.equals("WeHaveAnHTMLFileForClient")) {
         logging.logInfo("Receive File from Server --> Client");
         EMailModulAO.textfeld_email.setPage((new File(runApplication.arbeitsverzeichnis + "data/EMail/temp/email" + createNachrichtenID() + ".html")).getAbsoluteFile().toURI().toURL());
         tabEmpf.updateReadStatus(createNachrichtenID());
      } else {
         EMailModulAO.textfeld_email.setText(mailText);
         tabEmpf.updateReadStatus(createNachrichtenID());
      }

   }

   private void getAttachmentFromServer(String ordner, int id) throws Exception {
      if(this.AttachmentAvailable(ordner, id)) {
         EMailModulAO.buttonAttachment.setVisible(true);
      } else {
         EMailModulAO.buttonAttachment.setVisible(false);
      }

   }

   public boolean AttachmentAvailable(String ordner, int id) throws Exception {
      File attOrdner = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/" + ordner + "/" + id);
      return attOrdner.exists();
   }

   public static int createNachrichtenID() {
      boolean error = false;
      int ergebnis = 0;

      do {
         String selectedListItem = (String)EMailModulAO.mailListe.getSelectedValue();
         error = false;

         try {
            ergebnis = Integer.parseInt(selectedListItem.substring(11, selectedListItem.length()));
            error = false;
         } catch (NumberFormatException var5) {
            int aktuellePosition = EMailModulAO.mailListe.getSelectedIndex();
            EMailModulAO.mailListe.setSelectedIndex(aktuellePosition - 1);
            error = true;
         }
      } while(error);

      return ergebnis;
   }

   public void delete() {
      try {
         String e1;
         switch((e1 = EMailModulAO.tree.getSelectionPath().getLastPathComponent().toString()).hashCode()) {
         case -1598226551:
            if(e1.equals("Posteingang Ungelesen")) {
               File folderE_ungelesen = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Empfangende/" + createNachrichtenID());
               if(folderE_ungelesen.exists()) {
                  Datei.delFolder(folderE_ungelesen);
               }

               (new TabelleEMail_empfangen()).deleteNachricht(createNachrichtenID());
               EMailModulAO.mailListe.setListData(Utils.listToArray((new TabelleEMail_empfangen()).getAllMails(0)));
            }
            break;
         case -109155694:
            if(e1.equals("Postausgang")) {
               (new TabelleEMail_ausgang()).deleteNachricht(createNachrichtenID());
               EMailModulAO.mailListe.setListData(Utils.listToArray((new TabelleEMail_ausgang()).getAllSendMails()));
            }
            break;
         case 57663362:
            if(e1.equals("Posteingang Gelesen")) {
               File folderE_gelesen = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Empfangende/" + createNachrichtenID());
               if(folderE_gelesen.exists()) {
                  Datei.delFolder(folderE_gelesen);
               }

               (new TabelleEMail_empfangen()).deleteNachricht(createNachrichtenID());
               EMailModulAO.mailListe.setListData(Utils.listToArray((new TabelleEMail_empfangen()).getAllMails(1)));
            }
            break;
         case 73240797:
            if(e1.equals("Entwurf")) {
               File folderE = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Entwurf/" + createNachrichtenID());
               if(folderE.exists()) {
                  Datei.delFolder(folderE);
               }

               (new TabelleEMail_entwurf()).deleteNachricht(createNachrichtenID());
               EMailModulAO.mailListe.setListData(Utils.listToArray((new TabelleEMail_entwurf()).getAllEntwurfMails()));
            }
            break;
         case 1079996702:
            if(e1.equals("Gesendete Objekte")) {
               File folderG = new File(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/Gesendet/" + createNachrichtenID());
               if(folderG.exists()) {
                  Datei.delFolder(folderG);
               }

               (new TabelleEMail_gesendet()).deleteNachricht(createNachrichtenID());
               EMailModulAO.mailListe.setListData(Utils.listToArray((new TabelleEMail_gesendet()).getAllSendMails()));
            }
         }

         EMailModulAO.mailListe.setSelectedIndex(0);
      } catch (Exception var6) {
         logging.logError(var6);
      }

   }
}
