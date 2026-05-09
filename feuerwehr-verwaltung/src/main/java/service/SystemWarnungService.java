/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package service;

import data.tabellen.TabelleFahrzeug;
import data.tabellen.TabelleSystemwarnung;
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
        if (tabMitglieder.getAllMitgliederCount() == 0) {
            SystemWarnungService.insertSystemWarnung("Keine Mitglieder verf\u00fcgbar");
        } else {
            SystemWarnungService.deleteSystemWarnung("Keine Mitglieder verf\u00fcgbar");
        }
        if (tabFahrzeug.countALL() == 0) {
            SystemWarnungService.insertSystemWarnung("Keine Fahrzeuge verf\u00fcgbar");
        } else {
            SystemWarnungService.deleteSystemWarnung("Keine Fahrzeuge verf\u00fcgbar");
        }
        if (runApplication.EINSTELLUNGEN.get("Name").equals("") | runApplication.EINSTELLUNGEN.get("Stadt").equals("") | runApplication.EINSTELLUNGEN.get("plz").equals("") | runApplication.EINSTELLUNGEN.get("strasse").equals("")) {
            SystemWarnungService.insertSystemWarnung("Adressdaten sind unvollst\u00e4ndig");
        } else {
            SystemWarnungService.deleteSystemWarnung("Adressdaten sind unvollst\u00e4ndig");
        }
        int coutOfSystemWarnung = tabSystemWarnung.getCount();
        int[] idListe = Utils.listToIntArray(tabSystemWarnung.getIDListe());
        if (coutOfSystemWarnung != 0) {
            int i = 0;
            while (i < idListe.length) {
                buildSystemWarnung.append(SystemWarnungService.displayWarnung(idListe[i]));
                buildSystemWarnung.append("\n");
                ++i;
            }
        }
        return buildSystemWarnung.toString();
    }

    public static void insertSystemWarnung(String info) {
        TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();
        SystemWarnung warnung = new SystemWarnung();
        try {
            if (tabSystemWarnung.getCountByInfo(info) == 0) {
                warnung.setId(tabSystemWarnung.getNextNummer());
                warnung.setDatum(SbcUtils.timeStamp((String)"yyyy-MM-dd"));
                warnung.setZeit(SbcUtils.timeStamp((String)"HH:mm"));
                warnung.setInfo(info);
                tabSystemWarnung.insert(warnung);
                logging.logInfo((Object)("Systemwarnung \"" + info + "\" wurde in die DB geschrieben"));
            } else {
                logging.logInfo((Object)"Systemwarnung existiert bereits in der DB");
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void deleteSystemWarnung(String info) {
        TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();
        try {
            tabSystemWarnung.deleteOne(info);
            logging.logInfo((Object)"Systemwarnung wurde behoben...");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    public static void deleteAllSystemWarnung() {
        TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();
        try {
            tabSystemWarnung.deleteAll();
            logging.logInfo((Object)"Alle Systemwarnungen wurden gel\u00f6scht...");
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
    }

    private static String displayWarnung(int id) throws SQLException {
        TabelleSystemwarnung tabSystemWarnung = new TabelleSystemwarnung();
        HashMap<String, String> map = tabSystemWarnung.getData(id);
        String texte = String.valueOf(map.get("info")) + " (" + TimeCalculation.parseDateForGUI(map.get("datum")) + " " + map.get("zeit") + ")";
        return texte;
    }
}

