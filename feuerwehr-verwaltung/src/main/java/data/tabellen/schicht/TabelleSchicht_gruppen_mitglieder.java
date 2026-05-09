/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.schicht;

import data.DatenbankZugriff;
import go.schicht.SchichtGruppenMitglieder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleSchicht_gruppen_mitglieder {
    public void insert(SchichtGruppenMitglieder schichtMitglieder) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO schicht_gruppen_mitglieder (`gruppenID`, `mitgliederID`, `mandantID`) VALUES ('" + schichtMitglieder.getSchichtID() + "', '" + schichtMitglieder.getMitgliederID() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void deleteOne(SchichtGruppenMitglieder schichtMitglieder) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Delete from schicht_gruppen_mitglieder where gruppenID = " + schichtMitglieder.getSchichtID() + " and mitgliederID = " + schichtMitglieder.getMitgliederID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public void deleteGruppe0() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Delete from schicht_gruppen_mitglieder where gruppenID = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public void deleteAlleEinerGruppe(int gruppenID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Delete from schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public void updateGruppe0(int gruppenID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update schicht_gruppen_mitglieder set gruppenID = " + gruppenID + " where gruppenID = 0 and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getMitglederEinerSchichtGruppe(int gruppenID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname FROM schicht_gruppen_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.gruppenID = " + gruppenID + " and sm.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname FROM schicht_gruppen_mitglieder sm LEFT JOIN mitglieder m ON sm.mitgliederID = m.id where sm.gruppenID = " + gruppenID + " and sm.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(String.valueOf(result.getString(1)) + ", " + result.getString(2));
        }
        return liste;
    }

    public int getCountOfMitglieder(int gruppenID, int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT count(*) FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<Integer> getMitglederIDEinerSchichtGruppe(int gruppenID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT mitgliederID FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT mitgliederID FROM schicht_gruppen_mitglieder where gruppenID = " + gruppenID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            liste.add(result.getInt(1));
        }
        return liste;
    }
}

