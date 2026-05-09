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
 *  com.itextpdf.text.pdf.PdfWriter
 *  logging.logging
 *  utilities.SbcUtils
 */
package pdfdocumente.mitgliedakte;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import data.tabellen.TabelleFahrzeug_beschreibung;
import data.tabellen.TabelleFahrzeug_untersuchung;
import data.tabellen.TabelleGeraetepruefung;
import go.Fahrzeug;
import go.Fahrzeug_Untersuchung;
import go.Geraetepruefung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class PDFFahrzeugInfo {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 16.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, Fahrzeug fahrzeug) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        TabelleFahrzeug_beschreibung tabBeschreibung = new TabelleFahrzeug_beschreibung();
        TabelleGeraetepruefung tabGeraetepruefung = new TabelleGeraetepruefung();
        TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase textblock = new Phrase();
        Chunk chunk = new Chunk("                                                                                                   " + runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp((String)"dd.MM.yyyy"), NORMAL_FONT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        chunk = new Chunk("                                                                                                   Editiert durch: " + runApplication.loginName, NORMAL_FONT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        document.add((Element)new Paragraph(textblock));
        Phrase personal = new Phrase();
        Chunk address = new Chunk("Name: " + fahrzeug.getName(), UEBERSCHRIFT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        personal.add((Element)Chunk.NEWLINE);
        address = new Chunk("Fahrzeugdaten", NORMAL_BOLD);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Beschreibung: " + tabBeschreibung.getBeschreibungName(fahrzeug.getBeschreibung()), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("KFZ-Kennzeichen: " + fahrzeug.getKennzeichen(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Funkrufname: " + fahrzeug.getFunkrufname(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Ausr\u00fcckesortierung: " + fahrzeug.getSortierung(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Sitzpl\u00e4tze: " + fahrzeug.getSitzplaetze(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Maximale Besatzung: " + fahrzeug.getMaxBesatzung(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Minimale Besatzung: " + fahrzeug.getMinBesatzung(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = fahrzeug.getAnhaenger() == 0 ? new Chunk("Ist das Fahrzeug ein Anh\u00e4nger: 'NEIN'", NORMAL_FONT) : new Chunk("Ist das Fahrzeug ein Anh\u00e4nger: 'JA'", NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)Chunk.NEWLINE);
        address = new Chunk("Fahrzeugdaten - Wartung", NORMAL_BOLD);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        if (tabGeraetepruefung.getCount(fahrzeug.getId()) == 0) {
            address = new Chunk("keine Wartungsdaten eingetragen!", NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            logging.logInfo((Object)"Keine Ger\u00e4tewartung gefunden...");
        } else {
            Geraetepruefung geraet = tabGeraetepruefung.getData(fahrzeug.getId());
            address = new Chunk("Wartung Stromerzeuger: " + TimeCalculation.parseDateForGUI(geraet.getStromerzeuger()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Steckleiter: " + TimeCalculation.parseDateForGUI(geraet.getSteckleiter()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Schiebleiter: " + TimeCalculation.parseDateForGUI(geraet.getSchiebleiter()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Hydralikaggregat: " + TimeCalculation.parseDateForGUI(geraet.getHydraulik()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Pumpe: " + TimeCalculation.parseDateForGUI(geraet.getPumpe()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Kettens\u00e4ge: " + TimeCalculation.parseDateForGUI(geraet.getKettensaege()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Doppelkanister: " + TimeCalculation.parseDateForGUI(geraet.getDoppelkanister()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Ger\u00e4tepr\u00fcfung Allgemein: " + TimeCalculation.parseDateForGUI(geraet.getGeraetepruefung_allgm()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Wartung Absturzsicherungsset: " + TimeCalculation.parseDateForGUI(geraet.getAbstusiset()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            logging.logInfo((Object)"Ger\u00e4tewartung wurde hinzugef\u00fcgt...");
        }
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)Chunk.NEWLINE);
        address = new Chunk("Fahrzeugdaten - Untersuchung", NORMAL_BOLD);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        if (tabUntersuchung.getCount(fahrzeug.getId()) == 0) {
            address = new Chunk("keine Fahrzeuguntersuchungen eingetragen!", NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            logging.logInfo((Object)"Keine Fahrzeuguntersuchungen gefunden...");
        } else {
            Fahrzeug_Untersuchung untersuchung = tabUntersuchung.getData(fahrzeug.getId());
            address = new Chunk("N\u00e4chster T\u00fcv Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung.getT\u00fcv()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("N\u00e4chster SP Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung.getSp()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("N\u00e4chster Service Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung.getService()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("N\u00e4chster Gaswartung Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung.getGaswartung()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            logging.logInfo((Object)"Fahrzuguntersuchung wurde hinzugef\u00fcgt...");
        }
        document.add((Element)new Paragraph(personal));
        document.addTitle("FeuerwehrManagementSystem - FahrzeugInfo");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("FahrzeugInfo");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, Fahrzeug fahrzeug) throws DocumentException, IOException {
        PDFFahrzeugInfo ps = new PDFFahrzeugInfo();
        try {
            ps.createPdf(dateiname, fahrzeug);
            ps.printMeasures();
            logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

