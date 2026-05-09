/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package thread;

import ao.karte.KarteAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import logging.logging;
import run.runApplication;
import steuerung.Status;
import steuerung.Steuerung;

public class StatusCheck {
    public static void StatusCheckEinsatz() {
        File inputdata = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "data/einsatzinfo.txt");
        while (true) {
            try {
                while (true) {
                    if (!inputdata.exists()) {
                        logging.logInfo((Object)"keine Einsatzdaten gefunden...");
                        Thread.sleep(10000L);
                        continue;
                    }
                    BufferedReader in = new BufferedReader(new FileReader(inputdata));
                    StringBuilder string4Info = new StringBuilder();
                    String zeile = null;
                    while ((zeile = in.readLine()) != null) {
                        logging.logInfo((Object)zeile);
                        string4Info.append(zeile);
                        string4Info.append("\n");
                    }
                    in.close();
                    try {
                        KarteAO.alamierungsInfo.setText(string4Info.toString());
                    }
                    catch (NullPointerException e) {
                        Steuerung.setStatus(Status.KARTE);
                        Steuerung.steuerung();
                        Thread.sleep(5000L);
                        KarteAO.alamierungsInfo.setText(string4Info.toString());
                    }
                    String[] zeilenTextArea = KarteAO.alamierungsInfo.getText().split("\n");
                    logging.logInfo((Object)"Setze Stra\u00dfenauswahl");
                    KarteAO.StrasseSuchen.setSelectedItem(zeilenTextArea[1]);
                    Thread.sleep(20000L);
                    StatusCheck.deleteEinsatzInfo(inputdata);
                    logging.logInfo((Object)"Beginne mit der Suche nach neuem Einsatz");
                }
            }
            catch (InterruptedException e) {
                logging.logPrintStackTrace((Exception)e);
                continue;
            }
            catch (IOException e) {
                logging.logPrintStackTrace((Exception)e);
                continue;
            }
        }
    }

    public static void deleteEinsatzInfo(File inputdata) throws InterruptedException {
        inputdata.delete();
        logging.logInfo((Object)"l\u00f6sche letzte Einsatzdatei");
    }
}

