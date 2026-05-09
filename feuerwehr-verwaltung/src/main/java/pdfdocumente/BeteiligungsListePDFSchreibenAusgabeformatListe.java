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
import data.tabellen.mitglied.TabelleMitglied;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.Utils;

public class BeteiligungsListePDFSchreibenAusgabeformatListe {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font UEBERSCHRIFT2 = new Font(Font.FontFamily.HELVETICA, 16.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, String mitglied, String jahr, String grafikFile) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleAnwesenheit tabAnwesend = new TabelleAnwesenheit();
        TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
        TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase textblock = new Phrase();
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        Chunk chunk = new Chunk("Beteiligungs\u00fcbersicht " + jahr + ": " + mitglied, UEBERSCHRIFT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        int mID = tabMitglied.getIdByGuiString(mitglied);
        chunk = new Chunk("Auflistung in Zahlen: ", UEBERSCHRIFT2);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        String[] kategorie = Utils.listToArray(tabKategorie.getAllKategorien());
        int z = 0;
        while (z < kategorie.length) {
            chunk = new Chunk(String.valueOf(kategorie[z]) + ": ", NORMAL_BOLD);
            textblock.add((Element)chunk);
            int vKategorie = tabKategorie.getID(kategorie[z]);
            String zahl = Integer.toString(tabAnwesend.getBeteiligungByKategorie(mID, vKategorie, Integer.parseInt(jahr)));
            String gesZahl = Integer.toString(tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(jahr, vKategorie));
            chunk = new Chunk(String.valueOf(zahl) + " / " + gesZahl, NORMAL_FONT);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            ++z;
        }
        chunk = new Chunk("Beiteiligungs\u00fcbersicht Stand: " + SbcUtils.timeStamp((String)"dd.MM.yyyy"), NORMAL_FONT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock));
        if (grafikFile != null) {
            Image bild = Image.getInstance((String)grafikFile);
            bild.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(bild, 500));
            bild.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(bild, 550));
            document.add((Element)bild);
        }
        document.addTitle("FeuerwehrManagementSystem - Beteiligungs\u00fcbersicht");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Beteiligungs\u00fcbersicht");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, String mitglied, String jahr, String grafikFile) throws DocumentException, IOException, SQLException {
        BeteiligungsListePDFSchreibenAusgabeformatListe ps = new BeteiligungsListePDFSchreibenAusgabeformatListe();
        ps.createPdf(dateiname, mitglied, jahr, grafikFile);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

