/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.StatistikLehrgang;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import run.runApplication;

public class TabelleStatistikLehrgang {
    public void insert(StatistikLehrgang statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO statistiklehrgang (`id`, `mitgliederID`, `jahr`, `lehrgangID`, `dauer` , `mandantID`) VALUES ('" + statistik.getId() + "', '" + statistik.getMitgliederID() + "', '" + statistik.getJahr() + "', '" + statistik.getLehrgangID() + "', '" + statistik.getDauer() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(StatistikLehrgang statistik) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update statistiklehrgang set jahr = '" + statistik.getJahr() + "', lehrgangID = '" + statistik.getLehrgangID() + "', dauer = '" + statistik.getDauer() + "' where id = " + statistik.getId() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT max(id) FROM statistiklehrgang;");
        ResultSet result = statement.executeQuery("SELECT max(id) FROM statistiklehrgang;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public double getZusammengerechneteDauer(int jahr, int mitgliederID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT sum(dauer) FROM statistiklehrgang where jahr = " + jahr + " and mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT sum(dauer) FROM statistiklehrgang where jahr = " + jahr + " and mitgliederID = " + mitgliederID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getDouble(1);
        }
        return 0.0;
    }
}

