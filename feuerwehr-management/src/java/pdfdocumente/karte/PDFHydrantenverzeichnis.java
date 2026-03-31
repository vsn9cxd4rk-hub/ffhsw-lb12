package pdfdocumente.karte;

import ao.utils.ProzessBarAO;
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
import data.tabellen.karte.TabelleStrassen;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import service.HydrantService;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class PDFHydrantenverzeichnis {

   private static final Font EINLEITUNG = new Font(FontFamily.HELVETICA, 36.0F, 1);
   private static final Font UEBERSCHRIFT_STRASSE = new Font(FontFamily.HELVETICA, 14.0F, 4);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);


   public void createPdf(String filename) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      TabelleStrassen tabStrassen = new TabelleStrassen();
      HydrantService service = new HydrantService();
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
      Phrase hydrant = new Phrase();
      Chunk einleitung = new Chunk("   HYDRANTENVERZEICHNIS", EINLEITUNG);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(Chunk.NEWLINE);
      hydrant.add(einleitung);
      document.add(new Paragraph(hydrant));
      document.newPage();
      int gesamt = tabStrassen.getCount();

      for(int i = 1; i < gesamt; ++i) {
         document.add(imgBanner);
         Phrase h = new Phrase();
         Chunk hblock = new Chunk(tabStrassen.getStrassenName(i) + ":", UEBERSCHRIFT_STRASSE);
         h.add(Chunk.NEWLINE);
         h.add(Chunk.NEWLINE);
         h.add(hblock);
         h.add(Chunk.NEWLINE);
         h.add(Chunk.NEWLINE);
         hblock = new Chunk(service.getHydrantListe(tabStrassen.getStrassenName(i)), NORMAL_FONT);
         h.add(hblock);
         document.add(new Paragraph(h));
         document.newPage();
         ProzessBarAO.progressbar.setValue(100 * i / gesamt);
      }

      document.addTitle("FeuerwehrManagementSystem - Hydrantenverzeichnis");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Hydrantenverzeichnis");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname) throws DocumentException, IOException {
      PDFHydrantenverzeichnis ps = new PDFHydrantenverzeichnis();

      try {
         ps.createPdf(dateiname);
         ps.printMeasures();
         logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

   }
}
