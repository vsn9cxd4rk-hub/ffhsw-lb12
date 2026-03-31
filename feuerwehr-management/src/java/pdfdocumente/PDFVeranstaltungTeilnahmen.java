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
import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.TabelleVeranstaltung_Kategorie;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.Utils;

public class PDFVeranstaltungTeilnahmen {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 16.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, String veranstaltungKategorieName) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
      TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
      TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
      int kID = tabKategorie.getID(veranstaltungKategorieName);
      Phrase textblock = new Phrase();
      Chunk chunk = new Chunk("                                                                                                   " + (String)runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp("dd.MM.yyyy"), NORMAL_FONT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk("Teilnehmerliste " + veranstaltungKategorieName, UEBERSCHRIFT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      String[] veranstaltungListe = Utils.listToArray(tabVeranstaltung.getAllKommendenVeranstaltungEinerKategorieByJahr(kID, Integer.parseInt(SbcUtils.timeStamp("yyyy"))));

      for(int i = 0; i < veranstaltungListe.length; ++i) {
         chunk = new Chunk(veranstaltungListe[i], NORMAL_BOLD);
         textblock.add(Chunk.NEWLINE);
         textblock.add(chunk);
         String[] teilnehmerListe = Utils.listToArray(tabAnwesenheit.getAnwesendeMitgliederByVeranstaltung(tabVeranstaltung.getVeranstaltungID(veranstaltungListe[i])));

         for(int m = 0; m < teilnehmerListe.length; ++m) {
            chunk = new Chunk(teilnehmerListe[m], NORMAL_FONT);
            textblock.add(Chunk.NEWLINE);
            textblock.add(chunk);
         }

         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
      }

      document.add(new Paragraph(textblock));
      document.addTitle("FeuerwehrManagementSystem - Teilnehmerliste");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("FahrzeugInfo");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, String veranstaltungKategorieName) throws DocumentException, IOException {
      PDFVeranstaltungTeilnahmen ps = new PDFVeranstaltungTeilnahmen();

      try {
         ps.createPdf(dateiname, veranstaltungKategorieName);
         ps.printMeasures();
         logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }
}
