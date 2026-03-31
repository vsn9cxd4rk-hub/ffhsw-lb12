package utilities;

import data.tabellen.TabelleLogbuch;
import java.sql.SQLException;
import java.util.Date;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;

public class logbuchEingabe {

   public static void NeuerEintag(String aktion) {
      Date systemzeit = new Date();
      String zeitstempel = systemzeit.getHours() + ":" + systemzeit.getMinutes() + ":" + systemzeit.getSeconds();
      TabelleLogbuch logbuch = new TabelleLogbuch();

      try {
         if(logbuch.getCount() == Integer.parseInt((String)runApplication.PROPERTIES.get("logmax"))) {
            logbuch.delete();
            logbuch.insert(logbuch.getNextNummer(), SbcUtils.timeStamp("yyyy-MM-dd"), zeitstempel, runApplication.loginName, aktion);
         } else {
            try {
               logbuch.insert(logbuch.getNextNummer(), SbcUtils.timeStamp("yyyy-MM-dd"), zeitstempel, runApplication.loginName, aktion);
            } catch (NullPointerException var5) {
               logbuch.insert(logbuch.getNextNummer(), SbcUtils.timeStamp("yyyy-MM-dd"), zeitstempel, "GRUNDINSTALLATION", aktion);
            }
         }
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

   }
}
