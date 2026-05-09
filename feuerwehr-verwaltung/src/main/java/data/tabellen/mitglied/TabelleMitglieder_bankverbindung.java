/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 *  utilities.hash
 */
package data.tabellen.mitglied;

import data.DatenbankZugriff;
import go.Mitglieder_Bankverbindung;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import logging.logging;
import run.runApplication;
import utilities.hash;

public class TabelleMitglieder_bankverbindung {
    public void insert(Mitglieder_Bankverbindung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO mitglieder_bankverbindung (`id`, `iban`, `bic`, `mandantID`) VALUES ('" + untersuchung.getId() + "', '" + untersuchung.getIban() + "', '" + untersuchung.getBic() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        statement.executeUpdate(sql);
    }

    public void update(Mitglieder_Bankverbindung untersuchung) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "Update mitglieder_bankverbindung set iban = '" + untersuchung.getIban() + "', bic = '" + untersuchung.getBic() + "' where id = " + untersuchung.getId() + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getCount(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT count(*) FROM mitglieder_bankverbindung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT count(*) FROM mitglieder_bankverbindung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getInt(1);
        }
        return 0;
    }

    public String getiban(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT iban FROM mitglieder_bankverbindung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT iban FROM mitglieder_bankverbindung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getbic(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT bic FROM mitglieder_bankverbindung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT bic FROM mitglieder_bankverbindung where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public Vector<Vector<String>> getAllBankverbindungenForTable() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT d. beschreibung, m.name, m.vorname, u.iban, u.bic FROM mitglieder m LEFT JOIN mitglieder_bankverbindung u ON m.id = u.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname"));
        ResultSet result = statement.executeQuery("SELECT d. beschreibung, m.name, m.vorname, u.iban, u.bic FROM mitglieder m LEFT JOIN mitglieder_bankverbindung u ON m.id = u.id LEFT JOIN dienstgrad d ON m.dienstgrad = d.id where m.mitgliederGruppe = 1 and m.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " and d.mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by m.name, m.vorname");
        Vector<Vector<String>> liste = new Vector<Vector<String>>();
        while (result.next()) {
            liste.add(this.mapResultSetToVector(result));
        }
        return liste;
    }

    private Vector<String> mapResultSetToVector(ResultSet result) throws SQLException {
        Vector<String> mitgliederListe = new Vector<String>();
        mitgliederListe.add(result.getString("beschreibung"));
        mitgliederListe.add(result.getString("name"));
        mitgliederListe.add(result.getString("vorname"));
        try {
            mitgliederListe.add(hash.decodeHashCode((String)result.getString("iban")));
            mitgliederListe.add(hash.decodeHashCode((String)result.getString("bic")));
        }
        catch (Exception e) {
            mitgliederListe.add(" ");
            mitgliederListe.add(" ");
        }
        return mitgliederListe;
    }
}

