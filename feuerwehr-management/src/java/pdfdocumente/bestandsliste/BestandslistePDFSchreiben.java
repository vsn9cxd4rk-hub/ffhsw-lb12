package pdfdocumente.bestandsliste;

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
import data.tabellen.bestandsliste.TabelleLager;
import data.tabellen.bestandsliste.TabelleLager_zugewiesen;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.mitglied.TabelleMitglied;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.Utils;

public class BestandslistePDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font UEBERSCHRIFT2 = new Font(FontFamily.HELVETICA, 18.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);


   public void createPdf(String filename, String name, String typ) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase textblock = new Phrase();
      Chunk chunk = new Chunk("Bestandsliste für " + name, UEBERSCHRIFT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      TabelleFahrzeug tabFahrzeuge = new TabelleFahrzeug();
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleLager tabLager = new TabelleLager();
      TabelleLager_zugewiesen tabZugewiesen = new TabelleLager_zugewiesen();
      String[] lagerOrt = null;
      int id = 0;
      if(typ.equals("L")) {
         id = tabLager.getLagerID(name);
         lagerOrt = Utils.listToArray(tabZugewiesen.getAllOrt("L", id));
      } else if(typ.equals("M")) {
         id = tabMitglied.getIdByGuiString(name);
         lagerOrt = Utils.listToArray(tabZugewiesen.getAllOrt("M", id));
      } else if(typ.equals("F")) {
         id = tabFahrzeuge.getFahrzeugID(name);
         lagerOrt = Utils.listToArray(tabZugewiesen.getAllOrt("F", id));
      }

      for(int i = 0; i < lagerOrt.length; ++i) {
         logging.logInfo("Füge LagerOrt:" + lagerOrt[i] + " zur Liste bei...");
         if(lagerOrt[i].equals("")) {
            chunk = new Chunk("LAGERORT NICHT ZUGEORDNET", UEBERSCHRIFT2);
         } else {
            chunk = new Chunk(lagerOrt[i], UEBERSCHRIFT2);
         }

         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         String[] lagerArtikel = Utils.listToArray(tabZugewiesen.getZugewiesendeArtikelForPDF(typ, id, lagerOrt[i]));

         for(int a = 0; a < lagerArtikel.length; ++a) {
            logging.logInfo("Füge Artiel:" + lagerArtikel[a] + " zur Liste bei...");
            chunk = new Chunk(lagerArtikel[a], NORMAL_FONT);
            textblock.add(chunk);
            textblock.add(Chunk.NEWLINE);
         }

         textblock.add(Chunk.NEWLINE);
         textblock.add(Chunk.NEWLINE);
      }

      document.add(new Paragraph(textblock));
      document.addTitle("FeuerwehrManagementSystem - Bestandsliste");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Bestandsliste");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename, String name, String typ) throws DocumentException, IOException, SQLException {
      BestandslistePDFSchreiben ps = new BestandslistePDFSchreiben();
      ps.createPdf(filename, name, typ);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
