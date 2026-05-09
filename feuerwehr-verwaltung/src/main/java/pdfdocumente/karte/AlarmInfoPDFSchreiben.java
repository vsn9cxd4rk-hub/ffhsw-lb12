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
 */
package pdfdocumente.karte;

import ao.karte.KarteAO;
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

public class AlarmInfoPDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        String[] zeilenHydrant = KarteAO.hydrant.getText().split("\n");
        String[] zeilenSta\u00dfenInfo = KarteAO.stra\u00dfenInfo.getText().split("\n");
        String[] zeilenAlamierungsInfo = KarteAO.alamierungsInfo.getText().split("\n");
        String[] zeilenKoordinaten = KarteAO.koordinaten.getText().split("\n");
        String[] zeilenAnfahrt = KarteAO.anfahrtInfo.getText().split("\n");
        Phrase textblock = new Phrase();
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        Chunk chunk = new Chunk("Hydranten:", NORMAL_BOLD);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        int i = 0;
        while (i < zeilenHydrant.length) {
            chunk = new Chunk(zeilenHydrant[i], NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        chunk = new Chunk("Anfahrt:", NORMAL_BOLD);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        i = 0;
        while (i < zeilenAnfahrt.length) {
            chunk = new Chunk(zeilenAnfahrt[i], NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        chunk = new Chunk("Star\u00dfen Info:", NORMAL_BOLD);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        i = 0;
        while (i < zeilenSta\u00dfenInfo.length) {
            chunk = new Chunk(zeilenSta\u00dfenInfo[i], NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        chunk = new Chunk("Alamierungsinfo:", NORMAL_BOLD);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        i = 0;
        while (i < zeilenAlamierungsInfo.length) {
            chunk = new Chunk(zeilenAlamierungsInfo[i], NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        chunk = new Chunk("Koordinaten Stadtplan:", NORMAL_BOLD);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        i = 0;
        while (i < zeilenKoordinaten.length) {
            chunk = new Chunk(zeilenKoordinaten[i], NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        String image = (String)KarteAO.StrasseSuchen.getSelectedItem();
        Image imgG = Image.getInstance((String)("images/street/gro\u00df/" + image + ".jpg"));
        Image imgKL = Image.getInstance((String)("images/street/klein/" + image + ".jpg"));
        logging.logInfo((Object)("images/street/gro\u00df/" + image + ".jpg"));
        imgG.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgG, 500));
        imgG.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgG, 500));
        imgKL.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgKL, 500));
        imgKL.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgKL, 500));
        document.add((Element)new Paragraph(textblock));
        Phrase textblock2 = new Phrase();
        textblock2.add((Element)Chunk.NEWLINE);
        textblock2.add((Element)Chunk.NEWLINE);
        Chunk chunk2 = new Chunk("Katenansicht 1:", UEBERSCHRIFT);
        textblock2.add((Element)chunk2);
        textblock2.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock2));
        document.add((Element)imgKL);
        Phrase textblock3 = new Phrase();
        textblock3.add((Element)Chunk.NEWLINE);
        textblock3.add((Element)Chunk.NEWLINE);
        Chunk chunk3 = new Chunk("Kartenansicht 2:", UEBERSCHRIFT);
        textblock3.add((Element)chunk3);
        textblock3.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock3));
        document.add((Element)imgG);
        document.addTitle("FeuerwehrManagementSystem - AlarmInfo (Einsatz)");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("AlarmInfo");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String filename) throws DocumentException, IOException, SQLException {
        AlarmInfoPDFSchreiben ps = new AlarmInfoPDFSchreiben();
        ps.createPdf(filename);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

