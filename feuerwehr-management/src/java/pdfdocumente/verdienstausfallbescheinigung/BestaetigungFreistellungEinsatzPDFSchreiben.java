package pdfdocumente.verdienstausfallbescheinigung;

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
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_arbeit;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class BestaetigungFreistellungEinsatzPDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, String veranstaltung, int mID, String am, String bis, String vonUhr, String bisUhr) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleMitglieder_arbeit tabArbeit = new TabelleMitglieder_arbeit();
      Phrase textblock = new Phrase();
      Phrase textblock2 = new Phrase();
      PdfPTable table = null;
      table = new PdfPTable(2);

      try {
         Chunk e = new Chunk("BESTÄTIGUNG ZUR FREISTELLUNG   ", UEBERSCHRIFT);
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
         e = new Chunk("Bezeichung der Veranstaltung / des Lehrganges /des Einsatzes:", NORMAL_BOLD);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk(veranstaltung, NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("ARBEITGEBERADRESSE", NORMAL_BOLD);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Firma: ", NORMAL_BOLD);
         textblock.add(e);
         e = new Chunk(tabArbeit.getName(mID), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Anschrift: ", NORMAL_BOLD);
         textblock.add(e);
         e = new Chunk(tabArbeit.getOrt(mID), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Telefon: ", NORMAL_BOLD);
         textblock.add(e);
         e = new Chunk(tabArbeit.getTelefon(mID), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("ARBEITNEHMERDATEN", NORMAL_BOLD);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Name: ", NORMAL_BOLD);
         textblock.add(e);
         e = new Chunk(tabMitglied.getName(mID) + ", " + tabMitglied.getVorname(mID), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Anschrift: ", NORMAL_BOLD);
         textblock.add(e);
         e = new Chunk(tabMitglied.getStrasse(mID) + ", " + tabMitglied.getOrt(mID), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Geburtsdatum: ", NORMAL_BOLD);
         textblock.add(e);
         e = new Chunk(TimeCalculation.parseDateForGUI(tabMitglied.getGebDatum(mID)), NORMAL_FONT);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("Dienst / Berufsbezeichnung:    ", NORMAL_BOLD);
         textblock.add(e);
         String beruf = tabMitglied.getBeruf(mID);
         if(beruf.equals("")) {
            e = new Chunk("_______________________________", NORMAL_FONT);
         } else {
            e = new Chunk(beruf, NORMAL_FONT);
         }

         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         e = new Chunk("VERANSTALTUNGSDETAILS", NORMAL_BOLD);
         textblock.add(e);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         table.addCell("am/vom (Datum):\nbis (Datum):\nvon (Uhrzeit)\nbis (Uhrzeit):");
         table.addCell(am + "\n" + bis + "\n" + vonUhr + "\n" + bisUhr);
         Chunk chunk2 = new Chunk("Hiermit wird bestätigt, dass der oben genannte Kamerad als ehrenamtliches Mitglied der Freiwilligen Feuerwehr in Rahmen einen Feuerwehreinsatzes tätig war.", NORMAL_FONT);
         textblock2.add(chunk2);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         chunk2 = new Chunk("___________________                              __________________", NORMAL_FONT);
         textblock2.add(chunk2);
         textblock2.add(Chunk.NEWLINE);
         chunk2 = new Chunk("   Firmenstempel                                               Unterschrift", NORMAL_FONT);
         textblock2.add(chunk2);
      } catch (SQLException var19) {
         logging.logPrintStackTrace(var19);
      }

      document.add(new Paragraph(textblock));
      document.add(table);
      document.add(new Paragraph(textblock2));
      document.addTitle("FeuerwehrManagementSystem - Bestätigung Freistellung Einsatz");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Einsatzbericht");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename, String veranstaltung, int mID, String am, String bis, String vonUhr, String bisUhr) throws DocumentException, IOException, SQLException {
      BestaetigungFreistellungEinsatzPDFSchreiben ps = new BestaetigungFreistellungEinsatzPDFSchreiben();
      ps.createPdf(filename, veranstaltung, mID, am, bis, vonUhr, bisUhr);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
