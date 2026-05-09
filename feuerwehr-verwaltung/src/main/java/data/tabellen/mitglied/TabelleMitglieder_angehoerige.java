/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder_Angehoerige;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleMitglieder_angehoerige {
    public void insert(Mitglieder_Angehoerige angehoeriger) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitglieder_angehoerige (`id`, `name`, `strasse`, `ort`, `telefonPrivat`, `telefonMobil`, `email`, `mandantID`) VALUES ('" + angehoeriger.getId() + "', '" + angehoeriger.getName() + "', '" + angehoeriger.getStrasse() + "', '" + angehoeriger.getOrt() + "', '" + angehoeriger.getTelefonPrivat() + "', '" + angehoeriger.getTelefonMobil() + "', '" + angehoeriger.getEmail() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Mitglieder_Angehoerige angehoerige) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_angehoerige set name = '" + angehoerige.getName() + "', strasse = '" + angehoerige.getStrasse() + "', ort = '" + angehoerige.getOrt() + "', telefonPrivat = '" + angehoerige.getTelefonPrivat() + "', telefonMobil = '" + angehoerige.getTelefonMobil() + "', email = '" + angehoerige.getEmail() + "' where id = " + angehoerige.getId() + "' and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCount(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT name FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getStrasse(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT strasse FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT strasse FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getOrt(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ort FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT ort FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getTelefonPrivat(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT telefonPrivat FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT telefonPrivat FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getTelefonMobil(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT telefonMobil FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT telefonMobil FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getEMail(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT email FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT email FROM mitglieder_angehoerige where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public Vector<Vector<String>> getAllAngehoerigeForTable() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d. beschreibung, m.name, m.vorname, ar.name as firma, ar.strasse, ar.ort, ar.telefonPrivat, ar.telefonMobil, ar.email FROM mitglieder m LEFT JOIN mitglieder_angehoerige ar ON m.id = ar.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d. beschreibung, m.name, m.vorname, ar.name as firma, ar.strasse, ar.ort, ar.telefonPrivat, ar.telefonMobil, ar.email FROM mitglieder m LEFT JOIN mitglieder_angehoerige ar ON m.id = ar.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        logging.logSQL(liste);
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("beschreibung"));
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        mitgliederListe.add(result.getString("firma"));
        mitgliederListe.add(result.getString("strasse"));
        mitgliederListe.add(result.getString("ort"));
        mitgliederListe.add(result.getString("telefonPrivat"));
        mitgliederListe.add(result.getString("telefonMobil"));
        mitgliederListe.add(result.getString("email"));
        return mitgliederListe;
    }
}

