/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.Atemschutzpass_einsatzart;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleAtemschutzpass_einsatzart {
    public void insert(Atemschutzpass_einsatzart einsatzart) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO atemschutzpass_einsatzart (`id`, `name`) VALUES ('" + einsatzart.getId() + "', '" + einsatzart.getName() + "');";
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM atemschutzpass_einsatzart;");
        logging.logSQL((Object)"SELECT max(id) FROM atemschutzpass_einsatzart;");
        if (result.next()) {
            if (result.getInt(1) <= 10) {
                return 11;
            }
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getID(String kategorie) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id FROM atemschutzpass_einsatzart where name = '" + kategorie + "';");
        logging.logSQL((Object)("SELECT id FROM atemschutzpass_einsatzart where name = '" + kategorie + "';"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<String> getAllKategorien() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM atemschutzpass_einsatzart order by id;");
        logging.logSQL((Object)"SELECT name FROM atemschutzpass_einsatzart order by id;");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int getCount(String kategorieName) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM atemschutzpass_einsatzart where name = '" + kategorieName + "';");
        logging.logSQL((Object)("SELECT count(*) FROM atemschutzpass_einsatzart where name = '" + kategorieName + "';"));
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getName(int einsatzart) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name FROM atemschutzpass_einsatzart where id = " + einsatzart + ";");
        logging.logSQL((Object)("SELECT name FROM atemschutzpass_einsatzart where id = " + einsatzart + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }
}

