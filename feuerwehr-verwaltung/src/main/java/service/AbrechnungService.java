/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 */
package service;

import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleStatistikbsw;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.abrechnung.TabelleAbrechnung;
import data.tabellen.abrechnung.TabelleAbrechnung_artikel;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import data.tabellen.statistik.TabelleStatistikSonstigeVeranstaltung;
import go.abrechnung.Abrechnung;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Vector;
import logging.logging;
import pdfdocumente.abrechnung.PDFAbrechnung;
import run.runApplication;
import utilities.MoneyCalculation;
import utilities.Utils;
import utilities.logbuchEingabe;

public class AbrechnungService {
    public static void calculateAbrechnung(final int vID, final int kID, final int jahr) {
        Thread threadAbrechnung = new Thread(){

            @Override
            public void run() {
                TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
                TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
                TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
                Abrechnung abrechnung = new Abrechnung();
                try {
                    int[] anwesende = Utils.listToIntArray(tabAnwesenheit.getAnwesendeMitgliederIDByVeranstaltung(vID));
                    int artID = tabArtikel.getArtikelIDByKlasse(kID);
                    int wert = AbrechnungService.calculateAmount(vID, kID, artID);
                    int automatischerEinbehalt = tabArtikel.getArtikelRabattWert(artID);
                    int menge = wert / tabArtikel.getArtikelWert(artID);
                    int zahlungsart = tabArtikel.getZahlungsart(artID);
                    int buchungskonto = tabArtikel.getBuchungskontoID(artID);
                    int automatischerEinbehaltArt = tabArtikel.getArtikelrabattArt(artID);
                    String buchungstag = new TabelleVeranstaltung().getDatum(vID);
                    if (artID == 0) {
                        logging.logError((Object)"Es wurde kein Artikel gefunden, die Abrechnung wird Abgebrochen");
                        this.stop();
                    }
                    if (tabAbrechnung.getVeranstaltungsCount(vID) != 0) {
                        logging.logInfo((Object)"L\u00f6sche daten aus der Abrechnungstabelle");
                        tabAbrechnung.delete(vID);
                        if (tabAbrechnung.getVeranstaltungsCountMitAbrechnung(vID) != 0) {
                            try {
                                logging.logInfo((Object)"Die Veranstaltung wurde ge\u00e4ndert, jetzt muss umgebucht werden damit die Abrechnung stimmt");
                                int[] idList = Utils.listToIntArray(tabAbrechnung.getIDsByVeranstaltungForUmbuchung(vID));
                                int[] anwesendeBereitsAbgerechnete = Utils.listToIntArray(tabAbrechnung.getMitgliederIDByVeranstaltungID(vID));
                                int i = 0;
                                while (i < anwesendeBereitsAbgerechnete.length) {
                                    int umbuchungID = tabAbrechnung.getNextNummer();
                                    int wertUmbuchung = tabAbrechnung.getWertByID(idList[i]);
                                    int mengeUmbuchung = tabAbrechnung.getMengeByID(idList[i]);
                                    abrechnung.setId(umbuchungID);
                                    abrechnung.setAbrechnungID(0);
                                    abrechnung.setArtikelID(artID);
                                    abrechnung.setDatum(buchungstag);
                                    abrechnung.setJahr(jahr);
                                    abrechnung.setMitgliedID(anwesendeBereitsAbgerechnete[i]);
                                    abrechnung.setStatus(0);
                                    abrechnung.setVeranstaltungID(vID);
                                    abrechnung.setVeranstaltungKategorie(kID);
                                    abrechnung.setMenge(-mengeUmbuchung);
                                    abrechnung.setWert(-wertUmbuchung);
                                    abrechnung.setZahlungsart(3);
                                    abrechnung.setBuchungskonto(buchungskonto);
                                    abrechnung.setUmbuchungID(0);
                                    tabAbrechnung.insert(abrechnung);
                                    tabAbrechnung.updateUmbuchungID(idList[i], umbuchungID);
                                    ++i;
                                }
                                int[] idList2 = Utils.listToIntArray(tabAbrechnung.getIDsByVeranstaltungForUmbuchung2(vID));
                                AbrechnungService.rechneVorgangAb(idList2);
                            }
                            catch (ArrayIndexOutOfBoundsException idList) {
                                // empty catch block
                            }
                        }
                    }
                    int anzahlStunden = 1;
                    if (automatischerEinbehaltArt == 2) {
                        if (kID == 1) {
                            TabelleStatistikEinsatz tabEinsatzStatistik = new TabelleStatistikEinsatz();
                            anzahlStunden = AbrechnungService.aufrunden(tabEinsatzStatistik.getDauer(vID));
                        } else if (kID == 3) {
                            TabelleStatistikbsw tabStatistikBSW = new TabelleStatistikbsw();
                            anzahlStunden = AbrechnungService.aufrunden(tabStatistikBSW.getDauer(vID));
                        } else {
                            TabelleStatistikSonstigeVeranstaltung tabStatistikSonstige = new TabelleStatistikSonstigeVeranstaltung();
                            anzahlStunden = AbrechnungService.aufrunden(tabStatistikSonstige.getDauer(vID));
                        }
                    }
                    int i = 0;
                    while (i < anwesende.length) {
                        abrechnung.setId(tabAbrechnung.getNextNummer());
                        abrechnung.setAbrechnungID(0);
                        abrechnung.setArtikelID(artID);
                        abrechnung.setDatum(buchungstag);
                        abrechnung.setJahr(jahr);
                        abrechnung.setMitgliedID(anwesende[i]);
                        abrechnung.setStatus(0);
                        abrechnung.setVeranstaltungID(vID);
                        abrechnung.setVeranstaltungKategorie(kID);
                        abrechnung.setMenge(menge);
                        abrechnung.setWert(wert);
                        abrechnung.setZahlungsart(zahlungsart);
                        abrechnung.setBuchungskonto(buchungskonto);
                        abrechnung.setUmbuchungID(0);
                        tabAbrechnung.insert(abrechnung);
                        if (automatischerEinbehaltArt == 1 | automatischerEinbehaltArt == 2 && automatischerEinbehalt != 0) {
                            abrechnung.setId(tabAbrechnung.getNextNummer());
                            abrechnung.setAbrechnungID(0);
                            abrechnung.setArtikelID(artID);
                            abrechnung.setDatum(buchungstag);
                            abrechnung.setJahr(jahr);
                            abrechnung.setMitgliedID(anwesende[i]);
                            abrechnung.setStatus(0);
                            abrechnung.setVeranstaltungID(vID);
                            abrechnung.setVeranstaltungKategorie(kID);
                            abrechnung.setMenge(1);
                            if (automatischerEinbehaltArt == 2) {
                                abrechnung.setWert(automatischerEinbehalt * anzahlStunden);
                            } else {
                                abrechnung.setWert(automatischerEinbehalt);
                            }
                            abrechnung.setZahlungsart(1);
                            abrechnung.setBuchungskonto(buchungskonto);
                            abrechnung.setUmbuchungID(0);
                            tabAbrechnung.insert(abrechnung);
                        }
                        ++i;
                    }
                    logging.logInfo((Object)"Abrechnung erstellt");
                }
                catch (SQLException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
                catch (ArithmeticException e) {
                }
                catch (DocumentException e) {
                    logging.logPrintStackTrace((Exception)((Object)e));
                }
                catch (IOException e) {
                    logging.logPrintStackTrace((Exception)e);
                }
            }
        };
        threadAbrechnung.start();
    }

    private static int calculateAmount(int vID, int kID, int artID) throws SQLException {
        TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
        int ergebnis = 0;
        int wert = tabArtikel.getArtikelWert(artID);
        int automatischerEinbehalt = tabArtikel.getArtikelRabattWert(artID);
        int berechnungsart = tabArtikel.getArtikelBerechnungsart(artID);
        int berechnungsart2 = tabArtikel.getArtikelBerechnungsart2(artID);
        int automatischerEinbehaltArt = tabArtikel.getArtikelrabattArt(artID);
        if (berechnungsart == 1) {
            int anzahlStunden = 0;
            if (kID == 1) {
                TabelleStatistikEinsatz tabEinsatzStatistik = new TabelleStatistikEinsatz();
                anzahlStunden = AbrechnungService.aufrunden(tabEinsatzStatistik.getDauer(vID));
                if (berechnungsart2 == 1) {
                    ergebnis = wert * anzahlStunden;
                } else if (berechnungsart2 == 2) {
                    ergebnis = wert * tabEinsatzStatistik.getDauer(vID) / 60;
                }
            } else if (kID == 3) {
                TabelleStatistikbsw tabStatistikBSW = new TabelleStatistikbsw();
                anzahlStunden = AbrechnungService.aufrunden(tabStatistikBSW.getDauer(vID));
                if (berechnungsart2 == 1) {
                    ergebnis = wert * anzahlStunden;
                } else if (berechnungsart2 == 2) {
                    ergebnis = wert * tabStatistikBSW.getDauer(vID) / 60;
                }
            } else {
                TabelleStatistikSonstigeVeranstaltung tabStatistikSonstige = new TabelleStatistikSonstigeVeranstaltung();
                anzahlStunden = AbrechnungService.aufrunden(tabStatistikSonstige.getDauer(vID));
                if (berechnungsart2 == 1) {
                    ergebnis = wert * anzahlStunden;
                } else if (berechnungsart2 == 2) {
                    ergebnis = wert * tabStatistikSonstige.getDauer(vID) / 60;
                }
            }
            if (automatischerEinbehaltArt == 1) {
                ergebnis -= automatischerEinbehalt;
            } else if (automatischerEinbehaltArt == 2) {
                ergebnis -= automatischerEinbehalt * anzahlStunden;
            }
        } else if (berechnungsart == 2) {
            ergebnis = kID == 1 ? wert : (kID == 3 ? wert : wert);
            if (automatischerEinbehaltArt == 1) {
                ergebnis -= automatischerEinbehalt;
            }
        }
        logging.logInfo((Object)ergebnis);
        return ergebnis;
    }

    private static int aufrunden(int zeitwert) {
        double zwischenergebnis = new Double(zeitwert) / 60.0;
        BigDecimal bd = new BigDecimal(zwischenergebnis);
        bd = bd.setScale(0, 0);
        return bd.intValue();
    }

    public static void rechneVorgangAb(int[] id) throws SQLException, DocumentException, IOException {
        TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
        int abrID = tabAbrechnung.getAbrechnugID();
        String dateiname = String.valueOf(runApplication.arbeitsverzeichnis) + "data/Abrechnung/ABR_" + abrID + ".pdf";
        int i = 0;
        while (i < id.length) {
            tabAbrechnung.updateOffeneVorgaenge(abrID, id[i]);
            ++i;
        }
        Utils.dateiKatalogisieren(dateiname);
        logbuchEingabe.NeuerEintag("Abrechnung " + abrID + " wurde erstellt");
        logging.logInfo((Object)("Abrechnung " + abrID + " wurde erstellt"));
        PDFAbrechnung.PDFdocumentErstellen(dateiname, abrID);
    }

    public static Vector<Vector<String>> getAllKontobilanzen(String kontoname) throws SQLException {
        TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
        int einnahmen = tabAbrechnung.getSum(kontoname, 1);
        int ausgaben = tabAbrechnung.getSumWithStatus(kontoname, 2, 1);
        int offene = tabAbrechnung.getSumWithStatus(kontoname, 1, 0);
        int nochZuZahlen = tabAbrechnung.getSumWithStatus(kontoname, 2, 0);
        int summe = einnahmen - ausgaben;
        int summegesamt = summe - offene;
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        liste.add(AbrechnungService.mapResultSetToVector("EINNAHMEN GESAMT", MoneyCalculation.parseMoneyVauleForGUI(einnahmen)));
        liste.add(AbrechnungService.mapResultSetToVector("AUSGABEN GESAMT", MoneyCalculation.parseMoneyVauleForGUI(ausgaben)));
        liste.add(AbrechnungService.mapResultSetToVector("ZWISCHEN SUMME", MoneyCalculation.parseMoneyVauleForGUI(summe)));
        liste.add(AbrechnungService.mapResultSetToVector("OFFENE POSTEN", MoneyCalculation.parseMoneyVauleForGUI(offene)));
        liste.add(AbrechnungService.mapResultSetToVector("SUMME GESAMT", MoneyCalculation.parseMoneyVauleForGUI(summegesamt)));
        liste.add(AbrechnungService.mapResultSetToVector("NOCH ZU ZAHLEN", MoneyCalculation.parseMoneyVauleForGUI(nochZuZahlen)));
        logging.logSQL(liste);
        return liste;
    }

    private static Vector<String> mapResultSetToVector(String text, String value) {
        Vector<String> liste = new Vector<String>();
        if (text.equals("AUSGABEN GESAMT") | text.equals("OFFENE POSTEN") | text.equals("NOCH ZU ZAHLEN")) {
            liste.add(text);
            if (!value.equals("0,00")) {
                liste.add("-" + value + "\u20ac");
            } else {
                liste.add(String.valueOf(value) + "\u20ac");
            }
        } else {
            liste.add(text);
            liste.add(String.valueOf(value) + "\u20ac");
        }
        return liste;
    }
}

