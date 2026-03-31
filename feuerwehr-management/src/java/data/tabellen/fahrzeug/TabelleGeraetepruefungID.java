package data.tabellen.fahrzeug;

import data.DatenbankZugriff;
import go.Geraetepruefung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleGeraetepruefungID {

   public void insert(Geraetepruefung untersuchung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "INSERT INTO geraetepruefungID (`id`, `stromerzeuger`, `steckleiter`, `multileiter`, `schiebleiter`, `hydraulik`, `pumpe`, `kettensaege`, `kettensaege2`, `schnittschutz`, `schnittschutz2`, `trennschleifer`, `elektrosaege`, `doppelkanister`, `geraetepruefung_allgem`, `abstusiset`, `rollgliss`, `verbandsmaterial`, `tauchpumpe`, `tauchpumpe2`, `hebekissen`, `luefter`, `luefter2`, `pa1`, `pa2`, `pa3`, `pa4`, `pa5`, `pa6`, `infoEMail`, `mandantID`) VALUES (\'" + untersuchung.getId() + "\', \'" + untersuchung.getStromerzeuger() + "\', \'" + untersuchung.getSteckleiter() + "\', \'" + untersuchung.getSchiebleiter() + "\', \'" + untersuchung.getMultileiter() + "\', \'" + untersuchung.getHydraulik() + "\', \'" + untersuchung.getPumpe() + "\', \'" + untersuchung.getKettensaege() + "\', \'" + untersuchung.getKettensaege2() + "\', \'" + untersuchung.getSchnittschutzkleidung() + "\', \'" + untersuchung.getSchnittschutzkleidung2() + "\', \'" + untersuchung.getTrennschleifer() + "\', \'" + untersuchung.getElektrosaege() + "\', \'" + untersuchung.getDoppelkanister() + "\', \'" + untersuchung.getGeraetepruefung_allgm() + "\', \'" + untersuchung.getAbstusiset() + "\', \'" + untersuchung.getRollgliss() + "\', \'" + untersuchung.getVerbandsmaterial() + "\', \'" + untersuchung.getTauchpumpe() + "\', \'" + untersuchung.getTauchpumpe2() + "\', \'" + untersuchung.getHebekissen() + "\', \'" + untersuchung.getLüfter() + "\', \'" + untersuchung.getLüfter2() + "\', \'" + untersuchung.getPa1() + "\', \'" + untersuchung.getPa2() + "\', \'" + untersuchung.getPa3() + "\', \'" + untersuchung.getPa4() + "\', \'" + untersuchung.getPa5() + "\', \'" + untersuchung.getPa6() + "\', \'" + untersuchung.getInfoEMail() + "\', \'" + (String)runApplication.PROPERTIES.get("MandantID") + "\');";
      statement.executeUpdate(sql);
   }

   public void update(Geraetepruefung untersuchung) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      String sql = "Update geraetepruefungID set stromerzeuger = \'" + untersuchung.getStromerzeuger() + "\', steckleiter = \'" + untersuchung.getSteckleiter() + "\', schiebleiter = \'" + untersuchung.getSchiebleiter() + "\', multileiter = \'" + untersuchung.getMultileiter() + "\', hydraulik = \'" + untersuchung.getHydraulik() + "\', pumpe = \'" + untersuchung.getPumpe() + "\', kettensaege = \'" + untersuchung.getKettensaege() + "\', kettensaege2 = \'" + untersuchung.getKettensaege2() + "\', schnittschutz = \'" + untersuchung.getSchnittschutzkleidung() + "\', schnittschutz2 = \'" + untersuchung.getSchnittschutzkleidung2() + "\', trennschleifer = \'" + untersuchung.getTrennschleifer() + "\', elektrosaege = \'" + untersuchung.getElektrosaege() + "\', doppelkanister = \'" + untersuchung.getDoppelkanister() + "\', geraetepruefung_allgem = \'" + untersuchung.getGeraetepruefung_allgm() + "\', abstusiset = \'" + untersuchung.getAbstusiset() + "\', rollgliss = \'" + untersuchung.getRollgliss() + "\', verbandsmaterial = \'" + untersuchung.getVerbandsmaterial() + "\', tauchpumpe = \'" + untersuchung.getTauchpumpe() + "\', tauchpumpe2 = \'" + untersuchung.getTauchpumpe2() + "\', hebekissen = \'" + untersuchung.getHebekissen() + "\', luefter = \'" + untersuchung.getLüfter() + "\', luefter2 = \'" + untersuchung.getLüfter2() + "\', pa1 = \'" + untersuchung.getPa1() + "\', pa2 = \'" + untersuchung.getPa2() + "\', pa3 = \'" + untersuchung.getPa3() + "\', pa4 = \'" + untersuchung.getPa4() + "\', pa5 = \'" + untersuchung.getPa5() + "\', pa6 = \'" + untersuchung.getPa6() + "\', infoEMail = \'" + untersuchung.getInfoEMail() + "\' where id = " + untersuchung.getId() + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";";
      logging.logSQL(sql);
      statement.executeUpdate(sql);
   }

   public int getCount(int id) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      logging.logSQL("SELECT count(*) FROM geraetepruefungID where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      ResultSet result = statement.executeQuery("SELECT count(*) FROM geraetepruefungID where id = " + id + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      return result.next()?result.getInt(1):0;
   }

   public Geraetepruefung getData(int fahrzeugID) throws SQLException {
      Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
      ResultSet result = statement.executeQuery("SELECT * FROM geraetepruefungID where id = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      logging.logSQL("SELECT * FROM geraetepruefungID where id = " + fahrzeugID + " and mandantID = " + (String)runApplication.PROPERTIES.get("MandantID") + ";");
      Geraetepruefung geraete = new Geraetepruefung();

      while(result.next()) {
         geraete.setId(result.getInt("id"));
         geraete.setStromerzeuger(result.getString("stromerzeuger"));
         geraete.setSteckleiter(result.getString("steckleiter"));
         geraete.setSchiebleiter(result.getString("schiebleiter"));
         geraete.setMultileiter(result.getString("multileiter"));
         geraete.setHydraulik(result.getString("hydraulik"));
         geraete.setPumpe(result.getString("pumpe"));
         geraete.setKettensaege(result.getString("kettensaege"));
         geraete.setKettensaege2(result.getString("kettensaege2"));
         geraete.setSchnittschutzkleidung(result.getString("schnittschutz"));
         geraete.setSchnittschutzkleidung2(result.getString("schnittschutz2"));
         geraete.setTrennschleifer(result.getString("trennschleifer"));
         geraete.setElektrosaege(result.getString("elektrosaege"));
         geraete.setDoppelkanister(result.getString("doppelkanister"));
         geraete.setGeraetepruefung_allgm(result.getString("geraetepruefung_allgem"));
         geraete.setAbstusiset(result.getString("abstusiset"));
         geraete.setRollgliss(result.getString("rollgliss"));
         geraete.setVerbandsmaterial(result.getString("verbandsmaterial"));
         geraete.setTauchpumpe(result.getString("tauchpumpe"));
         geraete.setTauchpumpe2(result.getString("tauchpumpe2"));
         geraete.setHebekissen(result.getString("hebekissen"));
         geraete.setLüfter(result.getString("luefter"));
         geraete.setLüfter2(result.getString("luefter2"));
         geraete.setPa1(result.getString("pa1"));
         geraete.setPa2(result.getString("pa2"));
         geraete.setPa3(result.getString("pa3"));
         geraete.setPa4(result.getString("pa4"));
         geraete.setPa5(result.getString("pa5"));
         geraete.setPa6(result.getString("pa6"));
      }

      return geraete;
   }
}
