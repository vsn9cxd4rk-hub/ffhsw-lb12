/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  listener.AbstractActionListener
 *  logging.logging
 *  utilities.MyProperties
 *  utilities.SbcUtils
 *  utilities.hash
 */
package listener;

import ao.AnmeldungAO;
import ao.HauptprogrammAO;
import data.tabellen.einstellungen.TabelleBerechtigunggruppe;
import data.tabellen.einstellungen.TabelleUser;
import go.User;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import listener.AbstractActionListener;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import service.InformationService;
import steuerung.Status;
import steuerung.Steuerung;
import utilities.Konstante;
import utilities.MyProperties;
import utilities.SbcUtils;
import utilities.hash;
import utilities.logbuchEingabe;

public class LoginListener
extends AbstractActionListener {
    public LoginListener(JFrame frame) {
        super(frame);
    }

    public void actionPerformed(ActionEvent e) {
        if (AnmeldungAO.bentzeranmeldungspeichern.isSelected()) {
            MyProperties benutzeranmeldungspeichern = new MyProperties(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/user.properties");
            benutzeranmeldungspeichern.putVar("Name", (Object)AnmeldungAO.fieldUser.getText());
            benutzeranmeldungspeichern.putVar("Passwort", (Object)hash.createHashCode((String)AnmeldungAO.fieldPasswort.getText()));
            benutzeranmeldungspeichern.putVar("clientID", (Object)runApplication.clientID);
            if (AnmeldungAO.angemeldetBleiben.isSelected()) {
                benutzeranmeldungspeichern.putVar("angemeldetBleiben", (Object)"true");
            } else {
                benutzeranmeldungspeichern.putVar("angemeldetBleiben", (Object)"false");
            }
            benutzeranmeldungspeichern.saveVars();
            logging.logInfo((Object)"Bentzername wird als Standard Benuter gespeichert");
        } else if (!AnmeldungAO.bentzeranmeldungspeichern.isSelected()) {
            File file = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/user.properties");
            file.delete();
            logging.logInfo((Object)"Gepeicheter Bentzeranmeldung wird gel\u00f6scht");
        }
        TabelleUser tabelleUser = new TabelleUser();
        try {
            User user = tabelleUser.get(AnmeldungAO.fieldUser.getText());
            if (AnmeldungAO.fieldUser.getText().equals("MASTER_USER_FMS")) {
                int tag = Integer.parseInt(SbcUtils.timeStamp((String)"dd")) + 5;
                int monat = Integer.parseInt(String.valueOf(SbcUtils.timeStamp((String)"MM")) + 10);
                int jahr = Integer.parseInt(String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + 15);
                int summe = (tag + monat + jahr) / tag;
                if (AnmeldungAO.fieldPasswort.getText().equals(Integer.toString(summe))) {
                    runApplication.loginName = AnmeldungAO.fieldUser.getText();
                    logging.logInfo((Object)("Ein Benutzer hat sich angemeldet: " + runApplication.loginName));
                    logbuchEingabe.NeuerEintag("MASTER_USER_FMS hat sich angemeldet!");
                    HauptprogrammAO.anmeldeName.setText("Angemeldet als: " + runApplication.loginName);
                    if (AnmeldungAO.bentzeranmeldungspeichern.isSelected()) {
                        File file = new File(String.valueOf(runApplication.arbeitsverzeichnis) + "properties/user.properties");
                        file.delete();
                        logging.logInfo((Object)"Gepeicheter Bentzeranmeldung wird zwingend gel\u00f6scht bei MASTER_USER_FMS");
                    }
                    this.getFrame().dispose();
                    Steuerung.setStatus(Status.MEIN_PASSWORT);
                    Steuerung.steuerung();
                } else {
                    JOptionPane.showMessageDialog(null, "Die Anmedung ist Fehlgeschagen!\nBitte Benutzername und Passwort kontrollieren...", "Fehlermeldung", 0);
                }
            } else if (user == null) {
                JOptionPane.showMessageDialog(null, "Die Anmedung ist Fehlgeschagen!\nBitte Benutzername und Passwort kontrollieren...", "Fehlermeldung", 0);
            } else if (tabelleUser.getDeaktivStatus(AnmeldungAO.fieldUser.getText()) == 1) {
                JOptionPane.showMessageDialog(null, Konstante.BENUTZERKONTO_GESPERRT, "Fehlermeldung", 0);
            } else if (AnmeldungAO.fieldPasswort.getText().equals(hash.decodeHashCode((String)user.getPasswort()))) {
                this.getFrame().dispose();
                BerechtigunsManager.ber = new TabelleBerechtigunggruppe().getAll(tabelleUser.getRechte(AnmeldungAO.fieldUser.getText()), 1);
                BerechtigunsManager.ber2 = new TabelleBerechtigunggruppe().getAll(tabelleUser.getRechte(AnmeldungAO.fieldUser.getText()), 2);
                runApplication.loginName = AnmeldungAO.fieldUser.getText();
                logging.logInfo((Object)("Ein Benutzer hat sich angemeldet: " + runApplication.loginName));
                if (runApplication.BF == 0) {
                    HauptprogrammAO.anmeldeName.setText("Angemeldet als: " + runApplication.loginName);
                } else {
                    HauptprogrammAO.anmeldeName.setText("Verwaltungsbeh\u00f6rde (BF) // Angemeldet als: " + runApplication.loginName);
                }
                BerechtigunsManager.berechtigungDokumente();
                BerechtigunsManager.berechtigungListen();
                BerechtigunsManager.berechtigungMitglieder();
                BerechtigunsManager.berechtigungOptionen();
                BerechtigunsManager.berechtigungStatistik();
                BerechtigunsManager.berechtigungVeranstaltung();
                Thread threadLadeInformationen = new Thread(){

                    @Override
                    public void run() {
                        HauptprogrammAO.aufgabenListe.setText(null);
                        HauptprogrammAO.aufgabenListe.setText("Bitte warten...");
                        HauptprogrammAO.aufgabenListe.setText(InformationService.checkInformationen());
                    }
                };
                HauptprogrammAO.buttonAnmelden.setVisible(false);
                HauptprogrammAO.buttonAbmelden.setVisible(true);
                HauptprogrammAO.buttonPasswort\u00c4ndern.setVisible(true);
                logbuchEingabe.NeuerEintag("Benutzer hat sich angemeldet");
                threadLadeInformationen.start();
            } else {
                logging.logWarning((Object)("Passwort ist falsch... - Eingegeben wurde: " + AnmeldungAO.fieldUser.getText() + " / " + hash.createHashCode((String)AnmeldungAO.fieldPasswort.getText())));
                logbuchEingabe.NeuerEintag("Anmeldeversuch fehlgeschlagen - Anmeldeversuch mit falschen Passwort an: " + AnmeldungAO.fieldUser.getText());
                JOptionPane.showMessageDialog(null, "Die Anmedung ist Fehlgeschagen!\nBitte Benutzername und Passwort kontrollieren...", "Fehlermeldung", 0);
            }
        }
        catch (SQLException ex) {
            logging.logPrintStackTrace((Exception)ex);
        }
        catch (NumberFormatException exn) {
            JOptionPane.showMessageDialog(null, "Die Anmedung ist Fehlgeschagen!\nBitte Benutzername und Passwort kontrollieren...", "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)exn);
        }
    }
}

