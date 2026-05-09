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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class BerichtPDFSchreiben_alt {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, String title, String text) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        String[] bericht = text.split("\n");
        Phrase textblock = new Phrase();
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        Chunk chunk = new Chunk(title, UEBERSCHRIFT);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
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
        String jahr = SbcUtils.timeStamp((String)"yyyy");
        Image imgAnwesenheitEinsatz = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/Anwesenheit_Einsatz.jpg"));
        imgAnwesenheitEinsatz.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgAnwesenheitEinsatz, 400));
        imgAnwesenheitEinsatz.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgAnwesenheitEinsatz, 550));
        Phrase textblock2 = new Phrase();
        Chunk chunk2 = new Chunk("Anwesenheitsstatistik Einsatz:", UEBERSCHRIFT);
        textblock2.add((Element)Chunk.NEWLINE);
        textblock2.add((Element)chunk2);
        textblock2.add((Element)Chunk.NEWLINE);
        textblock2.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock2));
        document.add((Element)imgAnwesenheitEinsatz);
        Image imgAnwesenheitBSW = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/Anwesenheit_Brandsicherheitswache.jpg"));
        imgAnwesenheitBSW.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgAnwesenheitBSW, 400));
        imgAnwesenheitBSW.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgAnwesenheitBSW, 500));
        Phrase textblock3 = new Phrase();
        Chunk chunk3 = new Chunk("Anwesenheitsstatistik BSW:", UEBERSCHRIFT);
        textblock3.add((Element)Chunk.NEWLINE);
        textblock3.add((Element)chunk3);
        textblock3.add((Element)Chunk.NEWLINE);
        textblock3.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock3));
        document.add((Element)imgAnwesenheitBSW);
        document.newPage();
        Image imgAnwesenheitDienst = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/Anwesenheit_Dienst.jpg"));
        imgAnwesenheitDienst.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgAnwesenheitDienst, 400));
        imgAnwesenheitDienst.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgAnwesenheitDienst, 500));
        Phrase textblock4 = new Phrase();
        Chunk chunk4 = new Chunk("Anwesenheitsstatistik Dienst:", UEBERSCHRIFT);
        textblock4.add((Element)Chunk.NEWLINE);
        textblock4.add((Element)chunk4);
        textblock4.add((Element)Chunk.NEWLINE);
        textblock4.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock4));
        document.add((Element)imgAnwesenheitDienst);
        Image imgAnwesenheitGesamt = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/Anwesenheit_Gesamt.jpg"));
        imgAnwesenheitGesamt.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgAnwesenheitGesamt, 400));
        imgAnwesenheitGesamt.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgAnwesenheitGesamt, 500));
        Phrase textblock5 = new Phrase();
        Chunk chunk5 = new Chunk("Anwsenheitsstatistik Gesamt:", UEBERSCHRIFT);
        textblock5.add((Element)Chunk.NEWLINE);
        textblock5.add((Element)chunk5);
        textblock5.add((Element)Chunk.NEWLINE);
        textblock5.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock5));
        document.add((Element)imgAnwesenheitGesamt);
        document.newPage();
        Image imgAusrueckezeiten = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/Ausr\u00fcckezeit.jpg"));
        imgAusrueckezeiten.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgAusrueckezeiten, 400));
        imgAusrueckezeiten.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgAusrueckezeiten, 500));
        Phrase textblock6 = new Phrase();
        Chunk chunk6 = new Chunk("Ausr\u00fcckzeiten:", UEBERSCHRIFT);
        textblock6.add((Element)Chunk.NEWLINE);
        textblock6.add((Element)chunk6);
        textblock6.add((Element)Chunk.NEWLINE);
        textblock6.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock6));
        document.add((Element)imgAusrueckezeiten);
        Image imgEinsatzart = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/Einsatzart.jpg"));
        imgEinsatzart.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgEinsatzart, 400));
        imgEinsatzart.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgEinsatzart, 500));
        Phrase textblock7 = new Phrase();
        Chunk chunk7 = new Chunk("Einsatz\u00fcbersicht (Einsatzart):", UEBERSCHRIFT);
        textblock7.add((Element)Chunk.NEWLINE);
        textblock7.add((Element)chunk7);
        textblock7.add((Element)Chunk.NEWLINE);
        textblock7.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock7));
        document.add((Element)imgEinsatzart);
        document.newPage();
        Image imgEinsatzMannstunden = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/EinsatzMannstunden.jpg"));
        imgEinsatzMannstunden.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgEinsatzMannstunden, 400));
        imgEinsatzMannstunden.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgEinsatzMannstunden, 500));
        Phrase textblock8 = new Phrase();
        Chunk chunk8 = new Chunk("Einsatzmannstunden:", UEBERSCHRIFT);
        textblock8.add((Element)Chunk.NEWLINE);
        textblock8.add((Element)chunk8);
        textblock8.add((Element)Chunk.NEWLINE);
        textblock8.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock8));
        document.add((Element)imgEinsatzMannstunden);
        Image imgBSWMannstunden = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/BSWMannstunden.jpg"));
        imgBSWMannstunden.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBSWMannstunden, 400));
        imgBSWMannstunden.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBSWMannstunden, 500));
        Phrase textblock9 = new Phrase();
        Chunk chunk9 = new Chunk("BSW Mannstunden:", UEBERSCHRIFT);
        textblock9.add((Element)Chunk.NEWLINE);
        textblock9.add((Element)chunk9);
        textblock9.add((Element)Chunk.NEWLINE);
        textblock9.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock9));
        document.add((Element)imgBSWMannstunden);
        document.newPage();
        Image imgEinsatzProMonat = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/EinsatzProMonat.jpg"));
        imgEinsatzProMonat.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgEinsatzProMonat, 400));
        imgEinsatzProMonat.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgEinsatzProMonat, 500));
        Phrase textblock10 = new Phrase();
        Chunk chunk10 = new Chunk("Einsatzstatistik Monat/Anzahl:", UEBERSCHRIFT);
        textblock10.add((Element)Chunk.NEWLINE);
        textblock10.add((Element)chunk10);
        textblock10.add((Element)Chunk.NEWLINE);
        textblock10.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock10));
        document.add((Element)imgEinsatzProMonat);
        Image imgEinsatzProStunde = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/EinsatzProStunde.jpg"));
        imgEinsatzProStunde.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgEinsatzProStunde, 400));
        imgEinsatzProStunde.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgEinsatzProStunde, 500));
        Phrase textblock11 = new Phrase();
        Chunk chunk11 = new Chunk("Einsatzstatistik Stunde/Anzahl:", UEBERSCHRIFT);
        textblock11.add((Element)Chunk.NEWLINE);
        textblock11.add((Element)chunk11);
        textblock11.add((Element)Chunk.NEWLINE);
        textblock11.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock11));
        document.add((Element)imgEinsatzProStunde);
        document.newPage();
        Image imgEinsatzProWochentag = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/EinsatzProWochentag.jpg"));
        imgEinsatzProWochentag.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgAnwesenheitGesamt, 400));
        imgEinsatzProWochentag.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgAnwesenheitGesamt, 500));
        Phrase textblock13 = new Phrase();
        Chunk chunk13 = new Chunk("Einsatzstatistik Wochentag/Anzahl:", UEBERSCHRIFT);
        textblock13.add((Element)Chunk.NEWLINE);
        textblock13.add((Element)chunk13);
        textblock13.add((Element)Chunk.NEWLINE);
        textblock13.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock13));
        document.add((Element)imgEinsatzProWochentag);
        Image imgAbwesenheit = Image.getInstance((String)(String.valueOf(runApplication.arbeitsverzeichnis) + "data/" + jahr + "/temp/Abwesenheit.jpg"));
        imgAbwesenheit.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgAnwesenheitGesamt, 400));
        imgAbwesenheit.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgAnwesenheitGesamt, 500));
        Phrase textblock12 = new Phrase();
        Chunk chunk12 = new Chunk("Abwesenheitsgr\u00fcnde:", UEBERSCHRIFT);
        textblock12.add((Element)Chunk.NEWLINE);
        textblock12.add((Element)chunk12);
        textblock12.add((Element)Chunk.NEWLINE);
        textblock12.add((Element)Chunk.NEWLINE);
        document.add((Element)new Paragraph(textblock12));
        document.add((Element)imgAbwesenheit);
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

    public static void PDFdocumentErstellen(String dateiname, String title, String text) throws DocumentException, IOException, SQLException {
        BerichtPDFSchreiben_alt ps = new BerichtPDFSchreiben_alt();
        ps.createPdf(dateiname, title, text);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

