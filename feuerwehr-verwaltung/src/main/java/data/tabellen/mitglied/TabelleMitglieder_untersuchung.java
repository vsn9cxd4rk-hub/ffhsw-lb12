/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.SbcUtils
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder_Untersuchung;
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

public class TabelleMitglieder_untersuchung {
    public void insert(Mitglieder_Untersuchung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitglieder_untersuchung (`id`, `g25`, `g26`, `agttraining`, `infoG25`, `infoG26`, `ablaufLKW`, `infoAblaufLKW`, `ablaufDienstausweis`,`infoAblaufDienstausweis`, `pruefungDerFahrberechtigung`,`infoPruefungDerFahrberechtigung`,`g30`, `infoG30`,`g41`,`g42`, `mandantID`) VALUES ('" + untersuchung.getId() + "', '" + untersuchung.getG25() + "', '" + untersuchung.getG26() + "', '" + untersuchung.getAtemschutztraining() + "', '" + untersuchung.getInfoG25() + "', '" + untersuchung.getInfoG26() + "', '" + untersuchung.getAblaufLKW() + "', '" + untersuchung.getInfoAblaufLKW() + "', '" + untersuchung.getAblaufDienstausweis() + "', '" + untersuchung.getInfoAblaufDienstausweis() + "', '" + untersuchung.getPruefungDerFahrberechtigung() + "', '" + untersuchung.getInfoPruefungDerFahrberechtigung() + "', '" + untersuchung.getG30() + "', '" + untersuchung.getInfoG30() + "', '" + untersuchung.getG41() + "', '" + untersuchung.getG42() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void update(Mitglieder_Untersuchung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set g25 = '" + untersuchung.getG25() + "', g26 = '" + untersuchung.getG26() + "', agttraining = '" + untersuchung.getAtemschutztraining() + "', infoG25 = '" + untersuchung.getInfoG25() + "', infoG26 = '" + untersuchung.getInfoG26() + "', ablaufLKW = '" + untersuchung.getAblaufLKW() + "', infoAblaufLKW = '" + untersuchung.getInfoAblaufLKW() + "', g30 = '" + untersuchung.getG30() + "', infoG30 = '" + untersuchung.getInfoG30() + "', g41 = '" + untersuchung.getG41() + "', g42 = '" + untersuchung.getG42() + "' where id = " + untersuchung.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateDienstausweis(Mitglieder_Untersuchung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set ablaufDienstausweis = '" + untersuchung.getAblaufDienstausweis() + "', infoAblaufDienstausweis = '" + untersuchung.getInfoAblaufDienstausweis() + "', pruefungDerFahrberechtigung = '" + untersuchung.getPruefungDerFahrberechtigung() + "', infoPruefungDerFahrberechtigung = '" + untersuchung.getInfoPruefungDerFahrberechtigung() + "' where id = " + untersuchung.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoG25(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set infoG25 = 1 where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoAblaufLKW(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set infoAblaufLKW = 1 where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoAblaufDienstausweis(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set infoAblaufDienstausweis = 1 where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoPruefungDerFahrberechtigung(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set infoPruefungDerFahrberechtigung = 1 where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoG26(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set infoG26 = 1 where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateInfoG30(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_untersuchung set infoG30 = 1 where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCount(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoStatusG25(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoG25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoG25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoStatusAblaufLKW(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoAblaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoAblaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoStatusAblaufDienstausweis(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoAblaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoAblaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoPruefungDerFahrberechtigung(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoPruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoPruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoStatusG26(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoG26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoG26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getInfoStatusG30(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT infoG30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT infoG30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getG25(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT g25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT g25 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getAblaufLKW(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ablaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT ablaufLKW FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getAblaufDienstausweis(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ablaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT ablaufDienstausweis FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getPruefungDerFahrberechtigung(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT pruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT pruefungDerFahrberechtigung FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getG26(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT g26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT g26 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getG30(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT g30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT g30 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getG41(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT g41 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT g41 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getG42(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT g42 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT g42 FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getAgtTraining(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT agttraining FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT agttraining FROM mitglieder_untersuchung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAbgelaufendeUntersuchungen(String untersuchungsType) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname, mu." + untersuchungsType + " FROM mitglieder m LEFT JOIN mitglieder_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "'and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, mu." + untersuchungsType + " FROM mitglieder m LEFT JOIN mitglieder_untersuchung mu ON m.id = mu.id WHERE " + untersuchungsType + " < '" + SbcUtils.timeStamp((String)"yyyy-MM-dd") + "'and m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
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

    public Vector<Vector<String>> getAllUntersuchungForTable() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d. beschreibung, m.name, m.vorname, u.g25, u.g26, u.g30, u.g41, u.g42, u.ablaufLKW FROM mitglieder m LEFT JOIN mitglieder_untersuchung u ON m.id = u.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname"));
        ResultSet result = statement.executeQuery("SELECT d. beschreibung, m.name, m.vorname, u.g25, u.g26, u.g30, u.g41, u.g42, u.ablaufLKW FROM mitglieder m LEFT JOIN mitglieder_untersuchung u ON m.id = u.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public HashMap<String, String> getAllMitgliederUntersuchungData(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("select * from mitglieder_untersuchung where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("select * from mitglieder_untersuchung where id = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("id", Integer.toString(result.getInt(1)));
            map.put("g25", result.getString(2));
            map.put("g26", result.getString(3));
            map.put("agttraining", result.getString(4));
            map.put("infoG25", result.getString(5));
            map.put("infoG26", result.getString(6));
            map.put("ablaufLKW", result.getString(7));
            map.put("infoAblaufLKW", result.getString(8));
            map.put("ablaufDienstausweis", result.getString(9));
            map.put("infoAblaufDienstausweis", result.getString(10));
            map.put("pruefungDerFahrberechtigung", result.getString(11));
            map.put("infoPruefungDerFahrberechtigung", result.getString(12));
            map.put("g30", result.getString(13));
            map.put("infoG30", result.getString(14));
            map.put("g41", result.getString(15));
            map.put("g42", result.getString(16));
        }
        return map;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("beschreibung"));
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        mitgliederListe.add("D-->" + result.getString("g25"));
        mitgliederListe.add("D-->" + result.getString("g26"));
        mitgliederListe.add("D-->" + result.getString("g30"));
        mitgliederListe.add("D-->" + result.getString("g41"));
        mitgliederListe.add("D-->" + result.getString("g42"));
        mitgliederListe.add("D--!" + result.getString("ablaufLKW"));
        return mitgliederListe;
    }
}

