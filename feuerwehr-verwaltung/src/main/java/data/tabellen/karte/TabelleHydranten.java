/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.karte;

import data.DatenbankZugriff;
import go.Hydrant;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import logging.logging;

public class TabelleHydranten {
    public Integer getNextIndex() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) from karte_hydranten");
        logging.logSQL((Object)"SELECT max(id) from karte_hydranten");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return null;
    }

    public void insert(Hydrant hydrant) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO karte_hydranten (`id`, `starssenid`, `hausnummer`, `nennweite`) VALUES ('" + hydrant.getId() + "', '" + hydrant.getStrassenid() + "', '" + hydrant.getHausnummer() + "', '" + hydrant.getNennweite() + "');";
        statement.executeUpdate(sql);
    }

    public List<Hydrant> select(String strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT h.hausnummer, h.nennweite FROM karte_hydranten h left join karte_strassen s ON h.starssenid = s.id where s.name = '" + strasse + "' order by h.id");
        logging.logSQL((Object)("SELECT h.hausnummer, h.nennweite FROM karte_hydranten h left join karte_strassen s ON h.starssenid = s.id where s.name = '" + strasse + "' order by h.id"));
        ArrayList<Hydrant> liste = new ArrayList<Hydrant>();
        while (result.next()) {
            liste.add(this.mapToObject(result));
        }
        return liste;
    }

    private Hydrant mapToObject(ResultSet result) throws SQLException {
        Hydrant h = new Hydrant();
        h.setHausnummer(result.getString(1));
        h.setNennweite(result.getInt(2));
        return h;
    }

    public void deleteLastEntry() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        int hyid = this.getNextIndex() - 1;
        String sql = "delete from karte_hydranten where id = " + hyid + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }
}

