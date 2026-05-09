/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package run.update;

import data.DatenbankZugriff;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class UpdateDatenbank {
    public void executeSql(String sql) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public ArrayList<String> executeSqlWithReturn(String sql) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public int executeSqlWithReturnINT(String sql) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String executeSqlWithReturnString(String sql) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)sql);
        ResultSet result = statement.executeQuery(sql);
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }
}

