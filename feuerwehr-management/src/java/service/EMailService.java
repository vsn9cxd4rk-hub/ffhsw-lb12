package service;

import data.tabellen.TabelleEinsatz_kategorie;
import data.tabellen.TabelleEinsatz_organisationen;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleEinstellungen_gespeichert;
import data.tabellen.email.TabelleEMail_ausgang;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.fahrzeug.TabelleFahrzeug_untersuchung;
import data.tabellen.fahrzeug.TabelleGeraetepruefung;
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import go.Einsatz;
import go.Mängelmeldung;
import go.StatistikEinsatz;
import go.Veranstaltung;
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
         TabelleMitglied e = new TabelleMitglied();
         TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         int mID = e.getIdByGuiString(name);
         if(tabUntersuchung.getInfoStatusG26(mID) == 0 && e.getEMailDeaktivStatus(mID) == 0) {
            logging.logInfo("Miglied " + name + " wird über anstehende Untersuchung informiert");
            String eMailMitglied = e.getEMail(mID);
            String betreff = "Anstehende " + untersuchung + " für " + name;
            String nachricht = "Hallo " + e.getVorname(mID) + ",\n\nDeine " + untersuchung + " - Untersuchung ist im Monat: " + datum + " fällig.\n\nBitte vereinbare einen Termin für die anstehende " + untersuchung + " - Untersuchung!\nDie Kontaktdaten kannst du von uns erhalten." + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            if(eMailMitglied.equals("")) {
               return;
            }

            ausgang.setAn(eMailMitglied);
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(e));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            tabUntersuchung.updateInfoG26(mID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
         }
      } catch (Exception var12) {
         logging.logPrintStackTrace(var12);
      }

   }

   public static void EMailInformationServiceG25(String name, String untersuchung, String datum) {
      try {
         TabelleMitglied e = new TabelleMitglied();
         TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         int mID = e.getIdByGuiString(name);
         if(tabUntersuchung.getInfoStatusG25(mID) == 0 && e.getEMailDeaktivStatus(mID) == 0) {
            logging.logInfo("Miglied " + name + " wird über anstehende Untersuchung informiert");
            String eMailMitglied = e.getEMail(mID);
            String betreff = "Anstehende " + untersuchung + " für " + name;
            String nachricht = "Hallo " + e.getVorname(mID) + ",\n\nDeine " + untersuchung + " - Untersuchung ist im Monat: " + datum + " fällig.\n\nBitte vereinbare einen Termin für die anstehende " + untersuchung + " - Untersuchung!\nDie Kontaktdaten kannst du von uns erhalten." + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            if(eMailMitglied.equals("")) {
               return;
            }

            ausgang.setAn(eMailMitglied);
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(e));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            tabUntersuchung.updateInfoG25(mID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
         }
      } catch (Exception var12) {
         logging.logPrintStackTrace(var12);
      }

   }

   public static void EMailInformationServiceG30(String name, String untersuchung, String datum) {
      try {
         TabelleMitglied e = new TabelleMitglied();
         TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         int mID = e.getIdByGuiString(name);
         if(tabUntersuchung.getInfoStatusG30(mID) == 0 && e.getEMailDeaktivStatus(mID) == 0) {
            logging.logInfo("Miglied " + name + " wird über anstehende Untersuchung informiert");
            String eMailMitglied = e.getEMail(mID);
            String betreff = "Anstehende " + untersuchung + " für " + name;
            String nachricht = "Hallo " + e.getVorname(mID) + ",\n\nDeine " + untersuchung + " - Untersuchung ist im Monat: " + datum + " fällig.\n\nBitte vereinbare einen Termin für die anstehende " + untersuchung + " - Untersuchung!\nDie Kontaktdaten kannst du von uns erhalten." + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            if(eMailMitglied.equals("")) {
               return;
            }

            ausgang.setAn(eMailMitglied);
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(e));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            tabUntersuchung.updateInfoG30(mID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
         }
      } catch (Exception var12) {
         logging.logPrintStackTrace(var12);
      }

   }

   public static void EMailInformationServiceAblaufLKW(String name, String datum) {
      try {
         TabelleMitglied e = new TabelleMitglied();
         TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         int mID = e.getIdByGuiString(name);
         if(tabUntersuchung.getInfoStatusAblaufLKW(mID) == 0 && e.getEMailDeaktivStatus(mID) == 0) {
            logging.logInfo("Miglied " + name + " wird über anstehende Untersuchung informiert");
            String eMailMitglied = e.getEMail(mID);
            String betreff = "Anstehende verlängerung des LKW Führerscheins für " + name;
            String nachricht = "Hallo " + e.getVorname(mID) + ",\n\nDein LKW Führeschein (Führerschein Klasse C) läuft im Monat " + datum + " ab.\n\nBitte lasse deinen Führerschein beim Straßenverkehrsamt verlängern!" + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            if(eMailMitglied.equals("")) {
               return;
            }

            ausgang.setAn(eMailMitglied);
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(e));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            tabUntersuchung.updateInfoAblaufLKW(mID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
         }
      } catch (Exception var11) {
         logging.logPrintStackTrace(var11);
      }

   }

   public static void EMailInformationServiceAblaufDienstausweis(String name, String datum) {
      try {
         TabelleMitglied e = new TabelleMitglied();
         TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         int mID = e.getIdByGuiString(name);
         if(tabUntersuchung.getInfoStatusAblaufDienstausweis(mID) == 0 && e.getEMailDeaktivStatus(mID) == 0) {
            logging.logInfo("Mitglied " + name + " wird über Ablauf des Dienstausweises informiert");
            String eMailMitglied = e.getEMail(mID);
            String betreff = "Ablaufender Dienstausweis für " + name;
            String nachricht = "Hallo " + e.getVorname(mID) + ",\n\nDeine Dienstausweis" + " - läuft im Monat: " + datum + " ab.\n\nBitte beantrage einen neuen Dienstausweis!" + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            if(eMailMitglied.equals("")) {
               return;
            }

            ausgang.setAn(eMailMitglied);
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(e));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            tabUntersuchung.updateInfoAblaufDienstausweis(mID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
         }
      } catch (Exception var11) {
         logging.logPrintStackTrace(var11);
      }

   }

   public static void EMailInformationServiceAblaufDerFahberechtigung(String name, String datum) {
      try {
         TabelleMitglied e = new TabelleMitglied();
         TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         int mID = e.getIdByGuiString(name);
         if(tabUntersuchung.getInfoPruefungDerFahrberechtigung(mID) == 0 && e.getEMailDeaktivStatus(mID) == 0) {
            logging.logInfo("Mitglied " + name + " wird über Ablauf der Fahrberechtigung informiert");
            String eMailMitglied = e.getEMail(mID);
            String betreff = "Ablaufende Fahrberechtigung für " + name;
            String nachricht = "Hallo " + e.getVorname(mID) + ",\n\nDeine Fahrberechtigung zum Führen von Einsatzfahrzeugen" + " - läuft im Monat: " + datum + " ab.\n\nBitte beantrage eine neue Fahrberechtigung!" + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            if(eMailMitglied.equals("")) {
               return;
            }

            ausgang.setAn(eMailMitglied);
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(e));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            tabUntersuchung.updateInfoAblaufDienstausweis(mID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Mitgliederakte/" + mID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFMitgliederKommentar.PDFdocumentErstellen(dateiname, name, nachricht);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Mitglied " + name + " wurde mit folgender Nachricht Informiert: " + nachricht);
         }
      } catch (Exception var11) {
         logging.logPrintStackTrace(var11);
      }

   }

   public static void EMailInformationServiceUntersuchungGlobaleEMailAdressen(String liste, String untersuchung) {
      try {
         TabelleEinstellungen_gespeichert e = new TabelleEinstellungen_gespeichert();
         TabelleMitglied tabMitglied = new TabelleMitglied();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         if(!e.getEinstellungen(untersuchung).equals(SbcUtils.timeStamp("MM.yyyy"))) {
            logging.logInfo("Sende EMail: " + untersuchung + " an Globale EMail Adresse");
            String betreff = "Anstehende " + untersuchung;
            String nachricht = "";
            if(untersuchung.equals("Dienstausweis")) {
               nachricht = "Hallo,\n\nFolgende Kameraden benötigen in der nächsten Zeit einen neuen Dienstausweis! \n\n" + liste + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            } else if(untersuchung.equals("Fahrberechtigung")) {
               nachricht = "Hallo,\n\nFolgende Kameraden benötigen in der nächsten Zeit einen neue Fahrberechtigung! \n\n" + liste + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            } else {
               nachricht = "Hallo,\n\nFolgende Kameraden müssen in der nächsten Zeit zur " + untersuchung + " Untersuchung!\n\n" + liste + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            }

            if(untersuchung.equals("G25")) {
               ausgang.setAn((String)runApplication.EINSTELLUNGEN.get("globaleEMailG25"));
            } else if(untersuchung.equals("G26") | untersuchung.equals("G30")) {
               ausgang.setAn((String)runApplication.EINSTELLUNGEN.get("globaleEMailG26"));
            } else if(untersuchung.equals("Fahrberechtigung")) {
               ausgang.setAn((String)runApplication.EINSTELLUNGEN.get("globaleEMailFahrberechtigung"));
            } else if(untersuchung.equals("Dienstausweis")) {
               ausgang.setAn((String)runApplication.EINSTELLUNGEN.get("globaleEMailDienstausweis"));
            }

            if(ausgang.getAn().equals("")) {
               logging.logInfo("Globale E-Mail Adresse für " + untersuchung + " ist leer!");
               return;
            }

            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(tabMitglied));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            e.update(untersuchung, SbcUtils.timeStamp("MM.yyyy"));
            logging.logInfo("Speichere das senden an Globale E-Mail für " + untersuchung + " / " + SbcUtils.timeStamp("MM.yyyy"));
            logbuchEingabe.NeuerEintag("Speichere das senden an Globale E-Mail für " + untersuchung + " / " + SbcUtils.timeStamp("MM.yyyy"));
         }
      } catch (Exception var8) {
         logging.logPrintStackTrace(var8);
      }

   }

   public static void EMailVersandtServiceAnstehendeVeranstaltungen() {
      try {
         if(((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMail")).equals("1")) {
            logging.logInfo("Starte: EMailService.EMailVersandtServiceAnstehendeVeranstaltungen()");
            TabelleMitglied e = new TabelleMitglied();
            TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            Ausgang ausgang = new Ausgang();
            String nameMonat = SbcUtils.timeStamp("MMMM");
            String jahr = SbcUtils.timeStamp("yyyy");
            String monat = SbcUtils.timeStamp("MM");
            logging.logInfo("Bereite versenden von Terminen vor... Jahr: " + jahr + " , Monat: " + monat);
            if(((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMailConfig")).equals("2") && tabVeranstaltung.getCountVeranstaltungWithoutInfoVersandtInDiesemMonat(jahr + "-" + monat + "-01", jahr + "-" + monat + "-31", 1) != 0) {
               logging.logInfo("Termine sind bereits versendet für diesen Monat (EINSTELLUNGEN: terminVersandtViaEMailConfig == 2)");
               return;
            }

            if(tabVeranstaltung.getCountVeranstaltungWithoutInfoVersandtInDiesemMonat(jahr + "-" + monat + "-01", jahr + "-" + monat + "-31", 0) != 0) {
               logging.logInfo("Habe Termine zu versenden gefunden");
               String[] mitgliederEMailListe = Utils.listToArray(e.getAlleMailAdressenGruppe1());
               String[] veranstaltungsliste = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungWithoutInfoVersandtInDiesemMonat(jahr + "-" + monat + "-01", jahr + "-" + monat + "-31", false));
               String betreff = (String)runApplication.EINSTELLUNGEN.get("Name") + " - Termine " + nameMonat + " " + jahr;
               StringBuilder nachricht = new StringBuilder();
               nachricht.append("In dieser E-Mail könnt Ihr eine Übersicht der aktuellen Termine für " + nameMonat + " " + jahr + " finden.");
               nachricht.append("\n");
               nachricht.append("\n");

               for(int build = 0; build < veranstaltungsliste.length; ++build) {
                  nachricht.append(veranstaltungsliste[build]);
               }

               if(((String)runApplication.EINSTELLUNGEN.get("terminVersandtViaEMailFolgeMonat")).equals("1")) {
                  logging.logInfo("Füge Termine für den nächsten Monat zur E-Mail hinzu...");
                  String var16 = TimeCalculation.calculateNextMonth(monat);
                  String i = jahr;
                  if(var16.equals("01")) {
                     i = Integer.toString(Integer.parseInt(jahr) + 1);
                  }

                  logging.logInfo("Folgemonat: " + i + "-" + var16);
                  String[] veranstaltungslisteFolgeMonat = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungWithoutInfoVersandtInDiesemMonat(i + "-" + var16 + "-01", i + "-" + var16 + "-31", false));
                  nachricht.append("#### WEITERE TERMINE ####\nZur Vorplanung Termine für den nächsten Monat:");
                  nachricht.append("\n");
                  nachricht.append("\n");

                  for(int fmt = 0; fmt < veranstaltungslisteFolgeMonat.length; ++fmt) {
                     nachricht.append(veranstaltungslisteFolgeMonat[fmt]);
                  }
               }

               nachricht.append("\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
               ausgang.setAn((String)runApplication.EINSTELLUNGEN.get("emailAdresse"));
               ausgang.setCc("");
               StringBuilder var17 = new StringBuilder();

               for(int var18 = 0; var18 < mitgliederEMailListe.length; ++var18) {
                  var17.append(mitgliederEMailListe[var18]);
                  var17.append(", ");
               }

               ausgang.setBcc(var17.toString().substring(0, var17.toString().length() - 1));
               ausgang.setBetreff(betreff);
               ausgang.setNachricht(nachricht.toString());
               ausgang.setAnhang("");
               ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
               tabAusgang.insert(ausgang);
               tabVeranstaltung.updateInfoVersandt(jahr + "-" + monat + "-01", jahr + "-" + monat + "-31");
               logging.logInfo("E-Mail für den Terminversand wurde erfolgreich in den Ausgangskorb gelegt");
            } else {
               logging.logInfo("Keine Termine zum senden gefunden");
            }
         }
      } catch (SQLException var15) {
         logging.logPrintStackTrace(var15);
      }

   }

   public static void EMailInformationServiceVeranstaltung(String empfaenger, String veranstaltungName) {
      try {
         TabelleEMail_ausgang e = new TabelleEMail_ausgang();
         TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
         Ausgang ausgang = new Ausgang();
         logging.logInfo("Miglieder der Veranstaltung " + veranstaltungName + " werden erneut per E-Mail benachrichtigt");
         int vID = tabVeranstaltung.getVeranstaltungID(veranstaltungName);
         String nachricht = "Hallo Kameraden,\n\nErinnerungsmail an die Veranstaltung: " + veranstaltungName + "\n\nDatum: " + TimeCalculation.parseDateForGUI(tabVeranstaltung.getDatum(vID)) + "\nVon: " + tabVeranstaltung.getZeitStart(vID) + "\nBis: " + tabVeranstaltung.getZeitEnde(vID) + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
         ausgang.setAn((String)runApplication.EINSTELLUNGEN.get("emailAdresse"));
         ausgang.setCc("");
         ausgang.setBcc(empfaenger);
         ausgang.setBetreff(veranstaltungName);
         ausgang.setNachricht(nachricht);
         ausgang.setAnhang("");
         ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
         e.insert(ausgang);
         logging.logInfo("E-Mail für den Terminversand wurde erfolgreich in den Ausgangskorb gelegt");
         SendePostausgang.sendAusgang();
         logging.logInfo("Sende Postausgang");
      } catch (Exception var7) {
         logging.logPrintStackTrace(var7);
      }

   }

   public static void EMailInformationServiceGeräteprüfungen(String text) {
      try {
         TabelleGeraetepruefung e = new TabelleGeraetepruefung();
         TabelleMitglied tabMitglied = new TabelleMitglied();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         if(e.getInfoEMail() == 0) {
            logging.logInfo("Gerätewarte werden über anstehende Geräteprüfungen informiert");
            String betreff = "Anstehende Geräteprüfungen";
            String nachricht = "Es sind Geräteprüfungen fällig:\n\n" + text + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            ausgang.setAn(setGerätewarte(tabMitglied));
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(tabMitglied));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            e.updateInfoEMail(1);
         }
      } catch (Exception var7) {
         logging.logPrintStackTrace(var7);
      }

   }

   public static void EMailInformationServiceFahrzeugTuev(int fahrzeugID, String fahrzeugName, String termin) {
      try {
         TabelleFahrzeug_untersuchung e = new TabelleFahrzeug_untersuchung();
         TabelleMitglied tabMitglied = new TabelleMitglied();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         if(e.getInfoTuevStatus(fahrzeugID) == 0) {
            logging.logInfo("Gerätewarte werden über anstehende Tüv informiert: Fahrzeug " + fahrzeugName);
            String betreff = "Anstehender Tüv für Fahrzeug " + fahrzeugName;
            String nachricht = "Hallo Gerätewart,\n\nder TÜV für das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " fällig.\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            ausgang.setAn(setGerätewarte(tabMitglied));
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(tabMitglied));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            e.updateInfoStatus("infoTuev", fahrzeugID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Gerätewarte wurden per E-Mail Über den anstegenden Tüv informiert.\n\nDer Termin ist:\n " + termin);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Gerätewarte wurden per E-Mail Über den anstegenden Tüv informiert.\n\nDer Termin ist:\n " + termin);
         }
      } catch (Exception var10) {
         logging.logPrintStackTrace(var10);
      }

   }

   public static void EMailInformationServiceFahrzeugSP(int fahrzeugID, String fahrzeugName, String termin) {
      try {
         TabelleFahrzeug_untersuchung e = new TabelleFahrzeug_untersuchung();
         TabelleMitglied tabMitglied = new TabelleMitglied();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         if(e.getInfoSPStatus(fahrzeugID) == 0) {
            logging.logInfo("Gerätewarte werden über anstehende SP informiert: Fahrzeug " + fahrzeugName);
            String betreff = "Anstehende Sicherheitsprüfung für das Fahrzeug " + fahrzeugName;
            String nachricht = "Hallo Gerätewart,\n\ndie Sicherheitsprüfung für das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " fällig.\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            ausgang.setAn(setGerätewarte(tabMitglied));
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(tabMitglied));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            e.updateInfoStatus("infoSP", fahrzeugID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Gerätewarte wurden per E-Mail über die anstegende Sicherheitsprüfung informiert.\n\nDer Termin ist:\n " + termin);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Gerätewarte wurden per E-Mail über die anstegende Sicherheitsprüfung informiert.\n\nDer Termin ist:\n " + termin);
         }
      } catch (Exception var10) {
         logging.logPrintStackTrace(var10);
      }

   }

   public static void EMailInformationServiceFahrzeugService(int fahrzeugID, String fahrzeugName, String termin) {
      try {
         TabelleFahrzeug_untersuchung e = new TabelleFahrzeug_untersuchung();
         TabelleMitglied tabMitglied = new TabelleMitglied();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         if(e.getInfoServiceStatus(fahrzeugID) == 0) {
            logging.logInfo("Gerätewarte werden über anstehenden Service informiert: Fahrzeug " + fahrzeugName);
            String betreff = "Anstehender Service für Fahrzeug " + fahrzeugName;
            String nachricht = "Hallo Gerätewart,\n\nder Service für das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " fällig.\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            ausgang.setAn(setGerätewarte(tabMitglied));
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(tabMitglied));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            e.updateInfoStatus("infoService", fahrzeugID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Gerätewarte wurden per E-Mail über einen anstegenden Service informiert.\n\nDer Termin ist:\n " + termin);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Gerätewarte wurden per E-Mail über einen anstegenden Service informiert.\n\nDer Termin ist:\n " + termin);
         }
      } catch (Exception var10) {
         logging.logPrintStackTrace(var10);
      }

   }

   public static void EMailInformationServiceGaswartung(int fahrzeugID, String fahrzeugName, String termin) {
      try {
         TabelleFahrzeug_untersuchung e = new TabelleFahrzeug_untersuchung();
         TabelleMitglied tabMitglied = new TabelleMitglied();
         TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
         Ausgang ausgang = new Ausgang();
         if(e.getInfoGasStatus(fahrzeugID) == 0) {
            logging.logInfo("Gerätewarte werden über anstehenden Gaswartung informiert: Fahrzeug " + fahrzeugName);
            String betreff = "Anstehender Gaswartung für Fahrzeug " + fahrzeugName;
            String nachricht = "Hallo Gerätewart,\n\ndie Gaswartung für das Fahrzeug " + fahrzeugName + " ist im Monat " + termin + " fällig.\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
            ausgang.setAn(setGerätewarte(tabMitglied));
            ausgang.setCc("");
            ausgang.setBcc(setEinheitsführungInBCC(tabMitglied));
            ausgang.setBetreff(betreff);
            ausgang.setNachricht(nachricht);
            ausgang.setAnhang("");
            ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
            tabAusgang.insert(ausgang);
            e.updateInfoStatus("infoGas", fahrzeugID);
            String dateiname = runApplication.arbeitsverzeichnis + "data/Fahrzeugakte/" + fahrzeugID + "/" + SbcUtils.timeStamp("yyyy-MM-dd") + "_Kommentar.pdf";
            PDFFahrzeugKommentar.PDFdocumentErstellen(dateiname, fahrzeugName, "Gerätewarte wurden per E-Mail über einen anstegenden Gaswartung informiert.\n\nDer Termin ist:\n " + termin);
            Utils.dateiKatalogisieren(dateiname);
            logbuchEingabe.NeuerEintag("Gerätewarte wurden per E-Mail über einen anstegenden Gaswartung informiert.\n\nDer Termin ist:\n " + termin);
         }
      } catch (Exception var10) {
         logging.logPrintStackTrace(var10);
      }

   }

   public static void EMailInformationServiceMängelmeldung(final Mängelmeldung mangel) {
      Thread threadVersandt = new Thread() {
         public void run() {
            try {
               TabelleMitglied e = new TabelleMitglied();
               TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
               TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
               Ausgang ausgang = new Ausgang();
               logging.logInfo("Gerätewarte werden über Mängelmeldung informiert: Mangel-ID" + mangel.getId());
               String betreff = "Neue Mängelmeldung - Mangel-ID" + mangel.getId();
               String nachricht = "Hallo Gerätewart,\n\nEs wurde eine Mängelmeldung eingetragen:\n\nFahrzeug: " + tabFahrzeug.getFahrzeugName(mangel.getFahrzeugID()) + "\nMeldender: " + e.getName(mangel.getMitgliedID()) + ", " + e.getVorname(mangel.getMitgliedID()) + "\nWann trat der Mangel auf: " + mangel.getWann() + "\n\nBeschreibung:\n" + mangel.getBeschreibung() + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
               ausgang.setAn(EMailService.setGerätewarte(e));
               ausgang.setCc("");
               ausgang.setBcc(EMailService.setEinheitsführungInBCC(e));
               ausgang.setBetreff(betreff);
               ausgang.setNachricht(nachricht);
               ausgang.setAnhang(runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/Mangel/" + mangel.getDateiname() + ",");
               ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
               tabAusgang.insert(ausgang);
            } catch (Exception var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      };
      threadVersandt.start();
   }

   public static void EMailInformationServiceEinsatzkomponente(final Einsatz einsatz, final StatistikEinsatz statistik) {
      Thread threadVersandt = new Thread() {
         public void run() {
            try {
               TabelleMitglied e = new TabelleMitglied();
               TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
               TabelleStichwort tabStichwort = new TabelleStichwort();
               Ausgang ausgang = new Ausgang();
               TabelleEinsatz_organisationen tabEinsatz_organisation = new TabelleEinsatz_organisationen();
               logging.logInfo("Homepageteam wird über neuen Einsatz Informiert: " + einsatz.getId());
               String betreff;
               if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible")).equals("0")) {
                  betreff = "Freizugebender Einsatz für die Homepage";
               } else {
                  betreff = "Neuer Einsatz für die Homepage";
               }

               StringBuilder nachricht = new StringBuilder();
               if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible")).equals("0")) {
                  nachricht.append("Es wurde über das FeuerwehrManagementSystem ein Einsatz eingetragen, der auf der Homepage veröffentlicht werden kann:\n");
               } else {
                  nachricht.append("Es wurde über das FeuerwehrManagementSystem ein Einsatz eingetragen, der auf der Homepage veröffentlicht wurde:\n");
               }

               nachricht.append("\nFeuerwehrManagementSystem - VeranstaltungID: " + einsatz.getVeranstaltungID());
               nachricht.append("\nEinsatznummer: " + einsatz.getEinsatznummerOffiziell());
               nachricht.append("\nEinsatzzahl: " + einsatz.getEinsatznummer() + " / " + SbcUtils.timeStamp("yyyy"));
               if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort")).equals("1")) {
                  nachricht.append("\nSichtwort: " + tabStichwort.getStichwortName(einsatz.getStichwort()));
               } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort")).equals("2")) {
                  nachricht.append("\nKategorie: " + (new TabelleEinsatz_kategorie()).getEinsatzKategorieName(statistik.getKategorie()));
               } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteStichwort")).equals("3")) {
                  nachricht.append("\nKategorie / Stichwort: " + (new TabelleEinsatz_kategorie()).getEinsatzKategorieName(statistik.getKategorie()) + " / " + tabStichwort.getStichwortName(einsatz.getStichwort()));
               }

               nachricht.append("\nFahrzeuge: " + einsatz.getFahrzeug());
               if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("1")) {
                  nachricht.append("\nOrt / Straße: " + einsatz.getOrt());
               } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("2")) {
                  nachricht.append("\nOrt / Straße: " + einsatz.getOrt().replaceAll("[0-9]", ""));
               } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("3")) {
                  nachricht.append("\nOrt / Straße: " + einsatz.getOrt().replaceAll("[0-9]", "") + " / " + einsatz.getStadtteil());
               } else if(((String)runApplication.EINSTELLUNGEN.get("joomlaEinsatzkomponenteConfig")).equals("4")) {
                  nachricht.append("\nOrt / Straße: " + einsatz.getOrt() + " / " + einsatz.getStadtteil());
               }

               nachricht.append("\nDatum: " + TimeCalculation.parseDateForGUI(einsatz.getDatum()));
               nachricht.append("\nAlarmierung: " + einsatz.getZeitAlarm());
               if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzKomponenteNurAlamierungÜbertragen")).equals("0")) {
                  nachricht.append("\nAusrücken: " + einsatz.getZeitAusgerueckt());
                  nachricht.append("\nEinsatzende: " + einsatz.getZeitEingerueckt());
               }

               if(((String)runApplication.EINSTELLUNGEN.get("WeitereOrganisationen")).equals("1")) {
                  nachricht.append("\nOrganisationen: " + tabEinsatz_organisation.getOrganisationNameKommaSeperated(einsatz.getVeranstaltungID()));
               }

               nachricht.append("\nBeschreibung: " + einsatz.getBeschreibung());
               nachricht.append("\n\n");
               nachricht.append("Link zur Homepage:");
               nachricht.append("\n" + (String)runApplication.EINSTELLUNGEN.get("JoomlaLink"));
               if(((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteVisible")).equals("0")) {
                  nachricht.append("\n\n");
                  nachricht.append("Link zur direkten Veröffentlichung:");
                  nachricht.append("\n" + (String)runApplication.EINSTELLUNGEN.get("JoomlaLink") + (String)runApplication.EINSTELLUNGEN.get("Joomla_com_Einsatz_Freischalten") + "?id=" + einsatz.getVeranstaltungID());
               }

               nachricht.append("\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur"));
               StringBuilder buildEmpfänger = new StringBuilder();
               if(!((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1")).equals("0")) {
                  buildEmpfänger.append(e.getEMail(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1"))));
                  buildEmpfänger.append(", ");
               }

               if(!((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2")).equals("0")) {
                  buildEmpfänger.append(e.getEMail(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2"))));
                  buildEmpfänger.append(", ");
               }

               if(!((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3")).equals("0")) {
                  buildEmpfänger.append(e.getEMail(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3"))));
                  buildEmpfänger.append(", ");
               }

               ausgang.setAn(buildEmpfänger.toString());
               ausgang.setCc("");
               ausgang.setBcc("");
               ausgang.setBetreff(betreff);
               ausgang.setNachricht(nachricht.toString());
               ausgang.setAnhang("");
               ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
               tabAusgang.insert(ausgang);
               SendePostausgang.sendAusgang();
            } catch (Exception var9) {
               logging.logPrintStackTrace(var9);
            }

         }
      };
      threadVersandt.start();
   }

   public static void EMailInformationServiceFacebook(final String fbMessage, final Veranstaltung veranstaltung) {
      Thread threadVersandt = new Thread() {
         public void run() {
            try {
               TabelleMitglied e = new TabelleMitglied();
               TabelleEMail_ausgang tabAusgang = new TabelleEMail_ausgang();
               Ausgang ausgang = new Ausgang();
               logging.logInfo("Information über neuen Facebook Post...");
               String betreff = "Facebook-Seite - Neuer Eintrag ";
               String nachricht = "Es wurde folgender Eintrag auf der Facebook Seite von " + (String)runApplication.EINSTELLUNGEN.get("Name") + " erzeugt:\n\n" + "FeuerwehrManagementSystem" + " - VeranstaltungID: " + veranstaltung.getId() + "\n" + "Veranstaltung: " + veranstaltung.getName() + "\n\n" + "+++ HINWEIS +++ \n" + "Ablaufdatum FB-AccessToken:\n" + (String)runApplication.EINSTELLUNGEN_GESPEICHERT.get("facebookAccessTokenExpiereDate") + "\n\n" + "+++ FACEBOOK - NACHRICHT / POST +++ \n\n" + fbMessage + "\n\n\n" + (String)runApplication.EINSTELLUNGEN.get("emailSignatur");
               StringBuilder buildEmpfänger = new StringBuilder();
               if(!((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1")).equals("0")) {
                  buildEmpfänger.append(e.getEMail(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn1"))));
                  buildEmpfänger.append(", ");
               }

               if(!((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2")).equals("0")) {
                  buildEmpfänger.append(e.getEMail(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn2"))));
                  buildEmpfänger.append(", ");
               }

               if(!((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3")).equals("0")) {
                  buildEmpfänger.append(e.getEMail(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("JoomlaEinsatzkomponenteEMailAn3"))));
                  buildEmpfänger.append(", ");
               }

               ausgang.setAn(buildEmpfänger.toString());
               ausgang.setCc("");
               ausgang.setBcc("");
               ausgang.setBetreff(betreff);
               ausgang.setNachricht(nachricht.toString());
               ausgang.setAnhang("");
               ausgang.setDate(SbcUtils.timeStamp("yyyy-MM-dd"));
               tabAusgang.insert(ausgang);
               SendePostausgang.sendAusgang();
            } catch (Exception var7) {
               logging.logPrintStackTrace(var7);
            }

         }
      };
      threadVersandt.start();
   }

   private static String setEinheitsführungInBCC(TabelleMitglied tabMitglied) throws SQLException {
      String bcc;
      if(((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMailChefBCC")).equals("1") && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsführungAktiviert")).equals("0")) {
         String[] chefListe = Utils.listToArray(tabMitglied.getEinheitsführerMail());
         StringBuilder build = new StringBuilder();

         for(int i = 0; i < chefListe.length; ++i) {
            build.append(chefListe[i]);
            build.append(", ");
         }

         bcc = build.toString().substring(0, build.toString().length() - 1);
      } else if(((String)runApplication.EINSTELLUNGEN.get("untersuchungViaEMailChefBCC")).equals("1") && ((String)runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsführungAktiviert")).equals("1")) {
         bcc = (String)runApplication.EINSTELLUNGEN.get("globaleEMailEinheitsführung");
      } else {
         bcc = "";
      }

      return bcc;
   }

   private static String setGerätewarte(TabelleMitglied tabMitglied) throws SQLException {
      StringBuilder buildGeraetewareteEMail = new StringBuilder();
      if(((String)runApplication.EINSTELLUNGEN.get("globaleEMailGerätewarteAktiviert")).equals("0")) {
         String[] eMailGeraetewarte = Utils.listToArray(tabMitglied.getGeraetewarteMail());

         for(int i = 0; i < eMailGeraetewarte.length; ++i) {
            buildGeraetewareteEMail.append(eMailGeraetewarte[i]);
            buildGeraetewareteEMail.append(", ");
         }
      } else {
         buildGeraetewareteEMail.append((String)runApplication.EINSTELLUNGEN.get("globaleEMailGerätewarte"));
      }

      return buildGeraetewareteEMail.toString();
   }
}
