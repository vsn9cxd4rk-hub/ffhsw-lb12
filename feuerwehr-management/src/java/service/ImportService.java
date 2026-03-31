package service;

import data.tabellen.TabelleEinsatz_uebernahme;
import data.tabellen.karte.TabelleAnfahrt;
import data.tabellen.karte.TabelleHydranten;
import data.tabellen.karte.TabelleObjekte;
import data.tabellen.karte.TabelleStrassen;
import go.karte.Anfahrt;
import go.karte.Hydrant;
import go.karte.Straße;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import logging.logging;
import run.runApplication;
import utilities.Konstante;
import utilities.MyEvent;
import utilities.logbuchEingabe;

public class ImportService {

   public static void importStraßenDaten(String infile) throws IOException, SQLException {
      String zeile = null;
      int counter = 0;
      int counterError = 0;
      int counterInsert = 0;
      byte counterUpdate = 0;
      BufferedReader in = new BufferedReader(new FileReader(infile));
      TabelleStrassen tabStraßen = new TabelleStrassen();
      TabelleHydranten tabHydranten = new TabelleHydranten();
      TabelleObjekte tabObjekte = new TabelleObjekte();
      TabelleEinsatz_uebernahme tabUebernahme = new TabelleEinsatz_uebernahme();
      TabelleAnfahrt tabAnfahrt = new TabelleAnfahrt();
      Straße straße = new Straße();
      Anfahrt anfahrt = new Anfahrt();
      if(tabStraßen.getCount() != 0) {
         int infos = JOptionPane.showConfirmDialog((Component)null, Konstante.STRAßEN_LOESCHEN, "Frage", 0);
         if(infos == 0) {
            logging.logInfo("Benutzer möchte vor dem Import das Straßen-, Objekt- und Hydrantenverzeichnis löschen");
            logbuchEingabe.NeuerEintag("Benutzer möchte vor dem Import das Straßen-, Objekt- und Hydrantenverzeichnis löschen");
            tabStraßen.deleteTable();
            tabHydranten.deleteTable();
            tabObjekte.deleteTable();
            tabUebernahme.updateAlleObjekte();
         }
      }

      while((zeile = in.readLine()) != null) {
         String[] var22 = zeile.split(";");
         logging.logInfo("Eingelesende Zeile: " + zeile);
         if(counter == 0) {
            if(var22.length != 8) {
               logging.logInfo("Die Anzahl der Spalten muss 8 betragen!");
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_IMPORT + "\n\nDetails:\nDie Anzahl der Spalten muss 8 betragen!", "Fehlermeldung", 0);
               return;
            }

            if(!zeile.equals("Straßenname;Postleitzahl;Stadt;Anfahrt;Informationen;Koordinaten;GPS_N;GPS_O")) {
               logging.logInfo("Die Spalten müssen wie folgt beschriftet sein --> Straßenname;Postleitzahl;Stadt;Anfahrt;Informationen;Koordinaten;GPS_N;GPS_O");
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_IMPORT + "\n\nDetails:\nDie Spalten müssen wie folgt beschriftet sein --> Straßenname;Postleitzahl;Stadt;Anfahrt;Informationen;Koordinaten;GPS_N;GPS_O", "Fehlermeldung", 0);
               return;
            }

            ++counter;
         } else if(var22.length == 0) {
            logging.logInfo("Leere Zeile in der CSV Datei gefunden!");
            ++counterError;
         } else {
            int sID = tabStraßen.getNextNummer();
            straße.setId(sID);
            straße.setName(var22[0]);
            straße.setPLZ(var22[1] + " " + var22[2]);

            try {
               anfahrt.setAnfahrt(var22[3]);
               anfahrt.setStrassenID(sID);
               anfahrt.setObjektID(0);
               anfahrt.setId(tabAnfahrt.getNextNummer());
            } catch (ArrayIndexOutOfBoundsException var21) {
               anfahrt.setAnfahrt("");
               anfahrt.setStrassenID(sID);
               anfahrt.setObjektID(0);
               anfahrt.setId(tabAnfahrt.getNextNummer());
            }

            try {
               straße.setInfo(var22[4]);
            } catch (ArrayIndexOutOfBoundsException var20) {
               straße.setInfo("");
            }

            try {
               straße.setKoordinaten(var22[5]);
            } catch (ArrayIndexOutOfBoundsException var19) {
               straße.setKoordinaten("");
            }

            try {
               straße.setGPS_N(var22[6]);
               straße.setGPS_O(var22[7]);
            } catch (ArrayIndexOutOfBoundsException var18) {
               logging.logInfo("Keine GPS Daten gefunden...");
               straße.setGPS_N("");
               straße.setGPS_O("");
            }

            try {
               straße.setBild("");
               straße.setBild2("");
            } catch (ArrayIndexOutOfBoundsException var17) {
               straße.setBild("");
               straße.setBild2("");
            }

            if(tabStraßen.getStrassenCount(straße.getName()).intValue() == 0) {
               tabStraßen.insert(straße);
               if(!anfahrt.getAnfahrt().equals("")) {
                  tabAnfahrt.insert(anfahrt);
               }

               logging.logInfo("Straße hinzugefügt...");
               ++counterInsert;
            } else {
               logging.logInfo("Straße bereits vorhanden!");
            }
         }
      }

      in.close();
      logging.logInfo("Import Straßen abgeschlossen...");
      logging.logInfo("Straßen: Eingefügt: " + counterInsert + ", Aktualisiert: " + counterUpdate + ", Fehler: " + counterError);
      logbuchEingabe.NeuerEintag("Straßenimport erfolgreich...");
      logbuchEingabe.NeuerEintag("Straßen: Eingefügt: " + counterInsert + ", Aktualisiert: " + counterUpdate + ", Fehler: " + counterError);
      MyEvent.setEvent("0x0030");
      JOptionPane.showMessageDialog((Component)null, Konstante.IMPORT_ERFOLGREICH + "\n\nEingefügt: " + counterInsert + "\nAktualisiert: " + counterUpdate + "\nFehler: " + counterError);
   }

   public static void importHydrantenDaten(String infile) throws IOException, SQLException {
      String zeile = null;
      int counter = 0;
      int counterError = 0;
      int counterInsert = 0;
      int counterUpdate = 0;
      BufferedReader in = new BufferedReader(new FileReader(infile));
      TabelleStrassen tabStraßen = new TabelleStrassen();
      TabelleHydranten tabHydranten = new TabelleHydranten();
      Hydrant hydrant = new Hydrant();
      Straße straße = new Straße();
      if(tabHydranten.getCount().intValue() != 0) {
         int infos = JOptionPane.showConfirmDialog((Component)null, Konstante.HYDRANTEN_LOESCHEN, "Frage", 0);
         if(infos == 0) {
            logging.logInfo("Benutzer möchte vor dem Import das Hydrantenverzeichnis löschen");
            logbuchEingabe.NeuerEintag("Benutzer möchte vor dem Import das Hydrantenverzeichnis löschen");
            tabHydranten.deleteTable();
         }
      }

      while((zeile = in.readLine()) != null) {
         String[] var20 = zeile.split(";");
         logging.logInfo("Eingelesende Zeile: " + zeile);
         if(counter == 0) {
            if(var20.length != 8) {
               logging.logInfo("Die Anzahl der Spalten muss 8 betragen!");
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_IMPORT + "\n\nDetails:\nDie Anzahl der Spalten muss 8 betragen!", "Fehlermeldung", 0);
               return;
            }

            if(!zeile.equals("ID;Straße;Beschreibung;Hausnummer;Nenweite;Lage;GPS_N;GPS_O")) {
               logging.logInfo("Die Spalten müssen wie folgt beschriftet sein --> ID;Straße;Beschreibung;Hausnummer;Nenweite;Lage;GPS_N;GPS_O");
               JOptionPane.showMessageDialog((Component)null, Konstante.FEHLER_IMPORT + "\n\nDetails:\nDie Spalten müssen wie folgt beschriftet sein --> ID;Straße;Beschreibung;Hausnummer;Nenweite;Lage;GPS_N;GPS_O", "Fehlermeldung", 0);
               return;
            }

            ++counter;
         } else if(var20.length == 0) {
            logging.logInfo("Leere Zeile in der CSV Datei gefunden!");
            ++counterError;
         } else {
            int sID = tabStraßen.getStrassenID(var20[1]);
            if(sID != 0) {
               hydrant.setStrassenid(sID);
            } else {
               logging.logInfo("Straße ist noch nicht vorhanden!");
               straße.setId(tabStraßen.getNextNummer());
               straße.setName(var20[1]);
               straße.setInfo("");
               straße.setGPS_N("");
               straße.setGPS_O("");
               straße.setKoordinaten("");
               straße.setPLZ((String)runApplication.EINSTELLUNGEN.get("Stadt"));
               straße.setBild("");
               straße.setBild2("");
               tabStraßen.insert(straße);
               sID = straße.getId();
            }

            try {
               hydrant.setId(Integer.parseInt(var20[0]));
            } catch (NumberFormatException var19) {
               logging.logWarning("ID ist keine Nummer...");
               ++counterError;
               continue;
            }

            if(var20.length == 1 | var20.length == 2) {
               logging.logWarning("Ich habe zu dieser Straße keine Hydrantendaten gefunden!");
               ++counterError;
            } else {
               if(var20[2].equals("")) {
                  hydrant.setHausnummer("Haus-Nr: " + var20[3]);
               } else {
                  hydrant.setHausnummer(var20[2]);
               }

               if(var20.length == 3) {
                  logging.logWarning("Ich habe zu dieser Straße und die Beschreibung keine genauen Hydrantendaten gefunden!");
                  ++counterError;
               } else {
                  try {
                     hydrant.setHausnummerID(Integer.parseInt(var20[3]));
                  } catch (NumberFormatException var17) {
                     try {
                        logging.logInfo("Hausnummer endet mit einem Buchstaben!");
                        hydrant.setHausnummerID(Integer.parseInt(var20[3].substring(0, var20[3].length() - 1)));
                     } catch (Exception var16) {
                        hydrant.setHausnummerID(0);
                     }
                  }

                  try {
                     hydrant.setNennweite(Integer.parseInt(var20[4]));
                  } catch (NumberFormatException var18) {
                     logging.logError("Nennweite stimmt nicht --> Abbruch");
                     ++counterError;
                     continue;
                  }

                  try {
                     hydrant.setGPS_N(var20[6]);
                     hydrant.setGPS_O(var20[7]);
                  } catch (ArrayIndexOutOfBoundsException var15) {
                     logging.logWarning("Keine GPS daten vorhanden...");
                     hydrant.setGPS_N("");
                     hydrant.setGPS_O("");
                  }

                  if(tabHydranten.getHydrantCountByID(hydrant).intValue() == 0) {
                     tabHydranten.insert(hydrant);
                     logging.logInfo("Hydrant mit der ID " + var20[0] + " wurde hinzugefügt...");
                     ++counterInsert;
                  } else {
                     tabHydranten.update(hydrant);
                     logging.logInfo("Hydrant mit der ID " + var20[0] + " wurde aktualisiert...");
                     ++counterUpdate;
                  }
               }
            }
         }
      }

      in.close();
      logging.logInfo("Import Hydranten abgeschlossen...");
      logging.logInfo("Hydranten: Eingefügt: " + counterInsert + ", Aktualisiert: " + counterUpdate + ", Fehler: " + counterError);
      logbuchEingabe.NeuerEintag("Hydrantenimport erfolgreich...");
      logbuchEingabe.NeuerEintag("Hydranten: Eingefügt: " + counterInsert + ", Aktualisiert: " + counterUpdate + ", Fehler: " + counterError);
      MyEvent.setEvent("0x0030");
      JOptionPane.showMessageDialog((Component)null, Konstante.IMPORT_ERFOLGREICH + "\n\nEingefügt: " + counterInsert + "\nAktualisiert: " + counterUpdate + "\nFehler: " + counterError);
   }

   public static void ImportData(String infile, String stadt) {
      try {
         BufferedReader e = new BufferedReader(new FileReader(infile));
         String zeile = null;
         int dbcounter = 1;
         int durationcounter = 0;

         for(int durationcounter2 = 10; (zeile = e.readLine()) != null; ++durationcounter) {
            if(durationcounter == durationcounter2) {
               Runtime.getRuntime().exec("cmd /c taskkill /f /im firefox.exe");
               logging.logInfo("Firefox beenden: taskill /f /im firefox.exe");
               Thread.sleep(2000L);
               durationcounter2 += 10;
            }

            TabelleStrassen tabelleStrassen = new TabelleStrassen();
            Straße straße = new Straße();
            straße.setId(tabelleStrassen.getNextNummer());
            straße.setName(zeile);
            straße.setBild(zeile + ".jpg");
            straße.setInfo("");
            straße.setKoordinaten("");
            straße.setPLZ(stadt);
            tabelleStrassen.insert(straße);
            Desktop.getDesktop().browse(new URI("http://maps.google.de/maps?q=" + getStringBuilder(zeile) + ",+" + stadt + "&hl=de&ie=UTF8"));
            Thread.sleep(5000L);
            BufferedImage image = (new Robot()).createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(image, "jpg", new File("c:/windows/temp/screenshot.jpg"));
            BufferedImage img = ImageIO.read(new File("c:/windows/temp/screenshot.jpg"));
            BufferedImage partImg = img.getSubimage(380, 150, 980, 590);
            ImageIO.write(partImg, "jpeg", new File("images/street/groß/" + zeile + ".jpg"));
            Thread.sleep(3000L);
            Robot rob = new Robot();
            rob.mouseMove(426, 317);
            rob.delay(1000);
            rob.mousePress(16);
            rob.mousePress(1024);
            rob.mouseRelease(16);
            Thread.sleep(3000L);
            BufferedImage imageKL = (new Robot()).createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(imageKL, "jpg", new File("c:/windows/temp/screenshot.jpg"));
            BufferedImage imgKL = ImageIO.read(new File("c:/windows/temp/screenshot.jpg"));
            BufferedImage partImgKL = imgKL.getSubimage(380, 150, 980, 590);
            ImageIO.write(partImgKL, "jpeg", new File("images/street/klein/" + zeile + ".jpg"));
            ++dbcounter;
         }

         e.close();
         Runtime.getRuntime().exec("cmd /c taskkill /f /im firefox.exe");
         logging.logInfo("Firefox beenden: taskill /f /im firefox.exe");
         Thread.sleep(2000L);
      } catch (IOException var16) {
         var16.printStackTrace();
      } catch (HeadlessException var17) {
         var17.printStackTrace();
      } catch (AWTException var18) {
         var18.printStackTrace();
      } catch (URISyntaxException var19) {
         var19.printStackTrace();
      } catch (InterruptedException var20) {
         var20.printStackTrace();
      } catch (SQLException var21) {
         var21.printStackTrace();
      }

   }

   public static String getStringBuilder(String in) {
      StringBuilder out = new StringBuilder();
      String[] array = in.split(" ");

      for(int i = 0; i < array.length; ++i) {
         out.append(array[i]);
         out.append("+");
      }

      return out.toString();
   }
}
