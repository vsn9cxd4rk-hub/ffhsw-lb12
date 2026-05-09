/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen;

import data.DatenbankZugriff;
import go.M\u00e4ngelmeldung_kommentar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import logging.logging;
import utilities.TimeCalculation;

public class TabelleMaengelmeldung_kommentar {
    public void insert(M\u00e4ngelmeldung_kommentar mangel) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO maengelmeldung_kommentar (`mangelID`, `kommentarID`, `datum`,`zeit`, `kommentar`, `user`, `mandantID`) VALUES ('" + mangel.getMangelID() + "', '" + mangel.getKommentarID() + "', '" + mangel.getDatum() + "', '" + mangel.getZeit() + "', '" + mangel.getKommentar() + "', '" + mangel.getUser() + "', '" + mangel.getMandantID() + "');";
        statement.executeUpdate(sql);
    }

    public int getNextKommentarNummer(int mangelID, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT max(kommentarID) FROM maengelmeldung_kommentar where mangelID = " + mangelID + " and mandantID = " + mandantID + ";"));
        ResultSet result = statement.executeQuery("SELECT max(kommentarID) FROM maengelmeldung_kommentar where mangelID = " + mangelID + " and mandantID = " + mandantID + ";");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public String getKommentarListe(int mangelID, int mandantID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT datum, zeit, user, kommentar FROM maengelmeldung_kommentar where mangelID = " + mangelID + " and mandantID = " + mandantID + " order by kommentarID;"));
        ResultSet result = statement.executeQuery("SELECT datum, zeit, user, kommentar FROM maengelmeldung_kommentar where mangelID = " + mangelID + " and mandantID = " + mandantID + " order by kommentarID;");
        StringBuilder build = new StringBuilder();
        while (result.next()) {
            build.append("Datum: ");
            build.append(String.valueOf(TimeCalculation.parseDateForGUI(result.getString(1))) + " " + result.getString(2));
            build.append("\n");
            build.append("Kommentar von: " + result.getString(3));
            build.append("\n\n");
            build.append(result.getString(4));
            build.append("\n-----------------------------------------------------------------------------------------------\n\n");
        }
        return build.toString();
    }
}

