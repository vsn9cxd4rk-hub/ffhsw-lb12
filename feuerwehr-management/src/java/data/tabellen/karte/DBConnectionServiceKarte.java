package data.tabellen.karte;

import data.DatenbankKartenZugriff;
import data.DatenbankZugriff;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class DBConnectionServiceKarte {

   public static Statement getDBZugriff() throws SQLException {
      Statement statement;
      if(((String)runApplication.EINSTELLUNGEN.get("externeDatenbankFürKartendaten")).equals("0")) {
         statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
         return statement;
      } else if(((String)runApplication.EINSTELLUNGEN.get("externeDatenbankFürKartendaten")).equals("1")) {
         try {
            statement = DatenbankKartenZugriff.getInstance().getDbConnection().createStatement();
            return statement;
         } catch (Exception var2) {
            logging.logError("externer Kartenserver ist nicht erreichbar - verbinde mit Lokalen Karten Daten...");
            statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
            return statement;
         }
      } else {
         return null;
      }
   }
}
