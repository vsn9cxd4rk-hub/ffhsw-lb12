package service;

import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.abrechnung.TabelleAbrechnung;
import data.tabellen.abrechnung.TabelleAbrechnung_artikel;
import data.tabellen.statistik.TabelleStatistikEinsatz;
import data.tabellen.statistik.TabelleStatistikSonstigeVeranstaltung;
import data.tabellen.statistik.TabelleStatistikbsw;
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
      Thread threadAbrechnung = new Thread() {
         public void run() {
            TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
            TabelleAbrechnung_artikel tabArtikel = new TabelleAbrechnung_artikel();
            TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
            Abrechnung abrechnung = new Abrechnung();

            try {
               int[] e = Utils.listToIntArray(tabAnwesenheit.getAnwesendeMitgliederIDByVeranstaltung(vID));
               int artID = tabArtikel.getArtikelIDByKlasse(kID);
               int wert = AbrechnungService.calculateAmount(vID, kID, artID);
               int automatischerEinbehalt = tabArtikel.getArtikelRabattWert(artID);
               int menge = wert / tabArtikel.getArtikelWert(artID);
               int zahlungsart = tabArtikel.getZahlungsart(artID);
               int buchungskonto = tabArtikel.getBuchungskontoID(artID);
               int automatischerEinbehaltArt = tabArtikel.getArtikelrabattArt(artID);
               String buchungstag = (new TabelleVeranstaltung()).getDatum(vID);
               if(artID == 0) {
                  logging.logError("Es wurde kein Artikel gefunden, die Abrechnung wird Abgebrochen");
                  this.stop();
               }

               if(tabAbrechnung.getVeranstaltungsCount(vID) != 0) {
                  logging.logInfo("Lösche daten aus der Abrechnungstabelle");
                  tabAbrechnung.delete(vID);
                  if(tabAbrechnung.getVeranstaltungsCountMitAbrechnung(vID) != 0) {
                     try {
                        logging.logInfo("Die Veranstaltung wurde geändert, jetzt muss umgebucht werden damit die Abrechnung stimmt");
                        int[] anzahlStunden = Utils.listToIntArray(tabAbrechnung.getIDsByVeranstaltungForUmbuchung(vID));
                        int[] i = Utils.listToIntArray(tabAbrechnung.getMitgliederIDByVeranstaltungID(vID));

                        for(int idList2 = 0; idList2 < i.length; ++idList2) {
                           int umbuchungID = tabAbrechnung.getNextNummer();
                           int wertUmbuchung = tabAbrechnung.getWertByID(anzahlStunden[idList2]);
                           int mengeUmbuchung = tabAbrechnung.getMengeByID(anzahlStunden[idList2]);
                           abrechnung.setId(umbuchungID);
                           abrechnung.setAbrechnungID(0);
                           abrechnung.setArtikelID(artID);
                           abrechnung.setDatum(buchungstag);
                           abrechnung.setJahr(jahr);
                           abrechnung.setMitgliedID(i[idList2]);
                           abrechnung.setStatus(0);
                           abrechnung.setVeranstaltungID(vID);
                           abrechnung.setVeranstaltungKategorie(kID);
                           abrechnung.setMenge(-mengeUmbuchung);
                           abrechnung.setWert(-wertUmbuchung);
                           abrechnung.setZahlungsart(3);
                           abrechnung.setBuchungskonto(buchungskonto);
                           abrechnung.setUmbuchungID(0);
                           tabAbrechnung.insert(abrechnung);
                           tabAbrechnung.updateUmbuchungID(anzahlStunden[idList2], umbuchungID);
                        }

                        int[] var30 = Utils.listToIntArray(tabAbrechnung.getIDsByVeranstaltungForUmbuchung2(vID));
                        AbrechnungService.rechneVorgangAb(var30);
                     } catch (ArrayIndexOutOfBoundsException var20) {
                        ;
                     }
                  }
               }

               int var25 = 1;
               if(automatischerEinbehaltArt == 2) {
                  if(kID == 1) {
                     TabelleStatistikEinsatz var26 = new TabelleStatistikEinsatz();
                     var25 = AbrechnungService.aufrunden(var26.getDauer(vID));
                  } else if(kID == 3) {
                     TabelleStatistikbsw var27 = new TabelleStatistikbsw();
                     var25 = AbrechnungService.aufrunden(var27.getDauer(vID));
                  } else {
                     TabelleStatistikSonstigeVeranstaltung var28 = new TabelleStatistikSonstigeVeranstaltung();
                     var25 = AbrechnungService.aufrunden(var28.getDauer(vID));
                  }
               }

               for(int var29 = 0; var29 < e.length; ++var29) {
                  abrechnung.setId(tabAbrechnung.getNextNummer());
                  abrechnung.setAbrechnungID(0);
                  abrechnung.setArtikelID(artID);
                  abrechnung.setDatum(buchungstag);
                  abrechnung.setJahr(jahr);
                  abrechnung.setMitgliedID(e[var29]);
                  abrechnung.setStatus(0);
                  abrechnung.setVeranstaltungID(vID);
                  abrechnung.setVeranstaltungKategorie(kID);
                  abrechnung.setMenge(menge);
                  abrechnung.setWert(wert);
                  abrechnung.setZahlungsart(zahlungsart);
                  abrechnung.setBuchungskonto(buchungskonto);
                  abrechnung.setUmbuchungID(0);
                  tabAbrechnung.insert(abrechnung);
                  if(automatischerEinbehaltArt == 1 | automatischerEinbehaltArt == 2 && automatischerEinbehalt != 0) {
                     abrechnung.setId(tabAbrechnung.getNextNummer());
                     abrechnung.setAbrechnungID(0);
                     abrechnung.setArtikelID(artID);
                     abrechnung.setDatum(buchungstag);
                     abrechnung.setJahr(jahr);
                     abrechnung.setMitgliedID(e[var29]);
                     abrechnung.setStatus(0);
                     abrechnung.setVeranstaltungID(vID);
                     abrechnung.setVeranstaltungKategorie(kID);
                     abrechnung.setMenge(1);
                     if(automatischerEinbehaltArt == 2) {
                        abrechnung.setWert(automatischerEinbehalt * var25);
                     } else {
                        abrechnung.setWert(automatischerEinbehalt);
                     }

                     abrechnung.setZahlungsart(1);
                     abrechnung.setBuchungskonto(buchungskonto);
                     abrechnung.setUmbuchungID(0);
                     tabAbrechnung.insert(abrechnung);
                  }
               }

               logging.logInfo("Abrechnung erstellt");
            } catch (SQLException var21) {
               logging.logPrintStackTrace(var21);
            } catch (ArithmeticException var22) {
               ;
            } catch (DocumentException var23) {
               logging.logPrintStackTrace(var23);
            } catch (IOException var24) {
               logging.logPrintStackTrace(var24);
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
      if(berechnungsart == 1) {
         boolean anzahlStunden = false;
         int anzahlStunden1;
         if(kID == 1) {
            TabelleStatistikEinsatz tabStatistikSonstige = new TabelleStatistikEinsatz();
            anzahlStunden1 = aufrunden(tabStatistikSonstige.getDauer(vID));
            if(berechnungsart2 == 1) {
               ergebnis = wert * anzahlStunden1;
            } else if(berechnungsart2 == 2) {
               ergebnis = wert * tabStatistikSonstige.getDauer(vID) / 60;
            }
         } else if(kID == 3) {
            TabelleStatistikbsw tabStatistikSonstige1 = new TabelleStatistikbsw();
            anzahlStunden1 = aufrunden(tabStatistikSonstige1.getDauer(vID));
            if(berechnungsart2 == 1) {
               ergebnis = wert * anzahlStunden1;
            } else if(berechnungsart2 == 2) {
               ergebnis = wert * tabStatistikSonstige1.getDauer(vID) / 60;
            }
         } else {
            TabelleStatistikSonstigeVeranstaltung tabStatistikSonstige2 = new TabelleStatistikSonstigeVeranstaltung();
            anzahlStunden1 = aufrunden(tabStatistikSonstige2.getDauer(vID));
            if(berechnungsart2 == 1) {
               ergebnis = wert * anzahlStunden1;
            } else if(berechnungsart2 == 2) {
               ergebnis = wert * tabStatistikSonstige2.getDauer(vID) / 60;
            }
         }

         if(automatischerEinbehaltArt == 1) {
            ergebnis -= automatischerEinbehalt;
         } else if(automatischerEinbehaltArt == 2) {
            ergebnis -= automatischerEinbehalt * anzahlStunden1;
         }
      } else if(berechnungsart == 2) {
         if(kID == 1) {
            ergebnis = wert;
         } else if(kID == 3) {
            ergebnis = wert;
         } else {
            ergebnis = wert;
         }

         if(automatischerEinbehaltArt == 1) {
            ergebnis -= automatischerEinbehalt;
         }
      }

      logging.logInfo(Integer.valueOf(ergebnis));
      return ergebnis;
   }

   private static int aufrunden(int zeitwert) {
      double zwischenergebnis = (new Double((double)zeitwert)).doubleValue() / 60.0D;
      BigDecimal bd = new BigDecimal(zwischenergebnis);
      bd = bd.setScale(0, 0);
      return bd.intValue();
   }

   public static void rechneVorgangAb(int[] id) throws SQLException, DocumentException, IOException {
      TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
      int abrID = tabAbrechnung.getAbrechnugID();
      String dateiname = runApplication.arbeitsverzeichnis + "data/Abrechnung/ABR_" + abrID + ".pdf";

      for(int i = 0; i < id.length; ++i) {
         tabAbrechnung.updateOffeneVorgaenge(abrID, id[i]);
      }

      Utils.dateiKatalogisieren(dateiname);
      logbuchEingabe.NeuerEintag("Abrechnung " + abrID + " wurde erstellt");
      logging.logInfo("Abrechnung " + abrID + " wurde erstellt");
      PDFAbrechnung.PDFdocumentErstellen(dateiname, abrID);
   }

   public static Vector getAllKontobilanzen(String kontoname) throws SQLException {
      TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
      int einnahmen = tabAbrechnung.getSum(kontoname, 1);
      int ausgaben = tabAbrechnung.getSumWithStatus(kontoname, 2, 1);
      int offene = tabAbrechnung.getSumWithStatus(kontoname, 1, 0);
      int nochZuZahlen = tabAbrechnung.getSumWithStatus(kontoname, 2, 0);
      int summe = einnahmen - ausgaben;
      int summegesamt = summe - offene;
      Vector liste = new Vector();
      liste.add(mapResultSetToVector("EINNAHMEN GESAMT", MoneyCalculation.parseMoneyVauleForGUI(einnahmen)));
      liste.add(mapResultSetToVector("AUSGABEN GESAMT", MoneyCalculation.parseMoneyVauleForGUI(ausgaben)));
      liste.add(mapResultSetToVector("ZWISCHEN SUMME", MoneyCalculation.parseMoneyVauleForGUI(summe)));
      liste.add(mapResultSetToVector("OFFENE POSTEN", MoneyCalculation.parseMoneyVauleForGUI(offene)));
      liste.add(mapResultSetToVector("SUMME GESAMT", MoneyCalculation.parseMoneyVauleForGUI(summegesamt)));
      liste.add(mapResultSetToVector("NOCH ZU ZAHLEN", MoneyCalculation.parseMoneyVauleForGUI(nochZuZahlen)));
      logging.logSQL(liste);
      return liste;
   }

   private static Vector mapResultSetToVector(String text, String value) {
      Vector liste = new Vector();
      if(text.equals("AUSGABEN GESAMT") | text.equals("OFFENE POSTEN") | text.equals("NOCH ZU ZAHLEN")) {
         liste.add(text);
         if(!value.equals("0,00")) {
            liste.add("-" + value + "€");
         } else {
            liste.add(value + "€");
         }
      } else {
         liste.add(text);
         liste.add(value + "€");
      }

      return liste;
   }
}
