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
import data.tabellen.TabelleProtokoll;
import data.tabellen.TabelleVeranstaltung;
import go.Protokoll;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class BerichtPDFSchreiben {

   public void createPdf(String filename, String title, String text, String jahr, String[] grafiken, String[] grafikenBeschreibung, boolean deckblattIsSelected, boolean protokolle) throws DocumentException, IOException, SQLException {
      Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
      Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
      Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner;
      if(deckblattIsSelected) {
         imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
         imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
         imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
         document.add(imgBanner);
         Phrase bericht = new Phrase();
         Chunk textblock = new Chunk(title, UEBERSCHRIFT);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(Chunk.NEWLINE);
         bericht.add(textblock);
         Paragraph chunk = new Paragraph();
         chunk.setAlignment(1);
         chunk.add(bericht);
         document.add(chunk);
         document.newPage();
      }

      imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      String[] var28 = text.split("\n");
      Phrase var29 = new Phrase();
      var29.add(Chunk.NEWLINE);
      var29.add(Chunk.NEWLINE);
      Chunk var30 = new Chunk(title, UEBERSCHRIFT);
      if(!deckblattIsSelected) {
         var29.add(var30);
         var29.add(Chunk.NEWLINE);
         var29.add(Chunk.NEWLINE);
      }

      var30 = new Chunk("Bericht:", NORMAL_BOLD);
      var29.add(var30);
      var29.add(Chunk.NEWLINE);
      var29.add(Chunk.NEWLINE);

      int tabVeranstaltung;
      for(tabVeranstaltung = 0; tabVeranstaltung < var28.length; ++tabVeranstaltung) {
         var30 = new Chunk(var28[tabVeranstaltung], NORMAL_FONT);
         var29.add(var30);
         var29.add(Chunk.NEWLINE);
      }

      var29.add(Chunk.NEWLINE);
      var29.add(Chunk.NEWLINE);
      document.add(new Paragraph(var29));
      document.newPage();

      for(tabVeranstaltung = 0; tabVeranstaltung < grafiken.length; ++tabVeranstaltung) {
         if(grafiken[tabVeranstaltung] != null) {
            Image tabProtokoll = Image.getInstance(grafiken[tabVeranstaltung]);
            tabProtokoll.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(tabProtokoll, 400));
            tabProtokoll.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(tabProtokoll, 550));
            Phrase einsatzListe = new Phrase();
            Chunk textblock3 = new Chunk(grafikenBeschreibung[tabVeranstaltung], UEBERSCHRIFT);
            einsatzListe.add(Chunk.NEWLINE);
            einsatzListe.add(textblock3);
            einsatzListe.add(Chunk.NEWLINE);
            einsatzListe.add(Chunk.NEWLINE);
            document.add(new Paragraph(einsatzListe));
            document.add(tabProtokoll);
         }
      }

      if(protokolle) {
         TabelleVeranstaltung var31 = new TabelleVeranstaltung();
         TabelleProtokoll var32 = new TabelleProtokoll();
         String[] var33 = Utils.listToArray(var31.getAllVeranstaltungEinerKategorieByJahr(1, Integer.parseInt(jahr), 1));
         Phrase var34 = new Phrase();
         Chunk chunk3 = new Chunk("Einsatzberichte:", UEBERSCHRIFT);
         var34.add(Chunk.NEWLINE);
         var34.add(chunk3);
         var34.add(Chunk.NEWLINE);
         var34.add(Chunk.NEWLINE);
         document.add(new Paragraph(var34));

         for(int p = 0; p < var33.length; ++p) {
            int vID = var31.getVeranstaltungID(var33[p]);
            Protokoll protokoll = var32.getData(vID);
            if(protokoll != null) {
               logging.logInfo("Füge Protokoll für VeranstaltungID:" + vID + " + zu Bericht hinzu...");
               Phrase textblock4 = new Phrase();
               Chunk chunk4 = new Chunk(var33[p], UEBERSCHRIFT);
               textblock4.add(Chunk.NEWLINE);
               textblock4.add(chunk4);
               textblock4.add(Chunk.NEWLINE);
               chunk4 = new Chunk(protokoll.getProtokolltext(), NORMAL_FONT);
               textblock4.add(chunk4);
               textblock4.add(Chunk.NEWLINE);
               textblock4.add(Chunk.NEWLINE);
               document.add(new Paragraph(textblock4));
            }
         }
      }

      document.addTitle("FeuerwehrManagementSystem - Bericht " + jahr);
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Bericht " + jahr);
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, String title, String text, String jahr, String[] grafiken, String[] grafikenBeschreibung, boolean deckblattIsSelected, boolean protokolle) throws DocumentException, IOException, SQLException {
      BerichtPDFSchreiben ps = new BerichtPDFSchreiben();
      ps.createPdf(dateiname, title, text, jahr, grafiken, grafikenBeschreibung, deckblattIsSelected, protokolle);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
