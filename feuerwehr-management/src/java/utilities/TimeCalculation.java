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
         int e = Integer.parseInt(start.substring(0, 2));
         logging.logInfo("Start Stunde " + e);
         int minutenStart = Integer.parseInt(start.substring(3, 5));
         logging.logInfo("Start Minuten " + minutenStart);
         int stundeEnde = Integer.parseInt(ende.substring(0, 2));
         logging.logInfo("Ende Stunde " + stundeEnde);
         int minutenEnde = Integer.parseInt(ende.substring(3, 5));
         logging.logInfo("Ende Minuten " + minutenEnde);
         int ergebnisStart = e * 60 + minutenStart;
         logging.logInfo("Ergebnis Rechnung A: " + ergebnisStart);
         int ergebnisEnde = stundeEnde * 60 + minutenEnde;
         logging.logInfo("Ergebnis Rechnung A: " + ergebnisEnde);
         int ergebnis;
         if(ergebnisStart >= ergebnisEnde) {
            ergebnisEnde += 1440;
            ergebnis = ergebnisEnde - ergebnisStart;
         } else {
            ergebnis = ergebnisEnde - ergebnisStart;
         }

         logging.logInfo("Die Zeit betrgägt in Minuten: " + ergebnis);
         return ergebnis;
      } catch (StringIndexOutOfBoundsException var9) {
         logging.logWarning("Die Zeit konnte nicht berechnet werden. Es wir 0 zurück gegeben");
         return 0;
      }
   }

   public static String parseDateForDatabase(String datum) {
      try {
         String e = datum.substring(6, 10) + "-" + datum.substring(3, 5) + "-" + datum.substring(0, 2);
         logging.logInfo("ParseDateFormatForDatabase: " + e);
         return e;
      } catch (StringIndexOutOfBoundsException var2) {
         return "";
      }
   }

   public static String parseShortDateForDatabase(String datum) {
      try {
         String e;
         if(datum.substring(0, 2).equals("01") | datum.substring(0, 2).equals("03") | datum.substring(0, 2).equals("05") | datum.substring(0, 2).equals("07") | datum.substring(0, 2).equals("08") | datum.substring(0, 2).equals("10") | datum.substring(0, 2).equals("12")) {
            e = "31";
         } else if(datum.substring(0, 2).equals("02")) {
            e = "28";
         } else {
            e = "30";
         }

         String ergebnis = datum.substring(3, 7) + "-" + datum.substring(0, 2) + "-" + e;
         logging.logInfo("ParseShortDateFormatForDatabase: " + ergebnis);
         return ergebnis;
      } catch (StringIndexOutOfBoundsException var3) {
         return "";
      }
   }

   public static String parseDateForGUI(String datum) {
      try {
         String e = datum.substring(8, 10) + "." + datum.substring(5, 7) + "." + datum.substring(0, 4);
         logging.logInfo("ParseShortDateFormatForGUI: " + e);
         return e;
      } catch (StringIndexOutOfBoundsException var2) {
         return "";
      }
   }

   public static String parseShortDateForGUI(String datum) {
      try {
         String e = datum.substring(5, 7) + "." + datum.substring(0, 4);
         logging.logInfo("ParseDateFormatForGUI: " + e);
         return e;
      } catch (StringIndexOutOfBoundsException var2) {
         return "";
      }
   }

   public static boolean checkDateFormat(String datum) {
      try {
         if(Integer.parseInt(datum.substring(0, 2)) >= 32 | Integer.parseInt(datum.substring(0, 2)) <= 0) {
            logging.logInfo("Hoppala Tag ist falsch eingegeben");
            return false;
         } else if(Integer.parseInt(datum.substring(3, 5)) >= 13 | Integer.parseInt(datum.substring(3, 5)) <= 0) {
            logging.logInfo("Hoppala Monat ist falsch eingegeben");
            return false;
         } else if(datum.length() != 10) {
            logging.logInfo("Hoppala die Länge ist falsch eingegeben");
            return false;
         } else {
            return true;
         }
      } catch (StringIndexOutOfBoundsException var2) {
         return false;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   public static boolean checkDateShortFormat(String datum) {
      try {
         if(Integer.parseInt(datum.substring(0, 2)) >= 13 | Integer.parseInt(datum.substring(0, 2)) <= 0) {
            logging.logInfo("Hoppala Monat ist falsch eingegeben");
            return false;
         } else if(datum.length() != 7) {
            logging.logInfo("Hoppala die Länge ist falsch eingegeben");
            return false;
         } else {
            return true;
         }
      } catch (StringIndexOutOfBoundsException var2) {
         return false;
      } catch (NumberFormatException var3) {
         return false;
      }
   }

   public static boolean checkTimeFormat(String zeit) {
      try {
         if(Integer.parseInt(zeit.substring(0, 2)) >= 24) {
            logging.logInfo("Hoppala Stunde ist falsch eingegeben");
            return false;
         } else if(Integer.parseInt(zeit.substring(3, 5)) >= 60) {
            logging.logInfo("Hoppala Minute ist falsch eingegeben");
            return false;
         } else if(zeit.length() != 5) {
            logging.logInfo("Hoppala die Länge ist falsch eingegeben");
            return false;
         } else if(!zeit.substring(2, 3).equals(":") && !zeit.substring(2, 3).equals(".")) {
            logging.logInfo("Falsches Trennzeichen ist: " + zeit.substring(2, 3));
            logging.logInfo("Hoppala das Trennungszeichen ist falsch");
            return false;
         } else {
            return true;
         }
      } catch (StringIndexOutOfBoundsException var2) {
         logging.logError("NuberFormatException wird abgefangen und Benutzer muss Fehler anpassen");
         return false;
      }
   }

   public static int wochentagErmitteln(String datum) {
      int tag = Integer.parseInt(datum.substring(0, 2));
      int monat = Integer.parseInt(datum.substring(3, 5));
      int jahr = Integer.parseInt(datum.substring(6, 10));
      int ReferenzJahr = 1584;
      int VergangeneTage = 0;

      for(boolean Schaltjahr = false; ReferenzJahr < jahr; ++ReferenzJahr) {
         Schaltjahr = istSchaltjahr(ReferenzJahr);
         if(Schaltjahr) {
            VergangeneTage += 366;
         } else {
            VergangeneTage += 365;
         }
      }

      int Februar = istSchaltjahr(jahr)?29:28;
      switch(monat) {
      case 12:
         VergangeneTage += 30;
      case 11:
         VergangeneTage += 31;
      case 10:
         VergangeneTage += 30;
      case 9:
         VergangeneTage += 31;
      case 8:
         VergangeneTage += 31;
      case 7:
         VergangeneTage += 30;
      case 6:
         VergangeneTage += 31;
      case 5:
         VergangeneTage += 30;
      case 4:
         VergangeneTage += 31;
      case 3:
         VergangeneTage += Februar;
      case 2:
         VergangeneTage += 31;
      case 1:
         int Wochentag = (VergangeneTage + tag - 1) % 7;
         logging.logInfo("WochentagID == " + Wochentag);
         return Wochentag;
      default:
         logging.logError("Fehler bei der Berechnung deas Wochentages (WochentagID == 0)");
         return 0;
      }
   }

   private static boolean istTeilbar(int x, int teiler) {
      return x % teiler == 0;
   }

   private static boolean istSchaltjahr(int jahr) {
      return istTeilbar(jahr, 400) || istTeilbar(jahr, 4) && !istTeilbar(jahr, 100);
   }

   public static String wochentagNameByWochentagID(int wochentagID) {
      String wochentagName = null;
      if(wochentagID == 0) {
         wochentagName = "Sonntag";
      } else if(wochentagID == 1) {
         wochentagName = "Montag";
      } else if(wochentagID == 2) {
         wochentagName = "Dienstag";
      } else if(wochentagID == 3) {
         wochentagName = "Mittwoch";
      } else if(wochentagID == 4) {
         wochentagName = "Donnerstag";
      } else if(wochentagID == 5) {
         wochentagName = "Freitag";
      } else if(wochentagID == 6) {
         wochentagName = "Samstag";
      }

      return wochentagName;
   }

   public static String minutenInStundenUmrechnen(int dauer) {
      StringBuilder build = new StringBuilder();
      DecimalFormat f = new DecimalFormat("#00");
      int stunden = 0;
      if(dauer >= 60) {
         do {
            dauer -= 60;
            ++stunden;
         } while(dauer > 59);

         build.append(f.format((long)stunden));
         build.append(":");
         build.append(f.format((long)dauer));
      } else {
         build.append("00");
         build.append(":");
         build.append(f.format((long)dauer));
      }

      return build.toString();
   }

   public static int stundenInMinutenUmrechnen(String zeit) {
      int stunde = Integer.parseInt(zeit.substring(0, 2));
      logging.logInfo("Stunde " + stunde);
      int minuten = Integer.parseInt(zeit.substring(3, 5));
      logging.logInfo("Minuten " + minuten);
      int ergebnis = stunde * 60 + minuten;
      logging.logInfo("Ergebnis Rechnung: " + ergebnis);
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
      if(nextMonat == 13) {
         logging.logInfo("calculateNextMonth() --> Akt. Monat: " + currentMonth + " Fol. Monat: 01");
         return "01";
      } else if(nextMonat <= 9) {
         logging.logInfo("calculateNextMonth() --> Akt. Monat: " + currentMonth + " Fol. Monat: 0" + nextMonat);
         return "0" + Integer.toString(nextMonat);
      } else {
         logging.logInfo("calculateNextMonth() --> Akt. Monat: " + currentMonth + " Fol. Monat: " + nextMonat);
         return Integer.toString(nextMonat);
      }
   }

   public static String visableFutureDateItemsInList() {
      if(((String)runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungZukunft")).equals("Alle")) {
         logging.logInfo("visableFutureDateItemsInList --> 2099-12");
         return "2099-12";
      } else if(((String)runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungZukunft")).equals("aktuelles Jahr")) {
         logging.logInfo("visableFutureDateItemsInList --> " + SbcUtils.timeStamp("yyyy") + "-12");
         return SbcUtils.timeStamp("yyyy") + "-12";
      } else {
         StringBuilder ergebnis = new StringBuilder();
         String jahr = SbcUtils.timeStamp("yyyy");
         String monat = SbcUtils.timeStamp("MM");
         int neuerMonat = Integer.parseInt(monat) + Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungZukunft"));
         if(neuerMonat >= 13) {
            int nächstesJahr = neuerMonat - 12;
            if(nächstesJahr <= 9) {
               ergebnis.append(Integer.parseInt(jahr) + 1);
               ergebnis.append("-");
               ergebnis.append("0");
               ergebnis.append(nächstesJahr);
            } else {
               ergebnis.append(Integer.parseInt(jahr) + 1);
               ergebnis.append("-");
               ergebnis.append(nächstesJahr);
            }
         } else if(neuerMonat <= 9) {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append("0");
            ergebnis.append(neuerMonat);
         } else {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append(neuerMonat);
         }

         logging.logInfo("visableFutureDateItemsInList --> " + ergebnis.toString());
         return ergebnis.toString();
      }
   }

   public static String visablePastDateItemsInList() {
      if(((String)runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungVergangenheit")).equals("Alle")) {
         logging.logInfo("visablePastDateItemsInList --> 1977-01");
         return "1977-01";
      } else if(((String)runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungVergangenheit")).equals("aktuelles Jahr")) {
         logging.logInfo("visableFutureDateItemsInList --> " + SbcUtils.timeStamp("yyyy") + "-01");
         return SbcUtils.timeStamp("yyyy") + "-01";
      } else {
         StringBuilder ergebnis = new StringBuilder();
         String jahr = SbcUtils.timeStamp("yyyy");
         String monat = SbcUtils.timeStamp("MM");
         int neuerMonat = Integer.parseInt(monat) - Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("SichtbarkeitVeranstaltungVergangenheit"));
         if(neuerMonat <= 0) {
            int vergangenesJahr = 12 + neuerMonat;
            if(vergangenesJahr <= 9) {
               ergebnis.append(Integer.parseInt(jahr) - 1);
               ergebnis.append("-");
               ergebnis.append("0");
               ergebnis.append(vergangenesJahr);
            } else {
               ergebnis.append(Integer.parseInt(jahr) - 1);
               ergebnis.append("-");
               ergebnis.append(vergangenesJahr);
            }
         } else if(neuerMonat <= 9) {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append("0");
            ergebnis.append(neuerMonat);
         } else {
            ergebnis.append(jahr);
            ergebnis.append("-");
            ergebnis.append(neuerMonat);
         }

         logging.logInfo("visablePastDateItemsInList --> " + ergebnis.toString());
         return ergebnis.toString();
      }
   }

   public static int checkJahrDifferenz(String startJahr, String endJahr) {
      byte diff = 0;

      try {
         int diff1 = Integer.parseInt(endJahr.substring(0, 4)) - Integer.parseInt(startJahr.substring(0, 4));
         logging.logInfo("result JahrDifferenz == " + diff1);
         return diff1;
      } catch (StringIndexOutOfBoundsException var4) {
         logging.logPrintStackTrace(var4);
         return diff;
      }
   }

   public static String getTageBis(String dbDatum) {
      logging.logInfo("getTageBis() --> " + dbDatum);
      if(dbDatum.equals("")) {
         logging.logInfo("getTageBis() --> n.V.");
         return "n.V.";
      } else {
         try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Date reference = e.parse(SbcUtils.timeStamp("yyyy-MM-dd"));
            long longErgebnis = e.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
            int ergebnis = (int)longErgebnis;
            if(ergebnis == 1) {
               logging.logInfo("getTageBis() --> MORGEN!");
               return "Morgen";
            } else if(ergebnis == 0) {
               logging.logInfo("getTageBis() --> HEUTE!");
               return "Heute";
            } else if(ergebnis <= -1) {
               logging.logInfo("getTageBis() --> abgelaufen seit " + ergebnis);
               String ausgabe = Integer.toString(ergebnis);
               return "abgelaufen seit " + ausgabe.substring(1, ausgabe.length()) + " Tagen";
            } else {
               logging.logInfo("getTageBis() --> " + ergebnis);
               return Integer.toString(ergebnis) + " Tage";
            }
         } catch (ParseException var7) {
            logging.logPrintStackTrace(var7);
            return "n.V.";
         }
      }
   }

   public static String getTageBisOhneDebug(String dbDatum) {
      if(dbDatum.equals("") | dbDatum.equals("null")) {
         return "";
      } else {
         try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Date reference = e.parse(SbcUtils.timeStamp("yyyy-MM-dd"));
            long longErgebnis = e.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
            int ergebnis = (int)longErgebnis;
            if(ergebnis == 1) {
               System.out.println("Morgen");
               return "Morgen";
            } else if(ergebnis == 0) {
               System.out.println("Heute");
               return "Heute";
            } else if(ergebnis <= -1) {
               String ausgabe = Integer.toString(ergebnis);
               return "abgelaufen seit " + ausgabe.substring(1, ausgabe.length()) + " Tagen";
            } else {
               return Integer.toString(ergebnis) + " Tage";
            }
         } catch (ParseException var7) {
            System.out.println(dbDatum);
            logging.logPrintStackTrace(var7);
            return "n.V.";
         }
      }
   }

   public static String getTageBisGeburtstag(String dbOriginalDatum) {
      int jahr = Integer.parseInt(SbcUtils.timeStamp("yyyy"));
      ++jahr;
      String dbDatum = jahr + dbOriginalDatum;
      logging.logInfo("getTageBisGeburtstag() --> " + dbDatum);
      if(dbDatum.equals("")) {
         logging.logInfo("getTageBisGeburtstag() --> n.V.");
         return "n.V.";
      } else {
         try {
            SimpleDateFormat e = new SimpleDateFormat("yyyy-MM-dd");
            Date reference = e.parse(SbcUtils.timeStamp("yyyy-MM-dd"));
            long longErgebnis = e.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
            int ergebnis = (int)longErgebnis;
            if(ergebnis >= 365) {
               logging.logInfo("getTageBisGeburtstag() >= 365");
               --jahr;
               dbDatum = jahr + dbOriginalDatum;
               longErgebnis = e.parse(dbDatum).getTime() / 86400000L - reference.getTime() / 86400000L;
               ergebnis = (int)longErgebnis;
            }

            if(ergebnis == 1) {
               logging.logInfo("getTageBisGeburtstag() --> MORGEN!");
               return "Morgen";
            } else if(ergebnis == 0) {
               logging.logInfo("getTageBisGeburtstag() --> HEUTE!");
               return "Heute";
            } else {
               logging.logInfo("getTageBisGeburtstag() --> " + ergebnis);
               return Integer.toString(ergebnis) + " Tage";
            }
         } catch (ParseException var8) {
            logging.logPrintStackTrace(var8);
            return "n.V.";
         }
      }
   }
}
