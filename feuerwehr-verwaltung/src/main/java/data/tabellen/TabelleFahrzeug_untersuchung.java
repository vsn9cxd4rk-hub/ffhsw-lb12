/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Fahrzeug_Untersuchung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;
import utilities.SbcUtils;
import utilities.TimeCalculation;

public class TabelleFahrzeug_untersuchung {
    public void insert(Fahrzeug_Untersuchung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO fahrzeug_untersuchung (`id`, `sp`, `tuev`, `service`,`gaswartung`, `infoTuev`, `infoSP`, `infoService`, `infoGas`, `mandantID`) VALUES ('" + untersuchung.getId() + "', '" + untersuchung.getSp() + "', '" + untersuchung.getT\u00fcv() + "', '" + untersuchung.getService() + "', '" + untersuchung.getGaswartung() + "', '" + untersuchung.getInfoTuev() + "', '" + untersuchung.getInfoSP() + "', '" + untersuchung.getInfoService() + "', '" + untersuchung.getInfoGas() + "', '" + untersuchung.getMandantID() + "');";
        statement.executeUpdate(sql);
    }

    public void update(Fahrzeug_Untersuchung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update fahrzeug_untersuchung set sp = '" + untersuchung.getSp() + "', tuev = '" + untersuchung.getT\u00fcv() + "', service = '" + untersuchung.getService() + "', gaswartung = '" + untersuchung.getGaswartung() + "', infoTuev = '" + untersuchung.getInfoTuev() + "', infoSP = '" + untersuchung.getInfoSP() + "', infoService = '" + untersuchung.getInfoService() + "', infoGas = '" + untersuchung.getInfoGas() + "' where id = " + untersuchung.getId() + " and mandantID = " + untersuchung.getMandantID() + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoStatus(String spalte, int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update fahrzeug_untersuchung set " + spalte + " = 1 where id = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCount(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeug_untersuchung where id = " + id + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeug_untersuchung where id = " + id + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getSP(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sp FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sp FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getT\u00fcv(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT tuev FROM fahrzeug_untersuchung where id = " + id + ";"));
        ResultSet result = statement.executeQuery("SELECT tuev FROM fahrzeug_untersuchung where id = " + id + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getService(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT service FROM fahrzeug_untersuchung where id = " + id + ";"));
        ResultSet result = statement.executeQuery("SELECT service FROM fahrzeug_untersuchung where id = " + id + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getGasWartung(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT gaswartung FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT gaswartung FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAbgelaufendeUntersuchungen(String untersuchungsType) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, mu." + untersuchungsType + " FROM fahrzeuge m LEFT JOIN fahrzeug_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and ausserDienst = 0 and mu.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT m.name, mu." + untersuchungsType + " FROM fahrzeuge m LEFT JOIN fahrzeug_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "' and ausserDienst = 0 and mu.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (!result.getString(2).equals("")) {
                liste.add(String.valueOf(result.getString(1)) + " " + TimeCalculation.parseShortDateForGUI(result.getString(2)));
                continue;
            }
            liste.add("");
        }
        return liste;
    }

    public int getInfoTuevStatus(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoTuev FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoTuev FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoSPStatus(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoSP FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoSP FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoServiceStatus(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoService FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoService FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoGasStatus(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoGas FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoGas FROM fahrzeug_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public Fahrzeug_Untersuchung getData(int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM fahrzeug_untersuchung where id = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT * FROM fahrzeug_untersuchung where id = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        Fahrzeug_Untersuchung untersuchng = new Fahrzeug_Untersuchung();
        while (result.next()) {
            untersuchng.setId(result.getInt("id"));
            untersuchng.setT\u00fcv(result.getString("tuev"));
            untersuchng.setInfoTuev(result.getInt("infoTuev"));
            untersuchng.setSp(result.getString("sp"));
            untersuchng.setInfoSP(result.getInt("infoSP"));
            untersuchng.setService(result.getString("service"));
            untersuchng.setInfoService(result.getInt("infoService"));
            untersuchng.setGaswartung(result.getString("gaswartung"));
            untersuchng.setInfoGas(result.getInt("infoGas"));
        }
        return untersuchng;
    }
}

