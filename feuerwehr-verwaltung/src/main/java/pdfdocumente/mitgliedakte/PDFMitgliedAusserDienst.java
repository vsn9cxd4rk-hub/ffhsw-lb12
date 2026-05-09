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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class PDFMitgliedAusserDienst {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 16.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);

    public void createPdf(String filename, String mitglied) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
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
        Chunk address = new Chunk("Name: " + mitglied, UEBERSCHRIFT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        personal.add((Element)Chunk.NEWLINE);
        address = new Chunk("Das Mitglied " + mitglied + " wurde mit dem heutigen Tage Au\u00dfer Dienst gestellt.", NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        document.add((Element)new Paragraph(personal));
        document.addTitle("FeuerwehrManagementSystem - MitgliedInfo");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("MitgliedInfo");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, String mitglied) throws DocumentException, IOException {
        PDFMitgliedAusserDienst ps = new PDFMitgliedAusserDienst();
        try {
            ps.createPdf(dateiname, mitglied);
            ps.printMeasures();
            logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

