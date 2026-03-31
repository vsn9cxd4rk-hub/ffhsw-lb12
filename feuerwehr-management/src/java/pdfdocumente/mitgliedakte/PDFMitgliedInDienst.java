package pdfdocumente.mitgliedakte;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class PDFMitgliedInDienst {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 16.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);


   public void createPdf(String filename, String mitglied) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase textblock = new Phrase();
      Chunk chunk = new Chunk("                                                                                                   " + (String)runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp("dd.MM.yyyy"), NORMAL_FONT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      chunk = new Chunk("                                                                                                   Editiert durch: " + runApplication.loginName, NORMAL_FONT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      document.add(new Paragraph(textblock));
      Phrase personal = new Phrase();
      Chunk address = new Chunk("Name: " + mitglied, UEBERSCHRIFT);
      personal.add(Chunk.NEWLINE);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      personal.add(Chunk.NEWLINE);
      address = new Chunk("Das Mitglied " + mitglied + " wurde mit dem heutigen Tage wieder in Dienst gestellt.", NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      document.add(new Paragraph(personal));
      document.addTitle("FeuerwehrManagementSystem - MitgliedInfo");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("MitgliedInfo");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, String mitglied) throws DocumentException, IOException {
      PDFMitgliedInDienst ps = new PDFMitgliedInDienst();

      try {
         ps.createPdf(dateiname, mitglied);
         ps.printMeasures();
         logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }
}
