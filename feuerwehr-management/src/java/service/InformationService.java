package service;

import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeug_untersuchung;
import data.tabellen.fahrzeug.TabelleGeraetepruefung;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import go.Geraetepruefung;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import service.BerechtigunsManager;
import service.EMailService;
import service.SystemWarnungService;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities_email.SendePostausgang;

public class InformationService {

   private static int lengthOfSystemWarning = 0;


   public static String checkInformationen() {
      runApplication.infoServiceLäuft = 1;
      StringBuilder buildAufgaben = new StringBuilder();

      try {
         buildAufgaben.setLength(0);
         buildAufgaben.append(systemWarnung());
         if(((String)runApplication.EINSTELLUNGEN.get("termineAnzeigen")).equals("1") && BerechtigunsManager.ber2[0] == 1) {
            logging.logInfo("InformationService: Termin Anzeigen");
            buildAufgaben.append(anstehendeTermine());
         }

         EMailService.EMailVersandtServiceAnstehendeVeranstaltungen();
         if(((String)runApplication.EINSTELLUNGEN.get("gebAnzeigen")).equals("1") && BerechtigunsManager.ber2[1] == 1) {
            logging.logInfo("InformationService: Geb. Anzeigen");
            buildAufgaben.append(gebService());
         }

         if(BerechtigunsManager.ber2[2] == 1) {
            logging.logInfo("InformationService: G26 Anzeigen");
            buildAufgaben.append(untersuchungServiceG26());
         }

         if(BerechtigunsManager.ber2[3] == 1) {
            logging.logInfo("InformationService: G25 Anzeigen");
            buildAufgaben.append(untersuchungServiceG25());
         }

         if(BerechtigunsManager.ber2[4] == 1) {
            logging.logInfo("InformationService: G30 Anzeigen");
            buildAufgaben.append(untersuchungServiceG30());
         }

         if(((String)runApplication.EINSTELLUNGEN.get("ablaufLKWAnzeigen")).equals("1") && BerechtigunsManager.ber2[5] == 1) {
            logging.logInfo("InformationService: Ablauf LKW Anzeigen");
            buildAufgaben.append(untersuchungServiceAblaufLKW());
         }

         if(((String)runApplication.EINSTELLUNGEN.get("agtTrainingAnzeigen")).equals("1") && BerechtigunsManager.ber2[6] == 1) {
            logging.logInfo("InformationService: AGT Training Anzeigen");
            buildAufgaben.append(untersuchungServiceAgtTraining());
            logging.logInfo("InformationService: AGT Einsatztraining Anzeigen");
            buildAufgaben.append(untersuchungServiceAgtEinsatzTraining());
         }

         if(((String)runApplication.EINSTELLUNGEN.get("ablaufDienstausweisAnzeigen")).equals("1") && BerechtigunsManager.ber2[7] == 1) {
            logging.logInfo("InformationService: Ablauf Dienstausweis Anzeigen");
            buildAufgaben.append(untersuchungServiceAblaufDienstausweis());
         }

         if(((String)runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungAnzeigen")).equals("1") && BerechtigunsManager.ber2[8] == 1) {
            logging.logInfo("InformationService: Ablauf Fahrberechtigung Anzeigen");
            buildAufgaben.append(untersuchungServiceAblaufFahrberechtigung());
         }

         if(BerechtigunsManager.ber2[9] == 1) {
            logging.logInfo("InformationService: Abgelaufene G26 Anzeigen");
            buildAufgaben.append(abgelaufeneUntersuchung("G26"));
         }

         if(BerechtigunsManager.ber2[10] == 1) {
            logging.logInfo("InformationService: Abgelaufene G25 Anzeigen");
            buildAufgaben.append(abgelaufeneUntersuchung("G25"));
         }

         if(BerechtigunsManager.ber2[11] == 1) {
            logging.logInfo("InformationService: Abgelaufene G30 Anzeigen");
            buildAufgaben.append(abgelaufeneUntersuchung("G30"));
         }

         if(BerechtigunsManager.ber2[12] == 1) {
            logging.logInfo("InformationService: Abgelaufene AGT Training Anzeigen");
            buildAufgaben.append(abgelaufeneUntersuchung("agttraining"));
            logging.logInfo("InformationService: Abgelaufene AGT Einsatz Training Anzeigen");
            buildAufgaben.append(abgelaufeneUntersuchung("agteinsatztraining"));
         }

         if(((String)runApplication.EINSTELLUNGEN.get("ablaufLKWAnzeigen")).equals("1") && BerechtigunsManager.ber2[13] == 1) {
            logging.logInfo("InformationService: Abgelaufende LKW Führerscheine");
            buildAufgaben.append(abgelaufeneUntersuchung("ablaufLKW"));
         }

         if(((String)runApplication.EINSTELLUNGEN.get("ablaufDienstausweisAnzeigen")).equals("1") && BerechtigunsManager.ber2[14] == 1) {
            logging.logInfo("InformationService: Abgelaufende Dienstausweise");
            buildAufgaben.append(abgelaufeneUntersuchung("ablaufDienstausweis"));
         }

         if(((String)runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungAnzeigen")).equals("1") && BerechtigunsManager.ber2[15] == 1) {
            logging.logInfo("InformationService: Abgelaufene Fahrberechtigungen Anzeigen");
            buildAufgaben.append(abgelaufeneUntersuchung("pruefungDerFahrberechtigung"));
         }

         if(BerechtigunsManager.ber2[16] == 1) {
            logging.logInfo("InformationService: TÜV Anzeigen");
            buildAufgaben.append(untersuchungServiceTÜV());
         }

         if(BerechtigunsManager.ber2[17] == 1) {
            logging.logInfo("InformationService: SP Anzeigen");
            buildAufgaben.append(untersuchungServiceSP());
         }

         if(BerechtigunsManager.ber2[18] == 1) {
            logging.logInfo("InformationService: Service Anzeigen");
            buildAufgaben.append(untersuchungServiceService());
         }

         if(BerechtigunsManager.ber2[19] == 1) {
            logging.logInfo("InformationService: Gaswartung Anzeigen");
            buildAufgaben.append(untersuchungServiceGaswartung());
         }

         if(BerechtigunsManager.ber2[20] == 1) {
            logging.logInfo("InformationService: Abgelaufener TÜV Anzeigen");
            buildAufgaben.append(abgelaufeneFahrzeugUntersuchung("TUEV"));
         }

         if(BerechtigunsManager.ber2[21] == 1) {
            logging.logInfo("InformationService: Abgelaufene SP Anzeigen");
            buildAufgaben.append(abgelaufeneFahrzeugUntersuchung("SP"));
         }

         if(BerechtigunsManager.ber2[22] == 1) {
            logging.logInfo("InformationService: Abgelaufener Serive Anzeigen");
            buildAufgaben.append(abgelaufeneFahrzeugUntersuchung("Service"));
         }

         if(BerechtigunsManager.ber2[23] == 1) {
            logging.logInfo("InformationService: Abgelaufene Gaswartung Anzeigen");
            buildAufgaben.append(abgelaufeneFahrzeugUntersuchung("Gaswartung"));
         }

         if(BerechtigunsManager.ber2[24] == 1) {
            logging.logInfo("InformationService: Geräteprüfung Anzeigen");
            buildAufgaben.append(geraetepruefungService());
         }

         if(((String)runApplication.EINSTELLUNGEN.get("offeneMaengelAnzeigen")).equals("1") && BerechtigunsManager.ber2[25] == 1) {
            logging.logInfo("InformationService: offene Mängel Anzeigen");
            buildAufgaben.append(offeneManegelmeldungen());
         }

         if(buildAufgaben.length() == 0) {
            buildAufgaben.append("Keine aktuellen Informationen verfügbar");
            logging.logInfo("InformationService: Keine Information verfügbar!");
         }

         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && (Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 || Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("ablaufLKWFührerscheinViaEMail")) == 1 || Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("geraetepruefungViaEMail")) == 1 || Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1 || Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("ablaufDienstausweisViaEMail")) == 1 || Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungViaEMail")) == 1 || Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMail")) == 1)) {
            logging.logInfo("InformationService: Sende Paostausgang");
            SendePostausgang.sendAusgang();
         }

         logging.logInfo("InformationService: Setze Status auf 0!");
         runApplication.infoServiceLäuft = 0;
         logging.logInfo("InformationService: FERTIG...");
         return buildAufgaben.toString();
      } catch (Exception var2) {
         logging.logError("InformationService: Setze Status auf 0!");
         runApplication.infoServiceLäuft = 0;
         logging.logError("InformationService: FEHLER...");
         logging.logPrintStackTrace(var2);
         return "Fehler in der Darstellung!";
      }
   }

   private static String systemWarnung() throws SQLException {
      StringBuilder buildSystemWarnung = new StringBuilder();
      String einleitung = "Systemwarnung:\n----------------------------------------\n";
      buildSystemWarnung.append(einleitung);
      buildSystemWarnung.append(SystemWarnungService.checkSystem());
      if(buildSystemWarnung.toString().equals(einleitung)) {
         return "";
      } else {
         buildSystemWarnung.append("\n");
         buildSystemWarnung.append("\n");
         logging.logInfo("Länge der Systemwarnung: " + buildSystemWarnung.toString().length());
         lengthOfSystemWarning = buildSystemWarnung.toString().length();
         return buildSystemWarnung.toString();
      }
   }

   private static String gebService() throws SQLException {
      StringBuilder buildAufgabenGeb = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      buildAufgabenGeb.setLength(0);
      String[] gebListe = null;
      String einleitung = null;
      if(((String)runApplication.EINSTELLUNGEN.get("gebAnzeigeModus")).equals("1")) {
         einleitung = "Geburtstag (Heute):\n----------------------------------------\n";
         gebListe = Utils.listToArray(tabMitglieder.getGebDatumForInformationService(1));
      } else if(((String)runApplication.EINSTELLUNGEN.get("gebAnzeigeModus")).equals("2")) {
         einleitung = "Geburtstage (Monat " + SbcUtils.timeStamp("MMMM") + "):" + "\n" + "----------------------------------------" + "\n";
         gebListe = Utils.listToArray(tabMitglieder.getGebDatumForInformationService(2));
      }

      buildAufgabenGeb.append(einleitung);

      for(int i = 0; i < gebListe.length; ++i) {
         buildAufgabenGeb.append(gebListe[i].substring(0, gebListe[i].length() - 4));
         buildAufgabenGeb.append("\n");
      }

      if(buildAufgabenGeb.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenGeb.append("\n");
         buildAufgabenGeb.append("\n");
         return buildAufgabenGeb.toString();
      }
   }

   private static String untersuchungServiceG26() throws SQLException {
      StringBuilder buildAufgabenUntersuchung = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenUntersuchung.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "G26/3 Untersuchung:\n----------------------------------------\n";
      buildAufgabenUntersuchung.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG26(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenUntersuchung.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert")).equals("0")) {
                  EMailService.EMailInformationServiceG26(mitgliederListe[i], "G26", untersuchung);
               }
            }
         }
      }

      if(buildAufgabenUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchung.append("\n");
         buildAufgabenUntersuchung.append("\n");
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert")).equals("1")) {
            logging.logInfo("Globale E-Mail G26 ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenUntersuchung.toString(), "G26");
         }

         return buildAufgabenUntersuchung.toString();
      }
   }

   private static String untersuchungServiceAblaufLKW() throws SQLException {
      StringBuilder buildAufgabenUntersuchung = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenUntersuchung.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "Ablaufdatum Führerschein Klasse C:\n----------------------------------------\n";
      buildAufgabenUntersuchung.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAblaufLKW(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenUntersuchung.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("ablaufLKWFührerscheinViaEMail")) == 1) {
                  EMailService.EMailInformationServiceAblaufLKW(mitgliederListe[i], untersuchung);
               }
            }
         }
      }

      if(buildAufgabenUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchung.append("\n");
         buildAufgabenUntersuchung.append("\n");
         return buildAufgabenUntersuchung.toString();
      }
   }

   private static String untersuchungServiceAblaufDienstausweis() throws SQLException {
      StringBuilder buildAufgabenDienstausweis = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenDienstausweis.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "Ablaufdatum Dienstausweis:\n----------------------------------------\n";
      buildAufgabenDienstausweis.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("vorwarnungAblaufDienstausweis"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAblaufDienstausweis(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenDienstausweis.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenDienstausweis.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("ablaufDienstausweisViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweisAktiviert")).equals("0")) {
                  EMailService.EMailInformationServiceAblaufDienstausweis(mitgliederListe[i], untersuchung);
               }
            }
         }
      }

      if(buildAufgabenDienstausweis.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenDienstausweis.append("\n");
         buildAufgabenDienstausweis.append("\n");
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweisAktiviert")).equals("1")) {
            logging.logInfo("Globale E-Mail Dienstausweis ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenDienstausweis.toString(), "Dienstausweis");
         }

         return buildAufgabenDienstausweis.toString();
      }
   }

   private static String untersuchungServiceAblaufFahrberechtigung() throws SQLException {
      StringBuilder buildAufgabenFahrberechtigung = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenFahrberechtigung.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "Ablaufende Fahrberechtigungen:\n----------------------------------------\n";
      buildAufgabenFahrberechtigung.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("vorwarnungAblaufFahrberechtigung"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getPruefungDerFahrberechtigung(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenFahrberechtigung.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenFahrberechtigung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigungAktiviert")).equals("0")) {
                  EMailService.EMailInformationServiceAblaufDerFahberechtigung(mitgliederListe[i], untersuchung);
               }
            }
         }
      }

      if(buildAufgabenFahrberechtigung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenFahrberechtigung.append("\n");
         buildAufgabenFahrberechtigung.append("\n");
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigungAktiviert")).equals("1")) {
            logging.logInfo("Globale E-Mail Fahrberechtigung ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenFahrberechtigung.toString(), "Fahrberechtigung");
         }

         return buildAufgabenFahrberechtigung.toString();
      }
   }

   private static String untersuchungServiceG25() throws SQLException {
      StringBuilder buildAufgabenUntersuchung = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenUntersuchung.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "G25 Untersuchung:\n----------------------------------------\n";
      buildAufgabenUntersuchung.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG25(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenUntersuchung.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailG25Aktiviert")).equals("0")) {
                  EMailService.EMailInformationServiceG25(mitgliederListe[i], "G25", untersuchung);
               }
            }
         }
      }

      if(buildAufgabenUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchung.append("\n");
         buildAufgabenUntersuchung.append("\n");
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailG25Aktiviert")).equals("1")) {
            logging.logInfo("Globale E-Mail G25 ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenUntersuchung.toString(), "G25");
         }

         return buildAufgabenUntersuchung.toString();
      }
   }

   private static String untersuchungServiceG30() throws SQLException {
      StringBuilder buildAufgabenUntersuchung = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenUntersuchung.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "G30 Untersuchung:\n----------------------------------------\n";
      buildAufgabenUntersuchung.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG30(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenUntersuchung.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert")).equals("0")) {
                  EMailService.EMailInformationServiceG30(mitgliederListe[i], "G30", untersuchung);
               }
            }
         }
      }

      if(buildAufgabenUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchung.append("\n");
         buildAufgabenUntersuchung.append("\n");
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert")).equals("1")) {
            logging.logInfo("Globale E-Mail G30 ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenUntersuchung.toString(), "G30");
         }

         return buildAufgabenUntersuchung.toString();
      }
   }

   private static String untersuchungServiceAgtTraining() throws SQLException {
      StringBuilder buildAufgabenUntersuchung = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenUntersuchung.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "nächste AGT-Belastungsübung:\n----------------------------------------\n";
      buildAufgabenUntersuchung.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAgtTraining(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenUntersuchung.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenUntersuchung.append("\n");
            }
         }
      }

      if(buildAufgabenUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchung.append("\n");
         buildAufgabenUntersuchung.append("\n");
         return buildAufgabenUntersuchung.toString();
      }
   }

   private static String untersuchungServiceAgtEinsatzTraining() throws SQLException {
      StringBuilder buildAufgabenUntersuchung = new StringBuilder();
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenUntersuchung.setLength(0);
      String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
      int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
      String einleitung = "nächste AGT-Einsatzübung:\n----------------------------------------\n";
      buildAufgabenUntersuchung.append(einleitung);

      for(int i = 0; i < mitgliederListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
         int mID = mitgliederIDListe[i];
         String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAgtEinsatzTraining(mID));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenUntersuchung.append(mitgliederListe[i] + " " + untersuchung);
               buildAufgabenUntersuchung.append("\n");
            }
         }
      }

      if(buildAufgabenUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchung.append("\n");
         buildAufgabenUntersuchung.append("\n");
         return buildAufgabenUntersuchung.toString();
      }
   }

   private static String abgelaufeneUntersuchung(String untersuchungsType) throws SQLException {
      StringBuilder buildAufgabenUntersuchungAbgelaufende = new StringBuilder();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
      buildAufgabenUntersuchungAbgelaufende.setLength(0);
      String[] abgelaufende = Utils.listToArray(tabUntersuchung.getAbgelaufendeUntersuchungen(untersuchungsType));
      String einleitung;
      if(untersuchungsType.equals("ablaufLKW")) {
         einleitung = "Abgelaufene Führerscheine Klasse C:\n----------------------------------------\n";
      } else if(untersuchungsType.equals("ablaufDienstausweis")) {
         einleitung = "Abgelaufene Dienstausweise:\n----------------------------------------\n";
      } else if(untersuchungsType.equals("pruefungDerFahrberechtigung")) {
         einleitung = "Abgelaufene Fahrberechtigung:\n----------------------------------------\n";
      } else if(untersuchungsType.equals("agttraining")) {
         einleitung = "Abgelaufene AGT-Belastungsübung:\n----------------------------------------\n";
      } else if(untersuchungsType.equals("agteinsatztraining")) {
         einleitung = "Abgelaufene AGT-Einsatzübung:\n----------------------------------------\n";
      } else {
         einleitung = "Abgelaufene " + untersuchungsType + " Untersuchung:" + "\n" + "----------------------------------------" + "\n";
      }

      buildAufgabenUntersuchungAbgelaufende.append(einleitung);

      for(int i = 0; i < abgelaufende.length; ++i) {
         if(!abgelaufende[i].equals("")) {
            buildAufgabenUntersuchungAbgelaufende.append(abgelaufende[i]);
            buildAufgabenUntersuchungAbgelaufende.append("\n");
         }
      }

      if(buildAufgabenUntersuchungAbgelaufende.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchungAbgelaufende.append("\n");
         buildAufgabenUntersuchungAbgelaufende.append("\n");
         return buildAufgabenUntersuchungAbgelaufende.toString();
      }
   }

   private static String untersuchungServiceTÜV() throws SQLException {
      StringBuilder buildAufgabenFahrzeugUntersuchung = new StringBuilder();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
      buildAufgabenFahrzeugUntersuchung.setLength(0);
      String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
      int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
      String einleitung = "TÜV / HU:\n----------------------------------------\n";
      buildAufgabenFahrzeugUntersuchung.append(einleitung);

      for(int i = 0; i < fahrzeugListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            int fID = fahrzeugIDListe[i];
            String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getTüv(fID));
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenFahrzeugUntersuchung.append(fahrzeugListe[i] + " " + untersuchung);
               buildAufgabenFahrzeugUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                  EMailService.EMailInformationServiceFahrzeugTuev(fID, fahrzeugListe[i], untersuchung);
               }
            }
         }
      }

      if(buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenFahrzeugUntersuchung.append("\n");
         buildAufgabenFahrzeugUntersuchung.append("\n");
         return buildAufgabenFahrzeugUntersuchung.toString();
      }
   }

   private static String untersuchungServiceSP() throws SQLException {
      StringBuilder buildAufgabenFahrzeugUntersuchung = new StringBuilder();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
      buildAufgabenFahrzeugUntersuchung.setLength(0);
      String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
      int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
      String einleitung = "Sicherheitsprüfung LKW:\n----------------------------------------\n";
      buildAufgabenFahrzeugUntersuchung.append(einleitung);

      for(int i = 0; i < fahrzeugListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            int fID = fahrzeugIDListe[i];
            String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getSP(fID));
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenFahrzeugUntersuchung.append(fahrzeugListe[i] + " " + untersuchung);
               buildAufgabenFahrzeugUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                  EMailService.EMailInformationServiceFahrzeugSP(fID, fahrzeugListe[i], untersuchung);
               }
            }
         }
      }

      if(buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenFahrzeugUntersuchung.append("\n");
         buildAufgabenFahrzeugUntersuchung.append("\n");
         return buildAufgabenFahrzeugUntersuchung.toString();
      }
   }

   private static String untersuchungServiceService() throws SQLException {
      StringBuilder buildAufgabenFahrzeugUntersuchung = new StringBuilder();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
      buildAufgabenFahrzeugUntersuchung.setLength(0);
      String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
      int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
      String einleitung = "Wartung / Service:\n----------------------------------------\n";
      buildAufgabenFahrzeugUntersuchung.append(einleitung);

      for(int i = 0; i < fahrzeugListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            int fID = fahrzeugIDListe[i];
            String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getService(fID));
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenFahrzeugUntersuchung.append(fahrzeugListe[i] + " " + untersuchung);
               buildAufgabenFahrzeugUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                  EMailService.EMailInformationServiceFahrzeugService(fID, fahrzeugListe[i], untersuchung);
               }
            }
         }
      }

      if(buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenFahrzeugUntersuchung.append("\n");
         buildAufgabenFahrzeugUntersuchung.append("\n");
         return buildAufgabenFahrzeugUntersuchung.toString();
      }
   }

   private static String untersuchungServiceGaswartung() throws SQLException {
      StringBuilder buildAufgabenFahrzeugUntersuchung = new StringBuilder();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
      buildAufgabenFahrzeugUntersuchung.setLength(0);
      String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
      int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
      String einleitung = "Gaswartung Feldküche:\n----------------------------------------\n";
      buildAufgabenFahrzeugUntersuchung.append(einleitung);

      for(int i = 0; i < fahrzeugListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));

         for(int m = warung; m > 0; --m) {
            String datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), m);
            int fID = fahrzeugIDListe[i];
            String untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getGasWartung(fID));
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildAufgabenFahrzeugUntersuchung.append(fahrzeugListe[i] + " " + untersuchung);
               buildAufgabenFahrzeugUntersuchung.append("\n");
               if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                  EMailService.EMailInformationServiceGaswartung(fID, fahrzeugListe[i], untersuchung);
               }
            }
         }
      }

      if(buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenFahrzeugUntersuchung.append("\n");
         buildAufgabenFahrzeugUntersuchung.append("\n");
         return buildAufgabenFahrzeugUntersuchung.toString();
      }
   }

   private static String geraetepruefungService() throws SQLException {
      StringBuilder buildGeraetepruefung = new StringBuilder();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleGeraetepruefung tabGeraete = new TabelleGeraetepruefung();
      buildGeraetepruefung.setLength(0);
      String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
      int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
      String einleitung = "Geräteprüfung für:\n----------------------------------------\n";
      buildGeraetepruefung.append(einleitung);

      for(int i = 0; i < fahrzeugListe.length; ++i) {
         int warung = Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("vorwarnungGeräte"));
         Geraetepruefung geraete = tabGeraete.getData(fahrzeugIDListe[i]);
         String untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getStromerzeuger());

         int strom;
         String datum;
         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Stromerzeuger) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getSteckleiter());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Steckleiter) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getMultileiter());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Multifunktionsleiter) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getSchiebleiter());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Schiebleiter) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getAbstusiset());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Absturzsicherungsset) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getRollgliss());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Rollgliss) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getHydraulik());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Hydrauligaggregat) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getHebekissen());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Hebekissen) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getPumpe());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Pumpe) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getTauchpumpe());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Tauchpumpe) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getTauchpumpe2());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (2. Tauchpumpe) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getKettensaege());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Kettensäge) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getKettensaege2());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (2. Kettensäge) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getSchnittschutzkleidung());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Schnittschutzkleidung) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getSchnittschutzkleidung2());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (2. Schnittschutzkleidung) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getDoppelkanister());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Kettensäge - Doppelkanister) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getTrennschleifer());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Trennschleifer) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getElektrosaege());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Elektrosäge) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getLüfter());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Überdrucklüfter) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getLüfter2());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (2. Überdrucklüfter) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getGeraetepruefung_allgm());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (Geräteprüfung allgem.) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getPa1());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (PA1) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getPa2());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (PA2) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getPa3());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (PA3) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getPa4());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (PA4) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getPa5());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (PA5) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }

         untersuchung = TimeCalculation.parseShortDateForGUI(geraete.getPa6());

         for(strom = warung; strom > 0; --strom) {
            datum = calculateWarningTime(SbcUtils.timeStamp("MM.yyyy"), strom);
            if(datum.equals(calculateWarningTime(untersuchung, warung))) {
               buildGeraetepruefung.append(fahrzeugListe[i] + " (PA6) " + untersuchung);
               buildGeraetepruefung.append("\n");
            }
         }
      }

      if(buildGeraetepruefung.toString().equals(einleitung)) {
         return "";
      } else {
         if(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("geraetepruefungViaEMail")) == 1) {
            EMailService.EMailInformationServiceGeräteprüfungen(buildGeraetepruefung.toString());
         }

         buildGeraetepruefung.append("\n");
         buildGeraetepruefung.append("\n");
         return buildGeraetepruefung.toString();
      }
   }

   private static String abgelaufeneFahrzeugUntersuchung(String untersuchungsType) throws SQLException {
      StringBuilder buildAufgabenUntersuchungAbgelaufende = new StringBuilder();
      TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
      buildAufgabenUntersuchungAbgelaufende.setLength(0);
      String[] abgelaufende = Utils.listToArray(tabUntersuchung.getAbgelaufendeUntersuchungen(untersuchungsType));
      String einleitung = null;
      if(untersuchungsType.equals("TUEV")) {
         einleitung = "Abgelaufene TÜV / HU Untersuchung:\n----------------------------------------\n";
      } else {
         einleitung = "Abgelaufene " + untersuchungsType + " Untersuchung:" + "\n" + "----------------------------------------" + "\n";
      }

      buildAufgabenUntersuchungAbgelaufende.append(einleitung);

      for(int i = 0; i < abgelaufende.length; ++i) {
         if(!abgelaufende[i].equals("")) {
            buildAufgabenUntersuchungAbgelaufende.append(abgelaufende[i]);
            buildAufgabenUntersuchungAbgelaufende.append("\n");
         }
      }

      if(buildAufgabenUntersuchungAbgelaufende.toString().equals(einleitung)) {
         return "";
      } else {
         buildAufgabenUntersuchungAbgelaufende.append("\n");
         buildAufgabenUntersuchungAbgelaufende.append("\n");
         return buildAufgabenUntersuchungAbgelaufende.toString();
      }
   }

   private static String anstehendeTermine() throws SQLException {
      StringBuilder buildAnstehendeTermine = new StringBuilder();
      TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
      String einleitung = "Termine:\n----------------------------------------\n";
      buildAnstehendeTermine.append(einleitung);
      String[] liste = Utils.listToArray(tabVeranstaltung.getVeranstaltungDiesenMonats());

      for(int i = 0; i < liste.length; ++i) {
         buildAnstehendeTermine.append(liste[i]);
         buildAnstehendeTermine.append("\n");
      }

      if(buildAnstehendeTermine.toString().equals(einleitung)) {
         buildAnstehendeTermine.append("Keine aktuellen Termine verfügbar!");
         buildAnstehendeTermine.append("\n");
         buildAnstehendeTermine.append("\n");
         return buildAnstehendeTermine.toString();
      } else {
         buildAnstehendeTermine.append("\n");
         buildAnstehendeTermine.append("\n");
         return buildAnstehendeTermine.toString();
      }
   }

   private static String offeneManegelmeldungen() throws SQLException {
      StringBuilder buildOffeneManegelmeldungen = new StringBuilder();
      TabelleMaengelmeldung tabMaengel = new TabelleMaengelmeldung();
      buildOffeneManegelmeldungen.setLength(0);
      String einleitung = "Offene Mängelmeldungen:\n----------------------------------------\n";
      buildOffeneManegelmeldungen.append(einleitung);
      int mandantID = Integer.parseInt((String)runApplication.PROPERTIES.get("MandantID"));
      if(runApplication.BF == 1) {
         mandantID = 0;
      }

      String[] mängel = Utils.listToArray(tabMaengel.getMaengelmeldungForInformation(mandantID));

      for(int i = 0; i < mängel.length; ++i) {
         buildOffeneManegelmeldungen.append(mängel[i]);
         buildOffeneManegelmeldungen.append("\n");
      }

      if(buildOffeneManegelmeldungen.toString().equals(einleitung)) {
         return "";
      } else {
         buildOffeneManegelmeldungen.append("\n");
         buildOffeneManegelmeldungen.append("\n");
         return buildOffeneManegelmeldungen.toString();
      }
   }

   private static String calculateWarningTime(String time, int reduzierung) throws NumberFormatException, SQLException {
      try {
         int e = Integer.parseInt(time.substring(0, 2)) - reduzierung;
         String neu;
         if(e <= 9 && e >= 1) {
            neu = "0" + Integer.toString(e) + "." + time.substring(3, 7);
         } else if(e <= 0) {
            int vorjahr = 12 + e;
            if(vorjahr <= 9 && vorjahr >= 1) {
               neu = "0" + Integer.toString(vorjahr) + "." + Integer.toString(Integer.parseInt(time.substring(3, 7)) - 1);
            } else {
               neu = Integer.toString(vorjahr) + "." + Integer.toString(Integer.parseInt(time.substring(3, 7)) - 1);
            }
         } else {
            neu = Integer.toString(e) + "." + time.substring(3, 7);
         }

         return neu;
      } catch (StringIndexOutOfBoundsException var5) {
         return null;
      }
   }
}
