/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.BadElementException
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
package pdfdocumente.abrechnung;

import com.itextpdf.text.BadElementException;
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
import data.tabellen.abrechnung.TabelleAbrechnung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.Utils;

public class PDFAbrechnung {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);

    public void createPdf(String filename, int abrechnungID) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        String datum = SbcUtils.timeStamp((String)"dd.MM.yyyy");
        String uhrzeit = SbcUtils.timeStamp((String)"HH:mm:ss");
        int seite = 1;
        document.open();
        Phrase textblock = this.banner(abrechnungID, document, datum, uhrzeit, seite);
        TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
        String[] liste = Utils.listToArray(tabAbrechnung.getDataForPDF(abrechnungID));
        int count = 0;
        int i = 0;
        while (i < liste.length) {
            Chunk chunk = new Chunk(liste[i], NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            if (++count == 6) {
                document.add((Element)textblock);
                document.newPage();
                textblock = this.banner(abrechnungID, document, datum, uhrzeit, seite++);
                count = 0;
            }
            ++i;
        }
        document.add((Element)new Paragraph(textblock));
        document.addTitle("FeuerwehrManagementSystem - Abrechnung");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Abrechnung");
        document.close();
        writer.close();
    }

    private Phrase banner(int abrechnungID, Document document, String datum, String uhrzeit, int seite) throws BadElementException, MalformedURLException, IOException, DocumentException {
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase textblock = new Phrase();
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        Chunk chunk = new Chunk("Abrechnung:", UEBERSCHRIFT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk("Datum: " + datum, NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk("Uhrzeit: " + uhrzeit, NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk("Abrechnungsnumer: " + Integer.toString(abrechnungID), NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk(runApplication.EINSTELLUNGEN.get("Name"), NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk(runApplication.EINSTELLUNGEN.get("strasse"), NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk(String.valueOf(runApplication.EINSTELLUNGEN.get("plz")) + " " + runApplication.EINSTELLUNGEN.get("Stadt"), NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk(runApplication.EINSTELLUNGEN.get("telefon"), NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk("Seite: " + seite, NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        return textblock;
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String filename, int abrechnungID) throws DocumentException, IOException, SQLException {
        PDFAbrechnung ps = new PDFAbrechnung();
        ps.createPdf(filename, abrechnungID);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

