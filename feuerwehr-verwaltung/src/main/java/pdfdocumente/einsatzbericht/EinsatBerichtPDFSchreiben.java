/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.Chunk
 *  com.itextpdf.text.Document
 *  com.itextpdf.text.DocumentException
 *  com.itextpdf.text.Element
 *  com.itextpdf.text.Font
 *  com.itextpdf.text.Font$FontFamily
 *  com.itextpdf.text.Image
 *  com.itextpdf.text.PageSize
 *  com.itextpdf.text.Paragraph
 *  com.itextpdf.text.Phrase
 *  com.itextpdf.text.pdf.PdfPTable
 *  com.itextpdf.text.pdf.PdfWriter
 *  logging.logging
 *  utilities.SbcUtils
 */
package pdfdocumente.einsatzbericht;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import data.tabellen.TabelleEinsatz_bericht_elemente;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.mitglied.TabelleMitglied;
import go.EinsatzBerichtDaten;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class EinsatBerichtPDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, EinsatzBerichtDaten berichtDaten, HashMap<String, String> map) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
        TabelleStichwort tabStichwort = new TabelleStichwort();
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleEinsatz_bericht_elemente tabElemente = new TabelleEinsatz_bericht_elemente();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        Phrase textblock = new Phrase();
        Phrase textblock2 = new Phrase();
        Phrase textblock3 = new Phrase();
        Phrase textblock4 = new Phrase();
        PdfPTable table = null;
        try {
            int eingesetzteFahrzeuge = tabZeiten.getCountGesamtFahrzeuge(berichtDaten.getVeranstaltungID());
            table = new PdfPTable(eingesetzteFahrzeuge + 1);
            String[] einsatzArtListe = Utils.listToArray(tabElemente.getElemente("EinsatzArt"));
            String[] stelleListe = Utils.listToArray(tabElemente.getElemente("Stelle"));
            String[] objektListe = Utils.listToArray(tabElemente.getElemente("Objekt"));
            String[] ausdehungListe = Utils.listToArray(tabElemente.getElemente("Ausdehnung"));
            Chunk chunk = new Chunk("EINSATZBERICHT   ", UEBERSCHRIFT);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)chunk);
            chunk = new Chunk(tabVeranstaltung.getVeranstaltungName2(berichtDaten.getVeranstaltungID()), UEBERSCHRIFT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Einsatz Nummer: " + map.get("einsatznummerOffiziell"), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Datum: " + TimeCalculation.parseDateForGUI(map.get("Datum")), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Ort: " + runApplication.EINSTELLUNGEN.get("Stadt"), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Stra\u00dfe: " + map.get("Ort"), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Stadtteil: " + map.get("stadtteil"), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Stichwort: " + tabStichwort.getStichwortName(Integer.parseInt(map.get("Stichwort"))), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Fahrzeuge Im Einsatz: " + map.get("Fahrzeug"), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            if (runApplication.EINSTELLUNGEN.get("einsatzleiterBF").equals("1")) {
                chunk = new Chunk("1. Gruppenf\u00fchrer FF: " + tabMitglied.getName(Integer.parseInt(map.get("einsatzleiter"))) + ", " + tabMitglied.getVorname(Integer.parseInt(map.get("einsatzleiter"))), NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                if (map.get("einsatzleiterBF").equals("")) {
                    chunk = new Chunk("Einsatzleiter BF:  (-)", NORMAL_FONT);
                    textblock.add((Element)chunk);
                    textblock.add((Element)Chunk.NEWLINE);
                } else {
                    chunk = new Chunk("Einsatzleiter BF: " + map.get("einsatzleiterBF"), NORMAL_FONT);
                    textblock.add((Element)chunk);
                    textblock.add((Element)Chunk.NEWLINE);
                }
            } else {
                chunk = new Chunk("Einsatzleiter: " + tabMitglied.getName(Integer.parseInt(map.get("einsatzleiter"))) + ", " + tabMitglied.getVorname(Integer.parseInt(map.get("einsatzleiter"))), NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                textblock.add((Element)Chunk.NEWLINE);
            }
            chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Einsatz Art:", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            String einsatzArtName = tabElemente.getName(berichtDaten.getEinsatzArt());
            int itemCounter = 0;
            int i = 0;
            while (i < einsatzArtListe.length) {
                if (itemCounter == 4) {
                    textblock.add((Element)Chunk.NEWLINE);
                    itemCounter = 0;
                }
                if (einsatzArtName.equals(einsatzArtListe[i])) {
                    chunk = new Chunk(" (X) ", NORMAL_BOLD);
                    textblock.add((Element)chunk);
                } else {
                    chunk = new Chunk(" (_) ", NORMAL_FONT);
                    textblock.add((Element)chunk);
                }
                chunk = new Chunk(einsatzArtListe[i], NORMAL_FONT);
                textblock.add((Element)chunk);
                chunk = new Chunk("   ", NORMAL_FONT);
                textblock.add((Element)chunk);
                ++itemCounter;
                ++i;
            }
            itemCounter = 0;
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Stelle des Einsatzes:", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            String stelleName = tabElemente.getName(berichtDaten.getStelle());
            int i2 = 0;
            while (i2 < stelleListe.length) {
                if (itemCounter == 4) {
                    textblock.add((Element)Chunk.NEWLINE);
                    itemCounter = 0;
                }
                if (stelleName.equals(stelleListe[i2])) {
                    chunk = new Chunk(" (X) ", NORMAL_BOLD);
                    textblock.add((Element)chunk);
                } else {
                    chunk = new Chunk(" (_) ", NORMAL_FONT);
                    textblock.add((Element)chunk);
                }
                chunk = new Chunk(stelleListe[i2], NORMAL_FONT);
                textblock.add((Element)chunk);
                chunk = new Chunk("   ", NORMAL_FONT);
                textblock.add((Element)chunk);
                ++itemCounter;
                ++i2;
            }
            itemCounter = 0;
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Objekt:", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            String objektName = tabElemente.getName(berichtDaten.getObjekt());
            int i3 = 0;
            while (i3 < objektListe.length) {
                if (itemCounter == 3) {
                    textblock.add((Element)Chunk.NEWLINE);
                    itemCounter = 0;
                }
                if (objektName.equals(objektListe[i3])) {
                    chunk = new Chunk(" (X) ", NORMAL_BOLD);
                    textblock.add((Element)chunk);
                } else {
                    chunk = new Chunk(" (_) ", NORMAL_FONT);
                    textblock.add((Element)chunk);
                }
                chunk = new Chunk(objektListe[i3], NORMAL_FONT);
                textblock.add((Element)chunk);
                chunk = new Chunk("   ", NORMAL_FONT);
                textblock.add((Element)chunk);
                ++itemCounter;
                ++i3;
            }
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Eigent\u00fcmer / Gesch\u00e4digter:", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Name:  " + berichtDaten.getEigentuemerName(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Anschrift:  " + berichtDaten.getEigentuemerAnschrift(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Telefon:  " + berichtDaten.getEigentuemerTelefon(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Verursacher:", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Name:  " + berichtDaten.getVerursacherName(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Anschrift:  " + berichtDaten.getVerursacherAnschrift(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Telefon:  " + berichtDaten.getVerursacherTelefon(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            Chunk chunk2 = new Chunk("Alamierung:", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Erfolgt um:   " + map.get("ZeitAlarm") + " Uhr", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Erfolgt \u00fcber: " + tabElemente.getName(berichtDaten.getAlamierung()), NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Meldender:", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Name: " + berichtDaten.getMeldenderName(), NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Anschrift: " + berichtDaten.getMeldenderAnschrift(), NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Telefon: " + berichtDaten.getMeldenderTelefon(), NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Eingesetzte Fahrzeuge:", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)Chunk.NEWLINE);
            table.addCell("Name:\n\nAusger\u00fcckt:\nEingetroffen:\nEinger\u00fcckt:\n\nGesamtzeit:");
            int[] fahrzeugID = Utils.listToIntArray(tabZeiten.getFahrzeugListe(berichtDaten.getVeranstaltungID()));
            int f = 0;
            while (f < fahrzeugID.length) {
                HashMap<String, String> mapZeiten = tabZeiten.getData(berichtDaten.getVeranstaltungID(), fahrzeugID[f]);
                table.addCell(String.valueOf(tabFahrzeug.getFahrzeugName(Integer.parseInt(mapZeiten.get("fahrzeugID")))) + "\n\n" + mapZeiten.get("zeitAusgerueckt") + "\n" + mapZeiten.get("zeitEingetroffen") + "\n" + mapZeiten.get("zeitEingerueckt") + "\n\n" + TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration(mapZeiten.get("zeitAlarm"), mapZeiten.get("zeitEingerueckt"))));
                ++f;
            }
            textblock3.add((Element)Chunk.NEWLINE);
            Chunk chunk3 = new Chunk("Gesamtst\u00e4rke:", NORMAL_BOLD);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            if (map.get("staerkeZF").equals("0")) {
                chunk3 = new Chunk("Einsatzgesamtst\u00e4rke: " + map.get("staerkeGF") + " / " + map.get("staerkeFM"), NORMAL_FONT);
                textblock3.add((Element)chunk3);
                textblock3.add((Element)Chunk.NEWLINE);
            } else {
                chunk3 = new Chunk("Einsatzgesamtst\u00e4rke: " + map.get("staerkeZF") + " / " + map.get("staerkeGF") + " / " + map.get("staerkeFM"), NORMAL_FONT);
                textblock3.add((Element)chunk3);
                textblock3.add((Element)Chunk.NEWLINE);
            }
            chunk3 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk("Vorgefundene Lage:", NORMAL_BOLD);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk(berichtDaten.getLage(), NORMAL_FONT);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk("Verlauf des Einsatzes:", NORMAL_BOLD);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk(berichtDaten.getVerlauf(), NORMAL_FONT);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk("Eingesetzte Ger\u00e4te und Mittel:", NORMAL_BOLD);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk(berichtDaten.getEingesetzteGeraete(), NORMAL_FONT);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)Chunk.NEWLINE);
            Chunk chunk4 = new Chunk("Verbrauch:", NORMAL_BOLD);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Verbrauch Wasser: " + berichtDaten.getVerbrauchWasser(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Verbrauch Pulver: " + berichtDaten.getVerbrauchPulver(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Verbrauch Schaum: " + berichtDaten.getVerbrauchSchaum(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Verbrauch Bindemittel: " + berichtDaten.getVerbrauchBindemittel(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Ger\u00e4te im Detail:", NORMAL_BOLD);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Tragbare Leitern: " + berichtDaten.getTragbareLeitern(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Atemschutzger\u00e4t: " + berichtDaten.getAtemschutzgeraet(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Fluchthauben: " + berichtDaten.getFluchthauben(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Bel\u00fcftungsger\u00e4t: " + berichtDaten.getBelueftungsgeraet(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Hydraul. Rettungsger\u00e4t: " + berichtDaten.getRettungsgeraet(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            itemCounter = 0;
            chunk4 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Ausdehnung:", NORMAL_BOLD);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            String ausdehnungName = tabElemente.getName(berichtDaten.getAusdehnung());
            int i4 = 0;
            while (i4 < ausdehungListe.length) {
                if (itemCounter == 1) {
                    textblock4.add((Element)Chunk.NEWLINE);
                    itemCounter = 0;
                }
                if (ausdehnungName.equals(ausdehungListe[i4])) {
                    chunk4 = new Chunk(" (X) ", NORMAL_BOLD);
                    textblock4.add((Element)chunk4);
                } else {
                    chunk4 = new Chunk(" (_) ", NORMAL_FONT);
                    textblock4.add((Element)chunk4);
                }
                chunk4 = new Chunk(ausdehungListe[i4], NORMAL_FONT);
                textblock4.add((Element)chunk4);
                chunk4 = new Chunk("   ", NORMAL_FONT);
                textblock4.add((Element)chunk4);
                ++itemCounter;
                ++i4;
            }
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Schaden:", NORMAL_BOLD);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Verletzte: " + berichtDaten.getVerletzte(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Gerettete: " + berichtDaten.getGerettete(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Tote: " + berichtDaten.getTote(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Sachschaden: " + berichtDaten.getSchadenhoehe(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Brandwache:", NORMAL_BOLD);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            String brandwacheFahrzeugName = tabFahrzeug.getFahrzeugName(berichtDaten.getBrandwacheFahrzeug());
            if (brandwacheFahrzeugName == null) {
                chunk4 = new Chunk("Fahrzeug:  (-)", NORMAL_FONT);
                textblock4.add((Element)chunk4);
                textblock4.add((Element)Chunk.NEWLINE);
            } else {
                chunk4 = new Chunk("Fahrzeug: " + brandwacheFahrzeugName, NORMAL_FONT);
                textblock4.add((Element)chunk4);
                textblock4.add((Element)Chunk.NEWLINE);
            }
            chunk4 = new Chunk("St\u00e4rke: " + berichtDaten.getStaerke(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Dauer: " + berichtDaten.getDauer(), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            textblock4.add((Element)Chunk.NEWLINE);
            textblock4.add((Element)Chunk.NEWLINE);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("________________________________", NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Unterschrift: " + tabMitglied.getEinsatzleiter(Integer.parseInt(map.get("einsatzleiter"))), NORMAL_FONT);
            textblock4.add((Element)chunk4);
            textblock4.add((Element)Chunk.NEWLINE);
            chunk4 = new Chunk("Erstellt am: " + SbcUtils.timeStamp((String)"EEEE','  dd.MM.yyyy  HH:mm:ss"), NORMAL_FONT);
            textblock4.add((Element)chunk4);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        document.add((Element)new Paragraph(textblock));
        document.newPage();
        document.add((Element)new Paragraph(textblock2));
        document.add((Element)table);
        document.add((Element)new Paragraph(textblock3));
        document.newPage();
        document.add((Element)new Paragraph(textblock4));
        document.addTitle("FeuerwehrManagementSystem - Einsatz Bericht");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Einsatzbericht");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String filename, EinsatzBerichtDaten berichtDaten, HashMap<String, String> map) throws DocumentException, IOException, SQLException {
        EinsatBerichtPDFSchreiben ps = new EinsatBerichtPDFSchreiben();
        ps.createPdf(filename, berichtDaten, map);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

