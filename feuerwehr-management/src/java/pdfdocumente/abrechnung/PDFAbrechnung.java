package pdfdocumente.abrechnung;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfWriter;
import data.tabellen.abrechnung.TabelleAbrechnung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.Utils;

public class PDFAbrechnung {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);


   public void createPdf(String filename, int abrechnungID) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      String datum = SbcUtils.timeStamp("dd.MM.yyyy");
      String uhrzeit = SbcUtils.timeStamp("HH:mm:ss");
      int seite = 1;
      document.open();
      Phrase textblock = this.banner(abrechnungID, document, datum, uhrzeit, seite);
      TabelleAbrechnung tabAbrechnung = new TabelleAbrechnung();
      String[] liste = Utils.listToArray(tabAbrechnung.getDataForPDF(abrechnungID));
      int count = 0;

      for(int i = 0; i < liste.length; ++i) {
         Chunk chunk = new Chunk(liste[i], NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         ++count;
         if(count == 6) {
            document.add(textblock);
            document.newPage();
            textblock = this.banner(abrechnungID, document, datum, uhrzeit, seite++);
            count = 0;
         }
      }

      document.add(new Paragraph(textblock));
      document.addTitle("FeuerwehrManagementSystem - Abrechnung");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Abrechnung");
      document.close();
      writer.close();
   }

   private Phrase banner(int abrechnungID, Document document, String datum, String uhrzeit, int seite) throws BadElementException, MalformedURLException, IOException, DocumentException {
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase textblock = new Phrase();
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      Chunk chunk = new Chunk("Abrechnung:", UEBERSCHRIFT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk("Datum: " + datum, NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk("Uhrzeit: " + uhrzeit, NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk("Abrechnungsnumer: " + Integer.toString(abrechnungID), NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk((String)runApplication.EINSTELLUNGEN.get("Name"), NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk((String)runApplication.EINSTELLUNGEN.get("strasse"), NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk((String)runApplication.EINSTELLUNGEN.get("plz") + " " + (String)runApplication.EINSTELLUNGEN.get("Stadt"), NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk((String)runApplication.EINSTELLUNGEN.get("telefon"), NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk("Seite: " + seite, NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      return textblock;
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename, int abrechnungID) throws DocumentException, IOException, SQLException {
      PDFAbrechnung ps = new PDFAbrechnung();
      ps.createPdf(filename, abrechnungID);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
