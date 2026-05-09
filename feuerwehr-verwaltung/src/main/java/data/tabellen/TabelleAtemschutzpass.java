/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Atemschutzpass;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;

public class TabelleAtemschutzpass {
    public void insert(Atemschutzpass atemschutzpass) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO atemschutzpass (`id`, `jahr`, `veranstaltungID`,`veranstaltungKategorie`, `mitgliederID`, `zeit`, `einsatzart`, `truppZuordnung`, `mandantID`) VALUES ('" + atemschutzpass.getId() + "', '" + atemschutzpass.getJahr() + "', '" + atemschutzpass.getVeranstaltungID() + "', '" + atemschutzpass.getVeranstaltungKategorie() + "', '" + atemschutzpass.getMitgliederID() + "', '" + atemschutzpass.getZeit() + "', '" + atemschutzpass.getEinsatzart() + "', '" + atemschutzpass.getTruppZuordnung() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Atemschutzpass atemschutzpass) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update atemschutzpass set zeit = '" + atemschutzpass.getZeit() + "', einsatzart = '" + atemschutzpass.getEinsatzart() + "', truppZuordnung = '" + atemschutzpass.getTruppZuordnung() + "' where veranstaltungID = " + atemschutzpass.getVeranstaltungID() + " and mitgliederID = " + atemschutzpass.getMitgliederID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM atemschutzpass;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM atemschutzpass;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCountByVeranstaltungID(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountByVeranstaltungIDUndMitglied(int veranstaltungID, int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getZeit(int veranstaltungID, int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT zeit FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT zeit FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getTruppZuordnung(int veranstaltungID, int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT truppZuordnung FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT truppZuordnung FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getGesamtZeit(int mitgliedID, int einsatzart, int jahr, int veranstaltungsKategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String zusatzparameter = "";
        zusatzparameter = veranstaltungsKategorie == -1 ? " and veranstaltungKategorie != 1 " : " and veranstaltungKategorie = " + veranstaltungsKategorie;
        logging.logSQL((Object)("SELECT sum(zeit) FROM atemschutzpass where mitgliederID = " + mitgliedID + " and einsatzart = " + einsatzart + " and jahr = " + jahr + zusatzparameter + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(zeit) FROM atemschutzpass where mitgliederID = " + mitgliedID + " and einsatzart = " + einsatzart + " and jahr = " + jahr + zusatzparameter + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getEinsatzart(int veranstaltungID, int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ae.name FROM atemschutzpass a LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id where a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT ae.name FROM atemschutzpass a LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id where a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public Vector<Vector<String>> getEinsaetzeForTable(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT v.datum, e.einsatznummerOffiziell, v.name as veranstaltung, ae.name as einsatzart, a.zeit, m.name, m.vorname FROM atemschutzpass a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id LEFT JOIN einsatz e ON e.veranstaltungID = a.veranstaltungID LEFT JOIN mitglieder m ON m.id = e.einsatzleiter where a.mitgliederID = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;"));
        ResultSet result = statement.executeQuery("SELECT v.datum, e.einsatznummerOffiziell, v.name as veranstaltung, ae.name as einsatzart, a.zeit, m.name, m.vorname FROM atemschutzpass a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id LEFT JOIN einsatz e ON e.veranstaltungID = a.veranstaltungID LEFT JOIN mitglieder m ON m.id = e.einsatzleiter where a.mitgliederID = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    public ArrayList<String> getIDArrayVeransatltung(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT v.name FROM atemschutzpass a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id LEFT JOIN einsatz e ON e.veranstaltungID = a.veranstaltungID LEFT JOIN mitglieder m ON m.id = e.einsatzleiter where a.mitgliederID = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;");
        logging.logSQL((Object)("SELECT v.name FROM atemschutzpass a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id LEFT JOIN einsatz e ON e.veranstaltungID = a.veranstaltungID LEFT JOIN mitglieder m ON m.id = e.einsatzleiter where a.mitgliederID = " + mitgliedID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getMitgliederIDsByVeransatltung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT mitgliederID FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;");
        logging.logSQL((Object)("SELECT mitgliederID FROM atemschutzpass where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id;"));
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public HashMap<String, String> getData(int veranstaltungID, int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT a.id, a.jahr, a.veranstaltungID, a.veranstaltungKategorie, a.mitgliederID, m.name, m.vorname, a.zeit, ae.name as einsatzart, a.truppZuordnung FROM atemschutzpass a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id WHERE a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID = " + mitgliederID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT a.id, a.jahr, a.veranstaltungID, a.veranstaltungKategorie, a.mitgliederID, m.name, m.vorname, a.zeit, ae.name as einsatzart, a.truppZuordnung FROM atemschutzpass a LEFT JOIN mitglieder m ON a.mitgliederID = m.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id WHERE a.veranstaltungID = " + veranstaltungID + " and a.mitgliederID = " + mitgliederID + " and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("id", result.getString("id"));
            map.put("jahr", result.getString("jahr"));
            map.put("veranstaltungID", result.getString("veranstaltungID"));
            map.put("veranstaltungKategorie", result.getString("veranstaltungKategorie"));
            map.put("mitgliederID", result.getString("mitgliederID"));
            map.put("zeit", result.getString("zeit"));
            map.put("einsatzart", result.getString("einsatzart"));
            map.put("truppZuordnung", result.getString("truppZuordnung"));
            map.put("name", String.valueOf(result.getString("name")) + ", " + result.getString("vorname"));
        }
        return map;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> liste = new Vector<String>();
        liste.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
        liste.add(result.getString("einsatznummerOffiziell"));
        if (result.getString("veranstaltung") == null) {
            liste.add("Sonstige \u00dcbung / Lehrgang / Fortbildung");
        } else {
            liste.add(result.getString("veranstaltung"));
        }
        liste.add(result.getString("einsatzart"));
        liste.add(String.valueOf(result.getString("zeit")) + " min.");
        if (result.getString("name") == null && result.getString("vorname") == null) {
            liste.add("");
        } else {
            liste.add(String.valueOf(result.getString("name")) + ", " + result.getString("vorname"));
        }
        return liste;
    }

    public Vector<Vector<String>> getEinsaetzeForTableBYEinsatznummer(String veranstaltungName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT v.datum, e.einsatznummerOffiziell, v.name as veranstaltung, ae.name as einsatzart, agt.name as agtname, agt.vorname as agtvorname, a.zeit, a.truppZuordnung, m.name, m.vorname FROM atemschutzpass a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id LEFT JOIN einsatz e ON e.veranstaltungID = a.veranstaltungID LEFT JOIN mitglieder m ON m.id = e.einsatzleiter LEFT JOIN mitglieder agt ON a.mitgliederID = agt.id where v.name = '" + veranstaltungName + "' and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;"));
        ResultSet result = statement.executeQuery("SELECT v.datum, e.einsatznummerOffiziell, v.name as veranstaltung, ae.name as einsatzart, agt.name as agtname, agt.vorname as agtvorname, a.zeit, a.truppZuordnung, m.name, m.vorname FROM atemschutzpass a LEFT JOIN veranstaltung v ON a.veranstaltungID = v.id LEFT JOIN atemschutzpass_einsatzart ae ON a.einsatzart = ae.id LEFT JOIN einsatz e ON e.veranstaltungID = a.veranstaltungID LEFT JOIN mitglieder m ON m.id = e.einsatzleiter LEFT JOIN mitglieder agt ON a.mitgliederID = agt.id where v.name = '" + veranstaltungName + "' and a.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by v.datum desc, v.zeit desc;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVectorByEinsatznummer(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVectorByEinsatznummer(ResultSet result) throws SQLException {
        Vector<String> liste = new Vector<String>();
        liste.add(TimeCalculation.parseDateForGUI(result.getString("datum")));
        liste.add(result.getString("einsatznummerOffiziell"));
        if (result.getString("veranstaltung") == null) {
            liste.add("Sonstige \u00dcbung / Lehrgang / Fortbildung");
        } else {
            liste.add(result.getString("veranstaltung"));
        }
        liste.add(result.getString("einsatzart"));
        liste.add(String.valueOf(result.getString("agtname")) + ", " + result.getString("agtvorname"));
        liste.add(String.valueOf(result.getString("zeit")) + " min.");
        if (result.getInt("truppZuordnung") == 0) {
            liste.add("keine Zuordnung");
        } else {
            liste.add(String.valueOf(result.getString("truppZuordnung")) + ". Trupp");
        }
        if (result.getString("name") == null && result.getString("vorname") == null) {
            liste.add("");
        } else {
            liste.add(String.valueOf(result.getString("name")) + ", " + result.getString("vorname"));
        }
        return liste;
    }
}

