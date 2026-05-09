/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package utilities.joomla;

import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabellePHP;
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleStichwort;
import data.tabellen.mitglied.TabelleMitglied;
import go.Ausbildung_Kategorie;
import go.Ausbildung_Plan;
import go.Einsatz;
import go.Fahrzeug;
import go.Organisation;
import go.PHP_Request;
import go.StatistikEinsatz;
import go.Veranstaltung;
import go.Veranstaltung_Kategorie;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.HashMap;
import logging.logging;
import run.runApplication;
import service.EMailService;
import utilities.SbcUtils;
import utilities.Utils;

public class Joomla {
    public static void erstelleEinsatz(Einsatz einsatz, StatistikEinsatz statistik, boolean sendMail, boolean getProtokoll) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_com_Einsatz");
            String[] parameter = new String[18];
            parameter[0] = "id=" + einsatz.getVeranstaltungID();
            parameter[1] = "einsatzNummer=" + einsatz.getEinsatznummer();
            int errechneteStichwortID = 0;
            if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort").equals("1")) {
                parameter[2] = "stichwort=" + new TabelleStichwort().getStichwortName(einsatz.getStichwort());
                parameter[3] = "stichwortID=" + einsatz.getStichwort();
            } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort").equals("2")) {
                errechneteStichwortID = statistik.getKategorie() + 1000;
                parameter[2] = "stichwort=" + new TabelleEinsatz_kategorie().getEinsatzKategorieName(statistik.getKategorie());
                parameter[3] = "stichwortID=" + errechneteStichwortID;
            } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort").equals("3")) {
                errechneteStichwortID = einsatz.getStichwort() + 500;
                parameter[2] = "stichwort=" + new TabelleEinsatz_kategorie().getEinsatzKategorieName(statistik.getKategorie()) + " / " + new TabelleStichwort().getStichwortName(einsatz.getStichwort());
                parameter[3] = "stichwortID=" + errechneteStichwortID;
            }
            parameter[4] = statistik.getKategorie() == 1 ? "farbcode=#ff0000" : (statistik.getKategorie() == 2 ? "farbcode=#0066ff" : (statistik.getKategorie() == 3 ? "farbcode=#ff9900" : (statistik.getKategorie() == 4 ? "farbcode=#ffffff" : (statistik.getKategorie() == 5 ? "farbcode=#ff8000" : (statistik.getKategorie() == 6 ? "farbcode=#088A08" : "farbcode=#ff0000")))));
            parameter[5] = "alarmierungsZeit=" + einsatz.getDatum() + " " + einsatz.getZeitAlarm() + ":00";
            if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteNurAlamierung\u00dcbertragen").equals("0")) {
                parameter[6] = "einsatzEnde=" + einsatz.getDatum() + " " + einsatz.getZeitEingerueckt() + ":00";
                parameter[7] = "einsatzAusfahrt=" + einsatz.getDatum() + " " + einsatz.getZeitAusgerueckt() + ":00";
            } else {
                parameter[6] = "einsatzEnde=0000-00-00 00:00:00";
                parameter[7] = "einsatzAusfahrt=0000-00-00 00:00:00";
            }
            if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("1")) {
                parameter[8] = "ort=" + einsatz.getOrt();
            } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("2")) {
                parameter[8] = "ort=" + einsatz.getOrt().replaceAll("[0-9]", "");
            } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("3")) {
                parameter[8] = "ort=" + einsatz.getOrt().replaceAll("[0-9]", "") + " / " + einsatz.getStadtteil();
            } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("4")) {
                parameter[8] = "ort=" + einsatz.getOrt() + " / " + einsatz.getStadtteil();
            }
            parameter[9] = "einsatzleiter=" + new TabelleMitglied().getNameVornameByID(einsatz.getEinsatzleiter());
            parameter[10] = "fahrzeugName=" + einsatz.getFahrzeug();
            int[] idListe = Utils.listToIntArray(new TabelleEinsatz_zeiten().getFahrzeugListe(einsatz.getVeranstaltungID()));
            StringBuilder build = new StringBuilder();
            int x = 0;
            while (x < idListe.length) {
                build.append(idListe[x]);
                build.append(",");
                ++x;
            }
            parameter[11] = "fahrzeugID=" + build.toString();
            parameter[12] = einsatz.getBeschreibung().equals("") ? "beschreibung=--" : "beschreibung=" + einsatz.getBeschreibung();
            TabelleProtokoll tabProtokoll = new TabelleProtokoll();
            if (getProtokoll && tabProtokoll.getCount(einsatz.getVeranstaltungID()) == 1 && runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteEinsatzBericht\u00dcbermitteln").equals("1")) {
                logging.logInfo((Object)"\u00dcberschreibe Kurzbeschreibung mit dem Richtigen Protokoll / Bericht");
                StringBuilder buildEinsatzBerciht = new StringBuilder();
                String[] einsatzBericht = tabProtokoll.getData(einsatz.getVeranstaltungID()).getProtokolltext().split("\n");
                int b = 0;
                while (b < einsatzBericht.length) {
                    buildEinsatzBerciht.append(einsatzBericht[b]);
                    buildEinsatzBerciht.append("<p>");
                    ++b;
                }
                parameter[12] = "beschreibung=" + buildEinsatzBerciht.toString();
            }
            parameter[13] = "sichtbar=" + runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible");
            parameter[14] = "secretkey=" + runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteSecretKey");
            parameter[15] = "organisation=" + runApplication.EINSTELLUNGEN.get("Name");
            parameter[16] = "organisationID =1";
            parameter[17] = "organisationListe=" + new TabelleEinsatz_organisationen().getOrganisationIDKommaSeperated(einsatz.getVeranstaltungID());
            Joomla.sendPostRequest(adresse, parameter);
            if (runApplication.EINSTELLUNGEN.get("emailModul").equals("1") && runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMail").equals("1") && sendMail) {
                EMailService.EMailInformationServiceEinsatzkomponente(einsatz, statistik);
            }
        }
        catch (IOException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void erstelleEinsatzBericht(int veranstaltungID, String[] einsatzBericht, String organisationListe) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_com_Einsatz_Bericht");
            String[] parameter = new String[3];
            StringBuilder build = new StringBuilder();
            int b = 0;
            while (b < einsatzBericht.length) {
                build.append(einsatzBericht[b]);
                build.append("<p>");
                ++b;
            }
            System.out.println(build.toString());
            parameter[0] = "id=" + veranstaltungID;
            parameter[1] = build.toString().equals("") | build.toString().equals("<p>") ? "bericht=--" : "bericht=" + build.toString();
            parameter[2] = "organisationListe=" + organisationListe;
            Joomla.sendPostRequest(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void erstelleOrganisation(Organisation organisation) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_com_Einsatz_Orgaisation");
            String[] parameter = new String[]{"id=" + organisation.getId(), organisation.getId() == 1 ? "name=" + runApplication.EINSTELLUNGEN.get("Name") : "name=" + organisation.getName(), "sortierung=" + organisation.getSortierung(), "sichtbar=" + runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible"), "secretkey=" + runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteSecretKey")};
            Joomla.sendPostRequest(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void erstelleFahrzeug(Fahrzeug fahrzeug) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_com_Einsatz_Fahrzeug");
            String[] parameter = new String[]{"id=" + fahrzeug.getId(), "name=" + fahrzeug.getName(), "sortierung=" + fahrzeug.getSortierung(), "sichtbar=" + runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible"), "organisationID=1", "secretkey=" + runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteSecretKey")};
            Joomla.sendPostRequest(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void einsatzkomoneteLeeren() {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_com_Einsatz_Delete");
            Joomla.sendRequest(adresse);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void erstelleVeranstaltung(Veranstaltung veranstaltung) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_Veranstaltung");
            String[] parameter = new String[]{"id=" + veranstaltung.getId(), "name=" + veranstaltung.getName(), "name2=" + veranstaltung.getName2(), "kategorie=" + veranstaltung.getKategorie(), "datum=" + veranstaltung.getDatum(), "zeit=" + veranstaltung.getZeit(), "zeitEnde=" + veranstaltung.getZeitEnde(), veranstaltung.getName().toString().startsWith("BSW") ? "personen=" + Utils.getTeilnehmerEinerVeranstaltung(veranstaltung.getId()) : "personen="};
            Joomla.addPostRequestToDB(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void updateVeranstaltung(Veranstaltung veranstaltung) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_Veranstaltung_update");
            String[] parameter = new String[]{"id=" + veranstaltung.getId(), "name=" + veranstaltung.getName(), "name2=" + veranstaltung.getName2(), "kategorie=" + veranstaltung.getKategorie(), "datum=" + veranstaltung.getDatum(), "zeit=" + veranstaltung.getZeit(), "zeitEnde=" + veranstaltung.getZeitEnde(), veranstaltung.getName().toString().startsWith("BSW") ? "personen=" + Utils.getTeilnehmerEinerVeranstaltung(veranstaltung.getId()) : "personen="};
            Joomla.addPostRequestToDB(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void erstelleVeranstaltungKategorie(Veranstaltung_Kategorie kategorie) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_VeranstaltungKategorie");
            String[] parameter = new String[]{"id=" + kategorie.getId(), "name=" + kategorie.getName()};
            Joomla.addPostRequestToDB(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void deleteAllVeranstaltungData() {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_Veranstaltung_delete");
            Joomla.addRequestToDB(adresse);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void deleteAllAusbildungData() {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_Ausbildungsplan_delete");
            Joomla.addRequestToDB(adresse);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void deleteAllVeranstaltungKategorieData() {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_VeranstaltungKategorie_delete");
            Joomla.addRequestToDB(adresse);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void deleteAllAusbildungKategorieData() {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_AusbildungKategorie_delete");
            Joomla.addRequestToDB(adresse);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void erstelleAusbildungsplan(Ausbildung_Plan plan, HashMap<Integer, String> map) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_Ausbildungsplan");
            String[] parameter = new String[]{"id=" + plan.getId(), "jahr=" + plan.getJahr(), "veranstaltungID=" + plan.getVeranstaltungID(), "ausbildungKategorie=" + plan.getAusbildungKategorie(), "details=" + plan.getDetails(), plan.getAusbilder1() != 0 ? "ausbilder1=" + map.get(plan.getAusbilder1()) : "ausbilder1=", plan.getAusbilder2() != 0 ? "ausbilder2=" + map.get(plan.getAusbilder2()) : "ausbilder2="};
            Joomla.addPostRequestToDB(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void erstelleAusbildungKategorie(Ausbildung_Kategorie kategorie) {
        try {
            String adresse = String.valueOf(runApplication.EINSTELLUNGEN.get("JoomlaLink")) + runApplication.EINSTELLUNGEN.get("Joomla_mod_AusbildungKategorie");
            String[] parameter = new String[]{"id=" + kategorie.getId(), "name=" + kategorie.getName()};
            Joomla.addPostRequestToDB(adresse, parameter);
        }
        catch (IOException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void nutzungFMS(final String aktion) {
        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    String adresse = "http://feuerwehrmanagementsystem.de/nutzung.php";
                    String[] parameter = new String[]{"datum=" + SbcUtils.timeStamp((String)"dd.MM.yyyy"), "zeit=" + SbcUtils.timeStamp((String)"HH:mm:ss"), "clientID=" + runApplication.PROPERTIES.get("ClientID"), "aktion=" + aktion, "version=Version: 3.21", "plz=" + runApplication.EINSTELLUNGEN.get("plz"), "stadt=" + runApplication.EINSTELLUNGEN.get("Stadt"), "bundesland=" + runApplication.EINSTELLUNGEN.get("bundesland")};
                    Joomla.sendPostRequest(adresse, parameter);
                    this.stop();
                }
                catch (Exception e) {
                    this.stop();
                }
            }
        };
        thread.start();
    }

    private static void addPostRequestToDB(String adresse, String[] parameter) throws IOException, MalformedURLException {
        try {
            if (runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung").equals("1")) {
                logging.logInfo((Object)"PHP POST REQUEST in DB -->  getakteteInternetverbindung ==  1");
                TabellePHP tabPHP = new TabellePHP();
                PHP_Request data = new PHP_Request();
                StringBuilder build = new StringBuilder();
                int i = 0;
                while (i < parameter.length) {
                    build.append(parameter[i]);
                    build.append("\n");
                    ++i;
                }
                data.setId(tabPHP.getNextNummer());
                data.setTyp("POST");
                data.setAdresse(adresse);
                data.setParameter(build.toString());
                tabPHP.insert(data);
            } else {
                logging.logInfo((Object)"PHP POST REQUEST wird ausgef\u00fchrt -->  getakteteInternetverbindung ==  0");
                Joomla.sendPostRequest(adresse, parameter);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private static void addRequestToDB(String adresse) throws IOException, MalformedURLException {
        try {
            if (runApplication.EINSTELLUNGEN.get("getakteteInternetverbindung").equals("1")) {
                logging.logInfo((Object)"PHP REQUEST in DB -->  getakteteInternetverbindung ==  1");
                TabellePHP tabPHP = new TabellePHP();
                PHP_Request data = new PHP_Request();
                data.setId(tabPHP.getNextNummer());
                data.setTyp("REQUEST");
                data.setAdresse(adresse);
                data.setParameter("");
                tabPHP.insert(data);
            } else {
                logging.logInfo((Object)"PHP REQUEST wird ausgef\u00fchrt -->  getakteteInternetverbindung ==  0");
                Joomla.sendRequest(adresse);
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void sendPostRequest(String adresse, String[] parameter) throws IOException, MalformedURLException {
        double startZeit = System.currentTimeMillis();
        logging.logInfo((Object)("PHP POST REQUEST: " + adresse));
        URL url = new URL(adresse);
        URLConnection connect = url.openConnection();
        connect.setDoOutput(true);
        PrintStream ps = new PrintStream(connect.getOutputStream());
        int p = 0;
        while (p < parameter.length) {
            if (p == 0) {
                ps.print(parameter[p]);
            } else {
                ps.print("&" + parameter[p]);
            }
            ++p;
        }
        logging.logInfo((Object)("Datensatz auf " + adresse + " erfolgreich ausgef\u00fchrt..."));
        connect.getInputStream();
        ps.close();
        double endZeit = (double)System.currentTimeMillis() - startZeit;
        logging.logInfo((Object)("PHP POST REQUEST TIME: " + endZeit + " ms"));
    }

    public static void sendRequest(String adresse) throws IOException, MalformedURLException {
        double startZeit = System.currentTimeMillis();
        logging.logInfo((Object)("PHP REQUEST: " + adresse));
        InputStream in = new URL(adresse).openStream();
        in.close();
        logging.logInfo((Object)("Datensatz auf " + adresse + " erfolgreich ausgef\u00fchrt..."));
        double endZeit = (double)System.currentTimeMillis() - startZeit;
        logging.logInfo((Object)("PHP REQUEST TIME: " + endZeit + " ms"));
    }
}

