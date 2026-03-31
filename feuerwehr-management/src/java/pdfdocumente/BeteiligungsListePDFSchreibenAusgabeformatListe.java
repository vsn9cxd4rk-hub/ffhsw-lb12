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
import data.tabellen.mitglied.TabelleMitglied;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.Utils;

public class BeteiligungsListePDFSchreibenAusgabeformatListe {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font UEBERSCHRIFT2 = new Font(FontFamily.HELVETICA, 16.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, String mitglied, String jahr, String grafikFile) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleAnwesenheit tabAnwesend = new TabelleAnwesenheit();
      TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
      TabelleVeranstaltung_Kategorie tabKategorie = new TabelleVeranstaltung_Kategorie();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase textblock = new Phrase();
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      Chunk chunk = new Chunk("Beteiligungsübersicht " + jahr + ": " + mitglied, UEBERSCHRIFT);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      int mID = tabMitglied.getIdByGuiString(mitglied);
      chunk = new Chunk("Auflistung in Zahlen: ", UEBERSCHRIFT2);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      String[] kategorie = Utils.listToArray(tabKategorie.getAllKategorien());

      for(int bild = 0; bild < kategorie.length; ++bild) {
         chunk = new Chunk(kategorie[bild] + ": ", NORMAL_BOLD);
         textblock.add(chunk);
         int vKategorie = tabKategorie.getID(kategorie[bild]);
         int mGruppe = tabGruppe.getID(runApplication.mitgliederGruppe);
         String zahl = Integer.toString(tabAnwesend.getBeteiligungByKategorie(mID, vKategorie, Integer.parseInt(jahr)));
         String gesZahl = Integer.toString(tabVeranstaltung.getCountAllVeranstaltungEinesJahresByKategorie(jahr, vKategorie, mGruppe));
         chunk = new Chunk(zahl + " / " + gesZahl, NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
      }

      chunk = new Chunk("Beiteiligungsübersicht Stand: " + SbcUtils.timeStamp("dd.MM.yyyy"), NORMAL_FONT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      document.add(new Paragraph(textblock));
      if(grafikFile != null) {
         Image var22 = Image.getInstance(grafikFile);
         var22.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(var22, 500));
         var22.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(var22, 550));
         document.add(var22);
      }

      document.addTitle("FeuerwehrManagementSystem - Beteiligungsübersicht");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Beteiligungsübersicht");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, String mitglied, String jahr, String grafikFile) throws DocumentException, IOException, SQLException {
      BeteiligungsListePDFSchreibenAusgabeformatListe ps = new BeteiligungsListePDFSchreibenAusgabeformatListe();
      ps.createPdf(dateiname, mitglied, jahr, grafikFile);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
