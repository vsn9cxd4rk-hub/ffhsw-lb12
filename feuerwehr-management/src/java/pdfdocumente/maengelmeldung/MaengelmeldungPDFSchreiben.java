package pdfdocumente.maengelmeldung;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.mitglied.TabelleMitglied;
import go.Mängelmeldung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class MaengelmeldungPDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, Mängelmeldung mangel, String fahrzeugName) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      Phrase textblock = new Phrase();
      Phrase textblock2 = new Phrase();
      PdfPTable table = null;
      table = new PdfPTable(2);

      try {
         Chunk e = new Chunk("MÄNGELMELDUNG", UEBERSCHRIFT);
         textblock.add(Chunk.NEWLINE);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Erstellt: ", NORMAL_BOLD);
         textblock.add(e);
         e = new Chunk((String)runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp("dd.MM.yyyy"), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         if(mangel.getFahrzeugID() != 0) {
            e = new Chunk("Fahrzeug: ", NORMAL_BOLD);
            textblock.add(e);
            e = new Chunk(tabFahrzeug.getFahrzeugName(mangel.getFahrzeugID()), NORMAL_FONT);
            textblock.add(e);
            textblock.add(Chunk.NEWLINE);
            e = new Chunk("Kennzeichen: ", NORMAL_BOLD);
            textblock.add(e);
            e = new Chunk(tabFahrzeug.getKennezeichen(mangel.getFahrzeugID()), NORMAL_FONT);
            textblock.add(e);
            textblock.add(Chunk.NEWLINE);
            e = new Chunk("Funkrufname: ", NORMAL_BOLD);
            textblock.add(e);
            e = new Chunk(tabFahrzeug.getFunkrufname(mangel.getFahrzeugID()), NORMAL_FONT);
            textblock.add(e);
            textblock.add(Chunk.NEWLINE);
         } else {
            e = new Chunk("Art / Ort: ", NORMAL_BOLD);
            textblock.add(e);
            e = new Chunk(fahrzeugName, NORMAL_FONT);
            textblock.add(e);
            textblock.add(Chunk.NEWLINE);
         }

         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Wann ist der Mangel Aufgetreten (Veranstaltung, Einsatz, Übung):", NORMAL_BOLD);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk(mangel.getWann(), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Wer hat den Mangel festgestellt: ", NORMAL_BOLD);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk(tabMitglied.getName(mangel.getMitgliedID()) + ", " + tabMitglied.getVorname(mangel.getMitgliedID()), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Detaillierte Mangelbeschreibung: ", NORMAL_BOLD);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk(mangel.getBeschreibung(), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("__________________", NORMAL_FONT);
         textblock2.add(e);
         textblock2.add(Chunk.NEWLINE);
         e = new Chunk("    Unterschrift", NORMAL_FONT);
         textblock2.add(e);
      } catch (SQLException var13) {
         logging.logPrintStackTrace(var13);
      }

      document.add(new Paragraph(textblock));
      document.add(table);
      document.add(new Paragraph(textblock2));
      document.addTitle("FeuerwehrManagementSystem - Einsatz Bericht");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Einsatzbericht");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename, Mängelmeldung mangel, String fahrzeugName) throws DocumentException, IOException, SQLException {
      MaengelmeldungPDFSchreiben ps = new MaengelmeldungPDFSchreiben();
      ps.createPdf(filename, mangel, fahrzeugName);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
