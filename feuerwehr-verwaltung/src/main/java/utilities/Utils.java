/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  net.fortuna.ical4j.data.CalendarBuilder
 *  net.fortuna.ical4j.data.CalendarOutputter
 *  net.fortuna.ical4j.data.ParserException
 *  net.fortuna.ical4j.model.Calendar
 *  net.fortuna.ical4j.model.Component
 *  net.fortuna.ical4j.model.Date
 *  net.fortuna.ical4j.model.DateTime
 *  net.fortuna.ical4j.model.Property
 *  net.fortuna.ical4j.model.TimeZone
 *  net.fortuna.ical4j.model.TimeZoneRegistry
 *  net.fortuna.ical4j.model.TimeZoneRegistryFactory
 *  net.fortuna.ical4j.model.ValidationException
 *  net.fortuna.ical4j.model.component.VEvent
 *  net.fortuna.ical4j.model.component.VTimeZone
 *  net.fortuna.ical4j.model.property.CalScale
 *  net.fortuna.ical4j.model.property.Description
 *  net.fortuna.ical4j.model.property.ProdId
 *  net.fortuna.ical4j.model.property.Version
 *  utilities.Datei
 *  utilities.MyProperties
 *  utilities.RandomGenerator
 *  utilities.RandomGenerator$Mode
 */
package utilities;

import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleFTPSync;
import go.FTPSync;
import go.FTPSyncDelete;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import logging.logging;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import run.runApplication;
import utilities.Datei;
import utilities.Konstante;
import utilities.MyProperties;
import utilities.RandomGenerator;

public class Utils {
    private static PrintRequestAttributeSet printAtribut;

    public static String[] listToArray(List<String> liste) {
        String[] result = new String[liste.size()];
        int i = 0;
        while (i < liste.size()) {
            result[i] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static int[] listToIntArray(List<Integer> liste) {
        int[] result = new int[liste.size()];
        int i = 0;
        while (i < liste.size()) {
            result[i] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static long[] listToLongArray(List<Long> liste) {
        long[] result = new long[liste.size()];
        int i = 0;
        while (i < liste.size()) {
            result[i] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static Boolean[] listToBooleanArray(List<Boolean> liste) {
        Boolean[] result = new Boolean[liste.size()];
        int i = 0;
        while (i < liste.size()) {
            result[i] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static String[] listToArrayOnlyFORComboBoxes(List<String> liste) {
        String[] result = new String[liste.size() + 1];
        result[0] = "<bitte w\u00e4hlen>";
        int i = 0;
        while (i < liste.size()) {
            result[i + 1] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static String[] listToArrayMitOptionAlle(List<String> liste) {
        String[] result = new String[liste.size() + 1];
        result[0] = "<alle>";
        int i = 0;
        while (i < liste.size()) {
            result[i + 1] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static int[] listToIntArrayOnlyFORComboBoxes(List<Integer> liste) {
        int[] result = new int[liste.size() + 1];
        result[0] = 0;
        int i = 0;
        while (i < liste.size()) {
            result[i + 1] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static String[] listToArrayWithEmptyLine(List<String> liste) {
        String[] result = new String[liste.size() + 1];
        result[0] = "";
        int i = 0;
        while (i < liste.size()) {
            result[i + 1] = liste.get(i);
            ++i;
        }
        return result;
    }

    public static String listToString(List<String> liste) {
        StringBuilder build = new StringBuilder();
        int i = 0;
        while (i < liste.size()) {
            build.append(liste.get(i));
            build.append("\n");
            ++i;
        }
        return build.toString();
    }

    public static boolean checkText(String text, String notInTheText) {
        int i = 0;
        while (i < text.length()) {
            if (text.substring(i, i + 1).equals(notInTheText)) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public static String checkTextAndRemoveIllegalSigns(String text) {
        StringBuilder build = new StringBuilder();
        logging.logInfo((Object)("Checkt Input String: " + text));
        try {
            int i = 0;
            while (i < text.length()) {
                if (text.substring(i, i + 1).equals("\u00e4")) {
                    build.append("ae");
                } else if (text.substring(i, i + 1).equals("\u00f6")) {
                    build.append("oe");
                } else if (text.substring(i, i + 1).equals("\u00fc")) {
                    build.append("ue");
                } else if (text.substring(i, i + 1).equals("\u00c4")) {
                    build.append("Ae");
                } else if (text.substring(i, i + 1).equals("\u00d6")) {
                    build.append("Oe");
                } else if (text.substring(i, i + 1).equals("\u00dc")) {
                    build.append("Ue");
                } else if (text.substring(i, i + 1).equals("\u00df")) {
                    build.append("ss");
                } else if (text.substring(i, i + 1).equals("&")) {
                    build.append("u.");
                } else {
                    build.append(text.substring(i, i + 1));
                }
                ++i;
            }
            return build.toString();
        }
        catch (NullPointerException e) {
            logging.logInfo((Object)"Habe Null empfangen");
            return " ";
        }
    }

    public static String removeBackSlashFromString(String text) {
        StringBuilder build = new StringBuilder();
        logging.logInfo((Object)("Checkt Input String: " + text));
        int i = 0;
        while (i < text.length()) {
            if (text.substring(i, i + 1).equals("\\")) {
                build.append("/");
            } else {
                build.append(text.substring(i, i + 1));
            }
            ++i;
        }
        return build.toString();
    }

    public static String getTeilnehmerEinerVeranstaltung(int veranstaltungID) {
        TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();
        try {
            String[] mitgliederListe = Utils.listToArray(tabAnwesenheit.getAnwesendeMitgliederByVeranstaltung(veranstaltungID));
            StringBuilder result = new StringBuilder();
            int i = 0;
            while (i < mitgliederListe.length) {
                result.append(mitgliederListe[i]);
                result.append("; ");
                ++i;
            }
            return result.toString();
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
            return null;
        }
    }

    public static void checkProperties(MyProperties programmProperties) {
        logging.logInfo((Object)"Starte CheckProperties...");
        programmProperties.checkPropertiesEntry("logmax", (Object)"100000");
        programmProperties.checkPropertiesEntry("DatenbankPort", (Object)"3306");
        programmProperties.checkPropertiesEntry("DB_TYP", (Object)"Lokal");
        programmProperties.checkPropertiesEntry("Organisation", (Object)"Feuerwehr");
        programmProperties.checkPropertiesEntry("BlobActiv", (Object)"false");
        programmProperties.checkPropertiesEntry("ClientID", (Object)RandomGenerator.generate((int)20, (RandomGenerator.Mode)RandomGenerator.Mode.ALPHANUMERIC));
    }

    public static void ordnerErstellen(String ordnerName, String clientID) throws SQLException {
        File ordner = new File(ordnerName);
        if (!ordner.exists()) {
            ordner.mkdir();
            TabelleFTPSync tabFTP = new TabelleFTPSync();
            FTPSync sync = new FTPSync();
            sync.setClientID(clientID);
            sync.setId(tabFTP.getNextNummer());
            sync.setDatei("");
            sync.setOrdner(ordnerName);
            sync.setStaus(0);
            sync.setStausDB(1);
            sync.setGroe\u00dfe(0L);
            tabFTP.insert(sync);
        }
    }

    public static void dateiKatalogisieren(String dateiName) throws SQLException {
        TabelleFTPSync tabFTP = new TabelleFTPSync();
        FTPSync sync = new FTPSync();
        File datei = new File(dateiName);
        if (runApplication.clientID == null) {
            sync.setClientID("SYSTEM");
        } else {
            sync.setClientID(runApplication.clientID);
        }
        if (Utils.ausnahmeDateien(datei)) {
            return;
        }
        sync.setId(tabFTP.getNextNummer());
        sync.setDatei(dateiName);
        sync.setOrdner("");
        sync.setStaus(0);
        sync.setStausDB(0);
        sync.setGroe\u00dfe(datei.length());
        if (tabFTP.getCountOfFile(dateiName) == 0) {
            tabFTP.insert(sync);
        } else {
            tabFTP.update(sync);
        }
    }

    public static void dateiKatalogisierenForDelete(String dateiName) throws SQLException {
        TabelleFTPSync tabFTP = new TabelleFTPSync();
        FTPSyncDelete sync = new FTPSyncDelete();
        if (runApplication.clientID == null) {
            sync.setClientID("SYSTEM");
        } else {
            sync.setClientID(runApplication.clientID);
        }
        sync.setId(tabFTP.getFileID(dateiName));
        sync.setDatei(Utils.removeBackSlashFromString(dateiName));
        sync.setStatus(0);
        sync.setStatusDB(0);
        tabFTP.insertFTPDelete(sync);
    }

    public static boolean ausnahmeDateien(File file) {
        if (file.getName().equals("desktop.ini")) {
            return true;
        }
        if (file.getName().equals(".lock")) {
            return true;
        }
        return file.getName().equals("Thumbs.db");
    }

    public static void rekatalogisiereDateien(String ordnerName) {
        TabelleFTPSync tabFTP = new TabelleFTPSync();
        FTPSync sync = new FTPSync();
        File aktuellerOrdner = new File(ordnerName);
        File[] datei = aktuellerOrdner.listFiles();
        try {
            int d = 0;
            while (d < datei.length) {
                if (datei[d].isFile()) {
                    Utils.dateiKatalogisieren(Utils.removeBackSlashFromString(datei[d].toString()));
                } else {
                    sync.setClientID("SYSTEM");
                    sync.setId(tabFTP.getNextNummer());
                    sync.setDatei("");
                    sync.setOrdner(Utils.removeBackSlashFromString(datei[d].toString()));
                    sync.setStaus(0);
                    sync.setStausDB(0);
                    sync.setGroe\u00dfe(datei[d].length());
                    tabFTP.insert(sync);
                    Utils.rekatalogisiereDateien(Utils.removeBackSlashFromString(datei[d].toString()));
                }
                ++d;
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void kopiereDateiInDataOrdner(File in, String out, String outFolder) {
        try {
            if (!new File(outFolder).exists()) {
                new File(outFolder).mkdir();
            }
            Datei.copyFileAusf\u00fchren((File)in, (String)out);
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void ExportJTabletoCSV(JTable table, File ordner, String dateiname) {
        try {
            TableModel tableModel = table.getModel();
            FileWriter exportFile = new FileWriter(ordner + "/" + dateiname + "_" + "FMS" + "_CSV_EXPORT.csv");
            int i = 0;
            while (i < tableModel.getColumnCount()) {
                exportFile.write(String.valueOf(tableModel.getColumnName(i)) + runApplication.EINSTELLUNGEN.get("vCardSeperator"));
                ++i;
            }
            exportFile.write("\n");
            i = 0;
            while (i < tableModel.getRowCount()) {
                int j = 0;
                while (j < tableModel.getColumnCount()) {
                    try {
                        exportFile.write(String.valueOf(tableModel.getValueAt(i, j).toString()) + runApplication.EINSTELLUNGEN.get("vCardSeperator"));
                    }
                    catch (NullPointerException e) {
                        exportFile.write(" " + runApplication.EINSTELLUNGEN.get("vCardSeperator"));
                    }
                    ++j;
                }
                exportFile.write("\n");
                ++i;
            }
            exportFile.close();
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void ExportICS(String exportOrdner, String datumVon, String datumBis, int kategorie) {
        try {
            String calFile = String.valueOf(exportOrdner) + "/" + "FeuerwehrManagementSystem" + "_ics_EXPORT.ics";
            Calendar calendar = new Calendar();
            calendar.getProperties().add((Property)new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
            calendar.getProperties().add((Property)Version.VERSION_2_0);
            calendar.getProperties().add((Property)CalScale.GREGORIAN);
            TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
            TabelleAusbildung_plan tabAusbildung_plan = new TabelleAusbildung_plan();
            String[] veranstaltungListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraums(datumVon, datumBis, kategorie));
            int[] veranstaltungListeIds = Utils.listToIntArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsIDs(datumVon, datumBis, kategorie));
            String[] veranstaltungDatumListe = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsDatum(datumVon, datumBis, kategorie));
            String[] veranstaltungStart = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsZeit(datumVon, datumBis, kategorie));
            String[] veranstaltungEnde = Utils.listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsZeitEnde(datumVon, datumBis, kategorie));
            int v = 0;
            while (v < veranstaltungListe.length) {
                String eventBeschreibung;
                logging.logInfo((Object)("F\u00fcge Veranstaltung zum Kalender hinzu... " + veranstaltungListe[v]));
                TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
                TimeZone timezone = registry.getTimeZone("Europe/Berlin");
                VTimeZone tz = timezone.getVTimeZone();
                String eventName = veranstaltungListe[v];
                if (tabAusbildung_plan.getCountVeranstaltungID(veranstaltungListeIds[v]) == 1) {
                    HashMap<String, String> ausbildung = tabAusbildung_plan.getDataByVeranstaltungId(veranstaltungListeIds[v]);
                    eventBeschreibung = String.valueOf(ausbildung.get("ausbildungKategorie")) + " / " + ausbildung.get("details") + "  --> Ausbilder: " + ausbildung.get("ausbilder1");
                } else {
                    eventBeschreibung = String.valueOf(veranstaltungListe[v]) + " (" + veranstaltungDatumListe[v] + ", " + veranstaltungStart + ")";
                }
                GregorianCalendar startDate = new GregorianCalendar();
                startDate.set(Integer.parseInt(veranstaltungDatumListe[v].substring(0, 4)), Integer.parseInt(veranstaltungDatumListe[v].substring(5, 7)) - 1, Integer.parseInt(veranstaltungDatumListe[v].substring(8, 10)), Integer.parseInt(veranstaltungStart[v].substring(0, 2)), Integer.parseInt(veranstaltungStart[v].substring(3, 5)), 0);
                GregorianCalendar endDate = new GregorianCalendar();
                if (veranstaltungEnde[v].equals("")) {
                    endDate.set(Integer.parseInt(veranstaltungDatumListe[v].substring(0, 4)), Integer.parseInt(veranstaltungDatumListe[v].substring(5, 7)) - 1, Integer.parseInt(veranstaltungDatumListe[v].substring(8, 10)), Integer.parseInt(veranstaltungStart[v].substring(0, 2)) + 3, 0, 0);
                } else {
                    endDate.set(Integer.parseInt(veranstaltungDatumListe[v].substring(0, 4)), Integer.parseInt(veranstaltungDatumListe[v].substring(5, 7)) - 1, Integer.parseInt(veranstaltungDatumListe[v].substring(8, 10)), Integer.parseInt(veranstaltungEnde[v].substring(0, 2)), Integer.parseInt(veranstaltungEnde[v].substring(3, 5)), 0);
                }
                DateTime start = new DateTime(startDate.getTime());
                DateTime end = new DateTime(endDate.getTime());
                VEvent apointment = new VEvent((Date)start, (Date)end, eventName);
                apointment.getProperties().add((Property)new Description(eventBeschreibung));
                apointment.getProperties().add((Property)tz.getTimeZoneId());
                calendar.getComponents().add((Component)apointment);
                ++v;
            }
            FileOutputStream fout = new FileOutputStream(calFile);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.setValidating(false);
            outputter.output(calendar, (OutputStream)fout);
            FileInputStream fin = new FileInputStream(calFile);
            CalendarBuilder builder = new CalendarBuilder();
            calendar = builder.build((InputStream)fin);
            logging.logInfo((Object)("Schreibe Kalender Datei in: " + calFile));
            for (Object _comp : calendar.getComponents()) {
                Component component = (Component)_comp;
                System.out.println("Component [" + component.getName() + "]");
                for (Object _prop : component.getProperties()) {
                    Property property = (Property)_prop;
                    System.out.println("Property [" + property.getName() + ", " + property.getValue() + "]");
                }
            }
            fin.close();
        }
        catch (IOException | SQLException | ParserException | ValidationException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void printJTable(String headerText, JTable table, OrientationRequested seitenverhaeltnis, boolean zeilenh\u00f6heEditierbar, boolean showDruckDialog) {
        try {
            MessageFormat header = null;
            MessageFormat footer = null;
            if (runApplication.EINSTELLUNGEN.get("headerPrint").equals("1")) {
                header = new MessageFormat(headerText);
            }
            if (runApplication.EINSTELLUNGEN.get("footerPrint").equals("1")) {
                footer = new MessageFormat(String.valueOf(headerText) + " - Seite {0,number,#.#}");
            }
            if (zeilenh\u00f6heEditierbar) {
                table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heDruck")));
            }
            printAtribut = new HashPrintRequestAttributeSet();
            float leftMargin = 10.0f;
            float rightMargin = 10.0f;
            float topMargin = 15.0f;
            float bottomMargin = 15.0f;
            printAtribut.add(seitenverhaeltnis);
            printAtribut.add(MediaSizeName.ISO_A4);
            MediaSize mediaSize = MediaSize.ISO.A4;
            float mediaWidth = mediaSize.getX(1000);
            float mediaHeight = mediaSize.getY(1000);
            printAtribut.add(new MediaPrintableArea(leftMargin, topMargin, mediaWidth - leftMargin - rightMargin, mediaHeight - topMargin - bottomMargin, 1000));
            boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, header, footer, showDruckDialog, printAtribut, true);
            if (complete) {
                if (showDruckDialog) {
                    JOptionPane.showMessageDialog(null, Konstante.DRUCK_ERFOLGREICH);
                }
                if (zeilenh\u00f6heEditierbar) {
                    table.setRowHeight(Integer.parseInt(runApplication.EINSTELLUNGEN.get("zeilenh\u00f6heAnsicht")));
                }
            }
        }
        catch (PrinterException pe) {
            JOptionPane.showMessageDialog(null, "Der Druck ist Fehlgeschlagen", "Fehlermeldung", 0);
            logging.logPrintStackTrace((Exception)pe);
        }
    }
}

