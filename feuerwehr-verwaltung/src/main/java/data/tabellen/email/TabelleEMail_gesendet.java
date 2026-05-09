/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.email;

import data.DatenbankZugriff;
import go.email.Gesendet;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import logging.logging;
import run.runApplication;

public class TabelleEMail_gesendet {
    public void insert(Gesendet send) throws SQLException {
        String sql = "INSERT INTO email_gesendet (`id`, `an`, `cc`, `bcc`, `betreff`, `nachricht` , `anhang`, `date`, `mandantID`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pstmtQuery = DatenbankZugriff.getInstance().getDbConnection().prepareStatement(sql);
        pstmtQuery.setInt(1, send.getId());
        pstmtQuery.setString(2, send.getAn());
        pstmtQuery.setString(3, send.getCc());
        pstmtQuery.setObject(4, send.getBcc());
        pstmtQuery.setString(5, send.getBetreff());
        pstmtQuery.setString(6, send.getNachricht());
        pstmtQuery.setInt(7, send.getAnhang());
        pstmtQuery.setString(8, send.getDate());
        pstmtQuery.setString(9, runApplication.PROPERTIES.get("MandantID"));
        logging.logSQL((Object)sql);
        pstmtQuery.executeUpdate();
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM email_gesendet where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT max(id) FROM email_gesendet where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public ArrayList<String> getAllSendMails() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("Select * from email_gesendet where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id desc");
        logging.logSQL((Object)("Select * from email_gesendet where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id desc"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add("Nachricht: " + result.getString(1));
            liste.add("An: " + result.getString(2));
            liste.add("Betreff: " + result.getString(5));
            if (result.getInt(7) == 1) {
                liste.add(" --> Anhang <-- ");
            }
            liste.add("-----------------------------");
        }
        return liste;
    }

    public String getNachricht(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT nachricht FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT nachricht FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getBetreff(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT betreff FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT betreff FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getSender(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT an FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT an FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getCC(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT cc FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT cc FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getBCC(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT bcc FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT bcc FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public String getDate(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT date FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT date FROM email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public void deleteNachricht(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from email_gesendet where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }
}

