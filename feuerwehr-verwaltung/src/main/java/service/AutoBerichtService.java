/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.DocumentException
 *  logging.logging
 *  utilities.SbcUtils
 */
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
import com.itextpdf.text.DocumentException;
import data.tabellen.TabelleJahresbericht;
import data.tabellen.einstellungen.TabelleEinstellungen;
import go.Jahresbericht;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import pdfdocumente.BerichtPDFSchreiben;
import run.runApplication;
import utilities.MyChartUtils;
import utilities.SbcUtils;

public class AutoBerichtService {
    public static void AutoBericht() {
        try {
            if (runApplication.EINSTELLUNGEN.get("letzterAutoBericht").equals("0")) {
                logging.logInfo((Object)"Update Einstellungstabelle mit aktueller Zeit");
                new TabelleEinstellungen().update("letzterAutoBericht", Long.toString(System.currentTimeMillis()));
            } else {
                long days = (long)(86400 * Integer.parseInt(runApplication.EINSTELLUNGEN.get("ZeitAutoBericht"))) * 1000L;
                long value = System.currentTimeMillis() - days;
                if (Long.parseLong(runApplication.EINSTELLUNGEN.get("letzterAutoBericht")) <= value) {
                    logging.logInfo((Object)"AutoBericht muss erstellt werden");
                    AutoBerichtService.erstelleBericht();
                    new TabelleEinstellungen().update("letzterAutoBericht", Long.toString(System.currentTimeMillis()));
                    logging.logInfo((Object)"AutoBericht wurde erstellt und Datenbank aktualiesiert");
                } else {
                    logging.logInfo((Object)"AutoBericht muss NICHT erstellt werden");
                }
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private static void erstelleBericht() {
        TabelleJahresbericht tabBericht = new TabelleJahresbericht();
        Jahresbericht bericht = new Jahresbericht();
        try {
            String pdfDateiname = "AutomatischerBericht_" + SbcUtils.timeStamp((String)"dd.MM.yyyy") + ".pdf";
            int jahr = Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"));
            String datum = SbcUtils.timeStamp((String)"dd.MM.yyyy");
            String title = "Automatischer erstellter Bericht vom " + datum;
            String text = "Dieser Bericht wird durch das Programm Automatisch alle " + runApplication.EINSTELLUNGEN.get("ZeitAutoBericht") + " Tage erstellt.\n\nDer Bericht dient zur Moment Aufnahme des Aktuellen Statistik Status.\n\nAktuelle Uhrzeit:\n" + SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy  HH:mm:ss");
            bericht.setId(tabBericht.getNextNummer());
            bericht.setJahr(jahr);
            bericht.setTitle(title);
            bericht.setBericht(text);
            bericht.setErstelldatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
            bericht.setAutoBericht(1);
            bericht.setStatistiken("leer");
            bericht.setDateiname(pdfDateiname);
            String outputfolderTemp = String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/";
            String[] grafiken = new String[12];
            String[] grafikenBeschreibungen = new String[12];
            MyChartUtils.writeChartToJPEG(AnwesenheitEinsatzAO.createChart(AnwesenheitEinsatzAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Einsatz.jpg");
            grafiken[0] = String.valueOf(outputfolderTemp) + "Anwesenheit_Einsatz.jpg";
            grafikenBeschreibungen[0] = "Anwesenheit Einsatz";
            MyChartUtils.writeChartToJPEG(AnwesenheitBrandsicherheitswachenAO.createChart(AnwesenheitBrandsicherheitswachenAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Brandsicherheitswache.jpg");
            grafiken[1] = String.valueOf(outputfolderTemp) + "Anwesenheit_Brandsicherheitswache.jpg";
            grafikenBeschreibungen[1] = "Anwesenheit Brandsicherheitswache";
            MyChartUtils.writeChartToJPEG(AnwesenheitDienstabendAO.createChart(AnwesenheitDienstabendAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Dienst.jpg");
            grafiken[2] = String.valueOf(outputfolderTemp) + "Anwesenheit_Dienst.jpg";
            grafikenBeschreibungen[2] = "Anwesenheit Dienstabend";
            MyChartUtils.writeChartToJPEG(AnwesenheitGesamtAO.createChart(AnwesenheitGesamtAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Anwesenheit_Gesamt.jpg");
            grafiken[3] = String.valueOf(outputfolderTemp) + "Anwesenheit_Gesamt.jpg";
            grafikenBeschreibungen[3] = "Anwesenheit Gesamt";
            MyChartUtils.writeChartToJPEG(AusrueckezeitenAO.createChart(AusrueckezeitenAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "Ausr\u00fcckezeit.jpg");
            grafiken[4] = String.valueOf(outputfolderTemp) + "Ausr\u00fcckezeit.jpg";
            grafikenBeschreibungen[4] = "Ausr\u00fcckezeiten";
            MyChartUtils.writeChartToJPEG(AbwesenheitsStatistikAO.createChart(AbwesenheitsStatistikAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Abwesenheit.jpg");
            grafiken[5] = String.valueOf(outputfolderTemp) + "Abwesenheit.jpg";
            grafikenBeschreibungen[5] = "Abwesenheitsgr\u00fcnde";
            MyChartUtils.writeChartToJPEG(EinsatzArtAO.createChart(EinsatzArtAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "Einsatzart.jpg");
            grafiken[6] = String.valueOf(outputfolderTemp) + "Einsatzart.jpg";
            grafikenBeschreibungen[6] = "Einsatzart";
            MyChartUtils.writeChartToJPEG(EinsatzMannstundenAO.createChart(EinsatzMannstundenAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzMannstunden.jpg");
            grafiken[7] = String.valueOf(outputfolderTemp) + "EinsatzMannstunden.jpg";
            grafikenBeschreibungen[7] = "Einsatz Mannstunden";
            MyChartUtils.writeChartToJPEG(BswMannstundenAO.createChart(BswMannstundenAO.createDataset()), 1000, 800, String.valueOf(outputfolderTemp) + "BSWMannstunden.jpg");
            grafiken[8] = String.valueOf(outputfolderTemp) + "BSWMannstunden.jpg";
            grafikenBeschreibungen[8] = "BSW Mannstunden";
            MyChartUtils.writeChartToJPEG(EinsatzProMonatAO.createChart(EinsatzProMonatAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzProMonat.jpg");
            grafiken[9] = String.valueOf(outputfolderTemp) + "EinsatzProMonat.jpg";
            grafikenBeschreibungen[9] = "Einsatz Pro Monat";
            MyChartUtils.writeChartToJPEG(EinsatzProStundeAO.createChart(EinsatzProStundeAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzProStunde.jpg");
            grafiken[10] = String.valueOf(outputfolderTemp) + "EinsatzProStunde.jpg";
            grafikenBeschreibungen[10] = "Einsatz Pro Stunde";
            MyChartUtils.writeChartToJPEG(EinsatzProWochentagAO.createChart(EinsatzProWochentagAO.createDataset(jahr)), 1000, 800, String.valueOf(outputfolderTemp) + "EinsatzProWochentag.jpg");
            grafiken[11] = String.valueOf(outputfolderTemp) + "EinsatzProWochentag.jpg";
            grafikenBeschreibungen[11] = "Einsatz Pro Wochentag";
            BerichtPDFSchreiben.PDFdocumentErstellen(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + SbcUtils.timeStamp((String)"yyyy") + "/berichte/" + pdfDateiname, title, text, Integer.toString(jahr), grafiken, grafikenBeschreibungen, false, false);
            tabBericht.insert(bericht);
        }
        catch (DocumentException | IOException | SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

