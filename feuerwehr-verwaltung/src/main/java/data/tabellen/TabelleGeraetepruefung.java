/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Geraetepruefung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleGeraetepruefung {
    public void insert(Geraetepruefung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO geraetepruefung (`id`, `stromerzeuger`, `steckleiter`, `schiebleiter`, `hydraulik`, `pumpe`, `kettensaege`, `doppelkanister`, `geraetepruefung_allgem`, `abstusiset`,`infoEMail`, `mandantID`) VALUES ('" + untersuchung.getId() + "', '" + untersuchung.getStromerzeuger() + "', '" + untersuchung.getSteckleiter() + "', '" + untersuchung.getSchiebleiter() + "', '" + untersuchung.getHydraulik() + "', '" + untersuchung.getPumpe() + "', '" + untersuchung.getKettensaege() + "', '" + untersuchung.getDoppelkanister() + "', '" + untersuchung.getGeraetepruefung_allgm() + "', '" + untersuchung.getAbstusiset() + "', '" + untersuchung.getInfoEMail() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Geraetepruefung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update geraetepruefung set stromerzeuger = '" + untersuchung.getStromerzeuger() + "', steckleiter = '" + untersuchung.getSteckleiter() + "', schiebleiter = '" + untersuchung.getSchiebleiter() + "', hydraulik = '" + untersuchung.getHydraulik() + "', pumpe = '" + untersuchung.getPumpe() + "', kettensaege = '" + untersuchung.getKettensaege() + "', doppelkanister = '" + untersuchung.getDoppelkanister() + "', geraetepruefung_allgem = '" + untersuchung.getGeraetepruefung_allgm() + "', abstusiset = '" + untersuchung.getAbstusiset() + "', infoEMail = '" + untersuchung.getInfoEMail() + "' where id = " + untersuchung.getId() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoEMail(int status) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update geraetepruefung set infoEMail = " + status + " where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCount(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoEMail(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoEMail FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoEMail FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getStromerzeuger(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT stromerzeuger FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT stromerzeuger FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getSteckleiter(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT steckleiter FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT steckleiter FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getSchiebleiter(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT schiebleiter FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT schiebleiter FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getHydraulik(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT hydraulik FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT hydraulik FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getPumpe(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT pumpe FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT pumpe FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getKettensaege(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT kettensaege FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT kettensaege FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getDoppelkanister(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT doppelkanister FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT doppelkanister FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getGer\u00e4tePr\u00fcfungAllgemein(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT geraetepruefung_allgem FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT geraetepruefung_allgem FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getAbstusiset(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT abstusiset FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT abstusiset FROM geraetepruefung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAbgelaufendeUntersuchungen(String untersuchungsType) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, mu." + untersuchungsType + " FROM fahrzeug m LEFT JOIN geraetepruefung mu ON m.id = mu.id WHERE " + untersuchungsType + " < '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT m.name, mu." + untersuchungsType + " FROM fahrzeug m LEFT JOIN geraetepruefung mu ON m.id = mu.id WHERE " + untersuchungsType + " < '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (!result.getString(3).equals("")) {
                liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2) + " " + TimeCalculation.parseShortDateForGUI(result.getString(3)));
                continue;
            }
            liste.add("");
        }
        return liste;
    }

    public Geraetepruefung getData(int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM geraetepruefung where id = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT * FROM geraetepruefung where id = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        Geraetepruefung geraete = new Geraetepruefung();
        while (result.next()) {
            geraete.setId(result.getInt("id"));
            geraete.setStromerzeuger(result.getString("stromerzeuger"));
            geraete.setSteckleiter(result.getString("steckleiter"));
            geraete.setSchiebleiter(result.getString("schiebleiter"));
            geraete.setHydraulik(result.getString("hydraulik"));
            geraete.setPumpe(result.getString("pumpe"));
            geraete.setKettensaege(result.getString("kettensaege"));
            geraete.setDoppelkanister(result.getString("doppelkanister"));
            geraete.setGeraetepruefung_allgm(result.getString("geraetepruefung_allgem"));
            geraete.setAbstusiset(result.getString("abstusiset"));
        }
        return geraete;
    }
}

