/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.karte;

import data.DatenbankZugriff;
import go.Stra\u00dfe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;

public class TabelleStrassen {
    public ArrayList<String> getStra\u00dfenListe() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name from karte_strassen order by name");
        logging.logSQL((Object)"SELECT name from karte_strassen order by name");
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add(result.getString(1));
        }
        return liste;
    }

    public Integer getStrassenCount(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT count(*) from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getInt(1);
        }
        return null;
    }

    public Integer getStrassenNumber(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT id from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT id from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getInt(1);
        }
        return null;
    }

    public String getStrassenName(int strassenID) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT name from karte_strassen where id ='" + strassenID + "'");
        logging.logSQL((Object)("SELECT name from karte_strassen where id ='" + strassenID + "'"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getStrassenBild(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT bild from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT bild from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getStrassenBild2(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT bild2 from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT bild2 from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getAnfahrtInfo(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT anfahrt from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT anfahrt from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getStra\u00dfenInfo(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT info from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT info from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getStrassenKoordinaten(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT koordinaten from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT koordinaten from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getPLZ(String Strasse) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT plz from karte_strassen where name ='" + Strasse + "'");
        logging.logSQL((Object)("SELECT plz from karte_strassen where name ='" + Strasse + "'"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public void insert(Stra\u00dfe stra\u00dfe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO karte_strassen (`id`, `name`, `bild`, `bild2`, `anfahrt`, `info`, `koordinaten`, `PLZ`) VALUES ('" + stra\u00dfe.getId() + "', '" + stra\u00dfe.getName() + "', '" + stra\u00dfe.getBild() + "', '" + stra\u00dfe.getBild2() + "', '" + stra\u00dfe.getAnfahrt() + "', '" + stra\u00dfe.getInfo() + "', '" + stra\u00dfe.getKoordinaten() + "', '" + stra\u00dfe.getPLZ() + "');";
        statement.executeUpdate(sql);
    }

    public void update(Stra\u00dfe stra\u00dfe) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update `karte_strassen` set `anfahrt` = '" + stra\u00dfe.getAnfahrt() + "', `info` = '" + stra\u00dfe.getInfo() + "', `koordinaten` = '" + stra\u00dfe.getKoordinaten() + "', `bild` = '" + stra\u00dfe.getBild() + "', `bild2` = '" + stra\u00dfe.getBild2() + "' where `id` = " + stra\u00dfe.getId() + ";";
        logging.logSQL((Object)("Update karte_strassen set anfahrt = '" + stra\u00dfe.getAnfahrt() + "', info = '" + stra\u00dfe.getInfo() + "', koordinaten = '" + stra\u00dfe.getKoordinaten() + "', `bild` = '" + stra\u00dfe.getBild() + "', `bild2` = '" + stra\u00dfe.getBild2() + "' where id = " + stra\u00dfe.getId() + ";"));
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM karte_strassen;");
        logging.logSQL((Object)"SELECT max(id) FROM karte_strassen;");
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public int getCount() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT count(*) FROM karte_strassen;");
        logging.logSQL((Object)"SELECT count(*) FROM karte_strassen;");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }
}

