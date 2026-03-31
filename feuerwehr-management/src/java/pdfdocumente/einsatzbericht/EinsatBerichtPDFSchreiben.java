package pdfdocumente.einsatzbericht;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import data.tabellen.TabelleEinsatz_bericht_elemente;
import data.tabellen.TabelleEinsatz_zeiten;
import data.tabellen.TabelleStichwort;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.mitglied.TabelleMitglied;
import go.EinsatzBerichtDaten;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class EinsatBerichtPDFSchreiben {

   private static final Font UEBERSCHRIFT = new Font(FontFamily.HELVETICA, 22.0F, 1);
   private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 12.0F, 0);
   private static final Font NORMAL_BOLD = new Font(FontFamily.HELVETICA, 12.0F, 1);


   public void createPdf(String filename, EinsatzBerichtDaten berichtDaten, HashMap map) throws DocumentException, IOException, SQLException {
      Document document = new Document();
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
      document.open();
      Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
      imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
      imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
      document.add(imgBanner);
      TabelleEinsatz_zeiten tabZeiten = new TabelleEinsatz_zeiten();
      TabelleStichwort tabStichwort = new TabelleStichwort();
      TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
      TabelleMitglied tabMitglied = new TabelleMitglied();
      TabelleEinsatz_bericht_elemente tabElemente = new TabelleEinsatz_bericht_elemente();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      Phrase textblock = new Phrase();
      Phrase textblock2 = new Phrase();
      Phrase textblock3 = new Phrase();
      Phrase textblock4 = new Phrase();
      PdfPTable table = null;

      try {
         int e = tabZeiten.getCountGesamtFahrzeuge(berichtDaten.getVeranstaltungID());
         table = new PdfPTable(e + 1);
         String[] einsatzArtListe = Utils.listToArray(tabElemente.getElemente("EinsatzArt"));
         String[] stelleListe = Utils.listToArray(tabElemente.getElemente("Stelle"));
         String[] objektListe = Utils.listToArray(tabElemente.getElemente("Objekt"));
         String[] ausdehungListe = Utils.listToArray(tabElemente.getElemente("Ausdehnung"));
         Chunk chunk = new Chunk("EINSATZBERICHT   ", UEBERSCHRIFT);
         textblock.add(Chunk.NEWLINE);
         textblock.add(chunk);
         chunk = new Chunk(tabVeranstaltung.getVeranstaltungName2(berichtDaten.getVeranstaltungID()), UEBERSCHRIFT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Einsatz Nummer: " + (String)map.get("einsatznummerOffiziell"), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Datum: " + TimeCalculation.parseDateForGUI((String)map.get("Datum")), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Ort: " + (String)runApplication.EINSTELLUNGEN.get("Stadt"), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Straße: " + (String)map.get("Ort"), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Stadtteil: " + (String)map.get("stadtteil"), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Stichwort: " + tabStichwort.getStichwortName(Integer.parseInt((String)map.get("Stichwort"))), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Fahrzeuge Im Einsatz: " + (String)map.get("Fahrzeug"), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         if(((String)runApplication.EINSTELLUNGEN.get("einsatzleiterBF")).equals("1")) {
            chunk = new Chunk("1. Gruppenführer FF: " + tabMitglied.getName(Integer.parseInt((String)map.get("einsatzleiter"))) + ", " + tabMitglied.getVorname(Integer.parseInt((String)map.get("einsatzleiter"))), NORMAL_FONT);
            textblock.add(chunk);
            textblock.add(Chunk.NEWLINE);
            if(((String)map.get("einsatzleiterBF")).equals("")) {
               chunk = new Chunk("Einsatzleiter BF:  (-)", NORMAL_FONT);
               textblock.add(chunk);
               textblock.add(Chunk.NEWLINE);
            } else {
               chunk = new Chunk("Einsatzleiter BF: " + (String)map.get("einsatzleiterBF"), NORMAL_FONT);
               textblock.add(chunk);
               textblock.add(Chunk.NEWLINE);
            }
         } else {
            chunk = new Chunk("Einsatzleiter: " + tabMitglied.getName(Integer.parseInt((String)map.get("einsatzleiter"))) + ", " + tabMitglied.getVorname(Integer.parseInt((String)map.get("einsatzleiter"))), NORMAL_FONT);
            textblock.add(chunk);
            textblock.add(Chunk.NEWLINE);
            textblock.add(Chunk.NEWLINE);
         }

         chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Einsatz Art:", NORMAL_BOLD);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         String einsatzArtName = tabElemente.getName(berichtDaten.getEinsatzArt());
         int itemCounter = 0;

         for(int stelleName = 0; stelleName < einsatzArtListe.length; ++stelleName) {
            if(itemCounter == 4) {
               textblock.add(Chunk.NEWLINE);
               itemCounter = 0;
            }

            if(einsatzArtName.equals(einsatzArtListe[stelleName])) {
               chunk = new Chunk(" (X) ", NORMAL_BOLD);
               textblock.add(chunk);
            } else {
               chunk = new Chunk(" (_) ", NORMAL_FONT);
               textblock.add(chunk);
            }

            chunk = new Chunk(einsatzArtListe[stelleName], NORMAL_FONT);
            textblock.add(chunk);
            chunk = new Chunk("   ", NORMAL_FONT);
            textblock.add(chunk);
            ++itemCounter;
         }

         itemCounter = 0;
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Stelle des Einsatzes:", NORMAL_BOLD);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         String var35 = tabElemente.getName(berichtDaten.getStelle());

         for(int objektName = 0; objektName < stelleListe.length; ++objektName) {
            if(itemCounter == 4) {
               textblock.add(Chunk.NEWLINE);
               itemCounter = 0;
            }

            if(var35.equals(stelleListe[objektName])) {
               chunk = new Chunk(" (X) ", NORMAL_BOLD);
               textblock.add(chunk);
            } else {
               chunk = new Chunk(" (_) ", NORMAL_FONT);
               textblock.add(chunk);
            }

            chunk = new Chunk(stelleListe[objektName], NORMAL_FONT);
            textblock.add(chunk);
            chunk = new Chunk("   ", NORMAL_FONT);
            textblock.add(chunk);
            ++itemCounter;
         }

         itemCounter = 0;
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Objekt:", NORMAL_BOLD);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         String var36 = tabElemente.getName(berichtDaten.getObjekt());

         for(int chunk2 = 0; chunk2 < objektListe.length; ++chunk2) {
            if(itemCounter == 3) {
               textblock.add(Chunk.NEWLINE);
               itemCounter = 0;
            }

            if(var36.equals(objektListe[chunk2])) {
               chunk = new Chunk(" (X) ", NORMAL_BOLD);
               textblock.add(chunk);
            } else {
               chunk = new Chunk(" (_) ", NORMAL_FONT);
               textblock.add(chunk);
            }

            chunk = new Chunk(objektListe[chunk2], NORMAL_FONT);
            textblock.add(chunk);
            chunk = new Chunk("   ", NORMAL_FONT);
            textblock.add(chunk);
            ++itemCounter;
         }

         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Eigentümer / Geschädigter:", NORMAL_BOLD);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Name:  " + berichtDaten.getEigentuemerName(), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Anschrift:  " + berichtDaten.getEigentuemerAnschrift(), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Telefon:  " + berichtDaten.getEigentuemerTelefon(), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Verursacher:", NORMAL_BOLD);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Name:  " + berichtDaten.getVerursacherName(), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Anschrift:  " + berichtDaten.getVerursacherAnschrift(), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         chunk = new Chunk("Telefon:  " + berichtDaten.getVerursacherTelefon(), NORMAL_FONT);
         textblock.add(chunk);
         textblock.add(Chunk.NEWLINE);
         Chunk var37 = new Chunk("Alamierung:", NORMAL_BOLD);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("Erfolgt um:   " + (String)map.get("ZeitAlarm") + " Uhr", NORMAL_FONT);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("Erfolgt über: " + tabElemente.getName(berichtDaten.getAlamierung()), NORMAL_FONT);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("Meldender:", NORMAL_BOLD);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("Name: " + berichtDaten.getMeldenderName(), NORMAL_FONT);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("Anschrift: " + berichtDaten.getMeldenderAnschrift(), NORMAL_FONT);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("Telefon: " + berichtDaten.getMeldenderTelefon(), NORMAL_FONT);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         var37 = new Chunk("Eingesetzte Fahrzeuge:", NORMAL_BOLD);
         textblock2.add(var37);
         textblock2.add(Chunk.NEWLINE);
         textblock2.add(Chunk.NEWLINE);
         table.addCell("Name:\n\nAusgerückt:\nEingetroffen:\nEingerückt:\n\nGesamtzeit:");
         int[] fahrzeugID = Utils.listToIntArray(tabZeiten.getFahrzeugListe(berichtDaten.getVeranstaltungID()));

         for(int chunk3 = 0; chunk3 < fahrzeugID.length; ++chunk3) {
            HashMap chunk4 = tabZeiten.getData(berichtDaten.getVeranstaltungID(), fahrzeugID[chunk3]);
            table.addCell(tabFahrzeug.getFahrzeugName(Integer.parseInt((String)chunk4.get("fahrzeugID"))) + "\n\n" + (String)chunk4.get("zeitAusgerueckt") + "\n" + (String)chunk4.get("zeitEingetroffen") + "\n" + (String)chunk4.get("zeitEingerueckt") + "\n\n" + TimeCalculation.minutenInStundenUmrechnen(TimeCalculation.calculateDuration((String)chunk4.get("zeitAlarm"), (String)chunk4.get("zeitEingerueckt"))));
         }

         textblock3.add(Chunk.NEWLINE);
         Chunk var38 = new Chunk("Gesamtstärke:", NORMAL_BOLD);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         if(((String)map.get("staerkeZF")).equals("0")) {
            var38 = new Chunk("Einsatzgesamtstärke: " + (String)map.get("staerkeGF") + " / " + (String)map.get("staerkeFM"), NORMAL_FONT);
            textblock3.add(var38);
            textblock3.add(Chunk.NEWLINE);
         } else {
            var38 = new Chunk("Einsatzgesamtstärke: " + (String)map.get("staerkeZF") + " / " + (String)map.get("staerkeGF") + " / " + (String)map.get("staerkeFM"), NORMAL_FONT);
            textblock3.add(var38);
            textblock3.add(Chunk.NEWLINE);
         }

         var38 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         var38 = new Chunk("Vorgefundene Lage:", NORMAL_BOLD);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         var38 = new Chunk(berichtDaten.getLage(), NORMAL_FONT);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         textblock3.add(Chunk.NEWLINE);
         var38 = new Chunk("Verlauf des Einsatzes:", NORMAL_BOLD);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         var38 = new Chunk(berichtDaten.getVerlauf(), NORMAL_FONT);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         textblock3.add(Chunk.NEWLINE);
         var38 = new Chunk("Eingesetzte Geräte und Mittel:", NORMAL_BOLD);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         var38 = new Chunk(berichtDaten.getEingesetzteGeraete(), NORMAL_FONT);
         textblock3.add(var38);
         textblock3.add(Chunk.NEWLINE);
         textblock3.add(Chunk.NEWLINE);
         Chunk var39 = new Chunk("Verbrauch:", NORMAL_BOLD);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Verbrauch Wasser: " + berichtDaten.getVerbrauchWasser(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Verbrauch Pulver: " + berichtDaten.getVerbrauchPulver(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Verbrauch Schaum: " + berichtDaten.getVerbrauchSchaum(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Verbrauch Bindemittel: " + berichtDaten.getVerbrauchBindemittel(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Geräte im Detail:", NORMAL_BOLD);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Tragbare Leitern: " + berichtDaten.getTragbareLeitern(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Atemschutzgerät: " + berichtDaten.getAtemschutzgeraet(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Fluchthauben: " + berichtDaten.getFluchthauben(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Belüftungsgerät: " + berichtDaten.getBelueftungsgeraet(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Hydraul. Rettungsgerät: " + berichtDaten.getRettungsgeraet(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         itemCounter = 0;
         var39 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Ausdehnung:", NORMAL_BOLD);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         String ausdehnungName = tabElemente.getName(berichtDaten.getAusdehnung());

         for(int brandwacheFahrzeugName = 0; brandwacheFahrzeugName < ausdehungListe.length; ++brandwacheFahrzeugName) {
            if(itemCounter == 1) {
               textblock4.add(Chunk.NEWLINE);
               itemCounter = 0;
            }

            if(ausdehnungName.equals(ausdehungListe[brandwacheFahrzeugName])) {
               var39 = new Chunk(" (X) ", NORMAL_BOLD);
               textblock4.add(var39);
            } else {
               var39 = new Chunk(" (_) ", NORMAL_FONT);
               textblock4.add(var39);
            }

            var39 = new Chunk(ausdehungListe[brandwacheFahrzeugName], NORMAL_FONT);
            textblock4.add(var39);
            var39 = new Chunk("   ", NORMAL_FONT);
            textblock4.add(var39);
            ++itemCounter;
         }

         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Schaden:", NORMAL_BOLD);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Verletzte: " + berichtDaten.getVerletzte(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Gerettete: " + berichtDaten.getGerettete(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Tote: " + berichtDaten.getTote(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Sachschaden: " + berichtDaten.getSchadenhoehe(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Brandwache:", NORMAL_BOLD);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         String var40 = tabFahrzeug.getFahrzeugName(berichtDaten.getBrandwacheFahrzeug());
         if(var40 == null) {
            var39 = new Chunk("Fahrzeug:  (-)", NORMAL_FONT);
            textblock4.add(var39);
            textblock4.add(Chunk.NEWLINE);
         } else {
            var39 = new Chunk("Fahrzeug: " + var40, NORMAL_FONT);
            textblock4.add(var39);
            textblock4.add(Chunk.NEWLINE);
         }

         var39 = new Chunk("Stärke: " + berichtDaten.getStaerke(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Dauer: " + berichtDaten.getDauer(), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("--------------------------------------------------------------------------------------------------------------------", NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         textblock4.add(Chunk.NEWLINE);
         textblock4.add(Chunk.NEWLINE);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("________________________________", NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Unterschrift: " + tabMitglied.getEinsatzleiter(Integer.parseInt((String)map.get("einsatzleiter"))), NORMAL_FONT);
         textblock4.add(var39);
         textblock4.add(Chunk.NEWLINE);
         var39 = new Chunk("Erstellt am: " + SbcUtils.timeStamp("EEEE\',\'  dd.MM.yyyy  HH:mm:ss"), NORMAL_FONT);
         textblock4.add(var39);
      } catch (SQLException var34) {
         logging.logPrintStackTrace(var34);
      }

      document.add(new Paragraph(textblock));
      document.newPage();
      document.add(new Paragraph(textblock2));
      document.add(table);
      document.add(new Paragraph(textblock3));
      document.newPage();
      document.add(new Paragraph(textblock4));
      document.addTitle("FeuerwehrManagementSystem - Einsatz Bericht");
      document.addAuthor("FeuerwehrManagementSystem");
      document.addCreationDate();
      document.addSubject("Einsatzbericht");
      document.close();
      writer.close();
   }

   private void printMeasures() {
      logging.logInfo("A4-Maße: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527D + "mm x " + (double)PageSize.A4.getHeight() * 0.3527D + "mm");
   }

   public static void PDFdocumentErstellen(String filename, EinsatzBerichtDaten berichtDaten, HashMap map) throws DocumentException, IOException, SQLException {
      EinsatBerichtPDFSchreiben ps = new EinsatBerichtPDFSchreiben();
      ps.createPdf(filename, berichtDaten, map);
      ps.printMeasures();
      logging.logInfo("Datensatz wurde in eine PDF Datei Exportiert");
   }
}
