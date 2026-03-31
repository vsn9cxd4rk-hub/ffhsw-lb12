package pdfdocumente;

import ao.listen.LehrgangsmeldungAO;
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
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.TabelleLehrgangsmeldung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class LehrgangsmeldungPDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String jahr, String filename) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
      TabelleLehrgangsmeldung tabMeldung = new TabelleLehrgangsmeldung();
      Phrase textblock = new Phrase();
      Chunk textblock2;
      String[] ehrungen;
      int e;
      if(tabMeldung.getCount("L") != 0 && LehrgangsmeldungAO.lehrgang_checkbox.isSelected()) {
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         textblock2 = new Chunk("Lehrgangsmeldung für " + jahr + ":", UEBERSCHRIFT);
         textblock.add(textblock2);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         textblock2 = new Chunk("Dies sind die vom Programm automatisch vorgeschlagenden Lehrgangsmeldungen: ", NORMAL_FONT);
         textblock.add(textblock2);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         String[] textblock3 = Utils.listToArray(tabLehrgangKategorie.getAlleRelevantenNamen());

         for(int imgBanner3 = 0; imgBanner3 < textblock3.length; ++imgBanner3) {
            textblock2 = new Chunk(textblock3[imgBanner3] + ":", NORMAL_BOLD);
            textblock.add(textblock2);
            textblock.add(Chunk.NEWLINE);
            ehrungen = Utils.listToArray(tabMeldung.getLehrgangsmeldungByLehrgang(textblock3[imgBanner3]));

            for(e = 0; e < ehrungen.length; ++e) {
               textblock2 = new Chunk(ehrungen[e], NORMAL_FONT);
               textblock.add(textblock2);
               textblock.add(Chunk.NEWLINE);
            }

            textblock.add(Chunk.NEWLINE);
         }
      } else {
         textblock2 = new Chunk("KEINE Lehrgangsmeldungen verfügbar", NORMAL_FONT);
         textblock.add(textblock2);
      }

      Phrase var14 = new Phrase();
      Chunk var13;
      if(tabMeldung.getCount("B") != 0 && LehrgangsmeldungAO.beförderung_checkbox.isSelected()) {
         var14.add(Chunk.NEWLINE);
         var14.add(Chunk.NEWLINE);
         var13 = new Chunk("Beförderungen für " + jahr + ":", UEBERSCHRIFT);
         var14.add(var13);
         var14.add(Chunk.NEWLINE);
         var14.add(Chunk.NEWLINE);
         var13 = new Chunk("Dies sind die vom Programm automatisch vorgeschlagenden Beförderungen: ", NORMAL_FONT);
         var14.add(var13);
         var14.add(Chunk.NEWLINE);
         var14.add(Chunk.NEWLINE);
         String[] var15 = Utils.listToArray(tabMeldung.getBefoerderungen());

         for(int var18 = 0; var18 < var15.length; ++var18) {
            var13 = new Chunk(var15[var18], NORMAL_FONT);
            var14.add(var13);
            var14.add(Chunk.NEWLINE);
            var14.add(Chunk.NEWLINE);
         }

         var14.add(Chunk.NEWLINE);
      } else {
         var13 = new Chunk("KEINE Beförderungen verfügbar", NORMAL_FONT);
         var14.add(var13);
      }

      Phrase var16 = new Phrase();
      Chunk var17;
      if(tabMeldung.getCount("EH") != 0 && LehrgangsmeldungAO.ehrung_checkbox.isSelected()) {
         var16.add(Chunk.NEWLINE);
         var16.add(Chunk.NEWLINE);
         var17 = new Chunk("Ehrungen für " + jahr + ":", UEBERSCHRIFT);
         var16.add(var17);
         var16.add(Chunk.NEWLINE);
         var16.add(Chunk.NEWLINE);
         var17 = new Chunk("Dies sind die vom Programm automatisch vorgeschlagenden Ehrungen: ", NORMAL_FONT);
         var16.add(var17);
         var16.add(Chunk.NEWLINE);
         var16.add(Chunk.NEWLINE);
         ehrungen = Utils.listToArray(tabMeldung.getEhrungen());

         for(e = 0; e < ehrungen.length; ++e) {
            var17 = new Chunk(ehrungen[e], NORMAL_FONT);
            var16.add(var17);
            var16.add(Chunk.NEWLINE);
            var16.add(Chunk.NEWLINE);
         }

         var16.add(Chunk.NEWLINE);
      } else {
         var17 = new Chunk("KEINE Ehrungen verfügbar", NORMAL_FONT);
         var16.add(var17);
      }

      Image var19;
      if(LehrgangsmeldungAO.lehrgang_checkbox.isSelected()) {
         var19 = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
         var19.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(var19, 500));
         var19.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(var19, 520));
         document.add(var19);
         document.add(new Paragraph(textblock));
         document.newPage();
      }

      if(LehrgangsmeldungAO.beförderung_checkbox.isSelected()) {
         var19 = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
         var19.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(var19, 500));
         var19.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(var19, 520));
         document.add(var19);
         document.add(new Paragraph(var14));
      }

      if(LehrgangsmeldungAO.ehrung_checkbox.isSelected()) {
         var19 = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
         var19.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(var19, 500));
         var19.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(var19, 520));
         document.add(var19);
         document.add(new Paragraph(var16));
      }

      document.addTitle("FeuerwehrManagementSystem - Lehrgangsmeldungen " + jahr);
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Lehrgangsmeldungen " + jahr);
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String jahr, String filename) throws DocumentException, IOException, SQLException {
      LehrgangsmeldungPDFSchreiben ps = new LehrgangsmeldungPDFSchreiben();
      ps.createPdf(jahr, filename);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
