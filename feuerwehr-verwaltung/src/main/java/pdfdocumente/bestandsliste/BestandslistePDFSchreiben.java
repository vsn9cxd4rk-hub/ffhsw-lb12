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
package pdfdocumente.bestandsliste;

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
import data.tabellen.bestandsliste.TabelleLager;
import data.tabellen.bestandsliste.TabelleLager_zugewiesen;
import data.tabellen.mitglied.TabelleMitglied;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class BestandslistePDFSchreiben {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 22.0f, 1);
    private static final Font UEBERSCHRIFT2 = new Font(Font.FontFamily.HELVETICA, 18.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);

    public void createPdf(String filename, String name, String typ) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase textblock = new Phrase();
        Chunk chunk = new Chunk("Bestandsliste f\u00fcr " + name, UEBERSCHRIFT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
        TabelleMitglied tabMitglied = new TabelleMitglied();
        TabelleLager tabLager = new TabelleLager();
        TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
        String[] lagerOrt = null;
        int id = 0;
        if (typ.equals("L")) {
            id = tabLager.getLagerID(name);
            lagerOrt = Utils.listToArray(tabZugewiesen.getAllOrt("L", id));
        } else if (typ.equals("M")) {
            id = tabMitglied.getIdByGuiString(name);
            lagerOrt = Utils.listToArray(tabZugewiesen.getAllOrt("M", id));
        } else if (typ.equals("F")) {
            id = tabFahrzeuge.getFahrzeugID(name);
            lagerOrt = Utils.listToArray(tabZugewiesen.getAllOrt("F", id));
        }
        int i = 0;
        while (i < lagerOrt.length) {
            logging.logInfo((Object)("F\u00fcge LagerOrt:" + lagerOrt[i] + " zur Liste bei..."));
            chunk = lagerOrt[i].equals("") ? new Chunk("LAGERORT NICHT ZUGEORDNET", UEBERSCHRIFT2) : new Chunk(lagerOrt[i], UEBERSCHRIFT2);
            textblock.add((Element)chunk);
            textblock.add((Element)Chunk.NEWLINE);
            String[] lagerArtikel = Utils.listToArray(tabZugewiesen.getZugewiesendeArtikelForPDF(typ, id, lagerOrt[i]));
            int a = 0;
            while (a < lagerArtikel.length) {
                logging.logInfo((Object)("F\u00fcge Artiel:" + lagerArtikel[a] + " zur Liste bei..."));
                chunk = new Chunk(lagerArtikel[a], NORMAL_FONT);
                textblock.add((Element)chunk);
                textblock.add((Element)Chunk.NEWLINE);
                ++a;
            }
            textblock.add((Element)Chunk.NEWLINE);
            textblock.add((Element)Chunk.NEWLINE);
            ++i;
        }
        document.add((Element)new Paragraph(textblock));
        document.addTitle("FeuerwehrManagementSystem - Bestandsliste");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("Bestandsliste");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String filename, String name, String typ) throws DocumentException, IOException, SQLException {
        BestandslistePDFSchreiben ps = new BestandslistePDFSchreiben();
        ps.createPdf(filename, name, typ);
        ps.printMeasures();
        logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
    }
}

