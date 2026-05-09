/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Brandsicherheitswachen_temp;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleBrandsicherheitswachen_temp {
    public void insert(Brandsicherheitswachen_temp temp) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO brandsicherheitswachen_temp (`mitgliederID`, `beteiligung`, `mandantID`) VALUES ('" + temp.getMitgliederID() + "', '" + temp.getBeteiligung() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void deleteAll() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from brandsicherheitswachen_temp where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getListOfBeteiligung() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT m.name, m.vorname, bt.beteiligung FROM brandsicherheitswachen_temp bt LEFT JOIN mitglieder m ON bt.mitgliederID = m.id where bt.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by bt.beteiligung asc;"));
        ResultSet result = statement.executeQuery("SELECT m.name, m.vorname, bt.beteiligung FROM brandsicherheitswachen_temp bt LEFT JOIN mitglieder m ON bt.mitgliederID = m.id where bt.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by bt.beteiligung asc;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            if (result.getRow() > 15) continue;
            liste.add(String.valueOf(result.getRow()) + ". Position:  " + result.getString(1) + ", " + result.getString(2) + " (" + result.getString(3) + "xMal)");
        }
        return liste;
    }
}

