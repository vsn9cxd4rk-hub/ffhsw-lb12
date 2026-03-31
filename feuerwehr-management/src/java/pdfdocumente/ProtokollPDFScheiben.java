package pdfdocumente;

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
import data.tabellen.TabelleVeranstaltung;
import go.Protokoll;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class ProtokollPDFScheiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 16.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);


   public void createPdf(String filename, Protokoll protokoll) throws DocumentException, IOException, SQLException {
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
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk(protokoll.getTitle(), UEBERSCHRIFT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      if((new TabelleVeranstaltung()).getVeranstaltungKategorieID(protokoll.getVeranstaltungID()) != 1) {
         chunk = new Chunk((new TabelleVeranstaltung()).getVeranstaltungName(protokoll.getVeranstaltungID()), UEBERSCHRIFT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
      }

      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk(protokoll.getProtokolltext(), NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk("Erstellt am: " + TimeCalculation.parseDateForGUI(protokoll.getErstelldatum()), NORMAL_FONT);
      textblock.add(chunk);
      document.add(new Paragraph(textblock));
      document.addTitle("FeuerwehrManagementSystem - Protokoll");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Protokoll");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, Protokoll protokoll) throws DocumentException, IOException {
      ProtokollPDFScheiben ps = new ProtokollPDFScheiben();

      try {
         ps.createPdf(dateiname, protokoll);
         ps.printMeasures();
         logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }
}
