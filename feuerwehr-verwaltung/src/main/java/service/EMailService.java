/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package service;

import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleFahrzeug_untersuchung;
import data.tabellen.TabelleGeraetepruefung;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.email.TabelleEMail_ausgang;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import go.Einsatz;
import go.M\u00e4ngelmeldung;
import go.StatistikEinsatz;
import go.email.Ausgang;
import java.sql.SQLException;
import logging.logging;
import pdfdocumente.mitgliedakte.PDFFahrzeugKommentar;
import pdfdocumente.mitgliedakte.PDFMitgliederKommentar;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;
import utilities.logbuchEingabe;
import utilities_email.SendePostausgang;

public class EMailService {
    public static void EMailInformationServiceG26(String name, String untersuchung, String datum) {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            int mID = tabMitglied.getIdByGuiString(name);
            if (tabUntersuchung.getInfoStatusG26(mID) == 0 && tabMitglied.getEMailDeaktivStatus(mID) == 0) {
                logging.logInfo((Object)("Miglied " + name + " wird \u00fcber anstehende Untersuchung informiert"));
                String eMailMitglied = tabMitglied.getEMail(mID);
                String betreff = "Anstehende " + untersuchung + " f\u00fcr " + name;
                String nachricht = "Hallo " + tabMitglied.getVorname(mID) + ",\n\nDeine " + untersuchung + " - Untersuchung ist im Monat: " + datum + " f\u00e4llig.\n\nBitte vereinbare einen Termin f\u00fcr die anstehende " + untersuchung + " - Untersuchung!\nDie Kontaktdaten kannst du von uns erhalten." + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                if (eMailMitglied.equals("")) {
                    return;
                }
                ausgang.setAn(eMailMitglied);
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoG26(mID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceG25(String name, String untersuchung, String datum) {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            int mID = tabMitglied.getIdByGuiString(name);
            if (tabUntersuchung.getInfoStatusG25(mID) == 0 && tabMitglied.getEMailDeaktivStatus(mID) == 0) {
                logging.logInfo((Object)("Miglied " + name + " wird \u00fcber anstehende Untersuchung informiert"));
                String eMailMitglied = tabMitglied.getEMail(mID);
                String betreff = "Anstehende " + untersuchung + " f\u00fcr " + name;
                String nachricht = "Hallo " + tabMitglied.getVorname(mID) + ",\n\nDeine " + untersuchung + " - Untersuchung ist im Monat: " + datum + " f\u00e4llig.\n\nBitte vereinbare einen Termin f\u00fcr die anstehende " + untersuchung + " - Untersuchung!\nDie Kontaktdaten kannst du von uns erhalten." + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                if (eMailMitglied.equals("")) {
                    return;
                }
                ausgang.setAn(eMailMitglied);
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoG25(mID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceG30(String name, String untersuchung, String datum) {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            int mID = tabMitglied.getIdByGuiString(name);
            if (tabUntersuchung.getInfoStatusG30(mID) == 0 && tabMitglied.getEMailDeaktivStatus(mID) == 0) {
                logging.logInfo((Object)("Miglied " + name + " wird \u00fcber anstehende Untersuchung informiert"));
                String eMailMitglied = tabMitglied.getEMail(mID);
                String betreff = "Anstehende " + untersuchung + " f\u00fcr " + name;
                String nachricht = "Hallo " + tabMitglied.getVorname(mID) + ",\n\nDeine " + untersuchung + " - Untersuchung ist im Monat: " + datum + " f\u00e4llig.\n\nBitte vereinbare einen Termin f\u00fcr die anstehende " + untersuchung + " - Untersuchung!\nDie Kontaktdaten kannst du von uns erhalten." + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                if (eMailMitglied.equals("")) {
                    return;
                }
                ausgang.setAn(eMailMitglied);
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoG30(mID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceAblaufLKW(String name, String datum) {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            int mID = tabMitglied.getIdByGuiString(name);
            if (tabUntersuchung.getInfoStatusAblaufLKW(mID) == 0 && tabMitglied.getEMailDeaktivStatus(mID) == 0) {
                logging.logInfo((Object)("Miglied " + name + " wird \u00fcber anstehende Untersuchung informiert"));
                String eMailMitglied = tabMitglied.getEMail(mID);
                String betreff = "Anstehende verl\u00e4ngerung des LKW F\u00fchrerscheins f\u00fcr " + name;
                String nachricht = "Hallo " + tabMitglied.getVorname(mID) + ",\n\nDein LKW F\u00fchreschein l\u00e4uft im Monat " + datum + " ab.\n\nBitte lasse deinen F\u00fchrerschein beim Stra\u00dfenverkehrsamt verl\u00e4ngern!" + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                if (eMailMitglied.equals("")) {
                    return;
                }
                ausgang.setAn(eMailMitglied);
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoAblaufLKW(mID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceAblaufDienstausweis(String name, String datum) {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            int mID = tabMitglied.getIdByGuiString(name);
            if (tabUntersuchung.getInfoStatusAblaufDienstausweis(mID) == 0 && tabMitglied.getEMailDeaktivStatus(mID) == 0) {
                logging.logInfo((Object)("Mitglied " + name + " wird \u00fcber Ablauf des Dienstausweises informiert"));
                String eMailMitglied = tabMitglied.getEMail(mID);
                String betreff = "Ablaufender Dienstausweis f\u00fcr " + name;
                String nachricht = "Hallo " + tabMitglied.getVorname(mID) + ",\n\nDeine Dienstausweis" + " - l\u00e4uft im Monat: " + datum + " ab.\n\nBitte beantrage einen neuen Dienstausweis!" + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                if (eMailMitglied.equals("")) {
                    return;
                }
                ausgang.setAn(eMailMitglied);
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoAblaufDienstausweis(mID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceAblaufDerFahberechtigung(String name, String datum) {
        try {
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            int mID = tabMitglied.getIdByGuiString(name);
            if (tabUntersuchung.getInfoPruefungDerFahrberechtigung(mID) == 0 && tabMitglied.getEMailDeaktivStatus(mID) == 0) {
                logging.logInfo((Object)("Mitglied " + name + " wird \u00fcber Ablauf der Fahrberechtigung informiert"));
                String eMailMitglied = tabMitglied.getEMail(mID);
                String betreff = "Ablaufende Fahrberechtigung f\u00fcr " + name;
                String nachricht = "Hallo " + tabMitglied.getVorname(mID) + ",\n\nDeine Fahrberechtigung zum F\u00fchren von Einsatzfahrzeugen" + " - l\u00e4uft im Monat: " + datum + " ab.\n\nBitte beantrage eine neue Fahrberechtigung!" + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                if (eMailMitglied.equals("")) {
                    return;
                }
                ausgang.setAn(eMailMitglied);
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoAblaufDienstausweis(mID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceUntersuchungGlobaleEMailAdressen(String liste, String untersuchung) {
        try {
            TabelleEinstellungen_gespeichert tabGespeichert = new TabelleEinstellungen_gespeichert();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            if (!tabGespeichert.getEinstellungen(untersuchung).equals(SbcUtils.timeStamp((String)"MM.yyyy"))) {
                logging.logInfo((Object)("Sende EMail: " + untersuchung + " an Globale EMail Adresse"));
                String betreff = "Anstehende " + untersuchung;
                String nachricht = "";
                nachricht = untersuchung.equals("Dienstausweis") ? "Hallo,\n\nFolgende Kameraden ben\u00f6tigen in der n\u00e4chsten Zeit einen neuen Dienstausweis! \n\n" + liste + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur") : (untersuchung.equals("Fahrberechtigung") ? "Hallo,\n\nFolgende Kameraden ben\u00f6tigen in der n\u00e4chsten Zeit einen neue Fahrberechtigung! \n\n" + liste + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur") : "Hallo,\n\nFolgende Kameraden m\u00fcssen in der n\u00e4chsten Zeit zur " + untersuchung + " Untersuchung!\n\n" + liste + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                if (untersuchung.equals("G25")) {
                    ausgang.setAn(runApplication.EINSTELLUNGEN.get("globaleEMailG25"));
                } else if (untersuchung.equals("G26") | untersuchung.equals("G30")) {
                    ausgang.setAn(runApplication.EINSTELLUNGEN.get("globaleEMailG26"));
                } else if (untersuchung.equals("Fahrberechtigung")) {
                    ausgang.setAn(runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigung"));
                } else if (untersuchung.equals("Dienstausweis")) {
                    ausgang.setAn(runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweis"));
                }
                if (ausgang.getAn().equals("")) {
                    logging.logInfo((Object)("Globale E-Mail Adresse f\u00fcr " + untersuchung + " ist leer!"));
                    return;
                }
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabGespeichert.update(untersuchung, SbcUtils.timeStamp((String)"MM.yyyy"));
                logging.logInfo((Object)("Speichere das senden an Globale E-Mail f\u00fcr " + untersuchung + " / " + SbcUtils.timeStamp((String)"MM.yyyy")));
                logbuchEingabe.NeuerEintag("Speichere das senden an Globale E-Mail f\u00fcr " + untersuchung + " / " + SbcUtils.timeStamp((String)"MM.yyyy"));
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailVersandtServiceAnstehendeVeranstaltungen() {
        try {
            if (runApplication.EINSTELLUNGEN.get("terminVersandtViaEMail").equals("1")) {
                logging.logInfo((Object)"Starte: EMailService.EMailVersandtServiceAnstehendeVeranstaltungen()");
                TabelleMitglied tabMitglied = new TabelleMitglied();
                TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
                TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
                Ausgang ausgang = new Ausgang();
                String nameMonat = SbcUtils.timeStamp((String)"MMMM");
                String jahr = SbcUtils.timeStamp((String)"yyyy");
                String monat = SbcUtils.timeStamp((String)"MM");
                logging.logInfo((Object)("Bereite versenden von Terminen vor... Jahr: " + jahr + " , Monat: " + monat));
                if (runApplication.EINSTELLUNGEN.get("terminVersandtViaEMailConfig").equals("2") && tabVeranstaltung.getCountVeranstaltungWithoutInfoVersandtInDiesemMonat(String.valueOf(jahr) + "-" + monat + "-01", String.valueOf(jahr) + "-" + monat + "-31", 1) != 0) {
                    logging.logInfo((Object)"Termine sind bereits versendet f\u00fcr diesen Monat (EINSTELLUNGEN: terminVersandtViaEMailConfig == 2)");
                    return;
                }
                if (tabVeranstaltung.getCountVeranstaltungWithoutInfoVersandtInDiesemMonat(String.valueOf(jahr) + "-" + monat + "-01", String.valueOf(jahr) + "-" + monat + "-31", 0) != 0) {
                    logging.logInfo((Object)"Habe Termine zu versenden gefunden");
                    String[] mitgliederEMailListe = Utils.listToArray(tabMitglied.getAlleMailAdressenGruppe1());
                    String[] veranstaltungsliste = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungWithoutInfoVersandtInDiesemMonat(String.valueOf(jahr) + "-" + monat + "-01", String.valueOf(jahr) + "-" + monat + "-31", false));
                    String betreff = String.valueOf(runApplication.EINSTELLUNGEN.get("Name")) + " - Termine " + nameMonat + " " + jahr;
                    StringBuilder nachricht = new StringBuilder();
                    nachricht.append("In dieser E-Mail k\u00f6nnt Ihr eine \u00dcbersicht der aktuellen Termine f\u00fcr " + nameMonat + " " + jahr + " finden.");
                    nachricht.append("\n");
                    nachricht.append("\n");
                    int t = 0;
                    while (t < veranstaltungsliste.length) {
                        nachricht.append(veranstaltungsliste[t]);
                        ++t;
                    }
                    if (runApplication.EINSTELLUNGEN.get("terminVersandtViaEMailFolgeMonat").equals("1")) {
                        logging.logInfo((Object)"F\u00fcge Termine f\u00fcr den n\u00e4chsten Monat zur E-Mail hinzu...");
                        String folgeMonat = TimeCalculation.calculateNextMonth(monat);
                        String folgeJahr = jahr;
                        if (folgeMonat.equals("01")) {
                            folgeJahr = Integer.toString(Integer.parseInt(jahr) + 1);
                        }
                        logging.logInfo((Object)("Folgemonat: " + folgeJahr + "-" + folgeMonat));
                        String[] veranstaltungslisteFolgeMonat = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungWithoutInfoVersandtInDiesemMonat(String.valueOf(folgeJahr) + "-" + folgeMonat + "-01", String.valueOf(folgeJahr) + "-" + folgeMonat + "-31", false));
                        nachricht.append("#### WEITERE TERMINE ####\nZur Vorplanung Termine f\u00fcr den n\u00e4chsten Monat:");
                        nachricht.append("\n");
                        nachricht.append("\n");
                        int fmt = 0;
                        while (fmt < veranstaltungslisteFolgeMonat.length) {
                            nachricht.append(veranstaltungslisteFolgeMonat[fmt]);
                            ++fmt;
                        }
                    }
                    nachricht.append("\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                    ausgang.setAn(runApplication.EINSTELLUNGEN.get("emailAdresse"));
                    ausgang.setCc("");
                    StringBuilder build = new StringBuilder();
                    int i = 0;
                    while (i < mitgliederEMailListe.length) {
                        build.append(mitgliederEMailListe[i]);
                        build.append(", ");
                        ++i;
                    }
                    ausgang.setBcc(build.toString().substring(0, build.toString().length() - 1));
                    ausgang.setBetreff(betreff);
                    ausgang.setNachricht(nachricht.toString());
                    ausgang.setAnhang("");
                    ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    tabAusgang.insert(ausgang);
                    tabVeranstaltung.updateInfoVersandt(String.valueOf(jahr) + "-" + monat + "-01", String.valueOf(jahr) + "-" + monat + "-31");
                    logging.logInfo((Object)"E-Mail f\u00fcr den Terminversand wurde erfolgreich in den Ausgangskorb gelegt");
                } else {
                    logging.logInfo((Object)"Keine Termine zum senden gefunden");
                }
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceVeranstaltung(String empfaenger, String veranstaltungName) {
        try {
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            Ausgang ausgang = new Ausgang();
            logging.logInfo((Object)("Miglieder der Veranstaltung " + veranstaltungName + " werden erneut per E-Mail benachrichtigt"));
            int vID = tabVeranstaltung.getVeranstaltungID(veranstaltungName);
            String nachricht = "Hallo Kameraden,\n\nErinnerungsmail an die Veranstaltung: " + veranstaltungName + "\n\nDatum: " + TimeCalculation.parseDateForGUI(tabVeranstaltung.getDatum(vID)) + "\nVon: " + tabVeranstaltung.getZeitStart(vID) + "\nBis: " + tabVeranstaltung.getZeitEnde(vID) + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
            ausgang.setAn(runApplication.EINSTELLUNGEN.get("emailAdresse"));
            ausgang.setCc("");
            ausgang.setBcc(empfaenger);
            ausgang.setBetreff(veranstaltungName);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            logging.logInfo((Object)"E-Mail f\u00fcr den Terminversand wurde erfolgreich in den Ausgangskorb gelegt");
            SendePostausgang.sendAusgang();
            logging.logInfo((Object)"Sende Postausgang");
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceGer\u00e4tepr\u00fcfungen(int fahrzeugID, String text) {
        try {
            TabelleGeraetepruefung tabGerate = new TabelleGeraetepruefung();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            if (tabGerate.getInfoEMail(fahrzeugID) == 0) {
                logging.logInfo((Object)"Ger\u00e4tewarte werden \u00fcber anstehende Ger\u00e4tepr\u00fcfungen informiert");
                String betreff = "Anstehende Ger\u00e4tepr\u00fcfungen";
                String nachricht = "Es sind Ger\u00e4tepr\u00fcfungen f\u00e4llig:\n\n" + text + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                ausgang.setAn(EMailService.setGer\u00e4tewarte(tabMitglied));
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabGerate.updateInfoEMail(1);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceFahrzeugTuev(int fahrzeugID, String fahrzeugName, String termin) {
        try {
            TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            if (tabUntersuchung.getInfoTuevStatus(fahrzeugID) == 0) {
                logging.logInfo((Object)("Ger\u00e4tewarte werden \u00fcber anstehende T\u00fcv informiert: Fahrzeug " + fahrzeugName));
                String betreff = "Anstehender T\u00fcv f\u00fcr Fahrzeug " + fahrzeugName;
                String nachricht = "Hallo Ger\u00e4tewart,\n\nder T\u00dcV f\u00fcr das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " f\u00e4llig.\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                ausgang.setAn(EMailService.setGer\u00e4tewarte(tabMitglied));
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoStatus("infoTuev", fahrzeugID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Ger\u00e4tewarte wurden per E-Mail \u00dcber den anstegenden T\u00fcv informiert.\n\nDer Termin ist:\n " + termin);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Ger\u00e4tewarte wurden per E-Mail \u00dcber den anstegenden T\u00fcv informiert.\n\nDer Termin ist:\n " + termin);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceFahrzeugSP(int fahrzeugID, String fahrzeugName, String termin) {
        try {
            TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            if (tabUntersuchung.getInfoSPStatus(fahrzeugID) == 0) {
                logging.logInfo((Object)("Ger\u00e4tewarte werden \u00fcber anstehende SP informiert: Fahrzeug " + fahrzeugName));
                String betreff = "Anstehende Sicherheitspr\u00fcfung f\u00fcr das Fahrzeug " + fahrzeugName;
                String nachricht = "Hallo Ger\u00e4tewart,\n\ndie Sicherheitspr\u00fcfung f\u00fcr das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " f\u00e4llig.\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                ausgang.setAn(EMailService.setGer\u00e4tewarte(tabMitglied));
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoStatus("infoSP", fahrzeugID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Ger\u00e4tewarte wurden per E-Mail \u00fcber die anstegende Sicherheitspr\u00fcfung informiert.\n\nDer Termin ist:\n " + termin);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Ger\u00e4tewarte wurden per E-Mail \u00fcber die anstegende Sicherheitspr\u00fcfung informiert.\n\nDer Termin ist:\n " + termin);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceFahrzeugService(int fahrzeugID, String fahrzeugName, String termin) {
        try {
            TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            if (tabUntersuchung.getInfoServiceStatus(fahrzeugID) == 0) {
                logging.logInfo((Object)("Ger\u00e4tewarte werden \u00fcber anstehenden Service informiert: Fahrzeug " + fahrzeugName));
                String betreff = "Anstehender Service f\u00fcr Fahrzeug " + fahrzeugName;
                String nachricht = "Hallo Ger\u00e4tewart,\n\nder Service f\u00fcr das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " f\u00e4llig.\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                ausgang.setAn(EMailService.setGer\u00e4tewarte(tabMitglied));
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoStatus("infoService", fahrzeugID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Ger\u00e4tewarte wurden per E-Mail \u00fcber einen anstegenden Service informiert.\n\nDer Termin ist:\n " + termin);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Ger\u00e4tewarte wurden per E-Mail \u00fcber einen anstegenden Service informiert.\n\nDer Termin ist:\n " + termin);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceGaswartung(int fahrzeugID, String fahrzeugName, String termin) {
        try {
            TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
            TabelleMitglied tabMitglied = new TabelleMitglied();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            Ausgang ausgang = new Ausgang();
            if (tabUntersuchung.getInfoGasStatus(fahrzeugID) == 0) {
                logging.logInfo((Object)("Ger\u00e4tewarte werden \u00fcber anstehenden Gaswartung informiert: Fahrzeug " + fahrzeugName));
                String betreff = "Anstehender Gaswartung f\u00fcr Fahrzeug " + fahrzeugName;
                String nachricht = "Hallo Ger\u00e4tewart,\n\ndie Gaswartung f\u00fcr das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " f\u00e4llig.\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                ausgang.setAn(EMailService.setGer\u00e4tewarte(tabMitglied));
                ausgang.setCc("");
                ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                ausgang.setBetreff(betreff);
                ausgang.setNachricht(nachricht);
                ausgang.setAnhang("");
                ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                tabAusgang.insert(ausgang);
                tabUntersuchung.updateInfoStatus("infoGas", fahrzeugID);
                String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "_Kommentar.pdf";
                PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Ger\u00e4tewarte wurden per E-Mail \u00fcber einen anstegenden Gaswartung informiert.\n\nDer Termin ist:\n " + termin);
                Utils.dateiKatalogisieren(dateiname);
                logbuchEingabe.NeuerEintag("Ger\u00e4tewarte wurden per E-Mail \u00fcber einen anstegenden Gaswartung informiert.\n\nDer Termin ist:\n " + termin);
            }
        }
        catch (Exception e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void EMailInformationServiceM\u00e4ngelmeldung(final M\u00e4ngelmeldung mangel) {
        Thread threadVersandt = new Thread(){

            @Override
            public void run() {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
                    TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
                    Ausgang ausgang = new Ausgang();
                    logging.logInfo((Object)("Ger\u00e4tewarte werden \u00fcber M\u00e4ngelmeldung informiert: Mangel-ID" + mangel.getId()));
                    String betreff = "Neue M\u00e4ngelmeldung - Mangel-ID" + mangel.getId();
                    String nachricht = "Hallo Ger\u00e4tewart,\n\nEs wurde eine M\u00e4ngelmeldung eingetragen:\n\nFahrzeug: " + tabFahrzeug.getFahrzeugName(mangel.getFahrzeugID()) + "\nMeldender: " + tabMitglied.getName(mangel.getMitgliedID()) + ", " + tabMitglied.getVorname(mangel.getMitgliedID()) + "\nWann trat der Mangel auf: " + mangel.getWann() + "\n\nBeschreibung:\n" + mangel.getBeschreibung() + "\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur");
                    ausgang.setAn(EMailService.setGer\u00e4tewarte(tabMitglied));
                    ausgang.setCc("");
                    ausgang.setBcc(EMailService.setEinheitsf\u00fchrungInBCC(tabMitglied));
                    ausgang.setBetreff(betreff);
                    ausgang.setNachricht(nachricht);
                    ausgang.setAnhang(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/mangel/" + mangel.getDateiname() + ",");
                    ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    tabAusgang.insert(ausgang);
                }
                catch (Exception e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadVersandt.start();
    }

    public static void EMailInformationServiceEinsatzkomponente(final Einsatz einsatz, final StatistikEinsatz statistik) {
        Thread threadVersandt = new Thread(){

            @Override
            public void run() {
                try {
                    TabelleMitglied tabMitglied = new TabelleMitglied();
                    TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
                    TabelleStichwort tabStichwort = new TabelleStichwort();
                    Ausgang ausgang = new Ausgang();
                    TabelleEinsatz_organisationen tabEinsatz_organisation = new TabelleEinsatz_organisationen();
                    logging.logInfo((Object)("Homepageteam wird \u00fcber neuen Einsatz Informiert: " + einsatz.getId()));
                    String betreff = runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible").equals("0") ? "Freizugebender Einsatz f\u00fcr die Homepage" : "Neuer Einsatz f\u00fcr die Homepage";
                    StringBuilder nachricht = new StringBuilder();
                    if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible").equals("0")) {
                        nachricht.append("Es wurde \u00fcber das FeuerwehrManagementSystem ein Einsatz eingetragen, der auf der Homepage ver\u00f6ffentlicht werden kann:\n");
                    } else {
                        nachricht.append("Es wurde \u00fcber das FeuerwehrManagementSystem ein Einsatz eingetragen, der auf der Homepage ver\u00f6ffentlicht wurde:\n");
                    }
                    nachricht.append("\nFeuerwehrManagementSystem - VeranstaltungID: " + einsatz.getVeranstaltungID());
                    nachricht.append("\nEinsatznummer: " + einsatz.getEinsatznummerOffiziell());
                    nachricht.append("\nEinsatzzahl: " + einsatz.getEinsatznummer() + " / " + SbcUtils.timeStamp((String)"yyyy"));
                    if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort").equals("1")) {
                        nachricht.append("\nSichtwort: " + tabStichwort.getStichwortName(einsatz.getStichwort()));
                    } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort").equals("2")) {
                        nachricht.append("\nKategorie: " + new TabelleEinsatz_kategorie().getEinsatzKategorieName(statistik.getKategorie()));
                    } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort").equals("3")) {
                        nachricht.append("\nKategorie / Stichwort: " + new TabelleEinsatz_kategorie().getEinsatzKategorieName(statistik.getKategorie()) + " / " + tabStichwort.getStichwortName(einsatz.getStichwort()));
                    }
                    nachricht.append("\nFahrzeuge: " + einsatz.getFahrzeug());
                    if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("1")) {
                        nachricht.append("\nOrt / Stra\u00dfe: " + einsatz.getOrt());
                    } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("2")) {
                        nachricht.append("\nOrt / Stra\u00dfe: " + einsatz.getOrt().replaceAll("[0-9]", ""));
                    } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("3")) {
                        nachricht.append("\nOrt / Stra\u00dfe: " + einsatz.getOrt().replaceAll("[0-9]", "") + " / " + einsatz.getStadtteil());
                    } else if (runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig").equals("4")) {
                        nachricht.append("\nOrt / Stra\u00dfe: " + einsatz.getOrt() + " / " + einsatz.getStadtteil());
                    }
                    nachricht.append("\nDatum: " + TimeCalculation.parseDateForGUI(einsatz.getDatum()));
                    nachricht.append("\nAlarmierung: " + einsatz.getZeitAlarm());
                    if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteNurAlamierung\u00dcbertragen").equals("0")) {
                        nachricht.append("\nAusr\u00fccken: " + einsatz.getZeitAusgerueckt());
                        nachricht.append("\nEinsatzende: " + einsatz.getZeitEingerueckt());
                    }
                    if (runApplication.EINSTELLUNGEN.get("WeitereOrganisationen").equals("1")) {
                        nachricht.append("\nOrganisationen: " + tabEinsatz_organisation.getOrganisationNameKommaSeperated(einsatz.getVeranstaltungID()));
                    }
                    nachricht.append("\nBeschreibung: " + einsatz.getBeschreibung());
                    nachricht.append("\n\n");
                    nachricht.append("Link zur Homepage:");
                    nachricht.append("\n" + runApplication.EINSTELLUNGEN.get("JoomlaLink"));
                    if (runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible").equals("0")) {
                        nachricht.append("\n\n");
                        nachricht.append("Link zur direkten Ver\u00f6ffentlichung:");
                        nachricht.append("\n" + runApplication.EINSTELLUNGEN.get("JoomlaLink") + runApplication.EINSTELLUNGEN.get("Joomla_com_Einsatz_Freischalten") + "?id=" + einsatz.getVeranstaltungID());
                    }
                    nachricht.append("\n\n\n" + runApplication.EINSTELLUNGEN.get("emailSignatur"));
                    StringBuilder buildEmpf\u00e4nger = new StringBuilder();
                    if (!runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1").equals("0")) {
                        buildEmpf\u00e4nger.append(tabMitglied.getEMail(Integer.parseInt(runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1"))));
                        buildEmpf\u00e4nger.append(", ");
                    }
                    if (!runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2").equals("0")) {
                        buildEmpf\u00e4nger.append(tabMitglied.getEMail(Integer.parseInt(runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2"))));
                        buildEmpf\u00e4nger.append(", ");
                    }
                    if (!runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3").equals("0")) {
                        buildEmpf\u00e4nger.append(tabMitglied.getEMail(Integer.parseInt(runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3"))));
                        buildEmpf\u00e4nger.append(", ");
                    }
                    ausgang.setAn(buildEmpf\u00e4nger.toString());
                    ausgang.setCc("");
                    ausgang.setBcc("");
                    ausgang.setBetreff(betreff);
                    ausgang.setNachricht(nachricht.toString());
                    ausgang.setAnhang("");
                    ausgang.setDate(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    tabAusgang.insert(ausgang);
                    SendePostausgang.sendAusgang();
                }
                catch (Exception e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadVersandt.start();
    }

    private static String setEinheitsf\u00fchrungInBCC(TabelleMitglied tabMitglied) throws SQLException {
        String bcc;
        if (runApplication.EINSTELLUNGEN.get("untersuchungViaEMailChefBCC").equals("1") && runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsf\u00fchrungAktiviert").equals("0")) {
            String[] chefListe = Utils.listToArray(tabMitglied.getEinheitsf\u00fchrerMail());
            StringBuilder build = new StringBuilder();
            int i = 0;
            while (i < chefListe.length) {
                build.append(chefListe[i]);
                build.append(", ");
                ++i;
            }
            bcc = build.toString().substring(0, build.toString().length() - 1);
        } else {
            bcc = runApplication.EINSTELLUNGEN.get("untersuchungViaEMailChefBCC").equals("1") && runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsf\u00fchrungAktiviert").equals("1") ? runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsf\u00fchrung") : "";
        }
        return bcc;
    }

    private static String setGer\u00e4tewarte(TabelleMitglied tabMitglied) throws SQLException {
        StringBuilder buildGeraetewareteEMail = new StringBuilder();
        if (runApplication.EINSTELLUNGEN.get("globaleEMailGer\u00e4tewarteAktiviert").equals("0")) {
            String[] eMailGeraetewarte = Utils.listToArray(tabMitglied.getGeraetewarteMail());
            int i = 0;
            while (i < eMailGeraetewarte.length) {
                buildGeraetewareteEMail.append(eMailGeraetewarte[i]);
                buildGeraetewareteEMail.append(", ");
                ++i;
            }
        } else {
            buildGeraetewareteEMail.append(runApplication.EINSTELLUNGEN.get("globaleEMailGer\u00e4tewarte"));
        }
        return buildGeraetewareteEMail.toString();
    }
}

