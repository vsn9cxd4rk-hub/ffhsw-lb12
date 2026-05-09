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
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleVeranstaltung;
import go.Protokoll;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class BerichtPDFSchreiben {
    public void createPdf(String filename, String title, String text, String jahr, String[] grafiken, String[] grafikenBeschreibung, boolean deckblattIsSelected, boolean protokolle) throws DocumentException, IOException, SQLException {
        Image imgBanner;
        Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
        Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
        Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        if (deckblattIsSelected) {
            imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
            imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
            imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
            document.add((Element)imgBanner);
            Phrase textblock0 = new Phrase();
            Chunk chunk0 = new Chunk(title, UEBERSCHRIFT);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)Chunk.NEWLINE);
            textblock0.add((Element)chunk0);
            Paragraph deckblatt = new Paragraph();
            deckblatt.setAlignment(1);
            deckblatt.add((Element)textblock0);
            document.add((Element)deckblatt);
            document.newPage();
        }
        imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        String[] bericht = text.split("\n");
        Phrase textblock = new Phrase();
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        Chunk chunk = new Chunk(title, UEBERSCHRIFT);
        if (!deckblattIsSelected) {
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
        }
        chunk = new Chunk("Bericht:", NORMAL_BOLD);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        int i = 0;
        while (i < bericht.length) {
            chunk = new Chunk(bericht[i], NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock));
        document.newPage();
        int g = 0;
        while (g < grafiken.length) {
            if (grafiken[g] != null) {
                Image bild = Image.getInstance((String)grafiken[g]);
                bild.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(bild, 400));
                bild.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(bild, 550));
                Phrase textblock2 = new Phrase();
                Chunk chunk2 = new Chunk(grafikenBeschreibung[g], UEBERSCHRIFT);
                textblock2.add((Element)Chunk.NEWLINE);
                textblock2.add((Element)chunk2);
                textblock2.add((Element)Chunk.NEWLINE);
                textblock2.add((Element)Chunk.NEWLINE);
                document.add((Element)new Paragraph(textblock2));
                document.add((Element)bild);
            }
            ++g;
        }
        if (protokolle) {
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleProtokoll tabProtokoll = new TabelleProtokoll();
            String[] einsatzListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinerKategorieByJahr(1, Integer.parseInt(jahr)));
            Phrase textblock3 = new Phrase();
            Chunk chunk3 = new Chunk("Einsatzberichte:", UEBERSCHRIFT);
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)chunk3);
            textblock3.add((Element)Chunk.NEWLINE);
            textblock3.add((Element)Chunk.NEWLINE);
            document.add((Element)new Paragraph(textblock3));
            int p = 0;
            while (p < einsatzListe.length) {
                int vID = tabVeranstaltung.getVeranstaltungID(einsatzListe[p]);
                Protokoll protokoll = tabProtokoll.getData(vID);
                if (protokoll != null) {
                    logging.logInfo((Object)("F\u00fcge Protokoll f\u00fcr VeranstaltungID:" + vID + " + zu Bericht hinzu..."));
                    Phrase textblock4 = new Phrase();
                    Chunk chunk4 = new Chunk(einsatzListe[p], UEBERSCHRIFT);
                    textblock4.add((Element)Chunk.NEWLINE);
                    textblock4.add((Element)chunk4);
                    textblock4.add((Element)Chunk.NEWLINE);
                    chunk4 = new Chunk(protokoll.getProtokolltext(), NORMAL_FONT);
                    textblock4.add((Element)chunk4);
                    textblock4.add((Element)Chunk.NEWLINE);
                    textblock4.add((Element)Chunk.NEWLINE);
                    document.add((Element)new Paragraph(textblock4));
                }
                ++p;
            }
        }
        document.addTitle("FeuerwehrManagementSystem - Bericht " + jahr);
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Bericht " + jahr);
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, String title, String text, String jahr, String[] grafiken, String[] grafikenBeschreibung, boolean deckblattIsSelected, boolean protokolle) throws DocumentException, IOException, SQLException {
        BerichtPDFSchreiben ps = new BerichtPDFSchreiben();
        ps.createPdf(dateiname, title, text, jahr, grafiken, grafikenBeschreibung, deckblattIsSelected, protokolle);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

