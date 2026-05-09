/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package service;

import data.tabellen.karte.TabelleHydranten;
import go.Hydrant;
import java.sql.SQLException;
import java.util.List;
import logging.logging;

public class HydrantService {
    public String getHydrantListe(String strasse) {
        TabelleHydranten h = new TabelleHydranten();
        StringBuilder liste = new StringBuilder();
        try {
            List<Hydrant> daten = h.select(strasse);
            for (Hydrant elem : daten) {
                liste.append(String.valueOf(elem.getHausnummer()) + ", H" + elem.getNennweite());
                liste.append("\n");
            }
        }
        catch (SQLException e) {
            logging.logPrintStackTrace((Exception)e);
        }
        return liste.toString();
    }
}

