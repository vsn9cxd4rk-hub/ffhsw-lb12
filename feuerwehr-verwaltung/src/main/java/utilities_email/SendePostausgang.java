/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.mail.MessagingException
 *  logging.logging
 */
package utilities_email;

import data.tabellen.email.TabelleEMail_ausgang;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import javax.mail.MessagingException;
import logging.logging;
import run.runApplication;
import service.SystemWarnungService;
import utilities.Utils;
import utilities_email.ErstelleFileArrayForAnhang;
import utilities_email.SendeOpperation;

public class SendePostausgang {
    public static void sendAusgang() {
        Thread threadSendePostausgang = new Thread(){

            @Override
            public void run() {
                try {
                    try {
                        TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
                        int[] liste = Utils.listToIntArray(tabAusgang.getPostausgangNachrichten());
                        int i = 0;
                        while (i < liste.length) {
                            String TO = "";
                            try {
                                logging.logInfo((Object)("Sende E-Mail " + liste[i]));
                                TO = tabAusgang.getEmpfaenger(liste[i]);
                                String CC = tabAusgang.getCC(liste[i]);
                                String BCC = tabAusgang.getBCC(liste[i]);
                                String Betreff = tabAusgang.getBetreff(liste[i]);
                                String Nachricht = tabAusgang.getNachricht(liste[i]);
                                File[] Anhang = ErstelleFileArrayForAnhang.analysiereString(tabAusgang.getAnhang(liste[i]));
                                SendeOpperation.senden(TO, CC, BCC, Betreff, Nachricht, Anhang);
                                logging.logInfo((Object)("Senden von E-Mail " + liste[i] + " --> " + TO + " war erfolgreich..."));
                                tabAusgang.deleteNachricht(liste[i]);
                            }
                            catch (UnsupportedEncodingException | MessagingException e1) {
                                SystemWarnungService.insertSystemWarnung("Fehler beim Senden von E-Mails aus dem Posteingang " + TO);
                                logging.logError((Object)"Das senden der E-Mail ist Fehlgeschlagen...");
                                logging.logPrintStackTrace((Exception)e1);
                            }
                            ++i;
                        }
                    }
                    catch (SQLException e) {
                        SystemWarnungService.insertSystemWarnung("Unerwarteter Fehler beim Senden von E-Mails aus dem Posteingang!");
                        logging.logError((Object)"Fehler in der verarbeitung des Postausgang");
                        logging.logPrintStackTrace((Exception)e);
                        runApplication.verarbeitungL\u00e4uft = 0;
                    }
                }
                finally {
                    runApplication.verarbeitungL\u00e4uft = 0;
                }
            }
        };
        runApplication.verarbeitungL\u00e4uft = 1;
        threadSendePostausgang.start();
    }
}

