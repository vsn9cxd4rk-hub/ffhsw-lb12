/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Fahrzeugeinteilung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleFahrzeugeinteilung {
    public void insert(Fahrzeugeinteilung feinteilung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO fahrzeugeinteilung (`id`, `veranstaltungID`, `kategorie`, `jahr`, `mitgliederID`, `fahrzeugID`, `position`, `mandantID`) VALUES ('" + feinteilung.getId() + "', '" + feinteilung.getVeranstaltungID() + "', '" + feinteilung.getKategorie() + "', '" + feinteilung.getJahr() + "', '" + feinteilung.getMitgliederID() + "', '" + feinteilung.getFahrzeugID() + "', '" + feinteilung.getPosition() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void delete(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from fahrzeugeinteilung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void updateKategorie(int kategorieID, int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update fahrzeugeinteilung set kategorie = " + kategorieID + " where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNumer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT max(id) FROM fahrzeugeinteilung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrzeugeinteilung where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public ArrayList<String> getPositionListe(String fahrzeugID, int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.fahrzeugID = " + fahrzeugID + " and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by f.position;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.fahrzeugID = " + fahrzeugID + " and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by f.position;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public ArrayList<String> getEingeteilteKameraden(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT mitgliederID FROM fahrzeugeinteilung WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT mitgliederID FROM fahrzeugeinteilung WHERE veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getCountOfCurrentVehicle(int mitgliederID, int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliederID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliederID + " and fahrzeugID = " + fahrzeugID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfVeranstaltung(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung where veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCountOfPosition(int mitgliedID, int positionID, int jahr, int fahrzeugID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String additionalParameter = "";
        if (fahrzeugID != 0) {
            additionalParameter = "and fahrzeugID = " + fahrzeugID;
        }
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliedID + " and position = " + positionID + " and jahr = " + jahr + " and kategorie = 1 " + additionalParameter + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeugeinteilung where mitgliederID = " + mitgliedID + " and position = " + positionID + " and jahr = " + jahr + " and kategorie = 1 " + additionalParameter + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public Vector<Vector<String>> getFahrzeugBesatzungForTable(int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT fahr.name as fahrzeugname, fahr.trupp, fahr.beschreibung as fahrzeugtyp, f.position, d.beschreibung as dienstgrad, m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN fahrzeuge fahr ON f.fahrzeugID = fahr.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by f.fahrzeugID, f.position;"));
        ResultSet result = statement.executeQuery("SELECT fahr.name as fahrzeugname, fahr.trupp, fahr.beschreibung as fahrzeugtyp, f.position, d.beschreibung as dienstgrad, m.name, m.vorname FROM fahrzeugeinteilung f LEFT JOIN mitglieder m ON f.mitgliederID = m.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id LEFT JOIN fahrzeuge fahr ON f.fahrzeugID = fahr.id WHERE f.veranstaltungID = " + veranstaltungID + " and f.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by f.fahrzeugID, f.position;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> besatzung = new Vector<String>();
        besatzung.add(result.getString("fahrzeugname"));
        if (result.getString("fahrzeugtyp").equals("13") || result.getString("fahrzeugtyp").equals("14")) {
            if (result.getInt("position") == 0) {
                besatzung.add("Transportf\u00fchrer");
            } else if (result.getInt("position") == 1) {
                besatzung.add("Fahrzeugf\u00fchrer");
            } else if (result.getInt("position") == 2) {
                besatzung.add("Sitzplatz 1");
            } else if (result.getInt("position") == 3) {
                besatzung.add("Sitzplatz 2");
            } else {
                besatzung.add("Frei");
            }
        } else if (result.getString("fahrzeugtyp").equals("4") || result.getString("fahrzeugtyp").equals("5")) {
            if (result.getInt("trupp") == 0) {
                if (result.getInt("position") == 0) {
                    besatzung.add("Leiterf\u00fchrer");
                } else if (result.getInt("position") == 1) {
                    besatzung.add("Maschinist");
                } else if (result.getInt("position") == 2) {
                    besatzung.add("Truppmann");
                } else {
                    besatzung.add("Frei");
                }
            } else if (result.getInt("trupp") == 1) {
                if (result.getInt("position") == 2) {
                    besatzung.add("Leiterf\u00fchrer");
                } else if (result.getInt("position") == 1) {
                    besatzung.add("Maschinist");
                } else if (result.getInt("position") == 3) {
                    besatzung.add("Truppmann");
                } else {
                    besatzung.add("Frei");
                }
            }
        } else if (result.getString("fahrzeugtyp").equals("12")) {
            if (result.getInt("position") == 0) {
                besatzung.add("Zugf\u00fchrer");
            } else if (result.getInt("position") == 1) {
                besatzung.add("Fahrer");
            } else if (result.getInt("position") == 2) {
                besatzung.add("Sitzplatz 1");
            } else if (result.getInt("position") == 3) {
                besatzung.add("Sitzplatz 2");
            } else if (result.getInt("position") == 4) {
                besatzung.add("Sitzplatz 3");
            } else if (result.getInt("position") == 5) {
                besatzung.add("Sitzplatz 4");
            } else {
                besatzung.add("Frei");
            }
        } else if (result.getString("fahrzeugtyp").equals("6") | result.getString("fahrzeugtyp").equals("9") | result.getString("fahrzeugtyp").equals("7")) {
            if (result.getInt("position") == 0) {
                besatzung.add("Gruppenf\u00fchrer");
            } else if (result.getInt("position") == 1) {
                besatzung.add("Fahrer");
            } else if (result.getInt("position") == 2) {
                besatzung.add("Sitzplatz 1");
            } else if (result.getInt("position") == 3) {
                besatzung.add("Sitzplatz 2");
            } else if (result.getInt("position") == 4) {
                besatzung.add("Sitzplatz 3");
            } else if (result.getInt("position") == 5) {
                besatzung.add("Sitzplatz 4");
            } else if (result.getInt("position") == 6) {
                besatzung.add("Sitzplatz 5");
            } else if (result.getInt("position") == 7) {
                besatzung.add("Sitzplatz 6");
            } else if (result.getInt("position") == 8) {
                besatzung.add("Sitzplatz 7");
            }
        } else if (result.getInt("trupp") == 0) {
            if (result.getInt("position") == 0) {
                besatzung.add("Gruppenf\u00fchrer");
            } else if (result.getInt("position") == 1) {
                besatzung.add("Maschinist");
            } else if (result.getInt("position") == 2) {
                besatzung.add("Angrifftruppf\u00fchrer");
            } else if (result.getInt("position") == 3) {
                besatzung.add("Angriffstruppmann");
            } else if (result.getInt("position") == 4) {
                besatzung.add("Wassertruppf\u00fchrer");
            } else if (result.getInt("position") == 5) {
                besatzung.add("Wassertruppmann");
            } else if (result.getInt("position") == 6) {
                besatzung.add("Schlauchtruppf\u00fchrer");
            } else if (result.getInt("position") == 7) {
                besatzung.add("Schlauchtruppmann");
            } else if (result.getInt("position") == 8) {
                besatzung.add("Melder");
            }
        } else if (result.getInt("trupp") == 1) {
            if (result.getInt("position") == 2) {
                besatzung.add("Truppf\u00fchrer");
            } else if (result.getInt("position") == 1) {
                besatzung.add("Maschinist");
            } else if (result.getInt("position") == 3) {
                besatzung.add("Truppmann");
            } else {
                besatzung.add("Frei");
            }
        }
        besatzung.add(result.getString("dienstgrad"));
        besatzung.add(String.valueOf(result.getString("name")) + ", " + result.getString("vorname"));
        return besatzung;
    }
}

