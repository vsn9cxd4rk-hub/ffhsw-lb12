/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder_verfuegbarkeit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleMitglieder_verfuegbarkeit {
    public void insert(Mitglieder_verfuegbarkeit verfuegbarkeit) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitglieder_verfuegbarkeit (`id`, `mitgliedID`, `telegrammID`, `status`, `mandantID`) VALUES ('" + verfuegbarkeit.getId() + "', '" + verfuegbarkeit.getMitgliedID() + "', '" + verfuegbarkeit.getTelegrammID() + "', '" + verfuegbarkeit.getStatus() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public void update(Mitglieder_verfuegbarkeit verfuegbarkeit) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "update mitglieder_verfuegbarkeit set telegrammID = '" + verfuegbarkeit.getTelegrammID() + "', status = " + verfuegbarkeit.getStatus() + " where mitgliedID = " + verfuegbarkeit.getMitgliedID() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT max(id) FROM mitglieder_verfuegbarkeit where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT max(id) FROM mitglieder_verfuegbarkeit where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getStatus(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT status FROM mitglieder_verfuegbarkeit where mitgliedID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT status FROM mitglieder_verfuegbarkeit where mitgliedID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public int getCount(int mitgliedID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_verfuegbarkeit where mitgliedID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_verfuegbarkeit where mitgliedID = " + mitgliedID + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public ArrayList<Integer> getAlleVerfuegbarkeiten() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)"SELECT mv.status FROM mitglieder m LEFT JOIN mitglieder_verfuegbarkeit mv ON m.id = mv.mitgliedID where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mandantID = 1 and m.mitgliederGruppe = 1 order by m.name, m.vorname;");
        ResultSet result = statement.executeQuery("SELECT mv.status FROM mitglieder m LEFT JOIN mitglieder_verfuegbarkeit mv ON m.id = mv.mitgliedID where m.ausserDienst = 0  and m.loeschkenner = 0 and m.mandantID = 1 and m.mitgliederGruppe = 1 order by m.name, m.vorname;");
        ArrayList<Integer> liste = new ArrayList<Integer>();
        while (result.next()) {
            if (result.getString(1) == null) {
                liste.add(-1);
                continue;
            }
            liste.add(result.getInt(1));
        }
        return liste;
    }
}

