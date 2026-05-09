/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 *  logging.logging
 *  utilities.MyEvent
 */
package listener;

import ao.HauptprogrammAO;
import ao.utils.ProzessBarAO;
import data.tabellen.TabellePHP;
import data.tabellen.einstellungen.TabelleClients;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.einstellungen.TabelleFTPSync;
import data.tabellen.email.TabelleEMail_ausgang;
import java.awt.HeadlessException;
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

public class BeendenHauptprogrammAOListener
extends AbstractActionListener {
    public BeendenHauptprogrammAOListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        Joomla.nutzungFMS("Beenden");
        try {
            int msg;
            TabelleEMail_ausgang ausgang = new TabelleEMail_ausgang();
            if (ausgang.getCount() != 0 && (msg = JOptionPane.showConfirmDialog(null, Konstante.POSTAUSGANG, "Frage", 0)) == 0) {
                logging.logInfo((Object)"Sende E-Mail(s) aus dem Paostausgang...");
                logging.logInfo((Object)"GUI Fenster wurde geschlossen, E-Mail(s) werden gesendet...");
                SendePostausgang.sendAusgang();
                while (runApplication.verarbeitungL\u00e4uft == 1) {
                    logging.logInfo((Object)"Warte auf Senden der E-Mail(s)...");
                    Thread.sleep(1000L);
                }
            }
        }
        catch (HeadlessException | InterruptedException | SQLException e3) {
            logging.logError((Object)("Fehler beim senden von EMails - " + e3));
            logging.logPrintStackTrace((Exception)e3);
        }
        TabelleFTPSync tabSync = new TabelleFTPSync();
        TabellePHP tabPHP = new TabellePHP();
        try {
            if (runApplication.verarbeitungL\u00e4uft == 1) {
                JOptionPane.showMessageDialog(null, Konstante.VERARBEITUNG_L\u00c4UFT, "Warnung", 2);
            } else if (tabSync.getCountOfNotUploaded("SYSTEM") != 0 && runApplication.PROPERTIES.get("FTPUploadActiv").equals("true") || tabSync.getCountOfNotUploaded(runApplication.clientID) != 0 && runApplication.PROPERTIES.get("FTPUploadActiv").equals("true") || tabSync.getCountOfNotDeleted(runApplication.clientID) != 0 && runApplication.PROPERTIES.get("FTPUploadActiv").equals("true") || tabSync.getCountOfNotUploadedDB(runApplication.clientID) != 0 && runApplication.PROPERTIES.get("BlobActiv").equals("true") || tabSync.getCountOfNotDeletedDB(runApplication.clientID) != 0 && runApplication.PROPERTIES.get("BlobActiv").equals("true") || runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung").equals("1") && tabPHP.getCount() != 0) {
                HauptprogrammAO.buttonBeenden.setEnabled(false);
                logging.logInfo((Object)"Hochladen der Daten beginnt...");
                Steuerung.setStatus(Status.PROZESSBAR);
                Steuerung.steuerung();
                if (runApplication.PROPERTIES.get("BlobActiv").equals("true")) {
                    logging.logInfo((Object)"Lade Dateien in DB...");
                    logging.logInfo((Object)("Anzahl nicht hochgeladender Daten: " + tabSync.getCountOfNotUploadedDB(runApplication.clientID)));
                    ProzessBarAO.label_bitteWarten.setText("Speichern der neuen Daten... Bitte warten...");
                    DatabaseFileTransferService.uploadService();
                }
                if (runApplication.PROPERTIES.get("FTPUploadActiv").equals("true")) {
                    logging.logInfo((Object)"Lade Dateien auf den FTP Server...");
                    logging.logInfo((Object)("Anzahl nicht hochgeladender Daten: " + tabSync.getCountOfNotUploaded(runApplication.clientID)));
                    ProzessBarAO.label_bitteWarten.setText("FTP Upload l\u00e4uft... Bitte warten...");
                    FTPFileTransferservice.uploadService();
                }
                if (runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung").equals("1") && tabPHP.getCount() != 0) {
                    logging.logInfo((Object)"Eintr\u00e4ge in der Tabelle PHP wurden gefunden...");
                    ProzessBarAO.label_bitteWarten.setText("Aktualisiere Homepage... Bitte warten...");
                    JoomlaUpdateService.uploadService();
                }
                Thread threadEnde = new Thread(){

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000L);
                            while (true) {
                                if (runApplication.ftpUploadL\u00e4uft == 0 && runApplication.dbUploadL\u00e4uft == 0 && runApplication.joomlaUploadL\u00e4uft == 0) {
                                    BeendenHauptprogrammAOListener.this.executeEnde();
                                    MyEvent.setEvent((String)"0x0030");
                                    this.stop();
                                }
                                Thread.sleep(1000L);
                            }
                        }
                        catch (InterruptedException interruptedException) {
                            return;
                        }
                    }
                };
                threadEnde.start();
            } else {
                logging.logInfo((Object)"Blob und oder FTP ist deaktiv oder keine Daten sind verf\u00fcgbar zum hochladen...");
                this.executeEnde();
            }
        }
        catch (SQLException e2) {
            logging.logPrintStackTrace((Exception)e2);
        }
    }

    private void checkeVerf\u00fcgbareUnwetterwarnungZumSpeichern() {
        if (runApplication.EINSTELLUNGEN.get("unwetterwarnungModulAktiv").equals("1") && runApplication.unwetterwarnungStatus == 1) {
            logging.logInfo((Object)("Speichere Letzte Unwetterwarnung --> " + runApplication.unwetterwarnungDatumBis + " / " + runApplication.unwetterwarnungUhrzeitBis));
            try {
                TabelleEinstellungen_gespeichert tabGespeichert = new TabelleEinstellungen_gespeichert();
                tabGespeichert.update("unwetterwarnungDatumBis", runApplication.unwetterwarnungDatumBis);
                tabGespeichert.update("unwetterwarnungUhrzeitBis", runApplication.unwetterwarnungUhrzeitBis);
                logging.logInfo((Object)"Unwetterwarnung wurde gespeichert...");
            }
            catch (SQLException e) {
                logging.logPrintStackTrace((Exception)e);
            }
        }
    }

    private void executeEnde() {
        if (runApplication.verarbeitungL\u00e4uft == 1) {
            JOptionPane.showMessageDialog(null, Konstante.VERARBEITUNG_L\u00c4UFT, "Warnung", 2);
        } else {
            this.checkeVerf\u00fcgbareUnwetterwarnungZumSpeichern();
            new TabelleClients().updateOnline(0);
            logging.logInfo((Object)"Setze Status ENDE... Bye, Bye");
            Steuerung.setStatus(Status.ENDE);
            this.getFrame().dispose();
            Steuerung.steuerung();
        }
    }
}

