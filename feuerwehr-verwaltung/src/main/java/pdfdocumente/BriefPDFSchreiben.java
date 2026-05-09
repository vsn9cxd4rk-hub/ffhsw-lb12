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
package pdfdocumente;

import ao.BriefAO;
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
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_anrede;
import go.DokumentLayoutOptions;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class BriefPDFSchreiben {
    private static Font UEBERSCHRIFT;
    private static Font NORMAL_FONT;
    private static Font NORMAL_KLEIN;

    public void createPdf(String filename, String anWen) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        String[] brief = BriefAO.textfield.getText().split("\n");
        String[] empf\u00e4nger = anWen.split("\n");
        TabelleMitglied tabMitglieder = new TabelleMitglied();
        TabelleMitglieder_anrede tabAnrede = new TabelleMitglieder_anrede();
        int e = 0;
        while (e < empf\u00e4nger.length) {
            Chunk chunk;
            int mID = tabMitglieder.getIdByGuiString(empf\u00e4nger[e].toString());
            this.createDocumentHeadder(document, empf\u00e4nger, tabMitglieder, tabAnrede, e, mID);
            Phrase textblock = new Phrase();
            this.createBetreff(tabMitglieder, tabAnrede, mID, textblock);
            logging.logInfo((Object)("Brief L\u00e4nge: " + brief.length));
            if (brief.length >= 26) {
                int i = 0;
                while (i < 26) {
                    chunk = new Chunk(brief[i], NORMAL_FONT);
                    textblock.add((Element)chunk);
                    textblock.add((Element)Chunk.NEWLINE);
                    ++i;
                }
                document.add((Element)new Paragraph(textblock));
                document.newPage();
                this.createDocumentHeadder(document, empf\u00e4nger, tabMitglieder, tabAnrede, e, mID);
                Phrase textblock2 = new Phrase();
                textblock2.add((Element)Chunk.NEWLINE);
                Chunk chunk2 = new Chunk("Seite 2", NORMAL_FONT);
                textblock2.add((Element)chunk2);
                textblock2.add((Element)Chunk.NEWLINE);
                textblock2.add((Element)Chunk.NEWLINE);
                int n = 26;
                while (n < brief.length) {
                    chunk2 = new Chunk(brief[n], NORMAL_FONT);
                    textblock2.add((Element)chunk2);
                    textblock2.add((Element)Chunk.NEWLINE);
                    ++n;
                }
                document.add((Element)new Paragraph(textblock2));
                document.newPage();
            } else {
                int i = 0;
                while (i < brief.length) {
                    chunk = new Chunk(brief[i], NORMAL_FONT);
                    textblock.add((Element)chunk);
                    textblock.add((Element)Chunk.NEWLINE);
                    ++i;
                }
                textblock.add((Element)Chunk.NEWLINE);
                textblock.add((Element)Chunk.NEWLINE);
                document.add((Element)new Paragraph(textblock));
                document.newPage();
            }
            ++e;
        }
        document.addTitle("FeuerwehrManagementSystem - Brief");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Bericht");
        document.close();
        writer.close();
    }

    private void createDocumentHeadder(Document document, String[] empf\u00e4nger, TabelleMitglied tabMitglieder, TabelleMitglieder_anrede tabAnrede, int e, int mID) throws BadElementException, MalformedURLException, IOException, SQLException, DocumentException {
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase adressTextblock = new Phrase();
        Chunk address = new Chunk(String.valueOf(runApplication.EINSTELLUNGEN.get("Name")) + ", " + runApplication.EINSTELLUNGEN.get("strasse") + ", " + runApplication.EINSTELLUNGEN.get("plz") + " " + runApplication.EINSTELLUNGEN.get("Stadt"), NORMAL_KLEIN);
        adressTextblock.add((Element)Chunk.NEWLINE);
        adressTextblock.add((Element)Chunk.NEWLINE);
        adressTextblock.add((Element)address);
        address = new Chunk(tabAnrede.getAnredeName(tabMitglieder.getAnrede(mID)), NORMAL_FONT);
        adressTextblock.add((Element)Chunk.NEWLINE);
        adressTextblock.add((Element)address);
        address = new Chunk(String.valueOf(tabMitglieder.getVorname(mID)) + " " + tabMitglieder.getName(mID), NORMAL_FONT);
        adressTextblock.add((Element)Chunk.NEWLINE);
        adressTextblock.add((Element)address);
        address = new Chunk(tabMitglieder.getStrasse(mID), NORMAL_FONT);
        adressTextblock.add((Element)Chunk.NEWLINE);
        adressTextblock.add((Element)address);
        address = new Chunk(tabMitglieder.getOrt(mID), NORMAL_FONT);
        adressTextblock.add((Element)Chunk.NEWLINE);
        adressTextblock.add((Element)address);
        document.add((Element)new Paragraph(adressTextblock));
        Phrase textblockDatumStadt = new Phrase();
        Chunk chunk = new Chunk(String.valueOf(runApplication.EINSTELLUNGEN.get("Stadt")) + ", den " + SbcUtils.timeStamp((String)"dd.MM.yyyy"), NORMAL_FONT);
        textblockDatumStadt.add((Element)Chunk.NEWLINE);
        textblockDatumStadt.add((Element)chunk);
        Paragraph datum = new Paragraph();
        datum.add((Element)textblockDatumStadt);
        datum.setAlignment(2);
        document.add((Element)datum);
    }

    private Phrase createBetreff(TabelleMitglied tabMitglieder, TabelleMitglieder_anrede tabAnrede, int mID, Phrase textblock) throws SQLException {
        Chunk chunk = new Chunk(BriefAO.title.getText(), UEBERSCHRIFT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        int aID = tabMitglieder.getAnrede(mID);
        String anrede = String.valueOf(tabAnrede.getAnredeBrief(aID)) + " " + tabAnrede.getAnredeName(aID);
        chunk = new Chunk(String.valueOf(anrede) + " " + tabMitglieder.getName(mID) + ",", NORMAL_FONT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        return textblock;
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, String anWen, DokumentLayoutOptions dokOption) throws DocumentException, IOException {
        BriefPDFSchreiben ps = new BriefPDFSchreiben();
        UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 16.0f, 1);
        NORMAL_FONT = new Font(dokOption.getNormalSchriftart(), (float)dokOption.getNormalSchriftgr\u00f6\u00dfe(), 0);
        NORMAL_KLEIN = new Font(Font.FontFamily.HELVETICA, 8.0f, 4);
        try {
            ps.createPdf(dateiname, anWen);
            ps.printMeasures();
            logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

