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
package pdfdocumente;

import ao.listen.LehrgangsmeldungAO;
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
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.TabelleLehrgangsmeldung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class LehrgangsmeldungPDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String jahr, String filename) throws DocumentException, IOException, SQLException {
        Chunk chunk3;
        Chunk chunk2;
        Chunk chunk;
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
        TabelleLehrgangsmeldung tabMeldung = new TabelleLehrgangsmeldung();
        Phrase textblock = new Phrase();
        if (tabMeldung.getCount("L") != 0 && LehrgangsmeldungAO.lehrgang_checkbox.isSelected()) {
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Lehrgangsmeldung f\u00fcr " + jahr + ":", UEBERSCHRIFT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            chunk = new Chunk("Dies sind die vom Programm automatisch vorgeschlagenden Lehrgangsmeldungen: ", NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            String[] lehrg\u00e4nge = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());
            int l = 0;
            while (l < lehrg\u00e4nge.length) {
                chunk = new Chunk(String.valueOf(lehrg\u00e4nge[l]) + ":", NORMAL_BOLD);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                String[] teilnehmer = Utils.listToArray(tabMeldung.getLehrgangsmeldungByLehrgang(lehrg\u00e4nge[l]));
                int i = 0;
                while (i < teilnehmer.length) {
                    chunk = new Chunk(teilnehmer[i], NORMAL_FONT);
                    textblock.add((Element)chunk);
                    textblock.add((Element)Chunk.NEWLINE);
                    ++i;
                }
                textblock.add((Element)Chunk.NEWLINE);
                ++l;
            }
        } else {
            chunk = new Chunk("KEINE Lehrgangsmeldungen verf\u00fcgbar", NORMAL_FONT);
            textblock.add((Element)chunk);
        }
        Phrase textblock2 = new Phrase();
        if (tabMeldung.getCount("B") != 0 && LehrgangsmeldungAO.bef\u00f6rderung_checkbox.isSelected()) {
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Bef\u00f6rderungen f\u00fcr " + jahr + ":", UEBERSCHRIFT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)Chunk.NEWLINE);
            chunk2 = new Chunk("Dies sind die vom Programm automatisch vorgeschlagenden Bef\u00f6rderungen: ", NORMAL_FONT);
            textblock2.add((Element)chunk2);
            textblock2.add((Element)Chunk.NEWLINE);
            textblock2.add((Element)Chunk.NEWLINE);
            String[] befoerderungen = Utils.listToArray(tabMeldung.getBefoerderungen());
            int b = 0;
            while (b < befoerderungen.length) {
                chunk2 = new Chunk(befoerderungen[b], NORMAL_FONT);
                textblock2.add((Element)chunk2);
                textblock2.add((Element)Chunk.NEWLINE);
                textblock2.add((Element)Chunk.NEWLINE);
                ++b;
            }
            textblock2.add((Element)Chunk.NEWLINE);
        } else {
            chunk2 = new Chunk("KEINE Bef\u00f6rderungen verf\u00fcgbar", NORMAL_FONT);
            textblock2.add((Element)chunk2);
        }
        Phrase textblock3 = new Phrase();
        if (tabMeldung.getCount("EH") != 0 && LehrgangsmeldungAO.ehrung_checkbox.isSelected()) {
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk("Ehrungen f\u00fcr " + jahr + ":", UEBERSCHRIFT);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)Chunk.NEWLINE);
            chunk3 = new Chunk("Dies sind die vom Programm automatisch vorgeschlagenden Ehrungen: ", NORMAL_FONT);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)Chunk.NEWLINE);
            String[] ehrungen = Utils.listToArray(tabMeldung.getEhrungen());
            int e = 0;
            while (e < ehrungen.length) {
                chunk3 = new Chunk(ehrungen[e], NORMAL_FONT);
                textblock3.add((Element)chunk3);
                textblock3.add((Element)Chunk.NEWLINE);
                textblock3.add((Element)Chunk.NEWLINE);
                ++e;
            }
            textblock3.add((Element)Chunk.NEWLINE);
        } else {
            chunk3 = new Chunk("KEINE Ehrungen verf\u00fcgbar", NORMAL_FONT);
            textblock3.add((Element)chunk3);
        }
        if (LehrgangsmeldungAO.lehrgang_checkbox.isSelected()) {
            Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
            imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
            imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
            document.add((Element)imgBanner);
            document.add((Element)new Paragraph(textblock));
            document.newPage();
        }
        if (LehrgangsmeldungAO.bef\u00f6rderung_checkbox.isSelected()) {
            Image imgBanner2 = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
            imgBanner2.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner2, 500));
            imgBanner2.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner2, 520));
            document.add((Element)imgBanner2);
            document.add((Element)new Paragraph(textblock2));
        }
        if (LehrgangsmeldungAO.ehrung_checkbox.isSelected()) {
            Image imgBanner3 = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
            imgBanner3.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner3, 500));
            imgBanner3.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner3, 520));
            document.add((Element)imgBanner3);
            document.add((Element)new Paragraph(textblock3));
        }
        document.addTitle("FeuerwehrManagementSystem - Lehrgangsmeldungen " + jahr);
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Lehrgangsmeldungen " + jahr);
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String jahr, String filename) throws DocumentException, IOException, SQLException {
        LehrgangsmeldungPDFSchreiben ps = new LehrgangsmeldungPDFSchreiben();
        ps.createPdf(jahr, filename);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

