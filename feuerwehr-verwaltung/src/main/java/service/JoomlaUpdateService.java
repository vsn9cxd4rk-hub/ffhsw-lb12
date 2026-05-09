/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package service;

import ao.utils.ProzessBarAO;
import data.tabellen.TabellePHP;
import go.PHP_Request;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.Utils;
import utilities.joomla.Joomla;

public class JoomlaUpdateService {
    public static void uploadService() {
        Thread threadUpload = new Thread(){

            @Override
            public void run() {
                TabellePHP tabPHP = new TabellePHP();
                runApplication.joomlaUploadL\u00e4uft = 1;
                logging.logInfo((Object)"Joomla Daten werden syncronisiert");
                try {
                    int[] IDs = Utils.listToIntArray(tabPHP.getIds());
                    int aktuellePosition = 1;
                    int i = 0;
                    while (i < IDs.length) {
                        PHP_Request data = tabPHP.getData(IDs[i]);
                        if (data.getTyp().toString().equals("REQUEST")) {
                            Joomla.sendRequest(data.getAdresse());
                        } else if (data.getTyp().toString().equals("POST")) {
                            Joomla.sendPostRequest(data.getAdresse(), data.getParameter().split("\n"));
                        }
                        tabPHP.delete(IDs[i]);
                        ProzessBarAO.progressbar.setValue(++aktuellePosition * 100 / IDs.length);
                        ++i;
                    }
                    runApplication.joomlaUploadL\u00e4uft = 0;
                }
                catch (IOException | SQLException e) {
                    runApplication.joomlaUploadL\u00e4uft = 0;
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadUpload.start();
    }
}

