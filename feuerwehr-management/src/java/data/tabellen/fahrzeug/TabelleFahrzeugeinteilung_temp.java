package data.tabellen.fahrzeug;

import data.DatenbankZugriff;
import data.tabellen.mitglied.TabelleMitglied;
import go.Fahrzeugeinteilung_temp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleFahrzeugeinteilung_temp {

   public void insert(Fahrzeugeinteilung_temp temp) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO fahrzeugeinteilung_temp (`mitgliederID`, `dienstgradID`, `klasseC`, `klasseB`, `Maschi`,`dlkmaschi`,`korbsteuerung`, `chef`, `tm1`, `AGT`,`TF`,`GF`,`ZF`,`rh`,`rs`,`ra`, `beteiligung`, `position`, `mandantID`) VALUES (\'" + temp.getMitgliederID() + "\', \'" + temp.getDienstgradID() + "\', \'" + temp.getKlasseC() + "\', \'" + temp.getKlasseB() + "\', \'" + temp.getMaschi() + "\', \'" + temp.getDlkmaschi() + "\', \'" + temp.getKorbsteuerung() + "\', \'" + temp.getChef() + "\', \'" + temp.getTm1() + "\', \'" + temp.getAgt() + "\', \'" + temp.getTf() + "\', \'" + temp.getGf() + "\', \'" + temp.getZf() + "\', \'" + temp.getRh() + "\', \'" + temp.getRs() + "\', \'" + temp.getRa() + "\', \'" + temp.getBeteiligung() + "\', \'" + temp.getPosition() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void updatePosition(int mitgliederID, int count) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update fahrzeugeinteilung_temp set position = " + count + " where mitgliederID = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteAll() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from fahrzeugeinteilung_temp where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(String mitgliederNameAusComboBox) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      if(!mitgliederNameAusComboBox.equals("<bitte wählen>")) {
         logging.logInfo("--> Lösche Daten von Temp Tabelle: " + mitgliederNameAusComboBox);
         TabelleMitglied tabMitglieder = new TabelleMitglied();
         String sql = "delete from fahrzeugeinteilung_temp where mitgliederID = " + tabMitglieder.getIdByGuiString(mitgliederNameAusComboBox) + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
         logging.logSQL(sql);
         statement.executeUpdate(sql);
      }

   }

   public int getCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung_temp where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getRestOfMitglieder() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM fahrzeugeinteilung_temp ft LEFT JOIN mitglieder m ON ft.mitgliederID = m.id where ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM fahrzeugeinteilung_temp ft LEFT JOIN mitglieder m ON ft.mitgliederID = m.id where ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public int getGruppenfuehrerCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE gf = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE gf = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountErfahrenstenChef() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.beteiligung desc;");
      return result.next()?result.getInt(1):0;
   }

   public String getErfahrenstenChef() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.chef = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenGruppenfuehrer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.gf = 1 and ft.chef = 0 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung asc");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.gf = 1 and ft.chef = 0 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung asc");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public int getMaschiCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE maschi = 1 and klasseC = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE maschi = 1 and klasseC = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getErfahrenstenMaschnistKlasseC() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseC = 1 and ft.maschi = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getUnerfahrenstenMaschnistKlasseC() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseC = 1 and ft.maschi = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenMaschnistKlasseB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseB = 1 and ft.maschi = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getMaschiOhneTruppführer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.klasseC = 1 and ft.maschi = 1 and ft.tf = 0 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public int getTfCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE tf = 1 and gf = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE tf = 1 and gf = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getErfahrenstenAngriffstruppführer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.TF = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenAngriffstruppführerMitKlasseB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.TF = 1 and ft.klasseB = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenAngriffstruppführerMitKlasseC() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.TF = 1 and ft.klasseC = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getUnerfahrenstenAngriffstruppmann() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.AGT = 1 and ft.TF = 0 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung asc";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getAgtTräger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.AGT = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenerenAngriffstruppmann() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.AGT = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.dienstgradID";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getMelder() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID where ft.tm1 = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung";
      logging.logSQL(sql);
      ResultSet result = statement.executeQuery(sql);
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenRA() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.ra = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.ra = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenRS() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenRSMitKlasseC() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseC = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseC = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenRSMitKlasseB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseB = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rs = 1 and ft.klasseB = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public int getRsCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE rs = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung_temp WHERE rs = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getErfahrenstenRH() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenRHMitKlasseC() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseC = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseC = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenRHMitKlasseB() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseB = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.rh = 1 and ft.klasseB = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenDLKGF() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.GF = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.GF = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenDLKFahrer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.klasseC = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.dlkmaschi = 1 and ft.klasseC = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenTFMitKorbEinweisung() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tf = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tf = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenFMMitKorbEinweisung() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tm1 = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.korbsteuerung = 1 and ft.tm1 = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }

   public String getErfahrenstenZF() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.zf = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN fahrzeugeinteilung_temp ft ON m.id = ft.mitgliederID WHERE ft.zf = 1 and ft.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ft.position asc, ft.beteiligung desc;");
      if(result.next()) {
         logging.logInfo("Auswahl: " + result.getString(1) + ", " + result.getString(2));
         return result.getString(1) + ", " + result.getString(2);
      } else {
         return "<bitte wählen>";
      }
   }
}
