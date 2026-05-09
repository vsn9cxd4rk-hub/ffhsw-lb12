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
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.Utils;

public class PDFVeranstaltungTeilnahmen {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 16.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, String veranstaltungKategorieName) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
        int kID = tabKategorie.getID(veranstaltungKategorieName);
        Phrase textblock = new Phrase();
        Chunk chunk = new Chunk("                                                                                                   " + runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp((String)"dd.MM.yyyy"), NORMAL_FONT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk("Teilnehmerliste " + veranstaltungKategorieName, UEBERSCHRIFT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        String[] veranstaltungListe = Utils.listToArray(tabVeranstaltung.getAllKommendenVeranstaltungEinerKategorieByJahr(kID, Integer.parseInt(SbcUtils.timeStamp((String)"yyyy"))));
        int i = 0;
        while (i < veranstaltungListe.length) {
            chunk = new Chunk(veranstaltungListe[i], NORMAL_BOLD);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)chunk);
            String[] teilnehmerListe = Utils.listToArray(tabAnwesenheit.getAnwesendeMitgliederByVeranstaltung(tabVeranstaltung.getVeranstaltungID(veranstaltungListe[i])));
            int m = 0;
            while (m < teilnehmerListe.length) {
                chunk = new Chunk(teilnehmerListe[m], NORMAL_FONT);
                textblock.add((Element)Chunk.NEWLINE);
                textblock.add((Element)chunk);
                ++m;
            }
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        document.add((Element)new Paragraph(textblock));
        document.addTitle("FeuerwehrManagementSystem - Teilnehmerliste");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("FahrzeugInfo");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, String veranstaltungKategorieName) throws DocumentException, IOException {
        PDFVeranstaltungTeilnahmen ps = new PDFVeranstaltungTeilnahmen();
        try {
            ps.createPdf(dateiname, veranstaltungKategorieName);
            ps.printMeasures();
            logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

