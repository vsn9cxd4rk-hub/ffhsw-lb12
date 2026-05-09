/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.Datei
 *  utilities.SbcUtils
 */
package utilities_email;

import ao.email.EMailModulAO;
import data.tabellen.email.TabelleEMail_ausgang;
import data.tabellen.email.TabelleEMail_empfangen;
import data.tabellen.email.TabelleEMail_entwurf;
import data.tabellen.email.TabelleEMail_gesendet;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.Datei;
import utilities.SbcUtils;
import utilities.Utils;

public class EMail_utils {
    public static void saveFile(File anhang, int newMailID, String speicherOrt) throws IOException {
        try {
            Utils.ordnerErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/anhang/" + speicherOrt + "/" + newMailID, runApplication.clientID);
            Datei.copyFileAusf\u00fchren((File)anhang, (String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/Email/anhang/" + speicherOrt + "/" + newMailID + "/" + anhang.getName()));
            logging.logInfo((Object)("Speichere Anhnag: " + runApplication.arbeitsverzeichnis + "data/Email/anhang/" + speicherOrt + "/" + newMailID + "/" + anhang.getName()));
            Utils.dateiKatalogisieren(String.valueOf(runApplication.arbeitsverzeichnis) + "data/EMail/anhang/" + speicherOrt + "/" + newMailID + "/" + anhang.getName());
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void refreshMailsErhalten(int gelesen) {
        EMailModulAO.mailListe.setListData((Object[])EMail_utils.getMailListe(gelesen));
        EMailModulAO.status.setText(null);
    }

    public static void refreshMailsPostausgang() {
        EMailModulAO.mailListe.setListData((Object[])EMail_utils.getAusgangMailListe());
        EMailModulAO.status.setText(null);
    }

    public static void refreshMailsGesendet() {
        EMailModulAO.mailListe.setListData((Object[])EMail_utils.getSendMailListe());
        EMailModulAO.status.setText(null);
    }

    public static void refreshMailsEntwurf() {
        EMailModulAO.mailListe.setListData((Object[])EMail_utils.getEntwurfMailListe());
        EMailModulAO.status.setText(null);
    }

    public static String[] getMailListe(int gelesen_status) {
        TabelleEMail_empfangen emp = new TabelleEMail_empfangen();
        try {
            String[] result = SbcUtils.listToArray(emp.getAllMails(gelesen_status));
            logging.logInfo((Object)("Sende Liste zum Client: " + result));
            return result;
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
            return null;
        }
    }

    private static String[] getSendMailListe() {
        TabelleEMail_gesendet send = new TabelleEMail_gesendet();
        try {
            String[] result = SbcUtils.listToArray(send.getAllSendMails());
            logging.logInfo((Object)"Rufe Liste ab: Gesendete E-Mails");
            return result;
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
            return null;
        }
    }

    private static String[] getAusgangMailListe() {
        TabelleEMail_ausgang send = new TabelleEMail_ausgang();
        try {
            String[] result = SbcUtils.listToArray(send.getAllSendMails());
            logging.logInfo((Object)"Rufe Liste ab: Ausgang E-Mails");
            return result;
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
            return null;
        }
    }

    private static String[] getEntwurfMailListe() {
        TabelleEMail_entwurf send = new TabelleEMail_entwurf();
        try {
            String[] result = SbcUtils.listToArray(send.getAllEntwurfMails());
            logging.logInfo((Object)"Rufe Liste ab: Entwurf E-Mails");
            return result;
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
            return null;
        }
    }
}

