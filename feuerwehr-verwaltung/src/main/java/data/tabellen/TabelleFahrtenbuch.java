/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import data.tabellen.mitglied.TabelleMitglied;
import go.Fahrtenbuch;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.TimeCalculation;

public class TabelleFahrtenbuch {
    public void insert(Fahrtenbuch fahrtenbuch) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO fahrtenbuch (`id`, `fahrzeugID`, `veranstaltungID`, `datumVon`, `zeitVon`, `datumBis`, `zeitBis`, `kmBeginn`, `kmEnde`, `distance`, `tanken`, `pumpenbetrieb`, `sonstiges`, `fahrer`, `mandantID`) VALUES ('" + fahrtenbuch.getId() + "', '" + fahrtenbuch.getFahrzeugID() + "', '" + fahrtenbuch.getVeranstaltungID() + "', '" + fahrtenbuch.getDatumVon() + "', '" + fahrtenbuch.getZeitVon() + "', '" + fahrtenbuch.getDatumBis() + "', '" + fahrtenbuch.getZeitBis() + "', '" + fahrtenbuch.getKmBeginn() + "', '" + fahrtenbuch.getKmEnde() + "', '" + fahrtenbuch.getDistance() + "', '" + fahrtenbuch.getTanken() + "', '" + fahrtenbuch.getPumpenbetrieb() + "', '" + fahrtenbuch.getSonstiges() + "', '" + fahrtenbuch.getFahrer() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Fahrtenbuch fahrtenbuch) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update fahrtenbuch set kmBeginn = '" + fahrtenbuch.getKmBeginn() + "', kmEnde = '" + fahrtenbuch.getKmEnde() + "', distance = '" + fahrtenbuch.getDistance() + "', tanken = '" + fahrtenbuch.getTanken() + "', pumpenbetrieb = '" + fahrtenbuch.getPumpenbetrieb() + "', sonstiges = '" + fahrtenbuch.getSonstiges() + "', fahrer = '" + fahrtenbuch.getFahrer() + "' where veranstaltungID = " + fahrtenbuch.getVeranstaltungID() + " and fahrzeugID = " + fahrtenbuch.getFahrzeugID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrtenbuch;");
        logging.logSQL((Object)"SELECT max(id) FROM fahrtenbuch;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getLastEntryOfVehicle(int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrtenbuch where fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT max(id) FROM fahrtenbuch where fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getLastKmStand(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT kmEnde FROM fahrtenbuch where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT kmEnde FROM fahrtenbuch where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountVeranstaltungID(int veranstaltungID, int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrtenbuch where veranstaltungID = " + veranstaltungID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM fahrtenbuch where veranstaltungID = " + veranstaltungID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public HashMap<String, String> getData(int veranstaltungID, int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT * from fahrtenbuch where veranstaltungID = " + veranstaltungID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT * from fahrtenbuch where veranstaltungID = " + veranstaltungID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<String, String> map = new HashMap<String, String>();
        while (result.next()) {
            map.put("fahrer", String.valueOf(new TabelleMitglied().getName(result.getInt("fahrer"))) + ", " + new TabelleMitglied().getVorname(result.getInt("fahrer")));
            map.put("kmBeginn", Integer.toString(result.getInt("kmBeginn")));
            map.put("kmEnde", Integer.toString(result.getInt("kmEnde")));
            map.put("tanken", result.getString("tanken"));
            map.put("pumpenbetrieb", result.getString("pumpenbetrieb"));
            map.put("sonstiges", result.getString("sonstiges"));
        }
        return map;
    }

    public Vector<Vector<String>> getFahrtenbuchForTable(int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT v.name2 as veranstaltungName, fb.datumVon, fb.zeitVon, fb.zeitBis, fb.kmBeginn, fb.kmEnde, fb.distance, fb.tanken, fb.pumpenbetrieb, fb.sonstiges, m.name as nameFahrer, m.vorname as vornameFahrer  from fahrtenbuch fb LEFT JOIN veranstaltung v ON fb.veranstaltungID = v.id LEFT JOIN mitglieder m ON fb.fahrer = m.id where fb.fahrzeugID = " + fahrzeugID + " and fb.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by fb.datumVon desc, fb.zeitVon desc;"));
        ResultSet result = statement.executeQuery("SELECT v.name2 as veranstaltungName, fb.datumVon, fb.zeitVon, fb.zeitBis, fb.kmBeginn, fb.kmEnde, fb.distance, fb.tanken, fb.pumpenbetrieb, fb.sonstiges, m.name as nameFahrer, m.vorname as vornameFahrer  from fahrtenbuch fb LEFT JOIN veranstaltung v ON fb.veranstaltungID = v.id LEFT JOIN mitglieder m ON fb.fahrer = m.id where fb.fahrzeugID = " + fahrzeugID + " and fb.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by fb.datumVon desc, fb.zeitVon desc;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("veranstaltungName"));
        mitgliederListe.add(TimeCalculation.parseDateForGUI(result.getString("datumVon")));
        mitgliederListe.add(result.getString("zeitVon"));
        mitgliederListe.add(result.getString("zeitBis"));
        mitgliederListe.add(result.getString("kmBeginn"));
        mitgliederListe.add(result.getString("kmEnde"));
        mitgliederListe.add(result.getString("distance"));
        mitgliederListe.add(result.getString("tanken"));
        mitgliederListe.add(result.getString("pumpenbetrieb"));
        mitgliederListe.add(result.getString("sonstiges"));
        mitgliederListe.add(String.valueOf(result.getString("nameFahrer")) + ", " + result.getString("vornameFahrer"));
        return mitgliederListe;
    }
}

