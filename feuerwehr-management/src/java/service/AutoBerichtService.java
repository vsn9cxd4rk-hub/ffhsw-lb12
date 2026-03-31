package service;

import ao.statistik.AbwesenheitsStatistikAO;
import ao.statistik.AnwesenheitBrandsicherheitswachenAO;
import ao.statistik.AnwesenheitDienstabendAO;
import ao.statistik.AnwesenheitEinsatzAO;
import ao.statistik.AnwesenheitGesamtAO;
import ao.statistik.AusrueckezeitenAO;
import ao.statistik.BswMannstundenAO;
import ao.statistik.EinsatzArtAO;
import ao.statistik.EinsatzMannstundenAO;
import ao.statistik.EinsatzProMonatAO;
import ao.statistik.EinsatzProStundeAO;
import ao.statistik.EinsatzProWochentagAO;
import data.tabellen.TabelleJahresbericht;
import data.tabellen.einstellungen.TabelleEinstellungen;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import go.Jahresbericht;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import pdfdocumente.BerichtPDFSchreiben;
import run.runApplication;
import utilities.MyChartUtils;
import utilities.SbcUtils;
import utilities.Utils;

public class AutoBerichtService {

   public static void AutoBericht() {
      try {
         if(((String)runApplication.EINSTELLUNGEN.get("letzterAutoBericht")).equals("0")) {
            logging.logInfo("Update Einstellungstabelle mit aktueller Zeit");
            (new TabelleEinstellungen()).update("letzterAutoBericht", Long.toString(System.currentTimeMillis()));
         } else {
            long e = (long)(86400 * Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("ZeitAutoBericht"))) * 1000L;
            long value = System.currentTimeMillis() - e;
            if(Long.parseLong((String)runApplication.EINSTELLUNGEN.get("letzterAutoBericht")) <= value) {
               logging.logInfo("AutoBericht muss erstellt werden");
               erstelleBericht();
               (new TabelleEinstellungen()).update("letzterAutoBericht", Long.toString(System.currentTimeMillis()));
               logging.logInfo("AutoBericht wurde erstellt und Datenbank aktualiesiert");
            } else {
               logging.logInfo("AutoBericht muss NICHT erstellt werden");
            }
         }
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }

   private static void erstelleBericht() {
      TabelleJahresbericht tabBericht = new TabelleJahresbericht();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
      Jahresbericht bericht = new Jahresbericht();

      try {
         String[] e = Utils.listToArray(tabGruppe.getAllGruppen());
         String pdfDateiname = "AutomatischerBericht_" + tabGruppe.getID(e[0]) + "_" + SbcUtils.timeStamp("dd.MM.yyyy") + ".pdf";
         int jahr = Integer.parseInt(SbcUtils.timeStamp("yyyy"));
         int mGruppe = tabGruppe.getID(e[0]);
         String datum = SbcUtils.timeStamp("dd.MM.yyyy");
         String title = "Automatischer erstellter Bericht vom " + datum;
         String text = "Dieser Bericht wird durch das Programm Automatisch alle " + (String)runApplication.EINSTELLUNGEN.get("ZeitAutoBericht") + " Tage erstellt.\n\nDer Bericht dient zur Moment Aufnahme des Aktuellen Statistik Status.\n\nAktuelle Uhrzeit:\n" + SbcUtils.timeStamp("EEEE\',\'  dd.MM.yyyy  HH:mm:ss");
         bericht.setId(tabBericht.getNextNummer());
         bericht.setJahr(jahr);
         bericht.setTitle(title);
         bericht.setBericht(text);
         bericht.setErstelldatum(SbcUtils.timeStamp("yyyy-MM-dd"));
         bericht.setAutoBericht(1);
         bericht.setStatistiken("leer");
         bericht.setDateiname(pdfDateiname);
         bericht.setMitgliederGruppe(mGruppe);
         String outputfolderTemp = runApplication.arbeitsverzeichnis + "data/" + jahr + "/temp/";
         String[] grafiken = new String[12];
         String[] grafikenBeschreibungen = new String[12];
         MyChartUtils.writeChartToJPEG(AnwesenheitEinsatzAO.createChart(AnwesenheitEinsatzAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Einsatz.jpg");
         grafiken[0] = outputfolderTemp + "Anwesenheit_Einsatz.jpg";
         grafikenBeschreibungen[0] = "Anwesenheit Einsatz";
         MyChartUtils.writeChartToJPEG(AnwesenheitBrandsicherheitswachenAO.createChart(AnwesenheitBrandsicherheitswachenAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Brandsicherheitswache.jpg");
         grafiken[1] = outputfolderTemp + "Anwesenheit_Brandsicherheitswache.jpg";
         grafikenBeschreibungen[1] = "Anwesenheit Brandsicherheitswache";
         MyChartUtils.writeChartToJPEG(AnwesenheitDienstabendAO.createChart(AnwesenheitDienstabendAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Dienst.jpg");
         grafiken[2] = outputfolderTemp + "Anwesenheit_Dienst.jpg";
         grafikenBeschreibungen[2] = "Anwesenheit Dienstabend";
         MyChartUtils.writeChartToJPEG(AnwesenheitGesamtAO.createChart(AnwesenheitGesamtAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Anwesenheit_Gesamt.jpg");
         grafiken[3] = outputfolderTemp + "Anwesenheit_Gesamt.jpg";
         grafikenBeschreibungen[3] = "Anwesenheit Gesamt";
         MyChartUtils.writeChartToJPEG(AusrueckezeitenAO.createChart(AusrueckezeitenAO.createDataset()), 1000, 800, outputfolderTemp + "Ausrückezeit.jpg");
         grafiken[4] = outputfolderTemp + "Ausrückezeit.jpg";
         grafikenBeschreibungen[4] = "Ausrückezeiten";
         MyChartUtils.writeChartToJPEG(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Abwesenheit.jpg");
         grafiken[5] = outputfolderTemp + "Abwesenheit.jpg";
         grafikenBeschreibungen[5] = "Abwesenheitsgründe";
         MyChartUtils.writeChartToJPEG(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "Einsatzart.jpg");
         grafiken[6] = outputfolderTemp + "Einsatzart.jpg";
         grafikenBeschreibungen[6] = "Einsatzart";
         MyChartUtils.writeChartToJPEG(EinsatzMannstundenAO.createChart(EinsatzMannstundenAO.createDataset()), 1000, 800, outputfolderTemp + "EinsatzMannstunden.jpg");
         grafiken[7] = outputfolderTemp + "EinsatzMannstunden.jpg";
         grafikenBeschreibungen[7] = "Einsatz Mannstunden";
         MyChartUtils.writeChartToJPEG(BswMannstundenAO.createChart(BswMannstundenAO.createDataset()), 1000, 800, outputfolderTemp + "BSWMannstunden.jpg");
         grafiken[8] = outputfolderTemp + "BSWMannstunden.jpg";
         grafikenBeschreibungen[8] = "BSW Mannstunden";
         MyChartUtils.writeChartToJPEG(EinsatzProMonatAO.createChart(EinsatzProMonatAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "EinsatzProMonat.jpg");
         grafiken[9] = outputfolderTemp + "EinsatzProMonat.jpg";
         grafikenBeschreibungen[9] = "Einsatz Pro Monat";
         MyChartUtils.writeChartToJPEG(EinsatzProStundeAO.createChart(EinsatzProStundeAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "EinsatzProStunde.jpg");
         grafiken[10] = outputfolderTemp + "EinsatzProStunde.jpg";
         grafikenBeschreibungen[10] = "Einsatz Pro Stunde";
         MyChartUtils.writeChartToJPEG(EinsatzProWochentagAO.createChart(EinsatzProWochentagAO.createDataset(jahr)), 1000, 800, outputfolderTemp + "EinsatzProWochentag.jpg");
         grafiken[11] = outputfolderTemp + "EinsatzProWochentag.jpg";
         grafikenBeschreibungen[11] = "Einsatz Pro Wochentag";
         BerichtPDFSchreiben.PDFdocumentErstellen(runApplication.arbeitsverzeichnis + "data/" + SbcUtils.timeStamp("yyyy") + "/berichte/" + pdfDateiname, title, text, Integer.toString(jahr), grafiken, grafikenBeschreibungen, false, false);
         tabBericht.insert(bericht);
      } catch (IOException var13) {
         logging.logPrintStackTrace(var13);
      }

   }
}
