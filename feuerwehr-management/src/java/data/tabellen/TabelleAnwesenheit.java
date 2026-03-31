package data.tabellen;

import ao.listen.AnwesenheitListeOptionenAO;
import data.DatenbankZugriff;
import data.tabellen.mitglied.TabelleMitglied;
import go.Anwesenheit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;
import utilities.Utils;

public class TabelleAnwesenheit {

   public void insert(Anwesenheit anwesenheit) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO anwesenheit (`id`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `mitgliederID` , `mandantID`) VALUES (\'" + anwesenheit.getId() + "\', \'" + anwesenheit.getJahr() + "\', \'" + anwesenheit.getVeranstaltungID() + "\', \'" + anwesenheit.getVeranstaltungKategorie() + "\', \'" + anwesenheit.getMitgliederID() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void insertArray(Anwesenheit[] anwesenheit) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      StringBuilder build = new StringBuilder();

      for(int sql = 0; sql < anwesenheit.length; ++sql) {
         build.append("(");
         build.append(anwesenheit[sql].getId());
         build.append(",");
         build.append(anwesenheit[sql].getJahr());
         build.append(",");
         build.append(anwesenheit[sql].getVeranstaltungID());
         build.append(",");
         build.append(anwesenheit[sql].getVeranstaltungKategorie());
         build.append(",");
         build.append(anwesenheit[sql].getMitgliederID());
         build.append(",");
         build.append((String)runApplication.PROPERTIES.get("MandantID"));
         if(sql != anwesenheit.length - 1) {
            build.append("),");
         } else {
            build.append(");");
         }
      }

      String var5 = "INSERT INTO anwesenheit (`id`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `mitgliederID` , `mandantID`) VALUES " + build.toString();
      logging.logSQL(var5);
      statement.executeUpdate(var5);
   }

   public void deleteOne(int mitgliederID, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from anwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public void delete(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "delete from anwesenheit where veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      statement.executeUpdate(sql);
   }

   public ArrayList getNichtAnwesendeMitglieder(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("Select m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id != a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("Select m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id != a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " group by m.name, m.vorname order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getNichtInFahrzeugeinteilung(int veranstaltungID, String[] mitgliederIDs) throws SQLException, StringIndexOutOfBoundsException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      StringBuilder build = new StringBuilder();

      for(int result = 0; result < mitgliederIDs.length; ++result) {
         build.append(mitgliederIDs[result]);
         build.append(",");
      }

      logging.logSQL("SELECT m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID not in (" + build.substring(0, build.length() - 1) + ") and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet var7 = statement.executeQuery("SELECT m.name, m.vorname from anwesenheit a LEFT JOIN mitglieder m ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID not in (" + build.substring(0, build.length() - 1) + ") and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ArrayList liste = new ArrayList();

      while(var7.next()) {
         liste.add(var7.getString(1) + ", " + var7.getString(2));
      }

      return liste;
   }

   public int getAnwesendStatus(int mitgliederID, int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM anwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getNextNummer() throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT max(id) FROM anwesenheit;");
      ResultSet result = statement.executeQuery("SELECT max(id) FROM anwesenheit;");
      return result.next()?result.getInt(1) + 1:0;
   }

   public int getGesamtBeteiligung(int mitgliederID, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      return result.next()?result.getInt(1):0;
   }

   public int getGesamtVeranstaltung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `anwesenheit` WHERE `veranstaltungID` = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `veranstaltungID` = " + veranstaltungID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      return result.next()?result.getInt(1):0;
   }

   public int getGesamtVeranstaltungByKategorie(int kategorieID, int jahr, int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      StringBuilder build = new StringBuilder();
      int[] mitgliederIDListe = Utils.listToIntArray((new TabelleMitglied()).getMitgliederIDsEinerGruppe(mitgliederGruppe));

      for(int result = 0; result < mitgliederIDListe.length; ++result) {
         build.append(mitgliederIDListe[result]);
         if(result != mitgliederIDListe.length - 1) {
            build.append(",");
         }
      }

      logging.logSQL("SELECT count(*) FROM `anwesenheit` WHERE mitgliederID in (" + build.toString() + ") and veranstaltungKategorie = " + kategorieID + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet var8 = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE mitgliederID in (" + build.toString() + ") and veranstaltungKategorie = " + kategorieID + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      if(var8.next()) {
         return var8.getInt(1);
      } else {
         return 0;
      }
   }

   public int getBeteiligungByKategorie(int mitgliederID, int veranstaltungKategorie, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie = " + veranstaltungKategorie + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie = " + veranstaltungKategorie + " and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      return result.next()?result.getInt(1):0;
   }

   public int getBeteiligungByKategorieGroesser3(int mitgliederID, int jahr) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie > 3 and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie > 3 and jahr = " + jahr + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      return result.next()?result.getInt(1):0;
   }

   public int getBeteiligungEinsatzDienst(int mitgliederID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie IN (1,2) and jahr = " + SbcUtils.timeStamp("yyyy") + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM `anwesenheit` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungKategorie IN (1,2) and jahr = " + SbcUtils.timeStamp("yyyy") + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      return result.next()?result.getInt(1):0;
   }

   public Vector getAllDataForList(int mitgliederGruppe) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = " + mitgliederGruppe + " and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = " + mitgliederGruppe + " and m.ausserDienst = 0 and m.loeschkenner = 0 and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public Vector getFilterDataForList(String[] selektierteMitglieder, int mitgliederGruppe) throws SQLException {
      StringBuilder build = new StringBuilder();
      build.append("(");

      for(int statement = 0; statement < selektierteMitglieder.length; ++statement) {
         build.append(selektierteMitglieder[statement]);
         if(statement != selektierteMitglieder.length - 1) {
            build.append(",");
         }
      }

      build.append(")");
      Statement var7 = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.id in " + build.toString() + " and m.mitgliederGruppe = " + mitgliederGruppe + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      ResultSet result = var7.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM mitglieder m LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.id in " + build.toString() + " and m.mitgliederGruppe = " + mitgliederGruppe + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector2(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public Vector getAnwesendeMitgliederEinerVeranstaltung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT d.beschreibung, m.name, m.vorname FROM anwesenheit a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN dienstgrad d ON d.id = m.dienstgrad WHERE veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      ResultSet result = statement.executeQuery("SELECT d.beschreibung, m.name, m.vorname FROM anwesenheit a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN dienstgrad d ON d.id = m.dienstgrad WHERE veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " ;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector2(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   public ArrayList getAnwesendeMitgliederByVeranstaltung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getAnwesendeMitgliederIDByVeranstaltung(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.id FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.id FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(Integer.valueOf(result.getInt(1)));
      }

      return liste;
   }

   public ArrayList getAnwesendeMitglieder(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1) + ", " + result.getString(2));
      }

      return liste;
   }

   public ArrayList getAnwesendeMitgliederEMail(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT m.email FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.eMailDeaktiv = 0 and eMail != \'\' and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ResultSet result = statement.executeQuery("SELECT m.email FROM mitglieder m LEFT JOIN anwesenheit a ON m.id = a.mitgliederID where a.veranstaltungID = " + veranstaltungID + " and m.eMailDeaktiv = 0 and eMail != \'\' and m.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + "  order by m.name, m.vorname;");
      ArrayList liste = new ArrayList();

      while(result.next()) {
         liste.add(result.getString(1));
      }

      return liste;
   }

   public int getZFCountMitVeranstaltungsID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 20 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 20 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getGFCountMitVeranstaltungsID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 19 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 19 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public int getFMCountMitVeranstaltungsID(int veranstaltungID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 6 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM anwesenheit a LEFT JOIN mitglieder_laufbahn l ON a.mitgliederID = l.mitgliederID LEFT JOIN mitglieder m ON a.mitgliederID = m.id where l.lehrgang = 6 and a.veranstaltungID = " + veranstaltungID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public Vector getAlleAnwesendeVeranstaltungenProMitglied(int mitgliedID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT v.datum, v.zeit, v.zeitEnde, v.name2 FROM anwesenheit a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id WHERE a.mitgliederID = " + mitgliedID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;");
      ResultSet result = statement.executeQuery("SELECT v.datum, v.zeit, v.zeitEnde, v.name2 FROM anwesenheit a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id WHERE a.mitgliederID = " + mitgliedID + " and a.mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;");
      Vector liste = new Vector();

      while(result.next()) {
         liste.add(this.mapResultSetToVector3(result));
      }

      logging.logSQL(liste);
      return liste;
   }

   private Vector mapResultSetToVector(ResultSet result) throws SQLException {
      Vector anwesenheitListe = new Vector();
      if(((String)runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste")).equals("1")) {
         anwesenheitListe.add(result.getString("beschreibung"));
      }

      anwesenheitListe.add(result.getString("name"));
      anwesenheitListe.add(result.getString("vorname"));
      anwesenheitListe.add(" ");
      return anwesenheitListe;
   }

   private Vector mapResultSetToVector2(ResultSet result) throws SQLException {
      Vector anwesenheitListe = new Vector();
      if(((String)runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste")).equals("1")) {
         anwesenheitListe.add(result.getString("beschreibung"));
      }

      anwesenheitListe.add(result.getString("name"));
      anwesenheitListe.add(result.getString("vorname"));
      return anwesenheitListe;
   }

   public Vector createHeadnameForFilter() {
      Vector headname = new Vector();
      if(((String)runApplication.EINSTELLUNGEN.get("dienstgradAufAnwesenheitsliste")).equals("1")) {
         headname.add("Dienstgrad");
      }

      headname.add("Name");
      headname.add("Vorname");
      if(AnwesenheitListeOptionenAO.zusatzBox1.isSelected()) {
         headname.add(AnwesenheitListeOptionenAO.zusatzFeld1.getText());
      }

      if(AnwesenheitListeOptionenAO.zusatzBox2.isSelected()) {
         headname.add(AnwesenheitListeOptionenAO.zusatzFeld2.getText());
      }

      if(AnwesenheitListeOptionenAO.zusatzBox3.isSelected()) {
         headname.add(AnwesenheitListeOptionenAO.zusatzFeld3.getText());
      }

      return headname;
   }

   private Vector mapResultSetToVector3(ResultSet result) throws SQLException {
      Vector anwesenheitListe = new Vector();
      anwesenheitListe.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
      anwesenheitListe.add(result.getString("zeit"));
      anwesenheitListe.add(result.getString("zeitEnde"));
      anwesenheitListe.add(result.getString("name2"));
      return anwesenheitListe;
   }
}
