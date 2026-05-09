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
package pdfdocumente.maengelmeldung;

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
import data.tabellen.TabelleFahrzeug;
import data.tabellen.mitglied.TabelleMitglied;
import go.M\u00e4ngelmeldung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class MaengelmeldungPDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, M\u00e4ngelmeldung mangel, String fahrzeugName) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
        Phrase textblock = new Phrase();
        Phrase textblock2 = new Phrase();
        PdfPTable table = null;
        table = new PdfPTable(2);
        try {
            Chunk chunk = new Chunk("M\u00c4NGELMELDUNG", UEBERSCHRIFT);
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
            if (mangel.getFahrzeugID() != 0) {
                chunk = new Chunk("Fahrzeug: ", NORMAL_BOLD);
                textblock.add((Element)chunk);
                chunk = new Chunk(tabFahrzeug.getFahrzeugName(mangel.getFahrzeugID()), NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                chunk = new Chunk("Kennzeichen: ", NORMAL_BOLD);
                textblock.add((Element)chunk);
                chunk = new Chunk(tabFahrzeug.getKennezeichen(mangel.getFahrzeugID()), NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                chunk = new Chunk("Funkrufname: ", NORMAL_BOLD);
                textblock.add((Element)chunk);
                chunk = new Chunk(tabFahrzeug.getFunkrufname(mangel.getFahrzeugID()), NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
            } else {
                chunk = new Chunk("Art / Ort: ", NORMAL_BOLD);
                textblock.add((Element)chunk);
                chunk = new Chunk(fahrzeugName, NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
            }
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Wann ist der Mangel Aufgetreten (Veranstaltung, Einsatz, \u00dcbung):", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk(mangel.getWann(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Wer hat den Mangel festgestellt: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk(String.valueOf(tabMitglied.getName(mangel.getMitgliedID())) + ", " + tabMitglied.getVorname(mangel.getMitgliedID()), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Detaillierte Mangelbeschreibung: ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk(mangel.getBeschreibung(), NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("__________________", NORMAL_FONT);
            textblock2.add((Element)chunk);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("    Unterschrift", NORMAL_FONT);
            textblock2.add((Element)chunk);
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        document.add((Element)new Paragraph(textblock));
        document.add((Element)table);
        document.add((Element)new Paragraph(textblock2));
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

    public static void PDFdocumentErstellen(String filename, M\u00e4ngelmeldung mangel, String fahrzeugName) throws DocumentException, IOException, SQLException {
        MaengelmeldungPDFSchreiben ps = new MaengelmeldungPDFSchreiben();
        ps.createPdf(filename, mangel, fahrzeugName);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

