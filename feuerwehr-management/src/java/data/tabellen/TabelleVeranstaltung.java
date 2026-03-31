package data.tabellen;

import data.DatenbankZugriff;
import data.tabellen.TabelleAnwesenheit;
import go.Veranstaltung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class TabelleVeranstaltung {

   public void insert(Veranstaltung veranstaltung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO veranstaltung (`id`, `name`, `name2`, `kategorie`, `mitgliederGruppe`, `datum`, `zeit`, `zeitEnde`, `fahrzeugeinteilung`, `infoVersandt` , `mandantID`) VALUES (\'" + veranstaltung.getId() + "\', \'" + veranstaltung.getName() + "\', \'" + veranstaltung.getName2() + "\', \'" + veranstaltung.getKategorie() + "\', \'" + veranstaltung.getMitgliederGruppe() + "\', \'" + veranstaltung.getDatum() + "\', \'" + veranstaltung.getZeit() + "\', \'" + veranstaltung.getZeitEnde() + "\', \'" + veranstaltung.getFahrzeugeinteilung() + "\', \'" + veranstaltung.getInfoVersandt() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Veranstaltung veranstaltung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update veranstaltung set name = \'" + veranstaltung.getName() + "\', name2 = \'" + veranstaltung.getName2() + "\', kategorie = \'" + veranstaltung.getKategorie() + "\', mitgliederGruppe = \'" + veranstaltung.getMitgliederGruppe() + "\', datum = \'" + veranstaltung.getDatum() + "\', zeit = \'" + veranstaltung.getZeit() + "\', zeitEnde = \'" + veranstaltung.getZeitEnde() + "\' where id = " + veranstaltung.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateFahrzeugeinteilung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "UPDATE veranstaltung set fahrzeugeinteilung = 1 where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateInfoVersandt(String datumVon, String datumBis) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update veranstaltung set infoVersandt = 1 where kategorie != 1 and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void deleteOne(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM veranstaltung;");
      logging.logSQL("SELECT max(id) FROM veranstaltung;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getJahrDerVeranstaltung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?Integer.parseInt(result.getString(1).substring(0, 4)):0;
   }

   public int getVeranstaltungID(String name) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM veranstaltung where name = \'" + name + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getDatum(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getZeitStart(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT zeit FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT zeit FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getZeitEnde(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT zeitEnde FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT zeitEnde FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getFahrzeugeinteilungStatus(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT fahrzeugeinteilung FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT fahrzeugeinteilung FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCount(int veranstaltungKategorie, String datum, String zeit) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and datum = \'" + TimeCalculation.parseDateForDatabase(datum) + "\' and zeit = \'" + zeit + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and datum = \'" + TimeCalculation.parseDateForDatabase(datum) + "\' and zeit = \'" + zeit + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getCountByVeranstaltungskategorie(int veranstaltungKategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM veranstaltung where kategorie = " + veranstaltungKategorie + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllVeranstaltung(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM veranstaltung where datum between \'" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01\' and \'" + runApplication.veranstaltungsAnzeigeZukunft + "-31\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where datum between \'" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01\' and \'" + runApplication.veranstaltungsAnzeigeZukunft + "-31\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ID;");
      logging.logSQL("SELECT id FROM veranstaltung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by ID;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public HashMap getVeranstaltungData(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT * FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT * FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("id", result.getString("id"));
         map.put("name", result.getString("name"));
         map.put("name2", result.getString("name2"));
         map.put("kategorie", result.getString("kategorie"));
         map.put("mitgliederGruppe", result.getString("mitgliederGruppe"));
         map.put("datum", result.getString("datum"));
         map.put("zeit", result.getString("zeit"));
         map.put("zeitEnde", result.getString("zeitEnde"));
         map.put("fahrzeugeinteilung", result.getString("fahrzeugeinteilung"));
         map.put("infoVersandt", result.getString("infoVersandt"));
      }

      return map;
   }

   public Veranstaltung getVeranstaltungData2(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT * FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT * FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Veranstaltung veranstaltung = new Veranstaltung();

      while(result.next()) {
         veranstaltung.setId(result.getInt("id"));
         veranstaltung.setName(result.getString("name"));
         veranstaltung.setName2(result.getString("name2"));
         veranstaltung.setKategorie(result.getInt("kategorie"));
         veranstaltung.setDatum(result.getString("datum"));
         veranstaltung.setZeit(result.getString("zeit"));
         veranstaltung.setZeitEnde(result.getString("zeitEnde"));
         veranstaltung.setFahrzeugeinteilung(result.getInt("fahrzeugeinteilung"));
         veranstaltung.setInfoVersandt(result.getInt("infoVersandt"));
      }

      return veranstaltung;
   }

   public ArrayList getAllVeranstaltungWithoutFahrzeugeinteilung() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where fahrzeugeinteilung = 0 and datum between \'" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01\' and \'" + runApplication.veranstaltungsAnzeigeZukunft + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      logging.logSQL("SELECT name FROM veranstaltung where fahrzeugeinteilung = 0 and  datum between \'" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01\' and \'" + runApplication.veranstaltungsAnzeigeZukunft + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinerKategorie(int kategorie, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM veranstaltung where mitgliederGruppe = " + mitgliederGruppe + " and kategorie = " + kategorie + " and datum between \'" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01\' and \'" + runApplication.veranstaltungsAnzeigeZukunft + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where mitgliederGruppe = " + mitgliederGruppe + " and kategorie = " + kategorie + " and datum between \'" + runApplication.veranstaltungsAnzeigeVergangenheit + "-01\' and \'" + runApplication.veranstaltungsAnzeigeZukunft + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinerKategorieFromDB(int kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinerKategorieByJahr(int kategorie, int jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and mitgliederGruppe = " + mitgliederGruppe + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and mitgliederGruppe = " + mitgliederGruppe + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllKommendenVeranstaltungEinerKategorieByJahr(int kategorie, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between \'" + jahr + "-" + SbcUtils.timeStamp("MM-dd") + "\' and \'" + jahr + "-12-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where kategorie = " + kategorie + " and datum between \'" + jahr + "-" + SbcUtils.timeStamp("MM-dd") + "\' and \'" + jahr + "-12-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungWithoutInfoVersandtInDiesemMonat(String datumVon, String datumBis, boolean alleVeranstaltungenWaehlen) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String additionalSQL;
      if(alleVeranstaltungenWaehlen) {
         additionalSQL = "";
      } else {
         additionalSQL = "and infoVersandt = 0";
      }

      logging.logSQL("SELECT id, name2, datum, zeit, kategorie FROM veranstaltung v where kategorie != 1 " + additionalSQL + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT id, name2, datum, zeit, kategorie FROM veranstaltung v where kategorie != 1 " + additionalSQL + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");

      ArrayList liste;
      for(liste = new ArrayList(); result.next(); liste.add("\n")) {
         liste.add(result.getString("name2") + "\n" + TimeCalculation.parseDateForGUI(result.getString("datum")) + " um " + result.getString("zeit") + " Uhr\n");
         if(result.getInt("kategorie") == 3) {
            String[] anwesendeMitglieder = Utils.listToArray((new TabelleAnwesenheit()).getAnwesendeMitglieder(result.getInt("id")));
            if(anwesendeMitglieder.length != 0) {
               liste.add("Teilnehmer: ");

               for(int i = 0; i < anwesendeMitglieder.length; ++i) {
                  liste.add(anwesendeMitglieder[i] + "; ");
               }

               liste.add("\n");
            } else {
               liste.add("Teilnehmer: --\n");
            }
         }
      }

      return liste;
   }

   public int getCountVeranstaltungWithoutInfoVersandtInDiesemMonat(String datumVon, String datumBis, int status) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie != 1 and infoVersandt = " + status + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM veranstaltung where kategorie != 1 and infoVersandt = " + status + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getAllVeranstaltungEinesJahres(String jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM veranstaltung where mitgliederGruppe = " + mitgliederGruppe + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where mitgliederGruppe = " + mitgliederGruppe + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinesZeitraums(String datumVon, String datumBis, int kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String kategoriePartOfStatement = null;
      if(kategorie == 0) {
         kategoriePartOfStatement = "kategorie > 1";
      } else {
         kategoriePartOfStatement = "kategorie = " + kategorie;
      }

      logging.logSQL("SELECT name2 FROM veranstaltung where " + kategoriePartOfStatement + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name2 FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinesZeitraumsIDs(String datumVon, String datumBis, int kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String kategoriePartOfStatement = null;
      if(kategorie == 0) {
         kategoriePartOfStatement = "kategorie > 1";
      } else {
         kategoriePartOfStatement = "kategorie = " + kategorie;
      }

      logging.logSQL("SELECT id FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT id FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinesZeitraumsDatum(String datumVon, String datumBis, int kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String kategoriePartOfStatement = null;
      if(kategorie == 0) {
         kategoriePartOfStatement = "kategorie > 1";
      } else {
         kategoriePartOfStatement = "kategorie = " + kategorie;
      }

      logging.logSQL("SELECT datum FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT datum FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinesZeitraumsZeit(String datumVon, String datumBis, int kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String kategoriePartOfStatement = null;
      if(kategorie == 0) {
         kategoriePartOfStatement = "kategorie > 1";
      } else {
         kategoriePartOfStatement = "kategorie = " + kategorie;
      }

      logging.logSQL("SELECT zeit FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT zeit FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getAllVeranstaltungEinesZeitraumsZeitEnde(String datumVon, String datumBis, int kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String kategoriePartOfStatement = null;
      if(kategorie == 0) {
         kategoriePartOfStatement = "kategorie > 1";
      } else {
         kategoriePartOfStatement = "kategorie = " + kategorie;
      }

      logging.logSQL("SELECT zeitEnde FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT zeitEnde FROM veranstaltung where " + kategoriePartOfStatement + " and  datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getCountAllVeranstaltungEinesJahresByKategorie(String jahr, int kategorie, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between \'" + jahr + "-01-01\' and \'" + jahr + "-12-31\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      return result.next()?result.getInt(1):0;
   }

   public int getCountAllAbgelaufendenVeranstaltungEinesJahresByKategorie(String jahr, int kategorie) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between \'" + jahr + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where kategorie = " + kategorie + " and datum between \'" + jahr + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      return result.next()?result.getInt(1):0;
   }

   public int getCountAllVeranstaltungEinesJahres(String jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM veranstaltung where datum between \'" + jahr + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM veranstaltung where datum between \'" + jahr + "-01-01\' and \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum;");
      return result.next()?result.getInt(1):0;
   }

   public int getVeranstaltungKategorieID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT kategorie FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT kategorie FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getVeranstaltungName2AndDatum(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name2, datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name2, datum FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1) + "_" + TimeCalculation.parseDateForGUI(result.getString(2)):null;
   }

   public String getVeranstaltungName2(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name2 FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name2 FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getVeranstaltungName(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM veranstaltung where id = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public Vector getAllVeranstaltungForTable(String datumVon, String datumBis, int kategorie, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String kategoriePartOfStatement = null;
      if(kategorie == 0) {
         kategoriePartOfStatement = "kategorie > 1";
      } else {
         kategoriePartOfStatement = "kategorie = " + kategorie;
      }

      logging.logSQL("SELECT datum, zeit, name FROM veranstaltung where " + kategoriePartOfStatement + " and mitgliederGruppe = " + mitgliederGruppe + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT datum, zeit, name FROM veranstaltung where " + kategoriePartOfStatement + " and mitgliederGruppe = " + mitgliederGruppe + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getAllVeranstaltungForTableListe(String datumVon, String datumBis, int kategorie, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String kategoriePartOfStatement = null;
      if(kategorie == 0) {
         kategoriePartOfStatement = "kategorie > 1";
      } else {
         kategoriePartOfStatement = "kategorie = " + kategorie;
      }

      logging.logSQL("SELECT name FROM veranstaltung where " + kategoriePartOfStatement + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where " + kategoriePartOfStatement + " and datum between \'" + datumVon + "\' and \'" + datumBis + "\' and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public ArrayList getVeranstaltungDiesenMonats() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name FROM veranstaltung where datum between \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and \'" + SbcUtils.timeStamp("yyyy-MM") + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ResultSet result = statement.executeQuery("SELECT name FROM veranstaltung where datum between \'" + SbcUtils.timeStamp("yyyy-MM-dd") + "\' and \'" + SbcUtils.timeStamp("yyyy-MM") + "-31\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by datum, zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector veranstaltungListe = new Vector();
      veranstaltungListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
      veranstaltungListe.add(result.getString("zeit"));
      veranstaltungListe.add(result.getString("name"));
      return veranstaltungListe;
   }
}
