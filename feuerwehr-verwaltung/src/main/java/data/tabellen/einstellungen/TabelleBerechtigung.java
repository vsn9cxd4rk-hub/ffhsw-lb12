/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.einstellungen;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleBerechtigung {
    public ArrayList<String> getBerechtigungName(int seite) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name from berechtigung where seite = " + seite + " order by id;");
        logging.logSQL((Object)("SELECT name from berechtigung where seite = " + seite + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getBerechtigungIDs(int seite) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id from berechtigung where seite = " + seite + " order by id;");
        logging.logSQL((Object)("SELECT id from berechtigung where seite = " + seite + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add("BR" + result.getString(1));
        }
        return liste;
    }

    public ArrayList<String> getBerechtigungGruppe(int seite) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT gruppe from berechtigung where seite = " + seite + " order by id;");
        logging.logSQL((Object)("SELECT gruppe from berechtigung where seite = " + seite + " order by id;"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getCount(int seite) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM berechtigung where seite = " + seite + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM berechtigung where seite = " + seite + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

