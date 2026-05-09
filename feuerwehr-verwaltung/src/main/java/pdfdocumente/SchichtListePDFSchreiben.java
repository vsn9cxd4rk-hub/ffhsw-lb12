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
import data.tabellen.schicht.TabelleSchicht;
import data.tabellen.schicht.TabelleSchicht_mitglieder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class SchichtListePDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font UEBERSCHRIFT2 = new Font(Font.FontFamily.HELVETICA, 16.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);

    public void createPdf(String filename, String schichtMonat, String jahr) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        TabelleSchicht tabSchicht = new TabelleSchicht();
        TabelleSchicht_mitglieder tabSchichtMitglieder = new TabelleSchicht_mitglieder();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase textblock = new Phrase();
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        Chunk chunk = new Chunk("Schicht\u00fcbersicht: " + schichtMonat + " " + jahr, UEBERSCHRIFT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        String monatNummer = null;
        if (schichtMonat.equals("Januar")) {
            monatNummer = "01";
        } else if (schichtMonat.equals("Februar")) {
            monatNummer = "02";
        } else if (schichtMonat.equals("M\u00e4rz")) {
            monatNummer = "03";
        } else if (schichtMonat.equals("April")) {
            monatNummer = "04";
        } else if (schichtMonat.equals("Mai")) {
            monatNummer = "05";
        } else if (schichtMonat.equals("Juni")) {
            monatNummer = "06";
        } else if (schichtMonat.equals("Juli")) {
            monatNummer = "07";
        } else if (schichtMonat.equals("August")) {
            monatNummer = "08";
        } else if (schichtMonat.equals("September")) {
            monatNummer = "09";
        } else if (schichtMonat.equals("Oktober")) {
            monatNummer = "10";
        } else if (schichtMonat.equals("November")) {
            monatNummer = "11";
        } else if (schichtMonat.equals("Dezember")) {
            monatNummer = "12";
        }
        String[] schichtName = Utils.listToArray(tabSchicht.getAllSchichtenEinesMonats(monatNummer, jahr));
        int s = 0;
        while (s < schichtName.length) {
            int sID = tabSchicht.getSchichtID(schichtName[s]);
            chunk = new Chunk(String.valueOf(schichtName[s]) + ": ", UEBERSCHRIFT2);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            String[] teilnehmer = Utils.listToArray(tabSchichtMitglieder.getMitglederEinerSchicht(sID));
            int t = 0;
            while (t < teilnehmer.length) {
                chunk = new Chunk(teilnehmer[t], NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                ++t;
            }
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            ++s;
        }
        document.add((Element)new Paragraph(textblock));
        document.addTitle("FeuerwehrManagementSystem - Schichtliste " + schichtMonat + " " + jahr);
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Schichtliste " + schichtMonat + " " + jahr);
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, String schichtMonat, String jahr) throws DocumentException, IOException, SQLException {
        SchichtListePDFSchreiben ps = new SchichtListePDFSchreiben();
        ps.createPdf(dateiname, schichtMonat, jahr);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

