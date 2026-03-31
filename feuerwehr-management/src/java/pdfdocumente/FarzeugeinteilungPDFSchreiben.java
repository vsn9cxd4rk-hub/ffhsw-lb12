package pdfdocumente;

import ao.fahrzeuge.FahrzeugEinteilungAO;
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

public class FarzeugeinteilungPDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font UEBERSCHRIFT2 = new Font(FontFamily.HELVETICA, 18.0F, 1);
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
      Phrase textblock = new Phrase();
      Chunk chunk = new Chunk("Fahrzeugeinteilung für", UEBERSCHRIFT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      chunk = new Chunk(runApplication.letzterVeranstaltungsname, UEBERSCHRIFT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();

      try {
         String[] e = Utils.listToArray(tabFahrzeuge.getAllFahrzeugeOhneAnhaenger());

         int i;
         for(int zeilenÜbrige = 0; zeilenÜbrige < e.length; ++zeilenÜbrige) {
            i = tabFahrzeuge.getFahrzeugID(e[zeilenÜbrige]);
            logging.logInfo("FahrzeugName " + e[zeilenÜbrige] + " - ID: " + i);
            chunk = new Chunk(e[zeilenÜbrige], UEBERSCHRIFT2);
            textblock.add(chunk);
            textblock.add(Chunk.NEWLINE);
            textblock.add(Chunk.NEWLINE);

            for(int platz = 0; platz < 9; ++platz) {
               if(FahrzeugEinteilungAO.sitzplatz[zeilenÜbrige][platz].isVisible()) {
                  chunk = new Chunk(FahrzeugEinteilungAO.sitzplatz_label[zeilenÜbrige][platz].getText(), NORMAL_BOLD);
                  textblock.add(chunk);
                  if(FahrzeugEinteilungAO.sitzplatz[zeilenÜbrige][platz].getSelectedItem().toString().equals("<bitte wählen>")) {
                     chunk = new Chunk(" -- ", NORMAL_FONT);
                     textblock.add(chunk);
                     textblock.add(Chunk.NEWLINE);
                  } else {
                     chunk = new Chunk(FahrzeugEinteilungAO.sitzplatz[zeilenÜbrige][platz].getSelectedItem().toString(), NORMAL_FONT);
                     textblock.add(chunk);
                     textblock.add(Chunk.NEWLINE);
                  }
               }
            }

            textblock.add(Chunk.NEWLINE);
            textblock.add(Chunk.NEWLINE);
         }

         if(!FahrzeugEinteilungAO.textfield.getText().equals("")) {
            chunk = new Chunk("Übrige Personen", UEBERSCHRIFT2);
            textblock.add(chunk);
            textblock.add(Chunk.NEWLINE);
            String[] var13 = FahrzeugEinteilungAO.textfield.getText().split("\n");

            for(i = 0; i < var13.length; ++i) {
               chunk = new Chunk(var13[i], NORMAL_FONT);
               textblock.add(chunk);
               textblock.add(Chunk.NEWLINE);
            }
         }
      } catch (SQLException var12) {
         logging.logPrintStackTrace(var12);
      }

      document.add(new Paragraph(textblock));
      document.addTitle("FeuerwehrManagementSystem - Fahrzeugeinteilung");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Fahrzeugeinteilung");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename) throws DocumentException, IOException, SQLException {
      FarzeugeinteilungPDFSchreiben ps = new FarzeugeinteilungPDFSchreiben();
      ps.createPdf(filename);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
