/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package service;

import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeug_untersuchung;
import data.tabellen.TabelleGeraetepruefung;
import data.tabellen.TabelleMaengelmeldung;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
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
        runApplication.infoServiceL\u00e4uft = 1;
        StringBuilder buildAufgaben = new StringBuilder();
        try {
            buildAufgaben.setLength(0);
            buildAufgaben.append(InformationService.systemWarnung());
            if (runApplication.EINSTELLUNGEN.get("termineAnzeigen").equals("1") && BerechtigunsManager.ber2[0] == 1) {
                logging.logInfo((Object)"InformationService: Termin Anzeigen");
                buildAufgaben.append(InformationService.anstehendeTermine());
            }
            EMailService.EMailVersandtServiceAnstehendeVeranstaltungen();
            if (runApplication.EINSTELLUNGEN.get("gebAnzeigen").equals("1") && BerechtigunsManager.ber2[1] == 1) {
                logging.logInfo((Object)"InformationService: Geb. Anzeigen");
                buildAufgaben.append(InformationService.gebService());
            }
            if (BerechtigunsManager.ber2[2] == 1) {
                logging.logInfo((Object)"InformationService: G26 Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceG26());
            }
            if (BerechtigunsManager.ber2[3] == 1) {
                logging.logInfo((Object)"InformationService: G25 Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceG25());
            }
            if (BerechtigunsManager.ber2[4] == 1) {
                logging.logInfo((Object)"InformationService: G30 Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceG30());
            }
            if (runApplication.EINSTELLUNGEN.get("ablaufLKWAnzeigen").equals("1") && BerechtigunsManager.ber2[5] == 1) {
                logging.logInfo((Object)"InformationService: Ablauf LKW Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceAblaufLKW());
            }
            if (runApplication.EINSTELLUNGEN.get("agtTrainingAnzeigen").equals("1") && BerechtigunsManager.ber2[6] == 1) {
                logging.logInfo((Object)"InformationService: AGT Training Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceAgtTraining());
            }
            if (runApplication.EINSTELLUNGEN.get("ablaufDienstausweisAnzeigen").equals("1") && BerechtigunsManager.ber2[7] == 1) {
                logging.logInfo((Object)"InformationService: Ablauf Dienstausweis Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceAblaufDienstausweis());
            }
            if (runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungAnzeigen").equals("1") && BerechtigunsManager.ber2[8] == 1) {
                logging.logInfo((Object)"InformationService: Ablauf Fahrberechtigung Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceAblaufFahrberechtigung());
            }
            if (BerechtigunsManager.ber2[9] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufene G26 Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneUntersuchung("G26"));
            }
            if (BerechtigunsManager.ber2[10] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufene G25 Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneUntersuchung("G25"));
            }
            if (BerechtigunsManager.ber2[11] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufene G30 Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneUntersuchung("G30"));
            }
            if (BerechtigunsManager.ber2[12] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufene AGT Training Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneUntersuchung("AGTTraining"));
            }
            if (runApplication.EINSTELLUNGEN.get("ablaufLKWAnzeigen").equals("1") && BerechtigunsManager.ber2[13] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufende LKW F\u00fchrerscheine");
                buildAufgaben.append(InformationService.abgelaufeneUntersuchung("ablaufLKW"));
            }
            if (runApplication.EINSTELLUNGEN.get("ablaufDienstausweisAnzeigen").equals("1") && BerechtigunsManager.ber2[14] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufende Dienstausweise");
                buildAufgaben.append(InformationService.abgelaufeneUntersuchung("ablaufDienstausweis"));
            }
            if (runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungAnzeigen").equals("1") && BerechtigunsManager.ber2[15] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufene Fahrberechtigungen Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneUntersuchung("pruefungDerFahrberechtigung"));
            }
            if (BerechtigunsManager.ber2[16] == 1) {
                logging.logInfo((Object)"InformationService: T\u00dcV Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceT\u00dcV());
            }
            if (BerechtigunsManager.ber2[17] == 1) {
                logging.logInfo((Object)"InformationService: SP Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceSP());
            }
            if (BerechtigunsManager.ber2[18] == 1) {
                logging.logInfo((Object)"InformationService: Service Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceService());
            }
            if (BerechtigunsManager.ber2[19] == 1) {
                logging.logInfo((Object)"InformationService: Gaswartung Anzeigen");
                buildAufgaben.append(InformationService.untersuchungServiceGaswartung());
            }
            if (BerechtigunsManager.ber2[20] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufener T\u00dcV Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneFahrzeugUntersuchung("TUEV"));
            }
            if (BerechtigunsManager.ber2[21] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufene SP Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneFahrzeugUntersuchung("SP"));
            }
            if (BerechtigunsManager.ber2[22] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufener Serive Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneFahrzeugUntersuchung("Service"));
            }
            if (BerechtigunsManager.ber2[23] == 1) {
                logging.logInfo((Object)"InformationService: Abgelaufene Gaswartung Anzeigen");
                buildAufgaben.append(InformationService.abgelaufeneFahrzeugUntersuchung("Gaswartung"));
            }
            if (BerechtigunsManager.ber2[24] == 1) {
                logging.logInfo((Object)"InformationService: Ger\u00e4tepr\u00fcfung Anzeigen");
                buildAufgaben.append(InformationService.geraetepruefungService());
            }
            if (runApplication.EINSTELLUNGEN.get("offeneMaengelAnzeigen").equals("1") && BerechtigunsManager.ber2[25] == 1) {
                logging.logInfo((Object)"InformationService: offene M\u00e4ngel Anzeigen");
                buildAufgaben.append(InformationService.offeneManegelmeldungen());
            }
            if (buildAufgaben.length() == 0) {
                buildAufgaben.append("Keine aktuellen Informationen verf\u00fcgbar");
                logging.logInfo((Object)"InformationService: Keine Information verf\u00fcgbar!");
            }
            if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && (Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 || Integer.parseInt(runApplication.EINSTELLUNGEN.get("ablaufLKWF\u00fchrerscheinViaEMail")) == 1 || Integer.parseInt(runApplication.EINSTELLUNGEN.get("geraetepruefungViaEMail")) == 1 || Integer.parseInt(runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1 || Integer.parseInt(runApplication.EINSTELLUNGEN.get("ablaufDienstausweisViaEMail")) == 1 || Integer.parseInt(runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungViaEMail")) == 1 || Integer.parseInt(runApplication.EINSTELLUNGEN.get("terminVersandtViaEMail")) == 1)) {
                logging.logInfo((Object)"InformationService: Sende Paostausgang");
                SendePostausgang.sendAusgang();
            }
            logging.logInfo((Object)"InformationService: Setze Status auf 0!");
            runApplication.infoServiceL\u00e4uft = 0;
            logging.logInfo((Object)"InformationService: FERTIG...");
            return buildAufgaben.toString();
        }
        catch (Exception e) {
            logging.logError((Object)"InformationService: Setze Status auf 0!");
            runApplication.infoServiceL\u00e4uft = 0;
            logging.logError((Object)"InformationService: FEHLER...");
            logging.logPrintStackTrace((Exception)e);
            return "Fehler in der Darstellung";
        }
    }

    private static String systemWarnung() throws SQLException {
        StringBuilder buildSystemWarnung = new StringBuilder();
        String einleitung = "Systemwarnung:\n----------------------------------------\n";
        buildSystemWarnung.append(einleitung);
        buildSystemWarnung.append(SystemWarnungService.checkSystem());
        if (buildSystemWarnung.toString().equals(einleitung)) {
            return "";
        }
        buildSystemWarnung.append("\n");
        buildSystemWarnung.append("\n");
        logging.logInfo((Object)("L\u00e4nge der Systemwarnung: " + buildSystemWarnung.toString().length()));
        lengthOfSystemWarning = buildSystemWarnung.toString().length();
        return buildSystemWarnung.toString();
    }

    private static String gebService() throws SQLException {
        StringBuilder buildAufgabenGeb = new StringBuilder();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        buildAufgabenGeb.setLength(0);
        String[] gebListe = null;
        String einleitung = null;
        if (runApplication.EINSTELLUNGEN.get("gebAnzeigeModus").equals("1")) {
            einleitung = "Geburtstag (Heute):\n----------------------------------------\n";
            gebListe = Utils.listToArray(tabMitglieder.getGebDatumForInformationService(1));
        } else if (runApplication.EINSTELLUNGEN.get("gebAnzeigeModus").equals("2")) {
            einleitung = "Geburtstage (Monat " + SbcUtils.timeStamp((String)"MMMM") + "):" + "\n" + "----------------------------------------" + "\n";
            gebListe = Utils.listToArray(tabMitglieder.getGebDatumForInformationService(2));
        }
        buildAufgabenGeb.append(einleitung);
        int i = 0;
        while (i < gebListe.length) {
            buildAufgabenGeb.append(gebListe[i].substring(0, gebListe[i].length() - 4));
            buildAufgabenGeb.append("\n");
            ++i;
        }
        if (buildAufgabenGeb.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenGeb.append("\n");
        buildAufgabenGeb.append("\n");
        return buildAufgabenGeb.toString();
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
        int i = 0;
        while (i < mitgliederListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
            while (m > 0) {
                int mID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG26(mID = mitgliederIDListe[i])), warung))) {
                    buildAufgabenUntersuchung.append(String.valueOf(mitgliederListe[i]) + " " + untersuchung);
                    buildAufgabenUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert").equals("0")) {
                        EMailService.EMailInformationServiceG26(mitgliederListe[i], "G26", untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenUntersuchung.append("\n");
        buildAufgabenUntersuchung.append("\n");
        if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert").equals("1")) {
            logging.logInfo((Object)"Globale E-Mail G26 ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenUntersuchung.toString(), "G26");
        }
        return buildAufgabenUntersuchung.toString();
    }

    private static String untersuchungServiceAblaufLKW() throws SQLException {
        StringBuilder buildAufgabenUntersuchung = new StringBuilder();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
        buildAufgabenUntersuchung.setLength(0);
        String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
        int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
        String einleitung = "Ablaufdatum LKW F\u00fchrerschein:\n----------------------------------------\n";
        buildAufgabenUntersuchung.append(einleitung);
        int i = 0;
        while (i < mitgliederListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
            while (m > 0) {
                int mID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAblaufLKW(mID = mitgliederIDListe[i])), warung))) {
                    buildAufgabenUntersuchung.append(String.valueOf(mitgliederListe[i]) + " " + untersuchung);
                    buildAufgabenUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("ablaufLKWF\u00fchrerscheinViaEMail")) == 1) {
                        EMailService.EMailInformationServiceAblaufLKW(mitgliederListe[i], untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenUntersuchung.append("\n");
        buildAufgabenUntersuchung.append("\n");
        return buildAufgabenUntersuchung.toString();
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
        int i = 0;
        while (i < mitgliederListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("vorwarnungAblaufDienstausweis"));
            while (m > 0) {
                int mID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAblaufDienstausweis(mID = mitgliederIDListe[i])), warung))) {
                    buildAufgabenDienstausweis.append(String.valueOf(mitgliederListe[i]) + " " + untersuchung);
                    buildAufgabenDienstausweis.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("ablaufDienstausweisViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweisAktiviert").equals("0")) {
                        EMailService.EMailInformationServiceAblaufDienstausweis(mitgliederListe[i], untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenDienstausweis.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenDienstausweis.append("\n");
        buildAufgabenDienstausweis.append("\n");
        if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweisAktiviert").equals("1")) {
            logging.logInfo((Object)"Globale E-Mail Dienstausweis ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenDienstausweis.toString(), "Dienstausweis");
        }
        return buildAufgabenDienstausweis.toString();
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
        int i = 0;
        while (i < mitgliederListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("vorwarnungAblaufFahrberechtigung"));
            while (m > 0) {
                int mID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getPruefungDerFahrberechtigung(mID = mitgliederIDListe[i])), warung))) {
                    buildAufgabenFahrberechtigung.append(String.valueOf(mitgliederListe[i]) + " " + untersuchung);
                    buildAufgabenFahrberechtigung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigungAktiviert").equals("0")) {
                        EMailService.EMailInformationServiceAblaufDerFahberechtigung(mitgliederListe[i], untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenFahrberechtigung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenFahrberechtigung.append("\n");
        buildAufgabenFahrberechtigung.append("\n");
        if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigungAktiviert").equals("1")) {
            logging.logInfo((Object)"Globale E-Mail Fahrberechtigung ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenFahrberechtigung.toString(), "Fahrberechtigung");
        }
        return buildAufgabenFahrberechtigung.toString();
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
        int i = 0;
        while (i < mitgliederListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
            while (m > 0) {
                int mID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG25(mID = mitgliederIDListe[i])), warung))) {
                    buildAufgabenUntersuchung.append(String.valueOf(mitgliederListe[i]) + " " + untersuchung);
                    buildAufgabenUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailG25Aktiviert").equals("0")) {
                        EMailService.EMailInformationServiceG25(mitgliederListe[i], "G25", untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenUntersuchung.append("\n");
        buildAufgabenUntersuchung.append("\n");
        if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailG25Aktiviert").equals("1")) {
            logging.logInfo((Object)"Globale E-Mail G25 ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenUntersuchung.toString(), "G25");
        }
        return buildAufgabenUntersuchung.toString();
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
        int i = 0;
        while (i < mitgliederListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
            while (m > 0) {
                int mID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getG30(mID = mitgliederIDListe[i])), warung))) {
                    buildAufgabenUntersuchung.append(String.valueOf(mitgliederListe[i]) + " " + untersuchung);
                    buildAufgabenUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert").equals("0")) {
                        EMailService.EMailInformationServiceG30(mitgliederListe[i], "G30", untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenUntersuchung.append("\n");
        buildAufgabenUntersuchung.append("\n");
        if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungViaEMail")) == 1 && runApplication.EINSTELLUNGEN.get("globaleEMailG26Aktiviert").equals("1")) {
            logging.logInfo((Object)"Globale E-Mail G30 ist Aktiv --> EMailInformationServiceUntersuchungGlobaleEMailAdressen()");
            EMailService.EMailInformationServiceUntersuchungGlobaleEMailAdressen(buildAufgabenUntersuchung.toString(), "G30");
        }
        return buildAufgabenUntersuchung.toString();
    }

    private static String untersuchungServiceAgtTraining() throws SQLException {
        StringBuilder buildAufgabenUntersuchung = new StringBuilder();
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
        buildAufgabenUntersuchung.setLength(0);
        String[] mitgliederListe = Utils.listToArray(tabMitglieder.getMitgliederGruppe1());
        int[] mitgliederIDListe = Utils.listToIntArray(tabMitglieder.getMitgliederIDGruppe1());
        String einleitung = "n\u00e4chstes AGT-Training:\n----------------------------------------\n";
        buildAufgabenUntersuchung.append(einleitung);
        int i = 0;
        while (i < mitgliederListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
            while (m > 0) {
                int mID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getAgtTraining(mID = mitgliederIDListe[i])), warung))) {
                    buildAufgabenUntersuchung.append(String.valueOf(mitgliederListe[i]) + " " + untersuchung);
                    buildAufgabenUntersuchung.append("\n");
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenUntersuchung.append("\n");
        buildAufgabenUntersuchung.append("\n");
        return buildAufgabenUntersuchung.toString();
    }

    private static String abgelaufeneUntersuchung(String untersuchungsType) throws SQLException {
        StringBuilder buildAufgabenUntersuchungAbgelaufende = new StringBuilder();
        TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
        buildAufgabenUntersuchungAbgelaufende.setLength(0);
        String[] abgelaufende = Utils.listToArray(tabUntersuchung.getAbgelaufendeUntersuchungen(untersuchungsType));
        String einleitung = untersuchungsType.equals("ablaufLKW") ? "Abgelaufene LKW F\u00fchrerscheine:\n----------------------------------------\n" : (untersuchungsType.equals("ablaufDienstausweis") ? "Abgelaufene Dienstausweise:\n----------------------------------------\n" : (untersuchungsType.equals("pruefungDerFahrberechtigung") ? "Abgelaufene Fahrberechtigung:\n----------------------------------------\n" : "Abgelaufene " + untersuchungsType + " Untersuchung:" + "\n" + "----------------------------------------" + "\n"));
        buildAufgabenUntersuchungAbgelaufende.append(einleitung);
        int i = 0;
        while (i < abgelaufende.length) {
            if (!abgelaufende[i].equals("")) {
                buildAufgabenUntersuchungAbgelaufende.append(abgelaufende[i]);
                buildAufgabenUntersuchungAbgelaufende.append("\n");
            }
            ++i;
        }
        if (buildAufgabenUntersuchungAbgelaufende.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenUntersuchungAbgelaufende.append("\n");
        buildAufgabenUntersuchungAbgelaufende.append("\n");
        return buildAufgabenUntersuchungAbgelaufende.toString();
    }

    private static String untersuchungServiceT\u00dcV() throws SQLException {
        StringBuilder buildAufgabenFahrzeugUntersuchung = new StringBuilder();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
        buildAufgabenFahrzeugUntersuchung.setLength(0);
        String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
        int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
        String einleitung = "T\u00dcV / AU:\n----------------------------------------\n";
        buildAufgabenFahrzeugUntersuchung.append(einleitung);
        int i = 0;
        while (i < fahrzeugListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));
            while (m > 0) {
                int fID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getT\u00fcv(fID = fahrzeugIDListe[i])), warung))) {
                    buildAufgabenFahrzeugUntersuchung.append(String.valueOf(fahrzeugListe[i]) + " " + untersuchung);
                    buildAufgabenFahrzeugUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                        EMailService.EMailInformationServiceFahrzeugTuev(fID, fahrzeugListe[i], untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenFahrzeugUntersuchung.append("\n");
        buildAufgabenFahrzeugUntersuchung.append("\n");
        return buildAufgabenFahrzeugUntersuchung.toString();
    }

    private static String untersuchungServiceSP() throws SQLException {
        StringBuilder buildAufgabenFahrzeugUntersuchung = new StringBuilder();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
        buildAufgabenFahrzeugUntersuchung.setLength(0);
        String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
        int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
        String einleitung = "Sicherheitspr\u00fcfung LKW:\n----------------------------------------\n";
        buildAufgabenFahrzeugUntersuchung.append(einleitung);
        int i = 0;
        while (i < fahrzeugListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));
            while (m > 0) {
                int fID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getSP(fID = fahrzeugIDListe[i])), warung))) {
                    buildAufgabenFahrzeugUntersuchung.append(String.valueOf(fahrzeugListe[i]) + " " + untersuchung);
                    buildAufgabenFahrzeugUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                        EMailService.EMailInformationServiceFahrzeugSP(fID, fahrzeugListe[i], untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenFahrzeugUntersuchung.append("\n");
        buildAufgabenFahrzeugUntersuchung.append("\n");
        return buildAufgabenFahrzeugUntersuchung.toString();
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
        int i = 0;
        while (i < fahrzeugListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));
            while (m > 0) {
                int fID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getService(fID = fahrzeugIDListe[i])), warung))) {
                    buildAufgabenFahrzeugUntersuchung.append(String.valueOf(fahrzeugListe[i]) + " " + untersuchung);
                    buildAufgabenFahrzeugUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                        EMailService.EMailInformationServiceFahrzeugService(fID, fahrzeugListe[i], untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenFahrzeugUntersuchung.append("\n");
        buildAufgabenFahrzeugUntersuchung.append("\n");
        return buildAufgabenFahrzeugUntersuchung.toString();
    }

    private static String untersuchungServiceGaswartung() throws SQLException {
        StringBuilder buildAufgabenFahrzeugUntersuchung = new StringBuilder();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
        buildAufgabenFahrzeugUntersuchung.setLength(0);
        String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
        int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
        String einleitung = "Gaswartung Feldk\u00fcche:\n----------------------------------------\n";
        buildAufgabenFahrzeugUntersuchung.append(einleitung);
        int i = 0;
        while (i < fahrzeugListe.length) {
            int warung;
            int m = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("untersuchungVorwarnungFahrzeug"));
            while (m > 0) {
                int fID;
                String untersuchung;
                String datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), m);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabUntersuchung.getGasWartung(fID = fahrzeugIDListe[i])), warung))) {
                    buildAufgabenFahrzeugUntersuchung.append(String.valueOf(fahrzeugListe[i]) + " " + untersuchung);
                    buildAufgabenFahrzeugUntersuchung.append("\n");
                    if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("fahrzeugUntersuchungViaEMail")) == 1) {
                        EMailService.EMailInformationServiceGaswartung(fID, fahrzeugListe[i], untersuchung);
                    }
                }
                --m;
            }
            ++i;
        }
        if (buildAufgabenFahrzeugUntersuchung.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenFahrzeugUntersuchung.append("\n");
        buildAufgabenFahrzeugUntersuchung.append("\n");
        return buildAufgabenFahrzeugUntersuchung.toString();
    }

    private static String geraetepruefungService() throws SQLException {
        StringBuilder buildGeraetepruefung = new StringBuilder();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        TabelleGeraetepruefung tabGeraete = new TabelleGeraetepruefung();
        buildGeraetepruefung.setLength(0);
        String[] fahrzeugListe = Utils.listToArray(tabFahrzeug.getAllFahrzeugeMitAnhaenger());
        int[] fahrzeugIDListe = Utils.listToIntArray(tabFahrzeug.getAllFahrzeugeIDMitAnhaenger());
        String einleitung = "Ger\u00e4tepr\u00fcfung f\u00fcr:\n----------------------------------------\n";
        buildGeraetepruefung.append(einleitung);
        int i = 0;
        while (i < fahrzeugListe.length) {
            int fID;
            String untersuchung;
            String datum;
            int warung;
            int strom = warung = Integer.parseInt(runApplication.EINSTELLUNGEN.get("vorwarnungGer\u00e4te"));
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getStromerzeuger(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Stromerzeuger) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getSteckleiter(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Steckleiter) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getSchiebleiter(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Schiebleiter) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getAbstusiset(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Absturzsicherungsset) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getHydraulik(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Hydrauligaggregat) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getPumpe(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Pumpe) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getKettensaege(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Kettens\u00e4ge) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getDoppelkanister(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Kettens\u00e4ge - Doppelkanister) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            strom = warung;
            while (strom > 0) {
                datum = InformationService.calculateWarningTime(SbcUtils.timeStamp((String)"MM.yyyy"), strom);
                if (datum.equals(InformationService.calculateWarningTime(untersuchung = TimeCalculation.parseShortDateForGUI(tabGeraete.getGer\u00e4tePr\u00fcfungAllgemein(fID = fahrzeugIDListe[i])), warung))) {
                    buildGeraetepruefung.append(String.valueOf(fahrzeugListe[i]) + " (Ger\u00e4tepr\u00fcfung allgem.) " + untersuchung);
                    buildGeraetepruefung.append("\n");
                }
                --strom;
            }
            ++i;
        }
        if (buildGeraetepruefung.toString().equals(einleitung)) {
            return "";
        }
        if (Integer.parseInt(runApplication.EINSTELLUNGEN.get("emailModul")) == 1 && Integer.parseInt(runApplication.EINSTELLUNGEN.get("geraetepruefungViaEMail")) == 1) {
            EMailService.EMailInformationServiceGer\u00e4tepr\u00fcfungen(fahrzeugIDListe[0], buildGeraetepruefung.toString());
        }
        buildGeraetepruefung.append("\n");
        buildGeraetepruefung.append("\n");
        return buildGeraetepruefung.toString();
    }

    private static String abgelaufeneFahrzeugUntersuchung(String untersuchungsType) throws SQLException {
        StringBuilder buildAufgabenUntersuchungAbgelaufende = new StringBuilder();
        TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
        buildAufgabenUntersuchungAbgelaufende.setLength(0);
        String[] abgelaufende = Utils.listToArray(tabUntersuchung.getAbgelaufendeUntersuchungen(untersuchungsType));
        String einleitung = "Abgelaufene " + untersuchungsType + " Untersuchung:" + "\n" + "----------------------------------------" + "\n";
        buildAufgabenUntersuchungAbgelaufende.append(einleitung);
        int i = 0;
        while (i < abgelaufende.length) {
            if (!abgelaufende[i].equals("")) {
                buildAufgabenUntersuchungAbgelaufende.append(abgelaufende[i]);
                buildAufgabenUntersuchungAbgelaufende.append("\n");
            }
            ++i;
        }
        if (buildAufgabenUntersuchungAbgelaufende.toString().equals(einleitung)) {
            return "";
        }
        buildAufgabenUntersuchungAbgelaufende.append("\n");
        buildAufgabenUntersuchungAbgelaufende.append("\n");
        return buildAufgabenUntersuchungAbgelaufende.toString();
    }

    private static String anstehendeTermine() throws SQLException {
        StringBuilder buildAnstehendeTermine = new StringBuilder();
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        String einleitung = "Termine:\n----------------------------------------\n";
        buildAnstehendeTermine.append(einleitung);
        String[] liste = Utils.listToArray(tabVeranstaltung.getVeranstaltungDiesenMonats());
        int i = 0;
        while (i < liste.length) {
            buildAnstehendeTermine.append(liste[i]);
            buildAnstehendeTermine.append("\n");
            ++i;
        }
        if (buildAnstehendeTermine.toString().equals(einleitung)) {
            buildAnstehendeTermine.append("Keine aktuellen Termine verf\u00fcgbar!");
            buildAnstehendeTermine.append("\n");
            buildAnstehendeTermine.append("\n");
            return buildAnstehendeTermine.toString();
        }
        buildAnstehendeTermine.append("\n");
        buildAnstehendeTermine.append("\n");
        return buildAnstehendeTermine.toString();
    }

    private static String offeneManegelmeldungen() throws SQLException {
        StringBuilder buildOffeneManegelmeldungen = new StringBuilder();
        TabelleMaengelmeldung tabMaengel = new TabelleMaengelmeldung();
        buildOffeneManegelmeldungen.setLength(0);
        String einleitung = "Offene M\u00e4ngelmeldungen:\n----------------------------------------\n";
        buildOffeneManegelmeldungen.append(einleitung);
        int mandantID = Integer.parseInt(runApplication.PROPERTIES.get("MandantID"));
        if (runApplication.BF == 1) {
            mandantID = 0;
        }
        String[] m\u00e4ngel = Utils.listToArray(tabMaengel.getMaengelmeldungForInformation(mandantID));
        int i = 0;
        while (i < m\u00e4ngel.length) {
            buildOffeneManegelmeldungen.append(m\u00e4ngel[i]);
            buildOffeneManegelmeldungen.append("\n");
            ++i;
        }
        if (buildOffeneManegelmeldungen.toString().equals(einleitung)) {
            return "";
        }
        buildOffeneManegelmeldungen.append("\n");
        buildOffeneManegelmeldungen.append("\n");
        return buildOffeneManegelmeldungen.toString();
    }

    private static String calculateWarningTime(String time, int reduzierung) throws NumberFormatException, SQLException {
        try {
            int vorjahr;
            int minus = Integer.parseInt(time.substring(0, 2)) - reduzierung;
            String neu = minus <= 9 && minus >= 1 ? "0" + Integer.toString(minus) + "." + time.substring(3, 7) : (minus <= 0 ? ((vorjahr = 12 + minus) <= 9 && vorjahr >= 1 ? "0" + Integer.toString(vorjahr) + "." + Integer.toString(Integer.parseInt(time.substring(3, 7)) - 1) : String.valueOf(Integer.toString(vorjahr)) + "." + Integer.toString(Integer.parseInt(time.substring(3, 7)) - 1)) : String.valueOf(Integer.toString(minus)) + "." + time.substring(3, 7));
            return neu;
        }
        catch (NullPointerException | StringIndexOutOfBoundsException e) {
            return null;
        }
    }
}

