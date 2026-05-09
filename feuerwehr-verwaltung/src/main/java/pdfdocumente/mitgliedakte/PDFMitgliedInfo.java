/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.Chunk
 *  com.itextpdf.text.Document
 *  com.itextpdf.text.DocumentException
 *  com.itextpdf.text.Element
 *  com.itextpdf.text.Font
 *  com.itextpdf.text.Font$FontFamily
 *  com.itextpdf.text.Image
 *  com.itextpdf.text.PageSize
 *  com.itextpdf.text.Paragraph
 *  com.itextpdf.text.Phrase
 *  com.itextpdf.text.pdf.PdfWriter
 *  logging.logging
 *  utilities.SbcUtils
 */
package pdfdocumente.mitgliedakte;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
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
import java.io.OutputStream;
import java.sql.SQLException;
import logging.logging;
import run.runApplication;
import utilities.BildUmrechnenService;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class PDFMitgliedInfo {
    private static final Font UEBERSCHRIFT = new Font(Font.FontFamily.HELVETICA, 16.0f, 1);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 12.0f, 0);
    private static final Font NORMAL_BOLD = new Font(Font.FontFamily.HELVETICA, 12.0f, 1);

    public void createPdf(String filename, Mitglieder mitglied, Mitglieder_Untersuchung untersuchung) throws DocumentException, IOException, SQLException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(filename));
        document.open();
        TabelleDienstgrad tabDienstgrad = new TabelleDienstgrad();
        TabelleMitglieder_gruppe tabGruppe = new TabelleMitglieder_gruppe();
        TabelleMitglieder_untersuchung tabUntersuchung = new TabelleMitglieder_untersuchung();
        Image imgBanner = Image.getInstance((String)runApplication.EINSTELLUNGEN.get("briefkopf"));
        imgBanner.scaleAbsoluteHeight((float)BildUmrechnenService.bildHoeheVerkleinernPDF(imgBanner, 500));
        imgBanner.scaleAbsoluteWidth((float)BildUmrechnenService.bildBreiteVerkleinernPDF(imgBanner, 520));
        document.add((Element)imgBanner);
        Phrase textblock = new Phrase();
        Chunk chunk = new Chunk("                                                                                                   " + runApplication.EINSTELLUNGEN.get("Stadt") + ", den " + SbcUtils.timeStamp((String)"dd.MM.yyyy"), NORMAL_FONT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        chunk = new Chunk("                                                                                                   Editiert durch: " + runApplication.loginName, NORMAL_FONT);
        textblock.add((Element)Chunk.NEWLINE);
        textblock.add((Element)chunk);
        document.add((Element)new Paragraph(textblock));
        Phrase personal = new Phrase();
        Chunk address = new Chunk("Name: " + mitglied.getName() + ", " + mitglied.getVorname(), UEBERSCHRIFT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        personal.add((Element)Chunk.NEWLINE);
        address = new Chunk("Personendaten:", NORMAL_BOLD);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Stra\u00dfe: " + mitglied.getStrasse(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Ort: " + mitglied.getOrt(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Telefon Privat: " + mitglied.getTelefonePrivate(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Telefon Mobil: " + mitglied.getTelefonMobile(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Telefon Arbeit: " + mitglied.getTelefonArbeit(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("E-Mail: " + mitglied.getEmail(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("E-Mail2: " + mitglied.getEmail2(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Dienstgrad: " + tabDienstgrad.getDienstgradBeschreibungLang(mitglied.getDienstgrad()), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Geb-Datum: " + TimeCalculation.parseDateForGUI(mitglied.getGebDatum()), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Mitglieder Gruppe: " + tabGruppe.getGruppenName(mitglied.getMitgliederGruppe()), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Kommentar: " + mitglied.getKommentar(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("Beruf: " + mitglied.getBeruf(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        if (runApplication.EINSTELLUNGEN.get("hochzeitFeldFuerMitglieder").equals("1")) {
            address = new Chunk("Hochzeitstag: " + TimeCalculation.parseDateForGUI(mitglied.getHochzeit()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
        }
        address = new Chunk("Ablauf des Dienstausweises: " + TimeCalculation.parseDateForGUI(tabUntersuchung.getAblaufDienstausweis(mitglied.getId())), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        address = new Chunk("F\u00fchrerscheinnummer: " + mitglied.getFuehrerscheinNummer(), NORMAL_FONT);
        personal.add((Element)Chunk.NEWLINE);
        personal.add((Element)address);
        if (runApplication.EINSTELLUNGEN.get("ablaufFahrberechtigungAnzeigen").equals("1")) {
            address = new Chunk("Ablauf der Fahrbereichtigung: " + TimeCalculation.parseDateForGUI(untersuchung.getPruefungDerFahrberechtigung()), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
            address = new Chunk("Fahrbereichtigungnummer: " + mitglied.getFahrberechtigungNummer(), NORMAL_FONT);
            personal.add((Element)Chunk.NEWLINE);
            personal.add((Element)address);
        }
        document.add((Element)new Paragraph(personal));
        if (mitglied.getMitgliederGruppe() == 1) {
            Phrase lehrgangData = new Phrase();
            Chunk l = new Chunk("Bestandene Lehrg\u00e4nge:", NORMAL_BOLD);
            lehrgangData.add((Element)Chunk.NEWLINE);
            lehrgangData.add((Element)Chunk.NEWLINE);
            lehrgangData.add((Element)l);
            TabelleMitglieder_laufbahn tabLaufbahn = new TabelleMitglieder_laufbahn();
            TabelleLehrgang_kategorie tabLehrgangKategorie = new TabelleLehrgang_kategorie();
            String[] lehrgangListe = Utils.listToArray(tabLehrgangKategorie.getAlleLehrg\u00e4ngeSeminare());
            int[] lehrgangStatus = tabLaufbahn.getLehrgangSeminarData(mitglied.getId());
            int i = 0;
            while (i < lehrgangListe.length) {
                if (lehrgangStatus[i] == 0) {
                    logging.logInfo((Object)"Lehrgangsstatus == 0 --> Weiter zum n\u00e4chsten...");
                } else {
                    l = new Chunk(lehrgangListe[i], NORMAL_FONT);
                    lehrgangData.add((Element)Chunk.NEWLINE);
                    lehrgangData.add((Element)l);
                }
                ++i;
            }
            document.add((Element)new Paragraph(lehrgangData));
            Phrase funktionData = new Phrase();
            Chunk f = new Chunk("Funktionen in der Feuerwehr:", NORMAL_BOLD);
            funktionData.add((Element)Chunk.NEWLINE);
            funktionData.add((Element)Chunk.NEWLINE);
            funktionData.add((Element)f);
            String[] funktionListe = Utils.listToArray(tabLehrgangKategorie.getAlleFunktionen());
            int[] funktionStatus = tabLaufbahn.getFunktionData(mitglied.getId());
            int i2 = 0;
            while (i2 < funktionListe.length) {
                if (funktionStatus[i2] == 0) {
                    logging.logInfo((Object)"Lehrgangsstatus == 0 --> Weiter zum n\u00e4chsten...");
                } else {
                    System.out.println("Jop funktion gefunden --> " + funktionListe[i2]);
                    f = new Chunk(funktionListe[i2], NORMAL_FONT);
                    funktionData.add((Element)Chunk.NEWLINE);
                    funktionData.add((Element)f);
                }
                ++i2;
            }
            document.add((Element)new Paragraph(funktionData));
            Phrase funktionAu\u00dferhalbData = new Phrase();
            Chunk fa = new Chunk("Funktionen au\u00dferhalb der Feuerwehr:", NORMAL_BOLD);
            funktionAu\u00dferhalbData.add((Element)Chunk.NEWLINE);
            funktionAu\u00dferhalbData.add((Element)Chunk.NEWLINE);
            funktionAu\u00dferhalbData.add((Element)fa);
            String[] funktionAu\u00dferhalbListe = Utils.listToArray(tabLehrgangKategorie.getAlleFunktionenAu\u00dferhalb());
            int[] funktionAu\u00dferhalbStatus = tabLaufbahn.getFunktionAu\u00dferhalbData(mitglied.getId());
            int i3 = 0;
            while (i3 < funktionAu\u00dferhalbListe.length) {
                if (funktionAu\u00dferhalbStatus[i3] == 0) {
                    logging.logInfo((Object)"Lehrgangsstatus == 0 --> Weiter zum n\u00e4chsten...");
                } else {
                    fa = new Chunk(funktionAu\u00dferhalbListe[i3], NORMAL_FONT);
                    funktionAu\u00dferhalbData.add((Element)Chunk.NEWLINE);
                    funktionAu\u00dferhalbData.add((Element)fa);
                }
                ++i3;
            }
            document.add((Element)new Paragraph(funktionAu\u00dferhalbData));
        }
        document.addTitle("FeuerwehrManagementSystem - MitgliedInfo");
        document.addAuthor("FeuerwehrManagementSystem");
        document.addCreationDate();
        document.addSubject("MitgliedInfo");
        document.close();
        writer.close();
    }

    private void printMeasures() {
        logging.logInfo((Object)("A4-Ma\u00dfe: " + PageSize.A4.getWidth() + "pt x " + PageSize.A4.getHeight() + "pt - " + (double)PageSize.A4.getWidth() * 0.3527 + "mm x " + (double)PageSize.A4.getHeight() * 0.3527 + "mm"));
    }

    public static void PDFdocumentErstellen(String dateiname, Mitglieder mitglied, Mitglieder_Untersuchung untersuchung) throws DocumentException, IOException {
        PDFMitgliedInfo ps = new PDFMitgliedInfo();
        try {
            ps.createPdf(dateiname, mitglied, untersuchung);
            ps.printMeasures();
            logging.logInfo((Object)"Datensatz wurde in eine PDF Datei Exportiert");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }
}

