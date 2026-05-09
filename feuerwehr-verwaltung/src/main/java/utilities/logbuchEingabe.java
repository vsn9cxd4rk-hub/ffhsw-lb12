/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
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
        String zeitstempel = String.valueOf(systemzeit.getHours()) + ":" + systemzeit.getMinutes() + ":" + systemzeit.getSeconds();
        TabelleLogbuch logbuch = new TabelleLogbuch();
        try {
            if (logbuch.getCount() == Integer.parseInt(runApplication.PROPERTIES.get("logmax"))) {
                logbuch.delete();
                logbuch.insert(logbuch.getNextNummer(), SbcUtils.timeStamp((String)"yyyy-MM-dd"), zeitstempel, runApplication.loginName, aktion);
            } else {
                try {
                    logbuch.insert(logbuch.getNextNummer(), SbcUtils.timeStamp((String)"yyyy-MM-dd"), zeitstempel, runApplication.loginName, aktion);
                }
                catch (NullPointerException e) {
                    logbuch.insert(logbuch.getNextNummer(), SbcUtils.timeStamp((String)"yyyy-MM-dd"), zeitstempel, "GRUNDINSTALLATION", aktion);
                }
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

