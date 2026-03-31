package data.tabellen.abrechnung;

import data.DatenbankZugriff;
import go.abrechnung.Abrechnung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.MoneyCalculation;

public class TabelleAbrechnung {

   public void insert(Abrechnung abrechnung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO abrechnung (`id`,`abrechnungID`, `artikelID`,`buchungskonto`,`zahlungsart`,`mitgliederID`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `menge`, `wert`, `datum`,`status`,`umbuchungID` , `mandantID`) VALUES (\'" + abrechnung.getId() + "\', \'" + abrechnung.getAbrechnungID() + "\', \'" + abrechnung.getArtikelID() + "\', \'" + abrechnung.getBuchungskonto() + "\', \'" + abrechnung.getZahlungsart() + "\', \'" + abrechnung.getMitgliedID() + "\', \'" + abrechnung.getJahr() + "\', \'" + abrechnung.getVeranstaltungID() + "\', \'" + abrechnung.getVeranstaltungKategorie() + "\', \'" + abrechnung.getMenge() + "\', \'" + abrechnung.getWert() + "\', \'" + abrechnung.getDatum() + "\', \'" + abrechnung.getStatus() + "\', \'" + abrechnung.getUmbuchungID() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateUmbuchungID(int id, int neuUmbuchungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "update abrechnung set umbuchungID = " + neuUmbuchungID + " where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from abrechnung where veranstaltungID = " + veranstaltungID + " and abrechnungID = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void updateOffeneVorgaenge(int abrechnungID, int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update abrechnung SET status = 1, abrechnungID = " + abrechnungID + " where status = 0 and abrechnungID = 0 and id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getAbrechnugID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(abrechnungID) FROM abrechnung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT max(abrechnungID) FROM abrechnung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?(result.getInt(1) <= 999999?1000000:result.getInt(1) + 1):0;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT max(id) FROM abrechnung;");
      logging.logSQL("SELECT max(id) FROM abrechnung;");
      return result.next()?(result.getInt(1) <= '\uc34f'?500000:result.getInt(1) + 1):0;
   }

   public int getVeranstaltungsCount(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getVeranstaltungsCountMitAbrechnung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT count(*) FROM abrechnung where veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getSum(String kontoname, int zahlungsart) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.zahlungsart = " + zahlungsart + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.zahlungsart = " + zahlungsart + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getSumWithStatus(String kontoname, int zahlungsart, int status) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.zahlungsart = " + zahlungsart + " and a.status = " + status + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT sum(a.wert) FROM abrechnung a LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.zahlungsart = " + zahlungsart + " and a.status = " + status + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getStatus(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT status FROM abrechnung where id  = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT status FROM abrechnung where id  = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getWertByID(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT wert FROM abrechnung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT wert FROM abrechnung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getMengeByID(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT menge FROM abrechnung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT menge FROM abrechnung where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public ArrayList getIDsByVeranstaltungForUmbuchung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getIDsByVeranstaltungForUmbuchung2(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID = 0 and zahlungsart = 3 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT id FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID = 0 and zahlungsart = 3 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getMitgliederIDByVeranstaltungID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT mitgliederID FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT mitgliederID FROM abrechnung WHERE veranstaltungID = " + veranstaltungID + " and abrechnungID != 0 and (zahlungsart = 1 OR zahlungsart = 2) and umbuchungID = 0 and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getDataForPDF(int abrechnungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;");
      logging.logSQL("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;");

      ArrayList liste;
      String zahlart;
      String veranstaltung;
      String name;
      for(liste = new ArrayList(); result.next(); liste.add(result.getString(1) + "     " + result.getString(2) + "     " + veranstaltung + "\n" + name + "     " + "\n" + zahlart + ":                                                                                           Menge: " + result.getString(6) + "    " + MoneyCalculation.parseMoneyVauleForGUI(result.getInt(7)) + "€")) {
         if(result.getString(8).equals("1")) {
            zahlart = "Einzahlung";
         } else if(result.getString(8).equals("2")) {
            zahlart = "Auszahlung";
         } else {
            zahlart = "Umbuchung";
         }

         if(result.getString(3) == null) {
            veranstaltung = "";
         } else {
            veranstaltung = result.getString(3);
         }

         if(result.getString(4) == null && result.getString(5) == null) {
            if(result.getString(8).equals("1")) {
               name = "EINZAHLUNG";
            } else {
               name = "AUSZAHLUNG";
            }
         } else {
            name = result.getString(4) + ", " + result.getString(5);
         }
      }

      return liste;
   }

   public ArrayList getAllAbrechnungID() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT abrechnungID FROM abrechnung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by abrechnungID order by abrechnungID desc ;");
      logging.logSQL("SELECT abrechnungID FROM abrechnung where mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by abrechnungID order by abrechnungID desc ;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public Vector getAllAbrechnungenByMitglied(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, k.name as veranstaltungKategorie, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliederID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      ResultSet result = statement.executeQuery("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, k.name as veranstaltungKategorie, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliederID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getIDArrayMitglieder(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliedID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      logging.logSQL("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where m.id = " + mitgliedID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public Vector getAllAbrechnungenByKonto(String kontoname) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname, a.artikelID, aa.name as artikelname, v.name as veranstaltungName, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, a.artikelID, aa.name as artikelname, v.name as veranstaltungName, a.menge, a.wert, a.zahlungsart, a.status, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultToKontoVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getIDArrayKonto(String kontoname) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      logging.logSQL("SELECT a.id FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID =  aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where konto.name = \'" + kontoname + "\' and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by a.status asc, v.datum, v.zeit;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public Vector getAllAbrechnungenByAbrechnung(int abrechnungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;");
      ResultSet result = statement.executeQuery("SELECT a.artikelID, aa.name as artikelname, v.name as veranstaltungName, m.name, m.vorname, a.menge, a.wert, a.zahlungsart, konto.name as buchungskonto FROM abrechnung a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN abrechnung_artikelklassen k ON a.veranstaltungKategorie = k.id LEFT JOIN abrechnung_artikel aa ON a.artikelID = aa.id LEFT JOIN abrechnung_konto konto ON a.buchungskonto = konto.id where a.abrechnungID = " + abrechnungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and aa.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and k.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and konto.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by v.datum, v.zeit, a.status asc;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultToAbrechnungenVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector artikelListe = new Vector();
      artikelListe.add(result.getString("artikelID"));
      artikelListe.add(result.getString("artikelName"));
      artikelListe.add(result.getString("veranstaltungName"));
      artikelListe.add(result.getString("veranstaltungKategorie"));
      artikelListe.add(result.getString("menge"));
      artikelListe.add(MoneyCalculation.parseMoneyVauleForGUI(Integer.parseInt(result.getString("wert"))) + "€");
      if(result.getString("zahlungsart").equals("1")) {
         artikelListe.add("Einzahlung (" + result.getString("buchungskonto") + ")");
      } else if(result.getString("zahlungsart").equals("2")) {
         artikelListe.add("Auszahlung (" + result.getString("buchungskonto") + ")");
      } else {
         artikelListe.add("Umbuchung (" + result.getString("buchungskonto") + ")");
      }

      if(result.getString("status").equals("0")) {
         artikelListe.add("offen");
      } else if(result.getString("status").equals("1")) {
         artikelListe.add("abgerechnet");
      }

      return artikelListe;
   }

   private Vector mapResultToKontoVector(ResultSet result) throws SQLException {
      Vector artikelListe = new Vector();
      if(result.getString("name") == null && result.getString("vorname") == null) {
         if(result.getString("zahlungsart").equals("1")) {
            artikelListe.add("EINZAHLUNG");
         } else if(result.getString("zahlungsart").equals("2")) {
            artikelListe.add("AUSZAHLUNG");
         } else {
            artikelListe.add("UMBUCHUNG");
         }
      } else {
         artikelListe.add(result.getString("name") + ", " + result.getString("vorname"));
      }

      artikelListe.add(result.getString("artikelID"));
      artikelListe.add(result.getString("artikelName"));
      artikelListe.add(result.getString("veranstaltungName"));
      artikelListe.add(result.getString("menge"));
      artikelListe.add(MoneyCalculation.parseMoneyVauleForGUI(Integer.parseInt(result.getString("wert"))) + "€");
      if(result.getString("zahlungsart").equals("1")) {
         artikelListe.add("Einzahlung");
      } else if(result.getString("zahlungsart").equals("2")) {
         artikelListe.add("Auszahlung");
      } else {
         artikelListe.add("Umbuchung");
      }

      if(result.getString("status").equals("0")) {
         artikelListe.add("offen");
      } else if(result.getString("status").equals("1")) {
         artikelListe.add("abgerechnet");
      }

      return artikelListe;
   }

   private Vector mapResultToAbrechnungenVector(ResultSet result) throws SQLException {
      Vector artikelListe = new Vector();
      if(result.getString("name") == null && result.getString("vorname") == null) {
         if(result.getString("zahlungsart").equals("1")) {
            artikelListe.add("EINZAHLUNG");
         } else {
            artikelListe.add("AUSZAHLUNG");
         }
      } else {
         artikelListe.add(result.getString("name") + ", " + result.getString("vorname"));
      }

      artikelListe.add(result.getString("artikelID"));
      artikelListe.add(result.getString("artikelName"));
      artikelListe.add(result.getString("veranstaltungName"));
      artikelListe.add(result.getString("menge"));
      artikelListe.add(MoneyCalculation.parseMoneyVauleForGUI(Integer.parseInt(result.getString("wert"))) + "€");
      if(result.getString("zahlungsart").equals("1")) {
         artikelListe.add("Einzahlung (" + result.getString("buchungskonto") + ")");
      } else if(result.getString("zahlungsart").equals("2")) {
         artikelListe.add("Auszahlung (" + result.getString("buchungskonto") + ")");
      } else {
         artikelListe.add("Umbuchung");
      }

      return artikelListe;
   }
}
