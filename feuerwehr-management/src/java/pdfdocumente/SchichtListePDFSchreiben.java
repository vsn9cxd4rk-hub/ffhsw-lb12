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
import data.tabellen.schicht.TabelleSchicht;
import data.tabellen.schicht.TabelleSchicht_mitglieder;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class SchichtListePDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font UEBERSCHRIFT2 = new Font(FontFamily.HELVETICA, 16.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);


   public void createPdf(String filename, String schichtMonat, String jahr) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      TabelleSchicht tabSchicht = new TabelleSchicht();
      TabelleSchicht_mitglieder tabSchichtMitglieder = new TabelleSchicht_mitglieder();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase textblock = new Phrase();
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      Chunk chunk = new Chunk("Schichtübersicht: " + schichtMonat + " " + jahr, UEBERSCHRIFT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      String monatNummer = null;
      if(schichtMonat.equals("Januar")) {
         monatNummer = "01";
      } else if(schichtMonat.equals("Februar")) {
         monatNummer = "02";
      } else if(schichtMonat.equals("März")) {
         monatNummer = "03";
      } else if(schichtMonat.equals("April")) {
         monatNummer = "04";
      } else if(schichtMonat.equals("Mai")) {
         monatNummer = "05";
      } else if(schichtMonat.equals("Juni")) {
         monatNummer = "06";
      } else if(schichtMonat.equals("Juli")) {
         monatNummer = "07";
      } else if(schichtMonat.equals("August")) {
         monatNummer = "08";
      } else if(schichtMonat.equals("September")) {
         monatNummer = "09";
      } else if(schichtMonat.equals("Oktober")) {
         monatNummer = "10";
      } else if(schichtMonat.equals("November")) {
         monatNummer = "11";
      } else if(schichtMonat.equals("Dezember")) {
         monatNummer = "12";
      }

      String[] schichtName = Utils.listToArray(tabSchicht.getAllSchichtenEinesMonats(monatNummer, jahr));

      for(int s = 0; s < schichtName.length; ++s) {
         int sID = tabSchicht.getSchichtID(schichtName[s]);
         chunk = new Chunk(schichtName[s] + ": ", UEBERSCHRIFT2);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
         String[] teilnehmer = Utils.listToArray(tabSchichtMitglieder.getMitglederEinerSchicht(sID));

         for(int t = 0; t < teilnehmer.length; ++t) {
            chunk = new Chunk(teilnehmer[t], NORMAL_FONT);
            textblock.add(chunk);
            textblock.add(Chunk.NEWLINE);
         }

         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
      }

      document.add(new Paragraph(textblock));
      document.addTitle("FeuerwehrManagementSystem - Schichtliste " + schichtMonat + " " + jahr);
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Schichtliste " + schichtMonat + " " + jahr);
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, String schichtMonat, String jahr) throws DocumentException, IOException, SQLException {
      SchichtListePDFSchreiben ps = new SchichtListePDFSchreiben();
      ps.createPdf(dateiname, schichtMonat, jahr);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
