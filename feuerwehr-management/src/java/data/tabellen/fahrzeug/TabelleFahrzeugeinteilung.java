package data.tabellen.fahrzeug;

import data.DatenbankZugriff;
import go.Fahrzeugeinteilung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleFahrzeugeinteilung {

   public void insert(Fahrzeugeinteilung feinteilung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO fahrzeugeinteilung (`id`, `veranstaltungID`, `kategorie`, `jahr`, `mitgliederID`, `fahrzeugID`, `position`, `mandantID`) VALUES (\'" + feinteilung.getId() + "\', \'" + feinteilung.getVeranstaltungID() + "\', \'" + feinteilung.getKategorie() + "\', \'" + feinteilung.getJahr() + "\', \'" + feinteilung.getMitgliederID() + "\', \'" + feinteilung.getFahrzeugID() + "\', \'" + feinteilung.getPosition() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void delete(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from fahrzeugeinteilung where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateKategorie(int kategorieID, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update fahrzeugeinteilung set kategorie = " + kategorieID + " where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNumer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM fahrzeugeinteilung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrzeugeinteilung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1) + 1:0;
   }

   public ArrayList getPositionListe(String fahrzeugID, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.fahrzeugID = " + fahrzeugID + " and f.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by f.position;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.fahrzeugID = " + fahrzeugID + " and f.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by f.position;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getEingeteilteKameraden(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT mitgliederID FROM fahrzeugeinteilung WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT mitgliederID FROM fahrzeugeinteilung WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getCountOfCurrentVehicle(int mitgliederID, int fahrzeugID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliederID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliederID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfVeranstaltung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountOfPosition(int mitgliedID, int positionID, int jahr, int fahrzeugID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String additionalParameter = "";
      if(fahrzeugID != 0) {
         additionalParameter = "and fahrzeugID = " + fahrzeugID;
      }

      logging.logSQL("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliedID + " and position = " + positionID + " and jahr = " + jahr + " and kategorie = 1 " + additionalParameter + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliedID + " and position = " + positionID + " and jahr = " + jahr + " and kategorie = 1 " + additionalParameter + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public Vector getFahrzeugBesatzungForTable(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT fahr.name as fahrzeugname, fahr.trupp, fahr.beschreibung as fahrzeugtyp, f.position, d.beschreibung as dienstgrad, m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN fahrzeuge fahr ON f.fahrzeugID = fahr.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by f.fahrzeugID, f.position;");
      ResultSet result = statement.executeQuery("SELECT fahr.name as fahrzeugname, fahr.trupp, fahr.beschreibung as fahrzeugtyp, f.position, d.beschreibung as dienstgrad, m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN fahrzeuge fahr ON f.fahrzeugID = fahr.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by f.fahrzeugID, f.position;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector besatzung = new Vector();
      besatzung.add(result.getString("fahrzeugname"));
      if(!result.getString("fahrzeugtyp").equals("13") && !result.getString("fahrzeugtyp").equals("14")) {
         if(!result.getString("fahrzeugtyp").equals("4") && !result.getString("fahrzeugtyp").equals("5")) {
            if(result.getString("fahrzeugtyp").equals("12")) {
               if(result.getInt("position") == 0) {
                  besatzung.add("Zugführer");
               } else if(result.getInt("position") == 1) {
                  besatzung.add("Fahrer");
               } else if(result.getInt("position") == 2) {
                  besatzung.add("Sitzplatz 1");
               } else if(result.getInt("position") == 3) {
                  besatzung.add("Sitzplatz 2");
               } else if(result.getInt("position") == 4) {
                  besatzung.add("Sitzplatz 3");
               } else if(result.getInt("position") == 5) {
                  besatzung.add("Sitzplatz 4");
               } else {
                  besatzung.add("Frei");
               }
            } else if(result.getString("fahrzeugtyp").equals("6") | result.getString("fahrzeugtyp").equals("9") | result.getString("fahrzeugtyp").equals("7")) {
               if(result.getInt("position") == 0) {
                  besatzung.add("Gruppenführer");
               } else if(result.getInt("position") == 1) {
                  besatzung.add("Fahrer");
               } else if(result.getInt("position") == 2) {
                  besatzung.add("Sitzplatz 1");
               } else if(result.getInt("position") == 3) {
                  besatzung.add("Sitzplatz 2");
               } else if(result.getInt("position") == 4) {
                  besatzung.add("Sitzplatz 3");
               } else if(result.getInt("position") == 5) {
                  besatzung.add("Sitzplatz 4");
               } else if(result.getInt("position") == 6) {
                  besatzung.add("Sitzplatz 5");
               } else if(result.getInt("position") == 7) {
                  besatzung.add("Sitzplatz 6");
               } else if(result.getInt("position") == 8) {
                  besatzung.add("Sitzplatz 7");
               }
            } else if(result.getInt("trupp") == 0) {
               if(result.getInt("position") == 0) {
                  besatzung.add("Gruppenführer");
               } else if(result.getInt("position") == 1) {
                  besatzung.add("Maschinist");
               } else if(result.getInt("position") == 2) {
                  besatzung.add("Angrifftruppführer");
               } else if(result.getInt("position") == 3) {
                  besatzung.add("Angriffstruppmann");
               } else if(result.getInt("position") == 4) {
                  besatzung.add("Wassertruppführer");
               } else if(result.getInt("position") == 5) {
                  besatzung.add("Wassertruppmann");
               } else if(result.getInt("position") == 6) {
                  besatzung.add("Schlauchtruppführer");
               } else if(result.getInt("position") == 7) {
                  besatzung.add("Schlauchtruppmann");
               } else if(result.getInt("position") == 8) {
                  besatzung.add("Melder");
               }
            } else if(result.getInt("trupp") == 1) {
               if(result.getInt("position") == 2) {
                  besatzung.add("Truppführer");
               } else if(result.getInt("position") == 1) {
                  besatzung.add("Maschinist");
               } else if(result.getInt("position") == 3) {
                  besatzung.add("Truppmann");
               } else {
                  besatzung.add("Frei");
               }
            }
         } else if(result.getInt("trupp") == 0) {
            if(result.getInt("position") == 0) {
               besatzung.add("Leiterführer");
            } else if(result.getInt("position") == 1) {
               besatzung.add("Maschinist");
            } else if(result.getInt("position") == 2) {
               besatzung.add("Truppmann");
            } else {
               besatzung.add("Frei");
            }
         } else if(result.getInt("trupp") == 1) {
            if(result.getInt("position") == 2) {
               besatzung.add("Leiterführer");
            } else if(result.getInt("position") == 1) {
               besatzung.add("Maschinist");
            } else if(result.getInt("position") == 3) {
               besatzung.add("Truppmann");
            } else {
               besatzung.add("Frei");
            }
         }
      } else if(result.getInt("position") == 0) {
         besatzung.add("Transportführer");
      } else if(result.getInt("position") == 1) {
         besatzung.add("Fahrzeugführer");
      } else if(result.getInt("position") == 2) {
         besatzung.add("Sitzplatz 1");
      } else if(result.getInt("position") == 3) {
         besatzung.add("Sitzplatz 2");
      } else {
         besatzung.add("Frei");
      }

      besatzung.add(result.getString("dienstgrad"));
      besatzung.add(result.getString("name") + ", " + result.getString("vorname"));
      return besatzung;
   }
}
