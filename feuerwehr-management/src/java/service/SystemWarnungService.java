package service;

import data.tabellen.TabelleSystemwarnung;
import data.tabellen.fahrzeug.TabelleFahrzeug;
import data.tabellen.mitglied.TabelleMitglied;
import go.SystemWarnung;
import java.sql.SQLException;
import java.util.HashMap;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class SystemWarnungService {

   public static String checkSystem() throws SQLException {
      TabelleMitglied tabMitglieder = new TabelleMitglied();
      TabelleFahrzeug tabFahrzeug = new TabelleFahrzeug();
      TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();
      StringBuilder buildSystemWarnung = new StringBuilder();
      if(tabMitglieder.getAllMitgliederCount() == 0) {
         insertSystemWarnung("Keine Mitglieder verfügbar");
      } else {
         deleteSystemWarnung("Keine Mitglieder verfügbar");
      }

      if(tabFahrzeug.countALL() == 0) {
         insertSystemWarnung("Keine Fahrzeuge verfügbar");
      } else {
         deleteSystemWarnung("Keine Fahrzeuge verfügbar");
      }

      if(((String)runApplication.EINSTELLUNGEN.get("Name")).equals("") | ((String)runApplication.EINSTELLUNGEN.get("Stadt")).equals("") | ((String)runApplication.EINSTELLUNGEN.get("plz")).equals("") | ((String)runApplication.EINSTELLUNGEN.get("strasse")).equals("")) {
         insertSystemWarnung("Adressdaten sind unvollständig");
      } else {
         deleteSystemWarnung("Adressdaten sind unvollständig");
      }

      int coutOfSystemWarnung = tabSystemWarnung.getCount();
      int[] idListe = Utils.listToIntArray(tabSystemWarnung.getIDListe());
      if(coutOfSystemWarnung != 0) {
         for(int i = 0; i < idListe.length; ++i) {
            buildSystemWarnung.append(displayWarnung(idListe[i]));
            buildSystemWarnung.append("\n");
         }
      }

      return buildSystemWarnung.toString();
   }

   public static void insertSystemWarnung(String info) {
      TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();
      SystemWarnung warnung = new SystemWarnung();

      try {
         if(tabSystemWarnung.getCountByInfo(info) == 0) {
            warnung.setId(tabSystemWarnung.getNextNummer());
            warnung.setDatum(SbcUtils.timeStamp("yyyy-MM-dd"));
            warnung.setZeit(SbcUtils.timeStamp("HH:mm"));
            warnung.setInfo(info);
            tabSystemWarnung.insert(warnung);
            logging.logInfo("Systemwarnung \"" + info + "\" wurde in die DB geschrieben");
         } else {
            logging.logInfo("Systemwarnung existiert bereits in der DB");
         }
      } catch (SQLException var4) {
         logging.logPrintStackTrace(var4);
      }

   }

   public static void deleteSystemWarnung(String info) {
      TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();

      try {
         tabSystemWarnung.deleteOne(info);
         logging.logInfo("Systemwarnung wurde behoben...");
      } catch (SQLException var3) {
         logging.logPrintStackTrace(var3);
      }

   }

   public static void deleteAllSystemWarnung() {
      TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();

      try {
         tabSystemWarnung.deleteAll();
         logging.logInfo("Alle Systemwarnungen wurden gelöscht...");
      } catch (SQLException var2) {
         logging.logPrintStackTrace(var2);
      }

   }

   private static String displayWarnung(int id) throws SQLException {
      TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();
      HashMap map = tabSystemWarnung.getData(id);
      String texte = (String)map.get("info") + " (" + TimeCalculation.parseDateForGUI((String)map.get("datum")) + " " + (String)map.get("zeit") + ")";
      return texte;
   }
}
