package pdfdocumente;

import ao.BriefAO;
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
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_anrede;
import go.DokumentLayoutOptions;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;

public class BriefPDFSchreiben {

   private static Font UEBERSCHRIFT;
   private static Font NORMAL_FONT;
   private static Font NORMAL_KLEIN;


   public void createPdf(String filename, String anWen) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      String[] brief = BriefAO.textfield.getText().split("\n");
      String[] empfänger = anWen.split("\n");
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleMitglieder_anrede tabAnrede = new TabelleMitglieder_anrede();

      for(int e = 0; e < empfänger.length; ++e) {
         int mID = tabMitglieder.getIdByGuiString(empfänger[e].toString());
         this.createDocumentHeadder(document, empfänger, tabMitglieder, tabAnrede, e, mID);
         Phrase textblock = new Phrase();
         this.createBetreff(tabMitglieder, tabAnrede, mID, textblock);
         logging.logInfo("Brief Länge: " + brief.length);
         Chunk chunk;
         int i;
         if(brief.length >= 26) {
            for(i = 0; i < 26; ++i) {
               chunk = new Chunk(brief[i], NORMAL_FONT);
               textblock.add(chunk);
               textblock.add(Chunk.NEWLINE);
            }

            document.add(new Paragraph(textblock));
            document.newPage();
            this.createDocumentHeadder(document, empfänger, tabMitglieder, tabAnrede, e, mID);
            Phrase textblock2 = new Phrase();
            textblock2.add(Chunk.NEWLINE);
            Chunk var16 = new Chunk("Seite 2", NORMAL_FONT);
            textblock2.add(var16);
            textblock2.add(Chunk.NEWLINE);
            textblock2.add(Chunk.NEWLINE);

            for(int n = 26; n < brief.length; ++n) {
               var16 = new Chunk(brief[n], NORMAL_FONT);
               textblock2.add(var16);
               textblock2.add(Chunk.NEWLINE);
            }

            document.add(new Paragraph(textblock2));
            document.newPage();
         } else {
            for(i = 0; i < brief.length; ++i) {
               chunk = new Chunk(brief[i], NORMAL_FONT);
               textblock.add(chunk);
               textblock.add(Chunk.NEWLINE);
            }

            textblock.add(Chunk.NEWLINE);
            textblock.add(Chunk.NEWLINE);
            document.add(new Paragraph(textblock));
            document.newPage();
         }
      }

      document.addTitle("FeuerwehrManagementSystem - Brief");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Bericht");
      document.close();
      writer.close();
   }

   private void createDocumentHeadder(Document document, String[] empfänger, TabelleMitglied tabMitglieder, TabelleMitglieder_anrede tabAnrede, int e, int mID) throws BadElementException, MalformedURLException, IOException, SQLException, DocumentException {
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase adressTextblock = new Phrase();
      Chunk address = new Chunk((String)runApplication.EINSTELLUNGEN.get("Name") + ", " + (String)runApplication.EINSTELLUNGEN.get("strasse") + ", " + (String)runApplication.EINSTELLUNGEN.get("plz") + " " + (String)runApplication.EINSTELLUNGEN.get("Stadt"), NORMAL_KLEIN);
      adressTextblock.add(Chunk.NEWLINE);
      adressTextblock.add(Chunk.NEWLINE);
      adressTextblock.add(address);
      address = new Chunk(tabAnrede.getAnredeName(tabMitglieder.getAnrede(mID)), NORMAL_FONT);
      adressTextblock.add(Chunk.NEWLINE);
      adressTextblock.add(address);
      address = new Chunk(tabMitglieder.getVorname(mID) + " " + tabMitglieder.getName(mID), NORMAL_FONT);
      adressTextblock.add(Chunk.NEWLINE);
      adressTextblock.add(address);
      address = new Chunk(tabMitglieder.getStrasse(mID), NORMAL_FONT);
      adressTextblock.add(Chunk.NEWLINE);
      adressTextblock.add(address);
      address = new Chunk(tabMitglieder.getOrt(mID), NORMAL_FONT);
      adressTextblock.add(Chunk.NEWLINE);
      adressTextblock.add(address);
      document.add(new Paragraph(adressTextblock));
      Phrase textblockDatumStadt = new Phrase();
      Chunk chunk = new Chunk((String)runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp("dd.MM.yyyy"), NORMAL_FONT);
      textblockDatumStadt.add(Chunk.NEWLINE);
      textblockDatumStadt.add(chunk);
      Paragraph datum = new Paragraph();
      datum.add(textblockDatumStadt);
      datum.setAlignment(2);
      document.add(datum);
   }

   private Phrase createBetreff(TabelleMitglied tabMitglieder, TabelleMitglieder_anrede tabAnrede, int mID, Phrase textblock) throws SQLException {
      Chunk chunk = new Chunk(BriefAO.title.getText(), UEBERSCHRIFT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      int aID = tabMitglieder.getAnrede(mID);
      String anrede = tabAnrede.getAnredeBrief(aID) + " " + tabAnrede.getAnredeName(aID);
      chunk = new Chunk(anrede + " " + tabMitglieder.getName(mID) + ",", NORMAL_FONT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      return textblock;
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, String anWen, DokumentLayoutOptions dokOption) throws DocumentException, IOException {
      BriefPDFSchreiben ps = new BriefPDFSchreiben();
      UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 16.0F, 1);
      NORMAL_FONT = new Font(dokOption.getNormalSchriftart(), (float)dokOption.getNormalSchriftgröße(), 0);
      NORMAL_KLEIN = new Font(FontFamily.HELVETICA, 8.0F, 4);

      try {
         ps.createPdf(dateiname, anWen);
         ps.printMeasures();
         logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

   }
}
