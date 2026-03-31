package pdfdocumente;

import ao.fahrzeuge.FahrzeugBelegungAO;
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
import data.tabellen.fahrzeug.TabelleFahrzeug;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class FahrzeugbelegungPDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font UEBERSCHRIFT2 = new Font(FontFamily.HELVETICA, 18.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, String veranstaltungName) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase textblock = new Phrase();
      Chunk chunk = new Chunk("Fahrzeugbelegung für", UEBERSCHRIFT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk(veranstaltungName.toString(), UEBERSCHRIFT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();

      try {
         String[] e = Utils.listToArray(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());

         for(int f = 0; f < e.length; ++f) {
            int fahrzeug = tabFahrzeuge.getFahrzeugID(e[f]);
            logging.logInfo("FahrzeugName " + e[f] + " - ID: " + fahrzeug);
            chunk = new Chunk(e[f], UEBERSCHRIFT2);
            textblock.add(chunk);
            textblock.add(Chunk.NEWLINE);
            textblock.add(Chunk.NEWLINE);

            for(int platz = 0; platz < 9; ++platz) {
               if(FahrzeugBelegungAO.sitzplatz[f][platz].isVisible()) {
                  chunk = new Chunk(FahrzeugBelegungAO.sitzplatz_label[f][platz].getText(), NORMAL_BOLD);
                  textblock.add(chunk);
                  if(FahrzeugBelegungAO.sitzplatz[f][platz].getSelectedItem().toString().equals("<bitte wählen>")) {
                     chunk = new Chunk(" -- ", NORMAL_FONT);
                     textblock.add(chunk);
                     textblock.add(Chunk.NEWLINE);
                  } else {
                     chunk = new Chunk(FahrzeugBelegungAO.sitzplatz[f][platz].getSelectedItem().toString(), NORMAL_FONT);
                     textblock.add(chunk);
                     textblock.add(Chunk.NEWLINE);
                  }
               }
            }

            textblock.add(Chunk.NEWLINE);
            textblock.add(Chunk.NEWLINE);
         }
      } catch (SQLException var13) {
         logging.logPrintStackTrace(var13);
      }

      document.add(new Paragraph(textblock));
      document.addTitle("FeuerwehrManagementSystem - Fahrzeugbelegung");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Fahrzeugeinteilung");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename, String veranstaltungName) throws DocumentException, IOException, SQLException {
      FahrzeugbelegungPDFSchreiben ps = new FahrzeugbelegungPDFSchreiben();
      ps.createPdf(filename, veranstaltungName);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
