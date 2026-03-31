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
         Utils.ordnerErstellen(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/" + speicherOrt + "/" + newMailID, runApplication.clientID);
         Datei.copyFileAusführen(anhang, runApplication.arbeitsverzeichnis + "data/EMail/Anhang/" + speicherOrt + "/" + newMailID + "/" + anhang.getName());
         logging.logInfo("Speichere Anhnag: " + runApplication.arbeitsverzeichnis + "data/EMail/Anhang/" + speicherOrt + "/" + newMailID + "/" + anhang.getName());
         Utils.dateiKatalogisieren(runApplication.arbeitsverzeichnis + "data/EMail/Anhang/" + speicherOrt + "/" + newMailID + "/" + anhang.getName());
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }

   public static void refreshMailsErhalten(int gelesen) {
      EMailModulAO.mailListe.setListData(getMailListe(gelesen));
      EMailModulAO.status.setText((String)null);
   }

   public static void refreshMailsPostausgang() {
      EMailModulAO.mailListe.setListData(getAusgangMailListe());
      EMailModulAO.status.setText((String)null);
   }

   public static void refreshMailsGesendet() {
      EMailModulAO.mailListe.setListData(getSendMailListe());
      EMailModulAO.status.setText((String)null);
   }

   public static void refreshMailsEntwurf() {
      EMailModulAO.mailListe.setListData(getEntwurfMailListe());
      EMailModulAO.status.setText((String)null);
   }

   public static String[] getMailListe(int gelesen_status) {
      TabelleEMail_empfangen emp = new TabelleEMail_empfangen();

      try {
         String[] e = SbcUtils.listToArray(emp.getAllMails(gelesen_status));
         logging.logInfo("Sende Liste zum Client: " + e);
         return e;
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
         return null;
      }
   }

   private static String[] getSendMailListe() {
      TabelleEMail_gesendet send = new TabelleEMail_gesendet();

      try {
         String[] e = SbcUtils.listToArray(send.getAllSendMails());
         logging.logInfo("Rufe Liste ab: Gesendete E-Mails");
         return e;
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
         return null;
      }
   }

   private static String[] getAusgangMailListe() {
      TabelleEMail_ausgang send = new TabelleEMail_ausgang();

      try {
         String[] e = SbcUtils.listToArray(send.getAllSendMails());
         logging.logInfo("Rufe Liste ab: Ausgang E-Mails");
         return e;
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
         return null;
      }
   }

   private static String[] getEntwurfMailListe() {
      TabelleEMail_entwurf send = new TabelleEMail_entwurf();

      try {
         String[] e = SbcUtils.listToArray(send.getAllEntwurfMails());
         logging.logInfo("Rufe Liste ab: Entwurf E-Mails");
         return e;
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
         return null;
      }
   }
}
