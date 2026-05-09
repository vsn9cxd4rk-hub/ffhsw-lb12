/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  logging.logging
 */
package data.tabellen.email;

import data.DatenbankZugriff;
import go.email.Entwurf;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import logging.logging;
import run.runApplication;

public class TabelleEMail_entwurf {
    public void insert(Entwurf send) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "INSERT INTO email_entwurf (`id`, `an`, `cc`, `bcc`, `betreff`, `nachricht` , `anhang`, `date`, `mandantID`) VALUES ('" + send.getId() + "', '" + send.getAn() + "', '" + send.getCc() + "', '" + send.getBcc() + "', '" + send.getBetreff() + "', '" + send.getNachricht() + "', '" + send.getAnhang() + "', '" + send.getDate() + "', '" + runApplication.PROPERTIES.get("MandantID") + "');";
        logging.logSQL((Object)sql);
        statement.executeUpdate(sql);
    }

    public int getNextNummer() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT max(id) FROM email_entwurf where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT max(id) FROM email_entwurf where mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getInt(1) + 1;
        }
        return 0;
    }

    public void deleteNachricht(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        String sql = "delete from email_entwurf where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";";
        statement.executeUpdate(sql);
    }

    public ArrayList<String> getAllEntwurfMails() throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("Select * from email_entwurf where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id desc");
        logging.logSQL((Object)("Select * from email_entwurf where mandantID = " + runApplication.PROPERTIES.get("MandantID") + " order by id desc"));
        ArrayList<String> liste = new ArrayList<String>();
        while (result.next()) {
            liste.add("Nachricht: " + result.getString(1));
            liste.add("An: " + result.getString(2));
            liste.add("Betreff: " + result.getString(5));
            if (!result.getString(7).equals("")) {
                liste.add(" --> Anhang <-- ");
            }
            liste.add("-----------------------------");
        }
        return liste;
    }

    public String getNachricht(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        ResultSet result = statement.executeQuery("SELECT nachricht FROM email_entwurf where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        logging.logSQL((Object)("SELECT nachricht FROM email_entwurf where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        if (result.next()) {
            return result.getString(1);
        }
        return null;
    }

    public HashMap<String, String> getEntwurfMail(int id) throws SQLException {
        Statement statement = DatenbankZugriff.getInstance().getDbConnection().createStatement();
        logging.logSQL((Object)("SELECT * FROM email_entwurf where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";"));
        ResultSet result = statement.executeQuery("SELECT * FROM email_entwurf where id = " + id + " and mandantID = " + runApplication.PROPERTIES.get("MandantID") + ";");
        HashMap<String, String> map = new HashMap<String, String>();
        logging.logInfo((Object)"Liste der Einstellungen:");
        while (result.next()) {
            map.put("ID", result.getString(1));
            map.put("AN", result.getString(2));
            map.put("CC", result.getString(3));
            map.put("BCC", result.getString(4));
            map.put("betreff", result.getString(5));
            map.put("nachricht", result.getString(6));
            map.put("anhang", result.getString(7));
            map.put("date", result.getString(8));
        }
        return map;
    }
}

