/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Fahrzeug_beschreibung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleFahrzeug_beschreibung {
    public void insert(Fahrzeug_beschreibung gruppe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO fahrzeug_beschreibung (`id`, `beschreibung`) VALUES ('" + gruppe.getId() + "', '" + gruppe.getName() + "');";
        statement.executeUpdate(sql);
    }

    public void update(Fahrzeug_beschreibung gruppe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update fahrzeug_beschreibung set beschreibung = '" + gruppe.getName() + "' where id = " + gruppe.getId() + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void delete(String name) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from fahrzeug_beschreibung where beschreibung = '" + name + "';";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM fahrzeug_beschreibung;");
        logging.logSQL((Object)"SELECT max(id) FROM fahrzeug_beschreibung;");
        if (result.next()) {
            if (result.getInt(1) <= 49) {
                return 51;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getFahrzeugGruppenID(String beschreibung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM fahrzeug_beschreibung where beschreibung = '" + beschreibung + "';");
        logging.logSQL((Object)("SELECT count(*) FROM fahrzeug_beschreibung where beschreibung = '" + beschreibung + "';"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getAllFahrzeugBeschreibungen() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT beschreibung FROM fahrzeug_beschreibung order by id;");
        ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeug_beschreibung order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<Integer> getAllFahrzeugBeschreibungenID() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT id FROM fahrzeug_beschreibung order by id;");
        ResultSet result = statement.executeQuery("SELECT id FROM fahrzeug_beschreibung order by id;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }

    public String getBeschreibungName(int beschreibungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT beschreibung FROM fahrzeug_beschreibung where id = " + beschreibungID + ";"));
        ResultSet result = statement.executeQuery("SELECT beschreibung FROM fahrzeug_beschreibung where id = " + beschreibungID + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public int getBeschreibungID(String beschreibungName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT id FROM fahrzeug_beschreibung where beschreibung = '" + beschreibungName + "';"));
        ResultSet result = statement.executeQuery("SELECT id FROM fahrzeug_beschreibung where beschreibung = '" + beschreibungName + "';");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

