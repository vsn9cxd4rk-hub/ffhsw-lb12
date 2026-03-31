package pdfdocumente.karte;

import ao.karte.KarteAO;
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

public class AlarmInfoPDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      String[] zeilenHydrant = KarteAO.hydrant.getText().split("\n");
      String[] zeilenStaßenInfo = KarteAO.straßenInfo.getText().split("\n");
      String[] zeilenAlamierungsInfo = KarteAO.alamierungsInfo.getText().split("\n");
      String[] zeilenKoordinaten = KarteAO.koordinaten.getText().split("\n");
      String[] zeilenAnfahrt = KarteAO.anfahrtInfo.getText().split("\n");
      Phrase textblock = new Phrase();
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      Chunk chunk = new Chunk("Hydranten:", NORMAL_BOLD);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);

      int image;
      for(image = 0; image < zeilenHydrant.length; ++image) {
         chunk = new Chunk(zeilenHydrant[image], NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
      }

      chunk = new Chunk("Anfahrt:", NORMAL_BOLD);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);

      for(image = 0; image < zeilenAnfahrt.length; ++image) {
         chunk = new Chunk(zeilenAnfahrt[image], NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
      }

      chunk = new Chunk("Starßen Info:", NORMAL_BOLD);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);

      for(image = 0; image < zeilenStaßenInfo.length; ++image) {
         chunk = new Chunk(zeilenStaßenInfo[image], NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
      }

      chunk = new Chunk("Alamierungsinfo:", NORMAL_BOLD);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);

      for(image = 0; image < zeilenAlamierungsInfo.length; ++image) {
         chunk = new Chunk(zeilenAlamierungsInfo[image], NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
      }

      chunk = new Chunk("Koordinaten Stadtplan:", NORMAL_BOLD);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);

      for(image = 0; image < zeilenKoordinaten.length; ++image) {
         chunk = new Chunk(zeilenKoordinaten[image], NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
      }

      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      String var19 = (String)KarteAO.StrasseSuchen.getSelectedItem();
      Image imgG = Image.getInstance("images/street/groß/" + var19 + ".jpg");
      Image imgKL = Image.getInstance("images/street/klein/" + var19 + ".jpg");
      logging.logInfo("images/street/groß/" + var19 + ".jpg");
      imgG.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgG, 500));
      imgG.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgG, 500));
      imgKL.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgKL, 500));
      imgKL.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgKL, 500));
      document.add(new Paragraph(textblock));
      Phrase textblock2 = new Phrase();
      textblock2.add(Chunk.NEWLINE);
      textblock2.add(Chunk.NEWLINE);
      Chunk chunk2 = new Chunk("Katenansicht 1:", UEBERSCHRIFT);
      textblock2.add(chunk2);
      textblock2.add(Chunk.NEWLINE);
      document.add(new Paragraph(textblock2));
      document.add(imgKL);
      Phrase textblock3 = new Phrase();
      textblock3.add(Chunk.NEWLINE);
      textblock3.add(Chunk.NEWLINE);
      Chunk chunk3 = new Chunk("Kartenansicht 2:", UEBERSCHRIFT);
      textblock3.add(chunk3);
      textblock3.add(Chunk.NEWLINE);
      document.add(new Paragraph(textblock3));
      document.add(imgG);
      document.addTitle("FeuerwehrManagementSystem - AlarmInfo (Einsatz)");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("AlarmInfo");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename) throws DocumentException, IOException, SQLException {
      AlarmInfoPDFSchreiben ps = new AlarmInfoPDFSchreiben();
      ps.createPdf(filename);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
