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
package pdfdocumente.verdienstausfallbescheinigung;

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
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_arbeit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class VerdienstausfallbescheinigungPDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, String veranstaltung, int mID, String am, String bis, String vonUhr, String bisUhr) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleMitglieder_arbeit tabArbeit = new TabelleMitglieder_arbeit();
        Phrase textblock = new Phrase();
        Phrase textblock2 = new Phrase();
        PdfPTable table = null;
        table = new PdfPTable(2);
        try {
            Chunk chunk = new Chunk("VERDIENSTAUSFALLBESCHEINIGUNG   ", UEBERSCHRIFT);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Erstellt: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk(String.valueOf(runApplication.EINSTELLUNGEN.get("Stadt")) + ", den " + SbcUtils.timeStamp((String)"dd.MM.yyyy"), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Bezeichung der Veranstaltung / des Lehrganges /des Einsatzes:", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk(veranstaltung, NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("ARBEITGEBERADRESSE", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Firma: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk(tabArbeit.getName(mID), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Anschrift: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk(tabArbeit.getOrt(mID), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Telefon: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk(tabArbeit.getTelefon(mID), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Bankverbindung:    ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk("_______________________________________________________", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("ARBEITNEHMERDATEN", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Name: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk(String.valueOf(tabMitglied.getName(mID)) + ", " + tabMitglied.getVorname(mID), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Anschrift: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk(String.valueOf(tabMitglied.getStrasse(mID)) + ", " + tabMitglied.getOrt(mID), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Geburtsdatum: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            chunk = new Chunk(TimeCalculation.parseDateForGUI(tabMitglied.getGebDatum(mID)), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Dienst / Berufsbezeichnung:    ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            String beruf = tabMitglied.getBeruf(mID);
            chunk = beruf.equals("") ? new Chunk("_______________________________", NORMAL_FONT) : new Chunk(beruf, NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("VERANSTALTUNGSDETAILS", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            table.addCell("am/vom (Datum):\nbis (Datum):\nvon (Uhrzeit)\nbis (Uhrzeit):");
            table.addCell(String.valueOf(am) + "\n" + bis + "\n" + vonUhr + "\n" + bisUhr);
            Chunk chunk2 = new Chunk("Stunden w\u00f6chentl.:       ", NORMAL_BOLD);
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Arbeitszeitbeginn:          ", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Arbeitszeit Ende:            ", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Tage w\u00f6chentlich:           ", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Es wird um Erstattung der fortgew\u00e4hrten Leistungen f\u00fcr die Zeit des Arbeitsausfalles gebeten: ", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Vertragsgem\u00e4\u00df gezahlt in Euro: ", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Betrag in Euro:                             ", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Ausfallstunnden:                          ", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Angaben pro Stunde in Euro:      ", NORMAL_BOLD);
            textblock2.add((Element)chunk2);
            chunk2 = new Chunk("_______________________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("___________________                              __________________", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("   Firmenstempel                                               Unterschrift", NORMAL_FONT);
            textblock2.add((Element)chunk2);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        document.add((Element)new Paragraph(textblock));
        document.add((Element)table);
        document.add((Element)new Paragraph(textblock2));
        document.addTitle("FeuerwehrManagementSystem - Verdienstausfall");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Einsatzbericht");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String filename, String veranstaltung, int mID, String am, String bis, String vonUhr, String bisUhr) throws DocumentException, IOException, SQLException {
        VerdienstausfallbescheinigungPDFSchreiben ps = new VerdienstausfallbescheinigungPDFSchreiben();
        ps.createPdf(filename, veranstaltung, mID, am, bis, vonUhr, bisUhr);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

