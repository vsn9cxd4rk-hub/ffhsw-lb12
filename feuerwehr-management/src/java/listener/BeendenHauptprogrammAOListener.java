package listener;

import ao.HauptprogrammAO;
import ao.utils.ProzessBarAO;
import data.tabellen.TabellePHP;
import data.tabellen.einstellungen.TabelleClients;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleFTPSync;
import data.tabellen.email.TabelleEMail_ausgang;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import run.runApplication;
import service.DatabaseFileTransferService;
import service.FTPFileTransferservice;
import service.JoomlaUpdateService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.joomla.Joomla;
import utilities_email.SendePostausgang;

public class BeendenHauptprogrammAOListener extends AbstractActionListener {

   public BeendenHauptprogrammAOListener(JFrame frame) {
      super(frame);
   }

   public void actionPerformed(ActionEvent e) {
      Joomla.nutzungFMS("Beenden");

      try {
         TabelleEMail_ausgang tabSync = new TabelleEMail_ausgang();
         if(tabSync.getCount() != 0) {
            int tabPHP = JOptionPane.showConfirmDialog((Component)null, Konstante.POSTAUSGANG, "Frage", 0);
            if(tabPHP == 0) {
               logging.logInfo("Sende E-Mail(s) aus dem Paostausgang...");
               logging.logInfo("GUI Fenster wurde geschlossen, E-Mail(s) werden gesendet...");
               SendePostausgang.sendAusgang();

               while(runApplication.verarbeitungLäuft == 1) {
                  logging.logInfo("Warte auf Senden der E-Mail(s)...");
                  Thread.sleep(1000L);
               }
            }
         }
      } catch (InterruptedException var6) {
         logging.logError("Fehler beim senden von EMails - " + var6);
         logging.logPrintStackTrace(var6);
      }

      TabelleFTPSync tabSync1 = new TabelleFTPSync();
      TabellePHP tabPHP1 = new TabellePHP();

      try {
         if(runApplication.verarbeitungLäuft == 1) {
            JOptionPane.showMessageDialog((Component)null, Konstante.VERARBEITUNG_LÄUFT, "Warnung", 2);
         } else if((tabSync1.getCountOfNotUploaded("SYSTEM") == 0 || !((String)runApplication.PROPERTIES.get("FTPUploadActiv")).equals("true")) && (tabSync1.getCountOfNotUploaded(runApplication.clientID) == 0 || !((String)runApplication.PROPERTIES.get("FTPUploadActiv")).equals("true")) && (tabSync1.getCountOfNotDeleted(runApplication.clientID) == 0 || !((String)runApplication.PROPERTIES.get("FTPUploadActiv")).equals("true")) && (tabSync1.getCountOfNotUploadedDB(runApplication.clientID) == 0 || !((String)runApplication.PROPERTIES.get("BlobActiv")).equals("true")) && (tabSync1.getCountOfNotDeletedDB(runApplication.clientID) == 0 || !((String)runApplication.PROPERTIES.get("BlobActiv")).equals("true")) && (!((String)runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung")).equals("1") || tabPHP1.getCount() == 0)) {
            logging.logInfo("Blob und oder FTP ist deaktiv oder keine Daten sind verfügbar zum hochladen...");
            this.executeEnde();
         } else {
            HauptprogrammAO.buttonBeenden.setEnabled(false);
            logging.logInfo("Hochladen der Daten beginnt...");
            Steuerung.setStatus(Status.PROZESSBAR);
            Steuerung.steuerung();
            if(((String)runApplication.PROPERTIES.get("BlobActiv")).equals("true")) {
               logging.logInfo("Lade Dateien in DB...");
               logging.logInfo("Anzahl nicht hochgeladender Daten: " + tabSync1.getCountOfNotUploadedDB(runApplication.clientID));
               ProzessBarAO.label_bitteWarten.setText("Speichern der neuen Daten... Bitte warten...");
               DatabaseFileTransferService.uploadService();
            }

            if(((String)runApplication.PROPERTIES.get("FTPUploadActiv")).equals("true")) {
               logging.logInfo("Lade Dateien auf den FTP Server...");
               logging.logInfo("Anzahl nicht hochgeladender Daten: " + tabSync1.getCountOfNotUploaded(runApplication.clientID));
               ProzessBarAO.label_bitteWarten.setText("FTP Upload läuft... Bitte warten...");
               FTPFileTransferservice.uploadService();
            }

            if(((String)runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung")).equals("1") && tabPHP1.getCount() != 0) {
               logging.logInfo("Einträge in der Tabelle PHP wurden gefunden...");
               ProzessBarAO.label_bitteWarten.setText("Aktualisiere Homepage... Bitte warten...");
               JoomlaUpdateService.uploadService();
            }

            Thread e2 = new Thread() {
               public void run() {
                  try {
                     Thread.sleep(3000L);

                     while(true) {
                        if(runApplication.ftpUploadLäuft == 0 && runApplication.dbUploadLäuft == 0 && runApplication.joomlaUploadLäuft == 0) {
                           BeendenHauptprogrammAOListener.this.executeEnde();
                           MyEvent.setEvent("0x0030");
                           this.stop();
                        }

                        Thread.sleep(1000L);
                     }
                  } catch (InterruptedException var2) {
                     ;
                  }
               }
            };
            e2.start();
         }
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

   }

   private void checkeVerfügbareUnwetterwarnungZumSpeichern() {
      if(((String)runApplication.EINSTELLUNGEN.get("unwetterwarnungModulAktiv")).equals("1") && runApplication.unwetterwarnungStatus == 1) {
         logging.logInfo("Speichere Letzte Unwetterwarnung --> " + runApplication.unwetterwarnungDatumBis + " / " + runApplication.unwetterwarnungUhrzeitBis);

         try {
            TabelleEinstellungen_gespeichert e = new TabelleEinstellungen_gespeichert();
            e.update("unwetterwarnungDatumBis", runApplication.unwetterwarnungDatumBis);
            e.update("unwetterwarnungUhrzeitBis", runApplication.unwetterwarnungUhrzeitBis);
            logging.logInfo("Unwetterwarnung wurde gespeichert...");
         } catch (SQLException var2) {
            logging.logPrintStackTrace(var2);
         }
      }

   }

   private void executeEnde() {
      if(runApplication.verarbeitungLäuft == 1) {
         JOptionPane.showMessageDialog((Component)null, Konstante.VERARBEITUNG_LÄUFT, "Warnung", 2);
      } else {
         this.checkeVerfügbareUnwetterwarnungZumSpeichern();
         (new TabelleClients()).updateOnline(0);
         logging.logInfo("Setze Status ENDE... Bye, Bye");
         Steuerung.setStatus(Status.ENDE);
         this.getFrame().dispose();
         Steuerung.steuerung();
      }

   }
}
