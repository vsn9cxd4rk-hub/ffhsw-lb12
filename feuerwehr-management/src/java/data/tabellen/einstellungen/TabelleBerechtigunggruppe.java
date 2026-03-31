package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import data.tabellen.einstellungen.TabelleBerechtigung;
import go.Berechtigung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBerechtigunggruppe {

   public void insert(Berechtigung bereichtigung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO berechtigunggruppe (`id`, `seite`, `name`, `BR0`,`BR1`,`BR2`,`BR3`,`BR4`,`BR5`,`BR6`,`BR7`,`BR8`,`BR9`,`BR10`,`BR11`,`BR12`,`BR13`,`BR14`,`BR15`,`BR16`,`BR17`,`BR18`,`BR19`,`BR20`,`BR21`,`BR22`,`BR23`,`BR24`,`BR25`,`BR26`,`BR27`,`BR28`,`BR29`,`BR30`,`BR31`,`BR32`,`BR33`,`BR34`,`BR35`,`BR36`,`BR37`,`BR38`,`BR39`,`BR40`,`BR41`,`BR42`,`BR43`,`BR44`,`BR45`,`BR46`,`BR47`,`BR48`,`BR49`,`BR50`,`BR51`,`BR52`,`BR53`,`BR54`,`BR55`,`BR56`,`BR57`,`BR58`,`BR59`,`BR60`,`BR61`,`BR62`,`BR63`,`BR64`,`BR65`,`BR66`,`BR67`,`BR68`,`BR69`,`BR70`,`BR71`,`BR72`,`BR73`,`BR74`,`BR75`,`BR76`,`BR77`,`BR78`,`BR79`,`BR80`,`BR81`,`BR82`,`BR83`,`BR84`,`BR85`,`BR86`,`BR87`,`BR88`,`BR89`,`BR90`,`BR91`,`BR92`,`BR93`,`BR94`,`BR95`, `mandantID`) VALUES (\'" + bereichtigung.getId() + "\', \'" + bereichtigung.getSeite() + "\', \'" + bereichtigung.getName() + "\', \'" + bereichtigung.getBR0() + "\', \'" + bereichtigung.getBR1() + "\', \'" + bereichtigung.getBR2() + "\', \'" + bereichtigung.getBR3() + "\', \'" + bereichtigung.getBR4() + "\', \'" + bereichtigung.getBR5() + "\', \'" + bereichtigung.getBR6() + "\', \'" + bereichtigung.getBR7() + "\', \'" + bereichtigung.getBR8() + "\', \'" + bereichtigung.getBR9() + "\', \'" + bereichtigung.getBR10() + "\', \'" + bereichtigung.getBR11() + "\', \'" + bereichtigung.getBR12() + "\', \'" + bereichtigung.getBR13() + "\', \'" + bereichtigung.getBR14() + "\', \'" + bereichtigung.getBR15() + "\', \'" + bereichtigung.getBR16() + "\', \'" + bereichtigung.getBR17() + "\', \'" + bereichtigung.getBR18() + "\', \'" + bereichtigung.getBR19() + "\', \'" + bereichtigung.getBR20() + "\', \'" + bereichtigung.getBR21() + "\', \'" + bereichtigung.getBR22() + "\', \'" + bereichtigung.getBR23() + "\', \'" + bereichtigung.getBR24() + "\', \'" + bereichtigung.getBR25() + "\', \'" + bereichtigung.getBR26() + "\', \'" + bereichtigung.getBR27() + "\', \'" + bereichtigung.getBR28() + "\', \'" + bereichtigung.getBR29() + "\', \'" + bereichtigung.getBR30() + "\', \'" + bereichtigung.getBR31() + "\', \'" + bereichtigung.getBR32() + "\', \'" + bereichtigung.getBR33() + "\', \'" + bereichtigung.getBR34() + "\', \'" + bereichtigung.getBR35() + "\', \'" + bereichtigung.getBR36() + "\', \'" + bereichtigung.getBR37() + "\', \'" + bereichtigung.getBR38() + "\', \'" + bereichtigung.getBR39() + "\', \'" + bereichtigung.getBR40() + "\', \'" + bereichtigung.getBR41() + "\', \'" + bereichtigung.getBR42() + "\', \'" + bereichtigung.getBR43() + "\', \'" + bereichtigung.getBR44() + "\', \'" + bereichtigung.getBR45() + "\', \'" + bereichtigung.getBR46() + "\', \'" + bereichtigung.getBR47() + "\', \'" + bereichtigung.getBR48() + "\', \'" + bereichtigung.getBR49() + "\', \'" + bereichtigung.getBR50() + "\', \'" + bereichtigung.getBR51() + "\', \'" + bereichtigung.getBR52() + "\', \'" + bereichtigung.getBR53() + "\', \'" + bereichtigung.getBR54() + "\', \'" + bereichtigung.getBR55() + "\', \'" + bereichtigung.getBR56() + "\', \'" + bereichtigung.getBR57() + "\', \'" + bereichtigung.getBR58() + "\', \'" + bereichtigung.getBR59() + "\', \'" + bereichtigung.getBR60() + "\', \'" + bereichtigung.getBR61() + "\', \'" + bereichtigung.getBR62() + "\', \'" + bereichtigung.getBR63() + "\', \'" + bereichtigung.getBR64() + "\', \'" + bereichtigung.getBR65() + "\', \'" + bereichtigung.getBR66() + "\', \'" + bereichtigung.getBR67() + "\', \'" + bereichtigung.getBR68() + "\', \'" + bereichtigung.getBR69() + "\', \'" + bereichtigung.getBR70() + "\', \'" + bereichtigung.getBR71() + "\', \'" + bereichtigung.getBR72() + "\', \'" + bereichtigung.getBR73() + "\', \'" + bereichtigung.getBR74() + "\', \'" + bereichtigung.getBR75() + "\', \'" + bereichtigung.getBR76() + "\', \'" + bereichtigung.getBR77() + "\', \'" + bereichtigung.getBR78() + "\', \'" + bereichtigung.getBR79() + "\', \'" + bereichtigung.getBR80() + "\', \'" + bereichtigung.getBR81() + "\', \'" + bereichtigung.getBR82() + "\', \'" + bereichtigung.getBR83() + "\', \'" + bereichtigung.getBR84() + "\', \'" + bereichtigung.getBR85() + "\', \'" + bereichtigung.getBR86() + "\', \'" + bereichtigung.getBR87() + "\', \'" + bereichtigung.getBR88() + "\', \'" + bereichtigung.getBR89() + "\', \'" + bereichtigung.getBR90() + "\', \'" + bereichtigung.getBR91() + "\', \'" + bereichtigung.getBR92() + "\', \'" + bereichtigung.getBR93() + "\', \'" + bereichtigung.getBR94() + "\', \'" + bereichtigung.getBR95() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Berechtigung bereichtigung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update berechtigunggruppe set BR0 = " + bereichtigung.getBR0() + ", BR1 = " + bereichtigung.getBR1() + ", BR2 = " + bereichtigung.getBR2() + ", BR3 = " + bereichtigung.getBR3() + ", BR4 = " + bereichtigung.getBR4() + ", BR5 = " + bereichtigung.getBR5() + ", BR6 = " + bereichtigung.getBR6() + ", BR7 = " + bereichtigung.getBR7() + ", BR8 = " + bereichtigung.getBR8() + ", BR9 = " + bereichtigung.getBR9() + ", BR10 = " + bereichtigung.getBR10() + ", BR11 = " + bereichtigung.getBR11() + ", BR12 = " + bereichtigung.getBR12() + ", BR13 = " + bereichtigung.getBR13() + ", BR14 = " + bereichtigung.getBR14() + ", BR15 = " + bereichtigung.getBR15() + ", BR16 = " + bereichtigung.getBR16() + ", BR17 = " + bereichtigung.getBR17() + ", BR18 = " + bereichtigung.getBR18() + ", BR19 = " + bereichtigung.getBR19() + ", BR20 = " + bereichtigung.getBR20() + ", BR21 = " + bereichtigung.getBR21() + ", BR22 = " + bereichtigung.getBR22() + ", BR23 = " + bereichtigung.getBR23() + ", BR24 = " + bereichtigung.getBR24() + ", BR25 = " + bereichtigung.getBR25() + ", BR26 = " + bereichtigung.getBR26() + ", BR27 = " + bereichtigung.getBR27() + ", BR28 = " + bereichtigung.getBR28() + ", BR29 = " + bereichtigung.getBR29() + ", BR30 = " + bereichtigung.getBR30() + ", BR31 = " + bereichtigung.getBR31() + ", BR32 = " + bereichtigung.getBR32() + ", BR33 = " + bereichtigung.getBR33() + ", BR34 = " + bereichtigung.getBR34() + ", BR35 = " + bereichtigung.getBR35() + ", BR36 = " + bereichtigung.getBR36() + ", BR37 = " + bereichtigung.getBR37() + ", BR38 = " + bereichtigung.getBR38() + ", BR39 = " + bereichtigung.getBR39() + ", BR40 = " + bereichtigung.getBR40() + ", BR41 = " + bereichtigung.getBR41() + ", BR42 = " + bereichtigung.getBR42() + ", BR43 = " + bereichtigung.getBR43() + ", BR44 = " + bereichtigung.getBR44() + ", BR45 = " + bereichtigung.getBR45() + ", BR46 = " + bereichtigung.getBR46() + ", BR47 = " + bereichtigung.getBR47() + ", BR48 = " + bereichtigung.getBR48() + ", BR49 = " + bereichtigung.getBR49() + ", BR50 = " + bereichtigung.getBR50() + ", BR51 = " + bereichtigung.getBR51() + ", BR52 = " + bereichtigung.getBR52() + ", BR53 = " + bereichtigung.getBR53() + ", BR54 = " + bereichtigung.getBR54() + ", BR55 = " + bereichtigung.getBR55() + ", BR56 = " + bereichtigung.getBR56() + ", BR57 = " + bereichtigung.getBR57() + ", BR58 = " + bereichtigung.getBR58() + ", BR59 = " + bereichtigung.getBR59() + ", BR60 = " + bereichtigung.getBR60() + ", BR61 = " + bereichtigung.getBR61() + ", BR62 = " + bereichtigung.getBR62() + ", BR63 = " + bereichtigung.getBR63() + ", BR64 = " + bereichtigung.getBR64() + ", BR65 = " + bereichtigung.getBR65() + ", BR66 = " + bereichtigung.getBR66() + ", BR67 = " + bereichtigung.getBR67() + ", BR68 = " + bereichtigung.getBR68() + ", BR69 = " + bereichtigung.getBR69() + ", BR70 = " + bereichtigung.getBR70() + ", BR71 = " + bereichtigung.getBR71() + ", BR72 = " + bereichtigung.getBR72() + ", BR73 = " + bereichtigung.getBR73() + ", BR74 = " + bereichtigung.getBR74() + ", BR75 = " + bereichtigung.getBR75() + ", BR76 = " + bereichtigung.getBR76() + ", BR77 = " + bereichtigung.getBR77() + ", BR78 = " + bereichtigung.getBR78() + ", BR79 = " + bereichtigung.getBR79() + ", BR80 = " + bereichtigung.getBR80() + ", BR81 = " + bereichtigung.getBR81() + ", BR82 = " + bereichtigung.getBR82() + ", BR83 = " + bereichtigung.getBR83() + ", BR84 = " + bereichtigung.getBR84() + ", BR85 = " + bereichtigung.getBR85() + ", BR86 = " + bereichtigung.getBR86() + ", BR87 = " + bereichtigung.getBR87() + ", BR88 = " + bereichtigung.getBR88() + ", BR89 = " + bereichtigung.getBR89() + ", BR90 = " + bereichtigung.getBR90() + ", BR91 = " + bereichtigung.getBR91() + ", BR92 = " + bereichtigung.getBR92() + ", BR93 = " + bereichtigung.getBR93() + ", BR94 = " + bereichtigung.getBR94() + ", BR95 = " + bereichtigung.getBR95() + " where id = " + bereichtigung.getId() + " and seite = " + bereichtigung.getSeite() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int[] getAll(int id, int seite) {
      try {
         Statement e = DatenbankZugriff.getInstance().getDbConnection().createStatement();
         logging.logSQL("SELECT BR0,BR1,BR2,BR3,BR4,BR5,BR6,BR7,BR8,BR9,BR10,BR11,BR12,BR13,BR14,BR15,BR16,BR17,BR18,BR19,BR20,BR21,BR22,BR23,BR24,BR25,BR26,BR27,BR28,BR29,BR30,BR31,BR32,BR33,BR34,BR35,BR36,BR37,BR38,BR39,BR40,BR41,BR42,BR43,BR44,BR45,BR46,BR47,BR48,BR49,BR50,BR51,BR52,BR53,BR54,BR55,BR56,BR57,BR58,BR59,BR60,BR61,BR62,BR63,BR64,BR65,BR66,BR67,BR68,BR69,BR70,BR71,BR72,BR73,BR74,BR75,BR76,BR77,BR78,BR79,BR80,BR81,BR82,BR83,BR84,BR85,BR86,BR87,BR88,BR89,BR90,BR91,BR92,BR93,BR94,BR95 from berechtigunggruppe where id = " + id + " and seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
         ResultSet result = e.executeQuery("SELECT BR0,BR1,BR2,BR3,BR4,BR5,BR6,BR7,BR8,BR9,BR10,BR11,BR12,BR13,BR14,BR15,BR16,BR17,BR18,BR19,BR20,BR21,BR22,BR23,BR24,BR25,BR26,BR27,BR28,BR29,BR30,BR31,BR32,BR33,BR34,BR35,BR36,BR37,BR38,BR39,BR40,BR41,BR42,BR43,BR44,BR45,BR46,BR47,BR48,BR49,BR50,BR51,BR52,BR53,BR54,BR55,BR56,BR57,BR58,BR59,BR60,BR61,BR62,BR63,BR64,BR65,BR66,BR67,BR68,BR69,BR70,BR71,BR72,BR73,BR74,BR75,BR76,BR77,BR78,BR79,BR80,BR81,BR82,BR83,BR84,BR85,BR86,BR87,BR88,BR89,BR90,BR91,BR92,BR93,BR94,BR95 from berechtigunggruppe where id = " + id + " and seite = " + seite + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
         int[] liste = new int[(new TabelleBerechtigung()).getCount(seite)];

         while(result.next()) {
            for(int i = 0; i < liste.length; ++i) {
               liste[i] = result.getInt(i + 1);
            }
         }

         return liste;
      } catch (SQLException var7) {
         logging.logPrintStackTrace(var7);
         return null;
      }
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM berechtigunggruppe;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM berechtigunggruppe;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getID(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id FROM berechtigunggruppe where name = \'" + name + "\' and seite = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id FROM berechtigunggruppe where name = \'" + name + "\' and seite = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getBerechtigungName(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM berechtigunggruppe where id = " + id + " and seite = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT name FROM berechtigunggruppe where id = " + id + " and seite = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getBercehtigungsgruppen() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name from berechtigunggruppe where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by name order by name;");
      logging.logSQL("SELECT name from berechtigunggruppe where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by name order by name;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }
}
