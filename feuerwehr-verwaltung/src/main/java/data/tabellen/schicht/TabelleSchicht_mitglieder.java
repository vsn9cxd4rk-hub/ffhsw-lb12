/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.schicht;

import data.DatenbankZugriff;
import go.schicht.SchichtMitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleSchicht_mitglieder {
    public void insert(SchichtMitglieder schichtMitglieder) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO schicht_mitglieder (`schichtID`, `mitgliederID`, `mandantID`) VALUES ('" + schichtMitglieder.getSchichtID() + "', '" + schichtMitglieder.getMitgliederID() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void deleteOne(SchichtMitglieder schichtMitglieder) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Delete from schicht_mitglieder where schichtID = " + schichtMitglieder.getSchichtID() + " and mitgliederID = " + schichtMitglieder.getMitgliederID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public void deleteAlleSchichtMitglieder(int schichtID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Delete from schicht_mitglieder where schichtID = " + schichtID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getMitglederEinerSchicht(int schichtID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM schicht_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.schichtID = " + schichtID + " and sm.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM schicht_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.schichtID = " + schichtID + " and sm.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public int getCountOfMitglieder(int schichtID, int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM schicht_mitglieder where schichtID = " + schichtID + " and mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM schicht_mitglieder where schichtID = " + schichtID + " and mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

