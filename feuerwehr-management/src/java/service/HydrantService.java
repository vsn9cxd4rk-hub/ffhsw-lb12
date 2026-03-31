package service;

import data.tabellen.karte.TabelleHydranten;
import go.karte.Hydrant;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import logging.logging;

public class HydrantService {

   public String getHydrantListe(String strasse) {
      TabelleHydranten h = new TabelleHydranten();
      StringBuilder liste = new StringBuilder();

      try {
         List e = h.select(strasse);
         Iterator var6 = e.iterator();

         while(var6.hasNext()) {
            Hydrant elem = (Hydrant)var6.next();
            liste.append(elem.getHausnummer() + ", H" + elem.getNennweite());
            liste.append("\n");
         }
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
      }

      return liste.toString();
   }
}
