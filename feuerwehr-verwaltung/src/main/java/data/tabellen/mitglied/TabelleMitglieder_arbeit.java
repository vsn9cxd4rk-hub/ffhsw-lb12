/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder_Arbeit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;

public class TabelleMitglieder_arbeit {
    public void insert(Mitglieder_Arbeit arbeit) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitglieder_arbeit (`id`, `name`, `strasse`, `ort`, `telefon`, `ansprechpartner`, `email`, `mandantID`) VALUES ('" + arbeit.getId() + "', '" + arbeit.getName() + "', '" + arbeit.getStrasse() + "', '" + arbeit.getOrt() + "', '" + arbeit.getTelefon() + "', '" + arbeit.getAnsprechpartner() + "', '" + arbeit.getEmail() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Mitglieder_Arbeit arbeit) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_arbeit set name = '" + arbeit.getName() + "', strasse = '" + arbeit.getStrasse() + "', ort = '" + arbeit.getOrt() + "', telefon = '" + arbeit.getTelefon() + "', ansprechpartner = '" + arbeit.getAnsprechpartner() + "', email = '" + arbeit.getEmail() + "' where id = " + arbeit.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCount(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getName(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT name FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT name FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return "";
    }

    public String getStrasse(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT strasse FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT strasse FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getOrt(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ort FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT ort FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getTelefon(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT telefon FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT telefon FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getAnsprechpartner(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT ansprechpartner FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT ansprechpartner FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getEMail(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT email FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT email FROM mitglieder_arbeit where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public Vector<Vector<String>> getAllArbeitgeberForTable() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d. beschreibung, m.name, m.vorname, ar.name as firma, ar.strasse, ar.ort, ar.telefon, ar.ansprechpartner, ar.email FROM mitglieder m LEFT JOIN mitglieder_arbeit ar ON m.id = ar.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and ar.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;"));
        ResultSet result = statement.executeQuery("SELECT d. beschreibung, m.name, m.vorname, ar.name as firma, ar.strasse, ar.ort, ar.telefon, ar.ansprechpartner, ar.email FROM mitglieder m LEFT JOIN mitglieder_arbeit ar ON m.id = ar.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and ar.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname;");
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
        mitgliederListe.add(result.getString("telefon"));
        mitgliederListe.add(result.getString("ansprechpartner"));
        mitgliederListe.add(result.getString("email"));
        return mitgliederListe;
    }
}

