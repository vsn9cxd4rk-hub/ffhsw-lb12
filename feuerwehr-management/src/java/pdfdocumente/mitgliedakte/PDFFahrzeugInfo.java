package pdfdocumente.mitgliedakte;

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
import data.tabellen.fahrzeug.TabelleFahrzeug_beschreibung;
import data.tabellen.fahrzeug.TabelleFahrzeug_untersuchung;
import data.tabellen.fahrzeug.TabelleGeraetepruefung;
import go.Fahrzeug;
import go.Fahrzeug_Untersuchung;
import go.Geraetepruefung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class PDFFahrzeugInfo {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 16.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, Fahrzeug fahrzeug) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      TabelleFahrzeug_beschreibung tabBeschreibung = new TabelleFahrzeug_beschreibung();
      TabelleGeraetepruefung tabGeraetepruefung = new TabelleGeraetepruefung();
      TabelleFahrzeug_untersuchung tabUntersuchung = new TabelleFahrzeug_untersuchung();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      Phrase textblock = new Phrase();
      Chunk chunk = new Chunk("                                                                                                   " + (String)runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp("dd.MM.yyyy"), NORMAL_FONT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      chunk = new Chunk("                                                                                                   Editiert durch: " + runApplication.loginName, NORMAL_FONT);
      textblock.add(Chunk.NEWLINE);
      textblock.add(chunk);
      document.add(new Paragraph(textblock));
      Phrase personal = new Phrase();
      Chunk address = new Chunk("Name: " + fahrzeug.getName(), UEBERSCHRIFT);
      personal.add(Chunk.NEWLINE);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      personal.add(Chunk.NEWLINE);
      address = new Chunk("Fahrzeugdaten", NORMAL_BOLD);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Beschreibung: " + tabBeschreibung.getBeschreibungName(fahrzeug.getBeschreibung()), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("KFZ-Kennzeichen: " + fahrzeug.getKennzeichen(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Funkrufname: " + fahrzeug.getFunkrufname(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Ausrückesortierung: " + fahrzeug.getSortierung(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Sitzplätze: " + fahrzeug.getSitzplaetze(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Maximale Besatzung: " + fahrzeug.getMaxBesatzung(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Minimale Besatzung: " + fahrzeug.getMinBesatzung(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      if(fahrzeug.getAnhaenger() == 0) {
         new Chunk("Ist das Fahrzeug ein Anhänger: \'NEIN\'", NORMAL_FONT);
      } else {
         new Chunk("Ist das Fahrzeug ein Anhänger: \'JA\'", NORMAL_FONT);
      }

      personal.add(Chunk.NEWLINE);
      personal.add(Chunk.NEWLINE);
      address = new Chunk("Fahrzeugdaten - Wartung", NORMAL_BOLD);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      if(tabGeraetepruefung.getCount(fahrzeug.getId()) == 0) {
         address = new Chunk("keine Wartungsdaten eingetragen!", NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         logging.logInfo("Keine Gerätewartung gefunden...");
      } else {
         Geraetepruefung untersuchung = tabGeraetepruefung.getData(fahrzeug.getId());
         address = new Chunk("Wartung Stromerzeuger: " + TimeCalculation.parseDateForGUI(untersuchung.getStromerzeuger()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Steckleiter: " + TimeCalculation.parseDateForGUI(untersuchung.getSteckleiter()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Schiebleiter: " + TimeCalculation.parseDateForGUI(untersuchung.getSchiebleiter()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Hydralikaggregat: " + TimeCalculation.parseDateForGUI(untersuchung.getHydraulik()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Pumpe: " + TimeCalculation.parseDateForGUI(untersuchung.getPumpe()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Kettensäge: " + TimeCalculation.parseDateForGUI(untersuchung.getKettensaege()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Doppelkanister: " + TimeCalculation.parseDateForGUI(untersuchung.getDoppelkanister()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Geräteprüfung Allgemein: " + TimeCalculation.parseDateForGUI(untersuchung.getGeraetepruefung_allgm()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Wartung Absturzsicherungsset: " + TimeCalculation.parseDateForGUI(untersuchung.getAbstusiset()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         logging.logInfo("Gerätewartung wurde hinzugefügt...");
      }

      personal.add(Chunk.NEWLINE);
      personal.add(Chunk.NEWLINE);
      address = new Chunk("Fahrzeugdaten - Untersuchung", NORMAL_BOLD);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      if(tabUntersuchung.getCount(fahrzeug.getId()) == 0) {
         address = new Chunk("keine Fahrzeuguntersuchungen eingetragen!", NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         logging.logInfo("Keine Fahrzeuguntersuchungen gefunden...");
      } else {
         Fahrzeug_Untersuchung untersuchung1 = tabUntersuchung.getData(fahrzeug.getId());
         address = new Chunk("Nächster Tüv Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung1.getTüv()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Nächster SP Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung1.getSp()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Nächster Service Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung1.getService()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Nächster Gaswartung Termin: " + TimeCalculation.parseShortDateForGUI(untersuchung1.getGaswartung()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         logging.logInfo("Fahrzuguntersuchung wurde hinzugefügt...");
      }

      document.add(new Paragraph(personal));
      document.addTitle("FeuerwehrManagementSystem - FahrzeugInfo");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("FahrzeugInfo");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, Fahrzeug fahrzeug) throws DocumentException, IOException {
      PDFFahrzeugInfo ps = new PDFFahrzeugInfo();

      try {
         ps.createPdf(dateiname, fahrzeug);
         ps.printMeasures();
         logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }
}
