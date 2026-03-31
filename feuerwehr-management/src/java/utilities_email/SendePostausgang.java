package utilities_email;

import data.tabellen.email.TabelleEMail_ausgang;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import service.SystemWarnungService;
import utilities.Utils;
import utilities_email.ErstelleFileArrayForAnhang;
import utilities_email.SendeOpperation;

public class SendePostausgang {

   public static void sendAusgang() {
      Thread threadSendePostausgang = new Thread() {
         public void run() {
            try {
               TabelleEMail_ausgang e = new TabelleEMail_ausgang();
               int[] liste = Utils.listToIntArray(e.getPostausgangNachrichten());

               for(int i = 0; i < liste.length; ++i) {
                  String TO = "";

                  try {
                     logging.logInfo("Sende E-Mail " + liste[i]);
                     TO = e.getEmpfaenger(liste[i]);
                     String e1 = e.getCC(liste[i]);
                     String BCC = e.getBCC(liste[i]);
                     String Betreff = e.getBetreff(liste[i]);
                     String Nachricht = e.getNachricht(liste[i]);
                     File[] Anhang = ErstelleFileArrayForAnhang.analysiereString(e.getAnhang(liste[i]));
                     SendeOpperation.senden(TO, e1, BCC, Betreff, Nachricht, Anhang);
                     logging.logInfo("Senden von E-Mail " + liste[i] + " --> " + TO + " war erfolgreich...");
                     e.deleteNachricht(liste[i]);
                  } catch (UnsupportedEncodingException var14) {
                     SystemWarnungService.insertSystemWarnung("Fehler beim Senden von E-Mails aus dem Posteingang " + TO);
                     logging.logError("Das senden der E-Mail ist Fehlgeschlagen...");
                     logging.logPrintStackTrace(var14);
                  }
               }
            } catch (SQLException var15) {
               SystemWarnungService.insertSystemWarnung("Unerwarteter Fehler beim Senden von E-Mails aus dem Posteingang!");
               logging.logError("Fehler in der verarbeitung des Postausgang");
               logging.logPrintStackTrace(var15);
            } finally {
               runApplication.verarbeitungLäuft = 0;
            }

         }
      };
      runApplication.verarbeitungLäuft = 1;
      threadSendePostausgang.start();
   }
}
