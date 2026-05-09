/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Ausbildung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleAusbildung {
    public void insert(Ausbildung ausbildung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO ausbildung (`id`, `jahr`, `veranstaltungID`,`ausbildungKategorie`, `mitgliederID` , `mandantID`) VALUES ('" + ausbildung.getId() + "', '" + ausbildung.getJahr() + "', '" + ausbildung.getVeranstaltungID() + "', '" + ausbildung.getAusbildungKategorieID() + "', '" + ausbildung.getMitgliederID() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public int getAusbildungStatus(int mitgliederID, int veranstaltungID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM ausbildung where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM ausbildung where mitgliederID = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + "  and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM ausbildung;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM ausbildung;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getStatusFromDatabase(int mitgliederID, int veranstaltungID, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and veranstaltungID = " + veranstaltungID + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getBeteiligungByKategorie(int mitgliederID, int ausbildungKategorie, int jahr) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and ausbildungKategorie = " + ausbildungKategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM `ausbildung` WHERE `mitgliederID` = " + mitgliederID + " and ausbildungKategorie = " + ausbildungKategorie + " and jahr = " + jahr + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

