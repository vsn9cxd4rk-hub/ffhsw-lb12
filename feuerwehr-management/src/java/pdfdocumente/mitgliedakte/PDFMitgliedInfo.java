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
import data.tabellen.TabelleDienstgrad;
import data.tabellen.TabelleLehrgang_kategorie;
import data.tabellen.mitglied.TabelleMitglieder_gruppe;
import data.tabellen.mitglied.TabelleMitglieder_laufbahn;
import data.tabellen.mitglied.TabelleMitglieder_untersuchung;
import go.Mitglieder;
import go.Mitglieder_Untersuchung;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class PDFMitgliedInfo {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 16.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, Mitglieder mitglied, Mitglieder_Untersuchung untersuchung) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
      TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
      TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
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
      Chunk address = new Chunk("Name: " + mitglied.getName() + ", " + mitglied.getVorname(), UEBERSCHRIFT);
      personal.add(Chunk.NEWLINE);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      personal.add(Chunk.NEWLINE);
      address = new Chunk("Personendaten", NORMAL_BOLD);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Straße: " + mitglied.getStrasse(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Ort: " + mitglied.getOrt(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Telefon Privat: " + mitglied.getTelefonePrivate(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Telefon Mobil: " + mitglied.getTelefonMobile(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Telefon Arbeit: " + mitglied.getTelefonArbeit(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("E-Mail: " + mitglied.getEmail(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("E-Mail2: " + mitglied.getEmail2(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Dienstgrad: " + tabDienstgrad.getDienstgradBeschreibungLang(mitglied.getDienstgrad()), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Geb-Datum: " + TimeCalculation.parseDateForGUI(mitglied.getGebDatum()), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Mitglieder Gruppe: " + tabGruppe.getGruppenName(mitglied.getMitgliederGruppe()), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Kommentar: " + mitglied.getKommentar(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Beruf: " + mitglied.getBeruf(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      if(((String)runApplication.EINSTELLUNGEN.get("hochzeitFeldFuerMitglieder")).equals("1")) {
         address = new Chunk("Hochzeitstag: " + TimeCalculation.parseDateForGUI(mitglied.getHochzeit()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
      }

      address = new Chunk("Ablauf des Dienstausweises: " + TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufDienstausweis(mitglied.getId())), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      address = new Chunk("Führerscheinnummer: " + mitglied.getFuehrerscheinNummer(), NORMAL_FONT);
      personal.add(Chunk.NEWLINE);
      personal.add(address);
      if(((String)runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungAnzeigen")).equals("1")) {
         address = new Chunk("Ablauf der Fahrbereichtigung: " + TimeCalculation.parseDateForGUI(untersuchung.getPruefungDerFahrberechtigung()), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Fahrbereichtigungnummer: " + mitglied.getFahrberechtigungNummer(), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
      }

      if(((String)runApplication.EINSTELLUNGEN.get("mitgliederGesundheitTab")).equals("1")) {
         personal.add(Chunk.NEWLINE);
         address = new Chunk("Gesundheit", NORMAL_BOLD);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Krankenkasse: " + mitglied.getKrankenkasse(), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Medikamente: " + mitglied.getMedikamente(), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Krankheiten: " + mitglied.getKrankheiten(), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Sportabzeichen: " + mitglied.getSportabzeichen(), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
         address = new Chunk("Schwimmabzeichen: " + mitglied.getSchwimmabzeichen(), NORMAL_FONT);
         personal.add(Chunk.NEWLINE);
         personal.add(address);
      }

      document.add(new Paragraph(personal));
      if(mitglied.getMitgliederGruppe() == 1) {
         Phrase lehrgangData = new Phrase();
         Chunk l = new Chunk("Bestandene Lehrgänge:", NORMAL_BOLD);
         lehrgangData.add(Chunk.NEWLINE);
         lehrgangData.add(Chunk.NEWLINE);
         lehrgangData.add(l);
         TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
         TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
         String[] lehrgangListe = Utils.listToArray(tabLehrgangKategorie.getAlleLehrgängeSeminare());
         int[] lehrgangStatus = tabLaufbahn.getLehrgangSeminarData(mitglied.getId());

         for(int funktionData = 0; funktionData < lehrgangListe.length; ++funktionData) {
            if(lehrgangStatus[funktionData] == 0) {
               logging.logInfo("Lehrgangsstatus == 0 --> Weiter zum nächsten...");
            } else {
               l = new Chunk(lehrgangListe[funktionData], NORMAL_FONT);
               lehrgangData.add(Chunk.NEWLINE);
               lehrgangData.add(l);
            }
         }

         document.add(new Paragraph(lehrgangData));
         Phrase var30 = new Phrase();
         Chunk f = new Chunk("Funktionen in der Feuerwehr:", NORMAL_BOLD);
         var30.add(Chunk.NEWLINE);
         var30.add(Chunk.NEWLINE);
         var30.add(f);
         String[] funktionListe = Utils.listToArray(tabLehrgangKategorie.getAlleFunktionen());
         int[] funktionStatus = tabLaufbahn.getFunktionData(mitglied.getId());

         for(int funktionAußerhalbData = 0; funktionAußerhalbData < funktionListe.length; ++funktionAußerhalbData) {
            if(funktionStatus[funktionAußerhalbData] == 0) {
               logging.logInfo("Lehrgangsstatus == 0 --> Weiter zum nächsten...");
            } else {
               System.out.println("Jop funktion gefunden --> " + funktionListe[funktionAußerhalbData]);
               f = new Chunk(funktionListe[funktionAußerhalbData], NORMAL_FONT);
               var30.add(Chunk.NEWLINE);
               var30.add(f);
            }
         }

         document.add(new Paragraph(var30));
         Phrase var29 = new Phrase();
         Chunk fa = new Chunk("Funktionen außerhalb der Feuerwehr:", NORMAL_BOLD);
         var29.add(Chunk.NEWLINE);
         var29.add(Chunk.NEWLINE);
         var29.add(fa);
         String[] funktionAußerhalbListe = Utils.listToArray(tabLehrgangKategorie.getAlleFunktionenAußerhalb());
         int[] funktionAußerhalbStatus = tabLaufbahn.getFunktionAußerhalbData(mitglied.getId());

         for(int i = 0; i < funktionAußerhalbListe.length; ++i) {
            if(funktionAußerhalbStatus[i] == 0) {
               logging.logInfo("Lehrgangsstatus == 0 --> Weiter zum nächsten...");
            } else {
               fa = new Chunk(funktionAußerhalbListe[i], NORMAL_FONT);
               var29.add(Chunk.NEWLINE);
               var29.add(fa);
            }
         }

         document.add(new Paragraph(var29));
      }

      document.addTitle("FeuerwehrManagementSystem - MitgliedInfo");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("MitgliedInfo");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String dateiname, Mitglieder mitglied, Mitglieder_Untersuchung untersuchung) throws DocumentException, IOException {
      PDFMitgliedInfo ps = new PDFMitgliedInfo();

      try {
         ps.createPdf(dateiname, mitglied, untersuchung);
         ps.printMeasures();
         logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
      }

   }
}
