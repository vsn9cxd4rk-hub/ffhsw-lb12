/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package utilities;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;

public class TimeCalculation {
    public static int calculateDuration(String start, String ende) {
        try {
            int stundeStart = Integer.parseInt(start.substring(0, 2));
            logging.logInfo((Object)("Start Stunde " + stundeStart));
            int minutenStart = Integer.parseInt(start.substring(3, 5));
            logging.logInfo((Object)("Start Minuten " + minutenStart));
            int stundeEnde = Integer.parseInt(ende.substring(0, 2));
            logging.logInfo((Object)("Ende Stunde " + stundeEnde));
            int minutenEnde = Integer.parseInt(ende.substring(3, 5));
            logging.logInfo((Object)("Ende Minuten " + minutenEnde));
            int ergebnisStart = stundeStart * 60 + minutenStart;
            logging.logInfo((Object)("Ergebnis Rechnung A: " + ergebnisStart));
            int ergebnisEnde = stundeEnde * 60 + minutenEnde;
            logging.logInfo((Object)("Ergebnis Rechnung A: " + ergebnisEnde));
            int ergebnis = ergebnisStart >= ergebnisEnde ? (ergebnisEnde += 1440) - ergebnisStart : ergebnisEnde - ergebnisStart;
            logging.logInfo((Object)("Die Zeit betrg\u00e4gt in Minuten: " + ergebnis));
            return ergebnis;
        }
        catch (StringIndexOutOfBoundsException e) {
            logging.logWarning((Object)"Die Zeit konnte nicht berechnet werden. Es wir 0 zur\u00fcck gegeben");
            return 0;
        }
    }

    public static String parseDateForDatabase(String datum) {
        try {
            String ergebnis = String.valueOf(datum.substring(6, 10)) + "-" + datum.substring(3, 5) + "-" + datum.substring(0, 2);
            logging.logInfo((Object)("ParseDateFormatForDatabase: " + ergebnis));
            return ergebnis;
        }
        catch (StringIndexOutOfBoundsException e) {
            return "";
        }
    }

    public static String parseShortDateForDatabase(String datum) {
        try {
            String letzterTagDesMonats = datum.substring(0, 2).equals("01") | datum.substring(0, 2).equals("03") | datum.substring(0, 2).equals("05") | datum.substring(0, 2).equals("07") | datum.substring(0, 2).equals("08") | datum.substring(0, 2).equals("10") | datum.substring(0, 2).equals("12") ? "31" : (datum.substring(0, 2).equals("02") ? "28" : "30");
            String ergebnis = String.valueOf(datum.substring(3, 7)) + "-" + datum.substring(0, 2) + "-" + letzterTagDesMonats;
            logging.logInfo((Object)("ParseShortDateFormatForDatabase: " + ergebnis));
            return ergebnis;
        }
        catch (StringIndexOutOfBoundsException e) {
            return "";
        }
    }

    public static String parseDateForGUI(String datum) {
        try {
            String ergebnis = String.valueOf(datum.substring(8, 10)) + "." + datum.substring(5, 7) + "." + datum.substring(0, 4);
            logging.logInfo((Object)("ParseShortDateFormatForGUI: " + ergebnis));
            return ergebnis;
        }
        catch (NullPointerException | StringIndexOutOfBoundsException e) {
            return "";
        }
    }

    public static String parseShortDateForGUI(String datum) {
        try {
            String ergebnis = String.valueOf(datum.substring(5, 7)) + "." + datum.substring(0, 4);
            logging.logInfo((Object)("ParseDateFormatForGUI: " + ergebnis));
            return ergebnis;
        }
        catch (NullPointerException | StringIndexOutOfBoundsException e) {
            return "";
        }
    }

    public static boolean checkDateFormat(String datum) {
        block7: {
            block6: {
                if (!(Integer.parseInt(datum.substring(0, 2)) >= 32 | Integer.parseInt(datum.substring(0, 2)) <= 0)) break block6;
                logging.logInfo((Object)"Hoppala Tag ist falsch eingegeben");
                return false;
            }
            if (!(Integer.parseInt(datum.substring(3, 5)) >= 13 | Integer.parseInt(datum.substring(3, 5)) <= 0)) break block7;
            logging.logInfo((Object)"Hoppala Monat ist falsch eingegeben");
            return false;
        }
        try {
            if (datum.length() != 10) {
                logging.logInfo((Object)"Hoppala die L\u00e4nge ist falsch eingegeben");
                return false;
            }
            return true;
        }
        catch (StringIndexOutOfBoundsException e) {
            return false;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkDateShortFormat(String datum) {
        block5: {
            if (!(Integer.parseInt(datum.substring(0, 2)) >= 13 | Integer.parseInt(datum.substring(0, 2)) <= 0)) break block5;
            logging.logInfo((Object)"Hoppala Monat ist falsch eingegeben");
            return false;
        }
        try {
            if (datum.length() != 7) {
                logging.logInfo((Object)"Hoppala die L\u00e4nge ist falsch eingegeben");
                return false;
            }
            return true;
        }
        catch (StringIndexOutOfBoundsException e) {
            return false;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean checkTimeFormat(String zeit) {
        block8: {
            block7: {
                block6: {
                    if (Integer.parseInt(zeit.substring(0, 2)) < 24) break block6;
                    logging.logInfo((Object)"Hoppala Stunde ist falsch eingegeben");
                    return false;
                }
                if (Integer.parseInt(zeit.substring(3, 5)) < 60) break block7;
                logging.logInfo((Object)"Hoppala Minute ist falsch eingegeben");
                return false;
            }
            if (zeit.length() == 5) break block8;
            logging.logInfo((Object)"Hoppala die L\u00e4nge ist falsch eingegeben");
            return false;
        }
        try {
            if (!zeit.substring(2, 3).equals(":") && !zeit.substring(2, 3).equals(".")) {
                logging.logInfo((Object)("Falsches Trennzeichen ist: " + zeit.substring(2, 3)));
                logging.logInfo((Object)"Hoppala das Trennungszeichen ist falsch");
                return false;
            }
            return true;
        }
        catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            logging.logError((Object)"NuberFormatException wird abgefangen und Benutzer muss Fehler anpassen");
            return false;
        }
    }

    public static int wochentagErmitteln(String datum) {
        int tag = Integer.parseInt(datum.substring(0, 2));
        int monat = Integer.parseInt(datum.substring(3, 5));
        int jahr = Integer.parseInt(datum.substring(6, 10));
        int ReferenzJahr = 1584;
        int VergangeneTage = 0;
        boolean Schaltjahr = false;
        while (ReferenzJahr < jahr) {
            Schaltjahr = TimeCalculation.istSchaltjahr(ReferenzJahr);
            VergangeneTage = Schaltjahr ? (VergangeneTage += 366) : (VergangeneTage += 365);
            ++ReferenzJahr;
        }
        int Februar = TimeCalculation.istSchaltjahr(jahr) ? 29 : 28;
        switch (monat) {
            case 12: {
                VergangeneTage += 30;
            }
            case 11: {
                VergangeneTage += 31;
            }
            case 10: {
                VergangeneTage += 30;
            }
            case 9: {
                VergangeneTage += 31;
            }
            case 8: {
                VergangeneTage += 31;
            }
            case 7: {
                VergangeneTage += 30;
            }
            case 6: {
                VergangeneTage += 31;
            }
            case 5: {
                VergangeneTage += 30;
            }
            case 4: {
                VergangeneTage += 31;
            }
            case 3: {
                VergangeneTage += Februar;
            }
            case 2: {
                VergangeneTage += 31;
            }
            case 1: {
                break;
            }
            default: {
                logging.logError((Object)"Fehler bei der Berechnung deas Wochentages (WochentagID == 0)");
                return 0;
            }
        }
        int Wochentag = (VergangeneTage + tag - 1) % 7;
        logging.logInfo((Object)("WochentagID == " + Wochentag));
        return Wochentag;
    }

    private static boolean istTeilbar(int x, int teiler) {
        return x % teiler == 0;
    }

    private static boolean istSchaltjahr(int jahr) {
        return TimeCalculation.istTeilbar(jahr, 400) || TimeCalculation.istTeilbar(jahr, 4) && !TimeCalculation.istTeilbar(jahr, 100);
    }

    public static String wochentagNameByWochentagID(int wochentagID) {
        String wochentagName = null;
        if (wochentagID == 0) {
            wochentagName = "Sonntag";
        } else if (wochentagID == 1) {
            wochentagName = "Montag";
        } else if (wochentagID == 2) {
            wochentagName = "Dienstag";
        } else if (wochentagID == 3) {
            wochentagName = "Mittwoch";
        } else if (wochentagID == 4) {
            wochentagName = "Donnerstag";
        } else if (wochentagID == 5) {
            wochentagName = "Freitag";
        } else if (wochentagID == 6) {
            wochentagName = "Samstag";
        }
        return wochentagName;
    }

    public static String minutenInStundenUmrechnen(int dauer) {
        StringBuilder build = new StringBuilder();
        DecimalFormat f = new DecimalFormat("#00");
        int stunden = 0;
        if (dauer >= 60) {
            do {
                ++stunden;
            } while ((dauer -= 60) > 59);
            build.append(f.format(stunden));
            build.append(":");
            build.append(f.format(dauer));
        } else {
            build.append("00");
            build.append(":");
            build.append(f.format(dauer));
        }
        return build.toString();
    }

    public static int stundenInMinutenUmrechnen(String zeit) {
        int stunde = Integer.parseInt(zeit.substring(0, 2));
        logging.logInfo((Object)("Stunde " + stunde));
        int minuten = Integer.parseInt(zeit.substring(3, 5));
        logging.logInfo((Object)("Minuten " + minuten));
        int ergebnis = stunde * 60 + minuten;
        logging.logInfo((Object)("Ergebnis Rechnung: " + ergebnis));
        return ergebnis;
    }

    public static String millisecondsToDate(long millis) {
        Date currentDate = new Date(millis);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return df.format(currentDate);
    }

    public static String calculateNextMonth(String currentMonth) {
        int monat = Integer.parseInt(currentMonth);
        int nextMonat = monat + 1;
        if (nextMonat == 13) {
            logging.logInfo((Object)("calculateNextMonth() --> Akt. Monat: " + currentMonth + " Fol. Monat: 01"));
            return "01";
        }
        if (nextMonat <= 9) {
            logging.logInfo((Object)("calculateNextMonth() --> Akt. Monat: " + currentMonth + " Fol. Monat: 0" + nextMonat));
            return "0" + Integer.toString(nextMonat);
        }
        logging.logInfo((Object)("calculateNextMonth() --> Akt. Monat: " + currentMonth + " Fol. Monat: " + nextMonat));
        return Integer.toString(nextMonat);
    }

    public static String visableFutureDateItemsInList() {
        if (runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungZukunft").equals("Alle")) {
            logging.logInfo((Object)"visableFutureDateItemsInList --> 2099-12");
            return "2099-12";
        }
        if (runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungZukunft").equals("aktuelles Jahr")) {
            logging.logInfo((Object)("visableFutureDateItemsInList --> " + SbcUtils.timeStamp((String)"yyyy") + "-12"));
            return String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-12";
        }
        StringBuilder ergebnis = new StringBuilder();
        String jahr = SbcUtils.timeStamp((String)"yyyy");
        String monat = SbcUtils.timeStamp((String)"MM");
        int neuerMonat = Integer.parseInt(monat) + Integer.parseInt(runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungZukunft"));
        if (neuerMonat >= 13) {
            int n\u00e4chstesJahr = neuerMonat - 12;
            if (n\u00e4chstesJahr <= 9) {
                ergebnis.append(Integer.parseInt(jahr) + 1);
                ergebnis.append("-");
                ergebnis.append("0");
                ergebnis.append(n\u00e4chstesJahr);
            } else {
                ergebnis.append(Integer.parseInt(jahr) + 1);
                ergebnis.append("-");
                ergebnis.append(n\u00e4chstesJahr);
            }
        } else if (neuerMonat <= 9) {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append("0");
            ergebnis.append(neuerMonat);
        } else {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append(neuerMonat);
        }
        logging.logInfo((Object)("visableFutureDateItemsInList --> " + ergebnis.toString()));
        return ergebnis.toString();
    }

    public static String visablePastDateItemsInList() {
        if (runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungVergangenheit").equals("Alle")) {
            logging.logInfo((Object)"visablePastDateItemsInList --> 1977-01");
            return "1977-01";
        }
        if (runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungVergangenheit").equals("aktuelles Jahr")) {
            logging.logInfo((Object)("visableFutureDateItemsInList --> " + SbcUtils.timeStamp((String)"yyyy") + "-01"));
            return String.valueOf(SbcUtils.timeStamp((String)"yyyy")) + "-01";
        }
        StringBuilder ergebnis = new StringBuilder();
        String jahr = SbcUtils.timeStamp((String)"yyyy");
        String monat = SbcUtils.timeStamp((String)"MM");
        int neuerMonat = Integer.parseInt(monat) - Integer.parseInt(runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungVergangenheit"));
        if (neuerMonat <= 0) {
            int vergangenesJahr = 12 + neuerMonat;
            if (vergangenesJahr <= 9) {
                ergebnis.append(Integer.parseInt(jahr) - 1);
                ergebnis.append("-");
                ergebnis.append("0");
                ergebnis.append(vergangenesJahr);
            } else {
                ergebnis.append(Integer.parseInt(jahr) - 1);
                ergebnis.append("-");
                ergebnis.append(vergangenesJahr);
            }
        } else if (neuerMonat <= 9) {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append("0");
            ergebnis.append(neuerMonat);
        } else {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append(neuerMonat);
        }
        logging.logInfo((Object)("visablePastDateItemsInList --> " + ergebnis.toString()));
        return ergebnis.toString();
    }

    public static int checkJahrDifferenz(String startJahr, String endJahr) {
        int diff = 0;
        try {
            diff = Integer.parseInt(endJahr.substring(0, 4)) - Integer.parseInt(startJahr.substring(0, 4));
            logging.logInfo((Object)("result JahrDifferenz == " + diff));
            return diff;
        }
        catch (StringIndexOutOfBoundsException e) {
            logging.logPrintStackTrace((Exception)e);
            return diff;
        }
    }

    public static String getTageBis(String dbDatum) {
        int ergebnis;
        block7: {
            block6: {
                logging.logInfo((Object)("getTageBis() --> " + dbDatum));
                if (dbDatum.equals("")) {
                    logging.logInfo((Object)"getTageBis() --> n.V.");
                    return "n.V.";
                }
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date reference = format.parse(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    long longErgebnis = format.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
                    ergebnis = (int)longErgebnis;
                    if (ergebnis != 1) break block6;
                    logging.logInfo((Object)"getTageBis() --> MORGEN!");
                    return "Morgen";
                }
                catch (ParseException e) {
                    logging.logPrintStackTrace((Exception)e);
                    return "n.V.";
                }
            }
            if (ergebnis != 0) break block7;
            logging.logInfo((Object)"getTageBis() --> HEUTE!");
            return "Heute";
        }
        if (ergebnis <= -1) {
            logging.logInfo((Object)("getTageBis() --> abgelaufen seit " + ergebnis));
            String ausgabe = Integer.toString(ergebnis);
            return "abgelaufen seit " + ausgabe.substring(1, ausgabe.length()) + " Tagen";
        }
        logging.logInfo((Object)("getTageBis() --> " + ergebnis));
        return String.valueOf(Integer.toString(ergebnis)) + " Tage";
    }

    public static String getTageBisOhneDebug(String dbDatum) {
        int ergebnis;
        block7: {
            block6: {
                if (dbDatum.equals("") | dbDatum.equals("null")) {
                    return "";
                }
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date reference = format.parse(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    long longErgebnis = format.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
                    ergebnis = (int)longErgebnis;
                    if (ergebnis != 1) break block6;
                    System.out.println("Morgen");
                    return "Morgen";
                }
                catch (ParseException e) {
                    System.out.println(dbDatum);
                    logging.logPrintStackTrace((Exception)e);
                    return "n.V.";
                }
            }
            if (ergebnis != 0) break block7;
            System.out.println("Heute");
            return "Heute";
        }
        if (ergebnis <= -1) {
            String ausgabe = Integer.toString(ergebnis);
            return "abgelaufen seit " + ausgabe.substring(1, ausgabe.length()) + " Tagen";
        }
        return String.valueOf(Integer.toString(ergebnis)) + " Tage";
    }

    public static String getTageBisGeburtstag(String dbOriginalDatum) {
        int ergebnis;
        block7: {
            block6: {
                int jahr = Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"));
                String dbDatum = String.valueOf(++jahr) + dbOriginalDatum;
                logging.logInfo((Object)("getTageBisGeburtstag() --> " + dbDatum));
                if (dbDatum.equals("")) {
                    logging.logInfo((Object)"getTageBisGeburtstag() --> n.V.");
                    return "n.V.";
                }
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date reference = format.parse(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                    long longErgebnis = format.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
                    ergebnis = (int)longErgebnis;
                    if (ergebnis >= 365) {
                        logging.logInfo((Object)"getTageBisGeburtstag() >= 365");
                        dbDatum = String.valueOf(--jahr) + dbOriginalDatum;
                        longErgebnis = format.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
                        ergebnis = (int)longErgebnis;
                    }
                    if (ergebnis != 1) break block6;
                    logging.logInfo((Object)"getTageBisGeburtstag() --> MORGEN!");
                    return "Morgen";
                }
                catch (ParseException e) {
                    logging.logPrintStackTrace((Exception)e);
                    return "n.V.";
                }
            }
            if (ergebnis != 0) break block7;
            logging.logInfo((Object)"getTageBisGeburtstag() --> HEUTE!");
            return "Heute";
        }
        logging.logInfo((Object)("getTageBisGeburtstag() --> " + ergebnis));
        return String.valueOf(Integer.toString(ergebnis)) + " Tage";
    }
}

