package utilities;

import data.tabellen.TabelleAnwesenheit;
import data.tabellen.TabelleAusbildung_plan;
import data.tabellen.TabelleVeranstaltung;
import data.tabellen.einstellungen.TabelleFTPSync;
import go.FTPSync;
import go.FTPSyncDelete;
import java.awt.Component;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.MediaSize.ISO;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.table.TableModel;
import logging.logging;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
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
import utilities.RandomGenerator.Mode;

public class Utils {

   private static PrintRequestAttributeSet printAtribut;


   public static String[] listToArray(List liste) {
      String[] result = new String[liste.size()];

      for(int i = 0; i < liste.size(); ++i) {
         result[i] = (String)liste.get(i);
      }

      return result;
   }

   public static int[] listToIntArray(List liste) {
      int[] result = new int[liste.size()];

      for(int i = 0; i < liste.size(); ++i) {
         result[i] = ((Integer)liste.get(i)).intValue();
      }

      return result;
   }

   public static long[] listToLongArray(List liste) {
      long[] result = new long[liste.size()];

      for(int i = 0; i < liste.size(); ++i) {
         result[i] = ((Long)liste.get(i)).longValue();
      }

      return result;
   }

   public static Boolean[] listToBooleanArray(List liste) {
      Boolean[] result = new Boolean[liste.size()];

      for(int i = 0; i < liste.size(); ++i) {
         result[i] = (Boolean)liste.get(i);
      }

      return result;
   }

   public static String[] listToArrayOnlyFORComboBoxes(List liste) {
      String[] result = new String[liste.size() + 1];
      result[0] = "<bitte wählen>";

      for(int i = 0; i < liste.size(); ++i) {
         result[i + 1] = (String)liste.get(i);
      }

      return result;
   }

   public static String[] listToArrayMitOptionAlle(List liste) {
      String[] result = new String[liste.size() + 1];
      result[0] = "<alle>";

      for(int i = 0; i < liste.size(); ++i) {
         result[i + 1] = (String)liste.get(i);
      }

      return result;
   }

   public static int[] listToIntArrayOnlyFORComboBoxes(List liste) {
      int[] result = new int[liste.size() + 1];
      result[0] = 0;

      for(int i = 0; i < liste.size(); ++i) {
         result[i + 1] = ((Integer)liste.get(i)).intValue();
      }

      return result;
   }

   public static String[] listToArrayWithEmptyLine(List liste) {
      String[] result = new String[liste.size() + 1];
      result[0] = "";

      for(int i = 0; i < liste.size(); ++i) {
         result[i + 1] = (String)liste.get(i);
      }

      return result;
   }

   public static String listToString(List liste) {
      StringBuilder build = new StringBuilder();

      for(int i = 0; i < liste.size(); ++i) {
         build.append((String)liste.get(i));
         build.append("\n");
      }

      return build.toString();
   }

   public static boolean checkText(String text, String notInTheText) {
      for(int i = 0; i < text.length(); ++i) {
         if(text.substring(i, i + 1).equals(notInTheText)) {
            return false;
         }
      }

      return true;
   }

   public static String checkTextAndRemoveIllegalSigns(String text) {
      StringBuilder build = new StringBuilder();
      logging.logInfo("Checkt Input String: " + text);

      try {
         for(int e = 0; e < text.length(); ++e) {
            if(text.substring(e, e + 1).equals("ä")) {
               build.append("ae");
            } else if(text.substring(e, e + 1).equals("ö")) {
               build.append("oe");
            } else if(text.substring(e, e + 1).equals("ü")) {
               build.append("ue");
            } else if(text.substring(e, e + 1).equals("Ä")) {
               build.append("Ae");
            } else if(text.substring(e, e + 1).equals("Ö")) {
               build.append("Oe");
            } else if(text.substring(e, e + 1).equals("Ü")) {
               build.append("Ue");
            } else if(text.substring(e, e + 1).equals("ß")) {
               build.append("ss");
            } else if(text.substring(e, e + 1).equals("&")) {
               build.append("u.");
            } else if(text.substring(e, e + 1).equals("/")) {
               build.append("-");
            } else {
               build.append(text.substring(e, e + 1));
            }
         }

         return build.toString();
      } catch (NullPointerException var3) {
         logging.logInfo("Habe Null empfangen");
         return " ";
      }
   }

   public static String checkTextAndRemoveIllegalSigns2(String text) {
      StringBuilder build = new StringBuilder();
      logging.logInfo("Checkt Input String: " + text);

      try {
         for(int e = 0; e < text.length(); ++e) {
            if(text.substring(e, e + 1).equals("&")) {
               build.append("u.");
            } else if(text.substring(e, e + 1).equals("/")) {
               build.append("-");
            } else {
               build.append(text.substring(e, e + 1));
            }
         }

         return build.toString();
      } catch (NullPointerException var3) {
         logging.logInfo("Habe Null empfangen");
         return " ";
      }
   }

   public static String removeBackSlashFromString(String text) {
      StringBuilder build = new StringBuilder();
      logging.logInfo("Checkt Input String: " + text);

      for(int i = 0; i < text.length(); ++i) {
         if(text.substring(i, i + 1).equals("\\")) {
            build.append("/");
         } else {
            build.append(text.substring(i, i + 1));
         }
      }

      return build.toString();
   }

   public static String getTeilnehmerEinerVeranstaltung(int veranstaltungID) {
      TabelleAnwesenheit tabAnwesenheit = new TabelleAnwesenheit();

      try {
         String[] e = listToArray(tabAnwesenheit.getAnwesendeMitgliederByVeranstaltung(veranstaltungID));
         StringBuilder result = new StringBuilder();

         for(int i = 0; i < e.length; ++i) {
            result.append(e[i]);
            result.append("; ");
         }

         return result.toString();
      } catch (SQLException var5) {
         logging.logPrintStackTrace(var5);
         return null;
      }
   }

   public static void checkProperties(MyProperties programmProperties) {
      logging.logInfo("Starte CheckProperties...");
      programmProperties.checkPropertiesEntry("logmax", "100000");
      programmProperties.checkPropertiesEntry("DatenbankPort", "3306");
      programmProperties.checkPropertiesEntry("DB_TYP", "Lokal");
      programmProperties.checkPropertiesEntry("Organisation", "Feuerwehr");
      programmProperties.checkPropertiesEntry("BlobActiv", "false");
      programmProperties.checkPropertiesEntry("DateiDownload247", "false");
      programmProperties.checkPropertiesEntry("FTPServerPfad", "");
      programmProperties.checkPropertiesEntry("ClientID", RandomGenerator.generate(20, Mode.ALPHANUMERIC));
   }

   public static void ordnerErstellen(String ordnerName, String clientID) throws SQLException {
      File ordner = new File(ordnerName);
      if(!ordner.exists()) {
         ordner.mkdir();
         TabelleFTPSync tabFTP = new TabelleFTPSync();
         FTPSync sync = new FTPSync();
         sync.setClientID(clientID);
         sync.setId(tabFTP.getNextNummer());
         sync.setDatei("");
         sync.setOrdner(ordnerName);
         sync.setStaus(0);
         sync.setStausDB(1);
         sync.setGroeße(0L);
         tabFTP.insert(sync);
      }

   }

   public static void dateiKatalogisieren(String dateiName) throws SQLException {
      TabelleFTPSync tabFTP = new TabelleFTPSync();
      FTPSync sync = new FTPSync();
      File datei = new File(dateiName);
      if(runApplication.clientID == null) {
         sync.setClientID("SYSTEM");
      } else {
         sync.setClientID(runApplication.clientID);
      }

      if(!ausnahmeDateien(datei)) {
         sync.setId(tabFTP.getNextNummer());
         sync.setDatei(dateiName);
         sync.setOrdner("");
         sync.setStaus(0);
         sync.setStausDB(0);
         sync.setGroeße(datei.length());
         if(tabFTP.getCountOfFile(dateiName) == 0) {
            tabFTP.insert(sync);
         } else {
            tabFTP.update(sync);
         }

      }
   }

   public static void dateiKatalogisierenForDelete(String dateiName) throws SQLException {
      TabelleFTPSync tabFTP = new TabelleFTPSync();
      FTPSyncDelete sync = new FTPSyncDelete();
      if(runApplication.clientID == null) {
         sync.setClientID("SYSTEM");
      } else {
         sync.setClientID(runApplication.clientID);
      }

      sync.setId(tabFTP.getFileID(dateiName));
      sync.setDatei(removeBackSlashFromString(dateiName));
      sync.setStatus(0);
      sync.setStatusDB(0);
      tabFTP.insertFTPDelete(sync);
   }

   public static boolean ausnahmeDateien(File file) {
      return file.getName().equals("desktop.ini")?true:(file.getName().equals(".lock")?true:file.getName().equals("Thumbs.db"));
   }

   public static void rekatalogisiereDateien(String ordnerName) {
      TabelleFTPSync tabFTP = new TabelleFTPSync();
      FTPSync sync = new FTPSync();
      File aktuellerOrdner = new File(ordnerName);
      File[] datei = aktuellerOrdner.listFiles();

      try {
         for(int e = 0; e < datei.length; ++e) {
            if(datei[e].isFile()) {
               dateiKatalogisieren(removeBackSlashFromString(datei[e].toString()));
            } else {
               sync.setClientID("SYSTEM");
               sync.setId(tabFTP.getNextNummer());
               sync.setDatei("");
               sync.setOrdner(removeBackSlashFromString(datei[e].toString()));
               sync.setStaus(0);
               sync.setStausDB(0);
               sync.setGroeße(datei[e].length());
               tabFTP.insert(sync);
               rekatalogisiereDateien(removeBackSlashFromString(datei[e].toString()));
            }
         }
      } catch (SQLException var6) {
         logging.logPrintStackTrace(var6);
      }

   }

   public static void kopiereDateiInDataOrdner(File in, String out, String outFolder) {
      try {
         if(!(new File(outFolder)).exists()) {
            (new File(outFolder)).mkdir();
         }

         Datei.copyFileAusführen(in, out);
      } catch (IOException var4) {
         JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
         logging.logPrintStackTrace(var4);
      }

   }

   public static void ExportJTabletoCSV(JTable table, File ordner, String dateiname) {
      try {
         TableModel e = table.getModel();
         FileWriter exportFile = new FileWriter(ordner + "/" + dateiname + "_" + "FMS" + "_CSV_EXPORT.csv");

         int i;
         for(i = 0; i < e.getColumnCount(); ++i) {
            exportFile.write(e.getColumnName(i) + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator"));
         }

         exportFile.write("\n");

         for(i = 0; i < e.getRowCount(); ++i) {
            for(int j = 0; j < e.getColumnCount(); ++j) {
               try {
                  exportFile.write(e.getValueAt(i, j).toString() + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator"));
               } catch (NullPointerException var8) {
                  exportFile.write(" " + (String)runApplication.EINSTELLUNGEN.get("vCardSeperator"));
               }
            }

            exportFile.write("\n");
         }

         exportFile.close();
      } catch (IOException var9) {
         JOptionPane.showMessageDialog((Component)null, Konstante.SPEICHERN_FEHLER, "Fehlermeldung", 0);
         logging.logPrintStackTrace(var9);
      }

   }

   public static void ExportICS(String exportOrdner, String datumVon, String datumBis, int kategorie) {
      try {
         String e = exportOrdner + "/" + "FeuerwehrManagementSystem" + "_ics_EXPORT.ics";
         Calendar calendar = new Calendar();
         calendar.getProperties().add(new ProdId("-//Ben Fortuna//iCal4j 1.0//EN"));
         calendar.getProperties().add(Version.VERSION_2_0);
         calendar.getProperties().add(CalScale.GREGORIAN);
         TabelleVeranstaltung tabVeranstaltung = new TabelleVeranstaltung();
         TabelleAusbildung_plan tabAusbildung_plan = new TabelleAusbildung_plan();
         String[] veranstaltungListe = listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraums(datumVon, datumBis, kategorie));
         int[] veranstaltungListeIds = listToIntArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsIDs(datumVon, datumBis, kategorie));
         String[] veranstaltungDatumListe = listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsDatum(datumVon, datumBis, kategorie));
         String[] veranstaltungStart = listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsZeit(datumVon, datumBis, kategorie));
         String[] veranstaltungEnde = listToArray(tabVeranstaltung.getAllVeranstaltungEinesZeitraumsZeitEnde(datumVon, datumBis, kategorie));

         for(int fout = 0; fout < veranstaltungListe.length; ++fout) {
            logging.logInfo("Füge Veranstaltung zum Kalender hinzu... " + veranstaltungListe[fout]);
            TimeZoneRegistry outputter = TimeZoneRegistryFactory.getInstance().createRegistry();
            TimeZone fin = outputter.getTimeZone("Europe/Berlin");
            VTimeZone builder = fin.getVTimeZone();
            String i = veranstaltungListe[fout];
            String component;
            if(tabAusbildung_plan.getCountVeranstaltungID(veranstaltungListeIds[fout]) == 1) {
               HashMap j = tabAusbildung_plan.getDataByVeranstaltungId(veranstaltungListeIds[fout]);
               component = (String)j.get("ausbildungKategorie") + " / " + (String)j.get("details") + "  --> Ausbilder: " + (String)j.get("ausbilder1");
            } else {
               component = veranstaltungListe[fout] + " (" + veranstaltungDatumListe[fout] + ", " + veranstaltungStart + ")";
            }

            GregorianCalendar var31 = new GregorianCalendar();
            var31.set(Integer.parseInt(veranstaltungDatumListe[fout].substring(0, 4)), Integer.parseInt(veranstaltungDatumListe[fout].substring(5, 7)) - 1, Integer.parseInt(veranstaltungDatumListe[fout].substring(8, 10)), Integer.parseInt(veranstaltungStart[fout].substring(0, 2)), Integer.parseInt(veranstaltungStart[fout].substring(3, 5)), 0);
            GregorianCalendar property = new GregorianCalendar();
            if(veranstaltungEnde[fout].equals("")) {
               property.set(Integer.parseInt(veranstaltungDatumListe[fout].substring(0, 4)), Integer.parseInt(veranstaltungDatumListe[fout].substring(5, 7)) - 1, Integer.parseInt(veranstaltungDatumListe[fout].substring(8, 10)), Integer.parseInt(veranstaltungStart[fout].substring(0, 2)) + 3, 0, 0);
            } else {
               property.set(Integer.parseInt(veranstaltungDatumListe[fout].substring(0, 4)), Integer.parseInt(veranstaltungDatumListe[fout].substring(5, 7)) - 1, Integer.parseInt(veranstaltungDatumListe[fout].substring(8, 10)), Integer.parseInt(veranstaltungEnde[fout].substring(0, 2)), Integer.parseInt(veranstaltungEnde[fout].substring(3, 5)), 0);
            }

            DateTime start = new DateTime(var31.getTime());
            DateTime end = new DateTime(property.getTime());
            VEvent apointment = new VEvent(start, end, i);
            apointment.getProperties().add(new Description(component));
            apointment.getProperties().add(builder.getTimeZoneId());
            calendar.getComponents().add(apointment);
         }

         FileOutputStream var25 = new FileOutputStream(e);
         CalendarOutputter var26 = new CalendarOutputter();
         var26.setValidating(false);
         var26.output(calendar, var25);
         FileInputStream var27 = new FileInputStream(e);
         CalendarBuilder var28 = new CalendarBuilder();
         calendar = var28.build(var27);
         logging.logInfo("Schreibe Kalender Datei in: " + e);
         Iterator var29 = calendar.getComponents().iterator();

         while(var29.hasNext()) {
            net.fortuna.ical4j.model.Component var30 = (net.fortuna.ical4j.model.Component)var29.next();
            System.out.println("Component [" + var30.getName() + "]");
            Iterator var32 = var30.getProperties().iterator();

            while(var32.hasNext()) {
               Property var33 = (Property)var32.next();
               System.out.println("Property [" + var33.getName() + ", " + var33.getValue() + "]");
            }
         }

         var27.close();
      } catch (IOException var24) {
         logging.logPrintStackTrace(var24);
      }

   }

   public static void printJTable(String headerText, JTable table, OrientationRequested seitenverhaeltnis, boolean zeilenhöheEditierbar, boolean showDruckDialog) {
      try {
         MessageFormat pe = null;
         MessageFormat footer = null;
         if(((String)runApplication.EINSTELLUNGEN.get("headerPrint")).equals("1")) {
            pe = new MessageFormat(headerText);
         }

         if(((String)runApplication.EINSTELLUNGEN.get("footerPrint")).equals("1")) {
            footer = new MessageFormat(headerText + " - Seite {0,number,#.#}");
         }

         if(zeilenhöheEditierbar) {
            table.setRowHeight(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("zeilenhöheDruck")));
         }

         printAtribut = new HashPrintRequestAttributeSet();
         float leftMargin = 10.0F;
         float rightMargin = 10.0F;
         float topMargin = 15.0F;
         float bottomMargin = 15.0F;
         printAtribut.add(seitenverhaeltnis);
         printAtribut.add(MediaSizeName.ISO_A4);
         MediaSize mediaSize = ISO.A4;
         float mediaWidth = mediaSize.getX(1000);
         float mediaHeight = mediaSize.getY(1000);
         printAtribut.add(new MediaPrintableArea(leftMargin, topMargin, mediaWidth - leftMargin - rightMargin, mediaHeight - topMargin - bottomMargin, 1000));
         boolean complete = table.print(PrintMode.FIT_WIDTH, pe, footer, showDruckDialog, printAtribut, true);
         if(complete) {
            if(showDruckDialog) {
               JOptionPane.showMessageDialog((Component)null, Konstante.DRUCK_ERFOLGREICH);
            }

            if(zeilenhöheEditierbar) {
               table.setRowHeight(Integer.parseInt((String)runApplication.EINSTELLUNGEN.get("zeilenhöheAnsicht")));
            }
         }
      } catch (PrinterException var15) {
         JOptionPane.showMessageDialog((Component)null, "Der Druck ist Fehlgeschlagen", "Fehlermeldung", 0);
         logging.logPrintStackTrace(var15);
      }

   }
}
