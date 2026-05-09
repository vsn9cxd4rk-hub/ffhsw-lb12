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

import ao.fahrzeuge.FahrzeugBelegungAO;
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
import data.tabellen.TabelleFahrzeug;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class FahrzeugbelegungPDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font UEBERSCHRIFT2 = new Font(Font.FontFamily.HELVETICA, 18.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, String veranstaltungName) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase textblock = new Phrase();
        Chunk chunk = new Chunk("Fahrzeugbelegung f\u00fcr", UEBERSCHRIFT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        chunk = new Chunk(veranstaltungName.toString(), UEBERSCHRIFT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
        try {
            String[] fahrzeugListe = Utils.listToArray(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());
            int f = 0;
            while (f < fahrzeugListe.length) {
                int fahrzeug = tabFahrzeuge.getFahrzeugID(fahrzeugListe[f]);
                logging.logInfo((Object)("FahrzeugName " + fahrzeugListe[f] + " - ID: " + fahrzeug));
                chunk = new Chunk(fahrzeugListe[f], UEBERSCHRIFT2);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                textblock.add((Element)Chunk.NEWLINE);
                int platz = 0;
                while (platz < 9) {
                    if (FahrzeugBelegungAO.sitzplatz[f][platz].isVisible()) {
                        chunk = new Chunk(FahrzeugBelegungAO.sitzplatz_label[f][platz].getText(), NORMAL_BOLD);
                        textblock.add((Element)chunk);
                        if (FahrzeugBelegungAO.sitzplatz[f][platz].getSelectedItem().toString().equals("<bitte w\u00e4hlen>")) {
                            chunk = new Chunk(" -- ", NORMAL_FONT);
                            textblock.add((Element)chunk);
                            textblock.add((Element)Chunk.NEWLINE);
                        } else {
                            chunk = new Chunk(FahrzeugBelegungAO.sitzplatz[f][platz].getSelectedItem().toString(), NORMAL_FONT);
                            textblock.add((Element)chunk);
                            textblock.add((Element)Chunk.NEWLINE);
                        }
                    }
                    ++platz;
                }
                textblock.add((Element)Chunk.NEWLINE);
                textblock.add((Element)Chunk.NEWLINE);
                ++f;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        document.add((Element)new Paragraph(textblock));
        document.addTitle("FeuerwehrManagementSystem - Fahrzeugbelegung");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Fahrzeugeinteilung");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String filename, String veranstaltungName) throws DocumentException, IOException, SQLException {
        FahrzeugbelegungPDFSchreiben ps = new FahrzeugbelegungPDFSchreiben();
        ps.createPdf(filename, veranstaltungName);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

