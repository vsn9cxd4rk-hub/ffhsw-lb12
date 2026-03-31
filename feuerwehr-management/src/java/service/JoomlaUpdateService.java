package service;

import ao.utils.ProzessBarAO;
import data.tabellen.TabellePHP;
import go.PHP_Request;
import java.io.IOException;
import logging.logging;
import run.runApplication;
import utilities.Utils;
import utilities.joomla.Joomla;

public class JoomlaUpdateService {

   public static void uploadService() {
      Thread threadUpload = new Thread() {
         public void run() {
            TabellePHP tabPHP = new TabellePHP();
            runApplication.joomlaUploadLäuft = 1;
            logging.logInfo("Joomla Daten werden syncronisiert");

            try {
               int[] e = Utils.listToIntArray(tabPHP.getIds());
               int aktuellePosition = 1;

               for(int i = 0; i < e.length; ++i) {
                  PHP_Request data = tabPHP.getData(e[i]);
                  if(data.getTyp().toString().equals("REQUEST")) {
                     Joomla.sendRequest(data.getAdresse());
                  } else if(data.getTyp().toString().equals("POST")) {
                     Joomla.sendPostRequest(data.getAdresse(), data.getParameter().split("\n"));
                  }

                  tabPHP.delete(e[i]);
                  ++aktuellePosition;
                  ProzessBarAO.progressbar.setValue(aktuellePosition * 100 / e.length);
               }

               runApplication.joomlaUploadLäuft = 0;
            } catch (IOException var6) {
               runApplication.joomlaUploadLäuft = 0;
               logging.logPrintStackTrace(var6);
            }

         }
      };
      threadUpload.start();
   }
}
