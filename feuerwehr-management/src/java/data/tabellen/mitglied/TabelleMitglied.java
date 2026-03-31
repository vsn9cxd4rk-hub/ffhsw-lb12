package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder;
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

public class TabelleMitglied {

   public void insert(Mitglieder mitglied) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO mitglieder (`id`, `mitgliederGruppe`,`anrede`,`name`, `vorname`, `strasse`, `ort`, `telefonPrivat`, `telefonMobil`, `telefonArbeit`, `telegrammID`, `email`, `email2`, `beruf`, `staat`, `dienstgrad`, `ausserDienst`, `mitgliedSeit`, `mitgliedBis`, `gebDatum`, `hochzeit`, `kommentar`,`fuehrerscheinNummer`,`fahrberechtigungNummer`,`dienstausweisNummer`,`krankenkasse`,`medikamente`,`krankheiten`,`schwimmabzeichen`,`sportabzeichen`, `loeschkenner`, `eMailDeaktiv`, `mandantID`) VALUES (\'" + mitglied.getId() + "\', \'" + mitglied.getMitgliederGruppe() + "\', \'" + mitglied.getAnrede() + "\', \'" + mitglied.getName() + "\', \'" + mitglied.getVorname() + "\', \'" + mitglied.getStrasse() + "\', \'" + mitglied.getOrt() + "\', \'" + mitglied.getTelefonePrivate() + "\', \'" + mitglied.getTelefonMobile() + "\', \'" + mitglied.getTelefonArbeit() + "\', \'" + mitglied.getTelegrammID() + "\', \'" + mitglied.getEmail() + "\', \'" + mitglied.getEmail2() + "\', \'" + mitglied.getBeruf() + "\', \'" + mitglied.getStaat() + "\', \'" + mitglied.getDienstgrad() + "\', \'" + mitglied.getAusserDienst() + "\', \'" + mitglied.getMitgliedSeit() + "\', \'" + mitglied.getMitgliedBis() + "\', \'" + mitglied.getGebDatum() + "\', \'" + mitglied.getHochzeit() + "\', \'" + mitglied.getKommentar() + "\', \'" + mitglied.getFuehrerscheinNummer() + "\', \'" + mitglied.getFahrberechtigungNummer() + "\', \'" + mitglied.getDienstausweisNummer() + "\', \'" + mitglied.getKrankenkasse() + "\', \'" + mitglied.getMedikamente() + "\', \'" + mitglied.getKrankheiten() + "\', \'" + mitglied.getSchwimmabzeichen() + "\', \'" + mitglied.getSportabzeichen() + "\', \'" + mitglied.getLoschkenner() + "\', \'" + mitglied.geteMailVerteilung() + "\', \'" + mitglied.getMandantID() + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Mitglieder mitglied) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder set mitgliederGruppe = \'" + mitglied.getMitgliederGruppe() + "\', anrede = \'" + mitglied.getAnrede() + "\', name = \'" + mitglied.getName() + "\', vorname = \'" + mitglied.getVorname() + "\', strasse = \'" + mitglied.getStrasse() + "\', ort = \'" + mitglied.getOrt() + "\', telefonPrivat = \'" + mitglied.getTelefonePrivate() + "\', telefonMobil = \'" + mitglied.getTelefonMobile() + "\', telefonArbeit = \'" + mitglied.getTelefonArbeit() + "\', telegrammID = \'" + mitglied.getTelegrammID() + "\', email = \'" + mitglied.getEmail() + "\', email2 = \'" + mitglied.getEmail2() + "\', beruf = \'" + mitglied.getBeruf() + "\', staat = \'" + mitglied.getStaat() + "\', dienstgrad = \'" + mitglied.getDienstgrad() + "\', ausserDienst = \'" + mitglied.getAusserDienst() + "\', mitgliedSeit = \'" + mitglied.getMitgliedSeit() + "\', mitgliedBis = \'" + mitglied.getMitgliedBis() + "\', gebDatum = \'" + mitglied.getGebDatum() + "\', hochzeit = \'" + mitglied.getHochzeit() + "\', kommentar = \'" + mitglied.getKommentar() + "\', fuehrerscheinNummer = \'" + mitglied.getFuehrerscheinNummer() + "\', fahrberechtigungNummer = \'" + mitglied.getFahrberechtigungNummer() + "\', dienstausweisNummer = \'" + mitglied.getDienstausweisNummer() + "\', krankenkasse = \'" + mitglied.getKrankenkasse() + "\', medikamente = \'" + mitglied.getMedikamente() + "\', krankheiten = \'" + mitglied.getKrankheiten() + "\', schwimmabzeichen = \'" + mitglied.getSchwimmabzeichen() + "\', sportabzeichen = \'" + mitglied.getSportabzeichen() + "\', loeschkenner = \'" + mitglied.getLoschkenner() + "\', eMailDeaktiv = \'" + mitglied.geteMailVerteilung() + "\' where id = " + mitglied.getId() + " and mandantID = " + mitglied.getMandantID() + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateAusserDienst(int id, int ausserDienstStatus) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder set ausserDienst = " + ausserDienstStatus + " where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateLoeschkenner(int id, int loeschkennerstatus) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update mitglieder set loeschkenner = " + loeschkennerstatus + " where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getNextFreeNumber() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT (Id+1) AS FreieId FROM mitglieder WHERE (Id + 1) NOT IN (SELECT Id FROM mitglieder) ORDER BY FreieId;");
      ResultSet result = statement.executeQuery("SELECT (Id+1) AS FreieId FROM mitglieder WHERE (Id + 1) NOT IN (SELECT Id FROM mitglieder) ORDER BY FreieId;");
      return result.next()?(result.getInt(1) < 11000?11000:result.getInt(1)):11000;
   }

   public int getMitglierderGruppenCount(int mitgliederGruppenID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `mitglieder` WHERE mitgliederGruppe = " + mitgliederGruppenID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `mitglieder` WHERE mitgliederGruppe = " + mitgliederGruppenID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getDienstgradCount(int dienstgradID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getDienstgradCountMitglieder(int dienstgradID, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `mitglieder` WHERE dienstgrad = " + dienstgradID + " and mitgliederGruppe = " + mitgliederGruppe + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMitgliederCountGruppe1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = 1 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM mitglieder where ausserDienst = 0  and loeschkenner = 0 and mitgliederGruppe = 1 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMitgliederCountEinerGruppe(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = " + mitgliederGruppe + " and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = " + mitgliederGruppe + " and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMitgliederCountMitGeburtstag(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = " + mitgliederGruppe + " and gebDatum != \'\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = " + mitgliederGruppe + " and gebDatum != \'\' and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMitgliederCountAGTGruppe1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAllMitgliederCount() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMitgliederCountByNachnameVorname(String name, String vorname) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder where name = \'" + name + "\' and vorname = \'" + vorname + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM mitglieder where name = \'" + name + "\' and vorname = \'" + vorname + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getMitgliederGruppe1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where ausserDienst = 0  and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname ;");
      logging.logSQL("SELECT name, vorname FROM mitglieder where ausserDienst = 0  and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getMitgliederIDGruppe1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname ;");
      logging.logSQL("SELECT id FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mitgliederGruppe = 1 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAlleTruppUndGruppenfuehrerDerGruppe1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (15,19) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname");
      logging.logSQL("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (15,19) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getAlleMitFührerscheinDerGruppe1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (1,2,3) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname");
      logging.logSQL("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang in (1,2,3) and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getAlleAtemschutztraeger() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname");
      logging.logSQL("SELECT m.name, vorname FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 8 and m.mitgliederGruppe = 1 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getMitgliederEinerGruppeForTrees(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id, name, vorname FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT id, name, vorname FROM mitglieder where and mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add("(" + result.getString(1) + ") " + result.getString(2) + ", " + result.getString(3));
      }

      return liste;
   }

   public ArrayList getMitgliederEinerGruppe(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT name, vorname FROM mitglieder where and mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getMitgliederIDsEinerGruppe(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT id FROM mitglieder where and mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getMitgliederEinerGruppeForSearch(int mitgliederGruppe, String suche) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id, name, vorname FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and (name like \'%" + suche + "%\' or vorname like \'%" + suche + "%\') order by name, vorname;");
      logging.logSQL("SELECT id, name, vorname FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + "  and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and (name like \'%" + suche + "%\' or vorname like \'%" + suche + "%\') order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add("(" + result.getString(1) + ") " + result.getString(2) + ", " + result.getString(3));
      }

      return liste;
   }

   public ArrayList getAllMitgliederFromDataBase() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname ;");
      logging.logSQL("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getAllMitgliederNummernFromDataBase() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname ;");
      logging.logSQL("SELECT id FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAllMitgliederGruppenFromDataBaseByMitglied() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT mitgliederGruppe FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname ;");
      logging.logSQL("SELECT mitgliederGruppe FROM mitglieder where loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by mitgliederGruppe, name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAllMitgliederFromDataBaseWithEMail(String spalte) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and " + spalte + " != \'\' order by mitgliederGruppe, name, vorname;");
      ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and " + spalte + " != \'\' order by mitgliederGruppe, name, vorname ;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public Vector getAllMitgliederForTable(int mitgliederGruppenID, int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.email, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.email, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public Vector getAllMitgliederForTableUebersicht2(int mitgliederGruppenID, int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonArbeit, m.email2, m.mitgliedSeit, m.kommentar FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonArbeit, m.email2, m.mitgliedSeit, m.kommentar FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by name, vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVectorUebersicht2(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public Vector getAllMitgliederForTableTelefonliste(int mitgliederGruppenID, int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonPrivat, m.telefonMobil, m.telefonArbeit FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.telefonPrivat, m.telefonMobil, m.telefonArbeit FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id  where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVectorTelefonliste(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public Vector getAllMitgliederForTableZusatzdaten(int mitgliederGruppenID, int mandantID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.fuehrerscheinNummer, u.ablaufLKW, m.dienstausweisNummer, u.ablaufDienstausweis, m.fahrberechtigungNummer, u. pruefungDerFahrberechtigung FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_untersuchung u ON m.id = u.id where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung as dienstgrad, m.name, m.vorname, m.fuehrerscheinNummer,  u.ablaufLKW, m.dienstausweisNummer, u.ablaufDienstausweis, m.fahrberechtigungNummer, u. pruefungDerFahrberechtigung FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_untersuchung u ON m.id = u.id where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = " + mitgliederGruppenID + " and m.mandantID = " + mandantID + " and d.mandantID = " + mandantID + " order by m.name, m.vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVectorZusatzdaten(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public Vector getAllGeburtstageForTable(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String additionalFilter;
      if(mitgliederGruppe == 0) {
         additionalFilter = "";
      } else {
         additionalFilter = "and m.mitgliederGruppe = " + mitgliederGruppe;
      }

      logging.logSQL("SELECT mg.name as mitgliederGruppe, d.beschreibung as dienstgrad, m.name, m.vorname, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe mg ON mg.id = m.mitgliederGruppe where m.ausserDienst = 0  and m.loeschkenner = 0  " + additionalFilter + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and mg.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and m.gebDatum != \'\' order by SUBSTRING(m.gebDatum,6,10), m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT mg.name as mitgliederGruppe, d.beschreibung as dienstgrad, m.name, m.vorname, m.gebDatum FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe mg ON mg.id = m.mitgliederGruppe where m.ausserDienst = 0  and m.loeschkenner = 0 " + additionalFilter + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and mg.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and m.gebDatum != \'\' order by SUBSTRING(m.gebDatum,6,10), m.name, m.vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToGeburtstagVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getEinheitsführerMail() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 26 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 26 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(!result.getString(1).equals("")) {
            liste.add(result.getString(1));
         }
      }

      return liste;
   }

   public ArrayList getGeraetewarteMail() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 27 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT m.email FROM mitglieder m LEFT JOIN mitglieder_laufbahn l ON m.id = l.mitgliederID where l.lehrgang = 27 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(!result.getString(1).equals("")) {
            liste.add(result.getString(1));
         }
      }

      return liste;
   }

   public ArrayList getAlleMailAdressenGruppe1() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT email FROM mitglieder where mitgliederGruppe = 1 and loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT email FROM mitglieder where mitgliederGruppe = 1 and loeschkenner = 0 and eMailDeaktiv = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         if(!result.getString(1).equals("")) {
            liste.add(result.getString(1));
         }
      }

      return liste;
   }

   public ArrayList getVCardExport(int mitgliederGruppe, String seperator) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.vorname, m.name, a.name, m.gebDatum, m.kommentar, m.email, m.email2, m.telefonPrivat, m.telefonMobil, m.strasse, m.ort, m.telefonArbeit, g.name FROM mitglieder m LEFT JOIN mitglieder_anrede a ON m.anrede = a.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id where m.mitgliederGruppe = " + mitgliederGruppe + " and loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and g.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      logging.logSQL("SELECT m.vorname, m.name, a.name, m.gebDatum, m.kommentar, m.email, m.email2, m.telefonPrivat, m.telefonMobil, m.strasse, m.ort, m.telefonArbeit, g.name FROM mitglieder m LEFT JOIN mitglieder_anrede a ON m.anrede = a.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id where m.mitgliederGruppe = " + mitgliederGruppe + " and loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and g.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + seperator + seperator + result.getString(2) + seperator + result.getString(3) + seperator + seperator + seperator + seperator + seperator + result.getString(4) + seperator + seperator + seperator + seperator + seperator + result.getString(5) + seperator + result.getString(6) + seperator + result.getString(7) + seperator + seperator + seperator + result.getString(8) + seperator + seperator + result.getString(9) + seperator + seperator + seperator + seperator + result.getString(10) + seperator + seperator + seperator + seperator + result.getString(11) + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + result.getString(12) + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + seperator + result.getString(13));
      }

      return liste;
   }

   public ArrayList getVerfuegbareMaschinistenKlasseC(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseC = 1 and m.ausserDienst = 0  and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareMaschinistenKlasseCuB(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseB = 1 or l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.maschi = 1 and l.klasseB = 1 or l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareTruppfuehrer(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.TF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.TF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0  and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareGruppenfuehrer(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.gf = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.gf = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareZugfuehrer(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.gf = 1 or l.zf = 1) and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.gf = 1 or l.zf = 1) and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareAtemschtztraeger(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.agt = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.agt = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareMannschaft(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.tm1 = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.tm1 = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareRettunsdienstPersonal(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and m.mitgliederGruppe = 1 and (l.rh = 1 or l.rs = 1 or l.ra = 1) order by m.name, m.vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and m.mitgliederGruppe = 1 and (l.rh = 1 or l.rs = 1 or l.ra = 1) order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareRTWFahrerKlasseC(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareRTWFahrerKlasseB(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseB = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.rh = 1 or l.rs = 1 or l.ra = 1) and l.klasseB = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareDLKFahrer(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.dlkmaschi = 1 or l.korbsteuerung = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and (l.dlkmaschi = 1 or l.korbsteuerung = 1) and l.klasseC = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getVerfuegbareDLKGF(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.dlkmaschi = 1 and l.GF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID LEFT JOIN fahrzeugeinteilung_temp l ON m.id = l.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and l.dlkmaschi = 1 and l.GF = 1 and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mitgliederGruppe = 1 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public HashMap getAllMitgliederData(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.id, g.name as mitgliederGruppe, a.name as anrede, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.telefonArbeit, m.telegrammID, m.email, m.email2, m.beruf, m.staat, m.gebDatum, m.hochzeit, d.beschreibungLang as dienstgrad, m.ausserDienst, m.mitgliedSeit, m.mitgliedBis, m.kommentar, m.fuehrerscheinNummer, m.fahrberechtigungNummer, m.dienstausweisNummer, m.eMailDeaktiv, m.krankenkasse, m.medikamente, m.krankheiten, m.schwimmabzeichen, m.sportabzeichen FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id LEFT JOIN mitglieder_anrede a ON m.anrede = a.id where m.id = " + mitgliedID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT m.id, g.name as mitgliederGruppe, a.name as anrede, m.name, m.vorname, m.strasse, m.ort, m.telefonPrivat, m.telefonMobil, m.telefonArbeit, m.telegrammID, m.email, m.email2, m.beruf, m.staat, m.gebDatum, m.hochzeit, d.beschreibungLang as dienstgrad, m.ausserDienst, m.mitgliedSeit, m.mitgliedBis, m.kommentar, m.fuehrerscheinNummer, m.fahrberechtigungNummer, m.dienstausweisNummer, m.eMailDeaktiv, m.krankenkasse, m.medikamente, m.krankheiten, m.schwimmabzeichen, m.sportabzeichen FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN mitglieder_gruppe g ON m.mitgliederGruppe = g.id LEFT JOIN mitglieder_anrede a ON m.anrede = a.id where m.id = " + mitgliedID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put("id", Integer.toString(result.getInt("id")));
         map.put("mitgliederGruppe", result.getString("mitgliederGruppe"));
         map.put("anrede", result.getString("anrede"));
         map.put("name", result.getString("name"));
         map.put("vorname", result.getString("vorname"));
         map.put("strasse", result.getString("strasse"));
         map.put("ort", result.getString("ort"));
         map.put("telefonPrivat", result.getString("telefonPrivat"));
         map.put("telefonMobil", result.getString("telefonMobil"));
         map.put("telefonArbeit", result.getString("telefonArbeit"));
         map.put("telegrammID", result.getString("telegrammID"));
         map.put("email", result.getString("email"));
         map.put("email2", result.getString("email2"));
         map.put("beruf", result.getString("beruf"));
         map.put("staat", result.getString("staat"));
         map.put("geburtsdatum", result.getString("gebDatum"));
         map.put("hochzeit", result.getString("hochzeit"));
         map.put("dienstgrad", result.getString("dienstgrad"));
         map.put("ausserDienst", Integer.toString(result.getInt("ausserDienst")));
         map.put("mitgliedSeit", result.getString("mitgliedSeit"));
         map.put("mitgliedBis", result.getString("mitgliedBis"));
         map.put("kommentar", result.getString("kommentar"));
         map.put("fuehrerscheinNummer", result.getString("fuehrerscheinNummer"));
         map.put("fahrberechtigungNummer", result.getString("fahrberechtigungNummer"));
         map.put("dienstausweisNummer", result.getString("dienstausweisNummer"));
         map.put("krankenkasse", result.getString("krankenkasse"));
         map.put("medikamente", result.getString("medikamente"));
         map.put("krankheiten", result.getString("krankheiten"));
         map.put("schwimmabzeichen", result.getString("schwimmabzeichen"));
         map.put("sportabzeichen", result.getString("sportabzeichen"));
         map.put("eMailDeaktiv", result.getString("eMailDeaktiv"));
      }

      return map;
   }

   public HashMap getMitgliederListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT id, name, vorname FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT id, name, vorname FROM mitglieder where ausserDienst = 0 and loeschkenner = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      HashMap map = new HashMap();

      while(result.next()) {
         map.put(Integer.valueOf(result.getInt(1)), result.getString(2) + ", " + result.getString(3));
      }

      return map;
   }

   public int getId(String name, String vorname) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where name = \'" + name + "\' and vorname = \'" + vorname + "\';");
      logging.logSQL("SELECT id FROM mitglieder where name = \'" + name + "\' and vorname = \'" + vorname + "\';");
      return result.next()?result.getInt(1):0;
   }

   public int getIdByGuiString(String guiString) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      int komma = guiString.indexOf(",");
      String isSelectedName = guiString.substring(0, komma);
      String isSelectedVorname = guiString.substring(komma + 2, guiString.length());
      ResultSet result = statement.executeQuery("SELECT id FROM mitglieder where name = \'" + isSelectedName + "\' and vorname = \'" + isSelectedVorname + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM mitglieder where name = \'" + isSelectedName + "\' and vorname = \'" + isSelectedVorname + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getEinsatzleiter(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON d.id = m.dienstgrad where m.id = " + id + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON d.id = m.dienstgrad where m.id = " + id + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?(((String)runApplication.EINSTELLUNGEN.get("EinsatzBerichtEinsatzleiterMitDienstgrad")).equals("1")?result.getString(1) + " " + result.getString(2) + ", " + result.getString(3):result.getString(2) + ", " + result.getString(3)):null;
   }

   public int getAnrede(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT anrede FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT anrede FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getName(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getNameVornameByID(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT name, vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT name, vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1) + ", " + result.getString(2):null;
   }

   public String getVorname(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT vorname FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getStrasse(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT strasse FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT strasse FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getOrt(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ort FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ort FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getTelefonPrivat(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT telefonPrivat FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT telefonPrivat FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getTelefonMobil(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT telefonMobil FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT telefonMobil FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getTelefonArbeit(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT telefonArbeit FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT telefonArbeit FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getTelegrammID(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT telegrammID FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT telegrammID FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getEMail(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT email FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT email FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public ArrayList getAlleEMailEinerMitgliederGruppe(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT email FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + " and email != \'\' and eMailDeaktiv = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT email FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + " and email != \'\' and eMailDeaktiv = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public String getEMail2(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT email2 FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT email2 FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getGebDatum(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT gebDatum FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT gebDatum FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public String getBeruf(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT beruf FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT beruf FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getSummiertesGebJahr(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT sum(SUBSTRING(gebDatum,1,4)) FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + " and gebDatum != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT sum(SUBSTRING(gebDatum,1,4)) FROM mitglieder where mitgliederGruppe = " + mitgliederGruppe + " and gebDatum != \'\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getGebDatumForInformationService(int modus) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = null;
      if(modus == 1) {
         logging.logSQL("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != \'\' and gebdatum like \'%" + SbcUtils.timeStamp("MM-dd") + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname");
         result = statement.executeQuery("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != \'\' and gebdatum like \'%" + SbcUtils.timeStamp("MM-dd") + "\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by name, vorname");
      } else if(modus == 2) {
         logging.logSQL("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != \'\' and gebdatum like \'%-" + SbcUtils.timeStamp("MM") + "-%\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by SUBSTRING(gebDatum,6,8), name, vorname");
         result = statement.executeQuery("SELECT name, vorname, gebdatum FROM mitglieder where gebdatum != \'\' and gebdatum like \'%-" + SbcUtils.timeStamp("MM") + "-%\' and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by SUBSTRING(gebDatum,6,8), name, vorname");
      }

      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2) + " " + TimeCalculation.parseDateForGUI(result.getString(3)));
      }

      return liste;
   }

   public String getDienstgradLangText(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibungLang FROM mitglieder m LEFT JOIN dienstgrad d on d.id= m.dienstgrad where m.id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT d.beschreibungLang FROM mitglieder m LEFT JOIN dienstgrad d on d.id= m.dienstgrad where m.id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getDienstgradID(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT dienstgrad from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT dienstgrad from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public String getMitgliedSeit(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT mitgliedSeit from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT mitgliedSeit from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      if(result.next()) {
         System.out.println("Mitglied seit (Jahr)--> " + result.getString(1).toString().substring(0, 4));
         return result.getString(1).toString().substring(0, 4);
      } else {
         return null;
      }
   }

   public String getMitgliedKommentar(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT kommentar from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT kommentar from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getString(1):null;
   }

   public int getMitgliederGruppe(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT mitgliederGruppe from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT mitgliederGruppe from mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getAusserDienstStatus(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT ausserDienst FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT ausserDienst FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getEMailDeaktivStatus(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT eMailDeaktiv FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT eMailDeaktiv FROM mitglieder where id = " + mitgliederID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getKrankenkasseListe() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT krankenkasse FROM mitglieder where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by krankenkasse order by krankenkasse;");
      logging.logSQL("SELECT krankenkasse FROM mitglieder where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by krankenkasse order by krankenkasse;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(result.getString("dienstgrad"));
      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add(result.getString("strasse"));
      mitgliederListe.add(result.getString("ort"));
      mitgliederListe.add(result.getString("telefonPrivat"));
      mitgliederListe.add(result.getString("telefonMobil"));
      mitgliederListe.add(result.getString("email"));
      mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("gebDatum")));
      return mitgliederListe;
   }

   private Vector mapResultSetToVectorUebersicht2(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(result.getString("dienstgrad"));
      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add(result.getString("telefonArbeit"));
      mitgliederListe.add(result.getString("email2"));
      if(((String)runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat")).equals("yyyy")) {
         mitgliederListe.add(result.getString("mitgliedSeit").substring(0, 4));
      } else if(((String)runApplication.EINSTELLUNGEN.get("mitgliedSeitFormat")).equals("dd.MM.yyyy")) {
         if(result.getString("mitgliedSeit").toString().length() == 4) {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("mitgliedSeit") + "-01-01"));
         } else {
            mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("mitgliedSeit")));
         }
      }

      mitgliederListe.add(result.getString("kommentar"));
      return mitgliederListe;
   }

   private Vector mapResultSetToVectorTelefonliste(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(result.getString("dienstgrad"));
      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add(result.getString("telefonPrivat"));
      mitgliederListe.add(result.getString("telefonMobil"));
      mitgliederListe.add(result.getString("telefonArbeit"));
      return mitgliederListe;
   }

   private Vector mapResultSetToVectorZusatzdaten(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(result.getString("dienstgrad"));
      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add(result.getString("fuehrerscheinNummer"));
      mitgliederListe.add("D--!" + result.getString("ablaufLKW") + ";" + (String)runApplication.EINSTELLUNGEN.get("untersuchungVorwarnung"));
      mitgliederListe.add(result.getString("dienstausweisNummer"));
      mitgliederListe.add("D--!" + result.getString("ablaufDienstausweis") + ";" + (String)runApplication.EINSTELLUNGEN.get("vorwarnungAblaufDienstausweis"));
      mitgliederListe.add(result.getString("fahrberechtigungNummer"));
      mitgliederListe.add("D--!" + result.getString("pruefungDerFahrberechtigung") + ";" + (String)runApplication.EINSTELLUNGEN.get("vorwarnungAblaufFahrberechtigung"));
      return mitgliederListe;
   }

   private Vector mapResultSetToGeburtstagVector(ResultSet result) throws SQLException {
      Vector mitgliederListe = new Vector();
      mitgliederListe.add(result.getString("mitgliederGruppe"));
      if(((String)runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste")).equals("1")) {
         mitgliederListe.add(result.getString("dienstgrad"));
      }

      mitgliederListe.add(result.getString("name"));
      mitgliederListe.add(result.getString("vorname"));
      mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("gebDatum")));
      return mitgliederListe;
   }
}
